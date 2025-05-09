package de.soco.software.simuspace.suscore.location.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.core.service.SelectionReadService;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.location.service.rest.LocationService;

/**
 * Implementation Class for Interface responsible for all rest services related to locations.
 *
 * @author M.Nasir.Farooq
 */
public class LocationServiceImpl extends SuSBaseService implements LocationService, SelectionReadService {

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addNewLocation( String locationJson ) {
        try {
            LocationDTO locationDTO = JsonUtils.jsonToObject( locationJson, LocationDTO.class );
            LocationDTO retLocationDTO = locationManager.addNewLocation( getUserIdFromGeneralHeader(), locationDTO );
            return ResponseUtils.success( retLocationDTO );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listLocationUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_DATAOBJECT_LOCATION.getKey() ) ),
                    new TableUI( locationManager.listLocationUI(),
                            locationManager.getUserObjectViewsByKey( ConstantsObjectViewKey.LOCATION_TABLE_KEY,
                                    getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response singleLocationUI() {
        try {
            return ResponseUtils.success( locationManager.singleLocationUI() );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createLocationUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_LOCATION.getKey() ) ),
                    locationManager.getCreateLocationForm( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createLocation( String objectJson ) {
        try {
            LocationDTO locationDTO = JsonUtils.jsonToObject( objectJson, LocationDTO.class );
            LocationDTO retLocationDTO = locationManager.createLocation( getUserIdFromGeneralHeader(), locationDTO );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.CREATED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_LOCATION.getKey() ) ), retLocationDTO );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLocation( String id ) {
        try {
            return ResponseUtils.success( locationManager.getLocation( id ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllLocations( String locationFilterJson ) {
        try {
            FiltersDTO filter = JsonUtils.jsonToObject( locationFilterJson, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_DATAOBJECT_LOCATION.getKey() ) ),
                    locationManager.getAllLocations( getUserIdFromGeneralHeader(), filter ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteLocation( String id ) {
        try {
            return ResponseUtils.success( locationManager.deleteLocation( getUserIdFromGeneralHeader(), id ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateLocation( String objectJson ) {
        try {
            LocationDTO locationDTO = JsonUtils.jsonToObject( objectJson, LocationDTO.class );
            LocationDTO retLocationDTO = locationManager.updateLocation( getUserIdFromGeneralHeader(), locationDTO );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.UPDATED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_LOCATION.getKey() ) ), retLocationDTO );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllSelectionsWithPagination( String selectionId, String filterJson ) {
        try {
            return ResponseUtils.success( locationManager.getAllLocationTableSelection( selectionId, getUserIdFromGeneralHeader(),
                    JsonUtils.jsonToObject( filterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllSelectionsWithoutPagination( String selectionId ) {
        try {
            return ResponseUtils.success( locationManager.getAllLocationBySelectionId( selectionId, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listLocationWorkflowUI() {
        try {
            return ResponseUtils.success( new TableUI( locationManager.listLocationUI(),
                    locationManager.getUserObjectViewsByKey( ConstantsObjectViewKey.LOCATION_TABLE_KEY, getUserIdFromGeneralHeader(),
                            null ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllLocationsIncludeInternal( String locationFilterJson ) {
        try {
            FiltersDTO filter = JsonUtils.jsonToObject( locationFilterJson, FiltersDTO.class );
            return ResponseUtils.success( locationManager.getAllLocationsIncludeInternal( getUserIdFromGeneralHeader(), filter ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllLocationsWithSelection( String selectionId, String locationFilterJson ) {
        try {
            FiltersDTO filter = JsonUtils.jsonToObject( locationFilterJson, FiltersDTO.class );
            return ResponseUtils.success(
                    locationManager.getAllLocationsWithSelection( getUserIdFromGeneralHeader(), selectionId, filter ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLocationListWithSelectionId( String selectionId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.LOCATIONS_WITH_SELECTIONID.getKey() ) + " " + selectionId ),
                    locationManager.getLocationEntitiesBySelectionId( getUserIdFromGeneralHeader(), selectionId ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLocalLocationList() {
        try {
            return ResponseUtils.success( locationManager.getLocalLocationList( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContextRouter( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            List< ContextMenuItem > contextMenuItems = locationManager.getContextRouter( filter );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.CONTEXT_MENU_FETCHED.getKey() ), contextMenuItems );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllViews() {
        try {
            return ResponseUtils.success(
                    locationManager.getUserObjectViewsByKey( ConstantsObjectViewKey.LOCATION_TABLE_KEY, getUserIdFromGeneralHeader(),
                            null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getView( String viewId ) {
        try {
            return ResponseUtils.success( locationManager.getObjectViewById( UUID.fromString( viewId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.LOCATION_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    locationManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    locationManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.LOCATION_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteView( String viewId ) {
        try {
            if ( locationManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.LOCATION_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    locationManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
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

    @Override
    public Response editLocationForm( UUID locationId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.FORM_CREATED.getKey(),
                            MessageBundleFactory.getMessage( Messages.EDIT_LOCATION.getKey() ) ),
                    locationManager.getEditLocationForm( getUserIdFromGeneralHeader(), locationId ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getLocalStagingPath() {
        try {
            return ResponseUtils.success( locationManager.getUserStagingPath( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getKarafPath() {
        try {
            return ResponseUtils.success( locationManager.getKarafPath( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getPythonPath() {
        try {
            return ResponseUtils.success( PropertiesManager.getPythonExecutionPathOnServer() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllWorkflowViews() {
        try {
            return ResponseUtils.success(
                    locationManager.getUserObjectViewsByKey( ConstantsObjectViewKey.LOCATION_TABLE_KEY, getUserIdFromGeneralHeader(),
                            null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowView( String viewId ) {
        try {
            return ResponseUtils.success( locationManager.getObjectViewById( UUID.fromString( viewId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveWorkflowView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.LOCATION_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    locationManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setWorkflowViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    locationManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.LOCATION_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteWorkflowView( String viewId ) {
        try {
            if ( locationManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateWorkflowView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.LOCATION_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    locationManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getAllValuesForLocationsTableColumn( String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    locationManager.getAllValuesForLocationsTableColumn( column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
