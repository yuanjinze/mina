/**
 * 
 */
package org.yplatform.yminav2.core.future;

import org.yplatform.yminav2.core.session.IoSession;

/**
 * Represents the completion of an asynchronous I/O operation on an
 * {@link IoSession}. Can be listened for completion using a
 * {@link IoFutureListener}.
 * 
 * @author jinze-yuan
 *
 */
public interface IoFuture {
	/**
	 * Returns the {@link IoSession} which is associated with this future.
	 * @return
	 */
	IoSession getSession();

	/**
	 * Wait for the asynchronous operation to complete uninterruptibly.
	 * The attached listeners will be notified when the operation is
	 * completed.
	 * @return the current IoFuture
	 */
	IoFuture awwitUninterruptibly();
}
