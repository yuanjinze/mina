/**
 * 
 */
package org.yplatform.yminav2.core.session;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * Accepts incoming connection,communicates with clients,and fires events to
 * {@link IoHandler}s.
 * @author jinze-yuan
 * 
 * Please refer to EchoServer example.
 * 
 * <p>
 * You should bind to the desired socket address to accept incoming 
 * connections, and then events for incoming connections will be sent to
 * the specified default {@link IoHandler}
 * <p>
 * Threads accept incoming connections start automatically when 
 * {@link #bind()} is invoked, and stop when {@link #unbind()} is invoked.
 * 
 * 
 */
public interface IoAcceptor extends IoService{
	
	/**
	 * Binds to the default local address(es) and start to accept incoming
	 * connections.
	 * @throws IOException if failed to bind
	 */
	void bind()throws IOException;
	
	/**
	 * Binds to the specified local address and start to accept incoming
	 * connections.
	 * @param localAddress The SocketAddress to bind to
	 * @throws IOException if failed to bind
	 * 
	 */
	void bind(SocketAddress localAddress) throws IOException;

}
