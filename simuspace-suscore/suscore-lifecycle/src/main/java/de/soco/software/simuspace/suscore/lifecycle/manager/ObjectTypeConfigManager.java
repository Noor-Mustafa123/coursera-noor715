package de.soco.software.simuspace.suscore.lifecycle.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.ObjectType;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.util.ConfigUtil;
import de.soco.software.simuspace.suscore.data.common.model.ProjectConfiguration;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.ObjectJsonSchemaEntity;

/**
 * The Interface ObjectTypeConfigManager.
 *
 * @author Ahmar Nadeem
 * @Description This class is actually used for caching the project configuration in memory. It loads the configuration from the class path,
 * validates if the json is properly formatted. Then loads the existing configuration from database (if any) and compares the two. If it
 * doesn't violate the rules, the configuration is cached otherwise the module will not be initialized/run.
 *
 * It takes care of certain scenario's.
 * @SCENARIO_1: All the names of the objects must not violate the java identifiers.
 * @SCENARIO_2: There must not be any data loss. For example, an existing custom attribute is removed from the new configuration, any
 * default value of boolean attributes changed etc.
 * @SCENARIO_3: All the elements of Contains and Links must be there in the config file.
 */
public interface ObjectTypeConfigManager {

    /**
     * Gets the JSON object with id.
     *
     * @param classId
     *         the object class id
     * @param configType
     *         the config type
     *
     * @return the JSON object with id
     *
     * @Description A helper function to filter the cached map and return the required class/object.
     */
    SuSObjectModel getObjectTypeByIdAndConfigName( String classId, String configType );

    /**
     * Gets the container object types by object type id.
     *
     * @param objectTypeId
     *         the object type id
     * @param configType
     *         the config type
     *
     * @return the container object types of project by config name
     */
    List< SuSObjectModel > getContainerObjectTypesByObjectTypeId( UUID objectTypeId, String configType );

    /**
     * Load last json schema configuration.
     *
     * @param entityManager
     *         the entity manager
     * @param shemaName
     *         the shema name
     *
     * @return the object json schema entity
     */
    ObjectJsonSchemaEntity loadLastJsonSchemaConfiguration( EntityManager entityManager, String shemaName );

    /**
     * Save new configuration.
     *
     * @param entityManager
     *         the entity manager
     * @param masterEntity
     *         the master entity
     *
     * @return the object json schema entity
     */
    ObjectJsonSchemaEntity saveNewConfiguration( EntityManager entityManager, ObjectJsonSchemaEntity masterEntity );

    /**
     * Gets the JSON objects by type.
     *
     * @param objectType
     *         the object type
     *
     * @return the JSON objects by type
     */
    List< SuSObjectModel > getObjectTypesByConfigName( String objectType );

    /**
     * Gets project model by config label in master config.
     *
     * @param objectType
     *         the object type
     *
     * @return the project model by config label
     */
    SuSObjectModel getProjectModelByConfigLabel( String objectType );

    /**
     * Gets the default status by object type id.
     *
     * @param typeId
     *         the type id
     * @param configType
     *         the config type
     *
     * @return the default status by object type id
     */
    StatusDTO getDefaultStatusByObjectTypeId( String typeId, String configType );

    /**
     * Gets the status by idand object type.
     *
     * @param typeId
     *         the type id
     * @param lifeCycleStatus
     *         the life cycle status
     * @param configType
     *         the config type
     *
     * @return the status by idand object type
     */
    StatusDTO getStatusByIdandObjectType( UUID typeId, String lifeCycleStatus, String configType );

    /**
     * Load last project configuration.
     *
     * @param entityManager
     *         the entity manager
     * @param configName
     *         the config name
     *
     * @return the project configuration
     */
    ProjectConfiguration getProjectConfiguration( EntityManager entityManager, String configName );

    /**
     * Gets the master configuration file names.
     *
     * @return the master configuration file names
     */
    List< ConfigUtil.Config > getMasterConfigurationFileNames();

    /**
     * Gets the master configuration file names for work flows.
     *
     * @return the master configuration file names for work flows
     */
    List< ConfigUtil.Config > getMasterConfigurationFileNamesForWorkFlows();

    /**
     * Gets the all types.
     *
     * @param configFileName
     *         the config file name
     *
     * @return the all types
     */
    List< ObjectType > getTypesFromConfiguration( final String configFileName );

    /**
     * Gets the all types from configuration.
     *
     * @return the all types from configuration
     */
    List< ObjectType > getAllTypesFromConfiguration();

    /**
     * Gets the project configurations map.
     *
     * @return the project configurations map
     */
    Map< String, ProjectConfiguration > getProjectConfigurationsMap();

    /**
     * Gets the life cycle manager.
     *
     * @return the life cycle manager
     */
    LifeCycleManager getLifeCycleManager();

}