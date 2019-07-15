package org.yplatform.yminav2.core.session;

import java.util.EventListener;

/**
 * Listens to events related to an {@link IoService}
 * @author jinze-yuan
 */
public interface IoServiceListener extends EventListener {

	/**
	 * Invoked when a new service is activated by an {@link IoService}.
	 * @param service
	 */
	void serviceActivated(IoService service);
}
