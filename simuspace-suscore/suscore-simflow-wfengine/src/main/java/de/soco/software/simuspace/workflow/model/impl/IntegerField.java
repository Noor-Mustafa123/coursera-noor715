package de.soco.software.simuspace.workflow.model.impl;

/**
 * This class contains a integer value of a field.
 */
public class IntegerField extends Field< Integer > {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The value.
     */
    private Integer value;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue( Integer value ) {
        this.value = value;
    }

}
