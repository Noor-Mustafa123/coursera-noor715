package de.soco.software.simuspace.suscore.core.location.manager.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileExtension;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.FileObject;
import de.soco.software.simuspace.suscore.common.model.TransferLocationObject;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.ExecutionHosts;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.Hosts;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.common.util.SuSVaultUtils;
import de.soco.software.simuspace.suscore.core.location.manager.LocationCoreManager;
import de.soco.software.simuspace.suscore.core.location.model.LocationProperties;
import de.soco.software.simuspace.suscore.core.location.model.RunWorkflowLocationThread;
import de.soco.software.simuspace.suscore.executor.util.SusExecutorUtil;
import de.soco.software.simuspace.workflow.location.JobParametersLocationModel;
import de.soco.software.simuspace.workflow.location.MoreAdvApi32;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;

/**
 * The Class is responsible for providing business methods to usable for remote locations.
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class LocationCoreManagerImpl implements LocationCoreManager {

    private static final String SCRIPTS_DIR_NAME = "scripts";

    private static final String JSON_EXTENSION = ".json";

    /**
     * The Constant INVALID_PERMISSIONS.
     */
    private static final String INVALID_PERMISSIONS = "Invalid permissions.";

    /**
     * The Constant ACCESS_DENIED_EXCEPTION.
     */
    private static final String ACCESS_DENIED_EXCEPTION = "AccessDeniedException";

    /**
     * The Constant DIRECTORY_IS_NOT_EMPTY.
     */
    private static final String DIRECTORY_IS_NOT_EMPTY = "Directory is not empty.";

    /**
     * The Constant NO_SUCH_FILE_DIRECTORY_EXISTS.
     */
    private static final String NO_SUCH_FILE_DIRECTORY_EXISTS = "No such file/directory exists";

    /**
     * The Constant FILE_NOT_EXISTS_IN_VAULT.
     */
    public static final String FILE_NOT_EXISTS_IN_VAULT = "File does not exist in vault.";

    /**
     * The Constant FILE_NOT_EXISTS_IN_VAULT.
     */
    private static final String FILE_NOT_EXISTS_IN_STAGING = "File does not exist in staging.";

    /**
     * The Constant AUTH_TOKEN.
     */
    private static final String AUTH_TOKEN = "authToken";

    private static final String API_SYSTEM_LOCATION_ADD = "/api/system/location/add";

    /**
     * The Constant API_LOCATION_UPLOAD.
     */
    private static final String API_LOCATION_UPLOAD = "/api/core/location/upload";

    /**
     * The Constant API_LOCATION_UPLOAD_STAGING.
     */
    private static final String API_LOCATION_UPLOAD_STAGING = "/api/core/location/stagingupload";

    /**
     * The invalid token.
     */
    private static final String INVALID_TOKEN = "Invalid authorization token provided.";

    /**
     * The FILES_DIR.
     */
    private static final String FILES_DIR = "files";

    /**
     * The Constant JAVA_PATH.
     */
    public static final String JAVA_PATH = PropertiesManager.getJavaRunTimePath();

    /**
     * Instantiates a new location core manager impl.
     */
    public LocationCoreManagerImpl() {
        /*
         * For instantiating LocationCoreManagerImpl
         */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String isUp() {
        return "Location is up and running: " + PropertiesManager.getLocationName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPathExists( String path ) {
        return new File( path ).exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteVaultFile( String fileRelPath ) {
        try {
            Files.deleteIfExists( Paths.get( PropertiesManager.getVaultPath() + fileRelPath ) );
        } catch ( NoSuchFileException e ) {
            log.error( NO_SUCH_FILE_DIRECTORY_EXISTS, e );
            throw new SusException( NO_SUCH_FILE_DIRECTORY_EXISTS );
        } catch ( DirectoryNotEmptyException e ) {
            log.error( DIRECTORY_IS_NOT_EMPTY, e );
            throw new SusException( DIRECTORY_IS_NOT_EMPTY );
        } catch ( IOException e ) {
            log.error( INVALID_PERMISSIONS, e );
            throw new SusException( INVALID_PERMISSIONS );
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidToken( String authToken ) {

        if ( !PropertiesManager.getLocationAuthToken().contentEquals( authToken ) ) {
            throw new SusException( INVALID_TOKEN );
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exportVaultFile( TransferLocationObject transferObject ) {

        File file = new File( PropertiesManager.getVaultPath() + transferObject.getFilePath() );

        if ( !file.exists() ) {
            throw new SusException( FILE_NOT_EXISTS_IN_VAULT );
        }

        SuSClient.uploadFileRequest( transferObject.getTargetAddress() + API_LOCATION_UPLOAD, file,
                prepareDownloadHeaders( transferObject.getTargetToken(), transferObject.getFilePath() ) );

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exportStagingFile( String userUid, TransferLocationObject transferObject ) {

        File file = new File( PropertiesManager.getUserStagingPath( userUid ) + transferObject.getFilePath() );

        if ( !file.exists() ) {
            throw new SusException( FILE_NOT_EXISTS_IN_STAGING );
        }

        SuSClient.uploadStagingFileRequest( transferObject.getTargetAddress() + API_LOCATION_UPLOAD_STAGING, file,
                prepareDownloadHeaders( userUid, transferObject.getTargetToken(), transferObject.getFilePath() ) );

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getVaultFile( UUID id, int version ) {
        return SuSVaultUtils.getEncrypedFileFromPathIfEncpEnabled( id, version, PropertiesManager.getVaultPath() );
    }

    /**
     * Inits the location.
     */
    public void initLocation() {
        if ( !PropertiesManager.isMasterLocation() ) {
            LocationProperties locationProperties = new LocationProperties();
            SuSClient.postRequest( PropertiesManager.getLocationURL() + API_SYSTEM_LOCATION_ADD, JsonUtils.toJson( locationProperties ),
                    CommonUtils.prepareHeadersWithAuthToken( AUTH_TOKEN ) );
        }
    }

    /**
     * Prepare download headers.
     *
     * @param token
     *         the token
     * @param destPath
     *         the dest path
     *
     * @return the map
     */
    private Map< String, String > prepareDownloadHeaders( String token, String destPath ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTHORIZATION, token );
        requestHeaders.put( ConstantRequestHeader.DEST_PATH, destPath );

        return requestHeaders;

    }

    /**
     * Prepare download headers.
     *
     * @param userUid
     *         the user uid
     * @param token
     *         the token
     * @param destPath
     *         the dest path
     *
     * @return the map
     */
    private Map< String, String > prepareDownloadHeaders( String userUid, String token, String destPath ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTHORIZATION, token );
        requestHeaders.put( ConstantRequestHeader.DEST_PATH, destPath );
        requestHeaders.put( ConstantRequestHeader.USER_UID, userUid );

        return requestHeaders;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runServerJob( JobParametersLocationModel jobParametersLocationModel ) {

        // job type fix for designsummary
        if ( jobParametersLocationModel.getJobParameters().getJobType() == JobTypeEnums.SCHEME.getKey() ) {
            jobParametersLocationModel.getJobParameters().getWorkflow().setWorkflowType( JobTypeEnums.SCHEME.getKey() );
        } else {
            jobParametersLocationModel.getJobParameters().getWorkflow().setWorkflowType( JobTypeEnums.WORKFLOW.getKey() );
        }

        setUserIdInJobParameters( jobParametersLocationModel.getJobParameters() );

        if ( jobParametersLocationModel.getJobParameters().getRequestHeaders().getJobAuthToken() == null ) {

            jobParametersLocationModel.getJobParameters().getRequestHeaders().setJobAuthToken(
                    prepareJobToken( jobParametersLocationModel.getJobParameters().getJobRunByUserId().toString(),
                            jobParametersLocationModel.getUid(), jobParametersLocationModel.getJobParameters().getId(),
                            jobParametersLocationModel.getJobParameters().getServer(),
                            jobParametersLocationModel.getJobParameters().getRequestHeaders() ) );
        }

        final RunWorkflowLocationThread wfThread = new RunWorkflowLocationThread( jobParametersLocationModel, this, null );
        prepareWfUploadedFiles( jobParametersLocationModel.getJobParameters(), jobParametersLocationModel.getUid() );
        ExecutionHosts executionHost = null;
        if ( PropertiesManager.isHostEnable() ) {
            Hosts hostList = PropertiesManager.getHosts();
            if ( hostList != null && CollectionUtils.isNotEmpty( hostList.getExcutionHosts() ) ) {
                for ( ExecutionHosts host : hostList.getExcutionHosts() ) {
                    if ( host.getId().toString().equals( jobParametersLocationModel.getJobParameters().getRunsOn() ) ) {
                        executionHost = host;
                        break;
                    }
                }
            }
            if ( null == executionHost ) {
                SusExecutorUtil.threadPoolExecutorService.workflowExecute( wfThread,
                        UUID.fromString( jobParametersLocationModel.getJobParameters().getId() ) );
            } else {
                if ( executionHost.getId().toString().equals( ConstantsID.ANY_HOST_ID ) ) {
                    executionHost = getExecutionHost( jobParametersLocationModel.getUid(), hostList );
                    if ( null != executionHost ) {
                        jobParametersLocationModel.getJobParameters().setRunsOn( executionHost.getId().toString() );
                    }
                }
                SusExecutorUtil.threadPoolExecutorService.hostExecute( wfThread, executionHost,
                        UUID.fromString( jobParametersLocationModel.getJobParameters().getId() ) );
            }
        } else {
            SusExecutorUtil.threadPoolExecutorService.workflowExecute( wfThread,
                    UUID.fromString( jobParametersLocationModel.getJobParameters().getId() ) );
        }
    }

    /**
     * Set user id in job parameters.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void setUserIdInJobParameters( JobParameters jobParameters ) {
        if ( jobParameters.getJobRunByUserId() == null || jobParameters.getJobRunByUserUID() == null ) {
            try {
                UserDTO user = CommonUtils.getCurrentUser( jobParameters.getRequestHeaders().getToken() );

                jobParameters.setJobRunByUserId( UUID.fromString( user.getId() ) );
                jobParameters.setJobRunByUserUID( user.getUserUid() );
            } catch ( Exception e ) {
                log.warn( e.getMessage(), e );
            }
        }
    }

    /**
     * Gets the execution host.
     *
     * @param uid
     *         the uid
     * @param hostList
     *         the host list
     *
     * @return the execution host
     */
    private ExecutionHosts getExecutionHost( String uid, Hosts hostList ) {
        ExecutionHosts lessLoadHost = new ExecutionHosts();
        Map< String, String > loadMap = new HashMap<>();
        if ( hostList != null && CollectionUtils.isNotEmpty( hostList.getExcutionHosts() ) ) {
            for ( ExecutionHosts host : hostList.getExcutionHosts() ) {
                if ( !host.getId().toString().equals( ConstantsID.ANY_HOST_ID ) ) {
                    loadMap.put( host.getId().toString(),
                            executeCommand( LinuxUtils.getLoadNixHost( uid, host.getAddress() ).getCommand() ) );
                }
            }
        }
        for ( ExecutionHosts host : hostList.getExcutionHosts() ) {
            if ( host.getId().toString().equals( getKey( loadMap, Collections.min( loadMap.values() ) ) ) ) {
                lessLoadHost = host;
                break;
            }
        }
        return lessLoadHost;
    }

    /**
     * Gets the key.
     *
     * @param <K>
     *         the key type
     * @param <V>
     *         the value type
     * @param map
     *         the map
     * @param value
     *         the value
     *
     * @return the key
     */
    public < K, V > K getKey( Map< K, V > map, V value ) {
        for ( Entry< K, V > entry : map.entrySet() ) {
            if ( entry.getValue().equals( value ) ) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Execute command.
     *
     * @param command
     *         the command
     *
     * @return the string
     */
    private String executeCommand( String[] command ) {
        String load = null;
        try {
            Process process = runCommand( command );
            String line;
            try ( InputStreamReader isr = new InputStreamReader( process.getInputStream() );
                    final BufferedReader reader = new BufferedReader( isr ) ) {
                while ( ( line = reader.readLine() ) != null ) {
                    load = line.substring( line.lastIndexOf( ConstantsString.COMMA ) + ConstantsInteger.INTEGER_VALUE_ONE ).trim();
                }
            }
            process.waitFor();

            process.getInputStream().close();
            process.getOutputStream().close();
            process.getErrorStream().close();
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, LocationCoreManagerImpl.class );
        } catch ( InterruptedException e ) {
            log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
        }
        return load;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runServerSystemJob( JobParametersLocationModel jobParametersLocationModel ) {

        jobParametersLocationModel.getJobParameters().setJobType( JobTypeEnums.SYSTEM.getKey() );
        UserDTO user = CommonUtils.getCurrentUser( jobParametersLocationModel.getJobParameters().getRequestHeaders().getToken() );
        final RunWorkflowLocationThread wfThread = new RunWorkflowLocationThread( jobParametersLocationModel, this,
                PhaseInterceptorChain.getCurrentMessage() );
        prepareWfUploadedFiles( jobParametersLocationModel.getJobParameters(), user.getUserUid() );
        SusExecutorUtil.threadPoolExecutorService.systemWorkflowExecute( wfThread,
                UUID.fromString( jobParametersLocationModel.getJobParameters().getId() ) );
    }

    /**
     * Prepares files used by workflow elements by extracting the uploaded zip file in the vault.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void prepareWfUploadedFiles( JobParameters jobParameters, String userUid ) {

        String zipPath =
                PropertiesManager.getUserStagingPath( userUid ) + File.separator + jobParameters.getWorkflow().getId() + File.separator
                        + jobParameters.getName();

        File zipFile = new File( zipPath );

        if ( zipFile.exists() ) {
            try {
                if ( zipFile.renameTo( new File( zipPath + ConstantsFileExtension.ZIP ) ) ) {

                    FileUtils.setGlobalExecuteFilePermissions( new File(
                            PropertiesManager.getUserStagingPath( userUid ) + File.separator + jobParameters.getWorkflow().getId() ) );
                    LinuxUtils.createDirectory( userUid,
                            PropertiesManager.getUserStagingPath( userUid ) + File.separator + jobParameters.getWorkflow().getId()
                                    + File.separator + "/files" );
                    LinuxUtils.extractZipFile( userUid, zipFile.getAbsolutePath() + ConstantsFileExtension.ZIP );

                    FileUtils.deleteFile( zipPath + ConstantsFileExtension.ZIP ); // removing temp zip file after extract
                }
            } catch ( Exception e ) {
                ExceptionLogger.logException( e, LocationCoreManagerImpl.class );
            }
        }

    }

    /**
     * Create extract directory for zip file.
     *
     * @param userUid
     *         the user uid
     * @param jobParameters
     *         the job parameters
     *
     * @return the file
     */
    private File createFilesDirectory( String userUid, JobParameters jobParameters ) {
        String filesPath =
                PropertiesManager.getUserStagingPath( userUid ) + File.separator + jobParameters.getWorkflow().getId() + File.separator
                        + FILES_DIR;
        File dir = new File( filesPath );
        try {
            if ( !dir.exists() ) {
                dir.mkdir();
            } else if ( dir.isDirectory() ) {
                org.apache.commons.io.FileUtils.cleanDirectory( dir );
            }
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, this.getClass() );
        }
        return dir;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeWFProcessOnServer( JobParametersLocationModel jobParametersLocationModel, Message message ) throws IOException {
        /*
         * STEPS create java process through executorService copy jobParameters to a
         * file submit java process with command java -jar wfengine.jar -wf
         * <STAGING>/job_id/job_name.json -u userid
         */

        JobParameters jobParameters = jobParametersLocationModel.getJobParameters();
        jobParameters.getRequestHeaders().setToken( null );
        if ( jobParameters.getJobType() != JobTypeEnums.VARIANT.getKey() ) {
            jobParameters.getWorkingDir().setPath( PropertiesManager.getUserStagingPath( jobParametersLocationModel.getUid() ) );
        }

        String id;
        if ( jobParameters.getJobType() == JobTypeEnums.WORKFLOW.getKey() || null == jobParameters.getJobInteger() ) {
            id = getJobIntegerId( UUID.fromString( jobParameters.getId() ), jobParameters.getServer(), jobParameters.getRequestHeaders() );
            if ( id == null ) {
                id = saveJobIds( UUID.fromString( jobParameters.getId() ), jobParameters.getServer(), jobParameters.getRequestHeaders() );
            }
            jobParameters.setJobInteger( Integer.parseInt( id ) );
        } else {
            id = jobParameters.getJobInteger().toString();
        }

        String jobScriptDirPath;
        if ( jobParameters.getJobType() == JobTypeEnums.VARIANT.getKey() ) {
            jobScriptDirPath = PropertiesManager.getUserStagingPath( jobParametersLocationModel.getUid() ) + File.separator
                    + jobParametersLocationModel.getLoadcaseName() + "_" + jobParameters.getWorkflow().getName() + "_" + id + File.separator
                    + SCRIPTS_DIR_NAME;
        } else {
            jobScriptDirPath =
                    PropertiesManager.getUserStagingPath( jobParametersLocationModel.getUid() ) + File.separator + jobParameters.getName()
                            + "_" + id + File.separator + SCRIPTS_DIR_NAME;
        }

        SimpleDateFormat ft = new SimpleDateFormat( "yyMMddhhmmssMs" );
        String jobScriptFileName = jobParameters.getName() + "_" + ft.format( new Date() );
        String jobScriptFilePath = jobScriptDirPath + File.separator + jobScriptFileName + JSON_EXTENSION;

        LinuxUtils.createDirectory( jobParametersLocationModel.getUid(), jobScriptDirPath );
        File scriptFile = File.createTempFile( jobParameters.getName(), JSON_EXTENSION,
                new File( PropertiesManager.getDefaultServerTempPath() ) );
        FileUtils.setGlobalReadFilePermissions( scriptFile );
        new ObjectMapper().writeValue( scriptFile, jobParameters );
        LinuxUtils.createFile( jobParametersLocationModel.getUid(), jobScriptFilePath );
        LinuxUtils.writeFileByOtherFile( jobParametersLocationModel.getUid(), scriptFile.getAbsolutePath(), jobScriptFilePath );

        scriptFile.delete();

        executeSubProcess( jobParametersLocationModel, new File( jobScriptFilePath ), jobParameters.getRunsOn() );
    }

    private String saveJobIds( UUID uuid, RestAPI server, RequestHeaders requestHeaders ) {
        final String url = prepareURL( "/api/workflow/saveJobIds/" + uuid.toString(), server );
        SusResponseDTO susResponseDTO = SuSClient.getRequest( url, prepareHeaders( requestHeaders ) );
        String id = null;
        if ( susResponseDTO != null ) {
            if ( !susResponseDTO.getSuccess() ) {
                throw new SusException( new Exception( susResponseDTO.getMessage().getContent() ), getClass() );
            } else if ( susResponseDTO.getData() != null ) {
                id = susResponseDTO.getData().toString();
            }
        }
        return id;
    }

    private String getJobIntegerId( UUID uuid, RestAPI server, RequestHeaders requestHeaders ) {
        final String url = prepareURL( "/api/workflow/getJobIdByUUID/" + uuid.toString(), server );
        SusResponseDTO susResponseDTO = SuSClient.getRequest( url, prepareHeaders( requestHeaders ) );
        String id = null;
        if ( susResponseDTO != null ) {
            if ( !susResponseDTO.getSuccess() ) {
                throw new SusException( new Exception( susResponseDTO.getMessage().getContent() ), getClass() );
            } else if ( null != susResponseDTO.getData() ) {
                id = susResponseDTO.getData().toString();
            }
        }
        return id;
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    @Override
    public List< FileObject > getFileList( String userName, String password, String path, String show ) {

        if ( path.isEmpty() ) {
            path = getUserHome( userName, password );
        }
        if ( OSValidator.isUnix() ) {
            return getLinuxFiles( userName, path, show );
        } else if ( OSValidator.isWindows() ) {
            return getWinFiles( userName, password, path, show );
        }
        return new ArrayList<>();
    }

    /**
     * Gets the win files.
     *
     * @param userName
     *         the user name
     * @param password
     *         the password
     * @param path
     *         the path
     * @param show
     *         the show
     *
     * @return the win files
     */
    private List< FileObject > getWinFiles( String userName, String password, String path, String show ) {
        List< FileObject > fileList = new ArrayList<>();
        File tmpFile;
        try {
            tmpFile = File.createTempFile( "fileList", ".txt", new File( PropertiesManager.getStagingPath() ) );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_CREATED.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_CREATED.getKey() ) );
        }
        final String[] command = { PropertiesManager.getKarafPath() + "\\wfengine\\suscore-simflow-wfengine.bat", JAVA_PATH,
                PropertiesManager.getKarafPath(), tmpFile.getAbsolutePath(), path, show };
        winImpersonatedExec( userName, password, command );
        try ( FileInputStream fi = new FileInputStream( tmpFile ); ObjectInputStream oi = new ObjectInputStream( fi ) ) {
            while ( !tmpFile.exists() ) {
                Thread.sleep( 2000 );
            }
            String fileResult = ( String ) oi.readObject();
            if ( fileResult.contains( ACCESS_DENIED_EXCEPTION ) ) {
                Files.delete( tmpFile.toPath() );
                throw new AccessDeniedException( fileResult.split( ConstantsString.SPACE )[ ConstantsInteger.INTEGER_VALUE_ONE ].trim() );
            } else {
                fileList = JsonUtils.jsonToList( fileResult, FileObject.class );
            }
            Files.delete( tmpFile.toPath() );
        } catch ( FileNotFoundException e ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), tmpFile ), e );
        } catch ( ClassNotFoundException e ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey() ), e );
        } catch ( InterruptedException e ) {
            log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
        } catch ( AccessDeniedException e ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey() ), e );
        } catch ( IOException e ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), tmpFile ), e );
        }
        return fileList;
    }

    /**
     * Gets the linux files.
     *
     * @param userName
     *         the user name
     * @param serverDirOrFilePath
     *         the server dir or file path
     * @param action
     *         the action
     *
     * @return the linux files
     */
    private List< FileObject > getLinuxFiles( String userName, String serverDirOrFilePath, String action ) {
        List< FileObject > fileList = new ArrayList<>();
        Path tmpFile;
        try {
            tmpFile = Files.createTempFile( Paths.get( PropertiesManager.getDefaultServerTempPath() ), "fileList", ".txt" );
            FileUtils.setGlobalAllFilePermissions( tmpFile.toFile() );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, LocationCoreManagerImpl.class );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_CREATED.getKey() ) );
        }
        LinuxUtils.getLinuxServerFilesCommand( serverDirOrFilePath, action, tmpFile.toString(), userName );
        try {
            while ( !Files.exists( tmpFile ) ) {
                Thread.sleep( 2000 );
            }

            String fileResult = Files.readString( tmpFile, StandardCharsets.UTF_8 );

            if ( fileResult.contains( ACCESS_DENIED_EXCEPTION ) ) {
                Files.delete( tmpFile );
                throw new AccessDeniedException(
                        fileResult.split( ConstantsString.SPACE )[ ConstantsInteger.INTEGER_VALUE_ONE ].trim() );
            } else {
                fileList = JsonUtils.jsonToList( fileResult, FileObject.class );
            }
        } catch ( FileNotFoundException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), tmpFile ), e );
        } catch ( InterruptedException e ) {
            log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
        } catch ( AccessDeniedException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey() ), e );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), tmpFile ), e );
        } finally {
            try {
                if ( Files.exists( tmpFile ) ) {
                    Files.delete( tmpFile );
                }
            } catch ( IOException e ) {
                log.error( "ssfs File not deleted  :" + e.getMessage() );
            }
        }

        return fileList;
    }

    /**
     * {@inheritDoc}
     */
    public String getUserHome( String userName, String password ) {
        String userHome = null;
        if ( OSValidator.isUnix() ) {
            return LinuxUtils.getNixHome( userName );
        } else if ( OSValidator.isWindows() ) {
            File tmpFile;
            try {
                tmpFile = File.createTempFile( "userHome", ".txt", new File( PropertiesManager.getStagingPath() ) );
            } catch ( IOException e ) {
                ExceptionLogger.logException( e, LocationCoreManagerImpl.class );
                throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_CREATED.getKey() ) );
            }
            final String[] command = { PropertiesManager.getKarafPath() + "\\wfengine\\suscore-simflow-wfengine.bat", JAVA_PATH,
                    PropertiesManager.getKarafPath(), tmpFile.getAbsolutePath() };
            winImpersonatedExec( userName, password, command );
            try {
                Thread.sleep( 5000 );
                try ( FileInputStream fi = new FileInputStream( tmpFile ); ObjectInputStream oi = new ObjectInputStream( fi ) ) {
                    userHome = oi.readObject().toString();
                }
                Files.delete( tmpFile.toPath() );
            } catch ( ClassNotFoundException | IOException e ) {
                ExceptionLogger.logException( e, LocationCoreManagerImpl.class );
            } catch ( InterruptedException e ) {
                log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
            }
            return userHome;
        }
        return userHome;
    }

    /**
     * Execute sub process.
     *
     * @param jobParametersLocationModel
     *         the job parameters location model
     * @param jobFile
     *         the jobFile
     * @param runOn
     *         the run on
     */
    private void executeSubProcess( JobParametersLocationModel jobParametersLocationModel, File jobFile, String runOn ) {
        try {
            boolean isPersonated = PropertiesManager.isImpersonated();
            if ( OSValidator.isUnix() ) {
                ExecutionHosts executionHost = null;
                if ( PropertiesManager.isHostEnable() ) {
                    Hosts hostList = PropertiesManager.getHosts();
                    if ( hostList != null && CollectionUtils.isNotEmpty( hostList.getExcutionHosts() ) ) {
                        for ( ExecutionHosts host : hostList.getExcutionHosts() ) {
                            if ( host.getId().toString().equals( runOn ) ) {
                                executionHost = host;
                                break;
                            }
                        }
                    }
                    if ( null == executionHost ) {
                        LinuxUtils.linuxExecuteEngineJob( jobFile.getAbsolutePath(), jobParametersLocationModel.getUid() );
                    } else {
                        if ( executionHost.getId().toString().equals( ConstantsID.ANY_HOST_ID ) ) {
                            executionHost = getExecutionHost( jobParametersLocationModel.getUid(), hostList );
                            if ( null != executionHost ) {
                                jobParametersLocationModel.getJobParameters().setRunsOn( executionHost.getId().toString() );
                            }
                        }
                        LinuxUtils.linuxExecuteNixHost( jobParametersLocationModel.getUid(), jobFile.getAbsolutePath(),
                                executionHost.getAddress(), executionHost.getEngineDir() );
                    }
                } else {
                    LinuxUtils.linuxExecuteEngineJob( jobFile.getAbsolutePath(), jobParametersLocationModel.getUid() );
                }
            } else if ( OSValidator.isWindows() ) {
                final String[] command2 = LinuxUtils.getWindowsCommandForWorkflow( jobFile.getAbsolutePath() );

                if ( isPersonated ) {
                    winImpersonatedExec( jobParametersLocationModel.getUid(), jobParametersLocationModel.getPassword(), command2 );
                } else {
                    LinuxUtils.runCommand( command2, ConstantsString.COMMAND_KARAF_LOGGING_ON, null );
                }
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, LocationCoreManagerImpl.class );
        }
    }

    /**
     * Win impersonated exec.
     *
     * @param uid
     *         the uid
     * @param password
     *         the password
     * @param command
     *         the command
     */
    private void winImpersonatedExec( String uid, String password, final String[] command ) {
        final WString nullW = null;
        final PROCESS_INFORMATION processInformation = new PROCESS_INFORMATION();
        final STARTUPINFO startupInfo = new STARTUPINFO();
        final boolean result = MoreAdvApi32.INSTANCE.CreateProcessWithLogonW( new WString( uid ), // user
                nullW, // domain , null if local
                new WString( password ), // password
                MoreAdvApi32.LOGON_WITH_PROFILE, // dwLogonFlags
                nullW, // lpApplicationName
                new WString( String.join( " ", command ) ), // command line
                MoreAdvApi32.CREATE_NO_WINDOW, // dwCreationFlags
                null, // lpEnvironment
                new WString( PropertiesManager.getKarafPath() ), // directory
                startupInfo, processInformation );

        if ( !result ) {
            final int error = Kernel32.INSTANCE.GetLastError();
            log.error( "OS error #" + error );
            log.error( Kernel32Util.formatMessageFromLastErrorCode( error ) );
        }
    }

    /**
     * Run command.
     *
     * @param command
     *         the command
     *
     * @return the process
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private Process runCommand( String[] command ) throws IOException {
        final ProcessBuilder probuilder = new ProcessBuilder( command );
        probuilder.redirectErrorStream( true );
        Process process = probuilder.start();
        log.info( probuilder.command().toString() );
        return process;
    }

    private String prepareJobToken( String userId, String uid, String id, RestAPI server, RequestHeaders requestHeaders ) {
        final String url = prepareURL( "/api/auth/prepareJobToken/" + userId + "/" + uid + "/" + id + "/" + requestHeaders.getToken(),
                server );
        SusResponseDTO susResponseDTO = SuSClient.getRequest( url, prepareHeaders( requestHeaders ) );
        String token = null;
        if ( susResponseDTO != null ) {
            if ( !susResponseDTO.getSuccess() ) {
                throw new SusException( new Exception( susResponseDTO.getMessage().getContent() ), getClass() );
            } else if ( susResponseDTO.getData() != null ) {
                token = susResponseDTO.getData().toString();
            }
        }
        return token;
    }

    private String prepareURL( String url, RestAPI server ) {
        if ( server != null ) {
            return server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort() + url;
        } else {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) ) );
        }
    }

    private Map< String, String > prepareHeaders( RequestHeaders headers ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, headers.getToken() );
        requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, headers.getJobAuthToken() );
        return requestHeaders;
    }

}