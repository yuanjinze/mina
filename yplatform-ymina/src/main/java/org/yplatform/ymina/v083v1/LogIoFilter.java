/**
*
*/
package org.yplatform.ymina.v083v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuanjinze
 *
 */
public class LogIoFilter implements IoFilter {
	private final static Logger log = LoggerFactory.getLogger(LogIoFilter.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yplatform.ymina.v08v1.IoFilter#dataRead(org.yplatform.ymina.v08v1.
	 * IoFilter.NextFilter, org.yplatform.ymina.v08v1.IoSession,
	 * org.yplatform.ymina.v08v1.ByteBuffer)
	 */
	@Override
	public void dataRead(NextFilter nextFilter, IoSession session, ByteBuffer buf) throws Exception {
		// TODO Auto-generated method stub
		log.info(String.valueOf("yuanjizne log: ") + buf.getHexDump());
		System.out.println("yjz  在这里处理日志信息...");
		// 这里比较调用nextFilter的方法，不然到过滤器就完了，不能走到IoHandler
		nextFilter.dataRead(session, buf);

	}

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session, ByteBuffer buf, Object marker) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
