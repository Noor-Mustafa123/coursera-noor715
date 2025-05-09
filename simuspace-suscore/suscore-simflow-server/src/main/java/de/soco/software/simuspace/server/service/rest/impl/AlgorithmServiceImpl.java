package de.soco.software.simuspace.server.service.rest.impl;

import javax.ws.rs.core.Response;

import de.soco.software.simuspace.server.manager.AlgorithmManager;
import de.soco.software.simuspace.server.service.rest.AlgorithmService;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;

/**
 * The type Algorithm service.
 */
public class AlgorithmServiceImpl extends SuSBaseService implements AlgorithmService {

    /**
     * The Algorithm manager.
     */
    private AlgorithmManager algorithmManager;

    @Override
    public Response getAlgorithmsDataCount() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.ALGORITHMS_COUNT_DATA_FETCHED.getKey() ),
                    algorithmManager.getAlgorithmsDataCount( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets algorithm manager.
     *
     * @return the algorithm manager
     */
    public AlgorithmManager getAlgorithmManager() {
        return algorithmManager;
    }

    /**
     * Sets algorithm manager.
     *
     * @param algorithmManager
     *         the algorithm manager
     */
    public void setAlgorithmManager( AlgorithmManager algorithmManager ) {
        this.algorithmManager = algorithmManager;
    }

}
