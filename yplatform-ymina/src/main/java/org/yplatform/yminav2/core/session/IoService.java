/**
 * 
 */
package org.yplatform.yminav2.core.session;

/**
 * Base interface for all {@link IoAcceptor}s and {@link IoConnector}s
 * that provide I/O service and manage {@link IoSession}s.
 * 
 * @author jinze-yuan
 *
 */
public interface IoService {
	/**
	 * Returns the {@link TransportMetadata} that this service runs on.
	 */
	TransportMetadata getTransportMetadata();
	
	/**
	 * Adds an {@link IoServiceListener} that listens any events related with
	 * this service.
	 * @param listener
	 */
	void addListener(IoServiceListener listener);
	
	/**
	 * Removed an existing {@link IoServiceListener} that listens any events
	 * related with this service.
	 * @param listener
	 */
	void removeListner(IoServiceListener listener);
	

}
