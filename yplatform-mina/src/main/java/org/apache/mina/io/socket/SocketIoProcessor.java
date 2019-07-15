/*
 *   @(#) $Id: SocketIoProcessor.java 330742 2005-11-04 07:43:12Z trustin $
 *
 *   Copyright 2004 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.apache.mina.io.socket;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.SessionConfig;
import org.apache.mina.io.WriteTimeoutException;
import org.apache.mina.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs all I/O operations for sockets which is connected or bound.
 * This class is used by MINA internally.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 330742 $, $Date: 2005-11-04 15:43:12 +0800 (Fri, 04 Nov 2005) $,
 */
class SocketIoProcessor
{
    private static final Logger log = LoggerFactory.getLogger( SocketIoProcessor.class );
    private static final SocketIoProcessor instance;

    static
    {
        SocketIoProcessor tmp;

        try
        {
            tmp = new SocketIoProcessor();
        }
        catch( IOException e )
        {
            InternalError error = new InternalError(
                                                     "Failed to open selector." );
            error.initCause( e );
            throw error;
        }

        instance = tmp;
    }

    private final Selector selector;

    private final Queue newSessions = new Queue();

    private final Queue removingSessions = new Queue();

    private final Queue flushingSessions = new Queue();

    private final Queue readableSessions = new Queue();

    private Worker worker;

    private long lastIdleCheckTime = System.currentTimeMillis();

    private SocketIoProcessor() throws IOException
    {
        selector = Selector.open();
    }

    static SocketIoProcessor getInstance()
    {
        return instance;
    }

    void addSession( SocketSession session )
    {
        synchronized( this )
        {
            synchronized( newSessions )
            {
                newSessions.push( session );
            }
            startupWorker();
        }

        selector.wakeup();
    }

    void removeSession( SocketSession session )
    {
        scheduleRemove( session );
        startupWorker();
        selector.wakeup();
    }

    private synchronized void startupWorker()
    {
        if( worker == null )
        {
            worker = new Worker();
            worker.start();
        }
    }

    void flushSession( SocketSession session )
    {
        scheduleFlush( session );
        selector.wakeup();
    }

    void addReadableSession( SocketSession session )
    {
        synchronized( readableSessions )
        {
            readableSessions.push( session );
        }
        selector.wakeup();
    }

    private void addSessions()
    {
        if( newSessions.isEmpty() )
            return;

        SocketSession session;

        for( ;; )
        {
            synchronized( newSessions )
            {
                session = ( SocketSession ) newSessions.pop();
            }

            if( session == null )
                break;

            SocketChannel ch = session.getChannel();
            boolean registered;

            try
            {
                ch.configureBlocking( false );
                session.setSelectionKey( ch.register( selector,
                                                      SelectionKey.OP_READ,
                                                      session ) );
                registered = true;
            }
            catch( IOException e )
            {
                registered = false;
                session.getManagerFilterChain().exceptionCaught( session, e );
            }

            if( registered )
            {
                session.getManagerFilterChain().sessionOpened( session );
            }
        }
    }

    private void removeSessions()
    {
        if( removingSessions.isEmpty() )
            return;

        for( ;; )
        {
            SocketSession session;

            synchronized( removingSessions )
            {
                session = ( SocketSession ) removingSessions.pop();
            }

            if( session == null )
                break;

            SocketChannel ch = session.getChannel();
            SelectionKey key = session.getSelectionKey();
            // Retry later if session is not yet fully initialized.
            // (In case that Session.close() is called before addSession() is processed)
            if( key == null )
            {
                scheduleRemove( session );
                break;
            }

            // skip if channel is already closed
            if( !key.isValid() )
            {
                continue;
            }

            try
            {
                key.cancel();
                ch.close();
            }
            catch( IOException e )
            {
                session.getManagerFilterChain().exceptionCaught( session, e );
            }
            finally
            {
                releaseWriteBuffers( session );

                session.getManagerFilterChain().sessionClosed( session );
                session.notifyClose();
            }
        }
    }

    private void processSessions( Set selectedKeys )
    {
        Iterator it = selectedKeys.iterator();

        while( it.hasNext() )
        {
            SelectionKey key = ( SelectionKey ) it.next();
            SocketSession session = ( SocketSession ) key.attachment();

            if( key.isReadable() )
            {
                read( session );
            }

            if( key.isWritable() )
            {
                scheduleFlush( session );
            }
        }

        selectedKeys.clear();
    }

    private void read( SocketSession session )
    {
        ByteBuffer buf = ByteBuffer.allocate(
                (( SocketSessionConfig ) session.getConfig()).getSessionReceiveBufferSize() ); 
        SocketChannel ch = session.getChannel();

        try
        {
            int readBytes = 0;
            int ret;

            buf.clear();

            try
            {
                while( ( ret = ch.read( buf.buf() ) ) > 0 )
                {
                    readBytes += ret;
                }
            }
            finally
            {
                buf.flip();
            }

            session.increaseReadBytes( readBytes );
            session.resetIdleCount( IdleStatus.BOTH_IDLE );
            session.resetIdleCount( IdleStatus.READER_IDLE );

            if( readBytes > 0 )
            {
                ByteBuffer newBuf = ByteBuffer.allocate( readBytes );
                newBuf.put( buf );
                newBuf.flip();
                session.getManagerFilterChain().dataRead( session, newBuf );
            }
            if( ret < 0 )
            {
                scheduleRemove( session );
            }
        }
        catch( Throwable e )
        {
            if( e instanceof IOException )
                scheduleRemove( session );
            session.getManagerFilterChain().exceptionCaught( session, e );
        }
        finally
        {
            buf.release();
        }
    }

    private void scheduleRemove( SocketSession session )
    {
        synchronized( removingSessions )
        {
            removingSessions.push( session );
        }
    }

    private void scheduleFlush( SocketSession session )
    {
        synchronized( flushingSessions )
        {
            flushingSessions.push( session );
        }
    }

    private void notifyIdleSessions()
    {
        // process idle sessions
        long currentTime = System.currentTimeMillis();
        if( ( currentTime - lastIdleCheckTime ) >= 1000 )
        {
            lastIdleCheckTime = currentTime;
            Set keys = selector.keys();
            if( keys != null )
            {
                for( Iterator it = keys.iterator(); it.hasNext(); )
                {
                    SelectionKey key = ( SelectionKey ) it.next();
                    SocketSession session = ( SocketSession ) key.attachment();
                    notifyIdleSession( session, currentTime );
                }
            }
        }
    }

    private void notifyIdleSession( SocketSession session, long currentTime )
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

        notifyWriteTimeoutSession( session, currentTime, config
                .getWriteTimeoutInMillis(), session.getLastWriteTime() );
    }

    private void notifyIdleSession0( SocketSession session, long currentTime,
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

    private void notifyWriteTimeoutSession( SocketSession session,
                                           long currentTime,
                                           long writeTimeout, long lastIoTime )
    {
        SelectionKey key = session.getSelectionKey();
        if( writeTimeout > 0
            && ( currentTime - lastIoTime ) >= writeTimeout
            && key != null && key.isValid()
            && ( key.interestOps() & SelectionKey.OP_WRITE ) != 0 )
        {
            session
                    .getManagerFilterChain()
                    .exceptionCaught( session, new WriteTimeoutException() );
        }
    }

    private void flushSessions()
    {
        if( flushingSessions.size() == 0 )
            return;

        for( ;; )
        {
            SocketSession session;

            synchronized( flushingSessions )
            {
                session = ( SocketSession ) flushingSessions.pop();
            }

            if( session == null )
                break;

            if( !session.isConnected() )
            {
                releaseWriteBuffers( session );
                continue;
            }

            // If encountered write request before session is initialized, 
            // (In case that Session.write() is called before addSession() is processed)
            SelectionKey key = session.getSelectionKey();
            if( key == null )
            {
                // Reschedule for later write
                scheduleFlush( session );
                break;
            }
            if( !key.isValid() )
            {
                continue;
            }
            
            try
            {
                flush( session );
            }
            catch( IOException e )
            {
                scheduleRemove( session );
                session.getManagerFilterChain().exceptionCaught( session, e );
            }
        }
    }
    
    private void releaseWriteBuffers( SocketSession session )
    {
        Queue writeBufferQueue = session.getWriteBufferQueue();
        session.getWriteMarkerQueue().clear();
        ByteBuffer buf;
        
        while( ( buf = (ByteBuffer) writeBufferQueue.pop() ) != null )
        {
            try
            {
                buf.release();
            }
            catch( IllegalStateException e )
            {
                session.getManagerFilterChain().exceptionCaught( session, e );
            }
        }
    }

    private void flush( SocketSession session ) throws IOException
    {
        // Clear OP_WRITE
        SelectionKey key = session.getSelectionKey();
        key.interestOps( key.interestOps() & ( ~SelectionKey.OP_WRITE ) );

        SocketChannel ch = session.getChannel();

        Queue writeBufferQueue = session.getWriteBufferQueue();
        Queue writeMarkerQueue = session.getWriteMarkerQueue();

        ByteBuffer buf;
        Object marker;
        for( ;; )
        {
            synchronized( writeBufferQueue )
            {
                buf = ( ByteBuffer ) writeBufferQueue.first();
                marker = writeMarkerQueue.first();
            }

            if( buf == null )
                break;

            if( buf.remaining() == 0 )
            {
                synchronized( writeBufferQueue )
                {
                    writeBufferQueue.pop();
                    writeMarkerQueue.pop();
                }
                try
                {
                    buf.release();
                }
                catch( IllegalStateException e )
                {
                    session.getManagerFilterChain().exceptionCaught( session, e );
                }

                session.increaseWrittenWriteRequests();
                session.getManagerFilterChain().dataWritten( session, marker );
                continue;
            }

            int writtenBytes = ch.write( buf.buf() );
            if( writtenBytes > 0 )
            {
                session.increaseWrittenBytes( writtenBytes );
                session.resetIdleCount( IdleStatus.BOTH_IDLE );
                session.resetIdleCount( IdleStatus.WRITER_IDLE );
            }

            if( buf.hasRemaining() )
            {
                // Kernel buffer is full
                key.interestOps( key.interestOps() | SelectionKey.OP_WRITE );
                break;
            }
        }
    }

    private class Worker extends Thread
    {
        public Worker()
        {
            super( "SocketIoProcessor" );
        }

        public void run()
        {
            for( ;; )
            {
                try
                {
                    int nKeys = selector.select( 1000 );
                    addSessions();

                    if( nKeys > 0 )
                    {
                        processSessions( selector.selectedKeys() );
                    }

                    flushSessions();
                    removeSessions();
                    notifyIdleSessions();

                    if( selector.keys().isEmpty() )
                    {
                        synchronized( SocketIoProcessor.this )
                        {
                            if( selector.keys().isEmpty() &&
                                newSessions.isEmpty() )
                            {
                                worker = null;
                                break;
                            }
                        }
                    }
                }
                catch( Throwable t )
                {
                    log.warn( "Unexpected exception.", t );

                    try
                    {
                        Thread.sleep( 1000 );
                    }
                    catch( InterruptedException e1 )
                    {
                    }
                }
            }
        }
    }
}
