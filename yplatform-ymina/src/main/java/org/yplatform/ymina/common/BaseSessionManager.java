package org.yplatform.ymina.common;

public class BaseSessionManager implements SessionManager {

    /**
     *Current exception monitor
     */
    protected ExceptionMonitor exceptionMonitor= new DefaultExceptionMonitor();

    protected BaseSessionManager(){}


	@Override
	public ExceptionMonitor getExceptionMonitor() {

	    return exceptionMonitor;
	}

	@Override
	public void setExceptionMonitor(ExceptionMonitor monitor) {
	    if(monitor==null){
		monitor=new DefaultExceptionMonitor();
	    }
	    this.exceptionMonitor=monitor;

	}

}
