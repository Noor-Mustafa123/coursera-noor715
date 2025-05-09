package de.soco.software.simuspace.workflow.processing.impl;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.ElementOutputWait;
import de.soco.software.simuspace.workflow.util.JobLog;

/**
 * This Class designed to execute a wait element to suspend workflow for some duration e.g. seconds, due date, for file before creation.
 */
@Log4j2
public class WorkflowWaitElementAction extends WFElementAction {

    /**
     * Auto generated serial version UID.
     */
    private static final long serialVersionUID = 3195887977902136425L;

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * This constant is for message. i.e Wait Element Completed.
     */
    public static final String WAIT_ELEM_COMPL = "Wait Element Completed";

    /**
     * This constant is for message. i.e Element submitted for Wait : "
     */
    public static final String WAIT_ELEMENT = "Element submitted for Wait : ";

    /**
     * The file.
     */
    private static final String FILE = "file";

    /**
     * The time in sec.
     */
    private static final String TIME_IN_SEC = "time";

    /**
     * The due date to expire.
     */
    private static final String DUE_DATE_TO_EXPIRE = "date";

    /**
     * The path file lock.
     */
    private static final String PATH_FILE_LOCK = "file_unlocked";

    /**
     * The Constant SERVER_FILE_EXPLORER.
     */
    private static final String SERVER_FILE_EXPLORER = "server-file-explorer";

    /**
     * The date formate.
     */
    private static final String DATE_FORMATE = "MM-dd-yyyy HH:mm:ss";

    /**
     * The process sleep duration.
     */
    private static final long PROCESS_SLEEP_DURATION = 1000L;

    /**
     * The directory attribute.
     */
    private static final String DIRECTORY_ATTRIBUTE = "basic:isDirectory";

    /**
     * The path.
     */
    private static final String PATH = "Path : ";

    /**
     * The issue with path.
     */
    private static final String ISSUE_WITH_PATH = " having some issue.";

    /**
     * The is not a folder.
     */
    private static final String IS_NOT_A_FOLDER = " is not a folder";

    /**
     * The file locking permission.
     */
    private static final String FILE_LOCKING_PERMISSION = "rw";

    /**
     * The locking fail.
     */
    private static final String LOCKING_FAIL = "Locking failed : ";

    /**
     * The ele wait exec suspended.
     */
    private static final String ELE_WAIT_EXEC_SUSPENDED = "Element Wait execution suspended for Date : ";

    /**
     * The Constant DEFULT_EXIT_CODE.
     */
    private static final String DEFULT_EXIT_CODE = "0";

    /**
     * The Constant EMPTY_STRING.
     */
    private static final String EMPTY_STRING = "";

    private boolean fileLock = false;

    /**
     * The file not exist.
     */
    private boolean waitChecker = true;

    /**
     * The job element.
     */
    private final transient UserWFElement element;

    /**
     * The Job impl.
     */
    private final transient Job jobImpl;

    /**
     * Instantiates a new workflow wait element action.
     *
     * @param jobImpl
     *         the job impl
     * @param parameters
     *         the parameters
     * @param element
     *         the element
     */
    public WorkflowWaitElementAction( Job jobImpl, Map< String, Object > parameters, UserWFElement element ) {
        super( jobImpl, element );
        this.jobImpl = jobImpl;
        this.parameters = parameters;
        this.element = element;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Instantiates a new workflow wait element action.
     *
     * @param jobImpl
     *         the job impl
     * @param parameters
     *         the parameters
     * @param element
     *         the element
     * @param executedElementIds
     *         the executed element ids
     */
    public WorkflowWaitElementAction( Job jobImpl, Map< String, Object > parameters, UserWFElement element,
            Set< String > executedElementIds ) {
        super( jobImpl, element, executedElementIds );
        this.jobImpl = jobImpl;
        this.parameters = parameters;
        this.element = element;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Adds the wait in thread.
     *
     * @return the notification
     */
    private Notification addWaitInThread() {
        final Notification notif = new Notification();
        final String selectionType = getWaitSelection();
        final String selectionValue = getWaitSelectionValue( notif );
        if ( StringUtils.isNotNullOrEmpty( selectionType ) && StringUtils.isNotNullOrEmpty( selectionValue ) ) {
            switch ( selectionType ) {
                case TIME_IN_SEC -> {
                    if ( NumberUtils.isCreatable( selectionValue )
                            && ( Double.valueOf( selectionValue ).longValue() > ConstantsInteger.INTEGER_VALUE_ZERO ) ) {
                        notif.addNotification( waitTimerProcessInSeconds( Double.valueOf( selectionValue ).longValue() ) );
                    } else {
                        notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INAVLID_SECONDS_PROVIDED, selectionValue ) ) );
                    }
                }
                case FILE, SERVER_FILE_EXPLORER ->
                        notif.addNotification( fileActionViaWatchingDirectoryOrByAchievingLockFile( selectionValue, false ) );
                case DUE_DATE_TO_EXPIRE -> notif.addNotification( waitElementProcessForDueDate( selectionValue ) );
                case PATH_FILE_LOCK -> notif.addNotification( fileActionViaWatchingDirectoryOrByAchievingLockFile( selectionValue, true ) );
                default -> {
                    // do nothing
                }
            }
        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }
        return notif;
    }

    /**
     * Do action.
     *
     * @return the notification
     */
    public Notification doAction() {
        final Notification notif = new Notification();
        if ( parameters == null ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.EMPTY_PARAM ) ) );
        } else if ( element != null ) {
            notif.addNotification( element.validateException() );
            if ( notif.hasErrors() ) {
                return notif;
            }
            notif.addNotification( addWaitInThread() );
        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }
        return notif;
    }

    /**
     * Function to perform a task. This will be executed if a task is going to be executed.
     *
     * @return the decision object
     */
    @Override
    public DecisionObject execute() {

        try {

            final int executionTime = element.getExecutionValue();
            addLogForUnlimitedExecution( executionTime );

            final ExecutorService executor = Executors.newFixedThreadPool( ConstantsInteger.INTEGER_VALUE_ONE );

            final ElementOutputWait elementOutput = new ElementOutputWait();
            executeWaitElement( executor, executionTime, elementOutput );

            // wait all unfinished tasks for 2 sec
            try {
                if ( !executor.awaitTermination( ConstantsInteger.INTEGER_VALUE_TWO, TimeUnit.SECONDS ) ) {
                    executor.shutdownNow();
                }
            } catch ( final InterruptedException e ) {
                log.error( "Executor shoutdown interrupted.", e );
                elementOutput.setExitCode( EMPTY_STRING );
                Thread.currentThread().interrupt();
            }
            writeOutPutFile( elementOutput, DEFULT_EXIT_CODE );
            setJobResultParameters();

            return new DecisionObject( true, element.getKey(), parameters, workflowOutput );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            updateJobAndStatusFailed( e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Function to perform a task. This will be executed if a task is going to be executed.
     *
     * @param executor
     *         the executor
     * @param executionTime
     *         the execution time
     */
    private void executeWaitElement( ExecutorService executor, int executionTime, ElementOutputWait elementOutput ) {
        final Runnable task = () -> {
            try {
                wfLogger.info( WAIT_ELEMENT + WITH_NAME + element.getName() + FIELD_PARAM + parameters );
                JobLog.addLog( jobImpl.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + WITH_NAME + element.getName() ) );
                final Notification notif = doAction();
                processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
                JobLog.addLog( jobImpl.getId(),
                        new LogRecord( ConstantsMessageTypes.SUCCESS, WAIT_ELEM_COMPL + WITH_NAME + element.getName() ) );
                executedElementsIds.add( element.getId() );
                wfLogger.info( WAIT_ELEM_COMPL + WITH_NAME + element.getName() );
                if ( !job.isFileRun() ) {
                    JobLog.updateLogAndProgress( job, executedElementsIds.size() );
                }
            } catch ( final SusException e ) {
                log.error( "Wait Execution Error : ", e );
                wfLogger.error( "Wait Execution Error : " + e.getMessage() );
                try {
                    JobLog.addLog( jobImpl.getId(),
                            new LogRecord( ConstantsMessageTypes.ERROR, "Element : " + element.getName() + " : " + e ) );
                    updateJobAndStatusFailed( e );
                } catch ( final SusException e1 ) {
                    log.error( e1.getLocalizedMessage(), e1 );
                    wfLogger.error( e1.getLocalizedMessage() );
                }
                Thread.currentThread().interrupt();
                writeOutPutFile( elementOutput, DEFULT_EXIT_CODE );
                throw new SusRuntimeException( e.getMessage() );
            }
        };

        final Future< ? > future = executor.submit( task );

        executor.shutdown();

        try {
            if ( ( executionTime == ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT ) || ( executionTime
                    == ConstantsInteger.ELEMENT_NOT_EXECUTE_AT_ALL ) ) {
                future.get();
            } else {
                wfLogger.info(
                        MessagesUtil.getMessage( WFEMessages.ELEMENT_IS_GOING_TO_EXECUTE_FOR_SECONDS, element.getName(), executionTime ) );
                // <-- wait for runtime seconds to finish
                future.get( executionTime, TimeUnit.SECONDS );
            }
        } catch ( final InterruptedException e ) {
            log.error( "job was interrupted ", e );
            Thread.currentThread().interrupt();
            writeOutPutFile( elementOutput, DEFULT_EXIT_CODE );
            throw new SusRuntimeException( e.getMessage() );
        } catch ( final ExecutionException e ) {
            log.error( "caught exception: ", e );
            writeOutPutFile( elementOutput, DEFULT_EXIT_CODE );
            throw new SusRuntimeException( e.getMessage() );
        } catch ( final TimeoutException e ) {
            future.cancel( true );
            log.error( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, element.getName() ), e );
            writeOutPutFile( elementOutput, DEFULT_EXIT_CODE );
            throw new SusRuntimeException( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, element.getName() ) );
        }

    }

    /**
     * File action.
     *
     * @param selectionValue
     *         the selection value
     * @param lockerFlag
     *         the locker flag
     *
     * @return the notification
     */
    private Notification fileActionViaWatchingDirectoryOrByAchievingLockFile( final String selectionValue, boolean lockerFlag ) {
        final Notification notif = new Notification();
        final File file = new File( selectionValue );
        if ( !file.getAbsoluteFile().exists() ) {
            if ( file.getParent() != null ) {
                notif.addNotification( waitDirectoryWatcherForSpecificFile( Paths.get( file.getParent() ), file.getName(), lockerFlag ) );
            } else {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.FILE_PATH_NOT_EXIST, file.getPath() ) ) );
            }
        } else if ( lockerFlag ) {
            notif.addNotification( fileLocking( file ) );
        }
        return notif;
    }

    /**
     * File locking.
     *
     * @param file
     *         the file
     *
     * @return the notification
     */
    private Notification fileLocking( final File file ) {
        log.info( MessagesUtil.getMessage( WFEMessages.LOCKING_START_FOR_FILE, file.getName() ) );
        wfLogger.info( MessagesUtil.getMessage( WFEMessages.LOCKING_START_FOR_FILE, file.getName() ) );
        final Notification notif = new Notification();
        final Runnable task = () -> {
            while ( !fileLock ) {
                try ( final FileChannel channel = new RandomAccessFile( file, FILE_LOCKING_PERMISSION ).getChannel() ) {
                    final FileLock lock = channel.lock();
                    if ( lock.isValid() ) {
                        fileLock = true;
                        log.info( MessagesUtil.getMessage( WFEMessages.LOCK_ACHIEVED, file.getName() ) );
                        wfLogger.info( MessagesUtil.getMessage( WFEMessages.LOCK_ACHIEVED, file.getName() ) );
                    }
                    lock.release();
                } catch ( final FileNotFoundException e ) {
                    log.error( e.getLocalizedMessage(), e );
                    wfLogger.error( LOCKING_FAIL + file.getName() );
                    try {
                        Thread.sleep( PROCESS_SLEEP_DURATION );
                    } catch ( final InterruptedException ie ) {
                        log.error( ie.getLocalizedMessage(), ie );
                        Thread.currentThread().interrupt();
                    }
                } catch ( final Exception e ) {
                    log.error( e.getLocalizedMessage(), e );
                    notif.addError( new Error( LOCKING_FAIL + file.getName() ) );

                    wfLogger.error( LOCKING_FAIL + file.getName() );
                }
            }
        };
        try {
            final Thread thread = new Thread( task );
            thread.start();
            thread.join();
        } catch ( final InterruptedException e ) {
            log.info( e.getMessage(), e );
            Thread.currentThread().interrupt();
        }
        return notif;
    }

    /**
     * Gets the wait selection.
     *
     * @return the wait selection
     */
    private String getWaitSelection() {
        String selection = ConstantsString.EMPTY_STRING;
        for ( final Field< ? > elementField : element.getFields() ) {
            if ( elementField.getType().contentEquals( FieldTypes.SELECTION.getType() ) ) {
                selection = elementField.getValue().toString();
                break;
            }
        }
        return selection;
    }

    /**
     * Gets the wait selection value.
     *
     * @return the wait selection value
     */
    private String getWaitSelectionValue( final Notification notif ) {
        String selection = ConstantsString.EMPTY_STRING;
        for ( final Field< ? > elementField : element.getFields() ) {
            if ( elementField.getType().contains( FieldTypes.TEXT.getType() ) ) {
                final String strValue = elementField.getValue().toString();
                if ( elementField.isVariableMode() ) {
                    if ( strValue.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !strValue.startsWith(
                            ConstantsString.SYS_CMD_INDICATION ) ) {
                        cmd = strValue;
                        final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( cmd ) );
                        if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
                            final List< String > variablesIncludingDotResolved = new ArrayList<>();
                            for ( String splitters : variablesIncludingDot ) {
                                variablesIncludingDotResolved.add( replaceAfterDotVariablesForSupportOfJsonExtract( splitters ) );
                            }
                            computeGlobalCmdForMultipleDotKeys( new Notification(), variablesIncludingDotResolved, variablesIncludingDot );
                        }
                        final List< String > variables = getAllSimpleVariables( cmd );
                        if ( CollectionUtils.isNotEmpty( variables ) ) {
                            computeGlobalCmd( notif, variables );
                        }
                        selection = cmd;
                    } else {
                        selection = strValue;
                    }
                } else {
                    selection = strValue;
                }
            } else if ( elementField.getType().contains( FieldTypes.DATE.getType() ) || elementField.getType()
                    .contains( FieldTypes.INTEGER.getType() ) ) {
                final String strValue = elementField.getValue().toString();
                if ( strValue.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !strValue.startsWith(
                        ConstantsString.SYS_CMD_INDICATION ) ) {
                    cmd = strValue;
                    final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( cmd ) );
                    if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
                        computeGlobalCmd( new Notification(), variablesIncludingDot );
                    }
                    final List< String > variables = getAllSimpleVariables( cmd );
                    if ( CollectionUtils.isNotEmpty( variables ) ) {
                        computeGlobalCmd( notif, variables );
                    }
                    selection = cmd;
                } else {
                    selection = elementField.getValue().toString();
                }
            } else if ( elementField.getType().contentEquals( FieldTypes.SERVER_FILE_EXPLORER.getType() ) ) {
                if ( !elementField.isVariableMode() ) {
                    selection = getServerFile( elementField );
                } else {
                    final String strValue = elementField.getValue().toString();
                    if ( strValue.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !strValue.startsWith(
                            ConstantsString.SYS_CMD_INDICATION ) ) {
                        cmd = strValue;
                        final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( cmd ) );
                        if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
                            final List< String > variablesIncludingDotResolved = new ArrayList<>();
                            for ( String splitters : variablesIncludingDot ) {
                                variablesIncludingDotResolved.add( replaceAfterDotVariablesForSupportOfJsonExtract( splitters ) );
                            }
                            computeGlobalCmdForMultipleDotKeys( new Notification(), variablesIncludingDotResolved, variablesIncludingDot );
                        }
                        final List< String > variables = getAllSimpleVariables( cmd );
                        if ( CollectionUtils.isNotEmpty( variables ) ) {
                            computeGlobalCmd( notif, variables );
                        }
                        selection = cmd;
                    } else {
                        selection = strValue;
                    }
                }
            }
        }
        return selection;
    }

    /**
     * Date compared.
     *
     * @param futureCalander
     *         the future calander
     * @param currentCalander
     *         the current calander
     *
     * @return true, if successful
     */
    private boolean isSameDate( Calendar futureCalander, Calendar currentCalander ) {
        return ( futureCalander.get( Calendar.YEAR ) == currentCalander.get( Calendar.YEAR ) ) && ( futureCalander.get( Calendar.MONTH )
                == currentCalander.get( Calendar.MONTH ) ) && ( futureCalander.get( Calendar.DAY_OF_WEEK ) == currentCalander.get(
                Calendar.DAY_OF_WEEK ) ) && ( futureCalander.get( Calendar.HOUR ) == currentCalander.get( Calendar.HOUR ) ) && (
                futureCalander.get( Calendar.MINUTE ) == currentCalander.get( Calendar.MINUTE ) ) && ( futureCalander.get( Calendar.SECOND )
                == currentCalander.get( Calendar.SECOND ) );
    }

    /**
     * Lock file.
     *
     * @param fileName
     *         the file name
     * @param lockerFlag
     *         the locker flag
     * @param kind
     *         the kind
     * @param watchEvent
     *         the watch event
     */
    private void lockFile( String fileName, boolean lockerFlag, Kind< ? > kind, final WatchEvent< ? > watchEvent ) {
        if ( OVERFLOW != kind && ENTRY_CREATE == kind ) {
            final Path newPath = ( ( WatchEvent< Path > ) watchEvent ).context();
            log.info( MessagesUtil.getMessage( WFEMessages.NEW_FILE_CREATED, newPath ) );
            wfLogger.info( MessagesUtil.getMessage( WFEMessages.NEW_FILE_CREATED, newPath ) );
            if ( newPath.toString().equals( fileName ) ) {
                if ( lockerFlag ) {
                    fileLocking( new File( newPath.toString() ) );
                }
                waitChecker = false;
            }
        }
    }

    /**
     * Watch directory path.
     *
     * @param path
     *         the path
     * @param fileName
     *         the file name
     * @param lockerFlag
     *         the locker flag
     *
     * @return the notification
     */
    private Notification waitDirectoryWatcherForSpecificFile( Path path, String fileName, boolean lockerFlag ) {
        final Notification notif = new Notification();
        try {
            final Boolean isFolder = ( Boolean ) Files.getAttribute( path, DIRECTORY_ATTRIBUTE, NOFOLLOW_LINKS );
            if ( Boolean.FALSE.equals( isFolder ) ) {
                log.error( PATH + path + IS_NOT_A_FOLDER );
                wfLogger.error( PATH + path + IS_NOT_A_FOLDER );
                notif.addError( new Error( PATH + path + IS_NOT_A_FOLDER ) );
            }
        } catch ( final IOException ioe ) {
            log.error( ioe.getMessage(), ioe );
            notif.addError( new Error( PATH + path + ISSUE_WITH_PATH ) );
        }
        log.info( MessagesUtil.getMessage( WFEMessages.WATCHING_PATH, path ) );
        wfLogger.info( MessagesUtil.getMessage( WFEMessages.WATCHING_PATH, path ) );

        final FileSystem fs = path.getFileSystem();
        try ( WatchService service = fs.newWatchService() ) {
            path.register( service, ENTRY_CREATE );
            watchDirectory( fileName, lockerFlag, service );
        } catch ( final IOException ioe ) {
            log.error( ioe.getMessage(), ioe );
            notif.addError( new Error( PATH + path + ISSUE_WITH_PATH ) );
        } catch ( final InterruptedException ie ) {
            log.error( ie.getMessage(), ie );
            notif.addError( new Error( PATH + path + ISSUE_WITH_PATH ) );
            Thread.currentThread().interrupt();
        }
        return notif;
    }

    /**
     * Wait element process for due date.
     *
     * @param dateInString
     *         the date in string
     *
     * @return the notification
     */
    private Notification waitElementProcessForDueDate( String dateInString ) {
        log.info( MessagesUtil.getMessage( WFEMessages.ELEMENT_WAIT_STARTED, dateInString ) );
        wfLogger.info( MessagesUtil.getMessage( WFEMessages.ELEMENT_WAIT_STARTED, dateInString ) );
        final Notification notification = new Notification();
        long date;
        try {

            SimpleDateFormat dateFor = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm", Locale.getDefault() );
            Date dateMain = dateFor.parse( dateInString );
            log.info( MessagesUtil.getMessage( WFEMessages.ELEMENT_WAIT_STARTED, dateMain.toString() ) );
            // to millisec
            date = dateMain.getTime();
        } catch ( final ParseException e ) {
            log.error( e.getMessage(), e );
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.PLEASE_PROVIDE_IN_MILISECONDS ) ) );
            return notification;
        }

        final Date futureDate = DateUtils.convertFromMilliSecondToSpecifiedFormat( date, DATE_FORMATE );
        if ( futureDate == null ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.PLEASE_PROVIDE_CORRECT_DATE, DATE_FORMATE ) ) );
            return notification;
        }
        if ( futureDate.before( new Date() ) ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.WRONG_DATE_SELECTED_FOR_WAIT_ELEMENT ) ) );
            waitChecker = false;
            return notification;
        }
        final Runnable task = () -> {
            final Calendar futureCalander = Calendar.getInstance();
            futureCalander.setTime( futureDate );
            while ( waitChecker ) {

                final Calendar currentCalander = Calendar.getInstance();
                currentCalander.setTime( new Date() );
                final boolean sameDay = isSameDate( futureCalander, currentCalander );
                if ( sameDay ) {
                    waitChecker = false;
                }
                try {
                    Thread.sleep( PROCESS_SLEEP_DURATION );
                } catch ( final InterruptedException e ) {
                    log.error( e.getLocalizedMessage(), e );
                    waitChecker = false;
                    notification.addError( new Error( ELE_WAIT_EXEC_SUSPENDED + dateInString ) );
                    Thread.currentThread().interrupt();
                }
            }
        };
        try {
            final Thread thread = new Thread( task );
            thread.start();
            thread.join();
        } catch ( final InterruptedException e ) {
            log.error( e.getMessage(), e );
            notification.addError( new Error( ELE_WAIT_EXEC_SUSPENDED + dateInString ) );
            Thread.currentThread().interrupt();
        }
        return notification;
    }

    /**
     * Wait timer process in seconds.
     *
     * @param sec
     *         the sec
     *
     * @return the notification
     */
    private Notification waitTimerProcessInSeconds( long sec ) {
        log.info( MessagesUtil.getMessage( WFEMessages.ELEMENT_WAIT_STARTED, sec, " sec." ) );
        wfLogger.info( MessagesUtil.getMessage( WFEMessages.ELEMENT_WAIT_STARTED, sec, " sec." ) );
        final Notification notification = new Notification();
        final Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep( sec );
            } catch ( final InterruptedException e ) {
                log.error( e.getMessage(), e );
                wfLogger.error( MessagesUtil.getMessage( WFEMessages.ELE_WAIT_EXEC_SUSPENDED_FOR_SEC, sec ) );
                notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELE_WAIT_EXEC_SUSPENDED_FOR_SEC, sec ) ) );
                Thread.currentThread().interrupt();
            }
        };
        try {
            final Thread thread = new Thread( task );
            thread.start();
            thread.join();
        } catch ( final InterruptedException e ) {
            log.error( e.getMessage(), e );
            wfLogger.error( MessagesUtil.getMessage( WFEMessages.ELE_WAIT_EXEC_SUSPENDED_FOR_SEC, sec ) );
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELE_WAIT_EXEC_SUSPENDED_FOR_SEC, sec ) ) );
            Thread.currentThread().interrupt();
        }
        return notification;
    }

    /**
     * Watch directory.
     *
     * @param fileName
     *         the file name
     * @param lockerFlag
     *         the locker flag
     * @param service
     *         the service
     *
     * @throws InterruptedException
     *         the interrupted exception
     */
    private void watchDirectory( String fileName, boolean lockerFlag, WatchService service ) throws InterruptedException {
        WatchKey key;
        while ( waitChecker ) {
            key = service.take();
            Kind< ? > kind;

            for ( final WatchEvent< ? > watchEvent : key.pollEvents() ) {
                kind = watchEvent.kind();
                lockFile( fileName, lockerFlag, kind, watchEvent );
            }
            if ( !key.reset() ) {
                break; // loop
            }
        }
    }

    /**
     * Write out put file.
     *
     * @param elementOutput
     *         the element output
     * @param exitCode
     *         the exit code
     */
    private void writeOutPutFile( final ElementOutputWait elementOutput, String exitCode ) {
        addExistingOutputFileToWorkflowOutput();

        elementOutput.setExitCode( exitCode );
        workflowOutput.putIfAbsent( element.getName(), elementOutput );
        final String json = JsonUtils.objectToJson( workflowOutput );

        final File file = jobImpl.getElementOutput();

        try ( FileWriter fileWriter = new FileWriter( file, false ) ) {
            fileWriter.write( json );
            fileWriter.flush();
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

}
