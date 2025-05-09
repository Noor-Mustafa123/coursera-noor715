package de.soco.software.simuspace.suscore.common.util;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DashboardPluginConfigDTO;
import de.soco.software.simuspace.suscore.common.model.DashboardPluginDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class DashboardPluginUtil.
 */
@Log4j2
@Deprecated( since = "soco/2.3.1/release", forRemoval = true )
public class DashboardPluginUtil {

    /**
     * The Constant NAME.
     */
    private static final String NAME = "name";

    /**
     * The Constant VALUE.
     */
    private static final String VALUE = "value";

    /**
     * The Constant PLUGIN.
     */
    private static final String PLUGIN = "plugin";

    /**
     * Instantiates a new plugin util.
     */
    private DashboardPluginUtil() {
        // private to prevent instantiation
    }

    /**
     * Gets script path.
     *
     * @param pluginId
     *         the plugin name
     * @param config
     *         the config
     * @param script
     *         the script
     *
     * @return the script path
     */
    public static String getScriptPath( String pluginId, String config, String script ) {
        List< DashboardPluginDTO > plugins = PropertiesManager.getDashboardPlugins( PLUGIN );
        if ( plugins != null ) {
            DashboardPluginConfigDTO dashboardPluginConfigDTO = getPluginConfigByPluginAndConfig( pluginId, config );
            if ( dashboardPluginConfigDTO != null ) {
                for ( var command : dashboardPluginConfigDTO.getCommands() ) {
                    if ( script.equals( command.get( NAME ) ) ) {
                        return command.get( VALUE ).replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH,
                                PropertiesManager.getScriptsPath() );
                    }
                }
            }
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Gets the live interval.
     *
     * @param pluginId
     *         the plugin id
     * @param config
     *         the config
     *
     * @return the live interval
     */
    public static Integer getLiveInterval( String pluginId, String config ) {
        DashboardPluginConfigDTO dashboardPluginConfigDTO = getPluginConfigByPluginAndConfig( pluginId, config );
        if ( dashboardPluginConfigDTO != null ) {
            List< Map< String, Object > > properties = dashboardPluginConfigDTO.getProperties();
            for ( Map< String, Object > property : properties ) {
                if ( property.containsKey( "liveInterval" ) ) {
                    return ( Integer ) property.get( "liveInterval" );
                }
            }
        }
        return 60000;
    }

    /**
     * Gets plugin config by plugin and config.
     *
     * @param plugin
     *         the plugin
     * @param config
     *         the config
     *
     * @return the plugin config by plugin and config
     */
    public static DashboardPluginConfigDTO getPluginConfigByPluginAndConfig( String plugin, String config ) {
        try {
            List< DashboardPluginDTO > plugins = PropertiesManager.getDashboardPlugins( PLUGIN );
            if ( plugins != null ) {
                return plugins.stream().filter( dashboardPluginDTO -> dashboardPluginDTO.getId().equals( plugin ) ).findFirst()
                        .orElseThrow().getConfig().stream().filter( pluginConfigDTO -> pluginConfigDTO.getName().equals( config ) )
                        .findFirst().orElse( new DashboardPluginConfigDTO() );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.DASHBOARD_PLUGIN_NOT_FOUND.getKey(), plugin ) );
            }
        } catch ( NoSuchElementException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.DASHBOARD_PLUGIN_NOT_FOUND.getKey(), plugin ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.DASHBOARD_PLUGIN_NOT_FOUND.getKey(), plugin ) );
        }

    }

    /**
     * Gets dashboard plugin name from plugin id.
     *
     * @param pluginId
     *         the plugin id
     *
     * @return the dashboard plugin name from plugin id
     */
    public static String getDashboardPluginNameFromPluginId( String pluginId ) {
        List< DashboardPluginDTO > validOptions = PropertiesManager.getDashboardPlugins( PLUGIN );
        if ( CollectionUtil.isNotEmpty( validOptions ) ) {
            for ( DashboardPluginDTO dashboardPluginDTO : validOptions ) {
                if ( dashboardPluginDTO.getId().equals( pluginId ) ) {
                    return dashboardPluginDTO.getName();
                }
            }
        }
        return null;
    }

    /**
     * Gets script path from config.
     *
     * @param scriptName
     *         the script name
     * @param config
     *         the config
     *
     * @return the script path from config
     */
    public static String getScriptPathFromConfig( String scriptName, DashboardPluginConfigDTO config ) {
        if ( config != null ) {
            for ( var command : config.getCommands() ) {
                if ( scriptName.equals( command.get( NAME ) ) ) {
                    return command.get( VALUE ).replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH,
                            PropertiesManager.getScriptsPath() );
                }
            }
        }
        return null;
    }

}
