/**
*
*/
package org.yplatform.ymina.v083v1;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuanjinze
 *
 */
public class SocketIoProcessor {

	private static final Logger log = LoggerFactory.getLogger(SocketIoProcessor.class);

	private static final SocketIoProcessor instance;

	private final Selector selector;
	private final Queue newSessions = new Queue();
	private Worker worker;

	static {
		SocketIoProcessor tmp;
		try {
			tmp = new SocketIoProcessor();
		} catch (IOException e) {
			InternalError error = new InternalError("Failed to open selector");
			error.initCause(e);
			throw error;
		}
		instance = tmp;
	}

	private SocketIoProcessor() throws IOException {
		selector = Selector.open();// 又开了一个Selector
	}

	static SocketIoProcessor getInstance() {
		return instance;
	}

	void addSession(SocketSession session) {
		synchronized (this) {
			synchronized (newSessions) {
				newSessions.push(session);
			}
			startupWorker();
		}
		selector.wakeup();
	}

	/**
	 * 利用一个单线程来处理所有的SocketSession信息
	 */
	private synchronized void startupWorker() {
		if (worker == null) {// 说明只起了一个线程进行处理
			worker = new Worker();
			worker.start();
		}
	}

	private void addSessions() {
		if (newSessions.isEmpty())
			return;

		SocketSession session;
		for (;;) {
			synchronized (newSessions) {
				session = (SocketSession) newSessions.pop();
			}
			if (session == null)
				break;

			SocketChannel ch = session.getChannel();
			boolean registered;
			try {
				ch.configureBlocking(false);
				SelectionKey key = ch.register(selector, SelectionKey.OP_READ, session);
				session.setSelectionKey(key);
				registered = true;
			} catch (IOException e) {
				registered = false;
				// 在责任链里面对异常进行捕获
				log.warn("Unexcepted exception", e);
			}

			if (registered) {
				// 责任链里面打开session方法
			}

		}
	}

	private void processSessions(Set selectedKeys) {
		Iterator it = selectedKeys.iterator();
		while (it.hasNext()) {
			SelectionKey key = (SelectionKey) it.next();
			SocketSession session = (SocketSession) key.attachment();

			if (key.isReadable()) {
				// 读就绪，则开始读信息
				System.out.println("read able ....");
				read(session);
			}

			if (key.isWritable()) {
				// 写就绪，调度写数据
				// TODO yjz
				System.out.println("writ able ....");
			}
		}

		selectedKeys.clear();
	}

	/**
	 * 在这里从SocketChannel里面读到数据后就交由责任链来处理了。
	 * 
	 * @param session
	 */
	private void read(SocketSession session) {

		System.out.println("终于开始进行nio的读数据啦。。。。");
		// TODO yjz
		ByteBuffer buf = ByteBuffer.allocate(1024);
		SocketChannel ch = session.getChannel();

		try {
			// YjzReadSocketChannel.read_SocketChannel(ch);
			// mina 自己实现的ByteBuffer类进行处理这个信息.
			int readBytes = 0;
			int ret;
			buf.clear();
			try {
				while ((ret = ch.read(buf.buf())) > 0) {
					readBytes += ret;
				}
			} finally {
				buf.flip();
			}

			session.inscreaseReadBytes(readBytes);
			session.resetIdleCount(IdleStatus.BOTH_IDLE);
			session.resetIdleCount(IdleStatus.READER_IDLE);

			if (readBytes > 0) {
				ByteBuffer newBuf = ByteBuffer.allocate(readBytes);
				newBuf.put(buf);
				newBuf.flip();
				// TODO yjz
				System.out.println("----read data to newBuf......");
				session.getManagerFilterChain().dataRead(session, newBuf);
			}
			if (ret < 0) {// 表示客户端的链接退出，或是断开了。（正常是如果没有了是0）
				// TODO yjz
			}

		} catch (Throwable t) {
			log.warn("Unexcepted exception.", t);
			// ....
		} finally {
			// ....
		}

	}

	private class Worker extends Thread {

		public Worker() {
			super("SocketIoProcessor(worker)-0");
		}

		@Override
		public void run() {
			for (;;) {
				try {

					int nKeys = selector.select(1000);// 检查到是否有已经满足读准备的要求了的。
					addSessions();// 对队列newSession里面的每个SocketChannel设置读观察类型

					if (nKeys > 0) {
						processSessions(selector.selectedKeys());// 每一个SocketChannel有读就绪的信息就进行处理。
					}

					// TODO yjz...
				} catch (Throwable t) {// 捕获所有的异常
					log.warn("Unexcepted exception.", t);

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}

			}
		}

	}
}
