package de.soco.software.simuspace.workflow.model.impl;

import de.soco.software.simuspace.suscore.common.model.DocumentDTO;

/**
 * The Class HtmlObjectDTO.
 *
 * @author Ali Haider
 */
public class HtmlObjectDTO extends ObjectDTO {

    /**
     * The zip file.
     */
    private DocumentDTO zipFile;

    /**
     * Instantiates a new html object DTO.
     *
     * @param zipFile
     *         the zip file
     */
    public HtmlObjectDTO( DocumentDTO zipFile ) {
        super();
        this.setZipFile( zipFile );
    }

    /**
     * Gets the zip file.
     *
     * @return the zip file
     */
    public DocumentDTO getZipFile() {
        return zipFile;
    }

    /**
     * Sets the zip file.
     *
     * @param zipFile
     *         the new zip file
     */
    public void setZipFile( DocumentDTO zipFile ) {
        this.zipFile = zipFile;
    }

}
