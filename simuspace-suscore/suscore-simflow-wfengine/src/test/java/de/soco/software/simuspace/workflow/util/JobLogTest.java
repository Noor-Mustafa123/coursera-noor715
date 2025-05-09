package de.soco.software.simuspace.workflow.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;

/**
 * Test Cases for JobLog class.
 */
@PrepareForTest( JobLog.class )
public class JobLogTest {

    /**
     * The Constant DUMMY_TOKEN.
     */
    private static final String DUMMY_TOKEN = "eaa3a035689402e9581c115008dbf15c4ec30839";

    /**
     * The Constant DUMMY_USER_AGENT.
     */
    private static final String DUMMY_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) SIMuSPACE/2.0.0 Chrome/53.0.2785.113 Electron/1.4.3 Safari/537.36";

    /**
     * The Constant HOST.
     */
    private static final String HOST = "testHost";

    /**
     * The Constant JOB_ID.
     */
    private static final UUID JOB_ID = UUID.randomUUID();

    /**
     * The job impl.
     */
    private static JobImpl jobImpl;

    /**
     * The Constant MESSAGE.
     */
    private static final String MESSAGE = "test Message";

    private static final String MESSAGE_TYPE = "INFO";

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant PORT.
     */
    private static final String PORT = "testPort";

    /**
     * The Constant PROTOCOL.
     */
    private static final String PROTOCOL = "testProtocol";

    /**
     * Mock control.
     *
     * @return the mocks control object
     */
    private static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Gets the dummy log record.
     *
     * @return the dummy log
     */
    private LogRecord getDummyLog() {
        return new LogRecord( MESSAGE_TYPE, MESSAGE );
    }

    /**
     * This method is used to set to values before running test cases.
     */
    @Before
    public void setUp() {
        mockControl().resetToNice();
        jobImpl = new JobImpl();
    }

    /**
     * Should add log message of job.
     */
    @Test
    public void shouldAddLogMessageOfJob() {
        final RequestHeaders headers = new RequestHeaders( DUMMY_TOKEN, DUMMY_USER_AGENT );
        final RestAPI api = new RestAPI( PROTOCOL, HOST, PORT );

        jobImpl.setServer( api );
        jobImpl.setRequestHeaders( headers );
        jobImpl.setId( JOB_ID );

        mockControl().replay();
        JobLog.setJob( jobImpl );
        JobLog.addLog( JOB_ID, getDummyLog() );

        assertNotNull( JobLog.getlogListByJobId( JOB_ID ) );
        assertEquals( JobLog.getlogListByJobId( JOB_ID ).get( 0 ).getLogMessage(), getDummyLog().getLogMessage() );
    }

}
