package de.soco.software.simuspace.suscore.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;

/**
 * A class that would contain the utility methods for loggers and appenders.
 *
 * @author Zeeshan jamal
 */
public class LoggerUtils {

    /**
     * The Constant LOG_FILE_EXENSION.
     */
    private static final String LOG_FILE_EXENSION = ".log";

    /**
     * The constant FILENAME_PERIOD_INDEX for index of Period in file name.
     */
    private static final String FILENAME_PERIOD_INDEX = ".";

    /**
     * The Constant FORMAT.
     */
    public static final String FORMAT = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

    /**
     * The Constant HEADER_FOOTER_FORMAT.
     */
    public static final String HEADER_FOOTER_FORMAT = "\t\t %3$s %n";

    /**
     * Private constructor to hide implicit one.
     */
    private LoggerUtils() {
        super();
    }

    public static String addFileAppenderToLogger( String fileName, String directory, String loggerName ) throws IOException {

        // get the global logger to configure it
        Logger logger = Logger.getLogger( loggerName );

        logger.setLevel( Level.ALL );

        String filePath = createLogFileName( fileName, directory );

        final FileHandler handler = new FileHandler( filePath );
        handler.setFormatter( new SimpleSusFormatter( HEADER_FOOTER_FORMAT ) );

        logger.setUseParentHandlers( false );
        logger.addHandler( handler );
        return filePath;
    }

    /**
     * Sets the formatter to logger.
     *
     * @param loggerName
     *         the logger name
     * @param formatter
     *         the formatter
     */
    public static void setFormatterToLogger( String loggerName, String formatter ) {
        final java.util.logging.Logger globalLogger = Logger.getLogger( loggerName );
        final Handler[] handlers = globalLogger.getHandlers();
        for ( final Handler handler : handlers ) {
            handler.setFormatter( new SimpleSusFormatter( formatter ) );
        }
    }

    static class SimpleSusFormatter extends SimpleFormatter {

        private String formatter;

        public SimpleSusFormatter( String formatter ) {
            this.formatter = formatter;
        }

        @Override
        public synchronized String format( java.util.logging.LogRecord lr ) {
            return String.format( formatter, new Date( lr.getMillis() ), lr.getLevel().getLocalizedName(), lr.getMessage() );
        }

    }

    /**
     * It returns new log file name with current time.
     *
     * @param logFileName
     *         the log file name
     * @param directory
     *         the directory in which log file will go
     *
     * @return the log file name
     */
    private static String createLogFileName( String logFileName, String directory ) {

        final int dotIndex = LOG_FILE_EXENSION.indexOf( FILENAME_PERIOD_INDEX );
        final String newFileName = LOG_FILE_EXENSION.substring( ConstantsInteger.INTEGER_VALUE_ZERO, dotIndex ) + logFileName
                + ConstantsString.DEFAULT_FILE_NAME_SEPERATOR + DateUtils.getPrintableDateTime() + LOG_FILE_EXENSION.substring( dotIndex );
        return directory + File.separator + newFileName;
    }

}
