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
package org.apache.mina.examples.reverser;

import org.apache.mina.common.TransportType;
import org.apache.mina.protocol.ProtocolAcceptor;
import org.apache.mina.protocol.filter.ProtocolLoggingFilter;
import org.apache.mina.registry.Service;
import org.apache.mina.registry.ServiceRegistry;
import org.apache.mina.registry.SimpleServiceRegistry;

/**
 * (<b>Entry point</b>) Reverser server which reverses all text lines from
 * clients.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $,
 */
public class Main
{
    private static final int PORT = 8080;

    public static void main( String[] args ) throws Exception
    {
        ServiceRegistry registry = new SimpleServiceRegistry();

        addLogger( registry );
        // Bind
        Service service = new Service( "reverse", TransportType.SOCKET, PORT );
        registry.bind( service, new ReverseProtocolProvider() );

        System.out.println( "Listening on port " + PORT );
    }

    private static void addLogger( ServiceRegistry registry )
    {
        ProtocolAcceptor acceptor = registry.getProtocolAcceptor( TransportType.SOCKET );
        acceptor.getFilterChain().addLast( "logger", new ProtocolLoggingFilter() );
        System.out.println( "Logging ON" );
    }
}
