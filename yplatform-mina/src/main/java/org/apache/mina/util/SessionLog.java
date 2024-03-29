/*
 *   @(#) $Id: SessionLog.java 326586 2005-10-19 15:50:29Z trustin $
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

import org.apache.mina.common.Session;
import org.apache.mina.io.IoSession;
import org.apache.mina.protocol.ProtocolSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides utility methods to log protocol-specific messages.
 * <p>
 * Set {@link #PREFIX} and {@link #LOGGER} session attributes
 * to override prefix string and logger.
 *
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 *
 */
public class SessionLog {
    /**
     * Session attribute key: prefix string
     */
    public static final String PREFIX = SessionLog.class.getName() + ".prefix";

    /**
     * Session attribute key: {@link Logger}
     */
    public static final String LOGGER = SessionLog.class.getName() + ".logger";
    
    public static Logger getLogger( Session session )
    {
        Logger log = (Logger) session.getAttribute( LOGGER );
        if( log == null )
        {
            log = LoggerFactory.getLogger( getClass( session ) );
            String prefix = ( String ) session.getAttribute( PREFIX );
            if( prefix == null )
            {
                prefix = "[" + session.getRemoteAddress() + "] ";
                session.setAttribute( PREFIX, prefix );
            }
                
            session.setAttribute( LOGGER, log );
        }
        
        return log;
    }
    
    private static Class getClass( Session session )
    {
        if( session instanceof IoSession )
            return ( ( IoSession ) session ).getHandler().getClass();
        else
            return ( ( ProtocolSession ) session ).getHandler().getClass();
    }

    public static void debug( Session session, String message )
    {
        Logger log = getLogger( session );
        if( log.isDebugEnabled() )
        {
            log.debug( String.valueOf( session.getAttribute( PREFIX ) ) + message );
        }
    }

    public static void debug( Session session, String message, Throwable cause )
    {
        Logger log = getLogger( session );
        if( log.isDebugEnabled() )
        {
            log.debug( String.valueOf( session.getAttribute( PREFIX ) ) + message, cause );
        }
    }

    public static void info( Session session, String message )
    {
        Logger log = getLogger( session );
        if( log.isInfoEnabled() )
        {
            log.info( String.valueOf( session.getAttribute( PREFIX ) ) + message );
        }
    }

    public static void info( Session session, String message, Throwable cause )
    {
        Logger log = getLogger( session );
        if( log.isInfoEnabled() )
        {
            log.info( String.valueOf( session.getAttribute( PREFIX ) ) + message, cause );
        }
    }

    public static void warn( Session session, String message )
    {
        Logger log = getLogger( session );
        if( log.isWarnEnabled() )
        {
            log.warn( String.valueOf( session.getAttribute( PREFIX ) ) + message );
        }
    }

    public static void warn( Session session, String message, Throwable cause )
    {
        Logger log = getLogger( session );
        if( log.isWarnEnabled() )
        {
            log.warn( String.valueOf( session.getAttribute( PREFIX ) ) + message, cause );
        }
    }

    public static void error( Session session, String message )
    {
        Logger log = getLogger( session );
        if( log.isErrorEnabled() )
        {
            log.error( String.valueOf( session.getAttribute( PREFIX ) ) + message );
        }
    }

    public static void error( Session session, String message, Throwable cause )
    {
        Logger log = getLogger( session );
        if( log.isErrorEnabled() )
        {
            log.error( String.valueOf( session.getAttribute( PREFIX ) ) + message, cause );
        }
    }
}
