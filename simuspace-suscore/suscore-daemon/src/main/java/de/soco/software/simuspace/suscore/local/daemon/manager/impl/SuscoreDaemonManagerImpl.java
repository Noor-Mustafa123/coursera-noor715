package de.soco.software.simuspace.suscore.local.daemon.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.model.UserConfigFile;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.local.daemon.manager.SuscoreDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.model.FileExtension;
import de.soco.software.simuspace.suscore.local.daemon.model.LocalProjectConfig;
import de.soco.software.simuspace.suscore.local.daemon.model.SyncJson;

/**
 * This Class is Responsible for the starting of Spring-Boot Application as a Deamon.
 *
 * @author Nosheen.Sharif
 */
@Component
public class SuscoreDaemonManagerImpl implements SuscoreDaemonManager {

    private static final Logger logger = Logger.getLogger( ConstantsString.DAEMON_LOGGER );

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalProjectConfig localSyncDirectory() throws IOException {
        final String propFileName = System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator
                + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY + File.separator + ConstantsString.CONFIG + File.separator
                + ConstantsString.PROJECT_CONF_FILE_NAME;
        final File file = new File( propFileName );
        LocalProjectConfig projectConfig = null;
        try ( final InputStream targetConfigStream = new FileInputStream( file ) ) {
            projectConfig = JsonUtils.jsonToObject( targetConfigStream, LocalProjectConfig.class );
        }
        return projectConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyncJson localSyncConfig() throws IOException {
        final String propFileName = System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator
                + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY + File.separator + ConstantsString.CONFIG + File.separator
                + ConstantsString.SYNC_CONF_FILE_NAME;
        final File file = new File( propFileName );
        SyncJson projectConfig = null;
        if ( !file.exists() ) {
            return projectConfig;
        }
        try ( final InputStream targetConfigStream = new FileInputStream( file ) ) {
            projectConfig = JsonUtils.jsonToObject( targetConfigStream, SyncJson.class );
        }
        return projectConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileExtension localExtensionFileConfig() throws IOException {
        final String propFileName = System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator
                + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY + File.separator + ConstantsString.CONFIG + File.separator
                + ConstantsString.APPS_CONF_FILE_NAME;
        final File file = new File( propFileName );
        FileExtension extensionConfig = null;
        if ( !file.exists() ) {
            return extensionConfig;
        }
        try ( final InputStream targetConfigStream = new FileInputStream( file ) ) {
            extensionConfig = JsonUtils.jsonToObject( targetConfigStream, FileExtension.class );
        }
        return extensionConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServerAPIBase() throws IOException {
        String parent = null;
        try {
            String path = new File( SuscoreDaemonManagerImpl.class.getProtectionDomain().getCodeSource().getLocation().toURI() )
                    .getParent();
            parent = path.substring( ConstantsInteger.INTEGER_VALUE_ZERO, path.lastIndexOf( File.separator ) );
        } catch ( URISyntaxException e ) {
            e.printStackTrace();
        }
        final String propFileName = parent + File.separator + ConstantsString.SERVERFILE;
        logger.debug( "Reading File : " + propFileName );

        final File file = new File( propFileName );
        UserConfigFile userConfigFile = null;

        try ( final InputStream targetConfigStream = new FileInputStream( file ) ) {
            userConfigFile = JsonUtils.jsonToObject( targetConfigStream, UserConfigFile.class );
        }
        return userConfigFile.getServer().getProtocol() + userConfigFile.getServer().getHostname() + ":"
                + userConfigFile.getServer().getPort() + "/api/";
    }

}