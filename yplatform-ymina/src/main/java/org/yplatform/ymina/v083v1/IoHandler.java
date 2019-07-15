package org.yplatform.ymina.v083v1;

public interface IoHandler {
	
	void dataRead(IoSession session, ByteBuffer buf) throws Exception;
	
	void dataWritten(IoSession session,Object marker)throws Exception;
}
