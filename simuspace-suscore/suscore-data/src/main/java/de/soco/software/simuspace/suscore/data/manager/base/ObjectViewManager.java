package de.soco.software.simuspace.suscore.data.manager.base;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;

/**
 * The Interface ObjectViewManager that holds the CRUD method definitions related to object's views.
 *
 * @author Zeeshan jamal
 */
public interface ObjectViewManager {

    /**
     * Gets the user object views by key.
     *
     * @param key
     *         the key
     * @param userId
     *         the user id
     * @param objectId
     *         the object id (set null if its not for any other object)
     *
     * @return the user object views by key
     *
     * @apiNote To be used in service calls only
     */
    List< ObjectViewDTO > getUserObjectViewsByKey( String key, String userId, String objectId );

    /**
     * Gets the user object views by key.
     *
     * @param entityManager
     *         the entity manager
     * @param key
     *         the key
     * @param userId
     *         the user id
     * @param objectId
     *         the object id (set null if its not for any other object)
     *
     * @return the user object views by key
     */
    List< ObjectViewDTO > getUserObjectViewsByKey( EntityManager entityManager, String key, String userId, String objectId );

    /**
     * Gets the user object views by key and config.
     *
     * @param entityManager
     *         the entity manager
     * @param key
     *         the key
     * @param userId
     *         the user id
     * @param config
     *         the config
     *
     * @return the user object views by key and config
     */
    List< ObjectViewDTO > getUserObjectViewsByKeyAndConfig( EntityManager entityManager, String key, String userId, String config );

    /**
     * Save or update object view.
     *
     * @param viewDTO
     *         the view DTO
     * @param userId
     *         the user id
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveOrUpdateObjectView( ObjectViewDTO viewDTO, String userId );

    /**
     * Save or update object view.
     *
     * @param entityManager
     *         the entity manager
     * @param viewDTO
     *         the view DTO
     * @param userId
     *         the user id
     *
     * @return the object view DTO
     */
    ObjectViewDTO saveOrUpdateObjectView( EntityManager entityManager, ObjectViewDTO viewDTO, String userId );

    /**
     * Save default object view.
     *
     * @param viewId
     *         the view id
     * @param userId
     *         the user id
     * @param objectViewKey
     *         the object view key
     * @param objectId
     *         the object id (set null if its not for any other object)
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveDefaultObjectView( UUID viewId, String userId, String objectViewKey, String objectId );

    /**
     * Save default object view.
     *
     * @param entityManager
     *         the entity manager
     * @param viewId
     *         the view id
     * @param userId
     *         the user id
     * @param objectViewKey
     *         the object view key
     * @param objectId
     *         the object id (set null if its not for any other object)
     *
     * @return the object view DTO
     */
    ObjectViewDTO saveDefaultObjectView( EntityManager entityManager, UUID viewId, String userId, String objectViewKey, String objectId );

    /**
     * Save default object view by config.
     *
     * @param entityManager
     *         the entity manager
     * @param viewId
     *         the view id
     * @param userId
     *         the user id
     * @param objectViewKey
     *         the object view key
     * @param objectId
     *         the object id
     * @param config
     *         the config
     *
     * @return the object view DTO
     */
    ObjectViewDTO saveDefaultObjectViewByConfig( EntityManager entityManager, UUID viewId, String userId, String objectViewKey,
            String objectId, String config );

    /**
     * Gets the object views by key.
     *
     * @param entityManager
     *         the entity manager
     * @param key
     *         the key
     *
     * @return the user object views by key
     */
    List< ObjectViewDTO > getAllObjectViewsByKey( EntityManager entityManager, String key );

    /**
     * Delete object view.
     *
     * @param viewId
     *         the view id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteObjectView( UUID viewId );

    /**
     * Delete object view.
     *
     * @param entityManager
     *         the entity manager
     * @param viewId
     *         the view id
     *
     * @return true, if successful
     */
    boolean deleteObjectView( EntityManager entityManager, UUID viewId );

    /**
     * Gets the object view by id.
     *
     * @param viewId
     *         the view id
     *
     * @return the object view by id
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO getObjectViewById( UUID viewId );

    /**
     * Gets the object view by id.
     *
     * @param entityManager
     *         the entity manager
     * @param viewId
     *         the view id
     *
     * @return the object view by id
     */
    ObjectViewDTO getObjectViewById( EntityManager entityManager, UUID viewId );

    /**
     * Create custom views for user.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     */
    void createCustomViewsForUser( UUID userId );

    /**
     * Checks if is system genrated view exists.
     *
     * @param entityManager
     *         the entity manager
     * @param userid
     *         the userid
     * @param systemGenratedViewEnumId
     *         the system genrated view enum id
     *
     * @return true, if is system genrated view exists
     */
    boolean updateSystemGeneratedViewIfExists( EntityManager entityManager, String userid, int systemGenratedViewEnumId );

    /**
     * Prepare and save object view DTO for system genrated views.
     *
     * @param entityManager
     *         the entity manager
     * @param userDTO
     *         the user DTO
     * @param systemGenratedViewEnumId
     *         the system genrated view enum id
     */
    void prepareAndSaveObjectViewDTOForSystemGenratedViews( EntityManager entityManager, UserDTO userDTO, int systemGenratedViewEnumId );

}
