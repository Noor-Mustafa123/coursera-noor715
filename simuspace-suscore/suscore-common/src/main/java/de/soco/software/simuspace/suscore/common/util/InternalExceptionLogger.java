/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import lombok.extern.log4j.Log4j2;

/**
 * This class is to log internal exceptions
 *
 * @author Shan
 * @since 1.0
 */
@Log4j2
public class InternalExceptionLogger {

    // ------------------Constants Start

    private static final String EXCEPTION_END_MESSAGE = "\n\t\t\t\t     ************** Exception End ****************";

    private static final String EXCEPTION_GENERATED_MESSAGE = "\n\t\t\t\t     ************ Exception Generated **************";

    private static final String NEW_LINE = "\n ";

    private static final String NEW_LINES_AND_TABS = " \n\n\t\t\t\t\t >>>> ";

    private static final String NEW_LINES_TABS_CLASS = "\n\n\t\t\t\t\t Class \t\t::\t";

    private static final String NEW_LINES_TABS_EXCEPTION = "\n\t\t\t\t\t Exception \t::\t";

    private static final String NEW_LINES_TABS_METHOD = "\n\t\t\t\t\t Method \t::\t ";

    private static final String NEW_LINES_TABS_SYSTEM_CLASS = "\n\n\t\t\t\t\t System Class \t::\t";

    private static final String TABS_AND_HASHES = "\n\t\t\t\t ####################################################### ";

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
     * Gets the stack trace.
     *
     * @param aThrowable
     *         the a throwable
     *
     * @return the stack trace
     */
    private static String getStackTrace( Throwable aThrowable ) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter( result );
        aThrowable.printStackTrace( printWriter );
        return result.toString();
    }

    /**
     * Generic exception log.
     *
     * @param e
     *         the e
     * @param clazz
     *         the clazz
     *
     * @author jawad.hameed
     */
    public static void logException( Exception e, Class< ? > clazz ) {
        logException( e.getMessage(), e.getStackTrace()[ 0 ].getClassName(), e.getStackTrace()[ 0 ].getMethodName(), e, clazz );
    }

    /**
     * It logs the exception.
     *
     * @param exceptionDescription
     *         the exception description
     * @param className
     *         the class name
     * @param methodName
     *         the method name
     * @param exception
     *         the ex
     * @param clazz
     *         the clazz
     */
    public static void logException( String exceptionDescription, String className, String methodName, Exception exception,
            Class< ? > clazz ) {
        final StringBuilder exceptionGen = new StringBuilder();
        final StringBuilder exceptionEnd = new StringBuilder();
        final StringBuilder exceptionMsg = new StringBuilder();

        buildMessage( exceptionDescription, className, methodName, exceptionGen, exceptionEnd, exceptionMsg, clazz.getName() );
        if ( log.isDebugEnabled() ) {
            log.debug( "log call stack trace", new Exception( "log call stack trace" ) );
        }
        if ( exception != null ) {
            exceptionMsg.append( exception.getClass().getName() );
            log.error( exceptionGen.toString() + exceptionMsg.toString() + getStackTrace( exception ) + exceptionEnd.toString() );
        } else {
            log.error( exceptionGen.toString() + exceptionMsg.toString() + exceptionEnd.toString() );
        }
    }

    /**
     * Instantiates a new exception log.
     */
    private InternalExceptionLogger() {
        //
    }

}
