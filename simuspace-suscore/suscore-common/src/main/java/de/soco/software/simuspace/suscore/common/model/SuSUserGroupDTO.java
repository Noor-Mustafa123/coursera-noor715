package de.soco.software.simuspace.suscore.common.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.BusinessObject;
import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The Class is responsible to map and provide user group payload.
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SuSUserGroupDTO extends BusinessObject {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 5826643153428413254L;

    /**
     * The Constant ACTIVE.
     */
    public static final int ACTIVE = 1;

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * The Constant DESCRIPTION_FIELD.
     */
    private static final String DESCRIPTION_FIELD = "Description";

    /**
     * Unique id for userGroup.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, orderNum = 9, type = "hidden" )
    @UIColumn( data = "id", filter = "uuid", renderer = "hidden", title = "3000021x4", name = "id", isShow = false, orderNum = 9 )
    private UUID id;

    /**
     * version for userGroup.
     */
    private transient VersionDTO version;

    /**
     * Name of user Group.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4", orderNum = 1 )
    @UIColumn( data = "name", filter = "text", renderer = "text", title = "3000032x4", name = "name", orderNum = 1, width = 0 )
    private String name;

    /**
     * Description of user Group.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 1024, message = "3100002x4" )
    @UIFormField( name = "description", type = "textarea", title = "3000011x4", orderNum = 2 )
    @UIColumn( data = "description", filter = "text", renderer = "text", title = "3000011x4", name = "description", orderNum = 2 )
    private String description;

    /**
     * Status of user Group (1=ACTIVE , 0=DISABLE).
     */
    @UIFormField( name = "status", title = "3000049x4", type = "select", orderNum = 3 )
    @UIColumn( data = "status", filter = "select", renderer = "text", title = "3000049x4", type = "select", name = "status", isSortable = false, orderNum = 3, filterOptions = {
            "Active", "Inactive" } )
    private String status;

    /**
     * The created by.
     */
    @UIFormField( name = "createdBy.userUid", title = "3000064x4", isAsk = false, orderNum = 6 )
    @UIColumn( data = "createdBy.userUid", name = "createdBy.userUid", filter = "text", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", tooltip = "{createdBy.userName}", orderNum = 6 )
    private UserDTO createdBy;

    /**
     * The modified by.
     */
    @UIFormField( name = "modifiedBy.userUid", title = "3000065x4", isAsk = false, orderNum = 8 )
    @UIColumn( data = "modifiedBy.userUid", name = "modifiedBy.userUid", url = "system/user/{modifiedBy.id}", filter = "text", renderer = "link", title = "3000065x4", tooltip = "{modifiedBy.userName}", orderNum = 8 )
    private UserDTO modifiedBy;

    /**
     * The created on Date/time.
     */
    @UIFormField( name = "createdOn", title = "3000008x4", isAsk = false, orderNum = 5, type = "date")
    @UIColumn( data = "createdOn", name = "createdOn", filter = "dateRange", renderer = "html", title = "3000008x4", orderNum = 5 )
    private String createdOn;

    /**
     * The updated on Date/time.
     */
    @UIFormField( name = "updatedOn", title = "3000053x4", isAsk = false, orderNum = 7, type = "date")
    @UIColumn( data = "updatedOn", name = "modifiedOn", filter = "dateRange", renderer = "html", title = "3000053x4", orderNum = 7 )
    private String updatedOn;

    /**
     * The users list.
     */
    @UIFormField( name = "users", title = "4100157x4", type = "table", orderNum = 4 )
    private List< UserDTO > users;

    /**
     * The users count.
     */
    @UIColumn( data = "usersCount", name = "users", filter = "", url = "view/system/groups/{id}/users", renderer = "link", title = "3000055x4", orderNum = 4, isSortable = false )
    private Integer usersCount;

    /**
     * flag to check if save or update operation id performed.
     */
    private boolean isUpdate;

    /**
     * The security identity.
     */
    private UUID securityIdentity;

    /**
     * The selectionid.
     */
    private String selectionId;

    /**
     * Instantiates a new Sus USerGroupDTO object.
     */
    public SuSUserGroupDTO() {
        super();
    }

    /**
     * Instantiates a new su S user group DTO.
     *
     * @param name
     *         the name
     * @param description
     *         the description
     * @param status
     *         the status
     * @param selectionId
     *         the selection id
     * @param usersId
     *         the users id
     */
    public SuSUserGroupDTO( String name, String description, String status, String selectionId ) {
        super();
        this.name = name;
        this.description = description;
        this.status = status;
        this.selectionId = selectionId;
        this.users = null;
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
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *         the new status
     */
    public void setStatus( String status ) {
        this.status = status;
    }

    /**
     * Gets the users.
     *
     * @return the users
     */
    public List< UserDTO > getUsers() {
        return users;
    }

    /**
     * Sets the users.
     *
     * @param users
     *         the new users
     */
    public void setUsers( List< UserDTO > users ) {
        this.users = users;
    }

    /**
     * Gets the created on.
     *
     * @return the created on
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( String createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Gets the updated on.
     *
     * @return the updated on
     */
    public String getUpdatedOn() {
        return updatedOn;
    }

    /**
     * Sets the updated on.
     *
     * @param updatedOn
     *         the new updated on
     */
    public void setUpdatedOn( String updatedOn ) {
        this.updatedOn = updatedOn;
    }

    /**
     * Checks if is update.
     *
     * @return the isUpdate
     */
    public boolean isUpdate() {
        return isUpdate;
    }

    /**
     * Sets the update.
     *
     * @param isUpdate
     *         the isUpdate to set
     */
    public void setUpdate( boolean isUpdate ) {
        this.isUpdate = isUpdate;
    }

    /**
     * Gets the version dto.
     *
     * @return the versionDto
     */
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * Sets the version dto.
     *
     * @param versionDto
     *         the versionDto to set
     */
    public void setVersion( VersionDTO versionDto ) {
        this.version = versionDto;
    }

    /**
     * Gets the security identity.
     *
     * @return the security identity
     */
    public UUID getSecurityIdentity() {
        return securityIdentity;
    }

    /**
     * Sets the security identity.
     *
     * @param securityIdentity
     *         the new security identity
     */
    public void setSecurityIdentity( UUID securityIdentity ) {
        this.securityIdentity = securityIdentity;
    }

    /**
     * Gets the selection id.
     *
     * @return the selection id
     */
    public String getSelectionId() {
        return selectionId;
    }

    /**
     * Sets the selection id.
     *
     * @param selectionId
     *         the new selection id
     */
    public void setSelectionId( String selectionId ) {
        this.selectionId = selectionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SuSUserGroupDTO [id=" + id + ", name=" + name + ", description=" + description + ", status=" + status + ", createdOn="
                + createdOn + ", updatedOn=" + updatedOn + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Validate.
     *
     * @return the notification
     */
    public Notification validate() {

        Notification notify = new Notification();

        if ( StringUtils.isBlank( getName() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), NAME_FIELD ) ) );

        } else {
            notify.addNotification(
                    de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( getName(), NAME_FIELD,
                            ConstantsLength.STANDARD_NAME_LENGTH, false, true ) );
        }

        if ( StringUtils.isNotBlank( getDescription() ) ) {
            notify.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( getDescription(),
                    DESCRIPTION_FIELD, ConstantsLength.STANDARD_NAME_LENGTH, false, true ) );
        }
        return notify;
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
        SuSUserGroupDTO other = ( SuSUserGroupDTO ) obj;

        if ( createdOn == null ) {
            if ( other.createdOn != null ) {
                return false;
            }
        } else if ( !createdOn.equals( other.createdOn ) ) {
            return false;
        }
        if ( description == null ) {
            if ( other.description != null ) {
                return false;
            }
        } else if ( !description.equals( other.description ) ) {
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
        if ( status == null ) {
            if ( other.status != null ) {
                return false;
            }
        } else if ( !status.equals( other.status ) ) {
            return false;
        }
        if ( updatedOn == null ) {
            return other.updatedOn == null;
        } else {
            return updatedOn.equals( other.updatedOn );
        }
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
     * Gets the modified by.
     *
     * @return the modified by
     */
    public UserDTO getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets the modified by.
     *
     * @param modifiedBy
     *         the new modified by
     */
    public void setModifiedBy( UserDTO modifiedBy ) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount( Integer users ) {
        this.usersCount = users;
    }

}