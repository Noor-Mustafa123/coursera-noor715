package de.soco.software.simuspace.suscore.object.utility;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.BAR_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.BIND_FROM_URLS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.BIND_TO_URLS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.DATA_SOURCE_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.LINE_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.MIX_CHART_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.RADAR_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.SCATTER_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.SQL_CONSTANTS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.TEXT_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.URL_PARAMS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.COLOR_SCHEMES;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.CSVDelimiter;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.DashboardDataSourceOptions;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.DataSourceDatabaseOptions;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.DriverClassName;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.LINE_STYLE;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.LINE_WIDGET_OPTIONS;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.MULTI_CHART_PLOT_TYPE;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.OTHER_WIDGET_SOURCE;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.POINT_SYMBOL;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.RADAR_SHAPE;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.ScriptStatusOption;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.SqlAggregateOptions;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.TEXT_WIDGET_INPUT_METHOD;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WIDGET_RELATION;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WIDGET_SCRIPT_OPTION;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WIDGET_SOURCE_TYPE;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WIDGET_TYPE;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WidgetPythonOutputOptions;
import static de.soco.software.simuspace.suscore.common.model.DynamicScript.DASHBOARD_SCRIPTS_KEY;
import static de.soco.software.simuspace.suscore.common.util.DynamicScriptsUtil.getDynamicScriptFieldsFromConfigByKey;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants;
import de.soco.software.simuspace.suscore.common.enums.FilterType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.ButtonFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.CodeFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.BindTo;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryData;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryMetadata;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryResponse;
import de.soco.software.simuspace.suscore.common.model.SqlSchemaMetaDataDTO;
import de.soco.software.simuspace.suscore.common.model.SqlTableMetadataDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.entity.DashboardWidgetEntity;
import de.soco.software.simuspace.suscore.data.model.BarMixChartCurveOptions;
import de.soco.software.simuspace.suscore.data.model.BarWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.CSVDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;
import de.soco.software.simuspace.suscore.data.model.DataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.DatabaseDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.ExcelDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.GroupWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.JsonDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.LineMixChartCurveOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleXOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleXYOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleYOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.MetalBaseWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.MixChartWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.ProjectLifeCycleWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.PstWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.RadarWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.ScatterMixChartCurveOptions;
import de.soco.software.simuspace.suscore.data.model.ScatterWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.SuSObjectDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.TableWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.TextWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.TreemapWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.WidgetMaterialInspectorSource;
import de.soco.software.simuspace.suscore.data.model.WidgetMetalBaseSource;
import de.soco.software.simuspace.suscore.data.model.WidgetOptions;
import de.soco.software.simuspace.suscore.data.model.WidgetOtherSource;
import de.soco.software.simuspace.suscore.data.model.WidgetPstSource;
import de.soco.software.simuspace.suscore.data.model.WidgetPythonSource;
import de.soco.software.simuspace.suscore.data.model.WidgetQueryBuilderSource;
import de.soco.software.simuspace.suscore.data.model.WidgetSource;
import de.soco.software.simuspace.suscore.data.model.WidgetVmclSource;

/**
 * The Util class for Data dashboards.
 */
@Log4j2
public class DataDashboardUtil {

    /**
     * The constant POSTGRES_JDBC_URL_TEMPLATE.
     */
    public static final String POSTGRES_JDBC_URL_TEMPLATE = "jdbc:postgresql://%s:%s/%s";

    /**
     * Instantiates a new Data source util.
     */
    private DataDashboardUtil() {

    }

    /**
     * Close connection.
     *
     * @param jdbcTemplate
     *         the jdbc connection
     */
    public static void closeDataSource( JdbcTemplate jdbcTemplate ) {
        if ( jdbcTemplate != null && jdbcTemplate.getDataSource() != null
                && !( ( ( HikariDataSource ) jdbcTemplate.getDataSource() ).isClosed() ) ) {
            ( ( HikariDataSource ) jdbcTemplate.getDataSource() ).close();
        }
    }

    /**
     * Create hikari data source from database data source hikari data source.
     *
     * @param dto
     *         the dto
     *
     * @return the hikari data source
     */
    public static HikariDataSource createHikariDataSourceFromDatabaseDataSource( DatabaseDataSourceDTO dto ) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl( prepareJDBCUrlFromUrl( dto.getHost(), dto.getPort(), dto.getDbName(), dto.getDatabaseType() ) );
        dataSource.setUsername( dto.getUserName() );
        dataSource.setPassword( dto.getPassword() );
        dataSource.setDriverClassName( DriverClassName.getClassNameById( dto.getDatabaseType() ) );
        //optional additional configuration
        dataSource.setMaximumPoolSize( 10 );
        dataSource.setConnectionTimeout( 30000 );
        return dataSource;
    }

    /**
     * Gets jdbc template.
     *
     * @param dto
     *         the dto
     *
     * @return the jdbc template
     */
    public static JdbcTemplate getJdbcTemplate( DatabaseDataSourceDTO dto ) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate( createHikariDataSourceFromDatabaseDataSource( dto ) );
        if ( jdbcTemplate.getDataSource() == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_CONNECT_EXTERNAL_DATABASE.getKey() ) );
        }
        return jdbcTemplate;
    }

    /**
     * Prepare jdbc url from url string.
     *
     * @param host
     *         the host
     * @param port
     *         the port
     * @param dbName
     *         the db name
     * @param dbType
     *         the db type
     *
     * @return the string
     */
    private static String prepareJDBCUrlFromUrl( String host, Integer port, String dbName, String dbType ) {
        if ( dbType.equals( DriverClassName.POSTGRES.getId() ) ) {
            return String.format( POSTGRES_JDBC_URL_TEMPLATE, host, port, dbName );
        }
        throw new SusException( MessageBundleFactory.getMessage( Messages.DATABASE_NOT_SUPPORTED.getKey(), dbType ) );
    }

    /**
     * Sets dynamic field options f or data source.
     *
     * @param dataSourceFormItems
     *         the data source form items
     * @param objectId
     *         the object id
     */
    public static void setDynamicFieldOptionsForDatabaseDataSource( List< UIFormItem > dataSourceFormItems, CharSequence objectId ) {
        dataSourceFormItems.forEach( formItem -> {
            switch ( formItem.getName() ) {
                case DATA_SOURCE_FIELDS.DATABASE_TYPE -> {
                    List< SelectOptionsUI > databaseTypeOptions = new ArrayList<>();
                    for ( var databaseOptions : DataSourceDatabaseOptions.values() ) {
                        databaseTypeOptions.add( new SelectOptionsUI( databaseOptions.getId(), databaseOptions.getName() ) );
                    }
                    ( ( SelectFormItem ) formItem ).setOptions( databaseTypeOptions );
                    if ( CollectionUtils.isNotEmpty( databaseTypeOptions ) ) {
                        formItem.setValue( databaseTypeOptions.iterator().next().getId() );
                    }
                }
                case DATA_SOURCE_FIELDS.TEST_BUTTON -> {
                    BindTo bindTo = new BindTo();
                    bindTo.setUrl( BIND_TO_URLS.BIND_TO_URL_FOR_TEST_CONNECTION );
                    formItem.setBindTo( bindTo );
                }
                case DATA_SOURCE_FIELDS.PREVIEW_BUTTON -> {
                    BindTo bindTo = new BindTo();
                    bindTo.setUrl( BIND_TO_URLS.BIND_TO_URL_FOR_PREVIEW_CONNECTION.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ) );
                    formItem.setBindTo( bindTo );
                }
            }
        } );
    }

    /**
     * Sets dynamic field options for csv data source.
     *
     * @param dataSourceFormItems
     *         the data source form items
     */
    public static void setDynamicFieldOptionsForCSVDataSource( List< UIFormItem > dataSourceFormItems ) {
        dataSourceFormItems.forEach( formItem -> {
            if ( DATA_SOURCE_FIELDS.DELIMITER.equals( formItem.getName() ) ) {
                List< SelectOptionsUI > delimiterOptions = new ArrayList<>();
                for ( var delimiters : CSVDelimiter.values() ) {
                    delimiterOptions.add( new SelectOptionsUI( delimiters.getId(), delimiters.getName() ) );
                }
                ( ( SelectFormItem ) formItem ).setOptions( delimiterOptions );
                if ( CollectionUtils.isNotEmpty( delimiterOptions ) ) {
                    formItem.setValue( delimiterOptions.iterator().next().getId() );
                }
            }
        } );
    }

    /**
     * Gets data source fields.
     *
     * @param dataSource
     *         the data source
     * @param dataSourceDTO
     *         the data source dto
     *
     * @return the data source fields
     */
    public static UIForm getDataSourceFields( DashboardDataSourceOptions dataSource, DataSourceDTO dataSourceDTO, String objectId ) {
        var dataSourceFormItems = GUIUtils.prepareOrderedForm( true, dataSourceDTO );
        switch ( dataSource ) {
            case DATABASE -> DataDashboardUtil.setDynamicFieldOptionsForDatabaseDataSource( dataSourceFormItems, objectId );
            case CSV -> DataDashboardUtil.setDynamicFieldOptionsForCSVDataSource( dataSourceFormItems );
            case EXCEL -> {
                //do nothing
            }
        }
        dataSourceFormItems.removeIf( item -> item.getName().equals( DATA_SOURCE_FIELDS.SOURCE_TYPE ) );
        return GUIUtils.createFormFromItems( dataSourceFormItems );
    }

    /**
     * Gets database preview.
     *
     * @param dataSourceDTO
     *         the data source dto
     *
     * @return the database preview
     */
    public static List< SqlSchemaMetaDataDTO > getDatabasePreview( DatabaseDataSourceDTO dataSourceDTO ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSourceDTO );
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            List< SqlSchemaMetaDataDTO > schemaList = new ArrayList<>();

            // Get schemas
            try ( ResultSet schemas = metaData.getSchemas() ) {
                while ( schemas.next() ) {
                    DataDashboardUtil.addSchemaMetadataToPreviewList( schemas, metaData, schemaList );
                }
            }
            return schemaList;
        } catch ( SQLException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GENERATE_DATABASE_PREVIEW.getKey() ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Add schema metadata to preview.
     *
     * @param schemas
     *         the schemas
     * @param metaData
     *         the meta data
     * @param schemaList
     *         the schema list
     *
     * @throws SQLException
     *         the sql exception
     */
    public static void addSchemaMetadataToPreviewList( ResultSet schemas, DatabaseMetaData metaData,
            List< SqlSchemaMetaDataDTO > schemaList ) throws SQLException {
        SqlSchemaMetaDataDTO sqlSchemaMetaDataDTO = new SqlSchemaMetaDataDTO();
        String schemaName = schemas.getString( SQL_CONSTANTS.TABLE_SCHEM );
        List< SqlTableMetadataDTO > tablesList = new ArrayList<>();

        // Get tables in this schema
        try ( ResultSet tables = metaData.getTables( null, schemaName, "%", new String[]{ SQL_CONSTANTS.TABLE } ) ) {
            while ( tables.next() ) {
                addTableMetadataToPreviewList( metaData, tables, schemaName, tablesList );
            }
        }
        sqlSchemaMetaDataDTO.setName( schemaName );
        sqlSchemaMetaDataDTO.setTables( tablesList );
        schemaList.add( sqlSchemaMetaDataDTO );
    }

    /**
     * Add table metadata to preview.
     *
     * @param metaData
     *         the meta data
     * @param tables
     *         the tables
     * @param schemaName
     *         the schema name
     * @param tablesList
     *         the tables list
     *
     * @throws SQLException
     *         the sql exception
     */
    private static void addTableMetadataToPreviewList( DatabaseMetaData metaData, ResultSet tables, String schemaName,
            List< SqlTableMetadataDTO > tablesList ) throws SQLException {
        String tableName = tables.getString( SQL_CONSTANTS.TABLE_NAME );
        SqlTableMetadataDTO tableDetails = new SqlTableMetadataDTO();
        tableDetails.setName( tableName );

        // Get columns in this table
        List< SqlTableMetadataDTO.SqlColumnMetaData > columnsList = new ArrayList<>();
        try ( ResultSet columns = metaData.getColumns( null, schemaName, tableName, "%" ) ) {
            while ( columns.next() ) {
                SqlTableMetadataDTO.SqlColumnMetaData columnDetails = new SqlTableMetadataDTO.SqlColumnMetaData(
                        columns.getString( SQL_CONSTANTS.COLUMN_NAME ), columns.getString( SQL_CONSTANTS.TYPE_NAME ) );
                columnsList.add( columnDetails );
            }
        }
        tableDetails.setColumns( columnsList );
        tablesList.add( tableDetails );
    }

    /**
     * Prepare data source type item for form select form item.
     *
     * @return the select form item
     */
    public static SelectFormItem prepareDataSourceTypeItemForForm( String objectId ) {
        return prepareDataSourceTypeItemForForm( true, objectId );
    }

    /**
     * Prepare data source type item for from select form item.
     *
     * @param setBindFrom
     *         the set bind from
     * @param objectId
     *
     * @return the select form item
     */
    public static SelectFormItem prepareDataSourceTypeItemForForm( boolean setBindFrom, String objectId ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.getFormItemByField( DataSourceDTO.class, WIDGET_FIELDS.DATA_SOURCE_TYPE );
        if ( setBindFrom ) {
            item.setBindFrom( BIND_FROM_URLS.CREATE_OBJECT_UI_FORM_DATA_SOURCE.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ) );
        }
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( DashboardDataSourceOptions option : DashboardDataSourceOptions.values() ) {
            options.add( new SelectOptionsUI( option.getId(), option.getName() ) );
        }
        item.setOptions( options );
        return item;
    }

    /**
     * Gets class from data source json.
     *
     * @param objectJson
     *         the object json
     *
     * @return the class from data source json
     */
    public static Class< ? > getClassFromDataSourceJson( String objectJson ) {
        try {
            var dataSourceType = JsonUtils.getValue( objectJson, WIDGET_FIELDS.DATA_SOURCE_TYPE );
            return switch ( DashboardDataSourceOptions.getById( dataSourceType ) ) {
                case DATABASE -> DatabaseDataSourceDTO.class;
                case EXCEL -> ExcelDataSourceDTO.class;
                case CSV -> CSVDataSourceDTO.class;
                case JSON -> JsonDataSourceDTO.class;
                case SUS_OBJECT -> SuSObjectDataSourceDTO.class;
            };
        } catch ( IOException e ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.KEY_NOT_FOUND_IN_PAYLOAD.getKey(), WIDGET_FIELDS.DATA_SOURCE_TYPE ) );
        }
    }

    /**
     * Gets data source dto from json.
     *
     * @param objectJson
     *         the object json
     *
     * @return the data source dto from json
     */
    public static DataSourceDTO getDataSourceDTOFromJson( String objectJson ) {
        return ( DataSourceDTO ) JsonUtils.jsonToObject( objectJson, DataDashboardUtil.getClassFromDataSourceJson( objectJson ) );
    }

    /**
     * Add schema options to list.
     *
     * @param dataSourceId
     *         the data source id
     * @param metaData
     *         the meta data
     * @param schemaList
     *         the schema list
     *
     * @throws SQLException
     *         the sql exception
     */
    public static void addSchemaOptionsToList( String dataSourceId, DatabaseMetaData metaData, List< SelectOptionsUI > schemaList )
            throws SQLException {
        try ( ResultSet schemas = metaData.getSchemas() ) {
            while ( schemas.next() ) {
                var dbSchemaName = schemas.getString( SQL_CONSTANTS.TABLE_SCHEM );
                schemaList.add( new SelectOptionsUI( dataSourceId + ConstantsString.DOT + dbSchemaName, dbSchemaName ) );
            }
        }
    }

    /**
     * Add table options to list.
     *
     * @param schema
     *         the schema
     * @param metaData
     *         the meta data
     * @param schemaName
     *         the schema name
     * @param tablesList
     *         the tables list
     *
     * @throws SQLException
     *         the sql exception
     */
    public static void addTableOptionsToList( String schema, DatabaseMetaData metaData, String schemaName,
            List< SelectOptionsUI > tablesList ) throws SQLException {
        try ( ResultSet schemas = metaData.getSchemas() ) {
            while ( schemas.next() ) {
                var dbSchemaName = schemas.getString( SQL_CONSTANTS.TABLE_SCHEM );
                if ( schemaName.equals( dbSchemaName ) ) {
                    try ( ResultSet tables = metaData.getTables( null, schemaName, "%", new String[]{ SQL_CONSTANTS.TABLE } ) ) {
                        while ( tables.next() ) {
                            String tableName = tables.getString( SQL_CONSTANTS.TABLE_NAME );
                            tablesList.add( new SelectOptionsUI( schema + ConstantsString.DOT + tableName, tableName ) );
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets column type.
     *
     * @param schemaName
     *         the schema name
     * @param tableName
     *         the table name
     * @param columnName
     *         the column name
     * @param metaData
     *         the meta data
     *
     * @return the column type
     *
     * @throws SQLException
     *         the sql exception
     */
    public static String getColumnType( String schemaName, String tableName, String columnName, DatabaseMetaData metaData )
            throws SQLException {
        try ( ResultSet schemas = metaData.getSchemas() ) {
            while ( schemas.next() ) {
                var dbSchemaName = schemas.getString( SQL_CONSTANTS.TABLE_SCHEM );
                if ( schemaName.equals( dbSchemaName ) ) {
                    try ( ResultSet tables = metaData.getTables( null, schemaName, "%", new String[]{ SQL_CONSTANTS.TABLE } ) ) {
                        while ( tables.next() ) {
                            String dbTableName = tables.getString( SQL_CONSTANTS.TABLE_NAME );
                            if ( dbTableName.equals( tableName ) ) {
                                try ( ResultSet columns = metaData.getColumns( null, schemaName, tableName, "%" ) ) {
                                    while ( columns.next() ) {
                                        var dbColumnName = columns.getString( SQL_CONSTANTS.COLUMN_NAME );
                                        if ( dbColumnName.equals( columnName ) ) {
                                            return columns.getString( SQL_CONSTANTS.TYPE_NAME );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Populate data source type field for widget form.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget type
     *
     * @return the select form item
     */
    public static SelectFormItem populateDataSourceTypeFieldForWidgetForm( String objectId, DashboardWidgetDTO widget ) {
        SelectFormItem item;
        if ( widget.getSource() != null ) {
            item = ( SelectFormItem ) GUIUtils.getFormItemsByFields( widget.getSource(), WIDGET_FIELDS.DATA_SOURCE_TYPE )
                    .get( ConstantsInteger.INTEGER_VALUE_ZERO );
        } else {
            item = ( SelectFormItem ) GUIUtils.getFormItemByField( WidgetSource.class, WIDGET_FIELDS.DATA_SOURCE_TYPE );
        }
        GUIUtils.setLiveSearchInSelectItem( item );
        item.setBindFrom( BIND_FROM_URLS.DATA_SOURCE_TYPE.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                .replace( URL_PARAMS.PARAM_WIDGET_ID, widget.getId() ) );
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var widgetDataOption : WIDGET_SOURCE_TYPE.values() ) {
            options.add( new SelectOptionsUI( widgetDataOption.getId(), widgetDataOption.getName() ) );
        }
        item.setOptions( options );
        return item;
    }

    /**
     * Sets options in data source schema form item.
     *
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     */
    public static void setOptionsInDataSourceSchemaFormItem( UIFormItem item, String objectId, String widgetId ) {
        ( ( SelectFormItem ) item ).setBindTo( new BindTo(
                BIND_TO_URLS.WIDGET_DATA_SOURCE_SCHEMAS.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                        .replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ), null, WIDGET_FIELDS.DATA_SOURCE ) );
        GUIUtils.setLiveSearchInSelectItem( ( SelectFormItem ) item );
    }

    /**
     * Sets options in data source table form item.
     *
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     */
    public static void setOptionsInDataSourceTableFormItem( UIFormItem item, String objectId, String widgetId ) {
        ( ( SelectFormItem ) item ).setBindTo( new BindTo(
                BIND_TO_URLS.WIDGET_DATA_SOURCE_SCHEMA_TABLES.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                        .replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ), null, WIDGET_FIELDS.QUERY_BUILDER_SCHEMA ) );
        GUIUtils.setLiveSearchInSelectItem( ( SelectFormItem ) item );
    }

    /**
     * Add column options to list.
     *
     * @param table
     *         the table
     * @param metaData
     *         the meta data
     * @param columnsList
     *         the columns list
     */
    public static void addColumnOptionsToList( String table, DatabaseMetaData metaData, List< SelectOptionsUI > columnsList ) {
        var split = table.split( "\\." );
        String schemaName = split[ 1 ];
        String tableName = split[ 2 ];
        try ( ResultSet schemas = metaData.getSchemas() ) {
            while ( schemas.next() ) {
                var dbSchemaName = schemas.getString( SQL_CONSTANTS.TABLE_SCHEM );
                if ( schemaName.equals( dbSchemaName ) ) {
                    try ( ResultSet tables = metaData.getTables( null, schemaName, "%", new String[]{ SQL_CONSTANTS.TABLE } ) ) {
                        while ( tables.next() ) {
                            String dbTableName = tables.getString( SQL_CONSTANTS.TABLE_NAME );
                            if ( dbTableName.equals( tableName ) ) {
                                try ( ResultSet columns = metaData.getColumns( null, schemaName, tableName, "%" ) ) {
                                    while ( columns.next() ) {
                                        var dbColumnName = columns.getString( SQL_CONSTANTS.COLUMN_NAME );
                                        columnsList.add( new SelectOptionsUI( table + ConstantsString.DOT + dbColumnName, dbColumnName ) );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_COLUMNS_FROM_DATABASE.getKey() ) );
        }
    }

    /**
     * Gets columns for filter.
     *
     * @param table
     *         the table
     * @param metaData
     *         the meta data
     *
     * @return the columns for filter
     */
    public static List< TableColumn > getColumnsForFilter( String table, DatabaseMetaData metaData ) {
        List< TableColumn > columnsList = new ArrayList<>();
        var split = table.split( "\\." );
        String schemaName = split[ 1 ];
        String tableName = split[ 2 ];
        try ( ResultSet schemas = metaData.getSchemas() ) {
            while ( schemas.next() ) {
                var dbSchemaName = schemas.getString( SQL_CONSTANTS.TABLE_SCHEM );
                if ( schemaName.equals( dbSchemaName ) ) {
                    try ( ResultSet tables = metaData.getTables( null, schemaName, "%", new String[]{ SQL_CONSTANTS.TABLE } ) ) {
                        while ( tables.next() ) {
                            String dbTableName = tables.getString( SQL_CONSTANTS.TABLE_NAME );
                            if ( dbTableName.equals( tableName ) ) {
                                try ( ResultSet columns = metaData.getColumns( null, schemaName, tableName, "%" ) ) {
                                    while ( columns.next() ) {
                                        var dbColumnName = columns.getString( SQL_CONSTANTS.COLUMN_NAME );
                                        columnsList.add( new TableColumn( dbColumnName, dbColumnName, dbColumnName,
                                                mapSQLTypeToFilterType( columns ) ) );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_COLUMNS_FROM_DATABASE.getKey() ) );
        }
        return columnsList;
    }

    /**
     * Gets widget type from json.
     *
     * @param widgetJson
     *         the widget json
     *
     * @return the widget type from json
     */
    public static String getWidgetTypeFromJson( String widgetJson ) {
        try {
            return JsonUtils.getValue( widgetJson, WIDGET_FIELDS.WIDGET_TYPE );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.KEY_NOT_FOUND_IN_PAYLOAD.getKey(), WIDGET_FIELDS.WIDGET_TYPE ) );
        }
    }

    /**
     * Prepare widget dto from json dashboard widget dto.
     *
     * @param widgetJson
     *         the widget json
     *
     * @return the dashboard widget dto
     */
    public static DashboardWidgetDTO prepareWidgetDTOFromJson( String widgetJson ) {
        DashboardWidgetDTO dashboardWidgetDTO = JsonUtils.jsonToObject( widgetJson, DashboardWidgetDTO.class );
        dashboardWidgetDTO.setSource( prepareSourceFromJson( widgetJson ) );
        dashboardWidgetDTO.setOptions( prepareOptionsFromJson( widgetJson ) );
        Map< String, Object > widgetConfig = sanitizeWidgetConfiguration( widgetJson );
        dashboardWidgetDTO.setConfiguration( widgetConfig );
        return dashboardWidgetDTO;
    }

    /**
     * Sanitize widget configuration map.
     *
     * @param widgetJson
     *         the widget json
     *
     * @return the map
     */
    private static Map< String, Object > sanitizeWidgetConfiguration( String widgetJson ) {
        Map< String, Object > widgetConfig = ( Map< String, Object > ) JsonUtils.jsonToMap( widgetJson, new HashMap<>() );
        widgetConfig.remove( "configuration" );
        widgetConfig.remove( "source" );
        widgetConfig.remove( "options" );
        return widgetConfig;
    }

    /**
     * Prepare options from json widget options.
     *
     * @param widgetJson
     *         the widget json
     *
     * @return the widget options
     */
    private static WidgetOptions prepareOptionsFromJson( String widgetJson ) {
        var type = getWidgetTypeFromJson( widgetJson );
        if ( type == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.KEY_NOT_FOUND_IN_PAYLOAD.getKey(), WIDGET_FIELDS.TYPE ) );
        }
        return switch ( WIDGET_TYPE.getById( type ) ) {
            case MIX_CHART -> JsonUtils.jsonToObject( widgetJson, MixChartWidgetOptions.class );
            case BAR -> JsonUtils.jsonToObject( widgetJson, BarWidgetOptions.class );
            case SCATTER -> JsonUtils.jsonToObject( widgetJson, ScatterWidgetOptions.class );
            case LINE -> getLineWidgetOptions( widgetJson );
            case PST -> JsonUtils.jsonToObject( widgetJson, PstWidgetOptions.class );
            case TREEMAP -> JsonUtils.jsonToObject( widgetJson, TreemapWidgetOptions.class );
            case GROUP -> JsonUtils.jsonToObject( widgetJson, GroupWidgetOptions.class );
            case TEXT -> JsonUtils.jsonToObject( widgetJson, TextWidgetOptions.class );
            case RADAR -> JsonUtils.jsonToObject( widgetJson, RadarWidgetOptions.class );
            case METAL_BASE -> JsonUtils.jsonToObject( widgetJson, MetalBaseWidgetOptions.class );
            case TABLE -> JsonUtils.jsonToObject( widgetJson, TableWidgetOptions.class );
            case PROJECT_LIFE_CYCLE -> JsonUtils.jsonToObject( widgetJson, ProjectLifeCycleWidgetOptions.class );
        };
    }

    /**
     * Gets line widget options.
     *
     * @param widgetJson
     *         the widget json
     *
     * @return the line widget options
     */
    private static LineWidgetOptions getLineWidgetOptions( String widgetJson ) {
        String type;
        try {
            type = JsonUtils.getValue( widgetJson, LINE_WIDGET_FIELDS.LINE_WIDGET_TYPE );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.KEY_NOT_FOUND_IN_PAYLOAD.getKey(), WIDGET_FIELDS.WIDGET_TYPE ) );
        }
        if ( type == null ) {
            return JsonUtils.jsonToObject( widgetJson, LineWidgetOptions.class );
        }
        return switch ( LINE_WIDGET_OPTIONS.getById( type ) ) {
            case MULTIPLE_Y_SINGLE_X -> JsonUtils.jsonToObject( widgetJson, LineWidgetMultipleYOptions.class );
            case MULTIPLE_X_SINGLE_Y -> JsonUtils.jsonToObject( widgetJson, LineWidgetMultipleXOptions.class );
            case MULTIPLE_X_MULTIPLE_Y -> JsonUtils.jsonToObject( widgetJson, LineWidgetMultipleXYOptions.class );
        };
    }

    /**
     * Prepare source from json widget source.
     *
     * @param widgetJson
     *         the widget json
     *
     * @return the widget source
     */
    private static WidgetSource prepareSourceFromJson( String widgetJson ) {
        var type = getSourceTypeFromWidgetJson( widgetJson );
        if ( type == null ) {
            return null;
        }
        return switch ( WIDGET_SOURCE_TYPE.getById( type ) ) {
            case QUERY_BUILDER -> JsonUtils.jsonToObject( widgetJson, WidgetQueryBuilderSource.class );
            case PYTHON -> JsonUtils.jsonToObject( widgetJson, WidgetPythonSource.class );
            case OTHER -> {
                String otherSource = getOtherSourceNameFromWidgetJson( widgetJson );
                if ( otherSource == null ) {
                    yield JsonUtils.jsonToObject( widgetJson, WidgetOtherSource.class );
                }
                yield switch ( OTHER_WIDGET_SOURCE.getById( otherSource ) ) {
                    case METAL_BASE -> JsonUtils.jsonToObject( widgetJson, WidgetMetalBaseSource.class );
                    case MATERIAL_INSPECTION -> JsonUtils.jsonToObject( widgetJson, WidgetMaterialInspectorSource.class );
                    case PST -> JsonUtils.jsonToObject( widgetJson, WidgetPstSource.class );
                    case VMCL -> JsonUtils.jsonToObject( widgetJson, WidgetVmclSource.class );
                };
            }
        };
    }

    /**
     * Gets source type from widget json.
     *
     * @param widgetJson
     *         the widget json
     *
     * @return the source type from widget json
     */
    private static String getSourceTypeFromWidgetJson( String widgetJson ) {
        try {
            return JsonUtils.getValue( widgetJson, WIDGET_FIELDS.DATA_SOURCE_TYPE );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.KEY_NOT_FOUND_IN_PAYLOAD.getKey(), WIDGET_FIELDS.DATA_SOURCE_TYPE ) );
        }
    }

    /**
     * Gets other source name from widget json.
     *
     * @param widgetJson
     *         the widget json
     *
     * @return the other source name from widget json
     */
    private static String getOtherSourceNameFromWidgetJson( String widgetJson ) {
        try {
            return JsonUtils.getValue( widgetJson, WIDGET_FIELDS.NAME );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.KEY_NOT_FOUND_IN_PAYLOAD.getKey(), WIDGET_FIELDS.NAME ) );
        }
    }

    /**
     * Prepare widget dto from entity dashboard widget dto.
     *
     * @param widgetEntity
     *         the widget entity
     *
     * @return the dashboard widget dto
     */
    public static DashboardWidgetDTO prepareWidgetDTOFromEntity( DashboardWidgetEntity widgetEntity ) {
        var json = ByteUtil.convertByteToString( widgetEntity.getConfiguration() );
        Map< String, Object > config = ( Map< String, Object > ) JsonUtils.jsonToMap( json, new HashMap<>() );
        var dto = DataDashboardUtil.prepareWidgetDTOFromJson( json );
        dto.setId( widgetEntity.getCompositeId().getId().toString() );
        dto.setVersionId( widgetEntity.getCompositeId().getVersionId() );
        dto.setConfiguration( config );
        return dto;
    }

    /**
     * Run query for bar widget dynamic query response.
     *
     * @param dataSourceDTO
     *         the data source dto
     * @param dto
     *         the dto
     *
     * @return the dynamic query response
     */
    public static DynamicQueryResponse runQueryForBarWidget( DataSourceDTO dataSourceDTO, DashboardWidgetDTO dto ) {
        String query = DashboardQueryBuilder.getQueryForBarWidget( dto );
        JdbcTemplate jdbcTemplate = DataDashboardUtil.getJdbcTemplate( ( DatabaseDataSourceDTO ) dataSourceDTO );
        try {
            return fetchDataFromQuery( jdbcTemplate, query, dto );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Run query for bar widget dynamic query response.
     *
     * @param dataSourceDTO
     *         the data source dto
     * @param dto
     *         the dto
     *
     * @return the dynamic query response
     */
    public static DynamicQueryResponse runQueryForScatterWidget( DataSourceDTO dataSourceDTO, DashboardWidgetDTO dto ) {
        String query = DashboardQueryBuilder.getQueryForScatterWidget( dto );
        JdbcTemplate jdbcTemplate = DataDashboardUtil.getJdbcTemplate( ( DatabaseDataSourceDTO ) dataSourceDTO );
        try {
            return fetchDataFromQuery( jdbcTemplate, query, dto );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Run query for text widget dynamic query response.
     *
     * @param dataSourceDTO
     *         the data source dto
     * @param dto
     *         the dto
     *
     * @return the dynamic query response
     */
    public static DynamicQueryResponse runQueryForTextWidget( DataSourceDTO dataSourceDTO, DashboardWidgetDTO dto ) {
        var options = ( TextWidgetOptions ) dto.getOptions();
        if ( TEXT_WIDGET_INPUT_METHOD.getById( options.getInputMethod() ).equals( TEXT_WIDGET_INPUT_METHOD.USER ) ) {
            var response = new DynamicQueryResponse( new DynamicQueryMetadata(), new DynamicQueryData() );
            response.getData()
                    .put( DataDashboardConstants.TEXT_WIDGET_FIELDS.DISPLAY_VALUE, Collections.singletonList( options.getDisplayValue() ) );
            response.getMetadata().setMetadataByColumn( TEXT_WIDGET_FIELDS.DISPLAY_VALUE, "String", TEXT_WIDGET_FIELDS.DISPLAY_VALUE );
            return response;
        }
        String query = DashboardQueryBuilder.getQueryForTextWidget( dto );
        JdbcTemplate jdbcTemplate = DataDashboardUtil.getJdbcTemplate( ( DatabaseDataSourceDTO ) dataSourceDTO );
        try {
            return fetchDataFromQuery( jdbcTemplate, query, dto );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Fetch data from query dynamic query response.
     *
     * @param jdbcTemplate
     *         the jdbc template
     * @param query
     *         the query
     * @param dto
     *         the dto
     *
     * @return the dynamic query response
     */
    public static DynamicQueryResponse fetchDataFromQuery( JdbcTemplate jdbcTemplate, String query, DashboardWidgetDTO dto ) {
        log.info( "query: {}", query );
        return jdbcTemplate.query( con -> {
            // Prepare the statement and set the max rows here
            PreparedStatement ps = con.prepareStatement( query );
            ps.setMaxRows( DashboardConfigUtil.getQueryRecordsLimit() );  // Set the maximum number of rows here
            return ps;
        }, ( ResultSet rs ) -> {
            DynamicQueryResponse responseObject = new DynamicQueryResponse();
            DynamicQueryData queryData = new DynamicQueryData();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Fetch metadata and data
            fetchQueryMetadata( columnCount, metaData, responseObject, dto );
            fetchQueryData( rs, columnCount, metaData, queryData, responseObject );

            return responseObject;
        } );
    }

    /**
     * Fetch query metadata.
     *
     * @param columnCount
     *         the column count
     * @param metaData
     *         the meta data
     * @param responseObject
     *         the response object
     * @param dto
     *         the dto
     *
     * @throws SQLException
     *         the sql exception
     */
    private static void fetchQueryMetadata( int columnCount, ResultSetMetaData metaData, DynamicQueryResponse responseObject,
            DashboardWidgetDTO dto ) throws SQLException {
        DynamicQueryMetadata sqlDynamicQueryMetadata = new DynamicQueryMetadata();
        for ( int i = 1; i <= columnCount; i++ ) {
            String columnName = metaData.getColumnName( i );
            String columnType = mapSQLTypeToClientType( metaData.getColumnType( i ) );
            String columnTitle = getColumnTitle( columnName, dto );
            sqlDynamicQueryMetadata.setMetadataByColumn( columnName, columnType, columnTitle );
        }
        responseObject.setMetadata( sqlDynamicQueryMetadata );
    }

    /**
     * Gets column title.
     *
     * @param columnName
     *         the column name
     * @param dto
     *         the dto
     *
     * @return the column title
     */
    private static String getColumnTitle( String columnName, DashboardWidgetDTO dto ) {
        switch ( WIDGET_TYPE.getById( dto.getType() ) ) {
            case MIX_CHART -> {
                if ( columnName.equals( WIDGET_FIELDS.X_AXIS ) ) {
                    return ( ( MixChartWidgetOptions ) dto.getOptions() ).getX_axis_title();
                } else if ( columnName.equals( WIDGET_FIELDS.Y_AXIS ) ) {
                    return ( ( MixChartWidgetOptions ) dto.getOptions() ).getY_axis_title();
                }
            }
            case BAR -> {
                if ( columnName.equals( WIDGET_FIELDS.X_AXIS ) ) {
                    return ( ( BarWidgetOptions ) dto.getOptions() ).getX_axis_title();
                } else if ( columnName.equals( WIDGET_FIELDS.Y_AXIS ) ) {
                    return ( ( BarWidgetOptions ) dto.getOptions() ).getY_axis_title();
                }
            }
            case RADAR, METAL_BASE, LINE, TABLE -> {
                return columnName;
            }
            case TEXT -> {
                return ( ( TextWidgetOptions ) dto.getOptions() ).getColumnTitle();
            }
            case SCATTER -> {
                if ( columnName.equals( WIDGET_FIELDS.X_AXIS ) ) {
                    return ( ( ScatterWidgetOptions ) dto.getOptions() ).getX_axis_title();
                } else if ( columnName.equals( WIDGET_FIELDS.Y_AXIS ) ) {
                    return ( ( ScatterWidgetOptions ) dto.getOptions() ).getY_axis_title();
                }
            }
        }
        return null;
    }

    /**
     * Fetch query data.
     *
     * @param rs
     *         the rs
     * @param columnCount
     *         the column count
     * @param metaData
     *         the meta data
     * @param queryData
     *         the query data
     * @param responseObject
     *         the response object
     *
     * @throws SQLException
     *         the sql exception
     */
    private static void fetchQueryData( ResultSet rs, int columnCount, ResultSetMetaData metaData, DynamicQueryData queryData,
            DynamicQueryResponse responseObject ) throws SQLException {
        while ( rs.next() ) {
            for ( int i = 1; i <= columnCount; i++ ) {
                String columnName = metaData.getColumnName( i );
                Object value = rs.getObject( i );
                queryData.addValueByColumn( columnName, value );
            }

        }
        responseObject.setData( queryData );
    }

    /**
     * Map sql type to client type string.
     *
     * @param sqlType
     *         the sql type
     *
     * @return the string
     */
    public static String mapSQLTypeToClientType( int sqlType ) {
        return switch ( sqlType ) {
            case Types.VARCHAR, Types.CHAR, Types.LONGVARCHAR, Types.CLOB -> "String";
            case Types.INTEGER, Types.TINYINT, Types.SMALLINT, Types.BIGINT -> "Integer";
            case Types.FLOAT, Types.REAL, Types.DOUBLE, Types.DECIMAL, Types.NUMERIC -> "Double";
            case Types.BOOLEAN, Types.BIT -> "Boolean";
            case Types.DATE, Types.TIME, Types.TIMESTAMP -> "Date";
            case Types.BLOB -> "Blob";
            case Types.ARRAY -> "Array";
            case Types.NULL -> "Null";
            default -> "Object";  // Generic Object type for unknown mappings
        };
    }

    /**
     * Gets python script field.
     *
     * @return the python script field
     */
    public static CodeFormItem getPythonScriptField() {
        CodeFormItem item = ( CodeFormItem ) GUIUtils.getFormItemByField( WidgetPythonSource.class, WIDGET_FIELDS.PYTHON_SCRIPT );
        item.setSyntax( "python" );
        return item;
    }

    /**
     * Map sql type to filter type string.
     *
     * @param resultSet
     *         the result set
     *
     * @return the string
     *
     * @throws SQLException
     *         the sql exception
     */
    public static String mapSQLTypeToFilterType( ResultSet resultSet ) throws SQLException {
        return switch ( resultSet.getInt( "DATA_TYPE" ) ) {
            case Types.VARCHAR, Types.CHAR, Types.LONGVARCHAR, Types.CLOB, Types.BLOB -> FilterType.text.name();
            case Types.INTEGER, Types.TINYINT, Types.SMALLINT, Types.BIGINT, Types.FLOAT, Types.REAL, Types.DOUBLE, Types.DECIMAL,
                 Types.NUMERIC, Types.BOOLEAN, Types.BIT -> FilterType.number.name();
            case Types.DATE, Types.TIME, Types.TIMESTAMP -> FilterType.dateRange.name();
            case Types.ROWID -> FilterType.uuid.name();
            default -> null;  // Generic type for unknown mappings
        };
    }

    /**
     * Run query for line widget dynamic query response.
     *
     * @param dataSourceDTO
     *         the data source dto
     * @param dto
     *         the dto
     *
     * @return the dynamic query response
     */
    public static DynamicQueryResponse runQueryForLineWidget( DataSourceDTO dataSourceDTO, DashboardWidgetDTO dto ) {
        String query = DashboardQueryBuilder.getQueryForLineWidget( dto );
        JdbcTemplate jdbcTemplate = DataDashboardUtil.getJdbcTemplate( ( DatabaseDataSourceDTO ) dataSourceDTO );
        try {
            return fetchDataFromQuery( jdbcTemplate, query, dto );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Run query for multi chart widget dynamic query response.
     *
     * @param dataSourceDTO
     *         the data source dto
     * @param dto
     *         the dto
     *
     * @return the dynamic query response
     */
    public static DynamicQueryResponse runQueryForMultiChartWidget( DataSourceDTO dataSourceDTO, DashboardWidgetDTO dto ) {
        String query = DashboardQueryBuilder.getQueryForMultiChartWidget( dto );
        JdbcTemplate jdbcTemplate = DataDashboardUtil.getJdbcTemplate( ( DatabaseDataSourceDTO ) dataSourceDTO );
        try {
            return fetchDataFromQuery( jdbcTemplate, query, dto );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Sets line widget smooth options.
     *
     * @param item
     *         the item
     */
    public static void setLineWidgetSmoothOptions( SelectFormItem item ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( "Yes", "Yes" ) );
        options.add( new SelectOptionsUI( "No", "No" ) );
        item.setOptions( options );
    }

    /**
     * Populate color scheme options.
     *
     * @param item
     *         the item
     */
    public static void populateColorSchemeOptions( SelectFormItem item ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var colorScheme : COLOR_SCHEMES.values() ) {
            options.add( new SelectOptionsUI( colorScheme.getId(), colorScheme.getName() ) );
        }
        item.setOptions( options );

    }

    /**
     * Sets bind to for group by columns for bar.
     *
     * @param item
     *         the item
     * @param widgetId
     *         the widget type
     * @param objectId
     *         the object id
     */
    public static void setBindToForGroupByColumnsForBar( SelectFormItem item, String widgetId, String objectId ) {
        var bindTo = new BindTo( BIND_TO_URLS.WIDGET_GROUP_BY_FOR_AGGREGATE.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                .replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ), null, BAR_WIDGET_FIELDS.BAR_COLOR );
        item.setBindTo( bindTo );
    }

    /**
     * Gets aggregate values by type.
     *
     * @param columnType
     *         the column type
     *
     * @return the aggregate values by type
     */
    public static List< SelectOptionsUI > getAggregateValuesByType( String columnType ) {
        List< SelectOptionsUI > aggregateOptions = new ArrayList<>();
        switch ( columnType ) {
            case "int4", "int2", "numeric", "float4", "float8", "int8" -> getAllAggregateOptions( aggregateOptions );
            default -> getGenericAggregateOptions( aggregateOptions );
        }
        return aggregateOptions;
    }

    /**
     * Gets generic aggregate options.
     *
     * @param aggregateOptions
     *         the aggregate options
     */
    private static void getGenericAggregateOptions( List< SelectOptionsUI > aggregateOptions ) {
        aggregateOptions.add( new SelectOptionsUI( SqlAggregateOptions.COUNT.getId(), SqlAggregateOptions.COUNT.getName() ) );
    }

    /**
     * Gets all aggregate options.
     *
     * @param aggregateOptions
     *         the aggregate options
     */
    public static void getAllAggregateOptions( List< SelectOptionsUI > aggregateOptions ) {
        for ( var sqlAggregateOption : SqlAggregateOptions.values() ) {
            aggregateOptions.add( new SelectOptionsUI( sqlAggregateOption.getId(), sqlAggregateOption.getName() ) );
        }
    }

    /**
     * Sets python status options.
     *
     * @param item
     *         the item
     * @param pythonOutput
     *         the python output
     */
    public static void setPythonStatusOptions( SelectFormItem item, String pythonOutput ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var scriptStatusOption : ScriptStatusOption.values() ) {
            options.add(
                    new SelectOptionsUI( pythonOutput + ConstantsString.DOT + scriptStatusOption.getId(), scriptStatusOption.getName() ) );
        }
        item.setOptions( options );
    }

    /**
     * Gets python status field.
     *
     * @param pythonOutput
     *         the python output
     *
     * @return the python status field
     */
    public static SelectFormItem getPythonStatusField( String pythonOutput ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.getFormItemByField( WidgetPythonSource.class, WIDGET_FIELDS.PYTHON_STATUS );
        setPythonStatusOptions( item, pythonOutput );
        return item;
    }

    /**
     * Validate widget source options.
     *
     * @param dto
     *         the dto
     */
    public static void validateWidgetSourceOptions( DashboardWidgetDTO dto ) {
        if ( !( dto.getSource() != null && !StringUtils.isEmpty( dto.getId() ) && !StringUtils.isEmpty( dto.getType() )
                && !StringUtils.isEmpty( dto.getSource().getSourceType() ) ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.INCOMPLETE_WIDGET_FOR_QUERY.getKey(), dto.getOptions().getTitle(),
                            dto.getId() ) );
        }
    }

    /**
     * Validate widget for query.
     *
     * @param dto
     *         the dto
     * @param dataSource
     *         the data source
     */
    public static void validateWidgetForResultSet( DashboardWidgetDTO dto, DataSourceDTO dataSource ) {
        if ( !validateWidgetByWidgetType( dto, dataSource ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.INCOMPLETE_WIDGET_FOR_QUERY.getKey(), dto.getOptions().getTitle(),
                            dto.getId() ) );
        }
    }

    /**
     * Validate widget by widget type boolean.
     *
     * @param dto
     *         the dto
     * @param dataSource
     *         the data source
     *
     * @return the boolean
     */
    private static boolean validateWidgetByWidgetType( DashboardWidgetDTO dto, DataSourceDTO dataSource ) {
        return switch ( WIDGET_TYPE.getById( dto.getType() ) ) {
            case MIX_CHART -> {
                MixChartWidgetOptions multiChartDto = ( MixChartWidgetOptions ) dto.getOptions();
                yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && !CollectionUtils.isEmpty(
                        multiChartDto.getCurveOptions() );
            }
            case BAR -> {
                BarWidgetOptions barDto = ( BarWidgetOptions ) dto.getOptions();
                yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && !StringUtils.isEmpty( barDto.getX_axis() )
                        && !StringUtils.isEmpty( barDto.getY_axis() );
            }
            case RADAR -> {
                RadarWidgetOptions radarDto = ( RadarWidgetOptions ) dto.getOptions();
                yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && !CollectionUtils.isEmpty( radarDto.getValue() )
                        && !StringUtils.isEmpty( radarDto.getIndicator() );
            }
            case METAL_BASE -> validateGenericDashboardWidgetDTOFields( dto, dataSource );
            case TEXT -> {
                TextWidgetOptions textDto = ( TextWidgetOptions ) dto.getOptions();
                yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && !StringUtils.isEmpty( textDto.getInputMethod() )
                        && switch ( TEXT_WIDGET_INPUT_METHOD.getById( textDto.getInputMethod() ) ) {
                    case USER -> !StringUtils.isEmpty( textDto.getDisplayValue() );
                    case SELECT -> !StringUtils.isEmpty( textDto.getColumn() );
                };
            }
            case SCATTER -> {
                ScatterWidgetOptions scatterWidgetDTO = ( ScatterWidgetOptions ) dto.getOptions();
                yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && !StringUtils.isEmpty( scatterWidgetDTO.getX_axis() )
                        && !StringUtils.isEmpty( scatterWidgetDTO.getY_axis() );
            }
            case LINE -> {
                LineWidgetOptions lineDto = ( LineWidgetOptions ) dto.getOptions();
                yield switch ( LINE_WIDGET_OPTIONS.getById( lineDto.getLineWidgetType() ) ) {
                    case MULTIPLE_X_SINGLE_Y -> {
                        LineWidgetMultipleXOptions multipleXOptions = ( LineWidgetMultipleXOptions ) lineDto;
                        yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && !StringUtils.isEmpty(
                                multipleXOptions.getY_axis() ) && !CollectionUtils.isEmpty( multipleXOptions.getX_axis() );
                    }
                    case MULTIPLE_Y_SINGLE_X -> {
                        LineWidgetMultipleYOptions multipleYOptions = ( LineWidgetMultipleYOptions ) lineDto;
                        yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && !StringUtils.isEmpty(
                                multipleYOptions.getX_axis() ) && !CollectionUtils.isEmpty( multipleYOptions.getY_axis() );
                    }
                    case MULTIPLE_X_MULTIPLE_Y -> {
                        LineWidgetMultipleXYOptions multipleXYOptions = ( LineWidgetMultipleXYOptions ) lineDto;
                        yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && !CollectionUtils.isEmpty(
                                multipleXYOptions.getY_axis() ) && !CollectionUtils.isEmpty( multipleXYOptions.getX_axis() );
                    }
                };

            }
            case PST -> {
                PstWidgetOptions pstDto = ( PstWidgetOptions ) dto.getOptions();
                yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && !StringUtils.isEmpty( pstDto.getUpdateInterval() );
            }
            case TREEMAP -> {
                TreemapWidgetOptions treemapDto = ( TreemapWidgetOptions ) dto.getOptions();
                yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && treemapDto.getLeafDepth() > 0;
            }
            case GROUP -> {
                GroupWidgetOptions groupOptions = ( GroupWidgetOptions ) dto.getOptions();
                yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && CollectionUtils.isNotEmpty( groupOptions.getWidgets() );
            }
            case TABLE -> {
                TableWidgetOptions tableOptions = ( TableWidgetOptions ) dto.getOptions();
                yield validateGenericDashboardWidgetDTOFields( dto, dataSource ) && CollectionUtils.isNotEmpty(
                        tableOptions.getTableColumns() );
            }
            case PROJECT_LIFE_CYCLE -> {
                ProjectLifeCycleWidgetOptions projectLifecycleOptions = ( ProjectLifeCycleWidgetOptions ) dto.getOptions();
                yield validateGenericDashboardWidgetDTOFields( dto, dataSource );
            }
        };
    }

    /**
     * Validate generic dto fields boolean.
     *
     * @param dto
     *         the dto
     * @param dataSource
     *         the data source
     *
     * @return the boolean
     */
    private static boolean validateGenericDashboardWidgetDTOFields( DashboardWidgetDTO dto, DataSourceDTO dataSource ) {
        if ( StringUtils.isEmpty( dto.getId() ) || StringUtils.isEmpty( dto.getType() ) || StringUtils.isEmpty(
                dto.getSource().getSourceType() ) ) {
            return false;
        }
        return switch ( WIDGET_SOURCE_TYPE.getById( dto.getSource().getSourceType() ) ) {
            case PYTHON -> {
                var pythonSource = ( WidgetPythonSource ) dto.getSource();
                var split = pythonSource.getScriptOption().split( "\\." );
                String scriptOption = split[ 1 ];
                yield switch ( WIDGET_SCRIPT_OPTION.getById( scriptOption ) ) {
                    case USER ->
                            !StringUtils.isEmpty( pythonSource.getPythonScript() ) && !StringUtils.isEmpty( pythonSource.getPythonStatus() )
                                    && !StringUtils.isEmpty( pythonSource.getOutputType() );
                    case FILE ->
                            !StringUtils.isEmpty( pythonSource.getSelectScript() ) && !StringUtils.isEmpty( pythonSource.getPythonStatus() )
                                    && !StringUtils.isEmpty( pythonSource.getOutputType() );
                    case SYSTEM -> !StringUtils.isEmpty( pythonSource.getSystemScript() ) && !StringUtils.isEmpty(
                            pythonSource.getPythonStatus() );
                };

            }
            case QUERY_BUILDER -> {
                var qbSource = ( WidgetQueryBuilderSource ) dto.getSource();
                yield switch ( DashboardDataSourceOptions.getById( dataSource.getSourceType() ) ) {
                    case DATABASE -> !StringUtils.isEmpty( qbSource.getSchema() ) && !StringUtils.isEmpty( qbSource.getTable() );
                    case CSV, JSON, SUS_OBJECT -> true;
                    case EXCEL -> !StringUtils.isEmpty( qbSource.getSchema() );
                };
            }
            case OTHER -> true;
        };

    }

    /**
     * Sets options in python output form item.
     *
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param dataSourceType
     *         the data source type
     * @param setBindFrom
     *         the set bind from
     */
    public static void setOptionsInPythonOutputFormItem( UIFormItem item, String objectId, String dataSourceType, boolean setBindFrom ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var pythonInputOption : WidgetPythonOutputOptions.values() ) {
            options.add(
                    new SelectOptionsUI( dataSourceType + ConstantsString.DOT + pythonInputOption.getId(), pythonInputOption.getName() ) );
        }
        ( ( SelectFormItem ) item ).setOptions( options );
        if ( setBindFrom ) {
            ( ( SelectFormItem ) item ).setBindFrom( BIND_FROM_URLS.PYTHON_OUTPUT.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ) );
        }
    }

    /**
     * Sets options in python output form item.
     *
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param dataSourceType
     *         the data source type
     */
    public static void setOptionsInPythonScripOptionsItem( SelectFormItem item, String objectId, String dataSourceType ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var scriptOption : WIDGET_SCRIPT_OPTION.values() ) {
            options.add( new SelectOptionsUI( dataSourceType + ConstantsString.DOT + scriptOption.getId(), scriptOption.getName() ) );
        }
        item.setOptions( options );
        item.setBindFrom( BIND_FROM_URLS.PYTHON_SCRIPT_OPTIONS.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ) );
    }

    /**
     * Prepare widget fields for system script list.
     *
     * @return the list
     */
    public static List< UIFormItem > prepareWidgetFieldsForSystemScript() {
        List< UIFormItem > items = new ArrayList<>();
        var scriptItem = ( SelectFormItem ) GUIUtils.getFormItemByField( WidgetPythonSource.class, WIDGET_FIELDS.SYSTEM_SCRIPT );
        var scripts = getDynamicScriptFieldsFromConfigByKey( DASHBOARD_SCRIPTS_KEY );
        List< SelectOptionsUI > options = new ArrayList<>();
        scripts.forEach( script -> options.add( new SelectOptionsUI( script.getId(), script.getName() ) ) );
        scriptItem.setOptions( options );
        items.add( scriptItem );
        return items;
    }

    /**
     * Prepare widget fields for user script list.
     *
     * @param objectId
     *         the object id
     * @param dataSourceType
     *         the data source type
     *
     * @return the list
     */
    public static List< UIFormItem > prepareWidgetFieldsForUserScript( String objectId, String dataSourceType ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetPythonSource.class, WIDGET_FIELDS.PYTHON_OUTPUT_TYPE,
                WIDGET_FIELDS.PYTHON_STATUS );
        for ( var item : items ) {
            if ( item.getName().equals( WIDGET_FIELDS.PYTHON_OUTPUT_TYPE ) ) {
                setOptionsInPythonOutputFormItem( item, objectId, dataSourceType, true );
            } else if ( item.getName().equals( WIDGET_FIELDS.PYTHON_STATUS ) ) {
                setPythonStatusOptions( ( SelectFormItem ) item, WidgetPythonOutputOptions.CSV.getId() );
            }
        }

        return items;
    }

    /**
     * Prepare widget fields for select script list.
     *
     * @param objectId
     *         the object id
     * @param dataSourceType
     *         the data source type
     *
     * @return the list
     */
    public static List< UIFormItem > prepareWidgetFieldsForSelectScript( String objectId, String dataSourceType ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetPythonSource.class, WIDGET_FIELDS.PYTHON_OUTPUT_TYPE,
                WIDGET_FIELDS.SELECT_SCRIPT, WIDGET_FIELDS.PYTHON_STATUS );
        for ( var item : items ) {
            if ( item.getName().equals( WIDGET_FIELDS.PYTHON_OUTPUT_TYPE ) ) {
                setOptionsInPythonOutputFormItem( item, objectId, dataSourceType, false );
            } else if ( item.getName().equals( WIDGET_FIELDS.PYTHON_STATUS ) ) {
                setPythonStatusOptions( ( SelectFormItem ) item, WidgetPythonOutputOptions.CSV.getId() );
            }
        }
        return items;
    }

    /**
     * Populate columns list for widget options for database list.
     *
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     *
     * @return the list
     */
    public static List< SelectOptionsUI > populateColumnsListForWidgetOptionsForDatabase( DashboardWidgetDTO widget,
            DatabaseDataSourceDTO selectedDataSource ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( selectedDataSource );
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            List< SelectOptionsUI > columnsList = new ArrayList<>();
            var widgetSource = ( WidgetQueryBuilderSource ) widget.getSource();
            DataDashboardUtil.addColumnOptionsToList( widgetSource.getTable(), metaData, columnsList );
            return columnsList;
        } catch ( SQLException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_COLUMNS_FROM_DATABASE.getKey() ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Populate table columns for widget filters for database list.
     *
     * @param widgetSource
     *         the widget source
     * @param selectedDataSource
     *         the selected data source
     *
     * @return the list
     */
    public static List< TableColumn > populateTableColumnsForWidgetFiltersForDatabase( WidgetQueryBuilderSource widgetSource,
            DatabaseDataSourceDTO selectedDataSource ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( selectedDataSource );
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            var split = widgetSource.getTable().split( "\\." );
            String schemaName = split[ 1 ];
            String tableName = split[ 2 ];
            List< TableColumn > sqlDynamicQueryMetadata = null;
            try ( ResultSet schemas = metaData.getSchemas() ) {
                while ( schemas.next() ) {
                    var dbSchemaName = schemas.getString( SQL_CONSTANTS.TABLE_SCHEM );
                    if ( dbSchemaName.equals( schemaName ) ) {
                        try ( ResultSet tables = metaData.getTables( null, schemaName, "%", new String[]{ SQL_CONSTANTS.TABLE } ) ) {
                            while ( tables.next() ) {
                                String dbTableName = tables.getString( SQL_CONSTANTS.TABLE_NAME );
                                if ( tableName.equals( dbTableName ) ) {
                                    try ( ResultSet columns = metaData.getColumns( null, schemaName, tableName, "%" ) ) {
                                        sqlDynamicQueryMetadata = new ArrayList<>();
                                        while ( columns.next() ) {
                                            String columnName = columns.getString( SQL_CONSTANTS.COLUMN_NAME );
                                            sqlDynamicQueryMetadata.add( new TableColumn( columnName, columnName, columnName,
                                                    mapTYPE_NAMEToFilterType( columns.getString( SQL_CONSTANTS.TYPE_NAME ) ) ) );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_COLUMNS_FROM_DATABASE.getKey() ) );
            }
            return sqlDynamicQueryMetadata;
        } catch ( SQLException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_COLUMNS_FROM_DATABASE.getKey() ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Map type name to filter type string.
     *
     * @param TYPE_NAME
     *         the type name
     *
     * @return the string
     */
    public static String mapTYPE_NAMEToFilterType( String TYPE_NAME ) {
        return switch ( TYPE_NAME.toUpperCase() ) {
            case "INT", "INTEGER", "BIGINT", "SMALLINT", "TINYINT", "DECIMAL", "NUMERIC", "NUMBER", "FLOAT", "DOUBLE", "REAL" -> "number";
            case "DATE", "TIME", "TIME WITHOUT TIME ZONE", "TIMESTAMP", "DATETIME", "TIMESTAMP WITH TIME ZONE" -> "dateRange";
            case "BOOLEAN", "BOOL" -> "select";
            case "UUID" -> "id";
            default -> "text";
        };
    }

    /**
     * Sets bind from for line widget type.
     *
     * @param objectId
     *         the object id
     * @param id
     *         the id
     * @param item
     *         the item
     */
    public static void setBindFromForLineWidgetType( String objectId, String id, SelectFormItem item ) {
        item.setBindFrom( BIND_FROM_URLS.LINE_WIDGET_OPTIONS.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                .replace( URL_PARAMS.PARAM_WIDGET_ID, id ) );
        item.setOptions(
                Arrays.stream( LINE_WIDGET_OPTIONS.values() ).map( option -> new SelectOptionsUI( option.getId(), option.getName() ) )
                        .toList() );
        if ( item.getValue() == null ) {
            item.setValue( LINE_WIDGET_OPTIONS.MULTIPLE_X_MULTIPLE_Y.getId() );
        }
    }

    /**
     * Sets relation in widget if not set in entity.
     *
     * @param dashboardWidgetDTO
     *         the dashboard widget dto
     */
    public static void setRelationInWidgetIfNotSetInEntity( DashboardWidgetDTO dashboardWidgetDTO ) {
        if ( dashboardWidgetDTO.getType().equals( WIDGET_TYPE.GROUP.getId() ) ) {
            dashboardWidgetDTO.setRelation( WIDGET_RELATION.IS_GROUP );
        } else if ( dashboardWidgetDTO.getGroupId() != null ) {
            dashboardWidgetDTO.setRelation( WIDGET_RELATION.IS_IN_GROUP );
        } else {
            dashboardWidgetDTO.setRelation( WIDGET_RELATION.IS_NOT_IN_GROUP );
        }
    }

    /**
     * Update options fields.
     *
     * @param formItems
     *         the form items
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     * @param columnList
     *         the column list
     */
    public static void updateOptionsFields( List< UIFormItem > formItems, DashboardWidgetDTO widget, String objectId,
            List< SelectOptionsUI > columnList, String dataSourceOption ) {
        removePairsFromSelectedColumnList( widget, columnList );
        for ( Iterator< UIFormItem > iterator = formItems.iterator(); iterator.hasNext(); ) {
            UIFormItem item = iterator.next();
            switch ( item.getName() ) {
                case WIDGET_FIELDS.X_AXIS, WIDGET_FIELDS.Y_AXIS, SCATTER_WIDGET_FIELDS.POINT_COLOR, SCATTER_WIDGET_FIELDS.POINT_SIZE,
                     MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_X_AXIS, MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_Y_AXIS, BAR_WIDGET_FIELDS.BAR_COLOR,
                     TEXT_WIDGET_FIELDS.COLUMN, RADAR_WIDGET_FIELDS.INDICATOR, RADAR_WIDGET_FIELDS.VALUE, RADAR_WIDGET_FIELDS.MAX,
                     RADAR_WIDGET_FIELDS.MIN -> {
                    ( ( SelectFormItem ) item ).setOptions( columnList );
                    updateValuesWRTOptions( item, columnList );
                }
                case DataDashboardConstants.TABLE_WIDGET_FIELDS.TABLE_COLUMNS -> {
                    ( ( SelectFormItem ) item ).setOptions( columnList );
                    updateValuesWRTOptions( item, columnList );
                    if ( null == item.getValue() ) {
                        item.setValue( columnList.stream().map( SelectOptionsUI::getId ).toList() );
                    }
                }
                case BAR_WIDGET_FIELDS.COLOR_AGGREGATE -> {
                    if ( DashboardDataSourceOptions.DATABASE.getId().equalsIgnoreCase( dataSourceOption ) ) {
                        setBindToForGroupByColumnsForBar( ( SelectFormItem ) item, widget.getId(), objectId );
                        ( ( SelectFormItem ) item ).setCanBeEmpty( true );
                    } else {
                        iterator.remove();
                    }

                }
                case WIDGET_FIELDS.COLOR_THEME -> populateColorSchemeOptions( ( SelectFormItem ) item );
                case LINE_WIDGET_FIELDS.SMOOTH, MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_SMOOTH ->
                        setLineWidgetSmoothOptions( ( SelectFormItem ) item );
                case LINE_WIDGET_FIELDS.LINE_WIDGET_TYPE ->
                        setBindFromForLineWidgetType( objectId, widget.getId(), ( SelectFormItem ) item );
                case WIDGET_FIELDS.STYLING, MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_STYLING ->
                        setLineWidgetStylingOptions( ( SelectFormItem ) item );
                case MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_POINT_SYMBOL, WIDGET_FIELDS.POINT_SYMBOL ->
                        setPointSymbolOptions( ( SelectFormItem ) item );
                case MIX_CHART_WIDGET_FIELDS.ADD_CURVE ->
                        setBindFromForMixChartAddChartButton( ( ButtonFormItem ) item, widget.getId(), objectId );
                case MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_CURVE_TYPE -> iterator.remove();
                case TEXT_WIDGET_FIELDS.INPUT_METHOD ->
                        setBindFromForTextWidgetInputMethod( ( SelectFormItem ) item, widget.getId(), objectId );
                case RADAR_WIDGET_FIELDS.ADD_VALUE ->
                        setBindFromForRadarChartAddValueButton( ( ButtonFormItem ) item, widget.getId(), objectId );
                case RADAR_WIDGET_FIELDS.SHAPE -> setRadarShapeOptions( ( SelectFormItem ) item );
            }
            if ( FieldTypes.SELECTION.getType().equals( item.getType() ) ) {
                GUIUtils.setLiveSearchInSelectItem( ( SelectFormItem ) item );
            }

        }
    }

    /**
     * Remove pairs from selected column list.
     *
     * @param widget
     *         the widget
     * @param columnList
     *         the column list
     */
    private static void removePairsFromSelectedColumnList( DashboardWidgetDTO widget, List< SelectOptionsUI > columnList ) {
        var columnValues = columnList.stream().map( SelectOptionsUI::getId ).toList();
        if ( widget.getType().equals( WIDGET_TYPE.MIX_CHART.getId() ) ) {
            var options = ( MixChartWidgetOptions ) widget.getOptions();
            if ( CollectionUtils.isNotEmpty( options.getCurveOptions() ) ) {
                options.getCurveOptions()
                        .removeIf( val -> !columnValues.contains( val.getX_axis() ) || !columnValues.contains( val.getY_axis() ) );
            }
        }
        if ( widget.getType().equals( WIDGET_TYPE.LINE.getId() ) ) {
            var options = ( LineWidgetOptions ) widget.getOptions();
            if ( LINE_WIDGET_OPTIONS.MULTIPLE_X_MULTIPLE_Y.getId().equals( options.getLineWidgetType() ) ) {
                var pairOptions = ( LineWidgetMultipleXYOptions ) options;
                if ( CollectionUtils.isNotEmpty( pairOptions.getX_axis() ) && CollectionUtils.isNotEmpty( pairOptions.getY_axis() ) ) {
                    for ( int i = pairOptions.getX_axis().size() - 1; i >= 0; i-- ) {
                        if ( !columnValues.contains( pairOptions.getX_axis().get( i ) ) || !columnValues.contains(
                                pairOptions.getY_axis().get( i ) ) ) {
                            pairOptions.getX_axis().remove( i );
                            pairOptions.getY_axis().remove( i );
                        }
                    }
                }
            }
        }
    }

    private static void setRadarShapeOptions( SelectFormItem item ) {
        item.setOptions(
                Arrays.stream( RADAR_SHAPE.values() ).map( option -> new SelectOptionsUI( option.getId(), option.getName() ) ).toList() );
        if ( item.getValue() == null ) {
            item.setValue( item.getOptions().get( 0 ).getId() );
        }
    }

    /**
     * Sets bind from for text widget input method.
     *
     * @param item
     *         the item
     * @param id
     *         the id
     * @param objectId
     *         the object id
     */
    private static void setBindFromForTextWidgetInputMethod( SelectFormItem item, String id, String objectId ) {
        item.setBindFrom( BIND_FROM_URLS.TEXT_INPUT_OPTIONS.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                .replace( URL_PARAMS.PARAM_WIDGET_ID, id ) );
        item.setOptions(
                Arrays.stream( TEXT_WIDGET_INPUT_METHOD.values() ).map( option -> new SelectOptionsUI( option.getId(), option.getName() ) )
                        .toList() );
        if ( item.getValue() == null ) {
            item.setValue( TEXT_WIDGET_INPUT_METHOD.USER.getId() );
        }
    }

    /**
     * Sets point symbol options.
     *
     * @param item
     *         the item
     */
    private static void setPointSymbolOptions( SelectFormItem item ) {
        item.setOptions(
                Arrays.stream( POINT_SYMBOL.values() ).map( option -> new SelectOptionsUI( option.getId(), option.getName() ) ).toList() );
        if ( item.getValue() == null ) {
            item.setValue( item.getOptions().get( 0 ).getId() );
        }
    }

    /**
     * Sets bind to for mix chart add chart button.
     *
     * @param item
     *         the item
     * @param widgetId
     *         the widget id
     * @param objectId
     *         the object id
     */
    private static void setBindFromForMixChartAddChartButton( ButtonFormItem item, String widgetId, String objectId ) {
        item.setBindFrom( BIND_FROM_URLS.ADD_CURVE_IN_MIX_CHART.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                .replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
    }

    /**
     * Sets bind from for radar chart add value button.
     *
     * @param item
     *         the item
     * @param widgetId
     *         the widget id
     * @param objectId
     *         the object id
     */
    private static void setBindFromForRadarChartAddValueButton( ButtonFormItem item, String widgetId, String objectId ) {
        item.setBindFrom( BIND_FROM_URLS.ADD_VALUE_IN_RADAR_CHART.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                .replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
    }

    /**
     * Sets bind to for mix chart chart options.
     *
     * @param item
     *         the item
     * @param widgetId
     *         the widget id
     * @param objectId
     *         the object id
     */
    public static void setBindToForMixChartChartOptions( SelectFormItem item, String widgetId, String objectId ) {
        item.setBindFrom( BIND_FROM_URLS.GET_CURVE_OPTIONS_IN_MIX_CHART.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                .replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
        item.setOptions(
                Arrays.stream( MULTI_CHART_PLOT_TYPE.values() ).map( option -> new SelectOptionsUI( option.getId(), option.getName() ) )
                        .toList() );
    }

    /**
     * Sets line widget styling options.
     *
     * @param item
     *         the item
     */
    private static void setLineWidgetStylingOptions( SelectFormItem item ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var style : LINE_STYLE.values() ) {
            options.add( new SelectOptionsUI( style.getId(), style.getName() ) );
        }
        item.setOptions( options );
        if ( item.getValue() == null ) {
            item.setValue( LINE_STYLE.SOLID.getId() );
        }
    }

    /**
     * Update values wrt options.
     *
     * @param item
     *         the item
     * @param columnList
     *         the column list
     */
    private static void updateValuesWRTOptions( UIFormItem item, List< SelectOptionsUI > columnList ) {
        if ( item.getValue() instanceof String strValue && columnList.stream().noneMatch( option -> option.getId().equals( strValue ) ) ) {
            item.setValue( null );
        } else if ( item.getValue() instanceof List< ? > listValue ) {
            listValue.removeIf( val -> columnList.stream().noneMatch( option -> option.getId().equals( val ) ) );
        }
    }

    /**
     * Gets form items list for widget.
     *
     * @param widget
     *         the widget
     *
     * @return the form items list for widget
     */
    public static List< UIFormItem > getFormItemsListForWidgetOptions( DashboardWidgetDTO widget ) {
        List< UIFormItem > items;
        if ( WIDGET_TYPE.getById( widget.getType() ).equals( WIDGET_TYPE.TEXT ) ) {
            items = GUIUtils.getFormItemsByFields( widget.getOptions(), TEXT_WIDGET_FIELDS.INPUT_METHOD, WIDGET_FIELDS.TITLE,
                    WIDGET_FIELDS.UPDATE_WIDGET_INTERVAL );
        } else if ( WIDGET_TYPE.getById( widget.getType() ).equals( WIDGET_TYPE.RADAR ) ) {
            items = GUIUtils.getFormItemsByExcludingFields( widget.getOptions(), RADAR_WIDGET_FIELDS.VALUE, WIDGET_FIELDS.STYLING,
                    WIDGET_FIELDS.POINT_SYMBOL, WIDGET_FIELDS.UPDATE_WIDGET_INTERVAL );
        } else if ( WIDGET_TYPE.getById( widget.getType() ).equals( WIDGET_TYPE.TREEMAP ) ) {
            items = GUIUtils.getFormItemsByFields( widget.getOptions(), WIDGET_FIELDS.TITLE,
                    DataDashboardConstants.TREEMAP_WIDGET_FIELDS.LEAF_DEPTH, WIDGET_FIELDS.UPDATE_WIDGET_INTERVAL );
        } else {
            items = GUIUtils.prepareForm( true, widget.getOptions() );
        }
        return items;
    }

    /**
     * Gets data source id from widget.
     *
     * @param widget
     *         the widget
     *
     * @return the data source id from widget
     */
    public static String getDataSourceIdFromWidget( DashboardWidgetDTO widget ) {
        return switch ( WIDGET_SOURCE_TYPE.getById( widget.getSource().getSourceType() ) ) {
            case PYTHON -> ( ( WidgetPythonSource ) widget.getSource() ).getDataSource();
            case QUERY_BUILDER -> ( ( WidgetQueryBuilderSource ) widget.getSource() ).getDataSource();
            case OTHER -> switch ( OTHER_WIDGET_SOURCE.getById( ( ( WidgetOtherSource ) widget.getSource() ).getName() ) ) {
                case METAL_BASE -> ( ( WidgetMetalBaseSource ) widget.getSource() ).getMatDbSource();
                case MATERIAL_INSPECTION -> ( ( WidgetMaterialInspectorSource ) widget.getSource() ).getMatDbSource();
                case PST -> null;
                case VMCL -> ( ( WidgetVmclSource ) widget.getSource() ).getVmclDataSource();
            };
        };
    }

    /**
     * Gets form items for curve in mix chart.
     *
     * @param option
     *         the option
     *
     * @return the form items for curve in mix chart
     */
    public static List< UIFormItem > getFormItemsForCurveInMixChart( String option ) {
        return switch ( MULTI_CHART_PLOT_TYPE.getById( option ) ) {
            case BAR -> GUIUtils.getFormItemsByFields( BarMixChartCurveOptions.class, WIDGET_FIELDS.X_AXIS, WIDGET_FIELDS.Y_AXIS );
            case SCATTER -> GUIUtils.getFormItemsByFields( ScatterMixChartCurveOptions.class, WIDGET_FIELDS.X_AXIS, WIDGET_FIELDS.Y_AXIS,
                    WIDGET_FIELDS.POINT_SYMBOL );
            case LINE -> GUIUtils.getFormItemsByFields( LineMixChartCurveOptions.class, WIDGET_FIELDS.X_AXIS, WIDGET_FIELDS.Y_AXIS,
                    WIDGET_FIELDS.POINT_SYMBOL, LINE_WIDGET_FIELDS.SMOOTH, WIDGET_FIELDS.STYLING );
        };
    }

    /**
     * Run query for radar widget dynamic query response.
     *
     * @param dataSourceDTO
     *         the data source dto
     * @param dto
     *         the dto
     *
     * @return the dynamic query response
     */
    public static DynamicQueryResponse runQueryForRadarWidget( DataSourceDTO dataSourceDTO, DashboardWidgetDTO dto ) {
        String query = DashboardQueryBuilder.getQueryForRadarWidget( dto );
        JdbcTemplate jdbcTemplate = DataDashboardUtil.getJdbcTemplate( ( DatabaseDataSourceDTO ) dataSourceDTO );
        try {
            return fetchDataFromQuery( jdbcTemplate, query, dto );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    public static List< Map< String, Object > > convertToListOfMaps( DynamicQueryData queryData ) {
        List< Map< String, Object > > result = new ArrayList<>();

        // Get the maximum number of rows based on any list size
        int maxSize = queryData.values().stream().mapToInt( List::size ).max().orElse( 0 );

        for ( int i = 0; i < maxSize; i++ ) {
            Map< String, Object > row = new HashMap<>();
            for ( String column : queryData.keySet() ) {
                List< Object > values = queryData.get( column );
                // Add value only if exists at this index, else null
                row.put( column, i < values.size() ? values.get( i ) : null );
            }
            result.add( row );
        }

        return result;
    }

}