/**
*
*/
package org.yplatform.ymina.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author yuanjinze
 *
 */
public class BlockingQueue extends Queue {

	private static final long serialVersionUID = -5131971261757535825L;

	private int waiters=0;
	
	public BlockingQueue(){}
	
	private void notifyAdded()
    {
        if( waiters > 0 )
            notify();
    }
	
	public synchronized void waitForNewItem()throws InterruptedException{
		waiters++;
		try{
			while(super.isEmpty()){
				wait();
			}
		}finally{
			waiters--;
		}
	}
	
	public synchronized void push( Object obj )
    {
        super.push( obj );
        notifyAdded();
    }

    public synchronized void add( int idx, Object o )
    {
        super.add( idx, o );
        notifyAdded();
    }

    public synchronized boolean add( Object o )
    {
        if( super.add( o ) )
        {
            notifyAdded();
            return true;
        }
        else
        {
            return false;
        }
    }

    public synchronized boolean addAll( int arg0, Collection arg1 )
    {
        if( super.addAll( arg0, arg1 ) )
        {
            notifyAdded();
            return true;
        }
        else
        {
            return false;
        }
    }

    public synchronized boolean addAll( Collection arg0 )
    {
        if( super.addAll( arg0 ) )
        {
            notifyAdded();
            return true;
        }
        else
        {
            return false;
        }
    }

    public synchronized boolean offer( Object o )
    {
        if( super.offer( o ) )
        {
            notifyAdded();
            return true;
        }
        else
        {
            return false;
        }
    }
   
    public synchronized int capacity()
    {
        return super.capacity();
    }

    public synchronized void clear()
    {
        super.clear();
    }

    public synchronized Object first()
    {
        return super.first();
    }

    public synchronized Object get( int idx )
    {
        return super.get( idx );
    }

    public synchronized boolean isEmpty()
    {
        return super.isEmpty();
    }

    public synchronized Object last()
    {
        return super.last();
    }

    public synchronized Object pop()
    {
        return super.pop();
    }

    public synchronized int size()
    {
        return super.size();
    }

    public synchronized String toString()
    {
        return super.toString();
    }

    public synchronized Object remove( int idx )
    {
        return super.remove( idx );
    }

    public synchronized Object set( int idx, Object o )
    {
        return super.set( idx, o );
    }

    public synchronized boolean equals( Object o )
    {
        return super.equals( o );
    }

    public synchronized int hashCode()
    {
        return super.hashCode();
    }

    public synchronized int indexOf( Object o )
    {
        return super.indexOf( o );
    }

    public synchronized Iterator iterator()
    {
        return super.iterator();
    }

    public synchronized int lastIndexOf( Object o )
    {
        return super.lastIndexOf( o );
    }

    public synchronized ListIterator listIterator()
    {
        return super.listIterator();
    }

    public synchronized ListIterator listIterator( int index )
    {
        return super.listIterator( index );
    }

    public synchronized List subList( int fromIndex, int toIndex )
    {
        return super.subList( fromIndex, toIndex );
    }

    public synchronized boolean contains( Object o )
    {
        return super.contains( o );
    }

    public synchronized boolean containsAll( Collection arg0 )
    {
        return super.containsAll( arg0 );
    }

    public synchronized boolean remove( Object o )
    {
        return super.remove( o );
    }

    public synchronized boolean removeAll( Collection arg0 )
    {
        return super.removeAll( arg0 );
    }

    public synchronized boolean retainAll( Collection arg0 )
    {
        return super.retainAll( arg0 );
    }

    public synchronized Object[] toArray()
    {
        return super.toArray();
    }

    public synchronized Object[] toArray( Object[] arg0 )
    {
        return super.toArray( arg0 );
    }

    public synchronized Object element()
    {
        return super.element();
    }

    public synchronized Object peek()
    {
        return super.peek();
    }

    public synchronized Object poll()
    {
        return super.poll();
    }

    public synchronized Object remove()
    {
        return super.remove();
    }
    
}
