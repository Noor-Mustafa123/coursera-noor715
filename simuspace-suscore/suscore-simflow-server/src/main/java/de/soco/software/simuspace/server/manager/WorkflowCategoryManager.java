package de.soco.software.simuspace.server.manager;

import java.util.List;
import java.util.UUID;

/**
 * To manage the Category and workflow Relation operation to Dao layer
 *
 * @author Nosheen.Sharif
 */
public interface WorkflowCategoryManager {

    /**
     * Assign or De-assign categories to workflow. list(category) will be assigned to given workflowId. Previously assigned wil be removed
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param categoryIdsList
     *         the category ids list
     *
     * @return true, if successful
     */
    boolean assignCategoriesToWorkflow( UUID userId, String workflowId, List< String > categoryIdsList );

}
