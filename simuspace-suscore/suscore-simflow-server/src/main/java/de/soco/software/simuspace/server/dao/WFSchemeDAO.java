package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.data.entity.WFSchemeEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

public interface WFSchemeDAO extends GenericDAO< WFSchemeEntity > {

    /**
     * Gets latest non deleted wf scheme list by property.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the latest non deleted wf scheme list by property
     */
    List< WFSchemeEntity > getLatestNonDeletedWFSchemeListByProperty( EntityManager entityManager, String propertyName, Object value );

    List< WFSchemeEntity > getLatestNonDeletedWFSchemeList( EntityManager entityManager );

}
