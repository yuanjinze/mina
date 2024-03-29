/*
 *   @(#) $Id: ProtocolEncoderOutput.java 326586 2005-10-19 15:50:29Z trustin $
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
 * Callback for {@link ProtocolEncoder} to generate encoded {@link ByteBuffer}s.
 * {@link ProtocolEncoder} must call {@link #write(ByteBuffer)} for each decoded
 * messages.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
public interface ProtocolEncoderOutput
{
    /**
     * Callback for {@link ProtocolEncoder} to generate encoded
     * {@link ByteBuffer}s. {@link ProtocolEncoder} must call
     * {@link #write(ByteBuffer)} for each decoded messages.
     * 
     * @param buf the buffer which contains encoded data
     */
    void write( ByteBuffer buf );
    
    /**
     * Merges all buffers you wrote via {@link #write(ByteBuffer)} into
     * one {@link ByteBuffer} and replaces the old fragmented ones with it.
     * This method is useful when you want to control the way MINA generates
     * network packets.
     */
    void mergeAll();
}