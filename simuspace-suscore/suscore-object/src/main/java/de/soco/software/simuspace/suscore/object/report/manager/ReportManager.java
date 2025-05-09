package de.soco.software.simuspace.suscore.object.report.manager;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.data.model.SectionDTO;

import j2html.tags.ContainerTag;

/**
 * The Interface ReportManager.
 *
 * @author Ahsan.Khan
 */
public interface ReportManager {

    /**
     * Creates the section.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param sectionDTO
     *         the section DTO
     *
     * @return the section DTO
     *
     * @apiNote To be used in service calls only
     */
    SectionDTO createSection( String userIdFromGeneralHeader, SectionDTO sectionDTO, UUID reportId );

    /**
     * Gets the section list.
     *
     * @param userId
     *         the user id
     * @param reportId
     *         the report id
     *
     * @return the section list
     *
     * @apiNote To be used in service calls only
     */
    List< SectionDTO > getSectionList( String userId, UUID reportId );

    /**
     * Creates the section UI.
     *
     * @param userId
     *         the user id
     *
     * @return the list
     */
    UIForm createSectionUI( String userId );

    /**
     * Edits the section UI.
     *
     * @param userId
     *         the user id
     * @param sectionId
     *         the section id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editSectionUI( String userId, UUID sectionId );

    /**
     * Update section.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param sectionDTO
     *         the section DTO
     *
     * @return the section DTO
     *
     * @apiNote To be used in service calls only
     */
    SectionDTO updateSection( String userIdFromGeneralHeader, SectionDTO sectionDTO );

    /**
     * Generate section preview.
     *
     * @param sectionId
     *         the section id
     *
     * @return the string
     *
     * @throws IOException
     *         the io exception
     * @apiNote To be used in service calls only
     */
    ContainerTag generateSectionPreview( UUID sectionId ) throws IOException;

    /**
     * Generate section preview.
     *
     * @param entityManager
     *         the entity manager
     * @param sectionId
     *         the section id
     *
     * @return the string
     *
     * @throws IOException
     *         the io exception
     */
    ContainerTag generateSectionPreview( EntityManager entityManager, UUID sectionId ) throws IOException;

    /**
     * Preview report.
     *
     * @param reportId
     *         the report id
     *
     * @return the string
     *
     * @apiNote To be used in service calls only
     */
    ContainerTag previewReport( UUID reportId );

    /**
     * Preview report pdf.
     *
     * @param reportId
     *         the report id
     *
     * @return the file
     *
     * @apiNote To be used in service calls only
     */
    File previewReportPdf( UUID reportId );

    /**
     * Preview section pdf.
     *
     * @param sectionId
     *         the section id
     *
     * @return the file
     *
     * @apiNote To be used in service calls only
     */
    File previewSectionPdf( UUID sectionId );

    /**
     * Preview report docx.
     *
     * @param reportId
     *         the report id
     *
     * @return the file
     *
     * @throws IOException
     *         the io exception
     * @apiNote To be used in service calls only
     */
    File previewReportDocx( UUID reportId ) throws IOException;

    /**
     * Download preview report pdf.
     *
     * @param reportId
     *         the report id
     *
     * @return the string
     */
    String downloadPreviewReportPdf( UUID reportId );

    /**
     * Download preview section pdf.
     *
     * @param sectionId
     *         the section id
     *
     * @return the string
     */
    String downloadPreviewSectionPdf( UUID sectionId );

    /**
     * Download preview report docx.
     *
     * @param reportId
     *         the report id
     *
     * @return the string
     */
    String downloadPreviewReportDocx( UUID reportId );

    /**
     * Download preview section docx.
     *
     * @param sectionId
     *         the section id
     *
     * @return the string
     */
    String downloadPreviewSectionDocx( UUID sectionId );

    /**
     * Preview section docx.
     *
     * @param sectionId
     *         the section id
     *
     * @return the file
     */
    File previewSectionDocx( UUID sectionId );

    /**
     * Gets the section order list.
     *
     * @param userId
     *         the user id
     * @param reportId
     *         the report id
     * @param orderIds
     *         the order ids
     *
     * @return the section order list
     *
     * @apiNote To be used in service calls only
     */
    List< SectionDTO > getSectionOrderList( String userId, UUID reportId, List< UUID > orderIds );

}
