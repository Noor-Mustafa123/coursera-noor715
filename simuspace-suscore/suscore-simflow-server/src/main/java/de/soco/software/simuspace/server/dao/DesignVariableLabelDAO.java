package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.DesignVariableLabelEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The interface Design variable label dao.
 */
public interface DesignVariableLabelDAO extends GenericDAO< DesignVariableLabelEntity > {

    /**
     * Gets design variable label by workflow id.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable label by workflow id
     */
    DesignVariableLabelEntity getDesignVariableLabelByWorkflowId( EntityManager entityManager, UUID workflowId );

}
