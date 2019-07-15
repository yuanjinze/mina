/**
*
*/
package org.yplatform.ymina.registry;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.yplatform.ymina.io.IoAcceptor;
import org.yplatform.ymina.io.IoHandler;
import org.yplatform.ymina.io.filter.IoThreadPoolFilter;
import org.yplatform.ymina.io.socket.SocketAcceptor;
import org.yplatform.ymina.util.BaseThreadPool;

/**
 * A simple implementation of {@link ServiceRegistry}. This service registry
 * supports socket and thread pools were added by default.
 * 
 * @author yuanjinze
 *
 */
public class SimpleServiceRegistry implements ServiceRegistry {

	protected final IoAcceptor socketIoAcceptor = new SocketAcceptor();

	protected final BaseThreadPool threadPool = new IoThreadPoolFilter();

	private final Set services = new HashSet();

	public SimpleServiceRegistry() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.registry.ServiceRegistry#bind(org.yplatform.ymina.
	 * registry.Service, org.yplatform.ymina.io.IoHandler)
	 */
	@Override
	public void bind(Service service, IoHandler ioHandler) throws IOException {
		IoAcceptor acceptor = findIoAcceptor();
		acceptor.bind(service.getAddress(), ioHandler);
		startThreadPools();
		services.add(service);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yplatform.ymina.registry.ServiceRegistry#getIoAcceptor()
	 */
	@Override
	public IoAcceptor getIoAcceptor() {

		return findIoAcceptor();
	}

	protected IoAcceptor findIoAcceptor() {
		// ... refer to type return IoAcceptor. but now simple socketIoAcceptor
		return socketIoAcceptor;
	}

	private void startThreadPools() {
		// Start thread pools only when the first service is bound.
		// If any services are bound, it means that thread pools are started
		// already.
		if (!services.isEmpty())
			return;

		threadPool.start();// iompact
	}

}
