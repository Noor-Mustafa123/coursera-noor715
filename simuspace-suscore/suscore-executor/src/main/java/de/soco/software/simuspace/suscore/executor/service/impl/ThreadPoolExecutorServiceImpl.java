package de.soco.software.simuspace.suscore.executor.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.ExecutionHosts;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.executor.enums.SchedulerType;
import de.soco.software.simuspace.suscore.executor.enums.ThreadPoolEnum;
import de.soco.software.simuspace.suscore.executor.manager.ExecutorPropertiesManager;
import de.soco.software.simuspace.suscore.executor.model.ExecutorPool;
import de.soco.software.simuspace.suscore.executor.model.ExecutorProperties;
import de.soco.software.simuspace.suscore.executor.model.Pools;
import de.soco.software.simuspace.suscore.executor.service.ExecutorBaseService;
import de.soco.software.simuspace.suscore.executor.service.ThreadPoolExecutorService;

/**
 * Class is responsible for executing commands submitted by user on Linux shell. A thread pool is created which will execute 2 commands in
 * parallel and keep the rest in blocking queue.
 *
 * @author Noman Arshad , Shan Arshad
 */
@Log4j2
public class ThreadPoolExecutorServiceImpl extends ExecutorBaseService implements ThreadPoolExecutorService {

    /**
     * The Constant EXECUTOR_CALLED.
     */
    private static final String EXECUTOR_CALLED = "Executor Called ";

    /**
     * The executor map.
     */
    private final Map< String, TrackingThreadPoolExecutor > executorMap = new HashMap<>();

    /**
     * The scheduled pool executor.
     */
    protected ScheduledExecutorService scheduledPoolExecutor;

    /**
     * The is scheduled pool executor running.
     */
    private boolean isScheduledPoolExecutorRunning;

    /**
     * The executor pool.
     */
    private ExecutorPool executorPool;

    /**
     * The future map.
     */
    private final Map< UUID, Future< ? > > futureMap = new HashMap<>();

    /**
     * The thread map.
     */
    private final Map< UUID, Runnable > threadMap = new HashMap<>();

    /**
     * The singleton instance.
     */
    private static ThreadPoolExecutorServiceImpl instance = null;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ThreadPoolExecutorServiceImpl getInstance() {
        if ( instance == null ) {
            instance = new ThreadPoolExecutorServiceImpl();
        }
        return instance;
    }

    /**
     * Instantiates a new thread pool executor service impl.
     */
    private ThreadPoolExecutorServiceImpl() {
        super();
    }

    /**
     * Setup thread pool.
     *
     * @param name
     *         the name
     * @param pool
     *         the pool
     *
     * @return ThreadPoolExecutor with supplied configurations
     */
    private TrackingThreadPoolExecutor setupExecutor( String name, Pools pool ) {
        ExecutorProperties properties = Arrays.stream( pool.getProperties() ).findAny().orElse( null );
        TrackingThreadPoolExecutor threadPool = null;
        if ( properties != null ) {
            threadPool = new TrackingThreadPoolExecutor( Integer.parseInt( properties.getCorePoolSize() ),
                    Integer.parseInt( properties.getCoreMaxSize() ), Long.parseLong( properties.getKeepAliveTime() ), TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>( Integer.parseInt( properties.getQueueSize() ) ), new RejectedExecutionHandlerImpl(),
                    pool.getName() );
            executorMap.put( name, threadPool );
            if ( log.isTraceEnabled() ) {
                logThreadPoolInformation( threadPool, name );
            }
        }
        return threadPool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutorShutdownOrTerminated( TrackingThreadPoolExecutor pool ) {
        return pool == null || pool.isShutdown() || pool.isTerminated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutorShutdownOrTerminated( String name ) {
        TrackingThreadPoolExecutor pool = executorMap.get( name );
        return pool == null || pool.isShutdown() || pool.isTerminated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportExecute( Runnable exportRunnable ) {
        executeRunnableWithExecutor( exportRunnable, ThreadPoolEnum.EXPORT.getName() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void workflowExecute( Runnable workflowRunnable, UUID jobId ) {
        submitRunnableWithExecutor( workflowRunnable, ThreadPoolEnum.WORKFLOW.getName(), jobId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void schemeExecute( Runnable workflowRunnable, UUID jobId ) {
        submitRunnableWithExecutor( workflowRunnable, ThreadPoolEnum.SCHEME.getName(), jobId );
    }

    /**
     * System workflow execute.
     *
     * @param workflowRunnable
     *         the workflow runnable
     * @param jobId
     *         the job id
     */
    @Override
    public void systemWorkflowExecute( Runnable workflowRunnable, UUID jobId ) {
        submitRunnableWithExecutor( workflowRunnable, ThreadPoolEnum.SYSTEM_WORKFLOW.getName(), jobId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void archiveExecute( Runnable archiveRunnable ) {
        executeRunnableWithExecutor( archiveRunnable, ThreadPoolEnum.ARCHIVE.getName() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void importExecute( Runnable importRunnable ) {
        executeRunnableWithExecutor( importRunnable, ThreadPoolEnum.IMPORT.getName() );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void indexingExecute( Runnable indexingRunnable ) {
        executeRunnableWithExecutor( indexingRunnable, ThreadPoolEnum.INDEXING.getName() );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lifeCycleExecute( Runnable lifeCycleRunnable ) {
        executeRunnableWithExecutor( lifeCycleRunnable, ThreadPoolEnum.LIFECYCLE.getName() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void thumbnailExecute( Runnable thumbnailRunnable ) {
        executeRunnableWithExecutor( thumbnailRunnable, ThreadPoolEnum.THUMBNAIL.getName() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeFFmpeg( Runnable ffmpegRunnable ) {
        executeRunnableWithExecutor( ffmpegRunnable, ThreadPoolEnum.FFMPEG.getName() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteExecute( UUID jobId, Runnable deleteRunnable ) {
        submitRunnableWithExecutor( deleteRunnable, ThreadPoolEnum.DELETE.getName(), jobId );
    }

    /**
     * Upload execute.
     *
     * @param uploadRunnable
     *         the upload runnable
     */
    @Override
    public void uploadExecute( Runnable uploadRunnable ) {
        executeRunnableWithExecutor( uploadRunnable, ThreadPoolEnum.UPLOAD.getName() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportDataobjectExecute( Runnable exportdataobject ) {
        executeRunnableWithExecutor( exportdataobject, ThreadPoolEnum.EXPORT.getName() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture< ? > registerAndExecuteScheduledTask( Runnable task, TimeUnit timeUnit, SchedulerType schedulerType,
            int initialDelay, long interval ) {

        int corePoolSize = 0;
        ScheduledFuture< ? > future = null;

        if ( null == task || initialDelay < ConstantsInteger.INTEGER_VALUE_ZERO || interval <= ConstantsInteger.INTEGER_VALUE_ZERO
                || null == timeUnit ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.METHOD_INVALID_ARGUMENT.getKey(), getClass() ) );
        }

        boolean executorStatus = scheduledPoolExecutor == null || scheduledPoolExecutor.isShutdown()
                || scheduledPoolExecutor.isTerminated();

        for ( Pools pool : getExecutorPoolFromConf().getPools() ) {
            if ( pool.getName().equals( ConstantsString.DELETE ) ) {

                for ( ExecutorProperties property : pool.getProperties() ) {

                    corePoolSize = Integer.parseInt( property.getCorePoolSize() );
                    break;
                }
                break;
            }
        }
        if ( executorStatus ) {
            scheduledPoolExecutor = Executors.newScheduledThreadPool( corePoolSize );
        }

        if ( SchedulerType.FIXED_DELAY == schedulerType ) {

            future = scheduledPoolExecutor.scheduleWithFixedDelay( task, initialDelay, interval, timeUnit );

        } else if ( SchedulerType.FIXED_RATE == schedulerType ) {

            future = scheduledPoolExecutor.scheduleAtFixedRate( task, initialDelay, interval, timeUnit );
        }

        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopJobExecution( String userName, UUID jobId ) {
        boolean isStop = false;

        UserThread objectThread = ( UserThread ) threadMap.get( jobId );
        if ( objectThread != null ) {
            objectThread.setStoppedBy( userName );
            Future< ? > future = futureMap.get( jobId );
            if ( future != null ) {
                future.cancel( true );
                try {
                    objectThread.blockUntilInterrupted();
                } catch ( InterruptedException e ) {
                    log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
                }
                isStop = true;
            }
        }
        futureMap.remove( jobId );
        threadMap.remove( jobId );

        return isStop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopJobs( List< UUID > cancelledJobList ) {
        for ( UUID cancelJobId : cancelledJobList ) {
            Future< ? > service = futureMap.get( cancelJobId );
            if ( service != null ) {
                service.cancel( true );
            }
            futureMap.remove( cancelJobId );
            threadMap.remove( cancelJobId );
        }
    }

    /**
     * Check and setup executor.
     *
     * @param name
     *         the name
     *
     * @return the tracking thread pool executor
     */
    private TrackingThreadPoolExecutor checkAndSetupExecutor( String name ) {
        if ( !isExecutorShutdownOrTerminated( name ) ) {
            return executorMap.get( name );
        } else {
            Pools matchingPool;
            try {
                matchingPool = getExecutorPoolFromConf().getPools().stream().filter( pool -> name.equals( pool.getName() ) ).findFirst()
                        .orElseThrow();
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( MessageBundleFactory.getMessage( Messages.EXECUTOR_NOT_CONFIGURED_IN_CONF.getKey(), name ), e );
            }
            return setupExecutor( name, matchingPool );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkAndShutDownExecutorPool( String name ) {
        TrackingThreadPoolExecutor poolToShutDown = executorMap.get( name );
        if ( null != poolToShutDown ) {
            poolToShutDown.shutdown();
            poolToShutDown.setExecutorRunning( false );
        }
    }

    /**
     * Execute runnable with executor.
     *
     * @param runnable
     *         the runnable
     * @param executorName
     *         the executor name
     */
    private void executeRunnableWithExecutor( Runnable runnable, String executorName ) {
        TrackingThreadPoolExecutor executor = checkAndSetupExecutor( executorName );
        executor.setExecutorRunning( true );
        executor.execute( runnable );
        log.info( EXECUTOR_CALLED + executorName );
    }

    /**
     * Submit runnable with executor.
     *
     * @param runnable
     *         the runnable
     * @param executorName
     *         the executor name
     * @param jobId
     *         the job id
     */
    private void submitRunnableWithExecutor( Runnable runnable, String executorName, UUID jobId ) {
        TrackingThreadPoolExecutor executor = checkAndSetupExecutor( executorName );
        executor.setExecutorRunning( true );
        threadMap.put( jobId, runnable );
        Future< ? > future = executor.submit( runnable );
        futureMap.put( jobId, future );
        log.info( EXECUTOR_CALLED + executorName );
    }

    /**
     * Log debugging information.
     *
     * @param executorToMonitor
     *         the executor to monitor
     * @param name
     *         the name
     */
    private void logThreadPoolInformation( TrackingThreadPoolExecutor executorToMonitor, String name ) {
        Runnable monitorExecutor = () -> {
            while ( true ) {
                String template = """

                        ######################################################################################################
                        Monitoring %s executor
                        ######################################################################################################
                        \tRunning size: %s
                        \tqueue size: %s
                        \tActiveCount: %s
                        \tTask count: %s
                        \tisShutDown: %s
                        \tisTerminated: %s
                        ######################################################################################################
                        END
                        ######################################################################################################
                        """;
                log.trace( String.format( template, name, executorToMonitor.getRunning().size(), executorToMonitor.getQueue().size(),
                        executorToMonitor.getActiveCount(), executorToMonitor.getTaskCount(), executorToMonitor.isShutdown(),
                        executorToMonitor.isTerminated() ) );

                try {
                    // wait before printing info again
                    Thread.sleep( 5000 );
                } catch ( InterruptedException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( "" );
                }
            }
        };

        Thread thread1 = new Thread( monitorExecutor );
        thread1.start();

    }

    /**
     * Sets executor running by name.
     *
     * @param executorName
     *         the executor name
     * @param isExecutorRunning
     *         the is executor running
     */
    public void setExecutorRunningByName( String executorName, boolean isExecutorRunning ) {
        TrackingThreadPoolExecutor executorToUpdate = executorMap.get( executorName );
        if ( executorToUpdate != null ) {
            executorToUpdate.setExecutorRunning( isExecutorRunning );
        }
    }

    /**
     * Is executor running boolean.
     *
     * @param executorName
     *         the executor name
     *
     * @return the boolean
     */
    public boolean isExecutorRunning( String executorName ) {
        TrackingThreadPoolExecutor executor = executorMap.get( executorName );
        return executor != null && executor.isExecutorRunning();
    }

    /**
     * Checks if is scheduled pool executor running.
     *
     * @return true, if is scheduled pool executor running
     */
    public boolean isScheduledPoolExecutorRunning() {
        return isScheduledPoolExecutorRunning;
    }

    /**
     * Sets the scheduled pool executor running.
     *
     * @param isScheduledPoolExecutorRunning
     *         the new scheduled pool executor running
     */
    public void setScheduledPoolExecutorRunning( boolean isScheduledPoolExecutorRunning ) {
        this.isScheduledPoolExecutorRunning = isScheduledPoolExecutorRunning;
    }

    /**
     * Gets the executor pool.
     *
     * @return the executorPool
     */
    @Override
    public ExecutorPool getExecutorPoolFromConf() {
        if ( executorPool == null ) {
            executorPool = ExecutorPropertiesManager.getExecutorPools();
        }
        return executorPool;
    }

    /**
     * Sets the executor pool.
     *
     * @param executorPool
     *         the executorPool to set
     */
    @Override
    public void setExecutorPool( ExecutorPool executorPool ) {
        this.executorPool = executorPool;
    }

    /* *********************************************** Dynamic Host pool execution *************************************************. */

    /**
     * The Dynamic Map pool executor.
     */
    protected Map< String, TrackingThreadPoolExecutor > hostPoolExecutorMap = new HashMap<>();

    /**
     * Host execute.
     *
     * @param dynamicRunnable
     *         the dynamic runnable
     * @param hostConfigration
     *         the host configration
     * @param jobId
     *         the job id
     */
    @Override
    public void hostExecute( Runnable dynamicRunnable, ExecutionHosts hostConfigration, UUID jobId ) {

        TrackingThreadPoolExecutor dynamicHostPoolExecutor = null;
        if ( hostPoolExecutorMap.containsKey( hostConfigration.getId().toString() ) ) {
            dynamicHostPoolExecutor = hostPoolExecutorMap.get( hostConfigration.getId().toString() );
        } else {
            for ( Pools pool : getExecutorPoolFromConf().getPools() ) {
                if ( pool.getName().equals( ThreadPoolEnum.DYNAMIC_HOSTS.getName() ) ) {
                    dynamicHostPoolExecutor = setupExecutor( dynamicHostPoolExecutor, pool );
                    dynamicHostPoolExecutor.setCorePoolSize( Integer.parseInt( hostConfigration.getCorePoolSize() ) );
                    dynamicHostPoolExecutor.setMaximumPoolSize( Integer.parseInt( hostConfigration.getCoreMaxSize() ) );
                }
            }
            hostPoolExecutorMap.put( hostConfigration.getId().toString(), dynamicHostPoolExecutor );
        }

        if ( dynamicHostPoolExecutor != null ) {
            threadMap.put( jobId, dynamicRunnable );
            Future< ? > future = dynamicHostPoolExecutor.submit( dynamicRunnable );
            futureMap.put( jobId, future );
            log.info( EXECUTOR_CALLED + ThreadPoolEnum.DYNAMIC_HOSTS.getName() );
        }

    }

    /**
     * Setup thread pool.
     *
     * @param threadPool
     *         the thread pool
     * @param pool
     *         the pool
     *
     * @return ThreadPoolExecutor with supplied configurations
     */
    private TrackingThreadPoolExecutor setupExecutor( TrackingThreadPoolExecutor threadPool, Pools pool ) {
        for ( ExecutorProperties property : pool.getProperties() ) {
            threadPool = new TrackingThreadPoolExecutor( Integer.parseInt( property.getCorePoolSize() ),
                    Integer.parseInt( property.getCoreMaxSize() ), Long.parseLong( property.getKeepAliveTime() ), TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>( Integer.parseInt( property.getQueueSize() ) ), new RejectedExecutionHandlerImpl(),
                    pool.getName() );
        }
        return threadPool;
    }

    /**
     * Checks if is dynamic host running.
     *
     * @param hostId
     *         the host id
     *
     * @return true, if is dynamic host running
     */
    @Override
    public boolean isDynamicHostRunning( String hostId ) {
        return hostPoolExecutorMap.containsKey( hostId );
    }

    /************************************************* Dynamic Host pool execution **************************************************/

    public TrackingThreadPoolExecutor getWorkflowPoolExecutor() {
        return executorMap.get( ThreadPoolEnum.WORKFLOW.getName() );
    }

    @Override
    public Map< UUID, Runnable > getThreadMap() {
        return threadMap;
    }

    @Override
    public Map< UUID, Future< ? > > getFutureMap() {
        return futureMap;
    }

    @Override
    public Map< String, TrackingThreadPoolExecutor > getHostPoolExecutorMap() {
        return hostPoolExecutorMap;
    }

    /**
     * Check andshut down scheduled pool.
     */
    public void checkAndshutDownScheduledPool() {
        if ( scheduledPoolExecutor != null ) {
            scheduledPoolExecutor.shutdown();
        }
    }

}
