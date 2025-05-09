package de.soco.software.simuspace.suscore.object.manager;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.common.model.BreadCrumbDTO;

/**
 * Interface To Handle All operation related To Bread Crumb.
 */
public interface BreadCrumbManager {

    /**
     * Create Bread Crumb.
     *
     * @param userId
     *         the user id
     * @param uuid
     *         the uuid
     * @param api
     *         the api
     *
     * @return BreadCrumbDTO
     *
     * @apiNote To be used in service calls only
     */
    BreadCrumbDTO createBreadCrumb( String userId, String uuid, String api );

    /**
     * Create Bread Crumb.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param uuid
     *         the uuid
     * @param api
     *         the api
     *
     * @return BreadCrumbDTO bread crumb dto
     */
    BreadCrumbDTO createBreadCrumb( EntityManager entityManager, String userId, String uuid, String api );

    /**
     * Get Group Users Bread Crumb.
     *
     * @param userId
     *         the user id
     * @param uuid
     *         the uuid
     *
     * @return BreadCrumbDTO
     *
     * @apiNote To be used in service calls only
     */
    BreadCrumbDTO getGroupUsersBreadCrumb( String userId, String uuid );

    /**
     * Gets user groups bread crumb.
     *
     * @param userId
     *         the user id
     * @param uuid
     *         the uuid
     *
     * @return the user groups bread crumb
     */
    BreadCrumbDTO getUserGroupsBreadCrumb( String userId, String uuid );

    /**
     * Gets role groups bread crumb.
     *
     * @param userId
     *         the user id
     * @param uuid
     *         the uuid
     *
     * @return the role groups bread crumb
     */
    BreadCrumbDTO getRoleGroupsBreadCrumb( String userId, String uuid );

    /**
     * Create Run Workflow Bread Crumb.
     *
     * @param userId
     *         the user id
     * @param uuid
     *         the uuid
     * @param api
     *         the api
     *
     * @return BreadCrumbDTO
     */
    BreadCrumbDTO createRunWorkflowBreadCrumb( String userId, String uuid, String api );

    /**
     * Create Run Scheme Bread Crumb.
     *
     * @param userId
     *         the user id
     * @param uuid
     *         the uuid
     * @param api
     *         the api
     *
     * @return BreadCrumbDTO
     */
    BreadCrumbDTO createRunSchemeBreadCrumb( String userId, String uuid, String api );

}
