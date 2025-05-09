package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.TemplateEntity;

/**
 * The Interface TemplateDAO.
 *
 * @author Fahad Rafi
 */
public interface TemplateDAO {

    /**
     * Gets the template list by selection selectionId.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selectionId
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the template list by selection selectionId
     */
    List< TemplateEntity > getTemplateListBySelectionId( EntityManager entityManager, UUID selectionId, FiltersDTO filtersDTO );

    /**
     * Save template.
     *
     * @param entityManager
     *         the entity manager
     * @param templateEntity
     *         the template entity
     *
     * @return the template entity
     */
    TemplateEntity saveTemplate( EntityManager entityManager, TemplateEntity templateEntity );

    /**
     * Gets the template list by selection selectionId.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selectionId
     *
     * @return the template list by selection selectionId
     */
    List< TemplateEntity > getTemplateListBySelectionId( EntityManager entityManager, UUID selectionId );

    /**
     * Gets the template.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the template
     */
    TemplateEntity getTemplate( EntityManager entityManager, UUID id );

    /**
     * Delete template.
     *
     * @param entityManager
     *         the entity manager
     * @param templateEntity
     *         the template entity
     *
     * @return the boolean
     */
    Boolean deleteTemplate( EntityManager entityManager, TemplateEntity templateEntity );

    /**
     * Update template.
     *
     * @param entityManager
     *         the entity manager
     * @param templateEntity
     *         the template entity
     *
     * @return the template entity
     */
    TemplateEntity updateTemplate( EntityManager entityManager, TemplateEntity templateEntity );

}
