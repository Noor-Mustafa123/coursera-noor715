/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.Serial;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.BusinessObject;
import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The Class is responsible to map and provide user directory payload.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SuSUserDirectoryDTO extends BusinessObject {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 3993923040037245330L;

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * The Constant DESCRIPTION_FIELD.
     */
    private static final String DESCRIPTION_FIELD = "Description";

    /**
     * Unique id for directory configuration.
     */
    @UIFormField( name = "id", type = "hidden", title = "3000021x4", isAsk = false )
    @UIColumn( data = "id", name = "id", filter = "uuid", renderer = "hidden", type = "hidden", title = "3000021x4", isShow = false )
    private UUID id;

    /**
     * Name of directory configuration.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4", orderNum = 1 )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * Description of configuration.
     */
    @UIFormField( name = "description", type = "textarea", title = "3000011x4", orderNum = 2 )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 2 )
    private String description;

    /**
     * Status of config (1=ACTIVE , 0=INACTIVE).
     */
    @UIFormField( name = "status", title = "3000049x4", type = "select", orderNum = 4 )
    @UIColumn( data = "status", filter = "select", renderer = "text", title = "3000049x4", type = "select", name = "status", isSortable = false, filterOptions = {
            "Active", "Inactive" }, orderNum = 4 )
    private String status;

    /**
     * Configuration Type (0=SIMSPACE_INTERNAL_DIRECTORY ,1=LDAP_DIRECTORY, 2=ACTIVE_DIRECTORY, 3=MACHINE_USER_DIRECTORY, 4=OAUTH_DIRECTORY).
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "type", title = "3000051x4", type = "select", orderNum = 3 )
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000051x4", type = "select", name = "type", orderNum = 3 )
    private String type;

    /**
     * The created on.
     */
    @UIFormField( name = "createdOn", title = "3000008x4", isAsk = false, orderNum = 5, type = "date")
    @UIColumn( data = "createdOn", name = "createdOn", filter = "dateRange", renderer = "date", title = "3000008x4", orderNum = 5 )
    private String createdOn;

    /**
     * The modified on.
     */
    @UIFormField( name = "modifiedOn", title = "3000029x4", isAsk = false, orderNum = 7, type = "date")
    @UIColumn( data = "modifiedOn", name = "modifiedOn", filter = "dateRange", renderer = "date", title = "3000029x4", orderNum = 7 )
    private String modifiedOn;

    /**
     * The created on.
     */
    @UIFormField( name = "createdBy.userUid", title = "3000064x4", isAsk = false, orderNum = 6 )
    @UIColumn( data = "createdBy.userUid", name = "createdBy.userUid", url = "system/user/{createdBy.id}", filter = "text", renderer = "link", title = "3000064x4", tooltip = "{createdBy.userName}", orderNum = 6 )
    private UserDTO createdBy;

    /**
     * The modified on.
     */
    @UIFormField( name = "modifiedBy.userUid", title = "3000065x4", isAsk = false, orderNum = 8 )
    @UIColumn( data = "modifiedBy.userUid", name = "modifiedBy.userUid", url = "system/user/{modifiedBy.id}", filter = "text", renderer = "link", title = "3000065x4", tooltip = "{modifiedBy.userName}", orderNum = 8 )
    private UserDTO modifiedBy;

    /**
     * All attributes belongs to this configuration.
     */
    private transient SusUserDirectoryAttributeDTO userDirectoryAttribute;

    /**
     * The version dto.
     */
    private VersionDTO version;

    /**
     * Instantiates a new SuS user directory.
     */
    public SuSUserDirectoryDTO() {
    }

    /**
     * Instantiates a new SuS user directory model.
     *
     * @param name
     *         the name
     * @param description
     *         the description
     * @param status
     *         the status
     * @param type
     *         the type
     * @param userDirectoryAttribute
     *         the json attributes
     * @param jsonMapping
     *         the json mapping
     */
    public SuSUserDirectoryDTO( String name, String description, String status, String type,
            SusUserDirectoryAttributeDTO userDirectoryAttribute ) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.userDirectoryAttribute = userDirectoryAttribute;
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
     *         the id
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
     *         the name
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
     *         the description
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
     *         the status
     */
    public void setStatus( String status ) {
        this.status = status;
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
     *         the type
     */
    public void setType( String type ) {
        this.type = type;
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
     *         the createdOn
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
     * Gets the attributes.
     *
     * @return the attributes
     */
    public SusUserDirectoryAttributeDTO getUserDirectoryAttribute() {
        return userDirectoryAttribute;
    }

    /**
     * @param attributes
     *         the attributes to set
     */
    public void setUserDirectoryAttribute( SusUserDirectoryAttributeDTO userDirectoryAttribute ) {
        this.userDirectoryAttribute = userDirectoryAttribute;
    }

    /**
     * @return the version
     */
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * @param versionDto
     *         the versionDto to set
     */
    public void setVersion( VersionDTO versionDto ) {
        this.version = versionDto;
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SuSUserDirectory [id=" + id + ", name=" + name + ", description=" + description + ", status=" + status + ", type=" + type
                + ", userDirectoryAttributeDTO=" + userDirectoryAttribute + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + "]";
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

        if ( !StringUtils.isBlank( getDescription() ) ) {
            notify.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( getDescription(),
                    DESCRIPTION_FIELD, ConstantsLength.STANDARD_DESCRIPTION_LENGTH, false, false ) );
        }

        if ( StringUtils.isBlank( getType() ) || ( !getType().equals( ConstantsUserDirectories.INTERNAL_DIRECTORY )
                && !getType().equals( ConstantsUserDirectories.MACHINE_USER_DIRECTORY ) && !getType().equals(
                ConstantsUserDirectories.LDAP_DIRECTORY ) && !getType().equals( ConstantsUserDirectories.ACTIVE_DIRECTORY ) && !getType().equals(ConstantsUserDirectories.OAUTH_DIRECTORY )) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.DIRECTORY_TYPE_IS_NOT_VALID.getKey() ) ) );
        }

        if ( ( getType().equals( ConstantsUserDirectories.LDAP_DIRECTORY ) || getType().equals(
                ConstantsUserDirectories.ACTIVE_DIRECTORY ) ) && ( getUserDirectoryAttribute() == null ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.ATTRIBUTES_SHOULD_NOT_BE_EMPTY.getKey() ) ) );
        }

        if ( getUserDirectoryAttribute() != null && ( getType().equals( ConstantsUserDirectories.LDAP_DIRECTORY ) || getType().equals(
                ConstantsUserDirectories.ACTIVE_DIRECTORY ) ) ) {
            notify.addNotification( getUserDirectoryAttribute().validate( getType() ) );
        }

        if ( getUserDirectoryAttribute() != null && ( getType().equals( ConstantsUserDirectories.OAUTH_DIRECTORY ) ) ) {
            notify.addNotification( getUserDirectoryAttribute().validateOAuthAttributes() );
        }

        if ( StringUtils.isBlank( getStatus() ) || ( !getStatus().equals( ConstantsStatus.ACTIVE ) && !getStatus().equals(
                ConstantsStatus.DISABLE ) ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.STATUS_SHOULD_ACTIVE_OR_DISABLED.getKey() ) ) );
        }

        return notify;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
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
        SuSUserDirectoryDTO other = ( SuSUserDirectoryDTO ) obj;
        if ( userDirectoryAttribute == null ) {
            if ( other.userDirectoryAttribute != null ) {
                return false;
            }
        } else if ( !userDirectoryAttribute.equals( other.userDirectoryAttribute ) ) {
            return false;
        }
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
        if ( !Objects.equals( status, other.status ) ) {
            return false;
        }
        if ( !Objects.equals( type, other.type ) ) {
            return false;
        }
        if ( modifiedOn == null ) {
            return other.modifiedOn == null;
        } else {
            return modifiedOn.equals( other.modifiedOn );
        }
    }

}