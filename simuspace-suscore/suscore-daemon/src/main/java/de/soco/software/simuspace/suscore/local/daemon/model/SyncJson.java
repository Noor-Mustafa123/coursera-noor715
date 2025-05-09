package de.soco.software.simuspace.suscore.local.daemon.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class to map json of local files configuration.
 *
 * @author Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SyncJson {

    /**
     * The configuration.
     */
    private SyncJsonConfiguration configuration;

    /**
     * The ignore.
     */
    private List< String > ignore;

    /**
     * Gets the configuration.
     *
     * @return the configuration
     */
    public SyncJsonConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Sets the configuration.
     *
     * @param configuration
     *         the configuration
     */
    public void setConfiguration( SyncJsonConfiguration configuration ) {
        this.configuration = configuration;
    }

    /**
     * Gets the ignore.
     *
     * @return the ignore
     */
    public List< String > getIgnore() {
        return ignore;
    }

    /**
     * Sets the ignore.
     *
     * @param ignore
     *         the ignore
     */
    public void setIgnore( List< String > ignore ) {
        this.ignore = ignore;
    }

}
