package de.soco.software.simuspace.suscore.user.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.model.LanguageDTO;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.UserPasswordDTO;
import de.soco.software.simuspace.suscore.common.model.UserProfileDTO;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.user.dao.AclSecurityIdentityDAO;

/**
 * The Interface UserManager manage the User CRUD (and other) operation to Dao layer.
 */
public interface UserManager {

    /**
     * Creates the user.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param user
     *         the user
     *
     * @return the user entity
     *
     * @throws SusException
     *         the sus exception
     * @apiNote To be used in service calls only
     */
    UserDTO createUser( String userIdFromGeneralHeader, UserDTO user );

    /**
     * Creates the inactive user for oauth sign up.
     *
     * @return the user entity
     *
     * @throws SusException
     *         the sus exception
     * @apiNote To be used in service calls only
     */
    UserDTO createInactiveUserForOAuth( UserDTO userDTO, SuSUserDirectoryEntity userDirectoryEntity );

    /**
     * A method for authenticating user credentials from database.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param isAlreadyAuthenticated
     *         the is already authenticated
     *
     * @return user user dto
     */
    UserDTO authenticate( EntityManager entityManager, UserDTO user, Boolean isAlreadyAuthenticated );

    /**
     * Update a user.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param user
     *         the user
     *
     * @return User
     *
     * @throws SusException
     *         the sus exception
     * @apiNote To be used in service calls only
     */
    UserDTO updateUser( String userIdFromGeneralHeader, UserDTO user );

    /**
     * This function is responsible to return a user profile object if the user exists against the provided user ID..
     *
     * @param currentUserId
     *         the current user id
     * @param userId
     *         the user id
     *
     * @return the user object against the provided userId.
     *
     * @apiNote To be used in service calls only
     */
    UserDTO getUserById( String currentUserId, UUID userId );

    /**
     * Gets user by id.
     *
     * @param entityManager
     *         the entity manager
     * @param currentUserId
     *         the current user id
     * @param userId
     *         the user id
     *
     * @return the user by id
     */
    UserDTO getUserById( EntityManager entityManager, String currentUserId, UUID userId );

    /**
     * Gets the user by uid.
     *
     * @param uid
     *         the uid
     *
     * @return the user by uid
     *
     * @apiNote To be used in service calls only
     */
    UserEntity getUserByUid( String uid );

    /**
     * Gets the user list by uid list.
     *
     * @param entityManager
     *         the entity manager
     * @param uidList
     *         the uid list
     *
     * @return the user by uid
     */
    List< UserEntity > getUsersByUidList( EntityManager entityManager, List< UUID > uidList );

    /**
     * Gets the context router.
     *
     * @param filter
     *         the filter
     *
     * @return the context router
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getContextRouter( FiltersDTO filter );

    /**
     * Gets the context router.
     *
     * @param entityManager
     *         the entity manager
     * @param filter
     *         the filter
     *
     * @return the context router
     */
    List< ContextMenuItem > getContextRouter( EntityManager entityManager, FiltersDTO filter );

    /**
     * This function is to fetch all the users (Local Directory, Active Directory, LDAP) and return as a paginated view.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return Filtered users.
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< UserDTO > getAllUsers( String userIdFromGeneralHeader, FiltersDTO filter );

    /**
     * This function is to fetch all the users (Local Directory, Active Directory, LDAP) and return as a paginated view.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param dirId
     *         the dir id
     * @param userUid
     *         the user uid
     *
     * @return Filtered users.
     *
     * @apiNote To be used in service calls only
     */
    UIForm createUserForm( String userIdFromGeneralHeader, UUID dirId, String userUid );

    /**
     * Edits the user form.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editUserForm( String userIdFromGeneralHeader, UUID id );

    /**
     * Update user property user dto.
     *
     * @param userId
     *         the user id
     * @param propertyKey
     *         the property key
     * @param propertyValue
     *         the property value
     * @param currentUserId
     *         the current user id
     *
     * @return the user dto
     */
    UserDTO updateUserProperty( String userId, String propertyKey, String propertyValue, String currentUserId );

    UIFormItem getUserPropertyField( String propertyKey, String token );

    /**
     * gets all available languages.
     *
     * @return list of languages
     *
     * @apiNote To be used in service calls only
     */
    List< LanguageDTO > getAllLanguages();

    /**
     * To List all columns.
     *
     * @return List<TableColumn>
     */
    List< TableColumn > listUsersUI();

    /**
     * Update user password.
     *
     * @param userId
     *         the user id
     * @param userPasswordDTO
     *         the user password DTO
     *
     * @return String
     *
     * @apiNote To be used in service calls only
     */
    String updateUserPassword( String userId, UserPasswordDTO userPasswordDTO );

    /**
     * Edit user profile form sections map .
     *
     * @param id
     *         the id
     *
     * @return the map
     *
     * @apiNote To be used in service calls only
     */
    UIForm editUserProfileFormSections( UUID id );

    /**
     * Edit User Profile Form.
     *
     * @param id
     *         the id
     *
     * @return List<UIFormItem>
     *
     * @apiNote To be used in service calls only
     */
    UIForm editUserProfileForm( UUID id );

    /**
     * Update user profile.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param user
     *         the user
     *
     * @return UserProfileDTO
     *
     * @apiNote To be used in service calls only
     */
    UserProfileDTO updateUserProfile( String userIdFromGeneralHeader, UserProfileDTO user );

    /**
     * To View Single User profile.
     *
     * @return List<TableColumn>
     */
    List< TableColumn > singleUserUI();

    /**
     * Gets the security identity DAO.
     *
     * @return the security identity DAO
     */
    AclSecurityIdentityDAO getSecurityIdentityDAO();

    /**
     * Sets the security identity DAO.
     *
     * @param securityIdentityDAO
     *         the new security identity DAO
     */
    void setSecurityIdentityDAO( AclSecurityIdentityDAO securityIdentityDAO );

    /**
     * Gets the users by group entity.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the users by group entity
     */
    List< UserEntity > getUsersByGroupEntity( EntityManager entityManager, GroupEntity entity );

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
     * @param entity
     *         the entity
     * @param filter
     *         the filter
     *
     * @return the users by group entity
     */
    List< UserEntity > getFilteredUsersByGroupEntity( EntityManager entityManager, GroupEntity entity, FiltersDTO filter );

    /**
     * Validate user password in database boolean.
     *
     * @param userId
     *         the user id
     * @param password
     *         the password
     *
     * @return the boolean
     */
    boolean validateUserPasswordInDatabase( String userId, String password );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Delete user by selection.
     *
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param id
     *         the id
     * @param mode
     *         the mode
     *
     * @return the boolean
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteUserBySelection( String userNameFromGeneralHeader, String id, String mode );

    /**
     * Delete user by selection.
     *
     * @param entityManager
     *         the entity manager
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param id
     *         the id
     * @param mode
     *         the mode
     *
     * @return the boolean
     */
    boolean deleteUserBySelection( EntityManager entityManager, String userNameFromGeneralHeader, String id, String mode );

    /**
     * Gets the all user table selection.
     *
     * @param selectionId
     *         the selection id
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the all user table selection
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< UserDTO > getAllUserTableSelection( String selectionId, String userId, FiltersDTO filter );

    /**
     * Gets the all user table selection.
     *
     * @param selectionId
     *         the selection id
     * @param userId
     *         the user id
     *
     * @return the all user table selection
     *
     * @apiNote To be used in service calls only
     */
    List< UserDTO > getAllUsersBySelectionId( String selectionId, String userId );

    /**
     * Gets user entity by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the user entity by id
     */
    UserEntity getUserEntityById( EntityManager entityManager, UUID id );

    void isPermitted( EntityManager entityManager, String userIdFromGeneralHeader, String id, String premissionType, String message,
            String name );

    /**
     * Prepare user model from user entity.
     *
     * @param userEntity
     *         the user entity
     *
     * @return the user DTO
     *
     * @apiNote To be used in service calls only
     */
    UserDTO prepareUserModelFromUserEntity( UserEntity userEntity );

    /**
     * Prepare user model from user entity user dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     *
     * @return the user dto
     */
    UserDTO prepareUserModelFromUserEntity( EntityManager entityManager, UserEntity userEntity );

    /**
     * Has location permission boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param permissionType
     *         the permission type
     *
     * @return the boolean
     */
    boolean hasLocationPermission( EntityManager entityManager, String userIdFromGeneralHeader, String id, String permissionType );

    /**
     * Get all user ids.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return List<UUID> all user ids
     */
    List< UUID > getAllUserIds( EntityManager entityManager );

    /**
     * Prepare user model from user entity witout directory.
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     *
     * @return the user DTO
     */
    UserDTO prepareUserModelFromUserEntityWitoutDirectory( EntityManager entityManager, UserEntity userEntity );

    /**
     * Is User Exist In Ldap Directory.
     *
     * @param token
     *         the token
     *
     * @return boolean boolean
     */
    boolean isUserAuthenticateInLdap( String uid, String password );

    /**
     * Prepare user token string.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     *
     * @return the string
     */
    String prepareUserToken( EntityManager entityManager, UserDTO user );

    /**
     * Gets the user token map.
     *
     * @return the user token map
     */
    Map< String, SuSCoreSessionDTO > getUserTokenMap( String token );

    /**
     * Gets the user failded ldap attempts.
     *
     * @param userUid
     *         the user uid
     *
     * @return the user failded ldap attempts
     */
    Integer getUserFaildedLdapAttempts( String userUid );

    /**
     * Gets the user by user uid for verification.
     *
     * @param userUid
     *         the user uid
     *
     * @return the user by user uid for verification
     */
    UserDTO getUserByUserUidForVerification( String userUid );

    /**
     * List groups from user id ui table ui.
     *
     * @param userId
     *         the user id
     *
     * @return the table ui
     */
    TableUI listGroupsFromUserIdUI( String userId );

    /**
     * List groups from user id filtered response.
     *
     * @param userId
     *         the user id
     * @param groupId
     *         the group id
     * @param filter
     *         the filter
     *
     * @return the filtered response
     */
    FilteredResponse< SuSUserGroupDTO > listGroupsFromUserId( String userId, UUID groupId, FiltersDTO filter );

    /**
     * Gets context router for user groups.
     *
     * @param filter
     *         the filter
     *
     * @return the context router for user groups
     */
    List< ContextMenuItem > getContextRouterForUserGroups( FiltersDTO filter );

    /**
     * Gets all values for user table column.
     *
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for user table column
     */
    List< Object > getAllValuesForUserTableColumn( String columnName, String token );

}
