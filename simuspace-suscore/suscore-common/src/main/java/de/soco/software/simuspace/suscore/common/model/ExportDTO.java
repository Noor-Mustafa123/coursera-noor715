package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ExportDTO.
 *
 * @author Noman Arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ExportDTO {

    /**
     * The export path.
     */
    private ExportPath exportPath;

    /**
     * Gets the export path.
     *
     * @return the export path
     */
    public ExportPath getExportPath() {
        return exportPath;
    }

    /**
     * Sets the export path.
     *
     * @param exportPath
     *         the new export path
     */
    public void setExportPath( ExportPath exportPath ) {
        this.exportPath = exportPath;
    }

}