package de.soco.software.simuspace.workflow.processing.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Info;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.activator.WfEngineActivate;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.ElementConfig;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.WorkflowConfigElements;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.JobLog;
import de.soco.software.simuspace.workflow.util.WorkflowOutput;

@Log4j2
public class WorkflowTrainModelElementAction extends WFElementAction {

    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = -1561562095813285825L;

    /**
     * The job element.
     */
    private final transient UserWFElement element;

    /**
     * The Constant TRAINER_TYPE_FIELD.
     */
    private static final String TRAINER_TYPE_FIELD = "trainerType";

    /**
     * The Constant LOG_MESSAGE_PRE_FIX_ELEMENT.
     */
    private static final String LOG_MESSAGE_PRE_FIX_ELEMENT = "Element : ";

    /**
     * The Constant RESERVE_KEY_LIST.
     */
    private static final String RESERVE_KEY_LIST = ".LIST";

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The key pair train model.
     */
    private Map< String, Object > keyPairTrainModel = new HashMap<>();

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
    public WorkflowTrainModelElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        setId( element.getId() );
    }

    /**
     * Instantiates a new workflow train model element action.
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
    public WorkflowTrainModelElementAction( Job job, UserWFElement element, Map< String, Object > parameters,
            Set< String > executedElementIds ) {
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

            prepareAndExecuteTrainModelCommand( notif );

        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }

        return notif;
    }

    /**
     * Prepare and execute train model command
     */
    private void prepareAndExecuteTrainModelCommand( Notification notif ) {
        if ( job.getRunsOn().getId().toString().equals( LocationsEnum.LOCAL_LINUX.getId() ) || job.getRunsOn().getId().toString()
                .equals( LocationsEnum.LOCAL_WINDOWS.getId() ) ) {

            JobLog.addLog( job.getId(),
                    new LogRecord( ConstantsMessageTypes.WARNING, "Train Model element python script cannot be executed on local job" ) );
            wfLogger.warn( "Train Model element python script cannot be executed on local job" );
            return;
        }

        List< String > trainModelParameters = getParametersFromFields( notif );
        String pythonScriptPath = getPythonScriptPath();
        updateSystemOutputFile();
        executeAlgoPythonScript( notif, pythonScriptPath, trainModelParameters );
    }

    /**
     * Execute algo python file
     *
     * @return the command result
     */
    public Notification executeAlgoPythonScript( Notification notif, String pythonScriptPath, List< String > trainModelParameters ) {

        try {
            wfLogger.info( "executing training algo python script" );
            wfLogger.info( "script parameters: " + String.join( " ", trainModelParameters ) );

            String[] command = new String[ 2 + trainModelParameters.size() ];

            command[ 0 ] = WfEngineActivate.getPythonExecutionPath();
            command[ 1 ] = pythonScriptPath;

            for ( int i = 0; i < trainModelParameters.size(); i++ ) {
                command[ i + 2 ] = trainModelParameters.get( i );
            }

            ProcessResult result = WorkflowExecutionManagerImpl.runCommand( Arrays.asList( command ), element.getName(), true, null );

            if ( result.getExitValue() != 0 ) {
                if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
                    notif.addError( new Error( "Issue on calling Training model python file : " + result.getErrorStreamString() ) );
                    wfLogger.error( "Issue on calling Training model python file : " + result.getErrorStreamString() );
                }
            } else {
                if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
                    wfLogger.warn( result.getErrorStreamString() );
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.WARNING, result.getErrorStreamString() ) );
                }
            }

            if ( result.getOutputString() != null && !result.getOutputString().isEmpty() ) {
                notif.addInfo( new Info( result.getOutputString() ) );
                wfLogger.info( "Info on calling Training model python file : " + result.getOutputString() );
            }
            wfLogger.info( "Training model script execution finished" );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            notif.addError( new Error( "Failed to execute training model" ) );
        }

        return notif;
    }

    /**
     * Gets Parameters
     */
    private List< String > getParametersFromFields( Notification notif ) {
        List< String > trainModelParameters = new ArrayList<>();
        try {

            List< String > parameterNames = getParameterNamesFromConfig();
            JSONObject postProcessObject = null;

            if ( job.getPostprocess() != null && job.getPostprocess().getPostProcessWorkflow() != null
                    && job.getPostprocess().getPostProcessWorkflow().getElements() != null ) {

                boolean stop = false;
                for ( Entry< String, Object > askOnRunFields : job.getPostprocess().getPostProcessWorkflow().getElements().entrySet() ) {
                    try {
                        JSONParser parser = new JSONParser();
                        postProcessObject = ( JSONObject ) parser.parse( JsonUtils.toJson( askOnRunFields.getValue() ) );

                        for ( String paramName : parameterNames ) {
                            if ( postProcessObject.containsKey( paramName ) ) {
                                stop = true;
                                break;
                            }
                        }
                        if ( stop ) {
                            break;
                        }

                        wfLogger.debug( "JSONObject populated from postProcess " + postProcessObject );
                    } catch ( ParseException e ) {
                        log.error( e.getMessage(), e );
                        wfLogger.error( e.getMessage() );
                    }
                }
            }
            for ( final String paramName : parameterNames ) {
                Field< ? > elementField = element.getFields().stream().filter( p -> p.getName().equals( paramName ) ).findAny()
                        .orElse( null );
                if ( elementField != null ) {
                    if ( paramName.contains( "trainModelInputCSV" ) && postProcessObject != null && postProcessObject.containsKey(
                            paramName ) ) {
                        String inputFilePath = downloadCsvAndVerify();
                        trainModelParameters.add( inputFilePath );
                        keyPairTrainModel.put( paramName, inputFilePath );
                        continue;
                    }
                    if ( paramName.equalsIgnoreCase( "input_csv" ) && !checkInputFieldValueFormat( elementField.getValue().toString() ) ) {
                        // if rerun job has input csv in wrong format
                        // extracts fullpath of csv from elementField and put it directly in trainModelParaemeters
                        String fieldValue = elementField.getValue().toString();
                        String textData = fieldValue.substring( fieldValue.indexOf( "fullPath=" ) + 9,
                                fieldValue.lastIndexOf( ".csv" ) + 4 );
                        trainModelParameters.add( textData );
                        keyPairTrainModel.put( paramName, textData );

                    } else {
                        String textData = elementField.getValue().toString();
                        textData = replaceAllVariableValuesInText( textData, notif );
                        trainModelParameters.add( textData );
                        keyPairTrainModel.put( paramName, textData );
                    }
                }
            }

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            notif.addError( new Error( e.getMessage() ) );
            wfLogger.debug( "ERROR " + e );
        }

        return trainModelParameters;
    }

    /**
     * Check input field value format boolean.
     *
     * @param field
     *         the field
     *
     * @return true if correct format
     */
    private boolean checkInputFieldValueFormat( String field ) {
        return !field.startsWith( "[" ) || !field.endsWith( "]" );
    }

    private String downloadCsvAndVerify() {
        String urlCsv = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/job/" + job.getWorkflow().getDummyMasterJobId() + "/csv/download/" + job.getId().toString();

        SusResponseDTO response = SuSClient.getRequest( urlCsv, prepareHeaders( job.getRequestHeaders() ) );
        File fileCsv = new File( response.getData().toString() );
        if ( fileCsv.exists() ) {
            return response.getData().toString();
        } else {
            wfLogger.error( "ERROR: job summary file do not exists" );
        }
        return null;
    }

    /**
     * It gets workflow configurations latest from server.<br> It communicates with server and gets configuration in payload.
     *
     * @return parameter names
     */
    private List< String > getParameterNamesFromConfig() {
        List< String > parameterNames = new ArrayList<>();
        WorkflowConfigElements trainElementConfig;

        Field< ? > trainerTypeField = element.getFields().stream().filter( p -> p.getName().equals( TRAINER_TYPE_FIELD ) ).findAny()
                .orElse( null );
        String trainerTypeValue = trainerTypeField.getValue().toString();

        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/workflow/config";
        final SusResponseDTO responseDTO = SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) );

        try {
            JSONParser parser = new JSONParser();
            JSONObject workflowConf = ( JSONObject ) parser.parse( JsonUtils.toJson( responseDTO.getData(), String.class ) );

            JSONArray elements = ( JSONArray ) workflowConf.get( "elements" );

            for ( Object obj : elements ) {
                if ( obj instanceof JSONObject elementJson && elementJson.containsKey( "id" ) && elementJson.get( "id" )
                        .equals( "dc14ac39-1243-484a-94ba-12db7bb46930" ) ) {
                    JSONArray children = ( JSONArray ) elementJson.get( "children" );
                    for ( Object childObj : children ) {
                        if ( childObj instanceof JSONObject childElement && childElement.get( "title" ).equals( "TrainModel" ) ) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            trainElementConfig = objectMapper.readValue( childElement.toString(), WorkflowConfigElements.class );

                            if ( trainElementConfig.getElement() != null ) {
                                final ElementConfig elementConfig = trainElementConfig.getElement().getData();
                                for ( final Field< ? > elementField : elementConfig.getFields() ) {
                                    if ( elementField.getName().equals( TRAINER_TYPE_FIELD ) || elementField.getBindVisibility() == null
                                            || elementField.getBindVisibility().stream().noneMatch(
                                            bindVisibility -> bindVisibility.getValues().stream()
                                                    .anyMatch( bindValue -> bindValue.getValue().equals( trainerTypeValue ) ) ) ) {
                                        continue;
                                    }

                                    parameterNames.add( elementField.getName() );
                                }
                            }
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }

        return parameterNames;
    }

    /**
     * Gets python script path
     */
    private String getPythonScriptPath() {
        Field< ? > trainerTypeField = element.getFields().stream().filter( p -> p.getName().equals( TRAINER_TYPE_FIELD ) ).findAny()
                .orElse( null );
        String trainingAlgoName = trainerTypeField.getValue().toString();
        return getPythonScriptPathFromServer( trainingAlgoName );
    }

    /**
     * Gets the python script path from server.
     *
     * @return the python script path from server
     */
    private String getPythonScriptPathFromServer( String trainingAlgoName ) {
        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/config/trainingalgo/" + trainingAlgoName;
        return SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) ).getData().toString();
    }

    /**
     * Replaces all variable values in text
     */
    private String replaceAllVariableValuesInText( String textData, Notification notif ) {
        final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( textData ) );
        if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
            textData = replaceVariableValues( textData, variablesIncludingDot, new Notification() );
        }

        final List< String > variables = getAllSimpleVariables( textData );
        if ( CollectionUtils.isNotEmpty( variables ) ) {
            textData = replaceVariableValues( textData, variables, notif );
        }

        log.info( "After replacement notifies: " + notif.getErrors() );
        if ( notif.getErrors().size() > ConstantsInteger.INTEGER_VALUE_ZERO ) {
            wfLogger.error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_TYPE, element.getKey() ) + ConstantsString.PIPE_CHARACTER
                    + ConstantsString.TAB_SPACE + MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_NAME, element.getName() )
                    + ConstantsString.PIPE_CHARACTER + ConstantsString.TAB_SPACE + MessagesUtil.getMessage(
                    WFEMessages.ERROR_IN_SCRIPT_FORMAT, notif.getErrors() ) );

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
        String IdValue = splitCMD[ 0 ];
        IdValue = IdValue + "}}";
        fieldz = fieldz.replace( IdValue, "" );
        if ( parameters.containsKey( IdValue ) ) {
            Object fId = parameters.get( IdValue );
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

    @Override
    public DecisionObject execute() {
        try {

            final int executionTime = element.getExecutionValue();
            addLogForUnlimitedExecution( executionTime );

            final ExecutorService executor = Executors.newFixedThreadPool( ConstantsInteger.INTEGER_VALUE_ONE );

            executeTrainModelElement( executor, executionTime );

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

    private void updateSystemOutputFile() {
        try ( InputStream elementOutputStream = new FileInputStream( job.getElementOutput() ) ) {
            workflowOutput = JsonUtils.jsonStreamToObject( elementOutputStream, WorkflowOutput.class );
            workflowOutput.put( "TrainModel", keyPairTrainModel );
            if ( !job.getElementOutput().exists() ) {
                job.getElementOutput().createNewFile();
            }

            try ( final FileWriter fileWriter = new FileWriter( job.getElementOutput(), false ) ) {
                fileWriter.write( ( JsonUtils.toJson( workflowOutput ) ) );
                fileWriter.flush();
            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
                wfLogger.error( "failed to update outputJson file :" + e.getMessage() );
            }
        } catch ( final IOException e ) {
            log.error( e.getMessage(), e );
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
    private void executeTrainModelElement( ExecutorService executor, int executionTime ) {
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
                wfLogger.error( "Train Model Element Execution Error in Thread: ", e );
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

}
