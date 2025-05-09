package de.soco.software.simuspace.suscore.data.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFilterOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.data.model.SsfeSelectionDTO;

/**
 * Generic class for filtering and sorting table entries.
 */
@Log4j2
public class FilterHelper {

    private FilterHelper() {

    }

    private static final String MODIFIERS_STRING = "modifiers";

    private static final String NEW_STATIC_FIELD_VALUE = "newValue";

    /**
     * Filters and sorts table entries based on provided filters.
     *
     * @param filter
     *         The filtering criteria.
     * @param table
     *         The list of table entries.
     *
     * @return Filtered and sorted list of entries.
     */
    public static < T > List< T > getFilteredTableEntries( FiltersDTO filter, List< T > table ) {

        if ( CollectionUtils.isEmpty( table ) ) {
            return new ArrayList<>();
        }

        filter.setTotalRecords( ( long ) table.size() );

        if ( StringUtils.isNotEmpty( filter.getSearch() ) ) {
            table = applySearchToTable( filter.getSearch(), table );
        } else {
            for ( int level = 0; level < filter.getColumns().size(); level++ ) {
                table = applyFiltersToTable( filter.getColumns(), table );
            }
        }

        filter.setFilteredRecords( ( long ) table.size() );
        sortEntriesList( filter, table );
        return table;
    }

    /**
     * Applies search criteria to filter table entries.
     *
     * @param search
     *         The search term.
     * @param table
     *         The list of table entries.
     *
     * @return The filtered list based on search.
     */
    private static < T > List< T > applySearchToTable( String search, List< T > table ) {
        String fieldName;
        if ( CollectionUtils.isEmpty( table ) ) {
            fieldName = null;
            return table;
        } else {
            fieldName = getSearchFieldNameFromClass( table );
        }
        return table.stream().filter( entry -> matchesSearch( entry, search, fieldName ) ).collect( Collectors.toList() );
    }

    private static < T > String getSearchFieldNameFromClass( List< T > table ) {
        if ( SsfeSelectionDTO.class.isAssignableFrom( table.get( 0 ).getClass() ) ) {
            return "title";
        }
        throw new SusException(
                MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), table.get( 0 ).getClass().getSimpleName() ) );
    }

    private static < T > boolean matchesSearch( T entry, String search, String fieldName ) {
        try {
            Field field = entry.getClass().getDeclaredField( fieldName );
            field.setAccessible( true );
            Object value = field.get( entry );

            if ( value instanceof String ) {
                return StringUtils.containsIgnoreCase( ( String ) value, search );
            }
        } catch ( NoSuchFieldException | IllegalAccessException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), entry.getClass().getSimpleName() ), e );
        }
        return false;
    }

    /**
     * Applies column-based filters.
     *
     * @param filterColumns
     *         The filter conditions.
     * @param table
     *         The list of table entries.
     *
     * @return The filtered list based on conditions.
     */
    private static < T > List< T > applyFiltersToTable( List< FilterColumn > filterColumns, List< T > table ) {
        return table.stream().filter( entry -> CollectionUtils.isEmpty( filterColumns ) || applyFiltersOnEntry( filterColumns, entry ) )
                .collect( Collectors.toList() );
    }

    /**
     * Applies AND/OR filters on a single table entry.
     *
     * @param filterColumns
     *         The filter conditions.
     * @param entry
     *         The entry to check.
     *
     * @return True if entry satisfies the filters, otherwise false.
     */
    private static < T > boolean applyFiltersOnEntry( List< FilterColumn > filterColumns, T entry ) {
        if ( CollectionUtils.isEmpty( filterColumns ) ) {
            return true;
        }
        boolean b = applyANDFiltersOnEntry( filterColumns, entry ) && applyORFiltersOnEntry( filterColumns, entry );
        return b;
    }

    private static < T > boolean applyANDFiltersOnEntry( List< FilterColumn > filterColumns, T entry ) {
        AtomicBoolean isFirstFilter = new AtomicBoolean( true );
        AtomicBoolean filterResult = new AtomicBoolean( true );
        filterColumns.stream().filter( fc -> CollectionUtils.isNotEmpty( fc.getFilters() ) ).forEach( fc -> {
            for ( Filter filter : fc.getFilters() ) {
                try {
                    if ( filter.getCondition().equals( "AND" ) ) {
                        if ( isFirstFilter.get() ) {
                            isFirstFilter.set( false );
                            filterResult.set( applySingleFilterOnEntry( filter, entry, fc.getName() ) );
                        } else {
                            filterResult.set( filterResult.get() && applySingleFilterOnEntry( filter, entry, fc.getName() ) );
                        }
                    }
                } catch ( NoSuchFieldException | IllegalAccessException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e.getMessage(), e );
                }
            }
        } );
        return filterResult.get();
    }

    private static < T > boolean applyORFiltersOnEntry( List< FilterColumn > filterColumns, T entry ) {
        AtomicBoolean isFirstFilter = new AtomicBoolean( true );
        AtomicBoolean filterResult = new AtomicBoolean( true );
        filterColumns.stream().filter( fc -> CollectionUtils.isNotEmpty( fc.getFilters() ) ).forEach( fc -> {
            for ( Filter filter : fc.getFilters() ) {
                try {
                    if ( filter.getCondition().equals( "OR" ) ) {
                        if ( isFirstFilter.get() ) {
                            isFirstFilter.set( false );
                            filterResult.set( applySingleFilterOnEntry( filter, entry, fc.getName() ) );
                        } else {
                            filterResult.set( filterResult.get() || applySingleFilterOnEntry( filter, entry, fc.getName() ) );
                        }
                    }
                } catch ( NoSuchFieldException | IllegalAccessException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e.getMessage(), e );
                }
            }
        } );
        return filterResult.get();
    }

    /**
     * Applies a single filter to a table entry.
     *
     * @param filter
     *         The filter condition.
     * @param entry
     *         The entry to check.
     * @param columnName
     *         The column to apply the filter on.
     *
     * @return True if entry satisfies the filter, otherwise false.
     */
    private static < T > boolean applySingleFilterOnEntry( Filter filter, T entry, String columnName )
            throws NoSuchFieldException, IllegalAccessException {
        Object value = null;
        if ( entry instanceof LinkedCaseInsensitiveMap entryMap ) {
            value = entryMap.get( columnName );
        } else if ( entry instanceof HashMap entryMap ) {
            value = entryMap.get( columnName );
        } else {
            Field field = ReflectionUtils.getFieldByName( entry.getClass(), columnName );

            if ( field != null ) {
                field.setAccessible( true );
                value = field.get( entry );
                log.warn( value );
            }
        }
        if ( value == null ) {
            return true;
        }
        if ( value instanceof String strValue ) {
            boolean b = applyFilterOnStringValue( filter, strValue );
            return b;
        }
        throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), value ) );
    }

    /**
     * Applies filter operators on a column value.
     *
     * @param filter
     *         The filter.
     * @param columnValue
     *         The value to filter.
     *
     * @return True if filter condition is met.
     */
    private static boolean applyFilterOnStringValue( Filter filter, String columnValue ) {
        return switch ( filter.getOperator() ) {
            case ConstantsFilterOperators.BEGINS_WITH -> columnValue.toLowerCase().startsWith( filter.getValue().toLowerCase() );
            case ConstantsFilterOperators.ENDS_WITH -> columnValue.toLowerCase().endsWith( filter.getValue().toLowerCase() );
            case ConstantsFilterOperators.CONTAINS -> StringUtils.containsIgnoreCase( columnValue, filter.getValue() );
            case ConstantsFilterOperators.NOT_CONTAINS -> !StringUtils.containsIgnoreCase( columnValue, filter.getValue() );
            case ConstantsFilterOperators.EQUALS -> columnValue.equalsIgnoreCase( filter.getValue() );
            case ConstantsFilterOperators.NOT_EQUALS -> !columnValue.equalsIgnoreCase( filter.getValue() );
            default -> true;
        };
    }

    private static < T > void sortEntriesList( FiltersDTO filter, List< T > table ) {
        FilterColumn sortColumn = filter.getColumns().stream().filter( filterColumn -> filterColumn.getDir() != null ).findFirst()
                .orElse( null );
        if ( sortColumn != null ) {
            Comparator< T > comparator = Comparator.comparing( entry -> {
                try {
                    return getValueByColumnName( sortColumn.getName(), entry ).toLowerCase();
                } catch ( IllegalAccessException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e.getMessage(), e );
                }
            } );
            if ( ConstantsString.SORTING_DIRECTION_ASCENDING.equals( sortColumn.getDir() ) ) {
                table.sort( comparator );
            } else if ( ConstantsString.SORTING_DIRECTION_DESCENDING.equals( sortColumn.getDir() ) ) {
                table.sort( comparator.reversed() );
            }
        }
    }

    private static < T > String getValueByColumnName( String columnName, T entry ) throws IllegalAccessException {
        Field field = ReflectionUtils.getFieldByName( entry.getClass(), columnName );
        if ( field != null ) {
            field.setAccessible( true );

            return ( String ) field.get( entry );
        }
        return ConstantsString.EMPTY_STRING;

    }

}

