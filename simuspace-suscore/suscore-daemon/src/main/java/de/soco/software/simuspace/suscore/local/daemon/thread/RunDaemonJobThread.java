package de.soco.software.simuspace.suscore.local.daemon.thread;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.processing.WFExecutionManager;

/**
 * The Class RunDaemonJobThread.
 *
 * @author Ali Haider
 */
public class RunDaemonJobThread extends UserThread {

    /**
     * The execution manager of workflow engine to start stop jobs.
     */
    private WFExecutionManager executionManager;

    /**
     * The job parameters.
     */
    private final JobParameters jobParameters;

    /**
     * Instantiates a new run workflow thread.
     *
     * @param wfExecutionManager
     * @param uid
     *         the user id
     * @param jobParameters
     *         the jobParameters
     * @param workflowManager
     *         the workflowManager
     */
    public RunDaemonJobThread( WFExecutionManager executionManager, JobParameters jobParameters ) {
        super();
        this.executionManager = executionManager;
        this.jobParameters = jobParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        executionManager.executeWorkflow( jobParameters );
    }

    /**
     * Gets the execution manager.
     *
     * @return the execution manager
     */
    public WFExecutionManager getExecutionManager() {
        return executionManager;
    }

    /**
     * Sets the execution manager.
     *
     * @param executionManager
     *         the new execution manager
     */
    public void setExecutionManager( WFExecutionManager executionManager ) {
        this.executionManager = executionManager;
    }

    /**
     * Gets the job parameters.
     *
     * @return the job parameters
     */
    public JobParameters getJobParameters() {
        return jobParameters;
    }

}
