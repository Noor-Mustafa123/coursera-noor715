package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.ObjectViewEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface will be responsible for all the database operations necessary for dealing with the object's views.
 *
 * @author Zeeshan jamal
 */
public interface ObjectViewDAO extends GenericDAO< ObjectViewEntity > {

    /**
     * Gets the user object views by key.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectViewkey
     *         the object viewkey
     * @param objectId
     *         the object id
     *
     * @return the user object views by key
     */
    List< ObjectViewEntity > getUserObjectViewsByKey( EntityManager entityManager, UUID userId, String objectViewkey, String objectId );

    /**
     * Gets the user object views by key and config.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectViewkey
     *         the object viewkey
     * @param config
     *         the config
     *
     * @return the user object views by key and config
     */
    List< ObjectViewEntity > getUserObjectViewsByKeyAndConfig( EntityManager entityManager, UUID userId, String objectViewkey,
            String config );

    /**
     * Gets the user default object view by key.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectViewKey
     *         the object view key
     * @param objectId
     *         the object id
     *
     * @return the user default object view by key
     */
    ObjectViewEntity getUserDefaultObjectViewByKey( EntityManager entityManager, UUID userId, String objectViewKey, String objectId );

    /**
     * Gets the user default object view by key and config.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectViewKey
     *         the object view key
     * @param objectId
     *         the object id
     * @param config
     *         the config
     *
     * @return the user default object view by key and config
     */
    ObjectViewEntity getUserDefaultObjectViewByKeyAndConfig( EntityManager entityManager, UUID userId, String objectViewKey,
            String objectId, String config );

    /**
     * Checks if is object view already exists.
     *
     * @param entityManager
     *         the entity manager
     * @param objectViewName
     *         the object view name
     * @param objectViewKey
     *         the object view key
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return true, if is object view already exists
     */
    boolean isObjectViewAlreadyExists( EntityManager entityManager, String objectViewName, String objectViewKey, UUID userId,
            String objectId );

    /**
     * Gets the user object views by key and name.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectViewkey
     *         the object viewkey
     * @param name
     *         the name
     *
     * @return the user object views by key and name
     */
    List< ObjectViewEntity > getUserObjectViewsByKeyAndName( EntityManager entityManager, UUID userId, String objectViewkey, String name );

    /**
     * Gets all object views by key.
     *
     * @param entityManager
     *         the entity manager
     * @param objectViewkey
     *         the object viewkey
     *
     * @return the user object views by key
     */
    List< ObjectViewEntity > getAllObjectViewsByKey( EntityManager entityManager, String objectViewkey );

}
