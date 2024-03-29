/*
 *   @(#) $Id: IdentityHashSet.java 330965 2005-11-05 03:38:32Z trustin $
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
package org.yplatform.ymina.v083v1;

import java.util.AbstractSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An {@link IdentityHashMap}-backed {@link Set}.
 *
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 330965 $, $Date: 2005-11-05 11:38:32 +0800 (Sat, 05 Nov 2005) $
 */
public class IdentityHashSet extends AbstractSet
{
    private final Map delegate = new IdentityHashMap();

    public IdentityHashSet()
    {
    }

    public int size()
    {
        return delegate.size();
    }

    public boolean contains( Object o )
    {
        return delegate.containsKey( o );
    }

    public Iterator iterator()
    {
        return delegate.keySet().iterator();
    }

    public boolean add( Object arg0 )
    {
        return delegate.put( arg0, Boolean.TRUE ) == null;
    }

    public boolean remove( Object o )
    {
        return delegate.remove( o ) != null;
    }

    public void clear()
    {
        delegate.clear();
    }
}
