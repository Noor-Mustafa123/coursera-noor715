package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.object.manager.DataManager;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;

/**
 * The class is responsible to provide runnable for delete object and its dependencies;
 *
 * @author Nasir.Farooq
 */
@Log4j2
public class DeleteObjectThread extends UserThread {

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

    private SelectionManager selectionManager;

    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new delete object thread.
     */
    public DeleteObjectThread() {
    }

    /**
     * Instantiates a new delete object thread.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     * @param mode
     *         the mode
     * @param job
     *         the job
     * @param jobManager
     *         the job manager
     * @param dataManager
     *         the data manager
     * @param selectionManager
     *         the selection manager
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public DeleteObjectThread( String userId, String selectionId, String mode, Job job, JobManager jobManager, DataManager dataManager,
            SelectionManager selectionManager, EntityManagerFactory entityManagerFactory ) {
        super();
        this.userId = userId;
        this.selectionId = selectionId;
        this.mode = mode;
        this.job = job;
        this.jobManager = jobManager;
        this.dataManager = dataManager;
        this.selectionManager = selectionManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SuSEntity > objects;
            if ( mode.equals( ConstantsMode.SINGLE ) ) {
                objects = dataManager.verifyPermissionsAndGetDeleteObjectList( entityManager, userId, UUID.fromString( selectionId ) );
            } else {
                objects = dataManager.verifyPermissionsAndGetDeleteObjectListBulk( entityManager, userId, selectionId );
            }
            int deletedCount = 0;
            for ( SuSEntity suSEntity : objects ) {
                dataManager.deleteIndex( suSEntity );
            }
            for ( SuSEntity objToDelete : objects ) {
                if ( !Thread.currentThread().isInterrupted() ) {
                    deletedCount++;
                    job.getLog()
                            .add( new LogRecord( INFO,
                                    MessageBundleFactory.getMessage( Messages.DELETE_OBJECT_WITH_NAME.getKey(), objToDelete.getName(),
                                            objToDelete.getComposedId().getId(), objToDelete.getComposedId().getVersionId() ),
                                    new Date() ) );
                    dataManager.deleteObject( entityManager, userId, objToDelete );
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

            try {
                List< SuSEntity > parent = dataManager.getSusDAO().getParents( entityManager, objects.get( 0 ) );
                selectionManager.sendCustomerEvent( entityManager, userId, parent.get( 0 ), "update" );
            } catch ( Exception e ) {
                log.error( "auto refresh table ERROR after deletion of objects :", e );
            }
        } catch ( Exception e ) {
            job.getLog().add( new LogRecord( ERROR, e.getMessage(), new Date() ) );
            job.setStatus( new Status( WorkflowStatus.FAILED ) );
            jobManager.updateJob( entityManager, job );
            log.error( e.getMessage(), e );
        } finally {
            entityManager.close();
        }

    }

}
