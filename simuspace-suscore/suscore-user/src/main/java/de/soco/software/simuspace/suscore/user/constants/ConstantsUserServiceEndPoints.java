package de.soco.software.simuspace.suscore.user.constants;

/**
 * The constant class holding all addresses of user api.
 */
public class ConstantsUserServiceEndPoints {

    /**
     * The CREATE_USER_ON_BEHALF_OF_DIRECTORY.
     */
    public static final String CREATE_USER_ON_BEHALF_OF_DIRECTORY = "/{dirID}";

    /**
     * The part of URL pattern to get user by id.
     */
    public static final String GET_USER_BY_ID = "/{userId}";

    /**
     * The list of user table columns.
     */
    public static final String LIST_USER_TABLE_COLUMNS = "/columns";

    /**
     * The user tables columns and views.
     */
    public static final String LIST_USER_TABLE_UI = "/ui";

    /**
     * The user single view
     */
    public static final String VIEW_SINGLE_USER_UI = "/ui/single";

    /**
     * The user create form by dir id.
     */
    public static final String CREATE_USER_BY_DIR_ID = "/ui/create/{dirId}/user/{userUid}";

    /**
     * The user create form by dir id.
     */
    public static final String EDIT_USER_UI = "/ui/edit/{userId}";

    /**
     * The UPDATE_USER.
     */
    public static final String UPDATE_USER = "/";

    /**
     * The UPDATE_USER PROFILE.
     */
    public static final String UPDATE_USER_PROFILE = "/profile";

    /**
     * The UPDATE_USER_PASSWORD.
     */
    public static final String UPDATE_USER_PASS = "/{userId}/password/{token}";

    /**
     * The user create form by dir id.
     */
    public static final String EDIT_USER_PROFILE_UI = "/ui/edit/{userId}/profile";

    /**
     * The DIRECTORY_ID.
     */
    public static final String DIRECTORY_ID = "dirID";

    /**
     * The Constant CHECK_USER_ROLE_PERMITTED.
     */
    public static final String CHECK_USER_ROLE_PERMITTED = "/hasRole";

    /**
     * The Constant CHECK_USER_IS_PERMITTED_TO_RESOURCE.
     */
    public static final String CHECK_USER_IS_PERMITTED_TO_RESOURCE = "/permitted";

    /**
     * The Constant PROVIDE_ALL_PERMITTED_RESOURCES_TO_CURRENT_USER.
     */
    public static final String PROVIDE_ALL_PERMITTED_RESOURCES_TO_CURRENT_USER = "/allPermitted";

    /**
     * The Constant ADD_PERMISSION_TO_OBJECT.
     */
    public static final String ADD_PERMISSION_TO_OBJECT = "/object";

    /**
     * The Constant RESOURCE.
     */
    public static final String RESOURCE = "/resource";

    /**
     * The Constant UPDATE_RESOURCE.
     */
    public static final String UPDATE_RESOURCE = "/resource/{id}";

    /**
     * The end point for getting all users paginated.
     */
    public static final String GET_ALL_USERS = "/list";

    /**
     * The end point for getting all languages .
     */
    public static final String GET_ALL_LANGUAGES = "/list/languages";

    /**
     * The Constant CURRENT.
     */
    public static final String CURRENT = "/current";

    /**
     * The Constant CURRENT.
     */
    public static final String CURRENT_UID = "/current/uid";

    /**
     * The Constant GET_CONTEXT_ROUTER.
     */
    public static final String GET_CONTEXT_ROUTER = "/context";

    /**
     * The Constant DELETE_BY_MODULE.
     */
    public static final String DELETE_MODULE = "/{id}";

    /**
     * The Constant MANAGE_LICENSE_FORM.
     */
    public static final String MANAGE_LICENSE_FORM = "/license/ui/manage";

    /**
     * Private constructor to prevent instantiation.
     */
    private ConstantsUserServiceEndPoints() {

    }

}
