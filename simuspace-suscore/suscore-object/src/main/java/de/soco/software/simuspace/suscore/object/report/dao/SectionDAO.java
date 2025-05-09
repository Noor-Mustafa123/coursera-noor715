package de.soco.software.simuspace.suscore.object.report.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.SectionEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface SectionDAO.
 *
 * @author Ahsan.Khan
 */
public interface SectionDAO extends GenericDAO< SectionEntity > {

    /**
     * Gets the sections by report id.
     *
     * @param entityManager
     *         the entity manager
     * @param reportId
     *         the report id
     *
     * @return the sections by report id
     */
    List< SectionEntity > getSectionsByReportId( EntityManager entityManager, UUID reportId );

}
