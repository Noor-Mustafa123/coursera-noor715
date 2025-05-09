package de.soco.software.simuspace.suscore.object.utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.OverviewPluginEnums;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.OverviewPluginDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.model.ProjectDTO;

/**
 * The type Overview util.
 */
@Log4j2
public class OverviewPluginUtil {

    /**
     * The constant PLUGINS.
     */
    private static final String PLUGINS = "plugins";

    /**
     * The constant OVERVIEW_TAB_KEY.
     */
    private static final String OVERVIEW_TAB_KEY = "html";

    /**
     * The constant EN_OVERVIEW_TAB_TITLE_KEY.
     */
    private static final String EN_OVERVIEW_TAB_TITLE_KEY = "4101000x4";

    /**
     * The constant DE_OVERVIEW_TAB_TITLE_KEY.
     */
    private static final String DE_OVERVIEW_TAB_TITLE_KEY = "4101001x4";

    /**
     * The constant runningOverviews.
     */
    private static final Map< UUIDLangPair, Future< ProjectDTO > > runningOverviews = new HashMap<>();

    /**
     * Gets updated sub tabs list.
     *
     * @param overviewPlugin
     *         the overview plugin
     * @param subTabsUIs
     *         the sub tabs u is
     * @param userLanguage
     *         the user language
     */
    public static void getUpdatedSubTabsList( String overviewPlugin, List< SubTabsUI > subTabsUIs, String userLanguage ) {
        if ( overviewPlugin.equals( OverviewPluginEnums.VMCL_OVERVIEW.getId() ) ) {
            var defaultOverviewTab = subTabsUIs.stream().filter( subTabsUI -> subTabsUI.getName().equals( OVERVIEW_TAB_KEY ) ).findFirst()
                    .orElse( null );
            var pluginSettings = readPluginsSettings( OverviewPluginEnums.VMCL_OVERVIEW.getId() );
            List< SubTabsUI > newTabs = createLanguageOverviewTabs( pluginSettings, defaultOverviewTab );
            updateDefaultTabsList( subTabsUIs, userLanguage, defaultOverviewTab, newTabs );
        }
    }

    /**
     * Update default tabs list.
     *
     * @param subTabsUIs
     *         the sub tabs u is
     * @param userLanguage
     *         the user language
     * @param defaultOverviewTab
     *         the default overview tab
     * @param newTabs
     *         the new tabs
     */
    private static void updateDefaultTabsList( List< SubTabsUI > subTabsUIs, String userLanguage, SubTabsUI defaultOverviewTab,
            List< SubTabsUI > newTabs ) {
        subTabsUIs.remove( defaultOverviewTab );
        for ( int i = 0; i < newTabs.size(); i++ ) {
            if ( userLanguage.equals( newTabs.get( i ).getLanguage() ) ) {
                subTabsUIs.add( ConstantsInteger.INTEGER_VALUE_ZERO, newTabs.get( i ) );
            } else {
                subTabsUIs.add( i, newTabs.get( i ) );
            }
        }
    }

    /**
     * Create language overview tabs list.
     *
     * @param pluginSettings
     *         the plugin settings
     * @param defaultOverviewTab
     *         the default overview tab
     *
     * @return the list
     */
    private static List< SubTabsUI > createLanguageOverviewTabs( OverviewPluginDTO pluginSettings, SubTabsUI defaultOverviewTab ) {
        if ( pluginSettings == null ) {
            return Collections.singletonList( defaultOverviewTab );
        }
        List< SubTabsUI > languageOverviewTabs = new ArrayList<>();
        pluginSettings.getLanguages().forEach( lang -> {
            var langOverviewTab = new SubTabsUI();
            langOverviewTab.setName( defaultOverviewTab.getName() );
            langOverviewTab.setLanguage( lang );
            if ( lang.equals( "de" ) ) {
                langOverviewTab.setTitle( MessageBundleFactory.getMessage( DE_OVERVIEW_TAB_TITLE_KEY ) );
            } else if ( lang.equals( "en" ) ) {
                langOverviewTab.setTitle( MessageBundleFactory.getMessage( EN_OVERVIEW_TAB_TITLE_KEY ) );
            }
            languageOverviewTabs.add( langOverviewTab );
        } );
        return languageOverviewTabs;
    }

    /**
     * The type Uuid lang pair.
     */
    private record UUIDLangPair( UUID objId, String language ) {

    }

    /**
     * Instantiates a new Overview util.
     */
    private OverviewPluginUtil() {

    }

    /**
     * Add overview to running list.
     *
     * @param objectId
     *         the object id
     * @param overViewGenerationThread
     *         the over view generation thread
     * @param finalLanguage
     *         the final language
     */
    public static void addOverviewToRunningList( UUID objectId, Future< ProjectDTO > overViewGenerationThread, String finalLanguage ) {
        runningOverviews.put( new UUIDLangPair( objectId, finalLanguage ), overViewGenerationThread );
    }

    /**
     * Check if generation is in progress boolean.
     *
     * @param objectId
     *         the object id
     * @param language
     *         the language
     *
     * @return the boolean
     */
    public static boolean checkIfGenerationIsInProgress( UUID objectId, String language ) {
        var mapKey = new UUIDLangPair( objectId, language );
        return runningOverviews.containsKey( mapKey ) && !runningOverviews.get( mapKey ).isDone();
    }

    /**
     * Read overview json from file.
     *
     * @param htmlJsonPath
     *         the html json path
     * @param documentDTO
     *         the document DTO
     *
     * @return the project DTO
     */
    public static ProjectDTO readOverviewJsonFromFile( File htmlJsonPath, DocumentDTO documentDTO ) {
        if ( htmlJsonPath.exists() ) {
            try ( InputStream decryptedStream = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( htmlJsonPath,
                    documentDTO.getEncryptionDecryption() ) ) {
                byte[] byteArray = IOUtils.toByteArray( decryptedStream );
                try ( InputStream inputStream = new ByteArrayInputStream( byteArray ) ) {
                    return JsonUtils.jsonToObject( inputStream, ProjectDTO.class );
                } catch ( Exception e ) {
                    log.error( "Unable to read Html file", e );
                    String html = new String( byteArray, StandardCharsets.UTF_8 );
                    ProjectDTO projectDTO = new ProjectDTO();
                    projectDTO.setHtml( html );
                    return projectDTO;
                }
            } catch ( IOException e ) {
                log.error( "Unable to read Html file", e );
            }
        }
        return new ProjectDTO();
    }

    /**
     * Gets running thread.
     *
     * @param objectId
     *         the object id
     * @param language
     *         the language
     *
     * @return the running thread
     */
    public static Future< ProjectDTO > getRunningThread( UUID objectId, String language ) {
        return runningOverviews.get( new UUIDLangPair( objectId, language ) );
    }

    /**
     * Recreate input directory.
     *
     * @param basePath
     *         the base path
     * @param inputFilePath
     *         the input file path
     */
    public static void recreateInputDirectory( Path basePath, Path inputFilePath ) {
        try {
            if ( Files.notExists( basePath.getParent() ) ) {
                Files.createDirectory( basePath.getParent() );
            }
            FileUtils.deleteFiles( basePath );
            Files.createDirectory( basePath );
            Files.createFile( inputFilePath );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_CREATED.getKey(), inputFilePath.toAbsolutePath() ) );
        }
    }

    public static void writeOverviewPayloadToInputFile( Path inputFilePath, String jsonPayload ) {
        try {
            FileUtils.writeToFile( inputFilePath.toAbsolutePath().toString(), jsonPayload );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey(), inputFilePath.toAbsolutePath() ) );
        }
    }

    /**
     * Generate project overview from python.
     *
     * @param inputFilePath
     *         the input file path
     */
    public static void generateProjectOverviewFromPython( String inputFilePath ) {
        String scriptPath = PropertiesManager.getOverviewScriptPath();
        File scriptFile = new File( scriptPath );
        if ( !scriptFile.exists() ) {
            throw new SusException( "Please provide script file : " + scriptPath );
        }
        PythonUtils.callOverviewScript( scriptPath, inputFilePath );
    }

    /**
     * Submit overview generation future future.
     *
     * @param objectId
     *         the object id
     * @param finalLanguage
     *         the final language
     * @param callableTask
     *         the callable task
     *
     * @return the future
     */
    public static Future< ProjectDTO > submitOverviewGenerationFuture( UUID objectId, String finalLanguage,
            Callable< ProjectDTO > callableTask ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future< ProjectDTO > future = executor.submit( callableTask );
        OverviewPluginUtil.addOverviewToRunningList( objectId, future, finalLanguage );
        return future;
    }

    /**
     * Parse language query param string.
     *
     * @param token
     *         the token
     * @param language
     *         the language
     * @param overviewPluginId
     *         the overview plugin id
     *
     * @return the string
     */
    public static String parseLanguageQueryParam( String token, String language, String overviewPluginId ) {
        var pluginConf = readPluginsSettings( overviewPluginId );
        String userLang = TokenizedLicenseUtil.getNotNullUser( token ).getUserDetails().get( ConstantsInteger.INTEGER_VALUE_ZERO )
                .getLanguage();
        if ( pluginConf != null && pluginConf.getLanguages().contains( language ) ) {
            return language;
        }
        return userLang;

    }

    /**
     * Read plugins settings overview plugin dto.
     *
     * @param pluginId
     *         the plugin id
     *
     * @return the overview plugin dto
     */
    public static OverviewPluginDTO readPluginsSettings( String pluginId ) {
        List< OverviewPluginDTO > plugins = PropertiesManager.getOverviewPlugins( PLUGINS );
        return plugins.stream().filter( plugin -> pluginId.equals( plugin.getId() ) ).findFirst().orElse( null );
    }

    /**
     * Gets the relevant translation entity.
     *
     * @param projectEntity
     *         the project entity
     * @param language
     *         the language
     *
     * @return the relevant translation entity
     */
    public static TranslationEntity getRelevantTranslationEntityForOverview( ProjectEntity projectEntity, String language ) {
        TranslationEntity translationEntity;
        if ( CollectionUtils.isNotEmpty( projectEntity.getTranslation() ) ) {
            translationEntity = projectEntity.getTranslation().stream().filter(
                            translation -> ( language == null ? ConstantsString.DEFAULT_LANGUAGE : language ).equals( translation.getLanguage() ) )
                    .findFirst().orElse( new TranslationEntity( projectEntity.getName(),
                            language == null ? ConstantsString.DEFAULT_LANGUAGE : language, null ) );
        } else {
            translationEntity = new TranslationEntity( projectEntity.getName(),
                    language == null ? ConstantsString.DEFAULT_LANGUAGE : language, null );
        }
        return translationEntity;
    }

}
