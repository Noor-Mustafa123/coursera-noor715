package de.soco.software.simuspace.suscore.object.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants;
import de.soco.software.simuspace.suscore.common.enums.DashboardEnums;
import de.soco.software.simuspace.suscore.common.enums.ExitCodesAndSignals;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.BindTo;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;
import de.soco.software.simuspace.suscore.data.model.DataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.SuSObjectDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.TreemapWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.WidgetVmclSource;

@Log4j2
public class DashboardVMclUtil {

    /**
     * The constant INPUT_FILE_NAME.
     */
    private static final String INPUT_FILE_NAME = "python_input.json";

    public static final String JSON_OUTPUT_FILE = "python_output.json";

    public static final String CACHE_FILE_NAME = "cache.json";

    public static void populateDataSourceList( SelectFormItem item, List< DataSourceDTO > sources ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var source : sources ) {
            if ( DashboardEnums.DashboardDataSourceOptions.SUS_OBJECT.getId().equals( source.getSourceType() ) ) {
                options.add( new SelectOptionsUI( source.getId(),
                        source.getSourceName() != null ? source.getSourceName() : "Unnamed data source" ) );
            }
        }
        item.setOptions( options );
    }

    public static HashMap getResultFromOutputFile( DashboardWidgetDTO widget, String objectId ) {
        String idInPath = widget.getGroupId() != null ? widget.getGroupId() : widget.getId();
        return DashboardVMclUtil.getDataFromJsonFileForWidget( prepareOutputFilePath( UUID.fromString( idInPath ),
                new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ) ) );
    }

    private static Path prepareOutputFilePath( UUID widgetId, VersionPrimaryKey dashboardId ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + JSON_OUTPUT_FILE );
    }

    public static void setBindFromForSelection( SelectFormItem selectFormItem, String objectId, String widgetId ) {
        selectFormItem.setBindTo( new BindTo(
                DataDashboardConstants.BIND_TO_URLS.WIDGET_ACTION.replace( DataDashboardConstants.URL_PARAMS.PARAM_OBJECT_ID, objectId )
                        .replace( DataDashboardConstants.URL_PARAMS.PARAM_WIDGET_ID, widgetId )
                        .replace( DataDashboardConstants.URL_PARAMS.PARAM_ACTION, selectFormItem.getName() ),
                new HashMap< String, String >(), "" ) );
    }

    public static void callPython( String token, DashboardWidgetDTO widget, SuSObjectDataSourceDTO selectedDataSource, String objectId ) {
        String payload = preparePayloadForPythonScript( token, widget, selectedDataSource, objectId );
        VersionPrimaryKey dashboardId = new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() );
        String idInPath = widget.getGroupId() != null ? widget.getGroupId() : widget.getId();
        var inputFile = preparePythonInputFile( payload, UUID.fromString( idInPath ), dashboardId );
        var outputFile = preparePythonOutputFile( UUID.fromString( idInPath ), dashboardId );
        Supplier< String > scriptFile = () -> {
            if ( widget.getType().equals( DashboardEnums.WIDGET_TYPE.TREEMAP.getId() ) ) {
                return DashboardConfigUtil.getTreemapVmclScriptPath();
            } else if ( widget.getType().equals( DashboardEnums.WIDGET_TYPE.TABLE.getId() ) ) {
                return DashboardConfigUtil.getTableVmclScriptPath();
            } else if ( widget.getType().equals( DashboardEnums.WIDGET_TYPE.PROJECT_LIFE_CYCLE.getId() ) ) {
                return DashboardConfigUtil.getProjectLifeCycleVmclScriptPath();
            }
            return ConstantsString.EMPTY_STRING;
        };

        if ( runPythonProcess( scriptFile.get(), inputFile.toAbsolutePath().toString(),
                outputFile.toAbsolutePath().toString() ).getExitValue() != ExitCodesAndSignals.SUCCESS.getExitCode() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_OCCURRED_IN_PYTHON_PROCESS.getKey() ) );
        }
    }

    private static String preparePayloadForPythonScript( String token, DashboardWidgetDTO widget, SuSObjectDataSourceDTO selectedDataSource,
            String objectId ) {
        var widgetSource = ( WidgetVmclSource ) widget.getSource();
        Map< String, Object > payload = new HashMap<>();
        payload.put( DataDashboardConstants.DATA_SOURCE_FIELDS.SOURCE_TYPE, selectedDataSource.getSourceType() );
        payload.put( DataDashboardConstants.DATA_SOURCE_FIELDS.SOURCE_NAME, selectedDataSource.getSourceName() );
        payload.put( DataDashboardConstants.VMCL_FIELDS.VMCL_DATA_SOURCE, widgetSource.getVmclDataSource() );
        if ( widget.getType().equals( DashboardEnums.WIDGET_TYPE.TREEMAP.getId() ) ) {
            payload.put( DataDashboardConstants.TREEMAP_WIDGET_FIELDS.LEAF_DEPTH,
                    ( ( TreemapWidgetOptions ) widget.getOptions() ).getLeafDepth() );
        }
        payload.put( DataDashboardConstants.WIDGET_FIELDS.TITLE, widget.getOptions().getTitle() );
        payload.put( "cachePath", prepareCacheFilePath( objectId, selectedDataSource, TokenizedLicenseUtil.getUserLanguage( token ) ) );
        var dashboardId = new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() );
        var logDirPath = preparePythonLogDirectory( UUID.fromString( widget.getId() ), dashboardId );
        payload.put( "dashboardId", dashboardId );
        payload.put( "logDirectory", logDirPath.toAbsolutePath().toString() );
        return JsonUtils.toJson( payload );
    }

    /**
     * Prepare python log directory path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    public static Path preparePythonLogDirectory( UUID widgetId, VersionPrimaryKey dashboardId ) {
        try {
            Path logPath = prepareLogDirectoryPath( widgetId, dashboardId );
            if ( Files.exists( logPath ) ) {
                FileUtils.deleteNonEmptyDirectory( logPath );
            }
            Files.createDirectory( logPath );
            return logPath;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_INPUT_FILE.getKey() ) );
        }
    }

    private static Path prepareLogDirectoryPath( UUID widgetId, VersionPrimaryKey dashboardId ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + "logs_" + LocalDateTime.now()
                .format( DateTimeFormatter.ofPattern( DateUtils.DATE_PATTERN_FOR_TIMESTAMP ) ) );
    }

    /**
     * Prepare python input file path.
     *
     * @param payload
     *         the payload
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path preparePythonInputFile( String payload, UUID widgetId, VersionPrimaryKey dashboardId ) {
        try {
            Path inputPath = prepareInputFilePath( widgetId, dashboardId );
            Files.deleteIfExists( inputPath );
            Files.createFile( inputPath );
            FileUtils.writeToFile( inputPath.toAbsolutePath().toString(), payload );
            return inputPath;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_INPUT_FILE.getKey() ) );
        }
    }

    private static Path prepareInputFilePath( UUID widgetId, VersionPrimaryKey dashboardId ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + INPUT_FILE_NAME );
    }

    public static Path preparePythonOutputFile( UUID widgetId, VersionPrimaryKey dashboardId ) {
        try {
            Path outputPath = prepareOutputFilePath( widgetId, dashboardId );
            Files.deleteIfExists( outputPath );
            Files.createFile( outputPath );
            FileUtils.setGlobalAllFilePermissions( outputPath );
            return outputPath;
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_SCRIPT_FILE.getKey() ) );
        }
    }

    private static ProcessResult runPythonProcess( String pythonScriptPath, String inputPath, String outputPath ) {
        String command = String.format( "%s %s -i %s -o %s", PropertiesManager.getPythonExecutionPathOnServer(), pythonScriptPath,
                inputPath, outputPath );
        try {
            return LinuxUtils.runSystemCommand( command, DashboardConfigUtil.getTimeoutForPythonProcess() );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_OCCURRED_IN_PYTHON_PROCESS.getKey() ) );
        }

    }

    private static String prepareCacheFilePath( String objectId, SuSObjectDataSourceDTO dataSourceDTO, String userLang ) {
        var dataSourceCacheDirectory = DashboardCacheUtil.getDataSourceDirectoryInCache(
                new VersionPrimaryKey( UUID.fromString( objectId ), dataSourceDTO.getVersionId() ),
                UUID.fromString( dataSourceDTO.getId() ) );
        Path cacheFilePath = DashboardSuSObjectUtil.createCacheDirectoryForCacheWithLanguagePostFix( dataSourceCacheDirectory,
                dataSourceDTO.getProjectId(), userLang );
        return Path.of( cacheFilePath.toAbsolutePath().toString(), CACHE_FILE_NAME ).toString();
    }

    public static HashMap getDataFromJsonFileForWidget( Path outputFilePath ) {
        try {
            FileInputStream fileInputStream = new FileInputStream( outputFilePath.toString() );
            return JsonUtils.jsonToObject( fileInputStream, HashMap.class );
        } catch ( IOException e ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey() ) );
        }
    }

}
