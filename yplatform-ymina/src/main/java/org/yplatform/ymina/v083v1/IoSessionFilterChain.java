package org.yplatform.ymina.v083v1;

public class IoSessionFilterChain extends AbstractIoFilterChain {

	private final IoSessionManagerFilterChain managerFilterChain;

	public IoSessionFilterChain(IoSessionManagerFilterChain managerFilterChain) {
		this.managerFilterChain = managerFilterChain;
	}

	@Override
	protected void doWrite(IoSession session, ByteBuffer buf, Object marker) {
		System.out.println("调用会走这里吗？。。。。。");
		managerFilterChain.filterWrite(session, buf, marker);

	}
}
