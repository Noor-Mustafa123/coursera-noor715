package de.soco.software.simuspace.workflow.model.impl;

/**
 * This class contains a String value of a field and field is of type select.
 */
public class SelectField extends Field< String > {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The value.
     */
    private String value;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue( String value ) {
        this.value = value;
    }

}
