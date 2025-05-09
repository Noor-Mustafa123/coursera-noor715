package de.soco.software.simuspace.suscore.local.daemon.controller.impl;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.model.JobSubmitResponseDTO;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.local.daemon.controller.SchemeController;
import de.soco.software.simuspace.suscore.local.daemon.manager.WorkflowDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.model.SchemeEndPoints;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;

/**
 * The Class SchemeControllerImpl.
 */
@RestController
@RequestMapping( value = SchemeEndPoints.API_CONFIG_WORKFLOWSCHEME )
public class SchemeControllerImpl implements SchemeController {

    /**
     * The application manager reference.
     */
    @Autowired
    private WorkflowDaemonManager daemonManager;

    /**
     * logger for logging daemong logging information.
     */
    private static final Logger logger = Logger.getLogger( ConstantsString.DAEMON_LOGGER );

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping( value = SchemeEndPoints.RUNSCHEME, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > runScheme( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String jobParametersString ) {
        JobParameters jobParameters = null;
        try {
            jobParameters = JsonUtils.jsonToObject( jobParametersString, JobParametersImpl.class );
        } catch ( JsonSerializationException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
        try {
            jobParameters.setId( jobParameters.getId() == null ? UUID.randomUUID().toString() : jobParameters.getId() );
            jobParameters.getRequestHeaders().setToken( authToken );
            if ( null != jobParameters.getWorkflow().getJob() && !jobParameters.getWorkflow().getJob().isEmpty() ) {
                jobParameters.setRunsOn( jobParameters.getWorkflow().getJob().get( "runsOn" ).toString() );
                jobParameters.setDescription( jobParameters.getWorkflow().getJob().get( "description" ).toString() );
                jobParameters.setName( jobParameters.getWorkflow().getJob().get( "name" ).toString() );
                jobParameters.setRunsOn( jobParameters.getWorkflow().getJob().get( "runsOn" ).toString() );
                jobParameters.setWorkingDir( JsonUtils.jsonToObject(
                        JsonUtils.objectToJson( jobParameters.getWorkflow().getJob().get( "workingDir" ) ), EngineFile.class ) );
            } else {
                jobParameters.setName( jobParameters.getWorkflow().getName() + "_run" );
                jobParameters.setRunsOn( LocationsEnum.DEFAULT_LOCATION.getId() );
            }
            daemonManager.runScheme( jobParameters );
            logger.info( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ) );
            return new ResponseEntity<>( ResponseUtils.successResponse( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ),
                    new JobSubmitResponseDTO( jobParameters.getId(), jobParameters.getName() ) ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Gets the daemon manager.
     *
     * @return the daemon manager
     */
    public WorkflowDaemonManager getDaemonManager() {
        return daemonManager;
    }

    /**
     * Sets the daemon manager.
     *
     * @param daemonManager
     *         the new daemon manager
     */
    public void setDaemonManager( WorkflowDaemonManager daemonManager ) {
        this.daemonManager = daemonManager;
    }

}
