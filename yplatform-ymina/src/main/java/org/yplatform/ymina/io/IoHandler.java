/**
*
*/
package org.yplatform.ymina.io;

import java.io.IOException;

import org.yplatform.ymina.common.ByteBuffer;
import org.yplatform.ymina.common.IdleStatus;

/**
 * Handles all I/O events fired by {@link IoAcceptor} and {@link IoConnector}.
 * There are 6 event handler methods, and they are all invoked by MINA
 * automatically. Most users of MINA I/O package will be OK with this single
 * interface to implement their protocols.
 * <p>
 * Please refer to.
 * example.
 * 
 * @author yuanjinze
 *
 */
public interface IoHandler {
	
	/**
	 * Invoked when the session is created. Initialize default socket
	 * parameters and user-defined attributes here.
	 * @param session
	 * @throws Exception
	 */
	void sessionCreated(IoSession session) throws Exception;
	
	/**
	 * Invoked when the connection is opened. This method is not invoked if the transport type is UDP.
	 * @param session
	 * @throws Exception
	 */
	void sessionOpend(IoSession session)throws Exception;
	/**
	 * Invoked when the connection is closed. This method is not invoked if the transport type is UDP.
	 * @param session
	 * @throws Exception
	 */
	void sessionClosed(IoSession session)throws Exception;
	
	/**
	 * Invoked when the connection is idle.Refer to {@link IdleStatus}. This
	 * method is not invoked if the transport type is UDP.
	 * @param session
	 * @param status
	 * @throws Exception
	 */
	void sessionIdle(IoSession session,IdleStatus status)throws Exception;
	
	/**
	 * Invoked when any exception is thrown by user {@link IoHandler}
	 * implementation or by MINA. If <code>cause</code> is instanceof
	 * {@link IOException}, MINA will close the connection automatically.
	 * @param session
	 * @param cause
	 * @throws Exception
	 */
	void exceptionCaught(IoSession session,Throwable cause)throws Exception;
	

	/**
	 * Invoked when data is read from the connection. You can access
	 * <code>buf</code> to get read data. <code>buf</code> returns to 
	 * the internal buffer pool of MINA after this method is invoked, so
	 * please don't try to reuse it.
	 * @param session
	 * @param buf
	 * @throws Exception
	 */
	void dataRead(IoSession session, ByteBuffer buf) throws Exception;
	
	/**
	 * Invoked when MINA wrote {@link IoSession#write(ByteBuffer, Object)}
	 * request successedfully. <code>marker</code> is what you specified at the
	 * point of invocation of {@link IoSession#write(ByteBuffer, Object)};
	 * @param session
	 * @param marker
	 * @throws Exception
	 */
	void dataWritten(IoSession session,Object marker)throws Exception;
	
	
}
