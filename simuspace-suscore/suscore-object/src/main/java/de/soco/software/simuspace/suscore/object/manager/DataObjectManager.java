package de.soco.software.simuspace.suscore.object.manager;

import javax.persistence.EntityManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.CurveUnitDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.FileInfo;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.ValueUnitDTO;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.manager.base.AuditLogManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DataObjectCurveDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectMovieDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectTraceDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectValueDTO;
import de.soco.software.simuspace.suscore.data.model.ModelFilesDTO;
import de.soco.software.simuspace.suscore.object.model.ChangeStatusDTO;
import de.soco.software.simuspace.suscore.object.model.MetaDataEntryDTO;
import de.soco.software.simuspace.suscore.object.model.ObjectMetaDataDTO;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;

public interface DataObjectManager {

    /**
     * Create object form map.
     *
     * @param token
     *         the token
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     *
     * @return the map
     *
     * @apiNote To be used in service calls only
     */
    UIForm createObjectForm( String token, String parentId, String typeId );

    /**
     * Creates the translation form.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param translations
     *         the translations
     *
     * @return the list
     */
    UIForm createTranslationForm( String userId, String parentId, String typeId, String translations );

    /**
     * Update translation form.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param translations
     *         the translations
     *
     * @return the list
     */
    UIForm updateTranslationForm( String userId, String parentId, String typeId, String translations );

    /**
     * Create object form dashboard plugin map.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param plugin
     *         the plugin
     *
     * @return the map
     */
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    UIForm createObjectFormDashboardPlugin( String userId, String parentId, String typeId, String plugin );

    /**
     * Creates the object.
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param objectJson
     *         the object json
     * @param autoRefresh
     *         the auto refresh
     * @param token
     *         the token
     *
     * @return the object
     */
    Object createObject( EntityManager entityManager, UserEntity userEntity, String parentId, String typeId, String objectJson,
            boolean autoRefresh, String token );

    /**
     * Gets the all items objects.
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
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getAllItemsObjects( String userId, UUID parentId, FiltersDTO filter, boolean isObject );

    /**
     * Get Table View For Objects.
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
     * @return the list of objects UI table columns
     */
    TableUI getListOfObjectsUITableColumns( EntityManager entityManager, String userIdFromGeneralHeader, String parentId, String option );

    /**
     * Get Table View For Objects.
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
     * @return the list of objects UI table columns
     *
     * @apiNote To be used in service calls only
     */
    TableUI getListOfObjectsUITableColumns( String userIdFromGeneralHeader, String parentId, String option, List< ObjectViewDTO > views );

    /**
     * Get dataObject By Id.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param dataobjectId
     *         the dataobject id
     *
     * @return DataObjectDTO data object
     *
     * @apiNote To be used in service calls only
     */
    Object getDataObjectProperties( String userIdFromGeneralHeader, UUID dataobjectId );

    /**
     * Get dataObject By Id.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param dataobjectId
     *         the dataobject id
     *
     * @return DataObjectDTO data object
     *
     * @apiNote To be used in service calls only
     */
    Object getDataObject( String userIdFromGeneralHeader, UUID dataobjectId );

    /**
     * Get dataObject By Id.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param dataobjectId
     *         the dataobject id
     *
     * @return DataObjectDTO data object
     */
    Object getDataObject( EntityManager entityManager, String userIdFromGeneralHeader, UUID dataobjectId );

    /**
     * Move object.
     *
     * @param userId
     *         the user id
     * @param srcSelectionId
     *         the src selection id
     * @param targetSelectionId
     *         the target selection id
     *
     * @return boolean boolean
     *
     * @apiNote To be used in service calls only
     */
    boolean moveObject( String userId, UUID srcSelectionId, UUID targetSelectionId );

    /**
     * UI For Sub-tabs In DataObject View.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return List<SubTabs> tabs view data object ui
     *
     * @apiNote To be used in service calls only
     */
    SubTabsItem getTabsViewDataObjectUI( String token, String objectId );

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
     * Delete object versions and relations.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @apiNote To be used in service calls only
     */
    void deleteObjectVersionsAndRelations( String userId, UUID objectId );

    /**
     * Sets the permission inherited false.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param map
     *         the map
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean changeObjectPermissions( String userId, String objectId, Map< String, String > map );

    /**
     * UI For Sub-tabs In DataObject Version View.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return List<SubTabsUI> tabs view data object version ui
     *
     * @apiNote To be used in service calls only
     */
    SubTabsItem getTabsViewDataObjectVersionUI( String userId, String objectId, int versionId );

    /**
     * View Of Versions Table.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object versions UI
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getDataObjectVersionsUI( String objectId );

    /**
     * Gets the data object version context.
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
     * Get Filtered List of All version of DataObject.
     *
     * @param userId
     *         the user id
     * @param id
     *         of DataObject
     * @param filter
     *         the filter
     *
     * @return the data object versions
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getObjectVersions( String userId, UUID id, FiltersDTO filter );

    /**
     * Get DataObject By Id and VersionId.
     *
     * @param userId
     *         the user id
     * @param key
     *         of type VersionPrimaryKey
     *
     * @return the data object by id and version
     *
     * @apiNote To be used in service calls only
     */
    Object getObjectByIdAndVersion( String userId, VersionPrimaryKey key );

    /**
     * Object permission table UI.
     *
     * @param userId
     *         the user id
     *
     * @return the table UI
     *
     * @apiNote To be used in service calls only
     */
    TableUI objectPermissionTableUI( String userId );

    /**
     * Show permitted users and groups for object.
     *
     * @param filter
     *         the filter
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     *
     * @return the filtered response
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ManageObjectDTO > showPermittedUsersAndGroupsForObject( FiltersDTO filter, UUID objectId, String userId );

    /**
     * Permit permission to object.
     *
     * @param checkBox
     *         the check box
     * @param objectId
     *         the object id
     * @param securityId
     *         the security id
     * @param userId
     *         the user id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean permitPermissionToObject( CheckBox checkBox, UUID objectId, UUID securityId, String userId );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets the object single ui.
     *
     * @param objectId
     *         the object id
     *
     * @return the object single ui
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getObjectSingleUI( String objectId );

    /**
     * Gets the object version single UI.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the object version single UI
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getObjectVersionUI( String objectId, int versionId );

    /**
     * Adds the meta data to an object.
     *
     * @param objectId
     *         the object id
     * @param objectMetadataDTO
     *         the object metadata DTO
     * @param userId
     *         the user id
     * @param isCreateOperation
     *         the is create operation
     *
     * @return the object meta data DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectMetaDataDTO addMetaDataToAnObject( String objectId, ObjectMetaDataDTO objectMetadataDTO, String userId,
            boolean isCreateOperation );

    /**
     * Gets the object meta data list.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     *
     * @return the object meta data list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< MetaDataEntryDTO > getObjectMetaDataList( String userId, String objectId, FiltersDTO filter );

    /**
     * Gets the object meta data table UI.
     *
     * @param objectId
     *         the object id
     *
     * @return the object meta data table UI
     */
    List< TableColumn > getObjectMetaDataTableUI( String objectId );

    /**
     * Gets the meta data context router.
     *
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     *
     * @return the meta data context router
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getMetaDataContextRouter( String objectId, FiltersDTO filter );

    /**
     * Delete meta data by selection.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param key
     *         the key
     * @param mode
     *         the mode
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteMetaDataBySelection( String userIdFromGeneralHeader, String objectId, String key, String mode );

    /**
     * Prepare meta data form for edit.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param key
     *         the key
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm prepareMetaDataFormForEdit( String userIdFromGeneralHeader, String objectId, String key );

    /**
     * Prepare meta data form for create.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm prepareMetaDataFormForCreate( String userIdFromGeneralHeader, String objectId );

    /**
     * Gets the object meta data list by version.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     * @param jsonToObject
     *         the json to object
     *
     * @return the object meta data list by version
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< MetaDataEntryDTO > getObjectMetaDataListByVersion( String userIdFromGeneralHeader, String objectId, int versionId,
            FiltersDTO jsonToObject );

    /**
     * Change object option form.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the parent id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm changeStatusObjectOptionForm( String userId, UUID objectId );

    /**
     * Change status object version option form.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm changeStatusObjectVersionOptionForm( String userId, UUID objectId, int versionId );

    /**
     * Gets the update object permission UI.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param option
     *         the option
     *
     * @return the update object permission UI
     *
     * @apiNote To be used in service calls only
     */
    UIForm getUpdateObjectPermissionUI( String userId, String objectId, String option );

    /**
     * Gets the local context router.
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
     * Adds the file to an object.
     *
     * @param jobId
     *         the job id
     * @param objectId
     *         the object id
     * @param docMap
     *         the doc map
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object addFileToAnObject( String jobId, String objectId, Map< String, DocumentDTO > docMap, String userIdFromGeneralHeader );

    /**
     * Gets the data context router.
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
     * Gets the object items context router.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     *
     * @return the object items context router
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getObjectItemsContextRouter( String userIdFromGeneralHeader, String objectId, FiltersDTO filter );

    /**
     * Edits the data object form.
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editDataObjectForm( String objectId, String userId );

    /**
     * Edits the data object form.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     *
     * @return the list
     */
    UIForm editDataObjectForm( EntityManager entityManager, String objectId, String userId );

    /**
     * Update data object.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the data object DTO
     *
     * @apiNote To be used in service calls only
     */
    Object updateDataObject( String userId, String objectId, String objectJson );

    /**
     * Change status.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the parent id
     * @param changeStatusDTO
     *         the change status DTO
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    boolean changeStatusObject( String userIdFromGeneralHeader, UUID objectId, ChangeStatusDTO changeStatusDTO );

    /**
     * Change status object boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param changeStatusDTO
     *         the change status dto
     *
     * @return the boolean
     */
    boolean changeStatusObject( EntityManager entityManager, String userIdFromGeneralHeader, UUID objectId,
            ChangeStatusDTO changeStatusDTO );

    /**
     * Change status object version.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param changeStatusDTO
     *         the change status DTO
     * @param versionId
     *         the version id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean changeStatusObjectVersion( String userIdFromGeneralHeader, UUID objectId, ChangeStatusDTO changeStatusDTO, int versionId );

    /**
     * Gets the data object preview.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object preview
     *
     * @apiNote To be used in service calls only
     */
    DocumentDTO getDataObjectPreview( UUID objectId );

    /**
     * Delete selected data objects and its versions and relations.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @apiNote To be used in service calls only
     */
    void deleteObjectVersionsAndRelationsBulk( String userId, String objectId );

    /**
     * Gets the data object curve.
     *
     * @param objectId
     *         the object id
     * @param unitDTO
     *         the unit DTO
     *
     * @return the data object curve
     *
     * @apiNote To be used in service calls only
     */
    DataObjectCurveDTO getDataObjectCurve( UUID objectId, CurveUnitDTO unitDTO );

    /**
     * Gets the data object curve.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param unitDTO
     *         the unit DTO
     *
     * @return the data object curve
     */
    DataObjectCurveDTO getDataObjectCurve( EntityManager entityManager, UUID objectId, CurveUnitDTO unitDTO );

    /**
     * Gets the data object version curve.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version curve
     *
     * @throws IOException
     *         in case of error
     * @apiNote To be used in service calls only
     */
    DataObjectCurveDTO getDataObjectVersionCurve( UUID objectId, int versionId ) throws IOException;

    /**
     * Gets the data object version Html.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version Html
     *
     * @apiNote To be used in service calls only
     */
    Object getDataObjectVersionHtml( UUID objectId, int versionId );

    /**
     * Gets the data object movie.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object movie
     *
     * @apiNote To be used in service calls only
     */
    DataObjectMovieDTO getDataObjectMovie( UUID objectId );

    /**
     * Gets the data object movie.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     *
     * @return the data object movie
     */
    DataObjectMovieDTO getDataObjectMovie( EntityManager entityManager, UUID objectId );

    /**
     * Gets the data object version movie.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version movie
     *
     * @apiNote To be used in service calls only
     */
    DataObjectMovieDTO getDataObjectVersionMovie( UUID objectId, int versionId );

    /**
     * Delete object view.
     *
     * @param viewId
     *         the view id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteObjectView( String viewId );

    /**
     * Gets the data object version preview.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version preview
     *
     * @apiNote To be used in service calls only
     */
    DocumentDTO getDataObjectVersionPreview( UUID userId, UUID objectId, int versionId );

    /**
     * Gets the audit log manager.
     *
     * @return the audit log manager
     */
    AuditLogManager getAuditLogManager();

    /**
     * Gets the file info.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the file info
     *
     * @apiNote To be used in service calls only
     */
    FileInfo getFileInfo( String userId, String objectId );

    /**
     * Gets the file info.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the file info
     */
    FileInfo getFileInfo( EntityManager entityManager, String userId, String objectId );

    /**
     * Gets the curve X units.
     *
     * @param objectId
     *         the object id
     *
     * @return the curve X units
     *
     * @apiNote To be used in service calls only
     */
    List< SelectOptionsUI > getCurveXUnits( String objectId );

    /**
     * Gets the curve X units.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     *
     * @return the curve X units
     */
    List< SelectOptionsUI > getCurveXUnits( EntityManager entityManager, String objectId );

    /**
     * Gets the curve Y units.
     *
     * @param objectId
     *         the object id
     *
     * @return the curve X units
     *
     * @apiNote To be used in service calls only
     */
    List< SelectOptionsUI > getCurveYUnits( String objectId );

    /**
     * Gets the curve Y units.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     *
     * @return the curve X units
     */
    List< SelectOptionsUI > getCurveYUnits( EntityManager entityManager, String objectId );

    /**
     * Gets the data object value units.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object value units
     *
     * @apiNote To be used in service calls only
     */
    List< SelectOptionsUI > getDataObjectValueUnits( String objectId );

    /**
     * Gets the data object value.
     *
     * @param objectId
     *         the object id
     * @param valueUnitDTO
     *         the unit DTO
     *
     * @return the data object value
     *
     * @throws IOException
     *         in case of error
     * @apiNote To be used in service calls only
     */
    DataObjectValueDTO getDataObjectValue( UUID objectId, ValueUnitDTO valueUnitDTO ) throws IOException;

    /**
     * Gets the data object value.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param valueUnitDTO
     *         the unit DTO
     *
     * @return the data object value
     *
     * @throws IOException
     *         in case of error
     */
    DataObjectValueDTO getDataObjectValue( EntityManager entityManager, UUID objectId, ValueUnitDTO valueUnitDTO ) throws IOException;

    /**
     * Creates the generic DTO from object entity.
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
     * @return the generic DTO
     */
    GenericDTO createGenericDTOFromObjectEntity( EntityManager entityManager, String userId, UUID projectId, SuSEntity susEntity,
            List< TranslationEntity > translationEntities, boolean forList );

    /**
     * Creates the generic DTO from object entity.
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
     * @return the generic DTO
     */
    GenericDTO createGenericDTOFromObjectEntity( EntityManager entityManager, String userId, UUID projectId, SuSEntity susEntity,
            List< TranslationEntity > translationEntities );

    /**
     * Gets the data object trace by object id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the data object trace by object id
     *
     * @apiNote To be used in service calls only
     */
    Object getDataObjectTraceByObjectId( String selectionId );

    /**
     * Gets the data object trace by version and object id.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object trace by version and object id
     *
     * @apiNote To be used in service calls only
     */
    Object getDataObjectTraceByVersionAndObjectId( String objectId, int versionId );

    /**
     * Creates the DTO from sus entity.
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
     * Gets the data list context.
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
     * Gets the data object version curve Y.
     *
     * @param fromString
     *         the from string
     * @param versionId
     *         the version id
     *
     * @return the data object version curve Y
     *
     * @apiNote To be used in service calls only
     */
    Object getDataObjectVersionCurveY( UUID fromString, int versionId );

    /**
     * Gets the data object version curve X.
     *
     * @param fromString
     *         the from string
     * @param versionId
     *         the version id
     *
     * @return the data object version curve X
     *
     * @apiNote To be used in service calls only
     */
    Object getDataObjectVersionCurveX( UUID fromString, int versionId );

    /**
     * Gets the list of objects UI table columns by versions.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param typeId
     *         the type id
     * @param versionId
     *         the version id
     *
     * @return the list of objects UI table columns by versions
     *
     * @apiNote To be used in service calls only
     */
    TableUI getListOfObjectsUITableColumnsByVersions( String userIdFromGeneralHeader, String objectId, String typeId, int versionId );

    /**
     * Gets the all items objects by version.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     * @param toItems
     *         the to items
     * @param versionId
     *         the version id
     *
     * @return the all items objects by version
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getAllItemsObjectsByVersion( String userId, UUID parentId, FiltersDTO filter, boolean toItems,
            int versionId );

    /**
     * Gets the data object curve by version.
     *
     * @param objectId
     *         the object id
     * @param curveUnitDTO
     *         the curve unit DTO
     * @param versionId
     *         the version id
     *
     * @return the data object curve by version
     *
     * @apiNote To be used in service calls only
     */
    DataObjectCurveDTO getDataObjectCurveByVersion( UUID objectId, CurveUnitDTO curveUnitDTO, int versionId );

    /**
     * Gets the data object curve by version.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param curveUnitDTO
     *         the curve unit DTO
     * @param versionId
     *         the version id
     *
     * @return the data object curve by version
     */
    DataObjectCurveDTO getDataObjectCurveByVersion( EntityManager entityManager, UUID objectId, CurveUnitDTO curveUnitDTO, int versionId );

    /**
     * Gets the data object value units by version.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object value units by version
     *
     * @apiNote To be used in service calls only
     */
    List< SelectOptionsUI > getDataObjectValueUnitsByVersion( String objectId, int versionId );

    /**
     * Gets the data object value by version.
     *
     * @param objectId
     *         the object id
     * @param valueUnitDTO
     *         the value unit DTO
     * @param versionId
     *         the version id
     *
     * @return the data object value by version
     *
     * @throws IOException
     *         in case of error
     * @apiNote To be used in service calls only
     */
    DataObjectValueDTO getDataObjectValueByVersion( UUID objectId, ValueUnitDTO valueUnitDTO, int versionId ) throws IOException;

    /**
     * Gets the data object value by version.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param valueUnitDTO
     *         the value unit DTO
     * @param versionId
     *         the version id
     *
     * @return the data object value by version
     *
     * @throws IOException
     *         in case of error
     */
    DataObjectValueDTO getDataObjectValueByVersion( EntityManager entityManager, UUID objectId, ValueUnitDTO valueUnitDTO, int versionId )
            throws IOException;

    /**
     * Gets the container or childs by id.
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
     * Creates the object image DTO from object entity.
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
     * Gets the object view key.
     *
     * @param susObjectModel
     *         the config
     * @param typeId
     *         the type id
     *
     * @return the object view key
     */
    String getObjectViewKey( SuSObjectModel susObjectModel, String typeId );

    /**
     * Prepare object view DTO.
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
     * @return the object view DTO
     */
    ObjectViewDTO prepareObjectViewDTO( String config, String viewJson, String viewKey, String objectId, boolean isUpdateable );

    /**
     * Gets the object view list.
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
     * Save data object trace plot by object id.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    DataObjectTraceDTO saveDataObjectTracePlotByObjectId( String objectId, String objectJson );

    /**
     * Gets the data object trace plot by object id.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object trace plot by object id
     *
     * @apiNote To be used in service calls only
     */
    Object getDataObjectTracePlotByObjectId( String objectId );

    /**
     * Gets the data object trace plot by object id.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object trace plot by object id
     *
     * @apiNote To be used in service calls only
     */
    Object getDataObjectTracePlotByObjectIdAndVersionId( String objectId, int versionId );

    /**
     * Creates the su S object.
     *
     * @param userid
     *         the userid
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param objectJson
     *         the object json
     * @param autoRefresh
     *         the auto refresh
     * @param token
     *         the token
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object createSuSObject( String userid, String parentId, String typeId, String objectJson, boolean autoRefresh, String token );

    /**
     * Creates the su S object.
     *
     * @param entityManager
     *         the entity manager
     * @param userid
     *         the userid
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param objectJson
     *         the object json
     * @param autoRefresh
     *         the auto refresh
     * @param token
     *         the token
     *
     * @return the object
     */
    Object createSuSObject( EntityManager entityManager, String userid, String parentId, String typeId, String objectJson,
            boolean autoRefresh, String token );

    /**
     * Gets the model by object id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the data object trace by object id
     *
     * @apiNote To be used in service calls only
     */
    Object getModelByObjectId( String selectionId );

    /**
     * Gets the Model Files Data.
     *
     * @param objectId
     *         the objectId
     * @param filtersDTO
     *         the filters dto
     *
     * @return the model files list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ModelFilesDTO > getModelFilesData( UUID objectId, FiltersDTO filtersDTO );

    /**
     * Gets the html object by object id.
     *
     * @param userId
     *         the user id
     * @param userUid
     *         the user uid
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param language
     *         the language
     *
     * @return the html object by object id
     *
     * @apiNote To be used in service calls only
     */
    Object getHtmlObjectByObjectId( String userId, String userUid, String token, String objectId, String language );

    /**
     * Gets the ceetron 3 D object model.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the ceetron 3 D object model
     *
     * @apiNote To be used in service calls only
     */
    Map< String, String > getCeetron3DObjectModel( String userId, UUID objectId );

    /**
     * Removes the Elastic Search object if permission changes to none.
     *
     * @param checkBox
     *         the check box
     * @param objectId
     *         the object id
     *
     * @apiNote To be used in service calls only
     */
    void removeElsObjectIfPermissionNone( CheckBox checkBox, UUID objectId );

    /**
     * Create object form dashboard plugin and config list.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param plugin
     *         the plugin
     * @param config
     *         the config
     *
     * @return the list
     */
    UIForm createObjectFormDashboardPluginAndConfig( String userIdFromGeneralHeader, String parentId, String typeId, String plugin,
            String config );

    /**
     * Edit object form dashboard plugin list.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param plugin
     *         the plugin
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    UIForm getConfigFromObjectAndPlugin( String userIdFromGeneralHeader, String objectId, String plugin );

    /**
     * Gets config options from object and plugin and config.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param plugin
     *         the plugin
     * @param config
     *         the config
     *
     * @return the config options from object and plugin and config
     *
     * @apiNote To be used in service calls only
     */
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    UIForm getConfigOptionsFromObjectAndPluginAndConfig( String userIdFromGeneralHeader, String objectId, String plugin, String config );

    /**
     * Gets type of sus entity by id.
     *
     * @param objectId
     *         the object id
     *
     * @return the type of sus entity by id
     */
    String getTypeOfSusEntityById( UUID objectId );

}
