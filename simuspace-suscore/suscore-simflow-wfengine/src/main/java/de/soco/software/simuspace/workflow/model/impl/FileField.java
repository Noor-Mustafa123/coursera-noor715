package de.soco.software.simuspace.workflow.model.impl;

/**
 * This class contains a file value of a field.
 */
public class FileField extends Field< EngineFile > {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The value.
     */
    private EngineFile value;

    /**
     * {@inheritDoc}
     */
    @Override
    public EngineFile getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue( EngineFile value ) {
        this.value = value;
    }

}
