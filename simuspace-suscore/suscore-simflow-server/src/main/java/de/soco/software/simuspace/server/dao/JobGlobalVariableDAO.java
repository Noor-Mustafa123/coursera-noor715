package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobGlobalVariablesEntity;

/**
 * The Interface JobGlobalVariableDAO.
 */
public interface JobGlobalVariableDAO extends GenericDAO< JobGlobalVariablesEntity > {

    /**
     * Gets the JobGlobalVariableEntity By jobEntity and userId
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntityId
     *         the jobEntityId
     * @param userId
     *         the userId
     *
     * @return the JobGlobalVariableEntity
     */
    JobGlobalVariablesEntity getJobGlobalVariablesByJobEntityAndUserId( EntityManager entityManager, UUID jobEntityId, UUID userId );

}