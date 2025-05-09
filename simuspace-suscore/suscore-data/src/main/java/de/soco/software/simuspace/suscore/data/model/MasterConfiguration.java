package de.soco.software.simuspace.suscore.data.model;

import java.util.List;

import lombok.Getter;

import de.soco.software.simuspace.suscore.common.util.ConfigUtil;

/**
 * The class will be used to map project configuration file names.
 *
 * @author M.Nasir.Farooq
 */
@Getter
public class MasterConfiguration {

    /**
     * The project configs.
     */
    private List< ConfigUtil.Config > projectConfigs;

    /**
     * Instantiates a new configuration files.
     */
    public MasterConfiguration() {
        super();
    }

    /**
     * Instantiates a new configuration files.
     *
     * @param projectConfigs
     *         the project configs
     */
    public MasterConfiguration( List< ConfigUtil.Config > projectConfigs ) {
        super();
        this.projectConfigs = projectConfigs;
    }

}
