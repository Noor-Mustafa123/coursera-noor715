package de.soco.software.simuspace.workflow.model.impl;

/**
 * This class contains a boolean value of a field.
 */
public class BooleanField extends Field< Boolean > {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The value.
     */
    private Boolean value;

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    @Override
    public void setValue( Boolean value ) {
        this.value = value;
    }

}
