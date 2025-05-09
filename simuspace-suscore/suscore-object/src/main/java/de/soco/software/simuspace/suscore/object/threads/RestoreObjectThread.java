package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.object.manager.DataManager;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;

/**
 * The class is responsible to provide runnable to restore object and its dependencies;
 *
 * @author Nasir.Farooq
 */
public class RestoreObjectThread extends UserThread {

    /**
     * The Constant INFO.
     */
    private static final String INFO = "Info";

    /**
     * The Constant ERROR.
     */
    private static final String ERROR = "Error";

    /**
     * The user id.
     */
    private String userId;

    private String selectionId;

    private String mode;

    /**
     * The job.
     */
    private Job job;

    private DataManager dataManager;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new run import thread.
     */
    public RestoreObjectThread() {
    }

    /**
     * Instantiates a new run import thread.
     *
     * @param userId
     *         the user id
     * @param job
     *         the job
     * @param selectionId
     *         the selection id
     * @param mode
     *         the mode
     * @param jobManager
     *         the job manager
     * @param dataManager
     *         the data manager
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public RestoreObjectThread( String userId, Job job, String selectionId, String mode, JobManager jobManager, DataManager dataManager,
            EntityManagerFactory entityManagerFactory ) {
        super();
        this.userId = userId;
        this.selectionId = selectionId;
        this.mode = mode;
        this.job = job;
        this.jobManager = jobManager;
        this.dataManager = dataManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {

            List< UUID > objectIds = dataManager.verifyPermissionsAndGetObjectIdsForRestore( entityManager, userId, selectionId, mode );
            job.setJobType( JobTypeEnums.SYSTEM.getKey() );
            job.getLog()
                    .add( new LogRecord( INFO,
                            MessageBundleFactory.getMessage( Messages.GOING_TO_RESTORE_TOTAL_DELETED_OBJECTS.getKey() ) + objectIds.size(),
                            new Date() ) );

            int abc = 0;

            for ( UUID id : objectIds ) {
                if ( !Thread.currentThread().isInterrupted() ) {
                    abc++;
                    dataManager.restoreDeletedObjects( entityManager, userId, id, job );
                }
            }
            if ( abc == objectIds.size() ) {
                job.getLog().add( new LogRecord( INFO,
                        MessageBundleFactory.getMessage( Messages.OBJECT_AND_DEPENDENCIES_RESTORE_SUCCESSFULLY.getKey(), new Date() ) ) );
                job.setStatus( new Status( WorkflowStatus.COMPLETED ) );
                jobManager.updateJob( entityManager, job );
            }

            if ( Thread.currentThread().isInterrupted() ) {
                job.getLog().add( new LogRecord( INFO, MessageBundleFactory.getMessage( Messages.JOB_STOPPED.getKey() ), new Date() ) );
                job.getLog().add( new LogRecord( INFO, MessageBundleFactory.getMessage( Messages.JOB_STOPPED_BY.getKey(), getStoppedBy() ),
                        new Date() ) );
                job.setStatus( new Status( WorkflowStatus.ABORTED ) );
                jobManager.updateJob( entityManager, job );
            }

        } catch ( Exception e ) {

            job.getLog().add( new LogRecord( ERROR, e.getMessage(), new Date() ) );
            job.setStatus( new Status( WorkflowStatus.FAILED ) );
            jobManager.updateJob( entityManager, job );
            ExceptionLogger.logException( e, getClass() );
        } finally {
            entityManager.close();
        }
    }

}
