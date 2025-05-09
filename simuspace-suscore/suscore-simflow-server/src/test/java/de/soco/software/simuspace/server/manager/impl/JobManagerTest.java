package de.soco.software.simuspace.server.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.internal.WhiteboxImpl;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.data.common.dao.JobDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;

/**
 * This class is to test the job manager implementation class methods.
 *
 * @author sces126
 */
@PrepareForTest( JobManagerImpl.class )
public class JobManagerTest {

    /**
     * The Constant DUMMY_STRING.
     */
    private static final String DUMMY_STRING = "abc";

    /**
     * The Constant DUMMY_STRING.
     */
    private static final String DUMMY_STRING_MESSAGE = "def";

    /**
     * The Constant is private function name in calss {@link JobManagerImpl}.
     */
    private static final String METHOD_CONVERT_LOG_RECORDS_TO_BYTES = "convertFromLogRecordListToByteArray";

    /**
     * The Constant is private function name in calss {@link JobManagerImpl}.
     */
    private static final String METHOD_FILL_WORKFLOW_ENTITY = "fillWorkflowEntity";

    /**
     * The Constant DEFAULT_BE_USER_ID.
     */
    private static final UUID DEFAULT_BE_USER_ID = UUID.randomUUID();

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Its the Data Access Object for workflow jobs.
     */
    private JobDAO jobDao;

    /**
     * Its workflow job manager implementation class object.
     */
    private JobManagerImpl jobManagerImpl;

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        jobManagerImpl = new JobManagerImpl();
        jobDao = mockControl.createMock( JobDAO.class );
        licenseManager = mockControl.createMock( LicenseManager.class );
        jobManagerImpl.setLicenseManager( licenseManager );
    }

    /**
     * To clear the objects and mocking objects.
     */
    @After
    public void tearDown() {
        jobManagerImpl = null;
        jobDao = null;
    }

    /**
     * When in get method job id is null it should throw an exception.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void whenInGetMethodJobIdIsNullItShouldThrowAnException() throws SusException {
        final String jobId = null;
        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.JOB_ID_IS_NULL ) );
        jobManagerImpl.getJob( DEFAULT_BE_USER_ID, jobId );
    }

    /**
     * When in private method job impl is passed it should return workflow entity.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenInPrivateMethodJobImplIsPassedItShouldReturnWorkflowEntity() throws Exception {

        final JobManagerImpl jobManagerImpl = PowerMock.createPartialMock( JobManagerImpl.class, METHOD_FILL_WORKFLOW_ENTITY );
        final JobImpl jobImpl = new JobImpl();
        final WorkflowEntity workflowEntity = WhiteboxImpl.invokeMethod( jobManagerImpl, METHOD_FILL_WORKFLOW_ENTITY, jobImpl );
        Assert.assertNotNull( workflowEntity );
        Assert.assertNull( workflowEntity.getName() );
        Assert.assertNull( workflowEntity.getDescription() );
        Assert.assertNull( workflowEntity.getDefinition() );
        Assert.assertNull( workflowEntity.getCreatedOn() );
        Assert.assertNull( workflowEntity.getModifiedOn() );
    }

    /**
     * When in private method log records are passed it should return bytes.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenInPrivateMethodLogRecordsArePassedItShouldReturnBytes() throws Exception {

        final JobManagerImpl jobManagerImpl = PowerMock.createPartialMock( JobManagerImpl.class, METHOD_CONVERT_LOG_RECORDS_TO_BYTES );
        final List< LogRecord > logRecords = new ArrayList<>();
        logRecords.add( new LogRecord( DUMMY_STRING, DUMMY_STRING_MESSAGE ) );

        final byte[] bytes = WhiteboxImpl.invokeMethod( jobManagerImpl, METHOD_CONVERT_LOG_RECORDS_TO_BYTES, logRecords );
        Assert.assertTrue( bytes.length > ConstantsInteger.INTEGER_VALUE_ZERO );
    }

}
