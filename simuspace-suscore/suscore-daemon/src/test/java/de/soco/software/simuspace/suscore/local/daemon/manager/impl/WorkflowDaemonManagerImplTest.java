package de.soco.software.simuspace.suscore.local.daemon.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.processing.impl.WorkflowExecutionManagerImpl;

/**
 * TestCases for Class SpringManager.
 *
 * @author Nosheen.Sharif
 */
@PrepareForTest( WorkflowDaemonManagerImpl.class )
public class WorkflowDaemonManagerImplTest {

    /**
     * The Constant INVALID_JOB_ID.
     */
    private static final String INVALID_JOB_ID = "abcd";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant SECOND_INDEX.
     */
    private static final int SECOND_INDEX = 1;

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The manager refernece.
     */
    private WorkflowDaemonManagerImpl manager;

    /**
     * The execution manager refernce.
     */
    private WorkflowExecutionManagerImpl executionManager;

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
    private static final int STATUS_ID = 20;

    /**
     * The Constant STATUS_RUNNING.
     */
    private static final String STATUS_RUNNING = "running";

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
     * The Constant WORKFLOW_UI_DESCRIPTION_PARENT_ID.
     */
    private static final String WORKFLOW_UI_DESCRIPTION_PARENT_ID = "parentId";

    /**
     * The job reference.
     */
    private Job job;

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        manager = new WorkflowDaemonManagerImpl();
        executionManager = mockControl.createMock( WorkflowExecutionManagerImpl.class );

        manager.setExecutionManager( executionManager );

    }

    /* *************************************************************************
     * <getFileBaseJobs under test>
     * *************************************************************************
     */

    /**
     * Should get empty response data when file job list is empty.
     */
    @Test
    public void shouldGetEmptyListOfJobsWhenFileJobListReturnNull() {
        EasyMock.expect( executionManager.getFileJobsList() ).andReturn( null );
        mockControl.replay();
        List< Job > actual = manager.getFileBaseJobs();
        Assert.assertNotNull( actual );
        Assert.assertTrue( actual.isEmpty() );

    }

    /**
     * Should get list of jobs when file job list is not null.
     */
    @Test
    public void shouldGetListOfJobsEmptyWhenFileJobListIsEmpty() {
        List< Job > jobs = fillJobList();
        Map< UUID, Job > map = getMapfromJobList( jobs );

        EasyMock.expect( executionManager.getFileJobsList() ).andReturn( map ).anyTimes();

        mockControl.replay();
        List< Job > actual = manager.getFileBaseJobs();
        Assert.assertNotNull( actual );
        Assert.assertTrue( actual.isEmpty() );

    }

    /**
     * Should get list of jobs when file job list is not null.
     */
    @Test
    public void shouldGetListOfJobsWhenFileJobListIsNotNull() {
        List< Job > jobs = fillJobList();
        for ( Job job : jobs ) {
            job.setIsFileRun( true );
        }
        Map< UUID, Job > map = getMapfromJobList( jobs );

        EasyMock.expect( executionManager.getFileJobsList() ).andReturn( map ).anyTimes();

        mockControl.replay();
        List< Job > actual = manager.getFileBaseJobs();
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual.isEmpty() );

        for ( int i = 0; i < jobs.size(); i++ ) {
            Assert.assertEquals( jobs.get( i ), actual.get( i ) );
        }

    }

    /* *************************************************************************
     * <getRunningJobs under test>
     * *************************************************************************
     */

    /**
     * Should get null when file job running list return empty map.
     */
    @Test
    public void shouldGetNullWhenFileJobRunningListReturnEmptyMap() {
        List< Job > jobs = fillJobList();
        Map< UUID, Job > map = getMapfromJobList( jobs );

        EasyMock.expect( executionManager.getFileJobsList() ).andReturn( map ).anyTimes();
        mockControl.replay();
        List< Job > actual = manager.getRunningJobs();
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual.isEmpty() );

        for ( int i = 0; i < jobs.size(); i++ ) {
            Assert.assertEquals( jobs.get( i ), actual.get( i ) );
        }

    }

    /**
     * Should get list of running jobs when file job running list return valid map.
     */
    @Test
    public void shouldGetListOfRunningJobsWhenFileJobRunningListReturnValidMap() {
        Map< UUID, Job > map = new HashMap<>();
        EasyMock.expect( executionManager.getFileJobsList() ).andReturn( map ).anyTimes();
        mockControl.replay();
        List< Job > actual = manager.getRunningJobs();
        Assert.assertNotNull( actual );
        Assert.assertTrue( actual.isEmpty() );

    }

    /* *************************************************************************
     * <stopJob under test>
     * *************************************************************************
     */

    /**
     * Should stop job successfully with valid job id.
     */
    @Test
    public void shouldStopJobSuccessfullyWithValidJobId() {
        EasyMock.expect( executionManager.stopJobExecution( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        mockControl.replay();
        boolean actual = manager.stopJob( EasyMock.anyString(), JOB_ID.toString(), job );
        Assert.assertTrue( actual );
    }

    /**
     * Should not stop job successfully with invalid job id.
     */
    @Test
    public void shouldNotStopJobSuccessfullyWithInvalidJobId() {
        EasyMock.expect( executionManager.stopJobExecution( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( false ).anyTimes();
        mockControl.replay();
        boolean actual = manager.stopJob( EasyMock.anyString(), INVALID_JOB_ID, job );
        Assert.assertFalse( actual );

    }

    /* *************************************************************************
     * <getAllJobs under test>
     * *************************************************************************
     */

    /**
     * Should sucessfully get list of jobs when manager return filled list.
     */
    @Test
    public void shouldSucessfullyGetListOfJobsWhenManagerReturnFilledList() {
        List< Job > jobs = fillJobList();
        EasyMock.expect( executionManager.getFileJobsList() ).andReturn( getMapfromJobList( jobs ) ).anyTimes();
        mockControl.replay();
        List< Job > actual = manager.getAllJobs();
        Assert.assertNotNull( actual );
        Assert.assertEquals( jobs.get( FIRST_INDEX ), actual.get( FIRST_INDEX ) );

    }

    /**
     * Should get empty list of jobs when manager return empty list.
     */
    @Test
    public void shouldGetEmptyListOfJobsWhenManagerReturnEmptyList() {
        List< Job > jobs = new ArrayList<>();
        EasyMock.expect( executionManager.getFileJobsList() ).andReturn( getMapfromJobList( jobs ) ).anyTimes();
        mockControl.replay();
        List< Job > actual = manager.getAllJobs();
        Assert.assertNotNull( actual );
        Assert.assertTrue( jobs.isEmpty() );

    }

    /**
     * Should sucessfully get import work flow UI.
     */
    @Test
    public void shouldSucessfullyGetImportWorkFlowUI() {
        List< UIFormItem > expected = new ArrayList<>();
        expected.add( GUIUtils.createFormItem( WORKFLOW_UI_DESCRIPTION_PARENT_ID, WORKFLOW_UI_DESCRIPTION_PARENT_ID, null ) );
        expected.add( GUIUtils.createFormItem( WORKFLOW_UI_NAME_LABEL, WORKFLOW_UI_NAME_NAME, null ) );
        expected.add( GUIUtils.createFormItem( WORKFLOW_UI_DESCRIPTION_LABEL, WORKFLOW_UI_DESCRIPTION_NAME, null ) );
        var actual = manager.createWorkflowForm( PARENT_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( expected.get( SECOND_INDEX ), actual.get( "default" ).get( SECOND_INDEX ) );

    }

    /**
     * Gets the mapfrom job list.
     *
     * @param jobs
     *         the jobs
     *
     * @return the mapfrom job list
     */
    private Map< UUID, Job > getMapfromJobList( List< Job > jobs ) {
        Map< UUID, Job > map = new HashMap<>();
        for ( Job job : jobs ) {
            map.put( job.getId(), job );
        }
        return map;
    }

    /**
     * Fill job list.
     *
     * @return the list
     */
    private List< Job > fillJobList() {
        List< Job > joblist = new ArrayList<>();
        job = new JobImpl();
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
