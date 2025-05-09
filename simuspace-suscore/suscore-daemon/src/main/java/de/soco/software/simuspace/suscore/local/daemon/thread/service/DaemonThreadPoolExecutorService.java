package de.soco.software.simuspace.suscore.local.daemon.thread.service;

import java.util.UUID;

/**
 * The Interface DaemonThreadPoolExecutorService.
 *
 * @author noman arshad
 */
public interface DaemonThreadPoolExecutorService {

    /**
     * Daemon workflow execute.
     *
     * @param workflowRunnable
     *         the workflow runnable
     * @param uuid
     *         the uuid
     */
    void daemonWorkflowExecute( Runnable workflowRunnable, UUID uuid );

    /**
     * Check andshut down thread pool.
     *
     * @param poolCode
     *         the pool code
     */
    void checkAndshutDownThreadPool( int poolCode );

    /**
     * Stop job execution.
     *
     * @param uuid
     *         the job id
     * @param userName
     *         the user name
     *
     * @return true, if successful
     */
    boolean stopJobExecution( UUID uuid, String userName );

    /**
     * Stop Scheme job execution.
     *
     * @param uuid
     *         the job id
     * @param userName
     *         the user name
     *
     * @return true, if successful
     */
    boolean stopSchemeJobExecution( UUID jobId, String userName );

}