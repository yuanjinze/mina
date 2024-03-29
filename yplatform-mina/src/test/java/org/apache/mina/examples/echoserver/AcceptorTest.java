/*
 *   @(#) $Id: AcceptorTest.java 326586 2005-10-19 15:50:29Z trustin $
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
package org.apache.mina.examples.echoserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import org.apache.commons.net.EchoTCPClient;
import org.apache.commons.net.EchoUDPClient;
import org.apache.mina.common.TransportType;
import org.apache.mina.examples.echoserver.ssl.BogusSSLContextFactory;
import org.apache.mina.examples.echoserver.ssl.SSLServerSocketFactory;
import org.apache.mina.examples.echoserver.ssl.SSLSocketFactory;
import org.apache.mina.io.IoAcceptor;
import org.apache.mina.io.filter.SSLFilter;

/**
 * Tests echo server example.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
public class AcceptorTest extends AbstractTest
{
    public AcceptorTest()
    {
    }

    public void testTCP() throws Exception
    {
        EchoTCPClient client = new EchoTCPClient();
        testTCP0( client );
    }

    public void testTCPWithSSL() throws Exception
    {
        // Add an SSL filter
        SSLFilter sslFilter =
            new SSLFilter( BogusSSLContextFactory.getInstance( true ) );
        IoAcceptor acceptor = registry.getIoAcceptor( TransportType.SOCKET );
        acceptor.getFilterChain().addLast( "SSL", sslFilter );
        
        // Create a commons-net socket factory
        SSLSocketFactory.setSslEnabled(true);
        SSLServerSocketFactory.setSslEnabled(true);
        org.apache.commons.net.SocketFactory factory = new org.apache.commons.net.SocketFactory() {

            private SocketFactory f = SSLSocketFactory.getSocketFactory();
            private ServerSocketFactory ssf = SSLServerSocketFactory.getServerSocketFactory();

            public Socket createSocket( String arg0, int arg1 ) throws UnknownHostException, IOException
            {
                return f.createSocket(arg0, arg1);
            }

            public Socket createSocket( InetAddress arg0, int arg1 ) throws IOException
            {
                return f.createSocket(arg0, arg1);
            }

            public Socket createSocket( String arg0, int arg1, InetAddress arg2, int arg3 ) throws UnknownHostException, IOException
            {
                return f.createSocket(arg0, arg1, arg2, arg3);
            }

            public Socket createSocket( InetAddress arg0, int arg1, InetAddress arg2, int arg3 ) throws IOException
            {
                return f.createSocket(arg0, arg1, arg2, arg3);
            }

            public ServerSocket createServerSocket( int arg0 ) throws IOException
            {
                return ssf.createServerSocket(arg0);
            }

            public ServerSocket createServerSocket( int arg0, int arg1 ) throws IOException
            {
                return ssf.createServerSocket(arg0, arg1);
            }

            public ServerSocket createServerSocket( int arg0, int arg1, InetAddress arg2 ) throws IOException
            {
                return ssf.createServerSocket(arg0, arg1, arg2);
            }
            
        };
        
        // Create a echo client with SSL factory and test it.
        EchoTCPClient client = new EchoTCPClient();
        client.setSocketFactory( factory );
        testTCP0( client );
    }
    
    private void testTCP0( EchoTCPClient client ) throws Exception
    {
        client.connect( InetAddress.getLocalHost(), port );
        byte[] writeBuf = new byte[ 16 ];

        for( int i = 0; i < 10; i ++ )
        {
            fillWriteBuffer( writeBuf, i );
            client.getOutputStream().write( writeBuf );
        }

        client.setSoTimeout( 30000 );

        byte[] readBuf = new byte[ writeBuf.length ];

        for( int i = 0; i < 10; i ++ )
        {
            fillWriteBuffer( writeBuf, i );

            int readBytes = 0;
            while( readBytes < readBuf.length )
            {
                int nBytes = client.getInputStream().read( readBuf,
                        readBytes, readBuf.length - readBytes );

                if( nBytes < 0 )
                    fail( "Unexpected disconnection." );

                readBytes += nBytes;
            }

            assertEquals( writeBuf, readBuf );
        }

        client.setSoTimeout( 500 );

        try
        {
            client.getInputStream().read();
            fail( "Unexpected incoming data." );
        }
        catch( SocketTimeoutException e )
        {
        }

        client.disconnect();
    }

    public void testUDP() throws Exception
    {
        EchoUDPClient client = new EchoUDPClient();
        client.open();
        client.setSoTimeout( 3000 );

        byte[] writeBuf = new byte[ 16 ];
        byte[] readBuf = new byte[ writeBuf.length ];

        client.setSoTimeout( 500 );

        for( int i = 0; i < 10; i ++ )
        {
            fillWriteBuffer( writeBuf, i );
            client.send( writeBuf, writeBuf.length, InetAddress
                    .getLocalHost(), port );

            assertEquals( readBuf.length, client.receive( readBuf,
                    readBuf.length ) );
            assertEquals( writeBuf, readBuf );
        }

        try
        {
            client.receive( readBuf );
            fail( "Unexpected incoming data." );
        }
        catch( SocketTimeoutException e )
        {
        }

        client.close();
    }

    private void fillWriteBuffer( byte[] writeBuf, int i )
    {
        for( int j = writeBuf.length - 1; j >= 0; j -- )
        {
            writeBuf[ j ] = ( byte ) ( j + i );
        }
    }

    public static void main( String[] args )
    {
        junit.textui.TestRunner.run( AcceptorTest.class );
    }
}
