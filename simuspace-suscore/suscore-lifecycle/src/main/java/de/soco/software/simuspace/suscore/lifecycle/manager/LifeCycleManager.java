package de.soco.software.simuspace.suscore.lifecycle.manager;

import java.util.List;

import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.data.common.model.StatusConfigDTO;
import de.soco.software.simuspace.suscore.lifecycle.model.LifeCycleDTO;
import de.soco.software.suscore.jsonschema.model.LifeCyclePolicyDTO;

/**
 * Interface to manager SUS Entity Status Life Cycle.
 *
 * @author Nosheen.Sharif
 */
public interface LifeCycleManager {

    /**
     * Gets the default status by life cycle name.
     *
     * @param lifeCycleId
     *         the life cycle id
     *
     * @return the default status by life cycle name
     */
    StatusDTO getDefaultStatusByLifeCycleId( String lifeCycleId );

    /**
     * Gets the status by life cycle name and status id.
     *
     * @param name
     *         the name
     * @param status
     *         the status
     *
     * @return the status by life cycle name and status id
     */
    StatusDTO getStatusByLifeCycleNameAndStatusId( String name, String status );

    /**
     * Gets the life cycle status by status id.
     *
     * @param lifeCycleStatus
     *         the life cycle status
     *
     * @return the life cycle status by status id
     */
    StatusConfigDTO getLifeCycleStatusByStatusId( String lifeCycleStatus );

    /**
     * Gets the owner visible status by policy id.
     *
     * @param lifeCycleId
     *         the life cycle id
     *
     * @return the owner visible status by policy id
     */
    List< String > getOwnerVisibleStatusByPolicyId( String lifeCycleId );

    /**
     * Gets the any visible status by policy id.
     *
     * @param lifeCycleId
     *         the life cycle id
     *
     * @return the any visible status by policy id
     */
    List< String > getAnyVisibleStatusByPolicyId( String lifeCycleId );

    /**
     * Gets the life cycle configuration by file name.
     *
     * @param fileName
     *         the file name
     *
     * @return the life cycle configuration by file name
     */
    List< LifeCycleDTO > getLifeCycleConfigurationByFileName( String fileName );

    /**
     * Gets the life cycle policy configuration by file name.
     *
     * @return the life cycle policy configuration by file name
     */
    List< LifeCyclePolicyDTO > getLifeCyclePolicyConfigurationByFileName();

    /**
     * Gets the default status of any object.
     *
     * @return the default status of any object
     */
    String getDefaultStatusOfJob();

}
