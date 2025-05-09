package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.cxf.helpers.IOUtils;
import org.apache.shiro.subject.Subject;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.common.dao.AclCommonSecurityIdentityDAO;
import de.soco.software.simuspace.suscore.data.common.dao.GroupCommonDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SystemContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WorkflowProjectEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.ObjectTreeViewDTO;
import de.soco.software.simuspace.suscore.data.model.RouterConfigItem;
import de.soco.software.simuspace.suscore.data.model.RouterConfigList;
import de.soco.software.simuspace.suscore.data.model.TreeNodeDTO;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.license.manager.impl.LicenseManagerImpl;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.impl.ObjectTypeConfigManagerImpl;
import de.soco.software.simuspace.suscore.lifecycle.model.LifeCycleDTO;
import de.soco.software.simuspace.suscore.object.manager.DataManager;
import de.soco.software.simuspace.suscore.object.model.ProjectType;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;

/**
 * Test Class for SuSObjectManagerImpl with different test cases of all methods related to object and object type.
 *
 * @author Nosheen.Sharif
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { Activator.class } )
public class SuSObjectTreeManagerImplTest {

    /**
     * The Constant FIELD_NAME_MODIFIED_ON.
     */
    public static final String FIELD_NAME_MODIFIED_ON = "modifiedOn";

    /**
     * The Constant MAXIMUM_SHOWABLE_SIZE_OF_CHILD_OBJECTS_IN_CONTAINER.
     */
    private static final int MAXIMUM_SHOWABLE_SIZE_OF_CHILD_OBJECTS_IN_CONTAINER = 10;

    /**
     * The Constant IN_VALID_CLASS_NAME.
     */
    private static final String IN_VALID_CLASS_NAME = "in_valid_class_name";

    /**
     * The Constant CHILD_OBJECT_ID.
     */
    private static final String CHILD_OBJECT_ID = "ff8f6eb0-ffd0-4c2d-8662-ba30cb891759";

    /**
     * The Constant EDIT_PROJECT_TITLE.
     */
    private static final String EDIT_PROJECT_TITLE = "Edit";

    /**
     * The Constant CREATE_PROJECT_TITLE.
     */
    private static final String CREATE_PROJECT_TITLE = "Create";

    /**
     * The Constant UPDATE_DATA_PROJECT_URL.
     */
    private static final String UPDATE_DATA_PROJECT_URL = "update/data/project/";

    /**
     * The Constant CREATE_DATA_PROJECT_URL.
     */
    private static final String CREATE_DATA_PROJECT_URL = "create/data/project/";

    /**
     * The Constant LICENSE_OBJECT_ID.
     */
    private static final String LICENSE_OBJECT_ID = "3008fb63-1880-449a-956e-85c0470f6eac";

    /**
     * The Constant INVALID_PARENT_ID.
     */
    private static final String INVALID_PARENT_ID = "InvalidParentId";

    /**
     * The Constant CONTAINER_OBJECT_NAME.
     */
    private static final String CONTAINER_OBJECT_NAME = "Project";

    /**
     * The Constant CONTAINER_DTO_CLASS_NAME.
     */
    private static final String CONTAINER_DTO_CLASS_NAME = "de.soco.software.simuspace.suscore.object.model.ProjectDTO";

    /**
     * The Constant OBJECT_NAME.
     */
    private static final String OBJECT_NAME = "Dataobject";

    /**
     * The Constant OBJECT_DTO_CLASS_NAME.
     */
    private static final String OBJECT_DTO_CLASS_NAME = "de.soco.software.simuspace.suscore.object.model.DataObjectDTO";

    /**
     * The Constant DUMMY_CONTAINS_ID.
     */
    private static final String DUMMY_CONTAINS_ID = "contains_id";

    /**
     * The Constant MASTER_CONFIG.
     */
    private static final String MASTER_CONFIG = "masterConfig";

    /**
     * The Constant ROUTER_FILE_PATH.
     */
    private static final String ROUTER_FILE_PATH = "src/test/resources/router.json";

    /**
     * The Constant STATIC_METHOD_GET_ROUTERS.
     */
    private static final String STATIC_METHOD_GET_ROUTERS = "getRouters";

    /**
     * The Constant STRING_ENCODING.
     */
    private static final String STRING_ENCODING = "UTF-8";

    /**
     * The Constant LIFECYCLE_STATUS.
     */
    private static final String LIFECYCLE_STATUS = "553536c7-71ec-409d-8f48-ec779a98a68e";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant DESCRIPTION_FIELD.
     */
    private static final String GROUP_DESCRIPTION = "Description";

    /**
     * The Constant ACTIVE.
     */
    public static final String GROUP_STATUS = "Active";

    /**
     * The Constant OBJECT_VIEW_NAME.
     */
    public static final String OBJECT_VIEW_NAME = "testView";

    /**
     * The Constant OBJECT_VIEW_KEY.
     */
    public static final String OBJECT_VIEW_KEY = "object-tree-view";

    /**
     * The Constant OBJECT_TREE_VIEW_JSON.
     */
    public static final String OBJECT_TREE_VIEW_JSON = "[\n  {\n    \"id\": \"ff8f6eb0-ffd0-4c2d-8662-ba30cb891759\",\n    \"title\": \"Data\",\n    \"folder\": true,\n    \"lazy\": true,\n    \"icon\": \"fa fa-briefcase font-red\",\n    \"description\": null,\n    \"url\": null,\n    \"children\": null,\n    \"state\": 1\n  }\n]";

    /**
     * The Constant OBJECT_VIEW_TYPE.
     */
    public static final String OBJECT_VIEW_TYPE = "object-tree";

    /**
     * The Constant INVALID_OBJECT_VIEW_TYPE.
     */
    public static final String INVALID_OBJECT_VIEW_TYPE = "testViewType";

    /**
     * The Constant DEFAULT_VIEW.
     */
    public static final boolean DEFAULT_VIEW = false;

    /**
     * The Constant OBJECT_VIEW_CREATED_BY_USER_ID.
     */
    public static final UUID OBJECT_VIEW_CREATED_BY_USER_ID = UUID.randomUUID();

    /**
     * The Constant VIEW_ID.
     */
    public static final UUID VIEW_ID = UUID.randomUUID();

    /**
     * Dummy GROUP_ID for test Cases.
     */
    private static final UUID GROUP_ID = UUID.randomUUID();

    /**
     * The Constant NAME_FIELD.
     */
    private static final String GROUP_NAME = "Name";

    /**
     * The Constant OBJECT_TYPE_ID.
     */
    private static final UUID OBJECT_TYPE_ID = UUID.randomUUID();

    /**
     * The Constant DEFAULT_USER_ID.
     */
    public static final String DEFAULT_USER_ID = "1e98a77c-a0be-4983-9137-d9a8acd0ea8b";

    /**
     * The Constant TEST_SEARCH_NODE.
     */
    private static final String TEST_NODE_NAME = "test";

    /**
     * The Constant JOb_NAME.
     */
    private static final String JOb_NAME = "JOB TEst";

    /**
     * The Constant USER_ID.
     */
    private static final String USER_ID = UUID.randomUUID().toString();

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * SuSObjectManagerImpl reference.
     */
    private SuSObjectTreeManagerImpl manager;

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * SusDao reference.
     */
    private SuSGenericObjectDAO< SuSEntity > dao;

    /**
     * The config manager.
     */
    private ObjectTypeConfigManagerImpl configManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The permission manager
     */
    private PermissionManager permissionManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The user common DAO.
     */
    private UserCommonDAO userCommonDAO;

    /**
     * The group common DAO.
     */
    private GroupCommonDAO groupCommonDAO;

    /**
     * The subject for current session from apache shiro
     */
    private Subject subject;

    /**
     * The data manager.
     */
    private DataManager dataManager;

    /**
     * The acl common security identity DAO.
     */
    private AclCommonSecurityIdentityDAO aclCommonSecurityIdentityDAO;

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * setUp which is called before entering in each test case.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setUp() throws Exception {
        mockControl().resetToNice();
        manager = new SuSObjectTreeManagerImpl();
        dao = mockControl().createMock( SuSGenericObjectDAO.class );
        manager.setSusDAO( dao );
        configManager = mockControl().createMock( ObjectTypeConfigManagerImpl.class );
        licenseManager = mockControl().createMock( LicenseManagerImpl.class );
        manager.setConfigManager( configManager );
        dataManager = mockControl().createMock( DataManager.class );
        permissionManager = mockControl().createMock( PermissionManager.class );
        userCommonManager = mockControl().createMock( UserCommonManager.class );
        aclCommonSecurityIdentityDAO = mockControl.createMock( AclCommonSecurityIdentityDAO.class );
        groupCommonDAO = mockControl().createMock( GroupCommonDAO.class );
        userCommonDAO = mockControl().createMock( UserCommonDAO.class );
        objectViewManager = mockControl().createMock( ObjectViewManager.class );
        subject = Mockito.mock( Subject.class );
        lifeCycleManager = mockControl().createMock( LifeCycleManager.class );
        manager.setLifeCycleManager( lifeCycleManager );
        manager.setPermissionManager( permissionManager );
        manager.setObjectViewManager( objectViewManager );
        manager.setUserCommonManager( userCommonManager );
    }

    /**
     * should Get Model When Create Object Argument Class Is Project.
     */
    @Test
    public void shouldGetRootItems() {
        List< TreeNodeDTO > expected = getExpectedTree();

        UUID childId = UUID.fromString( CHILD_OBJECT_ID );
        UUID parentId = UUID.randomUUID();

        SuSEntity childEntity = new SystemContainerEntity();
        childEntity.setComposedId( new VersionPrimaryKey( childId, SusConstantObject.DEFAULT_VERSION_NO ) );
        childEntity.setName( SimuspaceFeaturesEnum.DATA.getKey() );
        SuSEntity parentEntity = new SystemContainerEntity();
        parentEntity.setComposedId( new VersionPrimaryKey( parentId, SusConstantObject.DEFAULT_VERSION_NO ) );

        EasyMock.expect( lifeCycleManager.getLifeCycleConfigurationByFileName( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( fillLifeCycleDTO() ) ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), ContainerEntity.class, childId ) )
                .andReturn( childEntity ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), ContainerEntity.class, parentId ) )
                .andReturn( parentEntity ).anyTimes();
        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( Arrays.asList() );
        EasyMock.expect( dao.getAllFilteredRecordsWithParent( EasyMock.anyObject( EntityManager.class ), ContainerEntity.class,
                        fillFilterForMaxNumberOfChildren(), parentEntity.getComposedId().getId() ) ).andReturn( Arrays.asList( childEntity ) )
                .anyTimes();
        EasyMock.expect( dao.getAllFilteredRecordsWithParent( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
                fillFilterForMaxNumberOfChildren(), childEntity.getComposedId().getId() ) ).andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect( dao.getAllRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( Arrays.asList( childEntity ) ).anyTimes();
        EasyMock.expect( dao.getCountWithParentId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( ( long ) MAXIMUM_SHOWABLE_SIZE_OF_CHILD_OBJECTS_IN_CONTAINER ).anyTimes();

        mockControl.replay();
        List< TreeNodeDTO > actual = manager.getTree( null, DEFAULT_USER_ID, parentId, null, null );

        for ( TreeNodeDTO child : actual ) {
            Assert.assertEquals( child.getId(), expected.get( FIRST_INDEX ).getId() );
        }

    }

    /**
     * Should get only ten child object when more then ten child objects exists in parent container.
     */
    @Test
    public void shouldGetOnlyTenChildObjectWhenMoreThenTenChildObjectsExistsInParentContainer() {

        UUID childId = UUID.fromString( CHILD_OBJECT_ID );
        UUID parentId = UUID.randomUUID();

        SuSEntity childEntity = new VariantEntity();
        childEntity.setComposedId( new VersionPrimaryKey( childId, SusConstantObject.DEFAULT_VERSION_NO ) );
        childEntity.setTypeId( UUID.randomUUID() );
        childEntity.setName( SimuspaceFeaturesEnum.DATA.getKey() );
        SuSEntity parentEntity = new SystemContainerEntity();
        parentEntity.setComposedId( new VersionPrimaryKey( parentId, SusConstantObject.DEFAULT_VERSION_NO ) );

        SuSObjectModel suSObjectModel = new SuSObjectModel();
        suSObjectModel.setLifeCycle( LIFECYCLE_STATUS );
        EasyMock.expect( lifeCycleManager.getLifeCycleConfigurationByFileName( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( fillLifeCycleDTO() ) ).anyTimes();
        EasyMock.expect( licenseManager.isFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), ContainerEntity.class, childId ) )
                .andReturn( childEntity ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), ContainerEntity.class, parentId ) )
                .andReturn( parentEntity ).anyTimes();

        EasyMock.expect(
                dao.getAllFilteredRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject(),
                        EasyMock.anyObject() ) ).andReturn( getMaximumShowAbleNumberOfChildsInAContainer( childEntity ) ).anyTimes();
        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( Arrays.asList( parentEntity ) ).anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( suSObjectModel ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByIdWithLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyObject( List.class ), EasyMock.anyObject( List.class ) ) )
                .andReturn( new SuSEntity() {
                } ).anyTimes();

        EasyMock.expect( dao.getAllRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( Arrays.asList( childEntity ) ).anyTimes();
        EasyMock.expect( dao.getCountWithParentId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( ( long ) MAXIMUM_SHOWABLE_SIZE_OF_CHILD_OBJECTS_IN_CONTAINER ).anyTimes();
        mockControl.replay();
        List< TreeNodeDTO > actual = manager.getTree( null, DEFAULT_USER_ID, parentId, null, null );
        Assert.assertEquals( MAXIMUM_SHOWABLE_SIZE_OF_CHILD_OBJECTS_IN_CONTAINER, actual.size() );
    }

    /**
     * Should return ten permitted child objects in tree when more then ten objects exists.
     */
    @Test
    public void shouldReturnTenPermittedChildObjectsInTreeWhenMoreThenTenObjectsExists() {
        SuSEntity suSEntity = new SystemContainerEntity();
        UUID childId = UUID.fromString( SimuspaceFeaturesEnum.RIGHTS.getId() );
        UUID parentId = UUID.randomUUID();

        SuSEntity childEntity = new VariantEntity();
        childEntity.setComposedId( new VersionPrimaryKey( childId, SusConstantObject.DEFAULT_VERSION_NO ) );
        childEntity.setName( SimuspaceFeaturesEnum.RIGHTS.getKey() );
        childEntity.setTypeId( UUID.randomUUID() );

        SuSEntity parentEntity = new SystemContainerEntity();
        parentEntity.setComposedId( new VersionPrimaryKey( parentId, SusConstantObject.DEFAULT_VERSION_NO ) );
        parentEntity.setName( SimuspaceFeaturesEnum.SYSTEM.getKey() );

        SuSObjectModel suSObjectModel = new SuSObjectModel();
        suSObjectModel.setLifeCycle( LIFECYCLE_STATUS );

        List< SuSEntity > childEntitiesInCurrentParent = getMaximumShowAbleNumberOfChildsInAContainer( childEntity );
        EasyMock.expect( lifeCycleManager.getLifeCycleConfigurationByFileName( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( fillLifeCycleDTO() ) ).anyTimes();
        EasyMock.expect( licenseManager.isFeatureAllowedToUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), ContainerEntity.class, childId ) )
                .andReturn( childEntity ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), ContainerEntity.class, parentId ) )
                .andReturn( parentEntity ).anyTimes();

        EasyMock.expect( dao.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyObject(), EasyMock.anyObject(),
                EasyMock.anyObject() ) ).andReturn( childEntitiesInCurrentParent ).anyTimes();

        EasyMock.expect(
                dao.getAllFilteredRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject(),
                        EasyMock.anyObject() ) ).andReturn( getMaximumShowAbleNumberOfChildsInAContainer( childEntity ) ).anyTimes();

//        EasyMock.expect( dataManager.getDataObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( new DataObjectDTO() ).anyTimes();

        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( suSObjectModel ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByIdWithLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyObject( List.class ), EasyMock.anyObject( List.class ) ) )
                .andReturn( new SuSEntity() {
                } ).anyTimes();

        EasyMock.expect( lifeCycleManager.getOwnerVisibleStatusByPolicyId( EasyMock.anyString() ) ).andReturn( new ArrayList<>() )
                .anyTimes();
        EasyMock.expect( lifeCycleManager.getAnyVisibleStatusByPolicyId( EasyMock.anyString() ) ).andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect( dao.getAllRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( Arrays.asList( childEntity ) ).anyTimes();

        EasyMock.expect( dao.getCountWithParentId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( ( long ) MAXIMUM_SHOWABLE_SIZE_OF_CHILD_OBJECTS_IN_CONTAINER ).anyTimes();

        /* EasyMock.expect( dataManager.isVisible( EasyMock.anyString(), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( true )
                .anyTimes();*/
        EasyMock.expect( dao.getAllFilteredRecordsWithParent( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
                fillFilterForMaxNumberOfChildren(), suSEntity.getComposedId().getId() ) ).andReturn( new ArrayList<>() ).anyTimes();
        mockControl.replay();
        List< TreeNodeDTO > actual = manager.getTree( null, DEFAULT_USER_ID, parentId,
                prepareObjectTreeViewDTO( VIEW_ID, OBJECT_VIEW_NAME, OBJECT_VIEW_KEY, OBJECT_TREE_VIEW_JSON, null, OBJECT_VIEW_TYPE,
                        DEFAULT_VIEW ), null );
        Assert.assertEquals( MAXIMUM_SHOWABLE_SIZE_OF_CHILD_OBJECTS_IN_CONTAINER, actual.size() );
    }

    /**
     * Should successfully execute persist system nodes without any exception.
     */
    @Test
    public void shouldSuccessfullyExecutePersistSystemNodesWithoutAnyException() {
        SuSEntity suSEntity = new SystemContainerEntity();
        suSEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( suSEntity ).anyTimes();
        EasyMock.expect( dao.getAllFilteredRecordsWithParent( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
                fillFilterForMaxNumberOfChildren(), suSEntity.getComposedId().getId() ) ).andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect( groupCommonDAO.readUserGroupByName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( null ).anyTimes();
        EasyMock.expect( userCommonManager.getGroupCommonDAO() ).andReturn( groupCommonDAO ).anyTimes();
        EasyMock.expect( userCommonManager.createUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( SuSUserGroupDTO.class ) ) ).andReturn( fillUserGrupDto() ).anyTimes();
        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new UserDTO() ).anyTimes();
        mockControl.replay();
        manager.bootloading();
    }

    /**
     * Should successfully execute persist system nodes and persist system features and persist super user without any exception.
     */
    @Test
    public void shouldSuccessfullyExecutePersistSystemNodesAndPersistSystemFeaturesAndPersistSuperUserWithoutAnyException() {
        SuSEntity suSEntity = new SystemContainerEntity();
        suSEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( suSEntity ).anyTimes();
        EasyMock.expect( dao.getAllFilteredRecordsWithParent( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
                fillFilterForMaxNumberOfChildren(), suSEntity.getComposedId().getId() ) ).andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect( groupCommonDAO.readUserGroupByName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( null ).anyTimes();
        EasyMock.expect( userCommonManager.getGroupCommonDAO() ).andReturn( groupCommonDAO ).anyTimes();
        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO ).anyTimes();
        EasyMock.expect( userCommonManager.createUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( SuSUserGroupDTO.class ) ) ).andReturn( fillUserGrupDto() ).anyTimes();
        EasyMock.expect( userCommonDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserEntity.class ) ) )
                .andReturn( new UserEntity() ).anyTimes();
        mockControl.replay();
        manager.bootloading();
    }

    /**
     * @return
     */
    private SuSUserGroupDTO fillUserGrupDto() {
        SuSUserGroupDTO susGroupDto = new SuSUserGroupDTO();
        susGroupDto.setId( GROUP_ID );
        susGroupDto.setName( GROUP_NAME );
        susGroupDto.setDescription( GROUP_DESCRIPTION );
        susGroupDto.setStatus( GROUP_STATUS );
        return susGroupDto;
    }

    /**
     * Gets the maximum show able number of children in a container.
     *
     * @param childEntity
     *         the child entity
     *
     * @return the maximum show able number of children in a container
     */
    private List< SuSEntity > getMaximumShowAbleNumberOfChildsInAContainer( SuSEntity childEntity ) {

        List< SuSEntity > suSEntities = new ArrayList<>();
        for ( int i = 0; i < MAXIMUM_SHOWABLE_SIZE_OF_CHILD_OBJECTS_IN_CONTAINER; i++ ) {
            suSEntities.add( childEntity );
        }

        return suSEntities;
    }

    /**
     * Gets the expected tree.
     *
     * @return the expected tree
     */
    private List< TreeNodeDTO > getExpectedTree() {
        TreeNodeDTO t = new TreeNodeDTO();
        t.setId( CHILD_OBJECT_ID );
        return Arrays.asList( t );
    }

    /**
     * Should return context menu item by of A project of type data and contains object types.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnContextMenuItemByOfAProjectOfTypeDataAndContainsObjectTypes() throws Exception {
        mockActivatorStaticFunctions();
        ProjectEntity suSEntity = fillProjectEntityOfTypeData();

        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( suSEntity ).anyTimes();

        SuSObjectModel projectObjectType = fillSuSModelObject();
        SuSObjectModel containterObjectType = fillContatinsOfTypeObject();
        projectObjectType.setContains( Arrays.asList( containterObjectType.getId() ) );
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( suSEntity ) );

        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( OBJECT_TYPE_ID.toString(), MASTER_CONFIG ) )
                .andReturn( projectObjectType );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( containterObjectType.getId(), MASTER_CONFIG ) )
                .andReturn( containterObjectType ).anyTimes();
        EasyMock.expect(
                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( true ).anyTimes();

        mockControl.replay();
        List< ContextMenuItem > actualContextMenuItems = manager.findMenu( EasyMock.anyObject( EntityManager.class ), USER_ID, "",
                suSEntity.getComposedId().getId().toString() );
        List< ContextMenuItem > expectedContext = fillExpectedMenuOfProjectOfTypeDataContainingObjects(
                suSEntity.getComposedId().getId().toString() );
        Assert.assertFalse( actualContextMenuItems.isEmpty() );
        Assert.assertEquals( expectedContext.size(), actualContextMenuItems.size() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getIcon(), actualContextMenuItems.get( FIRST_INDEX ).getIcon() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getTitle(), actualContextMenuItems.get( FIRST_INDEX ).getTitle() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getUrl(), actualContextMenuItems.get( FIRST_INDEX ).getUrl() );
    }

    /**
     * Should return context menu item by of A workflow project of type data and contains object types.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnContextMenuItemByOfAWorkflowProjectOfTypeDataAndContainsObjectTypes() throws Exception {
        mockActivatorStaticFunctions();
        ProjectEntity suSEntity = fillProjectEntityOfTypeData();
        WorkflowProjectEntity workflowProjectEntity = fillWorkflowProjectEntityOfTypeData();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( workflowProjectEntity ).anyTimes();

        SuSObjectModel projectObjectType = fillSuSModelObject();
        SuSObjectModel containterObjectType = fillContatinsOfTypeObject();
        projectObjectType.setContains( Arrays.asList( containterObjectType.getId() ) );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( OBJECT_TYPE_ID.toString(), MASTER_CONFIG ) )
                .andReturn( projectObjectType );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( containterObjectType.getId(), MASTER_CONFIG ) )
                .andReturn( containterObjectType ).anyTimes();
        EasyMock.expect(
                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( true ).anyTimes();
        mockControl.replay();

        List< ContextMenuItem > actualContextMenuItems = manager.findMenu( EasyMock.anyObject( EntityManager.class ), USER_ID, "",
                suSEntity.getComposedId().getId().toString() );
        Assert.assertFalse( actualContextMenuItems.isEmpty() );

    }

    /**
     * Should return context menu item of job entity.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnContextMenuItemOfJobEntity() throws Exception {
        mockActivatorStaticFunctions();
        // JobEntity suSEntity = fillJobEntityForContext();
        // EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
        // EasyMock.anyObject( UUID.class ) ) ).andReturn( suSEntity ).anyTimes();
        ContextMenuItem expectedContext = fillExpectedMenuOfJob( OBJECT_TYPE_ID.toString() );
        SuSObjectModel projectObjectType = fillSuSModelObject();
        SuSObjectModel containterObjectType = fillContatinsOfTypeObject();
        projectObjectType.setContains( Arrays.asList( containterObjectType.getId() ) );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( OBJECT_TYPE_ID.toString(), MASTER_CONFIG ) )
                .andReturn( projectObjectType );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( containterObjectType.getId(), MASTER_CONFIG ) )
                .andReturn( containterObjectType ).anyTimes();

        mockControl.replay();

        // List< ContextMenuItem > actualContextMenuItems = manager.findMenu( USER_ID, "", suSEntity.getComposedId().getId().toString() );
        // Assert.assertFalse( actualContextMenuItems.isEmpty() );
        // Assert.assertEquals( expectedContext.getTitle(), actualContextMenuItems.get( FIRST_INDEX ).getTitle() );
        // Assert.assertEquals( expectedContext.getIcon(), actualContextMenuItems.get( FIRST_INDEX ).getIcon() );

    }

    /**
     * Should return context menu item by of A project of type data contains container types.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnContextMenuItemByOfAProjectOfTypeDataContainsContainerTypes() throws Exception {
        mockActivatorStaticFunctions();
        ProjectEntity suSEntity = fillProjectEntityOfTypeData();

        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( suSEntity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( suSEntity ) );
        SuSObjectModel projectObjectType = fillSuSModelObject();
        SuSObjectModel containterObjectType = fillContainsOfTypeContainer();
        projectObjectType.setContains( Arrays.asList( containterObjectType.getId() ) );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( OBJECT_TYPE_ID.toString(), MASTER_CONFIG ) )
                .andReturn( projectObjectType );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( containterObjectType.getId(), MASTER_CONFIG ) )
                .andReturn( containterObjectType ).anyTimes();
        EasyMock.expect(
                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( true ).anyTimes();

        mockControl.replay();

        List< ContextMenuItem > actualContextMenuItems = manager.findMenu( EasyMock.anyObject( EntityManager.class ), USER_ID, "",
                suSEntity.getComposedId().getId().toString() );
        List< ContextMenuItem > expectedContext = fillExpectedMenuOfProjectOfTypeDataContainingObjects(
                suSEntity.getComposedId().getId().toString() );
        Assert.assertFalse( actualContextMenuItems.isEmpty() );
        Assert.assertEquals( expectedContext.size(), actualContextMenuItems.size() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getIcon(), actualContextMenuItems.get( FIRST_INDEX ).getIcon() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getTitle(), actualContextMenuItems.get( FIRST_INDEX ).getTitle() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getUrl(), actualContextMenuItems.get( FIRST_INDEX ).getUrl() );
    }

    /**
     * Should return context menu item by of object of type data contains container types.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnContextMenuItemByOfObjectOfTypeDataContainsContainerTypes() throws Exception {
        mockActivatorStaticFunctions();
        ProjectEntity suSEntity = fillProjectEntityOfTypeLabel();

        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( suSEntity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( suSEntity ) );
        SuSObjectModel projectObjectType = fillSuSModelObject();
        SuSObjectModel containterObjectType = fillContatinsOfTypeObject();
        projectObjectType.setContains( Arrays.asList( containterObjectType.getId() ) );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( OBJECT_TYPE_ID.toString(), MASTER_CONFIG ) )
                .andReturn( projectObjectType );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( containterObjectType.getId(), MASTER_CONFIG ) )
                .andReturn( containterObjectType ).anyTimes();
        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        mockControl.replay();

        List< ContextMenuItem > actualContextMenuItems = manager.findMenu( USER_ID, "", suSEntity.getComposedId().getId().toString() );
        List< ContextMenuItem > expectedContext = fillExpectedMenuOfProjectOfTypeDataContainingObjects(
                suSEntity.getComposedId().getId().toString() );
        Assert.assertFalse( actualContextMenuItems.isEmpty() );
        Assert.assertEquals( expectedContext.size(), actualContextMenuItems.size() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getIcon(), actualContextMenuItems.get( FIRST_INDEX ).getIcon() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getTitle(), actualContextMenuItems.get( FIRST_INDEX ).getTitle() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getUrl(), actualContextMenuItems.get( FIRST_INDEX ).getUrl() );
    }

    /**
     * Should return context menu item by of A project of type label contains container types.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnContextMenuItemByOfAProjectOfTypeLabelContainsContainerTypes() throws Exception {
        mockActivatorStaticFunctions();
        ProjectEntity suSEntity = fillProjectEntityOfTypeLabel();

        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( suSEntity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( suSEntity ) );
        SuSObjectModel projectObjectType = fillSuSModelObject();
        SuSObjectModel containterObjectType = fillContainsOfTypeContainer();
        projectObjectType.setContains( Arrays.asList( containterObjectType.getId() ) );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( OBJECT_TYPE_ID.toString(), MASTER_CONFIG ) )
                .andReturn( projectObjectType );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( containterObjectType.getId(), MASTER_CONFIG ) )
                .andReturn( containterObjectType ).anyTimes();
        EasyMock.expect(
                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( true ).anyTimes();
        mockControl.replay();

        List< ContextMenuItem > actualContextMenuItems = manager.findMenu( EasyMock.anyObject( EntityManager.class ), USER_ID, "",
                suSEntity.getComposedId().getId().toString() );
        List< ContextMenuItem > expectedContext = fillExpectedMenuOfProjectOfTypeDataContainingObjects(
                suSEntity.getComposedId().getId().toString() );
        Assert.assertFalse( actualContextMenuItems.isEmpty() );
        Assert.assertEquals( expectedContext.size(), actualContextMenuItems.size() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getIcon(), actualContextMenuItems.get( FIRST_INDEX ).getIcon() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getTitle(), actualContextMenuItems.get( FIRST_INDEX ).getTitle() );
        Assert.assertEquals( expectedContext.get( FIRST_INDEX ).getUrl(), actualContextMenuItems.get( FIRST_INDEX ).getUrl() );
    }

    /**
     * Should return empty context menu item list when parent id not exists.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnEmptyContextMenuItemListWhenParentIdNotExists() throws Exception {
        mockActivatorStaticFunctions();
        thrown.expect( IllegalArgumentException.class );
        List< ContextMenuItem > actualContextMenuItems = manager.findMenu( USER_ID, "", INVALID_PARENT_ID );
        Assert.assertTrue( actualContextMenuItems.isEmpty() );
    }

    /**
     * Should successfully get all object tree views for user when valid user id is given.
     */
    @Test
    public void shouldSuccessfullyGetAllObjectTreeViewsForUserWhenValidUserIdIsGiven() {
        List< ObjectViewDTO > expected = new ArrayList<>();
        ProjectEntity susEntity = fillProjectEntityOfTypeData();
        expected.add( prepareObjectTreeViewDTO( VIEW_ID, OBJECT_VIEW_NAME, OBJECT_VIEW_KEY, OBJECT_TREE_VIEW_JSON, null, OBJECT_VIEW_TYPE,
                DEFAULT_VIEW ) );
        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( susEntity ).anyTimes();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( expected ).anyTimes();
        mockControl.replay();
        List< ObjectTreeViewDTO > actual = manager.getAllObjectTreeViews( OBJECT_VIEW_CREATED_BY_USER_ID.toString(), null );
        Iterator< ObjectTreeViewDTO > actualIterator = actual.iterator();
        Iterator< ObjectViewDTO > expectedIterator = expected.iterator();
        while ( actualIterator.hasNext() && expectedIterator.hasNext() ) {
            ObjectTreeViewDTO actualObject = actualIterator.next();
            ObjectViewDTO expectedObject = expectedIterator.next();
            Assert.assertEquals( actualObject.getId(), expectedObject.getId().toString() );
            Assert.assertEquals( actualObject.getName(), expectedObject.getName() );
        }
    }

    /**
     * Should not successfully get object tree views for user when in valid user id is given.
     */
    @Test
    public void shouldNotSuccessfullyGetObjectTreeViewsForUserWhenInValidUserIdIsGiven() {
        List< ObjectViewDTO > expected = new ArrayList<>();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        List< ObjectTreeViewDTO > actual = manager.getAllObjectTreeViews( OBJECT_VIEW_CREATED_BY_USER_ID.toString(), null );
        Assert.assertEquals( expected.size(), actual.size() );
    }

    /**
     * Should successfully save object tree view when valid input is given.
     */
    @Test
    public void shouldSuccessfullySaveObjectTreeViewWhenValidInputIsGiven() {
        ObjectTreeViewDTO expected = prepareObjectTreeViewDTO( VIEW_ID, OBJECT_VIEW_NAME, OBJECT_VIEW_KEY, OBJECT_TREE_VIEW_JSON, null,
                OBJECT_VIEW_TYPE, DEFAULT_VIEW );
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        ObjectTreeViewDTO actual = manager.saveObjectTreeView( expected, OBJECT_VIEW_CREATED_BY_USER_ID.toString(), null );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should successfully update object tree view when valid input is given.
     */
    @Test
    public void shouldSuccessfullyUpdateObjectTreeViewWhenValidInputIsGiven() {
        ObjectTreeViewDTO expected = prepareObjectTreeViewDTO( VIEW_ID, OBJECT_VIEW_NAME, OBJECT_VIEW_KEY, OBJECT_TREE_VIEW_JSON, null,
                OBJECT_VIEW_TYPE, DEFAULT_VIEW );
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        EasyMock.expect( objectViewManager.getObjectViewById( EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
        mockControl.replay();
        ObjectTreeViewDTO actual = manager.updateObjectTreeView( expected, OBJECT_VIEW_CREATED_BY_USER_ID.toString() );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should successfully filter root items based on view states for object tree views.
     */
    @Test
    public void shouldSuccessfullyFilterRootItemsBasedOnViewStatesForObjectTreeViews() {
        List< TreeNodeDTO > expected = getTreeWithChildren();
        ObjectTreeViewDTO treeRequestDTO = new ObjectTreeViewDTO();
        treeRequestDTO.setView_nodes( expected );
        UUID childId = UUID.fromString( CHILD_OBJECT_ID );
        UUID parentId = UUID.randomUUID();

        SuSEntity childEntity = new SystemContainerEntity();
        childEntity.setComposedId( new VersionPrimaryKey( childId, SusConstantObject.DEFAULT_VERSION_NO ) );
        childEntity.setName( SimuspaceFeaturesEnum.DATA.getKey() );
        SuSEntity parentEntity = new SystemContainerEntity();
        parentEntity.setComposedId( new VersionPrimaryKey( parentId, SusConstantObject.DEFAULT_VERSION_NO ) );
        EasyMock.expect( lifeCycleManager.getLifeCycleConfigurationByFileName( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( fillLifeCycleDTO() ) ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), ContainerEntity.class, childId ) )
                .andReturn( childEntity ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), ContainerEntity.class, parentId ) )
                .andReturn( parentEntity ).anyTimes();

        EasyMock.expect( dao.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyObject(), EasyMock.anyObject(),
                EasyMock.anyObject() ) ).andReturn( getMaximumShowAbleNumberOfChildsInAContainer( childEntity ) ).anyTimes();

        EasyMock.expect(
                dao.getAllFilteredRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject(),
                        EasyMock.anyObject() ) ).andReturn( getMaximumShowAbleNumberOfChildsInAContainer( childEntity ) ).anyTimes();
        EasyMock.expect( dao.getCountWithParentId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( ( long ) MAXIMUM_SHOWABLE_SIZE_OF_CHILD_OBJECTS_IN_CONTAINER ).anyTimes();

        EasyMock.expect( dao.getAllRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( Arrays.asList( childEntity ) ).anyTimes();
        mockControl.replay();
        List< TreeNodeDTO > actual = manager.getTree( null, DEFAULT_USER_ID, parentId, treeRequestDTO, null );

        Iterator< TreeNodeDTO > actualIterator = actual.iterator();
        Iterator< TreeNodeDTO > expectedIterator = expected.iterator();
        while ( actualIterator.hasNext() && expectedIterator.hasNext() ) {
            TreeNodeDTO actualObject = actualIterator.next();
            TreeNodeDTO expectedObject = expectedIterator.next();
            Assert.assertEquals( expectedObject.getId().toString(), actualObject.getId() );
            Assert.assertEquals( expectedObject.getTitle(), actualObject.getTitle() );
            Assert.assertEquals( expectedObject.getUrl(), actualObject.getUrl() );
            Assert.assertEquals( expectedObject.getElement(), actualObject.getElement() );
        }
    }

    /**
     * Should filter tree items when valid search is passed.
     */
    @Test
    public void shouldFilterTreeItemsWhenValidSearchIsPassed() {

        ObjectTreeViewDTO treeRequestDTO = new ObjectTreeViewDTO();
        treeRequestDTO.setSearch( TEST_NODE_NAME );
        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
        SuSEntity parentEntity = new SystemContainerEntity();
        UUID parentId = UUID.randomUUID();
        parentEntity.setComposedId( new VersionPrimaryKey( parentId, SusConstantObject.DEFAULT_VERSION_NO ) );
        SuSObjectModel suSObjectModel = new SuSObjectModel();
        suSObjectModel.setLifeCycle( LIFECYCLE_STATUS );
        EasyMock.expect( dao.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( parentEntity ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();

        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( parentEntity ) );

        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( suSObjectModel ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByIdWithLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyObject( List.class ), EasyMock.anyObject( List.class ) ) )
                .andReturn( new SuSEntity() {
                } ).anyTimes();
        EasyMock.expect( lifeCycleManager.getOwnerVisibleStatusByPolicyId( EasyMock.anyString() ) ).andReturn( new ArrayList<>() )
                .anyTimes();
        EasyMock.expect( lifeCycleManager.getAnyVisibleStatusByPolicyId( EasyMock.anyString() ) ).andReturn( new ArrayList<>() ).anyTimes();

        mockControl.replay();
        List< TreeNodeDTO > actual = manager.getTree( null, DEFAULT_USER_ID, null, treeRequestDTO, null );
        Assert.assertTrue( !actual.isEmpty() );
    }

    /**
     * Should not filter tree items and return empty tree when in valid search is passed.
     */
    @Test
    public void shouldNotFilterTreeItemsAndReturnEmptyTreeWhenInValidSearchIsPassed() {

        ObjectTreeViewDTO treeRequestDTO = new ObjectTreeViewDTO();
        treeRequestDTO.setSearch( INVALID_PARENT_ID );
        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
        SuSEntity parentEntity = new SystemContainerEntity();
        UUID parentId = UUID.randomUUID();
        parentEntity.setComposedId( new VersionPrimaryKey( parentId, SusConstantObject.DEFAULT_VERSION_NO ) );
        EasyMock.expect( dao.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( projectEntity ).anyTimes();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();

        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( parentEntity ) );
        mockControl.replay();
        List< TreeNodeDTO > actual = manager.getTree( null, DEFAULT_USER_ID, null, treeRequestDTO, null );
        Assert.assertTrue( actual.isEmpty() );
    }

    /**
     * Should return context menu item by prent id.
     *
     * @throws Exception
     *         the exception
     */
    public void shouldReturnContextMenuItemByPrentIdWhenValidParentIdIsProvided() throws Exception {
        mockActivatorStaticFunctions();
        String parentId = LICENSE_OBJECT_ID;
        List< ContextMenuItem > actualContextMenuItems = manager.findMenu( USER_ID, "", parentId );
        List< ContextMenuItem > expectedContext = getExpectedContextMenu( parentId );
        Assert.assertFalse( actualContextMenuItems.isEmpty() );
        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_TWO, actualContextMenuItems.size() );
        Assert.assertEquals( expectedContext, actualContextMenuItems );
    }

    /**
     * Gets the expected context menu items list.
     *
     * @param parentId
     *         the parent id
     *
     * @return the expected context menu
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private List< ContextMenuItem > getExpectedContextMenu( String parentId ) throws IOException {

        List< ContextMenuItem > contextMenuItems = new ArrayList<>();

        InputStream targetStream = new FileInputStream( new File( ROUTER_FILE_PATH ) );
        String routerText = IOUtils.toString( targetStream, STRING_ENCODING );
        RouterConfigList licenseRouterConfig = JsonUtils.jsonToObject( routerText, RouterConfigList.class );

        for ( RouterConfigItem i : licenseRouterConfig.getRoutes() ) {

            if ( i.getContainer() != null && i.getContainer().equalsIgnoreCase( parentId ) && i.getType()
                    .equalsIgnoreCase( RouterConfigItem.TREE_CONTEXT ) ) {

                ContextMenuItem contextMenuItem = new ContextMenuItem();
                contextMenuItem.setUrl( i.getUrl() );
                contextMenuItem.setTitle( MessageBundleFactory.getMessage( i.getTitle() ) );

                contextMenuItems.add( contextMenuItem );
            }
        }
        return contextMenuItems;
    }

    /**
     * Fill expected menu of project of type data containing objects.
     *
     * @param parentId
     *         the parent id
     *
     * @return the list
     */
    private List< ContextMenuItem > fillExpectedMenuOfProjectOfTypeDataContainingObjects( String parentId ) {
        List< ContextMenuItem > cml = new ArrayList<>();
        cml.add( new ContextMenuItem( CREATE_DATA_PROJECT_URL + parentId, null, CREATE_PROJECT_TITLE ) );
        cml.add( new ContextMenuItem( UPDATE_DATA_PROJECT_URL + parentId, null, EDIT_PROJECT_TITLE ) );
        cml.add( new ContextMenuItem(
                SuSObjectTreeManagerImpl.DELETE_OBJECT_CONTEXT_URL.replace( SuSObjectTreeManagerImpl.OBJECT_ID_PARAM, "" + parentId ), null,
                SuSObjectTreeManagerImpl.DELETE_OBJECT_TITLE ) );
        cml.add( new ContextMenuItem(
                SuSObjectTreeManagerImpl.PERMISSION_PROJECT_CONTEXT_URL.replace( SuSObjectTreeManagerImpl.PROJECT_ID_PARAM, "" + parentId ),
                null, MessageBundleFactory.getMessage( SuSObjectTreeManagerImpl.PERMISSIONS_NODE_TITLE ) ) );
        cml.add( new ContextMenuItem(
                SuSObjectTreeManagerImpl.CHANGE_STATUS_OBJECT_CONTEXT_URL.replace( SuSObjectTreeManagerImpl.OBJECT_ID_PARAM,
                        "" + parentId ), null, SuSObjectTreeManagerImpl.CHANGE_STATUS_TITLE ) );
        cml.add( new ContextMenuItem(
                SuSObjectTreeManagerImpl.ADD_META_DATA_TO_OBJECT_CONTEXT_URL.replace( SuSObjectTreeManagerImpl.OBJECT_ID_PARAM,
                        "" + parentId ), null, MessageBundleFactory.getMessage( SuSObjectTreeManagerImpl.ADD_META_DATA_TITLE ) ) );
        cml.add( new ContextMenuItem(
                SuSObjectTreeManagerImpl.CREAT_STRUCTURE_CONTEXT_URL.replace( SuSObjectTreeManagerImpl.PROJECT_ID_PARAM, "" + parentId ),
                null, MessageBundleFactory.getMessage( SuSObjectTreeManagerImpl.CREAT_STRUCTURE_TITLLE ) ) );

        cml.add( new ContextMenuItem(
                SuSObjectTreeManagerImpl.CHANGE_STATUS_OBJECT_CONTEXT_URL.replace( SuSObjectTreeManagerImpl.OBJECT_ID_PARAM,
                        "" + parentId ), null, SuSObjectTreeManagerImpl.CHANGE_STATUS_TITLE ) );

        cml.add( new ContextMenuItem(
                SuSObjectTreeManagerImpl.EXPORT_PROJECT_CONTEXT_URL.replace( SuSObjectTreeManagerImpl.OBJECT_ID_PARAM, "" + parentId ),
                null, MessageBundleFactory.getMessage( SuSObjectTreeManagerImpl.EXPORT_TITLE ) ) );
        return cml;
    }

    /**
     * @param parentId
     *
     * @return
     */
    private ContextMenuItem fillExpectedMenuOfJob( String parentId ) {
        return new ContextMenuItem(
                SuSObjectTreeManagerImpl.PERMISSION_PROJECT_CONTEXT_URL.replace( SuSObjectTreeManagerImpl.OBJECT_ID_PARAM, "" + parentId ),
                null, MessageBundleFactory.getMessage( SuSObjectTreeManagerImpl.PERMISSIONS_NODE_TITLE ) );
    }

    /**
     * Mock activator static functions.
     *
     * @throws Exception
     *         the exception
     */
    private static void mockActivatorStaticFunctions() throws Exception {

        PowerMockito.spy( Activator.class );

        InputStream targetStream = new FileInputStream( new File( ROUTER_FILE_PATH ) );
        String routerText = IOUtils.toString( targetStream, STRING_ENCODING );
        RouterConfigList licenseRouterConfig = JsonUtils.jsonToObject( routerText, RouterConfigList.class );

        Map< String, List< RouterConfigItem > > routers = new HashMap<>();
        routers.put( licenseRouterConfig.getName(), licenseRouterConfig.getRoutes() );

        PowerMockito.doReturn( routers ).when( Activator.class, STATIC_METHOD_GET_ROUTERS );
    }

    /**
     * Prepare SuSModel For Expected Test.
     *
     * @return the su S object model
     */
    private SuSObjectModel fillSuSModelObject() {
        SuSObjectModel model = new SuSObjectModel();
        model.setContains( Arrays.asList( DUMMY_CONTAINS_ID ) );

        return model;

    }

    /**
     * Fill contatins of type object.
     *
     * @return the su S object model
     */
    private SuSObjectModel fillContatinsOfTypeObject() {
        SuSObjectModel model = new SuSObjectModel();
        model.setId( DUMMY_CONTAINS_ID );
        model.setClassName( OBJECT_DTO_CLASS_NAME );
        model.setName( OBJECT_NAME );

        return model;
    }

    /**
     * Fill contains of type container.
     *
     * @return the sus object model
     */
    private SuSObjectModel fillContainsOfTypeContainer() {
        SuSObjectModel model = new SuSObjectModel();
        model.setId( DUMMY_CONTAINS_ID );
        model.setClassName( CONTAINER_DTO_CLASS_NAME );
        model.setName( CONTAINER_OBJECT_NAME );

        return model;
    }

    /**
     * Fill contatins of type container.
     *
     * @return the SuS object model
     */
    private SuSObjectModel fillContainsOfTypeWithInvalidClass() {
        SuSObjectModel model = new SuSObjectModel();
        model.setId( DUMMY_CONTAINS_ID );
        model.setClassName( IN_VALID_CLASS_NAME );
        model.setName( CONTAINER_OBJECT_NAME );

        return model;
    }

    /**
     * Prepare filter for max ten number of children.
     *
     * @return the filters DTO
     */
    private FiltersDTO fillFilterForMaxNumberOfChildren() {
        FiltersDTO filtersDTO = new FiltersDTO( ConstantsInteger.INTEGER_VALUE_ONE, ConstantsInteger.INTEGER_VALUE_ZERO,
                MAXIMUM_SHOWABLE_SIZE_OF_CHILD_OBJECTS_IN_CONTAINER );
        FilterColumn filterColumn = new FilterColumn();

        filterColumn.setName( FIELD_NAME_MODIFIED_ON );
        filterColumn.setDir( ConstantsString.SORTING_DIRECTION_DESCENDING );
        filtersDTO.setColumns( Arrays.asList( filterColumn ) );

        return filtersDTO;
    }

    /***
     * Prepare object tree view DTO.
     *
     * @param viewId
     *            the view id
     * @param objectViewName
     *            the object view name
     * @param objectViewKey
     *            the object view key
     * @param objectViewJson
     *            the object view json
     * @param objectViewCreatedBy
     *            the object view created by
     * @param viewType
     *            the view type
     * @param defaultView
     *            the default view
     * @return the tree view DTO
     */
    public ObjectTreeViewDTO prepareObjectTreeViewDTO( UUID viewId, String objectViewName, String objectViewKey, String objectViewJson,
            UserDTO objectViewCreatedBy, String viewType, boolean defaultView ) {
        ObjectTreeViewDTO treeViewDTO = new ObjectTreeViewDTO();
        treeViewDTO.setId( viewId.toString() );
        treeViewDTO.setName( objectViewName );
        treeViewDTO.setObjectViewKey( objectViewKey );
        treeViewDTO.setObjectViewJson( objectViewJson );
        treeViewDTO.setObjectViewType( viewType );
        treeViewDTO.setDefaultView( defaultView );
        treeViewDTO.setView_nodes( getExpectedTree() );
        treeViewDTO.setSortDirection( ConstantsString.SORTING_DIRECTION_ASCENDING );
        treeViewDTO.setSortParameter( OBJECT_VIEW_NAME );
        return treeViewDTO;
    }

    /**
     * Gets the tree with children.
     *
     * @return the tree with children
     */
    private List< TreeNodeDTO > getTreeWithChildren() {
        TreeNodeDTO t = new TreeNodeDTO();
        t.setId( CHILD_OBJECT_ID );
        t.setState( ConstantsInteger.INTEGER_VALUE_ONE );
        t.setChildren( Arrays.asList( t ) );
        t.setTitle( SimuspaceFeaturesEnum.DATA.getKey() );
        return Arrays.asList( t );
    }

    /**
     * Fill data project entity.
     *
     * @return the project entity
     */
    private ProjectEntity fillProjectEntityOfTypeData() {
        ProjectEntity susEntity = new ProjectEntity();
        susEntity.setConfig( MASTER_CONFIG );
        susEntity.setTypeId( OBJECT_TYPE_ID );
        susEntity.setType( ProjectType.DATA.getKey() );
        susEntity.setComposedId( new VersionPrimaryKey( UUID.fromString( LICENSE_OBJECT_ID ), SusConstantObject.DEFAULT_VERSION_NO ) );
        susEntity.setName( SimuspaceFeaturesEnum.DATA.getKey() );
        return susEntity;
    }

    /**
     * Fill data project entity.
     *
     * @return the project entity
     */
    // private JobEntity fillJobEntityForContext() {
    // JobEntity susEntity = new JobEntity();
    // susEntity.setName( JOb_NAME );
    // susEntity.setConfig( MASTER_CONFIG );
    // susEntity.setComposedId( new VersionPrimaryKey( UUID.fromString( LICENSE_OBJECT_ID ), SusConstantObject.DEFAULT_VERSION_NO ) );
    // return susEntity;
    // }

    /**
     * Added some test cases Fill workflow project entity of type data.
     *
     * @return the workflow project entity
     */
    private WorkflowProjectEntity fillWorkflowProjectEntityOfTypeData() {
        WorkflowProjectEntity susEntity = new WorkflowProjectEntity();
        susEntity.setConfig( MASTER_CONFIG );
        susEntity.setTypeId( OBJECT_TYPE_ID );
        susEntity.setType( ProjectType.DATA.getKey() );
        susEntity.setComposedId( new VersionPrimaryKey( UUID.fromString( LICENSE_OBJECT_ID ), SusConstantObject.DEFAULT_VERSION_NO ) );
        return susEntity;
    }

    /**
     * Fill project entity of type label.
     *
     * @return the project entity
     */
    private ProjectEntity fillProjectEntityOfTypeLabel() {
        ProjectEntity susEntity = new ProjectEntity();
        susEntity.setConfig( MASTER_CONFIG );
        susEntity.setTypeId( OBJECT_TYPE_ID );
        susEntity.setType( ProjectType.LABEL.getKey() );
        susEntity.setComposedId( new VersionPrimaryKey( UUID.fromString( LICENSE_OBJECT_ID ), SusConstantObject.DEFAULT_VERSION_NO ) );
        return susEntity;
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
