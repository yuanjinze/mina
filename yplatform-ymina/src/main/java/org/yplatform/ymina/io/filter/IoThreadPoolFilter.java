/**
*
*/
package org.yplatform.ymina.io.filter;

import java.nio.ByteBuffer;

import org.yplatform.ymina.util.BaseThreadPool;
import org.yplatform.ymina.util.Session;
import org.yplatform.ymina.util.ThreadPool;

/**
 * @author yuanjinze
 *
 */
public class IoThreadPoolFilter extends BaseThreadPool implements ThreadPool {

	public IoThreadPoolFilter() {
		this("IoThreadPool");
	}

	public IoThreadPoolFilter(String threadNamePrefix) {
		super(threadNamePrefix);
	}

	@Override
	protected void processEvent(Session session, Object data) {
		// case1:
		ByteBuffer buf = (ByteBuffer) data;
		// read data;
		buf.release();

	}

	@Override
	protected void processEvent(org.yplatform.ymina.common.Session session, Object data) {
		// TODO Auto-generated method stub
		
	}

}
