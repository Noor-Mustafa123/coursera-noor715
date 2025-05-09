package de.soco.software.simuspace.suscore.data.manager.base;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.data.common.dao.AclCommonSecurityIdentityDAO;
import de.soco.software.simuspace.suscore.data.common.dao.GroupCommonDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LoginAttemptCommonDAO;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;

/**
 * The interface is used to expose the common function for user as blueprint service.
 *
 * @author M.Nasir.Farooq
 */
public interface UserCommonManager {

    /**
     * Gets filtered users.
     *
     * @param entityManager
     *         the entity manager
     * @param filter
     *         the filter
     *
     * @return the filtered users
     */
    FilteredResponse< UserDTO > getFilteredUsers( EntityManager entityManager, FiltersDTO filter );

    /**
     * Update user type user dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param type
     *         the type
     *
     * @return the user dto
     */
    UserDTO updateUserType( EntityManager entityManager, UUID userId, boolean type );

    /**
     * Gets user by id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the user by id
     */
    UserDTO getUserById( EntityManager entityManager, UUID userId );

    /**
     * Gets user by sid.
     *
     * @param entityManager
     *         the entity manager
     * @param sidId
     *         the sid id
     *
     * @return the user by sid
     */
    UserDTO getUserBySID( EntityManager entityManager, UUID sidId );

    /**
     * Gets user by user uid.
     *
     * @param entityManager
     *         the entity manager
     * @param userUid
     *         the user uid
     *
     * @return the user by user uid
     */
    UserDTO getUserByUserUid( EntityManager entityManager, String userUid );

    /**
     * Gets user group by id.
     *
     * @param entityManager
     *         the entity manager
     * @param groupId
     *         the group id
     *
     * @return the user group by id
     */
    SuSUserGroupDTO getUserGroupById( EntityManager entityManager, UUID groupId );

    /**
     * Gets user group by id for breadcrumb.
     *
     * @param entityManager
     *         the entity manager
     * @param groupId
     *         the group id
     *
     * @return the user group by id for breadcrumb
     */
    SuSUserGroupDTO getUserGroupByIdForBreadcrumb( EntityManager entityManager, UUID groupId );

    /**
     * Gets user group by sid.
     *
     * @param entityManager
     *         the entity manager
     * @param sidId
     *         the sid id
     *
     * @return the user group by sid
     */
    SuSUserGroupDTO getUserGroupBySID( EntityManager entityManager, UUID sidId );

    /**
     * Is user restricted boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the boolean
     */
    boolean isUserRestricted( EntityManager entityManager, UUID userId );

    /**
     * Gets security identity by sid id.
     *
     * @param entityManager
     *         the entity manager
     * @param sidId
     *         the sid id
     *
     * @return the security identity by sid id
     */
    AclSecurityIdentityEntity getSecurityIdentityBySidId( EntityManager entityManager, UUID sidId );

    /**
     * Create user group su s user group dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param userGroupDto
     *         the user group dto
     *
     * @return the su s user group dto
     */
    SuSUserGroupDTO createUserGroup( EntityManager entityManager, String userId, SuSUserGroupDTO userGroupDto );

    /**
     * Gets acl common security identity dao.
     *
     * @return the acl common security identity dao
     */
    AclCommonSecurityIdentityDAO getAclCommonSecurityIdentityDAO();

    /**
     * Gets group common dao.
     *
     * @return the group common dao
     */
    GroupCommonDAO getGroupCommonDAO();

    /**
     * Gets user common dao.
     *
     * @return the user common dao
     */
    UserCommonDAO getUserCommonDAO();

    /**
     * Prepare user model from user entity with directory user dto.
     *
     * @param userEntity
     *         the user entity
     *
     * @return the user dto
     */
    UserDTO prepareUserModelFromUserEntityWithDirectory( UserEntity userEntity );

    /**
     * Gets user entity by id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the user entity by id
     */
    UserEntity getUserEntityById( EntityManager entityManager, UUID userId );

    /**
     * Prepare user model from user entity without directory.
     *
     * @param userEntity
     *         the user entity
     *
     * @return the user DTO
     */
    UserDTO prepareUserModelFromUserEntity( UserEntity userEntity );

    /**
     * Gets user failed ldap attempts.
     *
     * @param entityManager
     *         the entity manager
     * @param userUid
     *         the user uid
     *
     * @return the user failed ldap attempts
     */
    Integer getUserFailedLdapAttempts( EntityManager entityManager, String userUid );

    /**
     * Update failed ldap attempt by one.
     *
     * @param entityManager
     *         the entity manager
     * @param userUid
     *         the user uid
     */
    void updateFailedLdapAttemptByOne( EntityManager entityManager, String userUid );

    /**
     * Reset failed ldap attempts.
     *
     * @param entityManager
     *         the entity manager
     * @param userUid
     *         the user uid
     */
    void resetFailedLdapAttempts( EntityManager entityManager, String userUid );

    /**
     * Gets role by id.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the roleId
     *
     * @return the role by id
     */
    String getRoleNameById( EntityManager entityManager, UUID roleId );

    /**
     * Gets login attempt common dao.
     *
     * @return the login attempt common dao
     */
    LoginAttemptCommonDAO getLoginAttemptCommonDAO();

}
