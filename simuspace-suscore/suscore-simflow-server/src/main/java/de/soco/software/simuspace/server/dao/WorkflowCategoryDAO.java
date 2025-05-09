package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowCategoryEntity;

/**
 * This interface perform all the operations of workflow category relation
 *
 * @author Nosheen.Sharif
 */
public interface WorkflowCategoryDAO {

    /**
     * Assign or remove categories to workflow. list(category) will be assigned to given workflowId. Previously assigned wil be removed
     *
     * @param entityManager
     *         the entity manager
     * @param list
     *         the list
     * @param workflowId
     *         the workflow id
     *
     * @return true, if successful
     */
    boolean assignCategoriesToWorkflow( EntityManager entityManager, List< WorkflowCategoryEntity > list, UUID workflowId );

}
