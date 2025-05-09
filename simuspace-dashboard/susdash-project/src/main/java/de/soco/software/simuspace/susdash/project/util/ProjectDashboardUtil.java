package de.soco.software.simuspace.susdash.project.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileExtension;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.DashboardConfigEnums;
import de.soco.software.simuspace.suscore.common.enums.DashboardPluginEnums;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DashboardPluginConfigDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.DashboardPluginUtil;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.data.common.model.StatusConfigDTO;
import de.soco.software.simuspace.suscore.data.entity.DataObjectDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantEntity;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.susdash.project.constants.ProjectDashboardConstants;
import de.soco.software.simuspace.susdash.project.model.ProjectClassificationCache;
import de.soco.software.simuspace.susdash.project.model.ProjectStructureTableCell;

/**
 * The type Project dashboard util.
 */
@Log4j2
public class ProjectDashboardUtil {

    /**
     * The constant runningCacheGenerations.
     */
    private static final Map< UUID, Future< Void > > runningCacheGenerations = new HashMap<>();

    /**
     * Read content from file map.
     *
     * @param outputFile
     *         the output file
     *
     * @return the map
     *
     * @throws IOException
     *         the io exception
     */
    public static Map< String, Object > readContentFromFile( Path outputFile ) throws IOException {
        Map< String, Object > fileContent;
        if ( Files.exists( outputFile ) ) {
            try ( InputStream is = Files.newInputStream( outputFile ) ) {
                fileContent = JsonUtils.jsonToObject( is, Map.class );
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_CHART_OUTPUT_FILE_NOT_FOUND.getKey() ) );
        }
        return fileContent;
    }

    /**
     * Write table data to temp file path.
     *
     * @param dashboardEntity
     *         the dashboard entity
     * @param table
     *         the table
     *
     * @return the path
     *
     * @throws IOException
     *         the io exception
     */
    public static Path writeTableDataToTempFile( DataObjectDashboardEntity dashboardEntity,
            List< Map< String, ProjectStructureTableCell > > table ) throws IOException {
        String projectName = dashboardEntity.getName().replaceAll( "\\s", "\u200B" );
        Path inputFile = Path
                .of( PropertiesManager.getDefaultServerTempPath() + File.separator + projectName + ConstantsFileExtension.JSON );
        Files.deleteIfExists( inputFile );
        Files.createFile( inputFile );
        FileUtils.writeToFile( inputFile.toAbsolutePath().toString(), JsonUtils.convertListToJson( table ) );
        FileUtils.setGlobalAllFilePermissions( inputFile );
        return inputFile;
    }

    /**
     * Gets table column by index.
     *
     * @param index
     *         the index
     *
     * @return the table column by index
     */
    public static TableColumn getTableColumnByIndex( int index ) {
        TableColumn column = new TableColumn();
        column.setData( ProjectDashboardConstants.LEVEL + index + ".name" );
        column.setName( ProjectDashboardConstants.LEVEL + index );
        column.setTitle( "Level " + index );
        column.setOrderNum( index );
        column.setFilter( "text" );
        Renderer renderer = new Renderer();
        renderer.setUrl( "{level" + index + ".url}" );
        renderer.setType( "link" );
        column.setRenderer( renderer );
        column.setSortable( true );
        return column;
    }

    /**
     * Validate and retrieve cache project classification cache.
     *
     * @param objectId
     *         the object id
     * @param userLang
     *         the user lang
     *
     * @return the project classification cache
     *
     * @throws IOException
     *         the io exception
     */
    public static ProjectClassificationCache validateAndRetrieveCache( String objectId, String userLang ) throws IOException {
        UUID objectUUID = UUID.fromString( objectId );
        String langKey = userLang == null ? ConstantsString.DEFAULT_LANGUAGE : userLang;
        Path folder = getCacheFolderPath( objectId, langKey );
        Path cacheFile = Path.of( folder.toAbsolutePath() + File.separator + ProjectDashboardConstants.CACHE_FILE_NAME );
        // Path lockFile = Path.of( folder.toAbsolutePath() + File.separator + ProjectDashboardConstants.LOCK_FILE_NAME );
        if ( checkIfGenerationIsInProgress( objectUUID ) ) {
            waitForRunningThread( objectUUID );
        }
        if ( Files.notExists( folder ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_NOT_EXIST.getKey(), folder ) );
        }
        if ( Files.notExists( cacheFile ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), cacheFile ) );
        }
        return readCacheContent( cacheFile );

    }

    /**
     * Read cache content project classification cache.
     *
     * @param cacheFile
     *         the cache file
     *
     * @return the project classification cache
     *
     * @throws IOException
     *         the io exception
     */
    private static ProjectClassificationCache readCacheContent( Path cacheFile ) throws IOException {
        String content;
        try {
            content = Files.readString( cacheFile, StandardCharsets.UTF_8 );
        } catch ( java.nio.charset.MalformedInputException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_CHARACTERS_IN_CACHE_FILE.getKey() ), e );
        }
        if ( content.isBlank() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_PROJECT_SELECTION_NO_DATA_GENERATED.getKey() ) );
        }
        return JsonUtils.jsonToObject( content, ProjectClassificationCache.class );
    }

    /**
     * Wait for running thread.
     *
     * @param objectUUID
     *         the object uuid
     */
    public static void waitForRunningThread( UUID objectUUID ) {
        try {
            getRunningThread( objectUUID ).get();
        } catch ( InterruptedException e ) {
            log.error( e.getMessage(), e );
            Thread.currentThread().interrupt();
        } catch ( ExecutionException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_WHILE_EXECUTING_CACHE_GENERATION.getKey() ), e );
        }
    }

    /**
     * Gets cache folder path.
     *
     * @param objectId
     *         the object id
     * @param langKey
     *         the lang key
     *
     * @return the cache folder path
     */
    public static Path getCacheFolderPath( String objectId, String langKey ) {
        return Path.of(
                ProjectDashboardConstants.DASHBOARD_CACHE_BASE_PATH + File.separator + objectId + ConstantsString.UNDERSCORE + langKey );
    }

    /**
     * Delete cache file.
     *
     * @param folder
     *         the folder
     */
    public static void deleteCacheFile( Path folder ) {
        Path cachePath = Path.of( folder + File.separator + ProjectDashboardConstants.CACHE_FILE_NAME );
        try {
            Files.deleteIfExists( cachePath );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Gets max depth from conf.
     *
     * @return the max depth from conf
     */
    public static String getMaxDepthFromConf() {
        DashboardPluginConfigDTO configDTO = DashboardPluginUtil.getPluginConfigByPluginAndConfig( DashboardPluginEnums.PROJECT.getId(),
                DashboardConfigEnums.PROJECT_CLASSIFICATION.getConfig() );
        // assigned to default value
        String maxDepth = "";
        if ( configDTO != null ) {
            var requiredPropertyMap = configDTO.getProperties().stream()
                    .filter( property -> property.containsKey( ProjectDashboardConstants.MAX_DEPTH_KEY ) ).findFirst().orElse( null );
            maxDepth = requiredPropertyMap != null ? ( String ) requiredPropertyMap.get( ProjectDashboardConstants.MAX_DEPTH_KEY ) : "";
        }
        return maxDepth;
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
            return ProjectDashboardConstants.URL_FORMAT
                    .replace( ProjectDashboardConstants.PARAM_TYPE, ProjectDashboardConstants.TYPE_PROJECT )
                    .replace( ProjectDashboardConstants.PARAM_ID, entity.getComposedId().getId().toString() );
        } else {
            return ProjectDashboardConstants.URL_FORMAT
                    .replace( ProjectDashboardConstants.PARAM_TYPE, ProjectDashboardConstants.TYPE_OBJECT )
                    .replace( ProjectDashboardConstants.PARAM_ID, entity.getComposedId().getId().toString() );
        }
    }

    /**
     * Create cache file.
     *
     * @param folder
     *         the folder
     */
    public static void createCacheFile( Path folder ) {
        Path cachePath = Path.of( folder + File.separator + ProjectDashboardConstants.CACHE_FILE_NAME );
        try {
            if ( Files.notExists( cachePath ) ) {
                Files.createFile( cachePath );
            }
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_FILE_AT.getKey(), folder.toString() ) + e.getMessage(),
                    e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_FILE_AT.getKey(), folder.toString() ) );
        }
    }

    /**
     * Create folder in temp path.
     *
     * @param id
     *         the id
     * @param langKey
     *         the lang key
     *
     * @return the path
     */
    public static Path createFolderInTemp( UUID id, String langKey ) {
        Path path = ProjectDashboardUtil.getCacheFolderPath( id.toString(), langKey );
        try {
            if ( Files.notExists( path ) ) {
                Files.createDirectories( path );
            }
        } catch ( IOException e ) {
            log.error(
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_DIRECTORY_AT.getKey(), path.toString() ) + e.getMessage(),
                    e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_DIRECTORY_AT.getKey(), path.toString() ) );
        }
        return path;
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
            if ( langKey.equals( translationEntity.getLanguage() ) && null != translationEntity.getName()
                    && !translationEntity.getName().isEmpty() ) {
                translatedName = translationEntity.getName();
            }
        }
        return translatedName.isEmpty() ? entity.getName() : translatedName;
    }

    /**
     * Add cache future to running list.
     *
     * @param objectId
     *         the object id
     * @param overViewGenerationThread
     *         the over view generation thread
     */
    public static void addCacheFutureToRunningList( UUID objectId, Future< Void > overViewGenerationThread ) {
        runningCacheGenerations.put( objectId, overViewGenerationThread );
    }

    /**
     * Check if generation is in progress boolean.
     *
     * @param objectId
     *         the object id
     *
     * @return the boolean
     */
    public static boolean checkIfGenerationIsInProgress( UUID objectId ) {
        return runningCacheGenerations.containsKey( objectId ) && !runningCacheGenerations.get( objectId ).isDone();
    }

    /**
     * Submit cache generation future.
     *
     * @param objectId
     *         the object id
     * @param callable
     *         the callable
     */
    public static void submitCacheGenerationFuture( UUID objectId, Callable< Void > callable ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future< Void > future = executor.submit( callable );
        addCacheFutureToRunningList( objectId, future );
    }

    /**
     * Remove cache thread from running list.
     *
     * @param objectId
     *         the object id
     */
    public static void removeCacheThreadFromRunningList( UUID objectId ) {
        runningCacheGenerations.remove( objectId );
    }

    /**
     * Gets running thread.
     *
     * @param objectId
     *         the object id
     *
     * @return the running thread
     */
    public static Future< Void > getRunningThread( UUID objectId ) {
        return runningCacheGenerations.get( objectId );
    }

    /**
     * Write cache to file.
     *
     * @param folder
     *         the folder
     * @param cache
     *         the cache
     */
    public static void writeCacheToFile( Path folder, ProjectClassificationCache cache ) {
        try {
            FileUtils.writeToFile( folder + File.separator + ProjectDashboardConstants.CACHE_FILE_NAME, JsonUtils.objectToJson( cache ) );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        }
    }

    /**
     * Create context menu item from status context menu item.
     *
     * @param objectId
     *         the object id
     * @param option
     *         the option
     * @param selectionId
     *         the selection id
     *
     * @return the context menu item
     */
    public static ContextMenuItem createContextMenuItemFromStatus( String objectId, StatusConfigDTO option, String selectionId ) {
        ContextMenuItem cmi = new ContextMenuItem();
        cmi.setTitle( MessageBundleFactory.getMessage( Messages.CHANGE_STATUS_TO.getKey(), option.getName() ) );
        cmi.setUrl( String.format( ProjectDashboardConstants.UPDATE_LIFECYCLE_URL_FORMAT, objectId, selectionId, option.getId() ) );
        cmi.setVisibility( "web" );
        return cmi;
    }

    /**
     * Write lifecycle values to cache.
     *
     * @param objectId
     *         the object id
     * @param userLanguage
     *         the user language
     * @param cache
     *         the cache
     */
    public static void writeLifecycleValuesToCache( String objectId, String userLanguage, ProjectClassificationCache cache ) {
        if ( checkIfGenerationIsInProgress( UUID.fromString( objectId ) ) ) {
            waitForRunningThread( UUID.fromString( objectId ) );
        }
        writeCacheToFile( getCacheFolderPath( objectId, userLanguage ), cache );
    }

    /**
     * Gets dashboard file name.
     *
     * @param dashboard
     *         the dashboard
     *
     * @return the dashboard file name
     *
     * @throws UnsupportedEncodingException
     *         the unsupported encoding exception
     */
    public static String getDashboardFileName( DataObjectDashboardEntity dashboard ) throws UnsupportedEncodingException {
        return new String( dashboard.getName().getBytes( Charset.forName( ConstantsString.UTF8.toUpperCase() ) ),
                ConstantsString.UTF8.toUpperCase() ).replace( ConstantsString.SPACE, ConstantsString.UNDERSCORE ) + "_output";
    }

    /**
     * Create output file for chart path.
     *
     * @param dashboard
     *         the dashboard
     *
     * @return the path
     */
    public static Path createOutputFileForChart( DataObjectDashboardEntity dashboard ) {
        try {
            String dashboardFileName = getDashboardFileName( dashboard );
            PropertiesManager.getInstance();
            String absolutFilePath = PropertiesManager.getDefaultServerTempPath() + File.separator + dashboardFileName;
            Path outputFile = Path.of( absolutFilePath );
            Files.deleteIfExists( outputFile );
            Files.createFile( outputFile );
            FileUtils.setGlobalAllFilePermissions( outputFile );
            return outputFile;
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * Call chart script.
     *
     * @param columnNumber
     *         the column number
     * @param maxDepth
     *         the max depth
     * @param userUid
     *         the user uid
     * @param inputFile
     *         the input file
     * @param outputFile
     *         the output file
     * @param dashboardScriptPath
     *         the dashboard script path
     */
    public static void callChartScript( String columnNumber, String maxDepth, Path inputFile, Path outputFile,
            String dashboardScriptPath ) {
        var result = PythonUtils.callProjectDashboardScript( dashboardScriptPath, inputFile.toAbsolutePath().toString(),
                outputFile.toAbsolutePath().toString(), String.valueOf( maxDepth == null ? getMaxDepthFromConf() : maxDepth ),
                columnNumber );
        if ( result.getExitValue() != 0 ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_ICICLE_CHART_SCRIPT_RETURNED_ERROR.getKey() ) );
        }
    }

    /**
     * Gets classification dashboard property by key.
     *
     * @param key
     *         the key
     *
     * @return the classification dashboard property by key
     */
    public static Object getClassificationDashboardPropertyByKey( String key ) {
        DashboardPluginConfigDTO configDTO = DashboardPluginUtil.getPluginConfigByPluginAndConfig( DashboardPluginEnums.PROJECT.getId(),
                DashboardConfigEnums.PROJECT_CLASSIFICATION.getConfig() );
        // assigned to default value
        Object propValue = null;
        if ( configDTO != null ) {
            var requiredPropertyMap = configDTO.getProperties().stream().filter( property -> property.containsKey( key ) ).findFirst()
                    .orElse( null );
            propValue = requiredPropertyMap != null ? ( String ) requiredPropertyMap.get( key ) : "";
        }
        return propValue;
    }

    /**
     * Gets update interval from conf.
     *
     * @return the update interval from conf
     */
    public static String getUpdateIntervalFromConf() {
        String updateInterval = ( String ) getClassificationDashboardPropertyByKey( ProjectDashboardConstants.UPDATE_INTERVAL_KEY );
        return StringUtils.isNotBlank( updateInterval ) ? updateInterval : "24";
    }

    /**
     * Gets interval unit from conf.
     *
     * @return the interval unit from conf
     */
    public static String getIntervalUnitFromConf() {
        String intervalUnit = ( String ) getClassificationDashboardPropertyByKey( ProjectDashboardConstants.INTERVAL_UNIT_KEY );
        return StringUtils.isNotBlank( intervalUnit ) ? intervalUnit : "hours";
    }

}
