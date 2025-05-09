package de.soco.software.simuspace.susdash.pst.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDate;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.model.DashboardPluginConfigDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.DashboardPluginUtil;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.susdash.pst.constants.PstConstants;
import de.soco.software.simuspace.susdash.pst.model.PstDTO;

/**
 * The type Pst util.
 */
@Log4j2
public class PstUtil {

    /**
     * The Move test lock.
     */
    private final static ReentrantLock editTestLock = new ReentrantLock( true );

    /**
     * The constant addTestLock.
     */
    private final static ReentrantLock addTestLock = new ReentrantLock( true );

    /**
     * The constant coupleTestLock.
     */
    private final static ReentrantLock coupleTestLock = new ReentrantLock( true );

    /**
     * The Update pre req lock.
     */
    private final static ReentrantLock updatePreReqLock = new ReentrantLock( true );

    /**
     * The Python call lock.
     */
    private final static ReentrantLock pythonCallLock = new ReentrantLock( true );

    /**
     * The constant getFormLock.
     */
    private static final ReentrantLock getFormLock = new ReentrantLock( true );

    /**
     * The constant getLegendLock.
     */
    private static final ReentrantLock getLegendLock = new ReentrantLock( true );

    /**
     * Acquire lock by action.
     *
     * @param action
     *         the action
     */
    public static void acquireLockByAction( String action ) {
        ReentrantLock lock;
        lock = getReentrantLockFromAction( action );
        if ( lock != null ) {
            log.info( action + " action called" );
            if ( lock.isLocked() ) {
                log.info( "waiting for previous " + action + " to update" );
                if ( log.isDebugEnabled() ) {
                    log.debug( "waiting queue size for " + action + " call: " + lock.getQueueLength() );
                }
            }
            lock.lock();
        }
    }

    /**
     * Gets reentrant lock from action.
     *
     * @param action
     *         the action
     *
     * @return the reentrant lock from action
     */
    private static ReentrantLock getReentrantLockFromAction( String action ) {
        ReentrantLock lock;
        switch ( action ) {
            case PstConstants.ACTIONS.ADD_TEST -> lock = addTestLock;
            case PstConstants.ACTIONS.COUPLE_TEST -> lock = coupleTestLock;
            case PstConstants.ACTIONS.EDIT_TEST -> lock = editTestLock;
            case PstConstants.ACTIONS.UPDATE_PREREQ -> lock = updatePreReqLock;
            case PstConstants.ACTIONS.GET_FORM -> lock = getFormLock;
            case PstConstants.ACTIONS.GET_LEGEND -> lock = getLegendLock;
            default -> lock = null;
        }
        return lock;
    }

    /**
     * Release lock by action.
     *
     * @param action
     *         the action
     */
    public static void releaseLockByAction( String action ) {
        ReentrantLock lock;
        lock = getReentrantLockFromAction( action );
        if ( lock != null ) {
            lock.unlock();
        }
    }

    /**
     * Gets form item value from dto.
     *
     * @param item
     *         the item
     * @param pstDTO
     *         the pst dto
     *
     * @return the form item value from dto
     */
    public static Object getFormItemValueFromDTO( UIFormItem item, PstDTO pstDTO ) {
        return switch ( item.getName() ) {
            case PstConstants.JSON_PROPERTIES.MOTOR_NR -> pstDTO.getMotorNr();
            case PstConstants.JSON_PROPERTIES.PST_ORT -> pstDTO.getPstOrt();
            case PstConstants.JSON_PROPERTIES.STATUS -> pstDTO.getStatus();
            case PstConstants.JSON_PROPERTIES.VERKN -> pstDTO.getVerkn();
            case PstConstants.JSON_PROPERTIES.MOTOR_BG -> pstDTO.getMotorBG();
            case PstConstants.JSON_PROPERTIES.MOTOR_TYPE -> pstDTO.getMotorType();
            case PstConstants.JSON_PROPERTIES.PROGRAM -> pstDTO.getProgram();
            case PstConstants.JSON_PROPERTIES.TOTAL_CYCLES -> pstDTO.getTotalCycles();
            case PstConstants.JSON_PROPERTIES.CURRENT_CYCLES -> pstDTO.getCurrentCycles();
            case PstConstants.JSON_PROPERTIES.ACTUAL_START_DATE -> pstDTO.getActualStartDate();
            case PstConstants.JSON_PROPERTIES.ACTUAL_END_DATE -> pstDTO.getActualEndDate();
            case PstConstants.JSON_PROPERTIES.PLANNED_START_DATE -> pstDTO.getPlannedStartDate();
            case PstConstants.JSON_PROPERTIES.PLANNED_END_DATE -> pstDTO.getPlannedEndDate();
            case PstConstants.JSON_PROPERTIES.ID -> pstDTO.getId();
            default -> ConstantsString.EMPTY_STRING;
        };
    }

    /**
     * Read form items from form json list.
     *
     * @param propertiesMap
     *         the properties map
     *
     * @return the list
     */
    public static List< UIFormItem > readFormItemsFromFormJson( Path formJsonPath ) {
        try {
            Map< String, Object > fileContent;
            List< UIFormItem > returnList = new ArrayList<>();
            if ( Files.exists( formJsonPath ) ) {
                try ( InputStream is = Files.newInputStream( formJsonPath ) ) {
                    fileContent = JsonUtils.jsonToObject( is, Map.class );
                    var list = ( List< Object > ) fileContent.get( "fields" );
                    for ( var item : list ) {
                        var itemJson = JsonUtils.toJson( item );
                        returnList.add( JsonUtils.jsonToObject( itemJson, SelectFormItem.class ) );
                    }
                    return returnList;

                }
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), formJsonPath ) );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_READING_BENCHES_DATA.getKey() ) );
        }
    }

    /**
     * Call pst script int.
     *
     * @param arguments
     *         the arguments
     * @param config
     *         the config
     *
     * @return the int
     */
    public static int callPstScript( String arguments, DashboardPluginConfigDTO config ) {
        try {
            log.info( "callPstPlanningScript  method called" );
            if ( pythonCallLock.isLocked() ) {
                log.info( "waiting for previous script call to complete" );
                if ( log.isDebugEnabled() ) {
                    log.debug( "waiting queue size for pst script: " + pythonCallLock.getQueueLength() );
                }
            }
            pythonCallLock.lock();
            var scriptPath = DashboardPluginUtil.getScriptPathFromConfig( PstConstants.CONFIG_KEYS.PLANNING_SCRIPT_NAME, config );
            var processResult = PythonUtils.callPstPlanningScript( scriptPath, arguments );
            if ( processResult.getExitValue() != 0 ) {
                throw new SusException( processResult.getErrorStreamString() );
            }
            return processResult.getExitValue();
        } finally {
            pythonCallLock.unlock();
        }
    }

    /**
     * Gets time until first run.
     *
     * @param now
     *         the now
     * @param pstPlanningConfig
     *         the pst planning config
     *
     * @return the time until first run
     */
    public static long getTimeUntilFirstRun( Calendar now, DashboardPluginConfigDTO pstPlanningConfig ) {
        var configuredTime = pstPlanningConfig.getProperties().get( 0 ).get( PstConstants.CONFIG_KEYS.UPDATE_TIME ).toString()
                .split( ConstantsString.COLON );
        Calendar startTime = Calendar.getInstance();
        startTime.set( Calendar.HOUR_OF_DAY, Integer.parseInt( configuredTime[ 0 ] ) );
        startTime.set( Calendar.MINUTE, Integer.parseInt( configuredTime[ 1 ] ) );
        startTime.set( Calendar.SECOND, 0 );
        startTime.set( Calendar.MILLISECOND, 0 );
        if ( log.isDebugEnabled() ) {
            log.debug( String.format( "pst update thread scheduled for %s daily",
                    DateUtils.changeFormatOfDateProvided( startTime.getTime(), ConstantsDate.TIME_ONLY_FORMAT ) ) );
        }
        // Calculate the delay until 6 o'clock
        return startTime.getTimeInMillis() - now.getTimeInMillis();
    }

    /**
     * Prepare pst args string.
     *
     * @param filePath
     *         the file path
     *
     * @return the string
     */
    public static String preparePstArgs( String filePath ) {
        return "-i " + filePath;
    }

    /**
     * Prepare pst input file string.
     *
     * @param propertiesMap
     *         the properties map
     * @param objectId
     *         the object id
     * @param action
     *         the action
     * @param payload
     *         the payload
     * @param outputFilePath
     *         the output file path
     * @param archiveName
     *         the archive name
     * @param language
     *         the language
     *
     * @return the string
     */
    public static Path preparePstInputFile( Map< String, Object > propertiesMap, String objectId, String action, Object payload,
            String outputFilePath, String archiveName, String language ) {
        Path inputFilePath = Paths.get( PropertiesManager.getDefaultServerTempPath() + File.separator + objectId
                + ConstantsString.UNDERSCORE + action + PstConstants.INPUT_POSTFIX );
        if ( Files.exists( inputFilePath ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_ALREADY_EXIST_ON_SERVER.getKey() ) );
        } else {
            String inputData = preparePstInputData( propertiesMap, action, payload, outputFilePath, archiveName, language );
            if ( log.isDebugEnabled() ) {
                log.debug( "pst python input file content: \n" + inputData );
            }
            try {
                return Files.writeString( inputFilePath, inputData, StandardCharsets.UTF_8 );
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_INPUT_FILE.getKey(),
                        inputFilePath.toAbsolutePath().toAbsolutePath() ) );
            }
        }

    }

    /**
     * Prepare pst input data string.
     *
     * @param propertiesMap
     *         the properties map
     * @param action
     *         the action
     * @param payload
     *         the payload
     * @param outputFilePath
     *         the output file path
     * @param archiveName
     *         the archive name
     * @param language
     *         the language
     *
     * @return the string
     */
    private static String preparePstInputData( Map< String, Object > propertiesMap, String action, Object payload, String outputFilePath,
            String archiveName, String language ) {
        Map< String, Object > inputDataMap = new HashMap<>( propertiesMap );
        inputDataMap.put( PstConstants.INPUT_FILE_KEYS.ACTION, action );
        inputDataMap.put( PstConstants.INPUT_FILE_KEYS.PAYLOAD, payload );
        inputDataMap.put( PstConstants.INPUT_FILE_KEYS.OUTPUT_FILE, outputFilePath );
        inputDataMap.put( PstConstants.INPUT_FILE_KEYS.ARCHIVE, archiveName );
        inputDataMap.put( PstConstants.INPUT_FILE_KEYS.LANGUAGE, language );
        return JsonUtils.toJson( inputDataMap );

    }

    /**
     * Read json file as map map.
     *
     * @param outputFile
     *         the output file
     *
     * @return the map
     *
     * @throws IOException
     *         the io exception
     */
    public static Map< String, Object > readJsonFileAsMap( Path outputFile ) throws IOException {
        Map< String, Object > fileContent;
        if ( Files.exists( outputFile ) ) {
            try ( InputStream is = Files.newInputStream( outputFile ) ) {
                fileContent = JsonUtils.jsonToObject( is, Map.class );
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), outputFile ) );
        }
        return fileContent;
    }

    /**
     * Prepare object view object view dto.
     *
     * @param objectJson
     *         the object json
     * @param save
     *         the save
     *
     * @return the object view dto
     */
    public static ObjectViewDTO prepareObjectView( String objectJson, boolean save ) {
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
        if ( save && !objectViewDTO.isDefaultView() ) {
            objectViewDTO.setId( null );
        }
        objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.PST_PLANNING_TABLE_VIEW_KEY );
        objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
        objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
        return objectViewDTO;
    }

}
