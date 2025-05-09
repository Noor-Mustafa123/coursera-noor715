package de.soco.software.simuspace.suscore.core.location.service.rest.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.TransferLocationObject;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.InternalExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.core.location.manager.LocationCoreManager;
import de.soco.software.simuspace.suscore.core.location.manager.impl.LocationCoreManagerImpl;
import de.soco.software.simuspace.suscore.core.location.service.rest.LocationCoreService;
import de.soco.software.simuspace.workflow.location.JobParametersLocationModel;

/**
 * Implementation Class for Interface responsible for all rest services related to remote locations.
 *
 * @author M.Nasir.Farooq
 */
public class LocationCoreServiceImpl implements LocationCoreService {

    /**
     * The Constant PATH_KEY.
     */
    private static final String PATH_KEY = "path";

    /**
     * The Constant DIRECTORY_ONLY.
     */
    private static final String DIRECTORY_ONLY = "dirOnly";

    /**
     * The Constant USER_NAME.
     */
    private static final String USER_NAME = "userName";

    /**
     * The Constant PASS.
     */
    private static final String PASS = "password";

    /**
     * The location manager.
     */
    private LocationCoreManager locationManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response isUp( String token ) {
        try {
            locationManager.isValidToken( token );
            return ResponseUtils.success( locationManager.isUp() );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response exportVaultFile( String token, String transferObjStr ) {
        try {
            locationManager.isValidToken( token );
            TransferLocationObject transferObject = JsonUtils.jsonToObject( transferObjStr, TransferLocationObject.class );
            return ResponseUtils.success( locationManager.exportVaultFile( transferObject ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response exportStagingFile( String userUid, String locationAuthToken, String transferObjStr ) {
        try {
            locationManager.isValidToken( locationAuthToken );
            TransferLocationObject transferObject = JsonUtils.jsonToObject( transferObjStr, TransferLocationObject.class );
            return ResponseUtils.success( locationManager.exportStagingFile( userUid, transferObject ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadVaultFile( String token, UUID id, int version ) {
        try {
            locationManager.isValidToken( token );
            File file = locationManager.getVaultFile( id, version );
            if ( null == file ) {
                throw new SusException( LocationCoreManagerImpl.FILE_NOT_EXISTS_IN_VAULT );
            }
            ResponseBuilder response = Response.ok( file );
            response.header( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
            response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            return response.build();
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response isPathExists( String token, String pathJson ) {
        try {
            locationManager.isValidToken( token );
            Map< String, String > pathMap = new HashMap<>();
            pathMap = ( Map< String, String > ) JsonUtils.jsonToMap( pathJson, pathMap );
            return ResponseUtils.success( locationManager.isPathExists( pathMap.get( PATH_KEY ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteVaultFile( String token, String pathJson ) {
        try {
            locationManager.isValidToken( token );
            Map< String, String > pathMap = new HashMap<>();
            pathMap = ( Map< String, String > ) JsonUtils.jsonToMap( pathJson, pathMap );
            return ResponseUtils.success( locationManager.deleteVaultFile( pathMap.get( PATH_KEY ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFileList( String locationAuthToken, String pathJson ) {
        try {
            locationManager.isValidToken( locationAuthToken );
            Map< String, String > pathMap = new HashMap<>();
            pathMap = ( Map< String, String > ) JsonUtils.jsonToMap( pathJson, pathMap );
            return ResponseUtils.success( locationManager.getFileList( pathMap.get( USER_NAME ), pathMap.get( PASS ),
                    pathMap.get( PATH_KEY ), pathMap.get( DIRECTORY_ONLY ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the user home.
     *
     * @param token
     *         the token
     * @param pathJson
     *         the path json
     *
     * @return the user home
     */
    @Override
    public Response getUserHome( String token, String pathJson ) {
        try {
            locationManager.isValidToken( token );
            Map< String, String > pathMap = new HashMap<>();
            pathMap = ( Map< String, String > ) JsonUtils.jsonToMap( pathJson, pathMap );
            return ResponseUtils.success( locationManager.getUserHome( pathMap.get( USER_NAME ), pathMap.get( PASS ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * Handle exception.
     *
     * @param exception
     *         the e
     *
     * @return the response
     */
    protected Response handleException( Exception exception ) {

        if ( exception instanceof SusException susException ) {
            final String userName = BundleUtils.getUserNameFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() );
            ExceptionLogger.logException( userName, susException, getClass() );
            return ResponseUtils.failure( susException.getStatusCode(), susException.getMessage() );
        }
        if ( exception instanceof SusDataBaseException susDataBaseException ) {
            final String userName = BundleUtils.getUserNameFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() );
            ExceptionLogger.logException( userName, susDataBaseException, getClass() );
            return ResponseUtils.failure( susDataBaseException.getStatusCode(),
                    MessageBundleFactory.getMessage( Messages.DB_DATABASE_QUERY_ERROR.getKey() ) );
        } else {
            InternalExceptionLogger.logException( exception, getClass() );
            return ResponseUtils.failure( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    MessageBundleFactory.getMessage( Messages.WEBSERVICE_INTERNAL_SERVER_ERROR.getKey() ) );

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response runWorkflow( String jobParametersString ) {

        JobParametersLocationModel jobParametersLocationModel = null;
        try {
            jobParametersLocationModel = JsonUtils.jsonToObject( jobParametersString, JobParametersLocationModel.class );
        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, getClass() );
            return ResponseUtils.failure( e.getMessage() );
        }
        try {

            locationManager.runServerJob( jobParametersLocationModel );
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ),
                    MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }

    }

    /**
     * Run system workflow.
     *
     * @param jobParametersString
     *         the job parameters string
     *
     * @return the response
     */
    @Override
    public Response runSystemWorkflow( String jobParametersString ) {

        JobParametersLocationModel jobParametersLocationModel = null;
        try {
            jobParametersLocationModel = JsonUtils.jsonToObject( jobParametersString, JobParametersLocationModel.class );
        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, getClass() );
            return ResponseUtils.failure( e.getMessage() );
        }
        try {
            locationManager.runServerSystemJob( jobParametersLocationModel );
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ),
                    MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }

    }

    /**
     * Gets the location manager.
     *
     * @return the location manager
     */
    public LocationCoreManager getLocationManager() {
        return locationManager;
    }

    /**
     * Sets the location manager.
     *
     * @param locationManager
     *         the new location manager
     */
    public void setLocationManager( LocationCoreManager locationManager ) {
        this.locationManager = locationManager;
    }

}
