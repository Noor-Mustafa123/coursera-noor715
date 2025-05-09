package de.soco.software.simuspace.suscore.lifecycle.manager.impl.test;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.jline.utils.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ConfigUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.data.common.model.ProjectConfiguration;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.ObjectJsonSchemaEntity;
import de.soco.software.simuspace.suscore.data.model.MasterConfiguration;
import de.soco.software.simuspace.suscore.lifecycle.dao.ObjectTypeDAO;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.impl.ObjectTypeConfigManagerImpl;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/***
 * The Class ObjectTypeConfigCacheManagerTest.**
 *
 * @author Ahmar Nadeem*@UpdateBy M.Nasir.Farooq**A test class to
 *
 *         test the
 *
 *         json configuration
 *
 *         validations at bootstrap
 */

@RunWith( PowerMockRunner.class )
@PrepareForTest( { ConfigFilePathReader.class, PropertiesManager.class } )
public class ObjectTypeConfigCacheManagerTest {

    /**
     * The Constant PROJECT_START_CONFIG_HAVING_CONTAINER_JS.
     */
    private static final String PROJECT_START_CONFIG_HAVING_CONTAINER_JS = "Project_start_config_having_container.js";

    /**
     * The Constant SECOND_VALID_PROJECT_CONFIGURATIONS_JS.
     */
    private static final String SECOND_VALID_PROJECT_CONFIGURATIONS_JS = "Project_Start_Config2.js";

    /**
     * The Constant PROJECT_CONFIGURATIONS_HAVING_DUPLICATE_IDS.
     */
    private static final String PROJECT_CONFIGURATIONS_HAVING_DUPLICATE_IDS = "Project_Start_Config3.js";

    /**
     * The Constant INVALID_PROJECT_CONFIG_WITH_CLASSNAME_HAVING_INVALID_ENTITY.
     */
    private static final String INVALID_PROJECT_CONFIG_WITH_CLASSNAME_HAVING_INVALID_ENTITY = "Project_Config_class_having_invalid_entity.js";

    /**
     * The Constant INVALID_PROJECT_CONFIG_WITH_CLASSNAME_NOT_HAVING_ENTITY.
     */
    private static final String INVALID_PROJECT_CONFIG_WITH_CLASSNAME_NOT_HAVING_ENTITY = "Project_Config_class_not_having_entity.js";

    /**
     * The Constant INVALID_PROJECT_CONFIG_WITH_INVALID_CLASSNAME.
     */
    private static final String INVALID_PROJECT_CONFIG_WITH_INVALID_CLASSNAME = "Project_Config_invalid_classname.js";

    /**
     * The Constant INVALID_PROJECT_CONFIG_INVALID_UUID_ID.
     */
    private static final String INVALID_PROJECT_CONFIG_INVALID_UUID_ID = "Project_Config_invalid_id.js";

    /**
     * The Constant INVALID_PROJECT_CONFIG_WITH_EMPTY_CLASSNAME.
     */
    private static final String INVALID_PROJECT_CONFIG_WITH_EMPTY_CLASSNAME = "Project_Config_empty_classname.js";

    /**
     * The Constant INVALID_PROJECT_CONFIG_WITH_EMPTY_ID.
     */
    private static final String INVALID_PROJECT_CONFIG_WITH_EMPTY_ID = "Project_Config_empty_id.js";

    /**
     * The Constant INVALID_PROJECT_CONFIG_WITH_MISSING_LINKS_JS.
     */
    private static final String INVALID_PROJECT_CONFIG_WITH_MISSING_LINKS_JS = "Invalid_Project_Config_Missing_Links.js";

    /**
     * The Constant PROJECT_CONFIGURATIONS_FILE_NAME.
     */
    private static final String PROJECT_CONFIGURATIONS_FILE_NAME = "Project_Start_Config.js";

    /**
     * ProjectConfig property in the JSON.
     */
    private static final String MASTER_CONFIG = "MasterConfig.js";

    /**
     * The property name that holds the master config file path
     */
    public static final String MASTER_CONFIG_PATH_PROPERTY = "jsonschema.parent.filename";

    /**
     * name string.
     */
    private static final String NAME = "name";

    /**
     * json content.
     */
    private static final String CONTENT = "{someJson:\"some json\"}";

    /**
     * Master config Id.
     */
    private static final UUID MASTER_CONFIG_ID = UUID.randomUUID();

    /**
     * for Test Case Type Id For Sus Object
     */
    private static final UUID TYPE_ID = UUID.randomUUID();

    /**
     * for Test Case Type Id For Sus Object.
     */
    private static final String STATUS_ID = UUID.randomUUID().toString();

    /**
     * The Constant STATUS_NAME.
     */
    private static final String STATUS_NAME = "WIP";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant WORKFLOW_PROJECT_FILE.
     */
    private static final String WORKFLOW_PROJECT_FILE = "WorkflowProjectConfig.js";

    /**
     * The Constant GET_MASTER_CONFIG_METHOD.
     */
    private static final String GET_MASTER_CONFIG_METHOD = "getMasterConfigurationFileNamesForWorkflows";

    /**
     * The Constant PROJECT.
     */
    private static final Object PROJECT = "Project";

    /**
     * The service manager.
     */
    private static ObjectTypeDAO objectTypeDao;

    /**
     * configCache to represent Cache Manager.
     */
    private static ObjectTypeConfigManagerImpl configManagerImpl;

    /**
     * The life cycle manager.
     */
    private static LifeCycleManager lifeCycleManager;

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
     * Test scenario 1.
     */
    private static String shouldInitCacheWhenExistingConfigDoesNotExistButAllValidationsInNewConfigurationAreSuccessful = "\n**********\n"
            + "This test verifies that when there is no any existing configuration stored in the DB\n"
            + "but all the validations are successful then the configuration should be available in the cache/memory\n";

    /**
     * Test scenario 2.
     */
    private static String shouldNotInitCacheAndThrowExceptionWhenInvalidJsonFileIsFed = "\n**********\n"
            + "This test makes sure that if there's any dependency in the\n"
            + "config file which couldn't be resolved, the server should not allow a startup. In this test the\n"
            + "Project_Crash_Config_Invalid.js file is used which does not\n"
            + "hold the DataObject project, hence the server should restrict the booting process.\n";

    /**
     * Test scenario 3.
     */
    private static String shouldNotInitCacheWhenMissingEntityInConfigAndUserFlagFalse = "\n**********\n"
            + "When there are some objects in the existing configuration\n"
            + "like \"MissingEntity\" was a part of LINKS or CONTAINS array of Project\n"
            + "(object) in the Crash configuration and the new\n"
            + "configurlation doesn't contain the MissingEntity and also, the user has opted not to allow loss of data,\n"
            + "then the server should throw exception and should not init the cache.\n";

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    private static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Setup things before it starts testing.
     */
    @BeforeClass
    public static void beforeClass() {
        objectTypeDao = mockControl().createMock( ObjectTypeDAO.class );
        lifeCycleManager = mockControl().createMock( LifeCycleManager.class );
        configManagerImpl = new ObjectTypeConfigManagerImpl();
        configManagerImpl.setObjectTypeDAO( objectTypeDao );
        configManagerImpl.setLifeCycleManager( lifeCycleManager );
    }

    /**
     * Reset all the existing configuration before every test run.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setUp() throws Exception {
        mockControl().resetToNice();
    }

    /**
     * Should cache configurations by init method when all validations are successful.
     *
     * @throws Exception
     *         the exception
     * @Description This test verifies that when all the validations are successful then the configuration should be available in the
     * cache/memory
     */
    @Test
    public void shouldCacheConfigurationsByInitMethodWhenAllValidationsAreSuccessful() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        Log.info( shouldInitCacheWhenExistingConfigDoesNotExistButAllValidationsInNewConfigurationAreSuccessful );
        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME );

        mockControl().replay();
        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_CONFIGURATIONS_FILE_NAME );

        configManagerImpl.init();
        Map< String, ProjectConfiguration > actualProjectConfigurationsMap = configManagerImpl.getProjectConfigurationsMap();
        Assert.assertTrue( actualProjectConfigurationsMap.containsKey( PROJECT_CONFIGURATIONS_FILE_NAME ) );
        Assert.assertEquals( expectedProjectConfiguration, actualProjectConfigurationsMap.get( PROJECT_CONFIGURATIONS_FILE_NAME ) );
    }

    /**
     * Should not init cache when id of object is empty in config.
     *
     * @throws Exception
     *         the exception
     * @Description When a new configuration is fed but any ID against an object is empty then the system should not let the configuration
     * be loaded and throw and exception.
     */
    @Test
    public void shouldNotInitCacheWhenIdOfObjectIsEmptyInConfig() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.OBJECT_ID_CANNOT_BE_EMPTY_IN_CONFIG.getKey(), PROJECT,
                INVALID_PROJECT_CONFIG_WITH_EMPTY_ID ) );
        mockStaticGetMasterConfigurationFileNames( INVALID_PROJECT_CONFIG_WITH_EMPTY_ID );
        createCommonExpectations();

        configManagerImpl.init();
    }

    /**
     * Should not init cache when class name of object is empty in config.
     *
     * @throws Exception
     *         the exception
     * @Description When a new configuration is fed but any className against an object is empty then the system should not let the
     * configuration be loaded and throw and exception.
     */
    @Test
    public void shouldNotInitCacheWhenClassNameOfObjectIsEmptyInConfig() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.OBJECT_CLASSNAME_CANNOT_BE_EMPTY.getKey(), PROJECT,
                INVALID_PROJECT_CONFIG_WITH_EMPTY_CLASSNAME ) );
        mockStaticGetMasterConfigurationFileNames( INVALID_PROJECT_CONFIG_WITH_EMPTY_CLASSNAME );
        createCommonExpectations();

        configManagerImpl.init();
    }

    /**
     * Should not init cache when id of object is invalid in config.
     *
     * @throws Exception
     *         the exception
     * @Description When a new configuration is fed but any ID against an object is invalid (not a valid UUID) then the system should not
     * let the configuration be loaded and throw and exception.
     */
    @Test
    public void shouldNotInitCacheWhenIdOfObjectIsInvalidInConfig() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.INVALID_OBJECT_ID_PROVIDED_IN_CONFIG.getKey(), PROJECT,
                INVALID_PROJECT_CONFIG_INVALID_UUID_ID ) );
        mockStaticGetMasterConfigurationFileNames( INVALID_PROJECT_CONFIG_INVALID_UUID_ID );
        createCommonExpectations();

        configManagerImpl.init();
    }

    /**
     * Should not init cache when class name of object is not valid in config.
     *
     * @throws Exception
     *         the exception
     * @Description When a new configuration is fed but any className against an object does not exist in the system then the system should
     * not let the configuration be loaded and throw and exception.
     */
    @Test
    public void shouldNotInitCacheWhenClassNameOfObjectIsNotValidInConfig() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        createCommonExpectations();
        ProjectConfiguration projectConfiguration = loadProjectConfigurations( INVALID_PROJECT_CONFIG_WITH_INVALID_CLASSNAME );

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.CLASS_DOES_NOT_EXIST_FOR_OBJECT.getKey(),
                projectConfiguration.getEntityConfig().get( FIRST_INDEX ).getClassName(), PROJECT ) );

        mockStaticGetMasterConfigurationFileNames( INVALID_PROJECT_CONFIG_WITH_INVALID_CLASSNAME );
        configManagerImpl.init();
    }

    /**
     * Should not init cache when DTO class name of object does not have entity class field.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotInitCacheWhenDTOClassNameOfObjectDoesNotHaveEntityClassField() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        createCommonExpectations();
        ProjectConfiguration projectConfiguration = loadProjectConfigurations( INVALID_PROJECT_CONFIG_WITH_CLASSNAME_NOT_HAVING_ENTITY );

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.ENTITY_CLASS_DOES_NOT_EXIST_IN_DTO.getKey(),
                projectConfiguration.getEntityConfig().get( FIRST_INDEX ).getClassName() ) );

        mockStaticGetMasterConfigurationFileNames( INVALID_PROJECT_CONFIG_WITH_CLASSNAME_NOT_HAVING_ENTITY );
        configManagerImpl.init();
    }

    /**
     * Should not init cache when DTO class name of object have entity field with invalid type.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotInitCacheWhenDTOClassNameOfObjectHaveEntityFieldWithInvalidType() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        createCommonExpectations();
        ProjectConfiguration projectConfiguration = loadProjectConfigurations(
                INVALID_PROJECT_CONFIG_WITH_CLASSNAME_HAVING_INVALID_ENTITY );

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.DTO_DOES_NOT_HAVE_VALID_ENTITY_CLASS.getKey(),
                projectConfiguration.getEntityConfig().get( FIRST_INDEX ).getClassName() ) );

        mockStaticGetMasterConfigurationFileNames( INVALID_PROJECT_CONFIG_WITH_CLASSNAME_HAVING_INVALID_ENTITY );
        configManagerImpl.init();
    }

    /**
     * Should init cache when no change in new configurations.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldInitCacheWhenNoChangeInNewConfigurations() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME );

        Map< String, ProjectConfiguration > projectConfigurations = loadConfigurationIntoMap(
                ConfigFilePathReader.getMasterConfigurationFileNames( ObjectTypeConfigManagerImpl.MASTER_CONFIG_PATH_PROPERTY ) );
        ObjectJsonSchemaEntity masterSchemaEntity = fillMasterJsonSchemaEntity(
                ConfigFilePathReader.getMasterConfigurationFileNames( ObjectTypeConfigManagerImpl.MASTER_CONFIG_PATH_PROPERTY ),
                projectConfigurations );

        EasyMock.expect( objectTypeDao.loadLastJsonSchemaConfiguration( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( String.class ) ) )
                .andReturn( masterSchemaEntity );
        mockControl.replay();
        configManagerImpl.init();

        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_CONFIGURATIONS_FILE_NAME );
        Map< String, ProjectConfiguration > actualProjectConfigurationsMap = configManagerImpl.getProjectConfigurationsMap();
        Assert.assertTrue( actualProjectConfigurationsMap.containsKey( PROJECT_CONFIGURATIONS_FILE_NAME ) );
        Assert.assertEquals( expectedProjectConfiguration, actualProjectConfigurationsMap.get( PROJECT_CONFIGURATIONS_FILE_NAME ) );

    }

    /**
     * Should add new configurations to cache when configurations are valid and no data loss occurred with new configurations.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldAddNewConfigurationsToCacheWhenConfigurationsAreValidAndNoDataLossOccurredWithNewConfigurations() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME, SECOND_VALID_PROJECT_CONFIGURATIONS_JS );

        List< ConfigUtil.Config > oldConfigurations = Arrays.asList(
                new ConfigUtil.Config( PROJECT_CONFIGURATIONS_FILE_NAME, PROJECT_CONFIGURATIONS_FILE_NAME ) );
        Map< String, ProjectConfiguration > projectConfigurations = loadConfigurationIntoMap( oldConfigurations );

        ObjectJsonSchemaEntity masterSchemaEntity = fillMasterJsonSchemaEntity( oldConfigurations, projectConfigurations );

        EasyMock.expect( objectTypeDao.loadLastJsonSchemaConfiguration( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( String.class ) ) )
                .andReturn( masterSchemaEntity );
        mockControl.replay();
        configManagerImpl.init();

        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_CONFIGURATIONS_FILE_NAME );
        Map< String, ProjectConfiguration > actualProjectConfigurationsMap = configManagerImpl.getProjectConfigurationsMap();
        Assert.assertTrue( actualProjectConfigurationsMap.containsKey( PROJECT_CONFIGURATIONS_FILE_NAME ) );
        Assert.assertEquals( expectedProjectConfiguration, actualProjectConfigurationsMap.get( PROJECT_CONFIGURATIONS_FILE_NAME ) );
        ProjectConfiguration expectedNewProjectConfiguration = loadProjectConfigurations( SECOND_VALID_PROJECT_CONFIGURATIONS_JS );
        Assert.assertTrue( actualProjectConfigurationsMap.containsKey( SECOND_VALID_PROJECT_CONFIGURATIONS_JS ) );
        Assert.assertEquals( expectedNewProjectConfiguration,
                actualProjectConfigurationsMap.get( SECOND_VALID_PROJECT_CONFIGURATIONS_JS ) );
    }

    /**
     * Mock static method of properties util class.
     *
     * @throws Exception
     *         the exception
     */
    private void mockStaticMethodOfPropertiesUtilClass() throws Exception {
        PowerMockito.spy( PropertiesManager.class );
        PowerMockito.doReturn( true ).when( PropertiesManager.class, "isMasterLocation" );
    }

    /**
     * Should not init cache when data loss occurred in contains but user flag false.
     *
     * @throws Exception
     *         the exception
     * @Description When there are some objects in the existing configuration like "MissingObject" was a part of contains array of Project
     * (object) in the NVH configuration and the new configuration doesn't contain the MissingObject and the user has opted to allow loss of
     * data, then the server should not init the cache and throw an exception.
     */
    @Test
    public void shouldNotInitCacheWhenDataLossOccurredInContainsButUserFlagFalse() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MISSING_CONFIG_FILE.getKey(), PROJECT_CONFIGURATIONS_FILE_NAME ) );

        Log.info( shouldNotInitCacheWhenMissingEntityInConfigAndUserFlagFalse );

        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( SECOND_VALID_PROJECT_CONFIGURATIONS_JS );

        List< ConfigUtil.Config > oldConfigurations = Arrays.asList(
                new ConfigUtil.Config( PROJECT_CONFIGURATIONS_FILE_NAME, PROJECT_CONFIGURATIONS_FILE_NAME ) );
        Map< String, ProjectConfiguration > oldProjectConfigurations = loadConfigurationIntoMap( oldConfigurations );

        ObjectJsonSchemaEntity masterSchemaEntity = fillMasterJsonSchemaEntity( oldConfigurations, oldProjectConfigurations );

        EasyMock.expect( objectTypeDao.loadLastJsonSchemaConfiguration( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( String.class ) ) )
                .andReturn( masterSchemaEntity );

        Object entityObject = initializeDTOzEntity(
                oldProjectConfigurations.get( PROJECT_CONFIGURATIONS_FILE_NAME ).getEntityConfig().get( FIRST_INDEX ).getClassName() );

        EasyMock.expect( objectTypeDao.getCount( EasyMock.anyObject( EntityManager.class ), entityObject.getClass(), false ) )
                .andReturn( 3L );
        mockControl.replay();
        configManagerImpl.init();

        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_CONFIGURATIONS_FILE_NAME );
        Map< String, ProjectConfiguration > actualProjectConfigurationsMap = configManagerImpl.getProjectConfigurationsMap();
        Assert.assertTrue( actualProjectConfigurationsMap.containsKey( PROJECT_CONFIGURATIONS_FILE_NAME ) );
        Assert.assertEquals( expectedProjectConfiguration, actualProjectConfigurationsMap.get( PROJECT_CONFIGURATIONS_FILE_NAME ) );
        ProjectConfiguration expectedNewProjectConfiguration = loadProjectConfigurations( SECOND_VALID_PROJECT_CONFIGURATIONS_JS );
        Assert.assertTrue( actualProjectConfigurationsMap.containsKey( SECOND_VALID_PROJECT_CONFIGURATIONS_JS ) );
        Assert.assertEquals( expectedNewProjectConfiguration,
                actualProjectConfigurationsMap.get( SECOND_VALID_PROJECT_CONFIGURATIONS_JS ) );
    }

    /**
     * Should get project type by project configuration file name when configurations are cached.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldGetProjectTypeByProjectConfigurationFileNameWhenConfigurationsAreCached() throws Exception {

        Log.info( shouldInitCacheWhenExistingConfigDoesNotExistButAllValidationsInNewConfigurationAreSuccessful );
        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME );
        mockControl().replay();
        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_CONFIGURATIONS_FILE_NAME );

        configManagerImpl.init();
        SuSObjectModel actualProjectType = configManagerImpl.getProjectModelByConfigLabel( PROJECT_CONFIGURATIONS_FILE_NAME );
        Assert.assertEquals( expectedProjectConfiguration.getEntityConfig().get( FIRST_INDEX ), actualProjectType );

    }

    /**
     * Should get object types by project configuration file name when configurations are cached.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldGetObjectTypesByProjectConfigurationFileNameWhenConfigurationsAreCached() throws Exception {

        Log.info( shouldInitCacheWhenExistingConfigDoesNotExistButAllValidationsInNewConfigurationAreSuccessful );
        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME );
        mockControl().replay();
        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_CONFIGURATIONS_FILE_NAME );

        configManagerImpl.init();

        List< SuSObjectModel > actualObjectTypes = configManagerImpl.getObjectTypesByConfigName( PROJECT_CONFIGURATIONS_FILE_NAME );
        Assert.assertEquals( expectedProjectConfiguration.getEntityConfig(), actualObjectTypes );

    }

    /**
     * Should get object type by object id and project configuration file name when configurations are cached.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldGetObjectTypeByObjectIdAndProjectConfigurationFileNameWhenConfigurationsAreCached() throws Exception {

        Log.info( shouldInitCacheWhenExistingConfigDoesNotExistButAllValidationsInNewConfigurationAreSuccessful );
        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME );
        mockControl().replay();
        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_CONFIGURATIONS_FILE_NAME );

        configManagerImpl.init();

        SuSObjectModel actualObjectTypes = configManagerImpl.getObjectTypeByIdAndConfigName(
                expectedProjectConfiguration.getEntityConfig().get( FIRST_INDEX ).getId(), PROJECT_CONFIGURATIONS_FILE_NAME );
        Assert.assertEquals( expectedProjectConfiguration.getEntityConfig().get( FIRST_INDEX ), actualObjectTypes );

    }

    /**
     * This function tests that if loadLastJsonSchemaConfiguration is called, the system should load the last saved configuration without
     * fail.
     */
    @Test
    public void shouldLoadLastJsonSchemaConfigurationWhenValidInputDataProvided() {
        ObjectJsonSchemaEntity expectedEntity = new ObjectJsonSchemaEntity( MASTER_CONFIG_ID, NAME, CONTENT, null );
        EasyMock.expect( objectTypeDao.loadLastJsonSchemaConfiguration( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( String.class ) ) ).andReturn( expectedEntity );
        mockControl.replay();
        ObjectJsonSchemaEntity actualEntity = configManagerImpl.loadLastJsonSchemaConfiguration( EasyMock.anyObject( EntityManager.class ),
                MASTER_CONFIG );
        Assert.assertNotNull( actualEntity );
        Assert.assertEquals( expectedEntity.getName(), actualEntity.getName() );
        Assert.assertEquals( expectedEntity.getContent(), actualEntity.getContent() );
        Assert.assertEquals( expectedEntity.getId(), actualEntity.getId() );
        Assert.assertEquals( expectedEntity.getParentSchema(), actualEntity.getParentSchema() );
    }

    /**
     * This function tests that if the entity while saving the new configuration contains all null values, then the system should not save
     * the configuration.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldNotSaveNewConfigurationAndThrowExceptionWhenAllAttributesAreNull() throws SusException {
        thrown.expect( SusException.class );
        thrown.expectMessage(
                "[Object Name cannot be empty or null., Object Type Content cannot be empty or null, Object Type created on cannot be empty or null]" );

        ObjectJsonSchemaEntity expectedEntity = new ObjectJsonSchemaEntity( null, null, null, null );
        EasyMock.expect( objectTypeDao.saveNewConfiguration( EasyMock.anyObject( EntityManager.class ), expectedEntity ) )
                .andReturn( expectedEntity );
        mockControl.replay();
        configManagerImpl.saveNewConfiguration( EasyMock.anyObject( EntityManager.class ), expectedEntity );
    }

    /**
     * This function tests that if the entity while saving the new configuration contains all null values except the name, then the system
     * should not save the configuration.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldNotSaveNewConfigurationAndThrowExceptionWhenContentIsNull() throws SusException {
        thrown.expect( SusException.class );
        thrown.expectMessage( "[Object Type Content cannot be empty or null, Object Type created on cannot be empty or null]" );

        ObjectJsonSchemaEntity entity = new ObjectJsonSchemaEntity( null, NAME, null, null );
        EasyMock.expect( objectTypeDao.saveNewConfiguration( EasyMock.anyObject( EntityManager.class ), entity ) ).andReturn( entity );
        mockControl.replay();
        configManagerImpl.saveNewConfiguration( EasyMock.anyObject( EntityManager.class ), entity );
    }

    /**
     * This function tests that if the entity while saving the new configuration contains createdOn as null, then the system should not save
     * the configuration.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldNotSaveNewConfigurationAndThrowExceptionWhenCreatedOnIsNull() throws SusException {
        thrown.expect( SusException.class );
        thrown.expectMessage( "[Object Type created on cannot be empty or null]" );

        ObjectJsonSchemaEntity entity = new ObjectJsonSchemaEntity( null, NAME, CONTENT, null );
        EasyMock.expect( objectTypeDao.saveNewConfiguration( EasyMock.anyObject( EntityManager.class ), entity ) ).andReturn( entity );
        mockControl.replay();
        configManagerImpl.saveNewConfiguration( EasyMock.anyObject( EntityManager.class ), entity );
    }

    /**
     * Should throw exception if there is some problem or error during saving new configuration when name is empty for method
     * saveNewConfiguration it without fail.
     */
    @Test
    public void shoulThrowExceptionWhenThereIsNullNamePassedInObjectJsonSchemaEntityInSaveNewConfigurationMethod() {
        thrown.expect( SusException.class );
        thrown.expectMessage( "[Object Name cannot be empty or null., Object Type created on cannot be empty or null]" );

        ObjectJsonSchemaEntity entity = new ObjectJsonSchemaEntity( null, null, CONTENT, null );
        ObjectJsonSchemaEntity savedEntity = new ObjectJsonSchemaEntity( MASTER_CONFIG_ID, NAME, CONTENT, null );
        EasyMock.expect( objectTypeDao.saveNewConfiguration( EasyMock.anyObject( EntityManager.class ), entity ) ).andReturn( savedEntity );
        mockControl.replay();

        configManagerImpl.saveNewConfiguration( EasyMock.anyObject( EntityManager.class ), entity );

    }

    /**
     * Should throw exception if there is some problem or error during saving new configuration when content is empty for method
     * saveNewConfiguration it without fail.
     */
    @Test
    public void shoulThrowExceptionWhenThereIsNullContentPassedInObjectJsonSchemaEntityInSaveNewConfigurationMethod() {
        thrown.expect( SusException.class );
        thrown.expectMessage( "[Object Type Content cannot be empty or null, Object Type created on cannot be empty or null]" );

        ObjectJsonSchemaEntity entity = new ObjectJsonSchemaEntity( null, NAME, null, null );
        ObjectJsonSchemaEntity savedEntity = new ObjectJsonSchemaEntity( MASTER_CONFIG_ID, NAME, CONTENT, null );
        EasyMock.expect( objectTypeDao.saveNewConfiguration( EasyMock.anyObject( EntityManager.class ), entity ) ).andReturn( savedEntity );
        mockControl.replay();

        configManagerImpl.saveNewConfiguration( EasyMock.anyObject( EntityManager.class ), entity );

    }

    /**
     * Should return empty container types when get container object types of project by config name and no container type is cached in
     * configurations.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnEmptyContainerTypesWhenGetContainerObjectTypesOfProjectByConfigNameAndNoContainerTypeIsCachedInConfigurations()
            throws Exception {

        Log.info( shouldInitCacheWhenExistingConfigDoesNotExistButAllValidationsInNewConfigurationAreSuccessful );
        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME );
        mockControl().replay();

        configManagerImpl.init();
        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_CONFIGURATIONS_FILE_NAME );

        UUID objectTypeId = UUID.fromString( expectedProjectConfiguration.getEntityConfig().get( FIRST_INDEX ).getId() );
        List< SuSObjectModel > actualContainerTypes = configManagerImpl.getContainerObjectTypesByObjectTypeId( objectTypeId,
                PROJECT_CONFIGURATIONS_FILE_NAME );
        Assert.assertTrue( actualContainerTypes.isEmpty() );
    }

    /**
     * Should get container object types of project by config name when configurations are cached.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldGetContainerObjectTypesOfProjectByConfigNameWhenConfigurationsAreCached() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        Log.info( shouldInitCacheWhenExistingConfigDoesNotExistButAllValidationsInNewConfigurationAreSuccessful );
        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_START_CONFIG_HAVING_CONTAINER_JS );
        mockControl().replay();
        configManagerImpl.init();
        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_START_CONFIG_HAVING_CONTAINER_JS );
        UUID objectTypeId = UUID.fromString( expectedProjectConfiguration.getEntityConfig().get( FIRST_INDEX ).getId() );
        List< SuSObjectModel > actualContainerTypes = configManagerImpl.getContainerObjectTypesByObjectTypeId( objectTypeId,
                PROJECT_START_CONFIG_HAVING_CONTAINER_JS );
        Assert.assertFalse( actualContainerTypes.isEmpty() );
        Assert.assertEquals( expectedProjectConfiguration.getEntityConfig().get( ConstantsInteger.INTEGER_VALUE_TWO ),
                actualContainerTypes.get( FIRST_INDEX ) );

    }

    @Test
    public void shouldSuccessFullyGetStatusDtoWhenValidTypeIdIGiven() throws Exception {
        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME );

        StatusDTO expectedStatus = getfilledStatus();
        EasyMock.expect( lifeCycleManager.getStatusByLifeCycleNameAndStatusId( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expectedStatus ).anyTimes();
        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_CONFIGURATIONS_FILE_NAME );
        UUID objectTypeId = UUID.fromString( expectedProjectConfiguration.getEntityConfig().get( FIRST_INDEX ).getId() );

        mockControl.replay();
        configManagerImpl.init();

        StatusDTO actual = configManagerImpl.getStatusByIdandObjectType( objectTypeId, STATUS_ID, PROJECT_CONFIGURATIONS_FILE_NAME );
        Assert.assertEquals( expectedStatus.getId(), actual.getId() );
        Assert.assertEquals( expectedStatus.getName(), actual.getName() );
    }

    @Test
    public void shouldNotGetStatusDtoWhenInValidTypeIdIGiven() throws Exception {

        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME );

        StatusDTO expectedStatus = null;
        EasyMock.expect( lifeCycleManager.getStatusByLifeCycleNameAndStatusId( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expectedStatus ).anyTimes();

        mockControl.replay();
        configManagerImpl.init();

        StatusDTO actual = configManagerImpl.getStatusByIdandObjectType( TYPE_ID, STATUS_ID, PROJECT_CONFIGURATIONS_FILE_NAME );
        Assert.assertEquals( expectedStatus, actual );
    }

    @Test
    public void shouldSuccessFullyGetDefaultStatusDtoWhenValidTypeIdIGiven() throws Exception {
        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME );

        StatusDTO expectedStatus = getfilledStatus();
        EasyMock.expect( lifeCycleManager.getDefaultStatusByLifeCycleId( EasyMock.anyString() ) ).andReturn( expectedStatus ).anyTimes();
        ProjectConfiguration expectedProjectConfiguration = loadProjectConfigurations( PROJECT_CONFIGURATIONS_FILE_NAME );
        UUID objectTypeId = UUID.fromString( expectedProjectConfiguration.getEntityConfig().get( FIRST_INDEX ).getId() );

        mockControl.replay();
        configManagerImpl.init();

        StatusDTO actual = configManagerImpl.getDefaultStatusByObjectTypeId( objectTypeId.toString(), PROJECT_CONFIGURATIONS_FILE_NAME );
        Assert.assertEquals( expectedStatus.getId(), actual.getId() );
        Assert.assertEquals( expectedStatus.getName(), actual.getName() );
    }

    @Test
    public void shouldNotGetDefaultStatusDtoWhenInValidTypeIdIGiven() throws Exception {

        createCommonExpectations();
        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIGURATIONS_FILE_NAME );

        StatusDTO expectedStatus = null;
        EasyMock.expect( lifeCycleManager.getDefaultStatusByLifeCycleId( EasyMock.anyString() ) ).andReturn( expectedStatus ).anyTimes();

        mockControl.replay();
        configManagerImpl.init();

        StatusDTO actual = configManagerImpl.getDefaultStatusByObjectTypeId( TYPE_ID.toString(), PROJECT_CONFIGURATIONS_FILE_NAME );
        Assert.assertEquals( expectedStatus, actual );
    }

    /**
     * Gets the filled status.
     *
     * @return the filled status
     */
    private StatusDTO getfilledStatus() {
        return new StatusDTO( STATUS_ID, STATUS_NAME );
    }

    /**
     * A utility function to set common expectations using easy mock.
     *
     * @throws NoSuchFieldException
     *         the no such field exception
     * @throws IllegalAccessException
     *         the illegal access exception
     */
    private void createCommonExpectations() throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtils.setFinalStaticField( ConstantsKaraf.class, ConstantsKaraf.STATIC_CONSTATN_FIELD_KARAF_CONF_PATH,
                ConstantsString.TEST_RESOURCE_PATH ); // For a String
    }

    /**
     * Mock static get master configuration file names.
     *
     * @param projectConfigurationsFileName
     *         the project configurations file name
     *
     * @throws Exception
     *         the exception
     */
    private void mockStaticGetMasterConfigurationFileNames( String... projectConfigurationsFileName ) throws Exception {
        List< String > list = new ArrayList<>();
        list.add( WORKFLOW_PROJECT_FILE );
        PowerMockito.spy( ConfigFilePathReader.class );
        PowerMockito.doReturn( Arrays.asList( projectConfigurationsFileName ) )
                .when( ConfigFilePathReader.class, ConfigFilePathReader.STATIC_METHOD_GET_MASTER_CONFIGURATION_FILE_NAMES,
                        ObjectTypeConfigManagerImpl.MASTER_CONFIG_PATH_PROPERTY );
        PowerMockito.doReturn( list ).when( ConfigFilePathReader.class, GET_MASTER_CONFIG_METHOD, Matchers.anyString() );
    }

    /**
     * Load project configurations.
     *
     * @param projectConfigurationsFileName
     *         the project configurations file name
     *
     * @return the project configuration
     *
     * @throws FileNotFoundException
     *         the file not found exception
     */
    private ProjectConfiguration loadProjectConfigurations( String projectConfigurationsFileName ) throws FileNotFoundException {
        return JsonUtils.jsonStreamToObject(
                new FileInputStream( new File( ConstantsKaraf.KARAF_CONF_PATH + projectConfigurationsFileName ) ),
                ProjectConfiguration.class );
    }

    /**
     * The function loads the new configuration into Map after converting to the object type format. It further helps in dealing with the
     * data.
     *
     * @param configFileNamesList
     *         the config file names list
     *
     * @return the map
     *
     * @throws FileNotFoundException
     *         the file not found exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private Map< String, ProjectConfiguration > loadConfigurationIntoMap( final List< ConfigUtil.Config > configFileNamesList )
            throws FileNotFoundException {

        Map< String, ProjectConfiguration > newConfigObjectsMap = new HashMap<>();

        for ( String subConfigFileName : ConfigUtil.fileNames( configFileNamesList ) ) {
            newConfigObjectsMap.put( subConfigFileName, loadProjectConfigurations( subConfigFileName ) );
        }
        return newConfigObjectsMap;
    }

    /**
     * Fill master json schema entity.
     *
     * @param configFileNames
     *         the config file names
     * @param projectConfigurations
     *         the project configurations
     *
     * @return the object json schema entity
     *
     * @Description This function is called at the boot time to validate the MasterConfig.js file and all others referenced in it. It then
     * caches the configuration for further use.
     */
    private ObjectJsonSchemaEntity fillMasterJsonSchemaEntity( List< ConfigUtil.Config > configFileNames,
            final Map< String, ProjectConfiguration > projectConfigurations ) {

        Date createdOn = new Date();

        MasterConfiguration configurationFiles = new MasterConfiguration( configFileNames );

        ObjectJsonSchemaEntity masterEntity = new ObjectJsonSchemaEntity();
        masterEntity.setId( UUID.randomUUID() );
        masterEntity.setName( MASTER_CONFIG );
        masterEntity.setContent( JsonUtils.toJson( configurationFiles ) );
        masterEntity.setCreatedOn( createdOn );

        List< ObjectJsonSchemaEntity > childEntities = new ArrayList<>();

        for ( Entry< String, ProjectConfiguration > projectConfig : projectConfigurations.entrySet() ) {
            ObjectJsonSchemaEntity childEntity = new ObjectJsonSchemaEntity();
            childEntity.setId( UUID.randomUUID() );
            childEntity.setName( projectConfig.getKey() );
            childEntity.setContent( JsonUtils.toJson( projectConfig.getValue() ) );
            childEntity.setParentSchema( masterEntity );
            childEntity.setCreatedOn( createdOn );
            childEntities.add( childEntity );
        }
        masterEntity.setObjectJsonSchemaChildList( childEntities );
        return masterEntity;
    }

    /**
     * Initialize DT oz entity.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    private Object initializeDTOzEntity( String className ) {

        Class< ? > entityClass = ReflectionUtils.getFieldTypeByName( initializeObject( className ).getClass(), "ENTITY_CLASS" );
        return initializeObject( entityClass.getName() );
    }

    /**
     * Initialize object.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    private static Object initializeObject( String className ) {

        try {
            return Class.forName( className ).newInstance();
        } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ) );
        }
    }

}
