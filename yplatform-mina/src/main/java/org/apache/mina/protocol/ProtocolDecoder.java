/*
 *   @(#) $Id: ProtocolDecoder.java 326586 2005-10-19 15:50:29Z trustin $
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
package org.apache.mina.protocol;

import org.apache.mina.common.ByteBuffer;

/**
 * Decodes binary or protocol-specific data into higher-level message objects.
 * MINA invokes {@link #decode(ProtocolSession, ByteBuffer, ProtocolDecoderOutput)}
 * method with read data, and then the decoder implementation puts decoded
 * messages into {@link ProtocolDecoderOutput} by calling
 * {@link ProtocolDecoderOutput#write(Object)}.
 * <p>
 * Please refer to
 * <a href="../../../../../xref-examples/org/apache/mina/examples/reverser/TextLineDecoder.html"><code>TextLineDecoder</code></a>
 * example. 
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
public interface ProtocolDecoder
{
    /**
     * Decodes binary or protocol-specific content into higher-level message objects.
     * MINA invokes {@link #decode(ProtocolSession, ByteBuffer, ProtocolDecoderOutput)}
     * method with read data, and then the decoder implementation puts decoded
     * messages into {@link ProtocolDecoderOutput}.
     * 
     * @throws ProtocolViolationException if the read data violated protocol
     *                                    specification 
     */
    void decode( ProtocolSession session, ByteBuffer in,
                 ProtocolDecoderOutput out ) throws ProtocolViolationException;
}