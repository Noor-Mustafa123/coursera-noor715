package de.soco.software.simuspace.workflow.processing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.constant.ConstantsElementKey;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.exception.UserWFParseException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.UserWorkflowImpl;

/**
 * This class test work flow output element action and validates all its public.
 *
 * @author nasir.farooq
 */
@Log4j2
@RunWith( PowerMockRunner.class )
@PrepareForTest( { SuSFileElementAction.class, SuSClient.class } )
public class SuSFileElementActionTest {

    /**
     * The Constant GET_DOWNLOAD_LINK_RESPONSE.
     */
    private static final String GET_DOWNLOAD_LINK_RESPONSE = "{\"url\":\"dataobject/download/file/1325?token=abc\",\"token\":\"abc\"}";

    /**
     * The Constant DOWNLOAD_VAULT_FILE_API.
     */
    private static final String DOWNLOAD_VAULT_FILE_API = "/api/v1/dataobject/download/file/1325?token=abc";

    /**
     * The dummy token.
     */
    private static String DUMMY_TOKEN = "eaa3a035689402e9581c115008dbf15c4ec30839";

    /**
     * The dummy user agent.
     */
    private static String DUMMY_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) SIMuSPACE/2.0.0 Chrome/53.0.2785.113 Electron/1.4.3 Safari/537.36";

    /**
     * The Constant HOST.
     */
    private static final String HOST = "testHost";

    /**
     * The Constant INVALID_MAPPING_KEY.
     */
    private static final String INVALID_MAPPING_KEY = "{{invalid.file1}}";

    /**
     * The Constant INVALID_MAPPING_VALUE.
     */
    private static final String INVALID_MAPPING_VALUE = "{{ivalid.file2}}";

    /**
     * The Constant JOB_ID.
     */
    private static final UUID JOB_ID = UUID.randomUUID();

    /**
     * The Constant KEY.
     */
    private static final String KEY = "Key";

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant parameters.
     */
    private static Map< String, Object > parameters = new HashMap<>();

    /**
     * The Constant PORT.
     */
    private static final String PORT = "testPort";

    /**
     * The Constant PROTOCOL.
     */
    private static final String PROTOCOL = "testProtocol";

    /**
     * The url download link.
     */
    private static String URL_DOWNLOAD_LINK = "/api/v1/dataobject/download/link/";

    /**
     * The Constant WORKFLOW_FILE_PATH.
     */
    private static final String USER_WORKFLOW_FILE_PATH = "/WF_User.js";

    /**
     * The Constant VALUE.
     */
    private static final String VALUE = "Value";

    /**
     * The Constant VAULT_ID.
     */
    private static final int VAULT_ID = 1;

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
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private static void mockSuSClientFunctions( String url, RestAPI restAPI ) throws JsonSerializationException {
        PowerMockito.mockStatic( SuSClient.class );

        JsonNode jsonNode = null;
        final ObjectMapper mapper = new ObjectMapper();

        try {
            jsonNode = mapper.readTree( GET_DOWNLOAD_LINK_RESPONSE );
        } catch ( final IOException e ) {

            log.error( e );

        }

        final SusResponseDTO responseDTO = new SusResponseDTO();
        responseDTO.setData( jsonNode );
        responseDTO.setSuccess( true );
        when( SuSClient.getRequest( url, prepareHeaders( restAPI ) ) ).thenReturn( responseDTO );

        final String vaultFile = "vaultfilePath";
        final List< Error > errors = new ArrayList<>();
        when( SuSClient.downloadRequest( prepareURL( ConstantsString.EMPTY_STRING, restAPI ) + DOWNLOAD_VAULT_FILE_API, vaultFile,
                prepareHeaders( restAPI ), errors ) ).thenReturn( vaultFile );
    }

    /**
     * It adds headers for required for communication with server.<br>
     *
     * @param restAPI
     *         the rest API
     *
     * @return requestHeaders
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

    /**
     * Setup.
     *
     * @throws FileNotFoundException
     *         the file not found exception
     * @throws UserWFParseException
     *         the user WF parse exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @BeforeClass
    public static void setup() throws FileNotFoundException, UserWFParseException, JsonSerializationException {

        final LinkedHashMap< String, String > file = new LinkedHashMap<>();
        file.put( "agent", "str" );
        file.put( "docId", "0" );
        file.put( "location", "0" );
        file.put( "path", "src/test/resources/copy.txt" );
        file.put( "type", "client" );
        parameters.put( "{{Input_Huz.object1}}", VAULT_ID );

        final LinkedHashMap< String, String > fileInput = new LinkedHashMap<>();
        fileInput.put( "agent", "str" );
        fileInput.put( "docId", "0" );
        fileInput.put( "location", "0" );
        fileInput.put( "path", "src/test/resources/test.txt" );
        fileInput.put( "type", "client" );
        parameters.put( "{{Output_Huz.file1}}", fileInput );

    }

    /**
     * The job impl.
     */
    private JobImpl jobImpl;

    /**
     * The sus file element action.
     */
    private SuSFileElementAction susFileElementAction;

    /**
     * The work flow DTO.
     */
    private LatestWorkFlowDTO workFlowDTO;

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
        final RestAPI restAPI = new RestAPI( PROTOCOL, HOST, PORT );
        restAPI.setRequestHeaders( headers );
        jobImpl.setServer( restAPI );
        jobImpl.setRequestHeaders( headers );
        jobImpl.setId( JOB_ID );
        return restAPI;
    }

    /**
     * It prepare userWorkflow Sets workflowDefinition , connections and elements in it. from config appended with url provided .
     *
     * @param WorkflowDTO
     *
     * @return UserWorkFlow
     */
    private UserWorkFlow prepareWorkflowModel( LatestWorkFlowDTO dto ) {

        final UserWorkFlow model = setWorkflowOwner( dto );

        String json = null;
        final Map< String, Object > map = dto.prepareDefinition();
        if ( map != null ) {
            json = JsonUtils.toJson( map );
        }
        final WorkflowDefinitionDTO workflowDefinitionDTO = JsonUtils.jsonToObject( json, WorkflowDefinitionDTO.class );

        model.setEdges( workflowDefinitionDTO.getElements().getEdges() );

        model.setNodes( setWorkflowElements( workflowDefinitionDTO ) );
        return model;
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setUp() throws Exception {
        workFlowDTO = JsonUtils.jsonStreamToObject( this.getClass().getResourceAsStream( USER_WORKFLOW_FILE_PATH ),
                LatestWorkFlowDTO.class );
        mockControl().resetToNice();
        jobImpl = new JobImpl();

    }

    /**
     * gets workflow elements from WorkflowDefinitionDTO
     *
     * @param WorkflowDefinitionDTO
     *
     * @return List<UserWFElement>
     */
    private List< UserWFElement > setWorkflowElements( WorkflowDefinitionDTO def ) {
        final List< UserWFElement > userElements = new ArrayList<>();
        for ( final WorkflowElement workflowElement : def.getElements().getNodes() ) {
            final UserWFElement element = workflowElement.getData();
            if ( ElementKeys.getkeys().contains( element.getKey() ) ) {
                userElements.add( element );
            }
        }
        return userElements;

    }

    /**
     * sets workflow owner in workflow model.
     *
     * @param WorkflowDTO
     *
     * @return UserWorkFlow
     */
    private UserWorkFlow setWorkflowOwner( LatestWorkFlowDTO dto ) {
        final UserWorkFlow model = new UserWorkflowImpl();
        model.setId( dto.getId().toString() );
        model.setName( dto.getName() );

        return model;

    }

    /**
     * Should execute successfully with no errors when valid mapping and parameters are given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldExecuteSuccessfullyWithNoErrorsWhenValidMappingAndParametersAreGiven() throws Exception {

        final RestAPI restAPI = prepareRestCredentials();

        UserWFElement userWFElement = null;

        mockControl().replay();
        mockSuSClientFunctions( prepareURL( URL_DOWNLOAD_LINK + VAULT_ID, restAPI ), restAPI );

        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workFlowDTO );

        for ( final UserWFElement userWFElementTemp : userWorkFlow.getNodes() ) {
            if ( userWFElementTemp.getKey().contentEquals( ConstantsElementKey.WFE_SUS_EXPORT_FILES ) ) {
                userWFElement = userWFElementTemp;
                break;
            }
        }

        assertNotNull( userWFElement );
        assertNotNull( userWFElement.getFields() );
        assertNotNull( userWFElement.getRunMode() );

        susFileElementAction = new SuSFileElementAction( prepareJobImpl(), userWFElement, parameters );
        susFileElementAction.setRestAPI( restAPI );
        final Notification notification = susFileElementAction.doAction();
        assertFalse( notification.hasErrors() );
    }

    /**
     * Should exit with error if null element is provieded.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldExitWithErrorIfNullElementIsProvieded() throws Exception {

        final RestAPI restAPI = prepareRestCredentials();

        mockControl().replay();
        mockSuSClientFunctions( prepareURL( URL_DOWNLOAD_LINK + VAULT_ID, restAPI ), restAPI );

        susFileElementAction = new SuSFileElementAction( prepareJobImpl(), null, parameters );
        final Notification errorNotification1 = susFileElementAction.doAction();
        assertTrue( errorNotification1.hasErrors() );
        assertEquals( errorNotification1.getErrors().get( 0 ).getMessage(),
                MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) );
    }

    /**
     * Should return notification with invalid mapping message when invalid mapping provided.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnNotificationWithInvalidMappingMessageWhenInvalidMappingProvided() throws Exception {

        final RestAPI restAPI = prepareRestCredentials();

        UserWFElement userWFElement = null;

        mockControl().replay();
        mockSuSClientFunctions( prepareURL( URL_DOWNLOAD_LINK + VAULT_ID, restAPI ), restAPI );

        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workFlowDTO );

        for ( final UserWFElement userWFElementTemp : userWorkFlow.getNodes() ) {
            if ( userWFElementTemp.getKey().contentEquals( ConstantsElementKey.WFE_SUS_EXPORT_FILES ) ) {
                userWFElement = userWFElementTemp;
                break;
            }
        }

        assertNotNull( userWFElement );
        assertNotNull( userWFElement.getFields() );
        assertNotNull( userWFElement.getRunMode() );

        final List< Field< ? > > fields = new ArrayList<>();

        for ( final Field< ? > field : userWFElement.getFields() ) {

            if ( field.getType().contentEquals( FieldTypes.TEXTAREA.getType() ) ) {
                final Field< String > textArea = new Field<>();
                textArea.setType( FieldTypes.TEXTAREA.getType() );
                textArea.setValue( INVALID_MAPPING_KEY + ConstantsString.COLON + INVALID_MAPPING_VALUE );
                fields.add( textArea );
            }
        }
        userWFElement.setFields( fields );

        susFileElementAction = new SuSFileElementAction( null, userWFElement, parameters );
        susFileElementAction.setRestAPI( restAPI );

        final Notification errorNotification = susFileElementAction.doAction();
        assertTrue( errorNotification.hasErrors() );
        assertEquals( ConstantsInteger.INTEGER_VALUE_TWO, errorNotification.getErrors().size() );
        assertEquals( MessagesUtil.getMessage( WFEMessages.MAPPING_KEY_OR_VALUE_IS_MISSING_IN_PARAMETERS, KEY, INVALID_MAPPING_KEY ),
                errorNotification.getErrors().get( 0 ).getMessage() );
        assertEquals( MessagesUtil.getMessage( WFEMessages.MAPPING_KEY_OR_VALUE_IS_MISSING_IN_PARAMETERS, VALUE, INVALID_MAPPING_VALUE ),
                errorNotification.getErrors().get( 1 ).getMessage() );
    }

}
