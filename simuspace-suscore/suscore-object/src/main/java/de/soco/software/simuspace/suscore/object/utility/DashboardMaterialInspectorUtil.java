package de.soco.software.simuspace.suscore.object.utility;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.BIND_FROM_URLS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.BIND_TO_URLS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.DATA_SOURCE_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.MATERIAL_INSPECTOR_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.URL_PARAMS;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.DashboardDataSourceOptions;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WidgetPythonOutputOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.enums.ExitCodesAndSignals;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.ButtonFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.BindTo;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryResponse;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;
import de.soco.software.simuspace.suscore.data.model.DataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.DatabaseDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.WidgetMaterialInspectorSource;

/**
 * The type Dashboard material inspector util.
 */
@Log4j2
public class DashboardMaterialInspectorUtil {

    /**
     * Gets lab data plot data related fields.
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
     * @return the lab data plot data related fields
     */
    private static List< UIFormItem > getLabDataPlotDataRelatedFields( String objectId, String widgetId, String fieldValue,
            WidgetMaterialInspectorSource widgetSource, DatabaseDataSourceDTO dataSource, String fieldName ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetMaterialInspectorSource.class,
                MATERIAL_INSPECTOR_FIELDS.INVESTIGATED_STRAIN_STATE, MATERIAL_INSPECTOR_FIELDS.FT0, MATERIAL_INSPECTOR_FIELDS.FT45,
                MATERIAL_INSPECTOR_FIELDS.FT90, MATERIAL_INSPECTOR_FIELDS.FB );
        Map< String, Object > flowCurveFieldValues = getFlowCurveFieldValues( dataSource, widgetSource, objectId, widgetId );
        for ( var item : items ) {
            if ( flowCurveFieldValues.containsKey( item.getName() ) ) {
                item.setValue( flowCurveFieldValues.get( item.getName() ) );
                item.setReadonly( true );
            }
        }
        return items;
    }

    /**
     * Gets flow curve field values.
     *
     * @param dataSource
     *         the data source
     * @param widgetSource
     *         the widget source
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the flow curve field values
     */
    private static Map< String, Object > getFlowCurveFieldValues( DatabaseDataSourceDTO dataSource,
            WidgetMaterialInspectorSource widgetSource, String objectId, String widgetId ) {
        var outputFilePath = prepareOutputFilePath( UUID.fromString( widgetId ),
                new VersionPrimaryKey( UUID.fromString( objectId ), dataSource.getVersionId() ) );
        return DashboardJsonUtil.getFieldsFromJson( outputFilePath );
    }

    /**
     * Sets bind from for button.
     *
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     */
    public static void setBindFromForButton( ButtonFormItem item, String objectId, String widgetId ) {
        item.setBindFrom(
                BIND_TO_URLS.WIDGET_ACTION.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_ACTION, item.getName() ) );

    }

    /**
     * Gets export file from output file.
     *
     * @param objectId
     *         the object id
     * @param widget
     *         the widget
     * @param action
     *         the action
     *
     * @return the export file from output file
     */
    public static Path getExportFileFromOutputFile( String objectId, DashboardWidgetDTO widget, String action ) {
        Path outputFilePath = prepareOutputFilePath( UUID.fromString( widget.getId() ),
                new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ) );
        var content = DashboardJsonUtil.getFileContent( outputFilePath );
        var output = ( Map< String, Object > ) content.get( "output" );
        Path exportFilePath = Path.of( ( String ) output.get( "exportFilePath" ) );
        validateExportedFile( exportFilePath, action );
        return exportFilePath;
    }

    /**
     * Validate exported file.
     *
     * @param exportFilePath
     *         the export file path
     * @param action
     *         the action
     */
    private static void validateExportedFile( Path exportFilePath, String action ) {
        if ( Files.notExists( exportFilePath ) ) {
            throw new SusException( "File not found or is in a directory SIMuSPACE can not access for action " + action );
        }
        if ( !Files.isReadable( exportFilePath ) ) {
            throw new SusException( "No permission to read file for action " + action );
        }
    }

    /**
     * Perform material inspection action object.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param action
     *         the action
     * @param widget
     *         the widget
     * @param selectedDataSource
     *         the selected data source
     *
     * @return the object
     */
    public static Object performMaterialInspectionAction( String objectId, String widgetId, String action, DashboardWidgetDTO widget,
            DatabaseDataSourceDTO selectedDataSource ) {
        DashboardMaterialInspectorUtil.callPython( widget, selectedDataSource, objectId, action );
        return switch ( action ) {
            case MATERIAL_INSPECTOR_FIELDS.LAB_DATA_PLOT_DATA -> GUIUtils.createFormFromItems(
                    getLabDataPlotDataRelatedFields( objectId, widgetId, null, ( WidgetMaterialInspectorSource ) widget.getSource(),
                            selectedDataSource, MATERIAL_INSPECTOR_FIELDS.LAB_DATA_PLOT_DATA ) );
            case MATERIAL_INSPECTOR_FIELDS.EXPORT_YIELD_LOCUS, MATERIAL_INSPECTOR_FIELDS.EXPORT_MEASUREMENT_DATA ->
                    getDownloadLinkForWxportFile( widget, objectId, action );
            default -> null;
        };
    }

    /**
     * Gets download link for wxport file.
     *
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     * @param action
     *         the action
     *
     * @return the download link for wxport file
     */
    private static Map< String, String > getDownloadLinkForWxportFile( DashboardWidgetDTO widget, String objectId, String action ) {
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
     */
    public static void setOptionsForCPUs( SelectFormItem item ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( int i = 1; i <= 8; i++ ) {
            options.add( new SelectOptionsUI( String.valueOf( i ), String.valueOf( i ) ) );
        }
        item.setOptions( options );

    }

    /**
     * The type Dependency fields.
     */
    private record DependencyFields( String thicknessValues, String surfaceFinishValues, String chargeValues, String laboratoryNameValues,
                                     String supplierNameValues, String statusValues ) {

    }

    /**
     * The constant INPUT_FILE_NAME.
     */
    private static final String INPUT_FILE_NAME = "python_input.json";

    /**
     * The constant CSV_OUTPUT_FILE.
     */
    private static final String JSON_OUTPUT_FILE = "python_output.json";

    /**
     * Gets result from output file.
     *
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     *
     * @return the result from output file
     */
    public static DynamicQueryResponse getResultFromOutputFile( DashboardWidgetDTO widget, String objectId ) {
        String idInPath = widget.getGroupId() != null ? widget.getGroupId() : widget.getId();
        return DashboardJsonUtil.getDataFromJsonFileForWidget( prepareOutputFilePath( UUID.fromString( idInPath ),
                new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ) ), widget );
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
    public static List< SelectOptionsUI > getColumnsFromOutputFile( DashboardWidgetDTO widget, String objectId ) {
        try {
            String idInPath = widget.getGroupId() != null ? widget.getGroupId() : widget.getId();
            var outputFilePath = prepareOutputFilePath( UUID.fromString( idInPath ),
                    new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ) );
            return DashboardJsonUtil.getColumnsFromJson( outputFilePath );
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
    public static boolean checkIfOutputFileExists( DashboardWidgetDTO widget, String objectId ) {
        String idInPath = widget.getGroupId() != null ? widget.getGroupId() : widget.getId();
        var outputFilePath = prepareOutputFilePath( UUID.fromString( idInPath ),
                new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ) );
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
        bindTo.setUrl( BIND_TO_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() )
                .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
        bindTo.setName( MATERIAL_INSPECTOR_FIELDS.MAT_DB_SOURCE );
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
    public static void setBindToForGsName( SelectFormItem item, String objectId, String widgetId ) {
        BindTo bindTo = new BindTo();
        bindTo.setUrl( BIND_TO_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() )
                .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
        bindTo.setName( MATERIAL_INSPECTOR_FIELDS.MAT_DB_SCHEMA );
        item.setBindTo( bindTo );
    }

    /**
     * Sets bind to for thickness.
     *
     * @param item
     *         the item
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     */
    public static void setBindToForThickness( SelectFormItem item, String objectId, String widgetId ) {
        BindTo bindTo = new BindTo();
        bindTo.setUrl( BIND_TO_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() )
                .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ) );
        bindTo.setName( MATERIAL_INSPECTOR_FIELDS.GS_MATERIAL_NAME );
        item.setBindFrom( BIND_FROM_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_FIELD_NAME, item.getName() ) );
        item.setBindTo( bindTo );
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
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(),
                    MATERIAL_INSPECTOR_FIELDS.GS_MATERIAL_NAME ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets select options for thickness.
     *
     * @param dataSource
     *         the data source
     * @param widgetSource
     *         the widget source
     * @param fieldValue
     *         the field value
     *
     * @return the select options for thickness
     */
    public static List< SelectOptionsUI > getSelectOptionsForThickness( DatabaseDataSourceDTO dataSource,
            WidgetMaterialInspectorSource widgetSource, String fieldValue ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            var split = widgetSource.getMatDbSchema().split( "\\." );
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            String sql = String.format( """
                    select ms.thickness from  %s.md_base mb\s
                    join %s.md_specs ms on mb.id = ms.base_id\s
                    where mb.id = '%s'""", split[ 1 ], split[ 1 ], fieldValue );
            Set< String > uniqueThicknessValues = new HashSet<>( jdbcTemplate.query( sql, ( rs, rowNum ) -> rs.getString( "thickness" ) ) );
            List< SelectOptionsUI > options = new ArrayList<>();
            for ( var thickness : uniqueThicknessValues ) {
                options.add( new SelectOptionsUI( thickness, thickness ) );
            }
            return options;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(), MATERIAL_INSPECTOR_FIELDS.THICKNESS ) );
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
            WidgetMaterialInspectorSource widgetSource, String fieldValue, String fieldName ) {
        JdbcTemplate jdbcTemplate = null;
        try {

            DependencyFields dependencyFields = getGetValuesForDependencyFields( widgetSource, fieldValue, fieldName );
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
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(), MATERIAL_INSPECTOR_FIELDS.ORDER_NR ) );
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
     *
     * @return the get dependency fields
     */
    private static DependencyFields getGetValuesForDependencyFields( WidgetMaterialInspectorSource widgetSource, String fieldValue,
            String fieldName ) {
        String thicknessValues = null;
        String surfaceFinishValues = null;
        String chargeValues = null;
        String laboratoryNameValues = null;
        String supplierNameValues = null;
        String statusValues = null;
        log.info( "fetching order number for {}", fieldName );
        switch ( fieldName ) {
            case MATERIAL_INSPECTOR_FIELDS.THICKNESS -> thicknessValues =
                    fieldValue.contains( "," ) ? Stream.of( fieldValue.split( "," ) ).map( thickness -> "'" + thickness + "'" )
                            .collect( Collectors.joining( "," ) ) : "'" + fieldValue + "'";
            case MATERIAL_INSPECTOR_FIELDS.SURFACE_FINISH -> {
                if ( CollectionUtils.isNotEmpty( widgetSource.getThickness() ) ) {
                    if ( widgetSource.getThickness().size() > 1 ) {
                        thicknessValues = widgetSource.getThickness().stream().map( thickness -> "'" + thickness + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        thicknessValues = "'" + widgetSource.getThickness().get( 0 ) + "'";
                    }
                }
                surfaceFinishValues =
                        fieldValue.contains( "," ) ? Stream.of( fieldValue.split( "," ) ).map( thickness -> "'" + thickness + "'" )
                                .collect( Collectors.joining( "," ) ) : "'" + fieldValue + "'";
            }
            case MATERIAL_INSPECTOR_FIELDS.CHARGE -> {
                if ( CollectionUtils.isNotEmpty( widgetSource.getThickness() ) ) {
                    if ( widgetSource.getThickness().size() > 1 ) {
                        thicknessValues = widgetSource.getThickness().stream().map( thickness -> "'" + thickness + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        thicknessValues = "'" + widgetSource.getThickness().get( 0 ) + "'";
                    }
                }
                if ( CollectionUtils.isNotEmpty( widgetSource.getSurface_finish() ) ) {
                    if ( widgetSource.getSurface_finish().size() > 1 ) {
                        surfaceFinishValues = widgetSource.getSurface_finish().stream().map( surfaceFinish -> "'" + surfaceFinish + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        surfaceFinishValues = "'" + widgetSource.getSurface_finish().get( 0 ) + "'";
                    }
                }
                chargeValues = fieldValue.contains( "," ) ? Stream.of( fieldValue.split( "," ) ).map( thickness -> "'" + thickness + "'" )
                        .collect( Collectors.joining( "," ) ) : "'" + fieldValue + "'";
            }
            case MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME -> {
                if ( CollectionUtils.isNotEmpty( widgetSource.getThickness() ) ) {
                    if ( widgetSource.getThickness().size() > 1 ) {
                        thicknessValues = widgetSource.getThickness().stream().map( thickness -> "'" + thickness + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        thicknessValues = "'" + widgetSource.getThickness().get( 0 ) + "'";
                    }
                }
                if ( CollectionUtils.isNotEmpty( widgetSource.getSurface_finish() ) ) {
                    if ( widgetSource.getSurface_finish().size() > 1 ) {
                        surfaceFinishValues = widgetSource.getSurface_finish().stream().map( surfaceFinish -> "'" + surfaceFinish + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        surfaceFinishValues = "'" + widgetSource.getSurface_finish().get( 0 ) + "'";
                    }
                }
                if ( CollectionUtils.isNotEmpty( widgetSource.getCharge() ) ) {
                    if ( widgetSource.getCharge().size() > 1 ) {
                        chargeValues = widgetSource.getCharge().stream().map( charge -> "'" + charge + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        chargeValues = "'" + widgetSource.getCharge().get( 0 ) + "'";
                    }
                }
                laboratoryNameValues = fieldValue.contains( "," ) ? Stream.of( fieldValue.split( "," ) )
                        .map( laboratoryName -> "'" + laboratoryName + "'" ).collect( Collectors.joining( "," ) ) : "'" + fieldValue + "'";
            }
            case MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME -> {
                if ( CollectionUtils.isNotEmpty( widgetSource.getThickness() ) ) {
                    if ( widgetSource.getThickness().size() > 1 ) {
                        thicknessValues = widgetSource.getThickness().stream().map( thickness -> "'" + thickness + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        thicknessValues = "'" + widgetSource.getThickness().get( 0 ) + "'";
                    }
                }
                if ( CollectionUtils.isNotEmpty( widgetSource.getSurface_finish() ) ) {
                    if ( widgetSource.getSurface_finish().size() > 1 ) {
                        surfaceFinishValues = widgetSource.getSurface_finish().stream().map( surfaceFinish -> "'" + surfaceFinish + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        surfaceFinishValues = "'" + widgetSource.getSurface_finish().get( 0 ) + "'";
                    }
                }
                if ( CollectionUtils.isNotEmpty( widgetSource.getCharge() ) ) {
                    if ( widgetSource.getCharge().size() > 1 ) {
                        chargeValues = widgetSource.getCharge().stream().map( charge -> "'" + charge + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        chargeValues = "'" + widgetSource.getCharge().get( 0 ) + "'";
                    }
                }
                supplierNameValues =
                        fieldValue.contains( "," ) ? Stream.of( fieldValue.split( "," ) ).map( supplierName -> "'" + supplierName + "'" )
                                .collect( Collectors.joining( "," ) ) : "'" + fieldValue + "'";
            }
            case MATERIAL_INSPECTOR_FIELDS.STATUS -> {
                if ( CollectionUtils.isNotEmpty( widgetSource.getThickness() ) ) {
                    if ( widgetSource.getThickness().size() > 1 ) {
                        thicknessValues = widgetSource.getThickness().stream().map( thickness -> "'" + thickness + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        thicknessValues = "'" + widgetSource.getThickness().get( 0 ) + "'";
                    }
                }
                if ( CollectionUtils.isNotEmpty( widgetSource.getSurface_finish() ) ) {
                    if ( widgetSource.getSurface_finish().size() > 1 ) {
                        surfaceFinishValues = widgetSource.getSurface_finish().stream().map( surfaceFinish -> "'" + surfaceFinish + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        surfaceFinishValues = "'" + widgetSource.getSurface_finish().get( 0 ) + "'";
                    }
                }
                if ( CollectionUtils.isNotEmpty( widgetSource.getCharge() ) ) {
                    if ( widgetSource.getCharge().size() > 1 ) {
                        chargeValues = widgetSource.getCharge().stream().map( charge -> "'" + charge + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        chargeValues = "'" + widgetSource.getCharge().get( 0 ) + "'";
                    }
                }
                if ( CollectionUtils.isNotEmpty( widgetSource.getLaboratory_name() ) ) {
                    if ( widgetSource.getLaboratory_name().size() > 1 ) {
                        laboratoryNameValues = widgetSource.getLaboratory_name().stream()
                                .map( laboratoryName -> "'" + laboratoryName + "'" ).collect( Collectors.joining( "," ) );
                    } else {
                        laboratoryNameValues = "'" + widgetSource.getLaboratory_name().get( 0 ) + "'";
                    }
                }
                if ( CollectionUtils.isNotEmpty( widgetSource.getSupplier_name() ) ) {
                    if ( widgetSource.getSupplier_name().size() > 1 ) {
                        supplierNameValues = widgetSource.getSupplier_name().stream().map( supplierName -> "'" + supplierName + "'" )
                                .collect( Collectors.joining( "," ) );
                    } else {
                        supplierNameValues = "'" + widgetSource.getSupplier_name().get( 0 ) + "'";
                    }
                }
                statusValues = fieldValue.contains( "," ) ? Stream.of( fieldValue.split( "," ) ).map( status -> "'" + status + "'" )
                        .collect( Collectors.joining( "," ) ) : "'" + fieldValue + "'";
            }
        }
        return new DependencyFields( thicknessValues, surfaceFinishValues, chargeValues, laboratoryNameValues, supplierNameValues,
                statusValues );
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
    private static String getSqlForOrderNumber( WidgetMaterialInspectorSource widgetSource, DependencyFields dependencyFields ) {
        var split = widgetSource.getMatDbSchema().split( "\\." );
        StringBuilder sql = new StringBuilder( String.format( """
                select distinct mt.id,  mt.order_nr from  %s.md_base mb\s
                join %s.md_specs ms on mb.id = ms.base_id\s
                join %s.md_test mt on ms.id = mt.specs_id
                join %s.md_flow_curve mf on mf.test_id = mt.id
                join %s.md_attributes ma on ma.test_id = mt.id""", split[ 1 ], split[ 1 ], split[ 1 ], split[ 1 ], split[ 1 ] ) );
        if ( dependencyFields.laboratoryNameValues != null ) {
            sql.append( String.format( " join %s.md_laboratory mlab on mlab.id = ms.laboratory_id", split[ 1 ] ) );
        }
        if ( dependencyFields.supplierNameValues != null ) {
            sql.append( String.format( " join %s.md_supplier msup on msup.id = ms.supplier_id", split[ 1 ] ) );
        }
        sql.append( String.format( " where mb.id = '%s' and ms.thickness in (%s)", widgetSource.getGs_material_name(),
                dependencyFields.thicknessValues ) );
        if ( dependencyFields.surfaceFinishValues != null ) {
            sql.append( String.format( " and ms.surface_finish in (%s)", dependencyFields.surfaceFinishValues ) );
        }
        if ( dependencyFields.chargeValues != null ) {
            sql.append( String.format( " and ms.charge in (%s)", dependencyFields.chargeValues ) );
        }
        if ( dependencyFields.laboratoryNameValues != null ) {
            sql.append( String.format( " and mlab.id in (%s)", dependencyFields.laboratoryNameValues ) );
        }
        if ( dependencyFields.supplierNameValues != null ) {
            sql.append( String.format( " and msup.id in (%s)", dependencyFields.supplierNameValues ) );
        }
        if ( dependencyFields.statusValues != null ) {
            sql.append( String.format( " and mt.status in (%s)", dependencyFields.chargeValues ) );
        }
        sql.append( String.format( """
                AND mt.id IN (
                      -- Ensure test_id exists in md_flow_curve with angles 0, 45, and 90
                      SELECT test_id\s
                      FROM %s.md_flow_curve
                      WHERE angle IN (0, 45, 90)
                      GROUP BY test_id
                      HAVING COUNT(DISTINCT angle) = 3
                  )
                  AND mt.id IN (
                      -- Ensure test_id exists in md_attributes with r NOT NULL for angles 0, 45, 90
                      SELECT test_id
                      FROM %s.md_attributes
                      WHERE angle IN (0, 45, 90) AND r IS NOT NULL
                      GROUP BY test_id
                      HAVING COUNT(DISTINCT angle) = 3
                  );""", split[ 1 ], split[ 1 ] ) );
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
            WidgetMaterialInspectorSource widgetSource, String fieldValue, String fieldName ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            DependencyFields dependencyFields = getGetValuesForDependencyFields( widgetSource, fieldValue, fieldName );
            var split = widgetSource.getMatDbSchema().split( "\\." );
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            String sql = String.format( """
                            select ms.surface_finish from %s.md_base mb
                            join %s.md_specs ms on mb.id = ms.base_id
                            join %s.md_test mt on ms.id = mt.specs_id
                            where mb.id = '%s' and ms.thickness in (%s)  and ms.surface_finish is not null;""", split[ 1 ], split[ 1 ], split[ 1 ],
                    widgetSource.getGs_material_name(), dependencyFields.thicknessValues );
            Set< String > uniqueSurfaceFinishValues = new HashSet<>(
                    jdbcTemplate.query( sql, ( rs, rowNum ) -> rs.getString( "surface_finish" ) ) );
            List< SelectOptionsUI > options = new ArrayList<>();
            for ( var surfaceFinish : uniqueSurfaceFinishValues ) {
                options.add( new SelectOptionsUI( surfaceFinish, surfaceFinish ) );
            }
            return options;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(),
                    MATERIAL_INSPECTOR_FIELDS.SURFACE_FINISH ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets select options for charge.
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
     * @return the select options for charge
     */
    public static List< SelectOptionsUI > getSelectOptionsForCharge( DatabaseDataSourceDTO dataSource,
            WidgetMaterialInspectorSource widgetSource, String fieldValue, String fieldName ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            DependencyFields dependencyFields = getGetValuesForDependencyFields( widgetSource, fieldValue, fieldName );
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            String sql = getSqlForCharge( widgetSource, dependencyFields );
            Set< String > uniqueChargeValues = new HashSet<>( jdbcTemplate.query( sql, ( rs, rowNum ) -> rs.getString( "charge" ) ) );
            List< SelectOptionsUI > options = new ArrayList<>();
            for ( var charge : uniqueChargeValues ) {
                options.add( new SelectOptionsUI( charge, charge ) );
            }
            return options;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(), MATERIAL_INSPECTOR_FIELDS.CHARGE ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets sql for charge.
     *
     * @param widgetSource
     *         the widget source
     * @param dependencyFields
     *         the dependency fields
     *
     * @return the sql for charge
     */
    private static String getSqlForCharge( WidgetMaterialInspectorSource widgetSource, DependencyFields dependencyFields ) {
        var split = widgetSource.getMatDbSchema().split( "\\." );
        StringBuilder sql = new StringBuilder( String.format( """
                select ms.charge from %s.md_base mb
                join %s.md_specs ms on mb.id = ms.base_id
                join %s.md_test mt on ms.id = mt.specs_id""", split[ 1 ], split[ 1 ], split[ 1 ] ) );
        sql.append( String.format( " where mb.id = '%s' and ms.thickness in (%s)  and ms.charge is not null",
                widgetSource.getGs_material_name(), dependencyFields.thicknessValues ) );
        if ( dependencyFields.surfaceFinishValues != null ) {
            sql.append( String.format( " and ms.surface_finish in (%s)", dependencyFields.surfaceFinishValues ) );
        }
        sql.append( ";" );
        return sql.toString();
    }

    /**
     * Gets select options for laboratory name.
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
     * @return the select options for laboratory name
     */
    public static List< SelectOptionsUI > getSelectOptionsForLaboratoryName( DatabaseDataSourceDTO dataSource,
            WidgetMaterialInspectorSource widgetSource, String fieldValue, String fieldName ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            DependencyFields dependencyFields = getGetValuesForDependencyFields( widgetSource, fieldValue, fieldName );
            String sql = getSqlForLaboratoryNames( widgetSource, dependencyFields );
            return jdbcTemplate.query( sql, ( rs, rowNum ) -> {
                SelectOptionsUI option = new SelectOptionsUI();
                option.setId( rs.getString( "id" ) );
                option.setName( rs.getString( "name" ) );
                return option;
            } );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(),
                    MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets sql for laboratory names.
     *
     * @param widgetSource
     *         the widget source
     * @param dependencyFields
     *         the dependency fields
     *
     * @return the sql for laboratory names
     */
    private static String getSqlForLaboratoryNames( WidgetMaterialInspectorSource widgetSource, DependencyFields dependencyFields ) {
        var split = widgetSource.getMatDbSchema().split( "\\." );
        StringBuilder sql = new StringBuilder( String.format( """
                select DISTINCT mlab.id, mlab.name from  %s.md_base mb\s
                join %s.md_specs ms on mb.id = ms.base_id\s
                join %s.md_test mt on ms.id = mt.specs_id
                join %s.md_laboratory mlab on mlab.id = ms.laboratory_id""", split[ 1 ], split[ 1 ], split[ 1 ], split[ 1 ] ) );
        sql.append( String.format( " where mb.id = '%s' and ms.thickness in (%s) ", widgetSource.getGs_material_name(),
                dependencyFields.thicknessValues ) );
        if ( dependencyFields.surfaceFinishValues != null ) {
            sql.append( String.format( " and ms.surface_finish in (%s)", dependencyFields.surfaceFinishValues ) );
        }
        if ( dependencyFields.chargeValues != null ) {
            sql.append( String.format( " and ms.charge in (%s)", dependencyFields.chargeValues ) );
        }
        sql.append( ";" );
        return sql.toString();
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
            WidgetMaterialInspectorSource widgetSource, String fieldValue, String fieldName ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            DependencyFields dependencyFields = getGetValuesForDependencyFields( widgetSource, fieldValue, fieldName );
            String sql = getSqlForSupplierNames( widgetSource, dependencyFields );
            return jdbcTemplate.query( sql, ( rs, rowNum ) -> {
                SelectOptionsUI option = new SelectOptionsUI();
                option.setId( rs.getString( "id" ) );
                option.setName( rs.getString( "name" ) );
                return option;
            } );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(),
                    MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME ) );
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
    private static String getSqlForSupplierNames( WidgetMaterialInspectorSource widgetSource, DependencyFields dependencyFields ) {
        var split = widgetSource.getMatDbSchema().split( "\\." );
        StringBuilder sql = new StringBuilder( String.format( """
                select DISTINCT msup.id,msup.name  from  %s.md_base mb\s
                join %s.md_specs ms on mb.id = ms.base_id\s
                join %s.md_test mt on ms.id = mt.specs_id
                join %s.md_supplier msup on msup.id = ms.supplier_id""", split[ 1 ], split[ 1 ], split[ 1 ], split[ 1 ] ) );
        sql.append( String.format( " where mb.id = '%s' and ms.thickness in (%s)", widgetSource.getGs_material_name(),
                dependencyFields.thicknessValues ) );
        if ( dependencyFields.surfaceFinishValues != null ) {
            sql.append( String.format( " and ms.surface_finish in (%s)", dependencyFields.surfaceFinishValues ) );
        }
        if ( dependencyFields.chargeValues != null ) {
            sql.append( String.format( " and ms.charge in (%s)", dependencyFields.chargeValues ) );
        }
        sql.append( ";" );
        return sql.toString();
    }

    /**
     * Gets select options for status.
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
     * @return the select options for status
     */
    public static List< SelectOptionsUI > getSelectOptionsForStatus( DatabaseDataSourceDTO dataSource,
            WidgetMaterialInspectorSource widgetSource, String fieldValue, String fieldName ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            DependencyFields dependencyFields = getGetValuesForDependencyFields( widgetSource, fieldValue, fieldName );
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            String sql = getSqlForStatus( widgetSource, dependencyFields );
            return jdbcTemplate.query( sql, ( rs, rowNum ) -> {
                SelectOptionsUI option = new SelectOptionsUI();
                option.setId( rs.getString( "status" ) );
                option.setName( rs.getString( "status" ) );
                return option;
            } );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(), MATERIAL_INSPECTOR_FIELDS.STATUS ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    /**
     * Gets sql for status.
     *
     * @param widgetSource
     *         the widget source
     * @param dependencyFields
     *         the dependency fields
     *
     * @return the sql for status
     */
    private static String getSqlForStatus( WidgetMaterialInspectorSource widgetSource, DependencyFields dependencyFields ) {
        var split = widgetSource.getMatDbSchema().split( "\\." );
        StringBuilder sql = new StringBuilder( String.format( """
                select DISTINCT mt.status from  %s.md_base mb\s
                join %s.md_specs ms on mb.id = ms.base_id\s
                join %s.md_test mt on ms.id = mt.specs_id""", split[ 1 ], split[ 1 ], split[ 1 ] ) );
        if ( dependencyFields.laboratoryNameValues != null ) {
            sql.append( String.format( " join %s.md_laboratory mlab on mlab.id = ms.laboratory_id", split[ 1 ] ) );
        }
        if ( dependencyFields.supplierNameValues != null ) {
            sql.append( String.format( " join %s.md_supplier msup on msup.id = ms.supplier_id", split[ 1 ] ) );
        }
        sql.append(
                String.format( " where mb.id = '%s' and ms.thickness in (%s) and mt.status is not null", widgetSource.getGs_material_name(),
                        dependencyFields.thicknessValues ) );
        if ( dependencyFields.surfaceFinishValues != null ) {
            sql.append( String.format( " and ms.surface_finish in (%s)", dependencyFields.surfaceFinishValues ) );
        }
        if ( dependencyFields.chargeValues != null ) {
            sql.append( String.format( " and ms.charge in (%s)", dependencyFields.chargeValues ) );
        }
        if ( dependencyFields.laboratoryNameValues != null ) {
            sql.append( String.format( " and mlab.id in (%s)", dependencyFields.laboratoryNameValues ) );
        }
        if ( dependencyFields.supplierNameValues != null ) {
            sql.append( String.format( " and msup.id in (%s)", dependencyFields.supplierNameValues ) );
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
    public static void callPython( DashboardWidgetDTO widget, DatabaseDataSourceDTO selectedDataSource, String objectId, String action ) {
        String payload = preparePayloadForPythonScript( widget, selectedDataSource, action, objectId );
        VersionPrimaryKey dashboardId = new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() );
        String idInPath = widget.getGroupId() != null ? widget.getGroupId() : widget.getId();
        var inputFile = preparePythonInputFile( payload, UUID.fromString( idInPath ), dashboardId );
        var outputFile = preparePythonOutputFile( UUID.fromString( idInPath ), dashboardId );
        var scriptFile = DashboardConfigUtil.getMaterialInspectionScriptPath();
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
        String command = String.format( "%s %s -i %s -o %s", DashboardConfigUtil.getMaterialInspectionPythonPath(), pythonScriptPath,
                inputPath, outputPath );
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
    private static Path preparePythonOutputFile( UUID widgetId, VersionPrimaryKey dashboardId ) {
        try {
            Path outputPath = prepareOutputFilePath( widgetId, dashboardId );
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
    private static Path prepareOutputFilePath( UUID widgetId, VersionPrimaryKey dashboardId ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + JSON_OUTPUT_FILE );
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
     * Prepare python log directory path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    public static Path preparePythonLogDirectory( UUID widgetId, VersionPrimaryKey dashboardId ) {
        try {
            Path logPath = prepareLogDirectoryPath( widgetId, dashboardId );
            if ( Files.exists( logPath ) ) {
                FileUtils.deleteNonEmptyDirectory( logPath );
            }
            Files.createDirectory( logPath );
            return logPath;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_INPUT_FILE.getKey() ) );
        }
    }

    /**
     * Prepare log directory path path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path prepareLogDirectoryPath( UUID widgetId, VersionPrimaryKey dashboardId ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + "logs_" + LocalDateTime.now()
                .format( DateTimeFormatter.ofPattern( DateUtils.DATE_PATTERN_FOR_TIMESTAMP ) ) );
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
     *
     * @return the string
     */
    private static String preparePayloadForPythonScript( DashboardWidgetDTO widget, DatabaseDataSourceDTO selectedDataSource, String action,
            String objectId ) {
        var widgetSource = ( WidgetMaterialInspectorSource ) widget.getSource();
        Map< String, Object > payload = new HashMap<>();
        payload.put( DATA_SOURCE_FIELDS.SOURCE_TYPE, selectedDataSource.getSourceType() );
        payload.put( DATA_SOURCE_FIELDS.DATABASE_TYPE, selectedDataSource.getDatabaseType() );
        payload.put( DATA_SOURCE_FIELDS.HOST, selectedDataSource.getHost() );
        payload.put( DATA_SOURCE_FIELDS.SOURCE_NAME, selectedDataSource.getSourceName() );
        payload.put( DATA_SOURCE_FIELDS.PORT, selectedDataSource.getPort() );
        payload.put( DATA_SOURCE_FIELDS.DB_NAME, selectedDataSource.getDbName() );
        payload.put( DATA_SOURCE_FIELDS.USER_NAME, selectedDataSource.getUserName() );
        payload.put( DATA_SOURCE_FIELDS.PASSWORD, selectedDataSource.getPassword() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.GS_MATERIAL_NAME, widgetSource.getGs_material_name() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.THICKNESS, widgetSource.getThickness() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.SURFACE_FINISH, widgetSource.getSurface_finish() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.CHARGE, widgetSource.getCharge() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME, widgetSource.getLaboratory_name() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME, widgetSource.getSupplier_name() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.STATUS, widgetSource.getStatus() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.ORDER_NR, widgetSource.getOrder_nr() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.PROBE, widgetSource.getProbe() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.INVESTIGATED_STRAIN_STATE, widgetSource.getInvestigatedStrainState() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.FT0, widgetSource.getFt0() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.FT45, widgetSource.getFt45() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.FT90, widgetSource.getFt90() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.FB, widgetSource.getFb() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.F_TAU, widgetSource.getF_tau() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.EXP, widgetSource.getExp() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.WEIGHT_S, widgetSource.getWeightS() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.WEIGHT_R, widgetSource.getWeightR() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.WEIGHT_N, widgetSource.getWeightN() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.DISCRETIZATION, widgetSource.getDiscretization() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.GENERATIONS, widgetSource.getGenerations() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.INTERVAL_N, widgetSource.getIntervalN() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.CPUS, widgetSource.getCpus() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.POP_SIZE, widgetSource.getPopSize() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.INITIAL_VALUE_FC_OPTIMIZATION, widgetSource.getInitialValueFCOptimization() );
        payload.put( MATERIAL_INSPECTOR_FIELDS.SCHEMA, widgetSource.getMatDbSchema().split( "\\." )[ 1 ] );
        payload.put( MATERIAL_INSPECTOR_FIELDS.MATERIAL_CARD_NAME, widgetSource.getMaterialCardName() );
        payload.put( "action", action );
        var dashboardId = new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() );
        var logDirPath = preparePythonLogDirectory( UUID.fromString( widget.getId() ), dashboardId );
        payload.put( "dashboardId", dashboardId );
        payload.put( "logDirectory", PythonUtils.getLogsDirectoryBasePath() );
        payload.put( "cacheDirectory", PythonUtils.getCentralPythonCachePath() );
        return JsonUtils.toJson( payload );
    }

    /**
     * Gets thickness related fields.
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
     * @return the thickness related fields
     */
    public static UIForm getThicknessRelatedFields( String objectId, String widgetId, String fieldValue,
            WidgetMaterialInspectorSource widgetSource, DatabaseDataSourceDTO dataSource, String fieldName ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetMaterialInspectorSource.class,
                MATERIAL_INSPECTOR_FIELDS.SURFACE_FINISH, MATERIAL_INSPECTOR_FIELDS.CHARGE, MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME,
                MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME, MATERIAL_INSPECTOR_FIELDS.STATUS, MATERIAL_INSPECTOR_FIELDS.ORDER_NR );
        for ( var item : items ) {
            if ( item instanceof SelectFormItem selectFormItem ) {
                switch ( item.getName() ) {
                    case MATERIAL_INSPECTOR_FIELDS.SURFACE_FINISH -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForSurfaceFinish( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.CHARGE -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForCharge( dataSource, widgetSource, fieldValue, fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForSupplierName( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForLaboratoryName( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.STATUS -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForStatus( dataSource, widgetSource, fieldValue, fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.ORDER_NR -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForOrderNumber( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                }
                selectFormItem.setBindFrom( BIND_FROM_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_FIELD_NAME, selectFormItem.getName() ) );
                if ( !selectFormItem.getName().equals( MATERIAL_INSPECTOR_FIELDS.ORDER_NR ) ) {
                    selectFormItem.setMultiple( true );
                }
            }
        }
        return GUIUtils.createFormFromItems( items );
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
            WidgetMaterialInspectorSource widgetSource, DatabaseDataSourceDTO dataSource, String fieldName ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetMaterialInspectorSource.class, MATERIAL_INSPECTOR_FIELDS.CHARGE,
                MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME, MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME, MATERIAL_INSPECTOR_FIELDS.STATUS,
                MATERIAL_INSPECTOR_FIELDS.ORDER_NR );
        for ( var item : items ) {
            if ( item instanceof SelectFormItem selectFormItem ) {
                switch ( item.getName() ) {
                    case MATERIAL_INSPECTOR_FIELDS.CHARGE -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForCharge( dataSource, widgetSource, fieldValue, fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForSupplierName( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForLaboratoryName( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.STATUS -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForStatus( dataSource, widgetSource, fieldValue, fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.ORDER_NR -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForOrderNumber( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                }
                selectFormItem.setBindFrom( BIND_FROM_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_FIELD_NAME, selectFormItem.getName() ) );
                if ( !selectFormItem.getName().equals( MATERIAL_INSPECTOR_FIELDS.ORDER_NR ) ) {
                    selectFormItem.setMultiple( true );
                }
            }
        }
        return GUIUtils.createFormFromItems( items );

    }

    /**
     * Gets charge related fields.
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
     * @return the charge related fields
     */
    public static UIForm getChargeRelatedFields( String objectId, String widgetId, String fieldValue,
            WidgetMaterialInspectorSource widgetSource, DatabaseDataSourceDTO dataSource, String fieldName ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetMaterialInspectorSource.class,
                MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME, MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME, MATERIAL_INSPECTOR_FIELDS.STATUS,
                MATERIAL_INSPECTOR_FIELDS.ORDER_NR );
        for ( var item : items ) {
            if ( item instanceof SelectFormItem selectFormItem ) {
                switch ( item.getName() ) {
                    case MATERIAL_INSPECTOR_FIELDS.SUPPLIER_NAME -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForSupplierName( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.LABORATORY_NAME -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForLaboratoryName( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.STATUS -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForStatus( dataSource, widgetSource, fieldValue, fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.ORDER_NR -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForOrderNumber( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                }
                selectFormItem.setBindFrom( BIND_FROM_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_FIELD_NAME, selectFormItem.getName() ) );
                if ( !selectFormItem.getName().equals( MATERIAL_INSPECTOR_FIELDS.ORDER_NR ) ) {
                    selectFormItem.setMultiple( true );
                }
            }
        }
        return GUIUtils.createFormFromItems( items );

    }

    /**
     * Gets laboratory name related fields.
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
     * @return the laboratory name related fields
     */
    public static UIForm getLaboratoryNameRelatedFields( String objectId, String widgetId, String fieldValue,
            WidgetMaterialInspectorSource widgetSource, DatabaseDataSourceDTO dataSource, String fieldName ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetMaterialInspectorSource.class, MATERIAL_INSPECTOR_FIELDS.STATUS,
                MATERIAL_INSPECTOR_FIELDS.ORDER_NR );
        for ( var item : items ) {
            if ( item instanceof SelectFormItem selectFormItem ) {
                switch ( item.getName() ) {
                    case MATERIAL_INSPECTOR_FIELDS.STATUS -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForStatus( dataSource, widgetSource, fieldValue, fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.ORDER_NR -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForOrderNumber( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                }
                selectFormItem.setBindFrom( BIND_FROM_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_FIELD_NAME, selectFormItem.getName() ) );
                if ( !selectFormItem.getName().equals( MATERIAL_INSPECTOR_FIELDS.ORDER_NR ) ) {
                    selectFormItem.setMultiple( true );
                }
            }
        }
        return GUIUtils.createFormFromItems( items );
    }

    /**
     * Gets supplier name related fields.
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
     * @return the supplier name related fields
     */
    public static UIForm getSupplierNameRelatedFields( String objectId, String widgetId, String fieldValue,
            WidgetMaterialInspectorSource widgetSource, DatabaseDataSourceDTO dataSource, String fieldName ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetMaterialInspectorSource.class, MATERIAL_INSPECTOR_FIELDS.STATUS,
                MATERIAL_INSPECTOR_FIELDS.ORDER_NR );
        for ( var item : items ) {
            if ( item instanceof SelectFormItem selectFormItem ) {
                switch ( item.getName() ) {
                    case MATERIAL_INSPECTOR_FIELDS.STATUS -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForStatus( dataSource, widgetSource, fieldValue, fieldName ) );
                    case MATERIAL_INSPECTOR_FIELDS.ORDER_NR -> selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForOrderNumber( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                }
                selectFormItem.setBindFrom( BIND_FROM_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId ).replace( URL_PARAMS.PARAM_FIELD_NAME, selectFormItem.getName() ) );
                if ( !selectFormItem.getName().equals( MATERIAL_INSPECTOR_FIELDS.ORDER_NR ) ) {
                    selectFormItem.setMultiple( true );
                }
            }
        }
        return GUIUtils.createFormFromItems( items );
    }

    /**
     * Gets status related fields.
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
     * @return the status related fields
     */
    public static UIForm getStatusRelatedFields( String objectId, String widgetId, String fieldValue,
            WidgetMaterialInspectorSource widgetSource, DatabaseDataSourceDTO dataSource, String fieldName ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetMaterialInspectorSource.class, MATERIAL_INSPECTOR_FIELDS.ORDER_NR );
        for ( var item : items ) {
            if ( item instanceof SelectFormItem selectFormItem ) {
                if ( item.getName().equals( MATERIAL_INSPECTOR_FIELDS.ORDER_NR ) ) {
                    selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForOrderNumber( dataSource, widgetSource, fieldValue,
                                    fieldName ) );
                    selectFormItem.setBindFrom( BIND_FROM_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                            .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                            .replace( URL_PARAMS.PARAM_FIELD_NAME, selectFormItem.getName() ) );
                } else {
                    selectFormItem.setMultiple( true );
                }
            }
        }
        return GUIUtils.createFormFromItems( items );
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
    public static UIForm getOrderNrRelatedFields( String objectId, String widgetId, String fieldValue,
            WidgetMaterialInspectorSource widgetSource, DatabaseDataSourceDTO dataSource, String fieldName ) {
        List< UIFormItem > items = GUIUtils.getFormItemsByFields( WidgetMaterialInspectorSource.class, MATERIAL_INSPECTOR_FIELDS.PROBE,
                MATERIAL_INSPECTOR_FIELDS.LAB_DATA_PLOT_DATA );
        for ( var item : items ) {
            if ( item instanceof SelectFormItem selectFormItem ) {
                if ( item.getName().equals( MATERIAL_INSPECTOR_FIELDS.PROBE ) ) {
                    selectFormItem.setOptions(
                            DashboardMaterialInspectorUtil.getSelectOptionsForProbe( dataSource, widgetSource, fieldValue, fieldName ) );
                } else {
                    selectFormItem.setMultiple( true );
                    selectFormItem.setBindFrom( BIND_FROM_URLS.MATERIAL_INSPECTOR.replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                            .replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                            .replace( URL_PARAMS.PARAM_FIELD_NAME, selectFormItem.getName() ) );
                }
            } else if ( item instanceof ButtonFormItem buttonFormItem ) {
                if ( item.getName().equals( MATERIAL_INSPECTOR_FIELDS.LAB_DATA_PLOT_DATA ) ) {
                    setBindFromForButton( buttonFormItem, objectId, widgetId );
                }
            }
        }
        return GUIUtils.createFormFromItems( items );
    }

    /**
     * Gets select options for probe.
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
     * @return the select options for probe
     */
    private static List< SelectOptionsUI > getSelectOptionsForProbe( DatabaseDataSourceDTO dataSource,
            WidgetMaterialInspectorSource widgetSource, String fieldValue, String fieldName ) {
        JdbcTemplate jdbcTemplate = null;
        try {
            var split = widgetSource.getMatDbSchema().split( "\\." );
            jdbcTemplate = DataDashboardUtil.getJdbcTemplate( dataSource );
            String sql = String.format( """
                    select mb.probe
                    from %s.md_test mt join %s.md_bulge mb\s
                    on mt.id = mb.test_id\s
                    where mt.id = '%s'""", split[ 1 ], split[ 1 ], fieldValue );
            Set< String > uniqueProbeValues = new HashSet<>( jdbcTemplate.query( sql, ( rs, rowNum ) -> rs.getString( "probe" ) ) );
            List< SelectOptionsUI > options = new ArrayList<>();
            for ( var probe : uniqueProbeValues ) {
                options.add( new SelectOptionsUI( probe, probe ) );
            }
            return options;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_GET_VALUES_FOR.getKey(), MATERIAL_INSPECTOR_FIELDS.PROBE ) );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
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
        var bindTo = new BindTo();
        var bindToUrl = BIND_TO_URLS.WIDGET_ACTION.replace( URL_PARAMS.PARAM_OBJECT_ID, objectId )
                .replace( URL_PARAMS.PARAM_WIDGET_ID, widgetId ).replace( URL_PARAMS.PARAM_ACTION, buttonFormItem.getName() );
        bindTo.setUrl( bindToUrl );
        buttonFormItem.setBindTo( bindTo );
    }

}
