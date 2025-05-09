package de.soco.software.simuspace.suscore.core.dao;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The SelectionDAO The Interface will be responsible for all the database operations necessary for dealing with the selections.
 *
 * @author Noman Arshad
 */
public interface SelectionDAO extends GenericDAO< SelectionEntity > {

    /**
     * Delete all selections with origin.
     *
     * @param entityManager
     *         the entity manager
     * @param origin
     *         the origin
     */
    void deleteAllSelectionsWithOrigin( EntityManager entityManager, String origin );

}
