package de.soco.software.simuspace.suscore.common.constants;

/**
 * The Class ConstantSuscoreApi is responsible to provide mutual API for Sus.client and Master (Simuspace) as string
 *
 * @author Ahsan Khan
 */
public class ConstantSuscoreApi {

    /**
     * The Constant API_V1_SUSCORE.
     */
    public static final String API_V1_SUSCORE = "/api/suscore";

    /**
     * The Constant RECEIVE_FILE.
     */
    public static final String RECEIVE_FILE = "/receiveFile";

    /**
     * The Constant SEND_FILE.
     */
    public static final String SEND_FILE = "/sendFile";

    /**
     * The Constant ACKNOWLEDGE_BYTES.
     */
    public static final String ACKNOWLEDGE_BYTES = "/ackByte";

    /**
     * The Constant ACKNOWLEDGE_CHECKSUM.
     */
    public static final String ACKNOWLEDGE_CHECKSUM = "/checkSum";

    /**
     * The Constant CALCULATE_HASH.
     */
    public static final String CALCULATE_HASH = "/calcHash";

    /**
     * The Constant CREATE_DIRECTORY.
     */
    public static final String CREATE_DIRECTORY = "/mkDir";

    /**
     * The Constant DETECT_FILE_SEPARATOR.
     */
    public static final String DETECT_FILE_SEPARATOR = "/detectFileSeparator";

    /**
     * The Constant DETECT_LOCAL_SYNC.
     */
    public static final String DETECT_LOCAL_SYNC = "/localSync";

    /**
     * The Constant SYNC_PATH.
     */
    public static final String SYNC_FILES = "/syncFiles";

    /**
     * The Constant UPLOAD_FILE.
     */
    public static final String UPLOAD_FILE = "/upload";

    /**
     * The Constant REMOVE_FILE.
     */
    public static final String REMOVE_FILE = "/removeFile";

    /**
     * Private constructor to hide implicit one.
     */
    private ConstantSuscoreApi() {
        super();
    }

}
