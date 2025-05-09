package de.soco.software.simuspace.suscore.document.constants;

/**
 * @author Ahmar Nadeem
 *
 * A class containing the constants for the document service.
 */
public final class ConstantsDocumentServiceEndPoints {

    /**
     * version number of the document
     */
    public static final String VERSION = "version";

    /**
     * document id of the document (UUID)
     */
    public static final String DOCUMENT_ID = "documentId";

    /**
     * The Constant OBJECT_ID.
     */
    public static final String OBJECT_ID = "objectId";

    /**
     * user id for which the document operation is required (UUID)
     */
    public static final String USER_ID = "userId";

    /**
     * get all documents paginated list
     */
    public static final String GET_ALL_DOCUMENTS = "/list";

    /**
     * get all documents paginated list by user
     */
    public static final String GET_ALL_DOCUMENTS_BY_USER = "/list/{" + USER_ID + "}";

    /**
     * get specific document by id
     */
    public static final String GET_DOCUMENT_BY_ID = "/{" + DOCUMENT_ID + "}/{" + VERSION + "}";

    /**
     * update document properties
     */
    public static final String UPDATE_DOCUMENT = "/";

    /**
     * delete a specific document entry
     */
    public static final String DELETE_DOCUMENT_BY_ID = "/{" + DOCUMENT_ID + "}/{" + VERSION + "}";

    /**
     * download a specific file API URL
     */
    public static final String DOWNLOAD_DOCUMENT_BY_ID = "/{" + OBJECT_ID + "}/version/{" + VERSION + "}/download";

}
