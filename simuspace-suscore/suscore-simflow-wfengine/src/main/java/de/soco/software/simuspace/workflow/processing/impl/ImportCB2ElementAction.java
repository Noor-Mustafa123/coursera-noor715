
/*
 *
 */

package de.soco.software.simuspace.workflow.processing.impl;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.AuditTrailRelationType;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.FileInfo;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.suscore.data.common.model.ProjectConfiguration;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ObjectDTO;
import de.soco.software.simuspace.workflow.model.impl.ObjectFile;
import de.soco.software.simuspace.workflow.model.impl.ObjectVersion;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.properties.EnginePropertiesManager;
import de.soco.software.simuspace.workflow.util.ElementOutputImport;
import de.soco.software.simuspace.workflow.util.ImportCB2Routine;
import de.soco.software.simuspace.workflow.util.ImportObject;
import de.soco.software.simuspace.workflow.util.JobLog;
import de.soco.software.simuspace.workflow.util.WorkflowDefinitionUtil;

/**
 * This Class designed to execute a project import cb2 element to create a process in existing simcore project.
 *
 * @author noman , zeeshan
 */
@Log4j2
public class ImportCB2ElementAction extends WFElementAction {

    /**
     * The Constant OVER_WRITE_EXISTING.
     */
    private static final String OVER_WRITE_EXISTING = "Yes";

    /**
     * The Constant SKIPPING_ALREADY_CREATED_OBJECT.
     */
    private static final String SKIPPING_ALREADY_CREATED_OBJECT = "Skipping already created object: ";

    /**
     * The Constant PROJECT_TYPE.
     */
    private static final String PROJECT_TYPE = "Data";

    /**
     * The Constant MULTIPLE_OBJECTS_ARE_SELECTED_AS_PARENT.
     */
    private static final String MULTIPLE_OBJECTS_ARE_SELECTED_AS_PARENT = "Multiple objects are selected as parent.";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant SELECTED_OBJECT_SIZE.
     */
    private static final int SELECTED_OBJECT_SIZE = 1;

    /**
     * The Constant API_DATA_SELECTION.
     */
    private static final String API_DATA_SELECTION = "/api/selection/";

    /**
     * The Constant TYPE.
     */
    private static final String TYPE = "/type/";

    /**
     * The Constant API_DOCUMENT_UPLOAD.
     */
    private static final String API_DOCUMENT_UPLOAD = "/api/document/upload";

    /**
     * The Constant API_DATA_PROJECT_CONFIG.
     */
    private static final String API_DATA_PROJECT_CONFIG = "/api/data/project/config/";

    /**
     * The Constant DATA_LIST.
     */
    private static final String DATA_LIST = "/data/list";

    /**
     * The Constant LOG_MESSAGE_PRE_FIX_ELEMENT.
     */
    private static final String LOG_MESSAGE_PRE_FIX_ELEMENT = "Element : ";

    /**
     * The Constant INVALID_PROJECT_ID.
     */
    private static final String INVALID_CONTAINER_ID = "invalid_project_id";

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
     * The label of container field in designer.
     */
    public static final String LABEL_OF_CONTAINER_FIELD_IN_DESIGNER = "To container";

    /**
     * The Constant LABEL_OF_INPUT_DIRECTORY.
     */
    public static final String LABEL_OF_INPUT_DIRECTORY = "From path";

    /**
     * The Constant LABEL_OF_OVER_WRITE_FLAG.
     */
    public static final String LABEL_OF_OVER_WRITE_FLAG = "Over-write";

    /**
     * The Constant EMPTY_STRING.
     */
    private static final String EMPTY_STRING = "";

    /**
     * The Constant DATA_OBJECT_URL.
     */
    private static final String DATA_OBJECT_URL = "/api/data/object/";

    /**
     * The Constant API_DATA_PROJECT.
     */
    private static final String API_DATA_PROJECT = "/api/data/project/";

    /**
     * The Constant SKIP_EXISTING.
     */
    private static final String SKIP_EXISTING = "Skip";

    /**
     * The Constant FILE_PATH.
     */
    private static final String FILE_PATH = "/file";

    /**
     * The Constant UPLOAD_FAILED.
     */
    private static final String UPLOAD_FAILED = "Upload failed";

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
    final ElementOutputImport elementOutput = new ElementOutputImport();

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
    public ImportCB2ElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Instantiates a new import CB2 element action.
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
    public ImportCB2ElementAction( Job job, UserWFElement element, Map< String, Object > parameters, RestAPI restApi,
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
     * Creates the relation.
     *
     * @param relation
     *         the relation
     */
    private void createRelation( Relation relation ) {
        SuSClient.postRequest( prepareURL( "/api/workflow/create/relation" ), JsonUtils.objectToJson( relation ), prepareHeaders() );
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

            final String selectionId = getSelectionIdFromUserFields( notif );
            final String directoryPathToImport = getFieldPathByLabel( notif, LABEL_OF_INPUT_DIRECTORY );
            final String overWrite = getOverWriteFromUserFields();

            log.debug( "selectionId: " + selectionId );
            log.debug( "zipOrDirPathToImport: " + directoryPathToImport );
            log.debug( "overWrite: " + overWrite );

            // get selected container id from selected id
            List< String > selectedContainerId = getSelectedIdsBySelection( selectionId );
            // check create object permission
            validateRights( job, selectedContainerId.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );

            validateUserInputs( notif, overWrite, selectionId, directoryPathToImport );

            if ( notif.hasErrors() ) {
                return notif;
            }

            final String parentId = getParentIdBySelectionId( selectionId, notif );
            if ( notif.hasErrors() ) {
                return notif;
            }

            log.debug( "parentId: " + parentId );
            final ImportObject parentImportObject = prepareDataAndUpload( notif, directoryPathToImport );

            if ( notif.hasErrors() ) {
                return notif;
            }

            final ProjectConfiguration projectConfiguration = getProjectConfigurations( parentId );
            log.debug( "projectConfiguration: " + projectConfiguration );

            final List< FileInfo > filesInPrent = WorkflowDefinitionUtil.getServerFiles(
                    SuSClient.getRequest( prepareURL( API_DATA_PROJECT ) + parentId + DATA_LIST, prepareHeaders() ) );

            // save first container of PPO object : exp PPO or simuspace etc
            final SusResponseDTO responseDTO = saveOrUpdateObject( parentId, parentImportObject, projectConfiguration, filesInPrent,
                    overWrite );
            ObjectDTO firstContainerCB2 = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), ObjectDTO.class );
            // container relation saved
            saveContainerRelation( parentId, firstContainerCB2 );

            // extracting unique types from all childs
            Map< String, String > uniqueCB2ObjectTypes = new HashMap<>();
            for ( ImportObject filterChilds : parentImportObject.getChildren() ) {
                if ( !uniqueCB2ObjectTypes.containsKey( filterChilds.getObjectType() ) ) {
                    uniqueCB2ObjectTypes.put( filterChilds.getObjectType(), filterChilds.getObjectType() );
                }
            }

            // preparing object list to upload all PostResult type Containers and their childs
            List< ImportObject > listToUpload = new ArrayList<>();
            for ( Map.Entry< String, String > uniqueTypes : uniqueCB2ObjectTypes.entrySet() ) {
                ImportObject importObj = new ImportObject();
                List< ImportObject > childsToUniqueList = new ArrayList<>();
                importObj.setFolder( true );
                importObj.setName( uniqueTypes.getKey() );
                importObj.setParent( parentImportObject.getParent() );
                importObj.setPath( parentImportObject.getPath() );
                importObj.setObjectType( "PostResult" );

                for ( ImportObject getChilds : parentImportObject.getChildren() ) {

                    // this condition is only for KeyResultDocument
                    if ( getChilds.getObjectType().equals( uniqueTypes.getKey() ) && getChilds.getObjectType()
                            .equals( "KeyResultDocument" ) ) {
                        if ( getChilds.getPath() != null && de.soco.software.simuspace.suscore.common.util.StringUtils.isNullOrEmpty(
                                getChilds.getGroup() ) ) {
                            childsToUniqueList.add( getChilds );
                        }
                    } else if ( getChilds.getObjectType().equals( uniqueTypes.getKey() ) ) {
                        childsToUniqueList.add( getChilds );
                    }
                }
                importObj.setChildren( childsToUniqueList );
                listToUpload.add( importObj );
            }

            // getting serverFiles to validate
            final List< FileInfo > filesInPrent2 = WorkflowDefinitionUtil.getServerFiles(
                    SuSClient.getRequest( prepareURL( API_DATA_PROJECT ) + parentId + DATA_LIST, prepareHeaders() ) );
            // validating and uploading objects
            for ( ImportObject importObjectUpload : listToUpload ) {
                try {
                    startUploadFiles( firstContainerCB2.getId(), importObjectUpload, projectConfiguration, filesInPrent2, overWrite );
                } catch ( Exception e ) {
                    log.error( importObjectUpload.getName() + ConstantsString.SPACE + UPLOAD_FAILED, e );
                    wfLogger.info( importObjectUpload.getName() + ConstantsString.SPACE + UPLOAD_FAILED );
                }
                elementOutput.setCountCreatedObjects( elementOutput.getCreatedobjects().size() );
            }

        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }
        return notif;
    }

    /**
     * Save container relation.
     *
     * @param parentId
     *         the parent id
     * @param firstContainerCB2
     *         the first container CB 2
     */
    private void saveContainerRelation( final String parentId, ObjectDTO firstContainerCB2 ) {
        final Relation r = new Relation();
        r.setParent( UUID.fromString( parentId ) );
        r.setId( UUID.randomUUID() );
        r.setType( AuditTrailRelationType.RELATION_CREATED );
        r.setChild( UUID.fromString( firstContainerCB2.getId() ) );
        // saving relation
        createRelation( r );
    }

    /**
     * Validate rights.
     *
     * @param job
     *         the job
     * @param workflowId
     *         the workflow id
     */
    private void validateRights( Job job, String workflowId ) {

        final String permUrl =
                job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                        + "/api/system/permissions/permitted";
        final SusResponseDTO request = SuSClient.postRequest( permUrl,
                workflowId + ConstantsString.COLON + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(
                        EnginePropertiesManager.hasTranslation(), job.getRequestHeaders().getToken() ),
                prepareHeaders( job.getRequestHeaders() ) );
        if ( !Boolean.parseBoolean( request.getData().toString() ) ) {
            throw new SusException(
                    MessageBundleFactory.getDefaultMessage( Messages.NO_RIGHTS_TO_CREATE.getKey(), job.getWorkflow().getName() ) );
        }
    }

    /**
     * Gets the selected ids by selection.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the selected ids by selection
     */
    private List< String > getSelectedIdsBySelection( final String selectionId ) {
        SusResponseDTO request = SuSClient.getRequest(
                job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                        + API_DATA_SELECTION + selectionId, prepareHeaders( job.getRequestHeaders() ) );

        return JsonUtils.jsonToList( JsonUtils.toJson( request.getData() ), String.class );
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

            executeImportCB2Element( executor, executionTime );
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
    private void executeImportCB2Element( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
                final Notification notif = doAction();
                processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
                wfLogger.info( EXE_ELEM_COMPL + element.getName() );
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
     * The method is used to get the export path value by replacing the variables in script with values provided in parameters.
     *
     * @param notif
     *         the notification
     * @param label
     *         the label
     *
     * @return String the String
     */
    private String getFieldPathByLabel( Notification notif, String label ) {
        final Field< ? > exportPathField = getField( label );

        String argObject;

        if ( exportPathField != null ) {

            final String pathValue = exportPathField.getValue().toString();
            if ( exportPathField.isVariableMode() ) {

                if ( pathValue.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) ) {
                    cmd = pathValue;

                    final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( cmd ) );
                    if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
                        computeGlobalCmd( new Notification(), variablesIncludingDot );
                    }

                    final List< String > variables = getAllSimpleVariables( cmd );
                    if ( CollectionUtils.isNotEmpty( variables ) ) {
                        computeGlobalCmd( notif, variables );
                    }
                    argObject = cmd;
                } else {
                    argObject = pathValue;
                }
            } else {

                argObject = getServerFile( exportPathField );
            }

            parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + ConstantsString.DOT
                    + exportPathField.getName() + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, argObject );

        } else {
            argObject = ConstantsString.EMPTY_STRING;
        }

        log.info( "After replacement notifies: " + notif.getErrors() );
        return argObject;
    }

    /**
     * Gets the field value of element.
     *
     * @return the field value of element
     */
    private String getOverWriteFromUserFields() {

        String variableValue = ConstantsString.EMPTY_STRING;
        for ( final Field< ? > elementField : element.getFields() ) {
            if ( elementField.getLabel().contentEquals( LABEL_OF_OVER_WRITE_FLAG ) ) {
                variableValue = elementField.getValue().toString();
                parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + ConstantsString.DOT
                        + elementField.getName() + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, variableValue );
                break;
            }
        }

        return variableValue;
    }

    /**
     * Gets the files by selection id.
     *
     * @param selectionId
     *         the selection id
     * @param notification
     *         the notification
     *
     * @return the files by selection id
     */
    private String getParentIdBySelectionId( String selectionId, Notification notification ) {
        String parentId = "";
        final SusResponseDTO susResponseDTO = SuSClient.getRequest( prepareURL( API_DATA_SELECTION + selectionId ), prepareHeaders() );
        if ( susResponseDTO == null ) {
            notification.addError( new Error( MessageBundleFactory.getDefaultMessage( Messages.NO_OBJECTS_FOUND.getKey() ) ) );
            return parentId;
        }

        if ( !susResponseDTO.getSuccess() ) {
            notification.addError( new Error( susResponseDTO.getMessage().getContent() ) );
        } else {

            final String json = JsonUtils.toJson( susResponseDTO.getData() );
            final List< String > selectedIds = JsonUtils.jsonToList( json, String.class );
            if ( CollectionUtils.isNotEmpty( selectedIds ) && ( selectedIds.size() == SELECTED_OBJECT_SIZE ) ) {
                parentId = selectedIds.get( FIRST_INDEX );
            } else if ( CollectionUtils.isNotEmpty( selectedIds ) ) {
                notification.addError( new Error( MULTIPLE_OBJECTS_ARE_SELECTED_AS_PARENT ) );
            }

        }
        return parentId;
    }

    /**
     * Gets the project configurations.
     *
     * @param parentId
     *         the parent id
     *
     * @return the project configurations
     */
    private ProjectConfiguration getProjectConfigurations( final String parentId ) {
        ProjectConfiguration projectConfiguration = null;
        if ( !StringUtils.isEmpty( parentId ) ) {
            // get project configurations from server
            final SusResponseDTO request = SuSClient.getRequest( prepareURL( API_DATA_PROJECT_CONFIG + parentId ), prepareHeaders() );
            if ( request != null ) {
                projectConfiguration = JsonUtils.jsonToObject( JsonUtils.toJson( request.getData() ), ProjectConfiguration.class );
            }
        }

        return projectConfiguration;
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
    private String getSelectionIdFromUserFields( Notification notif ) {

        String selectionId = null;
        final Field< ? > field = getField( LABEL_OF_CONTAINER_FIELD_IN_DESIGNER );
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
     * Prepare data and upload.
     *
     * @param notif
     *         the notif
     * @param directoryPath
     *         the directory path
     *
     * @return the file upload
     */
    private ImportObject prepareDataAndUpload( final Notification notif, final String directoryPath ) {
        log.debug( "going to checkIsDirectoy: " + directoryPath );

        final Path sourcePath = Paths.get( directoryPath );
        if ( !sourcePath.toFile().exists() ) {
            notif.addError(
                    new Error( MessagesUtil.getMessage( WFEMessages.FILE_PATH_NOT_EXIST, sourcePath.toFile().getAbsolutePath() ) ) );
        } else if ( sourcePath.toFile().isFile() ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.PATH_TO_IMPORT_SHOULD_ZIP_OR_DIRECTORY, directoryPath ) ) );
        }
        final String directoryPathWithSingleSlash = directoryPath.replace( ConstantsString.PATH_SEPARATOR_WIN,
                ConstantsString.DELIMETER_FOR_WIN );
        log.debug( "going to scan directory: " + directoryPathWithSingleSlash );
        final ImportObject objectToImport = ImportCB2Routine
                .scanDirectoryWithImportSettingsAndPrepareImportObjects( directoryPathWithSingleSlash );
        log.debug( "scaned: " + directoryPathWithSingleSlash );
        log.debug( "scaned directory found object: " + objectToImport );

        return objectToImport;
    }

    /**
     * Prepare download headers.
     *
     * @return the map
     */
    private Map< String, String > prepareDownloadHeaders() {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, restAPI.getRequestHeaders().getToken() );
        requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, restAPI.getRequestHeaders().getJobAuthToken() );

        return requestHeaders;

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
     * Save object.
     *
     * @param containerId
     *         the container id
     * @param objectToImport
     *         the object to import
     * @param projectConfiguration
     *         the project configuration
     * @param objectModel
     *         the object model
     * @param isProject
     *         the is project
     * @param objectFile
     *         the object file
     *
     * @return the sus response DTO
     */
    private SusResponseDTO saveObject( String containerId, ImportObject objectToImport, final ProjectConfiguration projectConfiguration,
            final SuSObjectModel objectModel, boolean isProject, ObjectFile objectFile ) {
        SusResponseDTO responseDTO;

        final ObjectDTO objectDTO = new ObjectDTO( objectToImport.getName(), objectFile, objectModel.getId(),
                new File( objectToImport.getPath() ).length() + ConstantsString.EMPTY_STRING );
        objectDTO.setJobId( job.getId().toString() );
        objectDTO.setConfig( projectConfiguration.getName() );
        objectDTO.setParentId( containerId );
        objectDTO.setTypeId( objectModel.getId() );
        objectDTO.setType( PROJECT_TYPE );
        final String josnObjectDTO = JsonUtils.toJsonString( objectDTO );

        if ( isProject ) {
            responseDTO = SuSClient.postRequest( prepareURL( API_DATA_PROJECT ), josnObjectDTO, prepareHeaders() );
        } else {
            responseDTO = SuSClient.postRequest( prepareURL( DATA_OBJECT_URL ) + containerId + TYPE + objectModel.getId(), josnObjectDTO,
                    prepareHeaders() );
        }
        return responseDTO;
    }

    /**
     * Save or update object.
     *
     * @param containerId
     *         the container id
     * @param objectToImport
     *         the object to import
     * @param projectConfiguration
     *         the project configuration
     * @param serverFiles
     *         the server files
     * @param overWrite
     *         the over write
     *
     * @return the sus response DTO
     */
    private SusResponseDTO saveOrUpdateObject( String containerId, ImportObject objectToImport,
            final ProjectConfiguration projectConfiguration, List< FileInfo > serverFiles, String overWrite ) {

        final List< SuSObjectModel > objectModels = projectConfiguration.getEntityConfig();

        final SuSObjectModel objectModel = objectModels.stream()
                .filter( objectType -> objectType.getName().contentEquals( objectToImport.getObjectType() ) ).findFirst().orElse( null );

        boolean isProject = false;
        if ( StringUtils.isNotBlank( objectToImport.getObjectType() ) ) {
            isProject = objectModels.get( FIRST_INDEX ).getName().contentEquals( objectToImport.getObjectType() );
        }

        final FileInfo fileInfo = serverFiles.stream()
                .filter( tmpFileInfo -> tmpFileInfo.getName().contentEquals( objectToImport.getName() ) ).findFirst().orElse( null );
        SusResponseDTO responseDTO = null;
        if ( objectModel != null ) {
            ObjectFile objectFile = null;
            DocumentDTO documentDTO = null;
            if ( !objectToImport.isFolder() ) {

                log.info( "FilePath going to create document: " + objectToImport.getPath() );
                documentDTO = SuSClient.uploadFileRequest( prepareURL( API_DOCUMENT_UPLOAD ), new File( objectToImport.getPath() ),
                        prepareDownloadHeaders() );
                log.info( "Document created: " + documentDTO.getName() );
                objectFile = new ObjectFile( documentDTO.getId(), new ObjectVersion( 1 ), String.valueOf( documentDTO.getSize() ) );
                elementOutput.setCountCreatedFiles( elementOutput.getCountCreatedFiles() + 1 );
            }

            if ( fileInfo == null ) {
                responseDTO = saveObject( containerId, objectToImport, projectConfiguration, objectModel, isProject, objectFile );
            } else if ( OVER_WRITE_EXISTING.contentEquals( overWrite ) ) {
                if ( objectToImport.isFolder() ) {
                    responseDTO = new SusResponseDTO( true, fileInfo );
                } else {
                    final String josnObjectDTO = JsonUtils.toJsonString( documentDTO );
                    responseDTO = SuSClient.putRequest( prepareURL( DATA_OBJECT_URL ) + fileInfo.getId() + FILE_PATH, prepareHeaders(),
                            josnObjectDTO );
                }
            } else if ( SKIP_EXISTING.contentEquals( overWrite ) ) {
                wfLogger.info( SKIPPING_ALREADY_CREATED_OBJECT + fileInfo.getName() );
                log.info( SKIPPING_ALREADY_CREATED_OBJECT + fileInfo.getName() );
                responseDTO = new SusResponseDTO();
                responseDTO.setData( fileInfo );
            }

        } else {
            wfLogger.error(
                    MessagesUtil.getMessage( WFEMessages.PROJECT_TYPE_NOT_FOUND_FOR_OBJECT_TYPE_NAME, objectToImport.getObjectType() ) );
            log.error(
                    MessagesUtil.getMessage( WFEMessages.PROJECT_TYPE_NOT_FOUND_FOR_OBJECT_TYPE_NAME, objectToImport.getObjectType() ) );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                    LOG_MESSAGE_PRE_FIX_ELEMENT + element.getName() + ConstantsString.COLON + MessagesUtil.getMessage(
                            WFEMessages.PROJECT_TYPE_NOT_FOUND_FOR_OBJECT_TYPE_NAME, objectToImport.getObjectType() ) ) );

        }
        return responseDTO;
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

    /**
     * Start upload files.
     *
     * @param containerId
     *         the container id
     * @param objectToImport
     *         the object to import
     * @param projectConfiguration
     *         the project configuration
     * @param serverFiles
     *         the server files
     * @param overWrite
     *         the over write
     */
    private void startUploadFiles( String containerId, ImportObject objectToImport, ProjectConfiguration projectConfiguration,
            List< FileInfo > serverFiles, String overWrite ) {

        try {

            final Relation r = new Relation();
            r.setParent( UUID.fromString( containerId ) );
            r.setId( UUID.randomUUID() );
            r.setType( AuditTrailRelationType.RELATION_CREATED );

            String newParentId = containerId;

            final FileInfo fileInfo = serverFiles.stream()
                    .filter( tmpFileInfo -> tmpFileInfo.getName().contentEquals( objectToImport.getName() ) ).findFirst().orElse( null );

            final SusResponseDTO responseDTO = saveOrUpdateObject( containerId, objectToImport, projectConfiguration, serverFiles,
                    overWrite );

            if ( ( responseDTO != null ) && responseDTO.getSuccess() ) {
                final ObjectDTO object = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), ObjectDTO.class );
                object.setJobId( job.getId().toString() );
                elementOutput.getCreatedobjects().add( object );
                newParentId = object.getId();
                r.setChild( UUID.fromString( object.getId() ) );
            } else if ( responseDTO != null ) {
                log.info( responseDTO.getMessage().getContent() );
                wfLogger.info( responseDTO.getMessage().getContent() );
            } else if ( fileInfo != null ) {
                newParentId = fileInfo.getId();
                r.setChild( UUID.fromString( fileInfo.getId() ) );
            }
            // saving relation
            createRelation( r );

            List< FileInfo > filesInPrent = new ArrayList<>();
            if ( CollectionUtils.isNotEmpty( objectToImport.getChildren() ) ) {
                filesInPrent = WorkflowDefinitionUtil.getServerFiles(
                        SuSClient.getRequest( prepareURL( API_DATA_PROJECT ) + newParentId + DATA_LIST, prepareHeaders() ) );
            }
            if ( CollectionUtils.isNotEmpty( objectToImport.getChildren() ) && ( responseDTO != null ) && responseDTO.getSuccess() ) {
                for ( final ImportObject importObject : objectToImport.getChildren() ) {
                    try {
                        startUploadFiles( newParentId, importObject, projectConfiguration, filesInPrent, overWrite );
                    } catch ( Exception e ) {
                        log.error( importObject.getName() + ConstantsString.SPACE + UPLOAD_FAILED, e );
                        wfLogger.info( importObject.getName() + ConstantsString.SPACE + UPLOAD_FAILED );
                    }
                }
            }

        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Validates user inputs.
     *
     * @param notif
     *         the notif
     * @param overWrite
     *         the over write
     * @param slectionId
     *         the project id
     * @param zipOrDirPathToImport
     *         the zip or dir path to import
     */
    private void validateUserInputs( final Notification notif, String overWrite, final String slectionId,
            final String zipOrDirPathToImport ) {

        if ( INVALID_CONTAINER_ID.equals( slectionId ) || StringUtils.isEmpty( slectionId ) ) {
            notif.addError(
                    new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, LABEL_OF_CONTAINER_FIELD_IN_DESIGNER ) ) );
        }

        if ( StringUtils.isEmpty( overWrite ) ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, LABEL_OF_OVER_WRITE_FLAG ) ) );
        }

        if ( StringUtils.isBlank( zipOrDirPathToImport ) ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, LABEL_OF_INPUT_DIRECTORY ) ) );
        }

    }

    /**
     * Write out put file.
     *
     * @param exitCode
     *         the exit code
     */
    private void writeOutPutFile( String exitCode ) {
        addExistingOutputFileToWorkflowOutput();

        elementOutput.setExitCode( exitCode );
        workflowOutput.putIfAbsent( element.getName(), elementOutput );
        final String json = JsonUtils.objectToJson( workflowOutput );
        final File file = job.getElementOutput();

        try ( final FileWriter fileWriter = new FileWriter( file, false ) ) {

            fileWriter.write( json );
            fileWriter.flush();
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

}