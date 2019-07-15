package org.yplatform.ymina.v083v1;

/**
 * @author yuanjinze
 *
 */
public class BaseSessionManager implements SessionManager {

	protected ExceptionMonitor exceptionMonitor = new DefaultExceptionMonitor();

	protected BaseSessionManager() {
	}

	@Override
	public ExceptionMonitor getExceptionMonitor() {
		return exceptionMonitor;
	}

	@Override
	public void setExceptionMonitor(ExceptionMonitor monitor) {
		if (monitor == null) {
			monitor = new DefaultExceptionMonitor();
		}
		this.exceptionMonitor = monitor;
	}

}
