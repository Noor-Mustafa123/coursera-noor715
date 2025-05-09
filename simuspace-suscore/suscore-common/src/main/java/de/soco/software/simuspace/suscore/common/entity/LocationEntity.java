package de.soco.software.simuspace.suscore.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * An entity class that would be mapped to the locations and responsible for managing locations.
 *
 * @author M.Nasir.Farooq
 * @since 2.0
 */
@Getter
@Setter
public class LocationEntity extends SystemEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -5600929072603973925L;

    /**
     * The name.
     */
    private String name;

    /**
     * The description.
     */
    private String description;

    /**
     * The status.
     */
    private boolean status;

    /**
     * The type.
     */
    private String type;

    /**
     * The priority.
     */
    private int priority;

    /**
     * The vault.
     */
    private String vault;

    /**
     * The staging.
     */
    private String staging;

    /**
     * The hpc.
     */
    private String hpc;

    /**
     * The url.
     */
    private String url;

    /**
     * The auth token.
     */
    private String authToken;

    /**
     * The is internal.
     */
    private boolean isInternal;

    /**
     * Instantiates a new role entity.
     */
    public LocationEntity() {
        super();
    }

    /**
     * Instantiates a new role entity.
     *
     * @param id
     *         the id
     */
    public LocationEntity( UUID id ) {
        this.setId( id );
    }

    /**
     * Instantiates a new role entity.
     *
     * @param id
     *         the role uuid
     * @param createdOn
     *         the created on
     * @param description
     *         the description
     * @param name
     *         the name
     * @param status
     *         the status
     * @param updatedOn
     *         the updated on
     */
    public LocationEntity( UUID id, String description, String name, boolean status ) {
        super();
        setId( id );
        this.description = description;
        this.name = name;
        this.status = status;
    }

    /**
     * Instantiates a new location entity.
     *
     * @param id
     *         the id
     * @param auditLogEntity
     *         the audit log entity
     * @param name
     *         the name
     * @param description
     *         the description
     * @param status
     *         the status
     * @param type
     *         the type
     * @param priority
     *         the priority
     * @param vault
     *         the vault
     * @param staging
     *         the staging
     * @param hpc
     *         the hpc
     * @param url
     *         the url
     * @param authToken
     *         the auth token
     */
    public LocationEntity( UUID id, AuditLogEntity auditLogEntity, String name, String description, boolean status, String type,
            int priority, String vault, String staging, String hpc, String url, String authToken ) {
        this.setId( id );
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.priority = priority;
        this.vault = vault;
        this.staging = staging;
        this.hpc = hpc;
        this.url = url;
        this.authToken = authToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( getId() == null ) ? 0 : getId().hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( !super.equals( obj ) ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        LocationEntity other = ( LocationEntity ) obj;
        if ( getId() == null ) {
            if ( other.getId() != null ) {
                return false;
            }
        } else if ( !getId().equals( other.getId() ) ) {
            return false;
        }
        return true;
    }

    /**
     * Checks if is internal.
     *
     * @return true, if is internal
     */
    public boolean isInternal() {
        return isInternal;
    }

    /**
     * Sets the internal.
     *
     * @param isInternal
     *         the new internal
     */
    public void setInternal( boolean isInternal ) {
        this.isInternal = isInternal;
    }

}