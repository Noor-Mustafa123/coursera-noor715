package de.soco.software.simuspace.suscore.data.manager.impl.base;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SystemGenratedViewEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.common.dao.ObjectViewDAO;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.ObjectViewEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;

/**
 * An implementation class of ObjectViewManager that manages CRUD operations related to Object's views .
 *
 * @author Zeeshan jamal
 */
@Log4j2
public class ObjectViewManagerImpl implements ObjectViewManager {

    private static final String SUS_DEFAULT_VIEW_NAME = "SusDefault";

    /**
     * The object view DAO.
     */
    private ObjectViewDAO objectViewDAO;

    /**
     * The user common DAO.
     */
    private UserCommonDAO userCommonDAO;

    /**
     * The Entity manager factory reference.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Initializes the ObjectViewManager .
     */
    public void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            createCustomViewsFromConfigForAllUsers( entityManager );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the views from plugin config for all users.
     *
     * @param entityManager
     *         the entity manager
     */
    private void createCustomViewsFromConfigForAllUsers( EntityManager entityManager ) {
        List< ObjectViewDTO > allViewsFromConfig = PropertiesManager.getInstance().getCustomView();
        var allUsers = userCommonDAO.findAll( entityManager );
        allUsers.forEach( user -> createOrUpdateAllCustomViewsForUser( entityManager, user, allViewsFromConfig ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createCustomViewsForUser( UUID userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< ObjectViewDTO > allViewsFromConfig = PropertiesManager.getInstance().getCustomView();
            UserEntity userEntity = userCommonDAO.getLatestNonDeletedObjectById( entityManager, userId );
            createOrUpdateAllCustomViewsForUser( entityManager, userEntity, allViewsFromConfig );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Checks if is system genrated view exists.
     *
     * @param entityManager
     *         the entity manager
     * @param userid
     *         the userid
     * @param systemGeneratedViewEnumId
     *         the system genrated view enum id
     *
     * @return true, if is system genrated view exists
     */
    @Override
    public boolean updateSystemGeneratedViewIfExists( EntityManager entityManager, String userid, int systemGeneratedViewEnumId ) {

        boolean systemGeneratedViewExists = false;
        List< ObjectViewEntity > userViews = objectViewDAO.getUserObjectViewsByKey( entityManager, UUID.fromString( userid ),
                ConstantsObjectViewKey.OBJECT_TREE_VIEW_KEY, null );
        for ( ObjectViewEntity viewDTO : userViews ) {
            if ( viewDTO.getObjectViewName().equals( SUS_DEFAULT_VIEW_NAME ) ) {
                systemGeneratedViewExists = true;
                String json = prepareDefaultViewJson( systemGeneratedViewEnumId );
                if ( json != null ) {
                    viewDTO.setObjectViewJson( json );
                }
                objectViewDAO.saveOrUpdate( entityManager, viewDTO );
            }
        }
        return systemGeneratedViewExists;
    }

    /**
     * Prepare and save object view DTO for system genrated views.
     *
     * @param entityManager
     *         the entity manager
     * @param userDTO
     *         the user DTO
     * @param systemGenratedViewEnumId
     *         the system genrated view enum id
     */
    @Override
    public void prepareAndSaveObjectViewDTOForSystemGenratedViews( EntityManager entityManager, UserDTO userDTO,
            int systemGenratedViewEnumId ) {
        String json = prepareDefaultViewJson( systemGenratedViewEnumId );
        if ( json != null ) {
            ObjectViewDTO viewDTO = new ObjectViewDTO( null, SUS_DEFAULT_VIEW_NAME, "object-tree-view", json, userDTO, "object-tree",
                    true );
            saveOrUpdateObjectView( entityManager, viewDTO, userDTO.getId() );
        }
    }

    /**
     * Prepare default view json.
     *
     * @param systemGenratedViewEnumId
     *         the system genrated view enum id
     *
     * @return the string
     */
    private String prepareDefaultViewJson( int systemGenratedViewEnumId ) {
        if ( systemGenratedViewEnumId == SystemGenratedViewEnum.RESTRICTED_DATAUSER.getKey() ) {
            return "[{\"id\":\"dc14ac39-1243-484a-94ba-12db7bb46930\",\"title\":\"Workflows\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false},{\"id\":\"9489ae1c-8e20-407c-8f30-f7357fb38016\",\"title\":\"System\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false},{\"id\":\"f258568b-e91c-4d6a-8b3a-922b41e2013f\",\"title\":\"Search\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"fd6aa6b6-fb12-4bea-ae22-7df7ccda657d\",\"title\":\"Jobs\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false},{\"id\":\"acbb88c3-5a12-498d-ab23-26dc7d6fc000\",\"title\":\"Deleted objects\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false},{\"id\":\"97fe736a-011f-46e3-971a-8ba77f711020\",\"title\":\"Data\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"cc24f7dc-a1f4-11e8-98d0-529269fb1459\",\"title\":\"Configuration\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false}]";
        } else if ( systemGenratedViewEnumId == SystemGenratedViewEnum.DATAUSER.getKey() ) {
            return "[{\"id\":\"dc14ac39-1243-484a-94ba-12db7bb46930\",\"title\":\"Workflows\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false},{\"id\":\"9489ae1c-8e20-407c-8f30-f7357fb38016\",\"title\":\"System\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false},{\"id\":\"f258568b-e91c-4d6a-8b3a-922b41e2013f\",\"title\":\"Search\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"fd6aa6b6-fb12-4bea-ae22-7df7ccda657d\",\"title\":\"Jobs\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"acbb88c3-5a12-498d-ab23-26dc7d6fc000\",\"title\":\"Deleted objects\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"97fe736a-011f-46e3-971a-8ba77f711020\",\"title\":\"Data\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"cc24f7dc-a1f4-11e8-98d0-529269fb1459\",\"title\":\"Configuration\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false}]";
        } else if ( systemGenratedViewEnumId == SystemGenratedViewEnum.DATAUSER_WFUSER_WFMANAGER.getKey() ) {
            return "[{\"id\":\"dc14ac39-1243-484a-94ba-12db7bb46930\",\"title\":\"Workflows\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"9489ae1c-8e20-407c-8f30-f7357fb38016\",\"title\":\"System\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false},{\"id\":\"f258568b-e91c-4d6a-8b3a-922b41e2013f\",\"title\":\"Search\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"fd6aa6b6-fb12-4bea-ae22-7df7ccda657d\",\"title\":\"Jobs\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"acbb88c3-5a12-498d-ab23-26dc7d6fc000\",\"title\":\"Deleted objects\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"97fe736a-011f-46e3-971a-8ba77f711020\",\"title\":\"Data\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"cc24f7dc-a1f4-11e8-98d0-529269fb1459\",\"title\":\"Configuration\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false}]";
        } else if ( systemGenratedViewEnumId == SystemGenratedViewEnum.DATAUSER_WFUSER.getKey() ) {
            return "[{\"id\":\"dc14ac39-1243-484a-94ba-12db7bb46930\",\"title\":\"Workflows\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"9489ae1c-8e20-407c-8f30-f7357fb38016\",\"title\":\"System\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false},{\"id\":\"f258568b-e91c-4d6a-8b3a-922b41e2013f\",\"title\":\"Search\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"fd6aa6b6-fb12-4bea-ae22-7df7ccda657d\",\"title\":\"Jobs\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"acbb88c3-5a12-498d-ab23-26dc7d6fc000\",\"title\":\"Deleted objects\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"97fe736a-011f-46e3-971a-8ba77f711020\",\"title\":\"Data\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"cc24f7dc-a1f4-11e8-98d0-529269fb1459\",\"title\":\"Configuration\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false}]";
        } else if ( systemGenratedViewEnumId == SystemGenratedViewEnum.RESTRICTED_DATAUSER_WFUSER.getKey() ) {
            return "[{\"id\":\"dc14ac39-1243-484a-94ba-12db7bb46930\",\"title\":\"Workflows\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"9489ae1c-8e20-407c-8f30-f7357fb38016\",\"title\":\"System\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false},{\"id\":\"f258568b-e91c-4d6a-8b3a-922b41e2013f\",\"title\":\"Search\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"fd6aa6b6-fb12-4bea-ae22-7df7ccda657d\",\"title\":\"Jobs\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"acbb88c3-5a12-498d-ab23-26dc7d6fc000\",\"title\":\"Deleted objects\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"97fe736a-011f-46e3-971a-8ba77f711020\",\"title\":\"Data\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":1,\"element\":null,\"expanded\":false},{\"id\":\"cc24f7dc-a1f4-11e8-98d0-529269fb1459\",\"title\":\"Configuration\",\"folder\":true,\"lazy\":true,\"icon\":\"fa fa-briefcase font-red\",\"description\":null,\"url\":null,\"children\":null,\"state\":0,\"element\":null,\"expanded\":false}]";
        } else {
            return null;
        }
    }

    /**
     * Creates the or update user views from plugin views.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param allViewsFromConfig
     *         the all views from config
     */
    private void createOrUpdateAllCustomViewsForUser( EntityManager entityManager, UserEntity user,
            List< ObjectViewDTO > allViewsFromConfig ) {
        allViewsFromConfig.forEach( configView -> createOrUpdateCustomViewForUser( entityManager, user, configView ) );
    }

    /**
     * Creates the or update user view from plugin view.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param configView
     *         the plugin view
     */
    private void createOrUpdateCustomViewForUser( EntityManager entityManager, UserEntity user, ObjectViewDTO configView ) {
        ObjectViewEntity userView = getUserObjectViewByKeyAndName( entityManager, user, configView );
        ObjectViewDTO viewDTO;
        try {
            viewDTO = ( ObjectViewDTO ) ByteUtil.createDeepCopyThroughSerialization( configView );
        } catch ( IOException | ClassNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.SERIALIZATION_ERROR.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.SERIALIZATION_ERROR.getKey() ), e );
        }
        if ( userView != null ) {
            // existing view
            viewDTO.setId( userView.getId().toString() );
        }
        updateVariablesInFilters( configView, user, viewDTO );
        viewDTO.setObjectViewJson( JsonUtils.toJson( viewDTO.getSettings() ) );
        saveOrUpdateObjectView( entityManager, viewDTO, user.getId().toString(), userView );
    }

    /**
     * Gets the user object view by key and name.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param pluginView
     *         the plugin view
     *
     * @return the user object view by key and name
     */
    private ObjectViewEntity getUserObjectViewByKeyAndName( EntityManager entityManager, UserEntity user, ObjectViewDTO pluginView ) {
        List< ObjectViewEntity > userViewList = objectViewDAO.getUserObjectViewsByKeyAndName( entityManager, user.getId(),
                pluginView.getObjectViewKey(), pluginView.getName() );
        return ( userViewList != null && !userViewList.isEmpty() ) ? userViewList.get( 0 ) : null;
    }

    /**
     * Update variables in filters.
     *
     * @param configView
     *         the plugin view
     * @param user
     *         the user
     * @param userView
     *         the existing view
     */
    private void updateVariablesInFilters( ObjectViewDTO configView, UserEntity user, ObjectViewDTO userView ) {
        if ( configView.getSettings() != null ) {
            configView.getSettings().getColumns().stream()
                    .filter( configFC -> ( configFC.getFilters() != null && configFC.getFilters().stream()
                            .anyMatch( configFilter -> configFilter.getValue().startsWith( ConstantsString.SUS_VARIABLE_PREFIX ) ) ) )
                    .forEach( configFC -> {
                        // plugin filter column containing variable filter
                        userView.getSettings().getColumns().stream()
                                .filter( userFC -> ( userFC.getFilters() != null && userFC.getName().equals( configFC.getName() ) ) )
                                .forEach(
                                        userFC -> userFC.setFilters( prepareFiltersWithReplacedVariables( user, configFC.getFilters() ) ) );
                    } );
        }
    }

    /**
     * Prepare filters with replaced variables.
     *
     * @param user
     *         the user
     * @param configFilters
     *         the plugin filters
     *
     * @return the list
     */
    private List< Filter > prepareFiltersWithReplacedVariables( UserEntity user, List< Filter > configFilters ) {
        List< Filter > userFilters = new ArrayList<>( configFilters.size() );
        configFilters.forEach( pluginFilter -> {
            Filter userFilter = new Filter();
            userFilter.setOperator( pluginFilter.getOperator() );
            userFilter.setCondition( pluginFilter.getCondition() );
            userFilter.setFrom( pluginFilter.getFrom() );
            userFilter.setTo( pluginFilter.getTo() );
            userFilter.setSelection( pluginFilter.getSelection() );
            if ( pluginFilter.getValue().equals( "{{currentUser}}" ) ) {
                // using string literal because only one such variable is defined. move {{currentUser}} to an enum when more variables are
                // required
                userFilter.setValue( user.getUserUid() );
            } else {
                userFilter.setValue( pluginFilter.getValue() );
            }
            userFilters.add( userFilter );
        } );
        return userFilters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewDTO > getUserObjectViewsByKey( String key, String userId, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< ObjectViewDTO > viewDTOs;
        try {
            viewDTOs = getUserObjectViewsByKey( entityManager, key, userId, objectId );
        } finally {
            entityManager.close();
        }
        return viewDTOs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewDTO > getUserObjectViewsByKey( EntityManager entityManager, String key, String userId, String objectId ) {
        List< ObjectViewDTO > viewDTOListToReurn = new ArrayList<>();
        if ( !userId.equals( ConstantsString.STRING_VALUE_ZERO ) ) {
            for ( ObjectViewEntity viewEntity : objectViewDAO.getUserObjectViewsByKey( entityManager, UUID.fromString( userId ), key,
                    objectId ) ) {
                viewDTOListToReurn.add( prepareObjectViewDTOFromViewEntity( viewEntity ) );
                entityManager.detach( viewEntity );
            }
        }
        return viewDTOListToReurn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewDTO > getUserObjectViewsByKeyAndConfig( EntityManager entityManager, String key, String userId, String config ) {
        List< ObjectViewDTO > viewDTOListToReurn = new ArrayList<>();
        if ( !userId.equals( ConstantsString.STRING_VALUE_ZERO ) ) {
            for ( ObjectViewEntity viewEntity : objectViewDAO.getUserObjectViewsByKeyAndConfig( entityManager, UUID.fromString( userId ),
                    key, config ) ) {
                viewDTOListToReurn.add( prepareObjectViewDTOFromViewEntity( viewEntity ) );
            }
        }
        return viewDTOListToReurn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveOrUpdateObjectView( ObjectViewDTO viewDTO, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ObjectViewDTO returnDTO;
        try {
            returnDTO = saveOrUpdateObjectView( entityManager, viewDTO, userId );
        } finally {
            entityManager.close();
        }
        return returnDTO;
    }

    @Override
    public ObjectViewDTO saveOrUpdateObjectView( EntityManager entityManager, ObjectViewDTO viewDTO, String userId ) {
        return saveOrUpdateObjectView( entityManager, viewDTO, userId, null );
    }

    public ObjectViewDTO saveOrUpdateObjectView( EntityManager entityManager, ObjectViewDTO viewDTO, String userId,
            ObjectViewEntity viewEntity ) {
        Notification notification = viewDTO.validate();
        if ( notification.hasErrors() ) {
            throw new SusException( notification.getErrors().toString() );
        }
        validateName( entityManager, viewDTO, userId );
        if ( viewDTO.getId() == null ) {
            viewDTO.setId( UUID.randomUUID().toString() );
        }
        var existingDefaultView = objectViewDAO.getUserDefaultObjectViewByKey( entityManager, UUID.fromString( userId ),
                viewDTO.getObjectViewKey(), viewDTO.getObjectId() );
        if ( viewDTO.isDefaultView() && existingDefaultView != null && !viewDTO.getId().equals( existingDefaultView.getId().toString() ) ) {
            removeOldDefaultView( entityManager, existingDefaultView );
        } else if ( existingDefaultView != null ) {
            entityManager.detach( existingDefaultView );
        }

        if ( viewDTO.getSettings() != null && viewDTO.getSettings().getSearch() != null
                && viewDTO.getSettings().getSearch().length() > ConstantsInteger.DEFAULT_DESCRIPTION_LENGTH ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DTO_UI_MIX_VALUE_ERROR_MESSAGE.getKey(),
                    ConstantsInteger.DEFAULT_DESCRIPTION_LENGTH ) );
        }

        return prepareObjectViewDTOFromViewEntity( objectViewDAO.saveOrUpdate( entityManager,
                prepareViewEntityFromObjectViewDTO( entityManager, viewDTO, userId, viewEntity ) ) );
    }

    private void validateName( EntityManager entityManager, ObjectViewDTO viewDTO, String userId ) {
        if ( viewDTO.getId() != null ) {
            // update call
            return;
        }
        // create or save-as call
        var userViews = objectViewDAO.getUserObjectViewsByKey( entityManager, UUID.fromString( userId ), viewDTO.getObjectViewKey(),
                viewDTO.getObjectId() );
        if ( userViews.stream().anyMatch( userView -> userView.getObjectViewName().equalsIgnoreCase( viewDTO.getName() ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.VIEW_NAME_DUPLICATE_UPDATE_EXISTING.getKey(),
                    viewDTO.getName().toLowerCase() ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveDefaultObjectView( UUID viewId, String userId, String objectViewKey, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ObjectViewDTO objectViewDTO;
        try {
            objectViewDTO = saveDefaultObjectView( entityManager, viewId, userId, objectViewKey, objectId );
        } finally {
            entityManager.close();
        }
        return objectViewDTO;
    }

    @Override
    public ObjectViewDTO saveDefaultObjectView( EntityManager entityManager, UUID viewId, String userId, String objectViewKey,
            String objectId ) {
        ObjectViewEntity viewEntityFromDb = objectViewDAO.getUserDefaultObjectViewByKey( entityManager, UUID.fromString( userId ),
                objectViewKey, objectId );
        removeOldDefaultView( entityManager, viewEntityFromDb );
        return addDefaultView( entityManager, viewId );
    }

    @Override
    public ObjectViewDTO saveDefaultObjectViewByConfig( EntityManager entityManager, UUID viewId, String userId, String objectViewKey,
            String objectId, String config ) {
        ObjectViewEntity viewEntityFromDb = objectViewDAO.getUserDefaultObjectViewByKeyAndConfig( entityManager, UUID.fromString( userId ),
                objectViewKey, objectId, config );
        removeOldDefaultView( entityManager, viewEntityFromDb );
        return addDefaultView( entityManager, viewId );
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager#deleteObjectView(java.util.UUID)
     */
    @Override
    public boolean deleteObjectView( UUID viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean isDeleted;
        try {
            isDeleted = deleteObjectView( entityManager, viewId );
        } finally {
            entityManager.close();
        }
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteObjectView( EntityManager entityManager, UUID viewId ) {
        ObjectViewEntity viewEntity = objectViewDAO.getLatestNonDeletedObjectById( entityManager, viewId );
        if ( viewEntity != null ) {
            if ( viewEntity.getObjectViewName().equals( SUS_DEFAULT_VIEW_NAME ) ) {
                throw new SusException( "Can not delete Default View :" + viewEntity.getObjectViewName() );
            }
            viewEntity.setDelete( true );
            viewEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForObjects( "Delete View : " + viewEntity.getObjectViewName(),
                    ConstantsDbOperationTypes.DELETED, "", viewId.toString(), viewEntity.getObjectViewName(), "View" ) );
            objectViewDAO.saveOrUpdate( entityManager, viewEntity );
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager#getObjectViewById(java.util.UUID)
     */
    @Override
    public ObjectViewDTO getObjectViewById( UUID viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ObjectViewDTO viewDTO;
        try {
            viewDTO = getObjectViewById( entityManager, viewId );
        } finally {
            entityManager.close();
        }
        return viewDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO getObjectViewById( EntityManager entityManager, UUID viewId ) {
        ObjectViewEntity viewEntity = objectViewDAO.getLatestNonDeletedObjectById( entityManager, viewId );
        if ( null == viewEntity ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
        } else {
            entityManager.detach( viewEntity );
            return prepareObjectViewDTOFromViewEntity( viewEntity );
        }
    }

    @Override
    public List< ObjectViewDTO > getAllObjectViewsByKey( EntityManager entityManager, String key ) {
        List< ObjectViewDTO > viewDTOListToReurn = new ArrayList<>();
        for ( ObjectViewEntity viewEntity : objectViewDAO.getAllObjectViewsByKey( entityManager, key ) ) {
            viewDTOListToReurn.add( prepareObjectViewDTOFromViewEntity( viewEntity ) );
        }
        return viewDTOListToReurn;
    }

    /**
     * Removes old the default view.
     *
     * @param entityManager
     *         the entity manager
     * @param viewEntity
     *         the view entity
     */
    private ObjectViewEntity removeOldDefaultView( EntityManager entityManager, ObjectViewEntity viewEntity ) {
        if ( viewEntity != null ) {
            viewEntity.setDefaultView( false );
            return objectViewDAO.saveOrUpdate( entityManager, viewEntity );
        }
        return null;
    }

    /**
     * Adds the default view.
     *
     * @param entityManager
     *         the entity manager
     * @param viewId
     *         the view id
     *
     * @return the object view DTO
     */
    private ObjectViewDTO addDefaultView( EntityManager entityManager, UUID viewId ) {
        ObjectViewDTO objectViewDTO = null;
        ObjectViewEntity viewEntityFromDb = objectViewDAO.getLatestNonDeletedObjectById( entityManager, viewId );
        if ( viewEntityFromDb != null ) {
            viewEntityFromDb.setDefaultView( true );
            objectViewDTO = prepareObjectViewDTOFromViewEntity( objectViewDAO.saveOrUpdate( entityManager, viewEntityFromDb ) );
        }
        return objectViewDTO;
    }

    /**
     * Prepare object view DTO from view entity.
     *
     * @param viewEntity
     *         the view entity
     *
     * @return the object view DTO
     */
    private ObjectViewDTO prepareObjectViewDTOFromViewEntity( ObjectViewEntity viewEntity ) {
        ObjectViewDTO objectViewDTO = new ObjectViewDTO();
        objectViewDTO.setId( viewEntity.getId().toString() );
        objectViewDTO.setName( viewEntity.getObjectViewName() );
        objectViewDTO.setObjectViewJson( viewEntity.getObjectViewJson() );
        if ( viewEntity.getObjectViewType().equals( ConstantsObjectViewType.TABLE_VIEW_TYPE ) && viewEntity.getObjectViewJson() != null ) {
            objectViewDTO.setSettings( JsonUtils.jsonToObject( viewEntity.getObjectViewJson(), FiltersDTO.class ) );
        }
        objectViewDTO.setObjectViewKey( viewEntity.getObjectViewKey() );
        objectViewDTO.setDefaultView( viewEntity.isDefaultView() );
        objectViewDTO.setObjectViewType( viewEntity.getObjectViewType() );
        if ( viewEntity.getObjectId() != null ) {
            objectViewDTO.setObjectId( viewEntity.getObjectId().toString() );
        }
        objectViewDTO.setConfig( viewEntity.getConfig() );
        objectViewDTO.setSearch( viewEntity.getSearchQuery() );
        objectViewDTO.setSortDirection( viewEntity.getSortDirection() );
        objectViewDTO.setSortParameter( viewEntity.getSortParameter() );
        return objectViewDTO;
    }

    /**
     * Prepare view entity from object view DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param viewDTO
     *         the view DTO
     * @param userId
     *         the user id
     * @param viewEntity
     *         the view entity
     *
     * @return the view entity
     */
    private ObjectViewEntity prepareViewEntityFromObjectViewDTO( EntityManager entityManager, ObjectViewDTO viewDTO, String userId,
            ObjectViewEntity viewEntity ) {
        if ( viewEntity == null ) {
            viewEntity = new ObjectViewEntity();
            if ( StringUtils.isBlank( viewDTO.getId() ) ) {
                viewEntity.setId( UUID.randomUUID() );
            } else {
                viewEntity.setId( UUID.fromString( viewDTO.getId() ) );
            }
        }
        viewEntity.setModifiedOn( new Date() );
        viewEntity.setCreatedOn( new Date() );

        viewEntity.setObjectViewName( viewDTO.getName() );
        viewEntity.setObjectViewJson( viewDTO.getObjectViewJson() );
        viewEntity.setObjectViewKey( viewDTO.getObjectViewKey() );
        viewEntity.setObjectViewType( viewDTO.getObjectViewType() );

        UserEntity userEntity = userCommonDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( userId ) );
        viewEntity.setCreatedBy( userEntity );
        // for data node objects
        viewEntity.setObjectId(
                ValidationUtils.validateUUIDString( viewDTO.getObjectId() ) ? UUID.fromString( viewDTO.getObjectId() ) : null );
        viewEntity.setDefaultView( viewDTO.isDefaultView() );
        if ( Boolean.TRUE.equals( PropertiesManager.isAuditSystemView() ) ) {
            viewEntity.setAuditLogEntity( generateAuditLogEntryForObjectView( entityManager, viewEntity, viewDTO, userEntity ) );
        }
        viewEntity.setSearchQuery( viewDTO.getSearch() );
        viewEntity.setSortDirection( viewDTO.getSortDirection() );
        viewEntity.setSortParameter( viewDTO.getSortParameter() );
        viewEntity.setConfig( viewDTO.getConfig() );
        return viewEntity;
    }

    /**
     * Generate audit log entry for object view.
     *
     * @param entityManager
     *         the entity manager
     * @param viewEntity
     *         the view entity
     * @param viewDTO
     *         the view DTO
     * @param userEntity
     *         the user entity
     *
     * @return the audit log entity
     */
    private AuditLogEntity generateAuditLogEntryForObjectView( EntityManager entityManager, ObjectViewEntity viewEntity,
            ObjectViewDTO viewDTO, UserEntity userEntity ) {
        boolean status = isViewAlreadyExistsForUser( entityManager, viewDTO, userEntity );
        AuditLogEntity auditLogEntity;
        if ( StringUtils.isBlank( viewDTO.getId() ) ) {
            auditLogEntity = AuditLogDTO.prepareAuditLogEntityForObjects(
                    "View : " + viewDTO.getName() + ConstantsString.SPACE + ConstantsDbOperationTypes.CREATED,
                    ConstantsDbOperationTypes.CREATED, userEntity.getId().toString(), "", viewDTO.getName(), "View" );
            auditLogEntity.setAddedBy( userEntity );

            if ( status && !viewDTO.isDefaultView() ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.VIEW_ALREADY_EXIST.getKey(), viewDTO.getName() ) );
            }
        } else {
            ObjectViewEntity oldObjectViewEntity = objectViewDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( viewDTO.getId() ) );
            if ( !viewDTO.getName().equals( oldObjectViewEntity.getObjectViewName() ) && status ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.VIEW_ALREADY_EXIST.getKey(), viewDTO.getName() ) );
            }
            viewEntity.setCreatedOn( oldObjectViewEntity.getCreatedOn() );
            auditLogEntity = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userEntity.getId().toString(), oldObjectViewEntity,
                    viewEntity, viewDTO.getId(), viewDTO.getName(), "View" );
            if ( null != auditLogEntity ) {
                auditLogEntity.setObjectId( oldObjectViewEntity.getId().toString() );
            }
        }
        return auditLogEntity;
    }

    /**
     * Checks if is view already exists for user.
     *
     * @param entityManager
     *         the entity manager
     * @param viewDTO
     *         the view DTO
     * @param userEntity
     *         the user entity
     *
     * @return true, if is view already exists for user
     */
    private boolean isViewAlreadyExistsForUser( EntityManager entityManager, ObjectViewDTO viewDTO, UserEntity userEntity ) {
        return objectViewDAO.isObjectViewAlreadyExists( entityManager, viewDTO.getName(), viewDTO.getObjectViewKey(), userEntity.getId(),
                viewDTO.getObjectId() );
    }

    /**
     * Sets the object view DAO.
     *
     * @param objectViewDAO
     *         the new object view DAO
     */
    public void setObjectViewDAO( ObjectViewDAO objectViewDAO ) {
        this.objectViewDAO = objectViewDAO;
    }

    /**
     * Sets the user common DAO.
     *
     * @param userCommonDAO
     *         the new user common DAO
     */
    public void setUserCommonDAO( UserCommonDAO userCommonDAO ) {
        this.userCommonDAO = userCommonDAO;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
