package de.soco.software.suscore.jsonschema.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.ConfigUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * @author Ahmar Nadeem
 *
 * This class helps to get the complete path of the file from the given path.
 */
@Log4j2
public class ConfigFilePathReader {

    /**
     * The Constant PROJECTS_CONFIG_KEY.
     */
    private static final String PROJECTS_CONFIG_KEY = "projectsConfig";

    /**
     * The Constant WORKFLOW_PROJECTS_CONFIG_KEY.
     */
    private static final String WORKFLOW_PROJECTS_CONFIG_KEY = "workflowsConfig";

    /**
     * Private constructor to avoid instantiation of class.
     */
    private ConfigFilePathReader() {
        super();
    }

    /**
     * The Constant PROPERTIES_FILE_PATH_FIELD_NAME.
     */
    public static final String PROPERTIES_FILE_PATH_FIELD_NAME = "PROPERTIES_FILE_PATH";

    /**
     * The properties file name with complete path
     */
    public static final String PROPERTIES_FILE_PATH = ConstantsKaraf.KARAF_CONF_PATH + File.separator
            + ConstantsFileProperties.SUSCORE_SIMUSPACE_CFG;

    public static final String PROPERTIES_SCHEME_PLOT = ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.SUSCORE_SCHEME_PLOT;

    /**
     * The Constant STATIC_METHOD_GET_MASTER_CONFIGURATION_FILE_NAMES.
     */
    public static final String STATIC_METHOD_GET_MASTER_CONFIGURATION_FILE_NAMES = "getMasterConfigurationFileNames";

    /**
     * The properties loaded into the memory
     */
    private static Properties configProperties = new Properties();

    /**
     * Utility method for getting the value of properties from directory config file in karaf conf folder.
     *
     * @param propertyName
     *         the propertyName
     *
     * @return the property value
     */
    public static String getValueByNameFromPropertiesFileInKaraf( String propertyName, String propertiesFileName ) {
        String directoryConfig = null;
        Properties properties = new Properties();
        directoryConfig = ConstantsKaraf.KARAF_CONF_PATH + propertiesFileName;
        try ( InputStream directoryConfigStream = new FileInputStream( directoryConfig ) ) {
            properties.load( directoryConfigStream );
            return properties.getProperty( propertyName );
        } catch ( final IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), directoryConfig ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), directoryConfig ) );
        }
    }

    /**
     * Utility method for getting the value of properties from directory config file in karaf conf folder.
     *
     * @param propertyName
     *         the propertyName
     *
     * @return the file's input stream
     */
    public static InputStream getFileByNameFromKaraf( String fileName ) {
        String filepath = null;
        try {
            filepath = ConstantsKaraf.KARAF_CONF_PATH + fileName;
            return new FileInputStream( filepath );
        } catch ( FileNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), filepath ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), filepath ) );
        }
    }

    /**
     * @param key
     *         the key
     *
     * @return the absolute file path of the master configuration after reading it from the properties file using the submitted key
     */
    public static String getMasterConfigFileAbsolutePath( String key ) {
        if ( !configProperties.containsKey( key ) ) {
            loadPropertiesFile();
        }
        return getCompleteFilePath( configProperties.getProperty( key ) );
    }

    /**
     * Gets the master configurations file names.
     *
     * @param key
     *         the key
     *
     * @return the absolute file path of the master configuration after reading it from the properties file using the submitted key
     *
     * @throws IOException
     *         in case of error
     */
    public static List< ConfigUtil.Config > getMasterConfigurationFileNames( String key ) throws IOException {
        if ( !configProperties.containsKey( key ) ) {
            loadPropertiesFile();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map< String, List< ConfigUtil.Config > > values = objectMapper.readValue(
                new File( getCompleteFilePath( configProperties.getProperty( key ) ) ),
                new TypeReference< Map< String, List< ConfigUtil.Config > > >() {

                } );
        return values.get( PROJECTS_CONFIG_KEY );
    }

    /**
     * Gets the master configuration file names for workflows.
     *
     * @param key
     *         the key
     *
     * @return the master configuration file names for workflows
     *
     * @throws IOException
     *         in case of arrows
     */
    public static List< ConfigUtil.Config > getMasterConfigurationFileNamesForWorkflows( String key ) throws IOException {
        if ( !configProperties.containsKey( key ) ) {
            loadPropertiesFile();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map< String, List< ConfigUtil.Config > > values = objectMapper.readValue(
                new File( getCompleteFilePath( configProperties.getProperty( key ) ) ),
                new TypeReference< Map< String, List< ConfigUtil.Config > > >() {

                } );
        return values.get( WORKFLOW_PROJECTS_CONFIG_KEY );

    }

    /**
     * @param fileName
     *         the fileName
     *
     * @return the complete file path using the karaf's conf directory
     */
    public static String getCompleteFilePath( final String fileName ) {
        String filePath = ConstantsKaraf.KARAF_CONF_PATH + File.separator + fileName;
        log.info( "The absolute file path is: " + filePath );
        return filePath;
    }

    /**
     * This function is responsible to return the property value against the provided key from config.properties file. It also takes care of
     * loading the properties file into memory if not already loaded.
     *
     * @param key
     *         the key
     *
     * @return the corresponding value of the provided key
     */
    public static String getPropertyValueFromConfiguration( String key ) {
        if ( !configProperties.containsKey( key ) ) {
            loadPropertiesFile();
        }
        Object propValue = configProperties.get( key );
        return propValue == null ? null : propValue.toString();
    }

    /**
     * <p>
     * This function is responsible to return the boolean property value against the provided key from config.properties file. <b>A null
     * property is returned as FALSE.</b>
     * <p>
     * It also takes care of loading the properties file into memory if not already loaded.
     *
     * @param key
     *         the key
     *
     * @return the corresponding value of the provided key
     */
    public static boolean getBooleanPropertyValueFromConfiguration( String key ) {
        String property = getPropertyValueFromConfiguration( key );
        if ( property == null ) {
            return false;
        } else {
            return Boolean.parseBoolean( getPropertyValueFromConfiguration( key ) );
        }
    }

    /**
     * Fetch property.
     *
     * @param property
     *         the property
     *
     * @return the properties
     */
    public static Properties fetchProperty( String property ) {
        Properties prop = new Properties();
        try ( InputStream input = new FileInputStream( property ) ) {
            prop.load( input );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FETCHING_PROPERTY_ISSUE.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FETCHING_PROPERTY_ISSUE.getKey() ) );
        }
        return prop;
    }

    /**
     * A helper function to load the properties file into memory.
     */
    private static void loadPropertiesFile() {
        log.info( "Loading properties file from path: " + ConfigFilePathReader.PROPERTIES_FILE_PATH );
        try ( InputStream proFileStream = new FileInputStream( ConfigFilePathReader.PROPERTIES_FILE_PATH ) ) {
            configProperties.load( proFileStream );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), PROPERTIES_FILE_PATH ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), PROPERTIES_FILE_PATH ) );
        }
    }

    /**
     * @param configProperties
     *         the configProperties to set
     */
    public static void setConfigProperties( Properties configProperties ) {
        ConfigFilePathReader.configProperties = configProperties;
    }

}
