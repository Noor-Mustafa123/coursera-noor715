package de.soco.software.simuspace.suscore.core.manager;

import java.util.UUID;

/**
 * The Interface LinkManager.
 *
 * @author Ahsan.Khan
 */
public interface LinkManager {

    /**
     * Objects linking.
     *
     * @param userId
     *         the user id
     * @param linkFrom
     *         (object selection from) the link from
     * @param linkTo
     *         (object selection to) the link to
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean objectsLinking( UUID userId, UUID linkFrom, UUID linkTo );

    /**
     * Removes the link object by id.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param selectionId
     *         the selection id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean removeLinkObjectById( UUID userId, UUID objectId, UUID selectionId );

    /**
     * Gets the linked relation by child id.
     *
     * @param child
     *         the child
     *
     * @return the linked relation by child id
     *
     * @apiNote To be used in service calls only
     */
    boolean getLinkedRelationByChildId( UUID child );

}
