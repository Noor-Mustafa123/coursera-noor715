package de.soco.software.simuspace.suscore.permissions.constants;

/**
 * The Class is responsible for holding the constant for all permissions.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class ConstantsPermissions {

    /**
     * The Constant CREATE_STANDARD_API_URL.
     */
    public static final String CREATE_STANDARD_API_URL = "/";

    /**
     * The Constant STANDARD_API_URL.
     */
    public static final String STANDARD_API_URL = "/";

    /**
     * The Constant STANDARD_API_URL_FOR_DELETE.
     */
    public static final String STANDARD_API_URL_FOR_DELETE = "/{id}";

    /**
     * The Constant CLEAR_AUTHORIZATION_CACHE.
     */
    public static final String CLEAR_AUTHORIZATION_CACHE = "/auth";

    /**
     * The Constant LIST_ROLE_TABLE_UI.
     */
    public static final String LIST_ROLE_TABLE_UI = "/ui";

    /**
     * The Constant LIST_ALL_ROLES.
     */
    public static final String LIST_ALL_ROLES = "/list";

    /**
     * The constant active account status
     */
    public static final boolean ACCOUNT_STATUS_ACTIVE = true;

    /**
     * The constant inactive account status
     */
    public static final boolean ACCOUNT_STATUS_IN_ACTIVE = false;

    /**
     * The Constant LIST_ROLE_TABLE_COLUMNS.
     */
    public static final String LIST_ROLE_TABLE_COLUMNS = "/columns";

    /**
     * The Constant CREATE_ROLE_FORM.
     */
    public static final String CREATE_ROLE_FORM = "/ui/create";

    /**
     * The Constant CREATE_RESOURCE_FORM.
     */
    public static final String CREATE_RESOURCE_FORM = "/resource/ui/create";

    /**
     * The Constant EDIT_ROLE_FORM.
     */
    public static final String EDIT_ROLE_FORM = "/ui/edit/{id}";

    /**
     * The Constant CREATE_ROLE_MANAGE_FORM.
     */
    public static final String CREATE_ROLE_MANAGE_FORM = "/manage/ui";

    /**
     * The Constant READ_ROLE.
     */
    public static final String READ_ROLE = "/{id}";
    // --------------------------------------------------

    /**
     * The Constant CHECK_USER_IS_PERMITTED_TO_RESOURCE.
     */
    public static final String CHECK_USER_IS_PERMITTED_TO_RESOURCE = "/permitted";

    /**
     * The Constant RESOURCE.
     */
    public static final String RESOURCE = "/resource";

    /**
     * The Constant PERMIT_PERMISSION_TO_ROLE.
     */
    public static final String PERMIT_PERMISSION_TO_ROLE = "/manage/{roleId}/{resourceId}";

    /**
     * The Constant PERMIT_PERMISSION_TO_USER.
     */
    public static final String PERMIT_PERMISSION_TO_USER = "/resource/manage/user/{userId}/{resourceId}";

    /**
     * The Constant MANAGE_PERMISSION_TABLE.
     */
    public static final String MANAGE_PERMISSION_TABLE = "/manage/{id}/ui";

    /**
     * The Constant MANAGE_PERMISSION_TABLE_FOR_USER_UI.
     */
    public static final String MANAGE_PERMISSION_TABLE_FOR_USER_UI = "/resource/manage/user/{id}/ui";

    /**
     * The Constant GET_PERMISSION_MODULE_LIST_BY_ROLE_ID.
     */
    public static final String GET_PERMISSION_MODULE_LIST_BY_ROLE_ID = "/manage/{id}/list";

    /**
     * The Constant GET_PERMISSION_MODULE_LIST_BY_USER_ID.
     */
    public static final String GET_PERMISSION_MODULE_LIST_BY_USER_ID = "/resource/manage/user/{id}/list";

    // --------------------------------------------------

    /**
     * The Constant LIST_VIEWS.
     */
    public static final String SAVE_OR_LIST_VIEWS = "manage/{id}/ui/view";

    /**
     * The Constant UPDATE_VIEW_AS_DEFAULT.
     */
    public static final String UPDATE_VIEW_AS_DEFAULT = "manage/{id}/ui/view/{viewId}/default";

    /**
     * The Constant DELETE_VIEW.
     */
    public static final String DELETE_OR_UPDATE_OR_GET_VIEW = "manage/{id}/ui/view/{viewId}";

}
