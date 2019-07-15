/*
 *   @(#) $Id: MessageHandler.java 330401 2005-11-03 01:09:13Z trustin $
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
package org.apache.mina.protocol.handler;

import org.apache.mina.protocol.ProtocolSession;

/**
 * A handler interface that {@link DemuxingProtocolHandler} forwards
 * <code>messageReceived</code> events to.  You have to register your
 * handler with the type of message you want to get notified using
 * {@link DemuxingProtocolHandler#addMessageHandler(Class, MessageHandler)}.
 * 
 * @author The Apache Directory Project
 * @version $Rev: 330401 $, $Date: 2005-11-03 09:09:13 +0800 (Thu, 03 Nov 2005) $
 */
public interface MessageHandler
{
    /**
     * A {@link MessageHandler} that does nothing.  This is usefule when
     * you want to ignore messages of the specific type silently.
     */
    static MessageHandler NOOP = new MessageHandler()
    {
        public void messageReceived( ProtocolSession session, Object message )
        {
        }
    };
    
    /**
     * Invoked when the specific type of message is received from the
     * specified <code>session</code>.
     */
    void messageReceived( ProtocolSession session, Object message ) throws Exception;
}