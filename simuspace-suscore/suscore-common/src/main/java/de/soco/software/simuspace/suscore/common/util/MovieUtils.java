package de.soco.software.simuspace.suscore.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.suscore.jsonschema.model.FFmpegConfig;
import de.soco.software.suscore.jsonschema.model.FfmpegExtension;

/**
 * Class Containing All Utilizes Method For Movie Media Type and formats using ffmpeg.
 *
 * @author Noman.Arshad Nosheen.Sharif
 */
@Log4j2
public class MovieUtils {

    /**
     * The Constant COMAND_FINAL.
     */
    private static final String COMAND_FINAL = "comand Final :";

    /**
     * The Constant THUMB_PNG.
     */
    private static final String THUMB_PNG = "thumb.png";

    /**
     * The Constant PNG.
     */
    private static final String PNG = ".png";

    /**
     * The Constant OUTPUT_PATH_HOLDER_VAR.
     */
    private static final String OUTPUT_PATH_HOLDER_VAR = "$output";

    /**
     * The Constant INPUT_PATH_HOLDER_VAR.
     */
    private static final String INPUT_PATH_HOLDER_VAR = "$inp";

    /**
     * The Constant OUTPUT_PATH_HOLDER.
     */
    private static final String OUTPUT_PATH_HOLDER = "[output]";

    /**
     * The Constant INPUT_PATH_HOLDER.
     */
    private static final String INPUT_PATH_HOLDER = "[input]";

    /**
     * Private Constructor to avoid Instantiation.
     */
    private MovieUtils() {

    }

    /**
     * Make AVI file.
     *
     * @param vault
     *         the vault
     * @param src
     *         the src
     * @param des
     *         the des
     * @param fileExtension
     *         the file extension
     */
    public static void makeAVIFile( String vault, File src, File des ) {
        try {
            if ( !des.exists() ) {
                des.mkdirs();
            }
            log.info( "media conversion: AVI" );

            File main1 = new File( src.getAbsolutePath() + File.separator + getLastDirName( vault ) );
            String destinationAndFormate = des.getPath() + File.separator + getLastDirName( vault );

            String decriptedSrcFilePath = PropertiesManager.getDefaultServerTempPath() + File.separator + main1.getName();
            File decriptedSrcFile = new File( decriptedSrcFilePath );

            log.info( "copying poster and thumb" );
            copyPosterAndThumb( main1, destinationAndFormate );
            log.info( "reading ffmpeg config" );
            Map< String, String > fileFormates = outputFormatesByFileExtension( "avi" );
            if ( fileFormates != null ) {
                for ( Entry< String, String > output : fileFormates.entrySet() ) {
                    String command = output.getValue();
                    String preparedCommand = prepareCommand( command, decriptedSrcFile.getAbsolutePath(), destinationAndFormate );
                    log.info( COMAND_FINAL + preparedCommand );
                    String[] executeCommand = preparedCommand.split( " " );
                    executeProcess( executeCommand );
                }
            } else {
                String[] finalCommand2 = { PropertiesManager.getFFmpegPath() + "ffmpeg", "-i", decriptedSrcFile.getAbsolutePath(), "-f",
                        "mp4", destinationAndFormate };
                executeProcess( finalCommand2 );
                log.info( Arrays.toString( finalCommand2 ) );
            }

        } catch ( Exception e ) {
            log.info( e.getMessage(), e );
        }
    }

    /**
     * Copy poster and thumb.
     *
     * @param main
     *         the main
     * @param destinationAndFormate
     *         the destination and formate
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static void copyPosterAndThumb( File main, String destinationAndFormate ) throws IOException {
        File srcPoster = new File( main.getAbsolutePath() + PNG );
        if ( srcPoster.exists() ) {
            FileUtils.copyFile( srcPoster, new File( destinationAndFormate + PNG ) );
            srcPoster.delete();
        }
        File srcThumb = new File( main.getAbsolutePath() + THUMB_PNG );
        if ( srcThumb.exists() ) {
            FileUtils.copyFile( srcThumb, new File( destinationAndFormate + THUMB_PNG ) );
            srcThumb.delete();
        }
    }

    /**
     * Output formates by file extension.
     *
     * @param extension
     *         the extension
     *
     * @return the map
     */
    private static Map< String, String > outputFormatesByFileExtension( String extension ) {
        try {
            FFmpegConfig ffmpegConfig = PropertiesManager.getFfmpegConfig();
            ArrayList< FfmpegExtension > fileFormates = ffmpegConfig.getFfmpeg();
            for ( FfmpegExtension ffmpegExtension : fileFormates ) {
                if ( ffmpegExtension.getInput().equalsIgnoreCase( extension ) ) {
                    return ffmpegExtension.getOutput();
                }
            }
            return null;
        } catch ( Exception e ) {
            log.info( "ffmpeg config error : " + e );
        }
        return null;
    }

    /**
     * Make MKV file.
     *
     * @param vault
     *         the vault
     * @param src
     *         the src
     * @param des
     *         the des
     * @param fileExtension
     *         the file extension
     */
    public static void makeMKVFile( String vault, File src, File des ) {
        try {
            if ( !des.exists() && des.isDirectory() ) {
                des.mkdirs();
            }
            log.info( "media conversion: MKV" );
            File main1 = new File( src.getAbsolutePath() + File.separator + getLastDirName( vault ) );
            String destinationAndFormate = des.getPath() + File.separator + getLastDirName( vault );

            String decriptedSrcFilePath = PropertiesManager.getDefaultServerTempPath() + File.separator + main1.getName();
            File decriptedSrcFile = new File( decriptedSrcFilePath );

            log.info( "copying poster and thumb" );
            copyPosterAndThumb( main1, destinationAndFormate );
            log.info( "reading ffmpeg config" );
            Map< String, String > fileFormates = outputFormatesByFileExtension( "mkv" );
            if ( fileFormates != null ) {
                for ( Entry< String, String > output : fileFormates.entrySet() ) {
                    String command = output.getValue();
                    String preparedCommand = prepareCommand( command, decriptedSrcFile.getAbsolutePath(), destinationAndFormate );
                    log.info( COMAND_FINAL + preparedCommand );
                    String[] executeCommand = preparedCommand.split( " " );
                    executeProcess( executeCommand );
                }
            } else {
                String[] finalCommand2 = { PropertiesManager.getFFmpegPath() + "ffmpeg", "-i", decriptedSrcFile.getAbsolutePath(), "-c",
                        "copy", "-f", "mp4", destinationAndFormate };
                executeProcess( finalCommand2 );
                log.info( Arrays.toString( finalCommand2 ) );
            }

        } catch ( Exception e ) {
            log.info( e.getMessage(), e );
        }
    }

    /**
     * Make MOV file.
     *
     * @param vault
     *         the vault
     * @param src
     *         the src
     * @param des
     *         the des
     * @param fileExtension
     *         the file extension
     */
    public static void makeMOVFile( String vault, File src, File des ) {
        try {
            if ( !des.exists() && des.isDirectory() ) {
                des.mkdirs();
            }
            log.info( "media conversion: MKV" );
            File main1 = new File( src.getAbsolutePath() + File.separator + getLastDirName( vault ) );
            String destinationAndFormate = des.getPath() + File.separator + getLastDirName( vault );

            String decriptedSrcFilePath = PropertiesManager.getDefaultServerTempPath() + File.separator + main1.getName();
            File decriptedSrcFile = new File( decriptedSrcFilePath );

            log.info( "copying poster and thumb" );
            copyPosterAndThumb( main1, destinationAndFormate );
            log.info( "reading ffmpeg config" );
            Map< String, String > fileFormates = outputFormatesByFileExtension( "mov" );
            if ( fileFormates != null ) {
                for ( Entry< String, String > output : fileFormates.entrySet() ) {
                    String command = output.getValue();
                    String preparedCommand = prepareCommand( command, decriptedSrcFile.getAbsolutePath(), destinationAndFormate );
                    log.info( COMAND_FINAL + preparedCommand );
                    String[] executeCommand = preparedCommand.split( " " );
                    executeProcess( executeCommand );
                }
            } else {
                String[] finalCommand2 = { PropertiesManager.getFFmpegPath() + "ffmpeg", "-i", decriptedSrcFile.getAbsolutePath(), "-c",
                        "copy", "-f", "mp4", destinationAndFormate };
                executeProcess( finalCommand2 );
                log.info( Arrays.toString( finalCommand2 ) );
            }

        } catch ( Exception e ) {
            log.info( e.getMessage(), e );
        }
    }

    /**
     * Make MP 4 file.
     *
     * @param uniqueIdentifier
     *         the unique identifier
     * @param vaultFile
     *         the vault file
     * @param sourceDir
     *         the source dir
     * @param destinationFile
     *         the destination file
     */
    public static void makeMP4File( String uniqueIdentifier, String vaultFile, File sourceDir, File destinationFile ) {
        try {
            String fileName = getLastDirName( vaultFile );
            String nameWithoutExtension = fileName.contains( ConstantsString.DOT ) ? fileName.split( "\\." )[ 0 ] : fileName;
            if ( !destinationFile.exists() && destinationFile.isDirectory() ) {
                destinationFile.mkdirs();
            }
            log.info( "media conversion: MP4" );
            File main1 = new File( sourceDir.getAbsolutePath() + File.separator + getLastDirName( vaultFile ) );
            String destinationAndFormate = destinationFile.getPath() + File.separator + nameWithoutExtension;

            String decriptedSrcFilePath = PropertiesManager.getDefaultServerTempPath() + File.separator + nameWithoutExtension
                    + uniqueIdentifier;
            File decriptedSrcFile = new File( decriptedSrcFilePath );

            log.info( "copying poster and thumb" );
            copyPosterAndThumb( main1, destinationAndFormate );
            log.info( "reading ffmpeg config" );
            Map< String, String > fileFormates = outputFormatesByFileExtension( "mp4" );
            if ( fileFormates != null ) {
                for ( Entry< String, String > output : fileFormates.entrySet() ) {
                    String command = output.getValue();
                    String preparedCommand = prepareCommand( command, decriptedSrcFile.getAbsolutePath(), destinationAndFormate );
                    log.info( COMAND_FINAL + preparedCommand );
                    String[] executeCommand = preparedCommand.split( " " );
                    executeProcess( executeCommand );
                }
            } else {
                String[] finalCommand2 = { PropertiesManager.getFFmpegPath() + "ffmpeg", "-i", decriptedSrcFile.getAbsolutePath(), "-f",
                        "mp4", destinationAndFormate };
                executeProcess( finalCommand2 );
                log.info( Arrays.toString( finalCommand2 ) );
            }

        } catch ( Exception e ) {
            log.info( e.getMessage(), e );
        }
    }

    /**
     * Make WEBM file.
     *
     * @param vault
     *         the vault
     * @param src
     *         the src
     * @param des
     *         the des
     * @param fileExtension
     *         the file extension
     */
    public static void makeWEBMFile( String vault, File src, File des ) {
        try {
            if ( !des.exists() && des.isDirectory() ) {
                des.mkdirs();
            }
            log.info( "media conversion: WEBM" );
            File main1 = new File( src.getAbsolutePath() + File.separator + getLastDirName( vault ) );
            String destinationAndFormate = des.getPath() + File.separator + getLastDirName( vault );

            String decriptedSrcFilePath = PropertiesManager.getDefaultServerTempPath() + File.separator + main1.getName();
            File decriptedSrcFile = new File( decriptedSrcFilePath );

            log.info( "copying poster and thumb" );
            copyPosterAndThumb( main1, destinationAndFormate );
            log.info( "reading ffmpeg config" );
            Map< String, String > fileFormates = outputFormatesByFileExtension( "webm" );
            if ( fileFormates != null ) {
                for ( Entry< String, String > output : fileFormates.entrySet() ) {
                    String command = output.getValue();
                    String preparedCommand = prepareCommand( command, decriptedSrcFile.getAbsolutePath(), destinationAndFormate );
                    log.info( COMAND_FINAL + preparedCommand );
                    String[] executeCommand = preparedCommand.split( " " );
                    executeProcess( executeCommand );
                }
            } else {
                String[] finalCommand2 = { PropertiesManager.getFFmpegPath() + "ffmpeg", "-i", decriptedSrcFile.getAbsolutePath(), "-f",
                        "mp4", destinationAndFormate };
                executeProcess( finalCommand2 );
                log.info( Arrays.toString( finalCommand2 ) );
            }

        } catch ( Exception e ) {
            log.info( e.getMessage(), e );
        }
    }

    /**
     * Prepare command.
     *
     * @param ffmpegCommand
     *         the ffmpeg command
     * @param inputFilePath
     *         the input file path
     * @param outputFilePath
     *         the output file path
     *
     * @return the string
     */
    private static String prepareCommand( final String ffmpegCommand, final String inputFilePath, final String outputFilePath ) {

        String command = null;
        if ( !StringUtils.isEmpty( outputFilePath ) ) {

            String ffcommand = ffmpegCommand.replace( INPUT_PATH_HOLDER, inputFilePath );

            command = ffcommand.replace( OUTPUT_PATH_HOLDER, outputFilePath );
        }
        return PropertiesManager.getFFmpegPath() + command;
    }

    /**
     * Checks if is process running A.
     *
     * @param taskList
     *         the task list
     *
     * @return true, if is process running A
     */
    public static boolean executeProcess( String[] taskList ) {
        LinuxUtils.runCommand( taskList, ConstantsString.COMMAND_KARAF_LOGGING_ON, null );
        return true;
    }

    /**
     * Gets the last dir name.
     *
     * @param f
     *         the f
     *
     * @return the last dir name
     */
    public static String getLastDirName( String f ) {
        String ext = "";
        int i = f.lastIndexOf( '/' );
        if ( i > 0 && i < f.length() - 1 ) {
            ext = f.substring( i + 1 );
        }
        return ext;
    }

    /**
     * Prepare thumbnail from file.
     *
     * @param srcDirPath
     *         the src dir path
     * @param srcFileName
     *         the src file name
     * @param thumFileName
     *         the thum file name
     *
     * @return the string
     */
    public static String prepareThumbnailFromMovieFile( final String srcDirPath, final String srcFileName, final String thumFileName ) {
        String thumbFile = srcDirPath + File.separator + thumFileName;
        String srcFileToRead = srcDirPath + File.separator + srcFileName;
        try {
            ImageUtil.resizeImageForPreview( new File( srcFileToRead ), new File( thumbFile ) );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return thumbFile;
    }

    /**
     * Gets the movie poster.
     *
     * @param srcFilePathOnVault
     *         the src file path on vault
     * @param srcfileName
     *         the srcfile name
     *
     * @return the movie poster
     */
    public static String getMoviePoster( final String srcFilePathOnVault, final String srcfileName, EncryptionDecryptionDTO encDec ) {
        String nameWithoutExtension = srcfileName.contains( ConstantsString.DOT ) ? srcfileName.split( "\\." )[ 0 ] : srcfileName;
        String outputFilePoster = srcFilePathOnVault + File.separator + nameWithoutExtension
                + ConstantsFileProperties.Commands.IMAGE_POSTER.getExtension();
        try {
            File outFile = new File( outputFilePoster );

            if ( !outFile.exists() ) {

                String srcFile = srcFilePathOnVault + File.separator + srcfileName;
                String decripedSrcFilePath = PropertiesManager.getDefaultServerTempPath() + File.separator + nameWithoutExtension;

                File decripedSrcFile = new File( decripedSrcFilePath );

                try ( InputStream decritedStreamDromVault = EncryptAndDecryptUtils
                        .decryptFileIfEncpEnabledAndReturnStream( new File( srcFile ), encDec ) ) {
                    Files.copy( decritedStreamDromVault, decripedSrcFile.toPath() );
                }

                String commandToExecuteForPoster = prepareFFMpegCommand( ConstantsFileProperties.Commands.IMAGE_POSTER.getCommand(),
                        decripedSrcFilePath, outputFilePoster );
                if ( OSValidator.isUnix() ) {
                    String[] cmdArrToExecuteForPoster = { ConstantsString.NIX_BASH, ConstantsString.NIX_C_PARAM,
                            commandToExecuteForPoster };

                    executeCommand( cmdArrToExecuteForPoster );
                } else if ( OSValidator.isWindows() ) {
                    String[] cmdArrToExecuteForPoster = { ConstantsString.WIN_EXE, ConstantsString.WIN_C_PARAM, commandToExecuteForPoster };

                    executeCommand( cmdArrToExecuteForPoster );
                }

            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return outputFilePoster;
    }

    /**
     * Prepare FF mpeg command.
     *
     * @param ffmpegCommand
     *         the ffmpeg command
     * @param inputFilePath
     *         the input file path
     * @param outputFilePath
     *         the output file path
     *
     * @return the string
     */
    private static String prepareFFMpegCommand( final String ffmpegCommand, final String inputFilePath, final String outputFilePath ) {

        String command = null;
        if ( !StringUtils.isEmpty( outputFilePath ) ) {

            String ffcommand = ffmpegCommand.replace( INPUT_PATH_HOLDER_VAR,
                    ConstantsString.DOUBLE_QUOTE_STRING + inputFilePath + ConstantsString.DOUBLE_QUOTE_STRING );

            command = ffcommand.replace( OUTPUT_PATH_HOLDER_VAR,
                    ConstantsString.DOUBLE_QUOTE_STRING + outputFilePath + ConstantsString.DOUBLE_QUOTE_STRING );

        }

        return command;
    }

    /**
     * Execute command.
     *
     * @param commandToExecute
     *         the command to execute
     */
    private static void executeCommand( String[] commandToExecute ) {
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec( commandToExecute );
            p.waitFor();
            String line;
            try ( InputStreamReader isr = new InputStreamReader( p.getInputStream() ); BufferedReader reader = new BufferedReader( isr ) ) {
                while ( ( line = reader.readLine() ) != null ) {
                    output.append( line ).append( ConstantsString.NEW_LINE );
                }
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        } catch ( InterruptedException e ) {
            log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
        }

    }

}
