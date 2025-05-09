package de.soco.software.simuspace.suscore.data.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;

/**
 * A location model class for mapping to json request and response.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LocationDTO extends Location {

    /**
     * The auth token.
     */
    @UIFormField( name = "authToken", title = "3000118x4", orderNum = 9 )
    @UIColumn( data = "authToken", filter = "text", renderer = "text", title = "3000118x4", name = "authToken", orderNum = 9 )
    private String authToken;

    /**
     * Instantiates a new location DTO.
     */
    public LocationDTO() {
        super();
    }

    /**
     * Instantiates a new location DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public LocationDTO( UUID id, String name ) {
        super( id, name );
    }

    /**
     * Instantiates a new location DTO.
     *
     * @param id
     *         the id
     */
    public LocationDTO( UUID id ) {
        super( id );
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
    public LocationDTO( UUID id, String name, String vaultPath, String stagingPath, String url, String authToken ) {
        super( id, name, vaultPath, stagingPath, url );
        this.authToken = authToken;
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
    public LocationDTO( UUID id, String name, String description, String status, String type, int priority, String vault, String staging,
            String url, String authToken, boolean isInternal ) {
        super( id, name, description, status, type, priority, vault, staging, url, isInternal );
        this.authToken = authToken;
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
     * Prepare entity.
     *
     * @return the location entity
     */
    public LocationEntity prepareEntity() {

        boolean statusVal = getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE );
        return new LocationEntity( getId() == null ? UUID.randomUUID() : getId(), null, getName(), getDescription(), statusVal, getType(),
                getPriority(), getVault(), getStaging(), null, getUrl(), getAuthToken() );
    }

    /**
     * Prepare entity.
     *
     * @return the location entity
     */
    public LocationEntity prepareEntity( LocationEntity locationEntity ) {

        boolean statusVal = getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE );
        locationEntity.setId( getId() == null ? UUID.randomUUID() : getId() );
        locationEntity.setAuditLogEntity( null );
        locationEntity.setName( getName() );
        locationEntity.setDescription( getDescription() );
        locationEntity.setStatus( statusVal );
        locationEntity.setType( getType() );
        locationEntity.setPriority( getPriority() );
        locationEntity.setVault( getVault() );
        locationEntity.setStaging( getStaging() );
        locationEntity.setHpc( null );
        locationEntity.setUrl( getUrl() );
        locationEntity.setAuthToken( getAuthToken() );
        return locationEntity;
    }

}
