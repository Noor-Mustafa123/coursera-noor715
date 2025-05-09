package de.soco.software.simuspace.suscore.common.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;

/**
 * @author Ahmar Nadeem
 *
 * This utility class deals with the paginating different kind of data.
 */
public final class PaginationUtil {

    private PaginationUtil() {

    }

    /**
     * A util function to return a map as paginated result. It expects a map and the filter criteria to filter/paginate the data.
     *
     * @param existingMap
     *         the existingMap
     * @param filter
     *         the filter
     *
     * @return FilteredResponse on the basis of provided filters
     */
    @SuppressWarnings( { "rawtypes" } )
    public static FilteredResponse getPaginatedMapData( Map< String, String > existingMap, FiltersDTO filter ) {
        FilteredResponse fr = new FilteredResponse<>();
        List< Entry< String, String > > subList = new ArrayList<>();
        fr.setRecordsTotal( ( long ) existingMap.size() );
        Map< String, String > orderedMap = new LinkedHashMap<>();
        existingMap.entrySet().stream().sorted( Map.Entry.comparingByKey() )
                .forEachOrdered( x -> orderedMap.put( x.getKey(), x.getValue() ) );

        if ( !orderedMap.isEmpty() ) {
            List< Entry< String, String > > toList = orderedMap.entrySet().stream().toList();

            int toIndex = filter.getLength() + filter.getStart();

            subList = toList.subList( filter.getStart(), Math.min( toIndex, toList.size() ) );
        }
        return constructFilteredResponse( filter, subList );
    }

    /**
     * This util function wraps the filtered data to the filtered response object, required by the front-end
     *
     * @param filter
     *         the filter
     * @param filteredData
     *         the filteredData
     *
     * @return FilteredResponse object
     */
    public static < T > FilteredResponse< T > constructFilteredResponse( FiltersDTO filter, List< T > filteredData ) {
        FilteredResponse< T > fr = new FilteredResponse<>();
        fr.setDraw( filter.getDraw() );
        fr.setStart( filter.getStart() );
        fr.setLength( filter.getLength() );
        fr.setRecordsFiltered( filter.getFilteredRecords() != null ? filter.getFilteredRecords().intValue() : 0 );
        fr.setRecordsTotal( filter.getTotalRecords() != null ? filter.getTotalRecords() : ConstantsInteger.INTEGER_VALUE_ZERO );
        fr.setSearchId( filter.getSearchId() );
        fr.setCurrentView( filter.getCurrentView() );
        fr.setData( filteredData );
        fr.setReloadUI( filter.isReloadUI() );
        return fr;
    }

    /**
     * Performs paging on list and returns list with items of current page only
     *
     * @param list
     *         list the list
     * @param filter
     *         the filter
     *
     * @return paged items list
     */
    public static < T > List< T > getPaginatedList( List< T > list, FiltersDTO filter ) {
        List< T > pageList = new ArrayList<>();

        // Getting list for current page only
        if ( filter.getStart() < list.size() ) {
            pageList = list.subList( filter.getStart(),
                    ( list.size() - filter.getStart() ) <= filter.getLength() ? list.size() : ( filter.getStart() + filter.getLength() ) );
        }

        return pageList;
    }

}
