/**
* yplatform-2016年8月15日
*/
package org.yplatform.ymina.util;

import java.util.HashSet;
import java.util.Iterator;

/**
 * A {@link HashSet} that can wait until it is not empty.
 * @author yuanjinze
 *
 */
public class BlockingSet extends HashSet {

	private static final long serialVersionUID = -4663583151213751294L;

	private int waiters = 0;

	public synchronized void waitForNewItem() throws InterruptedException {
		waiters++;
		try {
			while (isEmpty()) {
				wait();
			}
		} finally {
			waiters--;
		}
	}

	public synchronized boolean add(Object o) {
		boolean ret = super.add(o);
		if (ret && waiters > 0) {
			notify();
		}
		return ret;
	}

	public Iterator iterator() {
		return super.iterator();
	}

	public synchronized boolean remove(Object o) {
		return super.remove(o);
	}
}
