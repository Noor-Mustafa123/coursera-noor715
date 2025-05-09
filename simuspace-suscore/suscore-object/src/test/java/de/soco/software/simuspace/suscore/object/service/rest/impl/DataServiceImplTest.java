package de.soco.software.simuspace.suscore.object.service.rest.impl;

/**
 * Test Cases for DataServiceImpl Class.
 *
 * @author Nosheen.Sharif
 */
public class DataServiceImplTest {

//    /**
//     * The Constant FIELD_NAME_MODIFIED_ON.
//     */
//    public static final String FIELD_NAME_MODIFIED_ON = "modifiedOn";
//
//    /**
//     * The Constant DELETE_MODE.
//     */
//    private static final String DELETE_MODE = "single";
//
//    /**
//     * The Constant FILTERED_RECORDS.
//     */
//    private static final long FILTERED_RECORDS = 2l;
//
//    /**
//     * Generic Rule for the expected exception.
//     */
//    @Rule
//    public ExpectedException thrown = ExpectedException.none();
//
//    /**
//     * The mock control.
//     */
//    private static IMocksControl mockControl = EasyMock.createControl();
//
//    /**
//     * The data Service Reference.
//     */
//    private DataServiceImpl service;
//
//    /**
//     * The Data Manager Reference.
//     */
//    private DataManager manager;
//
//    /**
//     * The object view manager.
//     */
//    private ObjectViewManager objectViewManager;
//
//    /**
//     * The audit log manager.
//     */
//    private AuditLogManager auditLogManager;
//
//    /**
//     * Dummy Data Object Name of an object.
//     */
//    private static final String DATA_OBJECT_NAME = "Test Data Object name";
//
//    /**
//     * Dummy Error message.
//     */
//    private static final String OBJECT_ERROR_MESSAGE = "No Objects Found";
//
//    /**
//     * Dummy PROJECT_UPDATE_ERROR_MESSAGE message.
//     */
//    private static final String PROJECT_UPDATE_ERROR_MESSAGE = "Unable to Update Project";
//
//    /**
//     * Dummy PROJECT_GET_ERROR_MESSAGE message.
//     */
//    private static final String PROJECT_GET_ERROR_MESSAGE = "Unable to Get Project";
//
//    /**
//     * Dummy Error message TYpe.
//     */
//    private static final String MESSAGE_TYPE_ERROR = "ERROR";
//
//    /**
//     * The Constant WEBM.
//     */
//    private static final String WEBM = "webm";
//
//    /**
//     * The Constant MP4.
//     */
//    private static final String MP4 = "mp4";
//
//    /**
//     * The Constant POSTER.
//     */
//    private static final String POSTER = "poster";
//
//    /**
//     * The Constant THUMB_NAIL.
//     */
//    private static final String THUMB_NAIL = "thumbnail";
//
//    /**
//     * Dummy project Id
//     */
//    private static final UUID PROJECT_ID = UUID.randomUUID();
//
//    private static final String TYPE_ID = UUID.randomUUID().toString();
//
//    /**
//     * Dummy projectDto Object.
//     */
//    private ProjectDTO projectDTO = new ProjectDTO();
//
//    /**
//     * The workflow project DTO.
//     */
//    private WorkflowProjectDTO workflowProjectDTO = new WorkflowProjectDTO();
//
//    /**
//     * Dummy Version Id for test Cases.
//     */
//    private static final int DEFAULT_VERSION_ID = 1;
//
//    /**
//     * Dummy CreatedOn Project Date.
//     */
//    private static final Date CREATED_ON_DATE = new Date();
//
//    /**
//     * Dummy UpdatedOn Project Date.
//     */
//    private static final Date UPDATED_ON_DATE = new Date();
//
//    /**
//     * Dummy Project Name of an object.
//     */
//    private static final String PROJECT_NAME = "Test PROJECT name";
//
//    /**
//     * Dummy Project Description of an object.
//     */
//    private static final String PROJECT_DESCRPTION = "Test PROJECT Description";
//
//    /**
//     * TEST_CUSTOM_ATTRIBUTE.
//     */
//    private static final String TEST_CUSTOM_ATTRIBUTE = "test1";
//
//    /**
//     * Dummy INVALID_PARAM_ERROR_MESSAGEs message.
//     */
//    private static final String INVALID_PARAM_ERROR_MESSAGE = "Invalid parameters provided.";
//
//    /**
//     * The Constant UNIT_JSON.
//     */
//    private static final String UNIT_JSON = "{\"xunit\":\"cm\", \"yunit\":\"mm\"}";
//
//    /**
//     * Dummy Json for an object
//     */
//    private static final String WORKFLOW_PROJECT_JSON = "{\"parentId\":\"5ed5156d-743f-4ea9-a66c-b407f50ea0a9\",\"typeId\":\"62c4c2f6-15f8-11e7-93ae-92361f002671\",\"name\":\"name\",\"description\":\"description\",\"status\":\"Active\",\"type\":\"Data\",\"config\":\"Project_Start_Config.js\"}";
//
//    /**
//     * Dummy Json for an object.
//     */
//    private static final String PROJECT_JSON = "{\"parentId\": null,\"id\": null, \"name\": \"aaa\",\"description\": \"aaa\",\"createdOn\": null,\"updatedOn\": null,\"config\": null,\"customAttributes\": null }";
//
//    /**
//     * Dummy Json for an object.
//     */
//    private static final String INVALID_PROJECT_JSON = "{\"parentIdss\": null,\"id\": null, \"name\": \"aaa\",\"description\": \"aaa\",\"createdOn\": null,\"updatedOn\": null,\"config\": null,\"customAttributes\": null }";
//
//    /**
//     * Dummy Filter Json as Input Parameter.
//     */
//    private static final String FILTER_JSON = "{\"draw\":2,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"id\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"createdOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"updatedOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3}]}";
//
//    /**
//     * Dummy Invalid Filter Json as Input Parameter.
//     */
//    private static final String INVALID_FILTER_JSON = "{\"22draw\":2,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"id\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"createdOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"updatedOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3}]}";
//
//    /**
//     * Dummy Json for an object.
//     */
//    private static final String OBJECT_JSON = "{\"parentId\": null,\"id\": null, \"name\": \"aaa\",\"description\": \"aaa\",\"createdOn\": null,\"updatedOn\": null,\"customAttributes\": null }";
//
//    /**
//     * The Constant WORKFLOW_PROJ_JSON.
//     */
//    private static final String WORKFLOW_PROJ_JSON = "{\"parentId\":\"\",\"typeId\":\"3550a554-81ec-46b2-a01e-19d8a8ae14cd\",\"id\":\"746e2415-4e8c-4411-bc59-61ce89dd9a5f\",\"name\":\"update\",\"description\":\"test\",\"config\":\"WorkflowProjectConfig.json\",\"customAttributes\":{\"customatt1\":\"Admin\"}}";
//
//    /**
//     * The Constant OBJECT_VIEW_ID.
//     */
//    private static final String OBJECT_VIEW_ID = UUID.randomUUID().toString();
//
//    /**
//     * The Constant VIEW_NAME.
//     */
//    private static final String VIEW_NAME = "VIEW";
//
//    /**
//     * The Constant OBJECT_VIEW_PAYLOAD.
//     */
//    private static final String OBJECT_VIEW_PAYLOAD = "{\"id\":\"" + OBJECT_VIEW_ID
//            + "\",\"name\": \"view-1\",\"defaultView\": false,\"settings\": {\"draw\": 3,\"start\": 0,\"length\": 25,\"search\": \"search test\",\"columns\": [{\"name\": \"groups.name\",\"visible\": true,\"dir\": null,\"filters\": [ {\"operator\": \"Contains\",\"value\": \"beta\",\"condition\": \"AND\"},{ \"operator\": \"Contains\",\"value\": \"delta\",\"condition\": \"AND\"},{\"operator\": \"Contains\",\"value\": \"gamma\",\"condition\": \"AND\"}],\"reorder\": 3}] }}";
//
//    /**
//     * The Constant OBJECT_VIEW_INVALID_PAYLOAD.
//     */
//    private static final String OBJECT_VIEW_INVALID_PAYLOAD = "{\"id\": 12355,\"name\": \"view-1\",\"defaultView\": false,\"setting\": {\"draw\": 3,\"start\": 0,\"length\": 25,\"search\": \"search test\",\"columns\": [{\"name\": \"groups.name\",\"visible\": true,\"dir\": null,\"filters\": [ {\"operator\": \"Contains\",\"value\": \"beta\",\"condition\": \"AND\"},{ \"operator\": \"Contains\",\"value\": \"delta\",\"condition\": \"AND\"},{\"operator\": \"Contains\",\"value\": \"gamma\",\"condition\": \"AND\"}],\"reorder\": 3}] }}";
//
//    /**
//     * The Constant OBJECT_META_DATA_PAYLOAD.
//     */
//    private static final String OBJECT_META_DATA_PAYLOAD = "{\"metadata\":[{\"key\":\"1\",\"value\":\"1\"},{ \"key\": \"2\",\"value\":\"2\"}]}";
//
//    /**
//     * Dummy Error MEssage for invalid uuid.
//     */
//    private static final String INVALID_UUID_ERROR_MESSAGE = "Invalid UUID string: ";
//
//    /**
//     * Dummy Invalid DataObject Id.
//     */
//    private static final String INVALID_DATA_OBJECT_ID = "eg3-4nf";
//
//    /**
//     * Dummy Index for List get.
//     */
//    private static final int FIRST_INDEX = 0;
//
//    /**
//     * Dummy DataObject Id.
//     */
//    private static final UUID DATA_OBJECT_ID = UUID.randomUUID();
//
//    /**
//     * The Constant USER_ID.
//     */
//    private static final UUID USER_ID = UUID.randomUUID();
//
//    /**
//     * The Constant CHECK_BOX_STATE_STR.
//     */
//    private static final String CHECK_BOX_STATE_STR = "{\"permissionDTOs-View\":\"1\"}";
//
//    /**
//     * The Constant OBJECT_ID.
//     */
//    private static final UUID OBJECT_ID = UUID.randomUUID();
//
//    /**
//     * The Constant SID_ID.
//     */
//    private static final UUID SID_ID = UUID.randomUUID();
//
//    /**
//     * The Constant DISBALE.
//     */
//    private static final int DISBALE = 0;
//
//    /**
//     * The Constant OBJECT_VIEW_MASK.
//     */
//    private static final int OBJECT_VIEW_MASK = 2;
//
//    /**
//     * The Constant OBJECT_VIEW.
//     */
//    private static final String OBJECT_VIEW = "View";
//
//    /**
//     * The Constant INDEX.
//     */
//    private static final int INDEX = 0;
//
//    /**
//     * Dummy dataObject Dto.
//     */
//    private DataObjectDTO dataObjectDTO = new DataObjectDTO();
//
//    /**
//     * The variant DTO.
//     */
//    private VariantDTO variantDTO = new VariantDTO();
//
//    /**
//     * The library DTO.
//     */
//    private LibraryDTO libraryDTO = new LibraryDTO();
//
//    /**
//     * The Constant META_DATA_KEY.
//     */
//    public static final String META_DATA_KEY = "key";
//
//    /**
//     * The Constant META_DATA_VALUE.
//     */
//    public static final String META_DATA_VALUE = "value";
//
//    /**
//     * The Constant TEST_CONTEXT_URL.
//     */
//    private static final String TEST_CONTEXT_URL = "data-test";
//
//    /**
//     * The Constant TEST_CONTEXT_TITLE.
//     */
//    private static final String TEST_CONTEXT_TITLE = "test-context";
//
//    /**
//     * The Constant TEST_CONTEXT_ICON.
//     */
//    private static final String TEST_CONTEXT_ICON = "test-icon";
//
//    /**
//     * The Constant OBJECT_TYPE_ERROR_MESSAGE.
//     */
//    private static final String OBJECT_TYPE_ERROR_ENTITY = "{\"message\":{\"content\":\"Empty List\",\"type\":\"ERROR\"},\"success\":false,\"expire\":false}";
//
//    /**
//     * The Constant ERROR_MESSAGE.
//     */
//    private static final String ERROR_MESSAGE = "ERROR";
//
//    /**
//     * The Constant ERROR_ENTITY.
//     */
//    private static final String ERROR_ENTITY = "{\"message\":{\"content\":\"ERROR\",\"type\":\"ERROR\"},\"success\":false,\"expire\":false}";
//
//    /**
//     * The Constant OBJECT_TYPE_ERROR_MESSAGE.
//     */
//    private static final String OBJECT_TYPE_ERROR_MESSAGE = "Empty List";
//
//    /**
//     * The Constant SET_INHERITED.
//     */
//    private static final String SET_INHERITED = "1";
//
//    /**
//     * The Constant UNSET_INHERITED.
//     */
//    private static final String UNSET_INHERITED = "0";
//
//    /**
//     * The Constant SYNC_CONTEXT_TITLE.
//     */
//    private static final String SYNC_CONTEXT_TITLE = "Sync Files";
//
//    /**
//     * The Constant SYNC_CONTEXT_URL.
//     */
//    private static final String SYNC_CONTEXT_URL = "Sync Url";
//
//    /**
//     * The Constant SYNC_CONTEXT_ICON.
//     */
//    private static final String SYNC_CONTEXT_ICON = "SYNC-icon";
//
//    /**
//     * The Constant CHANGE_STATUS_JSON.
//     */
//    private static final String CHANGE_STATUS_JSON = "{\"includeChilds\":\"No\",\"status\":\"903bb897-9531-4c4f-b0ce-50a0db78a780\"}";
//
//    /**
//     * The Constant X_DIMENSION.
//     */
//    private static final String X_DIMENSION = "X";
//
//    /**
//     * The Constant Y_DIMENSION.
//     */
//    private static final String Y_DIMENSION = "Y";
//
//    /**
//     * The Constant X_UNIT.
//     */
//    private static final String X_UNIT = "x";
//
//    /**
//     * The Constant Y_UNIT.
//     */
//    private static final String Y_UNIT = "y";
//
//    /**
//     * The Constant WORKFLOW_PROJECT_ID.
//     */
//    private static final UUID WORKFLOW_PROJECT_ID = UUID.randomUUID();
//
//    /**
//     * The Constant WORKFLOW_PROJECT_NAME.
//     */
//    private static final String WORKFLOW_PROJECT_NAME = "Test PROJECT name";
//
//    /**
//     * The Constant WORKFLOW_PROJECT_DESCRPTION.
//     */
//    private static final String WORKFLOW_PROJECT_DESCRPTION = "Test PROJECT Description";
//
//    /**
//     * Mock control.
//     *
//     * @return the i mocks control
//     */
//    static IMocksControl mockControl() {
//        return mockControl;
//    }
//
//    /**
//     * set Up.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Before
//    public void setUp() throws Exception {
//        mockControl().resetToNice();
//        manager = mockControl().createMock( DataManager.class );
//        objectViewManager = mockControl.createMock( ObjectViewManager.class );
//        auditLogManager = mockControl.createMock( AuditLogManager.class );
//        service = new DataServiceImpl();
//        service.setDataManager( manager );
//        fillProjectDto();
//
//    }
//
//    /**
//     * Should allow permission binded with resource when permission allowed to object.
//     */
//    @Test
//    public void shouldAllowPermissionBindedWithResourceWhenPermissionAllowedToObject() {
//        UUID objectId = UUID.randomUUID();
//        UUID sidId = UUID.randomUUID();
//
//        EasyMock.expect( manager.permitPermissionToObject( EasyMock.anyObject(), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
//        mockControl.replay();
//
//        Response response = service.permitPermissionToObject( CHECK_BOX_STATE_STR, objectId, sidId );
//        SusResponseDTO expectedSusResponseDto = fillExpectedResponseDTO( true, "Permission applied successfully", null, null );
//        Assert.assertNotNull( response );
//        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
//        SusResponseDTO actualSusResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( expectedSusResponseDto.getData(), actualSusResponseDTO.getMessage().getContent() );
//
//    }
//
//    /**
//     * Shoul fail if permit permission to object having permission pay load null.
//     */
//    @Test
//    public void shoulFailIfPermitPermissionToObjectHavingPermissionPayLoadNull() {
//        Response response = service.permitPermissionToObject( null, OBJECT_ID, SID_ID );
//        SusResponseDTO expected = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertFalse( expected.getSuccess() );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
//        Assert.assertEquals( expected.getMessage().getContent(),
//                MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
//        Assert.assertNull( expected.getData() );
//    }
//
//    /**
//     * Should permission applied interruption.
//     */
//    @Test
//    public void shouldPermissionAppliedInterruption() {
//        UUID objectId = UUID.randomUUID();
//        UUID sidId = UUID.randomUUID();
//
//        EasyMock.expect( manager.permitPermissionToObject( EasyMock.anyObject(), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyString() ) ).andReturn( false ).anyTimes();
//        mockControl.replay();
//
//        Response response = service.permitPermissionToObject( CHECK_BOX_STATE_STR, objectId, sidId );
//        SusResponseDTO expectedSusResponseDto = fillExpectedResponseDTO( false, null, "Permission not applied successfully", null );
//        Assert.assertNotNull( response );
//        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
//        SusResponseDTO actualSusResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( expectedSusResponseDto.getData(), actualSusResponseDTO.getData() );
//    }
//
//    /**
//     * Should fail if null json provide for object permissions.
//     */
//    @Test
//    public void shouldFailIfNullJsonProvideForObjectPermissions() {
//        Response response = service.permitPermissionToObject( null, OBJECT_ID, SID_ID );
//        SusResponseDTO expected = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertFalse( expected.getSuccess() );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
//        Assert.assertEquals( expected.getMessage().getContent(),
//                MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
//        Assert.assertNull( expected.getData() );
//    }
//
//    /**
//     * Should return all permission related to object when exact object manage is called.
//     */
//    @Test
//    public void shouldReturnAllPermissionRelatedToObjectWhenExactObjectManageIsCalled() {
//        ManageObjectDTO objectManageDTO = getObjectManageDTO();
//        List< PermissionDTO > permissionDTOs = new ArrayList<>();
//        PermissionDTO permissionDTO = getFillPermissionDTO();
//        permissionDTOs.add( permissionDTO );
//        objectManageDTO.setPermissionDTOs( permissionDTOs );
//        FiltersDTO filtersDTO = populateFilterDTO();
//        List< ManageObjectDTO > expectedObjectManageDTO = new ArrayList<>();
//        expectedObjectManageDTO.add( objectManageDTO );
//        FilteredResponse< ManageObjectDTO > expectedFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO,
//                expectedObjectManageDTO );
//        EasyMock.expect( manager.showPermittedUsersAndGroupsForObject( EasyMock.anyObject( FiltersDTO.class ),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyString() ) ).andReturn( expectedFilteredResponse ).anyTimes();
//        mockControl.replay();
//        Response response = service.showPermittedUsersAndGroupsForObject( OBJECT_ID, JsonUtils.toJson( filtersDTO, FiltersDTO.class ) );
//        Assert.assertNotNull( response );
//        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
//        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
//        String objectManageDTOStr = JsonUtils.toJson( ( LinkedHashMap< Object, Object > ) susResponseDTO.getData() );
//        FilteredResponse< ResourceAccessControlDTO > filteredResponse = JsonUtils.jsonToObject( objectManageDTOStr,
//                FilteredResponse.class );
//        List< ManageObjectDTO > actual = JsonUtils.jsonToList( JsonUtils.toJson( filteredResponse.getData() ), ManageObjectDTO.class );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( expectedObjectManageDTO.get( INDEX ).getName(), actual.get( INDEX ).getName() );
//    }
//
//    /**
//     * Should fail if permission pay load is null.
//     */
//    @Test
//    public void shouldFailIfPermissionPayLoadIsNull() {
//        Response response = service.showPermittedUsersAndGroupsForObject( OBJECT_ID, null );
//        SusResponseDTO expected = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertFalse( expected.getSuccess() );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
//        Assert.assertEquals( expected.getMessage().getContent(),
//                MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
//        Assert.assertNull( expected.getData() );
//    }
//
//    /**
//     * ******************************** Edit Project Form **************************************************.
//     */
//
//    /**
//     * Should SucessFully Get Edit Form Items List If Manager Return Items List
//     */
//    @Test
//    public void shouldSucessFullyGetEditFormItemsListIfManagerReturnItemsList() {
//        List< UIFormItem > expected = prepareEditDummyForm();
//        mockControl().replay();
//        Response actual = service.editProjectForm( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//
//        Assert.assertNotNull( actualResponse.getData() );
//
//        String listUIColumnStr = JsonUtils.toJson( actualResponse.getData() );
//        List< SelectFormItem > actualUIColumnList = JsonUtils.jsonToList( listUIColumnStr, SelectFormItem.class );
//        Assert.assertEquals( expected.size(), actualUIColumnList.size() );
//    }
//
//    /**
//     * Should successfully get table UI of object manager DTO when valid input provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetTableUIOfObjectManagerDTOWhenValidInputProvided() {
//        TableUI expected = getFilledColumns();
//        EasyMock.expect( manager.objectPermissionTableUI( EasyMock.anyString() ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.objectPermissionTableUI( OBJECT_ID );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//
//        Assert.assertNotNull( actualResponse.getData() );
//
//        TableUI actualUIColumnList = JsonUtils.linkedMapObjectToClassObject( actualResponse.getData(), TableUI.class );
//        Assert.assertEquals( expected.getColumns().size(), actualUIColumnList.getColumns().size() );
//    }
//
//    /**
//     * Should Not Get Edit Form Items List If Manager Return Empty Items List.
//     */
//    @Test
//    public void shouldNotGetEditFormItemsListIfManagerReturnEmptyItemsList() {
//        List< UIFormItem > expected = null;
//        mockControl().replay();
//        Response actual = service.editProjectForm( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( expected, actualResponse.getData() );
//
//    }
//
//    /**
//     * Should Get Null As Edit Form Items List If Empty Paramters Are Given As Input To Get Edit Form.
//     */
//    @Test
//    public void shouldGetNullAsEditFormItemsListIfEmptyParamtersAreGivenAsInputToGetEditForm() {
//        List< UIFormItem > expected = null;
//        mockControl().replay();
//        Response actual = service.editProjectForm( null );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( expected, actualResponse.getData() );
//
//    }
//
//    /**
//     * Should throw Exception while editing project form when some thing goes wrong.
//     */
//    @Test
//    public void shouldThrowExceptionWhileEditProjectForm() {
//
//        EasyMock.expect( manager.editProjectForm( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.editProjectForm( PROJECT_ID.toString() );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * ************************************** Edit Project *************************************************.
//     */
//
//    /**
//     * Should SuccessFully Update Project With Valid Project Json Parameter
//     */
//    @Test
//    public void shouldSuccessFullyUpdateProjectWithValidProjectJsonParameter() {
//        ProjectDTO expected = projectDTO;
//        EasyMock.expect( manager.updateProject( EasyMock.anyString(), EasyMock.anyObject( String.class ) ) ).andReturn( projectDTO )
//                .anyTimes();
//        mockControl().replay();
//        Response actual = service.updateProject( PROJECT_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String projectdto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        ProjectDTO actualProjectDto = JsonUtils.jsonToObject( projectdto, ProjectDTO.class );
//
//        Assert.assertEquals( expected.getId(), actualProjectDto.getId() );
//        Assert.assertEquals( expected.getName(), actualProjectDto.getName() );
//        Assert.assertEquals( expected.getDescription(), actualProjectDto.getDescription() );
//        Assert.assertEquals( expected.getParentId(), actualProjectDto.getParentId() );
//        Assert.assertEquals( expected.getCreatedOn(), actualProjectDto.getCreatedOn() );
//
//    }
//
//    /**
//     * Should SuccessFully Update Project With Valid Project Json Parameter.
//     */
//    @Test
//    public void shouldGetErrorMessageInUpdateProjectWhenMangerReturnNullProjectDto() {
//        ProjectDTO expected = null;
//        EasyMock.expect( manager.updateProject( EasyMock.anyString(), EasyMock.anyObject( String.class ) ) ).andReturn( expected )
//                .anyTimes();
//        mockControl().replay();
//        Response actual = service.updateProject( PROJECT_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNull( actualResponse.getData() );
//        Message messageDto = JsonUtils.jsonToObject( JsonUtils.toJson( actualResponse.getMessage() ), Message.class );
//        Assert.assertEquals( PROJECT_UPDATE_ERROR_MESSAGE, messageDto.getContent() );
//        Assert.assertEquals( MESSAGE_TYPE_ERROR, messageDto.getType() );
//
//    }
//
//    /**
//     * Should Get Bad Request Status In Update Project With InValid Json Parameter.
//     */
//    @Test
//    public void shouldGetBadRequestStatusInUpdateProjectWithInValidJsonParameter() {
//        ProjectDTO expected = null;
//        EasyMock.expect( manager.updateProject( EasyMock.anyString(), EasyMock.anyObject( String.class ) ) ).andReturn( expected )
//                .anyTimes();
//        mockControl().replay();
//        Response actual = service.updateProject( INVALID_PROJECT_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNull( actualResponse.getData() );
//        Message messageDto = JsonUtils.jsonToObject( JsonUtils.toJson( actualResponse.getMessage() ), Message.class );
//        Assert.assertEquals( MessageBundleFactory.getMessage( Messages.UNABLE_TO_UPDATE_PROJECT.getKey() ), messageDto.getContent() );
//        Assert.assertEquals( MESSAGE_TYPE_ERROR, messageDto.getType() );
//
//    }
//
//    /**
//     * ************************************** Edit Workflow Project *************************************************.
//     */
//
//    /**
//     * Should success fully update workflow project with valid workflow project json parameter.
//     */
//    @Test
//    public void shouldSuccessFullyUpdateWorkflowProjectWithValidWorkflowProjectJsonParameter() {
//        WorkflowProjectDTO expected = workflowProjectDTO;
//        EasyMock.expect( manager.updateWorkFlowProject( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( workflowProjectDTO )
//                .anyTimes();
//        mockControl().replay();
//        Response actual = service.updateWorkflowProject( WORKFLOW_PROJ_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String projectdto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        WorkflowProjectDTO actualProjectDto = JsonUtils.jsonToObject( projectdto, WorkflowProjectDTO.class );
//
//        Assert.assertEquals( expected.getId(), actualProjectDto.getId() );
//        Assert.assertEquals( expected.getName(), actualProjectDto.getName() );
//        Assert.assertEquals( expected.getDescription(), actualProjectDto.getDescription() );
//        Assert.assertEquals( expected.getParentId(), actualProjectDto.getParentId() );
//        Assert.assertEquals( expected.getCreatedOn(), actualProjectDto.getCreatedOn() );
//
//    }
//
//    /**
//     * Should get error message in update workflow project when manger return null workflow project dto.
//     */
//    @Test
//    public void shouldGetErrorMessageInUpdateWorkflowProjectWhenMangerReturnNullWorkflowProjectDto() {
//        WorkflowProjectDTO expected = null;
//        EasyMock.expect( manager.updateWorkFlowProject( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.updateWorkflowProject( WORKFLOW_PROJ_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNull( actualResponse.getData() );
//        Message messageDto = JsonUtils.jsonToObject( JsonUtils.toJson( actualResponse.getMessage() ), Message.class );
//        Assert.assertEquals( PROJECT_UPDATE_ERROR_MESSAGE, messageDto.getContent() );
//        Assert.assertEquals( MESSAGE_TYPE_ERROR, messageDto.getType() );
//
//    }
//
//    /**
//     * Should get bad request status in update workflow project with in valid json parameter.
//     */
//    @Test
//    public void shouldGetBadRequestStatusInUpdateWorkflowProjectWithInValidJsonParameter() {
//
//        Response actual = service.updateWorkflowProject( INVALID_PROJECT_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );
//        JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//
//    }
//
//    /**
//     * ******************************* Get data object By Id ******************************************.
//     */
//
//    /**
//     * Should Get SuccessFully Get DataObject Response When Valid Object id Is Given
//     */
//    @Test
//    public void shouldGetSuccessFullyGetDataObjectResponseWhenValidObjectIdIsGiven() {
//        DataObjectDTO expected = fillDataObjectDto();
//        EasyMock.expect( manager.getDataObject( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getDataObjectProperties( DATA_OBJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String dataObjectdto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        DataObjectDTO actualDataObjDto = JsonUtils.jsonToObject( dataObjectdto, DataObjectDTO.class );
//
//        Assert.assertEquals( expected.getId(), actualDataObjDto.getId() );
//        Assert.assertEquals( expected.getName(), actualDataObjDto.getName() );
//        Assert.assertEquals( expected.getParentId(), actualDataObjDto.getParentId() );
//        Assert.assertEquals( expected.getCreatedOn(), actualDataObjDto.getCreatedOn() );
//    }
//
//    /**
//     * Should Get Error Message In Response When Manager Get DatObject Return Null.
//     */
//    @Test
//    public void shouldGetErrorMessageInResponseWhenManagerGetDatObjectReturnNull() {
//        DataObjectDTO expected = null;
//        EasyMock.expect( manager.getDataObject( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getDataObjectProperties( DATA_OBJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( Boolean.FALSE, actualResponse.getSuccess() );
//        Assert.assertEquals( expected, actualResponse.getData() );
//        Message messageDto = JsonUtils.jsonToObject( JsonUtils.toJson( actualResponse.getMessage() ), Message.class );
//        Assert.assertEquals( OBJECT_ERROR_MESSAGE, messageDto.getContent() );
//        Assert.assertEquals( MESSAGE_TYPE_ERROR, messageDto.getType() );
//
//    }
//
//    /**
//     * ************************************* Get Project By Id *****************************************************.
//     */
//
//    /**
//     * Should Get SuccessFully DataObject Response When Valid Object id Is Given
//     */
//    @Test
//    public void shouldGetSuccessFullyGetProjectDtoInResponseWhenValidProjectIdIsGiven() {
//        ProjectDTO expected = fillProjectDto();
//        EasyMock.expect( manager.getProject( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getProject( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String dataObjectdto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        ProjectDTO actualProjectObjDto = JsonUtils.jsonToObject( dataObjectdto, ProjectDTO.class );
//
//        Assert.assertEquals( expected.getId(), actualProjectObjDto.getId() );
//        Assert.assertEquals( expected.getName(), actualProjectObjDto.getName() );
//        Assert.assertEquals( expected.getParentId(), actualProjectObjDto.getParentId() );
//        Assert.assertEquals( expected.getCreatedOn(), actualProjectObjDto.getCreatedOn() );
//    }
//
//    /**
//     * Should Get Error Message In Response When Manager Get ProjectDto Return Null.
//     */
//    @Test
//    public void shouldGetErrorMessageInResponseWhenManagerGetProjectDtoReturnNull() {
//        ProjectDTO expected = null;
//        EasyMock.expect( manager.getProject( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getProject( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( Boolean.FALSE, actualResponse.getSuccess() );
//        Assert.assertEquals( expected, actualResponse.getData() );
//        Message messageDto = JsonUtils.jsonToObject( JsonUtils.toJson( actualResponse.getMessage() ), Message.class );
//        Assert.assertEquals( PROJECT_GET_ERROR_MESSAGE, messageDto.getContent() );
//        Assert.assertEquals( MESSAGE_TYPE_ERROR, messageDto.getType() );
//
//    }
//
//    /**
//     * ********************************** Single Object view ****************************************.
//     */
//    /**
//     * Should SuccessFully Get Single Object View UI List When Valid DataObject Id Is Given
//     */
//    @Test
//    public void shouldSuccessFullyGetSingleObjectViewUIListWhenValidDataObjectIdIsGiven() {
//        List< TableColumn > expected = GUIUtils.listColumns( DataObjectDTO.class );
//        EasyMock.expect( manager.getObjectSingleUI( EasyMock.anyString() ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getSingleDataObjectPropertiesUI( DATA_OBJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String actualTabsList = JsonUtils.toJsonString( actualResponse.getData() );
//        List< TableColumn > actualtableCol = JsonUtils.jsonToList( actualTabsList, TableColumn.class );
//
//        for ( TableColumn actualTableColumn : actualtableCol ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//
//        }
//    }
//
//    /**
//     * Should SuccessFully Get List of TableColumn For Object UI Valid DataObjectId Is Given.
//     */
//    @Test
//    public void shouldSuccessFullyGetListOfTableColumnForObjectUIWhenValidDataObjectIdIsGiven() {
//        List< TableColumn > expected = getDummySingleViewForDataObject();
//        EasyMock.expect( manager.getListOfObjectsUITableColumns( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( new TableUI( expected ) ).anyTimes();
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( new ArrayList<>() ).anyTimes();
//
//        mockControl().replay();
//        Response actual = service.listContainerUI( DATA_OBJECT_ID.toString(), DATA_OBJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
//        TableUI actualtableUI = ( TableUI ) JsonUtils.jsonToObject( actualTable, TableUI.class );
//
//        for ( TableColumn actualTableColumn : actualtableUI.getColumns() ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//        }
//    }
//
//    /**
//     * Should Not Get List of TableColumn For Object UI When Manager Return Null For Object UI Columns.
//     */
//    @Test
//    public void shouldNotGetListOfTableColumnForObjectUIWhenManagerReturnNullForObjectUIColumns() {
//        List< TableColumn > expected = null;
//        EasyMock.expect( manager.getListOfObjectsUITableColumns( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( new TableUI( expected ) ).anyTimes();
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( new ArrayList<>() ).anyTimes();
//        mockControl().replay();
//        Response actual = service.listContainerUI( DATA_OBJECT_ID.toString(), DATA_OBJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
//        TableUI actualtableUI = ( TableUI ) JsonUtils.jsonToObject( actualTable, TableUI.class );
//
//        Assert.assertEquals( expected, actualtableUI.getColumns() );
//
//    }
//
//    /**
//     * ******************************** Create Project Form **************************************************.
//     */
//
//    /**
//     * Should sucess fully get create form items list if manager return items list.
//     */
//    @Test
//    public void shouldSucessFullyGetCreateFormItemsListIfManagerReturnItemsList() {
//        List< UIFormItem > expected = prepareEditDummyForm();
//        mockControl().replay();
//        Response actual = service.createProjectForm( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//
//    }
//
//    /**
//     * Should not get create form items list if manager return empty items list.
//     */
//    @Test
//    public void shouldNotGetCreateFormItemsListIfManagerReturnEmptyItemsList() {
//        List< UIFormItem > expected = null;
//        mockControl().replay();
//        Response actual = service.createProjectForm( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( expected, actualResponse.getData() );
//    }
//
//    /**
//     * Should get null as edit form items list if empty paramters are given as input to get create form.
//     */
//    @Test
//    public void shouldGetNullAsEditFormItemsListIfEmptyParamtersAreGivenAsInputToGetCreateForm() {
//        List< UIFormItem > expected = null;
//        mockControl().replay();
//        Response actual = service.createProjectForm( null );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_INTERNAL_SERVER_ERROR, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( expected, actualResponse.getData() );
//
//    }
//
//    /**
//     * Should get null as edit form items list if empty paramters are given as input to get project custom attribute form.
//     */
//    @Test
//    public void shouldGetNullAsEditFormItemsListIfEmptyParamtersAreGivenAsInputToGetProjectCustomAttributeForm() {
//        List< UIFormItem > expected = null;
//        mockControl().replay();
//        Response actual = service.getProjectCustomAttributeUI( null );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( expected, actualResponse.getData() );
//    }
//
//    /**
//     * Should sucess fully get custom attribute form items list if manager return items list.
//     */
//    @Test
//    public void shouldSucessFullyGetCustomAttributeFormItemsListIfManagerReturnItemsList() {
//        List< UIFormItem > expected = prepareEditDummyForm();
//        mockControl().replay();
//        Response actual = service.getProjectCustomAttributeUI( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//
//    }
//
//    /**
//     * Should not get custom attribute form items list if manager return empty items list.
//     */
//    @Test
//    public void shouldNotGetCustomAttributeFormItemsListIfManagerReturnEmptyItemsList() {
//        List< UIFormItem > expected = null;
//        mockControl().replay();
//        Response actual = service.createProjectForm( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( expected, actualResponse.getData() );
//
//    }
//
//    /**
//     * ************************************** Create Project *************************************************.
//     */
//
//    /**
//     * Should success fully create project with valid project json parameter.
//     */
//    @Test
//    public void shouldSuccessFullyCreateProjectWithValidProjectJsonParameter() {
//        ProjectDTO expected = projectDTO;
//        EasyMock.expect( manager.createSuSProject( EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( expected );
//        mockControl().replay();
//        Response actual = service.createProject( WORKFLOW_PROJECT_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//
//        String projectdto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        ProjectDTO actualProjectDto = JsonUtils.jsonToObject( projectdto, ProjectDTO.class );
//
//        Assert.assertEquals( expected.getId(), actualProjectDto.getId() );
//        Assert.assertEquals( expected.getName(), actualProjectDto.getName() );
//        Assert.assertEquals( expected.getDescription(), actualProjectDto.getDescription() );
//        Assert.assertEquals( expected.getParentId(), actualProjectDto.getParentId() );
//        Assert.assertEquals( expected.getCreatedOn(), actualProjectDto.getCreatedOn() );
//
//    }
//
//    /**
//     * Should get error message in create project when manger return null project dto.
//     */
//    @Test
//    public void shouldGetErrorMessageInCreateProjectWhenMangerReturnNullProjectDto() {
//        ProjectDTO expected = null;
//        EasyMock.expect( manager.createSuSProject( EasyMock.anyString(), EasyMock.anyObject( String.class ) ) ).andReturn( expected )
//                .anyTimes();
//        mockControl().replay();
//        Response actual = service.createProject( PROJECT_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNull( actualResponse.getData() );
//        Message messageDto = JsonUtils.jsonToObject( JsonUtils.toJson( actualResponse.getMessage() ), Message.class );
//        Assert.assertEquals( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_PROJECT.getKey() ), messageDto.getContent() );
//        Assert.assertEquals( MESSAGE_TYPE_ERROR, messageDto.getType() );
//
//    }
//
//    /**
//     * ********************************** Single Object Version view ****************************************.
//     */
//
//    /**
//     * Should SuccessFully Get Single Object Version View UI List When Valid DataObject Id And Version Is Given
//     */
//    @Test
//    public void shouldSuccessFullyGetSingleObjectVersionViewUIListWhenValidDataObjectIdAndVersionIsGiven() {
//        List< TableColumn > expected = GUIUtils.listColumns( DataObjectDTO.class, VersionDTO.class );
//        EasyMock.expect( manager.getObjectVersionUI( DATA_OBJECT_ID.toString(), DEFAULT_VERSION_ID ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getDataObjectVersionPropertiesUI( DATA_OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String actualTabsList = JsonUtils.toJsonString( actualResponse.getData() );
//        List< TableColumn > actualtableCol = JsonUtils.jsonToList( actualTabsList, TableColumn.class );
//
//        for ( TableColumn actualTableColumn : actualtableCol ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//
//        }
//    }
//
//    /**
//     * ************************************** Delete Object *******************************************.
//     */
//
//    /**
//     * Should successfully delete object and dependencies when manager throw no exception.
//     */
//    @Test
//    public void shouldSuccessfullyDeleteObjectAndDependenciesWhenManagerThrowNoException() {
//
//        mockControl().replay();
//        Response actual = service.deleteObjectById( UUID.randomUUID().toString(), DELETE_MODE );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        System.out.println( actualResponse.getData() );
//        Assert.assertEquals( MessageBundleFactory.getMessage( Messages.OBJECT_AND_DEPENDENCIES_DELETED_SUCCESSFULLY.getKey() ),
//                actualResponse.getMessage().getContent() );
//    }
//
//    /**
//     * ******************************** Edit Object Options Form **************************************************.
//     */
//
//    /**
//     * ******************************** Create Object **************************************************.
//     */
//
//    /**
//     * Should get error response in create object when null object is rerturned from manager.
//     */
//    @Test
//    public void shouldGetErrorResponseInCreateObjectWhenNullObjectIsRerturnedFromManager() {
//        EasyMock.expect( manager.createSuSObject( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(),
//                EasyMock.anyBoolean(), null ) ).andReturn( null ).anyTimes();
//        mockControl().replay();
//        Response actual = service.createObject( UUID.randomUUID().toString(), UUID.randomUUID().toString(), null );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNull( actualResponse.getData() );
//        Assert.assertEquals( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT.getKey() ),
//                actualResponse.getMessage().getContent() );
//
//    }
//
//    /**
//     * Should successfully return created object when created object is returned from manager in create object.
//     */
//    @Test
//    public void shouldSuccessfullyReturnCreatedObjectWhenCreatedObjectIsReturnedFromManagerInCreateObject() {
//        DataObjectDTO expectedObject = fillDataObjectDto();
//        EasyMock.expect( manager.createSuSObject( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(),
//                EasyMock.anyBoolean(), null ) ).andReturn( expectedObject ).anyTimes();
//        mockControl().replay();
//        Response actual = service.createObject( UUID.randomUUID().toString(), UUID.randomUUID().toString(), null );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//
//        Assert.assertNotNull( actualResponse.getData() );
//
//        Assert.assertEquals( expectedObject, JsonUtils.linkedMapObjectToClassObject( actualResponse.getData(), DataObjectDTO.class ) );
//    }
//
//    /**
//     * ******************************** Create Object Form **************************************************.
//     */
//
//    /**
//     * Should successfully create object form items list if manager return items list.
//     */
//    @Test
//    public void shouldSuccessfullyCreateObjectFormItemsListIfManagerReturnItemsList() {
//        List< UIFormItem > expected = GUIUtils.prepareForm( true, new DataObjectDTO() );
//        mockControl().replay();
//        Response actual = service.createObjectForm( PROJECT_ID.toString(), UUID.randomUUID().toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//
//        Assert.assertNotNull( actualResponse.getData() );
//
//        List< UIFormItem > actualUIColumnList = ( List< UIFormItem > ) actualResponse.getData();
//        Assert.assertEquals( expected.size(), actualUIColumnList.size() );
//        for ( int i = 0; i < expected.size(); i++ ) {
//            Assert.assertEquals( expected.get( i ),
//                    JsonUtils.linkedMapObjectToClassObject( actualUIColumnList.get( i ), UIFormItem.class ) );
//        }
//    }
//
//    /**
//     * ******************************** Get All Objects **************************************************.
//     */
//
//    /**
//     * Should successfully get all data objects by project id when manager return paginated response.
//     */
//    @Test
//    public void shouldSuccessfullyGetAllDataObjectsByProjectIdWhenManagerReturnPaginatedResponse() {
//        DataObjectDTO expectedDataObjectDTO = fillDataObjectDto();
//        FiltersDTO filtersDTO = fillFilterDTO();
//        FilteredResponse< Object > filteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO,
//                Arrays.asList( expectedDataObjectDTO ) );
//        EasyMock.expect( manager.getAllChildObjectsByTypeId( EasyMock.anyString(), EasyMock.anyObject( UUID.class ), EasyMock.anyString(),
//                EasyMock.anyObject( FiltersDTO.class ), null ) ).andReturn( filteredResponse ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getAllSubContainersByProjectId( PROJECT_ID.toString(), PROJECT_ID.toString(),
//                JsonUtils.toJson( filtersDTO ) );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//
//        Assert.assertNotNull( actualResponse.getData() );
//
//        Object actualFilteredResponse = actualResponse.getData();
//        FilteredResponse< DataObjectDTO > parseObject = new FilteredResponse<>();
//        FilteredResponse< DataObjectDTO > actualFilteredResponseDto = JsonUtils.linkedMapObjectToClassObject( actualFilteredResponse,
//                parseObject.getClass() );
//        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_ONE, actualFilteredResponseDto.getData().size() );
//    }
//
//    /**
//     * ***************** getFilteredDataObjectVersionsList ************************.
//     */
//
//    /**
//     * Should SuccessFully Get Filtered Version List Of DataObjects When Valid Filter Json Is Given As Input
//     */
//    @Test
//    public void shouldSuccessFullyGetFilteredVersionListOfDataObjectsWhenValidFilterJsonIsGivenAsInput() {
//        List< DataObjectDTO > expectedList = new ArrayList<>();
//        expectedList.add( fillDataObjectDto() );
//
//        FilteredResponse< Object > expected = ( FilteredResponse< Object > ) getFilledFilteredResponse( expectedList );
//
//        EasyMock.expect( manager.getObjectVersions( EasyMock.anyString(), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expected ).anyTimes();
//
//        mockControl().replay();
//        Response actual = service.getFilteredObjectVersionsList( DATA_OBJECT_ID.toString(), FILTER_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( actual.getStatus(), HttpStatus.SC_OK );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        FilteredResponse< DataObjectDTO > actualDto = JsonUtils.jsonToObject( dataObjDto, FilteredResponse.class );
//        List< DataObjectDTO > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( actualDto.getData() ), DataObjectDTO.class );
//        Assert.assertNotNull( actualList );
//        DataObjectDTO expectedObject = ( DataObjectDTO ) expected.getData().get( FIRST_INDEX );
//        Assert.assertEquals( expectedObject.getId(), actualList.get( FIRST_INDEX ).getId() );
//        Assert.assertEquals( expectedObject.getName(), actualList.get( FIRST_INDEX ).getName() );
//        Assert.assertEquals( expectedObject.getParentId(), actualList.get( FIRST_INDEX ).getParentId() );
//        Assert.assertEquals( expectedObject.getCreatedOn(), actualList.get( FIRST_INDEX ).getCreatedOn() );
//
//    }
//
//    /**
//     * Should Get Null As Filtered Version List Of DataObjects When Manager Return Null As Version List.
//     */
//    @Test
//    public void shouldGetNullAsFilteredVersionListOfDataObjectsWhenManagerReturnNullAsVersionList() {
//
//        FilteredResponse< Object > expected = null;
//
//        EasyMock.expect( manager.getObjectVersions( EasyMock.anyString(), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expected ).anyTimes();
//
//        mockControl().replay();
//        Response actual = service.getFilteredObjectVersionsList( DATA_OBJECT_ID.toString(), FILTER_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( actualResponse.getData(), expected );
//
//    }
//
//    /**
//     * Should Throw Json Exception When Invalid Json Is Given To Get Filtered Versions List.
//     */
//    @Test
//    public void shouldThrowJsonExceptionWhenInvalidJsonIsGivenToGetFilteredVersionsList() {
//
//        Response actual = service.getFilteredObjectVersionsList( DATA_OBJECT_ID.toString(), INVALID_FILTER_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNull( actualResponse.getData() );
//        Assert.assertEquals( Boolean.FALSE, actualResponse.getSuccess() );
//        Message messageDto = JsonUtils.jsonToObject( JsonUtils.toJson( actualResponse.getMessage() ), Message.class );
//        Assert.assertEquals( INVALID_PARAM_ERROR_MESSAGE, messageDto.getContent() );
//        Assert.assertEquals( MESSAGE_TYPE_ERROR, messageDto.getType() );
//
//    }
//
//    /**
//     * ***************** getDataObjectVersionsUI *************************.
//     */
//
//    /**
//     * Should Successfully Get Version Table View for Valid DataObject Class Instance
//     */
//    @Test
//    public void shouldSuccessfullyGetVersionTableViewforValidDataObjectClassInstance() {
//        List< TableColumn > expected = GUIUtils.listColumns( DataObjectDTO.class );
//        EasyMock.expect( manager.getDataObjectVersionsUI( EasyMock.anyString() ) ).andReturn( expected ).anyTimes();
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( new ArrayList<>() ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getDataObjectVersionsUI( DATA_OBJECT_ID.toString() );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String actualJsonString = JsonUtils.toJsonString( actualResponse.getData() );
//        TableUI actualtable = JsonUtils.jsonToObject( actualJsonString, TableUI.class );
//
//        for ( TableColumn actualTableColumn : actualtable.getColumns() ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//
//        }
//    }
//
//    /**
//     * ***************** getSingleDataObjectVersionUI *************************.
//     */
//
//    /**
//     * Should Successfully Get Tab Views When Valid DataObject Id And Version Is Provided As Input Parameters
//     */
//    @Test
//    public void shouldSuccessfullyGetTabViewsWhenValidDataObjectIdAndVersionIsProvidedAsInputParameters() {
//        List< SubTabsUI > expected = getGeneralTabsList();
//        SubTabsItem expectedObject = new SubTabsItem();
//        expectedObject.setTabs( expected );
//        EasyMock.expect( manager.getTabsViewDataObjectVersionUI( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyInt() ) )
//                .andReturn( expectedObject ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getSingleDataObjectVersionUI( DATA_OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String actualTabsList = JsonUtils.toJsonString( actualResponse.getData() );
//        SubTabsItem actualtabs = JsonUtils.jsonToObject( actualTabsList, SubTabsItem.class );
//        Assert.assertEquals( expectedObject.getTabs(), actualtabs.getTabs() );
//
//    }
//
//    /**
//     * ***************** getDataObjectByIdAndVersion *************************.
//     */
//
//    /**
//     * Should Successfully Get Object When Valid Input Object Id Is Given In Parameter
//     */
//    @Test
//    public void shouldSuccessfullyGetObjectWhenValidInputObjectIdIsGivenInParameter() {
//        DataObjectDTO expected = fillDataObjectDto();
//
//        EasyMock.expect( manager.getObjectByIdAndVersion( EasyMock.anyString(), EasyMock.anyObject( VersionPrimaryKey.class ) ) )
//                .andReturn( expected ).anyTimes();
//
//        mockControl().replay();
//        Response actual = service.getObjectByIdAndVersion( DATA_OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String projectdto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        DataObjectDTO actualDataobjectDto = JsonUtils.jsonToObject( projectdto, DataObjectDTO.class );
//
//        Assert.assertEquals( expected.getId(), actualDataobjectDto.getId() );
//        Assert.assertEquals( expected.getName(), actualDataobjectDto.getName() );
//        Assert.assertEquals( expected.getParentId(), actualDataobjectDto.getParentId() );
//        Assert.assertEquals( expected.getCreatedOn(), actualDataobjectDto.getCreatedOn() );
//
//    }
//
//    /**
//     * Should Throw Illegal Argument Exception On Get Object When Invalid Input ObjectId Is Given As Parameter.
//     */
//    @Test
//    public void shouldThrowIllegalArgumentExceptionOnGetObjectWhenInvalidInputObjectIdIsGivenAsParameter() {
//
//        thrown.expect( IllegalArgumentException.class );
//        thrown.expectMessage( INVALID_UUID_ERROR_MESSAGE + INVALID_DATA_OBJECT_ID );
//        service.getObjectByIdAndVersion( INVALID_DATA_OBJECT_ID, DEFAULT_VERSION_ID );
//
//    }
//
//    /**
//     * Should Not Get Object By Id And Version When Manager Return Null As Object.
//     */
//    @Test
//    public void shouldNotGetObjectByIdAndVersionWhenManagerReturnNullAsObject() {
//        DataObjectDTO expected = null;
//
//        EasyMock.expect( manager.getObjectByIdAndVersion( EasyMock.anyString(), EasyMock.anyObject( VersionPrimaryKey.class ) ) )
//                .andReturn( expected ).anyTimes();
//
//        mockControl().replay();
//        Response actual = service.getObjectByIdAndVersion( DATA_OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( actualResponse.getData(), expected );
//
//    }
//
//    /**
//     * Should save view successfully when valid object view payload is given.
//     */
//    /*@Test
//    public void shouldSaveViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
//        ObjectViewDTO expected = prepareObjectViewDTO();
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
//                .andReturn( expected );
//        mockControl.replay();
//        Response actual = service.saveProjectView( PROJECT_ID.toString(), OBJECT_VIEW_PAYLOAD );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
//        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
//        Assert.assertEquals( expected.getObjectViewName(), objectViewDTO.getObjectViewName() );
//        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
//        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
//    }*/
//
//    /**
//     * Should get bad request response when payload is invalid for saving object view.
//     */
//    /* @Test
//    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForSavingObjectView() {
//
//        final Response response = service.saveProjectView( PROJECT_ID.toString(), OBJECT_VIEW_INVALID_PAYLOAD );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
//
//    }*/
//
//    /**
//     * Should update view successfully when valid object view payload is given.
//     */
//    /* @Test
//    public void shouldUpdateViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
//        ObjectViewDTO expected = prepareObjectViewDTO();
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
//                .andReturn( expected );
//        mockControl.replay();
//        Response actual = service.updateProjectView( PROJECT_ID.toString(), OBJECT_VIEW_ID, OBJECT_VIEW_PAYLOAD );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
//        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
//        Assert.assertEquals( expected.getObjectViewName(), objectViewDTO.getObjectViewName() );
//        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
//        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
//    }*/
//
//    /**
//     * Should get bad request response when payload is invalid for updating object view.
//     *//*
//       @Test
//       public void shouldGetBadRequestResponseWhenPayloadIsInvalidForUpdatingObjectView() {
//
//       final Response response = service.updateProjectView( PROJECT_ID.toString(), OBJECT_VIEW_ID, OBJECT_VIEW_INVALID_PAYLOAD );
//       Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
//       }*/
//
//    /**
//     * Should get all views successfully when valid object view key is provided.
//     */
//    /* @Test
//    public void shouldGetAllViewsSuccessfullyWhenValidObjectViewKeyIsProvided() {
//
//        ObjectViewDTO objectViewDTO = prepareObjectViewDTO();
//        List< ObjectViewDTO > expectedResponse = Arrays.asList( objectViewDTO );
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( expectedResponse );
//
//        mockControl.replay();
//        Response actual = service.getAllProjectViews( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
//        List< ObjectViewDTO > list = ( List< ObjectViewDTO > ) JsonUtils.jsonToList( objectViewDTOstr, ObjectViewDTO.class );
//        Assert.assertEquals( objectViewDTO.getObjectViewName(), list.get( INDEX ).getObjectViewName() );
//        Assert.assertEquals( objectViewDTO.getObjectViewJson(), list.get( INDEX ).getObjectViewJson() );
//        Assert.assertEquals( objectViewDTO.getObjectViewKey(), list.get( INDEX ).getObjectViewKey() );
//    }*/
//
//    /**
//     * Should delete view successfully when valid view id provided.
//     */
//    /* @Test
//    public void shouldDeleteViewSuccessfullyWhenValidViewIdProvided() {
//
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( true );
//        mockControl.replay();
//        Response actual = service.deleteProjectView( PROJECT_ID.toString(), OBJECT_VIEW_ID );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( actualResponse.getMessage().getContent(),
//                MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ) );
//    }*/
//
//    /**
//     * Should not delete view successfully when in valid view id provided.
//     *//*
//       @Test
//       public void shouldNotDeleteViewSuccessfullyWhenInValidViewIdProvided() {
//
//       EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//       EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( false );
//       mockControl.replay();
//       Response actual = service.deleteProjectView( PROJECT_ID.toString(), OBJECT_VIEW_ID );
//       Assert.assertNotNull( actual );
//       Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//       SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//       Assert.assertEquals( actualResponse.getMessage().getContent(),
//       MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
//       }*/
//
//    /**
//     * Should update view as default successfully when valid view id is given.
//     */
//    /*@Test
//    public void shouldUpdateViewAsDefaultSuccessfullyWhenValidViewIdIsGiven() {
//        ObjectViewDTO expected = prepareObjectViewDTO();
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
//                .andReturn( expected );
//        mockControl.replay();
//        Response actual = service.setProjectViewAsDefault( PROJECT_ID.toString(), OBJECT_VIEW_ID );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//    }*/
//
//    /**
//     * Should throw exception while getting data object by id.
//     */
//    @Test
//    public void shouldThrowExceptionWhileGettingDataObjectById() {
//
//        EasyMock.expect( manager.getDataObject( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getDataObjectProperties( DATA_OBJECT_ID.toString() );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * ************************************ HELPER METHODS **************************************.
//     *
//     * @return the filters DTO
//     */
//
//    /**
//     * Prepare filter for max ten number of children.
//     *
//     * @return the filters DTO
//     */
//    private FiltersDTO fillFilterDTO() {
//        FiltersDTO filtersDTO = new FiltersDTO( ConstantsInteger.INTEGER_VALUE_ONE, ConstantsInteger.INTEGER_VALUE_ZERO,
//                ConstantsInteger.INTEGER_VALUE_ZERO, FILTERED_RECORDS );
//        FilterColumn filterColumn = new FilterColumn();
//        filterColumn.setName( FIELD_NAME_MODIFIED_ON );
//        filterColumn.setDir( ConstantsString.SORTING_DIRECTION_DESCENDING );
//        filtersDTO.setColumns( Arrays.asList( filterColumn ) );
//
//        return filtersDTO;
//    }
//
//    /**
//     * ************************************ HELPER METHODS **************************************.
//     */
//
//    /**
//     * Should throw exception while getting custom attributes when some thing goes wrong.
//     */
//    @Test
//    public void shouldThrowExceptionWhileGettingCustomAttributes() {
//
//        EasyMock.expect( manager.getProjectCustomAttributeUI( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getProjectCustomAttributeUI( PROJECT_ID.toString() );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * ************************************** Create Project *************************************************.
//     */
//
//    /**
//     * Should throw exception while creating project when something goes wrong.
//     */
//    @Test
//    public void shouldThrowExceptionWhileCreatingProject() {
//
//        EasyMock.expect( manager.createSuSProject( EasyMock.anyString(), EasyMock.anyObject( String.class ) ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.createProject( WORKFLOW_PROJECT_JSON );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * ************************************** Delete Object *******************************************.
//     */
//
//    /**
//     * Should throw Exception while deleting object by id when some thing goes wrong.
//     */
//    @Test
//    public void shouldThrowExceptionWhileDeletingObjectById() {
//
//        mockControl.replay();
//        Response actual = service.deleteObjectById( UUID.randomUUID().toString(), DELETE_MODE );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * ******************************** Create Object **************************************************.
//     */
//
//    /**
//     * Should throw Exception while creating object form when some thing goes wrong.
//     */
//    @Test
//    public void shouldThrowExceptionWhileCreatingObject() {
//
//        EasyMock.expect( manager.createSuSObject( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(),
//                EasyMock.anyBoolean(), null ) ).andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.createObject( UUID.randomUUID().toString(), UUID.randomUUID().toString(), null );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * ******************************** Create Object Form **************************************************.
//     */
//
//    /**
//     * Should throw exception while creating object options UI form.
//     */
//    @Test
//    public void shouldThrowExceptionWhileCreatingObjectOptionsUIForm() {
//        EasyMock.expect( manager.createObjectForm( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.createObjectForm( PROJECT_ID.toString(), UUID.randomUUID().toString() );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * ***************** getDataObjectByIdAndVersion *************************.
//     */
//
//    /**
//     * Should throw exception while getting data object by id and version when something goes wrong.
//     */
//    @Test
//    public void shouldThrowExceptionWhileGettingDataObjectByIdAndVersion() {
//
//        EasyMock.expect( manager.getObjectByIdAndVersion( EasyMock.anyString(), EasyMock.anyObject( VersionPrimaryKey.class ) ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getObjectByIdAndVersion( DATA_OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should throw Exception while getting all project views when some thing goes wrong.
//     */
//    /* @Test
//    public void shouldThrowExceptionWhileGettingAllProjectViews() {
//
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getAllProjectViews( PROJECT_ID.toString() );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }*/
//
//    /**
//     * Should throw Exception while deleting project view form when some thing goes wrong.
//     */
//    /* @Test
//    public void shouldThrowExceptionWhileDeletingProjectView() {
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.deleteProjectView( PROJECT_ID.toString(), OBJECT_VIEW_ID );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }*/
//
//    /**
//     * Should throw Exception while setting project view as default when some thing goes wrong.
//     */
//    /* @Test
//    public void shouldThrowExceptionWhileSettingProjectViewAsDefault() {
//
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.saveDefaultObjectView( EasyMock.anyObject( UUID.class ), EasyMock.anyString(),
//                EasyMock.anyString(), EasyMock.anyString() ) ).andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.setProjectViewAsDefault( PROJECT_ID.toString(), OBJECT_VIEW_ID );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }*/
//
//    /**
//     * Should successfully add meta data to object when valid object id is provided.
//     */
//    @Test
//    public void shouldSuccessfullyAddMetaDataToObjectWhenValidObjectIdIsProvided() {
//
//        ObjectMetaDataDTO expectedObject = prepareObjectMetaDataDTO();
//        EasyMock.expect( manager.addMetaDataToAnObject( EasyMock.anyString(), EasyMock.anyObject( ObjectMetaDataDTO.class ),
//                EasyMock.anyString(), EasyMock.anyBoolean() ) ).andReturn( expectedObject ).anyTimes();
//        mockControl.replay();
//        Response actual = service.addMetaDataToAnObject( PROJECT_ID.toString(), OBJECT_META_DATA_PAYLOAD );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String objectMetaDTOJson = JsonUtils.objectToJson( actualResponse.getData() );
//        ObjectMetaDataDTO actualObject = JsonUtils.jsonToObject( objectMetaDTOJson, ObjectMetaDataDTO.class );
//        Assert.assertEquals( expectedObject.getMetadata().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getValue(),
//                actualObject.getMetadata().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getValue() );
//    }
//
//    /**
//     * Should successfully get meta data list when valid object id is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetMetaDataListWhenValidObjectIdIsProvided() {
//        ObjectMetaDataDTO expectedMetaDataDTO = prepareObjectMetaDataDTO();
//        FiltersDTO filtersDTO = fillFilterDTO();
//        FilteredResponse< MetaDataEntryDTO > filteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO,
//                expectedMetaDataDTO.getMetadata() );
//        EasyMock.expect(
//                        manager.getObjectMetaDataList( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject( FiltersDTO.class ) ) )
//                .andReturn( filteredResponse ).anyTimes();
//        mockControl.replay();
//        Response actual = service.getObjectMetaDatalist( PROJECT_ID.toString(), FILTER_JSON );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        FilteredResponse< MetaDataEntryDTO > actualFilteredResponseDto = JsonUtils.linkedMapObjectToClassObject(
//                JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData(),
//                new FilteredResponse<>().getClass() );
//        List< MetaDataEntryDTO > actualMetaDataEntryDTO = ( List< MetaDataEntryDTO > ) actualFilteredResponseDto.getData();
//        Assert.assertEquals( actualMetaDataEntryDTO.size(), expectedMetaDataDTO.getMetadata().size() );
//    }
//
//    /**
//     * Should get bad request response when invalid payload is passed while getting meta data list.
//     */
//    @Test
//    public void shouldGetBadRequestResponseWhenInvalidPayloadIsPassedWhileGettingMetaDataList() {
//        Response actual = service.getObjectMetaDatalist( PROJECT_ID.toString(), INVALID_FILTER_JSON );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );
//    }
//
//    /**
//     * Should successfully get meta data list when valid object id and version id is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetMetaDataListWhenValidObjectIdAndVersionIdIsProvided() {
//        ObjectMetaDataDTO expectedMetaDataDTO = prepareObjectMetaDataDTO();
//        FiltersDTO filtersDTO = fillFilterDTO();
//        FilteredResponse< MetaDataEntryDTO > filteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO,
//                expectedMetaDataDTO.getMetadata() );
//        EasyMock.expect( manager.getObjectMetaDataListByVersion( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyInt(),
//                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( filteredResponse ).anyTimes();
//        mockControl.replay();
//        Response actual = service.getObjectMetaDatalistByVersion( PROJECT_ID.toString(), DEFAULT_VERSION_ID, FILTER_JSON );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        FilteredResponse< MetaDataEntryDTO > actualFilteredResponseDto = JsonUtils.linkedMapObjectToClassObject(
//                JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData(),
//                new FilteredResponse<>().getClass() );
//        List< MetaDataEntryDTO > actualMetaDataEntryDTO = ( List< MetaDataEntryDTO > ) actualFilteredResponseDto.getData();
//        Assert.assertEquals( actualMetaDataEntryDTO.size(), expectedMetaDataDTO.getMetadata().size() );
//    }
//
//    /**
//     * Should successfully create object meta data form items.
//     */
//    @Test
//    public void shouldSuccessfullyCreateObjectMetaDataFormItems() {
//        List< UIFormItem > expected = GUIUtils.prepareForm( true, new ObjectMetaDataDTO(), new MetaDataEntryDTO() );
//        mockControl().replay();
//        Response actual = service.createMetaDataForm( OBJECT_ID.toString() );
//        List< UIFormItem > actualUIColumnList = ( List< UIFormItem > ) JsonUtils
//                .jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData();
//        Assert.assertEquals( expected.size(), actualUIColumnList.size() );
//    }
//
//    /**
//     * Should should not get list when nullis passed to create object meta data form items.
//     */
//    @Test
//    public void shouldShouldNotGetListWhenNullisPassedToCreateObjectMetaDataFormItems() {
//        List< UIFormItem > expected = null;
//        mockControl().replay();
//        Response actual = service.createMetaDataForm( OBJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( expected, actualResponse.getData() );
//
//    }
//
//    /**
//     * Should success fully get list of table column for object meta data UI when valid object id is given.
//     */
//    @Test
//    public void shouldSuccessFullyGetListOfTableColumnForObjectMetaDataUIWhenValidObjectIdIsGiven() {
//        List< TableColumn > expected = GUIUtils.listColumns( MetaDataEntryDTO.class );
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( new ArrayList<>() ).anyTimes();
//        EasyMock.expect( manager.getObjectMetaDataTableUI( EasyMock.anyString() ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getObjectMetaDataTableUI( DATA_OBJECT_ID.toString() );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
//        for ( TableColumn actualTableColumn : JsonUtils.jsonToObject( actualTable, TableUI.class ).getColumns() ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//        }
//    }
//
//    /**
//     * Should successfully get meta data context when valid filter is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetMetaDataContextWhenValidFilterIsProvided() {
//        List< ContextMenuItem > expectedList = new ArrayList<>();
//        expectedList.add( new ContextMenuItem( TEST_CONTEXT_URL, TEST_CONTEXT_ICON, TEST_CONTEXT_TITLE ) );
//        EasyMock.expect( manager.getMetaDataContextRouter( EasyMock.anyString(), EasyMock.anyObject( FiltersDTO.class ) ) )
//                .andReturn( expectedList ).anyTimes();
//        mockControl.replay();
//        Response actual = service.getObjectMetaDataContext( PROJECT_ID.toString(), FILTER_JSON );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        List< ContextMenuItem > actualList = JsonUtils.jsonToList(
//                JsonUtils.toJsonString( JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData() ),
//                ContextMenuItem.class );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should successfully delete meta data entry when valid delete mode is provided.
//     */
//    @Test
//    public void shouldSuccessfullyDeleteMetaDataEntryWhenValidDeleteModeIsProvided() {
//        EasyMock.expect( manager.deleteMetaDataBySelection( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(),
//                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
//        mockControl.replay();
//        Response actual = service.deleteMetaDataBySelection( PROJECT_ID.toString(), META_DATA_KEY, ConstantsMode.SINGLE );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( MessageBundleFactory.getMessage( Messages.RECORD_DELETED.getKey() ),
//                actualResponse.getMessage().getContent() );
//    }
//
//    /**
//     * Should throw exception while deleting meta data when some exception occurs.
//     */
//    @Test
//    public void shouldThrowExceptionWhileDeletingMetaData() {
//
//        EasyMock.expect( manager.deleteMetaDataBySelection( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(),
//                EasyMock.anyString() ) ).andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.deleteMetaDataBySelection( PROJECT_ID.toString(), META_DATA_KEY, ConstantsMode.SINGLE );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should successfully prepare meta dafa form for edit when valid payload is provided.
//     */
//    @Test
//    public void shouldSuccessfullyPrepareMetaDafaFormForEditWhenValidPayloadIsProvided() {
//        MetaDataEntryDTO expected = prepareObjectMetaDataDTO().getMetadata().get( ConstantsInteger.INTEGER_VALUE_ZERO );
//        List< UIFormItem > expectedList = GUIUtils.prepareForm( false, expected );
//        mockControl().replay();
//        Response actual = service.createMetaDataFormForEdit( DATA_OBJECT_ID.toString(), META_DATA_KEY );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
//        List< UIFormItem > actualList = JsonUtils.jsonToList( actualTable, UIFormItem.class );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should successfully update meta data to object when valid object id is provided.
//     */
//    @Test
//    public void shouldSuccessfullyUpdateMetaDataToObjectWhenValidObjectIdIsProvided() {
//
//        ObjectMetaDataDTO expectedObject = prepareObjectMetaDataDTO();
//        EasyMock.expect( manager.addMetaDataToAnObject( EasyMock.anyString(), EasyMock.anyObject( ObjectMetaDataDTO.class ),
//                EasyMock.anyString(), EasyMock.anyBoolean() ) ).andReturn( expectedObject ).anyTimes();
//        mockControl.replay();
//        Response actual = service.updateMetaDataToAnObject( PROJECT_ID.toString(), OBJECT_META_DATA_PAYLOAD );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String objectMetaDTOJson = JsonUtils.objectToJson( actualResponse.getData() );
//        ObjectMetaDataDTO actualObject = JsonUtils.jsonToObject( objectMetaDTOJson, ObjectMetaDataDTO.class );
//        Assert.assertEquals( expectedObject.getMetadata().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getValue(),
//                actualObject.getMetadata().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getValue() );
//    }
//
//    /**
//     * Should get bad request response when invalid payload is passed while update meta data to object.
//     */
//    @Test
//    public void shouldGetBadRequestResponseWhenInvalidPayloadIsPassedWhileUpdateMetaDataToObject() {
//        Response actual = service.updateMetaDataToAnObject( PROJECT_ID.toString(), ConstantsString.EMPTY_STRING );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );
//    }
//
//    /**
//     * Should sucess fully get context menu O bject option items list.
//     */
//    @Test
//    public void shouldSucessFullyGetContextMenuOBjectOptionItemsList() {
//        List< UIFormItem > expected = prepareEditDummyForm();
//        mockControl().replay();
//        Response actual = service.getObjectOptionForm( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//    }
//
//    /**
//     * Should get error mesage get context menu O bject option items list.
//     */
//    @Test
//    public void shouldGetErrorMesageGetContextMenuOBjectOptionItemsList() {
//        EasyMock.expect( manager.getObjectOptionForm( EasyMock.anyString(), EasyMock.anyObject() ) )
//                .andThrow( new SusException( OBJECT_TYPE_ERROR_MESSAGE ) ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getObjectOptionForm( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        Assert.assertEquals( OBJECT_TYPE_ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should successfully update object permission when valid id and option is provided.
//     */
//    @Test
//    public void shouldSuccessfullyUpdateObjectPermissionWhenValidIdAndOptionIsProvided() {
//        List< UIFormItem > expected = prepareEditDummyForm();
//        mockControl().replay();
//        Response actual = service.getUpdateObjectPermissionUI( PROJECT_ID.toString(), "option" );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//
//        Assert.assertNotNull( actualResponse.getData() );
//
//        String listUIColumnStr = JsonUtils.toJson( actualResponse.getData() );
//        List< SelectFormItem > actualUIColumnList = JsonUtils.jsonToList( listUIColumnStr, SelectFormItem.class );
//        Assert.assertEquals( expected.size(), actualUIColumnList.size() );
//    }
//
//    /**
//     * Should change permission of objects when valid filter is provided.
//     */
//    @Test
//    public void shouldChangePermissionOfObjectsWhenValidFilterIsProvided() {
//        FiltersDTO filtersDTO = populateFilterDTO();
//        EasyMock.expect( manager.changeObjectPermissions( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject( Map.class ) ) )
//                .andReturn( true ).anyTimes();
//        mockControl().replay();
//        Response actual = service.changeObjectPermissions( PROJECT_ID.toString(), JsonUtils.toJson( filtersDTO, FiltersDTO.class ) );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//    }
//
//    /**
//     * Should preparepermission object options form when valid project ID provided.
//     */
//    @Test
//    public void shouldPreparepermissionObjectOptionsFormWhenValidProjectIDProvided() {
//        List< SelectFormItem > expected = fillSelectUI( SET_INHERITED );
//        EasyMock.expect( manager.permissionObjectOptionsForm( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.permissionObjectOptionsForm( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//    }
//
//    /* Deleted object views test cases start */
//
//    /**
//     * Should save deleted object view successfully when valid object view payload is given.
//     */
//    @Test
//    public void shouldSaveDeletedObjectViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
//        ObjectViewDTO expected = prepareObjectViewDTO();
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
//                .andReturn( expected );
//        mockControl.replay();
//        Response actual = service.saveDeletedObjectView( OBJECT_VIEW_PAYLOAD );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
//        ObjectViewDTO actualObject = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
//        Assert.assertEquals( expected, actualObject );
//    }
//
//    /**
//     * Should get bad request response when payload is invalid for saving deleted object view.
//     */
//    @Test
//    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForSavingDeletedObjectView() {
//
//        final Response response = service.saveDeletedObjectView( OBJECT_VIEW_INVALID_PAYLOAD );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
//
//    }
//
//    /**
//     * Should update deleted object view successfully when valid object view payload is given.
//     */
//    @Test
//    public void shouldUpdateDeletedObjectViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
//        ObjectViewDTO expected = prepareObjectViewDTO();
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
//                .andReturn( expected );
//        mockControl.replay();
//        Response actual = service.updateDeletedObjectView( OBJECT_VIEW_ID, OBJECT_VIEW_PAYLOAD );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
//        ObjectViewDTO actualObject = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
//        Assert.assertEquals( expected, actualObject );
//    }
//
//    /**
//     * Should get bad request response when payload is invalid for updating deleted object view.
//     */
//    @Test
//    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForUpdatingDeletedObjectView() {
//
//        final Response response = service.updateDeletedObjectView( OBJECT_VIEW_ID, OBJECT_VIEW_INVALID_PAYLOAD );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
//    }
//
//    /**
//     * Should get all deleted object views successfully when valid object view key is provided.
//     */
//    @Test
//    public void shouldGetAllDeletedObjectViewsSuccessfullyWhenValidObjectViewKeyIsProvided() {
//        ObjectViewDTO objectViewDTO = prepareObjectViewDTO();
//        List< ObjectViewDTO > expectedList = new ArrayList<>();
//        expectedList.add( objectViewDTO );
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( expectedList );
//        mockControl.replay();
//        Response actual = service.getAllDeletedObjectViews();
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
//        List< ObjectViewDTO > actualList = ( List< ObjectViewDTO > ) JsonUtils.jsonToList( objectViewDTOstr, ObjectViewDTO.class );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should throw exception while getting all deleting object view when some problem occurs.
//     */
//    @Test
//    public void shouldThrowExceptionWhileGettingAllDeletingObjectView() {
//
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andThrow( new SusException( OBJECT_TYPE_ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getAllDeletedObjectViews();
//        Assert.assertEquals( OBJECT_TYPE_ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should delete deleted object view successfully when valid view id provided.
//     */
//    @Test
//    public void shouldDeleteDeletedObjectViewSuccessfullyWhenValidViewIdProvided() {
//
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( true );
//        mockControl.replay();
//        Response actual = service.deleteDeletedObjectView( OBJECT_VIEW_ID );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( actualResponse.getMessage().getContent(),
//                MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ) );
//    }
//
//    /**
//     * Should not delete deleted object view successfully when in valid view id provided.
//     */
//    @Test
//    public void shouldNotDeleteDeletedObjectViewSuccessfullyWhenInValidViewIdProvided() {
//
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( false );
//        mockControl.replay();
//        Response actual = service.deleteDeletedObjectView( OBJECT_VIEW_ID );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( actualResponse.getMessage().getContent(),
//                MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
//    }
//
//    /**
//     * Should throw exception while deleting an object view when some problem occurs.
//     */
//    @Test
//    public void shouldThrowExceptionWhileDeletingAnObjectViewWhenExceptionOccurs() {
//
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) )
//                .andThrow( new SusException( OBJECT_TYPE_ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.deleteDeletedObjectView( OBJECT_VIEW_ID );
//        Assert.assertEquals( OBJECT_TYPE_ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should update deleted object view as default successfully when valid view id is given.
//     */
//    @Test
//    public void shouldUpdateDeletedObjectViewAsDefaultSuccessfullyWhenValidViewIdIsGiven() {
//        ObjectViewDTO expected = prepareObjectViewDTO();
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
//                .andReturn( expected );
//        mockControl.replay();
//        Response actual = service.setDeletedObjectViewAsDefault( OBJECT_VIEW_ID );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//    }
//
//    /**
//     * Should throw exception while setting an object view as default when some problem occurs.
//     */
//    @Test
//    public void shouldThrowExceptionWhileSettingAnObjectViewAsDefault() {
//
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.saveDefaultObjectView( EasyMock.anyObject( UUID.class ), EasyMock.anyString(),
//                EasyMock.anyString(), EasyMock.anyString() ) ).andThrow( new SusException( OBJECT_TYPE_ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.setDeletedObjectViewAsDefault( OBJECT_VIEW_ID );
//        Assert.assertEquals( OBJECT_TYPE_ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /* Deleted object views test cases ends *.*/
//
//    /**
//     * Should success fully get list of table column for deleted object.
//     */
//    @Test
//    public void shouldSuccessFullyGetListOfTableColumnForDeletedObject() {
//        List< TableColumn > expected = GUIUtils.listColumns( DeletedObjectDTO.class, VersionDTO.class );
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( new ArrayList<>() ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getDeletedObjectsTableUI();
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
//        for ( TableColumn actualTableColumn : JsonUtils.jsonToObject( actualTable, TableUI.class ).getColumns() ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                    Assert.assertEquals( expectedTableColumn.getName(), actualTableColumn.getName() );
//                    Assert.assertEquals( expectedTableColumn.getFilter(), actualTableColumn.getFilter() );
//                }
//            }
//        }
//    }
//
//    /**
//     * Should throw exception while getting deleted objects table UI when some exception occurs.
//     */
//    @Test
//    public void shouldThrowExceptionWhileGettingDeletedObjectsTableUI() {
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getDeletedObjectsTableUI();
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should successfully get deleted object list when valid filter is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetDeletedObjectListWhenValidFilterIsProvided() {
//        List< DeletedObjectDTO > expectedList = new ArrayList<>();
//        expectedList.add( new DeletedObjectDTO( DATA_OBJECT_ID.toString(), DATA_OBJECT_NAME ) );
//        FilteredResponse< DeletedObjectDTO > expected = ( FilteredResponse< DeletedObjectDTO > ) getFilledFilteredResponse( expectedList );
//        EasyMock.expect( manager.getDeletedObjectList( EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyString() ) )
//                .andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getDeletedObjectList( FILTER_JSON );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        FilteredResponse< DeletedObjectDTO > filteredResponse = JsonUtils.jsonToObject( dataObjDto, FilteredResponse.class );
//        List< DeletedObjectDTO > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( filteredResponse.getData() ),
//                DeletedObjectDTO.class );
//        Assert.assertEquals( actualList, expectedList );
//    }
//
//    /**
//     * Should get bad request while getting deleted object list when invalid payload is provided.
//     */
//    @Test
//    public void shouldGetBadRequestWhileGettingDeletedObjectListWhenInvalidPayloadIsProvided() {
//
//        Response actual = service.getDeletedObjectList( INVALID_FILTER_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );
//        JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//
//    }
//
//    /**
//     * Should success fully get tabs view for container when valid container id is given.
//     */
//    @Test
//    public void shouldSuccessFullyGetTabsViewForContainerWhenValidContainerIdIsGiven() {
//
//        List< SubTabsUI > expected = getGeneralTabsList();
//        SubTabsItem expectedObject = new SubTabsItem();
//        expectedObject.setTabs( expected );
//        EasyMock.expect( manager.getTabsViewContainerUI( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( expectedObject )
//                .anyTimes();
//        mockControl().replay();
//        Response actual = service.listContainerTabsUI( DATA_OBJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String actualTabsList = JsonUtils.toJsonString( actualResponse.getData() );
//        SubTabsItem actualtabs = JsonUtils.jsonToObject( actualTabsList, SubTabsItem.class );
//        Assert.assertEquals( expectedObject.getTabs(), actualtabs.getTabs() );
//
//    }
//
//    /**
//     * Should successfully get sync context when valid filter is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetSyncContextWhenValidFilterIsProvided() {
//        List< ContextMenuItem > expectedList = new ArrayList<>();
//        expectedList.add( new ContextMenuItem( SYNC_CONTEXT_URL, SYNC_CONTEXT_ICON, SYNC_CONTEXT_TITLE ) );
//        EasyMock.expect( manager.getSyncContextRouter( EasyMock.anyString(), EasyMock.anyObject( FiltersDTO.class ) ) )
//                .andReturn( expectedList ).anyTimes();
//        mockControl.replay();
//        Response actual = service.getSyncContext( PROJECT_ID.toString(), FILTER_JSON );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        List< ContextMenuItem > actualList = JsonUtils.jsonToList(
//                JsonUtils.toJsonString( JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData() ),
//                ContextMenuItem.class );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should successfully get local context when valid filter is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetLocalContextWhenValidFilterIsProvided() {
//        List< ContextMenuItem > expectedList = new ArrayList<>();
//        expectedList.add( new ContextMenuItem( SYNC_CONTEXT_URL, SYNC_CONTEXT_ICON, SYNC_CONTEXT_TITLE ) );
//        EasyMock.expect(
//                        manager.getLocalContextRouter( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject( FiltersDTO.class ) ) )
//                .andReturn( expectedList ).anyTimes();
//        mockControl.replay();
//        Response actual = service.getLocalContext( PROJECT_ID.toString(), FILTER_JSON );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        List< ContextMenuItem > actualList = JsonUtils.jsonToList(
//                JsonUtils.toJsonString( JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData() ),
//                ContextMenuItem.class );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should successfully get sync context when valid filter is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetDataContextWhenValidFilterIsProvided() {
//        List< ContextMenuItem > expectedList = new ArrayList<>();
//        expectedList.add( new ContextMenuItem( SYNC_CONTEXT_URL, SYNC_CONTEXT_ICON, SYNC_CONTEXT_TITLE ) );
//        EasyMock.expect( manager.getDataContextRouter( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject( FiltersDTO.class ),
//                EasyMock.anyString() ) ).andReturn( expectedList ).anyTimes();
//        mockControl.replay();
//        Response actual = service.getDataContext( PROJECT_ID.toString(), FILTER_JSON );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        List< ContextMenuItem > actualList = JsonUtils.jsonToList(
//                JsonUtils.toJsonString( JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData() ),
//                ContextMenuItem.class );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should successfully get all file info of project when valid project is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetAllFileInfoOfProjectWhenValidProjectIsProvided() {
//        List< FileInfo > expectedList = new ArrayList<>();
//        expectedList.add( new FileInfo() );
//        EasyMock.expect(
//                        manager.getAllItemsWithFilters( EasyMock.anyString(), EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyObject() ) )
//                .andReturn( expectedList ).anyTimes();
//        mockControl.replay();
//        Response actual = service.getAllItemsinProjectWithFilter( PROJECT_ID.toString(), TYPE_ID, FILTER_JSON );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        List< FileInfo > actualList = JsonUtils.jsonToList(
//                JsonUtils.toJsonString( JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData() ),
//                FileInfo.class );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should successfully get items from selection when valid selection id is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetItemsFromSelectionWhenValidSelectionIdIsProvided() {
//        List< String > expectedList = new ArrayList<>();
//        expectedList.add( OBJECT_ID.toString() );
//        EasyMock.expect( manager.getItemsFromSelectionId( OBJECT_ID ) ).andReturn( expectedList ).anyTimes();
//        mockControl.replay();
//        Response actual = service.getItemsFromSelectionId( OBJECT_ID.toString() );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        List< String > actualList = JsonUtils.jsonToList(
//                JsonUtils.toJsonString( JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData() ),
//                String.class );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should return response with bad request when invalid pay load is provided.
//     */
//    @Test
//    public void shouldReturnResponseWithBadRequestWhenInvalidPayLoadIsProvided() {
//        DataObjectDTO expected = new DataObjectDTO();
//        EasyMock.expect( manager.addFileToAnObject( EasyMock.anyString(), EasyMock.anyObject(), null, EasyMock.anyString() ) )
//                .andReturn( expected ).anyTimes();
//        mockControl.replay();
//        Response actual = service.updateFileToAnObject( OBJECT_ID.toString(), FILTER_JSON, null );
//        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );
//        SusResponseDTO responseDTO = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNull( responseDTO.getData() );
//    }
//
//    /**
//     * Should successfully update file to an object when valid document is provided.
//     */
//    @Test
//    public void shouldSuccessfullyUpdateFileToAnObjectWhenValidDocumentIsProvided() {
//        DataObjectDTO expected = new DataObjectDTO();
//        EasyMock.expect( manager.addFileToAnObject( EasyMock.anyString(), EasyMock.anyObject(), null, EasyMock.anyString() ) )
//                .andReturn( expected ).anyTimes();
//        mockControl.replay();
//        Response actual = service.updateFileToAnObject( OBJECT_ID.toString(), JsonUtils.toJson( new DocumentDTO() ), null );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        DataObjectDTO actualObject = JsonUtils.jsonToObject(
//                JsonUtils.toJsonString( JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData() ),
//                DataObjectDTO.class );
//        Assert.assertEquals( expected, actualObject );
//    }
//
//    /**
//     * Should successfully restore object when valid restore mode is provided.
//     */
//    @Test
//    public void shouldSuccessfullyRestoreObjectWhenValidRestoreModeIsProvided() {
//        mockControl.replay();
//        Response actual = service.restoreObjectBySelection( PROJECT_ID.toString(), ConstantsMode.SINGLE );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( MessageBundleFactory.getMessage( Messages.RECORD_RESTORED.getKey() ),
//                actualResponse.getMessage().getContent() );
//    }
//
//    /**
//     * Should throw Exception while restoring deleted object when some thing goes wrong.
//     */
//    @Test
//    public void shouldThrowExceptionWhileRestoringDeletedObject() {
//        mockControl.replay();
//        Response actual = service.restoreObjectBySelection( PROJECT_ID.toString(), ConstantsMode.SINGLE );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should successfully get deleted objects context when valid filter is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetDeletedObjectsContextWhenValidFilterIsProvided() {
//        List< ContextMenuItem > expectedList = new ArrayList<>();
//        expectedList.add( new ContextMenuItem( TEST_CONTEXT_URL, TEST_CONTEXT_ICON, TEST_CONTEXT_TITLE ) );
//        EasyMock.expect( manager.getContextRouter( EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedList ).anyTimes();
//        mockControl.replay();
//        Response actual = service.getDeletedObjectsContext( FILTER_JSON );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        List< ContextMenuItem > actualList = JsonUtils.jsonToList(
//                JsonUtils.toJsonString( JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData() ),
//                ContextMenuItem.class );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should throw exception while getting context for deleted objects.
//     */
//    @Test
//    public void shouldThrowExceptionWhileGettingContextForDeletedObjects() {
//
//        EasyMock.expect( manager.getContextRouter( EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject() ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getDeletedObjectsContext( FILTER_JSON );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should success fully get list of table column for object version meta data UI when valid object id and version id is given.
//     */
//    @Test
//    public void shouldSuccessFullyGetListOfTableColumnForObjectVersionMetaDataUIWhenValidObjectIdAndVersionIdIsGiven() {
//        List< TableColumn > expected = GUIUtils.listColumns( MetaDataEntryDTO.class );
//        EasyMock.expect( manager.getObjectMetaDataTableUI( EasyMock.anyString() ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getObjectVersionMetaDataTableUI( DATA_OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
//        for ( TableColumn actualTableColumn : JsonUtils.jsonToObject( actualTable, TableUI.class ).getColumns() ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//        }
//    }
//
//    /**
//     * Should throw exception while getting table UI of object version meta data.
//     */
//    @Test
//    public void shouldThrowExceptionWhileGettingTableUIOfObjectVersionMetaData() {
//
//        EasyMock.expect( manager.getObjectMetaDataTableUI( EasyMock.anyString() ) ).andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getObjectVersionMetaDataTableUI( DATA_OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should successfully get edit data object form items when valid object id is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetEditDataObjectFormItemsWhenValidObjectIdIsProvided() {
//        List< UIFormItem > expected = GUIUtils.prepareForm( false, DataObjectDTO.class );
//        mockControl().replay();
//        Response actual = service.createEditDataObjectForm( OBJECT_ID.toString() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertEquals( expected, actualResponse.getData() );
//    }
//
//    /**
//     * Should throw exception while getting edit data object form items.
//     */
//    @Test
//    public void shouldThrowExceptionWhileGettingEditDataObjectFormItems() {
//
//        EasyMock.expect( manager.editDataObjectForm( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.createEditDataObjectForm( OBJECT_ID.toString() );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * ***************************** Tabs Views DataObject UI ************************************************.
//     */
//    /**
//     * Should SuccessFully Get Tabs View For Object When Valid DataObject Id Is Given
//     */
//    @Test
//    public void shouldSuccessFullyGetAuditViewForObjectWhenValidDataObjectIdIsGiven() {
//
//        List< SubTabsUI > expected = getGeneralTabsList();
//        SubTabsItem expectedObject = new SubTabsItem();
//        expectedObject.setTabs( expected );
//        EasyMock.expect( manager.getTabsViewDataObjectUI( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( expectedObject )
//                .anyTimes();
//        mockControl().replay();
//        Response actual = service.getTabsViewDataObjectUI( DATA_OBJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String actualTabsList = JsonUtils.toJsonString( actualResponse.getData() );
//        SubTabsItem actualtabs = JsonUtils.jsonToObject( actualTabsList, SubTabsItem.class );
//        Assert.assertEquals( expectedObject.getTabs(), actualtabs.getTabs() );
//    }
//
//    /**
//     * ****************************************** Change Status Object form Cases *******************************************.
//     */
//
//    /**
//     * Should success fully update data object when valid payload is provided.
//     */
//    @Test
//    public void shouldSuccessFullyUpdateDataObjectWhenValidPayloadIsProvided() {
//        DataObjectDTO expected = fillDataObjectDto();
//        EasyMock.expect( manager.updateDataObject( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.updateDataObject( expected.getId().toString(), OBJECT_JSON );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String dataObjectDTO = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        DataObjectDTO actualDataObjectDto = JsonUtils.jsonToObject( dataObjectDTO, DataObjectDTO.class );
//        Assert.assertEquals( expected, actualDataObjectDto );
//    }
//
//    /**
//     * Should throw exception while updating data object.
//     */
//    @Test
//    public void shouldThrowExceptionWhileUpdatingDataObject() {
//        EasyMock.expect( manager.updateDataObject( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.updateDataObject( OBJECT_VIEW_ID, OBJECT_JSON );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should successfully get status change options items list if manager return items list.
//     */
//    @Test
//    public void shouldSuccessfullyGetStatusChangeOptionItemsListIfManagerReturnItemsList() {
//        List< UIFormItem > expected = prepareEditDummyForm();
//        mockControl.replay();
//        Response actual = service.changeStatusObjectOptionForm( DATA_OBJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//        String listUIColumnStr = JsonUtils.toJson( actualResponse.getData() );
//        List< SelectFormItem > actualUIColumnList = JsonUtils.jsonToList( listUIColumnStr, SelectFormItem.class );
//        Assert.assertEquals( expected.size(), actualUIColumnList.size() );
//    }
//
//    /**
//     * Should throw exceptio when getting status change option items list thrown exception from manager.
//     */
//    @Test
//    public void shouldThrowExceptioWhenGettingStatusChangeOptionItemsListThrownExceptionFromManager() {
//
//        EasyMock.expect( manager.changeStatusObjectOptionForm( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.changeStatusObjectOptionForm( DATA_OBJECT_ID.toString() );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should successfully change status of items list.
//     */
//    @Test
//    public void shouldSuccessfullyChangeStatusOfItemsList() {
//        EasyMock.expect( manager.changeStatusObject( EasyMock.anyString(), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( ChangeStatusDTO.class ) ) ).andReturn( true );
//        mockControl.replay();
//        Response actual = service.changeStatusObject( DATA_OBJECT_ID.toString(), CHANGE_STATUS_JSON );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        Assert.assertNotNull( actualResponse.getData() );
//    }
//
//    /**
//     * Should throw exceptio when change status of items list thrown exception from manager.
//     */
//    @Test
//    public void shouldThrowExceptioWhenChangeStatusOfItemsListThrownExceptionFromManager() {
//
//        EasyMock.expect( manager.changeStatusObject( EasyMock.anyString(), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( ChangeStatusDTO.class ) ) ).andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.changeStatusObject( DATA_OBJECT_ID.toString(), CHANGE_STATUS_JSON );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should success fully get data object image preview when valid object id is provided.
//     */
//    @Test
//    public void shouldSuccessFullyGetDataObjectImagePreviewWhenValidObjectIdIsProvided() {
//        DocumentDTO expected = fillDocumentDTO();
//        EasyMock.expect( manager.getDataObjectPreview( EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getDataObjectPreview( OBJECT_ID.toString() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String dataObjectImageDTO = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        DocumentDTO actualPreview = JsonUtils.jsonToObject( dataObjectImageDTO, DocumentDTO.class );
//        Assert.assertEquals( expected.getId(), actualPreview.getId() );
//        Assert.assertEquals( expected.getUserId(), actualPreview.getUserId() );
//    }
//
//    /**
//     * Should success fully get data object image preview when valid object id and version is provided.
//     */
//    @Test
//    public void shouldSuccessFullyGetDataObjectImagePreviewWhenValidObjectIdAndVersionIsProvided() {
//        DocumentDTO expected = fillDocumentDTO();
//        EasyMock.expect( manager.getDataObjectVersionPreview( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyInt() ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getDataObjectVersionPreview( OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String dataObjectImageDTO = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        DocumentDTO actualPreview = JsonUtils.jsonToObject( dataObjectImageDTO, DocumentDTO.class );
//        Assert.assertEquals( expected.getId(), actualPreview.getId() );
//        Assert.assertEquals( expected.getUserId(), actualPreview.getUserId() );
//    }
//
//    /**
//     * Should success fully get data object curve when valid object id is provided.
//     */
//    @Test
//    public void shouldSuccessFullyGetDataObjectCurveWhenValidObjectIdIsProvided() {
//        DataObjectCurveDTO expected = fillDataObjectCurveDto();
//        EasyMock.expect( manager.getDataObjectCurve( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( CurveUnitDTO.class ) ) )
//                .andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getDataObjectCurve( OBJECT_ID.toString(), UNIT_JSON );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String dataObjectImageDTO = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        DataObjectCurveDTO actualPreview = JsonUtils.jsonToObject( dataObjectImageDTO, DataObjectCurveDTO.class );
//        Assert.assertEquals( expected.getId(), actualPreview.getId() );
//        Assert.assertEquals( expected.getName(), actualPreview.getName() );
//        Assert.assertEquals( expected.getxDimension(), actualPreview.getxDimension() );
//    }
//
//    /**
//     * Should success fully get data object movie when valid object id is provided.
//     */
//    @Test
//    public void shouldSuccessFullyGetDataObjectMovieWhenValidObjectIdIsProvided() {
//        DataObjectMovieDTO expected = fillDataObjectMoviewDto();
//        EasyMock.expect( manager.getDataObjectMovie( EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getDataObjectMovie( OBJECT_ID.toString() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String dataObjectImageDTO = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        DataObjectMovieDTO actualMoviewDto = JsonUtils.jsonToObject( dataObjectImageDTO, DataObjectMovieDTO.class );
//        Assert.assertEquals( expected.getId(), actualMoviewDto.getId() );
//        Assert.assertEquals( expected.getName(), actualMoviewDto.getName() );
//        Assert.assertEquals( expected.getPoster(), actualMoviewDto.getPoster() );
//        Assert.assertEquals( expected.getThumbnail(), actualMoviewDto.getThumbnail() );
//        Assert.assertEquals( expected.getSources().toString(), actualMoviewDto.getSources().toString() );
//    }
//
//    /**
//     * Should throw exception getting data object movie.
//     */
//    @Test
//    public void shouldThrowExceptionGettingDataObjectMovie() {
//
//        EasyMock.expect( manager.getDataObjectMovie( EasyMock.anyObject( UUID.class ) ) ).andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getDataObjectMovie( OBJECT_ID.toString() );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should success fully get data object movie version when valid object id is provided.
//     */
//    @Test
//    public void shouldSuccessFullyGetDataObjectMovieVersionWhenValidObjectIdIsProvided() {
//        DataObjectMovieDTO expected = fillDataObjectMoviewDto();
//        EasyMock.expect( manager.getDataObjectVersionMovie( EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) ).andReturn( expected )
//                .anyTimes();
//        mockControl().replay();
//        Response actual = service.getDataObjectVersionMovie( OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String dataObjectImageDTO = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        DataObjectMovieDTO actualMoviewDto = JsonUtils.jsonToObject( dataObjectImageDTO, DataObjectMovieDTO.class );
//        Assert.assertEquals( expected.getId(), actualMoviewDto.getId() );
//        Assert.assertEquals( expected.getName(), actualMoviewDto.getName() );
//        Assert.assertEquals( expected.getPoster(), actualMoviewDto.getPoster() );
//        Assert.assertEquals( expected.getThumbnail(), actualMoviewDto.getThumbnail() );
//        Assert.assertEquals( expected.getSources().toString(), actualMoviewDto.getSources().toString() );
//    }
//
//    /**
//     * Should throw exception getting data object version movie.
//     */
//    @Test
//    public void shouldThrowExceptionGettingDataObjectVersionMovie() {
//
//        EasyMock.expect( manager.getDataObjectVersionMovie( EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getDataObjectVersionMovie( OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should throw exception getting data object curve.
//     */
//    @Test
//    public void shouldThrowExceptionGettingDataObjectCurve() {
//
//        EasyMock.expect( manager.getDataObjectCurve( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( CurveUnitDTO.class ) ) )
//                .andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getDataObjectCurve( OBJECT_ID.toString(), UNIT_JSON );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should throw exception getting data object preview image.
//     */
//    @Test
//    public void shouldThrowExceptionGettingDataObjectPreviewImage() {
//
//        EasyMock.expect( manager.getDataObjectPreview( EasyMock.anyObject( UUID.class ) ) ).andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getDataObjectPreview( OBJECT_ID.toString() );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should throw exception getting data object preview with version image.
//     */
//    @Test
//    public void shouldThrowExceptionGettingDataObjectPreviewWithVersionImage() {
//
//        EasyMock.expect( manager.getDataObjectVersionPreview( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyInt() ) ).andThrow( new SusException( ERROR_MESSAGE ) );
//        mockControl.replay();
//        Response actual = service.getDataObjectVersionPreview( OBJECT_ID.toString(), DEFAULT_VERSION_ID );
//        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
//    }
//
//    /**
//     * Should successfully return data object when valid input is provided.
//     */
//    @Test
//    public void shouldSuccessfullyReturnDataObjectWhenValidInputIsProvided() {
//        DataObjectDTO objectDTO = fillDataObjectDto();
//        EasyMock.expect( manager.getobjectSynchStatus( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) ).andReturn( objectDTO )
//                .anyTimes();
//        mockControl().replay();
//
//        Response actual = service.getObjectSynchStatus( PROJECT_ID.toString() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String dataObjectImageDTO = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        DataObjectDTO actualPreview = JsonUtils.jsonToObject( dataObjectImageDTO, DataObjectDTO.class );
//        Assert.assertEquals( objectDTO.getId(), actualPreview.getId() );
//        Assert.assertEquals( objectDTO.getFile(), actualPreview.getFile() );
//        Assert.assertEquals( objectDTO.getCreatedOn(), actualPreview.getCreatedOn() );
//        Assert.assertEquals( objectDTO.getName(), actualPreview.getName() );
//
//    }
//
//    /**
//     * Should successfully return data object after processing checkin and checkout status when valid input is provided.
//     */
//    @Test
//    public void shouldSuccessfullyReturnDataObjectAfterProcessingCheckinAndCheckoutStatusWhenValidInputIsProvided() {
//        DataObjectDTO objectDTO = fillDataObjectDto();
//        EasyMock.expect( manager.setObjectSynchStatus( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyString() ) ).andReturn( objectDTO ).anyTimes();
//        mockControl().replay();
//        String trueValue = "true";
//        Response actual = service.setObjectCheckinCheckoutStatus( PROJECT_ID.toString(), trueValue );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String dataObjectImageDTO = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
//        DataObjectDTO actualPreview = JsonUtils.jsonToObject( dataObjectImageDTO, DataObjectDTO.class );
//        Assert.assertEquals( objectDTO.getId(), actualPreview.getId() );
//        Assert.assertEquals( objectDTO.getFile(), actualPreview.getFile() );
//        Assert.assertEquals( objectDTO.getCreatedOn(), actualPreview.getCreatedOn() );
//        Assert.assertEquals( objectDTO.getName(), actualPreview.getName() );
//
//    }
//
//    /**
//     * Should successfully return true if export permissions are set to user when valid input is provided.
//     */
//    @Test
//    public void shouldSuccessfullyReturnTrueIfExportPermissionsAreSetToUserWhenValidInputIsProvided() {
//
//        EasyMock.expect( manager.checkUserReadPermission( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
//        mockControl().replay();
//        Response actual = service.getUserReadPermission( PROJECT_ID.toString() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        String jsonDataCheck = JsonUtils.toJson( actualResponse.getData() );
//        Assert.assertTrue( Boolean.parseBoolean( jsonDataCheck ) );
//
//    }
//
//    /**
//     * Should return successfully when valid id is given.
//     */
//    @Test
//    public void shouldReturnSuccessfullyWhenValidIdIsGiven() {
//        EasyMock.expect( manager.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( new ArrayList<>() ).anyTimes();
//        mockControl().replay();
//        Response actual = service.auditLogForDataObjectView( OBJECT_ID.toString() );
//        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
//        TableUI jsonDataCheck = JsonUtils.jsonToObject( JsonUtils.toJson( actualResponse.getData() ), TableUI.class );
//        Assert.assertNotNull( jsonDataCheck );
//    }
//
//    /**
//     * ************************************ HELPER METHODS **************************************.
//     *
//     * @param value
//     *            the value
//     * @return the filters DTO
//     */
//
//    /**
//     * Fill select UI.
//     *
//     * @return the filters DTO
//     *
//     * @return the list
//     */
//    private List< SelectFormItem > fillSelectUI( String value ) {
//        List< SelectFormItem > selectFormItems = new ArrayList<>();
//        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
//        selectFormItem.setLabel( "Select Permission" );
//        selectFormItem.setName( "selection" );
//        selectFormItem.setType( "select" );
//        selectFormItem.setValue( value );
//        selectFormItem.setReadonly( false );
//        selectFormItem.setDuplicate( false );
//        selectFormItem.setMultiple( false );
//        selectFormItem.setBindFrom( "/data/object/" + OBJECT_ID + "/permission/fields/{__value__}" );
//        List< SelectOptionsUI > options = new ArrayList<>();
//        SelectOptionsUI selectOptionsUIForInheritTrue = new SelectOptionsUI();
//        selectOptionsUIForInheritTrue.setId( SET_INHERITED );
//        selectOptionsUIForInheritTrue.setName( "Set Inherited" );
//        SelectOptionsUI selectOptionsUIForInheritFalse = new SelectOptionsUI();
//        selectOptionsUIForInheritTrue.setId( UNSET_INHERITED );
//        selectOptionsUIForInheritTrue.setName( "Unset Inherited" );
//        options.add( selectOptionsUIForInheritTrue );
//        options.add( selectOptionsUIForInheritFalse );
//        selectFormItem.setOptions( options );
//        selectFormItems.add( selectFormItem );
//        return selectFormItems;
//    }
//
//    /**
//     * Tabs View Dummy Name.
//     *
//     * @return List of STring
//     */
//    private List< SubTabsUI > getGeneralTabsList() {
//        List< SubTabsUI > subTbs = new ArrayList<>();
//        subTbs.add( new SubTabsUI( SusConstantObject.PROPERTIES_TAB ) );
//
//        return subTbs;
//    }
//
//    /**
//     * A method to populate the project Dto for Expected Result of test.
//     *
//     * @return projectDTO
//     */
//    private ProjectDTO fillProjectDto() {
//
//        projectDTO.setId( PROJECT_ID );
//        projectDTO.setName( PROJECT_NAME );
//        projectDTO.setVersion( new VersionDTO( DEFAULT_VERSION_ID ) );
//        projectDTO.setDescription( PROJECT_DESCRPTION );
//        projectDTO.setCreatedOn( CREATED_ON_DATE );
//        projectDTO.setModifiedOn( UPDATED_ON_DATE );
//
//        return projectDTO;
//    }
//
//    /**
//     * Fill workflow project dto.
//     *
//     * @return the workflow project DTO
//     */
//    private WorkflowProjectDTO fillWorkflowProjectDto() {
//
//        workflowProjectDTO.setId( WORKFLOW_PROJECT_ID );
//        workflowProjectDTO.setName( WORKFLOW_PROJECT_NAME );
//        workflowProjectDTO.setVersion( new VersionDTO( DEFAULT_VERSION_ID ) );
//        workflowProjectDTO.setDescription( WORKFLOW_PROJECT_DESCRPTION );
//        workflowProjectDTO.setCreatedOn( CREATED_ON_DATE );
//        workflowProjectDTO.setModifiedOn( UPDATED_ON_DATE );
//
//        return workflowProjectDTO;
//    }
//
//    /**
//     * A method to populate the DataObject Dto for Expected Result of test.
//     *
//     * @return projectDTO
//     */
//    private DataObjectDTO fillDataObjectDto() {
//
//        dataObjectDTO.setId( DATA_OBJECT_ID );
//        dataObjectDTO.setName( DATA_OBJECT_NAME );
//        dataObjectDTO.setCreatedOn( CREATED_ON_DATE );
//        dataObjectDTO.setModifiedOn( UPDATED_ON_DATE );
//        dataObjectDTO.setCustomAttributes( getMapOfCustomAttributes() );
//
//        return dataObjectDTO;
//    }
//
//    /**
//     * A method to get Map of key value pair of customer Attributes.
//     *
//     * @return the map of custom attributes
//     */
//    private Map< String, Object > getMapOfCustomAttributes() {
//        Map< String, Object > map = new HashMap<>();
//
//        map.put( TEST_CUSTOM_ATTRIBUTE, TEST_CUSTOM_ATTRIBUTE );
//        return map;
//    }
//
//    /**
//     * Prepare create data object form.
//     *
//     * @return the list
//     */
//    private List< SelectFormItem > prepareCreateObjectOptionsForm() {
//
//        SelectFormItem ufi = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
//        List< SelectOptionsUI > options = new ArrayList<>();
//        options.add( new SelectOptionsUI( DATA_OBJECT_ID.toString(), DATA_OBJECT_NAME ) );
//
//        ufi.setName( DataManagerImpl.FIELD_NAME_OBJECT_TYPE_ID );
//        ufi.setOptions( options );
//
//        return Arrays.asList( ufi );
//    }
//
//    /**
//     * Get Filled Filtered Response.
//     *
//     * @param list
//     *         the list
//     *
//     * @return FilteredResponse
//     */
//    private FilteredResponse< ? > getFilledFilteredResponse( List list ) {
//        FiltersDTO filter = new FiltersDTO();
//        filter.setDraw( ConstantsInteger.INTEGER_VALUE_ZERO );
//        filter.setLength( ConstantsInteger.INTEGER_VALUE_TWO );
//        filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
//        filter.setFilteredRecords( Long.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ) );
//
//        List< ? > data = new ArrayList<>();
//        data.addAll( list );
//        FilteredResponse< ? > expectedResponse = PaginationUtil.constructFilteredResponse( filter, data );
//        return expectedResponse;
//    }
//
//    /**
//     * Test Helper method For DataObject single key/value pairs.
//     *
//     * @return List<TableColumn>
//     */
//    private List< TableColumn > getDummySingleViewForDataObject() {
//        return GUIUtils.listColumns( DataObjectDTO.class );
//    }
//
//    /**
//     * Prepare Expected Form Items.
//     *
//     * @return the list
//     */
//    private List< UIFormItem > prepareEditDummyForm() {
//
//        return GUIUtils.prepareForm( false, projectDTO );
//
//    }
//
//    /**
//     * ************************************ HELPER METHODS **************************************.
//     *
//     * /** Populates filter DTO.
//     *
//     * @return the filters DTO
//     */
//    private FiltersDTO populateFilterDTO() {
//        FiltersDTO filterDTO = new FiltersDTO();
//        filterDTO.setDraw( ConstantsInteger.INTEGER_VALUE_ONE );
//        filterDTO.setLength( ConstantsInteger.INTEGER_VALUE_ONE );
//        filterDTO.setStart( ConstantsInteger.INTEGER_VALUE_ONE );
//        filterDTO.setFilteredRecords( 1L );
//        return filterDTO;
//    }
//
//    /**
//     * Gets the object manage DTO.
//     *
//     * @return the object manage DTO
//     */
//    private ManageObjectDTO getObjectManageDTO() {
//        ManageObjectDTO objectManageDTO = new ManageObjectDTO();
//        objectManageDTO.setName( "Administrator" );
//        objectManageDTO.setType( "Group" );
//        objectManageDTO.setSidId( SID_ID );
//        return objectManageDTO;
//    }
//
//    /**
//     * Gets the fill permission DTO.
//     *
//     * @return the fill permission DTO
//     */
//    private PermissionDTO getFillPermissionDTO() {
//        PermissionDTO permissionDTO = new PermissionDTO();
//        permissionDTO.setValue( DISBALE );
//        permissionDTO.setManage( true );
//        permissionDTO.setMatrixKey( OBJECT_VIEW_MASK );
//        permissionDTO.setMatrexValue( OBJECT_VIEW );
//        return permissionDTO;
//    }
//
//    /**
//     * Fill expected response DTO.
//     *
//     * @param success
//     *         the success
//     * @param data
//     *         the data
//     * @param messageStr
//     *         the message str
//     * @param messageType
//     *         the message type
//     *
//     * @return the sus response DTO
//     */
//    private SusResponseDTO fillExpectedResponseDTO( boolean success, Object data, String messageStr, String messageType ) {
//        Message message = new Message( messageType, messageStr );
//        SusResponseDTO susResponseDTO = new SusResponseDTO( success, message, data );
//        return susResponseDTO;
//    }
//
//    /**
//     * Gets the filled columns.
//     *
//     * @return the filled columns
//     */
//    private TableUI getFilledColumns() {
//        TableUI tableUI = new TableUI();
//        List< TableColumn > columns = new ArrayList<>();
//        columns.add( getFilledTableUI() );
//        tableUI.setColumns( columns );
//        return tableUI;
//    }
//
//    /**
//     * Gets the filled table UI.
//     *
//     * @return the filled table UI
//     */
//    private TableColumn getFilledTableUI() {
//        TableColumn tableColumn = new TableColumn();
//        tableColumn.setData( "name" );
//        tableColumn.setTitle( "Name" );
//        tableColumn.setFilter( "text" );
//        tableColumn.setSortable( true );
//        tableColumn.setName( "name" );
//        tableColumn.setRenderer( getFilledRenderer() );
//        return tableColumn;
//    }
//
//    /**
//     * Gets the filled renderer.
//     *
//     * @return the filled renderer
//     */
//    private Renderer getFilledRenderer() {
//        Renderer renderer = new Renderer();
//        renderer.setType( "text" );
//        renderer.setSeparator( "," );
//        renderer.setLabelClass( "" );
//        renderer.setData( "name" );
//        renderer.setManage( true );
//        return renderer;
//    }
//
//    /**
//     * Prepare object view DTO.
//     *
//     * @return the object view DTO
//     */
//    private ObjectViewDTO prepareObjectViewDTO() {
//        ObjectViewDTO objectViewDTO = new ObjectViewDTO();
//        objectViewDTO.setName( VIEW_NAME );
//        objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.PROJECT_TABLE_KEY );
//        objectViewDTO.setObjectViewJson( OBJECT_VIEW_PAYLOAD );
//        return objectViewDTO;
//    }
//
//    /**
//     * Prepare object meta data DTO.
//     *
//     * @return the object meta data DTO
//     */
//    private ObjectMetaDataDTO prepareObjectMetaDataDTO() {
//        ObjectMetaDataDTO objectMetaDataDTO = new ObjectMetaDataDTO();
//        objectMetaDataDTO.setMetadata( Arrays.asList( new MetaDataEntryDTO( META_DATA_KEY, META_DATA_VALUE ) ) );
//        return objectMetaDataDTO;
//    }
//
//    /**
//     * Fill document DTO.
//     *
//     * @return the document DTO
//     */
//    private DocumentDTO fillDocumentDTO() {
//        DocumentDTO document = new DocumentDTO();
//        document.setUserId( USER_ID );
//        document.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
//        return document;
//    }
//
//    /**
//     * Fill data object curve dto.
//     *
//     * @return the data object curve DTO
//     */
//    private DataObjectCurveDTO fillDataObjectCurveDto() {
//        DataObjectCurveDTO document = new DataObjectCurveDTO();
//        document.setName( DATA_OBJECT_NAME );
//        document.setxDimension( X_DIMENSION );
//        document.setyDimension( Y_DIMENSION );
//        document.setxUnit( X_UNIT );
//        document.setyUnit( Y_UNIT );
//        List< double[] > curveList = new ArrayList<>();
//        double[] m = new double[ ConstantsInteger.INTEGER_VALUE_FOUR ];
//        curveList.add( m );
//        document.setCurve( curveList );
//        document.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
//        return document;
//    }
//
//    /**
//     * Fill data object moview dto.
//     *
//     * @return the data object movie DTO
//     */
//    private DataObjectMovieDTO fillDataObjectMoviewDto() {
//        DataObjectMovieDTO movieDto = new DataObjectMovieDTO();
//        movieDto.setName( DATA_OBJECT_NAME );
//        movieDto.setPoster( POSTER );
//        movieDto.setThumbnail( THUMB_NAIL );
//        MovieSources sources = new MovieSources();
//        sources.setMp4( MP4 );
//        sources.setWebm( WEBM );
//        movieDto.setSources( sources );
//        movieDto.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
//        return movieDto;
//    }

}