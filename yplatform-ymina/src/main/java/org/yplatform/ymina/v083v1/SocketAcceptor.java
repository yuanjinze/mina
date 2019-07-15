package org.yplatform.ymina.v083v1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author yuanjinze
 *
 */
public class SocketAcceptor extends BaseSessionManager implements IoAcceptor {
	private final IoSessionManagerFilterChain filters = new SocketSessionManagerFilterChain();

	private int backlog = 50;// 应该在配置文件里面配置,能够接受客户端连接的最大个数

	private final Queue registerQueue = new Queue();// 将每次客户端来的请求参数封装成一个request对象，并放到这个队列里面去，这里没有进行去重的处理。

	private final Map channels = new HashMap();
	private Selector selector;// java nio的知识。

	private Worker worker;
	// java 语言提供的弱同步机制，volatile变量的更新操作通知到其他线程，保证了新值能立即同步到主内存，
	// 以及每次使用前立即从主内存刷新，当把变量声明为volatile类型后，编译器与运行时都会注意到这个变量是共享的。
	private static volatile int nextId = 0;// volatile 关键字是与Java的内存模型相关的
	private final int id = nextId++;

	private final IoHandler handler = new DefaultIoHandler();

	public SocketAcceptor() {
	}

	@Override
	public void bind(SocketAddress address) throws IOException {
		if (address == null)
			throw new NullPointerException("address");

		if (!(address instanceof InetSocketAddress))
			throw new IllegalArgumentException("Unexcepted address type: " + address.getClass());

		RegistrationRequest request = new RegistrationRequest(address, backlog);

		// 因为允许多个线程调用该方法，故对下面的资源进行同步保护
		// step1:
		synchronized (this) {
			synchronized (registerQueue) {
				registerQueue.push(request);// 这里没有进行去重，直接push进队列（队列里面其实就是个Object[]）
			}
			startupWorker();// 启动一个worker线程，并运行它的run方法
		}

		// step2:
		selector.wakeup();// 用来唤醒阻塞在select（）方法上的线程

		synchronized (request) {
			while (!request.done) {
				try {
					request.wait();// 主线程等待，但是将request对象锁释放
				} catch (InterruptedException e) {

				}
			}
		}

				
		System.out.println("是否往下走了。即打印我。。。。");

		if (request.exception != null)
			throw request.exception;
		
		

	}

	/**
	 * 服务端用一个线程来处理所有客户的链接处理
	 * 
	 * @throws IOException
	 */
	private synchronized void startupWorker() throws IOException {
		// 即只用一个线程来处理所有的通道
		if (worker == null) {// 判断这个线程是否启动了。（在当前对象实例里面只启动一个worker线程）
			selector = Selector.open();//yjz ,注意这里其实发生了很多事情: WindowsSelectorProvider#openSelector()

			worker = new Worker();
			worker.start();
		}

	}

	private static class RegistrationRequest {
		private final SocketAddress address;
		private int backlog;
		private boolean done;// 是否完成

		private IOException exception;// 保存Worker线程里面出来的异常

		public RegistrationRequest(SocketAddress address, int backlog) {
			this.address = address;
			this.backlog = backlog;
		}
	}

	// 就像有产生了一个将军，自己可以独立带队进行打仗了。
	private class Worker extends Thread {
		// 为了以后看jvm里面的情况，需要给这个线程一个名称.
		public Worker() {
			super("SocketAcceptor(worker)-" + id);
		}

		private void registerNew() {
			if (registerQueue.isEmpty())
				return;

			for (;;) {
				RegistrationRequest req;
				synchronized (registerQueue) {
					req = (RegistrationRequest) registerQueue.pop();
				}

				if (req == null)
					break;

				ServerSocketChannel ssc = null;
				try {
					ssc = ServerSocketChannel.open();
					ssc.configureBlocking(false);// 这儿启用非阻塞，即在ssc.accept()方法上不阻塞
					ssc.socket().bind(req.address, req.backlog);
					ssc.register(selector, SelectionKey.OP_ACCEPT, req);// 将Selector选择器注册进来，在Java
																		// NIO
					// 中能够检查一到多个NIO通道，并能够知晓通道是否为读写事情做好准备的，目前只注册OP_ACCEPT类型事件进行观察。

					channels.put(req.address, ssc);// 保存地址和通道的对应关系
				} catch (IOException e) {
					req.exception = e;
				} finally {
					synchronized (req) {
						req.done = true;
						req.notify();// 通知其他线程，这儿是通知主线程
					}

					if (ssc != null && req.exception != null) {
						try {
							ssc.close();
						} catch (IOException e) {
							exceptionMonitor.exceptionCaught(this, e);
						}
					}
				}
			}
		}

		/**
		 * Selector检查到有一个客户端连接过来，就创建一个SocketSession，
		 * 并把这个SocketSession添加到SocketIoProcess上去.
		 * 
		 * @param keys
		 * @throws IOException
		 */
		private void processSessions(Set keys) throws IOException {
			Iterator it = keys.iterator();
			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();
				it.remove();// 取出一个，然后就从集合里面移除这个对象

				if (!key.isAcceptable()) {
					continue;
				}

				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel ch = ssc.accept();
				if (ch == null) {
					continue;
				}

				boolean success = false;
				try {
					RegistrationRequest req = (RegistrationRequest) key.attachment();
					// TODO yjz process 多线程
					System.out.println("处理数据信息.......");
					SocketSession session = new SocketSession(filters, ch, handler);
					SocketIoProcessor.getInstance().addSession(session);
					success = true;
				} catch (Throwable t) {
					exceptionMonitor.exceptionCaught(SocketAcceptor.this, t);
				} finally {
					if (!success)
						ch.close();
				}

			}
		}

		@Override
		public void run() {
			for (;;) {

				try {
					System.out.println("begin run 方法....." + new Date(System.currentTimeMillis()));
					int nKeys = selector.select();// 这儿，如果没有其它的线程进行调wakeup的话就一直阻塞在这里.

					registerNew();// 对应一个bind的请求则在address上创建一个对应的通道(即创建一个ServerSocketChannel)

					if (nKeys > 0) {
						processSessions(selector.selectedKeys());
					}

					System.out.println("end run 方法....." + new Date(System.currentTimeMillis()));
				} catch (Exception e) {
					// 因为run方法没有什么检查异常，所有我们在run方法里面抛出的异常我们要进行保存。（故此，我们就应该考虑一个异常收集的框架.）
					// SessionManager--> BaseSessionManager
					exceptionMonitor.exceptionCaught(SocketAcceptor.this, e);

					try {
						Thread.sleep(1000);// 休息1秒，就是不要急着进入下一次循环
					} catch (InterruptedException e1) {
					}
				}
			}
		}

	}

	@Override
	public IoFilterChain getFilterChain() {
		return this.filters;
	}

}
