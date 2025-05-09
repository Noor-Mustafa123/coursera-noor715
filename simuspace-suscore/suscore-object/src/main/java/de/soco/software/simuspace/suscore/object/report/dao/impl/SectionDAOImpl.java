package de.soco.software.simuspace.suscore.object.report.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.SectionEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.object.report.dao.SectionDAO;

/**
 * The Class SectionDAOImpl.
 *
 * @author Ahsan.Khan
 */
public class SectionDAOImpl extends AbstractGenericDAO< SectionEntity > implements SectionDAO {

    /**
     * The Constant REPORT.
     */
    private static final String REPORT = "reportEntity.composedId.id";

    /**
     * The Constant ORDER.
     */
    private static final String ORDER = "sectionOrder";

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SectionEntity > getSectionsByReportId( EntityManager entityManager, UUID reportId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( REPORT, reportId );
        return getListByPropertiesAescendingOrder( entityManager, properties, SectionEntity.class, ORDER );
    }

}
