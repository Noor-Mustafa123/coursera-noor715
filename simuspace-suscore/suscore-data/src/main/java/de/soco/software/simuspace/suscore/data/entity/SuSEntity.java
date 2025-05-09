/*
 *
 */

package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;

/**
 * This class serves as base entity for all objects which have versions in the system <br> and implements some methods to establish object
 * uniqueness across the system.
 *
 * @author Nosheen.Sharif
 */
@Getter
@Setter
@Entity
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn( name = "classname" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class SuSEntity implements Versionable, Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -903538240261576956L;

    /**
     * The Constant FIELD_NAME_MODIFIED_ON.
     */
    public static final String FIELD_NAME_MODIFIED_ON = "modifiedOn";

    /**
     * id of the object. versioning composite primary key reference
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
    @Column( name = "updated_on" )
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
    private UserEntity modifiedBy;

    @OneToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinTable( name = "susEntity_translation", joinColumns = { @JoinColumn( name = "susentity_id", referencedColumnName = "id" ),
            @JoinColumn( name = "susentity_version_id", referencedColumnName = "version_id" ) }, inverseJoinColumns = {
            @JoinColumn( name = "translation_id", referencedColumnName = "id" ) } )
    private Set< TranslationEntity > translation;

    /**
     * The selected translations.
     */
    private String selectedTranslations;

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
     * The path.
     */
    @Column( name = "path" )
    private String path;

    /**
     * The icon.
     */
    @Column
    private String icon;

    /**
     * The size.
     */
    @Column( name = "entity_size" )
    private Long entitySize;

    /**
     * The link.
     */
    @Column
    private String link;

    /**
     * isDeleted.
     */
    @Column( name = "is_auto_delete" )
    private Boolean isAutoDelete = false;

    /**
     * The id.
     */
    @Column
    @Type( type = "uuid-char" )
    private UUID indexId = UUID.randomUUID();

    /**
     * The users.
     */
    // @Field ( analyze = Analyze.NO, store = Store.YES )
    // @FieldBridge ( impl = AccessUsersBridge.class )
    @ElementCollection
    private List< UUID > users = new ArrayList<>();

    /**
     * The groups.
     */
    // @Field ( analyze = Analyze.NO, store = Store.YES )
    // @FieldBridge ( impl = AccessGroupsBridge.class )
    @ElementCollection
    private List< UUID > groups = new ArrayList<>();

    /**
     * The hidden.
     */
    @Column( name = "is_hidden" )
    private Boolean hidden = false;

    /**
     * Instantiates a new Sus entity.
     *
     * @param composedId
     *         the composed id
     * @param lifeCycleStatus
     *         the life cycle status
     * @param config
     *         the config
     * @param typeId
     *         the type id
     * @param createdOn
     *         the created on
     * @param modifiedOn
     *         the modified on
     * @param createdBy
     *         the created by
     * @param modifiedBy
     *         the modified by
     * @param name
     *         the name
     * @param description
     *         the description
     * @param entitySize
     *         the entity size
     * @param icon
     *         the icon
     */
    public SuSEntity( VersionPrimaryKey composedId, String lifeCycleStatus, String config, UUID typeId, Date createdOn, Date modifiedOn,
            UserEntity createdBy, UserEntity modifiedBy, String name, String description, Long entitySize, String icon,
            String selectedTranslations ) {
        this.composedId = composedId;
        this.lifeCycleStatus = lifeCycleStatus;
        this.config = config;
        this.typeId = typeId;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.name = name;
        this.description = description;
        this.entitySize = entitySize;
        this.icon = icon;
        this.selectedTranslations = selectedTranslations;
    }

    /**
     * Instantiates a new Sus entity.
     */
    public SuSEntity() {
        super();
    }

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

    /**
     * Hash code.
     *
     * @return the int
     */
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

    /**
     * Equals.
     *
     * @param obj
     *         the obj
     *
     * @return true, if successful
     */
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

    /**
     * Gets the size.
     *
     * @return the size
     */
    public Long getSize() {
        return entitySize;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( Long size ) {
        this.entitySize = size;
    }

    /**
     * Checks if is auto delete.
     *
     * @return true, if is auto delete
     */
    public Boolean isAutoDelete() {
        return isAutoDelete;
    }

    /**
     * Sets the auto delete.
     *
     * @param isAutoDelete
     *         the new auto delete
     */
    public void setAutoDelete( Boolean isAutoDelete ) {
        this.isAutoDelete = isAutoDelete;
    }

    /**
     * Checks if is hidden.
     *
     * @return true, if is hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Sets the hidden.
     *
     * @param hidden
     *         the new hidden
     */
    public void setHidden( boolean hidden ) {
        this.hidden = hidden;
    }

    @Override
    public UUID getId() {
        if ( composedId != null ) {
            return composedId.getId();
        }
        return null;
    }

    @Override
    public void setId( UUID id ) {
        if ( composedId == null ) {
            composedId = new VersionPrimaryKey();
        }
        composedId.setId( id );
    }

}
