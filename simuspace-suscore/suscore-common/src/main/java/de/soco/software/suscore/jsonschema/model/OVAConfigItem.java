package de.soco.software.suscore.jsonschema.model;

import java.util.List;

/**
 * Represents tab configuration of given object
 *
 * @author Huzaifah
 */
public class OVAConfigItem {

    /**
     * key to represent which type of object it applies to
     */
    private String key;

    /**
     * the configured tabs for this objecct type
     */
    private List< OVAConfigTab > tabs;

    /**
     * Get key of this object type
     *
     * @return key of the object type
     */
    public String getKey() {
        return key;
    }

    /**
     * Set key of object type
     *
     * @param key
     */
    public void setKey( String key ) {
        this.key = key;
    }

    /**
     * Current tabs for given object type
     *
     * @return List of tabs
     */
    public List< OVAConfigTab > getTabs() {
        return tabs;
    }

    /**
     * Set the tabs for object type
     *
     * @param tabs
     */
    public void setTabs( List< OVAConfigTab > tabs ) {
        this.tabs = tabs;
    }

    /**
     * Constructor empty for Jackson
     */
    public OVAConfigItem() {
        super();
    }

}
