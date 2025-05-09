package de.soco.software.simuspace.server.model;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.workflow.model.JobParameters;

/**
 * The class is responsible to provide runnable for run workflow and will do all executions on the behalf of the user logged in;
 *
 * @author M.Nasir.Farooq
 */
public class RunWorkflowThread implements Runnable {

    /**
     * The user id.
     */
    private final String userId;

    /**
     * The workflowManager.
     */
    private final WorkflowManager workflowManager;

    /**
     * The jobParameters.
     */
    private final JobParameters jobParameters;

    /**
     * Instantiates a new run workflow thread.
     *
     * @param uid
     *         the user id
     * @param jobParameters
     *         the jobParameters
     * @param workflowManager
     *         the workflowManager
     */
    public RunWorkflowThread( String userId, JobParameters jobParameters, WorkflowManager workflowManager ) {
        super();
        this.userId = userId;
        this.jobParameters = jobParameters;
        this.workflowManager = workflowManager;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        EntityManager entityManager = workflowManager.getEntityManagerFactory().createEntityManager();
        try {
            workflowManager.executeWFProcessOnServer( entityManager, userId, jobParameters );
        } finally {
            entityManager.close();
        }
    }

}
