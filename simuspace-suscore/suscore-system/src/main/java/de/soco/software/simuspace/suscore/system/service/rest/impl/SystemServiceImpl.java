package de.soco.software.simuspace.suscore.system.service.rest.impl;

import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.system.manager.SystemManager;
import de.soco.software.simuspace.suscore.system.service.rest.SystemService;

/**
 * The type System service.
 */
public class SystemServiceImpl extends SuSBaseService implements SystemService {

    /**
     * The System manager.
     */
    private SystemManager systemManager;

    @Override
    public Response getSystemDataCount() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.SYSTEM_COUNT_DATA_FETCHED.getKey() ),
                    systemManager.getSystemDataCount( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Sets system manager.
     *
     * @param systemManager
     *         the system manager
     */
    public void setSystemManager( SystemManager systemManager ) {
        this.systemManager = systemManager;
    }

}
