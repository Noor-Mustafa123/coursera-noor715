package de.soco.software.simuspace.suscore.common.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;

/**
 * Custom Logger class for modifying log messages
 *
 * @author Zeeshan jamal
 */
public class WfLogger {

    /**
     * Target Logger object for instantiating WfLogger
     */
    private final Logger logger;

    /**
     * Instantiates a new Wf logger.
     *
     * @param loggerClassName
     *         the logger class name
     */
    public WfLogger( String loggerClassName ) {
        logger = Logger.getLogger( loggerClassName );

    }

    /**
     * Logs the debug message.
     *
     * @param message
     *         the message
     */
    public void debug( String message ) {
        logger.log( Level.FINE, message );
    }

    /**
     * Logs the error message.
     *
     * @param message
     *         the message
     */
    public void error( String message ) {
        logger.log( Level.SEVERE, "############### ERROR START ###############" );
        logger.log( Level.SEVERE, message );
        logger.log( Level.SEVERE, "############### ERROR END ###############" );

    }

    /**
     * Log a message object with the <code>EXCEPTION</code> level including the stack trace of the {@link Throwable} <code>t</code> passed
     * as parameter.
     */
    public void error( String message, Throwable t ) {
        logger.log( Level.SEVERE, "############### EXCEPTION START ###############" );
        logger.log( Level.SEVERE, message, t );
        logger.log( Level.SEVERE, "############### EXCEPTION END ###############" );
    }

    /**
     * Logs the info message.
     *
     * @param message
     *         the message
     */
    public void info( String message ) {
        logger.log( Level.INFO, message );
    }

    /**
     * * Log a message object with the <code>SUCCESS</code> Level
     *
     * @param message
     *         the message
     */
    public void success( String message ) {
        logger.logp( Level.INFO, WfLogger.class.getCanonicalName(), ConstantsString.EMPTY_STRING, message );
    }

    /**
     * Logs the warning message.
     *
     * @param message
     *         the message
     */
    public void warn( String message ) {
        logger.log( Level.WARNING, "############### WARNING START ###############" );
        logger.log( Level.WARNING, message );
        logger.log( Level.WARNING, "############### WARNING END ###############" );
    }

    /**
     * Logs the fatal/critical message.
     *
     * @param message
     *         the message
     */
    public void fatal( String message ) {
        logger.log( Level.WARNING, "############### FATAL START ###############" );
        logger.log( Level.WARNING, message );
        logger.log( Level.WARNING, "############### FATAL END ###############" );
    }

}