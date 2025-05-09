package de.soco.software.simuspace.suscore.object.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.entity.DashboardWidgetEntity;
import de.soco.software.simuspace.suscore.data.entity.DataDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;

/**
 * The type Dashboard cache util.
 */
@Log4j2
public class DashboardCacheUtil {

    /**
     * Instantiates a new Dashboard cache util.
     */
    private DashboardCacheUtil() {

    }

    /**
     * Create widget directory in cache.
     *
     * @param dashboardId
     *         the dashboard id
     * @param id
     *         the id
     */
    public static void createWidgetDirectoryInCache( VersionPrimaryKey dashboardId, UUID id ) {
        var dashboardDirectory = createDashboardDirectoryInCache( dashboardId );
        createWidgetDirectoryInDashboardDirectory( dashboardDirectory, id, dashboardId.getVersionId() );
    }

    public static void createDataSourceDirectoryInCache( String dashboardId, String dataSourceId, Integer version ) {
        var compositeKey = new VersionPrimaryKey( UUID.fromString( dashboardId ), version );
        var dashboardDirectory = createDashboardDirectoryInCache( compositeKey );
        createDataSourceDirectoryInDashboardDirectory( dashboardDirectory, UUID.fromString( dataSourceId ), version );
    }

    /**
     * Create widget directory in dashboard directory.
     *
     * @param dashboardDirectory
     *         the dashboard directory
     * @param id
     *         the id
     * @param versionId
     *         the version id
     */
    private static void createWidgetDirectoryInDashboardDirectory( Path dashboardDirectory, UUID id, int versionId ) {
        try {
            var widgetDirectory = getWidgetDirectoryInCache( dashboardDirectory, id, versionId );
            if ( Files.notExists( widgetDirectory ) ) {
                Files.createDirectories( widgetDirectory );
            }
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private static void createDataSourceDirectoryInDashboardDirectory( Path dashboardDirectory, UUID id, int versionId ) {
        try {
            var dataSourceDirectory = getDataSourceDirectoryInCache( dashboardDirectory, id, versionId );
            if ( Files.notExists( dataSourceDirectory ) ) {
                Files.createDirectories( dataSourceDirectory );
            }
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * Gets widget directory in cache.
     *
     * @param dashboardDirectory
     *         the dashboard directory
     * @param id
     *         the id
     * @param versionId
     *         the version id
     *
     * @return the widget directory in cache
     */
    public static Path getWidgetDirectoryInCache( Path dashboardDirectory, UUID id, int versionId ) {
        return getDirectoryInCache( dashboardDirectory, id, versionId );
    }

    private static Path getDirectoryInCache( Path dashboardDirectory, UUID id, int versionId ) {
        var hex = CommonUtils.getSha1Hex( id, versionId );
        return Path.of( dashboardDirectory.toAbsolutePath() + File.separator + CommonUtils.getEncodedName( hex, null ) );
    }

    public static Path getDataSourceDirectoryInCache( Path dashboardDirectory, UUID id, int versionId ) {
        return getDirectoryInCache( dashboardDirectory, id, versionId );
    }

    /**
     * Gets widget directory in cache.
     *
     * @param dashboardId
     *         the dashboard id
     * @param id
     *         the id
     *
     * @return the widget directory in cache
     */
    public static Path getWidgetDirectoryInCache( VersionPrimaryKey dashboardId, UUID id ) {
        return getDirectoryInCache( dashboardId, id );
    }

    public static Path getDirectoryInCache( VersionPrimaryKey dashboardId, UUID id ) {
        var dashboardDirectory = getDashboardDirectoryInCache( dashboardId );
        var hex = CommonUtils.getSha1Hex( id, dashboardId.getVersionId() );
        return Path.of( dashboardDirectory.toAbsolutePath() + File.separator + CommonUtils.getEncodedName( hex, null ) );
    }

    public static Path getDataSourceDirectoryInCache( VersionPrimaryKey dashboardId, UUID id ) {
        return getDirectoryInCache( dashboardId, id );
    }

    /**
     * Delete widget directory from cache.
     *
     * @param dashboardId
     *         the dashboard id
     * @param id
     *         the id
     */
    public static void deleteWidgetDirectoryFromCache( VersionPrimaryKey dashboardId, UUID id ) {
        var dashboardDirectory = getDashboardDirectoryInCache( dashboardId );
        deleteWidgetDirectoryFromDashboardDirectory( dashboardDirectory, id, dashboardId.getVersionId() );
    }

    /**
     * Delete widget directory from dashboard directory.
     *
     * @param dashboardDirectory
     *         the dashboard directory
     * @param id
     *         the id
     * @param versionId
     *         the version id
     */
    private static void deleteWidgetDirectoryFromDashboardDirectory( Path dashboardDirectory, UUID id, int versionId ) {
        var widgetDirectory = getWidgetDirectoryInCache( dashboardDirectory, id, versionId );
        if ( Files.exists( widgetDirectory ) ) {
            FileUtils.deleteNonEmptyDirectory( widgetDirectory );
        }
    }

    /**
     * Create dashboard directory in cache path.
     *
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path createDashboardDirectoryInCache( VersionPrimaryKey dashboardId ) {
        try {
            var dashboardDirectory = getDashboardDirectoryInCache( dashboardId );
            if ( Files.notExists( dashboardDirectory ) ) {
                Files.createDirectories( dashboardDirectory );
            }
            return dashboardDirectory;
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * Gets dashboard directory in cache.
     *
     * @param dashboardId
     *         the dashboard id
     *
     * @return the dashboard directory in cache
     */
    private static Path getDashboardDirectoryInCache( VersionPrimaryKey dashboardId ) {
        var hex = CommonUtils.getSha1Hex( dashboardId.getId(), dashboardId.getVersionId() );
        var parentDirectory = Path.of(
                PropertiesManager.getServerCachePath() + File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO,
                        ConstantsInteger.INTEGER_VALUE_TWO ) );
        return Path.of( parentDirectory.toAbsolutePath() + File.separator + CommonUtils.getEncodedName(
                hex.substring( ConstantsInteger.INTEGER_VALUE_TWO ), null ) );
    }

    /**
     * Copy widget cache.
     *
     * @param updatedEntity
     *         the updated entity
     * @param widget
     *         the widget
     */
    public static void copyWidgetCache( DataDashboardEntity updatedEntity, DashboardWidgetEntity widget, Integer newVersionId ) {
        Path oldWidgetDirectory = DashboardCacheUtil.getWidgetDirectoryInCache(
                new VersionPrimaryKey( updatedEntity.getId(), widget.getVersionId() ), widget.getId() );
        createWidgetDirectoryInCache( new VersionPrimaryKey( updatedEntity.getId(), newVersionId ), widget.getId() );
        Path newWidgetDirectory = DashboardCacheUtil.getWidgetDirectoryInCache(
                new VersionPrimaryKey( updatedEntity.getId(), newVersionId ), widget.getId() );
        try {
            if ( FileUtils.isDirectoryNotEmpty( oldWidgetDirectory ) ) {
                FileUtils.copyDirectory( oldWidgetDirectory, newWidgetDirectory );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_COPY_DIRECTORY_TO_FROM.getKey(), newWidgetDirectory,
                    oldWidgetDirectory ) );
        }
    }

    /**
     * Copy widget cache.
     *
     * @param dashboardEntity
     *         the dashboard entity
     * @param widget1
     *         the widget 1
     * @param widget2
     *         the widget 2
     */
    public static void copyWidgetCache( DataDashboardEntity dashboardEntity, DashboardWidgetDTO widget1, DashboardWidgetDTO widget2 ) {
        Path newWidgetDirectory = null;
        Path oldWidgetDirectory = null;
        try {
            oldWidgetDirectory = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardEntity.getComposedId(),
                    UUID.fromString( widget1.getId() ) );
            newWidgetDirectory = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardEntity.getComposedId(),
                    UUID.fromString( widget2.getId() ) );
            if ( Files.exists( newWidgetDirectory ) ) {
                FileUtils.deleteNonEmptyDirectory( newWidgetDirectory );
            }
            createWidgetDirectoryInCache( dashboardEntity.getComposedId(), UUID.fromString( widget2.getId() ) );
            if ( FileUtils.isDirectoryNotEmpty( oldWidgetDirectory ) ) {
                FileUtils.copyDirectory( oldWidgetDirectory, newWidgetDirectory );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_COPY_DIRECTORY_TO_FROM.getKey(), newWidgetDirectory,
                    oldWidgetDirectory ) );
        }
    }

}
