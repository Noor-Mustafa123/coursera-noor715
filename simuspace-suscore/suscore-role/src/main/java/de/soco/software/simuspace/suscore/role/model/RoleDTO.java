package de.soco.software.simuspace.suscore.role.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.BusinessObject;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;

/**
 * The Class is responsible for providing available roles as Business Object.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class RoleDTO extends BusinessObject implements Serializable {

    /**
     * The serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant MAX_LENGTH_OF_NAME.
     */
    private static final int MAX_LENGTH_OF_NAME = 64;

    /**
     * The Constant MAX_LENGTH_OF_DESCRIPTION.
     */
    private static final int MAX_LENGTH_OF_DESCRIPTION = 255;

    /**
     * The Constant NAME.
     */
    private static final String ROLE_NAME = "Role";

    /**
     * The Constant DESCRIPTION.
     */
    private static final String ROLE_DESCRIPTION = "Description";

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, orderNum = 9, type = "hidden")
    @UIColumn( data = "id", filter = "uuid", renderer = "hidden", title = "3000021x4", name = "id", isShow = false, orderNum = 9 )
    private UUID id;

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000048x4", orderNum = 1 )
    @UIColumn( data = "name", filter = "text", renderer = "text", title = "3000048x4", name = "name", orderNum = 1, width = 0 )
    private String name;

    /**
     * The description.
     */
    @UIFormField( name = "description", type = "textarea", title = "3000011x4", orderNum = 2 )
    @UIColumn( data = "description", filter = "text", renderer = "text", title = "3000011x4", name = "description", orderNum = 2 )
    private String description;

    /**
     * The status.
     */
    @UIFormField( name = "status", title = "3000049x4", type = "select", orderNum = 3 )
    @UIColumn( data = "status", filter = "select", renderer = "text", title = "3000049x4", type = "select", filterOptions = { "Active",
            "Inactive" }, name = "status", isSortable = false, orderNum = 3 )
    private String status;

    /**
     * The resource set.
     */
    @UIFormField( name = "groups", title = "3000019x4", type = "connected-table", orderNum = 4 )
    private List< SuSUserGroupDTO > groups;

    /**
     * The Groups count.
     */
    @UIColumn( data = "groupsCount", name = "groups", filter = "", url = "system/permissions/role/{id}/groups", renderer = "link", title = "3000020x4", orderNum = 6, isSortable = false )
    private Integer groupsCount;

    /**
     * The is update.
     */
    private boolean isUpdate;

    /**
     * The created by.
     */
    @UIFormField( name = "createdBy.userUid", title = "3000064x4", isAsk = false, orderNum = 6 )
    @UIColumn( data = "createdBy.userUid", name = "createdBy.userUid", filter = "text", url = "system/user/{createdBy.id}", renderer = "link", tooltip = "{createdBy.userName}", title = "3000064x4", orderNum = 6 )
    private UserDTO createdBy;

    /**
     * The modified by.
     */
    @UIFormField( name = "modifiedBy.userUid", title = "3000065x4", isAsk = false, orderNum = 8 )
    @UIColumn( data = "modifiedBy.userUid", name = "modifiedBy.userUid", filter = "text", url = "system/user/{modifiedBy.id}", renderer = "link", title = "3000065x4", tooltip = "{modifiedBy.userName}", orderNum = 8 )
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
    @UIFormField( name = "modifiedOn", title = "3000053x4", isAsk = false, orderNum = 7, type = "date")
    @UIColumn( data = "modifiedOn", name = "modifiedOn", filter = "dateRange", renderer = "html", title = "3000053x4", orderNum = 7 )
    private String modifiedOn;

    private List< ResourceAccessControlDTO > resourceAccessControlDTOs;

    /**
     * The security identity.
     */
    private UUID securityIdentity;

    /**
     * The selectionid.
     */
    private String selectionId;

    /**
     * Instantiates a new role business object impl.
     */
    public RoleDTO() {
        super();
    }

    /**
     * Instantiates a new role DTO.
     *
     * @param name
     *         the name
     * @param description
     *         the description
     * @param status
     *         the status
     * @param selectionId
     *         the selection id
     */
    public RoleDTO( String name, String description, String status, String selectionId ) {
        super();
        this.name = name;
        this.description = description;
        this.status = status;
        this.selectionId = selectionId;
        this.groups = new ArrayList<>();
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
     * Gets the groups.
     *
     * @return the groups
     */
    public List< SuSUserGroupDTO > getGroups() {
        return groups;
    }

    /**
     * Sets the groups.
     *
     * @param groups
     *         the new groups
     */
    public void setGroups( List< SuSUserGroupDTO > groups ) {
        this.groups = groups;
    }

    /**
     * Checks if is update.
     *
     * @return true, if is update
     */
    public boolean isUpdate() {
        return isUpdate;
    }

    /**
     * Sets the update.
     *
     * @param isUpdate
     *         the new update
     */
    public void setUpdate( boolean isUpdate ) {
        this.isUpdate = isUpdate;
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
    public String getModifiedOn() {
        return modifiedOn;
    }

    /**
     * Sets the updated on.
     *
     * @param updatedOn
     *         the new updated on
     */
    public void setModifiedOn( String updatedOn ) {
        this.modifiedOn = updatedOn;
    }

    /**
     * Gets the resource access control DT os.
     *
     * @return the resource access control DT os
     */
    public List< ResourceAccessControlDTO > getResourceAccessControlDTOs() {
        return resourceAccessControlDTOs;
    }

    /**
     * Sets the resource access control DT os.
     *
     * @param resourceAccessControlDTOs
     *         the new resource access control DT os
     */
    public void setResourceAccessControlDTOs( List< ResourceAccessControlDTO > resourceAccessControlDTOs ) {
        this.resourceAccessControlDTOs = resourceAccessControlDTOs;
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
     * Validate.
     *
     * @return the notification
     *
     * @throws SusException
     *         the sus exception
     */
    public Notification validate() {
        Notification notification = new Notification();
        notification.addNotification( ValidationUtils.validateFieldAndLength( getName(), ROLE_NAME, MAX_LENGTH_OF_NAME, false, true ) );
        if ( StringUtils.isNotBlank( getDescription() ) ) {
            notification.addNotification(
                    ValidationUtils.validateFieldAndLength( getDescription(), ROLE_DESCRIPTION, MAX_LENGTH_OF_DESCRIPTION, false, false ) );
        }

        return notification;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RoleModel [id=" + id + ", name=" + name + ", description=" + description + ", status=" + status + ", groups=" + groups
                + ", isUpdate=" + isUpdate + ", createdOn=" + createdOn + ", updatedOn=" + modifiedOn + "]";
    }

    /**
     * Gets the selectionid.
     *
     * @return the selectionid
     */
    public String getSelectionId() {
        return selectionId;
    }

    /**
     * Sets the selectionid.
     *
     * @param selectionid
     *         the new selectionid
     */
    public void setSelectionId( String selectionId ) {
        this.selectionId = selectionId;
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

    /**
     * Gets groups count.
     *
     * @return the groups count
     */
    public Integer getGroupsCount() {
        return groupsCount;
    }

    /**
     * Sets groups count.
     *
     * @param groupsCount
     *         the groups count
     */
    public void setGroupsCount( Integer groupsCount ) {
        this.groupsCount = groupsCount;
    }

}
