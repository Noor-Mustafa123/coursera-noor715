package de.soco.software.simuspace.suscore.rights.manager;

import de.soco.software.simuspace.suscore.rights.model.RightsListDTO;

/**
 * The interface Rights manager.
 */
public interface RightsManager {

    /**
     * Gets all rights list.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the all rights list
     */
    RightsListDTO getAllRightsList( String userIdFromGeneralHeader );

}
