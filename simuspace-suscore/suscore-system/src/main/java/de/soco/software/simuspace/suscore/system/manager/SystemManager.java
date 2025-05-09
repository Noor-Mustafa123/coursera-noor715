package de.soco.software.simuspace.suscore.system.manager;

import de.soco.software.simuspace.suscore.system.model.SystemDTO;

/**
 * The interface System manager.
 */
public interface SystemManager {

    /**
     * Gets system data count.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the system data count
     */
    SystemDTO getSystemDataCount( String userIdFromGeneralHeader );

}
