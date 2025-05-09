package de.soco.software.simuspace.workflow.processing.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.dexecutor.core.task.ExecutionResults;
import com.github.dexecutor.core.task.Task;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.AuditTrailRelationType;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldModes;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTemplates;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.SchemeCategoryEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.FileObject;
import de.soco.software.simuspace.suscore.common.model.MatchedLine;
import de.soco.software.simuspace.suscore.common.model.ObjectVariableDTO;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.model.ScanResponseDTO;
import de.soco.software.simuspace.suscore.common.model.SchemeSummaryResults;
import de.soco.software.simuspace.suscore.common.model.TemplateFileDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateScanDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateVariableDTO;
import de.soco.software.simuspace.suscore.common.model.UserConfigFile;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.ScanObjectDTO;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.ExecutionHosts;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LoggerUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.common.util.RegexUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.common.util.ScanObjectUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.suscore.data.common.model.AdditionalFiles;
import de.soco.software.simuspace.suscore.data.common.model.ParserVariableDTO;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.workflow.constant.ConstantsElementKey;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsScriptType;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.dexecutor.ExecutorServiceManager;
import de.soco.software.simuspace.workflow.dexecutor.WorkFlowTaskProvider;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.dto.WorkflowModel;
import de.soco.software.simuspace.workflow.main.WFApplication;
import de.soco.software.simuspace.workflow.model.ElementConfig;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.WorkflowConfiguration;
import de.soco.software.simuspace.workflow.model.WorkflowExecutionParameters;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.NodeEdge;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.SectionWorkflow;
import de.soco.software.simuspace.workflow.model.impl.UserImpl;
import de.soco.software.simuspace.workflow.model.impl.UserWFElementImpl;
import de.soco.software.simuspace.workflow.model.impl.UserWorkflowImpl;
import de.soco.software.simuspace.workflow.model.impl.WorkflowConfigElements;
import de.soco.software.simuspace.workflow.model.impl.WorkflowConfigurationImpl;
import de.soco.software.simuspace.workflow.processing.WFExecutionManager;
import de.soco.software.simuspace.workflow.properties.EnginePropertiesManager;
import de.soco.software.simuspace.workflow.util.Diagraph;
import de.soco.software.simuspace.workflow.util.JobLog;
import de.soco.software.simuspace.workflow.util.WFLoopsUtils;

/**
 * This Class is an entry point in work flow execution takes two args , one is user workflow Js. and configuration validate the files
 * execute commands in scripts
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class WorkflowExecutionManagerImpl implements WFExecutionManager {

    /**
     * The Constant MAX_INT.
     */
    private static final String MAX_INT = String.valueOf( Integer.MAX_VALUE );

    /**
     * The Constant DESIGN_SUMMARY_PAYLOAD.
     */
    private static final String DESIGN_SUMMARY_PAYLOAD = "{\"draw\":2,\"start\":0,\"length\":" + MAX_INT
            + ",\"search\":\"\",\"columns\":[]}";

    /**
     * The Constant DATA_SELECTION.
     */
    private static final String DATA_SELECTION = "selection/";

    /**
     * The Constant API_GET_LOCATION.
     */
    private static final String API_GET_LOCATION = "/api/system/location/";

    /**
     * The Constant SUBWORKFLOW.
     */
    private static final String SUBWORKFLOW = "subworkflow";

    /**
     * The Constant EMPTY_JSON.
     */
    public static final String EMPTY_JSON = "{}";

    /**
     * The Constant WORKING_DIRECTORY.
     */
    private static final String WORKING_DIRECTORY = "Working directory: ";

    /**
     * The Constant ELEMENT_IS_NOT_OF_CONFIGURED_TYPES.
     */
    private static final String ELEMENT_IS_NOT_OF_CONFIGURED_TYPES = "Element key %s is not of configured types.";

    /**
     * The Constant ERROR.
     */
    private static final String ERROR = "Error ";

    /**
     * The Constant GIT_VERSION.
     */
    private static final String GIT_VERSION = "git --version";

    /**
     * The Constant GIT_VERSION_STRING.
     */
    private static final String GIT_VERSION_STRING = "git version";

    /**
     * The properties.
     */
    private static Properties properties = new Properties();

    /**
     * The Constant URL_GET_CONFIG.
     */
    private static final String URL_GET_CONFIG = "URL_GET_CONFIG";

    /**
     * The Constant URL_GET_HPC_CONFIG.
     */
    private static final String URL_GET_HPC_CONFIG = "URL_GET_HPC_CONFIG";

    /**
     * The Constant URL_GET_WF_BY_ID.
     */
    private static final String URL_GET_WF_BY_ID = "URL_GET_WF_BY_ID";

    /**
     * The Constant URL_SAVE_JOB.
     */
    private static final String URL_SAVE_JOB = "URL_SAVE_JOB";

    /**
     * The Constant GET_JOB_BY_ID.
     */
    private static final String GET_JOB_BY_ID = "GET_JOB_BY_ID";

    /**
     * The Constant URL_UPDATE_JOB_BY_ID.
     */
    private static final String URL_UPDATE_JOB_BY_ID = "URL_UPDATE_JOB_BY_ID";

    /**
     * The Constant UPDATE_LOG_OF_JOB.
     */
    private static final String UPDATE_LOG_OF_JOB = "UPDATE_LOG_OF_JOB";

    /**
     * The Constant BUNDLE_VERSION.
     */
    private static final String BUNDLE_VERSION = "Bundle-Version";

    /**
     * The Constant USER_AGENT.
     */
    private static final String USER_AGENT = "USER_AGENT";

    /**
     * The Constant VERSION_KEY_FOR_URL.
     */
    private static final String VERSION_KEY_FOR_URL = "/version/";

    /**
     * The Constant WIN_C_PARAM.
     */
    private static final String WIN_C_PARAM = "/c";

    /**
     * The Constant WIN_EXE.
     */
    private static final String WIN_EXE = "cmd.exe";

    /**
     * The Constant USER_MACHINE_NAME.
     */
    private static final String USER_MACHINE_NAME = System.getProperty( "user.name" );

    /**
     * The Constant PYTHON_VERSION_STRING.
     */
    private static final String PYTHON_VERSION_STRING = "Python ";

    /**
     * The Constant VERSION.
     */
    private static final String VERSION = " --version";

    /**
     * The Constant UNABLE_TO_WRITE_FILE.
     */
    private static final String UNABLE_TO_WRITE_FILE = "Unable to write file : ";

    /**
     * The Constant API_CURRENT_USER.
     */
    private static final String API_CURRENT_USER = "/api/system/user/current";

    /**
     * The Constant PYTHON_VERSION.
     */
    private static final String PYTHON_VERSION = "Python version : ";

    /**
     * The Constant JOB_NAME.
     */
    private static final String JOB_NAME = "{{Job.Name}}";

    /**
     * The constant JOB_PYTHON.
     */
    private static final String JOB_PYTHON = "{{Job.Python}}";

    /**
     * The Constant JOB_EXECUTION.
     */
    private static final String JOB_EXECUTION = "{{Job.ExecOn}}";

    /**
     * The Constant JOB_WORKING_DIR.
     */
    private static final String JOB_WORKING_DIR = "{{Job.WorkingDir}}";

    /**
     * The Constant JOB_SYSTEMOUTPUT_PATH.
     */
    private static final String JOB_SYSTEMOUTPUT_PATH = "{{Job.Systemoutput}}";

    /**
     * The Constant EXPERIMENT_NUMBER.
     */
    private static final String EXPERIMENT_NUMBER = "{{Exp.No}}";

    /**
     * The Constant JOB_DESCRIPTION.
     */
    private static final String JOB_DESCRIPTION = "{{Job.Description}}";

    /**
     * The Constant JOB_TOKEN.
     */
    private static final String JOB_TOKEN = "{{Job.Token}}";

    /**
     * The Constant USER_UID.
     */
    private static final String USER_UID = "{{UserUID}}";

    /**
     * The Constant JOB_ID.
     */
    private static final String JOB_ID = "{{Job.ID}}";

    /**
     * The Constant SHOW_DIR.
     */
    private static final String SHOW_DIR = "all";

    /**
     * The Constant REGEX.
     */
    public static final String REGEX = "^(.*)([a-z]*)$";

    /**
     * The Constant FLAG_I.
     */
    private static final String FLAG_I = "i";

    /**
     * The Constant SHAPE_MODULE_KEY.
     */
    private static final String SHAPE_MODULE_KEY = "ShapeModule";

    /**
     * The Constant SHAPE_MODULE_VARIABLE.
     */
    private static final String SHAPEMODULE_VARIABLE = "{{Job.ShapeModule}}";

    /**
     * The wf logger.
     */
    private WfLogger wfLogger;

    /**
     * The wf logger sum.
     */
    private WfLogger wfLoggerSum;

    /**
     * The Constant jobsList.
     */
    private static final ConcurrentHashMap< UUID, Job > jobsList = new ConcurrentHashMap<>();

    /**
     * The Constant jobsMap.
     */
    private static final Map< UUID, ExecutorServiceManager > jobsMap = new HashMap<>();

    /**
     * Instantiates a new workflow execution manager impl.
     */
    public WorkflowExecutionManagerImpl() {

    }

    /**
     * Check python on os.
     *
     * @param path
     *         the path
     * @param jobId
     *         the job id
     *
     * @return true, if successful
     */
    private static boolean checkPythonOnOS( String path, UUID jobId ) {
        InputStream stdout;
        InputStream stdError;
        final StringBuilder out = new StringBuilder();
        final StringBuilder error = new StringBuilder();
        String line;
        String[] cmdArray = null;
        if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_WINDOWS ) ) {
            cmdArray = new String[]{ ConstantsString.WIN_EXE, ConstantsString.WIN_C_PARAM, "\"" + path + "\"" + VERSION };
        } else if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
            cmdArray = new String[]{ ConstantsString.NIX_SHELL, ConstantsString.NIX_C_PARAM, path + VERSION };
        }
        Process process;
        try {
            process = Runtime.getRuntime().exec( cmdArray );
            stdout = process.getInputStream();
            stdError = process.getErrorStream();

            try ( InputStreamReader isr = new InputStreamReader( stdError );
                    final BufferedReader brCleanUpError = new BufferedReader( isr ) ) {
                while ( ( line = brCleanUpError.readLine() ) != null ) {
                    error.append( line + ConstantsString.NEW_LINE );
                }
            }

            // clean up if any output in stdout
            try ( InputStreamReader isr = new InputStreamReader( stdout ); final BufferedReader brCleanUpOut = new BufferedReader( isr ) ) {
                while ( ( line = brCleanUpOut.readLine() ) != null ) {
                    out.append( line + ConstantsString.NEW_LINE );
                }
            }

            process.waitFor();
            stdout.close();
            stdError.close();
            process.destroy();
        } catch ( InterruptedException e ) {
            log.warn( MessageBundleFactory.getDefaultMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
            Thread.currentThread().interrupt();
        } catch ( Exception e ) {
            log.error( UNABLE_TO_WRITE_FILE, e );
        }

        if ( out.toString().startsWith( PYTHON_VERSION_STRING ) ) {
            JobLog.addLog( jobId, new LogRecord( ConstantsMessageTypes.INFO, PYTHON_VERSION + out ) );
        } else if ( error.toString().startsWith( PYTHON_VERSION_STRING ) ) {
            JobLog.addLog( jobId, new LogRecord( ConstantsMessageTypes.INFO, PYTHON_VERSION + error ) );
        }

        return out.toString().startsWith( PYTHON_VERSION_STRING ) || error.toString().startsWith( PYTHON_VERSION_STRING );
    }

    /**
     * Adds the virtual node.
     *
     * @param inputIds
     *         the input ids
     * @param startingId
     *         the starting id
     * @param graph
     *         the graph
     *
     * @return the diagraph
     */
    private Diagraph< String > addVirtualNode( List< String > inputIds, final String startingId, Diagraph< String > graph ) {
        if ( CollectionUtil.isNotEmpty( inputIds ) ) {
            for ( final String id : inputIds ) {
                if ( !id.equals( startingId ) ) {
                    graph.add( startingId, id );
                }
            }
        }
        return graph;

    }

    /**
     * Gets the file list.
     *
     * @param path
     *         the path
     * @param show
     *         the show
     *
     * @return the file list
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public List< FileObject > getFileList( String path, String show ) throws IOException {
        return de.soco.software.simuspace.suscore.common.util.FileUtils.getFilelist( path, !show.equals( SHOW_DIR ) );
    }

    /**
     * Gets the user home.
     *
     * @return the user home
     */
    public String getUserHome() {
        return System.getProperty( ConstantsString.HOME_DIRECTORY );
    }

    /**
     * Check bash on window.
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *         the interrupted exception
     */
    public boolean checkBashOnWindow() throws IOException, InterruptedException {
        InputStream stdout;
        final StringBuilder out = new StringBuilder();
        String line;
        String[] cmdArray;
        cmdArray = new String[]{ WIN_EXE, WIN_C_PARAM, GIT_VERSION, ConstantsString.OS_WINDOWS };
        final Process process = Runtime.getRuntime().exec( cmdArray );
        stdout = process.getInputStream();
        // clean up if any output in stdout
        try ( InputStreamReader isr = new InputStreamReader( stdout, ConstantsString.UTF8 );
                final BufferedReader brCleanUp = new BufferedReader( isr ) ) {
            while ( ( line = brCleanUp.readLine() ) != null ) {
                out.append( line + ConstantsString.NEW_LINE );
            }
        }
        process.waitFor();
        stdout.close();
        process.destroy();
        return out.toString().startsWith( GIT_VERSION_STRING );
    }

    /**
     * Convert workflow dto to user workflow.
     *
     * @param workflowDto
     *         the workflow dto
     *
     * @return the user work flow
     */
    public UserWorkFlow convertWorkflowDtoToUserWorkflow( LatestWorkFlowDTO workflowDto ) {

        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workflowDto );
        writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.GET_WORKFLOW_COMPLETE ) );

        return userWorkFlow;
    }

    /**
     * Delete job temp folder.
     *
     * @param jobImpl
     *         the job impl
     */
    public void deleteJobTempFolder( Job jobImpl ) {
        try {
            final String temJobDir =
                    jobImpl.getWorkingDir().getPath() + File.separator + ConstantsString.DOT + jobImpl.getName() + ConstantsString.DOT
                            + jobImpl.getId();
            writeLogs( Level.FINE, "Script dir path going to delete: " + temJobDir );
            FileUtils.deleteDirectory( new File( temJobDir ) );
            final boolean deleted = Files.deleteIfExists( Paths.get( temJobDir ) );
            writeLogs( Level.FINE, "Script dir path deleted: " + deleted );
        } catch ( final IOException e ) {
            log.error( ERROR, e );
        }

    }

    /**
     * Execute user WF new.
     *
     * @param userWorkFlow
     *         the user work flow
     * @param jobImpl
     *         the job impl
     * @param executedElementsIds
     *         the executed elements ids
     * @param parameters
     *         the parameters
     * @param workflowExecutionParameters
     *         the workflow execution parameters
     * @param pythonExecutionPath
     *         the python execution path
     *
     * @return the execution results
     */
    public ExecutionResults< String, DecisionObject > executeUserWF( UserWorkFlow userWorkFlow, Job jobImpl,
            Set< String > executedElementsIds, Map< String, Object > parameters, WorkflowExecutionParameters workflowExecutionParameters,
            String pythonExecutionPath ) {
        final Diagraph< String > graph = getGraph( userWorkFlow.getEdges(), userWorkFlow.getNodes(), workflowExecutionParameters );
        final String startingId = getStartingNodeId( graph );
        final List< String > multipleInputs = getMultipleStartingIds( graph, startingId );
        final Diagraph< String > newGraph = addVirtualNode( multipleInputs, startingId, graph );
        if ( !jobImpl.isFileRun() ) {
            JobLog.addLog( jobImpl.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    "User: " + USER_MACHINE_NAME + " : Executing job on machine : " + jobImpl.getMachine() ) );
            JobLog.addLog( jobImpl.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    "Workflow name: " + jobImpl.getWorkflow().getName() + " and Version: " + jobImpl.getWorkflowVersion().getId()
                            + " and Workflow Id : " + jobImpl.getWorkflowId() ) );
        }
        final List< Task< String, DecisionObject > > userWFElements = new ArrayList<>();
        performAction( userWorkFlow, userWorkFlow.getNodes(), newGraph, startingId, jobImpl, userWFElements, pythonExecutionPath,
                parameters, null, executedElementsIds, workflowExecutionParameters );
        return executeWorkByDexecutor( jobImpl, userWorkFlow.getEdges(), userWFElements, workflowExecutionParameters );
    }

    /**
     * Execute section workflow.
     *
     * @param sectionWorkFlow
     *         the section work flow
     * @param workflowExecutionParameters
     *         the workflow execution parameters
     * @param additionalParameters
     *         the additional parameters
     * @param userWorkFlow
     *         the user work flow
     *
     * @return the execution results
     */
    @Override
    public ExecutionResults< String, DecisionObject > executeSectionWorkflow( SectionWorkflow sectionWorkFlow,
            WorkflowExecutionParameters workflowExecutionParameters, Map< String, Object > additionalParameters,
            UserWorkFlow userWorkFlow ) {
        final Diagraph< String > graph = getGraph( sectionWorkFlow.getEdges(), sectionWorkFlow.getNodes(), workflowExecutionParameters );
        final String startingId = getStartingNodeId( graph );
        final List< String > multipleInputs = getMultipleStartingIds( graph, startingId );
        final Diagraph< String > newGraph = addVirtualNode( multipleInputs, startingId, graph );
        final List< Task< String, DecisionObject > > userWFElements = new ArrayList<>();
        performAction( userWorkFlow, sectionWorkFlow.getNodes(), newGraph, startingId, workflowExecutionParameters.getJob(), userWFElements,
                workflowExecutionParameters.getPythonExecutionPath(), additionalParameters, null,
                workflowExecutionParameters.getExecutedElementsIds(), workflowExecutionParameters );
        return executeWorkByDexecutor( workflowExecutionParameters.getJob(), sectionWorkFlow.getEdges(), userWFElements,
                workflowExecutionParameters );
    }

    /**
     * Execute work by dexecutor.
     *
     * @param jobImpl
     *         the job impl
     * @param connections
     *         the connections
     * @param userWFElements
     *         the user WF elements
     * @param workflowExecutionParameters
     *         the workflow execution parameters
     *
     * @return the execution results
     */
    private ExecutionResults< String, DecisionObject > executeWorkByDexecutor( Job jobImpl, List< NodeEdge > connections,
            List< Task< String, DecisionObject > > userWFElements, WorkflowExecutionParameters workflowExecutionParameters ) {
        final WorkFlowTaskProvider p = new WorkFlowTaskProvider();
        p.addTasks( userWFElements );
        final ExecutorServiceManager executorServiceManager = new ExecutorServiceManager( connections, p, workflowExecutionParameters );
        jobsMap.put( jobImpl.getId(), executorServiceManager );
        writeLogs( Level.FINE, "JOBS MAPS:: " + jobsMap.size() );
        return executorServiceManager.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, Object > setSubworkflowParams( UUID jobId ) {
        ExecutorServiceManager service = jobsMap.get( jobId );
        return service.getSubWorkflowParams();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeWorkflow( JobParameters jobParameters ) {
        Job job = null;
        int totalElements = ConstantsInteger.INTEGER_VALUE_ZERO;
        Set< String > executedElementsIds = new HashSet<>();
        final RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        // Extract the PID by splitting the string returned
        final long pid = Long.parseLong( bean.getName().split( "@" )[ 0 ] );
        try {
            // Get Workflow by id and version to check the status of workflow
            final LatestWorkFlowDTO workflowDto = getWorkflowDTOByIdAndVersionId( jobParameters );
            if ( CollectionUtils.isEmpty( ( Collection< ? > ) workflowDto.getElements().get( "nodes" ) ) ) {
                throw new SusException( MessagesUtil.getMessage( WFEMessages.WORKFLOW_HAS_NO_ELEMENTS ) );
            }
            saveRelationInCaseOfSchemeOptimization( jobParameters );
            job = prepareJob( jobParameters );
            JobLog.setJob( job );
            job.setJobType( jobParameters.getJobType() );
            job.setJobRelationType( jobParameters.getJobRelationType() );
            job.setJobSchemeCategory( jobParameters.getJobSchemeCategory() );
            job.setJobProcessId( String.valueOf( pid ) );
            prepareJobImpl( jobParameters, totalElements, job );
            prepareJobPath( jobParameters, job );
            deleteJobJson( job );
            // saving relation
            writeLogs( Level.INFO, "creating relation" );
            saveRelationByExtractingNodes( jobParameters, job );
            System.setProperty( "user.dir", jobParameters.getWorkingDir().getPath() );
            if ( !jobParameters.isFileRun() ) {
                updateJob( job );
            }
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessagesUtil.getMessage( WFEMessages.WORKING_DIRECTORY, job.getWorkingDir().getPath() ) ) );
            jobParameters.getServer().setRequestHeaders( jobParameters.getRequestHeaders() );
            if ( !workflowDto.isExecutable() ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_NOT_EXECUTEABLE ) ),
                        WorkflowExecutionManagerImpl.class );
            }
            if ( workflowDto.getLifeCycleStatus() != null ) {
                writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_STATUS, workflowDto.getLifeCycleStatus().getName() ) );
            }
            // Full Job log and Summary job log Header start

            final String summaryLogFilePath = LoggerUtils.addFileAppenderToLogger( jobParameters.getName() + "_Summary",
                    jobParameters.getWorkingDir().getPath(), ConstantsString.WF_LOGGER_SUMMARY );
            final String generalLogFilePath = LoggerUtils.addFileAppenderToLogger( jobParameters.getName(),
                    jobParameters.getWorkingDir().getPath(), ConstantsString.WF_LOGGER );
            job.setLogPath( generalLogFilePath );
            job.setLogGeneralFilePath( generalLogFilePath );
            job.setLogSummaryFilePath( summaryLogFilePath );
            prepareLogFileHeader( jobParameters, workflowDto );
            LoggerUtils.setFormatterToLogger( ConstantsString.WF_LOGGER_SUMMARY, LoggerUtils.FORMAT );
            LoggerUtils.setFormatterToLogger( ConstantsString.WF_LOGGER, LoggerUtils.FORMAT );
            writeLogs( Level.INFO, WORKING_DIRECTORY + jobParameters.getWorkingDir().getPath() );
            writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.GET_WORKFLOW_FROM_ID, jobParameters.getWorkflow().getId() ) );
            final WorkflowConfiguration config = getConfig( jobParameters );
            final UserWorkFlow userWorkFlow = convertWorkflowDtoToUserWorkflow( workflowDto );
            userWorkFlow.setDirectory( jobParameters.getWorkingDir().getPath() );
            validateConfig( config );
            validateWorkflow( userWorkFlow, config );
            totalElements = userWorkFlow.getNodes().size();
            job.setProgress( new ProgressBar( totalElements ) );
            final Map< String, Map< String, Object > > allJOBIOParameters = validateJobDefinationsOverWorkflowDefination( jobParameters,
                    userWorkFlow, null );
            job.setIOParameters( allJOBIOParameters );
            // server file selection list print in output file in job dir
            // 1 jobParameters.getWorkflow().getElements()
            // 2 jobParameters.getServer()
            // 3 jobParameters.getRequestHeaders()
            // 4 jobParameters.getId()
            // 5 jobParameters.getGlobalVariables()
            Map< String, Object > outIt = getServerFilesSelectionMap( jobParameters.getWorkflow().getElements(), jobParameters.getServer(),
                    jobParameters.getRequestHeaders(), jobParameters.getId(), jobParameters.getGlobalVariables() );
            final File outputFile = new File(
                    jobParameters.getWorkingDir().getPath() + File.separator + jobParameters.getName() + ".systemoutput.json" );
            if ( !outputFile.exists() ) {
                Files.createFile( Paths.get(
                        jobParameters.getWorkingDir().getPath() + File.separator + jobParameters.getName() + ".systemoutput.json" ) );

            }
            try ( final FileWriter fileWriter = new FileWriter( outputFile, false ) ) {
                fileWriter.write( ( outIt == null ? EMPTY_JSON : JsonUtils.toJson( outIt ) ) );
                fileWriter.flush();
            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
            }
            job.setElementOutput( outputFile );
            job.setGlobalVariables( jobParameters.getGlobalVariables() );
            job.getWorkflow().setWorkflowType( jobParameters.getWorkflow().getWorkflowType() );
            job.setLogPath( generalLogFilePath );
            job.setLogGeneralFilePath( generalLogFilePath );
            job.setLogSummaryFilePath( summaryLogFilePath );
            jobsList.putIfAbsent( job.getId(), job );
            String updatedElements = replaceSelectValueFromElements( JsonUtils.toJson( userWorkFlow ) );
            UserWorkflowImpl userWorkflowImpl = JsonUtils.jsonToObject( updatedElements, UserWorkflowImpl.class );
            final Job jobPrep = prepareJobImplforDeExecutor( job );
            runWorkflow( jobPrep, userWorkflowImpl, executedElementsIds );
        } catch ( Exception e ) {
            writeLogs( Level.SEVERE, e.getLocalizedMessage() );
            if ( job != null ) {
                updateJobLogAndStatusToFail( job, e.getMessage() );
            }
            removeAllAppenders();
            try {
                log.error( JsonUtils.toFilteredJson( new String[]{}, new SusResponseDTO( Boolean.FALSE, e.getMessage() ) ) );
            } catch ( final JsonSerializationException e1 ) {
                log.error( ERROR, e1 );
                writeLogs( Level.SEVERE, e1.getLocalizedMessage() );
            }
            log.error( ERROR, e );
            if ( e instanceof InterruptedException ) {
                Thread.currentThread().interrupt();
            }
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Replace select value from elements.
     *
     * @param jobParJson
     *         the job par json
     *
     * @return the string
     */
    public String replaceSelectValueFromElements( String jobParJson ) {
        JSONObject jsonObj = new JSONObject( jobParJson );
        JSONArray nodes = ( JSONArray ) jsonObj.get( "nodes" );
        for ( int i = 0; i < nodes.length(); i++ ) {
            JSONObject nodeDetails = ( JSONObject ) nodes.get( i );

            if ( nodeDetails.get( "key" ).toString().equalsIgnoreCase( "wfe_io" ) ) {

                JSONArray feildsList = ( JSONArray ) nodeDetails.get( "fields" );
                for ( int j = 0; j < feildsList.length(); j++ ) {
                    JSONObject select = ( JSONObject ) feildsList.get( j );
                    if ( select.get( "type" ).toString().equalsIgnoreCase( "select" ) ) {
                        String stripedString = select.get( "value" ).toString().replace( "[", "" ).replace( "]", "" ).replace( "\"", "" );
                        select.remove( "value" );
                        select.put( "value", stripedString );
                    }
                }
            }
        }
        return jsonObj.toString();
    }

    /**
     * Gets the server files selection map.
     *
     * @param elements
     *         the elements
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     * @param jobId
     *         the job id
     * @param globalVariables
     *         the global variables
     *
     * @return the server files selection map
     */
    public Map< String, Object > getServerFilesSelectionMap( Map< String, Object > elements, RestAPI server, RequestHeaders requestHeaders,
            String jobId, Map< String, Object > globalVariables ) {
        WorkflowModel workflowModel = JsonUtils.jsonToObject( JsonUtils.toJson( elements ), WorkflowModel.class );
        Map< String, Object > mainMap = new HashMap<>();

        for ( WorkflowElement element : workflowModel.getNodes() ) {
            if ( element.getData().getKey().equals( "wfe_io" ) ) {
                Map< String, Object > ioMap = new HashMap<>();
                for ( Field< ? > f : element.getData().getFields() ) {
                    getElementFields( server, requestHeaders, jobId, ioMap, f );
                }
                mainMap.put( element.getData().getName(), ioMap );
            }
        }
        addGlobalVariablesToOutput( mainMap, globalVariables );
        return mainMap;
    }

    /**
     * Gets the element fields.
     *
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     * @param jobId
     *         the job id
     * @param ioMap
     *         the io map
     * @param field
     *         the field
     */
    private void getElementFields( RestAPI server, RequestHeaders requestHeaders, String jobId, Map< String, Object > ioMap, Field field ) {
        List< Object > fieldDetailsList = new ArrayList<>();
        if ( ( field.getType().equals( FieldTypes.INTEGER.getType() ) || field.getType().equals( FieldTypes.FLOAT.getType() )
                || field.getType().equals( FieldTypes.BOOLEAN.getType() ) || field.getType().equals( FieldTypes.TEXT.getType() )
                || field.getType().equals( FieldTypes.SELECTION.getType() ) || field.getType().equals( FieldTypes.TEXTAREA.getType() )
                || field.getType().equals( FieldTypes.COLOR.getType() ) || field.getType().equals( FieldTypes.INPUT_TABLE.getType() )
                || field.getType().equals( FieldTypes.SECTION.getType() ) || field.getType().equals( FieldTypes.DATE.getType() )
                || field.getType().equals( FieldTypes.DATE_RANGE.getType() ) ) && field.getValue() != null && !field.getValue()
                .equals( ConstantsString.EMPTY_STRING ) ) {
            String fieldValue = JsonUtils.toJson( field.getValue() );
            if ( fieldValue.startsWith( ConstantsString.DOUBLE_QUOTE_STRING ) ) {
                ioMap.put( field.getName(), field.getValue().toString() );
            } else {
                ioMap.put( field.getName(), field.getValue() );
            }
        }
        if ( field.getType().equals( FieldTypes.CB2_OBJECTS.getType() ) && field.getValue() != null && !field.getValue()
                .equals( ConstantsString.EMPTY_STRING ) ) {
            String url = server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort()
                    + "/api/workflow/cb2/entity/selection/" + field.getValue();
            SusResponseDTO susResponse = SuSClient.getRequest( url, prepareHeaders( requestHeaders ) );
            ioMap.put( field.getName(), susResponse.getData() );
        } else if ( field.getType().equals( FieldTypes.FILE.getType() ) || field.getType().equals( FieldTypes.DIRECTORY.getType() ) ) {
            if ( field.getValue() != null && !field.getValue().equals( ConstantsString.EMPTY_STRING ) ) {
                String file;
                if ( field.isVariableMode() ) {
                    file = ( String ) field.getValue();
                } else {
                    LinkedHashMap< String, List< String > > map = ( LinkedHashMap< String, List< String > > ) field.getValue();
                    file = map.get( "items" ).get( ConstantsInteger.INTEGER_VALUE_ZERO );
                }
                Map< String, String > fieldProperties = new HashMap<>();
                fieldProperties.put( "name",
                        file.substring( ( file.lastIndexOf( File.separator ) ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
                fieldProperties.put( "path", file );
                fieldDetailsList.add( fieldProperties );
                ioMap.put( field.getName(), fieldDetailsList );
            }
        } else if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() ) || field.getType()
                .equals( FieldTypes.TEMPLATE_FILE.getType() ) || field.getType().equals( FieldTypes.OBJECT_PARSER.getType() ) ) {
            if ( field.getTemplateType().equalsIgnoreCase( FieldTemplates.CUSTOM_VARIABLE.getValue() ) ) {
                ioMap.put( field.getName(), field.getValue() );
            } else if ( field.getValue() instanceof LinkedHashMap ) {
                LinkedHashMap< String, String > map = ( LinkedHashMap< String, String > ) field.getValue();
                String file = map.get( "file" );
                String name = file.substring( ( file.lastIndexOf( File.separator ) ) + ConstantsInteger.INTEGER_VALUE_ONE );
                Map< String, String > fieldProperties = new HashMap<>();
                fieldProperties.put( "name", name );
                fieldProperties.put( "path", file.substring( ConstantsInteger.INTEGER_VALUE_ZERO,
                        nthLastIndexOf( ConstantsInteger.INTEGER_VALUE_THREE, ConstantsString.FORWARD_SLASH, file )
                                + ConstantsInteger.INTEGER_VALUE_ONE ) + jobId + ConstantsString.FORWARD_SLASH + name );
                fieldDetailsList.add( fieldProperties );
                ioMap.put( field.getName(), fieldDetailsList );
            } else {
                String selectionId = ( String ) field.getValue();
                final String selectionURL = prepareURL( "/api/workflow/parser/" + selectionId + "/filepath", server );
                final SusResponseDTO responseDTO = SuSClient.getRequest( selectionURL, prepareHeaders( requestHeaders ) );
                String filePath = ( String ) responseDTO.getData();
                Map< String, String > fieldProperties = new HashMap<>();
                fieldProperties.put( "name", new File( filePath ).getName() );
                fieldProperties.put( "path", new File( filePath ).getPath() );
                fieldDetailsList.add( fieldProperties );
                ioMap.put( field.getName(), fieldDetailsList );
            }
        } else if ( field.getType().equals( FieldTypes.OBJECT.getType() ) && field.getValue() != null
                && !field.getValue().equals( ConstantsString.EMPTY_STRING ) ) {
            final SusResponseDTO responseDTO = getSelectionList( server, requestHeaders, field.getValue().toString() );
            if ( responseDTO != null ) {
                FilteredResponse fr = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), FilteredResponse.class );
                ioMap.put( field.getName(), fr.getData() );
            }
        } else if ( field.getType().equals( FieldTypes.SERVER_FILE_EXPLORER.getType() ) && field.getValue() != null && !field.getValue()
                .equals( ConstantsString.EMPTY_STRING ) ) {
            if ( field.isVariableMode() ) {
                ioMap.put( field.getName(), field.getValue().toString() );
            } else {
                final SusResponseDTO responseDTO = getSelectionList( server, requestHeaders, field.getValue().toString() );
                if ( responseDTO != null ) {
                    FilteredResponse fr = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), FilteredResponse.class );
                    ioMap.put( field.getName(), fr.getData() );
                }
            }
        }
    }

    /**
     * Gets the selection list.
     *
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     * @param field
     *         the field
     *
     * @return the selection list
     */
    private SusResponseDTO getSelectionList( RestAPI server, RequestHeaders requestHeaders, String value ) {
        FiltersDTO filter = new FiltersDTO();
        filter.setDraw( 1 );
        filter.setStart( 0 );
        filter.setLength( Integer.MAX_VALUE );
        final String selectionURL = prepareURL( "/api/selection/" + value + "/list", server );
        return SuSClient.postRequest( selectionURL, JsonUtils.objectToJson( filter ), prepareHeaders( requestHeaders ) );
    }

    /**
     * Update field list for server file.
     *
     * @param fieldDetailsList
     *         the field details list
     * @param data
     *         the data
     *
     * @return the list
     */
    private List< Object > updateFieldListForServerFile( List< Object > fieldDetailsList, String data ) {
        List< AdditionalFiles > additionalFilesList = new ArrayList<>();
        try {
            additionalFilesList = JsonUtils.jsonToList( data, AdditionalFiles.class );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            writeLogs( Level.INFO, "Server details write error for system output file" + e.getMessage() );
        }
        for ( AdditionalFiles additionalFiles : additionalFilesList ) {
            Map< String, String > fieldProperties = new HashMap<>();
            fieldProperties.put( "name", additionalFiles.getTitle() );
            fieldProperties.put( "path", additionalFiles.getFullPath() );
            fieldProperties.put( "attribute", ( additionalFiles.getCustomAttributes() != null
                    ? additionalFiles.getCustomAttributes().toString()
                    : ConstantsString.EMPTY_STRING ) );
            fieldDetailsList.add( fieldProperties );
        }
        return fieldDetailsList;
    }

    /**
     * Nth last index of.
     *
     * @param nth
     *         the nth
     * @param ch
     *         the ch
     * @param string
     *         the string
     *
     * @return the int
     */
    private int nthLastIndexOf( int nth, String ch, String string ) {
        if ( nth <= ConstantsInteger.INTEGER_VALUE_ZERO ) {
            return string.length();
        }
        return nthLastIndexOf( --nth, ch, string.substring( ConstantsInteger.INTEGER_VALUE_ZERO, string.lastIndexOf( ch ) ) );
    }

    /**
     * Adds the global variables to output.
     *
     * @param mainMap
     *         the main map
     * @param globalVariables
     *         the global variables
     */
    private void addGlobalVariablesToOutput( Map< String, Object > mainMap, Map< String, Object > globalVariables ) {
        Map< String, Object > map = new HashMap<>();
        if ( null != globalVariables ) {
            map.putAll( globalVariables );
        } else {
            mainMap.put( "globalVariables", map );
            return;
        }
        String[] keyArray = map.keySet().toArray( new String[ map.size() ] );
        for ( String orginalKeys : keyArray ) {
            String newKey = orginalKeys.replace( "{{", ConstantsString.EMPTY_STRING ).replace( "}}", ConstantsString.EMPTY_STRING );
            if ( !newKey.equals( orginalKeys ) ) {
                map.put( newKey, map.get( orginalKeys ) );
                map.remove( orginalKeys );
            }
        }
        mainMap.put( "globalVariables", map );
    }

    /**
     * Gets the empty server file field.
     *
     * @return the empty server file field
     */
    private Object getEmptyServerFileField() {
        List< Object > feildDetailsList = new ArrayList<>();
        Map< String, String > fieldProperties = new HashMap<>();
        fieldProperties.put( "name", "" );
        fieldProperties.put( "path", "" );
        fieldProperties.put( "attribute", "" );
        feildDetailsList.add( fieldProperties );
        return feildDetailsList;
    }

    /**
     * Prepare job implfor de executor.
     *
     * @param job
     *         the job
     *
     * @return the job
     */
    private Job prepareJobImplforDeExecutor( final Job job ) {
        final Job jobPrep = new JobImpl();
        jobPrep.setCompletionTime( job.getCompletionTime() );
        jobPrep.setCreatedBy( job.getCreatedBy() );
        jobPrep.setDescription( job.getDescription() );
        jobPrep.setEnvRequirements( job.getEnvRequirements() );
        jobPrep.setWhlFile( job.getWhlFile() );
        jobPrep.setElementOutput( job.getElementOutput() );
        jobPrep.setGlobalVariables( job.getGlobalVariables() );
        jobPrep.setId( job.getId() );
        jobPrep.setIOParameters( job.getIOParameters() );
        jobPrep.setIsFileRun( job.isFileRun() );
        jobPrep.setJobProcessId( job.getJobProcessId() );
        jobPrep.setJobType( job.getJobType() );
        jobPrep.setLog( job.getLog() );
        jobPrep.setLogGeneral( job.getLogGeneral() );
        jobPrep.setLogGeneralFilePath( job.getLogGeneralFilePath() );
        jobPrep.setLogPath( job.getLogPath() );
        jobPrep.setLogSummary( job.getLogSummary() );
        jobPrep.setLogSummaryFilePath( job.getLogSummaryFilePath() );
        jobPrep.setMachine( job.getMachine() );
        jobPrep.setName( job.getName() );
        jobPrep.setOs( job.getOs() );
        jobPrep.setProgress( job.getProgress() );
        jobPrep.setRequestHeaders( job.getRequestHeaders() );
        jobPrep.setRerunFromJob( job.getRerunFromJob() );
        jobPrep.setResultSummary( job.getResultSummary() );
        jobPrep.setRunMode( job.getRunMode() );
        jobPrep.setRunsOn( job.getRunsOn() );
        jobPrep.setServer( job.getServer() );
        jobPrep.setStatus( job.getStatus() );
        jobPrep.setSubmitTime( job.getSubmitTime() );
        jobPrep.setVersion( job.getVersion() );
        jobPrep.setWorkflow( job.getWorkflow() );
        jobPrep.setWorkflowId( job.getWorkflowId() );
        jobPrep.setWorkflowName( job.getWorkflowName() );
        jobPrep.setWorkflowVersion( job.getWorkflowVersion() );
        jobPrep.setWorkingDir( job.getWorkingDir() );
        jobPrep.setDesignSummary( job.getDesignSummary() );
        jobPrep.setObjectiveVariables( job.getObjectiveVariables() );
        jobPrep.setCustomVariables( job.getCustomVariables() );
        jobPrep.setJobSchemeCategory( job.getJobSchemeCategory() );
        jobPrep.setJobInteger( job.getJobInteger() );
        jobPrep.setPostprocess( job.getPostprocess() );
        jobPrep.setResultSchemeAsJson( job.getResultSchemeAsJson() );
        jobPrep.setAskOnRunParameters( job.getAskOnRunParameters() );
        return jobPrep;
    }

    /**
     * Save relation in case of scheme optimization.
     *
     * @param jobParameters
     *         the job parameters
     */
    private void saveRelationInCaseOfSchemeOptimization( JobParameters jobParameters ) {

        if ( jobParameters.getId() == null ) {
            jobParameters.setId( UUID.randomUUID().toString() );
        }
        if ( jobParameters.getWorkflow().getMasterJobId() != null && jobParameters.getWorkflow().getMasterJobId().length() > 5 ) {
            Relation relation = new Relation( UUID.fromString( jobParameters.getWorkflow().getMasterJobId() ),
                    UUID.fromString( jobParameters.getId() ) );
            final String realtionURL = prepareURL( "/api/workflow/create/relation", jobParameters.getServer() );
            SuSClient.postRequest( realtionURL, JsonUtils.objectToJson( relation ), prepareHeaders( jobParameters.getRequestHeaders() ) );
            writeLogs( Level.INFO, "relation saved for case of optimization" );
        }

    }

    /**
     * Scan file.
     *
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     * @param scanFile
     *         the scan file
     *
     * @return the list
     */
    private List< ScanResponseDTO > scanFile( RestAPI server, RequestHeaders requestHeaders, ScanFileDTO scanFile ) {
        setPropertiesForUrl();
        final String url = prepareURL( "/api/config/workflowscheme/scan/file", server );

        final String payload = JsonUtils.toFilteredJson( new String[]{}, scanFile );
        final SusResponseDTO responseDTO = SuSClient.postRequest( url, payload, prepareHeaders( requestHeaders ) );
        if ( !responseDTO.getSuccess() ) {
            throw new SusException( new Exception( responseDTO.getMessage().getContent() ), WorkflowExecutionManagerImpl.class );
        }
        return JsonUtils.linkedMapObjectToClassObject( responseDTO.getData(), List.class );

    }

    /**
     * Gets the scan file by selection id.
     *
     * @param field
     *         the field
     * @param jobImpl
     *         the job impl
     * @param type
     *         the type
     *
     * @return the scan file by selection id
     */
    private ScanFileDTO getScanFileBySelectionId( Field< ? > field, Job jobImpl, String type ) {
        setPropertiesForUrl();
        UUID selectionId = UUID.fromString( ( String ) field.getValue() );
        final String selectionURL = prepareURL( "/api/workflow/" + type + "/selection/" + selectionId, jobImpl.getServer() );
        writeLogs( Level.INFO, "url is  " + selectionURL );

        final SusResponseDTO responseDTO = SuSClient.getRequest( selectionURL, prepareHeaders( jobImpl.getRequestHeaders() ) );
        if ( !responseDTO.getSuccess() ) {
            throw new SusException( new Exception( responseDTO.getMessage().getContent() ), WorkflowExecutionManagerImpl.class );
        }
        return JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), ScanFileDTO.class );

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
        SusResponseDTO susresponse = SuSClient.getRequest( api, prepareHeaders( requestHeaders ) );
        return susresponse.getData().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Map< String, Object > > getDesignSummary( RestAPI server, RequestHeaders requestHeaders, UUID id ) {
        final String designSummaryURL = prepareURL( "/api/config/workflowscheme/" + id + "/designsummary/list", server );
        final SusResponseDTO responseDTO = SuSClient.postRequest( designSummaryURL, DESIGN_SUMMARY_PAYLOAD,
                prepareHeaders( requestHeaders ) );
        return JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), FilteredResponse.class ).getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectiveVariableDTO > getObjectiveVariable( RestAPI server, RequestHeaders requestHeaders, UUID id ) {
        final String designSummaryURL = prepareURL( "/api/config/workflowscheme/" + id + "/objectivevariables/list", server );
        final SusResponseDTO responseDTO = SuSClient.postRequest( designSummaryURL, DESIGN_SUMMARY_PAYLOAD,
                prepareHeaders( requestHeaders ) );
        return JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), FilteredResponse.class ).getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ? > getCustomVariable( RestAPI server, RequestHeaders requestHeaders, UUID id ) {
        final String customVariableURL = prepareURL( "/api/workflow/" + id + "/customVariables/list", server );
        final SusResponseDTO responseDTO = SuSClient.postRequest( customVariableURL, DESIGN_SUMMARY_PAYLOAD,
                prepareHeaders( requestHeaders ) );
        return JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), FilteredResponse.class ).getData();
    }

    /**
     * Gets the action done.
     *
     * @param userWorkFlow
     *         the user work flow
     * @param element
     *         the element
     * @param jobImpl
     *         the job impl
     * @param executedElements
     *         the executed elements
     * @param executionPath
     *         the execution path
     * @param executedElementsIds
     *         the executed elements ids
     *
     * @return the action done
     */
    private Task< String, DecisionObject > getActionDone( UserWorkFlow userWorkFlow, Map< String, Object > parameters,
            UserWFElement element, Job jobImpl, String executionPath, Set< String > executedElementsIds,
            WorkflowExecutionParameters workflowExecutionParameters ) {
        return switch ( element.getKey() ) {
            case ConstantsElementKey.WFE_IO -> new WorkflowIOElementAction( jobImpl, element, parameters, executedElementsIds );
            case ConstantsElementKey.WFE_SCRIPT -> new WorkflowScriptElementAction( jobImpl, element, parameters, executedElementsIds );
            case ConstantsElementKey.WFE_SCRIPT_PYTHON -> new WorkflowPythonElementAction( jobImpl, element, parameters, executionPath,
                    executedElementsIds );
            case ConstantsElementKey.WFE_SUS_ACTION -> new WorkflowSusActionElementAction( jobImpl, element, parameters, executionPath,
                    executedElementsIds );
            case ConstantsElementKey.WFE_SUS_EXPORT_FILES -> new SuSFileElementAction( jobImpl, element, parameters, jobImpl.getServer(),
                    executedElementsIds );
            case ConstantsElementKey.WFE_WAIT -> new WorkflowWaitElementAction( jobImpl, parameters, element, executedElementsIds );
            case ConstantsElementKey.WFE_CONDITIONAL -> new ConditionalElementAction( jobImpl, element, parameters, this, userWorkFlow,
                    executedElementsIds );
            case ConstantsElementKey.WFE_SUS_EXPORT_DATAOBJECT -> new SusDataObjectExportElementAction( jobImpl, element, parameters,
                    jobImpl.getServer(), executedElementsIds );
            case ConstantsElementKey.WFE_SUS_IMPORT_OBJECTS -> new ImportProcessElementAction( jobImpl, element, parameters,
                    jobImpl.getServer(), executedElementsIds );
            case ConstantsElementKey.WFE_SUS_IMPORT_DATAOBJECT -> new FilesImportElementAction( jobImpl, element, parameters,
                    jobImpl.getServer(), executedElementsIds );
            case ConstantsElementKey.WFE_SUB_WORKFLOW -> new WorkflowSubWfElementAction( jobImpl, element, parameters, userWorkFlow, this,
                    jobImpl.getServer(), executedElementsIds, executionPath );
            case ConstantsElementKey.WFE_SUS_TRANSFER_LOCATION -> new TransferLocationElementAction( jobImpl, element, parameters,
                    jobImpl.getServer(), executedElementsIds );
            case ConstantsElementKey.WFE_EMAIL -> new EmailElementAction( jobImpl, element, parameters, executedElementsIds );
            case ConstantsElementKey.WFE_IMPORT_CB2 -> new ImportCB2ElementAction( jobImpl, element, parameters, jobImpl.getServer(),
                    executedElementsIds );
            case ConstantsElementKey.WFE_ABAQUS_EXPLIZIT, ConstantsElementKey.WFE_ABAQUS_STANDARD, ConstantsElementKey.WFE_LSDYNA,
                 ConstantsElementKey.WFE_NASTRAN, ConstantsElementKey.WFE_OPTISTRUCT -> new WorkflowHPCElementAction(
                    jobImpl, element, parameters, executedElementsIds );
            case ConstantsElementKey.WFE_ASSEMBLE_AND_SIMULATE -> new AssembleAndSimulateCB2ElementAction( jobImpl, element, parameters,
                    executedElementsIds );
            case ConstantsElementKey.WFE_MACHINE_LEARNING -> new WorkflowTrainModelElementAction( jobImpl, element, parameters,
                    executedElementsIds );
            case ConstantsElementKey.WFE_FOREACHLOOP -> new WorkFlowLoopElementAction( jobImpl, element, parameters, this,
                    workflowExecutionParameters, userWorkFlow, executionPath, executedElementsIds );
            default -> throw new SusException( String.format( ELEMENT_IS_NOT_OF_CONFIGURED_TYPES, element.getKey() ) );
        };
    }

    /**
     * Gets the all input output parameters of workflow.
     *
     * @param jobParameters
     *         the job parameters
     * @param elements
     *         the elements
     * @param staggingPath
     *         the stagging path
     *
     * @return the all input output parameters of workflow
     */
    private Map< String, Map< String, Object > > getAllInputOutputParametersOfWorkflow( JobParameters jobParameters,
            List< UserWFElement > elements, String staggingPath ) {
        final Map< String, Map< String, Object > > parameters = new HashMap<>();
        for ( final UserWFElement userWFElement : elements ) {
            final Map< String, Object > inputUserFields = new HashMap<>();
            if ( ( ( userWFElement != null ) && ( userWFElement.getFields() != null ) ) && !userWFElement.getFields().isEmpty() ) {
                for ( final Field< ? > userWFElementField : userWFElement.getFields() ) {
                    if ( null != staggingPath ) {
                        if ( userWFElementField.getType().contentEquals( FieldTypes.REGEX_FILE.getType() )
                                && userWFElementField.getTemplateType().contentEquals( FieldTemplates.DESIGN_VARIABLE.getValue() ) ) {
                            ScanFileDTO scanFileObj = JsonUtils.jsonToObject( JsonUtils.toJson( userWFElementField.getValue() ),
                                    ScanFileDTO.class );
                            String fileName = new File( scanFileObj.getFile() ).getName();
                            scanFileObj.setFile( staggingPath + File.separator + fileName );
                            List< ScanResponseDTO > scanValues = scanFile( jobParameters.getServer(), jobParameters.getRequestHeaders(),
                                    scanFileObj );
                            for ( int i = 0; i < scanFileObj.getVariables().size(); i++ ) {
                                scanFileObj.getVariables().get( i ).setHighlight(
                                        JsonUtils.linkedMapObjectToClassObject( scanValues.get( i ), ScanResponseDTO.class ) );
                            }
                            inputUserFields.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + userWFElement.getName()
                                    + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + userWFElementField.getName()
                                    + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, scanFileObj );
                        } else {
                            if ( userWFElementField.getMode().contentEquals( FieldModes.USER.getType() ) || userWFElementField.getType()
                                    .contains( FieldTypes.TEXT.getType() ) ) {
                                inputUserFields.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + userWFElement.getName()
                                        + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + userWFElementField.getName()
                                        + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, userWFElementField.getValue() );
                            }
                        }
                    } else {
                        if ( userWFElementField.getMode().contentEquals( FieldModes.USER.getType() ) || userWFElementField.getType()
                                .contains( FieldTypes.TEXT.getType() ) ) {
                            inputUserFields.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + userWFElement.getName()
                                    + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + userWFElementField.getName()
                                    + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, userWFElementField.getValue() );
                        }
                    }
                }
                parameters.put( userWFElement.getName(), inputUserFields );
            }
        }
        return parameters;
    }

    /**
     * Gets the config.
     *
     * @param jobImpl
     *         the job impl
     *
     * @return the config
     */
    private WorkflowConfiguration getConfig( JobParameters jobImpl ) {
        setPropertiesForUrl();
        final String url = prepareURL( properties.getProperty( URL_GET_CONFIG ), jobImpl.getServer() );
        final SusResponseDTO responseDTO = SuSClient.getRequest( url, prepareHeaders( jobImpl.getRequestHeaders() ) );

        final WorkflowConfigurationImpl configImpl = JsonUtils.linkedMapObjectToClassObject( responseDTO.getData(),
                WorkflowConfigurationImpl.class );
        final Map< String, List< String > > applicable = new HashMap<>();
        for ( final WorkflowConfigElements elementConfigs : configImpl.getElements() ) {
            if ( elementConfigs.getElement() != null ) {
                final ElementConfig elementConfig = elementConfigs.getElement().getData();
                applicable.put( elementConfig.getKey(), elementConfig.getAllowedConnections() );
            }
        }

        final WorkflowConfigurationImpl hpcConfig = getHpcConfig( jobImpl );
        for ( final WorkflowConfigElements hpcElementConfigs : hpcConfig.getElements() ) {
            if ( hpcElementConfigs.getElement() != null ) {
                final ElementConfig hpcElementConfig = hpcElementConfigs.getElement().getData();
                applicable.put( hpcElementConfig.getKey(), hpcElementConfig.getAllowedConnections() );
            }
        }

        configImpl.setApplicable( applicable );

        writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.GET_WORKFLOW_CONFIG ) );
        return configImpl;
    }

    /**
     * Gets the hpc config.
     *
     * @param jobImpl
     *         the job impl
     *
     * @return the hpc config
     */
    private WorkflowConfigurationImpl getHpcConfig( JobParameters jobImpl ) {
        final String url = prepareURL( properties.getProperty( URL_GET_HPC_CONFIG ), jobImpl.getServer() );
        final SusResponseDTO responseDTO = SuSClient.getRequest( url, prepareHeaders( jobImpl.getRequestHeaders() ) );

        return JsonUtils.linkedMapObjectToClassObject( responseDTO.getData(), WorkflowConfigurationImpl.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< UUID, Job > getFileJobsList() {
        return jobsList;
    }

    /**
     * Gets the graph.
     *
     * @param userWorkFlow
     *         the user work flow
     *
     * @return the graph
     */
    @Deprecated
    private Diagraph< String > getGraph( UserWorkFlow userWorkFlow ) {
        // Create a Graph with Integer nodes
        final Diagraph< String > graph = new Diagraph<>();
        final List< NodeEdge > connections = userWorkFlow.getEdges();
        if ( CollectionUtils.isNotEmpty( connections ) ) {
            for ( final NodeEdge elementConnection : connections ) {
                graph.add( elementConnection.getData().getSource(), elementConnection.getData().getTarget() );
            }
        } else if ( CollectionUtils.isNotEmpty( userWorkFlow.getNodes() ) ) {
            graph.add( userWorkFlow.getNodes().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getId(), ConstantsString.EMPTY_STRING );
        }
        writeLogs( Level.INFO, "Graph Created" );
        return graph;
    }

    /**
     * Gets the graph.
     *
     * @param userWorkFlow
     *         the user work flow
     * @param workflowExecutionParameters
     *         the workflow execution parameters
     *
     * @return the graph
     */
    private Diagraph< String > getGraph( List< NodeEdge > connections, List< UserWFElement > nodes,
            WorkflowExecutionParameters workflowExecutionParameters ) {
        // Create a Graph with Integer nodes
        final Diagraph< String > graph = new Diagraph<>();
        if ( CollectionUtils.isNotEmpty( connections ) ) {
            for ( final NodeEdge elementConnection : connections ) {
                String target = elementConnection.getData().getTarget();
                String source = elementConnection.getData().getSource();
                if ( !( workflowExecutionParameters.getLoopStartEndMap().containsKey( target )
                        && workflowExecutionParameters.getLoopStartEndMap().get( target ).get( "end" ).equals( source ) ) ) {
                    graph.add( source, target );
                }
            }
        } else if ( CollectionUtils.isNotEmpty( nodes ) ) {
            graph.add( nodes.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getId(), ConstantsString.EMPTY_STRING );
        }
        writeLogs( Level.INFO, "Graph Created" );
        return graph;
    }

    /**
     * Gets the graph with single starting point.
     *
     * @param userWorkFlow
     *         the user work flow
     *
     * @return the graph with single starting point
     */
    @Override
    public Diagraph< String > getGraphWithSingleStartingPoint( UserWorkFlow userWorkFlow ) {
        final Diagraph< String > graph = getGraph( userWorkFlow );
        final String startingId = getStartingNodeId( graph );
        final List< String > multipleInputs = getMultipleStartingIds( graph, startingId );
        return addVirtualNode( multipleInputs, startingId, graph );
    }

    /**
     * Gets the multiple starting ids.
     *
     * @param graph
     *         the graph
     * @param startingId
     *         the starting id
     *
     * @return the multiple starting ids
     */
    private List< String > getMultipleStartingIds( Diagraph< String > graph, final String startingId ) {

        final Map< String, Integer > inDegree = graph.inDegree();
        final List< String > multipleIds = new ArrayList<>();
        for ( final Entry< String, Integer > entry : inDegree.entrySet() ) {
            if ( ( entry.getValue() == 0 ) && !entry.getKey().equals( startingId ) ) {
                multipleIds.add( entry.getKey() );
            }
        }
        return multipleIds;
    }

    /**
     * Gets the parameters of connected elements.
     *
     * @param elements
     *         the elements
     * @param graph
     *         the graph
     * @param startingId
     *         the starting id
     * @param jobImpl
     *         the job impl
     *
     * @return the parameters of connected elements
     */
    private Map< String, Object > getParametersOfConnectedElements( List< UserWFElement > elements, Diagraph< String > graph,
            final String startingId, Job jobImpl ) {
        final List< String > connectedElementIds = graph.getInVertex( startingId );
        final Map< String, Object > parameters = new HashMap<>();
        UserWFElement element;
        final Map< String, Map< String, Object > > allJobIOParams = jobImpl.getIOParameters();
        for ( final UserWFElement userWFElement : elements ) {
            if ( connectedElementIds.contains( userWFElement.getId() ) ) {
                element = userWFElement;
                if ( element.getKey() != null && element.getName() != null ) {
                    writeLogs( Level.INFO, "Found element: " + element.getName() );
                }
                for ( final Field< ? > userWFElementField : userWFElement.getFields() ) {
                    final String key = ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + userWFElement.getName()
                            + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + userWFElementField.getName()
                            + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES;

                    final Map< String, Object > elementParameters = allJobIOParams.get( userWFElement.getName() );
                    // special case, replace keys values with File names
                    // For only: scheme-regex-file : scheme-parse-file :scheme-template-file
                    if ( userWFElementField.getType().equalsIgnoreCase( FieldTypes.REGEX_FILE.getType() ) || userWFElementField.getType()
                            .equalsIgnoreCase( FieldTypes.OBJECT_PARSER.getType() ) || userWFElementField.getType()
                            .equalsIgnoreCase( FieldTypes.TEMPLATE_FILE.getType() ) ) {

                        replaceRegexAndParserAndTemplateKeyValues( jobImpl, parameters, userWFElementField, key, elementParameters );
                    } else {
                        // normal replacements for all other elements fields
                        if ( ( null != elementParameters ) ) {
                            parameters.put( key, elementParameters.get( key ) );
                        }
                    }
                }

            }
        }
        return parameters;
    }

    /**
     * Replace regex and parser and template key values.
     *
     * @param jobImpl
     *         the job
     * @param parameters
     *         the parameters
     * @param userWFElementField
     *         the user WF element field
     * @param key
     *         the key
     * @param elementParameters
     *         the element parameters
     */
    private void replaceRegexAndParserAndTemplateKeyValues( Job jobImpl, final Map< String, Object > parameters,
            final Field< ? > userWFElementField, final String key, final Map< String, Object > elementParameters ) {

        writeLogs( Level.INFO, "replacing template " + userWFElementField.getType() + " value to File name " );
        try {
            if ( ( null != elementParameters ) && ( elementParameters.get( key ) != null ) ) {

                if ( userWFElementField.getType().equalsIgnoreCase( FieldTypes.REGEX_FILE.getType() ) || userWFElementField.getType()
                        .equalsIgnoreCase( FieldTypes.TEMPLATE_FILE.getType() ) ) {

                    Object obj = elementParameters.get( key );
                    JSONObject jsonObj = new JSONObject( JsonUtils.toJson( obj ) );
                    if ( jsonObj.has( "file" ) ) {
                        String filePath = ( String ) jsonObj.get( "file" );
                        String fileName = getLastDirName( OSValidator.convertPathToRelitiveOS( filePath ) );
                        parameters.put( key, fileName );
                    }

                } else if ( userWFElementField.getType().equalsIgnoreCase( FieldTypes.OBJECT_PARSER.getType() ) ) {
                    if ( userWFElementField.getTemplateType().equalsIgnoreCase( FieldTemplates.CUSTOM_VARIABLE.getValue() ) ) {
                        parameters.put( key,
                                getCustomVariable( jobImpl.getServer(), jobImpl.getRequestHeaders(), jobImpl.getWorkflowId() ) );
                    } else {
                        Object fieldValue = elementParameters.get( key );
                        Map< String, Object > field = ( Map< String, Object > ) fieldValue;
                        String filePath = String.valueOf( field.get( "file" ) );
                        parameters.put( key, new File( filePath ).getName() );
                    }
                }
            }

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            writeLogs( Level.INFO, "Failed to replace key values for templates " + e.getMessage() );
        }

    }

    /**
     * Gets the last dir name.
     *
     * @param f
     *         the f
     *
     * @return the last dir name
     */
    public String getLastDirName( String f ) {
        String ext = "";
        int i = f.lastIndexOf( '/' );
        if ( i > 0 ) {
            writeLogs( Level.INFO, "index " + i );
            if ( i < f.length() - 1 ) {
                ext = f.substring( i + 1 );
            }
        } else {
            i = f.lastIndexOf( '\\' );
            writeLogs( Level.INFO, "index " + i );
            if ( i > 0 && i < f.length() - 1 ) {
                ext = f.substring( i + 1 );
            }
        }
        return ext;
    }

    /**
     * Gets the engine properties.
     *
     * @return the engine properties
     */
    private static Properties getEngineProperties() {
        Properties properties = new Properties();
        try ( InputStream engineStream = new FileInputStream( FilenameUtils.getFullPathNoEndSeparator(
                URLDecoder.decode( WFApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8" ) )
                + File.separator + "engine_host.conf" ) ) {
            properties.load( engineStream );
        } catch ( final IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusDataBaseException(
                    ( MessageBundleFactory.getDefaultMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), "engine_host.conf" ) ) );
        }
        return properties;
    }

    /**
     * Gets the python execution path.
     *
     * @param job
     *         the job
     *
     * @return the python execution path
     */
    private String getPythonExecutionPath( Job job ) {
        if ( !job.isFileRun() ) {
            updateJob( job );
        }
        String executionPythonPath = null;
        try {
            if ( !isLocal( job.getRunsOn().getId().toString() ) ) {
                Properties engineProperties = getEngineProperties();
                boolean installRequirements = null != job.getEnvRequirements() && !job.getEnvRequirements().isEmpty();
                String whlFilePath = ConstantsString.EMPTY_STRING;
                if ( null != job.getWhlFile() && !job.getWhlFile().isEmpty() ) {
                    whlFilePath = getWhlFilePath( job );
                }
                if ( installRequirements || !whlFilePath.isEmpty() ) {
                    executionPythonPath = createEnv( job, engineProperties, job.getEnvRequirements(), whlFilePath );
                } else {
                    if ( engineProperties.getProperty( "pythonpath" ) == null ) {
                        logAndThrowException( job, WFEMessages.PYTHON_EXECUTION_PATH_IS_NOT_SET, ConstantsString.LOCAL );
                    }
                    executionPythonPath = engineProperties.getProperty( "pythonpath" );
                }
            } else {
                executionPythonPath = getLocalPythonPathFromConfigHome();
                writeLogs( Level.INFO, "local python path :" + executionPythonPath );
            }
            if ( !checkPythonOnOS( executionPythonPath, job.getId() ) ) {
                writeLogs( Level.SEVERE,
                        MessagesUtil.getMessage( WFEMessages.PYTHON_EXECUTION_PATH_NOT_SET, ConstantsString.LOCAL, executionPythonPath,
                                "<USER_HOME>/SuS/config.json", "\"python\": {\n" + "    \"pythonpath\": \"/usr/bin/python\"\n" + "  }," ) );
                job.setStatus( new Status( WorkflowStatus.FAILED ) );
                updateJob( job );
                throw new SusException(
                        MessagesUtil.getMessage( WFEMessages.PYTHON_EXECUTION_PATH_NOT_SET, ConstantsString.LOCAL, executionPythonPath,
                                "<USER_HOME>/SuS/config.json", "\"python\": {\n" + "    \"pythonpath\": \"/usr/bin/python\"\n" + "  }," ) );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
        return executionPythonPath;

    }

    /**
     * Creates the env.
     *
     * @param job
     *         the job
     * @param engineProperties
     *         the engine properties
     *
     * @return the string
     */
    private String createEnv( Job job, Properties engineProperties, String requirements, String whlFilePath ) {
        if ( engineProperties.getProperty( "pythonpath" ) == null ) {
            logAndThrowException( job );
        }
        JobLog.addLog( job.getId(),
                new LogRecord( ConstantsMessageTypes.INFO, MessagesUtil.getMessage( WFEMessages.ENVIRONMENT_CREATING_FOR_JOB ) ) );
        if ( !job.isFileRun() ) {
            updateJob( job );
        }
        String pythonPath = engineProperties.getProperty( "pythonpath" );
        String repoURL = engineProperties.getProperty( "repoURL" );
        String jobEnv = job.getWorkingDir().getPath() + ConstantsString.FORWARD_SLASH + job.getName();
        String envCommand = String.format( "%s -m venv %s", pythonPath, jobEnv );
        ProcessResult envCreationResult = runCommand( Arrays.asList( envCommand.split( "\\s+" ) ), "Environment Creation Command", true,
                null );
        if ( envCreationResult.getExitValue() != ConstantsInteger.INTEGER_VALUE_ZERO ) {
            logAndThrowException( job, WFEMessages.ENVIRONMENT_CREATION_FAILED, ConstantsString.EMPTY_STRING );
        } else {
            installWhlFile( job, jobEnv, whlFilePath );
            installDependencies( job, jobEnv, requirements, repoURL );
        }
        return jobEnv + "/bin/python";
    }

    /**
     * Install whl and dependencies.
     *
     * @param job
     *         the job
     * @param jobEnv
     *         the job env
     * @param repoURL
     *         the repo url
     */
    private void installWhlFile( Job job, String jobEnv, String whlFilePath ) {
        if ( !whlFilePath.isEmpty() ) {
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessagesUtil.getMessage( WFEMessages.INSTALLING_WHL_FILES_IN_ENVIRONMENT ) ) );
            if ( !job.isFileRun() ) {
                updateJob( job );
            }
            String installCommand = String.format( "%s/bin/pip install %s", jobEnv, whlFilePath );
            ProcessResult result = runCommand( Arrays.asList( installCommand.split( "\\s+" ) ), "Whl Installation Command", true, null );
            if ( result.getExitValue() != ConstantsInteger.INTEGER_VALUE_ZERO ) {
                logAndThrowException( job, WFEMessages.INSTALLATION_FAILED_FOR_ENVIRONMENT, "Whl file" );
            }
        }
    }

    /**
     * Install dependencies.
     *
     * @param job
     *         the job
     * @param jobEnv
     *         the job env
     * @param repoURL
     *         the repo url
     */
    private void installDependencies( Job job, String jobEnv, String requirements, String repoURL ) {
        if ( !requirements.isEmpty() ) {
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessagesUtil.getMessage( WFEMessages.INSTALLING_DEPENDENCIES_IN_ENVIRONMENT ) ) );
            if ( !job.isFileRun() ) {
                updateJob( job );
            }
            String requirementFilePath = prepareRequirementFile( job );
            String installCommand = ( null != repoURL && !repoURL.isEmpty() )
                    ? String.format( "%s/bin/pip install -i %s -r %s", jobEnv, repoURL, requirementFilePath )
                    : String.format( "%s/bin/pip install -r %s", jobEnv, requirementFilePath );
            ProcessResult result = runCommand( Arrays.asList( installCommand.split( "\\s+" ) ), "Requirements Installation Command", true,
                    null );
            if ( result.getExitValue() != ConstantsInteger.INTEGER_VALUE_ZERO ) {
                logAndThrowException( job, WFEMessages.INSTALLATION_FAILED_FOR_ENVIRONMENT, "Dependencies" );
            }
        }
    }

    /**
     * Gets whl file path.
     *
     * @param job
     *         the job
     *
     * @return the whl file path
     */
    private String getWhlFilePath( Job job ) {
        final SusResponseDTO responseDTO = getSelectionList( job.getServer(), job.getRequestHeaders(), job.getWhlFile() );
        if ( null != responseDTO ) {
            FilteredResponse< Object > fr = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), FilteredResponse.class );
            List< AdditionalFiles > additionalFiles = JsonUtils.jsonToList( JsonUtils.toJson( fr.getData() ), AdditionalFiles.class );
            return additionalFiles.isEmpty() ? ConstantsString.EMPTY_STRING
                    : additionalFiles.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getFullPath();
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Run command process result.
     *
     * @param command
     *         the command
     * @param operation
     *         the operation
     * @param karafLogging
     *         the karaf logging
     * @param timeoutInSeconds
     *         the timeout in seconds
     *
     * @return the process result
     */
    protected static ProcessResult runCommand( List< String > command, String operation, boolean karafLogging, Integer timeoutInSeconds ) {
        ProcessResult processResult = new ProcessResult();
        final ProcessBuilder probuilder = new ProcessBuilder( command ).redirectErrorStream( true );
        try {
            final Process process = probuilder.start();
            if ( karafLogging ) {
                log.info( String.format( "***Started %s process with command :%s", operation, probuilder.command() ) );
            }
            final StringBuilder builder = new StringBuilder();
            String line = null;
            try ( InputStreamReader is = new InputStreamReader( process.getInputStream() );
                    final BufferedReader reader = new BufferedReader( is ) ) {
                while ( ( line = reader.readLine() ) != null ) {
                    builder.append( line );
                    builder.append( System.getProperty( "line.separator" ) );
                }
            }
            final String result = builder.toString();
            if ( karafLogging ) {
                log.info( "********Process Result ********" );
                log.info( result );
            }
            String error = null;
            try {
                error = getCommandError( process );
            } catch ( Exception e ) {
                log.info( "Command Stderror : " + e.getMessage() );
            }
            processResult.setErrorStreamString( error );
            processResult.setCommand( command );
            processResult.setOutputString( result );
            processResult.setOperation( operation );
            processResult.setPid( String.valueOf( process.pid() ) );
            try {
                if ( timeoutInSeconds != null ) {
                    if ( !process.waitFor( timeoutInSeconds, TimeUnit.SECONDS ) ) {
                        process.destroyForcibly();
                    }
                } else {
                    process.waitFor();
                }
            } catch ( InterruptedException e ) {
                // this exception occurs when client abort the job and thread is wait for process to execute n return
                // instead its forcefully killed by stopJob
                log.warn( MessageBundleFactory.getDefaultMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
                log.info( "waitFor released for :" + probuilder.command() );
            }
            processResult.setExitValue( process.exitValue() );
            process.getInputStream().close();
            process.getOutputStream().close();
            process.getErrorStream().close();
            if ( karafLogging ) {
                processResult.logProcessExitStatus();
            }
        } catch ( IOException e ) {
            processResult.setErrorStreamString( e.getMessage() );
            log.error( e.getMessage(), e );
            throw new SusException( String.format( "Error in executing external process for %s: %s", operation, e.getMessage() ) );
        }
        return processResult;
    }

    /**
     * Gets command error.
     *
     * @param process
     *         the process
     *
     * @return the command error
     *
     * @throws IOException
     *         the io exception
     */
    private static String getCommandError( Process process ) throws IOException {
        String line;
        StringBuilder error = null;

        try ( InputStream stdError = process.getErrorStream();
                InputStreamReader is = new InputStreamReader( stdError );
                final BufferedReader brCleanUpError = new BufferedReader( is ) ) {
            while ( ( line = brCleanUpError.readLine() ) != null ) {
                if ( error == null ) {
                    error = new StringBuilder();
                }
                error.append( line ).append( ConstantsString.NEW_LINE );
            }

            if ( error != null && !error.toString().isEmpty() ) {
                return error.toString();
            }
        }

        return null;
    }

    /**
     * Prepare requirement file.
     *
     * @param job
     *         the job
     *
     * @return the string
     */
    private String prepareRequirementFile( Job job ) {
        final File requirementFile = new File( job.getWorkingDir().getPath() + "/requirements.txt" );
        try {
            if ( !requirementFile.exists() ) {
                Files.createFile( Paths.get( job.getWorkingDir().getPath() + "/requirements.txt" ) );
            }
            try ( BufferedWriter writer = new BufferedWriter( new FileWriter( requirementFile ) ) ) {
                writer.write( job.getEnvRequirements() );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            wfLogger.error( "failed to write requirement file :" + e.getMessage() );
        }
        return requirementFile.getAbsolutePath();
    }

    /**
     * Log and throw exception.
     *
     * @param job
     *         the job
     */
    private void logAndThrowException( Job job ) {
        writeLogs( Level.SEVERE, MessagesUtil.getMessage( WFEMessages.PYTHON_EXECUTION_PATH_IS_NOT_SET, ConstantsString.LOCAL ) );
        job.setStatus( new Status( WorkflowStatus.FAILED ) );
        updateJob( job );
        throw new SusException( MessagesUtil.getMessage( WFEMessages.PYTHON_EXECUTION_PATH_IS_NOT_SET, ConstantsString.LOCAL ) );
    }

    /**
     * Log and throw exception.
     *
     * @param job
     *         the job
     * @param message
     *         the message
     */
    private void logAndThrowException( Job job, WFEMessages message, String value ) {
        writeLogs( Level.SEVERE, MessagesUtil.getMessage( message, value ) );
        JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, MessagesUtil.getMessage( message, value ) ) );
        job.setStatus( new Status( WorkflowStatus.FAILED ) );
        updateJob( job );
        throw new SusException( MessagesUtil.getMessage( message ) );
    }

    /**
     * Gets the local python path from config home.
     *
     * @return the local python path from config home
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static String getLocalPythonPathFromConfigHome() throws IOException {
        final String propFileName =
                System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY
                        + File.separator + ConstantsString.CONFIG + File.separator + ConstantsString.CONIGFILE;
        final File file = new File( propFileName );
        UserConfigFile userConfigFile;

        try ( final InputStream targetConfigStream = new FileInputStream( file ) ) {
            userConfigFile = JsonUtils.jsonToObject( targetConfigStream, UserConfigFile.class );
        }
        return userConfigFile.getPython().getPythonpath();
    }

    /**
     * Gets the reference job.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the reference job
     */
    private Job getReferenceJob( JobParameters jobParameters ) {
        setPropertiesForUrl();
        final String url = prepareURL( properties.getProperty( GET_JOB_BY_ID ) + jobParameters.getRefJobId().toString(),
                jobParameters.getServer() );
        final SusResponseDTO responseDTO = SuSClient.getRequest( url, prepareHeaders( jobParameters.getRequestHeaders() ) );

        if ( !responseDTO.getSuccess() ) {
            throw new SusException( new Exception( responseDTO.getMessage().getContent() ), WorkflowExecutionManagerImpl.class );
        }
        return JsonUtils.linkedMapObjectToClassObject( responseDTO.getData(), JobImpl.class );

    }

    /**
     * Gets the starting node id.
     *
     * @param graph
     *         the graph
     *
     * @return the starting node id
     */
    private String getStartingNodeId( Diagraph< String > graph ) {
        final Map< String, Integer > inDegree = graph.inDegree();
        String startingId = ConstantsString.EMPTY_STRING;
        for ( final Entry< String, Integer > entry : inDegree.entrySet() ) {
            if ( entry.getValue() == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                startingId = entry.getKey();
                break;
            }
        }
        writeLogs( Level.INFO, "Starting point of workflow Element Id: " + startingId );
        return startingId;
    }

    /**
     * Gets the sub workflow id.
     *
     * @param u
     *         the u
     *
     * @return the sub workflow id
     */
    private String getSubWorkflowId( UserWorkFlow u ) {
        String subWorkflowId = ConstantsString.EMPTY_STRING;
        for ( final UserWFElement userWFElement : u.getNodes() ) {
            for ( final Field< ? > field : userWFElement.getFields() ) {
                if ( field.getName().equalsIgnoreCase( SUBWORKFLOW ) ) {
                    subWorkflowId = field.getValue().toString();
                    break;
                }
            }
        }
        return subWorkflowId;
    }

    /**
     * Gets the wf logger.
     *
     * @return the wf logger
     */
    public WfLogger getWfLogger() {
        if ( wfLogger == null ) {
            wfLogger = new WfLogger( ConstantsString.WF_LOGGER );
        }
        return wfLogger;
    }

    /**
     * Gets the wf logger summary.
     *
     * @return the wf logger summary
     */
    public WfLogger getWfLoggerSummary() {
        if ( wfLoggerSum == null ) {
            wfLoggerSum = new WfLogger( ConstantsString.WF_LOGGER_SUMMARY );
        }
        return wfLoggerSum;
    }

    /**
     * Gets the workflow definition.
     *
     * @param workflowDefinitionMap
     *         the workflow definition map
     *
     * @return the workflow definition
     */
    private WorkflowDefinitionDTO getWorkflowDefinition( Map< String, Object > workflowDefinitionMap ) {
        String json = null;

        if ( workflowDefinitionMap != null ) {
            json = JsonUtils.toJson( workflowDefinitionMap );
        }
        return JsonUtils.jsonToObject( json, WorkflowDefinitionDTO.class );
    }

    /**
     * Gets the workflow DTO by id and version id.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the workflow DTO by id and version id
     */
    public LatestWorkFlowDTO getWorkflowDTOByIdAndVersionId( JobParameters jobParameters ) {
        LatestWorkFlowDTO workflowDTO;
        writeLogs( Level.INFO, "Is workflow file run: " + jobParameters.isFileRun() );
        if ( !jobParameters.isFileRun() ) {
            writeLogs( Level.INFO, "getting workflow from server: " );
            setPropertiesForUrl();
            final String url = prepareURL( properties.getProperty( URL_GET_WF_BY_ID ), jobParameters.getServer() );
            final SusResponseDTO responseDTO = SuSClient.getRequest(
                    url + jobParameters.getWorkflow().getId() + VERSION_KEY_FOR_URL + jobParameters.getWorkflow().getVersion().getId(),
                    prepareHeaders( jobParameters.getRequestHeaders() ) );
            workflowDTO = JsonUtils.linkedMapObjectToClassObject( responseDTO.getData(), LatestWorkFlowDTO.class );
        } else {
            writeLogs( Level.INFO, "parsing file to prepare workflow dto: " + jobParameters.getWorkflow() );
            workflowDTO = jobParameters.getWorkflow();
            workflowDTO.setExecutable( true );
        }

        return workflowDTO;
    }

    /**
     * Gets the sub workflow by selection id.
     *
     * @param job
     *         the job
     * @param subWorkflowId
     *         the sub workflow id
     *
     * @return the sub workflow by selection id
     */
    public UserWorkFlow getSubWorkflowBySelectionId( Job job, final String subWorkflowId ) {
        UserWorkFlow userWorkflowToReturn = null;
        setPropertiesForUrl();
        final String url = prepareURL( properties.getProperty( URL_GET_WF_BY_ID ) + DATA_SELECTION, job.getServer() );
        final SusResponseDTO responseDTO = SuSClient.getRequest( url + subWorkflowId, prepareHeaders( job.getRequestHeaders() ) );
        if ( null != responseDTO ) {
            final LatestWorkFlowDTO subWorkflow = JsonUtils.linkedMapObjectToClassObject( responseDTO.getData(), LatestWorkFlowDTO.class );
            if ( subWorkflow != null ) {
                // Check permissions
                validateRights( job, subWorkflow.getId().toString() );
                userWorkflowToReturn = convertWorkflowDtoToUserWorkflow( subWorkflow );
                userWorkflowToReturn.getNodes().forEach( node -> {
                    if ( node.getKey().equals( ElementKeys.WFE_SUB_WORKFLOW.getKey() ) ) {
                        failJobOnError( job,
                                MessagesUtil.getMessage( WFEMessages.SUBWORKFLOW_CANNOT_CONTAIN_ANOTHER_SUBWORKFLOW, node.getName() ) );
                    }
                } );
            }
        }
        return userWorkflowToReturn;
    }

    /**
     * Fail job on error.
     *
     * @param job
     *         the job
     * @param logMessage
     *         the log message
     */
    private void failJobOnError( Job job, String logMessage ) {
        job.setStatus( new Status( WorkflowStatus.FAILED ) );
        if ( !job.isFileRun() ) {
            updateJob( job );
        } else {
            job.setCompletionTime( new Date() );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessagesUtil.getMessage( WFEMessages.TOTAL_EXECUTION_TIME, getExecutionTime( job ) ) ) );
        }
        JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, logMessage ) );
        throw new SusException( logMessage );
    }

    /**
     * Gets the workflow from sub workflow element id.
     *
     * @param element
     *         the element
     * @param job
     *         the job
     *
     * @return the workflow from sub workflow element id
     */
    public UserWorkFlow getWorkflowFromSubWorkflowElementId( UserWFElement element, Job job ) {
        String subWorkflowId = null;
        for ( final Field< ? > field : element.getFields() ) {
            if ( field.getName().equalsIgnoreCase( SUBWORKFLOW ) ) {
                subWorkflowId = field.getValue().toString();
                break;
            }
        }
        return getSubWorkflowBySelectionId( job, subWorkflowId );
    }

    /**
     * Checks if is local.
     *
     * @param runsOn
     *         the runs on
     *
     * @return true, if is local
     */
    private boolean isLocal( String runsOn ) {
        return runsOn.equals( LocationsEnum.LOCAL_LINUX.getId() ) || runsOn.equals( LocationsEnum.LOCAL_WINDOWS.getId() );
    }

    /**
     * Perform action.
     *
     * @param userWorkFlow
     *         the user work flow
     * @param elements
     *         the elements
     * @param graph
     *         the graph
     * @param startingId
     *         the starting id
     * @param jobImpl
     *         the job impl
     * @param elementActions
     *         the element actions
     * @param executedElements
     *         the executed elements
     * @param executionPath
     *         the execution path
     * @param additionalParameters
     *         the additional parameters
     * @param subWorkflowParameters
     *         the sub workflow parameters
     * @param executedElementsIds
     *         the executed elements ids
     */
    private void performAction( UserWorkFlow userWorkFlow, List< UserWFElement > elements, Diagraph< String > graph, String startingId,
            Job jobImpl, List< Task< String, DecisionObject > > elementActions, String executionPath,
            Map< String, Object > additionalParameters, Map< String, Object > subWorkflowParameters, Set< String > executedElementsIds,
            WorkflowExecutionParameters workflowExecutionParameters ) {
        if ( startingId != null ) {
            final Map< String, Object > parameters = getParametersOfConnectedElements( elements, graph, startingId, jobImpl );
            putJobParameters( parameters, jobImpl );
            parameters.putAll( additionalParameters );
            if ( subWorkflowParameters != null ) {
                parameters.putAll( subWorkflowParameters );
            }
            // On the basis of element key execution class is selected, as each element type
            // will have a different action class.
            writeLogs( Level.FINE, "startingId: " + startingId );
            final UserWFElement userWFElement = elements.stream().filter( parkingLot -> parkingLot.getId().contentEquals( startingId ) )
                    .findFirst().orElse( null );
            Task< String, DecisionObject > task = null;
            if ( userWFElement != null ) {
                task = getActionDone( userWorkFlow, parameters, userWFElement, jobImpl, executionPath, executedElementsIds,
                        workflowExecutionParameters );
                writeLogs( Level.FINE, "taskId: " + task.getId() );
                elementActions.add( task );
            }
            final List< String > firstOutReferences = graph.getOutVertex( startingId );
            for ( final String string : firstOutReferences ) {
                performAction( userWorkFlow, elements, graph, string, jobImpl, elementActions, executionPath, additionalParameters,
                        subWorkflowParameters, executedElementsIds, workflowExecutionParameters );
            }
        }
    }

    /**
     * Populate param of elements connected with logical elements.
     *
     * @param elements
     *         the elements
     * @param startingId
     *         the starting id
     * @param connectedIds
     *         the connected ids
     * @param graph
     *         the graph
     * @param parameters
     *         the parameters
     * @param allJobIOParams
     *         the all job IO params
     */
    private void populateParamOfElementsConnectedWithLogicalElements( List< UserWFElement > elements, String startingId,
            List< String > connectedIds, Diagraph< String > graph, final Map< String, Object > parameters,
            final Map< String, Map< String, Object > > allJobIOParams ) {

        for ( final String connectedId : connectedIds ) {

            final UserWFElement tempUserWFElementForFinding = new UserWFElementImpl();
            tempUserWFElementForFinding.setId( connectedId );
            if ( elements.contains( tempUserWFElementForFinding ) ) {
                final UserWFElement userWFElement = elements.get( elements.indexOf( tempUserWFElementForFinding ) );

                populateParams( elements, startingId, graph, parameters, allJobIOParams, connectedId, userWFElement );

            } else {
                writeLogs( Level.FINE, "Element not found in all elements by its Id: " + connectedId );
            }
        }
    }

    /**
     * Populate params.
     *
     * @param elements
     *         the elements
     * @param startingId
     *         the starting id
     * @param graph
     *         the graph
     * @param parameters
     *         the parameters
     * @param allJobIOParams
     *         the all job IO params
     * @param connectedId
     *         the connected id
     * @param userWFElement
     *         the user WF element
     */
    private void populateParams( List< UserWFElement > elements, String startingId, Diagraph< String > graph,
            final Map< String, Object > parameters, final Map< String, Map< String, Object > > allJobIOParams, final String connectedId,
            final UserWFElement userWFElement ) {
        if ( userWFElement.getKey().contentEquals( ElementKeys.WFE_WAIT.getKey() ) || userWFElement.getKey()
                .contentEquals( ElementKeys.SUS_CONDITIONAL.getKey() ) ) {
            final List< String > connectedElementIds = graph.getInAndOutNodes( userWFElement.getId() );
            connectedElementIds.remove( startingId );
            populateParamOfElementsConnectedWithLogicalElements( elements, connectedId, connectedElementIds, graph, parameters,
                    allJobIOParams );

        } else {

            for ( final Field< ? > userWFElementField : userWFElement.getFields() ) {
                if ( userWFElementField.getMode().contentEquals( FieldModes.USER.getType() ) ) {
                    final String key = ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + userWFElement.getName()
                            + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + userWFElementField.getName()
                            + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES;
                    final Map< String, Object > elementParameters = allJobIOParams.get( userWFElement.getName() );
                    parameters.put( key, elementParameters.get( key ) );
                }
            }

        }
    }

    /**
     * Prepare headers.
     *
     * @param headers
     *         the headers
     *
     * @return the map
     */
    private Map< String, String > prepareHeaders( RequestHeaders headers ) {
        setPropertiesForUrl();
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( properties.getProperty( USER_AGENT ), headers.getUserAgent() );
        if ( headers.getJobAuthToken() != null && !headers.getJobAuthToken().isEmpty() ) {
            requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, headers.getJobAuthToken() );
        }
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, headers.getToken() );
        return requestHeaders;

    }

    /**
     * Prepare job.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the job
     */
    private Job prepareJob( JobParameters jobParameters ) {
        final Job jobImpl = new JobImpl();
        if ( jobParameters.getId() != null ) {
            jobImpl.setId( UUID.fromString( jobParameters.getId() ) );
        }
        if ( jobParameters.getJobInteger() != null ) {
            jobImpl.setJobInteger( jobParameters.getJobInteger() );
        }
        jobImpl.setDescription( jobParameters.getDescription() );
        jobImpl.setEnvRequirements( jobParameters.getEnvRequirements() );
        jobImpl.setWhlFile( jobParameters.getWhlFile() );
        jobImpl.setName( jobParameters.getName() );
        jobImpl.setWorkingDir( jobParameters.getWorkingDir() );
        jobImpl.setWorkflowId( jobParameters.getWorkflow().getId() );
        jobImpl.setRunMode( jobParameters.getRunsOn() );
        jobImpl.setRunsOn( new LocationDTO( UUID.fromString( jobParameters.getRunsOn() ) ) );
        jobImpl.setDesignSummary( jobParameters.getDesignSummary() );
        jobImpl.setObjectiveVariables( jobParameters.getObjectiveVariables() );
        jobImpl.setCustomVariables( jobParameters.getCustomVariables() );
        try {
            jobImpl.setMachine( InetAddress.getLocalHost().getHostName() );
        } catch ( final UnknownHostException e ) {
            log.error( e.getMessage(), e );
            writeLogs( Level.SEVERE, "Machine Name Error: " + e.getMessage() );
        }

        jobImpl.setWorkflowVersion( jobParameters.getWorkflow().getVersion() );
        jobImpl.setServer( jobParameters.getServer() );
        jobImpl.setRequestHeaders( jobParameters.getRequestHeaders() );

        jobImpl.setStatus( new Status( WorkflowStatus.RUNNING ) );
        jobImpl.setProgress( new ProgressBar() );

        jobImpl.setIsFileRun( jobParameters.isFileRun() );
        if ( jobParameters.isFileRun() ) {
            // set machine ,starting time for file based job
            jobImpl.setSubmitTime( new Date() );
            jobImpl.setWorkflowName( jobParameters.getWorkflow().getName() );
            jobImpl.setCreatedBy( getJobUser( jobParameters ) );
        }
        jobImpl.setCreateChild( jobParameters.isCreateChild() );

        jobImpl.setWorkflow( jobParameters.getWorkflow() );
        if ( null != jobParameters.getWorkflow().getDummyMasterJobId() ) {
            jobImpl.getWorkflow().setDummyMasterJobId( jobParameters.getWorkflow().getDummyMasterJobId() );
        }
        if ( ( null != jobParameters.getRefJobId() ) && !StringUtils.isNullOrEmpty( jobParameters.getRefJobId().toString() ) ) {
            jobImpl.setRerunFromJob( getReferenceJob( jobParameters ) );
        }

        jobImpl.setOs( OSValidator.getOperationSystemName() );
        jobImpl.setAskOnRunParameters( getAskOnRunParametersOfJob( jobParameters ) );
        jobImpl.setPostprocess( jobParameters.getPostprocess() );
        jobImpl.setResultSchemeAsJson( jobParameters.getResultSchemeAsJson() );
        return jobImpl;
    }

    /**
     * Gets the ask on run parameters of job.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the ask on run parameters of job
     */
    private LinkedHashMap< String, Object > getAskOnRunParametersOfJob( JobParameters jobParameters ) {
        try {
            LinkedHashMap< String, Object > askOnRunParameters = new LinkedHashMap<>();

            WorkflowModel workflowModel = JsonUtils.jsonToObject( JsonUtils.toJson( jobParameters.getWorkflow().getElements() ),
                    WorkflowModel.class );
            for ( WorkflowElement element : workflowModel.getNodes() ) {
                for ( Field field : element.getData().getFields() ) {
                    if ( field.isChangeOnRun() ) {
                        askOnRunParameters.put( field.getName(), field );
                    }
                }
            }
            return askOnRunParameters;
        } catch ( Exception e ) {
            log.error( "Failed to get ask on run parameters : ", e );
        }
        return null;
    }

    /**
     * Gets the job user.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the job user
     */
    private UserDTO getJobUser( JobParameters jobParameters ) {
        if ( jobParameters.getJobRunByUserId() != null ) { // if user data available, save api call
            UserDTO user = new UserDTO( jobParameters.getJobRunByUserId().toString() );
            if ( jobParameters.getJobRunByUserUID() != null ) {
                user.setUserUid( jobParameters.getJobRunByUserUID() );
            }
            return user;
        } else {
            return getCurrentUser( jobParameters );
        }
    }

    /**
     * Gets the current user.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the current user
     */
    private UserDTO getCurrentUser( JobParameters jobParameters ) {
        return getCurrentUserByRestApiAndRequestHeader( jobParameters.getServer(), jobParameters.getRequestHeaders() );
    }

    /**
     * Gets the current user by rest api and request header.
     *
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     *
     * @return the current user by rest api and request header
     */
    private UserDTO getCurrentUserByRestApiAndRequestHeader( RestAPI server, RequestHeaders requestHeaders ) {
        String url = server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort() + API_CURRENT_USER;
        SusResponseDTO susResponse = SuSClient.getRequest( url, prepareHeaders( requestHeaders ) );
        String json = JsonUtils.toJson( susResponse.getData() );
        return JsonUtils.jsonToObject( json, UserDTO.class );
    }

    /**
     * Prepare job impl.
     *
     * @param jobParameters
     *         the job parameters
     * @param totalElements
     *         the total elements
     * @param job
     *         the job
     * @param executedElements
     *         the executed elements
     */
    private void prepareJobImpl( JobParameters jobParameters, int totalElements, final Job job ) {
        Job jobImplSaved;
        if ( !jobParameters.isFileRun() ) {
            final String url =
                    job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                            + "/api/job/isHost/" + job.getRunsOn().getId();
            SusResponseDTO susResponseDTO = SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) );
            ExecutionHosts host = JsonUtils.jsonToObject( JsonUtils.toJson( susResponseDTO.getData() ), ExecutionHosts.class );
            if ( null != host.getId() ) {
                job.setRunMode( LocationsEnum.DEFAULT_LOCATION.getId() );

                SusResponseDTO susResponseDTO1 = SuSClient.getRequest(
                        job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                                + API_GET_LOCATION + LocationsEnum.DEFAULT_LOCATION.getId(), prepareHeaders( job.getRequestHeaders() ) );
                LocationDTO locationDTO = JsonUtils.jsonToObject( JsonUtils.toJson( susResponseDTO1.getData() ), LocationDTO.class );
                job.setRunsOn( locationDTO );
            }
            job.setRequestHeaders( jobParameters.getRequestHeaders() );
            job.getWorkflow().setToken( jobParameters.getRequestHeaders().getJobAuthToken() );
            job.setPostprocess( jobParameters.getPostprocess() );

            if ( null == jobParameters.getWorkflow().getJob().get( "max_execution_time" )
                    || Integer.parseInt( jobParameters.getWorkflow().getJob().get( "max_execution_time" ).toString() ) == -1 ) {
                jobParameters.setJobMaxExecutionTime( null );
            } else {
                jobParameters.setJobMaxExecutionTime( DateUtils.getMaxJobRunningTime(
                        Integer.parseInt( jobParameters.getWorkflow().getJob().get( "max_execution_time" ).toString() ) ) );
            }
            job.setJobMaxExecutionTime( jobParameters.getJobMaxExecutionTime() );
            job.setGlobalVariables( jobParameters.getGlobalVariables() );
            jobImplSaved = saveJob( job );
            JobLog.addLog( jobImplSaved.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessagesUtil.getMessage( WFEMessages.JOB_SAVED_ON_SERVER, job.getName() ) ) );

            job.setWorkflowName( jobImplSaved.getWorkflowName() );

            jobImplSaved.setRequestHeaders( jobParameters.getRequestHeaders() );
            jobImplSaved.setServer( jobParameters.getServer() );
            jobImplSaved.setName( jobParameters.getName() );
            jobImplSaved.setProgress( new ProgressBar( totalElements, ConstantsInteger.INTEGER_VALUE_ZERO ) );
            writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.JOB_SAVED_ON_SERVER, jobParameters.getName() ) );

            job.setWorkflowName( jobImplSaved.getWorkflowName() );
            job.setCreatedBy( jobImplSaved.getCreatedBy() );
            job.setId( jobImplSaved.getId() );
            job.setSubmitTime( jobImplSaved.getSubmitTime() );

        } else {
            // to store Id in map to stop a file base job
            job.setId( UUID.randomUUID() );

        }
    }

    /**
     * Prepare job path.
     *
     * @param jobParameters
     *         the job parameters
     * @param job
     *         the job
     */
    private void prepareJobPath( JobParameters jobParameters, final Job job ) {
        final EngineFile engineFile = new EngineFile();
        File jobPath;
        String bmwSuggestion;
        if ( jobParameters.getJobType() == JobTypeEnums.VARIANT.getKey() ) {
            if ( !isLocal( jobParameters.getRunsOn() ) ) {
                jobPath = new File( jobParameters.getWorkingDir().getPath() );
            } else {
                jobPath = new File( job.getWorkingDir().getItems().get( 0 ) );
            }
        } else {
            bmwSuggestion = File.separator + job.getName() + "_" + job.getJobInteger();
            if ( !isLocal( jobParameters.getRunsOn() ) ) {
                jobPath = new File( jobParameters.getWorkingDir().getPath() + bmwSuggestion );
            } else {
                jobPath = new File( job.getWorkingDir().getItems().get( 0 ) + bmwSuggestion );
            }
        }
        jobPath.mkdirs();
        engineFile.setPath( jobPath.getPath() );
        engineFile.setItems( List.of( jobPath.getPath() ) );
        job.setWorkingDir( engineFile );
        jobParameters.setWorkingDir( engineFile );
    }

    /**
     * Prepare log file footer.
     *
     * @param job
     *         the job
     * @param userWorkFlow
     *         the user work flow
     * @param executedElements
     *         the executed elements
     */
    private void prepareLogFileFooter( Job job, final UserWorkFlow userWorkFlow, int executedElements ) {
        writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.UPDATE_JOB, job.getName(), job.getStatus().getName() ) );
        writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.TOTAL_ELEMENTS_IN_WORKFLOW, userWorkFlow.getNodes().size() ) );
        writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.ELEMENTS_EXECUTED, executedElements ) );
        writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_COMPLETION_TIME, new Date() ) );
    }

    /**
     * Prepare log file header.
     *
     * @param jobParameters
     *         the job parameters
     * @param workflowDto
     *         the workflow dto
     */
    private void prepareLogFileHeader( JobParameters jobParameters, final LatestWorkFlowDTO workflowDto ) {

        if ( ( null != workflowDto.getVersion() ) && ( null != workflowDto.getCreatedBy() ) ) {
            writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_NAME, workflowDto.getName() ) );
            writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_VERSION, workflowDto.getVersion().getId() ) );
            writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_OWNER, workflowDto.getCreatedBy().getName() ) );
        }

        writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.JOB_NAME, jobParameters.getName() ) );
        writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_STARTED_ON, new Date() ) );
        writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_ENGINE_VERSION,
                CommonUtils.readPropertyFromJarManifest( WorkflowExecutionManagerImpl.class, BUNDLE_VERSION ) ) );
    }

    /**
     * Prepare URL.
     *
     * @param url
     *         the url
     * @param server
     *         the server
     *
     * @return the string
     */
    private String prepareURL( String url, RestAPI server ) {
        if ( server != null ) {
            return server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort() + url;
        } else {
            writeLogs( Level.SEVERE, MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) );
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) ) );
        }
    }

    /**
     * Prepare workflow model.
     *
     * @param dto
     *         the dto
     *
     * @return the user work flow
     */
    private UserWorkFlow prepareWorkflowModel( LatestWorkFlowDTO dto ) {
        final UserWorkFlow model = setWorkflowOwner( dto );
        String json = null;
        final Map< String, Object > map = dto.prepareDefinition();
        if ( map != null ) {
            json = JsonUtils.toJson( map );
        }
        final WorkflowDefinitionDTO workflowDefinitionDTO = JsonUtils.jsonToObject( json, WorkflowDefinitionDTO.class );
        model.setNodes( setWorkflowElements( workflowDefinitionDTO ) );
        model.setEdges( workflowDefinitionDTO.getElements().getEdges() );
        return model;
    }

    public static String findFilePath( Object jsonData ) {
        if ( jsonData instanceof List ) {
            for ( Object item : ( List< ? > ) jsonData ) {
                String result = findFilePath( item );
                if ( result != null ) {
                    return result;
                }
            }
        } else if ( jsonData instanceof Map ) {
            Map< ?, ? > jsonMap = ( Map< ?, ? > ) jsonData;
            if ( jsonMap.containsKey( "file_path" ) ) {
                return ( String ) jsonMap.get( "file_path" );
            } else {
                for ( Object value : jsonMap.values() ) {
                    String result = findFilePath( value );
                    if ( result != null ) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    public static Map< ?, ? > createDirectoriesAndCopyFiles( Object shapeModuleObj, String basePath ) {
        try {
            if ( shapeModuleObj instanceof Map ) {
                Map< ?, ? > shapeModuleMap = ( Map< ?, ? > ) shapeModuleObj;
                return processShapeModule( shapeModuleMap, basePath );
            } else {
                System.out.println( "shapeModuleObj is not a Map" );
            }
        } catch ( IllegalArgumentException e ) {
            e.printStackTrace();
        }

        return null;
    }

    private static Map< ?, ? > processShapeModule( Map< ?, ? > shapeModuleMap, String basePath ) {
        Map< Object, Object > updatedShapeModuleMap = new HashMap<>( shapeModuleMap );

        Object geometryListObj = updatedShapeModuleMap.get( "geometry_list" );
        if ( geometryListObj instanceof List ) {
            List< ? > geometryList = ( List< ? > ) geometryListObj;
            List< Object > updatedGeometryList = new ArrayList<>();

            for ( Object geometry : geometryList ) {
                if ( geometry instanceof Map ) {
                    Map< Object, Object > geometryMap = new HashMap<>( ( Map< ?, ? > ) geometry );
                    String name = ( String ) geometryMap.get( "name" );
                    String directoryName = basePath + File.separator + name;
                    new File( directoryName ).mkdirs();

                    Object ioNodeObj = geometryMap.get( "IO" );
                    System.out.println( ioNodeObj );
                    if ( ioNodeObj instanceof Map ) {
                        Map< Object, Object > ioNode = new HashMap<>( ( Map< ?, ? > ) ioNodeObj );
                        String filePath = ( String ) ioNode.get( "file_path" );
                        String fileName = Paths.get( filePath ).getFileName().toString();
                        String newFilePath = Paths.get( directoryName, fileName ).toString();
                        ioNode.put( "file_path", name + File.separator + fileName );

                        File sourceFile = new File( filePath );
                        try {
                            Files.copy( sourceFile.toPath(), Paths.get( directoryName, sourceFile.getName() ),
                                    StandardCopyOption.REPLACE_EXISTING );
                        } catch ( IOException e ) {
                            e.printStackTrace();
                        }
                        geometryMap.put( "IO", ioNode );
                    }
                    updatedGeometryList.add( geometryMap );
                }
            }
            updatedShapeModuleMap.put( "geometry_list", updatedGeometryList );
        }

        return updatedShapeModuleMap;
    }

    public static Object replaceFinalValues( Object jsonData, Map< String, String > getSections ) {
        if ( jsonData instanceof List ) {
            List< Object > jsonArray = ( List< Object > ) jsonData;
            for ( int i = 0; i < jsonArray.size(); i++ ) {
                jsonArray.set( i, replaceFinalValues( jsonArray.get( i ), getSections ) );
            }
        } else if ( jsonData instanceof Map ) {
            Map< String, Object > jsonMap = ( Map< String, Object > ) jsonData;
            for ( Map.Entry< String, Object > entry : jsonMap.entrySet() ) {
                jsonMap.put( entry.getKey(), replaceFinalValues( entry.getValue(), getSections ) );
            }
            // Remove the "getSections" key if it exists
            jsonMap.remove( "getSections" );
        } else if ( jsonData instanceof String && getSections != null && getSections.containsKey( jsonData ) ) {
            return getSections.get( jsonData );
        }

        return jsonData;
    }

    public static Object removeEmptyValues( Object jsonData ) {
        if ( jsonData instanceof List ) {
            List< Object > jsonArray = ( List< Object > ) jsonData;
            for ( int i = 0; i < jsonArray.size(); i++ ) {
                jsonArray.set( i, removeEmptyValues( jsonArray.get( i ) ) );
            }
        } else if ( jsonData instanceof Map ) {
            Map< String, Object > jsonMap = ( Map< String, Object > ) jsonData;
            Map< String, Object > copyMap = new HashMap<>( jsonMap );

            for ( Map.Entry< String, Object > entry : copyMap.entrySet() ) {
                Object value = entry.getValue();
                if ( value instanceof String && ( ( String ) value ).isEmpty() ) {
                    jsonMap.remove( entry.getKey() );
                } else {
                    jsonMap.put( entry.getKey(), removeEmptyValues( value ) );
                }
            }
        }

        return jsonData;
    }

    public static Object replaceFilePath( Object obj ) {
        if ( obj instanceof List ) {
            List< Object > list = ( List< Object > ) obj;
            for ( int i = 0; i < list.size(); i++ ) {
                list.set( i, replaceFilePath( list.get( i ) ) );
            }
            return list;
        } else if ( obj instanceof Map ) {
            Map< String, Object > map = ( Map< String, Object > ) obj;
            Map< String, Object > updatedMap = new LinkedHashMap<>( map );
            for ( Map.Entry< String, Object > entry : map.entrySet() ) {
                if ( "file_path".equals( entry.getKey() ) && entry.getValue() instanceof List ) {
                    List< Object > filePathList = ( List< Object > ) entry.getValue();
                    if ( !filePathList.isEmpty() ) {
                        updatedMap.put( "file_path", filePathList.get( 0 ) );
                    }
                } else {
                    updatedMap.put( entry.getKey(), replaceFilePath( entry.getValue() ) );
                }
            }
            return updatedMap;
        }
        return obj;
    }

    /**
     * Put job parameters.
     *
     * @param parameters
     *         the parameters
     * @param jobImpl
     *         the job impl
     */
    private void putJobParameters( Map< String, Object > parameters, Job jobImpl ) {
        parameters.put( JOB_NAME, jobImpl.getName() );
        parameters.put( EXPERIMENT_NUMBER, getExperimentNumber( jobImpl ) );
        parameters.put( JOB_EXECUTION, jobImpl.getRunMode() );
        parameters.put( JOB_WORKING_DIR, jobImpl.getWorkingDir().getPath() );
        parameters.put( JOB_DESCRIPTION, jobImpl.getDescription() );
        parameters.put( JOB_TOKEN, jobImpl.getRequestHeaders().getToken() );
        parameters.put( USER_UID, jobImpl.getCreatedBy() == null ? "N/A" : jobImpl.getCreatedBy().getUserUid() );
        parameters.put( JOB_ID, jobImpl.getJobInteger() );
        parameters.put( JOB_SYSTEMOUTPUT_PATH,
                jobImpl.getWorkingDir().getPath() + File.separator + jobImpl.getName() + ".systemoutput.json" );
        UserDTO currentUser = getCurrentUserByRestApiAndRequestHeader( jobImpl.getServer(), jobImpl.getRequestHeaders() );
        if ( currentUser != null && !CollectionUtils.isEmpty( currentUser.getUserDetails() ) ) {
            parameters.put( "{{User.Email}}", currentUser.getUserDetails().get( 0 ).getEmail() );
            parameters.put( "{{User.Name}}", currentUser.getName() );
            parameters.put( "{{User.UID}}", currentUser.getUserUid() );
        } else {
            parameters.put( "{{User.Email}}", "" );
            parameters.put( "{{User.Name}}", "" );
            parameters.put( "{{User.UID}}", "" );
        }
        if ( null != jobImpl.getResultSchemeAsJson() ) {
            parameters.put( "{{__SCHEME__}}", jobImpl.getResultSchemeAsJson() );
        }
        if ( jobImpl.getGlobalVariables() != null ) {
            parameters.putAll( jobImpl.getGlobalVariables() );
        }
        Map< String, Object > globalVariables = jobImpl.getGlobalVariables();
        if ( null != globalVariables && !globalVariables.isEmpty() && globalVariables.containsKey( SHAPE_MODULE_KEY ) ) {
            Object shapeModuleObj = globalVariables.get( SHAPE_MODULE_KEY );
            //
            shapeModuleObj = replaceFilePath( shapeModuleObj );

            if ( shapeModuleObj instanceof Map ) {
                Map< ?, ? > shapeModuleMap = ( Map< ?, ? > ) shapeModuleObj;
                Map< String, String > getSections = ( Map< String, String > ) shapeModuleMap.get( "getSections" );
                System.out.println( getSections );
                shapeModuleObj = replaceFinalValues( shapeModuleObj, getSections );
                System.out.println( shapeModuleObj );
            }
            System.out.println( shapeModuleObj );
            // if ( shapeModuleObj instanceof Map ) {
            // Map< ? , ? > shapeModuleMap = ( Map< ? , ? > ) shapeModuleObj;
            // Object getSections = shapeModuleMap.get( "getSections" );
            // shapeModuleObj = replaceFinalValues( shapeModuleObj, getSections );
            // }

            // Map< String, Object > jsonMap = ( Map< String, Object > ) shapeModuleObj;
            // Map< String, String > getSectionsObject = ( Map< String, String > ) jsonMap.get( "getSections" );
            // shapeModuleObj = replaceFinalValues( shapeModuleObj, getSectionsObject );
            shapeModuleObj = removeEmptyValues( shapeModuleObj );
            //
            shapeModuleObj = createDirectoriesAndCopyFiles( shapeModuleObj, jobImpl.getWorkingDir().getPath() + File.separator );
            String optimizationSetupPath = jobImpl.getWorkingDir().getPath() + File.separator + "OptimizationSetup.json";
            try ( Writer writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( optimizationSetupPath ), "utf-8" ) ) ) {
                writer.write( JsonUtils.toFilteredJson( new String[]{}, shapeModuleObj ) );
            } catch ( Exception e ) {
                writeLogs( Level.SEVERE, "Could not write shape module to Setup File: " + e );
                log.error( "Could not write shape module to Setup File: ", e );
            }
            parameters.put( SHAPEMODULE_VARIABLE, optimizationSetupPath );
        }
    }

    /**
     * Gets the experiment number.
     *
     * @param jobImpl
     *         the job impl
     *
     * @return the experiment number
     */
    private String getExperimentNumber( Job jobImpl ) {
        int lastIndex = jobImpl.getName().lastIndexOf( ConstantsString.UNDERSCORE );
        String expNum = jobImpl.getName().substring( lastIndex + 1 );
        if ( org.apache.commons.lang3.StringUtils.isNumeric( expNum ) ) {
            return expNum;
        } else {
            return ConstantsString.EMPTY_STRING;
        }
    }

    /**
     * Removes the all appenders.
     */
    private void removeAllAppenders() {
        LogManager.getLogManager().reset();
    }

    /**
     * Replace ask on run param values.
     *
     * @param userWorkFlow
     *         the user work flow
     * @param askOnRunParametersFromSubWorkFlow
     *         the ask on run parameters from sub work flow
     */
    public void replaceAskOnRunParamValues( UserWorkFlow userWorkFlow, Map< String, Object > askOnRunParametersFromSubWorkFlow ) {
        if ( !askOnRunParametersFromSubWorkFlow.isEmpty() ) {
            for ( final UserWFElement userWFElement : userWorkFlow.getNodes() ) {
                for ( final Field field : userWFElement.getFields() ) {
                    final String param = userWFElement.getName() + ConstantsString.DOT + field.getName();
                    if ( askOnRunParametersFromSubWorkFlow.containsKey( param ) ) {
                        field.setValue( askOnRunParametersFromSubWorkFlow.get( param ) );
                    }
                }
            }
        }
    }

    /**
     * Replace work flow fields.
     *
     * @param userWorkflowelements
     *         the user workflowelements
     * @param jobWorkflowelements
     *         the job workflowelements
     */
    private void replaceWorkFlowFields( List< UserWFElement > userWorkflowelements, List< UserWFElement > jobWorkflowelements ) {
        for ( final UserWFElement userWFElement : userWorkflowelements ) {
            for ( final UserWFElement jobWFElement : jobWorkflowelements ) {
                if ( userWFElement.getKey().contentEquals( jobWFElement.getKey() )
                        && userWFElement.getName().contentEquals( jobWFElement.getName() ) ) {
                    userWFElement.setFields( jobWFElement.getFields() );
                    userWFElement.setExceptions( jobWFElement.getExceptions() );
                }
            }
        }
    }

    /**
     * Run workflow.
     *
     * @param job
     *         the job
     * @param userWorkFlow
     *         the user work flow
     * @param executedElements
     *         the executed elements
     * @param executedElementsIds
     *         the executed elements ids
     */
    public void runWorkflow( final Job job, final UserWorkFlow userWorkFlow, Set< String > executedElementsIds ) {
        try {
            ExecutionResults< String, DecisionObject > errors = runWorkflowWithErrors( job, userWorkFlow, executedElementsIds,
                    new HashMap<>(), null );
            updateJobOnServerIfComplete( job, errors );
            if ( job.isFileRun() ) {
                jobsList.replace( job.getId(), job ); // update file job in list
            }
        } catch ( final Exception ex ) {
            log.error( ERROR, ex );
            writeLogs( Level.SEVERE, ex.getLocalizedMessage() );
            try {
                log.error( JsonUtils.toFilteredJson( new String[]{}, new SusResponseDTO( Boolean.FALSE, ex.getMessage() ) ), ex );
            } catch ( final JsonSerializationException e1 ) {
                log.error( ERROR, e1 );
                writeLogs( Level.SEVERE, e1.getLocalizedMessage() );
            }
        } finally {
            // Full job log and summary log tail start
            if ( job.getStatus().getName().equalsIgnoreCase( WorkflowStatus.FAILED.getValue() ) ) {
                writeLogs( Level.SEVERE, MessagesUtil.getMessage( WFEMessages.WORKFLOW_FAILED ) );
            } else {
                writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_EXECUTED ) );
            }
            prepareLogFileFooter( job, userWorkFlow, executedElementsIds.size() );
            // Full job log and summary log tail end
            removeAllAppenders();
        }
    }

    /**
     * Run workflow with errors.
     *
     * @param job
     *         the job
     * @param userWorkFlow
     *         the user work flow
     * @param executedElements
     *         the executed elements
     * @param executedElementsIds
     *         the executed elements ids
     * @param originalUserWorkFlow
     *         the original user work flow
     * @param parameters
     *         the parameters
     *
     * @return the execution results
     */
    @Override
    public ExecutionResults< String, DecisionObject > runWorkflowWithErrors( final Job job, final UserWorkFlow userWorkFlow,
            Set< String > executedElementsIds, Map< String, Object > parameters, String pythonPath ) {
        Diagraph< String > originalGraph = getGraphWithSingleStartingPoint( userWorkFlow );
        final String pythonExecutionPath = null != pythonPath ? pythonPath : getPythonExecutionPath( job );
        parameters.put( JOB_PYTHON, pythonExecutionPath );
        List< UserWFElement > elements = userWorkFlow.getNodes();
        WorkflowExecutionParameters workflowExecutionParameters = new WorkflowExecutionParameters( userWorkFlow, job, originalGraph,
                elements, new ArrayList<>(), pythonExecutionPath, executedElementsIds );
        workflowExecutionParameters.setLoopStartEndMap( prepareLoopStartEndMap( userWorkFlow ) );
        // update edges for loop Element
        userWorkFlow.setEdges( WFLoopsUtils.rearrangeRelationsForLoops( userWorkFlow.getEdges(), userWorkFlow.getNodes(),
                workflowExecutionParameters.getLoopStartEndMap() ) );
        return executeUserWF( userWorkFlow, job, executedElementsIds, parameters, workflowExecutionParameters, pythonExecutionPath );
    }

    /**
     * Prepare loop start end map.
     *
     * @param userWorkFlow
     *         the user work flow
     *
     * @return the map
     */
    private Map< String, Map< String, String > > prepareLoopStartEndMap( UserWorkFlow userWorkFlow ) {
        Map< String, Map< String, String > > loopStartEndMap = new HashMap<>();
        List< UserWFElement > loopNodes = userWorkFlow.getNodes().stream()
                .filter( userWFElement -> ( userWFElement.getKey().equals( ElementKeys.WFE_FOREACHLOOP.getKey() )
                        || userWFElement.getKey().equals( ElementKeys.WFE_WHILELOOP.getKey() )
                        || userWFElement.getKey().equals( ElementKeys.WFE_UNTILLOOP.getKey() ) ) )
                .toList();
        if ( CollectionUtil.isNotEmpty( loopNodes ) ) {
            // preparing map for loop start/end nodes
            for ( UserWFElement userWFElement : loopNodes ) {
                Map< String, String > startEndMap = new HashMap<>();
                userWFElement.getFields().forEach( field -> {
                    if ( field.getName().equals( "loop-start" ) ) {
                        startEndMap.put( "start", field.getValue().toString() );
                    } else if ( field.getName().equals( "loop-end" ) ) {
                        startEndMap.put( "end", field.getValue().toString() );
                    }
                } );
                loopStartEndMap.put( userWFElement.getId(), startEndMap );
            }

        }
        return loopStartEndMap;
    }

    /**
     * Delete job json.
     *
     * @param job
     *         the job
     */
    private void deleteJobJson( Job job ) {
        try {
            if ( !isLocal( job.getRunsOn().getId().toString() ) ) {
                String jobJsonScriptDirPath = job.getWorkingDir().getPath() + File.separator + "scripts";
                FileUtils.deleteDirectory( new File( jobJsonScriptDirPath ) );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Update design and objective summary from job parameter.
     *
     * @param jobImpl
     *         the job impl
     */
    private void updateDesignAndObjectiveSummaryFromJobParameter( Job jobImpl ) {

        try {
            if ( JobTypeEnums.SCHEME.getKey() == jobImpl.getWorkflow().getWorkflowType() ) {
                writeLogs( Level.INFO, "InProgress Design/Objective Variables in Job Summary" );
                String mainPath;
                String staggingPath;
                if ( isLocal( jobImpl.getRunsOn().getId().toString() ) ) {
                    writeLogs( Level.INFO, " working Dir : " + jobImpl.getWorkingDir().getItems().get( 0 ) );
                    staggingPath = jobImpl.getWorkingDir().getItems().get( 0 );
                    mainPath = jobImpl.getWorkingDir().getItems().get( 0 ) + File.separator + jobImpl.getId();
                } else {

                    staggingPath = getStagingPathOfServer( jobImpl.getServer().getProtocol(), jobImpl.getServer().getHostname(),
                            jobImpl.getServer().getPort(), jobImpl.getRequestHeaders() );
                    String bmwSuggestion = jobImpl.getName() + "_" + jobImpl.getJobInteger();
                    mainPath = staggingPath + File.separator + bmwSuggestion;
                }

                writeLogs( Level.INFO, " working Dir : " + jobImpl.getWorkingDir().getItems().get( 0 ) + File.separator + jobImpl.getId() );
                writeLogs( Level.INFO, "Reading Design/Objective files from : " + mainPath );

                String[] splitName = jobImpl.getName().split( "_" );
                String expNum = splitName[ splitName.length - 1 ];

                Map< String, Object > design = new HashMap<>();
                Map< String, Object > objective = new HashMap<>();
                Map< String, Object > goal = new HashMap<>();

                if ( jobImpl.getDesignSummary() != null && !jobImpl.getDesignSummary().isEmpty() ) {
                    // prepare design summary
                    for ( Entry< String, Object > designSum : jobImpl.getDesignSummary().entrySet() ) {
                        if ( "expNum".equals( designSum.getKey() ) ) {
                            design.put( designSum.getKey(), expNum );
                        } else {
                            design.put( designSum.getKey(), designSum.getValue() );
                        }
                    }
                }

                try {
                    final WorkflowDefinitionDTO jobParamDef = getWorkflowDefinition( jobImpl.getWorkflow().prepareDefinition() );
                    final List< UserWFElement > jobWFElements = setWorkflowElements( jobParamDef );
                    for ( UserWFElement userWFElement : jobWFElements ) {
                        // only io elements templates are being extracted
                        if ( userWFElement.getKey().equals( ElementKeys.IO.getKey() ) ) {
                            // saving design list and objective list for design summary
                            prepareObjectiveVarFromJobParameters( jobImpl, mainPath, objective, userWFElement, staggingPath );
                        }
                    }
                } catch ( Exception e ) {
                    writeLogs( Level.INFO, "Objective Variables Update FAILED : " + e );
                    log.error( "Objective Variables Update FAILED : ", e );
                }

                // setting result and design summary for Job summary TAB Api UI and List
                SchemeSummaryResults schemeSummaryResults = new SchemeSummaryResults();
                schemeSummaryResults.setExperimentNumber( expNum );
                schemeSummaryResults.setDesignSummaryMap( design );
                schemeSummaryResults.setObjectSummaryMap( objective );

                if ( SchemeCategoryEnum.OPTIMIZATION.getKey() == jobImpl.getJobSchemeCategory() ) {
                    try {
                        final String url = prepareURL( "/api/workflow/goal/", jobImpl.getServer() );
                        SusResponseDTO susResponseDTO = SuSClient.getRequest( url + jobImpl.getWorkflow().getId(),
                                prepareHeaders( jobImpl.getRequestHeaders() ) );
                        if ( susResponseDTO != null && susResponseDTO.getData() != null && !susResponseDTO.getData().toString()
                                .isEmpty() ) {
                            float combinedGoal = 0;
                            Map< String, Object > goalMap = ( Map< String, Object > ) JsonUtils.jsonToMap(
                                    susResponseDTO.getData().toString(), goal );
                            for ( Entry< String, Object > goalEntry : goalMap.entrySet() ) {
                                for ( Entry< String, Object > objectiveEntry : objective.entrySet() ) {
                                    if ( goalEntry.getKey().contains( objectiveEntry.getKey() ) ) {
                                        combinedGoal = combinedGoal + ( Float.parseFloat( objectiveEntry.getValue().toString() )
                                                - Float.parseFloat( goalEntry.getValue().toString() ) );
                                    }
                                }
                            }
                            goalMap.put( "combined_goal", combinedGoal );
                            schemeSummaryResults.setGoal( goalMap );
                        } else {
                            schemeSummaryResults.setGoal( ( Map< String, Object > ) goal.put( "goal", "" ) );
                        }
                    } catch ( Exception e ) {
                        log.warn( e.getMessage(), e );
                        schemeSummaryResults.setGoal( ( Map< String, Object > ) goal.put( "goal", "" ) );
                    }
                }

                jobImpl.setResultSummary( JsonUtils.toJsonString( schemeSummaryResults ) );
                writeLogs( Level.INFO, "Design/Objective saved in Job : " + JsonUtils.toJson( schemeSummaryResults.toString() ) );

            }

            // update job only if this is a scheme
            setPropertiesForUrl();
            final String urlNew = prepareURL( properties.getProperty( URL_UPDATE_JOB_BY_ID ), jobImpl.getServer() );
            final String payload = JsonUtils.toFilteredJson( new String[]{}, jobImpl );
            final SusResponseDTO responseDTO = SuSClient.putRequest( urlNew + jobImpl.getId(),
                    prepareHeaders( jobImpl.getRequestHeaders() ), payload );
            writeLogs( Level.INFO, "Job Update Response : " + responseDTO.getMessage() );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            writeLogs( Level.INFO, "Design/Objective Variables Updated FAILED " + e.getMessage() );
        }
    }

    /**
     * Prepare objective var from job parameters.
     *
     * @param jobImpl
     *         the job impl
     * @param mainPath
     *         the main path
     * @param objective
     *         the objective
     * @param userWFElement
     *         the user WF element
     * @param staggingPath
     *         the stagging path
     */
    private void prepareObjectiveVarFromJobParameters( Job jobImpl, String mainPath, Map< String, Object > objective,
            UserWFElement userWFElement, String staggingPath ) {
        List< Field< ? > > fields = userWFElement.getFields();
        for ( Field< ? > field : fields ) {
            if ( field.getTemplateType().equalsIgnoreCase( FieldTemplates.OBJECTIVE_VARIABLE.getValue() ) ) {
                if ( field.getType() != null && field.getType().equals( FieldTypes.REGEX_FILE.getType() ) ) {
                    writeLogs( Level.INFO, "read Regex Objective Variables called" );
                    readRegexVariables( mainPath, objective, field, jobImpl );
                } else if ( field.getType() != null && field.getType().equals( FieldTypes.REGEX_SCAN_SERVER.getType() ) ) {
                    writeLogs( Level.INFO, "read Regex Server Objective Variables called" );
                    readRegexServerVariables( mainPath, objective, field, jobImpl );
                } else if ( field.getType() != null && field.getType().equals( FieldTypes.TEMPLATE_FILE.getType() ) ) {
                    writeLogs( Level.INFO, "read Template Objective Variables called" );
                    readTemplateVariable( mainPath, staggingPath, objective, field, jobImpl );
                } else if ( field.getType() != null && field.getType().equals( FieldTypes.OBJECT_PARSER.getType() ) ) {
                    writeLogs( Level.INFO, "read Parser Objective Variables called" );
                    readParserVariable( mainPath, objective, field.getValue().toString(), jobImpl );
                } else if ( field.getType() != null && field.getType().equals( FieldTypes.TEMPLATE_SCAN_SERVER.getType() ) ) {
                    writeLogs( Level.INFO, "read Template Server Objective Variables called" );
                    readTemplateServerVariables( mainPath, staggingPath, objective, field, jobImpl );
                }
            }
        }
    }

    /**
     * Read parser variable.
     *
     * @param mainPath
     *         the main path
     * @param objective
     *         the objective
     * @param parserId
     *         the parser id
     * @param jobImpl
     *         the job impl
     */
    private void readParserVariable( String mainPath, Map< String, Object > objective, String parserId, Job jobImpl ) {
        writeLogs( Level.INFO, "Reading design Parser : " + mainPath );
        final String selectionURL = prepareURL( "/api/workflow/parser/" + parserId + "/selected/list/value", jobImpl.getServer() );
        writeLogs( Level.INFO, "url is  " + selectionURL );
        final SusResponseDTO responseDTO = SuSClient.postRequest( selectionURL, JsonUtils.toJson( mainPath ),
                prepareHeaders( jobImpl.getRequestHeaders() ) );
        final List< ParserVariableDTO > selectionItems = JsonUtils.jsonToList( JsonUtils.toJson( responseDTO.getData() ),
                ParserVariableDTO.class );
        for ( ParserVariableDTO parserVariableDTO : selectionItems ) {
            objective.put( parserVariableDTO.getVariableName(), parserVariableDTO.getScannedValue() );
        }
    }

    /**
     * Read template variable.
     *
     * @param mainPath
     *         the main path
     * @param staggingPath
     *         the stagging path
     * @param objective
     *         the objective
     * @param field
     *         the field
     * @param jobImpl
     *         the job impl
     */
    private void readTemplateVariable( String mainPath, String staggingPath, Map< String, Object > objective, Field< ? > field,
            Job jobImpl ) {

        TemplateFileDTO templateFile = JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), TemplateFileDTO.class );
        File temp = new File( OSValidator.convertPathToRelitiveOS( templateFile.getFile() ) );
        templateFile.setFile( OSValidator.convertPathToRelitiveOS( mainPath + File.separator + temp.getName() ) );
        String originalFile =
                staggingPath + File.separator + jobImpl.getWorkflowId() + File.separator + "files" + File.separator + temp.getName();
        Map< Integer, List< TemplateVariableDTO > > map = prepareTemplateVarMapForOriginalFileDataExtraction( templateFile );
        Map< Integer, List< TemplateVariableDTO > > lineIndexsMap = prepareTemplateVarMapForWfCreatedDataFile( originalFile, map );
        prepareTemplateVarForObjectiveSummary( objective, templateFile, lineIndexsMap );
        writeLogs( Level.INFO, "Prepration for template summary Complete " );
    }

    /**
     * Prepare template var for objective summary.
     *
     * @param objective
     *         the objective
     * @param templateFile
     *         the template file
     * @param lineIndexsMap
     *         the line indexs map
     */
    private void prepareTemplateVarForObjectiveSummary( Map< String, Object > objective, TemplateFileDTO templateFile,
            Map< Integer, List< TemplateVariableDTO > > lineIndexsMap ) {
        writeLogs( Level.INFO, "Preparing map for objective summary" );
        for ( Entry< Integer, List< TemplateVariableDTO > > orignanMapContents : lineIndexsMap.entrySet() ) {
            Integer lineNumber = orignanMapContents.getKey();
            List< TemplateVariableDTO > originalTemplateVarList = orignanMapContents.getValue();
            String originalFileLine = null;
            try {
                originalFileLine = Files.readAllLines( Paths.get( templateFile.getFile() ) ).get( lineNumber );
            } catch ( IOException e ) {
                log.error( "Failed to read file : ", e );
            }
            int listLength = originalTemplateVarList.size();
            for ( int i = 0; i < listLength; i++ ) {
                TemplateVariableDTO start = originalTemplateVarList.get( i );
                if ( start.getStart() == null ) {
                    break;
                }
                TemplateVariableDTO end = originalTemplateVarList.get( i + 1 );
                int startIndex = originalFileLine.indexOf( start.getMatch() );
                startIndex = ( startIndex + start.getMatch().length() );
                int endItIndex = originalFileLine.indexOf( end.getMatch() );
                if ( startIndex == endItIndex ) {
                    objective.put( start.getVariableName(), end.getMatch() );
                    break;
                } else {
                    String extractedValue;
                    if ( endItIndex <= 0 ) {
                        extractedValue = originalFileLine.substring( startIndex );
                    } else {
                        extractedValue = originalFileLine.substring( startIndex, endItIndex );
                    }
                    objective.put( start.getVariableName(), extractedValue );
                }
            }
        }
    }

    /**
     * Prepare template var map for wf created data file.
     *
     * @param originalFile
     *         the original file
     * @param map
     *         the map
     *
     * @return the map
     */
    private Map< Integer, List< TemplateVariableDTO > > prepareTemplateVarMapForWfCreatedDataFile( String originalFile,
            Map< Integer, List< TemplateVariableDTO > > map ) {
        writeLogs( Level.INFO, "Preparing map for original file data reading" );

        writeLogs( Level.INFO, "Preparing map for proccessed file data reading" );
        Map< Integer, List< TemplateVariableDTO > > lineIndexsMap = new HashMap<>();
        for ( Entry< Integer, List< TemplateVariableDTO > > mapIteration : map.entrySet() ) {
            Integer lineNumber = mapIteration.getKey();
            List< TemplateVariableDTO > lineVariablesList = mapIteration.getValue();

            List< TemplateVariableDTO > templateDTO = lineVariablesList.stream()
                    .sorted( Comparator.comparing( TemplateVariableDTO::getEnd ) ).toList();

            String originalFileLine = null;
            try {
                originalFileLine = Files.readAllLines( Paths.get( originalFile ) ).get( lineNumber );
            } catch ( IOException e ) {
                log.error( "Failed to read file : ", e );
            }

            List< TemplateVariableDTO > mytempList = new ArrayList<>();

            int matchEnd = 0;
            boolean iterationLine = true;
            int count = 1;
            int listSize = templateDTO.size();
            for ( TemplateVariableDTO templateVariableDTO : templateDTO ) {

                TemplateVariableDTO tempVarDto = new TemplateVariableDTO();

                int matchStart;

                if ( iterationLine ) {
                    matchStart = 0;
                    matchEnd = originalFileLine.indexOf( templateVariableDTO.getMatch() );
                    iterationLine = false;
                    tempVarDto.setStart( String.valueOf( matchStart ) );
                    tempVarDto.setEnd( String.valueOf( matchEnd ) );
                    tempVarDto.setMatch( originalFileLine.substring( matchStart, matchEnd ) );
                    tempVarDto.setLineNumber( String.valueOf( lineNumber ) );
                    tempVarDto.setVariableName( templateVariableDTO.getVariableName() );
                    mytempList.add( tempVarDto );
                    if ( count == listSize && matchStart != originalFileLine.length() ) {
                        TemplateVariableDTO tempVarDto2 = new TemplateVariableDTO();
                        String endMarker = originalFileLine.substring( ( matchEnd + templateVariableDTO.getMatch().length() ) );
                        tempVarDto2.setLineNumber( String.valueOf( lineNumber ) );
                        tempVarDto2.setMatch( endMarker );
                        mytempList.add( tempVarDto2 );
                    }
                    matchEnd = matchEnd + templateVariableDTO.getMatch().length();

                } else {
                    matchStart = originalFileLine.indexOf( templateVariableDTO.getMatch(), matchEnd );
                    tempVarDto.setStart( String.valueOf( matchEnd ) );
                    tempVarDto.setEnd( String.valueOf( matchStart ) );
                    tempVarDto.setMatch( originalFileLine.substring( matchEnd, matchStart ) );
                    tempVarDto.setLineNumber( String.valueOf( lineNumber ) );
                    tempVarDto.setVariableName( templateVariableDTO.getVariableName() );
                    mytempList.add( tempVarDto );
                    if ( count == listSize && matchStart != originalFileLine.length() ) {
                        TemplateVariableDTO tempVarDto2 = new TemplateVariableDTO();
                        String endMarker = originalFileLine.substring( matchStart );
                        tempVarDto2.setLineNumber( String.valueOf( lineNumber ) );
                        tempVarDto2.setMatch( endMarker );
                        mytempList.add( tempVarDto2 );
                    }
                    matchEnd = matchStart + templateVariableDTO.getMatch().length();
                }
                count++;
            }
            lineIndexsMap.put( lineNumber, mytempList );
        }
        return lineIndexsMap;
    }

    /**
     * Prepare template var map for original file data extraction.
     *
     * @param templateFile
     *         the template file
     *
     * @return the map
     */
    private Map< Integer, List< TemplateVariableDTO > > prepareTemplateVarMapForOriginalFileDataExtraction( TemplateFileDTO templateFile ) {
        Map< Integer, List< TemplateVariableDTO > > map = new TreeMap<>();
        for ( TemplateVariableDTO templateVariableDTO : templateFile.getVariables() ) {

            if ( map.containsKey( Integer.valueOf( templateVariableDTO.getLineNumber() ) ) ) {
                List< TemplateVariableDTO > listTemplate = map.get( Integer.valueOf( templateVariableDTO.getLineNumber() ) );
                listTemplate.add( templateVariableDTO );
                map.put( Integer.parseInt( templateVariableDTO.getLineNumber() ), listTemplate );
            } else {
                List< TemplateVariableDTO > listTemplate = new ArrayList<>();
                listTemplate.add( templateVariableDTO );
                map.put( Integer.parseInt( templateVariableDTO.getLineNumber() ), listTemplate );
            }
        }
        return map;
    }

    /**
     * Read regex variables.
     *
     * @param mainPath
     *         the main path
     * @param objective
     *         the objective
     * @param field
     *         the field
     * @param jobImpl
     *         the job impl
     */
    private void readRegexVariables( String mainPath, Map< String, Object > objective, Field< ? > field, Job jobImpl ) {
        // TO DO : this code is for automatic genrated file for ObjectiveVar Result file
        ScanFileDTO scanFile = JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), ScanFileDTO.class );
        File temp = new File( OSValidator.convertPathToRelitiveOS( scanFile.getFile() ) );
        scanFile.setFile( OSValidator.convertPathToRelitiveOS( mainPath + File.separator + temp.getName() ) );
        writeLogs( Level.INFO, " objectiveVariable: " + scanFile );
        List< ScanResponseDTO > scanValues;
        if ( isLocal( jobImpl.getRunsOn().getId().toString() ) ) {
            List< MatchedLine > matchedLines = ScanObjectUtil.getMatchingLinesFromFile(
                    OSValidator.convertPathToRelitiveOS( scanFile.getFile() ), scanFile.getVariables() );
            List< ScanObjectDTO > scanList = ScanObjectUtil.prepareScanObjectList( scanFile.getVariables(), matchedLines );
            scanValues = scanObjectiveFile( JsonUtils.jsonToObject( JsonUtils.toJson( scanList ), List.class ) );
            writeLogs( Level.INFO, " scan resultsss conversion  : " + scanValues );
        } else {
            scanValues = scanFile( jobImpl.getServer(), jobImpl.getRequestHeaders(), scanFile );
        }
        writeLogs( Level.INFO, "scanValues  objectiveVariable: " + scanValues );
        int index = 0;
        for ( ObjectVariableDTO scanVar : scanFile.getVariables() ) {
            ScanResponseDTO scanResponse = JsonUtils.linkedMapObjectToClassObject( scanValues.get( index ), ScanResponseDTO.class );
            objective.put( scanVar.getVariableName(), scanResponse.getMatch() );
            index++;
        }
        writeLogs( Level.INFO, "objectiveVariable : " + objective );
    }

    /**
     * Read regex server variables.
     *
     * @param mainPath
     *         the main path
     * @param objective
     *         the objective
     * @param field
     *         the field
     * @param jobImpl
     *         the job impl
     */
    private void readRegexServerVariables( String mainPath, Map< String, Object > objective, Field< ? > field, Job jobImpl ) {

        ScanFileDTO scanFile = getScanFileBySelectionId( field, jobImpl, "regex" );
        File temp = new File( OSValidator.convertPathToRelitiveOS( scanFile.getFile() ) );
        scanFile.setFile( OSValidator.convertPathToRelitiveOS( mainPath + File.separator + temp.getName() ) );
        writeLogs( Level.INFO, " objectiveVariable: " + scanFile );
        List< ScanResponseDTO > scanValues;
        if ( isLocal( jobImpl.getRunsOn().getId().toString() ) ) {
            List< MatchedLine > matchedLines = ScanObjectUtil.getMatchingLinesFromFile(
                    OSValidator.convertPathToRelitiveOS( scanFile.getFile() ), scanFile.getVariables() );
            List< ScanObjectDTO > scanList = ScanObjectUtil.prepareScanObjectList( scanFile.getVariables(), matchedLines );
            scanValues = scanObjectiveFile( JsonUtils.jsonToObject( JsonUtils.toJson( scanList ), List.class ) );
            writeLogs( Level.INFO, " scan resultsss conversion : " + scanValues );
        } else {
            scanValues = scanFile( jobImpl.getServer(), jobImpl.getRequestHeaders(), scanFile );
        }
        writeLogs( Level.INFO, "scanValues objectiveVariable: " + scanValues );
        int index = 0;
        for ( ObjectVariableDTO scanVar : scanFile.getVariables() ) {
            ScanResponseDTO scanResponse = JsonUtils.linkedMapObjectToClassObject( scanValues.get( index ), ScanResponseDTO.class );
            objective.put( scanVar.getVariableName(), scanResponse.getMatch() );
            index++;
        }
        writeLogs( Level.INFO, "objectiveVariable : " + objective );
    }

    /**
     * Read template server variables.
     *
     * @param mainPath
     *         the main path
     * @param staggingPath
     *         the stagging path
     * @param objective
     *         the objective
     * @param field
     *         the field
     * @param jobImpl
     *         the job impl
     */
    private void readTemplateServerVariables( String mainPath, String staggingPath, Map< String, Object > objective, Field< ? > field,
            Job jobImpl ) {
        ScanFileDTO scanFile = getScanFileBySelectionId( field, jobImpl, "template" );
        List< ObjectVariableDTO > objectVariableDTOs = scanFile.getVariables();
        List< TemplateScanDTO > templateScanDTOs = prepareTemplateScanDtoFromObjectVariableDTO( objectVariableDTOs );
        File temp = new File( OSValidator.convertPathToRelitiveOS( scanFile.getFile() ) );
        File objectiveFileStaggingPath = new File( OSValidator.convertPathToRelitiveOS( mainPath + File.separator + temp.getName() ) );
        String originalObjectiveFilePath = copyObjectiveVariableFileToStaging( staggingPath, jobImpl.getWorkflowId(), temp );
        Map< Integer, List< TemplateScanDTO > > map = prepareTemplateScanMap( templateScanDTOs );
        Map< Integer, List< TemplateScanDTO > > lineIndexsMap = prepareTemplateScanMapForSchemeOutput( originalObjectiveFilePath, map );
        prepareTemplateScanForObjectiveSummary( objective, lineIndexsMap, objectiveFileStaggingPath );
        writeLogs( Level.INFO, "Prepration for template summary Complete " );
    }

    /**
     * Copy objective variable file to staging.
     *
     * @param staggingPath
     *         the stagging path
     * @param workflowID
     *         the workflow ID
     * @param tempObjectiveFile
     *         the tempObjectiveFile
     *
     * @return the string
     */
    private String copyObjectiveVariableFileToStaging( String staggingPath, UUID workflowID, File tempObjectiveFile ) {
        String targetPath =
                staggingPath + File.separator + workflowID + File.separator + "files" + File.separator + tempObjectiveFile.getName();
        try {
            Files.copy( Paths.get( tempObjectiveFile.getAbsolutePath() ), Paths.get( targetPath ), StandardCopyOption.REPLACE_EXISTING );
        } catch ( IOException e ) {
            log.error( "objectiveVariable file write failed : ", e );
        }
        return targetPath;
    }

    /**
     * Prepare template scan dto from object variable DTO.
     *
     * @param objectVariableDTOs
     *         the object variable DT os
     *
     * @return the list
     */
    private List< TemplateScanDTO > prepareTemplateScanDtoFromObjectVariableDTO( List< ObjectVariableDTO > objectVariableDTOs ) {
        List< TemplateScanDTO > templateScanDTOs = new ArrayList<>();
        for ( ObjectVariableDTO objectVariableDTO : objectVariableDTOs ) {
            TemplateScanDTO templateScanDTO = new TemplateScanDTO();
            templateScanDTO.setId( objectVariableDTO.getId() );
            templateScanDTO.setVariableName( objectVariableDTO.getVariableName() );
            templateScanDTO.setLineNumber( objectVariableDTO.getHighlight().getLineNumber() );
            templateScanDTO.setStart( objectVariableDTO.getHighlight().getStart() );
            templateScanDTO.setEnd( objectVariableDTO.getHighlight().getEnd() );
            templateScanDTO.setMatch( objectVariableDTO.getHighlight().getMatch() );
            templateScanDTOs.add( templateScanDTO );
        }
        return templateScanDTOs;
    }

    /**
     * Prepare template scan map.
     *
     * @param templateScanDTOs
     *         the template scan DT os
     *
     * @return the map
     */
    private Map< Integer, List< TemplateScanDTO > > prepareTemplateScanMap( List< TemplateScanDTO > templateScanDTOs ) {
        Map< Integer, List< TemplateScanDTO > > map = new TreeMap<>();
        for ( TemplateScanDTO templateScanDTO : templateScanDTOs ) {

            if ( map.containsKey( Integer.valueOf( templateScanDTO.getLineNumber() ) ) ) {
                List< TemplateScanDTO > listTemplate = map.get( Integer.valueOf( templateScanDTO.getLineNumber() ) );
                listTemplate.add( templateScanDTO );
                map.put( Integer.parseInt( templateScanDTO.getLineNumber() ), listTemplate );
            } else {
                List< TemplateScanDTO > listTemplate = new ArrayList<>();
                listTemplate.add( templateScanDTO );
                map.put( Integer.parseInt( templateScanDTO.getLineNumber() ), listTemplate );
            }
        }
        return map;
    }

    /**
     * Prepare template scan map for scheme output.
     *
     * @param originalFile
     *         the original file
     * @param map
     *         the map
     *
     * @return the map
     */
    private Map< Integer, List< TemplateScanDTO > > prepareTemplateScanMapForSchemeOutput( String originalFile,
            Map< Integer, List< TemplateScanDTO > > map ) {
        writeLogs( Level.INFO, "Preparing map for original file data reading" );

        writeLogs( Level.INFO, "Preparing map for proccessed file data reading" );
        Map< Integer, List< TemplateScanDTO > > lineIndexsMap = new HashMap<>();
        for ( Entry< Integer, List< TemplateScanDTO > > mapIteration : map.entrySet() ) {
            Integer lineNumber = mapIteration.getKey();
            List< TemplateScanDTO > lineVariablesList = mapIteration.getValue();

            List< TemplateScanDTO > templateDTO = lineVariablesList.stream().sorted( Comparator.comparing( TemplateScanDTO::getEnd ) )
                    .toList();

            String originalFileLine = null;
            try {
                originalFileLine = Files.readAllLines( Paths.get( originalFile ) ).get( lineNumber );
            } catch ( IOException e ) {
                log.error( "Failed to read file : ", e );
            }

            List< TemplateScanDTO > tempList = new ArrayList<>();

            int matchEnd = 0;
            boolean iterationLine = true;
            int count = 1;
            int listSize = templateDTO.size();
            for ( TemplateScanDTO templateScanDTO : templateDTO ) {

                TemplateScanDTO tempScanDto = new TemplateScanDTO();

                int matchStart;

                if ( iterationLine ) {
                    matchStart = 0;
                    matchEnd = originalFileLine.indexOf( templateScanDTO.getMatch() );
                    iterationLine = false;
                    tempScanDto.setStart( String.valueOf( matchStart ) );
                    tempScanDto.setEnd( String.valueOf( matchEnd ) );
                    tempScanDto.setMatch( originalFileLine.substring( matchStart, matchEnd ) );
                    tempScanDto.setLineNumber( String.valueOf( lineNumber ) );
                    tempScanDto.setVariableName( templateScanDTO.getVariableName() );
                    tempList.add( tempScanDto );
                    if ( count == listSize && matchStart != originalFileLine.length() ) {
                        TemplateScanDTO tempScanDto2 = new TemplateScanDTO();
                        String endMarker = originalFileLine.substring( ( matchEnd + templateScanDTO.getMatch().length() ) );
                        tempScanDto2.setLineNumber( String.valueOf( lineNumber ) );
                        tempScanDto2.setMatch( endMarker );
                        tempList.add( tempScanDto2 );
                    }
                    matchEnd = matchEnd + templateScanDTO.getMatch().length();

                } else {
                    matchStart = originalFileLine.indexOf( templateScanDTO.getMatch(), matchEnd );
                    tempScanDto.setStart( String.valueOf( matchEnd ) );
                    tempScanDto.setEnd( String.valueOf( matchStart ) );
                    tempScanDto.setMatch( originalFileLine.substring( matchEnd, matchStart ) );
                    tempScanDto.setLineNumber( String.valueOf( lineNumber ) );
                    tempScanDto.setVariableName( templateScanDTO.getVariableName() );
                    tempList.add( tempScanDto );
                    if ( count == listSize && matchStart != originalFileLine.length() ) {
                        TemplateScanDTO tempVarDto2 = new TemplateScanDTO();
                        String endMarker = originalFileLine.substring( matchStart );
                        tempVarDto2.setLineNumber( String.valueOf( lineNumber ) );
                        tempVarDto2.setMatch( endMarker );
                        tempList.add( tempVarDto2 );
                    }
                    matchEnd = matchStart + templateScanDTO.getMatch().length();
                }
                count++;
            }
            lineIndexsMap.put( lineNumber, tempList );
        }
        return lineIndexsMap;
    }

    /**
     * Prepare template scan for objective summary.
     *
     * @param objective
     *         the objective
     * @param lineIndexsMap
     *         the line indexs map
     * @param objectiveFile
     *         the objective file
     */
    private void prepareTemplateScanForObjectiveSummary( Map< String, Object > objective,
            Map< Integer, List< TemplateScanDTO > > lineIndexsMap, File objectiveFile ) {
        writeLogs( Level.INFO, "Preparing map for objective summary" );
        for ( Entry< Integer, List< TemplateScanDTO > > originalMapContents : lineIndexsMap.entrySet() ) {
            Integer lineNumber = originalMapContents.getKey();
            List< TemplateScanDTO > originalTemplateScanList = originalMapContents.getValue();
            String originalFileLine = null;
            try {
                originalFileLine = Files.readAllLines( Paths.get( objectiveFile.getAbsolutePath() ) ).get( lineNumber );
            } catch ( IOException e ) {
                log.error( "Failed to read file : ", e );
            }
            int listLength = originalTemplateScanList.size();
            for ( int i = 0; i < listLength; i++ ) {
                TemplateScanDTO start = originalTemplateScanList.get( i );
                if ( start.getStart() == null ) {
                    break;
                }
                TemplateScanDTO end = originalTemplateScanList.get( i + 1 );
                int startIndex = originalFileLine.indexOf( start.getMatch() );
                startIndex = ( startIndex + start.getMatch().length() );
                int endItIndex = originalFileLine.indexOf( end.getMatch() );
                if ( startIndex == endItIndex ) {
                    objective.put( start.getVariableName(), end.getMatch() );
                    break;
                } else {
                    String extractedValue;
                    if ( endItIndex <= 0 ) {
                        extractedValue = originalFileLine.substring( startIndex );
                    } else {
                        extractedValue = originalFileLine.substring( startIndex, endItIndex );
                    }
                    objective.put( start.getVariableName(), extractedValue );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job saveJob( Job jobImpl ) {
        setPropertiesForUrl();
        final String url = prepareURL( properties.getProperty( URL_SAVE_JOB ), jobImpl.getServer() );
        jobImpl.setStatus( new Status( WorkflowStatus.RUNNING ) );

        final String payload = JsonUtils.toFilteredJson( new String[]{}, jobImpl );
        final SusResponseDTO responseDTO = SuSClient.postRequest( url, payload, prepareHeaders( jobImpl.getRequestHeaders() ) );
        if ( !responseDTO.getSuccess() ) {
            throw new SusException( new Exception( responseDTO.getMessage().getContent() ), WorkflowExecutionManagerImpl.class );
        }
        return JsonUtils.linkedMapObjectToClassObject( responseDTO.getData(), JobImpl.class );
    }

    /**
     * Scan objective file.
     *
     * @param scanObject
     *         the scan object
     *
     * @return the list
     */
    public List< ScanResponseDTO > scanObjectiveFile( List< ScanObjectDTO > scanObject ) {
        List< ScanResponseDTO > retList = new ArrayList<>();
        for ( Object scanObjectDTO : scanObject ) {
            ScanObjectDTO scanObj = JsonUtils.jsonToObject( JsonUtils.toJson( ( LinkedHashMap< String, String > ) scanObjectDTO ),
                    ScanObjectDTO.class );

            if ( null != scanObj.getVariableRegex() && !scanObj.getVariableRegex().isEmpty() ) {
                Pattern pattern;
                Pattern regexPattern = Pattern.compile( REGEX );
                Matcher regexMatcher = regexPattern.matcher( scanObj.getVariableRegex() );
                if ( regexMatcher.find() ) {
                    if ( regexMatcher.group( ConstantsInteger.INTEGER_VALUE_TWO ).contains( FLAG_I ) ) {
                        pattern = RegexUtil.preparePattern(
                                RegexUtil.prepareRegex( regexMatcher.group( ConstantsInteger.INTEGER_VALUE_ONE ) ), false );
                    } else {
                        pattern = RegexUtil.preparePattern(
                                RegexUtil.prepareRegex( regexMatcher.group( ConstantsInteger.INTEGER_VALUE_ONE ) ), true );
                    }
                    if ( null == pattern ) {
                        pattern = RegexUtil.preparePattern( scanObj.getVariableRegex(), true );
                    }
                    Matcher matcher = pattern.matcher( scanObj.getLine() );
                    Matcher matcherAllCount = pattern.matcher( scanObj.getLine() );
                    retList.add( prepareResponse( scanObj, matcher, matcherAllCount ) );
                } else {
                    ScanResponseDTO scanResponse = new ScanResponseDTO();
                    scanResponse.setLineNumber( scanObj.getLineNumber() );
                    retList.add( scanResponse );
                }
            } else {
                retList.add( new ScanResponseDTO() );
            }
        }
        return retList;
    }

    /**
     * Prepare response.
     *
     * @param scanObj
     *         the scan obj
     * @param matcher
     *         the matcher
     * @param matcherAllCount
     *         the matcher all count
     *
     * @return the scan response DTO
     */
    private ScanResponseDTO prepareResponse( ScanObjectDTO scanObj, Matcher matcher, Matcher matcherAllCount ) {
        ScanResponseDTO response = new ScanResponseDTO();
        int objectiveMatch = Integer.parseInt( scanObj.getVariableMatch() );
        int objectiveGroup = Integer.parseInt( scanObj.getVariableGroup() );
        int count = 1;
        int matchNumber = ScanObjectUtil.getObjectMatch( matcherAllCount ) + ( objectiveMatch + ConstantsInteger.INTEGER_VALUE_ONE );

        while ( matcher.find() ) {
            if ( scanObj.getVariableMatch().isEmpty() || ConstantsInteger.INTEGER_VALUE_ZERO == objectiveMatch ) {
                ScanObjectUtil.setFirstMatch( scanObj, matcher, response );
                break;
            } else if ( objectiveMatch < 0 && matchNumber == count ) {
                // Objective Match is a negative number i.e -1 = get last match
                prepareMatchResponse( matcher, matchNumber, response, objectiveGroup );
                response.setLineNumber( scanObj.getLineNumber() );
                break;
            } else {
                if ( objectiveMatch == count ) {
                    try {
                        response.setMatch( matcher.group( objectiveGroup ) );
                        response.setStart( Integer.toString( matcher.start( objectiveGroup ) ) );
                        response.setEnd( Integer.toString( matcher.end( objectiveGroup ) ) );
                    } catch ( Exception e ) {
                        log.warn( e.getMessage() );
                        response.setMatch( matcher.group() );
                        response.setStart( Integer.toString( matcher.start() ) );
                        response.setEnd( Integer.toString( matcher.end() ) );
                    }
                    response.setLineNumber( scanObj.getLineNumber() );
                }
            }
            count++;
        }
        return response;
    }

    /**
     * Prepare match response.
     *
     * @param matcher
     *         the matcher
     * @param matchNumber
     *         the match number
     * @param response
     *         the response
     * @param objectiveGroup
     *         the objective group
     */
    private void prepareMatchResponse( Matcher matcher, int matchNumber, ScanResponseDTO response, int objectiveGroup ) {
        response.setMatch( matcher.group( matchNumber ) );
        try {
            response.setStart( Integer.toString( matcher.start( objectiveGroup ) ) );
            response.setEnd( Integer.toString( matcher.end( objectiveGroup ) ) );
        } catch ( Exception e ) {
            log.warn( e.getMessage() );
            response.setStart( Integer.toString( matcher.start() ) );
            response.setEnd( Integer.toString( matcher.end() ) );
        }
    }

    /**
     * Save relation by extracting nodes.
     *
     * @param jobParameters
     *         the job parameters
     * @param job
     *         the job
     */
    private void saveRelationByExtractingNodes( JobParameters jobParameters, Job job ) {
        final WorkflowDefinitionDTO jobParamDef = getWorkflowDefinition( jobParameters.getWorkflow().prepareDefinition() );
        final List< UserWFElement > jobWFElements = setWorkflowElements( jobParamDef );
        final ArrayList< String > preparedSelectionList = new ArrayList<>();
        writeLogs( Level.INFO, "extracting elements for relation type" );
        for ( final UserWFElement userWFElement : jobWFElements ) {
            final List< Field< ? > > dsa = userWFElement.getFields();
            for ( final Field< ? > field : dsa ) {

                if ( field.getType().equals( FieldTypes.OBJECT.getType() ) && field.getValue() != null
                        && !field.getValue().toString().isEmpty() ) {
                    log.info( "inner fiels : " + field.getLabel() );
                    log.info( "inner fiels : " + field.getValue() );
                    preparedSelectionList.add( field.getValue().toString() );
                }
            }
        }

        log.info( "selectionIds :" + preparedSelectionList );

        final Set< String > uniqueSelectionIds = new LinkedHashSet<>( preparedSelectionList );
        final List< String > selctionId = new ArrayList<>( uniqueSelectionIds );

        final ArrayList< String > prepareItemsList = new ArrayList<>();
        writeLogs( Level.INFO, "preparing selection items" );

        for ( final String id : selctionId ) {
            if ( !id.startsWith( "{{" ) ) {
                // calling selection api
                final String selectionURL = prepareURL( "/api/selection/" + id, jobParameters.getServer() );
                final SusResponseDTO responseDTO = SuSClient.getRequest( selectionURL,
                        prepareHeaders( jobParameters.getRequestHeaders() ) );
                final List< String > selectionItems = JsonUtils.jsonToList( JsonUtils.toJson( responseDTO.getData() ), String.class );

                if ( CollectionUtil.isNotEmpty( selectionItems ) ) {
                    prepareItemsList.addAll( selectionItems );
                }
            }
        }

        writeLogs( Level.INFO, "preparing relation object" );
        final Set< String > prepareObjIds = new LinkedHashSet<>( prepareItemsList );
        final ArrayList< String > objectIds = new ArrayList<>( prepareObjIds );
        for ( final String oId : objectIds ) {
            writeLogs( Level.INFO, "intializing relation object" );
            Relation relation = new Relation( job.getId(), UUID.fromString( oId ), AuditTrailRelationType.RELATION_USED );
            writeLogs( Level.INFO, "saving relation" );
            final String realtionURL = prepareURL( "/api/workflow/create/relation", jobParameters.getServer() );
            try {
                SuSClient.postRequest( realtionURL, JsonUtils.objectToJson( relation ),
                        prepareHeaders( jobParameters.getRequestHeaders() ) );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                writeLogs( Level.INFO, "relation of " + oId + " failed to save" );
                continue;
            }
            writeLogs( Level.INFO, "relation saved" );
        }
    }

    /**
     * Script field validation.
     *
     * @param userWFElement
     *         the user WF element
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *         the interrupted exception
     */
    private void scriptFieldValidation( final UserWFElement userWFElement ) throws IOException, InterruptedException {
        for ( final Field< ? > field : userWFElement.getFields() ) {
            if ( field.getType().equals( FieldTypes.SELECTION.getType() ) ) {
                // either in nix
                if ( org.apache.commons.lang3.StringUtils.containsIgnoreCase( System.getProperty( ConstantsString.OS_NAME ),
                        ConstantsString.OS_LINUX ) && !field.getValue().toString().equals( ConstantsScriptType.BASH ) ) {
                    throw new SusException( new Exception( "Required command not supported : " + field.getValue().toString() ) );
                }
                // or in win
                if ( org.apache.commons.lang3.StringUtils.containsIgnoreCase( System.getProperty( ConstantsString.OS_NAME ),
                        ConstantsString.OS_WINDOWS ) && field.getValue().toString().equals( ConstantsScriptType.BASH )
                        && !checkBashOnWindow() ) {
                    throw new SusException( new Exception( "Required command not supported : " + field.getValue().toString() ) );
                }
            }
        }
    }

    /**
     * Sets the properties for url.
     */
    private void setPropertiesForUrl() {
        try {
            properties.load( WorkflowExecutionManagerImpl.class.getClassLoader().getResourceAsStream( ConstantsString.WF_ENGINE ) );
        } catch ( final IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.FILE_PATH_NOT_EXIST, ConstantsString.WF_ENGINE ) ),
                    WorkflowExecutionManagerImpl.class );
        }
    }

    /**
     * Sets the wf logger.
     *
     * @param wfLogger
     *         the new wf logger
     */
    public void setWfLogger( WfLogger wfLogger ) {
        wfLoggerSum = wfLogger;
    }

    /**
     * Sets the workflow elements.
     *
     * @param def
     *         the def
     *
     * @return the list
     */
    private List< UserWFElement > setWorkflowElements( WorkflowDefinitionDTO def ) {
        final List< UserWFElement > userElements = new ArrayList<>();
        for ( final WorkflowElement workflowElement : def.getElements().getNodes() ) {
            final UserWFElement element = workflowElement.getData();
            if ( ElementKeys.getkeys().contains( element.getKey() ) ) {
                userElements.add( element );
            } else {
                final List< NodeEdge > edges = def.getElements().getEdges();
                edges.removeIf(
                        ed -> ed.getData().getSource().equals( element.getId() ) || ed.getData().getTarget().equals( element.getId() ) );
            }
        }
        return userElements;

    }

    /**
     * Sets the workflow owner.
     *
     * @param dto
     *         the dto
     *
     * @return the user work flow
     */
    private UserWorkFlow setWorkflowOwner( LatestWorkFlowDTO dto ) {
        final UserWorkFlow model = new UserWorkflowImpl();
        if ( null != dto.getId() ) {
            model.setId( dto.getId().toString() );
        }
        if ( null != dto.getCreatedBy() ) {
            final UserImpl createdBy = new UserImpl();
            createdBy.setUid( dto.getCreatedBy().getUserUid() );
            model.setOwner( createdBy );
        }
        model.setName( dto.getName() );
        return model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopAllJobs( String userName ) {
        boolean allJobKilled = true;
        for ( final Job job : jobsList.values() ) {
            final boolean isJobStop = stopJobExecution( job.getId().toString(), userName );
            if ( !isJobStop ) {
                allJobKilled = false;
            }
        }

        return allJobKilled;
    }

    /**
     * Stop job execution.
     *
     * @param jobId
     *         the job id
     * @param userName
     *         the user name
     *
     * @return true, if successful
     */
    @SuppressWarnings( "unlikely-arg-type" )
    @Override
    public boolean stopJobExecution( String jobId, String userName ) {

        boolean isStop = false;
        if ( StringUtils.isNotNullOrEmpty( jobId ) && StringUtils.validateUUIDString( jobId ) ) {

            final ExecutorServiceManager serviceManager = jobsMap.get( UUID.fromString( jobId ) );
            if ( serviceManager.getExecutorService() != null ) {
                writeLogs( Level.FINE, "Going to stop executor : " + jobId );

                final Job stopFileJob = jobsList.get( UUID.fromString( jobId ) );
                if ( stopFileJob != null ) {
                    if ( stopFileJob.getStatus().getId() == WorkflowStatus.COMPLETED.getKey() ) {
                        writeLogs( Level.FINE, "Job is ALready completed Cannot stop completed job" );
                        return false;
                    }
                    updateJobOnServerIfKilledExernally( stopFileJob, userName );
                    isStop = true;
                }

                serviceManager.getExecutorService().shutdownNow();
                jobsMap.remove( UUID.fromString( jobId ) );
                jobsList.remove( UUID.fromString( jobId ) );
                writeLogs( Level.INFO, "Job stop " );

            }
        }
        return isStop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateJob( Job jobImpl ) {
        // job updating preprations
        setPropertiesForUrl();
        final String url = prepareURL( properties.getProperty( URL_UPDATE_JOB_BY_ID ), jobImpl.getServer() );
        final String payload = JsonUtils.toFilteredJson( new String[]{}, jobImpl );
        final SusResponseDTO responseDTO = SuSClient.putRequest( url + jobImpl.getId(), prepareHeaders( jobImpl.getRequestHeaders() ),
                payload );

        jobsList.replace( jobImpl.getId(), jobImpl );
        return responseDTO.getSuccess();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateJobLog( Job jobImpl ) {
        // job updating preprations
        setPropertiesForUrl();
        final String url = prepareURL( properties.getProperty( UPDATE_LOG_OF_JOB ), jobImpl.getServer() );
        final String payload = JsonUtils.toFilteredJson( new String[]{}, jobImpl );
        final SusResponseDTO responseDTO = SuSClient.putRequest( url + jobImpl.getId(), prepareHeaders( jobImpl.getRequestHeaders() ),
                payload );
        return responseDTO.getSuccess();
    }

    /**
     * Update job on server if complete.
     *
     * @param jobImpl
     *         the job impl
     * @param errors
     *         the errors
     */
    private void updateJobOnServerIfComplete( Job jobImpl, ExecutionResults< String, DecisionObject > errors ) {

        if ( jobImpl.getStatus().getName().equals( WorkflowStatus.ABORTED.getValue() ) ) {
            writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_ABORTED ) );
            deleteJobTempFolder( jobImpl );
        } else if ( errors.hasAnyResult() ) {
            JobLog.addLog( jobImpl.getId(),
                    new LogRecord( ConstantsMessageTypes.ERROR, MessagesUtil.getMessage( WFEMessages.WORKFLOW_FAILED ) ) );
            jobImpl.setLog( JobLog.getlogListByJobId( jobImpl.getId() ) );
            writeLogs( Level.SEVERE, MessagesUtil.getMessage( WFEMessages.WORKFLOW_FAILED ) );
            jobImpl.setStatus( new Status( WorkflowStatus.FAILED ) );
            jobImpl.setCompletionTime( new Date() );
            JobLog.addLog( jobImpl.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessagesUtil.getMessage( WFEMessages.TOTAL_EXECUTION_TIME, getExecutionTime( jobImpl ) ) ) );
            if ( !jobImpl.isFileRun() ) {
                updateJob( jobImpl );
            }
            writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.UPDATE_JOB, jobImpl.getName(), jobImpl.getStatus().getName() ) );
            if ( !( checkManageLicense( jobImpl ) && isManagePermission( jobImpl, jobImpl.getWorkflowId().toString() ) ) ) {
                deleteJobTempFolder( jobImpl );
            }
        } else {
            jobImpl.setCompletionTime( new Date() );
            JobLog.addLog( jobImpl.getId(),
                    new LogRecord( ConstantsMessageTypes.SUCCESS, MessagesUtil.getMessage( WFEMessages.WORKFLOW_EXECUTED ) ) );

            JobLog.addLog( jobImpl.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessagesUtil.getMessage( WFEMessages.TOTAL_EXECUTION_TIME, getExecutionTime( jobImpl ) ) ) );
            jobImpl.setLog( JobLog.getlogListByJobId( jobImpl.getId() ) );
            writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_EXECUTED ) );
            jobImpl.setStatus( new Status( WorkflowStatus.COMPLETED ) );
            if ( !jobImpl.isFileRun() ) {
                updateJob( jobImpl );
                removeEnvIfExist( jobImpl );
                writeLogs( Level.INFO, "Updating Design/Objective Variables in Job Summary :" + jobImpl.getId() );
                updateDesignAndObjectiveSummaryFromJobParameter( jobImpl );
            } else {
                writeLogs( Level.INFO,
                        MessagesUtil.getMessage( WFEMessages.UPDATE_JOB, jobImpl.getName(), jobImpl.getStatus().getName() ) );

            }
            deleteJobTempFolder( jobImpl );
        }

        if ( null != jobImpl.getRunsOn() ) {
            boolean isLocal =
                    jobImpl.getRunsOn().getId().toString().equals( LocationsEnum.LOCAL_LINUX.getId() ) || jobImpl.getRunsOn().getId()
                            .toString().equals( LocationsEnum.LOCAL_WINDOWS.getId() );
            if ( jobImpl.getJobType() == JobTypeEnums.WORKFLOW.getKey() && !isLocal ) {
                final String url =
                        jobImpl.getServer().getProtocol() + jobImpl.getServer().getHostname() + ConstantsString.COLON + jobImpl.getServer()
                                .getPort() + "/api/auth/jobexpire";
                Map< String, String > map = new HashMap<>();
                map.put( "token", jobImpl.getRequestHeaders().getJobAuthToken() );
                SuSClient.postRequest( url, JsonUtils.toJson( map ), prepareHeaders( jobImpl.getRequestHeaders() ) );
            }
        }
        jobsList.replace( jobImpl.getId(), jobImpl ); // update file job in list
    }

    /**
     * Removes the env if exist.
     *
     * @param jobImpl
     *         the job impl
     */
    private void removeEnvIfExist( Job jobImpl ) {
        try {
            File requirementFilePath = new File( jobImpl.getWorkingDir().getPath() + ConstantsString.FORWARD_SLASH + "requirements.txt" );
            if ( Files.exists( requirementFilePath.toPath() ) ) {
                FileUtils.delete( requirementFilePath );
            }
            File envPath = new File( jobImpl.getWorkingDir().getPath() + ConstantsString.FORWARD_SLASH + jobImpl.getName() );
            if ( Files.exists( envPath.toPath() ) ) {
                FileUtils.deleteDirectory( envPath );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Check manage license.
     *
     * @param job
     *         the job
     *
     * @return true, if successful
     */
    private boolean checkManageLicense( Job job ) {
        final String permUrl =
                job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                        + "/api/system/license/check/" + SimuspaceFeaturesEnum.WORKFLOW_MANAGER.getKey();
        final SusResponseDTO request = SuSClient.getRequest( permUrl, prepareHeaders( job.getRequestHeaders() ) );
        return null != request.getData() && Boolean.parseBoolean( request.getData().toString() );
    }

    /**
     * Gets the execution time.
     *
     * @param jobImpl
     *         the job impl
     *
     * @return the execution time
     */
    public String getExecutionTime( Job jobImpl ) {
        long timeDifference = jobImpl.getCompletionTime().getTime() - jobImpl.getSubmitTime().getTime();
        long sec = ( timeDifference / 1000 ) % 60;
        long min = ( timeDifference / ( 1000 * 60 ) ) % 60;
        long hour = ( timeDifference / ( 1000 * 60 * 60 ) ) % 24;
        return hour + "h:" + min + "m:" + sec + "s";
    }

    /**
     * Update job log and status to fail.
     *
     * @param jobImpl
     *         the job impl
     * @param errorMsg
     *         the error msg
     *
     * @return true, if successful
     */
    private boolean updateJobLogAndStatusToFail( Job jobImpl, String errorMsg ) {
        JobLog.addLog( jobImpl.getId(),
                new LogRecord( ConstantsMessageTypes.ERROR, MessagesUtil.getMessage( WFEMessages.WORKFLOW_FAILED ) ) );
        JobLog.addLog( jobImpl.getId(), new LogRecord( ConstantsMessageTypes.ERROR, errorMsg ) );
        if ( null == jobImpl.getCompletionTime() ) {
            jobImpl.setCompletionTime( new Date() );
        }
        JobLog.addLog( jobImpl.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                MessagesUtil.getMessage( WFEMessages.TOTAL_EXECUTION_TIME, getExecutionTime( jobImpl ) ) ) );
        jobImpl.setLog( JobLog.getlogListByJobId( jobImpl.getId() ) );

        writeLogs( Level.SEVERE, errorMsg );
        writeLogs( Level.SEVERE, MessagesUtil.getMessage( WFEMessages.WORKFLOW_FAILED ) );
        jobImpl.setStatus( new Status( WorkflowStatus.FAILED ) );
        if ( !jobImpl.isFileRun() ) {
            updateJob( jobImpl );
        }
        return true;
    }

    /**
     * Update job on server if killed exernally.
     *
     * @param jobImpl
     *         the job impl
     * @param userName
     *         the user name
     */
    private void updateJobOnServerIfKilledExernally( Job jobImpl, String userName ) {

        try {
            jobImpl.setCompletionTime( new Date() );
            JobLog.addLog( jobImpl.getId(),
                    new LogRecord( ConstantsMessageTypes.ERROR, MessagesUtil.getMessage( WFEMessages.WORKFLOW_KILLED_EXTERNALLY ),
                            new Date() ) );
            JobLog.addLog( jobImpl.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessageBundleFactory.getDefaultMessage( Messages.JOB_STOPPED_BY.getKey(), userName ), new Date() ) );
            JobLog.addLog( jobImpl.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessagesUtil.getMessage( WFEMessages.TOTAL_EXECUTION_TIME, getExecutionTime( jobImpl ) ) ) );
            jobImpl.setLog( JobLog.getlogListByJobId( jobImpl.getId() ) );

            writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_KILLED_EXTERNALLY ) );

            jobImpl.setStatus( new Status( WorkflowStatus.ABORTED ) );

            if ( !jobImpl.isFileRun() ) {

                updateJob( jobImpl );
            }

            writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.UPDATE_JOB, jobImpl.getName(), jobImpl.getStatus().getName() ) );

        } catch ( final SusException ex ) {
            log.error( ex.getMessage(), ex );
            writeLogs( Level.SEVERE, ex.getMessage() );

        }
    }

    /**
     * User work flow script validation.
     *
     * @param userWorkFlow
     *         the user work flow
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *         the interrupted exception
     */
    private void userWorkFlowScriptValidation( UserWorkFlow userWorkFlow ) throws IOException, InterruptedException {
        for ( final UserWFElement userWFElement : userWorkFlow.getNodes() ) {
            if ( userWFElement.getKey().equals( ConstantsElementKey.WFE_SCRIPT ) ) {
                scriptFieldValidation( userWFElement );
            }
        }
    }

    /**
     * Validate config.
     *
     * @param config
     *         the config
     *
     * @return the notification
     */
    private Notification validateConfig( WorkflowConfiguration config ) {

        final Notification notification = config.validate();

        if ( ( notification != null ) && !notification.getErrors().isEmpty() ) {
            writeLogs( Level.SEVERE, notification.getErrors().toString() );
            throw new SusException( new Exception( "Error message: Config " + notification.getErrors().toString() ), getClass() );
        }

        writeLogs( Level.INFO, "Config file validation done" );

        return notification;
    }

    /**
     * Validate job definations over workflow defination.
     *
     * @param jobParameters
     *         the job parameters
     * @param userWorkFlow
     *         the user work flow
     * @param staggingPath
     *         the stagging path
     *
     * @return the map
     */
    private Map< String, Map< String, Object > > validateJobDefinationsOverWorkflowDefination( JobParameters jobParameters,
            final UserWorkFlow userWorkFlow, String staggingPath ) {
        final WorkflowDefinitionDTO jobParamDef = getWorkflowDefinition( jobParameters.getWorkflow().prepareDefinition() );

        final List< UserWFElement > jobWFElements = setWorkflowElements( jobParamDef );

        log.info( JsonUtils.toFilteredJson( new String[]{},
                new SusResponseDTO( Boolean.TRUE, MessagesUtil.getMessage( WFEMessages.WORKFLOW_VALIDATED_JOB_IS_STARTING ) ) ) );
        writeLogs( Level.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_VALIDATED ) );

        log.info( JsonUtils.toFilteredJson( new String[]{},
                ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ), 0 ) ) );

        final Map< String, Map< String, Object > > allWFIOParameters = getAllInputOutputParametersOfWorkflow( jobParameters,
                userWorkFlow.getNodes(), null );

        final Map< String, Map< String, Object > > allJOBIOParameters = getAllInputOutputParametersOfWorkflow( jobParameters, jobWFElements,
                staggingPath );

        writeJobParametersToLogFile( allJOBIOParameters );

        validateWorkflowAndInputParameteres( allWFIOParameters, allJOBIOParameters );
        replaceWorkFlowFields( userWorkFlow.getNodes(), jobWFElements );
        return allJOBIOParameters;
    }

    /**
     * Write job parameters to log file.
     *
     * @param allJOBIOParameters
     *         the all JOBIO parameters
     */
    private void writeJobParametersToLogFile( Map< String, Map< String, Object > > allJOBIOParameters ) {
        writeLogs( Level.INFO, "All job IO parameters: " );
        for ( Entry< String, Map< String, Object > > entry : allJOBIOParameters.entrySet() ) {
            writeLogs( Level.INFO, "\tElement Name: " + entry.getKey() );
            for ( Entry< String, Object > ientry : entry.getValue().entrySet() ) {
                writeLogs( Level.INFO, "\t\tField Name: " + ientry.getKey() );
                if ( ientry.getKey().contains( ".script" ) ) {
                    writeLogs( Level.INFO, "\t\tField Value: _SKIPPED_" );
                } else {
                    writeLogs( Level.INFO, "\t\tField Value: " + ientry.getValue() );
                }
            }
        }
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
                workflowId + ConstantsString.COLON + PermissionMatrixEnum.EXECUTE.getValue( EnginePropertiesManager.hasTranslation(),
                        job.getRequestHeaders().getToken() ), prepareHeaders( job.getRequestHeaders() ) );
        if ( !Boolean.parseBoolean( request.getData().toString() ) ) {
            throw new SusException(
                    MessageBundleFactory.getDefaultMessage( Messages.NO_RIGHTS_TO_EXECUTE.getKey(), job.getWorkflow().getName() ) );
        }
    }

    /**
     * Checks if is manage permission.
     *
     * @param job
     *         the job
     * @param workflowId
     *         the workflow id
     *
     * @return true, if is manage permission
     */
    private boolean isManagePermission( Job job, String workflowId ) {
        final String permUrl =
                job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                        + "/api/system/permissions/permitted";
        final SusResponseDTO request = SuSClient.postRequest( permUrl,
                workflowId + ConstantsString.COLON + PermissionMatrixEnum.MANAGE.getValue( EnginePropertiesManager.hasTranslation(),
                        job.getRequestHeaders().getToken() ), prepareHeaders( job.getRequestHeaders() ) );
        return Boolean.parseBoolean( request.getData().toString() );
    }

    /**
     * Validate workflow.
     *
     * @param userWorkFlow
     *         the user work flow
     * @param config
     *         the config
     *
     * @return the notification
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *         the interrupted exception
     */
    private Notification validateWorkflow( UserWorkFlow userWorkFlow, WorkflowConfiguration config )
            throws IOException, InterruptedException {

        if ( userWorkFlow.getNodes() != null ) {
            // user WF Script validation
            userWorkFlowScriptValidation( userWorkFlow );
        }
        // user WF validation
        final Notification notify = userWorkFlow.validate( config );
        if ( ( notify != null ) && !notify.getErrors().isEmpty() ) {
            writeLogs( Level.SEVERE, notify.getErrors().toString() );

            throw new SusException( new Exception( "Error message: Workflow " + notify.getErrors().toString() ), getClass() );
        }
        writeLogs( Level.FINE, "User Workflow file validation done" );

        return notify;
    }

    /**
     * Validate workflow and input parameteres.
     *
     * @param allWFIOParameters
     *         the all WFIO parameters
     * @param allJOBIOParameters
     *         the all JOBIO parameters
     */
    private void validateWorkflowAndInputParameteres( Map< String, Map< String, Object > > allWFIOParameters,
            Map< String, Map< String, Object > > allJOBIOParameters ) {

        if ( allWFIOParameters.size() != allJOBIOParameters.size() ) {
            throw new SusException(
                    new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_AND_JOB_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH ) ),
                    getClass() );
        }

        for ( final Entry< String, Map< String, Object > > jobElementEntry : allJOBIOParameters.entrySet() ) {

            if ( !allWFIOParameters.containsKey( jobElementEntry.getKey() ) ) {
                throw new SusException( new Exception(
                        MessagesUtil.getMessage( WFEMessages.JOB_DOES_NOT_HAVE_ELEMENT_FIELDS_IN_WORKFLOW, jobElementEntry.getKey() ) ),
                        getClass() );
            }

            if ( jobElementEntry.getValue().size() != allWFIOParameters.get( jobElementEntry.getKey() ).size() ) {
                throw new SusException( new Exception(
                        MessagesUtil.getMessage( WFEMessages.JOB_DOES_AND_WORKFLOW_FIELDS_NOT_SAME_FOR_ELEMENT,
                                jobElementEntry.getKey() ) ), getClass() );
            }

            for ( final Entry< String, Object > field : jobElementEntry.getValue().entrySet() ) {

                if ( !allWFIOParameters.get( jobElementEntry.getKey() ).containsKey( field.getKey() ) ) {
                    throw new SusException( new Exception(
                            MessagesUtil.getMessage( WFEMessages.JOB_INPUT_FIELD_DOES_NOT_MATCH_IN_WORKFLOW_ELEMENT, field.getKey(),
                                    jobElementEntry.getKey() ) ), getClass() );
                }

            }

        }
    }

    /**
     * Write debug logs.
     *
     * @param message
     *         the message
     */
    private void writeDebugLogs( String message ) {
        getWfLogger().debug( message );
        log.debug( message );
    }

    /**
     * Write error logs.
     *
     * @param message
     *         the message
     */
    private void writeErrorLogs( String message ) {
        getWfLogger().error( message );
        getWfLoggerSummary().error( message );
        log.error( message );
    }

    /**
     * Write info logs.
     *
     * @param message
     *         the message
     */
    private void writeInfoLogs( String message ) {
        getWfLogger().info( message );
        getWfLoggerSummary().info( message );
        log.info( message );
    }

    /**
     * Write logs.
     *
     * @param level
     *         the level
     * @param message
     *         the message
     */
    private void writeLogs( Level level, String message ) {
        if ( level.equals( Level.INFO ) ) {
            writeInfoLogs( message );
        } else if ( level.equals( Level.FINE ) ) {
            writeDebugLogs( message );
        } else if ( level.equals( Level.SEVERE ) ) {
            writeErrorLogs( message );
        }
    }

}