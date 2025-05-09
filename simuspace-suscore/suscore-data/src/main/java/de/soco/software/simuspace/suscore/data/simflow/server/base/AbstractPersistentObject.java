package de.soco.software.simuspace.suscore.data.simflow.server.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Type;

import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.PersistentObject;

/**
 * This abstract class serves as base entity for all objects in the system <br> and implements some methods to establish object uniqueness
 * across the system
 *
 * @author Nosheen.Sharif
 */
@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
public abstract class AbstractPersistentObject implements PersistentObject {

    /**
     * comments about object given by user
     */
    @Column( name = "comments" )
    private String comments;

    /**
     * created by User .
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "created_by", nullable = true )
    private UserEntity createdBy;

    /**
     * Creation date of object .
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * description of object given by user.
     */
    @Column( name = "description" )
    private String description;

    /**
     * id of the object
     */
    @Id
    @Column( name = "id" )
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The modified by User.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "modified_by", nullable = true )
    private UserEntity modifiedBy;

    /**
     * The modified Date of object.
     */
    @Column( name = "modified_on" )
    private Date modifiedOn;

    /**
     * The name of object.
     */
    @Column( name = "name" )
    private String name;

    /**
     * The status of object . Id will be from status enum class
     */
    @Column( name = "status" )
    private int status;

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Gets the created by.
     *
     * @return the created by
     */
    public UserEntity getCreatedBy() {
        return createdBy;
    }

    /**
     * Gets the created on.
     *
     * @return the created on
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Gets the modified by.
     *
     * @return the modified by
     */
    public UserEntity getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Gets the modified on.
     *
     * @return the modified on
     */
    public Date getModifiedOn() {
        return modifiedOn;
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
     * Gets the status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the comments.
     *
     * @param comments
     *         the new comments
     */
    public void setComments( String comments ) {
        this.comments = comments;
    }

    /**
     * Sets the created by.
     *
     * @param createdBy
     *         the new created by
     */
    public void setCreatedBy( UserEntity createdBy ) {
        this.createdBy = createdBy;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
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

    @Override
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * Sets the modified by.
     *
     * @param modifiedBy
     *         the new modified by
     */
    public void setModifiedBy( UserEntity modifiedBy ) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * Sets the modified on.
     *
     * @param modifiedOn
     *         the new modified on
     */
    public void setModifiedOn( Date modifiedOn ) {
        this.modifiedOn = modifiedOn;
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
     * Sets the status.
     *
     * @param status
     *         the new status
     */
    public void setStatus( int status ) {
        this.status = status;
    }

}
