package de.soco.software.simuspace.suscore.object.utility;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFilterOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants;
import de.soco.software.simuspace.suscore.common.enums.DashboardEnums;
import de.soco.software.simuspace.suscore.common.enums.FilterType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryData;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryMetadata;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryResponse;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
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
 * The type Dashboard csv util.
 */
@Log4j2
public class DashboardCSVUtil {

    /**
     * Instantiates a new Dashboard csv util.
     */
    private DashboardCSVUtil() {

    }

    /**
     * The constant intPattern.
     */
    private static final Pattern intPattern = Pattern.compile( "^-?\\d+$" );

    /**
     * The constant floatPattern.
     */
    private static final Pattern floatPattern = Pattern.compile( "^-?\\d*\\.\\d+$" );

    /**
     * The constant booleanPattern.
     */
    private static final Pattern booleanPattern = Pattern.compile( "^(?i)(true|false)$" );

    /**
     * The constant CSV_MIME_TYPE.
     */
    private static final String CSV_MIME_TYPE = "text/csv";

    /**
     * Infer csv data type string.
     *
     * @param value
     *         the value
     *
     * @return the string
     */
    public static String inferCSVDataType( String value ) {

        if ( intPattern.matcher( value ).matches() ) {
            return "Integer";
        } else if ( floatPattern.matcher( value ).matches() ) {
            return "Float";
        } else if ( booleanPattern.matcher( value ).matches() ) {
            return "Boolean";
        } else if ( DateUtils.isDate( value ) ) {
            return "Date";
        }
        return "String";
    }

    /**
     * Infer csv filter type string.
     *
     * @param value
     *         the value
     *
     * @return the string
     */
    private static String inferCSVFilterType( String value ) {

        if ( intPattern.matcher( value ).matches() || floatPattern.matcher( value ).matches() ) {
            return "number";
        } else if ( booleanPattern.matcher( value ).matches() ) {
            return "select";
        } else if ( DateUtils.isDate( value ) ) {
            return "dateRange";
        }
        return "text";
    }

    /**
     * Gets columns from csv.
     *
     * @param outputFilePath
     *         the output file path
     *
     * @return the columns from csv
     */
    public static List< SelectOptionsUI > getColumnsFromCSV( Path outputFilePath ) {
        try {
            Reader in = new FileReader( outputFilePath.toAbsolutePath().toString() );
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setSkipHeaderRecord( true ).build();
            Iterable< CSVRecord > records = csvFormat.parse( in );
            List< SelectOptionsUI > csvColumns = new ArrayList<>();

            // The first record is the header if present
            CSVRecord header = records.iterator().next(); // Read the first record (header)
            // Add header names to the list
            for ( String column : header.stream().toList() ) {
                csvColumns.add( new SelectOptionsUI( column, column ) );
            }
            return csvColumns;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_WHILE_READING_OUTPUT_FILE.getKey(),
                    DashboardEnums.WidgetPythonOutputOptions.CSV.getName() ) );
        }
    }

    /**
     * Gets csv preview.
     *
     * @param outputFilePath
     *         the output file path
     *
     * @return the csv preview
     */
    public static List< Object > getCSVPreview( Path outputFilePath ) {
        try {
            Reader in = new FileReader( outputFilePath.toAbsolutePath().toString() );
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setSkipHeaderRecord( true ).build();
            List< Object > csv = new ArrayList<>();
            Iterable< CSVRecord > records = csvFormat.parse( in );
            for ( CSVRecord record : records ) {
                csv.add( record.toList() );
            }
            return csv;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_WHILE_READING_OUTPUT_FILE.getKey(),
                    DashboardEnums.WidgetPythonOutputOptions.CSV.getName() ) );
        }
    }

    /**
     * Validate csv mime type.
     *
     * @param outputFilePath
     *         the output file path
     *
     * @throws IOException
     *         the io exception
     */
    public static void validateCSVMimeType( Path outputFilePath ) throws IOException {
        String mimeType = Files.probeContentType( outputFilePath );
        if ( mimeType == null || !mimeType.equals( CSV_MIME_TYPE ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_FILE_FORMAT_FOR_FILE.getKey(), "CSV" ) );
        }
    }

    /**
     * Gets data from csv file for widget.
     *
     * @param outputFilePath
     *         the output file path
     * @param dto
     *         the dto
     *
     * @return the data from csv file for widget
     */
    public static DynamicQueryResponse getDataFromCsvFileForWidget( Path outputFilePath, DashboardWidgetDTO dto ) {
        DynamicQueryResponse queryResponse = new DynamicQueryResponse();
        try {
            Reader in = new FileReader( outputFilePath.toAbsolutePath().toString() );
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setSkipHeaderRecord( true ).build();
            Iterable< CSVRecord > records = csvFormat.parse( in );
            DynamicQueryData data = new DynamicQueryData();
            DynamicQueryMetadata metadata = new DynamicQueryMetadata();
            CSVRecord header = records.iterator().next();
            List< String > headerList = header.stream().toList();

            // Store all records in a headerList to iterate multiple times
            List< CSVRecord > allRecords = new ArrayList<>();
            applyFilters( dto, records, header, allRecords );
            getDataFromCSVForAllPlotTypes( dto, headerList, metadata, allRecords, data );
            queryResponse.setData( data );
            queryResponse.setMetadata( metadata );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_WHILE_READING_OUTPUT_FILE.getKey(),
                    DashboardEnums.WidgetPythonOutputOptions.CSV.getName() ) );
        }
        return queryResponse;
    }

    public static DynamicQueryResponse getPaginatedDataFromCsvFileForWidget( Path outputFilePath, DashboardWidgetDTO dto,
            FiltersDTO filters ) {
        DynamicQueryResponse queryResponse = new DynamicQueryResponse();
        try {
            Reader in = new FileReader( outputFilePath.toAbsolutePath().toString() );
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setSkipHeaderRecord( true ).build();
            Iterable< CSVRecord > records = csvFormat.parse( in );
            DynamicQueryData data = new DynamicQueryData();
            DynamicQueryMetadata metadata = new DynamicQueryMetadata();

            CSVRecord header = records.iterator().next();
            List< String > headerList = header.stream().toList();

            List< CSVRecord > allRecordsBeforeFiltering = new ArrayList<>();
            records.forEach( allRecordsBeforeFiltering::add ); // collect all rows
            filters.setTotalRecords( ( long ) allRecordsBeforeFiltering.size() );

            List< CSVRecord > filteredRecords = new ArrayList<>();
            applyFilters( dto, allRecordsBeforeFiltering, header, filteredRecords );

            filters.setFilteredRecords( ( long ) filteredRecords.size() );

            getDataFromCSVForAllPlotTypes( dto, headerList, metadata, filteredRecords, data );
            queryResponse.setData( data );
            queryResponse.setMetadata( metadata );

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_WHILE_READING_OUTPUT_FILE.getKey(),
                    DashboardEnums.WidgetPythonOutputOptions.CSV.getName() ) );
        }
        return queryResponse;
    }

    /**
     * Apply filters.
     *
     * @param dto
     *         the dto
     * @param records
     *         the records
     * @param header
     *         the header
     * @param allRecords
     *         the all records
     */
    private static void applyFilters( DashboardWidgetDTO dto, Iterable< CSVRecord > records, CSVRecord header,
            List< CSVRecord > allRecords ) {
        //iterating over csv rows
        records.forEach( record -> {
            if ( applyFiltersOnCsvRecord( record, dto.getColumns(), header ) ) {
                allRecords.add( record );
            }
        } );
    }

    /**
     * Apply filters on csv record boolean.
     *
     * @param record
     *         the record
     * @param columns
     *         the columns
     * @param header
     *         the header
     *
     * @return the boolean
     */
    private static boolean applyFiltersOnCsvRecord( CSVRecord record, List< FilterColumn > columns, CSVRecord header ) {
        if ( CollectionUtils.isEmpty( columns ) ) {
            return true;
        }
        boolean andFiltersResults = applyAndFiltersOnCsvRecord( record, columns, header );
        boolean orFiltersResults = applyOrFiltersOnCsvRecord( record, columns, header );

        return andFiltersResults && orFiltersResults;
    }

    /**
     * Apply or filters on csv record boolean.
     *
     * @param record
     *         the record
     * @param columns
     *         the columns
     * @param header
     *         the header
     *
     * @return the boolean
     */
    private static boolean applyAndFiltersOnCsvRecord( CSVRecord record, List< FilterColumn > columns, CSVRecord header ) {
        boolean filterResult = true;
        for ( int headerIndex = 0; headerIndex < header.size(); headerIndex++ ) {
            for ( var column : columns ) {
                if ( CollectionUtils.isNotEmpty( column.getFilters() ) ) {
                    var headerValue = header.get( headerIndex );
                    if ( column.getName().equals( headerValue ) ) {
                        var andFilters = column.getFilters().stream().filter( filter -> filter.getCondition().equals( "AND" ) ).toList();
                        for ( int filterIndex = 0; filterIndex < andFilters.size(); filterIndex++ ) {
                            if ( filterIndex == 0 ) {
                                filterResult = applySingleFilter( record.get( headerIndex ), andFilters.get( filterIndex ),
                                        column.getType() );
                            } else {
                                filterResult = filterResult && applySingleFilter( record.get( headerIndex ), andFilters.get( filterIndex ),
                                        column.getType() );
                            }

                        }
                    }
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
        if ( StringUtils.isBlank( value ) ) {
            return false;
        }
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
        Date filterValue = ( !intPattern.matcher( filter.getValue() ).matches() ) ? DateUtils.fromString( filter.getValue() )
                : new Date( Long.parseLong( filter.getValue() ) );
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
     * Apply and filters on csv record boolean.
     *
     * @param record
     *         the record
     * @param columns
     *         the columns
     * @param header
     *         the header
     *
     * @return the boolean
     */
    private static boolean applyOrFiltersOnCsvRecord( CSVRecord record, List< FilterColumn > columns, CSVRecord header ) {
        boolean filterResult = true;
        for ( int headerIndex = 0; headerIndex < header.size(); headerIndex++ ) {
            for ( var column : columns ) {
                if ( CollectionUtils.isNotEmpty( column.getFilters() ) && column.getName().equals( header.get( headerIndex ) ) ) {
                    var orFilters = column.getFilters().stream().filter( filter -> filter.getCondition().equals( "OR" ) ).toList();
                    for ( int filterIndex = 0; filterIndex < orFilters.size(); filterIndex++ ) {
                        if ( filterIndex == 0 ) {
                            filterResult = applySingleFilter( record.get( headerIndex ), orFilters.get( filterIndex ), column.getType() );
                        } else {
                            filterResult = filterResult || applySingleFilter( record.get( headerIndex ), orFilters.get( filterIndex ),
                                    column.getType() );
                        }

                    }
                }
            }
        }
        return filterResult;
    }

    /**
     * Gets data from csv for all plot types.
     *
     * @param dto
     *         the dto
     * @param headerList
     *         the headerList
     * @param metadata
     *         the metadata
     * @param allRecords
     *         the all records
     * @param data
     *         the data
     */
    private static void getDataFromCSVForAllPlotTypes( DashboardWidgetDTO dto, List< String > headerList, DynamicQueryMetadata metadata,
            List< CSVRecord > allRecords, DynamicQueryData data ) {
        switch ( DashboardEnums.WIDGET_TYPE.getById( dto.getType() ) ) {
            case MIX_CHART -> getDataFromCSVForMixChartWidget( dto, headerList, metadata, allRecords, data );
            case BAR -> getDataFromCSVForBarWidget( dto, headerList, metadata, allRecords, data );
            case SCATTER -> getDataFromCSVForScatterWidget( dto, headerList, metadata, allRecords, data );
            case LINE -> getDataFromCSVForLineWidget( dto, headerList, metadata, allRecords, data );
            case TEXT -> getDataFromCSVForTextWidget( dto, headerList, metadata, allRecords, data );
            case RADAR -> getDataFromCsvForRadarWidget( dto, headerList, metadata, allRecords, data );
            case TABLE -> getDataFromCSVForTableWidget( dto, headerList, metadata, allRecords, data );
            case METAL_BASE -> {
                //do nothing
            }
        }
    }

    /**
     * Gets data from csv for radar widget.
     *
     * @param dto
     *         the dto
     * @param list
     *         the list
     * @param metadata
     *         the metadata
     * @param allRecords
     *         the all records
     * @param data
     *         the data
     */
    private static void getDataFromCsvForRadarWidget( DashboardWidgetDTO dto, List< String > list, DynamicQueryMetadata metadata,
            List< CSVRecord > allRecords, DynamicQueryData data ) {
        RadarWidgetOptions radarOptions = ( RadarWidgetOptions ) dto.getOptions();
        getDataColumnDataFromCSV( list, metadata, allRecords, data, radarOptions.getIndicator(),
                DataDashboardConstants.RADAR_WIDGET_FIELDS.INDICATOR, DataDashboardConstants.RADAR_WIDGET_FIELDS.INDICATOR );
        List< String > values = radarOptions.getValue();
        for ( int i = 0; i < values.size(); i++ ) {
            String value = values.get( i );
            getDataColumnDataFromCSV( list, metadata, allRecords, data, value, value,
                    DataDashboardConstants.RADAR_WIDGET_FIELDS.VALUE + ConstantsString.UNDERSCORE + ( i
                            + ConstantsInteger.INTEGER_VALUE_ONE ) );
        }
        if ( StringUtils.isNotBlank( radarOptions.getMin() ) ) {
            getDataColumnDataFromCSV( list, metadata, allRecords, data, radarOptions.getMin(), radarOptions.getMin(),
                    DataDashboardConstants.RADAR_WIDGET_FIELDS.MIN );
        }
        if ( StringUtils.isNotBlank( radarOptions.getMax() ) ) {
            getDataColumnDataFromCSV( list, metadata, allRecords, data, radarOptions.getMax(), radarOptions.getMax(),
                    DataDashboardConstants.RADAR_WIDGET_FIELDS.MAX );
        }
    }

    /**
     * Gets data from csv for line widget.
     *
     * @param dto
     *         the dto
     * @param list
     *         the list
     * @param metadata
     *         the metadata
     * @param allRecords
     *         the all records
     * @param data
     *         the data
     */
    private static void getDataFromCSVForLineWidget( DashboardWidgetDTO dto, List< String > list, DynamicQueryMetadata metadata,
            List< CSVRecord > allRecords, DynamicQueryData data ) {
        LineWidgetOptions lineOptions = ( LineWidgetOptions ) dto.getOptions();
        switch ( DashboardEnums.LINE_WIDGET_OPTIONS.getById( lineOptions.getLineWidgetType() ) ) {
            case MULTIPLE_Y_SINGLE_X -> {
                LineWidgetMultipleYOptions multipleYOptions = ( LineWidgetMultipleYOptions ) lineOptions;
                getDataColumnDataFromCSV( list, metadata, allRecords, data, multipleYOptions.getX_axis(),
                        lineOptions.getX_axis_title() != null ? lineOptions.getX_axis_title() : multipleYOptions.getX_axis(),
                        DataDashboardConstants.WIDGET_FIELDS.X_AXIS );

                List< String > yAxis = multipleYOptions.getY_axis();
                for ( int i = 0; i < yAxis.size(); i++ ) {
                    String y_axis = yAxis.get( i );
                    getDataColumnDataFromCSV( list, metadata, allRecords, data, y_axis,
                            lineOptions.getY_axis_title() != null ? lineOptions.getY_axis_title() : y_axis,
                            DataDashboardConstants.WIDGET_FIELDS.Y_AXIS + ConstantsString.UNDERSCORE + ( i
                                    + ConstantsInteger.INTEGER_VALUE_ONE ) );
                }
            }

            case MULTIPLE_X_SINGLE_Y -> {
                LineWidgetMultipleXOptions multipleXOptions = ( LineWidgetMultipleXOptions ) lineOptions;

                List< String > xAxis = multipleXOptions.getX_axis();
                for ( int i = 0; i < xAxis.size(); i++ ) {
                    String x_axis = xAxis.get( i );
                    getDataColumnDataFromCSV( list, metadata, allRecords, data, x_axis,
                            lineOptions.getX_axis_title() != null ? lineOptions.getX_axis_title() : x_axis,
                            DataDashboardConstants.WIDGET_FIELDS.X_AXIS + ConstantsString.UNDERSCORE + ( i
                                    + ConstantsInteger.INTEGER_VALUE_ONE ) );

                }
                getDataColumnDataFromCSV( list, metadata, allRecords, data, multipleXOptions.getY_axis(),
                        lineOptions.getY_axis_title() != null ? lineOptions.getY_axis_title() : multipleXOptions.getY_axis(),
                        DataDashboardConstants.WIDGET_FIELDS.Y_AXIS );
            }

            case MULTIPLE_X_MULTIPLE_Y -> {
                LineWidgetMultipleXYOptions multipleXYOptions = ( LineWidgetMultipleXYOptions ) lineOptions;

                List< String > xAxis = multipleXYOptions.getX_axis();
                for ( int i = 0; i < xAxis.size(); i++ ) {
                    String x_axis = xAxis.get( i );
                    getDataColumnDataFromCSV( list, metadata, allRecords, data, x_axis,
                            lineOptions.getX_axis_title() != null ? lineOptions.getX_axis_title() : x_axis,
                            DataDashboardConstants.WIDGET_FIELDS.X_AXIS + ConstantsString.UNDERSCORE + ( i
                                    + ConstantsInteger.INTEGER_VALUE_ONE ) );

                }
                List< String > yAxis = multipleXYOptions.getY_axis();
                for ( int i = 0; i < yAxis.size(); i++ ) {
                    String y_axis = yAxis.get( i );
                    getDataColumnDataFromCSV( list, metadata, allRecords, data, y_axis,
                            lineOptions.getY_axis_title() != null ? lineOptions.getY_axis_title() : y_axis,
                            DataDashboardConstants.WIDGET_FIELDS.Y_AXIS + ConstantsString.UNDERSCORE + ( i
                                    + ConstantsInteger.INTEGER_VALUE_ONE ) );
                }
            }
        }
    }

    /**
     * Gets data from csv for mix chart widget.
     *
     * @param dto
     *         the dto
     * @param list
     *         the list
     * @param metadata
     *         the metadata
     * @param allRecords
     *         the all records
     * @param data
     *         the data
     */
    private static void getDataFromCSVForMixChartWidget( DashboardWidgetDTO dto, List< String > list, DynamicQueryMetadata metadata,
            List< CSVRecord > allRecords, DynamicQueryData data ) {
        MixChartWidgetOptions mixChartWidgetOptions = ( MixChartWidgetOptions ) dto.getOptions();
        List< String > xAxis = mixChartWidgetOptions.getX_axis();
        for ( int i = 0; i < xAxis.size(); i++ ) {
            String x_axis = xAxis.get( i );
            getDataColumnDataFromCSV( list, metadata, allRecords, data, x_axis,
                    mixChartWidgetOptions.getX_axis_title() != null ? mixChartWidgetOptions.getX_axis_title() : x_axis,
                    DataDashboardConstants.WIDGET_FIELDS.X_AXIS + ConstantsString.UNDERSCORE + ( i + ConstantsInteger.INTEGER_VALUE_ONE ) );

        }
        List< String > yAxis = mixChartWidgetOptions.getY_axis();
        for ( int i = 0; i < yAxis.size(); i++ ) {
            String y_axis = yAxis.get( i );
            getDataColumnDataFromCSV( list, metadata, allRecords, data, y_axis,
                    mixChartWidgetOptions.getY_axis_title() != null ? mixChartWidgetOptions.getY_axis_title() : y_axis,
                    DataDashboardConstants.WIDGET_FIELDS.Y_AXIS + ConstantsString.UNDERSCORE + ( i + ConstantsInteger.INTEGER_VALUE_ONE ) );
        }
    }

    /**
     * Gets data from csv for scatter widget.
     *
     * @param dto
     *         the dto
     * @param list
     *         the list
     * @param metadata
     *         the metadata
     * @param allRecords
     *         the all records
     * @param data
     *         the data
     */
    private static void getDataFromCSVForScatterWidget( DashboardWidgetDTO dto, List< String > list, DynamicQueryMetadata metadata,
            List< CSVRecord > allRecords, DynamicQueryData data ) {
        ScatterWidgetOptions scatterDTO = ( ScatterWidgetOptions ) dto.getOptions();
        getDataColumnDataFromCSV( list, metadata, allRecords, data, scatterDTO.getX_axis(),
                scatterDTO.getX_axis_title() != null ? scatterDTO.getX_axis_title() : scatterDTO.getX_axis(),
                DataDashboardConstants.WIDGET_FIELDS.X_AXIS );
        getDataColumnDataFromCSV( list, metadata, allRecords, data, scatterDTO.getY_axis(),
                scatterDTO.getY_axis_title() != null ? scatterDTO.getY_axis_title() : scatterDTO.getY_axis(),
                DataDashboardConstants.WIDGET_FIELDS.Y_AXIS );
        if ( scatterDTO.getPointSize() != null ) {
            getDataColumnDataFromCSV( list, metadata, allRecords, data, scatterDTO.getPointSize(),
                    DataDashboardConstants.SCATTER_WIDGET_FIELDS.POINT_SIZE, DataDashboardConstants.SCATTER_WIDGET_FIELDS.POINT_SIZE );
        }
        if ( scatterDTO.getPointColor() != null ) {
            getDataColumnDataFromCSV( list, metadata, allRecords, data, scatterDTO.getPointColor(),
                    DataDashboardConstants.SCATTER_WIDGET_FIELDS.POINT_COLOR, DataDashboardConstants.SCATTER_WIDGET_FIELDS.POINT_COLOR );
        }
    }

    /**
     * Gets data from csv for text widget.
     *
     * @param dto
     *         the dto
     * @param list
     *         the list
     * @param metadata
     *         the metadata
     * @param allRecords
     *         the all records
     * @param data
     *         the data
     */
    private static void getDataFromCSVForTextWidget( DashboardWidgetDTO dto, List< String > list, DynamicQueryMetadata metadata,
            List< CSVRecord > allRecords, DynamicQueryData data ) {
        TextWidgetOptions textOptions = ( TextWidgetOptions ) dto.getOptions();
        if ( DashboardEnums.TEXT_WIDGET_INPUT_METHOD.getById( textOptions.getInputMethod() )
                .equals( DashboardEnums.TEXT_WIDGET_INPUT_METHOD.USER ) ) {
            data.put( DataDashboardConstants.TEXT_WIDGET_FIELDS.DISPLAY_VALUE, Collections.singletonList( textOptions.getDisplayValue() ) );
            metadata.setMetadataByColumn( DataDashboardConstants.TEXT_WIDGET_FIELDS.DISPLAY_VALUE, "String",
                    DataDashboardConstants.TEXT_WIDGET_FIELDS.DISPLAY_VALUE );
            return;
        }
        getDataColumnDataFromCSV( list, metadata, allRecords, data, textOptions.getColumn(),
                textOptions.getColumnTitle() != null ? textOptions.getColumnTitle() : textOptions.getColumn(),
                DataDashboardConstants.TEXT_WIDGET_FIELDS.COLUMN );
    }

    private static void getDataFromCSVForTableWidget( DashboardWidgetDTO dto, List< String > list, DynamicQueryMetadata metadata,
            List< CSVRecord > allRecords, DynamicQueryData data ) {
        TableWidgetOptions tableOptions = ( TableWidgetOptions ) dto.getOptions();
        for ( String column : tableOptions.getTableColumns() ) {
            getDataColumnDataFromCSV( list, metadata, allRecords, data, column, column, column );
        }
    }

    /**
     * Gets data from csv for bar widget.
     *
     * @param dto
     *         the dto
     * @param list
     *         the list
     * @param metadata
     *         the metadata
     * @param allRecords
     *         the all records
     * @param data
     *         the data
     */
    private static void getDataFromCSVForBarWidget( DashboardWidgetDTO dto, List< String > list, DynamicQueryMetadata metadata,
            List< CSVRecord > allRecords, DynamicQueryData data ) {
        BarWidgetOptions barDTO = ( BarWidgetOptions ) dto.getOptions();
        getDataColumnDataFromCSV( list, metadata, allRecords, data, barDTO.getX_axis(),
                barDTO.getX_axis_title() != null ? barDTO.getX_axis_title() : barDTO.getX_axis(),
                DataDashboardConstants.WIDGET_FIELDS.X_AXIS );
        getDataColumnDataFromCSV( list, metadata, allRecords, data, barDTO.getY_axis(),
                barDTO.getY_axis_title() != null ? barDTO.getY_axis_title() : barDTO.getY_axis(),
                DataDashboardConstants.WIDGET_FIELDS.Y_AXIS );
        if ( barDTO.getBarColor() != null ) {
            getDataColumnDataFromCSV( list, metadata, allRecords, data, barDTO.getBarColor(),
                    DataDashboardConstants.BAR_WIDGET_FIELDS.BAR_COLOR, DataDashboardConstants.BAR_WIDGET_FIELDS.BAR_COLOR );
        }
    }

    /**
     * Gets data column data from csv.
     *
     * @param list
     *         the list
     * @param metadata
     *         the metadata
     * @param allRecords
     *         the all records
     * @param data
     *         the data
     * @param columnName
     *         the column name
     * @param columnTitle
     *         the column title
     * @param columnAlias
     *         the column alias
     */
    private static void getDataColumnDataFromCSV( List< String > list, DynamicQueryMetadata metadata, List< CSVRecord > allRecords,
            DynamicQueryData data, String columnName, String columnTitle, String columnAlias ) {
        int columnIndex = getColumnIndexFromHeaders( list, columnName );
        // Set metadata once per column
        if ( !metadata.containsKey( columnName ) && !allRecords.isEmpty() ) {
            String sampleValue = allRecords.get( 0 ).get( columnIndex );  // Get a sample value to infer type
            metadata.setMetadataByColumn( columnAlias, inferCSVDataType( sampleValue ), columnTitle );
        }

        // Add values to the data object for the given column
        for ( CSVRecord record : allRecords ) {
            var columnValue = record.get( columnIndex );
            data.addValueByColumn( columnAlias, columnValue );
        }
    }

    /**
     * Gets column index from headers.
     *
     * @param list
     *         the list
     * @param columnName
     *         the column name
     *
     * @return the column index from headers
     */
    private static int getColumnIndexFromHeaders( List< String > list, String columnName ) {
        for ( int i = 0; i < list.size(); i++ ) {
            if ( columnName.equals( list.get( i ) ) ) {
                return i;
            }
        }
        throw new SusException( "Column Found in CSV " + columnName );
    }

    /**
     * Validate user csv.
     *
     * @param filePath
     *         the file path
     */
    public static void validateUserCSV( Path filePath ) {
        if ( Files.notExists( filePath ) ) {
            throw new SusException( "File not found or is in a directory SIMuSPACE can not access" );
        }
        if ( !Files.isReadable( filePath ) ) {
            throw new SusException( "No permission to read file" );
        }
    }

    /**
     * Populate columns metadata for widget filters for csv list.
     *
     * @param outputFilePath
     *         the output file path
     *
     * @return the list
     */
    public static List< TableColumn > populateColumnsMetadataForWidgetFiltersForCsv( Path outputFilePath ) {
        try {
            Reader in = new FileReader( outputFilePath.toAbsolutePath().toString() );
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setSkipHeaderRecord( true ).build();
            Iterable< CSVRecord > records = csvFormat.parse( in );
            List< TableColumn > tableColumns = new ArrayList<>();
            CSVRecord header = records.iterator().next();
            List< String > headerList = header.stream().toList();
            CSVRecord sampleData = records.iterator().hasNext() ? records.iterator().next() : null;
            if ( sampleData == null ) {
                return null;
            }
            getTableColumnsFromCSVForFilters( headerList, tableColumns, sampleData );
            return tableColumns;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_WHILE_READING_OUTPUT_FILE.getKey(),
                    DashboardEnums.WidgetPythonOutputOptions.CSV.getName() ) );
        }
    }

    /**
     * Gets table columns from csv for filters.
     *
     * @param headerList
     *         the header list
     * @param tableColumns
     *         the table columns
     * @param sampleData
     *         the sample data
     */
    private static void getTableColumnsFromCSVForFilters( List< String > headerList, List< TableColumn > tableColumns,
            CSVRecord sampleData ) {
        for ( int columnIndex = 0; columnIndex < headerList.size(); columnIndex++ ) {
            String columnName = headerList.get( columnIndex );
            String sampleValue = sampleData.get( columnIndex );  // Get a sample value to infer type
            tableColumns.add( new TableColumn( columnName, columnName, columnName, inferCSVFilterType( sampleValue ) ) );
        }
    }

}
