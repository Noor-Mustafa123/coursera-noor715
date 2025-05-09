package de.soco.software.simuspace.suscore.object.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.data.entity.ScheduleEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface ScheduleDAO.
 *
 * @author noman arshad
 */
public interface ScheduleDAO extends GenericDAO< ScheduleEntity > {

    /**
     * Gets list by properties.
     *
     * @param entityManager
     *         the entity manager
     * @param properties
     *         the properties
     * @param class1
     *         the class 1
     * @param b
     *         the b
     *
     * @return the list by properties
     */
    List< ScheduleEntity > getListByProperties( EntityManager entityManager, Map< String, Object > properties,
            Class< ScheduleEntity > class1, boolean b );

}
