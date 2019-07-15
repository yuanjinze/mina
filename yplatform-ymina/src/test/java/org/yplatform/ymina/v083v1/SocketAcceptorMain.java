/**
*
*/
package org.yplatform.ymina.v083v1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author yuanjinze
 *
 */
public class SocketAcceptorMain {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SocketAcceptorMain sa = new SocketAcceptorMain();
		sa.init();
		sa.study_Selector();
	}

	protected final IoAcceptor acceptor;
	protected final IoThreadPoolFilter ioThreadPoolFilter;
	protected int port;

	public SocketAcceptorMain() {
		this.acceptor = new SocketAcceptor();
		ioThreadPoolFilter = new IoThreadPoolFilter();
	}

	protected void init() throws Exception {
		boolean socketBound = false;

		for (port = 1; port < 65535; port++) {
			try {
				acceptor.bind(new InetSocketAddress(port));
				// acceptor.getFilterChain().addFirst("yjz-Log", new
				// LogIoFilter());// 添加一个日志过滤处理类
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

		Thread.sleep(1000 * 60 * 60 * 24);
	}

	public void study_Selector() {
		AbstractSelector selector = null;
		try {
			selector = SelectorProvider.provider().openSelector();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
