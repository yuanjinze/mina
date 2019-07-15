/*
 *   @(#) $Id: IoFilterImpl.java 326586 2005-10-19 15:50:29Z trustin $
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
package org.apache.mina.util;

import org.apache.mina.io.IoFilter;
import org.apache.mina.io.IoFilterAdapter;
import org.apache.mina.io.IoFilterChain;

/**
 * Bogus implementation of {@link IoFilter} to test
 * {@link IoFilterChain}.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
public class IoFilterImpl extends IoFilterAdapter
{
    private final char c;

    public IoFilterImpl( char c )
    {
        this.c = c;
    }

    public int hashCode()
    {
        return c;
    }

    public boolean equals( Object o )
    {
        if( o == null )
            return false;
        if( ! ( o instanceof IoFilterImpl ) )
            return false;
        return this.c == ( ( IoFilterImpl ) o ).c;
    }

    public String toString()
    {
        return "" + c;
    }
}