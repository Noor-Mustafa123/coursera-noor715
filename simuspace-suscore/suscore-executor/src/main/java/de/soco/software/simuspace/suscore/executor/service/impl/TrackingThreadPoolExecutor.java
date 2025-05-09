package de.soco.software.simuspace.suscore.executor.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The Class TrackingThreadPoolExecutor A tracking thread pool is a usual thread pool that records the task execution statistics like total
 * number of tasks executed, average execution time of tasks and total execution time of all tasks.
 */
public class TrackingThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * The running.
     */
    private List< Runnable > running = Collections.synchronizedList( new ArrayList<>() );

    /**
     * The Is executor running.
     */
    private boolean isExecutorRunning;

    /**
     * The name.
     */
    private String name;

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
     * @param queue
     *         the queue
     * @param handler
     *         the handler
     */
    public TrackingThreadPoolExecutor( int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue< Runnable > queue, RejectedExecutionHandler handler, String name ) {
        super( corePoolSize, maximumPoolSize, keepAliveTime, unit, queue, handler );
        this.name = name;
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

    /**
     * Sets executor running.
     *
     * @param executorRunning
     *         the executor running
     */
    public void setExecutorRunning( boolean executorRunning ) {
        isExecutorRunning = executorRunning;
    }

    /**
     * Is executor running boolean.
     *
     * @return the boolean
     */
    public boolean isExecutorRunning() {
        return isExecutorRunning;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name
     *         the name
     */
    public void setName( String name ) {
        this.name = name;
    }

}
