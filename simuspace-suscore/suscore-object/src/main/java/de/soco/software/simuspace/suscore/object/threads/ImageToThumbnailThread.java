package de.soco.software.simuspace.suscore.object.threads;

import java.io.File;

import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.ImageUtil;

/**
 * The Class ImageToThumbnailThread for converting images to thumbnails in thread queues .
 *
 * @author Zeeshan jamal, Ali Haider
 */
public class ImageToThumbnailThread implements Runnable {

    /**
     * The src file.
     */
    private File srcFile;

    /**
     * The thumb nail file.
     */
    private File thumbNailFile;

    /**
     * The thumb nail vault file.
     */
    private File thumbNailVaultFile;

    /**
     * The encryption method.
     */
    private EncryptionDecryptionDTO encDec;

    /**
     * Instantiates a new image to thumbnail thread.
     */
    public ImageToThumbnailThread() {
    }

    /**
     * Instantiates a new image to thumbnail thread.
     *
     * @param srcFile
     *         the src file
     * @param thumbNailFile
     *         the thumb nail file
     * @param thumbNailVaultFile
     *         the thumb nail vault file
     * @param encDec
     *         the encryption decryption
     */
    public ImageToThumbnailThread( File srcFile, File thumbNailFile, File thumbNailVaultFile, EncryptionDecryptionDTO encDec ) {
        super();
        this.srcFile = srcFile;
        this.thumbNailFile = thumbNailFile;
        this.thumbNailVaultFile = thumbNailVaultFile;
        this.encDec = encDec;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            ImageUtil.createThumbNail( srcFile, thumbNailFile, encDec );

            // if encryption is on, copy encrypted thumbnail to vault
            if ( PropertiesManager.getEncryptionConfigs().isActive() ) {
                ImageUtil.createEncryptedThumbNail( srcFile, thumbNailVaultFile, encDec );
            } else {
                ImageUtil.createThumbNail( srcFile, thumbNailVaultFile, encDec );
            }

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }
    }

}
