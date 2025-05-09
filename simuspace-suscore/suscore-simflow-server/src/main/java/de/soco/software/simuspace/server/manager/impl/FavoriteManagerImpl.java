package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.server.dao.FavoriteDAO;
import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.FavoriteManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.FavoriteEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;

/**
 * To manage the Favorite CRUD Functionality no longer in use
 *
 * @author Zeeshan jamal
 */
@Deprecated( forRemoval = true )
public class FavoriteManagerImpl extends BaseManager implements FavoriteManager {

    /**
     * The favoriteDAO DAO reference for Database calls.
     */
    private FavoriteDAO favoriteDAO;

    /**
     * The workflow manager reference.
     */
    private WorkflowManager workflowManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * For FavoriteManagerImpl instantiation
     */
    public FavoriteManagerImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkflowDTO addWorkflowToFavorite( UUID userId, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isWorkflowUser( entityManager, userId );
            if ( !StringUtils.validateUUIDString( workflowId ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.INVALID_UUID, workflowId ) ), getClass() );
            }

            final WorkflowDTO workflowDto = workflowManager.getWorkflowById( entityManager, userId, workflowId );

            if ( workflowDto != null ) {

                final FavoriteEntity favoriteEntity = prepareFavoriteEntity();
                favoriteEntity.setWorkflow( workflowManager.prepareWorkflowEntity( workflowDto ) );
                favoriteEntity.setFavoriteBy( workflowManager.getUserManager().getSimUser( entityManager, userId ) );

                final UserEntity userEntity = workflowManager.getUserManager().getSimUser( entityManager, userId );

                if ( !favoriteDAO.isWorkFlowAlreadyFavorite( entityManager, userEntity.getId(), favoriteEntity.getWorkflow() ) ) {
                    favoriteDAO.addWorkFlowToFavorites( entityManager, favoriteEntity );
                    workflowDto.setFavorite( true );
                } else {
                    favoriteDAO.removeWorkFlowFromFavorites( entityManager, userEntity.getId(), favoriteEntity );
                    workflowDto.setFavorite( false );
                }

            }

            return workflowDto;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< WorkflowDTO > getFavoriteWorkflowListByUserId( UUID userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        isWorkflowUser( entityManager, userId );
        final List< WorkflowDTO > workFlowDTOList = new ArrayList<>();

        try {

            final UserEntity userEntity = workflowManager.getUserManager().getSimUser( entityManager, userId );

            if ( null != userEntity ) {
                final List< WorkflowEntity > workflowEntities = favoriteDAO.getFavouriteWorkFlowListByUserId( entityManager,
                        userEntity.getId() );
                for ( final WorkflowEntity workFlowEntity : workflowEntities ) {
                    addWorkflowDTO( entityManager, userId, workFlowDTOList, workFlowEntity );

                }

            } else {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.UNABLE_TO_GET_USER, userId ) ),
                        this.getClass() );
            }

        } catch ( final SusException e ) {
            ExceptionLogger.logException( e, getClass() );
        } finally {
            entityManager.close();
        }

        // to sort the workflows list
        workFlowDTOList.sort( Comparator.comparing( WorkflowDTO::getCreatedOn, Comparator.nullsLast( Comparator.reverseOrder() ) ) );

        return workFlowDTOList;
    }

    /**
     * Adds the workflow DTO.
     *
     * @param entityManager
     * @param userId
     *         the user id
     * @param workFlowDTOList
     *         the work flow DTO list
     * @param workFlowEntity
     *         the work flow entity
     */
    private void addWorkflowDTO( EntityManager entityManager, UUID userId, final List< WorkflowDTO > workFlowDTOList,
            final WorkflowEntity workFlowEntity ) {
        try {
            final WorkflowDTO dto = workflowManager.getWorkflowById( entityManager, userId,
                    workFlowEntity.getComposedId().getId().toString() );
            if ( dto != null ) {
                workFlowDTOList.add( dto );
            }
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }
    }

    /**
     * Prepare favorite entity.
     *
     * @return the favorite entity
     */
    private FavoriteEntity prepareFavoriteEntity() {

        final FavoriteEntity favoriteEntity = new FavoriteEntity();
        favoriteEntity.setId( UUID.randomUUID() );

        return favoriteEntity;
    }

    /**
     * Gets the favoriteDAO
     *
     * @return favoriteDAO
     */
    public FavoriteDAO getFavoriteDAO() {
        return favoriteDAO;
    }

    /**
     * Sets the favoriteDAO
     *
     * @param favoriteDAO
     *         the favorite dao
     */
    public void setFavoriteDAO( FavoriteDAO favoriteDAO ) {
        this.favoriteDAO = favoriteDAO;
    }

    /**
     * Sets the workflowManager
     *
     * @return workflowManager
     */
    public WorkflowManager getWorkflowManager() {
        return workflowManager;
    }

    /**
     * Sets the workflowManager
     *
     * @param workflowManager
     *         the workflow manager
     */
    public void setWorkflowManager( WorkflowManager workflowManager ) {
        this.workflowManager = workflowManager;
    }

    /**
     * Gets the license manager.
     *
     * @return the license manager
     */
    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}