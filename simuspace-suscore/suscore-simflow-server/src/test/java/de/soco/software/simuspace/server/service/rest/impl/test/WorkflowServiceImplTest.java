package de.soco.software.simuspace.server.service.rest.impl.test;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.fasterxml.jackson.databind.JsonNode;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.server.service.rest.impl.WorkflowServiceImpl;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.suscore.permissions.model.PermissionDTO;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;

/**
 * This class is to test the workflow service implementation class methods.
 *
 * @author Kiran.Azeem
 */
@PrepareForTest( WorkflowServiceImpl.class )
public class WorkflowServiceImplTest {

    /**
     * The Constant FILTER_TEXT.
     */
    private static final String FILTER_TEXT = "text";

    /**
     * The Constant FIELD_NAME.
     */
    private static final String FIELD_NAME = "name";

    /**
     * The Constant AGENT.
     */
    private static final String AGENT = "client";

    /**
     * The Constant DUMMY_INT_ZERO.
     */
    private static final int DUMMY_INT_ZERO = 0;

    /**
     * The Constant DUMMY_NAME.
     */
    private static final String DUMMY_NAME = "New dummy Name";

    /**
     * The Constant EMPTY_STRING.
     */
    private static final String EMPTY_STRING = "";

    /**
     * The Constant JOB_JSON.
     */
    private static final String JOB_JSON = "{\"workflowId\":\"2217a520-9f6f-11e6-af68-b36bcc6dfb14\",\"workflowVersion\":{\"id\":\"\"},\"name\":\"DUMMY_NAME\",\"workingDir\":{\"agent\":\"client\",\"type\":\"dir\",\"path\":\"/home/sces111/VM_Share/SUSWFGIT/User_WF\",\"docId\":null,\"location\":0},\"comments\":\"\",\"description\":\"\",\"createdBy\":{\"id\":\"2217a520-9f6f-11e6-af68-b36bcc6dfb14\"}}\"";

    /**
     * The Constant mockControl.
     */
    private static final IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant PATH.
     */
    private static final String PATH = "/home/sces111/VM_Share/SUSWFGIT/User_WF";

    /**
     * The Constant TYPE.
     */
    private static final String TYPE = "dir";

    /**
     * The Constant WORKFLOW_ID_STRING.
     */
    private static final String WORKFLOW_ID_STRING = "2217a520-9f6f-11e6-af68-b36bcc6dfb14";

    /**
     * The Constant WORKFLOW_JSON.
     */
    private static final String WORKFLOW_JSON = "{\"name\":\"DUMMY_NAME\",\"comments\":\"\",\"description\":\"\",\"createdBy\":{\"id\":\"2217a520-9f6f-11e6-af68-b36bcc6dfb14\"}}\"";

    /**
     * Dummy Filter Json as Input Parameter.
     */
    private static final String FILTER_JSON = "{\"draw\":2,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"id\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"createdOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"updatedOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3}]}";

    /**
     * Dummy Invalid Filter Json as Input Parameter.
     */
    private static final String INVALID_FILTER_JSON = "{\"22draw\":2,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"id\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"createdOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"updatedOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3}]}";

    /**
     * The Constant DEFAULT_BE_USER_ID.
     */
    private static final UUID DEFAULT_BE_USER_ID = UUID.randomUUID();

    /**
     * The Constant versionId.
     */
    private static final int versionId = 123;

    /**
     * Dummy Index for List get.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * Dummy INVALID_PARAM_ERROR_MESSAGEs message.
     */
    private static final String INVALID_PARAM_ERROR_MESSAGE = "Invalid parameters provided.";

    /**
     * Dummy Error message TYpe.
     */
    private static final String MESSAGE_TYPE_ERROR = "ERROR";

    /**
     * The Constant SID_ID.
     */
    private static final UUID SID_ID = UUID.randomUUID();

    /**
     * The Constant DISBALE.
     */
    private static final int DISBALE = 0;

    /**
     * The Constant OBJECT_VIEW_MASK.
     */
    private static final int WORKFLOW_VIEW_MASK = 2;

    /**
     * The Constant TAB_NAME_PERMISSIONS.
     */
    private static final String TAB_NAME_PERMISSIONS = "Permissions";

    /**
     * The Constant TAB_NAME_VERSIONS.
     */
    private static final String TAB_NAME_VERSIONS = "Versions";

    /**
     * The Constant TAB_NAME_DESIGNER.
     */
    private static final String TAB_NAME_DESIGNER = "Designer";

    /**
     * The Constant SYNC_CONTEXT_URL.
     */
    private static final String SYNC_CONTEXT_URL = "Sync Url";

    /**
     * The Constant SYNC_CONTEXT_ICON.
     */
    private static final String SYNC_CONTEXT_ICON = "SYNC-icon";

    /**
     * The Constant SYNC_CONTEXT_TITLE.
     */
    private static final String SYNC_CONTEXT_TITLE = "Sync Files";

    /**
     * Dummy project Id.
     */
    private static final UUID PROJECT_ID = UUID.randomUUID();

    // Constants End--------

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Its the workflow job data transfer object.
     */
    private JobImpl jobImpl;

    /**
     * Its workflow job manager interface object.
     */
    private JobManager jobManager;

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Its the workflow data transfer object.
     */
    private WorkflowDTO workflowDTO;

    /**
     * The version.
     */
    private VersionDTO version;

    /**
     * Its workflow manager interface object.
     */
    private WorkflowManager workflowManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * Its workflow service implementation class object.
     */
    private WorkflowServiceImpl workflowServiceImpl;

    /**
     * The Constant WORKFLOW_ID.
     */
    private static final UUID WORKFLOW_ID = UUID.randomUUID();

    /**
     * The Constant WORKFLOW_NAME.
     */
    private static final String WORKFLOW_NAME = "workflow";

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        jobImpl = new JobImpl();
        workflowDTO = new WorkflowDTO();
        workflowServiceImpl = new WorkflowServiceImpl();
        jobManager = mockControl.createMock( JobManager.class );
        workflowManager = mockControl.createMock( WorkflowManager.class );
        objectViewManager = mockControl.createMock( ObjectViewManager.class );
        workflowServiceImpl.setWorkflowManager( workflowManager );
        workflowServiceImpl.setObjectViewManager( objectViewManager );
    }

    /**
     * To clear the objects and mocking objects.
     */
    @After
    public void tearDown() {
        jobImpl = null;
        workflowDTO = null;
        workflowServiceImpl = null;
        jobManager = null;
        workflowManager = null;
    }

    /**
     * Sets the version detail.
     */
    private void setVersionDetail() {
        version = new VersionDTO();
        version.setId( DUMMY_INT_ZERO );
    }

    /**
     * When in get job method job id is not null it should return job in response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInGetJobMethodJobIdIsNotNullItShouldReturnJobInResponse() throws SusException {

        final UUID jobId = UUID.randomUUID();

        // To Mock Job Manager
        jobImpl.setName( DUMMY_NAME );
        jobImpl.setId( jobId );

        EasyMock.expect( jobManager.getJob( DEFAULT_BE_USER_ID, jobId.toString() ) ).andReturn( jobImpl );
        mockControl.replay();
        workflowServiceImpl.setJobManager( jobManager );

        final Response response = workflowServiceImpl.getJob( jobId.toString() );

        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Test when in get job method job id is null it should return failure response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInSaveJobMethodJobJsonIsEmptyItShouldSendFailureResponse() throws SusException {
        final String jobJson = EMPTY_STRING;
        final Response response = workflowServiceImpl.createJob( jobJson );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * Test when get workflow list method is called it should return workflows in response.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenInGetWorkflowListMethodIsCalledItShouldReturnAWorkflowsInResponse() throws Exception {

        // To Mock Workflow Manager
        workflowDTO.setName( DUMMY_NAME );
        workflowDTO.setId( WORKFLOW_ID_STRING );

        final List< WorkflowDTO > workflowDTOs = new ArrayList<>();
        workflowDTOs.add( workflowDTO );

        EasyMock.expect( workflowManager.getWorkflowList( DEFAULT_BE_USER_ID ) ).andReturn( workflowDTOs );
        mockControl.replay();
        workflowServiceImpl.setWorkflowManager( workflowManager );

        final String workflowJson = WORKFLOW_JSON;
        final Response response = workflowServiceImpl.createWorkflow( EMPTY_STRING, workflowJson );

        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * When in save workflow method workflow json is empty it should send failure response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInSaveWorkflowMethodWorkflowJsonIsEmptyItShouldSendFailureResponse() throws SusException {
        final String workflowJson = EMPTY_STRING;
        final Response response = workflowServiceImpl.createWorkflow( EMPTY_STRING, workflowJson );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * Test when in get workflow method workflow id is passed it should return a workflow in response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInGetWorkflowMethodWorkflowIdIsNotNullItShouldReturnAWorkflowInResponse() throws SusException {

        // To Mock Workflow Manager
        workflowDTO.setName( DUMMY_NAME );
        workflowDTO.setId( WORKFLOW_ID_STRING );

        mockControl.replay();
        workflowServiceImpl.setWorkflowManager( workflowManager );

        final Response response = workflowServiceImpl.getWorkflowById( WORKFLOW_ID_STRING );

        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Test when in get workflow method workflow id is null it should return failure response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInGetJobMethodJobIdIsNullItShouldSendFailureResponse() throws SusException {
        final UUID jobId = UUID.randomUUID();
        final Response response = workflowServiceImpl.getJob( jobId.toString() );
        Assert.assertEquals( HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatus() );
    }

    /**
     * Test when load config method is called it should return a config in response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInLoadConfigMethodIsCalledItShouldReturnAConfigInResponse() throws SusException {

        // To Mock Workflow Manager
        final JsonNode config = null;
        EasyMock.expect( workflowManager.getConfig() ).andReturn( config );
        mockControl.replay();
        workflowServiceImpl.setWorkflowManager( workflowManager );

        final Response response = workflowServiceImpl.loadConfig();

        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Test when in save job method job json is empty it should return failure response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInGetWorkflowMethodWorkflowIdIsNullItShouldSendFailureResponse() throws SusException {
        final Response response = workflowServiceImpl.getWorkflowById( WORKFLOW_ID_STRING );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Test when in save job method job json is not empty it should return newly created job in response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInSaveJobMethodJobJsonIsNotEmptyItShouldReturnNewlySavedJobInResponse() throws SusException {

        // final UUID workflowId = UUID.fromString( WORKFLOW_ID_STRING );

        jobImpl.setName( DUMMY_NAME );
        jobImpl.setWorkflowId( UUID.fromString( WORKFLOW_ID_STRING ) );
        jobImpl.setWorkflowVersion( new VersionDTO( DUMMY_INT_ZERO ) );
        jobImpl.setDescription( EMPTY_STRING );
        final EngineFile workingDir = new EngineFile( AGENT, TYPE, PATH, DUMMY_INT_ZERO, DUMMY_INT_ZERO );
        jobImpl.setWorkingDir( workingDir );

        // To Mock job Manager
        jobImpl.setCreatedBy( new UserDTO( DEFAULT_BE_USER_ID.toString() ) );

        EasyMock.expect( jobManager.createJob( DEFAULT_BE_USER_ID, jobImpl ) ).andReturn( jobImpl ).anyTimes();
        mockControl.replay();
        workflowServiceImpl.setJobManager( jobManager );

        final String jobJson = JOB_JSON;
        final Response response = workflowServiceImpl.createJob( jobJson );

        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Test when in save workflow method workflow json is not empty it should return newly saved workflow in response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInSaveWorkflowMethodWorkflowJsonIsNotEmptyItShouldReturnNewlySavedWorkflowInResponse() throws SusException {

        workflowDTO.setName( DUMMY_NAME );

        // To Mock workflow Manager
        workflowDTO.setCreatedBy( new UserDTO( DEFAULT_BE_USER_ID.toString() ) );
        workflowDTO.setDescription( EMPTY_STRING );
        setVersionDetail();
        workflowDTO.setVersion( version );

        EasyMock.expect( workflowManager.saveWorkflow( EasyMock.anyObject( EntityManager.class ), DEFAULT_BE_USER_ID, EMPTY_STRING,
                workflowDTO, EMPTY_STRING ) ).andReturn( workflowDTO ).anyTimes();
        mockControl.replay();

        final String workflowJson = WORKFLOW_JSON;
        final Response response = workflowServiceImpl.createWorkflow( EMPTY_STRING, workflowJson );

        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Test when in update job method job json is empty it should return updated job in response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInUpdateJobMethodJobJsonIsNotEmptyItShouldReturnUpdatedJobInResponse() throws SusException {

        final UUID workflowId = UUID.fromString( WORKFLOW_ID_STRING );
        final UUID jobId = UUID.randomUUID();

        jobImpl.setName( DUMMY_NAME );
        jobImpl.setId( jobId );
        jobImpl.setWorkflowId( workflowId );
        jobImpl.setWorkflowVersion( new VersionDTO( DUMMY_INT_ZERO ) );
        jobImpl.setDescription( EMPTY_STRING );
        final EngineFile workingDir = new EngineFile( AGENT, TYPE, PATH, DUMMY_INT_ZERO, DUMMY_INT_ZERO );
        jobImpl.setWorkingDir( workingDir );

        // To Mock job Manager
        jobImpl.setCreatedBy( new UserDTO( DEFAULT_BE_USER_ID.toString() ) );

        EasyMock.expect( jobManager.updateJob( jobImpl ) ).andReturn( jobImpl ).anyTimes();
        mockControl.replay();
        workflowServiceImpl.setJobManager( jobManager );

        final String jobJson = JOB_JSON;
        final Response response = workflowServiceImpl.updateJob( jobId, jobJson );

        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Test when in update job method job json is empty it should return failure response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInUpdateJobMethodJobJsonIsEmptyItShouldSendFailureResponse() throws SusException {

        final UUID jobId = UUID.randomUUID();
        final String jobJson = EMPTY_STRING;
        final Response response = workflowServiceImpl.updateJob( jobId, jobJson );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * Should send failure response when update workflow With null workflow json.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldSendFailureResponseWhenUpdateWorkflowWIthNullWorkflowJson() throws SusException {

        final Response response = workflowServiceImpl.updateWorkflow( WORKFLOW_ID_STRING, null );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * Should send failure response when update workflow With null workflow id.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldSendFailureResponseWhenUpdateWorkflowWIthNullWorkflowId() throws SusException {

        final Response response = workflowServiceImpl.updateWorkflow( null, WORKFLOW_JSON );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Should return workflow DTo when workflow Id is given send success response.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnWorkflowDTOwhenWorkflowIdisGivenSendSuccessResponse() throws Exception {
        // To Mock Workflow Manager
        workflowDTO.setName( DUMMY_NAME );
        workflowDTO.setId( WORKFLOW_ID_STRING );
        final List< LatestWorkFlowDTO > list = new ArrayList<>();
        list.add( prepareNewWorkflowFromOldDTO( workflowDTO ) );

        EasyMock.expect( workflowManager.getWorkflowVersionsById( DEFAULT_BE_USER_ID, WORKFLOW_ID_STRING ) ).andReturn( list );

        mockControl().replay();
        workflowServiceImpl.setWorkflowManager( workflowManager );
        final Response response = workflowServiceImpl.getWorkflowVersionsById( WORKFLOW_ID_STRING );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Should return workflow DTO when invalid workflow id is given send failure response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnErrorWhenInvalidWorkflowIdIsGivenSendFailureResponse() throws SusException {

        final Response response = workflowServiceImpl.getWorkflowVersionsById( null );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Should return workflow by id and version id succes response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnWorkflowByIdAndVersionIdSuccesResponse() throws SusException {
        // To Mock Workflow Manager
        workflowDTO.setName( DUMMY_NAME );
        workflowDTO.setId( WORKFLOW_ID_STRING );
        EasyMock.expect( workflowManager.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ), DEFAULT_BE_USER_ID,
                WORKFLOW_ID_STRING, versionId ) ).andReturn( workflowDTO ).anyTimes();

        mockControl.replay();

        final Response response = workflowServiceImpl.getWorkflowByIdAndVersionId( WORKFLOW_ID_STRING, versionId );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Should passed if get jobs list return success response.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldPassedIfGetJobsListReturnSuccessResponse() throws SusException {
        final List< Job > returnlist = new ArrayList<>();
        returnlist.add( jobImpl );
        EasyMock.expect( jobManager.getJobsList( DEFAULT_BE_USER_ID ) ).andReturn( returnlist );
        mockControl.replay();
        workflowServiceImpl.setJobManager( jobManager );
        final Response response = workflowServiceImpl.getJobsList();
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );

    }

    /**
     * Should return filtered workflow list.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnFilteredWorkflowList() throws SusException {

        final List< LatestWorkFlowDTO > expectedList = new ArrayList<>();
        expectedList.add( fillWorkflowDto() );

        final FilteredResponse< LatestWorkFlowDTO > expected = getFilledFilteredResponse( expectedList );
        EasyMock.expect(
                        workflowManager.getFilteredWorkflowList( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ) ) )
                .andReturn( expected );
        mockControl.replay();
        workflowServiceImpl.setWorkflowManager( workflowManager );

        final Response response = workflowServiceImpl.getFilteredWorkflowList( FILTER_JSON );

        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
    }

    /**
     * Should return filtered workflow UI views.
     */
    @Test
    public void shouldReturnFilteredWorkflowUIViews() {
        final List< TableColumn > expected = GUIUtils.listColumns( LatestWorkFlowDTO.class );
        EasyMock.expect( workflowManager.getListOfWorkFlowDTOUITableColumns() ).andReturn( expected ).anyTimes();
        mockControl().replay();
        final Response actual = workflowServiceImpl.getFilteredWorkflowUI();
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        final String actualJsonString = JsonUtils.toJsonString( actualResponse.getData() );
        final TableUI actualtable = JsonUtils.jsonToObject( actualJsonString, TableUI.class );
        for ( final TableColumn actualTableColumn : actualtable.getColumns() ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }

        }

    }

    /**
     * Prepare new workflow from old DTO.
     *
     * @param entity
     *         the entity
     *
     * @return the new workflow DTO
     */
    private LatestWorkFlowDTO prepareNewWorkflowFromOldDTO( WorkflowDTO entity ) {

        final LatestWorkFlowDTO newWorkflowDTO = new LatestWorkFlowDTO();

        newWorkflowDTO.setId( UUID.fromString( entity.getId() ) );
        newWorkflowDTO.setName( entity.getName() );
        newWorkflowDTO.setInteractive( false );
        newWorkflowDTO.setVersion( entity.getVersion() );
        newWorkflowDTO.setDescription( entity.getDescription() );
        // get StatusName from config file
        newWorkflowDTO.setActions( entity.getActions() );
        newWorkflowDTO.setCreatedOn( entity.getCreatedOn() );
        newWorkflowDTO.getModifiedOn( entity.getModifiedOn() );
        newWorkflowDTO.setExecutable( entity.isExecutable() );

        if ( entity.getDefinition() != null ) {
            newWorkflowDTO.setWithDefinition( entity.getDefinition() );
        }
        newWorkflowDTO.setCreatedBy( entity.getCreatedBy() );
        newWorkflowDTO.setModifiedBy( entity.getUpdatedBy() );
        newWorkflowDTO.setJobs( entity.getJobs() );

        return newWorkflowDTO;
    }

    /**
     * ***************** getDataObjectVersionsUI *************************.
     */
    /**
     * Should successfully get version table view for valid workflow object.
     */
    @Test
    public void shouldSuccessfullyGetVersionTableViewForValidWorkflowObject() {
        final List< TableColumn > expected = GUIUtils.listColumns( LatestWorkFlowDTO.class );
        EasyMock.expect( workflowManager.getWorkflowVersionsUI( EasyMock.anyString() ) ).andReturn( expected ).anyTimes();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( new ArrayList<>() );
        mockControl().replay();
        final Response actual = workflowServiceImpl.getWorkflowVersionsUI( WORKFLOW_ID.toString() );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String actualJsonString = JsonUtils.toJsonString( actualResponse.getData() );
        final TableUI actualtable = JsonUtils.jsonToObject( actualJsonString, TableUI.class );

        for ( final TableColumn actualTableColumn : actualtable.getColumns() ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }

        }
    }

    /**
     * ***************** getFilteredDataObjectVersionsList ************************.
     */

    /**
     * Should success fully get filtered version list of workflows when valid filter json is given as input.
     */
    @Test
    public void shouldSuccessFullyGetFilteredVersionListOfWorkflowsWhenValidFilterJsonIsGivenAsInput() {
        final List< LatestWorkFlowDTO > expectedList = new ArrayList<>();
        expectedList.add( fillWorkflowDto() );

        final FilteredResponse< LatestWorkFlowDTO > expected = getFilledFilteredResponse( expectedList );

        EasyMock.expect( workflowManager.getWorkflowVersions( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expected ).anyTimes();

        mockControl().replay();
        final Response actual = workflowServiceImpl.getFilteredWorkflowVersionsList( WORKFLOW_ID.toString(), FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final FilteredResponse< LatestWorkFlowDTO > actualDto = JsonUtils.jsonToObject( dataObjDto, FilteredResponse.class );
        final List< LatestWorkFlowDTO > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( actualDto.getData() ),
                LatestWorkFlowDTO.class );
        Assert.assertNotNull( actualList );
        final LatestWorkFlowDTO expectedObject = expected.getData().get( FIRST_INDEX );
        Assert.assertEquals( expectedObject.getId(), actualList.get( FIRST_INDEX ).getId() );
        Assert.assertEquals( expectedObject.getName(), actualList.get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedObject.getCreatedOn(), actualList.get( FIRST_INDEX ).getCreatedOn() );

    }

    /**
     * Should get null as filtered version list of workflows when manager return null as version list.
     */
    @Test
    public void shouldGetNullAsFilteredVersionListOfWorkflowsWhenManagerReturnNullAsVersionList() {

        final FilteredResponse< LatestWorkFlowDTO > expected = null;

        EasyMock.expect( workflowManager.getWorkflowVersions( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expected ).anyTimes();

        mockControl().replay();
        final Response actual = workflowServiceImpl.getFilteredWorkflowVersionsList( WORKFLOW_ID.toString(), FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getData(), expected );

    }

    /**
     * Should Throw Json Exception When Invalid Json Is Given To Get Filtered Versions List.
     */
    @Test
    public void shouldThrowJsonExceptionWhenInvalidJsonIsGivenToGetFilteredVersionsList() {

        final Response actual = workflowServiceImpl.getFilteredWorkflowVersionsList( WORKFLOW_ID.toString(), INVALID_FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNull( actualResponse.getData() );
        Assert.assertEquals( Boolean.FALSE, actualResponse.getSuccess() );
        final Message messageDto = JsonUtils.jsonToObject( JsonUtils.toJson( actualResponse.getMessage() ), Message.class );
        Assert.assertEquals( INVALID_PARAM_ERROR_MESSAGE, messageDto.getContent() );
        Assert.assertEquals( MESSAGE_TYPE_ERROR, messageDto.getType() );

    }

    /**
     * **************** Permission ui and permission table view.****************
     */
    /**
     * Should return all permission related to workflow when exact manage object is called.
     */
    @Test
    public void shouldReturnAllPermissionRelatedToWorkflowWhenExactManageObjectIsCalled() {
        final ManageObjectDTO objectManageDTO = getObjectManageDTO();
        final List< PermissionDTO > permissionDTOs = new ArrayList<>();
        final PermissionDTO permissionDTO = getFillPermissionDTO();
        permissionDTOs.add( permissionDTO );
        objectManageDTO.setPermissionDTOs( permissionDTOs );
        final FiltersDTO filtersDTO = populateFilterDTO();
        final List< ManageObjectDTO > expectedObjectManageDTO = new ArrayList<>();
        expectedObjectManageDTO.add( objectManageDTO );
        final FilteredResponse< ManageObjectDTO > expectedFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO,
                expectedObjectManageDTO );
        EasyMock.expect( workflowManager.showPermittedUsersAndGroupsForObject( EasyMock.anyObject( FiltersDTO.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedFilteredResponse ).anyTimes();
        mockControl.replay();
        final Response response = workflowServiceImpl.showPermittedUsersAndGroupsForWorkflow( WORKFLOW_ID,
                JsonUtils.toJson( filtersDTO, FiltersDTO.class ) );
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.SC_OK, response.getStatus() );
        final SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        final String objectManageDTOStr = JsonUtils.toJson( ( LinkedHashMap< Object, Object > ) susResponseDTO.getData() );
        final FilteredResponse< ResourceAccessControlDTO > filteredResponse = JsonUtils.jsonToObject( objectManageDTOStr,
                FilteredResponse.class );
        final List< ManageObjectDTO > actual = JsonUtils.jsonToList( JsonUtils.toJson( filteredResponse.getData() ),
                ManageObjectDTO.class );
        Assert.assertNotNull( actual );
        Assert.assertEquals( expectedObjectManageDTO.get( FIRST_INDEX ).getName(), actual.get( FIRST_INDEX ).getName() );
    }

    /**
     * Should fail if permission pay load is null.
     */
    @Test
    public void shouldFailIfPermissionPayLoadIsNull() {
        final Response response = workflowServiceImpl.showPermittedUsersAndGroupsForWorkflow( WORKFLOW_ID, null );
        final SusResponseDTO expected = JsonUtils.jsonToObject( response.getEntity().toString(), SusResponseDTO.class );
        Assert.assertFalse( expected.getSuccess() );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
        Assert.assertEquals( expected.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
        Assert.assertNull( expected.getData() );
    }

    /**
     * Should successfully get table UI of object manager DTO when valid input provided.
     */
    @Test
    public void shouldSuccessfullyGetTableUIOfObjectManagerDTOWhenValidInputProvided() {
        final TableUI expected = getFilledColumns();
        EasyMock.expect( workflowManager.workflowPermissionTableUI() ).andReturn( expected.getColumns() ).anyTimes();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( new ArrayList<>() );
        mockControl().replay();
        final Response actual = workflowServiceImpl.workflowPermissionTableUI( WORKFLOW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );

        Assert.assertNotNull( actualResponse.getData() );

        final TableUI actualUIColumnList = JsonUtils.linkedMapObjectToClassObject( actualResponse.getData(), TableUI.class );
        Assert.assertEquals( expected.getColumns().size(), actualUIColumnList.getColumns().size() );
    }

    /**
     * Should successfully get workflow context when valid filter is provided.
     */
    @Test
    public void shouldSuccessfullyGetWorkflowContextWhenValidFilterIsProvided() {
        final List< ContextMenuItem > expectedList = new ArrayList<>();
        expectedList.add( new ContextMenuItem( SYNC_CONTEXT_URL, SYNC_CONTEXT_ICON, SYNC_CONTEXT_TITLE ) );
        EasyMock.expect( workflowManager.getWorkflowContextRouter( EasyMock.anyObject(), EasyMock.anyString(),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expectedList ).anyTimes();
        mockControl.replay();
        final Response actual = workflowServiceImpl.getWorkflowContext( PROJECT_ID.toString(), FILTER_JSON );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final List< ContextMenuItem > actualList = JsonUtils.jsonToList(
                JsonUtils.toJsonString( JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class ).getData() ),
                ContextMenuItem.class );
        Assert.assertEquals( expectedList, actualList );
    }

    /**
     * ***************************** Tabs Views Workflow UI ************************************************.
     */
    /**
     * Should success fully get tabs view for workflow when valid workflow id is given.
     */
    @Test
    public void shouldSuccessFullyGetTabsViewForWorkflowWhenValidWorkflowIdIsGiven() {

        final SubTabsItem expected = new SubTabsItem( "", "", 1, getGeneralTabsList(), null );

        EasyMock.expect( workflowManager.getTabsViewWorkflowUI( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expected ).anyTimes();
        mockControl().replay();
        final Response actual = workflowServiceImpl.getTabsViewWorkflowUI( WORKFLOW_ID.toString() );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String actualTabsList = JsonUtils.toJsonString( actualResponse.getData() );
        final SubTabsItem actualtabs = JsonUtils.jsonToObject( actualTabsList, SubTabsItem.class );
        Assert.assertEquals( expected.getTabs().get( FIRST_INDEX ).getName(), actualtabs.getTabs().get( FIRST_INDEX ).getName() );

    }

    /**
     * Should Not Get Tabs Views In Response When Manger Return Null For Tabs Views.
     */
    @Test
    public void shouldNotGetTabsViewsInResponseWhenMangerReturnNullForTabsViews() {
        final SubTabsItem expected = new SubTabsItem( WORKFLOW_ID.toString(), WORKFLOW_NAME, 1, getWorkflowTabsList(), null );
        EasyMock.expect( workflowManager.getTabsViewWorkflowUI( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expected ).anyTimes();
        mockControl().replay();
        final Response actual = workflowServiceImpl.getTabsViewWorkflowUI( WORKFLOW_ID.toString() );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        final SubTabsItem actualTabs = JsonUtils.jsonToObject( JsonUtils.objectToJson( actualResponse.getData() ), SubTabsItem.class );
        Assert.assertEquals( expected, actualTabs );

    }

    /**
     * ***************** getFilteredDataObjectVersionsList ************************.
     */

    /**
     * Should SuccessFully Get Filtered Version List Of DataObjects When Valid Filter Json Is Given As Input
     */
    @Test
    public void shouldSuccessFullyGetFilteredVersionListOfDataObjectsWhenValidFilterJsonIsGivenAsInput() {
        final List< WorkflowProjectDTO > expectedList = new ArrayList<>();
        expectedList.add( fillWorkfProjectDto() );

        final FilteredResponse< WorkflowProjectDTO > expected = getFilledProjectFilteredResponse( expectedList );

        EasyMock.expect( workflowManager.getObjectVersions( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expected ).anyTimes();

        mockControl().replay();
        final Response actual = workflowServiceImpl.getFilteredObjectVersionsList( PROJECT_ID.toString(), FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( actual.getStatus(), HttpStatus.SC_OK );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final FilteredResponse< DataObjectDTO > actualDto = JsonUtils.jsonToObject( dataObjDto, FilteredResponse.class );
        final List< DataObjectDTO > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( actualDto.getData() ), DataObjectDTO.class );
        Assert.assertNotNull( actualList );
        final WorkflowProjectDTO expectedObject = expected.getData().get( FIRST_INDEX );
        Assert.assertEquals( expectedObject.getId(), actualList.get( FIRST_INDEX ).getId() );
        Assert.assertEquals( expectedObject.getName(), actualList.get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedObject.getParentId(), actualList.get( FIRST_INDEX ).getParentId() );
        Assert.assertEquals( expectedObject.getCreatedOn(), actualList.get( FIRST_INDEX ).getCreatedOn() );

    }

    /**
     * Should Get Null As Filtered Version List Of DataObjects When Manager Return Null As Version List.
     */
    @Test
    public void shouldGetNullAsFilteredVersionListOfDataObjectsWhenManagerReturnNullAsVersionList() {

        final FilteredResponse< WorkflowProjectDTO > expected = null;

        EasyMock.expect( workflowManager.getObjectVersions( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expected ).anyTimes();

        mockControl().replay();
        final Response actual = workflowServiceImpl.getFilteredObjectVersionsList( PROJECT_ID.toString(), FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getData(), expected );

    }

    /**
     * ************************************ HELPER METHODS **************************************.
     *
     * /** Populates filter DTO.
     *
     * @return the filters DTO
     */
    private FiltersDTO populateFilterDTO() {
        final FiltersDTO filterDTO = new FiltersDTO();
        filterDTO.setDraw( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setLength( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setStart( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setFilteredRecords( 1L );
        return filterDTO;
    }

    /**
     * Get Filled Filtered Response.
     *
     * @param list
     *         the list
     *
     * @return FilteredResponse
     */
    private FilteredResponse< LatestWorkFlowDTO > getFilledFilteredResponse( List< LatestWorkFlowDTO > list ) {
        final FiltersDTO filter = new FiltersDTO();
        filter.setDraw( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setLength( ConstantsInteger.INTEGER_VALUE_TWO );
        filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setFilteredRecords( Long.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ) );

        final FilteredResponse< LatestWorkFlowDTO > expectedResponse = PaginationUtil.constructFilteredResponse( filter, list );
        return expectedResponse;
    }

    /**
     * A method to populate the DataObject Dto for Expected Result of test.
     *
     * @return projectDTO
     */
    private LatestWorkFlowDTO fillWorkflowDto() {

        final LatestWorkFlowDTO latestWorkFlowDTO = new LatestWorkFlowDTO();
        latestWorkFlowDTO.setId( WORKFLOW_ID );
        latestWorkFlowDTO.setName( WORKFLOW_NAME );
        latestWorkFlowDTO.setCreatedOn( new Date() );
        latestWorkFlowDTO.getModifiedOn( new Date() );

        return latestWorkFlowDTO;
    }

    /**
     * Gets the object manage DTO.
     *
     * @return the object manage DTO
     */
    private ManageObjectDTO getObjectManageDTO() {
        final ManageObjectDTO objectManageDTO = new ManageObjectDTO();
        objectManageDTO.setName( "Administrator" );
        objectManageDTO.setType( "Group" );
        objectManageDTO.setSidId( SID_ID );
        return objectManageDTO;
    }

    /**
     * Gets the fill permission DTO.
     *
     * @return the fill permission DTO
     */
    private PermissionDTO getFillPermissionDTO() {
        final PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setValue( DISBALE );
        permissionDTO.setManage( true );
        permissionDTO.setMatrixKey( WORKFLOW_VIEW_MASK );
        permissionDTO.setMatrexValue( WORKFLOW_ID_STRING );
        return permissionDTO;
    }

    /**
     * Gets the filled columns.
     *
     * @return the filled columns
     */
    private TableUI getFilledColumns() {
        final TableUI tableUI = new TableUI();
        final List< TableColumn > columns = new ArrayList<>();
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
        final TableColumn tableColumn = new TableColumn();
        tableColumn.setData( FIELD_NAME );
        tableColumn.setTitle( FIELD_NAME );
        tableColumn.setFilter( FILTER_TEXT );
        tableColumn.setSortable( true );
        tableColumn.setName( FIELD_NAME );
        tableColumn.setRenderer( getFilledRenderer() );
        return tableColumn;
    }

    /**
     * Gets the filled renderer.
     *
     * @return the filled renderer
     */
    private Renderer getFilledRenderer() {
        final Renderer renderer = new Renderer();
        renderer.setType( FILTER_TEXT );
        renderer.setSeparator( ConstantsString.COMMA );
        renderer.setLabelClass( ConstantsString.EMPTY_STRING );
        renderer.setData( FIELD_NAME );
        renderer.setManage( true );
        return renderer;
    }

    /**
     * Tabs View Dummy Name.
     *
     * @return List of STring
     */
    private List< SubTabsUI > getGeneralTabsList() {
        final List< SubTabsUI > subTbs = new ArrayList<>();
        subTbs.add( new SubTabsUI( SusConstantObject.PROPERTIES_TAB ) );

        return subTbs;
    }

    /**
     * Tabs View Dummy Name.
     *
     * @return List of STring
     */
    private List< SubTabsUI > getWorkflowTabsList() {
        final List< SubTabsUI > subTbs = new ArrayList<>();
        subTbs.add( new SubTabsUI( TAB_NAME_DESIGNER ) );
        subTbs.add( new SubTabsUI( TAB_NAME_VERSIONS ) );
        subTbs.add( new SubTabsUI( TAB_NAME_PERMISSIONS ) );

        return subTbs;
    }

    /**
     * Gets the filled project filtered response.
     *
     * @param expectedList
     *         the expected list
     *
     * @return the filled project filtered response
     */
    private FilteredResponse< WorkflowProjectDTO > getFilledProjectFilteredResponse( List< WorkflowProjectDTO > expectedList ) {
        final FiltersDTO filter = new FiltersDTO();
        filter.setDraw( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setLength( ConstantsInteger.INTEGER_VALUE_TWO );
        filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setFilteredRecords( Long.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ) );

        final FilteredResponse< WorkflowProjectDTO > expectedResponse = PaginationUtil.constructFilteredResponse( filter, expectedList );
        return expectedResponse;
    }

    /**
     * Fill workf project dto.
     *
     * @return the workflow project DTO
     */
    private WorkflowProjectDTO fillWorkfProjectDto() {
        final WorkflowProjectDTO projectDTO = new WorkflowProjectDTO();
        projectDTO.setName( DUMMY_NAME );
        projectDTO.setId( PROJECT_ID );
        return projectDTO;
    }

}
