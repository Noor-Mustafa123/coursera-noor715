package de.soco.software.simuspace.suscore.object.utility;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.BIND_FROM_URLS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.BIND_TO_URLS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.DATA_SOURCE_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.METAL_BASE_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.URL_PARAMS;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.DashboardDataSourceOptions;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.METAL_BASE_ACTION;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WidgetPythonOutputOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants;
import de.soco.software.simuspace.suscore.common.enums.ExitCodesAndSignals;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.ButtonFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.BindTo;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;
import de.soco.software.simuspace.suscore.data.model.DataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.DatabaseDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.WidgetMetalBaseSource;

/**
 * The type Dashboard metal base util.
 */
@Log4j2
public class DashboardMetalBaseUtil {

    /**
     * The type Dependency fields.
     */
    @Getter
    @Setter
    @AllArgsConstructor
    private static class DependencyFields {

        /**
         * The Gs material name.
         */
        private String gsMaterialName;

        /**
         * The Angle.
         */
        private String angle;

        /**
         * The Surface finish values.
         */
        private String surfaceFinishValues;

        /**
         * The Supplier name values.
         */
        private String supplierNameValues;

        /**
         * The Test created after.
         */
        private Timestamp testCreatedAfter;

        /**
         * The Test created before.
         */
        private Timestamp testCreatedBefore;

        /**
         * The Min thickness.
         */
        private String minThickness;

        /**
         * The Max thickness.
         */
        private String maxThickness;

        /**
         * The Status.
         */
        private Boolean status;

        /**
         * The Flow curve.
         */
        private Boolean flowCurve;

        /**
         * The Bulge.
         */
        private Boolean bulge;

        /**
         * The Show only in review.
         */
        private Boolean showOnlyInReview;

    }

    /**
     * The constant INPUT_FILE_NAME.
     */
    private static final String INPUT_FILE_NAME = "python_input.json";

    /**
     * The constant CSV_OUTPUT_FILE.
     */
    private static final String OUTPUT_FILE = "output";

    /**
     * Sets bind from for fetch order number.
     *
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     */
    public static void setBindFromForFetchOrderNumber( ButtonFormItem item, String objectId, String widgetId ) {
        item.setBindFrom(
                BIND_FROM_URLS.METAL_BASE.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() ) );

    }

    /**
     * Sets options for characteristics.
     *
     * @param item
     *         the item
     */
    public static void setOptionsForCharacteristics( SelectFormItem item ) {
        List< String > axesForRadarChart = List.of( "A80 [%]", "Ag [%]", "Rm [Mpa]", "HET [%]", "FLC(PS) [-]", "n [-]", "R [-]",
                "Re [Mpa]" );
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var axisForRadarChart : axesForRadarChart ) {
            options.add( new SelectOptionsUI( axisForRadarChart, axisForRadarChart ) );
        }
        item.setOptions( options );
        if ( item.getValue() == null ) {
            //all values selected as default value
            item.setValue( axesForRadarChart );
        }
    }

    /**
     * Perform material inspection action object.
     *
     * @param objectId
     *         the object id
     * @param action
     *         the action
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param data
     *
     * @return the object
     */
    public static Object performMetalBaseAction( String objectId, METAL_BASE_ACTION action, DashboardWidgetDTO widget,
            DatabaseDataSourceDTO selectedDataSource, String data, UserDTO dashboardUser ) {
        callPython( widget, selectedDataSource, objectId, action.name(), data, dashboardUser );
        return switch ( action ) {
            case review_status, plotData -> null;
            case export_data, export_graphics -> getExportDifferencesLink( widget, objectId, action.name() );
        };
    }

    private static Map< String, String > getExportDifferencesLink( DashboardWidgetDTO widget, String objectId, String action ) {
        checkIfOutputFileExists( widget, objectId, action );
        Map< String, String > response = new HashMap<>();
        String downloadBase = "/data/dashboard/" + objectId + "/widget/" + widget.getId() + "/download/" + action;
        response.put( "url", downloadBase );
        return response;
    }

    /**
     * Sets options for cp us.
     *
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     */
    public static void setOptionsAndBindFromForAngles( SelectFormItem item, String objectId, String widgetId ) {
        List< String > angles = List.of( "0", "45", "90" );
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var angle : angles ) {
            options.add( new SelectOptionsUI( angle, angle ) );
        }
        item.setOptions( options );
        item.setBindFrom(
                BIND_FROM_URLS.METAL_BASE.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() ) );

    }

    /**
     * Gets surface finish related fields.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldValue
     *         the field value
     * @param widgetSource
     *         the widget source
     * @param dataSource
     *         the data source
     * @param fieldName
     *         the field name
     *
     * @return the surface finish related fields
     */
    public static UIForm getSurfaceFinishRelatedFields( String objectId, String widgetId, String fieldValue,
            WidgetMetalBaseSource widgetSource, DatabaseDataSourceDTO dataSource, String fieldName ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetMetalBaseSource.class, METAL_BASE_FIELDS.SUPPLIER_NAME );
        for ( var item : items ) {
            if ( item instanceof SelectFormItem selectFormItem && item.getName().equals( METAL_BASE_FIELDS.SUPPLIER_NAME ) ) {
                selectFormItem.setOptions( getSelectOptionsForSupplierName( dataSource, widgetSource, fieldValue, fieldName ) );
                selectFormItem.setMultiple( true );
            }
        }
        return GUIUtils.createFormFromItems( items );
    }

    /**
     * Gets material name related fields.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldValue
     *         the field value
     * @param widgetSource
     *         the widget source
     * @param dataSource
     *         the data source
     * @param fieldName
     *         the field name
     *
     * @return the material name related fields
     */
    public static UIForm getMaterialNameRelatedFields( String objectId, String widgetId, String fieldValue,
            WidgetMetalBaseSource widgetSource, DatabaseDataSourceDTO dataSource, String fieldName ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetMetalBaseSource.class, METAL_BASE_FIELDS.SUPPLIER_NAME,
                METAL_BASE_FIELDS.SURFACE_FINISH );
        for ( var item : items ) {
            if ( item instanceof SelectFormItem selectFormItem ) {
                if ( item.getName().equals( METAL_BASE_FIELDS.SUPPLIER_NAME ) ) {
                    selectFormItem.setOptions( getSelectOptionsForSupplierName( dataSource, widgetSource, fieldValue, fieldName ) );
                } else if ( item.getName().equals( METAL_BASE_FIELDS.SURFACE_FINISH ) ) {
                    selectFormItem.setOptions( getSelectOptionsForSurfaceFinish( dataSource, widgetSource, fieldValue, fieldName ) );
                    selectFormItem.setBindFrom( BIND_FROM_URLS.METAL_BASE.replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() )
                            .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
                }
                selectFormItem.setMultiple( true );
            }
        }
        return GUIUtils.createFormFromItems( items );
    }

    /**
     * Gets order nr field.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldValue
     *         the field value
     * @param widgetSource
     *         the widget source
     * @param dataSource
     *         the data source
     * @param fieldName
     *         the field name
     *
     * @return the order nr field
     */
    public static UIForm getOrderNrField( String objectId, String widgetId, String fieldValue, WidgetMetalBaseSource widgetSource,
            DatabaseDataSourceDTO dataSource, String fieldName ) {
        var items = GUIUtils.getFormItemsByFields( WidgetMetalBaseSource.class, METAL_BASE_FIELDS.ORDER_NR,
                METAL_BASE_FIELDS.ALL_TEST_IDS );

        SelectFormItem orderNr = null;
        UIFormItem allTestIds = null;
        // One simple loop to find both items
        for ( var item : items ) {
            if ( item.getName().equals( METAL_BASE_FIELDS.ORDER_NR ) ) {
                orderNr = ( SelectFormItem ) item;
            } else if ( item.getName().equals( METAL_BASE_FIELDS.ALL_TEST_IDS ) ) {
                allTestIds = item;
            }
        }
        if ( orderNr != null ) {
            orderNr.setBindFrom( BIND_FROM_URLS.METAL_BASE.replace( URL_PARAMS.PARAM_FIELD_NAME, orderNr.getName() )
                    .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
            List< SelectOptionsUI > options = getSelectOptionsForOrderNumber( dataSource, widgetSource, fieldValue, fieldName );
            orderNr.setOptions( options );
            if ( allTestIds != null ) {
                allTestIds.setValue( options.stream().map( SelectOptionsUI::getId ).toList() );
            }
        }
        return GUIUtils.createFormFromItems( items );
    }

    /**
     * Sets default value for quantile limit.
     *
     * @param item
     *         the item
     */
    public static void setDefaultValueForQuantileLimit( UIFormItem item ) {
        if ( item.getValue() == null ) {
            item.setValue( "0.025" );
        }
    }

    /**
     * Gets columns from output file.
     *
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     *
     * @return the columns from output file
     */
    public static Map< String, Object > getResultFromOutputFile( DashboardWidgetDTO widget, String objectId, METAL_BASE_ACTION action ) {
        try {
            String idInPath = widget.getGroupId() != null ? widget.getGroupId() : widget.getId();
            if ( !checkIfOutputFileExists( widget, objectId, action.name() ) ) {
                return new HashMap<>();
            }
            var outputFilePath = prepareOutputFilePath( UUID.fromString( idInPath ),
                    new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), action.name() );
            return DashboardJsonUtil.getFileContent( outputFilePath );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_WHILE_READING_OUTPUT_FILE.getKey(),
                    WidgetPythonOutputOptions.CSV.getName() ) );
        }
    }

    /**
     * Check if output file exists boolean.
     *
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     *
     * @return the boolean
     */
    public static boolean checkIfOutputFileExists( DashboardWidgetDTO widget, String objectId, String action ) {
        String idInPath = widget.getGroupId() != null ? widget.getGroupId() : widget.getId();
        var outputFilePath = prepareOutputFilePath( UUID.fromString( idInPath ),
                new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), action );
        return Files.exists( outputFilePath );
    }

    /**
     * Populate data source list.
     *
     * @param item
     *         the item
     * @param sources
     *         the sources
     */
    public static void populateDataSourceList( SelectFormItem item, List< DataSourceDTO > sources ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var source : sources ) {
            if ( DashboardDataSourceOptions.DATABASE.getId().equals( source.getSourceType() ) ) {
                options.add( new SelectOptionsUI( source.getId(),
                        source.getSourceName() != null ? source.getSourceName() : "Unnamed data source" ) );
            }
        }
        item.setOptions( options );
    }

    /**
     * Sets bind to for mat db schema.
     *
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     */
    public static void setBindToForMatDbSchema( SelectFormItem item, String objectId, String widgetId ) {
        BindTo bindTo = new BindTo();
        bindTo.setUrl( BIND_TO_URLS.METAL_BASE.replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() )
                .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
        bindTo.setName( METAL_BASE_FIELDS.MAT_DB_SOURCE );
        item.setBindTo( bindTo );
    }

    /**
     * Sets bind to for gs name.
     *
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     */
    public static void setBindToAndBindFromForGsName( SelectFormItem item, String objectId, String widgetId ) {
        BindTo bindTo = new BindTo();
        bindTo.setUrl( BIND_TO_URLS.METAL_BASE.replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() )
                .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
        bindTo.setName( METAL_BASE_FIELDS.MAT_DB_SCHEMA );
        item.setBindTo( bindTo );
        item.setBindFrom( BIND_FROM_URLS.METAL_BASE.replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() )
                .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
    }

    /**
     * Gets select options for schema.
     *
     * @param dataSource
     *         the data source
     *
     * @return the select options for schema
     */
    public static List< SelectOptionsUI > getSelectOptionsForSchema( DatabaseDataSourceDTO dataSource ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            List< SelectOptionsUI > schemaList = new ArrayList<>();
            DataDashboardUtil.addSchemaOptionsToList( dataSource.getId(), metaData, schemaList );
            return schemaList;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_SCHEMAS_FROM_DATABASE.getKey() ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets select options for material name.
     *
     * @param dataSource
     *         the data source
     * @param fieldValue
     *         the field value
     *
     * @return the select options for material name
     */
    public static List< SelectOptionsUI > getSelectOptionsForMaterialName( DatabaseDataSourceDTO dataSource, String fieldValue ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            var split = fieldValue.split( "\\." );
            String sql = String.format( "SELECT DISTINCT mb.gs_material_name, mb.id FROM %s.md_base mb", split[ 1 ] );
            return jdbcTemplate.query( sql, ( rs, rowNum ) -> {
                SelectOptionsUI option = new SelectOptionsUI();
                option.setId( rs.getString( "id" ) );
                option.setName( rs.getString( "gs_material_name" ) );
                return option;
            } );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(), METAL_BASE_FIELDS.GS_MATERIAL_NAME ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets select options for order number.
     *
     * @param dataSource
     *         the data source
     * @param widgetSource
     *         the widget source
     * @param fieldValue
     *         the field value
     * @param fieldName
     *         the field name
     *
     * @return the select options for order number
     */
    public static List< SelectOptionsUI > getSelectOptionsForOrderNumber( DatabaseDataSourceDTO dataSource,
            WidgetMetalBaseSource widgetSource, String fieldValue, String fieldName ) {
        JdbcTemplate jdbcTemplate = null;
        try {

            DependencyFields dependencyFields = getGetValuesForDependencyFields( widgetSource, fieldValue, fieldName, true );
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            String sql = getSqlForOrderNumber( widgetSource, dependencyFields );
            log.debug( sql );
            return jdbcTemplate.query( sql, ( rs, rowNum ) -> {
                SelectOptionsUI option = new SelectOptionsUI();
                option.setId( rs.getString( "id" ) );
                option.setName( rs.getString( "order_nr" ) );
                return option;
            } );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(), METAL_BASE_FIELDS.ORDER_NR ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets get dependency fields.
     *
     * @param widgetSource
     *         the widget source
     * @param fieldValue
     *         the field value
     * @param fieldName
     *         the field name
     * @param bindFrom
     *         the bind from
     *
     * @return the get dependency fields
     */
    private static DependencyFields getGetValuesForDependencyFields( WidgetMetalBaseSource widgetSource, String fieldValue,
            String fieldName, boolean bindFrom ) {
        if ( bindFrom ) {
            return getDependencyFieldsForBindFrom( widgetSource, fieldValue, fieldName );
        } else {
            return getDependencyFieldsForBindTo( widgetSource, fieldValue, fieldName );
        }
    }

    /**
     * Gets dependency fields for bind from.
     *
     * @param widgetSource
     *         the widget source
     * @param fieldValue
     *         the field value
     * @param fieldName
     *         the field name
     *
     * @return the dependency fields for bind from
     */
    private static DependencyFields getDependencyFieldsForBindFrom( WidgetMetalBaseSource widgetSource, String fieldValue,
            String fieldName ) {
        String gsMaterialName;
        String surfaceFinishValues = null;
        String supplierNameValues = null;
        Timestamp testCreatedBefore = null;
        Timestamp testCreatedAfter = null;
        String angle = null;
        String minThickness = null;
        String maxThickness = null;
        Boolean status = null;
        Boolean flowCurve = null;
        Boolean bulge = null;
        Boolean showOnlyInReview = null;
        DependencyFields dependencyFields;
        log.info( "fetching order number for {}", fieldName );
        switch ( fieldName ) {
            case METAL_BASE_FIELDS.GS_MATERIAL_NAME -> {
                gsMaterialName = fieldValue;
                dependencyFields = new DependencyFields( gsMaterialName, angle, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                        testCreatedBefore, minThickness, maxThickness, status, flowCurve, bulge, showOnlyInReview );
            }
            case METAL_BASE_FIELDS.SURFACE_FINISH -> {
                gsMaterialName = widgetSource.getGs_material_name();
                surfaceFinishValues = getDependencyValueFromListInDTO( widgetSource.getSurface_finish(), surfaceFinishValues );
                dependencyFields = new DependencyFields( gsMaterialName, angle, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                        testCreatedBefore, minThickness, maxThickness, status, flowCurve, bulge, showOnlyInReview );
            }
            case METAL_BASE_FIELDS.ANGLE -> {
                dependencyFields = getCommonDependencyFields( widgetSource, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                        testCreatedBefore );
                dependencyFields.setAngle( fieldValue );
            }
            case METAL_BASE_FIELDS.TEST_CREATED_BETWEEN -> {
                dependencyFields = getCommonDependencyFields( widgetSource, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                        testCreatedBefore );
                if ( fieldValue != null && fieldValue.contains( ConstantsString.HYPHEN ) ) {
                    var split = fieldValue.split( ConstantsString.HYPHEN );
                    dependencyFields.setTestCreatedAfter( new Timestamp( Long.parseLong( split[ 0 ] ) ) );
                    dependencyFields.setTestCreatedBefore( new Timestamp( Long.parseLong( split[ 1 ] ) ) );
                }

            }
            case METAL_BASE_FIELDS.MIN_THICKNESS -> {
                dependencyFields = getCommonDependencyFields( widgetSource, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                        testCreatedBefore );
                dependencyFields.setMinThickness( fieldValue );
            }
            case METAL_BASE_FIELDS.MAX_THICKNESS -> {
                dependencyFields = getCommonDependencyFields( widgetSource, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                        testCreatedBefore );
                dependencyFields.setMaxThickness( fieldValue );
            }
            case METAL_BASE_FIELDS.IO_STATUS -> {
                dependencyFields = getCommonDependencyFields( widgetSource, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                        testCreatedBefore );
                status = Boolean.TRUE == Boolean.parseBoolean( fieldValue );
                dependencyFields.setStatus( status );
            }
            case METAL_BASE_FIELDS.BULGE -> {
                dependencyFields = getCommonDependencyFields( widgetSource, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                        testCreatedBefore );
                bulge = Boolean.TRUE == Boolean.parseBoolean( fieldValue );
                dependencyFields.setBulge( bulge );
            }
            case METAL_BASE_FIELDS.FLOW_CURVE -> {
                dependencyFields = getCommonDependencyFields( widgetSource, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                        testCreatedBefore );
                flowCurve = Boolean.TRUE == Boolean.parseBoolean( fieldValue );
                dependencyFields.setFlowCurve( flowCurve );
            }
            case METAL_BASE_FIELDS.SHOW_ONLY_IN_REVIEW -> {
                dependencyFields = getCommonDependencyFields( widgetSource, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                        testCreatedBefore );
                showOnlyInReview = Boolean.TRUE == Boolean.parseBoolean( fieldValue );
                dependencyFields.setShowOnlyInReview( showOnlyInReview );
            }
            default -> throw new IllegalStateException( "Unexpected value: " + fieldName );
        }

        return dependencyFields;
    }

    /**
     * Gets dependency value from list in dto.
     *
     * @param widgetSource
     *         the widget source
     * @param valueToGet
     *         the value to get
     *
     * @return the dependency value from list in dto
     */
    private static String getDependencyValueFromListInDTO( List< String > widgetSource, String valueToGet ) {
        if ( CollectionUtils.isNotEmpty( widgetSource ) ) {
            if ( widgetSource.size() > 1 ) {
                valueToGet = widgetSource.stream().map( selectedOption -> "'" + selectedOption + "'" ).collect( Collectors.joining( "," ) );
            } else {
                valueToGet = "'" + widgetSource.get( 0 ) + "'";
            }
        }
        return valueToGet;
    }

    /**
     * Gets common dependency fields.
     *
     * @param widgetSource
     *         the widget source
     * @param surfaceFinishValues
     *         the surface finish values
     * @param supplierNameValues
     *         the supplier name values
     * @param testCreatedAfter
     *         the test created after
     * @param testCreatedBefore
     *         the test created before
     *
     * @return the common dependency fields
     */
    private static DependencyFields getCommonDependencyFields( WidgetMetalBaseSource widgetSource, String surfaceFinishValues,
            String supplierNameValues, Timestamp testCreatedAfter, Timestamp testCreatedBefore ) {
        DependencyFields dependencyFields;
        boolean status;
        boolean flowCurve;
        String gsMaterialName;
        String maxThickness;
        String angle;
        boolean showOnlyInReview;
        boolean bulge;
        String minThickness;
        gsMaterialName = widgetSource.getGs_material_name();
        surfaceFinishValues = getDependencyValueFromListInDTO( widgetSource.getSurface_finish(), surfaceFinishValues );
        supplierNameValues = getDependencyValueFromListInDTO( widgetSource.getSupplier_name(), supplierNameValues );
        minThickness = widgetSource.getMinThickness() != null ? widgetSource.getMinThickness() : "0";
        maxThickness = widgetSource.getMaxThickness() != null ? widgetSource.getMaxThickness() : String.valueOf( Integer.MAX_VALUE );
        status = Boolean.TRUE == Boolean.parseBoolean( widgetSource.getIoStatus() );
        bulge = Boolean.TRUE == Boolean.parseBoolean( widgetSource.getBulge() );
        flowCurve = Boolean.TRUE == Boolean.parseBoolean( widgetSource.getFlowCurves() );
        showOnlyInReview = Boolean.TRUE == Boolean.parseBoolean( widgetSource.getShowOnlyInReview() );
        if ( widgetSource.getTestCreatedBetween() != null && widgetSource.getTestCreatedBetween().getFrom() != null
                && widgetSource.getTestCreatedBetween().getTo() != null ) {
            testCreatedAfter = new Timestamp( widgetSource.getTestCreatedBetween().getFrom() );
            testCreatedBefore = new Timestamp( widgetSource.getTestCreatedBetween().getTo() );
        }
        angle = widgetSource.getAngle();
        dependencyFields = new DependencyFields( gsMaterialName, angle, surfaceFinishValues, supplierNameValues, testCreatedAfter,
                testCreatedBefore, minThickness, maxThickness, status, flowCurve, bulge, showOnlyInReview );
        return dependencyFields;
    }

    /**
     * Gets dependency fields for bind to.
     *
     * @param widgetSource
     *         the widget source
     * @param fieldValue
     *         the field value
     * @param fieldName
     *         the field name
     *
     * @return the dependency fields for bind to
     */
    private static DependencyFields getDependencyFieldsForBindTo( WidgetMetalBaseSource widgetSource, String fieldValue,
            String fieldName ) {
        String gsMaterialName = null;
        String surfaceFinishValues = null;
        String supplierNameValues = null;
        Timestamp testCreatedBetween = null;
        String angle = null;
        String minThickness = null;
        String maxThickness = null;
        Boolean status = null;
        Boolean flowCurve = null;
        Boolean bulge = null;
        Boolean showOnlyInReview = null;
        log.info( "fetching order number for {}", fieldName );
        switch ( fieldName ) {
            case METAL_BASE_FIELDS.SUPPLIER_NAME -> gsMaterialName = fieldValue;
        }
        return new DependencyFields( gsMaterialName, angle, surfaceFinishValues, supplierNameValues, testCreatedBetween, testCreatedBetween,
                minThickness, maxThickness, status, flowCurve, bulge, showOnlyInReview );
    }

    /**
     * Gets sql for order number.
     *
     * @param widgetSource
     *         the widget source
     * @param dependencyFields
     *         the dependency fields
     *
     * @return the sql for order number
     */
    private static String getSqlForOrderNumber( WidgetMetalBaseSource widgetSource, DependencyFields dependencyFields ) {
        var split = widgetSource.getMatDbSchema().split( "\\." );
        StringBuilder sql = new StringBuilder( String.format( """
                select distinct mt.id,  mt.order_nr from  %s.md_base mb
                join %s.md_specs ms on mb.id = ms.base_id
                join %s.md_test mt on ms.id = mt.specs_id
                join %s.md_flow_curve mf on mf.test_id = mt.id
                join %s.md_attributes ma on ma.test_id = mt.id""", split[ 1 ], split[ 1 ], split[ 1 ], split[ 1 ], split[ 1 ] ) );
        if ( dependencyFields.getSupplierNameValues() != null ) {
            sql.append( String.format( " join %s.md_supplier msup on msup.id = ms.supplier_id ", split[ 1 ] ) );
        }
        sql.append( String.format( " where mb.id = '%s' and ms.thickness  BETWEEN %s AND %s ", dependencyFields.getGsMaterialName(),
                dependencyFields.getMinThickness(), dependencyFields.getMaxThickness() ) );
        if ( dependencyFields.getSurfaceFinishValues() != null ) {
            sql.append( String.format( " and ms.surface_finish in (%s) ", dependencyFields.getSurfaceFinishValues() ) );
        }
        if ( dependencyFields.getSupplierNameValues() != null ) {
            sql.append( String.format( " and msup.id in (%s) ", dependencyFields.getSupplierNameValues() ) );
        }
        if ( Boolean.TRUE == dependencyFields.getBulge() ) {
            sql.append( String.format( " and mt.id in (select test_id from %s.md_bulge) ", split[ 1 ] ) );
        }
        if ( Boolean.TRUE == dependencyFields.getStatus() ) {
            sql.append( String.format( " and mt.status = '%s' ", "i.O." ) );
        }
        if ( Boolean.TRUE == dependencyFields.getFlowCurve() ) {
            sql.append( String.format( " and mf.angle = '%s' ", widgetSource.getAngle() ) );
        }
        if ( Boolean.TRUE == dependencyFields.getShowOnlyInReview() ) {
            sql.append( String.format( " and mt.review_status = '%s' ", "In Review" ) );
        }
        if ( dependencyFields.getTestCreatedAfter() != null && dependencyFields.getTestCreatedBefore() != null ) {
            sql.append( String.format( " AND mt.created_on BETWEEN '%s' AND '%s' ", dependencyFields.getTestCreatedAfter(),
                    dependencyFields.getTestCreatedBefore() ) );
        }
        sql.append( String.format( """
                AND mt.id IN (
                     -- Ensure test_id exists in md_flow_curve with selected angle
                     SELECT test_id
                     FROM %s.md_flow_curve
                     WHERE angle = %s
                 )
                 AND mt.id IN (
                     -- Ensure test_id exists in md_attributes with r NOT NULL for selected angle
                     SELECT test_id
                     FROM %s.md_attributes
                     WHERE angle = %s
                 );""", split[ 1 ], dependencyFields.getAngle(), split[ 1 ], dependencyFields.getAngle() ) );
        return sql.toString();
    }

    /**
     * Gets select options for surface finish.
     *
     * @param dataSource
     *         the data source
     * @param widgetSource
     *         the widget source
     * @param fieldValue
     *         the field value
     * @param fieldName
     *         the field name
     *
     * @return the select options for surface finish
     */
    public static List< SelectOptionsUI > getSelectOptionsForSurfaceFinish( DatabaseDataSourceDTO dataSource,
            WidgetMetalBaseSource widgetSource, String fieldValue, String fieldName ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            var split = widgetSource.getMatDbSchema().split( "\\." );
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            String sql = String.format( """
                    select ms.surface_finish from %s.md_base mb
                    join %s.md_specs ms on mb.id = ms.base_id
                    join %s.md_test mt on ms.id = mt.specs_id
                    where mb.id = '%s' and ms.surface_finish is not null;""", split[ 1 ], split[ 1 ], split[ 1 ], fieldValue );
            Set< String > uniqueSurfaceFinishValues = new HashSet<>(
                    jdbcTemplate.query( sql, ( rs, rowNum ) -> rs.getString( "surface_finish" ) ) );
            List< SelectOptionsUI > options = new ArrayList<>();
            for ( var surfaceFinish : uniqueSurfaceFinishValues ) {
                options.add( new SelectOptionsUI( surfaceFinish, surfaceFinish ) );
            }
            return options;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(), METAL_BASE_FIELDS.SURFACE_FINISH ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets select options for supplier name.
     *
     * @param dataSource
     *         the data source
     * @param widgetSource
     *         the widget source
     * @param fieldValue
     *         the field value
     * @param fieldName
     *         the field name
     *
     * @return the select options for supplier name
     */
    public static List< SelectOptionsUI > getSelectOptionsForSupplierName( DatabaseDataSourceDTO dataSource,
            WidgetMetalBaseSource widgetSource, String fieldValue, String fieldName ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            DependencyFields dependencyFields = getGetValuesForDependencyFields( widgetSource, fieldValue, fieldName, true );
            String sql = getSqlForSupplierNames( widgetSource, dependencyFields );
            return jdbcTemplate.query( sql, ( rs, rowNum ) -> {
                SelectOptionsUI option = new SelectOptionsUI();
                option.setId( rs.getString( "id" ) );
                option.setName( rs.getString( "name" ) );
                return option;
            } );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(), METAL_BASE_FIELDS.SUPPLIER_NAME ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets sql for supplier names.
     *
     * @param widgetSource
     *         the widget source
     * @param dependencyFields
     *         the dependency fields
     *
     * @return the sql for supplier names
     */
    private static String getSqlForSupplierNames( WidgetMetalBaseSource widgetSource, DependencyFields dependencyFields ) {
        var split = widgetSource.getMatDbSchema().split( "\\." );
        StringBuilder sql = new StringBuilder( String.format( """
                select DISTINCT msup.id,msup.name  from  %s.md_base mb\s
                join %s.md_specs ms on mb.id = ms.base_id\s
                join %s.md_test mt on ms.id = mt.specs_id
                join %s.md_supplier msup on msup.id = ms.supplier_id""", split[ 1 ], split[ 1 ], split[ 1 ], split[ 1 ] ) );
        sql.append( String.format( " where mb.id = '%s' ", dependencyFields.gsMaterialName ) );
        if ( dependencyFields.surfaceFinishValues != null ) {
            sql.append( String.format( " and ms.surface_finish in (%s)", dependencyFields.surfaceFinishValues ) );
        }
        sql.append( ";" );
        return sql.toString();
    }

    /**
     * Call python.
     *
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param objectId
     *         the object id
     * @param action
     *         the action
     */
    public static void callPython( DashboardWidgetDTO widget, DatabaseDataSourceDTO selectedDataSource, String objectId, String action,
            String data, UserDTO dashboardUser ) {
        String payload = preparePayloadForPythonScript( widget, selectedDataSource, action, objectId, data, dashboardUser );
        if ( log.isDebugEnabled() ) {
            log.debug( "payload for python script\n{}", payload );
        }
        VersionPrimaryKey dashboardId = new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() );
        String idInPath = widget.getGroupId() != null ? widget.getGroupId() : widget.getId();
        var inputFile = preparePythonInputFile( payload, UUID.fromString( idInPath ), dashboardId );
        var outputFile = preparePythonOutputFile( UUID.fromString( idInPath ), dashboardId, action );
        var scriptFile = DashboardConfigUtil.getMetalBaseScriptPath();
        if ( runPythonProcess( scriptFile, inputFile.toAbsolutePath().toString(), outputFile.toAbsolutePath().toString() ).getExitValue()
                != ExitCodesAndSignals.SUCCESS.getExitCode() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_OCCURRED_IN_PYTHON_PROCESS.getKey() ) );
        }
    }

    /**
     * Run python process process result.
     *
     * @param pythonScriptPath
     *         the python script path
     * @param inputPath
     *         the input path
     * @param outputPath
     *         the output path
     *
     * @return the process result
     */
    private static ProcessResult runPythonProcess( String pythonScriptPath, String inputPath, String outputPath ) {
        String command = String.format( "%s %s -i %s -o %s", DashboardConfigUtil.getMetalBasePythonPath(), pythonScriptPath, inputPath,
                outputPath );
        try {
            return LinuxUtils.runSystemCommand( command, DashboardConfigUtil.getTimeoutForPythonProcess() );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_OCCURRED_IN_PYTHON_PROCESS.getKey() ) );
        }

    }

    /**
     * Prepare python output file path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path preparePythonOutputFile( UUID widgetId, VersionPrimaryKey dashboardId, String action ) {
        try {
            Path outputPath = prepareOutputFilePath( widgetId, dashboardId, action );
            Files.deleteIfExists( outputPath );
            Files.createFile( outputPath );
            FileUtils.setGlobalAllFilePermissions( outputPath );
            return outputPath;
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_SCRIPT_FILE.getKey() ) );
        }
    }

    /**
     * Prepare output file path path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path prepareOutputFilePath( UUID widgetId, VersionPrimaryKey dashboardId, String action ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + action + ConstantsString.UNDERSCORE + OUTPUT_FILE );
    }

    /**
     * Prepare python input file path.
     *
     * @param payload
     *         the payload
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path preparePythonInputFile( String payload, UUID widgetId, VersionPrimaryKey dashboardId ) {
        try {
            Path inputPath = prepareInputFilePath( widgetId, dashboardId );
            Files.deleteIfExists( inputPath );
            Files.createFile( inputPath );
            FileUtils.writeToFile( inputPath.toAbsolutePath().toString(), payload );
            return inputPath;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_INPUT_FILE.getKey() ) );
        }
    }

    /**
     * Prepare input file path path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path prepareInputFilePath( UUID widgetId, VersionPrimaryKey dashboardId ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + INPUT_FILE_NAME );
    }

    /**
     * Prepare payload for python script string.
     *
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     * @param action
     *         the action
     * @param objectId
     *         the object id
     * @param data
     *
     * @return the string
     */
    private static String preparePayloadForPythonScript( DashboardWidgetDTO widget, DatabaseDataSourceDTO selectedDataSource, String action,
            String objectId, String data, UserDTO dashboardUser ) {
        var widgetSource = ( WidgetMetalBaseSource ) widget.getSource();
        Map< String, Object > payload = new HashMap<>();
        payload.put( DATA_SOURCE_FIELDS.SOURCE_TYPE, selectedDataSource.getSourceType() );
        payload.put( DATA_SOURCE_FIELDS.DATABASE_TYPE, selectedDataSource.getDatabaseType() );
        payload.put( DATA_SOURCE_FIELDS.HOST, selectedDataSource.getHost() );
        payload.put( DATA_SOURCE_FIELDS.SOURCE_NAME, selectedDataSource.getSourceName() );
        payload.put( DATA_SOURCE_FIELDS.PORT, selectedDataSource.getPort() );
        payload.put( DATA_SOURCE_FIELDS.DB_NAME, selectedDataSource.getDbName() );
        payload.put( DATA_SOURCE_FIELDS.USER_NAME, selectedDataSource.getUserName() );
        payload.put( DATA_SOURCE_FIELDS.PASSWORD, selectedDataSource.getPassword() );
        payload.put( METAL_BASE_FIELDS.GS_MATERIAL_NAME, widgetSource.getGs_material_name() );
        payload.put( METAL_BASE_FIELDS.MAX_THICKNESS, widgetSource.getMaxThickness() );
        payload.put( METAL_BASE_FIELDS.MIN_THICKNESS, widgetSource.getMinThickness() );
        payload.put( METAL_BASE_FIELDS.SURFACE_FINISH, widgetSource.getSurface_finish() );
        payload.put( METAL_BASE_FIELDS.SUPPLIER_NAME, widgetSource.getSupplier_name() );
        payload.put( METAL_BASE_FIELDS.IO_STATUS, widgetSource.getIoStatus() );
        payload.put( METAL_BASE_FIELDS.ORDER_NR, widgetSource.getOrder_nr() );
        payload.put( METAL_BASE_FIELDS.ANGLE, widgetSource.getAngle() );
        payload.put( METAL_BASE_FIELDS.BULGE, widgetSource.getBulge() );
        payload.put( METAL_BASE_FIELDS.FLOW_CURVE, widgetSource.getFlowCurves() );
        payload.put( METAL_BASE_FIELDS.SHOW_ONLY_IN_REVIEW, widgetSource.getShowOnlyInReview() );
        payload.put( METAL_BASE_FIELDS.QUANTILE_LIMIT, widgetSource.getQuantileLimit() );
        payload.put( METAL_BASE_FIELDS.CHARACTERISTICS, widgetSource.getCharacteristics() );
        payload.put( METAL_BASE_FIELDS.MAT_DB_SCHEMA, widgetSource.getMatDbSchema().split( "\\." )[ 1 ] );
        payload.put( METAL_BASE_FIELDS.TEST_CREATED_BETWEEN, widgetSource.getTestCreatedBetween() );
        payload.put( METAL_BASE_FIELDS.ALL_TEST_IDS, widgetSource.getAllTestIds() );
        payload.put( "dashboardUser", dashboardUser );
        payload.put( "action", action );
        var dashboardId = new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() );
        payload.put( "dashboardId", dashboardId );
        payload.put( "logDirectory", PythonUtils.getLogsDirectoryBasePath() );
        payload.put( "cacheDirectory", PythonUtils.getCentralPythonCachePath() );
        payload.put( DataDashboardConstants.PST_FIELDS.PAYLOAD,
                StringUtils.isNotEmpty( data ) ? JsonUtils.jsonToMap( data, new HashMap<>() ) : data );
        return JsonUtils.toJson( payload );
    }

    /**
     * Gets order nr related fields.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldValue
     *         the field value
     * @param widgetSource
     *         the widget source
     * @param dataSource
     *         the data source
     * @param fieldName
     *         the field name
     *
     * @return the order nr related fields
     */
    public static UIForm getOrderNrRelatedFields( String objectId, String widgetId, String fieldValue, WidgetMetalBaseSource widgetSource,
            DatabaseDataSourceDTO dataSource, String fieldName ) {
        ButtonFormItem buttonFormItem = ( ButtonFormItem ) GUIUtils.getFormItemByField( WidgetMetalBaseSource.class,
                METAL_BASE_FIELDS.PLOT_DATA );
        setBindToForButton( buttonFormItem, objectId, widgetId );
        return GUIUtils.createFormFromItems( Collections.singletonList( buttonFormItem ) );
    }

    /**
     * Sets bind to for button.
     *
     * @param buttonFormItem
     *         the button form item
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     */
    public static void setBindToForButton( ButtonFormItem buttonFormItem, String objectId, String widgetId ) {
        String bindToUrl = BIND_TO_URLS.WIDGET_ACTION.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                .replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ).replace( URL_PARAMS.PARAM_ACTION, buttonFormItem.getName() );
        var bindTo = new BindTo();
        bindTo.setUrl( bindToUrl );
        buttonFormItem.setBindTo( bindTo );
    }

    /**
     * Gets download file path.
     *
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     * @param action
     *         the action
     *
     * @return the download file path
     */
    public static Path getDownloadFilePath( DashboardWidgetDTO widget, String objectId, String action ) {
        return prepareOutputFilePath( UUID.fromString( widget.getId() ),
                new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), action );
    }

}
