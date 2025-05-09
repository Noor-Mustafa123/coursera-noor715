package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchNodeEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface BmwCaeBenchNodeDAO.
 *
 * @author noman arshad
 */
public interface BmwCaeBenchNodeDAO extends GenericDAO< BmwCaeBenchNodeEntity > {

    /**
     * Gets the bmw cae bench node by name.
     *
     * @param entityManager
     *         the entity manager
     * @param node
     *         the node
     *
     * @return the bmw cae bench node by name
     */
    BmwCaeBenchNodeEntity getBmwCaeBenchNodeByName( EntityManager entityManager, String node );

}
