package de.soco.software.simuspace.suscore.common.util;

import java.util.ArrayList;
import java.util.List;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;

/**
 * The Class ListFilterUtil to paginate list
 *
 * @author Zeeshan jamal
 */
public class ListFilterUtil extends ArrayList< Object > {

    /**
     * Paginate list.
     *
     * @param filterDTO
     *         the filter DTO
     *
     * @return the list filter util
     */
    public ListFilterUtil paginateList( FiltersDTO filterDTO ) {
        ListFilterUtil list = new ListFilterUtil();
        if ( filterDTO.getLength() >= size() ) {
            return this;
        }
        if ( !isEmpty() ) {
            int endIndex = filterDTO.getLength() + filterDTO.getStart();
            int endIndexToSearch = Math.min( endIndex, size() );
            List< Object > valuesList = this.subList( filterDTO.getStart(), endIndexToSearch );
            list.addAll( valuesList );
        }
        return list;
    }

}
