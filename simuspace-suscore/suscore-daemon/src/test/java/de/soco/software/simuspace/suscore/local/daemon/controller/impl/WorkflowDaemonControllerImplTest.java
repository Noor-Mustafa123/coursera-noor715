package de.soco.software.simuspace.suscore.local.daemon.controller.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.local.daemon.manager.WorkflowDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.manager.impl.WorkflowDaemonManagerImpl;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;

/**
 * Test Cases for Class WorkflowEngineController.
 *
 * @author Nosheen.Sharif
 */

public class WorkflowDaemonControllerImplTest {

    private static final String JOB_STOP_FAILED = "Job Stop failed";

    private static final String JOB_STOP_SUCCESSFULLY = "Job Stop Successfully";

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
     * The Constant FAIL.
     */
    private static final String ERROR = "ERROR";

    /**
     * The Constant SUCCESS.
     */
    private static final String SUCCESS = "SUCCESS";

    /**
     * The Constant invalidJobId.
     */
    private static final String INVALID_JOB_ID = "abcdef";

    /**
     * The Constant PARENT_ID.
     */
    private static final String PARENT_ID = UUID.randomUUID().toString();

    /**
     * The Constant WORKFLOW_UI_NAME_LABEL.
     */
    private static final String WORKFLOW_UI_NAME_LABEL = "Name";

    /**
     * The Constant WORKFLOW_UI_NAME_NAME.
     */
    private static final String WORKFLOW_UI_NAME_NAME = "name";

    /**
     * The Constant WORKFLOW_UI_DESCRIPTION_LABEL.
     */
    private static final String WORKFLOW_UI_DESCRIPTION_LABEL = "Description";

    /**
     * The Constant WORKFLOW_UI_DESCRIPTION_NAME.
     */
    private static final String WORKFLOW_UI_DESCRIPTION_NAME = "description";

    /**
     * The application manager.
     */
    private WorkflowDaemonManager daemonManager;

    /**
     * The controller.
     */
    private WorkflowDaemonControllerImpl controller = new WorkflowDaemonControllerImpl();

    /**
     * The job.
     */
    private Job job;

    /**
     * To initialize the objects and mocking objects
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        daemonManager = mockControl.createMock( WorkflowDaemonManagerImpl.class );
        controller.setDaemonManager( daemonManager );
        job = new JobImpl();

    }

    /* *************************************************************************
     * <getFileBaseJobs under test>
     * *************************************************************************
     */

    /**
     * Should get empty response data when file job list is empty.
     */
    @Test
    public void shouldGetEmptyResponseDataWhenFileJobListIsEmpty() {
        EasyMock.expect( daemonManager.getFileBaseJobs() ).andReturn( null );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.getfileJobs();
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertNull( response.getBody().getData() );
    }

    /**
     * Should get list of jobs in response data when file job list is not empty.
     */
    @Test
    public void shouldGetListOfJobsInResponseDataWhenFileJobListIsNotEmpty() {
        List< Job > jobs = fillJobList();
        EasyMock.expect( daemonManager.getFileBaseJobs() ).andReturn( jobs );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.getfileJobs();
        Assert.assertNotNull( response );

        List< Job > actual = ( List< Job > ) response.getBody().getData();
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertNotNull( response.getBody().getData() );
        Assert.assertEquals( actual.size(), jobs.size() );
        for ( int i = 0; i < jobs.size(); i++ ) {
            Assert.assertEquals( jobs.get( i ), actual.get( i ) );
        }

    }

    /* *************************************************************************
     * <getRunningJobs under test>
     * *************************************************************************
     */

    /**
     * Should get empty response data when running file job list is null.
     */
    @Test
    public void shouldGetEmptyResponseDataWhenRunningFileJobListIsNull() {
        EasyMock.expect( daemonManager.getRunningJobs() ).andReturn( null );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.getListOfRunningJobs();
        Assert.assertNotNull( response );
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertNull( response.getBody().getData() );
    }

    /**
     * Should get list of running jobs in response data when running file job list is not empty.
     */
    @Test
    public void shouldGetListOfRunningJobsInResponseDataWhenRunningFileJobListIsNotEmpty() {
        List< Job > jobs = fillJobList();
        EasyMock.expect( daemonManager.getRunningJobs() ).andReturn( jobs );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.getListOfRunningJobs();
        Assert.assertNotNull( response );

        List< Job > actual = ( List< Job > ) response.getBody().getData();
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertNotNull( response.getBody().getData() );
        Assert.assertEquals( actual.size(), jobs.size() );
        for ( int i = 0; i < jobs.size(); i++ ) {
            Assert.assertEquals( jobs.get( i ), actual.get( i ) );
        }
    }

    /* *************************************************************************
     * <stopJob under test>
     * *************************************************************************
     */

    /**
     * Should stop job successfully when method stop job called.
     */
    @Test
    public void shouldStopJobSuccessfullyWhenMethodStopJobCalled() {

        EasyMock.expect( daemonManager.stopJob( EasyMock.anyString(), EasyMock.anyString(), job ) ).andReturn( true );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.stopJob( JOB_ID.toString(), JOB_ID.toString() );
        Assert.assertNotNull( response );
        SusResponseDTO actual = ( SusResponseDTO ) response.getBody();
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertNotNull( actual );
        Assert.assertTrue( actual.getSuccess() );
        Assert.assertEquals( SUCCESS, actual.getMessage().getType() );
        Assert.assertEquals( JOB_STOP_SUCCESSFULLY, actual.getMessage().getContent() );

    }

    /**
     * Should not stop job successfully when null job id is given.
     */
    @Test
    public void shouldNotStopJobSuccessfullyWhenNullJobIdIsGiven() {

        EasyMock.expect( daemonManager.stopJob( null, null, null ) ).andReturn( false );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.stopJob( null, null );
        Assert.assertNotNull( response );
        SusResponseDTO actual = ( SusResponseDTO ) response.getBody();
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual.getSuccess() );
        Assert.assertEquals( ERROR, actual.getMessage().getType() );
        Assert.assertEquals( JOB_STOP_FAILED, actual.getMessage().getContent() );

    }

    /**
     * Should sucessfully get import work flow UI.
     */
    @Test
    public void shouldSucessfullyGetImportWorkFlowUI() {
        UIForm expected = new UIForm();
        List< UIFormItem > expectedList = new ArrayList<>();
        expectedList.add( GUIUtils.createFormItem( WORKFLOW_UI_NAME_LABEL, WORKFLOW_UI_NAME_NAME, null ) );
        expectedList.add( GUIUtils.createFormItem( WORKFLOW_UI_DESCRIPTION_LABEL, WORKFLOW_UI_DESCRIPTION_NAME, null ) );
        expected.put( "default", expectedList );
        EasyMock.expect( daemonManager.createWorkflowForm( EasyMock.anyString() ) ).andReturn( expected );
        mockControl.replay();
        UIForm actual = daemonManager.createWorkflowForm( PARENT_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( expected, actual );

    }

    /**
     * Should not stop job successfully when invalid job id is given.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldNotStopJobSuccessfullyWhenInvalidJobIdIsGiven() {

        EasyMock.expect( daemonManager.stopJob( EasyMock.anyString(), EasyMock.anyString(), job ) ).andReturn( false );
        mockControl.replay();
        ResponseEntity< SusResponseDTO > response = controller.stopJob( EasyMock.anyString(), INVALID_JOB_ID );
        Assert.assertNotNull( response );
        SusResponseDTO actual = ( SusResponseDTO ) response.getBody();
        Assert.assertEquals( HttpStatus.OK, response.getStatusCode() );
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual.getSuccess() );
        Assert.assertEquals( ERROR, actual.getMessage().getType() );
        Assert.assertEquals( JOB_STOP_FAILED, actual.getMessage().getContent() );

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

}
