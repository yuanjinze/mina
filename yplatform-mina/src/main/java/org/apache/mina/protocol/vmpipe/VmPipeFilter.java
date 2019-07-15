/*
 * @(#) $Id: VmPipeFilter.java 326586 2005-10-19 15:50:29Z trustin $
 */
package org.apache.mina.protocol.vmpipe;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.protocol.ProtocolFilterAdapter;
import org.apache.mina.protocol.ProtocolSession;

/**
 * Sets last(Read|Write)Time for {@link VmPipeSession}s. 
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
class VmPipeFilter extends ProtocolFilterAdapter
{
    public void messageReceived( NextFilter nextFilter,
                                 ProtocolSession session, Object message )
    {
        VmPipeSession vps = ( VmPipeSession ) session;

        vps.resetIdleCount( IdleStatus.BOTH_IDLE );
        vps.resetIdleCount( IdleStatus.READER_IDLE );
        vps.increaseReadBytes( 1 );

        // fire messageSent event first
        vps.remoteSession.getManagerFilterChain().messageSent( vps.remoteSession, message );

        // and then messageReceived
        nextFilter.messageReceived( session, message );
    }

    public void messageSent( NextFilter nextFilter,
                            ProtocolSession session, Object message )
    {
        VmPipeSession vps = ( VmPipeSession ) session;
        vps.resetIdleCount( IdleStatus.BOTH_IDLE );
        vps.resetIdleCount( IdleStatus.WRITER_IDLE );
        vps.increaseWrittenBytes( 1 );
        vps.increaseWrittenWriteRequests();

        nextFilter.messageSent( session, message );
    }
}