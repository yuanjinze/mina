package org.yplatform.yminav2.core.future;

import org.yplatform.yminav2.core.session.IoSession;

public interface ConnectFuture extends IoFuture {
	/**
	 * Returns {@link IoSession}which is the result of connect operation.
	 * @return <tt>null</tt> if the connect operation is not finished yet
	 * @throws RuntimeException if connection attempt failed by an exception
	 */
	@Override
	IoSession getSession();

	@Override
	ConnectFuture awwitUninterruptibly();

}
