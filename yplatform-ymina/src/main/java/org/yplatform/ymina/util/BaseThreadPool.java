/**
*
*/
package org.yplatform.ymina.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.yplatform.ymina.common.Session;
	
/**
 * A base implementation of Thread-pooling filters. This filter forwards events
 * to its thread pool. This is an implementation of Leader/Followers thread pool
 * by Douglas C. Schmidt et al.
 * 
 * @author yuanjinze
 *
 */
public abstract class BaseThreadPool implements ThreadPool {

	public static final int DEFAULT_MAXIMUM_POLL_SIZE = Integer.MAX_VALUE;
	public static final int DEFAULT_KEEP_ALIVE_TIME = 60 * 1000;

	private static final Queue threadIdReuseQueue = new Queue();
	private static int threadId = 0;

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

	private static void releaseThreadId(int id) {
		synchronized (threadIdReuseQueue) {
			threadIdReuseQueue.push(new Integer(id));
		}
	}

	private final String threadNamePrefix;
	private final Map buffers = new IdentityHashMap();
	private final BlockingQueue unfetchedSessionBuffers = new BlockingQueue();
	private final Set allSessionBuffers = new IdentityHashSet();

	private Worker leader;
	private final Stack followers = new Stack();
	private final Set allWorkers = new IdentityHashSet();

	private int maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE;
	private int keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;

	private boolean started;
	private boolean shuttingDown;

	private int poolSize;
	private final Object poolSizeLock = new Object();

	public BaseThreadPool(String threadNamePrefix) {
		if (threadNamePrefix == null) {
			throw new NullPointerException("threadNamePrefix");
		}
		if (threadNamePrefix.length() == 0) {
			throw new IllegalArgumentException("threadNamePrefix is empty.");
		}

		this.threadNamePrefix = threadNamePrefix;

	}

	@Override
	public int getPoolSize() {
		synchronized (poolSizeLock) {
			return poolSize;
		}
	}

	@Override
	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	@Override
	public int getKeepAliveTime() {
		return keepAliveTime;
	}

	@Override
	public void setMaximumPoolSize(int maximumPoolSize) {
		if (maximumPoolSize <= 0)
			throw new IllegalArgumentException();
		this.maximumPoolSize = maximumPoolSize;
	}

	@Override
	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	@Override
	public synchronized void start() {
		if (started)
			return;

		shuttingDown = false;

		leader = new Workder();
		leader.start();
		leader.lead();

		started = true;
	}

	@Override
	public void stop() {
		if (!started)
			return;

		shuttingDown = true;
		while (getPoolSize() != 0) {
			// Loop...yjz
		}

		this.allSessionBuffers.clear();
		this.unfetchedSessionBuffers.clear();
		this.buffers.clear();
		this.followers.clear();
		this.leader = null;

		started = false;
	}

	private void increasePoolSize(Worker worker) {
		synchronized (poolSizeLock) {
			poolSize++;
			allWorkers.add(worker);
		}
	}

	private void decreasePoolSize(Worker worker) {
		synchronized (poolSizeLock) {
			poolSize--;
			allWorkers.remove(worker);
		}
	}

	/**
	 * Implement this method to forward events to <tt>nextFilter</tt>
	 */
	protected abstract void processEvent(Session session, Object data);

	private class Worker extends Thread {

		private final int id;
		private final Object promotionLock = new Object();
		private boolean dead;

		private Worker() {
			int id = acquireThreadId();
			this.id = id;
			this.setName(threadNamePrefix + "-" + id);

			increasePoolSize(this);
		}

		public boolean lead() {
			final Object promotionLock = this.promotionLock;
			synchronized (promotionLock) {
				if (dead)
					return false;
				leader = this;
				promotionLock.notify();
			}
			return true;
		}

		public void run() {
			// TODO yjz..
			for (;;) {
				if (!waitForPromotion())
					break;

				SessionBuffer buf = fetchBuffer();
				giveUpLead();

				if (buf == null)
					break;

				// ---TODO yjz
				processEvents(buf);
				follow();
				releaseBuffer(buf);

			}

			decreasePoolSize(this);
			releaseThreadId(id);
		}
	}

}
