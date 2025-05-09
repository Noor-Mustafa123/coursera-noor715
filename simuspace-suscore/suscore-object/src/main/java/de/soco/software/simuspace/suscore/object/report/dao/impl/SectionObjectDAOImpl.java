package de.soco.software.simuspace.suscore.object.report.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.SectionObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.object.report.dao.SectionObjectDAO;

/**
 * The Class SectionObjectDAOImpl.
 *
 * @author Ahsan.Khan
 */
public class SectionObjectDAOImpl extends AbstractGenericDAO< SectionObjectEntity > implements SectionObjectDAO {

    /**
     * The Constant SECTION_ID.
     */
    private static final String SECTION_ID = "sectionEntity.id";

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SectionObjectEntity > getSectionObjectEntityBySectionId( EntityManager entityManager, UUID sectionId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( SECTION_ID, sectionId );
        return getListByPropertiesJpa( entityManager, properties, SectionObjectEntity.class, false );
    }

}
