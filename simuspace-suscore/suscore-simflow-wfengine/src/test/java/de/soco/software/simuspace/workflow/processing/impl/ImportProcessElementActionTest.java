package de.soco.software.simuspace.workflow.processing.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FileUpload;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.UserWFElementImpl;

/**
 * The is the test class for public functions in {@link ImportProcessElementAction}.
 */
@Log4j2
@RunWith( PowerMockRunner.class )
@PrepareForTest( { ImportProcessElementAction.class, SuSClient.class } )
public class ImportProcessElementActionTest {

    /**
     * The Constant SELECTION_ID.
     */
    private static final UUID SELECTION_ID = UUID.randomUUID();

    /**
     * The Constant PROJECT_ID.
     */
    private static final UUID PROJECT_ID = UUID.randomUUID();

    /**
     * The url vault file link1.
     */
    private static String SELECTION_ITEM = "/api/selection/";

    /**
     * The Constant OVER_WRITE_YES.
     */
    private static final String OVER_WRITE_YES = "yes";

    /**
     * The Constant ONE_ERROR.
     */
    private static final int ONE_ERROR = 1;

    /**
     * The Constant STARTING_INDEX.
     */
    private static final int STARTING_INDEX = 0;

    /**
     * The Constant ZIP_PATH_TO_IMPORT.
     */
    private static final String ZIP_PATH_TO_IMPORT = "src/test/resources/testData/imprtData.zip";

    /**
     * The Constant IN_VALID_PATH_TO_IMPORT.
     */
    private static final String IN_VALID_PATH_TO_IMPORT = "src/test/resources/testData/invalidFileToImport.jpeg";

    /**
     * The Constant SHOULD_HAVE_5_ERRORS_AS_5_REQUIRED_FIELDS_ARE_MISSING.
     */
    private static final String SHOULD_HAVE_4_ERRORS_AS_4_REQUIRED_FIELDS_ARE_MISSING = "Should have 4 errors as 4 required fields are missing.";

    /**
     * The Constant SHOULD_HAVE_ONLY_ONE_ERROR.
     */
    private static final String SHOULD_HAVE_ONLY_ONE_ERROR = "Should have only one error.";

    /**
     * The Constant DOCUMENT_ID.
     */
    private static final int DOCUMENT_ID = 1;

    /**
     * The Constant TYPE_OF_DOCUMENT.
     */
    private static final String TYPE_OF_DOCUMENT = "client";

    /**
     * The Constant FILE_TO_UPLOAD.
     */
    private static final String FILE_TO_UPLOAD = "imprtData.zip";

    /**
     * The Constant IMPORT_PROCESS_PAYLOAD.
     */
    private static final String IMPORT_PROCESS_PAYLOAD = "{\"discard\":\"-1\",\"locationId\":0,\"variant\":{\"name\":\"newVar\",\"variantStatus\":\"1\",\"referenceVariant\":[],\"variantImage\":null,\"description\":\"\",\"id\":null},\"pathToProcess\":[{\"path\":\"imprtData.zip\",\"docId\":1,\"selType\":\"client\",\"locationId\":0}],\"disciplineId\":1,\"projectId\":1}";

    /**
     * The Constant IMPORT_PROCESS_SUCCESS_RESPONSE.
     */
    private static final String IMPORT_PROCESS_SUCCESS_RESPONSE = "{\"success\":true,\"message\":{\"type\":\"SUCCESS\",\"content\":\"Files import request submitted successfully. Check Processes for status.\"}}";

    /**
     * The Constant DISCIPLINE_ID.
     */
    private static final int DISCIPLINE_ID = 1;

    /**
     * The Constant DEFAULT_LOCATION_ID.
     */
    private static final int DEFAULT_LOCATION_ID = 0;

    /**
     * The element.
     */
    private static UserWFElement element;

    /**
     * The dummy token.
     */
    private static String DUMMY_TOKEN = "eaa3a035689402e9581c115008dbf15c4ec30839";

    /**
     * The dummy user agent.
     */
    private static String DUMMY_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) SIMuSPACE/2.0.0 Chrome/53.0.2785.113 Electron/1.4.3 Safari/537.36";

    /**
     * The Constant PORT_OF_URL.
     */
    private static final String PORT_OF_URL = "8080";

    /**
     * The Constant PROTOCOL_OF_URL.
     */
    private static final String PROTOCOL_OF_URL = "http://";

    /**
     * The Constant HOST_OF_URL.
     */
    private static final String HOST_OF_URL = "localhost";

    /**
     * The Constant JOB_ID.
     */
    private static final UUID JOB_ID = UUID.randomUUID();

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The directory path to import.
     */
    private final static String DIRECTORY_PATH_TO_IMPORT = "src/test/resources/testData/routineTestData";

    /**
     * The Constant DIRECTORY_PATH_TO_IMPORT_NON_EXISTING.
     */
    private final static String DIRECTORY_PATH_TO_IMPORT_NON_EXISTING = "src/test/resources/nonExisting";

    /**
     * The Constant DATAOBJECT_VAULT_FILES_DATA_NOT_AVAILABLE.
     */
    private final static String DATAOBJECT_VAULT_FILES_DATA_NOT_AVAILABLE = "[\"" + PROJECT_ID + "\"]";

    /**
     * The Constant parameters is used to hold the element's parameters and their values.
     */
    private final static Map< String, Object > parameters = new HashMap<>();

    private static final int TWO_ERROR = 2;

    /**
     * Cleanups after test runing.
     */
    @AfterClass
    public static void cleanUpFileCreations() {
    }

    /**
     * Convert string to json node.
     *
     * @param value
     *         the value
     *
     * @return the json node
     */
    private static JsonNode convertStringToJsonNode( String value ) {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonFactory factory = mapper.getJsonFactory(); // since 2.1 use mapper.getFactory() instead
        JsonParser jp;
        JsonNode actualObj = null;
        try {
            jp = factory.createJsonParser( value );
            actualObj = mapper.readTree( jp );
        } catch ( final Exception e ) {
            log.error( e.getMessage() );
        }
        return actualObj;
    }

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Mock su S client functions.
     *
     * @param url
     *         the url
     * @param restAPI
     *         the rest API
     * @param responseDTO
     *         the response DTO
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private static void mockSuSClientFunctions( String url, RestAPI restAPI, SusResponseDTO responseDTO )
            throws JsonSerializationException {
        PowerMockito.mockStatic( SuSClient.class );
        when( SuSClient.getRequest( url, prepareHeaders( restAPI ) ) ).thenReturn( responseDTO );
    }

    /**
     * Prepare headers.
     *
     * @param restAPI
     *         the rest api
     *
     * @return the map
     */
    private static Map< String, String > prepareHeaders( RestAPI restAPI ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, restAPI.getRequestHeaders().getToken() );

        return requestHeaders;

    }

    /**
     * It prepares url by getting protocol : hostname and port<br> from server appended with url provided.
     *
     * @param url
     *         , url of API
     * @param restAPI
     *         the rest API
     *
     * @return requestHeaders
     */
    private static String prepareURL( String url, RestAPI restAPI ) {
        if ( restAPI != null ) {
            return restAPI.getProtocol() + restAPI.getHostname() + ConstantsString.COLON + restAPI.getPort() + url;
        }
        return ConstantsString.EMPTY_STRING;

    }

    ;

    /**
     * The response DTO.
     */
    private SusResponseDTO responseDTO;

    /**
     * The import settings file path.
     */
    private final String IMPORT_SETTINGS_FILE_PATH = "src/test/resources/ImportSettings.json";

    /**
     * The data object element action.
     */
    private ImportProcessElementAction importElementAction;

    /**
     * The job impl.
     */
    private JobImpl jobImpl;

    /**
     * The Constant PARAMETERS_IMPORT_SETTING_FILE_KEY is a parameters map key used to hold settings file path to scan import folder.
     */
    private final String PARAMETERS_IMPORT_SETTING_FILE_KEY = "{{input.setting}}";

    /**
     * The Constant PARAMETERS_ZIP_OR_PATH_KEY is a parameters map key used to hold zip or folder path to import.
     */
    private final String PARAMETERS_ZIP_OR_PATH_KEY = "{{input.zipOrFolder}}";

    /**
     * The Constant PARAMETERS_ZIP_OR_PATH_KEY is a parameters map key used to hold zip or folder path to import.
     */
    private final String PARAMETERS_PROJECT_OBJECT_KEY = "{{input.project}}";

    private RestAPI restAPI;

    /**
     * The expected exception.
     */
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    ;

    /**
     * The generic appender.
     */
    private final String GENERIC_APPENDER = "";

    /**
     * Creates the file upload.
     *
     * @return the file upload
     */
    private FileUpload createFileUpload() {
        final FileUpload uploadResponse = new FileUpload();
        uploadResponse.setSelType( TYPE_OF_DOCUMENT );
        uploadResponse.setPath( FILE_TO_UPLOAD );
        uploadResponse.setDocId( DOCUMENT_ID );
        uploadResponse.setLocationId( DEFAULT_LOCATION_ID );
        return uploadResponse;
    }

    /**
     * Fill user parameters.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private void fillUserParameters() throws JsonSerializationException {
        parameters.put( PARAMETERS_PROJECT_OBJECT_KEY, SELECTION_ID );
        final EngineFile zipOrDirPath = new EngineFile();
        zipOrDirPath.setPath( DIRECTORY_PATH_TO_IMPORT );
        parameters.put( PARAMETERS_ZIP_OR_PATH_KEY, zipOrDirPath );
        final EngineFile settingFilePath = new EngineFile();
        settingFilePath.setPath( IMPORT_SETTINGS_FILE_PATH );
        parameters.put( PARAMETERS_IMPORT_SETTING_FILE_KEY, settingFilePath );
    }

    /**
     * Fill user parameters with in valid path.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private void fillUserParametersWithInValidPath() throws JsonSerializationException {
        final EngineFile zipOrDirPath = new EngineFile();
        zipOrDirPath.setPath( DIRECTORY_PATH_TO_IMPORT_NON_EXISTING );
        parameters.put( PARAMETERS_ZIP_OR_PATH_KEY, DIRECTORY_PATH_TO_IMPORT_NON_EXISTING );

        final EngineFile settingFilePath = new EngineFile();
        settingFilePath.setPath( IMPORT_SETTINGS_FILE_PATH );
        parameters.put( PARAMETERS_IMPORT_SETTING_FILE_KEY, settingFilePath );
    }

    private void mockUploadFile( String fileToUpload ) throws SusException {
        final FileUpload uploadResponse = createFileUpload();
        when( SuSClient.uploadFileRequest( prepareURL( ImportProcessElementAction.URL_UPLOAD_FILE, restAPI ), fileToUpload,
                prepareHeaders( restAPI ) ) ).thenReturn( uploadResponse );
    }

    /**
     * Prepare job impl.
     *
     * @return the job
     */
    public Job prepareJobImpl() {
        final Job job = new JobImpl();
        job.setId( JOB_ID );
        return job;
    }

    /**
     * Prepare rest credentials.
     *
     * @return the rest API
     */
    private RestAPI prepareRestCredentials() {
        final RequestHeaders headers = new RequestHeaders( DUMMY_TOKEN, DUMMY_USER_AGENT );
        final RestAPI restAPI = new RestAPI( PROTOCOL_OF_URL, HOST_OF_URL, PORT_OF_URL );
        restAPI.setRequestHeaders( headers );
        jobImpl = new JobImpl();
        jobImpl.setServer( restAPI );
        jobImpl.setRequestHeaders( headers );
        jobImpl.setId( JOB_ID );
        return restAPI;
    }

    /**
     * Sets the import element action requisite.
     */
    private void setImportElementActionPreRequisite() {
        try {
            fillUserParameters();
        } catch ( final JsonSerializationException e ) {
            e.printStackTrace();
        }
        setImportElementFields();
    }

    /**
     * Sets the import element fields.
     */
    private void setImportElementFields() {
        final List< Field< ? > > fields = new ArrayList<>();
        element = new UserWFElementImpl();

        final Field< Object > project = new Field<>();
        project.setType( FieldTypes.TEXT.getType() );
        project.setLabel( ImportProcessElementAction.LABEL_OF_CONTAINER_FIELD_IN_DESIGNER );
        project.setValue( PARAMETERS_PROJECT_OBJECT_KEY );
        project.setVariableMode( true );
        fields.add( project );

        final Field< String > zipOrFolderPath = new Field<>();
        zipOrFolderPath.setType( FieldTypes.TEXT.getType() );
        zipOrFolderPath.setLabel( ImportProcessElementAction.LABEL_OF_INPUT_DIRECTORY );
        zipOrFolderPath.setValue( PARAMETERS_ZIP_OR_PATH_KEY );
        zipOrFolderPath.setVariableMode( true );
        fields.add( zipOrFolderPath );

        final Field< String > exportPath = new Field<>();
        exportPath.setType( FieldTypes.TEXT.getType() );
        exportPath.setLabel( ImportProcessElementAction.LABEL_OF_IMPORT_SETTINGS_FILE );
        exportPath.setValue( PARAMETERS_IMPORT_SETTING_FILE_KEY );
        exportPath.setVariableMode( true );
        fields.add( exportPath );

        final Field< String > overWrite = new Field<>();
        overWrite.setType( FieldTypes.TEXT.getType() );
        overWrite.setLabel( ImportProcessElementAction.LABEL_OF_OVER_WRITE_FLAG );
        overWrite.setValue( OVER_WRITE_YES );
        fields.add( overWrite );

        element.setFields( fields );
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setUp() throws Exception {
        mockControl().resetToNice();

        restAPI = prepareRestCredentials();
        PowerMockito.mockStatic( SuSClient.class );
        mockUploadFile( DIRECTORY_PATH_TO_IMPORT + ImportProcessElementAction.ZIP_EXTENSION );

        final SusResponseDTO processResponseDTO = JsonUtils.jsonToObject( IMPORT_PROCESS_SUCCESS_RESPONSE, SusResponseDTO.class );
        when( SuSClient.postRequest( prepareURL( ImportProcessElementAction.URL_FOR_START_IMPORT_PROCESS, restAPI ),
                JsonUtils.objectToJson( JsonUtils.jsonToObject( IMPORT_PROCESS_PAYLOAD, Object.class ) ),
                prepareHeaders( restAPI ) ) ).thenReturn( processResponseDTO );
    }

    /**
     * Should notify error if path to import not exists.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotifyErrorIfPathToImportNotExists() throws Exception {

        setImportElementActionPreRequisite();
        fillUserParametersWithInValidPath();

        responseDTO = new SusResponseDTO();
        responseDTO.setData( convertStringToJsonNode( DATAOBJECT_VAULT_FILES_DATA_NOT_AVAILABLE ) );
        responseDTO.setSuccess( true );
        mockSuSClientFunctions( prepareURL( SELECTION_ITEM + SELECTION_ID, restAPI ), restAPI, responseDTO );
        importElementAction = new ImportProcessElementAction( prepareJobImpl(), element, parameters );
        importElementAction.setRestAPI( restAPI );
        final Notification notification = importElementAction.doAction();

        final String[] errorMessages = { MessagesUtil.getMessage( WFEMessages.FILE_PATH_NOT_EXIST,
                Paths.get( DIRECTORY_PATH_TO_IMPORT_NON_EXISTING ).toFile().getAbsolutePath() ) };
        assertArrayEquals( errorMessages, notification.getMessages().toArray() );
    }

    /**
     * Should notify error if project configurations not exists.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotifyErrorIfProjectConfigurationsNotExists() throws Exception {

        setImportElementActionPreRequisite();

        responseDTO = new SusResponseDTO();
        responseDTO.setData( convertStringToJsonNode( DATAOBJECT_VAULT_FILES_DATA_NOT_AVAILABLE ) );
        responseDTO.setSuccess( true );
        mockSuSClientFunctions( prepareURL( SELECTION_ITEM + SELECTION_ID, restAPI ), restAPI, responseDTO );
        importElementAction = new ImportProcessElementAction( prepareJobImpl(), element, parameters );
        importElementAction.setRestAPI( restAPI );
        final Notification notification = importElementAction.doAction();

        final String[] errorMessages = { MessagesUtil.getMessage( WFEMessages.PROJECT_CONFIGURATIONS_NOT_FOUND_FOR_OBJECT, PROJECT_ID ) };

        assertArrayEquals( errorMessages, notification.getMessages().toArray() );
    }

    /**
     * Should notify if any of the required project field is missing.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotifyErrorsForEachMissingField() throws Exception {
        setImportElementActionPreRequisite();

        element.getFields().clear();
        Notification notification = new Notification();

        importElementAction = new ImportProcessElementAction( prepareJobImpl(), element, new HashMap<>() );
        importElementAction.setRestAPI( restAPI );
        notification = importElementAction.doAction();
        System.out.println( notification.getErrors().size() );
        assertTrue( SHOULD_HAVE_4_ERRORS_AS_4_REQUIRED_FIELDS_ARE_MISSING, notification.getErrors().size() == 4 );

        System.out.println( notification.getMessages() );

        final String[] errorMessages = { MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING,
                ImportProcessElementAction.LABEL_OF_CONTAINER_FIELD_IN_DESIGNER ),
                MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, ImportProcessElementAction.LABEL_OF_OVER_WRITE_FLAG ),
                MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, ImportProcessElementAction.LABEL_OF_INPUT_DIRECTORY ),
                MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING,
                        ImportProcessElementAction.LABEL_OF_IMPORT_SETTINGS_FILE ) };

        assertArrayEquals( errorMessages, notification.getMessages().toArray() );
        assertTrue( notification.hasErrors() );
    }

    /**
     * Should notify error when regular file is going to import instead of zip or directory.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotifyErrorWhenRegularFileIsGoingToImportInsteadOfZipOrDirectory() throws Exception {

        setImportElementActionPreRequisite();

        final EngineFile inputZipOrDirectory = ( EngineFile ) parameters.get( PARAMETERS_ZIP_OR_PATH_KEY );

        inputZipOrDirectory.setPath( IN_VALID_PATH_TO_IMPORT );

        Notification notification = new Notification();
        prepareRestCredentials();

        responseDTO = new SusResponseDTO();
        responseDTO.setData( convertStringToJsonNode( DATAOBJECT_VAULT_FILES_DATA_NOT_AVAILABLE ) );
        responseDTO.setSuccess( true );
        mockSuSClientFunctions( prepareURL( SELECTION_ITEM + SELECTION_ID, restAPI ), restAPI, responseDTO );
        importElementAction = new ImportProcessElementAction( prepareJobImpl(), element, parameters );
        importElementAction.setRestAPI( restAPI );
        notification = importElementAction.doAction();
        assertTrue( SHOULD_HAVE_ONLY_ONE_ERROR, notification.getErrors().size() == ONE_ERROR );
        assertEquals( MessagesUtil.getMessage( WFEMessages.PATH_TO_IMPORT_SHOULD_ZIP_OR_DIRECTORY, IN_VALID_PATH_TO_IMPORT ),
                notification.getErrors().get( STARTING_INDEX ).getMessage() );

        assertTrue( notification.hasErrors() );
    }

    /**
     * Should notify if any of the required project field is missing.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotifyIfAnyOfTheRequiredProjectFieldIsMissing() throws Exception {
        setImportElementActionPreRequisite();

        final Field< ? > projectField = element.getFields().stream()
                .filter( field -> field.getLabel().contentEquals( ImportProcessElementAction.LABEL_OF_CONTAINER_FIELD_IN_DESIGNER ) )
                .findFirst().get();
        element.getFields().remove( projectField );
        Notification notification = new Notification();

        importElementAction = new ImportProcessElementAction( prepareJobImpl(), element, parameters );
        importElementAction.setRestAPI( restAPI );
        notification = importElementAction.doAction();
        assertTrue( SHOULD_HAVE_ONLY_ONE_ERROR, notification.getErrors().size() == ONE_ERROR );
        assertEquals( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING,
                        ImportProcessElementAction.LABEL_OF_CONTAINER_FIELD_IN_DESIGNER ),
                notification.getErrors().get( STARTING_INDEX ).getMessage() );
        assertTrue( notification.hasErrors() );
    }

    /**
     * Should notify the error if field type from parameters miss matches.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotifyTheErrorIfFieldTypeFromParametersMissMatches() throws Exception {
        setImportElementActionPreRequisite();

        Notification notification = new Notification();
        prepareRestCredentials();

        importElementAction = new ImportProcessElementAction( prepareJobImpl(), element, parameters );
        importElementAction.setRestAPI( restAPI );
        notification = importElementAction.doAction();
        assertTrue( SHOULD_HAVE_ONLY_ONE_ERROR, notification.getErrors().size() == ONE_ERROR );
        assertTrue( notification.hasErrors() );
    }

    /**
     * Should not throw exception when null import path provided.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotThrowExceptionWhenNullImportPathProvided() throws Exception {

        setImportElementActionPreRequisite();

        parameters.put( PARAMETERS_ZIP_OR_PATH_KEY, null );

        Notification notification = new Notification();
        prepareRestCredentials();

        importElementAction = new ImportProcessElementAction( prepareJobImpl(), element, parameters );
        importElementAction.setRestAPI( restAPI );
        notification = importElementAction.doAction();
        System.out.println( notification.getMessages() );
        assertTrue( SHOULD_HAVE_ONLY_ONE_ERROR, notification.getErrors().size() == TWO_ERROR );
        assertEquals( MessagesUtil.getMessage( WFEMessages.ARGUMENT_CAN_NOT_BE_NULL, PARAMETERS_ZIP_OR_PATH_KEY ),
                notification.getErrors().get( STARTING_INDEX ).getMessage() );

        assertTrue( notification.hasErrors() );

    }

    /**
     * Should require setting file when zip is provided for import.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldRequireSettingFileWhenZipIsProvidedForImport() throws Exception {
        setImportElementActionPreRequisite();

        final Field< String > settingInformationFile = ( Field< String > ) element.getFields().stream()
                .filter( field -> field.getLabel().contentEquals( ImportProcessElementAction.LABEL_OF_IMPORT_SETTINGS_FILE ) ).findFirst()
                .get();
        // removing setting file variable value
        settingInformationFile.setValue( "" );

        final EngineFile inputZipOrDirectory = ( EngineFile ) parameters.get( PARAMETERS_ZIP_OR_PATH_KEY );

        inputZipOrDirectory.setPath( ZIP_PATH_TO_IMPORT );
        mockUploadFile( ZIP_PATH_TO_IMPORT );
        Notification notification = new Notification();

        importElementAction = new ImportProcessElementAction( prepareJobImpl(), element, parameters );
        importElementAction.setRestAPI( restAPI );
        notification = importElementAction.doAction();
        assertTrue( notification.hasErrors() );
    }

}
