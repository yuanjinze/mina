
package org.yplatform.ymina.util;

import java.io.Serializable;
import java.util.Arrays;

/**
 * A unbounded stack.
 * 
 * @author yuanjinze
 * @see java.io.Serializable
 */
public class Stack implements Serializable {

	private static final long serialVersionUID = 6099047678824837316L;

	private static final int DEFAULT_CAPACITY = 4;

	private Object[] items;
	private int size = 0;

	/**
	 * Construct a new, empty stack.
	 */
	public Stack() {
		items = new Object[DEFAULT_CAPACITY];
	}

	/**
	 * Clears this stack.
	 */
	public void clear() {
		Arrays.fill(items, null);
		size = 0;
	}

	/**
	 * Pops from this stack.
	 * 
	 * @return <code>null</code>, if this stack is empty or the element is
	 *         really <code>null</code>
	 */
	public Object pop() {
		if (size == 0) {
			return null;
		}
		int pos = size - 1;
		Object ret = items[pos];
		items[pos] = null;
		size--;

		return ret;
	}

	/**
	 * Push into this stack.
	 * 
	 * @param obj
	 */
	public void push(Object obj) {
		if (size == items.length) {
			// expand queue
			final int oldLen = items.length;
			Object[] tmp = new Object[oldLen << 1];// oldLen*2
			System.arraycopy(items, 0, tmp, 0, size);
			items = tmp;
		}

		items[size] = obj;
		size++;
	}

	/**
	 * yuanjinze:这个方法的算法值得学习.
	 * @param o
	 */
	public void remove(Object o) {
		for (int i = size - 1; i >= 0; i--) {
			if (items[i] == o) {
				System.arraycopy(items, i + 1, items, i, size - i - 1);
				items[size - 1] = null;
				size--;
				break;
			}
		}
	}

	/**
	 * Returns the first element of the stack.
	 * 
	 * @return <code>null</code>, if the stack is empty, or the element is
	 *         really <code>null</code>
	 */
	public Object first() {
		if (size == 0) {
			return null;
		}
		return items[size - 1];
	}

	public Object last() {
		if (size == 0) {
			return null;
		}
		return items[0];
	}

	/**
	 * Returns <code>true</code> if the stack is empty.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (size == 0);
	}

	/**
	 * Returns the number of elements in the stack.
	 * 
	 * @return
	 */
	public int size() {
		return size;
	}
}
