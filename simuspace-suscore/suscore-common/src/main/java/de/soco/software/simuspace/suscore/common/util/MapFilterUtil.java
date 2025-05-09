package de.soco.software.simuspace.suscore.common.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsOperators;

/**
 * The Class MapFilterUtil for filtering, validating and paginating sus maps.
 *
 * @author Zeeshan jamal
 */
public class MapFilterUtil extends LinkedHashMap< String, String > {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 9014905951601994112L;

    /**
     * The Constant KEY_FIELD.
     */
    public static final String KEY_FIELD = "key";

    /**
     * Filter map.
     *
     * @param filterDTO
     *         the filter DTO
     *
     * @return the map
     */
    public Map< String, String > filterMap( FiltersDTO filterDTO ) {
        MapFilterUtil filteredMetaDataMap = new MapFilterUtil();
        boolean hasFilter = false;
        if ( CollectionUtils.isNotEmpty( filterDTO.getColumns() ) ) {
            for ( FilterColumn filterColumn : filterDTO.getColumns() ) {
                if ( filterColumn.getFilters() != null ) {
                    for ( Filter filter : filterColumn.getFilters() ) {
                        hasFilter = true;
                        filteredMetaDataMap = filterMapEntries( filterColumn, filter );
                    }
                }
            }
        }
        if ( hasFilter ) {
            return searchOnKey( filteredMetaDataMap, filterDTO );
        } else {
            return searchOnKey( paginateMap( filterDTO ), filterDTO );
        }
    }

    /**
     * Paginate map.
     *
     * @param filterDTO
     *         the filter DTO
     *
     * @return the map filter util
     */
    public MapFilterUtil paginateMap( FiltersDTO filterDTO ) {
        MapFilterUtil newHashMap = new MapFilterUtil();
        if ( filterDTO.getLength() >= size() ) {
            return this;
        }
        if ( !isEmpty() ) {
            List< String > values = new LinkedList<>( values() );
            List< String > keys = new LinkedList<>( keySet() );
            int endIndex = filterDTO.getLength() + filterDTO.getStart();
            int endIndexToSearch = Math.min( endIndex, size() );
            List< String > valuesList = values.subList( filterDTO.getStart(), endIndexToSearch );
            List< String > keysList = keys.subList( filterDTO.getStart(), endIndexToSearch );
            Iterator< String > valIterator = valuesList.iterator();
            Iterator< String > keyIterator = keysList.iterator();

            while ( keyIterator.hasNext() && valIterator.hasNext() ) {
                newHashMap.put( keyIterator.next(), valIterator.next() );
            }
        }
        return newHashMap;
    }

    /**
     * Filter map entries.
     *
     * @param filterColumn
     *         the filter column
     * @param filter
     *         the filter
     *
     * @return the map filter util
     */
    private MapFilterUtil filterMapEntries( FilterColumn filterColumn, Filter filter ) {
        MapFilterUtil filteredMap = new MapFilterUtil();
        Set< Entry< String, String > > existingMapEntrySet = entrySet();
        if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            if ( filterColumn.getName().equals( KEY_FIELD ) ) {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> map.getKey().equals( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            } else {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> map.getValue().equals( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            if ( filterColumn.getName().equals( KEY_FIELD ) ) {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> !map.getKey().equals( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            } else {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> !map.getValue().equals( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            if ( filterColumn.getName().equals( KEY_FIELD ) ) {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> map.getKey().contains( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            } else {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> map.getValue().contains( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            if ( filterColumn.getName().equals( KEY_FIELD ) ) {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> !map.getKey().contains( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            } else {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> !map.getValue().contains( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            if ( filterColumn.getName().equals( KEY_FIELD ) ) {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> map.getKey().startsWith( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            } else {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> map.getValue().startsWith( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            if ( filterColumn.getName().equals( KEY_FIELD ) ) {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> map.getKey().endsWith( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            } else {
                filteredMap.putAll( existingMapEntrySet.stream().filter( map -> map.getValue().endsWith( filter.getValue() ) )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );
            }
        }
        return filteredMap;
    }

    /**
     * Search on key.
     *
     * @param filteredMap
     *         the filtered map
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the map
     */
    private Map< String, String > searchOnKey( MapFilterUtil filteredMap, FiltersDTO filtersDTO ) {
        if ( filtersDTO.getSearch() != null ) {
            return filteredMap.entrySet().stream().filter( map -> map.getKey().contains( filtersDTO.getSearch() ) )
                    .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) );
        }
        return filteredMap;
    }

}
