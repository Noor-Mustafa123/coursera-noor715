package de.soco.software.simuspace.suscore.role.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.permissions.constants.PermissionManageForm;
import de.soco.software.simuspace.suscore.permissions.model.PermissionDTO;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;
import de.soco.software.simuspace.suscore.role.manager.impl.RoleManagerImpl;
import de.soco.software.simuspace.suscore.role.model.RoleDTO;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * This Class provides the Roles API implementation test cases for managing roles and assigning permission to role.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class RoleServiceImplTest {

    /**
     * The Constant INDEX.
     */
    private static final int INDEX = 0;

    /**
     * The Constant LICENSE_CREATED_DATE.
     */
    private static final String LICENSE_CREATED_DATE = "2017-12-10T12:26:27+05:00";

    /**
     * The Constant DISBALE.
     */
    private static final int DISBALE = 0;

    /**
     * The Constant LICENSE_VIEW_MASK.
     */
    private static final int LICENSE_VIEW_MASK = 2;

    /**
     * The Constant LICENSE_VIEW.
     */
    private static final String LICENSE_VIEW = "View";

    /**
     * The Constant UID.
     */
    private static final String UID = "sces120";

    /**
     * The Constant UID.
     */
    private static final String USER_NAME = "Test_User";

    /**
     * Dummy USER_ID for test Cases.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant ACTIVE.
     */
    public static final String ACTIVE = "Active";

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
     * The service.
     */
    private RoleServiceImpl service;

    /**
     * The role manager impl.
     */
    private RoleManagerImpl roleManagerImpl;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * Dummy susGroupDTo object for test data.
     */
    private SuSUserGroupDTO susGroupDto = new SuSUserGroupDTO();

    /**
     * The Constant CHECK_BOX_STATE_STR.
     */
    private static final String CHECK_BOX_STATE_STR = "{\"permissionDTOs-View\":\"1\"}";

    /**
     * The role model.
     */
    private RoleDTO roleModel = new RoleDTO();

    /**
     * The Constant DESCRIPTION_FIELD.
     */
    private static final String DESCRIPTION_FIELD = "Description";

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * Dummy GROUP_ID for test Cases.
     */
    private static final UUID GROUP_ID = UUID.randomUUID();

    /**
     * The Constant OBJECT_VIEW_ID.
     */
    private static final String OBJECT_VIEW_ID = UUID.randomUUID().toString();

    /**
     * The Constant OBJECT_VIEW_PAYLOAD.
     */
    private static final String OBJECT_VIEW_PAYLOAD = "{\"id\":\"" + OBJECT_VIEW_ID
            + "\",\"name\": \"view-1\",\"defaultView\": false,\"settings\": {\"draw\": 3,\"start\": 0,\"length\": 25,\"search\": \"search test\",\"columns\": [{\"name\": \"groups.name\",\"visible\": true,\"dir\": null,\"filters\": [ {\"operator\": \"Contains\",\"value\": \"beta\",\"condition\": \"AND\"},{ \"operator\": \"Contains\",\"value\": \"delta\",\"condition\": \"AND\"},{\"operator\": \"Contains\",\"value\": \"gamma\",\"condition\": \"AND\"}],\"reorder\": 3}] }}";

    /**
     * The Constant ROLE_CREATE_PAYLOAD.
     */
    private static final String ROLE_CREATE_PAYLOAD = "{\"name\":\"fuyg\",\"description\":\"gh\",\"status\":\"Active\",\"groups\":\"14259282-3b6d-4f36-9e21-0787a62c336b\"}";

    /**
     * The Constant OBJECT_VIEW_INVALID_PAYLOAD.
     */
    private static final String OBJECT_VIEW_INVALID_PAYLOAD = "{\"id\": 12355,\"name\": \"view-1\",\"defaultView\": false,\"setting\": {\"draw\": 3,\"start\": 0,\"length\": 25,\"search\": \"search test\",\"columns\": [{\"name\": \"groups.name\",\"visible\": true,\"dir\": null,\"filters\": [ {\"operator\": \"Contains\",\"value\": \"beta\",\"condition\": \"AND\"},{ \"operator\": \"Contains\",\"value\": \"delta\",\"condition\": \"AND\"},{\"operator\": \"Contains\",\"value\": \"gamma\",\"condition\": \"AND\"}],\"reorder\": 3}] }}";

    /**
     * The Constant ROLE_PAYLOAD_INVALID.
     */
    private static final String ROLE_PAYLOAD_INVALID = null;

    /**
     * The Constant ROLE.
     */
    private static final String ROLE = "Role";

    /**
     * The Constant NAME.
     */
    private static final String NAME = "name";

    /**
     * The Constant TYPE.
     */
    private static final String TYPE = "text";

    /**
     * The Constant VALUE.
     */
    private static final String VALUE = null;

    /**
     * The Constant HELP.
     */
    private static final String HELP = null;

    /**
     * The Constant PLACE_HOLDER.
     */
    private static final String PLACE_HOLDER = null;

    /**
     * The Constant ROLES_VIEW_FILTER_PAYLOAD.
     */
    private static final String ROLES_VIEW_FILTER_PAYLOAD = "{\"draw\":1,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"id\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"description\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"status\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3},{\"name\":\"groups.name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":4},{\"name\":\"createdOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":5},{\"name\":\"modifiedOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":6}]}";

    /**
     * The Constant CONTEXT_ROUTER_JSON.
     */
    private static final String CONTEXT_ROUTER_JSON = "{\"items\":[\"19ffd34d-1e94-4484-92d8-6d6081bb0f91\"]}";

    /**
     * The Constant RESOURCE_ID.
     */
    private static final UUID RESOURCE_ID = UUID.randomUUID();

    /**
     * The Constant ROLE_ID.
     */
    private static final UUID ROLE_ID = UUID.randomUUID();

    /**
     * The Constant SUPER_USER_ID.
     */
    public static final String SUPER_USER_ID = "1e98a77c-a0be-4983-9137-d9a8acd0ea8b";

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * The Constant DIRECTORY_ID.
     */
    private static final UUID DIRECTORY_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92459ddc" );

    /**
     * The active account status.
     */

    public static final boolean ACCOUNT_STATUS_ACTIVE = true;

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        service = new RoleServiceImpl();
        roleManagerImpl = mockControl.createMock( RoleManagerImpl.class );
        objectViewManager = mockControl.createMock( ObjectViewManager.class );
        service.setRoleManager( roleManagerImpl );
        userManager = mockControl.createMock( UserManager.class );

    }

    /**
     * Should return all permission related to features when exact role manage is called.
     */
    @Test
    public void shouldReturnAllPermissionRelatedToFeaturesWhenExactRoleManageIsCalled() {
        PermissionManageForm resourceAccessControlDTO = getResourceAccessControl();
        FiltersDTO filtersDTO = populateFilterDTO();
        List< PermissionManageForm > expectedResourceAccessControlDTO = new ArrayList<>();
        expectedResourceAccessControlDTO.add( resourceAccessControlDTO );
        FilteredResponse< PermissionManageForm > expectedFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO,
                expectedResourceAccessControlDTO );

        UUID roleId = UUID.randomUUID();
        EasyMock.expect(
                        roleManagerImpl.getAllPermissionsByRoleId( EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedFilteredResponse ).anyTimes();
        mockControl.replay();

        Response response = service.getAllPermissionsByRoleId( JsonUtils.toJson( filtersDTO, FiltersDTO.class ), roleId );

        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );

        String resourceAccessControlStr = JsonUtils.toJson( ( LinkedHashMap< Object, Object > ) susResponseDTO.getData() );
        FilteredResponse< ResourceAccessControlDTO > filteredResponse = JsonUtils.jsonToObject( resourceAccessControlStr,
                FilteredResponse.class );

        List< ResourceAccessControlDTO > actual = JsonUtils.jsonToList( JsonUtils.toJson( filteredResponse.getData() ),
                ResourceAccessControlDTO.class );
        Assert.assertNotNull( actual );
        Assert.assertEquals( expectedResourceAccessControlDTO.get( INDEX ).getName(), actual.get( INDEX ).getName() );
    }

    /**
     * Should fail if permission pay load is null.
     */
    @Test
    public void shouldFailIfPermissionPayLoadIsNull() {
        Response response = service.getAllPermissionsByRoleId( null, ROLE_ID );
        SusResponseDTO expected = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertFalse( expected.getSuccess() );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
        Assert.assertEquals( expected.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
        Assert.assertNull( expected.getData() );
    }

    /**
     * Shoul fail if permit permission to role having permission pay load null.
     */
    @Test
    public void shoulFailIfPermitPermissionToRoleHavingPermissionPayLoadNull() {
        Response response = service.permitPermissionToRole( null, ROLE_ID, RESOURCE_ID );
        SusResponseDTO expected = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertFalse( expected.getSuccess() );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
        Assert.assertEquals( expected.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
        Assert.assertNull( expected.getData() );
    }

    /**
     * Should allow permission binded with resource when permission allowed to feature.
     */
    @Test
    public void shouldAllowPermissionBindedWithResourceWhenPermissionAllowedToFeature() {
        UUID roleId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        EasyMock.expect( roleManagerImpl.permitPermissionToRole( EasyMock.anyString(), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( true ).anyTimes();
        mockControl.replay();

        Response response = service.permitPermissionToRole( CHECK_BOX_STATE_STR, roleId, resourceId );
        SusResponseDTO expectedSusResponseDto = fillExpectedResponseDTO( true, "Permission applied successfully", null, null );
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
        SusResponseDTO actualSusResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( expectedSusResponseDto.getData(), actualSusResponseDTO.getData() );

    }

    /**
     * Should permission applied interruption.
     */
    @Test
    public void shouldPermissionAppliedInterruption() {
        UUID roleId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        EasyMock.expect( roleManagerImpl.permitPermissionToRole( EasyMock.anyString(), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean() ) ).andReturn( false ).anyTimes();
        mockControl.replay();

        Response response = service.permitPermissionToRole( CHECK_BOX_STATE_STR, roleId, resourceId );
        SusResponseDTO expectedSusResponseDto = fillExpectedResponseDTO( false, null, "Permission not applied successfully", null );
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
        SusResponseDTO actualSusResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( expectedSusResponseDto.getData(), actualSusResponseDTO.getData() );
    }

    /**
     * Should create role successfully when valid role payload is given.
     *
     * @throws ParseException
     */
    @Test
    public void shouldCreateRoleSuccessfullyWhenValidRolePayloadIsGiven() throws ParseException {
        roleModel = getRoleModel();
        EasyMock.expect( roleManagerImpl.createRole( EasyMock.anyString(), EasyMock.anyObject(), EasyMock.anyBoolean() ) )
                .andReturn( roleModel );
        mockControl.replay();
        Response expected = service.createRole( ROLE_CREATE_PAYLOAD );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
    }

    /**
     * Should get bad request response when payload is invalid for create role.
     *
     * @throws ParseException
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForCreateRole() throws ParseException {
        final Response response = service.createRole( OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * Should get internel server error response when payload is invalid for update role.
     */
    @Test
    public void shouldGetInternelServerErrorResponseWhenPayloadIsInvalidForUpdateRole() {
        final Response response = service.updateRole( ROLE_PAYLOAD_INVALID );
        Assert.assertEquals( HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatus() );
    }

    /**
     * Should update role successfully when valid role payload is given.
     */
    @Test
    public void shouldUpdateRoleSuccessfullyWhenValidRolePayloadIsGiven() {
        roleModel = getRoleModel();
        EasyMock.expect( roleManagerImpl.updateRole( EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( roleModel );
        mockControl.replay();
        Response expected = service.updateRole( ROLE_CREATE_PAYLOAD );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
    }

    /**
     * Should return true when valid role is going to delete.
     */
    @Test
    public void shouldReturnTrueWhenValidRoleIsGoingToDelete() {
        UUID roleId = UUID.randomUUID();
        EasyMock.expect( roleManagerImpl.deleteRoleBySelection( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( true ).anyTimes();
        mockControl.replay();
        Response expected = service.deleteRole( roleId.toString(), "single" );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
    }

    /**
     * Should return valid edit form.
     */
    @Test
    public void ShouldReturnValidEditForm() {
        List< UIFormItem > uiFormItems = new ArrayList<>();
        uiFormItems.add( getUIFormItem( ROLE, NAME, TYPE, VALUE, HELP, PLACE_HOLDER ) );
        // EasyMock.expect( roleManagerImpl.editRoleForm( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) ).andReturn( uiFormItems
        // )
        // .anyTimes();
        mockControl.replay();
        Response expected = service.editRoleForm( EasyMock.anyObject( UUID.class ) );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
    }

    /**
     * Should return valid create form.
     */
    @Test
    public void ShouldReturnValidCreateForm() {
        List< UIFormItem > uiFormItems = new ArrayList<>();
        uiFormItems.add( getUIFormItem( ROLE, NAME, TYPE, VALUE, HELP, PLACE_HOLDER ) );
        // EasyMock.expect( roleManagerImpl.createRoleForm( SUPER_USER_ID ) ).andReturn( uiFormItems ).anyTimes();
        mockControl.replay();
        Response expected = service.createRoleForm();
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
    }

    /**
     * Should return valid role list.
     */
    @Test
    public void shouldReturnValidRoleList() {
        roleModel = getRoleModel();
        FiltersDTO filtersDTO = populateFilterDTO();
        List< RoleDTO > roleDTOs = new ArrayList<>();
        roleDTOs.add( roleModel );

        FilteredResponse< RoleDTO > expectedFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO, roleDTOs );

        EasyMock.expect( roleManagerImpl.getRoleList( EasyMock.anyString(), new FiltersDTO() ) ).andReturn( expectedFilteredResponse )
                .anyTimes();
        mockControl.replay();
        Response expected = service.getRoleList( ROLES_VIEW_FILTER_PAYLOAD );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        String rolesStr = JsonUtils.toJson( ( LinkedHashMap< Object, Object > ) susResponseDTO.getData() );
        FilteredResponse< RoleDTO > filteredResponse = JsonUtils.jsonToObject( rolesStr, FilteredResponse.class );
        Assert.assertNotNull( filteredResponse );
    }

    /**
     * Should fail if null json provide for role list.
     */
    @Test
    public void shouldFailIfNullJsonProvideForRoleList() {
        Response response = service.getRoleList( null );
        SusResponseDTO expected = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertFalse( expected.getSuccess() );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
        Assert.assertEquals( expected.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
        Assert.assertNull( expected.getData() );
    }

    /**
     * Should prepare table UI for role when permission when permission managed.
     */
    @Test
    public void shouldPrepareTableUIForRoleWhenPermissionWhenPermissionManaged() {
        TableUI ui = getFilledColumns();
        UUID roleId = UUID.randomUUID();
        EasyMock.expect( roleManagerImpl.managePermissionTableUI( EasyMock.anyString() ) ).andReturn( ui.getColumns() ).anyTimes();
        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( new ArrayList<>() ).anyTimes();
        mockControl.replay();
        Response expected = service.manageRolePermissionUI( roleId );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO actual = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actual.getData() );
        String tableUI = JsonUtils.convertMapToString( ( Map< String, String > ) actual.getData() );
        TableUI actualTableUI = JsonUtils.jsonToObject( tableUI, TableUI.class );
        Assert.assertEquals( ui.getColumns().get( INDEX ).getData(), actualTableUI.getColumns().get( INDEX ).getData() );
        Assert.assertEquals( ui.getColumns().get( INDEX ).getFilter(), actualTableUI.getColumns().get( INDEX ).getFilter() );
        Assert.assertEquals( ui.getColumns().get( INDEX ).getName(), actualTableUI.getColumns().get( INDEX ).getName() );
        Assert.assertEquals( ui.getColumns().get( INDEX ).getTitle(), actualTableUI.getColumns().get( INDEX ).getTitle() );
        Assert.assertEquals( ui.getColumns().get( INDEX ).getClass(), actualTableUI.getColumns().get( INDEX ).getClass() );
        Assert.assertEquals( ui.getColumns().get( INDEX ).getRenderer().getData(),
                actualTableUI.getColumns().get( INDEX ).getRenderer().getData() );
        Assert.assertEquals( ui.getColumns().get( INDEX ).getRenderer().getSeparator(),
                actualTableUI.getColumns().get( INDEX ).getRenderer().getSeparator() );
        Assert.assertEquals( ui.getColumns().get( INDEX ).getRenderer().getType(),
                actualTableUI.getColumns().get( INDEX ).getRenderer().getType() );
    }

    /**
     * Should return context router for role when edit role is called.
     */
    @Test
    public void shouldReturnContextRouterForRoleWhenEditRoleIsCalled() {
        List< ContextMenuItem > contextMenuItems = new ArrayList<>();
        ContextMenuItem contextMenuItem = new ContextMenuItem();
        contextMenuItem.setUrl( "edit/system/permissions/role/19ffd34d-1e94-4484-92d8-6d6081bb0f91" );
        contextMenuItem.setTitle( MessageBundleFactory.getMessage( "4100014x4" ) );
        contextMenuItem.setIcon( "fa fa-edit" );
        contextMenuItems.add( contextMenuItem );

        EasyMock.expect( roleManagerImpl.getContextMenu( EasyMock.anyObject() ) ).andReturn( contextMenuItems ).anyTimes();
        mockControl.replay();
        Response expected = service.getContextRouter( CONTEXT_ROUTER_JSON );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
    }

    /**
     * Should fail if null json provided for context router for role.
     */
    @Test
    public void shouldFailIfNullJsonProvidedForContextRouterForRole() {
        Response response = service.getContextRouter( null );
        SusResponseDTO expected = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertFalse( expected.getSuccess() );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
        Assert.assertEquals( expected.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
        Assert.assertNull( expected.getData() );
    }

    /**
     * Should return context router for manage.
     */
    @Test
    public void shouldReturnContextRouterForManage() {
        List< ContextMenuItem > contextMenuItems = new ArrayList<>();
        ContextMenuItem contextMenuItem = new ContextMenuItem();
        contextMenuItem.setUrl( "manage/system/permissions/resource/manage/role/19ffd34d-1e94-4484-92d8-6d6081bb0f91" );
        contextMenuItem.setTitle( MessageBundleFactory.getMessage( "4100006x4" ) );
        contextMenuItem.setIcon( "fa fa-edit" );
        contextMenuItems.add( contextMenuItem );

        EasyMock.expect( roleManagerImpl.getContextMenu( EasyMock.anyObject() ) ).andReturn( contextMenuItems ).anyTimes();
        mockControl.replay();
        Response expected = service.getContextRouter( CONTEXT_ROUTER_JSON );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
    }

    /**
     * Should get bad request response when payload is invalid for saving object view.
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForSavingObjectView() {

        final Response response = service.saveView( OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );

    }

    /**
     * Should update view successfully when valid object view payload is given.
     */
    @Test
    public void shouldUpdateViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = service.updateView( OBJECT_VIEW_ID, OBJECT_VIEW_PAYLOAD );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( expected.getName(), objectViewDTO.getName() );
        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
    }

    /**
     * Should get bad request response when payload is invalid for updating object view.
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForUpdatingObjectView() {

        final Response response = service.updateView( OBJECT_VIEW_ID, OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * Should get all views successfully when valid object view key is provided.
     */
    @Test
    public void shouldGetAllViewsSuccessfullyWhenValidObjectViewKeyIsProvided() {

        ObjectViewDTO objectViewDTO = prepareObjectViewDTO();
        List< ObjectViewDTO > expectedResponse = Arrays.asList( objectViewDTO );
        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expectedResponse );

        mockControl.replay();
        Response actual = service.getAllViews();
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        List< ObjectViewDTO > list = ( List< ObjectViewDTO > ) JsonUtils.jsonToList( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( objectViewDTO.getName(), list.get( INDEX ).getName() );
        Assert.assertEquals( objectViewDTO.getObjectViewJson(), list.get( INDEX ).getObjectViewJson() );
        Assert.assertEquals( objectViewDTO.getObjectViewKey(), list.get( INDEX ).getObjectViewKey() );
    }

    /**
     * Should delete view successfully when valid view id provided.
     */
    @Test
    public void shouldDeleteViewSuccessfullyWhenValidViewIdProvided() {

        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( true );
        mockControl.replay();
        Response actual = service.deleteView( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ) );
    }

    /**
     * Should not delete view successfully when in valid view id provided.
     */
    @Test
    public void shouldNotDeleteViewSuccessfullyWhenInValidViewIdProvided() {

        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( false );
        mockControl.replay();
        Response actual = service.deleteView( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
    }

    /**
     * Should get view successfully when valid view id is given.
     */
    @Test
    public void shouldGetViewSuccessfullyWhenValidViewIdIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.getObjectViewById( EasyMock.anyObject( UUID.class ) ) ).andReturn( expected );
        mockControl.replay();
        Response actual = service.getView( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( expected.getName(), objectViewDTO.getName() );
        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
    }

    /**
     * Should save view successfully when valid object view payload is given.
     */
    @Test
    public void shouldSaveViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = service.saveView( OBJECT_VIEW_PAYLOAD );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( expected.getName(), objectViewDTO.getName() );
        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
    }

    /**
     * Should get bad request response when payload is invalid for saving manage role object view.
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForSavingManageRoleObjectView() {

        final Response response = service.saveManageRoleView( ROLE_ID.toString(), OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );

    }

    /**
     * Should update view successfully when valid manage role object view payload is given.
     */
    @Test
    public void shouldUpdateViewSuccessfullyWhenValidManageRoleObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = service.updateManageRoleView( ROLE_ID.toString(), OBJECT_VIEW_ID, OBJECT_VIEW_PAYLOAD );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( expected.getName(), objectViewDTO.getName() );
        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
    }

    /**
     * Should get bad request response when payload is invalid for updating manage role object view.
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForUpdatingManageRoleObjectView() {

        final Response response = service.updateManageRoleView( ROLE_ID.toString(), OBJECT_VIEW_ID, OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * Should not delete manage role view successfully when in valid view id provided.
     */
    @Test
    public void shouldNotDeleteManageRoleViewSuccessfullyWhenInValidViewIdProvided() {

        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( false );
        mockControl.replay();
        Response actual = service.deleteManageRoleView( ROLE_ID.toString(), OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
    }

    /**
     * Should save manage role view successfully when valid object view payload is given.
     */
    @Test
    public void shouldSaveManageRoleViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = service.saveManageRoleView( ROLE_ID.toString(), OBJECT_VIEW_PAYLOAD );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );

        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( expected.getName(), objectViewDTO.getName() );
        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
    }

    /**
     * Should get all views successfully when valid manage role object view key is provided.
     */
    @Test
    public void shouldGetAllViewsSuccessfullyWhenValidManageRoleObjectViewKeyIsProvided() {

        ObjectViewDTO objectViewDTO = prepareObjectViewDTO();
        List< ObjectViewDTO > expectedResponse = Arrays.asList( objectViewDTO );
        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expectedResponse ).anyTimes();

        mockControl.replay();
        Response actual = service.getAllManageRoleViews( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        List< ObjectViewDTO > list = ( List< ObjectViewDTO > ) JsonUtils.jsonToList( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( objectViewDTO.getName(), list.get( INDEX ).getName() );
        Assert.assertEquals( objectViewDTO.getObjectViewJson(), list.get( INDEX ).getObjectViewJson() );
        Assert.assertEquals( objectViewDTO.getObjectViewKey(), list.get( INDEX ).getObjectViewKey() );
    }

    /**
     * Should delete manage role view successfully when valid view id provided.
     */
    @Test
    public void shouldDeleteManageRoleViewSuccessfullyWhenValidViewIdProvided() {

        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( true ).anyTimes();
        mockControl.replay();
        Response actual = service.deleteManageRoleView( ROLE_ID.toString(), OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getData(), MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ) );
    }

    /**
     * Should update view as default successfully when valid view id is given.
     */
    @Test
    public void shouldUpdateViewAsDefaultSuccessfullyWhenValidViewIdIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = service.setViewAsDefault( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
    }

    /**
     * Should update manage role view as default successfully when valid view id is given.
     */
    @Test
    public void shouldUpdateManageRoleViewAsDefaultSuccessfullyWhenValidViewIdIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( roleManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = service.setManageRoleViewAsDefault( ROLE_ID.toString(), OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
    }

    /**
     * Gets the UI form item.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param type
     *         the type
     * @param value
     *         the value
     * @param help
     *         the help
     * @param placeHolder
     *         the place holder
     *
     * @return the UI form item
     */
    private UIFormItem getUIFormItem( String label, String name, String type, String value, String help, String placeHolder ) {
        UIFormItem uiFormItem = GUIUtils.createFormItem();
        uiFormItem.setLabel( label );
        uiFormItem.setName( name );
        uiFormItem.setType( type );
        uiFormItem.setValue( value );
        uiFormItem.setHelp( help );
        uiFormItem.setPlaceHolder( placeHolder );
        return null;
    }

    /**
     * Gets the role model.
     *
     * @return the role model
     */
    private RoleDTO getRoleModel() {
        roleModel.setId( ROLE_ID );
        roleModel.setName( NAME_FIELD );
        roleModel.setDescription( DESCRIPTION_FIELD );
        roleModel.setStatus( ACTIVE );
        List< SuSUserGroupDTO > suSUserGroupDTOs = new ArrayList<>();
        suSUserGroupDTOs.add( getUserGrpDto() );
        roleModel.setGroups( suSUserGroupDTOs );
        return roleModel;
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
        List< UserDTO > users = new ArrayList<>();
        users.add( getUserModel() );
        susGroupDto.setUsers( users );
        return susGroupDto;
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
     * Fill expected response DTO.
     *
     * @param success
     *         the success
     * @param data
     *         the data
     * @param messageStr
     *         the message str
     * @param messageType
     *         the message type
     *
     * @return the sus response DTO
     */
    private SusResponseDTO fillExpectedResponseDTO( boolean success, Object data, String messageStr, String messageType ) {
        Message message = new Message( messageType, messageStr );
        SusResponseDTO susResponseDTO = new SusResponseDTO( success, message, data );
        return susResponseDTO;
    }

    /**
     * Populates filter DTO.
     *
     * @return the filters DTO
     */
    private FiltersDTO populateFilterDTO() {
        FiltersDTO filterDTO = new FiltersDTO();
        filterDTO.setDraw( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setLength( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setStart( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setFilteredRecords( 1L );
        return filterDTO;
    }

    /**
     * Gets the resource access control.
     *
     * @return the resource access control
     */
    private PermissionManageForm getResourceAccessControl() {
        PermissionManageForm resourceAccessControlDTO = new PermissionManageForm();
        resourceAccessControlDTO.setName( "License" );
        List< PermissionDTO > permissionDTOs = new ArrayList<>();
        permissionDTOs.add( getFillPermissionDTO() );
        return resourceAccessControlDTO;
    }

    /**
     * Gets the fill permission DTO.
     *
     * @return the fill permission DTO
     */
    private PermissionDTO getFillPermissionDTO() {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setValue( DISBALE );
        permissionDTO.setManage( true );
        permissionDTO.setMatrixKey( LICENSE_VIEW_MASK );
        permissionDTO.setMatrexValue( LICENSE_VIEW );
        return permissionDTO;
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
     * Prepare object view DTO.
     *
     * @return the object view DTO
     */
    private ObjectViewDTO prepareObjectViewDTO() {
        ObjectViewDTO objectViewDTO = new ObjectViewDTO();
        objectViewDTO.setName( NAME_FIELD );
        objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.ROLE_TABLE_KEY );
        objectViewDTO.setObjectViewJson( OBJECT_VIEW_PAYLOAD );
        return objectViewDTO;
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
