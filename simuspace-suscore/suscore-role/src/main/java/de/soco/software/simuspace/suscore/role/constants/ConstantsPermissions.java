package de.soco.software.simuspace.suscore.role.constants;

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

    /**
     * The Constant TOTAL_CHECKBOX_COUNT.
     */
    public static final int TOTAL_CHECKBOX_COUNT = 15;
    // ------------------------------------------

    /**
     * The Constant PERMIT_PERMISSION_TO_ROLE.
     */
    public static final String PERMIT_PERMISSION_TO_ROLE = "/resource/manage/role/{roleId}/{resourceId}";

}
