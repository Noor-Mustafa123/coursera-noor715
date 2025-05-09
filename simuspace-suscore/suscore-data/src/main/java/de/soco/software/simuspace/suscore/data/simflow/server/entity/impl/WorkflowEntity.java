package de.soco.software.simuspace.suscore.data.simflow.server.entity.impl;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * This Class is mapped to database as Entity, it provides attributes and definition of the workflow mapped to database.
 */
@Entity
// @Table ( name = "workflow" )
@Indexed( index = "WorkflowEntity" )
public class WorkflowEntity extends SuSEntity {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1183655658903803181L;

    /**
     * key user of workflow.
     */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "key_user_id", referencedColumnName = "id" )
    private UserEntity keyuser;

    /**
     * whether workflow is active or not.
     */
    @Column( name = "active" )
    private boolean active;

    /**
     * for versioning , Label of workflow version.
     */
    @Column( name = "version_label" )
    private String versionLabel;

    /**
     * whether workflow is internal or not. These worklfows will not be visible
     */
    @Column( name = "internal" )
    private boolean internal;

    /**
     * whether workflow is private.
     */
    @Column( name = "private" )
    private boolean isPrivateWorkflow;

    /**
     * save as blob consist of key like elements,connection and positions workflow model field to store in db .
     */
    @Column( name = "definition" )
    @Lob
    private byte[] definition;

    /**
     * comments of workflow.
     */
    @Column( name = "comments", length = 1024 )
    private String comments;

    /**
     * status of worklfow.
     */
    @Column( name = "workflow_status" )
    private int status;

    /**
     * The list of user Groups.
     */
    @LazyCollection( LazyCollectionOption.FALSE )
    @ManyToMany
    @JoinTable( name = "workflow_location" )
    private List< LocationEntity > runOnLocation;

    @LazyCollection( LazyCollectionOption.FALSE )
    @ElementCollection
    private List< String > customFlags;

    @Column( name = "selection_id" )
    private String locations;

    /**
     * Instantiates a new workflow entity.
     */
    public WorkflowEntity() {
        super();
    }

    /**
     * Instantiates a new workflow entity.
     *
     * @param id
     *         the id
     * @param versionId
     *         the version id
     */
    public WorkflowEntity( UUID id, int versionId ) {
        super.setComposedId( new VersionPrimaryKey( id, versionId ) );

    }

    /**
     * Gets the keyuser.
     *
     * @return the keyuser
     */
    public UserEntity getKeyuser() {
        return keyuser;
    }

    /**
     * Sets the keyuser.
     *
     * @param keyuser
     *         the new keyuser
     */
    public void setKeyuser( UserEntity keyuser ) {
        this.keyuser = keyuser;
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active.
     *
     * @param active
     *         the new active
     */
    public void setActive( boolean active ) {
        this.active = active;
    }

    /**
     * Checks if is internal.
     *
     * @return true, if is internal
     */
    public boolean isInternal() {
        return internal;
    }

    /**
     * Sets the internal.
     *
     * @param internal
     *         the new internal
     */
    public void setInternal( boolean internal ) {
        this.internal = internal;
    }

    /**
     * Checks if is private.
     *
     * @return true, if is private
     */
    public boolean getIsPrivateWorkflow() {
        return isPrivateWorkflow;
    }

    /**
     * Sets the private.
     *
     * @param isPrivateWorkflow
     *         the new checks if is private workflow
     */
    public void setIsPrivateWorkflow( boolean isPrivateWorkflow ) {
        this.isPrivateWorkflow = isPrivateWorkflow;
    }

    /**
     * Gets the definition.
     *
     * @return the definition
     */
    public byte[] getDefinition() {
        return definition;
    }

    /**
     * Sets the definition.
     *
     * @param definition
     *         the new definition
     */
    public void setDefinition( byte[] definition ) {
        this.definition = definition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVersionId() {

        return getComposedId().getVersionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVersionId( int id ) {
        getComposedId().setVersionId( id );

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
     * Sets the status.
     *
     * @param status
     *         the new status
     */
    public void setStatus( int status ) {
        this.status = status;
    }

    /**
     * Gets the run on location.
     *
     * @return the run on location
     */
    public List< LocationEntity > getRunOnLocation() {
        return runOnLocation;
    }

    /**
     * Gets the locations.
     *
     * @return the locations
     */
    public String getLocations() {
        return locations;
    }

    /**
     * Sets the locations.
     *
     * @param locations
     *         the new locations
     */
    public void setLocations( String locations ) {
        this.locations = locations;
    }

    /**
     * Sets the run on location.
     *
     * @param runOnLocation
     *         the new run on location
     */
    public void setRunOnLocation( List< LocationEntity > runOnLocation ) {
        this.runOnLocation = runOnLocation;
    }

    /**
     * Gets the definition.
     *
     * @return the definition
     */
    public List< String > getCustomFlags() {
        return customFlags;
    }

    /**
     * Sets the definition.
     *
     * @param definition
     *         the new definition
     */
    public void setCustomFlags( List< String > customFlags ) {
        this.customFlags = customFlags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( getComposedId() == null ) ? 0 : getComposedId().hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        WorkflowEntity other = ( WorkflowEntity ) obj;
        if ( getComposedId() == null ) {
            if ( other.getComposedId() != null ) {
                return false;
            }
        } else if ( !getComposedId().equals( other.getComposedId() ) ) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "WorkflowEntity [id='" + getComposedId().getId() + "', versionId='" + getComposedId().getVersionId() + "', name='"
                + getName() + "', versionLabel='" + versionLabel + "', status='" + status + "']";
    }

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    public String getComments() {
        return comments;
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

}
