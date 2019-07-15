package org.yplatform.ymina.util;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author yuanjinze FIFO (即先进先出的顺序,期间利用位运算&)
 */
public class Queue extends AbstractList implements List, Serializable {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_CAPACITY = 4;
	private static final int DEFAULT_MASK = DEFAULT_CAPACITY - 1;

	private Object[] items;
	private int mask;
	private int first = 0;// 表示什么
	private int last = 0;// 表示什么
	private int size = 0;

	public Queue() {
		items = new Object[DEFAULT_CAPACITY];
		mask = DEFAULT_MASK;
	}

	public int capacity() {
		return items.length;
	}

	@Override
	public void clear() {
		Arrays.fill(items, null);
		first = 0;
		last = 0;
		size = 0;
	}

	/**
	 * @return <code>null</code>, if this queue is empty or the element is
	 *         really <code>null</code>
	 */
	public Object pop() {
		if (size == 0) {
			return null;
		}
		Object ret = items[first];
		items[first] = null;
		decreaseSize();

		return ret;
	}

	/**
	 * Enqueue into this queue.
	 * 
	 * @return <code>null</code>, if this queue is empty or the element is
	 *         really <code>null</code>
	 */
	public void push(Object obj) {
		ensureCapacity();
		items[last] = obj;
		increaseSize();
	}

	/**
	 * Returns the first element of the queue.
	 * 
	 * @return <code>null</code>, if the queue is empty,or the element is really
	 *         <code>null</code>
	 */
	public Object first() {
		if (size == 0) {
			return null;
		}
		return items[first];
	}

	/**
	 * Returns the last element of the queue.
	 * 
	 * @return <code>null</code>,if the queue is empty, or the element is really
	 *         <code>null</code>
	 */
	public Object last() {
		if (size == 0) {
			return null;
		}
		return items[(last + items.length - 1) & mask];
	}

	@Override
	public Object get(int index) {
		checkIndex(index);
		return items[getRealIndex(index)];
	}

	public boolean isEmpty() {
		return (size == 0);
	}

	/*
	 * (non-Javadoc) Returns the number of elements in the queue.
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return size;
	}

	@Override
	public String toString() {
		return "first=" + first + ",last=" + last + ",size=" + size + ",mask=" + mask;
	}

	private void checkIndex(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
	}

	private int getRealIndex(int index) {
		return (first + index) & mask;
	}

	private void increaseSize() {
		last = (last + 1) & mask;
		size++;
	}

	private void decreaseSize() {
		first = (first + 1) & mask;
		size--;

	}

	private void ensureCapacity() {
		if (size < items.length) {
			return;
		}

		final int oldLen = items.length;
		Object[] tmp = new Object[oldLen << 1];// oldLen *2;再扩大一倍容量
		if (first < last) {
			System.arraycopy(items, first, tmp, 0, last - first);
		} else {
			System.arraycopy(items, first, tmp, 0, oldLen - first);
			System.arraycopy(items, 0, tmp, oldLen - first, last);
		}

		first = 0;
		last = oldLen;
		items = tmp;
		mask = tmp.length - 1;
	}

	// java.util.List compatibility method //

	public boolean add(Object o) {
		push(o);
		return true;
	}

	public Object set(int index, Object o) {
		checkIndex(index);

		int realIndex = getRealIndex(index);
		Object old = items[realIndex];

		items[realIndex] = o;
		return old;
	}

	public void add(int index, Object o) {
		if (index == size) {
			push(o);
			return;
		}

		checkIndex(index);
		ensureCapacity();
		int realIndex = getRealIndex(index);
		if (first < last) {
			System.arraycopy(items, realIndex, items, realIndex + 1, items.length);
		} else {
			if (realIndex >= first) {
				System.arraycopy(items, 0, items, 1, last);
				items[0] = items[items.length - 1];
				System.arraycopy(items, realIndex, items, realIndex + 1, items.length - realIndex - 1);
			} else {
				System.arraycopy(items, realIndex, items, realIndex + 1, last - realIndex);
			}

		}
		items[realIndex] = o;
		increaseSize();
	}

	public Object remove(int index) {
		if (index == 0) {
			return pop();
		}
		checkIndex(index);
		int realIndex = getRealIndex(index);
		Object removed = items[realIndex];

		if (first < last) {
			System.arraycopy(items, first, items, first + 1, realIndex - first);
		} else {
			if (realIndex >= first) {
				System.arraycopy(items, first, items, first + 1, realIndex - first);
			} else {
				System.arraycopy(items, 0, items, 1, realIndex);
				items[0] = items[items.length - 1];
				System.arraycopy(items, first, items, first + 1, items.length - first - 1);
			}

		}

		items[first] = null;
		decreaseSize();
		return removed;
	}

	// java.util.Queue//
	public boolean offer(Object o) {
		push(o);
		return true;
	}

	public Object poll() {
		return pop();
	}

	public Object remove() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		return pop();
	}

	public Object peek() {
		return first();
	}

	public Object element() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		return first();
	}

}
