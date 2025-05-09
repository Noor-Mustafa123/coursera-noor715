package de.soco.software.simuspace.wizards.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.Date;
import java.util.List;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.wizards.manager.LoadcaseManager;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;

public class DeleteLoadcaseThread extends UserThread {

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

    /**
     * The object id.
     */
    private List< SuSEntity > objects;

    /**
     * The job.
     */
    private Job job;

    /**
     * The loadcase manager.
     */
    private LoadcaseManager loadcaseManager;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * The entity Manager Factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new delete loadcase thread.
     */
    public DeleteLoadcaseThread() {
    }

    /**
     * Instantiates a new delete loadcase thread.
     *
     * @param userId
     *         the user id
     * @param objects
     *         the objects
     * @param job
     *         the job
     * @param loadcaseManager
     *         the loadcase manager
     * @param jobManager
     *         the job manager
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public DeleteLoadcaseThread( String userId, List< SuSEntity > objects, Job job, LoadcaseManager loadcaseManager, JobManager jobManager,
            EntityManagerFactory entityManagerFactory ) {
        super();
        this.userId = userId;
        this.objects = objects;
        this.job = job;
        this.loadcaseManager = loadcaseManager;
        this.jobManager = jobManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            int deletedCount = 0;
            for ( SuSEntity objToDelete : objects ) {
                if ( !Thread.currentThread().isInterrupted() ) {
                    deletedCount++;
                    job.getLog()
                            .add( new LogRecord( INFO,
                                    MessageBundleFactory.getMessage( Messages.DELETE_OBJECT_WITH_NAME.getKey(), objToDelete.getName(),
                                            objToDelete.getComposedId().getId(), objToDelete.getComposedId().getVersionId() ),
                                    new Date() ) );
                    loadcaseManager.deleteLoadcase( entityManager, userId, objToDelete.getComposedId().getId() );
                }
            }

            if ( deletedCount == objects.size() ) {
                job.getLog().add( new LogRecord( INFO,
                        MessageBundleFactory.getMessage( Messages.OBJECT_AND_DEPENDENCIES_DELETED_SUCCESSFULLY.getKey() ), new Date() ) );
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
