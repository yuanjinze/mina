/**
*
*/
package org.yplatform.ymina.v083v1;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * @author yuanjinze
 *
 */
public interface IoAcceptor extends IoSessionManager {
	void bind(SocketAddress address) throws IOException;

}
