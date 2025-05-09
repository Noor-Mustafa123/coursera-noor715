/*
 *
 */

package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.server.dao.JobIdsDAO;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.server.manager.WorkflowUserManager;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.RunMode;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.data.common.dao.JobDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.impl.ObjectTypeConfigManagerImpl;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;

/**
 * This class is to test the job manager implementation class methods.
 *
 * @author sces126
 */
public class JobManagerImplTest {

    /**
     * Dummy Index for List get.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant DUMMY_INT_ONE.
     */
    private static final int DUMMY_INT_ONE = 1;

    /**
     * The Constant DUMMY_INT_FIVE.
     */
    private static final int DUMMY_INT_FIVE = 5;

    /**
     * The Constant DUMMY_LONG_FIVE.
     */
    private static final long DUMMY_LONG_FIVE = 5;

    /**
     * The Constant DUMMY_JOB_NAME.
     */
    private static final String DUMMY_JOB_NAME = "New Job 1";

    /**
     * The Constant DUMMY_STATUS_NAME.
     */
    private static final String DUMMY_STATUS_NAME = "Completed";

    /**
     * The Constant DUMMY_STRING_ABC.
     */
    private static final String DUMMY_STRING_ABC = "abc";

    /**
     * The Constant DUMMY_STRING_DEF.
     */
    private static final String DUMMY_STRING_DEF = "def";

    /**
     * The Constant DUMMY_JOB_DIR.
     */
    private static final String DUMMY_JOB_DIR = "/home/scesxxx/abc";

    /**
     * The Constant USER_ID.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant JOB_ID.
     */
    private static final UUID JOB_ID = UUID.randomUUID();

    /**
     * The Constant WORKFLOW_ID.
     */
    private static final UUID WORKFLOW_ID = UUID.randomUUID();

    /**
     * The Constant LOCATION_ID.
     */
    private static final UUID LOCATION_ID = UUID.randomUUID();

    /**
     * The Constant DEAFULT_LOCATION_NAME.
     */
    private static final String DEAFULT_LOCATION_NAME = "Default";

    /**
     * The machine.
     */
    private static final String MACHINE = "clockg.soco.pk";

    /**
     * The machine ip.
     */
    private static final String MACHINE_IP = "172.2.0.130";

    /**
     * The Constant DUMMY_JOB_LOG_FILE_PATH.
     */
    private static final String DUMMY_JOB_LOG_FILE_PATH = "/home/scesxxx/abc/logFile.log";

    /**
     * The map for preparing inputs.
     */
    private static Map< String, Map< String, Object > > map;

    /**
     * The Constant TABLE_COLUMN_LIST.
     */
    private static final String TABLE_COLUMN_LIST = "[{\"data\":\"jobInteger\",\"title\":\"Name\",\"filter\":\"\",\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"text\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":\"jobInteger\",\"manage\":true},\"name\":\"jobInteger\",\"orderNum\":0},{\"data\":\"type\",\"title\":\"Type\",\"filter\":\"text\",\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"text\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":\"type\",\"manage\":true},\"name\":\"type\",\"orderNum\":0},{\"data\":\"permissionDTOs.0.value\",\"title\":\"View\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.0.manage\"},\"name\":\"permissionDTOs-View\",\"orderNum\":0},{\"data\":\"permissionDTOs.1.value\",\"title\":\"ViewAuditLog\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.1.manage\"},\"name\":\"permissionDTOs-ViewAuditLog\",\"orderNum\":0},{\"data\":\"permissionDTOs.2.value\",\"title\":\"Read\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.2.manage\"},\"name\":\"permissionDTOs-Read\",\"orderNum\":0},{\"data\":\"permissionDTOs.3.value\",\"title\":\"Write\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.3.manage\"},\"name\":\"permissionDTOs-Write\",\"orderNum\":0},{\"data\":\"permissionDTOs.4.value\",\"title\":\"Execute\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.4.manage\"},\"name\":\"permissionDTOs-Execute\",\"orderNum\":0},{\"data\":\"permissionDTOs.5.value\",\"title\":\"Export\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.5.manage\"},\"name\":\"permissionDTOs-Export\",\"orderNum\":0},{\"data\":\"permissionDTOs.6.value\",\"title\":\"Delete\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.6.manage\"},\"name\":\"permissionDTOs-Delete\",\"orderNum\":0},{\"data\":\"permissionDTOs.7.value\",\"title\":\"Restore\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.7.manage\"},\"name\":\"permissionDTOs-Restore\",\"orderNum\":0},{\"data\":\"permissionDTOs.8.value\",\"title\":\"CreateNewObject\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.8.manage\"},\"name\":\"permissionDTOs-CreateNewObject\",\"orderNum\":0},{\"data\":\"permissionDTOs.9.value\",\"title\":\"CreateNewVersion\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.9.manage\"},\"name\":\"permissionDTOs-CreateNewVersion\",\"orderNum\":0},{\"data\":\"permissionDTOs.10.value\",\"title\":\"Update\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.10.manage\"},\"name\":\"permissionDTOs-Update\",\"orderNum\":0},{\"data\":\"permissionDTOs.11.value\",\"title\":\"Kill\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.11.manage\"},\"name\":\"permissionDTOs-Kill\",\"orderNum\":0},{\"data\":\"permissionDTOs.12.value\",\"title\":\"Share\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.12.manage\"},\"name\":\"permissionDTOs-Share\",\"orderNum\":0}]";

    /**
     * The Constant TAB_NAME_JOB_LOG.
     */
    private static final String TAB_NAME_JOB_LOG = "Log";

    /**
     * The Constant NOW_DATE.
     */
    private static final Date NOW_DATE = new Date();

    /**
     * The Constant OBJ_ID.
     */
    private static final UUID OBJ_ID = UUID.randomUUID();

    /**
     * The Constant TYPE_ID.
     */
    private static final UUID TYPE_ID = UUID.randomUUID();

    /**
     * Dummy Object Id.
     */
    private static final UUID DATA_OBJECT_ID = UUID.randomUUID();

    /**
     * Dummy Data Object Name of an object.
     */
    private static final String DATA_OBJECT_NAME = "Test Data Object name";

    /**
     * The Constant OBJECT_TYPE_ID.
     */
    private static final UUID OBJECT_TYPE_ID = UUID.randomUUID();

    /**
     * The Constant DEFAULT_LIFECYCLE_STATUS.
     */
    private static final String WIP_LIFECYCLE_STATUS_ID = "553536c7-71ec-409d-8f48-ec779a98a68e";

    /**
     * Dummy Version Id for test Cases.
     */
    private static final int DEFAULT_VERSION_ID = 1;

    /**
     * The run mode.
     */
    private final String RUN_MODE = "8d040038-8599-11e8-adc0-fa7ae01bbebc";

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant VALID_PARAM_MAPS_BYTES.
     */
    private static final byte[] VALID_PARAM_MAPS_BYTES = { -84, -19, 0, 5, 115, 114, 0, 17, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46,
            72, 97, 115, 104, 77, 97, 112, 5, 7, -38, -63, -61, 22, 96, -47, 3, 0, 2, 70, 0, 10, 108, 111, 97, 100, 70, 97, 99, 116, 111,
            114, 73, 0, 9, 116, 104, 114, 101, 115, 104, 111, 108, 100, 120, 112, 63, 64, 0, 0, 0, 0, 0, 12, 119, 8, 0, 0, 0, 16, 0, 0, 0,
            1, 116, 0, 3, 97, 98, 99, 115, 113, 0, 126, 0, 0, 63, 64, 0, 0, 0, 0, 0, 12, 119, 8, 0, 0, 0, 16, 0, 0, 0, 1, 113, 0, 126, 0, 2,
            113, 0, 126, 0, 2, 120, 120 };

    /**
     * The Constant default user id to by pass permission checking.
     */
    private static final UUID DEFAULT_BE_USER_ID = UUID.randomUUID();

    /**
     * The Constant INVALID_JOB_ID.
     */
    private static final String INVALID_JOB_ID = "35-345958-332d";

    /**
     * The Constant INVALID_JOB_ID_ERROR_MSG.
     */
    private static final String INVALID_JOB_ID_ERROR_MSG = "Invalid UUID string: ";

    /**
     * SusDao reference.
     */
    private SuSGenericObjectDAO< JobEntity > susDao;

    private SuSGenericObjectDAO< SuSEntity > susEntityDao;

    /**
     * The config manager.
     */
    private ObjectTypeConfigManagerImpl configManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The Constant WIP_LIFECYCLE_STATUS_NAME.
     */
    private static final String WIP_LIFECYCLE_STATUS_NAME = "WIP";

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    private static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Its the Data Access Object for workflow jobs
     */
    private JobDAO jobDao;

    private JobIdsDAO jobIdsDAO;

    /**
     * Its the data transfer object for job
     */
    private JobImpl jobImpl;

    /**
     * The job entity.
     */
    private JobEntity jobEntity;

    /**
     * Its workflow job manager implementation class object
     */
    private JobManagerImpl jobManagerImpl;

    /**
     * The user manager impl.
     */
    private static WorkflowUserManager userManagerImpl;

    /**
     * The user entity.
     */
    private UserEntity userEntity;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Its workflow manager interface object
     */
    private WorkflowManager workflowManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * Fill job entity.
     */
    private void fillJobEntity() {
        jobEntity = new JobEntity();
        jobEntity.setId( JOB_ID );
        jobEntity.setName( DUMMY_JOB_NAME );
        jobEntity.setRunMode( DUMMY_INT_ONE );
        jobEntity.setRunsOn( new LocationEntity( UUID.fromString( RUN_MODE ), "", DUMMY_STRING_ABC, true ) );
        jobEntity.setDescription( DUMMY_STRING_ABC );
        jobEntity.setLog( convertFromLogRecordListToByteArray( getLogsList() ) );
        jobEntity.setIOParameters( VALID_PARAM_MAPS_BYTES );
        jobEntity.setJobLogPath( DUMMY_JOB_LOG_FILE_PATH );
        jobEntity.setMachine( MACHINE );
        jobEntity.setMachineIP( MACHINE_IP );
        jobEntity.setCreatedOn( new Date() );
        jobEntity.setFinishedOn( new Date() );
        jobEntity.setTotalElements( DUMMY_LONG_FIVE );
        jobEntity.setCompletedElements( DUMMY_LONG_FIVE );
        jobEntity.setWorkflowId( WORKFLOW_ID );
        jobEntity.setJobType( DUMMY_INT_ONE );
        fillUserEntity();
        jobEntity.setCreatedBy( userEntity );

    }

    /**
     * Fill user entity.
     */
    private void fillUserEntity() {
        userEntity = new UserEntity();
        userEntity.setId( USER_ID );
    }

    /**
     * Sets the map.
     */
    private void setMap() {
        map = new HashMap<>();
        final Map< String, Object > mapInput = new HashMap<>();
        mapInput.put( "key1", "value1" );
        mapInput.put( "key2", "value2" );
        map.put( "h1", mapInput );
        map.put( "h2", mapInput );
    }

    /**
     * To initialize the objects and mocking objects
     */

    @Before
    public void setup() {
        mockControl.resetToNice();
        jobImpl = new JobImpl();
        jobManagerImpl = new JobManagerImpl();
        jobIdsDAO = mockControl().createMock( JobIdsDAO.class );
        susDao = mockControl().createMock( SuSGenericObjectDAO.class );
        susEntityDao = mockControl.createMock( SuSGenericObjectDAO.class );
        jobDao = mockControl.createMock( JobDAO.class );
        configManager = mockControl.createMock( ObjectTypeConfigManagerImpl.class );
        workflowManager = mockControl.createMock( WorkflowManager.class );
        permissionManager = mockControl.createMock( PermissionManager.class );
        locationManager = mockControl.createMock( LocationManager.class );
        licenseManager = mockControl.createMock( LicenseManager.class );
        objectViewManager = mockControl.createMock( ObjectViewManager.class );
        userCommonManager = mockControl.createMock( UserCommonManager.class );
        jobManagerImpl.setObjectViewManager( objectViewManager );
        jobManagerImpl.setJobDao( jobDao );
        jobManagerImpl.setJobIdsDAO( jobIdsDAO );
        jobManagerImpl.setWorkflowManager( workflowManager );
        userManagerImpl = mockControl().createMock( WorkflowUserManager.class );
        jobManagerImpl.setUserManager( userManagerImpl );
        jobManagerImpl.setJobEntityDAO( susDao );
        jobManagerImpl.setPermissionManager( permissionManager );
        jobManagerImpl.setLicenseManager( licenseManager );
        jobManagerImpl.setConfigManager( configManager );
        jobManagerImpl.setUserCommonManager( userCommonManager );
        jobManagerImpl.setLocationManager( locationManager );
        jobManagerImpl.setSusDAO( susEntityDao );

    }

    /**
     * To clear the objects and mocking objects
     */
    @After
    public void tearDown() {
        jobImpl = null;
        jobManagerImpl = null;
        jobDao = null;
        workflowManager = null;
    }

    /**
     * Should return list of jobs when Dao return list of jobs.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnListOfJobsWhenDaoReturnListofJobs() throws SusException {
        final List< JobEntity > expected = new ArrayList<>();
        fillJobEntity();
        expected.add( jobEntity );
        EasyMock.expect( jobDao.getJobsList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( expected ).anyTimes();
        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
        mockControl.replay();

        final List< Job > actual = jobManagerImpl.getJobsList( DEFAULT_BE_USER_ID );
        Assert.assertFalse( actual.isEmpty() );
        Assert.assertEquals( expected.size(), actual.size() );
        Assert.assertNotNull( actual.get( 0 ) );
        Assert.assertEquals( DUMMY_JOB_NAME, actual.get( 0 ).getName() );
        Assert.assertEquals( JOB_ID, actual.get( 0 ).getId() );
    }

    /**
     * Should return empty list of jobs when Dao return empty list.
     *
     * @throws SusException
     *         the sus exception Should return list of jobs.
     * @throws SusException
     */

    @Test
    public void shouldReturnListOfJobs() throws SusException {
        final List< JobEntity > list = new ArrayList<>();
        fillJobEntity();
        list.add( jobEntity );

        EasyMock.expect( jobDao.getJobsList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( list ).anyTimes();
        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
        mockControl.replay();
        jobManagerImpl.setJobDao( jobDao );
        final List< Job > actual = jobManagerImpl.getJobsList( DEFAULT_BE_USER_ID );
        Assert.assertFalse( actual.isEmpty() );

        Assert.assertEquals( actual.get( FIRST_INDEX ).getDescription(), jobEntity.getDescription() );
        Assert.assertEquals( actual.get( FIRST_INDEX ).getName(), jobEntity.getName() );
        Assert.assertEquals( actual.get( FIRST_INDEX ).getMachine(), jobEntity.getMachine() );
        Assert.assertEquals( actual.get( FIRST_INDEX ).getStatus().getId(), jobEntity.getStatus() );

    }

    /**
     * Should return empty list of jobs.
     *
     * @throws SusException
     */
    @Test
    public void shouldReturnEmptyListOfJobs() throws SusException {
        final List< JobEntity > list = new ArrayList<>();
        EasyMock.expect( jobDao.getJobsList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( list ).anyTimes();
        mockControl.replay();
        jobManagerImpl.setJobDao( jobDao );
        final List< Job > ret = jobManagerImpl.getJobsList( DEFAULT_BE_USER_ID );
        Assert.assertTrue( ret.isEmpty() );
    }

    /**
     * Should update log of job by valid job id and return true.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldUpdateLogOfJobByValidJobIdAndReturnTrue() throws SusException {

        fillJobEntity();
        setMap();
        jobEntity.setIOParameters( VALID_PARAM_MAPS_BYTES );
        final JobEntity expectedEntity = jobEntity;
        EasyMock.expect( jobDao.getJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect( jobDao.updateJobLog( EasyMock.anyObject( EntityManager.class ), jobEntity ) ).andReturn( expectedEntity )
                .anyTimes();
        EasyMock.expect(
                        susDao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
        mockControl.replay();
        jobImpl.setLog( getLogsList() );
        jobImpl.setIOParameters( map );
        jobImpl.setProgress( new ProgressBar( DUMMY_INT_FIVE, DUMMY_INT_FIVE ) );
        final Job result = jobManagerImpl.updateJobLog( jobImpl );
        Collections.reverse( result.getLog() );
        Assert.assertEquals( JOB_ID, result.getId() );
        Assert.assertEquals( result.getLog().size(), getLogsList().size() );
        Assert.assertEquals( result.getLog().get( FIRST_INDEX ).getDate(), jobImpl.getLog().get( FIRST_INDEX ).getDate() );
        Assert.assertEquals( result.getLog().get( FIRST_INDEX ).getType(), jobImpl.getLog().get( FIRST_INDEX ).getType() );
        Assert.assertEquals( result.getLog().get( FIRST_INDEX ).getLogMessage(), jobImpl.getLog().get( FIRST_INDEX ).getLogMessage() );

    }

    /**
     * Should successfully create job when valid job json input is provided.
     */
    @Test
    public void shouldSuccessfullyCreateJobWhenValidJobJsonInputIsProvided() {

        fillJobEntity();
        fillJobImpl();
        setMap();
        jobEntity.setIOParameters( VALID_PARAM_MAPS_BYTES );
        jobEntity.setWorkflow( new WorkflowEntity( WORKFLOW_ID, DUMMY_INT_ONE ) );
        final JobEntity expectedEntity = jobEntity;
        EasyMock.expect( jobDao.getJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect( jobDao.saveJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( expectedEntity )
                .anyTimes();
        EasyMock.expect(
                        susDao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();

        EasyMock.expect( locationManager.getLocation( EasyMock.anyString() ) )
                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
        mockControl.replay();

        final Job result = jobManagerImpl.createJob( DEFAULT_BE_USER_ID, jobImpl );
        Collections.reverse( result.getLog() );
        Assert.assertEquals( JOB_ID, result.getId() );
        Assert.assertEquals( result.getLog().size(), getLogsList().size() );
        Assert.assertEquals( result.getLog().get( FIRST_INDEX ).getDate(), jobImpl.getLog().get( FIRST_INDEX ).getDate() );
        Assert.assertEquals( result.getLog().get( FIRST_INDEX ).getLogMessage(), jobImpl.getLog().get( FIRST_INDEX ).getLogMessage() );
        Assert.assertEquals( result.getLog().get( FIRST_INDEX ).getType(), jobImpl.getLog().get( FIRST_INDEX ).getType() );

    }

    /**
     * Should successfully update job when valid job json input is provided.
     */
    @Test
    public void shouldSuccessfullyUpdateJobWhenValidJobJsonInputIsProvided() {

        fillJobEntity();
        fillJobImpl();
        setMap();
        jobEntity.setIOParameters( VALID_PARAM_MAPS_BYTES );
        jobEntity.setWorkflow( new WorkflowEntity( WORKFLOW_ID, DUMMY_INT_ONE ) );
        final JobEntity expectedEntity = jobEntity;
        EasyMock.expect( jobDao.getJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect( jobDao.updateJobStatus( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect(
                        susDao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        final LatestWorkFlowDTO latestWorkflow = fillLatestWorkflow();
        final WorkflowDTO workflow = fillWorkflow();
        EasyMock.expect( workflowManager.getWorkflowById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( latestWorkflow ).anyTimes();

        EasyMock.expect( workflowManager.getWorkflowById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyString() ) ).andReturn( workflow ).anyTimes();
        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
        mockControl.replay();

        final Job result = jobManagerImpl.updateJob( EasyMock.anyObject( EntityManager.class ), jobImpl );
        Collections.reverse( result.getLog() );
        Assert.assertEquals( JOB_ID, result.getId() );
        Assert.assertEquals( result.getLog().size(), getLogsList().size() );
        Assert.assertEquals( result.getLog().get( FIRST_INDEX ).getDate(), jobImpl.getLog().get( FIRST_INDEX ).getDate() );
        Assert.assertEquals( result.getLog().get( FIRST_INDEX ).getLogMessage(), jobImpl.getLog().get( FIRST_INDEX ).getLogMessage() );
        Assert.assertEquals( result.getLog().get( FIRST_INDEX ).getType(), jobImpl.getLog().get( FIRST_INDEX ).getType() );

    }

    /**
     * Should get job model as null when dao return null job entity for update job status.
     */
    @Test
    public void shouldGetJobModelAsNullWhenDaoReturnNullJobEntityForUpdateJobStatus() {

        fillJobEntity();
        fillJobImpl();
        setMap();
        jobEntity.setIOParameters( VALID_PARAM_MAPS_BYTES );
        jobEntity.setWorkflow( new WorkflowEntity( WORKFLOW_ID, DUMMY_INT_ONE ) );
        final JobEntity expectedEntity = jobEntity;
        EasyMock.expect( jobDao.getJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect( jobDao.updateJobStatus( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( null )
                .anyTimes();
        EasyMock.expect(
                        susDao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        final LatestWorkFlowDTO latestWorkflow = fillLatestWorkflow();
        final WorkflowDTO workflow = fillWorkflow();
        EasyMock.expect( workflowManager.getWorkflowById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( latestWorkflow ).anyTimes();

        EasyMock.expect( workflowManager.getWorkflowById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyString() ) ).andReturn( workflow ).anyTimes();
        mockControl.replay();

        Assert.assertNull( jobManagerImpl.updateJob( EasyMock.anyObject( EntityManager.class ), jobImpl ) );

    }

    /**
     * Should get null as object when manager gives null pointer exception in create job.
     */
    @Test
    public void shouldGetNullAsObjectWhenManagerGivesNullPointerExceptionInCreateJob() {

        fillJobEntity();
        fillJobImpl();
        setMap();
        jobEntity.setIOParameters( VALID_PARAM_MAPS_BYTES );
        jobEntity.setWorkflow( new WorkflowEntity( WORKFLOW_ID, DUMMY_INT_ONE ) );
        final JobEntity expectedEntity = jobEntity;
        EasyMock.expect( jobDao.getJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect( jobDao.saveJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( expectedEntity )
                .anyTimes();
        EasyMock.expect(
                        susDao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        final LatestWorkFlowDTO latestWorkflow = fillLatestWorkflow();
        fillWorkflow();
        EasyMock.expect( workflowManager.getWorkflowById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( latestWorkflow ).anyTimes();

        EasyMock.expect( workflowManager.getWorkflowById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyString() ) ).andThrow( new NullPointerException() );

        mockControl.replay();

        final Job result = jobManagerImpl.createJob( EasyMock.anyObject( EntityManager.class ), DEFAULT_BE_USER_ID, jobImpl );
        Assert.assertNull( result );

    }

    /**
     * Should return null when update log dao return null.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnNullWhenUpdateLogDaoReturnNull() throws SusException {
        fillJobEntity();
        EasyMock.expect( jobDao.getJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) ).andReturn( null )
                .anyTimes();
        EasyMock.expect( jobDao.updateJobLog( EasyMock.anyObject( EntityManager.class ), jobEntity ) ).andReturn( null ).anyTimes();
        mockControl.replay();
        jobImpl.setProgress( new ProgressBar( DUMMY_INT_FIVE, DUMMY_INT_FIVE ) );

        final Job result = jobManagerImpl.updateJobLog( jobImpl );
        Assert.assertNull( result );

    }

    /**
     * Gets the logs list.
     *
     * @return the logs list
     */
    private List< LogRecord > getLogsList() {
        final List< LogRecord > list = new ArrayList<>();
        list.add( new LogRecord( "INFO", "TEst Message 1", NOW_DATE ) );
        list.add( new LogRecord( "INFO", "TEst Message 2", NOW_DATE ) );
        return list;
    }

    /**
     * Should return last job directory name when valid work flow idis given.
     *
     * @throws UnknownHostException
     *         the unknown host exception
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnLastJobDirectoryNameWhenValidWorkFlowIdisGiven() throws UnknownHostException, SusException {
        final JobEntity entity = new JobEntity();
        entity.setJobDirectory( DUMMY_JOB_DIR );
        EasyMock.expect( jobDao.getLastJobByWorkFlow( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyString() ) ).andReturn( entity ).anyTimes();
        mockControl.replay();
        final Map< String, String > expected = jobManagerImpl.getLastJobDirectoryByWorkFlow( UUID.randomUUID().toString() );
        Assert.assertNotNull( expected );
        Assert.assertFalse( expected.isEmpty() );
        Assert.assertEquals( DUMMY_JOB_DIR, expected.get( ConstantsString.JOB_DIR ) );
    }

    /*
     * *************************************************************************
     * <getMySusJobsList under test>
     * *************************************************************************
     */

    /**
     * Should return null when DAO result is null to get My jobs list.
     *
     * @throws Exception
     */

    @Test
    public void shouldReturnNullWhenDAOResultIsNullToGetMyJobsList() throws Exception {
        final FiltersDTO filter = fillFilterForDataTable();
        EasyMock.expect( jobDao.getJobsByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( null ).anyTimes();

        final FilteredResponse< Job > expected = jobManagerImpl.getMySusJobsList( USER_ID, filter );
        Assert.assertTrue( expected.getData().isEmpty() );

    }

    /**
     * Should return null when inputis null given to get my jobs list.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnNullWhenInputisNullGivenToGetMyJobsList() throws Exception {
        final FiltersDTO filter = fillFilterForDataTable();
        EasyMock.expect( jobDao.getJobsByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( null ).anyTimes();

        final FilteredResponse< Job > expected = jobManagerImpl.getMySusJobsList( USER_ID, filter );
        Assert.assertTrue( expected.getData().isEmpty() );

    }

    /**
     * Should return valid list of my jobs when valid inputis given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnValidListOfMyJobsWhenValidInputisGiven() throws Exception {
        final FiltersDTO filter = fillFilterForDataTable();
        fillJobEntity();
        final List< JobEntity > expected = new ArrayList<>();
        expected.add( jobEntity );
        EasyMock.expect( jobDao.getJobsByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expected ).anyTimes();
        EasyMock.expect( workflowManager.getIPFromToken( EasyMock.anyObject() ) ).andReturn( MACHINE_IP );
        EasyMock.expect( locationManager.getLocation( EasyMock.anyString() ) )
                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
        mockControl.replay();
        final FilteredResponse< Job > actualData = jobManagerImpl.getMySusJobsList( USER_ID, filter );
        Assert.assertNotNull( actualData );
        final List< Job > actual = actualData.getData();
        Assert.assertEquals( expected.size(), actual.size() );
        Assert.assertEquals( actual.get( FIRST_INDEX ).getDescription(), jobEntity.getDescription() );
        Assert.assertEquals( actual.get( FIRST_INDEX ).getName(), jobEntity.getName() );
        Assert.assertEquals( actual.get( FIRST_INDEX ).getMachine(), jobEntity.getMachine() );
        Assert.assertEquals( actual.get( FIRST_INDEX ).getStatus().getId(), jobEntity.getStatus() );

    }

    /**
     * Should successfully get table columns for job UI when valid table UI for job is given.
     */
    @Test
    public void shouldSuccessfullyGetTableColumnsForJobUIWhenValidTableUIForJobIsGiven() {
        final List< TableColumn > expectedUI = getFilledColumns().getColumns();
        final TableUI actualTableUI = jobManagerImpl.getListOfJobUITableColumns();
        // final List< TableColumn > actualTableUI =
        // jobManagerImpl.getListOfJobUITableColumns();
        Assert.assertTrue( !actualTableUI.getColumns().isEmpty() );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getData(), actualTableUI.getColumns().get( FIRST_INDEX ).getData() );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getFilter(), actualTableUI.getColumns().get( FIRST_INDEX ).getFilter() );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getName(), actualTableUI.getColumns().get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getTitle(),
                MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_NAME.getKey() ) );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getRenderer().getData(),
                actualTableUI.getColumns().get( FIRST_INDEX ).getRenderer().getData() );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getRenderer().getManage(),
                actualTableUI.getColumns().get( FIRST_INDEX ).getRenderer().getManage() );
    }

    /**
     * Should successfully get table columns for job data created when valid table UI for job is given.
     */
    @Test
    public void shouldSuccessfullyGetTableColumnsForJobDataCreatedWhenValidTableUIForJobIsGiven() {
        final List< TableColumn > expectedUI = getFilledColumns().getColumns();
        final List< TableColumn > actualTableUI = jobManagerImpl.getJobDataCreatedTableUI();
        Assert.assertTrue( !actualTableUI.isEmpty() );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getData(), actualTableUI.get( FIRST_INDEX ).getData() );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getFilter(), actualTableUI.get( FIRST_INDEX ).getFilter() );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getName(), actualTableUI.get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getTitle(),
                MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_NAME.getKey() ) );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getRenderer().getData(),
                actualTableUI.get( FIRST_INDEX ).getRenderer().getData() );
        Assert.assertEquals( expectedUI.get( FIRST_INDEX ).getRenderer().getManage(),
                actualTableUI.get( FIRST_INDEX ).getRenderer().getManage() );
    }

    /**
     * Should successfully get table columns for job permission UI when valid table UI for job is given.
     */
    @Test
    public void shouldSuccessfullyGetTableColumnsForJobPermissionUIWhenValidTableUIForJobIsGiven() {
        final TableUI expectedUI = getFilledColumns();
        EasyMock.expect( workflowManager.workflowPermissionTableUI() ).andReturn( expectedUI.getColumns() ).anyTimes();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( null ).anyTimes();
        mockControl().replay();
        final TableUI actualUI = jobManagerImpl.getJobPermissionUITable( USER_ID );
        Assert.assertTrue( !actualUI.getColumns().isEmpty() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getData(), actualUI.getColumns().get( FIRST_INDEX ).getData() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getFilter(), actualUI.getColumns().get( FIRST_INDEX ).getFilter() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getName(), actualUI.getColumns().get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getTitle(),
                MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_NAME.getKey() ) );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getRenderer().getData(),
                actualUI.getColumns().get( FIRST_INDEX ).getRenderer().getData() );
        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getRenderer().getManage(),
                actualUI.getColumns().get( FIRST_INDEX ).getRenderer().getManage() );
    }

    /**
     * Should succesfully get tab view for job UI when valid job id is given.
     */
    @Test
    public void shouldSuccesfullyGetTabViewForJobUIWhenValidJobIdIsGiven() {
        fillJobEntity();
        final JobEntity expectedEntity = jobEntity;
        EasyMock.expect( susDao.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedEntity ).anyTimes();
        mockControl.replay();
        final SubTabsItem actual = jobManagerImpl.getTabsViewJobUI( JOB_ID.toString() );
        Assert.assertNotNull( actual );
        Assert.assertEquals( expectedEntity.getId().toString(), actual.getId() );
        Assert.assertEquals( expectedEntity.getName(), actual.getTitle() );
        Assert.assertFalse( actual.getTabs().isEmpty() );
        Assert.assertEquals( TAB_NAME_JOB_LOG, actual.getTabs().get( FIRST_INDEX ).getName() );

    }

    /**
     * Should throw illegal argument exception to get tab view for job UI when in valid job id is given.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionToGetTabViewForJobUIWhenInValidJobIdIsGiven() {

        thrown.expect( IllegalArgumentException.class );
        thrown.expectMessage( INVALID_JOB_ID_ERROR_MSG + INVALID_JOB_ID );
        jobManagerImpl.getTabsViewJobUI( INVALID_JOB_ID );

    }

    /**
     * Should get filtered job list empty when job record has no read permission for user.
     */
    @Test
    public void shouldGetFilteredJobListEmptyWhenJobRecordHasNoReadPermissionForUser() {
        fillJobEntity();
        final JobEntity expectedEntity = jobEntity;
        final FiltersDTO filter = fillFilterForDataTable();
        final List< JobEntity > list = new ArrayList<>();
        list.add( expectedEntity );
        final List< JobEntity > expectedList = new ArrayList<>();

        EasyMock.expect( susDao.getAllFilteredRecords( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( list ).anyTimes();
        EasyMock.expect( permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( false ).anyTimes();

        mockControl.replay();
        final FilteredResponse< Job > actualFilteredData = jobManagerImpl.getFilteredJobsList( USER_ID, filter );
        Assert.assertEquals( expectedList, actualFilteredData.getData() );

    }

    /**
     * Should throw exception in get filtered job list when filter parameter is null.
     */
    @Test
    public void shouldThrowExceptionInGetFilteredJobListWhenFilterParameterIsNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
        final FiltersDTO filter = null;
        jobManagerImpl.getFilteredJobsList( USER_ID, filter );

    }

    /**
     * Should throw exception in get filtered job data created list when filter parameter is null.
     */
    @Test
    public void shouldThrowExceptionInGetFilteredJobDataCreatedListWhenFilterParameterIsNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
        final FiltersDTO filter = null;
        jobManagerImpl.getFilteredJobDataCreatedObjectsList( UUID.fromString( ConstantsID.SUPER_USER_ID ), JOB_ID.toString(), filter );
        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
    }

    /**
     * Should succes fully get filtered job log list when valid input parameters are given.
     */
    @Test
    public void shouldSuccesFullyGetFilteredJobLogListWhenValidInputParametersAreGiven() {
        fillJobEntity();
        final JobEntity expectedEntity = jobEntity;
        final List< LogRecord > expectedLog = convertFromByteArrayToLogRecordList( expectedEntity.getLog() );
        final FiltersDTO filter = fillFilterForDataTable();

        EasyMock.expect(
                        susDao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
        mockControl.replay();
        final FilteredResponse< LogRecord > actualFilteredData = jobManagerImpl.getFilteredJobLogList( USER_ID, JOB_ID.toString(), filter );

        Assert.assertNotNull( actualFilteredData.getData() );
        final List< LogRecord > actualData = actualFilteredData.getData();
        Collections.reverse( actualData );
        Assert.assertEquals( expectedLog.get( FIRST_INDEX ).getDate(), actualData.get( FIRST_INDEX ).getDate() );
        Assert.assertEquals( expectedLog.get( FIRST_INDEX ).getType(), actualData.get( FIRST_INDEX ).getType() );
        Assert.assertEquals( expectedLog.get( FIRST_INDEX ).getLogMessage(), actualData.get( FIRST_INDEX ).getLogMessage() );

    }

    /**
     * Should successfully get filtered job data created list when valid input parameters are given.
     */
    @Test
    public void shouldSuccessfullyGetFilteredJobDataCreatedListWhenValidInputParametersAreGiven() {
        final DataObjectEntity entity = fillDataObjectEntity();
        entity.setWorkflowId( WORKFLOW_ID );
        entity.setJobId( JOB_ID );
        entity.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
        entity.setTypeId( TYPE_ID );
        entity.setConfig( WIP_LIFECYCLE_STATUS_ID );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( USER_ID );
        userEntity.setChangeable( true );
        entity.setCreatedBy( userEntity );
        userEntity.setRestricted( true );
        userEntity.setStatus( true );
        entity.setModifiedOn( NOW_DATE );
        entity.setModifiedBy( userEntity );
        final List< SuSEntity > expectedSusEntityList = new ArrayList<>();
        expectedSusEntityList.add( entity );
        final List< GenericDTO > expectedList = new ArrayList<>();
        expectedList.add( fillGenericDto() );
        final FiltersDTO filter = fillFilterForDataTable();
        final SuSObjectModel model = new SuSObjectModel();

        EasyMock.expect( susEntityDao.getAllFilteredRecordsByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(), EasyMock.anyObject( FiltersDTO.class ),
                EasyMock.anyBoolean(), EasyMock.anyObject() ) ).andReturn( expectedSusEntityList ).anyTimes();
        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
                .anyTimes();
        mockControl.replay();
        final FilteredResponse< GenericDTO > actualFilteredData = jobManagerImpl.getFilteredJobDataCreatedObjectsList( USER_ID,
                JOB_ID.toString(), filter );

        Assert.assertNotNull( actualFilteredData.getData() );
        Assert.assertFalse( actualFilteredData.getData().isEmpty() );
        final GenericDTO actualObject = actualFilteredData.getData().get( FIRST_INDEX );
        Assert.assertEquals( actualObject.getId(), expectedSusEntityList.get( FIRST_INDEX ).getComposedId().getId() );
        Assert.assertEquals( actualObject.getName(), expectedSusEntityList.get( FIRST_INDEX ).getName() );
    }

    /**
     * Should get error message in get filtered job log list when no job exist W ith given id.
     */
    @Test
    public void shouldGetErrorMessageInGetFilteredJobLogListWhenNoJobExistWIthGivenId() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.JOB_NOT_FOUND ) );
        final JobEntity expectedEntity = null;
        final FiltersDTO filter = fillFilterForDataTable();

        EasyMock.expect( jobDao.getJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        mockControl.replay();
        jobManagerImpl.getFilteredJobLogList( USER_ID, JOB_ID.toString(), filter );

    }

    /**
     * Should get error message in get filtered job log list when filter are passed as null.
     */
    @Test
    public void shouldGetErrorMessageInGetFilteredJobLogListWhenFilterArePassedAsNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
        final JobEntity expectedEntity = null;
        final FiltersDTO filter = null;

        EasyMock.expect( jobDao.getJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        mockControl.replay();
        jobManagerImpl.getFilteredJobLogList( USER_ID, JOB_ID.toString(), filter );

    }

    /**
     * Should succesfully get filtered job permitted permissions when valid input parameters are given.
     */
    @Test
    public void shouldSuccesfullyGetFilteredJobPermittedPermissionsWhenValidInputParametersAreGiven() {
        fillJobEntity();
        final ManageObjectDTO expectedEntity = fillManageDto();
        final List< ManageObjectDTO > expectedList = new ArrayList<>();
        expectedList.add( expectedEntity );
        final FiltersDTO filter = fillFilterForDataTable();

        EasyMock.expect( permissionManager.prepareObjectManagerDTOs( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyBoolean(), EasyMock.anyObject( FiltersDTO.class ) ) )
                .andReturn( expectedList ).anyTimes();
        EasyMock.expect(
                        susDao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( jobEntity ).anyTimes();
        EasyMock.expect( permissionManager.isManageable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( false ).anyTimes();

        mockControl.replay();
        final FilteredResponse< ManageObjectDTO > actualFilteredData = jobManagerImpl.showPermittedUsersAndGroupsForObject( filter, JOB_ID,
                UUID.fromString( ConstantsID.SUPER_USER_ID ) );

        Assert.assertNotNull( actualFilteredData.getData() );
        final List< ManageObjectDTO > actualData = actualFilteredData.getData();

        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getName(), actualData.get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getType(), actualData.get( FIRST_INDEX ).getType() );
        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getSidId(), actualData.get( FIRST_INDEX ).getSidId() );

    }

    /**
     * Should get error message in get filtered job permitted permissions when filter are passed as null.
     */
    @Test
    public void shouldGetErrorMessageInGetFilteredJobPermittedPermissionsWhenFilterArePassedAsNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
        final JobEntity expectedEntity = null;
        final FiltersDTO filter = null;

        EasyMock.expect( jobDao.getJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        mockControl.replay();
        jobManagerImpl.showPermittedUsersAndGroupsForObject( filter, JOB_ID, UUID.fromString( ConstantsID.SUPER_USER_ID ) );

    }

    /**
     * Should return false when job is not present stop server workflow.
     */
    @Test
    public void shouldReturnFalseWhenJobIsNotPresentStopServerWorkflow() {
        final UUID id = UUID.randomUUID();
        final boolean actual = jobManagerImpl.stopServerWorkFlow( id.toString(), DUMMY_STRING_ABC );
        Assert.assertFalse( actual );
    }

    /**
     * ********************************* Helper Methods ****************************************.
     *
     * @return the latest work flow DTO
     */

    private ManageObjectDTO fillManageDto() {
        final ManageObjectDTO manageDto = new ManageObjectDTO();
        manageDto.setName( DUMMY_STRING_DEF );
        manageDto.setType( DUMMY_STRING_ABC );
        manageDto.setSidId( JOB_ID );

        return manageDto;
    }

    private LatestWorkFlowDTO fillLatestWorkflow() {
        final LatestWorkFlowDTO workflow = new LatestWorkFlowDTO();
        workflow.setId( WORKFLOW_ID );
        workflow.setVersion( new VersionDTO( DUMMY_INT_ONE ) );
        workflow.setName( DUMMY_STRING_ABC );
        return workflow;
    }

    /**
     * Fill workflow.
     *
     * @return the workflow DTO
     */
    private WorkflowDTO fillWorkflow() {
        final WorkflowDTO workflow = new WorkflowDTO();
        workflow.setId( WORKFLOW_ID.toString() );
        workflow.setVersion( new VersionDTO( DUMMY_INT_ONE ) );
        workflow.setName( DUMMY_STRING_ABC );
        return workflow;
    }

    /**
     * A method to populate the DataObject Entity for Expected Result of test.
     *
     * @return projectEntity;
     */

    private DataObjectEntity fillDataObjectEntity() {

        final DataObjectEntity dob = new DataObjectEntity();
        dob.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
        dob.setName( DATA_OBJECT_NAME );
        dob.setTypeId( OBJECT_TYPE_ID );
        dob.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );

        return dob;
    }

    /**
     * Gets the filled columns.
     *
     * @return the filled columns
     */
    private TableUI getFilledColumns() {
        final TableUI tableUI = new TableUI();
        tableUI.setColumns( JsonUtils.jsonToList( TABLE_COLUMN_LIST, TableColumn.class ) );
        return tableUI;
    }

    /**
     * Prepare filter For table.
     *
     * @return the filters DTO
     */
    private FiltersDTO fillFilterForDataTable() {
        final FiltersDTO filtersDTO = new FiltersDTO( ConstantsInteger.INTEGER_VALUE_ONE, ConstantsInteger.INTEGER_VALUE_ZERO,
                ConstantsInteger.INTEGER_VALUE_TEN );
        final FilterColumn filterColumn = new FilterColumn();

        filterColumn.setName( SuSEntity.FIELD_NAME_MODIFIED_ON );
        filterColumn.setDir( ConstantsString.SORTING_DIRECTION_DESCENDING );
        filtersDTO.setColumns( Arrays.asList( filterColumn ) );
        filtersDTO.setFilteredRecords( ( long ) ConstantsInteger.INTEGER_VALUE_TEN );
        filtersDTO.setSearch( ConstantsString.EMPTY_STRING );

        return filtersDTO;
    }

    /**
     * Convert from byte array to log record list.
     *
     * @param bytes
     *         the bytes
     *
     * @return the list
     */
    @SuppressWarnings( "unchecked" )
    private List< LogRecord > convertFromByteArrayToLogRecordList( byte[] bytes ) {

        List< LogRecord > log = null;
        if ( bytes != null ) {

            try ( ObjectInputStream ois = new ObjectInputStream( new ByteArrayInputStream( bytes ) ); ) {

                log = ( List< LogRecord > ) ois.readObject();

            } catch ( final ClassNotFoundException | IOException e ) {
                e.printStackTrace();
            }
        }
        return log;
    }

    /**
     * It converts from log record list to byte array.
     *
     * @param logRecords
     *         log records of a job
     *
     * @return the serialized logs from the records list passed, an empty byte array in case there are no log recorde.
     */
    private byte[] convertFromLogRecordListToByteArray( List< LogRecord > logRecords ) {
        byte[] result = null;
        if ( logRecords != null ) {

            try ( ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream( bos ) ) {

                oos.writeObject( logRecords );
                result = bos.toByteArray();
            } catch ( final IOException e ) {
                ExceptionLogger.logException( e, getClass() );

                result = null;
            }

        }
        return result;
    }

    /**
     * Fill job impl.
     *
     * @return the job impl
     */
    private JobImpl fillJobImpl() {
        jobImpl.setId( JOB_ID );
        jobImpl.setName( DUMMY_JOB_NAME );
        jobImpl.setRunMode( RunMode.getById( DUMMY_INT_ONE ).getKey() );
        jobImpl.setRunsOn( new LocationDTO( JOB_ID, RUN_MODE ) );
        jobImpl.setDescription( DUMMY_STRING_ABC );
        jobImpl.setLogPath( DUMMY_JOB_LOG_FILE_PATH );
        jobImpl.setMachine( MACHINE );
        jobImpl.setSubmitTime( new Date() );
        jobImpl.setCompletionTime( new Date() );
        final ProgressBar progress = new ProgressBar( DUMMY_LONG_FIVE, DUMMY_LONG_FIVE );
        jobImpl.setLog( getLogsList() );

        jobImpl.setProgress( progress );
        jobImpl.setStatus( new Status( DUMMY_INT_ONE, DUMMY_STATUS_NAME ) );
        return jobImpl;
    }

    /**
     * Fill generic dto.
     *
     * @return the generic DTO
     */
    private GenericDTO fillGenericDto() {
        final GenericDTO dto = new GenericDTO();
        dto.setId( OBJ_ID );
        dto.setCreatedOn( NOW_DATE );
        dto.setTypeId( TYPE_ID );
        dto.setName( DUMMY_STRING_ABC );
        return dto;
    }

    /**
     * Gets the status dto.
     *
     * @return the status dto
     */
    private StatusDTO getStatusDto() {
        return new StatusDTO( WIP_LIFECYCLE_STATUS_ID, WIP_LIFECYCLE_STATUS_NAME );
    }

    /**
     * Gets the location manager.
     *
     * @return the location manager
     */
    public LocationManager getLocationManager() {
        return locationManager;
    }

    /**
     * Sets the location manager.
     *
     * @param locationManager
     *         the new location manager
     */
    public void setLocationManager( LocationManager locationManager ) {
        this.locationManager = locationManager;
    }

}
