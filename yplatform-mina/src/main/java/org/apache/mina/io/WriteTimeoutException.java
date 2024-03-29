/*
 *   @(#) $Id: WriteTimeoutException.java 326586 2005-10-19 15:50:29Z trustin $
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

import java.io.IOException;

import org.apache.mina.common.SessionConfig;

/**
 * An {@link IOException} which is thrown when write buffer is not flushed for
 * {@link SessionConfig#getWriteTimeout()} seconds.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $, 
 */
public class WriteTimeoutException extends IOException
{
    private static final long serialVersionUID = 3906931157944579121L;

	/**
     * Creates a new exception.
     */
    public WriteTimeoutException()
    {
        super();
    }

    /**
     * Creates a new exception.
     */
    public WriteTimeoutException( String s )
    {
        super( s );
    }
}