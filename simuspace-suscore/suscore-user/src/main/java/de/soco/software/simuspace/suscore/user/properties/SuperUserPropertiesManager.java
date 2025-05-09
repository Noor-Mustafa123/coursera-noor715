package de.soco.software.simuspace.suscore.user.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;

/**
 * A singleton utility class that would load the sus.superuser.cfg file properties.
 *
 * @author Zeeshan jamal
 */
public class SuperUserPropertiesManager {

    /**
     * The instance.
     */
    private static SuperUserPropertiesManager instance = null;

    /**
     * The configurable properties to read from liquibase.properties
     */
    private static Properties properties;

    /**
     * Instantiates a new properties manager.
     */
    private SuperUserPropertiesManager() {
        init();
    }

    /**
     * Gets the single instance of PropertiesManager.
     *
     * @return single instance of PropertiesManager
     */
    public static SuperUserPropertiesManager getInstance() {
        if ( instance == null ) {
            instance = new SuperUserPropertiesManager();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
        getInstance();
    }

    /**
     * A method for Configuration initialization!
     */
    public void init() {
        createConfiguration();
    }

    private static void createConfiguration() {
        properties = new Properties();
        final String sus_superuser_cfg = ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + File.separator
                + ConstantsFileProperties.SUPER_USER_CFG;
        try ( InputStream superUserCfg = new FileInputStream( sus_superuser_cfg ) ) {
            properties.load( superUserCfg );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, SuperUserPropertiesManager.class );
        }

    }

    /**
     * A method for reading property value by property key
     *
     * @param key
     *         the key
     *
     * @return properties property
     */
    public String getProperty( String key ) {
        return properties.getProperty( key );
    }

}
