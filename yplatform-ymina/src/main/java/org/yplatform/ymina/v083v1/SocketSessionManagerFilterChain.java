package org.yplatform.ymina.v083v1;

public class SocketSessionManagerFilterChain extends IoSessionManagerFilterChain {

	@Override
	protected void doWrite(IoSession session, ByteBuffer buffer, Object marker) {
		SocketSession s=(SocketSession)session;
		System.out.println("进入...SocketIoProcessor 方法拉");
		
//		SocketIoProcessor.getInstance().flushSession(s);//加入处理入口
		
	}

}
