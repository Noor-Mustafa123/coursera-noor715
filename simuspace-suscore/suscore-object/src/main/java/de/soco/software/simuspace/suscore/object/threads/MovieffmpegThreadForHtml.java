package de.soco.software.simuspace.suscore.object.threads;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MovieUtils;

/**
 * The Class MovieFfmpegThread for extracting movie preview ,thumbnail and poster etc
 *
 * @author Ali Haider
 */
@Log4j2
public class MovieffmpegThreadForHtml implements Runnable {

    /**
     * The src file path on vault.
     */
    private String srcFilePath;

    /**
     * The srcfile name.
     */
    private String srcfileName;

    /**
     * Instantiates a new movie ffmpeg thread.
     */
    public MovieffmpegThreadForHtml() {
    }

    /**
     * Instantiates a new movieffmpeg thread.
     *
     * @param srcFilePath
     *         the src file path
     * @param srcfileName
     *         the srcfile name
     */
    public MovieffmpegThreadForHtml( String srcFilePath, String srcfileName ) {
        super();
        this.srcFilePath = srcFilePath;
        this.srcfileName = srcfileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            String uniqueIdentifier = "_" + UUID.randomUUID();
            File decripedSrcFile = new File( PropertiesManager.getDefaultServerTempPath() + File.separator
                    + FilenameUtils.removeExtension( srcfileName ) + uniqueIdentifier );
            Files.copy( EncryptAndDecryptUtils.fileIntoStream( new File( srcFilePath + File.separator + srcfileName ) ),
                    decripedSrcFile.toPath() );

            writeAllFileToDiskInFETemp( uniqueIdentifier );

            deleteTempFilesInStaging( uniqueIdentifier );

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Write all file to disk in FE temp.
     *
     * @param uniqueIdentifier
     *         the unique identifier
     */
    private void writeAllFileToDiskInFETemp( String uniqueIdentifier ) {
        String vaultFile = srcFilePath + File.separator + srcfileName;
        File destFile = new File( srcFilePath );
        File sourceDir = new File( srcFilePath );
        try {
            // convert file in formates which described in ffmpeg.json file and copy it to particular destination
            MovieUtils.makeMP4File( uniqueIdentifier, vaultFile, sourceDir, destFile );

        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ) );

        }

    }

    /**
     * Delete temp files in staging.
     *
     * @param uniqueIdentifier
     *         the unique identifier
     */
    private void deleteTempFilesInStaging( String uniqueIdentifier ) {
        String nameWithoutExtension = srcfileName.contains( ConstantsString.DOT ) ? srcfileName.split( "\\." )[ 0 ] : srcfileName;
        // deleting temp files no longer in use
        File decripedStagingFile = new File(
                PropertiesManager.getDefaultServerTempPath() + File.separator + nameWithoutExtension + uniqueIdentifier );

        if ( decripedStagingFile.exists() ) {
            decripedStagingFile.delete();
        }
    }

}
