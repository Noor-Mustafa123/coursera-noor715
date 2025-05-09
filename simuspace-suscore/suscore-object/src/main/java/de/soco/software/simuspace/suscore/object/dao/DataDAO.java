package de.soco.software.simuspace.suscore.object.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface will be responsible for all the database operations necessary for dealing with the User.
 */
public interface DataDAO extends GenericDAO< DataObjectEntity > {

    /**
     * Gets latest non deleted object via cache.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the latest non deleted object via cache
     */
    SuSEntity getLatestNonDeletedObjectViaCache( EntityManager entityManager, UUID id );

}
