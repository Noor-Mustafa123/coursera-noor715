package de.soco.software.simuspace.workflow.model.impl;

/**
 * This class contains a directory value of a field.
 */
public class DirectoryField extends Field< String > {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The multiple.
     */
    private boolean multiple;

    /**
     * The rules.
     */
    private FieldRules rules;

    /**
     * The value.
     */
    private String value;

    /**
     * {@inheritDoc}
     */
    @Override
    public FieldRules getRules() {
        return rules;
    }

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
    public boolean isMultiple() {
        return multiple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMultiple( boolean multiple ) {
        this.multiple = multiple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRules( FieldRules rules ) {
        this.rules = rules;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    @Override
    public void setValue( String value ) {
        this.value = value;
    }

}
