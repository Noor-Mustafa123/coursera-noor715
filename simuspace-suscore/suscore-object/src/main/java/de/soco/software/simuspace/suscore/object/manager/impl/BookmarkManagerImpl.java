package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.suscore.common.base.BaseManager;
import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsOperators;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.data.entity.BookmarkEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.object.dao.BookmarkDAO;
import de.soco.software.simuspace.suscore.object.manager.BookmarkManager;
import de.soco.software.simuspace.suscore.object.manager.DataObjectManager;
import de.soco.software.simuspace.suscore.object.model.BookmarkDTO;

/**
 * The type Bookmark manager.
 */
@Setter
@Log4j
public class BookmarkManagerImpl extends BaseManager implements BookmarkManager {

    /**
     * The Constant PLUGIN_NAME.
     */
    private static final String PLUGIN_NAME = "plugin_workflow";

    /**
     * The Constant CREATE_BY_FIELD.
     */
    private static final String CREATE_BY_FIELD = "createdBy.userUid";

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * workflowDao reference for workflow database operation.
     */
    private BookmarkDAO bookmarkDAO;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    private DataObjectManager dataObjectManager;

    private WorkflowManager workflowManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public BookmarkDTO createBookmark( UUID userId, BookmarkDTO bookmarkModel ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            BookmarkEntity bookmarkEntity = new BookmarkEntity();
            bookmarkEntity.setId( UUID.randomUUID() );
            bookmarkEntity.setName( bookmarkModel.getName() );
            bookmarkEntity.setUrl( bookmarkModel.getUrl() );
            bookmarkEntity.setType( bookmarkModel.getType() );
            bookmarkEntity.setCreatedOn( new Date() );
            bookmarkEntity.setModifiedOn( new Date() );
            bookmarkEntity.setCreatedBy( userCommonManager.getUserEntityById( entityManager, userId ) );

            bookmarkDAO.save( entityManager, bookmarkEntity );
            return bookmarkModel;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BookmarkDTO updateBookmark( UUID userId, BookmarkDTO bookmarkModel ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            BookmarkEntity bookmarkEntity = bookmarkDAO.getLatestNonDeletedObjectById( entityManager, bookmarkModel.getId() );
            bookmarkEntity.setName( bookmarkModel.getName() );
            bookmarkEntity.setUrl( bookmarkModel.getUrl() );
            bookmarkEntity.setModifiedOn( new Date() );
            bookmarkEntity.setModifiedBy( userCommonManager.getUserEntityById( entityManager, userId ) );
            bookmarkDAO.update( entityManager, bookmarkEntity );
            return bookmarkModel;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getListOfBookmarkUITableColumns( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final List< TableColumn > columns = GUIUtils.listColumns( BookmarkDTO.class );
            for ( TableColumn tableColumn : columns ) {
                if ( tableColumn.getName().equalsIgnoreCase( "name" ) ) {
                    tableColumn.getRenderer().setUrl( "{url}" );
                    tableColumn.getRenderer().setType( "link" );
                    tableColumn.getRenderer().setData( "url" );
                }
            }
            return new TableUI( columns,
                    objectViewManager.getUserObjectViewsByKey( entityManager, ConstantsObjectViewKey.BOOKMARKS_TABLE_KEY, userId, null ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createForm() {
        return GUIUtils.createFormFromItems( GUIUtils.prepareForm( true, new BookmarkDTO() ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editForm( UUID bookmarkId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            BookmarkEntity bookmarkEntity = bookmarkDAO.getLatestNonDeletedObjectById( entityManager, bookmarkId );

            return GUIUtils.createFormFromItems( GUIUtils.prepareForm( false,
                    new BookmarkDTO( bookmarkEntity.getId(), bookmarkEntity.getName(), bookmarkEntity.getUrl() ) ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< BookmarkDTO > getAllBookmarks( String userName, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            addCurrentUserFilter( userName, filter );
            List< BookmarkDTO > bookmarkDTOList = new ArrayList<>();
            List< BookmarkEntity > bookmarkEntitiesList = bookmarkDAO.getAllFilteredRecords( entityManager, BookmarkEntity.class, filter );
            if ( CollectionUtil.isNotEmpty( bookmarkEntitiesList ) ) {
                for ( BookmarkEntity bookmark : bookmarkEntitiesList ) {
                    bookmarkDTOList.add( prepareBookmarkDTOFromEntity( bookmark ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, bookmarkDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Add current user filter to filters, so that only entries created by current users are fetched.
     */
    private void addCurrentUserFilter( String userName, FiltersDTO filter ) {
        FilterColumn userFilterColumn = new FilterColumn();
        userFilterColumn.setName( CREATE_BY_FIELD );
        List< Filter > filters = new ArrayList<>();
        Filter userfilter = new Filter();
        userfilter.setOperator( ConstantsOperators.EQUALS.getName() );
        userfilter.setValue( userName );
        userfilter.setCondition( ConstantsOperators.AND.getName() );
        filters.add( userfilter );
        userFilterColumn.setFilters( filters );
        filter.getColumns().add( userFilterColumn );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextRouter( String username, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter.getItems().isEmpty() && filter.getLength().toString().equalsIgnoreCase( "-1" ) ) {
                addSelectAllItemsToFilter( entityManager, username, filter ); // case of select all from data table
            }
            return contextMenuManager.getContextMenu( entityManager, PLUGIN_NAME, BookmarkDTO.class, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Add select all items to filter.
     */
    private void addSelectAllItemsToFilter( EntityManager entityManager, String userName, FiltersDTO filter ) {
        addCurrentUserFilter( userName, filter );
        Long maxResults = bookmarkDAO.getAllFilteredRecordsCount( entityManager, BookmarkEntity.class, filter );
        filter.setLength( Integer.valueOf( maxResults.toString() ) );
        List< BookmarkEntity > bookmarkEntitiesList = bookmarkDAO.getAllFilteredRecords( entityManager, BookmarkEntity.class, filter );
        List< Object > itemsList = new ArrayList<>();
        bookmarkEntitiesList.forEach( entity -> itemsList.add( entity.getId() ) );
        filter.setItems( itemsList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getTreeContextRouter() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteBookmarkBySelection( String selectionId, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( mode.contentEquals( ConstantsMode.BULK ) ) {
                FiltersDTO filtersDTO = contextMenuManager.getFilterBySelectionId( entityManager, selectionId );
                List< Object > ids = filtersDTO.getItems();
                for ( Object id : ids ) {
                    deleteBookmark( entityManager, UUID.fromString( id.toString() ) );
                }
            } else if ( mode.contentEquals( ConstantsMode.SINGLE ) ) {
                return deleteBookmark( entityManager, UUID.fromString( selectionId ) );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), mode ) );
            }
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Delete bookmark boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param bookmarkId
     *         the bookmark id
     *
     * @return the boolean
     */
    private boolean deleteBookmark( EntityManager entityManager, UUID bookmarkId ) {
        BookmarkEntity bookmarkEntity = bookmarkDAO.getLatestNonDeletedObjectById( entityManager, bookmarkId );
        bookmarkDAO.delete( entityManager, bookmarkEntity );
        return true;
    }

    private BookmarkDTO prepareBookmarkDTOFromEntity( BookmarkEntity bookmark ) {
        BookmarkDTO bookmarkDTO = new BookmarkDTO( bookmark.getId(), bookmark.getName(), bookmark.getUrl() );
        bookmarkDTO.setType( bookmark.getType() );
        return bookmarkDTO;
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
    public ObjectViewDTO saveOrUpdateObjectView( ObjectViewDTO viewDTO, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.saveOrUpdateObjectView( entityManager, viewDTO, userId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
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
    public ObjectViewDTO getObjectViewById( UUID viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.getObjectViewById( entityManager, viewId );
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
    public void setTypeForBookmarkElement( String userId, BookmarkDTO bookmarkModel ) {
        if ( bookmarkModel.getObjectId().equals( bookmarkModel.getRootParentId() ) || bookmarkModel.getRootParentId().toString()
                .equals( SimuspaceFeaturesEnum.SYSTEM.getId() ) || bookmarkModel.getRootParentId().toString()
                .equals( SimuspaceFeaturesEnum.CONFIGURATION.getId() ) ) {
            bookmarkModel.setType( SimuspaceFeaturesEnum.getKeyById( bookmarkModel.getObjectId().toString() ) );
        } else if ( bookmarkModel.getRootParentId().toString().equals( SimuspaceFeaturesEnum.JOBS.getId() ) ) {
            bookmarkModel.setType( SimuspaceFeaturesEnum.JOBS.getKey() );
        } else if ( bookmarkModel.getRootParentId().toString().equals( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() )
                || bookmarkModel.getRootParentId().toString().equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
            bookmarkModel.setType( dataObjectManager.getTypeOfSusEntityById( bookmarkModel.getObjectId() ) );
        } else {
            log.info( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_NOT_IDENTIFIED.getKey() ) );
        }
    }

}
