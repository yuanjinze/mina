/*
 *   @(#) $Id: AddMessageEncoder.java 209237 2005-07-05 07:44:16Z trustin $
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
package org.apache.mina.examples.sumup.codec;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.examples.sumup.message.AbstractMessage;
import org.apache.mina.examples.sumup.message.AddMessage;
import org.apache.mina.protocol.ProtocolSession;
import org.apache.mina.protocol.codec.MessageEncoder;

/**
 * A {@link MessageEncoder} that encodes {@link AddMessage}.
 *
 * @author The Apache Directory Project
 * @version $Rev: 209237 $, $Date: 2005-07-05 15:44:16 +0800 (Tue, 05 Jul 2005) $
 */
public class AddMessageEncoder extends AbstractMessageEncoder
{
    private static final Set TYPES;
   
    static
    {
        Set types = new HashSet();
        types.add( AddMessage.class );
        TYPES = Collections.unmodifiableSet( types );
    }

    public AddMessageEncoder()
    {
        super( Constants.ADD );
    }

    protected void encodeBody( ProtocolSession session, AbstractMessage message, ByteBuffer out )
    {
        AddMessage m = ( AddMessage ) message;
        out.putInt( m.getValue() );
    }

    public Set getMessageTypes()
    {
        return TYPES;
    }
}
