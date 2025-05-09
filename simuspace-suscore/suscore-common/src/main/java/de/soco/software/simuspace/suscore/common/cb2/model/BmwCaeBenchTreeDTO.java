package de.soco.software.simuspace.suscore.common.cb2.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class Cb2TreeDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchTreeDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 8531016810603374991L;

    /**
     * The data.
     */
    private List< Cb2TreeChildrenDTO > data;

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List< Cb2TreeChildrenDTO > getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( List< Cb2TreeChildrenDTO > data ) {
        this.data = data;
    }

    /**
     * Instantiates a new cb 2 tree DTO.
     *
     * @param data
     *         the data
     */
    public BmwCaeBenchTreeDTO( List< Cb2TreeChildrenDTO > data ) {
        super();
        this.data = data;
    }

    /**
     * Instantiates a new cb 2 tree DTO.
     */
    public BmwCaeBenchTreeDTO() {
    }

}