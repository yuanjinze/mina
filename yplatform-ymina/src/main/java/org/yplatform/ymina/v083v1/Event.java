/**
*
*/
package org.yplatform.ymina.v083v1;

/**
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

	public EventType getType() {
		return type;
	}

	public Object getNextFilter() {
		return nextFilter;
	}

	public Object getData() {
		return data;
	}

}
