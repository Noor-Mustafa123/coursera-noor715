package de.soco.software.simuspace.suscore.object.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.DashboardEnums;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.DynamicQueryResponse;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;
import de.soco.software.simuspace.suscore.data.model.DataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.DatabaseDataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.WidgetPythonSource;

/**
 * The type Dashboard python util.
 */
@Log4j2
public class DashboardPythonUtil {

    /**
     * The constant INPUT_FILE_NAME.
     */
    private static final String INPUT_FILE_NAME = "python_input.json";

    /**
     * The constant CSV_OUTPUT_FILE.
     */
    private static final String CSV_OUTPUT_FILE = "python_output.csv";

    /**
     * The constant JSON_OUTPUT_FILE.
     */
    private static final String JSON_OUTPUT_FILE = "python_output.json";

    /**
     * The constant PYTHON_FILE_NAME.
     */
    private static final String PYTHON_FILE_NAME = "user_script.py";

    /**
     * The constant runningPythonProcess.
     */
    private static final Map< ThreadKey, Future< ProcessResult > > runningPythonProcess = new ConcurrentHashMap<>();

    /**
     * The type Thread key.
     */
    private record ThreadKey( UUID widgetId, String token ) {

    }

    /**
     * Instantiates a new Dashboard python util.
     */
    private DashboardPythonUtil() {

    }

    /**
     * Gets python output preview.
     *
     * @param dashboardWidgetDTO
     *         the dashboard widget dto
     * @param composedId
     *         the composed id
     *
     * @return the python output preview
     */
    public static Object getPythonOutputPreview( DashboardWidgetDTO dashboardWidgetDTO, VersionPrimaryKey composedId ) {
        WidgetPythonSource source = ( WidgetPythonSource ) dashboardWidgetDTO.getSource();
        var split = source.getOutputType().split( "\\." );
        var outputType = DashboardEnums.WidgetPythonOutputOptions.getById( split[ 1 ] );
        Path outputFilePath = prepareOutputFilePath( UUID.fromString( dashboardWidgetDTO.getId() ), composedId, outputType );
        return previewPythonOutputfile( outputFilePath, outputType );
    }

    /**
     * Read columns from output list.
     *
     * @param composedId
     *         the composed id
     * @param dashboardWidgetDTO
     *         the dashboard widget dto
     * @param pythonOutput
     *         the python output
     *
     * @return the list
     */
    public static List< SelectOptionsUI > readColumnsFromOutput( VersionPrimaryKey composedId, DashboardWidgetDTO dashboardWidgetDTO,
            String pythonOutput ) {
        var outputType = DashboardEnums.WidgetPythonOutputOptions.getById( pythonOutput );
        Path outputFilePath = prepareOutputFilePath( UUID.fromString( dashboardWidgetDTO.getId() ), composedId, outputType );
        return getColumnsFromOutputFile( outputFilePath, outputType );
    }

    /**
     * Gets columns from output file.
     *
     * @param outputFilePath
     *         the output file path
     * @param outputOption
     *         the output option
     *
     * @return the columns from output file
     */
    private static List< SelectOptionsUI > getColumnsFromOutputFile( Path outputFilePath,
            DashboardEnums.WidgetPythonOutputOptions outputOption ) {
        validateOutputFile( outputFilePath, outputOption, false );
        return switch ( outputOption ) {
            case CSV -> DashboardCSVUtil.getColumnsFromCSV( outputFilePath );
            case JSON -> DashboardJsonUtil.getColumnsFromJson( outputFilePath );
        };
    }

    /**
     * Gets query response from python output.
     *
     * @param composedId
     *         the composed id
     * @param dto
     *         the dto
     *
     * @return the query response from python output
     */
    public static DynamicQueryResponse getQueryResponseFromPythonOutput( VersionPrimaryKey composedId, DashboardWidgetDTO dto ) {
        WidgetPythonSource source = ( WidgetPythonSource ) dto.getSource();
        var split = source.getOutputType().split( ConstantsString.DOT_REGEX );
        var outputType = DashboardEnums.WidgetPythonOutputOptions.getById( split[ 1 ] );
        Path outputFilePath = prepareOutputFilePath( UUID.fromString( dto.getId() ), composedId, outputType );
        return switch ( outputType ) {
            case CSV -> DashboardCSVUtil.getDataFromCsvFileForWidget( outputFilePath, dto );
            case JSON -> DashboardJsonUtil.getDataFromJsonFileForWidget( outputFilePath, dto );
        };
    }

    public static List< Map< String, Object > > getDataFromPythonForTableWidget( VersionPrimaryKey composedId, DashboardWidgetDTO dto ) {
        return DataDashboardUtil.convertToListOfMaps( getQueryResponseFromPythonOutput( composedId, dto ).getData() );
    }

    /**
     * Prepare python input file path.
     *
     * @param dataSourceDTO
     *         the dataSourceDTO
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     * @param widgetDTO
     *         the widget dto
     *
     * @return the path
     */
    public static Path preparePythonInputFile( DataSourceDTO dataSourceDTO, UUID widgetId, VersionPrimaryKey dashboardId,
            DashboardWidgetDTO widgetDTO ) {
        try {
            Path inputPath = prepareInputFilePath( widgetId, dashboardId );
            Files.deleteIfExists( inputPath );
            Files.createFile( inputPath );
            var logDirPath = preparePythonLogDirectory( widgetId, dashboardId );
            FileUtils.writeToFile( inputPath.toAbsolutePath().toString(),
                    JsonUtils.toJson( preparePayload( dataSourceDTO, widgetDTO, logDirPath.toAbsolutePath().toString() ) ) );
            return inputPath;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_INPUT_FILE.getKey() ) );
        }
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

    /**
     * Prepare log directory path path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path prepareLogDirectoryPath( UUID widgetId, VersionPrimaryKey dashboardId ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + "logs_" + LocalDateTime.now()
                .format( DateTimeFormatter.ofPattern( DateUtils.DATE_PATTERN_FOR_TIMESTAMP ) ) );
    }

    /**
     * Gets python input preview.
     *
     * @param dataSourceDTO
     *         the data source dto
     * @param dashboardWidgetDTO
     *         the dashboard widget dto
     * @param objectId
     *         the object id
     *
     * @return the python input preview
     */
    public static String getPythonInputPreview( DataSourceDTO dataSourceDTO, DashboardWidgetDTO dashboardWidgetDTO, String objectId ) {
        try {
            if ( dataSourceDTO instanceof DatabaseDataSourceDTO databaseDataSourceDTO ) {
                databaseDataSourceDTO.setPassword( "********" );
            }
            var logDirPath = prepareLogDirectoryPath( UUID.fromString( dashboardWidgetDTO.getId() ),
                    new VersionPrimaryKey( UUID.fromString( objectId ), dashboardWidgetDTO.getVersionId() ) );
            return JsonUtils.toJson( preparePayload( dataSourceDTO, dashboardWidgetDTO, logDirPath.toAbsolutePath().toString() ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_INPUT_PREVIEW.getKey() ) );
        }
    }

    /**
     * Prepare payload map.
     *
     * @param dataSourceDTO
     *         the data source dto
     * @param dashboardWidgetDTO
     *         the dashboard widget dto
     * @param logDirPath
     *         the log dir path
     *
     * @return the map
     */
    private static Map< String, Object > preparePayload( DataSourceDTO dataSourceDTO, DashboardWidgetDTO dashboardWidgetDTO,
            String logDirPath ) {
        Map< String, Object > payload = new HashMap<>();
        payload.put( "dataSource", dataSourceDTO );
        payload.put( "widget", dashboardWidgetDTO );
        payload.put( "logDirectory", logDirPath );
        return payload;
    }

    /**
     * Run widget python path.
     *
     * @param widgetDTO
     *         the widget dto
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     * @param dataSourceDTO
     *         the dataSourceDTO
     * @param token
     *         the token
     *
     * @return the path
     */
    public static ProcessResult runWidgetPython( DashboardWidgetDTO widgetDTO, UUID widgetId, VersionPrimaryKey dashboardId,
            DataSourceDTO dataSourceDTO, String token ) {
        try {
            WidgetPythonSource source = ( WidgetPythonSource ) widgetDTO.getSource();
            if ( source.getPythonPath() != null ) {
                validatePythonEnvironment( source.getPythonPath() );
            }
            checkRunningPythonProcess( widgetId, token );
            Callable< ProcessResult > callableTask = () -> prepareFilesAndSubmitPythonProcess( widgetDTO, widgetId, dashboardId,
                    dataSourceDTO );
            Future< ProcessResult > future = submitPythonProcess( callableTask, token, widgetId );
            ProcessResult processResult = future.get();
            removePythonProcessFromRunning( widgetId, token );
            return processResult;
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( CancellationException e ) {
            log.error( e.getMessage(), e );
            removePythonProcessFromRunning( widgetId, token );
            throw new SusException( MessageBundleFactory.getMessage( Messages.PYTHON_PROCESS_CANCELLED_DUE_TO_NEW_REQUEST.getKey() ) );
        } catch ( ExecutionException e ) {
            var actualException = e.getCause();
            log.error( actualException, e );
            if ( actualException instanceof SusException susException ) {
                throw susException;
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_OCCURRED_IN_PYTHON_PROCESS.getKey() ), e );
            }
        } catch ( InterruptedException e ) {
            log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
            stopPythonProcess( widgetId, token );
            throw new SusException( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
        }
    }

    /**
     * Prepare files and submit python process result.
     *
     * @param widgetDTO
     *         the widget dto
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     * @param dataSourceDTO
     *         the dataSourceDTO
     *
     * @return the process result
     */
    private static ProcessResult prepareFilesAndSubmitPythonProcess( DashboardWidgetDTO widgetDTO, UUID widgetId,
            VersionPrimaryKey dashboardId, DataSourceDTO dataSourceDTO ) {
        WidgetPythonSource source = ( WidgetPythonSource ) widgetDTO.getSource();
        var split = source.getOutputType().split( "\\." );
        var outputType = split[ 1 ];
        Path outputPath = preparePythonOutputFile( widgetId, dashboardId, DashboardEnums.WidgetPythonOutputOptions.getById( outputType ) );
        Path pythonScriptPath = preparePythonScriptFile( widgetDTO, widgetId, dashboardId );
        Path inputPath = preparePythonInputFile( dataSourceDTO, widgetId, dashboardId, widgetDTO );
        var pythonEnvironment =
                source.getPythonPath() == null ? PropertiesManager.getPythonExecutionPathOnServer() : source.getPythonPath();
        ProcessResult processResult = runPythonProcess( pythonEnvironment, pythonScriptPath.toAbsolutePath().toString(),
                inputPath.toAbsolutePath().toString(), outputPath.toAbsolutePath().toString() );
        FileUtils.setOwnerOnlyPermissions( outputPath );
        return processResult;
    }

    /**
     * Run python process.
     *
     * @param pythonEnvironment
     *         the python environment
     * @param pythonScriptPath
     *         the python script path
     * @param inputPath
     *         the input path
     * @param outputPath
     *         the output path
     *
     * @return the process result
     */
    private static ProcessResult runPythonProcess( String pythonEnvironment, String pythonScriptPath, String inputPath,
            String outputPath ) {
        String command = String.format( "%s %s -i %s -o %s", pythonEnvironment, pythonScriptPath, inputPath, outputPath );
        try {
            return LinuxUtils.runSystemCommand( command, DashboardConfigUtil.getTimeoutForPythonProcess() );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_OCCURRED_IN_PYTHON_PROCESS.getKey() ) );
        }

    }

    /**
     * Submit python process future.
     *
     * @param callable
     *         the callable
     * @param token
     *         the token
     * @param widgetId
     *         the widget id
     *
     * @return the future
     */
    private static Future< ProcessResult > submitPythonProcess( Callable< ProcessResult > callable, String token, UUID widgetId ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future< ProcessResult > future = executor.submit( callable );
        addPythonProcessToRunningList( widgetId, token, future );
        return future;
    }

    /**
     * Add python process to running list.
     *
     * @param widgetId
     *         the widget id
     * @param token
     *         the token
     * @param future
     *         the future
     */
    private static void addPythonProcessToRunningList( UUID widgetId, String token, Future< ProcessResult > future ) {
        runningPythonProcess.put( new ThreadKey( widgetId, token ), future );
    }

    /**
     * Check running python process.
     *
     * @param widgetId
     *         the widget id
     * @param token
     *         the token
     */
    private static void checkRunningPythonProcess( UUID widgetId, String token ) {
        var runningProcessKey = runningPythonProcess.keySet().stream().filter( key -> key.widgetId().equals( widgetId ) ).findFirst()
                .orElse( null );
        if ( runningProcessKey == null ) {
            //python process is not running for selected widget
            return;
        }
        if ( !runningProcessKey.token.equals( token ) ) {
            //python process for the selected widget is already running for another user
            throw new SusException( MessageBundleFactory.getMessage( Messages.ANOTHER_USER_IS_WORKING_ON_THE_WIDGET.getKey(),
                    TokenizedLicenseUtil.getNotNullUser( runningProcessKey.token ) ) );
        }
        //running python process belongs to the same user. stop previous process and submit new
        stopPythonProcess( runningProcessKey );
    }

    /**
     * Stop python process.
     *
     * @param runningProcessKey
     *         the running process key
     */
    private static void stopPythonProcess( ThreadKey runningProcessKey ) {
        var pythonProcess = runningPythonProcess.get( runningProcessKey );
        if ( !pythonProcess.isDone() && !pythonProcess.isCancelled() ) {
            pythonProcess.cancel( true );
        }
        removePythonProcessFromRunning( runningProcessKey );
    }

    /**
     * Stop python process.
     *
     * @param widgetId
     *         the widget id
     * @param token
     *         the token
     */
    private static void stopPythonProcess( UUID widgetId, String token ) {
        var runningProcessKey = new ThreadKey( widgetId, token );
        stopPythonProcess( runningProcessKey );
    }

    /**
     * Remove python process from running.
     *
     * @param runningProcessKey
     *         the running process key
     */
    private static void removePythonProcessFromRunning( ThreadKey runningProcessKey ) {
        runningPythonProcess.remove( runningProcessKey );
    }

    /**
     * Remove python process from running.
     *
     * @param widgetId
     *         the widget id
     * @param token
     *         the token
     */
    private static void removePythonProcessFromRunning( UUID widgetId, String token ) {
        removePythonProcessFromRunning( new ThreadKey( widgetId, token ) );
    }

    /**
     * Prepare python script file path.
     *
     * @param widgetDTO
     *         the widget dto
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path preparePythonScriptFile( DashboardWidgetDTO widgetDTO, UUID widgetId, VersionPrimaryKey dashboardId ) {
        WidgetPythonSource pythonSource = ( WidgetPythonSource ) widgetDTO.getSource();
        var split = pythonSource.getScriptOption().split( "\\." );
        var scriptOption = split[ 1 ];
        return switch ( DashboardEnums.WIDGET_SCRIPT_OPTION.getById( scriptOption ) ) {
            case SYSTEM -> null;
            case USER -> preparePythonScriptFileForUserScript( widgetDTO, widgetId, dashboardId );
            case FILE -> preparePythonScriptFileForSelectedScript( widgetDTO, widgetId, dashboardId );
        };
    }

    /**
     * Prepare python script file for selected script path.
     *
     * @param widgetDTO
     *         the widget dto
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path preparePythonScriptFileForSelectedScript( DashboardWidgetDTO widgetDTO, UUID widgetId,
            VersionPrimaryKey dashboardId ) {
        try {
            WidgetPythonSource source = ( WidgetPythonSource ) widgetDTO.getSource();
            Path pythonFilePath = preparePythonFilePathForSelectedScript( widgetId, dashboardId );
            Files.deleteIfExists( pythonFilePath );
            Files.createFile( pythonFilePath );
            var codeInSelectedFile = Files.readString( Path.of( source.getSelectScript() ) );
            FileUtils.writeToFile( pythonFilePath.toAbsolutePath().toString(), codeInSelectedFile );
            return pythonFilePath;
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_SCRIPT_FILE.getKey() ) );
        }
    }

    /**
     * Prepare python script file for user script path.
     *
     * @param widgetDTO
     *         the widget dto
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path preparePythonScriptFileForUserScript( DashboardWidgetDTO widgetDTO, UUID widgetId, VersionPrimaryKey dashboardId ) {
        try {
            WidgetPythonSource source = ( WidgetPythonSource ) widgetDTO.getSource();
            Path pythonFilePath = preparePythonFilePathForUserScript( widgetId, dashboardId );
            Files.deleteIfExists( pythonFilePath );
            Files.createFile( pythonFilePath );
            FileUtils.writeToFile( pythonFilePath.toAbsolutePath().toString(), source.getPythonScript() );
            return pythonFilePath;
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_SCRIPT_FILE.getKey() ) );
        }
    }

    /**
     * Prepare python output file path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     * @param outputType
     *         the output type
     *
     * @return the path
     */
    private static Path preparePythonOutputFile( UUID widgetId, VersionPrimaryKey dashboardId,
            DashboardEnums.WidgetPythonOutputOptions outputType ) {
        try {
            Path outputPath = prepareOutputFilePath( widgetId, dashboardId, outputType );
            Files.deleteIfExists( outputPath );
            Files.createFile( outputPath );
            FileUtils.setGlobalAllFilePermissions( outputPath );
            return outputPath;
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_SCRIPT_FILE.getKey() ) );
        }
    }

    /**
     * Validate python environment.
     *
     * @param pythonEnvironment
     *         the python environment
     */
    private static void validatePythonEnvironment( String pythonEnvironment ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "going to validate environment at {}", pythonEnvironment );
        }
        var pythonEnvPath = Path.of( pythonEnvironment );
        if ( Files.notExists( pythonEnvPath ) || !Files.isReadable( pythonEnvPath ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PYTHON_EXECUTABLE_IS_NOT_READABLE.getKey() ) );
        }
        if ( !Files.isExecutable( pythonEnvPath ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PYTHON_EXECUTABLE_IS_NOT_EXECUTABLE.getKey() ) );
        }
    }

    /**
     * Preview python outputfile list.
     *
     * @param outputFilePath
     *         the output file path
     * @param outputOption
     *         the output option
     *
     * @return the list
     */
    public static Object previewPythonOutputfile( Path outputFilePath, DashboardEnums.WidgetPythonOutputOptions outputOption ) {
        validateOutputFile( outputFilePath, outputOption, true );
        return switch ( outputOption ) {
            case CSV -> DashboardCSVUtil.getCSVPreview( outputFilePath );
            case JSON -> DashboardJsonUtil.getJsonPreview( outputFilePath );
        };
    }

    /**
     * Validate output file.
     *
     * @param outputFilePath
     *         the output file path
     * @param outputOption
     *         the output option
     * @param isForPreview
     *         the is for preview
     */
    private static void validateOutputFile( Path outputFilePath, DashboardEnums.WidgetPythonOutputOptions outputOption,
            boolean isForPreview ) {
        try {
            if ( Files.notExists( outputFilePath ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PYTHON_OUTPUT_FILE_NOT_FOUND.getKey() ) );
            }
            var maxOutputSize = DashboardConfigUtil.getPythonOutputFileSizeLimitForPreview();
            if ( Files.size( outputFilePath ) >= maxOutputSize ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PYTHON_OUTPUT_FILE_TOO_LARGE.getKey(), maxOutputSize ) );
            }
            if ( isForPreview && Files.size( outputFilePath ) <= ConstantsInteger.INTEGER_VALUE_ZERO ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PYTHON_OUTPUT_FILE_IS_EMPTY.getKey() ) );
            }
            switch ( outputOption ) {
                case CSV -> DashboardCSVUtil.validateCSVMimeType( outputFilePath );
            }
        } catch ( IOException e ) {
            throw new SusException( e.getMessage(), e );
        }
    }

    /**
     * Prepare input file path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path prepareInputFilePath( UUID widgetId, VersionPrimaryKey dashboardId ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + INPUT_FILE_NAME );
    }

    /**
     * Prepare output file path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     * @param outputType
     *         the output type
     *
     * @return the path
     */
    private static Path prepareOutputFilePath( UUID widgetId, VersionPrimaryKey dashboardId,
            DashboardEnums.WidgetPythonOutputOptions outputType ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return switch ( outputType ) {
            case CSV -> Path.of( widgetDirectoryInCache + File.separator + CSV_OUTPUT_FILE );
            case JSON -> Path.of( widgetDirectoryInCache + File.separator + JSON_OUTPUT_FILE );
        };
    }

    /**
     * Prepare python file path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path preparePythonFilePathForUserScript( UUID widgetId, VersionPrimaryKey dashboardId ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + PYTHON_FILE_NAME );
    }

    /**
     * Prepare python file path for selected script path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     *
     * @return the path
     */
    private static Path preparePythonFilePathForSelectedScript( UUID widgetId, VersionPrimaryKey dashboardId ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + PYTHON_FILE_NAME );
    }

    /**
     * Read python template file string.
     *
     * @param templateFilePath
     *         the template file path
     *
     * @return the string
     */
    public static String readPythonTemplateFile( String templateFilePath ) {
        try {
            return Files.readString( Path.of( templateFilePath ) );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), templateFilePath ) );
        }
    }

    public static List< TableColumn > populateColumnsMetadataForWidgetFilters( DashboardWidgetDTO widget, String objectId ) {
        WidgetPythonSource source = ( WidgetPythonSource ) widget.getSource();
        var outputType = DashboardEnums.WidgetPythonOutputOptions.getById( source.getOutputType().split( "\\." )[ 1 ] );
        Path outputFilePath = prepareOutputFilePath( UUID.fromString( widget.getId() ),
                new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), outputType );
        return switch ( outputType ) {
            case CSV -> DashboardCSVUtil.populateColumnsMetadataForWidgetFiltersForCsv( outputFilePath );
            case JSON -> DashboardJsonUtil.populateColumnsMetadataForWidgetFiltersForJson( outputFilePath );
        };
    }

}
