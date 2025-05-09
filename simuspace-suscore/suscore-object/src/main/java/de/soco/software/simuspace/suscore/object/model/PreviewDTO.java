package de.soco.software.simuspace.suscore.object.model;

import java.util.List;

import de.soco.software.simuspace.suscore.data.model.ReportDTO;
import de.soco.software.simuspace.suscore.data.model.SectionDTO;

/**
 * The Class PreviewDTO.
 */
public class PreviewDTO {

    /**
     * The report DTO.
     */
    private ReportDTO reportDTO;

    /**
     * The section DT os.
     */
    private List< SectionDTO > sectionDTOs;

    /**
     * Gets the report DTO.
     *
     * @return the report DTO
     */
    public ReportDTO getReportDTO() {
        return reportDTO;
    }

    /**
     * Sets the report DTO.
     *
     * @param reportDTO
     *         the new report DTO
     */
    public void setReportDTO( ReportDTO reportDTO ) {
        this.reportDTO = reportDTO;
    }

    /**
     * Gets the section DT os.
     *
     * @return the section DT os
     */
    public List< SectionDTO > getSectionDTOs() {
        return sectionDTOs;
    }

    /**
     * Sets the section DT os.
     *
     * @param sectionDTOs
     *         the new section DT os
     */
    public void setSectionDTOs( List< SectionDTO > sectionDTOs ) {
        this.sectionDTOs = sectionDTOs;
    }

}
