package de.soco.software.simuspace.workflow.processing.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.FileInfo;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectHtmlDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.ElementOutputExport;
import de.soco.software.simuspace.workflow.util.JobLog;
import de.soco.software.simuspace.workflow.util.WorkflowDefinitionUtil;

/**
 * This Class designed to execute a dataObject export element to download dataobject vault files from simcore.
 *
 * @author Ahsan Khan
 */
@Log4j2
public class SusDataObjectExportElementAction extends WFElementAction {

    /**
     * The Constant EXPORT_TYPE_FLAT.
     */
    private static final String EXPORT_TYPE_FLAT = "flat";

    /**
     * The Constant API_DATA_OBJECT.
     */
    private static final String API_DATA_OBJECT = "/api/data/object/";

    private static final String API_DATA_PROJECT = "/api/data/project/";

    private static final String DATA_LIST = "/data/list";

    private static final String VERSION = "/version/";

    private static final String DOWNLOAD = "/download";

    /**
     * The Constant API_SELECTION.
     */
    private static final String API_SELECTION = "/api/selection/";

    /**
     * The Constant API_DOCUMENT.
     */
    private static final String API_DOCUMENT = "/api/document/";

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -458055812168225241L;

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The Constant ELEMENT.
     */
    private static final String ELEMENT_STR = "Element : ";

    /**
     * The export path.
     */
    private static final String EXPORT_PATH = "Export Path";

    /**
     * The export by.
     */
    private static final String EXPORT_BY = "Export By";

    /**
     * The Constant TYPE.
     */
    private static final String TYPE = "Type";

    /**
     * The field name dataobject.
     */
    private static final String FIELD_NAME_DATAOBJECT = "DataObject";

    /**
     * The Constant PROJECT_PATH.
     */
    private static final String PROJECT_PATH = "Project Path";

    /**
     * The Constant QUERY.
     */
    private static final String NAME_FILTER = "Name Filter";

    /**
     * The Constant BREADCRUMB_VIEW_DATA_PROJECT.
     */
    private static final String BREADCRUMB_VIEW_DATA_PROJECT = "/api/breadcrumb/view/data/project/";

    /**
     * The Constant DEFULT_EXIT_CODE.
     */
    private static final String DEFULT_EXIT_CODE = "0";

    /**
     * The Constant EMPTY_STRING.
     */
    private static final String EMPTY_STRING = "";

    /**
     * The Constant AT_LOCATION
     */
    private static final String AT_LOCATION = "\tAt Location: ";

    /**
     * The Constant DATA_OBJECT_NAME
     */
    private static final String DATA_OBJECT_NAME = "DataObject: ";

    /**
     * The Constant REGEX_MATCH_FOUND
     */
    private static final String REGEX_MATCH_FOUND = "\tMatch found with: ";

    /**
     * The Constant NO_MATCH_FOUND
     */
    private static final String NO_MATCH_FOUND = "\tNo match found for : ";

    /**
     * The Constant INCORRECT_REGEX
     */
    private static final String INCORRECT_REGEX = "Incorrect Regular Expression: ";

    /**
     * The Constant TYPE_HTML.
     */
    private static final List< String > TYPE_HTML = Arrays.asList( "html",
            "Allgemeine Beschreibung und Filter-Keyworter, z. B. fur Hersteller, Fertigung, Anwendung, etc.; Verlinkung mit anderen BMW-Systemen",
            "Campus-Datensatz", "BMW-Datensatz", "Materialkarten", "ggf. weitere Datensatze", "HT", "RT", "LT" );

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
    final ElementOutputExport elementOutput = new ElementOutputExport();

    /**
     * Instantiates a new sus data object element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     */
    public SusDataObjectExportElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Instantiates a new sus data object element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     */
    public SusDataObjectExportElementAction( Job job, UserWFElement element, Map< String, Object > parameters, RestAPI restAPI,
            Set< String > executedElementIds ) {
        super( job, element, executedElementIds );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        this.restAPI = restAPI;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Compute local sync path.
     *
     * @param request
     *         the request
     *
     * @return the string
     */
    private String createDirectoryStructure( SusResponseDTO request ) {
        final StringBuilder path = new StringBuilder();
        final String json = JsonUtils.toJson( request.getData() );
        final List< BreadCrumbItemDTO > bcList = JsonUtils.jsonToList( json, BreadCrumbItemDTO.class );
        for ( final BreadCrumbItemDTO bc : bcList ) {
            path.append( File.separator );
            path.append( bc.getName() );
        }
        return path.toString();
    }

    /**
     * Do action.
     *
     * @return the notification
     */
    public Notification doAction() {
        Notification notif = new Notification();
        String exportBy = getExportByFieldValue();
        if ( exportBy.equals( "container" ) ) {
            notif = executeExportContainer();
        } else if ( exportBy.equals( "projectpath" ) ) {
            notif = executeExportProjectPath();
        }
        return notif;
    }

    /**
     * Export by field value.
     *
     * @return the string
     */
    private String getExportByFieldValue() {
        final Field< ? > exportByField = getField( EXPORT_BY );
        if ( exportByField != null ) {
            parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + ConstantsString.DOT
                            + exportByField.getName() + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES,
                    exportByField.getValue().toString() );

            return exportByField.getValue().toString();
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Execute export container.
     *
     * @return the notification
     */
    public Notification executeExportContainer() {
        final Notification notif = new Notification();
        if ( element != null ) {
            notif.addNotification( element.validateException() );
            if ( notif.hasErrors() ) {
                return notif;
            }
            final String exportPathValue = getExportPathValue( notif );
            final String selectionId = getSelectionId( notif );
            final String exportType = getExportType();

            if ( StringUtils.isBlank( exportType ) ) {
                notif.addError( new Error( MessageBundleFactory.getDefaultMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), TYPE ) ) );
            }

            final File downloadPath = new File( exportPathValue );
            if ( !downloadPath.exists() ) {
                notif.addError(
                        new Error( MessageBundleFactory.getDefaultMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), exportPathValue ) ) );
            } else if ( StringUtils.isNotEmpty( exportPathValue ) && ( selectionId != null ) ) {
                notif.addNotification( getFilesBySelectionId( selectionId, exportPathValue, exportType ) );
            } else {
                notif.addError(
                        new Error( MessageBundleFactory.getDefaultMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), exportPathValue ) ) );
            }
        } else {
            notif.addError( new Error( MessageBundleFactory.getDefaultMessage( Messages.OBJECT_CANT_BE_NULL.getKey() ) ) );
        }
        return notif;
    }

    /**
     * Execute export project path.
     *
     * @return the notification
     */
    public Notification executeExportProjectPath() {
        final Notification notif = new Notification();
        if ( element != null ) {
            notif.addNotification( element.validateException() );
            if ( notif.hasErrors() ) {
                return notif;
            }
            final String projectPathValue = getTextFieldValue( PROJECT_PATH, notif );
            final String queryValue = getTextFieldValue( NAME_FILTER, notif );
            String[] splitQuery = queryValue.split( ConstantsString.COMMA );
            List< Pattern > queryList = getPatternsFromQueries( splitQuery );
            final String exportPathValue = getExportPathValue( notif );
            final String exportType = getExportType();

            if ( StringUtils.isBlank( exportType ) ) {
                notif.addError( new Error( MessageBundleFactory.getDefaultMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), TYPE ) ) );
            }

            final File downloadPath = new File( exportPathValue );
            if ( !downloadPath.exists() ) {
                notif.addError(
                        new Error( MessageBundleFactory.getDefaultMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), exportPathValue ) ) );
            } else if ( StringUtils.isNotEmpty( exportPathValue ) ) {
                notif.addNotification( exportProjectPathFiles( projectPathValue, queryList, exportPathValue, exportType ) );
            } else {
                notif.addError(
                        new Error( MessageBundleFactory.getDefaultMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), exportPathValue ) ) );
            }
        } else {
            notif.addError( new Error( MessageBundleFactory.getDefaultMessage( Messages.OBJECT_CANT_BE_NULL.getKey() ) ) );
        }
        return notif;
    }

    /**
     * Get Regex Patterns From List of Queries
     *
     * @param splitQuery
     *         List of Queries
     *
     * @return List of Patterns
     */
    private List< Pattern > getPatternsFromQueries( String[] splitQuery ) {
        // pre-compiling regex patterns in comma separated queries
        List< Pattern > patterns = new ArrayList<>();
        for ( String query : splitQuery ) {
            try {
                patterns.add( Pattern.compile( query.trim() ) );
            } catch ( Exception e ) {
                wfLogger.info( INCORRECT_REGEX + query + ConstantsString.SPACE + e.getMessage() );
                log.info( INCORRECT_REGEX + query + ConstantsString.SPACE + e.getMessage(), e );
            }
        }
        return patterns;
    }

    /**
     * Export Project path files.
     *
     * @param projectPathValue
     *         the projectPathValue
     * @param query
     *         the query
     * @param exportPath
     *         the export path
     * @param exportType
     *         the export type
     *
     * @return the export project path files
     */
    private Notification exportProjectPathFiles( String projectPathValue, List< Pattern > query, String exportPath, String exportType ) {
        final Notification notification = new Notification();

        if ( !canWritePath( exportPath ) ) {
            notification.addError( new Error( MessageBundleFactory.getDefaultMessage( Messages.UNABLE_TO_EXPORT_TO_PATH.getKey() ) ) );
            return notification;
        }
        log.debug( "getting object info: " + projectPathValue );
        DataObjectDTO dataObjectDTO = getDataObjectsById( projectPathValue );

        if ( dataObjectDTO != null ) {
            try {
                final SusResponseDTO request = SuSClient.getRequest(
                        prepareURL( API_DATA_OBJECT + dataObjectDTO.getId().toString() + "/fileInfo" ), prepareHeaders() );
                final String json = JsonUtils.toJson( request.getData() );
                final FileInfo jsonToObject = JsonUtils.jsonToObject( json, FileInfo.class );
                if ( exportType.equals( EXPORT_TYPE_FLAT ) ) {
                    recursivelyDownloadFlatObjectsWithQuery( dataObjectDTO, jsonToObject.isDir(), exportPath, query );
                } else {
                    recursivelyDownloadStructuralObjectsWithQuery( dataObjectDTO, jsonToObject.isDir(), exportPath, query );
                }
            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
            }
        } else {
            notification.addError( new Error( "Project-ID is invalid" ) );
        }

        return notification;
    }

    /**
     * Download object.
     *
     * @param object
     *         the object
     * @param path
     *         the path
     */
    private void downloadObject( DataObjectDTO object, File path ) {
        DocumentDTO file = object.getFile();
        if ( !isOnlyOnRemoteLocation( object ) ) {
            SuSClient.downloadRequest( prepareURL( API_DOCUMENT + file.getId() + VERSION + file.getVersion().getId() + DOWNLOAD ),
                    path.getAbsolutePath(), prepareDownloadHeaders(), null );
        } else {
            SuSClient.downloadRequest(
                    prepareRemoteURL( object, API_DOCUMENT + file.getId() + VERSION + file.getVersion().getId() + DOWNLOAD ),
                    path.getAbsolutePath(), prepareDownloadHeaders(), null );
        }
    }

    private String prepareRemoteURL( DataObjectDTO object, String api ) {
        if ( null != object.getLocations() && !object.getLocations().isEmpty() ) {
            return object.getLocations().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getUrl() + api;
        }
        return ConstantsString.EMPTY_STRING;
    }

    private boolean isOnlyOnRemoteLocation( DataObjectDTO object ) {
        for ( LocationDTO locationDTO : object.getLocations() ) {
            if ( locationDTO.getUrl().contains( restAPI.getHostname() ) && locationDTO.getUrl().contains( restAPI.getPort() ) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DecisionObject execute() {

        try {

            final int executionTime = element.getExecutionValue();
            addLogForUnlimitedExecution( executionTime );

            final ExecutorService executor = Executors.newFixedThreadPool( ConstantsInteger.INTEGER_VALUE_ONE );

            executeDataobjectExportElement( executor, executionTime );

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
            elementOutput.setCountExportedObjects( elementOutput.getExportedObjects().size() );
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
     * Execute Dataobject export element.
     *
     * @param executor
     *         the executor
     * @param executionTime
     *         the execution time
     */
    private void executeDataobjectExportElement( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                executeElement();
            } catch ( final SusException e ) {
                log.error( "IO Element Execution Error in Thread: ", e );
                try {
                    wfLogger.error( ELEMENT_STR + element.getName() + ConstantsString.COLON + e.getMessage() );
                    JobLog.addLog( job.getId(),
                            new LogRecord( ConstantsMessageTypes.ERROR, ELEMENT_STR + element.getName() + ConstantsString.COLON + e ) );
                    updateJobAndStatusFailed( e );
                } catch ( final SusException e1 ) {
                    log.error( e1.getMessage(), e1 );
                }
                writeOutPutFile( EXIT_CODE_WITH_ERROR );
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
     * Actual execution of element.
     */
    private void executeElement() {
        JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
        final Notification notif = doAction();
        processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
        JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
        executedElementsIds.add( element.getId() );
        if ( !job.isFileRun() ) {
            JobLog.updateLogAndProgress( job, executedElementsIds.size() );
        }
    }

    /**
     * Gets the data object Ids by selection id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the data object Ids by selection id
     */
    private SusResponseDTO getDataObjectIDsBySelectionId( String selectionId ) {
        return SuSClient.getRequest( prepareURL( API_SELECTION + selectionId ), prepareHeaders() );
    }

    /**
     * Gets the data objects by id.
     *
     * @param objectId
     *         the object id
     *
     * @return the data objects by id
     */
    private DataObjectDTO getDataObjectsById( String objectId ) {
        try {

            final SusResponseDTO responseDTO = SuSClient.getRequest( prepareURL( API_DATA_OBJECT + objectId ), prepareHeaders() );
            final String documentDTOResponse = JsonUtils.objectToJson( responseDTO.getData() );
            return JsonUtils.jsonToObject( documentDTOResponse, DataObjectDTO.class );
        } catch ( Exception e ) {
            log.error( "Failed to get object: " + objectId, e );
            return null;
        }
    }

    /**
     * The method is used to get the export path value by replacing the variables in script with values provided in parameters.
     *
     * @param notif
     *         the notification
     *
     * @return String the String
     */
    private String getExportPathValue( Notification notif ) {
        final Field< ? > exportPathField = getField( EXPORT_PATH );
        String argObject = null;
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
        }
        log.info( "After replacement notifiez: " + notif.getErrors() );
        return argObject;
    }

    /**
     * The method is used to get the text field value by replacing the variables in script with values provided in parameters.
     *
     * @param notif
     *         the notification
     *
     * @return String the String
     */
    public String getTextFieldValue( String fieldLabel, Notification notif ) {
        final Field< ? > exportPathField = getField( fieldLabel );

        String argObject = null;

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
                if ( fieldLabel.equals( PROJECT_PATH ) && pathValue.contains( ConstantsString.FORWARD_SLASH ) ) {
                    argObject = pathValue.substring( ( pathValue.lastIndexOf( ConstantsString.FORWARD_SLASH ) ) + 1 );
                } else {
                    argObject = pathValue;
                }
            }
            parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + ConstantsString.DOT
                    + exportPathField.getName() + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, argObject );
        }

        log.info( "After replacement notifiez: " + notif.getErrors() );

        return argObject;
    }

    /**
     * Gets the field.
     *
     * @param label
     *         the label
     *
     * @return the field
     */
    public Field< ? > getField( String label ) {
        Field< ? > selection = null;
        for ( final Field< ? > elementField : element.getFields() ) {
            if ( elementField.getLabel().contentEquals( label ) ) {
                selection = elementField;
                break;
            }
        }
        return selection;
    }

    /**
     * Gets the export type.
     *
     * @return the export type
     */
    private String getExportType() {
        final Field< ? > exportType = getField( TYPE );
        if ( exportType != null ) {
            parameters.put(
                    ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + ConstantsString.DOT + exportType.getName()
                            + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, exportType.getValue().toString() );
            return exportType.getValue().toString();
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Gets the files by selection id.
     *
     * @param selectionId
     *         the selection id
     * @param exportPath
     *         the export path
     * @param exportType
     *         the export type
     *
     * @return the files by selection id
     */
    private Notification getFilesBySelectionId( String selectionId, String exportPath, String exportType ) {
        final Notification notification = new Notification();
        final SusResponseDTO susResponseDTO = getDataObjectIDsBySelectionId( selectionId );
        if ( susResponseDTO == null ) {
            notification.addError( new Error( MessageBundleFactory.getDefaultMessage( Messages.NO_OBJECTS_FOUND.getKey() ) ) );
            return notification;
        }

        if ( !canWritePath( exportPath ) ) {
            notification.addError( new Error( MessageBundleFactory.getDefaultMessage( Messages.UNABLE_TO_EXPORT_TO_PATH.getKey() ) ) );
            return notification;
        }

        if ( !susResponseDTO.getSuccess() ) {
            notification.addError( new Error( susResponseDTO.getMessage().getContent() ) );
            return notification;
        } else {
            final List< DataObjectDTO > dataObjectsResponsePayloadlist = new ArrayList<>();
            final List< String > dataObjectIds = ( List< String > ) susResponseDTO.getData();
            for ( final String dataObjectId : dataObjectIds ) {
                log.debug( "getting object info: " + dataObjectId );
                DataObjectDTO dataObjectDTO = getDataObjectsById( dataObjectId );
                if ( dataObjectDTO != null ) {
                    dataObjectsResponsePayloadlist.add( dataObjectDTO );
                }
            }
            log.info( "dataObjectsResponsePayloadlist size: " + dataObjectsResponsePayloadlist.size() );
            final List< String > downloadedObjects = new ArrayList<>();
            for ( final DataObjectDTO dataObjectDTO : dataObjectsResponsePayloadlist ) {
                if ( dataObjectDTO != null ) {
                    log.debug( "downloading object: " + dataObjectDTO.getId() );
                    try {
                        final SusResponseDTO request = SuSClient.getRequest(
                                prepareURL( API_DATA_OBJECT + dataObjectDTO.getId().toString() + "/fileInfo" ), prepareHeaders() );
                        final String json = JsonUtils.toJson( request.getData() );
                        final FileInfo jsonToObject = JsonUtils.jsonToObject( json, FileInfo.class );
                        if ( exportType.equals( EXPORT_TYPE_FLAT ) ) {
                            recursivelyDownloadFlatObjects( dataObjectDTO, jsonToObject.isDir(), exportPath );
                        } else {
                            recursivelyDownloadStructuralObjects( dataObjectDTO, jsonToObject.isDir(), exportPath );
                        }
                        downloadedObjects.add( dataObjectDTO.getName() );
                    } catch ( final Exception e ) {
                        log.error( e.getMessage(), e );
                    }
                }
            }
            wfLogger.info( "Total data objects downloaded :" + downloadedObjects.size() );
            wfLogger.info( downloadedObjects.toString() );
        }
        return notification;
    }

    /**
     * Download html data object files.
     *
     * @param dataObjectDTO
     *         the data object DTO
     * @param projectLocalDir
     *         the project local dir
     */
    private void downloadHtmlDataObjectFiles( DataObjectDTO dataObjectDTO, File projectLocalDir ) {
        DataObjectHtmlDTO dataObjectHtmlDto = getDataObjectHtmlById( dataObjectDTO.getId().toString() );
        projectLocalDir.mkdirs();

        if ( dataObjectHtmlDto.getFile() != null ) {
            downloadFile( dataObjectHtmlDto.getFile(), projectLocalDir );
        }
        if ( dataObjectHtmlDto.getZipFile() != null ) {
            downloadFile( dataObjectHtmlDto.getZipFile(), projectLocalDir );
        }
    }

    /**
     * Checks permission to write to path
     *
     * @param path
     *         the path
     *
     * @return the boolean
     */
    private boolean canWritePath( String path ) {
        final File f = new File( path );
        return f.canWrite();
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
     * Gets the selection id.
     *
     * @param notif
     *         the notif
     *
     * @return the selection id
     */
    private String getSelectionId( Notification notif ) {
        String selectionId = null;
        final Field< ? > field = getField( FIELD_NAME_DATAOBJECT );
        if ( field != null ) {
            final String dataObjectId = field.getValue().toString();
            try {
                selectionId = validateFieldAndGetVariableValueFromParameters( notif, dataObjectId );
            } catch ( final NumberFormatException ex ) {
                log.error( ex.getMessage(), ex );
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_VALUE_FOR_FIELD, FieldTypes.TEXT.getType() ) ) );
            }
            parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + ConstantsString.DOT + field.getName()
                    + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, dataObjectId );
        }

        return selectionId;
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
     * Recursively download flat objects.
     *
     * @param dataObjectDTO
     *         the data object dto
     * @param isDir
     *         the is dir
     * @param exportPath
     *         the export path
     */
    public void recursivelyDownloadFlatObjects( DataObjectDTO dataObjectDTO, boolean isDir, String exportPath ) {
        if ( CollectionUtils.isNotEmpty( dataObjectDTO.getLocations() ) ) {
            wfLogger.info( DATA_OBJECT_NAME + dataObjectDTO.getName() );
            log.info( DATA_OBJECT_NAME + dataObjectDTO.getName() );

            for ( final LocationDTO locationDTO : dataObjectDTO.getLocations() ) {
                wfLogger.info( AT_LOCATION + locationDTO.getName() );
                log.info( AT_LOCATION + locationDTO.getName() );
            }
        }
        final File projectLocalDir = new File( exportPath );
        if ( TYPE_HTML.stream().anyMatch( type -> dataObjectDTO.getType().equalsIgnoreCase( type ) ) ) {
            final File htmlLocalDir = new File( exportPath + File.separator + dataObjectDTO.getName() );
            downloadHtmlDataObjectFiles( dataObjectDTO, htmlLocalDir );
        } else if ( ( dataObjectDTO.getFile() != null ) && !isDir ) {
            downloadObject( dataObjectDTO, projectLocalDir );
        }
        if ( dataObjectDTO.getModifiedBy() != null ) {
            dataObjectDTO.getModifiedBy().setSusUserDirectoryDTO( null );
        }

        if ( dataObjectDTO.getCreatedBy() != null ) {
            dataObjectDTO.getCreatedBy().setSusUserDirectoryDTO( null );
        }

        elementOutput.getExportedObjects().add( dataObjectDTO );
        final List< FileInfo > serverFiles = WorkflowDefinitionUtil.getServerFiles(
                SuSClient.getRequest( prepareURL( API_DATA_PROJECT + dataObjectDTO.getId() + DATA_LIST ), prepareHeaders() ) );
        for ( final FileInfo fileInfo : serverFiles ) {
            final DataObjectDTO objectDTO = getDataObjectsById( fileInfo.getId() );
            if ( objectDTO != null ) {
                try {
                    recursivelyDownloadFlatObjects( objectDTO, fileInfo.isDir(), exportPath );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                }
            }
        }
    }

    /**
     * Gets the data object html by id.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object html by id
     */
    private DataObjectHtmlDTO getDataObjectHtmlById( String objectId ) {
        try {
            final SusResponseDTO responseDTO = SuSClient.getRequest( prepareURL( API_DATA_OBJECT + objectId ), prepareHeaders() );
            final String documentDTOResponse = JsonUtils.objectToJson( responseDTO.getData() );
            return JsonUtils.jsonToObject( documentDTOResponse, DataObjectHtmlDTO.class );
        } catch ( Exception e ) {
            log.error( "Failed to get object: " + objectId, e );
            return null;
        }
    }

    /**
     * Download file.
     *
     * @param file
     *         the file
     * @param path
     *         the path
     */
    private void downloadFile( DocumentDTO file, File path ) {
        SuSClient.downloadRequest( prepareURL( API_DOCUMENT + file.getId() + VERSION + file.getVersion().getId() + DOWNLOAD ),
                path.getAbsolutePath(), prepareDownloadHeaders(), null );
    }

    /**
     * Recursively download flat objects.
     *
     * @param dataObjectDTO
     *         the data object dto
     * @param isDir
     *         the is dir
     * @param exportPath
     *         the export path
     * @param query
     *         the query
     */
    public void recursivelyDownloadFlatObjectsWithQuery( DataObjectDTO dataObjectDTO, boolean isDir, String exportPath,
            List< Pattern > query ) {
        if ( CollectionUtils.isNotEmpty( dataObjectDTO.getLocations() ) ) {
            wfLogger.info( DATA_OBJECT_NAME + dataObjectDTO.getName() );
            log.info( DATA_OBJECT_NAME + dataObjectDTO.getName() );

            for ( final LocationDTO locationDTO : dataObjectDTO.getLocations() ) {
                wfLogger.info( AT_LOCATION + locationDTO.getName() );
                log.info( AT_LOCATION + locationDTO.getName() );
            }
        }
        final File projectLocalDir = new File( exportPath );
        if ( ( dataObjectDTO.getFile() != null ) && !isDir && doesDataObjectMatchTheQuery( dataObjectDTO, query ) ) {
            downloadObject( dataObjectDTO, projectLocalDir );
        }
        if ( dataObjectDTO.getModifiedBy() != null ) {
            dataObjectDTO.getModifiedBy().setSusUserDirectoryDTO( null );
        }

        if ( dataObjectDTO.getCreatedBy() != null ) {
            dataObjectDTO.getCreatedBy().setSusUserDirectoryDTO( null );
        }

        elementOutput.getExportedObjects().add( dataObjectDTO );
        final List< FileInfo > serverFiles = WorkflowDefinitionUtil.getServerFiles(
                SuSClient.getRequest( prepareURL( API_DATA_PROJECT + dataObjectDTO.getId() + DATA_LIST ), prepareHeaders() ) );
        for ( final FileInfo fileInfo : serverFiles ) {
            final DataObjectDTO objectDTO = getDataObjectsById( fileInfo.getId() );
            if ( objectDTO != null ) {
                try {
                    recursivelyDownloadFlatObjectsWithQuery( objectDTO, fileInfo.isDir(), exportPath, query );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                }
            }
        }
    }

    /**
     * Checks if data object matches the query.
     *
     * @param dataObjectDTO
     *         the data object dto
     * @param query
     *         the query
     *
     * @return the boolean
     */
    private boolean doesDataObjectMatchTheQuery( DataObjectDTO dataObjectDTO, List< Pattern > query ) {
        for ( Pattern regex : query ) {
            if ( regex.matcher( dataObjectDTO.getName() ).find() ) {
                wfLogger.info( REGEX_MATCH_FOUND + regex );
                log.info( REGEX_MATCH_FOUND + regex );
                return true;
            }
        }
        wfLogger.info( NO_MATCH_FOUND + dataObjectDTO.getName() );
        log.info( NO_MATCH_FOUND + dataObjectDTO.getName() );
        return false;
    }

    /**
     * Recursively download objects.
     *
     * @param dataObjectDTO
     *         the data object dto
     * @param isDir
     *         the is dir
     * @param exportPath
     *         the export path
     */
    public void recursivelyDownloadStructuralObjects( DataObjectDTO dataObjectDTO, boolean isDir, String exportPath ) {
        if ( CollectionUtils.isNotEmpty( dataObjectDTO.getLocations() ) ) {
            wfLogger.info( DATA_OBJECT_NAME + dataObjectDTO.getName() );
            log.info( DATA_OBJECT_NAME + dataObjectDTO.getName() );
            for ( final LocationDTO locationDTO : dataObjectDTO.getLocations() ) {
                wfLogger.info( AT_LOCATION + locationDTO.getName() );
                log.info( AT_LOCATION + locationDTO.getName() );
            }
        }
        final String relativeProjectPath = createDirectoryStructure(
                SuSClient.getRequest( prepareURL( BREADCRUMB_VIEW_DATA_PROJECT + dataObjectDTO.getId() ), prepareHeaders() ) );
        final File projectLocalDir = new File( exportPath + relativeProjectPath );

        if ( TYPE_HTML.stream().anyMatch( type -> dataObjectDTO.getType().equalsIgnoreCase( type ) ) ) {
            downloadHtmlDataObjectFiles( dataObjectDTO, projectLocalDir );
        } else if ( ( dataObjectDTO.getFile() != null ) && !isDir ) {
            final File filePath = new File( projectLocalDir.getPath()
                    .replace( projectLocalDir.getPath().substring( projectLocalDir.getPath().lastIndexOf( File.separator ) ),
                            ConstantsString.EMPTY_STRING ) );
            if ( !filePath.exists() ) {
                filePath.mkdirs();
            }
            downloadObject( dataObjectDTO, filePath );
        } else {
            projectLocalDir.mkdirs();
        }
        dataObjectDTO.getModifiedBy().setSusUserDirectoryDTO( null );
        dataObjectDTO.getCreatedBy().setSusUserDirectoryDTO( null );
        elementOutput.getExportedObjects().add( dataObjectDTO );
        final List< FileInfo > serverFiles = WorkflowDefinitionUtil.getServerFiles(
                SuSClient.getRequest( prepareURL( API_DATA_PROJECT + dataObjectDTO.getId() + DATA_LIST ), prepareHeaders() ) );
        for ( final FileInfo fileInfo : serverFiles ) {
            final DataObjectDTO objectDTO = getDataObjectsById( fileInfo.getId() );
            if ( objectDTO != null ) {
                try {
                    recursivelyDownloadStructuralObjects( objectDTO, fileInfo.isDir(), exportPath );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                }
            }
        }
    }

    /**
     * Recursively download objects.
     *
     * @param dataObjectDTO
     *         the data object dto
     * @param isDir
     *         the is dir
     * @param exportPath
     *         the export path
     * @param query
     *         the query
     */
    public void recursivelyDownloadStructuralObjectsWithQuery( DataObjectDTO dataObjectDTO, boolean isDir, String exportPath,
            List< Pattern > query ) {
        if ( CollectionUtils.isNotEmpty( dataObjectDTO.getLocations() ) ) {
            wfLogger.info( DATA_OBJECT_NAME + dataObjectDTO.getName() );
            log.info( DATA_OBJECT_NAME + dataObjectDTO.getName() );
            for ( final LocationDTO locationDTO : dataObjectDTO.getLocations() ) {
                wfLogger.info( AT_LOCATION + locationDTO.getName() );
                log.info( AT_LOCATION + locationDTO.getName() );
            }
        }
        final String relativeProjectPath = createDirectoryStructure(
                SuSClient.getRequest( prepareURL( BREADCRUMB_VIEW_DATA_PROJECT + dataObjectDTO.getId() ), prepareHeaders() ) );
        final File projectLocalDir = new File( exportPath + relativeProjectPath );
        if ( ( dataObjectDTO.getFile() != null ) && !isDir ) {
            final File filePath = new File( projectLocalDir.getPath()
                    .replace( projectLocalDir.getPath().substring( projectLocalDir.getPath().lastIndexOf( File.separator ) ),
                            ConstantsString.EMPTY_STRING ) );
            if ( !filePath.exists() ) {
                filePath.mkdirs();
            }
            if ( doesDataObjectMatchTheQuery( dataObjectDTO, query ) ) {
                downloadObject( dataObjectDTO, filePath );
            }
        } else {
            projectLocalDir.mkdirs();
        }
        dataObjectDTO.getModifiedBy().setSusUserDirectoryDTO( null );
        dataObjectDTO.getCreatedBy().setSusUserDirectoryDTO( null );
        elementOutput.getExportedObjects().add( dataObjectDTO );
        final List< FileInfo > serverFiles = WorkflowDefinitionUtil.getServerFiles(
                SuSClient.getRequest( prepareURL( API_DATA_PROJECT + dataObjectDTO.getId() + DATA_LIST ), prepareHeaders() ) );
        for ( final FileInfo fileInfo : serverFiles ) {
            final DataObjectDTO objectDTO = getDataObjectsById( fileInfo.getId() );
            if ( objectDTO != null ) {
                try {
                    recursivelyDownloadStructuralObjectsWithQuery( objectDTO, fileInfo.isDir(), exportPath, query );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                }
            }
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
     * validates the user provided scripts and Gets the single variable value from parameters provided by the connected elements.
     *
     * @param notif
     *         the notif
     * @param susUserScript
     *         the sus variable
     *
     * @return the single variable value
     */
    private String validateFieldAndGetVariableValueFromParameters( final Notification notif, String susUserScript ) {
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

        log.info( "After replacement notifiez: " + notif.getErrors() );
        return argObject.toString();
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