/*
 *
 */

package de.soco.software.simuspace.workflow.processing.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.base.TransferResult;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.TransferOperationType;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.TransferObject;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ObjectDTO;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.ElementOutputTransfer;
import de.soco.software.simuspace.workflow.util.JobLog;

/**
 * This Class designed to execute transfer object element to transfer objects from one location to another.
 *
 * @author Nasir.Farooq
 */
@Log4j2
public class TransferLocationElementAction extends WFElementAction {

    /**
     * The Constant API_DATA_TRANSFER_OBJECT.
     */
    private static final String API_DATA_TRANSFER_OBJECT = "/api/data/transfer/object";

    /**
     * The Constant API_DATA_SELECTION.
     */
    private static final String API_DATA_SELECTION = "/api/selection/";

    /**
     * The Constant LOG_MESSAGE_PRE_FIX_ELEMENT.
     */
    private static final String LOG_MESSAGE_PRE_FIX_ELEMENT = "Element : ";

    /**
     * The Constant INVALID_OBJECT_ID.
     */
    private static final String INVALID_OBJECT_ID = "invalid_object_id";

    /**
     * The Constant ZIP_EXTENSION.
     */
    public static final String ZIP_EXTENSION = ".zip";

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -458055812168225241L;

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The Constant LABEL_OF_INPUT_DIRECTORY.
     */
    public static final String LABEL_OF_OBJECT_OR_CONTAINER = "DataObjects/Container";

    /**
     * The Constant LABEL_OF_TARGET_LOCATION.
     */
    public static final String LABEL_OF_TARGET_LOCATION = "Target Location";

    /**
     * The Constant LABEL_OF_OPERATION_TYPE_FLAG.
     */
    public static final String LABEL_OF_OPERATION_TYPE_FLAG = "Operation Type";

    /**
     * The Constant EMPTY_STRING.
     */
    private static final String EMPTY_STRING = "";

    /**
     * This is the element information coming from designer containing fields having information what to do with that element.
     */
    private final transient UserWFElement element;

    /**
     * The rest API server credentials.
     */
    private transient RestAPI restAPI;

    /**
     * The element output.
     */
    final ElementOutputTransfer elementOutput = new ElementOutputTransfer();

    /**
     * Instantiates a new import process action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     */
    public TransferLocationElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Instantiates a new transfer location element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param restApi
     *         the rest api
     * @param executedElementIds
     *         the executed element ids
     */
    public TransferLocationElementAction( Job job, UserWFElement element, Map< String, Object > parameters, RestAPI restApi,
            Set< String > executedElementIds ) {
        super( job, element, executedElementIds );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        this.restAPI = restApi;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Do action.
     *
     * @return the notification
     */
    public Notification doAction() {
        final Notification notif = new Notification();
        if ( element != null ) {
            notif.addNotification( element.validateException() );
            if ( notif.hasErrors() ) {
                return notif;
            }
            final String objectSelectionId = getSelectionIdFromUserFields( notif, LABEL_OF_OBJECT_OR_CONTAINER );
            final String locationSelectionId = getSelectionIdFromUserFields( notif, LABEL_OF_TARGET_LOCATION );
            final String transferOption = getCopyOrMoveFromUserFields();

            log.debug( "locationSelectionId: " + locationSelectionId );
            log.debug( "objectSelectionId: " + objectSelectionId );
            log.debug( "CopyOrMove: " + transferOption );

            validateUserInputs( notif, transferOption, objectSelectionId, locationSelectionId );

            if ( notif.hasErrors() ) {
                return notif;
            }

            final List< String > locationIds = getObjectIdBySelectionId( locationSelectionId, notif );
            if ( notif.hasErrors() ) {
                return notif;
            }

            final List< String > objectIds = getObjectIdBySelectionId( objectSelectionId, notif );
            if ( notif.hasErrors() ) {
                return notif;
            }

            transferObjects( notif, locationIds, objectIds, transferOption );

        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }
        log.debug( "leaving doAction()" );
        return notif;
    }

    /**
     * Function to perform a task. This will be executed if a task is going to be executed.
     *
     * @return the decision object
     */
    @Override
    public DecisionObject execute() {
        try {

            final int executionTime = element.getExecutionValue();
            addLogForUnlimitedExecution( executionTime );

            final ExecutorService executor = Executors.newFixedThreadPool( ConstantsInteger.INTEGER_VALUE_ONE );

            executeImportProcessElement( executor, executionTime );
            // wait all unfinished tasks for 2 sec
            try {
                if ( !executor.awaitTermination( ConstantsInteger.INTEGER_VALUE_TWO, TimeUnit.SECONDS ) ) {
                    executor.shutdownNow();
                }
            } catch ( final InterruptedException e ) {
                log.error( "Executor shoutdown interrupted.", e );
                elementOutput.setExitCode( EMPTY_STRING );
                Thread.currentThread().interrupt();
            }
            writeOutPutFile( DEFULT_EXIT_CODE );
            setJobResultParameters();

            return new DecisionObject( true, element.getKey(), parameters, workflowOutput, element.getName() );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            updateJobAndStatusFailed( e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Function to perform a task. This will be executed if a task is going to be executed.
     *
     * @param executor
     *         the executor
     * @param executionTime
     *         the execution time
     */
    private void executeImportProcessElement( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
                final Notification notif = doAction();
                processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
                executedElementsIds.add( element.getId() );
                if ( !job.isFileRun() ) {
                    JobLog.updateLogAndProgress( job, executedElementsIds.size() );
                }
            } catch ( final Exception e ) {
                log.error( "IO Element Execution Error in Thread: ", e );
                try {
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                            LOG_MESSAGE_PRE_FIX_ELEMENT + element.getName() + ConstantsString.COLON + e ) );
                    updateJobAndStatusFailed( e );
                } catch ( final SusException e1 ) {
                    log.error( e1.getLocalizedMessage(), e1 );
                }

                writeOutPutFile( EXIT_CODE_WITH_ERROR );

                throw new SusRuntimeException( e.getMessage() );
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
            writeOutPutFile( EXIT_CODE_WITH_ERROR );
            throw new SusRuntimeException( e.getMessage() );
        } catch ( final ExecutionException e ) {
            log.error( "caught exception: ", e );
            writeOutPutFile( EXIT_CODE_WITH_ERROR );
            throw new SusRuntimeException( e.getMessage() );
        } catch ( final TimeoutException e ) {
            future.cancel( true );
            log.error( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, element.getName() ), e );
            writeOutPutFile( EXIT_CODE_WITH_ERROR );
            throw new SusRuntimeException( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, element.getName() ) );
        }

    }

    /**
     * Gets the field value of element.
     *
     * @return the field value of element
     */
    private String getCopyOrMoveFromUserFields() {

        String variableValue = ConstantsString.EMPTY_STRING;
        for ( final Field< ? > elementField : element.getFields() ) {
            if ( elementField.getLabel().contentEquals( LABEL_OF_OPERATION_TYPE_FLAG ) ) {
                variableValue = elementField.getValue().toString();
                parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + ConstantsString.DOT
                        + elementField.getName() + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, variableValue );
                break;
            }
        }

        return variableValue;
    }

    /**
     * Gets the field variable key by label.
     *
     * @param label
     *         the label
     *
     * @return the field variable key by label
     */
    private Field< ? > getField( String label ) {
        Field< ? > variableKey = null;
        for ( final Field< ? > elementField : element.getFields() ) {
            if ( elementField.getLabel().contentEquals( label ) ) {
                variableKey = elementField;
                break;
            }
        }

        return variableKey;
    }

    /**
     * Gets the object id by selection id.
     *
     * @param selectionId
     *         the selection id
     * @param notification
     *         the notification
     *
     * @return the files by selection id
     */
    private List< String > getObjectIdBySelectionId( String selectionId, Notification notification ) {
        List< String > objectIds = new ArrayList<>();
        final SusResponseDTO susResponseDTO = SuSClient.getRequest( prepareURL( API_DATA_SELECTION + selectionId ), prepareHeaders() );
        if ( susResponseDTO == null ) {
            notification.addError( new Error( MessageBundleFactory.getDefaultMessage( Messages.NO_OBJECTS_FOUND.getKey() ) ) );
            return objectIds;
        }

        if ( !susResponseDTO.getSuccess() ) {
            notification.addError( new Error( susResponseDTO.getMessage().getContent() ) );
        } else {
            final String json = JsonUtils.toJson( susResponseDTO.getData() );
            objectIds = JsonUtils.jsonToList( json, String.class );

        }
        return objectIds;
    }

    /**
     * Gets the rest api.
     *
     * @return the rest api
     */
    public RestAPI getRestAPI() {
        return restAPI;
    }

    /**
     * validates the user provided inputs and Gets the single variable value from parameters provided by the connected elements.
     *
     * @param notif
     *         the notif
     * @param susUserScript
     *         the sus user script
     *
     * @return the single variable value
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private String getSelectionId( final Notification notif, String susUserScript ) {
        Object argObject = susUserScript;

        if ( susUserScript.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) ) {

            cmd = susUserScript;

            final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( cmd ) );
            if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
                computeGlobalCmd( new Notification(), variablesIncludingDot );
            }

            final List< String > variables = getAllSimpleVariables( cmd );
            if ( CollectionUtils.isNotEmpty( variables ) ) {
                computeGlobalCmd( notif, variables );
            }
            argObject = cmd;
        }

        log.info( "After replacement notifies: " + notif.getErrors() );
        return argObject.toString();
    }

    /**
     * The method is used to get the project id value by replacing the variables in "text" field with title "Project Selector" with values
     * provided in parameters.
     *
     * @param notif
     *         the notification
     *
     * @return String the String
     */
    private String getSelectionIdFromUserFields( Notification notif, String label ) {

        String selectionId = null;
        final Field< ? > field = getField( label );
        if ( field != null ) {
            final String selection = field.getValue().toString();
            try {
                selectionId = getSelectionId( notif, selection );
            } catch ( final NumberFormatException ex ) {
                log.error( ex.getMessage(), ex );
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_VALUE_FOR_FIELD, FieldTypes.TEXT.getType() ) ) );
            }
            parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + ConstantsString.DOT + field.getName()
                    + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, selectionId );
        }

        return selectionId;
    }

    /**
     * It adds headers for required for communication with server.<br>
     *
     * @return requestHeaders
     */
    private Map< String, String > prepareHeaders() {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, restAPI.getRequestHeaders().getToken() );
        requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, restAPI.getRequestHeaders().getJobAuthToken() );

        return requestHeaders;

    }

    /**
     * It prepares url by getting protocol : hostname and port<br> from server appended with url provided.
     *
     * @param api
     *         , string of API
     *
     * @return url complete url to request an api
     */
    private String prepareURL( String api ) {
        if ( restAPI != null ) {
            return restAPI.getProtocol() + restAPI.getHostname() + ConstantsString.COLON + restAPI.getPort() + api;
        }

        return ConstantsString.EMPTY_STRING;

    }

    /**
     * Sets the rest api.
     *
     * @param restAPI
     *         the new rest api
     */
    public void setRestAPI( RestAPI restAPI ) {
        this.restAPI = restAPI;
    }

    private void transferObjects( Notification notif, List< String > locationIds, List< String > objectIds, String transferOption ) {

        for ( final String objectId : objectIds ) {

            for ( final String locationId : locationIds ) {
                // submit transfer location
                final TransferObject transferObject = new TransferObject();
                transferObject.setObjectId( objectId );
                transferObject.setTargetLocationId( locationId );
                transferObject.setOperationType( TransferOperationType.valueOf( transferOption ) );
                final String josnObjectDTO = JsonUtils.toJsonString( transferObject );
                SusResponseDTO responseDTO = null;
                try {
                    responseDTO = SuSClient.postRequest( prepareURL( API_DATA_TRANSFER_OBJECT ), josnObjectDTO, prepareHeaders() );
                } catch ( final Exception e ) {
                    log.error( e.getMessage(), e );
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, MessageBundleFactory.getDefaultMessage(
                            Messages.OBJECT_ID_FAILED_TO_TRANSFER.getKey() + " at location: " + locationId, objectId ) ) );
                }
                if ( responseDTO == null ) {
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, MessageBundleFactory.getDefaultMessage(
                            Messages.OBJECT_ID_FAILED_TO_TRANSFER.getKey() + " at location: " + locationId, objectId ) ) );
                } else if ( !responseDTO.getSuccess() ) {
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, responseDTO.getMessage().getContent() ) );
                    notif.addError( new Error( responseDTO.getMessage().getContent() ) );
                } else {
                    final TransferResult transferResult = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ),
                            TransferResult.class );
                    if ( transferResult.hasErrors() ) {
                        for ( final String error : transferResult.getErrors() ) {
                            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, error ) );
                        }

                    }
                    if ( transferResult.hasInfos() ) {
                        for ( final String info : transferResult.getInfos() ) {
                            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, info ) );
                        }
                    }

                    elementOutput.setCountOfCopiedFiles(
                            elementOutput.getCountOfCopiedFiles() + transferResult.getTransferedObjects().size() );
                    elementOutput.setCountOfCopiedObjects(
                            elementOutput.getCountOfCopiedObjects() + transferResult.getTransferedObjects().size() );
                    elementOutput.getCopiedObjects()
                            .addAll( JsonUtils.jsonToList( JsonUtils.toJson( transferResult.getTransferedObjects() ), ObjectDTO.class ) );
                }

            }

        }

    }

    /**
     * Validates user inputs.
     *
     * @param notif
     *         the notif
     * @param copyOrMove
     *         the over write
     * @param objectSelectionId
     *         the project id
     * @param targetLocation
     *         the zip or dir path to import
     */
    private void validateUserInputs( final Notification notif, String copyOrMove, final String objectSelectionId,
            final String targetLocation ) {

        if ( INVALID_OBJECT_ID.equals( objectSelectionId ) || StringUtils.isEmpty( objectSelectionId ) ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, LABEL_OF_OBJECT_OR_CONTAINER ) ) );
        }

        if ( INVALID_OBJECT_ID.equals( targetLocation ) || StringUtils.isEmpty( targetLocation ) ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, LABEL_OF_TARGET_LOCATION ) ) );
        }

        if ( StringUtils.isEmpty( copyOrMove ) ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, LABEL_OF_OPERATION_TYPE_FLAG ) ) );
        }
    }

    /**
     * Write out put file.
     */
    private void writeOutPutFile( String exitCode ) {
        addExistingOutputFileToWorkflowOutput();

        elementOutput.setExitCode( exitCode );
        workflowOutput.putIfAbsent( element.getName(), elementOutput );
        final String json = JsonUtils.objectToJson( workflowOutput );
        final File file = job.getElementOutput();

        try ( FileOutputStream fos = new FileOutputStream( file );
                final Writer fileWriter = new OutputStreamWriter( fos, StandardCharsets.UTF_8 ) ) {

            fileWriter.write( json );
            fileWriter.flush();
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

}
