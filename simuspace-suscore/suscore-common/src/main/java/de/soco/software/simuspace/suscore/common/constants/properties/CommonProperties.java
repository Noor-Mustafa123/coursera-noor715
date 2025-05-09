package de.soco.software.simuspace.suscore.common.constants.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;

/**
 * The Class CommonProperties loads properties for api hits when karaf is down and jobs are runing.
 *
 * @author noman arshad
 */
@Log4j2
public class CommonProperties {

    /**
     * The properties.
     */
    public static Properties properties;

    /**
     * The api expiry time when karaf down.
     */
    public static long API_EXPIRY_TIME_WHEN_KARAF_DOWN = 0; // 7200*1000

    /**
     * The api call delay between karaf down.
     */
    public static int API_CALL_DELAY_BETWEEN_KARAF_DOWN = 0; // 10000

    /**
     * Instantiates a new common properties.
     */
    public CommonProperties() {
    }

    /**
     * Inits the.
     */
    public static void init() {
        log.info( "Properties Loading  from Both loacation  Local user and karaf" );
        properties = new Properties();

        final String pathLocal = System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator
                + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY + File.separator + ConstantsString.CONFIG + File.separator
                + ConstantsString.WORKFLOW_EXECUTION;

        File file = getFileFromKarafConf( pathLocal );
        if ( file != null ) {
            try ( InputStream fileStream = new FileInputStream( file ) ) {
                properties.load( fileStream );
                API_EXPIRY_TIME_WHEN_KARAF_DOWN = Long.parseLong( properties.getProperty( "api_timeout_retry_ms" ) );
                API_CALL_DELAY_BETWEEN_KARAF_DOWN = Integer.parseInt( properties.getProperty( "api_wait_ms" ) );

            } catch ( IOException e ) {
                log.warn( e.getMessage() );
            }
        }
    }

    /**
     * Gets the expiry time when karaf down.
     *
     * @return the expiry time when karaf down
     */
    public static long getExpiryTimeWhenKarafDown() {
        if ( API_EXPIRY_TIME_WHEN_KARAF_DOWN == 0 ) {
            init();
        }
        return API_EXPIRY_TIME_WHEN_KARAF_DOWN;
    }

    /**
     * Gets the expiry time between karaf down.
     *
     * @return the expiry time between karaf down
     */
    public static int getDelayBetweenKarafDown() {
        if ( API_CALL_DELAY_BETWEEN_KARAF_DOWN == 0 ) {
            init();
        }
        return API_CALL_DELAY_BETWEEN_KARAF_DOWN;
    }

    /**
     * Gets the file from karaf conf.
     *
     * @param path
     *         the path
     *
     * @return the file from karaf conf
     */
    public static File getFileFromKarafConf( String path ) {
        log.debug( "Reading common susClient file" );
        File file = new File( path );
        if ( !file.exists() ) {
            String pathKaraf = ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + File.separator
                    + ConstantsString.WORKFLOW_EXECUTION;
            file = new File( pathKaraf );
            if ( !file.exists() ) {
                return null;
            }
        }
        log.debug( "Reading success" );
        return file;
    }

}
