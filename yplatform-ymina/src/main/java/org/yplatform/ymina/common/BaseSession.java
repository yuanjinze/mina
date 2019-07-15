/**
* yplatform-2016年8月17日
*/
package org.yplatform.ymina.common;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Base implementation of {@link Session}
 * 
 * @author yuanjinze
 *
 */
public abstract class BaseSession implements Session {

	private final Map attributes = new HashMap();
	private final long creationTime;

	private long readBytes;
	private long writtenBytes;
	private long writtenWriteRequests;

	private long lastReadTime;
	private long lastWriteTime;

	private int idleCountForBoth;
	private int idleCountForRead;
	private int idleCountForWrite;

	private long lastIdleTimeForBoth;
	private long lastIdleTimeForRead;
	private long lastIdleTimeForWrite;

	protected BaseSession() {
		creationTime = lastReadTime = lastWriteTime = lastIdleTimeForBoth = lastIdleTimeForRead = lastIdleTimeForWrite = System
				.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#close()
	 */
	@Override
	public void close() {
		this.close(false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getAttachment()
	 */
	@Override
	public Object getAttachment() {
		return attributes.get("");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#setAttachment(java.lang.Object)
	 */
	@Override
	public Object setAttachment(Object attachment) {
		synchronized (attributes) {
			return attributes.put("", attachment);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public Object setAttribute(String key, Object value) {
		synchronized (attributes) {
			return attributes.put(key, value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#removeAttribute(java.lang.String)
	 */
	@Override
	public Object removeAttribute(String key) {
		synchronized (attributes) {
			return attributes.remove(key);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getAttributeKeys()
	 */
	@Override
	public Set getAttributeKeys() {
		synchronized (attributes) {
			return attributes.keySet();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getReadBytes()
	 */
	@Override
	public long getReadBytes() {
		return readBytes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getWrittenBytes()
	 */
	@Override
	public long getWrittenBytes() {
		return writtenBytes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getWrittenWriteRequests()
	 */
	@Override
	public long getWrittenWriteRequests() {
		return writtenWriteRequests;
	}

	public void increaseReadBytes(int increment) {
		readBytes += increment;
		lastReadTime = System.currentTimeMillis();
	}

	public void increaseWrittenBytes(int increment) {
		writtenBytes += increment;
		lastWriteTime = System.currentTimeMillis();
	}

	public void increaseWrittenWriteRequests() {
		writtenWriteRequests++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getCreationTime()
	 */
	@Override
	public long getCreationTime() {
		return creationTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getLastToTime()
	 */
	@Override
	public long getLastToTime() {
		// (a>=b)?a:b
		return Math.max(lastReadTime, lastWriteTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getLastReadTime()
	 */
	@Override
	public long getLastReadTime() {
		// TODO Auto-generated method stub
		return lastReadTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getLastWriteTime()
	 */
	@Override
	public long getLastWriteTime() {
		// TODO Auto-generated method stub
		return lastWriteTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.common.Session#isIdle(org.yplatform.ymina.common.
	 * IdleStatus)
	 */
	@Override
	public boolean isIdle(IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE)
			return idleCountForBoth > 0;
		if (status == IdleStatus.READER_IDLE)
			return idleCountForRead > 0;
		if (status == IdleStatus.WRITER_IDLE)
			return idleCountForWrite > 0;

		throw new IllegalArgumentException("Unknown idle status: " + status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.Session#getIdleCount(org.yplatform.ymina.
	 * common.IdleStatus)
	 */
	@Override
	public int getIdleCount(IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE)
			return idleCountForBoth;
		if (status == IdleStatus.READER_IDLE)
			return idleCountForRead;
		if (status == IdleStatus.WRITER_IDLE)
			return idleCountForWrite;

		throw new IllegalArgumentException("Unknown idle status: " + status);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.common.Session#getLastIdleTime(org.yplatform.ymina.
	 * common.IdleStatus)
	 */
	@Override
	public long getLastIdleTime(IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE)
			return lastIdleTimeForBoth;
		if (status == IdleStatus.READER_IDLE)
			return lastIdleTimeForRead;
		if (status == IdleStatus.WRITER_IDLE)
			return lastIdleTimeForWrite;

		throw new IllegalArgumentException("Unknown idle status: " + status);
	}

	public void increaseIdleCount(IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE) {
			idleCountForBoth++;
			lastIdleTimeForBoth = System.currentTimeMillis();
		} else if (status == IdleStatus.READER_IDLE) {
			idleCountForRead++;
			lastIdleTimeForRead = System.currentTimeMillis();
		} else if (status == IdleStatus.WRITER_IDLE) {
			idleCountForWrite++;
			lastIdleTimeForWrite = System.currentTimeMillis();
		} else
			throw new IllegalArgumentException("Unknown idle status: " + status);
	}

	public void resetIdleCount(IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE)
			idleCountForBoth = 0;
		else if (status == IdleStatus.READER_IDLE)
			idleCountForRead = 0;
		else if (status == IdleStatus.WRITER_IDLE)
			idleCountForWrite = 0;
		else
			throw new IllegalArgumentException("Unknown idle status: " + status);
	}

}
