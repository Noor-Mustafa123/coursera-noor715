package de.soco.software.simuspace.suscore.common.cb2.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BmwCaeBenchLoadcaseTreeDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchScenerioTreeDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1378800442844312853L;

    /**
     * The data.
     */
    private List< Cb2ScenerioTreeChildrenDTO > data;

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List< Cb2ScenerioTreeChildrenDTO > getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( List< Cb2ScenerioTreeChildrenDTO > data ) {
        this.data = data;
    }

}
