/**
*
*/
package org.yplatform.ymina.v083v1;

/**
 * @author yuanjinze
 *
 */
public class IoThreadPoolFilter extends BaseThreadPool implements IoFilter {

	public IoThreadPoolFilter() {
		this("IoThreadPool");
	}

	public IoThreadPoolFilter(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.v083v1.IoFilter#dataRead(org.yplatform.ymina.v083v1.
	 * IoFilter.NextFilter, org.yplatform.ymina.v083v1.IoSession,
	 * org.yplatform.ymina.v083v1.ByteBuffer)
	 */
	@Override
	public void dataRead(NextFilter nextFilter, IoSession session, ByteBuffer buf) throws Exception {
		buf.acquire();
		fireEvent(nextFilter, session, EventType.READ, buf);
	}

	@Override
	protected void processEvent(Object nextFilter0, Session session0, EventType type, Object data) {
		System.out.println("processEvent: 分发event事件啦。。。");
		NextFilter nextFilter = (NextFilter) nextFilter0;
		IoSession session = (IoSession) session0;

		if (type == EventType.READ) {
			ByteBuffer buf = (ByteBuffer) data;
			nextFilter.dataRead(session, buf);
			buf.release();
		}
	}

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session, ByteBuffer buf, Object marker) throws Exception {
		nextFilter.filterWrite(session, buf, marker);
	}

}
