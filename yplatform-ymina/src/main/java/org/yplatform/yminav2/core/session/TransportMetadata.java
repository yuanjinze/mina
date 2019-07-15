/**
 * 
 */
package org.yplatform.yminav2.core.session;

import java.net.SocketAddress;
import java.util.Set;

/**
 * @author jinze-yuan
 *
 */
public interface TransportMetadata {

	/**
	 * Returns the name of the service provider (e.g. "nio", "apr" and "rxtx").
	 * 
	 * @return
	 */
	String getProviderName();

	/**
	 * Returns the name of the service.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Returns <code>true</code> if the session of this transport type is
	 * connectionless.
	 * 
	 * @return
	 */
	boolean isConnectionless();

	/**
	 * Returns {@code true} if the message exchanged by the service can be
	 * fragmented or reassembled by its underlying transport.
	 * 
	 * @return
	 */
	boolean hasFragmentation();

	/**
	 * Returns the address type of the service.
	 * 
	 * @return
	 */
	Class<? extends SocketAddress> getAddressType();

	/**
	 * Returns the set of the allowed message type when you write to an
	 * {@link IoSession} that is managed by the service.
	 * 
	 * @return
	 */
	Set<Class<? extends Object>> getEnvelopeTypes();

	/**
	 * Returns the type of the {@link IoSessionConfig} of the service
	 * @return
	 */
	Class<? extends IoSessionConfig> getSessionConfigType();
}
