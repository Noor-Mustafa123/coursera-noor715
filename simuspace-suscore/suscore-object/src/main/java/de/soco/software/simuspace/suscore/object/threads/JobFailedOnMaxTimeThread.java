package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.authentication.manager.AuthManager;
import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.object.manager.DataManager;
import de.soco.software.simuspace.workflow.model.Job;

/**
 * The Class JobFailedOnMaxTimeThread.
 *
 * @author Ali Haider
 */
@Log4j2
public class JobFailedOnMaxTimeThread extends UserThread {

    /**
     * The data manager.
     */
    private DataManager dataManager;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * The auth manager.
     */
    private AuthManager authManager;

    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new job failed on max time thread.
     */
    public JobFailedOnMaxTimeThread() {
    }

    /**
     * Instantiates a new job failed on max time thread.
     *
     * @param dataManager
     *         the data manager
     * @param jobManager
     *         the job manager
     * @param authManager
     *         the auth manager
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public JobFailedOnMaxTimeThread( DataManager dataManager, JobManager jobManager, AuthManager authManager,
            EntityManagerFactory entityManagerFactory ) {
        super();
        this.dataManager = dataManager;
        this.jobManager = jobManager;
        this.authManager = authManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Run.
     */
    @Override
    public void run() {
        log.info( "Check if job max execution time occur then abort" );
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Job > jobsList = jobManager.getRunningChildsAndSingleJobsList( entityManager );
            for ( Job job : jobsList ) {
                try {
                    if ( null != job.getJobMaxExecutionTime()
                            && !DateFormatStandard.checkIsPriorToNow( DateFormatStandard.format( job.getJobMaxExecutionTime() ) ) ) {
                        dataManager.stopJobExecution( entityManager, job.getCreatedBy().getId(), job.getCreatedBy().getName(),
                                job.getId().toString(), null );
                    }
                } catch ( Exception e ) {
                    authManager.expireJobToken( entityManager, job.getRequestHeaders().getJobAuthToken() );
                    log.error( e.getMessage(), e );
                }
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the job manager.
     *
     * @return the job manager
     */
    public JobManager getJobManager() {
        return jobManager;
    }

    /**
     * Sets the job manager.
     *
     * @param jobManager
     *         the new job manager
     */
    public void setJobManager( JobManager jobManager ) {
        this.jobManager = jobManager;
    }

    /**
     * Gets the data manager.
     *
     * @return the data manager
     */
    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Sets the data manager.
     *
     * @param dataManager
     *         the new data manager
     */
    public void setDataManager( DataManager dataManager ) {
        this.dataManager = dataManager;
    }

}
