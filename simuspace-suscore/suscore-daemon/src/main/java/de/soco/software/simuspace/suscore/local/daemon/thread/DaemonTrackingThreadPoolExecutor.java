package de.soco.software.simuspace.suscore.local.daemon.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The Class TrackingThreadPoolExecutor A tracking thread pool is a usual thread pool that records the task execution statistics like total
 * number of tasks executed, average execution time of tasks and total execution time of all tasks.
 *
 * @author noman arshad
 */
public class DaemonTrackingThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * The running.
     */
    private List< Runnable > running = Collections.synchronizedList( new ArrayList< Runnable >() );

    /**
     * Instantiates a new tracking thread pool executor.
     *
     * @param corePoolSize
     *         the core pool size
     * @param maximumPoolSize
     *         the maximum pool size
     * @param keepAliveTime
     *         the keep alive time
     * @param unit
     *         the unit
     */
    public DaemonTrackingThreadPoolExecutor( int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit ) {
        super( corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>() );
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread,
     * java.lang.Runnable)
     */
    @Override
    protected void beforeExecute( Thread t, Runnable r ) {
        super.beforeExecute( t, r );
        running.add( r );
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable,
     * java.lang.Throwable)
     */
    @Override
    protected void afterExecute( Runnable r, Throwable t ) {
        super.afterExecute( r, t );
        running.remove( r );
    }

    /**
     * Gets the running.
     *
     * @return the running
     */
    public List< Runnable > getRunning() {
        return running;
    }

}