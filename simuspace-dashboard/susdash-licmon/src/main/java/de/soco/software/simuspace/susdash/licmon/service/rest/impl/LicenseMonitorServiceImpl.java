package de.soco.software.simuspace.susdash.licmon.service.rest.impl;

import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.susdash.core.model.LicenseDashboardDTO;
import de.soco.software.simuspace.susdash.licmon.manager.LicenseMonitorManager;
import de.soco.software.simuspace.susdash.licmon.service.rest.LicenseMonitorService;

/**
 * The Class LicenseMonitorServiceImpl.
 */
public class LicenseMonitorServiceImpl extends SuSBaseService implements LicenseMonitorService {

    /**
     * The license monitor manager.
     */
    private LicenseMonitorManager licenseMonitorManager;

    @Override
    public Response getFemSettings( String objectId ) {
        try {
            return ResponseUtils.success( licenseMonitorManager.getFemSettings() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getMscSettings( String objectId ) {
        try {
            return ResponseUtils.success( licenseMonitorManager.getMscSettings() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response generateFEMLicenseLog( String objectId, String solver, String dashBoardLicenseJson ) {
        try {
            LicenseDashboardDTO licenseDashboardDTO = JsonUtils.jsonToObject( dashBoardLicenseJson, LicenseDashboardDTO.class );
            return ResponseUtils.success( licenseMonitorManager.generateLicenseLog( licenseDashboardDTO, solver ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response generateMSCLicenseLog( String objectId, String solver, String dashBoardLicenseJson ) {
        try {
            LicenseDashboardDTO licenseDashboardDTO = JsonUtils.jsonToObject( dashBoardLicenseJson, LicenseDashboardDTO.class );
            return ResponseUtils.success( licenseMonitorManager.generateLicenseLog( licenseDashboardDTO, solver ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response generateVMCLLog( String objectId, String solver, String dashBoardLicenseJson ) {
        try {
            LicenseDashboardDTO licenseDashboardDTO = JsonUtils.jsonToObject( dashBoardLicenseJson, LicenseDashboardDTO.class );
            return ResponseUtils.success( licenseMonitorManager.generateVmclLog( licenseDashboardDTO, solver ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Sets the license monitor manager.
     *
     * @param licenseMonitorManager
     *         the new license monitor manager
     */
    public void setLicenseMonitorManager( LicenseMonitorManager licenseMonitorManager ) {
        this.licenseMonitorManager = licenseMonitorManager;
    }

}
