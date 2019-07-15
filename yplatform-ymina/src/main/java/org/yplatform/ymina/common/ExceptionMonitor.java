/**
*
*/
package org.yplatform.ymina.common;

/**
 * Monitors uncaught exceptions. {@link #exceptionCaught(Object, Throwable)} is
 * invoked when there are any uncaught exceptions.
 * 
 * @author yuanjinze
 *
 * @see DefaultExceptionMonitor
 */
public interface ExceptionMonitor {

	/**
	 * Invoked when there are any uncaught exceptions.
	 * @param source
	 * @param cause
	 */
	void exceptionCaught(Object source,Throwable cause);
	
}
