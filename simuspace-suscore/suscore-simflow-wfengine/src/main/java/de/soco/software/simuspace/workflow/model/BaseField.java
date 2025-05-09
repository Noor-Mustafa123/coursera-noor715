package de.soco.software.simuspace.workflow.model;

/**
 * The Interface BaseField. This interface contains a Field's Basic properties
 */
public interface BaseField {

    /**
     * Gets the mode.
     *
     * @return the mode
     */
    String getMode();

    /**
     * Gets the name. Name of a workflow element It is a unique entity with in a workflow
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the type.
     *
     * @return the type
     */
    String getType();

    /**
     * Sets the mode.
     *
     * @param model
     *         the new mode
     */
    void setMode( String model );

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    void setName( String name );

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    void setType( String type );

}
