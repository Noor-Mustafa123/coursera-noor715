package de.soco.software.simuspace.server.model;

import java.util.Map;

import de.soco.software.simuspace.suscore.common.base.MenuPermission;

/**
 * This Class contains designer token and permission set for workflows.
 */
public class TokenDetails {

    /**
     * The permissions for a user on a specific token in workflow.
     */
    private Map< String, MenuPermission > permissions;

    /**
     * The token of a user.
     */
    private String token;

    /**
     * The version.
     */
    private String version;

    /**
     * Instantiates a new token details.
     */
    public TokenDetails() {
        super();
    }

    /**
     * Instantiates a new token details.
     *
     * @param permissions
     *         the permissions
     * @param token
     *         the token
     */
    public TokenDetails( Map< String, MenuPermission > permissions, String token ) {
        this();
        this.setPermissions( permissions );
        this.token = token;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token
     *         the new token
     */
    public void setToken( String token ) {
        this.token = token;
    }

    /**
     * Gets the permissions.
     *
     * @return the permissions
     */
    public Map< String, MenuPermission > getPermissions() {
        return permissions;
    }

    /**
     * Sets the permissions.
     *
     * @param permissions
     *         the permissions
     */
    public void setPermissions( Map< String, MenuPermission > permissions ) {
        this.permissions = permissions;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *         the new version
     */
    public void setVersion( String version ) {
        this.version = version;
    }

}
