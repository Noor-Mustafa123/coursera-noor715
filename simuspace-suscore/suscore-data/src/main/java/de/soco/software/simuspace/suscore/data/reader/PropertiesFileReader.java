package de.soco.software.simuspace.suscore.data.reader;

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

    public static String readJSONFromPropertiesFile( String key ) {
        Properties prop = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource( "sus.simuspace.cfg" );
        try ( FileInputStream fis = new FileInputStream( new URI( url.toString() ).getPath() ) ) {
            prop.load( fis );
            return prop.getProperty( key );
        } catch ( IOException | URISyntaxException ex ) {
            ExceptionLogger.logException( ex, PropertiesFileReader.class );
        }
        return key;
    }

}
