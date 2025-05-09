/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.util;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;

/**
 * This Class will be used as for the common template to through the log Errors. In log file. The benefit of using this common template is
 * to easy lookup in huge log file.
 *
 * @author Shan Arshad
 * @since 1.0
 */
@Log4j2
public class ExceptionLogger {

    // ------------------Constants Start

    private static final String EXCEPTION_END_MESSAGE = "\n\t\t\t\t     ************** Exception End ****************";

    private static final String EXCEPTION_GENERATED_MESSAGE = "\n\t\t\t\t     ************ Exception Generated **************";

    private static final String HASHES = "#######################################################################";

    private static final String NEW_LINE = "\n ";

    private static final String NEW_LINE_AND_TABS = "\n\t\t\t\t";

    private static final String NEW_LINES_AND_TABS = " \n\n\t\t\t\t\t >>>> ";

    private static final String NEW_LINES_TABS_CLASS = "\n\n\t\t\t\t\t Class \t\t::\t";

    private static final String NEW_LINES_TABS_EXCEPTION = "\n\t\t\t\t\t Exception \t::\t";

    private static final String NEW_LINES_TABS_METHOD = "\n\t\t\t\t\t Method \t::\t ";

    private static final String NEW_LINES_TABS_STACK_TRACE = "\n\t\t\t\tStackTrace :: \n\n";

    private static final String NEW_LINES_TABS_SYSTEM_CLASS = "\n\n\t\t\t\t\t System Class \t::\t";

    private static final String TABS_AND_HASHES = "\n\t\t\t\t ####################################################### ";

    private static final String USER_NAME_STRING = "userName";

    // ------------------Constants End

    /**
     * Builds the message.
     *
     * @param exceptionDescription
     *         the exception description
     * @param className
     *         the class name
     * @param methodName
     *         the method name
     * @param exceptionGen
     *         the exception gen
     * @param exceptionEnd
     *         the exception end
     * @param exceptionMsg
     *         the exception msg
     * @param classFromWhereExceptionGenerated
     *         the class from where exception generated
     */
    private static void buildMessage( String exceptionDescription, String className, String methodName, StringBuilder exceptionGen,
            StringBuilder exceptionEnd, StringBuilder exceptionMsg, String classFromWhereExceptionGenerated ) {

        exceptionGen.append( NEW_LINE ).append( TABS_AND_HASHES ).append( EXCEPTION_GENERATED_MESSAGE ).append( TABS_AND_HASHES );

        exceptionEnd.append( NEW_LINE ).append( TABS_AND_HASHES ).append( EXCEPTION_END_MESSAGE ).append( TABS_AND_HASHES );

        exceptionMsg.append( NEW_LINES_AND_TABS ).append( exceptionDescription ).append( NEW_LINES_TABS_SYSTEM_CLASS )
                .append( classFromWhereExceptionGenerated ).append( NEW_LINES_TABS_CLASS ).append( className )
                .append( NEW_LINES_TABS_METHOD ).append( methodName ).append( NEW_LINES_TABS_EXCEPTION );
    }

    /**
     * returns the exception stack trace as a string.
     *
     * @param exception
     *         the exception
     *
     * @return String
     */
    private static String getStackTraceAsString( Exception exception ) {

        final StackTraceElement[] trace = exception.getStackTrace();

        final StringBuilder exc = new StringBuilder();
        exc.append( NEW_LINE ).append( NEW_LINES_TABS_STACK_TRACE );
        for ( final StackTraceElement ste : trace ) {

            exc.append( NEW_LINE_AND_TABS ).append( ste.toString() );

        }
        return exc.toString();
    }

    /**
     * Generic exception logger.
     *
     * @param e
     *         the e
     * @param sourceClassOfException
     *         the class From Which Exception Is Thrown
     *
     * @author jawad.hameed
     */
    public static void logException( Exception e, Class< ? > sourceClassOfException ) {
        InternalExceptionLogger.logException( e, sourceClassOfException );
    }

    /**
     * Log exception.
     *
     * @param userName
     *         the user name
     * @param e
     *         the e
     * @param clazz
     *         the clazz
     */
    public static void logException( String userName, Exception e, Class< ? > clazz ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "log call stack trace", new Exception( "log call stack trace" ) );
        }
        UserException.logException( userName, e, clazz );
    }

    /**
     * It logs the exception.
     *
     * @param userName
     *         the username
     * @param exceptionDescription
     *         the exception description
     * @param className
     *         the class name
     * @param methodName
     *         the method name
     * @param ex
     *         the ex
     * @param clazz
     *         the clazz
     */
    public static void logException( String userName, String exceptionDescription, String className, String methodName, Exception ex,
            Class< ? > clazz ) {
        ThreadContext.put( USER_NAME_STRING, userName );
        final StringBuilder exceptionGen = new StringBuilder();
        final StringBuilder exceptionEnd = new StringBuilder();
        final StringBuilder exceptionMsg = new StringBuilder();

        buildMessage( exceptionDescription, className, methodName, exceptionGen, exceptionEnd, exceptionMsg, clazz.getName() );

        if ( log.isDebugEnabled() ) {
            log.debug( "log call stack trace", new Exception( "log call stack trace" ) );
        }
        if ( ex != null ) {
            exceptionMsg.append( ex.getClass().getName() );
            log.error( exceptionGen.toString() + exceptionMsg.toString() + getStackTraceAsString( ex ) + exceptionEnd.toString() );
        } else {
            log.error( exceptionGen.toString() + exceptionMsg.toString() + exceptionEnd.toString() );
        }
        ThreadContext.clearAll();
    }

    /**
     * Log message.
     *
     * @param message
     *         the message
     */
    public static void logMessage( final String message ) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( HASHES ).append( NEW_LINE_AND_TABS ).append( message ).append( NEW_LINE ).append( HASHES );
        log.info( stringBuilder.toString() );
    }

    /**
     * Log message.
     *
     * @param userName
     *         the user name
     * @param message
     *         the message
     */
    public static void logMessage( String userName, final String message ) {
        UserException.logMessage( userName, message );
    }

    /**
     * Instantiates a new exception logger.
     */
    private ExceptionLogger() {

    }

}