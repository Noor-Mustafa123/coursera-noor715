package de.soco.software.simuspace.server.service.rest.impl.test;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.server.service.rest.impl.JobServiceImpl;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.RunMode;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;

/**
 * Test Class for Job Manager Impl covering positive and negative test cases
 *
 * @author Nosheen.Sharif
 */
public class JobServiceImplTest {

    /**
     * The Constant mockControl.
     */
    private static final IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant JOB_ID.
     */
    private static final UUID JOB_ID = UUID.randomUUID();

    /**
     * The Constant OBJ_ID.
     */
    private static final UUID OBJ_ID = UUID.randomUUID();

    /**
     * The Constant TYPE_ID.
     */
    private static final UUID TYPE_ID = UUID.randomUUID();

    /**
     * The Constant USER_ID.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The service.
     */
    private JobServiceImpl service;

    /**
     * The manager.
     */
    private JobManager manager;

    /**
     * The object view manger.
     */
    private ObjectViewManager objectViewManger;

    /**
     * Its the job data transfer object.
     */
    private JobImpl jobImpl;

    /**
     * The Constant DUMMY_INT_ONE.
     */
    private static final int DUMMY_INT_ONE = 1;

    /**
     * The Constant DUMMY_LONG_FIVE.
     */
    private static final long DUMMY_LONG_FIVE = 5;

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
     * The dummy machine name for test cases.
     */
    private static final String MACHINE = "clockg.soco.pk";

    /**
     * The Constant DUMMY_JOB_LOG_FILE_PATH.
     */
    private static final String DUMMY_JOB_LOG_FILE_PATH = "/home/scesxxx/abc/logFile.log";

    /**
     * Dummy Error message TYpe.
     */
    private static final String MESSAGE_TYPE_ERROR = "ERROR";

    /**
     * Dummy Filter Json as Input Parameter.
     */
    private static final String FILTER_JSON = "{\"draw\":1,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"description\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"machine\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"status\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3},{\"name\":\"workflowName\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":4},{\"name\":\"createdBy\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":5},{\"name\":\"submitTime\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":6},{\"name\":\"comments\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":7},{\"name\":\"id\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":8}]}";

    /**
     * The Constant VIEW_JSON.
     */
    private static final String VIEW_JSON = "{\"name\":\"Facee\",\"settings\":{\"draw\":1,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"profilePhoto.url\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"uid\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"status\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"restricted\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3},{\"name\":\"directory.name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":4},{\"name\":\"userName\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":5},{\"name\":\"groups.name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":6},{\"name\":\"createdBy\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":7},{\"name\":\"modifiedBy\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":8},{\"name\":\"createdOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":9},{\"name\":\"modifiedOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":10},{\"name\":\"userDetails.designation\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":11},{\"name\":\"userUuid\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":12},{\"name\":\"userDetails.contacts\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":13},{\"name\":\"userDetails.email\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":14},{\"name\":\"userDetails.department\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":15}]}}";

    /**
     * The Constant INVALID_VIEW_JSON.
     */
    private static final String INVALID_VIEW_JSON = "{\"434name\":\"Facee\",\"settings\":{\"draw\":1,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"profilePhoto.url\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"uid\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"status\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"restricted\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3},{\"name\":\"directory.name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":4},{\"name\":\"userName\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":5},{\"name\":\"groups.name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":6},{\"name\":\"createdBy\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":7},{\"name\":\"modifiedBy\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":8},{\"name\":\"createdOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":9},{\"name\":\"modifiedOn\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":10},{\"name\":\"userDetails.designation\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":11},{\"name\":\"userUuid\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":12},{\"name\":\"userDetails.contacts\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":13},{\"name\":\"userDetails.email\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":14},{\"name\":\"userDetails.department\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":15}]}}";

    /**
     * Dummy Filter Json as Input Parameter.
     */
    private static final String INVALID_FILTER_JSON = "{\"22draw\":1,\"start\":0,\"length\":10,\"search\":\"\",\"columns\":[{\"name\":\"name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"description\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"machine\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"status\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3},{\"name\":\"workflowName\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":4},{\"name\":\"createdBy\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":5},{\"name\":\"submitTime\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":6},{\"name\":\"comments\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":7},{\"name\":\"id\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":8}]}";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant FIELD_NAME_MODIFIED_ON.
     */
    public static final String FIELD_NAME_MODIFIED_ON = "modifiedOn";

    /**
     * The Constant FILTERED_RECORDS.
     */
    private static final long FILTERED_RECORDS = 2l;

    /**
     * The Constant NOW.
     */
    private static final Date NOW_DATE = new Date();

    /**
     * The Constant ERROR_MESSAGE.
     */
    private static final String ERROR_MESSAGE = "ERROR";

    /**
     * The Constant ERROR_ENTITY.
     */
    private static final String ERROR_ENTITY = "{\"message\":{\"content\":\"ERROR\",\"type\":\"ERROR\"},\"success\":false,\"expire\":false}";

    /**
     * The Constant NULL_ENTITY_MESSAGE.
     */
    private static final String NULL_ENTITY_MESSAGE = "{\"success\":true,\"expire\":false,\"data\":null}";

    /**
     * The Constant ERROR_JSON_MESSAGE.
     */
    private static final String ERROR_JSON_MESSAGE = "{\"message\":{\"content\":\"Invalid parameters provided.\",\"type\":\"ERROR\"},\"success\":false,\"expire\":false}";

    /**
     * The Constant TABLE_COLUMN_LIST.
     */
    private static final String TABLE_COLUMN_LIST = "[{\"data\":\"name\",\"title\":\"Name\",\"filter\":\"text\",\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"text\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":\"name\",\"manage\":true},\"name\":\"name\",\"orderNum\":0},{\"data\":\"type\",\"title\":\"Type\",\"filter\":\"text\",\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"text\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":\"type\",\"manage\":true},\"name\":\"type\",\"orderNum\":0},{\"data\":\"permissionDTOs.0.value\",\"title\":\"View\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.0.manage\"},\"name\":\"permissionDTOs-View\",\"orderNum\":0},{\"data\":\"permissionDTOs.1.value\",\"title\":\"ViewAuditLog\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.1.manage\"},\"name\":\"permissionDTOs-ViewAuditLog\",\"orderNum\":0},{\"data\":\"permissionDTOs.2.value\",\"title\":\"Read\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.2.manage\"},\"name\":\"permissionDTOs-Read\",\"orderNum\":0},{\"data\":\"permissionDTOs.3.value\",\"title\":\"Write\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.3.manage\"},\"name\":\"permissionDTOs-Write\",\"orderNum\":0},{\"data\":\"permissionDTOs.4.value\",\"title\":\"Execute\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.4.manage\"},\"name\":\"permissionDTOs-Execute\",\"orderNum\":0},{\"data\":\"permissionDTOs.5.value\",\"title\":\"Export\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.5.manage\"},\"name\":\"permissionDTOs-Export\",\"orderNum\":0},{\"data\":\"permissionDTOs.6.value\",\"title\":\"Delete\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.6.manage\"},\"name\":\"permissionDTOs-Delete\",\"orderNum\":0},{\"data\":\"permissionDTOs.7.value\",\"title\":\"Restore\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.7.manage\"},\"name\":\"permissionDTOs-Restore\",\"orderNum\":0},{\"data\":\"permissionDTOs.8.value\",\"title\":\"CreateNewObject\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.8.manage\"},\"name\":\"permissionDTOs-CreateNewObject\",\"orderNum\":0},{\"data\":\"permissionDTOs.9.value\",\"title\":\"CreateNewVersion\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.9.manage\"},\"name\":\"permissionDTOs-CreateNewVersion\",\"orderNum\":0},{\"data\":\"permissionDTOs.10.value\",\"title\":\"Update\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.10.manage\"},\"name\":\"permissionDTOs-Update\",\"orderNum\":0},{\"data\":\"permissionDTOs.11.value\",\"title\":\"Kill\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.11.manage\"},\"name\":\"permissionDTOs-Kill\",\"orderNum\":0},{\"data\":\"permissionDTOs.12.value\",\"title\":\"Share\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.12.manage\"},\"name\":\"permissionDTOs-Share\",\"orderNum\":0}]";

    /**
     * The Constant VIEW_ID.
     */
    private static final String VIEW_ID = UUID.randomUUID().toString();

    /**
     * The Constant ERROR_VIEW_JSON.
     */
    private static final String ERROR_VIEW_JSON = "{\"message\":{\"content\":\"View set as default successfully.\",\"type\":\"SUCCESS\"},\"success\":true,\"expire\":false,\"data\":null}";

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl().resetToNice();
        jobImpl = new JobImpl();
        manager = mockControl().createMock( JobManager.class );
        objectViewManger = mockControl().createMock( ObjectViewManager.class );
        service = new JobServiceImpl();
        service.setJobManager( manager );
        service.setObjectViewManager( objectViewManger );
    }

    /**
     * To clear the objects and mocking objects.
     */
    @After
    public void tearDown() {
        jobImpl = null;
        service = null;
        manager = null;
    }

    /**
     * Should successfully get job with given valid job id.
     */
    @Test
    public void shouldSuccessfullyGetJobWithGivenValidJobId() {

        final JobImpl expected = fillJobImpl();
        EasyMock.expect( manager.getJob( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( String.class ) ) ).andReturn( expected )
                .anyTimes();
        mockControl().replay();
        final Response actual = service.getJob( JOB_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String jobObject = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final JobImpl jobDto = JsonUtils.jsonToObject( jobObject, JobImpl.class );
        assertDataCheck( expected, jobDto );

    }

    /**
     * Should throw exception in get job when managerthrow sus exception.
     */
    @Test
    public void shouldThrowExceptionInGetJobWhenManagerthrowSusException() {

        fillJobImpl();
        EasyMock.expect( manager.getJob( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( String.class ) ) )
                .andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl().replay();
        final Response actual = service.getJob( JOB_ID );
        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );

    }

    /**
     * Should not get job object in response when in valid job id is given.
     */
    @Test
    public void shouldNotGetJobObjectInResponseWhenInValidJobIdIsGiven() {

        final JobImpl expected = null;
        EasyMock.expect( manager.getJob( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( String.class ) ) ).andReturn( expected )
                .anyTimes();
        mockControl().replay();
        final Response actual = service.getJob( UUID.randomUUID() );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNull( actualResponse.getData() );

    }

    /**
     * Should successfully get tabs view UI when valid job id is given.
     */
    @Test
    public void shouldSuccessfullyGetTabsViewUIWhenValidJobIdIsGiven() {
        final List< SubTabsUI > expected = getGeneralTabsList();
        final SubTabsItem expectedObject = new SubTabsItem();
        expectedObject.setTabs( expected );

        EasyMock.expect( manager.getTabsViewJobUI( EasyMock.anyString() ) ).andReturn( expectedObject ).anyTimes();
        mockControl().replay();
        final Response actual = service.getTabsViewJobUI( JOB_ID.toString() );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String actualTabsList = JsonUtils.toJsonString( actualResponse.getData() );
        final SubTabsItem actualtabs = JsonUtils.jsonToObject( actualTabsList, SubTabsItem.class );
        Assert.assertEquals( expectedObject.getTabs(), actualtabs.getTabs() );
    }

    /**
     * Should throw exception in get tabs view UI when managerthrow sus exception.
     */
    @Test
    public void shouldThrowExceptionInGetTabsViewUIWhenManagerthrowSusException() {

        EasyMock.expect( manager.getTabsViewJobUI( EasyMock.anyString() ) ).andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl().replay();
        final Response actual = service.getTabsViewJobUI( JOB_ID.toString() );
        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );

    }

    /**
     * Should successfully get table columns list when valid job class parameter is passed.
     */
    @Test
    public void shouldSuccessfullyGetTableColumnsListWhenValidJobClassParameterIsPassed() {
        final List< TableColumn > expected = GUIUtils.listColumns( JobImpl.class );

        final Response actual = service.getSingleJobPropertiesUI( JOB_ID.toString() );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String actualTabsList = JsonUtils.toJsonString( actualResponse.getData() );
        final List< TableColumn > actualtableCol = JsonUtils.jsonToList( actualTabsList, TableColumn.class );

        for ( final TableColumn actualTableColumn : actualtableCol ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }

        }
    }

    /**
     * Should successfully get job log UI with valid class argument.
     */
    @Test
    public void shouldSuccessfullyGetJobLogUIWithValidClassArgument() {

        final List< TableColumn > expected = getJobLogTableColumns();
        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( new ArrayList<>() ).anyTimes();

        mockControl().replay();
        final Response actual = service.getJobLogUI( JOB_ID.toString(), JOB_ID.toString() );
        Assert.assertNotNull( actual );

        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
        final TableUI actualtableUI = JsonUtils.jsonToObject( actualTable, TableUI.class );

        for ( final TableColumn actualTableColumn : actualtableUI.getColumns() ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }
        }

    }

    /**
     * Should throw exception in get job log when manager Throw sus exception.
     */
    @Test
    public void shouldThrowExceptionInGetJobLogWhenManagerThrowSusException() {

        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl().replay();
        final Response actual = service.getJobLogUI( JOB_ID.toString(), JOB_ID.toString() );
        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );

    }

    /**
     * Should successfully get job log UI with valid class argument.
     */
    @Test
    public void shouldSuccessfullyGetJobDataCreatedUIWithValidClassArgument() {

        final List< TableColumn > expected = getGenericTableColumns();
        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect( manager.getJobDataCreatedTableUI() ).andReturn( expected ).anyTimes();
        mockControl().replay();
        final Response actual = service.getJobDataCreatedJobUI( JOB_ID.toString() );
        Assert.assertNotNull( actual );

        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
        final TableUI actualtableUI = JsonUtils.jsonToObject( actualTable, TableUI.class );

        for ( final TableColumn actualTableColumn : actualtableUI.getColumns() ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }
        }

    }

    /**
     * Should successfully get su S jobs table UI with valid class argument.
     */
    @Test
    public void shouldSuccessfullyGetSuSJobsTableUIWithValidClassArgument() {

        final List< TableColumn > expected = getJobUITableColumns();
        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect( manager.getJobDataCreatedTableUI() ).andReturn( expected ).anyTimes();
        mockControl().replay();
        final Response actual = service.listSusJobTableUI();
        Assert.assertNotNull( actual );

        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
        final TableUI actualtableUI = JsonUtils.jsonToObject( actualTable, TableUI.class );

        for ( final TableColumn actualTableColumn : actualtableUI.getColumns() ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }
        }

    }

    /**
     * Should throw exception in get job data created when manager throw sus exception.
     */
    @Test
    public void shouldThrowExceptionInGetJobDataCreatedWhenManagerThrowSusException() {

        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl().replay();
        final Response actual = service.getJobDataCreatedJobUI( JOB_ID.toString() );
        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );

    }

    /**
     * Should throw exception in get sus jobs table UI when manager throw sus exception.
     */
    @Test
    public void shouldThrowExceptionInGetSusJobsTableUIWhenManagerThrowSusException() {

        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl().replay();
        final Response actual = service.listSusJobTableUI();
        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );

    }

    /**
     * Should successfully get job table UI with valid class argument.
     */
    @Test
    public void shouldSuccessfullyGetJobTableUIWithValidClassArgument() {

        final List< TableColumn > expected = getJobUITableColumns();
        final TableUI tableUI = new TableUI( expected );
        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( new ArrayList<>() ).anyTimes();
        EasyMock.expect( manager.getListOfJobUITableColumns() ).andReturn( tableUI ).anyTimes();
        mockControl().replay();
        final Response actual = service.listJobTableUI();
        Assert.assertNotNull( actual );

        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
        final TableUI actualtableUI = JsonUtils.jsonToObject( actualTable, TableUI.class );

        for ( final TableColumn actualTableColumn : actualtableUI.getColumns() ) {
            for ( final TableColumn expectedTableColumn : expected ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }
        }

    }

    /**
     * Should throw exception in list job log UI when manager throw sus exception.
     */
    @Test
    public void shouldThrowExceptionInListJobLogUIWhenManagerThrowSusException() {

        EasyMock.expect( manager.getListOfJobUITableColumns() ).andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl().replay();
        final Response actual = service.listJobTableUI();
        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );

    }

    /**
     * Should success fully get filtered job response when valid filter json is given.
     */
    @Test
    public void shouldSuccessFullyGetFilteredJobResponseWhenValidFilterJsonIsGiven() {
        final List< Job > expectedList = new ArrayList<>();
        final Job expectedDto = fillJob();
        expectedList.add( expectedDto );

        final FiltersDTO filtersDTO = fillFilterDTO();
        PaginationUtil.constructFilteredResponse( filtersDTO, Arrays.asList( expectedDto ) );

        final FilteredResponse< Job > expected = getFilledFilteredResponse( expectedList );

        EasyMock.expect( manager.getFilteredJobsList( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ) ) )
                .andReturn( expected ).anyTimes();
        mockControl().replay();

        final Response actual = service.getFilteredJobsList( FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final FilteredResponse< JobImpl > actualDto = JsonUtils.jsonToObject( dataObjDto, FilteredResponse.class );
        final List< JobImpl > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( actualDto.getData() ), JobImpl.class );
        Assert.assertNotNull( actualList );
        final JobImpl actualObject = ( JobImpl ) expected.getData().get( FIRST_INDEX );
        assertDataCheck( expectedDto, actualObject );

    }

    /**
     * Should get bad request status when invalid filter json is given to get filtered jobs list.
     */
    @Test
    public void shouldGetBadRequestStatusWhenInvalidFilterJsonIsGivenToGetFilteredJobsList() {

        final Response actual = service.getFilteredJobsList( INVALID_FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );
        JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
    }

    /**
     * Should get bad request status when invalid filter json is given to get filtered jobs logs list.
     */
    @Test
    public void shouldGetBadRequestStatusWhenInvalidFilterJsonIsGivenToGetFilteredJobsLogsList() {

        final Response actual = service.getFilteredJobLogsList( JOB_ID.toString(), JOB_ID.toString(), INVALID_FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );
        JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
    }

    /**
     * Should get bad request status when invalid filter json is given to get filtered job data created list.
     */
    @Test
    public void shouldGetBadRequestStatusWhenInvalidFilterJsonIsGivenToGetFilteredJobDataCreatedList() {

        final Response actual = service.getFilteredJobDataCreatedObjectsList( JOB_ID.toString(), INVALID_FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );
        JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
    }

    /**
     * Should success fully get filtered job log response when valid filter json is given.
     */
    @Test
    public void shouldSuccessFullyGetFilteredJobLogResponseWhenValidFilterJsonIsGiven() {
        final List< LogRecord > expectedList = new ArrayList<>();
        final LogRecord expectedDto = fillLogRecord();
        expectedList.add( expectedDto );

        final FiltersDTO filtersDTO = fillFilterDTO();
        PaginationUtil.constructFilteredResponse( filtersDTO, Arrays.asList( expectedDto ) );

        final FilteredResponse< LogRecord > expected = getFilledLogRecordFilteredResponse( expectedList );

        EasyMock.expect( manager.getFilteredJobLogList( EasyMock.anyObject( UUID.class ), EasyMock.anyString(),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expected ).anyTimes();
        mockControl().replay();

        final Response actual = service.getFilteredJobLogsList( JOB_ID.toString(), JOB_ID.toString(), FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final FilteredResponse< JobImpl > actualDto = JsonUtils.jsonToObject( dataObjDto, FilteredResponse.class );
        final List< JobImpl > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( actualDto.getData() ), JobImpl.class );
        Assert.assertNotNull( actualList );
        final LogRecord actualObject = expected.getData().get( FIRST_INDEX );
        Assert.assertEquals( actualObject.getDate(), expectedDto.getDate() );
        Assert.assertEquals( actualObject.getType(), expectedDto.getType() );
        Assert.assertEquals( actualObject.getLogMessage(), expectedDto.getLogMessage() );

    }

    /**
     * Should success fully get sus job log response when valid machine name is given.
     */
    @Test
    public void shouldSuccessFullyGetSusJobLogResponseWhenValidMachineNameIsGiven() {
        final List< Job > expectedList = new ArrayList<>();
        expectedList.add( fillJobImpl() );
        final FilteredResponse< Job > list = PaginationUtil.constructFilteredResponse( populateFilterDTO(), expectedList );

        EasyMock.expect( manager.getMySusJobsList( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ) ) )
                .andReturn( list ).anyTimes();
        mockControl().replay();

        final Response actual = service.getMySusJobsList( JsonUtils.toJson( populateFilterDTO() ) );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final FilteredResponse< JobImpl > actualDto = JsonUtils.jsonToObject( dataObjDto, FilteredResponse.class );

        final List< JobImpl > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( actualDto.getData() ), JobImpl.class );
        Assert.assertNotNull( actualList );
        final Job expectedObject = expectedList.get( FIRST_INDEX );
        Assert.assertEquals( expectedObject.getId(), actualList.get( FIRST_INDEX ).getId() );
        Assert.assertEquals( expectedObject.getName(), actualList.get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedObject.getDescription(), actualList.get( FIRST_INDEX ).getDescription() );

    }

    /**
     * Should throw exception in getting sus jobs list when manager throw exception.
     */
    @Test
    public void shouldThrowExceptionInGettingSusJobsListWhenManagerThrowException() {
        EasyMock.expect( manager.getMySusJobsList( EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ) ) )
                .andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl().replay();

        final Response actual = service.getMySusJobsList( JsonUtils.toJson( populateFilterDTO() ) );
        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );
    }

    /**
     * Should success fully get filtered job data created response when valid filter json is given.
     */
    @Test
    public void shouldSuccessFullyGetFilteredJobDataCreatedResponseWhenValidFilterJsonIsGiven() {
        final List< GenericDTO > expectedList = new ArrayList<>();
        final GenericDTO expectedDto = fillGenericDto();
        expectedList.add( expectedDto );

        final FiltersDTO filtersDTO = fillFilterDTO();
        PaginationUtil.constructFilteredResponse( filtersDTO, Arrays.asList( expectedDto ) );

        final FilteredResponse< GenericDTO > expected = getFilledGenericFilteredResponse( expectedList );

        EasyMock.expect( manager.getFilteredJobDataCreatedObjectsList( EasyMock.anyObject( UUID.class ), EasyMock.anyString(),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( expected ).anyTimes();
        mockControl().replay();

        final Response actual = service.getFilteredJobDataCreatedObjectsList( JOB_ID.toString(), FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final FilteredResponse< GenericDTO > actualDto = JsonUtils.jsonToObject( dataObjDto, FilteredResponse.class );
        final List< GenericDTO > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( actualDto.getData() ), GenericDTO.class );
        Assert.assertNotNull( actualList );
        final GenericDTO actualObject = actualList.get( FIRST_INDEX );
        Assert.assertEquals( actualObject.getId(), expectedDto.getId() );
        Assert.assertEquals( actualObject.getTypeId(), expectedDto.getTypeId() );
        Assert.assertEquals( actualObject.getName(), expectedDto.getName() );

    }

    /**
     * Should successfully get job permission table UI with valid class argument.
     */
    @Test
    public void shouldSuccessfullyGetJobPermissionTableUIWithValidClassArgument() {

        final TableUI expectedUI = getFilledColumns();
        EasyMock.expect( manager.getJobPermissionUITable( EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedUI ).anyTimes();
        mockControl().replay();
        final Response actual = service.getJobPermissionTableUI( JOB_ID.toString() );
        Assert.assertNotNull( actual );

        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String actualTable = JsonUtils.toJsonString( actualResponse.getData() );
        final TableUI actualtableUI = JsonUtils.jsonToObject( actualTable, TableUI.class );

        for ( final TableColumn actualTableColumn : actualtableUI.getColumns() ) {
            for ( final TableColumn expectedTableColumn : expectedUI.getColumns() ) {
                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
                }
            }
        }

    }

    /**
     * Should throw exception in get job permssion table UI log UI when manager throw sus exception.
     */
    @Test
    public void shouldThrowExceptionInGetJobPermssionTableUILogUIWhenManagerThrowSusException() {

        EasyMock.expect( manager.getJobPermissionUITable( EasyMock.anyObject( UUID.class ) ) )
                .andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl().replay();
        final Response actual = service.getJobPermissionTableUI( JOB_ID.toString() );
        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );

    }

    /**
     * Should success fully get filtered permission response when valid filter json is given.
     */
    @Test
    public void shouldSuccessFullyGetFilteredPermissionResponseWhenValidFilterJsonIsGiven() {
        final List< ManageObjectDTO > expectedList = new ArrayList<>();
        final ManageObjectDTO expectedDto = fillManageDto();
        expectedList.add( new ManageObjectDTO() );

        final FiltersDTO filtersDTO = fillFilterDTO();
        PaginationUtil.constructFilteredResponse( filtersDTO, Arrays.asList( expectedDto ) );

        final FilteredResponse< ManageObjectDTO > expected = getFilledManageDtoFilteredResponse( expectedList );

        EasyMock.expect( manager.showPermittedUsersAndGroupsForObject( EasyMock.anyObject( FiltersDTO.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
        mockControl().replay();

        final Response actual = service.showPermittedUsersAndGroupsForJob( JOB_ID, FILTER_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final FilteredResponse< ManageObjectDTO > actualDto = JsonUtils.jsonToObject( dataObjDto, FilteredResponse.class );
        final List< ManageObjectDTO > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( actualDto.getData() ),
                ManageObjectDTO.class );
        Assert.assertFalse( actualList.isEmpty() );
        Assert.assertEquals( expectedDto.getName(), actualList.get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedDto.getType(), actualList.get( FIRST_INDEX ).getType() );
        Assert.assertEquals( expectedDto.getSidId(), actualList.get( FIRST_INDEX ).getSidId() );

    }

    /**
     * Should throw exception in get job permssion table UI log UI when manager throw sus exception.
     */
    @Test
    public void shouldThrowExceptionInShowPermittedUsersAndGroupsWhenManagerThrowSusException() {

        EasyMock.expect( manager.showPermittedUsersAndGroupsForObject( EasyMock.anyObject( FiltersDTO.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) ).andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl().replay();
        final Response actual = service.showPermittedUsersAndGroupsForJob( JOB_ID, FILTER_JSON );
        Assert.assertEquals( ERROR_ENTITY, actual.getEntity() );

    }

    /**
     * Should successfully save view of job table when valid view json.
     */
    @Test
    public void shouldSuccessfullySaveViewOfJobTableWhenValidViewJson() {
        final ObjectViewDTO expected = getObjectViewDto();
        EasyMock.expect( objectViewManger.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl().replay();
        final Response actual = service.saveView( VIEW_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                actualResponse.getMessage().getContent() );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final ObjectViewDTO actualDto = JsonUtils.jsonToObject( dataObjDto, ObjectViewDTO.class );

        Assert.assertEquals( expected.getObjectId(), actualDto.getObjectId() );
        Assert.assertEquals( expected.getObjectViewJson(), actualDto.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), actualDto.getObjectViewKey() );

        Assert.assertEquals( expected.getName(), actualDto.getName() );
        Assert.assertEquals( expected.getObjectViewType(), actualDto.getObjectViewType() );
        Assert.assertEquals( expected.getId(), actualDto.getId() );

    }

    /**
     * Should throw error message in save view when invalid view json is given.
     */
    @Test
    public void shouldThrowErrorMessageInSaveViewWhenInvalidViewJsonIsGiven() {

        EasyMock.expect( objectViewManger.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyString() ) )
                .andThrow( new SusException( ERROR_JSON_MESSAGE ) ).anyTimes();
        final Response actual = service.saveView( INVALID_VIEW_JSON );
        Assert.assertEquals( ERROR_JSON_MESSAGE, actual.getEntity() );

    }

    /**
     * Should get null data in response in get view when manager throw exception.
     */
    @Test
    public void shouldGetNullDataInResponseInGetViewWhenManagerThrowException() {

        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andThrow( new SusException( ERROR_MESSAGE ) ).anyTimes();
        final Response actual = service.getAllViews();
        Assert.assertEquals( NULL_ENTITY_MESSAGE, actual.getEntity() );

    }

    /**
     * Should successfully get view of job table when valid view json.
     */
    @Test
    public void shouldSuccessfullyGetViewOfJobTableWhenValidViewJson() {

        final ObjectViewDTO expected = getObjectViewDto();
        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( Arrays.asList( expected ) ).anyTimes();
        mockControl().replay();
        final Response actual = service.getAllViews();
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );

        Assert.assertNotNull( actualResponse.getData() );

        final List< ObjectViewDTO > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( actualResponse.getData() ),
                ObjectViewDTO.class );
        final ObjectViewDTO actualDto = actualList.get( FIRST_INDEX );

        Assert.assertEquals( expected.getObjectId(), actualDto.getObjectId() );
        Assert.assertEquals( expected.getObjectViewJson(), actualDto.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), actualDto.getObjectViewKey() );
        Assert.assertEquals( expected.getName(), actualDto.getName() );
        Assert.assertEquals( expected.getObjectViewType(), actualDto.getObjectViewType() );
        Assert.assertEquals( expected.getId(), actualDto.getId() );

    }

    /**
     * Should successfully get view of sus job table when valid view json.
     */
    @Test
    public void shouldSuccessfullyGetViewOfSusJobTableWhenValidViewJson() {

        final ObjectViewDTO expected = getObjectViewDto();
        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( Arrays.asList( expected ) ).anyTimes();
        mockControl().replay();
        final Response actual = service.getSuSJobsViews();
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );

        Assert.assertNotNull( actualResponse.getData() );

        final List< ObjectViewDTO > actualList = JsonUtils.jsonToList( JsonUtils.toJsonString( actualResponse.getData() ),
                ObjectViewDTO.class );
        final ObjectViewDTO actualDto = actualList.get( FIRST_INDEX );

        Assert.assertEquals( expected.getObjectId(), actualDto.getObjectId() );
        Assert.assertEquals( expected.getObjectViewJson(), actualDto.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), actualDto.getObjectViewKey() );
        Assert.assertEquals( expected.getName(), actualDto.getName() );
        Assert.assertEquals( expected.getObjectViewType(), actualDto.getObjectViewType() );
        Assert.assertEquals( expected.getId(), actualDto.getId() );

    }

    /**
     * Should get null data in response in get sus jobs view when manager throw exception.
     */
    @Test
    public void shouldGetNullDataInResponseInGetSusJobsViewWhenManagerThrowException() {

        EasyMock.expect( objectViewManger.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andThrow( new SusException( ERROR_MESSAGE ) ).anyTimes();
        final Response actual = service.getSuSJobsViews();
        Assert.assertEquals( NULL_ENTITY_MESSAGE, actual.getEntity() );

    }

    /**
     * Should successfully save view of sus job table when valid view json.
     */
    @Test
    public void shouldSuccessfullySaveViewOfSuSJobTableWhenValidViewJson() {
        final ObjectViewDTO expected = getObjectViewDto();
        EasyMock.expect( objectViewManger.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl().replay();
        final Response actual = service.saveSuSJobsView( VIEW_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                actualResponse.getMessage().getContent() );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final ObjectViewDTO actualDto = JsonUtils.jsonToObject( dataObjDto, ObjectViewDTO.class );

        Assert.assertEquals( expected.getObjectId(), actualDto.getObjectId() );
        Assert.assertEquals( expected.getObjectViewJson(), actualDto.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), actualDto.getObjectViewKey() );

        Assert.assertEquals( expected.getName(), actualDto.getName() );
        Assert.assertEquals( expected.getObjectViewType(), actualDto.getObjectViewType() );
        Assert.assertEquals( expected.getId(), actualDto.getId() );

    }

    /**
     * Should throw error message in save su S view when invalid view json is given.
     */
    @Test
    public void shouldThrowErrorMessageInSaveSuSViewWhenInvalidViewJsonIsGiven() {

        EasyMock.expect( objectViewManger.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyString() ) )
                .andThrow( new SusException( ERROR_JSON_MESSAGE ) ).anyTimes();
        final Response actual = service.saveSuSJobsView( INVALID_VIEW_JSON );
        Assert.assertEquals( ERROR_JSON_MESSAGE, actual.getEntity() );

    }

    /**
     * Should successfully update view of su S job table when valid view json.
     */
    @Test
    public void shouldSuccessfullyUpdateViewOfSuSJobTableWhenValidViewJson() {
        final ObjectViewDTO expected = getObjectViewDto();
        EasyMock.expect( objectViewManger.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl().replay();
        final Response actual = service.updateSuSJobsView( VIEW_ID, VIEW_JSON );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                actualResponse.getMessage().getContent() );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final ObjectViewDTO actualDto = JsonUtils.jsonToObject( dataObjDto, ObjectViewDTO.class );

        Assert.assertEquals( expected.getObjectId(), actualDto.getObjectId() );
        Assert.assertEquals( expected.getObjectViewJson(), actualDto.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), actualDto.getObjectViewKey() );

        Assert.assertEquals( expected.getName(), actualDto.getName() );
        Assert.assertEquals( expected.getObjectViewType(), actualDto.getObjectViewType() );
        Assert.assertEquals( expected.getId(), actualDto.getId() );

    }

    /**
     * Should throw error message in update su S view when invalid view json is given.
     */
    @Test
    public void shouldThrowErrorMessageInUpdateSuSViewWhenInvalidViewJsonIsGiven() {

        EasyMock.expect( objectViewManger.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyString() ) )
                .andThrow( new SusException( ERROR_JSON_MESSAGE ) ).anyTimes();
        final Response actual = service.updateSuSJobsView( VIEW_ID, INVALID_VIEW_JSON );
        Assert.assertEquals( ERROR_JSON_MESSAGE, actual.getEntity() );

    }

    /**
     * Should successfully save default view of su S job table when valid view json.
     */
    @Test
    public void shouldSuccessfullySaveDefaultViewOfSuSJobTableWhenValidViewJson() {
        final ObjectViewDTO expected = getObjectViewDto();
        EasyMock.expect( objectViewManger.saveDefaultObjectView( EasyMock.anyObject( UUID.class ), EasyMock.anyString(),
                EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( expected ).anyTimes();
        mockControl().replay();
        final Response actual = service.setSusJobsViewAsDefault( VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        final SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                actualResponse.getMessage().getContent() );
        Assert.assertNotNull( actualResponse.getData() );
        final String dataObjDto = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        final ObjectViewDTO actualDto = JsonUtils.jsonToObject( dataObjDto, ObjectViewDTO.class );

        Assert.assertEquals( expected.getObjectId(), actualDto.getObjectId() );
        Assert.assertEquals( expected.getObjectViewJson(), actualDto.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), actualDto.getObjectViewKey() );

        Assert.assertEquals( expected.getName(), actualDto.getName() );
        Assert.assertEquals( expected.getObjectViewType(), actualDto.getObjectViewType() );
        Assert.assertEquals( expected.getId(), actualDto.getId() );

    }

    /**
     * Should throw error message in set su S view as default when invalid view json is given.
     */
    @Test
    public void shouldThrowErrorMessageInSetSuSViewAsDefaultWhenInvalidViewJsonIsGiven() {

        EasyMock.expect( objectViewManger.saveDefaultObjectView( EasyMock.anyObject( UUID.class ), EasyMock.anyString(),
                EasyMock.anyString(), EasyMock.anyString() ) ).andThrow( new SusException( ERROR_JSON_MESSAGE ) ).anyTimes();
        final Response actual = service.setSusJobsViewAsDefault( VIEW_ID );
        Assert.assertEquals( ERROR_VIEW_JSON, actual.getEntity() );

    }

    /**
     * ***************** Data Assert Check******************************
     *
     * @param expected
     *         the expected
     * @param actual
     *         the actual
     */
    private void assertDataCheck( Job expected, Job actual ) {
        Assert.assertEquals( expected.getId(), actual.getId() );
        Assert.assertEquals( expected.getName(), actual.getName() );
        Assert.assertEquals( expected.getDescription(), actual.getDescription() );
        Assert.assertEquals( expected.getMachine(), actual.getMachine() );
        Assert.assertEquals( expected.getRunMode(), actual.getRunMode() );
        Assert.assertEquals( expected.getProgress().getTotal(), actual.getProgress().getTotal() );
        Assert.assertEquals( expected.getProgress().getCompleted(), actual.getProgress().getCompleted() );
    }

    /**
     * ************************ Helper Methods ******************************.
     *
     * /** Gets the filled columns.
     *
     * @return the filled columns
     */
    private TableUI getFilledColumns() {
        final TableUI tableUI = new TableUI();
        tableUI.setColumns( JsonUtils.jsonToList( TABLE_COLUMN_LIST, TableColumn.class ) );
        return tableUI;
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
     * Gets the object view dto.
     *
     * @return the object view dto
     */
    private ObjectViewDTO getObjectViewDto() {
        return JsonUtils.jsonToObject( VIEW_JSON, ObjectViewDTO.class );
    }

    /**
     * @return the log record
     */

    private LogRecord fillLogRecord() {
        return new LogRecord( MESSAGE_TYPE_ERROR, DUMMY_STRING_ABC, NOW_DATE );

    }

    private ManageObjectDTO fillManageDto() {
        return new ManageObjectDTO();
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
     * Gets the job log table columns.
     *
     * @return the job log table columns
     */
    private List< TableColumn > getJobLogTableColumns() {
        return GUIUtils.listColumns( LogRecord.class );
    }

    /**
     * Gets the generic table columns.
     *
     * @return the generic table columns
     */
    private List< TableColumn > getGenericTableColumns() {
        return GUIUtils.listColumns( GenericDTO.class );
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
        jobImpl.setDescription( DUMMY_STRING_ABC );
        jobImpl.setLogPath( DUMMY_JOB_LOG_FILE_PATH );
        jobImpl.setMachine( MACHINE );
        jobImpl.setSubmitTime( new Date() );
        jobImpl.setCompletionTime( new Date() );
        final ProgressBar progress = new ProgressBar( DUMMY_LONG_FIVE, DUMMY_LONG_FIVE );

        jobImpl.setProgress( progress );

        return jobImpl;
    }

    /**
     * Fill job.
     *
     * @return the job
     */
    private Job fillJob() {
        final Job job = new JobImpl();
        job.setId( JOB_ID );
        job.setName( DUMMY_JOB_NAME );
        job.setRunMode( RunMode.getById( DUMMY_INT_ONE ).getKey() );
        job.setDescription( DUMMY_STRING_ABC );
        job.setLogPath( DUMMY_JOB_LOG_FILE_PATH );
        job.setMachine( MACHINE );
        job.setSubmitTime( new Date() );
        job.setCompletionTime( new Date() );
        final ProgressBar progress = new ProgressBar( DUMMY_LONG_FIVE, DUMMY_LONG_FIVE );

        job.setProgress( progress );

        return job;
    }

    /**
     * Prepare filter for max ten number of children.
     *
     * @return the filters DTO
     */
    private FiltersDTO fillFilterDTO() {
        final FiltersDTO filtersDTO = new FiltersDTO( ConstantsInteger.INTEGER_VALUE_ONE, ConstantsInteger.INTEGER_VALUE_ZERO,
                ConstantsInteger.INTEGER_VALUE_ZERO, FILTERED_RECORDS );
        final FilterColumn filterColumn = new FilterColumn();
        filterColumn.setName( FIELD_NAME_MODIFIED_ON );
        filterColumn.setDir( ConstantsString.SORTING_DIRECTION_DESCENDING );
        filtersDTO.setColumns( Arrays.asList( filterColumn ) );

        return filtersDTO;
    }

    /**
     * Get Filled Filtered Response.
     *
     * @param list
     *         the list
     *
     * @return FilteredResponse
     */
    private FilteredResponse< Job > getFilledFilteredResponse( List< Job > list ) {
        final FiltersDTO filter = new FiltersDTO();
        filter.setDraw( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setLength( ConstantsInteger.INTEGER_VALUE_TWO );
        filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setFilteredRecords( Long.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ) );

        final FilteredResponse< Job > expectedResponse = PaginationUtil.constructFilteredResponse( filter, list );
        return expectedResponse;
    }

    /**
     * Get Filled Filtered Response.
     *
     * @param list
     *         the list
     *
     * @return FilteredResponse
     */
    private FilteredResponse< LogRecord > getFilledLogRecordFilteredResponse( List< LogRecord > list ) {
        final FiltersDTO filter = new FiltersDTO();
        filter.setDraw( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setLength( ConstantsInteger.INTEGER_VALUE_TWO );
        filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setFilteredRecords( Long.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ) );

        final FilteredResponse< LogRecord > expectedResponse = PaginationUtil.constructFilteredResponse( filter, list );
        return expectedResponse;
    }

    /**
     * Gets the filled manage dto filtered response.
     *
     * @param list
     *         the list
     *
     * @return the filled manage dto filtered response
     */
    private FilteredResponse< ManageObjectDTO > getFilledManageDtoFilteredResponse( List< ManageObjectDTO > list ) {
        final FiltersDTO filter = new FiltersDTO();
        filter.setDraw( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setLength( ConstantsInteger.INTEGER_VALUE_TWO );
        filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setFilteredRecords( Long.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ) );

        final FilteredResponse< ManageObjectDTO > expectedResponse = PaginationUtil.constructFilteredResponse( filter, list );
        return expectedResponse;
    }

    /**
     * Gets the filled generic filtered response.
     *
     * @param list
     *         the list
     *
     * @return the filled generic filtered response
     */
    private FilteredResponse< GenericDTO > getFilledGenericFilteredResponse( List< GenericDTO > list ) {
        final FiltersDTO filter = new FiltersDTO();
        filter.setDraw( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setLength( ConstantsInteger.INTEGER_VALUE_TWO );
        filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setFilteredRecords( Long.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ) );

        final FilteredResponse< GenericDTO > expectedResponse = PaginationUtil.constructFilteredResponse( filter, list );
        return expectedResponse;
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
     * Populates filter DTO.
     *
     * @return the filters DTO
     */
    private FiltersDTO populateFilterDTO() {
        final FiltersDTO filtersDTO = new FiltersDTO();
        filtersDTO.setDraw( ConstantsInteger.INTEGER_VALUE_ONE );
        filtersDTO.setLength( ConstantsInteger.INTEGER_VALUE_ONE );
        filtersDTO.setStart( ConstantsInteger.INTEGER_VALUE_ONE );
        filtersDTO.setFilteredRecords( 1L );
        return filtersDTO;
    }

}
