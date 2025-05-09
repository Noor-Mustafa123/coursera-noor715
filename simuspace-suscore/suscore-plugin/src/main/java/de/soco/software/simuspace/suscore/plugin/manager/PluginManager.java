package de.soco.software.simuspace.suscore.plugin.manager;

import java.util.List;

import de.soco.software.simuspace.suscore.plugin.dto.PluginDTO;

/**
 * Interface to Manage the Business operation related to plugin in suscore
 *
 * @author Nosheen.Sharif
 */
public interface PluginManager {

    /**
     * Add Plugin
     *
     * @param zipFilePath
     *         the zip file path
     *
     * @return plugin dto
     *
     * @apiNote To be used in service calls only
     */
    PluginDTO addPlugin( String zipFilePath );

    /**
     * Enable Plugin
     *
     * @param pluginDTO
     *         the plugin dto
     *
     * @return plugin dto
     *
     * @apiNote To be used in service calls only
     */
    PluginDTO enablePlugin( PluginDTO pluginDTO );

    /**
     * Start Plugin
     *
     * @param pluginDTO
     *         the plugin dto
     *
     * @return boolean
     *
     * @apiNote To be used in service calls only
     */
    boolean startPlugin( PluginDTO pluginDTO );

    /**
     * Get List of Plugins
     *
     * @return plugin list
     *
     * @apiNote To be used in service calls only
     */
    List< PluginDTO > getPluginList();

}
