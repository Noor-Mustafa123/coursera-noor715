package de.soco.software.simuspace.workflow.processing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
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
import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.exception.WorkFlowExecutionException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.UserWFElementImpl;

/**
 * This is test class for {@link FilesImportElementAction } will test public function with valid and invalid expressions.
 *
 * @author sces123
 */
@Log4j2
@RunWith( PowerMockRunner.class )
@PrepareForTest( { FilesImportElementAction.class, SuSClient.class } )
public class FilesImportElementActionTest {

    /**
     * The Constant UPLOAD_TYPE.
     */
    private static final String UPLOAD_TYPE = "client";

    /**
     * The Constant IMAGE_NAME_UPLOAD.
     */
    private static final String IMAGE_NAME_UPLOAD = "1.jpg";

    /**
     * The Constant DOCUMENT_ID.
     */
    private static final int DOCUMENT_ID = 1;

    /**
     * The Constant DEFAUL_LOCATION.
     */
    private static final int DEFAUL_LOCATION = 0;

    /**
     * The user WF element reference .
     */
    private static UserWFElement element;

    /**
     * The Constant for job id.
     */
    private static final UUID JOB_ID = UUID.randomUUID();

    /**
     * The files import element action.
     */
    private static FilesImportElementAction filesImportElementAction;

    /**
     * The Constant parameters is used to hold the element's parameters and their values.
     */
    private final static Map< String, Object > parameters = new HashMap<>();

    /**
     * The vault file payload.
     */
    private final static String VAULT_FILE_PAYLOAD = "{\"draw\":2,\"start\":0,\"length\":10,\"search\":[]}";

    /**
     * The Constant ADD_FILE_PAYLOAD.
     */
    private final static String ADD_FILE_PAYLOAD = "{\"files\":[{\"path\":\"1.jpg\",\"docId\":1,\"selType\":\"client\",\"locationId\":0}]}";

    /**
     * The dummy token.
     */
    private static String DUMMY_TOKEN = "eaa3a035689402e9581c115008dbf15c4ec30839";

    /**
     * The dummy user agent.
     */
    private static String DUMMY_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) SIMuSPACE/2.0.0 Chrome/53.0.2785.113 Electron/1.4.3 Safari/537.36";

    /**
     * The Constant for port.
     */
    private static final String PORT_OF_URL = "8080";

    /**
     * The Constant for URL.
     */
    private static final String PROTOCOL_OF_URL = "http://";

    /**
     * The Constant for host.
     */
    private static final String HOST_OF_URL = "localhost";

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The data object vaults.
     */
    private final static String DATAOBJECT_VAULTS = "{\"draw\":2,\"start\":0,\"displayStart\":0,\"length\":10,\"recordsTotal\":2,\"recordsFiltered\":2,\"searchId\":null,\"filtersJson\":null,\"data\":[{\"id\":8,\"name\":\"2.jpg\",\"size\":\"94725\",\"dataobjectId\":6,\"tail\":false,\"owner\":\"Ahsan\",\"exportable\":true}]}";

    /**
     * The Constant for response of dataobject files not available.
     */
    private final static String DATAOBJECT_VAULT_FILES_DATA_NOT_AVAILABLE = "{\"draw\":2,\"start\":0,\"displayStart\":0,\"length\":10,\"recordsTotal\":2,\"recordsFiltered\":2,\"searchId\":null,\"filtersJson\":null,\"data\":[]}";

    /**
     * The Constant for response of file updation.
     */
    private final static String FILE_UPDATED_RESPONSE = "{\"success\":true,\"message\":{\"type\":\"SUCCESS\",\"content\":\"Records updated.\"}}";

    /**
     * The Constant for get vault files of dataobject URL.
     */
    private static final String URL_VAULT_FILE_LINK = "/api/v1/dataobject/vaultfiles/1";

    /**
     * The Constant for add files URL in dataobejct.
     */
    private static final String ADD_FILES_IN_DATAOBJECT = "/api/v1/dataobject/add/files/1";

    /**
     * The Constant for upload file URL.
     */
    private static final String URL_UPLOAD_FILE = "/api/v1/document/upload";

    /**
     * The Constant file path.
     */
    private static final String FILE_PATH = "abc.txt";

    /**
     * The Constant SOME_ERROR_MESSAGE_IN_CASE_OF_SUCCESS_FALSE.
     */
    private static final String SOME_ERROR_MESSAGE_IN_CASE_OF_SUCCESS_FALSE = "Error message";

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
        final JsonFactory factory = mapper.getJsonFactory(); // since 2.1 use
        // mapper.getFactory()
        // instead
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
     * Prepare headers.
     *
     * @param restAPI
     *         the rest API
     *
     * @return the map
     *
     * @throws SusException
     *         the sus exception
     */
    private static Map< String, String > prepareHeaders( RestAPI restAPI ) throws SusException {
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

    /**
     * The Constant for target already exists.
     */
    private final String TARGET_ALREADY_EXISTS = "Target already exists";

    /**
     * The simcore files are not available.
     */
    private final String SIMCORE_FILES_ARE_NOT_AVAILABLE = "Simcore files are not availabe !";

    /**
     * The vault files are not available.
     */
    private final String VAULT_FILES_ARE_NOT_AVAILABLE = "Vault Files are not available";

    /**
     * The add files in dataobject failed.
     */
    private final String ADD_FILES_IN_DATAOBJECT_FAILED = "Add files in dataobject failed";

    /**
     * The Constant for file filter.
     */
    private final String FILTER_WITH_JPG_EXTENSION = "*jpg";

    /**
     * The Constant for user field select.
     */
    private final String USER_FIELD_SELECTION = "select";

    /**
     * The Constant for over write option value.
     */
    private final String OVER_WRITE_OPTION_VALUE_SKIP = "skip";

    /**
     * The constant OVERWRITE used to check whether the file to download needs to be overwritten to existing or skipped.
     */
    private final String OVER_WRITE_OPTION_VALUE_OVERWRITE = "overwrite";

    /**
     * The Constant for label files filter.
     */
    private final String FILES_FILTER = "Files filter";

    /**
     * The Constant for label directory path.
     */
    private final String DIRECTORY_PATH = "Directory Path";

    /**
     * The Constant for label data object.
     */
    private final String DATA_OBJECT = "DataObject";

    /**
     * The Constant for field key text.
     */
    private final String FIELD_KEY_TEXT = "text";

    /**
     * The mocked dataobject id to download.
     */
    private final String MOCKED_DATAOBJECT_ID_TO_ADD_FILES = "1";

    /**
     * The first index.
     */
    private final int FIRST_INDEX = 0;

    /**
     * The Constant FILE_FILTERS_PARAMETERS_MAP_KEY is a parameters map key used for filter types for downloading files .
     */
    private final String FILE_FILTERS_PARAMETERS_MAP_KEY = "{{input.filefilters}}";

    /**
     * The dataobject parameters map key.
     */
    private final String DATAOBJECT_PARAMETERS_MAP_KEY = "{{input.dataobjectId}}";

    /**
     * The directory path parameters map key.
     */
    private final String DIRECTORY_PATH_PARAMETERS_MAP_KEY = "{{input.directoryPath}}";

    /**
     * List of fields for element.
     */
    private List< Field< ? > > fields;

    /**
     * The job.
     */
    private JobImpl jobImpl;

    /**
     * Creates the file upload.
     *
     * @return the file upload
     */
    private FileUpload createFileUpload() {
        final FileUpload uploadResponse = new FileUpload();
        uploadResponse.setSelType( UPLOAD_TYPE );
        uploadResponse.setPath( IMAGE_NAME_UPLOAD );
        uploadResponse.setDocId( DOCUMENT_ID );
        uploadResponse.setLocationId( DEFAUL_LOCATION );
        return uploadResponse;
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
     * Sets the sus data object field.
     *
     * @param overWriteOrExistValue
     *         the over write or exist value
     * @param filesFilterValue
     *         the files filter value
     * @param dirPathValue
     *         the directory path value
     */
    private void setFilesImportField( String overWriteOrExistValue, String filesFilterValue, String dirPathValue ) {

        element = new UserWFElementImpl();
        fields = new ArrayList<>();
        parameters.put( FILE_FILTERS_PARAMETERS_MAP_KEY, FILTER_WITH_JPG_EXTENSION );
        parameters.put( DATAOBJECT_PARAMETERS_MAP_KEY, MOCKED_DATAOBJECT_ID_TO_ADD_FILES );
        parameters.put( DIRECTORY_PATH_PARAMETERS_MAP_KEY, this.getClass().getResource( "" ).getFile() );
        final Field< String > overWriteExisting = new Field<>();
        overWriteExisting.setType( USER_FIELD_SELECTION );
        overWriteExisting.setLabel( TARGET_ALREADY_EXISTS );
        overWriteExisting.setValue( overWriteOrExistValue );
        fields.add( overWriteExisting );
        final Field< String > filesFilter = new Field<>();
        filesFilter.setType( FIELD_KEY_TEXT );
        filesFilter.setLabel( FILES_FILTER );
        filesFilter.setValue( filesFilterValue );
        fields.add( filesFilter );
        final Field< String > dirpath = new Field<>();
        dirpath.setType( FIELD_KEY_TEXT );
        dirpath.setLabel( DIRECTORY_PATH );
        dirpath.setValue( dirPathValue );
        fields.add( dirpath );
        final Field< String > dataObject = new Field<>();
        dataObject.setType( FIELD_KEY_TEXT );
        dataObject.setLabel( DATA_OBJECT );
        dataObject.setValue( DATAOBJECT_PARAMETERS_MAP_KEY );
        fields.add( dataObject );
        element.setFields( fields );
    }

    /**
     * Should import with overwrite yes and with some extensions.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldImportWithOverwriteFilesFlagAndSomeExtensions() throws Exception {
        final Notification notif = new Notification();
        setFilesImportField( OVER_WRITE_OPTION_VALUE_OVERWRITE, FILE_FILTERS_PARAMETERS_MAP_KEY, DIRECTORY_PATH_PARAMETERS_MAP_KEY );
        final RestAPI restAPI = prepareRestCredentials();

        final SusResponseDTO responseDTO = new SusResponseDTO();
        responseDTO.setData( convertStringToJsonNode( DATAOBJECT_VAULTS ) );
        responseDTO.setSuccess( true );

        final SusResponseDTO addFileResponseDTO = new SusResponseDTO();
        addFileResponseDTO.setData( convertStringToJsonNode( FILE_UPDATED_RESPONSE ) );
        addFileResponseDTO.setSuccess( true );

        PowerMockito.mockStatic( SuSClient.class );
        final FileUpload uploadResponse = createFileUpload();

        when( SuSClient.postRequest( prepareURL( URL_VAULT_FILE_LINK, restAPI ), VAULT_FILE_PAYLOAD,
                prepareHeaders( restAPI ) ) ).thenReturn( responseDTO );
        when( SuSClient.uploadFileRequest( prepareURL( URL_UPLOAD_FILE, restAPI ), FILE_PATH, prepareHeaders( restAPI ) ) ).thenReturn(
                uploadResponse );
        when( SuSClient.putRequest( prepareURL( ADD_FILES_IN_DATAOBJECT, restAPI ), prepareHeaders( restAPI ),
                ADD_FILE_PAYLOAD ) ).thenReturn( addFileResponseDTO );

        filesImportElementAction = new FilesImportElementAction( prepareJobImpl(), element, parameters );
        filesImportElementAction.setRestAPI( restAPI );
        filesImportElementAction.doAction();

        assertFalse( notif.hasErrors() );
    }

    /**
     * Should import with overwrite yes and with some extensions.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldImportWithSkipFilesFlagAndSomeExtensions() throws Exception {
        final Notification notif = new Notification();
        setFilesImportField( OVER_WRITE_OPTION_VALUE_SKIP, FILE_FILTERS_PARAMETERS_MAP_KEY, DIRECTORY_PATH_PARAMETERS_MAP_KEY );
        final RestAPI restAPI = prepareRestCredentials();

        final SusResponseDTO responseDTO = new SusResponseDTO();
        responseDTO.setData( convertStringToJsonNode( DATAOBJECT_VAULTS ) );
        responseDTO.setSuccess( true );

        final SusResponseDTO addFileResponseDTO = new SusResponseDTO();
        addFileResponseDTO.setData( convertStringToJsonNode( FILE_UPDATED_RESPONSE ) );
        addFileResponseDTO.setSuccess( true );

        PowerMockito.mockStatic( SuSClient.class );
        final FileUpload uploadResponse = createFileUpload();

        when( SuSClient.postRequest( prepareURL( URL_VAULT_FILE_LINK, restAPI ), VAULT_FILE_PAYLOAD,
                prepareHeaders( restAPI ) ) ).thenReturn( responseDTO );
        when( SuSClient.uploadFileRequest( prepareURL( URL_UPLOAD_FILE, restAPI ), FILE_PATH, prepareHeaders( restAPI ) ) ).thenReturn(
                uploadResponse );
        when( SuSClient.putRequest( prepareURL( ADD_FILES_IN_DATAOBJECT, restAPI ), prepareHeaders( restAPI ),
                ADD_FILE_PAYLOAD ) ).thenReturn( addFileResponseDTO );

        filesImportElementAction = new FilesImportElementAction( prepareJobImpl(), element, parameters );
        filesImportElementAction.setRestAPI( restAPI );
        filesImportElementAction.doAction();

        assertFalse( notif.hasErrors() );
    }

    /**
     * Should notify when add vault files response is null.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotifyWhenAddVaultFilesResponseIsNull() throws Exception {
        Notification notif = new Notification();
        setFilesImportField( OVER_WRITE_OPTION_VALUE_SKIP, FILE_FILTERS_PARAMETERS_MAP_KEY, DIRECTORY_PATH_PARAMETERS_MAP_KEY );
        final RestAPI restAPI = prepareRestCredentials();

        final SusResponseDTO responseDTO = new SusResponseDTO();
        responseDTO.setData( convertStringToJsonNode( DATAOBJECT_VAULTS ) );
        responseDTO.setSuccess( true );

        final SusResponseDTO addFileResponseDTO = null;

        PowerMockito.mockStatic( SuSClient.class );
        final FileUpload uploadResponse = createFileUpload();

        when( SuSClient.postRequest( prepareURL( URL_VAULT_FILE_LINK, restAPI ), VAULT_FILE_PAYLOAD,
                prepareHeaders( restAPI ) ) ).thenReturn( responseDTO );
        when( SuSClient.uploadFileRequest( prepareURL( URL_UPLOAD_FILE, restAPI ), FILE_PATH, prepareHeaders( restAPI ) ) ).thenReturn(
                uploadResponse );
        when( SuSClient.putRequest( prepareURL( ADD_FILES_IN_DATAOBJECT, restAPI ), prepareHeaders( restAPI ),
                ADD_FILE_PAYLOAD ) ).thenReturn( addFileResponseDTO );

        filesImportElementAction = new FilesImportElementAction( prepareJobImpl(), element, parameters );
        filesImportElementAction.setRestAPI( restAPI );
        notif = filesImportElementAction.doAction();

        assertTrue( notif.hasErrors() );
        assertEquals( ADD_FILES_IN_DATAOBJECT_FAILED, notif.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * Should notify when add vault files response success false.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotifyWhenAddVaultFilesResponseSuccessFalse() throws Exception {
        Notification notif = new Notification();
        setFilesImportField( OVER_WRITE_OPTION_VALUE_SKIP, FILE_FILTERS_PARAMETERS_MAP_KEY, DIRECTORY_PATH_PARAMETERS_MAP_KEY );
        final RestAPI restAPI = prepareRestCredentials();

        final SusResponseDTO responseDTO = new SusResponseDTO();
        responseDTO.setData( convertStringToJsonNode( DATAOBJECT_VAULTS ) );
        responseDTO.setSuccess( true );

        final SusResponseDTO addFileResponseDTO = new SusResponseDTO();
        responseDTO.setSuccess( false );
        responseDTO.setMessage( new Message( "", SOME_ERROR_MESSAGE_IN_CASE_OF_SUCCESS_FALSE ) );

        PowerMockito.mockStatic( SuSClient.class );
        final FileUpload uploadResponse = createFileUpload();

        when( SuSClient.postRequest( prepareURL( URL_VAULT_FILE_LINK, restAPI ), VAULT_FILE_PAYLOAD,
                prepareHeaders( restAPI ) ) ).thenReturn( responseDTO );
        when( SuSClient.uploadFileRequest( prepareURL( URL_UPLOAD_FILE, restAPI ), FILE_PATH, prepareHeaders( restAPI ) ) ).thenReturn(
                uploadResponse );
        when( SuSClient.putRequest( prepareURL( ADD_FILES_IN_DATAOBJECT, restAPI ), prepareHeaders( restAPI ),
                ADD_FILE_PAYLOAD ) ).thenReturn( addFileResponseDTO );

        filesImportElementAction = new FilesImportElementAction( prepareJobImpl(), element, parameters );
        filesImportElementAction.setRestAPI( restAPI );
        notif = filesImportElementAction.doAction();

        assertTrue( notif.hasErrors() );
        assertEquals( SOME_ERROR_MESSAGE_IN_CASE_OF_SUCCESS_FALSE, notif.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * Should notify when simcore response with no vault files available.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldNotifyWhenDirectoryPathNotExists() throws SusException {
        Notification notif = new Notification();
        setFilesImportField( OVER_WRITE_OPTION_VALUE_SKIP, FILE_FILTERS_PARAMETERS_MAP_KEY, DIRECTORY_PATH_PARAMETERS_MAP_KEY );
        final RestAPI restAPI = prepareRestCredentials();

        final SusResponseDTO responseDTO = new SusResponseDTO();
        responseDTO.setData( convertStringToJsonNode( DATAOBJECT_VAULT_FILES_DATA_NOT_AVAILABLE ) );
        responseDTO.setSuccess( true );

        PowerMockito.mockStatic( SuSClient.class );

        when( SuSClient.postRequest( prepareURL( URL_VAULT_FILE_LINK, restAPI ), VAULT_FILE_PAYLOAD,
                prepareHeaders( restAPI ) ) ).thenReturn( responseDTO );

        filesImportElementAction = new FilesImportElementAction( prepareJobImpl(), element, parameters );
        filesImportElementAction.setRestAPI( restAPI );
        notif = filesImportElementAction.doAction();
        assertTrue( notif.hasErrors() );
        assertEquals( SIMCORE_FILES_ARE_NOT_AVAILABLE, notif.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * Should notify when element is null.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void shouldNotifyWhenElementIsNull() throws WorkFlowExecutionException, JsonSerializationException {

        Notification notification = new Notification();
        element = null;
        filesImportElementAction = new FilesImportElementAction( prepareJobImpl(), element, parameters );
        notification = filesImportElementAction.doAction();
        assertTrue( notification.hasErrors() );
        assertEquals( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ),
                notification.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * Should notify when get vault files response success false.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotifyWhenGetVaultFilesResponseSuccessFalse() throws Exception {
        Notification notif = new Notification();
        setFilesImportField( OVER_WRITE_OPTION_VALUE_SKIP, FILE_FILTERS_PARAMETERS_MAP_KEY, DIRECTORY_PATH_PARAMETERS_MAP_KEY );
        final RestAPI restAPI = prepareRestCredentials();

        final SusResponseDTO responseDTO = new SusResponseDTO();
        responseDTO.setSuccess( false );
        responseDTO.setMessage( new Message( "", SOME_ERROR_MESSAGE_IN_CASE_OF_SUCCESS_FALSE ) );

        final SusResponseDTO addFileResponseDTO = new SusResponseDTO();
        addFileResponseDTO.setData( convertStringToJsonNode( FILE_UPDATED_RESPONSE ) );
        addFileResponseDTO.setSuccess( true );

        PowerMockito.mockStatic( SuSClient.class );
        final FileUpload uploadResponse = createFileUpload();

        when( SuSClient.postRequest( prepareURL( URL_VAULT_FILE_LINK, restAPI ), VAULT_FILE_PAYLOAD,
                prepareHeaders( restAPI ) ) ).thenReturn( responseDTO );
        when( SuSClient.uploadFileRequest( prepareURL( URL_UPLOAD_FILE, restAPI ), FILE_PATH, prepareHeaders( restAPI ) ) ).thenReturn(
                uploadResponse );
        when( SuSClient.putRequest( prepareURL( ADD_FILES_IN_DATAOBJECT, restAPI ), prepareHeaders( restAPI ),
                ADD_FILE_PAYLOAD ) ).thenReturn( addFileResponseDTO );

        filesImportElementAction = new FilesImportElementAction( prepareJobImpl(), element, parameters );
        filesImportElementAction.setRestAPI( restAPI );
        notif = filesImportElementAction.doAction();

        assertTrue( notif.hasErrors() );
        assertEquals( SOME_ERROR_MESSAGE_IN_CASE_OF_SUCCESS_FALSE, notif.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * Should import with overwrite yes and with some extensions.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotifyWhenVaultFilesAreNotAvailable() throws Exception {
        Notification notif = new Notification();
        setFilesImportField( OVER_WRITE_OPTION_VALUE_SKIP, FILE_FILTERS_PARAMETERS_MAP_KEY, DIRECTORY_PATH_PARAMETERS_MAP_KEY );
        final RestAPI restAPI = prepareRestCredentials();

        final SusResponseDTO responseDTO = null;

        final SusResponseDTO addFileResponseDTO = new SusResponseDTO();
        addFileResponseDTO.setData( convertStringToJsonNode( FILE_UPDATED_RESPONSE ) );
        addFileResponseDTO.setSuccess( true );

        PowerMockito.mockStatic( SuSClient.class );
        final FileUpload uploadResponse = createFileUpload();

        when( SuSClient.postRequest( prepareURL( URL_VAULT_FILE_LINK, restAPI ), VAULT_FILE_PAYLOAD,
                prepareHeaders( restAPI ) ) ).thenReturn( responseDTO );
        when( SuSClient.uploadFileRequest( prepareURL( URL_UPLOAD_FILE, restAPI ), FILE_PATH, prepareHeaders( restAPI ) ) ).thenReturn(
                uploadResponse );
        when( SuSClient.putRequest( prepareURL( ADD_FILES_IN_DATAOBJECT, restAPI ), prepareHeaders( restAPI ),
                ADD_FILE_PAYLOAD ) ).thenReturn( addFileResponseDTO );

        filesImportElementAction = new FilesImportElementAction( prepareJobImpl(), element, parameters );
        filesImportElementAction.setRestAPI( restAPI );
        notif = filesImportElementAction.doAction();

        assertTrue( notif.hasErrors() );
        assertEquals( VAULT_FILES_ARE_NOT_AVAILABLE, notif.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * Should notify when some fields are missing.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void shouldNotImportAndNotifyWhenFilesFlagIsInvalid() throws WorkFlowExecutionException, JsonSerializationException {
        setFilesImportField( ConstantsString.EMPTY_STRING, FILE_FILTERS_PARAMETERS_MAP_KEY, DIRECTORY_PATH_PARAMETERS_MAP_KEY );
        Notification notification = new Notification();
        filesImportElementAction = new FilesImportElementAction( prepareJobImpl(), element, parameters );
        notification = filesImportElementAction.doAction();
        assertTrue( notification.hasErrors() );
        assertEquals( MessagesUtil.getMessage( WFEMessages.SOME_FIELDS_ARE_MISSING ),
                notification.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * Should notify when some fields are missing.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void shouldNotImportAndNotifyWhenSomeFieldsAreMissing() throws WorkFlowExecutionException, JsonSerializationException {
        setFilesImportField( OVER_WRITE_OPTION_VALUE_OVERWRITE, ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING );
        Notification notification = new Notification();
        filesImportElementAction = new FilesImportElementAction( prepareJobImpl(), element, parameters );
        notification = filesImportElementAction.doAction();
        assertTrue( notification.hasErrors() );
        assertEquals( MessagesUtil.getMessage( WFEMessages.SOME_FIELDS_ARE_MISSING ),
                notification.getErrors().get( FIRST_INDEX ).getMessage() );
    }

}
