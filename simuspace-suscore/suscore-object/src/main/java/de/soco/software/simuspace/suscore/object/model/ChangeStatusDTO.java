package de.soco.software.simuspace.suscore.object.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class is responsible for status change of an object.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ChangeStatusDTO {

    /**
     * The status.
     */
    private String status;

    /**
     * The include childs.
     */
    private String includeChilds;

    /**
     * Gets the include childs.
     *
     * @return the include childs
     */
    public String getIncludeChilds() {
        return includeChilds;
    }

    /**
     * Sets the include childs.
     *
     * @param includeChilds
     *         the new include childs
     */
    public void setIncludeChilds( String includeChilds ) {
        this.includeChilds = includeChilds;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *         the new status
     */
    public void setStatus( String status ) {
        this.status = status;
    }

}
