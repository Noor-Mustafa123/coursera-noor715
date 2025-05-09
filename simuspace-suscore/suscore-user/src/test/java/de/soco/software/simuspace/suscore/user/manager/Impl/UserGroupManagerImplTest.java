package de.soco.software.simuspace.suscore.user.manager.Impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.GroupRoleCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.user.dao.AclSecurityIdentityDAO;
import de.soco.software.simuspace.suscore.user.dao.UserGroupDAO;
import de.soco.software.simuspace.suscore.user.manager.UserManager;
import de.soco.software.simuspace.suscore.user.manager.impl.UserGroupManagerImpl;

/**
 * Test Cases for UserGroupManagerImpl Class.
 *
 * @author Nosheen.Sharif
 */
public class UserGroupManagerImplTest {

    /**
     * The manager reference for mocking.
     */
    private UserGroupManagerImpl manager;

    /**
     * The userGroupDao reference for mocking.
     */
    private UserGroupDAO dao;

    /**
     * The userManager reference for mocking.
     */
    private UserManager userManager;

    /**
     * The security identity DAO.
     */
    private AclSecurityIdentityDAO securityIdentityDAO;

    /**
     * The context selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Dummy UserId for test Cases.
     */
    private static final String DEFAULT_USER_ID = UUID.randomUUID().toString();

    /**
     * Dummy GROUP_ID for test Cases.
     */
    private static final UUID GROUP_ID = UUID.randomUUID();

    /**
     * Dummy USER_ID for test Cases.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * The Constant UID.
     */
    private static final String UID = "sces120";

    /**
     * The Constant UID.
     */
    private static final String USER_NAME = "Test_User";

    /**
     * The Constant INVALID_UUID.
     */
    private static final String INVALID_UUID = "as-bh344-udh0";

    /**
     * The Constant DESCRIPTION_FIELD.
     */
    private static final String DESCRIPTION_FIELD = "Description";

    /**
     * The Constant ACTIVE.
     */
    public static final String ACTIVE = "Active";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant Active Group.
     */
    private static final boolean ACTIVE_GRP = true;

    /**
     * Dummy groupEntity object for test data.
     */
    private GroupEntity groupEntity = new GroupEntity();

    /**
     * Dummy susGroupDTo object for test data.
     */
    private SuSUserGroupDTO susGroupDto = new SuSUserGroupDTO();

    /**
     * Error Message for test Case.
     */
    private static final String ERROR_MSG_FOR_GRP_ID = "No group with id : ";

    /**
     * Error Message for test Case.
     */
    private static final String ERROR_MSG_FOR_GRP_NAME = "Name can not be null or empty";

    /**
     * Error Message for test Case.
     */
    private static final String ERROR_MSG_FOR_GRP_DESC_LENGTH = "Value too large for Description (MaxLength=255).";

    /**
     * The Constant DESCRIPTION_WITH_GREATER_THEN_MAX.
     */
    private static final String DESCRPTION_WITH_GREATER_THEN_MAX = "ghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";

    /**
     * The Constant SINGLE.
     */
    private static final String MODE = "single";

    /**
     * The Constant ITEM_ID_SELECTED_ONE.
     */
    public static final UUID ITEM_ID_SELECTED_ONE = UUID.randomUUID();

    /**
     * The Constant ITEM_ID_SELECTED_TWO.
     */
    public static final UUID ITEM_ID_SELECTED_TWO = UUID.randomUUID();

    /**
     * The Constant ITEM_ID_SELECTED_THREE.
     */
    public static final UUID ITEM_ID_SELECTED_THREE = UUID.randomUUID();

    /**
     * The Constant SELECTION_ORIGIN_GROUP.
     */
    public static final String SELECTION_ORIGIN_GROUP = "Group";

    /**
     * The Constant SELECTION_ID.
     */
    public static final UUID SELECTION_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92259ddc" );

    /**
     * The Constant USER_UUID.
     */
    private static final UUID USER_UUID = UUID.randomUUID();

    /**
     * The Constant USER_UUID_Test.
     */
    private static final UUID USER_UUID_TEST = UUID.randomUUID();

    /**
     * The Constant SUPER_USER_ID.
     */
    public static final String SUPER_USER_ID = "1e98a77c-a0be-4983-9137-d9a8acd0ea8b";

    /**
     * The Constant DIRECTORY_ID.
     */
    private static final UUID DIRECTORY_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92459ddc" );

    /**
     * The active account status.
     */

    public static final boolean ACCOUNT_STATUS_ACTIVE = true;

    /**
     * The group role common DAO.
     */
    private GroupRoleCommonDAO groupRoleCommonDAO;

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
        manager = new UserGroupManagerImpl();
        dao = mockControl.createMock( UserGroupDAO.class );
        userManager = mockControl.createMock( UserManager.class );
        licenseManager = mockControl.createMock( LicenseManager.class );
        securityIdentityDAO = mockControl.createMock( AclSecurityIdentityDAO.class );
        selectionManager = mockControl.createMock( SelectionManager.class );
        groupRoleCommonDAO = mockControl.createMock( GroupRoleCommonDAO.class );
        permissionManager = mockControl.createMock( PermissionManager.class );
        manager.setSelectionManager( selectionManager );
        manager.setUserGroupDao( dao );
        manager.setUserManager( userManager );
        manager.setLicenseManager( licenseManager );
        manager.setGroupRoleCommonDAO( groupRoleCommonDAO );
    }

    /**
     * **************************** createUserGroup *****************************************.
     */
    /**
     * Should Successfully Create Group When Valid Group Id Is Given As Input
     */
    @Test( expected = NullPointerException.class )
    public void shouldSuccessfullyCreateGroupWhenValidGroupIdIsGivenAsInput() {
        GroupEntity expectedEntity = getGroupEntity();
        expectedEntity.setUsers( getUsersSetWithTwoRecords() );
        SuSUserGroupDTO expect = getUserGrpDto();
        List< UUID > expectedList = new ArrayList<>();
        expectedList.add( ITEM_ID_SELECTED_ONE );
        expectedList.add( ITEM_ID_SELECTED_TWO );
        expectedList.add( ITEM_ID_SELECTED_THREE );
        EasyMock.expect(
                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( expectedList ).anyTimes();
        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();

        EasyMock.expect( securityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillSecurityIdentity( expectedEntity ) ).anyTimes();
        EasyMock.expect( dao.createUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( GroupEntity.class ),
                EasyMock.anyObject() ) ).andReturn( expectedEntity ).anyTimes();
        EasyMock.expect( dao.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect(
                        userManager.getUsersByGroupEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( GroupEntity.class ) ) )
                .andReturn( new ArrayList<>( expectedEntity.getUsers() ) ).anyTimes();
        EasyMock.expect( userManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( getUserModel() ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        SuSUserGroupDTO actual = manager.createUserGroup( SUPER_USER_ID, expect );
        Assert.assertEquals( actual, expect );

    }

    /**
     * Should Get Validation Error Msg In Create Group Entity When Group Name is Empty.
     */
    @Test
    public void shouldGetValidationErrorMsgInCreateGroupEntityWhenGroupNameisEmpty() {
        thrown.expect( SusException.class );
        thrown.expectMessage( ERROR_MSG_FOR_GRP_NAME );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        SuSUserGroupDTO expected = getUserGrpDto();
        expected.setName( StringUtils.EMPTY );

        manager.createUserGroup( SUPER_USER_ID, expected );

    }

    /**
     * Should get validation error msg in create group entity when description fieldlegth is greater than 255.
     */
    @Test
    public void shouldGetValidationErrorMsgInCreateGroupEntityWhenDescriptionFieldlegthIsGreaterThan255() {

        thrown.expect( SusException.class );
        thrown.expectMessage( ERROR_MSG_FOR_GRP_DESC_LENGTH );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        SuSUserGroupDTO expected = getUserGrpDto();
        expected.setDescription( DESCRPTION_WITH_GREATER_THEN_MAX );

        manager.createUserGroup( SUPER_USER_ID, expected );

    }

    /**
     * Should Get Null In Create Group When Null Object Is Given As Input.
     */
    @Test
    public void shouldGetNullInCreateGroupWhenNullObjectIsGivenAsInput() {

        SuSUserGroupDTO expect = null;
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        SuSUserGroupDTO actual = manager.createUserGroup( SUPER_USER_ID, expect );
        Assert.assertEquals( actual, expect );

    }

    /**
     * **************************** updateUserGroup *****************************************.
     */
    /**
     * Should Successfully Update Group When Valid Group Id Is Given As Input
     */
    @Test( expected = NullPointerException.class )
    public void shouldSuccessfullyUpdateGroupWhenValidGroupIdIsGivenAsInput() {
        GroupEntity expectedEntity = getGroupEntity();
        expectedEntity.setUsers( getUsersSetWithTwoRecords() );
        SuSUserGroupDTO expect = getUserGrpDto();
        List< UUID > expectedList = new ArrayList<>();
        expectedList.add( ITEM_ID_SELECTED_ONE );
        expectedList.add( ITEM_ID_SELECTED_TWO );
        expectedList.add( ITEM_ID_SELECTED_THREE );
        EasyMock.expect(
                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( expectedList ).anyTimes();

        EasyMock.expect( dao.updateUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( GroupEntity.class ),
                EasyMock.anyObject() ) ).andReturn( expectedEntity ).anyTimes();

        EasyMock.expect( dao.getGroupAndUsersByGroupId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect( dao.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect(
                        userManager.getUsersByGroupEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( GroupEntity.class ) ) )
                .andReturn( new ArrayList<>( expectedEntity.getUsers() ) ).anyTimes();
        EasyMock.expect( userManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( getUserModel() ).anyTimes();
        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        EasyMock.expect( securityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillSecurityIdentity( expectedEntity ) ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        SuSUserGroupDTO actual = manager.updateUserGroup( SUPER_USER_ID, expect );
        Assert.assertEquals( actual, expect );

    }

    /**
     * Should Get Null In Update Group When Null Object Is Given As Input.
     */
    @Test
    public void shouldGetNullInUpdateGroupWhenNullObjectIsGivenAsInput() {

        SuSUserGroupDTO expect = null;
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        SuSUserGroupDTO actual = manager.updateUserGroup( SUPER_USER_ID, expect );
        Assert.assertEquals( actual, expect );

    }

    /**
     * **************************** deleteUserGroup *****************************************.
     */
    /**
     * Should Successfully Delete Group Entity When Valid Group Id Is Given As Input
     */
    @Test
    public void shouldSuccessfullyDeleteGroupEntityWhenValidGroupIdIsGivenAsInput() {
        GroupEntity expectedEntity = getGroupEntity();
        EasyMock.expect( dao.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect( dao.updateUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( GroupEntity.class ),
                EasyMock.anyObject() ) ).andReturn( expectedEntity ).anyTimes();
        EasyMock.expect(
                        groupRoleCommonDAO.getGroupRoleByGroupId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect(
                        userManager.getUsersByGroupEntity( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( GroupEntity.class ) ) )
                .andReturn( new ArrayList<>( expectedEntity.getUsers() ) ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        boolean actual = manager.deleteUserGroupBySelection( SUPER_USER_ID, GROUP_ID.toString(), MODE );
        Assert.assertTrue( actual );

    }

    /**
     * Should Throw Exception In Delete Group Entity When Group Id Has No Record In DB.
     */
    @Test
    public void shouldThrowExceptionInDeleteGroupEntityWhenGroupIdHasNoRecordInDB() {
        thrown.expect( SusException.class );
        thrown.expectMessage( ERROR_MSG_FOR_GRP_ID + GROUP_ID );
        EasyMock.expect( dao.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.deleteUserGroupBySelection( SUPER_USER_ID, GROUP_ID.toString(), MODE );
    }

    /**
     * **************************** readUserGroup *****************************************.
     */
    /**
     * Should Successfully Get Group Entity When Valid Group Id Is Given As Input
     */
    @Test
    public void shouldSuccessfullyGetGroupEntityWhenValidGroupIdIsGivenAsInput() {
        GroupEntity expect = getGroupEntity();
        EasyMock.expect( dao.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expect ).anyTimes();
        EasyMock.expect( userManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( getUserModel() ).anyTimes();

        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        EasyMock.expect( securityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillSecurityIdentity( expect ) ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        SuSUserGroupDTO actual = manager.readUserGroup( SUPER_USER_ID, GROUP_ID );
        assertsTest( actual, expect );

    }

    /**
     * Should Throw Illegal Args Exception In Get Group Entity When InValid GroupId Is Given As Input.
     */
    @Test
    public void shouldThrowIllegalArgsExceptionInGetGroupEntityWhenInValidGroupIdIsGivenAsInput() {
        thrown.expect( IllegalArgumentException.class );
        EasyMock.expect( dao.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();
        mockControl.replay();

        manager.readUserGroup( DEFAULT_USER_ID, UUID.fromString( INVALID_UUID ) );
    }

    /**
     * Should Throw Exception In Get Group Entity When No REcord Found Against Given Group Id.
     */
    @Test
    public void shouldThrowExceptionInGetGroupEntityWhenNoREcordFoundAgainstGivenGroupId() {
        thrown.expect( SusException.class );
        thrown.expectMessage( ERROR_MSG_FOR_GRP_ID + GROUP_ID );
        EasyMock.expect( dao.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        manager.readUserGroup( SUPER_USER_ID, GROUP_ID );
    }

    /**
     * Should Get Null Group Entity When GroupId Is Given Null As Input.
     */
    @Test
    public void shouldGetNullGroupEntityWhenGroupIdIsGivenNullAsInput() {
        SuSUserGroupDTO expect = null;
        EasyMock.expect( dao.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        SuSUserGroupDTO actual = manager.readUserGroup( SUPER_USER_ID, null );
        Assert.assertEquals( actual, expect );
    }

    /**
     * **************************** getUserGroupsList *****************************************.
     */

    /**
     * Should Successfully Get Group Entity List When Dao Return Valid List
     */
    @Test
    public void shouldSuccessfullyGetGroupEntityListWhenDaoReturnValidList() {
        GroupEntity expectEntity = getGroupEntity();

        List< GroupEntity > expect = new ArrayList<>();
        expect.add( expectEntity );
        FiltersDTO filtersDTO = populateFilterDTO();
        EasyMock.expect( dao.getAllFilteredRecords( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expect ).anyTimes();
        EasyMock.expect( userManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( getUserModel() ).anyTimes();

        EasyMock.expect( dao.getGroupAndUsersByGroupId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( expectEntity ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();

        mockControl.replay();

        FilteredResponse< SuSUserGroupDTO > actual = manager.getUserGroupsList( SUPER_USER_ID, filtersDTO );
        Assert.assertNotNull( actual );
        Assert.assertEquals( actual.getData().size(), expect.size() );
        assertsTest( actual.getData().get( FIRST_INDEX ), expect.get( FIRST_INDEX ) );

    }

    /**
     * Should Get Empty Group Entity List When Dao Return Null List.
     */
    @Test
    public void shouldGetEmptyGroupEntityListWhenDaoReturnNullList() {

        List< GroupEntity > expect = new ArrayList<>();
        FiltersDTO filtersDTO = populateFilterDTO();
        EasyMock.expect( dao.getAllFilteredRecords( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expect ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        FilteredResponse< SuSUserGroupDTO > actual = manager.getUserGroupsList( SUPER_USER_ID, filtersDTO );

        Assert.assertNotNull( actual );
        Assert.assertTrue( actual.getData().isEmpty() );
        Assert.assertEquals( actual.getData().size(), expect.size() );

    }

    /**
     * **************************** Selection manager *****************************************.
     */

    /**
     * Should return all the selected selections with pagination of given selection id.
     */
    @Test
    public void shouldReturnAllTheSelectedSelectionsWithPaginationOfGivenSelectionId() {
        FiltersDTO filtersDTO = populateFilterDTO();
        SelectionItemEntity selectionItemEntity = new SelectionItemEntity();
        selectionItemEntity.setId( GROUP_ID );
        selectionItemEntity.setItem( ITEM_ID_SELECTED_ONE.toString() );
        List< SelectionItemEntity > selectedUserIds = new ArrayList<>();
        selectedUserIds.add( selectionItemEntity );
        GroupEntity groupEntity = getGroupEntity();
        SuSUserGroupDTO sUserGroupDTO = getUserGrpDtoWithoutUser();
        List< SuSUserGroupDTO > userDTOList = new ArrayList<>();
        userDTOList.add( sUserGroupDTO );

        EasyMock.expect( selectionManager.getUserSelectionsBySelectionIds( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject() ) ).andReturn( selectedUserIds ).anyTimes();
        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( groupEntity ).anyTimes();
        mockControl.replay();
        FilteredResponse< SuSUserGroupDTO > expectedFilterResponse = PaginationUtil.constructFilteredResponse( filtersDTO,
                userDTOList );
        FilteredResponse< SuSUserGroupDTO > actualFilteredResponse = manager.getAllGroupSelections( SELECTION_ID.toString(),
                USER_ID.toString(), filtersDTO );
        Assert.assertEquals( actualFilteredResponse.getLength(), expectedFilterResponse.getLength() );
        Assert.assertEquals( actualFilteredResponse.getSearchId(), expectedFilterResponse.getSearchId() );
        Assert.assertEquals( actualFilteredResponse.getData(), expectedFilterResponse.getData() );
    }

    private SuSUserGroupDTO getUserGrpDtoWithoutUser() {
        SuSUserGroupDTO susGroupDto = new SuSUserGroupDTO();
        susGroupDto.setId( GROUP_ID );
        susGroupDto.setName( NAME_FIELD );
        susGroupDto.setDescription( DESCRIPTION_FIELD );
        susGroupDto.setStatus( ACTIVE );
        susGroupDto.setSelectionId( SELECTION_ID.toString() );
        susGroupDto.setUsersCount( Collections.emptyList().size() );
        return susGroupDto;
    }

    /**
     * ************************ Helper Methods ****************************.
     *
     * @param expected
     *            the expected
     * @param actual
     *            the actual
     */

    /**
     * Methods to check all asserts
     *
     * @param expected
     * @param actual
     */
    private void assertsTest( SuSUserGroupDTO expected, GroupEntity actual ) {
        Assert.assertEquals( expected.getId(), actual.getId() );

        Assert.assertEquals( expected.getName(), actual.getName() );
        Assert.assertEquals( expected.getDescription(), actual.getDescription() );
        Assert.assertEquals( expected.getCreatedOn(), actual.getCreatedOn() );
        Assert.assertEquals( expected.getUpdatedOn(), actual.getModifiedOn() );
        Assert.assertTrue( actual.getStatus() );
        Assert.assertEquals( ACTIVE, expected.getStatus() );

    }

    /**
     * Get Group Entity.
     *
     * @return the group entity
     */
    private GroupEntity getGroupEntity() {
        groupEntity.setId( GROUP_ID );
        groupEntity.setName( NAME_FIELD );
        groupEntity.setDescription( DESCRIPTION_FIELD );
        groupEntity.setStatus( ACTIVE_GRP );
        groupEntity.setSelectionId( SELECTION_ID.toString() );
        groupEntity.setUsers( Collections.emptySet() );
        return groupEntity;
    }

    /**
     * Gets the user model.
     *
     * @return the user model
     */
    private UserDTO getUserModel() {
        UserDTO userEntity = new UserDTO();
        userEntity.setUserUid( UID );
        userEntity.setName( USER_NAME );
        userEntity.setId( USER_ID.toString() );
        return userEntity;
    }

    /**
     * Gets the user grp dto.
     *
     * @return the user grp dto
     */
    private SuSUserGroupDTO getUserGrpDto() {
        susGroupDto.setId( GROUP_ID );
        susGroupDto.setName( NAME_FIELD );
        susGroupDto.setDescription( DESCRIPTION_FIELD );
        susGroupDto.setStatus( ACTIVE );
        susGroupDto.setSelectionId( SELECTION_ID.toString() );
        List< UserDTO > users = new ArrayList<>();
        users.add( getUserModel() );
        susGroupDto.setUsers( users );
        return susGroupDto;
    }

    /**
     * Populate Filter DTo.
     *
     * @return the filters DTO
     */
    private FiltersDTO populateFilterDTO() {
        FiltersDTO filterDTO = new FiltersDTO();
        filterDTO.setDraw( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setLength( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setStart( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setFilteredRecords( 10L );
        List< Object > items = new ArrayList<>();
        items.add( ITEM_ID_SELECTED_ONE );
        items.add( ITEM_ID_SELECTED_TWO );
        items.add( ITEM_ID_SELECTED_THREE );
        filterDTO.setItems( items );
        return filterDTO;
    }

    /**
     * Fill security identity related to user group.
     *
     * @param groupEntity
     *         the group entity
     *
     * @return the acl security identity entity
     */
    private AclSecurityIdentityEntity fillSecurityIdentity( GroupEntity groupEntity ) {
        AclSecurityIdentityEntity securityIdentityEntity = new AclSecurityIdentityEntity();
        securityIdentityEntity.setId( UUID.randomUUID() );
        securityIdentityEntity.setSid( groupEntity.getId() );
        securityIdentityEntity.setPrinciple( false );
        return securityIdentityEntity;
    }

    /**
     * Gets the users set with two records.
     *
     * @return the users set with two records
     */
    private Set< UserEntity > getUsersSetWithTwoRecords() {
        Set< UserEntity > set = new HashSet<>();
        set.add( getUserEntity() );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( USER_UUID_TEST );
        userEntity.setFirstName( NAME_FIELD );
        userEntity.setSurName( NAME_FIELD );

        set.add( userEntity );
        return set;
    }

    /**
     * Prepare user entity.
     *
     * @return the user entity
     */
    private UserEntity getUserEntity() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId( USER_UUID );
        userEntity.setFirstName( NAME_FIELD );
        userEntity.setSurName( NAME_FIELD );

        return userEntity;
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
     * Prepare directory entity.
     *
     * @return the su S user directory entity
     */
    private SuSUserDirectoryEntity prepareDirectoryEntity() {

        SuSUserDirectoryEntity directoryEntity = new SuSUserDirectoryEntity();
        directoryEntity.setId( DIRECTORY_ID );
        directoryEntity.setType( ConstantsUserDirectories.INTERNAL_DIRECTORY );
        directoryEntity.setStatus( ACCOUNT_STATUS_ACTIVE );

        return directoryEntity;
    }

}
