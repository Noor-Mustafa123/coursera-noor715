package de.soco.software.simuspace.suscore.common.constants;

/**
 * The Class ConstantsViewEndPoints that holds the end points for views.
 *
 * @author Zeeshan jamal
 */
public class ConstantsViewEndPoints {

    /**
     * The Constant SAVE_OR_LIST_VIEW.
     */
    public static final String SAVE_OR_LIST_VIEW = "/ui/view";

    /**
     * The constant SAVE_OR_LIST_SYSTEM_JOB_VIEW.
     */
    public static final String SAVE_OR_LIST_SYSTEM_JOB_VIEW = "/system/ui/view";

    /**
     * The Constant UPDATE_VIEW_AS_DEFAULT.
     */
    public static final String UPDATE_VIEW_AS_DEFAULT = "/ui/view/{viewId}/default";

    /**
     * The constant SYSTEM_JOB_UPDATE_VIEW_AS_DEFAULT.
     */
    public static final String SYSTEM_JOB_UPDATE_VIEW_AS_DEFAULT = "/system/ui/view/{viewId}/default";

    /**
     * The Constant SUS_SAVE_OR_LIST_VIEW.
     */
    public static final String SUS_SAVE_OR_LIST_VIEW = "/sus/ui/view";

    /**
     * The Constant SUS_UPDATE_VIEW_AS_DEFAULT.
     */
    public static final String SUS_UPDATE_VIEW_AS_DEFAULT = "/sus/ui/view/{viewId}/default";

    /**
     * The Constant DELETE_OR_UPDATE_OR_GET_VIEW.
     */
    public static final String DELETE_OR_UPDATE_OR_GET_VIEW = "/ui/view/{viewId}";

    /**
     * The constant DELETE_OR_UPDATE_SYSTEM_JOB_VIEW.
     */
    public static final String DELETE_OR_UPDATE_SYSTEM_JOB_VIEW = "/system/ui/view/{viewId}";

    /**
     * The Constant GET_RELATION_DELETE.
     */
    public static final String GET_RELATION_DELETE = "/relation/{id}";

    /**
     * The Constant CREATE_RELATION.
     */
    public static final String CREATE_RELATION = "/create/relation";

    /**
     * The Constant SUS_DELETE_OR_UPDATE_OR_GET_VIEW.
     */
    public static final String SUS_DELETE_OR_UPDATE_OR_GET_VIEW = "/sus/ui/view/{viewId}";

    /**
     * private constructor to avoid instantiation.
     */
    private ConstantsViewEndPoints() {

    }

}
