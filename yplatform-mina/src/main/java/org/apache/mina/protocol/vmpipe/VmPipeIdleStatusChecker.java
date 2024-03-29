/*
 * @(#) $Id: VmPipeIdleStatusChecker.java 326586 2005-10-19 15:50:29Z trustin $
 */
package org.apache.mina.protocol.vmpipe;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.SessionConfig;

/**
 * Dectects idle sessions and fires <tt>sessionIdle</tt> events to them. 
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
class VmPipeIdleStatusChecker
{
    static final VmPipeIdleStatusChecker INSTANCE = new VmPipeIdleStatusChecker();

    private final Map sessions = new IdentityHashMap(); // will use as a set

    private final Worker worker = new Worker();

    private VmPipeIdleStatusChecker()
    {
        worker.start();
    }

    void addSession( VmPipeSession session )
    {
        synchronized( sessions )
        {
            sessions.put( session, session );
        }
    }

    private class Worker extends Thread
    {
        private Worker()
        {
            super( "VmPipeIdleStatusChecker" );
            setDaemon( true );
        }

        public void run()
        {
            for( ;; )
            {
                try
                {
                    Thread.sleep( 1000 );
                }
                catch( InterruptedException e )
                {
                }

                long currentTime = System.currentTimeMillis();

                synchronized( sessions )
                {
                    Iterator it = sessions.keySet().iterator();
                    while( it.hasNext() )
                    {
                        VmPipeSession session = ( VmPipeSession ) it.next();
                        if( !session.isConnected() )
                        {
                            it.remove();
                        }
                        else
                        {
                            notifyIdleSession( session, currentTime );
                        }
                    }
                }
            }
        }
    }
    
    private void notifyIdleSession( VmPipeSession session, long currentTime )
    {
        SessionConfig config = session.getConfig();

        notifyIdleSession0(
                session, currentTime,
                config.getIdleTimeInMillis( IdleStatus.BOTH_IDLE ),
                IdleStatus.BOTH_IDLE,
                Math.max( session.getLastIoTime(), session.getLastIdleTime( IdleStatus.BOTH_IDLE ) ) );
        notifyIdleSession0(
                session, currentTime,
                config.getIdleTimeInMillis( IdleStatus.READER_IDLE ),
                IdleStatus.READER_IDLE,
                Math.max( session.getLastReadTime(), session.getLastIdleTime( IdleStatus.READER_IDLE ) ) );
        notifyIdleSession0(
                session, currentTime,
                config.getIdleTimeInMillis( IdleStatus.WRITER_IDLE ),
                IdleStatus.WRITER_IDLE,
                Math.max( session.getLastWriteTime(), session.getLastIdleTime( IdleStatus.WRITER_IDLE ) ) );
    }

    private void notifyIdleSession0( VmPipeSession session, long currentTime,
                                    long idleTime, IdleStatus status,
                                    long lastIoTime )
    {
        if( idleTime > 0 && lastIoTime != 0
            && ( currentTime - lastIoTime ) >= idleTime )
        {
            session.increaseIdleCount( status );
            session.getManagerFilterChain().sessionIdle( session, status );
        }
    }

}