/*
 *
 */

package de.soco.software.simuspace.workflow.model.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;

/**
 * This class is specifically written to test a Job validations<br> It tests all the public methods of JobImpl.java
 *
 * @author Aroosa.Bukhari
 */
@PrepareForTest( JobImpl.class )
public class JobImplTest {

    /**
     * The Constant DUMMY_INVALID_WORKFLOW_VERSION.
     */
    private static final int DUMMY_INVALID_WORKFLOW_VERSION = -1;

    /**
     * The Constant DUMMY_TOKEN.
     */
    private static final String DUMMY_TOKEN = "eaa3a035689402e9581c115008dbf15c4ec30839";

    /**
     * The Constant DUMMY_USER_AGENT.
     */
    private static final String DUMMY_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) SIMuSPACE/2.0.0 Chrome/53.0.2785.113 Electron/1.4.3 Safari/537.36";

    /**
     * The Constant DUMMY_WORKFLOW_VERSION.
     */
    private static final int DUMMY_WORKFLOW_VERSION = 1;

    /**
     * The Constant Element_NAME.
     */
    private static final String ELEMENT_NAME = "element's name";

    /**
     * The Constant INVALID_JOB_NAME_LENGTH.
     */
    private static final int INVALID_JOB_NAME_LENGTH = 65;

    /**
     * The mock control object.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * This method initialize mock control.
     *
     * @return the i mocks control
     */
    private static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * The job impl.
     */
    private JobImpl jobImpl;

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * This method sets up the jobImpl object which is used in different test cases.
     */
    @Before
    public void setUp() {
        jobImpl = new JobImpl();
        jobImpl.setWorkflowId( UUID.randomUUID() );
        jobImpl.setWorkflowVersion( new VersionDTO( DUMMY_WORKFLOW_VERSION ) );
        jobImpl.setName( RandomStringUtils.randomAlphabetic( ConstantsLength.STANDARD_NAME_LENGTH ) );
        jobImpl.setRequestHeaders( new RequestHeaders( DUMMY_TOKEN, DUMMY_USER_AGENT ) );
        mockControl().resetToNice();

        final RestAPI restApi = mockControl().createMock( RestAPI.class );
        EasyMock.expect( restApi.validate() ).andReturn( new Notification() );
        mockControl().replay();
        jobImpl.setServer( restApi );

    }

    // *************************************************************************
    //
    // Test under method validateForExecution()
    //
    // *************************************************************************

    /**
     * This method checks that only valid job id is accepted for updation of job
     */
    @Test
    public void whenJobIdIsNullItShouldHaveErrorInputJobIdIsNull() {
        boolean isJobUpdated = false;
        jobImpl.setId( null );
        try {
            isJobUpdated = jobImpl.validateUpdateJob();
        } catch ( final Exception e ) {
            if ( e.getMessage().toString().contains( MessagesUtil.getMessage( WFEMessages.JOB_ID_IS_NULL ) ) ) {
                isJobUpdated = true;
            }
        }
        assertTrue( isJobUpdated );
    }

    /**
     * This method checks that only valid name with proper length is accepted
     */
    @Test
    public void whenJobNameIsInvalidItShouldHaveInvalidWorkFlowVersion() {
        boolean error = false;
        jobImpl.setName( RandomStringUtils.randomAlphabetic( ConstantsLength.STANDARD_NAME_LENGTH + 1 ) );
        final Notification notif = jobImpl.validateForExecution();
        assertTrue( notif.hasErrors() );
        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains(
                    MessagesUtil.getMessage( WFEMessages.UTILS_VALUE_TOO_LARGE, ELEMENT_NAME, ConstantsLength.STANDARD_NAME_LENGTH ) ) ) {
                error = true;
            }
        }
        assertTrue( error );

    }

    /**
     * This method checks that only valid work flow name is accepted for creation of job
     *
     * @throws SusException
     */
    @Test
    public void whenJobNameIsNullItShouldHaveErrorInputJobNameIsNull() throws SusException {
        boolean isJobSave = false;
        jobImpl.setName( null );
        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.JOB_NAME_IS_NULL ) );
        isJobSave = jobImpl.validateSaveJob();
        assertTrue( isJobSave );
    }

    // *************************************************************************
    //
    // Test under method validateSaveJob()
    //
    // *************************************************************************

    /**
     * This method checks that only valid job owner is accepted for creation of job
     *
     * @throws SusException
     */
    @Test
    public void whenJobUserIsNullItShouldHaveErrorInputUserIsNull() throws SusException {
        boolean isJobSave = false;
        jobImpl.setCreatedBy( null );
        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.JOB_USER_IS_NULL ) );
        isJobSave = jobImpl.validateSaveJob();
        assertTrue( isJobSave );
    }

    /**
     * This method checks that only valid work flow version id is accepted
     */
    @Test
    public void whenJobVersionIsInvalidItShouldHaveInvalidWorkFlowVersion() {
        boolean error = false;
        jobImpl.setWorkflowVersion( new VersionDTO( DUMMY_INVALID_WORKFLOW_VERSION ) );
        final Notification notif = jobImpl.validateForExecution();
        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.INVALID_WORKFLOW_VERSION ) ) ) {
                error = true;
            }
        }
        assertTrue( error );
    }

    /**
     * This method checks that only valid work flow id for job is accepted for creation of job
     *
     * @throws SusException
     */
    @Test
    public void whenJobWorkFlowIdIsNullItShouldHaveErrorInputWorkFlowIdIsNull() throws SusException {
        boolean isJobSave = false;
        final UserDTO user = new UserDTO();
        jobImpl.setCreatedBy( user );
        jobImpl.setWorkflowId( null );

        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.JOB_WORKFLOW_ID_IS_NULL ) );
        isJobSave = jobImpl.validateSaveJob();
        assertNotNull( jobImpl.getCreatedBy() );
        assertTrue( isJobSave );
    }

}
