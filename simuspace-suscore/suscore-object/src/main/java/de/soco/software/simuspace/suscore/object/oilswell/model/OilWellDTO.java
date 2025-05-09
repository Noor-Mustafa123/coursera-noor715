package de.soco.software.simuspace.suscore.object.oilswell.model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class OilWellDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class OilWellDTO {

    /**
     * The data.
     */
    private Object data;

    /**
     * The layout.
     */
    private Object layout;

    /**
     * The config.
     */
    private Object config = new HashMap<>();

    /**
     * Gets the data.
     *
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( Object data ) {
        this.data = data;
    }

    /**
     * Gets the layout.
     *
     * @return the layout
     */
    public Object getLayout() {
        return layout;
    }

    /**
     * Sets the layout.
     *
     * @param layout
     *         the new layout
     */
    public void setLayout( Object layout ) {
        this.layout = layout;
    }

    /**
     * Gets the config.
     *
     * @return the config
     */
    public Object getConfig() {
        return config != null ? config : "";
    }

    /**
     * Sets the config.
     *
     * @param config
     *         the new config
     */
    public void setConfig( Object config ) {
        this.config = config;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OilWellRawDTO [data=" + data + ", layout=" + layout + ", config=" + config + "]";
    }

}
