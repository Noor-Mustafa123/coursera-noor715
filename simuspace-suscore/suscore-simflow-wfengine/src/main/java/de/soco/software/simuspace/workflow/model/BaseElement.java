package de.soco.software.simuspace.workflow.model;

import de.soco.software.simuspace.workflow.model.impl.ElementInfo;

/**
 * The Interface BaseElement. This interface contains a work flow elements basic properties.
 *
 * @author M.Nasir.Farooq
 */
public interface BaseElement {

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    String getComments();

    /**
     * This method returns the description of work flow elements.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Gets the information of elements containing version, comments and description.
     *
     * @return the info
     */
    ElementInfo getInfo();

    /**
     * Gets the key.
     *
     * @return the key
     */
    String getKey();

    /**
     * Gets the name.
     *
     * @return the name
     */
    String getName();

    /**
     * Sets the comments.
     *
     * @param comments
     *         the new comments
     */
    void setComments( String comments );

    /**
     * This method sets the description of work flow elements.
     *
     * @param description
     *         the new description
     */
    void setDescription( String description );

    /**
     * Sets the key.
     *
     * @param key
     *         the new key
     */
    void setKey( String key );

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    void setName( String name );

}
