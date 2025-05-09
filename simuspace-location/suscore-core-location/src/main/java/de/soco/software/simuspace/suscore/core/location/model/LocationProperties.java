package de.soco.software.simuspace.suscore.core.location.model;

import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * A location model class for mapping location configurations.
 *
 * @author M.Nasir.Farooq
 */
public class LocationProperties {

    /**
     * The name.
     */
    private String name;

    /**
     * The vault.
     */
    private String vault;

    /**
     * The staging.
     */
    private String staging;

    /**
     * The url.
     */
    private String url;

    /**
     * The url.
     */
    private String authToken;

    /**
     * Instantiates a new location properties.
     *
     * @param name
     *         the name
     * @param vault
     *         the vault
     * @param staging
     *         the staging
     * @param url
     *         the url
     * @param authToken
     *         the auth token
     */
    public LocationProperties() {
        super();
        this.name = PropertiesManager.getLocationName();
        this.vault = PropertiesManager.getVaultPath();
        this.staging = PropertiesManager.getStagingPath();
        this.url = PropertiesManager.getLocationURL();
        this.authToken = PropertiesManager.getLocationAuthToken();
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

}
