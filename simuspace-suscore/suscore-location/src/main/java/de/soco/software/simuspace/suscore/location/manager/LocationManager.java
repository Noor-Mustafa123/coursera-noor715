package de.soco.software.simuspace.suscore.location.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.Location;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.location.dao.LocationDAO;

/**
 * Interface To Handle All business operations related To locations.
 *
 * @author M.Nasir.Farooq
 */
public interface LocationManager {

    /**
     * Adds the new location.
     *
     * @param userId
     *         the user id from general header
     * @param locationDTO
     *         the location DTO
     *
     * @return the location DTO
     *
     * @apiNote To be used in service calls only
     */
    LocationDTO addNewLocation( String userId, LocationDTO locationDTO );

    /**
     * Creates the location.
     *
     * @param userId
     *         the user id
     * @param locationDTO
     *         the location DTO
     *
     * @return the location DTO
     *
     * @apiNote To be used in service calls only
     */
    LocationDTO createLocation( String userId, LocationDTO locationDTO );

    /**
     * Gets the location.
     *
     * @param id
     *         the id
     *
     * @return the location
     *
     * @apiNote To be used in service calls only
     */
    LocationDTO getLocation( String id );

    /**
     * Gets location.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the location
     */
    LocationDTO getLocation( EntityManager entityManager, String id );

    /**
     * To view single location.
     *
     * @return the list< TableColumn >
     */
    List< TableColumn > singleLocationUI();

    /**
     * Gets the all locations.
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the all locations
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LocationDTO > getAllLocations( String userId, FiltersDTO filter );

    /**
     * Gets the all locations include internal.
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the all locations include internal
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LocationDTO > getAllLocationsIncludeInternal( String userId, FiltersDTO filter );

    /**
     * Gets the all locations.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     * @param filter
     *         the filter
     *
     * @return the all locations
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LocationDTO > getAllLocationsWithSelection( String userId, String selectionId, FiltersDTO filter );

    /**
     * Delete location.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteLocation( String userId, String id );

    /**
     * Update location.
     *
     * @param userId
     *         the user id
     * @param locationDTO
     *         the location DTO
     *
     * @return the location DTO
     *
     * @apiNote To be used in service calls only
     */
    LocationDTO updateLocation( String userId, LocationDTO locationDTO );

    /**
     * List users UI.
     *
     * @return the list
     */
    List< TableColumn > listLocationUI();

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets the creates the location form.
     *
     * @param userId
     *         the user id
     * @param locationId
     *         the location id
     *
     * @return the creates the location form
     */
    UIForm getCreateLocationForm( String userId );

    /**
     * Gets the all location table selection.
     *
     * @param selectionId
     *         the selection id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param jsonToObject
     *         the json to object
     *
     * @return the all location table selection
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LocationDTO > getAllLocationTableSelection( String selectionId, String userIdFromGeneralHeader,
            FiltersDTO jsonToObject );

    /**
     * Gets the location entities by selection id.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     *
     * @return the location entities by selection id
     *
     * @apiNote To be used in service calls only
     */
    List< LocationDTO > getAllLocationBySelectionId( String userId, String selectionId );

    /**
     * Gets the location entities by selection id.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     *
     * @return the location entities by selection id
     *
     * @apiNote To be used in service calls only
     */
    List< LocationEntity > getLocationEntitiesBySelectionId( String userId, String selectionId );

    /**
     * Gets location entities by selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     *
     * @return the location entities by selection id
     */
    List< LocationEntity > getLocationEntitiesBySelectionId( EntityManager entityManager, String userId, String selectionId );

    /**
     * Gets the local location list.
     *
     * @param userId
     *         the user id
     *
     * @return the local location list
     *
     * @apiNote To be used in service calls only
     */
    List< LocationDTO > getLocalLocationList( String userId );

    /**
     * Gets the edits the location form.
     *
     * @param userId
     *         the user id
     * @param locationId
     *         the location id
     *
     * @return the edits the location form
     *
     * @apiNote To be used in service calls only
     */
    UIForm getEditLocationForm( String userId, UUID locationId );

    /**
     * Gets the context router.
     *
     * @param filter
     *         the filter
     *
     * @return the context router
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getContextRouter( FiltersDTO filter );

    /**
     * Gets the location list.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the location list
     *
     * @apiNote To be used in service calls only
     */
    List< Location > getLocationList( String userIdFromGeneralHeader );

    /**
     * Gets the location DAO.
     *
     * @return the location DAO
     */
    LocationDAO getLocationDAO();

    /**
     * Prepare DTO.
     *
     * @param locationEntity
     *         the location entity
     *
     * @return the location DTO
     */
    LocationDTO prepareDTO( LocationEntity locationEntity );

    /**
     * Gets user staging path.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the user staging path
     *
     * @apiNote To be used in service calls only
     */
    String getUserStagingPath( String userIdFromGeneralHeader );

    /**
     * Gets user staging path.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the user staging path
     */
    String getKarafPath( String userIdFromGeneralHeader );

    List< LocationDTO > getAllLocationsWithSelection( EntityManager entityManager, String userId, String selectionId );

    /**
     * Validate paths.
     *
     * @param locationDTO
     *         the location DTO
     */
    void validatePaths( LocationDTO locationDTO );

    /**
     * Save or update object view.
     *
     * @param viewDTO
     *         the view DTO
     * @param userId
     *         the user id
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveOrUpdateObjectView( ObjectViewDTO viewDTO, String userId );

    /**
     * Save default object view.
     *
     * @param viewId
     *         the view id
     * @param userId
     *         the user id
     * @param objectViewKey
     *         the object view key
     * @param objectId
     *         the object id (set null if its not for any other object)
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveDefaultObjectView( UUID viewId, String userId, String objectViewKey, String objectId );

    /**
     * Gets the user object views by key.
     *
     * @param key
     *         the key
     * @param userId
     *         the user id
     * @param objectId
     *         the object id (set null if its not for any other object)
     *
     * @return the user object views by key
     *
     * @apiNote To be used in service calls only
     */
    List< ObjectViewDTO > getUserObjectViewsByKey( String key, String userId, String objectId );

    /**
     * Delete object view.
     *
     * @param viewId
     *         the view id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteObjectView( UUID viewId );

    /**
     * Gets the object view by id.
     *
     * @param viewId
     *         the view id
     *
     * @return the object view by id
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO getObjectViewById( UUID viewId );

    /**
     * Gets all values for locations table column.
     *
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for locations table column
     */
    List< Object > getAllValuesForLocationsTableColumn( String columnName, String token );

    boolean isPermitted( String userId, String locationId, String message, String name );

}
