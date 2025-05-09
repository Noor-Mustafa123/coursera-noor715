package de.soco.software.simuspace.suscore.auth.connect.ldap.authentication;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;

/**
 * @author fahad A properties file reader for reading the directory configurations.
 */
public class PropertiesFileReader {

    /**
     * static final field for the properties file configuea
     */
    private static final String DIRECTORY_CONFIG_PROPERTIES_FILE = "directoryConfig.properties";

    /**
     * private constructor to avoid instantiation.
     */
    private PropertiesFileReader() {

    }

    /**
     * Read properties file.
     *
     * @param string
     *         the key
     *
     * @return the string path
     */
    public static String readPropertiesFile( String key ) {
        Properties prop = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource( DIRECTORY_CONFIG_PROPERTIES_FILE );
        try ( FileInputStream fis = new FileInputStream( new URI( url.toString() ).getPath() ) ) {
            prop.load( fis );
            return prop.getProperty( key );
        } catch ( IOException | URISyntaxException ex ) {
            ExceptionLogger.logException( ex, ex.getClass() );
        }
        return key;
    }

}
