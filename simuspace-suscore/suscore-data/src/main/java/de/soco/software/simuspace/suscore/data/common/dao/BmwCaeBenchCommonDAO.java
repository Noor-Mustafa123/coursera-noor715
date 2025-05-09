package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface DummyFileCommonDAO.
 *
 * @author noman arshad
 */
public interface BmwCaeBenchCommonDAO extends GenericDAO< BmwCaeBenchEntity > {

    /**
     * Gets the latest non deleted objects by list of ids.
     *
     * @param entityManager
     *         the entity manager
     * @param childs
     *         the childs
     *
     * @return the latest non deleted objects by list of ids
     */
    List< BmwCaeBenchEntity > getLatestNonDeletedObjectsByListOfIds( EntityManager entityManager, List< UUID > childs );

}
