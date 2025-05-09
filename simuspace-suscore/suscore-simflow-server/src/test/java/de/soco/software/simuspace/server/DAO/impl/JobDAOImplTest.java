package de.soco.software.simuspace.server.DAO.impl;

import javax.persistence.EntityManager;

import java.util.Date;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.hibernate.Criteria;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.common.dao.JobDAO;
import de.soco.software.simuspace.suscore.data.common.dao.impl.JobDAOImpl;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * Test Cases for JobDAOImpl.
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { Activator.class } )
public class JobDAOImplTest {

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant WORK_FLOW_ENTITY_NAME.
     */
    private static final String WORK_FLOW_ENTITY_NAME = "test workflow entity name";

    /**
     * The Constant COMMENTS.
     */
    private static final String COMMENTS = "Test comments";

    /**
     * The Constant TEST_DESCRIPTION.
     */
    private static final String TEST_DESCRIPTION = "Test Description";

    /**
     * The Constant NAME.
     */
    private static final String NAME = "Job Test";

    /**
     * The Constant STATUS.
     */
    private static final int STATUS = 23;

    /**
     * The Constant DUMMY_JOB_DIR.
     */
    private static final String DUMMY_JOB_DIR = "/home/scesxxx/abc";

    /**
     * The Constant DUMMY_JOB_LOG_FILE_PATH.
     */
    private static final String DUMMY_JOB_LOG_FILE_PATH = "/home/scesxxx/abc/logFile.log";

    /**
     * The Constant WORK_FLOW_ID.
     */
    private static final UUID WORK_FLOW_ID = UUID.randomUUID();

    /**
     * The Constant VERSION_ID.
     */
    private static final int VERSION_ID = 1;

    /**
     * The Constant USER_ID.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant ID.
     */
    private static final UUID ID = UUID.randomUUID();

    /**
     * The job DAO.
     */
    private JobDAO jobDAO;

    /**
     * The job entity.
     */
    private JobEntity jobEntity;

    /**
     * The work flow entity.
     */
    private WorkflowEntity workFlowEntity;

    /**
     * The machine.
     */
    private final String MACHINE = "clockg.soco.pk";

    /**
     * The machine ip.
     */
    private final String MACHINE_IP = "172.2.0.130";

    /**
     * The run mode.
     */
    private final int RUN_MODE = 0;

    /**
     * The user entity.
     */
    private UserEntity userEntity;

    /**
     * Sets the up.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setUp() throws Exception {
        HibernateTestConfigration.setUp();
        PowerMockito.spy( Activator.class );
        PowerMockito.when( Activator.getSession() ).thenReturn( HibernateTestConfigration.getSession() );
        jobDAO = new JobDAOImpl();

    }

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    private static final byte[] VALID_LOG_RECORD_BYTES = { -84, -19, 0, 5, 115, 114, 0, 19, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46,
            65, 114, 114, 97, 121, 76, 105, 115, 116, 120, -127, -46, 29, -103, -57, 97, -99, 3, 0, 1, 73, 0, 4, 115, 105, 122, 101, 120,
            112, 0, 0, 0, 1, 119, 4, 0, 0, 0, 1, 115, 114, 0, 56, 100, 101, 46, 105, 115, 107, 111, 46, 115, 111, 102, 116, 119, 97, 114,
            101, 46, 115, 105, 109, 117, 115, 112, 97, 99, 101, 46, 119, 111, 114, 107, 102, 108, 111, 119, 46, 109, 111, 100, 101, 108, 46,
            105, 109, 112, 108, 46, 76, 111, 103, 82, 101, 99, 111, 114, 100, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 2, 76, 0, 10, 108, 111, 103, 77,
            101, 115, 115, 97, 103, 101, 116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76,
            0, 4, 116, 121, 112, 101, 113, 0, 126, 0, 3, 120, 112, 116, 0, 3, 100, 101, 102, 116, 0, 3, 97, 98, 99, 120 };

    private static final byte[] VALID_PARAM_MAPS_BYTES = { -84, -19, 0, 5, 115, 114, 0, 17, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46,
            72, 97, 115, 104, 77, 97, 112, 5, 7, -38, -63, -61, 22, 96, -47, 3, 0, 2, 70, 0, 10, 108, 111, 97, 100, 70, 97, 99, 116, 111,
            114, 73, 0, 9, 116, 104, 114, 101, 115, 104, 111, 108, 100, 120, 112, 63, 64, 0, 0, 0, 0, 0, 12, 119, 8, 0, 0, 0, 16, 0, 0, 0,
            1, 116, 0, 3, 97, 98, 99, 115, 113, 0, 126, 0, 0, 63, 64, 0, 0, 0, 0, 0, 12, 119, 8, 0, 0, 0, 16, 0, 0, 0, 1, 113, 0, 126, 0, 2,
            113, 0, 126, 0, 2, 120, 120 };

    /**
     * Fill job entity.
     */
    private void fillJobEntity() {
        jobEntity = new JobEntity();
        jobEntity.setFinishedOn( new Date() );
        jobEntity.setIOParameters( VALID_PARAM_MAPS_BYTES );
        jobEntity.setLog( VALID_LOG_RECORD_BYTES );
        jobEntity.setMachine( MACHINE );
        jobEntity.setRunMode( RUN_MODE );
        jobEntity.setStartedOn( new Date() );
        jobEntity.setCreatedOn( new Date() );
        jobEntity.setJobDirectory( DUMMY_JOB_DIR );
        fillWorkFlowEntity();
        jobEntity.setWorkflow( workFlowEntity );
        fillUserEntity();
        jobEntity.setCreatedBy( userEntity );
        jobEntity.setDescription( TEST_DESCRIPTION );
        jobEntity.setId( ID );
        jobEntity.setModifiedOn( new Date() );
        jobEntity.setName( NAME );
        jobEntity.setStatus( STATUS );
        jobEntity.setMachineIP( MACHINE_IP );
        jobEntity.setJobLogPath( DUMMY_JOB_LOG_FILE_PATH );

    }

    /**
     * Fill workflow primary key.
     */
    private void fillWorkflowPrimaryKey() {
        workFlowEntity.setComposedId( new VersionPrimaryKey( WORK_FLOW_ID, VERSION_ID ) );
    }

    /**
     * Fill user entity.
     */
    private void fillUserEntity() {
        userEntity = new UserEntity();
        userEntity.setId( USER_ID );
    }

    /**
     * Fill work flow entity.
     */
    private void fillWorkFlowEntity() {

        workFlowEntity = new WorkflowEntity();
        fillWorkflowPrimaryKey();
        workFlowEntity.setName( WORK_FLOW_ENTITY_NAME );
        fillUserEntity();
        workFlowEntity.setCreatedBy( userEntity );
        workFlowEntity.setIsPrivateWorkflow( false );
        workFlowEntity.setActive( true );
        workFlowEntity.setCreatedOn( new Date() );
        workFlowEntity.setDescription( TEST_DESCRIPTION );
        workFlowEntity.setKeyuser( userEntity );
        workFlowEntity.setCreatedBy( userEntity );

    }

    /**
     * Replay configration.
     */
    private void replayConfigration() {
        HibernateTestConfigration.mockControl().replay();
    }

    /* *************************************************************************
     * <getLastJobByWorkFlow under test>
     * *************************************************************************
     */

    /**
     * Should return job entity when query result is not null.
     */
    @Test
    public void shouldReturnJobEntityWhenQueryResultIsNotNull() {

        fillJobEntity();

        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( JobEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( jobEntity ).anyTimes();

        replayConfigration();

        final JobEntity result = jobDAO.getLastJobByWorkFlow( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID, MACHINE );
        Assert.assertNotNull( result );
        Assert.assertEquals( result.getDescription(), jobEntity.getDescription() );
        Assert.assertEquals( result.getJobDirectory(), jobEntity.getJobDirectory() );
        Assert.assertEquals( result.getMachine(), jobEntity.getMachine() );
        Assert.assertEquals( result.getName(), jobEntity.getName() );
        Assert.assertEquals( result.getCreatedBy(), jobEntity.getCreatedBy() );
        Assert.assertEquals( result.getCreatedOn(), jobEntity.getCreatedOn() );

    }

    /**
     * Should return null when query result list is empty.
     */
    @Test
    public void shouldReturnNullWhenQueryResultNull() {

        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( JobEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( null ).anyTimes();
        replayConfigration();
        final JobEntity result = jobDAO.getLastJobByWorkFlow( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID, MACHINE );
        Assert.assertNull( result );

    }

}
