package de.soco.software.simuspace.wizards.dao;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.data.entity.DummyTypeEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface DummyTypeDAO.
 */
public interface DummyTypeDAO extends GenericDAO< DummyTypeEntity > {

    /**
     * Gets the dummy type by name.
     *
     * @param entityManager
     *         the entity manager
     * @param dummyType
     *         the dummy type
     * @param solverType
     *         the solver type
     *
     * @return the dummy type by name
     */
    DummyTypeEntity getDummyTypeByName( EntityManager entityManager, String dummyType, String solverType );

}
