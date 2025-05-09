package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.data.entity.SubmitLoadcaseEntity;
import de.soco.software.simuspace.suscore.object.dao.SubmitLoadcaseDAO;
import de.soco.software.simuspace.suscore.object.model.LoadcaseSubmitDTOTemp;

/**
 * The Class DummyLoadcasePendingJobsUpdateThread.
 *
 * @author noman arshad
 */
@Log4j2
public class DummyLoadcasePendingJobsUpdateThread extends UserThread {

    /**
     * The submit loadcase DAO.
     */
    private SubmitLoadcaseDAO submitLoadcaseDAO;

    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new dummy loadcase pending jobs update thread.
     */
    public DummyLoadcasePendingJobsUpdateThread() {
    }

    /**
     * Instantiates a new dummy loadcase pending jobs update thread.
     *
     * @param submitLoadcaseDAO
     *         the submit loadcase DAO
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public DummyLoadcasePendingJobsUpdateThread( SubmitLoadcaseDAO submitLoadcaseDAO, EntityManagerFactory entityManagerFactory ) {
        super();
        this.submitLoadcaseDAO = submitLoadcaseDAO;
        this.entityManagerFactory = entityManagerFactory;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.common.base.UserThread#run()
     */
    @Override
    public void run() {
        updateAllPendingJobs();
    }

    /**
     * Update all pending jobs.
     */
    public void updateAllPendingJobs() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {

            if ( log.isDebugEnabled() ) {
                log.debug( "Schedular updating all pending jobs" );
            }
            List< SubmitLoadcaseEntity > pendingJobsList = submitLoadcaseDAO.getAllPendingJobs( entityManager );

            if ( pendingJobsList != null && !pendingJobsList.isEmpty() ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "All pending job count : " + pendingJobsList.size() );
                }

                for ( SubmitLoadcaseEntity submitLoadcaseEntity : pendingJobsList ) {

                    if ( BooleanUtils.isFalse( submitLoadcaseEntity.getJobCompleted() ) ) {

                        String loadcaseListjson = submitLoadcaseEntity.getLoadcaseListjson();

                        List< LoadcaseSubmitDTOTemp > preparedLoadcaseList = JsonUtils.jsonToList( loadcaseListjson,
                                LoadcaseSubmitDTOTemp.class );
                        if ( log.isDebugEnabled() ) {
                            log.debug( "inner loop on pending Dummy job" );
                        }
                        boolean dummyLoadcaseComplete = false;
                        for ( LoadcaseSubmitDTOTemp loadcaseSubmitDTOTemp : preparedLoadcaseList ) {

                            if ( loadcaseSubmitDTOTemp.isDummyIterationCompleted() ) {
                                dummyLoadcaseComplete = true;
                            } else {
                                dummyLoadcaseComplete = false;
                                break;
                            }
                        }
                        if ( dummyLoadcaseComplete ) {
                            if ( log.isDebugEnabled() ) {
                                log.debug( "Marked completed submitLoadcaseEntity :" + submitLoadcaseEntity.getId() );
                            }
                            updateSubmitLoadcaseEntityAsCompletedById( entityManager, submitLoadcaseEntity );
                        }
                    }
                }
            }

        } catch ( Exception e ) {
            log.debug( "EXCEPTION" );
            log.debug( "Schedular PendingJobs FAILED : ", e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update submit loadcase entity as completed by id.
     *
     * @param entityManager
     *         the entity manager
     * @param submitLoadcaseEntity
     *         the submit loadcase entity
     */
    private void updateSubmitLoadcaseEntityAsCompletedById( EntityManager entityManager, SubmitLoadcaseEntity submitLoadcaseEntity ) {
        submitLoadcaseEntity.setJobCompleted( true );
        submitLoadcaseDAO.update( entityManager, submitLoadcaseEntity );
    }

    /**
     * Gets the submit loadcase DAO.
     *
     * @return the submit loadcase DAO
     */
    public SubmitLoadcaseDAO getSubmitLoadcaseDAO() {
        return submitLoadcaseDAO;
    }

    /**
     * Sets the submit loadcase DAO.
     *
     * @param submitLoadcaseDAO
     *         the new submit loadcase DAO
     */
    public void setSubmitLoadcaseDAO( SubmitLoadcaseDAO submitLoadcaseDAO ) {
        this.submitLoadcaseDAO = submitLoadcaseDAO;
    }

}
