package de.soco.software.simuspace.workflow.processing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.exception.UserWFParseException;
import de.soco.software.simuspace.workflow.exception.WorkFlowExecutionException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.UserWorkflowImpl;

/**
 * This class test workflow input element action and validates all its public and private methods.
 *
 * @author shaista
 */
@PrepareForTest( WorkflowIOElementAction.class )
public class WorkflowInputElementActionTest {

    /**
     * The Constant INVALID_FILE.
     */
    private final static String INVALID_FILE = "src/test/resources/WF_User_invalid.js";

    /**
     * The Constant VALID_FILE.
     */
    private final static String VALID_FILE = "src/test/resources/WF_User.js";

    /**
     * The work flow input element.
     */
    private static WorkflowIOElementAction wfInputElementAction;

    /**
     * The notification.
     */
    private Notification notif;

    /**
     * This method takes path of a work flow and generate a work flow from it.
     *
     * @param userWorkFlowFilePath
     *
     * @return work flow
     *
     * @throws FileNotFoundException
     */
    private UserWorkFlow getWorkflow( String userWorkFlowFilePath ) throws FileNotFoundException {
        UserWorkFlow workflow = new UserWorkflowImpl();
        final File initialFile = new File( userWorkFlowFilePath );
        try ( final InputStream targetStream = new FileInputStream( initialFile ) ) {
            workflow = JsonUtils.jsonStreamToObject( targetStream, UserWorkFlow.class );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return workflow;

    }

    /**
     * Prepare job impl.
     *
     * @return the job
     */
    public Job prepareJobImpl() {
        final Job job = new JobImpl();
        job.setId( UUID.randomUUID() );
        return job;
    }

    /**
     * This method set up notification object. This method executes before each test execution
     */
    @Before
    public void setInput() {
        notif = new Notification();
    }

    /**
     * When work flow input element is invalid it will throw Json serialization exception.
     *
     * @throws JsonSerializationException
     * @throws UserWFParseException
     * @throws FileNotFoundException
     * @throws WorkFlowExecutionException
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenWorkflowInputElementIsInValidItShouldReturnNoException() throws Exception {
        UserWorkFlow workflow = new UserWorkflowImpl();
        workflow = getWorkflow( INVALID_FILE );

        for ( final UserWFElement element : workflow.getNodes() ) {
            if ( element.getKey().equals( ElementKeys.IO.getKey() ) ) {
                wfInputElementAction = new WorkflowIOElementAction( prepareJobImpl(), element, new HashMap<>() );
                notif = wfInputElementAction.doAction();
                assertNotNull( wfInputElementAction );
                assertFalse( notif.hasErrors() );
            }
        }
    }

    /**
     * This test do action on a work flow input element successfully.
     *
     * @throws FileNotFoundException
     *         the file not found exception
     */
    @Test
    public void whenWorkflowInputElementIsValidItReturnsNoError() throws FileNotFoundException {
        UserWorkFlow workflow = new UserWorkflowImpl();
        workflow = getWorkflow( VALID_FILE );

        for ( final UserWFElement element : workflow.getNodes() ) {
            if ( element.getKey().equals( ElementKeys.IO.getKey() ) ) {
                wfInputElementAction = new WorkflowIOElementAction( prepareJobImpl(), element, new HashMap<>() );
                notif = wfInputElementAction.doAction();
                assertFalse( notif.hasErrors() );
            }
        }
    }

    /**
     * This test validate input element should not be null.
     *
     * @throws Exception
     */
    @Test
    public void whenWorkflowInputNullElementItShouldReturnError() throws Exception {
        wfInputElementAction = new WorkflowIOElementAction( prepareJobImpl(), null, new HashMap<>() );
        notif = wfInputElementAction.doAction();

        assertTrue( notif.hasErrors() );
        assertEquals( notif.getErrors().get( 0 ).getMessage(), MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) );
    }

}
