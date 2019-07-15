/*
 *   @(#) $Id: ServerProtocolProvider.java 209237 2005-07-05 07:44:16Z trustin $
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

import org.apache.mina.examples.sumup.codec.SumUpProtocolCodecFactory;
import org.apache.mina.protocol.ProtocolCodecFactory;
import org.apache.mina.protocol.ProtocolHandler;
import org.apache.mina.protocol.ProtocolProvider;

/**
 * {@link ProtocolProvider} for SumUp server.
 * 
 * @author The Apache Directory Project
 * @version $Rev: 209237 $, $Date: 2005-07-05 15:44:16 +0800 (Tue, 05 Jul 2005) $,
 */
public class ServerProtocolProvider implements ProtocolProvider
{
    private static final ProtocolCodecFactory CODEC_FACTORY =
        new SumUpProtocolCodecFactory( true );

    private static final ProtocolHandler HANDLER = new ServerSessionHandler();

    public ServerProtocolProvider()
    {
    }

    public ProtocolCodecFactory getCodecFactory()
    {
        return CODEC_FACTORY;
    }

    public ProtocolHandler getHandler()
    {
        return HANDLER;
    }
}