package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BmwCaeBenchDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public abstract class BmwCaeBenchDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -6856838753936744428L;

    /**
     * The id.
     */
    private UUID id;

    /**
     * The oid.
     */
    private String oid;

    /**
     * The name.
     */
    @UIFormField( name = "name", title = "3000161x4", orderNum = 1 )
    @UIColumn( data = "name", filter = "text", renderer = "text", title = "3000161x4", name = "name", orderNum = 1, width = 0 )
    private String name;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000154x4", orderNum = 24 )
    @UIColumn( data = "description", filter = "text", renderer = "text", title = "3000154x4", name = "description", orderNum = 24 )
    private String description;

    /**
     * The owner.
     */
    @UIFormField( name = "owner", title = "3000147x4", orderNum = 25 )
    @UIColumn( data = "owner", filter = "text", renderer = "text", title = "3000147x4", name = "owner", orderNum = 25 )
    private String owner;

    /**
     * The created by.
     */
    @UIFormField( name = "createdBy", title = "3000183x4", orderNum = 26 )
    @UIColumn( data = "createdBy", filter = "text", renderer = "text", title = "3000183x4", name = "createdBy", orderNum = 26 )
    private String createdBy;

    /**
     * The created at.
     */
    @UIFormField( name = "createdAt", title = "3000148x4", orderNum = 27 )
    @UIColumn( data = "createdAt", filter = "", renderer = "text", title = "3000148x4", name = "createdAt", orderNum = 27 )
    private String createdAt;

    /**
     * Instantiates a new bmw cae bench DTO.
     */
    public BmwCaeBenchDTO() {
        super();
    }

    /**
     * Instantiates a new bmw cae bench DTO.
     *
     * @param id
     *         the id
     * @param oid
     *         the oid
     * @param name
     *         the name
     * @param description
     *         the description
     * @param createdAt
     *         the created at
     */
    public BmwCaeBenchDTO( UUID id, String oid, String name, String description, String createdAt ) {
        super();
        this.id = id;
        this.oid = oid;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
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
     * Gets the oid.
     *
     * @return the oid
     */
    public String getOid() {
        return oid;
    }

    /**
     * Sets the oid.
     *
     * @param oid
     *         the new oid
     */
    public void setOid( String oid ) {
        this.oid = oid;
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
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *         the new description
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * Gets the created at.
     *
     * @return the created at
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the created at.
     *
     * @param createdAt
     *         the new created at
     */
    public void setCreatedAt( String createdAt ) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the owner.
     *
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     *
     * @param owner
     *         the new owner
     */
    public void setOwner( String owner ) {
        this.owner = owner;
    }

    /**
     * Gets the created by.
     *
     * @return the created by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the created by.
     *
     * @param createdBy
     *         the new created by
     */
    public void setCreatedBy( String createdBy ) {
        this.createdBy = createdBy;
    }

}
