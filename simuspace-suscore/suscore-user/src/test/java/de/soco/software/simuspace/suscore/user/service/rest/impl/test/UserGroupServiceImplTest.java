package de.soco.software.simuspace.suscore.user.service.rest.impl.test;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.user.manager.UserGroupManager;
import de.soco.software.simuspace.suscore.user.service.rest.impl.UserGroupServiceImpl;

/**
 * Test Cases for UserGroupServiceImpl Class.
 *
 * @author Nosheen.Sharif
 */
public class UserGroupServiceImplTest {

    /**
     * Mocking reference for manager.
     */
    private UserGroupManager manager;

    /**
     * Mocking reference for service.
     */
    private UserGroupServiceImpl service;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * The Constant ID_FIELD.
     */
    private static final UUID ID_FIELD = UUID.randomUUID();

    /**
     * The Constant INVALID_UUID.
     */
    private static final String INVALID_UUID = "as-bh344-udh0";

    /**
     * The Constant DESCRIPTION_FIELD.
     */
    private static final String DESCRIPTION_FIELD = "Description";

    /**
     * The Constant USER_GROUP_PAYLOAD_ONE.
     */
    private static final String USER_GROUP_PAYLOAD_ONE = "{\"name\":\"name\",\"description\":\"description\",\"status\":\"Active\",\"users\":\"99aba070-5cda-4a25-a0bf-95a4386a7fcd\"}";

    /**
     * The Constant USER_GROUP_INVALID_PAYLOAD.
     */
    private static final String USER_GROUP_INVALID_PAYLOAD = "{\"name\":\"Name\",\"description\":\"Description\",\"status\":1\"}\"}";

    /**
     * The Constant OBJECT_VIEW_ID.
     */
    private static final String OBJECT_VIEW_ID = UUID.randomUUID().toString();

    /**
     * The Constant SELECTION_ID.
     */
    public static final UUID SELECTION_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92259ddc" );

    /**
     * The Constant OBJECT_VIEW_PAYLOAD.
     */
    private static final String OBJECT_VIEW_PAYLOAD = "{\"id\":\"" + OBJECT_VIEW_ID
            + "\",\"name\": \"view-1\",\"defaultView\": false,\"settings\": {\"draw\": 3,\"start\": 0,\"length\": 25,\"search\": \"search test\",\"columns\": [{\"name\": \"groups.name\",\"visible\": true,\"dir\": null,\"filters\": [ {\"operator\": \"Contains\",\"value\": \"beta\",\"condition\": \"AND\"},{ \"operator\": \"Contains\",\"value\": \"delta\",\"condition\": \"AND\"},{\"operator\": \"Contains\",\"value\": \"gamma\",\"condition\": \"AND\"}],\"reorder\": 3}] }}";

    /**
     * The Constant OBJECT_VIEW_INVALID_PAYLOAD.
     */
    private static final String OBJECT_VIEW_INVALID_PAYLOAD = "{\"id\": 12355,\"name\": \"view-1\",\"defaultView\": false,\"setting\": {\"draw\": 3,\"start\": 0,\"length\": 25,\"search\": \"search test\",\"columns\": [{\"name\": \"groups.name\",\"visible\": true,\"dir\": null,\"filters\": [ {\"operator\": \"Contains\",\"value\": \"beta\",\"condition\": \"AND\"},{ \"operator\": \"Contains\",\"value\": \"delta\",\"condition\": \"AND\"},{\"operator\": \"Contains\",\"value\": \"gamma\",\"condition\": \"AND\"}],\"reorder\": 3}] }}";

    /**
     * The Constant for list index.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant ACTIVE.
     */
    public static final String ACTIVE = "Active";

    /**
     * The Constant SINGLE.
     */
    private static final String MODE = "single";

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
     * Dummy susGroupDTo object for test data.
     */
    private SuSUserGroupDTO susGroupDto = new SuSUserGroupDTO();

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
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        service = new UserGroupServiceImpl();
        manager = mockControl.createMock( UserGroupManager.class );
        objectViewManager = mockControl.createMock( ObjectViewManager.class );
        service.setUserGroupManager( manager );
    }

    /**
     * ************************** createUserGroup **********************************.
     */

    /**
     * Should Successfully Create User Group When Valid Json Is Given As Parameter
     */
    @Test
    public void shouldSuccessfullyCreateUserGroupWhenValidJsonIsGivenAsParameter() {
        SuSUserGroupDTO actual = getFilledUserGrpDto();
        EasyMock.expect( manager.createUserGroup( EasyMock.anyString(), EasyMock.anyObject( SuSUserGroupDTO.class ) ) ).andReturn( actual )
                .anyTimes();
        mockControl.replay();
        Response expected = service.createUserGroup( USER_GROUP_PAYLOAD_ONE );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO expectedData = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( expectedData.getData() );
        String userGroupStr = JsonUtils.convertMapToString( ( Map< String, String > ) expectedData.getData() );
        JsonUtils.jsonToObject( userGroupStr, SuSUserGroupDTO.class );
        Assert.assertEquals( actual, JsonUtils.jsonToObject( userGroupStr, SuSUserGroupDTO.class ) );

    }

    /**
     * Should Failed To Create User Group When Invalid Json Is Given As Parameter.
     */
    @Test
    public void shouldFailedToCreateUserGroupWhenInvalidJsonIsGivenAsParameter() {

        Response expected = service.createUserGroup( USER_GROUP_INVALID_PAYLOAD );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_INTERNAL_SERVER_ERROR, expected.getStatus() );
    }

    /**
     * Should Failed To Create User Group When Empty String Is Given As Parameter.
     */
    @Test
    public void shouldFailedToCreateUserGroupWhenEmptyStringIsGivenAsParameter() {

        Response expected = service.createUserGroup( StringUtils.EMPTY );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_INTERNAL_SERVER_ERROR, expected.getStatus() );
    }

    /**
     * Should Failed To Create User Group When Null Is Given As Parameter.
     */
    @Test
    public void shouldFailedToCreateUserGroupWhenNullIsGivenAsParameter() {
        Response expected = service.createUserGroup( null );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_INTERNAL_SERVER_ERROR, expected.getStatus() );
    }

    /**
     * ************************** updateUserGroup **********************************.
     */

    /**
     * Should Successfully Update User Group When Valid Json Is Given As Parameter
     */
    @Test
    public void shouldSuccessfullyUpdateUserGroupWhenValidJsonIsGivenAsParameter() {
        SuSUserGroupDTO actual = getFilledUserGrpDto();

        EasyMock.expect( manager.updateUserGroup( EasyMock.anyString(), EasyMock.anyObject( SuSUserGroupDTO.class ) ) ).andReturn( actual )
                .anyTimes();
        mockControl.replay();
        Response expected = service.updateUserGroup( USER_GROUP_PAYLOAD_ONE );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO expectedData = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( expectedData.getData() );
        String userGroupStr = JsonUtils.convertMapToString( ( Map< String, String > ) expectedData.getData() );
        Assert.assertEquals( actual, JsonUtils.jsonToObject( userGroupStr, SuSUserGroupDTO.class ) );
    }

    /**
     * Should Failed To Update User Group When Invalid Json Is Given As Parameter.
     */
    @Test
    public void shouldFailedToUpdateUserGroupWhenInvalidJsonIsGivenAsParameter() {

        Response expected = service.updateUserGroup( USER_GROUP_INVALID_PAYLOAD );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_INTERNAL_SERVER_ERROR, expected.getStatus() );
    }

    /**
     * Should Failed To Update User Group When Empty String Is Given As Parameter.
     */
    @Test
    public void shouldFailedToUpdateUserGroupWhenEmptyStringIsGivenAsParameter() {
        Response expected = service.updateUserGroup( StringUtils.EMPTY );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_INTERNAL_SERVER_ERROR, expected.getStatus() );
    }

    /**
     * Should Failed To Update User Group When Null Is Given As Parameter.
     */
    @Test
    public void shouldFailedToUpdateUserGroupWhenNullIsGivenAsParameter() {
        Response expected = service.updateUserGroup( null );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_INTERNAL_SERVER_ERROR, expected.getStatus() );
    }

    /**
     * ************************** deleteUserGroup **********************************.
     */

    /**
     * Should Successfully Delete User Group When Valid Id Is Given As Parameter
     */
    @Test
    public void shouldSuccessfullyDeleteUserGroupWhenValidIdIsGivenAsParameter() {
        EasyMock.expect( manager.deleteUserGroupBySelection( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( Boolean.TRUE ).anyTimes();
        mockControl.replay();
        Response expected = service.deleteUserGroup( ID_FIELD.toString(), MODE );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO actual = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertTrue( ( boolean ) actual.getData() );

    }

    /**
     * Should Failed To Delete User Group When Null Is Given As Parameter.
     */
    @Test
    public void shouldFailedToDeleteUserGroupWhenNullIsGivenAsParameter() {
        EasyMock.expect( manager.deleteUserGroupBySelection( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( Boolean.FALSE ).anyTimes();
        mockControl.replay();
        Response expected = service.deleteUserGroup( null, null );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO actual = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertFalse( ( boolean ) actual.getData() );
    }

    /**
     * ************************** readUserGroup **********************************.
     */

    /**
     * Should Successfully Get User Group When Valid Id Is Given As Parameter
     */
    @Test
    public void shouldSuccessfullyGetUserGroupWhenValidIdIsGivenAsParameter() {

        SuSUserGroupDTO actual = getFilledUserGrpDto();
        EasyMock.expect( manager.readUserGroup( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getFilledUserGrpDto() ).anyTimes();
        mockControl.replay();
        Response expected = service.readUserGroup( ID_FIELD );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO expectedData = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( expectedData.getData() );
        String userGroupStr = JsonUtils.convertMapToString( ( Map< String, String > ) expectedData.getData() );
        Assert.assertEquals( actual, JsonUtils.jsonToObject( userGroupStr, SuSUserGroupDTO.class ) );
    }

    /**
     * Should Throw Ilegal Args Exception To Get User Group When InValid UUID Is Given As Parameter.
     */
    @Test
    public void shouldThrowIlegalArgsExceptionToGetUserGroupWhenInValidUUIDIsGivenAsParameter() {
        thrown.expect( IllegalArgumentException.class );
        service.readUserGroup( UUID.fromString( INVALID_UUID ) );

    }

    /**
     * Should Failed To Get User Group When Null Id Is Given As Parameter.
     */
    @Test
    public void shouldFailedToGetUserGroupWhenNullIdIsGivenAsParameter() {
        EasyMock.expect( manager.readUserGroup( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) ).andReturn( null ).anyTimes();
        mockControl.replay();
        Response expected = service.readUserGroup( null );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO actual = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNull( actual.getData() );

    }

    /**
     * ************************** getUserGroupList **********************************.
     */

    /**
     * Should Successfully Get User Group List When Manager Return Filled List
     */
    @Test
    public void shouldSuccessfullyGetUserGroupListWhenManagerReturnFilledList() {
        List< SuSUserGroupDTO > actual = new ArrayList<>();
        actual.add( getFilledUserGrpDto() );
        FiltersDTO filtersDTO = populateFilterDTO();
        FilteredResponse< SuSUserGroupDTO > expectedFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO, actual );

        EasyMock.expect( manager.getUserGroupsList( EasyMock.anyString(), EasyMock.anyObject( FiltersDTO.class ) ) )
                .andReturn( expectedFilteredResponse ).anyTimes();
        mockControl.replay();
        Response expected = service.getUserGroupsList( getFilterDtoAsString() );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO expectedData = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        String userGroupStr = JsonUtils.toJson( ( LinkedHashMap ) expectedData.getData() );
        FilteredResponse< SuSUserGroupDTO > filteredResponse = JsonUtils.jsonToObject( userGroupStr, FilteredResponse.class );
        List expectedResponse = JsonUtils.jsonToList( JsonUtils.toJson( filteredResponse.getData() ), SuSUserGroupDTO.class );
        Assert.assertEquals( ( ( List< SuSUserGroupDTO > ) filteredResponse.getData() ).size(), expectedResponse.size() );
        Assert.assertNotNull( expectedResponse );
        Assert.assertEquals( actual.get( FIRST_INDEX ), expectedResponse.get( FIRST_INDEX ) );

    }

    /**
     * Should Get Empty List Of User Group When Manager Return Empty List.
     */
    @Test
    public void shouldGetEmptyListOfUserGroupWhenManagerReturnEmptyList() {
        List< SuSUserGroupDTO > actual = new ArrayList<>();
        FiltersDTO filtersDTO = populateFilterDTO();
        FilteredResponse< SuSUserGroupDTO > expectedFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO, actual );

        EasyMock.expect( manager.getUserGroupsList( EasyMock.anyString(), EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( null )
                .anyTimes();
        mockControl.replay();
        Response expected = service.getUserGroupsList( getFilterDtoAsString() );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO expectedData = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        String userGroupStr = JsonUtils.toJson( ( LinkedHashMap ) expectedData.getData() );
        FilteredResponse< ? > filteredResponse = JsonUtils.jsonToObject( userGroupStr, FilteredResponse.class );
        Assert.assertNull( filteredResponse );

    }

    /**
     * Should save view successfully when valid object view payload is given.
     */
    @Test
    public void shouldSaveViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
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
        ;
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
        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
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
        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
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
        Assert.assertEquals( objectViewDTO.getName(), list.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getName() );
        Assert.assertEquals( objectViewDTO.getObjectViewJson(), list.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewJson() );
        Assert.assertEquals( objectViewDTO.getObjectViewKey(), list.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewKey() );
    }

    /**
     * Should delete view successfully when valid view id provided.
     */
    @Test
    public void shouldDeleteViewSuccessfullyWhenValidViewIdProvided() {

        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
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

        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
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
     * Should update view as default successfully when valid view id is given.
     */
    @Test
    public void shouldUpdateViewAsDefaultSuccessfullyWhenValidViewIdIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = service.setViewAsDefault( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
    }

    /**
     * Should get view successfully when valid view id is given.
     */
    @Test
    public void shouldGetViewSuccessfullyWhenValidViewIdIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
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
     * ************************** Selections **********************************.
     */

    /**
     * Should successfully return paginated selections by selection id.
     */
    @Test
    public void shouldSuccessfullyReturnPaginatedSelectionsBySelectionId() {
        FiltersDTO filtersDTO = populateFilterDTO();
        filtersDTO.setFilteredRecords( ( long ) 1 );
        filtersDTO.setTotalRecords( ( long ) 1 );
        List< SuSUserGroupDTO > actual = new ArrayList<>();
        actual.add( getFilledUserGrpDto() );

        FilteredResponse< SuSUserGroupDTO > expectedFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO, actual );
        EasyMock.expect( manager.getAllGroupSelections( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject() ) )
                .andReturn( expectedFilteredResponse ).anyTimes();
        mockControl.replay();
        Response actualResponse = service.getAllSelectionsWithPagination( SELECTION_ID.toString(), getFilterDtoAsString() );
        Assert.assertNotNull( actualResponse );
        Assert.assertEquals( HttpStatus.SC_OK, actualResponse.getStatus() );
    }

    /**
     * Should throw bad request when wrong credentials are provided to group selection pagination.
     */
    @Test
    public void shouldThrowBadRequestWhenWrongCredentialsAreProvidedToGroupSelectionPagination() {
        FiltersDTO filtersDTO = populateFilterDTO();
        filtersDTO.setFilteredRecords( ( long ) 1 );
        filtersDTO.setTotalRecords( ( long ) 1 );
        List< SuSUserGroupDTO > actual = new ArrayList<>();
        actual.add( getFilledUserGrpDto() );

        FilteredResponse< SuSUserGroupDTO > expectedFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO, actual );
        EasyMock.expect( manager.getAllGroupSelections( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject() ) )
                .andReturn( expectedFilteredResponse ).anyTimes();
        mockControl.replay();
        Response actualResponse = service.getAllSelectionsWithPagination( SELECTION_ID.toString(), filtersDTO.toString() );
        Assert.assertNotNull( actualResponse );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actualResponse.getStatus() );
    }

    /**
     * ***************************** Helper Methods ******************************.
     *
     * @return the filter dto as string
     */

    private String getFilterDtoAsString() {
        FiltersDTO filtersDTO = populateFilterDTO();
        return JsonUtils.toJson( filtersDTO );
    }

    /**
     * Get Filled User Grp Dto.
     *
     * @return the filled user grp dto
     */
    private SuSUserGroupDTO getFilledUserGrpDto() {
        susGroupDto.setId( ID_FIELD );
        susGroupDto.setName( NAME_FIELD );
        susGroupDto.setDescription( DESCRIPTION_FIELD );
        susGroupDto.setStatus( ACTIVE );
        susGroupDto.setUsers( null );
        susGroupDto.setSelectionId( SELECTION_ID.toString() );
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
        return filterDTO;
    }

    /**
     * Prepare object view DTO.
     *
     * @return the object view DTO
     */
    private ObjectViewDTO prepareObjectViewDTO() {
        ObjectViewDTO objectViewDTO = new ObjectViewDTO();
        objectViewDTO.setName( NAME_FIELD );
        objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.GROUP_TABLE_KEY );
        objectViewDTO.setObjectViewJson( OBJECT_VIEW_PAYLOAD );
        return objectViewDTO;
    }

}