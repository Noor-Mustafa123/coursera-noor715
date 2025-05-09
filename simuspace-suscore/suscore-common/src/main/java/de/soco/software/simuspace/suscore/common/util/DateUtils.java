
/* ******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 * ******************************************************************************/

package de.soco.software.simuspace.suscore.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDate;
import de.soco.software.simuspace.suscore.common.enums.DateFormatEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.suscore.jsonschema.model.HistoryMap;

/**
 * Its a utility class which will contain all the functions related to Date and Time. It will be used application wide.
 *
 * @author Ahsan Khan
 */
public class DateUtils {

    public static final String DATE_FORMAT_YYYY_MM_DD_HHMM = "yyyy_MM_dd_HHmmss";

    public static final String DATE_PATTERN_FROM_DATE_PICKER = "yyyy-MM-dd'T'hh:mm";

    public static final String DATE_PATTERN_DD_MM_YYYY = "dd-MM-yyyy";

    public static final String DATE_PATTERN_FOR_TIMESTAMP = "yyyyMMdd_HHmmss_SSS";

    public static final String DATE_PATTERN_FOR_TIMESTAMP_WITH_COMMA = "M/d/yyyy, h:mm:ss a";

    /**
     * Instantiates a new date utils.
     */
    private DateUtils() {
        // private
    }

    /**
     * From string.
     *
     * @param stringDate
     *         the string date
     * @param format
     *         the format
     *
     * @return the date
     */
    public static Date fromString( final String stringDate, final String format ) {
        Date date = null;
        final DateFormat formatter = new SimpleDateFormat( format );
        try {
            date = formatter.parse( stringDate );
        } catch ( final ParseException e ) {
            ExceptionLogger.logException( e, DateUtils.class );
        }
        return date;
    }

    /**
     * From epoch string date.
     *
     * @param epochMillis
     *         the epoch millis
     *
     * @return the date
     */
    public static Date fromEpochString( String epochMillis ) {
        try {
            return new Date( Long.parseLong( epochMillis ) );
        } catch ( NumberFormatException e ) {
            ExceptionLogger.logException( e, DateUtils.class );
            return null;
        }
    }

    /**
     * From string date.
     *
     * @param stringDate
     *         the string date
     *
     * @return the date
     */
    public static Date fromString( final String stringDate ) {
        return DateUtils.fromString( stringDate, DateFormatEnum.matchFormat( stringDate ).getFormat() );
    }

    /**
     * Get current date without time.
     *
     * @return the date
     */
    public static Date getCurrentDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        return calendar.getTime();
    }

    /**
     * Get yesterday date without time.
     *
     * @return the date
     */
    public static Date getYesterdayDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.add( Calendar.DATE, -1 );

        return calendar.getTime();
    }

    /**
     * Compute to date and fromdate.
     *
     * @param historyMap
     *         the history map
     *
     * @return the history map
     */
    public static HistoryMap computeToDateAndFromdate( HistoryMap historyMap ) {
        SimpleDateFormat formater = new SimpleDateFormat( "EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH );
        Date fromDate = null;
        Date toDate = null;
        try {
            SimpleDateFormat simpleFormater = new SimpleDateFormat( "yyyy-MM-dd" );
            if ( !historyMap.getDateRange().contains( "today" ) && historyMap.getDateRange().contains( "to" ) ) {
                // normal date calculation
                String[] arrOfStr = historyMap.getDateRange().split( " to ", 2 );
                String date1 = arrOfStr[ 0 ];
                String date2 = arrOfStr[ 1 ];

                if ( simpleFormater.parse( date1 ).compareTo( simpleFormater.parse( date2 ) ) == 0 ) {

                    Date date = new SimpleDateFormat( "yyyy-MM-dd" ).parse( date2 );
                    fromDate = getStartOfTheDayTimeStampByDate( date );
                    toDate = getEndOfTheDayTimeStampByDate( date );
                } else {
                    // change formate for ToDate
                    Calendar cal = Calendar.getInstance();
                    cal.setTime( simpleFormater.parse( date1 ) );
                    fromDate = formater.parse( cal.getTime().toString() );

                    // change formate for FromDate
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime( simpleFormater.parse( date2 ) );
                    toDate = formater.parse( cal2.getTime().toString() );
                }

            } else if ( historyMap.getDateRange().contains( "Today" ) ) {
                Date date = new Date();
                fromDate = getStartOfTheDayTimeStampByDate( date );
                toDate = getEndOfTheDayTimeStampByDate( date );
            } else if ( historyMap.getDateRange().contains( "Yesterday" ) ) {
                Date yesterDay = getYesterdayTimeStamp();

                fromDate = getStartOfTheDayTimeStampByDate( yesterDay );
                toDate = getEndOfTheDayTimeStampByDate( yesterDay );
            } else if ( historyMap.getDateRange().contains( "Last Week" ) ) {

                fromDate = getLastWeekStartOfDayTimeStamp();
                toDate = getLastWeekEndOfDayTimeStamp();

            } else if ( historyMap.getDateRange().contains( "This Week" ) ) {

                fromDate = getThisWeekStartOfDayTimeStamp();
                toDate = getEndOfTheDayTimeStampByDate( new Date() );
            } else if ( historyMap.getDateRange().contains( "Last Month" ) ) {
                fromDate = getLastMonthStartOfDayTimeStamp();
                toDate = getLastMonthEndOfDayTimeStamp();

            } else if ( historyMap.getDateRange().contains( "This Month" ) ) {

                fromDate = getThisMonthStartOfDayTimeStamp();
                toDate = getEndOfTheDayTimeStampByDate( new Date() );
            } else if ( historyMap.getDateRange().contains( "Last Year" ) ) {

                fromDate = getLastYearStartOfDayTimeStamp();
                toDate = getLastYearEndOfDayTimeStamp();

            } else if ( historyMap.getDateRange().contains( "This Year" ) ) {

                fromDate = getThisYearStartOfDayTimeStamp();
                toDate = getEndOfTheDayTimeStampByDate( new Date() );
            }

            historyMap.setToDate( formater.parse( toDate.toString() ) );
            historyMap.setFromDate( formater.parse( fromDate.toString() ) );
        } catch ( ParseException e ) {
            ExceptionLogger.logException( e, DateUtils.class );
            throw new SusException( "Monitor History Date Parsing Failed " + e.getMessage() );
        }
        return historyMap;
    }

    /**
     * Gets the this year start of day time stamp.
     *
     * @return the this year start of day time stamp
     */
    private static Date getThisYearStartOfDayTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        cal.set( Calendar.DATE, 1 );
        cal.set( Calendar.DAY_OF_YEAR, 1 );
        cal.clear( Calendar.MINUTE );
        cal.clear( Calendar.SECOND );
        cal.clear( Calendar.MILLISECOND );
        return cal.getTime();
    }

    /**
     * Gets the last year start of day time stamp.
     *
     * @return the last year start of day time stamp
     */
    private static Date getLastYearStartOfDayTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        cal.set( Calendar.DATE, 1 );
        cal.set( Calendar.YEAR, ( cal.get( Calendar.YEAR ) - 1 ) );
        cal.set( Calendar.DAY_OF_YEAR, 1 );
        cal.clear( Calendar.MINUTE );
        cal.clear( Calendar.SECOND );
        cal.clear( Calendar.MILLISECOND );
        return cal.getTime();
    }

    /**
     * Gets the last year end of day time stamp.
     *
     * @return the last year end of day time stamp
     */
    private static Date getLastYearEndOfDayTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        cal.set( Calendar.DATE, 1 );
        cal.set( Calendar.YEAR, ( cal.get( Calendar.YEAR ) - 1 ) );
        cal.set( Calendar.MONTH, 11 );
        cal.set( Calendar.DAY_OF_MONTH, 31 );
        cal.clear( Calendar.MINUTE );
        cal.clear( Calendar.SECOND );
        cal.clear( Calendar.MILLISECOND );
        return cal.getTime();
    }

    /**
     * Gets the this month start of day time stamp.
     *
     * @return the this month start of day time stamp
     */
    private static Date getThisMonthStartOfDayTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        cal.set( Calendar.DATE, 1 );
        cal.clear( Calendar.MINUTE );
        cal.clear( Calendar.SECOND );
        cal.clear( Calendar.MILLISECOND );
        return cal.getTime();
    }

    /**
     * Gets the last month end of day time stamp.
     *
     * @return the last month end of day time stamp
     */
    private static Date getLastMonthEndOfDayTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        cal.set( Calendar.DATE, 1 );
        cal.add( Calendar.DAY_OF_MONTH, -1 );
        cal.clear( Calendar.MINUTE );
        cal.clear( Calendar.SECOND );
        cal.clear( Calendar.MILLISECOND );
        return cal.getTime();
    }

    /**
     * Gets the last month start of day time stamp.
     *
     * @return the last month start of day time stamp
     */
    private static Date getLastMonthStartOfDayTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        cal.set( Calendar.DATE, 1 );
        cal.add( Calendar.DAY_OF_MONTH, -1 );
        cal.set( Calendar.DATE, 1 );
        cal.set( Calendar.HOUR_OF_DAY, 0 );
        cal.clear( Calendar.MINUTE );
        cal.clear( Calendar.SECOND );
        cal.clear( Calendar.MILLISECOND );
        return cal.getTime();
    }

    /**
     * Gets the this week start of day time stamp.
     *
     * @return the this week start of day time stamp
     */
    private static Date getThisWeekStartOfDayTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        cal.set( Calendar.HOUR_OF_DAY, 0 );
        cal.clear( Calendar.MINUTE );
        cal.clear( Calendar.SECOND );
        cal.clear( Calendar.MILLISECOND );
        int i = cal.get( Calendar.DAY_OF_WEEK ) - cal.getFirstDayOfWeek();
        cal.add( Calendar.DATE, -i );
        return cal.getTime();
    }

    /**
     * Gets the last week start of day time stamp.
     *
     * @return the last week start of day time stamp
     */
    private static Date getLastWeekStartOfDayTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        cal.set( Calendar.HOUR_OF_DAY, 0 );
        cal.clear( Calendar.MINUTE );
        cal.clear( Calendar.SECOND );
        cal.clear( Calendar.MILLISECOND );
        int i = cal.get( Calendar.DAY_OF_WEEK ) - cal.getFirstDayOfWeek();
        cal.add( Calendar.DATE, -i - 7 );
        return cal.getTime();
    }

    /**
     * Gets the last week end of day time stamp.
     *
     * @return the last week end of day time stamp
     */
    private static Date getLastWeekEndOfDayTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        int i = cal.get( Calendar.DAY_OF_WEEK ) - cal.getFirstDayOfWeek();
        cal.add( Calendar.DATE, -i - 7 );
        cal.add( Calendar.DATE, 6 );
        cal.set( Calendar.HOUR, 23 );
        cal.set( Calendar.MINUTE, 59 );
        return cal.getTime();
    }

    /**
     * Gets the yesterday time stamp.
     *
     * @return the yesterday time stamp
     */
    private static Date getYesterdayTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        cal.add( Calendar.DATE, -1 );
        return cal.getTime();
    }

    /**
     * Gets the start of the day time stamp by date.
     *
     * @param date
     *         the date
     *
     * @return the start of the day time stamp by date
     */
    private static Date getStartOfTheDayTimeStampByDate( Date date ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        cal.set( Calendar.HOUR_OF_DAY, 0 );
        cal.clear( Calendar.MINUTE );
        cal.clear( Calendar.SECOND );
        cal.clear( Calendar.MILLISECOND );
        return cal.getTime();
    }

    /**
     * Gets the end of the day time stamp by date.
     *
     * @param date
     *         the date
     *
     * @return the end of the day time stamp by date
     */
    private static Date getEndOfTheDayTimeStampByDate( Date date ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        cal.set( Calendar.HOUR_OF_DAY, 23 );
        cal.set( Calendar.MINUTE, 59 );
        return cal.getTime();
    }

    /**
     * Get this week start date.
     *
     * @return the date
     */
    public static Date getThisWeekStartDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.set( Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() );

        return calendar.getTime();
    }

    /**
     * Get this week end date.
     *
     * @return the date
     */
    public static Date getThisWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.set( Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() );
        calendar.add( Calendar.DATE, 6 );

        return calendar.getTime();
    }

    /**
     * Get last week start date.
     *
     * @return the date
     */
    public static Date getLastWeekStartDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.set( Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() );
        calendar.add( Calendar.DATE, -7 );

        return calendar.getTime();
    }

    /**
     * Get last week end date.
     *
     * @return the date
     */
    public static Date getLastWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.set( Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() );
        calendar.add( Calendar.DATE, -1 );

        return calendar.getTime();
    }

    /**
     * Get this month start date.
     *
     * @return the date
     */
    public static Date getThisMonthStartDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.set( Calendar.DAY_OF_MONTH, 1 );

        return calendar.getTime();
    }

    /**
     * Get this month end date.
     *
     * @return the date
     */
    public static Date getThisMonthEndDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );

        return calendar.getTime();
    }

    /**
     * Get last month start date.
     *
     * @return the date
     */
    public static Date getLastMonthStartDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.add( Calendar.MONTH, -1 );
        calendar.set( Calendar.DAY_OF_MONTH, 1 );

        return calendar.getTime();
    }

    /**
     * Get last month end date.
     *
     * @return the date
     */
    public static Date getLastMonthEndDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.add( Calendar.MONTH, -1 );
        calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );

        return calendar.getTime();
    }

    /**
     * Get this year start date.
     *
     * @return the date
     */
    public static Date getThisYearStartDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.set( Calendar.DAY_OF_YEAR, 1 );

        return calendar.getTime();
    }

    /**
     * Get this year end date.
     *
     * @return the date
     */
    public static Date getThisYearEndDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.set( Calendar.DAY_OF_YEAR, calendar.getActualMaximum( Calendar.DAY_OF_YEAR ) );

        return calendar.getTime();
    }

    /**
     * Get last year start date.
     *
     * @return the date
     */
    public static Date getLastYearStartDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.add( Calendar.YEAR, -1 );
        calendar.set( Calendar.DAY_OF_YEAR, 1 );

        return calendar.getTime();
    }

    /**
     * Get last year end date.
     *
     * @return the date
     */
    public static Date getLastYearEndDate() {
        Calendar calendar = Calendar.getInstance();
        removeTimeFromDate( calendar );

        calendar.add( Calendar.YEAR, -1 );
        calendar.set( Calendar.DAY_OF_YEAR, calendar.getActualMaximum( Calendar.DAY_OF_YEAR ) );

        return calendar.getTime();
    }

    /**
     * Removes Time from date.
     */
    private static void removeTimeFromDate( Calendar calendar ) {
        calendar.set( Calendar.HOUR_OF_DAY, 0 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );
    }

    /**
     * From long string.
     *
     * @param dateInLong
     *         the date in long
     * @param format
     *         the format
     *
     * @return the date
     */
    public static Date convertFromMilliSecondToSpecifiedFormat( final long dateInLong, final String format ) {

        Date resultDate = null;
        try {
            final Date date = new Date( dateInLong );
            final String dateStr = changeFormatOfDateProvided( date, format );
            resultDate = fromString( dateStr, format );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, DateUtils.class );
            return resultDate;
        }
        return resultDate;
    }

    /**
     * Change format of date provided.
     *
     * @param srcDate
     *         the src date
     * @param format
     *         the format
     *
     * @return the string
     */
    public static String changeFormatOfDateProvided( Date srcDate, String format ) {
        String date = null;
        final DateFormat formatter = new SimpleDateFormat( format );
        date = formatter.format( srcDate );
        return date;
    }

    /**
     * Gets the current time stemp.
     *
     * @return the current time stemp
     */
    public static long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * Gets the printable date time.
     *
     * @return the printable date time
     */
    public static String getPrintableDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat( DATE_FORMAT_YYYY_MM_DD_HHMM );
        return sdf.format( new Date() );
    }

    /**
     * Gets a Date.
     *
     * @return the tomorrow Date
     */
    public static Date getTomorrowDate( Date date ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        cal.add( Calendar.DATE, 1 );
        return convertFromMilliSecondToSpecifiedFormat( cal.getTimeInMillis(), ConstantsDate.DATE_ONLY_FORMAT );
    }

    /**
     * Gets the max job running time.
     *
     * @param minute
     *         the minute
     *
     * @return the max job running time
     */
    public static Date getMaxJobRunningTime( int minute ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        cal.add( Calendar.MINUTE, minute );
        return convertFromMilliSecondToSpecifiedFormat( cal.getTimeInMillis(), ConstantsDate.DATE_TIME_FORMAT );
    }

    public static Long parseDateToEpoch( String date ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat( DATE_PATTERN_FROM_DATE_PICKER );
            Date dt = sdf.parse( date );
            long epoch = dt.getTime();
            return epoch / 1000;
        } catch ( ParseException e ) {
            ExceptionLogger.logException( e, DateUtils.class );
            throw new SusException( e );
        }
    }

    /**
     * Gets the time difference.
     *
     * @param firstDate
     *         the first date
     * @param secondDate
     *         the second date
     * @param timeUnit
     *         the time unit
     *
     * @return the time difference
     */
    public static Long getTimeDifferenceInMinutes( Date firstDate, Date secondDate ) {
        return TimeUnit.MINUTES.convert( Math.abs( secondDate.getTime() - firstDate.getTime() ), TimeUnit.MILLISECONDS );
    }

    /**
     * Convert iso 8601 to custom format.
     *
     * @param iso8601Date
     *         the iso 8601 date
     *
     * @return the date
     */
    public static Date convertIso8601ToCustomFormat( String iso8601Date ) {
        LocalDateTime dateTime = LocalDateTime.parse( iso8601Date, DateTimeFormatter.ISO_LOCAL_DATE_TIME );
        return Date.from( dateTime.atZone( ZoneId.systemDefault() ).toInstant() );
    }

    /**
     * Convert long date format to string.
     *
     * @param longDateFormat
     *         the long date format
     *
     * @return the string
     */
    public static String convertLongDateFormatToString( Date longDateFormat ) {
        LocalDateTime localDateTime = longDateFormat.toInstant().atZone( java.time.ZoneId.systemDefault() ).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( ConstantsDate.DATE_TIME_FORMAT );
        return localDateTime.format( formatter );
    }

    /**
     * Parses the date time string.
     *
     * @param dateString
     *         the date string
     *
     * @return the date
     */
    public static Date parseDateTimeString( String dateString ) {
        String pattern = "yyyy-MM-dd[ HH:mm[:ss]]";

        DateTimeFormatter fmt = new DateTimeFormatterBuilder().appendPattern( pattern )
                .parseDefaulting( java.time.temporal.ChronoField.HOUR_OF_DAY, 0 )
                .parseDefaulting( java.time.temporal.ChronoField.MINUTE_OF_HOUR, 0 )
                .parseDefaulting( java.time.temporal.ChronoField.SECOND_OF_MINUTE, 0 ).toFormatter();

        LocalDateTime dateTime = LocalDateTime.parse( dateString, fmt );
        return Date.from( dateTime.atZone( ZoneId.systemDefault() ).toInstant() );
    }

    /**
     * String to time unit time unit.
     *
     * @param timeUnitString
     *         the time unit string
     *
     * @return the time unit
     */
    public static TimeUnit stringToTimeUnit( String timeUnitString ) {
        if ( timeUnitString == null ) {
            return null;
        }
        return switch ( timeUnitString.toLowerCase().trim() ) {
            case "hrs", "hour", "hours", "h", "hourly" -> TimeUnit.HOURS;
            case "min", "minute", "minutes", "m" -> TimeUnit.MINUTES;
            case "sec", "second", "seconds", "s" -> TimeUnit.SECONDS;
            case "day", "days", "d", "daily" -> TimeUnit.DAYS;
            default -> null;
        };

    }

    /**
     * Gets n minutes later date.
     *
     * @param date
     *         the date
     * @param n
     *         the n
     *
     * @return the n minutes later date
     */
    public static Date getNMinutesLaterDate( Date date, int n ) {
        if ( date == null ) {
            throw new IllegalArgumentException( "The provided date cannot be null" );
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        calendar.add( Calendar.MINUTE, n ); // Add n hours to the date
        return calendar.getTime();
    }

    /**
     * Is date boolean.
     *
     * @param str
     *         the str
     *
     * @return the boolean
     */
    public static boolean isDate( String str ) {
        for ( DateFormatEnum format : DateFormatEnum.values() ) {
            if ( str.matches( format.getRegex() ) ) {
                return true;
            }
        }
        return false;
    }

}
