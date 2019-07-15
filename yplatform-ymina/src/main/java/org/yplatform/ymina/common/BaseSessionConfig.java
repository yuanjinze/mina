/**
* yplatform-2016年8月17日
*/
package org.yplatform.ymina.common;

/**
 * Base implementation of {@link SessionConfig}s.
 * 
 * @author yuanjinze
 *
 */
public abstract class BaseSessionConfig implements SessionConfig {

	private int idleTimeForRead;
	private int idleTimeForWrite;
	private int idleTimeForBoth;
	private int writeTimeout;

	protected BaseSessionConfig() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.common.SessionConfig#getIdleTime(org.yplatform.ymina.
	 * common.IdleStatus)
	 */
	@Override
	public int getIdleTime(IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE)
			return idleTimeForBoth;
		if (status == IdleStatus.READER_IDLE)
			return idleTimeForRead;
		if (status == IdleStatus.WRITER_IDLE)
			return idleTimeForWrite;

		throw new IllegalArgumentException("Unknown idle status: " + status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.SessionConfig#getIdleTimeInMillis(org.
	 * yplatform.ymina.common.IdleStatus)
	 */
	@Override
	public long getIdleTimeInMillis(IdleStatus status) {
		return getIdleTime(status) * 1000L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.common.SessionConfig#setIdleTime(org.yplatform.ymina.
	 * common.IdleStatus, int)
	 */
	@Override
	public void setIdleTime(IdleStatus status, int idleTime) {
		if (idleTime < 0)
			throw new IllegalArgumentException("Illegal idle time: " + idleTime);

		if (status == IdleStatus.BOTH_IDLE)
			idleTimeForBoth = idleTime;
		else if (status == IdleStatus.READER_IDLE)
			idleTimeForRead = idleTime;
		else if (status == IdleStatus.WRITER_IDLE)
			idleTimeForWrite = idleTime;
		else
			throw new IllegalArgumentException("Unknow idle status: " + status);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.SessionConfig#getWriteTimeout()
	 */
	@Override
	public int getWriteTimeout() {
		return writeTimeout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.SessionConfig#getWriteTimeoutInMillis()
	 */
	@Override
	public long getWriteTimeoutInMillis() {
		return writeTimeout * 1000L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.common.SessionConfig#setWriteTimeout(int)
	 */
	@Override
	public void setWriteTimeout(int writeTimeout) {
		if (writeTimeout < 0)
			throw new IllegalArgumentException("Illegal write timeout: " + writeTimeout);

		this.writeTimeout = writeTimeout;
	}

}
