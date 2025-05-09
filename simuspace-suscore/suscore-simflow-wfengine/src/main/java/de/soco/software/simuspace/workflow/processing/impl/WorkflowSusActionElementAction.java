package de.soco.software.simuspace.workflow.processing.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
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
import de.soco.software.simuspace.suscore.common.model.DynamicScript;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.JobLog;

/**
 * This Class designed to execute a susAction element.
 *
 * @author Shahzeb Iqbal
 */
@Log4j2
public class WorkflowSusActionElementAction extends WFElementAction {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -7304369052098288440L;

    /**
     * The Constant LOG_MESSAGE_PRE_FIX_ELEMENT.
     */
    private static final String LOG_MESSAGE_PRE_FIX_ELEMENT = "Element : ";

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The Constant ELEMENT_OUTPUT.
     */
    private static final String ELEMENT_OUTPUT = "ELement Output :";

    /**
     * The Constant SCRIPT_EXECUTION_DONE.
     */
    private static final String SCRIPT_EXECUTION_DONE = "Script execution done";

    /**
     * The Constant SCRIPT_EXECUTION_FAILED_WITH_EXIT_CODE.
     */
    private static final String SCRIPT_EXECUTION_FAILED_WITH_EXIT_CODE = "Script execution failed with exit code: ";

    /**
     * The Constant EXECUTION_TIME.
     */
    private static final String EXECUTION_TIME = "executionTime: ";

    /**
     * The Constant PYTHON_EXECUTION_PATH.
     */
    private static final String PYTHON_EXECUTION_PATH = "Python execution path : ";

    /**
     * The job element.
     */
    private final transient UserWFElement element;

    /**
     * The python execution path.
     */
    private String pythonExecutionPath;

    /**
     * The action script.
     */
    private DynamicScript actionScript;

    /**
     * The json file path.
     */
    private String jsonFilePath;

    /**
     * Instantiates a new workflow SusAction element action.
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
    public WorkflowSusActionElementAction( Job job, UserWFElement element, Map< String, Object > parameters, String pythonExecutionPath ) {
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
     * Instantiates a new workflow sus action element action.
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
    public WorkflowSusActionElementAction( Job job, UserWFElement element, Map< String, Object > parameters, String pythonExecutionPath,
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
                throw new SusRuntimeException( notif.getErrors().toString() );
            }
            wfLogger.info( EXE_ELEM_COMPL + element.getName() );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
            executedElementsIds.add( element.getId() );
            // update elemnt exeuted at server
            if ( !job.isFileRun() ) {
                JobLog.updateLogAndProgress( job, executedElementsIds.size() );
            }
        } catch ( final SusException | IOException e ) {
            log.error( "SUSAction Element Execution Error in Thread: ", e );
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                        LOG_MESSAGE_PRE_FIX_ELEMENT + element.getName() + ConstantsString.COLON + e ) );
                updateJobAndStatusFailed( e );
            } catch ( final SusException e1 ) {
                log.error( e1.getLocalizedMessage(), e1 );
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
            prepareActionScriptFile( executionTime, notif, element.getFields() );

        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
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
     * @param elementFields
     *         the element fields
     */
    private void prepareActionScriptFile( int executionTime, final Notification notif, final List< Field< ? > > elementFields ) {

        String selectedScript = null;
        for ( Field< ? > elementField : elementFields ) {
            if ( elementField.getType().contentEquals( FieldTypes.SELECTION.getType() ) ) {
                selectedScript = elementField.getValue().toString()
                        .replace( ConstantsString.DOUBLE_QUOTE_STRING, ConstantsString.EMPTY_STRING );
            }
            if ( elementField.getType().contentEquals( FieldTypes.SERVER_FILE_EXPLORER.getType() ) ) {
                jsonFilePath = checkVariableModeandGetServerFile( elementField, notif );
            }
        }

        final Notification scriptNotif = readExecutionConfigFromActionConfig( selectedScript );
        notif.addErrors( scriptNotif == null ? new ArrayList<>() : scriptNotif.getErrors() );

        final Notification a = this.executeScript( executionTime );
        notif.addErrors( a.getErrors() );
    }

    /**
     * Returns Server File path and resolves Variables if Variable Mode is On
     *
     * @param elementField
     *         the element field
     * @param notif
     *         the notif
     *
     * @return File Path
     */
    private String checkVariableModeandGetServerFile( Field< ? > elementField, Notification notif ) {
        if ( !elementField.isVariableMode() ) {
            return getServerFile( elementField );
        } else {
            final String strValue = elementField.getValue().toString();
            if ( strValue.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !strValue.startsWith(
                    ConstantsString.SYS_CMD_INDICATION ) ) {
                cmd = strValue;
                final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( cmd ) );
                if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
                    computeGlobalCmd( new Notification(), variablesIncludingDot );
                }
                final List< String > variables = getAllSimpleVariables( cmd );
                if ( CollectionUtils.isNotEmpty( variables ) ) {
                    computeGlobalCmd( notif, variables );
                }
                return cmd;

            } else {
                return strValue;
            }
        }
    }

    /**
     * Sets script paramaters by getting config of selected script
     *
     * @param selectedScript
     *         the selected script
     *
     * @return the notification
     */
    private Notification readExecutionConfigFromActionConfig( String selectedScript ) {
        Notification notif = new Notification();
        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/workflow/susaction/scripts/" + selectedScript;

        SusResponseDTO response = SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) );
        if ( response.getData() != null ) {
            actionScript = JsonUtils.jsonToObject( ( JsonUtils.toJson( response.getData() ) ), DynamicScript.class );
        } else {
            notif.addError( new Error( "Selected Sript's details could not be found" ) );
            return notif;
        }
        if ( actionScript.getName() == null || actionScript.getName().isEmpty() ) {
            notif.addError( new Error( "Script Name not found" ) );
            return notif;
        }
        if ( actionScript.getPath() == null || actionScript.getPath().isEmpty() || !( new File( actionScript.getPath() ) ).exists() ) {
            notif.addError( new Error( "Script Path not found" ) );
            return notif;
        }
        actionScript.setType( "python" );// default for now. Setting just in case it is not set in config
        return null;
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

            boolean isPermitted = false; // Do not Generate User Output File

            writeOutPutParentFile( DEFULT_EXIT_CODE );
            setJobResultParameters();

            return new DecisionObject( true, isPermitted, element.getKey(), element.getName(), parameters );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            updateJobAndStatusFailed( e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Execute script.
     *
     * @param executionTime
     *         the execution time
     *
     * @return the notification
     */
    private Notification executeScript( int executionTime ) {
        final StringBuilder out = new StringBuilder();
        Notification notif = new Notification();
        try {
            ProcessResult processResult = new ProcessResult();
            final Process process = runTimeExecution( getPythonExecutionPath(), processResult );
            notif.addNotification( killProcessIfTimeOutOccurs( executionTime, process ) );

            readOutputAndErrorsOfProcess( process, out, null );

            /* This blocks the current thread until the spawned process terminates. **/
            if ( executionTime == -1 ) {
                process.waitFor();
            } else {
                process.waitFor( executionTime, TimeUnit.SECONDS );
            }
            closeAllProcessStreams( process );
            processResult.setPid( String.valueOf( process.pid() ) );
            processResult.setExitValue( process.exitValue() );
            processResult.setOutputString( out.toString() );
            processResult.logProcessExitStatus();
            process.destroy();
            if ( process.exitValue() != 0 ) {
                handlePythonExitForError( out, notif, process );
            } else {
                handlePythonExitForSuccess( out );
            }

        } catch ( final Exception e ) {
            handlePythonCallException( e );

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
            log.error( e1.getMessage(), e1 );
            wfLogger.error( e1.getMessage(), e1 );
        }
        if ( e instanceof InterruptedException ) {
            Thread.currentThread().interrupt();
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
     * @param process
     *         the process
     */
    private void handlePythonExitForError( StringBuilder out, Notification notif, Process process ) {
        if ( StringUtils.isNotNullOrEmpty( out.toString() ) ) {
            wfLogger.error( ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out ) );
        }
        notif.addError( new Error( SCRIPT_EXECUTION_FAILED_WITH_EXIT_CODE + process.exitValue() ) );
    }

    /**
     * Close all process streams.
     *
     * @param process
     *         the process
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
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
     * Run time execution.
     *
     * @param path
     *         the path
     * @param processResult
     *
     * @return the process
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private Process runTimeExecution( String path, ProcessResult processResult ) throws IOException {
        JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, PYTHON_EXECUTION_PATH + path ) );
        wfLogger.info( PYTHON_EXECUTION_PATH + path );
        String[] cmdArray = null;
        Process process;
        File jsonFile = new File( jsonFilePath );
        final java.io.File file = new java.io.File( actionScript.getPath() );
        if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_WINDOWS ) ) {
            cmdArray = new String[]{ ConstantsString.WIN_EXE, ConstantsString.WIN_C_PARAM, "\"" + path + "\" -u" + ConstantsString.SPACE
                    + file.getAbsolutePath() + ConstantsString.SPACE + "-j" + ConstantsString.SPACE + jsonFile.getAbsolutePath() };
        } else if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
            cmdArray = new String[]{ ConstantsString.NIX_SHELL, ConstantsString.NIX_C_PARAM,
                    ( ( actionScript.getEnvCmd() != null && !actionScript.getEnvCmd().isEmpty() )
                            ? ( actionScript.getEnvCmd() + ConstantsString.SPACE + ConstantsString.SEMI_COLON )
                            : ( ConstantsString.SPACE ) ) + path + " -u" + ConstantsString.SPACE + file.getAbsolutePath()
                            + ConstantsString.SPACE + "-j" + ConstantsString.SPACE + jsonFile.getAbsolutePath() };
        }
        processResult.setCommand( cmdArray );
        final ProcessBuilder pb = new ProcessBuilder( cmdArray );
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
     * Gets actionScript
     *
     * @return the actionScript
     */
    public DynamicScript getActionScript() {
        return actionScript;
    }

    /**
     * Sets actionScript
     *
     * @param dynamicScript
     *         the action script
     */
    public void setActionScript( DynamicScript dynamicScript ) {
        this.actionScript = dynamicScript;
    }

    /**
     * Gets jsonFilePath
     *
     * @return the jsonFilePath
     */
    public String getJsonFilePath() {
        return jsonFilePath;
    }

    /**
     * Sets jsonFilePath
     *
     * @param jsonFilePath
     *         the json file path
     */
    public void setJsonFilePath( String jsonFilePath ) {
        this.jsonFilePath = jsonFilePath;
    }

}