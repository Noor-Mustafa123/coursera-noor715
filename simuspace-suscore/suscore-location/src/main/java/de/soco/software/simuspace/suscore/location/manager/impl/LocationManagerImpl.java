package de.soco.software.simuspace.suscore.location.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExecutionHosts;
import de.soco.software.simuspace.suscore.common.util.Hosts;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.entity.AclClassEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.Location;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.location.dao.LocationDAO;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.permissions.dao.AclClassDAO;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * The Class is responsible for providing to methods to save and retrieve the data mainly for locations.
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class LocationManagerImpl implements LocationManager {

    /**
     * The Constant LOCATION_URL_SHOULD_NOT_BE_NULL_OR_EMPTY.
     */
    private static final String LOCATION_URL_SHOULD_NOT_BE_NULL_OR_EMPTY = "Location url should not be null or empty";

    /**
     * The Constant YOU_HAVE_ALREADY_CONSUMED_LOCATION_LIMIT.
     */
    private static final String YOU_HAVE_ALREADY_CONSUMED_LOCATION_LIMIT = "Either you are super user or you have already consumed location limit.";

    /**
     * The Constant LOCATION.
     */
    private static final String LOCATION = "Location";

    /**
     * The Constant LOCATION_FEATURE_PRE_FIX.
     */
    private static final String LOCATION_FEATURE_PRE_FIX = "Location-";

    /**
     * The Constant API_ISEXISTS.
     */
    private static final String API_ISEXISTS = "/api/core/location/isexists";

    /**
     * The Constant PATH_KEY.
     */
    private static final String PATH_KEY = "path";

    /**
     * The Constant LOCATION_URL_IS_INVALID.
     */
    private static final String LOCATION_URL_IS_INVALID = "Location Server is Not accessible";

    /**
     * The Constant VAULT_PATH_DOES_NOT_EXIST_ON_LOCATION.
     */
    private static final String VAULT_PATH_DOES_NOT_EXIST_ON_LOCATION = "Vault path does not exist on location: ";

    /**
     * The Constant STAGING_PATH_DOES_NOT_EXIST_ON_LOCATION.
     */
    private static final String STAGING_PATH_DOES_NOT_EXIST_ON_LOCATION = "Staging path does not exist on location: ";

    /**
     * The Constant RENDER_TYPE_LINK.
     */
    private static final String RENDER_TYPE_LINK = "link";

    /**
     * The Constant SYSTEM_LOCATION_URL.
     */
    private static final String SYSTEM_LOCATION_URL = "system/location/{id}";

    /**
     * The Constant TYPE_SELECT.
     */
    private static final String TYPE_SELECT = "select";

    /**
     * The Constant NAME_KEY.
     */
    private static final String NAME_KEY = "name";

    /**
     * The Constant LOCATIONS_OPTION_TITLE.
     */
    private static final String LOCATIONS_OPTION_TITLE = "Locations";

    /**
     * The Constant BIND_FROM_URL_FOR_CREATE_LOCATION_UI_FORM.
     */
    private static final String BIND_FROM_URL_FOR_CREATE_LOCATION_UI_FORM = "/system/location/ui/create/{__value__}";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant TYPE_LOCATION.
     */
    private static final String TYPE_LOCATION = LOCATION;

    /**
     * The Constant TYPE_EXECUTION_ONLY.
     */
    private static final String TYPE_EXECUTION_ONLY = "Execution-only";

    /**
     * The Constant TYPE.
     */
    private static final String TYPE = "type";

    /**
     * The Constant VAULT.
     */
    private static final String VAULT = "vault";

    /**
     * The Constant STAGING.
     */
    private static final String STAGING = "staging";

    /**
     * The Constant URL.
     */
    private static final String URL = "url";

    /**
     * The Constant AUTHTOKEN.
     */
    private static final String AUTHTOKEN = "authToken";

    /**
     * The Constant USER_PLUGIN_NAME.
     */
    private static final String LOCATION_PLUGIN_NAME = "plugin_location";

    /**
     * The location DAO.
     */
    private LocationDAO locationDAO;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    private AclClassDAO classDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The locations.
     */
    private static List< LocationDTO > locations = new LinkedList<>( List.of( new LocationDTO( UUID.randomUUID(), "Add new Location" ) ) );

    /**
     * Inits the.
     */
    public void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !PropertiesManager.isMasterLocation() ) {
                return;
            }

            if ( null == getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() ) ) {
                LocationDTO locationDefault = new LocationDTO( UUID.fromString( LocationsEnum.DEFAULT_LOCATION.getId() ),
                        LocationsEnum.DEFAULT_LOCATION.getName() );
                locationDefault.setStatus( ConstantsStatus.ACTIVE );
                locationDefault.setVault( PropertiesManager.getVaultPath() );
                locationDefault.setStaging( PropertiesManager.getStagingPath() );
                locationDefault.setUrl( PropertiesManager.getLocationURL() );
                locationDefault.setAuthToken( PropertiesManager.getLocationAuthToken() );
                locationDefault.setInternal( true );

                permissionManager.addFeatures( entityManager, permissionManager.prepareResourceAccessControlDTO( locationDefault.getId(),
                        LOCATION_FEATURE_PRE_FIX + locationDefault.getName(), LocationEntity.class.getName(),
                        locationDefault.getDescription() ) );

                createLocation( entityManager, locationDefault );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the location.
     *
     * @param location
     *         the location
     */
    private void createLocation( EntityManager entityManager, LocationDTO location ) {
        LocationEntity locationEntity = location.prepareEntity();
        locationEntity.setCreatedOn( new Date() );
        locationEntity.setInternal( true );
        locationEntity.setCreatedBy( userManager.getUserEntityById( entityManager, UUID.fromString( ConstantsID.SUPER_USER_ID ) ) );
        locations.remove( location );
        locationDAO.save( entityManager, locationEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDTO addNewLocation( String userId, LocationDTO locationDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            boolean alreadyExist = false;
            if ( locations != null && !locations.isEmpty() ) {
                for ( LocationDTO location : locations ) {
                    if ( StringUtils.isNotNullOrEmpty( location.getUrl() ) && location.getUrl().equalsIgnoreCase( locationDTO.getUrl() ) ) {
                        alreadyExist = true;
                        break;
                    }
                }
            }
            if ( !alreadyExist && locationDAO != null ) {
                for ( LocationEntity location : locationDAO.getAllObjectList( entityManager ) ) {
                    if ( StringUtils.isNotNullOrEmpty( location.getUrl() ) && location.getUrl()
                            .equalsIgnoreCase( locationDTO.getUrl().trim() ) ) {
                        alreadyExist = true;
                        break;
                    }
                }

            }
            if ( !alreadyExist ) {
                locationDTO.setId( UUID.randomUUID() );
                locations.add( locationDTO );
            } else {
                throw new SusException( "Location Already Exists." );
            }

            return locationDTO;

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDTO createLocation( String userId, LocationDTO locationDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.LOCATIONS.getId(),
                    PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), LOCATION );

            validatePaths( locationDTO );

            UserEntity userEntity = new UserEntity();
            userEntity.setId( UUID.fromString( userId ) );

            LocationEntity locationEntity = locationDTO.prepareEntity();
            locationEntity.setCreatedBy( userEntity );
            locationEntity.setCreatedOn( new Date() );

            locations.remove( locationDTO );

            permissionManager.addFeatures( entityManager, permissionManager.prepareResourceAccessControlDTO( locationEntity.getId(),
                    LOCATION_FEATURE_PRE_FIX + locationEntity.getName(), locationEntity.getClass().getName(),
                    locationEntity.getDescription() ) );

            LocationDTO savedLocation = prepareDTO( locationDAO.save( entityManager, locationEntity ) );
            entityManager.close();
            return savedLocation;
        } catch ( Exception e ) {
            entityManager.close();
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Gets the size non internl locations.
     *
     * @return the size non internl locations
     */
    private int getSizeNonInternlLocations( EntityManager entityManager ) {
        int numberOfNonInternalLocations = 0;
        List< LocationEntity > locationEntities = locationDAO.getAllObjectList( entityManager );
        for ( LocationEntity locationEntity : locationEntities ) {
            if ( ( locationEntity.getId().toString().equals( LocationsEnum.DEFAULT_LOCATION.getId() ) || !locationEntity.isInternal() )
                    && locationEntity.isStatus() ) {
                numberOfNonInternalLocations++;
            }
        }
        return numberOfNonInternalLocations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validatePaths( LocationDTO locationDTO ) {
        if ( StringUtils.isNullOrEmpty( locationDTO.getUrl() ) ) {
            throw new SusException( LOCATION_URL_SHOULD_NOT_BE_NULL_OR_EMPTY );
        }
        if ( !isPathExists( locationDTO.getVault(), locationDTO.getUrl(), locationDTO.getAuthToken() ) ) {
            throw new SusException( VAULT_PATH_DOES_NOT_EXIST_ON_LOCATION + locationDTO.getName() );
        } else if ( !isPathExists( locationDTO.getStaging(), locationDTO.getUrl(), locationDTO.getAuthToken() ) ) {
            throw new SusException( STAGING_PATH_DOES_NOT_EXIST_ON_LOCATION + locationDTO.getName() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveOrUpdateObjectView( ObjectViewDTO viewDTO, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.saveOrUpdateObjectView( entityManager, viewDTO, userId );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public ObjectViewDTO saveDefaultObjectView( UUID viewId, String userId, String objectViewKey, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.saveDefaultObjectView( entityManager, viewId, userId, objectViewKey, objectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewDTO > getUserObjectViewsByKey( String key, String userId, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.getUserObjectViewsByKey( entityManager, key, userId, objectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteObjectView( UUID viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.deleteObjectView( entityManager, viewId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO getObjectViewById( UUID viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.getObjectViewById( entityManager, viewId );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< Object > getAllValuesForLocationsTableColumn( String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( LocationDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            allValues = locationDAO.getAllPropertyValues( entityManager, columnName );
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDTO getLocation( String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getLocation( entityManager, id );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public LocationDTO getLocation( EntityManager entityManager, String id ) {
        return prepareDTO( locationDAO.getLatestNonDeletedLocationById( entityManager, UUID.fromString( id ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > singleLocationUI() {

        return GUIUtils.listColumns( LocationDTO.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< LocationDTO > getAllLocations( String userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< LocationEntity > allFilteredRecords = locationDAO.getAllFilteredNonDeletedLocationsWithPermissions( entityManager, filter,
                    userId, PermissionMatrixEnum.VIEW.getKey() );
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.LOCATIONS.getId(), PermissionMatrixEnum.READ.getValue(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), LOCATION );
            List< LocationDTO > locationList = allFilteredRecords.stream().map( this::prepareDTO ).collect( Collectors.toList() );
            return PaginationUtil.constructFilteredResponse( filter, locationList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< LocationDTO > getAllLocationsIncludeInternal( String userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return PaginationUtil.constructFilteredResponse( filter, prepareDTOIncludeInternal( entityManager, userId,
                    locationDAO.getAllFilteredRecords( entityManager, LocationEntity.class, filter ) ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteLocation( String userId, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            LocationEntity locationEntity = new LocationEntity();
            locationEntity.setId( UUID.fromString( id ) );
            locationDAO.delete( entityManager, locationEntity );
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDTO updateLocation( String userId, LocationDTO locationDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.LOCATIONS.getId(), PermissionMatrixEnum.WRITE.getValue(),
                    Messages.NO_RIGHTS_TO_UPDATE.getKey(), LOCATION );

            UserEntity userEntity = new UserEntity();
            userEntity.setId( UUID.fromString( userId ) );

            LocationEntity updateLocation = locationDAO.getLatestNonDeletedObjectById( entityManager, locationDTO.getId() );
            validatePaths( locationDTO );
            AclClassEntity aclClassEntity = classDAO.getAclClassByQualifiedName( entityManager,
                    LOCATION_FEATURE_PRE_FIX + updateLocation.getName() );
            if ( aclClassEntity != null ) {
                aclClassEntity.setName( LOCATION_FEATURE_PRE_FIX + locationDTO.getName() );
                classDAO.updateClass( entityManager, aclClassEntity );
            }

            if ( locationDTO.getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE )
                    && getSizeNonInternlLocations( entityManager ) >= licenseManager.getAllowedLocationsToUser( entityManager, userId ) ) {
                throw new SusException( YOU_HAVE_ALREADY_CONSUMED_LOCATION_LIMIT );
            }
            updateLocation = locationDTO.prepareEntity( updateLocation );
            updateLocation.setModifiedBy( userEntity );
            updateLocation.setModifiedOn( new Date() );
            return prepareDTO( locationDAO.saveOrUpdate( entityManager, updateLocation ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > listLocationUI() {
        List< TableColumn > columns = GUIUtils.listColumns( LocationDTO.class );
        for ( TableColumn tableColumn : columns ) {

            if ( tableColumn.getName().equalsIgnoreCase( NAME_KEY ) ) {
                tableColumn.getRenderer().setUrl( SYSTEM_LOCATION_URL );
                tableColumn.getRenderer().setType( RENDER_TYPE_LINK );
            }
        }
        return columns;
    }

    /**
     * Gets ui form items.
     *
     * @return the ui form items
     */
    private List< UIFormItem > prepareUiFormItemsFromLocations() {
        List< UIFormItem > uiOptionFormItemList = new ArrayList<>();
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( LocationDTO location : locations ) {
            options.add( new SelectOptionsUI( location.getId().toString(), location.getName() ) );
        }

        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        item.setLabel( LOCATIONS_OPTION_TITLE );
        item.setOptions( options );
        item.setName( NAME_KEY );
        item.setType( TYPE_SELECT );
        item.setReadonly( false );
        item.setBindFrom( BIND_FROM_URL_FOR_CREATE_LOCATION_UI_FORM );

        uiOptionFormItemList.add( item );
        return uiOptionFormItemList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCreateLocationForm( String userId ) {

        List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( true, new LocationDTO() );

        for ( UIFormItem uiFormItem : uiFormItemList ) {
            switch ( uiFormItem.getName() ) {
                case ConstantsDAO.STATUS -> {
                    // Location will be Created with Disable status
                    Map< String, String > map = new HashMap<>();
                    map.put( ConstantsStatus.INACTIVE, ConstantsStatus.INACTIVE );
                    GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ),
                            ConstantsStatus.INACTIVE, false );
                    setRulesAndMessageOnUI( uiFormItem );

                }
                case TYPE -> {

                    Map< String, String > map = new HashMap<>();
                    map.put( TYPE_LOCATION, TYPE_LOCATION );
                    map.put( TYPE_EXECUTION_ONLY, TYPE_EXECUTION_ONLY );
                    GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ), TYPE_LOCATION,
                            false );
                    setRulesAndMessageOnUI( uiFormItem );

                }
                case VAULT, STAGING, URL, AUTHTOKEN, NAME_KEY -> {
                    setRulesAndMessageOnUI( uiFormItem );
                }
            }
        }

        return GUIUtils.createFormFromItems( uiFormItemList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getEditLocationForm( String userId, UUID locationId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.LOCATIONS.getId(), PermissionMatrixEnum.WRITE.getValue(),
                    Messages.NO_RIGHTS_TO_UPDATE.getKey(), LOCATION );
            LocationDTO locationDTO = getLocation( entityManager, locationId.toString() );
            List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( false, locationDTO );
            for ( UIFormItem uiFormItem : uiFormItemList ) {
                switch ( uiFormItem.getName() ) {
                    case ConstantsDAO.STATUS -> // Location will be Created with Disable status
                            updateFormItemForStatus( locationDTO, uiFormItem );
                    case TYPE -> updateFormItemForType( locationDTO, uiFormItem );
                    case VAULT, STAGING, URL, AUTHTOKEN, NAME_KEY -> setRulesAndMessageOnUI( uiFormItem );
                }
            }
            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update form item for type.
     *
     * @param locationDTO
     *         the location dto
     * @param uiFormItem
     *         the ui form item
     */
    private void updateFormItemForType( LocationDTO locationDTO, UIFormItem uiFormItem ) {
        Map< String, String > map = new HashMap<>();
        map.put( TYPE_LOCATION, TYPE_LOCATION );
        map.put( TYPE_EXECUTION_ONLY, TYPE_EXECUTION_ONLY );

        GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ), locationDTO.getType(), false );
        setRulesAndMessageOnUI( uiFormItem );
    }

    /**
     * Update form item for status.
     *
     * @param locationDTO
     *         the location dto
     * @param uiFormItem
     *         the ui form item
     */
    private void updateFormItemForStatus( LocationDTO locationDTO, UIFormItem uiFormItem ) {
        Map< String, String > map = new HashMap<>();
        map.put( ConstantsStatus.ACTIVE, ConstantsStatus.ACTIVE );
        map.put( ConstantsStatus.INACTIVE, ConstantsStatus.INACTIVE );
        GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ), locationDTO.getStatus(),
                false );
        setRulesAndMessageOnUI( uiFormItem );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< LocationDTO > getAllLocationTableSelection( String selectionId, String userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SelectionItemEntity > selectedLocationIds = selectionManager.getUserSelectionsBySelectionIds( entityManager, selectionId,
                    filter );
            List< LocationDTO > locationDTOList = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedLocationIds ) ) {
                for ( SelectionItemEntity locationItem : selectedLocationIds ) {
                    LocationDTO location = prepareDTO(
                            locationDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( locationItem.getItem() ) ) );
                    if ( location != null ) {
                        locationDTOList.add( location );
                    }
                }
            }
            filter.setTotalRecords( ( long ) selectedLocationIds.size() );
            filter.setFilteredRecords( ( long ) locationDTOList.size() );
            return PaginationUtil.constructFilteredResponse( filter, locationDTOList );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< LocationDTO > getAllLocationBySelectionId( String userId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > selectedLocationIds = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
            List< LocationDTO > locationDTOList = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedLocationIds ) ) {
                for ( UUID locationId : selectedLocationIds ) {
                    LocationDTO location = prepareDTO( locationDAO.getLatestNonDeletedObjectById( entityManager, locationId ) );
                    if ( location != null ) {
                        locationDTOList.add( location );
                    }
                }
            }
            return locationDTOList;
        } finally {
            entityManager.close();
        }
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
     * Prepare DTO.
     *
     * @param allObjectList
     *         the all object list
     *
     * @return the list
     */
    private List< LocationDTO > prepareDTOForLocations( List< LocationEntity > allObjectList ) {
        List< LocationDTO > locationDTOs = new ArrayList<>();
        for ( LocationEntity locationEntity : allObjectList ) {
            locationDTOs.add( prepareDTO( locationEntity ) );
        }
        return locationDTOs;
    }

    /**
     * Prepare DTO.
     *
     * @param locationEntity
     *         the location entity
     *
     * @return the location DTO
     */
    @Override
    public LocationDTO prepareDTO( LocationEntity locationEntity ) {

        if ( locationEntity != null ) {
            if ( locationEntity.isStatus() != null ) {
                String statusStr = locationEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE;
                return new LocationDTO( locationEntity.getId(), locationEntity.getName(), locationEntity.getDescription(), statusStr,
                        locationEntity.getType(), locationEntity.getPriority(), locationEntity.getVault(), locationEntity.getStaging(),
                        locationEntity.getUrl(), locationEntity.getAuthToken(), locationEntity.isInternal() );
            } else {
                LocationDTO locationDTO = new LocationDTO();
                locationDTO.setId( locationEntity.getId() );
                locationDTO.setName( locationEntity.getName() );
                return locationDTO;
            }
        }
        return null;
    }

    /**
     * Prepare location.
     *
     * @param locationEntity
     *         the location entity
     *
     * @return the location
     */
    private Location prepareLocation( LocationEntity locationEntity ) {

        if ( locationEntity != null ) {
            String statusStr = locationEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE;

            return new Location( locationEntity.getId(), locationEntity.getName(), locationEntity.getDescription(), statusStr,
                    locationEntity.getType(), locationEntity.getPriority(), locationEntity.getVault(), locationEntity.getStaging(),
                    locationEntity.getUrl(), locationEntity.isInternal() );
        }
        return null;
    }

    /**
     * Checks if is path exists.
     *
     * @param path
     *         the path
     * @param serverAddress
     *         the server address
     * @param authToken
     *         the auth token
     *
     * @return true, if is path exists
     */
    private boolean isPathExists( String path, String serverAddress, String authToken ) {
        SusResponseDTO responseDTO;

        Map< String, String > pathMap = new HashMap<>();
        pathMap.put( PATH_KEY, path );
        try {
            responseDTO = SuSClient.postRequestWithoutRetry( serverAddress + API_ISEXISTS, JsonUtils.toJson( pathMap ),
                    CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }

        if ( responseDTO == null ) {
            throw new SusException( LOCATION_URL_IS_INVALID );
        }

        if ( responseDTO.getData() == null ) {
            throw new SusException( responseDTO.getMessage().getContent() );
        }

        return ( boolean ) responseDTO.getData();
    }

    @Override
    public List< LocationDTO > getAllLocationsWithSelection( EntityManager entityManager, String userId, String selectionId ) {
        List< LocationEntity > locationEntity = getLocationEntitiesBySelectionId( entityManager, userId, selectionId );
        return prepareDTOForLocations( locationEntity );
    }

    @Override
    public List< LocationEntity > getLocationEntitiesBySelectionId( EntityManager entityManager, String userId, String url ) {
        boolean isWeb = false;
        String selectionId;
        if ( url.contains( "client" ) ) {
            selectionId = url.split( "/" )[ 1 ];
        } else {
            selectionId = url;
            isWeb = true;
        }

        List< LocationEntity > locationList = locationDAO.getAllObjectList( entityManager );
        List< LocationEntity > retLocations = new ArrayList<>();
        for ( LocationEntity location : locationList ) {
            if ( isWeb && ( location.getId().toString().equals( LocationsEnum.LOCAL_LINUX.getId() ) || location.getId().toString()
                    .equals( LocationsEnum.LOCAL_WINDOWS.getId() ) ) ) {
                continue;
            }
            LocationEntity newEntity = new LocationEntity();
            newEntity.setId( location.getId() );
            newEntity.setName( location.getName() );
            retLocations.add( newEntity );
        }

        List< UUID > selectedIds = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
        if ( CollectionUtil.isNotEmpty( selectedIds ) ) {
            for ( UUID locationId : selectedIds ) {
                LocationEntity locationEntity = locationDAO.getLatestNonDeletedLocationById( entityManager, locationId );
                if ( isWeb && ( locationEntity.getId().toString().equals( LocationsEnum.LOCAL_LINUX.getId() ) || locationEntity.getId()
                        .toString().equals( LocationsEnum.LOCAL_WINDOWS.getId() ) ) ) {
                    continue;
                }
                if ( locationEntity != null && locationEntity.isStatus() && ( locationEntity.isInternal()
                        || userManager.hasLocationPermission( entityManager, userId, locationEntity.getId().toString(),
                        PermissionMatrixEnum.READ.getValue() ) || locationEntity.getCreatedBy().getId().toString().equals( userId ) ) ) {

                    retLocations.removeIf( location -> location.getName().equals( locationEntity.getName() ) );
                    LocationEntity newEntity = new LocationEntity();
                    newEntity.setId( ( locationEntity.getId() ) );
                    newEntity.setName( locationEntity.getName() );
                    retLocations.add( newEntity );
                }
            }
        }

        if ( Boolean.TRUE.equals( PropertiesManager.isHostEnable() ) ) {
            addExecutionHostsToLocationList( retLocations );
        }
        return retLocations;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LocationEntity > getLocationEntitiesBySelectionId( String userId, String url ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getLocationEntitiesBySelectionId( entityManager, userId, url );
        } finally {
            entityManager.close();
        }
    }

    private static void addExecutionHostsToLocationList( List< LocationEntity > retLocations ) {
        Hosts hosts = PropertiesManager.getHosts();
        if ( hosts != null && CollectionUtils.isNotEmpty( hosts.getExcutionHosts() ) ) {
            for ( ExecutionHosts executionHosts : hosts.getExcutionHosts() ) {
                LocationEntity locationEntity = new LocationEntity();
                locationEntity.setId( ( executionHosts.getId() ) );
                locationEntity.setName( executionHosts.getName() );
                retLocations.add( locationEntity );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Location > getLocationList( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.LOCATIONS.getId(), PermissionMatrixEnum.READ.getValue(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), LOCATION );
            List< LocationEntity > allObjectList = locationDAO.getAllObjectList( entityManager );
            List< Location > locationList = new ArrayList<>();

            for ( LocationEntity locationEntity : allObjectList ) {
                if ( !locationEntity.isInternal() && (
                        userManager.hasLocationPermission( entityManager, userId, locationEntity.getId().toString(),
                                PermissionMatrixEnum.READ.getValue() ) || locationEntity.getCreatedBy().getId().toString()
                                .equalsIgnoreCase( userId ) ) || locationEntity.getId().toString()
                        .equals( LocationsEnum.DEFAULT_LOCATION.getId() ) ) {
                    locationList.add( prepareLocation( locationEntity ) );
                }
            }
            return locationList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LocationDTO > getLocalLocationList( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return prepareDTOForLocations( locationDAO.getAllLocalObjectList( entityManager ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the objectViewManager to set
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * Gets the location DAO.
     *
     * @return the location DAO
     */
    @Override
    public LocationDAO getLocationDAO() {
        return locationDAO;
    }

    /**
     * Sets the location DAO.
     *
     * @param locationDAO
     *         the new location DAO
     */
    public void setLocationDAO( LocationDAO locationDAO ) {
        this.locationDAO = locationDAO;
    }

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * Prepare DTO include internal.
     *
     * @param allObjectList
     *         the all object list
     *
     * @return the list
     */
    private List< LocationDTO > prepareDTOIncludeInternal( EntityManager entityManager, String userId,
            List< LocationEntity > allObjectList ) {
        List< LocationDTO > locationDTOs = new ArrayList<>();
        for ( LocationEntity locationEntity : allObjectList ) {
            if ( locationEntity.getId().toString().equals( LocationsEnum.DEFAULT_LOCATION.getId() ) ||
                    ( locationEntity.isInternal() || userManager.hasLocationPermission( entityManager, userId,
                            locationEntity.getId().toString(), PermissionMatrixEnum.READ.getValue() ) ) && locationEntity.isStatus() ) {
                locationDTOs.add( prepareDTO( locationEntity ) );
            }
        }
        return locationDTOs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< LocationDTO > getAllLocationsWithSelection( String userId, String selectionId, FiltersDTO filterJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SelectionItemEntity > selectedUserIds = selectionManager.getUserSelectionsBySelectionIds( entityManager, selectionId,
                    filterJson );
            List< LocationDTO > locationList = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedUserIds ) ) {
                for ( SelectionItemEntity location : selectedUserIds ) {
                    LocationEntity locationEntity = locationDAO.getLatestNonDeletedObjectById( entityManager,
                            UUID.fromString( location.getItem() ) );
                    LocationDTO locationDTO = prepareDTO( locationEntity );
                    if ( locationDTO != null && locationDTO.getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE ) && (
                            locationDTO.isInternal() || userManager.hasLocationPermission( entityManager, userId,
                                    locationDTO.getId().toString(), PermissionMatrixEnum.READ.getValue() ) || locationEntity.getCreatedBy()
                                    .getId().toString().equalsIgnoreCase( userId ) ) ) {
                        locationList.add( locationDTO );
                    }
                }
            }
            filterJson.setTotalRecords( ( long ) selectedUserIds.size() );
            filterJson.setFilteredRecords( ( long ) locationList.size() );
            return PaginationUtil.constructFilteredResponse( filterJson, locationList );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextRouter( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // case of select all from data table
        try {
            if ( filter.getItems().isEmpty() && "-1".equalsIgnoreCase( filter.getLength().toString() ) ) {

                Long maxResults = locationDAO.getAllFilteredRecordCountWithParentId( entityManager, LocationEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.LOCATIONS.getId() ) );
                filter.setLength( Integer.valueOf( maxResults.toString() ) );
                List< LocationEntity > allObjectsList = locationDAO.getAllFilteredRecordsWithParent( entityManager, LocationEntity.class,
                        filter, UUID.fromString( SimuspaceFeaturesEnum.LOCATIONS.getId() ) );
                List< Object > itemsList = new ArrayList<>();
                allObjectsList.stream().forEach( entity -> itemsList.add( entity.getId() ) );

                filter.setItems( itemsList );
            }

            return contextMenuManager.getContextMenu( entityManager, LOCATION_PLUGIN_NAME, LocationDTO.class, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserStagingPath( String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserEntity user = userManager.getUserEntityById( entityManager, UUID.fromString( userIdFromGeneralHeader ) );
            if ( user != null ) {
                return PropertiesManager.getUserStagingPath( user.getUserUid() );
            } else {
                return PropertiesManager.getStagingPath();
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKarafPath( String userIdFromGeneralHeader ) {
        return PropertiesManager.getKarafPath();
    }

    @Override
    public boolean isPermitted( String userId, String locationId, String message, String name ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !userManager.hasLocationPermission( entityManager, userId, locationId, PermissionMatrixEnum.READ.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( message, name ) );
            } else {
                return true;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Gets the user manager.
     *
     * @return the user manager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets the user manager.
     *
     * @param userManager
     *         the new user manager
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * Gets the license manager.
     *
     * @return the license manager
     */
    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    /**
     * Sets the license manager.
     *
     * @param licenseManager
     *         the new license manager
     */
    public void setLicenseManager( LicenseManager licenseManager ) {
        this.licenseManager = licenseManager;
    }

    /**
     * Gets the context menu manager.
     *
     * @return the context menu manager
     */
    public ContextMenuManager getContextMenuManager() {
        return contextMenuManager;
    }

    /**
     * Sets the context menu manager.
     *
     * @param contextMenuManager
     *         the new context menu manager
     */
    public void setContextMenuManager( ContextMenuManager contextMenuManager ) {
        this.contextMenuManager = contextMenuManager;
    }

    public AclClassDAO getClassDAO() {
        return classDAO;
    }

    public void setClassDAO( AclClassDAO classDAO ) {
        this.classDAO = classDAO;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
