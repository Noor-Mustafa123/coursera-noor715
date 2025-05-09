package de.soco.software.simuspace.suscore.local.daemon.controller.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.expectation.PowerMockitoStubber;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.local.daemon.manager.SuscoreDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.manager.WorkflowDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.manager.impl.WorkflowDaemonManagerImpl;
import de.soco.software.simuspace.suscore.local.daemon.model.ItemsDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;

/**
 * Test Cases for Class JobControllerImpl.
 *
 * @author Nosheen.Sharif
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { SuSClient.class } )
public class JobControllerImplTest {

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant DUMMY_JOB_NAME.
     */
    private static final String DUMMY_JOB_NAME = "New Job 1";

    /**
     * The Constant DUMMY_STRING_ABC.
     */
    private static final String DUMMY_STRING_ABC = "abc";

    /**
     * The Constant DUMMY_STRING_DEF.
     */
    private static final String DUMMY_STRING_DEF = "def";

    /**
     * The Constant JOB_ID.
     */
    private static final UUID JOB_ID = UUID.randomUUID();

    /**
     * The machine.
     */
    private static final String MACHINE = "clockg.soco.pk";

    /**
     * The Constant STATUS_ID.
     */
    private static final int STATUS_ID = 1;

    /**
     * The Constant STATUS_RUNNING.
     */
    private static final String STATUS_RUNNING = "running";

    /**
     * The Constant invalidJobId.
     */
    private static final String INVALID_JOB_ID = "abcdef";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant OPEN_LOG_TITLE.
     */
    private static final String OPEN_LOG_TITLE = "Open Log";

    /**
     * Dummy Filter Json as Input Parameter.
     */
    private static final String FILTER_JSON = "{\"draw\":1,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"description\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"machine\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"status\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3},{\"name\":\"workflowName\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":4},{\"name\":\"createdBy\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":5},{\"name\":\"submitTime\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":6},{\"name\":\"comments\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":7},{\"name\":\"id\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":8}]}";

    /**
     * The application manager.
     */
    private WorkflowDaemonManager daemonManager;

    /**
     * The suscore manager.
     */
    private SuscoreDaemonManager suscoreManager;

    /**
     * The controller.
     */
    private JobControllerImpl controller = new JobControllerImpl();

    /**
     * The Constant ERROR_MESSAGE.
     */
    private static final String ERROR_MESSAGE = "ERROR";

    /**
     * The Constant SUCCESS_MESSAGE.
     */
    private static final String SUCCESS_MESSAGE = "SUCCESS";

    /**
     * The Constant AUTH_TOKEN.
     */
    private static final String AUTH_TOKEN = "3edbe8f84777ae1ca6b4bccf515b78ecbd33cf32";

    /**
     * The Constant DUMMY_SERVER_API.
     */
    private static final String DUMMY_SERVER_API = "/api/job";

    /**
     * The Constant VIEW_JSON.
     */
    private static final String VIEW_JSON = "{\"name\":\"qwqw\",\"settings\":{\"draw\":1,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"description\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"machine\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"status\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3},{\"name\":\"workflowName\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":4},{\"name\":\"createdBy\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":5},{\"name\":\"submitTime\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":6},{\"name\":\"comments\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":7},{\"name\":\"id\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":8},{\"name\":\"runMode\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":9}]}}";

    /**
     * The job.
     */
    private Job job;

    /**
     * The Constant VIEW_ID.
     */
    private static final String VIEW_ID = UUID.randomUUID().toString();

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        PowerMockito.spy( SuSClient.class );
        daemonManager = mockControl.createMock( WorkflowDaemonManagerImpl.class );
        suscoreManager = mockControl.createMock( SuscoreDaemonManager.class );
        controller.setDaemonManager( daemonManager );
        controller.setSuscoreManager( suscoreManager );
        job = new JobImpl();

    }

    /* *************************************************************************
     * <getFileBaseJobs under test>
     * *************************************************************************
     */

    /**
     * Should get list of jobs in response data when file job list is not empty.
     */

    @Test
    public void shouldGetListOfJobsInResponseDataWhenFileJobListIsNotEmpty() {
        List< Job > jobs = fillJobList();
        EasyMock.expect( daemonManager.getFileBaseJobs() ).andReturn( jobs );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.getFileJobsList( FILTER_JSON );
        Assert.assertNotNull( response );
        FilteredResponse< Job > data = ( FilteredResponse< Job > ) response.getBody().getData();
        List< Job > actual = ( List< Job > ) data.getData();
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertNotNull( response.getBody().getData() );
        Assert.assertEquals( actual.size(), jobs.size() );
        for ( int i = 0; i < jobs.size(); i++ ) {
            Assert.assertEquals( jobs.get( i ).toString(), actual.get( i ).toString() );
        }

    }

    /**
     * Should get empty response data when file job list is empty.
     */

    @Test
    public void shouldGetEmptyResponseDataWhenFileJobListIsEmpty() {
        List< Job > expectedList = null;
        EasyMock.expect( daemonManager.getFileBaseJobs() ).andReturn( expectedList );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.getFileJobsList( FILTER_JSON );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        FilteredResponse< ? > data = ( FilteredResponse ) response.getBody().getData();
        Assert.assertEquals( ( List< Job > ) data, expectedList );

    }

    /**
     * Should throw exception in get list of jobs in response data when manager throw exception.
     */
    @Test
    public void shouldThrowExceptionInGetListOfJobsInResponseDataWhenManagerThrowException() {
        EasyMock.expect( daemonManager.getFileBaseJobs() ).andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.getFileJobsList( FILTER_JSON );
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        SusResponseDTO responseDto = response.getBody();
        Assert.assertEquals( Boolean.FALSE, responseDto.getSuccess() );
        Message responseMsg = responseDto.getMessage();
        Assert.assertEquals( ERROR_MESSAGE, responseMsg.getType() );

    }

    /**
     * Should get list of jobs in response data when sus job list is not empty.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */

    /* *************************************************************************
     * <getSuSBaseJobsTableUI under test>
     * *************************************************************************
     */

    /**
     * Should successfully get sus job table UI with valid class argument.
     */
    @Test
    public void shouldSuccessfullyGetSusJobTableUIWithValidClassArgument() {

        final List< TableColumn > expected = getJobUITableColumns();
        TableUI expectedList = new TableUI( getJobUITableColumns() );
        SusResponseDTO susResponseDTO = getResponseDto();
        susResponseDTO.setData( expectedList );
        mockSusClientGetRequest( susResponseDTO );
        final ResponseEntity< SusResponseDTO > response = controller.getSusJobTableUI( AUTH_TOKEN );
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );

        TableUI actualtableUI = ( TableUI ) response.getBody().getData();
        for ( final TableColumn actualTableColumn : actualtableUI.getColumns() ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }
        }

    }

    /**
     * Should successfully get file job table UI with valid class argument.
     */
    @Test
    public void shouldSuccessfullyGetFileJobTableUIWithValidClassArgument() {

        final List< TableColumn > expected = getJobUITableColumns();

        final ResponseEntity< SusResponseDTO > response = controller.getFileJobTableUI( AUTH_TOKEN );
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );

        TableUI actualtableUI = ( TableUI ) response.getBody().getData();
        for ( final TableColumn actualTableColumn : actualtableUI.getColumns() ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }
        }

    }

    /**
     * Should get job by id when valid job id is given.
     */
    @Ignore
    @Test
    public void shouldGetJobByIdWhenValidJobIdIsGiven() {
        List< Job > expectedJobs = fillJobList();
        SusResponseDTO susResponseDTO = getResponseDto();
        susResponseDTO.setData( expectedJobs );
        mockSusClientGetRequest( susResponseDTO );
        // EasyMock.expect( daemonManager.getAllSuSJobs( EasyMock.anyString() ) ).andReturn( expectedJobs );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.getJobById( JOB_ID.toString(), AUTH_TOKEN );
        Assert.assertNotNull( response );
        Job actual = ( Job ) response.getBody().getData();
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertNotNull( response.getBody().getData() );
        Assert.assertEquals( expectedJobs.get( FIRST_INDEX ), actual );

    }

    /**
     * Should successfully stop job by id when valid id is provided.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldSuccessfullyStopJobByIdWhenValidIdIsProvided() throws IOException {
        EasyMock.expect( daemonManager.stopJob( EasyMock.anyString(), EasyMock.anyString(), job ) ).andReturn( true );
        SusResponseDTO susResponseDTO = getResponseDto();
        mockSusClientGetRequest( susResponseDTO );
        EasyMock.expect( suscoreManager.getServerAPIBase() ).andReturn( DUMMY_SERVER_API ).anyTimes();
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.stopJob( AUTH_TOKEN, JOB_ID.toString() );
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertEquals( ERROR_MESSAGE, response.getBody().getMessage().getType() );

    }

    /**
     * Should return false when job is already completed or not in the list.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldReturnFalseWhenJobIsAlreadyCompletedOrNotInTheList() throws IOException {
        EasyMock.expect( daemonManager.stopJob( EasyMock.anyString(), EasyMock.anyString(), job ) ).andReturn( false );
        SusResponseDTO susResponseDTO = getResponseDto();
        mockSusClientGetRequest( susResponseDTO );
        EasyMock.expect( suscoreManager.getServerAPIBase() ).andReturn( DUMMY_SERVER_API ).anyTimes();
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.stopJob( AUTH_TOKEN, JOB_ID.toString() );
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertEquals( ERROR_MESSAGE, response.getBody().getMessage().getType() );

    }

    /**
     * Should not get job by id when in valid job id is given.
     */
    @Test
    public void shouldNotGetJobByIdWhenInValidJobIdIsGiven() {
        List< Job > expectedJobs = fillJobList();
        Job expected = null;
        EasyMock.expect( daemonManager.getAllJobs() ).andReturn( expectedJobs );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.getJobById( INVALID_JOB_ID, AUTH_TOKEN );
        Assert.assertNotNull( response );
        Job actual = ( Job ) response.getBody().getData();
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertEquals( expected, actual );

    }

    /**
     * Should not get successfully context menu for sus job when items jsonhave no id given.
     */
    @Test
    public void shouldNotGetSuccessfullyContextMenuForSusJobWhenItemsJsonhaveNoIdGiven() {
        ResponseEntity< SusResponseDTO > response = controller.getSusJobsContextMenu( AUTH_TOKEN, getEmptyItemsJson() );
        Assert.assertNotNull( response );
        List< ContextMenuItem > actual = ( List< ContextMenuItem > ) response.getBody().getData();
        Assert.assertNull( actual );

    }

    /**
     * Should successfully get sus job views when valid auth token is given.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldSuccessfullyGetSusJobViewsWhenValidAuthTokenIsGiven() throws IOException {

        SusResponseDTO susResponseDTO = getResponseDto();

        mockSusClientGetRequest( susResponseDTO );

        EasyMock.expect( suscoreManager.getServerAPIBase() ).andReturn( DUMMY_SERVER_API ).anyTimes();

        ResponseEntity< SusResponseDTO > response = controller.getSusJobsView( AUTH_TOKEN );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );

    }

    /**
     * Should successfully save sus job views when valid view json is given.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldSuccessfullySaveSusJobViewsWhenValidViewJsonIsGiven() throws IOException {

        SusResponseDTO susResponseDTO = getResponseDto();

        mockSusClientPostRequest( susResponseDTO, susResponseDTO );

        EasyMock.expect( suscoreManager.getServerAPIBase() ).andReturn( DUMMY_SERVER_API ).anyTimes();

        ResponseEntity< SusResponseDTO > response = controller.saveSusJobsView( AUTH_TOKEN, VIEW_JSON );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );

    }

    /**
     * Should successfully update sus job views when valid view json is given.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldSuccessfullyUpdateSusJobViewsWhenValidViewJsonIsGiven() throws IOException {

        SusResponseDTO susResponseDTO = getResponseDto();

        mockSusClientPutRequest( susResponseDTO, susResponseDTO );

        EasyMock.expect( suscoreManager.getServerAPIBase() ).andReturn( DUMMY_SERVER_API ).anyTimes();

        ResponseEntity< SusResponseDTO > response = controller.updateSusJobsView( AUTH_TOKEN, VIEW_ID, VIEW_JSON );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );

    }

    /**
     * Should successfully set sus job views as default when valid view id is given.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldSuccessfullySetSusJobViewsAsDefaultWhenValidViewIdIsGiven() throws IOException {

        SusResponseDTO susResponseDTO = getResponseDto();

        mockSusClientGetRequest( susResponseDTO );

        EasyMock.expect( suscoreManager.getServerAPIBase() ).andReturn( DUMMY_SERVER_API ).anyTimes();

        ResponseEntity< SusResponseDTO > response = controller.setSusJobsViewAsDefault( AUTH_TOKEN, VIEW_ID );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );

    }

    /**
     * Fill job list.
     *
     * @return the list
     */
    private List< Job > fillJobList() {
        List< Job > joblist = new ArrayList<>();
        job.setId( JOB_ID );
        job.setName( DUMMY_JOB_NAME );
        job.setRunMode( DUMMY_STRING_ABC );
        job.setDescription( DUMMY_STRING_ABC );
        job.setStatus( new Status( STATUS_ID, STATUS_RUNNING ) );
        job.setMachine( MACHINE );
        joblist.add( job );
        return joblist;

    }

    /**
     * Gets the items json.
     *
     * @return the items json
     */
    private String getItemsJson() {
        ItemsDTO dto = new ItemsDTO();
        List< UUID > ids = new ArrayList<>();
        ids.add( JOB_ID );
        dto.setItems( ids );
        return JsonUtils.toJson( dto );
    }

    /**
     * Gets the empty items json.
     *
     * @return the empty items json
     */
    private String getEmptyItemsJson() {
        ItemsDTO dto = new ItemsDTO();
        List< UUID > ids = new ArrayList<>();
        dto.setItems( ids );
        return JsonUtils.toJson( dto );
    }

    /**
     * Gets the job UI table columns.
     *
     * @return the job UI table columns
     */
    private List< TableColumn > getJobUITableColumns() {
        return GUIUtils.listColumns( JobImpl.class );
    }

    /**
     * Gets the response dto.
     *
     * @return the response dto
     */
    private SusResponseDTO getResponseDto() {
        SusResponseDTO res = new SusResponseDTO();
        return res;
    }

    /**
     * Mock sus client get request.
     *
     * @param susResponseDTO
     *         the sus response DTO
     */
    private void mockSusClientGetRequest( SusResponseDTO susResponseDTO ) {
        try {
            PowerMockito.doReturn( susResponseDTO ).when( SuSClient.class, "getRequest", Matchers.anyString(), Matchers.anyObject() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Mock sus client post request.
     *
     * @param susResponseDTO
     *         the sus response DTO
     * @param susResponseDTOAck
     *         the sus response DTO ack
     */
    private static void mockSusClientPostRequest( SusResponseDTO susResponseDTO, SusResponseDTO susResponseDTOAck ) {
        try {
            ( ( PowerMockitoStubber ) PowerMockito.doReturn( susResponseDTO ).doReturn( susResponseDTOAck ) ).when( SuSClient.class,
                    "postRequest", Matchers.anyString(), Matchers.anyString(), Matchers.anyObject() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Mock sus client put request.
     *
     * @param susResponseDTO
     *         the sus response DTO
     * @param susResponseDTOAck
     *         the sus response DTO ack
     */
    private static void mockSusClientPutRequest( SusResponseDTO susResponseDTO, SusResponseDTO susResponseDTOAck ) {
        try {
            ( ( PowerMockitoStubber ) PowerMockito.doReturn( susResponseDTO ).doReturn( susResponseDTOAck ) ).when( SuSClient.class,
                    "putRequest", Matchers.anyString(), Matchers.anyString(), Matchers.anyObject() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
