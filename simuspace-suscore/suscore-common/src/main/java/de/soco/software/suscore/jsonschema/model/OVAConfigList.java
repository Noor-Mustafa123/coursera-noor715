package de.soco.software.suscore.jsonschema.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all the object type view configurations, read from json file
 *
 * @author Huzaifah
 */
public class OVAConfigList {

    /**
     * view configurations for all object types
     */
    private List< OVAConfigItem > viewConfig;

    /**
     * Check if an object type has tabs configuration in json, searched by provided key
     *
     * @param key
     *
     * @return if configuration exists
     */
    public boolean containsKey( String key ) {
        for ( OVAConfigItem ovaConfigItem : viewConfig ) {
            if ( ovaConfigItem.getKey().equals( key ) ) {
                return true;
            }
        }
        return false;

    }

    /**
     * Gets tabs for given object type
     *
     * @param key
     *
     * @return configuration of tabs
     */
    public List< OVAConfigTab > getTabsByKey( String key ) {
        for ( OVAConfigItem ovaConfigItem : viewConfig ) {
            if ( ovaConfigItem.getKey().equals( key ) ) {
                return ovaConfigItem.getTabs();
            }
        }
        return new ArrayList<>();
    }

    /**
     * get the view config of all objects
     *
     * @return all configuration of views
     */
    public List< OVAConfigItem > getViewConfig() {
        return viewConfig;
    }

    /**
     * Set view configurations, for jackson usage
     *
     * @param viewConfig
     */
    public void setViewConfig( List< OVAConfigItem > viewConfig ) {
        this.viewConfig = viewConfig;
    }

    /**
     * Constructor for jackson
     */
    public OVAConfigList() {
        super();
    }

}
