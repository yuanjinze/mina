/**
*
*/
package org.yplatform.ymina.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A default {@link ExceptionMonitor} implementation that logs uncaught
 * exceptions using {@link Loggers}.
 * <p>
 * All {@link SessionManager}s have this implementation as a default exception
 * monitor.
 * 
 * @author yuanjinze
 *
 */
public class DefaultExceptionMonitor implements ExceptionMonitor {
	// study {@link Class}
	private static final Logger log = LoggerFactory.getLogger(DefaultExceptionMonitor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.common.ExceptionMonitor#exceptionCaught(java.lang.
	 * Object, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(Object source, Throwable cause) {
		log.warn("Unexpected exception", cause);
	}

}
