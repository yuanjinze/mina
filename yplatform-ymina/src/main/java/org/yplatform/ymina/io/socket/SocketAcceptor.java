/**
*
*/
package org.yplatform.ymina.io.socket;

import java.io.IOException;
import java.net.SocketAddress;

import org.yplatform.ymina.common.BaseSessionManager;
import org.yplatform.ymina.common.ExceptionMonitor;
import org.yplatform.ymina.io.IoAcceptor;
import org.yplatform.ymina.io.IoHandler;
import org.yplatform.ymina.io.IoSession;
import org.yplatform.ymina.util.Queue;

/**
 * {@link IoAcceptor} for socket transport (tcp/ip).
 * @author yuanjinze
 *
 */
public class SocketAcceptor extends BaseSessionManager implements IoAcceptor {
    private static volatile int nextId=0;
    private final int id=nextId++;
    private int backlog=50;


    private Queue registerQueue=new Queue();
    private Selector selector;
    private Worker worker;
    
    /**
     * Binds to the specified <code>address</code> and handles incoming
     * connections with the specified <code>handler</code>. Backlog value
     * is configured to the value of <code>backlog</code> property.
	 * @see org.yplatform.ymina.io.IoAcceptor#bind(java.net.SocketAddress, org.yplatform.ymina.io.IoHandler)
	 */
	@Override
	public void bind(SocketAddress address, IoHandler handler) throws IOException {
	    //step1. check parameter

	    //step2. 
	    RegistrationRequest request=new RegistrationRequest(address,backlog,handler);//warp request information to object
	    synchronized(this){
		synchronized(registerQueue){//because Queue not blockingQueue
		    registerQueue.push(request);
		}
		startupWorker();
	    }

	    selector.wakeup():

	    

	}

    private synchronized void startupWorker(){
	if(worker==null){
	    selector=Selector.open();
	    worker=new Worker();

	    worker.start();
	}
    }

	/* (non-Javadoc)
	 * @see org.yplatform.ymina.io.IoAcceptor#unbind(java.net.SocketAddress)
	 */
	@Override
	public void unbind(SocketAddress address) {
		// TODO Auto-generated method stub

	}

    private static class RegistrationRequest {
	private final SocketAddress address;
	private final int backlog;
	private final IoHandler handler;
	
	private IOException exception;
	private boolean done;
	
	private RegistrationRequest(SocketAddress address,int backlog,IoHandler handler){
	    this.address=address;
	    this.backlog=backlog;
	    this.handler=handler;
	}
    }

    private class Worker extends Thread {
	public Worker(){
	    super("SocketAcceptor-"+id);
	}

	public void run(){
	    for(;;){
		try{
		    //step1:
		    int nkeys=selector.select();

		    registerNew();
		    cancelKeys();

		    if(nkeys>0){
			processSession(selector.selectedKeys());
		    }
		    //step2;
		    if(selector.keys.isEmpty()){
			synchronized(SocketAcceptor.this){
			    
			}
		    }
		}catch(IOException e){
		    exceptionMonitor.exceptionCaught(SocketAcceptor.this,e);
		    Thread.sleep(1000);
		}
	    }
	}
    }

	@Override
	public IoSession newSession(SocketAddress remoteAddress, SocketAddress localAddress) {
		// TODO Auto-generated method stub
		return null;
	}

}
