package de.soco.software.simuspace.suscore.data.framework.interaction.service.impl;

import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.framework.interaction.manager.FrameWorkInteractionManager;
import de.soco.software.simuspace.suscore.data.framework.interaction.service.FrameWorkInteractionService;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;

/**
 * service end points which gets the status of bundles.
 */
public class FrameWorkInteractionServiceImpl extends SuSBaseService implements FrameWorkInteractionService {

    /**
     * The frame work interaction manager.
     */
    private FrameWorkInteractionManager frameWorkInteractionManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getStatus() {
        try {
            return ResponseUtils.success( frameWorkInteractionManager.getStatus() );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the frame work interaction manager.
     *
     * @return the frame work interaction manager
     */
    public FrameWorkInteractionManager getFrameWorkInteractionManager() {
        return frameWorkInteractionManager;
    }

    /**
     * Sets the frame work interaction manager.
     *
     * @param frameWorkInteractionManager
     *         the new frame work interaction manager
     */
    public void setFrameWorkInteractionManager( FrameWorkInteractionManager frameWorkInteractionManager ) {
        this.frameWorkInteractionManager = frameWorkInteractionManager;
    }

}
