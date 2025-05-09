package de.soco.software.simuspace.workflow.model.impl;

/**
 * This class contains a Float value of a field.
 */
public class FloatField extends Field< Float > {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The value.
     */
    private Float value;

    /**
     * {@inheritDoc}
     */
    @Override
    public Float getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue( Float value ) {
        this.value = value;
    }

}
