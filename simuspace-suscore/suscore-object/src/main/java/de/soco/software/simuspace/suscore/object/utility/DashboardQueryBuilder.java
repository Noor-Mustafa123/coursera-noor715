package de.soco.software.simuspace.suscore.object.utility;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFilterOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants;
import de.soco.software.simuspace.suscore.common.enums.DashboardEnums;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryResponse;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.model.BarWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.CSVDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;
import de.soco.software.simuspace.suscore.data.model.DataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.DatabaseDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.JsonDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleXOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleXYOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetMultipleYOptions;
import de.soco.software.simuspace.suscore.data.model.LineWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.MixChartWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.RadarWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.ScatterWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.TableWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.TextWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.WidgetQueryBuilderSource;

/**
 * The type Dashboard query builder.
 */
public class DashboardQueryBuilder {

    /**
     * The constant SELECT_TEMPLATE.
     */
    private static final String SELECT_TEMPLATE = "select %s ";

    /**
     * The constant SELECT_VALUE_TEMPLATE.
     */
    private static final String SELECT_VALUE_TEMPLATE = " %s as %s ";

    /**
     * The constant FROM_TEMPLATE.
     */
    private static final String FROM_TEMPLATE = " from %s";

    /**
     * The constant ORDER_BY_TEMPLATE.
     */
    private static final String ORDER_BY_TEMPLATE = " ORDER BY %s ASC";

    /**
     * The constant WHERE_TEMPLATE.
     */
    private static final String WHERE_TEMPLATE = " where %s";

    /**
     * The constant GROUP_BY_TEMPLATE.
     */
    private static final String GROUP_BY_TEMPLATE = " group by %s ";

    /**
     * The constant GROUP_BY_VALUE_TEMPLATE.
     */
    private static final String GROUP_BY_VALUE_TEMPLATE = " %s ";

    /**
     * The constant AGGREGATE_TEMPLATE.
     */
    private static final String AGGREGATE_TEMPLATE = " %s(%s) ";

    /**
     * Instantiates a new Dashboard query builder.
     */
    private DashboardQueryBuilder() {

    }

    /**
     * Gets query for scatter widget.
     *
     * @param widgetDto
     *         the widget dto
     *
     * @return the query for scatter widget
     */
    public static String getQueryForScatterWidget( DashboardWidgetDTO widgetDto ) {
        WidgetQueryBuilderSource source = ( WidgetQueryBuilderSource ) widgetDto.getSource();
        ScatterWidgetOptions dto = ( ScatterWidgetOptions ) widgetDto.getOptions();
        if ( StringUtils.isBlank( dto.getX_axis() ) || StringUtils.isBlank( dto.getY_axis() ) || StringUtils.isBlank(
                source.getTable() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MISSING_REQUIRED_FIELD.getKey() ) );
        }
        String x_axis = getSqlValueFromDTOValue( dto.getX_axis() );
        String y_axis = getSqlValueFromDTOValue( dto.getY_axis() );
        String pointSize = getSqlValueFromDTOValue( dto.getPointSize() );
        String pointColor = getSqlValueFromDTOValue( dto.getPointColor() );
        String table = getSqlValueFromDTOValue( source.getTable() );
        return String.format( SELECT_TEMPLATE, getScatterSelectStatement( x_axis, y_axis, pointSize, pointColor ) ) + String.format(
                FROM_TEMPLATE, table ) + getWhereClauseIfNecessary( widgetDto ) + String.format( ORDER_BY_TEMPLATE, x_axis )
                + ConstantsString.SEMI_COLON;
    }

    /**
     * Gets query for text widget.
     *
     * @param widgetDto
     *         the widget dto
     *
     * @return the query for text widget
     */
    public static String getQueryForTextWidget( DashboardWidgetDTO widgetDto ) {
        WidgetQueryBuilderSource source = ( WidgetQueryBuilderSource ) widgetDto.getSource();
        TextWidgetOptions dto = ( TextWidgetOptions ) widgetDto.getOptions();
        if ( StringUtils.isBlank( dto.getColumn() ) || StringUtils.isBlank( source.getTable() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MISSING_REQUIRED_FIELD.getKey() ) );
        }
        String column = getSqlValueFromDTOValue( dto.getColumn() );
        String table = getSqlValueFromDTOValue( source.getTable() );
        return String.format( SELECT_TEMPLATE, getTextSelectStatement( column ) ) + String.format( FROM_TEMPLATE, table )
                + getWhereClauseIfNecessary( widgetDto ) + String.format( ORDER_BY_TEMPLATE, column ) + ConstantsString.SEMI_COLON;
    }

    /**
     * Gets where clause if necessary.
     *
     * @param dto
     *         the dto
     *
     * @return the where clause if necessary
     */
    private static String getWhereClauseIfNecessary( DashboardWidgetDTO dto ) {
        if ( CollectionUtils.isNotEmpty( dto.getColumns() ) ) {
            return prepareFilteredQuery( dto );
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Gets scatter select statement.
     *
     * @param xAxis
     *         x axis
     * @param yAxis
     *         y axis
     * @param pointSize
     *         the point size
     * @param pointColor
     *         the point color
     *
     * @return the scatter select statement
     */
    private static String getScatterSelectStatement( String xAxis, String yAxis, String pointSize, String pointColor ) {
        StringBuilder selectStatementBuilder = new StringBuilder();
        selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, xAxis, DataDashboardConstants.SQL_CONSTANTS.X_AXIS_ALIAS ) );
        selectStatementBuilder.append( ConstantsString.COMMA );
        selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, yAxis, DataDashboardConstants.SQL_CONSTANTS.Y_AXIS_ALIAS ) );
        if ( StringUtils.isNotBlank( pointSize ) ) {
            selectStatementBuilder.append( ConstantsString.COMMA );
            selectStatementBuilder.append(
                    String.format( SELECT_VALUE_TEMPLATE, pointSize, DataDashboardConstants.SQL_CONSTANTS.POINT_SIZE_ALIAS ) );
        }
        if ( StringUtils.isNotBlank( pointColor ) ) {
            selectStatementBuilder.append( ConstantsString.COMMA );
            selectStatementBuilder.append(
                    String.format( SELECT_VALUE_TEMPLATE, pointColor, DataDashboardConstants.SQL_CONSTANTS.POINT_COLOR_ALIAS ) );
        }
        return selectStatementBuilder.toString();
    }

    /**
     * Gets text select statement.
     *
     * @param column
     *         the column
     *
     * @return the text select statement
     */
    private static String getTextSelectStatement( String column ) {
        return String.format( SELECT_VALUE_TEMPLATE, column, DataDashboardConstants.TEXT_WIDGET_FIELDS.COLUMN );
    }

    /**
     * Gets line select statement.
     *
     * @param xAxis
     *         the x axis
     * @param yAxis
     *         the y axis
     *
     * @return the line select statement
     */
    private static String getLineSelectStatement( String xAxis, List< String > yAxis ) {
        StringBuilder selectStatementBuilder = new StringBuilder();
        selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, xAxis, DataDashboardConstants.SQL_CONSTANTS.X_AXIS_ALIAS ) );
        for ( int i = 0; i < yAxis.size(); i++ ) {
            String yaxisValue = yAxis.get( i );
            selectStatementBuilder.append( ConstantsString.COMMA );
            selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, yaxisValue,
                    DataDashboardConstants.SQL_CONSTANTS.Y_AXIS_ALIAS + ConstantsString.UNDERSCORE + ( i + 1 ) ) );
        }
        return selectStatementBuilder.toString();
    }

    /**
     * Gets line select statement.
     *
     * @param xAxis
     *         the x axis
     * @param yAxis
     *         the y axis
     *
     * @return the line select statement
     */
    private static String getLineSelectStatement( List< String > xAxis, List< String > yAxis ) {
        StringBuilder selectStatementBuilder = new StringBuilder();
        for ( int i = 0; i < xAxis.size(); i++ ) {
            String xaxisValue = xAxis.get( i );
            selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, xaxisValue,
                    DataDashboardConstants.SQL_CONSTANTS.X_AXIS_ALIAS + ConstantsString.UNDERSCORE + ( i + 1 ) ) );
            selectStatementBuilder.append( ConstantsString.COMMA );
        }
        for ( int i = 0; i < yAxis.size(); i++ ) {
            String yaxisValue = yAxis.get( i );
            selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, yaxisValue,
                    DataDashboardConstants.SQL_CONSTANTS.Y_AXIS_ALIAS + ConstantsString.UNDERSCORE + ( i + 1 ) ) );
            if ( i < yAxis.size() - 1 ) {
                selectStatementBuilder.append( ConstantsString.COMMA );
            }
        }
        return selectStatementBuilder.toString();
    }

    private static String getLineSelectStatementForMultiSelectColumn( List< String > columns ) {
        StringBuilder selectStatementBuilder = new StringBuilder();
        for ( int i = 0; i < columns.size(); i++ ) {
            String columnVal = columns.get( i );
            String[] splitColumnVal = columnVal.split( ConstantsString.DOT_REGEX );
            selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, columnVal, splitColumnVal[ splitColumnVal.length - 1 ] ) );
            if ( i < columns.size() - 1 ) {
                selectStatementBuilder.append( ConstantsString.COMMA );
            }
        }
        return selectStatementBuilder.toString();
    }

    /**
     * Gets line select statement.
     *
     * @param xAxis
     *         the x axis
     * @param yAxis
     *         the y axis
     *
     * @return the line select statement
     */
    private static String getLineSelectStatement( List< String > xAxis, String yAxis ) {
        StringBuilder selectStatementBuilder = new StringBuilder();
        for ( int i = 0; i < xAxis.size(); i++ ) {
            String xaxisValue = xAxis.get( i );
            selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, xaxisValue,
                    DataDashboardConstants.SQL_CONSTANTS.X_AXIS_ALIAS + ConstantsString.UNDERSCORE + ( i + 1 ) ) );
            selectStatementBuilder.append( ConstantsString.COMMA );
        }
        selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, yAxis, DataDashboardConstants.SQL_CONSTANTS.Y_AXIS_ALIAS ) );
        return selectStatementBuilder.toString();
    }

    /**
     * Gets radar select statement.
     *
     * @param values
     *         the values
     * @param indicator
     *         the indicator
     * @param min
     *         the min
     * @param max
     *         the max
     *
     * @return the radar select statement
     */
    private static String getRadarSelectStatement( List< String > values, String indicator, String min, String max ) {
        StringBuilder selectStatementBuilder = new StringBuilder();
        for ( int i = 0; i < values.size(); i++ ) {
            String value = values.get( i );
            selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, value,
                    DataDashboardConstants.RADAR_WIDGET_FIELDS.VALUE + ConstantsString.UNDERSCORE + ( i + 1 ) ) );
            selectStatementBuilder.append( ConstantsString.COMMA );
        }
        selectStatementBuilder.append(
                String.format( SELECT_VALUE_TEMPLATE, indicator, DataDashboardConstants.RADAR_WIDGET_FIELDS.INDICATOR ) );

        if ( StringUtils.isNotBlank( max ) ) {
            selectStatementBuilder.append( ConstantsString.COMMA );
            selectStatementBuilder.append(
                    String.format( SELECT_VALUE_TEMPLATE, indicator, DataDashboardConstants.RADAR_WIDGET_FIELDS.MAX ) );
        }
        if ( StringUtils.isNotBlank( min ) ) {
            selectStatementBuilder.append( ConstantsString.COMMA );
            selectStatementBuilder.append(
                    String.format( SELECT_VALUE_TEMPLATE, indicator, DataDashboardConstants.RADAR_WIDGET_FIELDS.MIN ) );
        }
        return selectStatementBuilder.toString();
    }

    /**
     * Gets multiple order statement for x axis.
     *
     * @param xAxis
     *         the x axis
     *
     * @return the multiple order statement for x axis
     */
    private static String getMultipleOrderStatementForXAxis( List< String > xAxis ) {
        StringBuilder orderStatementBuilder = new StringBuilder();
        for ( int i = 0; i < xAxis.size(); i++ ) {
            String xaxisValue = xAxis.get( i );
            if ( i > 0 ) {
                orderStatementBuilder.append( ConstantsString.COMMA );
            }
            orderStatementBuilder.append( xaxisValue );
        }
        return orderStatementBuilder.toString();
    }

    /**
     * Gets query for bar widget.
     *
     * @param dto
     *         the dto
     *
     * @return the query for bar widget
     */
    public static String getQueryForBarWidget( DashboardWidgetDTO dto ) {
        WidgetQueryBuilderSource source = ( WidgetQueryBuilderSource ) dto.getSource();
        BarWidgetOptions widgetDTO = ( BarWidgetOptions ) dto.getOptions();
        if ( StringUtils.isBlank( widgetDTO.getX_axis() ) || StringUtils.isBlank( widgetDTO.getY_axis() ) || StringUtils.isBlank(
                source.getTable() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MISSING_REQUIRED_FIELD.getKey() ) );
        }
        String x_axis = getSqlValueFromDTOValue( widgetDTO.getX_axis() );
        String y_axis = getSqlValueFromDTOValue( widgetDTO.getY_axis() );
        String barColor = getSqlValueFromDTOValue( widgetDTO.getBarColor() );
        String colorAggregate = ( widgetDTO.getColorAggregate() );
        String table = getSqlValueFromDTOValue( source.getTable() );
        return String.format( SELECT_TEMPLATE, getBarSelectStatement( x_axis, y_axis, barColor, colorAggregate ) ) + String.format(
                FROM_TEMPLATE, table ) + getWhereClauseIfNecessary( dto ) + getGroupByForBar( x_axis, y_axis, barColor, colorAggregate )
                + String.format( ORDER_BY_TEMPLATE, x_axis ) + ConstantsString.SEMI_COLON;
    }

    /**
     * Gets group by for bar.
     *
     * @param xAxis
     *         the x axis
     * @param yAxis
     *         the y axis
     * @param barColor
     *         the bar color
     * @param colorAggregate
     *         the color aggregate
     *
     * @return the group by for bar
     */
    private static String getGroupByForBar( String xAxis, String yAxis, String barColor, String colorAggregate ) {
        if ( StringUtils.isNotBlank( barColor ) && StringUtils.isNotBlank( colorAggregate ) ) {
            return String.format( GROUP_BY_TEMPLATE,
                    String.format( GROUP_BY_VALUE_TEMPLATE, xAxis ) + ConstantsString.COMMA + String.format( GROUP_BY_VALUE_TEMPLATE,
                            yAxis ) );
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Gets bar select statement.
     *
     * @param xAxis
     *         x axis
     * @param yAxis
     *         y axis
     * @param barColor
     *         the bar color
     * @param colorAggregate
     *         the color aggregate
     *
     * @return the bar select statement
     */
    private static String getBarSelectStatement( String xAxis, String yAxis, String barColor, String colorAggregate ) {
        StringBuilder selectStatementBuilder = new StringBuilder();
        selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, xAxis, DataDashboardConstants.SQL_CONSTANTS.X_AXIS_ALIAS ) );
        selectStatementBuilder.append( ConstantsString.COMMA );
        selectStatementBuilder.append( String.format( SELECT_VALUE_TEMPLATE, yAxis, DataDashboardConstants.SQL_CONSTANTS.Y_AXIS_ALIAS ) );
        if ( StringUtils.isNotBlank( barColor ) ) {
            selectStatementBuilder.append( ConstantsString.COMMA );
            if ( StringUtils.isNotBlank( colorAggregate ) ) {
                selectStatementBuilder.append(
                        String.format( SELECT_VALUE_TEMPLATE, String.format( AGGREGATE_TEMPLATE, colorAggregate, barColor ),
                                DataDashboardConstants.SQL_CONSTANTS.BAR_COLOR_ALIAS ) );
            } else {
                selectStatementBuilder.append(
                        String.format( SELECT_VALUE_TEMPLATE, barColor, DataDashboardConstants.SQL_CONSTANTS.BAR_COLOR_ALIAS ) );
            }

        }
        return selectStatementBuilder.toString();
    }

    /**
     * Prepare filtered query string.
     *
     * @param barWidgetDTO
     *         the barWidgetDTO
     *
     * @return the string
     */
    private static String prepareFilteredQuery( DashboardWidgetDTO barWidgetDTO ) {
        StringBuilder andFilters = new StringBuilder();
        StringBuilder orFilters = new StringBuilder();
        barWidgetDTO.getColumns().stream().filter( column -> !column.getFilters().isEmpty() )
                .forEach( column -> column.getFilters().forEach( filter -> {
                    String filterQuery = addFilter( filter, column.getName() );
                    if ( ConstantsOperators.OR.getOperatorTemplate().equals( filter.getCondition() ) ) {
                        appendFilter( orFilters, filterQuery, " OR " );
                    } else {
                        appendFilter( andFilters, filterQuery, " AND " );
                    }
                } ) );
        String finalQuery = buildFinalQuery( andFilters, orFilters );
        return ConstantsString.EMPTY_STRING.equals( finalQuery ) ? ConstantsString.EMPTY_STRING
                : String.format( WHERE_TEMPLATE, finalQuery );
    }

    /**
     * Build final query string.
     *
     * @param andFilters
     *         and filters
     * @param orFilters
     *         or filters
     *
     * @return the string
     */
    private static String buildFinalQuery( StringBuilder andFilters, StringBuilder orFilters ) {
        if ( andFilters.isEmpty() && orFilters.isEmpty() ) {
            return ConstantsString.EMPTY_STRING;  // If both filters are empty
        } else if ( andFilters.isEmpty() ) {
            return " (" + orFilters + ")";
        } else if ( orFilters.isEmpty() ) {
            return " (" + andFilters + ")";
        } else {
            return " (" + andFilters + ") AND (" + orFilters + ")";
        }
    }

    /**
     * Append filter.
     *
     * @param builder
     *         the builder
     * @param filterQuery
     *         the filter query
     * @param condition
     *         the condition
     */
    private static void appendFilter( StringBuilder builder, String filterQuery, String condition ) {
        if ( !builder.isEmpty() ) {
            builder.append( condition );
        }
        builder.append( filterQuery );
    }

    /**
     * Add filter string.
     *
     * @param filter
     *         the filter
     * @param name
     *         the name
     *
     * @return the string
     */
    private static String addFilter( Filter filter, String name ) {
        if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            return name + ConstantsOperators.EQUALS.getOperatorTemplate().replace( ConstantsString.DOLLAR, filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            return name + ConstantsOperators.NOT_EQUALS.getOperatorTemplate().replace( ConstantsString.DOLLAR, filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            return name + ConstantsString.SPACE + ConstantsOperators.IS_IN.getOperatorTemplate()
                    .replace( ConstantsString.DOLLAR, filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            return name + ConstantsString.SPACE + ConstantsOperators.IS_NOT_IN.getOperatorTemplate()
                    .replace( ConstantsString.DOLLAR, filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            return name + ConstantsString.SPACE + ConstantsOperators.BEGINS_WITH.getOperatorTemplate()
                    .replace( ConstantsString.DOLLAR, filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            return name + ConstantsString.SPACE + ConstantsOperators.ENDS_WITH.getOperatorTemplate()
                    .replace( ConstantsString.DOLLAR, filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            return name + ConstantsOperators.AFTER.getOperatorTemplate().replace( ConstantsString.DOLLAR, filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            return name + ConstantsOperators.AFTER_OR_EQUAL_TO.getOperatorTemplate().replace( ConstantsString.DOLLAR, filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            return name + ConstantsOperators.BEFORE.getOperatorTemplate().replace( ConstantsString.DOLLAR, filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            return name + ConstantsOperators.BEFORE_OR_EQUAL_TO.getOperatorTemplate().replace( ConstantsString.DOLLAR, filter.getValue() );
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Gets sql value from dto value.
     *
     * @param dtoValue
     *         the dto value
     *
     * @return the sql value from dto value
     */
    private static String getSqlValueFromDTOValue( String dtoValue ) {
        if ( StringUtils.isNotBlank( dtoValue ) ) {
            return dtoValue.substring( dtoValue.indexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE );
        }
        return null;
    }

    /**
     * Gets query for line widget.
     *
     * @param widgetDTO
     *         the widget dto
     *
     * @return the query for line widget
     */
    public static String getQueryForLineWidget( DashboardWidgetDTO widgetDTO ) {
        WidgetQueryBuilderSource source = ( WidgetQueryBuilderSource ) widgetDTO.getSource();
        LineWidgetOptions dto = ( LineWidgetOptions ) widgetDTO.getOptions();
        return switch ( DashboardEnums.LINE_WIDGET_OPTIONS.getById( dto.getLineWidgetType() ) ) {
            case MULTIPLE_X_SINGLE_Y -> getSqlForLineWidgetMultipleXAxis( widgetDTO, ( LineWidgetMultipleXOptions ) dto, source );
            case MULTIPLE_Y_SINGLE_X -> getSqlForLineWidgetMultipleYAxis( widgetDTO, ( LineWidgetMultipleYOptions ) dto, source );
            case MULTIPLE_X_MULTIPLE_Y -> getSqlForLineWidgetMultipleXAndYAxis( widgetDTO, ( LineWidgetMultipleXYOptions ) dto, source );
        };
    }

    /**
     * Gets query for multi chart widget.
     *
     * @param widgetDTO
     *         the widget dto
     *
     * @return the query for multi chart widget
     */
    public static String getQueryForMultiChartWidget( DashboardWidgetDTO widgetDTO ) {
        WidgetQueryBuilderSource source = ( WidgetQueryBuilderSource ) widgetDTO.getSource();
        MixChartWidgetOptions dto = ( MixChartWidgetOptions ) widgetDTO.getOptions();
        if ( CollectionUtils.isEmpty( dto.getCurveOptions() ) || StringUtils.isBlank( source.getTable() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MISSING_REQUIRED_FIELD.getKey() ) );
        }
        List< String > x_axis = getMultiSelectSqlValueFromDTO( dto.getX_axis() );
        List< String > y_axis = getMultiSelectSqlValueFromDTO( dto.getY_axis() );
        String table = getSqlValueFromDTOValue( source.getTable() );
        return String.format( SELECT_TEMPLATE, getLineSelectStatement( x_axis, y_axis ) ) + String.format( FROM_TEMPLATE, table )
                + getWhereClauseIfNecessary( widgetDTO ) + String.format( ORDER_BY_TEMPLATE, getMultipleOrderStatementForXAxis( x_axis ) )
                + ConstantsString.SEMI_COLON;
    }

    /**
     * Gets query for table widget.
     *
     * @param widgetDTO
     *         the widget dto
     *
     * @return the query for table widget
     */
    public static String getQueryForTableWidget( DashboardWidgetDTO widgetDTO ) {
        WidgetQueryBuilderSource source = ( WidgetQueryBuilderSource ) widgetDTO.getSource();
        TableWidgetOptions dto = ( TableWidgetOptions ) widgetDTO.getOptions();
        if ( StringUtils.isBlank( source.getTable() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MISSING_REQUIRED_FIELD.getKey() ) );
        }
        List< String > columns = getMultiSelectSqlValueFromDTO( dto.getTableColumns() );
        String table = getSqlValueFromDTOValue( source.getTable() );
        return String.format( SELECT_TEMPLATE, getLineSelectStatementForMultiSelectColumn( columns ) ) + String.format( FROM_TEMPLATE,
                table ) + getWhereClauseIfNecessary( widgetDTO ) + String.format( ORDER_BY_TEMPLATE,
                getMultipleOrderStatementForXAxis( columns ) ) + ConstantsString.SEMI_COLON;
    }

    /**
     * Gets sql for line widget multiple x and y axis.
     *
     * @param widgetDTO
     *         the widget dto
     * @param dto
     *         the dto
     * @param source
     *         the source
     *
     * @return the sql for line widget multiple x and y axis
     */
    private static String getSqlForLineWidgetMultipleXAndYAxis( DashboardWidgetDTO widgetDTO, LineWidgetMultipleXYOptions dto,
            WidgetQueryBuilderSource source ) {
        if ( CollectionUtils.isEmpty( dto.getX_axis() ) || CollectionUtils.isEmpty( dto.getY_axis() ) || StringUtils.isBlank(
                source.getTable() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MISSING_REQUIRED_FIELD.getKey() ) );
        }
        List< String > x_axis = getMultiSelectSqlValueFromDTO( dto.getX_axis() );
        List< String > y_axis = getMultiSelectSqlValueFromDTO( dto.getY_axis() );
        String table = getSqlValueFromDTOValue( source.getTable() );
        return String.format( SELECT_TEMPLATE, getLineSelectStatement( x_axis, y_axis ) ) + String.format( FROM_TEMPLATE, table )
                + getWhereClauseIfNecessary( widgetDTO ) + String.format( ORDER_BY_TEMPLATE, getMultipleOrderStatementForXAxis( x_axis ) )
                + ConstantsString.SEMI_COLON;
    }

    /**
     * Gets sql for line widget multiple x axis.
     *
     * @param widgetDTO
     *         the widget dto
     * @param dto
     *         the dto
     * @param source
     *         the source
     *
     * @return the sql for line widget multiple x axis
     */
    private static String getSqlForLineWidgetMultipleXAxis( DashboardWidgetDTO widgetDTO, LineWidgetMultipleXOptions dto,
            WidgetQueryBuilderSource source ) {

        if ( CollectionUtils.isEmpty( dto.getX_axis() ) || StringUtils.isBlank( dto.getY_axis() ) || StringUtils.isBlank(
                source.getTable() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MISSING_REQUIRED_FIELD.getKey() ) );
        }
        List< String > x_axis = getMultiSelectSqlValueFromDTO( dto.getX_axis() );
        String y_axis = getSqlValueFromDTOValue( dto.getY_axis() );
        String table = getSqlValueFromDTOValue( source.getTable() );
        return String.format( SELECT_TEMPLATE, getLineSelectStatement( x_axis, y_axis ) ) + String.format( FROM_TEMPLATE, table )
                + getWhereClauseIfNecessary( widgetDTO ) + String.format( ORDER_BY_TEMPLATE, getMultipleOrderStatementForXAxis( x_axis ) )
                + ConstantsString.SEMI_COLON;
    }

    /**
     * Gets sql for line widget multiple y axis.
     *
     * @param widgetDTO
     *         the widget dto
     * @param dto
     *         the dto
     * @param source
     *         the source
     *
     * @return the sql for line widget multiple y axis
     */
    private static String getSqlForLineWidgetMultipleYAxis( DashboardWidgetDTO widgetDTO, LineWidgetMultipleYOptions dto,
            WidgetQueryBuilderSource source ) {
        if ( StringUtils.isBlank( dto.getX_axis() ) || CollectionUtils.isEmpty( dto.getY_axis() ) || StringUtils.isBlank(
                source.getTable() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MISSING_REQUIRED_FIELD.getKey() ) );
        }
        String x_axis = getSqlValueFromDTOValue( dto.getX_axis() );
        List< String > y_axis = getMultiSelectSqlValueFromDTO( dto.getY_axis() );
        String table = getSqlValueFromDTOValue( source.getTable() );
        return String.format( SELECT_TEMPLATE, getLineSelectStatement( x_axis, y_axis ) ) + String.format( FROM_TEMPLATE, table )
                + getWhereClauseIfNecessary( widgetDTO ) + String.format( ORDER_BY_TEMPLATE, x_axis ) + ConstantsString.SEMI_COLON;
    }

    /**
     * Gets multi select sql value from dto.
     *
     * @param dtoValues
     *         the dto values
     *
     * @return the multi select sql value from dto
     */
    private static List< String > getMultiSelectSqlValueFromDTO( List< String > dtoValues ) {
        List< String > sqlValues = new ArrayList<>();
        for ( String dtoValue : dtoValues ) {
            if ( StringUtils.isNotBlank( dtoValue ) ) {
                sqlValues.add( dtoValue.substring( dtoValue.indexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
            }
        }
        return sqlValues;
    }

    /**
     * Gets query for radar widget.
     *
     * @param widgetDTO
     *         the widget dto
     *
     * @return the query for radar widget
     */
    public static String getQueryForRadarWidget( DashboardWidgetDTO widgetDTO ) {
        WidgetQueryBuilderSource source = ( WidgetQueryBuilderSource ) widgetDTO.getSource();
        RadarWidgetOptions dto = ( RadarWidgetOptions ) widgetDTO.getOptions();
        if ( CollectionUtils.isEmpty( dto.getValue() ) || StringUtils.isBlank( source.getTable() ) || StringUtils.isBlank(
                dto.getIndicator() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MISSING_REQUIRED_FIELD.getKey() ) );
        }
        List< String > values = getMultiSelectSqlValueFromDTO( dto.getValue() );
        String indicator = getSqlValueFromDTOValue( dto.getIndicator() );
        String min = getSqlValueFromDTOValue( dto.getMin() );
        String max = getSqlValueFromDTOValue( dto.getMax() );
        String table = getSqlValueFromDTOValue( source.getTable() );
        return String.format( SELECT_TEMPLATE, getRadarSelectStatement( values, indicator, min, max ) ) + String.format( FROM_TEMPLATE,
                table ) + getWhereClauseIfNecessary( widgetDTO ) + String.format( ORDER_BY_TEMPLATE,
                getMultipleOrderStatementForXAxis( values ) ) + ConstantsString.SEMI_COLON;

    }

    public static List< Map< String, Object > > getFilteredRecordsForTableWidget( String tableName, FiltersDTO filtersDTO,
            DataSourceDTO dataSourceDTO, DashboardWidgetDTO dashboardWidgetDTO ) {
        return switch ( DashboardEnums.DashboardDataSourceOptions.getById( dataSourceDTO.getSourceType() ) ) {
            case DATABASE -> getAllFilteredRecordsFromExternalDB( tableName, filtersDTO, dataSourceDTO,
                    ( ( TableWidgetOptions ) dashboardWidgetDTO.getOptions() ).getTableColumns() );
            case CSV -> DataDashboardUtil.convertToListOfMaps(
                    DashboardCSVUtil.getPaginatedDataFromCsvFileForWidget( Path.of( ( ( CSVDataSourceDTO ) dataSourceDTO ).getFilePath() ),
                            dashboardWidgetDTO, filtersDTO ).getData() );
            case JSON -> DataDashboardUtil.convertToListOfMaps( DashboardJsonUtil.getPaginatedDataFromJsonFileForWidget(
                    Path.of( ( ( JsonDataSourceDTO ) dataSourceDTO ).getFilePath() ), dashboardWidgetDTO, filtersDTO ).getData() );
            case EXCEL, SUS_OBJECT -> null;
        };
    }

    /// //EXTERNAL DATABASE QUERY BUILDING

    public static List< Map< String, Object > > getAllFilteredRecordsFromExternalDB( String tableName, FiltersDTO filtersDTO,
            DataSourceDTO dataSourceDTO, List< String > columns ) {
        JdbcTemplate jdbcTemplate = DataDashboardUtil.getJdbcTemplate( ( DatabaseDataSourceDTO ) dataSourceDTO );
        StringBuilder baseWhereClause = new StringBuilder( " WHERE 1=1" );
        List< Object > whereParams = new ArrayList<>();

        // Add dynamic filters
        addWhereClause( baseWhereClause, whereParams, filtersDTO, "t" );

        // Count total records (no filters)
        long totalRecords = jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM " + tableName, Integer.class );

        // Count filtered records (with filters)
        long filteredRecords = jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM " + tableName + " t" + baseWhereClause.toString(),
                whereParams.toArray(), Integer.class );
        String selectClause = "SELECT ";
        if ( null != columns && CollectionUtils.isNotEmpty( columns ) ) {
            for ( int i = 0; i < columns.size(); i++ ) {
                selectClause = selectClause + columns.get( i ).split( ConstantsString.DOT_REGEX )[ ConstantsInteger.INTEGER_VALUE_THREE ];
                if ( i == columns.size() - ConstantsInteger.INTEGER_VALUE_ONE ) {
                    continue;
                }
                selectClause = selectClause + ConstantsString.COMMA;
            }
        } else {
            selectClause = selectClause + " *";
        }

        // Build full query with filters, sorting, pagination
        StringBuilder finalQuery = new StringBuilder( selectClause ).append( " FROM " ).append( tableName ).append( " t" )
                .append( baseWhereClause );
        finalQuery.append( buildOrderByClause( filtersDTO ) ).append( " LIMIT ? OFFSET ?" );

        // Add pagination parameters
        whereParams.add( filtersDTO.getLength() );
        whereParams.add( filtersDTO.getStart() );

        List< Map< String, Object > > results = jdbcTemplate.queryForList( finalQuery.toString(), whereParams.toArray() );

        // Set counts in DTO
        filtersDTO.setTotalRecords( totalRecords );
        filtersDTO.setFilteredRecords( filteredRecords );

        return results;
    }

    public static DynamicQueryResponse getDataForTableWidget( DataSourceDTO dataSourceDTO, DashboardWidgetDTO dto ) {

        return switch ( DashboardEnums.DashboardDataSourceOptions.getById( dataSourceDTO.getSourceType() ) ) {
            case DATABASE -> runQueryForTableWidget( dataSourceDTO, dto );
            case CSV ->
                    DashboardCSVUtil.getDataFromCsvFileForWidget( Path.of( ( ( CSVDataSourceDTO ) dataSourceDTO ).getFilePath() ), dto );
            case JSON ->
                    DashboardJsonUtil.getDataFromJsonFileForWidget( Path.of( ( ( JsonDataSourceDTO ) dataSourceDTO ).getFilePath() ), dto );
            case EXCEL, SUS_OBJECT -> null;
        };
    }

    /**
     * Run query for table widget dynamic query response.
     *
     * @param dataSourceDTO
     *         the data source dto
     * @param dto
     *         the dto
     *
     * @return the dynamic query response
     */
    public static DynamicQueryResponse runQueryForTableWidget( DataSourceDTO dataSourceDTO, DashboardWidgetDTO dto ) {
        String query = DashboardQueryBuilder.getQueryForTableWidget( dto );
        JdbcTemplate jdbcTemplate = DataDashboardUtil.getJdbcTemplate( ( DatabaseDataSourceDTO ) dataSourceDTO );
        try {
            return DataDashboardUtil.fetchDataFromQuery( jdbcTemplate, query, dto );
        } finally {
            DataDashboardUtil.closeDataSource( jdbcTemplate );
        }
    }

    private static void addWhereClause( StringBuilder sql, List< Object > params, FiltersDTO filtersDTO, String tableAlias ) {
        List< String > andConditions = new ArrayList<>();
        List< String > orConditions = new ArrayList<>();

        if ( filtersDTO.getColumns() != null ) {
            for ( FilterColumn column : filtersDTO.getColumns() ) {
                List< Filter > filters = column.getFilters();
                if ( filters != null && !filters.isEmpty() ) {
                    for ( Filter filter : filters ) {
                        String operator = mapOperator( filter.getOperator() );
                        if ( operator == null ) {
                            continue;
                        }

                        String condition = tableAlias + "." + column.getName() + " " + operator + " ?";
                        Object value = prepareValue( filter.getOperator(), filter.getValue() );
                        params.add( value );

                        if ( ConstantsString.OR_CONDITION.equalsIgnoreCase( filter.getCondition() ) ) {
                            orConditions.add( condition );
                        } else {
                            andConditions.add( condition ); // Default to AND if not specified
                        }
                    }
                }
            }
        }

        // Apply OR group
        if ( !orConditions.isEmpty() ) {
            sql.append( " AND (" ).append( String.join( " OR ", orConditions ) ).append( ")" );
        }

        // Apply AND group
        for ( String andCondition : andConditions ) {
            sql.append( " AND " ).append( andCondition );
        }
    }

    private static String mapOperator( String operator ) {
        if ( operator == null ) {
            return null;
        }
        switch ( operator ) {
            case ConstantsFilterOperators.EQUALS:
                return "=";
            case ConstantsFilterOperators.NOT_EQUALS:
                return "<>";
            case ConstantsFilterOperators.LESS_THAN:
                return "<";
            case ConstantsFilterOperators.LESS_THAN_OR_EQUAL_TO:
                return "<=";
            case ConstantsFilterOperators.GREATER_THAN:
                return ">";
            case ConstantsFilterOperators.GREATER_THAN_OR_EQUAL_TO:
                return ">=";
            case ConstantsFilterOperators.CONTAINS, ConstantsFilterOperators.BEGINS_WITH, ConstantsFilterOperators.ENDS_WITH:
                return "LIKE";
            case ConstantsFilterOperators.NOT_CONTAINS:
                return "NOT LIKE"; // Handle IN separately if needed
            default:
                return null;
        }
    }

    private static Object prepareValue( String operator, String rawValue ) {
        if ( operator == null || rawValue == null ) {
            return rawValue;
        }

        switch ( operator ) {
            case ConstantsFilterOperators.CONTAINS, ConstantsFilterOperators.NOT_CONTAINS:
                return "%" + rawValue + "%";
            case ConstantsFilterOperators.BEGINS_WITH:
                return rawValue + "%";
            case ConstantsFilterOperators.ENDS_WITH:
                return "%" + rawValue;
            default:
                return rawValue;
        }
    }

    private static String buildOrderByClause( FiltersDTO filtersDTO ) {
        StringBuilder orderBy = new StringBuilder();
        boolean hasSorting = false;

        if ( filtersDTO.getColumns() != null ) {
            for ( FilterColumn column : filtersDTO.getColumns() ) {
                if ( ValidationUtils.isNotNullOrEmpty( column.getDir() ) ) {
                    orderBy.append( column.getName() ).append( " " ).append( column.getDir() ).append( ", " );
                    hasSorting = true;
                }
            }
        }

        // If no sorting was provided, default to the first column's name (if available)
        if ( !hasSorting && filtersDTO.getColumns() != null && !filtersDTO.getColumns().isEmpty() ) {
            FilterColumn firstColumn = filtersDTO.getColumns().get( 0 );
            orderBy.append( firstColumn.getName() ).append( " ASC, " );
        }

        if ( orderBy.length() > 0 ) {
            orderBy.setLength( orderBy.length() - 2 ); // Remove last comma
            return " ORDER BY " + orderBy;
        }

        return "";
    }

}
