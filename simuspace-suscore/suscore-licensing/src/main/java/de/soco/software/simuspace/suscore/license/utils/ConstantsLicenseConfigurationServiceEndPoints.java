/*
 *
 */

package de.soco.software.simuspace.suscore.license.utils;

/**
 * The Class is responsible to hold API end points of licensing module.
 *
 * @author M.Nasir.Farooq
 */
public class ConstantsLicenseConfigurationServiceEndPoints {

    /**
     * The Constant ADD_LICENSE.
     */
    public static final String ADD_LICENSE = "/";

    /**
     * The Constant GET_MODULE_LIST.
     */
    public static final String GET_MODULE_LIST = "/list";

    /**
     * The Constant MANAGE_MODULE.
     */
    public static final String MANAGE_MODULE = "/manage";

    /**
     * The Constant DELETE_BY_MODULE.
     */
    public static final String DELETE_MODULE = "/{id}";

    /**
     * The Constant CHECK_FEATURE.
     */
    public static final String CHECK_FEATURE = "/check/{feature}";

    /**
     * The Constant LIST_USER_TABLE_UI.
     */
    public static final String LIST_LICENSE_TABLE_UI = "/ui";

    /**
     * The Constant CREATE_LICENSE_FORM.
     */
    public static final String CREATE_LICENSE_FORM = "/ui/create";

    /**
     * The Constant GET_CONTEXT_ROUTER.
     */
    public static final String GET_CONTEXT_ROUTER = "/context";

    /**
     * The Constant CREATE_LICENSE_FORM_FOR_EDIT.
     */
    public static final String CREATE_LICENSE_FORM_FOR_EDIT = "/ui/edit/{id}";

    /**
     * The Constant UPDATE_LICENSE.
     */
    public static final String UPDATE_LICENSE = "/";

    /**
     * The Constant DELETE_BY_MODULE.
     */
    public static final String DELETE_MULTI_BY_SELECTIOM = "/selection/{id}";

    /**
     * The Constant GET_ALL_LICENSES.
     */
    public static final String GET_ALL_LICENSES = "/";

    /**
     * The Constant MANAGE_LICENSE_TABLE_UI.
     */
    public static final String MANAGE_LICENSE_TABLE_UI = "/manage/ui";

    /**
     * The Constant GET_USER_MODULE_LIST.
     */
    public static final String GET_USER_MODULE_LIST = "/manage/list";

    /**
     * The Constant UPDATE_USER_LICENSE.
     */
    public static final String UPDATE_USER_LICENSE = "/manage/{userId}";

    /**
     * The Constant SAVE_OR_LIST_VIEWS.
     */
    public static final String SAVE_OR_LIST_VIEWS = "manage/ui/view";

    /**
     * The Constant UPDATE_VIEW_AS_DEFAULT.
     */
    public static final String UPDATE_VIEW_AS_DEFAULT = "manage/ui/view/{viewId}/default";

    /**
     * The Constant DELETE_OR_UPDATE_OR_GET_VIEW.
     */
    public static final String DELETE_OR_UPDATE_OR_GET_VIEW = "manage/ui/view/{viewId}";

    /**
     * Making constructor as private to avoid initialization.
     */
    private ConstantsLicenseConfigurationServiceEndPoints() {
    }

}
