/**
* yplatform-2016年8月17日
*/
package org.yplatform.ymina.common;

/**
 * @author yuanjinze
 *
 */
public interface SessionConfig {
	/**
	 * Returns idle time for the specified type of idleness in seconds.
	 * @param status
	 * @return
	 */
	int getIdleTime(IdleStatus status);

	/**
	 * Returnd idle time for the specified type of idleness in milliseconds.
	 * @param status
	 * @return
	 */
	long getIdleTimeInMillis(IdleStatus status);

	/**
	 * Sets idle time for the specified type of idleness in seconds.
	 * @param status
	 * @param idleTime
	 */
	void setIdleTime(IdleStatus status, int idleTime);

	/**
	 * Returns write timeout in seconds.
	 * @return
	 */
	int getWriteTimeout();
	/**
	 * Returns write timeout in milliseconds.
	 * @return
	 */
	long getWriteTimeoutInMillis();
	
	/**
	 * Sets write timeout in seconds.
	 * @param writeTimeout
	 */
	void setWriteTimeout(int writeTimeout);
	
}
