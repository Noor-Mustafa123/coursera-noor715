package de.soco.software.simuspace.workflow.processing.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FileUpload;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
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
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.JobLog;

/**
 * The Class is designed to import files in a dataobject.
 *
 * @author ali.haider
 */
@Log4j2
public class FilesImportElementAction extends WFElementAction {

    /**
     * The Constant LIST_OF_FILES_FOR_FILTER.
     */
    private static final String LIST_OF_FILES_FOR_FILTER = "List of files for filter ";

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2404406292279910787L;

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The Constant for directory path.
     */
    private static final String DIRECTORY_PATH = "Directory Path";

    /**
     * The Constant for vault file name.
     */
    private static final String KEY_NAME = "name";

    /**
     * The Constant for key value of list of files.
     */
    private static final String FILES_KEY = "files";

    /**
     * The Constant ELEMENT.
     */
    private static final String ELEMENT = "Element : ";

    /**
     * The Constant for url of upload file.
     */
    private static final String URL_UPLOAD_FILE = "/api/v1/document/upload";

    /**
     * The Constant for url of add files in data object.
     */
    private static final String URL_ADD_FILES_IN_DATA_OBJECT = "/api/v1/dataobject/add/files/";

    /**
     * The Constant for url of vault file link.
     */
    private static final String URL_VAULT_FILE_LINK = "/api/v1/dataobject/vaultfiles/";

    /**
     * The first index.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant for response field data.
     */
    private static final String VAULT_FILE_RESPONSE_FIELD_DATA = "data";

    /**
     * The vault file payload.
     */
    private static final String VAULT_FILE_PAYLOAD = "{\"draw\":2,\"start\":0,\"length\":10,\"search\":[]}";

    /**
     * The files filter.
     */
    private static final String FILES_FILTER = "Files filter";

    /**
     * The OVERWRITE existing.
     */
    private static final String TARGET_ALREADY_EXISTS = "Target already exists";

    /**
     * The user field to skip donload of existing files.
     */
    private static final String SKIP_TO_IMPORT_FILES = "skip";

    /**
     * The field name dataobject.
     */
    private static final String FIELD_NAME_DATAOBJECT = "DataObject";

    /**
     * The download all files flag.
     */
    private static final String ALL_KEY_WORD_FLAG = "ALL";

    /**
     * The standard separator.
     */
    private static final String STANDARD_SEPARATOR = ",";

    /**
     * The all files flag.
     */
    private static final String ALL_FILES_FLAG = "*";

    /**
     * The Constant DEFULT_EXIT_CODE.
     */
    private static final String DEFULT_EXIT_CODE = "0";

    /**
     * The Constant UPLOAD_FAILED.
     */
    private static final String UPLOAD_FAILED = "Upload failed";

    /**
     * This is the element information coming from designer containing fields having information what to do with that element.
     */
    private final transient UserWFElement userWorkflowElement;

    /**
     * The rest API server credentials.
     */
    private transient RestAPI restAPI;

    /**
     * Instantiates a new files import element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     */
    public FilesImportElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        userWorkflowElement = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Instantiates a new files import element action.
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
    public FilesImportElementAction( Job job, UserWFElement element, Map< String, Object > parameters, RestAPI restApi,
            Set< String > executedElementIds ) {
        super( job, element, executedElementIds );
        this.job = job;
        userWorkflowElement = element;
        this.parameters = parameters;
        this.restAPI = restApi;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Adds the files in data object.
     *
     * @param dataObjectId
     *         the data object id
     * @param filesToAdd
     *         the files to add
     *
     * @return the notification
     */
    private Notification addFilesInDataObject( String dataObjectId, List< java.io.File > filesToAdd ) {
        final Notification notification = new Notification();
        final Map< String, List< FileUpload > > mapForDataobject = new HashMap<>();
        final List< FileUpload > listOfFiles = new ArrayList<>();
        for ( final java.io.File file : filesToAdd ) {
            log.info( MessagesUtil.getMessage( WFEMessages.UPLOADING_FILE, file.getName() ) );
            wfLogger.info( MessagesUtil.getMessage( WFEMessages.UPLOADING_FILE, file.getName() ) );

            try {
                final FileUpload uploaded = uploadFile( file.getAbsolutePath() );
                listOfFiles.add( uploaded );
            } catch ( Exception e ) {
                log.error( file.getName() + ConstantsString.SPACE + UPLOAD_FAILED, e );
                wfLogger.info( file.getName() + ConstantsString.SPACE + UPLOAD_FAILED );
            }
        }

        mapForDataobject.put( FILES_KEY, listOfFiles );
        log.info( "Map of list of files : " + mapForDataobject );

        final String payLoadForVaultFiles = JsonUtils.objectToJson( mapForDataobject );

        final SusResponseDTO susResponseDTO = SuSClient.putRequest( prepareURL( URL_ADD_FILES_IN_DATA_OBJECT + dataObjectId ),
                prepareHeaders(), payLoadForVaultFiles );
        log.info( "SUSRESPONSEDTO : " + susResponseDTO );
        if ( susResponseDTO == null ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.ADD_FILES_IN_DATAOBJECT_FAILED ) ) );
            return notification;
        }

        if ( !susResponseDTO.getSuccess() ) {
            notification.addError( new Error( susResponseDTO.getMessage().getContent() ) );
            return notification;
        }
        return notification;
    }

    /**
     * Do action.
     *
     * @return the notification
     */
    public Notification doAction() {
        Notification notif = new Notification();
        if ( userWorkflowElement != null ) {
            notif.addNotification( userWorkflowElement.validateException() );
            if ( notif.hasErrors() ) {
                return notif;
            }

            final String actionForAlreadyExisting = getFieldValueOfElement( FieldTypes.SELECTION.getType(), TARGET_ALREADY_EXISTS );
            final String fileFilters = getFieldValueOfElement( FieldTypes.TEXT.getType(), FILES_FILTER );
            final String dataObjectId = getFieldValueOfElement( FieldTypes.TEXT.getType(), FIELD_NAME_DATAOBJECT );
            final String directoryPath = getFieldValueOfElement( FieldTypes.TEXT.getType(), DIRECTORY_PATH );

            if ( StringUtils.isNotNullOrEmpty( actionForAlreadyExisting ) && StringUtils.isNotNullOrEmpty( fileFilters )
                    && StringUtils.isNotNullOrEmpty( dataObjectId ) && StringUtils.isNotNullOrEmpty( directoryPath ) ) {
                try {
                    notif = getFilesAndAddInDataobject( directoryPath, fileFilters, dataObjectId, actionForAlreadyExisting );

                } catch ( final SusException e ) {
                    log.error( e.getMessage(), e );
                }
            } else {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.SOME_FIELDS_ARE_MISSING ) ) );
            }
        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }
        return notif;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DecisionObject execute() {

        try {

            final int executionTime = userWorkflowElement.getExecutionValue();
            addLogForUnlimitedExecution( executionTime );

            final ExecutorService executor = Executors.newFixedThreadPool( ConstantsInteger.INTEGER_VALUE_ONE );

            executeFilesImportElement( executor, executionTime );
            // wait all unfinished tasks for 2 sec
            try {
                if ( !executor.awaitTermination( ConstantsInteger.INTEGER_VALUE_TWO, TimeUnit.SECONDS ) ) {
                    executor.shutdownNow();
                }
            } catch ( final InterruptedException e ) {
                log.error( "Executor shoutdown interrupted.", e );
                Thread.currentThread().interrupt();
            }

            writeOutPutParentFile( DEFULT_EXIT_CODE );
            setJobResultParameters();

            return new DecisionObject( true, userWorkflowElement.getKey(), parameters, workflowOutput );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            updateJobAndStatusFailed( e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Actual execution of element.
     */
    private void executeElement() {
        JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + userWorkflowElement.getName() ) );
        log.info( EXE_ELEMENT + userWorkflowElement.getName() );
        wfLogger.info( EXE_ELEMENT + userWorkflowElement.getName() );
        final Notification notif = doAction();
        processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
        log.info( EXE_ELEM_COMPL + userWorkflowElement.getName() );
        wfLogger.info( EXE_ELEM_COMPL + userWorkflowElement.getName() );
        JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + userWorkflowElement.getName() ) );
        executedElementsIds.add( userWorkflowElement.getId() );
        if ( !job.isFileRun() ) {
            JobLog.updateLogAndProgress( job, executedElementsIds.size() );
        }
    }

    /**
     * Execute files import element.
     *
     * @param executor
     *         the executor
     * @param executionTime
     *         the execution time
     */
    private void executeFilesImportElement( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                executeElement();
            } catch ( final SusException e ) {
                log.error( "IO Element Execution Error in Thread: ", e );
                try {
                    wfLogger.error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_TYPE, userWorkflowElement.getKey() )
                            + ConstantsString.PIPE_CHARACTER + ConstantsString.TAB_SPACE + MessagesUtil.getMessage(
                            WFEMessages.WORKFLOW_ELEMENT_NAME, userWorkflowElement.getName() ) + ConstantsString.PIPE_CHARACTER
                            + ConstantsString.TAB_SPACE + MessagesUtil.getMessage( WFEMessages.ERROR_MESSAGE, e.getMessage() ) );

                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                            ELEMENT + userWorkflowElement.getName() + ConstantsString.PIPE_CHARACTER + e ) );

                } catch ( final SusException e1 ) {

                    log.error( e1.getMessage(), e1 );
                }
                updateJobAndStatusFailed( e );
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
                wfLogger.info( MessagesUtil.getMessage( WFEMessages.ELEMENT_IS_GOING_TO_EXECUTE_FOR_SECONDS, userWorkflowElement.getName(),
                        executionTime ) );
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
            log.error( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, userWorkflowElement.getName() ), e );
            throw new SusRuntimeException( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, userWorkflowElement.getName() ) );
        }

    }

    /**
     * Gets the actual file for dataobject.
     *
     * @param actionForAlreadyExisting
     *         the action for already existing
     * @param maps
     *         the maps
     * @param files
     *         the files
     */
    private void getActualFileForDataobject( String actionForAlreadyExisting, List< Map< String, String > > maps,
            List< java.io.File > files ) {
        if ( actionForAlreadyExisting.equalsIgnoreCase( SKIP_TO_IMPORT_FILES ) ) {
            removeFilesAlreadyExists( maps, files );
        } else {
            final List< String > overWriteFiles = new ArrayList<>();
            for ( final Map< String, String > vaultFile : maps ) {
                for ( final java.io.File file : files ) {
                    if ( file.getName().equalsIgnoreCase( vaultFile.get( KEY_NAME ) ) ) {
                        overWriteFiles.add( file.getName() );
                    }
                }
            }
            log.info( MessagesUtil.getMessage( WFEMessages.GOING_TO_BE_OVERWRITE, overWriteFiles ) );
            wfLogger.info( MessagesUtil.getMessage( WFEMessages.GOING_TO_BE_OVERWRITE, overWriteFiles ) );
        }

    }

    /**
     * Gets the field value of element.
     *
     * @param selector
     *         the selector
     * @param label
     *         the label
     *
     * @return the field value of element
     */
    private String getFieldValueOfElement( String selector, String label ) {
        String selection = ConstantsString.EMPTY_STRING;
        for ( final Field< ? > elementField : userWorkflowElement.getFields() ) {
            if ( elementField.getType().contentEquals( selector ) && elementField.getLabel().contentEquals( label ) ) {
                selection = elementField.getValue().toString();
                break;
            }
        }
        return selection;
    }

    /**
     * Gets the files and add in dataobject.
     *
     * @param directoryPath
     *         the directory path
     * @param fileFilters
     *         the file filters
     * @param dataObjectId
     *         the data object id
     * @param actionForAlreadyExisting
     *         the action for already existing
     *
     * @return the files and add in dataobject
     */
    private Notification getFilesAndAddInDataobject( String directoryPath, String fileFilters, String dataObjectId,
            String actionForAlreadyExisting ) {
        final Notification notif = new Notification();
        List< Map< String, String > > maps = new ArrayList<>();
        final String fileFilterValue = replaceFilterTypeExpressionWithValue( notif, fileFilters );
        final String dataobjectValue = replaceFilterTypeExpressionWithValue( notif, dataObjectId );
        final String directoryPathValue = replaceFilterTypeExpressionWithValue( notif, directoryPath );
        final java.io.File folder = new java.io.File( directoryPathValue );
        if ( !folder.exists() && !folder.isDirectory() ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.DIRECTORY_PATH_DOES_NOT_EXIST ) ) );
            return notif;
        }
        final SusResponseDTO susResponseDTO = SuSClient.postRequest( prepareURL( URL_VAULT_FILE_LINK + dataobjectValue ),
                VAULT_FILE_PAYLOAD, prepareHeaders() );
        if ( susResponseDTO == null ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.VAULT_FILES_ARE_NOT_AVAILABLE ) ) );
            return notif;
        }
        if ( !susResponseDTO.getSuccess() ) {
            notif.addError( new Error( susResponseDTO.getMessage().getContent() ) );
            return notif;
        } else {
            try {
                maps = getMapFromVaultFiles( notif, maps, susResponseDTO );
            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
            }
        }
        final List< String > filesOfDataobject = new ArrayList<>();
        for ( final Map< String, String > map : maps ) {
            filesOfDataobject.add( map.get( KEY_NAME ) );
        }
        log.info( MessagesUtil.getMessage( WFEMessages.EXISTING_VAULT_FILES, filesOfDataobject ) );
        wfLogger.info( MessagesUtil.getMessage( WFEMessages.EXISTING_VAULT_FILES, filesOfDataobject ) );
        final List< java.io.File > filesList = getFilesFromFilter( folder, fileFilterValue );
        logFiles( filesList );
        getActualFileForDataobject( actionForAlreadyExisting, maps, filesList );
        log.info( "Files going to add in data object vault : " + filesList );
        wfLogger.info( "Files going to add in data object vault : " + filesList );
        notif.addNotification( addFilesInDataObject( dataobjectValue, filesList ) );
        return notif;
    }

    /**
     * Gets the files from filter.
     *
     * @param folder
     *         the folder
     * @param fileFilterValue
     *         the file filter value
     *
     * @return the files from filter
     */
    private List< java.io.File > getFilesFromFilter( java.io.File folder, String fileFilterValue ) {
        final List< java.io.File > files = new ArrayList<>();
        final String[] filterExtension = fileFilterValue.split( STANDARD_SEPARATOR );

        if ( ( ( filterExtension.length == ConstantsInteger.INTEGER_VALUE_ONE ) && fileFilterValue.equals( ALL_FILES_FLAG ) )
                || fileFilterValue.equalsIgnoreCase( ALL_KEY_WORD_FLAG ) ) {
            files.addAll( Arrays.asList( folder.listFiles() ) );
            log.info( LIST_OF_FILES_FOR_FILTER + Arrays.toString( filterExtension ) + " : " + files );
            wfLogger.info( LIST_OF_FILES_FOR_FILTER + Arrays.toString( filterExtension ) + " : " + files );
        } else {
            for ( final String string : filterExtension ) {

                final java.io.File dir = new java.io.File( folder.getPath() );
                final List< java.io.File > extensionFiles = Arrays.asList(
                        dir.listFiles( ( dir1, name ) -> name.matches( StringUtils.replaceWildCardWithRegex( string ) ) ) );

                log.info( LIST_OF_FILES_FOR_FILTER + string + " : " + extensionFiles );
                wfLogger.info( LIST_OF_FILES_FOR_FILTER + string + " : " + extensionFiles );
                files.addAll( extensionFiles );
            }
        }
        return files;
    }

    /**
     * Gets the map from vault files.
     *
     * @param notif
     *         the notif
     * @param maps
     *         the maps
     * @param susResponseDTO
     *         the sus response dto
     *
     * @return the map from vault files
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private List< Map< String, String > > getMapFromVaultFiles( Notification notif, List< Map< String, String > > maps,
            SusResponseDTO susResponseDTO ) throws IOException {
        String jsonData;
        if ( null == susResponseDTO.getData() ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.SIMCORE_RESPONSE_IS_EMPTY ) ) );
            log.info( MessagesUtil.getMessage( WFEMessages.SIMCORE_RESPONSE_IS_EMPTY ) );
        }
        try ( ByteArrayInputStream bis = new ByteArrayInputStream( susResponseDTO.getData().toString().getBytes() ) ) {
            final JsonNode jsonNode = JsonUtils.convertInputStreamToJsonNode( bis );

            if ( jsonNode.get( VAULT_FILE_RESPONSE_FIELD_DATA ).size() == FIRST_INDEX ) {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.SIMCORE_FILES_ARE_NOT_AVAILABLE ) ) );
            }
            try ( ByteArrayInputStream bais = new ByteArrayInputStream(
                    jsonNode.get( VAULT_FILE_RESPONSE_FIELD_DATA ).toString().getBytes() ) ) {
                final JsonNode jsonNodeData = JsonUtils.convertInputStreamToJsonNode( bais );
                jsonData = jsonNodeData.toString();
            }
        }
        return ( List< Map< String, String > > ) JsonUtils.jsonToList( jsonData, maps );
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
     * Log files.
     *
     * @param files
     *         the files
     */
    private void logFiles( List< java.io.File > files ) {
        if ( files.isEmpty() ) {
            log.info( "No files found for provided filter" );
            wfLogger.info( MessagesUtil.getMessage( WFEMessages.NO_FILE_FOUND_FOR_FILTER ) );
        } else {
            log.info( "Files to import : " + files );
            wfLogger.info( "Files to import : " + files );
        }
    }

    /**
     * Prepare headers.
     *
     * @return the map
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
     * Removes the files already exists.
     *
     * @param maps
     *         the maps
     * @param files
     *         the files
     */
    private void removeFilesAlreadyExists( List< Map< String, String > > maps, List< java.io.File > files ) {
        for ( final Map< String, String > vaultFile : maps ) {
            final Iterator< java.io.File > iterator = files.iterator();
            while ( iterator.hasNext() ) {
                final java.io.File currentFile = iterator.next();
                if ( currentFile.getName().equalsIgnoreCase( vaultFile.get( KEY_NAME ) ) ) {
                    iterator.remove();
                    log.info( MessagesUtil.getMessage( WFEMessages.SKIPPING_FILE, currentFile.getName() ) );
                    wfLogger.info( MessagesUtil.getMessage( WFEMessages.SKIPPING_FILE, currentFile.getName() ) );
                }
            }
        }
    }

    /**
     * Gets the variable values from parameters and replace them in script.
     *
     * @param notif
     *         the notif
     * @param expression
     *         the expression
     *
     * @return the string
     */
    private String replaceFilterTypeExpressionWithValue( final Notification notif, String expression ) {
        Object value = ConstantsString.EMPTY_STRING;
        if ( expression.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !expression.startsWith(
                ConstantsString.SYS_CMD_INDICATION ) ) {
            if ( parameters.containsKey( expression ) ) {
                value = getArgValueFromJson( parameters, expression );
            } else {
                notif.addError( new Error(
                        MessagesUtil.getMessage( WFEMessages.PARAM_DONT_CONTAIN_ARGUMENT, expression, userWorkflowElement.getName() ) ) );
            }
        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.MESSAGE_IN_VALID_CUSTOM_METHOD_SUFIX ) ) );
        }
        return value.toString();
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
     * Create a payload for upload file .
     *
     * @param file
     *         the file
     *
     * @return the map
     */
    private FileUpload uploadFile( String file ) {

        final Map< String, String > headers = prepareHeaders();
        return SuSClient.uploadFileRequest( prepareURL( URL_UPLOAD_FILE ), file, headers );
    }

}
