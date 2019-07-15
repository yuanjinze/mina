/*
 *   @(#) $Id: ProtocolHandlerAdapter.java 326586 2005-10-19 15:50:29Z trustin $
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

import org.apache.mina.common.IdleStatus;
import org.apache.mina.util.SessionUtil;

/**
 * An abstract adapter class for {@link ProtocolHandler}.  You can extend this
 * class and selectively override required event handler methods only.  All
 * methods do nothing by default. 
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
public class ProtocolHandlerAdapter implements ProtocolHandler
{
    public void sessionCreated( ProtocolSession session ) throws Exception
    {
        SessionUtil.initialize( session );
    }

    public void sessionOpened( ProtocolSession session ) throws Exception
    {
    }

    public void sessionClosed( ProtocolSession session ) throws Exception
    {
    }

    public void sessionIdle( ProtocolSession session, IdleStatus status ) throws Exception
    {
    }

    public void exceptionCaught( ProtocolSession session, Throwable cause ) throws Exception
    {
    }

    public void messageReceived( ProtocolSession session, Object message ) throws Exception
    {
    }

    public void messageSent( ProtocolSession session, Object message ) throws Exception
    {
    }
}