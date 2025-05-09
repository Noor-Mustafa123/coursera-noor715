package de.soco.software.simuspace.suscore.user.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface will be responsible for all the database operations necessary for dealing with the User.
 */
public interface UserDAO extends GenericDAO< UserEntity > {

    /**
     * Creates the user.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     *
     * @return the user entity
     */
    UserEntity createUser( EntityManager entityManager, UserEntity user );

    /**
     * A method for getting user.
     *
     * @param entityManager
     *         the entity manager
     * @param uid
     *         the uid
     *
     * @return user by uid
     */
    UserEntity getUserByUid( EntityManager entityManager, String uid );

    /**
     * A method for updating userEntity
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     * @param logs
     *         the logs
     *
     * @return userEntity user entity
     */
    UserEntity updateUserEntity( EntityManager entityManager, UserEntity userEntity, List< AuditLogEntity > logs );

    /**
     * Gets user detail by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the user detail by id
     */
    UserEntity getUserEntityById( EntityManager entityManager, UUID id );

    /**
     * A method that checks if the user exists in database already
     *
     * @param entityManager
     *         the entity manager
     * @param uid
     *         the uid
     *
     * @return userEntity boolean
     */
    boolean isUserExist( EntityManager entityManager, String uid );

    /**
     * This function takes the responsibility to return all users in the system.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return all user entities
     */
    List< UserEntity > getAllUsers( EntityManager entityManager );

    /**
     * Get id of all users.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return all user ids
     */
    List< UUID > getIdOfAllUsers( EntityManager entityManager );

    /**
     * Gets groups by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return groups by user id
     */
    List< GroupEntity > getGroupsByUserId( EntityManager entityManager, String id );

    /**
     * Gets all groups.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return all groups
     */
    List< GroupEntity > getAllGroups( EntityManager entityManager );

    /**
     * Gets group and users by group id.
     *
     * @param entityManager
     *         the entity manager
     * @param groupId
     *         the group id
     *
     * @return group and users by group id
     */
    GroupEntity getGroupAndUsersByGroupId( EntityManager entityManager, UUID groupId );

    /**
     * Gets the users by group entity.
     *
     * @param entityManager
     *         the entity manager
     * @param groupEntity
     *         the group entity
     *
     * @return the users by group entity
     */
    List< UserEntity > getUsersByGroupEntity( EntityManager entityManager, GroupEntity groupEntity );

    /**
     * Gets users by directory id.
     *
     * @param entityManager
     *         the entity manager
     * @param dirId
     *         the dir id
     *
     * @return the users by directory id
     */
    List< UserEntity > getUsersByDirectoryId( EntityManager entityManager, UUID dirId );

    /**
     * Gets the users by group entity.
     *
     * @param entityManager
     *         the entity manager
     * @param groupEntity
     *         the group entity
     * @param filter
     *         the filter
     *
     * @return the users by group entity
     */
    List< UserEntity > getFilteredUsersByGroupEntity( EntityManager entityManager, GroupEntity groupEntity, FiltersDTO filter );

    /**
     * Gets the user by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the user entity
     */
    UserEntity readUser( EntityManager entityManager, UUID id );

    /**
     * Is restricted user boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the boolean
     */
    Boolean isRestrictedUser( EntityManager entityManager, UUID id );

    /**
     * Gets all property values.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     *
     * @return the all property values
     */
    List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName );

}
