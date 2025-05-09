package de.soco.software.simuspace.workflow.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is responsible for required field. It has only two values i.e. field is required or not.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class FieldRules {

    private boolean required;

    /**
     * Instantiates a new field rules.
     */
    public FieldRules() {
        super();
    }

    /**
     * Instantiates a new field rules.
     *
     * @param required
     *         the required
     */
    public FieldRules( boolean required ) {
        this();
        this.required = required;
    }

    /**
     * Checks if is required.
     *
     * @return true, if is required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets the required.
     *
     * @param required
     *         the new required
     */
    public void setRequired( boolean required ) {
        this.required = required;
    }

}
