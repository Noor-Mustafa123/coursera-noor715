package de.soco.software.simuspace.suscore.common.constants;

/**
 * @author Ahmar Nadeem
 *
 * A final class containing the document specific properties
 */
public final class ConstantsDocument {

    /**
     * The upload type client.
     */
    public static final String UPLOAD_TYPE_CLIENT = "client";

    /**
     * The upload type server.
     */
    public static final String UPLOAD_TYPE_SERVER = "server";

    /**
     * The upload type client.
     */
    public static final String UPLOAD_TYPE_EXISTING = "existing";

    /**
     * The db file threshhold.
     */
    public static final int DB_FILE_THRESHHOLD = ( int ) Math.pow( 1024, 2 ); // Maximum file size in DB

    /**
     * The max allowed size for image.
     */
    public static final int MAX_ALLOWED_SIZE_FOR_IMAGE = 10 * DB_FILE_THRESHHOLD;

    /**
     * The default expiry in seconds
     */
    public static final int DEFAULT_EXPIRY = ( int ) ( Math.pow( 60, 2 ) * 24 ); // 24 hours

    /**
     * a constant representing bytes in a kilo byte
     */
    public static final long BYTES_IN_KB = 1024;

    /**
     * The Constant REGISTERED_IMAGE_TYPES.
     */
    public static final String[] REGISTERED_IMAGE_TYPES = { "jpg", "jpeg", "png", "gif", "pcx", "pbm" };

    /**
     * the json content type for the response header
     */
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    /**
     * the document origin placeholder
     */
    public static final String DOCUMENT_AGENT = "agent";

    /**
     * the image document type placeholder
     */
    public static final String DOCUMENT_TYPE_IMAGE = "image";

    /**
     * the text document type placeholder
     */
    public static final String DOCUMENT_TYPE_TEXT = "text";

    /**
     * the movie document type placeholder
     */
    public static final String DOCUMENT_TYPE_MOVIE = "movie";

    /**
     * the zip document type placeholder
     */
    public static final String DOCUMENT_TYPE_ZIP = "application/zip";

    /**
     * the zip extension placeholder
     */
    public static final String DOCUMENT_EXTENSION_ZIP = "zip";

    /**
     * default extension to be used for images
     */
    public static final String DEFAULT_IMAGE_TYPE = "jpg";

    /**
     * documents config file name in the karaf
     */
    public static final String DOCUMENT_PROPERTIES_FILE_NAME = "documentConfig.properties";

    /**
     * max image upload size property name in the config file
     */
    public static final String MAX_UPLOAD_IMAGE_SIZE_PROPERTY = "max.upload.image.size";

    /**
     * default location of the documents to be uploaded property name
     */
    public static final String DEFAULT_FILE_UPLOAD_LOCATION_PROPERTY = "default.file.upload.location";

    /**
     * FE temp location of the documents to be uploaded property name
     */
    public static final String FE_TEMP_FILE_UPLOAD_LOCATION_PROPERTY = "fe.temp.file.upload.location";

    private ConstantsDocument() {

    }

}
