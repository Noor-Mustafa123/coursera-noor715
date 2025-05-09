package de.soco.software.simuspace.server.threads;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobRelationTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.executor.util.SusExecutorUtil;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;

@Log4j2
public class RunSchemeDoeThread implements Runnable {

    /**
     * The exit variable used to stop the thread.
     */
    private volatile boolean stop = false;

    /**
     * The user id.
     */
    private final String userId;

    /**
     * The job Name.
     */
    private final String jobName;

    /**
     * The jobParameters.
     */
    private final JobParameters jobParameters;

    /**
     * The savedMasterJob.
     */
    private final Job masterJob;

    /**
     * The designSummary.
     */
    private final List< Map< String, Object > > designSummary;

    /**
     * The childJobs.
     */
    private List< UUID > childJobs = new ArrayList<>();

    /**
     * The workflowManager.
     */
    private final WorkflowManager workflowManager;

    /**
     * Instantiates a new run workflow thread.
     *
     * @param userId
     *         the user id
     * @param jobName
     *         the job name
     * @param jobParameters
     *         the jobParameters
     * @param savedMasterJob
     *         the saved master job
     * @param designSummary
     *         the design summary
     * @param workflowManager
     *         the workflowManager
     */
    public RunSchemeDoeThread( String userId, String jobName, JobParameters jobParameters, Job savedMasterJob,
            List< Map< String, Object > > designSummary, WorkflowManager workflowManager ) {
        super();
        this.userId = userId;
        this.jobName = jobName;
        this.jobParameters = jobParameters;
        this.masterJob = savedMasterJob;
        this.designSummary = designSummary;
        this.workflowManager = workflowManager;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        EntityManager entityManager = workflowManager.getEntityManagerFactory().createEntityManager();
        try {
            for ( int jobCount = 1; jobCount <= designSummary.size(); jobCount++ ) {
                if ( stop ) {
                    return;
                }
                try {
                    jobParameters.setId( UUID.randomUUID().toString() );
                    jobParameters.setName( jobName + "_" + jobCount );
                    jobParameters.setJobRelationType( JobRelationTypeEnums.CHILD.getKey() );
                    jobParameters.getWorkflow().setWorkflowType( JobTypeEnums.SCHEME.getKey() );
                    jobParameters.setJobType( JobTypeEnums.SCHEME.getKey() );
                    jobParameters.setDesignSummary( designSummary.get( jobCount - 1 ) );
                    jobParameters.getWorkflow().setMasterJobId( String.valueOf( masterJob.getId() ) );
                    jobParameters.getRequestHeaders().setJobAuthToken( masterJob.getRequestHeaders().getJobAuthToken() );

                    Integer id = workflowManager.getJobManager().saveJobIds( entityManager, UUID.fromString( jobParameters.getId() ) );
                    if ( id != null ) {
                        jobParameters.setJobInteger( id );
                    }

                    workflowManager.runServerJobForDOE( entityManager, UUID.fromString( userId ), jobParameters,
                            designSummary.get( jobCount - 1 ), masterJob.getId(), masterJob.getJobInteger(), masterJob.getName() );

                    childJobs.add( UUID.fromString( jobParameters.getId() ) );
                } catch ( Exception e ) {
                    stop();
                    log.error( e.getMessage(), e );
                    String errorMsg = "Scheme job iteration Falid: " + e.getMessage();
                    workflowManager.updateMasterJobAndStatus( entityManager, masterJob.getId(), errorMsg );
                    throw new SusException( "Scheme job iteration Falid: " + e.getMessage() );
                }
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Sets the exit boolean to true so the thread can be stopped.
     */
    public void stop() {
        stop = true;
        SusExecutorUtil.threadPoolExecutorService.stopJobs( childJobs );
    }

}
