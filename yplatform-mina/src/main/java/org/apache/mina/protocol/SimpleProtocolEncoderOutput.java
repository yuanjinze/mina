/**
 * 
 */
package org.apache.mina.protocol;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.protocol.ProtocolEncoderOutput;
import org.apache.mina.util.Queue;

/**
 * A {@link ProtocolEncoderOutput} based on queue.
 *
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 */
public class SimpleProtocolEncoderOutput implements ProtocolEncoderOutput
{

    private final Queue bufferQueue = new Queue();
    
    public SimpleProtocolEncoderOutput()
    {
    }
    
    public Queue getBufferQueue()
    {
        return bufferQueue;
    }
    
    public void write( ByteBuffer buf )
    {
        bufferQueue.push( buf );
    }
    
    public void mergeAll()
    {
        int sum = 0;
        final int size = bufferQueue.size();
        
        if( size < 2 )
        {
            // no need to merge!
            return;
        }
        
        // Get the size of merged BB
        for( int i = size - 1; i >= 0; i -- )
        {
            sum += ( ( ByteBuffer ) bufferQueue.get( i ) ).remaining();
        }
        
        // Allocate a new BB that will contain all fragments
        ByteBuffer newBuf = ByteBuffer.allocate( sum );
        
        // and merge all.
        for( ;; )
        {
            ByteBuffer buf = ( ByteBuffer ) bufferQueue.pop();
            if( buf == null )
            {
                break;
            }
    
            newBuf.put( buf );
            buf.release();
        }
        
        // Push the new buffer finally.
        newBuf.flip();
        bufferQueue.push(newBuf);
    }
}