package de.soco.software.simuspace.server.manager;

import de.soco.software.simuspace.server.model.AlgorithmDTO;

/**
 * The interface Algorithm manager.
 */
public interface AlgorithmManager {

    /**
     * Gets algorithms data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the algorithms data
     */
    AlgorithmDTO getAlgorithmsDataCount( String userIdFromGeneralHeader );

}
