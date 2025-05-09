package de.soco.software.simuspace.suscore.local.daemon.constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;

/**
 * The Class DaemonThreadPoolProperties.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class DaemonThreadPoolProperties {

    /**
     * The properties.
     */
    private static Properties properties;

    /**
     * Instantiates a new daemon thread pool properties.
     */
    DaemonThreadPoolProperties() {
    }

    /**
     * Gets the executor properties.
     *
     * @param poolName
     *         the pool name
     *
     * @return the executor properties
     */
    public static String getExecutorProperties( String poolName ) {
        if ( properties == null ) {
            locadDaemonThreadPoolProperties();
        }
        return properties.getProperty( poolName );
    }

    /**
     * Locad daemon thread pool properties.
     */
    public static void locadDaemonThreadPoolProperties() {
        final String propFileName = System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator
                + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY + File.separator + ConstantsString.CONFIG + File.separator
                + ConstantsString.DAEMON_CONF_FILE_NAME;
        properties = new Properties();
        try ( InputStream propFileNameStream = new FileInputStream( propFileName ) ) {
            properties.load( propFileNameStream );
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

    }

    /**
     * Gets the core max size.
     *
     * @return the core max size
     */
    public static String getCoreMaxSize() {
        return getExecutorProperties( "coreMaxSize" );
    }

    /**
     * Gets the queue size.
     *
     * @return the queue size
     */
    public static String getQueueSize() {
        return getExecutorProperties( "queueSize" );
    }

    /**
     * Gets the core pool size.
     *
     * @return the core pool size
     */
    public static String getCorePoolSize() {
        return getExecutorProperties( "corePoolSize" );
    }

    /**
     * Gets the keep alive time.
     *
     * @return the keep alive time
     */
    public static String getKeepAliveTime() {
        return getExecutorProperties( "keepAliveTime" );
    }

}
