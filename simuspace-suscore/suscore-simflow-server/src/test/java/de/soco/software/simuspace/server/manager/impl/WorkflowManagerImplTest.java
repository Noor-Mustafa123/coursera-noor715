package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.server.dao.WorkflowDAO;
import de.soco.software.simuspace.server.manager.CategoryManager;
import de.soco.software.simuspace.server.manager.FavoriteManager;
import de.soco.software.simuspace.server.manager.LicenseTokenManager;
import de.soco.software.simuspace.server.manager.WorkflowUserManager;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.constants.simflow.StatusConstants;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.core.dao.LinkDAO;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.AclCommonSecurityIdentityDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.common.model.StatusConfigDTO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.AclClassEntity;
import de.soco.software.simuspace.suscore.data.entity.AclEntryEntity;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SystemContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.ProjectDTO;
import de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.lifecycle.model.LifeCycleDTO;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.permissions.dao.AclEntryDAO;
import de.soco.software.simuspace.suscore.permissions.dao.AclObjectIdentityDAO;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.WorkflowConfiguration;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.processing.WFExecutionManager;
import de.soco.software.suscore.jsonschema.model.OVAConfigTab;

/**
 * Junit Test Cases for Workflow Manager Impl Class.
 *
 * @author Nosheen.Sharif
 */
@PrepareForTest( WorkflowManagerImpl.class )
public class WorkflowManagerImplTest {

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant for work flow description.
     */
    private static final String TEST_DESCRIPTION = "Test Description";

    /**
     * The Constant for work flow entity name.
     */
    private static final String WORK_FLOW_ENTITY_NAME = "test workflow entity name";

    /**
     * The Constant for work flow id.
     */
    private static final UUID WORK_FLOW_ID = UUID.randomUUID();

    /**
     * The Constant for work flow user entity id.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * Dummy INVALID UUID for test cases.
     */
    private static final UUID INVALID_UUID = null;

    /**
     * The Constant for work flow category.
     */
    private static final UUID CATEGORY_ID = UUID.randomUUID();

    /**
     * The Constant for work flow version.
     */
    private static final int VERSION_ID = 1;

    /**
     * The Constant for work flow token key.
     */
    public static final String WORKFLOW_TOKEN_KEY = "wfKey";

    /**
     * The Constant for invalid version of user work flow.
     */
    private static final int USER_VERSION_ID_INVALID = 0;

    /**
     * The Constant for work flow comments.
     */
    private static final String COMMENTS = "Test comments";

    /**
     * The Constant for user name.
     */
    private static final String USER_NAME = "user";

    /**
     * The Constant for Work-in-progress status.
     */
    private static final String WIP = "WIP";

    /**
     * The Constant configFileInvalidPath.
     */
    private static final String configFileInvalidPath = "\\path\to\file";

    /**
     * The Constant for empty work flow id.
     */
    private static final String EMPTY_WORKFLOW_ID = "";

    /**
     * The Constant for invalid work flow id.
     */
    private static final String INVALID_WORKFLOW_ID = "012edhhe";

    /**
     * The Constant user id.
     */
    private static final UUID DEFAULT_USER_ID_MINUS_ONE = UUID.randomUUID();

    /**
     * Dummy project Id.
     */
    private static final UUID PROJECT_ID = UUID.randomUUID();

    /**
     * The Constant STR_UUID_PRENT_ID.
     */
    private static final String STR_UUID_PRENT_ID = UUID.randomUUID().toString();

    /**
     * The Constant for work-in-process default id.
     */
    private static final Integer WIP_ID = 1;

    /**
     * The Constant for null.
     */
    private static final WorkflowEntity INVALID_WORKFLOW_ENTITY = null;

    /**
     * The Constant for inavlid object status id.
     */
    private static final Integer INVALID_ID = 5;

    /**
     * The Constant ZERO for dummy input use.
     */
    private static final Integer ZERO = 0;

    /**
     * The Constant MINUS_ONE for dummy input.
     */
    private static final String MINUS_ONE = "-1";

    /**
     * workflow type objects for status config.
     */
    private static final String WORKFLOW_TYPE = "Workflow";

    /**
     * The Constant for expecting exception with this error.
     */
    private static final String ERROR_WORKFLOW_IS_NOT_EXECUTABLE = "WOrkflow is not executable according to configration";

    /**
     * The Constant VIEW_INDEX.
     */
    private static final int VIEW_INDEX = 0;

    /**
     * The Constant READ_INDEX.
     */
    private static final int READ_INDEX = 1;

    /**
     * The Constant WRITE_INDEX.
     */
    private static final int WRITE_INDEX = 2;

    /**
     * The Constant EXECUTE_INDEX.
     */
    private static final int EXECUTE_INDEX = 7;

    /**
     * The Constant DELETE_INDEX.
     */
    private static final int DELETE_INDEX = 3;

    /**
     * The Constant RESTORE_INDEX.
     */
    private static final int RESTORE_INDEX = 4;

    /**
     * The Constant MANAGE_INDEX.
     */
    private static final int MANAGE_INDEX = 5;

    /**
     * The Constant CREATE_NEW_OBJECT_INDEX.
     */
    private static final int CREATE_NEW_OBJECT_INDEX = 6;

    /**
     * The Constant KILL_INDEX.
     */
    private static final int KILL_INDEX = 8;

    /**
     * The Constant SHARE_INDEX.
     */
    private static final int SHARE_INDEX = 9;

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant USER.
     */
    private static final String USER = "User";

    /**
     * The Constant RESTRICTED_USER.
     */
    private static final String NOT_RESTRICTED = "No";

    /**
     * The Constant RESTRICTED.
     */
    private static final String RESTRICTED = "Yes";

    /**
     * Dummy Version Id for test Cases.
     */
    private static final int DEFAULT_VERSION_ID = 1;

    /**
     * The Constant TAB_NAME_PERMISSIONS.
     */
    private static final String TAB_NAME_PERMISSIONS = "Permissions";

    /**
     * The Constant TAB_NAME_VERSIONS.
     */
    private static final String TAB_NAME_VERSIONS = "Versions";

    /**
     * The Constant TAB_NAME_DESIGNER.
     */
    private static final String TAB_NAME_DESIGNER = "Designer";

    /**
     * The Constant TAB_KEY_DESIGNER.
     */
    private static final String TAB_KEY_DESIGNER = "designer";

    /**
     * The Constant OBJECT.
     */
    private static final String OBJECT = "Object";

    /**
     * The Constant SID_ID.
     */
    private static final UUID SID_ID = UUID.randomUUID();

    /**
     * The Constant CLASS_ID.
     */
    private static final UUID CLASS_ID = UUID.randomUUID();

    /**
     * The Constant OBJECT_IDENTITY_ID.
     */
    private static final UUID OBJECT_IDENTITY_ID = UUID.randomUUID();

    /**
     * The Constant PARENT_OBJECT_IDENTITY_ID.
     */
    private static final UUID PARENT_OBJECT_IDENTITY_ID = UUID.randomUUID();

    /**
     * The Constant ENTRY_ID.
     */
    private static final UUID ENTRY_ID = UUID.randomUUID();

    /**
     * The Constant SUPER_USER_ID.
     */
    private static final String SUPER_USER_ID = "1e98a77c-a0be-4983-9137-d9a8acd0ea8b";

    /**
     * The Constant TABLE_COLUMN_LIST.
     */
    private static final String TABLE_COLUMN_LIST = "[{\"data\":\"name\",\"title\":\"Name\",\"filter\":\"text\",\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"text\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":\"name\",\"manage\":true},\"name\":\"name\",\"orderNum\":0},{\"data\":\"type\",\"title\":\"Type\",\"filter\":\"text\",\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"text\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":\"type\",\"manage\":true},\"name\":\"type\",\"orderNum\":0},{\"data\":\"permissionDTOs.0.value\",\"title\":\"View\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.0.manage\"},\"name\":\"permissionDTOs-View\",\"orderNum\":0},{\"data\":\"permissionDTOs.1.value\",\"title\":\"ViewAuditLog\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.1.manage\"},\"name\":\"permissionDTOs-ViewAuditLog\",\"orderNum\":0},{\"data\":\"permissionDTOs.2.value\",\"title\":\"Read\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.2.manage\"},\"name\":\"permissionDTOs-Read\",\"orderNum\":0},{\"data\":\"permissionDTOs.3.value\",\"title\":\"Write\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.3.manage\"},\"name\":\"permissionDTOs-Write\",\"orderNum\":0},{\"data\":\"permissionDTOs.4.value\",\"title\":\"Execute\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.4.manage\"},\"name\":\"permissionDTOs-Execute\",\"orderNum\":0},{\"data\":\"permissionDTOs.5.value\",\"title\":\"Export\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.5.manage\"},\"name\":\"permissionDTOs-Export\",\"orderNum\":0},{\"data\":\"permissionDTOs.6.value\",\"title\":\"Delete\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.6.manage\"},\"name\":\"permissionDTOs-Delete\",\"orderNum\":0},{\"data\":\"permissionDTOs.7.value\",\"title\":\"Restore\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.7.manage\"},\"name\":\"permissionDTOs-Restore\",\"orderNum\":0},{\"data\":\"permissionDTOs.8.value\",\"title\":\"CreateNewObject\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.8.manage\"},\"name\":\"permissionDTOs-CreateNewObject\",\"orderNum\":0},{\"data\":\"permissionDTOs.9.value\",\"title\":\"CreateNewVersion\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.9.manage\"},\"name\":\"permissionDTOs-CreateNewVersion\",\"orderNum\":0},{\"data\":\"permissionDTOs.10.value\",\"title\":\"Update\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.10.manage\"},\"name\":\"permissionDTOs-Update\",\"orderNum\":0},{\"data\":\"permissionDTOs.11.value\",\"title\":\"Kill\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.11.manage\"},\"name\":\"permissionDTOs-Kill\",\"orderNum\":0},{\"data\":\"permissionDTOs.12.value\",\"title\":\"Share\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.12.manage\"},\"name\":\"permissionDTOs-Share\",\"orderNum\":0}]";

    /**
     * The Constant PROJECT_NAME.
     */
    private static final String PROJECT_NAME = "project";

    /**
     * The Constant PROJECT_DESCRPTION.
     */
    private static final String PROJECT_DESCRPTION = "project description";

    /**
     * The Constant SYNC_CONTEXT_TITLE.
     */
    private static final String SYNC_CONTEXT_TITLE = "Sync Files";

    /**
     * The Constant FIRST_INDEX_OF_LIST.
     */
    private static final int FIRST_INDEX_OF_LIST = 0;

    /**
     * The Constant DEFAULT_LIFECYCLE_STATUS.
     */
    private static final String WIP_LIFECYCLE_STATUS_ID = "553536c7-71ec-409d-8f48-ec779a98a68e";

    /**
     * The Constant WIP_LIFECYCLE_STATUS_NAME.
     */
    private static final String WIP_LIFECYCLE_STATUS_NAME = "WIP";

    /**
     * The Constant ALL_VISIBILITY.
     */
    private static final String ALL_VISIBILITY = "all";

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The user.
     */
    private UserDTO user;

    /**
     * The interface which provides workflowMangerImpl functions.
     */
    private WorkflowManagerImpl manager;

    /**
     * The interface which provides Favorite crud function.
     */
    private FavoriteManager favoriteManager;

    /**
     * The interface which provides category crud function.
     */
    private CategoryManager categoryManager;

    /**
     * The interface which provides workflow crud function.
     */
    private WorkflowDAO workflowDao;

    /**
     * The interface which provides userManager crud function.
     */
    private WorkflowUserManager userManager;

    /**
     * The LicenseTokenManager object for mocking and preparing expected results.
     */
    private LicenseTokenManager licenseTokenManager;

    /**
     * The workflowDto object for mocking and preparing expected results.
     */
    private WorkflowDTO workflowDTO;

    /**
     * The userDto object for mocking and preparing expected results.
     */
    private UserDTO userDto;

    /**
     * The workflowEntity object for mocking and preparing expected results.
     */
    private WorkflowEntity workFlowEntity;

    /**
     * The userEntity object for mocking and preparing expected results.
     */
    private UserEntity userEntity;

    /**
     * The UserManager object for mocking and preparing expected results.
     */
    private WorkflowUserManager userManagerImpl;

    /**
     * The sus DAO.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The acl common security identity DAO.
     */
    private AclCommonSecurityIdentityDAO aclCommonSecurityIdentityDAO;

    /**
     * The object identity DAO.
     */
    private AclObjectIdentityDAO objectIdentityDAO;

    /**
     * The acl entry DAO.
     */
    private AclEntryDAO aclEntryDAO;

    /**
     * The class entity.
     */
    private AclClassEntity classEntity;

    /**
     * The object identity entity.
     */
    private AclObjectIdentityEntity objectIdentityEntity;

    /**
     * The acl object identity entities.
     */
    private List< AclObjectIdentityEntity > aclObjectIdentityEntities;

    /**
     * The acl entry entities.
     */
    private List< AclEntryEntity > aclEntryEntities;

    /**
     * The parent object identity entity.
     */
    private AclObjectIdentityEntity parentObjectIdentityEntity;

    /**
     * The user common DAO.
     */
    private UserCommonDAO userCommonDAO;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    /**
     * The execution manager.
     */
    private WFExecutionManager executionManager;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * The Constant LOG_MESSAGE_HAS_VISIBILTY.
     */
    private static final String LOG_MESSAGE_HAS_VISIBILTY = " has visibilty: ";

    /**
     * The Constant LOG_MESSAGE_WORKFLOW_WITH_STATUS.
     */
    private static final String LOG_MESSAGE_WORKFLOW_WITH_STATUS = "Workflow with status : ";

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * The link DAO.
     */
    private LinkDAO linkDAO;

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Set up work flow manager for test.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setUp() throws Exception {
        mockControl().resetToNice();
        manager = new WorkflowManagerImpl();

        PowerMockito.spy( WorkflowManagerImpl.class );
        userManager = mockControl().createMock( WorkflowUserManager.class );
        workflowDao = mockControl().createMock( WorkflowDAO.class );
        categoryManager = mockControl().createMock( CategoryManager.class );
        favoriteManager = mockControl().createMock( FavoriteManager.class );
        executionManager = mockControl().createMock( WFExecutionManager.class );
        configManager = mockControl().createMock( ObjectTypeConfigManager.class );
        manager.setUserManager( userManager );
        manager.setWorkflowDao( workflowDao );
        licenseTokenManager = new LicenseTokenManagerImpl();
        licenseTokenManager.setCheckedOutLicenseMap( new HashMap<>() );
        manager.setLicenseTokenManager( licenseTokenManager );
        susDAO = mockControl().createMock( SuSGenericObjectDAO.class );
        manager.setSusDAO( susDAO );
        lifeCycleManager = mockControl.createMock( LifeCycleManager.class );
        manager.setLifeCycleManager( lifeCycleManager );
        selectionManager = mockControl().createMock( SelectionManager.class );
        contextMenuManager = mockControl().createMock( ContextMenuManager.class );
        licenseManager = mockControl.createMock( LicenseManager.class );
        locationManager = mockControl.createMock( LocationManager.class );

        manager.setSelectionManager( selectionManager );
        manager.setContextMenuManager( contextMenuManager );
        manager.setExecutionManager( executionManager );
        userCommonManager = mockControl().createMock( UserCommonManager.class );
        userCommonDAO = mockControl().createMock( UserCommonDAO.class );
        permissionManager = mockControl.createMock( PermissionManager.class );
        aclCommonSecurityIdentityDAO = mockControl.createMock( AclCommonSecurityIdentityDAO.class );
        manager.setPermissionManager( permissionManager );
        manager.setUserCommonManager( userCommonManager );
        manager.setLicenseManager( licenseManager );
        manager.setLocationManager( locationManager );

        objectIdentityDAO = mockControl.createMock( AclObjectIdentityDAO.class );
        aclEntryDAO = mockControl.createMock( AclEntryDAO.class );
        userManagerImpl = mockControl().createMock( WorkflowUserManager.class );
        fillProjectEntityOfTypeData();
        fillClassEntity();
        fillObjectIdentity( classEntity, fillSecurityIdentity( SID_ID ) );
        aclObjectIdentityEntities = new ArrayList<>();
        aclObjectIdentityEntities.add( objectIdentityEntity );
        aclEntryEntities = new ArrayList<>();
        aclEntryEntities.add( fillEntryEntity( objectIdentityEntity, fillSecurityIdentity( SID_ID ) ) );
        parentObjectIdentityEntity = fillParentObjectIdentity( fillClassEntity(), fillSecurityIdentity( SID_ID ) );
        manager.setConfigManager( configManager );
        linkDAO = mockControl.createMock( LinkDAO.class );
        manager.setLinkDAO( linkDAO );

        fillWorkFlowEntity();
        fillWorkflowDto();
        fillUserEntity();
        fillUserDto();
        fillSimcoreUser();
    }

    /**
     * Configration test file invalid path should give config empty.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void configrationTestFileInvalidPathShouldGiveConfigEmpty() throws JsonSerializationException {
        final File initialConfigFile = new File( configFileInvalidPath );
        WorkflowConfiguration config = null;
        try ( InputStream targetConfigStream = new FileInputStream( initialConfigFile ) ) {
            config = JsonUtils.jsonToObject( targetConfigStream, WorkflowConfiguration.class );
            thrown.expect( JsonSerializationException.class );
            thrown.expectMessage( "An IO Exception should have been thrown." );
        } catch ( final IOException e ) {
            Assert.assertNull( config );
        }

    }

    /**
     * If valid input is given it should save work flow successfully.
     */
    @Ignore
    @Test
    public void whenValidInputIsGivenItShouldSaveWorkflowSuccessfully() {

        EasyMock.expect( workflowDao.saveWorkflow( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( WorkflowEntity.class ) ) )
                .andReturn( workFlowEntity ).anyTimes();
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();
        EasyMock.expect( userManager.prepareUserEntity( EasyMock.anyObject( UserDTO.class ) ) ).andReturn( userEntity ).anyTimes();

        EasyMock.expect(
                        susDAO.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( fillWorkFlowEntity() ).anyTimes();

        EasyMock.expect( susDAO.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( new ArrayList<>() );

        fillSimcoreUser();
        EasyMock.expect( userManagerImpl.getUser( Mockito.anyObject() ) ).andReturn( user ).anyTimes();
        mockControl.replay();

        final WorkflowDTO savedworkflowDTO = manager.saveWorkflow( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID_MINUS_ONE,
                DEFAULT_USER_ID_MINUS_ONE.toString(), workflowDTO, "" );
        Assert.assertNotNull( savedworkflowDTO );
        Assert.assertEquals( savedworkflowDTO.getId().toString(), workFlowEntity.getComposedId().getId().toString() );
        Assert.assertEquals( savedworkflowDTO.getVersion().getId(), workFlowEntity.getComposedId().getVersionId() );
        Assert.assertEquals( savedworkflowDTO.getDescription(), workFlowEntity.getDescription() );

    }

    /**
     * When null id is given it should return message.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenNullIdIsGivenItShouldReturnMessage() throws SusException {

        thrown.expect( SusException.class );

        thrown.expectMessage( LOG_MESSAGE_WORKFLOW_WITH_STATUS + null + LOG_MESSAGE_HAS_VISIBILTY + null );

        EasyMock.expect( workflowDao.getWorkflowVersionsWithoutDefinition( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID ) )
                .andReturn( null );
        mockControl.replay();

        mockControl.verify();
    }

    /**
     * When invalid workflow id given it should throw message.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenInvalidWorkflowIdGivenItShouldThrowMessage() throws Exception {
        fillWorkflowDto();

        EasyMock.expect( licenseManager.isLicenseAddedAgainstUserForModule( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        mockControl.replay();
        thrown.expect( SusException.class );

        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, INVALID_WORKFLOW_ID ) );

        manager.updateWorkflow( Mockito.anyObject(), DEFAULT_USER_ID_MINUS_ONE, INVALID_WORKFLOW_ID, workflowDTO, Mockito.anyObject() );

    }

    /**
     * If valid input is given it should save new work flow successfully.
     */
    @Ignore
    @Test
    public void whenValidInputIsGivenItShouldSaveNewWorkflowSuccessfully() {
        EasyMock.expect( susDAO.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( new ArrayList<>() );
        EasyMock.expect( workflowDao.saveWorkflow( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( WorkflowEntity.class ) ) )
                .andReturn( workFlowEntity ).anyTimes();
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();
        EasyMock.expect( userManager.prepareUserEntity( EasyMock.anyObject( UserDTO.class ) ) ).andReturn( userEntity ).anyTimes();
        EasyMock.expect(
                        susDAO.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( fillWorkFlowEntity() ).anyTimes();
        EasyMock.expect( susDAO.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( new ArrayList<>() );
        fillSimcoreUser();
        EasyMock.expect( userManagerImpl.getUser( Mockito.anyObject() ) ).andReturn( user ).anyTimes();
        mockControl.replay();

        final LatestWorkFlowDTO savedworkflowDTO = manager.saveWorkflow( EasyMock.anyObject( EntityManager.class ),
                DEFAULT_USER_ID_MINUS_ONE, DEFAULT_USER_ID_MINUS_ONE.toString(), prepareLatestWorkflowFromWorkflowDTO( workflowDTO ), "" );
        Assert.assertNotNull( savedworkflowDTO );
        Assert.assertEquals( savedworkflowDTO.getId().toString(), workFlowEntity.getComposedId().getId().toString() );
        Assert.assertEquals( savedworkflowDTO.getVersion().getId(), workFlowEntity.getComposedId().getVersionId() );
        Assert.assertEquals( savedworkflowDTO.getDescription(), workFlowEntity.getDescription() );

    }

    /**
     * When work flow id is empty it should throw message.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenWorkflowIdIsEmptyItShouldThrowMessage() throws Exception {

        thrown.expect( SusException.class );

        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ID_SHOULD_NOT_EMPTY ) );

        manager.updateWorkflow( Mockito.anyObject(), DEFAULT_USER_ID_MINUS_ONE, EMPTY_WORKFLOW_ID, workflowDTO, Mockito.anyObject() );

    }

    /**
     * When version id is given it should update work flow version id.
     *
     * @throws Exception
     *         the exception
     */
    @Ignore
    @Test
    public void whenVersionIdIsGivenItShouldUpdateWorkFlowVersionID() throws Exception {

        PowerMockito.spy( UUID.class );
        // to make the Visiblity to owner
        userDto.setId( userEntity.getId().toString() );
        fillWorkflowDto();
        fillWorkFlowEntity();
        final List< WorkflowEntity > list = new ArrayList<>();
        final String str_workflow_id = WORK_FLOW_ID.toString();
        list.add( workFlowEntity );
        workFlowEntity.setVersionId( workFlowEntity.getVersionId() + 1 );
        workFlowEntity.getCreatedBy().setId( DEFAULT_USER_ID_MINUS_ONE );

        EasyMock.expect( userManager.prepareUserEntity( userDto ) ).andReturn( userEntity );
        EasyMock.expect( userManager.prepareUserDtoFromUserEntity( userEntity ) ).andReturn( userDto ).anyTimes();
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();

        EasyMock.expect(
                        workflowDao.updateWorkflow( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( WorkflowEntity.class ) ) )
                .andReturn( workFlowEntity );

        EasyMock.expect( workflowDao.getWorkflowVersionsWithoutDefinition( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID ) )
                .andReturn( list ).anyTimes();

        EasyMock.expect( workflowDao.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyInt() ) ).andReturn( workFlowEntity ).anyTimes();
        EasyMock.expect( userManagerImpl.getUser( EasyMock.anyObject() ) ).andReturn( user ).anyTimes();
        mockControl().replay();
        workflowDTO.getCreatedBy().setId( DEFAULT_USER_ID_MINUS_ONE.toString() );
        final WorkflowDTO retDto = manager.updateWorkflow( EasyMock.anyObject(), DEFAULT_USER_ID_MINUS_ONE, str_workflow_id, workflowDTO,
                Mockito.anyObject() );
        Assert.assertEquals( VERSION_ID + 1, retDto.getVersion().getId() );
        Assert.assertEquals( workFlowEntity.getDescription(), retDto.getDescription() );
        Assert.assertEquals( workFlowEntity.getName(), retDto.getName() );
        Assert.assertEquals( workFlowEntity.getDefinition(), retDto.getDefinition() );
    }

    /**
     * Should update workflow when valid inputs provided.
     *
     * @throws Exception
     *         the exception
     */
    @Ignore
    @Test
    public void shouldUpdateWorkflowWhenValidInputsProvided() throws Exception {

        PowerMockito.spy( UUID.class );
        // to make the Visiblity to owner
        userDto.setId( userEntity.getId().toString() );
        fillWorkflowDto();
        fillWorkFlowEntity();
        final List< WorkflowEntity > list = new ArrayList<>();
        final String str_workflow_id = WORK_FLOW_ID.toString();
        list.add( workFlowEntity );
        workFlowEntity.setVersionId( workFlowEntity.getVersionId() + 1 );
        workFlowEntity.getCreatedBy().setId( DEFAULT_USER_ID_MINUS_ONE );

        EasyMock.expect( userManager.prepareUserEntity( userDto ) ).andReturn( userEntity );
        EasyMock.expect( userManager.prepareUserDtoFromUserEntity( userEntity ) ).andReturn( userDto ).anyTimes();
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();

        EasyMock.expect(
                        workflowDao.updateWorkflow( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( WorkflowEntity.class ) ) )
                .andReturn( workFlowEntity );

        EasyMock.expect( workflowDao.getWorkflowVersionsWithoutDefinition( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID ) )
                .andReturn( list ).anyTimes();

        EasyMock.expect( workflowDao.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyInt() ) ).andReturn( workFlowEntity ).anyTimes();
        fillSimcoreUser();
        EasyMock.expect( userManagerImpl.getUser( EasyMock.anyObject() ) ).andReturn( user ).anyTimes();
        mockControl().replay();
        workflowDTO.getCreatedBy().setId( DEFAULT_USER_ID_MINUS_ONE.toString() );
        final LatestWorkFlowDTO retDto = manager.updateWorkflow( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID_MINUS_ONE,
                str_workflow_id, prepareLatestWorkflowFromWorkflowDTO( workflowDTO ), Mockito.anyObject(), false );
        Assert.assertEquals( VERSION_ID + 1, retDto.getVersion().getId() );
        Assert.assertEquals( workFlowEntity.getDescription(), retDto.getDescription() );
        Assert.assertEquals( workFlowEntity.getName(), retDto.getName() );

    }

    /**
     * Should return list of work flow data transfer object with previous versions.
     *
     * @throws Exception
     *         the exception
     */
    @Ignore
    @Test
    public void shouldReturnListOfWorkFlowDtoWithPreviousVersions() throws Exception {
        // to make the Visiblity to owner
        userDto.setId( userEntity.getId().toString() );
        fillWorkFlowEntity();
        final List< WorkflowEntity > list = new ArrayList<>();
        list.add( workFlowEntity );

        EasyMock.expect(
                        workflowDao.getWorkflowVersionsById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( list ).anyTimes();

        fillSimcoreUser();
        EasyMock.expect( userManagerImpl.getUser( Mockito.anyObject() ) ).andReturn( user ).anyTimes();
        EasyMock.expect( userManager.prepareUserDtoFromUserEntity( userEntity ) ).andReturn( userDto ).anyTimes();
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();
        final StatusConfigDTO statusConfigDTO = getStatusConfig();
        statusConfigDTO.setVisible( StatusConstants.VISIBLE_ALL );
        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( statusConfigDTO );
        mockControl().replay();

        final List< LatestWorkFlowDTO > actual = manager.getWorkflowVersionsById( DEFAULT_USER_ID_MINUS_ONE, WORK_FLOW_ID.toString() );
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual.isEmpty() );
        for ( final LatestWorkFlowDTO workflowDTO : actual ) {
            Assert.assertNotNull( "name not null", workflowDTO.getName() );
            Assert.assertNotNull( "id not null", workflowDTO.getId() );
            Assert.assertTrue( "version should not be zero", workflowDTO.getVersion().getId() > USER_VERSION_ID_INVALID );
            Assert.assertNotNull( "description not null", workflowDTO.getDescription() );
        }
    }

    /**
     * When work flow id or version id is not valid it should throw message.
     *
     * @throws SusException
     *         the simuspace exception
     */
    @Test
    public void whenWorkflowIdOrVersionIdIsNotValidItShouldThrowMessage() throws SusException {

        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID_AND_VERSION_ID, WORK_FLOW_ID, VERSION_ID ) );
        EasyMock.expect( workflowDao.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) ).andReturn( null );
        mockControl().replay();

        manager.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID_MINUS_ONE, WORK_FLOW_ID.toString(),
                VERSION_ID );

    }

    /**
     * When work flow id is not valid it should throw message.
     */
    @Test
    public void whenWorkflowIdIsNotValidItShouldThrowMessage() {

        thrown.expect( SusException.class );

        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, INVALID_WORKFLOW_ID ) );
        mockControl().replay();

        manager.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID_MINUS_ONE, INVALID_WORKFLOW_ID,
                VERSION_ID );

    }

    /**
     * When valid work flow id and version is given it should return work flow.
     */
    @Test
    public void whenValidWorkflowIdAndVersionIsGivenItShouldReturnWorkflow() {

        userDto.setId( userEntity.getId().toString() );
        EasyMock.expect( workflowDao.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) ).andReturn( workFlowEntity ).anyTimes();

        EasyMock.expect( userManagerImpl.getUser( EasyMock.anyObject() ) ).andReturn( user ).anyTimes();
        EasyMock.expect( userManager.prepareUserDtoFromUserEntity( userEntity ) ).andReturn( userDto ).anyTimes();
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSModelObject() ).anyTimes();
        mockControl.replay();

        final WorkflowDTO dto = manager.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID_MINUS_ONE,
                WORK_FLOW_ID.toString(), VERSION_ID );
        Assert.assertNotNull( dto );
        Assert.assertEquals( VERSION_ID, dto.getVersion().getId() );
        Assert.assertEquals( workFlowEntity.getDescription(), dto.getDescription() );
        Assert.assertEquals( workFlowEntity.getName(), dto.getName() );
        Assert.assertEquals( workFlowEntity.getDefinition(), dto.getDefinition() );

    }

    /**
     * When valid workflow id and version is given it should return new workflow.
     */
    @Test
    public void whenValidWorkflowIdAndVersionIsGivenItShouldReturnNewWorkflow() {
        userDto.setId( userEntity.getId().toString() );
        EasyMock.expect( workflowDao.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) ).andReturn( workFlowEntity ).anyTimes();
        EasyMock.expect( userManagerImpl.getUser( EasyMock.anyObject() ) ).andReturn( user ).anyTimes();
        EasyMock.expect( userManager.prepareUserDtoFromUserEntity( userEntity ) ).andReturn( userDto ).anyTimes();
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSModelObject() ).anyTimes();
        mockControl.replay();

        final LatestWorkFlowDTO dto = manager.getNewWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ),
                DEFAULT_USER_ID_MINUS_ONE, WORK_FLOW_ID.toString(), VERSION_ID );
        Assert.assertNotNull( dto );
        Assert.assertEquals( VERSION_ID, dto.getVersion().getId() );
        Assert.assertEquals( workFlowEntity.getDescription(), dto.getDescription() );
        Assert.assertEquals( workFlowEntity.getName(), dto.getName() );

    }

    /**
     * When valid workflow id and version is given it should return new workflow.
     */
    @Ignore
    @Test
    public void whenValidWorkflowIdIsGivenItShouldReturnNewWorkflow() {

        userDto.setId( userEntity.getId().toString() );
        final List< WorkflowEntity > expected = new ArrayList<>();
        expected.add( workFlowEntity );
        EasyMock.expect( workflowDao.getWorkflowVersionsWithoutDefinition( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();

        fillSimcoreUser();
        EasyMock.expect( userManagerImpl.getUser( EasyMock.anyObject() ) ).andReturn( user ).anyTimes();
        EasyMock.expect( userManager.prepareUserDtoFromUserEntity( userEntity ) ).andReturn( userDto ).anyTimes();
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();
        mockControl().replay();

        final LatestWorkFlowDTO dto = manager.getNewWorkflowById( DEFAULT_USER_ID_MINUS_ONE, WORK_FLOW_ID.toString() );
        Assert.assertNotNull( dto );
        Assert.assertEquals( VERSION_ID, dto.getVersion().getId() );
        Assert.assertEquals( workFlowEntity.getDescription(), dto.getDescription() );
        Assert.assertEquals( workFlowEntity.getName(), dto.getName() );

    }

    /**
     * When valid category id is given it should return work flow data transfer object list by category id.
     */
    @Ignore
    @Test
    public void whenValidCategoryIdIsGivenItShouldReturnWorkflowDtoListByCategoryId() {
        fillUserEntity();
        final List< UUID > ids = new ArrayList<>();
        ids.add( UUID.randomUUID() );
        // to make the Visiblity to owner
        userDto.setId( userEntity.getId().toString() );
        final List< WorkflowEntity > expected = new ArrayList<>();
        expected.add( workFlowEntity );
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();
        EasyMock.expect(
                        workflowDao.getWorkflowListByCategoryId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( ids ).anyTimes();
        EasyMock.expect( workflowDao.getWorkflowVersionsWithoutDefinition( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
        fillSimcoreUser();
        EasyMock.expect( userManagerImpl.getUser( Mockito.anyObject() ) ).andReturn( user ).anyTimes();
        EasyMock.expect( userManager.prepareUserDtoFromUserEntity( userEntity ) ).andReturn( userDto ).anyTimes();
        mockControl().replay();

        final List< WorkflowDTO > actual = manager.getWorkflowListByCategoryId( DEFAULT_USER_ID_MINUS_ONE, CATEGORY_ID.toString() );
        Assert.assertNotNull( actual );
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual.isEmpty() );
        for ( final WorkflowDTO workflowDTO : actual ) {
            Assert.assertNotNull( "name not null", workflowDTO.getName() );
            Assert.assertNotNull( "id not null", workflowDTO.getId() );
            Assert.assertTrue( "version should not be zero", workflowDTO.getVersion().getId() > USER_VERSION_ID_INVALID );
            Assert.assertNotNull( "description not null", workflowDTO.getDescription() );
        }
    }

    /**
     * When category id is minus one it should return empty work flow data transfer object list by category id.
     */
    @Ignore
    @Test
    public void whenCategoryIdIsMinusOneItShouldReturnEmptyWorkflowDtoListByCategoryId() {
        // to make the Visiblity to owner
        final List< UUID > ids = new ArrayList<>();
        ids.add( UUID.randomUUID() );
        userDto.setId( userEntity.getId().toString() );
        final List< WorkflowEntity > expected = new ArrayList<>();
        expected.add( workFlowEntity );
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();
        EasyMock.expect( workflowDao.getWorkflowListWithoutCategory( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( ids )
                .anyTimes();
        EasyMock.expect( workflowDao.getWorkflowVersionsWithoutDefinition( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
        EasyMock.expect( userManagerImpl.getUser( Mockito.anyObject() ) ).andReturn( user ).anyTimes();
        EasyMock.expect( userManager.prepareUserDtoFromUserEntity( userEntity ) ).andReturn( userDto ).anyTimes();

        mockControl().replay();
        final List< WorkflowDTO > actual = manager.getWorkflowListByCategoryId( DEFAULT_USER_ID_MINUS_ONE, MINUS_ONE );
        Assert.assertNotNull( actual );
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual.isEmpty() );
        for ( final WorkflowDTO workflowDTO : actual ) {
            Assert.assertNotNull( "name not null", workflowDTO.getName() );
            Assert.assertNotNull( "id not null", workflowDTO.getId() );
            Assert.assertTrue( "version should not be zero", workflowDTO.getVersion().getId() > USER_VERSION_ID_INVALID );
            Assert.assertNotNull( "description not null", workflowDTO.getDescription() );
        }
    }

    /**
     * When invalid category id is given it should throw exception.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void WhenInvalidCategoryIdIsGivenItShouldThrowException() throws SusException {
        final String invalid_uuid = "1223-2334-22-334";
        thrown.expect( SusException.class );

        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.INVALID_UUID, invalid_uuid ) );

        mockControl().replay();

        manager.getWorkflowListByCategoryId( DEFAULT_USER_ID_MINUS_ONE, invalid_uuid );

    }

    /**
     * When id null entity is given it should return message.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenNullIdIsGivenInRerunItShouldReturnMessage() throws SusException {
        final Job job = new JobImpl();

        final VersionDTO version = new VersionDTO( VERSION_ID );
        job.setWorkflowVersion( version );

        EasyMock.expect( workflowDao.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID,
                job.getWorkflowVersion().getId() ) ).andReturn( INVALID_WORKFLOW_ENTITY );
        mockControl.replay();

        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, WORK_FLOW_ID ) );
        manager.getWorkflowByIdWithRefJobParams( DEFAULT_USER_ID_MINUS_ONE, WORK_FLOW_ID.toString(), job );
        mockControl.verify();
    }

    /**
     * When job has different definition it should update in workflow.
     */
    @Test
    public void whenJobHasDifferentDefinitionFromWorkFlowItShouldUpdateInWorkflow() {
        userDto.setId( userEntity.getId().toString() );

        final Job job = new JobImpl();
        WorkflowDTO workflowDTO = new WorkflowDTO();

        final VersionDTO version = new VersionDTO( VERSION_ID );
        job.setWorkflowVersion( version );

        EasyMock.expect( workflowDao.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID,
                job.getWorkflowVersion().getId() ) ).andReturn( workFlowEntity );

        fillSimcoreUser();
        EasyMock.expect( userManagerImpl.getUser( EasyMock.anyObject() ) ).andReturn( user ).anyTimes();
        EasyMock.expect( userManager.prepareUserDtoFromUserEntity( userEntity ) ).andReturn( userDto ).anyTimes();
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSModelObject() ).anyTimes();

        mockControl.replay();

        workflowDTO = manager.getWorkflowByIdWithRefJobParams( DEFAULT_USER_ID_MINUS_ONE, WORK_FLOW_ID.toString(), job );

        Assert.assertNotNull( workflowDTO );
    }

    /**
     * When workflow status is invalid it should throw error.
     */
    @Ignore
    @Test
    public void whenWorkflowStatusIsExecutableFalseOnInvalidItShouldThrowError() {
        userDto.setId( userEntity.getId().toString() );

        final Job job = new JobImpl();
        final VersionDTO version = new VersionDTO( VERSION_ID );
        job.setWorkflowVersion( version );

        EasyMock.expect( workflowDao.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID,
                job.getWorkflowVersion().getId() ) ).andReturn( workFlowEntity );
        mockControl.replay();

        thrown.expect( SusException.class );
        thrown.expectMessage( ERROR_WORKFLOW_IS_NOT_EXECUTABLE );
        workflowDTO = manager.getWorkflowByIdWithRefJobParams( DEFAULT_USER_ID_MINUS_ONE, WORK_FLOW_ID.toString(), job );
    }

    /**
     * ********************************** GET DATAOBJECT ***************************************.
     */

    /**
     * Should not get workflow when in valid workflow id is given.
     */
    @Test
    public void shouldNotGetWorkflowWhenInValidWorkflowIdIsGiven() {

        final DataObjectEntity expected = null;
        EasyMock.expect( susDAO.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
        mockControl.replay();
        final LatestWorkFlowDTO actual = manager.getWorkflowById( EasyMock.anyObject( EntityManager.class ), USER_ID, INVALID_UUID );
        Assert.assertEquals( expected, actual );

    }

    /**
     * ******************************** Single Object UI *****************************************.
     *
     * @throws Exception
     *             the exception
     */

    /**
     * Should success fully get tabs view list for workflow when valid id is given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessFullyGetTabsViewListForWorkflowWhenValidIdIsGiven() throws Exception {
        final List< SubTabsUI > expected = getGeneralTabsList();
        EasyMock.expect(
                        susDAO.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( fillWorkFlowEntity() ).anyTimes();
        EasyMock.expect(
                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( true ).anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSModelObject() ).anyTimes();
        EasyMock.expect( licenseManager.checkIfFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( true ).anyTimes();
        mockControl.replay();
        final List< SubTabsUI > actual = manager.getTabsViewWorkflowUI( WORK_FLOW_ID.toString(), WORK_FLOW_ID ).getTabs();
        Assert.assertFalse( actual.isEmpty() );
        for ( int i = 0; i < expected.size(); i++ ) {
            Assert.assertEquals( expected.get( i ), actual.get( i ) );
        }

    }

    /**
     * Should success fully get table columns for single workflow DTO when id exists in database.
     */
    @Test
    public void shouldSuccessFullyGetTableColumnsForSingleWorkflowDTOWhenIdExistsInDatabase() {

        final List< TableColumn > expected = prepareWorkflowTableUI();
        EasyMock.expect(
                        susDAO.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( fillWorkFlowEntity() ).anyTimes();
        mockControl.replay();
        final List< TableColumn > actual = manager.getWorkflowSingleUI( PROJECT_ID.toString() );
        Assert.assertNotNull( actual );
        for ( final TableColumn actualTableColumn : actual ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }

        }
    }

    /**
     * Should successfully get versions view when valid workflow id is given.
     */
    @Test
    public void shouldSuccessfullyGetVersionsViewWhenValidWorkflowIdIsGiven() {

        final List< TableColumn > expected = prepareWorkflowTableUI();

        EasyMock.expect( susDAO.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillWorkFlowEntity() ).anyTimes();
        mockControl.replay();
        final List< TableColumn > actual = manager.getWorkflowVersionsUI( WORK_FLOW_ID.toString() );
        for ( final TableColumn actualTableColumn : actual ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getData().equals( actualTableColumn.getData() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }

        }
    }

    /**
     * Should successfully get filtered list of workflow versions when valid workflow id is given.
     */
    @Test
    public void shouldSuccessfullyGetFilteredListOfWorkflowVersionsWhenValidWorkflowIdIsGiven() {
        final FiltersDTO expectedFilter = fillFilterForDataTable();
        final List< SuSEntity > expectedList = new ArrayList<>();
        expectedList.add( fillWorkFlowEntity() );
        EasyMock.expect( susDAO.getAllFilteredVersionsById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( Class.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( List.class ), EasyMock.anyObject( List.class ) ) ).andReturn( expectedList ).anyTimes();
        getPermittedTrue();
        mockAccessControlEntities();
        final WorkflowEntity expected = fillWorkFlowEntity();
        EasyMock.expect( susDAO.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSModelObject() ).anyTimes();

        EasyMock.expect( linkDAO.getLinkedRelationByChildId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( Arrays.asList( new Relation() ) ).anyTimes();

        mockControl.replay();
        final FilteredResponse< LatestWorkFlowDTO > actual = manager.getWorkflowVersions( USER_ID, WORK_FLOW_ID, expectedFilter );
        Assert.assertNotNull( actual );
        Assert.assertEquals( expectedList.size(), actual.getData().size() );
        Assert.assertEquals( expectedFilter.getDraw(), actual.getDraw() );
        final LatestWorkFlowDTO actualObjectDTO = actual.getData().get( FIRST_INDEX );
        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getComposedId().getId(), actualObjectDTO.getId() );
        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getComposedId().getVersionId(), actualObjectDTO.getVersion().getId() );
        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getName(), actualObjectDTO.getName() );
        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getCreatedOn(), actualObjectDTO.getCreatedOn() );
    }

    /**
     * ********************************** GET Project ***************************************.
     */

    /**
     * Should Successfully Get ProjectDto When Valid projectId Is Given
     */
    @Test
    public void shouldSuccessfullyGetProjectDtoWhenValidProjectIdIsGiven() {

        final ProjectEntity expected = fillProjectEntityOfTypeData();
        EasyMock.expect( susDAO.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
        mockControl.replay();
        final WorkflowProjectDTO actual = manager.getWorkflowProjectById( USER_ID, PROJECT_ID );
        Assert.assertEquals( expected.getComposedId().getId(), actual.getId() );
        Assert.assertEquals( expected.getName(), actual.getName() );

    }

    /**
     * Should Not Get ProjectDto When InValid ProjectId Is Given.
     */
    @Test
    public void shouldNotGetProjectDtoWhenInValidProjectIdIsGiven() {

        final ProjectEntity expected = null;
        EasyMock.expect( susDAO.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
        mockControl.replay();
        final WorkflowProjectDTO actual = manager.getWorkflowProjectById( USER_ID, INVALID_UUID );
        Assert.assertEquals( expected, actual );

    }

    /**
     * ********************************** Get All OBJECT ***************************************.
     */

    /**
     * Should successfully get all existing objects when valid parent is passed.
     */
    @Test
    public void shouldSuccessfullyGetAllExistingObjectsWhenValidParentIsPassed() {

        final UUID childId = UUID.randomUUID();

        final WorkflowEntity childEntity = fillWorkFlowEntity();
        childEntity.setComposedId( new VersionPrimaryKey( childId, SusConstantObject.DEFAULT_VERSION_NO ) );
        EasyMock.expect( susDAO.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(),
                EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( Arrays.asList( childEntity ) ).anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSModelObject() ).anyTimes();
        EasyMock.expect( susDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( childEntity ).anyTimes();
        EasyMock.expect( susDAO.getLatestObjectByIdWithLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( childEntity ).anyTimes();
        prepareMockGetDataObjectMethods();
        mockControl.replay();

        final FiltersDTO filtersDTO = fillFilterForDataTable();

        final FilteredResponse< LatestWorkFlowDTO > filteredResponse = manager.getAllWorkflows( USER_ID, PROJECT_ID, filtersDTO );
        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_ONE, filteredResponse.getData().size() );
        Assert.assertEquals( prepareWorkflowDTO( childEntity ), filteredResponse.getData().get( FIRST_INDEX_OF_LIST ) );
    }

    /**
     * Should throw exception when get all objects is called with null filter.
     */
    @Test
    public void shouldThrowExceptionWhenGetAllObjectsIsCalledWithNullFilter() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );

        manager.getAllWorkflows( USER_ID, PROJECT_ID, null );
    }

    /**
     * Should success fully get table columns for workflow dto.
     */
    @Test
    public void shouldSuccessFullyGetTableColumnsForWorkflowDto() {

        final List< TableColumn > expected = prepareWorkflowTableUI();
        final List< TableColumn > actual = manager.getListOfWorkflowsUITableColumns( PROJECT_ID.toString() );
        Assert.assertNotNull( actual );
        for ( final TableColumn actualTableColumn : actual ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }

        }
    }

    /**
     * Should success fully get table columns for filtered workflow dto.
     */
    @Test
    public void shouldSuccessFullyGetTableColumnsForFilteredWorkflowDto() {

        final List< TableColumn > expected = prepareWorkflowTableUI();
        final List< TableColumn > actual = manager.getListOfWorkFlowDTOUITableColumns();
        Assert.assertNotNull( actual );
        for ( final TableColumn actualTableColumn : actual ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }

        }
    }

    /**
     * Should show manage permission object columns when api is called.
     */
    @Test
    public void shouldShowManagePermissionObjectColumnsWhenApiIsCalled() {
        final TableUI expectedUI = getFilledColumns();
        EasyMock.expect( permissionManager.extractColumnList( EasyMock.anyObject() ) ).andReturn( expectedUI.getColumns() ).anyTimes();
        mockControl.replay();
        final List< TableColumn > actualTableUI = manager.workflowPermissionTableUI();
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getData(), actualTableUI.get( FIRST_INDEX ).getData() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getFilter(), actualTableUI.get( FIRST_INDEX ).getFilter() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getName(), actualTableUI.get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getTitle(),
                MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_NAME.getKey() ) );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getRenderer().getData(),
                actualTableUI.get( FIRST_INDEX ).getRenderer().getData() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getRenderer().getManage(),
                actualTableUI.get( FIRST_INDEX ).getRenderer().getManage() );
    }

    /**
     * Mock access control entities.
     */
    private void mockAccessControlEntities() {
        EasyMock.expect( permissionManager.getEntryDAO() ).andReturn( aclEntryDAO ).anyTimes();
        EasyMock.expect(
                        aclEntryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( aclEntryEntities ).anyTimes();
    }

    /**
     * Should successfully persist super user without any exception when valid inputs are given.
     */
    @Test
    public void shouldSuccessfullyPersistSuperUserWithoutAnyExceptionWhenValidInputsAreGiven() {
        final SuSEntity suSEntity = new SystemContainerEntity();
        final SelectionResponseUI selectionResponseUI = new SelectionResponseUI();
        selectionResponseUI.setId( SUPER_USER_ID );
        suSEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO ).anyTimes();
        EasyMock.expect( userCommonDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ) ) )
                .andReturn( new UserEntity() ).anyTimes();
        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( new LocationDTO() ).anyTimes();
        EasyMock.expect( selectionManager.createSelectionForSingleItem( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( selectionResponseUI ).anyTimes();
        mockControl.replay();
        manager.init();
    }

    /**
     * Should successfully save system workflows when valid inputs are given.
     */
    @Test
    public void shouldSuccessfullySaveSystemWorkflowsWhenValidInputsAreGiven() {

        final SelectionResponseUI selectionResponseUI = new SelectionResponseUI();
        selectionResponseUI.setId( SUPER_USER_ID );
        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO ).anyTimes();
        EasyMock.expect( userCommonDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ) ) )
                .andReturn( new UserEntity() ).anyTimes();

        EasyMock.expect( workflowDao.saveWorkflow( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( WorkflowEntity.class ) ) )
                .andReturn( workFlowEntity ).anyTimes();
        EasyMock.expect( userManager.getSimUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( userEntity )
                .anyTimes();
        EasyMock.expect( userManager.prepareUserEntity( EasyMock.anyObject( UserDTO.class ) ) ).andReturn( userEntity ).anyTimes();

        fillSimcoreUser();
        EasyMock.expect( userManagerImpl.getUser( Mockito.anyObject() ) ).andReturn( user ).anyTimes();

        final DataObjectEntity expected = null;
        EasyMock.expect( susDAO.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSModelObject() ).anyTimes();
        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( new LocationDTO() ).anyTimes();
        EasyMock.expect( selectionManager.createSelectionForSingleItem( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( selectionResponseUI ).anyTimes();

        mockControl.replay();
        manager.init();
    }

    /**
     * Fill simcore user.
     */
    private void fillSimcoreUser() {
        user = new UserDTO();
        user.setId( DEFAULT_USER_ID_MINUS_ONE.toString() );
    }

    /**
     * Fill workflow dto.
     */

    private void fillWorkflowDto() {
        workflowDTO = new WorkflowDTO();
        workflowDTO.setId( WORK_FLOW_ID.toString() );
        workflowDTO.setDescription( TEST_DESCRIPTION );
        workflowDTO.setVersion( new VersionDTO() );
        workflowDTO.setName( WORK_FLOW_ENTITY_NAME );
        fillUserDto();
        workflowDTO.setCreatedBy( userDto );
        workflowDTO.setVersion( new VersionDTO( VERSION_ID ) );
    }

    /**
     * Fill user dto.
     */

    private void fillUserDto() {
        userDto = new UserDTO();
        userDto.setId( UUID.randomUUID().toString() );
        userDto.setFirstName( USER_NAME );
    }

    /**
     * Fill work flow entity.
     *
     * @return the workflow entity
     */

    private WorkflowEntity fillWorkFlowEntity() {

        workFlowEntity = new WorkflowEntity();
        fillWorkflowPrimaryKey();
        workFlowEntity.setName( WORK_FLOW_ENTITY_NAME );
        fillUserEntity();
        workFlowEntity.setCreatedBy( userEntity );
        workFlowEntity.setModifiedBy( userEntity );
        workFlowEntity.setIsPrivateWorkflow( false );
        workFlowEntity.setActive( true );
        workFlowEntity.setCreatedOn( new Date() );
        workFlowEntity.setDescription( TEST_DESCRIPTION );
        workFlowEntity.setKeyuser( userEntity );
        workFlowEntity.setVersionId( workFlowEntity.getComposedId().getVersionId() );
        workFlowEntity.setConfig( "asd" );
        workFlowEntity.setTypeId( UUID.randomUUID() );

        return workFlowEntity;
    }

    /**
     * Should return workflow context router menu when valid context is returned by context manager.
     */
    @Test
    public void shouldReturnWorkflowContextRouterMenuWhenValidContextIsReturnedByContextManager() {
        final SelectionResponseUI selectionResponseUI = new SelectionResponseUI();
        selectionResponseUI.setId( SUPER_USER_ID );
        final FiltersDTO expectedSelection = new FiltersDTO();
        expectedSelection.setItems( Arrays.asList( UUID.randomUUID().toString() ) );
        final List< ContextMenuItem > expectedContext = fillExpectedSyncContextMenuItems( STR_UUID_PRENT_ID );

        EasyMock.expect( selectionManager.createSelectionForSingleItem( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject() ) )
                .andReturn( selectionResponseUI ).anyTimes();

        EasyMock.expect( contextMenuManager.getWorkflowContextMenu( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(),
                EasyMock.anyObject() ) ).andReturn( expectedContext ).anyTimes();
        mockControl.replay();
        manager.getWorkflowContextRouter( UUID.fromString( SUPER_USER_ID ), STR_UUID_PRENT_ID, expectedSelection );

    }

    /**
     * Should return bulkworkflow context router menu when valid context is returned by context manager.
     */
    @Test
    public void shouldReturnBulkworkflowContextRouterMenuWhenValidContextIsReturnedByContextManager() {
        final SelectionResponseUI selectionResponseUI = new SelectionResponseUI();
        selectionResponseUI.setId( SUPER_USER_ID );
        final FiltersDTO expectedSelection = new FiltersDTO();
        final List< Object > filterList = new ArrayList<>();
        filterList.add( UUID.randomUUID() );
        filterList.add( UUID.randomUUID() );
        expectedSelection.setItems( filterList );

        final List< ContextMenuItem > expectedContext = fillExpectedSyncContextMenuItems( STR_UUID_PRENT_ID );

        EasyMock.expect( selectionManager.createSelectionForSingleItem( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject() ) )
                .andReturn( selectionResponseUI ).anyTimes();
        EasyMock.expect( selectionManager.createSelection( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject() ) )
                .andReturn( selectionResponseUI ).anyTimes();

        EasyMock.expect( contextMenuManager.getWorkflowContextMenu( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(),
                EasyMock.anyObject() ) ).andReturn( expectedContext ).anyTimes();
        EasyMock.expect( contextMenuManager.getDataContextMenuBulk( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(),
                EasyMock.anyObject(), EasyMock.anyBoolean() ) ).andReturn( expectedContext ).anyTimes();
        mockControl.replay();
        final List< ContextMenuItem > actualContextMenuItems = ( List< ContextMenuItem > ) manager
                .getWorkflowContextRouter( UUID.fromString( SUPER_USER_ID ), STR_UUID_PRENT_ID, expectedSelection );
        Assert.assertFalse( actualContextMenuItems.isEmpty() );
        Assert.assertEquals( expectedContext.size(), actualContextMenuItems.size() );
        Assert.assertEquals( expectedContext, actualContextMenuItems );
    }

    /**
     * Should return false when job is not present stop server workflow.
     */
    @Test
    public void shouldReturnFalseWhenJobIsNotPresentStopServerWorkflow() {
        final UUID id = UUID.randomUUID();
        EasyMock.expect( executionManager.stopJobExecution( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true );
        final boolean actual = manager.stopServerWorkFlow( id.toString(), USER_NAME );
        Assert.assertFalse( actual );
    }

    /**
     * Should successfully get filtered workflow list.
     */
    @Test
    public void shouldSuccessfullyGetFilteredWorkflowList() {

        final UUID childId = UUID.randomUUID();

        final WorkflowEntity childEntity = fillWorkFlowEntity();
        childEntity.setComposedId( new VersionPrimaryKey( childId, SusConstantObject.DEFAULT_VERSION_NO ) );
        EasyMock.expect( susDAO.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(),
                EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( Arrays.asList( childEntity ) ).anyTimes();
        EasyMock.expect( susDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( childEntity ).anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSModelObject() ).anyTimes();
        EasyMock.expect( susDAO.getLatestObjectByIdWithLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( childEntity ).anyTimes();
        EasyMock.expect(
                        permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( true ).anyTimes();

        EasyMock.expect( lifeCycleManager.getLifeCycleConfigurationByFileName( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( fillLifeCycleDTO() ) ).anyTimes();

        mockControl.replay();

        final FiltersDTO filtersDTO = fillFilterForDataTable();

        final FilteredResponse< LatestWorkFlowDTO > filteredResponse = manager.getFilteredWorkflowList( USER_ID, filtersDTO );
        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_ONE, filteredResponse.getData().size() );
        Assert.assertEquals( prepareWorkflowDTO( childEntity ), filteredResponse.getData().get( FIRST_INDEX_OF_LIST ) );
    }

    /**
     * Should throw exception when get filtered workflow list.
     */
    @Test
    public void shouldThrowExceptionWhenGetFilteredWorkflowList() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
        manager.getFilteredWorkflowList( USER_ID, null );
    }

    /**
     * Fill expected sync context menu items.
     *
     * @param parentId
     *         the parent id
     *
     * @return the list
     */
    private List< ContextMenuItem > fillExpectedSyncContextMenuItems( String parentId ) {
        final List< ContextMenuItem > cml = new ArrayList<>();
        cml.add( new ContextMenuItem( SYNC_CONTEXT_URL, null, SYNC_CONTEXT_TITLE ) );
        return cml;
    }

    /**
     * The Constant SYNC_CONTEXT_URL.
     */
    private static final String SYNC_CONTEXT_URL = "Sync Url";

    /**
     * Fill user entity.
     */
    private void fillUserEntity() {
        userEntity = new UserEntity();
        userEntity.setId( USER_ID );
    }

    /**
     * Fill workflow primary key.
     */
    private void fillWorkflowPrimaryKey() {
        workFlowEntity.setComposedId( new VersionPrimaryKey( WORK_FLOW_ID, VERSION_ID ) );
    }

    /**
     * Gets the status config.
     *
     * @return the status config
     */
    private StatusConfigDTO getStatusConfig() {
        final List< String > canMoveTo = new ArrayList<>();
        canMoveTo.add( WIP_LIFECYCLE_STATUS_ID );
        final StatusConfigDTO statusConfigDTO = new StatusConfigDTO();
        statusConfigDTO.setId( WIP_LIFECYCLE_STATUS_ID );
        statusConfigDTO.setName( WIP_LIFECYCLE_STATUS_NAME );
        statusConfigDTO.setUnique( true );
        statusConfigDTO.setMoveOldVersionToStatus( WIP_LIFECYCLE_STATUS_ID );
        statusConfigDTO.setCanMoveToStatus( canMoveTo );
        statusConfigDTO.setVisible( ALL_VISIBILITY );
        return statusConfigDTO;
    }

    /**
     * Prepare new workflow from old DTO.
     *
     * @param entity
     *         the entity
     *
     * @return the new workflow DTO
     */
    private LatestWorkFlowDTO prepareLatestWorkflowFromWorkflowDTO( WorkflowDTO entity ) {

        final LatestWorkFlowDTO newWorkflowDTO = new LatestWorkFlowDTO();

        newWorkflowDTO.setId( UUID.fromString( entity.getId() ) );
        newWorkflowDTO.setName( entity.getName() );
        newWorkflowDTO.setInteractive( false );
        newWorkflowDTO.setVersion( entity.getVersion() );
        newWorkflowDTO.setDescription( entity.getDescription() );
        // get StatusName from config file
        newWorkflowDTO.setActions( entity.getActions() );
        newWorkflowDTO.setCreatedOn( entity.getCreatedOn() );
        newWorkflowDTO.getModifiedOn( entity.getModifiedOn() );
        newWorkflowDTO.setExecutable( entity.isExecutable() );

        if ( entity.getDefinition() != null ) {
            newWorkflowDTO.setWithDefinition( entity.getDefinition() );
        }
        newWorkflowDTO.setCreatedBy( entity.getCreatedBy() );
        newWorkflowDTO.setModifiedBy( entity.getUpdatedBy() );
        newWorkflowDTO.setJobs( entity.getJobs() );

        return newWorkflowDTO;
    }

    /**
     * get the expectation for permission permitted.
     *
     * @return the permitted true
     */
    private void getPermittedTrue() {
        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
    }

    /**
     * Dummy Tabs For Test Expected Case.
     *
     * @return List of Tabs names
     */
    private List< SubTabsUI > getGeneralTabsList() {
        final List< SubTabsUI > subTbs = new ArrayList<>();
        subTbs.add( new SubTabsUI( TAB_NAME_DESIGNER ) );
        subTbs.add( new SubTabsUI( TAB_NAME_VERSIONS ) );
        subTbs.add( new SubTabsUI( TAB_NAME_PERMISSIONS ) );
        return subTbs;
    }

    /**
     * Prepare workflow table UI.
     *
     * @return the list
     */
    private List< TableColumn > prepareWorkflowTableUI() {
        final List< TableColumn > tableColumns = GUIUtils.listColumns( LatestWorkFlowDTO.class );

        return tableColumns;
    }

    /**
     * Gets the filled columns.
     *
     * @return the filled columns
     */
    private TableUI getFilledColumns() {
        final TableUI tableUI = new TableUI();
        tableUI.setColumns( JsonUtils.jsonToList( TABLE_COLUMN_LIST, TableColumn.class ) );
        return tableUI;
    }

    /**
     * Fill object identity.
     *
     * @param classEntity
     *         the class entity
     * @param securityIdentityEntity
     *         the security identity entity
     *
     * @return the object identity entity
     */
    private AclObjectIdentityEntity fillObjectIdentity( AclClassEntity classEntity, AclSecurityIdentityEntity securityIdentityEntity ) {
        objectIdentityEntity = new AclObjectIdentityEntity();
        objectIdentityEntity.setId( OBJECT_IDENTITY_ID );
        objectIdentityEntity.setClassEntity( classEntity );
        objectIdentityEntity.setOwnerSid( securityIdentityEntity );
        objectIdentityEntity.setCreatedOn( new Date() );
        return objectIdentityEntity;
    }

    /**
     * Fill class entity.
     *
     * @return the class entity
     */
    private AclClassEntity fillClassEntity() {
        classEntity = new AclClassEntity();
        classEntity.setId( CLASS_ID );
        classEntity.setName( WORK_FLOW_ENTITY_NAME );
        classEntity.setDescription( ConstantsString.EMPTY_STRING );
        classEntity.setCreatedOn( new Date() );
        return classEntity;
    }

    /**
     * Fill security identity.
     *
     * @param sidId
     *         the sid id
     *
     * @return the security identity entity
     */
    private AclSecurityIdentityEntity fillSecurityIdentity( UUID sidId ) {
        final AclSecurityIdentityEntity securityIdentityEntity = new AclSecurityIdentityEntity();
        securityIdentityEntity.setId( UUID.randomUUID() );
        securityIdentityEntity.setSid( sidId );
        securityIdentityEntity.setPrinciple( Boolean.FALSE );
        return securityIdentityEntity;
    }

    /**
     * Fill entry entity.
     *
     * @param objectIdentityEntity
     *         the object identity entity
     * @param securityIdentityEntity
     *         the security identity entity
     *
     * @return the acl entry entity
     */
    private AclEntryEntity fillEntryEntity( AclObjectIdentityEntity objectIdentityEntity,
            AclSecurityIdentityEntity securityIdentityEntity ) {
        final AclEntryEntity entryEntity = new AclEntryEntity();
        entryEntity.setId( ENTRY_ID );
        entryEntity.setCreatedOn( new Date() );
        entryEntity.setMask( 2 );
        entryEntity.setDelete( Boolean.FALSE );
        if ( objectIdentityEntity != null ) {
            entryEntity.setObjectIdentityEntity( objectIdentityEntity );
        }
        if ( securityIdentityEntity != null ) {
            entryEntity.setSecurityIdentityEntity( securityIdentityEntity );
        }
        return entryEntity;
    }

    /**
     * Fill parent object identity.
     *
     * @param classEntity
     *         the class entity
     * @param securityIdentityEntity
     *         the security identity entity
     *
     * @return the object identity entity
     */
    private AclObjectIdentityEntity fillParentObjectIdentity( AclClassEntity classEntity,
            AclSecurityIdentityEntity securityIdentityEntity ) {
        final AclObjectIdentityEntity objectIdentityEntity = new AclObjectIdentityEntity();
        objectIdentityEntity.setId( PARENT_OBJECT_IDENTITY_ID );
        objectIdentityEntity.setClassEntity( classEntity );
        objectIdentityEntity.setOwnerSid( securityIdentityEntity );
        objectIdentityEntity.setCreatedOn( new Date() );
        objectIdentityEntity.setInherit( false );
        return objectIdentityEntity;
    }

    /**
     * Prepare user DTO.
     *
     * @param name
     *         the name
     * @param restricted
     *         the restricted the restricted
     *
     * @return the user DTO
     */
    private UserDTO prepareUserDTO( String name, String restricted ) {
        final UserDTO userDTO = new UserDTO();
        userDTO.setName( name );
        userDTO.setRestricted( restricted );
        return userDTO;
    }

    /**
     * Prepare restricted user.
     *
     * @return the user DTO
     */
    private UserDTO prepareRestrictedUser() {
        final UserDTO userDTO = new UserDTO();
        userDTO.setFirstName( USER );
        userDTO.setRestricted( RESTRICTED );
        return userDTO;
    }

    /**
     * Check assertions related to object view and permission.
     *
     * @param objectManageDTO
     *         the object manage DTO
     */
    private void checkAssertionsRelatedToObjectViewAndPermission( ManageObjectDTO objectManageDTO ) {
        Assert.assertEquals( PermissionMatrixEnum.VIEW.getValue(), objectManageDTO.getPermissionDTOs().get( VIEW_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.READ.getValue(), objectManageDTO.getPermissionDTOs().get( READ_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.WRITE.getValue(),
                objectManageDTO.getPermissionDTOs().get( WRITE_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.EXECUTE.getValue(),
                objectManageDTO.getPermissionDTOs().get( EXECUTE_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.DELETE.getValue(),
                objectManageDTO.getPermissionDTOs().get( DELETE_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.RESTORE.getValue(),
                objectManageDTO.getPermissionDTOs().get( RESTORE_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(),
                objectManageDTO.getPermissionDTOs().get( CREATE_NEW_OBJECT_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.KILL.getValue(), objectManageDTO.getPermissionDTOs().get( KILL_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.SHARE.getValue(),
                objectManageDTO.getPermissionDTOs().get( SHARE_INDEX ).getMatrexValue() );
    }

    /**
     * A method to populate the project Entity for Expected Result of test.
     *
     * @return projectEntity;
     */
    private ProjectEntity fillProjectEntityOfTypeData() {
        final ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setComposedId( new VersionPrimaryKey( PROJECT_ID, DEFAULT_VERSION_ID ) );
        projectEntity.setName( PROJECT_NAME );
        projectEntity.setDescription( PROJECT_DESCRPTION );
        projectEntity.setTypeId( WORK_FLOW_ID );

        return projectEntity;
    }

    /**
     * Mock permitted allow then denied and provide ACEs.
     */
    private void mockPermittedAllowThenDeniedAndProvideACEs() {
        EasyMock.expect(
                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( true ).andReturn( false ).anyTimes();
        EasyMock.expect( permissionManager.getEntryDAO() ).andReturn( aclEntryDAO ).anyTimes();
        EasyMock.expect(
                        aclEntryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( aclEntryEntities ).anyTimes();
    }

    /**
     * Mock object identity and security identity.
     */
    private void mockObjectIdentityAndSecurityIdentity() {
        EasyMock.expect( permissionManager.getObjectIdentityDAO() ).andReturn( objectIdentityDAO ).anyTimes();
        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( objectIdentityEntity ).andReturn( parentObjectIdentityEntity );

        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
        EasyMock.expect( aclCommonSecurityIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();
    }

    private void prepareMockGetDataObjectMethods() {
        EasyMock.expect( lifeCycleManager.getOwnerVisibleStatusByPolicyId( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( "553536c7-71ec-409d-8f48-ec779a98a68e", "d762f4ef-e706-4a44-a46d-6b334745e2e5",
                        "29d94aa2-62f2-4add-9233-2f4781545c35" ) )
                .anyTimes();
        EasyMock.expect( lifeCycleManager.getAnyVisibleStatusByPolicyId( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( "d762f4ef-e706-4a44-a46d-6b334745e2e5", "29d94aa2-62f2-4add-9233-2f4781545c35" ) ).anyTimes();

        EasyMock.expect(
                        permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( true ).anyTimes();

    }

    /**
     * Prepare filter For table.
     *
     * @return the filters DTO
     */
    private FiltersDTO fillFilterForDataTable() {
        final FiltersDTO filtersDTO = new FiltersDTO( ConstantsInteger.INTEGER_VALUE_ONE, ConstantsInteger.INTEGER_VALUE_ZERO,
                ConstantsInteger.INTEGER_VALUE_TEN );
        final FilterColumn filterColumn = new FilterColumn();

        filterColumn.setName( SuSEntity.FIELD_NAME_MODIFIED_ON );
        filterColumn.setDir( ConstantsString.SORTING_DIRECTION_DESCENDING );
        filtersDTO.setColumns( Arrays.asList( filterColumn ) );
        filtersDTO.setFilteredRecords( ( long ) ConstantsInteger.INTEGER_VALUE_TEN );

        return filtersDTO;
    }

    /**
     * Creates the data object DTO from data object entity.
     *
     * @param workflowEntity
     *         the data object entity
     *
     * @return the data object DTO
     */
    private LatestWorkFlowDTO prepareWorkflowDTO( WorkflowEntity workflowEntity ) {
        LatestWorkFlowDTO workflowDTO = null;

        if ( workflowEntity != null ) {
            workflowDTO = new LatestWorkFlowDTO();
            workflowDTO.setName( workflowEntity.getName() );
            workflowDTO.setId( workflowEntity.getComposedId().getId() );
            workflowDTO.setVersion( new VersionDTO( workflowEntity.getComposedId().getVersionId() ) );
            workflowDTO.setCreatedOn( workflowEntity.getCreatedOn() );
            workflowDTO.getModifiedOn( workflowEntity.getModifiedOn() );
            final UserDTO createdBy = new UserDTO( workflowEntity.getCreatedBy().getId().toString() );
            createdBy.setFirstName( workflowEntity.getCreatedBy().getFirstName() );
            workflowDTO.setCreatedBy( createdBy );
            final UserDTO modifiedBy = new UserDTO( workflowEntity.getModifiedBy().getId().toString() );
            modifiedBy.setFirstName( workflowEntity.getModifiedBy().getFirstName() );
            workflowDTO.setModifiedBy( modifiedBy );
            workflowDTO.setDescription( workflowEntity.getDescription() );

        }

        return workflowDTO;
    }

    /**
     * Gets the filled su S model object.
     *
     * @return the filled su S model object
     */
    private SuSObjectModel getFilledSuSModelObject() {
        final SuSObjectModel model = new SuSObjectModel();
        final List< OVAConfigTab > listOVA = new ArrayList<>();
        final OVAConfigTab ovaConfigTabDesigner = new OVAConfigTab();
        ovaConfigTabDesigner.setTitle( TAB_NAME_DESIGNER );
        ovaConfigTabDesigner.setKey( TAB_KEY_DESIGNER );
        listOVA.add( ovaConfigTabDesigner );
        final OVAConfigTab ovaConfigTabVersions = new OVAConfigTab();
        ovaConfigTabVersions.setTitle( TAB_NAME_VERSIONS );
        listOVA.add( ovaConfigTabVersions );
        final OVAConfigTab ovaConfigTabPermissions = new OVAConfigTab();
        ovaConfigTabPermissions.setTitle( TAB_NAME_PERMISSIONS );
        listOVA.add( ovaConfigTabPermissions );
        model.setViewConfig( listOVA );
        model.setClassName( ProjectDTO.class.getName() );

        return model;

    }

    /**
     * Fill life cycle DTO.
     *
     * @return the life cycle DTO
     */
    private LifeCycleDTO fillLifeCycleDTO() {

        final LifeCycleDTO lifeCycleDTO = new LifeCycleDTO();
        lifeCycleDTO.setId( UUID.randomUUID().toString() );
        return lifeCycleDTO;
    }

}
