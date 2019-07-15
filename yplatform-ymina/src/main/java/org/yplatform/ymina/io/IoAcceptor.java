/**
*
*/
package org.yplatform.ymina.io;

import java.io.IOException;
import java.net.SocketAddress;

import org.yplatform.ymina.common.SessionManager;

/**
 * Accepts incoming connection,communicats with clients, and fires events to {@link IoHandler}s.
 * <p>
 * You should bind to the desired socket address to accept incoming
 * connections, and then events for incoming connections will be sent to 
 * the specified default {@link IoHandler}.
 * <p>
 * Threads accept incoming connections start automatically when
 * {@link #bind(SocketAddress,IoHandler)} is invoked,and stop when all
 * address are unbound.
 * @author yuanjinze
 *
 */
public interface IoAcceptor extends SessionManager {
    
    /**
     * Binds to the specified <code>address</code> and handlers incoming
     * connections with the specified <code>handler</code>.
     * @throws IOException if failed to bind.
     */
    void bind(SocketAddress address,IoHandler handler) throws IOException;

    /**
     * Unbinds from the speified <code>address</code>
     */
    void unbind(SocketAddress address);
    
    
    /**
     * (Optional) Returns an {@link IoSession} that is bound to the specified
     * <tt>localAddress</tt> and <tt> remoteAddress</tt> which reuses
     * the <tt>localAddress</tt> that is already bound by {@link IoAcceptor}
     * via {@link #bind(SocketAddress, IoHandler)}.
     * @param remoteAddress
     * @param localAddress
     * @return
     */
    IoSession newSession(SocketAddress remoteAddress,SocketAddress localAddress);

    


}
