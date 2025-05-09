package de.soco.software.simuspace.susdash.project.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.DashboardConfigEnums;
import de.soco.software.simuspace.suscore.common.enums.DashboardPluginEnums;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.DashboardPluginUtil;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.DataObjectDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.object.manager.DataManager;
import de.soco.software.simuspace.suscore.object.model.ChangeStatusDTO;
import de.soco.software.simuspace.susdash.core.model.ProjectDashboardDTO;
import de.soco.software.simuspace.susdash.project.constants.ProjectDashboardConstants;
import de.soco.software.simuspace.susdash.project.helper.FilterHelper;
import de.soco.software.simuspace.susdash.project.manager.ProjectDashboardManager;
import de.soco.software.simuspace.susdash.project.model.LifecycleTableDTO;
import de.soco.software.simuspace.susdash.project.model.ProjectClassificationCache;
import de.soco.software.simuspace.susdash.project.model.ProjectStructureTableCell;
import de.soco.software.simuspace.susdash.project.model.ProjectStructureTupleWrapper;
import de.soco.software.simuspace.susdash.project.threads.UpdateClassificationDashboardsThread;
import de.soco.software.simuspace.susdash.project.util.ProjectDashboardUtil;

/**
 * The Class ProjectDashboardManagerImpl.
 */
@Log4j2
public class ProjectDashboardManagerImpl implements ProjectDashboardManager {

    /**
     * The Sus dao.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The Selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The Life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    /**
     * The Data manager.
     */
    private DataManager dataManager;

    /**
     * The Object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The Update all dashboards future.
     */
    private ScheduledFuture< ? > updateAllDashboardsFuture;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Init.
     */
    public void init() {
        // called by container
        refreshCacheDirectory();
        updateAllDashboardsFuture = registerAndExecuteScheduledTask();
    }

    /**
     * Refresh cache directory.
     */
    private void refreshCacheDirectory() {
        // fail-safe approach
        Path basePath = Path.of( ProjectDashboardConstants.DASHBOARD_CACHE_BASE_PATH );
        FileUtils.deleteFiles( basePath );
        try {
            Files.createDirectory( basePath );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param objectId
     *         the object id
     * @param projectDashboardDTO
     *         the project dashboard dto
     * @param token
     *         the token
     *
     * @return the project structure chart
     */
    @Override
    public Map< String, Object > getProjectStructureChart( String objectId, ProjectDashboardDTO projectDashboardDTO, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ProjectClassificationCache cache = ProjectDashboardUtil.validateAndRetrieveCache( objectId,
                    TokenizedLicenseUtil.getUserLanguage( token ) );
            List< ProjectStructureTupleWrapper > table = cache.getChartPayload();
            if ( projectDashboardDTO.getMaxDepth() != null && projectDashboardDTO.getMaxDepth() < ConstantsInteger.INTEGER_VALUE_TWO ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_DASHBOARD_MINIMUM_DEPTH_EXCEPTION.getKey() ) );
            }

            DataObjectDashboardEntity dashboardEntity = ( DataObjectDashboardEntity ) susDAO.getLatestObjectById( entityManager,
                    DataObjectDashboardEntity.class, UUID.fromString( objectId ) );
            String maxDepth = String.valueOf( projectDashboardDTO.getMaxDepth() );
            String columnNumber = String.valueOf( projectDashboardDTO.getColumnNumber() );
            Path inputFile = ProjectDashboardUtil.writeTableDataToTempFile( dashboardEntity,
                    table.stream().map( ProjectStructureTupleWrapper::getEntry ).toList() );
            Path outputFile = ProjectDashboardUtil.createOutputFileForChart( dashboardEntity );
            String dashboardScriptPath = DashboardPluginUtil.getScriptPath( DashboardPluginEnums.PROJECT.getId(),
                    DashboardConfigEnums.PROJECT_CLASSIFICATION.getConfig(), ProjectDashboardConstants.ICICLE_CHART );
            if ( Files.notExists( Path.of( dashboardScriptPath ) ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.PROJECT_DASHBORAD_SCRIPT_NOT_FOUND.getKey(), dashboardScriptPath ) );
            }
            ProjectDashboardUtil.callChartScript( columnNumber, maxDepth, inputFile, outputFile, dashboardScriptPath );
            return ProjectDashboardUtil.readContentFromFile( outputFile );

        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     * @param token
     *         the token
     *
     * @return the project structure table
     */
    @Override
    public FilteredResponse< Map< String, ProjectStructureTableCell > > getProjectStructureTable( String objectId, String userId,
            FiltersDTO filter, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {

            ProjectClassificationCache cache = ProjectDashboardUtil.validateAndRetrieveCache( objectId,
                    TokenizedLicenseUtil.getUserLanguage( token ) );
            List< ProjectStructureTupleWrapper > table = cache.getStructure();

            table = FilterHelper.getFilteredTableEntriesList( filter, table, cache.getMaxDepth().toString() );
            FilterHelper.sortEntriesList( filter, table );
            FilterHelper.decreasePageNumberToListSize( filter, table.size() );
            return PaginationUtil.constructFilteredResponse( filter,
                    table.subList( filter.getStart(), Math.min( filter.getStart() + filter.getLength(), table.size() ) ).stream()
                            .map( ProjectStructureTupleWrapper::getEntry ).toList() );
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the project structure table ui
     */
    @Override
    public TableUI getProjectStructureTableUI( String userId, String objectId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ProjectClassificationCache cache = ProjectDashboardUtil.validateAndRetrieveCache( objectId,
                    TokenizedLicenseUtil.getUserLanguage( token ) );

            int maxLevel = cache.getMaxDepth();
            List< TableColumn > columns = new ArrayList<>( maxLevel );
            for ( int i = 0; i < maxLevel; i++ ) {
                TableColumn column = ProjectDashboardUtil.getTableColumnByIndex( i );
                columns.add( column );
            }
            return new TableUI( columns, objectViewManager.getUserObjectViewsByKey( entityManager,
                    ConstantsObjectViewKey.PROJECT_STRUCTURE_TABLE_VIEW_KEY, userId, null ) );
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets all project dashboards.
     *
     * @return the all project dashboards
     */
    @Override
    public List< DataObjectDashboardEntity > getAllProjectDashboards() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var projectDashboards = susDAO.getLatestNonDeletedObjectListByProperty( entityManager, DataObjectDashboardEntity.class,
                    "plugin", DashboardPluginEnums.PROJECT.getId() );
            return projectDashboards.stream()
                    .filter( susEntity -> susEntity instanceof DataObjectDashboardEntity dashboardEntity
                            && DashboardConfigEnums.PROJECT_CLASSIFICATION.getConfig()
                            .equals( ( ( Map< String, String > ) JsonUtils
                                    .jsonToMap( ByteUtil.convertByteToString( dashboardEntity.getSettings() ), new HashMap<>() ) )
                                    .get( "config" ) ) )
                    .map( susEntity -> ( DataObjectDashboardEntity ) susEntity ).collect( Collectors.toList() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets dashboard by id.
     *
     * @param objectId
     *         the object id
     *
     * @return the dashboard by id
     */
    public DataObjectDashboardEntity getDashboardById( String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Map< String, Object > properties = new HashMap<>();
            properties.put( "plugin", DashboardPluginEnums.PROJECT.getId() );
            properties.put( ConstantsDAO.IS_DELETE, Boolean.FALSE );
            properties.put( ConstantsDAO.COMPOSED_ID_VERSION_ID, ConstantsInteger.INTEGER_VALUE_ONE );
            properties.put( ConstantsDAO.COMPOSED_ID_ID, UUID.fromString( objectId ) );
            var projectDashboards = ( List< DataObjectDashboardEntity > ) susDAO.getListByPropertyMapAndClass( entityManager, properties,
                    DataObjectDashboardEntity.class );
            return projectDashboards.stream().findFirst().orElse( null );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the project lifecycle table ui
     */
    @Override
    public TableUI getProjectLifecycleTableUI( String userId, String objectId ) {
        return new TableUI( GUIUtils.listColumns( LifecycleTableDTO.class ), getUserProjectLifecycleViews( userId ) );
    }

    /**
     * Gets project lifecycle table.
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     * @param token
     *         the token
     *
     * @return the project lifecycle table
     */
    @Override
    public FilteredResponse< LifecycleTableDTO > getProjectLifecycleTable( String objectId, String userId, FiltersDTO filter,
            String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ProjectClassificationCache cache = ProjectDashboardUtil.validateAndRetrieveCache( objectId,
                    TokenizedLicenseUtil.getUserLanguage( token ) );
            var dtoList = cache.getLifecycle();
            dtoList = FilterHelper.applyFiltersToLifecycleTable( filter, dtoList );
            FilterHelper.sortDtoList( filter, dtoList );
            FilterHelper.decreasePageNumberToListSize( filter, dtoList.size() );
            return PaginationUtil.constructFilteredResponse( filter,
                    dtoList.subList( filter.getStart(), Math.min( filter.getStart() + filter.getLength(), dtoList.size() ) ) );
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param objectJson
     *         the object json
     * @param key
     *         the key
     * @param save
     *         the save
     *
     * @return the object view dto
     */
    @Override
    public ObjectViewDTO prepareObjectView( String objectJson, String key, boolean save ) {
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
        if ( save && !objectViewDTO.isDefaultView() ) {
            objectViewDTO.setId( null );
        }
        objectViewDTO.setObjectViewKey( key );
        objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
        objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
        return objectViewDTO;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId
     *         the user id
     *
     * @return the user project structure views
     */
    @Override
    public List< ObjectViewDTO > getUserProjectStructureViews( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.getUserObjectViewsByKey( entityManager, ConstantsObjectViewKey.PROJECT_STRUCTURE_TABLE_VIEW_KEY,
                    userId, null );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param objectJson
     *         the object json
     * @param userId
     *         the user id
     * @param save
     *         the save
     *
     * @return the object view dto
     */
    @Override
    public ObjectViewDTO saveOrUpdateProjectStructureView( String objectJson, String userId, boolean save ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.saveOrUpdateObjectView( entityManager,
                    prepareObjectView( objectJson, ConstantsObjectViewKey.PROJECT_STRUCTURE_TABLE_VIEW_KEY, save ), userId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param viewId
     *         the view id
     *
     * @return the boolean
     */
    @Override
    public boolean deleteProjectStructureView( String viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.deleteObjectView( entityManager, UUID.fromString( viewId ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId
     *         the user id
     *
     * @return the user project lifecycle views
     */
    @Override
    public List< ObjectViewDTO > getUserProjectLifecycleViews( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.getUserObjectViewsByKey( entityManager, ConstantsObjectViewKey.PROJECT_LIFECYCLE_TABLE_VIEW_KEY,
                    userId, null );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param objectJson
     *         the object json
     * @param userId
     *         the user id
     * @param save
     *         the save
     *
     * @return the object view dto
     */
    @Override
    public ObjectViewDTO saveOrUpdateProjectLifecycleView( String objectJson, String userId, boolean save ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.saveOrUpdateObjectView( entityManager,
                    prepareObjectView( objectJson, ConstantsObjectViewKey.PROJECT_LIFECYCLE_TABLE_VIEW_KEY, save ), userId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param viewId
     *         the view id
     *
     * @return the boolean
     */
    @Override
    public boolean deleteProjectLifecycleView( String viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.deleteObjectView( entityManager, UUID.fromString( viewId ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets dashboard last modified.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the dashboard last modified
     */
    @Override
    public Date getDashboardLastModified( String objectId, String token ) {
        try {
            ProjectClassificationCache cache = ProjectDashboardUtil.validateAndRetrieveCache( objectId,
                    TokenizedLicenseUtil.getUserLanguage( token ) );
            return cache.getLastUpdated();
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * Gets project lifecycle context.
     *
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     * @param token
     *         the token
     *
     * @return the project lifecycle context
     */
    @Override
    public List< ContextMenuItem > getProjectLifecycleContext( String objectId, FiltersDTO filter, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager,
                    TokenizedLicenseUtil.getUser( token ).getId(), SelectionOrigins.CONTEXT, filter );
            List< ContextMenuItem > cml = new ArrayList<>();
            var firstItem = filter.getItems().stream().findFirst().orElse( null );
            Set< String > validOptions;
            if ( firstItem == null ) {
                return new ArrayList<>();
            } else {
                validOptions = getValidOptionsBasedOnLifecyclesOfSelectedItems( filter, entityManager, firstItem );
            }
            prepareContextMenuItemList( objectId, validOptions, cml, selectionResponseUI );
            return cml;
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare context menu item list.
     *
     * @param objectId
     *         the object id
     * @param validOptions
     *         the valid options
     * @param cml
     *         the cml
     * @param selectionResponseUI
     *         the selection response ui
     */
    private void prepareContextMenuItemList( String objectId, Set< String > validOptions, List< ContextMenuItem > cml,
            SelectionResponseUI selectionResponseUI ) {
        for ( var option : validOptions ) {
            var status = lifeCycleManager.getLifeCycleStatusByStatusId( option );
            cml.add( ProjectDashboardUtil.createContextMenuItemFromStatus( objectId, status, selectionResponseUI.getId() ) );
        }
    }

    /**
     * Gets valid options based on lifecycles of selected items.
     *
     * @param filter
     *         the filter
     * @param entityManager
     *         the entity manager
     * @param firstItem
     *         the first item
     *
     * @return the valid options based on lifecycles of selected items
     */
    private Set< String > getValidOptionsBasedOnLifecyclesOfSelectedItems( FiltersDTO filter, EntityManager entityManager,
            Object firstItem ) {
        Set< String > validOptions;
        var firstSelection = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( firstItem.toString() ) );
        validOptions = new HashSet<>(
                lifeCycleManager.getLifeCycleStatusByStatusId( firstSelection.getLifeCycleStatus() ).getCanMoveToStatus() );
        for ( Object item : filter.getItems() ) {
            String selectedObjectId = item.toString();
            var selectedEntity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( selectedObjectId ) );
            validOptions.retainAll( new HashSet<>(
                    lifeCycleManager.getLifeCycleStatusByStatusId( selectedEntity.getLifeCycleStatus() ).getCanMoveToStatus() ) );
        }
        return validOptions;
    }

    /**
     * Change project lifecycle boolean.
     *
     * @param objectId
     *         the object id
     * @param selectionId
     *         the selection id
     * @param statusId
     *         the status id
     * @param token
     *         the token from general header
     *
     * @return the boolean
     */
    @Override
    public Boolean changeProjectLifecycle( String objectId, String selectionId, String statusId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var selectedIdList = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
            boolean returnVal = updateStatusInEntity( statusId, token, selectedIdList, entityManager );
            returnVal = updateLifecycleStatusInCacheForAllLanguages( objectId, statusId, selectedIdList, returnVal );
            return returnVal;

        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update lifecycle status in cache for all languages boolean.
     *
     * @param objectId
     *         the object id
     * @param statusId
     *         the status id
     * @param selectedIdList
     *         the selected id list
     * @param returnVal
     *         the return val
     *
     * @return the boolean
     *
     * @throws IOException
     *         the io exception
     */
    private boolean updateLifecycleStatusInCacheForAllLanguages( String objectId, String statusId, List< UUID > selectedIdList,
            Boolean returnVal ) throws IOException {
        for ( var language : PropertiesManager.getRequiredlanguages().keySet() ) {
            ProjectClassificationCache cache = ProjectDashboardUtil.validateAndRetrieveCache( objectId, language );
            for ( var selectedId : selectedIdList ) {
                returnVal = returnVal && updateLifecycleInCache( cache, statusId, selectedId );
                ProjectDashboardUtil.writeLifecycleValuesToCache( objectId, language, cache );
            }
        }
        return returnVal;
    }

    /**
     * Update status in entity boolean.
     *
     * @param statusId
     *         the status id
     * @param token
     *         the token
     * @param selectedIdList
     *         the selected id list
     * @param entityManager
     *         the entity manager
     *
     * @return the boolean
     */
    private boolean updateStatusInEntity( String statusId, String token, List< UUID > selectedIdList, EntityManager entityManager ) {
        boolean retVal = true;
        for ( var selectedId : selectedIdList ) {
            ChangeStatusDTO status = new ChangeStatusDTO();
            status.setStatus( statusId );
            retVal = retVal
                    && dataManager.changeStatusObject( entityManager, TokenizedLicenseUtil.getUser( token ).getId(), selectedId, status );
        }
        return retVal;
    }

    /**
     * Update lifecycle in cache boolean.
     *
     * @param cache
     *         the cache
     * @param statusId
     *         the status id
     * @param selectedId
     *         the selected id
     *
     * @return the boolean
     */
    private boolean updateLifecycleInCache( ProjectClassificationCache cache, String statusId, UUID selectedId ) {
        var matchingEntryInCache = cache.getLifecycle().stream().filter( dto -> dto.getId().equals( selectedId ) ).findFirst()
                .orElse( null );
        if ( matchingEntryInCache == null ) {
            return false;
        }
        matchingEntryInCache
                .setLifeCycleStatus( lifeCycleManager.getStatusByLifeCycleNameAndStatusId( ConstantsID.OBJECT_LIFECYCLE_ID, statusId ) );
        return true;

    }

    /**
     * Register and execute scheduled task scheduled future.
     *
     * @return the scheduled future
     */
    private ScheduledFuture< ? > registerAndExecuteScheduledTask() {
        int corePoolSize = 1;
        ScheduledExecutorService scheduledPoolExecutor = Executors.newScheduledThreadPool( corePoolSize );
        return scheduledPoolExecutor.scheduleWithFixedDelay( new UpdateClassificationDashboardsThread( this ),
                ConstantsInteger.INTEGER_VALUE_ZERO, Long.parseLong( ProjectDashboardUtil.getUpdateIntervalFromConf() ),
                DateUtils.stringToTimeUnit( ProjectDashboardUtil.getIntervalUnitFromConf() ) );
    }

    /**
     * Gets list of parent names.
     *
     * @param entityManager
     *         the entity manager
     * @param suSEntity
     *         the su s entity
     * @param finalParentId
     *         the final parent id
     *
     * @return the list of parent names
     */
    @Override
    public List< String > getListOfParentNames( EntityManager entityManager, SuSEntity suSEntity, String finalParentId ) {
        List< String > parentNames = new ArrayList<>();
        addParentNamesToList( entityManager, suSEntity, parentNames, finalParentId );
        return parentNames;
    }

    /**
     * Add parent name to list.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     * @param list
     *         the list
     * @param finalParentId
     *         the final parent id
     */
    private void addParentNamesToList( EntityManager entityManager, SuSEntity susEntity, List< String > list, String finalParentId ) {
        List< SuSEntity > parents = susDAO.getParents( entityManager, susEntity );
        if ( CollectionUtils.isNotEmpty( parents ) ) {
            SuSEntity parent = parents.get( ConstantsInteger.INTEGER_VALUE_ZERO );
            if ( !parent.getComposedId().getId().toString().equals( finalParentId ) && parent.getTypeId() != null
                    && parent.getConfig() != null ) {
                list.add( parent.getName() );
                addParentNamesToList( entityManager, parent, list, finalParentId );
            }
        }
    }

    /**
     * Destroy.
     */
    public void destroy() {
        // called by container
        updateAllDashboardsFuture.cancel( true );
    }

    /**
     * Sets sus dao.
     *
     * @param susDAO
     *         the sus dao
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

    /**
     * Gets sus dao.
     *
     * @return the sus dao
     */
    @Override
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
    }

    /**
     * Sets selection manager.
     *
     * @param selectionManager
     *         the selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * Gets selection manager.
     *
     * @return the selection manager
     */
    @Override
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets object view manager.
     *
     * @param objectViewManager
     *         the object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
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

    /**
     * Gets entity manager factory.
     *
     * @return the entity manager factory
     */
    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Update dashboard cache.
     *
     * @param objectId
     *         the object id
     */
    @Override
    public void updateDashboardCache( String objectId ) {
        var thread = new UpdateClassificationDashboardsThread( this, objectId );
        thread.run();
    }

    /**
     * Sets life cycle manager.
     *
     * @param lifeCycleManager
     *         the life cycle manager
     */
    public void setLifeCycleManager( LifeCycleManager lifeCycleManager ) {
        this.lifeCycleManager = lifeCycleManager;
    }

    /**
     * Sets data manager.
     *
     * @param dataManager
     *         the data manager
     */
    public void setDataManager( DataManager dataManager ) {
        this.dataManager = dataManager;
    }

}
