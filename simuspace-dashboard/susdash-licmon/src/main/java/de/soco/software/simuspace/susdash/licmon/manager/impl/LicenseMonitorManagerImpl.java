package de.soco.software.simuspace.susdash.licmon.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.DashboardConfigEnums;
import de.soco.software.simuspace.suscore.common.enums.DashboardPluginEnums;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DashboardPluginConfigDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.DashboardPluginUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.susdash.core.model.LicenseDashboardDTO;
import de.soco.software.simuspace.susdash.licmon.manager.LicenseMonitorManager;

/**
 * The Class LicenseMonitorManagerImpl.
 */
@Log4j2
public class LicenseMonitorManagerImpl implements LicenseMonitorManager {

    /**
     * The Constant NASTARN.
     */
    private static final String NASTARN = "nastran";

    /**
     * The Constant ANSA.
     */
    private static final String ANSA = "ansa";

    /**
     * The Constant ABAQUS.
     */
    private static final String ABAQUS = "abaqus";

    /**
     * The Constant ABAQUSMJ.
     */
    private static final String ABAQUSMJ = "abaqusmj";

    /**
     * The Folder in sus-temp for VMCL temp files
     */
    private static final String VMCL_TEMP_FOLDER = "8b0b0ab8-4adb-11ed-b878-0242ac120002/monitor_license";

    /**
     * The Folder in sus-temp for FEM temp files
     */
    private static final String FEM_TEMP_FOLDER = "f5bec2e1-c46a-4776-86dd-52801b9fbbe9/monitor_license";

    /**
     * The Constant LICENSE_NOT_GENERATED.
     */
    private static final String LICENSE_NOT_GENERATED = "License file not generated for ";

    /**
     * The Constant TOTAL_VALUE_ERROR.
     */
    private static final String TOTAL_VALUE_ERROR = "'s total value not set in dashboard_plugins config";

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Map< String, String > > getFemSettings() {
        DashboardPluginConfigDTO configDTO = DashboardPluginUtil
                .getPluginConfigByPluginAndConfig( DashboardPluginEnums.LICENCE_MONITOR.getId(), DashboardConfigEnums.LM_FEM.getConfig() );
        return getTotalsFromConfig( configDTO );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Map< String, String > > getMscSettings() {
        DashboardPluginConfigDTO configDTO = DashboardPluginUtil
                .getPluginConfigByPluginAndConfig( DashboardPluginEnums.LICENCE_MONITOR.getId(), DashboardConfigEnums.LM_MSC.getConfig() );
        return getTotalsFromConfig( configDTO );
    }

    /**
     * Gets the totals from config.
     *
     * @param configDTO
     *         the config DTO
     *
     * @return the totals from config
     */
    private List< Map< String, String > > getTotalsFromConfig( DashboardPluginConfigDTO configDTO ) {
        for ( Map< String, Object > property : configDTO.getProperties() ) {
            return ( List< Map< String, String > > ) property.get( "totals" );
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object generateLicenseLog( LicenseDashboardDTO licenseDashboardDTO, String solver ) {
        Map< String, String > dynamicProp = null;
        String totalValue = ConstantsString.EMPTY_STRING;

        String dateRangeFlag = licenseDashboardDTO.getDateRange().getStart() + " -to " + licenseDashboardDTO.getDateRange().getEnd();
        File cleanLogsFile = new File( DashboardPluginUtil.getScriptPath( DashboardPluginEnums.LICENCE_MONITOR.getId(),
                DashboardConfigEnums.LM_FEM.getConfig(), "clean_logs_v4" ) );
        String basePath = PropertiesManager.getDefaultServerTempPath() + File.separator + FEM_TEMP_FOLDER;

        try {
            Files.createDirectories( Paths.get( basePath ) );
        } catch ( IOException e1 ) {
            log.error( e1.getMessage(), e1 );
            throw new SusException( LICENSE_NOT_GENERATED + solver + e1.getLocalizedMessage() );
        }
        String genCsvPath = basePath + File.separator + "bmw_" + solver + "_log";

        if ( null != licenseDashboardDTO.getTotal() ) {
            totalValue = licenseDashboardDTO.getTotal().toString();
        } else {
            totalValue = getSolverTotalValue( DashboardPluginEnums.LICENCE_MONITOR.getId(), DashboardConfigEnums.LM_FEM.getConfig(),
                    solver );
        }

        if ( StringUtils.isNullOrEmpty( totalValue ) ) {
            throw new SusException( solver + TOTAL_VALUE_ERROR );
        }

        PythonUtils.callPythonCleanLogs( cleanLogsFile, solver, dateRangeFlag, genCsvPath, totalValue );
        File afterGenrattion = new File( genCsvPath + ".json" );
        if ( afterGenrattion.isFile() ) {
            try ( InputStream is = new FileInputStream( afterGenrattion ) ) {
                dynamicProp = ( Map< String, String > ) JsonUtils.jsonToObject( is, Map.class );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
        } else {
            throw new SusException( LICENSE_NOT_GENERATED + solver );
        }
        return dynamicProp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object generateVmclLog( LicenseDashboardDTO licenseDashboardDTO, String solver ) {
        Map< String, String > dynamicProp = null;

        String dateRangeFlag = licenseDashboardDTO.getDateRange().getStart() + " -to " + licenseDashboardDTO.getDateRange().getEnd();
        File vmclLogsFile = new File( DashboardPluginUtil.getScriptPath( DashboardPluginEnums.LICENCE_MONITOR.getId(),
                DashboardConfigEnums.LM_VMCL.getConfig(), "vmcl_dashboard_curves" ) );
        String basePath = PropertiesManager.getDefaultServerTempPath() + File.separator + VMCL_TEMP_FOLDER;

        try {
            Files.createDirectories( Paths.get( basePath ) );
        } catch ( IOException e1 ) {
            throw new SusException( LICENSE_NOT_GENERATED + solver + e1.getLocalizedMessage() );
        }
        String genCsvPath = basePath + File.separator + "vmcl_" + solver + "_log";
        PythonUtils.callPythonVmclLogs( vmclLogsFile, solver, dateRangeFlag, genCsvPath );
        File afterGenration = new File( genCsvPath + ConstantsString.JSON_EXTENSION );
        if ( afterGenration.isFile() ) {
            try ( InputStream is = new FileInputStream( afterGenration ) ) {
                dynamicProp = ( Map< String, String > ) JsonUtils.jsonToObject( is, Map.class );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
        } else {
            throw new SusException( LICENSE_NOT_GENERATED + solver );
        }
        return dynamicProp;
    }

    /**
     * Gets the solver total value.
     *
     * @param plugin
     *         the plugin
     * @param config
     *         the config
     * @param solver
     *         the solver
     *
     * @return the solver total value
     */
    private String getSolverTotalValue( String plugin, String config, String solver ) {
        DashboardPluginConfigDTO configDTO = DashboardPluginUtil.getPluginConfigByPluginAndConfig( plugin, config );
        if ( configDTO.getProperties() != null ) {
            for ( Map< String, Object > property : configDTO.getProperties() ) {
                var totals = ( List< Map< String, String > > ) property.get( "totals" );
                for ( Map< String, String > totalValueMap : totals ) {
                    if ( solver.equalsIgnoreCase( totalValueMap.get( "name" ) ) ) {
                        return totalValueMap.get( "value" );
                    }
                }
            }
        }
        return null;
    }

}