/*
 *   @(#) $Id: IoHandlerAdapter.java 326586 2005-10-19 15:50:29Z trustin $
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
package org.apache.mina.io;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.util.SessionUtil;

/**
 * An abstract adapter class for {@link IoHandler}.  You can extend this class
 * and selectively override required event handler methods only.  All methods
 * do nothing by default.
 * <p>
 * Please refer to
 * <a href="../../../../../xref-examples/org/apache/mina/examples/netcat/NetCatProtocolHandler.html"><code>NetCatProtocolHandler</code></a>
 * example. 
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
public class IoHandlerAdapter implements IoHandler
{
    public void sessionCreated( IoSession session ) throws Exception
    {
        SessionUtil.initialize( session );
    }

    public void sessionOpened( IoSession session ) throws Exception
    {
    }

    public void sessionClosed( IoSession session ) throws Exception
    {
    }

    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
    {
    }

    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
    {
    }

    public void dataRead( IoSession session, ByteBuffer buf ) throws Exception
    {
    }

    public void dataWritten( IoSession session, Object marker ) throws Exception
    {
    }
}