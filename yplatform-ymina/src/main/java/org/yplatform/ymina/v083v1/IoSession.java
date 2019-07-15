/**
*
*/
package org.yplatform.ymina.v083v1;

/**
 * @author yuanjinze
 *
 */
public interface IoSession extends Session {

	IoHandler getHandler();
	
	//---add method
	void write(ByteBuffer buf,Object marker);
}
