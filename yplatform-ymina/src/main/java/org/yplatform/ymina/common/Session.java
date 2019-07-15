/**
* yplatform-2016年8月16日
*/
package org.yplatform.ymina.common;

import java.net.SocketAddress;
import java.util.Set;

/**
 * A handle which represents connection between two endpoints regardless of
 * transport types.
 * <p>
 * Session provides user-defined attributes. Use-defined attributes are
 * application-specific data which is associated with a session. It often
 * contains objects that represents the state of a higher-level protocol and
 * becomes a way to exchange data between filters and handlers
 * 
 * 
 * @author yuanjinze
 *
 */
public interface Session {
	/**
	 * Closes this session immediately. Calling method is identical with calling
	 * <tt>close(false)</tt>
	 */
	void close();

	/**
	 * Closes this session immediately.
	 * 
	 * @param wait
	 *            <tt>true</tt> is you want to wait until closing process is
	 *            complete.
	 */
	void close(boolean wait);

	/**
	 * Returns an attachment of this session.
	 * This method is identical with <tt> getAttribute("")</tt>
	 * @return
	 */
	Object getAttachment();
	
	/**
	 * Sets an attachment of this session.
	 * This method is identical with <tt>setAttribute("",attachment)</tt>.
	 * @param attachment
	 * @return
	 */
	Object setAttachment(Object attachment);
	
	/**
	 * Returns the value of user-defined attribute of this session.
	 * @param key the key of the attribute.
	 * @return <tt>null</tt> if there is no attribute with the specified key
	 */
	Object getAttribute(String key);
	
	/**
	 * Sets a user-defined attribute.
	 * @param key the key of the attribute.
	 * @param value the value of the attribute.
	 * @return The old value of the attribute. <tt>null</tt> if it is new.
	 */
	Object setAttribute(String key,Object value);
	
	/**
	 * Removes a user-defined attribute with the specified key.
	 * @param key
	 * @return The old value of the attribute. <tt>null</tt> if not found.
	 */
	Object removeAttribute(String key);
	
	/**
	 * Returns the set of keys of all user-defined attributes.
	 * @return
	 */
	Set getAttributeKeys();
	
	/**
	 * Returns transport type of this session.
	 * @return
	 */
	TransportType getTransportType();
	
	/**
	 * Returns <code>true</code> if this session is connected with remote peer.
	 * @return
	 */
	boolean isConnected();
	
	/**
	 * Returns the configuration of this session.
	 */
	SessionConfig getConfig();
	
	/**
	 * Returns the socket address of remote peer.
	 * @return
	 */
	SocketAddress getRemoteAddress();
	
	/**
	 * Returns the socket address of local machine which is associated with this
	 * session.
	 * @return
	 */
	SocketAddress getLocalAddress();
	
	/**
	 * Returns the total number of bytes which were read from this session.
	 * @return
	 */
	long getReadBytes();
	
	/**
	 * Returns the total number of bytes which were written to this session.
	 * @return
	 */
	long getWrittenBytes();
	
	/**
	 * Returns the total number of write requests which were written to this session.
	 * 
	 * @return
	 */
	long getWrittenWriteRequests();
	
	/**
	 * Returns the number of write requests which are  scheduled to be written
	 * to this session.
	 * @return
	 */
	long getScheduledWriteRequests();
	
	/**
	 * Returns the time in millis when this session is created.
	 * @return
	 */
	long getCreationTime();
	
	/**
	 * Returns the time in millis when I/O occurred lastly.
	 * @return
	 */
	long getLastToTime();
	
	/**
	 * Returns the time in millis when write operation occurred lastly.
	 * @return
	 */
	long getLastReadTime();
	
	/**
	 * @return
	 */
	long getLastWriteTime();
	
	/**
	 * Returns <code>true</code> is this session is idle for the specified
	 * {@link IdleStatus}.
	 * @param status
	 * @return
	 */
	boolean isIdle(IdleStatus status);
	
	/**
	 * Returns the number of the fired continuous <tt>sessionIdle</tt> events
	 * for the specified {@link IdleStatus}.
	 * <p>
	 * If <tt>sesssionIdle</tt> event is fired first after some time after I/O,
	 * <tt>idleCount</tt> becomes <tt>1</tt>. <tt>idleCount</tt> resets to
	 * <tt>0</tt> if any I/O occurs again, otherwise it increases to
	 * <tt>2</tt> and so on if <tt>sessionIdle</tt> event is fired again without
	 * any I/O between two (or more> <tt>sessionIdle</tt> events.
	 * @param status
	 * @return
	 */
	int getIdleCount(IdleStatus status);
	
	/**
	 * Returns the time in millis when the last <tt>sessionIdle</tt> event
	 * is fired for the specified {@link IdleStatus}.
	 * @param status
	 * @return
	 */
	long getLastIdleTime(IdleStatus status);
}
