/*
 * @(#) $Id: TennisBall.java 326586 2005-10-19 15:50:29Z trustin $
 */
package org.apache.mina.examples.tennis;

/**
 * A tennis ball which has TTL value and state whose value is one of 'PING' and
 * 'PONG'.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
public class TennisBall
{
    private final boolean ping;

    private final int ttl;

    /**
     * Creates a new ball with the specified TTL (Time To Live) value.
     */
    public TennisBall( int ttl )
    {
        this( ttl, true );
    }

    /**
     * Creates a new ball with the specified TTL value and PING/PONG state.
     */
    private TennisBall( int ttl, boolean ping )
    {
        this.ttl = ttl;
        this.ping = ping;
    }

    /**
     * Returns the TTL value of this ball.
     */
    public int getTTL()
    {
        return ttl;
    }

    /**
     * Returns the ball after {@link TennisPlayer}'s stroke.
     * The returned ball has decreased TTL value and switched PING/PONG state.
     */
    public TennisBall stroke()
    {
        return new TennisBall( ttl - 1, !ping );
    }

    /**
     * Returns string representation of this message (<code>[PING|PONG]
     * (TTL)</code>).
     */
    public String toString()
    {
        if( ping )
        {
            return "PING (" + ttl + ")";
        }
        else
        {
            return "PONG (" + ttl + ")";
        }
    }
}