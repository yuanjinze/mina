/**
*
*/
package org.yplatform.ymina.v083v1;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author yuanjinze
 *
 */
public class SocketSession extends BaseSession implements IoSession {

	private final IoSessionManagerFilterChain managerFilterChain;
	private final IoSessionFilterChain filterChain;
	private final IoHandler handler;
	private final SocketChannel ch;
	private SelectionKey key;

	public SocketSession(IoSessionManagerFilterChain managerFilterChain, SocketChannel ch, IoHandler defaultHandler) {
		this.managerFilterChain = managerFilterChain;
		this.filterChain = new IoSessionFilterChain(managerFilterChain);
		this.ch = ch;
		this.handler = defaultHandler;
	}

	public IoSessionManagerFilterChain getManagerFilterChain() {
		return managerFilterChain;
	}

	public SocketChannel getChannel() {
		return ch;
	}

	public void setSelectionKey(SelectionKey key) {
		this.key = key;
	}

	@Override
	public IoHandler getHandler() {
		return handler;
	}

	@Override
	public void write(ByteBuffer buf, Object marker) {
		filterChain.filterWrite(this,buf,marker);
		
	}

}
