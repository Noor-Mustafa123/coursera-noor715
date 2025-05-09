package de.soco.software.simuspace.suscore.local.daemon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.CustomAttributeDTO;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;

;

/**
 * The Class AllObjectEntity for sqlite database to map entity.
 *
 * @author Ali.Haider
 */
@Entity
@JsonIgnoreProperties( ignoreUnknown = true )
@Table( name = "all_objects" )
public class AllObjectEntity {

    /**
     * The id.
     */
    @Id
    @Column( name = "id" )
    private String id;

    /**
     * The id.
     */
    @Column( name = "parentId" )
    private UUID parentId;

    /**
     * The syncStatus.
     */
    @Column( name = "syncStatus" )
    private String syncStatus;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

    /**
     * The type id.
     */
    @Column( name = "typeId" )
    private UUID typeId;

    /**
     * The type.
     */
    @Column( name = "type" )
    private String type;

    /**
     * The created by.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "createdBy", referencedColumnName = "id" )
    private UserDaemonEntity createdBy;

    /**
     * The updated by.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "modifiedBy", referencedColumnName = "id" )
    private UserDaemonEntity modifiedBy;

    /**
     * The created on.
     */
    @Column( name = "createdOn" )
    private Date createdOn;

    /**
     * The updated on.
     */
    @Column( name = "modifiedOn" )
    private Date modifiedOn;

    /**
     * The checkedOut.
     */
    @Column( name = "checkedOut" )
    private String checkedOut;

    /**
     * The updated on.
     */
    @Column( name = "lifeCycleStatus" )
    private StatusDTO lifeCycleStatus;

    /**
     * The checkedoutUser.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "checkedOutUser", referencedColumnName = "id" )
    private UserDaemonEntity checkedOutUser;

    /**
     * The description.
     */
    @Column( name = "description" )
    private String description;

    /**
     * The size.
     */
    @Column( name = "entitySize" )
    private Long size;

    /**
     * The job id.
     */
    @Column( name = "jobid" )
    private UUID jobId;

    /**
     * The custom attributes DTO.
     */
    @Column( name = "custom_attrib" )
    private CustomAttributeDTO customAttributesDTO;

    /**
     * The custom attributes.
     */
    @Column( name = "customAttribute" )
    private String customAttributes;

    /**
     * The file id.
     */
    @Column
    private String fileId;

    /**
     * The icon.
     */
    @Column
    private String icon;

    /**
     * Version Dto Reference.
     */
    @Column
    private VersionDTO version;

    /**
     * The url type.
     */
    @Column
    private String urlType;

    /**
     * The link.
     */
    @Column
    private String link;

    /**
     * The locations.
     */
    @OneToMany
    @JoinTable( name = "object_locations" )
    private List< LocationDTO > locations;

    /**
     * The auto deleted.
     */
    @Column
    private boolean autoDeleted;

    /**
     * Checks if is auto deleted.
     *
     * @return true, if is auto deleted
     */
    public boolean isAutoDeleted() {
        return autoDeleted;
    }

    /**
     * Sets the auto deleted.
     *
     * @param autoDeleted
     *         the new auto deleted
     */
    public void setAutoDeleted( boolean autoDeleted ) {
        this.autoDeleted = autoDeleted;
    }

    /**
     * Gets the sync status.
     *
     * @return the sync status
     */
    public String getSyncStatus() {
        return syncStatus;
    }

    /**
     * Sets the sync status.
     *
     * @param syncStatus
     *         the new sync status
     */
    public void setSyncStatus( String syncStatus ) {
        this.syncStatus = syncStatus;
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
     * Gets the type id.
     *
     * @return the type id
     */
    public UUID getTypeId() {
        return typeId;
    }

    /**
     * Sets the type id.
     *
     * @param typeId
     *         the new type id
     */
    public void setTypeId( UUID typeId ) {
        this.typeId = typeId;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets the created by.
     *
     * @return the created by
     */
    public UserDaemonEntity getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the created by.
     *
     * @param createdBy
     *         the new created by
     */
    public void setCreatedBy( UserDaemonEntity createdBy ) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the modified by.
     *
     * @return the modified by
     */
    public UserDaemonEntity getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets the modified by.
     *
     * @param updatedBy
     *         the new modified by
     */
    public void setModifiedBy( UserDaemonEntity updatedBy ) {
        this.modifiedBy = updatedBy;
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
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Gets the updated on.
     *
     * @return the updated on
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * Sets the updated on.
     *
     * @param updatedOn
     *         the new updated on
     */
    public void setModifiedOn( Date updatedOn ) {
        this.modifiedOn = updatedOn;
    }

    /**
     * Gets the checked out.
     *
     * @return the checked out
     */
    public String getCheckedOut() {
        return checkedOut;
    }

    /**
     * Sets the checked out.
     *
     * @param checkedOut
     *         the new checked out
     */
    public void setCheckedOut( String checkedOut ) {
        this.checkedOut = checkedOut;
    }

    /**
     * Gets the life cycle status.
     *
     * @return the life cycle status
     */
    public StatusDTO getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    /**
     * Sets the life cycle status.
     *
     * @param lifeCycleStatus
     *         the new life cycle status
     */
    public void setLifeCycleStatus( StatusDTO lifeCycleStatus ) {
        this.lifeCycleStatus = lifeCycleStatus;
    }

    /**
     * @return the checkedOutUser
     */
    public UserDaemonEntity getCheckedOutUser() {
        return checkedOutUser;
    }

    /**
     * @param checkedOutUser
     *         the checkedOutUser to set
     */
    public void setCheckedOutUser( UserDaemonEntity checkedOutUser ) {
        this.checkedOutUser = checkedOutUser;
    }

    /**
     * Gets the file id.
     *
     * @return the file id
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * Sets the file id.
     *
     * @param fileId
     *         the new file id
     */
    public void setFileId( String fileId ) {
        this.fileId = fileId;
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon
     *         the new icon
     */
    public void setIcon( String icon ) {
        this.icon = icon;
    }

    /**
     * Gets the url type.
     *
     * @return the url type
     */
    public String getUrlType() {
        return urlType;
    }

    /**
     * Sets the url type.
     *
     * @param urlType
     *         the new url type
     */
    public void setUrlType( String urlType ) {
        this.urlType = urlType;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *         the new version
     */
    public void setVersion( VersionDTO version ) {
        this.version = version;
    }

    /**
     * Gets the parent id.
     *
     * @return the parent id
     */
    public UUID getParentId() {
        return parentId;
    }

    /**
     * Sets the parent id.
     *
     * @param parentId
     *         the new parent id
     */
    public void setParentId( UUID parentId ) {
        this.parentId = parentId;
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
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets the link.
     *
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the link.
     *
     * @param link
     *         the new link
     */
    public void setLink( String link ) {
        this.link = link;
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AllObjectEntity [id=" + id + ", parentId=" + parentId + ", syncStatus=" + syncStatus + ", name=" + name + ", typeId="
                + typeId + ", type=" + type + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdOn=" + createdOn
                + ", updatedOn=" + modifiedOn + ", checkedOut=" + checkedOut + ", lifeCycleStatus=" + lifeCycleStatus + ", checkedOutUser="
                + checkedOutUser + ", fileId=" + fileId + ", icon=" + icon + ", version=" + version + ", urlType=" + urlType + "]";
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( Long size ) {
        this.size = size;
    }

    /**
     * Gets the locations.
     *
     * @return the locations
     */
    public List< LocationDTO > getLocations() {
        return locations;
    }

    /**
     * Sets the locations.
     *
     * @param locations
     *         the new locations
     */
    public void setLocations( List< LocationDTO > locations ) {
        this.locations = locations;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
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
        AllObjectEntity other = ( AllObjectEntity ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        return true;
    }

    /**
     * Gets the custom attributes.
     *
     * @return the custom attributes
     */
    public String getCustomAttributes() {
        return customAttributes;
    }

    /**
     * Sets the custom attributes.
     *
     * @param customAttributes
     *         the new custom attributes
     */
    public void setCustomAttributes( String customAttributes ) {
        this.customAttributes = customAttributes;
    }

}
