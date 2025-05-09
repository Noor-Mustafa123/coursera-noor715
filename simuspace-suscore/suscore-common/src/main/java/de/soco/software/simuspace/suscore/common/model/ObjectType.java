package de.soco.software.simuspace.suscore.common.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ObjectType.
 *
 * @author fahad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ObjectType {

    /**
     * The id.
     */
    private UUID id;

    /**
     * The name.
     */
    private String name;

    /**
     * Instantiates a new object type.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public ObjectType( UUID id, String name ) {
        this.id = id;
        this.name = name;
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

}
