package de.soco.software.simuspace.suscore.rights.service.rest.impl;

import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.rights.manager.RightsManager;
import de.soco.software.simuspace.suscore.rights.service.rest.RightsService;

/**
 * The type Rights service.
 */
public class RightsServiceImpl extends SuSBaseService implements RightsService {

    /**
     * The Rights manager.
     */
    private RightsManager rightsManager;

    @Override
    public Response getAllRightsList() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.RIGHTS.getKey() ) ),
                    rightsManager.getAllRightsList( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets rights manager.
     *
     * @return the rights manager
     */
    public RightsManager getRightsManager() {
        return rightsManager;
    }

    /**
     * Sets rights manager.
     *
     * @param rightsManager
     *         the rights manager
     */
    public void setRightsManager( RightsManager rightsManager ) {
        this.rightsManager = rightsManager;
    }

}
