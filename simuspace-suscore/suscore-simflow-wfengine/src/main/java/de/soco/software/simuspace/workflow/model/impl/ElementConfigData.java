package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.workflow.model.ElementConfig;

/**
 * The is responsible to map Element configurations.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ElementConfigData implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -7178204030772990179L;

    /**
     * The data.
     */
    private ElementConfig data;

    public ElementConfigData() {
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public ElementConfig getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( ElementConfig data ) {
        this.data = data;
    }

}
