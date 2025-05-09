package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class represent object form of status
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class StatusDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2349011519775921235L;

    /**
     * The id of status.
     */
    private String id;

    /**
     * The name of status.
     */
    private String name;

    /**
     * Instantiates a new status.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public StatusDTO() {
    }

    public StatusDTO( String id, String name ) {
        super();
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
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
    public void setId( String id ) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Status [id=" + id + ", name=" + name + "]";
    }

}
