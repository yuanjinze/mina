package org.yplatform.ymina.v083v1;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseThreadPool {

	private final String threadNamePrefix;
	private static final Queue threadIdReuseQueue = new Queue();// 记录线程的Id
	private static int threadId = 0;

	private int poolSize = 0;
	private final Object poolSizeLock = new Object();
	private final Set allWorkers = new IdentityHashSet();

	private final BlockingQueue unfetchedSessionBuffers = new BlockingQueue();// 还没有取走的SessionBuffer
	private final Set allSessionBuffers = new IdentityHashSet();// 所有的SessionBuffer
	private final Map buffers = new IdentityHashMap();// 是一个map保存:session-》SessionBuffer的关系(即是key-value关系)

	public static final int DEFAULT_MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;// 2G
	public static final int DEFAULT_KEEP_ALIVE_TIME = 60 * 1000;

	private int maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE;
	private int keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;

	private boolean started;
	private boolean shuttingDown;

	private Worker leader;
	private final Stack followers = new Stack();// save Worker声明一个实现了栈的类(LIFO
												// 后进先出)

	public BaseThreadPool(String threadNamePrefix) {
		this.threadNamePrefix = threadNamePrefix.trim();
	}

	protected abstract void processEvent(Object nextFilter, Session session, EventType event, Object data);

	protected void fireEvent(Object nextFilter, Session session, EventType type, Object data) {
		// TODO yjz
		System.out.println("begin fireEvent:数据到此啦。。。");

		final Event event = new Event(type, nextFilter, data);

		final BlockingQueue unfetchedSessionBuffers = this.unfetchedSessionBuffers;
		final Set allSessionBuffers = this.allSessionBuffers;

		synchronized (unfetchedSessionBuffers) {
			final SessionBuffer buf = getSessionBuffer(session);
			final Queue eventQueue = buf.eventQueue;

			synchronized (buf) {
				eventQueue.push(event);
			}

			// 往集合里面添加SessionBuffer对象.
			if (!allSessionBuffers.contains(buf)) {
				allSessionBuffers.add(buf);
				unfetchedSessionBuffers.push(buf);
			}
		}

		System.out.println("end fireEvent:看看集合里面的数据，size= " + allSessionBuffers.size());
	}

	private SessionBuffer getSessionBuffer(Session session) {
		final Map buffers = this.buffers;
		SessionBuffer buf = (SessionBuffer) buffers.get(session);
		if (buf == null) {
			synchronized (buffers) {
				buf = (SessionBuffer) buffers.get(session);
				if (buf == null) {
					buf = new SessionBuffer(session);// 创建一个新的SessionBuffer对象
					buffers.put(session, buf);
				}
			}
		}
		return buf;

	}

	/**
	 * 
	 * 保存这个同一个对象，只调用一次，创建一个Worker
	 */
	public synchronized void start() {
		if (started)
			return;
		shuttingDown = false;

		leader = new Worker();
		leader.start();
		leader.lead();

		started = true;
	}

	private class Worker extends Thread {

		private final int id;

		private final Object promotionLock = new Object();// 升级锁
		private boolean dead;

		/**
		 * 只能在BaseThreadPool这个类里面用
		 * 
		 */
		private Worker() {
			int id = acquireThreadId();// 获得该线程的Id
			this.id = id;
			this.setName(threadNamePrefix + "(worker)-" + id);
			increasePoolSize(this);// 在线程池里面增加记录数
		}

		public boolean lead() {
			// TODO yjz
			final Object promotionLock = this.promotionLock;
			synchronized (promotionLock) {

				if (dead) {
					return false;
				}

				leader = this;
				promotionLock.notify();// 应该在线程调start()后就启动run(),而run里面用的了该锁
			}
			return true;
		}

		// 理解一个线程的run() 需要做的几件事情.
		@Override
		public void run() {
			for (;;) {
				System.out.println(id + ":work...begin 开始啦。。。");

				if (!waitForPromotion())
					break;

				SessionBuffer buf = fetchBuffer();// 获取SessionBufffer
				giveUpLead();// 放弃领导权利

				if (buf == null) {
					break;
				}
				System.out.println(id + ":work...end 开始啦。。。");

				// ------
				processEvents(buf);
				follow();
				releaseBuffer(buf);
			}

			decreasePoolSize(this);
			releaseThreadId(id);

		}

		private void releaseBuffer(SessionBuffer buf) {
			// TODO Auto-generated method stub

			final BlockingQueue unfetchedSessionBuffers = BaseThreadPool.this.unfetchedSessionBuffers;
			final Set allSessionBuffers = BaseThreadPool.this.allSessionBuffers;
			final Queue eventQueue = buf.eventQueue;

			synchronized (unfetchedSessionBuffers) {
				if (eventQueue.isEmpty()) {
					allSessionBuffers.remove(buf);
					removeSessionBuffer(buf);
				} else {
					unfetchedSessionBuffers.push(buf);
				}
			}
		}

		/**
		 * 将该worker压入follower栈
		 */
		private void follow() {
			final Object promotionLock = this.promotionLock;
			final Stack followers = BaseThreadPool.this.followers;
			synchronized (promotionLock) {
				if (this != leader) {
					synchronized (followers) {
						followers.push(this);
					}
				}
			}

		}

		private void processEvents(SessionBuffer buf) {
			final Session session = buf.session;
			final Queue eventQueue = buf.eventQueue;
			for (;;) {
				Event event;
				synchronized (buf) {
					event = (Event) eventQueue.pop();// 把Queue里面的event事件处理完
					if (event == null)
						break;
				}

				processEvent(event.getNextFilter(), session, event.getType(), event.getData());//分发到对应的IoHandler去处理信息
			}
		}

		/**
		 * 放弃worker是lead的权利
		 */
		private void giveUpLead() {
			final Stack followers = BaseThreadPool.this.followers;

			Worker worker;
			do {

				synchronized (followers) {
					worker = (Worker) followers.pop();
				}

				if (worker == null) {
					if (!shuttingDown && getPoolSize() < getMaximumPoolSize()) {
						worker = new Worker();
						worker.lead();
						worker.start();
					}

					break;
				}

			} while (!worker.lead());

		}

		private SessionBuffer fetchBuffer() {
			BlockingQueue unfetchedSessionBuffers = BaseThreadPool.this.unfetchedSessionBuffers;

			synchronized (unfetchedSessionBuffers) {
				while (!shuttingDown) {
					try {
						unfetchedSessionBuffers.waitForNewItem();
					} catch (InterruptedException e) {
						// 如果发生异常，继续下一次循环
						continue;
					}

					return BaseThreadPool.this.fetchSessionBuffer(unfetchedSessionBuffers);
				}
			}
			return null;
		}

		private boolean waitForPromotion() {
			final Object promotionLock = this.promotionLock;
			final long startTime = System.currentTimeMillis();
			long currentTime = startTime;

			synchronized (promotionLock) {

				while (this != leader && !shuttingDown) {
					int keepAliveTime = getKeepAliveTime();
					if (keepAliveTime > 0) {
						keepAliveTime -= (currentTime - startTime);
					} else {
						keepAliveTime = Integer.MAX_VALUE;
					}

					if (keepAliveTime <= 0)
						break;

					try {
						promotionLock.wait(keepAliveTime);
					} catch (InterruptedException e) {
						System.out.println("看到时间线程是否中断了。。。");
					}

					currentTime = System.currentTimeMillis();
				}

				boolean timeToLead = this == leader && !shuttingDown;// 此时此刻它是leader

				if (!timeToLead) {
					synchronized (followers) {
						followers.remove(this);
					}

					dead = true;
				}
				return timeToLead;
			}
		}

	}

	/**
	 * 包括两个属性:Session,Queue(队列里面主要保存Event事件对象)
	 * 
	 * @author yuanjinze
	 *
	 */
	private static class SessionBuffer {
		private final Session session;
		private final Queue eventQueue = new Queue();// 保存事件event对象

		private SessionBuffer(Session session) {
			this.session = session;
		}

		public Session getSession() {
			return session;
		}

		public Queue getEventQueue() {
			return eventQueue;
		}

	}

	private static int acquireThreadId() {
		synchronized (threadIdReuseQueue) {
			Integer id = (Integer) threadIdReuseQueue.pop();
			if (id == null) {
				return ++threadId;
			} else {
				return id.intValue();
			}
		}
	}

	public void releaseThreadId(int id) {

		synchronized (threadIdReuseQueue) {
			threadIdReuseQueue.push(new Integer(id));
		}
	}

	public void decreasePoolSize(Worker worker) {

		synchronized (poolSizeLock) {
			poolSize--;
			allWorkers.remove(worker);
		}
	}

	public void removeSessionBuffer(SessionBuffer buf) {
		// TODO Auto-generated method stub

		final Map buffers = this.buffers;
		final Session session = buf.session;
		synchronized (buffers) {
			buffers.remove(session);
		}
	}

	public int getKeepAliveTime() {
		// TODO Auto-generated method stub
		return keepAliveTime;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public int getPoolSize() {
		synchronized (poolSizeLock) {
			return poolSize;
		}
	}

	protected SessionBuffer fetchSessionBuffer(BlockingQueue unfetchedSessionBuffers2) {
		return (SessionBuffer) unfetchedSessionBuffers.pop();
	}

	private void increasePoolSize(Worker worker) {
		synchronized (poolSizeLock) {
			poolSize++;
			allWorkers.add(worker);
		}
	}

}
