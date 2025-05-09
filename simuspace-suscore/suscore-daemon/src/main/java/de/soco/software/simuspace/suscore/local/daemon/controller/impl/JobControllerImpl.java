package de.soco.software.simuspace.suscore.local.daemon.controller.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.RouterConfigItem;
import de.soco.software.simuspace.suscore.data.model.RouterConfigList;
import de.soco.software.simuspace.suscore.data.utility.ContextUtilExternal;
import de.soco.software.simuspace.suscore.local.daemon.controller.JobController;
import de.soco.software.simuspace.suscore.local.daemon.manager.SuscoreDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.manager.WorkflowDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.model.ItemsDTO;
import de.soco.software.simuspace.suscore.local.daemon.model.WorkflowEndPoints;
import de.soco.software.simuspace.suscore.local.daemon.properties.DaemonPropertiesManager;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.dto.WorkflowModel;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.SystemWorkflow;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.FileJobDTO;

/**
 * This Class Behave like a service which consist of the endpoints for Job execution and other Job related tasks.
 *
 * @author Nosheen.Sharif
 */
@RestController
@RequestMapping( value = WorkflowEndPoints.API_JOB )
public class JobControllerImpl implements JobController {

    /**
     * The Constant DISCARD_JOB.
     */
    private static final String DISCARD_JOB_TITLE_KEY = "4100047x4";

    /**
     * The Constant SUS_JOBS_TABLE_UI_API.
     */
    private static final String SUS_JOBS_TABLE_UI_API = "/sus/jobs/ui";

    /**
     * The Constant SUS_JOBS_CONTEXT.
     */
    private static final String SUS_JOBS_CONTEXT = "/sus/jobs/context";

    /**
     * The Constant STRING_ENCODING.
     */
    private static final String STRING_ENCODING = "UTF-8";

    /**
     * The Constant ID_PARAM.
     */
    private static final String ID_PARAM = "{id}";

    /**
     * The Constant ROUTER_PATH.
     */
    private static final String ROUTER_PATH = "sus/router.json";

    /**
     * The Constant VIEW_ID_PARAM.
     */
    private static final String VIEW_ID_PARAM = "{viewId}";

    /**
     * The Constant JOB_END_POINT.
     */
    private static final String JOB_END_POINT = "job";

    /**
     * The Constant STOP_JOB_END_POINT.
     */
    private static final String STOP_JOB_END_POINT = "/{jobId}/stop";

    /**
     * The Constant PAUSE_JOB_END_POINT.
     */
    private static final String PAUSE_JOB_END_POINT = "/{jobId}/pause";

    /**
     * The Constant RESUME_JOB_END_POINT.
     */
    private static final String RESUME_JOB_END_POINT = "/{jobId}/resume";

    /**
     * The Constant STOP_JOB_CONTEXT_URL.
     */
    private static final String STOP_JOB_CONTEXT_URL = "job/stop/{id}";

    /**
     * The Constant PAUSE_JOB_CONTEXT_URL.
     */
    private static final String PAUSE_JOB_CONTEXT_URL = "job/pause/{id}";

    /**
     * The Constant RESUME_JOB_CONTEXT_URL.
     */
    private static final String RESUME_JOB_CONTEXT_URL = "job/resume/{id}";

    /**
     * The Constant RERUN_JOB_CONTEXT_URL.
     */
    private static final String RERUN_JOB_CONTEXT_URL = "rerun/job/{id}";

    /**
     * The Constant DISCARD_JOB_CONTEXT_URL.
     */
    private static final String DISCARD_JOB_CONTEXT_URL = "discard/job/{id}";

    /**
     * The Constant STOP_JOB_CONTEXT_TITLE.
     */
    private static final String STOP_JOB_CONTEXT_TITLE_KEY = "4100041x4";

    /**
     * The Constant RERUN_JOB_CONTEXT_TITLE.
     */
    private static final String RERUN_JOB_CONTEXT_TITLE_KEY = "4100046x4";

    private static final String PAUSE_JOB_CONTEXT_TITLE_KEY = "4100144x4";

    private static final String RESUME_JOB_CONTEXT_TITLE_KEY = "4100145x4";

    /**
     * The Constant MY_JOBS_ENDPOINT.
     */
    private static final String MY_JOBS_ENDPOINT = "/list/myjobs";

    /**
     * The Constant GET_JOB_END_POINT.
     */
    private static final String GET_JOB_END_POINT = "/properties/data";

    /**
     * The Constant SLASH.
     */
    private static final String SLASH = "/";

    /**
     * The Constant RUN_MODE_SERVE.
     */
    private static final String RUN_MODE_SERVER = "server";

    /**
     * The Constant RUN_MODE_LOCAL.
     */
    private static final String RUN_MODE_LOCAL = "local";

    /**
     * The Constant DELETE_PERMISSION_CHECK.
     */
    private static final String DELETE_PERMISSION_CHECK = "data/perm/delete/check/";

    /**
     * The Constant CSV_FILE_DOWNLOAD_ADDRESS.
     */
    public static final String CSV_FILE_DOWNLOAD_ADDRESS = "job/{id}/csv/download";

    /**
     * The Constant JOB_LOGS_ZIP_FILE_DOWNLOAD_ADDRESS.
     */
    public static final String JOB_LOGS_ZIP_FILE_DOWNLOAD_ADDRESS = "job/{id}/logs/download";

    /**
     * The Constant SCHEME_PLOTING_OPTION.
     */
    public static final String SCHEME_PLOTING_OPTION_HEATMAP = "job/{id}/plot/option/1";

    /**
     * The Constant SCHEME_PLOTING_OPTION_BUBBLE.
     */
    public static final String SCHEME_PLOTING_OPTION_BUBBLE = "job/{id}/plot/option/2";

    /**
     * The Constant SCHEME_PLOTING_OPTION_CORRELATION.
     */
    public static final String SCHEME_PLOTING_OPTION_CORRELATION = "job/{id}/plot/option/3";

    /**
     * The Constant SCHEME_PLOTING_OPTION_MATPLOTLIB.
     */
    public static final String SCHEME_PLOTING_OPTION_MATPLOTLIB = "job/selectworkflow/{id}/?mode=single";

    /**
     * The Constant DOWNLOAD_CSV_FILE.
     */
    private static final String DOWNLOAD_CSV_FILE = "4100053x4";

    /**
     * The Constant DOWNLOAD_JOB_LOGS_ZIP_FILE.
     */
    private static final String DOWNLOAD_JOB_LOGS_ZIP_FILE = "4100048x4";

    /**
     * The Constant GENERATE_IMAGE_URL.
     */
    public static final String GENERATE_IMAGE_URL = "job/{id}/generateimage{?key}";

    /**
     * The Constant KEY.
     */
    private static final String KEY = "?key=";

    /**
     * The Constant KEY_PATTERN.
     */
    public static final String KEY_PATTERN = "{?key}";

    /**
     * The application manager reference.
     */
    @Autowired
    private WorkflowDaemonManager daemonManager;

    /**
     * The suscore manager.
     */
    @Autowired
    private SuscoreDaemonManager suscoreManager;

    /**
     * logger for logging daemong logging information.
     */
    private static final Logger logger = Logger.getLogger( ConstantsString.DAEMON_LOGGER );

    /**
     * The Constant SERVER_STOP_JOB.
     */
    private static final String SERVER_STOP_JOB = "data/stop/";

    /**
     * The Constant SERVER_PAUSE_JOB.
     */
    private static final String SERVER_PAUSE_JOB = "data/pause/";

    /**
     * The Constant SERVER_RESUME_JOB.
     */
    private static final String SERVER_RESUME_JOB = "data/resume/";

    /*
     * (non-Javadoc)
     * @see de.soco.software.local.service.WorkflowDaemonController#stopJob(java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.RERUN_JOB_ID_STOP, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > reRunJob( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "id" ) String jobId ) throws IOException {
        Job retJob = null;
        String urlJob = suscoreManager.getServerAPIBase() + JOB_END_POINT + SLASH + jobId + GET_JOB_END_POINT;
        SusResponseDTO reponse = SuSClient.getRequest( urlJob, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        if ( reponse != null && reponse.getData() != null ) {
            retJob = JsonUtils.jsonToObject( JsonUtils.objectToJson( reponse.getData() ), Job.class );
        }

        SusResponseDTO reponseCheckChild = SuSClient.getRequest(
                suscoreManager.getServerAPIBase() + "job/" + retJob.getId().toString() + "/child",
                CommonUtils.prepareHeadersWithAuthToken( authToken ) );

        if ( Boolean.parseBoolean( reponseCheckChild.getData().toString() ) ) {
            return new ResponseEntity<>( ResponseUtils.failureResponse( "Master job is not allowed to rerun.", null ), HttpStatus.OK );
        }

        logger.debug( "show job : " + retJob.toString() );
        LatestWorkFlowDTO workflow = new LatestWorkFlowDTO();
        String urlWorkflow = suscoreManager.getServerAPIBase() + "workflow" + SLASH + retJob.getWorkflowId().toString() + SLASH + "version"
                + SLASH + retJob.getWorkflowVersion().getId();
        SusResponseDTO reponseWorkflow = SuSClient.getRequest( urlWorkflow, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        if ( reponseWorkflow != null && reponseWorkflow.getData() != null ) {
            workflow = JsonUtils.jsonToObject( JsonUtils.objectToJson( reponseWorkflow.getData() ), LatestWorkFlowDTO.class );
        } else {
            return new ResponseEntity<>( ResponseUtils.failureResponse( reponseWorkflow.getMessage().getContent(), null ),
                    HttpStatus.BAD_REQUEST );
        }
        workflow.setRunsOn( retJob.getRunsOn().getId().toString() );
        applyPreviousAskOnRunParameters( workflow, retJob );

        // job will have global variables if it has any custom flag other than ShapeModule
        workflow.setHasGlobalVariables( CollectionUtils.isNotEmpty( workflow.getCustomFlags() )
                && workflow.getCustomFlags().stream().anyMatch( flag -> !"ShapeModule".equals( flag ) ) );
        workflow.setRerunJobId( jobId );
        logger.debug( "rerun workFlow : " + workflow.toString() );
        return new ResponseEntity<>( ResponseUtils.successResponse( workflow ), HttpStatus.OK );
    }

    /**
     * Apply ask on run parameters from last job run.
     *
     * @param workflow
     *         the workflow
     * @param retJob
     *         the retJob
     */
    private void applyPreviousAskOnRunParameters( LatestWorkFlowDTO workflow, Job retJob ) {
        if ( retJob.getAskOnRunParameters() == null || retJob.getAskOnRunParameters().isEmpty() ) {
            return;
        }

        Map< String, Object > askOnRunParameters = retJob.getAskOnRunParameters();
        for ( String key : workflow.getJob().keySet() ) {
            if ( askOnRunParameters.containsKey( key ) ) {
                workflow.getJob().put( key, askOnRunParameters.get( key ) );
            }
        }

        WorkflowModel workflowModel = JsonUtils.jsonToObject( JsonUtils.toJson( workflow.getElements() ), WorkflowModel.class );
        for ( WorkflowElement element : workflowModel.getNodes() ) {
            for ( Field field : element.getData().getFields() ) {
                if ( askOnRunParameters.containsKey( field.getName() ) ) {
                    Field f = JsonUtils.jsonToObject( JsonUtils.toJson( askOnRunParameters.get( field.getName() ) ), Field.class );
                    field.setValue( f.getValue() );
                }
            }
        }

        // put replaced elements to wf
        workflow.setElements( JsonUtils.convertStringToMapGenericValue( JsonUtils.toJson( workflowModel ) ) );
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#discardJob(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.DISCARD_JOB_API, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > discardJob( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "id" ) String jobId ) throws IOException {
        try {
            if ( !checkDeletePermission( jobId, authToken ) ) {
                return new ResponseEntity<>( ResponseUtils.successInfoResponse( "Used do not have delete permissions", null ),
                        HttpStatus.OK );
            }

            // get job data object
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + SLASH + jobId + GET_JOB_END_POINT;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            if ( reponse.getData() != null ) {
                Job jobDto = JsonUtils.jsonToObject( JsonUtils.convertMapToString( ( Map< String, String > ) reponse.getData() ),
                        Job.class );
                if ( !jobDto.getStatus().getName().equals( WorkflowStatus.RUNNING.getValue() ) ) {
                    // update job status to discard
                    jobDto.setStatus( new Status( WorkflowStatus.DISCARD ) );
                    String updateJobURL = suscoreManager.getServerAPIBase() + "workflow/job/" + jobId;
                    SuSClient.putRequest( updateJobURL, CommonUtils.prepareHeadersWithAuthToken( authToken ), JsonUtils.toJson( jobDto ) );

                    // get created data objects by this job id
                    String urlCreatedObj = suscoreManager.getServerAPIBase() + "job/" + jobId + "/created/object/ids/list";
                    SusResponseDTO reponseCreatedObj = SuSClient.getRequest( urlCreatedObj,
                            CommonUtils.prepareHeadersWithAuthToken( authToken ) );
                    List< String > selectedFiles = JsonUtils.jsonToList( JsonUtils.toJson( reponseCreatedObj.getData() ), String.class );
                    List< Object > filterItem = Arrays.asList( selectedFiles.toArray() );

                    FiltersDTO filter = new FiltersDTO();
                    filter.setItems( filterItem );
                    // create selection id for created objects
                    String urlSelection = suscoreManager.getServerAPIBase() + "selection";
                    SusResponseDTO reponseSelection = SuSClient.postRequest( urlSelection, JsonUtils.objectToJson( filter ),
                            CommonUtils.prepareHeadersWithAuthToken( authToken ) );
                    SelectionResponseUI selectionObject = JsonUtils.jsonToObject( JsonUtils.toJson( reponseSelection.getData() ),
                            SelectionResponseUI.class );

                    // delete all created objects by selection id
                    String urldelete = suscoreManager.getServerAPIBase() + "data/object/" + selectionObject.getId() + "?mode=bulk";
                    SuSClient.deleteRequest( urldelete, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
                    return new ResponseEntity<>( ResponseUtils.successResponse( "Job Discarded successfully" ), HttpStatus.OK );
                } else {
                    return new ResponseEntity<>( ResponseUtils.successInfoResponse( "Can not Discard job while its Running", "" ),
                            HttpStatus.OK );
                }
            }

        } catch ( IOException e ) {
            return new ResponseEntity<>( ResponseUtils.failureResponse( "Discarding job Failed", e ), HttpStatus.BAD_REQUEST );
        }
        return new ResponseEntity<>( ResponseUtils.failureResponse( "Discarding job Failed", "" ), HttpStatus.BAD_REQUEST );
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#getSusJobsList(java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.API_SUS_JOBS, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > getSusJobsList( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String objectFilterJson ) {
        FilteredResponse< Job > results = null;
        try {
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + MY_JOBS_ENDPOINT;
            SusResponseDTO reponse = SuSClient.postRequest( url, objectFilterJson, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            if ( reponse != null && reponse.getData() != null ) {
                String jobdto = JsonUtils.convertMapToString( ( Map< String, String > ) reponse.getData() );
                results = JsonUtils.jsonToObject( jobdto, FilteredResponse.class );

            }

            return new ResponseEntity<>( ResponseUtils.successResponse( results ), HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#getFileJobsList(java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.API_LOCAL_JOBS, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > getFileJobsList( @RequestBody String objectFilterJson ) {
        try {
            FiltersDTO filter = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            List< Job > results = getDaemonManager().getFileBaseJobs();
            List< FileJobDTO > fileJobList = prepareFileJobModelFromJobs( results );
            filter.setTotalRecords( ( long ) results.size() );
            filter.setFilteredRecords( ( long ) results.size() );
            FilteredResponse< FileJobDTO > response = PaginationUtil.constructFilteredResponse( filter, fileJobList );
            return new ResponseEntity<>( ResponseUtils.successResponse( response ), HttpStatus.OK );
        } catch ( Exception e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#getSusJobTableUI()
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.API_SUS_JOB_TABLE_UI, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getSusJobTableUI( @RequestHeader( value = "X-Auth-Token" ) String authToken ) {

        try {
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + SUS_JOBS_TABLE_UI_API;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#getFileJobTableUI()
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.API_FILES_JOB_TABLE_UI, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getFileJobTableUI( @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            return new ResponseEntity<>(
                    ResponseUtils.successResponse( new TableUI(
                            GUIUtils.getColumnList( DaemonPropertiesManager.hasTranslation(), authToken, FileJobDTO.class ) ) ),
                    HttpStatus.OK );
        } catch ( SusException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#getLocalJobsContextMenu(java.lang.String, java.lang
     * .String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.API_LOCAL_JOB_CONTEXT, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > getLocalJobsContextMenu( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String itemsJson ) {
        return new ResponseEntity<>( ResponseUtils.successResponse( new ArrayList<>() ), HttpStatus.OK );
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#getSusJobsContextMenu(java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.API_JOB_CONTEXT, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > getSusJobsContextMenu( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String itemsJson ) {
        try {
            ItemsDTO filter = JsonUtils.jsonToObject( itemsJson, ItemsDTO.class );
            String jobid = filter.getItems().get( 0 ).toString();
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + SLASH + jobid + GET_JOB_END_POINT;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String jobJson = JsonUtils.convertMapToString( ( Map< String, String > ) reponse.getData() );
            Job jobDto = JsonUtils.jsonToObject( jobJson, Job.class );
            List< ContextMenuItem > cml = new ArrayList<>();
            if ( jobDto.getStatus().getId() == WorkflowStatus.DISCARD.getKey() ) {
                return new ResponseEntity<>(
                        ResponseUtils.successResponse(
                                ContextUtilExternal.allOrderedContext( DaemonPropertiesManager.hasTranslation(), authToken, cml ) ),
                        HttpStatus.OK );
            }
            if ( filter.getItems().size() == ConstantsInteger.INTEGER_VALUE_ONE
                    && ( jobDto.getStatus().getId() == WorkflowStatus.RUNNING.getKey()
                    || jobDto.getStatus().getId() == WorkflowStatus.PAUSED.getKey() )
                    && jobDto.getJobType() != JobTypeEnums.SYSTEM.getKey() ) {
                ContextMenuItem stopJobItem = new ContextMenuItem();
                stopJobItem.setUrl( STOP_JOB_CONTEXT_URL.replace( ID_PARAM, jobid ) );
                stopJobItem.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                        STOP_JOB_CONTEXT_TITLE_KEY ) );
                cml.add( stopJobItem );
                ContextMenuItem pauseJobItem = new ContextMenuItem();
                if ( jobDto.getStatus().getId() != WorkflowStatus.PAUSED.getKey() && jobDto.getJobType() == JobTypeEnums.WORKFLOW.getKey()
                        && jobDto.getRunMode().equalsIgnoreCase( RUN_MODE_SERVER ) ) {
                    pauseJobItem.setUrl( PAUSE_JOB_CONTEXT_URL.replace( ID_PARAM, jobid ) );
                    pauseJobItem.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                            PAUSE_JOB_CONTEXT_TITLE_KEY ) );
                    cml.add( pauseJobItem );
                } else if ( jobDto.getStatus().getId() == WorkflowStatus.PAUSED.getKey()
                        && jobDto.getJobType() == JobTypeEnums.WORKFLOW.getKey()
                        && jobDto.getRunMode().equalsIgnoreCase( RUN_MODE_SERVER ) ) {
                    ContextMenuItem resumeJobItem = new ContextMenuItem();
                    resumeJobItem.setUrl( RESUME_JOB_CONTEXT_URL.replace( ID_PARAM, jobid ) );
                    resumeJobItem.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                            RESUME_JOB_CONTEXT_TITLE_KEY ) );
                    cml.add( resumeJobItem );
                }
            } else if ( filter.getItems().size() == ConstantsInteger.INTEGER_VALUE_ONE
                    && jobDto.getStatus().getId() == WorkflowStatus.PAUSED.getKey()
                    && jobDto.getJobType() == JobTypeEnums.WORKFLOW.getKey() ) {
                ContextMenuItem stopJobItem = new ContextMenuItem();
                stopJobItem.setUrl( STOP_JOB_CONTEXT_URL.replace( ID_PARAM, jobid ) );
                stopJobItem.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                        STOP_JOB_CONTEXT_TITLE_KEY ) );
                cml.add( stopJobItem );
            } else if ( filter.getItems().size() == ConstantsInteger.INTEGER_VALUE_ONE && jobDto.getRunMode().equals( RUN_MODE_LOCAL )
                    && ( jobDto.getStatus().getId() != WorkflowStatus.RUNNING.getKey()
                    && jobDto.getStatus().getId() != WorkflowStatus.PAUSED.getKey() ) ) {

                for ( RouterConfigItem i : getContextRouterForJob() ) {

                    if ( i.getTitle().equals( DISCARD_JOB_TITLE_KEY ) && jobDto.getStatus().getId() == WorkflowStatus.DISCARD.getKey() ) {
                        continue;
                    }

                    if ( i.getUrl().contains( ID_PARAM ) && !i.getTitle().equals( STOP_JOB_CONTEXT_TITLE_KEY ) ) {

                        ContextMenuItem cm = new ContextMenuItem();

                        cm.setUrl( i.getUrl().replace( ID_PARAM, jobid ) );

                        cm.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                                i.getTitle() ) );
                        cml.add( cm );
                    }
                }
            } else if ( filter.getItems().size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
                if ( isWorkflowJob( jobDto ) ) {
                    if ( jobDto.getJobType() != JobTypeEnums.SCHEME.getKey() && jobDto.getJobType() != JobTypeEnums.VARIANT.getKey() ) {
                        ContextMenuItem cm2 = new ContextMenuItem();
                        cm2.setUrl( RERUN_JOB_CONTEXT_URL.replace( ID_PARAM, jobid ) );
                        cm2.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                                RERUN_JOB_CONTEXT_TITLE_KEY ) );
                        cml.add( cm2 );
                    }

                    if ( jobDto.getStatus().getId() != WorkflowStatus.DISCARD.getKey()
                            && jobDto.getStatus().getId() != WorkflowStatus.RUNNING.getKey()
                            && jobDto.getStatus().getId() != WorkflowStatus.PAUSED.getKey() ) {
                        ContextMenuItem cm3 = new ContextMenuItem();
                        cm3.setUrl( DISCARD_JOB_CONTEXT_URL.replace( ID_PARAM, jobid ) );
                        cm3.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                                DISCARD_JOB_TITLE_KEY ) );
                        cml.add( cm3 );
                    }
                }
            }
            // scheme plotting context added
            if ( filter.getItems().size() == ConstantsInteger.INTEGER_VALUE_ONE && jobDto.getJobType() == JobTypeEnums.SCHEME.getKey() ) {
                cml.add( prepareJobCSVFileHeatMapContext( jobDto.getId(), authToken ) );
                cml.add( prepareJobCSVFileBubbleChartContext( jobDto.getId(), authToken ) );
                cml.add( prepareJobCSVFileCorrelationContext( jobDto.getId(), authToken ) );
                cml.add( prepareJobCSVFileDownloadInStagingContext( jobDto.getId(), authToken ) );

                cml.addAll( getContextGenerateImageMenu( jobDto.getId().toString(), authToken ) );
            }

            if ( filter.getItems().size() == ConstantsInteger.INTEGER_VALUE_ONE && ( jobDto.getJobType() == JobTypeEnums.WORKFLOW.getKey()
                    || jobDto.getJobType() == JobTypeEnums.SCHEME.getKey() || jobDto.getJobType() == JobTypeEnums.VARIANT.getKey() ) ) {

                cml.add( prepareJobLogsZipToDownload( jobDto.getId().toString(), authToken ) );

            }

            return new ResponseEntity<>(
                    ResponseUtils.successResponse(
                            ContextUtilExternal.allOrderedContext( DaemonPropertiesManager.hasTranslation(), authToken, cml ) ),
                    HttpStatus.OK );
        } catch ( Exception e ) {
            return handleCatchBlock( e );
        }
    }

    /**
     * Prepare job logs zip to download.
     *
     * @param objectId
     *         the object id
     * @param authToken
     *         the auth token
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareJobLogsZipToDownload( String objectId, String authToken ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl(
                JOB_LOGS_ZIP_FILE_DOWNLOAD_ADDRESS.replace( "{id}", ConstantsString.EMPTY_STRING + objectId ) + "?token=" + authToken );
        containerCMI.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                DOWNLOAD_JOB_LOGS_ZIP_FILE ) );
        return containerCMI;
    }

    /**
     * Prepare job CSV file download in staging context.
     *
     * @param objectId
     *         the object id
     * @param authToken
     *         the auth token
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareJobCSVFileDownloadInStagingContext( UUID objectId, String authToken ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( CSV_FILE_DOWNLOAD_ADDRESS.replace( "{id}", ConstantsString.EMPTY_STRING + objectId ) + "?token=" + authToken );
        containerCMI.setTitle(
                MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken, DOWNLOAD_CSV_FILE ) );
        return containerCMI;
    }

    /**
     * Prepare job CSV file heat map context.
     *
     * @param objectId
     *         the object id
     * @param authToken
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareJobCSVFileHeatMapContext( UUID objectId, String authToken ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( SCHEME_PLOTING_OPTION_HEATMAP.replace( "{id}", ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                ConstantsString.HEATMAP_KEY ) );
        return containerCMI;
    }

    /**
     * Prepare job CSV file bubble chart context.
     *
     * @param objectId
     *         the object id
     * @param authToken
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareJobCSVFileBubbleChartContext( UUID objectId, String authToken ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( SCHEME_PLOTING_OPTION_BUBBLE.replace( "{id}", ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                ConstantsString.PLOT_BUBBLE_CHART_KEY ) );
        return containerCMI;
    }

    /**
     * Prepare job CSV file correlation context.
     *
     * @param objectId
     *         the object id
     * @param authToken
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareJobCSVFileCorrelationContext( UUID objectId, String authToken ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( SCHEME_PLOTING_OPTION_CORRELATION.replace( "{id}", ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                ConstantsString.PLOT_CORRELATION_KEY ) );
        return containerCMI;
    }

    /**
     * Get context generate image menu.
     *
     * @param jobId
     *         the job id
     * @param authToken
     *         the authToken
     *
     * @return the context menu items
     */
    private List< ContextMenuItem > getContextGenerateImageMenu( String jobId, String authToken ) {
        List< ContextMenuItem > contextToReturn = new ArrayList<>();
        List< String > options = getGenerateImageOptions( authToken );

        for ( String option : options ) {
            contextToReturn.add( prepareGenerateImageContextForOption( authToken, jobId, option ) );
        }

        return contextToReturn;
    }

    /**
     * Prepare generate image context for option.
     *
     * @param jobId
     *         the job id
     * @param option
     *         the option
     * @param s
     *
     * @return the generate image context
     */
    private ContextMenuItem prepareGenerateImageContextForOption( String authToken, String jobId, String option ) {
        ContextMenuItem contextItem = new ContextMenuItem();
        contextItem.setTitle( MessageBundleFactory.getExternalMessage( DaemonPropertiesManager.hasTranslation(), authToken,
                ConstantsString.GENERATE_IMAGE_KEY ) + ConstantsString.SPACE + option );
        contextItem.setUrl( GENERATE_IMAGE_URL.replace( RouterConfigItem.ID_PATTERN, jobId ).replace( KEY_PATTERN, KEY + option ) );

        return contextItem;
    }

    /**
     * Get generate image options.
     *
     * @param authToken
     *         the authToken
     *
     * @return the karaf path of server
     */
    private List< String > getGenerateImageOptions( String authToken ) {
        return DaemonPropertiesManager.getMatPlotLib();
    }

    /**
     * Checks if is workflow job.
     *
     * @param wf
     *         the wf
     *
     * @return true, if is workflow job
     */
    private boolean isWorkflowJob( Job wf ) {

        if ( wf.getWorkflowId() == null ) {
            return true;
        }
        if ( ( wf.getWorkflowId().equals( UUID.fromString( SystemWorkflow.DELETE.getId() ) ) )
                || ( wf.getWorkflowId().equals( UUID.fromString( SystemWorkflow.RESTORE.getId() ) ) )
                || ( wf.getJobType() == JobTypeEnums.SYSTEM.getKey() ) ) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#openJobDir(java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.GET_JOB_BY_ID, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getJobById( @PathVariable( "jobId" ) String jobId,
            @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        Job retJob = null;
        try {

            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + SLASH + jobId + GET_JOB_END_POINT;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            if ( reponse != null && reponse.getData() != null ) {
                retJob = JsonUtils.jsonToObject( JsonUtils.objectToJson( reponse.getData() ), Job.class );
            }

        } catch ( Exception e ) {
            return handleCatchBlock( e );
        }

        return new ResponseEntity<>( ResponseUtils.successResponse( retJob ), HttpStatus.OK );

    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#getSusJobsView(java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.FILE_JOBS_VIEW, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getFileJobsView( @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {

            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + WorkflowEndPoints.FILE_JOBS_VIEW;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }

    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#saveSusJobsView(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.FILE_JOBS_VIEW, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > saveFileJobsView( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String viewJson ) {
        try {
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + WorkflowEndPoints.FILE_JOBS_VIEW;
            SusResponseDTO reponse = SuSClient.postRequest( url, viewJson, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#saveSusJobsView(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.FILE_DELETE_OR_UPDATE_JOBS_VIEW, method = RequestMethod.PUT )
    public ResponseEntity< SusResponseDTO > updateFileJobsView( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "viewId" ) String viewId, @RequestBody String viewJson ) {
        try {
            String path = WorkflowEndPoints.FILE_DELETE_OR_UPDATE_JOBS_VIEW.replace( VIEW_ID_PARAM, viewId );
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + path;
            SusResponseDTO reponse = SuSClient.putRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ), viewJson );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#saveSusJobsView(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.FILE_DELETE_OR_UPDATE_JOBS_VIEW, method = RequestMethod.DELETE )
    public ResponseEntity< SusResponseDTO > deleteFileJobsView( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "viewId" ) String viewId ) {
        try {
            String path = WorkflowEndPoints.FILE_DELETE_OR_UPDATE_JOBS_VIEW.replace( VIEW_ID_PARAM, viewId );
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + path;
            SusResponseDTO reponse = SuSClient.deleteRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#setSusJobsViewAsDefault(java.lang.String,
     * java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.FILE_JOBS_VIEW_AS_DEFAULT, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > setFileJobsViewAsDefault( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "viewId" ) String viewId ) {
        try {
            String path = WorkflowEndPoints.FILE_JOBS_VIEW_AS_DEFAULT.replace( VIEW_ID_PARAM, viewId );
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + path;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }

    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#getSusJobsView(java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.SUS_JOBS_VIEW, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getSusJobsView( @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {

            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + ConstantsViewEndPoints.SUS_SAVE_OR_LIST_VIEW;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }

    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#saveSusJobsView(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.SUS_JOBS_VIEW, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > saveSusJobsView( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String viewJson ) {
        try {
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + ConstantsViewEndPoints.SUS_SAVE_OR_LIST_VIEW;
            SusResponseDTO reponse = SuSClient.postRequest( url, viewJson, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#saveSusJobsView(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.SUS_DELETE_OR_UPDATE_JOBS_VIEW, method = RequestMethod.PUT )
    public ResponseEntity< SusResponseDTO > updateSusJobsView( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "viewId" ) String viewId, @RequestBody String viewJson ) {
        try {
            String path = ConstantsViewEndPoints.SUS_DELETE_OR_UPDATE_OR_GET_VIEW.replace( VIEW_ID_PARAM, viewId );
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + path;
            SusResponseDTO reponse = SuSClient.putRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ), viewJson );
            if ( null != reponse.getData() ) {
                return new ResponseEntity<>( reponse, HttpStatus.OK );
            } else {
                return new ResponseEntity<>( ResponseUtils.failureResponse( reponse.getMessage().getContent(), null ), HttpStatus.OK );
            }
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#saveSusJobsView(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.SUS_DELETE_OR_UPDATE_JOBS_VIEW, method = RequestMethod.DELETE )
    public ResponseEntity< SusResponseDTO > deleteSusJobsView( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "viewId" ) String viewId ) {
        try {
            String path = ConstantsViewEndPoints.SUS_DELETE_OR_UPDATE_OR_GET_VIEW.replace( VIEW_ID_PARAM, viewId );
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + path;
            SusResponseDTO reponse = SuSClient.deleteRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#setSusJobsViewAsDefault(java.lang.String,
     * java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.SUS_JOBS_VIEW_AS_DEFAULT, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > setSusJobsViewAsDefault( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "viewId" ) String viewId ) {
        try {
            String path = ConstantsViewEndPoints.SUS_UPDATE_VIEW_AS_DEFAULT.replace( VIEW_ID_PARAM, viewId );
            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + path;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }

    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.local.service.WorkflowDaemonController#stopJob(java.lang.String)
     */
    @Override
    @RequestMapping( value = STOP_JOB_END_POINT, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > stopJob( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "jobId" ) String jobId ) {
        boolean result = false;
        try {

            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + SLASH + jobId + GET_JOB_END_POINT;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            if ( reponse.getData() != null ) {
                String jobdto = JsonUtils.convertMapToString( ( Map< String, String > ) reponse.getData() );
                Job jobDto = JsonUtils.jsonToObject( jobdto, Job.class );
                if ( jobDto != null && jobDto.getRunMode().equals( RUN_MODE_SERVER ) ) {
                    String urlStop = suscoreManager.getServerAPIBase() + SERVER_STOP_JOB + jobId;
                    SusResponseDTO reponseStopJob = SuSClient.getRequest( urlStop, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
                    result = reponseStopJob.getSuccess();
                    return new ResponseEntity<>( reponseStopJob, HttpStatus.OK );
                } else {
                    result = getDaemonManager().stopJob( authToken, jobId, jobDto );
                }
            }
            if ( result ) {
                logger.info( MessagesUtil.getMessage( WFEMessages.JOB_STOP_SUCCESSFULLY ) );
                return new ResponseEntity<>(
                        ResponseUtils.successResponse( MessagesUtil.getMessage( WFEMessages.JOB_STOP_SUCCESSFULLY ), null ),
                        HttpStatus.OK );
            } else {
                return new ResponseEntity<>( ResponseUtils.failureResponse( MessagesUtil.getMessage( WFEMessages.JOB_STOP_FAILED ), null ),
                        HttpStatus.OK );

            }
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.local.service.WorkflowDaemonController#pauseJob(java.lang.String)
     */
    @Override
    @RequestMapping( value = PAUSE_JOB_END_POINT, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > pauseJob( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "jobId" ) String jobId ) {
        boolean result = false;
        try {

            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + SLASH + jobId + GET_JOB_END_POINT;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            if ( reponse.getData() != null ) {
                String jobdto = JsonUtils.convertMapToString( ( Map< String, String > ) reponse.getData() );
                Job jobDto = JsonUtils.jsonToObject( jobdto, Job.class );
                if ( jobDto != null && jobDto.getRunMode().equals( RUN_MODE_SERVER ) ) {
                    String urlStop = suscoreManager.getServerAPIBase() + SERVER_PAUSE_JOB + jobId;
                    SusResponseDTO reponseStopJob = SuSClient.getRequest( urlStop, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
                    result = reponseStopJob.getSuccess();
                    return new ResponseEntity<>( reponseStopJob, HttpStatus.OK );
                } else {
                    return new ResponseEntity<>( ResponseUtils.failureResponse( "only server jobs can be paused/resumed", null ),
                            HttpStatus.OK );
                }
            }
            if ( result ) {
                logger.info( MessagesUtil.getMessage( WFEMessages.JOB_STOP_SUCCESSFULLY ) );
                return new ResponseEntity<>(
                        ResponseUtils.successResponse( MessagesUtil.getMessage( WFEMessages.JOB_STOP_SUCCESSFULLY ), null ),
                        HttpStatus.OK );
            } else {
                return new ResponseEntity<>( ResponseUtils.failureResponse( MessagesUtil.getMessage( WFEMessages.JOB_STOP_FAILED ), null ),
                        HttpStatus.OK );

            }
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.local.service.WorkflowDaemonController#resumeJob(java.lang.String)
     */
    @Override
    @RequestMapping( value = RESUME_JOB_END_POINT, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > resumeJob( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "jobId" ) String jobId ) {
        boolean result = false;
        try {

            String url = suscoreManager.getServerAPIBase() + JOB_END_POINT + SLASH + jobId + GET_JOB_END_POINT;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            if ( reponse.getData() != null ) {
                String jobdto = JsonUtils.convertMapToString( ( Map< String, String > ) reponse.getData() );
                Job jobDto = JsonUtils.jsonToObject( jobdto, Job.class );
                if ( jobDto != null && jobDto.getRunMode().equals( RUN_MODE_SERVER ) ) {
                    String urlStop = suscoreManager.getServerAPIBase() + SERVER_RESUME_JOB + jobId;
                    SusResponseDTO reponseStopJob = SuSClient.getRequest( urlStop, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
                    result = reponseStopJob.getSuccess();
                    return new ResponseEntity<>( reponseStopJob, HttpStatus.OK );
                } else {
                    return new ResponseEntity<>( ResponseUtils.failureResponse( "only server jobs can be paused/resumed", null ),
                            HttpStatus.OK );
                }
            }
            if ( result ) {
                logger.info( MessagesUtil.getMessage( WFEMessages.JOB_STOP_SUCCESSFULLY ) );
                return new ResponseEntity<>(
                        ResponseUtils.successResponse( MessagesUtil.getMessage( WFEMessages.JOB_STOP_SUCCESSFULLY ), null ),
                        HttpStatus.OK );
            } else {
                return new ResponseEntity<>( ResponseUtils.failureResponse( MessagesUtil.getMessage( WFEMessages.JOB_STOP_FAILED ), null ),
                        HttpStatus.OK );

            }
        } catch ( IOException e ) {
            return handleCatchBlock( e );
        }
    }

    /**
     * Gets the context router for job.
     *
     * @return the context router for job
     */
    private List< RouterConfigItem > getContextRouterForJob() {
        List< RouterConfigItem > list = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try ( InputStream stream = classLoader.getResourceAsStream( ROUTER_PATH ) ) {
            String routerText = IOUtils.toString( stream, STRING_ENCODING );
            RouterConfigList rcl = JsonUtils.jsonToObject( routerText, RouterConfigList.class );
            list = rcl.getRoutes();
        } catch ( Exception e ) {
            logger.info( "In catch " + e.getMessage() );
            ExceptionLogger.logException( e, getClass() );

        }
        return list;

    }

    /**
     * Prepares File job DTO list from jobs list.
     *
     * @param results
     *         the results
     *
     * @return the response entity
     */
    private List< FileJobDTO > prepareFileJobModelFromJobs( List< Job > results ) {
        List< FileJobDTO > fileJobList = new ArrayList<>();

        for ( Job job : results ) {
            FileJobDTO fileJob = new FileJobDTO();
            fileJob.setName( job.getName() );
            fileJob.setDescription( job.getDescription() );
            fileJob.setCompletionTime( job.getCompletionTime() );
            fileJob.setCreatedBy( job.getCreatedBy() );
            fileJob.setId( job.getId() );
            fileJob.setMachine( job.getMachine() );
            fileJob.setOs( job.getOs() );
            fileJob.setProgress( job.getProgress() );
            fileJob.setRunsOn( LocationsEnum.getNameById( job.getRunsOn().getId().toString() ) );
            fileJob.setStatus( job.getStatus() );
            fileJob.setSubmitTime( job.getSubmitTime() );
            fileJob.setWorkingDir( job.getWorkingDir() );

            fileJobList.add( fileJob );
        }

        return fileJobList;
    }

    /**
     * Check delete permission.
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
    private boolean checkDeletePermission( String containerId, String authToken ) throws IOException {
        // api for checking export permission for user
        SusResponseDTO susObject = SuSClient.getRequest( suscoreManager.getServerAPIBase() + DELETE_PERMISSION_CHECK + containerId,
                CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        return Boolean.parseBoolean( JsonUtils.toJson( susObject.getData() ) );
    }

    /**
     * Handle catch block.
     *
     * @param e
     *         the e
     *
     * @return the response entity
     */
    private ResponseEntity< SusResponseDTO > handleCatchBlock( Exception e ) {
        ExceptionLogger.logException( e, getClass() );
        StringWriter sw = new StringWriter();
        PrintWriter pr = new PrintWriter( sw );
        e.printStackTrace( pr );
        String exceptionAsString = sw.toString();
        pr.close();
        try {
            sw.close();
        } catch ( IOException e1 ) {
            e1.printStackTrace();
        }
        return new ResponseEntity<>( ResponseUtils.failureResponse( exceptionAsString, null ), HttpStatus.OK );
    }

    /**
     * Gets the daemon manager.
     *
     * @return the daemonManager
     */
    public WorkflowDaemonManager getDaemonManager() {
        return daemonManager;
    }

    /**
     * Sets the daemon manager.
     *
     * @param daemonManager
     *         the daemonManager to set
     */
    public void setDaemonManager( WorkflowDaemonManager daemonManager ) {
        this.daemonManager = daemonManager;
    }

    /**
     * Gets the suscore manager.
     *
     * @return the suscoreManager
     */
    public SuscoreDaemonManager getSuscoreManager() {
        return suscoreManager;
    }

    /**
     * Sets the suscore manager.
     *
     * @param suscoreManager
     *         the suscoreManager to set
     */
    public void setSuscoreManager( SuscoreDaemonManager suscoreManager ) {
        this.suscoreManager = suscoreManager;
    }

    public static String getSusJobsContext() {
        return SUS_JOBS_CONTEXT;
    }

}
