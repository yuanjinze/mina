/*
 *   @(#) $Id: Main.java 326586 2005-10-19 15:50:29Z trustin $
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
package org.apache.mina.examples.netcat;

import java.net.InetSocketAddress;

import org.apache.mina.io.socket.SocketConnector;

/**
 * (<b>Entry point</b>) NetCat client.  NetCat client connects to the specified
 * endpoint and prints out received data.  NetCat client disconnects
 * automatically when no data is read for 10 seconds. 
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $,
 */
public class Main
{
    public static void main( String[] args ) throws Exception
    {
        if( args.length != 2 )
        {
            System.out.println( Main.class.getName() + " <hostname> <port>" );
            return;
        }

        // Create TCP/IP connector.
        SocketConnector connector = new SocketConnector();

        // Start communication.
        connector.connect( new InetSocketAddress( args[ 0 ], Integer
                .parseInt( args[ 1 ] ) ), 60, new NetCatProtocolHandler() );
    }
}
