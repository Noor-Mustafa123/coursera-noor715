package de.soco.software.simuspace.suscore.common.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * A Utility class that would hold the constants for user profile
 *
 * @author Zeeshan jamal
 */
public class ConstantsUserProfile {

    /**
     * Instantiates a new constants user profile.
     */
    private ConstantsUserProfile() {
    }

    /**
     * The constant user id key
     */
    public static final String UID_KEY = "uid";

    /**
     * The Constant SUPER.
     */
    public static final String SUPER = "Super";

    /**
     * The Constant USER.
     */
    public static final String USER = "User";

    public static final String SUPER_USER = SUPER + StringUtils.SPACE + USER;

    /**
     * The constant SUPER_USER_ID
     */
    public static final Integer SUPER_USER_ID = 0;

    /**
     * The Constant SUPER_USER_GENERAL_HEADER_ID.
     */
    public static final Integer SUPER_USER_GENERAL_HEADER_ID = -1;

    /**
     * constant DEFAULT_USER_ID
     */
    public static final String DEFAULT_USER_ID = "0";

}