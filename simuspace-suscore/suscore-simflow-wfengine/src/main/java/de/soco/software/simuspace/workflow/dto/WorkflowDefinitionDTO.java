package de.soco.software.simuspace.workflow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.DataTransferObject;

/**
 * The Class represent the object form of work flow definition data transfer object.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class WorkflowDefinitionDTO extends DataTransferObject {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -4307893079432673116L;

    /**
     * The elements.
     */
    private WorkflowModel elements;

    /**
     * Instantiates a new workflow definition DTO.
     */
    public WorkflowDefinitionDTO() {
        super();
    }

    /**
     * Instantiates a new workflow definition DTO.
     *
     * @param elements
     *         the elements
     */
    public WorkflowDefinitionDTO( WorkflowModel elements ) {
        this.elements = elements;
    }

    /**
     * Gets the elements.
     *
     * @return the elements
     */
    public WorkflowModel getElements() {
        return elements;
    }

    /**
     * Sets the elements.
     *
     * @param elements
     *         the new elements
     */
    public void setElements( WorkflowModel elements ) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "WorkflowDefinitionDTO [elements=" + elements + "]";
    }

}
