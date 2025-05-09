package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * This interface perform all the operations of workflows
 *
 * @author Nosheen.Sharif
 */
public interface WorkflowDAO extends GenericDAO< WorkflowEntity > {

    /**
     * Gets the workflow with latest Version list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the workflow list
     */
    List< WorkflowEntity > getWorkflowList( EntityManager entityManager );

    /**
     * Save Workflow to Database.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowEntity
     *         the workflow entity
     *
     * @return the workflow entity
     */
    WorkflowEntity saveWorkflow( EntityManager entityManager, WorkflowEntity workflowEntity );

    /**
     * Update workflow workflow entity.
     *
     * @param entityManager
     *         the entity manager
     * @param newEntity
     *         the new entity
     *
     * @return the workflow entity
     */
    WorkflowEntity updateWorkflow( EntityManager entityManager, WorkflowEntity newEntity );

    /**
     * Gets the workflow versions by id.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow versions for the given workflow id
     */
    List< WorkflowEntity > getWorkflowVersionsById( EntityManager entityManager, UUID workflowId );

    WorkflowEntity getWorkflowByIdAndVersionId( EntityManager entityManager, UUID workflowId, int versionId );

    /**
     * Gets the workflow Ids list by category id.
     *
     * @param entityManager
     *         the entity manager
     * @param categoryId
     *         the category id
     *
     * @return the workflow list by category id
     */
    List< UUID > getWorkflowListByCategoryId( EntityManager entityManager, UUID categoryId );

    /**
     * Gets the workflow ids list with no category.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the workflow list with no category
     */
    List< UUID > getWorkflowListWithoutCategory( EntityManager entityManager );

    /**
     * Checks if is workflow exist in category.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     * @param categoryId
     *         the category id
     *
     * @return true, if is workflow exist in category
     */
    boolean isWorkflowExistInCategory( EntityManager entityManager, WorkflowEntity entity, UUID categoryId );

    /**
     * Gets workflow versions without definition.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the workflow versions without definition
     */
    List< WorkflowEntity > getWorkflowVersionsWithoutDefinition( EntityManager entityManager, UUID id );

    /**
     * Gets the WorkflowEntity list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the WorkflowEntity list
     */
    List< UUID > getWorkflowIdsList( EntityManager entityManager );

    boolean isAlreadyWipWorkflowExist( EntityManager entityManager, UUID curentUserId, UUID workflowId );

    boolean isAllVersionsOfWorkflowDeleted( EntityManager entityManager, UUID workflowId );

    long getCompletedJobCountByWorkflowIdAndVersion( EntityManager entityManager, UUID workflowId, int verisonId );

    long getTotalJobCountByWorkflowIdAndVersion( EntityManager entityManager, UUID workflowId, int verisonId );

    /**
     * Gets all property values by parent id.
     *
     * @param entityManager
     *         the entity manager
     * @param columnName
     *         the column name
     * @param uuid
     *         the uuid
     *
     * @return the all property values by parent id
     */
    List< Object > getAllPropertyValuesByParentId( EntityManager entityManager, String columnName, UUID uuid );

}
