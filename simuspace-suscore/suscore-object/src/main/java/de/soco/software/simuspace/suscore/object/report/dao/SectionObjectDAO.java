package de.soco.software.simuspace.suscore.object.report.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.SectionObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface SectionObjectDAO.
 *
 * @author Ahsan.Khan
 */
public interface SectionObjectDAO extends GenericDAO< SectionObjectEntity > {

    /**
     * Gets the section object entity by section id.
     *
     * @param entityManager
     *         the entity manager
     * @param sectionId
     *         the section id
     *
     * @return the section object entity by section id
     */
    List< SectionObjectEntity > getSectionObjectEntityBySectionId( EntityManager entityManager, UUID sectionId );

}
