package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.MovieUtils;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;

/**
 * The Class MovieFfmpegThread for extracting movie preview ,thumbnail and poster etc
 *
 * @author Nosheen.Sharif
 */
@Log4j2
public class MovieffmpegThread implements Runnable {

    /**
     * The src file path on vault.
     */
    private String srcFilePathOnVault;

    /**
     * The srcfile name.
     */
    private String srcfileName;

    /**
     * The document manager.
     */
    private DocumentManager documentManager;

    /**
     * The document DTO.
     */
    private DocumentDTO documentDTO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new movie ffmpeg thread.
     */
    public MovieffmpegThread() {
    }

    /**
     * Instantiates a new Movieffmpeg thread.
     *
     * @param documentManager
     *         the document manager
     * @param documentDTO
     *         the document dto
     * @param srcFilePathOnVault
     *         the src file path on vault
     * @param srcfileName
     *         the srcfile name
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public MovieffmpegThread( DocumentManager documentManager, DocumentDTO documentDTO, String srcFilePathOnVault, String srcfileName,
            EntityManagerFactory entityManagerFactory ) {
        super();
        this.srcFilePathOnVault = srcFilePathOnVault;
        this.srcfileName = srcfileName;
        this.documentManager = documentManager;
        this.documentDTO = documentDTO;
        this.entityManagerFactory = entityManagerFactory;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String extension = FilenameUtils.getExtension( documentDTO.getName() );

            createFFMpegMovies( srcFilePathOnVault, srcfileName );
            // write to disk
            documentManager.writeAllFileToDiskInFETemp( entityManager, documentDTO, PropertiesManager.getFeStaticPath(), extension );

            deleteTempFilesInStaging();

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Create ff mpeg movies.
     *
     * @param srcFilePathOnVault
     *         the src file path on vault
     * @param srcfileName
     *         the srcfile name
     */
    private void createFFMpegMovies( final String srcFilePathOnVault, final String srcfileName ) {

        String fileNameOnly = FilenameUtils.removeExtension( srcfileName );
        String imagePosterName = fileNameOnly + ConstantsFileProperties.Commands.IMAGE_POSTER.getExtension();

        MovieUtils.getMoviePoster( srcFilePathOnVault, srcfileName, documentDTO.getEncryptionDecryption() );
        String thumbnailName = fileNameOnly + ConstantsString.OBJECT_THUMB_NAIL_FILE_POSTFIX
                + ConstantsFileProperties.Commands.IMAGE_POSTER.getExtension();

        MovieUtils.prepareThumbnailFromMovieFile( srcFilePathOnVault, imagePosterName, thumbnailName );

    }

    /**
     * Delete temp files in staging.
     */
    private void deleteTempFilesInStaging() {
        // deleting temp files no longer in use
        File decripedStagingFile = new File( PropertiesManager.getDefaultServerTempPath() + File.separator + srcfileName );

        if ( decripedStagingFile.exists() ) {
            decripedStagingFile.delete();
        }
    }

}
