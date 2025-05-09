package de.soco.software.simuspace.workflow.processing.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.PythonEnvironmentDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsScriptExtension;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.JobLog;

/**
 * This Class designed to execute a python script element.
 *
 * @author Ahsan.Khan
 */
@Log4j2
public class WorkflowPythonElementAction extends WFElementAction {

    /**
     * Auto generated serial version UID of class.
     */
    private static final long serialVersionUID = 4678923538199837597L;

    /**
     * The Constant UNABLE_TO_WRITE_FILE.
     */
    private static final String UNABLE_TO_WRITE_FILE = "Unable to write file : ";

    /**
     * The Constant LOG_MESSAGE_PRE_FIX_ELEMENT.
     */
    private static final String LOG_MESSAGE_PRE_FIX_ELEMENT = "Element : ";

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The Constant SCRIPT_EXECUTION_DONE.
     */
    private static final String SCRIPT_EXECUTION_DONE = "Script execution done";

    /**
     * The Constant SCRIPT_EXECUTION_DONE.
     */
    private static final String SCRIPT_EXECUTION_FAILED_WITH_EXIT_CODE = "Script execution failed with exit code: ";

    /**
     * The Constant SCRIPT_FILE_NOT_CREATED.
     */
    private static final String SCRIPT_FILE_NOT_CREATED = "Script file not created : ";

    /**
     * The Constant OUT.
     */
    private static final String ELEMENT_OUTPUT = "Element Output :";

    /**
     * The Constant EXECUTION_TIME.
     */
    private static final String EXECUTION_TIME = "executionTime: ";

    private static final String DEFAULT = "Default";

    /**
     * The job element.
     */
    private final transient UserWFElement element;

    /**
     * The python execution path.
     */
    private String pythonExecutionPath;

    /**
     * The environment DTO.
     */
    private PythonEnvironmentDTO environmentDTO;

    /**
     * Instantiates a new workflow python element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param pythonExecutionPath
     *         the python execution path
     */
    public WorkflowPythonElementAction( Job job, UserWFElement element, Map< String, Object > parameters, String pythonExecutionPath ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        this.pythonExecutionPath = pythonExecutionPath;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Instantiates a new workflow python element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param pythonExecutionPath
     *         the python execution path
     * @param executedElementIds
     *         the executed element ids
     */
    public WorkflowPythonElementAction( Job job, UserWFElement element, Map< String, Object > parameters, String pythonExecutionPath,
            Set< String > executedElementIds ) {
        super( job, element, executedElementIds );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        this.pythonExecutionPath = pythonExecutionPath;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Adds the job logs.
     *
     * @param executionTime
     *         the execution time
     */
    private void addJobLogs( final int executionTime ) {
        try {
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
            wfLogger.info( EXE_ELEMENT + element.getName() );
            final Notification notif = doAction( executionTime );
            if ( ( notif != null ) && !notif.getErrors().isEmpty() ) {
                for ( final Error error : notif.getErrors() ) {
                    log.error( LOG_MESSAGE_PRE_FIX_ELEMENT + element.getName() + ConstantsString.COLON + error.getMessage() );
                    wfLogger.error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_TYPE, element.getKey() )
                            + ConstantsString.PIPE_CHARACTER + ConstantsString.TAB_SPACE
                            + MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_NAME, element.getName() )
                            + ConstantsString.PIPE_CHARACTER + ConstantsString.TAB_SPACE
                            + MessagesUtil.getMessage( WFEMessages.ERROR_MESSAGE, error.getMessage() ) );

                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                            LOG_MESSAGE_PRE_FIX_ELEMENT + element.getName() + ConstantsString.COLON + error.getMessage() ) );
                }
                writeOutPutParentFile( EXIT_CODE_WITH_ERROR );
                if ( element.getStopOnWorkFlowOption().equals( ConstantsString.DEFAULT_VALUE_FOR_WORKFLOW_STOP_ON_ERROR ) ) {
                    throw new SusRuntimeException( notif.getErrors().toString() );
                }
            }
            wfLogger.info( EXE_ELEM_COMPL + element.getName() );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
            executedElementsIds.add( element.getId() );
            // update elemnt exeuted at server
            if ( !job.isFileRun() ) {
                JobLog.updateLogAndProgress( job, executedElementsIds.size() );
            }
        } catch ( final SusException | IOException e ) {
            log.error( "Pythone Element Execution Error in Thread: ", e );
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                        LOG_MESSAGE_PRE_FIX_ELEMENT + element.getName() + ConstantsString.COLON + e ) );
                updateJobAndStatusFailed( e );
            } catch ( final SusException e1 ) {
                log.error( e1.getLocalizedMessage(), e );
            }
            writeOutPutParentFile( EXIT_CODE_WITH_ERROR );
            throw new SusRuntimeException( e.getMessage() );
        }
    }

    /**
     * Do action.
     *
     * @param executionTime
     *         the execution time
     *
     * @return the notification
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public Notification doAction( int executionTime ) throws IOException {
        final Notification notif = new Notification();
        if ( parameters == null ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.EMPTY_PARAM ) ) );
        } else if ( element != null ) {
            notif.addNotification( element.validateException() );
            if ( notif.hasErrors() ) {
                return notif;
            }
            notif.addNotification( setPythonExecutionPathFromSelectedEnvironment( element.getFields() ) );
            for ( final Field< ? > elementField : element.getFields() ) {
                preparePythonScriptFile( executionTime, notif, elementField );
            }
        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }
        return notif;
    }

    private Notification setPythonExecutionPathFromSelectedEnvironment( List< Field< ? > > fields ) {
        Notification notification = null;
        for ( Field< ? > field : fields ) {
            if ( field.getType().contentEquals( FieldTypes.SELECTION.getType() ) && field.getValue() != null ) {
                if ( !field.getValue().equals( DEFAULT ) ) {
                    notification = readPythonPathFromEnvironmentSelection( field.getValue().toString() );
                    pythonExecutionPath = environmentDTO.getPath();
                }
            }
        }
        return notification == null ? new Notification() : notification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DecisionObject execute() {
        try {
            final int executionTime = element.getExecutionValue();
            if ( executionTime == ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT ) {
                wfLogger.info( MessagesUtil.getMessage( WFEMessages.ELEMENT_IS_GOING_TO_EXECUTE_WITHOUT_TIME_LIMIT, element.getName() ) );
                try {
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                            MessagesUtil.getMessage( WFEMessages.ELEMENT_IS_GOING_TO_EXECUTE_WITHOUT_TIME_LIMIT, element.getName() ) ) );
                } catch ( final SusException e ) {
                    log.error( e.getLocalizedMessage(), e );
                }
            }
            addJobLogs( executionTime );

            Boolean isPermitted = false;
            for ( final Field< ? > elementField : element.getOutput() ) {
                if ( elementField.getType().contentEquals( FieldTypes.SELECTION.getType() ) ) {
                    isPermitted = elementField.getValue().toString().equals( "Yes" );
                }
            }

            parameters.put( element.getName(), isPermitted );
            writeOutPutParentFile( DEFULT_EXIT_CODE );
            setJobResultParameters();

            DecisionObject result = new DecisionObject( true, isPermitted, element.getKey(), element.getName(), parameters );
            result.setWorkflowOutput( workflowOutput );
            return result;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            updateJobAndStatusFailed( e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Execute script.
     *
     * @param fileName
     *         the file name
     * @param executionTime
     *         the execution time
     *
     * @return the notification
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private Notification executeScript( String fileName, int executionTime ) throws IOException {
        Notification notif = null;
        final StringBuilder out = new StringBuilder();
        if ( writeScriptWithListOfCommands( new java.io.File( fileName ) ) ) {
            notif = new Notification();
            try {
                ProcessResult processResult = new ProcessResult();
                final Process process = runTimeExecution( fileName, pythonExecutionPath, processResult );
                notif.addNotification( killProcessIfTimeOutOccurs( executionTime, process ) );
                readOutputAndErrorsOfProcess( process, out, null );
                /* This blocks the current thread until the spawned process terminates. **/
                if ( executionTime == -1 ) {
                    process.waitFor();
                } else {
                    process.waitFor( executionTime, TimeUnit.SECONDS );
                }
                processResult.setPid( String.valueOf( process.pid() ) );
                processResult.setExitValue( process.exitValue() );
                processResult.setOutputString( out.toString() );
                processResult.setOperation( element.getName() );
                processResult.logProcessExitStatus();
                if ( processResult.getExitValue() != 0 ) {
                    handlePythonExitForError( out, notif, processResult );
                } else {
                    handlePythonExitForSuccess( out );
                }
                closeAllProcessStreams( process );
                process.destroy();
            } catch ( final Exception e ) {
                handlePythonCallException( e );
            }
        } else {
            log.error( SCRIPT_FILE_NOT_CREATED + fileName );
            wfLogger.error( SCRIPT_FILE_NOT_CREATED + fileName );
        }
        return notif;
    }

    /**
     * Handle python call exception.
     *
     * @param e
     *         the e
     */
    private void handlePythonCallException( Exception e ) {
        log.error( e.getMessage(), e );
        wfLogger.error( e.getMessage(), e );
        try {
            JobLog.addLog( job.getId(),
                    new LogRecord( ConstantsMessageTypes.ERROR, "Element execution interrupted : " + element.getName() ) );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, e.getMessage() ) );
        } catch ( final SusException e1 ) {
            log.error( e.getMessage(), e );
            wfLogger.error( e.getMessage(), e );
        }
        if ( e instanceof InterruptedException ) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Close all process streams.
     *
     * @param process
     *         the process
     *
     * @throws IOException
     *         the io exception
     */
    private static void closeAllProcessStreams( Process process ) throws IOException {
        if ( process.getOutputStream() != null ) {
            process.getOutputStream().close();
        }
        if ( process.getInputStream() != null ) {
            process.getInputStream().close();
        }
        if ( process.getErrorStream() != null ) {
            process.getErrorStream().close();
        }
    }

    /**
     * Handle python exit for success.
     *
     * @param out
     *         the out
     */
    private void handlePythonExitForSuccess( StringBuilder out ) {
        if ( StringUtils.isNotNullOrEmpty( out.toString() ) ) {
            wfLogger.success( ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out ) );
        }
        wfLogger.success( SCRIPT_EXECUTION_DONE );
        JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, SCRIPT_EXECUTION_DONE ) );
    }

    /**
     * Handle python exit for error.
     *
     * @param out
     *         the out
     * @param notif
     *         the notif
     * @param processResult
     *         the process result
     */
    private void handlePythonExitForError( StringBuilder out, Notification notif, ProcessResult processResult ) {
        if ( StringUtils.isNotNullOrEmpty( out.toString() ) ) {
            wfLogger.error( ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out ) );
        }
        notif.addError( new Error( SCRIPT_EXECUTION_FAILED_WITH_EXIT_CODE + processResult.getExitValue() ) );
    }

    /**
     * Gets the parent element name.
     *
     * @return the parent element name
     */
    @Override
    public String getParentElementName() {
        return parentElementName;
    }

    /**
     * Gets the python execution path.
     *
     * @return the python execution path
     */
    public String getPythonExecutionPath() {
        return pythonExecutionPath;
    }

    /**
     * Kill process if time out occurs.
     *
     * @param executionTime
     *         the execution time
     * @param process
     *         the process
     *
     * @return the notification
     *
     * @throws InterruptedException
     *         the interrupted exception
     */
    private Notification killProcessIfTimeOutOccurs( int executionTime, Process process ) throws InterruptedException {
        final Notification notif = new Notification();
        log.info( EXECUTION_TIME + executionTime );
        if ( executionTime == ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT ) {
            wfLogger.info( MessagesUtil.getMessage( WFEMessages.ELEMENT_IS_GOING_TO_EXECUTE_WITHOUT_TIME_LIMIT, element.getName() ) );
        } else {
            wfLogger.info(
                    MessagesUtil.getMessage( WFEMessages.ELEMENT_IS_GOING_TO_EXECUTE_FOR_SECONDS, element.getName(), executionTime ) );
            if ( !process.waitFor( executionTime, TimeUnit.SECONDS ) ) {
                notif.addError(
                        new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_HAS_STOP_EXECUTION_DUE_TO_TIME_OUT, element.getName() ) ) );
                // timeout - kill the process.
                process.destroy(); // consider using destroyForcibly instead
            }
        }
        return notif;
    }

    /**
     * Prepare python script file.
     *
     * @param executionTime
     *         the execution time
     * @param notif
     *         the notif
     * @param elementField
     *         the element field
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void preparePythonScriptFile( int executionTime, final Notification notif, final Field< ? > elementField ) throws IOException {
        if ( elementField.getType().contentEquals( FieldTypes.TEXTAREA.getType() ) ) {
            cmd = elementField.getValue().toString().trim();
            replaceAllVariableValuesInScript( notif );
            // getting directory and making script file path
            final String directory =
                    job.getWorkingDir().getPath() + java.io.File.separator + ConstantsString.DOT + job.getName() + ConstantsString.DOT
                            + job.getId();
            new java.io.File( directory ).mkdir();
            String scriptName = directory + java.io.File.separator + element.getName() + ConstantsScriptExtension.PY;
            if ( parameters.containsKey( "loopNumber" ) ) {
                scriptName = directory + java.io.File.separator + element.getName() + parameters.get( "loopNumber" ).toString()
                        + ConstantsScriptExtension.PY;
            }
            final Notification a = this.executeScript( scriptName, executionTime );

            notif.addErrors( a == null ? new ArrayList<>() : a.getErrors() );
        }
    }

    /**
     * Sets script paramaters by getting config of selected script
     *
     * @param env
     *         the selected script
     *
     * @return the notification
     */
    private Notification readPythonPathFromEnvironmentSelection( String env ) {
        Notification notif = new Notification();
        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/workflow/python/environment/" + env;

        SusResponseDTO response = SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) );
        if ( response.getData() != null ) {
            environmentDTO = JsonUtils.jsonToObject( ( JsonUtils.toJson( response.getData() ) ), PythonEnvironmentDTO.class );
        } else {
            notif.addError( new Error( "Selected Environment's details could not be found" ) );
            return notif;
        }
        if ( environmentDTO.getName() == null || environmentDTO.getName().isEmpty() ) {
            notif.addError( new Error( "Environment Name not found" ) );
            return notif;
        }
        if ( environmentDTO.getPath() == null || environmentDTO.getPath().isEmpty() || !( new File(
                environmentDTO.getPath() ) ).exists() ) {
            notif.addError( new Error( "Environment Path not found" ) );
            return notif;
        }
        return new Notification();
    }

    /**
     * Replace all variable values in script.
     *
     * @param notif
     *         the notif
     */
    private void replaceAllVariableValuesInScript( final Notification notif ) {

        final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( cmd ) );
        if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
            final List< String > variablesIncludingDotResolved = new ArrayList<>();
            for ( String splitters : variablesIncludingDot ) {
                variablesIncludingDotResolved.add( replaceAfterDotVariablesForSupportOfJsonExtract( splitters ) );
            }
            computeGlobalCmdForMultipleDotKeys( new Notification(), variablesIncludingDotResolved, variablesIncludingDot );
        }

        final List< String > variables = getAllSimpleVariables( cmd );
        if ( CollectionUtils.isNotEmpty( variables ) ) {
            computeGlobalCmd( notif, variables );
        }

        log.info( "After replacement notifies: " + notif.getErrors() );
        if ( notif.getErrors().size() > ConstantsInteger.INTEGER_VALUE_ZERO ) {
            wfLogger.error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_TYPE, element.getKey() ) + ConstantsString.PIPE_CHARACTER
                    + ConstantsString.TAB_SPACE + MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_NAME, element.getName() )
                    + ConstantsString.PIPE_CHARACTER + ConstantsString.TAB_SPACE + MessagesUtil.getMessage(
                    WFEMessages.ERROR_IN_SCRIPT_FORMAT, notif.getErrors() ) );

        }

    }

    /**
     * Run time execution.
     *
     * @param fileName
     *         the file name
     * @param path
     *         the path
     * @param processResult
     *         the process result
     *
     * @return the process
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private Process runTimeExecution( String fileName, String path, ProcessResult processResult ) throws IOException {
        if ( environmentDTO != null ) {
            wfLogger.info( "using " + environmentDTO.getName() + " environment at path " + pythonExecutionPath );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    "using " + environmentDTO.getName() + " environment at path " + pythonExecutionPath ) );
        } else {
            wfLogger.info( "using environment at path " + pythonExecutionPath );
            JobLog.addLog( job.getId(),
                    new LogRecord( ConstantsMessageTypes.INFO, "using environment at path " + pythonExecutionPath ) );
        }
        String[] cmdArray = null;
        Process process;
        final java.io.File file = new java.io.File( fileName );
        if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_WINDOWS ) ) {
            cmdArray = new String[]{ ConstantsString.WIN_EXE, ConstantsString.WIN_C_PARAM,
                    "\"" + path + "\" -u" + ConstantsString.SPACE + file.getAbsolutePath() };
        } else if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
            cmdArray = new String[]{ ConstantsString.NIX_SHELL, ConstantsString.NIX_C_PARAM,
                    path + " -u" + ConstantsString.SPACE + file.getAbsolutePath() };
        }
        processResult.setCommand( cmdArray );
        final ProcessBuilder pb = new ProcessBuilder( cmdArray ).redirectErrorStream( true );
        pb.directory( new File( job.getWorkingDir().getPath() ) );
        process = pb.start();
        return process;
    }

    /**
     * Sets the python execution path.
     *
     * @param pythonExecutionPath
     *         the new python execution path
     */
    public void setPythonExecutionPath( String pythonExecutionPath ) {
        this.pythonExecutionPath = pythonExecutionPath;
    }

    /**
     * Write script with list of commands.
     *
     * @param file
     *         the file
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private boolean writeScriptWithListOfCommands( java.io.File file ) throws IOException {
        boolean checker = false;
        if ( file.exists() ) {
            file.delete();
        }

        if ( !file.exists() && file.createNewFile() ) {
            try ( final FileOutputStream fop = new FileOutputStream( file );
                    final PrintStream stream = new PrintStream( fop, true, StandardCharsets.UTF_8 ) ) {
                stream.write( ConstantsScriptExtension.PYTHON_SCRIPT_INDICATION.getBytes() );
                stream.write( ConstantsString.NEW_LINE.getBytes() );
                stream.write( cmd.getBytes( StandardCharsets.UTF_8 ) );
                stream.flush();
                checker = true;
            } catch ( final IOException e ) {
                log.error( UNABLE_TO_WRITE_FILE, e );
                wfLogger.error( UNABLE_TO_WRITE_FILE + e.getMessage() );
            }
        }
        return checker;
    }

}