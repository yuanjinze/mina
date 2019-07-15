/**
*
*/
package org.yplatform.ymina.util;

import java.util.AbstractSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An {@link IdentityHashMap}-backed {@link Set}
 * 
 * @author yuanjinze
 *
 */
public class IdentityHashSet<E> extends AbstractSet<E> {
	private final Map delegate = new IdentityHashMap();

	public IdentityHashSet(){
		
	}
	
	@Override
	public Iterator<E> iterator() {
		return delegate.keySet().iterator();
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean add(Object arg0) {
		return delegate.put(arg0, Boolean.TRUE)==null;// 添加同一个对象TRUE
	}

	@Override
	public boolean contains(Object o) {
		return delegate.containsKey(o);
	}

	@Override
	public boolean remove(Object o) {
		return delegate.remove(o)!=null;
	}

	@Override
	public void clear() {
		delegate.clear();
	}
	
	

}
