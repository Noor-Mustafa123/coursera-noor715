package de.soco.software.simuspace.suscore.user.manager.Impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.auth.connect.ad.authentication.ActiveDirectoryCustomRealm;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.FormItemFactory;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserDirectoryAttributeEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.user.dao.DirectoryDAO;
import de.soco.software.simuspace.suscore.user.dao.UserDirectoryAttributeDAO;
import de.soco.software.simuspace.suscore.user.manager.UserManager;
import de.soco.software.simuspace.suscore.user.manager.impl.DirectoryManagerImpl;

/**
 * The Class is responsible for testing the public functions of {@link DirectoryManagerImpl}.
 *
 * @author M.Nasir.Farooq
 */
public class DirectoryManagerImplTest {

    /**
     * The Constant INTERNAL_DIRECTORY_OPTION.
     */
    private static final String INTERNAL_DIRECTORY_OPTION = "Internal Directory";

    /**
     * The Constant LDAP_DIRECTORY_OPTION.
     */
    private static final String LDAP_DIRECTORY_OPTION = "Ldap Directory";

    /**
     * The Constant MACHINE_USER_DIRECTORY_OPTION.
     */
    private static final String MACHINE_USER_DIRECTORY_OPTION = "Machine User Directory";

    /**
     * The Constant ACTIVE_DIRECTORY_OPTION.
     */
    private static final String ACTIVE_DIRECTORY_OPTION = "Active Directory";

    /**
     * The Constant WRONG_OPTION_INPUT.
     */
    private static final String WRONG_OPTION_INPUT = "abc";

    /**
     * The Constant NAME_LENGTH_GREATER_THEN_ALLOWED.
     */
    private static final String NAME_LENGTH_GREATER_THEN_ALLOWED = "shhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";

    /**
     * The Constant DESCRIPTION_LENGTH_GREATER_THEN_ALLOWED.
     */
    private static final String DESCRIPTION_LENGTH_GREATER_THEN_ALLOWED = "shhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";

    /**
     * Dummy UserId for test Cases.
     */
    private static final String DEFAULT_USER_ID = "Super User";

    /**
     * The Constant SIMUSPACE.
     */
    private static final String SIMUSPACE = "simuspace";

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * The Constant DESCRIPTION_FIELD.
     */
    private static final String DESCRIPTION_FIELD = "Description";

    /**
     * system user name field.
     */
    public static final String SYSTEM_USER_NAME = "test System user name";

    /**
     * search base field.
     */
    public static final String SEARCH_BASE = "test Search base";

    /**
     * system password field.
     */
    public static final String SYSTEM_PASSWORD = "test password";

    /**
     * User DN template field.
     */
    public static final String USER_DN_TEMPLATE = "test User DN template";

    /**
     * url field.
     */
    public static final String URL = "url";

    /**
     * The Constant ID.
     */
    private static final UUID ID = UUID.fromString( "53d9bf78-2391-4142-a6c8-dac14bd86a78" );

    /**
     * The Constant INDEX.
     */
    private static final int INDEX = 0;

    /**
     * The Constant SECOND_INDEX.
     */
    private static final int SECOND_INDEX = 1;

    /**
     * The Constant THRIED_INDEX.
     */
    private static final int THRIED_INDEX = 2;

    /**
     * The Constant FORTH_INDEX.
     */
    private static final int FORTH_INDEX = 4;

    /**
     * The directory manager impl.
     */
    private DirectoryManagerImpl directoryManagerImpl;

    /**
     * The directory dao.
     */
    private DirectoryDAO directoryDAO;

    /**
     * The userDirectoryAttribute dao.
     */
    private UserDirectoryAttributeDAO userDirectoryAttributeDAO;

    /**
     * The ad custom realm.
     */
    private ActiveDirectoryCustomRealm adCustomRealm;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The user manager.
     */
    private UserManager userManager;

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
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        directoryManagerImpl = new DirectoryManagerImpl();
        directoryDAO = mockControl.createMock( DirectoryDAO.class );
        userDirectoryAttributeDAO = mockControl.createMock( UserDirectoryAttributeDAO.class );
        licenseManager = mockControl.createMock( LicenseManager.class );
        userManager = mockControl.createMock( UserManager.class );
        directoryManagerImpl.setUserManager( userManager );
        directoryManagerImpl.setDirectoryDAO( directoryDAO );
        directoryManagerImpl.setUserDirectoryAttributeDAO( userDirectoryAttributeDAO );
        adCustomRealm = mockControl.createMock( ActiveDirectoryCustomRealm.class );
        permissionManager = mockControl.createMock( PermissionManager.class );
        directoryManagerImpl.setAdCustomRealm( adCustomRealm );
        directoryManagerImpl.setLicenseManager( licenseManager );
    }

    /**
     * Should return null when directory is not provided.
     *
     * @ the sus exception
     */
    @Test
    public void shouldReturnNullWhenDirectoryIsNotProvided() {
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        SuSUserDirectoryDTO directory = directoryManagerImpl.createDirectory( ConstantsID.SUPER_USER_ID, null );
        Assert.assertNull( directory );
    }

    /**
     * Should create directory when inputs are valid.
     *
     * @ the sus exception
     */
    @Test
    public void shouldCreateDirectoryWhenInputsAreValid() {

        SuSUserDirectoryDTO directoryModel = prepareUserDirectory();
        UserEntity userEntity = prepareUserEntity();

        directoryModel.setUserDirectoryAttribute( prepareDirectoryAttributes() );

        SuSUserDirectoryEntity directoryEntity = directoryManagerImpl.prepareDirectoryEntityFromModel( directoryModel );
        EasyMock.expect( directoryDAO.createDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( SuSUserDirectoryEntity.class ) ) ).andReturn( directoryEntity ).anyTimes();
        EasyMock.expect( adCustomRealm.isActiveDirectoryConnectionEstablished( EasyMock.anyObject( SusUserDirectoryAttributeDTO.class ) ) )
                .andReturn( true ).anyTimes();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        SuSUserDirectoryDTO actual = directoryManagerImpl.createDirectory( ConstantsID.SUPER_USER_ID, directoryModel );
        Assert.assertEquals( directoryModel.getId(), actual.getId() );
        Assert.assertEquals( directoryModel.getName(), actual.getName() );
        Assert.assertEquals( directoryModel.getDescription(), actual.getDescription() );
        Assert.assertEquals( directoryModel.getType(), actual.getType() );
        Assert.assertEquals( directoryModel.getStatus(), actual.getStatus() );

    }

    /**
     * Should update directory when inputs are valid.
     *
     * @ the sus exception
     */
    @Test
    public void shouldUpdateDirectoryWhenInputsAreValid() {

        SuSUserDirectoryDTO directoryModel = prepareUserDirectory();
        SuSUserDirectoryEntity directoryEntity = directoryManagerImpl.prepareDirectoryEntityFromModel( directoryModel );
        EasyMock.expect( directoryDAO.readDirectory( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( directoryEntity ).anyTimes();
        EasyMock.expect( directoryDAO.updateDirectory( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( SuSUserDirectoryEntity.class ) ) ).andReturn( directoryEntity ).anyTimes();
        EasyMock.expect( adCustomRealm.isActiveDirectoryConnectionEstablished( EasyMock.anyObject( SusUserDirectoryAttributeDTO.class ) ) )
                .andReturn( true ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        SuSUserDirectoryDTO actual = directoryManagerImpl.updateDirectory( ConstantsID.SUPER_USER_ID, ConstantsID.SUPER_USER_ID,
                directoryModel );
        Assert.assertEquals( directoryModel.getId(), actual.getId() );
        Assert.assertEquals( directoryModel.getName(), actual.getName() );
        Assert.assertEquals( directoryModel.getDescription(), actual.getDescription() );
        Assert.assertEquals( directoryModel.getType(), actual.getType() );
        Assert.assertEquals( directoryModel.getStatus(), actual.getStatus() );

    }

    /**
     * Should delete directory when valid directory id is provided.
     *
     * @ the sus exception
     */
    @Test
    public void shouldDeleteDirectoryWhenValidDirectoryIdIsProvided() {

        EasyMock.expect( directoryDAO.readDirectory( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new SuSUserDirectoryEntity() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        boolean deleted = directoryManagerImpl.deleteDirectory( EasyMock.anyObject( EntityManager.class ), ConstantsID.SUPER_USER_ID,
                UUID.randomUUID() );
        Assert.assertTrue( deleted );
    }

    /**
     * Should read directory when valid directory id is provided.
     *
     * @ the sus exception
     */
    @Test
    public void shouldReadDirectoryWhenValidDirectoryIdIsProvided() {

        SuSUserDirectoryDTO directoryModel = prepareUserDirectory();

        Map< String, String > attributes = new HashMap<>();
        attributes.put( NAME_FIELD, DESCRIPTION_FIELD );
        directoryModel.setUserDirectoryAttribute( prepareDirectoryAttributes() );

        SuSUserDirectoryEntity directoryEntity = directoryManagerImpl.prepareDirectoryEntityFromModel( directoryModel );
        ;

        UUID directoryId = UUID.randomUUID();

        EasyMock.expect( directoryDAO.readDirectory( EasyMock.anyObject( EntityManager.class ), directoryId ) ).andReturn( directoryEntity )
                .anyTimes();
        mockControl.replay();

        SuSUserDirectoryDTO actual = directoryManagerImpl.readDirectory( EasyMock.anyObject( EntityManager.class ),
                directoryId, true );
        Assert.assertEquals( directoryModel.getId(), actual.getId() );
        Assert.assertEquals( directoryModel.getName(), actual.getName() );
        Assert.assertEquals( directoryModel.getDescription(), actual.getDescription() );
        Assert.assertEquals( directoryModel.getType(), actual.getType() );
        Assert.assertEquals( directoryModel.getStatus(), actual.getStatus() );
    }

    /**
     * Should return null when reading directory with id non existing.
     *
     * @ the sus exception
     */
    @Test
    public void shouldReturnNullWhenReadingDirectoryWithIdNonExisting() {

        SuSUserDirectoryEntity directoryEntity = directoryManagerImpl.prepareDirectoryEntityFromModel( prepareUserDirectory() );
        EasyMock.expect( directoryDAO.readDirectory( EasyMock.anyObject( EntityManager.class ), UUID.randomUUID() ) )
                .andReturn( directoryEntity ).anyTimes();
        mockControl.replay();

        SuSUserDirectoryDTO actual = directoryManagerImpl.readDirectory( EasyMock.anyObject( EntityManager.class ),
                UUID.randomUUID(), true );
        Assert.assertNull( actual );
    }

    /**
     * Should successfully test the connection when valid directory type is provided.
     */
    @Test
    public void shouldSuccessfullyTestConnectionWhenValidDirectoryTypeIsProvided() {

        SuSUserDirectoryDTO directoryModel = prepareUserDirectory();
        directoryModel.setUserDirectoryAttribute( prepareDirectoryAttributes() );

        EasyMock.expect( adCustomRealm.isActiveDirectoryConnectionEstablished( EasyMock.anyObject( SusUserDirectoryAttributeDTO.class ) ) )
                .andReturn( true ).anyTimes();
        mockControl.replay();

        boolean actual = directoryManagerImpl.testLdapADConnection( directoryModel );
        Assert.assertTrue( actual );
    }

    /**
     * Should not successfully test the connection when invalid directory type is provided.
     *
     * @ the sus exception
     */
    @Test
    public void shouldNotSuccessfullyTestConnectionWhenInValidDirectoryTypeIsProvided() {

        SuSUserDirectoryDTO directoryModel = prepareUserDirectory();
        directoryModel.setUserDirectoryAttribute( prepareDirectoryAttributes() );
        directoryModel.setType( ConstantsUserDirectories.INTERNAL_DIRECTORY );
        mockControl.replay();
        boolean actual = directoryManagerImpl.testLdapADConnection( directoryModel );
        Assert.assertFalse( actual );
    }

    /**
     * Should throw exception when directory name is null.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenDirectoryNameIsNull() {

        List< String > errors = new ArrayList<>();
        errors.add( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Name" ) );
        thrown.expect( SusException.class );
        thrown.expectMessage( errors.toString() );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        SuSUserDirectoryDTO directory = prepareUserDirectory();
        directory.setName( null );
        directory.setType( ConstantsUserDirectories.INTERNAL_DIRECTORY );
        directoryManagerImpl.createDirectory( ConstantsID.SUPER_USER_ID, directory );
    }

    /**
     * Should not delete directory when invalid directory id is provided.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionAndDoNotDeleteDirectoryWhenInValidDirectoryIdIsProvided() {

        UUID directoryId = UUID.randomUUID();
        EasyMock.expect( directoryDAO.findById( EasyMock.anyObject( EntityManager.class ), directoryId ) ).andReturn( null ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.REQUEST_ID_NOT_VALID.getKey(), directoryId ) );
        directoryManagerImpl.deleteDirectory( EasyMock.anyObject( EntityManager.class ), ConstantsID.SUPER_USER_ID, UUID.randomUUID() );
    }

    /**
     * Should throw exception when going to read directory with null id.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenGoingToReadDirectoryWithNullId() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.REQUEST_ID_NOT_VALID.getKey() ) );
        mockControl.replay();
        SuSUserDirectoryDTO actual = directoryManagerImpl.readDirectory( DEFAULT_USER_ID, null );
        Assert.assertNull( actual );
    }

    /**
     * Should throw exception when in valid directory id provided.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenGoingToDeleteDirectoryWithInValidIdProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.REQUEST_ID_NOT_VALID.getKey() ) );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        boolean deleted = directoryManagerImpl.deleteDirectory( EasyMock.anyObject( EntityManager.class ), ConstantsID.SUPER_USER_ID,
                null );
        Assert.assertFalse( deleted );
    }

    /**
     * Should not update directory when existing id is not correct.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionAndDoNotUpdateDirectoryWhenProvidedDirectoryIdDoesNotExist() {

        SuSUserDirectoryDTO directoryModel = new SuSUserDirectoryDTO();
        directoryModel.setName( NAME_FIELD );
        directoryModel.setType( ConstantsUserDirectories.ACTIVE_DIRECTORY );
        directoryModel.setUserDirectoryAttribute( prepareDirectoryAttributes() );
        directoryModel.setStatus( ConstantsStatus.ACTIVE );
        EasyMock.expect(
                        directoryDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSUserDirectoryEntity.class ) ) )
                .andReturn( null ).anyTimes();
        EasyMock.expect( adCustomRealm.isActiveDirectoryConnectionEstablished( EasyMock.anyObject( SusUserDirectoryAttributeDTO.class ) ) )
                .andReturn( true ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.DIRECTORY_NOT_EXIST.getKey() ) );
        directoryManagerImpl.updateDirectory( ConstantsID.SUPER_USER_ID, ConstantsID.SUPER_USER_ID, directoryModel );
    }

    /**
     * Should throw exception when attributes are not provided for LDAP directory creation.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenAttributesAreNotProvidedForActiveDirectoryCreation() {

        thrown.expect( SusException.class );
        List< String > errors = new ArrayList<>();
        errors.add( MessageBundleFactory.getMessage( Messages.ATTRIBUTES_SHOULD_NOT_BE_EMPTY.getKey() ) );
        thrown.expectMessage( errors.toString() );

        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        SuSUserDirectoryDTO directory = prepareUserDirectory();
        directory.setUserDirectoryAttribute( null );
        directoryManagerImpl.createDirectory( ConstantsID.SUPER_USER_ID, directory );
    }

    /**
     * Should throw exception when directory name length is greater then allowed length.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenDirectoryNameLengthIsGreaterThenAllowedLength() {

        List< String > errors = new ArrayList<>();
        errors.add( MessageBundleFactory.getMessage( Messages.UTILS_VALUE_TOO_LARGE.getKey(), NAME_FIELD,
                ConstantsLength.STANDARD_NAME_LENGTH ) );
        thrown.expect( SusException.class );
        thrown.expectMessage( errors.toString() );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        SuSUserDirectoryDTO directory = prepareUserDirectory();
        directory.setName( NAME_LENGTH_GREATER_THEN_ALLOWED );
        directoryManagerImpl.createDirectory( ConstantsID.SUPER_USER_ID, directory );
    }

    /**
     * Should throw exception when directory description length is greater then allowed length.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenDirectoryDescriptionLengthIsGreaterThenAllowedLength() {

        List< String > errors = new ArrayList<>();
        errors.add( MessageBundleFactory.getMessage( Messages.UTILS_VALUE_TOO_LARGE.getKey(), DESCRIPTION_FIELD,
                ConstantsLength.STANDARD_DESCRIPTION_LENGTH ) );
        thrown.expect( SusException.class );
        thrown.expectMessage( errors.toString() );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        SuSUserDirectoryDTO directory = prepareUserDirectory();
        directory.setDescription( DESCRIPTION_LENGTH_GREATER_THEN_ALLOWED );
        directory.setType( ConstantsUserDirectories.INTERNAL_DIRECTORY );
        directoryManagerImpl.createDirectory( ConstantsID.SUPER_USER_ID, directory );
    }

    /**
     * Should throw exception when attributes are not provided for LDAP directory creation.
     *
     * @ the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenAttributesAreNotProvidedForLDAPDirectoryCreation() {

        List< String > errors = new ArrayList<>();
        errors.add( MessageBundleFactory.getMessage( Messages.ATTRIBUTES_SHOULD_NOT_BE_EMPTY.getKey() ) );
        thrown.expect( SusException.class );
        thrown.expectMessage( errors.toString() );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        SuSUserDirectoryDTO directory = prepareUserDirectory();
        directory.setType( ConstantsUserDirectories.LDAP_DIRECTORY );
        directory.setUserDirectoryAttribute( null );
        directoryManagerImpl.createDirectory( ConstantsID.SUPER_USER_ID, directory );
    }

    /**
     * Should success fully generate create form when valid input parameters are provided.
     */
    @Test
    public void shouldSuccessFullyGenerateCreateFormWhenValidInputParametersAreProvided() {
        List< UIFormItem > expectedUiFormItems = prepareDirectoryUIForm();
        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        mockControl.replay();
        // List< UIFormItem > actualUiFormItems = directoryManagerImpl.createForm( ConstantsID.SUPER_USER_ID );
        // Assert.assertEquals( expectedUiFormItems.get( INDEX ).getName(), actualUiFormItems.get( INDEX ).getName() );
        // Assert.assertEquals( expectedUiFormItems.get( SECOND_INDEX ).getLabel(), actualUiFormItems.get( SECOND_INDEX ).getLabel() );
    }

    /**
     * Should return empty when internal directory called.
     */
    @Test
    public void shouldReturnEmptyWhenInternalDirectoryCalled() {
        Assert.assertTrue( directoryManagerImpl.getUpdateDirectoryUI( INTERNAL_DIRECTORY_OPTION ).isEmpty() );
    }

    /**
     * Should return ldap parameters when ldap option called.
     */
    @Test
    public void shouldReturnLdapParametersWhenLdapOptionCalled() {
        List< UIFormItem > expectedUiFormItems = prepareLdapParameters();
        // List< UIFormItem > actualUiFormItems = directoryManagerImpl.getUpdateDirectoryUI( LDAP_DIRECTORY_OPTION );
        // Assert.assertEquals( expectedUiFormItems.size(), actualUiFormItems.size() );
        // Assert.assertEquals( expectedUiFormItems.get( INDEX ).getName(), actualUiFormItems.get( INDEX ).getName() );
        // Assert.assertEquals( expectedUiFormItems.get( SECOND_INDEX ).getLabel(), actualUiFormItems.get( SECOND_INDEX ).getLabel() );
        // Assert.assertEquals( expectedUiFormItems.get( THRIED_INDEX ).getLabel(), actualUiFormItems.get( THRIED_INDEX ).getLabel() );
        // Assert.assertEquals( expectedUiFormItems.get( FORTH_INDEX ).getLabel(), actualUiFormItems.get( FORTH_INDEX ).getLabel() );
    }

    /**
     * Should return empty when machine directory called.
     */
    @Test
    public void shouldReturnEmptyWhenMachineDirectoryCalled() {
        Assert.assertTrue( directoryManagerImpl.getUpdateDirectoryUI( MACHINE_USER_DIRECTORY_OPTION ).isEmpty() );
    }

    /**
     * Should return AD parameters when AD option called.
     */
    @Test
    public void shouldReturnADParametersWhenADOptionCalled() {
        List< UIFormItem > expectedUiFormItems = prepareADParameters();
        // List< UIFormItem > actualUiFormItems = directoryManagerImpl.getUpdateDirectoryUI( ACTIVE_DIRECTORY_OPTION );
        // Assert.assertEquals( expectedUiFormItems.size(), actualUiFormItems.size() );
        // Assert.assertEquals( expectedUiFormItems.get( INDEX ).getName(), actualUiFormItems.get( INDEX ).getName() );
        // Assert.assertEquals( expectedUiFormItems.get( SECOND_INDEX ).getLabel(), actualUiFormItems.get( SECOND_INDEX ).getLabel() );
        // Assert.assertEquals( expectedUiFormItems.get( THRIED_INDEX ).getLabel(), actualUiFormItems.get( THRIED_INDEX ).getLabel() );
        // Assert.assertEquals( expectedUiFormItems.get( FORTH_INDEX ).getLabel(), actualUiFormItems.get( FORTH_INDEX ).getLabel() );
    }

    /**
     * Should throw exception when invalid option provided.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidOptionProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.INVALID_OPTION_FOR_DIRECTORY.getKey() ) );
        directoryManagerImpl.getUpdateDirectoryUI( WRONG_OPTION_INPUT );
    }

    /**
     * Prepare AD parameters.
     *
     * @return the list
     */
    private List< UIFormItem > prepareADParameters() {
        List< UIFormItem > formItems = new ArrayList<>();

        UIFormItem uiFormItemID = new FormItemFactory().createGeneralFormItem();
        uiFormItemID.setLabel( "ID" );
        uiFormItemID.setName( "userDirectoryAttribute.id" );
        uiFormItemID.setSelectable( null );
        uiFormItemID.setType( "hidden" );
        formItems.add( uiFormItemID );

        UIFormItem uiFormItemADSearchBase = new FormItemFactory().createGeneralFormItem();
        uiFormItemADSearchBase.setLabel( "Active directory searchBase" );
        uiFormItemADSearchBase.setName( "userDirectoryAttribute.searchBase" );
        uiFormItemADSearchBase.setSelectable( "status" );
        uiFormItemADSearchBase.setType( "type" );
        formItems.add( uiFormItemADSearchBase );

        UIFormItem uiFormItemLdapUrl = new FormItemFactory().createGeneralFormItem();
        uiFormItemLdapUrl.setLabel( "Ldap or active directory url" );
        uiFormItemLdapUrl.setName( "userDirectoryAttribute.url" );
        uiFormItemLdapUrl.setSelectable( "status" );
        uiFormItemLdapUrl.setType( "type" );
        formItems.add( uiFormItemLdapUrl );

        UIFormItem uiFormItemLdapUserName = new FormItemFactory().createGeneralFormItem();
        uiFormItemLdapUserName.setLabel( "Ldap or active directory system username" );
        uiFormItemLdapUserName.setName( "userDirectoryAttribute.systemUsername" );
        uiFormItemLdapUserName.setSelectable( "status" );
        uiFormItemLdapUserName.setType( "type" );
        formItems.add( uiFormItemLdapUserName );

        UIFormItem uiFormItemLdapPassword = new FormItemFactory().createGeneralFormItem();
        uiFormItemLdapPassword.setLabel( "Ldap or active directory system password" );
        uiFormItemLdapPassword.setName( "userDirectoryAttribute.systemPassword" );
        uiFormItemLdapPassword.setSelectable( "status" );
        uiFormItemLdapPassword.setType( "type" );
        formItems.add( uiFormItemLdapPassword );
        return formItems;
    }

    /**
     * Prepare ldap parameters.
     *
     * @return the list
     */
    private List< UIFormItem > prepareLdapParameters() {
        List< UIFormItem > formItems = new ArrayList<>();

        UIFormItem uiFormItemID = new FormItemFactory().createGeneralFormItem();
        uiFormItemID.setLabel( "ID" );
        uiFormItemID.setName( "userDirectoryAttribute.id" );
        uiFormItemID.setSelectable( null );
        uiFormItemID.setType( "hidden" );
        formItems.add( uiFormItemID );

        UIFormItem uiFormItemLdapTemplate = new FormItemFactory().createGeneralFormItem();
        uiFormItemLdapTemplate.setLabel( "LDAP User Dn Template" );
        uiFormItemLdapTemplate.setName( "userDirectoryAttribute.userDnTemplate" );
        uiFormItemLdapTemplate.setSelectable( "status" );
        uiFormItemLdapTemplate.setType( "type" );
        formItems.add( uiFormItemLdapTemplate );

        UIFormItem uiFormItemLdapUrl = new FormItemFactory().createGeneralFormItem();
        uiFormItemLdapUrl.setLabel( "Ldap or active directory url" );
        uiFormItemLdapUrl.setName( "userDirectoryAttribute.url" );
        uiFormItemLdapUrl.setSelectable( "status" );
        uiFormItemLdapUrl.setType( "type" );
        formItems.add( uiFormItemLdapUrl );

        UIFormItem uiFormItemLdapUserName = new FormItemFactory().createGeneralFormItem();
        uiFormItemLdapUserName.setLabel( "Ldap or active directory system username" );
        uiFormItemLdapUserName.setName( "userDirectoryAttribute.systemUsername" );
        uiFormItemLdapUserName.setSelectable( "status" );
        uiFormItemLdapUserName.setType( "type" );
        formItems.add( uiFormItemLdapUserName );

        UIFormItem uiFormItemLdapPassword = new FormItemFactory().createGeneralFormItem();
        uiFormItemLdapPassword.setLabel( "Ldap or active directory system password" );
        uiFormItemLdapPassword.setName( "userDirectoryAttribute.systemPassword" );
        uiFormItemLdapPassword.setSelectable( "status" );
        uiFormItemLdapPassword.setType( "type" );
        formItems.add( uiFormItemLdapPassword );
        return formItems;
    }

    /**
     * Prepare directory UI form.
     *
     * @return the list
     */
    private List< UIFormItem > prepareDirectoryUIForm() {
        List< UIFormItem > formItems = new ArrayList<>();
        UIFormItem uiFormItemName = new FormItemFactory().createGeneralFormItem();
        uiFormItemName.setLabel( "Name" );
        Map< String, Object > messages = new HashMap<>();
        messages.put( "minlength", "Please enter atleast 1 character" );
        messages.put( "maxlength", "Please enter no more than 255 characters" );
        messages.put( "required", "Name field cannot be empty" );
        uiFormItemName.setMessages( messages );
        uiFormItemName.setName( "name" );
        Map< String, Object > rules = new HashMap<>();
        rules.put( "minlength", "1" );
        rules.put( "maxlength", "255" );
        rules.put( "required", "true" );
        uiFormItemName.setRules( rules );
        uiFormItemName.setSelectable( null );
        uiFormItemName.setType( "type" );
        formItems.add( uiFormItemName );
        UIFormItem uiFormItemDescription = new FormItemFactory().createGeneralFormItem();
        uiFormItemDescription.setLabel( "Description" );
        uiFormItemDescription.setName( "description" );
        uiFormItemDescription.setSelectable( "status" );
        uiFormItemDescription.setType( "type" );
        formItems.add( uiFormItemDescription );
        SelectFormItem selectFormItemStatus = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItemStatus.setLabel( "Status" );
        selectFormItemStatus.setName( "status" );
        selectFormItemStatus.setType( "select" );
        formItems.add( selectFormItemStatus );
        SelectFormItem selectFormItemType = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItemType.setLabel( "Type" );
        selectFormItemType.setName( "type" );
        selectFormItemType.setType( "select" );
        formItems.add( selectFormItemType );
        return formItems;
    }

    /**
     * prepare user directory attribute model.
     *
     * @return the sus user directory attribute DTO
     */
    private SusUserDirectoryAttributeDTO prepareDirectoryAttributes() {
        SusUserDirectoryAttributeDTO directoryAttributes = new SusUserDirectoryAttributeDTO();
        directoryAttributes.setId( ID.toString() );
        directoryAttributes.setSearchBase( SEARCH_BASE );
        directoryAttributes.setSystemPassword( SYSTEM_PASSWORD );
        directoryAttributes.setSystemUsername( SYSTEM_USER_NAME );
        directoryAttributes.setUrl( URL );
        directoryAttributes.setUserDnTemplate( USER_DN_TEMPLATE );
        return directoryAttributes;
    }

    /**
     * prepare directory attribute entity.
     *
     * @return the user directory attribute entity
     */
    private UserDirectoryAttributeEntity prepareDirectoryAttributesEntity() {
        UserDirectoryAttributeEntity directoryAttributes = new UserDirectoryAttributeEntity();
        directoryAttributes.setId( ID );
        directoryAttributes.setSearchBase( SEARCH_BASE );
        directoryAttributes.setSystemPassword( SYSTEM_PASSWORD );
        directoryAttributes.setSystemUsername( SYSTEM_USER_NAME );
        directoryAttributes.setUrl( URL );
        directoryAttributes.setUserDnTemplate( USER_DN_TEMPLATE );
        return directoryAttributes;
    }

    /**
     * prepare filters dto.
     *
     * @return the filters DTO
     */
    private FiltersDTO prepareFiltersDTO() {

        FiltersDTO filter = new FiltersDTO();
        filter.setDraw( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setLength( ConstantsInteger.INTEGER_VALUE_TWO );
        filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        return filter;
    }

    /**
     * Prepare user directory model.
     *
     * @return the su S user directory DTO
     */
    private SuSUserDirectoryDTO prepareUserDirectory() {
        SuSUserDirectoryDTO directoryModel = new SuSUserDirectoryDTO();
        directoryModel.setId( ID );
        directoryModel.setName( NAME_FIELD );
        directoryModel.setType( ConstantsUserDirectories.ACTIVE_DIRECTORY );
        directoryModel.setStatus( ConstantsStatus.ACTIVE );
        directoryModel.setUserDirectoryAttribute( prepareDirectoryAttributes() );
        directoryModel.setModifiedOn( DateFormatStandard.format( new Date() ) );
        directoryModel.setCreatedOn( DateFormatStandard.format( new Date() ) );
        return directoryModel;
    }

    /**
     * Prepare user entity.
     *
     * @return the user entity
     */
    private UserEntity prepareUserEntity() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( ConstantsID.SUPER_USER_ID ) );
        userEntity.setFirstName( SIMUSPACE );
        userEntity.setSurName( NAME_FIELD );

        return userEntity;
    }

}
