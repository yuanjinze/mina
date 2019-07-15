package org.yplatform.yminav2.core.session;

import java.net.SocketAddress;

import org.yplatform.yminav2.core.future.ConnectFuture;

/**
 * Connects to endpoint, communicates with the server, and fires events to
 * {@link IoHandler}s.
 * <p>
 * Please refer to Netcat example.
 * <p>
 * You should connect to the desired socket address to start communication,
 * and then events for incoming connections will be sent to the specified
 * default {@link IoHandler}.
 * <p>
 * Threads connect to endpoint start automatically when
 * {@link #connect(SocketAddress)} is invoked,and stop when all
 * connection attempts are finished.
 * @author jinze-yuan
 *
 */
public interface IoConnector extends IoService {
	/**
	 * Sets the default remote address to connect to when no argument is 
	 * specified in {@link #connect()} method.	
	 * @param defaultRemoteAddress
	 */
	void setDefaultRemoteAddress(SocketAddress defaultRemoteAddress);
/**
 * Connects to the {@link #setDefaultRemoteAddress(SocketAddress) default remote address}.
 * @return
 */
	ConnectFuture connect();

}
