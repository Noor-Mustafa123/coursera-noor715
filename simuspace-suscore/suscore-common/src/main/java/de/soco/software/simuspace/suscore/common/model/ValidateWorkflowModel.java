package de.soco.software.simuspace.suscore.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class ValidateWorkflowModel.
 *
 * @author noman arshad
 */
public class ValidateWorkflowModel {

    /**
     * The elements.
     */
    Map< String, String > elements = new HashMap<>();

    /**
     * The fields.
     */
    ValidateWorkflowFeildsModel fields;

    /**
     * Instantiates a new validate workflow model.
     */
    public ValidateWorkflowModel() {
    }

    /**
     * Instantiates a new validate workflow model.
     *
     * @param elements
     *         the elements
     * @param fields
     *         the fields
     */
    public ValidateWorkflowModel( Map< String, String > elements, ValidateWorkflowFeildsModel fields ) {
        super();
        this.elements = elements;
        this.fields = fields;
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
     * Gets the fields.
     *
     * @return the fields
     */
    public ValidateWorkflowFeildsModel getFields() {
        return fields;
    }

    /**
     * Sets the fields.
     *
     * @param fields
     *         the new fields
     */
    public void setFields( ValidateWorkflowFeildsModel fields ) {
        this.fields = fields;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ValidateWorkflowModel [elements=" + elements + ", fields=" + fields + "]";
    }

}
