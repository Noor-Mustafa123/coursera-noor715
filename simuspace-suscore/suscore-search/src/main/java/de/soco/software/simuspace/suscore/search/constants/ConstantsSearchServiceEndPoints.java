package de.soco.software.simuspace.suscore.search.constants;

/**
 * The Class ConstantsSearchServiceEndPoints.
 *
 * @author Ali Haider
 */
public class ConstantsSearchServiceEndPoints {

    /**
     * The list of user tables.
     */
    public static final String GET_SEARCH_TREE = "/tree";

    /**
     * The Constant GET_SEARCH_TREE_BY_SEARCH_ID.
     */
    public static final String GET_SEARCH_TREE_BY_SEARCH_ID = "/tree/{searchId}";

    /**
     * The Constant GET_SEARCH_TABLE_UI.
     */
    public static final String GET_SEARCH_TABLE_UI = "/table/ui";

    /**
     * The Constant GET_SEARCH_TABLE_UI_BY_SEARCH_ID.
     */
    public static final String GET_SEARCH_TABLE_UI_BY_SEARCH_ID = "/table/{searchId}/ui";

    /**
     * The Constant GET_SEARCH_TABLE_LIST.
     */
    public static final String GET_SEARCH_TABLE_LIST = "/table/list";

    /**
     * The Constant GET_FILTER_SEARCH_ID.
     */
    public static final String GET_FILTER_SEARCH_ID = "/table/{searchId}/list";

    /**
     * The Constant GET_CONTEXT_SEARCH.
     */
    public static final String GET_CONTEXT_SEARCH = "/table/context";

    /**
     * The Constant GET_CONTEXT_SEARCH_ID.
     */
    public static final String GET_CONTEXT_SEARCH_ID = "/table/{searchId}/context";

}
