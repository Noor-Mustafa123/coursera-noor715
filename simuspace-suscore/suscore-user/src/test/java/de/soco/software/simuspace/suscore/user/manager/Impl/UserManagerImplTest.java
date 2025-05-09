package de.soco.software.simuspace.suscore.user.manager.Impl;

import static org.easymock.EasyMock.expect;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.auth.connect.ad.authentication.ActiveDirectoryCustomRealm;
import de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.LdapCustomAuthRealm;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
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
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.ImageUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.LanguageEntity;
import de.soco.software.simuspace.suscore.data.entity.LoginAttemptEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserDetailEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.interceptors.dao.UserTokenDAO;
import de.soco.software.simuspace.suscore.license.manager.LicenseConfigurationManager;
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
import de.soco.software.simuspace.suscore.user.manager.impl.UserManagerImpl;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * This class provides all the test cases related to the interface UserManager.
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { ConfigFilePathReader.class, PropertiesManager.class } )
public class UserManagerImplTest {

    /**
     * The Constant STATIC_METHOD_GET_VALUE_BY_NAME_FROM_PROPERTIES_FILE_IN_KARAF.
     */
    private static final String STATIC_METHOD_GET_VALUE_BY_NAME_FROM_PROPERTIES_FILE_IN_KARAF = "getValueByNameFromPropertiesFileInKaraf";

    /**
     * The manager.
     */
    private UserManagerImpl manager;

    /**
     * The directoryManager.
     */
    private DirectoryManager directoryManager;

    /**
     * The context selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The user entity.
     */
    private UserEntity userEntity;

    /**
     * The languageEntity .
     */
    private LanguageEntity languageEntity;

    /**
     * The loginAttemptEntity entity.
     */
    private LoginAttemptEntity loginAttemptEntity;

    /**
     * The userLoginAttemptDAO reference.
     */
    private UserLoginAttemptDAO userLoginAttemptDAO;

    /**
     * The languageDAO reference.
     */
    private LanguageDAO languageDAO;

    /**
     * The userTokenDAO reference.
     */
    private UserTokenDAO userTokenDAO;

    /**
     * The user dao.
     */
    private UserDAO userDAO;

    /**
     * The user deatil dao.
     */
    private UserDetailDAO userDetailDAO;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The ad custom realm.
     */
    ActiveDirectoryCustomRealm adCustomRealm;

    /**
     * The LDAP custom realm.
     */
    LdapCustomAuthRealm ldapCustomRealm;

    /**
     * The security identity DAO.
     */
    private AclSecurityIdentityDAO securityIdentityDAO;

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
     * The license configuration manager.
     */
    LicenseConfigurationManager licenseConfigManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The document manager.
     */
    private DocumentManager documentManager;

    /**
     * Dummy USER_ID for test Cases.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant ITEM_ID_ONE.
     */
    public static final UUID ITEM_ID_ONE = UUID.randomUUID();

    /**
     * The Constant ITEM_ID_TWO.
     */
    public static final UUID ITEM_ID_TWO = UUID.randomUUID();

    /**
     * The Constant ITEM_ID_THREE.
     */
    public static final UUID ITEM_ID_THREE = UUID.randomUUID();

    /**
     * The Constant SELECTION_ORIGIN_USER.
     */
    public static final String SELECTION_ORIGIN_USER = "User";

    /**
     * The Constant SELECTION_ID.
     */
    public static final UUID SELECTION_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92259ddc" );

    /**
     * Dummy UserId for test Cases.
     */
    private static final String DEFAULT_USER_ID = "1";

    /**
     * The Constant DIR_ID.
     */
    private static final UUID DIR_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92259ddc" );

    /**
     * The Constant FIRSTNAME.
     */
    private static final String FIRSTNAME = "firstName";

    /**
     * The Constant user id changed for mocking.
     */
    private static final String UID_CHANGED = "sces138_changed";

    /**
     * The Constant PASSWD.
     */
    private static final String PASSWD = "aaZZa44e";

    /**
     * The Constant ENCRYPTED_PASSWORD.
     */
    private static final String ENCRYPTED_PASSWORD = "45oBURKWXvy723DqheJTYoY25OAgsxSTd7pPPa3CAvA=";

    /**
     * The Constant PASSWD_LESS_THAN_EIGHT_CHAR.
     */
    private static final String PASSWD_LESS_THAN_EIGHT_CHAR = "aaZZ";

    /**
     * The Constant PASSWD_WITHOUT_NATURAL_NUMBER.
     */
    private static final String PASSWD_WITHOUT_NATURAL_NUMBER = "aaZZauue";

    /**
     * The Constant PASSWD_WITHOUT_LOWER_CASE_ALPHABET.
     */
    private static final String PASSWD_WITHOUT_LOWER_CASE_ALPHABET = "AAZZ777D";

    /**
     * The Constant PASSWD_WITHOUT_LOWER_CASE_ALPHABET.
     */
    private static final String PASSWD_WITHOUT_UPPER_CASE_ALPHABET = "aaaa777a";

    /**
     * The Constant NULL_PASSWORD.
     */
    private static final String NULL_PASSWORD = null;

    /**
     * The Constant EMPTY_PASSWORD.
     */
    private static final String EMPTY_PASSWORD = "";

    /**
     * The Constant PASSWD_WITHOUT_LOWER_CASE_ALPHABET.
     */
    private static final String PASSWD_WITH_WHITE_SPACES = "aa Za 4e";

    /**
     * The Constant LASTNAME.
     */
    private static final String LASTNAME = "khan";

    /**
     * The Constant DESIGNATION.
     */
    private static final String DESIGNATION = "lahore";

    /**
     * The Constant CONTACT.
     */
    private static final String CONTACT = "+1(321).640-8410";

    /**
     * The Constant INVALID_CONTACT.
     */
    private static final String INVALID_CONTACT = "567890(&dfsdf";

    /**
     * The Constant EMAIL.
     */
    private static final String EMAIL = "test@supermoon.com";

    /**
     * The Constant having invalid email address.
     */
    private static final String INVALID_EMAIL = "te#@st@s#$ample-fdstest.c#om";

    /**
     * The Constant DEPARTMENT.
     */
    private static final String DEPARTMENT = "development";

    /**
     * The Constant EMPTY_USERNAME.
     */
    private static final String EMPTY_USERNAME = "";

    /**
     * The Constant DIRECTORY_ID.
     */
    private static final UUID DIRECTORY_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92459ddc" );

    /**
     * The Constant SURNAME_WITH_GREATER_THEN_MAX.
     */
    private static final String FIRSTNAME_WITH_GREATER_THEN_MAX = "ghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";

    /**
     * The Constant UID_WITH_GREATER_THEN_MAX.
     */
    private static final String UID_WITH_GREATER_THEN_MAX = "ghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";

    /**
     * The Constant VALUE_TOO_LARGE_FOR_SURNAME.
     */
    private static final String VALUE_TOO_LARGE_FOR_FIRSTNAME = "Value too large for FIRST NAME (MaxLength=255).";

    /**
     * The Constant VALUE_TOO_LARGE_FOR_UID.
     */
    private static final String VALUE_TOO_LARGE_FOR_UID = "Value too large for UID (MaxLength=255).";

    /**
     * The Constant SURNAME_CANNOT_BE_NULL_OR_EMPTY.
     */
    private static final String FIRSTNAME_CANNOT_BE_NULL_OR_EMPTY = "FIRST NAME can not be null or empty.";

    /**
     * The Constant UID_CANNOT_BE_NULL_OR_EMPTY.
     */
    private static final String UID_CANNOT_BE_NULL_OR_EMPTY = "UID can not be null or empty.";

    /**
     * The Constant REQUIRE_ATLEAST_ONE_NATURAL_NUMBER.
     */
    private static final String REQUIRE_ATLEAST_ONE_NATURAL_NUMBER = "At least one natural number required for password.";

    /**
     * The Constant REQUIRE_ATLEAST_ONE_LOWER_CASE_ALPHABET.
     */
    private static final String REQUIRE_ATLEAST_ONE_LOWER_CASE_ALPHABET = "At least one lower case alphabet required for password.";

    /**
     * The Constant REQUIRE_ATLEAST_ONE_UPPER_CASE_ALPHABET.
     */
    private static final String REQUIRE_ATLEAST_ONE_UPPER_CASE_ALPHABET = "At least one upper case alphabet required for password.";

    /**
     * The Constant NO_WHITE_SPCE_ALLOWED.
     */
    private static final String NO_WHITE_SPCE_ALLOWED = "No whitespace allowed in the password.";

    /**
     * The Constant PASSWORD_CANNOT_BE_NULL_OR_EMPTY.
     */
    private static final String PASSWORD_CANNOT_BE_NULL_OR_EMPTY = "Password can not be null or empty.";

    /**
     * The account not found.
     */
    private static final String NO_ACCOUNT_FOUND_FOR_USER = "No account found for user.";

    /**
     * Provide user details.
     */
    private static final String PROVIDE_USER_DETAILS = "Provide User Detail";

    /**
     * The user already exist.
     */
    private static final String USER_ALREADY_EXIST = "User Already exist";

    /**
     * The invalid credentials.
     */
    private static final String INVALID_LOGIN_CREDENTIALS = "Invalid username and / or password";

    /**
     * The account locked.
     */
    private static final String TO_MANY_ATTEMPTS_ACCOUNT_LOCKED = "Too many attempts! Account Locked";

    /**
     * The Constant UID.
     */
    private static final String UID = "sces120";

    /**
     * The Constant UID.
     */
    private static final UUID USER_UUID = UUID.randomUUID();

    /**
     * The Constant LANGUAGE_UUID.
     */
    private static final UUID LANGUAGE_UUID = UUID.randomUUID();

    /**
     * The Constant IS_CHANGEABLE.
     */
    private static final boolean IS_CHANGEABLE = false;

    /**
     * active directoey usernmae.
     */
    private static final String VALID_SYSTEM_USERNAME_AD = "cn=Administrator,CN=Users,dc=whpc2,dc=clus";

    /**
     * active directory password.
     */
    private static final String VALID_SYSTEM_PASSWORD_AD = "Lite0lite0";

    /**
     * valid url for active directory.
     */
    private static final String VALID_URL_AD = "ldap://172.24.1.233:389";

    /**
     * Invalid url for active directory.
     */
    private static final String INVALID_URL_AD = "ldap://172.24.1.111:389";

    /** valid search base for active directory. */
    /**
     * The valid search based AD value.
     */
    private static final String VALID_SEARCH_BASE_AD = "CN=Users,DC=WHPC2,DC=CLUS";

    /**
     * The Constant EMPTY_UID.
     */
    private static final String EMPTY_UID = "";

    /**
     * The Constant NULL_UID.
     */
    private static final String NO_UID_PROVIDED = null;

    /**
     * The active account status.
     */

    public static final boolean ACCOUNT_STATUS_ACTIVE = true;

    /**
     * The inactive account status.
     */
    public static final String ACCOUNT_STATUS_IN_ACTIVE = "Inactive";

    /**
     * invalid AD system name for testing.
     */
    private static final String INVALID_AD_SYSTEM_NAME = "abc";

    /**
     * invalid AD system password for testing.
     */
    private static final String INVALID_AD_SYSTEM_PASSWORD = "abc";

    /**
     * local directory type id container *.
     */
    private static final String LOCAL_DIRECTORY_TYPE = "0";

    /**
     * Dummy GROUP_ID for test Cases.
     */
    private static final UUID GROUP_ID = UUID.randomUUID();

    /**
     * Dummy GROUP_ID_2 for test Cases.
     */
    private static final UUID GROUP_ID_2 = UUID.randomUUID();

    /**
     * Dummy GROUP_ID_3 for test Cases.
     */
    private static final UUID GROUP_ID_3 = UUID.randomUUID();

    /**
     * The Constant DESCRIPTION_FIELD.
     */
    private static final String DESCRIPTION_FIELD = "Description";

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * The Constant ACTIVE.
     */
    public static final String ACTIVE = "Active";

    /**
     * The Constant USER_ID_FOR_TEST.
     */
    private static final String USER_ID_TEST = UUID.randomUUID().toString();

    /**
     * The Constant Active Group.
     */
    private static final boolean ACTIVE_GRP = true;

    /**
     * The Constant SUPER_USER_ID.
     */
    public static final String SUPER_USER_ID = "1e98a77c-a0be-4983-9137-d9a8acd0ea8b";

    /**
     * The Constant PASSWORD_INVALID_ERROR_MSG.
     */
    public static final String PASSWORD_INVALID_ERROR_MSG = "Password must be at least 8 characters.";

    /**
     * >A simple username/password authentication token to support the most widely-used authentication mechanism. This instance for Active
     * directory.
     */
    private UsernamePasswordToken userNamePasswordTokenActiveDirectory;

    /**
     * The user reference .
     */
    private UserDTO user;

    /**
     * The user profile dto reference .
     */
    private UserProfileDTO userProfile;

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Authentication info reference for LDAP/AD mocking result.
     */
    private AuthenticationInfo info;

    /**
     * A mocked user detail id.
     */
    private static final UUID USER_DETAIL_ID = UUID.randomUUID();

    /**
     * The Constant ACCOUNT_STATUS_ACTIVE_AT_DTO.
     */
    private static final String ACCOUNT_STATUS_ACTIVE_AT_DTO = "Active";

    /**
     * The Constant ACCOUNT_STATUS_INACTIVE_AT_DTO.
     */
    private static final Object ACCOUNT_STATUS_INACTIVE_AT_DTO = "Inactive";

    /**
     * The Constant SUR_NAME.
     */
    private static final String SUR_NAME = "Khan";

    /**
     * The Constant IS_RESTICTEED.
     */
    private static final String IS_RESTRICTED = "No";

    /**
     * The Dummy Constant for sttaus DropDown.
     */
    private static final String STATUS = "status";

    private static final String USER = "User";

    /**
     * The Constant GET_FAILED_LOGIN_ATTEMPTS.
     */
    private static final String GET_FAILED_LOGIN_ATTEMPT = "getFailedLoginAttempt";

    /**
     * The Constant LOGIN_ATTEMPTS.
     */
    private static final String LOGIN_ATTEMPTS = "9";

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * remove files at the end of all the tests.
     */
    @AfterClass
    public static void tearDown() {
        File dir = new File( ConstantsString.TEST_RESOURCE_PATH );
        for ( File file : dir.listFiles() ) {
            if ( !file.isDirectory() && file.getName().contains( USER_DETAIL_ID.toString() ) ) {
                file.delete();
            }
        }
    }

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        manager = new UserManagerImpl();
        userLoginAttemptDAO = mockControl.createMock( UserLoginAttemptDAO.class );
        userDAO = mockControl.createMock( UserDAO.class );
        userDetailDAO = mockControl.createMock( UserDetailDAO.class );
        languageDAO = mockControl.createMock( LanguageDAO.class );
        directoryManager = mockControl.createMock( DirectoryManager.class );
        adCustomRealm = mockControl.createMock( ActiveDirectoryCustomRealm.class );
        ldapCustomRealm = mockControl.createMock( LdapCustomAuthRealm.class );
        licenseManager = mockControl.createMock( LicenseManager.class );
        userTokenDAO = mockControl.createMock( UserTokenDAO.class );
        securityIdentityDAO = mockControl.createMock( AclSecurityIdentityDAO.class );
        susDAO = mockControl.createMock( SuSGenericObjectDAO.class );
        userGroupManager = mockControl.createMock( UserGroupManager.class );
        userGroupDAO = mockControl.createMock( UserGroupDAO.class );
        licenseConfigManager = mockControl.createMock( LicenseConfigurationManager.class );
        contextMenuManager = mockControl.createMock( ContextMenuManager.class );
        selectionManager = mockControl.createMock( SelectionManager.class );
        documentManager = mockControl.createMock( DocumentManager.class );
        permissionManager = mockControl.createMock( PermissionManager.class );

        manager.setSelectionManager( selectionManager );
        manager.setUserDAO( userDAO );
        manager.setLicenseManager( licenseManager );
        manager.setUserLoginAttemptDAO( userLoginAttemptDAO );
        manager.setUserDetailDAO( userDetailDAO );
        manager.setDirectoryManager( directoryManager );
        manager.setAdCustomRealm( adCustomRealm );
        manager.setLdapAuthRealm( ldapCustomRealm );
        manager.setUserTokenDAO( userTokenDAO );
        manager.setLanguageDAO( languageDAO );
        manager.setSecurityIdentityDAO( securityIdentityDAO );
        manager.setSusDAO( susDAO );
        manager.setUserGroupManager( userGroupManager );
        manager.setUserGroupDAO( userGroupDAO );
        manager.setContextMenuManager( contextMenuManager );
        manager.setDocumentManager( documentManager );
        manager.setPermissionManager( permissionManager );
    }

    /**
     * Should throw exception when no user is provided.
     */

    @Test
    public void shouldThrowExceptionWhenUserIsNotProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.FEATURE_NOT_ALLOWED_TO_USER.getKey(), SimuspaceFeaturesEnum.USERS.getKey() ) );
        manager.deleteUserBySelection( DEFAULT_USER_ID, UUID.randomUUID().toString(), ConstantsString.EMPTY_STRING );
        thrown.expectMessage( PROVIDE_USER_DETAILS );
        mockControl.replay();
        manager.createUser( USER_ID_TEST, user );
    }

    /**
     * Should throw exception if User name is empty.
     */
    @Test
    public void shouldThrowExceptionIfUserNameIsEmpty() {
        setUpAuthenticationTokenForAD();
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), EMPTY_USERNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        thrown.expect( SusException.class );
        thrown.expectMessage( FIRSTNAME_CANNOT_BE_NULL_OR_EMPTY );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if user name is null.
     */
    @Test
    public void shouldThrowExceptionIfUserNameIsNull() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), null, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        setUpAuthenticationTokenForAD();
        thrown.expect( SusException.class );
        thrown.expectMessage( FIRSTNAME_CANNOT_BE_NULL_OR_EMPTY );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if uid is empty.
     */
    @Test
    public void shouldThrowExceptionIfUIDIsEmpty() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, EMPTY_UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        thrown.expect( SusException.class );
        thrown.expectMessage( UID_CANNOT_BE_NULL_OR_EMPTY );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if uid is null.
     */
    @Test
    public void shouldThrowExceptionIfUIDIsNull() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, NO_UID_PROVIDED, ACCOUNT_STATUS_ACTIVE_AT_DTO,
                PASSWD );
        thrown.expect( SusException.class );
        thrown.expectMessage( UID_CANNOT_BE_NULL_OR_EMPTY );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if uid exceed max length.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionIfUIDExceedMaxLength() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID_WITH_GREATER_THEN_MAX,
                ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        thrown.expect( SusException.class );
        thrown.expectMessage( VALUE_TOO_LARGE_FOR_UID );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if user name exceed max length.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionIfUserNameExceedMaxLength() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME_WITH_GREATER_THEN_MAX, UID,
                ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        thrown.expect( SusException.class );
        thrown.expectMessage( VALUE_TOO_LARGE_FOR_FIRSTNAME );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if UID exceed max length.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionIfUIDNameExceedMaxLength() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID_WITH_GREATER_THEN_MAX,
                ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        setUpAuthenticationTokenForAD();
        thrown.expect( SusException.class );
        thrown.expectMessage( VALUE_TOO_LARGE_FOR_UID );
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledInternalDirectory() ).anyTimes();
        EasyMock.expect(
                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( true ).anyTimes();
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( USER_ID_TEST, user );
    }

    /**
     * Should throw exception when unique user is not provided.
     *
     * the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenUniqueUserIsNotProvided() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );
        thrown.expect( SusException.class );
        thrown.expectMessage( USER_ALREADY_EXIST );
        // set user to AD
        setUpAuthenticationTokenForAD();
        EasyMock.expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( true )
                .anyTimes();

        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();

        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Set Up Authentication Token For AD.
     */
    private void setUpAuthenticationTokenForAD() {
        info = new AuthenticationInfo() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public PrincipalCollection getPrincipals() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return userNamePasswordTokenActiveDirectory;
            }
        };

        EasyMock.expect( adCustomRealm.isActiveDirectoryConnectionEstablished( EasyMock.anyObject() ) ).andReturn( true );

        EasyMock.expect( adCustomRealm.doGetAuthenticationInfo( EasyMock.anyObject() ) ).andReturn( info ).anyTimes();

        userNamePasswordTokenActiveDirectory = new UsernamePasswordToken( VALID_SYSTEM_USERNAME_AD, VALID_SYSTEM_PASSWORD_AD );
    }

    /**
     * Should add user when validated user entity is given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldAddUserWhenValidatedUserEntityIsGiven() throws Exception {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD_WITHOUT_LOWER_CASE_ALPHABET, DIRECTORY_ID, IS_CHANGEABLE,
                LASTNAME, DESIGNATION, CONTACT, EMAIL );

        final UserEntity expectedUserEntity = userEntity;
        EasyMock.expect( licenseManager.isLicenseAddedAgainstUserForModule( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity.getDirectory() ).anyTimes();
        EasyMock.expect( directoryManager.prepareDirectoryEntityFromModel( EasyMock.anyObject() ) ).andReturn( userEntity.getDirectory() )
                .anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect( userDAO.createUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ) ) )
                .andReturn( expectedUserEntity ).anyTimes();

        EasyMock.expect(
                        userGroupManager.getUserGroupsByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();

        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity )
                .anyTimes();
        UserEntity sperUserEntity = prepareUserEntity();
        sperUserEntity.setStatus( true );
        sperUserEntity.setRestricted( false );
        sperUserEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( sperUserEntity ).anyTimes();

        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( sperUserEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        mockControl.replay();

        final UserDTO actualUser = manager.createUser( SUPER_USER_ID, user );

        Assert.assertEquals( expectedUserEntity.getId(), UUID.fromString( actualUser.getId() ) );
        Assert.assertEquals( expectedUserEntity.getFirstName(), actualUser.getFirstName() );
        Assert.assertEquals( expectedUserEntity.getSurName(), actualUser.getSurName() );
        Assert.assertEquals( expectedUserEntity.isStatus(), actualUser.getStatus().equals( ACCOUNT_STATUS_ACTIVE_AT_DTO ) ? true : false );
        Assert.assertEquals( expectedUserEntity.getDirectory().getId(), actualUser.getSusUserDirectoryDTO().getId() );

        Assert.assertNotNull( actualUser.getUserDetails() );
        for ( UserDetail actualDetail : actualUser.getUserDetails() ) {
            Assert.assertEquals( actualDetail.getContacts(), expectedUserEntity.getUserDetails().iterator().next().getContacts() );
            Assert.assertEquals( actualDetail.getEmail(), expectedUserEntity.getUserDetails().iterator().next().getEmail() );
            Assert.assertEquals( actualDetail.getDesignation(), expectedUserEntity.getUserDetails().iterator().next().getDesignation() );
            Assert.assertEquals( actualDetail.getId(), expectedUserEntity.getUserDetails().iterator().next().getId().toString() );
        }
    }

    /**
     * should Add User When Valid Internal Directory User Entity Is Given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldAddUserWhenValidInternalDirectoryUserEntityIsGiven() throws Exception {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );
        SuSUserDirectoryDTO internalDirectory = getfilledDirectory();
        internalDirectory.setType( ConstantsUserDirectories.INTERNAL_DIRECTORY );
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD_WITHOUT_LOWER_CASE_ALPHABET, DIRECTORY_ID, IS_CHANGEABLE,
                LASTNAME, DESIGNATION, CONTACT, EMAIL );
        final UserEntity expectedUserEntity = userEntity;
        EasyMock.expect( licenseManager.isLicenseAddedAgainstUserForModule( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect( directoryManager.prepareDirectoryEntityFromModel( EasyMock.anyObject() ) ).andReturn( userEntity.getDirectory() )
                .anyTimes();
        EasyMock.expect( userDAO.createUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ) ) )
                .andReturn( expectedUserEntity ).anyTimes();
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity.getDirectory() ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();

        EasyMock.expect( securityIdentityDAO.save( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( AclSecurityIdentityEntity.class ) ) ).andReturn( fillSecurityIdentity( expectedUserEntity ) )
                .anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        final UserDTO actualUser = manager.createUser( SUPER_USER_ID, user );

        Assert.assertEquals( expectedUserEntity.getId(), UUID.fromString( actualUser.getId() ) );
        Assert.assertEquals( expectedUserEntity.getFirstName(), actualUser.getFirstName() );
        Assert.assertEquals( expectedUserEntity.getSurName(), actualUser.getSurName() );
        Assert.assertEquals( expectedUserEntity.isStatus(), actualUser.getStatus().equals( ACCOUNT_STATUS_ACTIVE_AT_DTO ) ? true : false );
        Assert.assertEquals( expectedUserEntity.getDirectory().getId(), actualUser.getSusUserDirectoryDTO().getId() );

        Assert.assertNotNull( actualUser.getUserDetails() );
        for ( UserDetail actualDetail : actualUser.getUserDetails() ) {
            Assert.assertEquals( actualDetail.getContacts(), expectedUserEntity.getUserDetails().iterator().next().getContacts() );
            Assert.assertEquals( actualDetail.getEmail(), expectedUserEntity.getUserDetails().iterator().next().getEmail() );
            Assert.assertEquals( actualDetail.getDesignation(), expectedUserEntity.getUserDetails().iterator().next().getDesignation() );
            Assert.assertEquals( actualDetail.getId(), expectedUserEntity.getUserDetails().iterator().next().getId().toString() );
        }
    }

    /**
     * Should throw exception if password is not eight length characters.
     */
    @Test
    public void shouldThrowExceptionIfPasswordIsNotEightLengthCharacters() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO,
                PASSWD_LESS_THAN_EIGHT_CHAR );
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.PASSWD_LENGTH_MUST_BE_8_CHAR.getKey(),
                MessageBundleFactory.getMessage( UserDTO.PASS_LABEL ) ) );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if password is not containing natural number.
     */
    @Test
    public void shouldThrowExceptionIfPasswordIsNotContainingNaturalNumber() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO,
                PASSWD_WITHOUT_NATURAL_NUMBER );
        thrown.expect( SusException.class );
        thrown.expectMessage( REQUIRE_ATLEAST_ONE_NATURAL_NUMBER );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if password containing eight char but is not containing natural number.
     */
    @Test
    public void shouldThrowExceptionIfPasswordContainingEightCharButIsNotContainingNaturalNumber() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO,
                PASSWD_WITHOUT_NATURAL_NUMBER );
        thrown.expect( SusException.class );
        thrown.expectMessage( REQUIRE_ATLEAST_ONE_NATURAL_NUMBER );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if password is not containing lower case alphabet.
     */
    @Test
    public void shouldThrowExceptionIfPasswordIsNotContainingLowerCaseAlphabet() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO,
                PASSWD_WITHOUT_LOWER_CASE_ALPHABET );
        thrown.expect( SusException.class );
        thrown.expectMessage( REQUIRE_ATLEAST_ONE_LOWER_CASE_ALPHABET );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * test that does not update the user and print error message when the user provided does not exists.
     */
    @Test
    public void shouldNotSuccessfullyUpdateTheUserAndThrowExceptionWhenWhenUserProvidedDoesNotExists() {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();
        thrown.expect( SusException.class );
        thrown.expectMessage( NO_ACCOUNT_FOUND_FOR_USER );
        manager.updateUser( SUPER_USER_ID, user );
    }

    /**
     * test that changes the status of the user account status to active.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessfullyActivateTheUserWhenWhenValidUserIdIsGiven() throws Exception {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        user.setSurName( SUR_NAME );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        final UserEntity expectedUserEntity = userEntity;
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false )
                .anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( userEntity ).anyTimes();
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID ) )
                .andReturn( prepareDirectoryEntity() ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( userDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity ).anyTimes();
        EasyMock.expect(
                        userTokenDAO.getUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new ArrayList< UserTokenEntity >() );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        final UserDTO actualUser = manager.updateUser( SUPER_USER_ID, user );
        assertUserData( expectedUserEntity, actualUser );
    }

    /**
     * test that changes the status of the user account status to inactive.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessfullyDeactivateTheUserWhenWhenValidUserIdIsGiven() throws Exception {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );

        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        UserEntity expectedUserEntity = userEntity;
        expectedUserEntity.setStatus( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false )
                .anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( userEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID ) )
                .andReturn( prepareDirectoryEntity() ).anyTimes();

        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect(
                        userTokenDAO.getUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new ArrayList< UserTokenEntity >() );
        expect( userDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        final UserDTO actualUser = manager.updateUser( SUPER_USER_ID, user );
        Assert.assertEquals( ACCOUNT_STATUS_IN_ACTIVE, actualUser.getStatus() );
        Assert.assertEquals( expectedUserEntity.getId(), UUID.fromString( actualUser.getId() ) );
        Assert.assertEquals( expectedUserEntity.getFirstName(), actualUser.getFirstName() );
        Assert.assertEquals( expectedUserEntity.getSurName(), actualUser.getSurName() );
        Assert.assertEquals( expectedUserEntity.isStatus(),
                actualUser.getStatus().equals( ACCOUNT_STATUS_INACTIVE_AT_DTO ) ? false : true );

        Assert.assertNotNull( actualUser.getUserDetails() );
        for ( UserDetail actualDetail : actualUser.getUserDetails() ) {
            Assert.assertEquals( actualDetail.getContacts(), expectedUserEntity.getUserDetails().iterator().next().getContacts() );
            Assert.assertEquals( actualDetail.getEmail(), expectedUserEntity.getUserDetails().iterator().next().getEmail() );
            Assert.assertEquals( actualDetail.getDesignation(), expectedUserEntity.getUserDetails().iterator().next().getDesignation() );
            Assert.assertEquals( actualDetail.getId(), expectedUserEntity.getUserDetails().iterator().next().getId().toString() );
        }
    }

    /**
     * Mock static get value by name from properties file in karaf.
     *
     * @throws Exception
     *         the exception
     */
    private void mockStaticGetValueByNameFromPropertiesFileInKaraf() throws Exception {
        PowerMockito.spy( ConfigFilePathReader.class );
        PowerMockito.doReturn( ConstantsString.TEST_RESOURCE_PATH ).when( ConfigFilePathReader.class,
                STATIC_METHOD_GET_VALUE_BY_NAME_FROM_PROPERTIES_FILE_IN_KARAF, Matchers.anyString(), Matchers.anyString() );
    }

    /**
     * Should throw exception if password containing eight char atleast one natural number but no lower case alphabet.
     */
    @Test
    public void shouldThrowExceptionIfPasswordContainingEightCharAtleastOneNaturalNumberButNoLowerCaseAlphabet() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO,
                PASSWD_WITHOUT_LOWER_CASE_ALPHABET );
        thrown.expect( SusException.class );
        thrown.expectMessage( REQUIRE_ATLEAST_ONE_LOWER_CASE_ALPHABET );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if password is not containing upper case alphabet.
     */
    @Test
    public void shouldThrowExceptionIfPasswordIsNotContainingUpperCaseAlphabet() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO,
                PASSWD_WITHOUT_UPPER_CASE_ALPHABET );
        thrown.expect( SusException.class );
        thrown.expectMessage( REQUIRE_ATLEAST_ONE_UPPER_CASE_ALPHABET );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if password containing eight char atleast one natural number and lower case alphabet but no upper case
     * alphabet.
     */
    @Test
    public void shouldThrowExceptionIfPasswordContainingEightCharAtleastOneNaturalNumberAndLowerCaseAlphabetButNoUpperCaseAlphabet() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO,
                PASSWD_WITHOUT_UPPER_CASE_ALPHABET );
        thrown.expect( SusException.class );
        thrown.expectMessage( REQUIRE_ATLEAST_ONE_UPPER_CASE_ALPHABET );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if password is null.
     */
    @Test
    public void shouldThrowExceptionIfPasswordIsNULL() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, NULL_PASSWORD );
        thrown.expect( SusException.class );
        thrown.expectMessage( PASSWORD_CANNOT_BE_NULL_OR_EMPTY );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if password is empty.
     */
    @Test
    public void shouldThrowExceptionIfPasswordIsEMPTY() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, EMPTY_PASSWORD );
        thrown.expect( SusException.class );
        thrown.expectMessage( PASSWORD_CANNOT_BE_NULL_OR_EMPTY );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception if password is containing white spaces.
     */
    @Test
    public void shouldThrowExceptionIfPasswordIsContainingWhiteSpaces() {
        fillUserModelWithDirectoryAttribute( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO,
                PASSWD_WITH_WHITE_SPACES );
        thrown.expect( SusException.class );
        thrown.expectMessage( NO_WHITE_SPCE_ALLOWED );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.createUser( SUPER_USER_ID, user );
    }

    /**
     * Should Throw Exception If User Does Not Exist In Any Directory.
     */
    @Test
    public void shouldThrowExceptionIfUserDoesNotExistInAnyDirectory() {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setStatus( ACCOUNT_STATUS_ACTIVE );
        expect( directoryManager.readDirectory( LOCAL_DIRECTORY_TYPE, DIR_ID ) ).andReturn( getfilledDirectory() ).anyTimes();

        user.getSusUserDirectoryDTO().setType( ConstantsUserDirectories.ACTIVE_DIRECTORY );
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        expect( adCustomRealm.isActiveDirectoryConnectionEstablished( EasyMock.anyObject() ) ).andReturn( true );
        expect( adCustomRealm.doGetAuthenticationInfo( EasyMock.anyObject() ) ).andReturn( null ).anyTimes();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_CREATE.getKey(), USER ) );

        mockControl.replay();
        manager.createUser( USER_ID_TEST, user );
    }

    /**
     * test which validates the user id and password and and return user on successful validation
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnUserAndPerformLoginWhenValidCredentialsAreProvided() throws Exception {
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setStatus( ACCOUNT_STATUS_ACTIVE );

        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        final UserEntity expectedUserEntity = userEntity;
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), UID ) ).andReturn( expectedUserEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        EasyMock.expect( licenseManager.isFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( getfilledInternalDirectory() ).anyTimes();
        EasyMock.expect( directoryManager.prepareDirectoryModel( EasyMock.anyObject() ) ).andReturn( getfilledInternalDirectory() )
                .anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect(
                        userDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        final UserDTO actualUser = manager.authenticate( EasyMock.anyObject( EntityManager.class ), this.user, null );

        assertUserData( expectedUserEntity, actualUser );
    }

    /**
     * test which validates the user uid from database and from AD directory and and return user on successful authentication.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnUserAndPerformLoginWhenValidCredentialsForADUserAreProvided() throws Exception {
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setStatus( ACCOUNT_STATUS_ACTIVE );

        setUpAuthenticationTokenForAD();
        SuSUserDirectoryDTO expectedDir = getfilledDirectory();

        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        final UserEntity expectedUserEntity = userEntity;
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), UID ) ).andReturn( expectedUserEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        EasyMock.expect( licenseManager.isFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( expectedDir ).anyTimes();
        EasyMock.expect( directoryManager.prepareDirectoryModel( EasyMock.anyObject() ) ).andReturn( expectedDir ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( expectedDir ).anyTimes();
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity )
                .anyTimes();
        expect( userDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        final UserDTO actualUser = manager.authenticate( EasyMock.anyObject( EntityManager.class ), this.user, null );

        assertUserData( expectedUserEntity, actualUser );
    }

    /**
     * test which validates the user id and password and throw exception when invalid user password is given.
     */
    @Test
    public void shouldThrowExceptionWithErrorMessageWhenInvalidPasswordIsGivenInLogin() {

        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setStatus( ACCOUNT_STATUS_ACTIVE );
        SuSUserDirectoryDTO expectedDir = getfilledInternalDirectory();

        fillUserModel( UID, PASSWD_WITH_WHITE_SPACES );
        user.setSusUserDirectoryDTO( expectedDir );
        EasyMock.expect( licenseManager.isFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), UID ) ).andReturn( userEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( expectedDir ).anyTimes();
        expect( directoryManager.prepareDirectoryModel( EasyMock.anyObject() ) ).andReturn( expectedDir ).anyTimes();
        mockControl.replay();
        thrown.expect( RuntimeException.class );
        thrown.expectMessage( INVALID_LOGIN_CREDENTIALS );
        manager.authenticate( EasyMock.anyObject( EntityManager.class ), user, null );

    }

    /**
     * test which validates the user can change password of local user.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessfullyChangePasswordOrLocalUserWhenPasswordStregthIsUptoTheMark() throws Exception {

        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );

        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );

        UserEntity expectedUserEntity = userEntity;
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false ).anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( userEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID ) )
                .andReturn( prepareDirectoryEntity() ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( userDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity ).anyTimes();
        List< UserTokenEntity > tokenList = new ArrayList< UserTokenEntity >();
        expect( userTokenDAO.getUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( tokenList );
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        final UserDTO actualUser = manager.updateUser( SUPER_USER_ID, user );
        assertUserData( expectedUserEntity, actualUser );
    }

    /**
     * test which validates the user can change password of local user.
     */
    @Test
    public void shouldNotChangePasswordForADUserEvenWhenPasswordStregthIsUptoTheMark() {

        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setStatus( ACCOUNT_STATUS_ACTIVE );
        expect( directoryManager.readDirectory( LOCAL_DIRECTORY_TYPE, DIR_ID ) ).andReturn( getfilledDirectoryWithInvalidSystemUserName() )
                .anyTimes();
        expect( adCustomRealm.isActiveDirectoryConnectionEstablished( EasyMock.anyObject() ) ).andReturn( false );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        thrown.expect( SusException.class );
        thrown.expectMessage( NO_ACCOUNT_FOUND_FOR_USER );
        manager.updateUser( SUPER_USER_ID, user );
    }

    /**
     * test which validates the user id and password and throw exception when invalid user id is given.
     */
    @Test
    public void shouldThrowExceptionWithErrorMessageWhenInvalidUserIdIsGivenInLogin() {

        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD_WITH_WHITE_SPACES, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME,
                DESIGNATION, CONTACT, EMAIL );

        fillUserModel( UID_WITH_GREATER_THEN_MAX, PASSWD );
        EasyMock.expect( licenseManager.isFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), UID ) ).andReturn( userEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledInternalDirectory() ).anyTimes();
        mockControl.replay();
        thrown.expect( RuntimeException.class );
        thrown.expectMessage( INVALID_LOGIN_CREDENTIALS );
        manager.authenticate( EasyMock.anyObject( EntityManager.class ), user, null );

    }

    /**
     * test that blocks the user accounts when invalid failed login attempts greater than three are performed.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldThrowExceptionWithErrorMessageWhenUserAccountGetsBlockOnInvalidAttempts() throws Exception {

        fillLoginAttemptEntity();
        fillUserModel( UID, PASSWD_WITH_WHITE_SPACES );
        SuSUserDirectoryDTO expectedDir = getfilledInternalDirectory();
        user.setSusUserDirectoryDTO( expectedDir );
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setStatus( ACCOUNT_STATUS_ACTIVE );
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), UID ) ).andReturn( userEntity ).anyTimes();
        EasyMock.expect( licenseManager.isFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        expect( userLoginAttemptDAO.getFailedLoginAttempts( EasyMock.anyObject( EntityManager.class ), UID ) )
                .andReturn( loginAttemptEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledInternalDirectory() ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( expectedDir ).anyTimes();
        EasyMock.expect( directoryManager.prepareDirectoryModel( EasyMock.anyObject() ) ).andReturn( expectedDir ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockStaticMethodOfPropertiesUtilClass();
        mockControl.replay();
        thrown.expect( RuntimeException.class );
        thrown.expectMessage( TO_MANY_ATTEMPTS_ACCOUNT_LOCKED );
        manager.authenticate( EasyMock.anyObject( EntityManager.class ), user, null );
    }

    /**
     * Mock static method of properties util class.
     *
     * @throws Exception
     *         the exception
     */
    private void mockStaticMethodOfPropertiesUtilClass() throws Exception {
        PowerMockito.spy( PropertiesManager.class );
        PowerMockito.doReturn( LOGIN_ATTEMPTS ).when( PropertiesManager.class, GET_FAILED_LOGIN_ATTEMPT );
    }

    /**
     * test that updates the user when valid user id is given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessfullyUpdateTheUserWhenValidUserIdIsGiven() throws Exception {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        final UserEntity expectedUserEntity = userEntity;
        expectedUserEntity.setGroups( getSetOfGroupsWithTwoRecord() );
        EasyMock.expect( licenseManager.isLicenseAddedAgainstUserForModule( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedUserEntity ).anyTimes();
        expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false ).anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( userEntity ).anyTimes();
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID ) )
                .andReturn( prepareDirectoryEntity() ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect( userGroupDAO.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getGroupEntity() ).anyTimes();
        expect( userDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        final UserDTO actualUser = manager.updateUser( SUPER_USER_ID, user );
        assertUserData( expectedUserEntity, actualUser );
    }

    /**
     * test that does not update the user and print error message when the user provided does not exists.
     */
    @Test
    public void shouldNotSuccessfullyUpdateTheUserWhenUserProvidedDoesNotExists() {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) ).andReturn( null )
                .anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        thrown.expect( SusException.class );
        thrown.expectMessage( NO_ACCOUNT_FOUND_FOR_USER );
        manager.updateUser( SUPER_USER_ID, user );
    }

    /**
     * Should throw exception when internal user gives invalid password for update.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldThrowExceptionWhenInternalUserGivesInvalidPasswordForUpdate() throws Exception {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD_LESS_THAN_EIGHT_CHAR );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        final UserEntity expectedUserEntity = userEntity;
        expectedUserEntity.setGroups( getSetOfGroupsWithTwoRecord() );
        EasyMock.expect( licenseManager.isLicenseAddedAgainstUserForModule( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedUserEntity ).anyTimes();
        expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false ).anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( userEntity ).anyTimes();
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect( userGroupDAO.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getGroupEntity() ).anyTimes();
        expect( userDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        thrown.expect( SusException.class );
        thrown.expectMessage( PASSWORD_INVALID_ERROR_MSG );
        manager.updateUser( SUPER_USER_ID, user );

    }

    /**
     * test that changes the status of the user account status to active.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldActivateTheUserWhenValidUserIdIsGiven() throws Exception {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        EasyMock.expect( licenseManager.isLicenseAddedAgainstUserForModule( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                        EasyMock.anyString() ) ).andReturn( true )

                .anyTimes();
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false ).anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( userEntity ).anyTimes();
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID ) )
                .andReturn( prepareDirectoryEntity() ).anyTimes();
        expect( userDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        final UserDTO actualUser = manager.updateUser( SUPER_USER_ID, user );
        Assert.assertEquals( ACCOUNT_STATUS_ACTIVE_AT_DTO, actualUser.getStatus() );
    }

    /**
     * test that changes the status of the user account status to inactive.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldDeactivateTheUserWhenValidUserIdIsGiven() throws Exception {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        UserEntity expectedUserEntity = userEntity;
        expectedUserEntity.setStatus( false );
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false ).anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( userEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID ) )
                .andReturn( prepareDirectoryEntity() ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( userDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity ).anyTimes();
        List< UserTokenEntity > tokenList = new ArrayList< UserTokenEntity >();
        expect( userTokenDAO.getUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( tokenList );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        final UserDTO actualUser = manager.updateUser( SUPER_USER_ID, user );
        Assert.assertEquals( ACCOUNT_STATUS_IN_ACTIVE, actualUser.getStatus() );
        assertUserData( expectedUserEntity, actualUser );
    }

    /**
     * test that local directory user data is changed.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldUpdateTheLocalUserWhenValidUserIdIsGiven() throws Exception {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );

        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        UserEntity expectedUserEntity = userEntity;
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect( directoryManager.prepareDirectoryEntityFromModel( EasyMock.anyObject() ) ).andReturn( userEntity.getDirectory() )
                .anyTimes();
        expect( userDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity ).anyTimes();
        expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false ).anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( expectedUserEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIR_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectoryForLocalUser( ConstantsUserDirectories.INTERNAL_DIRECTORY ) ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID ) )
                .andReturn( prepareDirectoryEntity() ).anyTimes();
        List< UserTokenEntity > tokenList = new ArrayList< UserTokenEntity >();
        expect( userTokenDAO.getUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( tokenList );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        final UserDTO actualUser = manager.updateUser( SUPER_USER_ID, user );
        assertUserData( expectedUserEntity, actualUser );
    }

    /**
     * Should Update The Local User Profile When Valid User Id Is Given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldUpdateTheLocalUserProfileWhenValidUserIdIsGiven() throws Exception {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        fillUSerProfileDto();
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        UserEntity expectedUserEntity = userEntity;
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( directoryManager.prepareDirectoryEntityFromModel( EasyMock.anyObject() ) ).andReturn( userEntity.getDirectory() )
                .anyTimes();
        expect( licenseManager.isLicenseAddedAgainstUserForModule( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false ).anyTimes();
        expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( expectedUserEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIR_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectoryForLocalUser( ConstantsUserDirectories.INTERNAL_DIRECTORY ) ).anyTimes();
        List< UserTokenEntity > tokenList = new ArrayList< UserTokenEntity >();
        expect( userTokenDAO.getUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( tokenList );
        expect( userDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity )
                .anyTimes();
        expect( documentManager.prepareEntityFromDocumentDTO( EasyMock.anyObject( DocumentDTO.class ) ) ).andReturn( new DocumentEntity() );
        expect( documentManager.getDocumentById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new DocumentDTO() );
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        mockControl.replay();
        final UserProfileDTO actualUser = manager.updateUserProfile( USER_ID_TEST, userProfile );
        assertUserProfileData( expectedUserEntity, actualUser );
    }

    /**
     * test that invalid email if provided, the user is not save.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidEmailIdProvidedToUpdateUser() {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        user.setUserDetails( mockUserDetailWithInvalidContactAndEmail( INVALID_EMAIL, CONTACT ) );
        EasyMock.expect( directoryManager.prepareDirectoryEntityFromModel( EasyMock.anyObject() ) ).andReturn( userEntity.getDirectory() )
                .anyTimes();
        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.INVALID_EMAIL.getKey() ) );
        manager.updateUser( SUPER_USER_ID, user );
    }

    /**
     * test that invalid email if provided, the user is not saved.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidContactNumberProvidedToUpdateUser() {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        user.setUserDetails( mockUserDetailWithInvalidContactAndEmail( EMAIL, INVALID_CONTACT ) );
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.INVALID_PHONE_NUMBER.getKey() ) );
        manager.updateUser( SUPER_USER_ID, user );

    }

    /**
     * test that ldap/ad user data is changed.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldUpdateTheDirectoryUserWhenValidUserIdIsGiven() throws Exception {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        user.setSurName( SUR_NAME );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        UserEntity expectedUserEntity = userEntity;
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false ).anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                        EasyMock.anyObject() ) ).andReturn( userEntity )

                .anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID ) )
                .andReturn( prepareDirectoryEntity() ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( userDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity ).anyTimes();
        List< UserTokenEntity > tokenList = new ArrayList< UserTokenEntity >();
        expect( userTokenDAO.getUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( tokenList );
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();
        final UserDTO actualUser = manager.updateUser( SUPER_USER_ID, user );
        assertUserData( expectedUserEntity, actualUser );
    }

    /**
     * test that gets the user information.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldGetTheUserWhenValidUserIdIsGiven() throws Exception {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( USER_UUID, UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        user.setUserUid( FIRSTNAME );
        UserEntity expectedUserEntity = userEntity;
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), USER_UUID ) ).andReturn( userEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity )
                .anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockLanguageCodeForUserDetails();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();
        final UserDTO actualUser = manager.getUserById( SUPER_USER_ID, USER_UUID );
        assertUserData( expectedUserEntity, actualUser );
    }

    /**
     * test that gets the user information.
     */
    @Test
    public void shouldGetAllTheUsersWhenValidFiltersAreProvided() {
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( USER_UUID, UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.getUserDetails().iterator().next().setProfilePhoto( null );

        List< UserEntity > expectedUsersList = new ArrayList< UserEntity >();
        expectedUsersList.add( userEntity );
        FiltersDTO filters = new FiltersDTO();
        filters.setDraw( ConstantsInteger.INTEGER_VALUE_ZERO );
        filters.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        filters.setLength( ConstantsInteger.INTEGER_VALUE_TWO );
        filters.setFilteredRecords( 10L );
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity )
                .anyTimes();

        expect( userDAO.getAllFilteredRecords( EasyMock.anyObject( EntityManager.class ), UserEntity.class, filters ) )
                .andReturn( expectedUsersList ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();

        FilteredResponse< UserDTO > actualResponse = manager.getAllUsers( SUPER_USER_ID, filters );
        List< UserDTO > actualUsersList = actualResponse.getData();

        Assert.assertEquals( expectedUsersList.size(), actualUsersList.size() );
        for ( int i = 0; i < expectedUsersList.size(); i++ ) {
            Assert.assertEquals( expectedUsersList.get( i ).getId(), UUID.fromString( actualUsersList.get( i ).getId() ) );
            Assert.assertEquals( expectedUsersList.get( i ).getFirstName(), actualUsersList.get( i ).getFirstName() );
            Assert.assertEquals( expectedUsersList.get( i ).isStatus(),
                    actualUsersList.get( i ).getStatus().equals( ACCOUNT_STATUS_ACTIVE_AT_DTO ) ? true : false );
            Assert.assertEquals( expectedUsersList.get( i ).getDirectory().getId(),
                    actualUsersList.get( i ).getSusUserDirectoryDTO().getId() );
        }
    }

    /**
     * test that gets the user information.
     */
    @Test
    public void shouldThrowExceptionAndDoNotGetAllUsersWhenFiltersAreNotProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );

        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        setUpAuthenticationTokenForAD();

        List< UserEntity > expectedUsersList = new ArrayList< UserEntity >();

        userEntity = fillUserEntity( USER_UUID, UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        expect( userDAO.getAllUsers( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( expectedUsersList ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();

        manager.getAllUsers( SUPER_USER_ID, null );
    }

    /**
     * test that throws exception if the user id has already been used by another user.
     */
    @Test
    public void shouldThrowExceptionAndDoNotUpdateUserWhenUserIdAlreadyExists() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.USER_ALREADY_EXIST.getKey() ) );

        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        user.setUserUid( FIRSTNAME );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( true )
                .anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( userEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();

        EasyMock.expect(
                        userTokenDAO.getUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new ArrayList< UserTokenEntity >() );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();

        manager.updateUser( SUPER_USER_ID, user );
    }

    /**
     * test that throws exception if the user id is changed.
     */
    @Test
    public void shouldThrowExceptionAndDoNotUpdateUserWhenUserIDIsChanged() {

        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );

        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        user.setUserUid( UID_CHANGED );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false )
                .anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( userEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ), DIRECTORY_ID, EasyMock.anyBoolean() ) )
                .andReturn( getfilledDirectory() ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect(
                        userTokenDAO.getUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new ArrayList< UserTokenEntity >() );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.UID_CANNOT_BE_CHANGED.getKey() ) );
        manager.updateUser( SUPER_USER_ID, user );
    }

    /**
     * test that throws exception if the user status is not valid or null.
     */
    @Test
    public void shouldThrowExceptionAndDoNotUpdateUserWhenUserStatusIsNotValid() {
        thrown.expect( SusException.class );

        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, null, PASSWD );

        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );

        user.setUserUid( UID );

        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( userDAO.isUserExist( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( false )
                .anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( userEntity ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledInternalDirectory() ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();

        EasyMock.expect(
                        userTokenDAO.getUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new ArrayList< UserTokenEntity >() );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();

        manager.updateUser( SUPER_USER_ID, user );
    }

    /**
     * test if the manager throws exception when user id is null.
     */
    @Test
    public void shouldThrowExceptionWhenEmptyORNullUserIdIsPassed() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL_OR_EMPTY.getKey() ) );

        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        user.setUserUid( UID );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), UID ) ).andReturn( userEntity ).anyTimes();

        mockControl.replay();
        manager.getUserById( SUPER_USER_ID, null );
    }

    /**
     * test if the manager throws exception when user id is null.
     */
    @Test
    public void shouldThrowExceptionWhenUserDoesNotExist() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );

        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_IN_ACTIVE, PASSWD );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        user.setUserUid( UID );
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), USER_UUID.toString() ) ).andReturn( userEntity )
                .anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.getUserById( SUPER_USER_ID, USER_UUID );
    }

    /**
     * test that updates the user password when old and confirm password are matched.
     */
    @Test
    public void shouldSuccessfullyUpdatePasswordWhenNewPasswordAndConfirmPasswordsAreMatched() {
        UserPasswordDTO userPasswordDTO = prepareUserPasswordDTO( PASSWD, PASSWD, PASSWD );
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setPassword( ENCRYPTED_PASSWORD );
        userEntity.setSurName( SUR_NAME );
        final UserEntity expectedUserEntity = userEntity;
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedUserEntity ).anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( expectedUserEntity ).anyTimes();
        mockControl.replay();
        final String actualMessage = manager.updateUserPassword( user.getId(), userPasswordDTO );
        Assert.assertEquals( actualMessage, MessageBundleFactory.getMessage( Messages.PASSWORD_CHANGED_SUCCESSFULLY.getKey() ) );
    }

    /**
     * test that does not update password and throw exception when confirm and new passwords are not matched.
     */
    @Test
    public void shouldNotSuccessfullyUpdatePasswordWhenNewPasswordAndConfirmPasswordsAreNotMatched() {
        UserPasswordDTO userPasswordDTO = prepareUserPasswordDTO( PASSWD, PASSWD, PASSWD_WITHOUT_UPPER_CASE_ALPHABET );
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );
        setUpAuthenticationTokenForAD();
        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setPassword( ENCRYPTED_PASSWORD );
        userEntity.setSurName( SUR_NAME );
        final UserEntity expectedUserEntity = userEntity;

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.PASSWORD_AND_CONFIRM_PASSWORD_DOES_NOT_MATCHED.getKey() ) );
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedUserEntity ).anyTimes();
        EasyMock.expect( userDAO.updateUserEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ),
                EasyMock.anyObject() ) ).andReturn( expectedUserEntity ).anyTimes();
        mockControl.replay();
        manager.updateUserPassword( user.getId(), userPasswordDTO );
    }

    /**
     * Should SuccessFully Get Edit User Form Items With Filled User Dto.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessFullyGetEditUserFormItemsWithFilledUserDto() throws Exception {
        List< UIFormItem > expected = prepareEditDummyForm();

        fillUserModel( USER_UUID.toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );

        userEntity = fillUserEntity( USER_UUID, UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setPassword( ENCRYPTED_PASSWORD );
        userEntity.setSurName( SUR_NAME );

        final UserEntity expectedUserEntity = userEntity;
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledDirectory() ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedUserEntity ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity )
                .anyTimes();
        expect( userDAO.getGroupsByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( getListOfGroups() ).anyTimes();
        expect( userDAO.getAllGroups( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( getListOfGroups() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();

        // List< UIFormItem > actual = manager.editUserForm( SUPER_USER_ID, USER_UUID );
        // Assert.assertNotNull( actual );
        // for ( UIFormItem actualItem : actual ) {
        // for ( UIFormItem expectedItem : expected ) {
        // if ( expectedItem.getName().equals( actualItem.getName() ) && !expectedItem.getLabel().equals( UserDTO.LABEL_GROUPS ) ) {
        // Assert.assertEquals( expectedItem.getValue(), actualItem.getValue() );
        //
        // }
        // }
        //
        // }

    }

    /**
     * Should SuccessFully Get Create User Form Items With Filled UserDto.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessFullyGetCreateUserFormItemsWithFilledUserDto() throws Exception {
        List< UIFormItem > expected = prepareCreateUserDummyForm();

        fillUserModel( USER_UUID.toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );

        userEntity = fillUserEntity( USER_UUID, UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setPassword( ENCRYPTED_PASSWORD );
        userEntity.setSurName( SUR_NAME );

        final UserEntity expectedUserEntity = userEntity;
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledDirectory() ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedUserEntity ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( userDAO.getGroupsByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( getListOfGroups() ).anyTimes();
        expect( userDAO.getAllGroups( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( getListOfGroups() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        // List< UIFormItem > actual = manager.createUserForm( SUPER_USER_ID, DIR_ID );
        // Assert.assertNotNull( actual );
        // for ( UIFormItem actualItem : actual ) {
        // for ( UIFormItem expectedItem : expected ) {
        // if ( expectedItem.getName().equals( actualItem.getName() ) && !expectedItem.getLabel().equals( UserDTO.LABEL_GROUPS )
        // && !expectedItem.getLabel().equalsIgnoreCase( STATUS ) ) {
        // Assert.assertEquals( expectedItem.getValue(), actualItem.getValue() );
        //
        // }
        // }
        //
        // }

    }

    /**
     * Should SuccessFully Get Edit User Profile Form List Items With Filled User Dto.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessFullyGetEditUserProfileFormListItemsWithFilledUserDto() throws Exception {
        List< UIFormItem > expected = prepareEditProfileDummyForm();
        fillUserModel( USER_UUID.toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        userEntity = fillUserEntity( USER_UUID, UID, FIRSTNAME, PASSWD, DIRECTORY_ID, IS_CHANGEABLE, LASTNAME, DESIGNATION, CONTACT,
                EMAIL );
        userEntity.setPassword( ENCRYPTED_PASSWORD );
        userEntity.setSurName( SUR_NAME );
        final UserEntity expectedUserEntity = userEntity;
        expect( userDAO.getGroupsByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( getListOfGroups() ).anyTimes();
        expect( userDAO.getAllGroups( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( getListOfGroups() ).anyTimes();
        expect( directoryManager.readDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( getfilledDirectory() ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedUserEntity ).anyTimes();
        expect( directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        expect( userDAO.getUserByUid( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) ).andReturn( userEntity )
                .anyTimes();
        expect( userDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        // List< UIFormItem > actual = manager.editUserProfileForm( USER_UUID );
        // Assert.assertNotNull( actual );
        // for ( UIFormItem actualItem : actual ) {
        // for ( UIFormItem expectedItem : expected ) {
        // if ( expectedItem.getName().equals( actualItem.getName() ) && !expectedItem.getLabel().equals( UserDTO.LABEL_GROUPS )
        // && !expectedItem.getLabel().equalsIgnoreCase( STATUS ) ) {
        // Assert.assertEquals( expectedItem.getValue(), actualItem.getValue() );
        //
        // }
        // }
        //
        // }

    }

    /**
     * should Get List Of Columns For Users.
     */
    @Test
    public void shouldGetListOfColumnsForUsers() {
        List< TableColumn > actual = manager.listUsersUI();
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual.isEmpty() );
    }

    /**
     * should Get List Of Columns For Users.
     */
    @Test
    public void shouldGetListOfColumnsForSingleUserProfileView() {
        List< TableColumn > actual = manager.singleUserUI();
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual.isEmpty() );
    }

    /**
     * Should throw exception when feature is not allowed.
     */
    @Test
    public void shouldThrowExceptionWhenFeatureIsNotAllowed() {
        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.FEATURE_NOT_ALLOWED_TO_USER.getKey(), SimuspaceFeaturesEnum.USERS.getKey() ) );
        EasyMock.expect( licenseManager.checkIfFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( String.class ) ) ).andReturn( false ).anyTimes();
        EasyMock.replay( licenseManager );
        manager.deleteUserBySelection( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID, UUID.randomUUID().toString(),
                ConstantsString.EMPTY_STRING );
    }

    /**
     * Should throw exception when invalid mode provided.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidModeProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), ConstantsString.EMPTY_STRING ) );
        EasyMock.expect( licenseManager.checkIfFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( String.class ) ) ).andReturn( true ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.replay( licenseManager );
        manager.deleteUserBySelection( SUPER_USER_ID, UUID.randomUUID().toString(), ConstantsString.EMPTY_STRING );
    }

    /**
     * Should throw exception when invalid selection id provided for users.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidSelectionIdProvidedForUsers() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.REQUEST_ID_NOT_VALID.getKey() ) );
        EasyMock.expect( userDAO.readUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) ).andReturn( null )
                .anyTimes();
        EasyMock.expect( licenseManager.checkIfFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( String.class ) ) ).andReturn( true ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.replay( userDAO );

        manager.deleteUserBySelection( SUPER_USER_ID, UUID.randomUUID().toString(), ConstantsMode.SINGLE );
    }

    /**
     * Should throw exception when project reference exist in DB.
     */
    @Test
    public void shouldThrowExceptionWhenProjectReferenceExistInDB() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.USER_CANNOT_NOT_BE_DELETED.getKey() ) );
        List< SuSEntity > objectListByProperty = new ArrayList<>();
        objectListByProperty.add( new ProjectEntity() );
        EasyMock.expect( userDAO.readUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( prepareUserEntity() ).anyTimes();
        EasyMock.expect( licenseManager.checkIfFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( String.class ) ) ).andReturn( true ).anyTimes();
        EasyMock.expect( susDAO.getObjectListByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( String.class ),
                EasyMock.anyObject( String.class ) ) ).andReturn( objectListByProperty ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.replay( userDAO );
        EasyMock.replay();
        manager.deleteUserBySelection( SUPER_USER_ID, UUID.randomUUID().toString(), ConstantsMode.SINGLE );
    }

    /**
     * Should return all the selected selections with pagination of given selection id.
     */
    public void shouldReturnAllTheSelectedSelectionsWithPaginationOfGivenSelectionId() {
        SelectionItemEntity selectionItemEntity = new SelectionItemEntity();
        selectionItemEntity.setId( USER_ID );
        selectionItemEntity.setItem( ITEM_ID_ONE.toString() );
        List< SelectionItemEntity > selectedUserIds = new ArrayList<>();

        userEntity = fillUserEntity( UUID.randomUUID(), UID, FIRSTNAME, PASSWD_WITHOUT_LOWER_CASE_ALPHABET, DIRECTORY_ID, IS_CHANGEABLE,
                LASTNAME, DESIGNATION, CONTACT, EMAIL );
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID_WITH_GREATER_THEN_MAX, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        List< UserDTO > userDTOList = new ArrayList<>();
        userDTOList.add( user );

        EasyMock.expect(
                        directoryManager.prepareDirectoryModelFromEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                                EasyMock.anyBoolean() ) )
                .andReturn( user.getSusUserDirectoryDTO() ).anyTimes();
        EasyMock.expect( selectionManager.getUserSelectionsBySelectionIds( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject() ) ).andReturn( selectedUserIds ).anyTimes();
        EasyMock.expect(
                        userDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new UserEntity() ).anyTimes();
        mockControl.replay();
    }

    /**
     * Should deleted successfully when no user dependency provided.
     */
    @Test
    public void shouldDeletedSuccessfullyWhenNoUserDependencyProvided() {
        mockInstances();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.replay( userDAO );
        Assert.assertTrue( manager.deleteUserBySelection( SUPER_USER_ID, UUID.randomUUID().toString(), ConstantsMode.SINGLE ) );
    }

    /**
     * Should deleted successfully when bulk of users with no dependency provided.
     */
    @Test
    public void shouldDeletedSuccessfullyWhenBulkOfUsersWithNoDependencyProvided() {
        mockInstances();
        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( manager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.replay( userDAO );
        Assert.assertTrue( manager.deleteUserBySelection( SUPER_USER_ID, UUID.randomUUID().toString(), ConstantsMode.BULK ) );
    }

    @Test
    public void shouldSuccessfullyCheckPermission() {

        UserEntity userEntity = prepareUserEntity();
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        userEntity.setChangeable( false );
        EasyMock.expect( userDAO.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect(
                        userDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect(
                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( true ).anyTimes();
        mockControl.replay();
        manager.isPermitted( EasyMock.anyObject( EntityManager.class ), SUPER_USER_ID, SimuspaceFeaturesEnum.USERS.getId(),
                PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), USER );

    }

    @Test
    public void shouldThrowExceptionWhenNotPermitted() {
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect(
                        userDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect(
                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( false ).anyTimes();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_CREATE.getKey(), USER ) );
        mockControl.replay();
        manager.isPermitted( EasyMock.anyObject( EntityManager.class ), USER_ID_TEST, SimuspaceFeaturesEnum.USERS.getId(),
                PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), USER );
    }

    /**
     * Should SuccessFully Get Edit User Profile Form List Items With Filled User Dto.
     */
    @Test
    public void shouldNotProvideContextMenuAndReturnEmptyListWhenSuperUserRecordSelected() {

        List< ContextMenuItem > actual = manager.getContextRouter( getFilterDTOForSuperUser() );
        Assert.assertEquals( expectedListFromContextMenu(), actual );

    }

    /**
     * ****************************************** HELPER METHODS *************************************************.
     *
     *
     */

    /**
     * Prepare Expected Form Items.
     *
     * @return the list
     */

    private List< UIFormItem > prepareEditDummyForm() {
        fillUserModel( USER_UUID.toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );

        return GUIUtils.prepareForm( false, user );

    }

    /**
     * Expected list from context menu.
     *
     * @return the list empty arrayList for admin context menu in user grid view
     */
    private List expectedListFromContextMenu() {
        return new ArrayList();
    }

    /**
     * Gets the filter DTO for super user.
     *
     * @return the filter DTO
     */
    private FiltersDTO getFilterDTOForSuperUser() {
        List< Object > uuids = new ArrayList<>();
        uuids.add( ConstantsID.SUPER_USER_ID );
        FiltersDTO filtersDTO = new FiltersDTO();
        filtersDTO.setItems( uuids );
        return filtersDTO;
    }

    /**
     * Prepare create user dummy form.
     *
     * @return the list
     */
    private List< UIFormItem > prepareCreateUserDummyForm() {
        fillUserModel( USER_UUID.toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );

        return GUIUtils.prepareForm( false, new UserDTO() );

    }

    /**
     * Prepare Edit Profile Dummy Form Items.
     *
     * @return the list
     */
    private List< UIFormItem > prepareEditProfileDummyForm() {
        fillUserModel( USER_UUID.toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );
        user.setSurName( SUR_NAME );
        user.setGroups( null );
        return GUIUtils.prepareForm( false, user );

    }

    /**
     * Fill user entity.
     *
     * @param Id
     *         the id
     * @param uid
     *         the uid
     * @param userName
     *         the user name
     * @param passwd
     *         the passwd
     * @param dirId
     *         the dir id
     * @param isChangeable
     *         the is changeable
     * @param lastName
     *         the last name
     * @param designation
     *         the designation
     * @param contact
     *         the contact
     * @param email
     *         the email
     *
     * @return the user entity
     */
    private UserEntity fillUserEntity( UUID Id, String uid, String userName, String passwd, UUID dirId, boolean isChangeable,
            String lastName, String designation, String contact, String email ) {
        UserEntity userEntity = new UserEntity( Id, uid, userName, passwd, prepareDirectoryEntity(), isChangeable );
        userEntity.setStatus( ACCOUNT_STATUS_ACTIVE );
        userEntity.setRestricted( false );
        userEntity.setId( Id );
        userEntity.setUserDetails( fillUserDetailEntity( Id, email, contact, designation ) );
        userEntity.setPassword( FileUtils.getSha256CheckSum( passwd ) );
        userEntity.setGroups( getSetOfGroupsWithOneREecord() );
        return userEntity;
    }

    /**
     * Gets the sets the of groups with one R eecord.
     *
     * @return the sets the of groups with one R eecord
     */
    private Set< GroupEntity > getSetOfGroupsWithOneREecord() {
        Set< GroupEntity > set = new HashSet<>();
        set.add( getGroupEntity() );
        return set;
    }

    /**
     * Gets the sets the of groups with two record.
     *
     * @return the sets the of groups with two record
     */
    private Set< GroupEntity > getSetOfGroupsWithTwoRecord() {
        Set< GroupEntity > set = new HashSet<>();
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId( GROUP_ID_3 );
        groupEntity.setName( NAME_FIELD );
        groupEntity.setDescription( DESCRIPTION_FIELD );
        groupEntity.setStatus( ACTIVE_GRP );

        set.add( groupEntity );

        GroupEntity groupEntity2 = new GroupEntity();
        groupEntity2.setId( GROUP_ID_2 );
        groupEntity2.setName( NAME_FIELD );
        groupEntity2.setDescription( DESCRIPTION_FIELD );
        groupEntity2.setStatus( ACTIVE_GRP );

        set.add( groupEntity2 );

        return set;
    }

    /**
     * Gets the list of groups.
     *
     * @return the list of groups
     */
    private List< GroupEntity > getListOfGroups() {
        List< GroupEntity > set = new ArrayList<>();
        set.add( getGroupEntity() );
        return set;
    }

    /**
     * Get Group Entity.
     *
     * @return the group entity
     */
    private GroupEntity getGroupEntity() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId( GROUP_ID );
        groupEntity.setName( NAME_FIELD );
        groupEntity.setDescription( DESCRIPTION_FIELD );
        groupEntity.setStatus( ACTIVE_GRP );

        return groupEntity;
    }

    /**
     * Fill user detail entity.
     *
     * @param id
     *         the id
     * @param email
     *         the email
     * @param contacts
     *         the contacts
     * @param designation
     *         the designation
     *
     * @return the sets the
     */
    private Set< UserDetailEntity > fillUserDetailEntity( UUID id, String email, String contacts, String designation ) {
        Set< UserDetailEntity > list = new HashSet<>();

        UserDetailEntity userDetailEntity = new UserDetailEntity( USER_DETAIL_ID, designation, contacts, email, DEPARTMENT, "en",
                getByteArrayFromImage() );
        EasyMock.expect( userDetailDAO.getUserDetailById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDetailEntity ).anyTimes();
        list.add( userDetailEntity );
        return list;
    }

    /**
     * A method for filling user entity.
     */
    private void fillLoginAttemptEntity() {
        loginAttemptEntity = new LoginAttemptEntity();
        loginAttemptEntity.setUuid( UUID.randomUUID() );
        loginAttemptEntity.setUid( UID );
        loginAttemptEntity.setAttempts( 9 );
        loginAttemptEntity.setLastMofied( new Date() );
    }

    /**
     * A method for filling user model.
     *
     * @param id
     *         the id
     * @param firstName
     *         the first name
     * @param UID
     *         the uid
     * @param accountStatus
     *         the account status
     * @param password
     *         the password
     */
    private void fillUserModel( String id, String firstName, String UID, String accountStatus, String password ) {
        user = new UserDTO();
        user.setId( id );
        user.setFirstName( firstName );
        user.setPassword( password );
        user.setUserUid( UID );
        user.setSusUserDirectoryDTO( prepareDirectoryDTO() );
        user.setStatus( accountStatus );
        user.setRestricted( IS_RESTRICTED );
        user.setUserDetails( getFilleduserDetail() );
        user.setSurName( SUR_NAME );
        user.setName( user.getUserUid() );
        user.setGroups( fillGroups() );
        user.setChangable( IS_CHANGEABLE );

    }

    private void fillUserModelWithDirectoryAttribute( String id, String firstName, String UID, String accountStatus, String password ) {
        user = new UserDTO();
        user.setId( id );
        user.setFirstName( firstName );
        user.setPassword( password );
        user.setUserUid( UID );
        user.setSusUserDirectoryDTO( prepareDirectoryDTOWithAttrribute() );
        user.setStatus( accountStatus );
        user.setRestricted( IS_RESTRICTED );
        user.setUserDetails( getFilleduserDetail() );
        user.setSurName( SUR_NAME );
        user.setName( user.getUserUid() );
        user.setGroups( fillGroups() );
        user.setChangable( IS_CHANGEABLE );
    }

    /**
     * Fill groups.
     *
     * @return the list
     */
    private List< SuSUserGroupDTO > fillGroups() {
        List< SuSUserGroupDTO > groups = new ArrayList<>();
        groups.add( fillUserGroupDto() );
        return groups;
    }

    /**
     * Fill user group dto.
     *
     * @return the su S user group DTO
     */
    private SuSUserGroupDTO fillUserGroupDto() {
        SuSUserGroupDTO susGroupDto = new SuSUserGroupDTO();
        susGroupDto.setId( GROUP_ID );
        susGroupDto.setName( NAME_FIELD );
        susGroupDto.setDescription( DESCRIPTION_FIELD );
        susGroupDto.setStatus( ACTIVE );

        return susGroupDto;

    }

    /**
     * filled the user detail model.
     *
     * @return the filleduser detail
     */
    private List< UserDetail > getFilleduserDetail() {
        List< UserDetail > list = new ArrayList<>();

        UserDetail userDetail = new UserDetail( USER_DETAIL_ID.toString(), DESIGNATION, CONTACT, EMAIL, DEPARTMENT,
                LANGUAGE_UUID.toString() );
        list.add( userDetail );
        return list;
    }

    /**
     * helper function to mock user details.
     *
     * @param email
     *         the email
     * @param contact
     *         the contact
     *
     * @return the list
     */
    private List< UserDetail > mockUserDetailWithInvalidContactAndEmail( String email, String contact ) {
        List< UserDetail > list = new ArrayList<>();

        UserDetail userDetail = new UserDetail( USER_DETAIL_ID.toString(), DESIGNATION, contact, email, DEPARTMENT,
                LANGUAGE_UUID.toString() );
        list.add( userDetail );
        return list;
    }

    /**
     * A method for filling user model.
     *
     * @param uid
     *         the uid
     * @param password
     *         the password
     */
    private void fillUserModel( String uid, String password ) {
        user = new UserDTO( uid, password );
    }

    /**
     * A method for filling directory model.
     *
     * @return the filled directory
     */
    private SuSUserDirectoryDTO getfilledDirectory() {
        SuSUserDirectoryDTO dir = new SuSUserDirectoryDTO();
        dir.setId( DIRECTORY_ID );
        dir.setUserDirectoryAttribute(
                prepareDirectoryAttributes( VALID_SEARCH_BASE_AD, VALID_URL_AD, VALID_SYSTEM_USERNAME_AD, VALID_SYSTEM_PASSWORD_AD ) );
        dir.setType( ConstantsUserDirectories.ACTIVE_DIRECTORY );
        dir.setStatus( ConstantsStatus.ACTIVE );
        dir.setCreatedOn( DateFormatStandard.format( new Date() ) );
        return dir;
    }

    /**
     * Prepare directory DTO.
     *
     * @return the su S user directory DTO
     */
    public SuSUserDirectoryDTO prepareDirectoryDTO() {
        SuSUserDirectoryDTO directoryDTO = new SuSUserDirectoryDTO();
        directoryDTO.setId( DIRECTORY_ID );
        directoryDTO.setType( ConstantsUserDirectories.INTERNAL_DIRECTORY );
        return directoryDTO;
    }

    public SuSUserDirectoryDTO prepareDirectoryDTOWithAttrribute() {
        SuSUserDirectoryDTO directoryDTO = new SuSUserDirectoryDTO();
        directoryDTO.setId( DIRECTORY_ID );
        directoryDTO.setUserDirectoryAttribute(
                prepareDirectoryAttributes( VALID_SEARCH_BASE_AD, VALID_URL_AD, VALID_SYSTEM_USERNAME_AD, VALID_SYSTEM_PASSWORD_AD ) );
        directoryDTO.setType( ConstantsUserDirectories.INTERNAL_DIRECTORY );
        return directoryDTO;
    }

    /**
     * A method for filling directory model.
     *
     * @return the filled internal directory
     */
    private SuSUserDirectoryDTO getfilledInternalDirectory() {
        SuSUserDirectoryDTO dir = new SuSUserDirectoryDTO();
        dir.setId( DIRECTORY_ID );
        dir.setUserDirectoryAttribute(
                prepareDirectoryAttributes( VALID_SEARCH_BASE_AD, VALID_URL_AD, VALID_SYSTEM_USERNAME_AD, VALID_SYSTEM_PASSWORD_AD ) );
        dir.setType( ConstantsUserDirectories.INTERNAL_DIRECTORY );
        dir.setStatus( ConstantsStatus.ACTIVE );
        dir.setCreatedOn( DateFormatStandard.format( new Date() ) );
        return dir;
    }

    /**
     * A method for filling directory model.
     *
     * @param directoryType
     *         the directory type
     *
     * @return the filled directory for local user
     */
    private SuSUserDirectoryDTO getfilledDirectoryForLocalUser( String directoryType ) {
        SuSUserDirectoryDTO dir = new SuSUserDirectoryDTO();
        dir.setId( DIRECTORY_ID );
        dir.setUserDirectoryAttribute(
                prepareDirectoryAttributes( VALID_SEARCH_BASE_AD, VALID_URL_AD, VALID_SYSTEM_USERNAME_AD, VALID_SYSTEM_PASSWORD_AD ) );
        dir.setType( directoryType );
        return dir;
    }

    /**
     * A method for filling directory model with Invalid Credential.
     *
     * @return the filled directory with invalid system user name
     */
    private SuSUserDirectoryDTO getfilledDirectoryWithInvalidSystemUserName() {
        SuSUserDirectoryDTO dir = new SuSUserDirectoryDTO();
        dir.setId( DIR_ID );
        dir.setUserDirectoryAttribute(
                prepareDirectoryAttributes( VALID_SEARCH_BASE_AD, INVALID_URL_AD, INVALID_AD_SYSTEM_NAME, INVALID_AD_SYSTEM_PASSWORD ) );
        dir.setType( ConstantsUserDirectories.ACTIVE_DIRECTORY );
        return dir;
    }

    /**
     * Assert user data.
     *
     * @param expectedUserEntity
     *         the expected user entity
     * @param actualUser
     *         the actual user
     */
    private void assertUserData( UserEntity expectedUserEntity, UserDTO actualUser ) {
        Assert.assertEquals( expectedUserEntity.getId(), UUID.fromString( actualUser.getId() ) );
        Assert.assertEquals( expectedUserEntity.getFirstName(), actualUser.getFirstName() );
        Assert.assertEquals( expectedUserEntity.isStatus(), actualUser.getStatus().equals( ACCOUNT_STATUS_ACTIVE_AT_DTO ) ? true : false );

        Assert.assertNotNull( actualUser.getUserDetails() );
        for ( UserDetail actualDetail : actualUser.getUserDetails() ) {
            Assert.assertEquals( actualDetail.getId(), USER_DETAIL_ID.toString() );
            Assert.assertEquals( expectedUserEntity.getUserDetails().iterator().next().getContacts(), actualDetail.getContacts() );
            Assert.assertEquals( expectedUserEntity.getUserDetails().iterator().next().getEmail(), actualDetail.getEmail() );
            Assert.assertEquals( expectedUserEntity.getUserDetails().iterator().next().getDesignation(), actualDetail.getDesignation() );
            Assert.assertEquals( expectedUserEntity.getUserDetails().iterator().next().getDepartment(), actualDetail.getDepartment() );
            Assert.assertEquals( expectedUserEntity.getUserDetails().iterator().next().getLanguage(),
                    UUID.fromString( actualDetail.getLanguage() ) );

        }
    }

    /**
     * Assert user profile data.
     *
     * @param expectedUserEntity
     *         the expected user entity
     * @param actualUser
     *         the actual user
     */
    private void assertUserProfileData( UserEntity expectedUserEntity, UserProfileDTO actualUser ) {
        Assert.assertEquals( expectedUserEntity.getId(), UUID.fromString( actualUser.getId() ) );
        Assert.assertEquals( expectedUserEntity.getSurName(), actualUser.getSurName() );

        Assert.assertNotNull( actualUser.getUserDetails() );
        for ( UserDetail actualDetail : actualUser.getUserDetails() ) {
            Assert.assertEquals( actualDetail.getId(), USER_DETAIL_ID.toString() );
            Assert.assertEquals( expectedUserEntity.getUserDetails().iterator().next().getContacts(), actualDetail.getContacts() );
            Assert.assertEquals( expectedUserEntity.getUserDetails().iterator().next().getEmail(), actualDetail.getEmail() );
            Assert.assertEquals( expectedUserEntity.getUserDetails().iterator().next().getDesignation(), actualDetail.getDesignation() );
            Assert.assertEquals( expectedUserEntity.getUserDetails().iterator().next().getDepartment(), actualDetail.getDepartment() );
            Assert.assertEquals( expectedUserEntity.getUserDetails().iterator().next().getLanguage(),
                    UUID.fromString( actualDetail.getLanguage() ) );

        }
    }

    /**
     * A method to mock language code for user details.
     */
    private void mockLanguageCodeForUserDetails() {
        List< LanguageEntity > languageEntityList = new ArrayList<>();
        languageEntityList.add( languageEntity );
        EasyMock.expect( languageDAO.getLanguageById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( languageEntity ).anyTimes();
    }

    /**
     * A helper function for getting profileImage string of a temp image.
     *
     * @return profile image path
     */
    private String getProfileImagePath() {
        return ConstantsString.TEST_RESOURCE_PATH + "user.png";
    }

    /**
     * A helper function for getting profileImage bytes of a temp image.
     *
     * @return byte array
     */
    private byte[] getByteArrayFromImage() {
        File file = new File( getProfileImagePath() );
        BufferedImage originalImage;
        byte[] byteArray = null;
        try {
            originalImage = ImageIO.read( file );
            byteArray = ImageUtil.toByteArray( originalImage, "png" );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return byteArray;
    }

    /**
     * A method to prepare directory attributes.
     *
     * @param searchBase
     *         the search base
     * @param url
     *         the url
     * @param userName
     *         the user name
     * @param password
     *         the password
     *
     * @return SusUserDirectoryAttribute
     */
    private SusUserDirectoryAttributeDTO prepareDirectoryAttributes( String searchBase, String url, String userName, String password ) {
        SusUserDirectoryAttributeDTO attributes = new SusUserDirectoryAttributeDTO();
        attributes.setSearchBase( searchBase );
        attributes.setUrl( url );
        attributes.setSystemUsername( userName );
        attributes.setSystemPassword( password );
        return attributes;
    }

    /**
     * Prepare directory entity from model.
     *
     * @return the sus user directory entity
     */
    private SuSUserDirectoryEntity prepareDirectoryEntity() {

        SuSUserDirectoryEntity directoryEntity = new SuSUserDirectoryEntity();
        directoryEntity.setId( DIRECTORY_ID );
        directoryEntity.setType( ConstantsUserDirectories.INTERNAL_DIRECTORY );
        directoryEntity.setStatus( ACCOUNT_STATUS_ACTIVE );

        return directoryEntity;
    }

    /**
     * A method to prepare userPasswordDTO.
     *
     * @param oldPassword
     *         the old password
     * @param newPassword
     *         the new password
     * @param confirmPassword
     *         the confirm password
     *
     * @return the user password DTO
     */
    private UserPasswordDTO prepareUserPasswordDTO( String oldPassword, String newPassword, String confirmPassword ) {
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setOldPassword( oldPassword );
        userPasswordDTO.setNewPassword( newPassword );
        userPasswordDTO.setConfirmPassword( confirmPassword );
        return userPasswordDTO;
    }

    /**
     * A method to prepare User Profile Dto.
     */
    private void fillUSerProfileDto() {
        userProfile = new UserProfileDTO();
        fillUserModel( UUID.randomUUID().toString(), FIRSTNAME, UID, ACCOUNT_STATUS_ACTIVE_AT_DTO, PASSWD );

        userProfile.setId( user.getId() );
        userProfile.setType( user.getType() );
        userProfile.setUid( user.getUserUid() );

        userProfile.setFirstName( user.getFirstName() );
        userProfile.setSurName( user.getSurName() );

        UserPasswordDTO pwd = new UserPasswordDTO();
        pwd.setNewPassword( PASSWD );
        pwd.setConfirmPassword( PASSWD );
        pwd.setOldPassword( PASSWD );

        userProfile.setUserPasswordDto( pwd );
        userProfile.setUserDetails( user.getUserDetails() );

    }

    /**
     * Fill security identity.
     *
     * @param userEntity
     *         the user entity
     *
     * @return the security identity entity
     */
    private AclSecurityIdentityEntity fillSecurityIdentity( UserEntity userEntity ) {
        AclSecurityIdentityEntity securityIdentityEntity = new AclSecurityIdentityEntity();
        securityIdentityEntity.setId( UUID.randomUUID() );
        securityIdentityEntity.setSid( userEntity.getId() );
        securityIdentityEntity.setPrinciple( Boolean.TRUE );
        return securityIdentityEntity;
    }

    /**
     * Prepare user entity.
     *
     * @return the user entity
     */
    private UserEntity prepareUserEntity() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( SUPER_USER_ID ) );
        userEntity.setFirstName( NAME_FIELD );
        userEntity.setSurName( NAME_FIELD );
        userEntity.setDirectory( prepareDirectoryEntity() );

        return userEntity;
    }

    /**
     * Gets the filter DTO.
     *
     * @return the filter DTO
     */
    private FiltersDTO getFilterDTO() {
        List< Object > uuids = new ArrayList<>();
        uuids.add( UUID.randomUUID() );
        FiltersDTO filtersDTO = new FiltersDTO();
        filtersDTO.setItems( uuids );
        return filtersDTO;
    }

    /**
     * Mock instances.
     */
    private void mockInstances() {
        List< GroupEntity > groupEntities = new ArrayList<>();
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setStatus( true );
        groupEntity.setUsers( new HashSet<>() );
        groupEntities.add( groupEntity );
        List< UserTokenEntity > userTokenList = new ArrayList<>();
        userTokenList.add( new UserTokenEntity() );

        EasyMock.expect( userDAO.readUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( prepareUserEntity() ).anyTimes();
        EasyMock.expect( licenseManager.checkIfFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( String.class ) ) ).andReturn( true ).anyTimes();
        EasyMock.expect( susDAO.getObjectListByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( String.class ),
                EasyMock.anyObject( String.class ) ) ).andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect(
                        userGroupManager.getUserGroupsByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( groupEntities ).anyTimes();
        EasyMock.expect( userGroupDAO.saveOrUpdate( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( GroupEntity.class ) ) )
                .andReturn( groupEntity ).anyTimes();
        EasyMock.expect( userTokenDAO.getAllUserTokenEntityListByUserId( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( userTokenList ).anyTimes();
        EasyMock.expect( userDAO.getGroupAndUsersByGroupId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( groupEntity ).anyTimes();
        EasyMock.expect( licenseManager.getLicenseConfigManager() ).andReturn( licenseConfigManager ).anyTimes();
        EasyMock.expect(
                        contextMenuManager.getFilterBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( String.class ) ) )
                .andReturn( getFilterDTO() ).anyTimes();
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
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

}