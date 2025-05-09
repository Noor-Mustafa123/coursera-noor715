package de.soco.software.simuspace.suscore.common.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.ExitCodesAndSignals;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.OSValidator;

/**
 * The Class ProcessResult.
 */
@Log4j2
@JsonIgnoreProperties( ignoreUnknown = true )
@Getter
@Setter
public class ProcessResult {

    /**
     * The Constant UNIX_SIGNAL_EXIT_CODES_START.
     */
    private static final int UNIX_SIGNAL_EXIT_CODES_START = 128;

    /**
     * The Constant UNIX_SIGNAL_EXIT_CODES_END.
     */
    private static final int UNIX_SIGNAL_EXIT_CODES_END = 128 + 32;

    /**
     * The command.
     */
    private String[] command;

    /**
     * The outputString.
     */
    private String outputString;

    /**
     * The errorStreamString.
     */
    private String errorStreamString;

    /**
     * The exitValue.
     */
    private int exitValue;

    /**
     * The operation.
     */
    private String operation;

    /**
     * The pid.
     */
    private String pid;

    /**
     * Sets the command.
     *
     * @param command
     *         the new command
     */
    public void setCommand( String[] command ) {
        this.command = command;
    }

    /**
     * Sets command.
     *
     * @param command
     *         the command
     */
    public void setCommand( List< String > command ) {
        this.command = command.toArray( new String[ 0 ] );
    }

    /**
     * Log process exit status.
     */
    public void logProcessExitStatus() {
        ExitCodesAndSignals exitCode = ExitCodesAndSignals.getByExitCode( getExitValue() );
        if ( exitCode == ExitCodesAndSignals.SUCCESS ) {
            logProcessSuccess( exitCode );
        } else if ( exitDueToUnixSignal() ) {
            logExitOnUnixSignal( exitCode );
        } else if ( exitCode == ExitCodesAndSignals.UNKNOWN ) {
            logProcessUnkownError( exitCode );
            if ( log.isDebugEnabled() ) {
                log.debug( "actual exit code = {}", getExitValue() );
            }
        } else {
            logProcessError( exitCode );
        }
        if ( getCommand() != null ) {
            log.info( MessageBundleFactory.getDefaultMessage( Messages.PROCESS_COMMAND.getKey(), getOperation(), getPid(),
                    ConstantsString.DOUBLE_QUOTE_STRING + String.join( "\" \"", getCommand() ) + ConstantsString.DOUBLE_QUOTE_STRING ) );
        }
    }

    /**
     * Log process unkown error.
     *
     * @param exitCode
     *         the exit code
     */
    private void logProcessUnkownError( ExitCodesAndSignals exitCode ) {
        log.warn( "Actual exit code: " + getExitValue() );
        logProcessError( exitCode );
    }

    /**
     * Log process success.
     *
     * @param exitCode
     *         the exit code
     */
    private void logProcessSuccess( ExitCodesAndSignals exitCode ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.PROCESS_EXIT_CODE.getKey(), getOperation(), getPid(), exitCode ) );
        if ( StringUtils.isNotBlank( getOutputString() ) ) {
            log.info( MessageBundleFactory.getDefaultMessage( Messages.PROCESS_OUTPUT.getKey(), getOperation(), getPid(),
                    getOutputString() ) );
        }
        if ( StringUtils.isNotBlank( getErrorStreamString() ) ) {
            log.warn( MessageBundleFactory.getDefaultMessage( Messages.PROCESS_WARNING.getKey(), getOperation(), getPid(),
                    getErrorStreamString() ) );
        }
    }

    /**
     * Exit due to unix signal.
     *
     * @return true, if successful
     */
    public boolean exitDueToUnixSignal() {
        return OSValidator.isUnix() && UNIX_SIGNAL_EXIT_CODES_START < getExitValue() && getExitValue() < UNIX_SIGNAL_EXIT_CODES_END;
    }

    /**
     * Log exit on unix signal.
     *
     * @param exitCode
     *         the exit code
     */
    private void logExitOnUnixSignal( ExitCodesAndSignals exitCode ) {
        log.warn( MessageBundleFactory.getDefaultMessage( Messages.PROCESS_SIGNAL.getKey(), getOperation(), getPid(),
                ( exitCode.getExitCode() - 128 ), exitCode.getId() ) );
        logProcessError( exitCode );
    }

    /**
     * Log process error.
     *
     * @param exitCode
     *         the exit code
     */
    private void logProcessError( ExitCodesAndSignals exitCode ) {
        log.error( MessageBundleFactory.getDefaultMessage( Messages.PROCESS_EXIT_CODE.getKey(), getOperation(), getPid(), exitCode ) );
        if ( StringUtils.isNotBlank( getOutputString() ) ) {
            log.info( MessageBundleFactory.getDefaultMessage( Messages.PROCESS_OUTPUT.getKey(), getOperation(), getPid(),
                    getOutputString() ) );
        }
        if ( StringUtils.isNotBlank( getErrorStreamString() ) ) {
            log.error( MessageBundleFactory.getDefaultMessage( Messages.PROCESS_ERROR.getKey(), getOperation(), getPid(),
                    getErrorStreamString() ) );
        }
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "CommandResult [outputString=" + outputString + ", errorStreamString=" + errorStreamString + ", exitValue=" + exitValue
                + ", operation=" + operation + "]";
    }

}
