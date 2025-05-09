package de.soco.software.simuspace.suscore.common.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.MatchedLine;
import de.soco.software.simuspace.suscore.common.model.ObjectVariableDTO;
import de.soco.software.simuspace.suscore.common.model.ScanResponseDTO;
import de.soco.software.simuspace.suscore.common.properties.ScanObjectDTO;

public class ScanObjectUtil {

    /**
     * The Constant REGEX.
     */
    public static final String REGEX = "^/(.*)/([a-z]*)$";

    /**
     * The Constant FLAG_I.
     */
    private static final String FLAG_I = "i";

    private ScanObjectUtil() {

    }

    /**
     * Prepare ScanObjectDTO from ObjectVariableDTO.
     *
     * @param objectVariable
     *         the object Variable
     * @param line
     *         the line
     * @param lineNumber
     *
     * @return the scan object DTO
     */
    public static ScanObjectDTO prepareScanObjectDTOFromObjectVariableDTO( ObjectVariableDTO objectVariable, MatchedLine matchedLine ) {
        ScanObjectDTO scanObjectDTO = new ScanObjectDTO();

        if ( matchedLine != null ) {
            scanObjectDTO.setLine( matchedLine.getLine() );

            if ( matchedLine.getLineNumber() != null ) {
                scanObjectDTO.setLineNumber( Integer.toString( matchedLine.getLineNumber() ) );
            }
        } else {
            scanObjectDTO.setLine( ConstantsString.EMPTY_STRING );
        }

        scanObjectDTO.setVariableGroup( objectVariable.getVariableGroup() );
        scanObjectDTO.setVariableMatch( objectVariable.getVariableMatch() );
        scanObjectDTO.setVariableRegex( objectVariable.getVariableRegex() );

        return scanObjectDTO;
    }

    /**
     * Prepares scanObject list from variables list and matchedLine list. Each index of scanList contains scanObject prepared from variables
     * list and matchedLineList corresponding index Example: scanList[0] contains scanObject prepared from variables[0] and
     * matchedLineList[0], scanList[1] contains scanObject prepared from variables[1] and matchedLineList[1], etc
     *
     * @param variables
     *         the variables
     * @param matchedLineList
     *         the matchedLine List
     *
     * @return the scan object DTO list
     */
    public static List< ScanObjectDTO > prepareScanObjectList( List< ObjectVariableDTO > variables, List< MatchedLine > matchedLineList ) {
        List< ScanObjectDTO > scanList = new ArrayList<>();

        int index = 0;
        for ( ObjectVariableDTO objectVariable : variables ) {
            MatchedLine matchedLine = matchedLineList.get( index );
            scanList.add( ScanObjectUtil.prepareScanObjectDTOFromObjectVariableDTO( objectVariable, matchedLine ) );
            index++;
        }

        return scanList;

    }

    /**
     * Returns MatchedLine List after scanning file with objectVariable List Each index of matchedLineList contains matched line data of
     * variables list corresponding index Example: matchedLineList[0] contains matched line data of variableList[0], matchedLineList[1]
     * contains matched line data of variableList[1], etc
     *
     * @param filePath
     *         the file Path
     * @param objectVariables
     *         the object Variable
     *
     * @return the matching lines
     */
    public static List< MatchedLine > getMatchingLinesFromFile( String filePath, List< ObjectVariableDTO > objectVariables ) {
        List< MatchedLine > matchedLineWithOffset;
        List< Pattern > patterns = getLinePatternsFromVariable( objectVariables );
        List< MatchedLine > matchedLine = getMatchedLines( filePath, objectVariables, patterns );

        // Rescan file to obtain matched lines with offset
        try ( LineIterator lineIteratorForOffset = FileUtils.lineIterator( new File( filePath ) ) ) {
            matchedLineWithOffset = getMatchingLineWithOffset( lineIteratorForOffset, matchedLine, objectVariables );
        } catch ( IOException e ) {
            throw new SusException( e.getMessage() );
        }
        return matchedLineWithOffset;
    }

    /**
     * Gets the matched lines.
     *
     * @param filePath
     *         the file path
     * @param objectVariables
     *         the object variables
     * @param patterns
     *         the patterns
     *
     * @return the matched lines
     */
    private static List< MatchedLine > getMatchedLines( String filePath, List< ObjectVariableDTO > objectVariables,
            List< Pattern > patterns ) {
        List< MatchedLine > matchedLines = new ArrayList<>();

        for ( int i = 0; i < patterns.size(); i++ ) {
            int lineFound = 0;
            int lineCount = 0;
            try ( LineIterator lineIterator = FileUtils.lineIterator( new File( filePath ) ) ) {
                while ( lineIterator.hasNext() ) {
                    String line = lineIterator.nextLine();
                    Matcher matcher = patterns.get( i ).matcher( line );
                    if ( matcher.find() ) {
                        lineFound++;
                        MatchedLine matchLine = new MatchedLine();
                        matchLine.setLinesMatched( lineFound );
                        matchLine.setLine( line );
                        matchLine.setLineNumber( lineCount );
                        if ( Integer.valueOf( objectVariables.get( i ).getLineMatch() ).equals( lineFound ) ) {
                            matchedLines.add( matchLine );
                            break;
                        }
                    }
                    lineCount++;
                }
            } catch ( IOException e ) {
                throw new SusException( e.getMessage() );
            }

        }
        return matchedLines;
    }

    /**
     * Each index of patterns list contains pattern of variables list corresponding index Example: patterns[0] contains pattern data of
     * variableList[0], patterns[0] contains pattern data of variableList[0], etc
     *
     * @param objectVariables
     *         the object Variables
     *
     * @return the pattern list
     */
    private static List< Pattern > getLinePatternsFromVariable( List< ObjectVariableDTO > variableList ) {

        List< Pattern > patterns = new ArrayList<>();

        for ( ObjectVariableDTO objectVariable : variableList ) {
            Pattern pattern = null;
            Pattern regexPattern = Pattern.compile( REGEX );
            Matcher regexMatcher = regexPattern.matcher( objectVariable.getLineRegex() );
            if ( regexMatcher.find() ) {
                if ( regexMatcher.group( ConstantsInteger.INTEGER_VALUE_TWO ).contains( FLAG_I ) ) {
                    pattern = RegexUtil.preparePattern( RegexUtil.prepareRegex( regexMatcher.group( ConstantsInteger.INTEGER_VALUE_ONE ) ),
                            false );
                } else {
                    pattern = RegexUtil.preparePattern( RegexUtil.prepareRegex( regexMatcher.group( ConstantsInteger.INTEGER_VALUE_ONE ) ),
                            true );
                }
            }
            if ( null == pattern ) {
                pattern = RegexUtil.preparePattern( objectVariable.getLineRegex(), true );
            }

            patterns.add( pattern );
        }

        return patterns;
    }

    /**
     * Get Matching line with offset from text file.
     *
     * @param lineIterator
     *         the line Iterator
     * @param matchedLine
     *         the matchedLine
     *
     * @return the matched line with offset
     */
    public static List< MatchedLine > getMatchingLineWithOffset( LineIterator it, List< MatchedLine > matchedLine,
            List< ObjectVariableDTO > variableList ) {

        List< MatchedLine > matchedLinesWithOffset = new ArrayList<>();

        // add offset to line number
        populateOffsetLineNumbers( matchedLinesWithOffset, matchedLine, variableList );

        int lineCount = 0;
        while ( it.hasNext() ) {

            String line = it.nextLine();

            int index = 0;
            for ( ObjectVariableDTO objectVariable : variableList ) {

                if ( matchedLinesWithOffset.get( index ) == null || matchedLinesWithOffset.get( index ).getLineNumber() < 0 ) {
                    matchedLinesWithOffset.set( index, null );
                    index++;
                    continue;
                }

                MatchedLine offsetLine = matchedLinesWithOffset.get( index );

                if ( lineCount == offsetLine.getLineNumber() ) {
                    offsetLine.setLine( line );
                    matchedLinesWithOffset.set( index, offsetLine );
                }

                index++;
            }

            lineCount++;
        }

        return matchedLinesWithOffset;
    }

    /**
     * Updated line numbers of matched lines after offset.
     *
     * @param offsetLines
     *         the offsetLines
     * @param matchedLines
     *         the matchedLines
     */
    private static void populateOffsetLineNumbers( List< MatchedLine > offsetLines, List< MatchedLine > matchedLines,
            List< ObjectVariableDTO > variableList ) {
        int index = 0;
        for ( ObjectVariableDTO objectVariable : variableList ) {
            if ( matchedLines.get( index ) == null ) {
                offsetLines.add( index, null );
                index++;
                continue;
            }

            MatchedLine matchedLine = matchedLines.get( index );

            MatchedLine matchedLineWithOffset = new MatchedLine();
            matchedLineWithOffset.setLineNumber( matchedLine.getLineNumber() + Integer.parseInt( objectVariable.getLineOffset() ) );
            matchedLineWithOffset.setLinesMatched( matchedLine.getLinesMatched() );

            offsetLines.add( index, matchedLineWithOffset );
            index++;
        }
    }

    /**
     * Gets the object match.
     *
     * @param matcher
     *         the matcher
     *
     * @return the object match
     */
    public static int getObjectMatch( Matcher matcher ) {
        int matchCount = 0;
        while ( matcher.find() ) {
            matchCount++;
        }
        return matchCount;
    }

    /**
     * Sets the first match.
     *
     * @param scanObj
     *         the scan obj
     * @param matcher
     *         the matcher
     * @param response
     *         the response
     */
    public static void setFirstMatch( ScanObjectDTO scanObj, Matcher matcher, ScanResponseDTO response ) {
        response.setMatch( matcher.group() );
        if ( scanObj.getVariableGroup().isEmpty()
                || Integer.parseInt( scanObj.getVariableGroup() ) == ConstantsInteger.INTEGER_VALUE_ZERO ) {
            response.setStart( Integer.toString( matcher.start() ) );
            response.setEnd( Integer.toString( matcher.end() ) );
        } else {
            try {
                response.setStart( Integer.toString( matcher.start( Integer.parseInt( scanObj.getVariableGroup() ) ) ) );
                response.setEnd( Integer.toString( matcher.end( Integer.parseInt( scanObj.getVariableGroup() ) ) ) );
            } catch ( Exception e ) {
                response.setStart( Integer.toString( matcher.start() ) );
                response.setEnd( Integer.toString( matcher.end() ) );
            }
        }
        response.setLineNumber( scanObj.getLineNumber() );
    }

}
