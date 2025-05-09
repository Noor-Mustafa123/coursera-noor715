package de.soco.software.simuspace.suscore.local.daemon.controller.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.JobSubmitResponseDTO;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.local.daemon.DaemonApplication;
import de.soco.software.simuspace.suscore.local.daemon.controller.WorkflowDaemonController;
import de.soco.software.simuspace.suscore.local.daemon.manager.WorkflowDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.model.WorkflowEndPoints;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;

/**
 * This Class Behave like a service which consist of the endpoints for Job execution and other Job related tasks.
 *
 * @author Nosheen.Sharif
 */
@RestController
@RequestMapping( value = WorkflowEndPoints.API_WORKFLOW )
public class WorkflowDaemonControllerImpl implements WorkflowDaemonController {

    /**
     * The Constant DAEMON_IS_UP_AND_RUNNING.
     */
    public static final String DAEMON_IS_UP_AND_RUNNING = "Daemon is up and running";

    /**
     * The Constant NAME.
     */
    public static final String NAME = "name";

    /**
     * The application manager reference.
     */
    @Autowired
    private WorkflowDaemonManager daemonManager;

    /**
     * logger for logging daemong logging information.
     */
    private static final Logger logger = Logger.getLogger( ConstantsString.DAEMON_LOGGER );

    /* (non-Javadoc)
     * @see de.soco.software.local.service.WorkflowDaemonController#runJob(java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.RUNJOB, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > runJob( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String jobParametersString ) {
        JobParameters jobParameters = new JobParametersImpl();
        try {
            Map< String, Object > map = new HashMap<>();
            map = ( Map< String, Object > ) JsonUtils.jsonToMap( jobParametersString, map );
            jobParameters.setRequestHeaders(
                    JsonUtils.jsonToObject( JsonUtils.objectToJson( map.get( "requestHeaders" ) ), RequestHeaders.class ) );
            jobParameters.setServer( JsonUtils.jsonToObject( JsonUtils.objectToJson( map.get( "server" ) ), RestAPI.class ) );
            LatestWorkFlowDTO workflow = JsonUtils.jsonToObject( JsonUtils.objectToJson( map.get( "workflow" ) ), LatestWorkFlowDTO.class );
            jobParameters.setDescription( workflow.getJob().get( "description" ).toString() );
            jobParameters.setName( workflow.getJob().get( "name" ).toString() );
            jobParameters.setRunsOn( workflow.getJob().get( "runsOn" ).toString() );
            jobParameters.setWorkingDir(
                    JsonUtils.jsonToObject( JsonUtils.objectToJson( workflow.getJob().get( "workingDir" ) ), EngineFile.class ) );
            jobParameters.setWorkflow( workflow );
            jobParameters.setId( jobParameters.getId() == null ? UUID.randomUUID().toString() : jobParameters.getId() );
            jobParameters.getRequestHeaders().setToken( authToken );
            String jobInteger = saveJobIds( UUID.fromString( jobParameters.getId() ), jobParameters.getServer(),
                    jobParameters.getRequestHeaders() );
            if ( jobInteger != null ) {
                jobParameters.setJobInteger( Integer.parseInt( jobInteger ) );
            }

            Map< String, Object > globalVariables = new HashMap<>();
            if ( null != workflow.getCustomFlags() ) {
                for ( String plugin : workflow.getCustomFlags() ) {
                    for ( Map< String, Object > field : daemonManager.getDynamicFields( plugin, jobParametersString,
                            jobParameters.getServer(), jobParameters.getRequestHeaders() ) ) {
                        globalVariables.put( "{{" + plugin + ConstantsString.DOT + field.get( NAME ).toString() + "}}",
                                map.get( field.get( NAME ).toString() ) );
                    }
                }
            }
            jobParameters.setGlobalVariables( globalVariables );
            daemonManager.runjob( jobParameters );
            logger.info( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ) );
            return new ResponseEntity<>( ResponseUtils.successResponse( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ),
                    new JobSubmitResponseDTO( jobParameters.getId(), jobParameters.getName() ) ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    private static String saveJobIds( UUID uuid, RestAPI server, RequestHeaders requestHeaders ) {
        final String url = prepareURL( "/api/workflow/saveJobIds/" + uuid.toString(), server );
        SusResponseDTO susResponseDTO = SuSClient.getRequest( url, prepareHeaders( requestHeaders ) );
        String id = null;
        if ( susResponseDTO != null && !susResponseDTO.getSuccess() ) {
            throw new SusException( new Exception( susResponseDTO.getMessage().getContent() ) );
        } else {
            id = susResponseDTO.getData().toString();
        }
        return id;
    }

    public static Map< String, String > prepareHeaders( RequestHeaders headers ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( "User-Agent", headers.getUserAgent() );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, headers.getToken() );

        return requestHeaders;
    }

    private static String prepareURL( String url, RestAPI server ) {
        if ( server != null ) {
            return server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort() + url;
        } else {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) ) );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.local.service.WorkflowDaemonController#getfileJobs()
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.JOBS_FILES_LIST, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getfileJobs() {

        List< Job > jobList = getDaemonManager().getFileBaseJobs();

        return new ResponseEntity<>( ResponseUtils.successResponse( "", jobList ), HttpStatus.OK );
    }

    /* (non-Javadoc)
     * @see de.soco.software.local.service.WorkflowDaemonController#stopJob(java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.JOB_JOB_ID_STOP, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > stopJob( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "jobId" ) String jobId ) {
        boolean result = getDaemonManager().stopJob( authToken, jobId, new JobImpl() );
        if ( result ) {
            logger.info( MessagesUtil.getMessage( WFEMessages.JOB_STOP_SUCCESSFULLY ) );
            return new ResponseEntity<>(
                    ResponseUtils.successResponse( MessagesUtil.getMessage( WFEMessages.JOB_STOP_SUCCESSFULLY ), null ), HttpStatus.OK );
        } else {
            return new ResponseEntity<>( ResponseUtils.failureResponse( MessagesUtil.getMessage( WFEMessages.JOB_STOP_FAILED ), null ),
                    HttpStatus.OK );

        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.WorkflowDaemonController#importWorkflowForm(java.lang.String)
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.SUS_IMPORT_UI, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > importWorkflowForm( @PathVariable( "parentId" ) String parentId ) {
        return new ResponseEntity<>( ResponseUtils.successResponse( "", daemonManager.createWorkflowForm( parentId ) ), HttpStatus.OK );

    }

    /* (non-Javadoc)
     * @see de.soco.software.local.service.WorkflowDaemonController#getListOfRunningJobs()
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.JOBS_FILES_RUNNING_LIST, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getListOfRunningJobs() {

        return new ResponseEntity<>( ResponseUtils.successResponse( "", getDaemonManager().getRunningJobs() ), HttpStatus.OK );
    }

    @Override
    @RequestMapping( value = WorkflowEndPoints.SUS_IMPORT, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > importWorkflow( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String objectJson ) {

        try {
            boolean result = getDaemonManager().importWorkflow( authToken, objectJson );

            if ( result ) {
                return new ResponseEntity<>( ResponseUtils.successResponse( MessagesUtil.getMessage( WFEMessages.NEW_FILE_CREATED ), null ),
                        HttpStatus.OK );
            } else {
                return new ResponseEntity<>( ResponseUtils.failureResponse( MessagesUtil.getMessage( WFEMessages.ERROR_MESSAGE ), null ),
                        HttpStatus.OK );

            }
        } catch ( Exception e ) {
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }

    }

    /* (non-Javadoc)
     * @see de.soco.software.local.daemon.controller.WorkflowDaemonController#isUp()
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.ISUP, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > isUp() {
        return new ResponseEntity<>( ResponseUtils.successResponse( DAEMON_IS_UP_AND_RUNNING, null ), HttpStatus.OK );
    }

    @Override
    @RequestMapping( value = "/addPID/{pid}", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > addPID( @PathVariable( "pid" ) Integer pid ) {
        ExceptionLogger.logMessage( ">>addPIDAPI susClientPID: " + pid );
        DaemonApplication.addPID( pid );
        return new ResponseEntity<>( ResponseUtils.successResponse( "added", null ), HttpStatus.OK );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = WorkflowEndPoints.SHUTDOWN, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > shutdown( @PathVariable( "pid" ) Integer pid ) {
        if ( !daemonManager.isLocalJobRunning() ) {
            RestTemplate restTemplate = new RestTemplate();
            Properties daemonProperties = new Properties();
            try ( InputStream demonConfigStream = new FileInputStream( DaemonApplication.daemonPropertyFilePath ) ) {
                daemonProperties.load( demonConfigStream );
            } catch ( FileNotFoundException e ) {
                logger.error( e.getMessage() );
            } catch ( IOException e ) {
                logger.error( e.getMessage() );
            }
            DaemonApplication.removePID( pid );
            if ( DaemonApplication.isPIDListEmpty() ) {
                int port = Integer.parseInt( daemonProperties.getProperty( "port" ) );
                restTemplate.postForEntity( String.format( DaemonApplication.HTTP_LOCALHOST_URI, port, "shutdown" ), HttpMethod.POST,
                        String.class );
            }

            return new ResponseEntity<>( ResponseUtils.successResponse( "Daemon shutting down", null ), HttpStatus.OK );
        } else {
            return new ResponseEntity<>( ResponseUtils.failureResponse( "Jobs in Progress, cannot shutdown client.", null ),
                    HttpStatus.OK );
        }
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

}
