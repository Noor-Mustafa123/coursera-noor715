package de.soco.software.simuspace.workflow.model;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.soco.software.simuspace.workflow.model.impl.CategoryImpl;

/**
 * The Interface Category. This Interface is an work flow element type An element can be of [element,composed,custom] category/type
 */
@JsonDeserialize( as = CategoryImpl.class )
@Deprecated( forRemoval = true )
public interface Category extends Serializable {

    /**
     * Gets the count.
     *
     * @return the count
     */
    int getCount();

    /**
     * Gets the id.
     *
     * @return the id
     */
    String getId();

    /**
     * Gets the name.
     *
     * @return the name
     */
    String getName();

    /**
     * Sets the count.
     *
     * @param count
     *         the new count
     */
    void setCount( int count );

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    void setId( String id );

    /**
     * Sets the name.
     *
     * @param name
     *         the name
     */
    void setName( String name );

}
