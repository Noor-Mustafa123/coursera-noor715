package de.soco.software.simuspace.wizards.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.LoadCaseEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The interface Load case dao.
 */
public interface LoadCaseDAO extends GenericDAO< LoadCaseEntity > {

    /**
     * Gets non deleted object list by property.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the non deleted object list by property
     */
    List< LoadCaseEntity > getNonDeletedObjectListByProperty( EntityManager entityManager, UUID id );

}
