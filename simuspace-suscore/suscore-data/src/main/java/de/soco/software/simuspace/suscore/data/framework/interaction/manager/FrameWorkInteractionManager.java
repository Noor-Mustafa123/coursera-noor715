package de.soco.software.simuspace.suscore.data.framework.interaction.manager;

import de.soco.software.simuspace.suscore.data.common.model.FrameWorkStatusDTO;

/**
 * The Interface FrameWorkInteractionManager manages the status of bundles.
 */
public interface FrameWorkInteractionManager {

    /**
     * Gets the status.
     *
     * @return the status
     *
     * @apiNote To be used in service calls only
     */
    FrameWorkStatusDTO getStatus();

}
