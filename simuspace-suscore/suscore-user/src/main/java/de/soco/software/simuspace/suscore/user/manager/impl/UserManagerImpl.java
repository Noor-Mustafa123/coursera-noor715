package de.soco.software.simuspace.suscore.user.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.auth.connect.ad.authentication.ActiveDirectoryCustomRealm;
import de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.LdapCustomAuthRealm;
import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDocument;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserProfile;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.UIThemeEnums;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.formitem.impl.TableFormItem;
import de.soco.software.simuspace.suscore.common.model.ComparisonDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.ImageView;
import de.soco.software.simuspace.suscore.common.model.LanguageDTO;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.UserDetail;
import de.soco.software.simuspace.suscore.common.model.UserPasswordDTO;
import de.soco.software.simuspace.suscore.common.model.UserProfileDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.ImageUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.LanguageEntity;
import de.soco.software.simuspace.suscore.data.entity.LoginAttemptEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserDetailEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.interceptors.dao.UserTokenDAO;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.user.dao.AclSecurityIdentityDAO;
import de.soco.software.simuspace.suscore.user.dao.LanguageDAO;
import de.soco.software.simuspace.suscore.user.dao.UserDAO;
import de.soco.software.simuspace.suscore.user.dao.UserDetailDAO;
import de.soco.software.simuspace.suscore.user.dao.UserGroupDAO;
import de.soco.software.simuspace.suscore.user.dao.UserLoginAttemptDAO;
import de.soco.software.simuspace.suscore.user.manager.DirectoryManager;
import de.soco.software.simuspace.suscore.user.manager.UserGroupManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;
import de.soco.software.simuspace.suscore.user.properties.SuperUserPropertiesManager;

/**
 * The Class UserManagerImpl manage the User CRUD (and other) operation to Dao layer.
 */
@Log4j2
public class UserManagerImpl implements UserManager {

    /**
     * The user dao.
     */
    private UserDAO userDAO;

    /**
     * The user details dao.
     */
    private UserDetailDAO userDetailDAO;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * the userLoginAttemptDAO reference.
     */
    private UserLoginAttemptDAO userLoginAttemptDAO;

    /**
     * the userTokenDAO reference.
     */
    private UserTokenDAO userTokenDAO;

    /**
     * the languageDAO reference.
     */
    private LanguageDAO languageDAO;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The Directory manager.
     */
    private DirectoryManager directoryManager;

    /**
     * The ldap auth realm.
     */
    private LdapCustomAuthRealm ldapAuthRealm;

    /**
     * The ad custom realm.
     */
    private ActiveDirectoryCustomRealm adCustomRealm;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The security identity DAO.
     */
    private AclSecurityIdentityDAO securityIdentityDAO;

    /**
     * The context selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The sus DAO.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The user group manager.
     */
    private UserGroupManager userGroupManager;

    /**
     * The user group DAO.
     */
    private UserGroupDAO userGroupDAO;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The document manager.
     */
    private DocumentManager documentManager;

    /**
     * The Token manager.
     */
    private UserTokenManager tokenManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant IS_LOCATION.
     */
    private static final String IS_LOCATION = "isLocation";

    /**
     * The Constant CHANGABLE.
     */
    private static final String CHANGABLE = "changable";

    /**
     * The Constant RESTRICTED_USER.
     */
    private static final String RESTRICTED_USER = "Yes";

    /**
     * The Constant LDAP_DIR_FIRST_NAME.
     */
    private static final String LDAP_DIR_FIRST_NAME = "susUserDirectoryDTO.userDirectoryAttribute.ldapFirstName";

    /**
     * The Constant LDAP_DIR_SURNAME.
     */
    private static final String LDAP_DIR_SURNAME = "susUserDirectoryDTO.userDirectoryAttribute.ldapSurName";

    /**
     * constant STATUS_KEY.
     */
    private static final String STATUS_KEY = "status";

    /**
     * constant PASSWORD_KEY.
     */
    private static final String PASS_KEY = "password";

    /**
     * The Constant USER_PLUGIN_NAME.
     */
    private static final String USER_PLUGIN_NAME = "plugin_user";

    /**
     * first index of list.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant DIRECTORY.
     */
    private static final String DIRECTORY = "susUserDirectoryDTO.name";

    /**
     * The Constant LOCATION_SELECTION_PATH.
     */
    private static final String LOCATION_SELECTION_PATH = "system/location";

    /**
     * The constant LOCATION_SELECTION_CONNECTED_TABLE_LABEL.
     */
    private static final String LOCATION_SELECTION_CONNECTED_TABLE_LABEL = "name";

    /**
     * The Constant SELECTION_TYPE_TABLE.
     */
    private static final String SELECTION_TYPE_TABLE = "connected-table";

    /**
     * The Constant USER.
     */
    private static final String USER = "User";

    /**
     * The Constant OLD_PASSWD.
     */
    private static final String OLD_PASSWD = "Old Password";

    /**
     * The Constant NEW_PASSWD.
     */
    private static final String NEW_PASSWD = "New Password";

    /**
     * The Constant CONFIRM_PASSWD.
     */
    private static final String CONFIRM_PASSWD = "Confirm New Password";

    private static final String REQUIRED = "required";

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO createUser( String userIdFromGeneralHeader, UserDTO user ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !licenseManager.isTokenBasedLicenseExists( entityManager ) ) {
                licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.USERS.getKey() );
            }
            isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.USERS.getId(),
                    PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), USER );

            if ( user == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROVIDE_USER_DETAIL.getKey() ) );
            }
            if ( user.getSusUserDirectoryDTO() == null ) {

                throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_IS_NOT_VALID.getKey(),
                        user.getSusUserDirectoryDTO().getName() ) );

            }
            SuSUserDirectoryDTO susUserDirectory = directoryManager.readDirectory( entityManager, user.getSusUserDirectoryDTO().getId(),
                    true );
            if ( susUserDirectory == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_IS_NOT_VALID.getKey(),
                        user.getSusUserDirectoryDTO().getName() ) );

            }
            if ( !StringUtils.isBlank( user.getSusUserDirectoryDTO().getUserDirectoryAttribute().getLdapFirstName() ) ) {
                susUserDirectory.getUserDirectoryAttribute()
                        .setLdapFirstName( user.getSusUserDirectoryDTO().getUserDirectoryAttribute().getLdapFirstName() );
            }
            if ( !StringUtils.isBlank( user.getSusUserDirectoryDTO().getUserDirectoryAttribute().getLdapSurName() ) ) {
                susUserDirectory.getUserDirectoryAttribute()
                        .setLdapSurName( user.getSusUserDirectoryDTO().getUserDirectoryAttribute().getLdapSurName() );
            }

            user.setSusUserDirectoryDTO( susUserDirectory );
            // validation of user
            boolean isLdap = user.getSusUserDirectoryDTO().getType().equals( ConstantsUserDirectories.LDAP_DIRECTORY );
            Notification notification = user.validate( false, isLdap );
            if ( isLdap ) {
                user.setUserUid( ldapAuthRealm.getCaseSensitiveUserUidFromLdap( user.getUserUid(), user.getSusUserDirectoryDTO() ) );
            }
            notification.addNotification( validateUserNameInDirectories( user ) );
            if ( notification.hasErrors() ) {
                throw new SusException( notification.getErrors().toString() );
            }
            if ( userDAO.isUserExist( entityManager, user.getUserUid() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ALREADY_EXIST.getKey() ) );
            }
            UserEntity userEntity = prepareUserEntityFromUserModel( entityManager, user, new UserEntity() );
            String password = getEncryptedPasswordForUser( user.getPassword(), userEntity.getPassword() );
            if ( StringUtils.isNotEmpty( password ) ) {
                userEntity.setPassword( password );
            }
            AclSecurityIdentityEntity securityIdentityEntity = saveSecurityIdentity( entityManager, userEntity );
            if ( securityIdentityEntity != null ) {
                userEntity.setSecurityIdentityEntity( securityIdentityEntity );
            }
            // set changeable flag to be true on user creation
            if ( userEntity.getDirectory() != null && userEntity.getDirectory().getType()
                    .equals( ConstantsUserDirectories.ACTIVE_DIRECTORY ) || userEntity.getDirectory().getType()
                    .equals( ConstantsUserDirectories.LDAP_DIRECTORY ) ) {
                updateNameOfUserEntityFromLdapAndActiveDirectories( userEntity );
                userEntity.setChangeable( false );
            } else {
                userEntity.setChangeable( user.isChangable() );
            }

            // Audit log entry
            if ( Boolean.TRUE.equals( PropertiesManager.isAuditUser() ) ) {
                userEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForObjects(
                        "User : " + user.getName() + ConstantsString.SPACE + ConstantsDbOperationTypes.CREATED,
                        ConstantsDbOperationTypes.CREATED, userIdFromGeneralHeader, "", user.getName(), "User" ) );
            }

            // set createdBy into Entity
            UserEntity createdBy = userDAO.findById( entityManager, UUID.fromString( userIdFromGeneralHeader ) );
            userEntity.setCreatedBy( createdBy );
            userEntity.setModifiedBy( createdBy );
            userEntity.setStatus( null != user.getStatus() && user.getStatus().equalsIgnoreCase( "Active" ) );

            var newUser = userDAO.createUser( entityManager, userEntity );
            new Thread( () -> objectViewManager.createCustomViewsForUser( newUser.getId() ) ).start();
            return prepareUserModelFromUserEntity( entityManager, newUser );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public UserDTO createInactiveUserForOAuth( UserDTO user, SuSUserDirectoryEntity suSUserDirectoryEntity ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {

            // setting the SusUserDirectoryDTO property in user
            SuSUserDirectoryDTO suSUserDirectoryDTO = directoryManager.prepareDirectoryModelFromEntity( entityManager,
                    suSUserDirectoryEntity, false );
            user.setSusUserDirectoryDTO( suSUserDirectoryDTO );

            if ( user == null ) {
                log.warn( MessageBundleFactory.getMessage( Messages.PROVIDE_USER_DETAIL.getKey() ) );
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROVIDE_USER_DETAIL.getKey() ) );
            }
            if ( user.getSusUserDirectoryDTO() == null ) {
                log.warn( MessageBundleFactory.getMessage( Messages.DIRECTORY_IS_NOT_VALID.getKey() ) );
                throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_IS_NOT_VALID.getKey(),
                        user.getSusUserDirectoryDTO().getName() ) );
            }

            UserEntity userEntity = prepareUserEntityFromUserModel( entityManager, user, new UserEntity() );

            if ( StringUtils.isNotEmpty( user.getPassword() ) ) {
                userEntity.setPassword( user.getPassword() );
            }

            AclSecurityIdentityEntity securityIdentityEntity = saveSecurityIdentity( entityManager, userEntity );
            if ( securityIdentityEntity != null ) {
                userEntity.setSecurityIdentityEntity( securityIdentityEntity );
            }
            var newUser = userDAO.createUser( entityManager, userEntity );
            new Thread( () -> objectViewManager.createCustomViewsForUser( newUser.getId() ) ).start();
            return prepareUserModelFromUserEntity( entityManager, newUser );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Check if User Authenticated Via Directory Params for user.
     *
     * @param userEntity
     *         the user entity
     */
    private void updateNameOfUserEntityFromLdapAndActiveDirectories( UserEntity userEntity ) {
        String[] names;

        if ( userEntity.getDirectory().getType().equalsIgnoreCase( ConstantsUserDirectories.LDAP_DIRECTORY ) ) {
            names = ldapAuthRealm.getUserFirstNameAndSurNameFromUid( userEntity.getUserUid() );
        } else if ( userEntity.getDirectory().getType().equalsIgnoreCase( ConstantsUserDirectories.ACTIVE_DIRECTORY ) ) {
            names = adCustomRealm.getUserFirstNameAndSurNameFromUid( userEntity.getUserUid() );
        } else {
            return;
        }

        userEntity.setLdapFirstName( names[ 0 ] );
        userEntity.setLdapSurName( names[ 1 ] );

        // firstname and surname are same as ldap first name and ldap sur name for ldap users
        userEntity.setFirstName( names[ 0 ] );
        userEntity.setSurName( names[ 1 ] );
    }

    /**
     * User security identity.
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     *
     * @return the security identity entity
     */
    private AclSecurityIdentityEntity saveSecurityIdentity( EntityManager entityManager, UserEntity userEntity ) {
        AclSecurityIdentityEntity securityIdentityEntity = new AclSecurityIdentityEntity();
        securityIdentityEntity.setId( UUID.randomUUID() );
        securityIdentityEntity.setSid( userEntity.getId() );
        securityIdentityEntity.setPrinciple( Boolean.TRUE );
        securityIdentityEntity.setCreatedOn( new Date() );
        securityIdentityEntity = securityIdentityDAO.save( entityManager, securityIdentityEntity );
        return securityIdentityEntity;
    }

    /**
     * Validate user in directories.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param isAlreadyAuthenticated
     *         the is already authenticated
     */
    private void validateUserInDirectories( EntityManager entityManager, UserDTO user, Boolean isAlreadyAuthenticated ) {
        SuSUserDirectoryDTO dir = user.getSusUserDirectoryDTO();
        if ( Boolean.FALSE.equals( isAlreadyAuthenticated ) ) {
            if ( !isUserAuthenticatedViaDirectoryParams( entityManager, user, dir ) ) {
                manageFailedLoginAttempts( entityManager, user.getUserUid() );
            } else {
                LoginAttemptEntity loginAttemptEntity = userLoginAttemptDAO.getFailedLoginAttempts( entityManager, user.getUserUid() );
                if ( null != loginAttemptEntity ) {
                    loginAttemptEntity.setAttempts( ConstantsInteger.INTEGER_VALUE_ZERO );
                    userLoginAttemptDAO.updateFailedLoginAttempt( entityManager, prepareLoginAttemptEntity( loginAttemptEntity ) );
                }
            }
        }
    }

    /**
     * Checks if is user authenticated via directory params.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param dir
     *         the dir
     *
     * @return true, if is user authenticated via directory params
     */
    private boolean isUserAuthenticatedViaDirectoryParams( EntityManager entityManager, UserDTO user, SuSUserDirectoryDTO dir ) {
        UsernamePasswordToken token = new UsernamePasswordToken( user.getUserUid(), user.getPassword() );
        // check in ldap
        if ( dir.getType().equalsIgnoreCase( ConstantsUserDirectories.LDAP_DIRECTORY ) ) {
            return isUserExistInLdap( entityManager, token, dir.getUserDirectoryAttribute() );
        } else if ( dir.getType().equalsIgnoreCase( ConstantsUserDirectories.ACTIVE_DIRECTORY ) ) {
            return isUserExistInActiveDirectory( token, dir.getUserDirectoryAttribute() );
        } else {
            // for other directories no need to Authenticate
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO authenticate( EntityManager entityManager, UserDTO user, Boolean isAlreadyAuthenticated ) {
        UserDTO authenticatedUser = null;
        String plainPassword;
        UserDTO superUser = authenticateSuperUser( user );
        if ( null != superUser ) {
            return superUser;
        }
        UserEntity userEntity = userDAO.getUserByUid( entityManager, user.getUserUid() );
        if ( userEntity != null ) {

            // set directory
            if ( userEntity.getDirectory() != null ) {
                if ( !userEntity.getDirectory().getStatus() ) {
                    throw new IncorrectCredentialsException(
                            MessageBundleFactory.getMessage( Messages.DIRECTORY_DISABLED_WHICH_USER_BELONGS_TO.getKey() ) );
                }
                SuSUserDirectoryDTO dir = directoryManager.prepareDirectoryModel( userEntity.getDirectory() );
                user.setSusUserDirectoryDTO( dir );
                plainPassword = user.getPassword();
                if ( !dir.getType().equalsIgnoreCase( ConstantsUserDirectories.INTERNAL_DIRECTORY ) ) {
                    // ldap-ad credentials validation
                    if ( userEntity.isStatus() ) {
                        validateUserInDirectories( entityManager, user, isAlreadyAuthenticated );
                    } else {
                        LoginAttemptEntity loginAttemptEntity = userLoginAttemptDAO.getFailedLoginAttempts( entityManager,
                                user.getUserUid() );
                        if ( loginAttemptEntity != null && Boolean.TRUE.equals( loginAttemptEntity.getLocked() ) ) {
                            updateUnlockTime( entityManager, loginAttemptEntity );
                            throw new ExcessiveAttemptsException(
                                    MessageBundleFactory.getMessage( Messages.TO_MANY_ATTEMPTS_TRY_AGAIN_IN_N_MINUTES.getKey(),
                                            PropertiesManager.getAccountLockTimeForFailedAttempts() ) );
                        }
                        throw new LockedAccountException( MessageBundleFactory.getMessage( Messages.USER_ACCOUNT_IS_INACTIVE.getKey() ) );
                    }
                }

                /* encrypt password so that shiro could authenticate the user */
                user.setPassword( FileUtils.getSha256CheckSum( plainPassword ) );
                // for first login , password change flag need to be send in
                // response
                user.setChangable( userEntity.isChangeable() );

                authenticatedUser = authenticateUser( entityManager, userEntity, user );
            }
            return authenticatedUser;
        } else {
            manageFailedLoginAttempts( entityManager, user.getUserUid() );
            throw new IncorrectCredentialsException( MessageBundleFactory.getMessage( Messages.INVALID_LOGIN_CREDENTIALS.getKey() ) );
        }
    }

    /**
     * A method for managing failed login attempts.
     *
     * @param entityManager
     *         the entity manager
     * @param username
     *         the username
     */
    private void manageFailedLoginAttempts( EntityManager entityManager, String username ) {
        LoginAttemptEntity loginAttemptEntity = userLoginAttemptDAO.getFailedLoginAttempts( entityManager, username );
        if ( loginAttemptEntity == null ) {
            userLoginAttemptDAO.saveFailedLoginAttempt( entityManager, username );
        } else {
            if ( loginAttemptEntity.getAttempts() < Integer.parseInt( PropertiesManager.getFailedLoginAttempt() ) ) {
                loginAttemptEntity.setAttempts( loginAttemptEntity.getAttempts() + ConstantsInteger.INTEGER_VALUE_ONE );
                userLoginAttemptDAO.updateFailedLoginAttempt( entityManager, prepareLoginAttemptEntity( loginAttemptEntity ) );
            } else {
                lockUserAccount( entityManager, loginAttemptEntity, username );
                throw new ExcessiveAttemptsException(
                        MessageBundleFactory.getMessage( Messages.TO_MANY_ATTEMPTS_TRY_AGAIN_IN_N_MINUTES.getKey(),
                                PropertiesManager.getAccountLockTimeForFailedAttempts() ) );
            }
        }
        throw new IncorrectCredentialsException( MessageBundleFactory.getMessage( Messages.INVALID_LOGIN_CREDENTIALS.getKey() ) );
    }

    /**
     * Lock user account.
     *
     * @param entityManager
     *         the entity manager
     * @param loginAttemptEntity
     *         the login attempt entity
     * @param userEntity
     *         the user entity
     */
    private void lockUserAccount( EntityManager entityManager, LoginAttemptEntity loginAttemptEntity, String userName ) {
        UserEntity userEntity = userDAO.getUserByUid( entityManager, userName );
        if ( userEntity != null ) {
            userEntity.setStatus( false );
            userDAO.updateUserEntity( entityManager, userEntity, null );
        }
        loginAttemptEntity.setLockTime( new Date() );
        loginAttemptEntity.setUnlockTime(
                DateUtils.getNMinutesLaterDate( new Date(), Integer.parseInt( PropertiesManager.getAccountLockTimeForFailedAttempts() ) ) );
        loginAttemptEntity.setLocked( true );
        userLoginAttemptDAO.updateFailedLoginAttempt( entityManager, prepareLoginAttemptEntity( loginAttemptEntity ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO updateUser( String userId, UserDTO user ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !licenseManager.isTokenBasedLicenseExists( entityManager ) ) {
                licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.USERS.getKey() );
            }
            isPermitted( entityManager, userId, SimuspaceFeaturesEnum.USERS.getId(), PermissionMatrixEnum.WRITE.getValue(),
                    Messages.NO_RIGHTS_TO_UPDATE.getKey(), USER );
            UserEntity userEntity = userDAO.getUserEntityById( entityManager, UUID.fromString( user.getId() ) );

            if ( null != userEntity ) {
                if ( !user.getUserUid().equals( userEntity.getUserUid() ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.UID_CANNOT_BE_CHANGED.getKey() ) );
                }
                user.setSusUserDirectoryDTO(
                        directoryManager.prepareDirectoryModelFromEntityWithoutUsers( userEntity.getDirectory(), true ) );
                boolean isLdap = userEntity.getDirectory().getType().equals( ConstantsUserDirectories.LDAP_DIRECTORY );
                Notification notif = user.validate( true, isLdap );
                checksForUserNameUniqnessOnUpdation( entityManager, notif, user, userEntity );

                if ( notif.hasErrors() ) {
                    throw new SusException( notif.getErrors().toString() );
                }
                if ( !user.getSusUserDirectoryDTO().getType().equalsIgnoreCase( ConstantsUserDirectories.INTERNAL_DIRECTORY ) ) {
                    userEntity.setPassword( userEntity.getPassword() );
                }
                if ( CollectionUtils.isNotEmpty( userEntity.getGroups() ) && CollectionUtils.isEmpty( user.getGroups() ) ) {
                    List< SuSUserGroupDTO > groupEntities = prepareUserGroupDTO( userEntity.getGroups() );
                    user.setGroups( groupEntities );
                    user.setGroupsCount( groupEntities.size() );
                }
                // set groups
                List< AuditLogEntity > logs = createAuditLogForGroups( entityManager, userId, userEntity, user );
                userEntity.setGroups(
                        prepareUserGroups( entityManager, user, prepareUserEntityFromUserModel( entityManager, user, userEntity ) ) );
                if ( userId.equals( ConstantsID.SUPER_USER_ID ) ) {
                    String password = getEncryptedPasswordForUser( user.getPassword(), userEntity.getPassword() );
                    if ( StringUtils.isNotEmpty( password ) ) {
                        userEntity.setPassword( password );
                    }
                }
                if ( !userId.equals( ConstantsUserProfile.SUPER_USER_ID.toString() ) ) {
                    UserEntity oldUserEntity = userDAO.findById( entityManager, UUID.fromString( user.getId() ) );
                    if ( Boolean.TRUE.equals( PropertiesManager.isAuditUser() ) ) {
                        AuditLogEntity auditLog = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userId, oldUserEntity, userEntity,
                                userEntity.getId().toString(), user.getName(), "User" );
                        if ( null != auditLog ) {
                            auditLog.setObjectId( oldUserEntity.getId().toString() );
                        }
                        userEntity.setAuditLogEntity( auditLog );
                    }
                    UserEntity modifiedBy = userDAO.findById( entityManager, UUID.fromString( userId ) );
                    userEntity.setModifiedBy( modifiedBy );
                }

                updateNameOfUserEntityFromLdapAndActiveDirectories( userEntity );
                UserDTO userToReturn = prepareUserModelFromUserEntity( entityManager,
                        userDAO.updateUserEntity( entityManager, userEntity, logs ) );

                if ( !userToReturn.getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE ) ) {
                    expireTokensForDeactivatedUser( entityManager,
                            userTokenDAO.getUserTokenEntityListByUserId( entityManager, UUID.fromString( userToReturn.getId() ) ) );
                } else if ( userEntity.getStatus() == Boolean.FALSE ) {
                    // update payload has status =  active while saved userEntity has status = inactive
                    unlockUserAccount( user, entityManager );
                }
                boolean isLanguageChanged = detectLanguageChangeInUserUpdate( user, userEntity );
                return userToReturn;
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Activate user account.
     *
     * @param user
     *         the user
     * @param entityManager
     *         the entity manager
     */
    private void unlockUserAccount( UserDTO user, EntityManager entityManager ) {
        LoginAttemptEntity loginAttemptEntity = userLoginAttemptDAO.getFailedLoginAttempts( entityManager, user.getUserUid() );
        loginAttemptEntity.setAttempts( ConstantsInteger.INTEGER_VALUE_ZERO );
        loginAttemptEntity.setLocked( false );
        loginAttemptEntity.setUnlockTime( null );
        loginAttemptEntity.setLockTime( null );
        userLoginAttemptDAO.updateFailedLoginAttempt( entityManager, prepareLoginAttemptEntity( loginAttemptEntity ) );
    }

    /**
     * Creates the audit log for users.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param userEntity
     *         the group entity
     * @param dto
     *         the dto
     *
     * @return the list
     */
    private List< AuditLogEntity > createAuditLogForGroups( EntityManager entityManager, String userId, UserEntity userEntity,
            UserDTO dto ) {
        List< AuditLogEntity > list = null;

        if ( userId != null && !userId.equals( ConstantsUserProfile.DEFAULT_USER_ID ) ) {

            List< UUID > oldList =
                    CollectionUtil.isNotEmpty( userEntity.getGroups() ) ? userEntity.getGroups().stream().map( GroupEntity::getId ).toList()
                            : new ArrayList<>();

            List< UUID > newList =
                    CollectionUtil.isNotEmpty( dto.getGroups() ) ? dto.getGroups().stream().map( SuSUserGroupDTO::getId ).toList()
                            : new ArrayList<>();

            ComparisonDTO grpNamesList = CollectionUtil.getComparedList( newList, oldList );

            list = prepareGroupAuditLogList( entityManager, userId, userEntity, grpNamesList );

        }

        return list;
    }

    /**
     * Prepare user audit log list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param userEntity
     *         the group entity
     * @param groupNamesList
     *         the ids list
     *
     * @return the list
     */
    private List< AuditLogEntity > prepareGroupAuditLogList( EntityManager entityManager, String userId, UserEntity userEntity,
            ComparisonDTO groupNamesList ) {
        List< AuditLogEntity > list = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( groupNamesList.getAdded() ) ) {
            for ( UUID addgrpId : groupNamesList.getAdded() ) {
                GroupEntity grp = userGroupDAO.readUserGroup( entityManager, addgrpId );
                list.add( AuditLogDTO.prepareAuditLogEntityForObjects( userEntity.getFirstName() + " assigned To Group" + grp.getName(),
                        ConstantsDbOperationTypes.ASSIGNED, userId ) );
            }
        }
        if ( CollectionUtil.isNotEmpty( groupNamesList.getRemoved() ) ) {
            for ( UUID removeGrpId : groupNamesList.getRemoved() ) {
                GroupEntity grp = userGroupDAO.readUserGroup( entityManager, removeGrpId );
                list.add( AuditLogDTO.prepareAuditLogEntityForObjects( userEntity.getFirstName() + " removed from Group" + grp.getName(),
                        ConstantsDbOperationTypes.DE_ASSIGN, userId ) );
            }
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String updateUserPassword( String userId, UserPasswordDTO userPasswordDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Notification notification = userPasswordDTO.validate();
            if ( notification.hasErrors() ) {
                throw new SusException( notification.getErrors().toString() );
            }
            UserEntity userEntity = userDAO.getUserEntityById( entityManager, UUID.fromString( userId ) );
            if ( null != userEntity ) {
                if ( userEntity.getPassword().equals( FileUtils.getSha256CheckSum( userPasswordDTO.getOldPassword() ) ) ) {
                    userEntity.setChangeable( false );
                    userEntity.setStatus( true );
                    userEntity.setPassword( FileUtils.getSha256CheckSum( userPasswordDTO.getConfirmPassword() ) );
                    userDAO.saveOrUpdate( entityManager, userEntity );
                    return MessageBundleFactory.getMessage( Messages.PASSWORD_CHANGED_SUCCESSFULLY.getKey() );
                } else {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.OLD_PASSWORD_DOES_NOT_MATCHED.getKey() ) );
                }
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserByUid( String uid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return userDAO.getUserByUid( entityManager, uid );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserEntity > getUsersByUidList( EntityManager entityManager, List< UUID > uidList ) {
        return userDAO.getLatestNonDeletedObjectsByIds( entityManager, uidList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserProfileDTO updateUserProfile( String userId, UserProfileDTO userProfileDto ) {
        if ( !userId.equals( userProfileDto.getId() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.UPDATE_NOT_ALLOWED.getKey() ) );
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.USERS.getKey() );
            UserEntity userEntity = userDAO.getUserEntityById( entityManager, UUID.fromString( userProfileDto.getId() ) );
            if ( null != userEntity ) {
                if ( !userProfileDto.getUid().equals( userEntity.getUserUid() ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.UID_CANNOT_BE_CHANGED.getKey() ) );
                }
                boolean isLdap = userEntity.getDirectory().getType().equals( ConstantsUserDirectories.LDAP_DIRECTORY );
                Notification notif = userProfileDto.validate( isLdap );
                if ( notif.hasErrors() ) {
                    throw new SusException( notif.getErrors().toString() );
                }
                UserDTO user = prepareUserDtoFromUserProfile( userProfileDto );
                boolean isLanguageChanged = detectLanguageChangeInUserUpdate( user, userEntity );
                user.setSusUserDirectoryDTO(
                        directoryManager.prepareDirectoryModelFromEntityWithoutUsers( userEntity.getDirectory(), true ) );
                user.setStatus( userEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
                checksForUserNameUniqnessOnUpdation( entityManager, notif, user, userEntity );

                if ( checkNotEmptyPasswords( userProfileDto.getUserPasswordDto() ) ) {
                    if ( !StringUtils.isEmpty( userProfileDto.getUserPasswordDto().getNewPassword() ) && !FileUtils.getSha256CheckSum(
                            userProfileDto.getUserPasswordDto().getOldPassword() ).equals( userEntity.getPassword() ) ) {
                        notif.addError( new Error( MessageBundleFactory.getMessage( Messages.OLD_PASSWORD_DOES_NOT_MATCHED.getKey() ) ) );
                    }
                    if ( notif.hasErrors() ) {
                        throw new SusException( notif.getErrors().toString() );
                    }
                    getUserPassword( userProfileDto.getUserPasswordDto(), userEntity, user );
                }

                checkUserPhoto( entityManager, userProfileDto, userEntity );

                UserEntity userEntityPrepared = prepareUserEntityFromUserModel( entityManager, user, userEntity );
                String password = getEncryptedPasswordForUser( user.getPassword(), userEntity.getPassword() );
                if ( StringUtils.isNotEmpty( password ) ) {
                    userEntity.setPassword( password );
                }
                createAuditLogForUser( entityManager, userId, user, userEntityPrepared );
                UserProfileDTO userToReturn = prepareUserProfileDto( prepareUserModelFromUserEntity( entityManager,
                        userDAO.updateUserEntity( entityManager, userEntityPrepared, null ) ) );
                TokenizedLicenseUtil.updateUserInSession( user );
                if ( !user.getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE ) ) {
                    expireTokensForDeactivatedUser( entityManager,
                            userTokenDAO.getUserTokenEntityListByUserId( entityManager, UUID.fromString( userToReturn.getId() ) ) );
                }
                if ( isLanguageChanged ) {
                    selectionManager.sendCustomerEventForLanguage( entityManager, userId, userEntity );
                }
                return userToReturn;
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Detect language change in user update boolean.
     *
     * @param user
     *         the user
     * @param userEntity
     *         the user entity
     *
     * @return the boolean
     */
    private boolean detectLanguageChangeInUserUpdate( UserDTO user, UserEntity userEntity ) {
        var entityDetail = userEntity.getUserDetails().stream().findFirst().orElse( null );
        return entityDetail == null || !user.getUserDetails().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getLanguage()
                .equals( entityDetail.getLanguage() );
    }

    /**
     * Check not empty passwords.
     *
     * @param userProfileDto
     *         the user profile dto
     *
     * @return true, if successful
     */
    private boolean checkNotEmptyPasswords( UserPasswordDTO userPasswordDTO ) {
        if ( userPasswordDTO == null ) {
            return false;
        }
        return !StringUtils.isEmpty( userPasswordDTO.getOldPassword() ) || !StringUtils.isEmpty( userPasswordDTO.getNewPassword() )
                || !StringUtils.isEmpty( userPasswordDTO.getConfirmPassword() );
    }

    /**
     * Gets the user password.
     *
     * @param userPasswordDTO
     *         the user profile dto
     * @param userEntity
     *         the user entity
     * @param user
     *         the user
     */
    private void getUserPassword( UserPasswordDTO userPasswordDTO, UserEntity userEntity, UserDTO user ) {
        if ( StringUtils.isEmpty( userPasswordDTO.getOldPassword() ) && StringUtils.isEmpty( userPasswordDTO.getNewPassword() )
                && StringUtils.isEmpty( userPasswordDTO.getConfirmPassword() ) ) {
            user.setPassword( userEntity.getPassword() );
        } else {
            user.setPassword( userPasswordDTO.getNewPassword() );
        }
    }

    /**
     * Check user photo.
     *
     * @param entityManager
     *         the entity manager
     * @param userProfileDto
     *         the user profile dto
     * @param userEntity
     *         the user entity
     */
    private void checkUserPhoto( EntityManager entityManager, UserProfileDTO userProfileDto, UserEntity userEntity ) {
        if ( null != userProfileDto.getProfilePhoto() && CollectionUtils.isNotEmpty( userProfileDto.getProfilePhoto().getBrowser() ) ) {
            DocumentDTO document = documentManager.getDocumentById( entityManager,
                    UUID.fromString( userProfileDto.getProfilePhoto().getBrowser().get( FIRST_INDEX ).getId() ) );
            try {
                String path = PropertiesManager.getFeStaticPath() + documentManager.writeToDiskInFETemp( entityManager, document,
                        PropertiesManager.getFeStaticPath() );
                ImageUtil.createThumbNailForProfilePhoto( new File( path ), new File( path ) );
            } catch ( Exception e ) {
                log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_IMAGE.getKey() ), e );
                throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_IMAGE.getKey() ) );
            }
            setDocumentInUser( document, userEntity );
        } else {
            userEntity.setDocument( null );
        }
    }

    /**
     * Sets the document in user.
     *
     * @param docu
     *         the docu
     * @param userEntity
     *         the user entity
     */
    private void setDocumentInUser( DocumentDTO docu, UserEntity userEntity ) {
        DocumentEntity documentEntity = documentManager.prepareEntityFromDocumentDTO( docu );
        documentEntity.setOwner( userEntity );
        userEntity.setDocument( documentEntity );

    }

    /**
     * Create Audit Log.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param user
     *         the user
     * @param userEntityPrepared
     *         the user entity prepared
     */
    private void createAuditLogForUser( EntityManager entityManager, String userId, UserDTO user, UserEntity userEntityPrepared ) {
        if ( !userId.equals( ConstantsUserProfile.SUPER_USER_ID.toString() ) && userEntityPrepared != null ) {
            UserDTO dto = prepareUserModelFromUserEntity( entityManager, userEntityPrepared );
            if ( Boolean.TRUE.equals( PropertiesManager.isAuditUser() ) ) {
                AuditLogEntity auditLog = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userId, user, dto );
                if ( null != auditLog ) {
                    auditLog.setObjectId( user.getId() );
                }
                userEntityPrepared.setAuditLogEntity( auditLog );
            }
            UserEntity modifiedBy = userDAO.findById( entityManager, UUID.fromString( userId ) );
            userEntityPrepared.setModifiedBy( modifiedBy );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserById( String currentUserId, UUID userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getUserById( entityManager, currentUserId, userId );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public UserDTO getUserById( EntityManager entityManager, String currentUserId, UUID userId ) {
        licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.USERS.getKey() );
        if ( userId == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL_OR_EMPTY.getKey() ) );
        }
        if ( !currentUserId.equals( userId.toString() ) ) {
            isPermitted( entityManager, currentUserId, SimuspaceFeaturesEnum.USERS.getId(), PermissionMatrixEnum.READ.getValue(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), USER );
        }

        UserEntity entity = userDAO.getUserEntityById( entityManager, userId );
        if ( entity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );
        }
        // for groups
        UserDTO user = prepareUserModelFromUserEntity( entityManager, entity );
        List< GroupEntity > groupEntities = userGroupManager.getUserGroupsByUserId( entityManager, entity.getId() );
        if ( CollectionUtil.isNotEmpty( groupEntities ) ) {
            user.setGroups( prepareUserGroupDTO( new HashSet<>( groupEntities ) ) );
            user.setGroupsCount( groupEntities.size() );
        }

        // show edit button if current loggin user view his/her own profile
        if ( currentUserId.equals( userId.toString() ) ) {
            user.setEditable( true );
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > listUsersUI() {

        List< TableColumn > columns = GUIUtils.listColumns( UserDTO.class, UserDetail.class );
        columns.removeIf( column -> column.getName().equalsIgnoreCase( "userDetails.language" ) && !PropertiesManager.hasTranslation() );
        for ( TableColumn tableColumn : columns ) {
            if ( tableColumn.getName().equalsIgnoreCase( UserDTO.UID_UI_COLUMN_NAME ) ) {
                tableColumn.getRenderer().setUrl( "system/user/{id}" );
                tableColumn.getRenderer().setType( "link" );
            }
            if ( tableColumn.getName().equalsIgnoreCase( "profilePhoto.filePath" ) ) {
                tableColumn.getRenderer().setImageWidth( 100 );
            }
            if ( tableColumn.getName().equalsIgnoreCase( UserDTO.THEME_LABEL ) ) {
                tableColumn.setVisible( Boolean.FALSE );
            }
        }
        return columns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > singleUserUI() {
        List< TableColumn > columns = GUIUtils.listColumns( UserDTO.class, UserDetail.class );
        columns.removeIf( column -> column.getName().equalsIgnoreCase( "userDetails.language" ) && !PropertiesManager.hasTranslation() );
        for ( TableColumn tableColumn : columns ) {
            if ( tableColumn.getName().equalsIgnoreCase( "userUuid" ) ) {
                tableColumn.getRenderer().setType( "text" );
            }
            if ( tableColumn.getName().equalsIgnoreCase( "groups.name" ) ) {
                tableColumn.getRenderer().setType( "list" );
            }
        }
        return columns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< UserDTO > getAllUsers( String userIdFromGeneralHeader, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.USERS.getKey() );
        isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.USERS.getId(), PermissionMatrixEnum.READ.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), USER );

        if ( filter == null ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey(), SimuspaceFeaturesEnum.USERS.getKey() ) );
        }

        List< UserDTO > userDTOList = new ArrayList<>();
        List< UserEntity > userEntitiesList = userDAO.getAllFilteredRecords( entityManager, UserEntity.class, filter, Boolean.TRUE );
        if ( CollectionUtil.isNotEmpty( userEntitiesList ) ) {
            for ( UserEntity user : userEntitiesList ) {
                userDTOList.add( prepareUserModelFromUserEntityWithHiddenPasswd( entityManager, user ) );
            }
        }
        entityManager.close();
        return PaginationUtil.constructFilteredResponse( filter, userDTOList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LanguageDTO > getAllLanguages() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< LanguageEntity > languageEntityList = languageDAO.getAllLanguages( entityManager );
            List< LanguageDTO > languageDTOlistToSet = new ArrayList<>();
            for ( LanguageEntity languageEntity : languageEntityList ) {
                languageDTOlistToSet.add( prepareLanguageDTOFromLanguageEntity( languageEntity ) );
            }
            return languageDTOlistToSet;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextRouter( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getContextRouter( entityManager, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextRouterForUserGroups( FiltersDTO filter ) {
        return userGroupManager.getContextMenu( filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextRouter( EntityManager entityManager, FiltersDTO filter ) {
        // case of select all from data table
        if ( filter.getItems().isEmpty() && filter.getLength().toString().equalsIgnoreCase( "-1" ) ) {
            Long maxResults = userDAO.getAllFilteredRecordCountWithParentId( entityManager, UserEntity.class, filter,
                    UUID.fromString( SimuspaceFeaturesEnum.USERS.getId() ) );
            filter.setLength( Integer.valueOf( maxResults.toString() ) );
            List< UserEntity > allObjectsList = userDAO.getAllFilteredRecordsWithParent( entityManager, UserEntity.class, filter,
                    UUID.fromString( SimuspaceFeaturesEnum.USERS.getId() ) );
            List< Object > itemsList = new ArrayList<>();
            allObjectsList.stream().forEach( entity -> itemsList.add( entity.getId() ) );
            filter.setItems( itemsList );
        }
        List< Object > selectedIds = filter.getItems();
        if ( selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).equals( ConstantsID.SUPER_USER_ID ) ) {
            return new ArrayList<>();
            // For super user dont provide edit or delete option in context menu .
            // SUS-2803
        }
        return contextMenuManager.getContextMenu( entityManager, USER_PLUGIN_NAME, UserDTO.class, filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createUserForm( String userId, UUID dirId, String userUid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isPermitted( entityManager, userId, SimuspaceFeaturesEnum.USERS.getId(), PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(),
                    Messages.NO_RIGHTS_TO_CREATE.getKey(), USER );
            SuSUserDirectoryDTO directoryDTO = directoryManager.readDirectory( entityManager, dirId, true );
            if ( directoryDTO.getType().equals( ConstantsUserDirectories.LDAP_DIRECTORY ) ) {
                return prepareUIFormForLdapUser( userUid, directoryDTO );
            } else if ( directoryDTO.getType().equals( ConstantsUserDirectories.INTERNAL_DIRECTORY ) ) {
                return prepareUIFormForInternalUser( userUid );
            } else {
                //TODO Active directory case
                return new UIForm();
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare ui form for ldap user map.
     *
     * @param userUid
     *         the user uid
     * @param directoryDTO
     *         the directory dto
     *
     * @return the map
     */
    private UIForm prepareUIFormForLdapUser( String userUid, SuSUserDirectoryDTO directoryDTO ) {
        List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( true, new UserDTO(), new UserDetail() );
        String[] names = ldapAuthRealm.getUserFirstNameAndSurNameFromUid( userUid, directoryDTO );
        for ( Iterator< UIFormItem > iterator = uiFormItemList.iterator(); iterator.hasNext(); ) {
            UIFormItem uiFormItem = iterator.next();
            handleStatusKey( uiFormItem );
            handleThemeLabel( uiFormItem );
            handleRemovableFields( uiFormItem, iterator );
            handleLanguageOptions( uiFormItem, iterator );
            if ( UserDTO.UID_UI_COLUMN_NAME.equalsIgnoreCase( uiFormItem.getName() ) ) {
                uiFormItem.setReadonly( true );
                uiFormItem.setValue( ldapAuthRealm.getCaseSensitiveUserUidFromLdap( userUid, directoryDTO ) );
            }
            if ( PASS_KEY.equalsIgnoreCase( uiFormItem.getName() ) ) {
                iterator.remove();
            }
            if ( uiFormItem.getName().contains( "userDetails[0].email" ) ) {
                uiFormItem.setValue( ldapAuthRealm.getUserEmailFromUid( userUid, directoryDTO ) );
            }
            if ( CHANGABLE.equalsIgnoreCase( uiFormItem.getName() ) ) {
                iterator.remove();
            }
            if ( uiFormItem.getName().equalsIgnoreCase( "firstName" ) ) {
                uiFormItem.setValue( names[ 0 ] );
            } else if ( uiFormItem.getName().equalsIgnoreCase( "surName" ) ) {
                uiFormItem.setValue( names[ 1 ] );
            }
        }
        return GUIUtils.createFormFromItems( uiFormItemList );
    }

    /**
     * Prepare ui form for internal user map.
     *
     * @param userUid
     *         the user uid
     *
     * @return the map
     */
    private UIForm prepareUIFormForInternalUser( String userUid ) {
        List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( true, new UserDTO(), new UserDetail() );
        for ( Iterator< UIFormItem > iterator = uiFormItemList.iterator(); iterator.hasNext(); ) {
            UIFormItem uiFormItem = iterator.next();
            handleStatusKey( uiFormItem );
            handleThemeLabel( uiFormItem );
            handleRemovableFields( uiFormItem, iterator );
            handleLanguageOptions( uiFormItem, iterator );
            if ( UserDTO.UID_UI_COLUMN_NAME.equalsIgnoreCase( uiFormItem.getName() ) ) {
                uiFormItem.setValue( userUid );
            }
        }
        return GUIUtils.createFormFromItems( uiFormItemList );
    }

    /**
     * Handle status key.
     *
     * @param uiFormItem
     *         the ui form item
     */
    private void handleStatusKey( UIFormItem uiFormItem ) {
        if ( uiFormItem.getName().equals( STATUS_KEY ) ) {
            Map< String, String > map = new HashMap<>();
            if ( licenseManager.isTokenBasedLicenseExists() ) {
                map.put( ConstantsStatus.ACTIVE, ConstantsStatus.ACTIVE );
                map.put( ConstantsStatus.INACTIVE, ConstantsStatus.INACTIVE );
            } else {
                map.put( ConstantsStatus.INACTIVE, ConstantsStatus.INACTIVE );
                uiFormItem.setReadonly( true );
            }
            GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ), ConstantsStatus.INACTIVE,
                    false );
        }
    }

    /**
     * Handle theme label.
     *
     * @param uiFormItem
     *         the ui form item
     */
    private void handleThemeLabel( UIFormItem uiFormItem ) {
        if ( UserDTO.THEME_LABEL.equals( uiFormItem.getLabel() ) ) {
            List< SelectOptionsUI > options = new ArrayList<>();
            for ( UIThemeEnums themeEnum : UIThemeEnums.getAll() ) {
                options.add( new SelectOptionsUI( themeEnum.getId(), themeEnum.getName() ) );
            }
            ( ( SelectFormItem ) uiFormItem ).setOptions( options );
        }
    }

    /**
     * Handle removable fields.
     *
     * @param uiFormItem
     *         the ui form item
     * @param iterator
     *         the iterator
     */
    private void handleRemovableFields( UIFormItem uiFormItem, Iterator< UIFormItem > iterator ) {
        if ( uiFormItem.getName().equalsIgnoreCase( DIRECTORY ) || UserDTO.LABEL_GROUPS.equalsIgnoreCase( uiFormItem.getLabel() )
                || UserDTO.LOCATION_PREFERENCE_SELECTION_ID.equalsIgnoreCase( uiFormItem.getName() ) ) {
            iterator.remove();
        }
    }

    /**
     * Handle language options.
     *
     * @param uiFormItem
     *         the ui form item
     * @param iterator
     *         the iterator
     */
    private void handleLanguageOptions( UIFormItem uiFormItem, Iterator< UIFormItem > iterator ) {
        if ( uiFormItem.getName().contains( "language" ) ) {
            if ( PropertiesManager.hasTranslation() ) {
                List< SelectOptionsUI > options = new ArrayList<>();
                Map< String, String > languages = PropertiesManager.getRequiredlanguages();
                for ( Map.Entry< String, String > str : languages.entrySet() ) {
                    options.add( new SelectOptionsUI( str.getKey(), str.getValue() ) );
                }
                ( ( SelectFormItem ) uiFormItem ).setOptions( options );
            } else {
                iterator.remove();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editUserForm( String userId, UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isPermitted( entityManager, userId, SimuspaceFeaturesEnum.USERS.getId(), PermissionMatrixEnum.WRITE.getValue(),
                    Messages.NO_RIGHTS_TO_UPDATE.getKey(), USER );
            UserDTO user = getUserDTOById( entityManager, id );
            user.setPassword( null );
            var items = prepareUserEditForm( entityManager, userId, user );
            for ( var item : items ) {
                if ( item.getName().equals( UserDTO.UID_UI_COLUMN_NAME ) ) {
                    item.setReadonly( true );
                    break;
                }
            }
            return GUIUtils.createFormFromItems( items );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public UserDTO updateUserProperty( String userId, String propertyKey, String propertyValue, String currentUserId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !StringUtils.equals( userId, currentUserId ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OPERATION_ONLY_ALLOWED_FOR_CURRENT_USER.getKey() ) );
            }
            var userEntity = userDAO.findById( entityManager, UUID.fromString( userId ) );
            switch ( propertyKey.toLowerCase().trim() ) {
                case "language" -> updateLanguageInEntity( propertyValue, userEntity );
                case "theme" -> updateThemeInUserEntity( propertyValue, userEntity );
                default -> throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), propertyKey ) );
            }
            userEntity.setModifiedBy( userEntity );
            userEntity.setModifiedOn( new Date() );
            userDAO.update( entityManager, userEntity );
            return prepareUserModelFromUserEntityWithHiddenPasswd( entityManager, userEntity );
        } finally {
            entityManager.close();
        }
    }

    private static void updateThemeInUserEntity( String propertyValue, UserEntity userEntity ) {
        if ( Arrays.stream( UIThemeEnums.getAll() ).noneMatch( theme -> StringUtils.equalsIgnoreCase( theme.getId(), propertyValue ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), propertyValue ) );
        }
        userEntity.setTheme( propertyValue );
    }

    private static void updateLanguageInEntity( String propertyValue, UserEntity userEntity ) {
        if ( !PropertiesManager.getRequiredlanguages().containsKey( propertyValue ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), propertyValue ) );
        }
        userEntity.getUserDetails().forEach( detail -> detail.setLanguage( propertyValue ) );
    }

    /**
     * Gets user property field.
     *
     * @param propertyKey
     *         the property key
     * @param token
     *         the token
     *
     * @return the user property field
     */
    @Override
    public UIFormItem getUserPropertyField( String propertyKey, String token ) {
        return switch ( propertyKey.toLowerCase().trim() ) {
            case "language" -> UserDTO.getUserLanguageFormItem( TokenizedLicenseUtil.getUser( token ) );
            case "theme" -> UserDTO.getUserThemeFormItem( TokenizedLicenseUtil.getUser( token ) );
            default -> throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), propertyKey ) );
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editUserProfileFormSections( UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return GUIUtils.createFormFromItems( editUserProfileForm( entityManager, id ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editUserProfileForm( UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return GUIUtils.createFormFromItems( editUserProfileForm( entityManager, id ) );
        } finally {
            entityManager.close();
        }
    }

    private List< UIFormItem > editUserProfileForm( EntityManager entityManager, UUID id ) {
        UserDTO user = getUserDTOById( entityManager, id );
        return prepareUserProfileEditForm( prepareUserProfileDto( user ), user.getSusUserDirectoryDTO() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteUserBySelection( String actingUser, String selectionId, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            boolean featureAllowed = licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.USERS.getKey() );
            if ( featureAllowed ) {
                isPermitted( entityManager, actingUser, SimuspaceFeaturesEnum.USERS.getId(), PermissionMatrixEnum.DELETE.getValue(),
                        Messages.NO_RIGHTS_TO_DELETE.getKey(), USER );

                if ( mode.contentEquals( ConstantsMode.BULK ) ) {
                    FiltersDTO filtersDTO = contextMenuManager.getFilterBySelectionId( entityManager, selectionId );
                    List< Object > ids = filtersDTO.getItems();
                    for ( Object id : ids ) {
                        deleteUser( entityManager, actingUser, UUID.fromString( id.toString() ) );
                    }
                } else if ( mode.contentEquals( ConstantsMode.SINGLE ) ) {
                    return deleteUser( entityManager, actingUser, UUID.fromString( selectionId ) );
                } else {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), mode ) );
                }
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FEATURE_NOT_ALLOWED_TO_USER.getKey(),
                        SimuspaceFeaturesEnum.USERS.getKey() ) );
            }
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteUserBySelection( EntityManager entityManager, String actingUser, String selectionId, String mode ) {

        boolean featureAllowed = licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.USERS.getKey() );
        if ( featureAllowed ) {
            isPermitted( entityManager, actingUser, SimuspaceFeaturesEnum.USERS.getId(), PermissionMatrixEnum.DELETE.getValue(),
                    Messages.NO_RIGHTS_TO_DELETE.getKey(), USER );

            if ( mode.contentEquals( ConstantsMode.BULK ) ) {
                FiltersDTO filtersDTO = contextMenuManager.getFilterBySelectionId( entityManager, selectionId );
                List< Object > ids = filtersDTO.getItems();
                for ( Object id : ids ) {
                    deleteUser( entityManager, actingUser, UUID.fromString( id.toString() ) );
                }
            } else if ( mode.contentEquals( ConstantsMode.SINGLE ) ) {
                return deleteUser( entityManager, actingUser, UUID.fromString( selectionId ) );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), mode ) );
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FEATURE_NOT_ALLOWED_TO_USER.getKey(),
                    SimuspaceFeaturesEnum.USERS.getKey() ) );
        }
        return true;
    }

    /**
     * Delete user.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userId
     *         the directory id
     *
     * @return true, if successful
     */
    private boolean deleteUser( EntityManager entityManager, String userIdFromGeneralHeader, UUID userId ) {

        UserEntity userEntity = validateAndGetExistingUser( entityManager, userId );
        SuSUserDirectoryEntity suSUserDirectoryEntity = userEntity.getDirectory();
        userEntity.setDirectory( null );
        userEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForObjects(
                "Delete User : " + userEntity.getFirstName() + StringUtils.SPACE + userEntity.getSurName(),
                ConstantsDbOperationTypes.DELETED, userIdFromGeneralHeader, userEntity.getId().toString(),
                userEntity.getFirstName() + StringUtils.SPACE + userEntity.getSurName(), "User" ) );
        userDAO.update( entityManager, userEntity );
        userEntity.setAuditLogEntity( null );
        try {
            userDAO.delete( entityManager, userEntity );
        } catch ( Exception e ) {
            userEntity.setDirectory( suSUserDirectoryEntity );
            userDAO.merge( entityManager, userEntity );
            log.error( MessageBundleFactory.getMessage( Messages.USER_CANNOT_NOT_BE_DELETED.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_CANNOT_NOT_BE_DELETED.getKey() ) );
        }

        return true;
    }

    /**
     * Validate and get existing user.
     *
     * @param userId
     *         the id
     *
     * @return the user entity
     */
    private UserEntity validateAndGetExistingUser( EntityManager entityManager, UUID userId ) {
        UserEntity userEntity = userDAO.readUser( entityManager, userId );
        if ( userEntity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.REQUEST_ID_NOT_VALID.getKey(), userId ) );
        }
        checkAndRemoveDependencies( entityManager, userEntity );
        return userEntity;
    }

    /**
     * Check dependencies.
     *
     * @param userEntity
     *         the user entity
     */
    private void checkAndRemoveDependencies( EntityManager entityManager, UserEntity userEntity ) {
        List< SuSEntity > objectListByProperty = susDAO.getObjectListByProperty( entityManager, ConstantsDAO.OWNER, userEntity );
        if ( !objectListByProperty.isEmpty() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_CANNOT_NOT_BE_DELETED.getKey() ) );
        }

        List< GroupEntity > groupEntities = userGroupManager.getUserGroupsByUserId( entityManager, userEntity.getId() );

        for ( GroupEntity group : groupEntities ) {
            GroupEntity retGrp = userDAO.getGroupAndUsersByGroupId( entityManager, group.getId() );
            if ( !CollectionUtil.isEmpty( retGrp.getUsers() ) ) {
                retGrp.getUsers().remove( userEntity );
                userGroupDAO.saveOrUpdate( entityManager, retGrp );
            }
        }

        List< UserTokenEntity > userTokenList = userTokenDAO.getAllUserTokenEntityListByUserId( entityManager, userEntity.getId() );
        for ( UserTokenEntity userTokenEntity : userTokenList ) {
            userTokenDAO.deleteUserTokenEntity( entityManager, userTokenEntity );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< UserDTO > getAllUserTableSelection( String selectionId, String userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SelectionItemEntity > selectedUserIds = selectionManager.getUserSelectionsBySelectionIds( entityManager, selectionId,
                    filter );
            List< Object > notFoundObjects = new ArrayList<>();
            List< UserDTO > userDTOList = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedUserIds ) ) {
                for ( SelectionItemEntity user : selectedUserIds ) {
                    UserEntity userEntity = userDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( user.getItem() ) );
                    if ( null != userEntity ) {
                        UserDTO users = prepareUserModelFromUserEntity( entityManager, userEntity );
                        if ( users != null ) {
                            userDTOList.add( users );
                        }
                    } else {
                        notFoundObjects.add( user.getItem() );
                    }
                }
            } else {
                // selection is empty in case of select all hence get all users
                List< UserEntity > userList = userDAO.getAllFilteredRecords( entityManager, UserEntity.class, filter );

                if ( userList != null ) {
                    for ( UserEntity userEntity : userList ) {
                        UserDTO userDTO = prepareUserModelFromUserEntity( entityManager, userEntity );
                        if ( userDTO != null ) {
                            userDTOList.add( userDTO );
                        }
                    }
                }
            }
            FiltersDTO removeId = new FiltersDTO();
            removeId.setItems( notFoundObjects );
            selectionManager.removeSelectionItemsInExistingSelection( entityManager, selectionId, removeId );
            return PaginationUtil.constructFilteredResponse( filter, userDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserDTO > getAllUsersBySelectionId( String selectionId, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > selectedUserIds = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
            List< Object > notFoundObjects = new ArrayList<>();
            List< UserDTO > userDTOList = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedUserIds ) ) {
                for ( UUID id : selectedUserIds ) {
                    UserEntity userEntity = userDAO.getLatestNonDeletedObjectById( entityManager, id );
                    if ( null != userEntity ) {
                        UserDTO users = prepareUserModelFromUserEntity( entityManager, userEntity );
                        if ( users != null ) {
                            userDTOList.add( users );
                        }
                    } else {
                        notFoundObjects.add( id );
                    }
                }
            }
            FiltersDTO removeId = new FiltersDTO();
            removeId.setItems( notFoundObjects );
            selectionManager.removeSelectionItemsInExistingSelection( entityManager, selectionId, removeId );
            return userDTOList;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public UserEntity getUserEntityById( EntityManager entityManager, UUID id ) {
        return userDAO.getLatestNonDeletedObjectById( entityManager, id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void isPermitted( EntityManager entityManager, String userId, String id, String premissionType, String message, String name ) {
        log.debug( ">>isPermitted: userId: " + userId );
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + premissionType ) ) {
            throw new SusException( MessageBundleFactory.getMessage( message, name ) );
        }
        isRestricted( entityManager, userId, UUID.fromString( userId ) );
    }

    /**
     * Is restricted.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userId
     *         the user id
     */
    private void isRestricted( EntityManager entityManager, String userIdFromGeneralHeader, UUID userId ) {
        UserDTO userDTO = getUserById( entityManager, userIdFromGeneralHeader, userId );
        if ( userDTO != null && userDTO.getRestricted().equals( RESTRICTED_USER ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_RESTRICTED_TO_SYSTEM.getKey(), userDTO.getName() ) );
        }
    }

    @Override
    public boolean hasLocationPermission( EntityManager entityManager, String userIdFromGeneralHeader, String id, String permissionType ) {
        log.debug( ">>isPermitted: userIdFromGeneralHeader:" + userIdFromGeneralHeader );
        if ( !userIdFromGeneralHeader.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager,
                userIdFromGeneralHeader, id + ConstantsString.COLON + permissionType + ConstantsString.COLON + IS_LOCATION ) ) {
            return false;
        }
        return !userDAO.isRestrictedUser( entityManager, UUID.fromString( userIdFromGeneralHeader ) );
    }

    /**
     * Prepare UserDto From User Profile.
     *
     * @param userProfileDto
     *         the user profile dto
     *
     * @return UserDTO user dto
     */
    private UserDTO prepareUserDtoFromUserProfile( UserProfileDTO userProfileDto ) {
        UserDTO user = new UserDTO();

        user.setId( userProfileDto.getId() );
        if ( userProfileDto.getUserPasswordDto() != null ) {
            user.setPassword( userProfileDto.getUserPasswordDto().getNewPassword() );
        }
        user.setSurName( userProfileDto.getSurName() );
        user.setUserUid( userProfileDto.getUid() );
        user.setFirstName( userProfileDto.getFirstName() );
        user.setUserDetails( userProfileDto.getUserDetails() );
        user.setDescription( userProfileDto.getDescription() );
        user.setLocationPreferenceSelectionId( userProfileDto.getLocationPreferenceSelectionId() );
        user.setTheme( userProfileDto.getTheme() );

        return user;
    }

    /**
     * Common Checks For User For Updation of User/User Profile.
     *
     * @param entityManager
     *         the entity manager
     * @param notif
     *         the notif
     * @param user
     *         the user
     * @param userEntity
     *         the user entity
     */
    private void checksForUserNameUniqnessOnUpdation( EntityManager entityManager, Notification notif, UserDTO user,
            UserEntity userEntity ) {
        notif.addNotification( validateUserNameInDirectories( user ) );
        if ( !userEntity.getUserUid().equalsIgnoreCase( user.getUserUid() ) && userDAO.isUserExist( entityManager, user.getUserUid() ) ) {
            notif.addError( new Error( MessageBundleFactory.getMessage( Messages.USER_ALREADY_EXIST.getKey() ) ) );
        }
        if ( !userEntity.getUserUid().equalsIgnoreCase( user.getUserUid() ) ) {
            notif.addError( new Error( MessageBundleFactory.getMessage( Messages.UID_CANNOT_BE_CHANGED.getKey() ) ) );
        }
        user.getUserDetails().get( FIRST_INDEX ).setId( userEntity.getUserDetails().iterator().next().getId().toString() );
    }

    /**
     * A method for authenticating super user.
     *
     * @param user
     *         the user
     *
     * @return UserDTO user dto
     */
    private UserDTO authenticateSuperUser( UserDTO user ) {
        UserDTO userToReturn = null;
        String superUserName = SuperUserPropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUPER_USER_NAME_PROPERTY );
        String superUserPassword = SuperUserPropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUPER_USER_PASS_PROPERTY );
        String superUserStatus = SuperUserPropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUPER_USER_STATUS_PROPERTY );
        if ( user.getUserUid().equals( superUserName ) ) {
            if ( superUserStatus.trim().equalsIgnoreCase( ConstantsStatus.ACTIVE ) ) {
                if ( user.getPassword().equals( superUserPassword ) ) {
                    userToReturn = new UserDTO( ConstantsID.SUPER_USER_ID + ConstantsString.EMPTY_STRING, superUserName, superUserStatus );
                    userToReturn.setFirstName( ConstantsUserProfile.SUPER );
                    userToReturn.setSurName( ConstantsUserProfile.USER );
                    userToReturn.setName( ConstantsUserProfile.SUPER_USER );
                    return userToReturn;
                } else {
                    throw new IncorrectCredentialsException(
                            MessageBundleFactory.getMessage( Messages.INVALID_LOGIN_CREDENTIALS.getKey() ) );
                }
            } else {
                throw new LockedAccountException( MessageBundleFactory.getMessage( Messages.USER_ACCOUNT_IS_INACTIVE.getKey() ) );
            }
        }
        return userToReturn;
    }

    /**
     * Is User Exist In Ldap Directory.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     * @param attributes
     *         of directory
     *
     * @return boolean boolean
     */
    private boolean isUserExistInLdap( EntityManager entityManager, UsernamePasswordToken token, SusUserDirectoryAttributeDTO attributes ) {
        AuthenticationInfo info = null;

        if ( ldapAuthRealm.isLdapConnectionEstablished( attributes ) ) {
            try {
                info = ldapAuthRealm.doGetAuthenticationInfo( token );
                resetFailedLdapAttempts( entityManager, token.getUsername() );
            } catch ( AuthenticationException e ) {
                updateFailedLdapAttempt( entityManager, token.getUsername() );
                log.error( e.getMessage(), e );
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_INVALID_CREDENTIALS.getKey() ) );
        }

        return info != null && info.getCredentials() != null;
    }

    /**
     * Reset failed ldap attempts.
     *
     * @param userUid
     *         the user uid
     */
    private void resetFailedLdapAttempts( String userUid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            resetFailedLdapAttempts( entityManager, userUid );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Reset failed ldap attempts.
     *
     * @param entityManager
     *         the entity manager
     * @param userUid
     *         the user uid
     */
    private void resetFailedLdapAttempts( EntityManager entityManager, String userUid ) {
        var attempts = userLoginAttemptDAO.getFailedLoginAttempts( entityManager, userUid );
        if ( attempts == null ) {
            attempts = userLoginAttemptDAO.saveFailedLoginAttempt( entityManager, userUid );
        }
        if ( attempts.getLdapAttempts() == null || attempts.getLdapAttempts() > ConstantsInteger.INTEGER_VALUE_ZERO ) {
            attempts.setLdapAttempts( ConstantsInteger.INTEGER_VALUE_ZERO );
            userLoginAttemptDAO.updateFailedLoginAttempt( entityManager, attempts );
        }
    }

    /**
     * Is User Exist In AD Directory.
     *
     * @param token
     *         the token
     * @param attributes
     *         the attributes
     *
     * @return boolean boolean
     */
    private boolean isUserExistInActiveDirectory( UsernamePasswordToken token, SusUserDirectoryAttributeDTO attributes ) {
        AuthenticationInfo info = null;
        // check in AD
        if ( adCustomRealm.isActiveDirectoryConnectionEstablished( attributes ) ) {
            try {
                info = adCustomRealm.doGetAuthenticationInfo( token );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_INVALID_CREDENTIALS.getKey() ) );
        }

        return info != null && info.getCredentials() != null;

    }

    /**
     * Method for authenticating user credentials.
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     * @param user
     *         the user
     *
     * @return UserDTO user dto
     */
    private UserDTO authenticateUser( EntityManager entityManager, UserEntity userEntity, UserDTO user ) {
        UserDTO userDTOToReturn = null;

        if ( user.getSusUserDirectoryDTO().getType().equals( ConstantsUserDirectories.LDAP_DIRECTORY ) || user.getSusUserDirectoryDTO()
                .getType().equals( ConstantsUserDirectories.ACTIVE_DIRECTORY ) ) {
            if ( !userEntity.isStatus() ) {
                throw new LockedAccountException( MessageBundleFactory.getMessage( Messages.USER_ACCOUNT_IS_INACTIVE.getKey() ) );
            }

            checkUserLicense( entityManager, userEntity );
            return prepareUserModelFromUserEntity( entityManager, userEntity );
        }

        if ( userEntity.isStatus() ) {
            if ( user.getPassword().equals( userEntity.getPassword() ) && user.getUserUid().equals( userEntity.getUserUid() ) ) {
                userDTOToReturn = prepareUserModelFromUserEntity( entityManager, userEntity );
                LoginAttemptEntity loginAttemptEntity = userLoginAttemptDAO.getFailedLoginAttempts( entityManager, user.getUserUid() );
                if ( loginAttemptEntity != null ) {
                    loginAttemptEntity.setAttempts( ConstantsInteger.INTEGER_VALUE_ZERO );
                    userLoginAttemptDAO.updateFailedLoginAttempt( entityManager, prepareLoginAttemptEntity( loginAttemptEntity ) );
                }

                checkUserLicense( entityManager, userEntity );
                return userDTOToReturn;
            } else {
                manageFailedLoginAttempts( entityManager, user.getUserUid() );
            }
        } else {
            LoginAttemptEntity loginAttemptEntity = userLoginAttemptDAO.getFailedLoginAttempts( entityManager, user.getUserUid() );
            if ( loginAttemptEntity != null && Boolean.TRUE.equals( loginAttemptEntity.getLocked() ) ) {
                updateUnlockTime( entityManager, loginAttemptEntity );
                throw new ExcessiveAttemptsException(
                        MessageBundleFactory.getMessage( Messages.TO_MANY_ATTEMPTS_TRY_AGAIN_IN_N_MINUTES.getKey(),
                                PropertiesManager.getAccountLockTimeForFailedAttempts() ) );
            }
            throw new LockedAccountException( MessageBundleFactory.getMessage( Messages.USER_ACCOUNT_IS_INACTIVE.getKey() ) );
        }
        return userDTOToReturn;
    }

    /**
     * Check user license.
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     */
    private void checkUserLicense( EntityManager entityManager, UserEntity userEntity ) {
        if ( !licenseManager.isFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.USERS.getKey(),
                userEntity.getId().toString() ) ) {
            throw new IncorrectCredentialsException(
                    MessageBundleFactory.getMessage( Messages.CANNOT_VERIFY_LICENSE.getKey(), SimuspaceFeaturesEnum.USERS.getKey() ) );
        }
    }

    /**
     * Update unlock time login attempt entity.
     *
     * @param entityManager
     *         the entity manager
     * @param loginAttemptEntity
     *         the login attempt entity
     */
    private void updateUnlockTime( EntityManager entityManager, LoginAttemptEntity loginAttemptEntity ) {
        if ( loginAttemptEntity.getLockTime() != null && loginAttemptEntity.getUnlockTime() != null ) {
            loginAttemptEntity.setUnlockTime( DateUtils.getNMinutesLaterDate( new Date(),
                    Integer.parseInt( PropertiesManager.getAccountLockTimeForFailedAttempts() ) ) ); // reset the 15 minutes countdown
            userLoginAttemptDAO.updateFailedLoginAttempt( entityManager, prepareLoginAttemptEntity( loginAttemptEntity ) );
        }
    }

    /**
     * A method used for expiring tokens for deactivated user.
     *
     * @param entityManager
     *         the entity manager
     * @param userTokenEntityList
     *         the user token entity list
     */
    private void expireTokensForDeactivatedUser( EntityManager entityManager, List< UserTokenEntity > userTokenEntityList ) {
        for ( UserTokenEntity userTokenEntity : userTokenEntityList ) {
            userTokenEntity.setExpired( true );
            userTokenDAO.updateUserTokenEntity( entityManager, userTokenEntity );
        }
    }

    /**
     * For preparing login attempt entity.
     *
     * @param loginAttemptEntity
     *         the login attempt entity
     *
     * @return LoginAttemptEntity login attempt entity
     */
    private LoginAttemptEntity prepareLoginAttemptEntity( LoginAttemptEntity loginAttemptEntity ) {
        if ( loginAttemptEntity.getUuid() == null ) {
            loginAttemptEntity.setUuid( UUID.randomUUID() );
        }
        if ( loginAttemptEntity.getAttempts() == null ) {
            loginAttemptEntity.setAttempts( ConstantsInteger.INTEGER_VALUE_ZERO );
        }
        if ( loginAttemptEntity.getLdapAttempts() == null ) {
            loginAttemptEntity.setLdapAttempts( ConstantsInteger.INTEGER_VALUE_ZERO );
        }
        loginAttemptEntity.setLastMofied( new Date() );
        return loginAttemptEntity;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO prepareUserModelFromUserEntity( UserEntity userEntity ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return prepareUserModelFromUserEntity( entityManager, userEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO prepareUserModelFromUserEntity( EntityManager entityManager, UserEntity userEntity ) {
        UserDTO user = new UserDTO();
        if ( userEntity != null ) {
            user.setId( userEntity.getId().toString() );

            user.setUserUid( userEntity.getUserUid() );
            user.setStatus( userEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
            user.setRestricted( Boolean.TRUE.equals( userEntity.isRestricted() ) ? ConstantsStatus.YES : ConstantsStatus.NO );
            user.setFirstName( userEntity.getFirstName() );
            if ( userEntity.getSurName() != null ) {
                user.setSurName( userEntity.getSurName() );
            } else {
                user.setSurName( "" );
            }
            user.setName( user.getName() );
            user.setDescription( userEntity.getDescription() );
            user.setChangable( userEntity.isChangeable() );
            user.setPassword( "******" );
            user.setLocationPreferenceSelectionId( userEntity.getLocationPreferenceSelectionId() );
            user.setTheme( userEntity.getTheme() == null ? UIThemeEnums.SYSTEM.getName() : userEntity.getTheme() );
            prepareUserModelLazyAttributesFromUserEntity( entityManager, user, userEntity, Boolean.TRUE );

        }
        return user;
    }

    /**
     * Prepare user model lazy attributes from user entity.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param userEntity
     *         the user entity
     * @param prepareDirectory
     *         the prepare directory
     */
    private void prepareUserModelLazyAttributesFromUserEntity( EntityManager entityManager, UserDTO user, UserEntity userEntity,
            boolean prepareDirectory ) {

        // refresh userEntity to initialize lazy loaded objects
        userEntity = getUserEntityById( entityManager, userEntity.getId() );

        userProperties( userEntity, user );

        // userDetails
        List< UserDetailEntity > userDetailList = new ArrayList<>();
        UserDetailEntity detail = null;
        if ( CollectionUtils.isNotEmpty( userEntity.getUserDetails() ) ) {
            detail = userEntity.getUserDetails().stream().findFirst().orElse( null );
        }

        if ( detail != null ) {
            userDetailList.add( detail );
        }
        user.setUserDetails( prepareUserDetailsFromUser( userDetailList ) );

        // userDocument
        if ( null != userEntity.getDocument() ) {
            DocumentDTO documentDTO = documentManager.prepareDocumentDTO( userEntity.getDocument() );
            String url = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + documentDTO.getPath() + File.separator + documentDTO.getName();
            documentDTO.setUrl( url );
            user.setProfilePhoto( documentDTO );
        }

        // userGroups
        if ( CollectionUtil.isNotEmpty( userEntity.getGroups() ) ) {
            List< SuSUserGroupDTO > groupEntities = prepareUserGroupDTO( new HashSet<>( userEntity.getGroups() ) );
            user.setGroups( groupEntities );
            user.setGroupsCount( groupEntities.size() );
        }

        // userDirectory
        if ( prepareDirectory ) {
            user.setSusUserDirectoryDTO( directoryManager.prepareDirectoryModelFromEntityWithoutUsers( userEntity.getDirectory(), true ) );

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO prepareUserModelFromUserEntityWitoutDirectory( EntityManager entityManager, UserEntity userEntity ) {
        UserDTO user = new UserDTO();
        if ( userEntity != null ) {
            user.setId( userEntity.getId().toString() );

            user.setUserUid( userEntity.getUserUid() );
            user.setStatus( userEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
            user.setRestricted( Boolean.TRUE.equals( userEntity.isRestricted() ) ? ConstantsStatus.YES : ConstantsStatus.NO );
            user.setFirstName( userEntity.getFirstName() );
            if ( userEntity.getSurName() != null ) {
                user.setSurName( userEntity.getSurName() );
            } else {
                user.setSurName( "" );
            }
            user.setName( user.getName() );
            user.setDescription( userEntity.getDescription() );
            user.setChangable( userEntity.isChangeable() );
            user.setPassword( "******" );
            user.setTheme( userEntity.getTheme() );
            prepareUserModelLazyAttributesFromUserEntity( entityManager, user, userEntity, Boolean.FALSE );
            user.setLocationPreferenceSelectionId( userEntity.getLocationPreferenceSelectionId() );

        }
        return user;
    }

    /**
     * Prepare user model from user entity with hidden passwd user dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     *
     * @return the user dto
     */
    private UserDTO prepareUserModelFromUserEntityWithHiddenPasswd( EntityManager entityManager, UserEntity userEntity ) {
        UserDTO user = new UserDTO();
        if ( userEntity != null ) {
            user.setId( userEntity.getId().toString() );

            user.setUserUid( userEntity.getUserUid() );
            user.setStatus( userEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
            user.setRestricted( Boolean.TRUE.equals( userEntity.isRestricted() ) ? ConstantsStatus.YES : ConstantsStatus.NO );
            user.setFirstName( userEntity.getFirstName() );
            if ( userEntity.getSurName() != null ) {
                user.setSurName( userEntity.getSurName() );
            } else {
                user.setSurName( "" );
            }
            user.setName( user.getName() );
            user.setDescription( userEntity.getDescription() );
            userProperties( userEntity, user );
            user.setSusUserDirectoryDTO( directoryManager.prepareDirectoryModelFromEntityForUserModelDTO( userEntity.getDirectory() ) );
            user.setChangable( userEntity.isChangeable() );
            user.setPassword( "******" );
            List< UserDetailEntity > userDetailList = new ArrayList<>();

            UserDetailEntity detail = userDetailDAO.getUserDetailById( entityManager, userEntity.getId() );

            if ( detail != null ) {
                userDetailList.add( detail );
            }

            List< GroupEntity > groupEntities = userGroupManager.getUserGroupsByUserId( entityManager, userEntity.getId() );
            if ( CollectionUtil.isNotEmpty( groupEntities ) ) {
                user.setGroups( prepareUserGroupDTO( new HashSet<>( groupEntities ) ) );
                user.setGroupsCount( groupEntities.size() );
            } else {
                user.setGroups( new ArrayList<>() );
                user.setGroupsCount( ConstantsInteger.INTEGER_VALUE_ZERO );
            }

            user.setUserDetails( prepareUserDetailsFromUser( userDetailList ) );
            if ( null != userEntity.getDocument() ) {
                DocumentDTO documentDTO = documentManager.prepareDocumentDTO( userEntity.getDocument() );
                String url =
                        CommonUtils.getBaseUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_BASE_URL ) )
                                + File.separator + ConstantsString.STATIC_PATH + documentDTO.getPath() + File.separator
                                + documentDTO.getName();
                documentDTO.setUrl( url );
                user.setProfilePhoto( documentDTO );
            }
            user.setLocationPreferenceSelectionId( userEntity.getLocationPreferenceSelectionId() );

        }
        return user;
    }

    /**
     * User properties.
     *
     * @param userEntity
     *         the user entity
     * @param user
     *         the user
     */
    private void userProperties( UserEntity userEntity, UserDTO user ) {
        if ( null != userEntity.getCreatedBy() ) {
            user.setCreatedBy( setUserCommonFields( userEntity.getCreatedBy() ) );
        } else {
            // To avoid undified error in FE
            user.setCreatedBy( new UserDTO() );
        }
        if ( null != userEntity.getModifiedBy() ) {
            user.setModifiedBy( setUserCommonFields( userEntity.getModifiedBy() ) );
        } else {
            // To avoid undified error in FE
            user.setModifiedBy( new UserDTO() );
        }
        if ( null != userEntity.getCreatedOn() ) {
            user.setCreatedOn( DateFormatStandard.format( userEntity.getCreatedOn() ) );
        }
        if ( null != userEntity.getModifiedOn() ) {
            user.setModifiedOn( DateFormatStandard.format( userEntity.getModifiedOn() ) );
        }
    }

    /**
     * Sets the user common fields.
     *
     * @param userEntity
     *         the user entity
     *
     * @return the user DTO
     */
    private UserDTO setUserCommonFields( UserEntity userEntity ) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId( userEntity.getId().toString() );
        userDTO.setUserUid( userEntity.getUserUid() );
        userDTO.setFirstName( userEntity.getFirstName() );
        userDTO.setSurName( userEntity.getSurName() );
        return userDTO;
    }

    /**
     * Prepare User Groups Attached.
     *
     * @param groups
     *         the groups
     *
     * @return List
     */
    private List< SuSUserGroupDTO > prepareUserGroupDTO( Set< GroupEntity > groups ) {
        List< SuSUserGroupDTO > list = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( groups ) ) {
            for ( GroupEntity grp : groups ) {
                SuSUserGroupDTO dto = new SuSUserGroupDTO();
                dto.setId( grp.getId() );
                dto.setName( grp.getName() );
                dto.setStatus( Boolean.TRUE.equals( grp.getStatus() ) ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
                list.add( dto );
            }
        }
        return list;
    }

    /**
     * For preparing user entity object from user model.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param userEntity
     *         the user entity
     *
     * @return userEntity user entity
     */
    private UserEntity prepareUserEntityFromUserModel( EntityManager entityManager, UserDTO user, UserEntity userEntity ) {
        if ( user.getId() == null ) {
            userEntity.setId( UUID.randomUUID() );
            userEntity.setCreatedOn( new Date() );
        } else {
            userEntity.setId( UUID.fromString( user.getId() ) );
            userEntity.setModifiedOn( new Date() );
        }
        userEntity.setDescription( user.getDescription() );
        if ( user.getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE ) && !licenseManager.isTokenBasedLicenseExists( entityManager ) ) {
            licenseManager.isLicenseAddedAgainstUserForModule( entityManager, SimuspaceFeaturesEnum.USERS.getKey(), user.getId() );
        }
        userEntity.setFirstName( user.getFirstName() );
        userEntity.setSurName( user.getSurName() );
        userEntity.setUserUid( user.getUserUid() );
        userEntity.setStatus( user.getStatus().equals( ConstantsStatus.ACTIVE ) );
        if ( ConstantsStatus.YES.equals( user.getRestricted() ) ) {
            userEntity.setRestricted( true );
        } else {
            userEntity.setRestricted( user.getStatus().equals( ConstantsStatus.YES ) );
        }
        userEntity.setDirectory( directoryManager.readDirectory( entityManager, user.getSusUserDirectoryDTO().getId() ) );
        userEntity.setChangeable( user.isChangable() );
        userEntity.setUserDetails( prepareUserDetailsFromUser( user, userEntity ) );
        userEntity.setLocationPreferenceSelectionId( user.getLocationPreferenceSelectionId() );
        userEntity.setTheme( user.getTheme() );

        return userEntity;
    }

    /**
     * Method to set Encrypted Password for user.
     *
     * @param userDtoPwd
     *         the user dto pwd
     * @param userEntityPwd
     *         the user entity pwd
     *
     * @return the encrypted password for user
     */
    private String getEncryptedPasswordForUser( String userDtoPwd, String userEntityPwd ) {
        String password = null;

        if ( StringUtils.isNotEmpty( userDtoPwd ) ) {
            var msg = ValidationUtils.passwordValidation( userDtoPwd, MessageBundleFactory.getMessage( UserDTO.PASS_LABEL ) );
            if ( msg != null ) {
                //throw error if password from client fails validation
                throw new SusException( msg );
            }
            if ( StringUtils.isNotEmpty( userEntityPwd ) ) {
                // for update case, if old password =new password no need to change
                // else save new encryped pwd
                if ( userEntityPwd.contentEquals( userDtoPwd ) ) {
                    password = userDtoPwd;
                } else {
                    password = FileUtils.getSha256CheckSum( userDtoPwd );
                }

            } else {
                // create user save new encrypted pwd
                password = FileUtils.getSha256CheckSum( userDtoPwd );
            }
        }
        return password;
    }

    /**
     * Prepare User Groups.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param userEntity
     *         the user entity
     *
     * @return Set<GroupEntity> set
     */
    private Set< GroupEntity > prepareUserGroups( EntityManager entityManager, UserDTO user, UserEntity userEntity ) {
        Set< GroupEntity > groupList = new HashSet<>();
        if ( CollectionUtil.isNotEmpty( user.getGroups() ) ) {
            for ( SuSUserGroupDTO group : user.getGroups() ) {
                GroupEntity returnGroup = userDAO.getGroupAndUsersByGroupId( entityManager, group.getId() );
                if ( returnGroup != null ) {
                    if ( group.getId().equals( returnGroup.getId() ) ) {
                        returnGroup.getUsers().add( userEntity );
                    } else {
                        returnGroup.getUsers().remove( userEntity );
                    }
                    groupList.add( returnGroup );
                }
            }
        } else if ( StringUtils.isNotEmpty( user.getId() ) ) {
            // remove from all groups
            attachGroupToUser( entityManager, user, userEntity, groupList );
        }
        return groupList;
    }

    /**
     * Attached Groups to user.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param userEntity
     *         the user entity
     * @param groupList
     *         the group list
     */
    private void attachGroupToUser( EntityManager entityManager, UserDTO user, UserEntity userEntity, Set< GroupEntity > groupList ) {
        List< GroupEntity > list = userDAO.getGroupsByUserId( entityManager, user.getId() );

        if ( CollectionUtil.isNotEmpty( list ) ) {
            for ( GroupEntity group : list ) {
                GroupEntity retGrp = userDAO.getGroupAndUsersByGroupId( entityManager, group.getId() );
                retGrp.getUsers().remove( userEntity );

                groupList.add( retGrp );

            }
        }
    }

    /**
     * For preparing userDTO detail entity object from userDTO model.
     *
     * @param userDTO
     *         the userDTO
     * @param userEntity
     *         the userDTO entity
     *
     * @return userEntity
     */
    private Set< UserDetailEntity > prepareUserDetailsFromUser( UserDTO userDTO, UserEntity userEntity ) {
        Set< UserDetailEntity > returnList = new HashSet<>();
        if ( CollectionUtil.isNotEmpty( userDTO.getUserDetails() ) ) {
            for ( UserDetail userDetail : userDTO.getUserDetails() ) {
                UserDetailEntity entity = getMatchingUserDetailFromUserEntity( userEntity.getUserDetails(), userDetail );
                if ( userDetail.getId() != null && entity.getId() == null ) {
                    entity.setId( UUID.fromString( userDetail.getId() ) );
                } else if ( entity.getId() == null ) {
                    entity.setId( UUID.randomUUID() );
                }
                entity.setUserEntity( userEntity );
                entity.setContacts( userDetail.getContacts() );
                entity.setDesignation( userDetail.getDesignation() );
                entity.setEmail( userDetail.getEmail() );
                entity.setDepartment( userDetail.getDepartment() );
                entity.setDocumentEntity( userEntity.getDocument() );
                if ( userDetail.getLanguage() != null ) {
                    entity.setLanguage( userDetail.getLanguage() );
                }
                returnList.add( entity );
            }
        }
        return returnList;
    }

    /**
     * Gets matching user detail from user entity.
     *
     * @param userDetails
     *         the user details
     * @param userDetail
     *         the user detail
     *
     * @return the matching user detail from user entity
     */
    private UserDetailEntity getMatchingUserDetailFromUserEntity( Set< UserDetailEntity > userDetails, UserDetail userDetail ) {
        return CollectionUtils.isNotEmpty( userDetails ) ? userDetails.stream()
                .filter( userDetailEntity -> StringUtils.equals( userDetail.getId(), String.valueOf( userDetailEntity.getId() ) ) )
                .findFirst().orElse( new UserDetailEntity() ) : new UserDetailEntity();
    }

    /**
     * Prepare User Details From User.
     *
     * @param userDetailList
     *         the user detail list
     *
     * @return List<UserDetail> list
     */
    private List< UserDetail > prepareUserDetailsFromUser( List< UserDetailEntity > userDetailList ) {
        List< UserDetail > returnList = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( userDetailList ) ) {
            UserDetail userDetailDTO;
            for ( UserDetailEntity userDetailEntity : userDetailList ) {
                userDetailDTO = new UserDetail( userDetailEntity.getId() != null ? userDetailEntity.getId().toString() : null,
                        userDetailEntity.getDesignation(), userDetailEntity.getContacts(), userDetailEntity.getEmail(),
                        userDetailEntity.getDepartment() );

                if ( userDetailEntity.getLanguage() != null ) {
                    userDetailDTO.setLanguage( userDetailEntity.getLanguage() );
                }
                returnList.add( userDetailDTO );
            }
        }
        return returnList;
    }

    /**
     * Prepare LanguageDTO From LanguageEntity.
     *
     * @param languageEntity
     *         the language entity
     *
     * @return LanguageDTO language dto
     */
    private LanguageDTO prepareLanguageDTOFromLanguageEntity( LanguageEntity languageEntity ) {
        LanguageDTO languageDTO = new LanguageDTO();
        languageDTO.setId( languageEntity.getId().toString() );
        languageDTO.setName( languageEntity.getName() );
        return languageDTO;
    }

    /**
     * To Check if user exist in directory.
     *
     * @param user
     *         the user
     *
     * @return Notification notification
     */
    private Notification validateUserNameInDirectories( UserDTO user ) {
        Notification notif = new Notification();

        if ( !isUserNameAuthenticatedViaDirectoryParams( user ) ) {
            notif.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.USER_DOES_NOT_EXIST_IN_DIRECTORY.getKey(), user.getUserUid() ) ) );
        }

        return notif;

    }

    /**
     * Check if User Authenticated Via Directory Params for user.
     *
     * @param user
     *         the user
     *
     * @return boolean boolean
     */
    private boolean isUserNameAuthenticatedViaDirectoryParams( UserDTO user ) {

        // check in ldap

        SusUserDirectoryAttributeDTO attributes = user.getSusUserDirectoryDTO().getUserDirectoryAttribute();

        if ( user.getSusUserDirectoryDTO().getType().equalsIgnoreCase( ConstantsUserDirectories.LDAP_DIRECTORY )
                && ldapAuthRealm.isLdapConnectionEstablished( attributes ) ) {

            return ldapAuthRealm.lookUpUserUidInLdap( user.getUserUid() );

        } else if ( user.getSusUserDirectoryDTO().getType().equalsIgnoreCase( ConstantsUserDirectories.ACTIVE_DIRECTORY )
                && adCustomRealm.isActiveDirectoryConnectionEstablished( attributes ) ) {

            return adCustomRealm.lookUpUserUidInAD( user.getUserUid() );

        } else {
            // for other directories no need to Authenticate
            return true;

        }

    }

    /**
     * Prepare UserProfileDto from UserDTO.
     *
     * @param user
     *         the user
     *
     * @return UserProfileDTO user profile dto
     */
    private UserProfileDTO prepareUserProfileDto( UserDTO user ) {
        UserProfileDTO profileDto = new UserProfileDTO();
        profileDto.setId( user.getId() );
        profileDto.setType( user.getType() );
        profileDto.setUid( user.getUserUid() );
        profileDto.setFirstName( user.getFirstName() );
        profileDto.setSurName( user.getSurName() );
        profileDto.setDescription( user.getDescription() );
        UserPasswordDTO pwd = new UserPasswordDTO();

        ImageView agentDTO = new ImageView();
        List< DocumentDTO > browser = new ArrayList<>();
        if ( user.getProfilePhoto() != null ) {
            browser.add( user.getProfilePhoto() );
            agentDTO.setBrowser( browser );
            profileDto.setProfilePhoto( agentDTO );
        }

        profileDto.setUserPasswordDto( pwd );
        profileDto.setUserDetails( user.getUserDetails() );
        profileDto.setLocationPreferenceSelectionId( user.getLocationPreferenceSelectionId() );
        profileDto.setTheme( user.getTheme() );
        profileDto.setCreatedBy( user.getCreatedBy() );
        profileDto.setCreatedOn( user.getCreatedOn() );
        profileDto.setModifiedBy( user.getModifiedBy() );
        profileDto.setModifiedOn( user.getModifiedOn() );
        profileDto.setRestricted( user.getRestricted() );
        profileDto.setStatus( user.getStatus() );
        profileDto.setSusUserDirectoryDTO( user.getSusUserDirectoryDTO() );

        return profileDto;
    }

    /**
     * Get USer With All Details.
     *
     * @param id
     *         the id
     *
     * @return UserDTO user dto by id
     */
    private UserDTO getUserDTOById( EntityManager entityManager, UUID id ) {
        UserEntity entityToEdit = userDAO.getUserEntityById( entityManager, id );
        if ( entityToEdit == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_NOT_FOUND_WITH_ID.getKey(), id ) );
        }
        UserDTO user = prepareUserModelFromUserEntity( entityManager, entityToEdit );
        if ( CollectionUtil.isNotEmpty( entityToEdit.getGroups() ) ) {
            List< SuSUserGroupDTO > groupEntities = prepareUserGroupDTO( entityToEdit.getGroups() );
            user.setGroups( groupEntities );
            user.setGroupsCount( groupEntities.size() );
        }
        return user;
    }

    /**
     * Prepare User Edit Form For Admin.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     * @param user
     *         the user
     *
     * @return List<UIFormItem> list
     */
    private List< UIFormItem > prepareUserEditForm( EntityManager entityManager, String userId, UserDTO user ) {
        List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( false, user, user.getUserDetails().get( FIRST_INDEX ) );
        SuSUserDirectoryDTO ud = directoryManager.readDirectory( entityManager, user.getSusUserDirectoryDTO().getId(), false );
        for ( Iterator< UIFormItem > iterator = uiFormItemList.iterator(); iterator.hasNext(); ) {
            UIFormItem uiFormItem = iterator.next();
            if ( uiFormItem.getName().equals( STATUS_KEY ) ) {
                GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getStatusSelectObjectOptionsForUser(),
                        user.getStatus(), false );
            }

            if ( uiFormItem.getType().equalsIgnoreCase( ConstantsDocument.DOCUMENT_TYPE_IMAGE ) ) {
                ImageView agentDTO = new ImageView();
                List< DocumentDTO > browser = new ArrayList<>();
                if ( user.getProfilePhoto() != null ) {
                    browser.add( user.getProfilePhoto() );
                    agentDTO.setBrowser( browser );
                    uiFormItem.setValue( agentDTO );
                }
                uiFormItem.setReadonly( true );
            }

            if ( UserDTO.THEME_LABEL.equalsIgnoreCase( uiFormItem.getName() ) ) {
                List< SelectOptionsUI > options = new ArrayList<>();
                for ( UIThemeEnums themeEnum : UIThemeEnums.getAll() ) {
                    options.add( new SelectOptionsUI( themeEnum.getId(), themeEnum.getName() ) );
                }
                ( ( SelectFormItem ) uiFormItem ).setOptions( options );
            }

            if ( uiFormItem.getName().equalsIgnoreCase( DIRECTORY ) || CHANGABLE.equalsIgnoreCase( uiFormItem.getName() ) ) {
                iterator.remove();
            }

            if ( uiFormItem.getName().contains( "language" ) ) {
                if ( PropertiesManager.hasTranslation() ) {
                    List< SelectOptionsUI > options = new ArrayList<>();
                    Map< String, String > languages = PropertiesManager.getRequiredlanguages();
                    for ( String str : languages.keySet() ) {
                        options.add( new SelectOptionsUI( str, languages.get( str ) ) );
                    }
                    ( ( SelectFormItem ) uiFormItem ).setOptions( options );
                    ( ( SelectFormItem ) uiFormItem ).setTooltip( "Relogin required for translation changes" );
                } else {
                    iterator.remove();
                }
            }

            if ( ud.getType().equals( ConstantsUserDirectories.INTERNAL_DIRECTORY ) ) {
                if ( LDAP_DIR_FIRST_NAME.equalsIgnoreCase( uiFormItem.getName() ) || LDAP_DIR_SURNAME.equalsIgnoreCase(
                        uiFormItem.getName() ) ) {
                    iterator.remove();
                }
                if ( PASS_KEY.equalsIgnoreCase( uiFormItem.getName() ) ) {
                    if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
                        iterator.remove();
                    } else {
                        uiFormItem.getRules().put( REQUIRED, false );
                        uiFormItem.getMessages().put( REQUIRED, null );
                        uiFormItem.setLabel( MessageBundleFactory.getMessage( "3000320x4" ) );
                    }
                }
            } else { // remove extra fields for ldap and active directories users
                if ( uiFormItem.getName().equalsIgnoreCase( "firstName" ) || uiFormItem.getName().equalsIgnoreCase( "surName" )
                        || PASS_KEY.equalsIgnoreCase( uiFormItem.getName() ) ) {
                    iterator.remove();
                }
                if ( "userDetails[].email".equalsIgnoreCase( uiFormItem.getName() ) ) {
                    uiFormItem.setReadonly( true );
                }
            }
            if ( UserDTO.LOCATION_PREFERENCE_SELECTION_ID.equalsIgnoreCase( uiFormItem.getName() ) ) {
                iterator.remove();
            }
            // fill groups combo
            if ( UserDTO.LABEL_GROUPS.equalsIgnoreCase( uiFormItem.getLabel() ) ) {
                // get groups with user id
                List< SuSUserGroupDTO > groups = user.getGroups();
                List< SelectOptionsUI > groupList = prepareSelectItem( userDAO.getAllGroups( entityManager ) );
                GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, groupList, prepareSelectedGroups( groups ), true );
            }
        }
        return uiFormItemList;
    }

    /**
     * Prepare User Profile Edit Form.
     *
     * @param profileDTO
     *         the profileDTO
     * @param suSUserDirectoryDTO
     *         the su S profileDTO directory DTO
     *
     * @return List<UIFormItem> list
     */
    private List< UIFormItem > prepareUserProfileEditForm( UserProfileDTO profileDTO, SuSUserDirectoryDTO suSUserDirectoryDTO ) {

        if ( profileDTO.getId().equals( ConstantsID.SUPER_USER_ID ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.EDITING_NOT_ALLOWED_ON_SU.getKey(), SimuspaceFeaturesEnum.USERS.getKey() ) );
        } else {
            List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( false, profileDTO, profileDTO.getUserDetails().get( FIRST_INDEX ),
                    profileDTO.getUserPasswordDto() );
            TableFormItem locationTable = null;
            for ( Iterator< UIFormItem > iterator = uiFormItemList.iterator(); iterator.hasNext(); ) {
                UIFormItem uiFormItem = iterator.next();

                if ( !suSUserDirectoryDTO.getType().equalsIgnoreCase( ConstantsUserDirectories.INTERNAL_DIRECTORY ) && (
                        uiFormItem.getLabel().equalsIgnoreCase( OLD_PASSWD ) || uiFormItem.getLabel().equalsIgnoreCase( NEW_PASSWD )
                                || uiFormItem.getLabel().equalsIgnoreCase( CONFIRM_PASSWD ) ) ) {
                    iterator.remove();
                }

                if ( UserDTO.THEME_LABEL.equalsIgnoreCase( uiFormItem.getName() ) ) {
                    List< SelectOptionsUI > options = new ArrayList<>();
                    for ( UIThemeEnums themeEnum : UIThemeEnums.getAll() ) {
                        options.add( new SelectOptionsUI( themeEnum.getId(), themeEnum.getName() ) );
                    }
                    ( ( SelectFormItem ) uiFormItem ).setOptions( options );
                }

                if ( "userDetails[].email".equalsIgnoreCase( uiFormItem.getName() ) ) {
                    uiFormItem.setReadonly( true );

                } else if ( UserProfileDTO.LOCATION_PREFERENCE_SELECTION_ID.equalsIgnoreCase( uiFormItem.getName() ) ) {
                    iterator.remove();
                    locationTable = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
                    locationTable.setMultiple( true );
                    locationTable.setName( UserProfileDTO.LOCATION_PREFERENCE_SELECTION_ID );
                    locationTable.setLabel( "Select Location Preferences" );
                    locationTable.setType( SELECTION_TYPE_TABLE );
                    locationTable.setConnected( LOCATION_SELECTION_PATH );
                    locationTable.setConnectedTableLabel( LOCATION_SELECTION_CONNECTED_TABLE_LABEL );
                    locationTable.setValue( profileDTO.getLocationPreferenceSelectionId() );
                    locationTable.setSelectable( null );
                    locationTable.setRowReorder( true );
                    locationTable.setSection( "Profile Details" );
                    locationTable.setButtonLabel( MessageBundleFactory.getMessage( Messages.SELECT_LOCATION.getKey() ) );
                    locationTable.setShow( String.valueOf( true ) );
                } else if ( uiFormItem.getName().contains( "language" ) ) {
                    if ( PropertiesManager.hasTranslation() ) {
                        List< SelectOptionsUI > options = new ArrayList<>();
                        Map< String, String > languages = PropertiesManager.getRequiredlanguages();
                        for ( String str : languages.keySet() ) {
                            options.add( new SelectOptionsUI( str, languages.get( str ) ) );
                        }
                        ( ( SelectFormItem ) uiFormItem ).setOptions( options );
                        ( ( SelectFormItem ) uiFormItem ).setTooltip( "Relogin required for translation changes" );
                    } else {
                        iterator.remove();
                    }

                }
            }
            if ( locationTable != null ) {
                uiFormItemList.add( locationTable );
            }
            return uiFormItemList;
        }
    }

    /**
     * Prepare Select Item.
     *
     * @param groups
     *         the groups
     *
     * @return List<SelectObject> list
     */
    private List< SelectOptionsUI > prepareSelectItem( List< GroupEntity > groups ) {
        Map< String, String > map = new HashMap<>();
        if ( CollectionUtil.isNotEmpty( groups ) ) {
            for ( GroupEntity groupEntity : groups ) {
                map.put( groupEntity.getId().toString(), groupEntity.getName() );
            }
        }
        return GUIUtils.getSelectBoxOptions( map );
    }

    /**
     * Prepare Selected Groups.
     *
     * @param groups
     *         the groups
     *
     * @return String representation of list
     */
    private String prepareSelectedGroups( List< SuSUserGroupDTO > groups ) {
        List< String > groupIds = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( groups ) ) {
            for ( SuSUserGroupDTO allowedUser : groups ) {
                groupIds.add( allowedUser.getId().toString() );
            }
        }
        return GUIUtils.prepareStringFromList( groupIds, ConstantsString.COMMA );
    }

    /**
     * A method to create user selected language file in karaf.
     *
     * @param locale
     *         the locale
     */
    private void createUserLanguageFile( String locale ) {
        boolean fileCreated = false;
        final String file =
                System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY
                        + File.separator + MessageBundleFactory.LANGUAGE_FILE;
        File languageFile = new File( file );
        if ( !languageFile.exists() ) {
            try {
                fileCreated = languageFile.createNewFile();
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
            }
        }

        try ( FileOutputStream fos = new FileOutputStream( languageFile.getPath() ); OutputStreamWriter osw = new OutputStreamWriter( fos,
                StandardCharsets.UTF_8 ); Writer writer = new BufferedWriter( osw ) ) {
            if ( fileCreated ) {
                writer.write( MessageBundleFactory.DEFAULT_LOCALE + ConstantsString.EQUALS_OPERATOR + locale );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UUID > getAllUserIds( EntityManager entityManager ) {
        return userDAO.getIdOfAllUsers( entityManager );
    }

    /**
     * Sets the language DAO.
     *
     * @param languageDAO
     *         the new language DAO
     */
    public void setLanguageDAO( LanguageDAO languageDAO ) {
        this.languageDAO = languageDAO;
    }

    /**
     * Sets the user token DAO.
     *
     * @param userTokenDAO
     *         the new user token DAO
     */
    public void setUserTokenDAO( UserTokenDAO userTokenDAO ) {
        this.userTokenDAO = userTokenDAO;
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
     * Sets the license manager.
     *
     * @param licenseManager
     *         the new license manager
     */
    public void setLicenseManager( LicenseManager licenseManager ) {
        this.licenseManager = licenseManager;
    }

    /**
     * Sets the user dao.
     *
     * @param userDAO
     *         the new user dao
     */
    public void setUserDAO( UserDAO userDAO ) {
        this.userDAO = userDAO;
    }

    /**
     * Sets the user login attempt dao.
     *
     * @param userLoginAttemptDAO
     *         the new user login attempt dao
     */
    public void setUserLoginAttemptDAO( UserLoginAttemptDAO userLoginAttemptDAO ) {
        this.userLoginAttemptDAO = userLoginAttemptDAO;
    }

    /**
     * Sets the directory manager.
     *
     * @param directoryManager
     *         the new directory manager
     */
    public void setDirectoryManager( DirectoryManager directoryManager ) {
        this.directoryManager = directoryManager;
    }

    /**
     * Sets the user detail DAO.
     *
     * @param userDetailDAO
     *         the new user detail DAO
     */
    public void setUserDetailDAO( UserDetailDAO userDetailDAO ) {
        this.userDetailDAO = userDetailDAO;
    }

    /**
     * Sets the ldap auth realm.
     *
     * @param ldapAuthRealm
     *         the new ldap auth realm
     */
    public void setLdapAuthRealm( LdapCustomAuthRealm ldapAuthRealm ) {
        this.ldapAuthRealm = ldapAuthRealm;
    }

    /**
     * Sets the ad custom realm.
     *
     * @param adCustomRealm
     *         the new ad custom realm
     */
    public void setAdCustomRealm( ActiveDirectoryCustomRealm adCustomRealm ) {
        this.adCustomRealm = adCustomRealm;
    }

    /**
     * sets context menu manager.
     *
     * @param contextMenuManager
     *         the new context menu manager
     */
    public void setContextMenuManager( ContextMenuManager contextMenuManager ) {
        this.contextMenuManager = contextMenuManager;
    }

    /**
     * Gets the security identity DAO.
     *
     * @return the security identity DAO
     */
    public AclSecurityIdentityDAO getSecurityIdentityDAO() {
        return securityIdentityDAO;
    }

    /**
     * Sets the security identity DAO.
     *
     * @param securityIdentityDAO
     *         the new security identity DAO
     */
    public void setSecurityIdentityDAO( AclSecurityIdentityDAO securityIdentityDAO ) {
        this.securityIdentityDAO = securityIdentityDAO;
    }

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserEntity > getUsersByGroupEntity( EntityManager entityManager, GroupEntity groupEntity ) {
        return userDAO.getUsersByGroupEntity( entityManager, groupEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserEntity > getUsersByDirectoryId( EntityManager entityManager, UUID dirId ) {
        return userDAO.getUsersByDirectoryId( entityManager, dirId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserEntity > getFilteredUsersByGroupEntity( EntityManager entityManager, GroupEntity groupEntity, FiltersDTO filter ) {
        return userDAO.getFilteredUsersByGroupEntity( entityManager, groupEntity, filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String prepareUserToken( EntityManager entityManager, UserDTO user ) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        if ( currentMessage == null ) {
            throw new AuthenticationException( "Message is Null for this request : try again" );
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "Request Message > " + currentMessage );
        }
        long expiryTime;
        try {
            int expiry = Integer.parseInt( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.USER_SESSION_EXPIRY ) );
            expiryTime = tokenManager.getExpiryTime( expiry );
        } catch ( Exception ex ) {
            expiryTime = tokenManager.getExpiryTime( ConstantsFileProperties.USER_SESSION_EXPIRY_TIME );
            log.error( ex.getMessage(), ex );
        }
        return tokenManager.prepareAndPersistToken( entityManager, currentMessage, user.getId(), user.getUserUid(), expiryTime,
                String.valueOf( new Random().nextInt( 1000 ) ) );
        // passing random number b/w 0-999 as key for unique token generation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserAuthenticateInLdap( String uid, String password ) {
        UsernamePasswordToken token = new UsernamePasswordToken( uid, password.toCharArray() );
        AuthenticationInfo info = null;
        try {
            info = ldapAuthRealm.doGetAuthenticationInfo( token );
            resetFailedLdapAttempts( token.getUsername() );
        } catch ( AuthenticationException e ) {
            ExceptionLogger.logException( e, getClass() );
            updateFailedLdapAttempt( uid );
        }
        return info != null && info.getCredentials() != null;
    }

    /**
     * Update failed ldap attempt.
     *
     * @param uid
     *         the uid
     */
    private void updateFailedLdapAttempt( String uid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            updateFailedLdapAttempt( entityManager, uid );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update failed ldap attempt.
     *
     * @param entityManager
     *         the entity manager
     * @param uid
     *         the uid
     */
    private void updateFailedLdapAttempt( EntityManager entityManager, String uid ) {
        var attempt = userLoginAttemptDAO.getFailedLoginAttempts( entityManager, uid );
        if ( attempt == null ) {
            attempt = userLoginAttemptDAO.saveFailedLoginAttempt( entityManager, uid );
        }
        if ( attempt.getLdapAttempts() == null ) {
            attempt.setLdapAttempts( ConstantsInteger.INTEGER_VALUE_ONE );
        } else {
            attempt.setLdapAttempts( attempt.getLdapAttempts() + ConstantsInteger.INTEGER_VALUE_ONE );
        }
        userLoginAttemptDAO.updateFailedLoginAttempt( entityManager, attempt );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, SuSCoreSessionDTO > getUserTokenMap( String token ) {
        Map< String, SuSCoreSessionDTO > map = new HashMap<>();
        map.put( token, TokenizedLicenseUtil.getSessionDTO( token ) );
        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getUserFaildedLdapAttempts( String userUid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var attempts = userLoginAttemptDAO.getFailedLoginAttempts( entityManager, userUid );
            return attempts.getLdapAttempts() != null ? attempts.getLdapAttempts() : ConstantsInteger.INTEGER_VALUE_ZERO;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public UserDTO getUserByUserUidForVerification( String userUid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var userEntity = userDAO.getUserByUid( entityManager, userUid );
            if ( userEntity == null ) {
                return null;
            }
            var userDto = new UserDTO();
            if ( userEntity.getDocument() != null ) {
                DocumentDTO profilePhoto = documentManager.prepareDocumentDTO( userEntity.getDocument() );
                profilePhoto.setUrl(
                        CommonUtils.getBaseUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_BASE_URL ) )
                                + File.separator + ConstantsString.STATIC_PATH + profilePhoto.getPath() + File.separator
                                + profilePhoto.getName() );
                userDto.setProfilePhoto( new DocumentDTO() );
                userDto.getProfilePhoto().setUrl( profilePhoto.getUrl() );
            }
            userDto.setName( userEntity.getFirstName() + ConstantsString.SPACE + userEntity.getSurName() );
            userDto.setFirstName( userEntity.getFirstName() );
            userDto.setSurName( userEntity.getSurName() );
            userDto.setSusUserDirectoryDTO(
                    directoryManager.prepareDirectoryModelFromEntityWithoutUsers( userEntity.getDirectory(), false ) );
            return userDto;
        } finally {
            entityManager.close();
        }
    }

    public List< Object > getAllValuesForUserTableColumn( String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( UserDTO.class, UserDetail.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            allValues = userDAO.getAllPropertyValues( entityManager, columnName );
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI listGroupsFromUserIdUI( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return new TableUI( GUIUtils.listColumns( SuSUserGroupDTO.class ), userGroupManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.GROUP_TABLE_KEY, userId, null ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< SuSUserGroupDTO > listGroupsFromUserId( String userId, UUID userIdForGroups, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SuSUserGroupDTO > groupList = new ArrayList<>();
            List< GroupEntity > groupEntities = userGroupManager.getUserGroupsByUserId( entityManager, userIdForGroups );
            if ( CollectionUtil.isNotEmpty( groupEntities ) ) {
                groupEntities.forEach( groupEntity -> groupList.add(
                        userGroupManager.prepareSusUserGroupDTOFromGroupEntity( entityManager, groupEntity ) ) );
            }
            return PaginationUtil.constructFilteredResponse( filter, groupList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateUserPasswordInDatabase( String userId, String password ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var userEntity = userDAO.getUserByUid( entityManager, userId );
            return FileUtils.getSha256CheckSum( password ).equals( userEntity.getPassword() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * Sets the sus DAO.
     *
     * @param susDAO
     *         the new sus DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

    /**
     * Sets the user group DAO.
     *
     * @param userGroupDAO
     *         the new user group DAO
     */
    public void setUserGroupDAO( UserGroupDAO userGroupDAO ) {
        this.userGroupDAO = userGroupDAO;
    }

    /**
     * Sets the user group manager.
     *
     * @param userGroupManager
     *         the new user group manager
     */
    public void setUserGroupManager( UserGroupManager userGroupManager ) {
        this.userGroupManager = userGroupManager;
    }

    /**
     * Gets the document manager.
     *
     * @return the document manager
     */
    public DocumentManager getDocumentManager() {
        return documentManager;
    }

    /**
     * Sets the document manager.
     *
     * @param documentManager
     *         the new document manager
     */
    public void setDocumentManager( DocumentManager documentManager ) {
        this.documentManager = documentManager;
    }

    /**
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Gets token manager.
     *
     * @return the token manager
     */
    public UserTokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Sets token manager.
     *
     * @param tokenManager
     *         the token manager
     */
    public void setTokenManager( UserTokenManager tokenManager ) {
        this.tokenManager = tokenManager;
    }

    /**
     * Gets entity manager factory.
     *
     * @return the entity manager factory
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
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
