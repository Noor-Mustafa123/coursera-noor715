package de.soco.software.simuspace.suscore.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.ExitCodesAndSignals;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DynamicScript;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.WfFieldsUiDTO;

/**
 * The type Dynamic scripts util.
 */
@Log4j2
public class DynamicScriptsUtil {

    /**
     * Gets dynamic script fields from config by key.
     *
     * @param key
     *         the key
     *
     * @return the dynamic script fields from config by key
     */
    public static List< WfFieldsUiDTO > getDynamicScriptFieldsFromConfigByKey( String key ) {
        List< WfFieldsUiDTO > fields = new ArrayList<>();
        List< DynamicScript > scripts;
        try {
            scripts = PropertiesManager.getDynamicScriptsByKey( key );
            scripts.forEach( script -> fields.add(
                    new WfFieldsUiDTO( script.getName().replace( ConstantsString.DOUBLE_QUOTE_STRING, ConstantsString.EMPTY_STRING ),
                            script.getName(), null ) ) );

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }

        return fields;
    }

    /**
     * Gets dynamic script details from config by name and key.
     *
     * @param name
     *         the name
     * @param key
     *         the key
     *
     * @return the dynamic script details from config by name and key
     */
    public static DynamicScript getDynamicScriptDetailsFromConfigByNameAndKey( String name, String key ) {
        try {
            List< DynamicScript > scripts;
            scripts = PropertiesManager.getDynamicScriptsByKey( key );
            for ( DynamicScript script : scripts ) {
                if ( script.getName() != null && script.getName().equalsIgnoreCase( name ) ) {
                    return script;
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
        return null;
    }

    /* **** Select Scripts *****/

    /**
     * Call select script map.
     *
     * @param script
     *         the script
     * @param userUid
     *         the user uid
     * @param elementKey
     *         the element key
     * @param selectField
     *         the select field
     *
     * @return the map
     */
    public static Map< String, Object > callSelectScript( DynamicScript script, String userUid, String elementKey, String selectField ) {
        Path outputFilePath = null;
        Path inputFilePath = null;
        try {
            outputFilePath = prepareOutputFilePathForUser( userUid, elementKey, script.getName() );
            inputFilePath = prepareInputFileForUser( userUid, elementKey, script.getName(), selectField );
            var scriptResult = callSelectScript( script.getPath(), userUid, elementKey, outputFilePath.toAbsolutePath().toString(),
                    inputFilePath.toAbsolutePath().toString() );
            if ( scriptResult.getExitValue() != ExitCodesAndSignals.SUCCESS.getExitCode() ) {
                throw new SusException( "Failed to retrieve options from script" );
            }
            return readFileContent( outputFilePath );
        } finally {
            FileUtils.deleteIfExists( outputFilePath );
            FileUtils.deleteIfExists( inputFilePath );
        }
    }

    /**
     * Prepare input file for user path.
     *
     * @param userUid
     *         the user uid
     * @param elementKey
     *         the element key
     * @param name
     *         the name
     * @param payload
     *         the payload
     *
     * @return the path
     */
    private static Path prepareInputFileForUser( String userUid, String elementKey, String name, String payload ) {
        Path inputFilePath = Path.of(
                PropertiesManager.getDefaultServerTempPath() + File.separator + userUid + File.separator + elementKey + File.separator
                        + name + "_input_" + LocalDateTime.now()
                        .format( DateTimeFormatter.ofPattern( DateUtils.DATE_PATTERN_FOR_TIMESTAMP ) ) );
        try {
            Files.deleteIfExists( inputFilePath );
            Files.createFile( inputFilePath );
            Files.writeString( inputFilePath, payload );
        } catch ( IOException e ) {
            throw new SusException( "failed to create payload for script" );
        }
        FileUtils.setGlobalAllFilePermissions( inputFilePath );
        return inputFilePath;
    }

    /**
     * Read select ui from file map.
     *
     * @param outputFilePath
     *         the output file path
     *
     * @return the map
     */
    private static Map< String, Object > readFileContent( Path outputFilePath ) {
        Map< String, Object > fileContent;
        if ( Files.exists( outputFilePath ) ) {
            try ( InputStream is = Files.newInputStream( outputFilePath ) ) {
                fileContent = JsonUtils.jsonToObject( is, Map.class );
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( "Failed to parse script response" );
            }
        } else {
            throw new SusException( "Script did not generate response" );
        }
        return fileContent;
    }

    /**
     * Prepare output file path for user path.
     *
     * @param userUid
     *         the user uid
     * @param elementKey
     *         the element key
     * @param name
     *         the name
     *
     * @return the path
     */
    private static Path prepareOutputFilePathForUser( String userUid, String elementKey, String name ) {
        Path outputFile = Path.of(
                PropertiesManager.getDefaultServerTempPath() + File.separator + userUid + File.separator + elementKey + File.separator
                        + name + "_output_" + LocalDateTime.now()
                        .format( DateTimeFormatter.ofPattern( DateUtils.DATE_PATTERN_FOR_TIMESTAMP ) ) );
        if ( Files.notExists( outputFile.getParent() ) ) {
            try {
                Files.createDirectories( outputFile.getParent() );
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( "Failed to create directories for selectScript output path" );
            }
        }
        FileUtils.setGlobalAllFilePermissions( outputFile.getParent() );
        return outputFile;
    }

    /**
     * Call select script process result.
     *
     * @param path
     *         the path
     * @param userUid
     *         the user uid
     * @param elementKey
     *         the element key
     * @param outputFilePath
     *         the output file path
     * @param inputFilePath
     *         the input file path
     *
     * @return the process result
     */
    private static ProcessResult callSelectScript( String path, String userUid, String elementKey, String outputFilePath,
            String inputFilePath ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "calling Select Script at path " + path );
            StringBuilder builder = new StringBuilder();
            builder.append( PropertiesManager.getPythonExecutionPathOnServer() ).append( " " );
            builder.append( path ).append( " " );
            builder.append( "-e" ).append( " " );
            builder.append( elementKey ).append( " " );
            builder.append( "-o" ).append( " " );
            builder.append( outputFilePath ).append( " " );
            builder.append( "-i" ).append( " " );
            builder.append( inputFilePath ).append( " " );
            processResult = LinuxUtils.runUserCommand( userUid, builder.toString(), null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /* **** Field Scripts *****/
    public static Map< String, Object > callFieldScript( DynamicScript script, String userUid, String elementKey, String selectField ) {
        Path outputFilePath = null;
        Path inputFilePath = null;
        try {
            outputFilePath = prepareOutputFilePathForUser( userUid, elementKey, script.getName() );
            inputFilePath = prepareInputFileForUser( userUid, elementKey, script.getName(), selectField );
            //
            var scriptResult = callFieldScript( script.getPath(), userUid, elementKey, outputFilePath.toAbsolutePath().toString(),
                    inputFilePath.toAbsolutePath().toString() );
            if ( scriptResult.getExitValue() != ExitCodesAndSignals.SUCCESS.getExitCode() ) {
                throw new SusException( "Failed to retrieve options from script" );
            }

            return readFileContent( outputFilePath );
        } finally {
            FileUtils.deleteIfExists( outputFilePath );
            FileUtils.deleteIfExists( inputFilePath );
        }
    }

    private static ProcessResult callFieldScript( String path, String userUid, String elementKey, String outputFilePath,
            String inputFilePath ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "calling Field Script at path " + path );
            StringBuilder builder = new StringBuilder();
            builder.append( PropertiesManager.getPythonExecutionPathOnServer() ).append( " " );
            builder.append( path ).append( " " );
            builder.append( "-e" ).append( " " );
            builder.append( elementKey ).append( " " );
            builder.append( "-o" ).append( " " );
            builder.append( outputFilePath ).append( " " );
            builder.append( "-i" ).append( " " );
            builder.append( inputFilePath ).append( " " );
            processResult = LinuxUtils.runUserCommand( userUid, builder.toString(), null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

}
