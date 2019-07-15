/*
 *   @(#) $Id: Client.java 209237 2005-07-05 07:44:16Z trustin $
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
package org.apache.mina.examples.sumup;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.io.filter.IoThreadPoolFilter;
import org.apache.mina.io.socket.SocketConnector;
import org.apache.mina.protocol.ProtocolProvider;
import org.apache.mina.protocol.ProtocolSession;
import org.apache.mina.protocol.filter.ProtocolThreadPoolFilter;
import org.apache.mina.protocol.io.IoProtocolConnector;

/**
 * (<strong>Entry Point</strong>) Starts SumUp client.
 * 
 * @author The Apache Directory Project
 * @version $Rev: 209237 $, $Date: 2005-07-05 15:44:16 +0800 (Tue, 05 Jul 2005) $
 */
public class Client
{
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8080;
    private static final int CONNECT_TIMEOUT = 30; // seconds

    public static void main( String[] args ) throws Throwable
    {
        if( args.length == 0 )
        {
            System.out.println( "Please specify the list of any integers" );
            return;
        }

        // prepare values to sum up
        int[] values = new int[ args.length ];
        for( int i = 0; i < args.length; i++ )
        {
            values[ i ] = Integer.parseInt( args[ i ] );
        }

        // Create I/O and Protocol thread pool filter.
        // I/O thread pool performs encoding and decoding of messages.
        // Protocol thread pool performs actual protocol flow.
        IoThreadPoolFilter ioThreadPoolFilter = new IoThreadPoolFilter();
        ProtocolThreadPoolFilter protocolThreadPoolFilter = new ProtocolThreadPoolFilter();

        // and start both.
        ioThreadPoolFilter.start();
        protocolThreadPoolFilter.start();

        IoProtocolConnector connector = new IoProtocolConnector(
                new SocketConnector() );
        connector.getIoConnector().getFilterChain().addFirst( "threadPool",
                ioThreadPoolFilter );
        connector.getFilterChain().addFirst( "threadPool",
                protocolThreadPoolFilter );

        ProtocolProvider protocolProvider = new ClientProtocolProvider( values );
        ProtocolSession session;
        for( ;; )
        {
            try
            {
                session = connector.connect( new InetSocketAddress( HOSTNAME,
                        PORT ), CONNECT_TIMEOUT, protocolProvider );
                break;
            }
            catch( IOException e )
            {
                System.err.println( "Failed to connect." );
                e.printStackTrace();
                Thread.sleep( 5000 );
            }
        }

        // wait until the summation is done
        while( session.isConnected() )
        {
            Thread.sleep( 100 );
        }

        ioThreadPoolFilter.stop();
        protocolThreadPoolFilter.stop();
    }
}
