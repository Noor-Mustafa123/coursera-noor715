/*
 *
 */

package de.soco.software.simuspace.workflow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;

/**
 * This class represent object form of status
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Status {

    /**
     * The id of status.
     */
    private int id;

    /**
     * The name of status.
     */
    private String name;

    /**
     * Instantiates a new status.
     */
    public Status() {
        super();
    }

    /**
     * Instantiates a new status.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public Status( int id, String name ) {
        super();
        this.id = id;
        this.name = name;
    }

    /**
     * Instantiates a new status.
     *
     * @param status
     *         the status
     */
    public Status( WorkflowStatus status ) {
        super();
        this.id = status.getKey();
        this.name = status.getValue();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
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
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( int id ) {
        this.id = id;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Status [id=" + id + ", name=" + name + "]";
    }

}
