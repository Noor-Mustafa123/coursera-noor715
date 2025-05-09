package de.soco.software.simuspace.workflow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class consist of action type performing on any object
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Action {

    /**
     * The id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    /**
     * The type.
     */
    private String type;

    public Action() {
        super();
    }

    /**
     * Instantiates a new action object.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param type
     *         the type
     */
    public Action( int id, String name, String type ) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * Gets the id of action.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of action.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of action.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the id of action.
     *
     * @param id
     *         the new id
     */
    public void setId( int id ) {
        this.id = id;
    }

    /**
     * Sets the name of action.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Sets the type of action.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Status [id=" + id + ", name=" + name + ", type" + type + "]";
    }

}