package de.soco.software.simuspace.workflow.processing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.internal.WhiteboxImpl;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.exception.UserWFParseException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.NodeEdge;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.UserWorkflowImpl;
import de.soco.software.simuspace.workflow.model.impl.WorkflowConfigurationImpl;
import de.soco.software.simuspace.workflow.util.Diagraph;
import de.soco.software.simuspace.workflow.util.JobLog;

/**
 * This Class contains test cases that test all the possible scenarios in execution of workflow. The test cases files from input paths and
 * validate them.
 */
@Log4j2
@PrepareForTest( WorkflowExecutionManagerImpl.class )
public class WorkflowExecutionManagerImplTest {

    /**
     * The config.
     */
    private static WorkflowConfigurationImpl config;

    /**
     * The dummy token.
     */
    private static String DUMMY_TOKEN = "eaa3a035689402e9581c115008dbf15c4ec30839";

    /**
     * The dummy user agent.
     */
    private static String DUMMY_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) SIMuSPACE/2.0.0 Chrome/53.0.2785.113 Electron/1.4.3 Safari/537.36";

    /**
     * The get graph.
     */
    private static String GET_GRAPH = "getGraph";

    /**
     * The job impl.
     */
    private static Job jobImpl;

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The prepare headers.
     */
    private static String PREPARE_HEADERS = "prepareHeaders";

    /**
     * The prepare url.
     */
    private static String PREPARE_URL = "prepareURL";

    /**
     * The set wokflow definition.
     */
    private static String SET_WOKFLOW_DEFINITION = "getWorkflowDefinition";

    /**
     * The set workflow elements.
     */
    private static String SET_WORKFLOW_ELEMENTS = "setWorkflowElements";

    /**
     * The set workflow owner.
     */
    private static String SET_WORKFLOW_OWNER = "setWorkflowOwner";

    /**
     * The sim id.
     */
    static UUID SIM_ID = UUID.randomUUID();

    /**
     * The user workflow.
     */
    static UserWorkFlow userWorkflow;

    /**
     * The valid workflow.
     */
    private static String VALID_WORKFLOW = "src/test/resources/WF_User.js";

    /**
     * The valid workflow dto.
     */
    private static String VALID_WORKFLOW_DTO = "src/test/resources/WF_User_DTO.js";

    /**
     * The validate config.
     */
    private static String VALIDATE_CONFIG = "validateConfig";

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * The expected ex.
     */
    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();

    /**
     * The graph.
     */
    final Diagraph< String > graph = new Diagraph<>();

    /**
     * The threads.
     */
    Map< String, Thread > threads = new HashMap<>();

    /**
     * The wf execution manager.
     */
    WorkflowExecutionManagerImpl wfExecutionManager = new WorkflowExecutionManagerImpl();

    /**
     * Creates the mock job log.
     *
     * @throws SusException
     *         the sus exception
     */
    public void createMockJobLog() throws SusException {

        final RequestHeaders headers = new RequestHeaders( DUMMY_TOKEN, DUMMY_USER_AGENT );
        final RestAPI api = new RestAPI( "testProtocol", "testHost", "testPort" );
        jobImpl = new JobImpl();
        jobImpl.setServer( api );
        jobImpl.setRequestHeaders( headers );
        jobImpl.setId( UUID.randomUUID() );

        // mockControl().replay();
        JobLog.setJob( jobImpl );

    }

    /**
     * this method is used to create graph from work flow.
     *
     * @return Diagraph
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private Diagraph< String > getGraph() throws JsonSerializationException {

        List< NodeEdge > connections = null;
        try {
            connections = getUserWorkflow().getEdges();
        } catch ( FileNotFoundException | UserWFParseException e ) {
            log.fatal( e.getMessage() );
            e.printStackTrace();
        }
        for ( final NodeEdge elementConnection : connections ) {
            graph.add( elementConnection.getData().getSource(), elementConnection.getData().getTarget() );
        }
        return graph;
    }

    /**
     * This method is used to get user workflow model object from json file.
     *
     * @return String
     *
     * @throws FileNotFoundException
     *         the file not found exception
     */
    private UserWorkFlow getUserWorkflow() throws FileNotFoundException {
        final File initialFile = new File( VALID_WORKFLOW );
        final InputStream targetStream = new FileInputStream( initialFile );
        final LatestWorkFlowDTO latestWorkFlowDTO = JsonUtils.jsonStreamToObject( targetStream, LatestWorkFlowDTO.class );
        return prepareWorkflowModel( latestWorkFlowDTO );

    }

    /**
     * This method is used to workflowDefinitionDTO object from workflowDTO.
     *
     * @return WorkflowDefinitionDTO
     *
     * @throws FileNotFoundException
     *         the file not found exception
     * @throws UserWFParseException
     *         the user WF parse exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private WorkflowDefinitionDTO getWorkFlowDefinition() throws FileNotFoundException, UserWFParseException, JsonSerializationException {
        final Map< String, Object > map = getWorkflowDTO( VALID_WORKFLOW_DTO ).prepareDefinition();
        if ( map != null ) {

            JsonUtils.toJson( map );
        }

        final File initialFile = new File( VALID_WORKFLOW_DTO );
        final InputStream targetStream = new FileInputStream( initialFile );

        final WorkflowDefinitionDTO definitionObject = JsonUtils.jsonStreamToObject( targetStream, WorkflowDefinitionDTO.class );
        return definitionObject;
    }

    /**
     * This method is used to workflowDTO object from Json.
     *
     * @param userWorkFlowFilePath
     *         the user work flow file path
     *
     * @return WorkflowDTO
     *
     * @throws FileNotFoundException
     *         the file not found exception
     * @throws UserWFParseException
     *         the user WF parse exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private LatestWorkFlowDTO getWorkflowDTO( String userWorkFlowFilePath )
            throws FileNotFoundException, UserWFParseException, JsonSerializationException {
        LatestWorkFlowDTO workflow = new LatestWorkFlowDTO();
        final File initialFile = new File( userWorkFlowFilePath );
        final InputStream targetStream = new FileInputStream( initialFile );
        workflow = JsonUtils.jsonStreamToObject( targetStream, LatestWorkFlowDTO.class );
        return workflow;

    }

    /**
     * This method initialize mockControl.
     */
    @Before
    public void init() {
        mockControl.resetToNice();
    }

    /**
     * This method is used to set values in a workflowDTO object .
     *
     * @return WorkflowDTO
     *
     * @throws FileNotFoundException
     *         the file not found exception
     * @throws UserWFParseException
     *         the user WF parse exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private LatestWorkFlowDTO prepareWorkflowDto() throws FileNotFoundException, UserWFParseException, JsonSerializationException {

        return getWorkflowDTO( VALID_WORKFLOW_DTO );

    }

    /**
     * It prepare userWorkflow Sets workflowDefinition , connections and elements in it. from config appended with url provided .
     *
     * @param dto
     *         the dto
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
     * gets workflow elements from WorkflowDefinitionDTO.
     *
     * @param def
     *         the def
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
     * @param dto
     *         the dto
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
     * Should return notification if invali configuration.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnNotificationIfInvaliConfiguration() throws Exception {
        init();
        expectedEx.expect( SusException.class );
        // expectedEx.expectMessage( CONF );
        final Notification notif = new Notification();
        notif.addError( new Error( "some error" ) );
        config = mockControl.createMock( WorkflowConfigurationImpl.class );
        EasyMock.expect( config.validate() ).andReturn( notif );
        mockControl.replay();
        final WorkflowExecutionManagerImpl mock = PowerMock.createPartialMock( WorkflowExecutionManagerImpl.class, VALIDATE_CONFIG );
        final Notification notification = WhiteboxImpl.invokeMethod( mock, VALIDATE_CONFIG, config );
        assertTrue( notification.getErrors().get( 0 ).equals( notif.getErrors().get( 0 ) ) );

        for ( final Error error : notif.getErrors() ) {
            assertNotNull( error.getMessage() );
        }

    }

    /**
     * Should return valid workflow Definition object when it has valid workflow dto.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnWorkflowDefinitionWhenItHasValidWorkflowDto() throws Exception {

        final LatestWorkFlowDTO workflowDto = prepareWorkflowDto();

        final WorkflowExecutionManagerImpl mock = PowerMock.createPartialMock( WorkflowExecutionManagerImpl.class, SET_WOKFLOW_DEFINITION );
        final WorkflowDefinitionDTO def = WhiteboxImpl.invokeMethod( mock, SET_WOKFLOW_DEFINITION, workflowDto.prepareDefinition() );
        String json = null;
        final Map< String, Object > map = workflowDto.prepareDefinition();
        if ( map != null ) {

            json = JsonUtils.toJson( map );
        }
        final WorkflowDefinitionDTO definitionObject = JsonUtils.jsonToObject( json, WorkflowDefinitionDTO.class );
        assertTrue( !def.getElements().getEdges().isEmpty() && definitionObject.getElements().getEdges().get( 0 ).getData().getSource()
                .toString().equals( def.getElements().getEdges().get( 0 ).getData().getSource() ) );
    }

    /**
     * should throw exception when null API is given.
     *
     * @throws Exception
     *         the exception
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenNUllApiIsGiven() throws Exception {
        init();
        expectedEx.expect( SusException.class );

        final RestAPI api = null;
        mockControl.replay();

        final WorkflowExecutionManagerImpl mock = PowerMock.createPartialMock( WorkflowExecutionManagerImpl.class, PREPARE_URL );
        final String result = WhiteboxImpl.invokeMethod( mock, PREPARE_URL, "testUrl", api );
        assertNull( result );
    }

    /**
     * should prepare reuestHeaders and return it.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void testPrepareHeaders() throws Exception {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( "Content-Type", "application/json" );
        requestHeaders.put( "Accept", "application/json" );
        requestHeaders.put( "User-Agent", DUMMY_USER_AGENT );
        requestHeaders.put( "X-Auth-Token", DUMMY_TOKEN );

        final RequestHeaders headers = new RequestHeaders( DUMMY_TOKEN, DUMMY_USER_AGENT );
        final WorkflowExecutionManagerImpl mock = PowerMock.createPartialMock( WorkflowExecutionManagerImpl.class, PREPARE_HEADERS );
        final Map< String, String > result = WhiteboxImpl.invokeMethod( mock, PREPARE_HEADERS, headers );
        assertEquals( result, requestHeaders );

    }

    /**
     * should return valid Diagraph when walid workflow is given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenGetGraphHasValidUserWorkFlowItWillReturnDiagraph() throws Exception {
        createMockJobLog();
        final WorkflowExecutionManagerImpl mock = PowerMock.createPartialMock( WorkflowExecutionManagerImpl.class, GET_GRAPH );
        final Diagraph< String > graph = WhiteboxImpl.invokeMethod( mock, GET_GRAPH, getUserWorkflow(), jobImpl );
        assertEquals( graph.toString(), getGraph().toString() );
    }

    /**
     * Should return Url when it has a valid API object.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenItHasValidApiAndUrlWillReturnUrl() throws Exception {
        final RestAPI api = new RestAPI( "testProtocol", "testHost", "testPort" );
        final String testUrl = "testUrl";
        final String url = api.getProtocol() + api.getHostname() + ":" + api.getPort() + testUrl;
        final WorkflowExecutionManagerImpl mock = PowerMock.createPartialMock( WorkflowExecutionManagerImpl.class, PREPARE_URL );
        final String result = WhiteboxImpl.invokeMethod( mock, PREPARE_URL, "testUrl", api );
        assertEquals( result, url );

    }

    /**
     * When set workflow has valid workflow definition itwill set elements.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenSetWorkflowHasValidWorkflowDefinitionItwillSetElements() throws Exception {
        prepareWorkflowDto();
        final WorkflowDefinitionDTO definitionObject = getWorkFlowDefinition();
        final WorkflowExecutionManagerImpl mock = PowerMock.createPartialMock( WorkflowExecutionManagerImpl.class, SET_WORKFLOW_ELEMENTS );

        final List< UserWFElement > elements = WhiteboxImpl.invokeMethod( mock, SET_WORKFLOW_ELEMENTS, definitionObject );

        assertTrue( !elements.isEmpty() );
        assertTrue( elements.size() == definitionObject.getElements().getNodes().size() );

    }

    /**
     * should set owner of workflow when it has valid workflowDto.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenSetWorkFlowOwnerHasValidDTOItwillSetItsOwner() throws Exception {
        final LatestWorkFlowDTO workflowDto = prepareWorkflowDto();
        final WorkflowExecutionManagerImpl mock = PowerMock.createPartialMock( WorkflowExecutionManagerImpl.class, SET_WORKFLOW_OWNER );
        final UserWorkFlow worlflow = WhiteboxImpl.invokeMethod( mock, SET_WORKFLOW_OWNER, workflowDto );
        assertTrue( worlflow.getId().equals( workflowDto.getId().toString() ) && worlflow.getName().equals( workflowDto.getName() ) );

    }

    /**
     * Should return No Error if workflow and configuration is valid.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenValidateArgumentsHasValidInputWillReturnEmptyNotification() throws Exception {
        init();
        final Notification notif = new Notification();
        config = mockControl.createMock( WorkflowConfigurationImpl.class );
        EasyMock.expect( config.validate() ).andReturn( notif );
        mockControl.replay();
        final WorkflowExecutionManagerImpl mock = PowerMock.createPartialMock( WorkflowExecutionManagerImpl.class, VALIDATE_CONFIG );
        final Notification notification = WhiteboxImpl.invokeMethod( mock, VALIDATE_CONFIG, config );
        assertTrue( !notification.hasErrors() );

    }

}
