package org.yplatform.ymina.v083v1;

public interface IoFilter {

	void dataRead(NextFilter nextFilter, IoSession session, ByteBuffer buf) throws Exception;

	void filterWrite(NextFilter nextFilter,IoSession session,ByteBuffer buf,Object marker)throws Exception;
	public interface NextFilter {
		void dataRead(IoSession session, ByteBuffer buf);
		void filterWrite(IoSession session,ByteBuffer buf,Object marker);
	}
}
