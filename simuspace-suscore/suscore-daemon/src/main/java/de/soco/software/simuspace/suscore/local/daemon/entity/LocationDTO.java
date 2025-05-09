package de.soco.software.simuspace.suscore.local.daemon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.entity.LocationEntity;

/**
 * A location model class for mapping to json request and response.
 *
 * @author M.Nasir.Farooq
 */
@Entity
@Table( name = "locations" )
@JsonIgnoreProperties( ignoreUnknown = true )
public class LocationDTO {

    /**
     * The id.
     */
    @Id
    @Column( name = "locationId" )
    private String locationId;

    @ManyToOne
    @JoinColumn( name = "id" )
    private AllObjectEntity allObjectEntity;

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
    private String status;

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
     * Instantiates a new location DTO.
     */
    public LocationDTO() {
    }

    /**
     * Instantiates a new location DTO.
     *
     * @param id
     *         the id
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
    public LocationDTO( String id, String name, String description, String status, String type, int priority, String vault, String staging,
            String url, String authToken ) {
        super();
        this.locationId = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.priority = priority;
        this.vault = vault;
        this.staging = staging;
        this.authToken = authToken;
        this.url = url;
    }

    /**
     * Instantiates a new location DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public LocationDTO( String id, String name ) {
        this.locationId = id;
        this.name = name;
    }

    /**
     * Instantiates a new location DTO.
     *
     * @param id
     *         the id
     */
    public LocationDTO( String id ) {
        this.locationId = id;
    }

    /**
     * Instantiates a new location DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param vaultPath
     *         the vault path
     * @param stagingPath
     *         the staging path
     * @param url
     *         the url
     * @param authToken
     *         the auth token
     */
    public LocationDTO( String id, String name, String vaultPath, String stagingPath, String url, String authToken ) {
        this.locationId = id;
        this.name = name;
        this.vault = vaultPath;
        this.staging = stagingPath;
        this.authToken = authToken;
        this.url = url;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return locationId;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.locationId = id;
    }

    /**
     * Gets the vault.
     *
     * @return the vault
     */
    public String getVault() {
        return vault;
    }

    /**
     * Sets the vault.
     *
     * @param vault
     *         the new vault
     */
    public void setVault( String vault ) {
        this.vault = vault;
    }

    /**
     * Gets the staging.
     *
     * @return the staging
     */
    public String getStaging() {
        return staging;
    }

    /**
     * Sets the staging.
     *
     * @param staging
     *         the new staging
     */
    public void setStaging( String staging ) {
        this.staging = staging;
    }

    /**
     * Gets the auth token.
     *
     * @return the auth token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken
     *         the new auth token
     */
    public void setAuthToken( String authToken ) {
        this.authToken = authToken;
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
     * Gets the priority.
     *
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the priority.
     *
     * @param priority
     *         the new priority
     */
    public void setPriority( int priority ) {
        this.priority = priority;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url
     *         the new url
     */
    public void setUrl( String url ) {
        this.url = url;
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

    /**
     * Prepare entity.
     *
     * @return the location entity
     */
    public LocationEntity prepareEntity() {

        boolean statusVal = getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE ) ? true : false;
        return new LocationEntity( UUID.fromString( getId() ), null, getName(), getDescription(), statusVal, getType(), getPriority(),
                getVault(), getStaging(), null, getUrl(), getAuthToken() );
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( locationId == null ) ? 0 : locationId.hashCode() );
        return result;
    }

    /*
     * (non-Javadoc)
     *
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
        LocationDTO other = ( LocationDTO ) obj;
        if ( locationId == null ) {
            if ( other.locationId != null ) {
                return false;
            }
        } else if ( !locationId.equals( other.locationId ) ) {
            return false;
        }
        return true;
    }

}
