package de.soco.software.simuspace.workflow.processing.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsScriptExtension;
import de.soco.software.simuspace.workflow.constant.ConstantsScriptType;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.JobLog;

import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

/**
 * This Class designed to execute a workflow script element.
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class WorkflowScriptElementAction extends WFElementAction {

    /**
     * The Constant RESERVE_KEY_LIST.
     */
    private static final String RESERVE_KEY_LIST = ".LIST";

    /**
     * The Constant FIRST_INDEX_OF_FILE_ITEMS.
     */
    private static final int FIRST_INDEX_OF_FILE_ITEMS = 0;

    /**
     * The Constant LOG_MESSAGE_PRE_FIX_ELEMENT.
     */
    private static final String LOG_MESSAGE_PRE_FIX_ELEMENT = "Element : ";

    /**
     * The Constant OUT.
     */
    private static final String ELEMENT_OUTPUT = "ELement Output :";

    /**
     * The Constant SCRIPT_EXECUTION_DONE.
     */
    private static final String SCRIPT_EXECUTION_DONE = "Script execution done";

    /**
     * The Constant SCRIPT_EXECUTION_DONE.
     */
    private static final String SCRIPT_EXECUTION_FAILED_WITH_EXIT_CODE = "Script execution failed with exit code: ";

    /**
     * Auto generated serial version UID of class.
     */
    private static final long serialVersionUID = 4678923538199837597L;

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The job element.
     */
    private final transient UserWFElement element;

    /**
     * The constructor which sets different fields of object.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     */
    public WorkflowScriptElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }

    }

    /**
     * Instantiates a new workflow script element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param executedElementsIds
     *         the executed elements ids
     */
    public WorkflowScriptElementAction( Job job, UserWFElement element, Map< String, Object > parameters,
            Set< String > executedElementsIds ) {
        super( job, element, executedElementsIds );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }

    }

    /**
     * Compute os script cmd.
     *
     * @param notif
     *         the notif
     * @param scriptType
     *         the script type
     * @param simpleElementsIncludingDot
     *         the simple elements including dot
     */
    private void computeOsScriptCmd( final Notification notif, String scriptType, final List< String > simpleElementsIncludingDot ) {
        for ( final String argKey : simpleElementsIncludingDot ) {
            log.info( ">>replaceAllVariableValuesInScript argKey:" + argKey );
            if ( argKey.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES )
                    && !argKey.startsWith( ConstantsString.SYS_CMD_INDICATION ) && !argKey.contains( USER_OUTPUT_TAB )
                    && !argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                if ( parameters.containsKey( argKey ) ) {
                    final Object argObj = getArgValueFromJson( parameters, argKey );
                    notif.addNotification( replaceArguments( argObj, argKey ) );
                } else if ( argKey.contentEquals( WORKFLOW_PROPERTIES_KEY ) ) {
                    final Object argObj = getWorkflowPropertiesFromParameters();
                    notif.addNotification( replaceArguments( argObj, argKey ) );
                } else if ( argKey.contains( RESERVE_KEY_LIST ) ) {
                    computeReserveKeyWordLIST( argKey );
                } else {
                    notif.addError(
                            new Error( MessagesUtil.getMessage( WFEMessages.PARAM_DONT_CONTAIN_ARGUMENT, argKey, element.getName() ) ) );
                }
            } else if ( argKey.startsWith( ConstantsString.SYS_CMD_INDICATION ) ) {
                String localCmd = argKey.replace( ConstantsString.SYS_CMD_INDICATION, ConstantsString.EMPTY_STRING );
                localCmd = localCmd.replace( ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, ConstantsString.EMPTY_STRING );
                if ( scriptType.equals( ConstantsScriptType.BASH ) ) {
                    localCmd = "$" + localCmd;
                } else if ( scriptType.equals( ConstantsScriptType.BAT ) ) {
                    localCmd = "%" + localCmd + "%";
                }
                notif.addNotification( replaceArguments( localCmd, argKey ) );
            } else if ( argKey.contains( USER_OUTPUT_TAB ) ) {
                outputTransferringElements( notif, argKey, null );
            } else if ( argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                systemTransferringElements( notif, argKey, null );
            }
        }
    }

    /**
     * Compute os script cmd for multiple dot keys.
     *
     * @param notif
     *         the notif
     * @param scriptType
     *         the script type
     * @param simpleElementsIncludingDot
     *         the simple elements including dot
     * @param originalElementDotKeys
     *         the original element dot keys
     */
    private void computeOsScriptCmdForMultipleDotKeys( final Notification notif, String scriptType,
            final List< String > simpleElementsIncludingDot, List< String > originalElementDotKeys ) {

        int keyIndex = 0;

        for ( final String argKey : simpleElementsIncludingDot ) {
            log.info( ">>replaceAllVariableValuesInScript argKey:" + argKey );
            if ( argKey.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES )
                    && !argKey.startsWith( ConstantsString.SYS_CMD_INDICATION ) && !argKey.contains( USER_OUTPUT_TAB )
                    && !argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                if ( parameters.containsKey( argKey ) ) {
                    final Object argObj = getArgValueFromJson( parameters, argKey );
                    notif.addNotification( replaceArguments( argObj, argKey ) );
                } else if ( argKey.contains( RESERVE_KEY_LIST ) ) {
                    computeReserveKeyWordLIST( argKey );
                } else if ( argKey.contains( "}}." ) ) {
                    final String basicJsonKeyword = argKey.substring( FIRST_INDEX, argKey.indexOf( "}}." ) + 2 );
                    if ( parameters.containsKey( basicJsonKeyword ) ) {
                        final Object mainJsonObject = getArgValueFromJson( parameters, basicJsonKeyword );
                        String item = argKey.substring( argKey.indexOf( "}}." ) + 3 );
                        JSONValue.COMPRESSION = JSONStyle.LT_COMPRESS;
                        Object object = JsonPath.read(
                                Configuration.defaultConfiguration().jsonProvider().parse( mainJsonObject.toString() ),
                                ConstantsString.DOLLAR + "." + item );
                        notif.addNotification( replaceArguments( object, argKey ) );
                    }
                } else {
                    notif.addError(
                            new Error( MessagesUtil.getMessage( WFEMessages.PARAM_DONT_CONTAIN_ARGUMENT, argKey, element.getName() ) ) );
                }
            } else if ( argKey.startsWith( ConstantsString.SYS_CMD_INDICATION ) ) {
                String localCmd = argKey.replace( ConstantsString.SYS_CMD_INDICATION, ConstantsString.EMPTY_STRING );
                localCmd = localCmd.replace( ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, ConstantsString.EMPTY_STRING );
                if ( scriptType.equals( ConstantsScriptType.BASH ) ) {
                    localCmd = "$" + localCmd;
                } else if ( scriptType.equals( ConstantsScriptType.BAT ) ) {
                    localCmd = "%" + localCmd + "%";
                }
                notif.addNotification( replaceArguments( localCmd, argKey ) );
            } else if ( argKey.contains( USER_OUTPUT_TAB ) ) {
                String orgkey = originalElementDotKeys.get( keyIndex );
                outputTransferringElements( notif, argKey, orgkey );
            } else if ( argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                String orgkey = originalElementDotKeys.get( keyIndex );
                systemTransferringElements( notif, argKey, orgkey );
            }
            keyIndex++;
        }
    }

    /**
     * Compute reserve key word LIST.
     *
     * @param argKey
     *         the arg key
     */
    private void computeReserveKeyWordLIST( final String argKey ) {
        String fieldz = argKey.replace( RESERVE_KEY_LIST, "" );
        String replaceField = fieldz;
        String[] splitCMD = fieldz.split( "}}" );
        String idValue = splitCMD[ 0 ];
        idValue = idValue + "}}";
        fieldz = fieldz.replace( idValue, "" );
        if ( parameters.containsKey( idValue ) ) {
            Object fId = parameters.get( idValue );
            fieldz = fieldz.replace( replaceField, "" );
            String json = getChildObjectsFromServer( fId );
            if ( !fieldz.isEmpty() ) {
                List< Object > listObject = JsonUtils.jsonToList( json, Object.class );
                Map< String, Object > listMap = new HashMap<>();
                listMap.put( "objects", listObject );
                try ( ByteArrayInputStream bis = new ByteArrayInputStream( JsonUtils.toJson( listMap ).getBytes() ) ) {
                    JsonNode jsonObject = JsonUtils.convertInputStreamToJsonNode( bis );
                    Object object = JsonPath.read( Configuration.defaultConfiguration().jsonProvider().parse( jsonObject.toString() ),
                            ( ConstantsString.DOLLAR + fieldz ).trim() );
                    cmd = cmd.replace( argKey, object.toString() );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                }
            } else {
                cmd = cmd.replace( argKey, json );
            }

        }
    }

    /**
     * Gets the child objects from server.
     *
     * @param fId
     *         the f id
     *
     * @return the child objects from server
     */
    private String getChildObjectsFromServer( Object fId ) {
        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/data/project/container/childs/" + fId;
        return JsonUtils.objectToJson( SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) ).getData() );
    }

    /**
     * The doAction. This function is responsible for performing some action on a workflow script element This function gets the fields of
     * an element understand what are commands in the script that has to be executed, prepare a list of them and then calls excuteScript.
     * the parameters. this is a map which have values that two elements which are connection are sharing among them
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
            String scriptType = ConstantsString.EMPTY_STRING;
            for ( final Field< ? > elementField : element.getFields() ) {
                if ( elementField.getType().contentEquals( FieldTypes.SELECTION.getType() ) ) {
                    scriptType = elementField.getValue().toString();
                }

                getOSPermission( executionTime, notif, scriptType, elementField );
            }
        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }

        return notif;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.github.dexecutor.core.task.Task#execute()
     */
    @Override
    public DecisionObject execute() {

        final int executionTime = element.getExecutionValue();
        addLogForUnlimitedExecution( executionTime );
        try {
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
            wfLogger.info( EXE_ELEMENT + element.getName() );
            final Notification notif = doAction( executionTime );
            processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
            wfLogger.info( EXE_ELEM_COMPL + element.getName() );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
            executedElementsIds.add( element.getId() );
            // update elemnt exeuted at server
            if ( !job.isFileRun() ) {
                JobLog.updateLogAndProgress( job, executedElementsIds.size() );
            }
        } catch ( final Exception e ) {
            log.error( ElementKeys.SCRIPT.getKey() + " Execution Error in Thread: ", e );
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

        boolean isPermitted = false;
        for ( final Field< ? > elementField : element.getOutput() ) {
            if ( elementField.getType().contentEquals( FieldTypes.SELECTION.getType() ) ) {
                isPermitted = elementField.getValue().toString().equals( "Yes" );
            }
        }
        parameters.put( element.getName(), isPermitted );
        writeOutPutParentFile( DEFULT_EXIT_CODE );
        setJobResultParameters();

        return new DecisionObject( true, isPermitted, element.getKey(), element.getName(), parameters );
    }

    /**
     * The executeScript. This function is responsible for execution of commands.
     *
     * @param fileName
     *         the file name
     * @param scriptType
     *         the script type
     * @param executionTime
     *         the execution time
     *
     * @return the notification
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private Notification executeScript( String fileName, String scriptType, int executionTime ) throws IOException {

        final Notification notif = new Notification();
        final StringBuilder out = new StringBuilder();
        if ( writeScriptWithListOfCommands( new java.io.File( fileName ), scriptType ) ) {
            try {
                final Process process = runTimeExecution( fileName, scriptType );
                notif.addNotification( killProcessIfTimeOutOccurs( executionTime, process ) );

                readOutputAndErrorsOfProcess( process, out, null );

                /* This blocks the current thread until the spawned process terminates. **/
                if ( executionTime == -1 ) {
                    process.waitFor();
                } else {
                    process.waitFor( executionTime, TimeUnit.SECONDS );
                }

                if ( process.getOutputStream() != null ) {
                    process.getOutputStream().close();
                }

                if ( process.getInputStream() != null ) {
                    process.getInputStream().close();
                }

                if ( process.getErrorStream() != null ) {
                    process.getErrorStream().close();
                }

                process.destroy();

                if ( process.exitValue() != 0 ) {
                    if ( StringUtils.isNotNullOrEmpty( out.toString() ) ) {
                        log.error( ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out );
                        wfLogger.error( ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out );
                        JobLog.addLog( job.getId(),
                                new LogRecord( ConstantsMessageTypes.ERROR, ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out ) );
                    }
                    notif.addError( new Error( SCRIPT_EXECUTION_FAILED_WITH_EXIT_CODE + process.exitValue() ) );
                } else {
                    if ( StringUtils.isNotNullOrEmpty( out.toString() ) ) {
                        log.info( ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out );
                        wfLogger.success( ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out );
                        JobLog.addLog( job.getId(),
                                new LogRecord( ConstantsMessageTypes.SUCCESS, ELEMENT_OUTPUT + ConstantsString.NEW_LINE + out ) );
                    }
                    wfLogger.success( SCRIPT_EXECUTION_DONE );
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, SCRIPT_EXECUTION_DONE ) );
                }

            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
                wfLogger.error( e.getMessage(), e );
                try {
                    JobLog.addLog( job.getId(),
                            new LogRecord( ConstantsMessageTypes.ERROR, "Element execution interrupted : " + element.getName() ) );
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, e.getMessage() ) );
                } catch ( final SusException e1 ) {
                    log.error( e1.getMessage(), e1 );
                    wfLogger.error( e1.getMessage(), e1 );
                    throw new SusRuntimeException( e1.getMessage() );
                }
                if ( e instanceof InterruptedException ) {
                    Thread.currentThread().interrupt();
                }
                throw new SusRuntimeException( e.getMessage() );

            }
        } else {
            log.error( "Script file not created : " + fileName );
            wfLogger.error( "Script file not created : " + fileName );
            throw new SusRuntimeException( "Script file not created : " + fileName );
        }
        return notif;
    }

    /**
     * Gets the argument value from json.
     *
     * @param parameters
     *         the parameters
     * @param argKey
     *         the argKey
     *
     * @return the Object from json
     */
    @SuppressWarnings( "unchecked" )
    @Override
    protected Object getArgValueFromJson( Map< String, Object > parameters, String argKey ) {
        Object argObject;
        try {
            final Object object = parameters.get( argKey );
            if ( object instanceof EngineFile engineFile ) {
                argObject = engineFile.getPath();
            } else if ( object instanceof LinkedHashMap ) {
                argObject = JsonUtils.jsonToObject( JsonUtils.toJson( ( LinkedHashMap< String, String > ) parameters.get( argKey ) ),
                        de.soco.software.simuspace.workflow.model.impl.EngineFile.class ).getItems().get( FIRST_INDEX_OF_FILE_ITEMS );
            } else {
                argObject = object;
            }
        } catch ( final Exception e ) {
            log.error( "Error while executing Script : ", e );
            wfLogger.error( "Error while executing Script : " + e.getMessage() );
            throw e;
        }
        return argObject;
    }

    /**
     * Gets the cmd.
     *
     * @return the cmd
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * Gets the OS permission.
     *
     * @param executionTime
     *         the execution time
     * @param notif
     *         the notif
     * @param scriptType
     *         the script type
     * @param elementField
     *         the element field
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void getOSPermission( int executionTime, final Notification notif, String scriptType, final Field< ? > elementField )
            throws IOException {
        if ( elementField.getType().contentEquals( FieldTypes.TEXTAREA.getType() ) ) {
            cmd = elementField.getValue().toString().trim();
            if ( org.apache.commons.lang3.StringUtils.isBlank( cmd ) ) {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.UTILS_NAME_CANT_BE_NULL, elementField.getName() ) ) );
            }
            replaceAllVariableValuesInScript( notif, scriptType );

            if ( CollectionUtils.isEmpty( notif.getErrors() ) ) {
                // getting directory and making script file path
                final String directory =
                        job.getWorkingDir().getPath() + java.io.File.separator + ConstantsString.DOT + job.getName() + ConstantsString.DOT
                                + job.getId();
                new java.io.File( directory ).mkdir();
                final StringBuilder scriptName = new StringBuilder( directory + java.io.File.separator + element.getName() );
                if ( parameters.containsKey( "loopNumber" ) ) {
                    scriptName.append( parameters.get( "loopNumber" ).toString() );
                }

                if ( scriptType.equals( ConstantsScriptType.BASH ) ) {
                    scriptName.append( ConstantsScriptExtension.SH );

                } else if ( scriptType.equals( ConstantsScriptType.BAT ) ) {
                    scriptName.append( ConstantsScriptExtension.BAT );
                }
                final Notification a = this.executeScript( scriptName.toString(), scriptType, executionTime );

                notif.addErrors( a.getErrors() );
            }

        }
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
        log.info( "executionTime: " + executionTime );
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
     * Gets the variable values from parameters and replace them in script.
     *
     * @param notif
     *         the notif
     * @param scriptType
     *         the script type
     */
    private void replaceAllVariableValuesInScript( final Notification notif, String scriptType ) {

        final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( cmd ) );
        if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
            final List< String > variablesIncludingDotResolved = new ArrayList<>();
            for ( String splitters : variablesIncludingDot ) {
                variablesIncludingDotResolved.add( replaceAfterDotVariablesForSupportOfJsonExtract( splitters ) );
            }
            computeOsScriptCmdForMultipleDotKeys( new Notification(), scriptType, variablesIncludingDotResolved, variablesIncludingDot );
        }

        final List< String > variables = getAllSimpleVariables( cmd );
        if ( CollectionUtils.isNotEmpty( variables ) ) {
            computeOsScriptCmd( notif, scriptType, variables );
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
     * @param scriptType
     *         the script type
     *
     * @return the process
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public Process runTimeExecution( String fileName, String scriptType ) throws IOException {
        String[] cmdArray = null;
        Process process;
        final java.io.File file = new java.io.File( fileName );
        if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_WINDOWS ) ) {

            if ( scriptType.equals( ConstantsScriptType.BASH ) ) {
                cmdArray = new String[]{ ConstantsScriptType.BASH,
                        ConstantsString.WIN_QUOTE_STANDARD + file.getAbsolutePath() + ConstantsString.WIN_QUOTE_STANDARD };
            } else {
                cmdArray = new String[]{
                        ConstantsString.WIN_QUOTE_STANDARD + file.getAbsolutePath() + ConstantsString.WIN_QUOTE_STANDARD };
            }

        } else if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
            cmdArray = new String[]{ ConstantsString.NIX_BASH, fileName };

        }
        final ProcessBuilder pb = new ProcessBuilder( cmdArray ).redirectErrorStream( true );
        pb.directory( new File( job.getWorkingDir().getPath() ) );
        process = pb.start();
        return process;
    }

    /**
     * Sets the cmd.
     *
     * @param cmd
     *         the cmd to set
     */
    public void setCmd( String cmd ) {
        this.cmd = cmd;
    }

    /**
     * Write script with list of commands.
     *
     * @param file
     *         the file
     * @param scriptType
     *         the script type
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */

    // testing
    private boolean writeScriptWithListOfCommands( java.io.File file, String scriptType ) throws IOException {
        boolean checker = false;
        if ( file.exists() ) {
            file.delete();
        }

        if ( !file.isFile() && file.createNewFile() ) {
            try ( final FileOutputStream fop = new FileOutputStream( file );
                    final PrintStream stream = new PrintStream( fop, true, StandardCharsets.UTF_8 ) ) {
                // writing magic line for shell
                if ( scriptType.equals( ConstantsScriptType.BASH ) ) {
                    stream.write( ConstantsScriptExtension.SHELL_SCRIPT_INDICATION.getBytes() );
                } else if ( scriptType.equals( ConstantsScriptType.BAT ) ) {
                    stream.write( ConstantsScriptExtension.CMD_SCRIPT_INDICATION.getBytes() );
                    stream.write( ConstantsString.NEW_LINE.getBytes() );
                    stream.write( ConstantsScriptExtension.WIN_ENCODER.getBytes() );
                }
                stream.write( ConstantsString.NEW_LINE.getBytes() );
                if ( scriptType.equals( ConstantsScriptType.BASH ) ) {
                    stream.write( cmd.getBytes( StandardCharsets.UTF_8 ) );
                } else if ( scriptType.equals( ConstantsScriptType.BAT ) ) {
                    stream.write( cmd.getBytes( ConstantsString.ENCODING_CONST_FORWIN ) );
                }
                stream.flush();
                checker = true;
            } catch ( final IOException e ) {
                log.error( "Unable to write file : ", e );
                wfLogger.error( "Unable to write file : " + e.getMessage() );
            }
        }
        return checker;
    }

}
