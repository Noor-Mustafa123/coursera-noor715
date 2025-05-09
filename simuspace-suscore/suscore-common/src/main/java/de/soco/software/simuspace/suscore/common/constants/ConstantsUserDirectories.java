package de.soco.software.simuspace.suscore.common.constants;

/**
 * A class that would hold the directory type constants
 *
 * @author Zeeshan jamal Configuration Type (0=SIMSPACE_INTERNAL_DIRECTORY ,1=LDAP_DIRECTORY, 2=ACTIVE_DIRECTORY, 3=MACHINE_USER_DIRECTORY )
 */
public class ConstantsUserDirectories {

    /**
     * The Constant INTERNAL_DIRECTORY.
     */
    public static final String INTERNAL_DIRECTORY = "Internal Directory";

    /**
     * The Constant LDAP_DIRECTORY.
     */
    public static final String LDAP_DIRECTORY = "Ldap Directory";

    /**
     * The Constant ACTIVE_DIRECTORY.
     */
    public static final String ACTIVE_DIRECTORY = "Active Directory";

    /**
     * The Constant MACHINE_USER_DIRECTORY.
     */
    public static final String MACHINE_USER_DIRECTORY = "Machine User Directory";


    public static final String OAUTH_DIRECTORY = "OAuth Directory";


    /**
     * private constructor to avoid instantiation
     */
    private ConstantsUserDirectories() {

    }

}
