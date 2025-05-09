package de.soco.software.simuspace.suscore.executor.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import de.soco.software.simuspace.suscore.common.util.ExecutionHosts;
import de.soco.software.simuspace.suscore.executor.enums.SchedulerType;
import de.soco.software.simuspace.suscore.executor.model.ExecutorPool;
import de.soco.software.simuspace.suscore.executor.service.impl.TrackingThreadPoolExecutor;

/**
 * The Interface ThreadPoolExecutorService is allow access to relative methods that needs to be started by other class or bundle.
 *
 * @author Noman Arshad
 */

public interface ThreadPoolExecutorService {

    /**
     * Workflow execute.
     *
     * @param workflowRunnable
     *         the workflow runnable
     * @param jobId
     *         the job id
     */
    void workflowExecute( Runnable workflowRunnable, UUID jobId );

    /**
     * Workflow execute.
     *
     * @param workflowRunnable
     *         the workflow runnable
     * @param jobId
     *         the job id
     */
    void schemeExecute( Runnable workflowRunnable, UUID jobId );

    /**
     * Register and execute scheduled task.
     *
     * @param task
     *         the task
     * @param timeUnit
     *         the time unit
     * @param schedulerType
     *         the scheduler type
     * @param initialDelay
     *         the initial delay
     * @param interval
     *         the interval
     *
     * @return the scheduled future
     */
    ScheduledFuture< ? > registerAndExecuteScheduledTask( Runnable task, TimeUnit timeUnit, SchedulerType schedulerType, int initialDelay,
            long interval );

    /**
     * Indexing execute.
     *
     * @param indexingRunnable
     *         the indexing runnable
     */
    void indexingExecute( Runnable indexingRunnable );

    /**
     * Is executor shutdown or terminated boolean.
     *
     * @param pool
     *         the pool
     *
     * @return the boolean
     */
    boolean isExecutorShutdownOrTerminated( TrackingThreadPoolExecutor pool );

    /**
     * Is executor shutdown or terminated boolean.
     *
     * @param name
     *         the name
     *
     * @return the boolean
     */
    boolean isExecutorShutdownOrTerminated( String name );

    /**
     * Export executor.
     *
     * @param exportRunnable
     *         the export runnable
     */
    void exportExecute( Runnable exportRunnable );

    /**
     * Archive execute.
     *
     * @param archiveRunnable
     *         the archive runnable
     */
    void archiveExecute( Runnable archiveRunnable );

    /**
     * Import execute.
     *
     * @param importRunnable
     *         the import runnable
     */
    void importExecute( Runnable importRunnable );

    /**
     * Life cycle execute.
     *
     * @param lifeCycleRunnable
     *         the life cycle runnable
     */
    void lifeCycleExecute( Runnable lifeCycleRunnable );

    /**
     * Thumbnail execute.
     *
     * @param thumbnailRunnable
     *         the thumbnail runnable
     */
    void thumbnailExecute( Runnable thumbnailRunnable );

    /**
     * Delete execute.
     *
     * @param jobid
     *         the jobid
     * @param deleteRunnable
     *         the delete runnable
     */
    void deleteExecute( UUID jobid, Runnable deleteRunnable );

    /**
     * Stop job execution.
     *
     * @param userName
     *         the user name
     * @param jobId
     *         the all childrens of master job
     *
     * @return true, if successful
     */
    boolean stopJobExecution( String userName, UUID jobId );

    /**
     * Stop Jobs.
     *
     * @param cancelledJobList
     *         the cancelled Job List
     */
    void stopJobs( List< UUID > cancelledJobList );

    /**
     * Ffmpeg execute.
     *
     * @param thumbnailRunnable
     *         the thumbnail runnable
     */
    void executeFFmpeg( Runnable thumbnailRunnable );

    /**
     * Upload execute.
     *
     * @param uploadRunnable
     *         the upload runnable
     */
    void uploadExecute( Runnable uploadRunnable );

    /**
     * Export dataobject execute.
     *
     * @param exportdataobject
     *         the exportdataobject
     */
    void exportDataobjectExecute( Runnable exportdataobject );

    /**
     * System workflow execute.
     *
     * @param workflowRunnable
     *         the workflow runnable
     * @param jobId
     *         the job id
     */
    void systemWorkflowExecute( Runnable workflowRunnable, UUID jobId );

    /**
     * Check and shut down executor pool.
     *
     * @param name
     *         the name
     */
    void checkAndShutDownExecutorPool( String name );

    /**
     * Gets the executor pool from conf.
     *
     * @return the executor pool from conf
     */
    ExecutorPool getExecutorPoolFromConf();

    /**
     * Sets the executor pool.
     *
     * @param executorPool
     *         the new executor pool
     */
    void setExecutorPool( ExecutorPool executorPool );

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
    void hostExecute( Runnable dynamicRunnable, ExecutionHosts hostConfigration, UUID jobId );

    /**
     * Checks if is dynamic host running.
     *
     * @param hostId
     *         the host id
     *
     * @return true, if is dynamic host running
     */
    boolean isDynamicHostRunning( String hostId );

    /**
     * @return the workflowPoolExecutor
     */
    TrackingThreadPoolExecutor getWorkflowPoolExecutor();

    /**
     * @return the threadMap
     */
    Map< UUID, Runnable > getThreadMap();

    /**
     * @return the Future Map
     */
    Map< UUID, Future< ? > > getFutureMap();

    /**
     * @return the hostPoolExecutorMap
     */
    Map< String, TrackingThreadPoolExecutor > getHostPoolExecutorMap();

}