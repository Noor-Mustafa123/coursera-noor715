package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serializable;

import de.soco.software.simuspace.workflow.model.BaseField;

/**
 * This class contains a Field's Basic properties This is extended by all the element Fields
 */

public class BaseFieldImpl implements BaseField, Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The key.
     */
    private String mode;

    /**
     * The name.
     */
    private String name;

    /**
     * The type.
     */
    private String type;

    /**
     * Instantiates a new base field impl.
     */
    public BaseFieldImpl() {
        super();
    }

    /**
     * Instantiates a new base field impl.
     *
     * @param mode
     *         the mode
     * @param name
     *         the name
     */
    public BaseFieldImpl( String mode, String name ) {
        this();
        this.mode = mode;
        this.name = name;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMode() {
        return mode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMode( String mode ) {
        this.mode = mode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setType( String type ) {
        this.type = type;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BaseFieldImpl [key='" + mode + "', name='" + name + "', type='" + type + "']";
    }

}
