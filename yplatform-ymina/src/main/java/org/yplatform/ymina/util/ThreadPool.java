/**
*
*/
package org.yplatform.ymina.util;

/**
 * A generic thread pool interface.
 * @author yuanjinze
 *
 */
public interface ThreadPool {
    /**
     * Returns the number of threads in the thread pool.
     */
    int getPoolSize();
    /**
     * Returns the maximum size of the thread pool.
     */
    int getMaximumPoolSize();
    /**
     * Returns the keep-alive time until the thread suicides after it became
     * idle (milliseconds unit).
     */
    int getKeepAliveTime();

    void setMaximumPoolSize(int maximumPoolSize);
    void setKeepAliveTime(int keepAliveTime);

    /**
     * Starts thread pool threads and starts forwarding events to them.
     */
    void start();
    /**
     * Stops all thread pool threads.
     */
    void stop();
    

}
