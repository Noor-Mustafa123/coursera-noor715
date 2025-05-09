package de.soco.software.simuspace.suscore.permissions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.data.entity.AclEntryEntity;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;

/**
 * This Class stores individual permission to each recipient. In this class we specify what action can be performed on each domain object
 * instance by the desired user/group/role
 */
public class PermissionDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 4988309935242309369L;

    /**
     * The Id.
     */
    private UUID id;

    /**
     * The is inherit.
     */
    private boolean isInherit;

    /**
     * The parent.
     */
    private UUID parent;

    /**
     * flag to check if save or update operation performed.
     */
    private boolean isUpdate;

    /**
     * The created on Date/time.
     */
    private Date createdOn;

    /**
     * The updated on Date/time.
     */
    private Date updatedOn;

    /**
     * The version dto.
     */
    private transient VersionDTO version;

    /**
     * The value.
     */
    private int value;

    /**
     * The manage.
     */
    private boolean manage;

    /**
     * key of the constant
     */
    private int matrixKey;

    /**
     * value against the constant key
     */
    private String matrexValue;

    /**
     * Instantiates a new permission DTO 1.
     */
    public PermissionDTO() {
        super();
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
     * Validate.
     *
     * @return the notification
     */
    public Notification validate() {
        return null;
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
     * Gets the version dto.
     *
     * @return the version dto
     */
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * Sets the version dto.
     *
     * @param version
     *         the new version dto
     */
    public void setVersion( VersionDTO versionDto ) {
        this.version = versionDto;
    }

    /**
     * Checks if is inherit.
     *
     * @return true, if is inherit
     */
    public boolean isInherit() {
        return isInherit;
    }

    /**
     * Sets the inherit.
     *
     * @param isInherit
     *         the new inherit
     */
    public void setInherit( boolean isInherit ) {
        this.isInherit = isInherit;
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
    public Date getUpdatedOn() {
        return updatedOn;
    }

    /**
     * Sets the updated on.
     *
     * @param updatedOn
     *         the new updated on
     */
    public void setUpdatedOn( Date updatedOn ) {
        this.updatedOn = updatedOn;
    }

    /**
     * Gets the parent.
     *
     * @return the parent
     */
    public UUID getParent() {
        return parent;
    }

    /**
     * Sets the parent.
     *
     * @param parent
     *         the new parent
     */
    public void setParent( UUID parent ) {
        this.parent = parent;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( int value ) {
        this.value = value;
    }

    /**
     * Checks if is manage.
     *
     * @return true, if is manage
     */
    public boolean isManage() {
        return manage;
    }

    /**
     * Sets the manage.
     *
     * @param manage
     *         the new manage
     */
    public void setManage( boolean manage ) {
        this.manage = manage;
    }

    /**
     * Gets the matrix key.
     *
     * @return the matrix key
     */
    public int getMatrixKey() {
        return matrixKey;
    }

    /**
     * Sets the matrix key.
     *
     * @param matrixKey
     *         the new matrix key
     */
    public void setMatrixKey( int matrixKey ) {
        this.matrixKey = matrixKey;
    }

    /**
     * Gets the matrex value.
     *
     * @return the matrex value
     */
    public String getMatrexValue() {
        return matrexValue;
    }

    /**
     * Sets the matrex value.
     *
     * @param matrexValue
     *         the new matrex value
     */
    public void setMatrexValue( String matrexValue ) {
        this.matrexValue = matrexValue;
    }

    /**
     * Perpare instant permission.
     *
     * @param checkBox
     *         the check box
     *
     * @return the permission DTO
     */
    public PermissionDTO perpareInstantPermission( CheckBox checkBox ) {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setValue( checkBox.getValue() );
        permissionDTO.setMatrexValue( checkBox.getName() );
        permissionDTO.setInherit( false );
        permissionDTO.setUpdate( false );
        return permissionDTO;
    }

    /**
     * Prepare entry entity only.
     *
     * @param objectIdentityEntity
     *         the object identity entity
     * @param securityIdentityEntity
     *         the security identity entity
     * @param mask
     *         the mask
     *
     * @return the entry entity
     */
    public AclEntryEntity prepareEntryEntityOnly( AclObjectIdentityEntity objectIdentityEntity,
            AclSecurityIdentityEntity securityIdentityEntity, int mask ) {
        AclEntryEntity entryEntity = new AclEntryEntity();
        entryEntity.setId( UUID.randomUUID() );
        entryEntity.setDelete( false );
        entryEntity.setSecurityIdentityEntity( securityIdentityEntity );
        entryEntity.setObjectIdentityEntity( objectIdentityEntity );
        entryEntity.setMask( mask );
        entryEntity.setCreatedOn( new Date() );
        return entryEntity;
    }

}