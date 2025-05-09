package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class CommonLocationDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class CommonLocationDTO {

    /**
     * The location id.
     */
    private String locationId;

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
     * Gets the location id.
     *
     * @return the location id
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * Sets the location id.
     *
     * @param locationId
     *         the new location id
     */
    public void setLocationId( String locationId ) {
        this.locationId = locationId;
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
     * Instantiates a new common location DTO.
     */
    public CommonLocationDTO() {
        super();
    }

    /**
     * Instantiates a new common location DTO.
     *
     * @param locationId
     *         the location id
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
     * @param url
     *         the url
     * @param authToken
     *         the auth token
     * @param isInternal
     *         the is internal
     */
    public CommonLocationDTO( String locationId, String name, String description, String status, String type, int priority, String vault,
            String staging, String url, String authToken, boolean isInternal ) {
        super();
        this.locationId = locationId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.priority = priority;
        this.vault = vault;
        this.staging = staging;
        this.url = url;
        this.authToken = authToken;
        this.isInternal = isInternal;
    }

}
