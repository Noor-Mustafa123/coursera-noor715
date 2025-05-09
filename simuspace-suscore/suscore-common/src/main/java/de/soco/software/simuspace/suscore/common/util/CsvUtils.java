package de.soco.software.simuspace.suscore.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;

/**
 * The Class CSV.
 */
@Log4j2
public class CsvUtils {

    /**
     * The Constant NUMMARK.
     */
    private static final int NUMMARK = 10;

    /**
     * The Constant DQUOTE.
     */
    private static final char DQUOTE = '"';

    /**
     * The Constant CRETURN.
     */
    private static final char CRETURN = '\r';

    /**
     * The Constant LFEED.
     */
    private static final char LFEED = '\n';

    /**
     * Should we ignore multiple carriage-return/newline characters at the end of the record?.
     */
    private boolean stripMultipleNewlines;

    /**
     * What should be used as the separator character?.
     */
    private char separator;

    /**
     * The fields.
     */
    private ArrayList< String > fields;

    /**
     * The eof seen.
     */
    private boolean eofSeen;

    /**
     * The in.
     */
    private Reader in;

    /**
     * Strip bom.
     *
     * @param in
     *         the in
     *
     * @return the reader
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws UnsupportedEncodingException
     *         the unsupported encoding exception
     */
    public static Reader stripBom( InputStream in ) throws java.io.IOException {
        Reader reader = null;
        try ( PushbackInputStream pin = new PushbackInputStream( in, 3 ) ) {
            byte[] b = new byte[ 3 ];
            int len = pin.read( b, 0, b.length );
            if ( ( b[ 0 ] & 0xFF ) == 0xEF && len == 3 ) {
                if ( ( b[ 1 ] & 0xFF ) == 0xBB && ( b[ 2 ] & 0xFF ) == 0xBF ) {
                    return new InputStreamReader( pin, "UTF-8" );
                } else {
                    pin.unread( b, 0, len );
                }
            } else if ( len >= 2 ) {
                if ( ( b[ 0 ] & 0xFF ) == 0xFE && ( b[ 1 ] & 0xFF ) == 0xFF ) {
                    return new InputStreamReader( pin, "UTF-16BE" );
                } else if ( ( b[ 0 ] & 0xFF ) == 0xFF && ( b[ 1 ] & 0xFF ) == 0xFE ) {
                    return new InputStreamReader( pin, "UTF-16LE" );
                } else {
                    pin.unread( b, 0, len );
                }
            } else if ( len > 0 ) {
                pin.unread( b, 0, len );
            }
            reader = new InputStreamReader( pin, "UTF-8" );
        }
        return reader;
    }

    /**
     * Instantiates a new csv.
     *
     * @param stripMultipleNewlines
     *         the strip multiple newlines
     * @param separator
     *         the separator
     * @param input
     *         the input
     */
    public CsvUtils( boolean stripMultipleNewlines, char separator, Reader input ) {
        this.stripMultipleNewlines = stripMultipleNewlines;
        this.separator = separator;
        this.fields = new ArrayList<>();
        this.eofSeen = false;
        this.in = new BufferedReader( input );
    }

    /**
     * Instantiates a new csv.
     *
     * @param stripMultipleNewlines
     *         the strip multiple newlines
     * @param separator
     *         the separator
     * @param input
     *         the input
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws UnsupportedEncodingException
     *         the unsupported encoding exception
     */
    public CsvUtils( boolean stripMultipleNewlines, char separator, InputStream input ) throws java.io.IOException {
        this.stripMultipleNewlines = stripMultipleNewlines;
        this.separator = separator;
        this.fields = new ArrayList<>();
        this.eofSeen = false;
        try ( Reader reader = stripBom( input ) ) {
            this.in = new BufferedReader( reader );
        }
    }

    /**
     * Checks for next.
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public boolean hasNext() throws java.io.IOException {
        if ( eofSeen ) {
            return false;
        }
        fields.clear();
        eofSeen = split( in, fields );
        if ( eofSeen ) {
            return !fields.isEmpty();
        } else {
            return true;
        }
    }

    /**
     * Next.
     *
     * @return the list
     */
    public List< String > next() {
        return fields;
    }

    /**
     * Discard linefeed.
     *
     * @param in
     *         the in
     * @param stripMultiple
     *         the strip multiple
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    // Returns true if EOF seen.
    private static boolean discardLinefeed( Reader in, boolean stripMultiple ) throws java.io.IOException {
        if ( stripMultiple ) {
            in.mark( NUMMARK );
            int value = in.read();
            while ( value != -1 ) {
                char c = ( char ) value;
                if ( c != CRETURN && c != LFEED ) {
                    in.reset();
                    return false;
                } else {
                    in.mark( NUMMARK );
                    value = in.read();
                }
            }
            return true;
        } else {
            in.mark( NUMMARK );
            int value = in.read();
            if ( value == -1 ) {
                return true;
            } else if ( ( char ) value != LFEED ) {
                in.reset();
            }
            return false;
        }
    }

    /**
     * Close.
     */
    public void close() {
        try {
            this.in.close();
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Skip comment.
     *
     * @param in
     *         the in
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private boolean skipComment( Reader in ) throws java.io.IOException {
        /* Discard line. */
        int value;
        while ( ( value = in.read() ) != -1 ) {
            char c = ( char ) value;
            if ( c == CRETURN ) {
                return discardLinefeed( in, stripMultipleNewlines );
            }
        }
        return true;
    }

    /**
     * Split.
     *
     * @param in
     *         the in
     * @param fields
     *         the fields
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    // Returns true when EOF has been seen.
    private boolean split( Reader in, ArrayList< String > fields ) throws java.io.IOException {
        StringBuilder sbuf = new StringBuilder();
        int value;
        while ( ( value = in.read() ) != -1 ) {
            char c = ( char ) value;
            switch ( c ) {
                case CRETURN -> {
                    if ( !sbuf.isEmpty() ) {
                        fields.add( sbuf.toString() );
                        sbuf.delete( 0, sbuf.length() );
                    }
                    return discardLinefeed( in, stripMultipleNewlines );
                }
                case LFEED -> {
                    if ( !sbuf.isEmpty() ) {
                        fields.add( sbuf.toString() );
                        sbuf.delete( 0, sbuf.length() );
                    }
                    if ( stripMultipleNewlines ) {
                        return discardLinefeed( in, stripMultipleNewlines );
                    } else {
                        return false;
                    }
                }
                case DQUOTE -> {
                    // Processing double-quoted string ..
                    while ( ( value = in.read() ) != -1 ) {
                        c = ( char ) value;
                        if ( c == DQUOTE ) {
                            // Saw another double-quote. Check if
                            // another char can be read.
                            in.mark( NUMMARK );
                            if ( ( value = in.read() ) == -1 ) {
                                // Nope, found EOF; means End of
                                // field, End of record and End of
                                // File
                                if ( !sbuf.isEmpty() ) {
                                    fields.add( sbuf.toString() );
                                    sbuf.delete( 0, sbuf.length() );
                                }
                                return true;
                            } else if ( ( c = ( char ) value ) == DQUOTE ) {
                                // Found a second double-quote
                                // character. Means the double-quote
                                // is included.
                                sbuf.append( DQUOTE );
                            } else if ( c == CRETURN ) {
                                // Found End of line. Means End of
                                // field, and End of record.
                                if ( !sbuf.isEmpty() ) {
                                    fields.add( sbuf.toString() );
                                    sbuf.delete( 0, sbuf.length() );
                                }
                                // Read and discard a line-feed if we
                                // can indeed do so.
                                return discardLinefeed( in, stripMultipleNewlines );
                            } else if ( c == LFEED ) {
                                // Found end of line. Means End of
                                // field, and End of record.
                                if ( !sbuf.isEmpty() ) {
                                    fields.add( sbuf.toString() );
                                    sbuf.delete( 0, sbuf.length() );
                                }
                                // No need to check further. At this
                                // point, we have not yet hit EOF, so
                                // we return false.
                                if ( stripMultipleNewlines ) {
                                    return discardLinefeed( in, stripMultipleNewlines );
                                } else {
                                    return false;
                                }
                            } else {
                                // Not one of EOF, double-quote,
                                // newline or line-feed. Means end of
                                // double-quote processing. Does NOT
                                // mean end-of-field or end-of-record.
                                in.reset();
                                break;
                            }
                        } else {
                            // Not a double-quote, so no special meaning.
                            sbuf.append( c );
                        }
                    }
                    // Hit EOF, and did not see the terminating double-quote.
                    if ( value == -1 ) {
                        // We ignore this error, and just add whatever
                        // left as the next field.
                        if ( !sbuf.isEmpty() ) {
                            fields.add( sbuf.toString() );
                            sbuf.delete( 0, sbuf.length() );
                        }
                        return true;
                    }
                }
                default -> {
                    if ( c == separator ) {
                        fields.add( sbuf.toString() );
                        sbuf.delete( 0, sbuf.length() );
                    } else {
                        sbuf.append( c );
                    }
                }
            }
        }
        if ( !sbuf.isEmpty() ) {
            fields.add( sbuf.toString() );
            sbuf.delete( 0, sbuf.length() );
        }
        return true;
    }

    public static List< Map< String, Object > > generateCsv( InputStream inputStream2 ) {
        if ( inputStream2 != null ) {

            byte[] byteArray;
            try {
                byteArray = IOUtils.toByteArray( inputStream2 );
                inputStream2.close();
            } catch ( Exception e ) {
                log.error( "CSV file is not available", e );
                throw new SusException( "CSV file is not available" );
            }

            try ( InputStream inputStream = new ByteArrayInputStream( byteArray );
                    Reader reader = new InputStreamReader( inputStream );
                    CSVParser csvParser = new CSVParser( reader, CSVFormat.DEFAULT ) ) {
                Iterator< CSVRecord > csvRecordIterator = csvParser.getRecords().iterator();
                List< Map< String, Object > > list = new ArrayList<>();

                List< String > fieldNames = getFieldHeader( new ByteArrayInputStream( byteArray ) );
                if ( isHeaderPresent( new ByteArrayInputStream( byteArray ) ) ) {
                    csvRecordIterator.next(); // skip header
                }
                while ( csvRecordIterator.hasNext() ) {
                    CSVRecord csvRecord = csvRecordIterator.next();
                    Map< String, Object > obj = new LinkedHashMap<>();
                    for ( int i = 0; i < fieldNames.size(); i++ ) {
                        String val = csvRecord.get( i );
                        obj.put( fieldNames.get( i ), findStringTypeAndReturn( val ) );
                    }
                    list.add( obj );
                }
                return list;
            } catch ( Exception e ) {
                log.error( "Unable to read CSV file", e );
                throw new SusException( "Unable to read CSV file" );
            }
        } else {
            log.error( "CSV file is not available" );
            throw new SusException( "CSV file is not available" );
        }
    }

    /**
     * Gets the field header.
     *
     * @param inputS1
     *         the input S 1
     *
     * @return the field header
     *
     * @throws IOException
     *         the ioexception
     */
    private static List< String > getFieldHeader( InputStream inputS1 ) throws IOException {
        List< String > fieldNames = new ArrayList<>();
        try ( Reader reader = new InputStreamReader( inputS1 ); CSVParser csvParser = new CSVParser( reader, CSVFormat.DEFAULT ) ) {
            Iterator< CSVRecord > csvRecordIterator = csvParser.getRecords().iterator();
            if ( csvRecordIterator.hasNext() ) {
                CSVRecord csvRecord = csvRecordIterator.next();
                char colName = 'A';
                for ( String item : csvRecord ) {
                    if ( item.matches( ".*[a-zA-Z].*" ) ) {
                        fieldNames.add( item.trim().replaceAll( "\\s+", ConstantsString.UNDERSCORE ).replaceAll( "\\.",
                                ConstantsString.UNDERSCORE ) );
                    } else {
                        fieldNames.add( colName + "" );
                    }
                    colName++;
                }
            }
        }
        inputS1.close();
        return fieldNames;
    }

    /**
     * Checks if is header present.
     *
     * @param inputS1
     *         the input S 1
     *
     * @return true, if is header present
     *
     * @throws IOException
     *         the exception
     */
    private static boolean isHeaderPresent( InputStream inputS1 ) throws IOException {
        try ( Reader reader = new InputStreamReader( inputS1 ); CSVParser csvParser = new CSVParser( reader, CSVFormat.DEFAULT ) ) {
            Iterator< CSVRecord > csvRecordIterator = csvParser.getRecords().iterator();
            if ( csvRecordIterator.hasNext() ) {
                CSVRecord csvRecord = csvRecordIterator.next();
                for ( String item : csvRecord ) {
                    if ( item.matches( ".*[a-zA-Z].*" ) ) {
                        inputS1.close();
                        return true;
                    }
                }
            }
        }
        inputS1.close();
        return false;
    }

    /**
     * Find string type and return.
     *
     * @param strNum
     *         the str num
     *
     * @return the object
     */
    private static Object findStringTypeAndReturn( String strNum ) {
        try {
            return Double.parseDouble( strNum );
        } catch ( NumberFormatException | NullPointerException e ) {
            log.error( e.getMessage(), e );
            return strNum;
        }
    }

}
