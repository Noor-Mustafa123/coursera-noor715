/*
 *
 */

package de.soco.software.simuspace.workflow.processing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.exception.UserWFParseException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.UserWorkflowImpl;

/**
 * This class test workflow output element action and validates all its public.
 *
 * @author shaista
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( WorkflowScriptElementAction.class )
public class WorkflowScriptElementActionTest {

    /**
     * The command.
     */
    private static String COMMAND = "/bin/cp src/test/resources/test.txt src/test/resources/copy.txt";

    private static String EXECUTE_SCRIPT = "executeScript";

    /**
     * The Constant invalidParameters for user work flow .
     */
    private final static Map< String, Object > invalidParameters = new HashMap<>();

    /**
     * The job impl.
     */
    private static Job jobImpl;

    /**
     * The Constant parameters.
     */
    private final static Map< String, Object > parameters = new HashMap<>();

    /**
     * The source file.
     */
    private static String SOURCE_FILE = "src/test/resources/test.txt";

    /**
     * The target file.
     */
    private static String TARGET_FILE = "src/test/resources/copy.txt";

    /**
     * The valid file.
     */
    private static String VALID_FILE = "src/test/resources/WF_User.js";

    /**
     * The Constant DEFAULT_EXECUTION_TIME.
     */
    private static final int DEFAULT_EXECUTION_TIME = -1;

    /**
     * This method takes path of a workflow and generate a workflow from it.
     *
     * @param userWorkFlowFilePath
     *
     * @return workflow
     *
     * @throws FileNotFoundException
     * @throws UserWFParseException
     * @throws JsonSerializationException
     */
    private static UserWorkFlow getWorkflow( String userWorkFlowFilePath )
            throws FileNotFoundException, UserWFParseException, JsonSerializationException {
        UserWorkFlow workflow = new UserWorkflowImpl();
        final File initialFile = new File( userWorkFlowFilePath );
        try ( final InputStream targetStream = new FileInputStream( initialFile ) ) {
            workflow = JsonUtils.jsonStreamToObject( targetStream, UserWorkFlow.class );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return workflow;

    }

    /**
     * This method set up the commands and parameters for user work flow. This method executes before class execution
     *
     * @throws JsonSerializationException
     * @throws UserWFParseException
     * @throws FileNotFoundException
     */
    @BeforeClass
    public static void setup() throws FileNotFoundException, UserWFParseException, JsonSerializationException {

        final LinkedHashMap< String, String > file = new LinkedHashMap<>();
        file.put( "agent", "str" );
        file.put( "docId", "0" );
        file.put( "location", "0" );
        file.put( "path", TARGET_FILE );
        file.put( "type", "client" );
        parameters.put( "{{Output_Huz.header}}", "User Ouput Parameters" );
        parameters.put( "{{Output_Huz.file2}}", file );

        final LinkedHashMap< String, String > fileInput = new LinkedHashMap<>();
        fileInput.put( "agent", "str" );
        fileInput.put( "docId", "0" );
        fileInput.put( "location", "0" );
        fileInput.put( "path", SOURCE_FILE );
        fileInput.put( "type", "client" );
        parameters.put( "{{Input_Huz.header}}", "User Input Parameters" );
        parameters.put( "{{Input_Huz.file1}}", fileInput );
        final LinkedHashMap< String, String > fileInvalid = new LinkedHashMap<>();
        fileInvalid.put( "agent", "str" );
        fileInvalid.put( "docId", "0" );
        fileInvalid.put( "location", "0" );
        fileInvalid.put( "path", "src/test/resources/test.txt" );
        fileInvalid.put( "type", "client" );
        fileInvalid.put( "bug", "1" );
        invalidParameters.put( "{{Output_Huz.header}}", "User Ouput Parameters" );
        invalidParameters.put( "{{Output_Huz.file2}}", fileInvalid );
        invalidParameters.put( "{{Input_Huz.header}}", "User Input Parameters" );
        invalidParameters.put( "{{Input_Huz.file1}}", fileInvalid );

    }

    /**
     * The notification.
     */
    private Notification notif = null;

    /**
     * The wf script element action.
     */
    private WorkflowScriptElementAction wfScriptElementAction;

    /**
     * This method set up notification object. This method executes before each test execution
     */
    @Before
    public void setInput() {
        notif = new Notification();
    }

    /**
     * Sets the job impl.
     */
    private void setJobImpl() {
        jobImpl = new JobImpl();
        jobImpl.setId( UUID.randomUUID() );
        final de.soco.software.simuspace.workflow.model.impl.EngineFile file = new de.soco.software.simuspace.workflow.model.impl.EngineFile();
        file.setPath( this.getClass().getClassLoader().getResource( ConstantsString.EMPTY_STRING ).getFile().trim() );
        jobImpl.setWorkingDir( file );
    }

    /**
     * create a new file if file not found on output path
     *
     * @throws Exception
     */
    @Test
    public void whenOutputPathNotExistsItShouldCreateIt() throws Exception {
        UserWorkFlow workflow = new UserWorkflowImpl();
        workflow = getWorkflow( VALID_FILE );
        final LinkedHashMap< String, String > file = new LinkedHashMap<>();
        file.put( "agent", "str" );
        file.put( "docId", "0" );
        file.put( "location", "0" );
        file.put( "path", "src/test/resources/copy" );
        file.put( "type", "client" );
        parameters.put( "{{Output_Huz.header}}", "User Ouput Parameters" );
        parameters.put( "{{Output_Huz.file2}}", file );
        setJobImpl();
        for ( final UserWFElement element : workflow.getNodes() ) {

            if ( element.getKey().equals( ElementKeys.SCRIPT.getKey() ) ) {
                wfScriptElementAction = new WorkflowScriptElementAction( jobImpl, element, parameters );

                final String[] funtions = { EXECUTE_SCRIPT };
                wfScriptElementAction = PowerMock.createPartialMock( WorkflowScriptElementAction.class, funtions, jobImpl, element,
                        parameters );
                PowerMock.expectPrivate( wfScriptElementAction, EXECUTE_SCRIPT, Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyInt() ).andReturn( new Notification() ).anyTimes();
                PowerMock.replay();

                notif = wfScriptElementAction.doAction( DEFAULT_EXECUTION_TIME );
            }
        }
        assertFalse( notif.hasErrors() );

    }

    /**
     * When workflow S element has correct element it will execute successfully.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void whenWorkflowSElementHasCorrectElementItWillExecuteSuccessfully() throws Exception {
        setJobImpl();
        UserWorkFlow workflow = new UserWorkflowImpl();
        workflow = getWorkflow( VALID_FILE );
        final List< String > command = new ArrayList<>();
        command.add( COMMAND );
        for ( final UserWFElement element : workflow.getNodes() ) {
            if ( element.getKey().equals( ElementKeys.SCRIPT.getKey() ) ) {
                final String[] funtions = { EXECUTE_SCRIPT };
                wfScriptElementAction = PowerMock.createPartialMock( WorkflowScriptElementAction.class, funtions, jobImpl, element,
                        parameters );
                PowerMock.expectPrivate( wfScriptElementAction, EXECUTE_SCRIPT, Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyInt() ).andReturn( new Notification() ).anyTimes();

                PowerMock.replay();
                notif = wfScriptElementAction.doAction( DEFAULT_EXECUTION_TIME );
            }
        }
        assertFalse( notif.hasErrors() );

    }

    /**
     * If elements are null it returns "Element cannot be null" notification
     *
     * @throws SusException
     * @throws IOException
     */
    @Test
    public void whenWorkflowsElementHasNullElementItShouldReturnError() throws SusException, IOException {
        setJobImpl();
        wfScriptElementAction = new WorkflowScriptElementAction( jobImpl, null, parameters );
        final Notification notif = wfScriptElementAction.doAction( DEFAULT_EXECUTION_TIME );

        assertTrue( notif.hasErrors() );
        assertEquals( notif.getErrors().get( 0 ).getMessage(), MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) );
    }

    /**
     * When workflow S element has null element it will return error.
     *
     * @throws SusException
     *         the sus exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void whenWorkflowSElementHasNullElementItWillReturnError() throws SusException, IOException {
        setJobImpl();
        wfScriptElementAction = new WorkflowScriptElementAction( jobImpl, null, parameters );
        final Notification notif = wfScriptElementAction.doAction( DEFAULT_EXECUTION_TIME );
        assertEquals( notif.getErrors().get( 0 ).getMessage(), MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) );
        assertTrue( notif.hasErrors() );
    }

}
