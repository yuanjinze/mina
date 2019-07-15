/*
 * @(#) $Id: AnonymousVmPipeAddress.java 433523 2006-08-22 05:09:52Z trustin $
 */
package org.apache.mina.protocol.vmpipe;

import java.net.SocketAddress;

/**
 * A {@link SocketAddress} which represents anonymous in-VM pipe port.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 433523 $, $Date: 2006-08-22 13:09:52 +0800 (Tue, 22 Aug 2006) $
 */
class AnonymousVmPipeAddress extends SocketAddress implements Comparable
{
    private static final long serialVersionUID = 3258135768999475512L;

    /**
     * Creates a new instance with the specifid port number.
     */
    public AnonymousVmPipeAddress()
    {
    }

    public int hashCode()
    {
        return System.identityHashCode( this );
    }

    public boolean equals( Object o )
    {
	return this == o;
    }

    public int compareTo( Object o )
    {
        return this.hashCode() - ( ( AnonymousVmPipeAddress ) o ).hashCode();
    }

    public String toString()
    {
        return "vm:anonymous(" + hashCode() + ')';
    }
}