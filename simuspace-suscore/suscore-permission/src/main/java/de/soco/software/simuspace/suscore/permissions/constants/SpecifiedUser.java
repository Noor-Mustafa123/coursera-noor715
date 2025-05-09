package de.soco.software.simuspace.suscore.permissions.constants;

/**
 * The Enum SpecifiedUser.
 */
public enum SpecifiedUser {

    /**
     * The super user.
     */
    SUPER_USER( "simuspace" );

    /**
     * Instantiates a new specified user.
     *
     * @param user
     *         the user
     */
    SpecifiedUser( String user ) {
        this.user = user;
    }

    /**
     * The user.
     */
    private String user;

    /**
     * Gets the user.
     *
     * @return the user
     */
    public String getUser() {
        return user;
    }
}
