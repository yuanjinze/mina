/**
 * 
 */
package org.apache.mina.protocol;

import org.apache.mina.protocol.ProtocolDecoderOutput;
import org.apache.mina.util.Queue;

/**
 * A {@link ProtocolDecoderOutput} based on queue.
 * 
 * @author The Apache Directory Project (dev@directory.apache.org)
 * @version $Rev: 326586 $, $Date: 2005-10-19 23:50:29 +0800 (Wed, 19 Oct 2005) $
 *
 */
public class SimpleProtocolDecoderOutput implements ProtocolDecoderOutput
{
    private final Queue messageQueue = new Queue();
    
    public SimpleProtocolDecoderOutput()
    {
    }
    
    public Queue getMessageQueue()
    {
        return messageQueue;
    }
    
    public void write( Object message )
    {
        messageQueue.push( message );
    }
}