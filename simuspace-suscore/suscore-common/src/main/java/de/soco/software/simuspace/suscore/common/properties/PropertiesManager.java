package de.soco.software.simuspace.suscore.common.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.collections4.MapUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.DashboardPluginEnums;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DashboardPluginDTO;
import de.soco.software.simuspace.suscore.common.model.DynamicScript;
import de.soco.software.simuspace.suscore.common.model.EncryptionConfiguration;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.OverviewPluginDTO;
import de.soco.software.simuspace.suscore.common.model.PythonEnvironmentDTO;
import de.soco.software.simuspace.suscore.common.model.WidgetCategoryGroupDTO;
import de.soco.software.simuspace.suscore.common.model.WidgetDTO;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.Hosts;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.suscore.jsonschema.model.FFmpegConfig;
import de.soco.software.suscore.jsonschema.model.UnitsFamily;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * A singleton utility class that would load the sus.simuspace.cfg file properties.
 *
 * @author Zeeshan jamal
 */
@Log4j2
public class PropertiesManager {

    public static final String BUILT_IN = "builtIn";

    public static final String PREVIEW = "preview";

    /**
     * Singleton instance of PropertiesManager. This AtomicReference holds the singleton instance of the PropertiesManager class, ensuring
     * thread-safe lazy initialization.
     */
    private static final AtomicReference< PropertiesManager > INSTANCE = new AtomicReference<>();

    /**
     * A static Properties object that holds configuration settings and key-value pairs. This variable is used for managing application-wide
     * properties.
     */
    private static Properties properties;

    /**
     * The properties CB 2.
     */
    private static Properties propertiesCB2;

    /**
     * The constant propertiesWebEam.
     */
    private static Properties propertiesWebEam;

    /**
     * The constant propertiesShapeModule.
     */
    private static Properties propertiesShapeModule;

    /**
     * The properties audit.
     */
    private static Properties propertiesAudit;

    /**
     * The properties els.
     */
    private static Properties propertiesEls;

    /**
     * The properties costcenter regex.
     */
    private static Properties propertiesCostcenter;

    /**
     * The constant dashboardProperties.
     */
    private static Properties dashboardProperties;

    /**
     * The ffmpeg formates.
     */
    private static FFmpegConfig ffmpegConfig;

    /**
     * The convertion units.
     */
    private static List< UnitsFamily > convertionUnits;

    /**
     * The parser.
     */
    private static Map< String, Object > parser;

    /**
     * The dynamic prop.
     */
    private static Map< String, String > dynamicProp;

    /**
     * The img mov format prop.
     */
    private static Map< String, List< String > > imgMovFormatProp;

    /**
     * The dashboard plugins.
     */
    @Deprecated( since = "soco/2.3.1/release" )
    private static Map< String, List< Object > > dashboardPlugins;

    /**
     * The Homepage widgets.
     */
    private static Map< String, Map< String, List< WidgetDTO > > > homepageWidgets;

    /**
     * The overview plugins.
     */
    private static Map< String, List< Object > > overviewPlugins;

    /**
     * The custom views.
     */
    private static Map< String, List< Object > > customViews;

    /**
     * The custom views.
     */
    private static Map< String, List< String > > fileExtensions;

    /**
     * The language props.
     */
    private static Map< String, Object > languageProps;

    /**
     * The encryption config.
     */
    private static EncryptionConfiguration encryptionConfigs;

    /**
     * The constant propertiesCeetron.
     */
    private static JsonNode propertiesCeetron;

    /**
     * The dynamicScripts map from dynamic_scripts.json
     */
    private static Map< String, List< Object > > dynamicScripts;

    /**
     * The pythonEnvironments map from py.env.json
     */
    private static Map< String, List< Object > > pythonEnvironments;

    /**
     * Returns the singleton instance of PropertiesManager. Ensures that only one instance of the PropertiesManager class is created and
     * provides a global point of access to it.
     *
     * @return the singleton instance of PropertiesManager
     */
    @Synchronized
    public static PropertiesManager getInstance() {
        if ( INSTANCE.get() == null ) {
            INSTANCE.set( new PropertiesManager() );
        }
        return INSTANCE.get();
    }

    /**
     * Private constructor for the PropertiesManager class. Initializes the class by invoking the init() method.
     */
    private PropertiesManager() {
        init();
    }

    /**
     * Resets the singleton instance to null and then initializes a new instance. This method first sets the current INSTANCE to null and
     * then calls {@code getInstance()} to ensure a new instance is created.
     */
    public static void resetInstance() {
        INSTANCE.set( null );
        getInstance();
    }

    /**
     * A method for Configuration initialization!.
     */
    public void init() {
        createConfiguration();
        ffmpegFileFormatesConfig();
        conversionUnitsConfig();
        loadCB2Configuration();
        loadParserConfiguration();
        loadDynamicConfiguration();
        loadDashboardPlugins();
        createConfigurationForAudit();
        loadEncryptionConfig();
        loadCeetronConfiguration();
        loadActionsConfiguration();
        loadPythonEnvironments();
        loadWebEAMConfiguration();
        loadElsConfig();
        loadShapeModuleConfig();
        loadHpcCostcenterRegexConfig();
        loadLanguageConfiguration();
        loadImageMovieFormatConfiguration();
        loadCustomViews();
        loadFileExtensions();
        loadOverviewPlugins();
        loadDashboardProperties();
        loadHomepageProperties();
    }

    /**
     * Load dashboard properties.
     */
    private void loadDashboardProperties() {
        dashboardProperties = loadPropertiesFromCfg( ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.SUS_DASHBOARDS_CFG );
    }

    /**
     * Creates the configuration.
     */
    private static void createConfiguration() {
        properties = loadPropertiesFromCfg( ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.SUSCORE_SIMUSPACE_CFG );
    }

    /**
     * Creates the configuration for audit.
     */
    private static void createConfigurationForAudit() {
        propertiesAudit = loadPropertiesFromCfg( ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.SUSCORE_AUDIT_CFG );
    }

    /**
     * Load CB 2 configuration.
     */
    private static void loadCB2Configuration() {
        propertiesCB2 = loadPropertiesFromCfg( ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.SUSCORE_CB2_CFG );
    }

    /**
     * Load web eam configuration.
     */
    private static void loadWebEAMConfiguration() {
        propertiesWebEam = loadPropertiesFromCfg( ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.WEB_EAM_CFG );
    }

    /**
     * Load els config.
     */
    private static void loadElsConfig() {
        propertiesEls = loadPropertiesFromCfg( ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.SUSCORE_ELASTICSEARCH_CFG );
    }

    /**
     * Load shape module configuration.
     */
    private static void loadShapeModuleConfig() {
        propertiesShapeModule = loadPropertiesFromCfg( ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.SUSCORE_SHAPEMODULE_CFG );
    }

    /**
     * Load hpc costcenter regex config.
     */
    private static void loadHpcCostcenterRegexConfig() {
        propertiesCostcenter = loadPropertiesFromCfg(
                ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.SUSCORE_HPC_COSTCENTERS_CFG );
    }

    /**
     * Load cfg file from fileName and returns Properties
     *
     * @param fileName
     *         the file name
     *
     * @return properties properties
     */
    private static Properties loadPropertiesFromCfg( String fileName ) {
        Properties prop = new Properties();
        try ( InputStream stream = new FileInputStream( fileName ) ) {
            prop.load( stream );
        } catch ( FileNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), fileName ), e );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), fileName ), e );
        }
        return prop;
    }

    /**
     * Load ceetron configuration.
     */
    private static void loadCeetronConfiguration() {
        final String sus_ceetron_json = ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.CEETRON_JSON;
        try ( InputStream dummyFileStream = new FileInputStream( sus_ceetron_json ) ) {
            propertiesCeetron = JsonUtils.convertInputStreamToJsonNode( dummyFileStream );
        } catch ( FileNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), sus_ceetron_json ), e );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), sus_ceetron_json ), e );
        }
    }

    /**
     * Load action configurations from dynamic_scripts.json
     */
    private static void loadActionsConfiguration() {
        try {
            dynamicScripts = ( Map< String, List< Object > > ) JsonUtils.jsonConfToMap(
                    ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.SUS_ACTIONS );
        } catch ( final Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * load Python Environments from py.env.json
     */
    private static void loadPythonEnvironments() {
        try {
            pythonEnvironments = ( Map< String, List< Object > > ) JsonUtils.jsonConfToMap(
                    ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.PY_ENV );
        } catch ( final Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Load Parser configuration.
     */
    private static void loadParserConfiguration() {
        try {
            parser = ( Map< String, Object > ) JsonUtils.jsonConfToMap( ConstantsKaraf.KARAF_CONF_PATH + ConstantsString.SUS_PARSER_JSON );
        } catch ( Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Load language configuration.
     */
    private static void loadLanguageConfiguration() {
        try {
            languageProps = ( Map< String, Object > ) JsonUtils.jsonConfToMap(
                    ConstantsKaraf.KARAF_CONF_PATH + ConstantsString.SUS_LANGUAGE_JSON );
        } catch ( Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Load Dynamic Properties configuration.
     */
    private static void loadDynamicConfiguration() {
        try {
            dynamicProp = ( Map< String, String > ) JsonUtils.jsonConfToMap(
                    ConstantsKaraf.KARAF_CONF_PATH + ConstantsString.DYNAMIC_PROPERTIES );
        } catch ( Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Load image movie format configuration.
     */
    private static void loadImageMovieFormatConfiguration() {
        try {
            imgMovFormatProp = ( Map< String, List< String > > ) JsonUtils.jsonConfToMap(
                    ConstantsKaraf.KARAF_CONF_PATH + ConstantsString.IMG_MOV_FORMAT_PROPERTIES );
        } catch ( Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Load Dashboard Plugins configuration.
     */
    private static void loadDashboardPlugins() {
        try {
            dashboardPlugins = ( Map< String, List< Object > > ) JsonUtils.jsonConfToMap(
                    ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.DASHBOARD_PLUGINS_JSON );
        } catch ( Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Load homepage properties.
     */
    private void loadHomepageProperties() {
        try {
            homepageWidgets = ( Map< String, Map< String, List< WidgetDTO > > > ) JsonUtils.jsonConfToMap(
                    ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.HOMEPAGE_JSON );
        } catch ( Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Load overview plugins.
     */
    @Deprecated( since = "soco/2.3.1/release" )
    private static void loadOverviewPlugins() {
        try {
            overviewPlugins = ( Map< String, List< Object > > ) JsonUtils.jsonConfToMap(
                    ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.OVERVIEW_PLUGINS_JSON );
        } catch ( Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Load custom views.
     */
    private static void loadCustomViews() {
        try {
            customViews = ( Map< String, List< Object > > ) JsonUtils.jsonConfToMap(
                    ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.CUSTOM_VIEWS );
        } catch ( Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Load custom views.
     */
    private static void loadFileExtensions() {
        try {
            fileExtensions = ( Map< String, List< String > > ) JsonUtils.jsonConfToMap(
                    ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.FILE_EXTENSIONS );
        } catch ( Exception e ) {
            log.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Ffmpeg file formates config.
     */
    public static void ffmpegFileFormatesConfig() {
        final String propFileName = ConstantsKaraf.KARAF_CONF_PATH + ConstantsString.Ffmpeg_CONF_FILE_NAME;
        final File file = new File( propFileName );

        try ( final InputStream targetConfigStream = new FileInputStream( file ) ) {
            ffmpegConfig = JsonUtils.jsonToObject( targetConfigStream, FFmpegConfig.class );
        } catch ( FileNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), propFileName ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), propFileName ) );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), propFileName ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), propFileName ) );
        }
    }

    /**
     * Convertion units config.
     */
    public static void conversionUnitsConfig() {
        final String propFileName = ConstantsKaraf.KARAF_CONF_PATH + ConstantsString.SUS_SI_SYSTEM_UNITS_FILE;

        try ( FileReader reader = new FileReader( propFileName ) ) {
            JSONParser jsonParser = new JSONParser();
            Object objFile = jsonParser.parse( reader );
            convertionUnits = JsonUtils.jsonToList( JsonUtils.toJson( objFile ), UnitsFamily.class );
        } catch ( FileNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), propFileName ), e );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), propFileName ), e );
        } catch ( ParseException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_PARSE_JSON_FILE.getKey(), propFileName ), e );
        }
    }

    /**
     * Loads the encryption configurations.
     */
    public static void loadEncryptionConfig() {
        String filePath = ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.ENCRP_DECRP_FILE;
        try {
            String encryptionConfigJson = new String( Files.readAllBytes( Paths.get( filePath ) ) );
            encryptionConfigs = JsonUtils.jsonToObject( encryptionConfigJson, EncryptionConfiguration.class );
        } catch ( FileNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), filePath ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), filePath ) );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), filePath ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), filePath ) );
        }
    }

    /**
     * A method for reading property value by property key.
     *
     * @param key
     *         the key
     *
     * @return properties property
     */
    public String getProperty( String key ) {
        return properties.getProperty( key );
    }

    /**
     * Gets the vault path.
     *
     * @return the vault path
     */
    public static String getVaultPath() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.VAULT_PATH );
    }

    /**
     * Gets overview script path.
     *
     * @return the overview script path
     */
    public static String getOverviewScriptPath() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.PROJECT_OVERVIEW_SCRIPT )
                .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, getScriptsPath() );
    }

    /**
     * Gets ceetron config by object type.
     *
     * @param objectType
     *         the object type
     *
     * @return the ceetron config by object type
     */
    public static JsonNode getCeetronConfigByObjectType( String objectType ) {
        JsonNode formate = propertiesCeetron.get( "formate" );
        if ( formate.isArray() ) {
            for ( JsonNode jsonNode : formate ) {
                if ( jsonNode.get( "name" ).asText().equals( objectType ) && jsonNode.has( "defaultConfig" ) ) {
                    return jsonNode.get( "defaultConfig" );
                }
            }
        }
        return null;

    }

    /**
     * Gets ceetron output path.
     *
     * @return the ceetron output path
     */
    public static JsonNode getCeetronOutputPath() {
        return propertiesCeetron.get( "outputPath" );
    }

    /**
     * gets script options from actions config
     *
     * @return the susAction scripts
     */
    public static List< DynamicScript > getSusActionsScripts() {
        List< Object > actionScriptsObj = PropertiesManager.dynamicScripts.get( "scripts" );
        List< DynamicScript > dynamicScripts = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( actionScriptsObj ) ) {
            actionScriptsObj.forEach( actionScriptObj -> {
                DynamicScript dynamicScript = JsonUtils.jsonToObject( JsonUtils.toJson( actionScriptObj ), DynamicScript.class );
                dynamicScript.setPath(
                        dynamicScript.getPath().replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, getScriptsPath() ) );
                dynamicScripts.add( dynamicScript );
            } );
        }
        return dynamicScripts;
    }

    /**
     * gets script options from actions config
     *
     * @param key
     *         the key
     *
     * @return the susAction scripts
     */
    public static List< DynamicScript > getDynamicScriptsByKey( String key ) {
        List< Object > dynamicScriptsObj = PropertiesManager.dynamicScripts.get( key );
        List< DynamicScript > dynamicScripts = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( dynamicScriptsObj ) ) {
            dynamicScriptsObj.forEach( actionScriptObj -> {
                DynamicScript dynamicScript = JsonUtils.jsonToObject( JsonUtils.toJson( actionScriptObj ), DynamicScript.class );
                dynamicScript.setPath(
                        dynamicScript.getPath().replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, getScriptsPath() ) );
                dynamicScripts.add( dynamicScript );
            } );
        }
        return dynamicScripts;
    }

    /**
     * Gets the python environments.
     *
     * @return the python environments
     */
    public static List< PythonEnvironmentDTO > getPythonEnvironments() {
        List< Object > pythonEnvironmentObjs = pythonEnvironments.get( "environments" );
        List< PythonEnvironmentDTO > pythonEnvironments = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( pythonEnvironmentObjs ) ) {
            pythonEnvironmentObjs.forEach( pythonEnvironmentObj -> {
                PythonEnvironmentDTO envDTO = JsonUtils.jsonToObject( JsonUtils.toJson( pythonEnvironmentObj ),
                        PythonEnvironmentDTO.class );
                pythonEnvironments.add( envDTO );
            } );
        }
        return pythonEnvironments;
    }

    /**
     * Gets ceetron conversion command.
     *
     * @return the ceetron conversion command
     */
    public static String getCeetronConversionCommand() {
        return propertiesCeetron.get( ConstantsFileProperties.CONVERSION_COMMAND ).asText()
                .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, getScriptsPath() );
    }

    /**
     * Gets ceetron server url.
     *
     * @return the ceetron server url
     */
    public static JsonNode getCeetronServerUrl() {
        return propertiesCeetron.get( "serverPath" );
    }

    /**
     * Gets regex context line.
     *
     * @return the regex context line
     */
    public static String getRegexContextLine() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.REGEX_CONTEXT_LINE );
    }

    /**
     * Gets the hpc cost center command.
     *
     * @return the hpc cost center command
     */
    public static String getHpcCostCenterCommand() {
        return propertiesCostcenter.getProperty( ConstantsFileProperties.HPC_COST_CENTER_COMAND );
    }

    /**
     * Gets if config path.
     *
     * @return the if config path
     */
    public static String getIfConfigPath() {
        return properties.getProperty( ConstantsFileProperties.IFCONFIG_BASEPATH_KEY );
    }

    /**
     * Gets session expiry.
     *
     * @return the session expiry
     */
    public static String getSessionExpiry() {
        return properties.getProperty( ConstantsFileProperties.USER_SESSION_EXPIRY );
    }

    /**
     * Gets the java run time path.
     *
     * @return the java run time path
     */
    public static String getJavaRunTimePath() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.JAVA_PATH );
    }

    /**
     * Gets scheme template file size.
     *
     * @return the scheme template file size
     */
    public static String getSchemeTemplateFileSize() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SCHEME_TEMPLATE_FILE_SIZE );
    }

    /**
     * Gets the elastic search URL.
     *
     * @return the elastic search URL
     */
    public static String getElasticSearchURL() {
        return propertiesEls.getProperty( ConstantsFileProperties.ELS_URL );
    }

    /**
     * Gets the costcenter regex.
     *
     * @param solver
     *         the solver
     *
     * @return the costcenter regex
     */
    public static String getCostcenterRegex( String solver ) {
        return propertiesCostcenter.getProperty( solver );
    }

    /**
     * Enable elastic search.
     *
     * @return true, if successful
     */
    public static boolean enableElasticSearch() {
        return getBooleanPropertyValueFromConfiguration( propertiesEls.getProperty( ConstantsFileProperties.ELS_ENABLE ) );
    }

    /**
     * Re index data.
     *
     * @return the string
     */
    public static boolean reindexElasticSearch() {
        return getBooleanPropertyValueFromConfiguration( propertiesEls.getProperty( ConstantsFileProperties.ELS_REINDEX ) );
    }

    /**
     * Gets the boolean property value from configuration.
     *
     * @param value
     *         the value
     *
     * @return the boolean property value from configuration
     */
    public static boolean getBooleanPropertyValueFromConfiguration( String value ) {
        if ( value == null ) {
            return false;
        } else {
            return Boolean.parseBoolean( value );
        }
    }

    /**
     * Gets the staging path.
     *
     * @return the staging path
     */
    public static String getStagingPath() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.STAGING_PATH );
    }

    /**
     * Gets the user staging path.
     *
     * @param userUid
     *         the user uid
     *
     * @return the user staging path
     */
    public static String getUserStagingPath( String userUid ) {
        if ( userUid == null ) {
            return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.STAGING_PATH );
        } else {
            return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.STAGING_PATH ) + File.separator + userUid;
        }
    }

    /**
     * Gets the user staging path.
     *
     * @param userUid
     *         the user uid
     * @param stagingPath
     *         the staging path
     *
     * @return the user staging path
     */
    public static String getUserStagingPathFromStaging( String userUid, String stagingPath ) {
        if ( userUid == null ) {
            return stagingPath;
        } else {
            return stagingPath + File.separator + userUid;
        }
    }

    /**
     * Gets the karaf path.
     *
     * @return the karaf path
     */
    public static String getKarafPath() {
        return ( ConstantsKaraf.KARAF_HOME == null ) ? ConstantsString.EMPTY_STRING : ConstantsKaraf.KARAF_HOME;
    }

    /**
     * Gets karaf scripts path.
     *
     * @return the karaf scripts path
     */
    public static String getScriptsPath() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.MISC_BASE_PATH ) + "/scripts";
    }

    /**
     * Gets an engine path.
     *
     * @return the engine path
     */
    public static String getEnginePath() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.MISC_BASE_PATH ) + "/engine";
    }

    /**
     * Gets email conf.
     *
     * @return the email conf
     */
    public static String getEmailConf() {
        return getEnginePath() + ConstantsString.EMAIL_CONFIG_FILE;
    }

    /**
     * Gets the f fmpeg path.
     *
     * @return the f fmpeg path
     */
    public static String getFFmpegPath() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.FFMPEG_PATH ) + File.separator;
    }

    /**
     * Gets the plunger url.
     *
     * @return the plunger url
     */
    public static String getPlungerUrl() {
        List< DashboardPluginDTO > validOptions = PropertiesManager.getDashboardPlugins( "plugin" );
        if ( CollectionUtil.isNotEmpty( validOptions ) ) {
            for ( Object op : validOptions ) {
                DashboardPluginDTO dashboardPluginDTO = JsonUtils.jsonToObject( JsonUtils.toJson( op ), DashboardPluginDTO.class );
                if ( DashboardPluginEnums.PLUNGERLIFT.getName().equals( dashboardPluginDTO.getName() ) ) {
                    return dashboardPluginDTO.getUrl();

                }

            }
        }
        return null;
    }

    /**
     * Gets the Fe-Static path.
     *
     * @return the FE-Static path
     */
    public static String getFeStaticPath() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.FE_STATIC );
    }

    /**
     * Gets the default server temp path.
     *
     * @return the default server temp path
     */
    public static String getDefaultServerTempPath() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.DEFAULT_SERVER_TEMP_PATH );
    }

    /**
     * Gets allowed origins.
     *
     * @return the allowed origins
     */
    public static List< String > getAllowedOrigins() {
        String allowedOrigins = PropertiesManager.getInstance().getProperty( ConstantsFileProperties.ALLOWED_ORIGINS );
        if ( allowedOrigins.contains( ConstantsString.COMMA ) ) {
            var split = allowedOrigins.split( ConstantsString.COMMA );
            return Arrays.stream( split ).map( String::trim ).toList();
        }
        return Collections.singletonList( allowedOrigins.trim() );
    }

    /**
     * Gets server cache path.
     *
     * @return the server cache path
     */
    public static String getServerCachePath() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SERVER_CACHE );
    }

    /*** Gets the default server temp path.
     *
     * @param userUid the user uid
     * @return the default server temp path
     */
    public static String getUserDefaultServerTempPath( String userUid ) {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.DEFAULT_SERVER_TEMP_PATH ) + File.separator + userUid;
    }

    /**
     * Gets the failed login attempt.
     *
     * @return the failed login attempt
     */
    public static String getFailedLoginAttempt() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.FAILED_LOGIN_ATTEMPTS );
    }

    /**
     * Gets account lock time for failed attempts.
     *
     * @return the account lock time for failed attempts
     */
    public static String getAccountLockTimeForFailedAttempts() {
        var minutes = PropertiesManager.getInstance().getProperty( ConstantsFileProperties.FAILED_LOGIN_ATTEMPTS_LOCK_TIME );
        return minutes != null ? minutes : "15";
    }

    /**
     * Gets the web base URL.
     *
     * @return the web base URL
     */
    public static String getWebBaseURL() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_BASE_URL );
    }

    /**
     * Gets the project label icon.
     *
     * @return the project label icon
     */
    public static String getProjectLabelIcon() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.PROJECT_LABLE_ICON );
    }

    /**
     * Checks if is impersonated.
     *
     * @return true, if is impersonated
     */
    public static boolean isImpersonated() {
        return "ON".equalsIgnoreCase( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.IMPERSONATED ) );
    }

    /**
     * Checks if is host enable.
     *
     * @return the boolean
     */
    public static Boolean isHostEnable() {
        return Boolean.parseBoolean( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.EXECUTE_ON_REMOTE ) );
    }

    /**
     * Gets the location name.
     *
     * @return the location name
     */
    public static String getLocationName() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.LOCATION_NAME );
    }

    /**
     * Gets the location auth token.
     *
     * @return the location auth token
     */
    public static String getLocationAuthToken() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.LOCATION_TOKEN );
    }

    /**
     * Gets the location URL.
     *
     * @return the location URL
     */
    public static String getLocationURL() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.LOCATION_URL );
    }

    /**
     * Checks if is master location.
     *
     * @return true, if is master location
     */
    public static boolean isMasterLocation() {
        String locationMaster = PropertiesManager.getInstance().getProperty( ConstantsFileProperties.LOCATION_MASTER );
        if ( StringUtils.isNullOrEmpty( locationMaster ) ) {
            return false;
        }
        return Integer.valueOf( locationMaster ) == 1;
    }

    /**
     * Gets the ffmpeg config.
     *
     * @return the ffmpeg config
     */
    public static FFmpegConfig getFfmpegConfig() {
        return ffmpegConfig;
    }

    /**
     * Sets the ffmpeg config.
     *
     * @param ffmpegConfig
     *         the new ffmpeg config
     */
    public static void setFfmpegConfig( FFmpegConfig ffmpegConfig ) {
        PropertiesManager.ffmpegConfig = ffmpegConfig;
    }

    /**
     * Gets the audit trail depth.
     *
     * @return the audit trail depth
     */
    public static String getAuditTrailDepth() {
        return properties.getProperty( ConstantsFileProperties.SUS_AUDIT_TRAIL_DEPTH );
    }

    /**
     * Gets the job monitoring time.
     *
     * @return the job monitoring time
     */
    public static String getJobMonitoringTimeSeconds() {
        final String propFileName = ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.SUSCORE_DUMMY;
        JsonNode jsonJobSystemObject = null;
        try ( InputStream propFileNameStream = new FileInputStream( propFileName ) ) {
            jsonJobSystemObject = JsonUtils.convertInputStreamToJsonNode( propFileNameStream );
        } catch ( final IOException e ) {
            log.error( e.getLocalizedMessage() );
        }
        return jsonJobSystemObject.get( ConstantsFileProperties.JOB_MONITORING_TIME_SECONDS ).asText();
    }

    /**
     * Gets the audit trail level.
     *
     * @return the audit trail level
     */
    public static String getAuditTrailLevel() {
        return properties.getProperty( ConstantsFileProperties.SUS_AUDIT_TRAIL_LEVEL );
    }

    /**
     * Gets the data node name.
     *
     * @return the data node name
     */
    public static String getDataNodeName() {
        return properties.getProperty( ConstantsFileProperties.SUS_DATA_NODE_NAME );
    }

    /**
     * Gets the convertion units.
     *
     * @return the convertion units
     */
    public static List< UnitsFamily > getConvertionUnits() {
        return convertionUnits;
    }

    /**
     * Gets the sudo command.
     *
     * @return the sudo command
     */
    public static String[] getSudoCommandConfigurations() {
        return PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUDO_COMMAND_CONFIGS ).split( ConstantsString.SPACE );
    }

    /**
     * Gets the parser key set.
     *
     * @return the parser key set
     */
    public static Set< String > getParserKeySet() {
        return parser != null ? parser.keySet() : new HashSet<>();
    }

    /**
     * Gets the parser paths by key.
     *
     * @param key
     *         the key
     *
     * @return the parser paths by key
     */
    public static Map< String, String > getParserPathsByKey( String key ) {
        if ( parser != null ) {
            return ( Map< String, String > ) JsonUtils.jsonToObject( JsonUtils.toJson( parser.get( key ) ), Map.class );
        } else {
            return new HashMap<>();
        }
    }

    /**
     * Gets the dynamic path by key.
     *
     * @param key
     *         the key
     *
     * @return the dynamic path by key
     */
    public static String getDynamicPathByKey( String key ) {
        return null != dynamicProp ? dynamicProp.get( key ) : ConstantsString.EMPTY_STRING;
    }

    /**
     * Gets qa dyna json.
     *
     * @return the qa dyna json
     */
    public static String getQADynaJson() {
        return PropertiesManager.getKarafPath() + File.separator + "conf" + File.separator + "run_QADyna.json";
    }

    /**
     * Gets the image movie format by key.
     *
     * @param key
     *         the key
     *
     * @return the image movie format by key
     */
    public static List< String > getImageMovieFormatByKey( String key ) {
        return null != imgMovFormatProp ? imgMovFormatProp.get( key ) : null;
    }

    /**
     * Gets the dashboard plugins.
     *
     * @param key
     *         the key
     *
     * @return the dashboard plugins
     */
    @Deprecated( since = "soco/2.3.1/release" )
    public static List< DashboardPluginDTO > getDashboardPlugins( String key ) {
        if ( null != dashboardPlugins && !( dashboardPlugins.isEmpty() ) ) {
            return dashboardPlugins.get( key ).stream()
                    .map( op -> JsonUtils.jsonToObject( JsonUtils.toJson( op ), DashboardPluginDTO.class ) ).toList();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Gets homepage widgets.
     *
     * @param key
     *         the key
     *
     * @return the homepage widgets
     */
    public static WidgetCategoryGroupDTO getHomepageWidgets( String key ) {
        if ( null != homepageWidgets && !( homepageWidgets.isEmpty() ) ) {
            var configLists = new WidgetCategoryGroupDTO();
            var listsFromJson = homepageWidgets.get( key );
            configLists.setBuiltIn( JsonUtils.jsonToList( JsonUtils.toJson( listsFromJson.get( BUILT_IN ) ), WidgetDTO.class ) );
            configLists.setPreview( JsonUtils.jsonToList( JsonUtils.toJson( listsFromJson.get( PREVIEW ) ), WidgetDTO.class ) );
            return configLists;
        }
        throw new SusException( "widget not found!" );
    }

    /**
     * Gets the dashboard plugins.
     *
     * @param key
     *         the key
     *
     * @return the dashboard plugins
     */
    public static List< OverviewPluginDTO > getOverviewPlugins( String key ) {
        if ( null != overviewPlugins && !( overviewPlugins.isEmpty() ) ) {
            return overviewPlugins.get( key ).stream()
                    .map( op -> JsonUtils.jsonToObject( JsonUtils.toJson( op ), OverviewPluginDTO.class ) ).toList();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Gets the requiredlanguages.
     *
     * @return the requiredlanguages
     */
    public static Map< String, String > getRequiredlanguages() {
        Map< String, String > map = new HashMap<>();
        return ( null != languageProps && !( languageProps.isEmpty() ) ) ? ( Map< String, String > ) JsonUtils.jsonToMap(
                JsonUtils.toJson( languageProps.get( ConstantsFileProperties.LANGUAGES ) ), map ) : map;

    }

    /**
     * Checks for translation.
     *
     * @return true, if successful
     */
    public static boolean hasTranslation() {
        return properties != null && "ON".equalsIgnoreCase( properties.getProperty( ConstantsFileProperties.TRANSLATION ) );
    }

    /**
     * Gets the parser paths by key and identifier key.
     *
     * @param key
     *         the key
     * @param identifierKey
     *         the identifier key
     *
     * @return the parser paths by key and identifier key
     */
    public static String getParserPathsByKeyAndIdentifierKey( String key, String identifierKey ) {
        if ( parser != null && parser.get( key ) != null ) {
            Map< String, String > parserpaths = ( Map< String, String > ) JsonUtils.jsonToObject( JsonUtils.toJson( parser.get( key ) ),
                    Map.class );
            return parserpaths.get( identifierKey ).replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, getScriptsPath() );
        } else {
            return "";
        }
    }

    /**
     * Gets the parser key by path.
     *
     * @param path
     *         the path
     *
     * @return the parser key by path
     */
    public static String getParserKeyByPath( String path ) {
        if ( parser != null ) {
            for ( Entry< String, Object > parserFamily : parser.entrySet() ) {
                Map< String, String > parserpaths = ( Map< String, String > ) JsonUtils.jsonToObject(
                        JsonUtils.toJson( parserFamily.getValue() ), Map.class );
                for ( Entry< String, String > paths : parserpaths.entrySet() ) {

                    if ( paths.getValue().replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, getScriptsPath() )
                            .equalsIgnoreCase( path ) ) {
                        return parserFamily.getKey();
                    }
                }
            }
        }
        return "";
    }

    /**
     * Login to CB 2.
     *
     * @return true, if successful
     */
    public static boolean loginToCB2() {
        return getBooleanPropertyValueFromConfiguration( propertiesCB2.getProperty( ConstantsFileProperties.CB2_LOGIN ) );
    }

    /**
     * Cb 2 ip.
     *
     * @return the string
     */
    public static String cb2Ip() {
        return propertiesCB2.getProperty( ConstantsFileProperties.CB2_IP );
    }

    /**
     * Cb 2 port int.
     *
     * @return the int
     */
    public static int cb2Port() {
        return Integer.parseInt( propertiesCB2.getProperty( ConstantsFileProperties.CB2_PORT ) );
    }

    /**
     * Cb 2 request process timeout int.
     *
     * @return the int
     */
    public static int cb2RequestProcessTimeout() {
        return Integer.parseInt( propertiesCB2.getProperty( ConstantsFileProperties.CB2_PROCESS_REQUEST_TIMEOUT ) );
    }

    /**
     * web eam refresh token endpoint string.
     *
     * @return the string
     */
    public static String refreshTokenEndpoint() {
        return webEamRoot() + "/" + webEamRealm() + propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_REFRESH_TOKEN );
    }

    /**
     * redirect url string.
     *
     * @return the string
     */
    public static String redirectUrlCB2() {
        return getWebBaseURL() + "cb2login" + propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_REFRESH_TOKEN );
    }

    /**
     * Cb 2 run variant string.
     *
     * @return the string
     */
    public static String cb2RunVariant() {
        return propertiesCB2.getProperty( "cb2.run.variant.path" );
    }

    /**
     * Qa dyna base input project path string.
     *
     * @return the string
     */
    public static String qaDynaBaseInputProjectPath() {
        return propertiesCB2.getProperty( ConstantsFileProperties.QADYNA_BASE_INPUT_PROJECT_PATH );
    }

    /**
     * Qa dyna base input base status string.
     *
     * @return the string
     */
    public static String qaDynaBaseInputBaseStatus() {
        return propertiesCB2.getProperty( ConstantsFileProperties.QADYNA_BASE_INPUT_BASE_STATUS );
    }

    /**
     * Web eam client id string.
     *
     * @return the string
     */
    public static String webEamClientId() {
        return propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_CLIENT_ID );
    }

    /**
     * Web eam client secrete string.
     *
     * @return the string
     */
    public static String webEamClientSecrete() {
        return propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_CLIENT_SECRETE );
    }

    /**
     * Web eam scope string.
     *
     * @return the string
     */
    public static String webEamScope() {
        return propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_SCOPE );
    }

    /**
     * Web eam redirect uri string.
     *
     * @return the string
     */
    public static String webEamRedirectUri() {
        return propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_REDIRECT_URI );
    }

    /**
     * Web eam well known url string.
     *
     * @return the string
     */
    public static String webEamWellKnownURL() {
        return webEamRoot() + "/" + webEamRealm() + propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_WELL_KNOWN_URL );
    }

    /**
     * Web eam token info string.
     *
     * @return the string
     */
    public static String webEamTokenInfo() {
        return webEamRoot() + "/" + webEamRealm() + propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_TOKEN_INFO );
    }

    /**
     * Web eam token revoke string.
     *
     * @return the string
     */
    public static String webEamTokenRevoke() {
        return webEamRoot() + "/" + webEamRealm() + propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_TOKEN_REVOKE );
    }

    /**
     * Web eam root string.
     *
     * @return the string
     */
    public static String webEamRoot() {
        return propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_ROOT );
    }

    /**
     * Web eam realm string.
     *
     * @return the string
     */
    public static String webEamRealm() {
        return propertiesWebEam.getProperty( ConstantsFileProperties.WEB_EAM_REALM );
    }

    /**
     * Shape module cfg url string.
     *
     * @return the string
     */
    public static String SHAPE_MODULE_CFG_URL() {
        return propertiesShapeModule.getProperty( ConstantsFileProperties.SHAPEMODULE_URL );
    }

    /**
     * Is sm enabled boolean.
     *
     * @return the boolean
     */
    public static boolean isSMEnabled() {
        return getBooleanPropertyValueFromConfiguration( propertiesShapeModule.getProperty( ConstantsFileProperties.SHAPEMODULE_ENABLE ) );
    }

    /**
     * Gets sm schema path.
     *
     * @return the sm schema path
     */
    public static String getSMSchemaPath() {
        return propertiesShapeModule.getProperty( ConstantsFileProperties.SHAPEMODULE_PATH );
    }

    /**
     * Gets the python execution path on server.
     *
     * @return the python execution path on server
     */
    public static String getPythonExecutionPathOnServer() {
        return properties.getProperty( ConstantsFileProperties.PYTHON_PATH ).trim();
    }

    /**
     * Gets the hosts.
     *
     * @return the hosts
     */
    public static Hosts getHosts() {
        Hosts hosts = null;
        try ( InputStream stream = ConfigFilePathReader.getFileByNameFromKaraf( ConstantsFileProperties.SUSCORE_EXECUTION_HOST ) ) {
            hosts = JsonUtils.jsonStreamToObject( stream, Hosts.class );
        } catch ( IOException e ) {
            log.error( e.getMessage() );
        }
        return hosts;
    }

    /**
     * Gets blacklist file path.
     *
     * @return the blacklist file path
     */
    public static String getBlacklistFilePath() {
        return ConstantsKaraf.KARAF_CONF_PATH + ConstantsFileProperties.BLACKLIST_TXT;
    }

    /**
     * Checks if is audit directory.
     *
     * @return the boolean
     */
    public static Boolean isAuditDirectory() {
        return Boolean.valueOf( propertiesAudit.getProperty( ConstantsFileProperties.AUDIT_DIRECTORY ) );
    }

    /**
     * Checks if is audit role.
     *
     * @return the boolean
     */
    public static Boolean isAuditRole() {
        return Boolean.valueOf( propertiesAudit.getProperty( ConstantsFileProperties.AUDIT_ROLE ) );
    }

    /**
     * Checks if is audit group.
     *
     * @return the boolean
     */
    public static Boolean isAuditGroup() {
        return Boolean.valueOf( propertiesAudit.getProperty( ConstantsFileProperties.AUDIT_GROUP ) );
    }

    /**
     * Checks if is audit user.
     *
     * @return the boolean
     */
    public static Boolean isAuditUser() {
        return Boolean.valueOf( propertiesAudit.getProperty( ConstantsFileProperties.AUDIT_USER ) );
    }

    /**
     * Checks if is audit system view.
     *
     * @return the boolean
     */
    public static Boolean isAuditSystemView() {
        return Boolean.valueOf( propertiesAudit.getProperty( ConstantsFileProperties.AUDIT_SYSTEM_VIEW ) );
    }

    /**
     * Checks if is audit permission.
     *
     * @return the boolean
     */
    public static Boolean isAuditPermission() {
        return Boolean.valueOf( propertiesAudit.getProperty( ConstantsFileProperties.AUDIT_PERMISSION ) );
    }

    /**
     * Checks if is audit data.
     *
     * @return the boolean
     */
    public static Boolean isAuditData() {
        return Boolean.valueOf( propertiesAudit.getProperty( ConstantsFileProperties.AUDIT_DATA ) );
    }

    /**
     * Checks if is audit workflow.
     *
     * @return the boolean
     */
    public static Boolean isAuditWorkflow() {
        return Boolean.valueOf( propertiesAudit.getProperty( ConstantsFileProperties.AUDIT_WORKFLOW ) );
    }

    /**
     * Gets the encryption configs.
     *
     * @return the encryption configs method
     */
    public static EncryptionConfiguration getEncryptionConfigs() {
        return encryptionConfigs;
    }

    /**
     * Gets the encryption configs.
     *
     * @return the encryption configs method
     */
    public static boolean isEncrypted() {
        return encryptionConfigs.isActive();
    }

    /**
     * Gets the encryption decryption DTO.
     *
     * @return the encryption decryption DTO
     */
    public static EncryptionDecryptionDTO getEncryptionDecryptionDTO() {
        if ( encryptionConfigs.isActive() ) {
            for ( EncryptionDecryptionDTO encDec : encryptionConfigs.getAvailableMethods() ) {
                if ( encDec.isActive() ) {
                    return encDec;
                }
            }
        }
        return null;
    }

    /**
     * Gets the custom view.
     *
     * @return the custom view
     */
    public List< ObjectViewDTO > getCustomView() {
        if ( !MapUtils.isNotEmpty( customViews ) ) {
            loadCustomViews();
        }
        return customViews.get( "customViews" ).stream().map( op -> JsonUtils.jsonToObject( JsonUtils.toJson( op ), ObjectViewDTO.class ) )
                .toList();
    }

    /**
     * Gets the custom view.
     *
     * @param className
     *         the class name
     *
     * @return the custom view
     */
    public List< String > getFileExtensions( String className ) {
        if ( !MapUtils.isNotEmpty( fileExtensions ) ) {
            loadFileExtensions();
        }
        return fileExtensions.get( className );
    }

    /**
     * Gets dashboard properties.
     *
     * @return the dashboard properties
     */
    public static Properties getDashboardProperties() {
        getInstance();
        return dashboardProperties;
    }

    /**
     * Gets file extensions.
     *
     * @return the file extensions
     */
    public static Map< String, List< String > > getFileExtensions() {
        if ( !MapUtils.isNotEmpty( fileExtensions ) ) {
            loadFileExtensions();
        }
        return fileExtensions;
    }

}