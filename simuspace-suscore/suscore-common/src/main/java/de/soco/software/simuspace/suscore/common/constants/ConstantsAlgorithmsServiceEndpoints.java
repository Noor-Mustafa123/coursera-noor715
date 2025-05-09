package de.soco.software.simuspace.suscore.common.constants;

/**
 * The Class ConstantsSchemeServiceEndpoints.
 */
public class ConstantsAlgorithmsServiceEndpoints {

    /**
     * The Constant WORKFLOW_ID_PARAM.
     */
    public static final String WORKFLOW_ID_PARAM = "workflowId";

    /**
     * The Constant CATEGORY_ID_PARAM.
     */
    public static final String CATEGORY_ID_PARAM = "categoryId";

    /**
     * The Constant GET_CATEGORY_OPTIONS_BY_WORKFLOW_ID.
     */
    public static final String CATEGORY_OPTIONS_BY_WORKFLOW_ID = "/options/{workflowId}";

    public static final String FORM_POSTPROCESS_WORKFLOW_ID = "/postprocess/{workflowId}";

    public static final String FORM_POSTPROCESS_WF_WORKFLOW_ID = "/postprocess/wf/{workflowId}";

    /**
     * The Constant SCHEME_ID_PARAM.
     */
    public static final String SCHEME_ID_PARAM = "schemeId";

    /**
     * The Constant DESIGN_VARIABLE_ID_PARAM.
     */
    public static final String DESIGN_VARIABLE_ID_PARAM = "designVariableId";

    /**
     * The Constant OBJECTIVE_VARIABLE_ID_PARAM.
     */
    public static final String OBJECTIVE_VARIABLE_ID_PARAM = "objectiveVariableId";

    /**
     * The Constant MODE.
     */
    public static final String MODE = "mode";

    /**
     * The Constant GET_SCHEME_UI.
     */
    public static final String GET_SCHEME_UI = "/ui";

    /**
     * The Constant GET_SCHEME_LIST.
     */
    public static final String GET_SCHEME_LIST = "/list";

    /**
     * The Constant GET_SCHEME_CONTEXT.
     */
    public static final String GET_SCHEME_CONTEXT = "/context";

    /**
     * The Constant CREATE_SCHEME_UI.
     */
    public static final String CREATE_SCHEME_UI = "/ui/create";

    /**
     * The Constant CREATE_SCHEME.
     */
    public static final String CREATE_SCHEME = "/";

    /**
     * The Constant EDIT_SCHEME_BY_ID.
     */
    public static final String EDIT_SCHEME_BY_ID = "/ui/edit/{schemeId}";

    /**
     * The Constant UPDATE_OR_DELETE_SCHEME_BY_ID.
     */
    public static final String UPDATE_OR_DELETE_SCHEME_BY_ID = "/{schemeId}";

    /**
     * The Constant RUN_SCHEME_WORKFLOW_ID.
     */
    public static final String RUN_SCHEME_WORKFLOW_ID = "/{workflowId}/runscheme";

    /**
     * The Constant GET_SCHEME_TABS_BY_WORKFLOW_ID.
     */
    public static final String GET_SCHEME_TABS_BY_WORKFLOW_ID = "/{workflowId}/ui";

    /**
     * The Constant VALIDATE_SCHEME_BY_WORKFLOW_ID.
     */
    public static final String VALIDATE_SCHEME_BY_WORKFLOW_ID = "/{workflowId}";

    /**
     * The Constant RUN_SCHEME_CATEGORY_FORM_UI.
     */
    public static final String RUN_SCHEME_CATEGORY_FORM_UI = "/ui/create/{workflowId}/category/{categoryId}";

    /**
     * The Constant RUN_SCHEME_SCHEME_FORM_UI.
     */
    public static final String RUN_SCHEME_SCHEME_FORM_UI = "/ui/create/{workflowId}/scheme/{schemeId}";

    /**
     * The Constant SCAN_OBJECTIVE_FILE.
     */
    public static final String SCAN_OBJECTIVE_FILE = "/scan";

    public static final String SCAN_OBJECTIVE_FILE_ON_SERVER_SIDE = "/scan/onServerSide";

    /**
     * The Constant SCAN_OBJECTIVE_FILE_PATH.
     */
    public static final String SCAN_OBJECTIVE_FILE_PATH = "/scan/file";

    /**
     * The Constant DESIGN_VARIABLES.
     */
    public static final String DESIGN_VARIABLES = "/{workflowId}/designvariables";

    public static final String DESIGN_SUMMARY = "/{workflowId}/designsummary";

    /**
     * The Constant GET_DESIGN_VARIABLES_UI.
     */
    public static final String GET_DESIGN_VARIABLES_UI = "/{workflowId}/designvariables/ui";

    /**
     * The Constant GET_DESIGN_VARIABLES_LIST.
     */
    public static final String GET_DESIGN_VARIABLES_LIST = "/{workflowId}/designvariables/list";

    /**
     * The Constant GET_DESIGN_VARIABLES_FILTER.
     */
    public static final String GET_DESIGN_VARIABLES_FILTER = "/{workflowId}/designsummary/filter";

    /**
     * The Constant GET_DESIGN_SUMMARY_UI.
     */
    public static final String GET_DESIGN_SUMMARY_UI = "/{workflowId}/designsummary/ui";

    /**
     * The Constant DESIGN_SUMMARY_FILTER_SYNTAX.
     */
    public static final String DESIGN_SUMMARY_FILTER_SYNTAX = "/{workflowId}/designsummary/filter/syntax";

    /**
     * The Constant GET_DESIGN_SUMMARY_LIST.
     */
    public static final String GET_DESIGN_SUMMARY_LIST = "/{workflowId}/designsummary/list";

    /**
     * The Constant GET_GENERATE_DESIGN_SUMMARY_LIST.
     */
    public static final String GET_GENERATE_DESIGN_SUMMARY_LIST = "/{workflowId}/generatedesignsummary";

    /**
     * The Constant GET_DESIGN_SUMMARY_ORIGINAL_LIST.
     */
    public static final String GET_DESIGN_SUMMARY_ORIGINAL_LIST = "/{workflowId}/designsummary/list/refresh";

    /**
     * The Constant DESIGN_VARIABLES_CONTEXT.
     */
    public static final String DESIGN_VARIABLES_CONTEXT = "/{workflowId}/designvariables/context";

    /**
     * The Constant UPDATE_DESIGN_VARIABLES_UI.
     */
    public static final String UPDATE_DESIGN_VARIABLES_UI = "/designvariables/{designVariableId}/ui";

    /**
     * The Constant UPDATE_DESIGN_VARIABLES_BY_ID.
     */
    public static final String UPDATE_DESIGN_VARIABLES_BY_ID = "/designvariables/{designVariableId}";

    /**
     * The Constant OBJECTIVE_VARIABLES.
     */
    public static final String OBJECTIVE_VARIABLES = "/{workflowId}/objectivevariables";

    /**
     * The Constant GET_OBJECTIVE_VARIABLES_UI.
     */
    public static final String GET_OBJECTIVE_VARIABLES_UI = "/{workflowId}/objectivevariables/ui";

    /**
     * The Constant GET_OBJECTIVE_VARIABLES_LIST.
     */
    public static final String GET_OBJECTIVE_VARIABLES_LIST = "/{workflowId}/objectivevariables/list";

    /**
     * The Constant GET_CUSTOM_VARIABLES_LIST.
     */
    public static final String GET_CUSTOM_VARIABLES_LIST = "/{workflowId}/customVariables/list";

    /**
     * The Constant OBJECTIVE_VARIABLES_CONTEXT.
     */
    public static final String OBJECTIVE_VARIABLES_CONTEXT = "/{workflowId}/objectivevariables/context";

    /**
     * The Constant UPDATE_OBJECTIVE_VARIABLES_UI.
     */
    public static final String UPDATE_OBJECTIVE_VARIABLES_UI = "/objectivevariables/{objectiveVariableId}/ui";

    /**
     * The Constant UPDATE_OBJECTIVE_VARIABLES_BY_ID.
     */
    public static final String UPDATE_OBJECTIVE_VARIABLES_BY_ID = "/objectivevariables/{objectiveVariableId}";

    /**
     * The Constant ADD_DESIGN_SUMMARY_UI.
     */
    public static final String ADD_DESIGN_SUMMARY_UI = "/{workflowId}/designsummary/add/ui";

    /**
     * The Constant ADD_DESIGN_SUMMARY.
     */
    public static final String ADD_DESIGN_SUMMARY = "/{workflowId}/designsummary";

    /**
     * The Constant UPDATE_OBJECTIVE_VARIABLES_UI.
     */
    public static final String UPDATE_DESIGN_SUMMARY_UI = "/{workflowId}/designsummary/{id}/edit/ui";

    /**
     * The Constant UPDATE_OBJECTIVE_VARIABLES_BY_ID.
     */
    public static final String UPDATE_DESIGN_SUMMARY_BY_ID = "/{workflowId}/designsummary/{id}";

    /**
     * The Constant DELETE_DESIGN_SUMMARY.
     */
    public static final String DELETE_DESIGN_SUMMARY = "/{workflowId}/designsummary/delete";

    /**
     * The Constant GENERATE_IMAGE_OPTIONS.
     */
    public static final String GENERATE_IMAGE_OPTIONS = "/generateimage/options";

    /**
     * The constant SAVE_VARIABLES for ask-on-run parameters
     */
    public static final String SAVE_VARIABLES = "/general/{workflowId}";

}