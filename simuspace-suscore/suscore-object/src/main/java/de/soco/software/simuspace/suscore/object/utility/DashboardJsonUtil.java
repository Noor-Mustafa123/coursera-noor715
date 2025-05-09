package de.soco.software.simuspace.suscore.object.utility;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFilterOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants;
import de.soco.software.simuspace.suscore.common.enums.DashboardEnums;
import de.soco.software.simuspace.suscore.common.enums.FilterType;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryData;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryMetadata;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryResponse;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.data.model.BarWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleXOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleXYOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleYOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.MixChartWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.RadarWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.ScatterWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.TableWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.TextWidgetOptions;

/**
 * The type Dashboard json util.
 */
@Log4j
public class DashboardJsonUtil {

    /**
     * Gets data from json file for widget.
     *
     * @param outputFilePath
     *         the output file path
     * @param dto
     *         the dto
     *
     * @return the data from json file for widget
     */
    public static DynamicQueryResponse getDataFromJsonFileForWidget( Path outputFilePath, DashboardWidgetDTO dto ) {
        Map< String, Object > content = getFileContent( outputFilePath );
        List< Map< String, Object > > list = ( List< Map< String, Object > > ) content.get( "data" );
        list = getFilteredList( list, dto.getColumns() );
        DynamicQueryResponse queryResponse = new DynamicQueryResponse();
        if ( null == list ) {
            return queryResponse;
        }
        DynamicQueryData data = new DynamicQueryData();
        getDataBasedOnPlotType( dto, data, list );
        queryResponse.setData( data );
        DynamicQueryMetadata metadata = getCleanedMetadataBasedOnUserSelection( content, data, dto );
        queryResponse.setMetadata( metadata );
        return queryResponse;

    }

    public static DynamicQueryResponse getPaginatedDataFromJsonFileForWidget( Path outputFilePath, DashboardWidgetDTO dto,
            FiltersDTO filters ) {
        Map< String, Object > content = getFileContent( outputFilePath );
        List< Map< String, Object > > allRecords = ( List< Map< String, Object > > ) content.get( "data" );

        filters.setTotalRecords( ( long ) allRecords.size() );

        List< Map< String, Object > > filteredRecords = getFilteredList( allRecords, dto.getColumns() );

        filters.setFilteredRecords( ( long ) filteredRecords.size() );

        int start = Math.min( filters.getStart(), filteredRecords.size() );
        int end = Math.min( start + filters.getLength(), filteredRecords.size() );
        List< Map< String, Object > > paginatedRecords = filteredRecords.subList( start, end );

        DynamicQueryResponse queryResponse = new DynamicQueryResponse();
        DynamicQueryData data = new DynamicQueryData();
        getDataBasedOnPlotType( dto, data, paginatedRecords );
        queryResponse.setData( data );

        DynamicQueryMetadata metadata = getCleanedMetadataBasedOnUserSelection( content, data, dto );
        queryResponse.setMetadata( metadata );

        return queryResponse;

    }

    /**
     * Gets cleaned metadata based on user selection.
     *
     * @param content
     *         the content
     * @param data
     *         the data
     * @param dto
     *         the dto
     *
     * @return the cleaned metadata based on user selection
     */
    private static DynamicQueryMetadata getCleanedMetadataBasedOnUserSelection( Map< String, Object > content, DynamicQueryData data,
            DashboardWidgetDTO dto ) {
        DynamicQueryMetadata metadata = new DynamicQueryMetadata();
        if ( dto.getOptions() instanceof TextWidgetOptions textWidgetOptions && textWidgetOptions.getInputMethod()
                .equals( DashboardEnums.TEXT_WIDGET_INPUT_METHOD.USER.getId() ) ) {
            metadata.setMetadataByColumn( DataDashboardConstants.TEXT_WIDGET_FIELDS.DISPLAY_VALUE, "String",
                    DataDashboardConstants.TEXT_WIDGET_FIELDS.DISPLAY_VALUE );
        } else {
            List< Map< String, String > > metadataList = ( List< Map< String, String > > ) content.get( "metadata" );
            Map< String, String > selectedColumns = getSelectionColumnsFromWidgetOptions( dto );
            for ( var selectedColumn : selectedColumns.entrySet() ) {
                for ( var jsonMeta : metadataList ) {
                    if ( selectedColumn.getValue().equals( jsonMeta.get( "name" ) ) ) {
                        metadata.setMetadataByColumn( selectedColumn.getKey(), jsonMeta.get( "type" ), selectedColumn.getValue() );
                    }
                }
            }
        }
        return metadata;
    }

    /**
     * Gets selection columns from widget options.
     *
     * @param dto
     *         the dto
     *
     * @return the selection columns from widget options
     */
    private static Map< String, String > getSelectionColumnsFromWidgetOptions( DashboardWidgetDTO dto ) {
        Map< String, String > columns = new HashMap<>();
        switch ( DashboardEnums.WIDGET_TYPE.getById( dto.getType() ) ) {
            case MIX_CHART -> {
                MixChartWidgetOptions options = ( MixChartWidgetOptions ) dto.getOptions();
                for ( int i = 0; i < options.getX_axis().size(); i++ ) {
                    columns.put( DataDashboardConstants.WIDGET_FIELDS.X_AXIS + ConstantsString.UNDERSCORE + i,
                            options.getX_axis().get( i ) );
                }
                for ( int i = 0; i < options.getY_axis().size(); i++ ) {
                    columns.put( DataDashboardConstants.WIDGET_FIELDS.Y_AXIS + ConstantsString.UNDERSCORE + i,
                            options.getY_axis().get( i ) );
                }
            }
            case BAR -> {
                BarWidgetOptions options = ( BarWidgetOptions ) dto.getOptions();
                columns.put( DataDashboardConstants.WIDGET_FIELDS.X_AXIS, options.getX_axis() );
                columns.put( DataDashboardConstants.WIDGET_FIELDS.Y_AXIS, options.getY_axis() );
            }
            case RADAR -> {
                RadarWidgetOptions options = ( RadarWidgetOptions ) dto.getOptions();
                for ( int i = 0; i < options.getValue().size(); i++ ) {
                    columns.put( DataDashboardConstants.RADAR_WIDGET_FIELDS.VALUE + ConstantsString.UNDERSCORE + i,
                            options.getValue().get( i ) );
                }
                columns.put( DataDashboardConstants.RADAR_WIDGET_FIELDS.INDICATOR, options.getIndicator() );
                if ( StringUtils.isNotBlank( options.getMax() ) ) {
                    columns.put( DataDashboardConstants.RADAR_WIDGET_FIELDS.MAX, options.getMax() );
                }
                if ( StringUtils.isNotBlank( options.getMin() ) ) {
                    columns.put( DataDashboardConstants.RADAR_WIDGET_FIELDS.MIN, options.getMin() );
                }
            }
            case METAL_BASE -> {
                //do nothing
            }
            case SCATTER -> {
                ScatterWidgetOptions options = ( ScatterWidgetOptions ) dto.getOptions();
                columns.put( DataDashboardConstants.WIDGET_FIELDS.X_AXIS, options.getX_axis() );
                columns.put( DataDashboardConstants.WIDGET_FIELDS.Y_AXIS, options.getY_axis() );
            }
            case TEXT -> {
                TextWidgetOptions options = ( TextWidgetOptions ) dto.getOptions();
                if ( DashboardEnums.TEXT_WIDGET_INPUT_METHOD.getById( options.getInputMethod() )
                        .equals( DashboardEnums.TEXT_WIDGET_INPUT_METHOD.SELECT ) ) {
                    columns.put( DataDashboardConstants.TEXT_WIDGET_FIELDS.COLUMN, options.getColumn() );
                }
            }
            case LINE -> {
                LineWidgetOptions options = ( LineWidgetOptions ) dto.getOptions();
                switch ( DashboardEnums.LINE_WIDGET_OPTIONS.getById( options.getLineWidgetType() ) ) {
                    case MULTIPLE_X_SINGLE_Y -> {
                        LineWidgetMultipleXOptions lineOptions = ( LineWidgetMultipleXOptions ) dto.getOptions();
                        for ( int i = 0; i < lineOptions.getX_axis().size(); i++ ) {
                            columns.put( DataDashboardConstants.WIDGET_FIELDS.X_AXIS + ConstantsString.UNDERSCORE + i,
                                    lineOptions.getX_axis().get( i ) );
                        }
                        columns.put( DataDashboardConstants.WIDGET_FIELDS.Y_AXIS, lineOptions.getY_axis() );
                    }
                    case MULTIPLE_Y_SINGLE_X -> {
                        LineWidgetMultipleYOptions lineOptions = ( LineWidgetMultipleYOptions ) dto.getOptions();
                        columns.put( DataDashboardConstants.WIDGET_FIELDS.X_AXIS, lineOptions.getX_axis() );
                        for ( int i = 0; i < lineOptions.getY_axis().size(); i++ ) {
                            columns.put( DataDashboardConstants.WIDGET_FIELDS.Y_AXIS + ConstantsString.UNDERSCORE + i,
                                    lineOptions.getY_axis().get( i ) );
                        }
                    }
                    case MULTIPLE_X_MULTIPLE_Y -> {
                        LineWidgetMultipleXYOptions lineOptions = ( LineWidgetMultipleXYOptions ) dto.getOptions();
                        for ( int i = 0; i < lineOptions.getX_axis().size(); i++ ) {
                            columns.put( DataDashboardConstants.WIDGET_FIELDS.X_AXIS + ConstantsString.UNDERSCORE + i,
                                    lineOptions.getX_axis().get( i ) );
                        }
                        for ( int i = 0; i < lineOptions.getY_axis().size(); i++ ) {
                            columns.put( DataDashboardConstants.WIDGET_FIELDS.Y_AXIS + ConstantsString.UNDERSCORE + i,
                                    lineOptions.getY_axis().get( i ) );
                        }
                    }
                }
            }
            case TABLE -> {

                TableWidgetOptions options = ( TableWidgetOptions ) dto.getOptions();
                for ( int i = 0; i < options.getTableColumns().size(); i++ ) {
                    columns.put( options.getTableColumns().get( i ), options.getTableColumns().get( i ) );
                }
            }
            case PST, TREEMAP, GROUP -> {
                //do nothing
            }
        }
        return columns;
    }

    /**
     * Gets data based on plot type.
     *
     * @param dto
     *         the dto
     * @param data
     *         the data
     * @param list
     *         the list
     */
    private static void getDataBasedOnPlotType( DashboardWidgetDTO dto, DynamicQueryData data, List< Map< String, Object > > list ) {
        switch ( DashboardEnums.WIDGET_TYPE.getById( dto.getType() ) ) {
            case MIX_CHART -> getDataForMixChartWidget( dto, data, list );
            case BAR -> getDataForBarWidget( dto, data, list );
            case SCATTER -> getDataForScatterWidget( dto, data, list );
            case LINE -> getDataForLineWidget( dto, data, list );
            case TEXT -> getDataForTextWidget( dto, data, list );
            case RADAR -> getDataForRadarWidget( dto, data, list );
            case TABLE -> getDataForTableWidget( dto, data, list );
            case METAL_BASE, PST, TREEMAP, GROUP -> {
                //do nothing
            }
        }
    }

    /**
     * Gets data for radar widget.
     *
     * @param dto
     *         the dto
     * @param data
     *         the data
     * @param list
     *         the list
     */
    private static void getDataForRadarWidget( DashboardWidgetDTO dto, DynamicQueryData data, List< Map< String, Object > > list ) {
        RadarWidgetOptions options = ( RadarWidgetOptions ) dto.getOptions();
        for ( int i = 0; i < options.getValue().size(); i++ ) {
            getAttributes( data, list, options.getValue().get( i ),
                    DataDashboardConstants.RADAR_WIDGET_FIELDS.VALUE + ConstantsString.UNDERSCORE + i );
        }
        getAttributes( data, list, options.getIndicator(), DataDashboardConstants.RADAR_WIDGET_FIELDS.INDICATOR );
        if ( StringUtils.isNotBlank( options.getMax() ) ) {
            getAttributes( data, list, options.getMax(), DataDashboardConstants.RADAR_WIDGET_FIELDS.MAX );
        }
        if ( StringUtils.isNotBlank( options.getMin() ) ) {
            getAttributes( data, list, options.getMin(), DataDashboardConstants.RADAR_WIDGET_FIELDS.MIN );
        }
    }

    /**
     * Gets data for mix chart widget.
     *
     * @param dto
     *         the dto
     * @param data
     *         the data
     * @param list
     *         the list
     */
    private static void getDataForMixChartWidget( DashboardWidgetDTO dto, DynamicQueryData data, List< Map< String, Object > > list ) {
        MixChartWidgetOptions options = ( MixChartWidgetOptions ) dto.getOptions();
        for ( int i = 0; i < options.getX_axis().size(); i++ ) {
            getAttributes( data, list, options.getX_axis().get( i ),
                    DataDashboardConstants.WIDGET_FIELDS.X_AXIS + ConstantsString.UNDERSCORE + i );
        }
        for ( int i = 0; i < options.getY_axis().size(); i++ ) {
            getAttributes( data, list, options.getY_axis().get( i ),
                    DataDashboardConstants.WIDGET_FIELDS.Y_AXIS + ConstantsString.UNDERSCORE + i );
        }
    }

    /**
     * Gets data for line widget.
     *
     * @param dto
     *         the dto
     * @param data
     *         the data
     * @param list
     *         the list
     */
    private static void getDataForLineWidget( DashboardWidgetDTO dto, DynamicQueryData data, List< Map< String, Object > > list ) {
        LineWidgetOptions options = ( LineWidgetOptions ) dto.getOptions();
        switch ( DashboardEnums.LINE_WIDGET_OPTIONS.getById( options.getLineWidgetType() ) ) {
            case MULTIPLE_X_SINGLE_Y -> {
                getDataForLineMultipleXSingleY( data, list, ( LineWidgetMultipleXOptions ) options );
            }
            case MULTIPLE_Y_SINGLE_X -> {
                getDataForLineSingleXMultipleY( data, list, ( LineWidgetMultipleYOptions ) options );
            }
            case MULTIPLE_X_MULTIPLE_Y -> {
                getDataForMultipleXMultipleY( data, list, ( LineWidgetMultipleXYOptions ) options );
            }
        }
    }

    /**
     * Gets data for multiple x multiple y.
     *
     * @param data
     *         the data
     * @param list
     *         the list
     * @param options
     *         the options
     */
    private static void getDataForMultipleXMultipleY( DynamicQueryData data, List< Map< String, Object > > list,
            LineWidgetMultipleXYOptions options ) {
        for ( int i = 0; i < options.getX_axis().size(); i++ ) {
            getAttributes( data, list, options.getX_axis().get( i ),
                    DataDashboardConstants.WIDGET_FIELDS.X_AXIS + ConstantsString.UNDERSCORE + i );
        }
        for ( int i = 0; i < options.getY_axis().size(); i++ ) {
            getAttributes( data, list, options.getY_axis().get( i ),
                    DataDashboardConstants.WIDGET_FIELDS.Y_AXIS + ConstantsString.UNDERSCORE + i );
        }
    }

    /**
     * Gets data for line single x multiple y.
     *
     * @param data
     *         the data
     * @param list
     *         the list
     * @param options
     *         the options
     */
    private static void getDataForLineSingleXMultipleY( DynamicQueryData data, List< Map< String, Object > > list,
            LineWidgetMultipleYOptions options ) {
        for ( int i = 0; i < options.getY_axis().size(); i++ ) {
            getAttributes( data, list, options.getY_axis().get( i ),
                    DataDashboardConstants.WIDGET_FIELDS.Y_AXIS + ConstantsString.UNDERSCORE + i );
        }
        getAttributes( data, list, options.getX_axis(), DataDashboardConstants.WIDGET_FIELDS.X_AXIS );
    }

    /**
     * Gets data for line multiple x single y.
     *
     * @param data
     *         the data
     * @param list
     *         the list
     * @param options
     *         the options
     */
    private static void getDataForLineMultipleXSingleY( DynamicQueryData data, List< Map< String, Object > > list,
            LineWidgetMultipleXOptions options ) {
        for ( int i = 0; i < options.getX_axis().size(); i++ ) {
            getAttributes( data, list, options.getX_axis().get( i ),
                    DataDashboardConstants.WIDGET_FIELDS.X_AXIS + ConstantsString.UNDERSCORE + i );
        }
        getAttributes( data, list, options.getY_axis(), DataDashboardConstants.WIDGET_FIELDS.Y_AXIS );
    }

    /**
     * Gets data from scatter widget.
     *
     * @param dto
     *         the dto
     * @param data
     *         the data
     * @param list
     *         the list
     */
    private static void getDataForScatterWidget( DashboardWidgetDTO dto, DynamicQueryData data, List< Map< String, Object > > list ) {
        ScatterWidgetOptions options = ( ScatterWidgetOptions ) dto.getOptions();
        getAttributes( data, list, options.getX_axis(), DataDashboardConstants.WIDGET_FIELDS.X_AXIS );
        getAttributes( data, list, options.getY_axis(), DataDashboardConstants.WIDGET_FIELDS.Y_AXIS );
    }

    /**
     * Gets data for text widget.
     *
     * @param dto
     *         the dto
     * @param data
     *         the data
     * @param list
     *         the list
     */
    private static void getDataForTextWidget( DashboardWidgetDTO dto, DynamicQueryData data, List< Map< String, Object > > list ) {
        TextWidgetOptions options = ( TextWidgetOptions ) dto.getOptions();
        if ( DashboardEnums.TEXT_WIDGET_INPUT_METHOD.getById( options.getInputMethod() )
                .equals( DashboardEnums.TEXT_WIDGET_INPUT_METHOD.USER ) ) {
            data.put( DataDashboardConstants.TEXT_WIDGET_FIELDS.DISPLAY_VALUE, Collections.singletonList( options.getDisplayValue() ) );
            return;
        }
        getAttributes( data, list, options.getColumn(), DataDashboardConstants.TEXT_WIDGET_FIELDS.COLUMN );
    }

    private static void getDataForTableWidget( DashboardWidgetDTO dto, DynamicQueryData data, List< Map< String, Object > > list ) {
        TableWidgetOptions options = ( TableWidgetOptions ) dto.getOptions();
        for ( String column : options.getTableColumns() ) {
            getAttributes( data, list, column, column );
        }
    }

    /**
     * Gets data for bar widget.
     *
     * @param dto
     *         the dto
     * @param data
     *         the data
     * @param list
     *         the list
     */
    private static void getDataForBarWidget( DashboardWidgetDTO dto, DynamicQueryData data, List< Map< String, Object > > list ) {
        BarWidgetOptions options = ( BarWidgetOptions ) dto.getOptions();
        getAttributes( data, list, options.getX_axis(), DataDashboardConstants.WIDGET_FIELDS.X_AXIS );
        getAttributes( data, list, options.getY_axis(), DataDashboardConstants.WIDGET_FIELDS.Y_AXIS );
    }

    /**
     * Gets filtered list.
     *
     * @param list
     *         the list
     * @param columns
     *         the columns
     *
     * @return the filtered list
     */
    private static List< Map< String, Object > > getFilteredList( List< Map< String, Object > > list, List< FilterColumn > columns ) {
        if ( CollectionUtils.isEmpty( columns ) ) {
            return list;
        }
        List< Map< String, Object > > filteredList = new ArrayList<>();
        for ( var item : list ) {
            if ( applyFilterOnListItem( item, columns ) ) {
                filteredList.add( item );
            }
        }
        return filteredList;
    }

    /**
     * Apply filter on list item boolean.
     *
     * @param item
     *         the item
     * @param columns
     *         the columns
     *
     * @return the boolean
     */
    private static boolean applyFilterOnListItem( Map< String, Object > item, List< FilterColumn > columns ) {
        boolean andFiltersResults = applyAndFiltersOnJsonRecord( item, columns );
        boolean orFiltersResults = applyOrFiltersOnJsonRecord( item, columns );

        return andFiltersResults && orFiltersResults;
    }

    /**
     * Apply or filters on json record boolean.
     *
     * @param item
     *         the item
     * @param columns
     *         the columns
     *
     * @return the boolean
     */
    private static boolean applyOrFiltersOnJsonRecord( Map< String, Object > item, List< FilterColumn > columns ) {
        boolean filterResult = true;
        for ( var column : columns ) {
            if ( CollectionUtils.isEmpty( column.getFilters() ) ) {
                break;
            }
            var columnValue = item.get( column.getName() );
            var orFilters = column.getFilters().stream().filter( filter -> filter.getCondition().equals( "OR" ) ).toList();
            for ( int filterIndex = 0; filterIndex < orFilters.size(); filterIndex++ ) {
                if ( filterIndex == 0 ) {
                    filterResult = applySingleFilter( String.valueOf( columnValue ), orFilters.get( filterIndex ), column.getType() );
                } else {
                    filterResult = filterResult || applySingleFilter( String.valueOf( columnValue ), orFilters.get( filterIndex ),
                            column.getType() );
                }

            }
        }
        return filterResult;
    }

    /**
     * Apply single filter boolean.
     *
     * @param value
     *         the value
     * @param filter
     *         the filter
     * @param type
     *         the type
     *
     * @return the boolean
     */
    private static boolean applySingleFilter( String value, Filter filter, String type ) {
        return switch ( FilterType.getByName( type ) ) {
            case number -> applySingleNumberFilter( value, filter );
            case text -> applySingleTextFilter( value, filter );
            case dateRange -> applySingleDateFilter( value, filter );
            case uuid -> applySingleUuidFilter( value, filter );

        };
    }

    /**
     * Apply single number filter boolean.
     *
     * @param value
     *         the value
     * @param filter
     *         the filter
     *
     * @return the boolean
     */
    private static boolean applySingleNumberFilter( String value, Filter filter ) {
        float numericValue = Float.parseFloat( value );
        float filterValue = Float.parseFloat( filter.getValue() );
        switch ( filter.getOperator() ) {
            case ConstantsFilterOperators.GREATER_THAN -> {
                return numericValue > filterValue;
            }
            case ConstantsFilterOperators.GREATER_THAN_OR_EQUAL_TO -> {
                return numericValue >= filterValue;
            }
            case ConstantsFilterOperators.LESS_THAN -> {
                return numericValue < filterValue;
            }
            case ConstantsFilterOperators.LESS_THAN_OR_EQUAL_TO -> {
                return numericValue <= filterValue;
            }
            case ConstantsFilterOperators.EQUALS -> {
                return numericValue == filterValue;
            }
            case ConstantsFilterOperators.NOT_EQUALS -> {
                return numericValue != filterValue;
            }
            default -> {
                return true;
            }
        }
    }

    /**
     * Apply single date filter boolean.
     *
     * @param value
     *         the value
     * @param filter
     *         the filter
     *
     * @return the boolean
     */
    private static boolean applySingleDateFilter( String value, Filter filter ) {
        Date dateValue = DateUtils.fromString( value );
        Date filterValue = DateUtils.fromString( filter.getValue() );
        return switch ( filter.getOperator() ) {
            case ConstantsFilterOperators.AFTER -> dateValue.after( filterValue );
            case ConstantsFilterOperators.AFTER_OR_EQUAL_TO -> dateValue.equals( filterValue ) || dateValue.after( filterValue );
            case ConstantsFilterOperators.BEFORE -> dateValue.before( filterValue );
            case ConstantsFilterOperators.BEFORE_OR_EQUAL_TO -> dateValue.equals( filterValue ) || dateValue.before( filterValue );
            case ConstantsFilterOperators.EQUALS -> dateValue.equals( filterValue );
            case ConstantsFilterOperators.NOT_EQUALS -> !dateValue.equals( filterValue );
            default -> true;
        };
    }

    /**
     * Apply single uuid filter boolean.
     *
     * @param value
     *         the value
     * @param filter
     *         the filter
     *
     * @return the boolean
     */
    private static boolean applySingleUuidFilter( String value, Filter filter ) {
        switch ( filter.getOperator() ) {
            case ConstantsFilterOperators.EQUALS -> {
                return value.equalsIgnoreCase( filter.getValue() );
            }
            case ConstantsFilterOperators.NOT_EQUALS -> {
                return !value.equalsIgnoreCase( filter.getValue() );
            }
            default -> {
                return true;
            }
        }
    }

    /**
     * Apply single text filter boolean.
     *
     * @param value
     *         the value
     * @param filter
     *         the filter
     *
     * @return the boolean
     */
    private static boolean applySingleTextFilter( String value, Filter filter ) {
        switch ( filter.getOperator() ) {
            case ConstantsFilterOperators.BEGINS_WITH -> {
                return value.regionMatches( true, 0, filter.getValue(), 0, filter.getValue().length() );
            }
            case ConstantsFilterOperators.ENDS_WITH -> {
                return value.regionMatches( true, value.length() - filter.getValue().length(), filter.getValue(), 0,
                        filter.getValue().length() );
            }
            case ConstantsFilterOperators.CONTAINS -> {
                return StringUtils.containsIgnoreCase( value, filter.getValue() );
            }
            case ConstantsFilterOperators.NOT_CONTAINS -> {
                return !StringUtils.containsIgnoreCase( value, filter.getValue() );
            }
            case ConstantsFilterOperators.EQUALS -> {
                return value.equalsIgnoreCase( filter.getValue() );
            }
            case ConstantsFilterOperators.NOT_EQUALS -> {
                return !value.equalsIgnoreCase( filter.getValue() );
            }
            default -> {
                return true;
            }
        }
    }

    /**
     * Apply and filters on json record boolean.
     *
     * @param item
     *         the item
     * @param columns
     *         the columns
     *
     * @return the boolean
     */

    private static boolean applyAndFiltersOnJsonRecord( Map< String, Object > item, List< FilterColumn > columns ) {
        for ( var column : columns ) {
            if ( CollectionUtils.isEmpty( column.getFilters() ) ) {
                continue;
            }

            var columnValue = item.get( column.getName() );
            var andFilters = column.getFilters().stream().filter( filter -> "AND".equalsIgnoreCase( filter.getCondition() ) ).toList();

            for ( Filter filter : andFilters ) {
                boolean match = applySingleFilter( String.valueOf( columnValue ), filter, column.getType() );
                if ( !match ) {
                    return false; // any one mismatch in AND condition means record doesn't pass
                }
            }
        }
        return true; // all AND conditions passed
    }

    /**
     * Gets metadata from json.
     *
     * @param content
     *         the content
     * @param data
     *         the data
     *
     * @return the metadata from json
     */
    private static DynamicQueryMetadata getMetadataFromJson( Map< String, Object > content, DynamicQueryData data ) {
        List< Map< String, String > > metadataList = ( List< Map< String, String > > ) content.get( "metadata" );
        DynamicQueryMetadata metadata = new DynamicQueryMetadata();
        for ( var metadataFromJson : metadataList ) {
            metadata.setMetadataByColumn( metadataFromJson.get( "name" ), metadataFromJson.get( "type" ), metadataFromJson.get( "name" ) );
        }
        return metadata;
    }

    /**
     * Gets attributes.
     *
     * @param queryData
     *         the query data
     * @param list
     *         the list
     * @param columnName
     *         the column name
     * @param columnAlias
     *         the column alias
     */
    private static void getAttributes( DynamicQueryData queryData, List< Map< String, Object > > list, String columnName,
            String columnAlias ) {
        for ( var row : list ) {
            var valueFromJson = row.get( columnName );
            queryData.addValueByColumn( columnAlias, String.valueOf( valueFromJson ) );
        }

    }

    /**
     * Gets json preview.
     *
     * @param jsonPath
     *         the json path
     *
     * @return the json preview
     */
    public static Object getJsonPreview( Path jsonPath ) {
        try {
            return JsonUtils.jsonToMap( Files.readString( jsonPath, StandardCharsets.UTF_8 ), new HashMap<>() );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * Populate columns metadata for widget filters for json list.
     *
     * @param outputFilePath
     *         the output file path
     *
     * @return the list
     */
    public static List< TableColumn > populateColumnsMetadataForWidgetFiltersForJson( Path outputFilePath ) {
        Map< String, Object > content = getFileContent( outputFilePath );
        List< Map< String, String > > metadataList = ( List< Map< String, String > > ) content.get( "metadata" );
        List< TableColumn > columns = new ArrayList<>();
        for ( var metadataFromJson : metadataList ) {
            String column = metadataFromJson.get( "name" );
            columns.add( new TableColumn( column, column, column, getFilterTypeFromColumnType( metadataFromJson.get( "type" ) ) ) );
        }
        return columns;
    }

    /**
     * Gets filter type from column type.
     *
     * @param columnType
     *         the column type
     *
     * @return the filter type from column type
     */
    private static String getFilterTypeFromColumnType( String columnType ) {
        return switch ( columnType ) {
            case "Integer", "Double" -> FilterType.number.name();
            case "Date" -> FilterType.dateRange.name();
            default -> FilterType.text.name();
        };

    }

    /**
     * Validate user json.
     *
     * @param filePath
     *         the file path
     */
    public static void validateUserJson( Path filePath ) {
        if ( Files.notExists( filePath ) ) {
            throw new SusException( "File not found or is in a directory SIMuSPACE can not access" );
        }
        if ( !Files.isReadable( filePath ) ) {
            throw new SusException( "No permission to read file" );
        }
    }

    /**
     * Gets columns from json.
     *
     * @param filePath
     *         the file path
     *
     * @return the columns from json
     */
    public static List< SelectOptionsUI > getColumnsFromJson( Path filePath ) {
        try {
            List< SelectOptionsUI > options = null;
            Map< String, Object > content = getFileContent( filePath );
            List< Map< String, String > > metadataList = ( List< Map< String, String > > ) content.get( "metadata" );
            if ( !metadataList.isEmpty() ) {
                options = metadataList.stream().map( meta -> new SelectOptionsUI( meta.get( "name" ), meta.get( "name" ) ) ).toList();
            }
            return options;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * Gets file content.
     *
     * @param filePath
     *         the file path
     *
     * @return the file content
     */
    public static Map< String, Object > getFileContent( Path filePath ) {
        try {
            return ( Map< String, Object > ) JsonUtils.jsonToMap( Files.readString( filePath, StandardCharsets.UTF_8 ), new HashMap<>() );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * Gets fields from json.
     *
     * @param outputFilePath
     *         the output file path
     *
     * @return the fields from json
     */
    public static Map< String, Object > getFieldsFromJson( Path outputFilePath ) {
        var content = getFileContent( outputFilePath );
        var output = ( Map< String, Object > ) content.get( "output" );
        return ( Map< String, Object > ) output.get( "fields" );
    }

}
