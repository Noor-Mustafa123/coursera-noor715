package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum DashboardConfigEnums.
 *
 * @deprecated since soco/2.3.1/release
 */
@Deprecated( forRemoval = true )
public enum DashboardConfigEnums {

    /**
     * The pl features.
     */
    PL_FEATURES( DashboardPluginEnums.PLUNGERLIFT.getId(), "Features" ),

    /**
     * The pl prediction.
     */
    PL_PREDICTION( DashboardPluginEnums.PLUNGERLIFT.getId(), "Prediction" ),

    /**
     * The pl well statistics.
     */
    PL_WELL_STATISTICS( DashboardPluginEnums.PLUNGERLIFT.getId(), "Well Statistics" ),

    /**
     * The pl train well.
     */
    PL_TRAIN_WELL( DashboardPluginEnums.PLUNGERLIFT.getId(), "Train Well" ),

    /**
     * The lm fem.
     */
    LM_FEM( DashboardPluginEnums.LICENCE_MONITOR.getId(), "fem" ),

    /**
     * The lm vmcl.
     */
    LM_VMCL( DashboardPluginEnums.LICENCE_MONITOR.getId(), "vmcl" ),

    /**
     * The lm msc.
     */
    LM_MSC( DashboardPluginEnums.LICENCE_MONITOR.getId(), "msc" ),

    /**
     * The hpc uge.
     */
    HPC_UGE( DashboardPluginEnums.HPC.getId(), "Job Monitoring" ),

    /**
     * The hpc femzip.
     */
    HPC_FEMZIP( DashboardPluginEnums.HPC.getId(), "FEMZIP Statistics" ),

    /**
     * The Hpc lsdyna statistics.
     */
    HPC_LSDYNA_STATISTICS( DashboardPluginEnums.HPC.getId(), "LSDYNA Statistics" ),

    /**
     * The Hpc abaqus statistics.
     */
    HPC_ABAQUS_STATISTICS( DashboardPluginEnums.HPC.getId(), "ABAQUS Standard Statistics" ),

    /**
     * The project classification.
     */
    PROJECT_CLASSIFICATION( DashboardPluginEnums.PROJECT.getId(), "Classification" ),

    /**
     * Pst planning dashboard config enums.
     */
    PST_PLANNING( DashboardPluginEnums.PST.getId(), "Planning" );

    /**
     * Instantiates a new dashboard config enums.
     *
     * @param pluginId
     *         the plugin name
     * @param config
     *         the config
     */
    DashboardConfigEnums( String pluginId, String config ) {
        this.pluginId = pluginId;
        this.config = config;
    }

    /**
     * The plugin name.
     */
    private final String pluginId;

    /**
     * The config.
     */
    private final String config;

    /**
     * Gets the plugin name.
     *
     * @return the plugin name
     */
    public String getPluginId() {
        return pluginId;
    }

    /**
     * Gets the config.
     *
     * @return the config
     */
    public String getConfig() {
        return config;
    }
}
