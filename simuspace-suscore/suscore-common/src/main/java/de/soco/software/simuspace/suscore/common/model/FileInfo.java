package de.soco.software.simuspace.suscore.common.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The class is responsible to hold file informations.
 *
 * @author Huzaifah.Mubashir
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class FileInfo {

    /**
     * The id.
     */
    private String id;

    /**
     * The parent id.
     */
    private UUID parentId;

    /**
     * The name.
     */
    private String name;

    /**
     * The hash.
     */
    private String hash;

    /**
     * The hash list.
     */
    private List< String > hashList;

    /**
     * The dir.
     */
    private boolean dir;

    /**
     * The type.
     */
    private String type;

    /**
     * The type id.
     */
    private UUID typeId;

    /**
     * The created on.
     */
    private Date createdOn;

    /**
     * The file.
     */
    private DocumentDTO file;

    /**
     * The checked out.
     */
    private Boolean checkedOut;

    /**
     * The checked out same.
     */
    private Boolean checkedOutSame;

    /**
     * The checked out user.
     */
    private UserDTO checkedOutUser;

    /**
     * The lifecycle status.
     */
    private StatusDTO lifeCycleStatus;

    /**
     * The icon.
     */
    private String icon;

    /**
     * The abort.
     */
    private boolean abort = false;

    /**
     * The description.
     */
    private String description;

    /**
     * The size.
     */
    private String size;

    /**
     * The link.
     */
    private String link;

    /**
     * The modified on.
     */
    private Date modifiedOn;

    /**
     * The created by.
     */
    private UserDTO createdBy;

    /**
     * The modified by.
     */
    private UserDTO modifiedBy;

    /**
     * The url type.
     */
    private String urlType;

    /**
     * Version Dto Reference.
     */
    private VersionDTO version;

    /**
     * The auto deleted.
     */
    private Boolean autoDeleted;

    /**
     * The location.
     */
    List< CommonLocationDTO > location;

    /**
     * The additional files.
     */
    private List< DocumentDTO > additionalFiles;

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
     * Gets the checked out.
     *
     * @return the checked out
     */
    public Boolean getCheckedOut() {
        return checkedOut;
    }

    /**
     * Sets the checked out.
     *
     * @param checkedOut
     *         the new checked out
     */
    public void setCheckedOut( Boolean checkedOut ) {
        this.checkedOut = checkedOut;
    }

    /**
     * Gets the checked out user.
     *
     * @return the checkedOutUser
     */
    public UserDTO getCheckedOutUser() {
        return checkedOutUser;
    }

    /**
     * Sets the checked out user.
     *
     * @param checkedOutUser
     *         the checkedOutUser to set
     */
    public void setCheckedOutUser( UserDTO checkedOutUser ) {
        this.checkedOutUser = checkedOutUser;
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
     * Gets the hash.
     *
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the hash.
     *
     * @param hash
     *         the new hash
     */
    public void setHash( String hash ) {
        this.hash = hash;
    }

    /**
     * Instantiates a new file info.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param hash
     *         the hash
     * @param dir
     *         the dir
     * @param type
     *         the type
     * @param createdOn
     *         the created on
     */
    public FileInfo( String id, String name, String hash, boolean dir, String type, Date createdOn ) {
        super();
        this.id = id;
        this.name = name;
        this.hash = hash;
        this.dir = dir;
        this.type = type;
        this.createdOn = createdOn;
    }

    /**
     * Instantiates a new file info.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param hash
     *         the hash
     * @param dir
     *         the dir
     * @param type
     *         the type
     * @param createdOn
     *         the created on
     * @param size
     *         the size
     */
    public FileInfo( String id, String name, String hash, boolean dir, String type, Date createdOn, String size ) {
        super();
        this.id = id;
        this.name = name;
        this.hash = hash;
        this.dir = dir;
        this.type = type;
        this.createdOn = createdOn;
        this.size = size;
    }

    /**
     * Instantiates a new file info.
     *
     * @param name
     *         the name
     */
    public FileInfo( String name ) {
        super();
        this.name = name;
    }

    /**
     * Instantiates a new file info.
     */
    public FileInfo() {
        super();
    }

    /**
     * Checks if is dir.
     *
     * @return true, if is dir
     */
    public boolean isDir() {
        return dir;
    }

    /**
     * Sets the dir.
     *
     * @param dir
     *         the new dir
     */
    public void setDir( boolean dir ) {
        this.dir = dir;
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
     * Gets the file.
     *
     * @return the file
     */
    public DocumentDTO getFile() {
        return file;
    }

    /**
     * Sets the file.
     *
     * @param file
     *         the new file
     */
    public void setFile( DocumentDTO file ) {
        this.file = file;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( hash == null ) ? 0 : hash.hashCode() );
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
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
    /*
     * (non-Javadoc)
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
        FileInfo other = ( FileInfo ) obj;
        if ( hash == null ) {
            if ( other.hash != null ) {
                return false;
            }
        } else if ( !hash.equals( other.hash ) ) {
            return false;
        }
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

    /**
     * To string.
     *
     * @return the string
     */
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FileInfo [id=" + id + ", name=" + name + ", hash=" + hash + ", hashList=" + hashList + ", dir=" + dir + ", type=" + type
                + ", createdOn=" + createdOn + ", file=" + file + ", checkedOut=" + checkedOut + ", checkedOutUser=" + checkedOutUser
                + ", lifecycleStatus=" + getLifeCycleStatus() + ", abort=" + abort + "]";
    }

    /**
     * Checks if is abort.
     *
     * @return true, if is abort
     */
    public boolean isAbort() {
        return abort;
    }

    /**
     * Sets the abort.
     *
     * @param abort
     *         the new abort
     */
    public void setAbort( boolean abort ) {
        this.abort = abort;
    }

    /**
     * Gets the hash list.
     *
     * @return the hash list
     */
    public List< String > getHashList() {
        return hashList;
    }

    /**
     * Sets the hash list.
     *
     * @param hashList
     *         the new hash list
     */
    public void setHashList( List< String > hashList ) {
        this.hashList = hashList;
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
     * Gets the lifecycle status.
     *
     * @return the lifecycle status
     */
    public StatusDTO getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    /**
     * Sets the lifecycle status.
     *
     * @param lifeCycleStatus
     *         the new life cycle status
     */
    public void setLifeCycleStatus( StatusDTO lifeCycleStatus ) {
        this.lifeCycleStatus = lifeCycleStatus;
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
     * Gets the modified on.
     *
     * @return the modified on
     */
    public Date getModifiedOn() {
        return modifiedOn;
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
     * Gets the checked out same.
     *
     * @return the checked out same
     */
    public Boolean getCheckedOutSame() {
        return checkedOutSame;
    }

    /**
     * Sets the checked out same.
     *
     * @param checkedOutSame
     *         the new checked out same
     */
    public void setCheckedOutSame( Boolean checkedOutSame ) {
        this.checkedOutSame = checkedOutSame;
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
     * Checks if is auto deleted.
     *
     * @return true, if is auto deleted
     */
    public Boolean isAutoDeleted() {
        return autoDeleted;
    }

    /**
     * Sets the auto deleted.
     *
     * @param autoDeleted
     *         the new auto deleted
     */
    public void setAutoDeleted( Boolean autoDeleted ) {
        this.autoDeleted = autoDeleted;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public List< CommonLocationDTO > getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location
     *         the new location
     */
    public void setLocation( List< CommonLocationDTO > location ) {
        this.location = location;
    }

    /**
     * Gets the additional files.
     *
     * @return the additional files
     */
    public List< DocumentDTO > getAdditionalFiles() {
        return additionalFiles;
    }

    /**
     * Sets the additional files.
     *
     * @param additional
     *         files the new additional files
     */
    public void setAdditionalFiles( List< DocumentDTO > additionalFiles ) {
        this.additionalFiles = additionalFiles;
    }

}
