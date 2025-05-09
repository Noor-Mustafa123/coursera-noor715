package de.soco.software.simuspace.suscore.permissions.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.VersionDTO;

/**
 * This class allow us to uniquely identify any domain object class in the system as as Business Object.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ResourceAccessControlDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1645443306041421785L;

    /**
     * The id.
     */
    private UUID id;

    /**
     * The name.
     */
    private String name;

    /**
     * The qualified name.
     */
    private String qualifiedName;

    /**
     * The description.
     */
    private String description;

    /**
     * The created on Date/time.
     */
    private String createdOn;

    /**
     * The updated on Date/time.
     */
    private String updatedOn;

    /**
     * The parent.
     */
    private ResourceAccessControlDTO parent;

    /**
     * The security identity.
     */
    private UUID securityIdentity;

    /**
     * The is inherit.
     */
    private boolean isInherit;

    /**
     * The permissions.
     */
    private List< PermissionDTO > permissionDTOs;

    /**
     * flag to check if save or update operation id performed.
     */
    private boolean isUpdate;

    /**
     * The version dto.
     */
    private transient VersionDTO version;

    /**
     * Instantiates a new resource access control DTO 1.
     */
    public ResourceAccessControlDTO() {
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
     * Gets the qualified name.
     *
     * @return the qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Sets the qualified name.
     *
     * @param qualifiedName
     *         the new qualified name
     */
    public void setQualifiedName( String qualifiedName ) {
        this.qualifiedName = qualifiedName;
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
     * Gets the parent.
     *
     * @return the parent
     */
    public ResourceAccessControlDTO getParent() {
        return parent;
    }

    /**
     * Sets the parent.
     *
     * @param parent
     *         the new parent
     */
    public void setParent( ResourceAccessControlDTO parent ) {
        this.parent = parent;
    }

    /**
     * Gets the permission DT os.
     *
     * @return the permission DT os
     */
    public List< PermissionDTO > getPermissionDTOs() {
        return permissionDTOs;
    }

    /**
     * Sets the permission DT os.
     *
     * @param permissionDTOs
     *         the new permission DT os
     */
    public void setPermissionDTOs( List< PermissionDTO > permissionDTOs ) {
        this.permissionDTOs = permissionDTOs;
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

}