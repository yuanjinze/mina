/**
* yplatform-2016年8月15日
*/
package org.yplatform.ymina.util;

/**
 * MINA Event used by {@link BaseThreadPool} internally.
 * 
 * @author yuanjinze
 *
 */
public class Event {

	private final EventType type;
	private final Object nextFilter;
	private final Object data;

	public Event(EventType type, Object nextFilter, Object data) {
		this.type = type;
		this.nextFilter = nextFilter;
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public Object getNextFilter() {
		return nextFilter;
	}

	public EventType getType() {
		return type;
	}
}
