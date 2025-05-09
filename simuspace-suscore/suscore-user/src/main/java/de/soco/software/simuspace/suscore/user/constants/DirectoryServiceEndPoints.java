package de.soco.software.simuspace.suscore.user.constants;

/**
 * Utility class which will hold all the end-points of directory service.
 *
 * @author M.Nasir.Farooq
 */

public class DirectoryServiceEndPoints {

    /**
     * The Constant CREATE_DIRECTORY.
     */
    public static final String READ_DIRECTORY_LIST = "/list";

    /**
     * The Constant GET_ALL_DIRECTORIES.
     */
    public static final String GET_ALL_DIRECTORIES = "/";

    /**
     * The Constant GET_DIRECTORIES_BY_TYPE.
     */
    public static final String GET_DIRECTORIES_BY_TYPE = "/{type}";

    /**
     * The Constant CREATE_DIRECTORY.
     */
    public static final String CREATE_DIRECTORY = "/";

    /**
     * The Constant UPDATE_DIRECTORY.
     */
    public static final String UPDATE_DIRECTORY = "/";

    /**
     * The Constant DELETE_DIRECTORY.
     */
    public static final String DELETE_DIRECTORY = "/{id}";

    /**
     * The Constant READ_DIRECTORY.
     */
    public static final String READ_DIRECTORY = "/{id}";

    /**
     * The Constant READ_DIRECTORY.
     */
    public static final String LIST_DIRECTORY_TABLE_UI = "/ui";

    /**
     * The Constant TEST_CONNECTION to test ldap or ad connection.
     */
    public static final String TEST_CONNECTION = "/test/connection";

    /**
     * The Constant READ_DIRECTORY.
     */
    public static final String LIST_DIRECTORY_TABLE_COLUMNS = "/columns";

    /**
     * The directoy create form
     */
    public static final String CREATE_DIRECTORY_FORM = "/ui/create";

    /**
     * The Constant GET_CONTEXT_ROUTER.
     */
    public static final String GET_CONTEXT_ROUTER = "/context";

    /**
     * The Constant CREATE_USER_DIRECTORY_FOR_EDIT.
     */
    public static final String CREATE_USER_DIRECTORY_FOR_EDIT = "/ui/edit/{id}";

    /**
     * Private constructor to prevent instantiation.
     */
    private DirectoryServiceEndPoints() {

    }

}
