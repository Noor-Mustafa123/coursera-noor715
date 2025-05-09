package de.soco.software.simuspace.server.manager.impl;

import java.util.HashMap;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.properties.DesignPlotingConfig;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

public class PropertiesManagerImpl implements de.soco.software.simuspace.server.manager.PropertiesManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAllProperties() {
        Map< String, Object > props = new HashMap<>();
        props.put( ConstantsFileProperties.SUS_AUDIT_TRAIL_DEPTH, PropertiesManager.getAuditTrailDepth() );
        props.put( ConstantsFileProperties.SUS_AUDIT_TRAIL_LEVEL, PropertiesManager.getAuditTrailLevel() );
        props.put( ConstantsFileProperties.CONVERSION_COMMAND, PropertiesManager.getCeetronConversionCommand() );
        props.put( ConstantsFileProperties.OUTPUT_PATH, PropertiesManager.getCeetronOutputPath() );
        props.put( ConstantsFileProperties.SERVER_PATH, PropertiesManager.getCeetronServerUrl() );
        props.put( ConstantsFileProperties.SUS_DATA_NODE_NAME, PropertiesManager.getDataNodeName() );
        props.put( ConstantsFileProperties.DEFAULT_SERVER_TEMP_PATH, PropertiesManager.getDefaultServerTempPath() );
        props.put( ConstantsFileProperties.DYNAMIC_BASE_PATH,
                PropertiesManager.getDynamicPathByKey( ConstantsFileProperties.DYNAMIC_BASE_PATH ) );
        props.put( ConstantsFileProperties.ELS_URL, PropertiesManager.getElasticSearchURL() );
        props.put( ConstantsFileProperties.FAILED_LOGIN_ATTEMPTS, PropertiesManager.getFailedLoginAttempt() );
        props.put( ConstantsFileProperties.FAILED_LOGIN_ATTEMPTS_LOCK_TIME, PropertiesManager.getAccountLockTimeForFailedAttempts() );
        props.put( ConstantsFileProperties.FE_STATIC, PropertiesManager.getFeStaticPath() );
        props.put( ConstantsFileProperties.HPC_COST_CENTER_COMAND, PropertiesManager.getHpcCostCenterCommand() );
        props.put( ConstantsFileProperties.IFCONFIG_BASEPATH_KEY, PropertiesManager.getIfConfigPath() );
        props.put( ConstantsFileProperties.JAVA_PATH, PropertiesManager.getJavaRunTimePath() );
        props.put( "karaf.base", PropertiesManager.getKarafPath() );
        props.put( "karaf.script", PropertiesManager.getScriptsPath() );
        props.put( ConstantsFileProperties.LOCATION_TOKEN, PropertiesManager.getLocationAuthToken() );
        props.put( ConstantsFileProperties.LOCATION_NAME, PropertiesManager.getLocationName() );
        props.put( ConstantsFileProperties.LOCATION_URL, PropertiesManager.getLocationURL() );
        props.put( ConstantsFileProperties.PROJECT_LABLE_ICON, PropertiesManager.getProjectLabelIcon() );
        props.put( ConstantsFileProperties.USER_SESSION_EXPIRY,
                PropertiesManager.getInstance().getProperty( ConstantsFileProperties.USER_SESSION_EXPIRY ) );
        props.put( ConstantsFileProperties.SUS_WEB_BASE_URL, PropertiesManager.getWebBaseURL() );
        props.put( ConstantsFileProperties.SUS_WEB_DOCS_URL,
                PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_DOCS_URL ) );
        props.put( ConstantsFileProperties.EXECUTE_ON_REMOTE, PropertiesManager.isHostEnable() );
        props.put( ConstantsFileProperties.IMPERSONATED, PropertiesManager.isImpersonated() );
        props.put( ConstantsFileProperties.REGEX_CONTEXT_LINE, PropertiesManager.getRegexContextLine() );
        props.put( ConstantsFileProperties.VAULT_PATH, PropertiesManager.getVaultPath() );
        props.put( ConstantsFileProperties.SUDO_COMMAND_CONFIGS, PropertiesManager.getSudoCommandConfigurations() );
        props.put( ConstantsFileProperties.SCHEME_TEMPLATE_FILE_SIZE, PropertiesManager.getSchemeTemplateFileSize() );
        props.put( ConstantsFileProperties.STAGING_PATH, PropertiesManager.getStagingPath() );
        props.put( ConstantsFileProperties.FFMPEG_PATH, PropertiesManager.getFFmpegPath() );
        props.put( ConstantsFileProperties.TRANSLATION, PropertiesManager.hasTranslation() );
        props.put( ConstantsFileProperties.FILE_EXTENSIONS, PropertiesManager.getFileExtensions() );
        props.put( "matplotlibPaths", DesignPlotingConfig.getGenerateImageOptions() );
        return props;
    }

}
