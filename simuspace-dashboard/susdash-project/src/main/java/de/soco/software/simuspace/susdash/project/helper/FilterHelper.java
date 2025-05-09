package de.soco.software.simuspace.susdash.project.helper;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFilterOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.susdash.project.model.LifecycleTableDTO;
import de.soco.software.simuspace.susdash.project.model.ProjectStructureTableCell;
import de.soco.software.simuspace.susdash.project.model.ProjectStructureTupleWrapper;

/**
 * The type Filter helper.
 */
@Deprecated
public class FilterHelper {

    /*####################### For structure table #######################*/

    /**
     * The constant LEVEL.
     */
    private static final String LEVEL = "level";

    /**
     * Gets filtered entries list.
     *
     * @param filter
     *         the filter
     * @param table
     *         the table
     * @param maxDepth
     *         the max depth
     *
     * @return the filtered entries list
     */
    public static List< ProjectStructureTupleWrapper > getFilteredTableEntriesList( FiltersDTO filter,
            List< ProjectStructureTupleWrapper > table, String maxDepth ) {
        filter.setTotalRecords( ( long ) table.size() );
        if ( !StringUtils.isEmpty( filter.getSearch() ) ) {
            for ( int level = 0; level < filter.getColumns().size(); level++ ) {
                table = applySearchToTable( filter.getSearch(), table, level, maxDepth );
            }
        }
        table = applyFiltersToTable( filter.getColumns(), table );
        filter.setFilteredRecords( ( long ) table.size() );
        return table;
    }

    /**
     * Apply search to table list.
     *
     * @param search
     *         the search
     * @param originalTable
     *         the original table
     * @param level
     *         the level
     * @param maxDepthFromConf
     *         the max depth from conf
     *
     * @return the list
     */
    private static List< ProjectStructureTupleWrapper > applySearchToTable( String search,
            List< ProjectStructureTupleWrapper > originalTable, int level, String maxDepthFromConf ) {
        int maxDepth = Integer.parseInt( maxDepthFromConf );
        return originalTable.stream()
                .filter( entry -> ( entry.get( LEVEL + level ) == null )
                        || ( entry.get( LEVEL + level ) != null && applySearchOnTableEntry( search, entry, maxDepth ) ) )
                .collect( Collectors.toList() );
    }

    /**
     * Apply search on table entry boolean.
     *
     * @param search
     *         the search
     * @param entry
     *         the entry
     * @param maxDepth
     *         the max depth
     *
     * @return the boolean
     */
    private static boolean applySearchOnTableEntry( String search, ProjectStructureTupleWrapper entry, int maxDepth ) {
        String columnValue;
        for ( int i = 0; i < maxDepth; i++ ) {
            columnValue = entry.get( LEVEL + i ) != null ? entry.get( LEVEL + i ).getName() : null;
            if ( columnValue == null || StringUtils.containsIgnoreCase( columnValue, search ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Apply filters to table list.
     *
     * @param filterColumns
     *         the filter column
     * @param originalTable
     *         the original table
     *
     * @return the list
     */
    private static List< ProjectStructureTupleWrapper > applyFiltersToTable( List< FilterColumn > filterColumns,
            List< ProjectStructureTupleWrapper > originalTable ) {
        return originalTable.stream()
                .filter( entry -> ( CollectionUtils.isEmpty( filterColumns ) || applyFiltersOnTableEntry( filterColumns, entry ) ) )
                .collect( Collectors.toList() );
    }

    /**
     * Apply filterColumns on table entry boolean.
     *
     * @param filterColumns
     *         the filterColumns
     * @param entry
     *         the entry
     *
     * @return the boolean
     */
    private static boolean applyFiltersOnTableEntry( List< FilterColumn > filterColumns, ProjectStructureTupleWrapper entry ) {
        if ( CollectionUtils.isEmpty( filterColumns ) ) {
            return true;
        }
        return applyANDFiltersOnSingleEntry( filterColumns, entry ) && applyORFiltersOnSingleEntry( filterColumns, entry );
    }

    /**
     * Apply and filters on single entry boolean.
     *
     * @param filtersColumns
     *         the filters columns
     * @param entry
     *         the entry
     *
     * @return the boolean
     */
    private static boolean applyANDFiltersOnSingleEntry( List< FilterColumn > filtersColumns, ProjectStructureTupleWrapper entry ) {
        AtomicBoolean isFirstFilter = new AtomicBoolean( true );
        AtomicBoolean filterResult = new AtomicBoolean( true );
        filtersColumns.stream().filter( fc -> CollectionUtils.isNotEmpty( fc.getFilters() ) ).forEach( fc -> {
            for ( Filter filter : fc.getFilters() ) {
                if ( filter.getCondition().equals( "AND" ) ) {
                    if ( isFirstFilter.get() ) {
                        isFirstFilter.set( false );
                        filterResult.set( applySingleFilterOnTableEntry( filter, entry, fc.getName() ) );
                    } else {
                        filterResult.set( filterResult.get() && applySingleFilterOnTableEntry( filter, entry, fc.getName() ) );
                    }
                }
            }
        } );
        return filterResult.get();
    }

    private static boolean applyORFiltersOnSingleEntry( List< FilterColumn > filterColumns, ProjectStructureTupleWrapper entry ) {
        AtomicBoolean isFirstFilter = new AtomicBoolean( true );
        AtomicBoolean filterResult = new AtomicBoolean( true );
        filterColumns.stream().filter( fc -> CollectionUtils.isNotEmpty( fc.getFilters() ) ).forEach( fc -> {
            for ( Filter filter : fc.getFilters() ) {
                if ( filter.getCondition().equals( "OR" ) ) {
                    if ( isFirstFilter.get() ) {
                        isFirstFilter.set( false );
                        filterResult.set( applySingleFilterOnTableEntry( filter, entry, fc.getName() ) );
                    } else {
                        filterResult.set( filterResult.get() || applySingleFilterOnTableEntry( filter, entry, fc.getName() ) );
                    }
                }
            }
        } );
        return filterResult.get();
    }

    /**
     * Apply single filter on table entry boolean.
     *
     * @param filter
     *         the filter
     * @param entry
     *         the entry
     * @param name
     *         the name
     *
     * @return the boolean
     */
    private static boolean applySingleFilterOnTableEntry( Filter filter, ProjectStructureTupleWrapper entry, String name ) {
        ProjectStructureTableCell cellValue = entry.get( name );
        if ( cellValue == null ) {
            return true;
        }
        String columnValue = cellValue.getName();
        if ( columnValue == null ) {
            return true;
        }
        return applyFilterOnStringValue( filter, columnValue );
    }

    /*####################### For lifecycle table #######################*/

    /**
     * Apply filters to lifecycle table list .
     *
     * @param filter
     *         the filter
     * @param dtoList
     *         the dto list
     *
     * @return the list
     */
    public static List< LifecycleTableDTO > applyFiltersToLifecycleTable( FiltersDTO filter, List< LifecycleTableDTO > dtoList ) {
        filter.setTotalRecords( ( long ) dtoList.size() );
        var dtoColumns = GUIUtils.listColumns( LifecycleTableDTO.class );
        if ( !StringUtils.isEmpty( filter.getSearch() ) ) {
            dtoList = applySearchToLifecycleTable( filter.getSearch(), dtoList, dtoColumns );
        }
        dtoList = applyFiltersToDTOList( filter.getColumns(), dtoList );
        filter.setFilteredRecords( ( long ) dtoList.size() );
        return dtoList;
    }

    /**
     * Apply search to lifecycle table list .
     *
     * @param search
     *         the search
     * @param dtoList
     *         the dto list
     * @param tableColumns
     *         the table columns
     *
     * @return the list
     */
    private static List< LifecycleTableDTO > applySearchToLifecycleTable( String search, List< LifecycleTableDTO > dtoList,
            List< TableColumn > tableColumns ) {
        return dtoList.stream()
                .filter( dto -> tableColumns.stream()
                        .anyMatch( column -> StringUtils.containsIgnoreCase( dto.getColumnValue( column.getName() ), search ) ) )
                .collect( Collectors.toList() );

    }

    /**
     * Apply filters to dto list list.
     *
     * @param filterColumns
     *         the filterColumns
     * @param originalList
     *         the original list
     *
     * @return the list
     */
    private static List< LifecycleTableDTO > applyFiltersToDTOList( List< FilterColumn > filterColumns,
            List< LifecycleTableDTO > originalList ) {
        return originalList.stream()
                .filter( dto -> ( CollectionUtils.isEmpty( filterColumns ) || applyFiltersOnLifecycleTableEntry( filterColumns, dto ) ) )
                .collect( Collectors.toList() );
    }

    /**
     * Apply filters on lifecycle table entry boolean.
     *
     * @param filterColumns
     *         the filter columns
     * @param dto
     *         the dto
     *
     * @return the boolean
     */
    private static boolean applyFiltersOnLifecycleTableEntry( List< FilterColumn > filterColumns, LifecycleTableDTO dto ) {
        if ( CollectionUtils.isEmpty( filterColumns ) ) {
            return true;
        }
        return applyANDFiltersOnSingleLifeCycleEntry( filterColumns, dto ) && applyORFiltersOnSingleLifeCycleEntry( filterColumns, dto );
    }

    /**
     * Apply or filters on single life cycle entry boolean.
     *
     * @param filterColumns
     *         the filter columns
     * @param dto
     *         the dto
     *
     * @return the boolean
     */
    private static boolean applyORFiltersOnSingleLifeCycleEntry( List< FilterColumn > filterColumns, LifecycleTableDTO dto ) {
        AtomicBoolean isFirstFilter = new AtomicBoolean( true );
        AtomicBoolean filterResult = new AtomicBoolean( true );
        filterColumns.stream().filter( fc -> CollectionUtils.isNotEmpty( fc.getFilters() ) ).forEach( fc -> {
            for ( Filter filter : fc.getFilters() ) {
                if ( filter.getCondition().equals( "AND" ) ) {
                    if ( isFirstFilter.get() ) {
                        isFirstFilter.set( false );
                        filterResult.set( applySingleFilterLifeCycleTableEntry( filter, dto, fc.getName() ) );
                    } else {
                        filterResult.set( filterResult.get() && applySingleFilterLifeCycleTableEntry( filter, dto, fc.getName() ) );
                    }
                }
            }
        } );
        return filterResult.get();
    }

    /**
     * Apply and filters on single life cycle entry boolean.
     *
     * @param filterColumns
     *         the filter columns
     * @param dto
     *         the dto
     *
     * @return the boolean
     */
    private static boolean applyANDFiltersOnSingleLifeCycleEntry( List< FilterColumn > filterColumns, LifecycleTableDTO dto ) {
        AtomicBoolean isFirstFilter = new AtomicBoolean( true );
        AtomicBoolean filterResult = new AtomicBoolean( true );
        filterColumns.stream().filter( fc -> CollectionUtils.isNotEmpty( fc.getFilters() ) ).forEach( fc -> {
            for ( Filter filter : fc.getFilters() ) {
                if ( filter.getCondition().equals( "OR" ) ) {
                    if ( isFirstFilter.get() ) {
                        isFirstFilter.set( false );
                        filterResult.set( applySingleFilterLifeCycleTableEntry( filter, dto, fc.getName() ) );
                    } else {
                        filterResult.set( filterResult.get() || applySingleFilterLifeCycleTableEntry( filter, dto, fc.getName() ) );
                    }
                }
            }
        } );
        return filterResult.get();
    }

    /**
     * Apply single filter life cycle table entry boolean.
     *
     * @param filter
     *         the filter
     * @param dto
     *         the dto
     * @param columnName
     *         the column name
     *
     * @return the boolean
     */
    private static boolean applySingleFilterLifeCycleTableEntry( Filter filter, LifecycleTableDTO dto, String columnName ) {
        String columnValue = dto.getColumnValue( columnName );
        if ( columnValue == null ) {
            return true;
        }
        return applyFilterOnStringValue( filter, columnValue );
    }

    /*####################### generic methods #######################*/

    /**
     * Apply filter on string value boolean.
     *
     * @param filter
     *         the filter
     * @param columnValue
     *         the column value
     *
     * @return the boolean
     */
    private static boolean applyFilterOnStringValue( Filter filter, String columnValue ) {
        switch ( filter.getOperator() ) {
            case ConstantsFilterOperators.BEGINS_WITH -> {
                return columnValue.regionMatches( true, 0, filter.getValue(), 0, filter.getValue().length() );
            }
            case ConstantsFilterOperators.ENDS_WITH -> {
                return columnValue.regionMatches( true, columnValue.length() - filter.getValue().length(), filter.getValue(), 0,
                        filter.getValue().length() );
            }
            case ConstantsFilterOperators.CONTAINS -> {
                return StringUtils.containsIgnoreCase( columnValue, filter.getValue() );
            }
            case ConstantsFilterOperators.NOT_CONTAINS -> {
                return !StringUtils.containsIgnoreCase( columnValue, filter.getValue() );
            }
            case ConstantsFilterOperators.EQUALS -> {
                return columnValue.equalsIgnoreCase( filter.getValue() );
            }
            case ConstantsFilterOperators.NOT_EQUALS -> {
                return !columnValue.equalsIgnoreCase( filter.getValue() );
            }
            default -> {
                return true;
            }
        }
    }

    /**
     * Decrease page number to list size.
     *
     * @param filter
     *         the filter
     * @param size
     *         the size
     */
    public static void decreasePageNumberToListSize( FiltersDTO filter, int size ) {
        if ( size <= ConstantsInteger.INTEGER_VALUE_ZERO ) {
            filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        } else if ( filter.getStart() >= size ) {
            filter.setStart( Math.max( ( filter.getStart() - filter.getLength() ), ConstantsInteger.INTEGER_VALUE_ZERO ) );
            decreasePageNumberToListSize( filter, size );
        }
    }

    /**
     * Sort entries list.
     *
     * @param filter
     *         the filter
     * @param table
     *         the table
     */
    public static void sortEntriesList( FiltersDTO filter, List< ProjectStructureTupleWrapper > table ) {
        FilterColumn sortColumn = filter.getColumns().stream().filter( filterColumn -> filterColumn.getDir() != null ).findFirst()
                .orElse( null );
        if ( sortColumn != null ) {
            Comparator< ProjectStructureTupleWrapper > comparator = Comparator
                    .comparing( projectStructureTupleWrapper -> projectStructureTupleWrapper.getNameByColumnName( sortColumn.getName() )
                            .toLowerCase() );
            if ( ConstantsString.SORTING_DIRECTION_ASCENDING.equals( sortColumn.getDir() ) ) {
                table.sort( comparator );
            } else if ( ConstantsString.SORTING_DIRECTION_DESCENDING.equals( sortColumn.getDir() ) ) {
                table.sort( comparator.reversed() );
            }
        }
    }

    /**
     * Sort entries list.
     *
     * @param filter
     *         the filter
     * @param dtoList
     *         the dto list
     */
    public static void sortDtoList( FiltersDTO filter, List< LifecycleTableDTO > dtoList ) {
        FilterColumn sortColumn = filter.getColumns().stream().filter( filterColumn -> filterColumn.getDir() != null ).findFirst()
                .orElse( null );
        if ( sortColumn != null ) {
            Comparator< LifecycleTableDTO > comparator = Comparator
                    .comparing( lifecycleTableDTO -> lifecycleTableDTO.getColumnValue( sortColumn.getName() ).toLowerCase() );
            if ( ConstantsString.SORTING_DIRECTION_ASCENDING.equals( sortColumn.getDir() ) ) {
                dtoList.sort( comparator );
            } else if ( ConstantsString.SORTING_DIRECTION_DESCENDING.equals( sortColumn.getDir() ) ) {
                dtoList.sort( comparator.reversed() );
            }
        }
    }

}
