package de.soco.software.simuspace.suscore.object.utility;

/**
 * Utility class which will contain all the end-points of object service.
 *
 * @author Nosheen.Sharif
 */
public class ConstantsObjectServiceEndPoints {

    /**
     * The Constant RERUN_JOB.
     */
    public static final String RERUN_JOB = "rerun/job/{objectId}";

    /**
     * The Constant TRANSLATIONS_PARAM.
     */
    public static final String TRANSLATIONS_PARAM = "translations";

    /**
     * Constant ParentId For Url *.
     */
    public static final String PARENT_ID_PARAM = "parentId";

    /**
     * The Constant SELECTION_ID_PARAM.
     */
    public static final String SELECTION_ID_PARAM = "selectionId";

    /**
     * Constant objectId For Url *.
     */
    public static final String OBJ_ID_PARAM = "objectId";

    /**
     * Constant JobId For Url *.
     */
    public static final String JOB_ID_PARAM = "jobId";

    /**
     * The Constant INCLUDE_CHILD.
     */
    public static final String INCLUDE_CHILD = "include";

    /**
     * Constant ProjectId For Url *.
     */
    public static final String PROJ_ID_PARAM = "projectId";

    /**
     * The Constant VERSION_ID_PARAM.
     */
    public static final String VERSION_ID_PARAM = "versionId";

    /**
     * The Constant OPTION.
     */
    public static final String OPTION = "option";

    /**
     * create object API end point.
     */
    public static final String CREATE_OBJECT = "/";

    /**
     * view object API end point for router url.
     */
    public static final String VIEW_OBJECT_BY_ID_URL = "view/data/object/{id}";

    /**
     * view object With Id and Version API end point for router url.
     */
    public static final String VIEW_OBJECT_BY_ID_AND_VERSION_URL = "view/data/object/{id}/version/{version.id}";

    /**
     * The Constant VIEW_PROJECT_BY_ID_AND_VERSION_URL.
     */
    public static final String VIEW_PROJECT_BY_ID_AND_VERSION_URL = "view/data/project/{id}/version/{version.id}";

    /**
     * update object API end point.
     */
    public static final String UPDATE_OBJECT = "/{objectId}";

    /**
     * The Constant UPDATE_OBJECT_ATTRIBUT_VALUE.
     */
    public static final String UPDATE_OBJECT_ATTRIBUT_VALUE = "/{objectId}/attribute";

    /**
     * get Object list by type API end point.
     */
    public static final String GET_OBJECT_LIST_BY_TYPE = "/list/{objectType}";

    /**
     * get Object Type configuration by scheme type and object type Id.
     */
    public static final String GET_OBJECT_TYPE_BY_ID = "/scheme/{schemeName}/{objectTypeId}";

    /**
     * get Object Version list by type API end point.
     */
    public static final String GET_OBJECT_VERSION_LIST_BY_TYPE = "/list/{objectType}/{objectId}/versions";

    /**
     * Get Object By Id .This will get the object with latest version
     */
    public static final String GET_OBJECT_BY_ID = "/{objectType}/{objectId}";

    /**
     * Get Object By Id and version Id.This will get the object with given version
     **/
    public static final String GET_OBJECT_BY_ID_AND_VERSION = "{objectType}/{objectId}/{version}";

    /**
     * Gets the container able object types.
     */
    public static final String GET_CONTAINER_OBJECT_TYPES = "/container";

    /**
     * service endpoint to search audit log.
     */
    public static final String SEARCH_AUDIT_LOG = "/list";

    /**
     * service endpoint to get audit log table ui.
     */
    public static final String LIST_AUDIT_LOG_TABLE_UI = "/ui";

    /**
     * service endpoint to get audit log table columns.
     */
    public static final String LIST_AUDIT_LOG_TABLE_COLUMNS = "/columns";

    /**
     * The Constant OBJECT_PERMISSION_UI.
     */
    public static final String OBJECT_PERMISSION_UI = "/object/{objectId}/permissions/ui";

    /**
     * The Constant OBJECT_PERMISSION_LIST.
     */
    public static final String OBJECT_PERMISSION_LIST = "/object/{objectId}/permissions/list";

    /**
     * The Constant PERMIT_PERMISSION_TO_OBJECT.
     */
    public static final String PERMIT_PERMISSION_TO_OBJECT = "/object/{objectId}/permissions/{sidId}";

    /**
     * The Constant VIEW_DATA_PROJECT.
     */
    public static final String VIEW_DATA_PROJECT = "view/data/project/{objectId}";

    /**
     * The Constant VIEW_SEARCH.
     */
    public static final String VIEW_SEARCH = "view/search";

    /**
     * The Constant VIEW_SEARCH_ID.
     */
    public static final String VIEW_SEARCH_ID = "view/search/{searchId}";

    /**
     * The Constant CREATE_DATA_PROJECT_.
     */
    public static final String CREATE_IN_CONTAINER = "project/create/options/{objectId}";

    /**
     * The Constant UPDATE_DATA_PROJECT.
     */
    public static final String UPDATE_DATA_PROJECT = "update/data/project/{objectId}";

    /**
     * The Constant VIEW_DATA_OBJECT.
     */
    public static final String VIEW_DATA_OBJECT = "view/data/object/{objectId}";

    /**
     * The Constant VIEW_DATA_OBJECT_BY_ID_AND_VERSION.
     */
    public static final String VIEW_DATA_OBJECT_BY_ID_AND_VERSION = "view/data/object/{objectId}/version/{version.id}";

    /**
     * The Constant VIEW_PROJECT_BY_ID_AND_VERSION.
     */
    public static final String VIEW_PROJECT_BY_ID_AND_VERSION = "view/data/project/{objectId}/version/{version.id}";

    /**
     * The Constant CREATE_DATA_OBJECT.
     */
    public static final String CREATE_DATA_OBJECT = "create/data/object/{objectId}";

    /**
     * The Constant CREATE_DATA_CONTAINER.
     */
    public static final String CREATE_DATA_CONTAINER = "create/data/container/{objectId}";

    /**
     * The Constant SYSTEM_AUDIT_LOGS.
     */
    public static final String SYSTEM_AUDIT_LOGS = "system/auditlog";

    /**
     * The Constant SYSTEM_PERMISSIONS_ROLES.
     */
    public static final String SYSTEM_PERMISSIONS_ROLES = "system/permissions/roles";

    /**
     * The Constant CREATE_SYSTEM_PERMISSIONS_ROLE.
     */
    public static final String CREATE_SYSTEM_PERMISSIONS_ROLE = "create/system/permissions/role";

    /**
     * The Constant EXPORT_OBJECT.
     */
    public static final String EXPORT_OBJECT = "export/data/{objectId}/{selectionId}";

    /**
     * The Constant CREATE_SECTION.
     */
    public static final String CREATE_SECTION = "section/create/{objectId}";

    /**
     * The Constant VIEW_SYSTEM_GROUPS.
     */
    public static final String VIEW_SYSTEM_GROUPS = "system/permissions/groups";

    /**
     * The Constant VIEW_DATA_OBJECT_ID.
     */
    public static final String VIEW_DATA_OBJECT_ID = "/view/data/object/{objectId}";

    /**
     * The Constant OBJECT_META_DATA_FORM.
     */
    public static final String OBJECT_META_DATA_FORM = "/object/{objectId}/metadata/ui/create";

    /**
     * The Constant OBJECT_PERMISSION_FORM.
     */
    public static final String OBJECT_PERMISSION_FORM = "/object/perm/ui/create/{objectId}";

    /**
     * The Constant PROJECT_PERMISSION_FORM.
     */
    public static final String PROJECT_PERMISSION_FORM = "/project/perm/ui/create/{projectId}";

    /**
     * The Constant OBJECT_META_DATA_UI.
     */
    public static final String OBJECT_META_DATA_UI = "/object/{objectId}/metadata/ui";

    /**
     * The Constant OBJECT_META_DATA_LIST.
     */
    public static final String OBJECT_META_DATA_LIST = "/object/{objectId}/metadata/list";

    /**
     * The Constant OBJECT_VERSION_META_DATA_LIST.
     */
    public static final String OBJECT_VERSION_META_DATA_LIST = "/object/{objectId}/version/{versionId}/metadata/list";

    /**
     * The Constant OBJECT_META_DATA_CONTEXT.
     */
    public static final String OBJECT_META_DATA_CONTEXT = "/object/{objectId}/metadata/context";

    /**
     * The Constant CREATE_SYSTEM_USER_GROUP.
     */
    public static final String CREATE_SYSTEM_USER_GROUP = "create/system/user/group";

    /**
     * The Constant SYSTEM_USER_DIRECTORIES.
     */
    public static final String SYSTEM_USER_DIRECTORIES = "system/user-directories";

    /**
     * The Constant CREATE_SYSTEM_USER_DIRECTORY.
     */
    public static final String CREATE_SYSTEM_USER_DIRECTORY = "create/system/user-directory";

    /**
     * The Constant SYSTEM_USERS.
     */
    public static final String SYSTEM_USERS = "system/users";

    /**
     * The Constant SYSTEM_LOCATIONS.
     */
    public static final String SYSTEM_LOCATIONS = "system/locations";

    /**
     * The Constant CREATE_SYSTEM_LOCATOIN.
     */
    public static final String CREATE_SYSTEM_LOCATION = "create/system/location";

    /**
     * The Constant EDIT_SYSTEM_LOCATION.
     */
    public static final String EDIT_SYSTEM_LOCATION = "/edit/system/location/{objectId}";

    /**
     * The Constant SYSTEM_USERS.
     */
    public static final String SYSTEM_JOBS = "view/job";

    /**
     * The Constant SYSTEM_USERS.
     */
    public static final String HPC_UGE_JOBS = "view/dashboard/hpc/{objectId}/uge/job/{id}";

    /**
     * The Constant CREATE_SYSTEM_USER.
     */
    public static final String CREATE_SYSTEM_USER = "create/system/user";

    /**
     * The Constant SYSTEM_LICENSE.
     */
    public static final String SYSTEM_LICENSE = "system/license";

    /**
     * The Constant CREATE_SYSTEM_LICENSE.
     */
    public static final String CREATE_SYSTEM_LICENSE = "create/system/license";

    /**
     * The Constant MANAGE_SYSTEM_LICENSE.
     */
    public static final String MANAGE_SYSTEM_LICENSE = "manage/system/license";

    /**
     * The Constant EDIT_USER_PROFILE.
     */
    public static final String EDIT_USER_PROFILE = "edit/system/user/{objectId}/profile";

    /**
     * The Constant VIEW_DATA_OBJECT.
     */
    public static final String VIEW_WORKFLOW_PROJECT = "/view/workflow/project/{objectId}";

    /**
     * The Constant CREATE_WORKFLOW.
     */
    public static final String CREATE_WORKFLOW = "/create/workflow/{objectId}";

    /**
     * The Constant VIEW_WORKFLOW_SINGLE_OBJECT_ID.
     */
    public static final String VIEW_WORKFLOW_SINGLE = "view/workflow/{objectId}/version/{versionId}";

    /**
     * The Constant VIEW_WORKFLOW_
     */
    public static final String VIEW_WORKFLOW = "view/workflow/{objectId}";

    /**
     * The Constant VIEW_WORKFLOW_SINGLE_OBJECT_ID.
     */
    public static final String VIEW_JOB_SINGLE = "view/job/{objectId}";

    /**
     * The Constant OBJECT_PERMISSION_FORM_OPTION.
     */
    public static final String OBJECT_PERMISSION_FORM_OPTION = "object/{objectId}/permission/fields/{option}";

    /**
     * The Constant PROJECT_PERMISSION_FORM_OPTION.
     */
    public static final String PROJECT_PERMISSION_FORM_OPTION = "project/{projectId}/permission/fields/{option}";

    /**
     * The Constant OBJECT_PERMISSION_CHANGE.
     */
    public static final String OBJECT_PERMISSION_CHANGE = "object/{objectId}/permission/change";

    /**
     * The Constant DELETED_OBJECTS_TABLE_UI.
     */
    public static final String DELETED_OBJECTS_TABLE_UI = "/deletedobjects/ui";

    /**
     * The Constant DELETED_OBJECTS_LIST.
     */
    public static final String DELETED_OBJECTS_LIST = "/deletedobjects/list";

    /**
     * The Constant DELETED_OBJECTS_BREADCRUMB.
     */
    public static final String DELETED_OBJECTS_BREADCRUMB = "/data/deletedobjects";

    /**
     * The Constant DELETED_OBJECTS_CONTEXT.
     */
    public static final String DELETED_OBJECTS_CONTEXT = "/deletedobjects/context";

    /**
     * The Constant PROJECT_PERMISSION_CHANGE.
     */
    public static final String PROJECT_PERMISSION_CHANGE = "project/{projectId}/permission/change";

    /**
     * Constant variant For Url
     **/
    public static final String VARIANT_ID_PARAM = "variantId";

    /**
     * Constant library For Url
     **/
    public static final String LIBRARY_ID_PARAM = "libraryId";

    /**
     * The Constant TYPE.
     */
    public static final String TYPE = "typeId";

    /**
     * The Constant ABOUT_BREADCRUMB.
     */
    public static final String ABOUT_BREADCRUMB = "/help/about";

    /**
     * The Constant SUPPORT_BREADCRUMB.
     */
    public static final String SUPPORT_BREADCRUMB = "/help/support";

    /**
     * The Constant DOWNLOAD_CLIENT_BREADCRUMB.
     */
    public static final String DOWNLOAD_CLIENT_BREADCRUMB = "/help/download-client";

    /**
     * The Constant ADD_META_DATA_BREAD_CRUMB.
     */
    public static final String ADD_META_DATA_BREAD_CRUMB = "/add/data/object/{objectId}";

    /**
     * The Constant UPDATE_DATA_OBJECT_BREAD_CRUMB.
     */
    public static final String UPDATE_DATA_OBJECT_BREAD_CRUMB = "/update/data/object/{objectId}";

    /**
     * The Constant CREATE_DATA_OBJECT_BREAD_CRUMB.
     */
    public static final String CREATE_DATA_OBJECT_BREAD_CRUMB = "/create/data/project/{projectId}";

    /**
     * The Constant UPDATE_STATUS_OBJECT_BREAD_CRUMB.
     */
    public static final String UPDATE_STATUS_OBJECT_BREAD_CRUMB = "/update/data/object/{objectId}/status";

    /**
     * The Constant IMPORT_WORKFLOW_PROJECT.
     */
    public static final String IMPORT_WORKFLOW_PROJECT = "/workflow/import/ui/{objectId}";

    /**
     * The Constant EDIT_WORKFLOW_PROJECT.
     */
    public static final String EDIT_WORKFLOW_PROJECT = "/workflow/ui/edit/{objectId}";

    /**
     * The Constant EDIT_ROLE.
     */
    public static final String EDIT_ROLE = "/edit/system/permissions/role/{objectId}";

    /**
     * The Constant ROLE_PERMISSION_MANAGE.
     */
    public static final String ROLE_PERMISSION_MANAGE = "/manage/system/permissions/resource/manage/role/{objectId}";

    /**
     * The Constant EDIT_GROUP.
     */
    public static final String EDIT_GROUP = "/edit/system/group/{objectId}";

    /**
     * The Constant EDIT_SYSTEM_LICENSE.
     */
    public static final String EDIT_SYSTEM_LICENSE = "/edit/system/license/{objectId}";

    /**
     * The Constant EDIT_DIRECTORY.
     */
    public static final String EDIT_DIRECTORY = "/edit/system/user-directory/{objectId}";

    /**
     * The Constant ADD_PERMISSIONS_TO_OBJECT.
     */
    public static final String ADD_PERMISSIONS_TO_OBJECT = "/perm/data/object/{objectId}";

    /**
     * The Constant EDIT_SYSTEM_USER.
     */
    public static final String EDIT_SYSTEM_USER = "/edit/system/user/{objectId}";

    /**
     * The Constant UPDATE_OBJECT_STATUS.
     */
    public static final String UPDATE_OBJECT_STATUS = "/update/data/object/{objectId}/version/{versionId}/status";

    /**
     * The Constant UPDATE_OBJECT_TYPE.
     */
    public static final String UPDATE_OBJECT_TYPE = "/data/project/{projectId}/selection/{selectionId}/changetype";

    /**
     * The Constant UPDATE_PROJECT_METADATA.
     */
    public static final String UPDATE_PROJECT_METADATA = "/edit/data/object/{objectId}/metadata/{projectName}";

    /**
     * The Constant PROJECT_PERMISSION.
     */
    public static final String PROJECT_PERMISSION = "/perm/data/project/{objectId}";

    /**
     * The Constant EDIT_WORKFLOW.
     */
    public static final String EDIT_WORKFLOW = "/workflow/ui/edit/{objectId}";

    public static final String CREATE_WORKFLOW_PROJECT_OBJECT_ID = "create/workflow/project/{objectId}";

    public static final String OBJECT_MODELFILES_CONTEXT = "object/{objectId}/modelfiles/context";

    /**
     * Private Constructor to avoid Object Instantiation.
     */
    private ConstantsObjectServiceEndPoints() {

    }

}
