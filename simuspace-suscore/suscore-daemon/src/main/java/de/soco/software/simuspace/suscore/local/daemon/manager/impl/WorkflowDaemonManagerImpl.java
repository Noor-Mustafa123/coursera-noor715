package de.soco.software.simuspace.suscore.local.daemon.manager.impl;

import javax.annotation.PreDestroy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTemplates;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobRelationTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.model.ObjectVariableDTO;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateFileDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.ExecutionHosts;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.executor.model.ExecutorProperties;
import de.soco.software.simuspace.suscore.local.daemon.manager.SuscoreDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.manager.WorkflowDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.properties.DaemonPropertiesManager;
import de.soco.software.simuspace.suscore.local.daemon.thread.RunDaemonJobThread;
import de.soco.software.simuspace.suscore.local.daemon.thread.RunDaemonSchemeJobThread;
import de.soco.software.simuspace.suscore.local.daemon.thread.service.DaemonThreadPoolExecutorService;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.dto.ImportWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.processing.WFExecutionManager;
import de.soco.software.simuspace.workflow.processing.impl.WorkflowExecutionManagerImpl;
import de.soco.software.simuspace.workflow.util.WorkflowDefinitionUtil;

/**
 * This Class is Responsible for the starting of Spring-Boot Application as a Deamon.
 *
 * @author Nosheen.Sharif
 */
@Component
public class WorkflowDaemonManagerImpl implements WorkflowDaemonManager {

    /**
     * The Constant IS_LOCATION.
     */
    private static final String IS_LOCATION = "isLocation";

    /**
     * The Constant RUNS_ON.
     */
    private static final String RUN_ON_SERVER = "server";

    /**
     * The Constant API_RUN_WORKFLOW.
     */
    private static final String API_RUN_WORKFLOW = "/api/workflow/run/job";

    /**
     * The Constant API_GET_DYNAMIC_PROPERTY.
     */
    private static final String API_GET_DYNAMIC_PROPERTY = "/api/workflow/dynamic/fields/";

    /**
     * The Constant WORKFLOW.
     */
    private static final String WORKFLOW = "workflow/";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant API_DOCUMENT_UPLOAD.
     */
    private static final String API_DOCUMENT_UPLOAD = "/api/document/upload";

    /**
     * The Constant API_DOCUMENT_UPLOAD.
     */
    private static final String API_GET_LOCATION = "/api/system/location/";

    /**
     * The Constant COLON.
     */
    private static final String COLON = ":";

    /**
     * The Constant ITEMS.
     */
    private static final String ITEMS = "items";

    /**
     * The Constant PERMITTED_API.
     */
    private static final String PERMITTED_API = "/api/system/permissions/permitted";

    /**
     * The Constant LICENSE_MANAGER_API.
     */
    private static final String LICENSE_MANAGER_API = "/api/workflow/user/haslicense";

    /**
     * The Constant SCHEME_LICENSE_API.
     */
    private static final String SCHEME_LICENSE_API = "/api/workflow/user/schemelicense/";

    /**
     * The Constant API_LOCATION_EXPORT_FILE.
     */
    private static final String API_LOCATION_EXPORT_FILE = "/api/core/location/export/staging/file";

    /**
     * The FILES_DIR.
     */
    private static final String FILES_DIR = "files";

    /**
     * logger for logging daemong logging information.
     */
    private static final Logger logger = Logger.getLogger( ConstantsString.DAEMON_LOGGER );

    /**
     * The parent job.
     */
    private static final Map< UUID, Job > parentJob = new HashMap<>();

    /**
     * The execution manager of workflow engine to start stop jobs.
     */
    private WFExecutionManager executionManager;

    /**
     * The daemon manager.
     */
    @Autowired
    private SuscoreDaemonManager daemonManager;

    /**
     * The exe.
     */
    @Autowired
    private DaemonThreadPoolExecutorService daemonExecutorService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void runjob( JobParameters jobParameters ) {
        if ( jobParameters != null ) {
            validateRights( jobParameters );
            validateWorkflow( jobParameters );
            setUserIdInJobParameters( jobParameters );
            if ( !WorkflowDefinitionUtil.isLocal( jobParameters.getRunsOn() ) ) {
                if ( jobParameters.isFileRun() ) {
                    throw new SusException( "Cannot run file based workflows on server." );
                }
                new Thread( new Runnable() {

                    @Override
                    public void run() {
                        uploadLocalWfFilesToServer( jobParameters );
                        submitServerJob( jobParameters );
                    }
                } ).start();

            } else {
                LocationDTO location = getLocation( jobParameters ); // get execution location
                validateLocalOSForLocalJob( jobParameters.getRunsOn(), location.getName() );

                jobParameters.getWorkflow().setWorkflowType( JobTypeEnums.WORKFLOW.getKey() );
                jobParameters.setJobType( JobTypeEnums.WORKFLOW.getKey() );
                RunDaemonJobThread workflowRunnable = new RunDaemonJobThread( new WorkflowExecutionManagerImpl(), jobParameters );
                jobParameters.setId( jobParameters.getId() == null ? UUID.randomUUID().toString() : jobParameters.getId() );
                daemonExecutorService.daemonWorkflowExecute( workflowRunnable, UUID.fromString( jobParameters.getId() ) );
            }
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
                String url = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                        + jobParameters.getServer().getPort(); // preparing server url

                UserDTO user = CommonUtils.getCurrentUser( jobParameters.getRequestHeaders().getToken(), url );

                jobParameters.setJobRunByUserId( UUID.fromString( user.getId() ) );
                jobParameters.setJobRunByUserUID( user.getUserUid() );
            } catch ( Exception e ) {
            }
        }
    }

    /**
     * Submit server job.
     *
     * @param jobParameters
     *         the job parameters
     */
    @Override
    public List< Map< String, Object > > getDynamicFields( String plugin, String jobParametersString, RestAPI server,
            RequestHeaders requestHeaders ) {
        List< Map< String, Object > > fieldMapList = new ArrayList<>();
        String url =
                server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort() + API_GET_DYNAMIC_PROPERTY + plugin;
        final SusResponseDTO responseDTO = SuSClient.postRequest( url, jobParametersString,
                WorkflowDefinitionUtil.prepareHeaders( requestHeaders ) );
        if ( responseDTO.getData() != null ) {
            return ( List< Map< String, Object > > ) JsonUtils.jsonToList( JsonUtils.objectToJson( responseDTO.getData() ), fieldMapList );
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runScheme( JobParameters jobParameters ) {
        if ( jobParameters != null ) {
            validateSchemeRights( jobParameters );
            validateScheme( jobParameters );
            setUserIdInJobParameters( jobParameters );
            if ( !WorkflowDefinitionUtil.isLocal( jobParameters.getRunsOn() ) ) {
                if ( jobParameters.isFileRun() ) {
                    throw new SusException( "Cannot run file based workflows on server." );
                }
                uploadLocalWfFilesToServer( jobParameters );

                new Thread() {

                    @Override
                    public void run() {
                        submitServerWFJob( jobParameters );
                    }
                }.start();
            } else {
                LocationDTO location = getLocation( jobParameters ); // get execution location
                validateLocalOSForLocalJob( jobParameters.getRunsOn(), location.getName() );
                runSchemeJobs( jobParameters );
            }
        }
    }

    /**
     * Validates WF Scheme.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void validateScheme( JobParameters jobParameters ) {
        String url = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort() + "/api/config/workflowscheme/" + jobParameters.getWorkflow().getId().toString();
        SusResponseDTO response = SuSClient.getRequest( url, WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
        if ( !response.getSuccess() ) {
            throw new SusException( response.getMessage().getContent() );
        }
    }

    /**
     * Submit server WF job.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void submitServerWFJob( JobParameters jobParameters ) {
        final String payload = JsonUtils.toFilteredJson( new String[]{}, jobParameters );
        String url = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort() + "/api/config/workflowscheme/" + jobParameters.getWorkflow().getId().toString()
                + "/runscheme";
        SuSClient.postRequest( url, payload, WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
    }

    /**
     * {@inheritDoc}
     */
    private void runSchemeJobs( JobParameters jobParameters ) {

        AtomicInteger executedElements = new AtomicInteger( ConstantsInteger.INTEGER_VALUE_ZERO );
        Job masterJobParameters = prepareJobModel( jobParameters.getName() + "_" + new Date().getTime() + "_Master", "",
                jobParameters.getWorkflow() );
        masterJobParameters.setId( jobParameters.getId() == null ? UUID.randomUUID() : UUID.fromString( jobParameters.getId() ) );
        masterJobParameters.setRunsOn( new LocationDTO( UUID.fromString( jobParameters.getRunsOn() ) ) );
        List< LogRecord > jobLog = new ArrayList<>();
        jobLog.add(
                new LogRecord( ConstantsMessageTypes.INFO, MessageBundleFactory.getMessage( Messages.GOING_TO_MAKE_MASTER_JOB.getKey() ),
                        new Date() ) );
        masterJobParameters.setLog( jobLog );
        masterJobParameters.setProgress(
                new ProgressBar( Long.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ), executedElements.longValue() ) );
        masterJobParameters.setServer( jobParameters.getServer() );
        masterJobParameters.setRequestHeaders( jobParameters.getRequestHeaders() );
        masterJobParameters.setJobType( JobTypeEnums.SCHEME.getKey() );
        masterJobParameters.setJobRelationType( JobRelationTypeEnums.MASTER.getKey() );
        masterJobParameters.setWorkingDir( prepareWorkingDirectory( jobParameters ) );
        Job savedMasterJob = getExecutionManager().saveJob( masterJobParameters );
        savedMasterJob.setServer( jobParameters.getServer() );
        savedMasterJob.setRequestHeaders( jobParameters.getRequestHeaders() );
        logger.info( ">>submitMasterLoadcase jobParametersM: " + savedMasterJob );
        logger.info( "Monitoring job with id: " + savedMasterJob.getId() );

        String jobName = jobParameters.getName();
        copyLocalWfFilesToLocalBaseDir( jobParameters );

        parentJob.put( savedMasterJob.getId(), savedMasterJob );

        RunDaemonSchemeJobThread schemeRunnable = new RunDaemonSchemeJobThread( new WorkflowExecutionManagerImpl(), jobParameters,
                savedMasterJob, jobParameters.getName() );
        daemonExecutorService.daemonWorkflowExecute( schemeRunnable, savedMasterJob.getId() );

    }

    /**
     * Prepare working directory.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the engine file
     */
    private EngineFile prepareWorkingDirectory( JobParameters jobParameters ) {
        EngineFile newEngineFile = jobParameters.getWorkingDir();
        newEngineFile.setPath( jobParameters.getWorkingDir().getPath() + File.separator + jobParameters.getWorkflow().getId() );
        return newEngineFile;
    }

    /**
     * Creates the file with file object.
     *
     * @param file
     *         the file
     */
    private void createFileWithFileObject( File file ) {
        if ( !file.exists() ) {
            file.mkdirs();
        }
    }

    /**
     * Prepare properties.
     *
     * @param poolsize
     *         the poolsize
     * @param maxsize
     *         the maxsize
     * @param aliveTime
     *         the alive time
     * @param qSize
     *         the q size
     *
     * @return the executor properties[]
     */
    private static ExecutorProperties[] prepareProperties( String poolsize, String maxsize, String aliveTime, String qSize ) {
        ExecutorProperties[] properArr1 = new ExecutorProperties[]{ new ExecutorProperties( poolsize, maxsize, aliveTime, qSize ) };
        return properArr1;
    }

    /**
     * Creates the staging file.
     *
     * @param jobParameters
     *         the job parameters
     * @param designSummary
     *         the design summary
     * @param jobId
     *         the job id
     *
     * @return the string
     */
    private String createStagingFile( JobParameters jobParameters, Map< String, Object > designSummary, UUID jobId ) {
        String staggingfilePath = null;
        final WorkflowDefinitionDTO jobParamDef = WorkflowDefinitionUtil.getWorkflowDefinitionDTOFromMap(
                jobParameters.getWorkflow().prepareDefinition() );
        final List< UserWFElement > jobWFElements = WorkflowDefinitionUtil.setWorkflowElements( jobParamDef );
        for ( UserWFElement userWFElement : jobWFElements ) {
            if ( userWFElement.getKey().equals( ElementKeys.IO.getKey() ) ) {
                List< Field< ? > > fields = userWFElement.getFields();
                for ( Field< ? > field : fields ) {
                    ScanFileDTO scanFile = null;
                    if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() ) && field.getTemplateType()
                            .equalsIgnoreCase( FieldTemplates.DESIGN_VARIABLE.getValue() ) ) {
                        scanFile = JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), ScanFileDTO.class );
                        staggingfilePath = prepareTemplateFile( scanFile, designSummary, jobParameters, jobId );
                    }
                }
            }
        }
        return staggingfilePath;
    }

    /**
     * Prepare template file.
     *
     * @param scanFile
     *         the scan file
     * @param designSummary
     *         the design summary
     * @param jobParameters
     *         the job parameters
     * @param jobId
     *         the master job id
     *
     * @return the string
     */
    private String prepareTemplateFile( ScanFileDTO scanFile, Map< String, Object > designSummary, JobParameters jobParameters,
            UUID jobId ) {
        String fullPath = null;
        List< ObjectVariableDTO > variables = scanFile.getVariables();
        String fileName = new File( scanFile.getFile() ).getName();
        Map< Integer, ObjectVariableDTO > map = new HashMap<>();
        String stagingPath = getStagingPathOfServer( jobParameters.getServer().getProtocol(), jobParameters.getServer().getHostname(),
                jobParameters.getServer().getPort(), jobParameters.getRequestHeaders() ) + File.separator + jobId;
        for ( ObjectVariableDTO objectVariableDTO : variables ) {
            map.put( Integer.parseInt( objectVariableDTO.getHighlight().getLineNumber() ), objectVariableDTO );
        }
        try ( BufferedReader br = new BufferedReader( new FileReader( scanFile.getFile() ) ) ) {
            String line = null;
            int lineCount = 0;
            File file = new File( stagingPath );
            if ( !file.exists() ) {
                file.mkdirs();
            }

            fullPath = stagingPath + File.separator + fileName;
            FileWriter writer = new FileWriter( fullPath );
            while ( ( line = br.readLine() ) != null ) {
                StringBuilder buf = new StringBuilder( line );
                if ( map.containsKey( lineCount ) ) {

                    ObjectVariableDTO response = map.get( lineCount );
                    writer.write( buf.replace( Integer.parseInt( response.getHighlight().getStart() ),
                            Integer.parseInt( response.getHighlight().getEnd() ),
                            designSummary.get( response.getVariableName().toLowerCase() ).toString() ) + ConstantsString.NEW_LINE );
                } else {
                    writer.write( buf + ConstantsString.NEW_LINE );
                }
                lineCount++;
            }
            writer.close();
        } catch ( NumberFormatException | IOException e ) {
            logger.error( "ERROR", e );
        }
        return stagingPath;
    }

    /**
     * Gets the staging path of server.
     *
     * @param protocol
     *         the protocol
     * @param hostname
     *         the hostname
     * @param port
     *         the port
     * @param requestHeaders
     *         the request headers
     *
     * @return the staging path of server
     */
    private String getStagingPathOfServer( String protocol, String hostname, String port, RequestHeaders requestHeaders ) {
        String api = protocol + hostname + ConstantsString.COLON + port + API_GET_LOCATION + "stagingpath";
        SusResponseDTO susresponseL = SuSClient.getRequest( api, WorkflowDefinitionUtil.prepareHeaders( requestHeaders ) );
        return susresponseL.getData().toString();
    }

    /**
     * Prepare job model.
     *
     * @param jobName
     *         the job name
     * @param description
     *         the description
     * @param workflow
     *         the workflow
     *
     * @return the job
     */
    private Job prepareJobModel( String jobName, String description, LatestWorkFlowDTO workflow ) {
        Status status = new Status( WorkflowStatus.COMPLETED );

        final Job jobImpl = new JobImpl();
        jobImpl.setDescription( description );
        jobImpl.setName( jobName );
        jobImpl.setWorkflowId( workflow.getId() );
        jobImpl.setWorkflowName( workflow.getName() );
        jobImpl.setOs( OSValidator.getOperationSystemName() );

        try {
            jobImpl.setMachine( InetAddress.getLocalHost().getHostName() );
        } catch ( final UnknownHostException e ) {
            logger.error( "Machine Name Error: " + e.getMessage() );
        }

        jobImpl.setStatus( status );
        jobImpl.setWorkflowVersion( new VersionDTO( SusConstantObject.DEFAULT_VERSION_NO ) );

        return jobImpl;
    }

    /**
     * Uploads local files used by workflow to remote locations and updates JobParameters file path with server path.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void uploadLocalWfFilesToServer( JobParameters jobParameters ) {
        // Getting workflow elements from job parameter
        final WorkflowDefinitionDTO jobParamDef = WorkflowDefinitionUtil.getWorkflowDefinitionDTOFromMap(
                jobParameters.getWorkflow().prepareDefinition() );
        final List< UserWFElement > jobWFElements = WorkflowDefinitionUtil.setWorkflowElements( jobParamDef );

        Set< String > filePaths = getFilePathsFromWfJobParameters( jobWFElements );
        Set< String > dirPaths = getDirectoriesPathsFromWfJobParameters( jobWFElements );

        if ( !filePaths.isEmpty() || !dirPaths.isEmpty() ) {
            LocationDTO location = getLocation( jobParameters ); // get execution location

            String url = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                    + jobParameters.getServer().getPort(); // preparing server url

            WorkflowDefinitionUtil.uploadWorkflowFilesToServer( jobParameters, url, filePaths, dirPaths );

            if ( location.getUrl() != null ) {
                if ( !url.equals( location.getUrl() ) ) {
                    // if execution location is different to server then export files to the execution location
                    WorkflowDefinitionUtil.exportWorkflowFilesToLocationStaging( jobParameters, location, url );
                }
            }

            WorkflowDefinitionUtil.renameLocalPathsToStagingPath( jobWFElements, getUserStagingPathOfServer( jobParameters ), jobParamDef,
                    jobParameters );
        }
    }

    /**
     * Upload local wf files to local base dir.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void copyLocalWfFilesToLocalBaseDir( JobParameters jobParameters ) {

        Set< String > filePaths = new HashSet<>();

        // Getting workflow elements from job parameter
        final WorkflowDefinitionDTO jobParamDef = WorkflowDefinitionUtil.getWorkflowDefinitionDTOFromMap(
                jobParameters.getWorkflow().prepareDefinition() );
        final List< UserWFElement > jobWFElements = WorkflowDefinitionUtil.setWorkflowElements( jobParamDef );

        filePaths = getFilePathsFromWfJobParameters( jobWFElements );
        if ( !filePaths.isEmpty() ) {

            // base dir Path for file uploading
            String baseDirPath = jobParameters.getWorkingDir().getItems().get( 0 );

            WorkflowDefinitionUtil.copyWorkflowFilesZipToGivenPath( filePaths, jobParameters, baseDirPath );

            WorkflowDefinitionUtil.renameLocalPathsToStagingPath( jobWFElements, baseDirPath, jobParamDef, jobParameters );
        }
    }

    /**
     * Gets the file paths from wf job parameters.
     *
     * @param jobWFElements
     *         the job WF elements
     *
     * @return the file paths from wf job parameters
     */
    private Set< String > getFilePathsFromWfJobParameters( List< UserWFElement > jobWFElements ) {
        Set< String > filePaths = new HashSet<>();

        // get paths of all local files used in workflow
        for ( UserWFElement element : jobWFElements ) {
            final List< Field< ? > > elementFields = element.getFields();
            for ( final Field< ? > field : elementFields ) {
                if ( field.getType().equals( "os-file" ) && !field.isVariableMode() && field.getValue() != null ) {
                    Map< String, Object > map = ( Map< String, Object > ) field.getValue();
                    String path = map.get( ITEMS ).toString();
                    path = path.substring( 1, path.length() - 1 ); // Remove square brackets

                    if ( new File( path ).exists() ) {
                        filePaths.add( path );
                    }
                } else if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() ) && field.getValue() != null ) {
                    ScanFileDTO scanFile = JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), ScanFileDTO.class );

                    if ( new File( scanFile.getFile() ).exists() ) {
                        filePaths.add( scanFile.getFile() );
                    }
                } else if ( field.getType().equals( FieldTypes.TEMPLATE_FILE.getType() ) && field.getValue() != null ) {
                    TemplateFileDTO templateFile = JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), TemplateFileDTO.class );

                    if ( new File( templateFile.getFile() ).exists() ) {
                        filePaths.add( templateFile.getFile() );
                    }
                }
            }
        }

        return filePaths;
    }

    /**
     * Gets the directories paths from wf job parameters.
     *
     * @param jobWFElements
     *         the job WF elements
     *
     * @return the directories paths from wf job parameters
     */
    private Set< String > getDirectoriesPathsFromWfJobParameters( List< UserWFElement > jobWFElements ) {
        Set< String > dirPaths = new HashSet<>();

        // get paths of all local files used in workflow
        for ( UserWFElement element : jobWFElements ) {
            final List< Field< ? > > elementFields = element.getFields();
            for ( final Field< ? > field : elementFields ) {
                if ( field.getType().equals( "os-directory" ) && !field.isVariableMode() && field.getValue() != null ) {
                    Map< String, Object > map = ( Map< String, Object > ) field.getValue();
                    String path = map.get( ITEMS ).toString();
                    path = path.substring( 1, path.length() - 1 ); // Remove square brackets

                    if ( new File( path ).exists() ) {
                        dirPaths.add( path );
                    }
                }
            }
        }

        return dirPaths;
    }

    /**
     * Gets the staging path of server.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the staging path of server
     */
    private String getUserStagingPathOfServer( JobParameters jobParameters ) {
        String api = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort() + API_GET_LOCATION + "stagingpath";
        SusResponseDTO susresponseL = SuSClient.getRequest( api,
                WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
        return susresponseL.getData().toString();
    }

    /**
     * Gets location from job parameters.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the location
     */
    private LocationDTO getLocation( JobParameters jobParameters ) {
        final String url = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort() + "/api/job/isHost/" + jobParameters.getRunsOn();
        SusResponseDTO susResponseDTO = SuSClient.getRequest( url,
                WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
        ExecutionHosts host = JsonUtils.jsonToObject( JsonUtils.toJson( susResponseDTO.getData() ), ExecutionHosts.class );
        SusResponseDTO susResponseLocation = null;
        if ( null != host && null != host.getId() ) {
            susResponseLocation = SuSClient.getRequest(
                    jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                            + jobParameters.getServer().getPort() + API_GET_LOCATION + LocationsEnum.DEFAULT_LOCATION.getId(),
                    WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
        } else {
            String urlLocation = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                    + jobParameters.getServer().getPort() + API_GET_LOCATION + jobParameters.getRunsOn();
            susResponseLocation = SuSClient.getRequest( urlLocation,
                    WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
        }
        return JsonUtils.jsonToObject( JsonUtils.toJson( susResponseLocation.getData() ), LocationDTO.class );
    }

    /**
     * Validate local OS for local job.
     *
     * @param runsOn
     *         the runs on
     */
    private void validateLocalOSForLocalJob( String runsOn, String locationName ) {
        if ( OSValidator.isWindows() && !runsOn.contentEquals( LocationsEnum.LOCAL_WINDOWS.getId() ) ) {
            throw new SusException( MessagesUtil.getMessage( WFEMessages.JOB_NOT_RUN_ON_THIS_OS, "Windows" ) );
        }
        if ( !OSValidator.isWindows() && runsOn.contentEquals( LocationsEnum.LOCAL_WINDOWS.getId() ) ) {
            throw new SusException( MessagesUtil.getMessage( WFEMessages.JOB_NOT_RUN_ON_THIS_OS, locationName ) );
        }

    }

    /**
     * Validate workflow.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void validateWorkflow( JobParameters jobParameters ) {
        try {
            final LatestWorkFlowDTO workflowDto = getExecutionManager().getWorkflowDTOByIdAndVersionId( jobParameters );
            if ( CollectionUtils.isEmpty( ( Collection< ? > ) workflowDto.getElements().get( "nodes" ) ) ) {
                throw new SusException( MessagesUtil.getMessage( WFEMessages.WORKFLOW_HAS_NO_ELEMENTS ) );
            }
        } catch ( final Exception ex ) {
            throw new SusException( ex.getMessage() );
        }
    }

    /**
     * Validate rights.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void validateRights( JobParameters jobParameters ) {
        String licenseUrl = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort() + LICENSE_MANAGER_API;

        SusResponseDTO licenseCheckResponse = SuSClient.getRequest( licenseUrl,
                WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
        if ( !licenseCheckResponse.getSuccess() ) {
            throw new SusException( licenseCheckResponse.getMessage().getContent() );
        }

        String permUrl = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort() + PERMITTED_API;
        if ( jobParameters.getWorkflow().getId() != null ) {
            SusResponseDTO request = SuSClient.postRequest( permUrl,
                    jobParameters.getWorkflow().getId().toString() + COLON + PermissionMatrixEnum.EXECUTE.getValue(
                            DaemonPropertiesManager.hasTranslation(), jobParameters.getRequestHeaders().getToken() ),
                    WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );

            if ( !Boolean.parseBoolean( request.getData().toString() ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_EXECUTE.getKey(), jobParameters.getWorkflow().getName() ) );
            }
        }

        if ( !WorkflowDefinitionUtil.isLocal( jobParameters.getRunsOn() ) ) {

            SusResponseDTO locationResponse = SuSClient.getRequest(
                    jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                            + jobParameters.getServer().getPort() + "/api/system/location/" + jobParameters.getRunsOn(),
                    WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
            if ( null != locationResponse.getData() ) {
                LocationDTO locationDTO = JsonUtils.jsonToObject( JsonUtils.toJson( locationResponse.getData() ), LocationDTO.class );
                if ( locationDTO != null ) {
                    SusResponseDTO request = SuSClient.postRequest( permUrl,
                            locationDTO.getId() + ConstantsString.COLON + PermissionMatrixEnum.EXECUTE.getValue(
                                    DaemonPropertiesManager.hasTranslation(), jobParameters.getRequestHeaders().getToken() )
                                    + ConstantsString.COLON + IS_LOCATION,
                            WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
                    if ( !Boolean.parseBoolean( request.getData().toString() ) ) {
                        throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_EXECUTE.getKey(),
                                jobParameters.getWorkflow().getName() ) );
                    }
                }
            } else {
                final String url = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                        + jobParameters.getServer().getPort() + "/api/job/isHost/" + jobParameters.getRunsOn();
                SusResponseDTO susResponseDTO = SuSClient.getRequest( url,
                        WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
                ExecutionHosts host = JsonUtils.jsonToObject( JsonUtils.toJson( susResponseDTO.getData() ), ExecutionHosts.class );
                if ( null != host && null != host.getId() ) {
                    SusResponseDTO request = SuSClient.postRequest( permUrl,
                            LocationsEnum.DEFAULT_LOCATION.getId() + ConstantsString.COLON + PermissionMatrixEnum.EXECUTE.getValue(
                                    DaemonPropertiesManager.hasTranslation(), jobParameters.getRequestHeaders().getToken() )
                                    + ConstantsString.COLON + IS_LOCATION,
                            WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
                    if ( !Boolean.parseBoolean( request.getData().toString() ) ) {
                        throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_EXECUTE.getKey(),
                                jobParameters.getWorkflow().getName() ) );
                    }
                }
            }
        }
    }

    /**
     * Validate rights.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void validateSchemeRights( JobParameters jobParameters ) {
        String licenseUrl = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort() + SCHEME_LICENSE_API + jobParameters.getWorkflow().getId().toString();

        SusResponseDTO licenseCheckResponse = SuSClient.getRequest( licenseUrl,
                WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
        if ( !licenseCheckResponse.getSuccess() ) {
            throw new SusException( licenseCheckResponse.getMessage().getContent() );
        }

        String permUrl = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort() + PERMITTED_API;
        if ( jobParameters.getWorkflow().getId() != null ) {
            SusResponseDTO request = SuSClient.postRequest( permUrl,
                    jobParameters.getWorkflow().getId().toString() + COLON + PermissionMatrixEnum.EXECUTE.getValue(
                            DaemonPropertiesManager.hasTranslation(), jobParameters.getRequestHeaders().getToken() ),
                    WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );

            if ( !Boolean.parseBoolean( request.getData().toString() ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_EXECUTE.getKey(), jobParameters.getWorkflow().getName() ) );
            }
        }

        if ( !WorkflowDefinitionUtil.isLocal( jobParameters.getRunsOn() ) ) {

            SusResponseDTO locationResponse = SuSClient.getRequest(
                    jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                            + jobParameters.getServer().getPort() + "/api/system/location/" + jobParameters.getRunsOn(),
                    WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
            if ( null != locationResponse.getData() ) {
                LocationDTO locationDTO = JsonUtils.jsonToObject( JsonUtils.toJson( locationResponse.getData() ), LocationDTO.class );

                if ( locationDTO != null ) {
                    SusResponseDTO request = SuSClient.postRequest( permUrl,
                            locationDTO.getId() + ConstantsString.COLON + PermissionMatrixEnum.EXECUTE.getValue(
                                    DaemonPropertiesManager.hasTranslation(), jobParameters.getRequestHeaders().getToken() )
                                    + ConstantsString.COLON + IS_LOCATION,
                            WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
                    if ( !Boolean.parseBoolean( request.getData().toString() ) ) {
                        throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_EXECUTE.getKey(),
                                jobParameters.getWorkflow().getName() ) );
                    }
                }
            }
        }

    }

    /**
     * Submit server job.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void submitServerJob( JobParameters jobParameters ) {
        final String payload = JsonUtils.toFilteredJson( new String[]{}, jobParameters );
        String url = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort() + API_RUN_WORKFLOW;
        SuSClient.postRequest( url, payload, WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopJob( String authToken, String jobId, Job jobDto ) {

        boolean isStop = false;

        if ( jobDto.getJobType() == JobTypeEnums.SCHEME.getKey() ) {
            isStop = daemonExecutorService.stopSchemeJobExecution( UUID.fromString( jobId ), jobDto.getCreatedBy().getUserName() );
        } else {
            isStop = daemonExecutorService.stopJobExecution( UUID.fromString( jobId ), jobDto.getCreatedBy().getUserName() );
        }

        if ( !isStop ) {
            return false;
        }

        logger.debug( "Parent jobs List : " + JsonUtils.toJson( parentJob ) );

        List< UUID > childJobs = getJobChildrens( authToken, jobId );
        logger.debug( "stopping Master Job " );

        if ( childJobs != null && !childJobs.isEmpty() ) {
            childJobs.forEach( childId -> executionManager.stopJobExecution( childId.toString(), jobDto.getCreatedBy().getUserName() ) );
        }

        logger.debug( "Master Job Stop : " + isStop );
        logger.debug( "Parent jobs List after: " + JsonUtils.toJson( parentJob ) );
        updateStatusOnServer( authToken, UUID.fromString( jobId ) );
        return true;
    }

    /**
     * Gets the job childrens.
     *
     * @param authToken
     *         the auth token
     * @param jobId
     *         the job id
     *
     * @return the job childrens
     */
    private List< UUID > getJobChildrens( String authToken, String jobId ) {
        List< UUID > childrens = new ArrayList<>();
        try {
            String url = daemonManager.getServerAPIBase() + "job/" + jobId + "/childId";
            SusResponseDTO susresponse = SuSClient.getRequest( url, prepareHeadersForJob( authToken ) );
            if ( susresponse.getSuccess() ) {
                String json = JsonUtils.toJson( susresponse.getData() );
                childrens = JsonUtils.jsonToList( json, UUID.class );
            }
        } catch ( Exception e ) {
            logger.debug( e );
        }

        return childrens;
    }

    /**
     * Update status on server.
     *
     * @param authToken
     *         the auth token
     * @param jobId
     *         the job id
     */
    private void updateStatusOnServer( String authToken, UUID jobId ) {
        Job parent = parentJob.get( jobId );
        if ( parent == null ) {
            try {
                String url = daemonManager.getServerAPIBase() + "job/" + jobId.toString() + "/properties/data";
                SusResponseDTO susresponse = SuSClient.getRequest( url, prepareHeadersForJob( authToken ) );
                if ( susresponse.getSuccess() ) {
                    String json = JsonUtils.toJson( susresponse.getData() );
                    parent = JsonUtils.jsonToObject( json, Job.class );
                }
            } catch ( Exception e ) {
                logger.debug( e );
            }
        }
        parent.setStatus( new Status( WorkflowStatus.ABORTED ) );
        getExecutionManager().getFileJobsList().get( jobId ).setStatus( new Status( WorkflowStatus.ABORTED ) );

        final String payload = JsonUtils.toFilteredJson( new String[]{}, parent );

        try {
            String url = daemonManager.getServerAPIBase() + "workflow/job/" + jobId;
            SuSClient.putRequest( url, prepareHeadersForJob( authToken ), payload );
        } catch ( Exception e ) {
            logger.debug( e );
        }
    }

    /**
     * It adds headers for required for communication with server.<br>
     *
     * @param authToken
     *         the auth token
     *
     * @return requestHeaders
     */
    private Map< String, String > prepareHeadersForJob( String authToken ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, authToken );

        return requestHeaders;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreDestroy
    public boolean stopAllJobs() {
        return getExecutionManager().stopAllJobs( ConstantsString.EMPTY_STRING );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Job > getRunningJobs() {

        List< Job > returnList = new ArrayList<>();
        List< Job > jobList = new ArrayList<>( getExecutionManager().getFileJobsList().values() );

        if ( CollectionUtil.isNotEmpty( jobList ) ) {

            for ( Job job : jobList ) {
                if ( job.getStatus().getId() == WorkflowStatus.RUNNING.getKey()
                        || job.getStatus().getId() == WorkflowStatus.PAUSED.getKey() ) {
                    returnList.add( job );
                }
            }
        }

        return returnList;
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.manager.WorkflowDaemonManager#isLocalJobRunning()
     */
    public boolean isLocalJobRunning() {

        List< Job > jobList = new ArrayList<>( getExecutionManager().getFileJobsList().values() );

        if ( CollectionUtil.isNotEmpty( jobList ) ) {
            logger.debug( jobList.size() + ": running jobs :" + jobList );
            for ( Job job : jobList ) {
                if ( job.getRunMode() != null && WorkflowDefinitionUtil.isLocal( job.getRunMode() ) && (
                        job.getStatus().getId() == WorkflowStatus.RUNNING.getKey()
                                || job.getStatus().getId() == WorkflowStatus.PAUSED.getKey() ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Job > getAllJobs() {

        List< Job > returnList = new ArrayList<>();
        List< Job > jobList = new ArrayList<>( getExecutionManager().getFileJobsList().values() );

        if ( CollectionUtil.isNotEmpty( jobList ) ) {

            for ( Job job : jobList ) {

                returnList.add( job );
            }
        }

        return returnList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Job > getFileBaseJobs() {
        List< Job > fileBasedJobs = new ArrayList<>();
        if ( getExecutionManager().getFileJobsList() != null ) {

            for ( Job job : getExecutionManager().getFileJobsList().values() ) {
                if ( job.isFileRun() ) {
                    fileBasedJobs.add( job );
                }
            }

        }

        // to sort the list
        fileBasedJobs.sort( Comparator.comparing( Job::getSubmitTime, Comparator.nullsLast( Comparator.reverseOrder() ) ) );

        return fileBasedJobs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createWorkflowForm( String parentId ) {
        ImportWorkFlowDTO workflowDTO = new ImportWorkFlowDTO();
        workflowDTO.setParentId( UUID.fromString( parentId ) );

        List< UIFormItem > itemsUI = GUIUtils.prepareForm( true, workflowDTO );
        for ( UIFormItem uiFormItem : itemsUI ) {
            if ( uiFormItem.getName().equalsIgnoreCase( "file" ) ) {
                setRulesAndMessageOnUI( uiFormItem );
            }
        }
        return GUIUtils.createFormFromItems( itemsUI );
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
     * Gets the execution manager.
     *
     * @return the executionManager
     */
    public WFExecutionManager getExecutionManager() {
        if ( executionManager == null ) {
            executionManager = new WorkflowExecutionManagerImpl();
        }
        return executionManager;
    }

    /**
     * Sets the execution manager.
     *
     * @param executionManager
     *         the executionManager to set
     */
    public void setExecutionManager( WorkflowExecutionManagerImpl executionManager ) {
        this.executionManager = executionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean importWorkflow( String authToken, String objectJson ) {

        RequestHeaders requestHeaders = new RequestHeaders();
        requestHeaders.setToken( authToken );

        try {
            ImportWorkFlowDTO importWorkFlowDTO = JsonUtils.jsonToObject( objectJson, ImportWorkFlowDTO.class );
            JSONParser parser = new JSONParser();
            FileReader fileReader = new FileReader( ( String ) importWorkFlowDTO.getFile().getItems().get( FIRST_INDEX ) );
            Object obj = parser.parse( fileReader );
            fileReader.close();
            JSONObject jsonObject = ( JSONObject ) obj;
            LatestWorkFlowDTO latestWorkFlowDTO = JsonUtils.jsonToObject( jsonObject.toString(), LatestWorkFlowDTO.class );
            latestWorkFlowDTO.setName( importWorkFlowDTO.getName() );
            latestWorkFlowDTO.setDescription( importWorkFlowDTO.getDescription() );

            final String payload = JsonUtils.objectToJson( latestWorkFlowDTO );
            String url = daemonManager.getServerAPIBase() + WORKFLOW + importWorkFlowDTO.getParentId().toString();
            SuSClient.postRequest( url, payload, WorkflowDefinitionUtil.prepareHeaders( requestHeaders ) );

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return true;

    }

}
