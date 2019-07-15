/**
*
*/
package org.yplatform.ymina.common;

/**
 * 这儿可以监控任何没有捕获的异常
 * @author yuanjinze
 *
 */
public interface SessionManager {

	/**
	 * 
	 * Returns the current exception monitor.
	 */
	ExceptionMonitor getExceptionMonitor();
	
	/**
	 * Sets the uncaught exception monitor. if <code>null</code> is specified, 
	 * a new instance of {@link DefaultExceptionMonitor} will be set
	 * @param monitor A new instance of {@link DefaultExceptionMonitor} is set 
	 * 	if <tt>null</tt> is specified.
	 */
	void setExceptionMonitor(ExceptionMonitor monitor);
}
