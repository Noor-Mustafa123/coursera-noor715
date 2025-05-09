package de.soco.software.simuspace.suscore.common.ui;

import java.util.List;

/**
 * The Class SelectionResponseUI to give back response to UI from saved selection
 *
 * @author noman arshad
 */
public class SelectionResponseUI {

    /**
     * The id.
     */
    private String id;

    /**
     * The id list.
     */
    private List< Object > items;

    private String json;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets the items.
     *
     * @return the items
     */
    public List< Object > getItems() {
        return items;
    }

    /**
     * Sets the items.
     *
     * @param items
     *         the new items
     */
    public void setItems( List< Object > items ) {
        this.items = items;
    }

    public String getJson() {
        return json;
    }

    public void setJson( String json ) {
        this.json = json;
    }

}
