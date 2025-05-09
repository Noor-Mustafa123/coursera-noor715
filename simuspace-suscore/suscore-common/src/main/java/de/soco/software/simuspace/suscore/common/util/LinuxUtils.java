package de.soco.software.simuspace.suscore.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLinuxFileActions;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLinuxScriptOperations;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.ExitCodesAndSignals;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.CommandBuilder;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * This util used to run workflow , run Files Utill , get server Dirs.
 *
 * @author Noman Arshad
 * @since 2.0
 */
@Log4j2
public class LinuxUtils {

    /**
     * The Constant JAVA_PATH.
     */
    public static final String JAVA_PATH = PropertiesManager.getJavaRunTimePath();

    /**
     * The constant ENGINE_FILE_SCRIPT.
     */
    public static final String ENGINE_FILE_SCRIPT = "suscore-simflow-wfengine.sh";

    // ************************COPY************************ */

    /**
     * Instantiates a new Linux utils.
     */
    private LinuxUtils() {

    }

    /**
     * Copy file from src path to dest path.
     *
     * @param userUID
     *         the user UID
     * @param srcFile
     *         the src file
     * @param destFile
     *         the dest file
     *
     * @return the command result
     */
    public static ProcessResult copyFileFromSrcPathToDestPath( String userUID, String srcFile, String destFile ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] command = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() ).srcPath( srcFile )
                .destPath( destFile ).action( ConstantsLinuxFileActions.COPY_FILE_BY_SRC_DEST_ADRESS ).build().buildCommand();

        ProcessResult copyFileResult = runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );

        if ( copyFileResult.getErrorStreamString() != null && !copyFileResult.getErrorStreamString().isEmpty() ) {
            log.error( MessageBundleFactory.getMessage( Messages.FAILED_TO_COPY_FILE_TO_FROM.getKey(), destFile, srcFile ) );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_COPY_FILE_TO_FROM.getKey(), destFile, srcFile )
                    + copyFileResult.getErrorStreamString() );
        }

        return copyFileResult;
    }

    /**
     * Copy dir from src path to dest path.
     *
     * @param userUID
     *         the user UID
     * @param srcDir
     *         the src dir
     * @param destDir
     *         the dest dir
     *
     * @return the command result
     */
    public static ProcessResult copyDirFromSrcPathToDestPath( String userUID, String srcDir, String destDir ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] command = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() ).srcPath( srcDir )
                .destPath( destDir ).action( ConstantsLinuxFileActions.COPY_DIR_BY_SRC_DEST_ADRESS ).build().buildCommand();

        ProcessResult result = runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );

        if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
            log.error( MessageBundleFactory.getMessage( Messages.FAILED_TO_COPY_DIRECTORY_TO_FROM.getKey(), destDir, srcDir ) );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_COPY_DIRECTORY_TO_FROM.getKey(), destDir, srcDir )
                    + result.getErrorStreamString() );
        }

        return result;
    }

    /**
     * Copy dir from src path to dest path with impersonation.
     *
     * @param userUID
     *         the user UID
     * @param srcFile
     *         the src file
     * @param destFile
     *         the dest file
     *
     * @return the command result
     */
    public static ProcessResult copyFileFromSrcPathToDestPathWithImpersonation( String userUID, String srcFile, String destFile ) {
        String[] command = addSudoCredential();
        addCommonCommandProperties( true, command );
        String[] finalCommand = CommandBuilder.builder().isImpersonated( true ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() ).srcPath( srcFile )
                .destPath( destFile ).action( ConstantsLinuxFileActions.COPY_FILE_BY_SRC_DEST_ADRESS ).build().buildCommand();

        return runScript( true, log.isDebugEnabled(), null, finalCommand );
    }

    // ************************CREATE************************ */

    /**
     * Creates the directory.
     *
     * @param userUID
     *         the user UID
     * @param createDirPath
     *         the create dir path
     *
     * @return the command result
     */
    public static ProcessResult createDirectory( String userUID, String createDirPath ) {
        boolean impersonated = PropertiesManager.isImpersonated();
        String[] command = impersonated ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonated, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonated ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() )
                .srcPath( createDirPath ).action( ConstantsLinuxFileActions.CREATE_DIRECTORY ).build().buildCommand();

        ProcessResult result = runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );

        if ( result.getExitValue() != ExitCodesAndSignals.SUCCESS.getExitCode() ) {
            String message = MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_DIRECTORY_AT.getKey(), createDirPath );
            SusException e = new SusException( message );
            log.error( message, e );
            throw e;
        }

        return result;
    }

    /**
     * Creates the file.
     *
     * @param userUID
     *         the user UID
     * @param createFilePath
     *         the create file path
     *
     * @return the command result
     */
    public static ProcessResult createFile( String userUID, String createFilePath ) {
        boolean impersonated = PropertiesManager.isImpersonated();
        String[] command = impersonated ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonated, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonated ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() )
                .serverDirOrFilePath( createFilePath ).action( ConstantsLinuxFileActions.CREATE_FILE ).build().buildCommand();

        ProcessResult result = runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );

        if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
            String errorMessage = MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_FILE_AT.getKey(), createFilePath );
            log.error( errorMessage );
            throw new SusException( errorMessage + result.getErrorStreamString() );
        }

        return result;
    }

    /**
     * Creates the file with impersonation.
     *
     * @param userUID
     *         the user UID
     * @param createFilePath
     *         the create file path
     *
     * @return the command result
     */
    public static ProcessResult createFileWithImpersonation( String userUID, String createFilePath ) {
        String[] command = addSudoCredential();
        addCommonCommandProperties( true, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( true ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() )
                .srcPath( createFilePath ).action( ConstantsLinuxFileActions.CREATE_FILE ).build().buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );
    }

    // ************************CB2************************ */

    /**
     * Tree CB 2 with impersonation.
     *
     * @param uid
     *         the uid
     * @param paramFile
     *         the param file
     *
     * @return the command result
     */
    public static ProcessResult treeCB2WithImpersonation( String uid, String paramFile ) {
        String[] command = addSudoCredential();
        addCommonCommandProperties( true, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( true ).command( command )
                .operation( ConstantsLinuxScriptOperations.CB2 ).enginePath( PropertiesManager.getEnginePath() ).userUID( uid )
                .cb2ParamFile( paramFile ).build().buildCommand();

        return runScript( true, log.isDebugEnabled(), null, finalCommand );
    }

    /**
     * Login logout CB 2 with impersonation.
     *
     * @param userId
     *         the user id
     * @param paramFilePath
     *         the param file path
     *
     * @return the command result
     */
    public static ProcessResult loginLogoutCB2WithImpersonation( String userId, String paramFilePath ) {
        String[] command = addSudoCredential();
        addCommonCommandProperties( true, command );
        String[] finalCommand = CommandBuilder.builder().isImpersonated( true ).command( command )
                .operation( ConstantsLinuxScriptOperations.CB2 ).enginePath( PropertiesManager.getEnginePath() ).userUID( userId )
                .cb2ParamFile( paramFilePath ).build().buildCommand();

        return runScript( true, log.isDebugEnabled(), PropertiesManager.cb2RequestProcessTimeout(), finalCommand );
    }

    // ************************DELETE************************ */

    /**
     * Delete file or dir by path.
     *
     * @param userUID
     *         the user UID
     * @param dirOrFilePath
     *         the dir or file path
     *
     * @return the command result
     */
    public static ProcessResult deleteFileOrDirByPath( String userUID, String dirOrFilePath ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] command = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() )
                .srcPath( dirOrFilePath ).action( ConstantsLinuxFileActions.DELETE_FILE ).build().buildCommand();

        ProcessResult result = runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );

        if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
            String errorMessage = MessageBundleFactory.getMessage( Messages.FAILED_TO_DELETE_FILE_DIRECTORY_AT.getKey(), dirOrFilePath );
            log.error( errorMessage );
            throw new SusException( errorMessage + result.getErrorStreamString() );
        }

        return result;
    }

    // ************************ZIP************************ */

    /**
     * Extract zip file with impersonation.
     *
     * @param userUID
     *         the user UID
     * @param createFilePath
     *         the create file path
     *
     * @return the command result
     */
    public static ProcessResult extractZipFileWithImpersonation( String userUID, String zipFilePath ) {
        String[] command = addSudoCredential();
        addCommonCommandProperties( true, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( true ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() )
                .srcPath( zipFilePath ).action( ConstantsLinuxFileActions.EXTRACT_ZIP_FILE ).build().buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );
    }

    /**
     * Extract dummy zip file with impersonation.
     *
     * @param userUID
     *         the user UID
     * @param createFilePath
     *         the create file path
     *
     * @return the command result
     */
    public static ProcessResult extractDummyZipFileWithImpersonation( String userUID, String createFilePath ) {
        String[] command = addSudoCredential();
        addCommonCommandProperties( true, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( true ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() )
                .srcPath( createFilePath ).action( ConstantsLinuxFileActions.EXTRACT_ZIP_DUMMY_FILE ).build().buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );
    }

    /**
     * Extract zip file.
     *
     * @param userUID
     *         the user UID
     * @param zipFilePath
     *         the zip file path
     *
     * @return the command result
     */
    public static ProcessResult extractZipFile( String userUID, String zipFilePath ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] command = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() )
                .serverDirOrFilePath( zipFilePath ).action( ConstantsLinuxFileActions.EXTRACT_ZIP_FILE ).build().buildCommand();

        ProcessResult result = runScript( true, log.isDebugEnabled(), null, finalCommand );

        if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
            String errorMessage = MessageBundleFactory.getMessage( Messages.FAILED_TO_EXTRACT_ZIP_FILE.getKey(), zipFilePath );
            log.error( errorMessage );
            throw new SusException( errorMessage + result.getErrorStreamString() );
        }

        return result;
    }

    /**
     * Extract dummy zip file.
     *
     * @param userUID
     *         the user UID
     * @param dummyZipFilePath
     *         the dummy zip file path
     *
     * @return the command result
     */
    public static ProcessResult extractDummyZipFile( String userUID, String dummyZipFilePath ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] command = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() )
                .srcPath( dummyZipFilePath ).action( ConstantsLinuxFileActions.EXTRACT_ZIP_DUMMY_FILE ).build().buildCommand();

        ProcessResult result = runScript( true, log.isDebugEnabled(), null, finalCommand );

        if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
            String errorMessage = MessageBundleFactory.getMessage( Messages.FAILED_TO_EXTRACT_DUMMY_ZIP_FILE.getKey(), dummyZipFilePath );
            log.error( errorMessage );
            throw new SusException( errorMessage + result.getErrorStreamString() );
        }

        return result;
    }

    // ************************WRITE************************ */

    /**
     * Write file.
     *
     * @param userUID
     *         the user UID
     * @param fileWritePath
     *         the file write path
     * @param fileWriteData
     *         the file write data
     *
     * @return the command result
     */
    public static ProcessResult writeFile( String userUID, String fileWritePath, String fileWriteData ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] command = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() )
                .srcPath( fileWritePath ).destPath( fileWriteData ).action( ConstantsLinuxFileActions.WRITE_FILE ).build().buildCommand();

        ProcessResult result = runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );

        if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
            String errorMessage = MessageBundleFactory.getMessage( Messages.FAILED_TO_WRITE_FILE_AT.getKey(), fileWritePath );
            log.error( errorMessage );
            throw new SusException( errorMessage + result.getErrorStreamString() );
        }

        return result;
    }

    /**
     * Write file by other file.
     *
     * @param userUid
     *         the user uid
     * @param inputFilePath
     *         the input file path
     * @param outputFilePath
     *         the output file path
     *
     * @return the command result
     */
    public static ProcessResult writeFileByOtherFile( String userUid, String inputFilePath, String outputFilePath ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] command = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( command ).userUID( userUid )
                .operation( ConstantsLinuxScriptOperations.FILES_UTILS ).enginePath( PropertiesManager.getEnginePath() )
                .srcPath( inputFilePath ).destPath( outputFilePath ).action( ConstantsLinuxFileActions.WRITE_FILE_BY_OTHER_FILE ).build()
                .buildCommand();

        ProcessResult result = runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );

        if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
            String errorMessage = MessageBundleFactory.getMessage( Messages.FAILED_TO_WRITE_FILE_FROM_TO.getKey(), inputFilePath,
                    outputFilePath );
            log.error( errorMessage );
            throw new SusException( errorMessage + result.getErrorStreamString() );
        }

        return result;
    }

    // ********************* RUN ************************/

    /**
     * Run command process result.
     *
     * @param command
     *         the command
     * @param karafLogging
     *         the karaf logging
     * @param timeoutInSeconds
     *         the timeout in seconds
     *
     * @return the process result
     */
    public static ProcessResult runCommand( String[] command, boolean karafLogging, Integer timeoutInSeconds ) {
        String operation = ConstantsLinuxScriptOperations.RUN_COMMAND;
        ProcessResult processResult = new ProcessResult();
        final ProcessBuilder probuilder = new ProcessBuilder( command );
        try {
            final Process process = probuilder.start();
            if ( karafLogging ) {
                log.info( String.format( "***Started %s with command :%s", operation, probuilder.command() ) );
            }

            final StringBuilder builder = new StringBuilder();
            String line;

            try ( InputStreamReader is = new InputStreamReader(
                    process.getInputStream() ); final BufferedReader reader = new BufferedReader( is ) ) {
                while ( ( line = reader.readLine() ) != null ) {
                    builder.append( line );
                    builder.append( System.lineSeparator() );
                }
            }
            final String result = builder.toString();

            String error = null;
            try {
                error = LinuxUtils.stdError( process );
            } catch ( Exception e ) {
                log.error( "LinuxUtils.Stderror : " + e.getMessage(), e );
            }
            processResult.setErrorStreamString( error );
            processResult.setCommand( command );
            processResult.setOutputString( result );
            processResult.setOperation( operation );
            processResult.setPid( String.valueOf( process.pid() ) );

            try {
                if ( timeoutInSeconds != null ) {
                    if ( !process.waitFor( timeoutInSeconds, TimeUnit.SECONDS ) ) {
                        process.destroyForcibly();
                    }
                } else {
                    process.waitFor();
                }
            } catch ( InterruptedException e ) {
                // this exception occurs when client abort the job and thread is wait for
                // process to execute n return
                // instead its forcefully killed by stopJob
                log.warn( MessageBundleFactory.getDefaultMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
                log.info( "waitFor released for :" + probuilder.command() );
                Thread.currentThread().interrupt();
            }

            processResult.setExitValue( process.exitValue() );

            process.getInputStream().close();
            process.getOutputStream().close();
            process.getErrorStream().close();
            if ( karafLogging ) {
                processResult.logProcessExitStatus();
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, LinuxUtils.class );
        }

        return processResult;
    }

    /**
     * Run command.
     *
     * @param command
     *         the command
     * @param karafLogging
     *         the karaf logging
     * @param timeoutInSeconds
     *         the timeout in seconds
     *
     * @return the command result
     */
    public static ProcessResult runPreparedCommand( String[] command, boolean karafLogging, Integer timeoutInSeconds ) {
        ProcessResult processResult = new ProcessResult();
        final ProcessBuilder probuilder = new ProcessBuilder( command );
        try {
            final Process process = probuilder.start();
            if ( karafLogging ) {
                log.info( String.format( "***Started %s with command :%s", CommandBuilder.getOperation( command ), probuilder.command() ) );
            }
            final StringBuilder builder = new StringBuilder();
            String line;
            try ( InputStreamReader is = new InputStreamReader(
                    process.getInputStream() ); final BufferedReader reader = new BufferedReader( is ) ) {
                while ( ( line = reader.readLine() ) != null ) {
                    builder.append( line );
                    builder.append( System.lineSeparator() );
                }
            }
            final String result = builder.toString();
            String error = null;
            try {
                error = LinuxUtils.stdError( process );
            } catch ( Exception e ) {
                log.error( "LinuxUtils.Stderror : " + e.getMessage(), e );
            }
            processResult.setErrorStreamString( error );
            processResult.setCommand( command );
            processResult.setOutputString( result );
            processResult.setOperation( CommandBuilder.getOperation( command ) );
            processResult.setPid( String.valueOf( process.pid() ) );
            try {
                if ( timeoutInSeconds != null ) {
                    if ( !process.waitFor( timeoutInSeconds, TimeUnit.SECONDS ) ) {
                        process.destroyForcibly();
                    }
                } else {
                    process.waitFor();
                }
            } catch ( InterruptedException e ) {
                // this exception occurs when client abort the job and thread is wait for
                // process to execute n return
                // instead its forcefully killed by stopJob
                log.warn( MessageBundleFactory.getDefaultMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
                log.info( "waitFor released for :" + probuilder.command() );
                Thread.currentThread().interrupt();
            }
            processResult.setExitValue( process.exitValue() );
            process.getInputStream().close();
            process.getOutputStream().close();
            process.getErrorStream().close();
            if ( karafLogging ) {
                processResult.logProcessExitStatus();
            } else if ( processResult.getExitValue() != ExitCodesAndSignals.SUCCESS.getExitCode() ) {
                log.error( MessageBundleFactory.getDefaultMessage( Messages.PROCESS_EXIT_CODE.getKey(), processResult.getOperation(),
                        processResult.getPid(), ExitCodesAndSignals.getByExitCode( processResult.getExitValue() ) ) );
                log.error( processResult.getErrorStreamString() );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, LinuxUtils.class );
        }

        return processResult;
    }

    /**
     * Run command.
     *
     * @param userUID
     *         the user UID
     * @param exeCommand
     *         the exe command
     * @param timeoutInSeconds
     *         the timeout in seconds
     *
     * @return the command result
     */
    public static ProcessResult runUserCommand( String userUID, String exeCommand, Integer timeoutInSeconds ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] baseCommand = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, baseCommand );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( baseCommand ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.RUN_COMMAND ).javaPath( ConstantsString.EMPTY_STRING )
                .executionCommand( exeCommand ).build().buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, timeoutInSeconds, finalCommand );
    }

    /**
     * Run system command.
     *
     * @param executionCommand
     *         the execution command
     * @param timeoutInSeconds
     *         the timeout in seconds
     *
     * @return the process result
     */
    public static ProcessResult runSystemCommand( String executionCommand, Integer timeoutInSeconds ) {
        String[] command = CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( false, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( false ).command( command )
                .operation( ConstantsLinuxScriptOperations.RUN_COMMAND ).javaPath( ConstantsString.EMPTY_STRING )
                .executionCommand( executionCommand ).build().buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, timeoutInSeconds, finalCommand );
    }

    /**
     * Pause command.
     *
     * @param userUID
     *         the user UID
     * @param pid
     *         the pid
     * @param timeoutInSeconds
     *         the timeout in seconds
     * @param forChildProcesses
     *         the for child processes
     *
     * @return the process result
     */
    public static ProcessResult pauseCommand( String userUID, String pid, Integer timeoutInSeconds, boolean forChildProcesses ) {
        return killCommand( userUID, ExitCodesAndSignals.SIGSTOP.getFlag(), pid, timeoutInSeconds, forChildProcesses );
    }

    /**
     * Resume command.
     *
     * @param userUID
     *         the user UID
     * @param pid
     *         the pid
     * @param timeoutInSeconds
     *         the timeout in seconds
     * @param forChildProcesses
     *         the for child processes
     *
     * @return the process result
     */
    public static ProcessResult resumeCommand( String userUID, String pid, Integer timeoutInSeconds, boolean forChildProcesses ) {
        return killCommand( userUID, ExitCodesAndSignals.SIGCONT.getFlag(), pid, timeoutInSeconds, forChildProcesses );
    }

    /**
     * Kill command.
     *
     * @param userUID
     *         the user UID
     * @param pid
     *         the pid
     * @param timeoutInSeconds
     *         the timeout in seconds
     * @param forChildProcesses
     *         the for child processes
     *
     * @return the process result
     */
    public static ProcessResult killCommand( String userUID, String pid, Integer timeoutInSeconds, boolean forChildProcesses ) {
        return killCommand( userUID, ExitCodesAndSignals.SIGKILL.getFlag(), pid, timeoutInSeconds, forChildProcesses );
    }

    /**
     * Kill command process result.
     *
     * @param userUID
     *         the user uid
     * @param flag
     *         the flag
     * @param pid
     *         the pid
     * @param timeoutInSeconds
     *         the timeout in seconds
     * @param forChildProcesses
     *         the for child processes
     *
     * @return the process result
     */
    public static ProcessResult killCommand( String userUID, String flag, String pid, Integer timeoutInSeconds,
            boolean forChildProcesses ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] baseCommand = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, baseCommand );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( baseCommand ).userUID( userUID )
                .operation( forChildProcesses ? ConstantsLinuxScriptOperations.PKILL_COMMAND : ConstantsLinuxScriptOperations.KILL_COMMAND )
                .processId( pid ).processFlag( flag ).build().buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, timeoutInSeconds, finalCommand );
    }

    /**
     * Run command.
     *
     * @param command
     *         the command
     *
     * @return the process
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static Process runCommand( String[] command ) throws IOException {
        final ProcessBuilder probuilder = new ProcessBuilder( command );
        probuilder.redirectErrorStream( true );
        return probuilder.start();
    }

    // ************************NIX************************ */

    /**
     * Gets the nix home.
     *
     * @param userUID
     *         the user UID
     *
     * @return the nix home
     */
    public static String getNixHome( String userUID ) {
        String[] envCmd = runUserCommand( userUID, "env", null ).getCommand();
        String userHome = null;
        try {
            Process process = runCommand( envCmd );
            final StringBuilder builder = new StringBuilder();
            String line;
            try ( InputStreamReader isr = new InputStreamReader(
                    process.getInputStream() ); final BufferedReader reader = new BufferedReader( isr ) ) {
                while ( ( line = reader.readLine() ) != null ) {
                    if ( line.startsWith( "HOME" ) ) {
                        builder.append( line );
                    }
                }
            }
            userHome = builder.toString().split( ConstantsString.EQUALS_OPERATOR )[ 1 ];
            process.waitFor();

            // missing these was causing the mass amounts of open 'files'
            process.getInputStream().close();
            process.getOutputStream().close();
            process.getErrorStream().close();
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, LinuxUtils.class );
        } catch ( InterruptedException e ) {
            log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
        }
        return userHome;
    }

    /**
     * Linux execute nix host.
     *
     * @param userUID
     *         the user UID
     * @param workflowJsonPath
     *         the workflow json path
     * @param executionHostAddress
     *         the execution host address
     * @param enginePathJava
     *         the engine path java
     *
     * @return the command result
     */
    public static ProcessResult linuxExecuteNixHost( String userUID, String workflowJsonPath, String executionHostAddress,
            String enginePathJava ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] baseCommand = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, baseCommand );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( baseCommand ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.RUN_WORKFLOW_ON_HOST ).enginePath( PropertiesManager.getEnginePath() )
                .workFlowPath( workflowJsonPath ).executionHostAddress( executionHostAddress ).javaPath( enginePathJava ).build()
                .buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );
    }

    /**
     * Gets the load nix host.
     *
     * @param userUid
     *         the user uid
     * @param executionHostAddress
     *         the execution host address
     *
     * @return the load nix host
     */
    public static ProcessResult getLoadNixHost( String userUid, String executionHostAddress ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] baseCommand = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, baseCommand );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( baseCommand ).userUID( userUid )
                .operation( ConstantsLinuxScriptOperations.CHECK_LOAD ).executionHostAddress( executionHostAddress ).build().buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );
    }

    // *************************************************** */

    /**
     * Gets the SGE list by user.
     *
     * @param userUID
     *         the user UID
     * @param commandToRun
     *         the command to run
     *
     * @return the SGE list by user
     */
    public static ProcessResult getSGEListByUser( String userUID, String commandToRun ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] baseCommand = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, baseCommand );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( baseCommand ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.RUN_COMMAND ).executionCommand( commandToRun ).build().buildCommand();

        ProcessResult result = runScript( true, true, null, finalCommand );

        if ( result.getErrorStreamString() != null && !result.getErrorStreamString().isEmpty() ) {
            log.error( result.getErrorStreamString() );
            throw new SusException( result.getErrorStreamString() );
        }

        return result;
    }

    /**
     * Gets the linux server files command.
     *
     * @param serverDirOrFilePath
     *         the server dir or file path
     * @param action
     *         the action
     * @param tempPath
     *         the temp path
     * @param userUID
     *         the user UID
     *
     * @return the linux server files command
     */
    public static ProcessResult getLinuxServerFilesCommand( String serverDirOrFilePath, String action, String tempPath, String userUID ) {
        boolean impersonated = PropertiesManager.isImpersonated();
        String[] command = impersonated ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonated, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonated ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.SERVER_DIRECTORY ).enginePath( PropertiesManager.getEnginePath() )
                .serverDirOrFilePath( serverDirOrFilePath ).action( action ).tempPath( tempPath ).build().buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );
    }

    /**
     * Linux execute engine job.
     *
     * @param workflowJsonPath
     *         the workflow json path
     * @param userUID
     *         the user UID
     *
     * @return the command result
     */
    public static ProcessResult linuxExecuteEngineJob( String workflowJsonPath, String userUID ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] baseCommand = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, baseCommand );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( baseCommand ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.RUN_WORKFLOW ).enginePath( PropertiesManager.getEnginePath() )
                .workFlowPath( workflowJsonPath ).build().buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );
    }

    /**
     * Gets the windows command for workflow.
     *
     * @param workflowJsonPath
     *         the workflow json path
     *
     * @return the windows command for workflow
     */
    public static String[] getWindowsCommandForWorkflow( String workflowJsonPath ) {

        String[] command = new String[ 6 ];
        command[ 0 ] = JAVA_PATH;
        command[ 1 ] = "-jar";
        command[ 2 ] = "-Dkaraf.base=\"" + PropertiesManager.getKarafPath() + "\"";
        command[ 3 ] = "\"" + PropertiesManager.getScriptsPath() + File.separator + "suscore-simflow-wfengine.jar\"";
        command[ 4 ] = "-wf";
        command[ 5 ] = "\"" + workflowJsonPath + "\"";
        return command;
    }

    /**
     * Call parser python script.
     *
     * @param userUID
     *         the user UID
     * @param commandToRun
     *         the command to run
     *
     * @return the command result
     */
    public static ProcessResult callParserPythonScript( String userUID, String commandToRun ) {
        boolean impersonation = PropertiesManager.isImpersonated();
        String[] command = impersonation ? addSudoCredential() : CommandBuilder.prepareCommand( 14 );
        addCommonCommandProperties( impersonation, command );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( impersonation ).command( command ).userUID( userUID )
                .operation( ConstantsLinuxScriptOperations.RUN_COMMAND ).executionCommand( commandToRun ).build().buildCommand();

        return runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );
    }

    /**
     * Stderror.
     *
     * @param process
     *         the process
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static String stdError( Process process ) throws IOException {
        String line;
        StringBuilder error = null;

        try ( InputStream stdError = process.getErrorStream(); InputStreamReader is = new InputStreamReader(
                stdError ); final BufferedReader brCleanUpError = new BufferedReader( is ) ) {
            while ( ( line = brCleanUpError.readLine() ) != null ) {
                if ( error == null ) {
                    error = new StringBuilder();
                }
                error.append( line ).append( ConstantsString.NEW_LINE );
            }

            if ( error != null ) {
                return error.toString();
            }
        }

        return null;
    }

    // ************************RUN SCRIPT************************ */

    /**
     * Run script process result.
     *
     * @param runOrReturnCommand
     *         the run or return command
     * @param karafLogging
     *         the karaf logging
     * @param timeoutInSeconds
     *         the timeout in seconds
     * @param command
     *         the command
     *
     * @return the process result
     */
    public static ProcessResult runScript( boolean runOrReturnCommand, boolean karafLogging, Integer timeoutInSeconds, String[] command ) {
        ProcessResult processResult = new ProcessResult();
        if ( runOrReturnCommand ) {
            processResult = runPreparedCommand( command, karafLogging, timeoutInSeconds );
        } else {
            processResult.setCommand( command );
        }

        return processResult;
    }

    /**
     * Add sudo credential string [ ].
     *
     * @return the string [ ]
     */
    public static String[] addSudoCredential() {
        String[] sudoCommand = PropertiesManager.getSudoCommandConfigurations();
        int sudoCommandLength = sudoCommand.length;
        String[] command = CommandBuilder.prepareCommand( sudoCommandLength + 15 );
        System.arraycopy( sudoCommand, ConstantsInteger.INTEGER_VALUE_ZERO, command, ConstantsInteger.INTEGER_VALUE_ZERO,
                sudoCommandLength );
        return command;
    }

    /**
     * Add common command properties.
     *
     * @param impersonation
     *         the impersonation
     * @param command
     *         the command
     */
    public static void addCommonCommandProperties( boolean impersonation, String[] command ) {
        CommandBuilder.builder().isImpersonated( impersonation ).command( command )
                .engineScriptFile( PropertiesManager.getEnginePath() + File.separator + ENGINE_FILE_SCRIPT ).javaPath( JAVA_PATH )
                .karafPath( PropertiesManager.getKarafPath() ).pythonPath( PropertiesManager.getPythonExecutionPathOnServer() ).build()
                .buildCommand();
    }

    public static boolean checkUserImpersonation( String userUid ) {
        return runUserCommand( userUid, "echo a", 2 ).getExitValue() == ExitCodesAndSignals.SUCCESS.getExitCode();
    }

}