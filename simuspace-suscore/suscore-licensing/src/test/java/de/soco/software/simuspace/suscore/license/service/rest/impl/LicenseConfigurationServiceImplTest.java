package de.soco.software.simuspace.suscore.license.service.rest.impl;

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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseConfigurationManager;

/**
 * The Class is responsible to test public functions of class {@link LicenseConfigurationServiceImpl}.
 */
@Ignore
public class LicenseConfigurationServiceImplTest {

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant LICENSE_JSON_WITHOUT_SIGN.
     */
    private final static String LICENSE_FORM_JSON_WITHOUT_SIGN = "{\"license\":\"{\\\"customer\\\":\\\"Festo\\\",\\\"vendor\\\":\\\"SOCO\\\",\\\"reseller\\\":\\\"soco\\\",\\\"type\\\":\\\"commercial\\\",\\\"module\\\":\\\"RunSim\\\",\\\"userLimit\\\":{\\\"allowedUsers\\\":2,\\\"restrictedUsers\\\":2},\\\"addons\\\":{\\\"Location\\\":2,\\\"archive\\\":true},\\\"features\\\":[\\\"solver\\\",\\\"licenseserver\\\"],\\\"licenseType\\\":\\\"named\\\",\\\"expiryTime\\\":\\\"20/08/2017\\\",\\\"macAddress\\\":\\\"38:60:77:2A:23:14\\\"}\"}";

    /**
     * The Constant LICENSE_CONFIG_FILE_PATH.
     */
    private static final String LICENSE_CONFIG_FILE_PATH = "/license.json";

    /**
     * The Constant CHECK_BOX_STATE_STR.
     */
    private static final String CHECK_BOX_STATE_STR = "{\"userType\":\"1\"}";

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
     * The Constant OBJECT_VIEW_INVALID_PAYLOAD.
     */
    private static final String OBJECT_VIEW_INVALID_PAYLOAD = "{\"id\": 12355,\"name\": \"view-1\",\"defaultView\": false,\"setting\": {\"draw\": 3,\"start\": 0,\"length\": 25,\"search\": \"search test\",\"columns\": [{\"name\": \"groups.name\",\"visible\": true,\"dir\": null,\"filters\": [ {\"operator\": \"Contains\",\"value\": \"beta\",\"condition\": \"AND\"},{ \"operator\": \"Contains\",\"value\": \"delta\",\"condition\": \"AND\"},{\"operator\": \"Contains\",\"value\": \"gamma\",\"condition\": \"AND\"}],\"reorder\": 3}] }}";

    /**
     * The Constant thrown.
     */
    private static final String OBJECT_VIEW_NAME = "Name";

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The configuration manager.
     */
    private LicenseConfigurationManager configurationManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The license config service.
     */
    private LicenseConfigurationServiceImpl licenseConfigService;

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    private static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * set Up.
     */
    @Before
    public void setUp() {
        mockControl().resetToNice();
        configurationManager = mockControl().createMock( LicenseConfigurationManager.class );
        licenseConfigService = new LicenseConfigurationServiceImpl();
        objectViewManager = mockControl.createMock( ObjectViewManager.class );
        licenseConfigService.setConfigurationManager( configurationManager );
    }

    /**
     * Should sign license when valid json is provided.
     */
    @Test
    public void shouldAddLicenseWhenValidJsonIsProvided() {
        ModuleLicenseDTO expected = fillLicenseDTO();

        EasyMock.expect( configurationManager.addLicense( EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( expected );
        mockControl().replay();
        Response response = licenseConfigService.addLicense( LICENSE_FORM_JSON_WITHOUT_SIGN );

        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( susResponseDTO.getData() );
        String moduleLicenseStr = JsonUtils.convertMapToString( ( Map< String, String > ) susResponseDTO.getData() );
        ModuleLicenseDTO actual = JsonUtils.jsonToObject( moduleLicenseStr, ModuleLicenseDTO.class );
        Assert.assertNotNull( actual );
        Assert.assertNotNull( actual.getKeyInformation() );

        Assert.assertEquals( expected, actual );
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
     * Should add users to module when valid users are provided.
     */
    @Test
    public void shouldAddUsersToModuleWhenValidUsersAreProvided() {

        UUID userId = UUID.randomUUID();
        Response response = licenseConfigService.manageUserLicense( userId, CHECK_BOX_STATE_STR );
        SusResponseDTO expectedSusResponseDto = fillExpectedResponseDTO( true, true,
                MessageBundleFactory.getMessage( Messages.USER_LICENSE_SUCCESSFULLY_UPDATED.getKey() ), Message.SUCCESS );
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
        SusResponseDTO actualSusResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( expectedSusResponseDto, actualSusResponseDTO );

    }

    /**
     * Should produce response with success equals to false plus A fail message and null data when null users going to add.
     */
    @Test
    public void shouldProduceResponseWithSuccessEqualsToFalsePlusAFailMessageAndNullDataWhenNullUsersGoingToAdd() {
        UUID userId = UUID.randomUUID();
        Response response = licenseConfigService.manageUserLicense( userId, null );
        SusResponseDTO expected = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertFalse( expected.getSuccess() );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
        Assert.assertEquals( expected.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
        Assert.assertNull( expected.getData() );

    }

    /**
     * Should delete module license when valid existed module name is passed to delete.
     */
    @Test
    public void shouldDeleteModuleLicenseWhenValidExistedModuleNameIsPassedToDelete() {

        Response response = licenseConfigService.deleteModuleLicense( UUID.randomUUID().toString(), "single" );

        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertTrue( ( boolean ) susResponseDTO.getData() );
        Assert.assertEquals( susResponseDTO.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.MODULE_LICENSE_IS_SUCCESSFULLY_DELETED.getKey() ) );

    }

    /**
     * Should get module license list when modules are already exists.
     */
    @Test
    public void shouldGetModuleLicenseListWhenModulesAreAlreadyExists() {

        ModuleLicenseDTO expected = fillLicenseDTO();
        FiltersDTO filtersDTO = populateFilterDTO();
        List< ModuleLicenseDTO > expectedLicenseDTOs = new ArrayList<>();
        expectedLicenseDTOs.add( expected );
        FilteredResponse< ModuleLicenseDTO > expectedFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO,
                expectedLicenseDTOs );

        EasyMock.expect( configurationManager.getModuleLicenseList( EasyMock.anyString(), EasyMock.anyObject( FiltersDTO.class ) ) )
                .andReturn( expectedFilteredResponse );
        mockControl().replay();

        Response response = licenseConfigService.getModuleLicenseList( JsonUtils.toJson( filtersDTO ) );

        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );

        String moduleLicenseStr = JsonUtils.toJson( ( LinkedHashMap< Object, Object > ) susResponseDTO.getData() );
        FilteredResponse< ModuleLicenseDTO > filteredResponse = JsonUtils.jsonToObject( moduleLicenseStr, FilteredResponse.class );

        List< ModuleLicenseDTO > actual = JsonUtils.jsonToList( JsonUtils.toJson( filteredResponse.getData() ), ModuleLicenseDTO.class );
        Assert.assertNotNull( actual );
        Assert.assertEquals( expectedLicenseDTOs.get( FIRST_INDEX ), actual.get( FIRST_INDEX ) );

    }

    /**
     * Should save view successfully when valid object view payload is given.
     */
    @Test
    public void shouldSaveViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = licenseConfigService.saveLicenseView( OBJECT_VIEW_PAYLOAD );
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
     * Should get bad request response when payload is invalid for saving object view.
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForSavingObjectView() {
        final Response response = licenseConfigService.saveLicenseView( OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * Should update view successfully when valid object view payload is given.
     */
    @Test
    public void shouldUpdateViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = licenseConfigService.updateLicenseView( OBJECT_VIEW_ID, OBJECT_VIEW_PAYLOAD );
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

        final Response response = licenseConfigService.updateLicenseView( OBJECT_VIEW_ID, OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
        ;
    }

    /**
     * Should get all views successfully when valid object view key is provided.
     */
    @Test
    public void shouldGetAllViewsSuccessfullyWhenValidObjectViewKeyIsProvided() {

        ObjectViewDTO objectViewDTO = prepareObjectViewDTO();
        List< ObjectViewDTO > expectedResponse = Arrays.asList( objectViewDTO );
        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expectedResponse );

        mockControl.replay();
        Response actual = licenseConfigService.getAllLicenseViews();
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        List< ObjectViewDTO > list = ( List< ObjectViewDTO > ) JsonUtils.jsonToList( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( objectViewDTO.getName(), list.get( 0 ).getName() );
        Assert.assertEquals( objectViewDTO.getObjectViewJson(), list.get( 0 ).getObjectViewJson() );
        Assert.assertEquals( objectViewDTO.getObjectViewKey(), list.get( 0 ).getObjectViewKey() );
    }

    /**
     * Should delete view successfully when valid view id provided.
     */
    @Test
    public void shouldDeleteViewSuccessfullyWhenValidViewIdProvided() {

        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( true );
        mockControl.replay();
        Response actual = licenseConfigService.deleteLicenseView( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getData(), MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ) );
    }

    /**
     * Should not delete view successfully when in valid view id provided.
     */
    @Test
    public void shouldNotDeleteViewSuccessfullyWhenInValidViewIdProvided() {

        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( false );
        mockControl.replay();
        Response actual = licenseConfigService.deleteLicenseView( OBJECT_VIEW_ID );
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
        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = licenseConfigService.setLicenseViewAsDefault( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
    }

    /**
     * Should get view successfully when valid view id is given.
     */
    @Test
    public void shouldGetViewSuccessfullyWhenValidViewIdIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.getObjectViewById( EasyMock.anyObject( UUID.class ) ) ).andReturn( expected );
        mockControl.replay();
        Response actual = licenseConfigService.getLicenseView( OBJECT_VIEW_ID );
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
     * Should save manage license view successfully when valid object view payload is given.
     */
    @Test
    public void shouldSaveManageLicenseViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = licenseConfigService.saveManageLicenseView( OBJECT_VIEW_PAYLOAD );
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
     * Should get bad request response when payload is invalid for saving manage license view.
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForSavingManageLicenseView() {

        final Response response = licenseConfigService.saveManageLicenseView( OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
        ;

    }

    /**
     * Should update manage license view successfully when valid object view payload is given.
     */
    @Test
    public void shouldUpdateManageLicenseViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = licenseConfigService.updateManageLicenseView( OBJECT_VIEW_ID, OBJECT_VIEW_PAYLOAD );
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
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForUpdatingManageLicenseView() {

        final Response response = licenseConfigService.updateManageLicenseView( OBJECT_VIEW_ID, OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
        ;
    }

    /**
     * Should get all manage license views successfully when valid object view key is provided.
     */
    @Test
    public void shouldGetAllManageLicenseViewsSuccessfullyWhenValidObjectViewKeyIsProvided() {

        ObjectViewDTO objectViewDTO = prepareObjectViewDTO();
        List< ObjectViewDTO > expectedResponse = Arrays.asList( objectViewDTO );
        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expectedResponse );

        mockControl.replay();
        Response actual = licenseConfigService.getAllManageLicenseViews();
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        List< ObjectViewDTO > list = ( List< ObjectViewDTO > ) JsonUtils.jsonToList( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( objectViewDTO.getName(), list.get( 0 ).getName() );
        Assert.assertEquals( objectViewDTO.getObjectViewJson(), list.get( 0 ).getObjectViewJson() );
        Assert.assertEquals( objectViewDTO.getObjectViewKey(), list.get( 0 ).getObjectViewKey() );
    }

    /**
     * Should delete manage license view successfully when valid view id provided.
     */
    @Test
    public void shouldDeleteManageLicenseViewSuccessfullyWhenValidViewIdProvided() {

        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( true );
        mockControl.replay();
        Response actual = licenseConfigService.deleteManageLicenseView( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getData(), MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ) );
    }

    /**
     * Should not delete manage license view successfully when in valid view id provided.
     */
    @Test
    public void shouldNotDeleteManageLicenseViewSuccessfullyWhenInValidViewIdProvided() {

        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( false );
        mockControl.replay();
        Response actual = licenseConfigService.deleteManageLicenseView( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
    }

    /**
     * Should update manage license view as default successfully when valid view id is given.
     */
    @Test
    public void shouldUpdateManageLicenseViewAsDefaultSuccessfullyWhenValidViewIdIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = licenseConfigService.setManageLicenseViewAsDefault( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
    }

    /**
     * Should get manage license view successfully when valid view id is given.
     */
    @Test
    public void shouldGetManageLicenseViewSuccessfullyWhenValidViewIdIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( configurationManager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.getObjectViewById( EasyMock.anyObject( UUID.class ) ) ).andReturn( expected );
        mockControl.replay();
        Response actual = licenseConfigService.getManageLicenseView( OBJECT_VIEW_ID );
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
     * Fill license.
     *
     * @return the license
     */
    private ModuleLicenseDTO fillLicenseDTO() {

        ModuleLicenseDTO license = JsonUtils.jsonStreamToObject( this.getClass().getResourceAsStream( LICENSE_CONFIG_FILE_PATH ),
                ModuleLicenseDTO.class );
        return license;
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
     * Prepare object view DTO.
     *
     * @return the object view DTO
     */
    private ObjectViewDTO prepareObjectViewDTO() {
        ObjectViewDTO objectViewDTO = new ObjectViewDTO();
        objectViewDTO.setName( OBJECT_VIEW_NAME );
        objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.USER_DIRECTORY_TABLE_KEY );
        objectViewDTO.setObjectViewJson( OBJECT_VIEW_PAYLOAD );
        return objectViewDTO;
    }

}
