package de.soco.software.simuspace.suscore.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The class BreadCrumbDTO deals with FE for Providing Data
 *
 * @author Zain ul Hassan
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BreadCrumbDTO implements Serializable {

    /**
     * Constant serialVersionUID
     */
    @Serial
    private static final long serialVersionUID = -2956802852383420672L;

    /**
     * The BreadCrumbItemDTO List.
     */
    private List< BreadCrumbItemDTO > breadCrumbItems;

    /**
     * Default Constructor
     */
    public BreadCrumbDTO() {
        super();
    }

    /**
     * Constructor Using Fields
     *
     * @param breadCrumbItems
     *         the breadCrumbItems
     */
    public BreadCrumbDTO( List< BreadCrumbItemDTO > breadCrumbItems ) {
        this.breadCrumbItems = breadCrumbItems;
    }

    /**
     * getBreadCrumbItems
     *
     * @return List<BreadCrumbItemDTO>
     */
    public List< BreadCrumbItemDTO > getBreadCrumbItems() {
        return breadCrumbItems;
    }

    /**
     * Sets the List of BreadCrumbItemDTO
     *
     * @param breadCrumbItems
     *         the breadCrumbItems
     */
    public void setBreadCrumbItems( List< BreadCrumbItemDTO > breadCrumbItems ) {
        this.breadCrumbItems = breadCrumbItems;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BreadCrumbDTO [ breadCrumbItemsDTO = " + breadCrumbItems + "]";
    }

}
