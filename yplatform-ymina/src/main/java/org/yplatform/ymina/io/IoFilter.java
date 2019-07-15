package org.yplatform.ymina.io;

/**
 * A after which intercepts {@link IoHandler} events like Servlet filters.
 * Filters can be used for these purposes:
 * <ul>
 * <li>Event logging</li>
 * <li>Performance measurement</li>
 * <li>Data transformation (e.g. SSL support)</li>
 * <li>Firewalling</li>
 * <li>and many more</li>
 * </ul>
 * 
 * <p>
 * <strong>Please NEVER implement your filters to wrap {@link IoSession}
 * s.</strong> Users can cache the reference to Session, which might malfunction
 * if any filters are added or removed later.
 * 
 * @see IoFilterAdapter
 *
 * @author jinze-yuan
 *
 */
public interface IoFilter {

	/**
	 * Filters {@link IoHandler#sessionOpend(IoSession)} event.
	 * @param nextFilter
	 * @param session
	 * @throws Exception
	 */
	void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception;

	/**
	 * Filters {@link IoHandler#sessionClosed(IoSession)} event.
	 * @param nextFilter
	 * @param session
	 * @throws Exception
	 */
	void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception;

	public interface NextFilter {

		void sessionOpened(IoSession session);
		// TODO yuanjinze

		void sessionClosed(IoSession session);
	}
}
