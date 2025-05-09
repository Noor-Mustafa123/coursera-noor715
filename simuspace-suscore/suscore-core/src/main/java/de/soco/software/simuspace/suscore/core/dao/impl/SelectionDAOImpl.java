package de.soco.software.simuspace.suscore.core.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.Map;

import de.soco.software.simuspace.suscore.core.dao.SelectionDAO;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Class SelectionDAO will be responsible for all the database operations necessary for dealing with the selections.
 *
 * @author Noman Arshad
 */
public class SelectionDAOImpl extends AbstractGenericDAO< SelectionEntity > implements SelectionDAO {

    @Override
    public void deleteAllSelectionsWithOrigin( EntityManager entityManager, String origin ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( "origin", origin );
        deleteListByProperties( entityManager, properties, SelectionEntity.class );
    }

}
