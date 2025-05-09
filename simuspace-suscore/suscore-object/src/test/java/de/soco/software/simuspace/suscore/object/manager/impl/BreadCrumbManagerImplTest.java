package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbDTO;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.LibraryEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SystemContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.object.manager.SuSObjectTreeManager;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * Test Cases For BreadCrumbManagerImpl
 *
 * @author Zain ul Hassan
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { ConfigFilePathReader.class } )
public class BreadCrumbManagerImplTest {

    /** The Constant FIRST_INDEX_OF_LIST. */

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * BreadCrumbManagerImpl reference.
     */
    private BreadCrumbManagerImpl breadCrumbManager;

    /**
     * SusDao reference
     */
    private SuSGenericObjectDAO< SuSEntity > dao;

    /**
     * SuSObjectTreeManager reference.
     */
    private SuSObjectTreeManager treeManager;

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Dummy Error Message to test
     */
    private static final String OBJECT_ID_CANNOT_NULL = "Object Id cannot be null or empty";

    /**
     * The Constant OBJECT_ID.
     */
    private static final UUID OBJECT_ID = UUID.randomUUID();

    /**
     * The Constant USER_ID.
     */
    private static final String USER_ID = UUID.randomUUID().toString();

    /**
     * The Constant CREATE.
     */
    private static final String CREATE = "4100018x4";

    /**
     * The Constant MANAGE.
     */
    private static final String MANAGE = "4100006x4";

    /**
     * The Constant CONTEXT.
     */
    private static final String CONTEXT = "/context";

    /**
     * The Constant OBJECT_TREE_URL.
     */
    private static final String OBJECT_TREE_URL = "/object-tree/";

    /**
     * The Constant PROJECT_ENTITY_URL_VIEW_DATA_PROJECT.
     */
    private static final String ENTITY_URL_VIEW_DATA = "view/data/project/";

    /**
     * The Constant VIEW_JOB.
     */
    private static final String VIEW_JOB = "view/job";

    /**
     * The Constant DATA_OBJECT_ENTITY_URL_VIEW_DATA_OBJECT.
     */
    private static final String DATA_OBJECT_ENTITY_URL_VIEW_DATA_OBJECT = "view/data/object/";

    /**
     * The Constant SYSTEM_AUDIT_LOG.
     */
    private static final String SYSTEM_AUDIT_LOG = "system/auditlog";

    /**
     * The Constant SYSTEM_GROUPS.
     */
    private static final String SYSTEM_GROUPS = "system/permissions/groups";

    /**
     * The Constant SYSTEM_PERMISSIONS_ROLES.
     */
    private static final String SYSTEM_PERMISSIONS_ROLES = "system/permissions/roles";

    /**
     * The Constant SYSTEM_USER_DIRECTORIES.
     */
    private static final String SYSTEM_USER_DIRECTORIES = "system/user-directories";

    /**
     * The Constant SYSTEM_USERS.
     */
    private static final String SYSTEM_USERS = "system/users";

    /**
     * The Constant SYSTEM_LICENSE.
     */
    private static final String SYSTEM_LICENSE = "system/license";

    /**
     * The Constant PROJECT_NAME.
     */
    private static final String PROJECT_NAME = "Project-1";

    /**
     * The Constant JOB_NAME.
     */
    private static final String JOB_NAME = "Job-1";

    /**
     * The Constant OBJECT_NAME.
     */
    private static final String OBJECT_NAME = "Object-1";

    /**
     * The Constant VARIANT_NAME.
     */
    private static final String VARIANT_NAME = "Variant-1";

    /**
     * The Constant LIBRARY_NAME.
     */
    private static final String LIBRARY_NAME = "Library-1";

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
     */
    @Before
    public void setUp() throws Exception {
        mockControl().resetToNice();
        breadCrumbManager = new BreadCrumbManagerImpl();
        dao = mockControl().createMock( SuSGenericObjectDAO.class );
        treeManager = mockControl().createMock( SuSObjectTreeManager.class );
        breadCrumbManager.setSusDAO( dao );
        breadCrumbManager.setTreeManager( treeManager );
    }

    /************ BREAD CRUMBS ************/
    /**
     * Should Throw Exception In Get Data Bread Crumbs With Null ObjectId
     */
    @Test
    public void shouldThrowExceptionInCreateDataBreadCrumbsWhenNullObjectId() {
        thrown.expect( SusException.class );
        thrown.expectMessage( OBJECT_ID_CANNOT_NULL );
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( null ).anyTimes();
        mockControl.replay();
        breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, null, null );

    }

    /**
     * Should Successfully Get Data Bread Crumb With Valid Project Entity
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidProjectEntityIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbProjectEntityDto();
        SuSEntity projectEntity = fillSuSEntityWithProjectEntity();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( projectEntity ).anyTimes();
        EasyMock.expect(
                        dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject() ) )
                .andReturn( Arrays.asList( projectEntity ) ).once();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {

            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
            }
        }
    }

    /**
     * Should Successfully Get Data Bread Crumb With Valid Job Entity
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidJobEntityIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbJobEntityDTO();
        // SuSEntity jobEntity = fillSuSEntityWithJobEntity();
        // EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
        // .andReturn( jobEntity ).anyTimes();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {

            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                if ( actualItems.getName().equals( expectedItems.getName() ) ) {
                    Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                    Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                    Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
                }
            }
            break;
        }
    }

    /**
     * Should Successfully Get Data Bread Crumb With Valid Object Entity
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidObjectEntityIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbObjectEntityDto();
        SuSEntity susObjectEntity = fillSuSEntityWithDataObjectEntity();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( susObjectEntity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( susObjectEntity ) ).once();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {
            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
            }
        }
    }

    /**
     * Should Successfully Get Data Bread Crumb With Valid Library Entity
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidLibraryEntityIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbLibraryEntityDto();
        SuSEntity susObjectEntity = fillSuSEntityWithDataLibraryEntity();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( susObjectEntity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( susObjectEntity ) ).once();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {
            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
            }
        }
    }

    /**
     * Should Successfully Get Data Bread Crumb With Valid Variant Entity
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidVariantEntityIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbVariantEntityDto();
        SuSEntity susObjectEntity = fillSuSEntityWithDataVariantEntity();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( susObjectEntity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( susObjectEntity ) ).once();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {
            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
            }
        }
    }

    /**
     * Should Successfully Get Data Bread Crumb With Valid Audit Log
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidAuditLogIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbAuditLog();
        SuSEntity entity = fillSuSEntityWithAuditLog();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( entity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( entity ) ).once();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {
            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
            }
        }
    }

    /**
     * Should Successfully Get Data Bread Crumb With Valid Groups
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidGroupIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbGroups();
        SuSEntity entity = fillSuSEntityWithGroups();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( entity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( entity ) ).once();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {
            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
            }
        }
    }

    /**
     * Should Successfully Get Bread Crumb When Create Group Data Is Provided
     */
    @Test
    public void shouldSuccessfullyGetBreadCrumbsWhenCreateGroupDataIsProvided() {
        BreadCrumbDTO expected = createBreadCrumbGroups();
        SuSEntity entity = fillSuSEntityWithGroups();
        ContextMenuItem contextMenuItem = fillCreateGroupContextMenuItem();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( entity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( entity ) ).once();
        EasyMock.expect( treeManager.findMenu( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString(),
                EasyMock.anyObject() ) ).andReturn( Arrays.asList( contextMenuItem ) ).once();
        mockControl.replay();

        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                ConstantsObjectServiceEndPoints.CREATE_SYSTEM_USER_GROUP );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
            Assert.assertEquals( expectedItems.getName(), actual.getBreadCrumbItems().get( 1 ).getName() );
            Assert.assertEquals( expectedItems.getUrl(), actual.getBreadCrumbItems().get( 1 ).getUrl() );
        }
    }

    /**
     * Should Successfully Get Data Bread Crumb With Role
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidRoleIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbRole();
        SuSEntity entity = fillSuSEntityWithRole();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( entity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( entity ) ).once();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {
            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
            }
        }
    }

    /**
     * Should Successfully Get Data Bread Crumb With Valid Directories
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidDirectoriesIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbDirectories();
        SuSEntity entity = fillSuSEntityWithDirectories();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( entity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( entity ) ).once();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {
            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
            }
        }
    }

    /**
     * Should Successfully Get Data Bread Crumb With Valid Users
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidUsersIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbUsers();
        SuSEntity entity = fillSuSEntityWithUsers();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( entity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( entity ) ).once();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {
            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
            }
        }
    }

    /**
     * Should Successfully Get Data Bread Crumb With Valid License
     */
    @Test
    public void shouldSuccessfullyGetDataBreadCrumbsWhenTheValidLicenseIsProvided() {
        BreadCrumbDTO expected = getBreadCrumbLicense();
        SuSEntity entity = fillSuSEntityWithLicense();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( entity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( entity ) ).once();
        mockControl.replay();
        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                null );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO actualItems : actual.getBreadCrumbItems() ) {
            for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
                Assert.assertEquals( expectedItems.getName(), actualItems.getName() );
                Assert.assertEquals( expectedItems.getUrl(), actualItems.getUrl() );
                Assert.assertEquals( expectedItems.getContext(), actualItems.getContext() );
            }
        }
    }

    /**
     * Should Successfully Get Bread Crumb When Create License Data Is Provided
     */
    @Test
    public void shouldSuccessfullyGetBreadCrumbWhenCreateLicenseDataIsProvided() {
        BreadCrumbDTO expected = createBreadCrumbLicense();
        SuSEntity entity = fillSuSEntityWithLicense();
        ContextMenuItem contextMenuItem = fillCreateLicenseContextMenuItem();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( entity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( entity ) ).once();
        EasyMock.expect( treeManager.findMenu( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString(),
                EasyMock.anyObject() ) ).andReturn( Arrays.asList( contextMenuItem ) ).once();
        mockControl.replay();

        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                ConstantsObjectServiceEndPoints.CREATE_SYSTEM_LICENSE );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
            Assert.assertEquals( expectedItems.getName(), actual.getBreadCrumbItems().get( 1 ).getName() );
            Assert.assertEquals( expectedItems.getUrl(), actual.getBreadCrumbItems().get( 1 ).getUrl() );
        }
    }

    /**
     * Should Successfully Get Bread Crumb When Manage License Data Is Provided
     */
    @Test
    public void shouldSuccessfullyGetBreadCrumbWhenManageLicenseDataIsProvided() {
        BreadCrumbDTO expected = manageBreadCrumbLicense();
        SuSEntity entity = fillSuSEntityWithLicense();
        ContextMenuItem contextMenuItem = fillManageLicenseContextMenuItem();
        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, OBJECT_ID ) )
                .andReturn( entity ).anyTimes();
        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
                .andReturn( Arrays.asList( entity ) ).once();
        EasyMock.expect( treeManager.findMenu( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString(),
                EasyMock.anyObject() ) ).andReturn( Arrays.asList( contextMenuItem ) ).once();
        mockControl.replay();

        BreadCrumbDTO actual = breadCrumbManager.createBreadCrumb( EasyMock.anyObject( EntityManager.class ), USER_ID, OBJECT_ID.toString(),
                ConstantsObjectServiceEndPoints.MANAGE_SYSTEM_LICENSE );
        Assert.assertNotNull( actual );
        for ( BreadCrumbItemDTO expectedItems : expected.getBreadCrumbItems() ) {
            Assert.assertEquals( expectedItems.getName(), actual.getBreadCrumbItems().get( 1 ).getName() );
            Assert.assertEquals( expectedItems.getUrl(), actual.getBreadCrumbItems().get( 1 ).getUrl() );
        }
    }

    /**
     * ************************** HELPER METHODS **************************. /** Get BreadCrumb Project Entity Dto.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO getBreadCrumbProjectEntityDto() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbProjectItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Gets the bread crumb job entity dto.
     *
     * @return the bread crumb job entity dto
     */
    private BreadCrumbDTO getBreadCrumbJobEntityDto() {
        BreadCrumbDTO breadCrumbDto = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbJobItem() );
        breadCrumbDto.setBreadCrumbItems( items );
        return breadCrumbDto;
    }

    /**
     * Gets the bread crumb job entity DTO.
     *
     * @return the bread crumb job entity DTO
     */
    private BreadCrumbDTO getBreadCrumbJobEntityDTO() {
        BreadCrumbDTO breadCrumbDto = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbJobItem() );
        items.add( setBreadCrumbJobItem2() );
        breadCrumbDto.setBreadCrumbItems( items );
        return breadCrumbDto;
    }

    /**
     * Set BreadCrumb Project Item.
     *
     * @return BreadCrumbItemDTO
     */
    private BreadCrumbItemDTO setBreadCrumbProjectItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( PROJECT_NAME );
        SuSEntity susEntity = new ProjectEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( OBJECT_ID );
        bcItems.setUrl( ENTITY_URL_VIEW_DATA + susEntity.getComposedId().getId() );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Set BreadCrumb Project Item.
     *
     * @return BreadCrumbItemDTO
     */
    private BreadCrumbItemDTO setBreadCrumbJobItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( JOB_NAME );
        SuSEntity susEntity = new ProjectEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( OBJECT_ID );
        bcItems.setUrl( VIEW_JOB + "/" + susEntity.getComposedId().getId() );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Sets the bread crumb job item 2.
     *
     * @return the bread crumb item DTO
     */
    private BreadCrumbItemDTO setBreadCrumbJobItem2() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( SimuspaceFeaturesEnum.JOBS.getKey() );
        SuSEntity susEntity = new ProjectEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( OBJECT_ID );
        bcItems.setUrl( VIEW_JOB );
        bcItems.setContext( OBJECT_TREE_URL + SimuspaceFeaturesEnum.JOBS.getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Fill SuSEntity With Project Entity
     *
     * @return SuSEntity
     */
    private SuSEntity fillSuSEntityWithProjectEntity() {

        SuSEntity projectEntity = new ProjectEntity();
        projectEntity.setComposedId( new VersionPrimaryKey() );
        projectEntity.getComposedId().setId( OBJECT_ID );
        projectEntity.setName( PROJECT_NAME );
        return projectEntity;
    }

    /**
     * Fill SuSEntity With Project Entity
     *
     * @return SuSEntity
     */
    // private SuSEntity fillSuSEntityWithJobEntity() {
    //
    // SuSEntity jobEntity = new JobEntity();
    // jobEntity.setComposedId( new VersionPrimaryKey() );
    // jobEntity.getComposedId().setId( OBJECT_ID );
    // jobEntity.setName( JOB_NAME );
    // return jobEntity;
    // }

    /**
     * Get BreadCrumb Object Entity Dto.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO getBreadCrumbObjectEntityDto() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbObjectItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Set BreadCrumb Object Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO setBreadCrumbObjectItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( OBJECT_NAME );
        SuSEntity susEntity = new ProjectEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( OBJECT_ID );
        bcItems.setUrl( DATA_OBJECT_ENTITY_URL_VIEW_DATA_OBJECT + susEntity.getComposedId().getId() );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Fill SuSEntity With Data Object Entity
     *
     * @return SuSEntity
     */
    private SuSEntity fillSuSEntityWithDataObjectEntity() {

        SuSEntity dataObjectEntity = new DataObjectEntity();
        dataObjectEntity.setComposedId( new VersionPrimaryKey() );
        dataObjectEntity.getComposedId().setId( OBJECT_ID );
        dataObjectEntity.setName( OBJECT_NAME );
        return dataObjectEntity;
    }

    /**
     * Get BreadCrumb Library Entity Dto.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO getBreadCrumbLibraryEntityDto() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbLibraryItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Set BreadCrumb Library Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO setBreadCrumbLibraryItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( LIBRARY_NAME );
        SuSEntity susEntity = new LibraryEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( OBJECT_ID );
        bcItems.setUrl( ENTITY_URL_VIEW_DATA + susEntity.getComposedId().getId() );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Fill SuSEntity With Data Library Entity
     *
     * @return SuSEntity
     */
    private SuSEntity fillSuSEntityWithDataLibraryEntity() {

        SuSEntity libraryEntity = new LibraryEntity();
        libraryEntity.setComposedId( new VersionPrimaryKey() );
        libraryEntity.getComposedId().setId( OBJECT_ID );
        libraryEntity.setName( LIBRARY_NAME );
        return libraryEntity;
    }

    /**
     * Get BreadCrumb Variant Entity Dto.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO getBreadCrumbVariantEntityDto() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbVariantItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Set BreadCrumb Variant Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO setBreadCrumbVariantItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( VARIANT_NAME );
        SuSEntity susEntity = new VariantEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( OBJECT_ID );
        bcItems.setUrl( ENTITY_URL_VIEW_DATA + susEntity.getComposedId().getId() );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Fill SuSEntity With Data Variant Entity
     *
     * @return SuSEntity
     */
    private SuSEntity fillSuSEntityWithDataVariantEntity() {

        SuSEntity variantEntity = new VariantEntity();
        variantEntity.setComposedId( new VersionPrimaryKey() );
        variantEntity.getComposedId().setId( OBJECT_ID );
        variantEntity.setName( VARIANT_NAME );
        return variantEntity;
    }

    /**
     * Get BreadCrumb Audit Log.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO getBreadCrumbAuditLog() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbAuditLogItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Set BreadCrumb Audit Log Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO setBreadCrumbAuditLogItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( SimuspaceFeaturesEnum.AUDIT.getKey() );
        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.AUDIT.getId() ) );
        bcItems.setUrl( SYSTEM_AUDIT_LOG );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Fill SuSEntity With System Container Entity For Audit Log
     *
     * @return SuSEntity
     */
    private SuSEntity fillSuSEntityWithAuditLog() {

        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.AUDIT.getId() ) );
        susEntity.setName( SimuspaceFeaturesEnum.AUDIT.getKey() );
        return susEntity;
    }

    /**
     * Get BreadCrumb Groups.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO getBreadCrumbGroups() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbGroupsItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Set BreadCrumb Groups Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO setBreadCrumbGroupsItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( SimuspaceFeaturesEnum.GROUPS.getKey() );
        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.GROUPS.getId() ) );
        bcItems.setUrl( SYSTEM_GROUPS );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Fill SuSEntity With System Container Entity For Groups
     *
     * @return SuSEntity
     */
    private SuSEntity fillSuSEntityWithGroups() {

        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.GROUPS.getId() ) );
        susEntity.setName( SimuspaceFeaturesEnum.GROUPS.getKey() );
        return susEntity;
    }

    /**
     * Create BreadCrumb Groups.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO createBreadCrumbGroups() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( createBreadCrumbGroupsItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Create BreadCrumb Groups Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO createBreadCrumbGroupsItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( MessageBundleFactory.getMessage( CREATE ) );
        bcItems.setUrl( ConstantsObjectServiceEndPoints.CREATE_SYSTEM_USER_GROUP );
        return bcItems;
    }

    /**
     * Fill Create ContextMenuItem For Groups
     *
     * @return ContextMenuItem
     */
    private ContextMenuItem fillCreateGroupContextMenuItem() {

        ContextMenuItem contextMenuItem = new ContextMenuItem();
        contextMenuItem.setTitle( MessageBundleFactory.getMessage( CREATE ) );
        contextMenuItem.setUrl( ConstantsObjectServiceEndPoints.CREATE_SYSTEM_USER_GROUP );
        return contextMenuItem;
    }

    /**
     * Get BreadCrumb Role.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO getBreadCrumbRole() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbRoleItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Set BreadCrumb Role Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO setBreadCrumbRoleItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( SimuspaceFeaturesEnum.ROLES.getKey() );
        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.ROLES.getId() ) );
        bcItems.setUrl( SYSTEM_PERMISSIONS_ROLES );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Fill SuSEntity With System Container Entity For Role
     *
     * @return SuSEntity
     */
    private SuSEntity fillSuSEntityWithRole() {

        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.ROLES.getId() ) );
        susEntity.setName( SimuspaceFeaturesEnum.ROLES.getKey() );
        return susEntity;
    }

    /**
     * Get BreadCrumb Directories.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO getBreadCrumbDirectories() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbDirectoriesItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Set BreadCrumb Directories Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO setBreadCrumbDirectoriesItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( SimuspaceFeaturesEnum.DIRECTORIES.getKey() );
        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.DIRECTORIES.getId() ) );
        bcItems.setUrl( SYSTEM_USER_DIRECTORIES );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Fill SuSEntity With System Container Entity For Directories
     *
     * @return SuSEntity
     */
    private SuSEntity fillSuSEntityWithDirectories() {

        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.DIRECTORIES.getId() ) );
        susEntity.setName( SimuspaceFeaturesEnum.DIRECTORIES.getKey() );
        return susEntity;
    }

    /**
     * Get BreadCrumb Users.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO getBreadCrumbUsers() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbUsersItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Set BreadCrumb Users Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO setBreadCrumbUsersItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( SimuspaceFeaturesEnum.USERS.getKey() );
        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.USERS.getId() ) );
        bcItems.setUrl( SYSTEM_USERS );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Fill SuSEntity With System Container Entity For Users
     *
     * @return SuSEntity
     */
    private SuSEntity fillSuSEntityWithUsers() {

        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.USERS.getId() ) );
        susEntity.setName( SimuspaceFeaturesEnum.USERS.getKey() );
        return susEntity;
    }

    /**
     * Get BreadCrumb License.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO getBreadCrumbLicense() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( setBreadCrumbLicenseItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Set BreadCrumb License Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO setBreadCrumbLicenseItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( SimuspaceFeaturesEnum.LICENSES.getKey() );
        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.LICENSES.getId() ) );
        bcItems.setUrl( SYSTEM_LICENSE );
        bcItems.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        return bcItems;
    }

    /**
     * Fill SuSEntity With System Container Entity For License
     *
     * @return SuSEntity
     */
    private SuSEntity fillSuSEntityWithLicense() {

        SuSEntity susEntity = new SystemContainerEntity();
        susEntity.setComposedId( new VersionPrimaryKey() );
        susEntity.getComposedId().setId( UUID.fromString( SimuspaceFeaturesEnum.LICENSES.getId() ) );
        susEntity.setName( SimuspaceFeaturesEnum.LICENSES.getKey() );
        return susEntity;
    }

    /**
     * Create BreadCrumb License.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO createBreadCrumbLicense() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( createBreadCrumbLicenseItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Create BreadCrumb License Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO createBreadCrumbLicenseItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( MessageBundleFactory.getMessage( CREATE ) );
        bcItems.setUrl( ConstantsObjectServiceEndPoints.CREATE_SYSTEM_LICENSE );
        return bcItems;
    }

    /**
     * Fill Create ContextMenuItem For License
     *
     * @return ContextMenuItem
     */
    private ContextMenuItem fillCreateLicenseContextMenuItem() {

        ContextMenuItem contextMenuItem = new ContextMenuItem();
        contextMenuItem.setTitle( MessageBundleFactory.getMessage( CREATE ) );
        contextMenuItem.setUrl( ConstantsObjectServiceEndPoints.CREATE_SYSTEM_LICENSE );
        return contextMenuItem;
    }

    /**
     * Manage BreadCrumb License.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbDTO manageBreadCrumbLicense() {
        BreadCrumbDTO b = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > items = new ArrayList<>();
        items.add( manageBreadCrumbLicenseItem() );
        b.setBreadCrumbItems( items );
        return b;
    }

    /**
     * Manage BreadCrumb License Item.
     *
     * @return BreadCrumbDTO
     */
    private BreadCrumbItemDTO manageBreadCrumbLicenseItem() {
        BreadCrumbItemDTO bcItems = new BreadCrumbItemDTO();
        bcItems.setName( MessageBundleFactory.getMessage( MANAGE ) );
        bcItems.setUrl( ConstantsObjectServiceEndPoints.MANAGE_SYSTEM_LICENSE );
        return bcItems;
    }

    /**
     * Fill Manage ContextMenuItem For License
     *
     * @return ContextMenuItem
     */
    private ContextMenuItem fillManageLicenseContextMenuItem() {

        ContextMenuItem contextMenuItem = new ContextMenuItem();
        contextMenuItem.setTitle( MessageBundleFactory.getMessage( MANAGE ) );
        contextMenuItem.setUrl( ConstantsObjectServiceEndPoints.MANAGE_SYSTEM_LICENSE );
        return contextMenuItem;
    }

}
