package org.yplatform.ymina.io;

/**
 * An abstract adapter class for {@link IoFilter}. You can extend this class and
 * selectively override required event filter methods only. All methods forwards
 * events to the next filter by default.
 * <p>
 * Please refer to ... example.
 * 
 * @author jinze-yuan
 *
 */
public class IoFilterAdapter implements IoFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.io.IoFilter#sessionOpened(org.yplatform.ymina.io.
	 * IoFilter.NextFilter, org.yplatform.ymina.io.IoSession)
	 */
	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
		nextFilter.sessionOpened(session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.io.IoFilter#sessionClosed(org.yplatform.ymina.io.
	 * IoFilter.NextFilter, org.yplatform.ymina.io.IoSession)
	 */
	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
		nextFilter.sessionClosed(session);
	}

}
