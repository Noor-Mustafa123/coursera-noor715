package de.soco.software.simuspace.workflow.processing.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Info;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.activator.WfEngineActivate;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.properties.EnginePropertiesManager;
import de.soco.software.simuspace.workflow.util.JobLog;

import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

/**
 * The Class WorkflowHPCElementAction.
 */
@Log4j2
public class WorkflowHPCElementAction extends WFElementAction {

    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = -1561562095813285825L;

    /**
     * The job element.
     */
    private final transient UserWFElement element;

    /**
     * The Constant LOG_MESSAGE_PRE_FIX_ELEMENT.
     */
    private static final String LOG_MESSAGE_PRE_FIX_ELEMENT = "Element : ";

    /**
     * The Constant HPC_PROJECT_FIELD.
     */
    private static final String HPC_PROJECT_FIELD = "project";

    /**
     * The Constant RESERVE_KEY_LIST.
     */
    private static final String RESERVE_KEY_LIST = ".LIST";

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

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
    public WorkflowHPCElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        setId( element.getId() );
    }

    /**
     * Instantiates a new workflow HPC element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param executedElementIds
     *         the executed element ids
     */
    public WorkflowHPCElementAction( Job job, UserWFElement element, Map< String, Object > parameters, Set< String > executedElementIds ) {
        super( job, element, executedElementIds );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        setId( element.getId() );
    }

    /**
     * The doAction. This function is responsible for parsing user workflow element and replacing input variables for parameters and will
     * create expression.
     *
     * @return the notification
     */
    public Notification doAction() {
        final Notification notif = new Notification();

        if ( parameters == null ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.EMPTY_PARAM ) ) );
        } else if ( element != null ) {
            notif.addNotification( element.validateException() );
            if ( notif.hasErrors() ) {
                return notif;
            }
            wfLogger.info( "create Attributes File From Fields" );
            createAttributesFileFromFields( notif );
            wfLogger.info( "prepare And Execute SGE Command" );
            prepareAndExecuteSGECommand( notif );
        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }

        return notif;
    }

    /**
     * Executes sge command
     */
    private void prepareAndExecuteSGECommand( Notification notif ) {

        if ( job.getRunsOn().getId().toString().equals( LocationsEnum.LOCAL_LINUX.getId() ) || job.getRunsOn().getId().toString()
                .equals( LocationsEnum.LOCAL_WINDOWS.getId() ) ) {

            JobLog.addLog( job.getId(),
                    new LogRecord( ConstantsMessageTypes.WARNING, "HPC element python script cannot be executed on local job" ) );
            wfLogger.warn( "HPC element python script cannot be executed on local job" );
            return;
        }

        String projectOption = "";

        for ( final Field< ? > elementField : element.getFields() ) {
            if ( elementField.getName().contentEquals( HPC_PROJECT_FIELD ) ) {
                projectOption = elementField.getValue().toString();
            }
        }

        String attributesFilePath = job.getWorkingDir().getPath() + File.separator + "HpcOptions.cfg";
        FileUtils.setGlobalAllFilePermissions( new File( attributesFilePath ) );

        executeSGEPythonFile( notif, projectOption, element.getKey().split( "_" )[ 1 ], attributesFilePath );

    }

    /**
     * Call sge python file
     *
     * @param notif
     *         the notif
     * @param project
     *         the project
     * @param solver
     *         the solver
     * @param attributesFilePath
     *         the input file path
     */
    public void executeSGEPythonFile( Notification notif, String project, String solver, String attributesFilePath ) {

        try {
            wfLogger.info( "callSGEPythonFile  method called" );
            Properties properties = getEngineProperties();
            String[] command = new String[ 7 ];
            command[ 0 ] = WfEngineActivate.getSgePythonPath();
            command[ 1 ] = getPythonPath( properties );
            command[ 2 ] = EnginePropertiesManager.getKarafScriptPath();
            command[ 3 ] = job.getWorkingDir().getPath();
            command[ 4 ] = project;
            command[ 5 ] = solver;
            command[ 6 ] = attributesFilePath;

            ProcessResult result = WorkflowExecutionManagerImpl.runCommand( Arrays.asList( command ), element.getName(), true, null );

            if ( result.getExitValue() != 0 ) {
                if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
                    notif.addError( new Error( "Issue on calling SGE python file : " + result.getErrorStreamString() ) );
                    wfLogger.error( "Issue on calling SGE python file : " + result.getErrorStreamString() );
                }
            } else {
                if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
                    notif.addInfo( new Info( "Issue on calling SGE python file : " + result.getErrorStreamString() ) );
                    wfLogger.warn( "Issue on calling SGE python file : " + result.getErrorStreamString() );
                }
            }
            if ( result.getOutputString() != null && !result.getOutputString().isEmpty() ) {
                notif.addInfo( new Info( result.getOutputString() ) );
                wfLogger.info( "Info on calling SGE python file : " + result.getOutputString() );
            }
            wfLogger.info( "callSGEPythonFile  Finished : " + Arrays.toString( command ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            notif.addError( new Error( "Failed to execute: " + WfEngineActivate.getSgePythonPath() ) );
        }
    }

    /**
     * Creates file in job directory from data in text field
     */
    private void createAttributesFileFromFields( Notification notif ) {
        StringBuilder fileData = new StringBuilder();
        for ( final Field< ? > elementField : element.getFields() ) {
            if ( elementField.getType().contentEquals( FieldTypes.TEXT.getType() ) || elementField.getType()
                    .contentEquals( FieldTypes.TEXTAREA.getType() ) || elementField.isVariableMode() ) {
                String textData = elementField.getValue().toString();
                textData = replaceAllVariableValuesInText( textData, notif );
                fileData.append( elementField.getName() ).append( ConstantsString.EQUALS_OPERATOR ).append( textData )
                        .append( System.lineSeparator() );
                parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + ConstantsString.DOT
                        + elementField.getName() + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, textData );
            } else if ( ( elementField.getType().contentEquals( FieldTypes.SERVER_FILE_EXPLORER.getType() ) )
                    && !elementField.isVariableMode() ) {
                String textData = getServerFile( elementField );
                fileData.append( elementField.getName() ).append( ConstantsString.EQUALS_OPERATOR ).append( textData )
                        .append( System.lineSeparator() );
            } else {
                fileData.append( elementField.getName() ).append( ConstantsString.EQUALS_OPERATOR ).append( elementField.getValue() )
                        .append( System.lineSeparator() );
            }
        }
        final String destinationDir = job.getWorkingDir().getPath() + File.separator + "HpcOptions.cfg";
        File file = new File( destinationDir );
        try {
            file.createNewFile();
            try ( FileWriter writer = new FileWriter( file ) ) {
                writer.write( fileData.toString() );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            notif.addError( new Error( "Failed to write HpcOptions.cfg" ) );
        }
    }

    /**
     * Replaces all variable values in text
     */
    private String replaceAllVariableValuesInText( String textData, Notification notif ) {
        final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( textData ) );
        if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
            final List< String > variablesIncludingDotResolved = new ArrayList<>();
            for ( String splitters : variablesIncludingDot ) {
                variablesIncludingDotResolved.add( replaceAfterDotVariablesForSupportOfJsonExtract( splitters ) );
            }
            textData = replaceVariableValuesForMultipleDotKeys( textData, variablesIncludingDotResolved, variablesIncludingDot,
                    new Notification() );
        }

        final List< String > variables = getAllSimpleVariables( textData );
        if ( CollectionUtils.isNotEmpty( variables ) ) {
            textData = replaceVariableValues( textData, variables, notif );
        }

        log.info( "After replacement notifies: " + notif.getErrors() );
        if ( !notif.getErrors().isEmpty() ) {
            wfLogger.error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_TYPE, element.getKey() ) + ConstantsString.PIPE_CHARACTER
                    + ConstantsString.TAB_SPACE + MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_NAME, element.getName() )
                    + ConstantsString.PIPE_CHARACTER + ConstantsString.TAB_SPACE + MessagesUtil.getMessage(
                    WFEMessages.ERROR_IN_SCRIPT_FORMAT, notif.getErrors() ) );

        }

        return textData;
    }

    private String replaceVariableValuesForMultipleDotKeys( String textData, List< String > variablesIncludingDotResolved,
            List< String > variablesIncludingDot, Notification notif ) {
        int keyIndex = 0;
        for ( final String argKey : variablesIncludingDotResolved ) {
            if ( argKey.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !argKey.startsWith(
                    ConstantsString.SYS_CMD_INDICATION ) && !argKey.contains( USER_OUTPUT_TAB ) && !argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                if ( parameters.containsKey( argKey ) ) {
                    final Object argObj = getArgValueFromJson( parameters, argKey );
                    textData = replaceArguments( argObj, argKey, textData );
                } else if ( argKey.contains( "}}." ) ) {
                    final String basicJsonKeyword = argKey.substring( FIRST_INDEX, argKey.indexOf( "}}." ) + 2 );
                    if ( parameters.containsKey( basicJsonKeyword ) ) {
                        try {
                            final Object mainJsonObject = getArgValueFromJson( parameters, basicJsonKeyword );
                            String item = argKey.substring( argKey.indexOf( "}}." ) + 3 );
                            JSONValue.COMPRESSION = JSONStyle.LT_COMPRESS;
                            Object object = JsonPath.read(
                                    Configuration.defaultConfiguration().jsonProvider().parse( mainJsonObject.toString() ),
                                    ConstantsString.DOLLAR + "." + item );
                            textData = replaceArguments( object, argKey, textData );
                        } catch ( final SusException e ) {
                            log.error( e.getLocalizedMessage(), e );
                        }

                    }
                } else if ( argKey.contains( RESERVE_KEY_LIST ) ) {
                    textData = computeReserveKeyWordLIST( argKey, textData );
                } else {
                    notif.addError(
                            new Error( MessagesUtil.getMessage( WFEMessages.PARAM_DONT_CONTAIN_ARGUMENT, argKey, element.getName() ) ) );
                }
            } else if ( argKey.startsWith( ConstantsString.SYS_CMD_INDICATION ) ) {
                String localCmd = argKey.replace( ConstantsString.SYS_CMD_INDICATION, ConstantsString.EMPTY_STRING );
                localCmd = localCmd.replace( ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, ConstantsString.EMPTY_STRING );
                if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
                    localCmd = "$" + localCmd;
                } else if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_WINDOWS ) ) {
                    localCmd = "%" + localCmd + "%";
                }
                textData = replaceArguments( localCmd, argKey, textData );
            } else if ( argKey.contains( USER_OUTPUT_TAB ) ) {
                cmd = textData; // below method operates on cmd hence assigning textData to cmd
                outputTransferringElements( notif, argKey, variablesIncludingDot.get( keyIndex ) );
                textData = cmd;
            } else if ( argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                cmd = textData; // below method operates on cmd hence assigning textData to cmd
                systemTransferringElements( notif, argKey, variablesIncludingDot.get( keyIndex ) );
                textData = cmd;
            }
            keyIndex++;
        }
        return textData;
    }

    /**
     * Replaces all variable values in text
     */
    private String replaceVariableValues( String textData, List< String > variables, Notification notif ) {

        for ( final String argKey : variables ) {
            if ( argKey.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !argKey.startsWith(
                    ConstantsString.SYS_CMD_INDICATION ) && !argKey.contains( USER_OUTPUT_TAB ) && !argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                if ( parameters.containsKey( argKey ) ) {
                    final Object argObj = getArgValueFromJson( parameters, argKey );
                    textData = replaceArguments( argObj, argKey, textData );
                } else if ( argKey.contains( RESERVE_KEY_LIST ) ) {
                    textData = computeReserveKeyWordLIST( argKey, textData );
                } else {
                    notif.addError(
                            new Error( MessagesUtil.getMessage( WFEMessages.PARAM_DONT_CONTAIN_ARGUMENT, argKey, element.getName() ) ) );
                }
            } else if ( argKey.startsWith( ConstantsString.SYS_CMD_INDICATION ) ) {
                String localCmd = argKey.replace( ConstantsString.SYS_CMD_INDICATION, ConstantsString.EMPTY_STRING );
                localCmd = localCmd.replace( ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, ConstantsString.EMPTY_STRING );
                if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
                    localCmd = "$" + localCmd;
                } else if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_WINDOWS ) ) {
                    localCmd = "%" + localCmd + "%";
                }
                textData = replaceArguments( localCmd, argKey, textData );
            } else if ( argKey.contains( USER_OUTPUT_TAB ) ) {
                cmd = textData; // below method operates on cmd hence assigning textData to cmd
                outputTransferringElements( notif, argKey, null );
                textData = cmd;
            } else if ( argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                cmd = textData; // below method operates on cmd hence assigning textData to cmd
                systemTransferringElements( notif, argKey, null );
                textData = cmd;
            }
        }
        return textData;
    }

    /**
     * Replace arguments.
     *
     * @param argObject
     *         the arg object
     * @param argKey
     *         the arg key
     *
     * @return the notification
     */
    protected String replaceArguments( Object argObject, String argKey, String textData ) {
        if ( argObject != null ) {
            String value = argObject.toString();
            if ( value.contains( ConstantsString.DELIMETER_FOR_WIN ) ) {
                value = value.replace( ConstantsString.DELIMETER_FOR_WIN, ConstantsString.PATH_SEPARATOR_WIN );
            }
            textData = textData.replace( argKey, value );
        } else {
            textData = textData.replace( argKey, ConstantsString.EMPTY_STRING );
            wfLogger.warn( MessagesUtil.getMessage( WFEMessages.ARGUMENT_CAN_NOT_BE_NULL, argKey ) );
        }
        return textData;
    }

    @Override
    public DecisionObject execute() {
        try {
            final int executionTime = element.getExecutionValue();
            addLogForUnlimitedExecution( executionTime );
            final ExecutorService executor = Executors.newFixedThreadPool( ConstantsInteger.INTEGER_VALUE_ONE );
            executeHPCElement( executor, executionTime );
            // wait all unfinished tasks for 2 sec
            try {
                if ( !executor.awaitTermination( ConstantsInteger.INTEGER_VALUE_TWO, TimeUnit.SECONDS ) ) {
                    executor.shutdownNow();
                }
            } catch ( final InterruptedException e ) {
                log.error( "Executor shoutdown interrupted.", e );
                Thread.currentThread().interrupt();
            }
            setJobResultParameters();
            return new DecisionObject( true, element.getKey(), parameters );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            updateJobAndStatusFailed( e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Executes Hpc element.
     *
     * @param executor
     *         the executor
     * @param executionTime
     *         the execution time
     */
    private void executeHPCElement( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
                wfLogger.info( EXE_ELEMENT + element.getName() );
                final Notification notif = doAction();
                processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
                wfLogger.success( EXE_ELEM_COMPL + element.getName() );

                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
                executedElementsIds.add( element.getId() );
                // update elemnt exeuted at server
                if ( !job.isFileRun() ) {
                    JobLog.updateLogAndProgress( job, executedElementsIds.size() );
                }
            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
                wfLogger.error( "HPC Element Execution Error in Thread: ", e );
                try {
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                            LOG_MESSAGE_PRE_FIX_ELEMENT + element.getName() + ConstantsString.COLON + e ) );
                    updateJobAndStatusFailed( e );
                } catch ( final SusException e1 ) {
                    log.error( e1.getMessage(), e1 );
                    wfLogger.error( e1.getLocalizedMessage() );
                }
                throw new SusRuntimeException( e.getLocalizedMessage() );
            }
        };

        final Future< ? > future = executor.submit( task );

        executor.shutdown();

        try {
            if ( ( executionTime == ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT ) || ( executionTime
                    == ConstantsInteger.ELEMENT_NOT_EXECUTE_AT_ALL ) ) {
                future.get();
            } else {
                wfLogger.info(
                        MessagesUtil.getMessage( WFEMessages.ELEMENT_IS_GOING_TO_EXECUTE_FOR_SECONDS, element.getName(), executionTime ) );
                // <-- wait for runtime seconds to finish
                future.get( executionTime, TimeUnit.SECONDS );
            }
        } catch ( final InterruptedException e ) {
            log.error( "job was interrupted ", e );
            Thread.currentThread().interrupt();
            throw new SusRuntimeException( e.getMessage() );
        } catch ( final ExecutionException e ) {
            log.error( "caught exception: ", e );
            throw new SusRuntimeException( e.getMessage() );
        } catch ( final TimeoutException e ) {
            future.cancel( true );
            log.error( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, element.getName() ), e );
            throw new SusRuntimeException( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, element.getName() ) );
        }
    }

    /**
     * Compute reserve key word LIST.
     *
     * @param argKey
     *         the arg key
     */
    private String computeReserveKeyWordLIST( final String argKey, String textData ) {
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
                    textData = textData.replace( argKey, object.toString() );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                }
            } else {
                textData = textData.replace( argKey, json );
            }

        }

        return textData;
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

}
