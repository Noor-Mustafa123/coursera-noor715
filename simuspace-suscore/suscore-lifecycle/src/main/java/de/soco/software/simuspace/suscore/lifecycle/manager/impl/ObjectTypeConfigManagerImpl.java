package de.soco.software.simuspace.suscore.lifecycle.manager.impl;

import static org.apache.commons.lang3.StringUtils.isBlank;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

import org.apache.cxf.common.util.CollectionUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectType;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.ConfigUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.common.model.ProjectConfiguration;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.ObjectJsonSchemaEntity;
import de.soco.software.simuspace.suscore.data.model.MasterConfiguration;
import de.soco.software.simuspace.suscore.lifecycle.dao.ObjectTypeDAO;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.suscore.jsonschema.model.OVAConfigTab;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * @author Ahmar Nadeem
 * @UpdatedBy M.Nasir.Farooq
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
@Log4j2
public class ObjectTypeConfigManagerImpl implements ObjectTypeConfigManager {

    /**
     * The Constant ENTITY_CLASS_FIELD_NAME.
     */
    private static final String ENTITY_CLASS_FIELD_NAME = "ENTITY_CLASS";

    /**
     * MasterConfig file name
     */
    private static final String MASTER_CONFIG = "MasterConfig";

    /**
     * The Constant DATA.
     */
    private static final String DATA = "data";

    /**
     * The property name that holds the user acceptance criteria for data lost
     */
    private static final String USER_ACCEPTANCE_PROPERTY = "continue.if.data.lost";

    /**
     * The property name that holds the master config file path
     */
    public static final String MASTER_CONFIG_PATH_PROPERTY = "jsonschema.parent.filename";

    /**
     * ObjectTypeDAO for db operations other than SuSEntity
     */
    private ObjectTypeDAO objectTypeDAO;

    /**
     * The actual cache of the loaded configuration
     */
    private Map< String, ProjectConfiguration > objectsMap = new HashMap<>();

    /**
     * The actual cache of the loaded configuration
     */

    private LifeCycleManager lifeCycleManager;

    /**
     * The project start config.
     */
    private List< ConfigUtil.Config > projectStartConfig;

    /**
     * The master configuration file names for workflows.
     */
    private List< ConfigUtil.Config > masterConfigurationFileNamesForWorkflows;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * No argument constructor
     */
    public ObjectTypeConfigManagerImpl() {
        super();
    }

    /**
     * The function used to initiate the configurations on bundle up time.
     *
     * @throws IOException
     *         in case of error
     */
    public void init() throws IOException {
        if ( !PropertiesManager.isMasterLocation() ) {
            return;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            projectStartConfig = ConfigFilePathReader.getMasterConfigurationFileNames( MASTER_CONFIG_PATH_PROPERTY );
            if ( projectStartConfig.isEmpty() ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_CONFIG_ARRAY_IS_EMPTY.getKey() ) );
            }
            masterConfigurationFileNamesForWorkflows = ConfigFilePathReader.getMasterConfigurationFileNamesForWorkflows(
                    MASTER_CONFIG_PATH_PROPERTY );
            List< ConfigUtil.Config > configFiles = new ArrayList<>();
            configFiles.addAll( projectStartConfig );
            configFiles.addAll( masterConfigurationFileNamesForWorkflows );
            Map< String, ProjectConfiguration > newProjectConfigMap = loadNewConfigurationIntoMap( configFiles );
            applyBasicValidations( newProjectConfigMap );

            // Fetch data from DB.
            ObjectJsonSchemaEntity existingLatestMaterConfigEntity = loadLastJsonSchemaConfiguration( entityManager, MASTER_CONFIG );
            if ( existingLatestMaterConfigEntity != null ) {

                // create a similar map from the existing configuration saved in the DB.
                Map< String, ProjectConfiguration > existingProjectConfigMap = createObjectsMapFromEntity(
                        existingLatestMaterConfigEntity );
                boolean configFilesChanged = isMasterConfigFilesChanged( ConfigUtil.fileNames(
                        JsonUtils.jsonToObject( existingLatestMaterConfigEntity.getContent(), MasterConfiguration.class )
                                .getProjectConfigs() ), ConfigUtil.fileNames( configFiles ) );
                boolean configurationsChanged =
                        configFilesChanged || isProjectConfigurationsChanged( newProjectConfigMap, existingProjectConfigMap );
                if ( configFilesChanged || configurationsChanged ) {
                    log.info( "Config change detected. Updating configs" );
                    if ( ensureDataLossInNewConfig( entityManager, existingProjectConfigMap, newProjectConfigMap ) ) {
                        throw new JsonSerializationException( MessageBundleFactory.getMessage( Messages.DATA_LOSS_OCCURRED.getKey() ) );
                    }

                    // Save into the DB
                    persistConfigurationToDB( entityManager, configFiles, newProjectConfigMap );
                }

            } else {
                persistConfigurationToDB( entityManager, configFiles, newProjectConfigMap );
            }

            // Finally cache the configuration
            objectsMap.putAll( newProjectConfigMap );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the master configuration file names.
     *
     * @return the master configuration file names
     */
    @Override
    public List< ConfigUtil.Config > getMasterConfigurationFileNames() {
        return projectStartConfig;
    }

    /**
     * Gets the master configuration file names for work flows.
     *
     * @return the master configuration file names for work flows
     */
    @Override
    public List< ConfigUtil.Config > getMasterConfigurationFileNamesForWorkFlows() {
        return masterConfigurationFileNamesForWorkflows;
    }

    /**
     * Checks if new project configurations has changed from existing or not.
     *
     * @param newProjectConfigMap
     *         the new project config map
     * @param existingProjectConfigMap
     *         the existing project config map
     *
     * @return true, if is project configurations changed
     */
    private boolean isProjectConfigurationsChanged( Map< String, ProjectConfiguration > newProjectConfigMap,
            Map< String, ProjectConfiguration > existingProjectConfigMap ) {
        if ( newProjectConfigMap.size() != existingProjectConfigMap.size() ) {
            return true;
        }

        int equalConfigCount = ConstantsInteger.INTEGER_VALUE_ZERO;

        for ( Entry< String, ProjectConfiguration > newProjectConfigEntry : newProjectConfigMap.entrySet() ) {

            ProjectConfiguration existingProjectConfiguration = existingProjectConfigMap.get( newProjectConfigEntry.getKey() );
            if ( existingProjectConfiguration == null ) {
                return true;
            }
            if ( existingProjectConfiguration.equals( newProjectConfigEntry.getValue() ) && iconNotChanged(
                    newProjectConfigEntry.getValue(), existingProjectConfiguration ) ) {
                equalConfigCount++;
            }

        }

        return equalConfigCount != existingProjectConfigMap.size();
    }

    private boolean iconNotChanged( ProjectConfiguration newProjectConfiguration, ProjectConfiguration existingProjectConfiguration ) {
        int matchingIconsCount = ConstantsInteger.INTEGER_VALUE_ZERO;
        for ( SuSObjectModel newModel : newProjectConfiguration.getEntityConfig() ) {
            SuSObjectModel matchingExistingModel = existingProjectConfiguration.getEntityConfig().stream()
                    .filter( existingModel -> ( newModel.getId().equals( existingModel.getId() ) ) ).findFirst().orElse( null );
            if ( matchingExistingModel != null && newModel.getIcon().equals( matchingExistingModel.getIcon() ) ) {
                matchingIconsCount++;
            } else {
                return false;
            }
        }
        return matchingIconsCount == existingProjectConfiguration.getEntityConfig().size();
    }

    /**
     * MasterConfig Helper function to verify if the basic validations are successful
     *
     * @param newConfigObjectsMap
     *         the newConfigObjectsMap
     */
    private void applyBasicValidations( Map< String, ProjectConfiguration > newConfigObjectsMap ) {
        Map< String, String > ids = new HashMap<>();
        for ( Entry< String, ProjectConfiguration > entry : newConfigObjectsMap.entrySet() ) {
            for ( SuSObjectModel objectModel : entry.getValue().getEntityConfig() ) {
                handleIdValidations( entry.getKey(), objectModel );
                handleObjectClassNameValidations( entry.getKey(), objectModel );
                handleViewConfigValidation( entry, objectModel );
                ids.put( objectModel.getId(), null );
            }
        }
    }

    /**
     * Handle view config validation.
     *
     * @param entry
     *         the entry
     */
    private void handleViewConfigValidation( Entry< String, ProjectConfiguration > entry, SuSObjectModel objectModel ) {
        verifyIfObjectsRequiredInListsPresent( objectModel, entry.getValue() );

    }

    /**
     * Helper function to apply validations on object id
     *
     * @param configFileName
     *         the configFileName
     * @param objectModel
     *         the objectModel
     */
    private void handleIdValidations( String configFileName, SuSObjectModel objectModel ) {
        if ( isBlank( objectModel.getId() ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.OBJECT_ID_CANNOT_BE_EMPTY_IN_CONFIG.getKey(), objectModel.getName(),
                            configFileName ) );
        }

        if ( isBlank( objectModel.getLifeCycle() ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.LIFE_CYCLE_NAME_CANNOT_BE_EMPTY.getKey(), objectModel.getName(),
                            configFileName ) );

        }

        if ( !ValidationUtils.validateUUIDString( objectModel.getId() ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.INVALID_OBJECT_ID_PROVIDED_IN_CONFIG.getKey(), objectModel.getName(),
                            configFileName ) );
        }
    }

    /**
     * Helper function to apply validations on object class name
     *
     * @param configFileName
     *         the configFileName
     * @param objectModel
     *         the objectModel
     */
    private void handleObjectClassNameValidations( String configFileName, SuSObjectModel objectModel ) {
        if ( isBlank( objectModel.getClassName() ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.OBJECT_CLASSNAME_CANNOT_BE_EMPTY.getKey(), objectModel.getName(),
                            configFileName ) );
        }

        try {
            Class.forName( objectModel.getClassName() );
        } catch ( ClassNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.CLASS_DOES_NOT_EXIST_FOR_OBJECT.getKey(), objectModel.getClassName(),
                    objectModel.getName() ), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CLASS_DOES_NOT_EXIST_FOR_OBJECT.getKey(), objectModel.getClassName(),
                            objectModel.getName() ) );
        }

        if ( !isDTOClassHasEntityField( objectModel.getClassName() ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.ENTITY_CLASS_DOES_NOT_EXIST_IN_DTO.getKey(), objectModel.getClassName() ) );
        }

        if ( !isDTOClassHasValidEntityField( objectModel.getClassName() ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.DTO_DOES_NOT_HAVE_VALID_ENTITY_CLASS.getKey(), objectModel.getClassName() ) );
        }

    }

    /**
     * A helper function to compare the New Master Configuration files with the existing one.
     *
     * @param newConfigFiles
     *         the new config files
     * @param oldConfigFiles
     *         the old config files
     *
     * @return true, if successful
     */
    private boolean isMasterConfigFilesChanged( List< String > newConfigFiles, List< String > oldConfigFiles ) {

        boolean bothAreEmpty = CollectionUtils.isEmpty( newConfigFiles ) && CollectionUtils.isEmpty( oldConfigFiles );
        if ( bothAreEmpty ) {
            return false;
        }

        return !( newConfigFiles.containsAll( oldConfigFiles ) && newConfigFiles.size() == oldConfigFiles.size() );
    }

    /**
     * The function loads the new configuration into Map after converting to the object type format. It further helps in dealing with the
     * data.
     *
     * @param configFileNamesList
     *         the configFileName
     *
     * @return the new config objects map
     */
    private Map< String, ProjectConfiguration > loadNewConfigurationIntoMap( final List< ConfigUtil.Config > configFileNamesList ) {
        Map< String, ProjectConfiguration > newConfigObjectsMap = new HashMap<>();
        for ( ConfigUtil.Config subConfigFileName : configFileNamesList ) {
            newConfigObjectsMap.put( subConfigFileName.label(), getProjectConfigurationsByFileName( subConfigFileName.file() ) );
        }
        return newConfigObjectsMap;
    }

    /**
     * Gets the project configurations by file name.
     *
     * @param subConfigFileName
     *         the sub config file name
     *
     * @return the project configurations by file name
     */
    private ProjectConfiguration getProjectConfigurationsByFileName( String subConfigFileName ) {
        ProjectConfiguration projectConfig = new ProjectConfiguration();
        try ( InputStream propFileNameStream = new FileInputStream( ConstantsKaraf.KARAF_CONF_PATH + subConfigFileName ) ) {
            projectConfig = JsonUtils.jsonStreamToObject( propFileNameStream, ProjectConfiguration.class );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return projectConfig;
    }

    /**
     * Helper function to verify if the two lists contain the objects mentioned in the inMap object
     *
     * @param objectModel
     *         the objectModel
     * @param projectConfiguration
     *         the projectConfiguration
     */
    private void verifyIfObjectsRequiredInListsPresent( SuSObjectModel objectModel, ProjectConfiguration projectConfiguration ) {
        List< String > allObjectsIds = new ArrayList<>();
        List< String > allObjectsClassNames = new ArrayList<>();
        for ( SuSObjectModel object : projectConfiguration.getEntityConfig() ) {
            allObjectsIds.add( object.getId() );
            allObjectsClassNames.add( object.getClassName() );
        }
        for ( String linkedId : objectModel.getLinks() ) {
            if ( !allObjectsClassNames.contains( linkedId ) ) {
                log.warn( MessageBundleFactory.getMessage( Messages.LINK_REFFERED_OBJECT_NOT_FOUND.getKey(), linkedId,
                        objectModel.getName() ) );
                throw new SusException( MessageBundleFactory.getMessage( Messages.LINK_REFFERED_OBJECT_NOT_FOUND.getKey(), linkedId,
                        objectModel.getName() ) );
            }
        }
        for ( String containId : objectModel.getContains() ) {
            if ( !allObjectsIds.contains( containId ) ) {
                log.warn( MessageBundleFactory.getMessage( Messages.CONTAIN_REFFERED_OBJECT_NOT_FOUND.getKey(), containId,
                        objectModel.getName() ) );
                throw new SusException( MessageBundleFactory.getMessage( Messages.CONTAIN_REFFERED_OBJECT_NOT_FOUND.getKey(), containId,
                        objectModel.getName() ) );
            }
        }

        for ( OVAConfigTab viewConf : objectModel.getViewConfig() ) {
            if ( null != viewConf.getTypeId() && !viewConf.getKey().equalsIgnoreCase( DATA )
                    && !allObjectsIds.contains( viewConf.getTypeId() ) ) {
                log.warn( MessageBundleFactory.getMessage( Messages.VIEW_CONFIG_REFFERED_OBJECT_NOT_FOUND.getKey(), viewConf.getTypeId(),
                        objectModel.getName() ) );
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.VIEW_CONFIG_REFFERED_OBJECT_NOT_FOUND.getKey(), viewConf.getTypeId(),
                                objectModel.getName() ) );
            }
        }
    }

    /**
     * Checks if is data loss occurred with new configurations.
     *
     * @param entityManager
     *         the entity manager
     * @param existingProjectConfigMap
     *         the existing project config map
     * @param newProjectConfigMap
     *         the new project config map
     *
     * @return true, if successful
     */
    public boolean ensureDataLossInNewConfig( EntityManager entityManager,
            final Map< String, ProjectConfiguration > existingProjectConfigMap,
            final Map< String, ProjectConfiguration > newProjectConfigMap ) {

        // Load the user option whether to accept the changes even if data is
        // lost
        boolean isContinueIfDataLoss = ConfigFilePathReader.getBooleanPropertyValueFromConfiguration( USER_ACCEPTANCE_PROPERTY );
        for ( Entry< String, ProjectConfiguration > existingConfigurationsEntry : existingProjectConfigMap.entrySet() ) {

            String existingConfigFileName = existingConfigurationsEntry.getKey();

            boolean containsOldConfigFile = newProjectConfigMap.containsKey( existingConfigFileName );

            if ( !containsOldConfigFile && !isContinueIfDataLoss && isDataLossOccurred( entityManager,
                    existingConfigurationsEntry.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.MISSING_CONFIG_FILE.getKey(), existingConfigFileName ) );
            }

            ProjectConfiguration existingProjectConfig = existingConfigurationsEntry.getValue();
            ProjectConfiguration newProjectConfig = newProjectConfigMap.get( existingConfigFileName );
            for ( SuSObjectModel existingObjectType : existingProjectConfig.getEntityConfig() ) {

                SuSObjectModel newObjectType = newProjectConfig == null
                        ? null
                        : newProjectConfig.getEntityConfig().stream()
                                .filter( susObject -> Objects.equals( susObject.getId(), existingObjectType.getId() ) ).findFirst()
                                .orElse( null );

                /*
                 * In case the ID is not found in the new configuration, it means either the ID has been changed or missing this will ensure
                 * the data loss.
                 */
                checkIfIDMissingInConfig( entityManager, isContinueIfDataLoss, existingConfigFileName, existingObjectType, newObjectType );
            }
        }
        return false;
    }

    /**
     * Check if id missing in config.
     *
     * @param entityManager
     *         the entity manager
     * @param isContinueIfDataLoss
     *         the isContinueIfDataLoss
     * @param existingConfigFileName
     *         the existingConfigFileName
     * @param existingObjectType
     *         the existingObjectType
     * @param newObjectType
     *         the newObjectType
     */
    private void checkIfIDMissingInConfig( EntityManager entityManager, boolean isContinueIfDataLoss, String existingConfigFileName,
            SuSObjectModel existingObjectType, SuSObjectModel newObjectType ) {
        if ( newObjectType == null && !isContinueIfDataLoss && isDataLossOccurred( entityManager, existingObjectType ) ) {
            throw new JsonSerializationException(
                    MessageBundleFactory.getMessage( Messages.MISSING_OBJECT.getKey(), existingObjectType.getName() ) );
        }

        /*
         * In case the data is lost and also the user opted not to continue if data lost, then the system should halt the booting process of
         * the bundle.
         */
        if ( newObjectType != null && !newObjectType.getClassName().equals( existingObjectType.getClassName() ) && !isContinueIfDataLoss
                && isDataLossOccurred( entityManager, existingObjectType ) ) {
            throw new JsonSerializationException(
                    MessageBundleFactory.getMessage( Messages.NAME_OR_CLASS_CHANGED.getKey(), existingConfigFileName ) );
        }
    }

    /**
     * A helper function to check if the data loss has been occurred if project configurations removed.
     *
     * @param entityManager
     *         the entity manager
     * @param existingProjectConfiguration
     *         the existing project configuration
     *
     * @return true, if is data loss occurred
     */
    private boolean isDataLossOccurred( EntityManager entityManager, ProjectConfiguration existingProjectConfiguration ) {

        for ( SuSObjectModel susObjectModel : existingProjectConfiguration.getEntityConfig() ) {
            if ( isDataLossOccurred( entityManager, susObjectModel ) ) {
                return true;
            }
        }

        return false;
    }

    /**
     * A helper function to check if the data loss has been occurred.
     *
     * @param existingObjectType
     *         the existing object type
     *
     * @return True if the data lost. False otherwise
     */
    private boolean isDataLossOccurred( EntityManager entityManager, SuSObjectModel existingObjectType ) {

        boolean dataLost = false;
        Object entityObject = initializeDTOEntity( existingObjectType.getClassName() );

        Long totalNumberOfRecourds = objectTypeDAO.getCount( entityManager, entityObject.getClass(), false );
        if ( totalNumberOfRecourds != null && totalNumberOfRecourds.intValue() != 0 ) {
            log.warn( "Total number of records:" + totalNumberOfRecourds.intValue() + "exists for entity: " + entityObject.getClass() );
            dataLost = true;
        }

        return dataLost;
    }

    /**
     * Checks if is DTO class has entity field.
     *
     * @param dtoClassPath
     *         the dto class path
     *
     * @return true, if is DTO class has entity field
     */
    private boolean isDTOClassHasEntityField( String dtoClassPath ) {
        return ReflectionUtils.hasField( initializeObject( dtoClassPath ).getClass(), ENTITY_CLASS_FIELD_NAME );
    }

    /**
     * Checks if is DTO class has valid entity field.
     *
     * @param dtoClassPath
     *         the dto class path
     *
     * @return true, if is DTO class has valid entity field
     */
    private boolean isDTOClassHasValidEntityField( String dtoClassPath ) {
        Class< ? > clazz = ReflectionUtils.getFieldTypeByName( initializeObject( dtoClassPath ).getClass(), ENTITY_CLASS_FIELD_NAME );
        return clazz != null && clazz.isAnnotationPresent( Entity.class );
    }

    /**
     * Initialize DTO entity.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    private Object initializeDTOEntity( String className ) {

        Class< ? > entityClass = ReflectionUtils.getFieldTypeByName( initializeObject( className ).getClass(), ENTITY_CLASS_FIELD_NAME );
        return initializeObject( entityClass.getName() );
    }

    /**
     * Initialize object.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    private static Object initializeObject( String className ) {

        try {
            return Class.forName( className ).newInstance();
        } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ) );
        }
    }

    /**
     * @param existingConfig
     *         the existingConfig
     *
     * @return the new config objects map
     */
    private Map< String, ProjectConfiguration > createObjectsMapFromEntity( final ObjectJsonSchemaEntity existingConfig ) {

        Map< String, ProjectConfiguration > newConfigObjectsMap = new HashMap<>();
        /*
         * We actually don't need to read the master config in this case because it's already validated when it was saved. It's however
         * supposed that the contents are not changed in the DB outside the application.
         *
         */

        for ( ObjectJsonSchemaEntity childEntity : existingConfig.getObjectJsonSchemaChildList() ) {

            newConfigObjectsMap.put( childEntity.getName(),
                    JsonUtils.jsonToObject( childEntity.getContent(), ProjectConfiguration.class ) );
        }

        return newConfigObjectsMap;
    }

    /**
     * Persist configuration to db.
     *
     * @param entityManager
     *         the entity manager
     * @param configFileNames
     *         the configFileNames
     * @param projectConfigurations
     *         the projectConfiguration
     *
     * @Description This function is called at the boot time to validate the MasterConfig.js file and all others referenced in it. It then
     * caches the configuration for further use.
     */
    private void persistConfigurationToDB( EntityManager entityManager, List< ConfigUtil.Config > configFileNames,
            final Map< String, ProjectConfiguration > projectConfigurations ) {

        Date createdOn = new Date();

        MasterConfiguration configurationFiles = new MasterConfiguration( configFileNames );

        ObjectJsonSchemaEntity masterEntity = new ObjectJsonSchemaEntity();
        masterEntity.setId( UUID.randomUUID() );
        masterEntity.setName( MASTER_CONFIG );
        masterEntity.setContent( JsonUtils.toJson( configurationFiles ) );
        masterEntity.setCreatedOn( createdOn );
        masterEntity = saveNewConfiguration( entityManager, masterEntity );

        for ( Entry< String, ProjectConfiguration > projectConfig : projectConfigurations.entrySet() ) {
            log.info( projectConfig.getKey() );
            ObjectJsonSchemaEntity childEntity = new ObjectJsonSchemaEntity();
            childEntity.setId( UUID.randomUUID() );
            childEntity.setName( projectConfig.getKey() );
            childEntity.setContent( JsonUtils.toJson( projectConfig.getValue() ) );
            childEntity.setParentSchema( masterEntity );
            childEntity.setCreatedOn( createdOn );
            saveNewConfiguration( entityManager, childEntity );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSObjectModel getObjectTypeByIdAndConfigName( final String classId, final String config ) {

        if ( objectsMap.containsKey( config ) ) {
            ProjectConfiguration inMap = objectsMap.get( config );

            for ( SuSObjectModel suSObjectModel : inMap.getEntityConfig() ) {
                if ( suSObjectModel.getId().contentEquals( classId ) ) {
                    return suSObjectModel;
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSObjectModel > getObjectTypesByConfigName( final String config ) {

        List< SuSObjectModel > susObjectModels = new ArrayList<>();

        if ( objectsMap.containsKey( config ) ) {
            ProjectConfiguration projectConfiguration = objectsMap.get( config );
            if ( projectConfiguration != null ) {
                susObjectModels = projectConfiguration.getEntityConfig();
            }
        }

        return susObjectModels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSObjectModel getProjectModelByConfigLabel( final String config ) {

        SuSObjectModel susObjectModel = null;

        if ( objectsMap.containsKey( config ) ) {
            ProjectConfiguration projectConfiguration = objectsMap.get( config );
            susObjectModel = projectConfiguration.getEntityConfig().get( ConstantsInteger.INTEGER_VALUE_ZERO );
        }
        return susObjectModel;
    }

    @Override
    public List< ObjectType > getTypesFromConfiguration( final String config ) {
        List< ObjectType > objectTypeList = new ArrayList<>();
        if ( config != null ) {
            ProjectConfiguration projectConfiguration = objectsMap.get( config );
            for ( SuSObjectModel objectModel : projectConfiguration.getEntityConfig() ) {
                objectTypeList.add( new ObjectType( UUID.fromString( objectModel.getId() ), objectModel.getName() ) );
            }
        }
        return objectTypeList;
    }

    /**
     * Gets the all types from configuration.
     *
     * @return the all types from configuration
     */
    @Override
    public List< ObjectType > getAllTypesFromConfiguration() {
        List< ObjectType > objectTypeList = new ArrayList<>();
        for ( ProjectConfiguration projectConfiguration : objectsMap.values() ) {
            for ( SuSObjectModel objectModel : projectConfiguration.getEntityConfig() ) {
                objectTypeList.add( new ObjectType( UUID.fromString( objectModel.getId() ), objectModel.getName() ) );
            }
        }
        return objectTypeList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSObjectModel > getContainerObjectTypesByObjectTypeId( UUID objectTypeId, String configType ) {

        List< SuSObjectModel > containerTypes = new ArrayList<>();

        SuSObjectModel susObjectModel = getObjectTypeByIdAndConfigName( objectTypeId.toString(), configType );

        if ( susObjectModel != null && CollectionUtil.isNotEmpty( susObjectModel.getContains() ) ) {

            for ( String id : susObjectModel.getContains() ) {

                SuSObjectModel containingObjectType = getObjectTypeByIdAndConfigName( id, configType );
                Object entity = initializeDTOEntity( containingObjectType.getClassName() );
                if ( entity instanceof ContainerEntity ) {
                    containerTypes.add( containingObjectType );
                }

            }
        }

        return containerTypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectJsonSchemaEntity loadLastJsonSchemaConfiguration( EntityManager entityManager, String schemaName ) {

        return objectTypeDAO.loadLastJsonSchemaConfiguration( entityManager, schemaName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectConfiguration getProjectConfiguration( EntityManager entityManager, String configName ) {

        ObjectJsonSchemaEntity existingLatestMaterConfigEntity = loadLastJsonSchemaConfiguration( entityManager, MASTER_CONFIG );
        ProjectConfiguration projectConfiguration = null;
        if ( existingLatestMaterConfigEntity != null ) {

            for ( ObjectJsonSchemaEntity childEntity : existingLatestMaterConfigEntity.getObjectJsonSchemaChildList() ) {

                if ( configName.contentEquals( childEntity.getName() ) ) {
                    projectConfiguration = JsonUtils.jsonToObject( childEntity.getContent(), ProjectConfiguration.class );
                }

            }
        }

        return projectConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectJsonSchemaEntity saveNewConfiguration( EntityManager entityManager, ObjectJsonSchemaEntity masterEntity ) {

        Notification notify = validateObjectJsonEntity( masterEntity );
        if ( notify.hasErrors() ) {
            throw new SusException( notify.getErrors().toString() );
        }
        return objectTypeDAO.saveNewConfiguration( entityManager, masterEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusDTO getDefaultStatusByObjectTypeId( String typeId, String configType ) {
        SuSObjectModel susobject = getObjectTypeByIdAndConfigName( typeId, configType );
        return susobject != null ? lifeCycleManager.getDefaultStatusByLifeCycleId( susobject.getLifeCycle() ) : null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusDTO getStatusByIdandObjectType( UUID typeId, String lifeCycleStatus, String configType ) {
        SuSObjectModel susobject = getObjectTypeByIdAndConfigName( typeId.toString(), configType );
        return susobject != null ? lifeCycleManager.getStatusByLifeCycleNameAndStatusId( susobject.getLifeCycle(), lifeCycleStatus ) : null;
    }

    /**
     * @param masterEntity
     *         the masterEntity
     *
     * @return the notification object by filling the error messages (if any) after validating the OjbectJsonSchemaEntity
     */
    private Notification validateObjectJsonEntity( ObjectJsonSchemaEntity masterEntity ) {
        Notification notify = new Notification();

        if ( masterEntity != null ) {
            if ( isBlank( masterEntity.getName() ) ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_NAME_CANNOT_EMPTY.getKey() ) ) );
            }
            if ( isBlank( masterEntity.getContent() ) ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_CONTENT_CANNOT_EMPTY.getKey() ) ) );
            }
            if ( masterEntity.getCreatedOn() == null ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_CREATED_ON_CANNOT_EMPTY.getKey() ) ) );
            }
        }
        return notify;
    }

    /**
     * @param objectTypeDAO
     *         the objectTypeDAO to set
     */
    public void setObjectTypeDAO( ObjectTypeDAO objectTypeDAO ) {
        this.objectTypeDAO = objectTypeDAO;
    }

    /**
     * Gets the project configurations map.
     *
     * @return the project configurations map
     */
    @Override
    public Map< String, ProjectConfiguration > getProjectConfigurationsMap() {
        return objectsMap;
    }

    /**
     * Gets the life cycle manager.
     *
     * @return the life cycle manager
     */
    @Override
    public LifeCycleManager getLifeCycleManager() {
        return lifeCycleManager;
    }

    /**
     * Sets the life cycle manager.
     *
     * @param lifeCycleManager
     *         the new life cycle manager
     */
    public void setLifeCycleManager( LifeCycleManager lifeCycleManager ) {
        this.lifeCycleManager = lifeCycleManager;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
