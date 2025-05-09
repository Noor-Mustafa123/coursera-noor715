package de.soco.software.simuspace.server.manager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.workflow.dto.WorkflowDTO;

/**
 * To manage the addition and removal of objects to favorites. Functionality no longer in use
 *
 * @author Zeeshan jamal
 */
@Deprecated( forRemoval = true )
public interface FavoriteManager {

    /**
     * Add work flow to the favorite
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the boolean
     */
    WorkflowDTO addWorkflowToFavorite( UUID userId, String workflowId );

    /**
     * Gets the WorkFlows.
     *
     * @param userId
     *         the user id
     *
     * @return the WorkFlows
     */
    List< WorkflowDTO > getFavoriteWorkflowListByUserId( UUID userId );

}
