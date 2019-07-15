package org.yplatform.yminav2.core.session;

/**
 * Handlers all I/O events fired by MINA
 * 
 * @author jinze-yuan
 *
 */

public interface IoHandler {
	/**
	 * Invoked from an I/O processor thread when a new connection has been
	 * created. Because this method is supposed to be called from the same
	 * thread that handles I/O of mutiple sessions, please implement this method
	 * to perform tasks that consumes minimal amount of time such as socket
	 * parameter and user-defined session attribute initialization.
	 * 
	 * @throws Exception
	 */
	void sessionCreated() throws Exception;

	void sessionOpened() throws Exception;

	void sessionClosed() throws Exception;

	void sessionIdle(IoSession session, IdleStatus status) throws Exception;

	void exceptionCaught(IoSession session, Throwable cause) throws Exception;

	/**
	 * Invoked when a message is received.
	 * @param session
	 * @param message
	 * @throws Exception
	 */
	void messageReceived(IoSession session, Object message) throws Exception;

	/**
	 * Invoked when a message written by {@link IoSession#write(Object)} is sent out.
	 * @param session
	 * @param message
	 * @throws Exception
	 */
	void messageSent(IoSession session, Object message) throws Exception;
}
