/**
*
*/
package org.yplatform.ymina.v083v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuanjinze
 *
 */
public class DefaultExceptionMonitor implements ExceptionMonitor {

	// 这儿打印异常信息，就用到了:slf4j的日志记录框架
	private static final Logger log = LoggerFactory.getLogger(DefaultExceptionMonitor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.v08v1.ExceptionMonitor#exceptionCaught(java.lang.
	 * Object, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(Object source, Throwable cause) {
		log.warn("Unexcepted exception.", cause);//其实在warn方法里面要一层一层的去找cause，同时调他们对于的toString方法.
	}

}
