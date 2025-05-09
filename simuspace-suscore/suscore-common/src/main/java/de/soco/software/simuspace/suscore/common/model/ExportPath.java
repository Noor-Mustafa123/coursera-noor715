package de.soco.software.simuspace.suscore.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ExportPath.
 *
 * @author Noman Arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ExportPath {

    /**
     * The items.
     */
    private List< String > items;

    /**
     * Gets the items.
     *
     * @return the items
     */
    public List< String > getItems() {
        return items;
    }

    /**
     * Sets the items.
     *
     * @param items
     *         the new items
     */
    public void setItems( List< String > items ) {
        this.items = items;
    }

}