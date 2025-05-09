package de.soco.software.simuspace.susdash.licmon.manager;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.susdash.core.model.LicenseDashboardDTO;

public interface LicenseMonitorManager {

    /**
     * Gets the fem settings.
     *
     * @return the fem settings
     */
    List< Map< String, String > > getFemSettings();

    /**
     * Gets the msc settings.
     *
     * @return the msc settings
     */
    List< Map< String, String > > getMscSettings();

    /**
     * Generate License Logs for FEM config
     *
     * @param licenseDashboardDTO
     *         the LicenseDashboardDTO
     * @param solver
     *         the Solver name
     *
     * @return dynamic log properties
     */
    Object generateLicenseLog( LicenseDashboardDTO licenseDashboardDTO, String solver );

    /**
     * Generate License Logs for VMCL config
     *
     * @param licenseDashboardDTO
     *         the LicenseDashboardDTO
     * @param solver
     *         the Solver name
     *
     * @return dynamic log properties
     */
    Object generateVmclLog( LicenseDashboardDTO licenseDashboardDTO, String solver );

}
