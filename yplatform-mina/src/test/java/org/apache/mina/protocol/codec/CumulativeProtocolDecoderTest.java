/*
 *   @(#) $Id: CumulativeProtocolDecoderTest.java 326586 2005-10-19 15:50:29Z trustin $
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
package org.apache.mina.protocol.codec;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.mina.common.BaseSession;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.SessionConfig;
import org.apache.mina.common.TransportType;
import org.apache.mina.protocol.ProtocolDecoder;
import org.apache.mina.protocol.ProtocolDecoderOutput;
import org.apache.mina.protocol.ProtocolEncoder;
import org.apache.mina.protocol.ProtocolHandler;
import org.apache.mina.protocol.ProtocolFilterChain;
import org.apache.mina.protocol.ProtocolSession;
import org.apache.mina.protocol.ProtocolViolationException;

/**
 * Tests {@link CumulativeProtocolDecoder}.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $ 
 */
public class CumulativeProtocolDecoderTest extends TestCase
{
    private final ProtocolSession session = new ProtocolSessionImpl();
    private ByteBuffer buf;
    private IntegerDecoder decoder;
    private IntegerDecoderOutput output;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(CumulativeProtocolDecoderTest.class);
    }

    protected void setUp() throws Exception
    {
        buf = ByteBuffer.allocate( 16 );
        decoder = new IntegerDecoder();
        output = new IntegerDecoderOutput();
    }

    protected void tearDown() throws Exception
    {
    }
    
    public void testCumulation() throws Exception
    {
        buf.put( (byte) 0 );
        buf.flip();
        
        decoder.decode( session, buf, output );
        Assert.assertEquals( 0, output.getValues().size() );
        Assert.assertEquals( buf.limit(), buf.position() );
        
        buf.clear();
        buf.put( (byte) 0 );
        buf.put( (byte) 0 );
        buf.put( (byte) 1 );
        buf.flip();

        decoder.decode( session, buf, output );
        Assert.assertEquals( 1, output.getValues().size() );
        Assert.assertEquals( new Integer( 1 ), output.getValues().get( 0 ) );
        Assert.assertEquals( buf.limit(), buf.position() );
    }
    
    public void testRepeatitiveDecode() throws Exception
    {
        for( int i = 0; i < 4; i ++ )
        {
            buf.putInt( i );
        }
        buf.flip();
        
        decoder.decode( session, buf, output );
        Assert.assertEquals( 4, output.getValues().size() );
        Assert.assertEquals( buf.limit(), buf.position() );
        
        List expected = new ArrayList();
        for( int i = 0; i < 4; i ++ )
        {
            expected.add( new Integer( i ) );
        }
        Assert.assertEquals( expected, output.getValues() );
        
    }
    
    public void testWrongImplementationDetection() throws Exception {
        try
        {
            new WrongDecoder().decode( session, buf, output );
            Assert.fail();
        }
        catch( IllegalStateException e )
        {
            // OK
        }
    }
    
    private static class IntegerDecoder extends CumulativeProtocolDecoder
    {
        protected IntegerDecoder()
        {
            super( 4 );
        }

        protected boolean doDecode( ProtocolSession session, ByteBuffer in,
                                    ProtocolDecoderOutput out ) throws ProtocolViolationException
        {
            Assert.assertTrue( in.hasRemaining() );
            if( in.remaining() < 4 )
                return false;
            
            out.write( new Integer( in.getInt() ) );
            return true;
        }
        
    }
    
    private static class IntegerDecoderOutput implements ProtocolDecoderOutput
    {
        private List values = new ArrayList();

        public void write( Object message )
        {
            values.add( message );
        }
        
        public List getValues()
        {
            return values;
        }
        
        public void clear()
        {
            values.clear();
        }
    }
    
    private static class WrongDecoder extends CumulativeProtocolDecoder
    {
        public WrongDecoder()
        {
            super( 4 );
        }

        protected boolean doDecode( ProtocolSession session, ByteBuffer in,
                                    ProtocolDecoderOutput out ) throws ProtocolViolationException {
            return true;
        }
    }
    
    private static class ProtocolSessionImpl extends BaseSession implements ProtocolSession
    {

        public ProtocolHandler getHandler() {
            return null;
        }

        public ProtocolEncoder getEncoder() {
            return null;
        }

        public ProtocolDecoder getDecoder() {
            return null;
        }

        public void close( boolean wait ) {
        }

        public void write(Object message) {
        }

        public int getScheduledWriteRequests()
        {
            return 0;
        }

        public TransportType getTransportType() {
            return TransportType.SOCKET;
        }

        public boolean isConnected() {
            return false;
        }

        public SessionConfig getConfig() {
            return null;
        }

        public SocketAddress getRemoteAddress() {
            return null;
        }

        public SocketAddress getLocalAddress() {
            return null;
        }

        public ProtocolFilterChain getFilterChain()
        {
            return null;
        }
    }
}
