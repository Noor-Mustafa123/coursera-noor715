package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The is responsible to map workflow element.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class WorkflowConfigElements implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The element.
     */
    private ElementConfigData element;

    public WorkflowConfigElements() {
    }

    /**
     * Gets the element.
     *
     * @return the element
     */
    public ElementConfigData getElement() {
        return element;
    }

    /**
     * Sets the element.
     *
     * @param element
     *         the new element
     */
    public void setElement( ElementConfigData element ) {
        this.element = element;
    }

}
