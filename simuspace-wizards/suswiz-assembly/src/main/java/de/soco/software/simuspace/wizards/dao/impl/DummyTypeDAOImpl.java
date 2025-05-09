package de.soco.software.simuspace.wizards.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.Map;

import de.soco.software.simuspace.suscore.data.entity.DummyTypeEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.wizards.dao.DummyTypeDAO;

/**
 * The Class DummyTypeDAOImpl.
 */
public class DummyTypeDAOImpl extends AbstractGenericDAO< DummyTypeEntity > implements DummyTypeDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public DummyTypeEntity getDummyTypeByName( EntityManager entityManager, String dummyType, String solverType ) {

        Map< String, Object > properties = new HashMap<>();
        properties.put( "dummyTypeName", dummyType );
        properties.put( "solverType", solverType );
        return getUniqueObjectByProperties( entityManager, properties, DummyTypeEntity.class );
    }

}
