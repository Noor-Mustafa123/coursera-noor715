package de.soco.software.simuspace.suscore.object.utility;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.PST_FIELDS;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.PST_ACTIONS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.ExitCodesAndSignals;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.InputTableFormItem;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;
import de.soco.software.simuspace.suscore.data.model.PstWidgetOptions;
import de.soco.software.simuspace.suscore.data.model.WidgetPstSource;

/**
 * The type Dashboard pst util.
 */
@Log4j2
public class DashboardPSTUtil {

    /**
     * The constant OUTPUT_JSON.
     */
    private static final String OUTPUT_FILE = "output_file";

    /**
     * The constant INPUT_JSON.
     */
    private static final String INPUT_JSON = "input.json";

    /**
     * Read test json map.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     * @param action
     *         the action
     *
     * @return the map
     */
    public static Map< String, Object > readOutputFile( UUID widgetId, VersionPrimaryKey dashboardId, PST_ACTIONS action ) {
        try {

            Map< String, Object > fileContent;
            var outputFile = prepareOutputFilePath( widgetId, dashboardId, action.name() );
            if ( Files.exists( outputFile ) ) {
                try ( InputStream is = Files.newInputStream( outputFile ) ) {
                    fileContent = JsonUtils.jsonToObject( is, Map.class );
                }
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), outputFile ) );
            }
            return fileContent;
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_READING_TEST_DATA.getKey() ) );
        }
    }

    /**
     * Call python.
     *
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     * @param action
     *         the action
     * @param data
     *         the data
     */
    public static void callPython( DashboardWidgetDTO widget, String objectId, PST_ACTIONS action, String data ) {
        String payload = preparePayloadForPythonScript( widget, action.name(), data, objectId );
        VersionPrimaryKey dashboardId = new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() );
        var inputFile = preparePythonInputFile( payload, UUID.fromString( widget.getId() ), dashboardId );
        var outputFile = preparePythonOutputFile( UUID.fromString( widget.getId() ), dashboardId, action.name() );
        var scriptFile = DashboardConfigUtil.getPstScriptPath();
        if ( runPythonProcess( scriptFile, inputFile.toAbsolutePath().toString(), outputFile.toAbsolutePath().toString() ).getExitValue()
                != ExitCodesAndSignals.SUCCESS.getExitCode() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_OCCURRED_IN_PYTHON_PROCESS.getKey() ) );
        }
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
            if ( log.isDebugEnabled() ) {
                log.debug( "pst python input file content: {}", payload );
            }
            return inputPath;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_PREPARING_INPUT_FILE.getKey() ) );
        }
    }

    /**
     * Prepare input file path path.
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
        return Path.of( widgetDirectoryInCache + File.separator + INPUT_JSON );
    }

    /**
     * Prepare data for python script string.
     *
     * @param widget
     *         the widget
     * @param action
     *         the action
     * @param payload
     *         the payload
     * @param objectId
     *         the object id
     *
     * @return the string
     */
    private static String preparePayloadForPythonScript( DashboardWidgetDTO widget, String action, String payload, String objectId ) {
        WidgetPstSource widgetSource = ( WidgetPstSource ) widget.getSource();
        PstWidgetOptions widgetOptions = ( PstWidgetOptions ) widget.getOptions();
        Map< String, Object > data = new HashMap<>();
        data.put( PST_FIELDS.ACE_LOUNGE_CSV, widgetSource.getAceLoungeCsv() );
        data.put( PST_FIELDS.BMW_UPDATES, widgetSource.getBmwUpdates() );
        data.put( PST_FIELDS.KS_UPDATES, widgetSource.getKsUpdates() );
        data.put( PST_FIELDS.APL_UPDATES, widgetSource.getAplUpdates() );
        data.put( PST_FIELDS.ACTION, action );
        data.put( PST_FIELDS.PRUFSTAND, widgetOptions.getPrufstand() );
        data.put( PST_FIELDS.PROGRAMM, widgetOptions.getProgramm() );
        data.put( PST_FIELDS.MOTORTYP, widgetOptions.getMotortyp() );
        data.put( PST_FIELDS.MOTOR_BG, widgetOptions.getMotor_bg() );
        data.put( PST_FIELDS.STATUS, widgetOptions.getStatus() );
        data.put( PST_FIELDS.VORBEREITUNG, widgetOptions.getVorbereitung() );
        data.put( "widgetId", widget.getId() );
        data.put( PST_FIELDS.PAYLOAD, StringUtils.isNotEmpty( payload ) ? JsonUtils.jsonToMap( payload, new HashMap<>() ) : payload );
        data.put( "logDirectory", PythonUtils.getLogsDirectoryBasePath() );
        data.put( "cacheDirectory", PythonUtils.getCentralPythonCachePath() );
        return JsonUtils.toJson( data );
    }

    /**
     * Run python process process result.
     *
     * @param pythonScriptPath
     *         the python script path
     * @param inputPath
     *         the input path
     * @param outputPath
     *         the output path
     *
     * @return the process result
     */
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

    /**
     * Prepare output file path path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     * @param action
     *         the action
     *
     * @return the path
     */
    private static Path prepareOutputFilePath( UUID widgetId, VersionPrimaryKey dashboardId, String action ) {
        var widgetDirectoryInCache = DashboardCacheUtil.getWidgetDirectoryInCache( dashboardId, widgetId );
        return Path.of( widgetDirectoryInCache + File.separator + action + ConstantsString.UNDERSCORE + OUTPUT_FILE );
    }

    /**
     * Prepare python output file path.
     *
     * @param widgetId
     *         the widget id
     * @param dashboardId
     *         the dashboard id
     * @param action
     *         the action
     *
     * @return the path
     */
    private static Path preparePythonOutputFile( UUID widgetId, VersionPrimaryKey dashboardId, String action ) {
        try {
            Path outputPath = prepareOutputFilePath( widgetId, dashboardId, action );
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
     * Perform pst action object.
     *
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     * @param action
     *         the action
     * @param data
     *         the data
     * @param token
     *         the token
     *
     * @return the object
     */
    public static Object performPSTAction( DashboardWidgetDTO widget, String objectId, PST_ACTIONS action, String data, String token ) {
        callPython( widget, objectId, action, data );
        return switch ( action ) {
            case update, add_form, add_test, edit_form, edit_test, couple_test, add_archive, read_archive, delete_archive, list_archives,
                 get_benches, remove_couple, get, restore_archive -> readOutputFile( UUID.fromString( widget.getId() ),
                    new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), action );
            case export_differences -> getExportDifferencesLink( widget, objectId, action.name(), token );
        };
    }

    /**
     * Gets export differences link.
     *
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     * @param action
     *         the action
     * @param token
     *         the token
     *
     * @return the export differences link
     */
    private static Map< String, String > getExportDifferencesLink( DashboardWidgetDTO widget, String objectId, String action,
            String token ) {
        checkOutputFileExists( UUID.fromString( widget.getId() ),
                new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), action );
        Map< String, String > response = new HashMap<>();
        String downloadBase = "/data/dashboard/" + objectId + "/widget/" + widget.getId() + "/download/" + action;
        response.put( "url", downloadBase );
        return response;
    }

    /**
     * Check output file exists.
     *
     * @param uuid
     *         the uuid
     * @param versionPrimaryKey
     *         the version primary key
     * @param action
     *         the action
     */
    private static void checkOutputFileExists( UUID uuid, VersionPrimaryKey versionPrimaryKey, String action ) {
        try {
            var outputFile = prepareOutputFilePath( uuid, versionPrimaryKey, action );
            if ( Files.exists( outputFile ) && Files.size( outputFile ) > ConstantsInteger.INTEGER_VALUE_ZERO ) {
                return;
            }
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), outputFile.toAbsolutePath().toString() ) );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        }
    }

    /**
     * Gets download file path.
     *
     * @param widget
     *         the widget
     * @param objectId
     *         the object id
     * @param action
     *         the action
     *
     * @return the download file path
     */
    public static Path getDownloadFilePath( DashboardWidgetDTO widget, String objectId, String action ) {
        return prepareOutputFilePath( UUID.fromString( widget.getId() ),
                new VersionPrimaryKey( UUID.fromString( objectId ), widget.getVersionId() ), action );
    }

    /**
     * Populate options fields for pst widget.
     *
     * @param formItems
     *         the form items
     */
    public static void populateOptionsFieldsForPstWidget( List< UIFormItem > formItems ) {
        for ( UIFormItem item : formItems ) {
            switch ( item.getName() ) {
                case PST_FIELDS.PRUFSTAND -> {
                    var prufstand = new InputTableFormItem.OptionField( "PST Ort", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    var kaelte = new InputTableFormItem.OptionField( "Kaelte", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    var motortypPossible = new InputTableFormItem.OptionField( "Motortyp Possible", FieldTypes.TEXT.getType(),
                            new ArrayList<>(), "No" );
                    var dauerlaufPossible = new InputTableFormItem.OptionField( "Dauerlauf Possible", FieldTypes.TEXT.getType(),
                            new ArrayList<>(), "No" );
                    var unavailableDates = new InputTableFormItem.OptionField( "Unavailable Dates", FieldTypes.DATE_RANGE.getType(),
                            new ArrayList<>(), "No" );
                    var pstType = new InputTableFormItem.OptionField( "PST Type", FieldTypes.SELECTION.getType(), List.of( "AMP", "MGM" ),
                            "No" );
                    List< InputTableFormItem.OptionField > options = List.of( prufstand, kaelte, motortypPossible, dauerlaufPossible,
                            unavailableDates, pstType );
                    ( ( InputTableFormItem ) item ).setFields( options );
                }
                case PST_FIELDS.PROGRAMM -> {
                    var programm = new InputTableFormItem.OptionField( "Programm", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    var kaelte = new InputTableFormItem.OptionField( "Kaelte", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    var sollZykKm = new InputTableFormItem.OptionField( "Soll Zyk/km", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    ( ( InputTableFormItem ) item ).setFields( List.of( programm, kaelte, sollZykKm ) );
                }
                case PST_FIELDS.MOTORTYP -> {
                    var motortyp = new InputTableFormItem.OptionField( "Motortyp", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    var inverterStorm = new InputTableFormItem.OptionField( "Inverter-Strom", FieldTypes.TEXT.getType(), new ArrayList<>(),
                            "No" );
                    var ubersetzung = new InputTableFormItem.OptionField( "Ubersetzung", FieldTypes.TEXT.getType(), new ArrayList<>(),
                            "No" );
                    var haupt = new InputTableFormItem.OptionField( "Haupt", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    var vertretung = new InputTableFormItem.OptionField( "Vertretung", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    ( ( InputTableFormItem ) item ).setFields( List.of( motortyp, inverterStorm, ubersetzung, haupt, vertretung ) );
                }
                case PST_FIELDS.MOTOR_BG -> {
                    var motorBg = new InputTableFormItem.OptionField( "Motor-BG", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    ( ( InputTableFormItem ) item ).setFields( List.of( motorBg ) );
                }
                case PST_FIELDS.STATUS -> {
                    var status = new InputTableFormItem.OptionField( "Status", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    var color = new InputTableFormItem.OptionField( "Color", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    var moveAllowed = new InputTableFormItem.OptionField( "Move Allowed", FieldTypes.TEXT.getType(), new ArrayList<>(),
                            "No" );
                    ( ( InputTableFormItem ) item ).setFields( List.of( status, color, moveAllowed ) );
                }
                case PST_FIELDS.VORBEREITUNG -> {
                    var vorbereitung = new InputTableFormItem.OptionField( "Vorbereitung", FieldTypes.TEXT.getType(), new ArrayList<>(),
                            "No" );
                    var color = new InputTableFormItem.OptionField( "Color", FieldTypes.TEXT.getType(), new ArrayList<>(), "No" );
                    ( ( InputTableFormItem ) item ).setFields( List.of( vorbereitung, color ) );
                }
            }

        }
    }

}
