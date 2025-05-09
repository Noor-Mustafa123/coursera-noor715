package de.soco.software.simuspace.suscore.common.constants;

/**
 * The constant class holding all addresses of selection api.
 *
 * @author Noman Arshad
 */
public class ConstantsSelectionServiceEndPoints {

    /**
     * Instantiates a new constants selection service end points.
     */
    private ConstantsSelectionServiceEndPoints() {
    }

    /**
     * The Constant SAVE_USER_SELECTIONS.
     */
    public static final String SAVE_SELECTIONS = "/";

    /**
     * The Constant SAVE_USER_SELECTIONS.
     */
    public static final String SAVE_SELECTIONS_ORIGIN = "/{origin}";

    /**
     * API endpoint to get User IDs only against given selection id.
     */
    public static final String GET_SELECTION_IDS_ONLY = "/{id}";

    /**
     * The Constant GET_USER_SELECTIONS.
     */
    public static final String GET_USER_SELECTIONS = "/list/selection/{id}";

    /**
     * The Constant GET_SIMUSPACE_ABOUT_MENU.
     */
    public static final String GET_SIMUSPACE_ABOUT_MENU = "/";

    /**
     * The Constant GET_SIMUSPACE_SUPPORT_MENU.
     */
    public static final String GET_SIMUSPACE_UI_MENU = "/ui";

    /**
     * The Constant ADD_SELECTIONS.
     */
    public static final String ADD_SELECTIONS = "/{selectionId}/add";

    /**
     * The Constant REMOVE_SELECTIONS.
     */
    public static final String REMOVE_SELECTIONS = "/{selectionId}/remove";

    /**
     * The Constant UI_GENERIC_DTO.
     */
    public static final String UI_GENERIC_DTO = "/{selectionId}/ui";

    /**
     * The Constant LIST_GENERIC_DTO.
     */
    public static final String LIST_GENERIC_DTO = "/{selectionId}/list";

    /**
     * The Constant REORDER_SELECTION.
     */
    public static final String REORDER_SELECTION = "/{selectionId}/order";

    /**
     * The Constant UI_UPDATE_VIEW.
     */
    public static final String UI_UPDATE_VIEW = "/{selectionId}/ui/updateView";

    /**
     * The Constant ITEMS_LIST.
     */
    public static final String ITEMS_LIST = "/items/list";

    /**
     * The Constant SELECTION_SORT.
     */
    public static final String SELECTION_SORT = "/{selectionId}/sort";

    /**
     * The Constant SELECTION_ITEM_ATTRIBUTE.
     */
    public static final String SELECTION_ITEM_ATTRIBUTE = "/{selectionId}/item/{selectionItemId}";

    /**
     * The Constant SELECTION_ATTRIBUTE_UI.
     */
    public static final String SELECTION_ATTRIBUTE_UI = "/{selectionId}/attribute/ui";

    /**
     * The Constant SELECTION_ATTRIBUTE.
     */
    public static final String SELECTION_ATTRIBUTE = "/{selectionId}/attribute";

    /**
     * The Constant SELECTION_ATTRIBUTE_LIST.
     */
    public static final String SELECTION_ATTRIBUTE_LIST = "/{selectionId}/attribute/list";

    /**
     * The Constant SELECTION_ATTRIBUTE_UI_CREATE.
     */
    public static final String SELECTION_ATTRIBUTE_UI_CREATE = "/{selectionId}/attribute/ui/create";

    /**
     * The Constant SELECTION_ATTRIBUTE_CONTEXT.
     */
    public static final String SELECTION_ATTRIBUTE_CONTEXT = "/{selectionId}/attribute/context";

    /**
     * The Constant SELECTION_ATTRIBUTE_EDIT.
     */
    public static final String SELECTION_ATTRIBUTE_EDIT = "/ui/edit/{selectionId}/attribute/{attribSelectionId}";

    /**
     * The Constant SELECTION_ATTRIBUTE_DELETE.
     */
    public static final String SELECTION_ATTRIBUTE_DELETE = "/{selectionId}/attribute/{attribSelectionId}";

    /**
     * The Constant SELECTION_ATTRIBUTE_UI_CREATE.
     */
    public static final String SELECTION_ATTRIBUTE_UI_SELECT_OPTION = "/{selectionId}/attribute/{attribSelectionId}/ui/createoption/{option}";

}
