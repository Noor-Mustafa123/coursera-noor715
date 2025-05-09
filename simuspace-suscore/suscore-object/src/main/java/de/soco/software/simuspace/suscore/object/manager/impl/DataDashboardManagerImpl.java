package de.soco.software.simuspace.suscore.object.manager.impl;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.BAR_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.BIND_FROM_URLS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.BIND_TO_URLS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.DATA_SOURCE_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.LINE_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.MATERIAL_INSPECTOR_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.METAL_BASE_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.PYTHON_MODES;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.RADAR_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.SCATTER_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.SQL_CONSTANTS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.TEXT_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.URL_PARAMS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.CSVDelimiter;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.DashboardDataSourceOptions;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.DataSourceDatabaseOptions;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.LINE_WIDGET_OPTIONS;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.METAL_BASE_ACTION;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.OTHER_WIDGET_SOURCE;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.PST_ACTIONS;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.ScriptStatusOption;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.TEXT_WIDGET_INPUT_METHOD;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WIDGET_RELATION;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WIDGET_SCRIPT_OPTION;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WIDGET_SOURCE_TYPE;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WIDGET_TYPE;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WidgetPythonOutputOptions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.ButtonFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.BindTo;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryResponse;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.common.util.Tree;
import de.soco.software.simuspace.suscore.common.util.TreeNode;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectCacheDTO;
import de.soco.software.simuspace.suscore.data.entity.CSVDataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.DashboardWidgetEntity;
import de.soco.software.simuspace.suscore.data.entity.DataDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.DataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.DatabaseDataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.ExcelDataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.JsonDataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSObjectDataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.CSVDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.CustomAttributeDTO;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;
import de.soco.software.simuspace.suscore.data.model.DataDashboardDTO;
import de.soco.software.simuspace.suscore.data.model.DataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.DatabaseDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.ExcelDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.GroupWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.JsonDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleXOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleXYOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleYOptions;
import de.soco.software.simuspace.suscore.data.model.MixChartCurveOptions;
import de.soco.software.simuspace.suscore.data.model.RadarWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.SsfeSelectionDTO;
import de.soco.software.simuspace.suscore.data.model.SuSObjectDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.TableWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.WidgetMaterialInspectorSource;
import de.soco.software.simuspace.suscore.data.model.WidgetMetalBaseSource;
import de.soco.software.simuspace.suscore.data.model.WidgetOtherSource;
import de.soco.software.simuspace.suscore.data.model.WidgetPstSource;
import de.soco.software.simuspace.suscore.data.model.WidgetPythonSource;
import de.soco.software.simuspace.suscore.data.model.WidgetQueryBuilderSource;
import de.soco.software.simuspace.suscore.data.model.WidgetVmclSource;
import de.soco.software.simuspace.suscore.data.utility.DataUtils;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.object.manager.DataDashboardManager;
import de.soco.software.simuspace.suscore.object.utility.DashboardCSVUtil;
import de.soco.software.simuspace.suscore.object.utility.DashboardCacheUtil;
import de.soco.software.simuspace.suscore.object.utility.DashboardConfigUtil;
import de.soco.software.simuspace.suscore.object.utility.DashboardExcelUtil;
import de.soco.software.simuspace.suscore.object.utility.DashboardJsonUtil;
import de.soco.software.simuspace.suscore.object.utility.DashboardMaterialInspectorUtil;
import de.soco.software.simuspace.suscore.object.utility.DashboardMetalBaseUtil;
import de.soco.software.simuspace.suscore.object.utility.DashboardPSTUtil;
import de.soco.software.simuspace.suscore.object.utility.DashboardPythonUtil;
import de.soco.software.simuspace.suscore.object.utility.DashboardQueryBuilder;
import de.soco.software.simuspace.suscore.object.utility.DashboardSuSObjectUtil;
import de.soco.software.simuspace.suscore.object.utility.DashboardVMclUtil;
import de.soco.software.simuspace.suscore.object.utility.DataDashboardUtil;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;

/**
 * The type Data dashboard manager.
 */
@Setter
@Log4j2
public class DataDashboardManagerImpl implements DataDashboardManager {

    /**
     * The Empty string.
     */
    private String EMPTY_STRING = "";

    /**
     * The User common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The Config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * The Sus dao.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The Permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The Selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The constant scheduler.
     */
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 1 );

    /**
     * The constant CACHE_FILE_NAME.
     */
    private static final String CACHE_FILE_NAME = "cache.json";

    /**
     * Gets dashboard permission.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return the dashboard permission
     */
    @Override
    public boolean getDashboardPermission( String token, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
        } finally {
            entityManager.close();
        }
        return true;
    }

    /**
     * Test connection boolean.
     *
     * @param token
     *         the token
     * @param dataSourceDTO
     *         the data source dto
     *
     * @return the boolean
     */
    @Override
    public boolean testConnection( String token, DatabaseDataSourceDTO dataSourceDTO ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSourceDTO );
            jdbcTemplate.execute( SQL_CONSTANTS.SELECT_1 );
            return true;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            return false;
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets database preview.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param dataSourceJson
     *         the data source json
     *
     * @return the database preview
     */
    @Override
    public Object getDataSourcePreview( String objectId, String token, String dataSourceJson ) {
        DataSourceDTO dataSourceDTO = DataDashboardUtil.getDataSourceDTOFromJson( dataSourceJson );
        var type = DashboardDataSourceOptions.getById( dataSourceDTO.getSourceType() );
        return switch ( type ) {
            case DATABASE -> DataDashboardUtil.getDatabasePreview( ( DatabaseDataSourceDTO ) dataSourceDTO );
            case CSV -> getCSVPreview( ( CSVDataSourceDTO ) dataSourceDTO );
            case EXCEL -> getExcelPreview( ( ExcelDataSourceDTO ) dataSourceDTO );
            case JSON -> getJsonPreview( ( JsonDataSourceDTO ) dataSourceDTO );
            case SUS_OBJECT -> getSuSObjectPreview( objectId, token, ( SuSObjectDataSourceDTO ) dataSourceDTO );
        };
    }

    /**
     * Gets su s object preview.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param dataSourceDTO
     *         the data source dto
     *
     * @return the su s object preview
     */
    private Object getSuSObjectPreview( String objectId, String token, SuSObjectDataSourceDTO dataSourceDTO ) {
        String userLang = TokenizedLicenseUtil.getUserLanguage( token );
        var dataSourceCacheDirectory = DashboardCacheUtil.getDataSourceDirectoryInCache(
                new VersionPrimaryKey( UUID.fromString( objectId ), dataSourceDTO.getVersionId() ),
                UUID.fromString( dataSourceDTO.getId() ) );
        Path cacheFilePath = DashboardSuSObjectUtil.createCacheDirectoryForCacheWithLanguagePostFix( dataSourceCacheDirectory,
                dataSourceDTO.getProjectId(), userLang );
        return DashboardJsonUtil.getJsonPreview( Path.of( cacheFilePath.toAbsolutePath().toString(), CACHE_FILE_NAME ) );
    }

    /**
     * Gets excel preview.
     *
     * @param dataSourceDTO
     *         the data source dto
     *
     * @return the excel preview
     */
    private Object getExcelPreview( ExcelDataSourceDTO dataSourceDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Path excelPath = Path.of( getPathFromSelectionId( entityManager, dataSourceDTO.getFileSelection() ) );
            DashboardExcelUtil.validateUserExcel( excelPath );
            return DashboardExcelUtil.getPreview( excelPath );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets csv preview.
     *
     * @param dataSourceDTO
     *         the data source dto
     *
     * @return the csv preview
     */
    private Object getCSVPreview( CSVDataSourceDTO dataSourceDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Path csvPath = Path.of( getPathFromSelectionId( entityManager, dataSourceDTO.getFileSelection() ) );
            DashboardCSVUtil.validateUserCSV( csvPath );
            return DashboardCSVUtil.getCSVPreview( csvPath );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets json preview.
     *
     * @param dataSourceDTO
     *         the data source dto
     *
     * @return the json preview
     */
    private Object getJsonPreview( JsonDataSourceDTO dataSourceDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Path jsonPath = Path.of( getPathFromSelectionId( entityManager, dataSourceDTO.getFileSelection() ) );
            DashboardJsonUtil.validateUserJson( jsonPath );
            return DashboardJsonUtil.getJsonPreview( jsonPath );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare data dashboard dto data dashboard dto.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param dashboardEntity
     *         the dashboard entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the data dashboard dto
     */
    @Override
    public DataDashboardDTO prepareDataDashboardDTO( EntityManager entityManager, UUID id, DataDashboardEntity dashboardEntity,
            boolean setCustomAttributes ) {
        DataDashboardDTO dashboardDTO = new DataDashboardDTO();
        dashboardDTO.setName( dashboardEntity.getName() );
        dashboardDTO.setId( dashboardEntity.getComposedId().getId() );
        dashboardDTO.setCreatedOn( dashboardEntity.getCreatedOn() );
        dashboardDTO.setModifiedOn( dashboardEntity.getModifiedOn() );
        dashboardDTO.setParentId( id );
        dashboardDTO.setTypeId( dashboardEntity.getTypeId() );
        dashboardDTO.setAutoDeleted( dashboardEntity.isAutoDelete() );
        dashboardDTO.setDataSources( prepareDataSourcesDTOList( entityManager, dashboardEntity.getDataSources() ) );
        if ( null != dashboardEntity.getCreatedBy() ) {
            dashboardDTO.setCreatedBy( userCommonManager.getUserById( entityManager, dashboardEntity.getCreatedBy().getId() ) );
        }
        if ( null != dashboardEntity.getModifiedBy() ) {
            dashboardDTO.setModifiedBy( userCommonManager.getUserById( entityManager, dashboardEntity.getModifiedBy().getId() ) );
        }

        dashboardDTO.setDescription( dashboardEntity.getDescription() );
        if ( setCustomAttributes ) {
            dashboardDTO.setCustomAttributes(
                    CustomAttributeDTO.prepareCustomAttributesMapFromSet( dashboardEntity.getCustomAttributes() ) );
        }
        if ( dashboardEntity.getTypeId() != null ) {
            dashboardDTO.setTypeId( dashboardEntity.getTypeId() );
            dashboardDTO.setLifeCycleStatus(
                    configManager.getStatusByIdandObjectType( dashboardEntity.getTypeId(), dashboardEntity.getLifeCycleStatus(),
                            dashboardEntity.getConfig() ) );
        }
        if ( null != dashboardEntity.getConfig() ) {
            dashboardDTO.setType( configManager.getObjectTypeByIdAndConfigName( String.valueOf( dashboardEntity.getTypeId() ),
                    dashboardEntity.getConfig() ).getName() );
        }

        return dashboardDTO;
    }

    /**
     * Prepare data sources dto list list.
     *
     * @param entityManager
     *         the entity manager
     * @param dataSourceEntities
     *         the data source entities
     *
     * @return the list
     */
    private List< DataSourceDTO > prepareDataSourcesDTOList( EntityManager entityManager, List< DataSourceEntity > dataSourceEntities ) {
        List< DataSourceDTO > dataSourceDTOList = new ArrayList<>();
        dataSourceEntities.stream().filter( entity -> !entity.isDelete() )
                .forEach( entity -> dataSourceDTOList.add( prepareDataSourceDTOFromEntity( entityManager, entity ) ) );
        return dataSourceDTOList;
    }

    /**
     * Prepare data source dto from entity data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the data source dto
     */
    private DataSourceDTO prepareDataSourceDTOFromEntity( EntityManager entityManager, DataSourceEntity entity ) {
        var sourceType = DashboardDataSourceOptions.getById( entity.getSourceType() );
        return switch ( sourceType ) {
            case DATABASE -> prepareDatabaseDataSourceDTOFromDatabaseDataSourceEntity( entityManager, ( DatabaseDataSourceEntity ) entity );
            case EXCEL -> prepareExcelDataSourceDTOFromExcelDataSourceEntity( entityManager, ( ExcelDataSourceEntity ) entity );
            case CSV -> prepareCSVDataSourceDTOFromCSVDataSourceEntity( entityManager, ( CSVDataSourceEntity ) entity );
            case JSON -> prepareJsonDataSourceDTOFromJsonDataSourceEntity( entityManager, ( JsonDataSourceEntity ) entity );
            case SUS_OBJECT ->
                    prepareSuSObjectDataSourceDTOFromSuSObjectDataSourceEntity( entityManager, ( SuSObjectDataSourceEntity ) entity );
        };
    }

    /**
     * Prepare su s object data source dto from su s object data source entity data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param suSObjectDataSource
     *         the json data source entity
     *
     * @return the data source dto
     */
    private DataSourceDTO prepareSuSObjectDataSourceDTOFromSuSObjectDataSourceEntity( EntityManager entityManager,
            SuSObjectDataSourceEntity suSObjectDataSource ) {
        SuSObjectDataSourceDTO dataSourceDTO = new SuSObjectDataSourceDTO();
        dataSourceDTO.setSourceType( suSObjectDataSource.getSourceType() );
        dataSourceDTO.setSourceName( suSObjectDataSource.getName() );
        if ( suSObjectDataSource.getSelectionEntity() != null ) {
            dataSourceDTO.setProjectId(
                    getSelectedIdFromSelectionId( entityManager, suSObjectDataSource.getSelectionEntity().getId().toString() ) );
            dataSourceDTO.setProjectSelection( suSObjectDataSource.getSelectionEntity().getId().toString() );
        }
        dataSourceDTO.setId( suSObjectDataSource.getId().toString() );
        dataSourceDTO.setVersionId( suSObjectDataSource.getVersionId() );
        dataSourceDTO.setCreatedBy( userCommonManager.getUserById( entityManager, suSObjectDataSource.getCreatedBy().getId() ) );
        dataSourceDTO.setModifiedBy( userCommonManager.getUserById( entityManager, suSObjectDataSource.getModifiedBy().getId() ) );
        dataSourceDTO.setCreatedOn( suSObjectDataSource.getCreatedOn() );
        dataSourceDTO.setModifiedOn( suSObjectDataSource.getModifiedOn() );
        dataSourceDTO.setUpdateInterval( suSObjectDataSource.getUpdateInterval() );
        dataSourceDTO.setTraversalDepth( suSObjectDataSource.getTraversalDepth() );
        dataSourceDTO.setCacheUpdatedAt( suSObjectDataSource.getCacheUpdatedAt() );
        return dataSourceDTO;
    }

    /**
     * Prepare python payload from data source entity data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the data source dto
     */
    private DataSourceDTO preparePythonPayloadFromDataSourceEntity( EntityManager entityManager, DataSourceEntity entity ) {
        var sourceType = DashboardDataSourceOptions.getById( entity.getSourceType() );
        return switch ( sourceType ) {
            case DATABASE -> preparePythonPayloadFromDatabaseDataSourceEntity( ( DatabaseDataSourceEntity ) entity );
            case EXCEL -> preparePythonPayloadFromExcelDataSourceEntity( entityManager, ( ExcelDataSourceEntity ) entity );
            case CSV -> preparePythonPayloadFromCSVDataSourceEntity( entityManager, ( CSVDataSourceEntity ) entity );
            case JSON -> preparePythonPayloadFromJsonDataSourceEntity( entityManager, ( JsonDataSourceEntity ) entity );
            case SUS_OBJECT -> preparePythonPayloadFromSuSObjectDataSourceEntity( entityManager, ( SuSObjectDataSourceEntity ) entity );
        };
    }

    /**
     * Prepare python payload from excel data source entity excel data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param excelDataSourceEntity
     *         the excel data source entity
     *
     * @return the excel data source dto
     */
    private ExcelDataSourceDTO preparePythonPayloadFromExcelDataSourceEntity( EntityManager entityManager,
            ExcelDataSourceEntity excelDataSourceEntity ) {
        ExcelDataSourceDTO dataSourceDTO = new ExcelDataSourceDTO();
        dataSourceDTO.setSourceType( excelDataSourceEntity.getSourceType() );
        dataSourceDTO.setSourceName( excelDataSourceEntity.getName() );
        if ( excelDataSourceEntity.getSelectionEntity() != null ) {
            dataSourceDTO.setFilePath(
                    getPathFromSelectionId( entityManager, excelDataSourceEntity.getSelectionEntity().getId().toString() ) );
            dataSourceDTO.setFileSelection( excelDataSourceEntity.getSelectionEntity().getId().toString() );
        }
        return dataSourceDTO;
    }

    /**
     * Prepare python payload from csv data source entity csv data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param csvDataSource
     *         the csv data source
     *
     * @return the csv data source dto
     */
    private CSVDataSourceDTO preparePythonPayloadFromCSVDataSourceEntity( EntityManager entityManager, CSVDataSourceEntity csvDataSource ) {
        CSVDataSourceDTO dataSourceDTO = new CSVDataSourceDTO();
        dataSourceDTO.setSourceType( csvDataSource.getSourceType() );
        dataSourceDTO.setSourceName( csvDataSource.getName() );
        if ( csvDataSource.getSelectionEntity() != null ) {
            dataSourceDTO.setFilePath( getPathFromSelectionId( entityManager, csvDataSource.getSelectionEntity().getId().toString() ) );
            dataSourceDTO.setFileSelection( csvDataSource.getSelectionEntity().getId().toString() );
        }
        dataSourceDTO.setDelimiter( csvDataSource.getDelimiter() == null ? CSVDelimiter.COMMA.getId() : csvDataSource.getDelimiter() );
        return dataSourceDTO;
    }

    /**
     * Prepare python payload from json data source entity json data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param jsonDataSource
     *         the json data source
     *
     * @return the json data source dto
     */
    private JsonDataSourceDTO preparePythonPayloadFromJsonDataSourceEntity( EntityManager entityManager,
            JsonDataSourceEntity jsonDataSource ) {
        JsonDataSourceDTO dataSourceDTO = new JsonDataSourceDTO();
        dataSourceDTO.setSourceType( jsonDataSource.getSourceType() );
        dataSourceDTO.setSourceName( jsonDataSource.getName() );
        if ( jsonDataSource.getSelectionEntity() != null ) {
            dataSourceDTO.setFilePath( getPathFromSelectionId( entityManager, jsonDataSource.getSelectionEntity().getId().toString() ) );
            dataSourceDTO.setFileSelection( jsonDataSource.getSelectionEntity().getId().toString() );
        }
        return dataSourceDTO;
    }

    /**
     * Prepare python payload from su s object data source entity json data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param suSObjectDataSource
     *         the su s object data source
     *
     * @return the json data source dto
     */
    private JsonDataSourceDTO preparePythonPayloadFromSuSObjectDataSourceEntity( EntityManager entityManager,
            SuSObjectDataSourceEntity suSObjectDataSource ) {
        JsonDataSourceDTO dataSourceDTO = new JsonDataSourceDTO();
        dataSourceDTO.setSourceType( suSObjectDataSource.getSourceType() );
        dataSourceDTO.setSourceName( suSObjectDataSource.getName() );
        if ( suSObjectDataSource.getSelectionEntity() != null ) {
            dataSourceDTO.setFilePath(
                    getPathFromSelectionId( entityManager, suSObjectDataSource.getSelectionEntity().getId().toString() ) );
            dataSourceDTO.setFileSelection( suSObjectDataSource.getSelectionEntity().getId().toString() );
        }
        return dataSourceDTO;
    }

    /**
     * Gets path from selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     *
     * @return the path from selection id
     */
    private String getPathFromSelectionId( EntityManager entityManager, String selectionId ) {
        if ( selectionId == null ) {
            return null;
        }
        List< SsfeSelectionDTO > selectedList = selectionManager.getSelectedFilesFromSsfsSelection( entityManager, selectionId );
        if ( CollectionUtils.isEmpty( selectedList ) ) {
            return null;
        }
        var selectedFile = selectedList.get( ConstantsInteger.INTEGER_VALUE_ZERO );
        return selectedFile.getFullPath();
    }

    /**
     * Gets selected id from selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     *
     * @return the selected id from selection id
     */
    private String getSelectedIdFromSelectionId( EntityManager entityManager, String selectionId ) {
        if ( selectionId == null ) {
            return null;
        }
        List< UUID > selectedList = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
        if ( CollectionUtils.isEmpty( selectedList ) ) {
            return null;
        }
        return selectedList.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString();
    }

    /**
     * Prepare database data source dto from database data source entity database data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param databaseDataSource
     *         the database data source
     *
     * @return the database data source dto
     */
    private DatabaseDataSourceDTO prepareDatabaseDataSourceDTOFromDatabaseDataSourceEntity( EntityManager entityManager,
            DatabaseDataSourceEntity databaseDataSource ) {
        DatabaseDataSourceDTO dataSourceDTO = new DatabaseDataSourceDTO();
        dataSourceDTO.setSourceType( databaseDataSource.getSourceType() );
        dataSourceDTO.setSourceName( databaseDataSource.getName() );
        dataSourceDTO.setDatabaseType( databaseDataSource.getDatabaseType() );
        dataSourceDTO.setPort( databaseDataSource.getPort() );
        dataSourceDTO.setHost( databaseDataSource.getHost() );
        dataSourceDTO.setPassword( databaseDataSource.getPassword() );
        dataSourceDTO.setUserName( databaseDataSource.getUserName() );
        dataSourceDTO.setDbName( databaseDataSource.getDbName() );
        dataSourceDTO.setId( databaseDataSource.getId().toString() );
        dataSourceDTO.setVersionId( databaseDataSource.getVersionId() );
        dataSourceDTO.setCreatedBy( userCommonManager.getUserById( entityManager, databaseDataSource.getCreatedBy().getId() ) );
        dataSourceDTO.setModifiedBy( userCommonManager.getUserById( entityManager, databaseDataSource.getModifiedBy().getId() ) );
        dataSourceDTO.setCreatedOn( databaseDataSource.getCreatedOn() );
        dataSourceDTO.setModifiedOn( databaseDataSource.getModifiedOn() );
        return dataSourceDTO;
    }

    /**
     * Prepare excel data source dto from excel data source entity excel data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param excelDataSourceEntity
     *         the excel data source entity
     *
     * @return the excel data source dto
     */
    private ExcelDataSourceDTO prepareExcelDataSourceDTOFromExcelDataSourceEntity( EntityManager entityManager,
            ExcelDataSourceEntity excelDataSourceEntity ) {
        ExcelDataSourceDTO dataSourceDTO = new ExcelDataSourceDTO();
        dataSourceDTO.setSourceType( excelDataSourceEntity.getSourceType() );
        dataSourceDTO.setSourceName( excelDataSourceEntity.getName() );
        if ( excelDataSourceEntity.getSelectionEntity() != null ) {
            dataSourceDTO.setFilePath(
                    getPathFromSelectionId( entityManager, excelDataSourceEntity.getSelectionEntity().getId().toString() ) );
            dataSourceDTO.setFileSelection( excelDataSourceEntity.getSelectionEntity().getId().toString() );
        }
        dataSourceDTO.setId( excelDataSourceEntity.getId().toString() );
        dataSourceDTO.setVersionId( excelDataSourceEntity.getVersionId() );
        dataSourceDTO.setCreatedBy( userCommonManager.getUserById( entityManager, excelDataSourceEntity.getCreatedBy().getId() ) );
        dataSourceDTO.setModifiedBy( userCommonManager.getUserById( entityManager, excelDataSourceEntity.getModifiedBy().getId() ) );
        dataSourceDTO.setCreatedOn( excelDataSourceEntity.getCreatedOn() );
        dataSourceDTO.setModifiedOn( excelDataSourceEntity.getModifiedOn() );
        return dataSourceDTO;
    }

    /**
     * Prepare csv data source dto from csv data source entity csv data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param csvDataSourceEntity
     *         the csv data source entity
     *
     * @return the csv data source dto
     */
    private CSVDataSourceDTO prepareCSVDataSourceDTOFromCSVDataSourceEntity( EntityManager entityManager,
            CSVDataSourceEntity csvDataSourceEntity ) {
        CSVDataSourceDTO dataSourceDTO = new CSVDataSourceDTO();
        dataSourceDTO.setSourceType( csvDataSourceEntity.getSourceType() );
        dataSourceDTO.setSourceName( csvDataSourceEntity.getName() );
        if ( csvDataSourceEntity.getSelectionEntity() != null ) {
            dataSourceDTO.setFilePath(
                    getPathFromSelectionId( entityManager, csvDataSourceEntity.getSelectionEntity().getId().toString() ) );
            dataSourceDTO.setFileSelection( csvDataSourceEntity.getSelectionEntity().getId().toString() );
        }
        dataSourceDTO.setDelimiter( csvDataSourceEntity.getDelimiter() );
        dataSourceDTO.setId( csvDataSourceEntity.getId().toString() );
        dataSourceDTO.setVersionId( csvDataSourceEntity.getVersionId() );
        dataSourceDTO.setCreatedBy( userCommonManager.getUserById( entityManager, csvDataSourceEntity.getCreatedBy().getId() ) );
        dataSourceDTO.setModifiedBy( userCommonManager.getUserById( entityManager, csvDataSourceEntity.getModifiedBy().getId() ) );
        dataSourceDTO.setCreatedOn( csvDataSourceEntity.getCreatedOn() );
        dataSourceDTO.setModifiedOn( csvDataSourceEntity.getModifiedOn() );
        return dataSourceDTO;
    }

    /**
     * Prepare json data source dto from json data source entity json data source dto.
     *
     * @param entityManager
     *         the entity manager
     * @param jsonDataSourceEntity
     *         the json data source entity
     *
     * @return the json data source dto
     */
    private JsonDataSourceDTO prepareJsonDataSourceDTOFromJsonDataSourceEntity( EntityManager entityManager,
            JsonDataSourceEntity jsonDataSourceEntity ) {
        JsonDataSourceDTO dataSourceDTO = new JsonDataSourceDTO();
        dataSourceDTO.setSourceType( jsonDataSourceEntity.getSourceType() );
        dataSourceDTO.setSourceName( jsonDataSourceEntity.getName() );
        if ( jsonDataSourceEntity.getSelectionEntity() != null ) {
            dataSourceDTO.setFilePath(
                    getPathFromSelectionId( entityManager, jsonDataSourceEntity.getSelectionEntity().getId().toString() ) );
            dataSourceDTO.setFileSelection( jsonDataSourceEntity.getSelectionEntity().getId().toString() );
        }
        dataSourceDTO.setId( jsonDataSourceEntity.getId().toString() );
        dataSourceDTO.setVersionId( jsonDataSourceEntity.getVersionId() );
        dataSourceDTO.setCreatedBy( userCommonManager.getUserById( entityManager, jsonDataSourceEntity.getCreatedBy().getId() ) );
        dataSourceDTO.setModifiedBy( userCommonManager.getUserById( entityManager, jsonDataSourceEntity.getModifiedBy().getId() ) );
        dataSourceDTO.setCreatedOn( jsonDataSourceEntity.getCreatedOn() );
        dataSourceDTO.setModifiedOn( jsonDataSourceEntity.getModifiedOn() );
        return dataSourceDTO;
    }

    /**
     * Prepare python payload from database data source entity database data source dto.
     *
     * @param databaseDataSource
     *         the database data source
     *
     * @return the database data source dto
     */
    private DatabaseDataSourceDTO preparePythonPayloadFromDatabaseDataSourceEntity( DatabaseDataSourceEntity databaseDataSource ) {
        DatabaseDataSourceDTO dataSourceDTO = new DatabaseDataSourceDTO();
        dataSourceDTO.setSourceType( databaseDataSource.getSourceType() );
        dataSourceDTO.setSourceName( databaseDataSource.getName() );
        dataSourceDTO.setDatabaseType( databaseDataSource.getDatabaseType() );
        dataSourceDTO.setPort( databaseDataSource.getPort() );
        dataSourceDTO.setHost( databaseDataSource.getHost() );
        dataSourceDTO.setPassword( databaseDataSource.getPassword() );
        dataSourceDTO.setUserName( databaseDataSource.getUserName() );
        dataSourceDTO.setDbName( databaseDataSource.getDbName() );
        return dataSourceDTO;
    }

    /**
     * Create object form dashboard data source map.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param dataSource
     *         the data source
     *
     * @return the map
     */
    @Override
    public UIForm createObjectFormDashboardDataSource( String userId, String objectId, String dataSource ) {
        var dataSourceOption = DashboardDataSourceOptions.getById( dataSource );
        return switch ( dataSourceOption ) {
            case DATABASE ->
                    DataDashboardUtil.getDataSourceFields( DashboardDataSourceOptions.DATABASE, new DatabaseDataSourceDTO(), objectId );
            case EXCEL -> DataDashboardUtil.getDataSourceFields( DashboardDataSourceOptions.EXCEL, new ExcelDataSourceDTO(), objectId );
            case CSV -> DataDashboardUtil.getDataSourceFields( DashboardDataSourceOptions.CSV, new CSVDataSourceDTO(), objectId );
            case JSON -> DataDashboardUtil.getDataSourceFields( DashboardDataSourceOptions.JSON, new JsonDataSourceDTO(), objectId );
            case SUS_OBJECT ->
                    DataDashboardUtil.getDataSourceFields( DashboardDataSourceOptions.SUS_OBJECT, new SuSObjectDataSourceDTO(), objectId );
        };
    }

    /**
     * Gets data source form for dashboard edit.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param dataSource
     *         the data source
     *
     * @return the data source form for dashboard edit
     */
    @Override
    public UIForm getDataSourceFormForDashboardEdit( String userId, String objectId, String dataSource ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                UUID.fromString( objectId ) );
        var dataSourceDTO = prepareDataSourceDTOFromEntity( entityManager, dashboardEntity.getDataSources().iterator().next() );
        var dataSourceOption = DashboardDataSourceOptions.getById( dataSourceDTO.getSourceType() );
        return switch ( dataSourceOption ) {
            case DATABASE -> DataDashboardUtil.getDataSourceFields( DashboardDataSourceOptions.DATABASE, dataSourceDTO, objectId );
            case CSV -> DataDashboardUtil.getDataSourceFields( DashboardDataSourceOptions.CSV, dataSourceDTO, objectId );
            case EXCEL -> DataDashboardUtil.getDataSourceFields( DashboardDataSourceOptions.EXCEL, dataSourceDTO, objectId );
            case JSON -> DataDashboardUtil.getDataSourceFields( DashboardDataSourceOptions.JSON, dataSourceDTO, objectId );
            case SUS_OBJECT -> DataDashboardUtil.getDataSourceFields( DashboardDataSourceOptions.SUS_OBJECT, dataSourceDTO, objectId );
        };
    }

    /**
     * Gets widget types field.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return the widget types field
     */
    @Override
    public UIForm getWidgetTypesField( String token, String objectId ) {
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setTitle( "Add Widget" );
        selectFormItem.setBindFrom( BIND_FROM_URLS.WIDGET_TYPES.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ) );
        selectFormItem.setType( FieldTypes.SELECTION.getType() );

        List< SelectOptionsUI > selectOptions = new ArrayList<>();
        for ( WIDGET_TYPE option : WIDGET_TYPE.values() ) {
            selectOptions.add( new SelectOptionsUI( option.getId(), option.getName() ) );
        }
        selectFormItem.setOptions( selectOptions );
        selectFormItem.setMultiple( false );
        selectFormItem.setCanBeEmpty( true );

        return GUIUtils.createFormFromItems( Collections.singletonList( selectFormItem ) );

    }

    /**
     * Gets widget form by type.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the widget form by type
     */
    @Override
    public UIForm getWidgetFormByType( String token, String objectId, String widgetId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            List< UIFormItem > formItems = new ArrayList<>( GUIUtils.prepareForm( true, widget, widget.getOptions() ).stream()
                    .filter( item -> item.getSection().equals( "default" ) ).toList() );
            formItems.add( DataDashboardUtil.populateDataSourceTypeFieldForWidgetForm( objectId, widget ) );
            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets field for data source type.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param sourceType
     *         the source type
     *
     * @return the field for data source type
     */
    @Override
    public UIForm getFieldForDataSourceType( String token, String objectId, String widgetId, String sourceType ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            WIDGET_SOURCE_TYPE option = WIDGET_SOURCE_TYPE.getById( sourceType );
            List< UIFormItem > formItems = switch ( option ) {
                case QUERY_BUILDER -> prepareQueryBuilderFieldsForWidget( entityManager, objectId, sourceType, widgetId );
                case PYTHON -> preparePythonFieldsForWidget( entityManager, objectId, sourceType, widgetId );
                case OTHER -> prepareOtherFieldsForWidget( objectId, sourceType, widgetId );
            };
            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets field for data source.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param sourceType
     *         the source type
     * @param dataSourceId
     *         the data source id
     *
     * @return the field for data source
     */
    @Override
    public UIForm getFieldForDataSource( String token, String objectId, String widgetId, String sourceType, String dataSourceId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widgetDTO = getWidgetById( entityManager, objectId, widgetId );
            var dataSource = getDataSourceById( entityManager, objectId, dataSourceId );
            WIDGET_SOURCE_TYPE option = WIDGET_SOURCE_TYPE.getById( sourceType );
            List< UIFormItem > formItems = switch ( option ) {
                case QUERY_BUILDER ->
                        prepareQueryBuilderFieldsForWidgetAndDataSource( objectId, widgetDTO, widgetId, dataSource.getSourceType() );
                case PYTHON -> preparePythonFieldsForWidgetAndDataSource( objectId, widgetDTO, dataSource.getSourceType() );
                case OTHER -> {
                    WidgetOtherSource source = ( WidgetOtherSource ) widgetDTO.getSource();
                    yield switch ( OTHER_WIDGET_SOURCE.getById( source.getName() ) ) {
                        case MATERIAL_INSPECTION, PST, METAL_BASE, VMCL ->
                                List.of( GUIUtils.getFormItemByField( WidgetOtherSource.class, WIDGET_FIELDS.NAME ) );
                    };
                }
            };
            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Prepare python fields for widget list.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param sourceType
     *         the source type
     * @param widgetId
     *         the widget id
     *
     * @return the list
     */
    private List< UIFormItem > preparePythonFieldsForWidget( EntityManager entityManager, String objectId, String sourceType,
            String widgetId ) {
        var item = ( SelectFormItem ) GUIUtils.getFormItemByField( WidgetPythonSource.class, WIDGET_FIELDS.DATA_SOURCE );
        setOptionsInDataSourceFormItem( entityManager, item, objectId, WIDGET_SOURCE_TYPE.PYTHON );
        item.setBindFrom(
                BIND_FROM_URLS.DATA_SOURCE.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_SOURCE_TYPE, sourceType ) );
        return Collections.singletonList( item );
    }

    /**
     * Prepare other fields for widget list.
     *
     * @param objectId
     *         the object id
     * @param sourceType
     *         the source type
     * @param widgetId
     *         the widget id
     *
     * @return the list
     */
    private List< UIFormItem > prepareOtherFieldsForWidget( String objectId, String sourceType, String widgetId ) {
        var item = ( SelectFormItem ) GUIUtils.getFormItemByField( WidgetOtherSource.class, WIDGET_FIELDS.NAME );
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var option : OTHER_WIDGET_SOURCE.values() ) {
            options.add( new SelectOptionsUI( option.getId(), option.getName() ) );
        }
        item.setOptions( options );
        item.setBindFrom(
                BIND_FROM_URLS.OTHER_SOURCE.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_SOURCE_TYPE, sourceType ) );
        return Collections.singletonList( item );
    }

    /**
     * Prepare query builder fields for widget list.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param sourceType
     *         the source type
     * @param widgetId
     *         the widget id
     *
     * @return the list
     */
    private List< UIFormItem > prepareQueryBuilderFieldsForWidget( EntityManager entityManager, String objectId, String sourceType,
            String widgetId ) {
        var item = ( SelectFormItem ) GUIUtils.getFormItemByField( WidgetQueryBuilderSource.class, WIDGET_FIELDS.DATA_SOURCE );
        setOptionsInDataSourceFormItem( entityManager, item, objectId, WIDGET_SOURCE_TYPE.QUERY_BUILDER );
        item.setBindFrom(
                BIND_FROM_URLS.DATA_SOURCE.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_SOURCE_TYPE, sourceType ) );
        return Collections.singletonList( item );
    }

    /**
     * Prepare python fields for widget and data source list.
     *
     * @param objectId
     *         the object id
     * @param widgetDto
     *         the widget dto
     * @param dataSourceType
     *         the data source type
     *
     * @return the list
     */
    private List< UIFormItem > preparePythonFieldsForWidgetAndDataSource( String objectId, DashboardWidgetDTO widgetDto,
            String dataSourceType ) {
        var allItems = GUIUtils.prepareForm( true, widgetDto.getSource() );

        for ( Iterator< UIFormItem > iterator = allItems.iterator(); iterator.hasNext(); ) {
            UIFormItem item = iterator.next();
            switch ( item.getName() ) {
                case WIDGET_FIELDS.PYTHON_SCRIPT_OPTION ->
                        DataDashboardUtil.setOptionsInPythonScripOptionsItem( ( SelectFormItem ) item, objectId, dataSourceType );
                case WIDGET_FIELDS.X_AXIS, WIDGET_FIELDS.Y_AXIS, SCATTER_WIDGET_FIELDS.POINT_COLOR, SCATTER_WIDGET_FIELDS.POINT_SIZE,
                     BAR_WIDGET_FIELDS.BAR_COLOR, WIDGET_FIELDS.PYTHON_SCRIPT, WIDGET_FIELDS.ID, WIDGET_FIELDS.TYPE,
                     WIDGET_FIELDS.QUERY_BUILDER_SCHEMA, WIDGET_FIELDS.QUERY_BUILDER_TABLE, WIDGET_FIELDS.DATA_SOURCE_TYPE,
                     WIDGET_FIELDS.PYTHON_STATUS, BAR_WIDGET_FIELDS.COLOR_AGGREGATE, WIDGET_FIELDS.DATA_SOURCE,
                     WIDGET_FIELDS.PYTHON_OUTPUT_TYPE, WIDGET_FIELDS.SELECT_SCRIPT, WIDGET_FIELDS.SYSTEM_SCRIPT -> iterator.remove();
                case WIDGET_FIELDS.COLOR_THEME -> DataDashboardUtil.populateColorSchemeOptions( ( SelectFormItem ) item );
                case LINE_WIDGET_FIELDS.SMOOTH -> DataDashboardUtil.setLineWidgetSmoothOptions( ( SelectFormItem ) item );
                case LINE_WIDGET_FIELDS.LINE_WIDGET_TYPE ->
                        DataDashboardUtil.setBindFromForLineWidgetType( objectId, widgetDto.getId(), ( SelectFormItem ) item );
            }
            if ( FieldTypes.SELECTION.getType().equals( item.getType() ) ) {
                GUIUtils.setLiveSearchInSelectItem( ( SelectFormItem ) item );
            }

        }
        return allItems;
    }

    /**
     * Prepare query builder fields for widget and data source list.
     *
     * @param objectId
     *         the object id
     * @param widgetDto
     *         the widget dto
     * @param widgetId
     *         the widget id
     * @param dataSourceType
     *         the data source type
     *
     * @return the list
     */
    private List< UIFormItem > prepareQueryBuilderFieldsForWidgetAndDataSource( String objectId, DashboardWidgetDTO widgetDto,
            String widgetId, String dataSourceType ) {
        var allItems = GUIUtils.prepareForm( true, widgetDto.getSource() );
        for ( Iterator< UIFormItem > iterator = allItems.iterator(); iterator.hasNext(); ) {
            UIFormItem item = iterator.next();
            switch ( item.getName() ) {
                case WIDGET_FIELDS.QUERY_BUILDER_SCHEMA -> {
                    if ( dataSourceType.equals( DashboardDataSourceOptions.DATABASE.getId() ) ) {
                        DataDashboardUtil.setOptionsInDataSourceSchemaFormItem( item, objectId, widgetId );
                    } else {
                        iterator.remove();
                    }
                }
                case WIDGET_FIELDS.QUERY_BUILDER_TABLE -> {
                    if ( dataSourceType.equals( DashboardDataSourceOptions.DATABASE.getId() ) ) {
                        DataDashboardUtil.setOptionsInDataSourceTableFormItem( item, objectId, widgetId );
                    } else {
                        iterator.remove();
                    }
                }
                case WIDGET_FIELDS.X_AXIS, WIDGET_FIELDS.Y_AXIS, SCATTER_WIDGET_FIELDS.POINT_COLOR, SCATTER_WIDGET_FIELDS.POINT_SIZE,
                     BAR_WIDGET_FIELDS.BAR_COLOR, WIDGET_FIELDS.PYTHON_PATH, WIDGET_FIELDS.PYTHON_SCRIPT, WIDGET_FIELDS.PYTHON_OUTPUT_TYPE,
                     WIDGET_FIELDS.PYTHON_STATUS, WIDGET_FIELDS.ID, WIDGET_FIELDS.TYPE, WIDGET_FIELDS.DATA_SOURCE,
                     WIDGET_FIELDS.DATA_SOURCE_TYPE -> iterator.remove();
                case BAR_WIDGET_FIELDS.COLOR_AGGREGATE -> {
                    DataDashboardUtil.setBindToForGroupByColumnsForBar( ( SelectFormItem ) item, widgetId, objectId );
                    ( ( SelectFormItem ) item ).setCanBeEmpty( true );
                }
                case WIDGET_FIELDS.COLOR_THEME -> DataDashboardUtil.populateColorSchemeOptions( ( SelectFormItem ) item );
                case LINE_WIDGET_FIELDS.SMOOTH -> DataDashboardUtil.setLineWidgetSmoothOptions( ( SelectFormItem ) item );
                case LINE_WIDGET_FIELDS.LINE_WIDGET_TYPE ->
                        DataDashboardUtil.setBindFromForLineWidgetType( objectId, widgetDto.getId(), ( SelectFormItem ) item );
            }
            if ( FieldTypes.SELECTION.getType().equals( item.getType() ) ) {
                GUIUtils.setLiveSearchInSelectItem( ( SelectFormItem ) item );
            }

        }
        return allItems;
    }

    /**
     * Gets schema list field by data source.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param dataSourceId
     *         the data source id
     *
     * @return the schema list field by data source
     */
    @Override
    public List< SelectOptionsUI > getSchemaListFieldByDataSource( String token, String objectId, String widgetId, String dataSourceId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            return getSchemaOptionsFromDataSource( entityManager, objectId, dataSourceId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets schema options from data source.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the schema options from data source
     */
    private List< SelectOptionsUI > getSchemaOptionsFromDataSource( EntityManager entityManager, String objectId, String dataSourceId ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate(
                    getDataSourceDTOByDashboardIdAndSourceId( entityManager, objectId, dataSourceId ) );
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            List< SelectOptionsUI > schemaList = new ArrayList<>();
            DataDashboardUtil.addSchemaOptionsToList( dataSourceId, metaData, schemaList );
            return schemaList;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_SCHEMAS_FROM_DATABASE.getKey() ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets data source dto by dashboard id and source id.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the data source dto by dashboard id and source id
     */
    private DatabaseDataSourceDTO getDataSourceDTOByDashboardIdAndSourceId( EntityManager entityManager, String objectId,
            String dataSourceId ) {
        DataDashboardEntity dashEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                UUID.fromString( objectId ) );

        var matchingEntity = dashEntity.getDataSources().stream()
                .filter( dataSource -> dataSource.getId().toString().equals( dataSourceId ) ).findFirst();
        if ( matchingEntity.isEmpty() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DATA_SOURCE_NOT_FOUND.getKey(), dataSourceId ) );
        }
        return ( DatabaseDataSourceDTO ) prepareDataSourceDTOFromEntity( entityManager, matchingEntity.get() );
    }

    /**
     * Gets tables list field by data source and schema.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param schema
     *         the schema
     *
     * @return the tables list field by data source and schema
     */
    @Override
    public List< SelectOptionsUI > getTablesListFieldByDataSourceAndSchema( String token, String objectId, String widgetId,
            String schema ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            return prepareTableListOptions( entityManager, objectId, schema );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets data sources list by dashboard id.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return the data sources list by dashboard id
     */
    @Override
    public List< DataSourceDTO > getDataSourcesListByDashboardId( String token, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            var dataSourceEntityList = dashboardEntity.getDataSources();
            if ( CollectionUtils.isEmpty( dataSourceEntityList ) ) {
                return List.of();
            }
            List< DataSourceDTO > dataSourceDTOS = new ArrayList<>( dataSourceEntityList.size() );
            dataSourceEntityList.forEach( entity -> dataSourceDTOS.add( prepareDataSourceDTOFromEntity( entityManager, entity ) ) );
            return dataSourceDTOS;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Add data source to dashboard data source dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceJson
     *         the data source json
     *
     * @return the data source dto
     */
    @Override
    public DataSourceDTO addDataSourceToDashboard( String token, String objectId, String dataSourceJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            var dataSourceDTO = DataDashboardUtil.getDataSourceDTOFromJson( dataSourceJson );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            dataSourceDTO.prepareEntity( dashboardEntity, userCommonManager.getUserEntityById( entityManager,
                    UUID.fromString( TokenizedLicenseUtil.getNotNullUser( token ).getId() ) ) );
            if ( dashboardEntity.getDataSources() == null ) {
                dashboardEntity.setDataSources( new ArrayList<>() );
            }
            var newEntity = dataSourceDTO.prepareEntity( dashboardEntity, userCommonManager.getUserEntityById( entityManager,
                    UUID.fromString( TokenizedLicenseUtil.getNotNullUser( token ).getId() ) ) );
            dashboardEntity.getDataSources().add( newEntity );
            susDAO.saveOrUpdateObject( entityManager, dashboardEntity );
            DataSourceDTO savedDataSource = prepareDataSourceDTOFromEntity( entityManager, newEntity );
            performBackgroundTasksForDataSource( savedDataSource, objectId );
            return savedDataSource;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Perform background tasks for data source.
     *
     * @param savedDataSource
     *         the saved data source
     * @param objectId
     *         the object id
     */
    private void performBackgroundTasksForDataSource( DataSourceDTO savedDataSource, String objectId ) {
        if ( savedDataSource instanceof SuSObjectDataSourceDTO suSObjectDataSourceDTO ) {
            Runnable myRunnable = () -> prepareCache( entityManagerFactory, suSObjectDataSourceDTO, objectId );
            myRunnable.run();
        }
    }

    /**
     * Update data source data source dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     * @param dataSourceJson
     *         the data source json
     *
     * @return the data source dto
     */
    @Override
    public DataSourceDTO updateDataSource( String token, String objectId, String dataSourceId, String dataSourceJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            var dataSourceDTO = DataDashboardUtil.getDataSourceDTOFromJson( dataSourceJson );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            if ( dashboardEntity.getDataSources() == null || dashboardEntity.getDataSources().stream()
                    .noneMatch( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), dataSourceId ) );
            }
            @SuppressWarnings( "OptionalGetWithoutIsPresent" ) var matchingEntity = dashboardEntity.getDataSources().stream()
                    .filter( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ).findFirst().get();
            updateDataSourceEntity( entityManager, matchingEntity, dataSourceDTO, token );
            susDAO.saveOrUpdateObject( entityManager, dashboardEntity );
            DataSourceDTO savedDataSource = prepareDataSourceDTOFromEntity( entityManager, matchingEntity );
            performBackgroundTasksForDataSource( savedDataSource, objectId );
            return savedDataSource;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Delete data source boolean.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the boolean
     */
    @Override
    public Boolean deleteDataSource( String token, String objectId, String dataSourceId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            if ( dashboardEntity.getDataSources() == null || dashboardEntity.getDataSources().stream()
                    .noneMatch( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), dataSourceId ) );
            }
            var dsToRemove = dashboardEntity.getDataSources().stream()
                    .filter( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ).findFirst().get();
            dashboardEntity.getDataSources().remove( dsToRemove );
            if ( dsToRemove.getSourceType().equals( DashboardDataSourceOptions.SUS_OBJECT.getId() ) ) {
                DashboardSuSObjectUtil.deleteCache( dashboardEntity.getComposedId(), dataSourceId );
            }
            susDAO.saveOrUpdateObject( entityManager, dashboardEntity );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            return false;
        } finally {
            entityManager.close();
        }
        return true;
    }

    /**
     * Create data source form map.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return the map
     */
    @Override
    public UIForm createDataSourceForm( String token, String objectId ) {
        return GUIUtils.createFormFromItems( List.of( DataDashboardUtil.prepareDataSourceTypeItemForForm( objectId ) ) );
    }

    /**
     * Edit data source form map.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the map
     */
    @Override
    public UIForm editDataSourceForm( String token, String objectId, String dataSourceId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            var matchingEntity = dashboardEntity.getDataSources().stream()
                    .filter( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ).findFirst().orElse( null );
            List< UIFormItem > items = null;
            if ( matchingEntity != null ) {
                var dataSourceDTO = prepareDataSourceDTOFromEntity( entityManager, matchingEntity );
                items = GUIUtils.prepareForm( false, dataSourceDTO );
                for ( var item : items ) {
                    switch ( item.getName() ) {
                        case DATA_SOURCE_FIELDS.PORT -> item.setValue( String.valueOf( item.getValue() ) );
                        case DATA_SOURCE_FIELDS.DATABASE_TYPE -> {
                            List< SelectOptionsUI > databaseTypeOptions = new ArrayList<>();
                            for ( var databaseOptions : DataSourceDatabaseOptions.values() ) {
                                databaseTypeOptions.add( new SelectOptionsUI( databaseOptions.getId(), databaseOptions.getName() ) );
                            }
                            ( ( SelectFormItem ) item ).setOptions( databaseTypeOptions );
                        }
                        case DATA_SOURCE_FIELDS.SOURCE_TYPE -> {
                            item.setReadonly( true );
                            item.setValue( dataSourceDTO.getSourceType() );
                            List< SelectOptionsUI > options = new ArrayList<>();
                            for ( DashboardDataSourceOptions option : DashboardDataSourceOptions.values() ) {
                                options.add( new SelectOptionsUI( option.getId(), option.getName() ) );
                            }
                            ( ( SelectFormItem ) item ).setOptions( options );
                        }
                        case DATA_SOURCE_FIELDS.DELIMITER -> {
                            List< SelectOptionsUI > delimiterOptions = new ArrayList<>();
                            for ( var delimiters : CSVDelimiter.values() ) {
                                delimiterOptions.add( new SelectOptionsUI( delimiters.getId(), delimiters.getName() ) );
                            }
                            ( ( SelectFormItem ) item ).setOptions( delimiterOptions );
                        }
                        case DATA_SOURCE_FIELDS.TEST_BUTTON -> {
                            var bindTo = new BindTo();
                            var bindToUrl = BIND_TO_URLS.BIND_TO_URL_FOR_TEST_CONNECTION;
                            bindTo.setUrl( bindToUrl );
                            item.setBindTo( bindTo );
                        }
                        case DATA_SOURCE_FIELDS.PREVIEW_BUTTON -> {
                            var bindTo = new BindTo();
                            var bindToUrl = BIND_TO_URLS.BIND_TO_URL_FOR_PREVIEW_CONNECTION;
                            bindTo.setUrl( bindToUrl );
                            item.setBindTo( bindTo );
                        }
                        case DATA_SOURCE_FIELDS.UPDATE_CACHE -> {
                            var bindTo = new BindTo();
                            var bindToUrl = BIND_TO_URLS.BIND_TO_URL_FOR_UPDATE_CACHE.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                                    .replace( URL_PARAMS.PARAM_SOURCE_ID, dataSourceId );
                            bindTo.setUrl( bindToUrl );
                            item.setBindTo( bindTo );
                        }
                    }
                }
            }
            return GUIUtils.createFormFromItems( items != null ? items : new ArrayList<>() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets columns list field by data source and schema and table.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param sourceType
     *         the source type
     * @param value
     *         the value
     *
     * @return the columns list field by data source and schema and table
     */
    @Override
    public List< SelectOptionsUI > getColumnsListFieldByDataSourceAndSchemaAndTable( String token, String objectId, String widgetId,
            String sourceType, String value ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var widget = getWidgetById( entityManager, objectId, widgetId );
            return switch ( WIDGET_SOURCE_TYPE.getById( sourceType ) ) {
                case QUERY_BUILDER -> prepareColumnListOptions( entityManager, objectId, value );
                case PYTHON -> getColumnsFromPythonOutput( entityManager, objectId, value, widgetId );
                case OTHER -> switch ( OTHER_WIDGET_SOURCE.getById( ( ( WidgetOtherSource ) widget.getSource() ).getName() ) ) {
                    case METAL_BASE, PST -> null;
                    case MATERIAL_INSPECTION -> DashboardMaterialInspectorUtil.getColumnsFromOutputFile( widget, objectId );
                    case VMCL -> null;
                };
            };
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets columns from python output.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param status
     *         the status
     * @param widgetId
     *         the widget id
     *
     * @return the columns from python output
     */
    private List< SelectOptionsUI > getColumnsFromPythonOutput( EntityManager entityManager, String objectId, String status,
            String widgetId ) {
        DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                UUID.fromString( objectId ) );
        if ( dashboardEntity.getWidgets() == null || dashboardEntity.getWidgets().stream()
                .noneMatch( widgetEntity -> widgetEntity.getId().toString().equals( widgetId ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), widgetId ) );
        }
        @SuppressWarnings( "OptionalGetWithoutIsPresent" ) var matchingEntity = dashboardEntity.getWidgets().stream()
                .filter( widgetEntity -> widgetEntity.getId().toString().equals( widgetId ) ).findFirst().get();
        DashboardWidgetDTO dashboardWidgetDTO = DataDashboardUtil.prepareWidgetDTOFromJson(
                ByteUtil.convertByteToString( matchingEntity.getConfiguration() ) );
        dashboardWidgetDTO.setId( widgetId );
        var split = status.split( "\\." );
        String pythonOutput = split[ 0 ];
        String scriptStatus = split[ 1 ];
        return switch ( ScriptStatusOption.getById( scriptStatus ) ) {
            case IN_CHANGE -> new ArrayList<>();
            case FINAL -> getColumnsFromPythonOutputByOutputType( dashboardEntity.getComposedId(), dashboardWidgetDTO, pythonOutput );
        };
    }

    /**
     * Gets columns from python output by output type.
     *
     * @param composedId
     *         the composed id
     * @param dashboardWidgetDTO
     *         the dashboard widget dto
     * @param pythonOutput
     *         the python output
     *
     * @return the columns from python output by output type
     */
    private List< SelectOptionsUI > getColumnsFromPythonOutputByOutputType( VersionPrimaryKey composedId,
            DashboardWidgetDTO dashboardWidgetDTO, String pythonOutput ) {
        return DashboardPythonUtil.readColumnsFromOutput( composedId, dashboardWidgetDTO, pythonOutput );

    }

    /**
     * Gets filter columns by data source and schema and table.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param table
     *         the table
     *
     * @return the filter columns by data source and schema and table
     */
    @Override
    public TableUI getFilterColumnsByDataSourceAndSchemaAndTable( String token, String objectId, String table ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return prepareFilteredColumns( entityManager, objectId, table );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets widgets list by dashboard id.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return the widgets list by dashboard id
     */
    @Override
    public List< DashboardWidgetDTO > getWidgetsListByDashboardId( String token, String objectId ) {
        List< DashboardWidgetDTO > widgetList = new ArrayList<>();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            if ( CollectionUtils.isEmpty( dashboardEntity.getWidgets() ) ) {
                return widgetList;
            }
            dashboardEntity.getWidgets()
                    .forEach( widgetEntity -> widgetList.add( DataDashboardUtil.prepareWidgetDTOFromEntity( widgetEntity ) ) );
        } finally {
            entityManager.close();
        }
        return widgetList;
    }

    /**
     * Add widget to dashboard dashboard widget dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the dashboard widget dto
     */
    @Override
    public DashboardWidgetDTO addWidgetToDashboard( String token, String objectId, String widgetJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            if ( dashboardEntity.getWidgets() == null ) {
                dashboardEntity.setWidgets( new ArrayList<>() );
            }
            var newEntity = prepareWidgetEntityFromJson( entityManager, dashboardEntity, token, widgetJson );
            dashboardEntity.getWidgets().add( newEntity );
            susDAO.saveOrUpdateObject( entityManager, dashboardEntity );
            DashboardCacheUtil.createWidgetDirectoryInCache( dashboardEntity.getComposedId(), newEntity.getId() );
            DashboardWidgetDTO dashboardWidgetDTO = DataDashboardUtil.prepareWidgetDTOFromJson( widgetJson );
            dashboardWidgetDTO.setId( newEntity.getId().toString() );
            dashboardWidgetDTO.setVersionId( newEntity.getVersionId() );
            return dashboardWidgetDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare widget entity from json dashboard widget entity.
     *
     * @param entityManager
     *         the entity manager
     * @param dashboardEntity
     *         the dashboard entity
     * @param token
     *         the token
     * @param widgetJson
     *         the widget json
     *
     * @return the dashboard widget entity
     */
    private DashboardWidgetEntity prepareWidgetEntityFromJson( EntityManager entityManager, DataDashboardEntity dashboardEntity,
            String token, String widgetJson ) {
        DashboardWidgetEntity entity = new DashboardWidgetEntity();
        DashboardWidgetDTO dto = DataDashboardUtil.prepareWidgetDTOFromJson( widgetJson );
        entity.setConfiguration( ByteUtil.convertStringToByte( widgetJson ) );
        entity.setCreatedOn( new Date() );
        entity.setModifiedOn( new Date() );
        UserEntity userEntity = userCommonManager.getUserEntityById( entityManager,
                UUID.fromString( TokenizedLicenseUtil.getNotNullUser( token ).getId() ) );
        entity.setCreatedBy( userEntity );
        entity.setModifiedBy( userEntity );
        entity.setWidgetType( dto.getType() );
        entity.setDashboard( dashboardEntity );
        entity.setCompositeId( new VersionPrimaryKey( UUID.randomUUID(), dashboardEntity.getComposedId().getVersionId() ) );
        entity.setRelation(
                dto.getRelation() == null ? dto.getType().equals( WIDGET_TYPE.GROUP.getId() ) ? WIDGET_RELATION.IS_GROUP.getKey()
                        : WIDGET_RELATION.IS_NOT_IN_GROUP.getKey() : dto.getRelation().getKey() );
        return entity;
    }

    /**
     * Update widget dashboard widget dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param widgetJson
     *         the widget json
     *
     * @return the dashboard widget dto
     */
    @Override
    public DashboardWidgetDTO updateWidget( String token, String objectId, String widgetId, String widgetJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            if ( dashboardEntity.getWidgets() == null || dashboardEntity.getWidgets().stream()
                    .noneMatch( widgetEntity -> widgetEntity.getId().toString().equals( widgetId ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), widgetId ) );
            }
            @SuppressWarnings( "OptionalGetWithoutIsPresent" ) var matchingEntity = dashboardEntity.getWidgets().stream()
                    .filter( widgetEntity -> widgetEntity.getId().toString().equals( widgetId ) ).findFirst().get();
            updateWidgetEntity( entityManager, matchingEntity, widgetJson, token );
            susDAO.saveOrUpdateObject( entityManager, dashboardEntity );
            return DataDashboardUtil.prepareWidgetDTOFromJson( widgetJson );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update widget entity.
     *
     * @param entityManager
     *         the entity manager
     * @param matchingEntity
     *         the matching entity
     * @param widgetJson
     *         the widget json
     * @param token
     *         the token
     */
    private void updateWidgetEntity( EntityManager entityManager, DashboardWidgetEntity matchingEntity, String widgetJson, String token ) {
        DashboardWidgetDTO dto = DataDashboardUtil.prepareWidgetDTOFromJson( widgetJson );
        if ( dto.getOptions() != null ) {
            matchingEntity.setName( dto.getOptions().getTitle() );
            matchingEntity.setWidgetType( dto.getType() );
        }
        matchingEntity.setRelation( dto.getType().equals( WIDGET_TYPE.GROUP.getId() ) ? WIDGET_RELATION.IS_GROUP.getKey()
                : ( dto.getRelation() != null ? dto.getRelation().getKey() : WIDGET_RELATION.IS_NOT_IN_GROUP.getKey() ) );
        matchingEntity.setModifiedOn( new Date() );
        matchingEntity.setModifiedBy( userCommonManager.getUserEntityById( entityManager,
                UUID.fromString( TokenizedLicenseUtil.getNotNullUser( token ).getId() ) ) );
        matchingEntity.setConfiguration( ByteUtil.convertStringToByte( widgetJson ) );
    }

    /**
     * Delete widget boolean.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the boolean
     */
    @Override
    public Boolean deleteWidget( String token, String objectId, String widgetId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            if ( dashboardEntity.getWidgets() == null || dashboardEntity.getWidgets().stream()
                    .noneMatch( widgetEntity -> widgetEntity.getId().toString().equals( widgetId ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), widgetId ) );
            }
            var widgetToDelete = dashboardEntity.getWidgets().stream()
                    .filter( widgetEntity -> widgetEntity.getId().toString().equals( widgetId ) ).findFirst().get();
            dashboardEntity.getWidgets().remove( widgetToDelete );
            deleteNestedWidgetsForGroup( widgetToDelete, dashboardEntity );
            DashboardCacheUtil.deleteWidgetDirectoryFromCache( dashboardEntity.getComposedId(), UUID.fromString( widgetId ) );
            susDAO.saveOrUpdateObject( entityManager, dashboardEntity );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            return false;
        } finally {
            entityManager.close();
        }
        return true;
    }

    /**
     * Delete nested widgets for group.
     *
     * @param widgetToDelete
     *         the widget to delete
     * @param dashboardEntity
     *         the dashboard entity
     */
    private static void deleteNestedWidgetsForGroup( DashboardWidgetEntity widgetToDelete, DataDashboardEntity dashboardEntity ) {
        if ( widgetToDelete.getWidgetType().equals( WIDGET_TYPE.GROUP.getId() ) ) {
            var dto = DataDashboardUtil.prepareWidgetDTOFromEntity( widgetToDelete );
            GroupWidgetOptions groupWidgetOptions = ( GroupWidgetOptions ) dto.getOptions();
            if ( groupWidgetOptions.getWidgets() != null ) {
                List< DashboardWidgetEntity > widgets = dashboardEntity.getWidgets();
                Iterator< DashboardWidgetEntity > iterator = widgets.iterator();
                while ( iterator.hasNext() ) {
                    DashboardWidgetEntity widgetEntity = iterator.next();
                    if ( groupWidgetOptions.getWidgets().contains( widgetEntity.getId().toString() ) ) {
                        iterator.remove();
                        DashboardCacheUtil.deleteWidgetDirectoryFromCache( dashboardEntity.getComposedId(), widgetEntity.getId() );
                    }
                }
            }

        }
    }

    /**
     * Gets widget by id.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the widget by id
     */
    private DashboardWidgetDTO getWidgetById( EntityManager entityManager, String objectId, String widgetId ) {
        DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                UUID.fromString( objectId ) );
        if ( dashboardEntity.getWidgets() == null || dashboardEntity.getWidgets().stream()
                .noneMatch( widgetEntity -> widgetEntity.getId().toString().equals( widgetId ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), widgetId ) );
        }
        @SuppressWarnings( "OptionalGetWithoutIsPresent" ) var matchingEntity = dashboardEntity.getWidgets().stream()
                .filter( widgetEntity -> widgetEntity.getId().toString().equals( widgetId ) ).findFirst().get();
        DashboardWidgetDTO dashboardWidgetDTO = DataDashboardUtil.prepareWidgetDTOFromJson(
                ByteUtil.convertByteToString( matchingEntity.getConfiguration() ) );
        dashboardWidgetDTO.setId( widgetId );
        dashboardWidgetDTO.setVersionId( dashboardEntity.getVersionId() );
        if ( dashboardWidgetDTO.getRelation() == null ) {
            DataDashboardUtil.setRelationInWidgetIfNotSetInEntity( dashboardWidgetDTO );
        }
        if ( WIDGET_RELATION.IS_IN_GROUP.equals( dashboardWidgetDTO.getRelation() ) && dashboardWidgetDTO.getGroupId() != null ) {
            var group = getWidgetById( entityManager, objectId, dashboardWidgetDTO.getGroupId() );
            dashboardWidgetDTO.setSource( group.getSource() );
        }
        return dashboardWidgetDTO;
    }

    /**
     * Gets widget result set.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     * @param pythonMode
     *         the python mode
     *
     * @return the widget result set
     */
    @Override
    public Object getWidgetResultSet( String token, String objectId, String widgetJson, String pythonMode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var dto = DataDashboardUtil.prepareWidgetDTOFromJson( widgetJson );
            if ( dto.getRelation() == null ) {
                DataDashboardUtil.setRelationInWidgetIfNotSetInEntity( dto );
            }
            if ( WIDGET_RELATION.IS_IN_GROUP.equals( dto.getRelation() ) && dto.getGroupId() != null ) {
                var group = getWidgetById( entityManager, objectId, dto.getGroupId() );
                dto.setSource( group.getSource() );
            }
            DataDashboardUtil.validateWidgetSourceOptions( dto );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            String dataSourceId = DataDashboardUtil.getDataSourceIdFromWidget( dto );
            DataSourceDTO dataSource = null;
            if ( dataSourceId != null ) {
                if ( dashboardEntity.getDataSources() == null || dashboardEntity.getDataSources().stream()
                        .noneMatch( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), dataSourceId ) );
                }
                dataSource = prepareDataSourceDTOFromEntity( entityManager,
                        dashboardEntity.getDataSources().stream().filter( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) )
                                .findFirst().get() );
            }
            DataDashboardUtil.validateWidgetForResultSet( dto, dataSource );
            var sourceType = WIDGET_SOURCE_TYPE.getById( dto.getSource().getSourceType() );
            if ( null != dataSource && dto.getSource() instanceof WidgetVmclSource && sourceType.equals( WIDGET_SOURCE_TYPE.OTHER )
                    && DashboardDataSourceOptions.SUS_OBJECT.getId().equalsIgnoreCase( dataSource.getSourceType() ) ) {
                DashboardVMclUtil.callPython( token, dto, ( SuSObjectDataSourceDTO ) dataSource, objectId );
            }
            return getWidgetResultSetBySourceType( token, objectId, pythonMode, sourceType, dataSource, dto, entityManager,
                    dashboardEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets widget result set by source type.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param pythonMode
     *         the python mode
     * @param sourceType
     *         the source type
     * @param dataSource
     *         the data source
     * @param dto
     *         the dto
     * @param entityManager
     *         the entity manager
     * @param dashboardEntity
     *         the dashboard entity
     *
     * @return the widget result set by source type
     */
    private Object getWidgetResultSetBySourceType( String token, String objectId, String pythonMode, WIDGET_SOURCE_TYPE sourceType,
            DataSourceDTO dataSource, DashboardWidgetDTO dto, EntityManager entityManager, DataDashboardEntity dashboardEntity ) {
        return switch ( sourceType ) {
            case QUERY_BUILDER -> switch ( DashboardDataSourceOptions.getById( dataSource.getSourceType() ) ) {
                case DATABASE -> runQueryForQueryBuilder( dto, dataSource );
                case CSV -> getDataFromCSV( entityManager, dto, ( CSVDataSourceDTO ) dataSource );
                case JSON -> getDataFromJson( entityManager, dto, ( JsonDataSourceDTO ) dataSource );
                case SUS_OBJECT -> getDataFromSuSObject( entityManager, dto, ( SuSObjectDataSourceDTO ) dataSource );
                case EXCEL -> null;

            };
            case PYTHON -> {
                if ( PYTHON_MODES.REGENERATE.equals( pythonMode ) ) {
                    DashboardPythonUtil.runWidgetPython( dto, UUID.fromString( dto.getId() ), dashboardEntity.getComposedId(), dataSource,
                            token );
                }
                yield DashboardPythonUtil.getQueryResponseFromPythonOutput( dashboardEntity.getComposedId(), dto );
            }
            case OTHER -> {
                WidgetOtherSource source = ( WidgetOtherSource ) dto.getSource();
                yield switch ( OTHER_WIDGET_SOURCE.getById( source.getName() ) ) {
                    case MATERIAL_INSPECTION -> DashboardMaterialInspectorUtil.getResultFromOutputFile( dto, objectId );
                    case METAL_BASE -> DashboardMetalBaseUtil.getResultFromOutputFile( dto, objectId, METAL_BASE_ACTION.plotData );
                    case PST -> {
                        DashboardPSTUtil.callPython( dto, objectId, PST_ACTIONS.get, null );
                        yield DashboardPSTUtil.readOutputFile( UUID.fromString( dto.getId() ),
                                new VersionPrimaryKey( UUID.fromString( objectId ), dto.getVersionId() ), PST_ACTIONS.get );
                    }
                    case VMCL -> DashboardVMclUtil.getResultFromOutputFile( dto, objectId );
                };
            }
        };
    }

    /**
     * Gets data from su s object.
     *
     * @param entityManager
     *         the entity manager
     * @param dto
     *         the dto
     * @param dataSource
     *         the data source
     *
     * @return the data from su s object
     */
    private DynamicQueryResponse getDataFromSuSObject( EntityManager entityManager, DashboardWidgetDTO dto,
            SuSObjectDataSourceDTO dataSource ) {
        SuSEntity selectedProject = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( dataSource.getProjectId() ) );
        return DashboardSuSObjectUtil.getDataFromSuSObjectForWidget( entityManager, selectedProject, dto, susDAO );
    }

    /**
     * Gets data from csv.
     *
     * @param entityManager
     *         the entity manager
     * @param dto
     *         the dto
     * @param dataSource
     *         the data source
     *
     * @return the data from csv
     */
    private DynamicQueryResponse getDataFromCSV( EntityManager entityManager, DashboardWidgetDTO dto, CSVDataSourceDTO dataSource ) {
        Path csvPath = Path.of( getPathFromSelectionId( entityManager, dataSource.getFileSelection() ) );
        return DashboardCSVUtil.getDataFromCsvFileForWidget( csvPath, dto );
    }

    /**
     * Gets data from json.
     *
     * @param entityManager
     *         the entity manager
     * @param dto
     *         the dto
     * @param dataSource
     *         the data source
     *
     * @return the data from json
     */
    private DynamicQueryResponse getDataFromJson( EntityManager entityManager, DashboardWidgetDTO dto, JsonDataSourceDTO dataSource ) {
        Path jsonPath = Path.of( getPathFromSelectionId( entityManager, dataSource.getFileSelection() ) );
        return DashboardJsonUtil.getDataFromJsonFileForWidget( jsonPath, dto );
    }

    /**
     * Run query for query builder dynamic query response.
     *
     * @param dto
     *         the dto
     * @param widgetSource
     *         the widget source
     *
     * @return the dynamic query response
     */
    private DynamicQueryResponse runQueryForQueryBuilder( DashboardWidgetDTO dto, DataSourceDTO widgetSource ) {
        return switch ( WIDGET_TYPE.getById( dto.getType() ) ) {
            case MIX_CHART -> DataDashboardUtil.runQueryForMultiChartWidget( widgetSource, dto );
            case BAR -> DataDashboardUtil.runQueryForBarWidget( widgetSource, dto );
            case SCATTER -> DataDashboardUtil.runQueryForScatterWidget( widgetSource, dto );
            case LINE -> DataDashboardUtil.runQueryForLineWidget( widgetSource, dto );
            case TEXT -> DataDashboardUtil.runQueryForTextWidget( widgetSource, dto );
            case RADAR -> DataDashboardUtil.runQueryForRadarWidget( widgetSource, dto );
            case TABLE -> DashboardQueryBuilder.runQueryForTableWidget( widgetSource, dto );
            case PST, TREEMAP, GROUP, METAL_BASE, PROJECT_LIFE_CYCLE -> null;
        };
    }

    /**
     * Gets widget python script field.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param pythonOutput
     *         the python output
     *
     * @return the widget python script field
     */
    @Override
    public UIForm getWidgetPythonScriptField( String token, String objectId, String pythonOutput ) {
        var split = pythonOutput.split( "\\." );
        pythonOutput = split[ 1 ];
        String dataSourceType = split[ 0 ];
        List< UIFormItem > itemList = new ArrayList<>();
        var item = DataDashboardUtil.getPythonScriptField();
        item.setValue( getPythonTemplate( pythonOutput, dataSourceType ) );
        itemList.add( item );
        itemList.add( DataDashboardUtil.getPythonStatusField( pythonOutput ) );
        return GUIUtils.createFormFromItems( itemList );
    }

    /**
     * Gets widget python fields.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param scriptOption
     *         the script option
     *
     * @return the widget python fields
     */
    @Override
    public UIForm getWidgetPythonFields( String token, String objectId, String scriptOption ) {
        var split = scriptOption.split( "\\." );
        scriptOption = split[ 1 ];
        String dataSourceType = split[ 0 ];
        return GUIUtils.createFormFromItems( switch ( WIDGET_SCRIPT_OPTION.getById( scriptOption ) ) {
            case SYSTEM -> DataDashboardUtil.prepareWidgetFieldsForSystemScript();
            case USER -> DataDashboardUtil.prepareWidgetFieldsForUserScript( objectId, dataSourceType );
            case FILE -> DataDashboardUtil.prepareWidgetFieldsForSelectScript( objectId, dataSourceType );
        } );
    }

    /**
     * Gets widget python input preview.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the widget python input preview
     */
    @Override
    public String getWidgetPythonInputPreview( String token, String objectId, String widgetJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            var dashboardWidgetDTO = DataDashboardUtil.prepareWidgetDTOFromJson( widgetJson );
            var dataSourceId = ( ( WidgetPythonSource ) dashboardWidgetDTO.getSource() ).getDataSource();
            var dataSource = dashboardEntity.getDataSources().stream()
                    .filter( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ).findFirst().stream().findFirst().orElseThrow(
                            () -> new SusException(
                                    MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), dataSourceId ) ) );
            var dataSourceDTO = preparePythonPayloadFromDataSourceEntity( entityManager, dataSource );
            return DashboardPythonUtil.getPythonInputPreview( dataSourceDTO, dashboardWidgetDTO, objectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets widget python output preview.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the widget python output preview
     */
    @Override
    public Object getWidgetPythonOutputPreview( String token, String objectId, String widgetJson ) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var dashboardEntity = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( objectId ) );
            var dashboardWidgetDTO = DataDashboardUtil.prepareWidgetDTOFromJson( widgetJson );
            resolveSelectionIdInPythonPathField( entityManager, ( WidgetPythonSource ) dashboardWidgetDTO.getSource() );
            return DashboardPythonUtil.getPythonOutputPreview( dashboardWidgetDTO, dashboardEntity.getComposedId() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Resolve selection id in python path field.
     *
     * @param entityManager
     *         the entity manager
     * @param widgetSource
     *         the dashboard widget dto
     */
    private void resolveSelectionIdInPythonPathField( EntityManager entityManager, WidgetPythonSource widgetSource ) {
        widgetSource.setPythonPath( getPathFromSelectionId( entityManager, widgetSource.getPythonPath() ) );
    }

    /**
     * Resolve selection id in python select script field.
     *
     * @param entityManager
     *         the entity manager
     * @param widgetSource
     *         the widget source
     */
    private void resolveSelectionIdInPythonSelectScriptField( EntityManager entityManager, WidgetPythonSource widgetSource ) {
        widgetSource.setSelectScript( getPathFromSelectionId( entityManager, widgetSource.getSelectScript() ) );
    }

    /**
     * Run widget python process result.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the process result
     */
    @Override
    public ProcessResult runWidgetPython( String token, String objectId, String widgetJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            var dashboardWidgetDTO = DataDashboardUtil.prepareWidgetDTOFromJson( widgetJson );
            var dataSourceId = ( ( WidgetPythonSource ) dashboardWidgetDTO.getSource() ).getDataSource();
            var dataSource = dashboardEntity.getDataSources().stream()
                    .filter( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ).findFirst().stream().findFirst().orElseThrow(
                            () -> new SusException(
                                    MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), dataSourceId ) ) );
            var dataSourceDTO = preparePythonPayloadFromDataSourceEntity( entityManager, dataSource );
            resolveSelectionIdInPythonPathField( entityManager, ( WidgetPythonSource ) dashboardWidgetDTO.getSource() );
            ProcessResult processResult = DashboardPythonUtil.runWidgetPython( dashboardWidgetDTO,
                    UUID.fromString( dashboardWidgetDTO.getId() ), dashboardEntity.getComposedId(), dataSourceDTO, token );
            processResult.setCommand( new ArrayList<>() );
            return processResult;

        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets applicable aggregate methods.
     *
     * @param tokenFromGeneralHeader
     *         the token from general header
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param groupBy
     *         the group by
     *
     * @return the applicable aggregate methods
     */
    @Override
    public List< SelectOptionsUI > getApplicableAggregateMethods( String tokenFromGeneralHeader, String objectId, String widgetId,
            String groupBy ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        JdbcTemplate jdbcTemplate = null;
        try {
            //temporarily using split. will be fixed later with multiple bindTo support
            var split = groupBy.split( "\\." );
            String dataSourceId = split[ 0 ];
            String schemaName = split[ 1 ];
            String tableName = split[ 2 ];
            String columnName = split[ 3 ];

            jdbcTemplate = DataDashboardUtil.getJdbcTemplate(
                    getDataSourceDTOByDashboardIdAndSourceId( entityManager, objectId, dataSourceId ) );
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            var columnType = DataDashboardUtil.getColumnType( schemaName, tableName, columnName, metaData );
            return DataDashboardUtil.getAggregateValuesByType( columnType );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_TABLES_FROM_DATABASE.getKey() ) );
        } finally {
            entityManager.close();
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets data source by id.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the data source by id
     */
    @Override
    public DataSourceDTO getDataSourceById( String token, String objectId, String dataSourceId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getDataSourceById( entityManager, objectId, dataSourceId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Create copies of data sources for new version list.
     *
     * @param dataSources
     *         the data sources
     * @param updatedEntity
     *         the version id
     * @param newVersionId
     *         the new version id
     *
     * @return the list
     */
    @Override
    public List< DataSourceEntity > createCopiesOfDataSourcesForNewVersion( List< DataSourceEntity > dataSources,
            DataDashboardEntity updatedEntity, int newVersionId ) {
        if ( CollectionUtils.isEmpty( dataSources ) ) {
            return new ArrayList<>();
        }
        return dataSources.stream().map( dataSource -> {
            try {
                DataSourceEntity copy = ( DataSourceEntity ) DataUtils.createDeepCopyThroughSerialization( dataSource );
                copy.getCompositeId().setVersionId( newVersionId );
                copy.setDashboard( updatedEntity );
                return copy;
            } catch ( IOException | ClassNotFoundException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( MessageBundleFactory.getMessage( Messages.SERIALIZATION_ERROR.getKey() ) );
            }
        } ).toList();
    }

    /**
     * Create copies of widgets for new version list.
     *
     * @param widgets
     *         the widgets
     * @param updatedEntity
     *         the version id
     * @param newVersionId
     *         the new version id
     *
     * @return the list
     */
    @Override
    public List< DashboardWidgetEntity > createCopiesOfWidgetsForNewVersion( List< DashboardWidgetEntity > widgets,
            DataDashboardEntity updatedEntity, int newVersionId ) {
        if ( CollectionUtils.isEmpty( widgets ) ) {
            return new ArrayList<>();
        }
        return widgets.stream().map( widget -> {
            try {
                DashboardCacheUtil.copyWidgetCache( updatedEntity, widget, newVersionId );
                DashboardWidgetEntity copy = ( DashboardWidgetEntity ) DataUtils.createDeepCopyThroughSerialization( widget );
                copy.setVersionId( newVersionId );
                copy.setDashboard( updatedEntity );
                return copy;
            } catch ( IOException | ClassNotFoundException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( MessageBundleFactory.getMessage( Messages.SERIALIZATION_ERROR.getKey() ) );
            }
        } ).toList();
    }

    /**
     * Gets widget options form.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param pythonMode
     *         the python mode
     *
     * @return the widget options form
     */
    @Override
    public UIForm getWidgetOptionsForm( String token, String objectId, String widgetId, String pythonMode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            String dataSourceId = DataDashboardUtil.getDataSourceIdFromWidget( widget );
            List< UIFormItem > formItems = DataDashboardUtil.getFormItemsListForWidgetOptions( widget );
            DataSourceDTO selectedDataSource = null;
            if ( dataSourceId != null ) {
                selectedDataSource = getDataSourceById( entityManager, objectId, dataSourceId );
            }
            populateOptionsFields( entityManager, formItems, widget, selectedDataSource, objectId, token, pythonMode );
            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Resolve selection ids for pst widget source.
     *
     * @param entityManager
     *         the entity manager
     * @param widget
     *         the widget
     * @param dsEntities
     *         the ds entities
     */
    private void resolveFilePathsForPstWidgetSource( EntityManager entityManager, DashboardWidgetDTO widget,
            List< DataSourceEntity > dsEntities ) {
        WidgetPstSource widgetSource = ( WidgetPstSource ) widget.getSource();

        CSVDataSourceEntity aceLoungeSource = ( CSVDataSourceEntity ) dsEntities.stream()
                .filter( ds -> ds.getId().toString().equals( widgetSource.getAceLoungeCsv() ) ).findFirst().orElse( null );
        if ( aceLoungeSource != null ) {
            widgetSource.setAceLoungeCsv(
                    getPathFromSelectionId( entityManager, aceLoungeSource.getSelectionEntity().getId().toString() ) );
        }

        ExcelDataSourceEntity aplUpdatesSource = ( ExcelDataSourceEntity ) dsEntities.stream()
                .filter( ds -> ds.getId().toString().equals( widgetSource.getAplUpdates() ) ).findFirst().orElse( null );
        if ( aplUpdatesSource != null ) {
            widgetSource.setAplUpdates( getPathFromSelectionId( entityManager, aplUpdatesSource.getSelectionEntity().getId().toString() ) );
        }

        ExcelDataSourceEntity bmwUpdatesSource = ( ExcelDataSourceEntity ) dsEntities.stream()
                .filter( ds -> ds.getId().toString().equals( widgetSource.getBmwUpdates() ) ).findFirst().orElse( null );
        if ( bmwUpdatesSource != null ) {
            widgetSource.setBmwUpdates( getPathFromSelectionId( entityManager, bmwUpdatesSource.getSelectionEntity().getId().toString() ) );
        }

        ExcelDataSourceEntity ksUpdatesSource = ( ExcelDataSourceEntity ) dsEntities.stream()
                .filter( ds -> ds.getId().toString().equals( widgetSource.getKsUpdates() ) ).findFirst().orElse( null );
        if ( ksUpdatesSource != null ) {
            widgetSource.setKsUpdates( getPathFromSelectionId( entityManager, ksUpdatesSource.getSelectionEntity().getId().toString() ) );
        }
    }

    /**
     * Gets widget filters.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param pythonMode
     *         the python mode
     *
     * @return the widget filters
     */
    @Override
    public List< TableColumn > getWidgetFilters( String token, String objectId, String widgetId, String pythonMode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            String dataSourceId = DataDashboardUtil.getDataSourceIdFromWidget( widget );
            DataSourceDTO selectedDataSource = null;
            if ( dataSourceId != null ) {
                selectedDataSource = getDataSourceById( entityManager, objectId, dataSourceId );
            }
            return getTableColumnsFromWidget( entityManager, widget, selectedDataSource, objectId, token );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets table columns from widget.
     *
     * @param entityManager
     *         the entity manager
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the table columns from widget
     */
    private List< TableColumn > getTableColumnsFromWidget( EntityManager entityManager, DashboardWidgetDTO widget,
            DataSourceDTO selectedDataSource, String objectId, String token ) {
        List< TableColumn > queryMetadata = null;
        if ( selectedDataSource != null ) {
            queryMetadata = switch ( DashboardDataSourceOptions.getById( selectedDataSource.getSourceType() ) ) {
                case DATABASE -> getTableColumnsFromWidgetFromDatabase( entityManager, widget, ( DatabaseDataSourceDTO ) selectedDataSource,
                        objectId );
                case CSV ->
                        getTableColumnsFromWidgetFromCsv( entityManager, widget, ( CSVDataSourceDTO ) selectedDataSource, objectId, token );
                case EXCEL -> null;
                case JSON -> getTableColumnsFromWidgetFromJson( entityManager, widget, ( JsonDataSourceDTO ) selectedDataSource, objectId,
                        token );
                case SUS_OBJECT ->
                        getColumnsFromWidgetFromSuSObject( entityManager, widget, ( SuSObjectDataSourceDTO ) selectedDataSource, objectId,
                                token );
            };
        }
        return queryMetadata;
    }

    /**
     * Gets table columns from widget from csv.
     *
     * @param entityManager
     *         the entity manager
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the table columns from widget from csv
     */
    private List< TableColumn > getTableColumnsFromWidgetFromCsv( EntityManager entityManager, DashboardWidgetDTO widget,
            CSVDataSourceDTO selectedDataSource, String objectId, String token ) {
        return switch ( WIDGET_SOURCE_TYPE.getById( widget.getSource().getSourceType() ) ) {
            case PYTHON -> DashboardPythonUtil.populateColumnsMetadataForWidgetFilters( widget, objectId );
            case QUERY_BUILDER -> {
                var outputFilePath = Path.of( getPathFromSelectionId( entityManager, selectedDataSource.getFileSelection() ) );
                yield DashboardCSVUtil.populateColumnsMetadataForWidgetFiltersForCsv( outputFilePath );
            }
            case OTHER -> null;
        };
    }

    /**
     * Gets table columns from widget from json.
     *
     * @param entityManager
     *         the entity manager
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the table columns from widget from json
     */
    private List< TableColumn > getTableColumnsFromWidgetFromJson( EntityManager entityManager, DashboardWidgetDTO widget,
            JsonDataSourceDTO selectedDataSource, String objectId, String token ) {
        return switch ( WIDGET_SOURCE_TYPE.getById( widget.getSource().getSourceType() ) ) {
            case PYTHON -> null;
            case QUERY_BUILDER -> {
                var outputFilePath = Path.of( getPathFromSelectionId( entityManager, selectedDataSource.getFileSelection() ) );
                yield DashboardJsonUtil.populateColumnsMetadataForWidgetFiltersForJson( outputFilePath );
            }
            case OTHER -> null;
        };
    }

    /**
     * Gets columns from widget from su s object.
     *
     * @param entityManager
     *         the entity manager
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the columns from widget from su s object
     */
    private List< TableColumn > getColumnsFromWidgetFromSuSObject( EntityManager entityManager, DashboardWidgetDTO widget,
            SuSObjectDataSourceDTO selectedDataSource, String objectId, String token ) {
        return switch ( WIDGET_SOURCE_TYPE.getById( widget.getSource().getSourceType() ) ) {
            case PYTHON -> null;
            case QUERY_BUILDER -> {
                SuSEntity suSEntity = susDAO.getLatestNonDeletedObjectById( entityManager,
                        UUID.fromString( selectedDataSource.getProjectId() ) );
                yield DashboardSuSObjectUtil.populateColumnsMetadataForWidgetFiltersForSuSObject( entityManager, suSEntity, susDAO );
            }
            case OTHER -> null;
        };
    }

    /**
     * Gets table columns from widget from database.
     *
     * @param entityManager
     *         the entity manager
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     *
     * @return the table columns from widget from database
     */
    private List< TableColumn > getTableColumnsFromWidgetFromDatabase( EntityManager entityManager, DashboardWidgetDTO widget,
            DatabaseDataSourceDTO selectedDataSource, String objectId ) {
        return switch ( WIDGET_SOURCE_TYPE.getById( widget.getSource().getSourceType() ) ) {
            case PYTHON -> null;
            case QUERY_BUILDER -> {
                WidgetQueryBuilderSource widgetSource = ( WidgetQueryBuilderSource ) widget.getSource();
                yield DataDashboardUtil.populateTableColumnsForWidgetFiltersForDatabase( widgetSource, selectedDataSource );
            }
            case OTHER -> null;
        };
    }

    /**
     * Gets other data source fields.
     *
     * @param tokenFromGeneralHeader
     *         the token from general header
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param otherOption
     *         the other option
     *
     * @return the other data source fields
     */
    @Override
    public UIForm getOtherDataSourceFields( String tokenFromGeneralHeader, String objectId, String widgetId, String otherOption ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            var dataSourceEntityList = dashboardEntity.getDataSources();
            var widget = getWidgetById( entityManager, objectId, widgetId );
            List< DataSourceDTO > dataSourceDTOS;
            dataSourceDTOS = new ArrayList<>( dataSourceEntityList.size() );
            List< DataSourceDTO > finalDataSourceDTOS = dataSourceDTOS;
            dataSourceEntityList.forEach( entity -> finalDataSourceDTOS.add( prepareDataSourceDTOFromEntity( entityManager, entity ) ) );
            List< UIFormItem > items = switch ( OTHER_WIDGET_SOURCE.getById( otherOption ) ) {
                case METAL_BASE -> prepareMetalBaseForm( entityManager, finalDataSourceDTOS, objectId, widgetId );
                case MATERIAL_INSPECTION -> prepareMaterialInspectorForm( entityManager, finalDataSourceDTOS, objectId, widgetId );
                case PST -> preparePstForm( widget, dataSourceEntityList );
                case VMCL -> prepareVmclForm( finalDataSourceDTOS, widget );
            };
            return GUIUtils.createFormFromItems( items );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare metal base form list.
     *
     * @param entityManager
     *         the entity manager
     * @param sources
     *         the sources
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the list
     */
    private List< UIFormItem > prepareMetalBaseForm( EntityManager entityManager, List< DataSourceDTO > sources, String objectId,
            String widgetId ) {
        var items = GUIUtils.prepareForm( true, new WidgetMetalBaseSource() );
        for ( Iterator< UIFormItem > iterator = items.iterator(); iterator.hasNext(); ) {
            UIFormItem item = iterator.next();
            switch ( item.getName() ) {
                case METAL_BASE_FIELDS.MAT_DB_SOURCE -> DashboardMetalBaseUtil.populateDataSourceList( ( SelectFormItem ) item, sources );
                case METAL_BASE_FIELDS.MAT_DB_SCHEMA ->
                        DashboardMetalBaseUtil.setBindToForMatDbSchema( ( SelectFormItem ) item, objectId, widgetId );
                case METAL_BASE_FIELDS.GS_MATERIAL_NAME ->
                        DashboardMetalBaseUtil.setBindToAndBindFromForGsName( ( SelectFormItem ) item, objectId, widgetId );
                case WIDGET_FIELDS.DATA_SOURCE_TYPE, WIDGET_FIELDS.DATA_SOURCE, WIDGET_FIELDS.NAME, METAL_BASE_FIELDS.PLOT_DATA,
                     METAL_BASE_FIELDS.ORDER_NR, METAL_BASE_FIELDS.SUPPLIER_NAME, METAL_BASE_FIELDS.SURFACE_FINISH -> iterator.remove();
                case METAL_BASE_FIELDS.ANGLE ->
                        DashboardMetalBaseUtil.setOptionsAndBindFromForAngles( ( SelectFormItem ) item, objectId, widgetId );
                case METAL_BASE_FIELDS.TEST_CREATED_BETWEEN, METAL_BASE_FIELDS.BULGE, METAL_BASE_FIELDS.MAX_THICKNESS,
                     METAL_BASE_FIELDS.MIN_THICKNESS, METAL_BASE_FIELDS.IO_STATUS, METAL_BASE_FIELDS.SHOW_ONLY_IN_REVIEW,
                     METAL_BASE_FIELDS.FLOW_CURVE -> item.setBindFrom(
                        BIND_FROM_URLS.METAL_BASE.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                                .replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ).replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() ) );
                case METAL_BASE_FIELDS.QUANTILE_LIMIT -> DashboardMetalBaseUtil.setDefaultValueForQuantileLimit( item );
                case METAL_BASE_FIELDS.CHARACTERISTICS -> DashboardMetalBaseUtil.setOptionsForCharacteristics( ( SelectFormItem ) item );

            }
            if ( FieldTypes.SELECTION.getType().equals( item.getType() ) ) {
                GUIUtils.setLiveSearchInSelectItem( ( SelectFormItem ) item );
            }
        }
        return items;
    }

    /**
     * Prepare pst form list.
     *
     * @param widget
     *         the widget
     * @param dataSourceEntityList
     *         the data source entity list
     *
     * @return the list
     */
    private List< UIFormItem > preparePstForm( DashboardWidgetDTO widget, List< DataSourceEntity > dataSourceEntityList ) {
        List< UIFormItem > pstSourceFields;
        if ( widget.getSource() instanceof WidgetPstSource pstSource ) {
            pstSourceFields = GUIUtils.prepareForm( true, pstSource );
        } else {
            pstSourceFields = GUIUtils.prepareForm( true, new WidgetPstSource() );
        }
        for ( Iterator< UIFormItem > iterator = pstSourceFields.iterator(); iterator.hasNext(); ) {
            var item = iterator.next();
            switch ( item.getName() ) {
                case WIDGET_FIELDS.DATA_SOURCE_TYPE -> iterator.remove();
                case DataDashboardConstants.PST_FIELDS.ACE_LOUNGE_CSV -> {
                    ( ( SelectFormItem ) item ).setOptions( dataSourceEntityList.stream()
                            .filter( ds -> DashboardDataSourceOptions.CSV.getId().equals( ds.getSourceType() ) )
                            .map( ds -> new SelectOptionsUI( ds.getId().toString(), ds.getName() ) ).toList() );
                    ( ( SelectFormItem ) item ).setCanBeEmpty( true );
                }
                case DataDashboardConstants.PST_FIELDS.APL_UPDATES, DataDashboardConstants.PST_FIELDS.KS_UPDATES,
                     DataDashboardConstants.PST_FIELDS.BMW_UPDATES -> {
                    ( ( SelectFormItem ) item ).setOptions( dataSourceEntityList.stream()
                            .filter( ds -> DashboardDataSourceOptions.EXCEL.getId().equals( ds.getSourceType() ) )
                            .map( ds -> new SelectOptionsUI( ds.getId().toString(), ds.getName() ) ).toList() );
                    ( ( SelectFormItem ) item ).setCanBeEmpty( true );
                }
            }
        }
        return pstSourceFields;
    }

    /**
     * Prepare vMcl form list.
     *
     * @param widget
     *         the widget
     *
     * @return the list
     */

    private List< UIFormItem > prepareVmclForm( List< DataSourceDTO > sources, DashboardWidgetDTO widget ) {

        var items = GUIUtils.prepareForm( true, new WidgetVmclSource() );
        for ( Iterator< UIFormItem > iterator = items.iterator(); iterator.hasNext(); ) {
            UIFormItem item = iterator.next();
            switch ( item.getName() ) {
                case DataDashboardConstants.VMCL_FIELDS.VMCL_DATA_SOURCE ->
                        DashboardVMclUtil.populateDataSourceList( ( SelectFormItem ) item, sources );
                case WIDGET_FIELDS.NAME, WIDGET_FIELDS.DATA_SOURCE_TYPE -> iterator.remove();
            }
        }
        return items;
    }

    /**
     * Gets select options for material inspector.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldName
     *         the field name
     * @param fieldValue
     *         the field value
     *
     * @return the select options for material inspector
     */
    @Override
    public List< SelectOptionsUI > getSelectOptionsForMaterialInspector( String token, String objectId, String widgetId, String fieldName,
            String fieldValue ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            if ( widget.getSource() instanceof WidgetMaterialInspectorSource widgetSource ) {
                var dataSourceId = MATERIAL_INSPECTOR_FIELDS.MAT_DB_SCHEMA.equals( fieldName ) ? fieldValue : widgetSource.getMatDbSource();
                DatabaseDataSourceDTO dataSource = ( DatabaseDataSourceDTO ) getDataSourceById( entityManager, objectId, dataSourceId );
                return switch ( fieldName ) {
                    case MATERIAL_INSPECTOR_FIELDS.MAT_DB_SCHEMA -> DashboardMaterialInspectorUtil.getSelectOptionsForSchema( dataSource );
                    case MATERIAL_INSPECTOR_FIELDS.GS_MATERIAL_NAME ->
                            DashboardMaterialInspectorUtil.getSelectOptionsForMaterialName( dataSource, fieldValue );
                    case MATERIAL_INSPECTOR_FIELDS.THICKNESS ->
                            DashboardMaterialInspectorUtil.getSelectOptionsForThickness( dataSource, widgetSource, fieldValue );
                    default -> throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), fieldName ) );
                };
            }
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets select options for metal base.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldName
     *         the field name
     * @param fieldValue
     *         the field value
     *
     * @return the select options for metal base
     */
    @Override
    public List< SelectOptionsUI > getSelectOptionsForMetalBase( String token, String objectId, String widgetId, String fieldName,
            String fieldValue ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            if ( widget.getSource() instanceof WidgetMetalBaseSource widgetSource ) {
                var dataSourceId = METAL_BASE_FIELDS.MAT_DB_SCHEMA.equals( fieldName ) ? fieldValue : widgetSource.getMatDbSource();
                DatabaseDataSourceDTO dataSource = ( DatabaseDataSourceDTO ) getDataSourceById( entityManager, objectId, dataSourceId );
                return switch ( fieldName ) {
                    case METAL_BASE_FIELDS.MAT_DB_SCHEMA -> DashboardMetalBaseUtil.getSelectOptionsForSchema( dataSource );
                    case METAL_BASE_FIELDS.GS_MATERIAL_NAME ->
                            DashboardMetalBaseUtil.getSelectOptionsForMaterialName( dataSource, fieldValue );
                    case METAL_BASE_FIELDS.SUPPLIER_NAME ->
                            DashboardMetalBaseUtil.getSelectOptionsForSupplierName( dataSource, widgetSource, fieldValue, fieldName );
                    case METAL_BASE_FIELDS.SURFACE_FINISH ->
                            DashboardMetalBaseUtil.getSelectOptionsForSurfaceFinish( dataSource, widgetSource, fieldValue, fieldName );
                    default -> throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), fieldName ) );
                };
            }
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets dynamic fields for material inspector.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldName
     *         the field name
     * @param fieldValue
     *         the field value
     *
     * @return the dynamic fields for material inspector
     */
    @Override
    public UIForm getDynamicFieldsForMaterialInspector( String token, String objectId, String widgetId, String fieldName,
            String fieldValue ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            if ( widget.getSource() instanceof WidgetMaterialInspectorSource widgetSource ) {
                var dataSourceId = MATERIAL_INSPECTOR_FIELDS.MAT_DB_SCHEMA.equals( fieldName ) ? fieldValue : widgetSource.getMatDbSource();
                DatabaseDataSourceDTO dataSource = ( DatabaseDataSourceDTO ) getDataSourceById( entityManager, objectId, dataSourceId );
                return switch ( fieldName ) {
                    case MATERIAL_INSPECTOR_FIELDS.THICKNESS ->
                            DashboardMaterialInspectorUtil.getThicknessRelatedFields( objectId, widgetId, fieldValue, widgetSource,
                                    dataSource, fieldName );
                    case MATERIAL_INSPECTOR_FIELDS.SURFACE_FINISH ->
                            DashboardMaterialInspectorUtil.getSurfaceFinishRelatedFields( objectId, widgetId, fieldValue, widgetSource,
                                    dataSource, fieldName );
                    case MATERIAL_INSPECTOR_FIELDS.CHARGE ->
                            DashboardMaterialInspectorUtil.getChargeRelatedFields( objectId, widgetId, fieldValue, widgetSource, dataSource,
                                    fieldName );
                    case MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME ->
                            DashboardMaterialInspectorUtil.getLaboratoryNameRelatedFields( objectId, widgetId, fieldValue, widgetSource,
                                    dataSource, fieldName );
                    case MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME ->
                            DashboardMaterialInspectorUtil.getSupplierNameRelatedFields( objectId, widgetId, fieldValue, widgetSource,
                                    dataSource, fieldName );
                    case MATERIAL_INSPECTOR_FIELDS.STATUS ->
                            DashboardMaterialInspectorUtil.getStatusRelatedFields( objectId, widgetId, fieldValue, widgetSource, dataSource,
                                    fieldName );
                    case MATERIAL_INSPECTOR_FIELDS.ORDER_NR ->
                            DashboardMaterialInspectorUtil.getOrderNrRelatedFields( objectId, widgetId, fieldValue, widgetSource,
                                    dataSource, fieldName );
                    default -> throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), fieldName ) );
                };
            }
            return new UIForm();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets dynamic fields for metal base.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldName
     *         the field name
     * @param fieldValue
     *         the field value
     *
     * @return the dynamic fields for metal base
     */
    @Override
    public UIForm getDynamicFieldsForMetalBase( String token, String objectId, String widgetId, String fieldName, String fieldValue ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            if ( widget.getSource() instanceof WidgetMetalBaseSource widgetSource ) {
                var dataSourceId = METAL_BASE_FIELDS.MAT_DB_SCHEMA.equals( fieldName ) ? fieldValue : widgetSource.getMatDbSource();
                DatabaseDataSourceDTO dataSource = ( DatabaseDataSourceDTO ) getDataSourceById( entityManager, objectId, dataSourceId );
                return switch ( fieldName ) {
                    case METAL_BASE_FIELDS.GS_MATERIAL_NAME ->
                            DashboardMetalBaseUtil.getMaterialNameRelatedFields( objectId, widgetId, fieldValue, widgetSource, dataSource,
                                    fieldName );
                    case METAL_BASE_FIELDS.SURFACE_FINISH ->
                            DashboardMetalBaseUtil.getSurfaceFinishRelatedFields( objectId, widgetId, fieldValue, widgetSource, dataSource,
                                    fieldName );
                    case METAL_BASE_FIELDS.ORDER_NR ->
                            DashboardMetalBaseUtil.getOrderNrRelatedFields( objectId, widgetId, fieldValue, widgetSource, dataSource,
                                    fieldName );
                    case METAL_BASE_FIELDS.ANGLE, METAL_BASE_FIELDS.TEST_CREATED_BETWEEN, METAL_BASE_FIELDS.MIN_THICKNESS,
                         METAL_BASE_FIELDS.MAX_THICKNESS, METAL_BASE_FIELDS.IO_STATUS, METAL_BASE_FIELDS.FLOW_CURVE,
                         METAL_BASE_FIELDS.BULGE, METAL_BASE_FIELDS.SHOW_ONLY_IN_REVIEW ->
                            DashboardMetalBaseUtil.getOrderNrField( objectId, widgetId, fieldValue, widgetSource, dataSource, fieldName );
                    default -> throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), fieldName ) );
                };
            }
            return new UIForm();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Perform widget action object.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param action
     *         the action
     * @param data
     *         the data
     *
     * @return the object
     */
    @Override
    public Object performWidgetAction( String token, String objectId, String widgetId, String action, String data ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            var dsEntities = dashboardEntity.getDataSources();
            var widget = getWidgetById( entityManager, objectId, widgetId );
            return switch ( WIDGET_SOURCE_TYPE.getById( widget.getSource().getSourceType() ) ) {
                case PYTHON, QUERY_BUILDER -> null;
                case OTHER -> switch ( OTHER_WIDGET_SOURCE.getById( ( ( WidgetOtherSource ) widget.getSource() ).getName() ) ) {
                    case MATERIAL_INSPECTION -> {
                        DatabaseDataSourceDTO selectedDataSource = ( DatabaseDataSourceDTO ) getDataSourceById( entityManager, objectId,
                                ( ( WidgetMaterialInspectorSource ) widget.getSource() ).getMatDbSource() );
                        yield DashboardMaterialInspectorUtil.performMaterialInspectionAction( objectId, widgetId, action, widget,
                                selectedDataSource );
                    }
                    case METAL_BASE -> {
                        DatabaseDataSourceDTO selectedDataSource = ( DatabaseDataSourceDTO ) getDataSourceById( entityManager, objectId,
                                ( ( WidgetMetalBaseSource ) widget.getSource() ).getMatDbSource() );
                        yield DashboardMetalBaseUtil.performMetalBaseAction( objectId, METAL_BASE_ACTION.getByName( action ), widget,
                                selectedDataSource, data, TokenizedLicenseUtil.getNotNullUser( token ) );
                    }
                    case PST -> {
                        resolveFilePathsForPstWidgetSource( entityManager, widget, dsEntities );
                        yield DashboardPSTUtil.performPSTAction( widget, objectId, PST_ACTIONS.getByName( action ), data, token );
                    }
                    case VMCL -> null;
                };
            };
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets download file path.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param action
     *         the action
     * @param token
     *         the token
     *
     * @return the download file path
     */
    @Override
    public Path getDownloadFilePath( String objectId, String widgetId, String action, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            return switch ( WIDGET_SOURCE_TYPE.getById( widget.getSource().getSourceType() ) ) {
                case PYTHON, QUERY_BUILDER -> null;
                case OTHER -> switch ( OTHER_WIDGET_SOURCE.getById( ( ( WidgetOtherSource ) widget.getSource() ).getName() ) ) {
                    case METAL_BASE -> {
                        var metalBaseAction = METAL_BASE_ACTION.getByName( action );
                        yield switch ( metalBaseAction ) {
                            case export_data, export_graphics ->
                                    DashboardMetalBaseUtil.getDownloadFilePath( widget, objectId, metalBaseAction.name() );
                            default -> throw new IllegalArgumentException( metalBaseAction.name() );
                        };
                    }
                    case MATERIAL_INSPECTION -> DashboardMaterialInspectorUtil.getExportFileFromOutputFile( objectId, widget, action );
                    case PST -> {
                        var pstAction = PST_ACTIONS.getByName( action );
                        yield switch ( pstAction ) {
                            case export_differences -> DashboardPSTUtil.getDownloadFilePath( widget, objectId, pstAction.name() );
                            default -> throw new IllegalArgumentException( pstAction.name() );
                        };
                    }
                    case VMCL -> null;
                };
            };

        } finally {
            entityManager.close();
        }
    }

    /**
     * Add widget to group dashboard widget dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     * @param widgetId
     *         the widget id
     *
     * @return the dashboard widget dto
     */
    @Override
    public DashboardWidgetDTO addWidgetToGroup( String token, String objectId, String groupId, String widgetId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataDashboardEntity dashboard = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            validateSourceAndTargetIds( groupId, widgetId, dashboard );
            var groupEntity = dashboard.getWidgets().stream().filter( widget -> widget.getId().toString().equals( groupId ) ).findFirst()
                    .get();
            DashboardWidgetDTO groupDto = DataDashboardUtil.prepareWidgetDTOFromEntity( groupEntity );
            var widgetEntity = dashboard.getWidgets().stream().filter( widget -> widget.getId().toString().equals( widgetId ) ).findFirst()
                    .get();
            DashboardWidgetDTO widgetDTO = DataDashboardUtil.prepareWidgetDTOFromEntity( widgetEntity );
            if ( widgetDTO.getType().equals( WIDGET_TYPE.GROUP.getId() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.CAN_NOT_ADD_GROUP_TO_GROUP.getKey() ) );
            }
            var groupOptions = ( GroupWidgetOptions ) groupDto.getOptions();
            if ( groupOptions.getWidgets() == null ) {
                groupOptions.setWidgets( new HashSet<>() );
            }
            if ( groupOptions.getWidgets().isEmpty() ) {
                //adding 1st widget to group. Assign widget's source to the group and copy widget's cache to group's cache
                groupDto.getConfiguration().put( "source", widgetDTO.getSource() );
                DashboardCacheUtil.copyWidgetCache( dashboard, widgetDTO, groupDto );
            } else {
                //group already has a source and >= 1 widgets. Assign group's source to the widget and copy group's cache to widget's cache
                widgetDTO.getConfiguration().put( "source", groupDto.getSource() );
                DashboardCacheUtil.copyWidgetCache( dashboard, groupDto, widgetDTO );
            }
            groupOptions.getWidgets().add( widgetDTO.getId() );
            groupDto.getConfiguration().put( "widgets", groupOptions.getWidgets() );
            groupDto.getConfiguration().put( "relation", WIDGET_RELATION.IS_GROUP );
            widgetDTO.getConfiguration().put( "groupId", groupDto.getId() );
            widgetDTO.getConfiguration().put( "relation", WIDGET_RELATION.IS_IN_GROUP );
            updateWidgetEntity( entityManager, groupEntity, JsonUtils.toJson( groupDto.getConfiguration() ), token );
            updateWidgetEntity( entityManager, widgetEntity, JsonUtils.toJson( widgetDTO.getConfiguration() ), token );
            susDAO.saveOrUpdateObject( entityManager, dashboard );
            return groupDto;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Validate source and target ids.
     *
     * @param groupId
     *         the group id
     * @param widgetId
     *         the widget id
     * @param dashboard
     *         the dashboard
     */
    private static void validateSourceAndTargetIds( String groupId, String widgetId, DataDashboardEntity dashboard ) {
        if ( CollectionUtils.isEmpty( dashboard.getWidgets() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_X_NOT_EXIST_WITH_ID_Y.getKey(), "Widget", groupId ) );
        }
        if ( dashboard.getWidgets().stream().noneMatch(
                widget -> widget.getId().toString().equals( widgetId ) && !widget.getWidgetType().equals( WIDGET_TYPE.GROUP.getId() ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_X_NOT_EXIST_WITH_ID_Y.getKey(), "Widget", groupId ) );
        }
        if ( dashboard.getWidgets().stream().noneMatch(
                widget -> widget.getId().toString().equals( groupId ) && widget.getWidgetType().equals( WIDGET_TYPE.GROUP.getId() ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_X_NOT_EXIST_WITH_ID_Y.getKey(), "Group", groupId ) );
        }
    }

    /**
     * Remove widget from group dashboard widget dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     * @param widgetId
     *         the widget id
     *
     * @return the dashboard widget dto
     */
    @Override
    public DashboardWidgetDTO removeWidgetFromGroup( String token, String objectId, String groupId, String widgetId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataDashboardEntity dashboard = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            validateSourceAndTargetIds( groupId, widgetId, dashboard );
            var groupEntity = dashboard.getWidgets().stream().filter( widget -> widget.getId().toString().equals( groupId ) ).findFirst()
                    .get();
            DashboardWidgetDTO groupDto = DataDashboardUtil.prepareWidgetDTOFromEntity( groupEntity );
            var widgetEntity = dashboard.getWidgets().stream().filter( widget -> widget.getId().toString().equals( widgetId ) ).findFirst()
                    .get();
            DashboardWidgetDTO widgetDTO = DataDashboardUtil.prepareWidgetDTOFromEntity( widgetEntity );
            var groupOptions = ( GroupWidgetOptions ) groupDto.getOptions();
            if ( groupOptions.getWidgets() == null || !groupOptions.getWidgets().contains( widgetDTO.getId() ) ) {
                throw new SusException();
            }
            groupOptions.getWidgets().remove( widgetDTO.getId() );
            groupDto.getConfiguration().put( "widgets", groupOptions.getWidgets() );
            widgetDTO.getConfiguration().put( "groupId", null );
            widgetDTO.getConfiguration().put( "source", groupDto.getSource() );
            widgetDTO.getConfiguration().put( "relation", WIDGET_RELATION.IS_NOT_IN_GROUP );
            DashboardCacheUtil.copyWidgetCache( dashboard, groupDto, widgetDTO );
            updateWidgetEntity( entityManager, groupEntity, JsonUtils.toJson( groupDto.getConfiguration() ), token );
            updateWidgetEntity( entityManager, widgetEntity, JsonUtils.toJson( widgetDTO.getConfiguration() ), token );
            susDAO.saveOrUpdateObject( entityManager, dashboard );
            return groupDto;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets widgets in group.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     *
     * @return the widgets in group
     */
    @Override
    public List< DashboardWidgetDTO > getWidgetsInGroup( String token, String objectId, String groupId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataDashboardEntity dashboard = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            if ( dashboard.getWidgets() == null || dashboard.getWidgets().stream()
                    .noneMatch( widgetEntity -> widgetEntity.getId().toString().equals( groupId ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), groupId ) );
            }
            var groupEntity = dashboard.getWidgets().stream().filter( widget -> widget.getId().toString().equals( groupId ) ).findFirst()
                    .get();
            DashboardWidgetDTO groupDto = DataDashboardUtil.prepareWidgetDTOFromEntity( groupEntity );
            var groupOptions = ( GroupWidgetOptions ) groupDto.getOptions();
            if ( CollectionUtils.isEmpty( groupOptions.getWidgets() ) ) {
                return new ArrayList<>();
            }
            return dashboard.getWidgets().stream().filter( widget -> groupOptions.getWidgets().contains( widget.getId().toString() ) )
                    .map( DataDashboardUtil::prepareWidgetDTOFromEntity ).toList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Un group widgets boolean.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     *
     * @return the boolean
     */
    @Override
    public Boolean unGroupWidgets( String token, String objectId, String groupId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            DataDashboardEntity dashboard = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            if ( dashboard.getWidgets() == null || dashboard.getWidgets().stream().noneMatch(
                    widgetEntity -> widgetEntity.getId().toString().equals( groupId ) && widgetEntity.getWidgetType()
                            .equals( WIDGET_TYPE.GROUP.getId() ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), groupId ) );
            }
            var widgetToDelete = dashboard.getWidgets().stream().filter( widgetEntity -> widgetEntity.getId().toString().equals( groupId ) )
                    .findFirst().get();
            dashboard.getWidgets().remove( widgetToDelete );
            removeGroupingFromContainedWidgets( token, widgetToDelete, dashboard, entityManager );
            DashboardCacheUtil.deleteWidgetDirectoryFromCache( dashboard.getComposedId(), UUID.fromString( groupId ) );
            susDAO.saveOrUpdateObject( entityManager, dashboard );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            return false;
        } finally {
            entityManager.close();
        }
        return true;
    }

    /**
     * Gets line widget options by type.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param lineWidgetType
     *         the line widget type
     *
     * @return the line widget options by type
     */
    @Override
    public UIForm getLineWidgetOptionsByType( String token, String objectId, String widgetId, String lineWidgetType ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            String dataSourceId = DataDashboardUtil.getDataSourceIdFromWidget( widget );
            List< UIFormItem > formItems = switch ( LINE_WIDGET_OPTIONS.getById( lineWidgetType ) ) {
                case MULTIPLE_X_SINGLE_Y ->
                        GUIUtils.getFormItemsByFields( LineWidgetMultipleXOptions.class, WIDGET_FIELDS.X_AXIS, WIDGET_FIELDS.Y_AXIS,
                                WIDGET_FIELDS.STYLING, WIDGET_FIELDS.POINT_SYMBOL );
                case MULTIPLE_Y_SINGLE_X ->
                        GUIUtils.getFormItemsByFields( LineWidgetMultipleYOptions.class, WIDGET_FIELDS.X_AXIS, WIDGET_FIELDS.Y_AXIS,
                                WIDGET_FIELDS.STYLING, WIDGET_FIELDS.POINT_SYMBOL );
                case MULTIPLE_X_MULTIPLE_Y ->
                        GUIUtils.getFormItemsByFields( LineWidgetMultipleXYOptions.class, WIDGET_FIELDS.X_AXIS, WIDGET_FIELDS.Y_AXIS,
                                WIDGET_FIELDS.STYLING, WIDGET_FIELDS.POINT_SYMBOL );
            };
            DataSourceDTO selectedDataSource = null;
            if ( dataSourceId != null ) {
                selectedDataSource = getDataSourceById( entityManager, objectId, dataSourceId );
            }
            populateOptionsFields( entityManager, formItems, widget, selectedDataSource, objectId, token, PYTHON_MODES.REGENERATE );
            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets chart options field for mix chart.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the chart options field for mix chart
     */
    @Override
    public UIForm getCurveOptionsFieldForMixChart( String token, String objectId, String widgetId ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.getFormItemByField( MixChartCurveOptions.class, "curveType" );
        DataDashboardUtil.setBindToForMixChartChartOptions( item, widgetId, objectId );
        return GUIUtils.createFormFromItems( Collections.singletonList( item ) );
    }

    /**
     * Gets value options field for radar chart.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the value options field for radar chart
     */
    @Override
    public UIForm getValueOptionsFieldForRadarChart( String token, String objectId, String widgetId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            String dataSourceId = DataDashboardUtil.getDataSourceIdFromWidget( widget );
            List< UIFormItem > formItems = GUIUtils.getFormItemsByFields( RadarWidgetOptions.class, RADAR_WIDGET_FIELDS.VALUE,
                    WIDGET_FIELDS.POINT_SYMBOL, WIDGET_FIELDS.STYLING );
            DataSourceDTO selectedDataSource = null;
            if ( dataSourceId != null ) {
                selectedDataSource = getDataSourceById( entityManager, objectId, dataSourceId );
            }
            populateOptionsFields( entityManager, formItems, widget, selectedDataSource, objectId, token, PYTHON_MODES.REGENERATE );
            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets chart options related fields for mix chart.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param option
     *         the option
     *
     * @return the chart options related fields for mix chart
     */
    @Override
    public UIForm getCurveOptionsRelatedFieldsForMixChart( String token, String objectId, String widgetId, String option ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            String dataSourceId = DataDashboardUtil.getDataSourceIdFromWidget( widget );
            List< UIFormItem > formItems = DataDashboardUtil.getFormItemsForCurveInMixChart( option );
            DataSourceDTO selectedDataSource = null;
            if ( dataSourceId != null ) {
                selectedDataSource = getDataSourceById( entityManager, objectId, dataSourceId );
            }
            populateOptionsFields( entityManager, formItems, widget, selectedDataSource, objectId, token, PYTHON_MODES.REGENERATE );
            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets text input fields.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param textInputMethod
     *         the text input method
     *
     * @return the text input fields
     */
    @Override
    public UIForm getTextInputFields( String token, String objectId, String widgetId, String textInputMethod ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermission( entityManager, token, objectId );
            var widget = getWidgetById( entityManager, objectId, widgetId );
            String dataSourceId = DataDashboardUtil.getDataSourceIdFromWidget( widget );
            List< UIFormItem > formItems = switch ( TEXT_WIDGET_INPUT_METHOD.getById( textInputMethod ) ) {
                case USER -> GUIUtils.getFormItemsByFields( widget.getOptions(), TEXT_WIDGET_FIELDS.DISPLAY_VALUE );
                case SELECT ->
                        GUIUtils.getFormItemsByFields( widget.getOptions(), TEXT_WIDGET_FIELDS.COLUMN, TEXT_WIDGET_FIELDS.COLUMN_TITLE );
            };
            DataSourceDTO selectedDataSource = null;
            if ( dataSourceId != null ) {
                selectedDataSource = getDataSourceById( entityManager, objectId, dataSourceId );
            }
            populateOptionsFields( entityManager, formItems, widget, selectedDataSource, objectId, token, PYTHON_MODES.REGENERATE );
            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update data source cache boolean.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the boolean
     */
    @Override
    public Boolean updateDataSourceCache( String token, String objectId, String dataSourceId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkWritePermission( entityManager, token, objectId );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            if ( dashboardEntity.getDataSources() == null || dashboardEntity.getDataSources().stream()
                    .noneMatch( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), dataSourceId ) );
            }
            var matchingDataSourceEntity = dashboardEntity.getDataSources().stream()
                    .filter( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ).findFirst().get();
            dashboardEntity.getDataSources().remove( matchingDataSourceEntity );
            if ( matchingDataSourceEntity.getSourceType().equals( DashboardDataSourceOptions.SUS_OBJECT.getId() ) ) {
                prepareCache( entityManagerFactory,
                        ( SuSObjectDataSourceDTO ) prepareDataSourceDTOFromEntity( entityManager, matchingDataSourceEntity ), objectId );
                return true;
            }
            return false;
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

    @Override
    public TableUI getTableWidgetUi( String token, String objectId, String widgetId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        DynamicQueryResponse response = new DynamicQueryResponse();
        try {
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            if ( dashboardEntity.getWidgets().stream().noneMatch( widget -> widget.getId().toString().equalsIgnoreCase( widgetId ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), widgetId ) );
            }
            var matchingWidget = dashboardEntity.getWidgets().stream()
                    .filter( widget -> widget.getId().toString().equalsIgnoreCase( widgetId ) ).findFirst().get();
            DashboardWidgetDTO dashboardWidgetDTO = DataDashboardUtil.prepareWidgetDTOFromEntity( matchingWidget );
            String dataSourceId = DataDashboardUtil.getDataSourceIdFromWidget( dashboardWidgetDTO );
            var matchingDataSource = prepareDataSourceDTOFromEntity( entityManager,
                    dashboardEntity.getDataSources().stream().filter( ds -> ds.getId().toString().equals( dataSourceId ) ).findFirst()
                            .get() );
            List< TableColumn > tableColumns = new ArrayList<>();

            if ( matchingDataSource instanceof CSVDataSourceDTO csvDataSourceDTO ) {
                csvDataSourceDTO.setFilePath( getPathFromSelectionId( entityManager, csvDataSourceDTO.getFileSelection() ) );
            } else if ( matchingDataSource instanceof JsonDataSourceDTO jsonDataSourceDTO ) {
                jsonDataSourceDTO.setFilePath( getPathFromSelectionId( entityManager, jsonDataSourceDTO.getFileSelection() ) );
            }
            if ( dashboardWidgetDTO.getOptions() instanceof TableWidgetOptions ) {
                response = switch ( WIDGET_SOURCE_TYPE.getById( dashboardWidgetDTO.getSource().getSourceType() ) ) {
                    case PYTHON ->
                            DashboardPythonUtil.getQueryResponseFromPythonOutput( dashboardEntity.getComposedId(), dashboardWidgetDTO );
                    case QUERY_BUILDER -> DashboardQueryBuilder.getDataForTableWidget( matchingDataSource, dashboardWidgetDTO );

                    case OTHER -> null;
                };
            }

            for ( var columnRecord : response.getMetadata().entrySet() ) {
                TableColumn tableColumn = new TableColumn( columnRecord.getKey(), columnRecord.getKey(), columnRecord.getKey(),
                        DataDashboardUtil.mapTYPE_NAMEToFilterType( columnRecord.getValue().type() ) );
                tableColumns.add( tableColumn );

            }
            return new TableUI( tableColumns );
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

    public FilteredResponse< Map< String, Object > > getListForTableWidget( String token, String objectId, String widgetId,
            String filterJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                UUID.fromString( objectId ) );
        if ( dashboardEntity.getWidgets().stream().noneMatch( widget -> widget.getId().toString().equalsIgnoreCase( widgetId ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), widgetId ) );
        }
        var matchingWidget = dashboardEntity.getWidgets().stream()
                .filter( widget -> widget.getId().toString().equalsIgnoreCase( widgetId ) ).findFirst().get();
        DashboardWidgetDTO dashboardWidgetDTO = DataDashboardUtil.prepareWidgetDTOFromEntity( matchingWidget );
        String dataSourceId = DataDashboardUtil.getDataSourceIdFromWidget( dashboardWidgetDTO );
        var matchingDataSource = prepareDataSourceDTOFromEntity( entityManager,
                dashboardEntity.getDataSources().stream().filter( ds -> ds.getId().toString().equals( dataSourceId ) ).findFirst().get() );
        FiltersDTO filters = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
        String tableName = ConstantsString.EMPTY_STRING;
        if ( DashboardDataSourceOptions.DATABASE.getId().equalsIgnoreCase( matchingDataSource.getSourceType() ) ) {
            String table = ( ( WidgetQueryBuilderSource ) dashboardWidgetDTO.getSource() ).getTable();
            tableName = table.substring( table.indexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE );
        }
        List< Map< String, Object > > listOfRecords = switch ( WIDGET_SOURCE_TYPE.getById(
                dashboardWidgetDTO.getSource().getSourceType() ) ) {
            case QUERY_BUILDER ->
                    DashboardQueryBuilder.getFilteredRecordsForTableWidget( tableName, filters, matchingDataSource, dashboardWidgetDTO );
            case PYTHON -> DashboardPythonUtil.getDataFromPythonForTableWidget( dashboardEntity.getComposedId(), dashboardWidgetDTO );
            case OTHER -> null;
        };

        return PaginationUtil.constructFilteredResponse( filters, listOfRecords );
    }

    /**
     * Prepare cache.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     * @param dataSource
     *         the data source
     * @param objectId
     *         the object id
     */
    private void prepareCache( EntityManagerFactory entityManagerFactory, SuSObjectDataSourceDTO dataSource, String objectId ) {
        DashboardCacheUtil.createDataSourceDirectoryInCache( objectId, dataSource.getId(), dataSource.getVersionId() );
        var dataSourceCacheDirectory = DashboardCacheUtil.getDataSourceDirectoryInCache(
                new VersionPrimaryKey( UUID.fromString( objectId ), dataSource.getVersionId() ), UUID.fromString( dataSource.getId() ) );
        var languages = PropertiesManager.getRequiredlanguages();
        stopScheduler();
        scheduler = Executors.newScheduledThreadPool( 1 );
        for ( var langKey : languages.keySet() ) {
            try {
                String cacheThreadKey =
                        dataSourceCacheDirectory.toAbsolutePath() + File.separator + dataSource.getProjectId() + ConstantsString.UNDERSCORE
                                + langKey;
                log.info( "generating cache for sus object at path {}", cacheThreadKey );
                if ( DashboardSuSObjectUtil.checkIfGenerationIsInProgress( cacheThreadKey ) ) {
                    log.info( "cache generation already in progress for {}", cacheThreadKey );
                    DashboardSuSObjectUtil.waitForRunningThread( cacheThreadKey );
                    log.info( "previous call for cache generation completed for {}", cacheThreadKey );
                }
                Runnable runnable = () -> {
                    Callable< Void > callable = () -> createOrUpdateDataSourceCache( entityManagerFactory, dataSource, langKey,
                            dataSourceCacheDirectory, cacheThreadKey, objectId );
                    DashboardSuSObjectUtil.submitCacheGenerationFuture( cacheThreadKey, callable );
                    log.info( "cache generation completed sus object at path {}", cacheThreadKey );
                };
                scheduler.scheduleWithFixedDelay( runnable, 0, Long.parseLong( dataSource.getUpdateInterval() ), TimeUnit.HOURS );

            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e.getMessage() );
            }
        }
    }

    /**
     * Stop scheduler.
     */
    public static void stopScheduler() {
        if ( !scheduler.isShutdown() ) {
            scheduler.shutdown();
            try {
                if ( !scheduler.awaitTermination( 10, TimeUnit.MINUTES ) ) { // Adjust timeout as needed
                    scheduler.shutdownNow(); // Force shutdown if tasks take too long
                }
            } catch ( InterruptedException e ) {
                log.error( "Scheduler termination interrupted", e );
                scheduler.shutdownNow(); // Force shutdown on interruption
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Create or update data source cache void.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     * @param dataSource
     *         the data source
     * @param langKey
     *         the lang key
     * @param dataSourceCacheDirectory
     *         the data source cache directory
     * @param cacheThreadKey
     *         the cache thread key
     *
     * @return the void
     *
     * @throws InterruptedException
     *         the interrupted exception
     */
    public Void createOrUpdateDataSourceCache( EntityManagerFactory entityManagerFactory, SuSObjectDataSourceDTO dataSource, String langKey,
            Path dataSourceCacheDirectory, String cacheThreadKey, String objectId ) {
        Path langCacheDirectory = DashboardSuSObjectUtil.createCacheDirectoryForCacheWithLanguagePostFix( dataSourceCacheDirectory,
                dataSource.getProjectId(), langKey );
        Path cacheFile = DashboardSuSObjectUtil.createCacheFileOnLanguageDirectory( langCacheDirectory );
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var projectTreeStructure = getProjectAsTree( entityManager, langKey, dataSource );
            DashboardSuSObjectUtil.writeTreeToFile( projectTreeStructure, cacheFile );
            DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( objectId ) );
            var ds = dashboardEntity.getDataSources().stream().filter( dSource -> dSource.getId().toString().equals( dataSource.getId() ) )
                    .findFirst().orElse( null );
            if ( ds instanceof SuSObjectDataSourceEntity susDataSourceEntry ) {
                susDataSourceEntry.setCacheUpdatedAt(
                        LocalDateTime.now().format( DateTimeFormatter.ofPattern( DateUtils.DATE_PATTERN_FOR_TIMESTAMP_WITH_COMMA ) ) );
            }
            susDAO.saveOrUpdateObject( entityManager, dashboardEntity );
        } finally {
            entityManager.close();
            DashboardSuSObjectUtil.removeCacheThreadFromRunningList( cacheThreadKey );
        }
        return null;
    }

    /**
     * Gets project as tree.
     *
     * @param entityManager
     *         the entity manager
     * @param langKey
     *         the lang key
     * @param dataSource
     *         the data source
     *
     * @return the project as tree
     */
    private Tree< SuSObjectCacheDTO > getProjectAsTree( EntityManager entityManager, String langKey, SuSObjectDataSourceDTO dataSource ) {
        log.debug( "fetching selected project with id {}", dataSource.getProjectId() );
        SuSEntity selectedProject = susDAO.getLatestObjectById( entityManager, SuSEntity.class,
                UUID.fromString( dataSource.getProjectId() ) );
        if ( selectedProject != null ) {
            if ( log.isDebugEnabled() ) {
                log.debug( "queries started for {}  with id {} for classification dashboard with language {}", selectedProject.getName(),
                        dataSource.getProjectId(), langKey );
            }
            TreeNode< SuSObjectCacheDTO > root = new TreeNode<>(
                    prepareSusObjectCacheDTOFromSusEntity( entityManager, selectedProject, UUID.fromString( dataSource.getProjectId() ) ) );
            Tree< SuSObjectCacheDTO > entityTree = new Tree<>( root );
            addNodesToEntityTree( entityManager, selectedProject, entityTree, root, langKey, dataSource, 0 );
            if ( log.isDebugEnabled() ) {
                log.debug( "queries ended for {}  with id {} for classification dashboard with language {}", selectedProject.getName(),
                        selectedProject.getComposedId().getId(), langKey );
            }
            return entityTree;
        }
        return null;
    }

    /**
     * Add nodes to entity tree.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     * @param entityTree
     *         the entity tree
     * @param currentNode
     *         the current node
     * @param langKey
     *         the lang key
     * @param dataSource
     *         the selected project id
     * @param level
     *         the level
     */
    private void addNodesToEntityTree( EntityManager entityManager, SuSEntity entity, Tree< SuSObjectCacheDTO > entityTree,
            TreeNode< SuSObjectCacheDTO > currentNode, String langKey, SuSObjectDataSourceDTO dataSource, int level ) {
        var translatedName = DashboardSuSObjectUtil.translateName( langKey, entity, entity.getTranslation().stream()
                .filter( translationEntity -> translationEntity.getSuSEntity().getComposedId().equals( entity.getComposedId() ) )
                .toList() );
        entity.setName( translatedName );
        if ( StringUtils.isNotBlank( dataSource.getTraversalDepth() ) && Integer.parseInt( dataSource.getTraversalDepth() ) <= level ) {
            return;
        }
        List< SuSEntity > children = susDAO.getChildren( entityManager, entity );
        children.forEach( child -> {
            var childNode = entityTree.addChildToParent( currentNode,
                    prepareSusObjectCacheDTOFromSusEntity( entityManager, child, UUID.fromString( dataSource.getProjectId() ) ) );
            addNodesToEntityTree( entityManager, child, entityTree, childNode, langKey, dataSource, level + 1 );
        } );
    }

    /**
     * Prepare sus object cache dto from sus entity su s object cache dto.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     * @param selectedProjectID
     *         the selected project id
     *
     * @return the su s object cache dto
     */
    private SuSObjectCacheDTO prepareSusObjectCacheDTOFromSusEntity( EntityManager entityManager, SuSEntity entity,
            UUID selectedProjectID ) {
        SuSObjectCacheDTO dto = new SuSObjectCacheDTO();
        dto.setName( entity.getName() );
        dto.setId( entity.getComposedId().getId() );

        if ( entity.getTypeId() != null ) {
            dto.setLifeCycleStatus( selectionManager.getConfigManager()
                    .getStatusByIdandObjectType( entity.getTypeId(), entity.getLifeCycleStatus(), entity.getConfig() ) );
        }
        if ( null != entity.getConfig() ) {
            dto.setType(
                    selectionManager.getConfigManager().getObjectTypeByIdAndConfigName( entity.getTypeId().toString(), entity.getConfig() )
                            .getName() );
        }
        dto.setIcon( entity.getIcon() );
        dto.setUrl( DashboardSuSObjectUtil.prepareUrlForSusEntity( entity ) );
        dto.setPath( prepareFullPath( entityManager, entity, selectedProjectID ) );
        return dto;
    }

    /**
     * Prepare full path string.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     * @param selectedProjectID
     *         the selected project id
     *
     * @return the string
     */
    private String prepareFullPath( EntityManager entityManager, SuSEntity entity, UUID selectedProjectID ) {
        String[] parentNames = getListOfParentNames( entityManager, entity, selectedProjectID.toString() ).toArray( new String[ 0 ] );
        CollectionUtils.reverseArray( parentNames );
        return String.join( " -> ", parentNames );
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
    private List< String > getListOfParentNames( EntityManager entityManager, SuSEntity suSEntity, String finalParentId ) {
        List< String > parentNames = new ArrayList<>();
        addParentNamesToList( entityManager, suSEntity, parentNames, finalParentId );
        return parentNames;
    }

    /**
     * Add parent names to list.
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
     * Remove grouping from contained widgets.
     *
     * @param token
     *         the token
     * @param widgetToDelete
     *         the widget to delete
     * @param dashboard
     *         the dashboard
     * @param entityManager
     *         the entity manager
     */
    private void removeGroupingFromContainedWidgets( String token, DashboardWidgetEntity widgetToDelete, DataDashboardEntity dashboard,
            EntityManager entityManager ) {
        var dto = DataDashboardUtil.prepareWidgetDTOFromEntity( widgetToDelete );
        GroupWidgetOptions groupOptions = ( GroupWidgetOptions ) dto.getOptions();
        for ( DashboardWidgetEntity widget : dashboard.getWidgets() ) {
            if ( groupOptions.getWidgets().contains( widget.getId().toString() ) ) {
                DashboardWidgetDTO widgetDTO = DataDashboardUtil.prepareWidgetDTOFromEntity( widget );
                widgetDTO.setRelation( WIDGET_RELATION.IS_NOT_IN_GROUP );
                widgetDTO.setGroupId( null );
                updateWidgetEntity( entityManager, widget, JsonUtils.toJson( widgetDTO ), token );
            }
        }
    }

    /**
     * Prepare material inspector form list.
     *
     * @param entityManager
     *         the entity manager
     * @param sources
     *         the sources
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the list
     */
    private List< UIFormItem > prepareMaterialInspectorForm( EntityManager entityManager, List< DataSourceDTO > sources, String objectId,
            String widgetId ) {
        var items = GUIUtils.prepareForm( true, new WidgetMaterialInspectorSource() );
        for ( Iterator< UIFormItem > iterator = items.iterator(); iterator.hasNext(); ) {
            UIFormItem item = iterator.next();
            switch ( item.getName() ) {
                case MATERIAL_INSPECTOR_FIELDS.MAT_DB_SOURCE ->
                        DashboardMaterialInspectorUtil.populateDataSourceList( ( SelectFormItem ) item, sources );
                case MATERIAL_INSPECTOR_FIELDS.MAT_DB_SCHEMA ->
                        DashboardMaterialInspectorUtil.setBindToForMatDbSchema( ( SelectFormItem ) item, objectId, widgetId );
                case MATERIAL_INSPECTOR_FIELDS.GS_MATERIAL_NAME ->
                        DashboardMaterialInspectorUtil.setBindToForGsName( ( SelectFormItem ) item, objectId, widgetId );
                case MATERIAL_INSPECTOR_FIELDS.THICKNESS -> {
                    DashboardMaterialInspectorUtil.setBindToForThickness( ( SelectFormItem ) item, objectId, widgetId );
                    item.setMultiple( true );
                }
                case MATERIAL_INSPECTOR_FIELDS.ORDER_NR, WIDGET_FIELDS.DATA_SOURCE_TYPE, WIDGET_FIELDS.DATA_SOURCE, WIDGET_FIELDS.NAME,
                     MATERIAL_INSPECTOR_FIELDS.SURFACE_FINISH, MATERIAL_INSPECTOR_FIELDS.CHARGE, MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME,
                     MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME, MATERIAL_INSPECTOR_FIELDS.STATUS,
                     MATERIAL_INSPECTOR_FIELDS.LAB_DATA_PLOT_DATA -> iterator.remove();
                case MATERIAL_INSPECTOR_FIELDS.OPT_FLOW_CURVE_MODELS, MATERIAL_INSPECTOR_FIELDS.LOAD_OPT_RESULTS,
                     MATERIAL_INSPECTOR_FIELDS.EXPORT_FLOW_CURVE_DATA, MATERIAL_INSPECTOR_FIELDS.FLOW_CURVE_PLOT_DATA,
                     MATERIAL_INSPECTOR_FIELDS.CAL_YIELD_LOCUS_B89, MATERIAL_INSPECTOR_FIELDS.CAL_YIELD_LOCUS_B2000,
                     MATERIAL_INSPECTOR_FIELDS.DEFINE_YIELD_LOCUS, MATERIAL_INSPECTOR_FIELDS.GEN_YIELD_LOCUS,
                     MATERIAL_INSPECTOR_FIELDS.EXPORT_YIELD_LOCUS, MATERIAL_INSPECTOR_FIELDS.EXPORT_MEASUREMENT_DATA ->
                        DashboardMaterialInspectorUtil.setBindToForButton( ( ButtonFormItem ) item, objectId, widgetId );
                case MATERIAL_INSPECTOR_FIELDS.CPUS -> DashboardMaterialInspectorUtil.setOptionsForCPUs( ( SelectFormItem ) item );
                case MATERIAL_INSPECTOR_FIELDS.FT0, MATERIAL_INSPECTOR_FIELDS.FT45, MATERIAL_INSPECTOR_FIELDS.INVESTIGATED_STRAIN_STATE,
                     MATERIAL_INSPECTOR_FIELDS.FT90, MATERIAL_INSPECTOR_FIELDS.FB -> item.setReadonly( true );

            }
            if ( FieldTypes.SELECTION.getType().equals( item.getType() ) ) {
                GUIUtils.setLiveSearchInSelectItem( ( SelectFormItem ) item );
            }
        }
        return items;
    }

    /**
     * Populate options fields.
     *
     * @param entityManager
     *         the entity manager
     * @param formItems
     *         the form items
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param pythonMode
     *         the python mode
     */
    private void populateOptionsFields( EntityManager entityManager, List< UIFormItem > formItems, DashboardWidgetDTO widget,
            DataSourceDTO selectedDataSource, String objectId, String token, String pythonMode ) {
        switch ( WIDGET_SOURCE_TYPE.getById( widget.getSource().getSourceType() ) ) {
            case QUERY_BUILDER -> populateOptionsFieldsForQueryBuilder( entityManager, formItems, widget, selectedDataSource, objectId );
            case PYTHON ->
                    populateOptionsFieldsForPython( entityManager, formItems, widget, selectedDataSource, objectId, token, pythonMode );
            case OTHER -> populateOptionsFieldsForOther( entityManager, formItems, widget, objectId, selectedDataSource, token );
        }
    }

    /**
     * Populate options fields for other.
     *
     * @param formItems
     *         the form items
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     * @param selectedDataSource
     *         the selected data source
     */
    private void populateOptionsFieldsForOther( EntityManager entityManager, List< UIFormItem > formItems, DashboardWidgetDTO widget,
            String objectId, DataSourceDTO selectedDataSource, String token ) {
        var name = ( ( WidgetOtherSource ) widget.getSource() ).getName();
        switch ( OTHER_WIDGET_SOURCE.getById( name ) ) {
            case MATERIAL_INSPECTION -> populateOptionsFieldsForMaterialInspector( formItems, widget, objectId, selectedDataSource );
            case PST -> DashboardPSTUtil.populateOptionsFieldsForPstWidget( formItems );
            case VMCL -> populateOptionsFieldsForVMcl( entityManager, formItems, widget, selectedDataSource, objectId, token );
        }
    }

    /**
     * Populate options fields for material inspector.
     *
     * @param formItems
     *         the form items
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     * @param selectedDataSource
     *         the selected data source
     */
    private void populateOptionsFieldsForMaterialInspector( List< UIFormItem > formItems, DashboardWidgetDTO widget, String objectId,
            DataSourceDTO selectedDataSource ) {
        if ( !DashboardMaterialInspectorUtil.checkIfOutputFileExists( widget, objectId ) ) {
            return;
        }
        var columnList = DashboardMaterialInspectorUtil.getColumnsFromOutputFile( widget, objectId );
        DataDashboardUtil.updateOptionsFields( formItems, widget, objectId, columnList,
                null == selectedDataSource ? EMPTY_STRING : selectedDataSource.getSourceType() );
    }

    /**
     * Populate options fields for query builder.
     *
     * @param entityManager
     *         the entity manager
     * @param formItems
     *         the form items
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     */
    private void populateOptionsFieldsForQueryBuilder( EntityManager entityManager, List< UIFormItem > formItems, DashboardWidgetDTO widget,
            DataSourceDTO selectedDataSource, String objectId ) {
        switch ( DashboardDataSourceOptions.getById( selectedDataSource.getSourceType() ) ) {
            case DATABASE -> populateOptionsFieldsForQueryBuilderAndDatabase( formItems, widget, selectedDataSource, objectId );
            case CSV -> populateOptionsFieldsForQueryBuilderAndCSV( entityManager, formItems, widget, selectedDataSource, objectId );
            case JSON -> populateOptionsFieldsForQueryBuilderAndJson( entityManager, formItems, widget, selectedDataSource, objectId );
            case EXCEL -> {
                //do nothing
            }
        }
    }

    /**
     * Populate options fields for query builder and csv.
     *
     * @param entityManager
     *         the entity manager
     * @param formItems
     *         the form items
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     */
    private void populateOptionsFieldsForQueryBuilderAndCSV( EntityManager entityManager, List< UIFormItem > formItems,
            DashboardWidgetDTO widget, DataSourceDTO selectedDataSource, String objectId ) {
        var columnList = prepareColumnListOptionsForCsvDataSource( entityManager, ( CSVDataSourceDTO ) selectedDataSource );
        DataDashboardUtil.updateOptionsFields( formItems, widget, objectId, columnList,
                null == selectedDataSource ? EMPTY_STRING : selectedDataSource.getSourceType() );
    }

    /**
     * Populate options fields for query builder and json.
     *
     * @param entityManager
     *         the entity manager
     * @param formItems
     *         the form items
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     */
    private void populateOptionsFieldsForQueryBuilderAndJson( EntityManager entityManager, List< UIFormItem > formItems,
            DashboardWidgetDTO widget, DataSourceDTO selectedDataSource, String objectId ) {
        var columnList = prepareColumnListOptionsForJsonDataSource( entityManager, ( JsonDataSourceDTO ) selectedDataSource );
        DataDashboardUtil.updateOptionsFields( formItems, widget, objectId, columnList,
                null == selectedDataSource ? EMPTY_STRING : selectedDataSource.getSourceType() );
    }

    /**
     * Populate options fields for query builder and database.
     *
     * @param formItems
     *         the form items
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     */
    private void populateOptionsFieldsForQueryBuilderAndDatabase( List< UIFormItem > formItems, DashboardWidgetDTO widget,
            DataSourceDTO selectedDataSource, String objectId ) {
        var columnList = DataDashboardUtil.populateColumnsListForWidgetOptionsForDatabase( widget,
                ( DatabaseDataSourceDTO ) selectedDataSource );
        DataDashboardUtil.updateOptionsFields( formItems, widget, objectId, columnList,
                null == selectedDataSource ? EMPTY_STRING : selectedDataSource.getSourceType() );

    }

    /**
     * Populate options fields for python.
     *
     * @param entityManager
     *         the entity manager
     * @param formItems
     *         the form items
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param pythonMode
     *         the python mode
     */
    private void populateOptionsFieldsForPython( EntityManager entityManager, List< UIFormItem > formItems, DashboardWidgetDTO widget,
            DataSourceDTO selectedDataSource, String objectId, String token, String pythonMode ) {
        if ( PYTHON_MODES.REGENERATE.equals( pythonMode ) ) {
            resolveSelectionIdInPythonPathField( entityManager, ( WidgetPythonSource ) widget.getSource() );
            DashboardPythonUtil.runWidgetPython( widget, UUID.fromString( widget.getId() ),
                    new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), selectedDataSource, token );
        }
        var split = ( ( WidgetPythonSource ) widget.getSource() ).getScriptOption().split( "\\." );
        var scriptOption = split[ 1 ];
        var columnList = switch ( WIDGET_SCRIPT_OPTION.getById( scriptOption ) ) {
            case FILE -> getColumnsFromSelectedPythonFile( entityManager, widget, selectedDataSource, objectId, token );
            case USER -> getColumnsFromUserPythonScript( entityManager, widget, selectedDataSource, objectId, token );
            case SYSTEM -> null;
        };
        DataDashboardUtil.updateOptionsFields( formItems, widget, objectId, columnList,
                null == selectedDataSource ? EMPTY_STRING : selectedDataSource.getSourceType() );

    }

    private void populateOptionsFieldsForVMcl( EntityManager entityManager, List< UIFormItem > formItems, DashboardWidgetDTO widget,
            DataSourceDTO selectedDataSource, String objectId, String token ) {
        DashboardVMclUtil.callPython( token, widget, ( SuSObjectDataSourceDTO ) selectedDataSource, objectId );
        VersionPrimaryKey dashboardId = new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() );
        String idInPath = widget.getGroupId() != null ? widget.getGroupId() : widget.getId();
        var outputFile = DashboardVMclUtil.preparePythonOutputFile( UUID.fromString( idInPath ), dashboardId );
        var columnList = getColumnsFromPythonOutputByOutputType(
                new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), widget,
                outputFile.toAbsolutePath().toString() );
        DataDashboardUtil.updateOptionsFields( formItems, widget, objectId, columnList,
                null == selectedDataSource ? EMPTY_STRING : selectedDataSource.getSourceType() );
    }

    /**
     * Gets columns from user python script.
     *
     * @param entityManager
     *         the entity manager
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the columns from user python script
     */
    private List< SelectOptionsUI > getColumnsFromUserPythonScript( EntityManager entityManager, DashboardWidgetDTO widget,
            DataSourceDTO selectedDataSource, String objectId, String token ) {
        WidgetPythonSource pythonSource = ( WidgetPythonSource ) widget.getSource();
        var split = pythonSource.getPythonStatus().split( "\\." );
        String pythonOutput = split[ 0 ];
        String scriptStatus = split[ 1 ];
        resolveSelectionIdInPythonPathField( entityManager, pythonSource );
        switch ( ScriptStatusOption.getById( scriptStatus ) ) {
            case IN_CHANGE -> DashboardPythonUtil.runWidgetPython( widget, UUID.fromString( widget.getId() ),
                    new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), selectedDataSource, token );
            case FINAL -> {
                //do nothing
            }
        }
        return getColumnsFromPythonOutputByOutputType( new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), widget,
                pythonOutput );
    }

    /**
     * Gets columns from selected python file.
     *
     * @param entityManager
     *         the entity manager
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the columns from selected python file
     */
    private List< SelectOptionsUI > getColumnsFromSelectedPythonFile( EntityManager entityManager, DashboardWidgetDTO widget,
            DataSourceDTO selectedDataSource, String objectId, String token ) {
        WidgetPythonSource pythonSource = ( WidgetPythonSource ) widget.getSource();
        var split = pythonSource.getPythonStatus().split( "\\." );
        String pythonOutput = split[ 0 ];
        String scriptStatus = split[ 1 ];
        resolveSelectionIdInPythonSelectScriptField( entityManager, pythonSource );
        resolveSelectionIdInPythonPathField( entityManager, pythonSource );
        switch ( ScriptStatusOption.getById( scriptStatus ) ) {
            case IN_CHANGE -> DashboardPythonUtil.runWidgetPython( widget, UUID.fromString( widget.getId() ),
                    new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), selectedDataSource, token );
            case FINAL -> {
                //do nothing
            }
        }
        return getColumnsFromPythonOutputByOutputType( new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), widget,
                pythonOutput );

    }

    /**
     * Gets data source by id.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the data source by id
     */
    private DataSourceDTO getDataSourceById( EntityManager entityManager, String objectId, String dataSourceId ) {
        DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                UUID.fromString( objectId ) );
        if ( dashboardEntity.getDataSources() == null || dashboardEntity.getDataSources().stream()
                .noneMatch( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), dataSourceId ) );
        }
        @SuppressWarnings( "OptionalGetWithoutIsPresent" ) var matchingEntity = dashboardEntity.getDataSources().stream()
                .filter( dsEntity -> dsEntity.getId().toString().equals( dataSourceId ) ).findFirst().get();
        return prepareDataSourceDTOFromEntity( entityManager, matchingEntity );
    }

    /**
     * Gets python template.
     *
     * @param pythonOutput
     *         the python output
     * @param dataSourceType
     *         the data source type
     *
     * @return the python template
     */
    private String getPythonTemplate( String pythonOutput, String dataSourceType ) {
        var templateFilePath = DashboardConfigUtil.getTemplatePath( DashboardDataSourceOptions.getById( dataSourceType ),
                WidgetPythonOutputOptions.getById( pythonOutput ) );
        return DashboardPythonUtil.readPythonTemplateFile( templateFilePath );
    }

    /**
     * Prepare column list options list.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param value
     *         the value
     *
     * @return the list
     */
    private List< SelectOptionsUI > prepareColumnListOptions( EntityManager entityManager, String objectId, String value ) {
        var split = value.split( "\\." );
        String dataSourceId = split[ 0 ];
        var dataSource = getDataSourceById( entityManager, objectId, dataSourceId );
        return switch ( DashboardDataSourceOptions.getById( dataSource.getSourceType() ) ) {
            case DATABASE -> prepareColumnListOptionsForDatabaseDataSource( entityManager, objectId, value );
            case CSV -> prepareColumnListOptionsForCsvDataSource( entityManager, ( CSVDataSourceDTO ) dataSource );
            case JSON -> prepareColumnListOptionsForJsonDataSource( entityManager, ( JsonDataSourceDTO ) dataSource );
            case SUS_OBJECT -> prepareColumnListOptionsForSuSObjectDataSource( entityManager, ( SuSObjectDataSourceDTO ) dataSource );
            case EXCEL -> null;
        };
    }

    /**
     * Prepare column list options for csv data source list.
     *
     * @param entityManager
     *         the entity manager
     * @param dataSource
     *         the data source
     *
     * @return the list
     */
    private List< SelectOptionsUI > prepareColumnListOptionsForCsvDataSource( EntityManager entityManager, CSVDataSourceDTO dataSource ) {
        var filePath = Path.of( getPathFromSelectionId( entityManager, dataSource.getFileSelection() ) );
        DashboardCSVUtil.validateUserCSV( filePath );
        return DashboardCSVUtil.getColumnsFromCSV( filePath );
    }

    /**
     * Prepare column list options for json data source list.
     *
     * @param entityManager
     *         the entity manager
     * @param dataSource
     *         the data source
     *
     * @return the list
     */
    private List< SelectOptionsUI > prepareColumnListOptionsForJsonDataSource( EntityManager entityManager, JsonDataSourceDTO dataSource ) {
        var filePath = Path.of( getPathFromSelectionId( entityManager, dataSource.getFileSelection() ) );
        DashboardJsonUtil.validateUserJson( filePath );
        return DashboardJsonUtil.getColumnsFromJson( filePath );
    }

    /**
     * Prepare column list options for su s object data source list.
     *
     * @param entityManager
     *         the entity manager
     * @param dataSource
     *         the data source
     *
     * @return the list
     */
    private List< SelectOptionsUI > prepareColumnListOptionsForSuSObjectDataSource( EntityManager entityManager,
            SuSObjectDataSourceDTO dataSource ) {
        SuSEntity suSEntity = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( dataSource.getProjectId() ) );
        return DashboardSuSObjectUtil.getColumnsFromSuSObject( entityManager, suSEntity, susDAO );
    }

    /**
     * Prepare column list options for database data source list.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param table
     *         the table
     *
     * @return the list
     */
    private List< SelectOptionsUI > prepareColumnListOptionsForDatabaseDataSource( EntityManager entityManager, String objectId,
            String table ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            //temporarily using split. will be fixed later with multiple bindTo support
            var split = table.split( "\\." );
            String dataSourceId = split[ 0 ];

            jdbcTemplate = DataDashboardUtil.getJdbcTemplate(
                    getDataSourceDTOByDashboardIdAndSourceId( entityManager, objectId, dataSourceId ) );
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            List< SelectOptionsUI > columnsList = new ArrayList<>();
            DataDashboardUtil.addColumnOptionsToList( table, metaData, columnsList );
            return columnsList;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_COLUMNS_FROM_DATABASE.getKey() ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Prepare filtered columns table ui.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param table
     *         the table
     *
     * @return the table ui
     */
    private TableUI prepareFilteredColumns( EntityManager entityManager, String objectId, String table ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            //temporarily using split. will be fixed later with multiple bindTo support
            var split = table.split( "\\." );
            String dataSourceId = split[ 0 ];

            jdbcTemplate = DataDashboardUtil.getJdbcTemplate(
                    getDataSourceDTOByDashboardIdAndSourceId( entityManager, objectId, dataSourceId ) );
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            TableUI filteredColumns = new TableUI();
            filteredColumns.setColumns( DataDashboardUtil.getColumnsForFilter( table, metaData ) );
            return filteredColumns;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_COLUMNS_FROM_DATABASE.getKey() ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Update data source entity.
     *
     * @param entityManager
     *         the entity manager
     * @param matchingEntity
     *         the matching entity
     * @param dataSourceDTO
     *         the data source dto
     * @param token
     *         the token
     */
    private void updateDataSourceEntity( EntityManager entityManager, DataSourceEntity matchingEntity, DataSourceDTO dataSourceDTO,
            String token ) {
        if ( !matchingEntity.getSourceType().equals( dataSourceDTO.getSourceType() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CAN_NOT_UPDATE_TYPE_DATA_SOURCE.getKey() ) );
        }
        matchingEntity.setSourceType( dataSourceDTO.getSourceType() );
        matchingEntity.setName( dataSourceDTO.getSourceName() );
        matchingEntity.setModifiedOn( new Date() );
        matchingEntity.setModifiedBy( userCommonManager.getUserEntityById( entityManager,
                UUID.fromString( TokenizedLicenseUtil.getNotNullUser( token ).getId() ) ) );
        if ( matchingEntity instanceof DatabaseDataSourceEntity dbDsEntity && dataSourceDTO instanceof DatabaseDataSourceDTO dbDsDTO ) {
            dbDsEntity.setDatabaseType( dbDsDTO.getDatabaseType() );
            dbDsEntity.setPassword( dbDsDTO.getPassword() );
            dbDsEntity.setHost( dbDsDTO.getHost() );
            dbDsEntity.setPort( dbDsDTO.getPort() );
            dbDsEntity.setDbName( dbDsDTO.getDbName() );
            dbDsEntity.setUserName( dbDsDTO.getUserName() );
        } else if ( matchingEntity instanceof SuSObjectDataSourceEntity dbDsEntity
                && dataSourceDTO instanceof SuSObjectDataSourceDTO dbDsDTO ) {
            dbDsEntity.setUpdateInterval( dbDsDTO.getUpdateInterval() );
            dbDsEntity.setTraversalDepth( dbDsDTO.getTraversalDepth() );

        }
    }

    /**
     * Prepare table list options list.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param schema
     *         the schema
     *
     * @return the list
     */
    private List< SelectOptionsUI > prepareTableListOptions( EntityManager entityManager, String objectId, String schema ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            //temporarily using split. will be fixed later with multiple bindTo support
            var split = schema.split( "\\." );
            String dataSourceId = split[ 0 ];
            String schemaName = split[ 1 ];

            jdbcTemplate = DataDashboardUtil.getJdbcTemplate(
                    getDataSourceDTOByDashboardIdAndSourceId( entityManager, objectId, dataSourceId ) );
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            List< SelectOptionsUI > tablesList = new ArrayList<>();
            DataDashboardUtil.addTableOptionsToList( schema, metaData, schemaName, tablesList );
            return tablesList;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_TABLES_FROM_DATABASE.getKey() ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Sets options in data source form item.
     *
     * @param entityManager
     *         the entity manager
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param widgetDataSourceType
     *         the widget data source type
     */
    private void setOptionsInDataSourceFormItem( EntityManager entityManager, UIFormItem item, String objectId,
            WIDGET_SOURCE_TYPE widgetDataSourceType ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        DataDashboardEntity dashboardEntity = ( DataDashboardEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                UUID.fromString( objectId ) );
        var dataSources = dashboardEntity.getDataSources();
        if ( CollectionUtils.isEmpty( dataSources ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DATA_SOURCE_NOT_FOUND.getKey(), objectId ) );
        }
        switch ( widgetDataSourceType ) {
            case PYTHON -> options = dataSources.stream()
                    .map( dataSource -> new SelectOptionsUI( dataSource.getId().toString(), dataSource.getName() ) )
                    .collect( Collectors.toList() );
            case QUERY_BUILDER -> {
                for ( var dataSource : dataSources ) {
                    if ( !dataSource.getSourceType().equals( DashboardDataSourceOptions.EXCEL.getId() ) ) {
                        options.add( new SelectOptionsUI( dataSource.getId().toString(), dataSource.getName() ) );
                    }

                }
            }
        }
        ( ( SelectFormItem ) item ).setOptions( options );
    }

    /**
     * Check read permission.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     * @param objectId
     *         the object id
     */
    private void checkReadPermission( EntityManager entityManager, String token, String objectId ) {
        if ( !permissionManager.isReadable( entityManager, UUID.fromString( TokenizedLicenseUtil.getNotNullUser( token ).getId() ),
                UUID.fromString( objectId ) ) ) {
            var dashboardEntity = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( objectId ) );
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), dashboardEntity.getName() ) );
        }
    }

    /**
     * Check write permission.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     * @param objectId
     *         the object id
     */
    private void checkWritePermission( EntityManager entityManager, String token, String objectId ) {
        if ( !permissionManager.isWritable( entityManager, UUID.fromString( TokenizedLicenseUtil.getNotNullUser( token ).getId() ),
                UUID.fromString( objectId ) ) ) {
            var dashboardEntity = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( objectId ) );
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_UPDATE.getKey(), dashboardEntity.getName() ) );
        }
    }

}