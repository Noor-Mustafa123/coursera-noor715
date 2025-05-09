package de.soco.software.simuspace.suscore.common.base;

import org.apache.cxf.phase.PhaseInterceptorChain;

import de.soco.software.simuspace.suscore.common.util.BundleUtils;

/**
 * The Class responsible to provide base utility functions for all managers.
 *
 * @author M.Nasir.Farooq
 */
public class BaseManager {

    /**
     * Instantiates a new service.
     *
     * @param serviceClass
     *         the service class
     */
    public BaseManager( Class< ? > serviceClass ) {

    }

    /**
     * Gets the user id from header message.
     *
     * @return the user id from general header
     */
    protected String getUserIdFromMessage() {
        return BundleUtils.getUserIdFromMessage( PhaseInterceptorChain.getCurrentMessage() );
    }

    /**
     * Instantiates a new manager.
     */
    public BaseManager() {
    }

}
