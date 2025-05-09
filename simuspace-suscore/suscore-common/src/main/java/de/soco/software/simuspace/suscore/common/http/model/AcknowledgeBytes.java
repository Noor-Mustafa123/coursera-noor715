package de.soco.software.simuspace.suscore.common.http.model;

import java.io.File;
import java.io.Serializable;

/**
 * The Class AcknowledgeBytes only deals to provide byte acknowledge model for utility purposes available globally in the system.
 *
 * @author Ahsan Khan
 */
public class AcknowledgeBytes implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 6854469548902034080L;

    /**
     * The file.
     */
    private File file;

    /**
     * The transferred bytes.
     */
    private long transferredBytes;

    /**
     * Gets the file.
     *
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the file.
     *
     * @param file
     *         the new file
     */
    public void setFile( File file ) {
        this.file = file;
    }

    /**
     * Gets the transferred bytes.
     *
     * @return the transferred bytes
     */
    public long getTransferredBytes() {
        return transferredBytes;
    }

    /**
     * Sets the transferred bytes.
     *
     * @param transferredBytes
     *         the new transferred bytes
     */
    public void setTransferredBytes( long transferredBytes ) {
        this.transferredBytes = transferredBytes;
    }

}