package de.soco.software.simuspace.suscore.object.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.model.FileInfo;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.common.model.ProjectConfiguration;
import de.soco.software.simuspace.suscore.data.common.model.StatusConfigDTO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.data.model.ProjectDTO;

/**
 * The interface Data project manager.
 */
public interface DataProjectManager {

    /**
     * Create project form map.
     *
     * @param token
     *         the token
     * @param parentId
     *         the parent id
     *
     * @return the map
     */
    UIForm createProjectForm( String token, UUID parentId );

    /**
     * Create project project dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     * @param projectJson
     *         the project json
     *
     * @return the project dto
     */
    ProjectDTO createProject( EntityManager entityManager, UserEntity userEntity, String projectJson );

    /**
     * Gets project custom attribute ui.
     *
     * @param userId
     *         the user id
     * @param projectType
     *         the project type
     *
     * @return the project custom attribute ui
     */
    UIForm getProjectCustomAttributeUI( String userId, String projectType );

    /**
     * Gets all objects.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     * @param token
     *         the token
     *
     * @return the all objects
     */
    FilteredResponse< Object > getAllObjects( String userId, UUID parentId, FiltersDTO filter, String token );

    /**
     * Gets all objects.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     * @param token
     *         the token
     *
     * @return the all objects
     */
    FilteredResponse< Object > getAllObjects( EntityManager entityManager, String userId, UUID parentId, FiltersDTO filter, String token );

    /**
     * Gets all items objects.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     * @param isObject
     *         the is object
     *
     * @return the all items objects
     */
    FilteredResponse< Object > getAllItemsObjects( String userId, UUID parentId, FiltersDTO filter, boolean isObject );

    /**
     * Gets project.
     *
     * @param entityManager
     *         the entity manager
     * @param projectId
     *         the project id
     *
     * @return the project
     */
    ProjectDTO getProject( EntityManager entityManager, UUID projectId );

    /**
     * Gets project.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     *
     * @return the project
     */
    ProjectDTO getProject( String userId, UUID projectId );

    /**
     * Edit project form map.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     *
     * @return the map
     */
    UIForm editProjectForm( String userId, String projectId );

    /**
     * Update project object.
     *
     * @param userId
     *         the user id
     * @param projectJson
     *         the project json
     *
     * @return the object
     */
    Object updateProject( String userId, String projectJson );

    /**
     * Gets list of objects ui table columns.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param parentId
     *         the parent id
     * @param option
     *         the option
     *
     * @return the list of objects ui table columns
     */
    TableUI getListOfObjectsUITableColumns( String userIdFromGeneralHeader, String parentId, String option );

    /**
     * Gets list of objects ui table columns.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param parentId
     *         the parent id
     * @param option
     *         the option
     *
     * @return the list of objects ui table columns
     */
    TableUI getListOfObjectsUITableColumns( EntityManager entityManager, String userIdFromGeneralHeader, String parentId, String option );

    /**
     * Gets list of objects ui table columns.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param parentId
     *         the parent id
     * @param option
     *         the option
     * @param views
     *         the views
     *
     * @return the list of objects ui table columns
     */
    TableUI getListOfObjectsUITableColumns( String userIdFromGeneralHeader, String parentId, String option, List< ObjectViewDTO > views );

    /**
     * List data ui list.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the list
     */
    List< TableColumn > listDataUI( String userId, String objectId );

    /**
     * Permission object options form list.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param option
     *         the option
     *
     * @return the list
     */
    List< SelectFormItem > permissionObjectOptionsForm( String userId, String objectId, String option );

    /**
     * Change object permissions boolean.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param map
     *         the map
     *
     * @return the boolean
     */
    boolean changeObjectPermissions( String userId, String objectId, Map< String, String > map );

    /**
     * Gets data object version context.
     *
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     *
     * @return the data object version context
     */
    List< ContextMenuItem > getDataObjectVersionContext( String objectId, FiltersDTO filter );

    /**
     * Gets object versions.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     * @param filter
     *         the filter
     *
     * @return the object versions
     */
    FilteredResponse< Object > getObjectVersions( String userId, UUID id, FiltersDTO filter );

    /**
     * Gets all values for project table column.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for project table column
     */
    List< Object > getAllValuesForProjectTableColumn( String projectId, String typeId, String columnName, String token );

    /**
     * Gets object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets object option form.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     *
     * @return the object option form
     */
    UIForm getObjectOptionForm( String userId, UUID parentId );

    /**
     * Gets update object permission ui.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param option
     *         the option
     *
     * @return the update object permission ui
     */
    UIForm getUpdateObjectPermissionUI( String userId, String objectId, String option );

    /**
     * Gets tabs view container ui.
     *
     * @param token
     *         the token
     * @param projectId
     *         the project id
     *
     * @return the tabs view container ui
     */
    SubTabsItem getTabsViewContainerUI( String token, String projectId );

    /**
     * Gets all items.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     *
     * @return the all items
     */
    List< FileInfo > getAllItems( String userId, UUID projectId );

    /**
     * Gets all child objects by type id.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param filter
     *         the filter
     * @param tokenFromGeneralHeader
     *         the token from general header
     *
     * @return the all child objects by type id
     */
    FilteredResponse< Object > getAllChildObjectsByTypeId( String userId, UUID projectId, String typeId, FiltersDTO filter,
            String tokenFromGeneralHeader );

    /**
     * Gets sync context router.
     *
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     *
     * @return the sync context router
     */
    List< ContextMenuItem > getSyncContextRouter( String projectId, FiltersDTO filter );

    /**
     * Gets transfer context router.
     *
     * @param filter
     *         the filter
     *
     * @return the transfer context router
     */
    List< ContextMenuItem > getTransferContextRouter( FiltersDTO filter );

    /**
     * Gets local context router.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     *
     * @return the local context router
     */
    List< ContextMenuItem > getLocalContextRouter( String userIdFromGeneralHeader, String projectId, FiltersDTO filter );

    /**
     * Gets local context router.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     *
     * @return the local context router
     */
    List< ContextMenuItem > getLocalContextRouter( EntityManager entityManager, String userIdFromGeneralHeader, String projectId,
            FiltersDTO filter );

    /**
     * Gets data context router.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     * @param token
     *         the token
     *
     * @return the data context router
     */
    List< ContextMenuItem > getDataContextRouter( String userIdFromGeneralHeader, String projectId, FiltersDTO filter, String token );

    /**
     * Gets data context router.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     * @param token
     *         the token
     *
     * @return the data context router
     */
    List< ContextMenuItem > getDataContextRouter( EntityManager entityManager, String userIdFromGeneralHeader, String projectId,
            FiltersDTO filter, String token );

    /**
     * Gets object items context router.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     *
     * @return the object items context router
     */
    List< ContextMenuItem > getObjectItemsContextRouter( String userIdFromGeneralHeader, String objectId, FiltersDTO filter );

    /**
     * Gets container versions ui.
     *
     * @param projectId
     *         the project id
     *
     * @return the container versions ui
     */
    List< TableColumn > getContainerVersionsUI( String projectId );

    /**
     * Sets object synch status.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userName
     *         the user name
     * @param fromString
     *         the from string
     * @param operation
     *         the operation
     *
     * @return the object synch status
     */
    DataObjectDTO setObjectSynchStatus( String userIdFromGeneralHeader, String userName, UUID fromString, String operation );

    /**
     * Gets synch status.
     *
     * @param userId
     *         the user id
     * @param fromString
     *         the from string
     *
     * @return the synch status
     */
    DataObjectDTO getObjectSynchStatus( String userId, UUID fromString );

    /**
     * Gets project configuration of object.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the project configuration of object
     */
    ProjectConfiguration getProjectConfigurationOfObject( String userId, UUID objectId );

    /**
     * Gets list of object view.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param typeId
     *         the type id
     *
     * @return the list of object view
     */
    List< ObjectViewDTO > getListOfObjectView( String userId, String objectId, UUID typeId );

    /**
     * Save or update object view object view dto.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param typeId
     *         the type id
     * @param viewJson
     *         the view json
     * @param isUpdateable
     *         the is updateable
     *
     * @return the object view dto
     */
    ObjectViewDTO saveOrUpdateObjectView( String userId, String objectId, UUID typeId, String viewJson, boolean isUpdateable );

    /**
     * Sets object view as default.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param typeId
     *         the type id
     * @param viewId
     *         the view id
     *
     * @return the object view as default
     */
    ObjectViewDTO setObjectViewAsDefault( String userId, String objectId, UUID typeId, String viewId );

    /**
     * Delete object view boolean.
     *
     * @param viewId
     *         the view id
     *
     * @return the boolean
     */
    boolean deleteObjectView( String viewId );

    /**
     * Gets life cycle status by id.
     *
     * @param lifeCycleName
     *         the life cycle name
     *
     * @return the life cycle status by id
     */
    StatusConfigDTO getLifeCycleStatusById( String lifeCycleName );

    /**
     * Gets object with version ui.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the object with version ui
     */
    SubTabsItem getObjectWithVersionUI( String userIdFromGeneralHeader, String objectId, int versionId );

    /**
     * Gets all filtered items.
     *
     * @param filtersDTO
     *         the filters dto
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     *
     * @return the all filtered items
     */
    List< FileInfo > getAllFilteredItems( FiltersDTO filtersDTO, String userId, UUID projectId );

    /**
     * Gets items count.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     *
     * @return the items count
     */
    Long getItemsCount( String userId, UUID projectId );

    /**
     * Gets all items count with type id.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     *
     * @return the all items count with type id
     */
    Long getAllItemsCountWithTypeId( String userId, UUID projectId, UUID typeId );

    /**
     * Create generic dto from object entity generic dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     * @param translationEntities
     *         the translation entities
     * @param forList
     *         the for list
     *
     * @return the generic dto
     */
    GenericDTO createGenericDTOFromObjectEntity( EntityManager entityManager, String userId, UUID projectId, SuSEntity susEntity,
            List< TranslationEntity > translationEntities, boolean forList );

    /**
     * Create generic dto from object entity generic dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     * @param translationEntities
     *         the translation entities
     *
     * @return the generic dto
     */
    GenericDTO createGenericDTOFromObjectEntity( EntityManager entityManager, String userId, UUID projectId, SuSEntity susEntity,
            List< TranslationEntity > translationEntities );

    /**
     * Create dto from sus entity object.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param susEntity
     *         the sus entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the object
     */
    Object createDTOFromSusEntity( EntityManager entityManager, UUID id, SuSEntity susEntity, boolean setCustomAttributes );

    /**
     * Gets data list context.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     * @param selectedIds
     *         the selected ids
     * @param token
     *         the token
     * @param getSearchContext
     *         the get search context
     *
     * @return the data list context
     */
    List< ContextMenuItem > getDataListContext( EntityManager entityManager, String userId, String projectId, FiltersDTO filter,
            List< String > selectedIds, String token, boolean getSearchContext );

    /**
     * Gets container or childs by id.
     *
     * @param objectId
     *         the object id
     *
     * @return the container or childs by id
     */
    List< Object > getContainerOrChildsById( String objectId );

    /**
     * Gets container or childs by id.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     *
     * @return the container or childs by id
     */
    List< Object > getContainerOrChildsById( EntityManager entityManager, String objectId );

    /**
     * Apply permissions on selected users or groups.
     *
     * @param entityManager
     *         the entity manager
     * @param aclObjectIdentityEntity
     *         the acl object identity entity
     * @param newSidSelection
     *         the new sid selection
     * @param objectInitiallyInherited
     *         the object initially inherited
     */
    void applyPermissionsOnSelectedUsersOrGroups( EntityManager entityManager, AclObjectIdentityEntity aclObjectIdentityEntity,
            List< UUID > newSidSelection, boolean objectInitiallyInherited );

    /**
     * Create object image dto from object entity object.
     *
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the object
     */
    Object createObjectImageDTOFromObjectEntity( UUID projectId, SuSEntity susEntity, boolean setCustomAttributes );

    /**
     * Gets object view key.
     *
     * @param susObjectModel
     *         the sus object model
     * @param typeId
     *         the type id
     *
     * @return the object view key
     */
    String getObjectViewKey( SuSObjectModel susObjectModel, String typeId );

    /**
     * Prepare object view dto object view dto.
     *
     * @param config
     *         the config
     * @param viewJson
     *         the view json
     * @param viewKey
     *         the view key
     * @param objectId
     *         the object id
     * @param isUpdateable
     *         the is updateable
     *
     * @return the object view dto
     */
    ObjectViewDTO prepareObjectViewDTO( String config, String viewJson, String viewKey, String objectId, boolean isUpdateable );

    /**
     * Gets object view list.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     * @param userId
     *         the user id
     * @param typeId
     *         the type id
     *
     * @return the object view list
     */
    List< ObjectViewDTO > getObjectViewList( EntityManager entityManager, SuSEntity susEntity, String userId, String typeId );

    /**
     * Gets all items with filters.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param filtersDTO
     *         the filters dto
     *
     * @return the all items with filters
     */
    List< FileInfo > getAllItemsWithFilters( String userId, UUID projectId, String typeId, FiltersDTO filtersDTO );

    /**
     * Create su s project project dto.
     *
     * @param userId
     *         the user id
     * @param projectJson
     *         the project json
     *
     * @return the project dto
     */
    ProjectDTO createSuSProject( String userId, String projectJson );

    /**
     * Gets overview by project id.
     *
     * @param token
     *         the token
     * @param projectId
     *         the project id
     * @param language
     *         the language
     *
     * @return the overview by project id
     */
    Object getOverviewByProjectId( String token, String projectId, String language );

}
