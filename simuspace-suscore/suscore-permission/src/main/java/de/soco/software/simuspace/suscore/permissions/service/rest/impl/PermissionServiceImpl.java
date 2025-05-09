package de.soco.software.simuspace.suscore.permissions.service.rest.impl;

import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.service.rest.PermissionService;

/**
 * This Class provides the Permission API implementation for authorizing to the system.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class PermissionServiceImpl extends SuSBaseService implements PermissionService {

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response isPermitted( String resource ) {
        try {
            return ResponseUtils.success( permissionManager.isPermitted( getUserIdFromGeneralHeader(), resource ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

}