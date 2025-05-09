package de.soco.software.simuspace.suscore.plugin.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileExtension;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.entity.PluginEntity;
import de.soco.software.simuspace.suscore.plugin.constants.ConstantsPlugins;
import de.soco.software.simuspace.suscore.plugin.dao.PluginDAO;
import de.soco.software.simuspace.suscore.plugin.dto.PluginDTO;
import de.soco.software.simuspace.suscore.plugin.karaf.client.KarafClient;
import de.soco.software.simuspace.suscore.plugin.manager.PluginManager;

/**
 * Implementation of PluginManager Interface to Manager the plugin related Actions
 *
 * @author Nosheen.Sharif
 */
public class PluginManagerImpl implements PluginManager {

    /**
     * Plugin dao refernce
     */
    private PluginDAO pluginDao;

    /**
     * karaf client reference
     */
    private KarafClient karafClient;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * constant for Hypen Sign
     */
    private static final String HYPHEN_MINUS = "-";

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.manager.PluginManager#addPlugin( java.lang.String)
     */
    @Override
    public PluginDTO addPlugin( String zipFilePath ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Notification notif = validateZipFile( zipFilePath );
            if ( notif.hasErrors() ) {
                throw new SusException( notif.getErrors().toString() );
            }

            String pluginDataFolder = extractZipContentToFolder( zipFilePath );

            PluginDTO dto = validateAndGetPlugindata( pluginDataFolder );

            PluginEntity returnEntity = pluginDao.savePlugin( entityManager, preparePluginEntity( dto ) );

            return preparePluginDto( returnEntity );
        } finally {
            entityManager.close();
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.manager.PluginManager#enablePlugin(de.soco.software.simuspace.suscore.plugin.dto.PluginDTO)
     */
    @Override
    public PluginDTO enablePlugin( PluginDTO pluginDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            PluginEntity retEntity = getPluginEntity( entityManager, pluginDTO.getId() );

            File srcDir = new File( retEntity.getPath() );
            File destDir = new File( ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_PLUGIN );

            try {

                for ( File file : FileUtils.getDirectoryWithInFolder( srcDir ) ) {
                    org.apache.commons.io.FileUtils.copyDirectoryToDirectory( file, destDir );
                }

            } catch ( IOException e ) {
                ExceptionLogger.logException( e, this.getClass() );
                throw new SusException( e.getMessage() );
            }
            String featureFileName = getFeatureFileName( retEntity );

            File featureFile = FileUtils.findAndReturnFile( featureFileName, destDir );

            if ( featureFile == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), featureFileName ) );
            }

            // now add feature To Karaf

            karafClient.executeCommandInKarafShell( getFeatureAddRepoCommand( retEntity ) );
            PluginDTO dto = preparePluginDto( retEntity );

            // to calculate dependencies execute command
            String dependencies = karafClient.executeCommandInKarafShell( getFeatureDependencyInfo( retEntity ) );

            dto.setDependencies( dependencies );
            return dto;
        } finally {
            entityManager.close();
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.manager.PluginManager#startPlugin( de.soco.software.simuspace.suscore.plugin.dto.PluginDTO)
     */
    @Override
    public boolean startPlugin( PluginDTO pluginDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            PluginEntity retEntity = getPluginEntity( entityManager, pluginDTO.getId() );
            karafClient.executeCommandInKarafShell( getFeatureInstallRepoCommand( retEntity ) );

            retEntity.setStatus( ConstantsPlugins.ACTIVE_STATUS );
            pluginDao.updatePlugin( entityManager, retEntity );

            return Boolean.TRUE;
        } finally {
            entityManager.close();
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.manager.PluginManager#getPluginList()
     */
    @Override
    public List< PluginDTO > getPluginList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< PluginDTO > list = new ArrayList<>();

            List< PluginEntity > entities = pluginDao.getPluginList( entityManager );
            if ( CollectionUtil.isNotEmpty( entities ) ) {
                for ( PluginEntity pluginEntity : entities ) {
                    list.add( preparePluginDto( pluginEntity ) );
                }

            }
            return list;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Method to build the Karaf Feature command
     *
     * @param retEntity
     *         the ret entity
     *
     * @return feature install repo command
     */
    private String getFeatureInstallRepoCommand( PluginEntity retEntity ) {

        return ConstantsKaraf.FEATURE_INSTALL_CMD + retEntity.getPluginName();
    }

    /**
     * Get PLugin By Id
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return plugin entity
     */
    private PluginEntity getPluginEntity( EntityManager entityManager, String id ) {
        ValidationUtils.validateUUID( id );
        PluginEntity retEntity = pluginDao.getPluginById( entityManager, UUID.fromString( id ) );
        if ( retEntity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PLUGIN_NOT_FOUND_WITH_ID.getKey(), id ) );
        }
        return retEntity;

    }

    /**
     * Gets feature file name.
     *
     * @param pluginEntity
     *         the plugin entity
     *
     * @return feature file name
     */
    public String getFeatureFileName( PluginEntity pluginEntity ) {

        return pluginEntity.getArtifactId() + HYPHEN_MINUS + pluginEntity.getVersion() + ConstantsFileExtension.XML;
    }

    /**
     * Gets feature dependency info.
     *
     * @param pluginEntity
     *         the plugin entity
     *
     * @return feature dependency info
     */
    private String getFeatureDependencyInfo( PluginEntity pluginEntity ) {
        return ConstantsKaraf.FEATURE_DEPENDENCY_INFO_CMD + pluginEntity.getPluginName();
    }

    /**
     * Gets feature add repo command.
     *
     * @param pluginEntity
     *         the plugin entity
     *
     * @return the feature add repo command
     */
    public String getFeatureAddRepoCommand( PluginEntity pluginEntity ) {
        return ConstantsKaraf.FEATURE_ADD_REPO_CMD + pluginEntity.getGroupId() + File.separator + pluginEntity.getArtifactId()
                + File.separator + pluginEntity.getVersion() + File.separator + ConstantsPlugins.XML_EXT;
    }

    /**
     * Method to prepare Plugin Entity from Plugin DTO
     *
     * @param dto
     *         the dto
     *
     * @return plugin entity
     */
    private PluginEntity preparePluginEntity( PluginDTO dto ) {
        PluginEntity entity = null;
        if ( dto != null ) {
            entity = new PluginEntity();
            if ( StringUtils.isBlank( dto.getId() ) ) {
                entity.setPluginId( UUID.randomUUID() );
            } else {
                entity.setPluginId( UUID.fromString( dto.getId() ) );
            }
            entity.setAuthor( dto.getAuthor() );
            entity.setCompaitableVersion( dto.getCompaitableVersion() );
            entity.setLicense( dto.getLicense() );
            entity.setPluginName( dto.getName() );
            entity.setSource( dto.getSource() );
            if ( StringUtils.isBlank( dto.getStatus() ) ) {
                entity.setStatus( ConstantsPlugins.DISABLE_STATUS );
            } else {
                entity.setStatus( dto.getStatus() );
            }
            entity.setSummary( dto.getSummary() );
            entity.setVersion( dto.getVersion() );
            entity.setPath( dto.getPath() );
            entity.setArtifactId( dto.getArtifactId() );
            entity.setGroupId( dto.getGroupId() );

        }
        return entity;

    }

    /**
     * Method to prepare Plugin DTO from Plugin Entity
     *
     * @param entity
     *         the entity
     *
     * @return plugin dto
     */
    private PluginDTO preparePluginDto( PluginEntity entity ) {
        PluginDTO dto = null;
        if ( entity != null ) {
            dto = new PluginDTO();
            dto.setId( entity.getPluginId().toString() );
            dto.setAuthor( entity.getAuthor() );
            dto.setCompaitableVersion( entity.getCompaitableVersion() );
            dto.setLicense( entity.getLicense() );
            dto.setName( entity.getPluginName() );
            dto.setSource( entity.getSource() );
            dto.setStatus( entity.getStatus() );
            dto.setSummary( entity.getSummary() );
            dto.setVersion( entity.getVersion() );
            dto.setPath( entity.getPath() );
            dto.setArtifactId( entity.getArtifactId() );
            dto.setGroupId( entity.getGroupId() );
        }

        return dto;
    }

    /**
     * Validate the Plugin Zip file Contents and return the plugin DTO
     *
     * @param pluginDataFolder
     *         the plugin data folder
     *
     * @return plugin dto
     */
    private PluginDTO validateAndGetPlugindata( String pluginDataFolder ) {
        PluginDTO plugin = null;
        if ( StringUtils.isNoneBlank( pluginDataFolder ) ) {
            File dir = new File( pluginDataFolder );
            File jsonFile = FileUtils.findAndReturnFile( ConstantsPlugins.META_DATA_PLUGIN_FILE_NAME, dir );
            if ( jsonFile == null ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), ConstantsPlugins.META_DATA_PLUGIN_FILE_NAME ) );
            }
            try ( InputStream jsonFileStream = new FileInputStream( jsonFile ) ) {
                plugin = JsonUtils.jsonStreamToObject( jsonFileStream, PluginDTO.class );
            } catch ( JsonSerializationException | IOException e ) {
                ExceptionLogger.logException( e, this.getClass() );
            }
            if ( null != plugin ) {
                Notification notif = plugin.validate();
                if ( notif.hasErrors() ) {
                    throw new SusException( notif.getErrors().toString() );
                }
                plugin.setPath( pluginDataFolder );
            }
        }
        return plugin;
    }

    /**
     * Method to extract zip input file for plugin to Karaf folder
     *
     * @param sourcePath
     *         the source path
     *
     * @return string
     */
    private String extractZipContentToFolder( String sourcePath ) {
        // create the karaf plugin folder
        if ( StringUtils.isNotBlank( sourcePath ) ) {
            String folderName = new File( sourcePath ).getName().split( ConstantsFileExtension.ZIP )[ 0 ];

            String destinationPath = createPluginFolderInKaraf( folderName );
            if ( StringUtils.isNoneEmpty( destinationPath ) ) {
                try {

                    FileUtils.extractZipFile( sourcePath, destinationPath );

                    return destinationPath;
                } catch ( IOException e ) {
                    ExceptionLogger.logException( e, this.getClass() );

                }
            }
        }
        return null;

    }

    /**
     * This method will check if the plugin folder exist in karaf if not it will crate one to store plugin metadata
     *
     * @param folderName
     *         the folder name
     *
     * @return string
     */
    private String createPluginFolderInKaraf( String folderName ) {
        String destinationPath = ConstantsString.EMPTY_STRING;

        String plugintempFolderPath = ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_PLUGIN_temp;
        File file = new File( plugintempFolderPath );
        if ( !file.exists() ) {
            file.mkdirs();
        }
        // now crete the pluginfolder with name

        File karafFolder = new File( file + File.separator + folderName );

        // if path doesnot exist then create it
        if ( !karafFolder.exists() ) {

            if ( karafFolder.mkdir() ) {

                destinationPath = karafFolder.getPath();
            }
        } else {
            destinationPath = karafFolder.getPath();
        }

        return destinationPath;

    }

    /**
     * Validation of Input Zip for plugin
     *
     * @param filePath
     *         the file path
     *
     * @return notification
     */
    private Notification validateZipFile( String filePath ) {
        Notification notify = new Notification();
        if ( StringUtils.isNotBlank( filePath ) ) {
            File zipFilePath = new File( filePath );
            // check if file/folder exist
            if ( !zipFilePath.exists() ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), zipFilePath ) ) );
            } else {

                if ( !FileUtils.isZipFile( filePath ) ) {
                    notify.addError( new Error( MessageBundleFactory.getMessage( Messages.ZIP_FILE_NOT_SELECTED.getKey() ) ) );
                }
            }
        }
        return notify;
    }

    /**
     * Gets plugin dao.
     *
     * @return plugin dao
     */
    public PluginDAO getPluginDao() {
        return pluginDao;
    }

    /**
     * Sets plugin dao.
     *
     * @param pluginDao
     *         the plugin dao
     */
    public void setPluginDao( PluginDAO pluginDao ) {
        this.pluginDao = pluginDao;
    }

    /**
     * Gets karaf client.
     *
     * @return karaf client
     */
    public KarafClient getKarafClient() {
        return karafClient;
    }

    /**
     * Sets karaf client.
     *
     * @param karafClient
     *         the karaf client
     */
    public void setKarafClient( KarafClient karafClient ) {
        this.karafClient = karafClient;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
