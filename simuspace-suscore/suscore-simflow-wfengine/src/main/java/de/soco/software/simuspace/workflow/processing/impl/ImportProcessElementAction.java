package de.soco.software.simuspace.workflow.processing.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
import de.soco.software.simuspace.workflow.model.impl.HtmlObjectDTO;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ObjectDTO;
import de.soco.software.simuspace.workflow.model.impl.ObjectFile;
import de.soco.software.simuspace.workflow.model.impl.ObjectVersion;
import de.soco.software.simuspace.workflow.model.impl.PredictionModelDTO;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.TraceObjectDTO;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.properties.EnginePropertiesManager;
import de.soco.software.simuspace.workflow.util.ElementOutputImport;
import de.soco.software.simuspace.workflow.util.ImportObject;
import de.soco.software.simuspace.workflow.util.ImportRoutine;
import de.soco.software.simuspace.workflow.util.JobLog;
import de.soco.software.simuspace.workflow.util.WorkflowDefinitionUtil;

/**
 * This Class designed to execute a project import element to create a process in existing simcore project.
 *
 * @author Nasir.Farooq
 */
@Log4j2
public class ImportProcessElementAction extends WFElementAction {

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
     * The Constant WorkFlowLogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The label of container field in designer.
     */
    public static final String LABEL_OF_CONTAINER_FIELD_IN_DESIGNER = "Import to container";

    /**
     * The Constant LABEL_OF_INPUT_DIRECTORY.
     */
    public static final String LABEL_OF_INPUT_DIRECTORY = "Import from path";

    /**
     * The Constant LABEL_OF_IMPORT_SETTINGS_FILE.
     */
    public static final String LABEL_OF_IMPORT_SETTINGS_FILE = "Import Settings File";

    /**
     * The Constant LABEL_OF_OVER_WRITE_FLAG.
     */
    public static final String LABEL_OF_OVER_WRITE_FLAG = "Over-write";

    /**
     * The Constant URL_UPLOAD_FILE.
     */
    public static final String URL_UPLOAD_FILE = "/api/v1/document/upload";

    /**
     * The Constant URL_FOR_START_IMPORT_PROCESS.
     */
    public static final String URL_FOR_START_IMPORT_PROCESS = "/api/v1/process/import";

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

    private static final String DATA_URL = "/api/data/";

    private static final String OBJECT_URL = "/object/";

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
    public ImportProcessElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Instantiates a new import process element action.
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
    public ImportProcessElementAction( Job job, UserWFElement element, Map< String, Object > parameters, RestAPI restApi,
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
            final String settingFileImportInfo = getFieldPathByLabel( notif, LABEL_OF_IMPORT_SETTINGS_FILE );
            final String overWrite = getOverWriteFromUserFields();

            log.debug( "selectionId: " + selectionId );
            log.debug( "zipOrDirPathToImport: " + directoryPathToImport );
            log.debug( "settingFileImportInfo: " + settingFileImportInfo );
            log.debug( "overWrite: " + overWrite );

            // get selected container id from selected id
            List< String > selectedContainerId = getSelectedIdsBySelection( selectionId );

            if ( selectedContainerId.isEmpty() ) {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.IMPORT_ELEMENT_PROPERTIES_NOT_VALID ) ) );
            } else {
                // check create object permission
                validateRights( job, selectedContainerId.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
            }

            validateUserInputs( notif, overWrite, selectionId, directoryPathToImport, settingFileImportInfo );

            if ( notif.hasErrors() ) {
                return notif;
            }

            final String targetId = getTargetIdBySelectionId( selectionId, notif );
            if ( notif.hasErrors() ) {
                return notif;
            }

            final ProjectConfiguration targetProjectConfiguration = getProjectConfigurations( targetId );
            if ( targetProjectConfiguration == null ) {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.PROJECT_CONFIGURATIONS_NOT_FOUND_FOR_OBJECT, targetId ) ) );
                return notif;
            }
            log.debug( "targetProjectConfiguration: " + targetProjectConfiguration.getName() + " "
                    + targetProjectConfiguration.getObjectModel().getName() );

            log.debug( "targetId: " + targetId );
            final ImportObject parentImportObject = prepareDataForUpload( notif, directoryPathToImport, settingFileImportInfo,
                    targetProjectConfiguration );

            if ( notif.hasErrors() ) {
                return notif;
            }

            final List< FileInfo > filesInPrent = WorkflowDefinitionUtil.getServerFiles(
                    SuSClient.getRequest( prepareURL( API_DATA_PROJECT ) + targetId + DATA_LIST, prepareHeaders() ) );

            for ( final ImportObject importObject : parentImportObject.getChildren() ) {
                validateIfImportTargetCanContainImportObject( importObject, targetProjectConfiguration, notif );
                if ( null != importObject.getObjectType() && !importObject.getObjectType().equalsIgnoreCase( ImportRoutine.IGNORE ) ) {
                    try {
                        startUploadFiles( targetId, importObject, targetProjectConfiguration, filesInPrent, overWrite );
                    } catch ( Exception e ) {
                        log.info( importObject.getName() + ConstantsString.SPACE + UPLOAD_FAILED, e );
                        wfLogger.info( importObject.getName() + ConstantsString.SPACE + UPLOAD_FAILED );
                    }
                }
            }
            elementOutput.setCountCreatedObjects( elementOutput.getCreatedobjects().size() );

        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }
        log.debug( "leaving doAction()" );
        return notif;
    }

    /**
     * Validate if import target can contain parent.
     *
     * @param importObject
     *         the parent import object
     * @param targetProjectConfiguration
     *         the target project configuration
     * @param notif
     *         the notif
     */
    private void validateIfImportTargetCanContainImportObject( ImportObject importObject, ProjectConfiguration targetProjectConfiguration,
            Notification notif ) {
        SuSObjectModel importObjectModel = targetProjectConfiguration.getEntityConfig().stream()
                .filter( model -> importObject.getObjectType().equals( model.getName() ) ).findFirst().orElse( null );
        if ( StringUtils.isBlank( importObject.getObjectType() ) ) {
            // skip validation if type not defined in settings
            return;
        }
        if ( importObjectModel == null ) {
            notif.addError( new Error(
                    "target container configuration " + targetProjectConfiguration.getName() + " does not have selected type "
                            + importObject.getObjectType() ) );
        } else if ( !targetProjectConfiguration.getObjectModel().getContains().contains( importObjectModel.getId() ) ) {
            notif.addError( new Error(
                    "target container " + targetProjectConfiguration.getObjectModel().getName() + " can not contain selected type "
                            + importObjectModel.getName() ) );
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
                log.error( "Executor shutdown interrupted.", e );
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
                wfLogger.info( EXE_ELEM_COMPL + element.getName() );
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
                executedElementsIds.add( element.getId() );
                if ( !job.isFileRun() ) {
                    JobLog.updateLogAndProgress( job, executedElementsIds.size() );
                }
            } catch ( final Exception e ) {
                log.error( "Import Element Execution Error in Thread: ", e );
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
    private String getTargetIdBySelectionId( String selectionId, Notification notification ) {
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
     * @param settingFileImportInfo
     *         the setting file import info
     * @param projectConfiguration
     *         the project configuration
     *
     * @return the file upload
     */
    private ImportObject prepareDataForUpload( final Notification notif, final String directoryPath, final String settingFileImportInfo,
            ProjectConfiguration projectConfiguration ) {
        log.debug( "going to checkIsDirectory: " + directoryPath );

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
        final ImportObject objectToImport = ImportRoutine.scanDirectoryWithImportSettingsAndPrepareImportObjects(
                directoryPathWithSingleSlash, settingFileImportInfo, projectConfiguration );
        log.debug( "scanned: " + directoryPathWithSingleSlash );
        log.debug( "scanned directory found parent object: " + objectToImport );

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
        String jsonObjectDTO = getObjectJsonString( containerId, objectToImport, projectConfiguration, objectModel, objectFile );

        if ( isProject ) {
            responseDTO = SuSClient.postRequest( prepareURL( API_DATA_PROJECT ), jsonObjectDTO, prepareHeaders() );
        } else {
            responseDTO = SuSClient.postRequest( prepareURL( DATA_OBJECT_URL ) + containerId + TYPE + objectModel.getId(), jsonObjectDTO,
                    prepareHeaders() );
        }
        return responseDTO;
    }

    /**
     * Get object json string.
     *
     * @param containerId
     *         the container id
     * @param objectToImport
     *         the object to import
     * @param projectConfiguration
     *         the project configuration
     * @param objectModel
     *         the object model
     * @param objectFile
     *         the object file
     *
     * @return the sus response DTO
     */
    private String getObjectJsonString( String containerId, ImportObject objectToImport, final ProjectConfiguration projectConfiguration,
            final SuSObjectModel objectModel, ObjectFile objectFile ) {

        ObjectDTO objectDTO = new ObjectDTO();

        if ( objectToImport.getObjectType().equalsIgnoreCase( "Trace" ) ) {
            objectDTO = new TraceObjectDTO( getTraceSettingsFromSusttFile( objectToImport ) );
        } else if ( objectToImport.getObjectType().equalsIgnoreCase( "PredictionModel" ) ) {
            objectDTO = new PredictionModelDTO( uploadPredictionJsonFile( objectToImport ), uploadPredictionBinFile( objectToImport ) );
        } else if ( objectToImport.getObjectType().equalsIgnoreCase( "Html" ) ) {
            objectDTO = new HtmlObjectDTO( getZipFileForHtml( objectToImport ) );
        }
        objectDTO.setName( checkAndRemoveExtensionFromName( objectToImport.getName(), objectModel.getClassName() ) );
        objectDTO.setFile( objectFile );
        objectDTO.setTypeId( objectModel.getId() );
        objectDTO.setSize( new File( objectToImport.getPath() ).length() + ConstantsString.EMPTY_STRING );
        objectDTO.setJobId( job.getId().toString() );
        objectDTO.setConfig( projectConfiguration.getName() );
        objectDTO.setParentId( containerId );
        objectDTO.setTypeId( objectModel.getId() );
        objectDTO.setType( PROJECT_TYPE );
        objectDTO.setCustomAttributes( objectToImport.getCustomAttributes() );
        return JsonUtils.toJsonString( objectDTO );
    }

    /**
     * Upload prediction json file.
     *
     * @param objectToImport
     *         the object to import
     *
     * @return the object file
     */
    private ObjectFile uploadPredictionJsonFile( ImportObject objectToImport ) {
        try {
            File sustmFile = new File( objectToImport.getPath() );
            String nameOnly = sustmFile.getName().substring( sustmFile.getName().lastIndexOf( "." ) );
            String jsonPath = sustmFile.getParent() + File.separator + sustmFile.getName().replace( nameOnly, "" ) + ".json";
            File jsonFile = new File( jsonPath );
            DocumentDTO documentDTO = SuSClient.uploadFileRequest( prepareURL( API_DOCUMENT_UPLOAD ), jsonFile, prepareDownloadHeaders() );
            return new ObjectFile( documentDTO.getId(), new ObjectVersion( 1 ), documentDTO.getName(), documentDTO.getPath(),
                    String.valueOf( documentDTO.getSize() ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return null;
    }

    /**
     * Upload prediction bin file.
     *
     * @param objectToImport
     *         the object to import
     *
     * @return the object file
     */
    private ObjectFile uploadPredictionBinFile( ImportObject objectToImport ) {
        try {
            File sustmFile = new File( objectToImport.getPath() );
            // get bin files in the sustmFile directory
            File[] files = sustmFile.getParentFile().listFiles( ( d, name ) -> name.endsWith( ".bin" ) );
            if ( files.length != 0 ) {
                DocumentDTO documentDTO = SuSClient.uploadFileRequest( prepareURL( API_DOCUMENT_UPLOAD ), files[ 0 ],
                        prepareDownloadHeaders() );
                return new ObjectFile( documentDTO.getId(), new ObjectVersion( 1 ), documentDTO.getName(), documentDTO.getPath(),
                        String.valueOf( documentDTO.getSize() ) );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return null;
    }

    /**
     * Gets the zip file for html.
     *
     * @param objectToImport
     *         the object to import
     *
     * @return the zip file for html
     */
    private DocumentDTO getZipFileForHtml( ImportObject objectToImport ) {
        File htmlJsonFile = new File( objectToImport.getPath() );
        String nameOnly = htmlJsonFile.getName().substring( htmlJsonFile.getName().lastIndexOf( "." ) );
        String zipPath = htmlJsonFile.getParent() + File.separator + htmlJsonFile.getName().replace( nameOnly, "" ) + ".zip";
        File zipFile = new File( zipPath );
        DocumentDTO documentDTO = null;
        if ( zipFile.exists() && !objectToImport.isFolder() ) {
            log.info( "ZipFilePath going to create document: " + zipFile.getPath() );
            documentDTO = SuSClient.uploadFileRequest( prepareURL( API_DOCUMENT_UPLOAD ), new File( zipFile.getPath() ),
                    prepareDownloadHeaders() );
            log.info( "Document created: " + documentDTO.getName() );
        }
        return documentDTO;
    }

    /**
     * Update trace object with sustt file.
     *
     * @param objectToImport
     *         the object to import
     *
     * @return the sus response DTO
     */
    private String getTraceSettingsFromSusttFile( ImportObject objectToImport ) {
        try {
            log.debug( "updating Trace object with sustt file" );
            File csvFile = new File( objectToImport.getPath() );

            String nameOnly = csvFile.getName().substring( csvFile.getName().lastIndexOf( "." ) );
            String susttPath = csvFile.getParent() + File.separator + csvFile.getName().replace( nameOnly, "" ) + ".sustt";
            File susttFile = new File( susttPath );
            if ( susttFile.exists() ) {
                String[] headersCsv = getCsvHeaderArrayByPath( csvFile );

                JSONParser parser = new JSONParser();
                Object obj = parser.parse( new FileReader( susttPath ) );
                JSONObject jsonObject = ( JSONObject ) obj;
                if ( jsonObject.containsKey( "data" ) ) {
                    JSONArray dataArray = ( JSONArray ) jsonObject.get( "data" );
                    for ( Object object : dataArray ) {
                        JSONObject dataIteration = ( JSONObject ) object;
                        if ( dataIteration.containsKey( "x" ) ) {
                            String x = ( String ) dataIteration.get( "x" );
                            if ( !findHeaderInArray( headersCsv, x ) ) {
                                jsonObject.put( "x", "" );
                            }
                        }
                        if ( dataIteration.containsKey( "y" ) ) {
                            String y = ( String ) dataIteration.get( "y" );
                            if ( !findHeaderInArray( headersCsv, y ) ) {
                                jsonObject.put( "y", "" );
                            }
                        }
                    }
                }

                return jsonObject.toString();
            }

        } catch ( Exception e ) {
            log.error( "trace object plot failed:" + e.getMessage(), e );
        }
        return null;
    }

    /**
     * Find header in array.
     *
     * @param headersCsv
     *         the headers csv
     * @param x
     *         the x
     *
     * @return true, if successful
     */
    private boolean findHeaderInArray( String[] headersCsv, String x ) {
        for ( String header : headersCsv ) {
            if ( header.equalsIgnoreCase( x.replace( "__", "" ) ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the csv header array by path.
     *
     * @param csvFile
     *         the csv file
     *
     * @return the csv header array by path
     *
     * @throws FileNotFoundException
     *         the file not found exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private String[] getCsvHeaderArrayByPath( File csvFile ) throws IOException {
        String header;
        try ( BufferedReader br = new BufferedReader( new FileReader( csvFile.getAbsolutePath() ) ) ) {
            header = br.readLine();
        }
        if ( header != null ) {
            return header.split( "," );
        }
        return new String[ 0 ];

    }

    /**
     * Save or update object.
     *
     * @param containerId
     *         the container id
     * @param objectToImport
     *         the object to import
     * @param targetProjectConfig
     *         the project configuration
     * @param serverFiles
     *         the server files
     * @param overWrite
     *         the over write
     *
     * @return the sus response DTO
     */
    private SusResponseDTO saveOrUpdateObject( String containerId, ImportObject objectToImport,
            final ProjectConfiguration targetProjectConfig, List< FileInfo > serverFiles, String overWrite ) {

        final List< SuSObjectModel > objectModels = targetProjectConfig.getEntityConfig();

        final SuSObjectModel objectModel = objectModels.stream()
                .filter( objectType -> objectType.getName().contentEquals( objectToImport.getObjectType() ) ).findFirst().orElse( null );

        boolean isProject = false;
        if ( StringUtils.isNotBlank( objectToImport.getObjectType() ) ) {
            isProject = objectModels.get( FIRST_INDEX ).getName().contentEquals( objectToImport.getObjectType() );
        }

        SusResponseDTO responseDTO = null;
        if ( objectModel != null ) {
            final FileInfo fileInfo = serverFiles.stream().filter( tmpFileInfo -> tmpFileInfo.getName()
                            .contentEquals( checkAndRemoveExtensionFromName( objectToImport.getName(), objectModel.getClassName() ) ) ).findFirst()
                    .orElse( null );

            ObjectFile objectFile = null;
            DocumentDTO documentDTO = null;
            if ( !objectToImport.isFolder() ) {
                log.info( "FilePath going to create document: " + objectToImport.getPath() );
                documentDTO = SuSClient.uploadFileRequest( prepareURL( API_DOCUMENT_UPLOAD ), new File( objectToImport.getPath() ),
                        prepareDownloadHeaders() );
                log.info( "Document created: " + documentDTO.getName() );
                objectFile = new ObjectFile( documentDTO.getId(), new ObjectVersion( 1 ), documentDTO.getName(), documentDTO.getPath(),
                        String.valueOf( documentDTO.getSize() ) );
                elementOutput.setCountCreatedFiles( elementOutput.getCountCreatedFiles() + 1 );

            }

            if ( fileInfo == null ) {
                responseDTO = saveObject( containerId, objectToImport, targetProjectConfig, objectModel, isProject, objectFile );
            } else if ( OVER_WRITE_EXISTING.contentEquals( overWrite ) ) {
                if ( objectToImport.isFolder() ) {
                    responseDTO = new SusResponseDTO( true, fileInfo );
                } else {
                    Map< String, DocumentDTO > docMap = new HashMap<>();
                    docMap.put( "file", documentDTO );
                    if ( objectToImport.getObjectType().equalsIgnoreCase( "Html" ) ) {
                        docMap.put( "zipFile", getZipFileForHtml( objectToImport ) );
                    }
                    final String jsonObjectDTO = JsonUtils.toJsonString( docMap );
                    String fileApi = DATA_URL + job.getId().toString() + OBJECT_URL + fileInfo.getId() + FILE_PATH;
                    responseDTO = SuSClient.putRequest( prepareURL( fileApi ), prepareHeaders(), jsonObjectDTO );
                    updateObjectSettings( fileInfo.getId(), objectToImport );
                }
            } else if ( SKIP_EXISTING.contentEquals( overWrite ) ) {
                wfLogger.info( SKIPPING_ALREADY_CREATED_OBJECT + fileInfo.getName() );
                log.info( SKIPPING_ALREADY_CREATED_OBJECT + fileInfo.getName() );
            }

        } else if ( StringUtils.isNotBlank( objectToImport.getObjectType() ) ) {
            wfLogger.info(
                    MessagesUtil.getMessage( WFEMessages.PROJECT_TYPE_NOT_FOUND_FOR_OBJECT_TYPE_NAME, objectToImport.getObjectType() ) );
            log.info(
                    MessagesUtil.getMessage( WFEMessages.PROJECT_TYPE_NOT_FOUND_FOR_OBJECT_TYPE_NAME, objectToImport.getObjectType() ) );

        } else {
            wfLogger.info( MessagesUtil.getMessage( WFEMessages.OBJECT_TYPE_NOT_FOUND_FOR_OBJECT_NAME, objectToImport.getName() ) );
            log.info( MessagesUtil.getMessage( WFEMessages.OBJECT_TYPE_NOT_FOUND_FOR_OBJECT_NAME, objectToImport.getName() ) );
        }

        return responseDTO;
    }

    /**
     * Updates object settings in case of overwrite.
     *
     * @param objectToImport
     *         the objectToImport
     */
    private void updateObjectSettings( String objectId, ImportObject objectToImport ) {
        try {
            if ( objectToImport.getObjectType().equalsIgnoreCase( "Trace" ) ) {
                String payload = getTraceSettingsFromSusttFile( objectToImport );
                if ( payload != null ) {
                    // update trace object settings from sustt file
                    SuSClient.putRequest( prepareURL( DATA_OBJECT_URL ) + objectId + "/plot", prepareHeaders(), payload );
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
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
     * @param targetProjectConfig
     *         the project configuration
     * @param serverFiles
     *         the server files
     * @param overWrite
     *         the over write
     */
    private void startUploadFiles( String containerId, ImportObject objectToImport, ProjectConfiguration targetProjectConfig,
            List< FileInfo > serverFiles, String overWrite ) {

        try {

            final Relation r = new Relation();
            r.setParent( UUID.fromString( containerId ) );
            r.setId( UUID.randomUUID() );
            r.setType( AuditTrailRelationType.RELATION_CREATED );

            String newParentId = containerId;

            final FileInfo fileInfo = serverFiles.stream()
                    .filter( tmpFileInfo -> tmpFileInfo.getName().contentEquals( objectToImport.getName() ) ).findFirst().orElse( null );

            final SusResponseDTO responseDTO = saveOrUpdateObject( containerId, objectToImport, targetProjectConfig, serverFiles,
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
                        startUploadFiles( newParentId, importObject, targetProjectConfig, filesInPrent, overWrite );
                    } catch ( Exception e ) {
                        log.info( importObject.getName() + ConstantsString.SPACE + UPLOAD_FAILED, e );
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
     * @param selectionId
     *         the project id
     * @param zipOrDirPathToImport
     *         the zip or dir path to import
     * @param settingFileImportInfo
     *         the setting file import info
     */
    private void validateUserInputs( final Notification notif, String overWrite, final String selectionId,
            final String zipOrDirPathToImport, final String settingFileImportInfo ) {

        if ( INVALID_CONTAINER_ID.equals( selectionId ) || StringUtils.isEmpty( selectionId ) ) {
            notif.addError(
                    new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, LABEL_OF_CONTAINER_FIELD_IN_DESIGNER ) ) );
        }

        if ( StringUtils.isEmpty( overWrite ) ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, LABEL_OF_OVER_WRITE_FLAG ) ) );
        }

        if ( StringUtils.isBlank( zipOrDirPathToImport ) ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, LABEL_OF_INPUT_DIRECTORY ) ) );
        }

        if ( StringUtils.isBlank( settingFileImportInfo ) ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, LABEL_OF_IMPORT_SETTINGS_FILE ) ) );
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

        try ( FileOutputStream fos = new FileOutputStream( file );
                final Writer fileWriter = new OutputStreamWriter( fos, StandardCharsets.UTF_8 ) ) {

            fileWriter.write( json );
            fileWriter.flush();
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Check and remove extension from name string.
     *
     * @param name
     *         the name
     * @param className
     *         the class name
     *
     * @return the string
     */
    public String checkAndRemoveExtensionFromName( String name, String className ) {
        if ( !name.contains( ConstantsString.DOT ) ) {
            return name;
        }
        String extension = name.substring( name.lastIndexOf( ConstantsString.DOT ) );
        var extensionsToRemove = EnginePropertiesManager.getFileExtensions( className );
        if ( CollectionUtils.isEmpty( extensionsToRemove ) ) {
            return name;
        } else if ( extensionsToRemove.contains( extension ) ) {
            return name.replace( extension, ConstantsString.EMPTY_STRING );
        }
        return name;
    }

}
