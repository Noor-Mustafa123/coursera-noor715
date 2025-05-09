package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.JobIdsEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface JobIdsDAO.
 */
public interface JobIdsDAO extends GenericDAO< JobIdsEntity > {

    /**
     * Save job ids.
     *
     * @param entityManager
     *         the entity manager
     * @param jobIdsEntity
     *         the job ids entity
     *
     * @return the job ids entity
     */
    JobIdsEntity saveJobIds( EntityManager entityManager, JobIdsEntity jobIdsEntity );

    /**
     * Gets job ids entity by uuid.
     *
     * @param entityManager
     *         the entity manager
     * @param uuid
     *         the uuid
     *
     * @return the job ids entity by uuid
     */
    JobIdsEntity getJobIdsEntityByUUID( EntityManager entityManager, UUID uuid );

}
