package de.soco.software.simuspace.suscore.common.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;

/**
 * This abstract class serves as base entity for all objects which have versions in the system <br> and implements some methods to establish
 * object uniqueness across the system.
 *
 * @author Nosheen.Sharif
 */
@Getter
@Setter
public abstract class SuSEntity implements Versionable, Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -903538240261576956L;

    /**
     * The Constant FIELD_NAME_MODIFIED_ON.
     */
    public static final String FIELD_NAME_MODIFIED_ON = "modifiedOn";

    /** id of the object. */
    /**
     * versioning composite primary key reference
     */
    @EmbeddedId
    private VersionPrimaryKey composedId;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

    /**
     * status id of an object.
     */
    @Column( name = "lifecycle_status" )
    private String lifeCycleStatus;

    /**
     * The description.
     */
    @Column( name = "description", length = 1024 )
    private String description;

    /**
     * The config.
     */
    @Column( name = "config" )
    private String config;

    /**
     * The object type id.
     */
    @Column( name = "typeid" )
    @Type( type = "uuid-char" )
    private UUID typeId;

    /**
     * the One to Many relationship with object metadata.
     */
    @OneToMany( fetch = FetchType.LAZY, mappedBy = "susEntity", cascade = CascadeType.ALL )
    private Set< CustomAttributeEntity > customAttributes = new HashSet<>( ConstantsInteger.INTEGER_VALUE_ZERO );

    /**
     * The meta data document.
     */
    @OneToOne( fetch = FetchType.EAGER )
    private DocumentEntity metaDataDocument;

    /**
     * The meta data document.
     */
    @Column( name = "job_workflow_id", nullable = true )
    @Type( type = "uuid-char" )
    private UUID workflowId;

    /**
     * Creation date of object.
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * modification date of object.
     */
    @Column( name = "modified_on" )
    private Date modifiedOn;

    /**
     * isDeleted.
     */
    @Column( name = "is_delete" )
    private boolean isDelete;

    /**
     * auditLogEntity mapping.
     */
    @ManyToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private AuditLogEntity auditLogEntity;

    /**
     * owner mapping.
     */
    @ManyToOne
    private UserEntity owner;

    /**
     * The user selection id.
     */
    @Column( name = "perm_user_selection_id" )
    private String userSelectionId;

    /**
     * The group selection id.
     */
    @Column( name = "perm_group_selection_id" )
    private String groupSelectionId;

    /**
     * The deleted by.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "deleted_by_user_id", referencedColumnName = "id" )
    private UserEntity deletedBy;

    /**
     * who created the workflow.
     */
    // make it eager as we need user information with workflow
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "created_by", referencedColumnName = "id" )
    private UserEntity createdBy;

    /**
     * who updated the workflow.
     */
    // make it eager as we need user information with workflow
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "updated_by", referencedColumnName = "id" )
    private UserEntity updatedBy;

    /**
     * The deleted on.
     */
    @Column( name = "deleted_on" )
    private Date deletedOn;

    /**
     * The object type id.
     */
    @Column( name = "jobid" )
    @Type( type = "uuid-char" )
    private UUID jobId;

    /**
     * The icon.
     */
    @Column
    String icon;

    /**
     * The size.
     */
    @Column
    private String size;

    /**
     * Gets the composed id.
     *
     * @return the composed id
     */
    public VersionPrimaryKey getComposedId() {

        return composedId == null ? new VersionPrimaryKey() : composedId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVersionId() {

        return composedId.getVersionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVersionId( int id ) {
        composedId.setVersionId( id );

    }

    /**
     * Checks if is delete.
     *
     * @return the isDelete
     */
    public boolean isDelete() {
        return isDelete;
    }

    /**
     * Sets the delete.
     *
     * @param isDelete
     *         the isDelete to set
     */
    public void setDelete( boolean isDelete ) {
        this.isDelete = isDelete;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( composedId == null ) ? 0 : composedId.hashCode() );
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
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
        SuSEntity other = ( SuSEntity ) obj;
        if ( composedId == null ) {
            if ( other.composedId != null ) {
                return false;
            }
        } else if ( !composedId.equals( other.composedId ) ) {
            return false;
        }
        return true;
    }

}
