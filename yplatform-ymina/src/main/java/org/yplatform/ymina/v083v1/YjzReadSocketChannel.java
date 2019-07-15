/**
*
*/
package org.yplatform.ymina.v083v1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author yuanjinze
 *
 */
public class YjzReadSocketChannel {

	public static void read_SocketChannel(SocketChannel ch) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(100);
		int len = ch.read(byteBuffer);
		byteBuffer.flip();
		// 读取完毕
		if (byteBuffer.limit() > 0) {
			System.out.println("服务端收到的消息:" + new String(byteBuffer.array()).trim());
		}
	}
}
