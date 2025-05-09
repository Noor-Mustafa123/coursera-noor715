package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.CustomVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.DesignVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.ObjectiveVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.VariableEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The interface Variable dao.
 */
public interface VariableDAO extends GenericDAO< VariableEntity > {

    /**
     * Adds the custom variables.
     *
     * @param entityManager
     *         the entity manager
     * @param variableEntities
     *         the variable entities
     */
    void addVariables( EntityManager entityManager, List< VariableEntity > variableEntities );

    /**
     * Gets the custom variable by id.
     *
     * @param entityManager
     *         the entity manager
     * @param variableId
     *         the custom variable id
     * @param clazz
     *         the clazz
     *
     * @return the custom variable by id
     */
    VariableEntity getVariableById( EntityManager entityManager, String variableId, Class< ? > clazz );

    /**
     * Gets the all filtered design variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     * @param filtersDTO
     *         the filters DTO
     * @param entityClazz
     *         the sub class of variable entity
     *
     * @return the all filtered design variables
     */
    List< ? > getAllFilteredVariables( EntityManager entityManager, String workflowId, String userId, FiltersDTO filtersDTO,
            Class< ? > entityClazz );

    /**
     * Gets the all filtered design variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered design variables
     */
    List< DesignVariableEntity > getAllFilteredDesignVariables( EntityManager entityManager, String workflowId, String userId,
            FiltersDTO filtersDTO );

    /**
     * Gets the all filtered design variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered design variables
     */
    List< ObjectiveVariableEntity > getAllFilteredObjectiveVariables( EntityManager entityManager, String workflowId, String userId,
            FiltersDTO filtersDTO );

    /**
     * Gets the all filtered design variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered design variables
     */
    List< CustomVariableEntity > getAllFilteredCustomVariables( EntityManager entityManager, String workflowId, String userId,
            FiltersDTO filtersDTO );

    /**
     * Gets the all custom variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     * @param variableSubClass
     *         the variable sub class
     *
     * @return the all custom variables
     */
    List< ? > getAllVariables( EntityManager entityManager, String workflowId, String userId, Class< ? > variableSubClass );

    /**
     * Read custom variable.
     *
     * @param entityManager
     *         the entity manager
     * @param variableId
     *         the custom variable id
     *
     * @return the custom variable entity
     */
    DesignVariableEntity readDesignVariable( EntityManager entityManager, UUID variableId );

    /**
     * Read custom variable.
     *
     * @param entityManager
     *         the entity manager
     * @param variableId
     *         the custom variable id
     *
     * @return the custom variable entity
     */
    ObjectiveVariableEntity readObjectiveVariable( EntityManager entityManager, UUID variableId );

    /**
     * Read custom variable.
     *
     * @param entityManager
     *         the entity manager
     * @param variableId
     *         the custom variable id
     *
     * @return the custom variable entity
     */
    CustomVariableEntity readCustomVariable( EntityManager entityManager, UUID variableId );

    /**
     * Read custom variable.
     *
     * @param entityManager
     *         the entity manager
     * @param variableId
     *         the custom variable id
     * @param variableSubClass
     *         the variable sub class
     *
     * @return the custom variable entity
     */
    VariableEntity readVariable( EntityManager entityManager, UUID variableId, Class< ? > variableSubClass );

    /**
     * Gets the all objective variables by workflow id.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param variableSubClass
     *         the entity clazz
     *
     * @return the all objective variables by workflow id
     */
    List< ? > getAllVariablesByWorkflowIdAndClass( EntityManager entityManager, String workflowId, Class< ? > variableSubClass );

    /**
     * Gets the all objective variables by workflow id.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     *
     * @return the all objective variables by workflow id
     */
    List< ObjectiveVariableEntity > getAllObjectiveVariablesByWorkflowId( EntityManager entityManager, String workflowId );

    /**
     * Gets the all custom variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     *
     * @return the all custom variables
     */
    List< DesignVariableEntity > getAllDesignVariables( EntityManager entityManager, String workflowId, String userId );

    /**
     * Gets the all custom variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     *
     * @return the all custom variables
     */
    List< ObjectiveVariableEntity > getAllObjectiveVariables( EntityManager entityManager, String workflowId, String userId );

    /**
     * Gets the all custom variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     *
     * @return the all custom variables
     */
    List< CustomVariableEntity > getAllCustomVariables( EntityManager entityManager, String workflowId, String userId );

}
