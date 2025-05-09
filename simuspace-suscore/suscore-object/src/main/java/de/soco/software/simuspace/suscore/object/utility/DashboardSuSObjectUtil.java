package de.soco.software.simuspace.suscore.object.utility;

import javax.persistence.EntityManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryResponse;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.Tree;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectCacheDTO;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;

/**
 * The type Dashboard su s object util.
 */
@Log4j2
public class DashboardSuSObjectUtil {

    /**
     * The constant runningCacheGenerations.
     */
    private static final Map< String, Future< Void > > runningCacheGenerations = new HashMap<>();

    /**
     * The constant URL_FORMAT.
     */
    public static final String URL_FORMAT = "view/data/{type}/{id}";

    /**
     * The constant TYPE_PROJECT.
     */
    public static final String TYPE_PROJECT = "project";

    /**
     * The constant TYPE_OBJECT.
     */
    public static final String TYPE_OBJECT = "object";

    /**
     * The constant PARAM_TYPE.
     */
    public static final String PARAM_TYPE = "{type}";

    /**
     * The constant PARAM_ID.
     */
    public static final String PARAM_ID = "{id}";

    /**
     * The constant CACHE_FILE_NAME.
     */
    private static final String CACHE_FILE_NAME = "cache.json";

    /**
     * Gets data from su s object for widget.
     *
     * @param entityManager
     *         the entity manager
     * @param selectedProject
     *         the selected project
     * @param dto
     *         the dto
     * @param susDao
     *         the sus dao
     *
     * @return the data from su s object for widget
     */
    public static DynamicQueryResponse getDataFromSuSObjectForWidget( EntityManager entityManager, SuSEntity selectedProject,
            DashboardWidgetDTO dto, SuSGenericObjectDAO< SuSEntity > susDao ) {
        return null;
    }

    /**
     * Populate columns metadata for widget filters for su s object list.
     *
     * @param entityManager
     *         the entity manager
     * @param suSEntity
     *         the su s entity
     * @param susDao
     *         the sus dao
     *
     * @return the list
     */
    public static List< TableColumn > populateColumnsMetadataForWidgetFiltersForSuSObject( EntityManager entityManager, SuSEntity suSEntity,
            SuSGenericObjectDAO< SuSEntity > susDao ) {
        return null;
    }

    /**
     * Gets columns from su s object.
     *
     * @param entityManager
     *         the entity manager
     * @param suSEntity
     *         the su s entity
     * @param susDao
     *         the sus dao
     *
     * @return the columns from su s object
     */
    public static List< SelectOptionsUI > getColumnsFromSuSObject( EntityManager entityManager, SuSEntity suSEntity,
            SuSGenericObjectDAO< SuSEntity > susDao ) {
        return null;
    }

    /**
     * Wait for running thread.
     *
     * @param key
     *         the key
     */
    public static void waitForRunningThread( String key ) {
        try {
            getRunningThread( key ).get();
        } catch ( InterruptedException e ) {
            log.error( e.getMessage(), e );
            Thread.currentThread().interrupt();
        } catch ( ExecutionException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_WHILE_EXECUTING_CACHE_GENERATION.getKey() ), e );
        }
    }

    /**
     * Add cache future to running list.
     *
     * @param key
     *         the key
     * @param overViewGenerationThread
     *         the over view generation thread
     */
    private static void addCacheFutureToRunningList( String key, Future< Void > overViewGenerationThread ) {
        runningCacheGenerations.put( key, overViewGenerationThread );
    }

    /**
     * Check if generation is in progress boolean.
     *
     * @param key
     *         the key
     *
     * @return the boolean
     */
    public static boolean checkIfGenerationIsInProgress( String key ) {
        return runningCacheGenerations.containsKey( key ) && !runningCacheGenerations.get( key ).isDone();
    }

    /**
     * Submit cache generation future.
     *
     * @param key
     *         the key
     * @param callable
     *         the callable
     */
    public static void submitCacheGenerationFuture( String key, Callable< Void > callable ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future< Void > future = executor.submit( callable );
        addCacheFutureToRunningList( key, future );
    }

    /**
     * Remove cache thread from running list.
     *
     * @param key
     *         the key
     */
    public static void removeCacheThreadFromRunningList( String key ) {
        runningCacheGenerations.remove( key );
    }

    /**
     * Gets running thread.
     *
     * @param key
     *         the key
     *
     * @return the running thread
     */
    private static Future< Void > getRunningThread( String key ) {
        return runningCacheGenerations.get( key );
    }

    /**
     * Write tree to file.
     *
     * @param projectTreeStructure
     *         the project tree structure
     * @param cacheFile
     *         the cache file
     */
    public static void writeTreeToFile( Tree< SuSObjectCacheDTO > projectTreeStructure, Path cacheFile ) {
        try {
            FileUtils.writeToFile( cacheFile.toAbsolutePath().toString(), JsonUtils.objectToJson( projectTreeStructure ) );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        }
    }

    /**
     * Create cache file on language directory path.
     *
     * @param langCacheDirectory
     *         the lang cache directory
     *
     * @return the path
     */
    public static Path createCacheFileOnLanguageDirectory( Path langCacheDirectory ) {
        Path cacheFile = Path.of( langCacheDirectory.toAbsolutePath().toString(), CACHE_FILE_NAME );
        try {
            Files.deleteIfExists( cacheFile );
            Files.createFile( cacheFile );
            return cacheFile;
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }

    }

    /**
     * Create cache directory for cache with language post fix path.
     *
     * @param dataSourceCacheDirectory
     *         the data source cache directory
     * @param projectId
     *         the project id
     * @param langKey
     *         the lang key
     *
     * @return the path
     */
    public static Path createCacheDirectoryForCacheWithLanguagePostFix( Path dataSourceCacheDirectory, String projectId, String langKey ) {
        Path langCacheDirectory = Path.of( dataSourceCacheDirectory.toAbsolutePath().toString(),
                projectId + ConstantsString.UNDERSCORE + langKey );
        try {
            if ( Files.notExists( langCacheDirectory ) ) {
                Files.createDirectories( langCacheDirectory );
            }
            return langCacheDirectory;
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * Translate name string.
     *
     * @param langKey
     *         the lang key
     * @param entity
     *         the entity
     * @param translationEntities
     *         the translation entities
     *
     * @return the string
     */
    public static String translateName( String langKey, SuSEntity entity, List< TranslationEntity > translationEntities ) {
        String translatedName = ConstantsString.EMPTY_STRING;
        for ( TranslationEntity translationEntity : translationEntities ) {
            if ( langKey.equals( translationEntity.getLanguage() ) && null != translationEntity.getName() && !translationEntity.getName()
                    .isEmpty() ) {
                translatedName = translationEntity.getName();
            }
        }
        return translatedName.isEmpty() ? entity.getName() : translatedName;
    }

    /**
     * Delete cache.
     *
     * @param dashboardId
     *         the dashboard id
     * @param dataSourceId
     *         the data source id
     */
    public static void deleteCache( VersionPrimaryKey dashboardId, String dataSourceId ) {
        var dataSourceCacheDirectory = DashboardCacheUtil.getDataSourceDirectoryInCache( dashboardId, UUID.fromString( dataSourceId ) );
        FileUtils.deleteNonEmptyDirectory( dataSourceCacheDirectory );
    }

    /**
     * Prepare url for sus entity string.
     *
     * @param entity
     *         the entity
     *
     * @return the string
     */
    public static String prepareUrlForSusEntity( SuSEntity entity ) {
        if ( entity instanceof ProjectEntity || entity instanceof VariantEntity ) {
            return URL_FORMAT.replace( PARAM_TYPE, TYPE_PROJECT ).replace( PARAM_ID, entity.getComposedId().getId().toString() );
        } else {
            return URL_FORMAT.replace( PARAM_TYPE, TYPE_OBJECT ).replace( PARAM_ID, entity.getComposedId().getId().toString() );
        }
    }

}
