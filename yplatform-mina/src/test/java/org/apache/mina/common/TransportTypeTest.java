/*
 *   @(#) $Id: TransportTypeTest.java 326586 2005-10-19 15:50:29Z trustin $
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
package org.apache.mina.common;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Tests {@link TransportType}.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
public class TransportTypeTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TransportTypeTest.class);
    }
    
    public void testRegistration()
    {
        TransportType myType = new TransportType( new String[] { "a", "b", "c" }, true );
        
        Assert.assertSame( myType, TransportType.getInstance( "a" ) );
        Assert.assertSame( myType, TransportType.getInstance( "A" ) );
        Assert.assertSame( myType, TransportType.getInstance( "b" ) );
        Assert.assertSame( myType, TransportType.getInstance( "B" ) );
        Assert.assertSame( myType, TransportType.getInstance( "c" ) );
        Assert.assertSame( myType, TransportType.getInstance( "C" ) );
        try
        {
            TransportType.getInstance( "unknown" );
            Assert.fail();
        }
        catch( IllegalArgumentException e )
        {
            // ignore
        }
        
        try
        {
            new TransportType( new String[] { "A" }, false );
            Assert.fail();
        }
        catch( IllegalArgumentException e )
        {
            // ignore
        }
    }

}
