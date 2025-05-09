package de.soco.software.simuspace.suscore.local.daemon.controller.impl;

import javax.ws.rs.QueryParam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.base.TransferManager;
import de.soco.software.simuspace.suscore.common.base.UploadObjectThread;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMessageHeaders;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.http.model.TransferInfo;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.model.ComputedDataObject;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.ExportDTO;
import de.soco.software.simuspace.suscore.common.model.FileInfo;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SyncFileDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.ListFilterUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MimeTypeConstants;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.common.model.StatusConfigDTO;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.manager.impl.base.ContextMenuManagerImpl;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectHtmlDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectPredictionModelDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtilExternal;
import de.soco.software.simuspace.suscore.executor.model.ExecutorPool;
import de.soco.software.simuspace.suscore.executor.service.ThreadPoolExecutorService;
import de.soco.software.simuspace.suscore.executor.service.impl.ThreadPoolExecutorServiceImpl;
import de.soco.software.simuspace.suscore.local.daemon.controller.SyncController;
import de.soco.software.simuspace.suscore.local.daemon.entity.AllObjectEntity;
import de.soco.software.simuspace.suscore.local.daemon.entity.ComputedDataObjectEntity;
import de.soco.software.simuspace.suscore.local.daemon.entity.UserDaemonEntity;
import de.soco.software.simuspace.suscore.local.daemon.manager.SqLiteManager;
import de.soco.software.simuspace.suscore.local.daemon.manager.SuscoreDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.model.AllObjectsDTO;
import de.soco.software.simuspace.suscore.local.daemon.model.AppsConfig;
import de.soco.software.simuspace.suscore.local.daemon.model.FileExtension;
import de.soco.software.simuspace.suscore.local.daemon.model.LocalFilesToSync;
import de.soco.software.simuspace.suscore.local.daemon.model.SyncJson;
import de.soco.software.simuspace.suscore.local.daemon.properties.DaemonPropertiesManager;
import de.soco.software.simuspace.suscore.local.daemon.thread.ExportDataObjectThread;
import de.soco.software.simuspace.workflow.model.impl.ProjectConfiguration;
import de.soco.software.simuspace.workflow.model.impl.SuSObjectModel;

/**
 * The Class SyncControllerImpl. used for synch operation such as downloading , uploading , dragging , abort files uploading , checkout
 * ,checkin, etc
 *
 * @author noman.arshad Sir.Huzaifa
 */
@RestController
@RequestMapping( value = "api/data/project" )
public class SyncControllerImpl extends SuSBaseService implements SyncController {

    /**
     * The Constant UNABLE_TO_GET_FILE_EXTENSION_FROM_PATH.
     */
    private static final String UNABLE_TO_GET_FILE_EXTENSION_FROM_PATH = "Unable To get File Extension From Path :";

    /**
     * The Constant SQL_EXCEPTION.
     */
    private static final String SQL_EXCEPTION = "could not execute statement; nested exception is org.hibernate.exception.GenericJDBCException: could not execute statement";

    /**
     * The Constant OBJECT_DOES_NOT_CONTAIN_ANY_FILE.
     */
    private static final String OBJECT_DOES_NOT_CONTAIN_ANY_FILE = " Object does not contain any File";

    /**
     * The Constant DRAG_AND_DROP.
     */
    private static final String DRAG_AND_DROP = "Drag and Drop";

    /**
     * The Constant ABORT.
     */
    private static final String ABORT = "Abort";

    /**
     * The Constant CHECK_IN.
     */
    private static final String CHECK_IN = "CheckIn";

    /**
     * The Constant DISCARD.
     */
    private static final String DISCARD = "Discard";

    /**
     * The Constant CHECKOUT2.
     */
    private static final String CHECKOUT2 = "Checkout";

    /**
     * The Constant UPLOAD.
     */
    private static final String UPLOAD = "Upload";

    /**
     * The Constant EXPORT_OBJECT.
     */
    private static final String EXPORT_OBJECT = "Export Object";

    /**
     * The Constant DOWNLOAD2.
     */
    private static final String DOWNLOAD2 = "Download";

    /**
     * The Constant OPEN_FILE.
     */
    private static final String OPEN_FILE = "4100013x4";

    /**
     * The Constant OPEN_WITH.
     */
    private static final String OPEN_WITH = "4100078x4";

    /**
     * The Constant COMMAND_TO_RUN_IS_NULL.
     */
    private static final String OPENED_FILE_EXTENSION_IS_UNDEFINED_IN_CONFIGURATION = "Opened file extension is undefined in configuration :";

    /**
     * The Constant DOWNLOAD.
     */
    private static final String DOWNLOAD = DOWNLOAD2;

    /**
     * The Constant SYNC_STATUS.
     */
    private static final String SYNC_STATUS = "syncStatus";

    /**
     * The Constant CHECKED_OUT.
     */
    private static final String CHECKED_OUT = "checkedOut";

    /**
     * The Constant CHECKED_OUT_USER.
     */
    private static final String CHECKED_OUT_USER = "checkedOutUser";

    /**
     * The Constant IS_ALREADY_CHECKED_OUT.
     */
    private static final String IS_ALREADY_CHECKED_OUT = " is Already Checked Out.";

    /**
     * The Constant PROJECT_CONFIGRATION_IS_NOT_LOADED.
     */
    private static final String PROJECT_CONFIGRATION_IS_NOT_LOADED = "Project Configuration is not loaded";

    /**
     * The Constant COPY_TO_LOCAL.
     */
    private static final String COPY_TO_LOCAL = "Copy to Local";

    /**
     * The Constant CANT_COPY_SOURCE_AND_DESTINATION_ARE_SAME.
     */
    private static final String CANT_COPY_SOURCE_AND_DESTINATION_ARE_SAME = "Can't Copy: Source and Destination are same.";

    /**
     * The Constant LIFECYCLE_STATUS_ID.
     */
    private static final String LIFECYCLE_STATUS_ID = "data/project/lifecycle/id/";

    /**
     * The Constant logger to log error or exceptions of work flow wait element.
     */
    private static final Logger logger = Logger.getLogger( SyncControllerImpl.class.getName() );

    /**
     * The Constant YOU_HAVE_NOT_PROVIDED_DOWNLOAD_ABLE_LINK.
     */
    private static final String YOU_HAVE_NOT_PROVIDED_DOWNLOAD_ABLE_LINK = "You have not provided downloadable link";

    /**
     * The Constant YOU_HAVE_NOT_PROVIDED_DESTINATION_DIRECTORY.
     */
    private static final String YOU_HAVE_NOT_PROVIDED_DESTINATION_DIRECTORY = "You have not provided destination directory";

    /**
     * The Constant YOU_DON_T_HAVE_PROVIDED_DOWNLOAD_INFO.
     */
    private static final String YOU_DON_T_HAVE_PROVIDED_DOWNLOAD_INFO = "You don't have provided download info.";

    /**
     * The Constant DEST_PATH.
     */
    private static final String DEST_PATH = "destPath";

    /**
     * The Constant DOWNLOAD_LINK_KEY.
     */
    private static final String DOWNLOAD_LINK_KEY = "downloadLink";

    /**
     * The Constant OBJECT_COPY_SUCCESSFULL.
     */
    private static final String OBJECT_COPY_SUCCESSFULL = "Object Copy Successfull";

    /**
     * The Constant ONLY_LOCAL.
     */
    private static final String ONLY_LOCAL = "Only Local";

    /**
     * The Constant ONLY_LOCAL_UNKNOWN.
     */
    private static final String ONLY_LOCAL_UNKNOWN = "Only Local Unknown";

    /**
     * The Constant \LOCAL_UNKNOWN.
     */
    private static final String LOCAL_UNKNOWN = "Local Unknown";

    /**
     * The Constant IGNORE.
     */
    private static final String IGNORED = "Ignored";

    /**
     * The Constant CHANGED_ON_LOCAL.
     */
    private static final String CHANGED_ON_LOCAL = "Changed on Local";

    /**
     * The Constant CHANGED_ON_SERVER.
     */
    private static final String CHANGED_ON_SERVER = "Changed on Server";

    /**
     * The Constant SYNCED.
     */
    private static final String SYNCED = "Synced";

    /**
     * The Constant DATA_PROJECT.
     */
    private static final String DATA_PROJECT = "data/project/";

    /**
     * The Constant DATA_OBJECT.
     */
    private static final String DATA_OBJECT = "data/object/";

    /**
     * The Constant DATA_LIST.
     */
    private static final String DATA_LIST = "/data/list";

    /**
     * The Constant LIST.
     */
    private static final String LIST = "/list";

    /**
     * The Constant UI.
     */
    private static final String UI = "/ui";

    /**
     * The Constant TYPE.
     */
    private static final String TYPE = "/type/";

    /**
     * The Constant DATA_CONFIGURATION.
     */
    private static final String DATA_CONFIGURATION = "data/project/config/";

    /**
     * The Constant FILE_SEPERATOR.
     */
    private static final String FILE_SEPERATOR = "/";

    /**
     * The Constant ENTITY_CLASS_FIELD_NAME.
     */
    private static final String ENTITY_CLASS_FIELD_NAME = "ENTITY_CLASS";

    /**
     * The Constant DAEMON_IS_UP_AND_RUNNING.
     */
    public static final String DAEMON_IS_UP_AND_RUNNING = "Daemon is up and running";

    /**
     * The Constant BREADCRUMB_VIEW_DATA_PROJECT.
     */
    private static final String BREADCRUMB_VIEW_DATA_PROJECT = "breadcrumb/view/data/project/";

    /**
     * The Constant DATA_SELECTION.
     */
    private static final String DATA_SELECTION = "selection/";

    /**
     * The Constant CHECKOUT.
     */
    private static final String CHECKOUT = "/checkout/";

    /**
     * The Constant CHECKOUT_GET.
     */
    private static final String CHECKOUT_GET = "/checkout/get";

    /**
     * The Constant ONLY_SERVER.
     */
    private static final String ONLY_SERVER = "Only Server";

    /**
     * The Constant EXPORT_PERMISSION_CHECK.
     */
    private static final String READ_PERMISSION_CHECK = "data/perm/read/check/";

    /**
     * The Constant USER_DONT_HAVE_EXPORT_PERMISSION.
     */
    private static final String USER_DONT_HAVE_READ_PERMISSION = "this user dont have read permission";

    /**
     * The Constant YOU_HAVE_PROVIDED_INVALID_JSON.
     */
    private static final String YOU_HAVE_PROVIDED_INVALID_JSON = "you have provided an invalid json";

    /**
     * The Constant DEFAULT_DIR.
     */
    private static final Object DEFAULT_DIR = "_default_dir_";

    /**
     * The Constant DEFAULT_FILE.
     */
    private static final Object DEFAULT_FILE = "_default_file_";

    /**
     * The Constant CONTEXT.
     */
    private static final String CONTEXT = "/context";

    /**
     * The Constant LOCAL.
     */
    private static final String LOCAL = "/local";

    /**
     * The Constant SYNC.
     */
    private static final String SYNC = "/sync";

    /**
     * The Constant UI_VIEW.
     */
    private static final String UI_VIEW = "/ui/view/";

    /**
     * The Constant DEFAULT.
     */
    private static final String DEFAULT = "/default";

    /**
     * The Constant TYPE_OPTIONS_LABEL.
     */
    public static final String TYPE_OPTIONS_LABEL = "Type Options";

    /**
     * The Constant FIELD_TYPE_SELECT.
     */
    public static final String FIELD_TYPE_SELECT = "select";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant CHANGE_TYPE.
     */
    private static final String CHANGE_TYPE = "changeType";

    /**
     * The Constant API_DOCUMENT.
     */
    private static final String API_DOCUMENT = "document/";

    /**
     * The Constant PROPERTIES_DATA.
     */
    private static final String PROPERTIES_DATA = "/properties/data";

    /**
     * The Constant CREATED_ON_FIELD.
     */
    private static final String CREATED_ON_FIELD = "createdOn";

    /**
     * The Constant SORTING_DIRECTION_DESCENDING.
     */
    public static final String SORTING_DIRECTION_DESCENDING = "desc";

    /**
     * The Constant APP_PATTERN.
     */
    public static final String APP_PATTERN = "{?app}";

    /**
     * The constant APP_URL_PARAM.
     */
    private static final String APP_URL_PARAM = "?app=";

    /**
     * The Constant OPEN_FILE_CONTEXT_URL.
     */
    private static final String OPEN_FILE_CONTEXT_URL = "get/data/project/{projectId}/openfile/{selectionId}{?app}";

    /**
     * The Constant LIVESEARCH.
     */
    private static final String LIVESEARCH = "liveSearch";

    /**
     * The Constant FILE_TYPE_HTML.
     */
    private static final String FILE_TYPE_HTML = "Html";

    /**
     * The sq lite manager.
     */
    @Autowired
    SqLiteManager sqLiteManager;

    /**
     * The daemon manager.
     */
    @Autowired
    private SuscoreDaemonManager daemonManager;

    /**
     * The transfer manager.
     */
    @Autowired
    private TransferManager transferManager;

    /**
     * The thread pool executor service.
     */
    private ThreadPoolExecutorService threadPoolExecutorService;

    /**
     * The server files.
     */
    List< FileInfo > serverFileUpload = new ArrayList<>();

    /**
     * The computed files.
     */
    List< ComputedDataObject > computedFiles = new ArrayList<>();

    /**
     * Gets the thread pool executor service.
     *
     * @return the thread pool executor service
     */
    public ThreadPoolExecutorService getThreadPoolExecutorService() {
        if ( threadPoolExecutorService == null ) {
            threadPoolExecutorService = ThreadPoolExecutorServiceImpl.getInstance();
        }
        try {
            if ( threadPoolExecutorService.getExecutorPoolFromConf() == null ) {
                String path = DaemonPropertiesManager.getInstance().getProps().get( "karaf.base" ) + File.separator
                        + ConstantsKaraf.KARAF_CONF + File.separator + ConstantsFileProperties.SUSCORE_EXECUTOR_JSON;
                threadPoolExecutorService.setExecutorPool( JsonUtils.jsonToObject(
                        org.apache.commons.io.FileUtils.readFileToString( getFileFromKarafConf( path ), Charset.defaultCharset() ),
                        ExecutorPool.class ) );
            }
        } catch ( IOException e ) {
            logger.error( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey() ) );
        }
        return threadPoolExecutorService;
    }

    /**
     * Gets the file from karaf conf.
     *
     * @param path
     *         the path
     *
     * @return the file from karaf conf
     */
    public static File getFileFromKarafConf( String path ) {
        File file = new File( path );
        if ( !file.exists() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey() ) );
        } else {
            return file;
        }
    }

    /**
     * Sets the thread pool executor service.
     *
     * @param threadPoolExecutorService
     *         the new thread pool executor service
     */
    public void setThreadPoolExecutorService( ThreadPoolExecutorService threadPoolExecutorService ) {
        this.threadPoolExecutorService = threadPoolExecutorService;
    }

    /**
     * Gets the computed files.
     *
     * @return the computed files
     */
    public List< ComputedDataObject > getComputedFiles() {
        return computedFiles;
    }

    /**
     * Sets the computed files.
     *
     * @param computedFiles
     *         the new computed files
     */
    public void setComputedFiles( List< ComputedDataObject > computedFiles ) {
        this.computedFiles = computedFiles;
    }

    /**
     * Gets the server file upload.
     *
     * @return the server file upload
     */
    public List< FileInfo > getServerFileUpload() {
        return serverFileUpload;
    }

    /**
     * Sets the server file upload.
     *
     * @param serverFileUpload
     *         the new server file upload
     */
    public void setServerFileUpload( List< FileInfo > serverFileUpload ) {
        this.serverFileUpload = serverFileUpload;
    }

    /**
     * Gets the daemon manager.
     *
     * @return the daemonManager
     */
    public SuscoreDaemonManager getDaemonManager() {
        return daemonManager;
    }

    /**
     * Sets the daemon manager.
     *
     * @param daemonManager
     *         the daemonManager to set
     */
    public void setDaemonManager( SuscoreDaemonManager daemonManager ) {
        this.daemonManager = daemonManager;
    }

    /**
     * Gets the transfer manager.
     *
     * @return the transfer manager
     */
    public TransferManager getTransferManager() {
        return transferManager;
    }

    /**
     * Sets the transfer manager.
     *
     * @param transferManager
     *         the new transfer manager
     */
    public void setTransferManager( TransferManager transferManager ) {
        this.transferManager = transferManager;
    }

    /**
     * Checks if is valid from server.
     *
     * @param configProject
     *         the config project
     * @param type
     *         the type
     *
     * @return true, if is valid from server
     */
    public boolean isValidFromServer( ProjectConfiguration configProject, String type ) {
        for ( SuSObjectModel entityConfig : configProject.getEntityConfig() ) {
            if ( entityConfig.getName().equalsIgnoreCase( type ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * **************************************** project ova tabs view ****************************************.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param authToken
     *         the auth token
     *
     * @return the all object views by object id and type id
     */
    @RequestMapping( value = "{projectId}/type/{typeId}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getAllObjectViewsByObjectIdAndTypeId( @PathVariable String projectId,
            @PathVariable String typeId, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.getRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + TYPE + typeId + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String json = JsonUtils.toJson( request.getData() );
            List< ObjectViewDTO > objectView = JsonUtils.jsonToList( json, ObjectViewDTO.class );
            return new ResponseEntity<>( ResponseUtils.successResponse( objectView ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Save object view.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param objectJson
     *         the object json
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    @RequestMapping( value = "{projectId}/type/{typeId}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > saveObjectView( @PathVariable String projectId, @PathVariable String typeId,
            @RequestBody String objectJson, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.postRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + TYPE + typeId + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW,
                    objectJson, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            if ( null != request.getData() ) {
                String json = JsonUtils.toJson( request.getData() );
                ObjectViewDTO objectView = JsonUtils.jsonToObject( json, ObjectViewDTO.class );
                return new ResponseEntity<>( ResponseUtils.successResponse( objectView ), HttpStatus.OK );
            } else {
                return new ResponseEntity<>( ResponseUtils.failureResponse( request.getMessage().getContent(), null ), HttpStatus.OK );
            }

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Sets the object view as default.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param viewId
     *         the view id
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    @RequestMapping( value = "{projectId}/type/{typeId}" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > setObjectViewAsDefault( @PathVariable String projectId, @PathVariable String typeId,
            @PathVariable String viewId, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient
                    .getRequest(
                            daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + TYPE + typeId
                                    + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW + FILE_SEPERATOR + viewId + DEFAULT,
                            CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String json = JsonUtils.toJson( request.getData() );
            ObjectViewDTO objectView = JsonUtils.jsonToObject( json, ObjectViewDTO.class );
            return new ResponseEntity<>( ResponseUtils.successResponse( objectView ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Delete object view.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param viewId
     *         the view id
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    @RequestMapping( value = "{projectId}/type/{typeId}"
            + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW, method = RequestMethod.DELETE )
    public ResponseEntity< SusResponseDTO > deleteObjectView( @PathVariable String projectId, @PathVariable String typeId,
            @PathVariable String viewId, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient
                    .deleteRequest(
                            daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + TYPE + typeId
                                    + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW + FILE_SEPERATOR + viewId,
                            CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            if ( Boolean.parseBoolean( request.getData().toString() ) ) {
                return new ResponseEntity<>(
                        ResponseUtils.successResponse( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(),
                                authToken, Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true ),
                        HttpStatus.OK );
            } else {
                return new ResponseEntity<>( ResponseUtils.successResponse( MessageBundleFactory
                        .getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken, Messages.VIEW_DOES_NOT_EXIST.getKey() ) ),
                        HttpStatus.OK );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Update object view.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    @RequestMapping( value = "{projectId}/type/{typeId}"
            + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW, method = RequestMethod.PUT )
    public ResponseEntity< SusResponseDTO > updateObjectView( @PathVariable String projectId, @PathVariable String typeId,
            @PathVariable String viewId, @RequestBody String objectJson, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient
                    .putRequest(
                            daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + TYPE + typeId
                                    + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW + FILE_SEPERATOR + viewId,
                            CommonUtils.prepareHeadersWithAuthToken( authToken ), objectJson );
            if ( null != request.getData() ) {
                String json = JsonUtils.toJson( request.getData() );
                ObjectViewDTO objectView = JsonUtils.jsonToObject( json, ObjectViewDTO.class );
                return new ResponseEntity<>( ResponseUtils.successResponse( objectView ), HttpStatus.OK );
            } else {
                return new ResponseEntity<>( ResponseUtils.failureResponse( request.getMessage().getContent(), null ), HttpStatus.OK );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * **************************************** project ova tabs view end****************************************.
     *
     * @param projectId
     *            the project id
     * @param typeId
     *            the type id
     * @param authToken
     *            the auth token
     * @param objectFilterJson
     *            the object filter json
     * @return the all objects context
     */

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/type/{typeId}/context", method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > getAllObjectsContext( @PathVariable String projectId, @PathVariable String typeId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken, @RequestBody String objectFilterJson ) {
        try {
            SusResponseDTO request = SuSClient.postRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + TYPE + typeId + CONTEXT, objectFilterJson,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String json = JsonUtils.toJson( request.getData() );
            boolean containsDirItem = false;
            List< ComputedDataObjectEntity > onlyLocalObjects = new ArrayList<>();
            FiltersDTO filter = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            for ( Object item : filter.getItems() ) {
                ComputedDataObjectEntity localObject = sqLiteManager.findById( item.toString() );
                if ( localObject != null && !localObject.isExistOnServer() ) {
                    onlyLocalObjects.add( localObject );
                }
                if ( null != localObject && localObject.isDir() ) {
                    containsDirItem = true;
                    break;
                }
            }

            List< ContextMenuItem > context = JsonUtils.jsonToList( json, ContextMenuItem.class );
            if ( !containsDirItem ) {
                List< ContextMenuItem > openfileContext = getOpenFileContext( projectId, authToken, filter, objectFilterJson,
                        onlyLocalObjects );
                if ( !openfileContext.isEmpty() ) {
                    context.addAll( openfileContext );
                }
            }

            SusResponseDTO response = SuSClient.postRequest( daemonManager.getServerAPIBase() + "system/permissions/permitted",
                    projectId + ":"
                            + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue( DaemonPropertiesManager.hasTranslation(), authToken ),
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );

            for ( ContextMenuItem contextMenuItem : context ) {
                if ( contextMenuItem.getTitle().equals(
                        MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken, "4100022x4" ) )
                        && !Boolean.parseBoolean( response.getData().toString() ) ) {
                    context.remove( contextMenuItem );
                    break;
                }
            }

            return new ResponseEntity<>(
                    ResponseUtils.successResponse(
                            ContextUtilExternal.allOrderedContext( DaemonPropertiesManager.hasTranslation(), authToken, context ) ),
                    HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Gets Open file context.
     *
     * @param projectId
     *         the project id
     * @param authToken
     *         the authToken
     * @param filter
     *         the filter
     * @param objectFilterJson
     *         the object filter json
     * @param onlyLocalObjects
     *         the only local objects
     *
     * @return the open file context
     */
    private List< ContextMenuItem > getOpenFileContext( String projectId, String authToken, FiltersDTO filter, String objectFilterJson,
            List< ComputedDataObjectEntity > onlyLocalObjects ) {
        List< ContextMenuItem > context = new ArrayList<>();
        try {
            String selectionId = saveSelection( objectFilterJson, authToken );

            if ( filter.getItems().size() > 1 ) {
                List< GenericDTO > serverObjects = getSelectedObjects( objectFilterJson, authToken );

                if ( !isFileExtensionSameForAllSelection( projectId, filter, serverObjects, authToken ) ) {
                    context.add( getOpenFileContextForMultipleExtensions( projectId, authToken, selectionId ) );
                    return context;
                }
            }

            DataObjectDTO objectDTO = null;
            if ( filter.getItems().size() != onlyLocalObjects.size() ) {
                objectDTO = getDataObjectsById( filter.getItems().get( 0 ).toString(), authToken );
            }
            String fileExtension = getFileExtension( projectId, filter.getItems().get( 0 ).toString(),
                    objectDTO != null ? objectDTO.getName() : null, authToken );

            AppsConfig app = getAppsConfig();
            if ( app == null ) {
                return context;
            }

            Set< String > names = app.getApplicationsForExtension( fileExtension );

            for ( String name : names ) {
                ContextMenuItem containerCMI = new ContextMenuItem();
                containerCMI.setUrl( OPEN_FILE_CONTEXT_URL.replace( ContextMenuManagerImpl.PROJECT_ID_PARAM, projectId )
                        .replace( ContextMenuManagerImpl.SELECTION_ID_PARAM, selectionId ).replace( APP_PATTERN, APP_URL_PARAM + name ) );
                containerCMI
                        .setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken, OPEN_WITH )
                                + ConstantsString.SPACE + name );
                context.add( containerCMI );
            }

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return context;
    }

    /**
     * Gets Open file context For multiple Extensions.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selectionId
     * @param id
     *
     * @return the open file context
     */
    private ContextMenuItem getOpenFileContextForMultipleExtensions( String projectId, String authToken, String selectionId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( OPEN_FILE_CONTEXT_URL.replace( ContextMenuManagerImpl.PROJECT_ID_PARAM, projectId )
                .replace( ContextMenuManagerImpl.SELECTION_ID_PARAM, selectionId ).replace( APP_PATTERN, APP_URL_PARAM + "multiple" ) );
        containerCMI.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken, OPEN_FILE ) );
        return containerCMI;
    }

    /**
     * Is File Extension Same For All Selection .
     *
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     * @param serverObjects
     *         the server objects
     * @param authToken
     *         the auth token
     *
     * @return the Is File Extension Same
     */
    private boolean isFileExtensionSameForAllSelection( String projectId, FiltersDTO filter, List< GenericDTO > serverObjects,
            String authToken ) {
        boolean isSame = true;
        String fileExtension = null;

        // check local files extension
        for ( Object item : filter.getItems() ) {
            String itemExtension = getFileExtension( projectId, item.toString(), null, authToken );
            if ( fileExtension == null ) {
                fileExtension = itemExtension;
            } else if ( !fileExtension.equals( itemExtension ) ) {
                return false;
            }
        }

        // check server files extension
        for ( GenericDTO item : serverObjects ) {
            String itemExtension = getFileExtension( projectId, null, item.getName(), authToken );
            if ( fileExtension == null ) {
                fileExtension = itemExtension;
            } else if ( !fileExtension.equals( itemExtension ) ) {
                return false;
            }
        }

        return isSame;
    }

    /**
     * Gets File Extension.
     *
     * @param projectId
     *         the project id
     * @param selectedObjectId
     *         the selectedObjectId
     * @param objectName
     *         the objectName
     * @param authToken
     *         the authToken
     *
     * @return the file extension
     */
    private String getFileExtension( String projectId, String selectedObjectId, String objectName, String authToken ) {
        try {
            String filePath = "";

            if ( objectName != null ) {
                File localDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir()
                        + getRelativeLocalSynchPathFromBreadCrumb( projectId, authToken ) );
                File localFile = getLocalFileByName( localDir, objectName );
                if ( localFile != null ) {
                    filePath = localFile.getPath();
                }
            } else {
                ComputedDataObjectEntity entity = sqLiteManager.findById( selectedObjectId );
                filePath = entity != null ? entity.getPath() : "";
            }

            // get extension by URL
            String fileExtension = FilenameUtils.getExtension( filePath );
            // if extension do not exist
            if ( fileExtension == null || fileExtension.isEmpty() ) {
                // get extension by File Prob content type
                Path path = Paths.get( filePath );
                String type = Files.probeContentType( path );
                fileExtension = MimeTypeConstants.getMimeKeyByValue( type );
            }

            return fileExtension;

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }

        return null;
    }

    /**
     * Save Selection.
     *
     * @param objectFilterJson
     *         the objectFilterJson
     * @param authToken
     *         the authToken
     *
     * @return the selection id
     */
    private String saveSelection( String objectFilterJson, String authToken ) {
        try {
            SusResponseDTO request = SuSClient.postRequest( daemonManager.getServerAPIBase() + DATA_SELECTION + "context",
                    prepareJsonForServer( objectFilterJson ), CommonUtils.prepareHeadersWithAuthToken( authToken ) );

            String json = JsonUtils.toJson( request.getData() );
            JSONObject jsonObject = ( JSONObject ) new JSONParser().parse( json );
            return jsonObject.get( "id" ).toString();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }

        return null;
    }

    /**
     * Gets selected objects.
     *
     * @param objectFilterJson
     *         the objectFilterJson
     * @param authToken
     *         the authToken
     *
     * @return the selected objects
     */
    private List< GenericDTO > getSelectedObjects( String objectFilterJson, String authToken ) {
        List< GenericDTO > objects = new ArrayList<>();
        try {
            SusResponseDTO request = SuSClient.postRequest( daemonManager.getServerAPIBase() + DATA_SELECTION + "items/list",
                    prepareJsonForServer( objectFilterJson ), CommonUtils.prepareHeadersWithAuthToken( authToken ) );

            String json = JsonUtils.toJson( request.getData() );

            objects = JsonUtils.jsonToList( json, GenericDTO.class );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }

        return objects;
    }

    /**
     * Gets app config.
     *
     * @return the app config
     */
    private AppsConfig getAppsConfig() {
        final String propFileName = System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator
                + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY + File.separator + ConstantsString.CONFIG + File.separator
                + ConstantsString.APPS_CONF_FILE_NAME;
        File f = new File( propFileName );
        if ( !f.exists() ) {
            return null;
        }
        try ( InputStream is = new FileInputStream( propFileName ) ) {
            AppsConfig appConfig = JsonUtils.jsonToObject( is, AppsConfig.class );
            return appConfig;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{containerId}/openfile/{selectionId}", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > openDataObject( @PathVariable String containerId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken, @PathVariable String selectionId,
            @QueryParam( "app" ) String app ) {
        try {
            printStarBorderInLogFile(
                    MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken, OPEN_FILE ) );
            File localDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir()
                    + getRelativeLocalSynchPathFromBreadCrumb( containerId, authToken ) );
            // get from server all items
            List< FileInfo > serverFiles = getAllItemsFromServer( containerId, authToken );
            // get from server selected IDs
            List< String > selectedFiles = JsonUtils.jsonToList( getAllSelectedItemsFromServer( authToken, selectionId ), String.class );
            AppsConfig appsConfig = getAppsConfig();
            // for local tab
            forLocalTabOpenFile( localDir, selectedFiles, appsConfig, containerId, app );
            // for synch tab
            String msg = forSynchTabOpenFile( containerId, authToken, localDir, serverFiles, selectedFiles, appsConfig, app );
            if ( msg != null ) {
                return new ResponseEntity<>( ResponseUtils.successInfoResponse( msg, null ), HttpStatus.OK );
            }
            return new ResponseEntity<>( ResponseUtils.successResponse( MessageBundleFactory
                    .getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken, Messages.FILE_OPEN_SUCCESS.getKey() ), true ),
                    HttpStatus.OK );
        } catch ( IOException | SusException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        } catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        return new ResponseEntity<>( ResponseUtils.successResponse( true ), HttpStatus.OK );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{containerId}/sync/download/{selectionId}", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > downloadObjects( @PathVariable String containerId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken, @PathVariable String selectionId ) {
        try {
            printStarBorderInLogFile( DOWNLOAD2 );
            // just for return to FE
            List< FileInfo > addAndReturnDownloadedFiles = new ArrayList<>();
            List< ComputedDataObject > computedDOFiles = new ArrayList<>();

            if ( !checkReadPermission( containerId, authToken ) ) {
                return new ResponseEntity<>( ResponseUtils.successInfoResponse( USER_DONT_HAVE_READ_PERMISSION, null ), HttpStatus.OK );
            }
            // calculate project local directory
            File projectLocalDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir()
                    + getRelativeLocalSynchPathFromBreadCrumb( containerId, authToken ) );
            // get from server selected IDs
            List< String > namesInSelection = JsonUtils.jsonToList( getAllSelectedItemsFromServer( authToken, selectionId ), String.class );
            Map< String, String > selectedIdsMap = new HashMap<>();
            namesInSelection.stream().forEach( name -> selectedIdsMap.put( name.toLowerCase(), name ) );
            // evaluate if user selected to download an object, by name of the object.

            List< FileInfo > serverFiles = getAllItemsFromServer( containerId, authToken );

            // SYNC both .json file and .zip file *IF* file type is *HTML*
            List< FileInfo > allServerFiles = new ArrayList< FileInfo >();
            for ( FileInfo file : serverFiles ) {
                if ( file.getType().equalsIgnoreCase( FILE_TYPE_HTML ) ) {
                    allServerFiles.add( file );
                    DataObjectHtmlDTO SuSObjectEntity = getDataObjectHtmlDTOById( file.getId(), authToken );
                    if ( SuSObjectEntity.getZipFile() != null ) {
                        allServerFiles.add( prepareZipFileInfo( file, SuSObjectEntity.getZipFile() ) );
                    }
                }
                // Type NOT EQUAL HTML simply add
                else {
                    allServerFiles.add( file );
                }
            }
            if ( !allServerFiles.isEmpty() ) {
                serverFiles = allServerFiles;
            }

            evaluateAndDownloadObjectFiles( containerId, authToken, projectLocalDir, serverFiles, selectedIdsMap, computedDOFiles );

            for ( ComputedDataObject filesToDownload : computedDOFiles ) {
                if ( filesToDownload.isCheckOutFileToDownload() ) {
                    transferManager.getTransfers().put( filesToDownload.getFile().getName(), new TransferInfo( 0L, DOWNLOAD,
                            projectLocalDir.getPath() + File.separator + filesToDownload.getFile().getName() ) );
                }
                if ( null != filesToDownload.getFile().getFile() ) {
                    transferManager.downloadDataObjectFromLocation( containerId, filesToDownload.getFile(), authToken,
                            filesToDownload.getLocation() );
                    addAndReturnDownloadedFiles.add( filesToDownload.getFile() );
                    // saving or updating entries in sqlite
                    saveOrUpdateServerObjectsInSqlLite( filesToDownload.getFile().getFile().getName(), containerId, filesToDownload,
                            authToken );
                }
            }
            return new ResponseEntity<>(
                    ResponseUtils
                            .successResponse(
                                    MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                                            Messages.DOWNLOAD_SUCCESS.getKey(), addAndReturnDownloadedFiles.size() ),
                                    addAndReturnDownloadedFiles ),
                    HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Gets data object html dto by id.
     *
     * @param fileId
     *         the file id
     * @param authToken
     *         the auth token
     *
     * @return the data object html dto by id
     */
    private DataObjectHtmlDTO getDataObjectHtmlDTOById( String fileId, String authToken ) {
        DataObjectHtmlDTO SuSObjectEntity = null;
        try {
            SusResponseDTO request = SuSClient.getRequest( daemonManager.getServerAPIBase() + DATA_OBJECT + fileId + PROPERTIES_DATA,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String jsonOutput = JsonUtils.toJson( request.getData() );
            SuSObjectEntity = JsonUtils.jsonToObject( jsonOutput, DataObjectHtmlDTO.class );
        } catch ( Exception e ) {
        }
        return SuSObjectEntity;
    }

    /**
     * Prepare zip file info file info.
     *
     * @param jsonFileInfo
     *         the json file info
     * @param zipFile
     *         the zip file
     *
     * @return the file info
     */
    private FileInfo prepareZipFileInfo( FileInfo jsonFileInfo, DocumentDTO zipFile ) {
        FileInfo zipFileInfo = new FileInfo();

        zipFileInfo.setId( jsonFileInfo.getId() );
        zipFileInfo.setName( jsonFileInfo.getName() );
        zipFileInfo.setHash( jsonFileInfo.getHash() );
        zipFileInfo.setHashList( jsonFileInfo.getHashList() );
        zipFileInfo.setDir( jsonFileInfo.isDir() );
        zipFileInfo.setType( jsonFileInfo.getType() );
        zipFileInfo.setCreatedOn( jsonFileInfo.getCreatedOn() );
        zipFileInfo.setCheckedOut( jsonFileInfo.getCheckedOut() );
        zipFileInfo.setCheckedOutUser( jsonFileInfo.getCheckedOutUser() );
        zipFileInfo.setLifeCycleStatus( jsonFileInfo.getLifeCycleStatus() );
        zipFileInfo.setAbort( jsonFileInfo.isAbort() );
        zipFileInfo.setLocation( jsonFileInfo.getLocation() );
        zipFileInfo.setAutoDeleted( jsonFileInfo.isAutoDeleted() );
        zipFileInfo.setFile( zipFile );
        return zipFileInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "document/download", method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > downloadDocument(
            @RequestHeader( value = ConstantsMessageHeaders.X_AUTH_TOKEN ) String authToken, @RequestBody String downloadInfoJson ) {
        try {
            Map< String, String > downloadInfo = new HashMap<>();
            downloadInfo = ( Map< String, String > ) JsonUtils.jsonToMap( downloadInfoJson, downloadInfo );
            if ( downloadInfo != null && CollectionUtil.isNotEmpty( downloadInfo.keySet() ) ) {
                if ( !downloadInfo.containsKey( DOWNLOAD_LINK_KEY ) ) {
                    throw new SusException( YOU_HAVE_NOT_PROVIDED_DOWNLOAD_ABLE_LINK );
                } else if ( !downloadInfo.containsKey( DEST_PATH ) ) {
                    throw new SusException( YOU_HAVE_NOT_PROVIDED_DESTINATION_DIRECTORY );
                } else {
                    SuSClient.downloadRequest( daemonManager.getServerAPIBase() + downloadInfo.get( DOWNLOAD_LINK_KEY ),
                            downloadInfo.get( DEST_PATH ), prepareDownloadHeaders( authToken ), null );
                }
            } else {
                throw new SusException( YOU_DON_T_HAVE_PROVIDED_DOWNLOAD_INFO );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
        return new ResponseEntity<>(
                ResponseUtils.successResponse( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                        Messages.DOCUMENT_DOWNLOAD_SUCCESS.getKey(), ConstantsInteger.INTEGER_VALUE_ONE ), null ),
                HttpStatus.OK );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "isup", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > isUp() {
        return new ResponseEntity<>( ResponseUtils.successResponse( DAEMON_IS_UP_AND_RUNNING, null ), HttpStatus.OK );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "properties", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getAllProperties() {
        try {
            Map< String, Object > map = new HashMap<>();
            String url = daemonManager.getServerAPIBase() + "properties";
            SusResponseDTO response = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( null ) );
            if ( null != response.getData() ) {
                DaemonPropertiesManager.getInstance()
                        .setProps( ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.objectToJson( response.getData() ), map ) );
            }
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return new ResponseEntity<>( ResponseUtils.successResponse( "All properties set", null ), HttpStatus.OK );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{containerId}/sync/upload/{selectionId}", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > uploadObjects( @PathVariable String containerId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken, @PathVariable String selectionId ) {
        // just for return to FE
        List< FileInfo > addAndReturnUploadededFiles = new ArrayList<>();
        try {
            printStarBorderInLogFile( UPLOAD );
            if ( !checkReadPermission( containerId, authToken ) ) {
                return new ResponseEntity<>( ResponseUtils.successInfoResponse( USER_DONT_HAVE_READ_PERMISSION, null ), HttpStatus.OK );
            }
            // get from server all items
            List< FileInfo > serverFiles = getAllItemsFromServer( containerId, authToken );
            // get from server selected IDs
            List< String > namesInSelection = JsonUtils.jsonToList( getAllSelectedItemsFromServer( authToken, selectionId ), String.class );
            // get from server the container to compute path
            File localDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir()
                    + getRelativeLocalSynchPathFromBreadCrumb( containerId, authToken ) );
            Map< String, LocalFilesToSync > selectedIdsMaps = prepareIdsMap( serverFiles, namesInSelection, localDir );

            // filter objects by selected ids
            filterServerObjects( serverFiles, selectedIdsMaps );
            evaluateUserForUpload( containerId, authToken, addAndReturnUploadededFiles, serverFiles, selectedIdsMaps, localDir );
            return new ResponseEntity<>(
                    ResponseUtils
                            .successResponse(
                                    MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                                            Messages.UPLOAD_SUCCESS.getKey(), addAndReturnUploadededFiles.size() ),
                                    addAndReturnUploadededFiles ),
                    HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{containerId}/checkout/{selectionId}", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > checkoutObjects( @PathVariable String containerId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken, @PathVariable String selectionId ) {
        try {
            printStarBorderInLogFile( CHECKOUT2 );
            // just for return to FE
            List< FileInfo > checkoutFiles = new ArrayList<>();
            List< ComputedDataObject > computeCheckOutList = new ArrayList<>();
            // get from server the container to compute path
            File localDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir()
                    + getRelativeLocalSynchPathFromBreadCrumb( containerId, authToken ) );
            if ( !checkReadPermission( containerId, authToken ) ) {
                return new ResponseEntity<>( ResponseUtils.successInfoResponse( USER_DONT_HAVE_READ_PERMISSION, null ), HttpStatus.OK );
            }
            List< FileInfo > serverFiles = addHtmlObjectToServerFile( authToken, selectionId,
                    getAllItemsFromServer( containerId, authToken ) );
            List< Object > objList = compareLocalSync( serverFiles, null, containerId, authToken, false );
            List< SyncFileDTO > filledResponseList = ( List< SyncFileDTO > ) ( Object ) objList;
            // get from server selected IDs
            List< String > namesInSelection = JsonUtils.jsonToList( getAllSelectedItemsFromServer( authToken, selectionId ), String.class );
            Map< String, String > selectedIdsMap = new HashMap<>();
            namesInSelection.stream().forEach( name -> selectedIdsMap.put( name.toLowerCase(), name ) );
            // evaluate if user selected to download an object, by name of the object.

            serverFiles.stream().forEach( file -> {
                if ( selectedIdsMap.containsKey( file.getId() ) && !file.isDir() && !file.isAutoDeleted() ) {
                    evaluateCheckoutStatusAndBeginDownload( containerId, authToken, file, computeCheckOutList, filledResponseList );
                }
            } );

            // all files are checked against every condition now download prepared files/objects adding entries into progress bar
            computeCheckOutList.stream().forEach( preparedFile -> {
                if ( preparedFile.isCheckOutFileToDownload() ) {
                    transferManager.getTransfers().put( preparedFile.getFileName(),
                            new TransferInfo( 0L, DOWNLOAD, localDir.getPath() + File.separator + preparedFile.getFile().getName() ) );
                }

                if ( preparedFile.isCheckOutFileToDownload() ) {
                    transferManager.downloadDataObjectFromLocation( containerId, preparedFile.getFile(), authToken,
                            preparedFile.getLocation() );
                }
                if ( !checkoutFiles.contains( preparedFile.getFile() ) ) {
                    try {
                        SuSClient.getRequest( daemonManager.getServerAPIBase() + DATA_PROJECT + preparedFile.getFile().getId() + CHECKOUT
                                + ConstantsDbOperationTypes.CHECKOUT, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
                        checkoutFiles.add( preparedFile.getFile() );
                    } catch ( IOException e ) {
                        ExceptionLogger.logException( e, getClass() );
                    }
                }
            } );
            return new ResponseEntity<>(
                    ResponseUtils.successResponse( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(),
                            authToken, Messages.CHECKOUT_OBJECT.getKey(), checkoutFiles.size() ), checkoutFiles ),
                    HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Adds the html object to server file.
     *
     * @param authToken
     *         the auth token
     * @param selectionId
     *         the selection id
     * @param serverFiles
     *         the server files
     *
     * @return the list
     */
    private List< FileInfo > addHtmlObjectToServerFile( String authToken, String selectionId, List< FileInfo > serverFiles ) {
        try {
            List< String > selectedObjectId = JsonUtils.jsonToList( getAllSelectedItemsFromServer( authToken, selectionId ), String.class );
            List< FileInfo > allServerFiles = new ArrayList< FileInfo >();
            for ( FileInfo file : serverFiles ) {
                DataObjectHtmlDTO SuSObjectEntity = getDataObjectHtmlDTOById( file.getId(), authToken );
                if ( selectedObjectId.contains( SuSObjectEntity.getId().toString() ) ) {
                    if ( file.getType().equalsIgnoreCase( FILE_TYPE_HTML ) ) {
                        allServerFiles.add( file );
                        if ( SuSObjectEntity.getZipFile() != null ) {
                            allServerFiles.add( prepareZipFileInfo( file, SuSObjectEntity.getZipFile() ) );
                        }
                    } else {
                        allServerFiles.add( file );
                    }
                }
            }
            if ( !allServerFiles.isEmpty() ) {
                serverFiles = allServerFiles;
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return serverFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{containerId}/discard/{selectionId}", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > discardObjects( @PathVariable String containerId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken, @PathVariable String selectionId ) {
        try {
            printStarBorderInLogFile( DISCARD );
            // just for return to FE
            List< FileInfo > addAndReturnDiscardFiles = new ArrayList<>();

            if ( !checkReadPermission( containerId, authToken ) ) {
                return new ResponseEntity<>( ResponseUtils.successInfoResponse( USER_DONT_HAVE_READ_PERMISSION, null ), HttpStatus.OK );
            }

            List< FileInfo > serverFiles = getAllItemsFromServer( containerId, authToken );
            String json = getAllSelectedItemsFromServer( authToken, selectionId );
            List< String > namesInSelection = JsonUtils.jsonToList( json, String.class );

            Map< String, String > selectedIdsMap = new HashMap<>();
            for ( String name : namesInSelection ) {
                selectedIdsMap.put( name.toLowerCase(), name );
            }

            // evaluate if user selected to download an object, by name of the
            // object.
            evaluateAndDiscardObject( authToken, serverFiles, selectedIdsMap, addAndReturnDiscardFiles, containerId );

            return new ResponseEntity<>(
                    ResponseUtils.successResponse( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(),
                            authToken, Messages.DISCARD_OBJECT.getKey(), addAndReturnDiscardFiles.size() ), addAndReturnDiscardFiles ),
                    HttpStatus.OK );

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{containerId}/checkin/{selectionId}", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > checkinObjects( @PathVariable String containerId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken, @PathVariable String selectionId ) {
        try {
            printStarBorderInLogFile( CHECK_IN );
            if ( !checkReadPermission( containerId, authToken ) ) {
                return new ResponseEntity<>( ResponseUtils.successInfoResponse( USER_DONT_HAVE_READ_PERMISSION, null ), HttpStatus.OK );
            }
            List< FileInfo > checkinFiles = new ArrayList<>();
            // All items from server
            List< FileInfo > serverFile = getAllItemsFromServer( containerId, authToken );
            // All selected IDs from server
            List< String > namesInSelection = JsonUtils.jsonToList( getAllSelectedItemsFromServer( authToken, selectionId ), String.class );
            // get from server the container to compute path
            File localDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir()
                    + getRelativeLocalSynchPathFromBreadCrumb( containerId, authToken ) );
            Map< String, LocalFilesToSync > selectedIdsMaps = prepareIdsMapForCheckIn( serverFile, namesInSelection, localDir );
            ProjectConfiguration configProject = getSusObjectModelListFromAPI( containerId, authToken );
            if ( configProject != null ) {
                // evaluate if user selected to upload an object, by name of the object, and not exists on server.
                evaluateAndCheckinObjectAndUploadLocallyChangedFiles( containerId, authToken, selectedIdsMaps,
                        configProject.getEntityConfig(), serverFile );
            } else {
                throw new SusException( PROJECT_CONFIGRATION_IS_NOT_LOADED );
            }
            // all files are checked against every condition now upload prepared files/objects adding entries into progress bar
            computedFiles.stream().forEach( preparedFile -> {
                if ( preparedFile.isCheckInFileToUpload() ) {
                    transferManager.getTransfers().put( preparedFile.getFileName(),
                            new TransferInfo( 0L, UPLOAD, localDir.getPath() + File.separator + preparedFile.getFileName() ) );
                }
                if ( !preparedFile.isAbort() ) {
                    if ( preparedFile.isCheckInFileToUpload() ) {
                        // if file is already synch not allowed to upload
                        transferManager.uploadDataObjectToLocations( containerId, preparedFile.getFileName(), authToken,
                                preparedFile.getObjectId(), preparedFile.getObjectType(), preparedFile.getSize(),
                                preparedFile.getLocation() );
                    }
                    try {
                        SuSClient.getRequest( daemonManager.getServerAPIBase() + DATA_PROJECT + preparedFile.getFileId() + CHECKOUT
                                + ConstantsDbOperationTypes.CHECKIN, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
                    } catch ( IOException e ) {
                        ExceptionLogger.logException( e, getClass() );
                    }
                    preparedFile.setComplete( true );
                    checkinFiles.add( new FileInfo( preparedFile.getFileName() ) );
                }
            } );
            // removing extra entries from list
            removeCompleteOrAbortedFilesFromList();
            return new ResponseEntity<>(
                    ResponseUtils.successResponse( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(),
                            authToken, Messages.CHECKIN_OBJECT.getKey(), checkinFiles.size() ), checkinFiles ),
                    HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/type/{typeId}/list", method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > getAllObjects( @PathVariable String projectId, @PathVariable String typeId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken, @RequestBody String objectFilterJson ) {
        try {
            printStarBorderInLogFile( "get all data objects" );
            return new ResponseEntity<>( ResponseUtils.successResponse( getAllItems( projectId, typeId, authToken, objectFilterJson ) ),
                    HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            if ( e.getMessage().equalsIgnoreCase( SQL_EXCEPTION ) ) {
                return new ResponseEntity<>( ResponseUtils.successInfoResponse( "Retry: List API processing", true ), HttpStatus.OK );
            }
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/type/{typeId}/ui", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getAllObjectsUI( @PathVariable String projectId, @PathVariable String typeId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.getRequest( daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + LOCAL + "/ui",
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            logger.debug( "local result UI  : " + request.getData().toString() );
            TableUI tableViewFiles = JsonUtils.jsonToObject( JsonUtils.toJson( request.getData() ), TableUI.class );
            TableUI allItemsUI = getAllItemsUI( projectId, typeId, authToken );
            if ( typeId.equals( SimuspaceFeaturesEnum.IMAGE_DATA_OBJECTS.getId() )
                    || projectId.equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
                return new ResponseEntity<>( ResponseUtils.successResponse( allItemsUI ), HttpStatus.OK );
            }
            return new ResponseEntity<>( ResponseUtils.successResponse( getAllTables( tableViewFiles, allItemsUI ) ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "/abort/{selectionId}", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > performAbortOperation( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable String selectionId ) {
        try {
            printStarBorderInLogFile( ABORT );
            // just for return to FE
            List< ComputedDataObject > abortedFiles = new ArrayList<>();
            // get from server selected IDs
            String json = getSelectedFilesFromSelection( authToken, selectionId );
            List< String > namesInSelection = JsonUtils.jsonToList( json, String.class );
            Map< String, String > selectedIdsMap = new HashMap<>();
            namesInSelection.stream().forEach( path -> {
                File extractName = new File( path );
                selectedIdsMap.put( extractName.getName(), path );
                abortFileUpload( extractName.getName() );
            } );
            logger.debug( "aborting " + selectedIdsMap );
            computedFiles.stream().forEach( abortfile -> {
                if ( selectedIdsMap.containsKey( abortfile.getFileName() )
                        && selectedIdsMap.get( abortfile.getFileName() ).equals( abortfile.getFilePath() ) && !abortfile.isAbort()
                        && !abortfile.isComplete() ) {
                    abortfile.setAbort( true );
                    abortedFiles.add( abortfile );
                    selectedIdsMap.remove( abortfile.getFileName() );
                }
            } );
            return new ResponseEntity<>( ResponseUtils.successResponse( MessageBundleFactory.getExternalMessage(
                    DaemonPropertiesManager.hasTranslation(), authToken, Messages.ABORT_OBJECT.getKey() ), abortedFiles ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Abort file upload.
     *
     * @param fileName
     *         the fileName
     */
    private void abortFileUpload( String fileName ) {
        if ( transferManager.getTransfers().containsKey( fileName ) ) {
            TransferInfo transferInfo = transferManager.getTransfers().get( fileName );
            if ( transferInfo.getHttpPost() != null ) {
                transferInfo.getHttpPost().abort();
            }
            if ( transferInfo.getHttpGet() != null ) {
                transferInfo.getHttpGet().abort();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{containerId}/type/{typeId}/sync", method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > dragAndSaveFile( @PathVariable String containerId, @PathVariable String typeId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken, @RequestBody String filePath ) {
        File localDir = null;
        try {
            printStarBorderInLogFile( DRAG_AND_DROP );
            String containerName = getRelativeLocalSynchPathFromBreadCrumb( containerId, authToken );
            String destination = daemonManager.localSyncDirectory().getLocal_sync_dir() + containerName;

            List< String > localPath = JsonUtils.jsonToList( filePath, String.class );
            for ( String sources : localPath ) {
                File sourceFile = new File( sources );
                File destFile = new File( destination + File.separator + sourceFile.getName() );
                if ( destFile.getPath().equals( sourceFile.getPath() ) ) {
                    return new ResponseEntity<>( ResponseUtils.failureResponse( CANT_COPY_SOURCE_AND_DESTINATION_ARE_SAME,
                            "source :" + sourceFile + ": destination :" + destFile ), HttpStatus.OK );
                }
            }
            localDir = new File( destination );
            if ( !localDir.exists() ) {
                localDir.mkdirs();
            }
            for ( String source : localPath ) {
                File sourceLocation = new File( source );
                if ( sourceLocation.isDirectory() ) {
                    if ( !sourceLocation.exists() ) {
                        sourceLocation.mkdirs();
                    }
                    transferManager.getTransfers().put( sourceLocation.getName(),
                            new TransferInfo( 0L, COPY_TO_LOCAL, sourceLocation.getPath() ) );
                    File targetLocation = new File( destination + File.separator + sourceLocation.getName() );
                    org.apache.commons.io.FileUtils.copyDirectory( new File( sourceLocation.getPath() + File.separator ),
                            new File( targetLocation.getPath() + File.separator ) );
                    transferManager.getTransfers().put( sourceLocation.getName(),
                            new TransferInfo( 100L, COPY_TO_LOCAL, sourceLocation.getPath() ) );
                } else if ( sourceLocation.isFile() ) {
                    transferManager.getTransfers().put( sourceLocation.getName(),
                            new TransferInfo( 0L, COPY_TO_LOCAL, sourceLocation.getPath() ) );
                    String desPath = destination + File.separator + sourceLocation.getName();
                    File destFile = new File( desPath );
                    com.google.common.io.Files.copy( sourceLocation, destFile );
                    transferManager.getTransfers().put( sourceLocation.getName(),
                            new TransferInfo( 100L, COPY_TO_LOCAL, sourceLocation.getPath() ) );
                }
            }
            return new ResponseEntity<>( ResponseUtils.successResponse( OBJECT_COPY_SUCCESSFULL, localDir ), HttpStatus.OK );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.toString(), localDir ), HttpStatus.OK );
        }
    }

    /**
     * **************************************** project local view ****************************************.
     *
     * @param projectId
     *            the project id
     * @param authToken
     *            the auth token
     * @return the all project local views by project id
     */

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/local" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getAllProjectLocalViewsByProjectId( @PathVariable String projectId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.getRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + LOCAL + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String json = JsonUtils.toJson( request.getData() );
            List< ObjectViewDTO > objectViews = JsonUtils.jsonToList( json, ObjectViewDTO.class );
            return new ResponseEntity<>( ResponseUtils.successResponse( objectViews ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/local" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > saveProjectLocalViewByProjectId( @PathVariable String projectId,
            @RequestBody String objectFilterJson, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.postRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + LOCAL + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW,
                    objectFilterJson, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String json = JsonUtils.toJson( request.getData() );
            ObjectViewDTO objectView = JsonUtils.jsonToObject( json, ObjectViewDTO.class );
            return new ResponseEntity<>( ResponseUtils.successResponse( objectView ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/local" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > setProjectLocalViewAsDefaultByProjectId( @PathVariable String projectId,
            @PathVariable String viewId, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.getRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + LOCAL + UI_VIEW + viewId + DEFAULT,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String json = JsonUtils.toJson( request.getData() );
            ObjectViewDTO objectView = JsonUtils.jsonToObject( json, ObjectViewDTO.class );
            return new ResponseEntity<>( ResponseUtils.successResponse( objectView ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/local" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW, method = RequestMethod.DELETE )
    public ResponseEntity< SusResponseDTO > deleteProjectLocalViewByProjectId( @PathVariable String projectId, @PathVariable String viewId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.deleteRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + LOCAL + UI_VIEW + viewId,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            if ( Boolean.parseBoolean( request.getData().toString() ) ) {
                return new ResponseEntity<>(
                        ResponseUtils.successResponse( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(),
                                authToken, Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true ),
                        HttpStatus.OK );
            } else {
                return new ResponseEntity<>( ResponseUtils.successResponse( MessageBundleFactory
                        .getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken, Messages.VIEW_DOES_NOT_EXIST.getKey() ) ),
                        HttpStatus.OK );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/local" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW, method = RequestMethod.PUT )
    public ResponseEntity< SusResponseDTO > updateProjectLocalViewByProjectId( @PathVariable String projectId, @PathVariable String viewId,
            @RequestBody String objectJson, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.putRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + LOCAL + UI_VIEW + viewId,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ), objectJson );
            String json = JsonUtils.toJson( request.getData() );
            ObjectViewDTO objectView = JsonUtils.jsonToObject( json, ObjectViewDTO.class );
            return new ResponseEntity<>( ResponseUtils.successResponse( objectView ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * **************************************** project sync view ****************************************.
     *
     * @param projectId
     *            the project id
     * @param authToken
     *            the auth token
     * @return the all project sync views by project id
     */

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/sync" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getAllProjectSyncViewsByProjectId( @PathVariable String projectId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.getRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + SYNC + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String json = JsonUtils.toJson( request.getData() );
            List< ObjectViewDTO > objectViews = JsonUtils.jsonToList( json, ObjectViewDTO.class );
            return new ResponseEntity<>( ResponseUtils.successResponse( objectViews ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/sync" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > saveProjectSyncViewByProjectId( @PathVariable String projectId,
            @RequestBody String objectFilterJson, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.postRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + SYNC + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW,
                    objectFilterJson, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String json = JsonUtils.toJson( request.getData() );
            ObjectViewDTO objectView = JsonUtils.jsonToObject( json, ObjectViewDTO.class );
            return new ResponseEntity<>( ResponseUtils.successResponse( objectView ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/sync" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > setProjectSyncViewAsDefaultByProjectId( @PathVariable String projectId,
            @PathVariable String viewId, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.getRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + SYNC + UI_VIEW + viewId + DEFAULT,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String json = JsonUtils.toJson( request.getData() );
            ObjectViewDTO objectView = JsonUtils.jsonToObject( json, ObjectViewDTO.class );
            return new ResponseEntity<>( ResponseUtils.successResponse( objectView ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/sync" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW, method = RequestMethod.DELETE )
    public ResponseEntity< SusResponseDTO > deleteProjectSyncViewByProjectId( @PathVariable String projectId, @PathVariable String viewId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.deleteRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + SYNC + UI_VIEW + viewId,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );

            if ( Boolean.parseBoolean( request.getData().toString() ) ) {
                return new ResponseEntity<>(
                        ResponseUtils.successResponse( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(),
                                authToken, Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true ),
                        HttpStatus.OK );
            } else {
                return new ResponseEntity<>( ResponseUtils.successResponse( MessageBundleFactory
                        .getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken, Messages.VIEW_DOES_NOT_EXIST.getKey() ) ),
                        HttpStatus.OK );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/sync" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW, method = RequestMethod.PUT )
    public ResponseEntity< SusResponseDTO > updateProjectSyncViewByProjectId( @PathVariable String projectId, @PathVariable String viewId,
            @RequestBody String objectJson, @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            SusResponseDTO request = SuSClient.putRequest(
                    daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + SYNC + UI_VIEW + viewId,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ), objectJson );
            String json = JsonUtils.toJson( request.getData() );
            ObjectViewDTO objectView = JsonUtils.jsonToObject( json, ObjectViewDTO.class );
            return new ResponseEntity<>( ResponseUtils.successResponse( objectView ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "{projectId}/sync/download/directory", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > createStructureInLocalSyncDir( @PathVariable String projectId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            // api for checking export permission for user
            if ( !checkReadPermission( projectId, authToken ) ) {
                return new ResponseEntity<>( ResponseUtils.successInfoResponse( USER_DONT_HAVE_READ_PERMISSION, null ), HttpStatus.OK );
            }
            File projectLocalDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir()
                    + getRelativeLocalSynchPathFromBreadCrumb( projectId, authToken ) );
            if ( !projectLocalDir.exists() ) {
                File m = new File( projectLocalDir.getPath() );
                m.mkdirs();
            }
            return new ResponseEntity<>(
                    ResponseUtils.successResponse( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(),
                            authToken, Messages.DIRECTORY_CREATED_SUCCESSFULLY.getKey() ), true ),
                    HttpStatus.OK );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Gets the change type option form.
     *
     * @param projectId
     *         the parent id
     * @param selectionId
     *         the selection id
     * @param authToken
     *         the auth token
     *
     * @return the change type option form
     */
    @RequestMapping( value = "/{projectId}/selection/{selectionId}/changetype/ui", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > changeTypeObjectOptionForm( @PathVariable String projectId, @PathVariable String selectionId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            return new ResponseEntity<>( ResponseUtils.successResponse( changeTypeOptionForm( authToken, projectId, selectionId ) ),
                    HttpStatus.OK );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }

    }

    /**
     * Change type of object.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param authToken
     *         the auth token
     * @param json
     *         the json
     *
     * @return the response
     */
    @RequestMapping( value = "/{projectId}/selection/{selectionId}/changetype", method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > changeTypeObject( @PathVariable String projectId, @PathVariable String selectionId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken, @RequestBody String json ) {
        try {
            String type = null;
            Map< String, String > typeMap = new HashMap<>();
            typeMap = ( Map< String, String > ) JsonUtils.jsonToMap( json, typeMap );
            type = typeMap.get( CHANGE_TYPE );

            for ( String localFileId : JsonUtils.jsonToList( getAllSelectedItemsFromServer( authToken, selectionId ), String.class ) ) {
                ComputedDataObjectEntity entity;
                entity = sqLiteManager.findById( localFileId );
                entity.setType( type );
                entity.setTypeUpdated( true );
                sqLiteManager.save( entity );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
        return new ResponseEntity<>( ResponseUtils.successResponse( MessageBundleFactory.getExternalMessage(
                DaemonPropertiesManager.hasTranslation(), authToken, Messages.TYPE_UPDATED_SUCCESSFULLY.getKey() ), null ), HttpStatus.OK );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = "/export/{containerId}/{selectionId}", method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > exportDataObject( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( value = "containerId" ) String containerId, @PathVariable( value = "selectionId" ) String selectionId,
            @RequestBody String osPath ) {
        try {
            printStarBorderInLogFile( "export data object" );
            ExportDTO exportDTO = JsonUtils.jsonToObject( osPath, ExportDTO.class );
            String path = exportDTO.getExportPath().getItems().get( 0 );
            final SusResponseDTO responseDTO = SuSClient.getRequest(
                    daemonManager.getServerAPIBase() + DATA_OBJECT + selectionId + PROPERTIES_DATA,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            if ( responseDTO.getData() == null ) {
                return new ResponseEntity<>( ResponseUtils.successInfoResponse( responseDTO.getMessage().getContent(), null ),
                        HttpStatus.OK );
            }
            final String documentDTOResponse = JsonUtils.objectToJson( responseDTO.getData() );
            DataObjectDTO dataObjectDTO = JsonUtils.jsonToObject( documentDTOResponse, DataObjectDTO.class );
            logger.debug( "object by id > " + dataObjectDTO.toString() );
            ProjectConfiguration configProject = getSusObjectModelListFromAPI( containerId, authToken );
            boolean isDir = true;
            // check if data object is container or not and contains file or not
            if ( configProject != null ) {
                for ( SuSObjectModel suSObjectModel : configProject.getEntityConfig() ) {

                    if ( suSObjectModel.getId().equals( dataObjectDTO.getTypeId().toString() )
                            && ( !( classInstanceFromConfig( suSObjectModel ) instanceof ContainerEntity ) ) ) {
                        isDir = false;
                        if ( dataObjectDTO.getFile() == null ) {
                            return new ResponseEntity<>(
                                    ResponseUtils.successInfoResponse( dataObjectDTO.getName() + OBJECT_DOES_NOT_CONTAIN_ANY_FILE, null ),
                                    HttpStatus.OK );
                        } else {
                            transferManager.getTransfers().put( dataObjectDTO.getName(),
                                    new TransferInfo( 0L, EXPORT_OBJECT, path + File.separator + dataObjectDTO.getName() ) );
                        }
                    }
                }
            }
            logger.debug( "object export started" );
            ExportDataObjectThread dataObjectThread = new ExportDataObjectThread( dataObjectDTO, isDir, path, authToken, this,
                    transferManager );
            getThreadPoolExecutorService().exportDataobjectExecute( dataObjectThread );
            return new ResponseEntity<>( ResponseUtils.successResponse( true ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            StringWriter sw = new StringWriter();
            e.printStackTrace( new PrintWriter( sw ) );
            String exceptionAsString = sw.toString();
            return new ResponseEntity<>( ResponseUtils.failureResponse( exceptionAsString, null ), HttpStatus.OK );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recursivelyDownloadStructuralObjects( DataObjectDTO dataObjectDTO, boolean isDir, String exportPath, String authToken ) {
        try {
            final String relativeProjectPath = createDirectoryStructure(
                    SuSClient.getRequest( daemonManager.getServerAPIBase() + BREADCRUMB_VIEW_DATA_PROJECT + dataObjectDTO.getId(),
                            CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );

            final File projectLocalDir = new File( exportPath + relativeProjectPath );
            if ( ( dataObjectDTO.getFile() != null ) && !isDir ) {
                final File downloadPath = projectLocalDir.getParentFile();
                if ( !downloadPath.exists() ) {
                    downloadPath.mkdirs();
                }
                downloadObject( dataObjectDTO, downloadPath, authToken );
            } else {
                projectLocalDir.mkdirs();
            }
            dataObjectDTO.getModifiedBy().setSusUserDirectoryDTO( null );
            dataObjectDTO.getCreatedBy().setSusUserDirectoryDTO( null );

            final List< FileInfo > serverFiles = getServerFiles(
                    SuSClient.getRequest( daemonManager.getServerAPIBase() + DATA_PROJECT + dataObjectDTO.getId() + DATA_LIST,
                            CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );
            for ( final FileInfo fileInfo : serverFiles ) {
                final DataObjectDTO objectDTO = getDataObjectsById( fileInfo.getId(), authToken );
                recursivelyDownloadStructuralObjects( objectDTO, fileInfo.isDir(), exportPath, authToken );
            }
        } catch ( Exception e ) {
            logger.debug( e );
        }
    }

    /**
     * Download object.
     *
     * @param dataObjectDTO
     *         the dataObjectDTO
     * @param path
     *         the path
     * @param authToken
     *         the auth token
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void downloadObject( DataObjectDTO dataObjectDTO, File path, String authToken ) throws IOException {
        // it might be daemon api so chenge it
        SuSClient.downloadRequest(
                daemonManager.getServerAPIBase() + API_DOCUMENT + dataObjectDTO.getFile().getId() + "/version/"
                        + dataObjectDTO.getFile().getVersion().getId() + "/download",
                path.getAbsolutePath(), prepareDownloadHeaders( authToken ), null );

        downloadAdditionalFilesOfObject( dataObjectDTO, path, authToken );
    }

    /**
     * Download additional files of object.
     *
     * @param dataObjectDTO
     *         the dataObjectDTO
     * @param path
     *         the path
     * @param authToken
     *         the auth token
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void downloadAdditionalFilesOfObject( DataObjectDTO dataObjectDTO, File path, String authToken ) throws IOException {
        if ( dataObjectDTO.getType().equals( "PredictionModel" ) ) {

            final SusResponseDTO responseDTO = SuSClient.getRequest(
                    daemonManager.getServerAPIBase() + DATA_OBJECT + dataObjectDTO.getId() + PROPERTIES_DATA,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );

            if ( responseDTO.getData() == null ) {
                return;
            }

            final String documentDTOResponse = JsonUtils.objectToJson( responseDTO.getData() );
            DataObjectPredictionModelDTO predictionModelObject = JsonUtils.jsonToObject( documentDTOResponse,
                    DataObjectPredictionModelDTO.class );

            if ( predictionModelObject == null ) {
                return;
            }

            // download json file of PredictionModel object
            if ( predictionModelObject.getJsonFile() != null ) {
                SuSClient.downloadRequest(
                        daemonManager.getServerAPIBase() + API_DOCUMENT + predictionModelObject.getJsonFile().getId() + "/version/"
                                + predictionModelObject.getJsonFile().getVersion().getId() + "/download",
                        path.getAbsolutePath(), prepareDownloadHeaders( authToken ), null );
            }

            // download bin file of PredictionModel object
            if ( predictionModelObject.getBinFile() != null ) {
                SuSClient.downloadRequest(
                        daemonManager.getServerAPIBase() + API_DOCUMENT + predictionModelObject.getBinFile().getId() + "/version/"
                                + predictionModelObject.getBinFile().getVersion().getId() + "/download",
                        path.getAbsolutePath(), prepareDownloadHeaders( authToken ), null );
            }
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
     * Gets the data objects by id.
     *
     * @param objectId
     *         the object id
     * @param authToken
     *         the auth token
     *
     * @return the data objects by id
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private DataObjectDTO getDataObjectsById( String objectId, String authToken ) throws IOException {
        DataObjectDTO dataObj = null;
        final SusResponseDTO responseDTO = SuSClient.getRequest(
                daemonManager.getServerAPIBase() + DATA_OBJECT + objectId + PROPERTIES_DATA,
                CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        if ( responseDTO.getSuccess() ) {
            final String documentDTOResponse = JsonUtils.objectToJson( responseDTO.getData() );
            dataObj = JsonUtils.jsonToObject( documentDTOResponse, DataObjectDTO.class );
        } else {
            updateIsExistOnServer( objectId );
        }
        return dataObj;
    }

    /**
     * Update is exist on server.
     *
     * @param objectId
     *         the object id
     */
    private void updateIsExistOnServer( String objectId ) {
        ComputedDataObjectEntity computedDataObject = sqLiteManager.findById( objectId );
        computedDataObject.setExistOnServer( false );
        sqLiteManager.save( computedDataObject );
    }

    /**
     * Prepare all object entity from DTO.
     *
     * @param allObjectsDTO
     *         the all objects DTO
     *
     * @return the all object entity
     */
    private AllObjectEntity prepareAllObjectEntityFromDTO( AllObjectsDTO allObjectsDTO ) {
        AllObjectEntity allObjectEntity = new AllObjectEntity();
        allObjectEntity.setId( allObjectsDTO.getId() );
        allObjectEntity.setCheckedOut( allObjectsDTO.getCheckedOut() );
        if ( allObjectsDTO.getCheckedOutUser() != null ) {
            allObjectEntity.setCheckedOutUser( prepareUserEntityFromUserModel( allObjectsDTO.getCheckedOutUser() ) );
        }

        if ( allObjectsDTO.getCreatedBy() != null ) {
            allObjectEntity.setCreatedBy( prepareUserEntityFromUserModel( allObjectsDTO.getCreatedBy() ) );
        }
        if ( allObjectsDTO.getCustomAttributes() != null ) {
            allObjectEntity.setCustomAttributes( JsonUtils.convertMapToString( allObjectsDTO.getCustomAttributes() ) );
        }
        allObjectEntity.setCreatedOn( allObjectsDTO.getCreatedOn() );
        allObjectEntity.setDescription( allObjectsDTO.getDescription() );
        allObjectEntity.setFileId( allObjectsDTO.getFileId() );
        allObjectEntity.setIcon( allObjectsDTO.getIcon() );

        allObjectEntity.setAutoDeleted( allObjectsDTO.isAutoDeleted() );
        allObjectEntity.setLifeCycleStatus( allObjectsDTO.getLifeCycleStatus() );
        allObjectEntity.setLink( allObjectsDTO.getLink() );
        allObjectEntity.setLocations( allObjectsDTO.getLocations() );
        if ( allObjectsDTO.getModifiedBy() != null ) {
            allObjectEntity.setModifiedBy( prepareUserEntityFromUserModel( allObjectsDTO.getModifiedBy() ) );
        }
        allObjectEntity.setName( allObjectsDTO.getName() );
        allObjectEntity.setParentId( allObjectsDTO.getParentId() );

        if ( allObjectsDTO.getSize() != null && org.apache.commons.lang3.StringUtils.isNumeric( allObjectsDTO.getSize() ) ) {
            allObjectEntity.setSize( Long.parseLong( allObjectsDTO.getSize() ) );
        }

        allObjectEntity.setSyncStatus( allObjectsDTO.getSyncStatus() );
        allObjectEntity.setType( allObjectsDTO.getType() );
        allObjectEntity.setTypeId( allObjectsDTO.getTypeId() );
        allObjectEntity.setModifiedOn( allObjectsDTO.getModifiedOn() );
        allObjectEntity.setUrlType( allObjectsDTO.getUrlType() );
        allObjectEntity.setVersion( allObjectsDTO.getVersion() );
        return allObjectEntity;

    }

    /**
     * Prepare all object entity from DTO.
     *
     * @param allObjectEntity
     *         the all object entity
     *
     * @return the all object entity
     */
    private AllObjectsDTO prepareAllObjectDTOFromEntity( AllObjectEntity allObjectEntity ) {
        AllObjectsDTO allObjectsDTO = new AllObjectsDTO();
        allObjectsDTO.setId( allObjectEntity.getId() );
        allObjectsDTO.setCheckedOut( allObjectEntity.getCheckedOut() );
        if ( allObjectEntity.getCheckedOutUser() != null ) {
            allObjectsDTO.setCheckedOutUser( prepareUserModelFromUserEntity( allObjectEntity.getCheckedOutUser() ) );
        }

        if ( allObjectEntity.getCreatedBy() != null ) {
            allObjectsDTO.setCreatedBy( prepareUserModelFromUserEntity( allObjectEntity.getCreatedBy() ) );
        }
        if ( allObjectEntity.getCustomAttributes() != null ) {
            allObjectsDTO.setCustomAttributes(
                    ( Map< String, String > ) JsonUtils.jsonToMap( allObjectEntity.getCustomAttributes(), new HashMap<>() ) );
        }

        allObjectsDTO.setCreatedOn( allObjectEntity.getCreatedOn() );
        allObjectsDTO.setDescription( allObjectEntity.getDescription() );
        allObjectsDTO.setFileId( allObjectEntity.getFileId() );
        allObjectsDTO.setIcon( allObjectEntity.getIcon() );

        allObjectsDTO.setAutoDeleted( allObjectEntity.isAutoDeleted() );
        allObjectsDTO.setLifeCycleStatus( allObjectEntity.getLifeCycleStatus() );
        allObjectsDTO.setLink( allObjectEntity.getLink() );
        allObjectsDTO.setLocations( allObjectEntity.getLocations() );
        if ( allObjectEntity.getModifiedBy() != null ) {

            allObjectsDTO.setModifiedBy( prepareUserModelFromUserEntity( allObjectEntity.getModifiedBy() ) );
        }
        allObjectsDTO.setName( allObjectEntity.getName() );
        allObjectsDTO.setParentId( allObjectEntity.getParentId() );
        allObjectsDTO.setSyncStatus( allObjectEntity.getSyncStatus() );
        allObjectsDTO.setType( allObjectEntity.getType() );
        allObjectsDTO.setTypeId( allObjectEntity.getTypeId() );
        allObjectsDTO.setModifiedOn( allObjectEntity.getModifiedOn() );
        allObjectsDTO.setUrlType( allObjectEntity.getUrlType() );
        allObjectsDTO.setVersion( allObjectEntity.getVersion() );
        if ( allObjectEntity.getSize() != null && allObjectEntity.getSize() != ConstantsInteger.INTEGER_VALUE_ZERO ) {
            allObjectsDTO.setSize( org.apache.commons.io.FileUtils.byteCountToDisplaySize( allObjectEntity.getSize() ) );
        } else {
            allObjectsDTO.setSize( ConstantsString.NOT_AVAILABLE );
        }

        return allObjectsDTO;

    }

    /**
     * Prepare user entity from user model.
     *
     * @param user
     *         the user
     *
     * @return the user daemon entity
     */
    private UserDaemonEntity prepareUserEntityFromUserModel( UserDTO user ) {
        UserDaemonEntity userEntity = new UserDaemonEntity();
        userEntity.setId( user.getId() );
        userEntity.setDescription( user.getDescription() );
        userEntity.setFirstName( user.getFirstName() );
        userEntity.setSurName( user.getSurName() );
        userEntity.setUserUid( user.getUserUid() );
        userEntity.setStatus( user.getStatus().equals( ConstantsStatus.ACTIVE ) );
        userEntity.setRestricted( user.getStatus().equals( ConstantsStatus.YES ) );
        userEntity.setChangeable( user.isChangable() );
        userEntity.setLocationPreferenceSelectionId( user.getLocationPreferenceSelectionId() );
        UserDaemonEntity userDaemomEntity = sqLiteManager.findUserById( userEntity.getId() );
        if ( userDaemomEntity == null ) {
            sqLiteManager.saveUser( userEntity );
        }
        return userEntity;
    }

    /**
     * Prepare user model from user entity.
     *
     * @param userEntity
     *         the user entity
     *
     * @return the user DTO
     */
    private UserDTO prepareUserModelFromUserEntity( UserDaemonEntity userEntity ) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId( userEntity.getId() );
        userDTO.setDescription( userEntity.getDescription() );
        userDTO.setFirstName( userEntity.getFirstName() );
        userDTO.setSurName( userEntity.getSurName() );
        userDTO.setUserUid( userEntity.getUserUid() );
        userDTO.setStatus( userEntity.getStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
        userDTO.setRestricted( userEntity.isRestricted() ? ConstantsStatus.YES : ConstantsStatus.NO );
        userDTO.setChangable( userEntity.getChangeable() );
        userDTO.setLocationPreferenceSelectionId( userDTO.getLocationPreferenceSelectionId() );
        return userDTO;
    }

    /**
     * Compare local sync.
     *
     * @param serverFiles
     *         the server files
     * @param filterDTO
     *         the filter DTO
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param isLocal
     *         the is local
     *
     * @return the list
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private List< Object > compareLocalSync( List< FileInfo > serverFiles, FiltersDTO filterDTO, String containerId, String authToken,
            boolean isLocal ) throws IOException {
        ListFilterUtil rows = new ListFilterUtil();
        // get local sync dir items with hash
        Map< String, FileInfo > localFiles = new HashMap<>();
        File localDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir()
                + getRelativeLocalSynchPathFromBreadCrumb( containerId, authToken ) );
        SyncJson syncConf = validateSynchJsonPath( findSyncFileinSyncDirectory( localDir ) );
        ProjectConfiguration configProject = getSusObjectModelListFromAPI( containerId, authToken );
        if ( localDir.exists() ) {
            for ( File f : localDir.listFiles() ) {
                if ( isLocal ) {
                    saveLocalObjects( containerId, localFiles, syncConf, configProject, f, "", serverFiles );
                } else {
                    saveSynchObjects( containerId, localFiles, f, f.isDirectory(), syncConf, configProject, serverFiles );
                }
            }
        }
        // compare and prepare table response
        Map< String, FileInfo > serverProcessedFiles = new HashMap<>();
        // method to compare and prepare table response
        compareAndPrepareTableResponse( rows, localFiles, serverFiles, serverProcessedFiles, isLocal );
        // process local files
        localFiles.entrySet().parallelStream().forEach( localfilename -> prepareRowForSyncList( isLocal, rows, serverProcessedFiles,
                syncConf, localfilename.getValue(), configProject ) );
        if ( filterDTO != null ) {
            ListFilterUtil paginatedList = rows.paginateList( filterDTO );
            filterDTO.setTotalRecords( ( long ) rows.size() );
            filterDTO.setFilteredRecords( ( long ) rows.size() );
            return paginatedList;
        } else {
            return rows;
        }
    }

    /**
     * Compare local sync data api.
     *
     * @param serverFiles
     *         the server files
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     *
     * @return the sets the
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private Set< AllObjectsDTO > compareLocalSyncDataApi( List< FileInfo > serverFiles, String containerId, String authToken )
            throws IOException {
        Set< AllObjectsDTO > allObjectsDTO = new HashSet<>();
        // get from server the container to compute path
        String relativeLocalSyncPath = getRelativeLocalSynchPathFromBreadCrumb( containerId, authToken );
        File localDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir() + relativeLocalSyncPath );
        File syncFilePath = findSyncFileinSyncDirectory( localDir );
        SyncJson syncConf = null;
        syncConf = validateSynchJsonPath( syncFilePath );
        ProjectConfiguration configProject = getSusObjectModelListFromAPI( containerId, authToken );
        List< Object > synch = saveAndGetSychProcessedData( serverFiles, containerId, localDir, syncConf, configProject, authToken );

        if ( !synch.isEmpty() ) {
            allObjectsDTO.addAll( JsonUtils.jsonToList( JsonUtils.toJson( synch ), AllObjectsDTO.class ) );
        }
        // Local files
        List< Object > local = saveAndGetLocalProcessedData( serverFiles, containerId, localDir, syncConf, configProject );
        if ( !local.isEmpty() ) {
            allObjectsDTO.addAll( JsonUtils.jsonToList( JsonUtils.toJson( local ), AllObjectsDTO.class ) );
        }
        logger.info( "DATA Added: " + allObjectsDTO.size() );
        return allObjectsDTO;
    }

    /**
     * Save and get sych processed data.
     *
     * @param serverFiles
     *         the server files
     * @param containerId
     *         the container id
     * @param localDir
     *         the local dir
     * @param syncConf
     *         the sync conf
     * @param configProject
     *         the config project
     * @param authToken
     *         the auth token
     *
     * @return the list
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private List< Object > saveAndGetSychProcessedData( List< FileInfo > serverFiles, String containerId, File localDir, SyncJson syncConf,
            ProjectConfiguration configProject, String authToken ) throws IOException {

        ListFilterUtil rows = new ListFilterUtil();
        Map< String, FileInfo > localFiles = new HashMap<>();
        if ( localDir.exists() ) {
            for ( File f : localDir.listFiles() ) {
                saveSynchObjects( containerId, localFiles, f, f.isDirectory(), syncConf, configProject, serverFiles );
            }
        }
        // compare and prepare table response
        Map< String, FileInfo > serverProcessedFiles = new HashMap<>();
        // method to compare and prepare table response
        compareAndPrepareTableResponse( rows, localFiles, serverFiles, serverProcessedFiles, false );

        // process local files
        for ( Map.Entry< String, FileInfo > localfilename : localFiles.entrySet() ) {
            prepareRowForSyncList( false, rows, serverProcessedFiles, syncConf, localfilename.getValue(), configProject );
        }
        return rows;
    }

    /**
     * Save and get local processed data.
     *
     * @param serverFiles
     *         the server files
     * @param containerId
     *         the container id
     * @param localDir
     *         the local dir
     * @param syncConf
     *         the sync conf
     * @param configProject
     *         the config project
     *
     * @return the list
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private List< Object > saveAndGetLocalProcessedData( List< FileInfo > serverFiles, String containerId, File localDir, SyncJson syncConf,
            ProjectConfiguration configProject ) throws IOException {
        ListFilterUtil rows = new ListFilterUtil();
        Map< String, FileInfo > allFilesOnLocal = new HashMap<>();
        if ( localDir.exists() ) {
            for ( File f : localDir.listFiles() ) {
                saveLocalObjects( containerId, allFilesOnLocal, syncConf, configProject, f, "", serverFiles );
            }
        }
        // compare and prepare table response
        Map< String, FileInfo > serverProcessedFiles = new HashMap<>();
        // method to compare and prepare table response
        compareAndPrepareTableResponse( rows, allFilesOnLocal, serverFiles, serverProcessedFiles, true );
        for ( Map.Entry< String, FileInfo > localfilename : allFilesOnLocal.entrySet() ) {
            prepareRowForSyncList( true, rows, serverProcessedFiles, syncConf, localfilename.getValue(), configProject );
        }
        return rows;
    }

    /**
     * Save synch objects.
     *
     * @param containerId
     *         the container id
     * @param localFiles
     *         the local files
     * @param f
     *         the f
     * @param dir
     *         the dir
     * @param syncConf
     *         the sync conf
     * @param configProject
     *         the config project
     * @param serverFiles
     *         the server files
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void saveSynchObjects( String containerId, Map< String, FileInfo > localFiles, File f, boolean dir, SyncJson syncConf,
            ProjectConfiguration configProject, List< FileInfo > serverFiles ) throws IOException {

        FileInfo htmlFileInfo = getFileInfoByName( f.getName(), serverFiles );
        String checksum = "";
        // synch called and data obj exist in SQLite start processing
        ComputedDataObjectEntity savedObj = findByPath( f.getPath() );
        if ( savedObj != null ) {
            boolean saveObject = false;
            Date modified = new Date( f.lastModified() );

            if ( htmlFileInfo != null && htmlFileInfo.getType().toString().equalsIgnoreCase( FILE_TYPE_HTML ) ) {
                File jsonFile = new File( f.getAbsolutePath() + FILE_SEPERATOR + f.getName() + ".json" );
                File zipFile = new File( f.getAbsolutePath() + FILE_SEPERATOR + f.getName() + ".zip" );

                if ( jsonFile.exists() ) {
                    String localHtmlJsonChecksum = FileUtils.getAdler32CheckSumForLocalFile( jsonFile );
                    if ( StringUtils.isNotNullOrEmpty( localHtmlJsonChecksum ) ) {
                        savedObj.setChecksum( localHtmlJsonChecksum );
                        savedObj.setModifiedOn( modified );
                        savedObj.setSize( f.length() );
                    }
                }
                if ( zipFile.exists() ) {
                    String localHtmlZipChecksum = FileUtils.getAdler32CheckSumForLocalFile( zipFile );
                    // do something with localFiles and serverFiles
                }
            } else if ( !savedObj.isDir() && savedObj.getModifiedOn() != null && savedObj.isExistOnServer()
                    && savedObj.getModifiedOn().compareTo( modified ) == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                // if file does not modified then add existing hash into local
                // file list

                localFiles.put( f.getName().toLowerCase(),
                        new FileInfo( savedObj.getId(), f.getName(), savedObj.getChecksum(), dir, savedObj.getType(),
                                new Date( Files.readAttributes( f.toPath(), BasicFileAttributes.class ).creationTime().toMillis() ) ) );

            } else if ( !savedObj.isDir() && savedObj.getModifiedOn() != null && savedObj.isExistOnServer()
                    && savedObj.getModifiedOn().compareTo( modified ) < ConstantsInteger.INTEGER_VALUE_ZERO ) {

                // if file modified then calculate hash and add new hash into
                // local SQLite list
                logger.debug( "Modified file hash calculating : " + f.getName() );
                checksum = FileUtils.getAdler32CheckSumForLocalFile( f );
                savedObj.setChecksum( checksum );
                savedObj.setModifiedOn( modified );
                savedObj.setSize( f.length() );
                saveObject = true;
            }
            // only save file if modified
            if ( saveObject ) {
                savedObj.setContainer( containerId );
                logger.debug( "saving Modified obj : " + f.getName() );
                // again save object because of modification hash
                sqLiteManager.save( savedObj );
            }

            localFiles.put( f.getName().toLowerCase(),
                    new FileInfo( savedObj.getId(), f.getName(), savedObj.getChecksum(), dir, savedObj.getType(),
                            new Date( Files.readAttributes( f.toPath(), BasicFileAttributes.class ).creationTime().toMillis() ),
                            savedObj.getSize().toString() ) );
        } else {
            // get from server a list of objects in container, with hash values,
            // and compare in localFiles
            logger.debug( "server files : " + serverFiles.toString() );
            // saveLocalObjects( containerId, localFiles, syncConf, configProject, f, checksum, dir ,serverFiles);
        }
    }

    /**
     * Gets file info by name.
     *
     * @param fileName
     *         the file name
     * @param serverFiles
     *         the server files
     *
     * @return the file info by name
     */
    private FileInfo getFileInfoByName( String fileName, List< FileInfo > serverFiles ) {
        for ( FileInfo serverFile : serverFiles ) {
            String serverFileName = serverFile.getName();
            if ( serverFileName.equals( fileName ) ) {
                return serverFile;
            }
        }
        return null;
    }

    /**
     * Save local objects.
     *
     * @param containerId
     *         the container id
     * @param localFiles
     *         the local files
     * @param syncConf
     *         the sync conf
     * @param configProject
     *         the config project
     * @param f
     *         the f
     * @param checksum
     *         the checksum
     * @param serverFiles
     *         the server files
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void saveLocalObjects( String containerId, Map< String, FileInfo > localFiles, SyncJson syncConf,
            ProjectConfiguration configProject, File f, String checksum, List< FileInfo > serverFiles ) throws IOException {
        ComputedDataObjectEntity isExistInSQLite = findByPath( f.getPath() );
        if ( isExistInSQLite == null ) {

            String uuid = UUID.randomUUID().toString();
            String typeOfLocalObject = getTypeOfFile( f, syncConf, configProject );
            ComputedDataObjectEntity saveobj = new ComputedDataObjectEntity( uuid, f.getName(), containerId, null, null, f.getPath(),
                    typeOfLocalObject, f.isDirectory(), f.length(), false );

            // saving local data object into sql without hash and modified date
            sqLiteManager.save( saveobj );
            localFiles.put( f.getName().toLowerCase(),
                    new FileInfo( uuid, f.getName(), checksum, f.isDirectory(), typeOfLocalObject,
                            new Date( Files.readAttributes( f.toPath(), BasicFileAttributes.class ).creationTime().toMillis() ),
                            f.length() + "" ) );

        } else {
            if ( !isExistInSQLite.isTypeUpdated() ) {
                String typeOfLocalObject = getTypeOfFile( f, syncConf, configProject );
                // if ( ( typeOfLocalObject.equalsIgnoreCase( "Html" ) ) && ( isExistInSQLite.isExistOnServer() == false ) ) {}
                isExistInSQLite.setType( typeOfLocalObject );
                sqLiteManager.save( isExistInSQLite );
            }
            if ( !isExistInSQLite.isDir() && isExistInSQLite.getModifiedOn() == null && !isExistInSQLite.isExistOnServer() ) {
                for ( FileInfo fileInfo : serverFiles ) {
                    if ( fileInfo.getName().equals( f.getName() ) && !f.isDirectory() ) {
                        isExistInSQLite.setExistOnServer( true );
                        isExistInSQLite.setModifiedOn( new Date( f.lastModified() ) );
                        isExistInSQLite.setChecksum( FileUtils.getAdler32CheckSumForLocalFile( f ) );
                        break;
                    }
                }
            }

            // if local SQLite object exists
            localFiles.put( f.getName().toLowerCase(),
                    new FileInfo( isExistInSQLite.getId(), f.getName(), isExistInSQLite.getChecksum(), f.isDirectory(),
                            isExistInSQLite.getType(),
                            new Date( Files.readAttributes( f.toPath(), BasicFileAttributes.class ).creationTime().toMillis() ),
                            f.length() + "" ) );
        }

    }

    /**
     * Gets the type of file.
     *
     * @param f
     *         the f
     * @param syncConf
     *         the sync conf
     * @param configProject
     *         the config project
     *
     * @return the type of file
     */
    private String getTypeOfFile( File f, SyncJson syncConf, ProjectConfiguration configProject ) {
        if ( null == syncConf ) {
            return ConstantsString.EMPTY_STRING;
        }
        if ( f.isDirectory() ) {
            if ( new File( f.getAbsolutePath() + FILE_SEPERATOR + f.getName() + ".json" ).exists() ) {
                Boolean isHtml = false;
                try {
                    String expected_value = "\"html\":";
                    String file = f.getAbsolutePath() + FILE_SEPERATOR + f.getName() + ".json";
                    BufferedReader br = new BufferedReader( new FileReader( file ) );
                    String line;
                    while ( ( line = br.readLine() ) != null ) {
                        if ( line.contains( expected_value ) ) {
                            isHtml = true;
                            break;
                        }
                    }
                } catch ( Exception e ) {
                }

                if ( isHtml == true ) {
                    return FILE_TYPE_HTML;
                } else {
                    return getFolderType( f, syncConf, configProject );
                }
            } else {
                return getFolderType( f, syncConf, configProject );
            }
        } else {
            return getFileType( f, syncConf, configProject );
        }
    }

    /**
     * Prints the star border in log file.
     *
     * @param valuetoPrint
     *         the valueto print
     */
    private void printStarBorderInLogFile( String valuetoPrint ) {
        logger.debug( "***************************************" + valuetoPrint + "***************************************" );
    }

    /**
     * Gets the file type.
     *
     * @param f
     *         the f
     * @param syncConf
     *         the sync conf
     * @param configProject
     *         the config project
     *
     * @return the file type
     */
    private String getFileType( File f, SyncJson syncConf, ProjectConfiguration configProject ) {
        String type = ConstantsString.EMPTY_STRING;
        for ( Map.Entry< String, List< String > > entry : syncConf.getConfiguration().getFiles().entrySet() ) {
            List< String > extList = entry.getValue();
            for ( String extensionWild : extList ) {
                final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );
                if ( f.getName().matches( extensionRegex ) ) {
                    if ( isValidFromServer( configProject, entry.getKey() ) ) {
                        type = entry.getKey();
                    } else {
                        type = ConstantsString.EMPTY_STRING;
                    }
                    break;
                }
            }
        }
        if ( type.isEmpty() ) {
            for ( Map.Entry< String, List< String > > entry : syncConf.getConfiguration().getFiles().entrySet() ) {
                List< String > extList = entry.getValue();
                for ( String extensionWild : extList ) {
                    final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );
                    if ( f.getName().matches( extensionRegex ) ) {
                        if ( isValidFromServer( configProject, entry.getKey() ) ) {
                            type = entry.getKey();
                        }
                    } else if ( extList.contains( DEFAULT_FILE ) ) {
                        if ( isValidFromServer( configProject, entry.getKey() ) ) {
                            type = entry.getKey();
                        } else {
                            type = ConstantsString.EMPTY_STRING;
                        }

                    }
                }

            }
        }
        return type;
    }

    /**
     * Gets the folder type.
     *
     * @param f
     *         the f
     * @param syncConf
     *         the sync conf
     * @param configProject
     *         the config project
     *
     * @return the folder type
     */
    private String getFolderType( File f, SyncJson syncConf, ProjectConfiguration configProject ) {
        String type = ConstantsString.EMPTY_STRING;
        for ( Map.Entry< String, List< String > > entry : syncConf.getConfiguration().getFolders().entrySet() ) {
            List< String > extList = entry.getValue();
            for ( String extensionWild : extList ) {
                final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );
                if ( f.getName().matches( extensionRegex ) ) {
                    if ( isValidFromServer( configProject, entry.getKey() ) ) {
                        type = entry.getKey();
                    } else {
                        type = ConstantsString.EMPTY_STRING;
                    }
                    break;
                }
            }
        }
        if ( type.isEmpty() ) {
            for ( Map.Entry< String, List< String > > entry : syncConf.getConfiguration().getFolders().entrySet() ) {
                List< String > extList = entry.getValue();
                if ( extList.contains( DEFAULT_DIR ) ) {
                    if ( isValidFromServer( configProject, entry.getKey() ) ) {
                        type = entry.getKey();
                    } else {
                        type = ConstantsString.EMPTY_STRING;
                    }

                }
            }
        }
        return type;
    }

    /**
     * findByPath.
     *
     * @param path
     *         the path
     *
     * @return the computed data object entity
     */
    private ComputedDataObjectEntity findByPath( String path ) {
        ComputedDataObjectEntity computed;
        computed = sqLiteManager.findByPath( path );

        if ( computed != null && computed.getPath().equals( path ) ) {
            return computed;

        }
        return null;
    }

    /**
     * Gets the relative local synch path from bread crumb.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     *
     * @return the relative local synch path from bread crumb
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private String getRelativeLocalSynchPathFromBreadCrumb( String containerId, String authToken ) throws IOException {
        return computeLocalSyncPath( SuSClient.getRequest( daemonManager.getServerAPIBase() + BREADCRUMB_VIEW_DATA_PROJECT + containerId,
                CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );
    }

    /**
     * Prepare row for sync list.
     *
     * @param isLocal
     *         the is local
     * @param rows
     *         the rows
     * @param serverProcessedFiles
     *         the server processed files
     * @param syncConf
     *         the sync conf
     * @param localfile
     *         the localfile
     * @param configProject
     *         the config project
     */
    private void prepareRowForSyncList( boolean isLocal, List< Object > rows, Map< String, FileInfo > serverProcessedFiles,
            SyncJson syncConf, FileInfo localfile, ProjectConfiguration configProject ) {
        SyncFileDTO row = new SyncFileDTO();
        if ( serverProcessedFiles.containsKey( localfile.getName().toLowerCase() ) ) {
            return;
        } else if ( isLocal ) {
            row.setId( localfile.getId() );
            row.setName( localfile.getName() );
            row.setSyncStatus( ONLY_LOCAL );
            row.setSize( localfile.getSize() );
            if ( localfile.isDir() && localfile.getType() == FILE_TYPE_HTML ) {
                row.setType( FILE_TYPE_HTML );
            } else {
                row.setType( localfile.getType() );
            }
            if ( !checkIfIgnored( syncConf, localfile, row ) ) {
                if ( localfile.isDir() ) {
                    checkFolderExtension( syncConf, localfile, row, configProject );
                } else {
                    checkFileExtension( syncConf, localfile, row, configProject );
                }
            }
            row.setCreatedOn( localfile.getCreatedOn() );
            rows.add( row );
        }
    }

    /**
     * Check folder extension.
     *
     * @param syncConf
     *         the sync conf
     * @param localfile
     *         the localfile
     * @param row
     *         the row
     * @param configProject
     *         the config project
     */
    private void checkFolderExtension( SyncJson syncConf, FileInfo localfile, SyncFileDTO row, ProjectConfiguration configProject ) {
        if ( null == syncConf ) {
            row.setSyncStatus( ONLY_LOCAL_UNKNOWN );
            return;
        }
        for ( Map.Entry< String, List< String > > entry : syncConf.getConfiguration().getFolders().entrySet() ) {
            List< String > extList = entry.getValue();
            for ( String extensionWild : extList ) {
                final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );
                if ( localfile.getName().matches( extensionRegex ) ) {

                    if ( isValidFromServer( configProject, entry.getKey() ) ) {
                        row.setType( localfile.getType() );
                        row.setIcon( getIconFromConfig( configProject, localfile.getType() ) );
                    } else {
                        row.setType( LOCAL_UNKNOWN );
                        // throw new SusException( PROJECT_CONFIGRATION_IS_INVAILD + entry.getKey() + NOT_FOUND_IN_PROJECT_CONFIG );
                    }
                    break;
                }
            }
        }
        if ( null == row.getType() ) {
            row.setSyncStatus( ONLY_LOCAL_UNKNOWN );
            for ( Map.Entry< String, List< String > > entry : syncConf.getConfiguration().getFolders().entrySet() ) {
                List< String > extList = entry.getValue();
                if ( extList.contains( DEFAULT_DIR ) ) {
                    if ( isValidFromServer( configProject, entry.getKey() ) ) {
                        row.setType( localfile.getType() );
                        row.setIcon( getIconFromConfig( configProject, localfile.getType() ) );
                    } else {
                        row.setType( LOCAL_UNKNOWN );
                        // throw new SusException( PROJECT_CONFIGRATION_IS_INVAILD + entry.getKey() + NOT_FOUND_IN_PROJECT_CONFIG );
                    }

                }
            }
        }
    }

    /**
     * Check file extension.
     *
     * @param syncConf
     *         the sync conf
     * @param localfile
     *         the localfile
     * @param row
     *         the row
     * @param configProject
     *         the config project
     */
    private void checkFileExtension( SyncJson syncConf, FileInfo localfile, SyncFileDTO row, ProjectConfiguration configProject ) {
        if ( null == syncConf ) {
            row.setSyncStatus( ONLY_LOCAL_UNKNOWN );
            return;
        }
        for ( Map.Entry< String, List< String > > entry : syncConf.getConfiguration().getFiles().entrySet() ) {
            List< String > extList = entry.getValue();
            for ( String extensionWild : extList ) {
                final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );
                if ( localfile.getName().matches( extensionRegex ) ) {
                    if ( isValidFromServer( configProject, entry.getKey() ) ) {
                        row.setType( localfile.getType() );
                        row.setIcon( getIconFromConfig( configProject, localfile.getType() ) );
                    } else {
                        row.setType( LOCAL_UNKNOWN );
                        // throw new SusException( PROJECT_CONFIGRATION_IS_INVAILD + entry.getKey() + NOT_FOUND_IN_PROJECT_CONFIG );
                    }
                    break;
                } else {
                    row.setType( localfile.getType() );
                    row.setIcon( getIconFromConfig( configProject, localfile.getType() ) );

                }
            }
        }

        if ( null == row.getType() ) {
            row.setSyncStatus( ONLY_LOCAL_UNKNOWN );
            for ( Map.Entry< String, List< String > > entry : syncConf.getConfiguration().getFiles().entrySet() ) {
                List< String > extList = entry.getValue();

                for ( String extensionWild : extList ) {
                    final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );
                    if ( localfile.getName().matches( extensionRegex ) ) {
                        if ( isValidFromServer( configProject, entry.getKey() ) ) {
                            row.setType( entry.getKey() );
                            row.setIcon( getIconFromConfig( configProject, localfile.getType() ) );
                        } else {
                            row.setType( LOCAL_UNKNOWN );
                            // throw new SusException( PROJECT_CONFIGRATION_IS_INVAILD + entry.getKey() + NOT_FOUND_IN_PROJECT_CONFIG );
                        }
                        break;
                    } else {
                        if ( extList.contains( DEFAULT_FILE ) ) {
                            if ( isValidFromServer( configProject, entry.getKey() ) ) {
                                row.setType( localfile.getType() );
                                row.setIcon( getIconFromConfig( configProject, localfile.getType() ) );
                            } else {
                                row.setType( LOCAL_UNKNOWN );
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the icon from config.
     *
     * @param configProject
     *         the config project
     * @param key
     *         the key
     *
     * @return the icon from config
     */
    private String getIconFromConfig( ProjectConfiguration configProject, String key ) {
        String icon = ConstantsString.EMPTY_STRING;
        for ( SuSObjectModel entityConfig : configProject.getEntityConfig() ) {
            if ( entityConfig.getName().equalsIgnoreCase( key ) ) {
                icon = entityConfig.getIcon();
                break;
            }
        }
        return icon;
    }

    /**
     * Check if ignored.
     *
     * @param syncConf
     *         the sync conf
     * @param localfile
     *         the localfile
     * @param row
     *         the row
     *
     * @return true, if successful
     */
    private boolean checkIfIgnored( SyncJson syncConf, FileInfo localfile, SyncFileDTO row ) {
        if ( null == syncConf ) {
            return false;
        }
        boolean isIgnored = false;

        if ( localfile.isDir() ) {
            for ( String extensionWild : syncConf.getIgnore() ) {
                final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );
                if ( ( localfile.getName() + File.separator ).matches( extensionRegex ) ) {
                    row.setSyncStatus( IGNORED );
                    row.setType( ConstantsString.EMPTY_STRING );
                    isIgnored = true;
                    break;
                }
            }
        } else {
            for ( String extensionWild : syncConf.getIgnore() ) {
                final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );
                if ( localfile.getName().matches( extensionRegex ) ) {
                    row.setSyncStatus( IGNORED );
                    row.setType( ConstantsString.EMPTY_STRING );
                    isIgnored = true;
                    break;
                }
            }
        }
        return isIgnored;
    }

    /**
     * Compare and prepare table response.
     *
     * @param rows
     *         the rows
     * @param localFiles
     *         the local files
     * @param serverFiles
     *         the server files
     * @param serverProcessedFiles
     *         the server processed files
     * @param isLocal
     *         the is local
     */
    private void compareAndPrepareTableResponse( List< Object > rows, Map< String, FileInfo > localFiles, List< FileInfo > serverFiles,
            Map< String, FileInfo > serverProcessedFiles, boolean isLocal ) {
        for ( FileInfo serverfile : serverFiles ) {
            if ( !isLocal ) {
                SyncFileDTO row = new SyncFileDTO();
                row.setType( serverfile.getType() );
                row.setTypeId( serverfile.getTypeId() );
                row.setSize( serverfile.getSize() );
                row.setLink( serverfile.getLink() );
                row.setCreatedBy( serverfile.getCreatedBy() );
                row.setModifiedBy( serverfile.getModifiedBy() );
                row.setModifiedOn( serverfile.getModifiedOn() );
                row.setUrlType( serverfile.getUrlType() );
                row.setParentId( serverfile.getParentId() );
                row.setDescription( serverfile.getDescription() );
                row.setVersion( serverfile.getVersion() );
                row.setAutoDeleted( serverfile.isAutoDeleted() );
                if ( localFiles.containsKey( serverfile.getName().toLowerCase() ) ) {

                    row.setCheckedOut( String.valueOf( serverfile.getCheckedOut() ) );
                    row.setId( serverfile.getId() );
                    if ( serverfile.getFile() != null ) {
                        row.setFileId( serverfile.getFile().getId() );
                    }
                    row.setLifeCycleStatus( serverfile.getLifeCycleStatus() );
                    row.setCheckedOutUser( serverfile.getCheckedOutUser() );

                    setRelativeFileStatuses( localFiles, serverfile, row );
                } else {
                    row.setName( serverfile.getName() );
                    row.setSyncStatus( ONLY_SERVER );
                    row.setType( serverfile.getType() );
                    row.setIcon( serverfile.getIcon() );
                    row.setCreatedOn( serverfile.getCreatedOn() );
                    row.setId( serverfile.getId() );
                    if ( serverfile.getFile() != null ) {
                        row.setFileId( serverfile.getFile().getId() );
                    }
                    row.setCheckedOut( String.valueOf( serverfile.getCheckedOut() ) );
                    row.setLifeCycleStatus( serverfile.getLifeCycleStatus() );
                    row.setCheckedOutUser( serverfile.getCheckedOutUser() );
                }
                rows.add( row );
            }
            serverProcessedFiles.put( serverfile.getName().toLowerCase(), serverfile );
        }
    }

    /**
     * Validate path.
     *
     * @param syncFilePath
     *         the sync file path
     *
     * @return the local sync config
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private SyncJson validateSynchJsonPath( File syncFilePath ) throws IOException {
        SyncJson syncConf = null;
        if ( syncFilePath.exists() && syncFilePath.isFile() ) {

            try ( final InputStream targetConfigStream = new FileInputStream( syncFilePath ) ) {
                syncConf = JsonUtils.jsonToObject( targetConfigStream, SyncJson.class );
            } catch ( JsonSerializationException e ) {
                ExceptionLogger.logException( e, getClass() );
                throw new SusException( YOU_HAVE_PROVIDED_INVALID_JSON );
            }
        } else {
            try {
                syncConf = daemonManager.localSyncConfig();
            } catch ( JsonSerializationException e ) {
                throw new SusException( YOU_HAVE_PROVIDED_INVALID_JSON );
            }
        }
        return syncConf;
    }

    /**
     * Find sync filein sync directory.
     *
     * @param localDir
     *         the local dir
     *
     * @return the file
     */
    private File findSyncFileinSyncDirectory( File localDir ) {
        if ( localDir.getName().equals( ConstantsString.PROJECT_TYPE ) ) {
            return localDir;
        }

        File syncFileJson = new File( localDir.getAbsolutePath() + File.separator + ConstantsString.SYNC_CONF_FILE_NAME );
        if ( !syncFileJson.exists() ) {
            syncFileJson = findSyncFileinSyncDirectory( new File( new File( localDir.getParent() ).getParent() + "/Data" ) );
        }
        return syncFileJson;

    }

    /**
     * Sets the relative file statuses.
     *
     * @param localFiles
     *         the local files
     * @param serverfile
     *         the serverfile
     * @param row
     *         the row
     */
    private void setRelativeFileStatuses( Map< String, FileInfo > localFiles, FileInfo serverfile, SyncFileDTO row ) {
        if ( serverfile.isDir() ) {
            row.setName( serverfile.getName() );
            row.setSyncStatus( SYNCED );
            row.setType( serverfile.getType() );
            row.setIcon( serverfile.getIcon() );
            row.setCreatedOn( serverfile.getCreatedOn() );
        } else {
            // compare hash
            row.setName( serverfile.getName() );
            row.setType( serverfile.getType() );
            row.setIcon( serverfile.getIcon() );
            row.setCreatedOn( serverfile.getCreatedOn() );

            if ( serverfile.getFile() == null || serverfile.getHash() == null ) {
                row.setSyncStatus( CHANGED_ON_LOCAL );
            } else if ( serverfile.getHash().equals( localFiles.get( serverfile.getName().toLowerCase() ).getHash() ) ) {
                row.setSyncStatus( SYNCED );
            } else {
                row.setSyncStatus( CHANGED_ON_LOCAL );
            }
            if ( serverfile.getHash() != null
                    && !serverfile.getHash().equals( localFiles.get( serverfile.getName().toLowerCase() ).getHash() )
                    && isVersionExists( localFiles.get( serverfile.getName().toLowerCase() ).getHash(), serverfile.getHashList() ) ) {
                row.setSyncStatus( CHANGED_ON_SERVER );
            }
        }
    }

    /**
     * Checks if is version exists.
     *
     * @param hash
     *         the hash
     * @param hashList
     *         the hash list
     *
     * @return true, if is version exists
     */
    private boolean isVersionExists( String hash, List< String > hashList ) {
        if ( CollectionUtil.isNotEmpty( hashList ) && hash != null ) {
            for ( String previousVerHash : hashList ) {
                if ( hash.equals( previousVerHash ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the server files.
     *
     * @param request
     *         the request
     *
     * @return the server files
     */
    private List< FileInfo > getServerFiles( SusResponseDTO request ) {
        String json = JsonUtils.toJson( request.getData() );
        logger.debug( "*** get server files json" + json );
        return JsonUtils.jsonToList( json, FileInfo.class );
    }

    /**
     * Gets the files.
     *
     * @param request
     *         the request
     *
     * @return the files
     */
    private FilteredResponse< Object > getFiles( SusResponseDTO request ) {
        String json = JsonUtils.toJson( request.getData() );
        logger.debug( "***" + json );
        return JsonUtils.jsonToObject( json, FilteredResponse.class );
    }

    /**
     * Gets the files UI.
     *
     * @param request
     *         the request
     *
     * @return the files UI
     */
    private TableUI getFilesUI( SusResponseDTO request ) {
        String json = JsonUtils.toJson( request.getData() );
        logger.debug( "***" + json );
        return JsonUtils.jsonToObject( json, TableUI.class );
    }

    /**
     * Compute local sync path.
     *
     * @param request
     *         the request
     *
     * @return the string
     */
    private String computeLocalSyncPath( SusResponseDTO request ) {
        StringBuilder path = new StringBuilder();
        String json = JsonUtils.toJson( request.getData() );
        List< BreadCrumbItemDTO > bcList = JsonUtils.jsonToList( json, BreadCrumbItemDTO.class );
        for ( int i = 0; i < bcList.size(); i++ ) {
            path.append( FILE_SEPERATOR );
            if ( i == 0 ) {
                path.append( "Data" );
            } else {
                path.append( bcList.get( i ).getName() );
            }
        }
        return path.toString();
    }

    /**
     * Gets the extension.
     *
     * @return the extension
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private FileExtension getExtension() throws IOException {
        FileExtension extension = null;
        try {
            extension = daemonManager.localExtensionFileConfig();
        } catch ( JsonSerializationException e ) {
            throw new SusException( YOU_HAVE_PROVIDED_INVALID_JSON );
        }
        return extension;
    }

    /**
     * For synch tab open file.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param localDir
     *         the local dir
     * @param serverFiles
     *         the server files
     * @param selectedFiles
     *         the selected files
     * @param appsConfig
     *         the apps config
     * @param application
     *         the application
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *         the interrupted exception
     */
    private String forSynchTabOpenFile( String containerId, String authToken, File localDir, List< FileInfo > serverFiles,
            List< String > selectedFiles, AppsConfig appsConfig, String application ) throws IOException, InterruptedException {
        // for synch tab open file api call
        if ( selectedFiles != null && !selectedFiles.isEmpty() ) {
            for ( String file : selectedFiles ) {
                for ( FileInfo fileInfo : serverFiles ) {
                    if ( fileInfo.getId().equals( file ) || fileInfo.getName().equals( file ) ) {
                        File localFile = getLocalFileByName( localDir, fileInfo );
                        if ( fileInfo.getFile() == null || fileInfo.getFile().getId() == null ) {
                            return fileInfo.getName() + OBJECT_DOES_NOT_CONTAIN_ANY_FILE;
                        } else if ( localFile != null && localFile.exists() ) {
                            // if file exists then open file
                            openFileWithCMD( appsConfig, localFile, application );
                        } else {
                            downloadAndOpenFile( containerId, authToken, localDir, serverFiles, appsConfig, file, fileInfo, application );
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Download and open file.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param localDir
     *         the local dir
     * @param serverFiles
     *         the server files
     * @param appsConfig
     *         the apps config
     * @param file
     *         the file
     * @param fileInfo
     *         the file info
     * @param application
     *         the application
     *
     * @throws InterruptedException
     *         the interrupted exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void downloadAndOpenFile( String containerId, String authToken, File localDir, List< FileInfo > serverFiles,
            AppsConfig appsConfig, String file, FileInfo fileInfo, String application ) throws InterruptedException, IOException {
        logger.debug( "file needs to be downloaded" + fileInfo.getName() );
        Map< String, String > selectedIdsMap = new HashMap<>();
        selectedIdsMap.put( file.toLowerCase(), file );
        // evaluate if user selected to download an object,
        // by name of the object.
        evaluateAndDownloadObjectFiles( containerId, authToken, localDir, serverFiles, selectedIdsMap, computedFiles );
        computedFiles.stream().forEach( filesToDownload -> {
            if ( filesToDownload.isCheckOutFileToDownload() ) {
                transferManager.getTransfers().put( filesToDownload.getFile().getName(),
                        new TransferInfo( 0L, DOWNLOAD, localDir.getPath() + File.separator + filesToDownload.getFile().getName() ) );
                // create structure
                logger.debug( "creating structure" );
                createStructureInLocalSyncDir( filesToDownload.getContainerId(), authToken );
            }
        } );
        // execute downloading process in thread
        Runnable myRunnable = () -> {
            // download unavailible dataObject
            for ( ComputedDataObject filesToDownload : computedFiles ) {
                transferManager.downloadDataObjectFromLocation( containerId, filesToDownload.getFile(), authToken,
                        filesToDownload.getLocation() );
            }
        };
        Thread thread = new Thread( myRunnable );
        thread.start();
        // waiting for file to download then open file
        thread.join();
        logger.debug( "file Downloaded : " + localDir + File.separator + fileInfo.getName() );
        openFileWithCMD( appsConfig, new File( localDir + File.separator + fileInfo.getName() ), application );
    }

    /**
     * For local tab open file.
     *
     * @param localDir
     *         the local dir
     * @param selectedFiles
     *         the selected files
     * @param appsConfig
     *         the apps config
     * @param containerId
     *         the container id
     * @param application
     *         the application
     *
     * @return the list
     */
    private void forLocalTabOpenFile( File localDir, List< String > selectedFiles, AppsConfig appsConfig, String containerId,
            String application ) {
        // if local tab has the file
        List< ComputedDataObjectEntity > selectedObj = sqLiteManager.findBycontainerId( containerId );
        if ( localDir != null && localDir.listFiles() != null ) {
            selectedFiles.stream().forEach( file -> {
                for ( File f : localDir.listFiles() ) {
                    if ( f.isFile() ) {
                        selectedObj.stream().forEach( computedDataObjectEntity -> {
                            if ( computedDataObjectEntity.getId().equals( file )
                                    && f.getName().equals( computedDataObjectEntity.getName() ) ) {
                                logger.debug( "selection match : " + computedDataObjectEntity.getName() + file );
                                try {
                                    openFileWithCMD( appsConfig, f, application );
                                } catch ( IOException e ) {
                                    throw new UncheckedIOException( e );
                                }
                            }
                        } );
                    }
                }
            } );
        }
    }

    /**
     * Open file with CMD.
     *
     * @param appsConfig
     *         the apps config
     * @param localFile
     *         the local file
     * @param application
     *         the application
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void openFileWithCMD( AppsConfig appsConfig, File localFile, String application ) throws IOException {
        String commandToRun = getCommandByExtension( appsConfig, localFile.getPath(), application );

        String[] commandInArray = commandToRun.split( "\\s+" );
        Pattern p = Pattern.compile( ".*\\{input\\}.*" );
        for ( int x = 0; x < commandInArray.length; x++ ) {
            Matcher m = p.matcher( commandInArray[ x ] );
            if ( m.find() ) {
                commandInArray[ x ] = commandInArray[ x ].replace( "{input}", localFile.getPath() ).trim();

            }
        }
        Runtime.getRuntime().exec( commandInArray, null, localFile.getParentFile() );
    }

    /**
     * Gets the local file by name.
     *
     * @param localDir
     *         the local dir
     * @param fileInfo
     *         the file info
     *
     * @return the local file by name
     */
    private File getLocalFileByName( File localDir, FileInfo fileInfo ) {
        if ( localDir.exists() ) {
            for ( File f : localDir.listFiles() ) {
                if ( fileInfo.getName().equals( f.getName() ) ) {
                    return f;
                }
            }
        }
        return null;
    }

    /**
     * Gets the local file by name.
     *
     * @param localDir
     *         the local dir
     * @param fileName
     *         the file name
     *
     * @return the local file by name
     */
    private File getLocalFileByName( File localDir, String fileName ) {
        if ( localDir.exists() ) {
            for ( File f : localDir.listFiles() ) {
                if ( fileName.equals( f.getName() ) ) {
                    return f;
                }
            }
        }
        return null;
    }

    /**
     * Gets the command by extension.
     *
     * @param appsConfig
     *         the apps config
     * @param filePath
     *         the file path
     * @param application
     *         the application
     *
     * @return the command by extension
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private String getCommandByExtension( AppsConfig appsConfig, String filePath, String application ) throws IOException {
        // get extension by URL
        String fileExtension = FilenameUtils.getExtension( filePath );
        // if extension do not exist
        if ( fileExtension == null || fileExtension.isEmpty() ) {
            // get extension by File Prob content type
            Path path = Paths.get( filePath );
            String type = Files.probeContentType( path );
            fileExtension = MimeTypeConstants.getMimeKeyByValue( type );
        }
        if ( fileExtension == null || fileExtension.isEmpty() ) {
            throw new SusException( UNABLE_TO_GET_FILE_EXTENSION_FROM_PATH + filePath );
        }

        String command = "";
        if ( application.equals( "multiple" ) ) {
            command = appsConfig.getCommandForFileExtension( fileExtension );
        } else {
            command = appsConfig.getCommandForApplication( application, fileExtension );
        }

        if ( command == null || command.isEmpty() ) {
            throw new SusException( OPENED_FILE_EXTENSION_IS_UNDEFINED_IN_CONFIGURATION + fileExtension );
        }

        return command;
    }

    /**
     * Check read permission.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private boolean checkReadPermission( String containerId, String authToken ) throws IOException {
        // api for checking export permission for user
        SusResponseDTO susObject = SuSClient.getRequest( daemonManager.getServerAPIBase() + READ_PERMISSION_CHECK + containerId,
                CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        return Boolean.parseBoolean( JsonUtils.toJson( susObject.getData() ) );
    }

    /**
     * Save or update server objects in sql lite.
     *
     * @param f
     *         the f
     * @param containerId
     *         the container id
     * @param filesToDownload
     *         the files to download
     * @param authToken
     *         the auth token
     */
    private void saveOrUpdateServerObjectsInSqlLite( String f, String containerId, ComputedDataObject filesToDownload, String authToken ) {
        try {

            // get from server the container to compute path
            File localDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir()
                    + getRelativeLocalSynchPathFromBreadCrumb( containerId, authToken ) );
            File syncFilePath = findSyncFileinSyncDirectory( localDir );
            SyncJson syncConf = null;
            syncConf = validateSynchJsonPath( syncFilePath );
            ProjectConfiguration configProject = getSusObjectModelListFromAPI( containerId, authToken );
            File file = new File( localDir.getAbsolutePath() + FILE_SEPERATOR + f );
            String typeOfLocalObject = getTypeOfFile( file, syncConf, configProject );
            ComputedDataObjectEntity sqlLiteEntry = sqLiteManager.findByPath( filesToDownload.getFilePath() );
            if ( sqlLiteEntry != null ) {
                sqlLiteEntry.setExistOnServer( true );
                sqLiteManager.save( sqlLiteEntry );
            } else {
                sqLiteManager.save( new ComputedDataObjectEntity( UUID.randomUUID().toString(), filesToDownload.getFileName(), containerId,
                        null, null, filesToDownload.getFilePath(), typeOfLocalObject, false, Long.valueOf( filesToDownload.getSize() ),
                        true ) );
            }
        } catch ( Exception e ) {
            logger.debug( e );
        }
    }

    /**
     * Evaluate and download object files.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param projectLocalDir
     *         the project local dir
     * @param serverFiles
     *         the server files
     * @param selectedIdsMap
     *         the selected ids map
     * @param computedFiles
     *         the computed files
     */
    private void evaluateAndDownloadObjectFiles( String containerId, String authToken, File projectLocalDir, List< FileInfo > serverFiles,
            Map< String, String > selectedIdsMap, List< ComputedDataObject > computedFiles ) {
        for ( FileInfo file : serverFiles ) {
            if ( selectedIdsMap.containsKey( file.getId() ) ) {
                projectLocalDir.mkdirs();
                // get object by ID, evaulate type, and mkdir or getfile.
                if ( file.isDir() ) {
                    File containerDir = new File( projectLocalDir.getAbsolutePath() + FILE_SEPERATOR + file.getName() );
                    containerDir.mkdirs();
                } else {
                    // preparing list of objects to download
                    ComputedDataObject computedDataObject = new ComputedDataObject( containerId, file, authToken, true );
                    computedDataObject.setLocation( file.getLocation() );
                    computedFiles.add( computedDataObject );
                }
            }
        }
    }

    /**
     * Filter server objects.
     *
     * @param serverFiles
     *         the server files
     * @param selectedIdsMaps
     *         the selected ids maps
     */
    private void filterServerObjects( List< FileInfo > serverFiles, Map< String, LocalFilesToSync > selectedIdsMaps ) {
        Iterator< FileInfo > info = serverFiles.iterator();
        while ( info.hasNext() ) {
            FileInfo fileInfo = info.next();
            if ( !selectedIdsMaps.containsKey( fileInfo.getId() ) ) {
                info.remove();
            }
        }
    }

    /**
     * Sets the local file infor for ids map.
     *
     * @param localfilesInfo
     *         the localfiles info
     * @param f
     *         the f
     * @param localFile
     *         the local file
     */
    private void setLocalFileInforForIdsMap( LocalFilesToSync localfilesInfo, File f, ComputedDataObjectEntity localFile ) {
        localfilesInfo.setHash( localFile.getChecksum() );
        localfilesInfo.setName( f.getName() );
        localfilesInfo.setPath( f.getPath() );
        localfilesInfo.setIsDir( f.isDirectory() );
    }

    /**
     * Evaluate user for upload.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param addAndReturnUploadededFiles
     *         the add and return uploadeded files
     * @param serverFiles
     *         the server files
     * @param selectedIdsMap
     *         the selected ids map
     * @param localDir
     *         the local dir
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void evaluateUserForUpload( String containerId, String authToken, List< FileInfo > addAndReturnUploadededFiles,
            List< FileInfo > serverFiles, Map< String, LocalFilesToSync > selectedIdsMap, File localDir ) throws IOException {
        // get from server a list of objects in container, with hash values, and
        // compare in localFiles

        logger.debug( "server files : " + serverFiles.toString() );
        List< Object > results = compareLocalSync( serverFiles, null, containerId, authToken, true );
        List< SyncFileDTO > filledResponseList = ( List< SyncFileDTO > ) ( Object ) results;

        updateExistingDataObjectFiles( containerId, authToken, serverFiles, selectedIdsMap );

        updateFilesOnServerFromLocal( containerId, authToken, selectedIdsMap, filledResponseList );

        // all files are checked against every condition now upload prepared
        // adding entries into progress bar
        for ( ComputedDataObject preparedFile : computedFiles ) {
            String fullPath = localDir.getPath() + File.separator + preparedFile.getFileName();
            // entry in transfer bar
            transferManager.getTransfers().put( preparedFile.getFileName(), new TransferInfo( 0L, UPLOAD, fullPath ) );
            // entry in local sqlite db
            ComputedDataObjectEntity updateFlag = sqLiteManager.findByPath( fullPath );
            if ( updateFlag != null ) {
                sqLiteManager.save( updateFlag );
            }
        }

        // uploading actual objects
        Runnable task2 = () -> validatedFilesUpload( containerId, authToken, addAndReturnUploadededFiles );
        Thread thread = new Thread( task2 );
        thread.start();
        try {
            thread.join();
            // removing extra entries from list
            removeCompleteOrAbortedFilesFromList();
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Validated files upload.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param addAndReturnUploadededFiles
     *         the add and return uploadeded files
     */
    private void validatedFilesUpload( String containerId, String authToken, List< FileInfo > addAndReturnUploadededFiles ) {
        for ( ComputedDataObject preparedFile : computedFiles ) {
            if ( preparedFile.isDir() ) {
                if ( !preparedFile.isAbort() ) {
                    transferManager.uploadDirectory( containerId, preparedFile.getFileName(), authToken, preparedFile.getObjectId(),
                            preparedFile.getObjectType() );
                    preparedFile.setComplete( true );
                    addAndReturnUploadededFiles.add( new FileInfo( preparedFile.getFileName() ) );
                }
            } else if ( !preparedFile.isAbort() ) {
                logger.debug( "upload executor preparing" );
                // thread executor prepared and called
                UploadObjectThread uploadObjectThread = new UploadObjectThread( containerId, authToken, preparedFile, transferManager );
                getThreadPoolExecutorService().uploadExecute( uploadObjectThread );
                logger.debug( "upload executor started" );
                preparedFile.setComplete( true );
                addAndReturnUploadededFiles.add( new FileInfo( preparedFile.getFileName() ) );
            }
        }
    }

    /**
     * Removes the complete or aborted files from list.
     */
    private void removeCompleteOrAbortedFilesFromList() {
        logger.debug( "before removing completed and aborted files and size : " + computedFiles.size() + " >>>> " + computedFiles );

        Iterator< ComputedDataObject > it = computedFiles.iterator();
        while ( it.hasNext() ) {
            ComputedDataObject uploadedObjects = it.next();

            if ( uploadedObjects.isAbort() || uploadedObjects.isComplete() ) {
                it.remove();
            }
        }
        logger.debug( "after removing completed and aborted files and size : " + computedFiles.size() + " >>>> " + computedFiles );
    }

    /**
     * Update existing data object files.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param serverFiles
     *         the server files
     * @param selectedIdsMap
     *         the selected ids map
     */
    private void updateExistingDataObjectFiles( String containerId, String authToken, List< FileInfo > serverFiles,
            Map< String, LocalFilesToSync > selectedIdsMap ) {
        // evaluate if user selected to upload an object, by name of the object,
        // and not exists on server.
        for ( FileInfo file : serverFiles ) {
            if ( ( file != null || file.getCheckedOut() && file.getCheckedOutSame() ) || ( file != null || !file.getCheckedOut() ) ) {
                checkFiletypeAndHashthenUploadDataObjectFile( containerId, authToken, selectedIdsMap, file, computedFiles );
            }
        }
    }

    /**
     * Update files on server from local.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param selectedIdsMap
     *         the selected ids map
     * @param filledResponseList
     *         the filled response list
     */
    private void updateFilesOnServerFromLocal( String containerId, String authToken, Map< String, LocalFilesToSync > selectedIdsMap,
            List< SyncFileDTO > filledResponseList ) {
        ProjectConfiguration configProject = getSusObjectModelListFromAPI( containerId, authToken );
        if ( configProject == null || configProject.getEntityConfig() == null ) {
            logger.debug( ">>evaluateUserForUpload" );
            throw new SusException( "Config is Empty Or Invalid" );
        }

        for ( Entry< String, LocalFilesToSync > localFileNameLower : selectedIdsMap.entrySet() ) {
            for ( SyncFileDTO syncFileDTO : filledResponseList ) {
                if ( syncFileDTO.getId().equals( localFileNameLower.getKey() ) ) {
                    logger.debug( "updateFilesOnServerFromLocal > localFileNameLower > syncFileDTO" );

                    for ( SuSObjectModel suSObjectModel : configProject.getEntityConfig() ) {
                        if ( suSObjectModel.getName().equalsIgnoreCase( syncFileDTO.getType() )
                                && isDataTypeContained( configProject, syncFileDTO, suSObjectModel ) ) {

                            if ( localFileNameLower.getValue().getIsDir() ) {
                                // preparing list of objects to upload
                                computedFiles.add(
                                        new ComputedDataObject( containerId, syncFileDTO.getName(), authToken, null, suSObjectModel.getId(),
                                                localFileNameLower.getValue().getPath(), true, false, syncFileDTO.getSize() ) );
                            } else {
                                logger.debug( "updateFilesOnServerFromLocal creating object:& " + localFileNameLower );
                                // preparing list of objects to upload
                                computedFiles.add(
                                        new ComputedDataObject( containerId, syncFileDTO.getName(), authToken, null, suSObjectModel.getId(),
                                                localFileNameLower.getValue().getPath(), false, false, syncFileDTO.getSize() ) );
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Check filetype and hashthen upload data object file.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param selectedIdsMap
     *         the selected ids map
     * @param file
     *         the file
     * @param computedFiles
     *         the computed files
     */
    private void checkFiletypeAndHashthenUploadDataObjectFile( String containerId, String authToken,
            Map< String, LocalFilesToSync > selectedIdsMap, FileInfo file, List< ComputedDataObject > computedFiles ) {
        for ( Map.Entry< String, LocalFilesToSync > localFileNameLower : selectedIdsMap.entrySet() ) {
            LocalFilesToSync localFilesComputed = localFileNameLower.getValue();
            if ( ( selectedIdsMap.containsKey( file.getName().toLowerCase() ) || selectedIdsMap.containsKey( file.getId() ) )
                    && ( localFileNameLower.getKey().equalsIgnoreCase( file.getName() )
                    || localFileNameLower.getKey().equalsIgnoreCase( file.getId() ) ) ) {

                if ( file.getHash() != null ) {
                    if ( !file.getHash().equals( localFilesComputed.getHash() )
                            && !isVersionExists( localFilesComputed.getHash(), file.getHashList() ) ) {
                        logger.debug( "updateFilesOnServerFromLocal creating object: # " + localFileNameLower.getKey() );
                        computedFiles.add( new ComputedDataObject( containerId, file.getName(), authToken, file.getId(), file.getType(),
                                selectedIdsMap.get( localFileNameLower.getKey() ).getPath(), false, false, file.getSize(),
                                file.getLocation() ) );

                        selectedIdsMap.remove( file.getName().toLowerCase() );
                    }
                } else {
                    logger.debug( "updateFilesOnServerFromLocal creating object: * " + localFileNameLower.getKey() );
                    computedFiles.add( new ComputedDataObject( containerId, file.getName(), authToken, file.getId(), file.getType(),
                            selectedIdsMap.get( localFileNameLower.getKey() ).getPath(), false, false, file.getSize(),
                            file.getLocation() ) );

                    selectedIdsMap.remove( file.getName().toLowerCase() );
                }
            }
        }
    }

    /**
     * Gets the sus object model list from API.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     *
     * @return the sus object model list from API
     */
    private ProjectConfiguration getSusObjectModelListFromAPI( String containerId, String authToken ) {
        ProjectConfiguration configProject = null;
        SusResponseDTO configJson;
        try {
            configJson = SuSClient.getRequest( daemonManager.getServerAPIBase() + DATA_CONFIGURATION + containerId,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            configProject = JsonUtils.jsonToObject( JsonUtils.objectToJson( configJson.getData() ), ProjectConfiguration.class );
        } catch ( IOException e ) {
            logger.error( e.getMessage() );
        }

        return configProject;
    }

    /**
     * Gets the file checkout status byfile id.
     *
     * @param authToken
     *         the auth token
     * @param file
     *         the file
     *
     * @return the file checkout status byfile id
     */
    private DataObjectDTO getFileCheckoutStatusByfileId( String authToken, FileInfo file ) {
        // api for checkout value
        SusResponseDTO susObjectEntity = new SusResponseDTO();
        try {
            susObjectEntity = SuSClient.getRequest( daemonManager.getServerAPIBase() + DATA_PROJECT + file.getId() + CHECKOUT_GET,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        } catch ( IOException e ) {
            logger.error( e.getMessage() );
        }
        // getting checkout value
        return JsonUtils.jsonToObject( JsonUtils.toJson( susObjectEntity.getData() ), DataObjectDTO.class );
    }

    /**
     * Checks if is data type contained.
     *
     * @param config
     *         the config
     * @param syncFileDTO
     *         the sync file DTO
     * @param suSObjectModel
     *         the su S object model
     *
     * @return true, if is data type contained
     */
    private boolean isDataTypeContained( ProjectConfiguration config, SyncFileDTO syncFileDTO, SuSObjectModel suSObjectModel ) {
        List< String > containIdsOfObjects = suSObjectModel.getContains();
        if ( CollectionUtil.isEmpty( containIdsOfObjects ) ) {
            for ( String ids : containIdsOfObjects ) {
                for ( SuSObjectModel subModels : config.getEntityConfig() ) {
                    return ( ids.equals( subModels.getId() ) && syncFileDTO.getType().equals( subModels.getName() ) );
                }
            }
        }
        return true;
    }

    /**
     * Initialize object.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    private static Object initializeObject( String className ) {

        try {
            return Class.forName( className ).newInstance();
        } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ) );
        }
    }

    /**
     * Class instance from config.
     *
     * @param suSObjectModel
     *         the su S object model
     *
     * @return the object
     */
    private static Object classInstanceFromConfig( SuSObjectModel suSObjectModel ) {
        Class< ? > entityClass = ReflectionUtils
                .getFieldByName( initializeObject( suSObjectModel.getClassName() ).getClass(), ENTITY_CLASS_FIELD_NAME ).getType();
        return initializeObject( entityClass.getName() );
    }

    /**
     * Evaluate checkout status and begin download.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param file
     *         the file
     * @param computeCheckOutList
     *         the compute check out list
     * @param objList
     *         the obj list
     */
    private void evaluateCheckoutStatusAndBeginDownload( String containerId, String authToken, FileInfo file,
            List< ComputedDataObject > computeCheckOutList, List< SyncFileDTO > objList ) {

        // api for checkout value
        DataObjectDTO dataObject = getFileCheckoutStatusByfileId( authToken, file );

        if ( dataObject != null && !dataObject.isCheckedOut() ) {
            if ( isLifeCycleStatusCheckOutAllowed( dataObject.getLifeCycleStatus().getId(), authToken ) ) {
                ComputedDataObject computedDataObject = new ComputedDataObject( containerId, file, authToken, false );
                computedDataObject.setFileName( file.getName() );
                for ( SyncFileDTO syncFileDTO : objList ) {
                    if ( syncFileDTO.getSyncStatus().equals( ONLY_SERVER ) || null == objList || objList.isEmpty() ) {
                        computedDataObject.setCheckOutFileToDownload( true );
                    }
                }
                computedDataObject.setLocation( file.getLocation() );
                computeCheckOutList.add( computedDataObject );
            }
        } else {
            throw new SusException( file.getName() + IS_ALREADY_CHECKED_OUT );
        }

    }

    /**
     * Gets the all items from server.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     *
     * @return the all items from server
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private List< FileInfo > getAllItemsFromServer( String containerId, String authToken ) throws IOException {
        return getServerFiles( SuSClient.getRequest( daemonManager.getServerAPIBase() + DATA_PROJECT + containerId + DATA_LIST,
                CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );
    }

    /**
     * Gets the all items from server with filter.
     *
     * @param containerId
     *         the container id
     * @param typeId
     *         the type id
     * @param authToken
     *         the auth token
     * @param filterJson
     *         the filter json
     *
     * @return the all items from server with filter
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private List< FileInfo > getAllItemsFromServerWithFilter( String containerId, String typeId, String authToken, String filterJson )
            throws IOException {
        SusResponseDTO susResponseDTO = SuSClient.postRequestWithoutRetry(
                daemonManager.getServerAPIBase() + DATA_PROJECT + containerId + TYPE + typeId + DATA_LIST, filterJson,
                CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        List< FileInfo > fileInfos = getServerFiles( susResponseDTO );
        return fileInfos;
    }

    /**
     * Gets the all items.
     *
     * @param projectId
     *         the container id
     * @param typeId
     *         the type id
     * @param authToken
     *         the auth token
     * @param json
     *         the json
     *
     * @return the all items
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private FilteredResponse< Object > getAllItems( String projectId, String typeId, String authToken, String json ) throws IOException {
        FiltersDTO filter = JsonUtils.jsonToObject( json, FiltersDTO.class );
        if ( typeId.equals( SimuspaceFeaturesEnum.IMAGE_DATA_OBJECTS.getId() ) ) {
            FilteredResponse< Object > response = getFiles(
                    SuSClient.postRequest( daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + TYPE + typeId + LIST,
                            prepareJsonForServer( json ), CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );
            filter = getServerRecordsCount( projectId, typeId, authToken, filter );
            return PaginationUtil.constructFilteredResponse( filter, response.getData() );
        }
        Set< AllObjectsDTO > allObjectsDTO = new HashSet<>();
        if ( !projectId.equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
            // get from server a list of objects in container, with hash values,
            // and compare in localFiles
            List< FileInfo > allServerItems = getAllItemsFromServerWithFilter( projectId, typeId, authToken, json );
            allObjectsDTO = compareLocalSyncDataApi( allServerItems, projectId, authToken );
        } else {
            FilteredResponse< Object > response = getFiles(
                    SuSClient.postRequest( daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + TYPE + typeId + LIST,
                            prepareJsonForServer( json ), CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );
            filter.setTotalRecords( response.getRecordsTotal() );
            filter.setFilteredRecords( ( long ) response.getRecordsFiltered() );
            return PaginationUtil.constructFilteredResponse( filter, response.getData() );
        }
        List< AllObjectEntity > allObjectToSave = new ArrayList<>();
        // preparing data to save in bulk
        allObjectsDTO.stream().forEach( objectsDTO -> allObjectToSave.add( prepareAllObjectEntityFromDTO( objectsDTO ) ) );
        sqLiteManager.saveBulkData( allObjectToSave );
        int totalRecords = allObjectsDTO.size();
        List< AllObjectEntity > allObject = sqLiteManager.listAllByPage( filter, totalRecords );
        filter.setTotalRecords( ( long ) allObjectsDTO.size() );
        Thread sd = new Thread( new Runnable() {

            @Override
            public void run() {
                sqLiteManager.deleteAllObject();
            }
        } );
        sd.start();
        List< Object > allObjectsListToReturn = new ArrayList<>();
        allObject.stream().forEach( allObjectsEntity -> allObjectsListToReturn.add( prepareAllObjectDTOFromEntity( allObjectsEntity ) ) );
        return PaginationUtil.constructFilteredResponse( filter, allObjectsListToReturn );
    }

    /**
     * Gets the server records count.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param authToken
     *         the auth token
     * @param filter
     *         the filter
     *
     * @return the server records count
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private FiltersDTO getServerRecordsCount( String projectId, String typeId, String authToken, FiltersDTO filter ) throws IOException {
        SusResponseDTO request = SuSClient.getRequest(
                daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + TYPE + typeId + "/data/count",
                CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        long size = Long.parseLong( JsonUtils.toJson( request.getData() ) );
        filter.setTotalRecords( ( long ) size );
        filter.setFilteredRecords( ( long ) size );
        return filter;
    }

    /**
     * Prepare json for server.
     *
     * @param json
     *         the json
     *
     * @return the string
     */
    private String prepareJsonForServer( String json ) {
        boolean hasSorting = false;
        FiltersDTO filter = JsonUtils.jsonToObject( json, FiltersDTO.class );
        List< FilterColumn > columns = new ArrayList<>();

        if ( filter.getColumns() != null ) {
            for ( FilterColumn filterColumn : filter.getColumns() ) {
                if ( !filterColumn.getName().equals( SYNC_STATUS ) && !filterColumn.getName().equals( CHECKED_OUT )
                        && !filterColumn.getName().equals( CHECKED_OUT_USER ) ) {
                    columns.add( filterColumn );
                }
                if ( filterColumn.getDir() != null ) {
                    hasSorting = true;
                }
            }
        }

        // if no sorting then add default sorting on createdOn column
        if ( !hasSorting ) {
            for ( int i = 0; i < columns.size(); i++ ) {
                if ( columns.get( i ).getName().equals( CREATED_ON_FIELD ) ) {
                    columns.get( i ).setDir( SORTING_DIRECTION_DESCENDING );
                }
            }
        }

        filter.setColumns( columns );

        return JsonUtils.toJson( filter );
    }

    /**
     * Gets the all items UI.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param authToken
     *         the auth token
     *
     * @return the all items UI
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private TableUI getAllItemsUI( String projectId, String typeId, String authToken ) throws IOException {
        return getFilesUI( SuSClient.getRequest( daemonManager.getServerAPIBase() + DATA_PROJECT + projectId + TYPE + typeId + UI,
                CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );
    }

    /**
     * Evaluate and discard object.
     *
     * @param authToken
     *         the auth token
     * @param serverFiles
     *         the server files
     * @param selectedIdsMap
     *         the selected ids map
     * @param addAndReturnDiscardtFiles
     *         the add and return discardt files
     * @param containerId
     *         the container id
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void evaluateAndDiscardObject( String authToken, List< FileInfo > serverFiles, Map< String, String > selectedIdsMap,
            List< FileInfo > addAndReturnDiscardtFiles, String containerId ) throws IOException {
        for ( FileInfo file : serverFiles ) {
            if ( selectedIdsMap.containsKey( file.getId() ) && !file.isDir() ) {
                // api for checkout value
                DataObjectDTO dataObject = getFileCheckoutStatusByfileId( authToken, file );

                if ( dataObject.isCheckedOut() && isLifeCycleStatusCheckOutAllowed( dataObject.getLifeCycleStatus().getId(), authToken ) ) {
                    addAndReturnDiscardtFiles.add( file );
                    transferManager.downloadDataObjectFromLocation( containerId, file, authToken, file.getLocation() );
                    logger.debug( "Object discarded " + file.getName() );
                } else {
                    logger.debug( "Object is not checkedout so it can not be discarded" );
                }
            }
        }
    }

    /**
     * Gets the all tables.
     *
     * @param tableView
     *         the table view
     * @param fileViewColumn
     *         the file view column
     *
     * @return the all tables
     */
    private Object getAllTables( TableUI tableView, TableUI fileViewColumn ) {

        List< TableColumn > tableColumn2 = fileViewColumn.getColumns();

        for ( TableColumn additionalColumn : tableView.getColumns() ) {
            boolean columnExist = false;
            for ( TableColumn tableColum : tableColumn2 ) {
                if ( tableColum.getData().equals( additionalColumn.getData() ) ) {
                    columnExist = true;
                }
            }
            if ( !columnExist ) {
                tableColumn2.add( additionalColumn );
            }
        }

        tableColumn2.sort( ( TableColumn tc1, TableColumn tc2 ) -> tc1.getOrderNum() - tc2.getOrderNum() );

        tableView.setColumns( tableColumn2 );
        tableView.setViews( fileViewColumn.getViews() );

        return tableView;
    }

    /**
     * Gets the all selected items from server.
     *
     * @param authToken
     *         the auth token
     * @param selectionId
     *         the selection id
     *
     * @return the all selected items from server
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private String getSelectedFilesFromSelection( String authToken, String selectionId ) throws IOException {
        SusResponseDTO request = SuSClient.getRequest( daemonManager.getServerAPIBase() + "data/selection/" + selectionId,
                CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        return JsonUtils.toJson( request.getData() );
    }

    /**
     * Gets the all selected items from server.
     *
     * @param authToken
     *         the auth token
     * @param selectionId
     *         the selection id
     *
     * @return the all selected items from server
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private String getAllSelectedItemsFromServer( String authToken, String selectionId ) throws IOException {
        SusResponseDTO request = SuSClient.getRequest( daemonManager.getServerAPIBase() + DATA_SELECTION + selectionId,
                CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        return JsonUtils.toJson( request.getData() );
    }

    /**
     * Evaluate and checkin object and upload locally changed files.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param selectedIdsMap
     *         the selected ids map
     * @param model
     *         the model
     * @param serverFile
     *         the server file
     */
    private void evaluateAndCheckinObjectAndUploadLocallyChangedFiles( String containerId, String authToken,
            Map< String, LocalFilesToSync > selectedIdsMap, List< SuSObjectModel > model, List< FileInfo > serverFile ) {
        serverFile.stream().forEach( file -> {
            if ( selectedIdsMap.containsKey( file.getId() ) ) {
                DataObjectDTO dataObject = getFileCheckoutStatusByfileId( authToken, file );
                if ( dataObject.isCheckedOut() && dataObject.isCheckedOutSame()
                        && isLifeCycleStatusCheckOutAllowed( dataObject.getLifeCycleStatus().getId(), authToken ) && !file.isAbort() ) {
                    // get local file more details from map
                    LocalFilesToSync localFilesComputed = selectedIdsMap.get( file.getId() );
                    // check if file type exist in conf file
                    String idFromConfigFile = isTypeExistInConfig( model, file );
                    if ( idFromConfigFile != null ) {
                        ComputedDataObject computedDataObject = new ComputedDataObject( containerId, file.getName(), authToken,
                                file.getId(), file.getType(), selectedIdsMap.get( file.getId() ).getPath(), false, false, true,
                                file.getId(), file.getSize() );
                        computedDataObject.setLocation( file.getLocation() );
                        if ( null != file.getHash() && !file.getHash().equals( localFilesComputed.getHash() )
                                && !isVersionExists( localFilesComputed.getHash(), file.getHashList() ) ) {
                            // 1. upload the file and update object then
                            // change the checkout status
                            computedDataObject.setCheckInFileToUpload( true );
                        }
                        computedFiles.add( computedDataObject );
                    }
                }
                selectedIdsMap.remove( file.getName().toLowerCase() );
            }
        } );
    }

    /**
     * Checks if is type exist in configuration.
     *
     * @param model
     *         the model
     * @param file
     *         the file
     *
     * @return the string
     */
    private String isTypeExistInConfig( List< SuSObjectModel > model, FileInfo file ) {
        for ( SuSObjectModel suSObjectModel : model ) {
            if ( suSObjectModel.getName().equalsIgnoreCase( file.getType() ) ) {
                return suSObjectModel.getId();
            }
        }
        return null;
    }

    /**
     * Prepare download headers.
     *
     * @param authToken
     *         the auth token
     *
     * @return the map
     */
    private Map< String, String > prepareDownloadHeaders( String authToken ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, authToken );
        return requestHeaders;
    }

    /**
     * Checks if is life cycle status check out allowed.
     *
     * @param statusId
     *         the status id
     * @param authToken
     *         the auth token
     *
     * @return true, if is life cycle status check out allowed
     */
    private boolean isLifeCycleStatusCheckOutAllowed( String statusId, String authToken ) {

        SusResponseDTO request = null;
        try {
            request = SuSClient.getRequest( daemonManager.getServerAPIBase() + LIFECYCLE_STATUS_ID + statusId,
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        if ( request != null && request.getData() != null ) {
            String json = JsonUtils.toJson( request.getData() );
            logger.debug( "***  Lifecyle json  ***" + json );
            StatusConfigDTO statusConfigDTO = JsonUtils.jsonToObject( json, StatusConfigDTO.class );
            return ( statusConfigDTO.isAllowCheckOut() );
        } else {
            return false;
        }

    }

    /**
     * Gets the change type option form.
     *
     * @param authToken
     *         the auth token
     * @param projectId
     *         the parent id
     * @param selectionId
     *         the selection id
     *
     * @return the change type option form
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private List< SelectFormItem > changeTypeOptionForm( String authToken, String projectId, String selectionId ) throws IOException {
        logger.debug( "entered in getChangeTypeOptionForm method" );
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        List< SelectOptionsUI > options = new ArrayList<>();

        selectFormItem.setLabel( TYPE_OPTIONS_LABEL );
        selectFormItem.setName( CHANGE_TYPE );
        selectFormItem.setType( FIELD_TYPE_SELECT );
        selectFormItem.setMultiple( Boolean.FALSE );

        String json = getAllSelectedItemsFromServer( authToken, selectionId );
        List< String > idsInSelection = JsonUtils.jsonToList( json, String.class );

        List< ComputedDataObjectEntity > selectedDOEntity = new ArrayList<>();

        for ( String localFileId : idsInSelection ) {
            ComputedDataObjectEntity obj;
            obj = sqLiteManager.findById( localFileId );
            selectedDOEntity.add( obj );
        }

        LinkedHashSet< String > types = new LinkedHashSet<>();
        for ( ComputedDataObjectEntity computedDataObjectEntity : selectedDOEntity ) {
            types.add( computedDataObjectEntity.getType() );
        }

        List< String > fileTypes = new ArrayList<>();
        List< String > folderTypes = new ArrayList<>();

        ProjectConfiguration configProject = getSusObjectModelListFromAPI( projectId, authToken );
        if ( configProject != null && configProject.getEntityConfig() != null ) {
            logger.debug( ">>changeTypeOptionForm" );

            List< SuSObjectModel > abc = configProject.getEntityConfig();
            for ( SuSObjectModel suSObjectModel : abc ) {
                Object entity = initializeDTOEntity( suSObjectModel.getClassName() );
                if ( entity instanceof ContainerEntity ) {
                    folderTypes.add( suSObjectModel.getName() );
                }
                if ( entity instanceof DataObjectEntity ) {
                    fileTypes.add( suSObjectModel.getName() );
                }
            }

        } else {
            throw new SusException( PROJECT_CONFIGRATION_IS_NOT_LOADED );
        }

        options.addAll( getTypes( fileTypes ) );
        options.addAll( getTypes( folderTypes ) );

        setRulesAndMessageOnUI( selectFormItem );
        selectFormItem.setOptions( options );

        Map< String, Object > liveSearch = new HashMap<>();
        liveSearch.put( LIVESEARCH, true );
        selectFormItem.setPicker( liveSearch );

        return Arrays.asList( selectFormItem );
    }

    /**
     * Initialize DTO entity.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    private Object initializeDTOEntity( String className ) {

        Class< ? > entityClass = ReflectionUtils.getFieldTypeByName( initializeObject( className ).getClass(), ENTITY_CLASS_FIELD_NAME );
        return initializeObject( entityClass.getName() );
    }

    /**
     * Gets the types.
     *
     * @param fileTypes
     *         the file types
     *
     * @return the types
     */
    private Collection< ? extends SelectOptionsUI > getTypes( List< String > fileTypes ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( String type : fileTypes ) {
            options.add( new SelectOptionsUI( type, type ) );
        }
        return options;
    }

    /**
     * Sets the rules and message on UI.
     *
     * @param uiFormItem
     *         the new rules and message on UI
     */
    private void setRulesAndMessageOnUI( UIFormItem uiFormItem ) {
        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        uiFormItem.setRules( rules );
        uiFormItem.setMessages( message );
    }

    /**
     * Prepare ids map.
     *
     * @param serverFiles
     *         the server files
     * @param namesInSelection
     *         the names in selection
     * @param localDir
     *         the local dir
     *
     * @return the map
     */
    private Map< String, LocalFilesToSync > prepareIdsMap( List< FileInfo > serverFiles, List< String > namesInSelection, File localDir ) {
        Map< String, LocalFilesToSync > selectedIdsMaps = new HashMap<>();
        for ( String FileNameOrId : namesInSelection ) {
            LocalFilesToSync localfilesInfo = new LocalFilesToSync();
            localfilesInfo.setIsDir( true );
            if ( localDir.listFiles() != null ) {
                for ( File f : localDir.listFiles() ) {
                    if ( f.isFile() ) {
                        ComputedDataObjectEntity localFile;
                        localFile = sqLiteManager.findByPath( f.getPath() );
                        if ( localFile != null ) {
                            if ( FileNameOrId.equalsIgnoreCase( localFile.getId() ) ) {
                                setLocalFileInforForIdsMap( localfilesInfo, f, localFile );
                            } else {
                                for ( FileInfo serverFileName : serverFiles ) {
                                    // auto delete file should not be uploaded
                                    if ( serverFileName.getId().equals( FileNameOrId )
                                            && serverFileName.getName().equalsIgnoreCase( f.getName() )
                                            && !serverFileName.isAutoDeleted() ) {
                                        setLocalFileInforForIdsMap( localfilesInfo, f, localFile );
                                    }
                                }
                            }
                        }
                    }
                }

                selectedIdsMaps.put( FileNameOrId.toLowerCase(), localfilesInfo );
            }
        }
        return selectedIdsMaps;
    }

    /**
     * Prepare ids map 2.
     *
     * @param serverFile
     *         the server file
     * @param namesInSelection
     *         the names in selection
     * @param localDir
     *         the local dir
     *
     * @return the map
     */
    private Map< String, LocalFilesToSync > prepareIdsMapForCheckIn( List< FileInfo > serverFile, List< String > namesInSelection,
            File localDir ) {
        Map< String, LocalFilesToSync > selectedIdsMaps = new HashMap<>();
        ComputedDataObjectEntity temp = new ComputedDataObjectEntity();
        for ( String FileNameOrId : namesInSelection ) {
            LocalFilesToSync localfilesInfo = new LocalFilesToSync();
            if ( localDir != null && localDir.exists() ) {
                for ( File f : localDir.listFiles() ) {
                    if ( f.isFile() ) {
                        if ( FileNameOrId.equalsIgnoreCase( f.getName() ) ) {
                            setLocalFileInforForIdsMap( localfilesInfo, f, temp );

                        } else {
                            for ( FileInfo serverFileName : serverFile ) {
                                if ( serverFileName.getId().equals( FileNameOrId )
                                        && serverFileName.getName().equalsIgnoreCase( f.getName() ) && !serverFileName.isAutoDeleted() ) {
                                    setLocalFileInforForIdsMap( localfilesInfo, f, temp );
                                }
                            }
                        }
                    }
                }
                selectedIdsMaps.put( FileNameOrId.toLowerCase(), localfilesInfo );
            }
        }
        return selectedIdsMaps;
    }

}