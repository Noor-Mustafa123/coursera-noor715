package de.soco.software.simuspace.suscore.common.constants;

/**
 * Utility class that will contain common url routing views usable in all bundles.
 *
 * @author Ahsan.Khan
 */
public class ConstantsUrlViews {

    /**
     * The Constant DATA_OBJECT_VIEW.
     */
    public static final String DATA_OBJECT_VIEW = "view/data/object/{id}";

    /**
     * The Constant DATA_PROJECT_VIEW.
     */
    public static final String DATA_PROJECT_VIEW = "view/data/project/{id}";

    /**
     * The Constant VIEW_WORKFLOW_BY_ID_AND_VERSION_URL.
     */
    public static final String WORKFLOW_VIEW = "view/workflow/{id}/version/{version.id}";

    /**
     * The Constant WORKFLOW_PROJECT_VIEW.
     */
    public static final String WORKFLOW_PROJECT_VIEW = "view/workflow/project/{id}";

    /**
     * Instantiates a new constants url views.
     */
    private ConstantsUrlViews() {
    }

}
