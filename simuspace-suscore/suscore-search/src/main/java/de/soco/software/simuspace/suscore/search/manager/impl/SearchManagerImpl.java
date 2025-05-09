package de.soco.software.simuspace.suscore.search.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDate;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDateFilters;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFilterOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUrlViews;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.WidgetSettingsDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ConfigUtil;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.SearchUtil;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SearchObject;
import de.soco.software.simuspace.suscore.data.entity.SearchObjectModel;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.WidgetSettingsEntity;
import de.soco.software.simuspace.suscore.data.entity.WorkflowProjectEntity;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.data.model.ProjectDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.object.manager.DataObjectManager;
import de.soco.software.simuspace.suscore.search.constants.ConstantsSearch;
import de.soco.software.simuspace.suscore.search.dao.SearchDAO;
import de.soco.software.simuspace.suscore.search.manager.SearchManager;
import de.soco.software.simuspace.suscore.search.manager.WidgetSettingsManager;
import de.soco.software.simuspace.suscore.search.model.Bool;
import de.soco.software.simuspace.suscore.search.model.ELSQueryObject;
import de.soco.software.simuspace.suscore.search.model.Query;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.model.impl.ProjectConfiguration;
import de.soco.software.simuspace.workflow.model.impl.SuSObjectModel;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * This class manages search CRUD related (and other) operation to Dao layer.
 *
 * @author Ali Haider
 * @since 2.3
 */
@Log4j2
public class SearchManagerImpl implements SearchManager {

    /**
     * The property name that holds the master config file path.
     */
    public static final String MASTER_CONFIG_PATH_PROPERTY = "jsonschema.parent.filename";

    /**
     * The Constant ENTITY_CLASS_FIELD_NAME.
     */
    private static final String ENTITY_CLASS_FIELD_NAME = "ENTITY_CLASS";

    /**
     * The Constant URL_TYPE.
     */
    private static final String URL_TYPE = "urlType";

    /**
     * The Constant USERS.
     */
    private static final String USERS_QUERY = " AND ( users:*";

    /**
     * The Constant IS_DELETE_QUERY.
     */
    private static final String IS_DELETE_QUERY = " AND isDelete:false";

    /**
     * The Constant HIDDEN_OBJECTS_QUERY.
     */
    private static final String HIDDEN_OBJECTS_QUERY = " AND hidden:false";

    /**
     * The Constant RESTRICTED_USER.
     */
    private static final String RESTRICTED_USER = "Yes";

    /**
     * The Constant CREATED_ON.
     */
    private static final String CREATED_ON = "createdOn";

    /**
     * The Constant MODIFIED_ON.
     */
    private static final String MODIFIED_ON = "modifiedOn";

    /**
     * The Constant WILDCARD.
     */
    private static final String WILDCARD = "wildcard";

    /**
     * The constant SEARCH_SERVER_NOT_ACCESSIBLE.
     */
    private static final String SEARCH_SERVER_NOT_ACCESSIBLE = "Search server is not accessible";

    /**
     * The constant SEARCH_IS_DISABLED.
     */
    private static final String SEARCH_IS_DISABLED = "Elastic Search is disabled";

    /**
     * The Constant MATCH.
     */
    private static final String MATCH = "match";

    private static final String UTF_ENCODING = "UTF-8";

    /**
     * The Constant EMPTY_LIST.
     */
    private static final List< Object > EMPTY_LIST = new ArrayList<>();

    /**
     * The search dao.
     */
    private SearchDAO searchDao;

    /**
     * The widgets manager.
     */
    private WidgetSettingsManager widgetsManager;

    /**
     * The data manager.
     */
    private DataObjectManager dataObjectManager;

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    private UserCommonManager userCommonManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Init method for bootup.
     *
     * @throws IOException
     *         the io exception
     */
    public void init() throws IOException {
        if ( PropertiesManager.reindexElasticSearch() && CommonUtils.validateURL() ) {
            deleteAllIndexes();
            prepareIndexMap();
            new Thread( this::createNewIndexes ).start();
        }
    }

    /**
     * Create new indexes.
     */
    private void createNewIndexes() {
        EntityManager runnableEM = entityManagerFactory.createEntityManager();
        var entityList = searchDao.getLatestNonDeletedObjectListByNotNullProperty( runnableEM, SuSEntity.class, ConstantsDAO.TYPE_ID );
        try {
            for ( SuSEntity suSEntity : entityList ) {
                int type = 0;
                if ( SearchUtil.getIndexMap().containsKey( suSEntity.getTypeId().toString() ) ) {
                    type = SearchUtil.getIndexMap().get( suSEntity.getTypeId().toString() ).index();
                }
                if ( type > 0 ) {
                    createIndexEntity( runnableEM, type, suSEntity );
                }
            }
        } finally {
            runnableEM.close();
        }
    }

    /**
     * Prepare index map.
     *
     * @throws IOException
     *         the io exception
     */
    private void prepareIndexMap() throws IOException {
        int typeRepresentation = ConstantsInteger.INTEGER_VALUE_ZERO;
        List< SearchObject > lst = getSearchTree();
        for ( SearchObject searchObject : lst ) {
            for ( SearchObjectModel searchObjectModel : searchObject.getEntityConfig() ) {
                SearchUtil.getIndexMap().put( searchObjectModel.getId().toString(),
                        SearchUtil.getIndexMap().values().stream().filter( pair -> searchObject.getName().equals( pair.name() ) )
                                .findFirst().orElse( SearchUtil.createNameIndexPair( searchObject.getName(), ++typeRepresentation ) ) );

            }
        }
    }

    /**
     * Creates the index entity.
     *
     * @param entityManager
     *         the entity manager
     * @param elsIndex
     *         the els index
     * @param entity
     *         the entity
     */
    private void createIndexEntity( EntityManager entityManager, int elsIndex, SuSEntity entity ) {
        try {
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
            requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
            String url = PropertiesManager.getElasticSearchURL() + ConstantsString.FORWARD_SLASH + elsIndex + "/_doc/"
                    + entity.getComposedId().getId();
            SuSClient.postRequest( url,
                    JsonUtils.toJson(
                            dataObjectManager.createGenericDTOFromObjectEntity( entityManager, null, null, entity, new ArrayList<>(),
                                    false ) ),
                    requestHeaders );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, SearchManagerImpl.class );
        }
    }

    /**
     * Delete all indexes.
     */
    private void deleteAllIndexes() {
        try {
            String commaSeperatedString = SearchUtil.getIndexMap().values().stream()
                    .map( nameIndexPair -> nameIndexPair.index().toString() ).collect( Collectors.joining( ConstantsString.COMMA ) );
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
            requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
            String url = PropertiesManager.getElasticSearchURL() + ConstantsString.FORWARD_SLASH + commaSeperatedString
                    + "?ignore_unavailable=true";
            SuSClient.deleteRequest( url, requestHeaders );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, SearchManagerImpl.class );
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public List< SearchObject > getSearchTree() throws IOException {
        List< ConfigUtil.Config > projectStartConfig = ConfigFilePathReader.getMasterConfigurationFileNames( MASTER_CONFIG_PATH_PROPERTY );
        if ( projectStartConfig.isEmpty() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_CONFIG_ARRAY_IS_EMPTY.getKey() ) );
        }
        List< ConfigUtil.Config > masterConfigurationFileNamesForWorkflows = ConfigFilePathReader
                .getMasterConfigurationFileNamesForWorkflows( MASTER_CONFIG_PATH_PROPERTY );
        List< ConfigUtil.Config > configFiles = new ArrayList<>();
        configFiles.addAll( projectStartConfig );
        configFiles.addAll( masterConfigurationFileNamesForWorkflows );
        return getConfigs( configFiles );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SearchObject > getSearchTreeBySearchId( String userIdFromGeneralHeader, String searchId ) throws IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< ConfigUtil.Config > projectStartConfig = ConfigFilePathReader.getMasterConfigurationFileNames(
                    MASTER_CONFIG_PATH_PROPERTY );
            if ( projectStartConfig.isEmpty() ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_CONFIG_ARRAY_IS_EMPTY.getKey() ) );
            }
            List< ConfigUtil.Config > masterConfigurationFileNamesForWorkflows = ConfigFilePathReader
                    .getMasterConfigurationFileNamesForWorkflows( MASTER_CONFIG_PATH_PROPERTY );
            List< ConfigUtil.Config > configFiles = new ArrayList<>();
            configFiles.addAll( projectStartConfig );
            configFiles.addAll( masterConfigurationFileNamesForWorkflows );

            List< SearchObject > configs = getConfigs( configFiles );
            WidgetSettingsEntity widgetSettingsEntity = widgetsManager.getWidgetSettingsById( entityManager, UUID.fromString( searchId ) );
            if ( widgetSettingsEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_SEARCH_ID.getKey() ) );
            } else {
                String settings = widgetSettingsEntity.getSettings();
                try {
                    JSONObject settingsObject = new JSONObject( settings );
                    String searchInString = settingsObject.getJSONArray( ConstantsSearch.SEARCH_IN ).toString();
                    List< String > filterSearchIn = JsonUtils.jsonToList( searchInString, String.class );

                    for ( SearchObject searchObject : configs ) {
                        if ( !filterSearchIn.contains( searchObject.getName() ) ) {
                            searchObject.setSelected( false );
                        }
                    }

                } catch ( Exception e ) {
                    ExceptionLogger.logException( e, getClass() );
                    throw e;
                }
            }
            return configs;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getSearchListUIBySearchId( String userId, String searchId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String settings = null;
            if ( null != searchId ) {
                WidgetSettingsEntity widgetSettingsEntity = widgetsManager.getWidgetSettingsById( entityManager,
                        UUID.fromString( searchId ) );
                if ( widgetSettingsEntity == null ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_SEARCH_ID.getKey() ) );
                } else {
                    settings = widgetSettingsEntity.getSettings();
                }
            }

            Set< String > classNameList = new HashSet<>();
            List< TableColumn > columns = new ArrayList<>();
            if ( null != searchId ) {
                WidgetSettingsEntity widgetSettingEntity = widgetsManager.getWidgetSettingsById( entityManager,
                        UUID.fromString( searchId ) );
                FiltersDTO filter = JsonUtils.jsonToObject( widgetSettingEntity.getSettings(), FiltersDTO.class );
                try {
                    List< SearchObject > searchObjectList = getSearchTree();
                    for ( String typeName : filter.getSearchIn() ) {
                        for ( SearchObject searchObject : searchObjectList ) {
                            prepareClassList( classNameList, typeName, searchObject );
                        }
                    }
                    if ( classNameList.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
                        Class< ? > clazz = Class.forName( new ArrayList<>( classNameList ).get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
                        Object o = initializeObject( clazz.getName() );
                        Object entity = new DataObjectEntity();
                        try {
                            Field f = clazz.getDeclaredField( ENTITY_CLASS_FIELD_NAME );
                            f.setAccessible( true );
                            entity = f.get( o );
                        } catch ( Exception e ) {
                            ExceptionLogger.logException( e, getClass() );
                        }
                        List< TableColumn > column = GUIUtils.listColumns( clazz );

                        if ( entity instanceof WorkflowEntity ) {
                            GUIUtils.setLinkColumn( WorkflowDTO.NAME, ConstantsUrlViews.WORKFLOW_VIEW, column );
                        } else if ( entity instanceof WorkflowProjectEntity ) {
                            GUIUtils.setLinkColumn( WorkflowDTO.NAME, ConstantsUrlViews.WORKFLOW_PROJECT_VIEW, column );
                        } else if ( entity instanceof ContainerEntity ) {
                            GUIUtils.setLinkColumn( ProjectDTO.PROJECT_NAME, ConstantsUrlViews.DATA_PROJECT_VIEW, column );
                        } else {
                            GUIUtils.setLinkColumn( DataObjectDTO.DATAOBJECT_NAME, ConstantsUrlViews.DATA_OBJECT_VIEW, column );
                        }
                        return new TableUI( column );
                    } else {
                        columns = GUIUtils.listColumns( GenericDTO.class );
                        Map< String, String > nameUrlValues = new HashMap<>();
                        nameUrlValues.put( ConstantsString.OBJECT_KEY, ConstantsUrlViews.DATA_OBJECT_VIEW );
                        nameUrlValues.put( ConstantsString.PROJECT_KEY, ConstantsUrlViews.DATA_PROJECT_VIEW );
                        nameUrlValues.put( ConstantsString.WORKFLOW_KEY, ConstantsUrlViews.WORKFLOW_VIEW );
                        nameUrlValues.put( ConstantsString.WORKFLOW_PROJECT_KEY, ConstantsUrlViews.WORKFLOW_PROJECT_VIEW );
                        GUIUtils.setLinkColumn( DataObjectDTO.DATAOBJECT_NAME, URL_TYPE, nameUrlValues, columns );
                    }
                } catch ( IOException | ClassNotFoundException e ) {
                    ExceptionLogger.logException( e, getClass() );
                }
            }
            return new TableUI( columns, new ArrayList<>(), settings );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getSearchListUI( String userIdFromGeneralHeader ) {
        List< TableColumn > columns = GUIUtils.listColumns( GenericDTO.class );
        Map< String, String > nameUrlValues = new HashMap<>();
        nameUrlValues.put( ConstantsString.OBJECT_KEY, ConstantsUrlViews.DATA_OBJECT_VIEW );
        nameUrlValues.put( ConstantsString.PROJECT_KEY, ConstantsUrlViews.DATA_PROJECT_VIEW );
        nameUrlValues.put( ConstantsString.WORKFLOW_KEY, ConstantsUrlViews.WORKFLOW_VIEW );
        nameUrlValues.put( ConstantsString.WORKFLOW_PROJECT_KEY, ConstantsUrlViews.WORKFLOW_PROJECT_VIEW );
        GUIUtils.setLinkColumn( DataObjectDTO.DATAOBJECT_NAME, URL_TYPE, nameUrlValues, columns );
        return new TableUI( columns );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ? > getFilteredSearchList( String userId, String searchId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getFilteredSearchList( entityManager, userId, searchId, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param searchId
     */
    @Override
    public FilteredResponse< ? > getFilteredSearchList( EntityManager entityManager, String userId, String searchId, FiltersDTO filter ) {
        isRestricted( entityManager, UUID.fromString( userId ) );
        List< String > searchIn = filter.getSearchIn();
        Set< String > classNameList = new HashSet<>();
        try {
            List< SearchObject > searchObjectList = getSearchTree();
            for ( String typeName : searchIn ) {
                for ( SearchObject searchObject : searchObjectList ) {
                    prepareClassList( classNameList, typeName, searchObject );
                }
            }
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
        }

        if ( null == filter.getSearchId() ) {
            WidgetSettingsDTO widgetDTO = saveSearch( entityManager, UUID.fromString( userId ), filter );
            if ( widgetDTO != null ) {
                filter.setSearchId( UUID.fromString( widgetDTO.getId() ) );
                filter.setCurrentView( widgetDTO.getSettings().toString() );
            }
        }
        List< Class< ? > > classList = new ArrayList<>();
        for ( String className : classNameList ) {
            classList.add( ReflectionUtils.getFieldTypeByName( initializeObject( className ).getClass(), ENTITY_CLASS_FIELD_NAME ) );
        }
        if ( filter.isReloadUI() ) {
            return PaginationUtil.constructFilteredResponse( filter, new ArrayList<>() );
        } else {
            return prepareResponse( entityManager, userId, filter, searchIn, classList.toArray( new Class[ classList.size() ] ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ? > getFilteredSearchListBySearchId( String userId, String searchId, FiltersDTO filterDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String settings;
            if ( null != searchId ) {
                WidgetSettingsEntity widgetSettingsEntity = widgetsManager.getWidgetSettingsById( entityManager,
                        UUID.fromString( searchId ) );
                if ( widgetSettingsEntity == null ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_SEARCH_ID.getKey() ) );
                } else {
                    settings = widgetSettingsEntity.getSettings();
                    FiltersDTO savedFilter = JsonUtils.jsonToObject( settings, FiltersDTO.class );
                    if ( filterDTO.equals( savedFilter ) ) {
                        FiltersDTO filters = prepareFilters( searchId, filterDTO, settings );
                        filters.setReloadUI( getReloadUI( filterDTO.getSearchIn(), savedFilter.getSearchIn() ) );
                        return this.getFilteredSearchList( userId, searchId, filters );
                    } else {
                        filterDTO.setReloadUI( getReloadUI( filterDTO.getSearchIn(), savedFilter.getSearchIn() ) );
                        filterDTO.setSearchId( null );
                        return this.getFilteredSearchList( userId, searchId, filterDTO );
                    }
                }
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_SEARCH_ID.getKey() ) );
            }
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< ContextMenuItem > getSearchContextRouter( String userIdFromGeneralHeader, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter.getItems().size() > ConstantsInteger.INTEGER_VALUE_ONE || filter.getItems().isEmpty() ) {
                return new ArrayList<>();
            }
            List< String > selectedIds = new ArrayList<>();
            selectedIds.add( Objects.toString( filter.getItems().get( ConstantsInteger.INTEGER_VALUE_ZERO ), null ) );

            return dataObjectManager.getDataListContext( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.SEARCH.getId(),
                    filter,
                    selectedIds, ConstantsString.EMPTY_STRING, true );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare filters.
     *
     * @param searchId
     *         the search id
     * @param filterDTO
     *         the filter DTO
     * @param settings
     *         the settings
     *
     * @return the filters DTO
     */
    private FiltersDTO prepareFilters( String searchId, FiltersDTO filterDTO, String settings ) {
        JSONObject settingsObject = new JSONObject( settings );
        FiltersDTO filters = new FiltersDTO();
        filters.setStart( filterDTO.getStart() );
        filters.setLength( settingsObject.getInt( ConstantsSearch.LENGTH ) );
        String searchString = settingsObject.getJSONArray( ConstantsSearch.COLUMNS ).toString();
        List< FilterColumn > filterSearch = JsonUtils.jsonToList( searchString, FilterColumn.class );
        filters.setColumns( filterSearch );
        filters.setSearch( settingsObject.getString( ConstantsSearch.SEARCH ) );
        String searchInString = settingsObject.getJSONArray( ConstantsSearch.SEARCH_IN ).toString();
        List< String > filterSearchIn = JsonUtils.jsonToList( searchInString, String.class );
        filters.setSearchIn( filterSearchIn );
        filters.setSearchId( UUID.fromString( searchId ) );
        filters.setCurrentView( settings );
        return filters;
    }

    /**
     * Gets the reload UI.
     *
     * @param types
     *         the types
     * @param savedtypes
     *         the savedtypes
     *
     * @return the reload UI
     */
    private boolean getReloadUI( List< String > types, List< String > savedtypes ) {
        Set< String > classNameList = new HashSet<>();
        Set< String > savedClassNameList = new HashSet<>();
        try {
            List< SearchObject > searchObjectList = getSearchTree();
            for ( String typeName : types ) {
                for ( SearchObject searchObject : searchObjectList ) {
                    prepareClassList( classNameList, typeName, searchObject );
                }
            }
            for ( String typeName : savedtypes ) {
                for ( SearchObject searchObject : searchObjectList ) {
                    prepareClassList( savedClassNameList, typeName, searchObject );
                }
            }
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return !classNameList.equals( savedClassNameList );
    }

    /**
     * Save search.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param filterDTO
     *         the filter
     *
     * @return the widget settings DTO
     */
    private WidgetSettingsDTO saveSearch( EntityManager entityManager, UUID userId, FiltersDTO filterDTO ) {
        WidgetSettingsDTO widgetSettingsDTO = null;

        if ( filterDTO != null ) {
            if ( null == filterDTO.getSearchId() ) {
                try {
                    widgetSettingsDTO = widgetsManager.saveWidgetSetting( entityManager, userId,
                            prepareSearchEntity( entityManager, userId, filterDTO ) );

                } catch ( Exception e ) {
                    ExceptionLogger.logException( e, getClass() );
                }
            } else {
                widgetSettingsDTO = new WidgetSettingsDTO( UUID.randomUUID().toString(), ConstantsString.EMPTY_STRING, null );
            }
        }
        return widgetSettingsDTO;
    }

    /**
     * Prepare search entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param filterDTO
     *         the filter DTO
     *
     * @return the widget settings entity
     */
    private WidgetSettingsEntity prepareSearchEntity( EntityManager entityManager, UUID userId, FiltersDTO filterDTO ) {
        WidgetSettingsEntity createdEntity = new WidgetSettingsEntity( ConstantsSearch.SEARCH, prepareSettings( filterDTO ) );
        createdEntity.setId( UUID.randomUUID() );
        UserDTO user = userCommonManager.getUserById( entityManager, userId );
        createdEntity.setCreatedBy( prepareUserEntityFromUserModel( user ) );
        Date currentDate = new Date();
        createdEntity.setCreatedOn( currentDate );
        createdEntity.setUpdatedOn( currentDate );
        return createdEntity;
    }

    /**
     * Prepare settings.
     *
     * @param filterDTO
     *         the filter DTO
     *
     * @return the string
     */
    private String prepareSettings( FiltersDTO filterDTO ) {
        Map< String, Object > settingsMap = new HashMap<>();
        settingsMap.put( ConstantsSearch.COLUMNS, filterDTO.getColumns() );
        settingsMap.put( ConstantsSearch.LENGTH, filterDTO.getLength() );
        settingsMap.put( ConstantsSearch.SEARCH, filterDTO.getSearch() );
        settingsMap.put( ConstantsSearch.SEARCH_IN, filterDTO.getSearchIn() );
        settingsMap.put( ConstantsSearch.START, filterDTO.getStart() );
        return JsonUtils.toJson( settingsMap );
    }

    /**
     * Gets the configs.
     *
     * @param configFiles
     *         the config files
     *
     * @return the configs
     */
    private List< SearchObject > getConfigs( List< ConfigUtil.Config > configFiles ) {
        List< SearchObject > configs = new ArrayList<>();
        Map< String, ProjectConfiguration > newProjectConfigMap = loadNewConfigurationIntoMap( configFiles );
        for ( Entry< String, ProjectConfiguration > map : newProjectConfigMap.entrySet() ) {
            for ( SuSObjectModel susObjectModel : map.getValue().getEntityConfig() ) {
                // for new
                if ( !isTypeContains( configs, susObjectModel ) ) {
                    SearchObjectModel searchObjectType = prepareSearchObjectModel( susObjectModel );
                    searchObjectType.setConfig( map.getKey() );
                    List< SearchObjectModel > entityConfig = new ArrayList<>();
                    entityConfig.add( searchObjectType );
                    SearchObject searchObject = new SearchObject( susObjectModel.getName(), susObjectModel.getIcon(), entityConfig );
                    searchObject.setSelected( true );
                    configs.add( searchObject );
                } else {
                    addEntity( configs, map, susObjectModel );
                }
            }
        }
        return sortTreeList( configs );
    }

    /**
     * Sort list.
     *
     * @param configs
     *         the configs
     *
     * @return the list
     */
    private List< SearchObject > sortTreeList( List< SearchObject > configs ) {
        return configs.stream().sorted( Comparator.comparing( SearchObject::getName ) ).toList();
    }

    /**
     * Prepare user entity from user model.
     *
     * @param userDTO
     *         the user DTO
     *
     * @return the user entity
     */
    public UserEntity prepareUserEntityFromUserModel( UserDTO userDTO ) {
        UserEntity user = new UserEntity();
        user.setId( UUID.fromString( userDTO.getId() ) );
        user.setUserUid( userDTO.getUserUid() );
        user.setFirstName( userDTO.getFirstName() );
        user.setSurName( userDTO.getSurName() );
        user.setStatus( userDTO.getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE ) );
        user.setRestricted( userDTO.getRestricted().equalsIgnoreCase( ConstantsStatus.ACTIVE ) );
        user.setChangeable( userDTO.isChangable() );
        return user;
    }

    /**
     * Prepare class list.
     *
     * @param classNameList
     *         the class name list
     * @param typeName
     *         the type name
     * @param searchObject
     *         the search object
     */
    private void prepareClassList( Set< String > classNameList, String typeName, SearchObject searchObject ) {
        if ( typeName.equalsIgnoreCase( searchObject.getName() ) ) {
            for ( SearchObjectModel objectModel : searchObject.getEntityConfig() ) {
                classNameList.add( objectModel.getClassName() );
            }
        }
    }

    /**
     * Initialize object.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    private static Object initializeObject( String className ) {

        try {
            return Class.forName( className ).newInstance();
        } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ) );
        }
    }

    /**
     * Checks if is type contains.
     *
     * @param configs
     *         the configs
     * @param susObjectModel
     *         the sus object model
     *
     * @return true, if is type contains
     */
    private boolean isTypeContains( List< SearchObject > configs, SuSObjectModel susObjectModel ) {
        for ( SearchObject searchObject : configs ) {
            if ( searchObject.getName().equalsIgnoreCase( susObjectModel.getName() ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the entity.
     *
     * @param configs
     *         the configs
     * @param map
     *         the map
     * @param susObjectModel
     *         the sus object model
     */
    private void addEntity( List< SearchObject > configs, Entry< String, ProjectConfiguration > map, SuSObjectModel susObjectModel ) {
        for ( SearchObject searchObject : configs ) {
            if ( searchObject.getName().equalsIgnoreCase( susObjectModel.getName() ) ) {
                SearchObjectModel searchObjectType = prepareSearchObjectModel( susObjectModel );
                searchObjectType.setConfig( map.getKey() );
                searchObject.getEntityConfig().add( searchObjectType );
            }
        }
    }

    /**
     * Prepare search object model.
     *
     * @param susObjectModel
     *         the sus object model
     *
     * @return the search object model
     */
    private SearchObjectModel prepareSearchObjectModel( SuSObjectModel susObjectModel ) {
        SearchObjectModel searchObjectType = new SearchObjectModel();
        searchObjectType.setId( UUID.fromString( susObjectModel.getId() ) );
        searchObjectType.setClassName( susObjectModel.getClassName() );
        return searchObjectType;
    }

    /**
     * The function loads the new configuration into Map after converting to the object type format. It further helps in dealing with the
     * data.
     *
     * @param configFileNamesList
     *         the config file names list
     *
     * @return the map
     */
    private Map< String, ProjectConfiguration > loadNewConfigurationIntoMap( final List< ConfigUtil.Config > configFileNamesList ) {
        Map< String, ProjectConfiguration > newConfigObjectsMap = new HashMap<>();
        for ( ConfigUtil.Config subConfigFileName : configFileNamesList ) {
            newConfigObjectsMap.put( subConfigFileName.label(), getProjectConfigurationsByFileName( subConfigFileName.file() ) );
        }
        return newConfigObjectsMap;
    }

    /**
     * Gets the project configurations by file name.
     *
     * @param subConfigFileName
     *         the sub config file name
     *
     * @return the project configurations by file name
     */
    private ProjectConfiguration getProjectConfigurationsByFileName( String subConfigFileName ) {
        try ( FileInputStream fis = new FileInputStream( ConstantsKaraf.KARAF_CONF_PATH + subConfigFileName ) ) {
            return JsonUtils.jsonStreamToObject( fis, ProjectConfiguration.class );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return new ProjectConfiguration();

    }

    /**
     * Prepare general response.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     * @param searchIn
     *         the search in
     * @param clazz
     *         the clazz
     *
     * @return the filtered response
     */
    private FilteredResponse< ? > prepareResponse( EntityManager entityManager, String userId, FiltersDTO filter, List< String > searchIn,
            Class< ? >... clazz ) {
        List< Object > returnList = new ArrayList<>();
        if ( clazz.length != 0 ) {
            List< GenericDTO > genericDTO = fullTextSearch( entityManager, userId, searchIn, filter );
            returnList.addAll( genericDTO );
            String allCountUrl = PropertiesManager.getElasticSearchURL() + "/*/_count";
            Long total = getCount( allCountUrl );
            filter.setTotalRecords( total );
        } else {
            filter.setTotalRecords( 0L );
        }
        return PaginationUtil.constructFilteredResponse( filter, returnList );
    }

    /**
     * Full text search.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param searchInIds
     *         the search in ids
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the list
     */
    private List< GenericDTO > fullTextSearch( EntityManager entityManager, String userId, List< String > searchInIds,
            FiltersDTO filtersDTO ) {
        List< GenericDTO > list = new ArrayList<>();
        if ( !PropertiesManager.enableElasticSearch() ) {
            throw new SusException( SEARCH_IS_DISABLED );
        }
        if ( !CommonUtils.validateURL() ) {
            throw new SusException( SEARCH_SERVER_NOT_ACCESSIBLE );
        }
        try {
            StringJoiner commaSeperatedString = new StringJoiner( ConstantsString.COMMA );
            for ( String type : searchInIds ) {
                var matchingPair = SearchUtil.getIndexMap().values().stream().filter( pair -> type.equals( pair.name() ) ).findFirst()
                        .orElse( null );
                if ( matchingPair != null ) {
                    int index = matchingPair.index();
                    commaSeperatedString.add( String.valueOf( index ) );
                }
            }
            String url = PropertiesManager.getElasticSearchURL() + ConstantsString.FORWARD_SLASH + commaSeperatedString
                    + "/_search?ignore_unavailable=true";
            Query mainQuery = new Query( prepareBoolQuery( entityManager, userId, filtersDTO ) );
            String payload = JsonUtils.toJson( getQueryJson( mainQuery, filtersDTO, false ) );
            log.info( payload );

            CloseableHttpResponse closeableHttpfilterLimitResponse = SuSClient.postExternalRequest( url, payload, getHeadersForELS() );
            HttpEntity entity = closeableHttpfilterLimitResponse.getEntity();
            String responseString = EntityUtils.toString( entity, UTF_ENCODING );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode hitNode = jsonNode.get( "hits" ).get( "hits" );
            for ( JsonNode indexEntity : hitNode ) {
                list.add( JsonUtils.jsonToObject( indexEntity.get( "_source" ).toString(), GenericDTO.class ) );
            }
            populateFilteredCount( filtersDTO, url, mainQuery );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, SearchManagerImpl.class );
            throw new SusException( e );
        }
        return list;
    }

    /**
     * Prepare main bool query.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the bool
     */
    private Bool prepareBoolQuery( EntityManager entityManager, String userId, FiltersDTO filtersDTO ) {
        Query query = new Query( getBoolQueryList( entityManager, userId ) );
        List< Object > shouldQueryList = new ArrayList<>();
        shouldQueryList.add( query );
        shouldQueryList.add( addCreatedByQuery( userId ) );
        return new Bool( getMainBoolQuery( filtersDTO, shouldQueryList ), EMPTY_LIST, EMPTY_LIST );
    }

    /**
     * Gets the main bool query.
     *
     * @param filtersDTO
     *         the filters DTO
     * @param shouldQueryList
     *         the should query list
     *
     * @return the main bool query
     */
    private List< Object > getMainBoolQuery( FiltersDTO filtersDTO, List< Object > shouldQueryList ) {
        List< Object > mustQueryList = new ArrayList<>();
        mustQueryList.add( new Query( new Bool( EMPTY_LIST, EMPTY_LIST, shouldQueryList ) ) );
        mustQueryList.add( getSearchAndFilterQuery( filtersDTO ) );
        return mustQueryList;
    }

    /**
     * Gets the search and filter query.
     *
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the search and filter query
     */
    private Query getSearchAndFilterQuery( FiltersDTO filtersDTO ) {
        List< Object > mustQueryList = new ArrayList<>();
        List< Object > mustNotQueryList = new ArrayList<>();
        List< Object > shouldQueryList = new ArrayList<>();
        for ( FilterColumn column : filtersDTO.getColumns() ) {
            if ( null != column.getFilters() && !column.getFilters().isEmpty() ) {
                for ( Filter filter : column.getFilters() ) {
                    // update column name to get els query field name
                    String columnNameInELS = null;
                    if ( column.getName().equalsIgnoreCase( "name" ) || column.getName().equalsIgnoreCase( "description" )
                            || column.getName().equalsIgnoreCase( "type" ) ) {
                        columnNameInELS = column.getName() + ".keyword";
                    } else if ( column.getName().equalsIgnoreCase( "createdBy" ) || ( column.getName()
                            .equalsIgnoreCase( "modifiedBy" ) ) ) {
                        columnNameInELS = column.getName() + ".userUid";
                    } else {
                        columnNameInELS = column.getName();
                    }
                    if ( column.getName().equalsIgnoreCase( CREATED_ON ) || column.getName().equalsIgnoreCase( MODIFIED_ON ) ) {
                        // apply date filters
                        prepareFilterQueryForDate( mustQueryList, shouldQueryList, filter, columnNameInELS );
                    } else {
                        perpareFilterQuery( mustQueryList, mustNotQueryList, shouldQueryList, filter, columnNameInELS );
                    }

                }
            }
        }
        if ( !filtersDTO.getSearch().isEmpty() ) {
            mustQueryList.add( addSearchQuery( filtersDTO ) );
        }
        return new Query( new Bool( mustQueryList, mustNotQueryList, shouldQueryList ) );
    }

    /**
     * Populate filtered count.
     *
     * @param filter
     *         the filter
     * @param url
     *         the url
     * @param query
     *         the query
     */
    private void populateFilteredCount( FiltersDTO filter, String url, Query query ) {
        String payload = JsonUtils.toJson( getQueryJson( query, filter, true ) );
        CloseableHttpResponse closeableHttpFilterCountResponse = SuSClient.postExternalRequest( url, payload, getHeadersForELS() );
        log.info( payload );
        try {
            HttpEntity entity = closeableHttpFilterCountResponse.getEntity();
            String responseString = EntityUtils.toString( entity, UTF_ENCODING );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            Long filteredCount = jsonNode.get( "hits" ).get( "total" ).get( "value" ).asLong();
            filter.setFilteredRecords( filteredCount );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, SearchManagerImpl.class );
            throw new SusException( e );
        }
    }

    /**
     * Gets the query json.
     *
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the query json
     */
    private ELSQueryObject getQueryJson( Query query, FiltersDTO filtersDTO, boolean filterResult ) {
        ELSQueryObject json;
        if ( filterResult ) {
            json = new ELSQueryObject( query, 0, 10000, getSorting( filtersDTO ) );
        } else {
            json = new ELSQueryObject( query, filtersDTO.getStart(), filtersDTO.getLength(), getSorting( filtersDTO ) );
        }
        return json;

    }

    /**
     * Gets the bool query list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the bool query list
     */
    private Bool getBoolQueryList( EntityManager entityManager, String userId ) {
        List< Object > mustQueryListWithSearch = new ArrayList<>();
        mustQueryListWithSearch.add( addUserOrGroupPermissionQuery( entityManager, userId ) );
        mustQueryListWithSearch.add( addLifeCycleVisibilityCheckQuery() );
        return new Bool( mustQueryListWithSearch, EMPTY_LIST, EMPTY_LIST );
    }

    /**
     * Adds the life cycle visibility check query.
     *
     * @return the query
     */
    private Query addLifeCycleVisibilityCheckQuery() {
        List< String > lifeCycleIds = lifeCycleManager.getAnyVisibleStatusByPolicyId( ConstantsID.OBJECT_LIFECYCLE_ID );
        List< Object > lifeCycleStatusForORQuery = new ArrayList<>();
        lifeCycleIds.forEach( lifeCycleId -> {
            Map< String, Object > queryMap = new HashMap<>();
            Map< String, String > fieldMap = new HashMap<>();
            fieldMap.put( "lifeCycleStatus.id", lifeCycleId );
            queryMap.put( MATCH, fieldMap );
            lifeCycleStatusForORQuery.add( queryMap );
        } );
        return new Query( new Bool( EMPTY_LIST, EMPTY_LIST, lifeCycleStatusForORQuery ) );
    }

    /**
     * Adds the user or group permission query.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the object
     */
    private Query addUserOrGroupPermissionQuery( EntityManager entityManager, String userId ) {
        List< Object > shouldQueryList = new ArrayList<>();
        List< UUID > groupEntities = searchDao.getGroupsByUserId( entityManager, userId );
        shouldQueryList.add( addUsersQuery( userId ) );
        groupEntities.forEach( uuid -> {
            Map< String, Object > queryMap = new HashMap<>();
            Map< String, String > fieldMap = new HashMap<>();
            fieldMap.put( "groups", String.valueOf( uuid ) );
            queryMap.put( MATCH, fieldMap );
            shouldQueryList.add( queryMap );
        } );
        return new Query( new Bool( EMPTY_LIST, EMPTY_LIST, shouldQueryList ) );
    }

    /**
     * Adds the search query.
     *
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the map
     */
    private Map< String, Object > addSearchQuery( FiltersDTO filtersDTO ) {
        Map< String, Object > nameQuery = new HashMap<>();
        Map< String, String > nameQueryMap = new HashMap<>();
        nameQueryMap.put( "name.keyword", filtersDTO.getSearch() );
        nameQuery.put( WILDCARD, nameQueryMap );
        return nameQuery;
    }

    /**
     * Adds the user query.
     *
     * @param userId
     *         the user id
     *
     * @return the map
     */
    private Map< String, Object > addUsersQuery( String userId ) {
        Map< String, Object > nameQuery = new HashMap<>();
        Map< String, String > nameQueryMap = new HashMap<>();
        nameQueryMap.put( "users", userId );
        nameQuery.put( MATCH, nameQueryMap );
        return nameQuery;
    }

    /**
     * Adds the created by query.
     *
     * @param userId
     *         the user id
     *
     * @return the map
     */
    private Map< String, Object > addCreatedByQuery( String userId ) {
        Map< String, Object > nameQuery = new HashMap<>();
        Map< String, String > nameQueryMap = new HashMap<>();
        nameQueryMap.put( "createdBy.id", userId );
        nameQuery.put( MATCH, nameQueryMap );
        return nameQuery;
    }

    /**
     * Perpare filter query.
     *
     * @param must
     *         the must
     * @param mustNot
     *         the must not
     * @param should
     *         the should
     * @param filter
     *         the filter
     * @param columnNameInELS
     *         the column name in ELS
     */
    private void perpareFilterQuery( List< Object > must, List< Object > mustNot, List< Object > should, Filter filter,
            String columnNameInELS ) {
        if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            Map< String, Object > queryMap = new HashMap<>();
            Map< String, String > fieldMap = new HashMap<>();
            fieldMap.put( columnNameInELS, filter.getValue() );
            queryMap.put( MATCH, fieldMap );
            mustNot.add( queryMap );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            Map< String, Object > queryMap = new HashMap<>();
            Map< String, String > fieldMap = new HashMap<>();
            fieldMap.put( columnNameInELS, ConstantsString.ASTERISK + filter.getValue() + ConstantsString.ASTERISK );
            queryMap.put( WILDCARD, fieldMap );
            mustNot.add( queryMap );
        } else {
            if ( filter.getCondition().equalsIgnoreCase( ConstantsOperators.AND.getName() ) ) {
                Map< String, Object > queryMap = new HashMap<>();
                Map< String, String > fieldMap = new HashMap<>();
                extractQuery( columnNameInELS, filter, queryMap, fieldMap );
                must.add( queryMap );
            } else if ( filter.getCondition().equalsIgnoreCase( ConstantsOperators.OR.getName() ) ) {
                Map< String, Object > queryMap = new HashMap<>();
                Map< String, String > fieldMap = new HashMap<>();
                extractQuery( columnNameInELS, filter, queryMap, fieldMap );
                should.add( queryMap );
            }
        }
    }

    /**
     * Prepare filter query for date.
     *
     * @param must
     *         the must
     * @param should
     *         the should
     * @param filter
     *         the filter
     * @param columnNameInELS
     *         the column name in ELS
     */
    private void prepareFilterQueryForDate( List< Object > must, List< Object > should, Filter filter, String columnNameInELS ) {
        Date date = prepareDate( filter );
        if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            prepareDateFilterQuery2( should, columnNameInELS, date, "lte", "gte" );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            prepareDateFilterQuery2( should, columnNameInELS, date, "gte", "lt" );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            prepareDateFieldQuery( must, columnNameInELS, date, "gt" );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            prepareDateFieldQuery( must, columnNameInELS, date, "gte" );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            prepareDateFieldQuery( must, columnNameInELS, date, "lt" );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            prepareDateFieldQuery( must, columnNameInELS, DateUtils.getTomorrowDate( date ), "lte" );
        }
    }

    /**
     * Prepare date filter query 2.
     *
     * @param should
     *         the should
     * @param columnNameInELS
     *         the column name in ELS
     * @param date
     *         the date
     * @param op
     *         the op
     * @param op2
     *         the op 2
     */
    private void prepareDateFilterQuery2( List< Object > should, String columnNameInELS, Date date, String op, String op2 ) {
        Map< String, Object > queryMap = new HashMap<>();
        Map< String, Object > queryMap2 = new HashMap<>();
        Map< String, Object > fieldQueryMap = new HashMap<>();
        Map< String, Object > fieldQueryMap2 = new HashMap<>();
        Map< String, String > operatorMap = new HashMap<>();
        Map< String, String > operatorMap2 = new HashMap<>();
        operatorMap.put( op, JsonUtils.toJson( DateUtils.getTomorrowDate( date ) ) );
        operatorMap2.put( op2, JsonUtils.toJson( date ) );
        fieldQueryMap.put( columnNameInELS, operatorMap );
        fieldQueryMap2.put( columnNameInELS, operatorMap2 );
        queryMap.put( "range", fieldQueryMap );
        queryMap2.put( "range", fieldQueryMap2 );
        should.add( queryMap );
        should.add( queryMap2 );
    }

    /**
     * Prepare date field query.
     *
     * @param must
     *         the must
     * @param columnNameInELS
     *         the column name in ELS
     * @param date
     *         the date
     * @param operator
     *         the operator
     */
    private void prepareDateFieldQuery( List< Object > must, String columnNameInELS, Date date, String operator ) {
        Map< String, Object > queryMap = new HashMap<>();
        Map< String, Object > fieldQueryMap = new HashMap<>();
        Map< String, String > operatorMap = new HashMap<>();
        operatorMap.put( operator, JsonUtils.toJson( date ) );
        fieldQueryMap.put( columnNameInELS, operatorMap );
        queryMap.put( "range", fieldQueryMap );
        must.add( queryMap );
    }

    /**
     * Prepare date.
     *
     * @param filter
     *         the filter
     *
     * @return the date
     */
    private Date prepareDate( Filter filter ) {
        return switch ( filter.getValue() ) {
            case ConstantsDateFilters.TODAY -> DateUtils.getCurrentDateWithoutTime();
            case ConstantsDateFilters.YESTERDAY -> DateUtils.getYesterdayDateWithoutTime();
            case ConstantsDateFilters.LAST_WEEK -> DateUtils.getLastWeekStartDate();
            case ConstantsDateFilters.THIS_WEEK -> DateUtils.getThisWeekStartDate();
            case ConstantsDateFilters.LAST_MONTH -> DateUtils.getLastMonthStartDate();
            case ConstantsDateFilters.THIS_MONTH -> DateUtils.getThisMonthStartDate();
            case ConstantsDateFilters.THIS_YEAR -> DateUtils.getThisYearStartDate();
            case ConstantsDateFilters.LAST_YEAR -> DateUtils.getLastYearStartDate();
            default -> DateUtils.fromString( filter.getValue(), ConstantsDate.DATE_ONLY_FORMAT );
        };
    }

    /**
     * Extract query.
     *
     * @param columnNameInELS
     *         the column name in els
     * @param filter
     *         the filter
     * @param map
     *         the map
     * @param mapQ
     *         the map q
     */
    private void extractQuery( String columnNameInELS, Filter filter, Map< String, Object > map, Map< String, String > mapQ ) {

        if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            mapQ.put( columnNameInELS, filter.getValue() );
            map.put( MATCH, mapQ );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            mapQ.put( columnNameInELS, ConstantsString.ASTERISK + filter.getValue() + ConstantsString.ASTERISK );
            map.put( WILDCARD, mapQ );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            mapQ.put( columnNameInELS, filter.getValue() + ConstantsString.ASTERISK );
            map.put( WILDCARD, mapQ );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            mapQ.put( columnNameInELS, ConstantsString.ASTERISK + filter.getValue() );
            map.put( WILDCARD, mapQ );
        }
    }

    /**
     * Gets the sorting.
     *
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the sorting
     */
    private List< Object > getSorting( FiltersDTO filtersDTO ) {
        List< Object > sortList = new ArrayList<>();
        FilterColumn sortColumn = null;
        FilterColumn modifiedColumn = null;
        for ( FilterColumn filterColumn : filtersDTO.getColumns() ) {
            if ( null != filterColumn.getDir() ) {
                sortColumn = filterColumn;
            } else if ( filterColumn.getName().equals( MODIFIED_ON ) ) {
                modifiedColumn = filterColumn;
            }
        }

        if ( null != sortColumn ) {
            sortList.add( prepareSortColumnQuery( sortColumn ) );
        } else {
            modifiedColumn.setDir( "desc" );
            sortList.add( prepareSortColumnQuery( modifiedColumn ) );
        }
        return sortList;
    }

    /**
     * Prepare sort column query.
     *
     * @param sortColumn
     *         the sort column
     *
     * @return the map
     */
    private Map< String, Object > prepareSortColumnQuery( FilterColumn sortColumn ) {
        Map< String, Object > sortQueryMap = new HashMap<>();
        Map< String, String > sortOrderMap = new HashMap<>();
        sortOrderMap.put( "order", sortColumn.getDir() );
        switch ( sortColumn.getName() ) {
            case "name" -> sortQueryMap.put( "name.keyword", sortOrderMap );
            case "description" -> sortQueryMap.put( "description.keyword", sortOrderMap );
            case "type" -> sortQueryMap.put( "type.keyword", sortOrderMap );
            case "size" -> sortQueryMap.put( "size.keyword", sortOrderMap );
            case CREATED_ON -> sortQueryMap.put( CREATED_ON, sortOrderMap );
            case MODIFIED_ON -> sortQueryMap.put( MODIFIED_ON, sortOrderMap );
            case "createdBy.userUid" -> sortQueryMap.put( "createdBy.userUid.keyword", sortOrderMap );
            case "modifiedBy.userUid" -> sortQueryMap.put( "modifiedBy.userUid.keyword", sortOrderMap );
        }
        return sortQueryMap;
    }

    /**
     * Gets the count.
     *
     * @param url
     *         the url
     *
     * @return the count
     */
    private Long getCount( String url ) {
        try {
            CloseableHttpResponse closeableHttpResponse = SuSClient.getExternalRequest( url, getHeadersForELS() );
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, UTF_ENCODING );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            return jsonNode.get( "count" ).asLong();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, SearchManagerImpl.class );
            throw new SusException( e );
        }
    }

    /**
     * Gets the headers for ELS.
     *
     * @return the headers for ELS
     */
    private Map< String, String > getHeadersForELS() {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        return requestHeaders;
    }

    /**
     * Prepare group sub query.
     *
     * @param groupEntities
     *         the group entities
     *
     * @return the string
     */
    private String prepareGroupSubQuery( List< UUID > groupEntities ) {
        if ( null == groupEntities || groupEntities.isEmpty() ) {
            return ")";
        }
        if ( groupEntities.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            return " OR groups:*" + groupEntities.get( ConstantsInteger.INTEGER_VALUE_ZERO ) + "*)";
        } else {
            StringBuilder sb = new StringBuilder();
            for ( UUID uuid : groupEntities ) {
                String newStr = " OR groups:*" + uuid + ConstantsString.ASTERISK;
                sb.append( newStr );
            }
            return sb + ")";
        }
    }

    /**
     * Prepare type sub query.
     *
     * @param typeIds
     *         the type ids
     *
     * @return the string
     */
    private String prepareTypeSubQuery( List< String > typeIds ) {
        if ( null == typeIds || typeIds.isEmpty() ) {
            return ConstantsString.EMPTY_STRING;
        }
        if ( typeIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            return " AND type:\"" + typeIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) + ConstantsString.DOUBLE_QUOTE_STRING;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append( "(" );
            for ( String typeName : typeIds ) {
                sb.append( ConstantsString.DOUBLE_QUOTE_STRING );
                sb.append( typeName );
                sb.append( ConstantsString.DOUBLE_QUOTE_STRING );
                sb.append( ConstantsString.SPACE );
            }
            sb.append( ")" );
            return " AND type:" + sb;
        }
    }

    /**
     * Prepare query.
     *
     * @param query
     *         the query
     * @param userId
     *         the user id
     * @param groupEntities
     *         the group entities
     *
     * @return the string
     */
    private String prepareQuery( String query, String userId, List< String > searchInIds, List< UUID > groupEntities ) {
        StringBuilder sb = new StringBuilder();
        sb.append( query );
        sb.append( IS_DELETE_QUERY );
        sb.append( HIDDEN_OBJECTS_QUERY );
        if ( !ConstantsID.SUPER_USER_ID.equals( userId ) ) {
            sb.append( USERS_QUERY );
            sb.append( userId + "*" );
            sb.append( prepareGroupSubQuery( groupEntities ) );
        }
        if ( !query.contains( "type" ) ) {
            sb.append( prepareTypeSubQuery( searchInIds ) );
        }

        return sb.toString();
    }

    /**
     * Gets the filtered columns query.
     *
     * @param filtersDTO
     *         the filters DTO
     * @param query
     *         the query
     * @param sortField
     *         the sort field
     * @param sortMethod
     *         the sort method
     *
     * @return the filtered columns query
     */
    private List< String > getFilteredFields( FiltersDTO filtersDTO, StringBuilder query, StringBuilder sortField,
            StringBuilder sortMethod ) {
        Set< String > fieldList = new HashSet<>();
        if ( filtersDTO.getColumns() != null ) {
            for ( FilterColumn column : filtersDTO.getColumns() ) {
                getSortField( sortField, sortMethod, column );
                if ( null != column.getFilters() ) {
                    prepareFieldList( query, fieldList, column );
                }
            }
        }
        return new ArrayList<>( fieldList );
    }

    /**
     * Prepare field list.
     *
     * @param query
     *         the query
     * @param fieldList
     *         the field list
     * @param column
     *         the column
     */
    private void prepareFieldList( StringBuilder query, Set< String > fieldList, FilterColumn column ) {
        for ( Filter filter : column.getFilters() ) {
            String columnName;
            if ( column.getName().contains( ConstantsString.DOT ) ) {
                columnName = column.getName().split( ConstantsSearch.REGEX_DOT )[ ConstantsInteger.INTEGER_VALUE_ZERO ];
            } else if ( column.getName().equalsIgnoreCase( ConstantsSearch.ID ) ) {
                columnName = ConstantsSearch.COMPOSED_ID;
            } else {
                columnName = column.getName();
            }
            if ( !columnName.equals( ConstantsDAO.IS_DELETE ) ) {
                fieldList.add( columnName );
            }
            prepareQuery( query, columnName, filter );
        }
    }

    /**
     * Gets the sort field.
     *
     * @param sortField
     *         the sort field
     * @param sortMethod
     *         the sort method
     * @param column
     *         the column
     */
    private static void getSortField( StringBuilder sortField, StringBuilder sortMethod, FilterColumn column ) {
        if ( null != column.getDir() ) {
            String columnName;
            if ( column.getName().contains( ConstantsString.DOT ) ) {
                columnName = column.getName().split( ConstantsSearch.REGEX_DOT )[ ConstantsInteger.INTEGER_VALUE_ZERO ];
            } else if ( column.getName().equalsIgnoreCase( ConstantsSearch.ID ) ) {
                columnName = ConstantsSearch.COMPOSED_ID;
            } else {
                columnName = column.getName();
            }
            sortField.append( columnName );
            sortMethod.append( column.getDir() );
        }
    }

    /**
     * Prepare query.
     *
     * @param query
     *         the query
     * @param columnName
     *         the column name
     * @param filter
     *         the filter
     */
    private void prepareQuery( StringBuilder query, String columnName, Filter filter ) {

        StringBuilder endDate = new StringBuilder();
        if ( columnName.equalsIgnoreCase( CREATED_ON ) || columnName.equalsIgnoreCase( MODIFIED_ON ) ) {

            handleDateFilters( filter, endDate );
        }
        String op = filter.getOperator();
        switch ( op ) {
            case ConstantsFilterOperators.EQUALS -> {
                if ( columnName.equalsIgnoreCase( CREATED_ON ) || columnName.equalsIgnoreCase( MODIFIED_ON ) ) {
                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + columnName + ConstantsString.COLON
                            + ConstantsString.LEFT_SQUARE_BRACKET + ConstantsString.DOUBLE_QUOTE_STRING
                            + getDaysFromFilter( filter.getValue(), -1 )// from previous date
                            + ConstantsString.DOUBLE_QUOTE_STRING + ConstantsString.SPACE + ConstantsSearch.TO + ConstantsString.SPACE
                            + ConstantsString.DOUBLE_QUOTE_STRING + getDateFromFilter( endDate.toString() ) // today
                            + ConstantsString.DOUBLE_QUOTE_STRING + ConstantsString.RIGHT_SQUARE_BRACKET );
                } else {
                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + columnName + ConstantsString.COLON
                            + ConstantsString.DOUBLE_QUOTE_STRING + filter.getValue() + ConstantsString.DOUBLE_QUOTE_STRING );
                }
            }
            case ConstantsFilterOperators.NOT_EQUALS -> {
                if ( columnName.equalsIgnoreCase( CREATED_ON ) || columnName.equalsIgnoreCase( MODIFIED_ON ) ) {
                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + ConstantsSearch.NOT
                            + ConstantsString.SPACE + columnName + ConstantsString.COLON + ConstantsString.LEFT_SQUARE_BRACKET
                            + ConstantsString.DOUBLE_QUOTE_STRING + getDaysFromFilter( filter.getValue(), -1 )
                            + ConstantsString.DOUBLE_QUOTE_STRING + ConstantsString.SPACE + ConstantsSearch.TO + ConstantsString.SPACE
                            + ConstantsString.DOUBLE_QUOTE_STRING + getDateFromFilter( endDate.toString() ) // today
                            + ConstantsString.DOUBLE_QUOTE_STRING + ConstantsString.RIGHT_SQUARE_BRACKET );
                } else {
                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + ConstantsSearch.NOT
                            + ConstantsString.SPACE + columnName + ConstantsString.COLON + ConstantsString.DOUBLE_QUOTE_STRING
                            + filter.getValue() + ConstantsString.DOUBLE_QUOTE_STRING );
                }
            }
            case ConstantsFilterOperators.BEGINS_WITH -> query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE
                    + columnName + ConstantsString.COLON + filter.getValue() + ConstantsString.ASTERISK );
            case ConstantsFilterOperators.ENDS_WITH -> query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE
                    + columnName + ConstantsString.COLON + ConstantsString.ASTERISK + filter.getValue() );
            case ConstantsFilterOperators.CONTAINS -> query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE
                    + columnName + ConstantsString.COLON + ConstantsString.ASTERISK + filter.getValue() + ConstantsString.ASTERISK );
            case ConstantsFilterOperators.NOT_CONTAINS -> query
                    .append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + ConstantsSearch.NOT
                            + ConstantsString.SPACE
                            + columnName + ConstantsString.COLON + ConstantsString.ASTERISK + filter.getValue()
                            + ConstantsString.ASTERISK );
            case ConstantsFilterOperators.AFTER -> {
                if ( columnName.equalsIgnoreCase( CREATED_ON ) || columnName.equalsIgnoreCase( MODIFIED_ON ) ) {
                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + ConstantsString.SPACE + columnName
                            + ConstantsString.COLON + ConstantsString.LEFT_SQUARE_BRACKET + ConstantsString.DOUBLE_QUOTE_STRING
                            + getDateFromFilter( endDate.toString() ) + ConstantsString.DOUBLE_QUOTE_STRING + ConstantsString.SPACE
                            + ConstantsSearch.TO + ConstantsString.SPACE + ConstantsString.ASTERISK
                            + ConstantsString.RIGHT_SQUARE_BRACKET );
                } else {
                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + columnName + ConstantsString.COLON
                            + ConstantsString.LEFT_SQUARE_BRACKET
                            + (
                            Integer.parseInt( filter.getValue().replaceAll( ConstantsSearch.REGEX_DASHES, ConstantsString.EMPTY_STRING ) )
                                    + ConstantsInteger.INTEGER_VALUE_ONE )
                            + ConstantsSearch.TO + ConstantsString.ASTERISK + ConstantsString.RIGHT_SQUARE_BRACKET );
                }
            }
            case ConstantsFilterOperators.AFTER_OR_EQUAL_TO -> {
                if ( columnName.equalsIgnoreCase( CREATED_ON ) || columnName.equalsIgnoreCase( MODIFIED_ON ) ) {
                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + ConstantsString.SPACE + columnName
                            + ConstantsString.COLON + ConstantsString.LEFT_SQUARE_BRACKET + ConstantsString.DOUBLE_QUOTE_STRING
                            + getDaysFromFilter( endDate.toString(), -1 ) + ConstantsString.DOUBLE_QUOTE_STRING + ConstantsString.SPACE
                            + ConstantsSearch.TO + ConstantsString.SPACE + ConstantsString.ASTERISK
                            + ConstantsString.RIGHT_SQUARE_BRACKET );
                } else {

                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + columnName + ConstantsString.COLON
                            + ConstantsString.LEFT_SQUARE_BRACKET
                            + filter.getValue().replaceAll( ConstantsSearch.REGEX_DASHES, ConstantsString.EMPTY_STRING )
                            + ConstantsSearch.TO
                            + ConstantsString.ASTERISK + ConstantsString.RIGHT_SQUARE_BRACKET );
                }
            }
            case ConstantsFilterOperators.BEFORE -> {
                if ( columnName.equalsIgnoreCase( CREATED_ON ) || columnName.equalsIgnoreCase( MODIFIED_ON ) ) {
                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + columnName + ConstantsString.COLON
                            + ConstantsString.LEFT_SQUARE_BRACKET + ConstantsString.ASTERISK + ConstantsString.SPACE + ConstantsSearch.TO
                            + ConstantsString.SPACE + ConstantsString.DOUBLE_QUOTE_STRING + getDaysFromFilter( filter.getValue(), -1 )
                            + ConstantsString.DOUBLE_QUOTE_STRING + ConstantsString.RIGHT_SQUARE_BRACKET );
                } else {

                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + columnName + ConstantsString.COLON
                            + ConstantsString.LEFT_SQUARE_BRACKET + ConstantsString.ASTERISK + ConstantsSearch.TO
                            + (
                            Integer.parseInt( filter.getValue().replaceAll( ConstantsSearch.REGEX_DASHES, ConstantsString.EMPTY_STRING ) )
                                    - ConstantsInteger.INTEGER_VALUE_ONE )
                            + ConstantsString.RIGHT_SQUARE_BRACKET );
                }
            }
            case ConstantsFilterOperators.BEFORE_OR_EQUAL_TO -> {
                if ( columnName.equalsIgnoreCase( CREATED_ON ) || columnName.equalsIgnoreCase( MODIFIED_ON ) ) {
                    query.append( ConstantsString.SPACE + filter.getCondition() + ConstantsString.SPACE + columnName + ConstantsString.COLON
                            + ConstantsString.LEFT_SQUARE_BRACKET + ConstantsString.ASTERISK + ConstantsString.SPACE + ConstantsSearch.TO
                            + ConstantsString.SPACE + ConstantsString.DOUBLE_QUOTE_STRING + getDateFromFilter( filter.getValue() )
                            + ConstantsString.DOUBLE_QUOTE_STRING + ConstantsString.RIGHT_SQUARE_BRACKET );
                } else {
                    query.append( ConstantsString.SPACE ).append( filter.getCondition() ).append( ConstantsString.SPACE )
                            .append( columnName )
                            .append( ConstantsString.COLON ).append( ConstantsString.LEFT_SQUARE_BRACKET )
                            .append( ConstantsString.ASTERISK )
                            .append( ConstantsSearch.TO )
                            .append( filter.getValue().replaceAll( ConstantsSearch.REGEX_DASHES, ConstantsString.EMPTY_STRING ) )
                            .append( ConstantsString.RIGHT_SQUARE_BRACKET );
                }
            }
            default -> query.append( ConstantsString.EMPTY_STRING );
        }
    }

    /**
     * @param filter
     *         Search Filter needed to be applied
     * @param endDate
     *         For end of Date range in filter
     */
    private void handleDateFilters( Filter filter, StringBuilder endDate ) {
        LocalDateTime dateTime = LocalDateTime.now();
        String fl = filter.getValue();
        switch ( fl ) {
            case ConstantsDateFilters.TODAY -> {
                filter.setValue( LocalDateTime.of( dateTime.toLocalDate(), LocalTime.MIN ).toString() );
                endDate.append( dateTime );
            }
            case ConstantsDateFilters.YESTERDAY -> {
                filter.setValue(
                        LocalDateTime.of( dateTime.toLocalDate().minusDays( ConstantsInteger.INTEGER_VALUE_ONE ), LocalTime.MIN )
                                .toString() );
                endDate.append( LocalDateTime.of( dateTime.toLocalDate().minusDays( ConstantsInteger.INTEGER_VALUE_ONE ), LocalTime.MAX ) );
            }
            case ConstantsDateFilters.LAST_WEEK -> {
                filter.setValue( LocalDateTime.of(
                        dateTime.toLocalDate().minusWeeks( ConstantsInteger.INTEGER_VALUE_ONE )
                                .minusDays( dateTime.getDayOfWeek().getValue() ),
                        LocalTime.MIN ).toString() );
                endDate.append( LocalDateTime.of( dateTime.toLocalDate().minusWeeks( ConstantsInteger.INTEGER_VALUE_ONE )
                        .plusDays( ConstantsInteger.INTEGER_VALUE_SEVEN - dateTime.getDayOfWeek().getValue() ), LocalTime.MAX ) );
            }
            case ConstantsDateFilters.THIS_WEEK -> {
                filter.setValue(
                        LocalDateTime.of( dateTime.toLocalDate().minusDays( dateTime.getDayOfWeek().getValue() ), LocalTime.MIN )
                                .toString() );
                endDate.append( dateTime );
            }
            case ConstantsDateFilters.LAST_MONTH -> {
                filter.setValue( LocalDateTime.of( dateTime.toLocalDate().minusMonths( ConstantsInteger.INTEGER_VALUE_ONE )
                        .withDayOfMonth( ConstantsInteger.INTEGER_VALUE_ONE ), LocalTime.MIN ).toString() );
                endDate.append( LocalDateTime.of( dateTime.toLocalDate().withDayOfMonth( ConstantsInteger.INTEGER_VALUE_ONE )
                        .minusDays( ConstantsInteger.INTEGER_VALUE_ONE ), LocalTime.MAX ) );
            }
            case ConstantsDateFilters.THIS_MONTH -> {
                filter.setValue(
                        LocalDateTime.of( dateTime.toLocalDate().withDayOfMonth( ConstantsInteger.INTEGER_VALUE_ONE ), LocalTime.MIN )
                                .toString() );
                endDate.append( dateTime );
            }
            case ConstantsDateFilters.LAST_YEAR -> {
                filter.setValue( LocalDateTime.of(
                        dateTime.toLocalDate().minusYears( ConstantsInteger.INTEGER_VALUE_ONE )
                                .withDayOfMonth( ConstantsInteger.INTEGER_VALUE_ONE ).withMonth( ConstantsInteger.INTEGER_VALUE_ONE ),
                        LocalTime.MIN ).toString() );

                endDate.append( LocalDateTime.of(
                        dateTime.toLocalDate().withDayOfMonth( ConstantsInteger.INTEGER_VALUE_ONE )
                                .withMonth( ConstantsInteger.INTEGER_VALUE_ONE ).minusDays( ConstantsInteger.INTEGER_VALUE_ONE ),
                        LocalTime.MIN ) );
            }
            case ConstantsDateFilters.THIS_YEAR -> {
                filter.setValue( LocalDateTime.of( dateTime.toLocalDate().withDayOfMonth( ConstantsInteger.INTEGER_VALUE_ONE )
                        .withMonth( ConstantsInteger.INTEGER_VALUE_ONE ), LocalTime.MIN ).toString() );

                endDate.append( dateTime );
            }
            default -> {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime dateTimeCalendar = LocalDateTime.parse( filter.getValue(), formatter );
                filter.setValue( LocalDateTime.of( dateTimeCalendar.toLocalDate(), LocalTime.MIN ).toString() );
                endDate.append( LocalDateTime.of( dateTimeCalendar.toLocalDate(), LocalTime.MAX ) );
            }
        }

    }

    /**
     * Formats Date+Time+TiemZone String LocalDateTime and returns String Containing only date+time
     */
    private String getDateFromFilter( String dateString ) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dateTime = LocalDateTime.parse( dateString, formatter );
        return dateTime.toString().replaceAll( ConstantsSearch.REGEX_DASHES, ConstantsString.EMPTY_STRING ).substring( 0, 9 );

    }

    /**
     * Formats Date+Time+TiemZone String LocalDateTime Adds/Subtracts number of days specified and returns String Containing only date+time
     */
    private String getDaysFromFilter( String dateString, int numberOfDays ) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dateTime = LocalDateTime.parse( dateString, formatter );
        return dateTime.plusDays( numberOfDays ).toString().replaceAll( ConstantsSearch.REGEX_DASHES, ConstantsString.EMPTY_STRING )
                .substring( 0, 9 );

    }

    /**
     * Get entity Fields.
     *
     * @return the entity fields
     */
    private String[] getEntityFields() {
        List< String > allFieldNames = getLuceneIndexedFields( SuSEntity.class );
        return allFieldNames.toArray( new String[ 0 ] );
    }

    /**
     * Gets the lucene indexed fields.
     *
     * @param clazz
     *         the clazz
     *
     * @return the lucene indexed fields
     */
    private List< String > getLuceneIndexedFields( Class< ? > clazz ) {
        List< String > fields = new ArrayList<>();
        Field[] attributes = clazz.getDeclaredFields();
        for ( Field field : attributes ) {
            // NOTE: noman FIX
            // org.hibernate.search.annotations.Field annotation = field.getAnnotation( org.hibernate.search.annotations.Field.class );
            Object annotation = null;
            if ( annotation != null && !field.getName().equals( ConstantsDAO.IS_DELETE ) && !field.getName()
                    .equals( ConstantsDAO.IS_HIDDEN ) && !field.getName().equals( "users" ) && !field.getName().equals( "groups" ) ) {
                fields.add( field.getName() );
            }
        }
        return fields;
    }

    /**
     * Checks if is restricted.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     */
    private void isRestricted( EntityManager entityManager, UUID userId ) {
        UserDTO userDTO = userCommonManager.getUserById( entityManager, userId );
        if ( userDTO != null && userDTO.getRestricted().equals( RESTRICTED_USER ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_RESTRICTED_TO_SYSTEM.getKey(), userDTO.getName() ) );
        }
    }

    /**
     * Gets the search dao.
     *
     * @return the search dao
     */
    public SearchDAO getSearchDao() {
        return searchDao;
    }

    /**
     * Sets the search dao.
     *
     * @param searchDao
     *         the new search dao
     */
    public void setSearchDao( SearchDAO searchDao ) {
        this.searchDao = searchDao;
    }

    /**
     * Sets the data manager.
     *
     * @param dataObjectManager
     *         the new data manager
     */
    public void setDataObjectManager( DataObjectManager dataObjectManager ) {
        this.dataObjectManager = dataObjectManager;
    }

    /**
     * Gets the widgets manager.
     *
     * @return the widgets manager
     */
    public WidgetSettingsManager getWidgetsManager() {
        return widgetsManager;
    }

    /**
     * Sets the widgets manager.
     *
     * @param widgetsManager
     *         the new widgets manager
     */
    public void setWidgetsManager( WidgetSettingsManager widgetsManager ) {
        this.widgetsManager = widgetsManager;
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
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Sets user common manager.
     *
     * @param userCommonManager
     *         the user common manager
     */
    public void setUserCommonManager( UserCommonManager userCommonManager ) {
        this.userCommonManager = userCommonManager;
    }

}