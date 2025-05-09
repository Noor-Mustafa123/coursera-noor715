package de.soco.software.simuspace.suscore.local.daemon.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class to map json of list of ids of any object as array
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ItemsDTO {

    /**
     * The items.
     */
    private List< UUID > items;

    /**
     * @return the items
     */
    public List< UUID > getItems() {
        return items;
    }

    /**
     * @param items
     *         the items to set
     */
    public void setItems( List< UUID > items ) {
        this.items = items;
    }

}
