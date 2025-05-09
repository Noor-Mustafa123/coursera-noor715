package de.soco.software.simuspace.suscore.common.ui;

import java.io.Serializable;

/**
 * The Class BindVisibility.
 */
public class BindVisibility implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 9036771566786324237L;

    /**
     * The name.
     */
    private String name;

    /**
     * The value.
     */
    private String value;

    /**
     * Instantiates a new bind visibility.
     */
    public BindVisibility() {
        super();
    }

    /**
     * Instantiates a new bind visibility.
     *
     * @param name
     *         the name
     * @param value
     *         the value
     */
    public BindVisibility( String name, String value ) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( String value ) {
        this.value = value;
    }

}
