package de.soco.software.simuspace.suscore.common.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A pojo class for mapping sync file object to json and back to class.
 *
 * @author Huzaifah.Mubashir
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SyncFileDTO {

    /**
     * The syncStatus.
     */
    @UIFormField( name = "syncStatus", title = "3000071x4", orderNum = 4 )
    @UIColumn( data = "syncStatus", name = "syncStatus", filter = "text", renderer = "text", title = "3000071x4", orderNum = 4 )
    private String syncStatus;

    /**
     * The name.
     */
    @UIFormField( name = "name", title = "3000032x4", orderNum = 1 )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The type id.
     */
    @UIFormField( name = "typeId", title = "3000037x4", type = "hidden" )
    @UIColumn( data = "typeId", name = "typeId", filter = "uuid", renderer = "hidden", title = "3000037x4", type = "hidden", isShow = false )
    private UUID typeId;

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000051x4", orderNum = 3 )
    @UIColumn( data = "type", name = "type", filter = "text", renderer = "replaceText", options = "<i class='{icon}'></i> {type}", title = "3000051x4", orderNum = 3 )
    private String type;

    /**
     * The created on.
     */
    @UIFormField( name = "createdBy.userUid", title = "3000064x4", isAsk = false, orderNum = 4 )
    @UIColumn( data = "createdBy.userUid", filter = "text", renderer = "date", title = "3000064x4", name = "createdBy", orderNum = 4, isShow = false )
    private UserDTO createdBy;

    /**
     * The updated on.
     */
    @UIFormField( name = "modifiedBy.userUid", title = "3000065x4", isAsk = false, orderNum = 5 )
    @UIColumn( data = "modifiedBy.userUid", filter = "text", renderer = "text", title = "3000065x4", name = "modifiedBy", orderNum = 5, isShow = false )
    private UserDTO modifiedBy;

    /**
     * The created on.
     */
    @UIFormField( name = "createdOn", title = "3000008x4", isAsk = false, orderNum = 6, type = "date")
    @UIColumn( data = "createdOn", filter = "dateRange", renderer = "date", title = "3000008x4", name = "createdOn", orderNum = 6 )
    private Date createdOn;

    /**
     * The updated on.
     */
    @UIFormField( name = "modifiedOn", title = "3000053x4", isAsk = false, orderNum = 7, type = "date")
    @UIColumn( data = "modifiedOn", filter = "dateRange", renderer = "date", title = "3000053x4", name = "modifiedOn", orderNum = 7 )
    private Date modifiedOn;

    /**
     * The checkedOut.
     */
    @UIFormField( name = "checkedOut", title = "3000088x4", orderNum = 8 )
    @UIColumn( data = "checkedOut", name = "checkedOut", filter = "", renderer = "text", title = "3000088x4", orderNum = 8 )
    private String checkedOut;

    /**
     * The updated on.
     */
    @UIFormField( name = "lifeCycleStatus.name", title = "3000066x4", isAsk = false )
    @UIColumn( data = "lifeCycleStatus.name", filter = "text", renderer = "text", title = "3000066x4", name = "lifeCycleStatus", orderNum = 10 )
    private StatusDTO lifeCycleStatus;

    /**
     * The checkedoutUser.
     */
    @UIFormField( name = "checkedOutUser.userUid", title = "3000087x4", orderNum = 9 )
    @UIColumn( data = "checkedOutUser.userUid", name = "checkedOutUser.userUid", filter = "", url = "system/user/{checkedOutUser.id}", renderer = "link", title = "3000087x4", orderNum = 9 )
    private UserDTO checkedOutUser;

    /**
     * The checkedoutUser.
     */
    @UIFormField( name = "size", title = "3000123x4", orderNum = 10 )
    @UIColumn( data = "size", name = "entitySize", filter = "text", renderer = "text", title = "3000123x4", orderNum = 10 )
    private String size;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000011x4", orderNum = 11 )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 11 )
    private String description;

    /**
     * The autoDeleted
     */
    @UIColumn( data = "autoDeleted", filter = "text", renderer = "text", title = "3000149x4", name = "autoDeleted", isShow = false, orderNum = 30 )
    private boolean autoDeleted;

    /**
     * The id.
     */
    private String id;

    /**
     * The file id.
     */
    private String fileId;

    /**
     * The icon.
     */
    private String icon;

    /**
     * The link.
     */
    private String link;

    /**
     * The url type.
     */
    private String urlType;

    /**
     * Version Dto Reference.
     */
    private VersionDTO version;

    /**
     * The parent id.
     */
    private UUID parentId;

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
     * Gets the auto deleted.
     *
     * @return the auto deleted
     */
    public boolean getAutoDeleted() {
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
    public UserDTO getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the created by.
     *
     * @param createdBy
     *         the new created by
     */
    public void setCreatedBy( UserDTO createdBy ) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the updated by.
     *
     * @return the updated by
     */
    public UserDTO getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets the updated by.
     *
     * @param updatedBy
     *         the new updated by
     */
    public void setModifiedBy( UserDTO updatedBy ) {
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

    public UserDTO getCheckedOutUser() {
        return checkedOutUser;
    }

    public void setCheckedOutUser( UserDTO checkedOutUser ) {
        this.checkedOutUser = checkedOutUser;
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( createdBy == null ) ? 0 : createdBy.hashCode() );
        result = prime * result + ( ( createdOn == null ) ? 0 : createdOn.hashCode() );
        result = prime * result + ( ( modifiedBy == null ) ? 0 : modifiedBy.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( syncStatus == null ) ? 0 : syncStatus.hashCode() );
        result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
        result = prime * result + ( ( typeId == null ) ? 0 : typeId.hashCode() );
        result = prime * result + ( ( modifiedOn == null ) ? 0 : modifiedOn.hashCode() );
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
        SyncFileDTO other = ( SyncFileDTO ) obj;
        if ( createdBy == null ) {
            if ( other.createdBy != null ) {
                return false;
            }
        } else if ( !createdBy.equals( other.createdBy ) ) {
            return false;
        }
        if ( createdOn == null ) {
            if ( other.createdOn != null ) {
                return false;
            }
        } else if ( !createdOn.equals( other.createdOn ) ) {
            return false;
        }
        if ( modifiedBy == null ) {
            if ( other.modifiedBy != null ) {
                return false;
            }
        } else if ( !modifiedBy.equals( other.modifiedBy ) ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        if ( syncStatus == null ) {
            if ( other.syncStatus != null ) {
                return false;
            }
        } else if ( !syncStatus.equals( other.syncStatus ) ) {
            return false;
        }
        if ( type == null ) {
            if ( other.type != null ) {
                return false;
            }
        } else if ( !type.equals( other.type ) ) {
            return false;
        }
        if ( typeId == null ) {
            if ( other.typeId != null ) {
                return false;
            }
        } else if ( !typeId.equals( other.typeId ) ) {
            return false;
        }
        if ( modifiedOn == null ) {
            if ( other.modifiedOn != null ) {
                return false;
            }
        } else if ( !modifiedOn.equals( other.modifiedOn ) ) {
            return false;
        }
        return true;
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
     * Gets the size.
     *
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( String size ) {
        this.size = size;
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

}
