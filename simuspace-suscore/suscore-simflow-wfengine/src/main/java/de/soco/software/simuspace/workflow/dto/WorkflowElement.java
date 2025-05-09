package de.soco.software.simuspace.workflow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.workflow.model.UserWFElement;

/**
 * The Class is responsible to map the workflow element json on it.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class WorkflowElement {

    /**
     * The list of elements.
     */
    private UserWFElement data;

    /**
     * Instantiates a new workflow element.
     */
    public WorkflowElement() {
        super();
    }

    /**
     * Instantiates a new workflow element.
     *
     * @param data
     *         the data
     */
    public WorkflowElement( UserWFElement data ) {
        super();
        this.data = data;

    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public UserWFElement getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( UserWFElement data ) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WorkflowElement [data=" + data + "]";
    }

}
