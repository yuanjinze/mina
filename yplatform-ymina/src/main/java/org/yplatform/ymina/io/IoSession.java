package org.yplatform.ymina.io;

import org.yplatform.ymina.common.ByteBuffer;
import org.yplatform.ymina.common.Session;

/**
 * A {@link Session} that represents low-level I/O connection between two
 * endpoints regardless of underlying transport types.
 * @author jinze-yuan
 *
 */
public interface IoSession extends Session {

	/**
	 * Returns the event handler for this session.
	 * @return
	 */
	IoHandler getHandler();
	
	/**
	 * Returns the filter chain that only affects this session.
	 * @return
	 */
	IoFilterChain getFilterChain();
	
	/**
	 * Writes the content of the specified <code>buf</code>
	 * This operation is asynchronous, and you'll get notified by
	 * {@link IoHandler#dataWritten(IoSession,Object)} event.
	 * @param buf
	 * @param marker
	 */
	void write(ByteBuffer buf,Object marker);
	
	
	
}
