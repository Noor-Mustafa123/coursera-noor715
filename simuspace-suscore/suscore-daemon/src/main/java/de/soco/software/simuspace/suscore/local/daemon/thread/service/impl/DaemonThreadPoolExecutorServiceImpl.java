package de.soco.software.simuspace.suscore.local.daemon.thread.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.local.daemon.constants.DaemonThreadPoolConstants;
import de.soco.software.simuspace.suscore.local.daemon.constants.DaemonThreadPoolProperties;
import de.soco.software.simuspace.suscore.local.daemon.thread.DaemonTrackingThreadPoolExecutor;
import de.soco.software.simuspace.suscore.local.daemon.thread.RunDaemonSchemeJobThread;
import de.soco.software.simuspace.suscore.local.daemon.thread.service.DaemonExecutorBaseService;
import de.soco.software.simuspace.suscore.local.daemon.thread.service.DaemonThreadPoolExecutorService;

/**
 * The Class DaemonThreadPoolExecutorServiceImpl.
 *
 * @author noman arshad
 */
@Component
public class DaemonThreadPoolExecutorServiceImpl extends DaemonExecutorBaseService implements DaemonThreadPoolExecutorService {

    /**
     * The Constant EXECUTOR_CALLED.
     */
    private static final String EXECUTOR_CALLED = "Executor Called";

    /**
     * The workflow pool executor.
     */
    protected DaemonTrackingThreadPoolExecutor workflowPoolExecutor;

    /**
     * The is workflow pool executor running.
     */
    private boolean isWorkflowPoolExecutorRunning;

    /**
     * The future map.
     */
    private Map< UUID, Future< ? > > futureMap = new HashMap<>();

    /**
     * The thread map.
     */
    private Map< UUID, Runnable > threadMap = new HashMap<>();

    /**
     * logger for messages.
     */
    private final Logger logger = Logger.getLogger( DaemonThreadPoolExecutorServiceImpl.class.getName() );

    /**
     * Instantiates a new thread pool executor service impl.
     */
    public DaemonThreadPoolExecutorServiceImpl() {
        super( DaemonThreadPoolExecutorServiceImpl.class );
    }

    /**
     * Setup thread pool.
     *
     * @param threadPool
     *         the thread pool
     *
     * @return ThreadPoolExecutor with supplied configurations
     */
    private DaemonTrackingThreadPoolExecutor setupThreadPool( DaemonTrackingThreadPoolExecutor threadPool ) {

        logger.debug( "Loading Daemon Thread Pool values " );
        if ( DaemonThreadPoolProperties.getCorePoolSize() == null ) {
            // incase of file do not exists default values will be loaded
            threadPool = new DaemonTrackingThreadPoolExecutor( Integer.parseInt( "1" ), Integer.parseInt( "1" ), Long.parseLong( "300" ),
                    TimeUnit.SECONDS );
        } else {
            threadPool = new DaemonTrackingThreadPoolExecutor( Integer.parseInt( DaemonThreadPoolProperties.getCorePoolSize() ),
                    Integer.parseInt( DaemonThreadPoolProperties.getCoreMaxSize() ),
                    Long.parseLong( DaemonThreadPoolProperties.getKeepAliveTime() ), TimeUnit.SECONDS );

        }

        return threadPool;
    }

    /**
     * Checks if is pool shutdown or terminated.
     *
     * @param poolCode
     *         the pool code
     *
     * @return true, if is pool shutdown or terminated
     */
    public boolean isPoolShutdownOrTerminated( final int poolCode ) {

        switch ( poolCode ) {
            case DaemonThreadPoolConstants.LOCAL_WF_POOL:
                if ( null != workflowPoolExecutor ) {
                    return getPoolStatus( workflowPoolExecutor );
                }
                break;

            default:
                break;
        }

        return true;
    }

    /**
     * Gets the pool status.
     *
     * @param executorPool
     *         the executor pool
     *
     * @return the pool status
     */
    private boolean getPoolStatus( final ThreadPoolExecutor executorPool ) {

        return executorPool.isShutdown() || executorPool.isTerminating();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void daemonWorkflowExecute( Runnable workflowRunnable, UUID jobId ) {

        boolean executorStatus = isPoolShutdownOrTerminated( DaemonThreadPoolConstants.LOCAL_WF_POOL );

        if ( executorStatus ) {
            logger.debug( EXECUTOR_CALLED + ConstantsString.WORKFLOW + ": setupThreadPool" );
            workflowPoolExecutor = setupThreadPool( workflowPoolExecutor );
        }

        setWorkflowPoolExecutorRunning( true );
        threadMap.put( jobId, workflowRunnable );
        Future< ? > future = workflowPoolExecutor.submit( workflowRunnable );
        futureMap.put( jobId, future );
        logger.debug( EXECUTOR_CALLED + ConstantsString.WORKFLOW );
        monitorThreadPool();
    }

    public void monitorThreadPool() {
        StringBuffer strBuff = new StringBuffer();
        strBuff.append( "CurrentPoolSize : " ).append( workflowPoolExecutor.getPoolSize() );
        strBuff.append( " - CorePoolSize : " ).append( workflowPoolExecutor.getCorePoolSize() );
        strBuff.append( " - MaximumPoolSize : " ).append( workflowPoolExecutor.getMaximumPoolSize() );
        strBuff.append( " - ActiveTaskCount : " ).append( workflowPoolExecutor.getActiveCount() );
        strBuff.append( " - CompletedTaskCount : " ).append( workflowPoolExecutor.getCompletedTaskCount() );
        strBuff.append( " - TotalTaskCount : " ).append( workflowPoolExecutor.getTaskCount() );
        strBuff.append( " - isTerminated : " ).append( workflowPoolExecutor.isTerminated() );
        strBuff.append( " - workflowPoolExecutor status : " )
                .append( isPoolShutdownOrTerminated( DaemonThreadPoolConstants.LOCAL_WF_POOL ) );

        logger.debug( strBuff.toString() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkAndshutDownThreadPool( final int poolCode ) {

        switch ( poolCode ) {
            case DaemonThreadPoolConstants.LOCAL_WF_POOL:

                checkAndShutdownWorkflowPool();
                break;

            default:
                break;
        }

    }

    /**
     * Check and shutdown workflow pool.
     */
    private void checkAndShutdownWorkflowPool() {
        if ( null != workflowPoolExecutor ) {
            workflowPoolExecutor.shutdown();
            setWorkflowPoolExecutorRunning( false );
        }
    }

    /**
     * Checks if is workflow pool executor running.
     *
     * @return true, if is workflow pool executor running
     */
    public boolean isWorkflowPoolExecutorRunning() {
        return isWorkflowPoolExecutorRunning;
    }

    /**
     * Sets the workflow pool executor running.
     *
     * @param isWorkflowPoolExecutorRunning
     *         the new workflow pool executor running
     */
    public void setWorkflowPoolExecutorRunning( boolean isWorkflowPoolExecutorRunning ) {
        this.isWorkflowPoolExecutorRunning = isWorkflowPoolExecutorRunning;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopJobExecution( UUID jobId, String userName ) {
        boolean isStop = false;
        Future< ? > service = futureMap.get( jobId );

        UserThread objectThread = ( UserThread ) threadMap.get( jobId );
        if ( objectThread != null ) {
            objectThread.setStoppedBy( userName );
        }

        if ( service != null ) {
            service.cancel( true );
            try {
                objectThread.blockUntilInterrupted();
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
            isStop = service.isCancelled();
        }
        futureMap.remove( service );
        threadMap.remove( objectThread );
        return isStop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopSchemeJobExecution( UUID jobId, String userName ) {
        Future< ? > service = futureMap.get( jobId );

        RunDaemonSchemeJobThread schemeThread = ( RunDaemonSchemeJobThread ) threadMap.get( jobId );
        if ( schemeThread != null ) {
            schemeThread.setStoppedBy( userName );
            schemeThread.stop();
        }

        if ( service != null ) {
            service.cancel( true );
            try {
                schemeThread.blockUntilInterrupted();
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
        futureMap.remove( service );
        threadMap.remove( schemeThread );
        return true;
    }

}