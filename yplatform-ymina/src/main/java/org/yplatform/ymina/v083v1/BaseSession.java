/**
*
*/
package org.yplatform.ymina.v083v1;

/**
 * @author yuanjinze
 *
 */
public abstract class BaseSession implements Session {

	private long readBytes;

	private long lastReadTime;

	private int idleCountForBoth;
	private int idleCountForRead;
	private int idleCountForWrite;

	public void inscreaseReadBytes(int increment) {
		readBytes += increment;
		lastReadTime = System.currentTimeMillis();
	}

	public void resetIdleCount(IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE)
			idleCountForBoth = 0;
		else if (status == IdleStatus.READER_IDLE)
			idleCountForRead = 0;
		else if (status == IdleStatus.WRITER_IDLE)
			idleCountForWrite = 0;
		else
			throw new IllegalArgumentException("Unknow idle status: " + status);
	}
}
