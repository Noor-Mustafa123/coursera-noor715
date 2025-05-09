/*
 *
 */

package de.soco.software.simuspace.workflow.processing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
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

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.UserWFElementImpl;

/**
 * The is the test class for public functions in {@link SusDataObjectExportElementAction}.
 */
@Log4j2
@RunWith( PowerMockRunner.class )
@PrepareForTest( { SusDataObjectExportElementAction.class, SuSClient.class } )
public class SusDataObjectExportElementActionTest {

    /**
     * The Constant FLAT_FILES_EXPORT_TYPE.
     */
    private static final String FLAT_FILES_EXPORT_TYPE = "flat";

    /**
     * The Constant EXPORT_TYPE.
     */
    private static final String EXPORT_TYPE = "Type";

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
    private static final String PROTOCOL_OF_URL = "http";

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
     * The Constant parameters.
     */
    private final static Map< String, Object > parameters = new HashMap<>();

    /**
     * The directory path.
     */
    private static final String DIRECTORY_PATH = ConstantsString.HOME_DIRECTORY;

    /**
     * The filter with zip and json extension.
     */
    private static final String FILTER_WITH_ZIP_AND_JSON_EXTENSION = "*.zip,*.json";

    /**
     * The first index.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The sus data object element action.
     */
    private static SusDataObjectExportElementAction susDataObjectElementAction;

    /**
     * The mocked dataobject id to download.
     */
    private static final String MOCKED_DATAOBJECT_ID_TO_DOWNLOAD = "1";

    /**
     * The user field selection.
     */
    private static final String USER_FIELD_SELECTION = "select";

    /**
     * The user field action.
     */
    private static final String USER_FIELD_ACTION = "Action";

    /**
     * The user field export.
     */
    private static final String USER_FIELD_EXPORT = "Export";

    /**
     * The target already exists.
     */
    private static final String TARGET_ALREADY_EXISTS = "Target already exists";

    /**
     * The files filter.
     */
    private static final String FILES_FILTER = "Files filter";

    /**
     * The export path.
     */
    private static final String EXPORT_PATH = "Export Path";

    ;

    /**
     * The data object.
     */
    private static final String DATA_OBJECT = "DataObject";

    /**
     * The field key text.
     */
    private static final String FIELD_KEY_TEXT = "text";

    /**
     * The job impl.
     */
    private static JobImpl jobImpl;

    /**
     * The fields.
     */
    private static List< Field< ? > > fields;

    /**
     * The response DTO.
     */
    private static SusResponseDTO responseDTO;

    /**
     * The file filters parameters map key.
     */
    private static final String FILE_FILTERS_PARAMETERS_MAP_KEY = "{{input.filefilters}}";

    /**
     * The export path parameters map key.
     */
    private static final String EXPORT_PATH_PARAMETERS_MAP_KEY = "{{input.exportpath}}";

    ;

    /**
     * The data object parameters map key.
     */
    private static final String DATA_OBJECT_PARAMETERS_MAP_KEY = "{{input.dataobject}}";

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
     *         the rest API
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
     * Prepare URL.
     *
     * @param url
     *         the url
     * @param restAPI
     *         the rest API
     *
     * @return the string
     */
    private static String prepareURL( String url, RestAPI restAPI ) {
        if ( restAPI != null ) {
            return restAPI.getProtocol() + restAPI.getHostname() + ConstantsString.COLON + restAPI.getPort() + url;
        }
        return ConstantsString.EMPTY_STRING;

    }

    /**
     * The overwrite.
     */
    private final String OVERWRITE = "overwrite";

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
     * @param exportValue
     *         the export value
     * @param overWriteOrExistValue
     *         the over write or exist value
     * @param filesFilterValue
     *         the files filter value
     * @param exportPathValue
     *         the export path value
     */
    private void setSusDataObjectField( String exportValue, String overWriteOrExistValue, String filesFilterValue,
            String exportPathValue ) {
        element = new UserWFElementImpl();
        fields = new ArrayList<>();
        parameters.put( FILE_FILTERS_PARAMETERS_MAP_KEY, FILTER_WITH_ZIP_AND_JSON_EXTENSION );
        parameters.put( EXPORT_PATH_PARAMETERS_MAP_KEY, new EngineFile( System.getProperty( DIRECTORY_PATH ) ) );
        parameters.put( DATA_OBJECT_PARAMETERS_MAP_KEY, MOCKED_DATAOBJECT_ID_TO_DOWNLOAD );
        final Field< String > selectAction = new Field<>();
        selectAction.setType( USER_FIELD_SELECTION );
        selectAction.setLabel( USER_FIELD_ACTION );
        selectAction.setValue( exportValue );
        fields.add( selectAction );
        final Field< String > overWriteExisting = new Field<>();
        overWriteExisting.setType( USER_FIELD_SELECTION );
        overWriteExisting.setLabel( TARGET_ALREADY_EXISTS );
        overWriteExisting.setValue( overWriteOrExistValue );
        overWriteExisting.setVariableMode( true );
        fields.add( overWriteExisting );
        final Field< String > filesFilter = new Field<>();
        filesFilter.setType( FIELD_KEY_TEXT );
        filesFilter.setLabel( FILES_FILTER );
        filesFilter.setValue( filesFilterValue );
        filesFilter.setVariableMode( true );
        fields.add( filesFilter );
        final Field< String > exportPath = new Field<>();
        exportPath.setType( FIELD_KEY_TEXT );
        exportPath.setLabel( EXPORT_PATH );
        exportPath.setValue( exportPathValue );
        exportPath.setVariableMode( true );
        fields.add( exportPath );
        final Field< String > dataObject = new Field<>();
        dataObject.setType( FIELD_KEY_TEXT );
        dataObject.setLabel( DATA_OBJECT );
        dataObject.setValue( DATA_OBJECT_PARAMETERS_MAP_KEY );
        dataObject.setVariableMode( true );
        fields.add( dataObject );

        final Field< String > exportType = new Field<>();
        exportType.setType( USER_FIELD_SELECTION );
        exportType.setLabel( EXPORT_TYPE );
        exportType.setValue( FLAT_FILES_EXPORT_TYPE );
        exportType.setVariableMode( true );
        fields.add( exportType );

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
        mockControl.resetToNice();
    }

    /**
     * Should notify when data objects are null.
     */
    @Test
    public void shouldNotifyWhenDataObjectsAreNull() {
        setSusDataObjectField( USER_FIELD_EXPORT, OVERWRITE, FILE_FILTERS_PARAMETERS_MAP_KEY, EXPORT_PATH_PARAMETERS_MAP_KEY );
        Notification notification = new Notification();
        final RestAPI restAPI = prepareRestCredentials();
        mockControl.replay();
        mockSuSClientFunctions( prepareURL( null, restAPI ), restAPI, responseDTO );
        susDataObjectElementAction = new SusDataObjectExportElementAction( prepareJobImpl(), element, parameters );
        susDataObjectElementAction.setRestAPI( restAPI );
        notification = susDataObjectElementAction.doAction();
        assertTrue( notification.hasErrors() );
        assertEquals( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ),
                notification.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * Should notify when data objects response is null.
     */
    @Test
    public void shouldNotifyWhenDataObjectsResponseIsNull() {
        setSusDataObjectField( USER_FIELD_EXPORT, OVERWRITE, FILE_FILTERS_PARAMETERS_MAP_KEY, EXPORT_PATH_PARAMETERS_MAP_KEY );
        Notification notification = new Notification();
        final RestAPI restAPI = prepareRestCredentials();
        responseDTO = new SusResponseDTO();
        responseDTO.setData( convertStringToJsonNode( ConstantsString.EMPTY_STRING ) );
        responseDTO.setSuccess( true );
        mockSuSClientFunctions( prepareURL( null, restAPI ), restAPI, responseDTO );
        susDataObjectElementAction = new SusDataObjectExportElementAction( prepareJobImpl(), element, parameters );
        susDataObjectElementAction.setRestAPI( restAPI );
        mockControl.replay();
        notification = susDataObjectElementAction.doAction();
        assertTrue( notification.hasErrors() );
        assertEquals( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ),
                notification.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * Should notify when element is null.
     */
    @Test
    public void shouldNotifyWhenElementIsNull() {
        Notification notification = new Notification();
        element = null;
        susDataObjectElementAction = new SusDataObjectExportElementAction( prepareJobImpl(), element, parameters );
        notification = susDataObjectElementAction.doAction();
        assertTrue( notification.hasErrors() );
        assertEquals( MessageBundleFactory.getMessage( Messages.OBJECT_CANT_BE_NULL.getKey() ),
                notification.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * Should notify when no objects are found in data objects response.
     */
    @Test
    public void shouldNotifyWhenNoObjectsAreFoundInDataObjectsResponse() {
        setSusDataObjectField( USER_FIELD_EXPORT, OVERWRITE, FILE_FILTERS_PARAMETERS_MAP_KEY, EXPORT_PATH_PARAMETERS_MAP_KEY );
        Notification notification = new Notification();
        final RestAPI restAPI = prepareRestCredentials();

        responseDTO = new SusResponseDTO();
        responseDTO.setData( convertStringToJsonNode( ConstantsString.EMPTY_STRING ) );
        responseDTO.setSuccess( true );
        mockSuSClientFunctions( prepareURL( null, restAPI ), restAPI, responseDTO );
        susDataObjectElementAction = new SusDataObjectExportElementAction( prepareJobImpl(), element, parameters );
        susDataObjectElementAction.setRestAPI( restAPI );
        notification = susDataObjectElementAction.doAction();
        mockControl.replay();
        assertTrue( notification.hasErrors() );
        assertEquals( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ),
                notification.getErrors().get( FIRST_INDEX ).getMessage() );
    }

}
