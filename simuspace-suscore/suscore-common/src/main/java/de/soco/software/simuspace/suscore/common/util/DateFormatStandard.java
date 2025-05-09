package de.soco.software.simuspace.suscore.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.IntStream;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;

/**
 * Helper class for handling a most common subset of ISO 8601 strings (in the following format: "2008-03-01T13:00:00+01:00"). It supports
 * parsing the "Z" timezone, but many other less-used features are missing.
 */
public final class DateFormatStandard {

    /**
     * The Constant ZOON_SUFIX.
     */
    private static final String ZOON_SUFIX = "Z";

    /**
     * The Constant TIME_SUFIX.
     */
    private static final String TIME_SUFIX = "+00:00";

    /**
     * The Constant INVALID_LENGTH_OF_DATE_STRING.
     */
    private static final String INVALID_LENGTH_OF_DATE_STRING = "Invalid date string length for ISO 8601 format.";

    /**
     * The Constant ISO_8601_DATE_FORMAT.
     */
    private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    private static final String ISO_8601_DATE_FORMAT_WITHOUT_ZONE = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Instantiates a new date format standard.
     */
    private DateFormatStandard() {
        super();
    }

    /**
     * Format.
     *
     * @param calendar
     *         the calendar
     *
     * @return the string
     */
    public static String format( final Calendar calendar ) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat( ISO_8601_DATE_FORMAT ).format( date );
        return formatted.substring( 0, 22 ) + ConstantsString.COLON + formatted.substring( 22 );
    }

    /**
     * Get current date and time formatted as ISO 8601 string.
     *
     * @return the string
     */
    public static String now() {
        return format( GregorianCalendar.getInstance() );
    }

    /**
     * Format.
     *
     * @param date
     *         the date
     *
     * @return the string
     */
    public static String format( Date date ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        return format( cal );
    }

    /**
     * Transform ISO 8601 string to Calendar.
     *
     * @param iso8601string
     *         the iso 8601 string
     *
     * @return the calendar
     *
     * @throws ParseException
     *         the parse exception
     */
    public static Calendar toCalendar( final String iso8601string ) throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace( ZOON_SUFIX, TIME_SUFIX );
        try {
            s = s.substring( 0, 22 ) + s.substring( 23 ); // to get rid of the ":"
        } catch ( IndexOutOfBoundsException e ) {
            ExceptionLogger.logException( e, DateFormatStandard.class );
            throw new ParseException( INVALID_LENGTH_OF_DATE_STRING, ConstantsInteger.INTEGER_VALUE_ZERO );
        }
        Date date = new SimpleDateFormat( ISO_8601_DATE_FORMAT ).parse( s );
        calendar.setTime( date );
        return calendar;
    }

    /**
     * Transform ISO 8601 string to Calendar.
     *
     * @param iso8601string
     *         the iso 8601 string
     *
     * @return the calendar
     *
     * @throws ParseException
     *         the parse exception
     */
    public static Calendar toCalendarWithoutZone( final String iso8601string ) throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace( ZOON_SUFIX, TIME_SUFIX );
        try {
            s = s.substring( 0, 22 ) + s.substring( 23 ); // to get rid of the ":"
        } catch ( IndexOutOfBoundsException e ) {
            ExceptionLogger.logException( e, DateFormatStandard.class );
            throw new ParseException( INVALID_LENGTH_OF_DATE_STRING, ConstantsInteger.INTEGER_VALUE_ZERO );
        }
        Date date = new SimpleDateFormat( ISO_8601_DATE_FORMAT_WITHOUT_ZONE ).parse( s );
        calendar.setTime( date );
        return calendar;
    }

    /**
     * Transform ISO 8601 string to Calendar.
     *
     * @param iso8601string
     *         the iso 8601 string
     *
     * @return the date
     */
    public static Date toDate( final String iso8601string ) {

        try {
            return toCalendar( iso8601string ).getTime();
        } catch ( ParseException e ) {
            ExceptionLogger.logException( e, DateFormatStandard.class );
        }
        return toDate( now() );
    }

    /**
     * Transform ISO 8601 string to Calendar.
     *
     * @param iso8601string
     *         the iso 8601 string
     *
     * @return the date
     *
     * @throws ParseException
     */
    public static long toMilliseconds( final String iso8601string ) throws ParseException {

        try {
            return toCalendarWithoutZone( iso8601string ).getTimeInMillis();
        } catch ( ParseException e ) {
            ExceptionLogger.logException( e, DateFormatStandard.class );
            throw e;
        }
    }

    public static boolean checkIsPriorToNow( final String iso8601string ) {

        try {
            return toCalendar( iso8601string ).getTimeInMillis() > Calendar.getInstance().getTimeInMillis();
        } catch ( ParseException e ) {
            ExceptionLogger.logException( e, DateFormatStandard.class );
        }

        return false;
    }

    public static List< LocalDate > getAllDatesBetweenRanges( LocalDate startDate, LocalDate endDate ) {
        long numOfDaysBetween = ChronoUnit.DAYS.between( startDate, endDate );
        if ( numOfDaysBetween == 0 ) {
            return new ArrayList<>();
        }
        return IntStream.iterate( 1, i -> i + 1 ).limit( numOfDaysBetween - 1 ).mapToObj( i -> startDate.plusDays( i ) ).toList();
    }

}
