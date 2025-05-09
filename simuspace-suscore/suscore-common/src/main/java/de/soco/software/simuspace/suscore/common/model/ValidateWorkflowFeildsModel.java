/*
 *
 */

package de.soco.software.simuspace.suscore.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class ValidateWorkflowFeildsModel.
 *
 * @author noman arshad
 */
public class ValidateWorkflowFeildsModel {

    /**
     * The elements.
     */
    Map< String, String > elements = new HashMap<>();

    /**
     * Instantiates a new validate workflow feilds model.
     */
    public ValidateWorkflowFeildsModel() {

    }

    /**
     * Instantiates a new validate workflow feilds model.
     *
     * @param elements
     *         the elements
     */
    public ValidateWorkflowFeildsModel( Map< String, String > elements ) {
        super();
        this.elements = elements;
    }

    /**
     * Gets the elements.
     *
     * @return the elements
     */
    public Map< String, String > getElements() {
        return elements;
    }

    /**
     * Sets the elements.
     *
     * @param elements
     *         the elements
     */
    public void setElements( Map< String, String > elements ) {
        this.elements = elements;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ValidateWorkflowFeildsModel [elements=" + elements + "]";
    }

}
