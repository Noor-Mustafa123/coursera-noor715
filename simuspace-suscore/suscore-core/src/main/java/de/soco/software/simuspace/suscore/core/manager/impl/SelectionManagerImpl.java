package de.soco.software.simuspace.suscore.core.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.phase.PhaseInterceptorChain;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserProfile;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.core.dao.SelectionDAO;
import de.soco.software.simuspace.suscore.core.dao.SelectionItemDAO;
import de.soco.software.simuspace.suscore.core.manager.AdditionalAttributeManager;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.core.model.SelectionItemOrder;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchCommonDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SystemContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.WorkflowProjectEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.SsfeSelectionDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.data.utility.FilterHelper;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.notification.model.TreeChangeDTO;
import de.soco.software.simuspace.suscore.notification.model.TreeStateDTO;
import de.soco.software.simuspace.suscore.notification.model.UIStateDTO;
import de.soco.software.simuspace.suscore.notification.model.UrlDTO;
import de.soco.software.simuspace.suscore.notification.service.rest.WSService;

/**
 * The Class SelectionManagerImpl provide methods to save update and get selection made by users.
 *
 * @author Noman Arshad
 */
@Log4j2
public class SelectionManagerImpl implements SelectionManager {

    /**
     * The common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The selection DAO.
     */
    private SelectionDAO selectionDAO;

    /**
     * The selection item DAO.
     */
    private SelectionItemDAO selectionItemDAO;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The dummy file DAO.
     */
    private BmwCaeBenchCommonDAO bmwCaeBenchCommonDAO;

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * The link manager.
     */
    private LinkManagerImpl linkManager;

    /**
     * The web socket service.
     */
    private WSService webSocketService;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The Constant LINK_TYPE_YES.
     */
    private static final String LINK_TYPE_YES = "Yes";

    /**
     * The Constant SSFE_ORIGIN.
     */
    private static final String SSFE_ORIGIN = "ssfe";

    /**
     * The Constant FULL_PATH.
     */
    private static final String FULL_PATH = "fullPath";

    /**
     * The Constant LINK_TYPE_NO.
     */
    private static final String LINK_TYPE_NO = "No";

    /**
     * The additional attribute manager.
     */
    private AdditionalAttributeManager additionalAttributeManager;

    /**
     * The Entity manager factory reference.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionResponseUI createSelection( String userId, String origin, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SelectionResponseUI selectionResponseUI;
        try {
            selectionResponseUI = createSelection( entityManager, userId, SelectionOrigins.getByOrigin( origin ), filter );
        } finally {
            entityManager.close();
        }
        return selectionResponseUI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionResponseUI createSelection( EntityManager entityManager, String userId, SelectionOrigins selectionOrigin,
            FiltersDTO filter ) {
        SelectionEntity exitingSelection;
        UserEntity userEntity = validateUserForSelection( entityManager, userId );

        exitingSelection = saveSelectionEntity( entityManager, userEntity, selectionOrigin.getOrigin() );

        if ( exitingSelection != null ) {
            prepareAndSaveItemSelections( entityManager, filter, exitingSelection, null );
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_NOT_SAVED.getKey() ) );
        }

        return prepareSelectionResponseFormSelectionEntity( entityManager, exitingSelection.getId().toString(),
                selectionOrigin.getOrigin() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionItemEntity saveSelectionItemsWithAttributes( String userId, String selectionId, String selectionItemId,
            String additionalAttributesJson, String string ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SelectionItemEntity selectionItemEntity;
        try {
            selectionItemEntity = selectionItemDAO.getLatestObjectById( entityManager, SelectionItemEntity.class,
                    UUID.fromString( selectionItemId ) );

            selectionItemEntity.setAdditionalAttributesJson( additionalAttributesJson );
            selectionItemDAO.saveOrUpdate( entityManager, selectionItemEntity );
        } finally {
            entityManager.close();
        }
        return selectionItemEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionResponseUI createSelectionForSingleItem( String userId, String origin, String itemId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return createSelectionForSingleItem( entityManager, userId, origin, itemId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionResponseUI createSelectionForSingleItem( EntityManager entityManager, String userId, String origin, String itemId ) {
        SelectionResponseUI selectionResponseUI;
        UserEntity userEntity = validateUserForSelection( entityManager, userId );

        SelectionEntity exitingSelection = saveSelectionEntity( entityManager, userEntity, origin );

        if ( exitingSelection != null ) {
            Set< SelectionItemEntity > itemEntities = new HashSet<>();
            SelectionItemEntity entity = new SelectionItemEntity();
            entity.setId( UUID.randomUUID() );
            entity.setSelectionEntity( exitingSelection );
            entity.setItem( itemId );
            itemEntities.add( entity );
            exitingSelection.setItems( itemEntities );
        }
        if ( selectionDAO.saveOrUpdate( entityManager, exitingSelection ) == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_SAVED.getKey() ) );
        }

        selectionResponseUI = prepareSelectionResponseFormSelectionEntity( entityManager, exitingSelection.getId().toString(), origin );
        return selectionResponseUI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionResponseUI createSelectionForMultipleItems( String userId, String origin, List< String > itemIds ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return createSelectionForMultipleItems( entityManager, userId, origin, itemIds );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionResponseUI createSelectionForMultipleItems( EntityManager entityManager, String userId, String origin,
            List< String > itemIds ) {
        SelectionResponseUI selectionResponseUI;
        SelectionEntity exitingSelection;
        UserEntity userEntity = validateUserForSelection( entityManager, userId );
        exitingSelection = saveSelectionEntity( entityManager, userEntity, origin );
        if ( CollectionUtils.isNotEmpty( itemIds ) ) {
            Set< SelectionItemEntity > itemEntities = new HashSet<>();
            int order = 0;
            for ( Object item : itemIds ) {
                SelectionItemEntity entity = new SelectionItemEntity();
                entity.setId( UUID.randomUUID() );
                entity.setSelectionEntity( exitingSelection );
                entity.setItem( item.toString() );
                entity.setItemOrder( order );
                itemEntities.add( entity );
                order++;
            }
            exitingSelection.setItems( itemEntities );
        }
        if ( selectionDAO.saveOrUpdate( entityManager, exitingSelection ) == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_SAVED.getKey() ) );
        }
        selectionResponseUI = prepareSelectionResponseFormSelectionEntity( entityManager, exitingSelection.getId().toString(), origin );
        return selectionResponseUI;
    }

    /**
     * Validate user for selection user entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the user entity
     */
    private UserEntity validateUserForSelection( EntityManager entityManager, String userId ) {
        UserEntity userEntity;
        if ( userId.equals( ConstantsUserProfile.SUPER_USER_ID.toString() )
                || userId.equals( ConstantsUserProfile.SUPER_USER_GENERAL_HEADER_ID.toString() ) ) {
            userId = ConstantsID.SUPER_USER_ID;
        }
        userEntity = userCommonManager.getUserCommonDAO().getLatestNonDeletedObjectById( entityManager, UUID.fromString( userId ) );
        if ( userEntity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PROVIDED_USER_NOT_AVAILABLE.getKey() ) );
        }
        return userEntity;
    }

    /**
     * Prepare filter DTO form selection entity.
     *
     * @param entityManager
     *         the entity manager
     * @param exitingSelection
     *         the exiting selection
     *
     * @return the selection response UI
     */
    @Override
    public SelectionResponseUI prepareSelectionResponseFormSelectionEntity( EntityManager entityManager, String exitingSelection,
            String origin ) {
        SelectionResponseUI filterResponse = new SelectionResponseUI();
        List< Object > preparedListSelections = new ArrayList<>();

        List< SelectionItemEntity > oldSelections = selectionItemDAO.getObjectListByProperty( entityManager,
                ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( exitingSelection ) );
        if ( origin.equalsIgnoreCase( SSFE_ORIGIN ) ) {
            for ( SelectionItemEntity selectionItems : oldSelections ) {
                preparedListSelections.add( JsonUtils.jsonToObject( selectionItems.getItem(), SsfeSelectionDTO.class ) );
            }
        } else {
            for ( SelectionItemEntity selectionItems : oldSelections ) {
                preparedListSelections.add( selectionItems.getItem() );
            }
        }

        filterResponse.setId( exitingSelection );
        filterResponse.setItems( preparedListSelections );
        return filterResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeSelection( UUID id, boolean isSelectionEntity ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return removeSelection( entityManager, id, isSelectionEntity );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeSelection( EntityManager entityManager, UUID id, boolean isSelectionEntity ) {
        if ( isSelectionEntity ) {
            SelectionEntity selectionEntity = selectionDAO.findById( entityManager, id );
            if ( selectionEntity != null ) {
                selectionEntity.setUserEntity( null );
                selectionEntity = selectionDAO.saveOrUpdate( entityManager, selectionEntity );
                selectionDAO.delete( entityManager, selectionEntity );
            }
        } else {
            SelectionItemEntity selectionItemEntity = selectionItemDAO.findById( entityManager, id );
            if ( selectionItemEntity != null ) {
                selectionItemDAO.delete( entityManager, selectionItemEntity );
            }
        }

        return true;
    }

    /**
     * Prepare and save item selections.
     *
     * @param entityManager
     *         the entity manager
     * @param filter
     *         the filter
     * @param exitingSelection
     *         the exiting selection
     * @param attributesJson
     *         the attributes json
     */
    private void prepareAndSaveItemSelections( EntityManager entityManager, FiltersDTO filter, SelectionEntity exitingSelection,
            String attributesJson ) {
        if ( CollectionUtils.isNotEmpty( filter.getItems() ) ) {
            Set< SelectionItemEntity > itemEntities = new HashSet<>();
            int order = 0;
            for ( Object item : filter.getItems() ) {
                SelectionItemEntity entity = new SelectionItemEntity();
                entity.setId( UUID.randomUUID() );
                entity.setSelectionEntity( exitingSelection );
                entity.setItem( item.toString() );
                entity.setItemOrder( order );
                if ( attributesJson != null && !attributesJson.isEmpty() ) {
                    entity.setAdditionalAttributesJson( attributesJson );
                }
                itemEntities.add( entity );
                order++;
            }
            exitingSelection.setItems( itemEntities );
        }
        if ( selectionDAO.saveOrUpdate( entityManager, exitingSelection ) == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_SAVED.getKey() ) );
        }
    }

    /**
     * Creates the generic DTO from object entity.
     *
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     *
     * @return the object
     */
    @Override
    public GenericDTO prepareGenericDTOFromObjectEntity( EntityManager entityManager, UUID projectId, SuSEntity susEntity ) {
        GenericDTO objectDTO = null;
        if ( susEntity != null ) {
            GenericDTO genericDTO = new GenericDTO();
            genericDTO.setName( susEntity.getName() );
            genericDTO.setId( susEntity.getComposedId().getId() );
            genericDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            genericDTO.setCreatedOn( susEntity.getCreatedOn() );
            genericDTO.setModifiedOn( susEntity.getModifiedOn() );
            genericDTO.setParentId( projectId );
            genericDTO.setDescription( susEntity.getDescription() );
            genericDTO.setTypeId( susEntity.getTypeId() );
            genericDTO.setSize( susEntity.getSize() != null && susEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                    ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( susEntity.getSize() )
                    : ConstantsString.NOT_AVAILABLE );
            if ( null != susEntity.getCreatedBy() ) {
                genericDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            if ( null != susEntity.getModifiedBy() ) {
                genericDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            }

            if ( susEntity instanceof ContainerEntity ) {
                genericDTO.setUrlType( ConstantsString.PROJECT_KEY );
            } else {
                genericDTO.setUrlType( ConstantsString.OBJECT_KEY );
            }

            if ( susEntity.getTypeId() != null ) {
                genericDTO.setLifeCycleStatus( configManager.getStatusByIdandObjectType( susEntity.getTypeId(),
                        susEntity.getLifeCycleStatus(), susEntity.getConfig() ) );
                genericDTO.setType(
                        configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() ).getName() );
                genericDTO.setIcon( susEntity.getIcon() );
            }

            List< Relation > relation = linkManager.getLinkDAO().getLinkedRelationByChildId( entityManager,
                    susEntity.getComposedId().getId() );

            if ( CollectionUtils.isNotEmpty( relation ) ) {
                genericDTO.setLink( LINK_TYPE_YES );
            } else {
                genericDTO.setLink( LINK_TYPE_NO );
            }

            objectDTO = genericDTO;
        }
        return objectDTO;
    }

    /**
     * Save and get selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     * @param origin
     *         the origin
     *
     * @return the selection entity
     */
    public SelectionEntity saveSelectionEntity( EntityManager entityManager, UserEntity userEntity, String origin ) {
        SelectionEntity selectionEntity = new SelectionEntity();
        selectionEntity.setId( UUID.randomUUID() );
        selectionEntity.setCreatedon( new Date() );
        selectionEntity.setOrigin( origin );
        selectionEntity.setUserEntity( userEntity );
        return selectionDAO.save( entityManager, selectionEntity );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SelectionItemEntity > getUserSelectionsBySelectionIds( EntityManager entityManager, String sId, FiltersDTO filter ) {
        try {
            return selectionItemDAO.getPaginatedSelectionByProperties( entityManager, ConstantsDAO.SELECTION_ENTITY_ID,
                    UUID.fromString( sId ), filter );
        } catch ( IllegalArgumentException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.INVALID_UUID.getKey(), sId ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_UUID.getKey(), sId ) );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ? > getSelectedIdsListBySelectionId( String sId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SelectionEntity selectionEntity = getSelectionEntityById( sId );
            if ( selectionEntity.getOrigin().equalsIgnoreCase( SSFE_ORIGIN ) ) {
                return extractSsfeDTOFromSelectionItems( selectionItemDAO.getObjectListByProperty( entityManager,
                        ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( sId ) ) );
            } else {
                return getSelectedIdsListBySelectionId( entityManager, sId );
            }
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< SsfeSelectionDTO > getSelectedFilesFromSsfsSelection( EntityManager entityManager, String sId ) {
        SelectionEntity selectionEntity = getSelectionEntityById( sId );
        if ( !selectionEntity.getOrigin().equalsIgnoreCase( SSFE_ORIGIN ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_NOT_AVAILIABLE.getKey() ) );
        } else {
            return extractSsfeDTOFromSelectionItems( selectionItemDAO.getObjectListByProperty( entityManager,
                    ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( sId ) ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UUID > getSelectedIdsListBySelectionId( EntityManager entityManager, String sId ) {
        return extractIdsFromSelectionItemEntityList(
                selectionItemDAO.getObjectListByProperty( entityManager, ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( sId ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< String > getSelectedIdsStringListBySelectionId( String sId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< String > selectedIds;
        try {
            selectedIds = getSelectedIdsStringListBySelectionId( entityManager, sId );
        } finally {
            entityManager.close();
        }
        return selectedIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< String > getSelectedIdsStringListBySelectionId( EntityManager entityManager, String sId ) {
        return extractIdsFromSelectionItems(
                selectionItemDAO.getObjectListByProperty( entityManager, ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( sId ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< BmwCaeBenchEntity > getSortedSelection( String sId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getSortedSelection( entityManager, sId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< BmwCaeBenchEntity > getSortedSelection( EntityManager entityManager, String sId ) {
        List< BmwCaeBenchEntity > bmwCaeBenchEntities;
        List< SelectionItemEntity > slectionList = selectionItemDAO.getObjectListByProperty( entityManager,
                ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( sId ) );
        bmwCaeBenchEntities = extractDummyFilesFromSelectionItemEntityListString( entityManager, slectionList );
        return bmwCaeBenchEntities;
    }

    /**
     * Extract dummy files from selection item entity list string.
     *
     * @param entityManager
     *         the entity manager
     * @param objectListByProperty
     *         the object list by property
     *
     * @return the list
     */
    private List< BmwCaeBenchEntity > extractDummyFilesFromSelectionItemEntityListString( EntityManager entityManager,
            List< SelectionItemEntity > objectListByProperty ) {

        List< UUID > selectedIdUUID = new ArrayList<>();

        for ( SelectionItemEntity selectionEntity : objectListByProperty ) {
            selectedIdUUID.add( UUID.fromString( selectionEntity.getItem() ) );
        }
        return bmwCaeBenchCommonDAO.getLatestNonDeletedObjectsByListOfIds( entityManager, selectedIdUUID );
    }

    /**
     * Extract ids from selection item entity list.
     *
     * @param objectListByProperty
     *         the object list by property
     *
     * @return the list
     */
    public List< UUID > extractIdsFromSelectionItemEntityList( List< SelectionItemEntity > objectListByProperty ) {

        List< UUID > userIdList = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( objectListByProperty ) ) {
            for ( SelectionItemEntity selectionsItems : objectListByProperty ) {
                userIdList.add( UUID.fromString( selectionsItems.getItem() ) );
            }
        }
        return userIdList;
    }

    /**
     * Extract ids from selection item entity list string.
     *
     * @param selectionItems
     *         the object list by property
     *
     * @return the list
     */
    public List< String > extractIdsFromSelectionItems( List< SelectionItemEntity > selectionItems ) {

        List< String > userIdList = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectionItems ) ) {
            for ( SelectionItemEntity selectionItem : selectionItems ) {
                userIdList.add( selectionItem.getItem() );
            }
        }
        return userIdList;
    }

    /**
     * Extract ssfe DTO from selection item entity list string.
     *
     * @param selectionItems
     *         the object list by property
     *
     * @return the list
     */
    public List< SsfeSelectionDTO > extractSsfeDTOFromSelectionItems( List< SelectionItemEntity > selectionItems ) {
        List< SsfeSelectionDTO > ssfeDTOList = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectionItems ) ) {
            for ( SelectionItemEntity selectionItem : selectionItems ) {
                ssfeDTOList.add( JsonUtils.jsonToObject( selectionItem.getItem(), SsfeSelectionDTO.class ) );
            }
        }
        return ssfeDTOList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionResponseUI addSelectionItemsInExistingSelection( String selectionId, FiltersDTO selectionItems ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return addSelectionItemsInExistingSelection( entityManager, selectionId, selectionItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionResponseUI addSelectionItemsInExistingSelection( EntityManager entityManager, String selectionId,
            FiltersDTO selectionItems ) {
        SelectionResponseUI selectionResponseUI;
        boolean checkSelectionExist;
        SelectionEntity selectionEntity = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                UUID.fromString( selectionId ) );
        List< SelectionItemEntity > oldSelections = selectionItemDAO.getObjectListByProperty( entityManager,
                ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( selectionId ) );
        List< Object > newSelectionIds = selectionItems.getItems();

        if ( selectionEntity != null && oldSelections != null ) {
            int maxOrder = 0;
            for ( Object object : newSelectionIds ) {
                checkSelectionExist = false;
                for ( SelectionItemEntity selectionItemEntity : oldSelections ) {
                    if ( object.toString().equals( selectionItemEntity.getItem() ) ) {
                        checkSelectionExist = true;
                    }
                    if ( selectionItemEntity.getItemOrder() >= maxOrder ) {
                        maxOrder = selectionItemEntity.getItemOrder() + 1;
                    }
                }

                if ( !checkSelectionExist ) {
                    SelectionItemEntity items = new SelectionItemEntity();
                    items.setId( UUID.randomUUID() );
                    items.setItem(
                            selectionEntity.getOrigin().equalsIgnoreCase( SSFE_ORIGIN ) ? JsonUtils.toJson( object ) : object.toString() );
                    items.setSelectionEntity( selectionEntity );
                    items.setItemOrder( maxOrder );
                    selectionItemDAO.save( entityManager, items );
                    maxOrder += 1;
                }
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_SAVED.getKey() ) );
        }

        selectionResponseUI = prepareSelectionResponseFormSelectionEntity( entityManager, selectionId, selectionEntity.getOrigin() );
        return selectionResponseUI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionResponseUI removeSelectionItemsInExistingSelection( String selectionId, FiltersDTO selectionItems ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return removeSelectionItemsInExistingSelection( entityManager, selectionId, selectionItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionResponseUI removeSelectionItemsInExistingSelection( EntityManager entityManager, String selectionId,
            FiltersDTO selectionItems ) {
        SelectionResponseUI selectionResponseUI;
        SelectionEntity selectionEntity = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                UUID.fromString( selectionId ) );
        Set< SelectionItemEntity > oldSelections = selectionEntity.getItems();
        List< Object > newSelectionIds = selectionItems.getItems();
        if ( oldSelections != null ) {
            for ( Object object : newSelectionIds ) {
                Iterator< SelectionItemEntity > iterator = oldSelections.iterator();
                while ( iterator.hasNext() ) {
                    SelectionItemEntity selectionItemEntity = iterator.next();
                    if ( selectionEntity.getOrigin().equalsIgnoreCase( SSFE_ORIGIN ) ) {
                        Map< String, Object > newMap = new HashMap<>();
                        newMap = ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( object ), newMap );
                        Map< String, Object > oldMap = new HashMap<>();
                        oldMap = ( Map< String, Object > ) JsonUtils.jsonToMap( selectionItemEntity.getItem(), oldMap );
                        if ( newMap.get( FULL_PATH ).toString().equalsIgnoreCase( oldMap.get( FULL_PATH ).toString() ) ) {
                            iterator.remove();
                        }
                    } else if ( object.toString().equals( selectionItemEntity.getItem() ) ) {
                        iterator.remove();
                    }
                }
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_REMOVED.getKey() ) );
        }
        selectionEntity.setItems( oldSelections );
        selectionDAO.update( entityManager, selectionEntity );
        selectionResponseUI = prepareSelectionResponseFormSelectionEntity( entityManager, selectionId, selectionEntity.getOrigin() );
        return selectionResponseUI;
    }

    /**
     * Removes the all selection items.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     */
    @Override
    public void removeAllSelectionItems( EntityManager entityManager, String selectionId ) {
        List< SelectionItemEntity > oldSelections = selectionItemDAO.getObjectListByProperty( entityManager,
                ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( selectionId ) );
        for ( SelectionItemEntity selectionItemEntity : oldSelections ) {
            selectionItemDAO.delete( entityManager, selectionItemEntity );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getGenericDTOUI( String userId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< TableColumn > tableColumnList;
        try {
            SelectionEntity entity = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                    UUID.fromString( selectionId ) );
            if ( entity == null ) {
                throw new SusException( "Selection does not exist." );
            }

            if ( entity.getOrigin().equalsIgnoreCase( SSFE_ORIGIN ) ) {
                tableColumnList = GUIUtils.listColumns( SsfeSelectionDTO.class );
            } else {
                tableColumnList = GUIUtils.listColumns( GenericDTO.class );
            }

            if ( entity.getAdditionalAttributesJson() != null ) {
                // NOTE : adding additional attribute in table columns
                additionalAttributeManager.addAdditionalAttributToTableColumns( entity, tableColumnList );
            }

            List< ObjectViewDTO > views = objectViewManager.getAllObjectViewsByKey( entityManager, selectionId );
            if ( views != null && !views.isEmpty() ) {
                ObjectViewDTO objectViewDTO = views.get( 0 ); // only one view is saved for this table
                FiltersDTO viewFilters = JsonUtils.jsonToObject( objectViewDTO.getObjectViewJson(), FiltersDTO.class );
                List< FilterColumn > viewColumnsList = viewFilters.getColumns();

                for ( TableColumn tableColumn : tableColumnList ) {
                    for ( FilterColumn viewColumn : viewColumnsList ) {
                        if ( tableColumn.getName().equals( viewColumn.getName() ) ) {
                            tableColumn.setVisible( viewColumn.isVisible() );
                            tableColumn.setOrderNum( viewColumn.getReorder() );
                            tableColumn.setReorder( viewColumn.getReorder() );
                        }
                    }
                }

                return tableColumnList.stream().sorted( Comparator.comparing( TableColumn::getOrderNum ) ).toList();
            }
        } finally {
            entityManager.close();
        }

        return tableColumnList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO updateSelectionView( String userId, String selectionId, List< TableColumn > updateColumns ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ObjectViewDTO returnDTO;
        try {
            ObjectViewDTO objectViewDTO = new ObjectViewDTO();

            List< TableColumn > viewColumnsList = updateColumns.stream().sorted( Comparator.comparing( TableColumn::getReorder ) ).toList();

            List< FilterColumn > filterColumns = new ArrayList<>();

            for ( TableColumn viewColumn : viewColumnsList ) {
                FilterColumn filterColumn = new FilterColumn();
                filterColumn.setName( viewColumn.getName() );
                filterColumn.setVisible( viewColumn.isVisible() );
                filterColumn.setReorder( viewColumn.getReorder() );

                filterColumns.add( filterColumn );
            }

            FiltersDTO viewFilters = new FiltersDTO();
            viewFilters.setColumns( filterColumns );

            List< ObjectViewDTO > views = objectViewManager.getAllObjectViewsByKey( entityManager, selectionId );

            if ( views != null && !views.isEmpty() ) {
                objectViewDTO = views.get( 0 ); // only one view is saved for this table
            }

            objectViewDTO.setDefaultView( true );

            objectViewDTO.setName( selectionId );
            objectViewDTO.setObjectViewKey( selectionId );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( viewFilters ) );

            returnDTO = objectViewManager.saveOrUpdateObjectView( entityManager, objectViewDTO, userId );
        } finally {
            entityManager.close();
        }
        return returnDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ? > getGenericDTOList( String userId, String selectionId, FiltersDTO filter ) {
        FilteredResponse< ? > genericDTOFilteredResponse;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            genericDTOFilteredResponse = getGenericDTOList( entityManager, userId, selectionId, filter );
        } finally {
            entityManager.close();
        }
        return genericDTOFilteredResponse;
    }

    @Override
    public FilteredResponse< ? > getGenericDTOList( EntityManager entityManager, String userId, String selectionId, FiltersDTO filter ) {
        FilteredResponse< ? > genericDTOFilteredResponse;
        SelectionEntity selectionEntity = getSelectionEntityById( selectionId );
        if ( selectionEntity.getOrigin().equalsIgnoreCase( SSFE_ORIGIN ) ) {
            List< SsfeSelectionDTO > ssfeSelectionDTOS = new ArrayList<>();
            List< SelectionItemEntity > selectedObjects = getUserSelectionsBySelectionIds( entityManager, selectionId, filter );
            for ( SelectionItemEntity selectionItemEntity : selectedObjects ) {
                var ssfeDTO = JsonUtils.jsonToObject( selectionItemEntity.getItem(), SsfeSelectionDTO.class );
                if ( !StringUtils.isBlank( filter.getSearch() ) ||
                        ( null != filter.getColumns() &&
                                filter.getColumns().stream().anyMatch( filterColumn ->
                                        filterColumn.getFilters() != null && !filterColumn.getFilters().isEmpty() ) ) ||
                        ( null != filter.getColumns() &&
                                filter.getColumns().stream().anyMatch( filterColumn ->
                                        null != filterColumn.getDir() && !StringUtils.isBlank( filterColumn.getDir() ) ) ) ) {
                    ssfeSelectionDTOS.add( ssfeDTO );
                    ssfeSelectionDTOS = FilterHelper.getFilteredTableEntries( filter, ssfeSelectionDTOS );
                } else {
                    ssfeSelectionDTOS.add( ssfeDTO );
                }
            }

            filter.setFilteredRecords( ( long ) ssfeSelectionDTOS.size() );
            genericDTOFilteredResponse = PaginationUtil.constructFilteredResponse( filter, ssfeSelectionDTOS );
        } else {
            List< GenericDTO > genericDTO = new ArrayList<>();
            List< SelectionItemEntity > selectedObjects = getUserSelectionsBySelectionIds( entityManager, selectionId, filter );
            List< UUID > selectedIdUUID = new ArrayList<>();
            for ( SelectionItemEntity selectionItemEntity : selectedObjects ) {
                selectedIdUUID.add( UUID.fromString( selectionItemEntity.getItem() ) );
            }
            List< SuSEntity > listEntry = susDAO.getLatestNonDeletedObjectsByListOfIdsAndFilter( entityManager, selectedIdUUID, filter );
            for ( SuSEntity suSEntity : listEntry ) {
                if ( suSEntity != null ) {
                    translateName( TokenizedLicenseUtil
                            .getUser( BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) ), suSEntity );
                    genericDTO.add( prepareGenericDTOFromObjectEntity( entityManager, null, suSEntity ) );
                }
            }
            genericDTOFilteredResponse = PaginationUtil.constructFilteredResponse( filter, genericDTO );
        }

        return genericDTOFilteredResponse;
    }

    /**
     * Translate name.
     *
     * @param user
     *         the user
     * @param entity
     *         the entity
     * @param translationEntities
     *         the translation entities
     */
    private void translateName( UserDTO user, SuSEntity entity ) {
        if ( PropertiesManager.hasTranslation() && null != user ) {
            if ( user.getId().equals( ConstantsID.SUPER_USER_ID ) ) {
                return;
            } else if ( null != user.getUserDetails() ) {
                String userLang = user.getUserDetails().iterator().next().getLanguage();
                entity.getTranslation().forEach( translation -> {
                    if ( userLang.equals( translation.getLanguage() ) && null != translation.getName()
                            && !translation.getName().isEmpty() ) {
                        entity.setName( translation.getName() );
                    }
                } );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< GenericDTO > getGenericDTOList( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< GenericDTO > genericDTOList;
        try {
            List< UUID > itemsList = new ArrayList<>();
            for ( Object item : filter.getItems() ) {
                itemsList.add( UUID.fromString( item.toString() ) );
            }
            genericDTOList = new ArrayList<>();
            List< SuSEntity > listOfEntities = susDAO.getLatestNonDeletedObjectsByListOfIds( entityManager, itemsList );
            for ( SuSEntity suSEntity : listOfEntities ) {
                if ( suSEntity != null ) {
                    genericDTOList.add( prepareGenericDTOFromObjectEntity( entityManager, null, suSEntity ) );
                }
            }
        } finally {
            entityManager.close();
        }
        return genericDTOList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCustomerEvent( String userId, SuSEntity updateEntity, String action ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            sendCustomerEvent( entityManager, userId, updateEntity, action );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCustomerEvent( EntityManager entityManager, String userId, SuSEntity updateEntity, String action ) {
        final UserDTO changeByUser = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
        UrlDTO updateItem;
        boolean isTreeUpdated = false;
        if ( updateEntity instanceof ContainerEntity ) {
            updateItem = new UrlDTO( "view/data/project/" + updateEntity.getComposedId().getId(), action, changeByUser );
            if ( updateEntity instanceof WorkflowProjectEntity ) {
                updateItem = new UrlDTO( "view/workflow/project/" + updateEntity.getComposedId().getId(), action, changeByUser );
            }
            isTreeUpdated = true;
        } else if ( updateEntity instanceof WorkflowEntity ) {
            updateItem = new UrlDTO( "view/workflow/" + updateEntity.getComposedId().getId(), action, changeByUser );
        } else {
            updateItem = new UrlDTO( "view/data/object/" + updateEntity.getComposedId().getId(), action, changeByUser );
        }

        UrlDTO parentItem;
        List< SuSEntity > parentEntities = susDAO.getParents( entityManager, updateEntity );
        if ( parentEntities != null && !parentEntities.isEmpty() ) {
            final SuSEntity parentEntity = parentEntities.get( 0 );
            if ( parentEntity instanceof ContainerEntity ) {
                parentItem = new UrlDTO( "view/data/project/" + parentEntity.getComposedId().getId(), "update", changeByUser );
                if ( parentEntity instanceof WorkflowProjectEntity ) {
                    parentItem = new UrlDTO( "view/workflow/project/" + parentEntity.getComposedId().getId(), "update", changeByUser );
                }
                if ( parentEntity instanceof SystemContainerEntity && updateEntity instanceof WorkflowProjectEntity ) {
                    parentItem = new UrlDTO( "view/workflow/project/" + parentEntity.getComposedId().getId(), "update", changeByUser );
                }
            } else {
                parentItem = new UrlDTO( "view/data/project/" + parentEntity.getComposedId().getId(), "update", changeByUser );
            }
            final UIStateDTO send = new UIStateDTO();
            if ( isTreeUpdated ) {
                send.setTree( new TreeStateDTO( changeByUser,
                        new TreeChangeDTO( action, updateEntity.getComposedId().getId(), parentEntity.getComposedId().getId() ) ) );
            }
            send.setUrls( Arrays.asList( updateItem, parentItem ) );

            final String json = JsonUtils.objectToJson( ResponseUtils.successResponse( send ) );
            webSocketService.postUpdate( json );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCustomerEventForLanguage( EntityManager entityManager, String userId, UserEntity updateEntity ) {
        final UserDTO changeByUser = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
        final UIStateDTO send = new UIStateDTO();
        send.setTree( new TreeStateDTO( changeByUser, new TreeChangeDTO( "language", updateEntity.getId(), UUID.fromString( userId ) ) ) );
        final String json = JsonUtils.objectToJson( ResponseUtils.successResponse( send ) );
        webSocketService.postUpdate( json );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEvent( EntityManager entityManager, String userId, String id, UUID parentId, String action ) {
        final UserDTO changeByUser = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
        UrlDTO updateItem = new UrlDTO( "run/scheme/" + parentId, action, changeByUser );
        final UIStateDTO send = new UIStateDTO();
        send.setUrls( List.of( updateItem ) );
        final String json = JsonUtils.objectToJson( ResponseUtils.successResponse( send ) );
        webSocketService.postUpdate( json );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCustomerEventOnCreate( String userId, SuSEntity createdEntity, SuSEntity parentEntity ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            sendCustomerEventOnCreate( entityManager, userId, createdEntity, parentEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCustomerEventOnCreate( EntityManager entityManager, String userId, SuSEntity createdEntity, SuSEntity parentEntity ) {
        final UserDTO changeByUser = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );

        boolean isTreeUpdated = createdEntity instanceof ContainerEntity;
        UrlDTO parentItem;
        if ( parentEntity instanceof ContainerEntity ) {
            parentItem = new UrlDTO( "view/data/project/" + parentEntity.getComposedId().getId(), "update", changeByUser );
            if ( parentEntity instanceof WorkflowProjectEntity ) {
                parentItem = new UrlDTO( "view/workflow/project/" + parentEntity.getComposedId().getId(), "update", changeByUser );
            }
            if ( parentEntity instanceof SystemContainerEntity && createdEntity instanceof WorkflowProjectEntity ) {
                parentItem = new UrlDTO( "view/workflow/project/" + parentEntity.getComposedId().getId(), "update", changeByUser );
            }
        } else {
            parentItem = new UrlDTO( "view/data/project/" + parentEntity.getComposedId().getId(), "update", changeByUser );
        }
        final UIStateDTO send = new UIStateDTO();
        if ( isTreeUpdated ) {
            send.setTree( new TreeStateDTO( changeByUser,
                    new TreeChangeDTO( "create", createdEntity.getComposedId().getId(), parentEntity.getComposedId().getId() ) ) );
        }
        send.setUrls( List.of( parentItem ) );

        final String json = JsonUtils.objectToJson( ResponseUtils.successResponse( send ) );
        webSocketService.postUpdate( json );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean reOrderSelection( String selectionId, List< SelectionItemOrder > orderFilter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean isReordered;
        try {
            isReordered = reOrderSelection( entityManager, selectionId, orderFilter );
        } finally {
            entityManager.close();
        }
        return isReordered;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean reOrderSelection( EntityManager entityManager, String selectionId, List< SelectionItemOrder > orderFilter ) {

        List< SelectionItemEntity > savedSelections = selectionItemDAO.getObjectListByProperty( entityManager,
                ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( selectionId ) );

        if ( CollectionUtil.isNotEmpty( savedSelections ) ) {

            for ( SelectionItemOrder order : orderFilter ) {

                SelectionItemEntity itemEntity = savedSelections
                        .get( savedSelections.indexOf( new SelectionItemEntity( order.getId().toString() ) ) );
                itemEntity.setItemOrder( order.getNewPosition() );
                selectionItemDAO.update( entityManager, itemEntity );
            }

        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_REMOVED.getKey() ) );
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveSelection( String selectionId, String filterJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean isSaved;
        try {
            isSaved = saveSelection( entityManager, selectionId, filterJson );
        } finally {
            entityManager.close();
        }
        return isSaved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveSelection( EntityManager entityManager, String selectionId, String filterJson ) {

        List< SelectionItemEntity > selectedUserIds = selectionItemDAO.getSelectionItemsByProperty( entityManager,
                ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( selectionId ) );
        List< SelectionItemEntity > userOrderList = new ArrayList<>();
        int itemOrder = 0;
        SelectionEntity selectionEntity = getSelectionEntityById( selectionId );
        if ( selectionEntity.getOrigin().equalsIgnoreCase( SSFE_ORIGIN ) ) {
            List< SsfeSelectionDTO > selectionItems = JsonUtils.jsonToList( filterJson, SsfeSelectionDTO.class );
            for ( SsfeSelectionDTO selectionItem : selectionItems ) {
                for ( SelectionItemEntity selectionItemEntity : selectedUserIds ) {
                    if ( JsonUtils.jsonToObject( selectionItemEntity.getItem(), SsfeSelectionDTO.class ).getFullPath()
                            .equals( selectionItem.getFullPath() ) ) {
                        selectionItemEntity.setItemOrder( itemOrder );
                        userOrderList.add( selectionItemEntity );
                        itemOrder += 1;
                        break;
                    }
                }
            }
        } else {
            List< String > selectionItems = JsonUtils.jsonToList( filterJson, String.class );
            for ( String selectionItem : selectionItems ) {
                for ( SelectionItemEntity selectionItemEntity : selectedUserIds ) {
                    if ( selectionItemEntity.getItem().equals( selectionItem ) ) {
                        selectionItemEntity.setItemOrder( itemOrder );
                        userOrderList.add( selectionItemEntity );
                        itemOrder += 1;
                        break;
                    }
                }
            }
        }
        removeAllSelectionItems( entityManager, selectionId );
        for ( SelectionItemEntity selectionItemEntity : userOrderList ) {
            selectionItemDAO.save( entityManager, selectionItemEntity );
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ? > getAllSelectionWFObjectSort( String sId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< ? > genericDTOS;
        try {
            List< SelectionItemEntity > selectionItemEntities = selectionItemDAO.getObjectListByProperty( entityManager,
                    ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( sId ) );

            SelectionEntity selectionEntity = getSelectionEntityById( sId );
            if ( selectionEntity.getOrigin().equalsIgnoreCase( SSFE_ORIGIN ) ) {
                genericDTOS = extractSsfeFromSelectionItemEntity( entityManager, selectionItemEntities );
            } else {
                genericDTOS = extractFromSelectionItemEntity( entityManager, selectionItemEntities );
            }

        } finally {
            entityManager.close();
        }
        return genericDTOS;
    }

    /**
     * Extract from selection item entity.
     *
     * @param entityManager
     *         the entity manager
     * @param objectListByProperty
     *         the object list by property
     *
     * @return the list
     */
    private List< GenericDTO > extractFromSelectionItemEntity( EntityManager entityManager,
            List< SelectionItemEntity > objectListByProperty ) {
        List< GenericDTO > genericDTO = new ArrayList<>();
        List< UUID > selectedIdUUID = new ArrayList<>();
        for ( SelectionItemEntity selectionEntity : objectListByProperty ) {
            selectedIdUUID.add( UUID.fromString( selectionEntity.getItem() ) );
        }
        List< SuSEntity > listEntiry = susDAO.getLatestNonDeletedObjectsByListOfIds( entityManager, selectedIdUUID );
        for ( SuSEntity suSEntity : listEntiry ) {
            if ( suSEntity != null ) {
                genericDTO.add( prepareGenericDTOFromObjectEntity( entityManager, null, suSEntity ) );
            }
        }
        return genericDTO;
    }

    /**
     * Extract ssfe from selection item entity.
     *
     * @param entityManager
     *         the entity manager
     * @param objectListByProperty
     *         the object list by property
     *
     * @return the list
     */
    private List< ? > extractSsfeFromSelectionItemEntity( EntityManager entityManager, List< SelectionItemEntity > objectListByProperty ) {
        List< SsfeSelectionDTO > ssfeDTO = new ArrayList<>();
        for ( SelectionItemEntity selectionItemEntity : objectListByProperty ) {
            ssfeDTO.add( JsonUtils.jsonToObject( selectionItemEntity.getItem(), SsfeSelectionDTO.class ) );
        }
        return ssfeDTO;
    }

    @Override
    public SelectionResponseUI duplicateSelectionAndSelectionItemsIfAttributeExists( EntityManager entityManager, String selectionId,
            String userId, String origin ) {

        SelectionEntity selection = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                UUID.fromString( selectionId ) );
        if ( selection.getAdditionalAttributesJson() == null || selection.getAdditionalAttributesJson().isEmpty() ) {
            log.debug( "selection can not be duplicated because no additional attribute exists" );
            return null;
        }

        if ( userId.equals( ConstantsUserProfile.SUPER_USER_ID.toString() )
                || userId.equals( ConstantsUserProfile.SUPER_USER_GENERAL_HEADER_ID.toString() ) ) {
            userId = ConstantsID.SUPER_USER_ID;
        }
        UserEntity userEntity = userCommonManager.getUserCommonDAO().getLatestNonDeletedObjectById( entityManager,
                UUID.fromString( userId ) );
        if ( userEntity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PROVIDED_USER_NOT_AVAILABLE.getKey() ) );
        }

        selection.setId( UUID.randomUUID() );
        selection.setOrigin( origin );
        selection.setUserEntity( userEntity );

        if ( selection.getItems() != null && CollectionUtils.isNotEmpty( selection.getItems() ) ) {
            Set< SelectionItemEntity > itemEntities = new HashSet<>();
            for ( SelectionItemEntity item : selection.getItems() ) {
                item.setId( UUID.randomUUID() );
                item.setSelectionEntity( selection );
                itemEntities.add( item );
            }
            selection.setItems( itemEntities );
        }

        SelectionEntity savedSelection = selectionDAO.saveOrUpdate( entityManager, selection );
        if ( savedSelection == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_SAVED.getKey() ) );
        }

        SelectionResponseUI filterResponse = new SelectionResponseUI();
        filterResponse.setId( savedSelection.getId().toString() );
        return filterResponse;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SelectionEntity getSelectionEntityById( String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SelectionEntity latestObjectById;
        try {
            latestObjectById = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( selectionId ) );
        } finally {
            entityManager.close();
        }
        return latestObjectById;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllSelectionsWithOrigin( EntityManager entityManager, String origin ) {
        selectionDAO.deleteAllSelectionsWithOrigin( entityManager, origin );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SelectionEntity getSelectionEntityById( EntityManager entityManager, String selectionId ) {
        return selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( selectionId ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SelectionEntity duplicateSelectionAndSelectionItems( EntityManager entityManager, SelectionEntity sourceEntity,
            boolean updateIdInJson ) {
        SelectionEntity duplicate = sourceEntity.copy();
        duplicate.setItems( null );
        duplicate = selectionDAO.save( entityManager, duplicate );
        duplicate.setItems( duplicateSelectionItems( entityManager, sourceEntity.getItems(), duplicate ) );
        if ( updateIdInJson && sourceEntity.getJson() != null ) {
            Map< String, Object > tempMap = ( Map< String, Object > ) JsonUtils
                    .jsonToMap( ByteUtil.convertByteToString( sourceEntity.getJson() ), new HashMap<>() );
            tempMap.put( "id", duplicate.getId().toString() );
            duplicate.setJson( ByteUtil.convertStringToByte( JsonUtils.toJson( tempMap ) ) );
        }
        return selectionDAO.update( entityManager, duplicate );
    }

    /**
     * Duplicate selection items.
     *
     * @param entityManager
     *         the entity manager
     * @param sourceItems
     *         the source items
     * @param duplicateSelection
     *         the duplicate selection
     *
     * @return the sets the
     */
    private Set< SelectionItemEntity > duplicateSelectionItems( EntityManager entityManager, Set< SelectionItemEntity > sourceItems,
            SelectionEntity duplicateSelection ) {
        if ( CollectionUtils.isEmpty( sourceItems ) ) {
            return new HashSet<>();
        }
        Set< SelectionItemEntity > duplicates = new HashSet<>();
        for ( var sourceItem : sourceItems ) {
            duplicates.add( duplicateSelectionItem( entityManager, sourceItem, duplicateSelection ) );
        }
        return duplicates;
    }

    /**
     * Duplicate selection item.
     *
     * @param entityManager
     *         the entity manager
     * @param sourceItem
     *         the source item
     * @param duplicateSelection
     *         the duplicate selection
     *
     * @return the selection item entity
     */
    private SelectionItemEntity duplicateSelectionItem( EntityManager entityManager, SelectionItemEntity sourceItem,
            SelectionEntity duplicateSelection ) {
        var duplicate = sourceItem.copy();
        duplicate.setSelectionEntity( duplicateSelection );
        return selectionItemDAO.saveOrUpdate( entityManager, duplicate );
    }

    /**
     * Sets the selection DAO.
     *
     * @param selectionDAO
     *         the new selection DAO
     */
    public void setSelectionDAO( SelectionDAO selectionDAO ) {
        this.selectionDAO = selectionDAO;
    }

    /**
     * Sets the selection item DAO.
     *
     * @param selectionItemDAO
     *         the new selection item DAO
     */
    public void setSelectionItemDAO( SelectionItemDAO selectionItemDAO ) {
        this.selectionItemDAO = selectionItemDAO;
    }

    /**
     * Gets the selection item DAO.
     *
     * @return the selection item DAO
     */
    @Override
    public SelectionItemDAO getSelectionItemDAO() {
        return selectionItemDAO;
    }

    /**
     * Gets the user common manager.
     *
     * @return the user common manager
     */
    public UserCommonManager getUserCommonManager() {
        return userCommonManager;
    }

    /**
     * Sets the user common manager.
     *
     * @param userCommonManager
     *         the new user common manager
     */
    public void setUserCommonManager( UserCommonManager userCommonManager ) {
        this.userCommonManager = userCommonManager;
    }

    /**
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
    }

    /**
     * Sets the sus DAO.
     *
     * @param susDAO
     *         the new sus DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

    /**
     * Gets the life cycle manager.
     *
     * @return the life cycle manager
     */
    public LifeCycleManager getLifeCycleManager() {
        return lifeCycleManager;
    }

    /**
     * Sets the life cycle manager.
     *
     * @param lifeCycleManager
     *         the new life cycle manager
     */
    public void setLifeCycleManager( LifeCycleManager lifeCycleManager ) {
        this.lifeCycleManager = lifeCycleManager;
    }

    /**
     * Gets the config manager.
     *
     * @return the config manager
     */
    @Override
    public ObjectTypeConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Sets the config manager.
     *
     * @param configManager
     *         the new config manager
     */
    public void setConfigManager( ObjectTypeConfigManager configManager ) {
        this.configManager = configManager;
    }

    /**
     * Gets the link manager.
     *
     * @return the link manager
     */
    public LinkManagerImpl getLinkManager() {
        return linkManager;
    }

    /**
     * Sets the link manager.
     *
     * @param linkManager
     *         the new link manager
     */
    public void setLinkManager( LinkManagerImpl linkManager ) {
        this.linkManager = linkManager;
    }

    /**
     * Gets the web socket service.
     *
     * @return the web socket service
     */
    public WSService getWebSocketService() {
        return webSocketService;
    }

    /**
     * Sets the web socket service.
     *
     * @param webSocketService
     *         the new web socket service
     */
    public void setWebSocketService( WSService webSocketService ) {
        this.webSocketService = webSocketService;
    }

    /**
     * Gets the bmw cae bench common DAO.
     *
     * @return the bmw cae bench common DAO
     */
    public BmwCaeBenchCommonDAO getBmwCaeBenchCommonDAO() {
        return bmwCaeBenchCommonDAO;
    }

    /**
     * Sets the bmw cae bench common DAO.
     *
     * @param bmwCaeBenchCommonDAO
     *         the new bmw cae bench common DAO
     */
    public void setBmwCaeBenchCommonDAO( BmwCaeBenchCommonDAO bmwCaeBenchCommonDAO ) {
        this.bmwCaeBenchCommonDAO = bmwCaeBenchCommonDAO;
    }

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * Gets the selection DAO.
     *
     * @return the selection DAO
     */
    @Override
    public SelectionDAO getSelectionDAO() {
        return selectionDAO;
    }

    /**
     * Gets the additional attribute manager.
     *
     * @return the additional attribute manager
     */
    @Override
    public AdditionalAttributeManager getAdditionalAttributeManager() {
        return additionalAttributeManager;
    }

    /**
     * Sets the additional attribute manager.
     *
     * @param additionalAttributeManager
     *         the new additional attribute manager
     */
    public void setAdditionalAttributeManager( AdditionalAttributeManager additionalAttributeManager ) {
        this.additionalAttributeManager = additionalAttributeManager;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}