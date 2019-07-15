/*
 *   @(#) $Id: SSLFilter.java 326583 2005-10-19 15:36:22Z trustin $
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
package org.apache.mina.io.filter;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.io.IoFilterAdapter;
import org.apache.mina.io.IoHandler;
import org.apache.mina.io.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An SSL filter that encrypts and decrypts the data exchanged in the session.
 * This filter uses an {@link SSLEngine} which was introduced in Java 5, so 
 * Java version 5 or above is mandatory to use this filter. And please note that
 * this filter only works for TCP/IP connections.
 * <p>
 * This filter logs debug information using {@link Logger}.
 * 
 * <h2>Implementing StartTLS</h2>
 * <p>
 * You can use {@link #DISABLE_ENCRYPTION_ONCE} attribute to implement StartTLS:
 * <pre>
 * public void messageReceived(ProtocolSession session, Object message) {
 *    if (message instanceof MyStartTLSRequest) {
 *        // Insert SSLFilter to get ready for handshaking
 *        IoSession ioSession = ((IoProtocolSession) session).getIoSession();
 *        ioSession.getFilterChain().addLast(sslFilter);
 *
 *        // Disable encryption temporarilly.
 *        // This attribute will be removed by SSLFilter
 *        // inside the Session.write() call below.
 *        session.setAttribute(SSLFilter.DISABLE_ENCRYPTION_ONCE, Boolean.TRUE);
 *
 *        // Write StartTLSResponse which won't be encrypted.
 *        session.write(new MyStartTLSResponse(OK));
 *        
 *        // Now DISABLE_ENCRYPTION_ONCE attribute is cleared.
 *        assert session.getAttribute(SSLFilter.DISABLE_ENCRYPTION_ONCE) == null;
 *    }
 * }
 * </pre>
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326583 $, $Date: 2005-10-19 23:36:22 +0800 (Wed, 19 Oct 2005) $
 */
public class SSLFilter extends IoFilterAdapter
{
    /**
     * A session attribute key that stores underlying {@link SSLSession}
     * for each session.
     */
    public static final String SSL_SESSION = SSLFilter.class.getName() + ".SSLSession";
    
    /**
     * A session attribute key that makes next one write request bypass
     * this filter (not encrypting the data).  This is a marker attribute,
     * which means that you can put whatever as its value. ({@link Boolean#TRUE}
     * is preferred.)  The attribute is automatically removed from the session
     * attribute map as soon as {@link IoSession#write(ByteBuffer, Object)} is
     * invoked, and therefore should be put again if you want to make more
     * messages bypass this filter.  This is especially useful when you
     * implement StartTLS.   
     */
    public static final String DISABLE_ENCRYPTION_ONCE = SSLFilter.class.getName() + ".DisableEncryptionOnce";
    
    private static final String SSL_HANDLER = SSLFilter.class.getName() + ".SSLHandler";

    private static final Logger log = LoggerFactory.getLogger( SSLFilter.class );

    /**
     * A marker which is passed with {@link IoHandler#dataWritten(IoSession, Object)}
     * when <tt>SSLFilter</tt> writes data other then user actually requested.
     */
    private static final Object SSL_MARKER = new Object()
    {
        public String toString()
        {
            return "SSL_MARKER";
        }
    };
    
    // SSL Context
    private SSLContext sslContext;

    private boolean client;
    private boolean needClientAuth;
    private boolean wantClientAuth;
    private String[] enabledCipherSuites;
    private String[] enabledProtocols;

    /**
     * Creates a new SSL filter using the specified {@link SSLContext}.
     */
    public SSLFilter( SSLContext sslContext )
    {
        if( sslContext == null )
        {
            throw new NullPointerException( "sslContext" );
        }

        this.sslContext = sslContext;
    }
    
    /**
     * Returns the underlying {@link SSLSession} for the specified session.
     * 
     * @return <tt>null</tt> if no {@link SSLSession} is initialized yet.
     */
    public SSLSession getSSLSession( IoSession session )
    {
        return ( SSLSession ) session.getAttribute( SSL_SESSION );
    }

    /**
     * Returns <tt>true</tt> if the engine is set to use client mode
     * when handshaking.
     */
    public boolean isUseClientMode()
    {
        return client;
    }
    
    /**
     * Configures the engine to use client (or server) mode when handshaking.
     */
    public void setUseClientMode( boolean clientMode )
    {
        this.client = clientMode;
    }
    
    /**
     * Returns <tt>true</tt> if the engine will <em>require</em> client authentication.
     * This option is only useful to engines in the server mode.
     */
    public boolean isNeedClientAuth()
    {
        return needClientAuth;
    }

    /**
     * Configures the engine to <em>require</em> client authentication.
     * This option is only useful for engines in the server mode.
     */
    public void setNeedClientAuth( boolean needClientAuth )
    {
        this.needClientAuth = needClientAuth;
    }
    
    
    /**
     * Returns <tt>true</tt> if the engine will <em>request</em> client authentication.
     * This option is only useful to engines in the server mode.
     */
    public boolean isWantClientAuth()
    {
        return wantClientAuth;
    }
    
    /**
     * Configures the engine to <em>request</em> client authentication.
     * This option is only useful for engines in the server mode.
     */
    public void setWantClientAuth( boolean wantClientAuth )
    {
        this.wantClientAuth = wantClientAuth;
    }
    
    /**
     * Returns the list of cipher suites to be enabled when {@link SSLEngine}
     * is initialized.
     * 
     * @return <tt>null</tt> means 'use {@link SSLEngine}'s default.'
     */
    public String[] getEnabledCipherSuites()
    {
        return enabledCipherSuites;
    }
    
    /**
     * Sets the list of cipher suites to be enabled when {@link SSLEngine}
     * is initialized.
     * 
     * @param cipherSuites <tt>null</tt> means 'use {@link SSLEngine}'s default.'
     */
    public void setEnabledCipherSuites( String[] cipherSuites )
    {
        this.enabledCipherSuites = cipherSuites;
    }

    /**
     * Returns the list of protocols to be enabled when {@link SSLEngine}
     * is initialized.
     * 
     * @return <tt>null</tt> means 'use {@link SSLEngine}'s default.'
     */
    public String[] getEnabledProtocols()
    {
        return enabledProtocols;
    }
    
    /**
     * Sets the list of protocols to be enabled when {@link SSLEngine}
     * is initialized.
     * 
     * @param protocols <tt>null</tt> means 'use {@link SSLEngine}'s default.'
     */
    public void setEnabledProtocols( String[] protocols )
    {
        this.enabledProtocols = protocols;
    }

    // IoFilter impl.

    public void sessionOpened( NextFilter nextFilter, IoSession session ) throws SSLException
    {
        // Create an SSL handler
        createSSLSessionHandler( nextFilter, session );
        nextFilter.sessionOpened( session );
    }

    public void sessionClosed( NextFilter nextFilter, IoSession session ) throws SSLException
    {
        SSLHandler sslHandler = getSSLSessionHandler( session );
        if( log.isDebugEnabled() )
        {
            log.debug( session + " Closed: " + sslHandler );
        }
        if( sslHandler != null )
        {
            synchronized( sslHandler )
            {
               // Start SSL shutdown process
               try
               {
                  // shut down
                  sslHandler.shutdown();
                  
                  // there might be data to write out here?
                  writeNetBuffer( nextFilter, session, sslHandler );
               }
               finally
               {
                  // notify closed session
                  nextFilter.sessionClosed( session );
                  
                  // release buffers
                  sslHandler.release();
                  removeSSLSessionHandler( session );
               }
            }
        }
    }
   
    public void dataRead( NextFilter nextFilter, IoSession session,
                          ByteBuffer buf ) throws SSLException
    {
        SSLHandler sslHandler = createSSLSessionHandler( nextFilter, session );
        if( sslHandler != null )
        {
            if( log.isDebugEnabled() )
            {
                log.debug( session + " Data Read: " + sslHandler + " (" + buf+ ')' );
            }
            synchronized( sslHandler )
            {
                try
                {
                    // forward read encrypted data to SSL handler
                    sslHandler.dataRead( nextFilter, buf.buf() );

                    // Handle data to be forwarded to application or written to net
                    handleSSLData( nextFilter, session, sslHandler );

                    if( sslHandler.isClosed() )
                    {
                        if( log.isDebugEnabled() )
                        {
                            log.debug( session + " SSL Session closed. Closing connection.." );
                        }
                        session.close();
                    }
                }
                catch( SSLException ssle )
                {
                    if( !sslHandler.isInitialHandshakeComplete() )
                    {
                        SSLException newSSLE = new SSLHandshakeException(
                                "Initial SSL handshake failed." );
                        newSSLE.initCause( ssle );
                        ssle = newSSLE;
                    }

                    throw ssle;
                }
            }
        }
        else
        {
            nextFilter.dataRead( session, buf );
        }
    }

    public void dataWritten( NextFilter nextFilter, IoSession session,
                            Object marker )
    {
        if( marker != SSL_MARKER )
        {
            nextFilter.dataWritten( session, marker );
        }
    }

    public void filterWrite( NextFilter nextFilter, IoSession session, ByteBuffer buf, Object marker ) throws SSLException
    {
        // Don't encrypt the data if encryption is disabled.
        if( session.getAttribute(DISABLE_ENCRYPTION_ONCE) != null )
        {
            // Remove the marker attribute because it is temporary.
            session.removeAttribute(DISABLE_ENCRYPTION_ONCE);
            nextFilter.filterWrite( session, buf, marker );
            return;
        }
        
        // Otherwise, encrypt the buffer.
        SSLHandler handler = createSSLSessionHandler( nextFilter, session );
        if( log.isDebugEnabled() )
        {
            log.debug( session + " Filtered Write: " + handler );
        }

        synchronized( handler )
        {
            if( handler.isWritingEncryptedData() )
            {
                // data already encrypted; simply return buffer
                if( log.isDebugEnabled() )
                {
                    log.debug( session + "   already encrypted: " + buf );
                }
                nextFilter.filterWrite( session, buf, marker );
                return;
            }
            
            if( handler.isInitialHandshakeComplete() )
            {
                // SSL encrypt
                if( log.isDebugEnabled() )
                {
                    log.debug( session + " encrypt: " + buf );
                }
                handler.encrypt( buf.buf() );
                ByteBuffer encryptedBuffer = copy( handler
                        .getOutNetBuffer() );

                if( log.isDebugEnabled() )
                {
                    log.debug( session + " encrypted buf: " + encryptedBuffer);
                }
                buf.release();
                nextFilter.filterWrite( session, encryptedBuffer, marker );
                return;
            }
            else
            {
                if( !session.isConnected() )
                {
                    if( log.isDebugEnabled() )
                    {
                        log.debug( session + " Write request on closed session." );
                    }
                }
                else
                {
                    if( log.isDebugEnabled() )
                    {
                        log.debug( session + " Handshaking is not complete yet. Buffering write request." );
                    }
                    handler.scheduleWrite( nextFilter, buf, marker );
                }
            }
        }
    }

    // Utiliities

    private void handleSSLData( NextFilter nextFilter, IoSession session,
                               SSLHandler handler ) throws SSLException
    {
        // Flush any buffered write requests occurred before handshaking.
        if( handler.isInitialHandshakeComplete() )
        {
            handler.flushScheduledWrites();
        }

        // Write encrypted data to be written (if any)
        writeNetBuffer( nextFilter, session, handler );

        // handle app. data read (if any)
        handleAppDataRead( nextFilter, session, handler );
    }

    private void handleAppDataRead( NextFilter nextFilter, IoSession session,
                                   SSLHandler sslHandler )
    {
        if( log.isDebugEnabled() )
        {
            log.debug( session + " appBuffer: " + sslHandler.getAppBuffer() );
        }
        if( sslHandler.getAppBuffer().hasRemaining() )
        {
            // forward read app data
            ByteBuffer readBuffer = copy( sslHandler.getAppBuffer() );
            if( log.isDebugEnabled() )
            {
                log.debug( session + " app data read: " + readBuffer + " (" + readBuffer.getHexDump() + ')' );
            }
            nextFilter.dataRead( session, readBuffer );
        }
    }

    void writeNetBuffer( NextFilter nextFilter, IoSession session, SSLHandler sslHandler )
            throws SSLException
    {
        // Check if any net data needed to be writen
        if( !sslHandler.getOutNetBuffer().hasRemaining() )
        {
            // no; bail out
            return;
        }

        // write net data

        // set flag that we are writing encrypted data
        // (used in filterWrite() above)
        synchronized( sslHandler )
        {
            sslHandler.setWritingEncryptedData( true );
        }

        try
        {
            if( log.isDebugEnabled() )
            {
                log.debug( session + " write outNetBuffer: " +
                                   sslHandler.getOutNetBuffer() );
            }
            ByteBuffer writeBuffer = copy( sslHandler.getOutNetBuffer() );
            if( log.isDebugEnabled() )
            {
                log.debug( session + " session write: " + writeBuffer );
            }
            //debug("outNetBuffer (after copy): {0}", sslHandler.getOutNetBuffer());
            filterWrite( nextFilter, session, writeBuffer, SSL_MARKER );

            // loop while more writes required to complete handshake
            while( sslHandler.needToCompleteInitialHandshake() )
            {
                try
                {
                    sslHandler.continueHandshake( nextFilter );
                }
                catch( SSLException ssle )
                {
                    SSLException newSSLE = new SSLHandshakeException(
                            "Initial SSL handshake failed." );
                    newSSLE.initCause( ssle );
                    throw newSSLE;
                }
                if( sslHandler.getOutNetBuffer().hasRemaining() )
                {
                    if( log.isDebugEnabled() )
                    {
                        log.debug( session + " write outNetBuffer2: " +
                                           sslHandler.getOutNetBuffer() );
                    }
                    ByteBuffer writeBuffer2 = copy( sslHandler
                            .getOutNetBuffer() );
                    filterWrite( nextFilter, session, writeBuffer2, SSL_MARKER );
                }
            }
        }
        finally
        {
            synchronized( sslHandler )
            {
                sslHandler.setWritingEncryptedData( false );
            }
        }
    }

    /**
     * Creates a new Mina byte buffer that is a deep copy of the remaining bytes
     * in the given buffer (between index buf.position() and buf.limit())
     *
     * @param src the buffer to copy
     * @return the new buffer, ready to read from
     */
    private static ByteBuffer copy( java.nio.ByteBuffer src )
    {
        ByteBuffer copy = ByteBuffer.allocate( src.remaining() );
        copy.put( src );
        copy.flip();
        return copy;
    }

    // Utilities to mainpulate SSLHandler based on IoSession

    private SSLHandler createSSLSessionHandler( NextFilter nextFilter, IoSession session ) throws SSLException
    {
        SSLHandler handler = getSSLSessionHandler( session );
        if( handler == null )
        {
            synchronized( session )
            {
                handler = getSSLSessionHandler( session );
                if( handler == null )
                {
                    boolean done = false;
                    try
                    {
                        handler =
                            new SSLHandler( this, sslContext, session );
                        session.setAttribute( SSL_HANDLER, handler );
                        handler.doHandshake( nextFilter );
                        done = true;
                    }
                    finally 
                    {
                        if( !done )
                        {
                            session.removeAttribute( SSL_HANDLER );
                        }
                    }
                }
            }
        }
        
        return handler;
    }

    private SSLHandler getSSLSessionHandler( IoSession session )
    {
        return ( SSLHandler ) session.getAttribute( SSL_HANDLER );
    }

    private void removeSSLSessionHandler( IoSession session )
    {
        session.removeAttribute( SSL_HANDLER );
    }
}
