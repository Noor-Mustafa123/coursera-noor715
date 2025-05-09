package de.soco.software.simuspace.suscore.object.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.data.entity.ScheduleEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.object.dao.ScheduleDAO;

/**
 * The Class ScheduleDAOImpl.
 *
 * @author noman arshad
 */
public class ScheduleDAOImpl extends AbstractGenericDAO< ScheduleEntity > implements ScheduleDAO {

    @Override
    public List< ScheduleEntity > getListByProperties( EntityManager entityManager, Map< String, Object > properties,
            Class< ScheduleEntity > class1, boolean b ) {
        return getListByPropertiesJpa( entityManager, properties, class1, b );
    }

}
