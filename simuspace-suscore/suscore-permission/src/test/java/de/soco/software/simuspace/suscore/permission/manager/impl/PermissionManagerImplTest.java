package de.soco.software.simuspace.suscore.permission.manager.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.subject.Subject;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.dao.AclCommonSecurityIdentityDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.AclClassEntity;
import de.soco.software.simuspace.suscore.data.entity.AclEntryEntity;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.permissions.constants.PermissionManageForm;
import de.soco.software.simuspace.suscore.permissions.dao.AclClassDAO;
import de.soco.software.simuspace.suscore.permissions.dao.AclEntryDAO;
import de.soco.software.simuspace.suscore.permissions.dao.AclObjectIdentityDAO;
import de.soco.software.simuspace.suscore.permissions.manager.impl.PermissionManagerImpl;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;

/**
 * The Class PermissionManagerImplTest the permission operations to Dao layer.
 *
 * @author Ahsan Khan
 * @version 2.0
 */
public class PermissionManagerImplTest {

    /**
     * The Constant OBJECT_CREATE_READ_PERMISSION.
     */
    private static final String OBJECT_CREATE_READ_PERMISSION = "Disciplines:create,read";

    /**
     * The Constant SECURITY_IDENTITY_ID_FOR_PERMIT.
     */
    private static final UUID SECURITY_IDENTITY_ID_FOR_PERMIT = UUID.fromString( "c43376e2-63ca-4bfc-9488-306a71c294ff" );

    /**
     * The Constant RESOURCE_ID_FOR_PERMIT.
     */
    private static final UUID RESOURCE_ID_FOR_PERMIT = UUID.fromString( "cd7eb485-85f8-4b92-9e64-6586ccdc1d61" );

    /**
     * The Constant CLASS_ID.
     */
    private static final UUID CLASS_ID = UUID.fromString( "da00a3b9-31e0-441d-affa-7b2e812a2882" );

    /**
     * The Constant OBJECT_IDENTITY_ID.
     */
    private static final UUID OBJECT_IDENTITY_ID = UUID.fromString( "6728bfc5-d52c-4cda-9b02-01268b4789fa" );

    /**
     * The Constant PARENT_OBJECT_IDENTITY_ID.
     */
    private static final UUID PARENT_OBJECT_IDENTITY_ID = UUID.fromString( "0197e945-5ff1-4b53-b88e-2813acf522b8" );

    /**
     * The Constant ENTRY_ID.
     */
    private static final UUID ENTRY_ID = UUID.fromString( "ccd26b28-ab5a-4ed4-9e61-648070d76d0d" );

    /**
     * The Constant USER_ID.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * Dummy Object Id.
     */
    private static final UUID DATA_OBJECT_ID = UUID.randomUUID();

    /**
     * Dummy Data Object Name of an object.
     */
    private static final String DATA_OBJECT_NAME = "Test Data Object name";

    /**
     * The Constant OBJECT_TYPE_ID.
     */
    private static final UUID OBJECT_TYPE_ID = UUID.randomUUID();

    /**
     * The Constant DEFAULT_LIFECYCLE_STATUS.
     */
    private static final String WIP_LIFECYCLE_STATUS_ID = "553536c7-71ec-409d-8f48-ec779a98a68e";

    /**
     * Dummy Version Id for test Cases.
     */
    private static final int DEFAULT_VERSION_ID = 1;

    /**
     * The Constant SUPER_USER_ID_FROM_FILE.
     */
    public static final String SUPER_USER_ID = "0";

    /**
     * The Constant CREATE_NEW_OBJECT_INDEX.
     */
    private static final int CREATE_NEW_OBJECT_INDEX = 0;

    /**
     * The Constant KILL_INDEX.
     */
    private static final int KILL_INDEX = 2;

    /**
     * The Constant SHARE_INDEX.
     */
    private static final int EXECUTE_INDEX = 1;

    /**
     * The Constant SHARE_INDEX.
     */
    private static final int SHARE_INDEX = 3;

    /**
     * The Constant OBJECT.
     */
    private static final String OBJECT = "Object";

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The permission manager.
     */
    private PermissionManagerImpl permissionManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The class DAO.
     */
    private AclClassDAO classDAO;

    /**
     * The object identity DAO.
     */
    private AclObjectIdentityDAO objectIdentityDAO;

    /**
     * The object identity entity.
     */
    private AclObjectIdentityEntity objectIdentityEntity;

    /**
     * The Constant SID_ID.
     */
    private static final UUID SID_ID = UUID.randomUUID();

    /**
     * The acl common security identity DAO.
     */
    private AclCommonSecurityIdentityDAO aclCommonSecurityIdentityDAO;

    /**
     * The Constant USER.
     */
    private static final String USER = "User";

    /**
     * The Constant RESTRICTED_USER.
     */
    private static final String NOT_RESTRICTED = "No";

    /**
     * The Constant RESTRICTED_USER.
     */
    private static final String RESTRICTED = "Yes";

    /**
     * The entry DAO.
     */
    private AclEntryDAO entryDAO;

    /**
     * The subject for current session from apache shiro
     */
    private Subject subject;

    /**
     * The acl entry entities.
     */
    private List< AclEntryEntity > aclEntryEntities;

    /**
     * The Constant RESOURCE_ID.
     */
    private static final UUID RESOURCE_ID = UUID.randomUUID();

    /**
     * The Constant ROOT_RESOURCE.
     */
    private static final String ROOT_RESOURCE = "Root Resource";

    /**
     * The Constant PARENT_RESOURCE_ID.
     */
    private static final UUID PARENT_RESOURCE_ID = UUID.randomUUID();

    /**
     * The Constant RESOURCE_NAME.
     */
    private static final String RESOURCE_NAME = "Resource Object";

    /**
     * The Constant LICENSE.
     */
    private static final String LICENSE = "de.soco.software.simuspace.suscore.data.entity.LicenseEntity";

    /**
     * The Constant INVALID_LICENSE.
     */
    private static final String INVALID_LICENSE = "de.soco.software.simuspace.suscore.data.ehcntity.LicenseEntity";

    /**
     * The Constant DESCRIPTION.
     */
    private static final String DESCRIPTION = "License feature to consume.";

    /**
     * The Constant ENABLE.
     */
    private static final int ENABLE = 1;

    /**
     * The Constant DISBALE.
     */
    private static final int DISBALE = 0;

    /**
     * The Constant INVALID_PERMISSION.
     */
    private static final String INVALID_PERMISSION = "Illegal";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * SusDao reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * To initialize the objects and mocking objects
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        susDAO = mockControl.createMock( SuSGenericObjectDAO.class );
        permissionManager = new PermissionManagerImpl();
        classDAO = mockControl.createMock( AclClassDAO.class );
        objectIdentityDAO = mockControl.createMock( AclObjectIdentityDAO.class );
        entryDAO = mockControl.createMock( AclEntryDAO.class );
        userCommonManager = mockControl.createMock( UserCommonManager.class );
        aclCommonSecurityIdentityDAO = mockControl.createMock( AclCommonSecurityIdentityDAO.class );
        subject = Mockito.mock( Subject.class );
        permissionManager.setClassDAO( classDAO );
        permissionManager.setObjectIdentityDAO( objectIdentityDAO );
        permissionManager.setEntryDAO( entryDAO );
        permissionManager.setUserCommonManager( userCommonManager );
        permissionManager.setSusDAO( susDAO );
        aclEntryEntities = new ArrayList<>();
        aclEntryEntities.add( fillEntryEntity( objectIdentityEntity, fillSecurityIdentity( SID_ID ) ) );
    }

    /**
     * Should bind and permit allow new feature with provided security identity.
     */
    @Test
    public void shouldBindAndPermitAllowNewFeatureWithProvidedSecurityIdentity() {
        mockPermitFeatureExternalCalls();
        mockControl.replay();
        Assert.assertTrue( permissionManager.permitFeaturesBySecurityIdentityAndResourceId( EasyMock.anyObject( EntityManager.class ),
                fillCheckBoxWithEnable(), RESOURCE_ID_FOR_PERMIT, fillSecurityIdentityForRole() ) );
    }

    /**
     * Should disallow resource permission binded with security identity.
     */
    @Test
    public void shouldDisallowResourcePermissionBindedWithSecurityIdentity() {
        AclClassEntity classEntity = fillClassEntity();
        EasyMock.expect(
                        classDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( classEntity ).anyTimes();
        AclObjectIdentityEntity objectIdentityEntity = fillObjectIdentity( classEntity, fillSecurityIdentityForRole() );
        EasyMock.expect( objectIdentityDAO.getAclObjectIdentityByClassEntityIdAndBySecurityIdentityEntityId(
                        EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( objectIdentityEntity ).anyTimes();
        EasyMock.expect( entryDAO.getAclEntryEntityByObjectIdAndMaskAndSidId( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) )
                .andReturn( fillEntryEntity( objectIdentityEntity, fillSecurityIdentityForRole() ) ).anyTimes();

        EasyMock.expect( entryDAO.saveOrUpdate( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclEntryEntity.class ) ) )
                .andReturn( fillEntryEntity( objectIdentityEntity, fillSecurityIdentityForRole() ) ).anyTimes();

        mockControl.replay();
        Assert.assertTrue( permissionManager.permitFeaturesBySecurityIdentityAndResourceId( EasyMock.anyObject( EntityManager.class ),
                fillCheckBoxWithDisbale(), RESOURCE_ID_FOR_PERMIT, fillSecurityIdentityForRole() ) );
    }

    /**
     * Should check new permission binded with security identity when permission is allowed.
     */
    @Test
    public void shouldCheckNewPermissionBindedWithSecurityIdentityWhenPermissionIsAllowed() {
        AclClassEntity classEntity = fillClassEntity();
        EasyMock.expect(
                        classDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( classEntity ).anyTimes();
        AclObjectIdentityEntity objectIdentityEntity = fillObjectIdentity( classEntity, fillSecurityIdentityForRole() );
        EasyMock.expect( objectIdentityDAO.getAclObjectIdentityByClassEntityIdAndBySecurityIdentityEntityId(
                        EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( objectIdentityEntity ).anyTimes();
        EasyMock.expect( entryDAO.getAclEntryEntityByObjectIdAndMaskAndSidId( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) )
                .andReturn( fillEntryEntity( objectIdentityEntity, fillSecurityIdentityForRole() ) ).anyTimes();

        EasyMock.expect( entryDAO.saveOrUpdate( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclEntryEntity.class ) ) )
                .andReturn( fillEntryEntity( objectIdentityEntity, fillSecurityIdentityForRole() ) ).anyTimes();

        mockControl.replay();
        Assert.assertTrue( permissionManager.permitFeaturesBySecurityIdentityAndResourceId( EasyMock.anyObject( EntityManager.class ),
                fillCheckBoxWithEnable(), RESOURCE_ID_FOR_PERMIT, fillSecurityIdentityForRole() ) );
    }

    /**
     * Should allow resource on existing permission binded with security identity.
     */
    @Test
    public void shouldAllowResourceOnExistingPermissionBindedWithSecurityIdentity() {
        mockPermitFeatureExternalCalls();
        mockControl.replay();
        Assert.assertTrue( permissionManager.permitFeaturesBySecurityIdentityAndResourceId( EasyMock.anyObject( EntityManager.class ),
                fillCheckBoxWithEnable(), RESOURCE_ID_FOR_PERMIT, fillSecurityIdentityForRole() ) );
    }

    /**
     * Should allow resource on new permission binded with security identity.
     */
    @Test
    public void shouldAllowResourceOnNewPermissionBindedWithSecurityIdentity() {
        mockPermitFeatureExternalCalls();
        mockControl.replay();
        Assert.assertTrue( permissionManager.permitFeaturesBySecurityIdentityAndResourceId( EasyMock.anyObject( EntityManager.class ),
                fillCheckBoxWithEnable(), RESOURCE_ID_FOR_PERMIT, fillSecurityIdentityForRole() ) );
    }

    private void mockPermitFeatureExternalCalls() {
        AclClassEntity classEntity = fillClassEntity();
        EasyMock.expect(
                        classDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( classEntity ).anyTimes();
        AclObjectIdentityEntity objectIdentityEntity = fillObjectIdentity( classEntity, fillSecurityIdentityForRole() );
        EasyMock.expect( objectIdentityDAO.getAclObjectIdentityByClassEntityId( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( objectIdentityEntity ).anyTimes();
        EasyMock.expect( objectIdentityDAO.getAclObjectIdentityByClassEntityIdAndBySecurityIdentityEntityId(
                EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( objectIdentityEntity );
        EasyMock.expect(
                        objectIdentityDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclObjectIdentityEntity.class ) ) )
                .andReturn( new AclObjectIdentityEntity() ).anyTimes();
        EasyMock.expect(
                        objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( objectIdentityEntity );
        EasyMock.expect( entryDAO.saveOrUpdate( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclEntryEntity.class ) ) )
                .andReturn( fillEntryEntity( objectIdentityEntity, fillSecurityIdentityForRole() ) ).anyTimes();
        EasyMock.expect( entryDAO.getAclEntryEntityByObjectIdAndMaskAndSidId( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) )
                .andReturn( fillEntryEntity( objectIdentityEntity, fillSecurityIdentityForRole() ) ).anyTimes();
    }

    /**
     * Should return all permissions if no permission is binded with security identity.
     */
    @Test
    public void shouldReturnAllPermissionsIfNoPermissionIsBindedWithSecurityIdentity() {
        List< AclClassEntity > classEntities = new ArrayList<>();
        classEntities.add( fillClassEntity() );
        EasyMock.expect( classDAO.getAllObjectList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( classEntities ).anyTimes();
        mockControl.replay();
        List< PermissionManageForm > resourceAccessControlDTOs = permissionManager
                .getResourceAccessControlList( EasyMock.anyObject( EntityManager.class ), SECURITY_IDENTITY_ID_FOR_PERMIT );
        for ( PermissionManageForm resourceAccessControlDTO : resourceAccessControlDTOs ) {
            checkAssertionForFeaturePermission( resourceAccessControlDTO );
        }
    }

    /**
     * Should throw exception when invalid fully qualified class name is provided.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidFullyQualifiedClassNameIsProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), INVALID_LICENSE ) );
        List< AclClassEntity > classEntities = new ArrayList<>();
        classEntities.add( fillInvalidClassEntity() );
        EasyMock.expect( classDAO.getAllObjectList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( classEntities ).anyTimes();
        mockControl.replay();
        List< PermissionManageForm > resourceAccessControlDTOs = permissionManager
                .getResourceAccessControlList( EasyMock.anyObject( EntityManager.class ), SECURITY_IDENTITY_ID_FOR_PERMIT );
        for ( PermissionManageForm resourceAccessControlDTO : resourceAccessControlDTOs ) {
            checkAssertionForFeaturePermission( resourceAccessControlDTO );
        }
    }

    /**
     * Should return all permissions if permission is binded with security identity and view audit log permission applied.
     */
    @Test
    public void shouldReturnAllPermissionsIfPermissionIsBindedWithSecurityIdentityAndViewAuditLogPermissionApplied() {
        List< AclClassEntity > classEntities = new ArrayList<>();
        classEntities.add( fillClassEntity() );
        EasyMock.expect( classDAO.getAllObjectList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( classEntities ).anyTimes();
        AclObjectIdentityEntity objectIdentityEntity = fillObjectIdentity( fillClassEntity(), fillSecurityIdentityForRole() );
        EasyMock.expect( objectIdentityDAO.getAclObjectIdentityByClassEntityIdAndBySecurityIdentityEntityId(
                        EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( objectIdentityEntity ).anyTimes();

        EasyMock.expect( entryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( aclEntryEntities ).anyTimes();

        mockControl.replay();
        List< PermissionManageForm > resourceAccessControlDTOs = permissionManager
                .getResourceAccessControlList( EasyMock.anyObject( EntityManager.class ), SECURITY_IDENTITY_ID_FOR_PERMIT );
        for ( PermissionManageForm resourceAccessControlDTO : resourceAccessControlDTOs ) {
            checkAssertionForFeaturePermission( resourceAccessControlDTO );
        }
    }

    /**
     * Should add root object to acl when inheritance flag false.
     */
    @Test
    public void shouldAddRootObjectToAclWhenInheritanceFlagFalse() {
        ResourceAccessControlDTO resourceAccessControlDTO = fillResourceAccessControlDTO();
        EasyMock.expect( classDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclClassEntity.class ) ) )
                .andReturn( fillClassEntity() );
        EasyMock.expect(
                        objectIdentityDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclObjectIdentityEntity.class ) ) )
                .andReturn( new AclObjectIdentityEntity() ).anyTimes();
        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
        EasyMock.expect( aclCommonSecurityIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();
        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclObjectIdentityEntity() ).anyTimes();
        mockControl.replay();
        Assert.assertTrue( permissionManager.addObjectToAcl( EasyMock.anyObject( EntityManager.class ), resourceAccessControlDTO ) );
    }

    /**
     * Should copy permission from its parent when parent has inherit false.
     */
    @Test
    public void shouldCopyPermissionFromItsParentWhenParentHasInheritFalse() {
        ResourceAccessControlDTO resourceAccessControlDTO = fillParentResourceAccessControlDTO( RESOURCE_ID, RESOURCE_NAME, OBJECT, false,
                PARENT_RESOURCE_ID, RESOURCE_NAME, OBJECT, false );

        EasyMock.expect( classDAO.getAclClassByQualifiedName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( new AclClassEntity() ).anyTimes();
        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
        EasyMock.expect( aclCommonSecurityIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();
        EasyMock.expect(
                        objectIdentityDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclObjectIdentityEntity.class ) ) )
                .andReturn( fillAclObjectIdentityEntity() ).anyTimes();

        List< AclEntryEntity > aclEntryEntities = new ArrayList<>();
        aclEntryEntities.add( fillEntryEntity( fillAclObjectIdentityEntity(), fillAclObjectIdentityEntity().getOwnerSid() ) );

        EasyMock.expect( entryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( aclEntryEntities ).anyTimes();

        mockControl.replay();
        Assert.assertTrue( permissionManager.addObjectToAcl( EasyMock.anyObject( EntityManager.class ), resourceAccessControlDTO ) );
    }

    /**
     * Should throw exception when trying to copy permission from its parent but object is not availabe.
     */
    @Test
    public void shouldThrowExceptionWhenTryingToCopyPermissionFromItsParentButObjectIsNotAvailabe() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.OBJECT_CANT_BE_NULL.getKey() ) );
        ResourceAccessControlDTO resourceAccessControlDTO = fillParentResourceAccessControlDTO( RESOURCE_ID, RESOURCE_NAME, OBJECT, false,
                PARENT_RESOURCE_ID, RESOURCE_NAME, OBJECT, false );
        EasyMock.expect( classDAO.getAclClassByQualifiedName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( new AclClassEntity() ).anyTimes();
        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
        EasyMock.expect( aclCommonSecurityIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();
        mockControl.replay();
        permissionManager.addObjectToAcl( EasyMock.anyObject( EntityManager.class ), resourceAccessControlDTO );
    }

    /**
     * Should not copy permission from its parent when parent has inherit true.
     */
    @Test
    public void shouldNotCopyPermissionFromItsParentWhenParentHasInheritTrue() {
        ResourceAccessControlDTO resourceAccessControlDTO = fillParentResourceAccessControlDTO( RESOURCE_ID, RESOURCE_NAME, OBJECT, false,
                PARENT_RESOURCE_ID, RESOURCE_NAME, OBJECT, true );

        EasyMock.expect(
                        objectIdentityDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclObjectIdentityEntity.class ) ) )
                .andReturn( new AclObjectIdentityEntity() ).anyTimes();

        EasyMock.expect( classDAO.getAclClassByQualifiedName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( new AclClassEntity() ).anyTimes();
        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();

        EasyMock.expect( aclCommonSecurityIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();

        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillAclObjectIdentityEntity() ).anyTimes();

        mockControl.replay();
        Assert.assertTrue( permissionManager.addObjectToAcl( EasyMock.anyObject( EntityManager.class ), resourceAccessControlDTO ) );
    }

    /**
     * Should not add tree node TO acl when already exist.
     */
    @Test
    public void shouldNotAddTreeNodeTOAclWhenAlreadyExist() {
        ResourceAccessControlDTO resourceAccessControlDTO = fillParentResourceAccessControlDTO( RESOURCE_ID, RESOURCE_NAME, OBJECT, false,
                PARENT_RESOURCE_ID, RESOURCE_NAME, OBJECT, false );
        EasyMock.expect(
                        classDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( fillClassEntity() );
        mockControl.replay();
        Assert.assertTrue( permissionManager.addTreeNodeToAcl( EasyMock.anyObject( EntityManager.class ), resourceAccessControlDTO ) );
    }

    /**
     * Should add features to acl when valid input parameters provided.
     */
    @Test
    public void shouldAddFeaturesToAclWhenValidInputParametersProvided() {
        ResourceAccessControlDTO resourceAccessControlDTO = fillParentResourceAccessControlDTO( RESOURCE_ID, RESOURCE_NAME, OBJECT, false,
                PARENT_RESOURCE_ID, RESOURCE_NAME, OBJECT, false );
        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillAclObjectIdentityEntity() ).anyTimes();
        EasyMock.expect( classDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclClassEntity.class ) ) )
                .andReturn( new AclClassEntity() ).anyTimes();
        mockControl.replay();
        Assert.assertTrue( permissionManager.addTreeNodeToAcl( EasyMock.anyObject( EntityManager.class ), resourceAccessControlDTO ) );
    }

    /**
     * Should add tree node in acl and do not copy permission from its parent when parent has inherit true.
     */
    @Test
    public void shouldAddTreeNodeInAclAndDoNotCopyPermissionFromItsParentWhenParentHasInheritTrue() {
        ResourceAccessControlDTO resourceAccessControlDTO = fillParentResourceAccessControlDTO( RESOURCE_ID, RESOURCE_NAME, OBJECT, false,
                PARENT_RESOURCE_ID, RESOURCE_NAME, OBJECT, false );

        EasyMock.expect( classDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclClassEntity.class ) ) )
                .andReturn( fillClassEntity() );
        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillAclObjectIdentityEntity() ).anyTimes();
        EasyMock.expect(
                        objectIdentityDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( AclObjectIdentityEntity.class ) ) )
                .andReturn( fillAclObjectIdentityEntity() ).anyTimes();

        EasyMock.expect( objectIdentityDAO.getAclObjectIdentityByClassEntityId( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillAclObjectIdentityEntityWithInheritTrue() ).anyTimes();

        mockControl.replay();
        Assert.assertTrue( permissionManager.addTreeNodeToAcl( EasyMock.anyObject( EntityManager.class ), resourceAccessControlDTO ) );
    }

    /**
     * Should return valid resource access control DTO.
     */
    @Test
    public void shouldReturnValidResourceAccessControlDTO() {
        ResourceAccessControlDTO expected = fillResourceAccessControlDTOForLicense();
        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillAclObjectIdentityEntityForLicense() );
        mockControl.replay();
        ResourceAccessControlDTO actual = permissionManager.getResourceAccessControl( EasyMock.anyObject( EntityManager.class ), CLASS_ID );
        Assert.assertEquals( expected.getId(), actual.getId() );
        Assert.assertEquals( expected.isInherit(), actual.isInherit() );
    }

    /**
     * Should permit allow if permission applied.
     */
    @Test
    public void shouldPermitAllowIfPermissionApplied() {
        Mockito.when( subject.isPermitted( Mockito.anyString() ) ).thenReturn( true );
        Mockito.when( subject.getPrincipal() ).thenReturn( prepareUser() );
        Assert.assertTrue( permissionManager.isPermitted( USER_ID.toString(), OBJECT_CREATE_READ_PERMISSION ) );
    }

    /**
     * Should get true when user has read permission to resource.
     */
    @Test
    public void shouldGetTrueWhenUserHasReadPermissionToResource() {
        Mockito.when( subject.isPermitted( Mockito.anyString() ) ).thenReturn( true );
        Mockito.when( subject.getPrincipal() ).thenReturn( prepareUser() );
        mockControl.replay();
        boolean actual = permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), USER_ID, RESOURCE_ID_FOR_PERMIT );
        Assert.assertTrue( actual );
    }

    /**
     * Should get false when user has no read permission to resource.
     */
    @Test
    public void shouldGetFalseWhenUserHasNoReadPermissionToResource() {
        Mockito.when( subject.isPermitted( Mockito.anyString() ) ).thenReturn( false );
        Mockito.when( subject.getPrincipal() ).thenReturn( prepareUser() );
        mockControl.replay();
        boolean actual = permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), USER_ID, RESOURCE_ID_FOR_PERMIT );
        Assert.assertFalse( actual );
    }

    /**
     * Should get false when user has no write permission to resource.
     */
    @Test
    public void shouldGetFalseWhenUserHasNoWritePermissionToResource() {
        Mockito.when( subject.isPermitted( Mockito.anyString() ) ).thenReturn( false );
        Mockito.when( subject.getPrincipal() ).thenReturn( prepareUser() );
        mockControl.replay();
        boolean actual = permissionManager.isWritable( EasyMock.anyObject( EntityManager.class ), USER_ID, RESOURCE_ID_FOR_PERMIT );
        Assert.assertFalse( actual );
    }

    /**
     * Should get true when user has write permission to resource.
     */
    @Test
    public void shouldGetTrueWhenUserHasWritePermissionToResource() {
        Mockito.when( subject.isPermitted( Mockito.anyString() ) ).thenReturn( true );
        Mockito.when( subject.getPrincipal() ).thenReturn( prepareUser() );
        mockControl.replay();
        boolean actual = permissionManager.isWritable( EasyMock.anyObject( EntityManager.class ), USER_ID, RESOURCE_ID_FOR_PERMIT );
        Assert.assertTrue( actual );
    }

    /**
     * Should successfully grant permission with non restricted user when valid resource id is given.
     */
    @Test
    public void shouldSuccessfullyGrantPermissionWithNonRestrictedUserWhenValidResourceIdIsGiven() {
        objectIdentityEntity = fillAclObjectIdentityEntity();
        List< AclEntryEntity > entryEntities = new ArrayList<>();
        entryEntities.add( fillEntryEntity( fillAclObjectIdentityEntity(), fillAclObjectIdentityEntity().getOwnerSid() ) );
        Mockito.when( subject.isPermitted( Mockito.anyString() ) ).thenReturn( true );
        EasyMock.expect( entryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( entryEntities ).anyTimes();
        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( objectIdentityEntity ).anyTimes();
        final UserDTO userDTO = prepareUserDTO( USER, NOT_RESTRICTED );
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDTO ).anyTimes();
        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
        EasyMock.expect( aclCommonSecurityIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();

        mockControl.replay();
        FiltersDTO expectedFilters = getFilledFilters();
        List< ManageObjectDTO > actual = permissionManager.prepareObjectManagerDTOs( EasyMock.anyObject( EntityManager.class ), RESOURCE_ID,
                true, expectedFilters );
        Assert.assertNull( actual.get( FIRST_INDEX ).getName() );
    }

    /**
     * Should successfully grant permission with restricted user when valid resource id is given.
     */
    @Test
    public void shouldSuccessfullyGrantPermissionWithRestrictedUserWhenValidResourceIdIsGiven() {
        objectIdentityEntity = fillAclObjectIdentityEntity();
        List< AclEntryEntity > entryEntities = new ArrayList<>();
        entryEntities.add( fillEntryEntity( fillAclObjectIdentityEntity(), fillAclObjectIdentityEntity().getOwnerSid() ) );
        Mockito.when( subject.isPermitted( Mockito.anyString() ) ).thenReturn( true );
        EasyMock.expect( entryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( entryEntities ).anyTimes();
        EasyMock.expect( entryDAO.getAclEntryListByObjectIdAndSidId( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) ).andReturn( entryEntities ).anyTimes();
        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( objectIdentityEntity ).anyTimes();
        final UserDTO userDTO = prepareUserDTO( USER, RESTRICTED );
        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDTO ).anyTimes();
        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
        EasyMock.expect( aclCommonSecurityIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();

        mockControl.replay();
        FiltersDTO expectedFilters = getFilledFilters();
        List< ManageObjectDTO > actual = permissionManager.prepareObjectManagerDTOs( EasyMock.anyObject( EntityManager.class ), RESOURCE_ID,
                true, expectedFilters );
        Assert.assertNull( actual.get( FIRST_INDEX ).getName() );
    }

    /**
     * Should successfully grant permission with group when valid resource id is given.
     */
    @Test
    public void shouldSuccessfullyGrantPermissionWithGroupWhenValidResourceIdIsGiven() {
        objectIdentityEntity = fillAclObjectIdentityEntity();
        List< AclEntryEntity > entryEntities = new ArrayList<>();
        entryEntities.add( fillEntryEntity( fillAclObjectIdentityEntity(), fillAclObjectIdentityEntity().getOwnerSid() ) );
        Mockito.when( subject.isPermitted( Mockito.anyString() ) ).thenReturn( true );
        EasyMock.expect( entryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( entryEntities ).anyTimes();
        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( objectIdentityEntity ).anyTimes();

        final SuSUserGroupDTO groupDto = prepareSusGroup();
        EasyMock.expect( userCommonManager.getUserGroupById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( groupDto ).anyTimes();
        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
        EasyMock.expect( aclCommonSecurityIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();

        mockControl.replay();
        FiltersDTO expectedFilters = getFilledFilters();
        List< ManageObjectDTO > actual = permissionManager.prepareObjectManagerDTOs( EasyMock.anyObject( EntityManager.class ), RESOURCE_ID,
                true, expectedFilters );
        Assert.assertEquals( RESOURCE_NAME, actual.get( FIRST_INDEX ).getName() );

    }

    /**
     * Should successfully grant permission with parent inherit true when valid resource id is given.
     */
    @Test
    public void shouldSuccessfullyGrantPermissionWithParentInheritTrueWhenValidResourceIdIsGiven() {
        objectIdentityEntity = fillAclObjectIdentityEntityWithInheritTrue();
        AclObjectIdentityEntity childIdentityEntity = fillAclObjectIdentityEntity();

        List< AclEntryEntity > entryEntities = new ArrayList<>();
        entryEntities.add( fillEntryEntity( fillAclObjectIdentityEntity(), fillAclObjectIdentityEntity().getOwnerSid() ) );
        Mockito.when( subject.isPermitted( Mockito.anyString() ) ).thenReturn( true );
        EasyMock.expect( entryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( entryEntities ).anyTimes();
        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( objectIdentityEntity );
        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( childIdentityEntity );

        final SuSUserGroupDTO groupDto = prepareSusGroup();
        EasyMock.expect( userCommonManager.getUserGroupById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( groupDto ).anyTimes();
        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
        EasyMock.expect( aclCommonSecurityIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();

        mockControl.replay();
        FiltersDTO expectedFilters = getFilledFilters();
        List< ManageObjectDTO > actual = permissionManager.prepareObjectManagerDTOs( EasyMock.anyObject( EntityManager.class ), RESOURCE_ID,
                true, expectedFilters );
        Assert.assertEquals( RESOURCE_NAME, actual.get( FIRST_INDEX ).getName() );

    }

    /**
     * Should return empty list when no group has permission with given resource id.
     */
    @Test
    public void shouldReturnEmptyListWhenNoGroupHasPermissionWithGivenResourceId() {
        objectIdentityEntity = fillAclObjectIdentityEntity();
        List< AclEntryEntity > entryEntities = new ArrayList<>();
        entryEntities.add( fillEntryEntity( fillAclObjectIdentityEntity(), fillAclObjectIdentityEntity().getOwnerSid() ) );
        Mockito.when( subject.isPermitted( Mockito.anyString() ) ).thenReturn( true );
        EasyMock.expect( entryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( entryEntities ).anyTimes();

        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( objectIdentityEntity ).anyTimes();

        final SuSUserGroupDTO groupDto = null;
        EasyMock.expect( userCommonManager.getUserGroupById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( groupDto ).anyTimes();
        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
        EasyMock.expect( aclCommonSecurityIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();
        FiltersDTO expectedFilters = getFilledFilters();
        mockControl.replay();
        List< ManageObjectDTO > actual = permissionManager.prepareObjectManagerDTOs( EasyMock.anyObject( EntityManager.class ), RESOURCE_ID,
                true, expectedFilters );
        Assert.assertTrue( actual.isEmpty() );

    }

    /**
     * Should show manage permission columns when api is called.
     */
    @Test
    public void shouldShowManagePermissionColumnsWhenApiIsCalled() {
        TableUI expectedUI = getFilledColumns();
        List< TableColumn > actualTableUI = permissionManager.managePermissionTableUI();
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getData(), actualTableUI.get( FIRST_INDEX ).getData() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getName(), actualTableUI.get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getTitle(), actualTableUI.get( FIRST_INDEX ).getTitle() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getRenderer().getData(),
                actualTableUI.get( FIRST_INDEX ).getRenderer().getData() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getRenderer().getManage(),
                actualTableUI.get( FIRST_INDEX ).getRenderer().getManage() );
    }

    /**
     * Prepare sus group.
     *
     * @return the su S user group DTO
     */
    private SuSUserGroupDTO prepareSusGroup() {
        SuSUserGroupDTO dto = new SuSUserGroupDTO();
        dto.setId( ENTRY_ID );
        dto.setName( RESOURCE_NAME );

        return dto;
    }

    private UserDTO prepareUser() {
        UserDTO dto = new UserDTO();
        dto.setId( USER_ID.toString() );
        dto.setUserUid( USER );

        return dto;
    }

    /**
     * Gets the filled columns.
     *
     * @return the filled columns
     */
    private TableUI getFilledColumns() {
        TableUI tableUI = new TableUI();
        List< TableColumn > columns = new ArrayList<>();
        columns.add( getFilledTableUI() );
        tableUI.setColumns( columns );
        return tableUI;
    }

    /**
     * Gets the filled table UI.
     *
     * @return the filled table UI
     */
    private TableColumn getFilledTableUI() {
        TableColumn tableColumn = new TableColumn();
        tableColumn.setData( "name" );
        tableColumn.setTitle( "Resource Name" );
        tableColumn.setFilter( "text" );
        tableColumn.setSortable( true );
        tableColumn.setName( "name" );
        tableColumn.setRenderer( getFilledRenderer() );
        return tableColumn;
    }

    private FiltersDTO getFilledFilters() {
        FiltersDTO filters = new FiltersDTO();
        filters.setColumns( filters.getColumns() );
        filters.setCurrentView( filters.getCurrentView() );
        filters.setDraw( filters.getDraw() );
        filters.setFilteredRecords( filters.getFilteredRecords() );
        return filters;
    }

    /**
     * Gets the filled renderer.
     *
     * @return the filled renderer
     */
    private Renderer getFilledRenderer() {
        Renderer renderer = new Renderer();
        renderer.setType( "text" );
        renderer.setSeparator( "," );
        renderer.setLabelClass( "" );
        renderer.setData( "name" );
        renderer.setManage( true );
        return renderer;
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
     * Fill parent resource access control DTO.
     *
     * @param resourceId
     *         the resource id
     * @param resourceName
     *         the resource name
     * @param objectType
     *         the object type
     * @param isInherit
     *         the is inherit
     * @param parentResourceId
     *         the parent resource id
     * @param parentResourceName
     *         the parent resource name
     * @param parentObjectType
     *         the parent object type
     * @param parentIsInherit
     *         the parent is inherit
     *
     * @return the resource access control DTO
     */
    private ResourceAccessControlDTO fillParentResourceAccessControlDTO( UUID resourceId, String resourceName, String objectType,
            boolean isInherit, UUID parentResourceId, String parentResourceName, String parentObjectType, boolean parentIsInherit ) {
        ResourceAccessControlDTO resourceAccessControlDTO = new ResourceAccessControlDTO();
        resourceAccessControlDTO.setId( resourceId );
        resourceAccessControlDTO.setName( resourceName );
        resourceAccessControlDTO.setInherit( isInherit );
        ResourceAccessControlDTO parentResourceAccessControlDTO = new ResourceAccessControlDTO();
        parentResourceAccessControlDTO.setId( parentResourceId );
        parentResourceAccessControlDTO.setName( parentResourceName );
        parentResourceAccessControlDTO.setInherit( parentIsInherit );

        resourceAccessControlDTO.setParent( parentResourceAccessControlDTO );
        return resourceAccessControlDTO;
    }

    /**
     * Fill acl object identity entity.
     *
     * @return the acl object identity entity
     */
    private AclObjectIdentityEntity fillAclObjectIdentityEntity() {
        AclObjectIdentityEntity aclObjectIdentityEntity = new AclObjectIdentityEntity();
        aclObjectIdentityEntity.setId( UUID.randomUUID() );
        aclObjectIdentityEntity.setInherit( false );
        aclObjectIdentityEntity.setOwnerSid( fillSecurityIdentity( SID_ID ) );

        return aclObjectIdentityEntity;
    }

    /**
     * Fill acl object identity entity with inherit true.
     *
     * @return the acl object identity entity
     */
    private AclObjectIdentityEntity fillAclObjectIdentityEntityWithInheritTrue() {
        AclObjectIdentityEntity aclObjectIdentityEntity = new AclObjectIdentityEntity();
        aclObjectIdentityEntity.setId( UUID.randomUUID() );
        aclObjectIdentityEntity.setInherit( true );
        aclObjectIdentityEntity.setParentObject( fillAclObjectIdentityEntityAsParent() );
        return aclObjectIdentityEntity;
    }

    /**
     * Fill acl object identity entity as parent.
     *
     * @return the acl object identity entity
     */
    private AclObjectIdentityEntity fillAclObjectIdentityEntityAsParent() {
        AclObjectIdentityEntity aclObjectIdentityEntity = new AclObjectIdentityEntity();
        aclObjectIdentityEntity.setId( UUID.randomUUID() );
        aclObjectIdentityEntity.setInherit( false );
        return aclObjectIdentityEntity;
    }

    /**
     * Fill resource access control DTO.
     *
     * @return the resource access control DTO
     */
    private ResourceAccessControlDTO fillResourceAccessControlDTO() {
        ResourceAccessControlDTO resourceAccessControlDTO = new ResourceAccessControlDTO();
        resourceAccessControlDTO.setId( RESOURCE_ID );
        resourceAccessControlDTO.setName( ROOT_RESOURCE );
        return resourceAccessControlDTO;
    }

    /**
     * Fill class entity.
     *
     * @return the class entity
     */
    private AclClassEntity fillClassEntity() {
        AclClassEntity classEntity = new AclClassEntity();
        classEntity.setId( CLASS_ID );
        classEntity.setQualifiedName( LICENSE );
        classEntity.setDescription( DESCRIPTION );
        classEntity.setCreatedOn( new Date() );
        return classEntity;
    }

    /**
     * Fill invalid class entity.
     *
     * @return the acl class entity
     */
    private AclClassEntity fillInvalidClassEntity() {
        AclClassEntity classEntity = new AclClassEntity();
        classEntity.setId( CLASS_ID );
        classEntity.setQualifiedName( INVALID_LICENSE );
        classEntity.setDescription( DESCRIPTION );
        classEntity.setCreatedOn( new Date() );
        return classEntity;
    }

    /**
     * Fill resource access control DTO for license.
     *
     * @return the resource access control DTO
     */
    private ResourceAccessControlDTO fillResourceAccessControlDTOForLicense() {
        ResourceAccessControlDTO resourceAccessControlDTO = new ResourceAccessControlDTO();
        resourceAccessControlDTO.setId( CLASS_ID );
        resourceAccessControlDTO.setDescription( DESCRIPTION );
        resourceAccessControlDTO.setName( LICENSE );
        return resourceAccessControlDTO;
    }

    /**
     * Fill acl object identity entity for license.
     *
     * @return the acl object identity entity
     */
    private AclObjectIdentityEntity fillAclObjectIdentityEntityForLicense() {
        AclObjectIdentityEntity aclObjectIdentityEntity = new AclObjectIdentityEntity();
        aclObjectIdentityEntity.setId( CLASS_ID );
        aclObjectIdentityEntity.setInherit( false );
        return aclObjectIdentityEntity;
    }

    /**
     * Fill check box with enable.
     *
     * @return the check box
     */
    private CheckBox fillCheckBoxWithEnable() {
        CheckBox checkBox = new CheckBox();
        checkBox.setName( PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() );
        checkBox.setValue( ENABLE );
        return checkBox;
    }

    /**
     * Fill check box with enable.
     *
     * @return the check box
     */
    private CheckBox fillCheckBoxWithDisbale() {
        CheckBox checkBox = new CheckBox();
        checkBox.setName( PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() );
        checkBox.setValue( DISBALE );
        return checkBox;
    }

    /**
     * Fill security identity.
     *
     * @return the security identity entity
     */
    private AclSecurityIdentityEntity fillSecurityIdentityForRole() {
        AclSecurityIdentityEntity securityIdentityEntity = new AclSecurityIdentityEntity();
        securityIdentityEntity.setId( UUID.randomUUID() );
        securityIdentityEntity.setSid( SECURITY_IDENTITY_ID_FOR_PERMIT );
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
     * @return the entry entity
     */
    private AclEntryEntity fillEntryEntity( AclObjectIdentityEntity objectIdentityEntity,
            AclSecurityIdentityEntity securityIdentityEntity ) {
        AclEntryEntity entryEntity = new AclEntryEntity();
        entryEntity.setId( ENTRY_ID );
        entryEntity.setCreatedOn( new Date() );
        entryEntity.setModifiedOn( new Date() );
        entryEntity.setMask( PermissionMatrixEnum.VIEW.getKey() );
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
        AclObjectIdentityEntity objectIdentityEntity = new AclObjectIdentityEntity();
        objectIdentityEntity.setId( OBJECT_IDENTITY_ID );
        objectIdentityEntity.setClassEntity( classEntity );
        objectIdentityEntity.setOwnerSid( securityIdentityEntity );
        objectIdentityEntity.setCreatedOn( new Date() );
        return objectIdentityEntity;
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
     * Check assertion for feature permission.
     *
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     */
    private void checkAssertionForFeaturePermission( PermissionManageForm resourceAccessControlDTO ) {
        Assert.assertEquals( PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(),
                resourceAccessControlDTO.getPermissionDTOs().get( CREATE_NEW_OBJECT_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.EXECUTE.getValue(),
                resourceAccessControlDTO.getPermissionDTOs().get( EXECUTE_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.KILL.getValue(),
                resourceAccessControlDTO.getPermissionDTOs().get( KILL_INDEX ).getMatrexValue() );
        Assert.assertEquals( PermissionMatrixEnum.SHARE.getValue(),
                resourceAccessControlDTO.getPermissionDTOs().get( SHARE_INDEX ).getMatrexValue() );
    }

    /**
     * A method to populate the DataObject Entity for Expected Result of test.
     *
     * @return projectEntity;
     */

    private DataObjectEntity fillDataObjectEntity() {

        final DataObjectEntity dob = new DataObjectEntity();
        dob.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
        dob.setName( DATA_OBJECT_NAME );
        dob.setTypeId( OBJECT_TYPE_ID );
        dob.setCreatedOn( new Date() );
        dob.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );

        return dob;
    }

}