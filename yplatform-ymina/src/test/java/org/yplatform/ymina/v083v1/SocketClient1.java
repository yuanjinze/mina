/**
*
*/
package org.yplatform.ymina.v083v1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author yuanjinze
 *
 */
public class SocketClient1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		// 打开一个选择器
		Selector selector = Selector.open();
		// 打开一个SocketChannel实例 并设置为false
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		// 绑定ip +端口
		 socketChannel.connect(new InetSocketAddress(1));
//		socketChannel.connect(new InetSocketAddress("127.0.0.1", 1024));
		// 向 serverSocketChannel 注册 连接就绪事件
		socketChannel.register(selector, SelectionKey.OP_CONNECT);

		// 进行控制太输入 写事件 进行通讯
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				// java IO 阻塞读取数据
				String data = bufferedReader.readLine();// 从控制台读取用户输入的信息.
				if ("exit".equals(data)) {
					socketChannel.close();
					System.out.println("主线程关闭.....");
					System.exit(0);
				}
				ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
				if (!socketChannel.isConnected()) {
					socketChannel.finishConnect();
				}
				socketChannel.write(buffer);// 将buffer里面的信息写入SocketChannel里面去.

			} catch (IOException e) {
				e.printStackTrace();
			}
			// try {
			// Thread.sleep(1000 * 60 /** 60 * 24 */
			// );
			// } catch (InterruptedException e) {
			// }
		}

	}

}
