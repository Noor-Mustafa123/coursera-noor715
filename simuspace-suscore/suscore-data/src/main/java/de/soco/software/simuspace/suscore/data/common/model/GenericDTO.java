package de.soco.software.simuspace.suscore.data.common.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;

/**
 * This class is responsible for data transfer of generic attributes as object.
 */
public class GenericDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -332577885307007363L;

    /**
     * The Constant GENERIC_DTO_TYPE.
     */
    public static final String GENERIC_DTO_TYPE = "32ab43d4-d062-460b-b4aa-e1a4d9252001";

    /**
     * The id.
     */
    @UIFormField( name = "parentId", title = "3000041x4", type = "hidden" )
    @UIColumn( data = "parentId", name = "parentId", filter = "uuid", renderer = "hidden", title = "3000041x4", type = "hidden", isShow = false, orderNum = 5 )
    private UUID parentId;

    /**
     * The object type id.
     */
    private UUID typeId;

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000051x4", isAsk = false )
    @UIColumn( data = "type", name = "type", filter = "text", renderer = "replaceText", options = "<i class='{icon}'></i> {type}", title = "3000051x4", orderNum = 3 )
    private String type;

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false )
    @UIColumn( data = "id", name = "id", filter = "uuid", renderer = "text", title = "3000021x4", orderNum = 14 )
    private UUID id;

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4" )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000011x4", orderNum = 2 )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 2 )
    private String description;

    /**
     * The created on.
     */
    @UIFormField( name = "createdOn", title = "3000008x4", isAsk = false, type = "date")
    @UIColumn( data = "createdOn", filter = "dateRange", renderer = "date", title = "3000008x4", name = "createdOn", orderNum = 10 )
    private Date createdOn;

    /**
     * The updated on.
     */
    @UIFormField( name = "modifiedOn", title = "3000053x4", isAsk = false, type = "date")
    @UIColumn( data = "modifiedOn", filter = "dateRange", renderer = "date", title = "3000053x4", name = "modifiedOn", orderNum = 12 )
    private Date modifiedOn;

    /**
     * The updated on.
     */
    @UIFormField( name = "lifeCycleStatus.name", title = "3000066x4", isAsk = false )
    @UIColumn( data = "lifeCycleStatus.name", filter = "", renderer = "text", title = "3000066x4", name = "lifeCycleStatus", orderNum = 5 )
    private StatusDTO lifeCycleStatus;

    /**
     * The created on.
     */
    @UIFormField( name = "createdBy.userUid", title = "3000064x4", isAsk = false, orderNum = 11 )
    @UIColumn( data = "createdBy.userUid", tooltip = "{createdBy.userName}", name = "createdBy.userUid", filter = "text", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", orderNum = 11 )
    private UserDTO createdBy;

    /**
     * The modified on.
     */
    @UIFormField( name = "modifiedBy.userUid", title = "3000065x4", isAsk = false, orderNum = 13 )
    @UIColumn( data = "modifiedBy.userUid", tooltip = "{modifiedBy.userName}", name = "modifiedBy.userUid", filter = "text", url = "system/user/{modifiedBy.id}", renderer = "link", title = "3000065x4", orderNum = 13 )
    private UserDTO modifiedBy;

    /**
     * The link.
     */
    @UIFormField( name = "link", title = "3000121x4", isAsk = false, orderNum = 7 )
    @UIColumn( data = "link", filter = "", renderer = "text", title = "3000121x4", name = "link", isSortable = false, orderNum = 7 )
    private String link;

    /**
     * The size.
     */
    @UIFormField( name = "size", title = "3000123x4", isAsk = false, orderNum = 6 )
    @UIColumn( data = "size", filter = "", renderer = "text", title = "3000123x4", name = "entitySize", orderNum = 6 )
    private String size;

    /**
     * The autoDeleted.
     */
    @UIColumn( data = "autoDeleted", filter = "text", renderer = "text", title = "3000149x4", name = "autoDeleted", isShow = false, orderNum = 30 )
    private boolean autoDeleted;

    /**
     * Version Dto Reference.
     */
    private VersionDTO version;

    /**
     * The url type.
     */
    private String urlType;

    /**
     * The icon.
     */
    private String icon;

    /**
     * The users.
     */
    private List< UUID > users = new ArrayList<>();

    /**
     * The groups.
     */
    private List< UUID > groups = new ArrayList<>();

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
     * Gets the parent id.
     *
     * @return the parent id
     */
    public UUID getParentId() {
        return parentId;
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
     * Sets the parent id.
     *
     * @param parentId
     *         the new parent id
     */
    public void setParentId( UUID parentId ) {
        this.parentId = parentId;
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( createdOn == null ) ? 0 : createdOn.hashCode() );
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( parentId == null ) ? 0 : parentId.hashCode() );
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
        GenericDTO other = ( GenericDTO ) obj;

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
        if ( parentId == null ) {
            if ( other.parentId != null ) {
                return false;
            }
        } else if ( !parentId.equals( other.parentId ) ) {
            return false;
        }

        return true;
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
     * Gets the users.
     *
     * @return the users
     */
    public List< UUID > getUsers() {
        return users;
    }

    /**
     * Sets the users.
     *
     * @param users
     *         the new users
     */
    public void setUsers( List< UUID > users ) {
        this.users = users;
    }

    /**
     * Gets the groups.
     *
     * @return the groups
     */
    public List< UUID > getGroups() {
        return groups;
    }

    /**
     * Sets the groups.
     *
     * @param groups
     *         the new groups
     */
    public void setGroups( List< UUID > groups ) {
        this.groups = groups;
    }

}