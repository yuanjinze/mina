/**
* yplatform-2016年8月15日
*/
package org.yplatform.ymina.util;

/**
 * Enumeration for MINA event types.
 * Used by {@link ThreadTool}s when they push events to event queue.
 * @author yuanjinze
 *
 */
public class EventType {
	public static final EventType OPENED = new EventType();
	public static final EventType CLOSED = new EventType();

	public static final EventType READ = new EventType();
	public static final EventType WRITTEN = new EventType();

	public static final EventType RECEIVED = new EventType();
	public static final EventType SENT = new EventType();

	public static final EventType IDLE = new EventType();
	public static final EventType EXCEPTION = new EventType();

	private EventType() {
	}
}
