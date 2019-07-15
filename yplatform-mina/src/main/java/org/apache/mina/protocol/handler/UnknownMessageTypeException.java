/*
 *   @(#) $Id: UnknownMessageTypeException.java 325850 2005-10-17 07:07:19Z trustin $
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


/**
 * An exception that is thrown when {@link DemuxingProtocolHandler}
 * cannot find any {@link MessageHandler}s associated with the specific
 * message type.  You have to use
 * {@link DemuxingProtocolHandler#addMessageHandler(Class, MessageHandler)}
 * to associate a message type and a message handler. 
 * 
 * @author The Apache Directory Project
 * @version $Rev: 325850 $, $Date: 2005-10-17 15:07:19 +0800 (Mon, 17 Oct 2005) $
 */
public class UnknownMessageTypeException extends RuntimeException
{
    private static final long serialVersionUID = 3257290227428047158L;

    public UnknownMessageTypeException()
    {
    }

    public UnknownMessageTypeException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public UnknownMessageTypeException( String message )
    {
        super( message );
    }

    public UnknownMessageTypeException( Throwable cause )
    {
        super( cause );
    }
}