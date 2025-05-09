package de.soco.software.simuspace.suscore.common.base;

import java.util.UUID;

/**
 * The Class object is used to render check box on Front end.
 *
 * @author M.Nasir.Farooq
 */
public class CheckBox {

    /**
     * The Constant CHECKED.
     */
    public static final int CHECKED = 1;

    /**
     * The Constant UN_CHECKED.
     */
    public static final int UN_CHECKED = 0;

    /**
     * The Constant DISABLED.
     */
    public static final int DISABLED = -1;

    /**
     * The id.
     */
    private UUID id;

    /**
     * The name.
     */
    private String name;

    /**
     * The value.
     */
    private int value;

    /**
     * Instantiates a new check box.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param value
     *         the value
     */
    public CheckBox( UUID id, String name, int value ) {
        super();
        this.id = id;
        this.name = name;
        this.value = value;
    }

    /**
     * Instantiates a new check box.
     */
    public CheckBox() {
        super();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
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
    public int getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( int value ) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CheckBox [id=" + id + ", name=" + name + ", value=" + value + "]";
    }

}
