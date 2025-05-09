package de.soco.software.simuspace.suscore.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class DashboardPluginDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
@Deprecated( since = "soco/2.3.1/release", forRemoval = true )
public class DashboardPluginDTO {

    /**
     * The Id.
     */
    private String id;

    /**
     * The name.
     */
    private String name;

    private String url;

    /**
     * The config.
     */
    private List< DashboardPluginConfigDTO > config;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the config.
     *
     * @return the config
     */
    public List< DashboardPluginConfigDTO > getConfig() {
        return config;
    }

    /**
     * Sets the config.
     *
     * @param config
     *         the config
     */
    public void setConfig( List< DashboardPluginConfigDTO > config ) {
        this.config = config;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url
     *         the url
     */
    public void setUrl( String url ) {
        this.url = url;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id
     *         the id
     */
    public void setId( String id ) {
        this.id = id;
    }

}
