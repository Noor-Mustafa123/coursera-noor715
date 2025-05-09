package de.soco.software.simuspace.suscore.location.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.model.Directory;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.model.UserUrlDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.location.manager.SsfsManager;
import de.soco.software.simuspace.suscore.location.service.rest.SsfsService;

/**
 * The Class SsfsServiceImpl for server side selection api's implementation.
 */
public class SsfsServiceImpl extends SuSBaseService implements SsfsService {

    /**
     * The location manager.
     */
    private SsfsManager ssfsManager;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    private static final String LOCATION = "Location";

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLocationList() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.SSFS_LOCATIONS.getKey() ) ),
                    locationManager.getLocationList( getUserIdFromGeneralHeader() ) );

        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveUserUrlInHistory( String locationId, String objectJson ) {

        try {
            UserUrlDTO userUrl = JsonUtils.jsonToObject( objectJson, UserUrlDTO.class );
            UserUrlDTO retUserUrlDTO = ssfsManager.saveOrUpdateUserUrlHistory( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                    UUID.fromString( locationId ), userUrl );
            return ResponseUtils.success( retUserUrlDTO );

        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getFileContent( String objectJson ) {
        try {
            Directory directry = JsonUtils.jsonToObject( objectJson, Directory.class );
            return ResponseUtils.success( ssfsManager.getFileContent( directry ) );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getUserUrl( String locationId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.SSFS_LOCATION_URL.getKey() ) ),
                    ssfsManager.getUserUrlHistory( getUserIdFromGeneralHeader(), UUID.fromString( locationId ) ) );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFileList( String objectJson ) {
        try {
            Directory directory = JsonUtils.jsonToObject( objectJson, Directory.class );
            locationManager.isPermitted( getUserIdFromGeneralHeader(), directory.getLocationId().toString(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), LOCATION );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.SSFS_FILES_LIST.getKey() ) ),
                    ssfsManager.getFiles( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), directory ) );

        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the ssfs manager.
     *
     * @return the ssfs manager
     */
    public SsfsManager getSsfsManager() {
        return ssfsManager;
    }

    /**
     * Sets the ssfs manager.
     *
     * @param ssfsManager
     *         the new ssfs manager
     */
    public void setSsfsManager( SsfsManager ssfsManager ) {
        this.ssfsManager = ssfsManager;
    }

    /**
     * Gets the location manager.
     *
     * @return the location manager
     */
    public LocationManager getLocationManager() {
        return locationManager;
    }

    /**
     * Sets the location manager.
     *
     * @param locationManager
     *         the new location manager
     */
    public void setLocationManager( LocationManager locationManager ) {
        this.locationManager = locationManager;
    }

}
