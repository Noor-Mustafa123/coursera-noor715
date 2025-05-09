package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.FavoriteEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * This interface perform all CRUD and other operations of favorites
 *
 * @author Zeeshan jamal
 */
public interface FavoriteDAO {

    /**
     * It saves a favorite into the database
     *
     * @param entityManager
     *         the entity manager
     * @param favoriteEntity
     *         the favorite entity
     */
    void addWorkFlowToFavorites( EntityManager entityManager, FavoriteEntity favoriteEntity );

    /**
     * It removes a favorite from database
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param favoriteEntity
     *         the favorite entity
     */
    void removeWorkFlowFromFavorites( EntityManager entityManager, UUID userId, FavoriteEntity favoriteEntity );

    /**
     * Get the favorite workflow list by user id
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return WorkflowEntity favourite work flow list by user id
     */
    List< WorkflowEntity > getFavouriteWorkFlowListByUserId( EntityManager entityManager, UUID userId );

    /**
     * Check if workFlow is already favorite or not.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workFlowEntity
     *         the work flow entity
     *
     * @return boolean boolean
     */
    boolean isWorkFlowAlreadyFavorite( EntityManager entityManager, UUID userId, WorkflowEntity workFlowEntity );

}
