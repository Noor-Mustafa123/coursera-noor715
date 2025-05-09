package de.soco.software.simuspace.suscore.license.manager.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
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

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LicenseUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.common.dao.AuditLogDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseConfigurationDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseUserDAO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseFeatureEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseUserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;

/**
 * The Class is responsible to test public functions of {@link LicenseManagerImpl}.
 *
 * @author M.Nasir.Farooq
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { LicenseUtils.class, Activator.class } )
public class LicenseConfigurationManagerImplTest {

    /**
     * The Constant IN_VALID_FEATURE.
     */
    private static final String IN_VALID_FEATURE = "abc";

    /**
     * The Constant MAC_ADDRESS_IS_VALID.
     */
    private static final boolean MAC_ADDRESS_IS_VALID = true;

    /**
     * The Constant STATIC_METHOD_NAME_VERIFY_MAC_ADDRESS.
     */
    private static final String STATIC_METHOD_MOCK_VERIFY_MAC_ADDRESS = "verifyMacAddress";

    /**
     * The Constant FIRST_INDEX_OF_LIST.
     */
    private static final int FIRST_INDEX_OF_LIST = 0;

    /**
     * The Constant LICENSE_CONFIG_FILE_PATH.
     */
    private static final String LICENSE_CONFIG_FILE_PATH = "/license.json";

    /**
     * The Constant MODULE.
     */
    private static final String MODULE_RUN_SIM = "RunSim";

    /**
     * The Constant MODULE_2.
     */
    private static final String MODULE_2 = "Module-2";

    /**
     * The Constant EDITABLE_FIELD_VENDOR.
     */
    private static final String EDITABLE_FIELD_VENDOR = "vendor";

    /**
     * The Constant EDITABLE_FIELD_RESELLER.
     */
    private static final String EDITABLE_FIELD_RESELLER = "reseller";

    /**
     * The Constant EDITABLE_FIELD_CUSTOMER.
     */
    private static final String EDITABLE_FIELD_CUSTOMER = "customer";

    /**
     * The license configuration.
     */
    private LicenseConfigurationManagerImpl licenseConfiguration;

    /**
     * The license config dao.
     */
    private LicenseConfigurationDAO licenseConfigDao;

    /**
     * The license user dao.
     */
    private LicenseUserDAO licenseUserDao;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The Constant NULL_LICENSE.
     */
    private static final ModuleLicenseDTO NULL_LICENSE = null;

    /**
     * The Constant NULL_MODULE_NAME.
     */
    private static final String NULL_MODULE_NAME = null;

    /**
     * The Constant NULL_MODULE_NAME.
     */
    private static final UUID NULL_MODULE_ID = null;

    /**
     * The Constant EMPTY_MODULE_NAME.
     */
    private static final String EMPTY_MODULE_NAME = "";

    /**
     * The Constant FIRSTNAME.
     */
    private static final String USERNAME = "firstName";

    /**
     * The active account status.
     */
    public static final String ACCOUNT_STATUS_ACTIVE = "active";

    /**
     * The inactive account status.
     */
    public static final String ACCOUNT_STATUS_IN_ACTIVE = "inactive";

    /**
     * The Constant CHANGE_ABLE.
     */
    private static final boolean CHANGE_ABLE = false;

    /**
     * The Constant USER_UID.
     */
    private static final String USER_UID = "sces122";

    /**
     * The Constant USER_ID.
     */
    private static final String USER_ID = UUID.randomUUID().toString();

    /**
     * The Constant NULL_USER_ID.
     */
    private static final String NULL_USER_ID = null;

    /**
     * The Constant NULL_FEATURE_NAME.
     */
    private static final String NULL_FEATURE_NAME = null;

    /**
     * The Constant EMPTY_FEATURE_NAME.
     */
    private static final String EMPTY_FEATURE_NAME = "";

    /**
     * The Constant FEATURE_NAME.
     */
    private static final String FEATURE_NAME = "FEATURE";

    /**
     * The Constant SUPER_USER_ID.
     */
    public static final String SUPER_USER_ID = "1e98a77c-a0be-4983-9137-d9a8acd0ea8b";

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * The Constant STATUS_ACTIVE.
     */
    public static final boolean STATUS_ACTIVE = true;

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The audit log DAO.
     */
    private AuditLogDAO auditLogDAO;

    /**
     * Setup.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setup() throws Exception {
        mockControl.resetToNice();
        licenseConfiguration = new LicenseConfigurationManagerImpl();
        licenseConfigDao = mockControl.createMock( LicenseConfigurationDAO.class );
        licenseConfiguration.setLicenseConfigDao( licenseConfigDao );
        licenseUserDao = mockControl.createMock( LicenseUserDAO.class );
        licenseConfiguration.setLicenseUserDao( licenseUserDao );

        auditLogDAO = mockControl.createMock( AuditLogDAO.class );
        licenseConfiguration.setAuditLogDAO( auditLogDAO );
        contextMenuManager = mockControl.createMock( ContextMenuManager.class );
        licenseConfiguration.setContextMenuManager( contextMenuManager );

        userCommonManager = mockControl.createMock( UserCommonManager.class );
        licenseConfiguration.setUserCommonManager( userCommonManager );

        mockStaticFunctions();

    }

    /**
     * Should parse validate sign and add license when valid input provided.
     *
     * @throws Exception
     */
    @Test
    public void shouldParseValidateSignAndAddLicenseWhenValidInputProvided() throws Exception {
        ModuleLicenseDTO expected = JsonUtils.jsonStreamToObject( this.getClass().getResourceAsStream( LICENSE_CONFIG_FILE_PATH ),
                ModuleLicenseDTO.class );
        LicenseEntity licenseEntity = prepareEntity( expected );
        EasyMock.expect( licenseConfigDao.addLicense( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( licenseEntity ).anyTimes();
        List< LicenseFeatureEntity > licenseFeatureEntities = new ArrayList<>(
                prepareLicenseFeatureEntitySet( licenseEntity, expected.getFeatures() ) );
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect( licenseConfigDao.getLicenseFeaturesByLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( licenseFeatureEntities ).anyTimes();
        PowerMockito.doReturn( true ).when( LicenseUtils.class, "verifyLicense", expected );
        UserDTO userEntity = prepareUserEntity();
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        ModuleLicenseDTO actual = licenseConfiguration.addLicense( SUPER_USER_ID, expected );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should add license when valid license provided.
     *
     * @throws Exception
     */
    @Test
    public void shouldAddLicenseWhenValidLicenseProvided() throws Exception {

        ModuleLicenseDTO expected = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( expected );
        EasyMock.expect( licenseConfigDao.addLicense( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( licenseEntity );

        List< LicenseFeatureEntity > licenseFeatureEntities = new ArrayList<>(
                prepareLicenseFeatureEntitySet( licenseEntity, expected.getFeatures() ) );

        EasyMock.expect( auditLogDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AuditLogEntity.class ) ) )
                .andReturn( new AuditLogEntity() ).anyTimes();
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect( licenseConfigDao.getLicenseFeaturesByLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( licenseFeatureEntities ).anyTimes();
        PowerMockito.doReturn( true ).when( LicenseUtils.class, "verifyLicense", expected );
        UserDTO userEntity = prepareUserEntity();
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        ModuleLicenseDTO actual = licenseConfiguration.addLicense( SUPER_USER_ID, expected );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should throw exception when non editable fields tempered in add license.
     */
    @Test
    public void shouldThrowExceptionWhenNonEditableFieldsTemperedInAddLicense() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.LICENSE_SIGNATURE_IS_NOT_VALID.getKey() ) );

        ModuleLicenseDTO expected = fillLicenseDTO();
        expected.setModule( MODULE_2 );
        LicenseEntity licenseEntity = prepareEntity( expected );
        EasyMock.expect( licenseConfigDao.addLicense( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( licenseEntity );
        UserDTO userEntity = prepareUserEntity();
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        licenseConfiguration.addLicense( SUPER_USER_ID, expected );
    }

    /**
     * Should throw exception when null module name is passed to get user count by module.
     */
    @Test
    public void shouldThrowExceptionWhenNullLicenseObjectIsPassedToAddLicense() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.LICENSE_INPUT_CANNOT_BE_NULL.getKey() ) );
        mockControl.replay();
        licenseConfiguration.addLicense( SUPER_USER_ID, NULL_LICENSE );

    }

    /**
     * Should throw exception when null module name is passed to get module license.
     */
    @Test
    public void shouldThrowExceptionWhenNullModuleNameIsPassedToGetModuleLicense() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MODULE_NAME_CANNOT_BE_NULL.getKey() ) );
        licenseConfiguration.getModuleLicense( EasyMock.anyObject( EntityManager.class ), NULL_MODULE_NAME );

    }

    /**
     * Should throw exception when empty module name is passed to get module license.
     */
    @Test
    public void shouldThrowExceptionWhenEmptyModuleNameIsPassedToGetModuleLicense() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MODULE_NAME_CANNOT_BE_NULL.getKey() ) );
        licenseConfiguration.getModuleLicense( EasyMock.anyObject( EntityManager.class ), EMPTY_MODULE_NAME );

    }

    /**
     * Should add license when editable fields tempered in add license.
     *
     * @throws Exception
     */
    @Test
    public void shouldAddLicenseWhenEditableFieldsTemperedInAddLicense() throws Exception {

        ModuleLicenseDTO expected = fillLicenseDTO();
        expected.setVendor( EDITABLE_FIELD_VENDOR );
        expected.setCustomer( EDITABLE_FIELD_CUSTOMER );
        expected.setReseller( EDITABLE_FIELD_RESELLER );
        LicenseEntity licenseEntity = prepareEntity( expected );
        List< LicenseFeatureEntity > licenseFeatureEntities = new ArrayList<>(
                prepareLicenseFeatureEntitySet( licenseEntity, expected.getFeatures() ) );
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect( licenseConfigDao.getLicenseFeaturesByLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( licenseFeatureEntities ).anyTimes();
        EasyMock.expect( licenseConfigDao.addLicense( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( licenseEntity );
        PowerMockito.doReturn( true ).when( LicenseUtils.class, "verifyLicense", expected );
        UserDTO userEntity = prepareUserEntity();
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        ModuleLicenseDTO actual = licenseConfiguration.addLicense( SUPER_USER_ID, expected );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should throw exception when required fields are missing.
     */
    @Test
    public void shouldThrowExceptionWhenRequiredFieldsAreMissing() {

        Notification notify = new Notification();
        notify.addError( new Error( MessageBundleFactory.getMessage( Messages.MODULE_NAME_CANNOT_BE_NULL.getKey() ) ) );
        notify.addError( new Error( MessageBundleFactory.getMessage( Messages.MAC_ADDRESS_CANNOT_BE_NULL.getKey() ) ) );
        notify.addError( new Error( MessageBundleFactory.getMessage( Messages.EXPIRY_TIME_CANNOT_BE_NULL.getKey() ) ) );
        thrown.expect( SusException.class );
        thrown.expectMessage( notify.getErrors().toString() );
        UserDTO userEntity = prepareUserEntity();
        mockControl.replay();
        ModuleLicenseDTO expected = fillLicenseDTO();
        expected.setModule( null );
        expected.setMacAddress( null );
        expected.setExpiryTime( null );

        licenseConfiguration.addLicense( SUPER_USER_ID, expected );
    }

    /**
     * Should return null license by moudle when not exists.
     */
    @Test
    public void shouldReturnNullLicenseByMoudleWhenNotExists() {

        ModuleLicenseDTO license = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( license );
        EasyMock.expect( licenseConfigDao.getModuleLicense( EasyMock.anyObject( EntityManager.class ), MODULE_RUN_SIM ) )
                .andReturn( licenseEntity );
        mockControl.replay();

        ModuleLicenseDTO retLicense = licenseConfiguration.getModuleLicense( EasyMock.anyObject( EntityManager.class ), MODULE_2 );
        Assert.assertNull( retLicense );
    }

    /**
     * Should return module list when get module license list is called.
     */
    @Test
    public void shouldReturnModuleListWhenGetModuleLicenseListIsCalled() {

        ModuleLicenseDTO actualLicenseDTO = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( actualLicenseDTO );
        List< LicenseEntity > licenseEntities = new ArrayList<>();
        licenseEntities.add( licenseEntity );

        EasyMock.expect( licenseConfigDao.getModuleLicenseList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( licenseEntities );

        List< LicenseFeatureEntity > licenseFeatureEntities = new ArrayList<>(
                prepareLicenseFeatureEntitySet( licenseEntity, actualLicenseDTO.getFeatures() ) );
        EasyMock.expect( licenseConfigDao.getLicenseFeaturesByLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( licenseFeatureEntities ).anyTimes();

        mockControl.replay();
        //
        // List<ModuleLicenseDTO> expectedModuleLicenseDTO =
        // licenseConfiguration.getModuleLicenseList();
        // Assert.assertEquals( expectedModuleLicenseDTO.get( FIRST_INDEX_OF_LIST ),
        // actualLicenseDTO );
    }

    /**
     * Should return module list when get modules by user id and module is attached to user.
     */
    @Test
    public void shouldReturnModuleListWhenGetModulesByUserIdAndModuleIsAttachedToUser() {

        ModuleLicenseDTO actualLicenseDTO = fillLicenseDTO();

        LicenseEntity licenseEntity = prepareEntity( actualLicenseDTO );
        List< LicenseEntity > licenseEntities = new ArrayList<>();
        licenseEntities.add( licenseEntity );

        EasyMock.expect( licenseConfigDao.getModulesByUserId( EasyMock.anyObject( EntityManager.class ), USER_ID ) )
                .andReturn( licenseEntities );
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( new ArrayList<>() ).anyTimes();
        List< LicenseFeatureEntity > licenseFeatureEntities = new ArrayList<>(
                prepareLicenseFeatureEntitySet( licenseEntity, actualLicenseDTO.getFeatures() ) );
        EasyMock.expect( licenseConfigDao.getLicenseFeaturesByLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( licenseFeatureEntities ).anyTimes();

        mockControl.replay();

        List< ModuleLicenseDTO > expectedModuleLicenseDTO = licenseConfiguration.getModulesByUserId(
                EasyMock.anyObject( EntityManager.class ), USER_ID );
        Assert.assertEquals( expectedModuleLicenseDTO.get( FIRST_INDEX_OF_LIST ), actualLicenseDTO );
    }

    /**
     * Should throw exception when null user id is passed to ge modules by user id.
     */
    @Test
    public void shouldThrowExceptionWhenNullUserIdIsPassedToGeModulesByUserId() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        licenseConfiguration.getModulesByUserId( EasyMock.anyObject( EntityManager.class ), NULL_USER_ID );
    }

    /**
     * Should throw exception when null module name is passed to is user valid for module.
     */
    @Test
    public void shouldThrowExceptionWhenNullModuleNameIsPassedToIsUserValidForModule() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MODULE_NAME_CANNOT_BE_NULL.getKey() ) );
        licenseConfiguration.isUserValidForModule( EasyMock.anyObject( EntityManager.class ), NULL_MODULE_NAME, USER_ID );
    }

    /**
     * Should throw exception when empty module is passed to is user valid for module.
     */
    @Test
    public void shouldThrowExceptionWhenEmptyModuleIsPassedToIsUserValidForModule() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MODULE_NAME_CANNOT_BE_NULL.getKey() ) );
        licenseConfiguration.isUserValidForModule( EasyMock.anyObject( EntityManager.class ), EMPTY_MODULE_NAME, USER_ID );
    }

    /**
     * Should throw exception when null user id is passed to is user valid for module.
     */
    @Test
    public void shouldThrowExceptionWhenNullUserIdIsPassedToIsUserValidForModule() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        licenseConfiguration.isUserValidForModule( EasyMock.anyObject( EntityManager.class ), MODULE_RUN_SIM, NULL_USER_ID );
    }

    /**
     * Should return user as valid user when user id is associated with module.
     */
    @Test
    public void shouldReturnUserAsValidUserWhenUserIdIsAssociatedWithModule() {

        ModuleLicenseDTO license = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( license );

        EasyMock.expect( licenseConfigDao.getLicenseByModuleAndUser( EasyMock.anyObject( EntityManager.class ), MODULE_RUN_SIM, USER_ID ) );
        mockControl.replay();

        boolean expected = licenseConfiguration.isUserValidForModule( EasyMock.anyObject( EntityManager.class ), MODULE_RUN_SIM, USER_ID );
        Assert.assertTrue( expected );
    }

    /**
     * Should throw exception when null module name is passed to is user valid for module.
     */
    @Test
    public void shouldThrowExceptionWhenNullModuleNameIsPassedToIsUserValidForModuleAndFeature() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.FEATURE_NAME_SHOULD_NOT_BE_NULL_OR_EMPTY.getKey() ) );
        licenseConfiguration.checkIfFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), NULL_FEATURE_NAME, USER_ID );
    }

    /**
     * Should throw exception when empty module is passed to is user valid for module.
     */
    @Test
    public void shouldThrowExceptionWhenEmptyModuleIsPassedToIsUserValidForModuleAndFeature() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.FEATURE_NAME_SHOULD_NOT_BE_NULL_OR_EMPTY.getKey() ) );
        licenseConfiguration.checkIfFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), EMPTY_FEATURE_NAME, USER_ID );
    }

    /**
     * Should throw exception when null user id is passed to is user valid for module.
     */
    @Test
    public void shouldThrowExceptionWhenNullUserIdIsPassedToIsUserValidForModuleAndFeature() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        licenseConfiguration.checkIfFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), FEATURE_NAME, NULL_USER_ID );
    }

    /**
     * Should return user as valid user when user id is associated with module and module had feature.
     */
    @Test
    public void shouldReturnUserAsValidUserWhenUserIdIsAssociatedWithModuleAndModuleHadFeature() {

        ModuleLicenseDTO license = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( license );

        LicenseFeatureEntity featureEntity = new LicenseFeatureEntity( licenseEntity, license.getFeatures().get( FIRST_INDEX_OF_LIST ) );

        EasyMock.expect(
                licenseUserDao.getLicenseUserEntityByModuleAndUser( EasyMock.anyObject( EntityManager.class ), UUID.fromString( USER_ID ),
                        MODULE_RUN_SIM ) ).andReturn( new LicenseUserEntity() );
        EasyMock.expect( licenseConfigDao.getLicenseFeature( EasyMock.anyObject( EntityManager.class ),
                license.getFeatures().get( FIRST_INDEX_OF_LIST ) ) ).andReturn( featureEntity );
        mockControl.replay();

        boolean expected = licenseConfiguration.isFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ),
                license.getFeatures().get( FIRST_INDEX_OF_LIST ), USER_ID );
        Assert.assertTrue( expected );
    }

    /**
     * Should return user as in valid for feature when module do not have that feature.
     */
    @Test
    public void shouldReturnUserAsInValidForFeatureWhenModuleDoNotHaveThatFeature() {

        ModuleLicenseDTO license = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( license );
        LicenseFeatureEntity featureEntity = new LicenseFeatureEntity( licenseEntity, license.getFeatures().get( FIRST_INDEX_OF_LIST ) );

        EasyMock.expect( licenseConfigDao.getLicenseByModuleAndUser( EasyMock.anyObject( EntityManager.class ), MODULE_RUN_SIM, USER_ID ) );
        EasyMock.expect( licenseConfigDao.getLicenseFeature( EasyMock.anyObject( EntityManager.class ),
                license.getFeatures().get( FIRST_INDEX_OF_LIST ) ) ).andReturn( featureEntity ).anyTimes();
        mockControl.replay();

        boolean expected = licenseConfiguration.isFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), IN_VALID_FEATURE,
                USER_ID );
        Assert.assertFalse( expected );
    }

    /**
     * Should return user as in valid for feature when feature is not attached to module.
     */
    @Test
    public void shouldReturnUserAsInValidForFeatureWhenFeatureIsNotAttachedToModule() {

        ModuleLicenseDTO license = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( license );
        licenseEntity.setModule( MODULE_2 );
        LicenseFeatureEntity featureEntity = new LicenseFeatureEntity( licenseEntity, license.getFeatures().get( FIRST_INDEX_OF_LIST ) );

        EasyMock.expect( licenseConfigDao.getLicenseByModuleAndUser( EasyMock.anyObject( EntityManager.class ), MODULE_RUN_SIM, USER_ID ) );
        EasyMock.expect( licenseConfigDao.getLicenseFeature( EasyMock.anyObject( EntityManager.class ),
                license.getFeatures().get( FIRST_INDEX_OF_LIST ) ) ).andReturn( featureEntity );
        mockControl.replay();

        boolean expected = licenseConfiguration.isFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ),
                license.getFeatures().get( FIRST_INDEX_OF_LIST ), USER_ID );
        Assert.assertFalse( expected );
    }

    /**
     * Should throw exception when user type is going to update to full rights user and full rights users limit is consumed.
     */
    @Test
    public void shouldThrowExceptionWhenUserTypeIsGoingToUpdateToFullRightsUserAndFullRightsUsersLimitIsConsumed() {

        ModuleLicenseDTO expected = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( expected );
        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.ALLOWED_USERS_EXCEEDS_FROM_AVAILABLE.getKey(), licenseEntity.getModule() ) );

        List< LicenseEntity > licenseEntities = new ArrayList<>();
        licenseEntities.add( licenseEntity );

        EasyMock.expect( licenseConfigDao.getModuleLicenseList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( licenseEntities );
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( fillTwoFullRightsLicenseUserEntities( licenseEntity ) );
        UserDTO userDTO = prepareUserEntity();
        userDTO.setRestricted( "No" );
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDTO ).anyTimes();

        mockControl.replay();
        licenseConfiguration.manageUserLicense( SUPER_USER_ID, UUID.randomUUID(),
                new CheckBox( null, LicenseConfigurationManagerImpl.USER_TYPE_COLUMN_NAME, CheckBox.UN_CHECKED ) );

    }

    /**
     * Should throw exception when user type is going to update to restricted user and restricted users limit is consumed.
     */
    @Test
    public void shouldThrowExceptionWhenUserTypeIsGoingToUpdateToRestrictedUserAndRestrictedUsersLimitIsConsumed() {

        ModuleLicenseDTO expected = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( expected );

        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.RESTRICTED_USERS_EXCEEDS_FROM_AVAILABLE.getKey(), licenseEntity.getModule() ) );

        List< LicenseEntity > licenseEntities = new ArrayList<>();
        licenseEntities.add( licenseEntity );

        EasyMock.expect( licenseConfigDao.getModuleLicenseList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( licenseEntities );
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( fillTwoRestrictedLicenseUserEntities( licenseEntity ) );
        UserDTO userDTO = prepareUserEntity();
        userDTO.setRestricted( "No" );
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDTO ).anyTimes();
        mockControl.replay();

        licenseConfiguration.manageUserLicense( SUPER_USER_ID, UUID.randomUUID(),
                new CheckBox( null, LicenseConfigurationManagerImpl.USER_TYPE_COLUMN_NAME, CheckBox.CHECKED ) );

    }

    /**
     * Should update user type to restricted when restricted user limit is not consumed for any license.
     */
    @Test
    public void shouldUpdateUserTypeToRestrictedWhenRestrictedUserLimitIsNotConsumedForAnyLicense() {

        ModuleLicenseDTO expected = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( expected );
        List< LicenseEntity > licenseEntities = new ArrayList<>();
        licenseEntities.add( licenseEntity );

        List< LicenseUserEntity > singleRestrictedLicenseUserEntity = new ArrayList<>();
        LicenseUserEntity restrictedUserEntity = new LicenseUserEntity();
        restrictedUserEntity.setUserEntity( fillRestrictedUserEntity() );
        restrictedUserEntity.setLicenseEntity( licenseEntity );
        singleRestrictedLicenseUserEntity.add( restrictedUserEntity );

        EasyMock.expect( licenseConfigDao.getModuleLicenseList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( licenseEntities );
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( singleRestrictedLicenseUserEntity );
        UserDTO userDTO = prepareUserEntity();
        userDTO.setRestricted( "No" );
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDTO ).anyTimes();
        mockControl.replay();

        licenseConfiguration.manageUserLicense( SUPER_USER_ID, UUID.randomUUID(),
                new CheckBox( null, LicenseConfigurationManagerImpl.USER_TYPE_COLUMN_NAME, CheckBox.CHECKED ) );
    }

    /**
     * Should update user type to full rights user when full rights user limit is not consumed for any license.
     */
    @Test
    public void shouldUpdateUserTypeToFullRightsUserWhenFullRightsUserLimitIsNotConsumedForAnyLicense() {

        ModuleLicenseDTO expected = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( expected );
        List< LicenseEntity > licenseEntities = new ArrayList<>();
        licenseEntities.add( licenseEntity );

        List< LicenseUserEntity > singleFullRightsLicenseUserEntity = new ArrayList<>();
        LicenseUserEntity fullRightsLicenseUserEntity = new LicenseUserEntity();
        fullRightsLicenseUserEntity.setUserEntity( fillFullRightsUserEntity() );
        fullRightsLicenseUserEntity.setLicenseEntity( licenseEntity );
        singleFullRightsLicenseUserEntity.add( fullRightsLicenseUserEntity );

        EasyMock.expect( licenseConfigDao.getModuleLicenseList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( licenseEntities );
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( singleFullRightsLicenseUserEntity );
        UserDTO userDTO = prepareUserEntity();
        userDTO.setRestricted( "No" );
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDTO ).anyTimes();
        mockControl.replay();

        licenseConfiguration.manageUserLicense( SUPER_USER_ID, UUID.randomUUID(),
                new CheckBox( null, LicenseConfigurationManagerImpl.USER_TYPE_COLUMN_NAME, CheckBox.UN_CHECKED ) );
    }

    /**
     * Should attach restricted user to module license.
     *
     * @throws Exception
     */
    @Test
    public void shouldAttachRestrictedUserToModuleLicense() throws Exception {

        ModuleLicenseDTO expected = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( expected );
        List< LicenseEntity > licenseEntities = new ArrayList<>();
        licenseEntities.add( licenseEntity );

        List< LicenseUserEntity > singleRestrictedLicenseUserEntity = new ArrayList<>();
        LicenseUserEntity restrictedUserEntity = new LicenseUserEntity();
        restrictedUserEntity.setUserEntity( fillRestrictedUserEntity() );
        restrictedUserEntity.setLicenseEntity( licenseEntity );
        singleRestrictedLicenseUserEntity.add( restrictedUserEntity );

        UUID userId = UUID.randomUUID();
        List< LicenseFeatureEntity > licenseFeatureEntities = new ArrayList<>(
                prepareLicenseFeatureEntitySet( licenseEntity, expected.getFeatures() ) );
        EasyMock.expect( licenseConfigDao.getLicenseFeaturesByLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( licenseFeatureEntities ).anyTimes();
        EasyMock.expect( licenseConfigDao.getModuleLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity.getModule() ) )
                .andReturn( licenseEntity ).anyTimes();
        EasyMock.expect( userCommonManager.isUserRestricted( EasyMock.anyObject( EntityManager.class ), userId ) ).andReturn( true )
                .anyTimes();
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( singleRestrictedLicenseUserEntity ).anyTimes();
        PowerMockito.doReturn( true ).when( LicenseUtils.class, "verifyLicense", expected );
        UserDTO userDTO = prepareUserEntity();
        userDTO.setRestricted( "No" );
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDTO ).anyTimes();
        mockControl.replay();

        licenseConfiguration.manageUserLicense( SUPER_USER_ID, userId,
                new CheckBox( null, LicenseConfigurationManagerImpl.MODULE_COLUMN_NAME_PREFIX + licenseEntity.getModule(),
                        CheckBox.CHECKED ) );
    }

    /**
     * Should attach full rights user to module license.
     *
     * @throws Exception
     */
    @Test
    public void shouldAttachFullRightsUserToModuleLicense() throws Exception {

        ModuleLicenseDTO expected = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( expected );
        List< LicenseEntity > licenseEntities = new ArrayList<>();
        licenseEntities.add( licenseEntity );

        List< LicenseUserEntity > singleFullRightsLicenseUserEntity = new ArrayList<>();
        LicenseUserEntity fullRightsLicenseUserEntity = new LicenseUserEntity();
        fullRightsLicenseUserEntity.setUserEntity( fillFullRightsUserEntity() );
        fullRightsLicenseUserEntity.setLicenseEntity( licenseEntity );
        singleFullRightsLicenseUserEntity.add( fullRightsLicenseUserEntity );

        UUID userId = UUID.randomUUID();

        List< LicenseFeatureEntity > licenseFeatureEntities = new ArrayList<>(
                prepareLicenseFeatureEntitySet( licenseEntity, expected.getFeatures() ) );
        EasyMock.expect( licenseConfigDao.getModuleLicense( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( licenseEntity ).anyTimes();
        EasyMock.expect( licenseConfigDao.getLicenseFeaturesByLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( licenseFeatureEntities ).anyTimes();
        EasyMock.expect( licenseConfigDao.getModuleLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity.getModule() ) )
                .andReturn( licenseEntity );

        EasyMock.expect( userCommonManager.isUserRestricted( EasyMock.anyObject( EntityManager.class ), userId ) ).andReturn( false );

        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( singleFullRightsLicenseUserEntity ).anyTimes();
        PowerMockito.doReturn( true ).when( LicenseUtils.class, "verifyLicense", expected );
        UserDTO userDTO = prepareUserEntity();
        userDTO.setRestricted( "No" );
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDTO ).anyTimes();
        mockControl.replay();

        licenseConfiguration.manageUserLicense( SUPER_USER_ID, UUID.randomUUID(),
                new CheckBox( null, LicenseConfigurationManagerImpl.MODULE_COLUMN_NAME_PREFIX + licenseEntity.getModule(),
                        CheckBox.CHECKED ) );
    }

    /**
     * Should throw exception when user is going to add as full rights user to module license and full user limit is consumed.
     *
     * @throws Exception
     */
    @Test
    public void shouldThrowExceptionWhenUserIsGoingToAddAsFullRightsUserToModuleLicenseAndFullUserLimitIsConsumed() throws Exception {

        ModuleLicenseDTO expected = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( expected );

        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.ALLOWED_USERS_EXCEEDS_FROM_AVAILABLE.getKey(), licenseEntity.getModule() ) );

        List< LicenseEntity > licenseEntities = new ArrayList<>();
        licenseEntities.add( licenseEntity );

        UUID userId = UUID.randomUUID();
        List< LicenseFeatureEntity > licenseFeatureEntities = new ArrayList<>(
                prepareLicenseFeatureEntitySet( licenseEntity, expected.getFeatures() ) );
        EasyMock.expect( licenseConfigDao.getModuleLicense( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( licenseEntity ).anyTimes();
        EasyMock.expect( licenseConfigDao.getLicenseFeaturesByLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( licenseFeatureEntities ).anyTimes();
        EasyMock.expect( licenseConfigDao.getModuleLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity.getModule() ) )
                .andReturn( licenseEntity );
        EasyMock.expect( userCommonManager.isUserRestricted( EasyMock.anyObject( EntityManager.class ), userId ) ).andReturn( false );
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( fillTwoFullRightsLicenseUserEntities( licenseEntity ) ).anyTimes();
        PowerMockito.doReturn( true ).when( LicenseUtils.class, "verifyLicense", expected );
        UserDTO userDTO = prepareUserEntity();
        userDTO.setRestricted( "No" );
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDTO ).anyTimes();
        mockControl.replay();

        licenseConfiguration.manageUserLicense( SUPER_USER_ID, userId,
                new CheckBox( null, LicenseConfigurationManagerImpl.MODULE_COLUMN_NAME_PREFIX + licenseEntity.getModule(),
                        CheckBox.CHECKED ) );
    }

    /**
     * Should throw exception when user is going to add as full rights user to module license and limit is consumed.
     *
     * @throws Exception
     */
    @Test
    public void shouldThrowExceptionWhenUserIsGoingToAddAsRistrictedUserToModuleLicenseAndRestrictedLimitIsConsumed() throws Exception {

        ModuleLicenseDTO expected = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( expected );

        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.RESTRICTED_USERS_EXCEEDS_FROM_AVAILABLE.getKey(), licenseEntity.getModule() ) );

        List< LicenseEntity > licenseEntities = new ArrayList<>();
        licenseEntities.add( licenseEntity );

        UUID userId = UUID.randomUUID();
        List< LicenseFeatureEntity > licenseFeatureEntities = new ArrayList<>(
                prepareLicenseFeatureEntitySet( licenseEntity, expected.getFeatures() ) );
        EasyMock.expect( licenseConfigDao.getLicenseFeaturesByLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( licenseFeatureEntities ).anyTimes();
        EasyMock.expect( licenseConfigDao.getModuleLicense( EasyMock.anyObject( EntityManager.class ), licenseEntity.getModule() ) )
                .andReturn( licenseEntity ).anyTimes();
        EasyMock.expect( userCommonManager.isUserRestricted( EasyMock.anyObject( EntityManager.class ), userId ) ).andReturn( true )
                .anyTimes();
        EasyMock.expect( licenseUserDao.getLicenseUserEntitiesByModule( EasyMock.anyObject( EntityManager.class ), licenseEntity ) )
                .andReturn( fillTwoRestrictedLicenseUserEntities( licenseEntity ) ).anyTimes();
        PowerMockito.doReturn( true ).when( LicenseUtils.class, "verifyLicense", expected );
        UserDTO userDTO = prepareUserEntity();
        userDTO.setRestricted( "No" );
        mockControl.replay();

        licenseConfiguration.manageUserLicense( SUPER_USER_ID, userId,
                new CheckBox( null, LicenseConfigurationManagerImpl.MODULE_COLUMN_NAME_PREFIX + licenseEntity.getModule(),
                        CheckBox.CHECKED ) );
    }

    /**
     * Should throw exception when null module name is passed to delete license by module name.
     */
    @Test
    public void shouldThrowExceptionWhenNullModuleNameIsPassedToDeleteLicenseByModuleName() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.LICENSE_NOT_FOUND_FOR_REQUESTED_MODULE.getKey() ) );
        licenseConfiguration.deleteModuleLicenseById( EasyMock.anyObject( EntityManager.class ), NULL_MODULE_ID, SUPER_USER_ID );

    }

    /**
     * Should throw exception when non existed module name is passed to delete license by module name.
     */
    @Test
    public void shouldThrowExceptionWhenNonExistedModuleNameIsPassedToDeleteLicenseByModuleName() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.LICENSE_NOT_FOUND_FOR_REQUESTED_MODULE.getKey() ) );

        ModuleLicenseDTO license = fillLicenseDTO();
        LicenseEntity licenseEntity = prepareEntity( license );
        EasyMock.expect( licenseConfigDao.getModuleLicenseById( EasyMock.anyObject( EntityManager.class ), licenseEntity.getId() ) )
                .andReturn( licenseEntity );
        mockControl.replay();

        licenseConfiguration.deleteModuleLicenseById( EasyMock.anyObject( EntityManager.class ), UUID.randomUUID(), SUPER_USER_ID );
    }

    /**
     * Fill two full rights license user entities.
     *
     * @param licenseEntity
     *         the license entity
     *
     * @return the list
     */
    private List< LicenseUserEntity > fillTwoFullRightsLicenseUserEntities( LicenseEntity licenseEntity ) {
        List< LicenseUserEntity > fullRightsLicenseUserEntities = new ArrayList<>();

        LicenseUserEntity fullRightsUserEntity = new LicenseUserEntity();
        fullRightsUserEntity.setUserEntity( fillFullRightsUserEntity() );
        fullRightsUserEntity.setLicenseEntity( licenseEntity );
        fullRightsLicenseUserEntities.add( fullRightsUserEntity );

        LicenseUserEntity fullRightsUserEntity1 = new LicenseUserEntity();
        fullRightsUserEntity1.setUserEntity( fillFullRightsUserEntity() );
        fullRightsUserEntity1.setLicenseEntity( licenseEntity );
        fullRightsLicenseUserEntities.add( fullRightsUserEntity1 );

        return fullRightsLicenseUserEntities;
    }

    /**
     * Fill two restricted license user entities.
     *
     * @param licenseEntity
     *         the license entity
     *
     * @return the list
     */
    private List< LicenseUserEntity > fillTwoRestrictedLicenseUserEntities( LicenseEntity licenseEntity ) {
        List< LicenseUserEntity > restrictedLicenseUserEntities = new ArrayList<>();

        LicenseUserEntity restrictedUserEntity = new LicenseUserEntity();
        restrictedUserEntity.setUserEntity( fillRestrictedUserEntity() );
        restrictedUserEntity.setLicenseEntity( licenseEntity );
        restrictedLicenseUserEntities.add( restrictedUserEntity );

        LicenseUserEntity restrictedUserEntity1 = new LicenseUserEntity();
        restrictedUserEntity1.setUserEntity( fillRestrictedUserEntity() );
        restrictedUserEntity1.setLicenseEntity( licenseEntity );
        restrictedLicenseUserEntities.add( restrictedUserEntity1 );

        return restrictedLicenseUserEntities;
    }

    /**
     * Fills user entity.
     *
     * @return the user entity
     */
    private UserEntity fillFullRightsUserEntity() {
        UserEntity userEntity = new UserEntity( UUID.fromString( USER_ID ), USER_UID, USERNAME, CHANGE_ABLE );
        userEntity.setId( UUID.fromString( USER_ID ) );
        userEntity.setStatus( true );
        userEntity.setRestricted( false );
        return userEntity;
    }

    /**
     * Fill restricted user entity.
     *
     * @return the user entity
     */
    private UserEntity fillRestrictedUserEntity() {
        UserEntity userEntity = new UserEntity( UUID.fromString( USER_ID ), USER_UID, USERNAME, CHANGE_ABLE );
        userEntity.setId( UUID.fromString( USER_ID ) );
        userEntity.setStatus( true );
        userEntity.setRestricted( true );
        return userEntity;
    }

    /**
     * Fills license DTO.
     *
     * @return the licenseDTO
     */
    private ModuleLicenseDTO fillLicenseDTO() {
        ModuleLicenseDTO license = JsonUtils.jsonStreamToObject( this.getClass().getResourceAsStream( LICENSE_CONFIG_FILE_PATH ),
                ModuleLicenseDTO.class );
        return license;
    }

    /**
     * Prepares license entity.
     *
     * @param license
     *         the license
     *
     * @return the license entity
     */
    private LicenseEntity prepareEntity( ModuleLicenseDTO license ) {

        LicenseEntity licenseEntity = new LicenseEntity( license.getCustomer(), license.getVendor(), license.getReseller(),
                license.getType(), license.getModule(), license.getUserLimit().getAllowedUsers(),
                license.getUserLimit().getRestrictedUsers(), JsonUtils.convertMapToStringGernericValue( license.getAddons() ),
                license.getLicenseType(), DateFormatStandard.toDate( license.getExpiryTime() ), license.getMacAddress(),
                license.getKeyInformation() );

        licenseEntity.setFeatures( prepareLicenseFeatureEntitySet( licenseEntity, license.getFeatures() ) );

        licenseEntity.setId( UUID.randomUUID() );
        return licenseEntity;
    }

    /**
     * Prepare user entity.
     *
     * @return the user entity
     */
    private UserDTO prepareUserEntity() {

        UserDTO userEntity = new UserDTO();
        userEntity.setId( SUPER_USER_ID );
        userEntity.setFirstName( NAME_FIELD );
        userEntity.setSurName( NAME_FIELD );

        return userEntity;
    }

    /**
     * Prepare license feature entity set.
     *
     * @param licenseEntity
     *         the license entity
     * @param features
     *         the features
     *
     * @return the sets the
     */
    private Set< LicenseFeatureEntity > prepareLicenseFeatureEntitySet( LicenseEntity licenseEntity, List< String > features ) {
        Set< LicenseFeatureEntity > featureEntities = new HashSet<>();

        for ( String licenseFeature : features ) {
            featureEntities.add( new LicenseFeatureEntity( licenseEntity, licenseFeature ) );
        }

        return featureEntities;
    }

    /**
     * Mock static functions.
     *
     * @throws Exception
     *         the exception
     */
    private static void mockStaticFunctions() throws Exception {
        PowerMockito.spy( LicenseUtils.class );
        PowerMockito.doReturn( MAC_ADDRESS_IS_VALID )
                .when( LicenseUtils.class, STATIC_METHOD_MOCK_VERIFY_MAC_ADDRESS, Matchers.anyString() );
    }

    /**
     * Gets the audit log DAO.
     *
     * @return the audit log DAO
     */
    public AuditLogDAO getAuditLogDAO() {
        return auditLogDAO;
    }

    /**
     * Sets the audit log DAO.
     *
     * @param auditLogDAO
     *         the new audit log DAO
     */
    public void setAuditLogDAO( AuditLogDAO auditLogDAO ) {
        this.auditLogDAO = auditLogDAO;
    }

}
