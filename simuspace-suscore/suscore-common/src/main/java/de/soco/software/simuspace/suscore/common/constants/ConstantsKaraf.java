package de.soco.software.simuspace.suscore.common.constants;

import java.io.File;

/**
 * Utility class which will contain all the constants for apache karaf
 *
 * @author Zeeshan Jamal
 */
public class ConstantsKaraf {

    /**
     * The karaf conf.
     */
    public static final String KARAF_CONF = "conf";

    /**
     * The plugin folder .
     */
    public static final String KARAF_PLUGIN = "plugin";

    /**
     * The plugin temp folder .
     */
    public static final String KARAF_PLUGIN_temp = "pluginTemp";

    /**
     * The Constant KARAF_BASE_KEY.
     */
    private static final String KARAF_BASE_KEY = "karaf.base";

    /**
     * The Constant KARAF_HOME.
     */
    public static final String KARAF_HOME = System.getProperty( KARAF_BASE_KEY );

    /**
     * The karaf name property.
     */
    public static final String KARAF_NAME = "karaf.name";

    /**
     * The karaf manager role.
     */
    public static final String ROLE_MANAGER = "manager";

    /**
     * The karaf admin role.
     */
    public static final String ROLE_ADMIN = "admin";

    /**
     * The karaf viewer role.
     */
    public static final String ROLE_VIEW = "viewer";

    /**
     * The karaf as root user.
     */
    public static final String ROOT = "root";

    /**
     * To add feature repo in karaf command prefix
     */
    public static final String FEATURE_ADD_REPO_CMD = "feature:repo-add mvn:";

    /**
     * To install feature repo in karaf command prefix
     */
    public static final String FEATURE_INSTALL_CMD = "feature:install ";

    /**
     * To GEt feature Info in karaf command prefix
     */
    public static final String FEATURE_DEPENDENCY_INFO_CMD = "feature:info -d ";

    public static final String STATIC_CONSTATN_FIELD_KARAF_CONF_PATH = "KARAF_CONF_PATH";

    /**
     * @return Karaf Conf complete path
     */
    public static final String KARAF_CONF_PATH = KARAF_HOME + File.separator + KARAF_CONF + File.separator;

}
