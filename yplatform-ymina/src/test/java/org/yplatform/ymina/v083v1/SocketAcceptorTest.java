package org.yplatform.ymina.v083v1;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.junit.Assert;
import org.yplatform.ymina.v083v1.IoAcceptor;
import org.yplatform.ymina.v083v1.LogIoFilter;
import org.yplatform.ymina.v083v1.SocketAcceptor;

import junit.framework.TestCase;

public class SocketAcceptorTest extends TestCase {

	protected final IoAcceptor acceptor;
	protected final IoThreadPoolFilter ioThreadPoolFilter;
	protected int port;

	public SocketAcceptorTest() {
		this.acceptor = new SocketAcceptor();
		ioThreadPoolFilter=new IoThreadPoolFilter();
	}

	@Override
	protected void setUp() throws Exception {
		boolean socketBound = false;

		for (port = 1; port < 65535; port++) {
			try {
				acceptor.bind(new InetSocketAddress(port));
//				acceptor.getFilterChain().addFirst("yjz-Log", new LogIoFilter());// 添加一个日志过滤处理类
				acceptor.getFilterChain().addFirst("threadPool", ioThreadPoolFilter);
				ioThreadPoolFilter.start();
				
				socketBound = true;
				System.out.println("当前socket服务用的端口是: " + port);
				break;
			} catch (IOException e) {

			}
		}

		if (!socketBound)
			throw new IOException("Cannot bind any test port");
	}

	// -------------------------test method

	public void testDuplicatebind() {

		try {
			acceptor.bind(new InetSocketAddress(port));// 重复调用绑定方法，没有抛IOException
			Assert.fail("IOException is not throw");
		} catch (IOException e) {

		}

	}

}
