package de.soco.software.simuspace.suscore.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ComputedDataObject is to save all the valid information related to object and then pass it to tasferManager for data object to
 * upload.
 *
 * @author noman.arshad
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class ComputedDataObject {

    /**
     * The file id.
     */
    private String fileId;

    /**
     * The file.
     */
    private FileInfo file;

    /**
     * The container id.
     */
    private String containerId;

    /**
     * The file name.
     */
    private String fileName;

    /**
     * The auth token.
     */
    private String authToken;

    /**
     * The object id.
     */
    private String objectId;

    /**
     * The object type.
     */
    private String objectType;

    /**
     * The file path.
     */
    private String filePath;

    /**
     * The check in file to upload.
     */
    private boolean checkInFileToUpload;

    /**
     * The check out file to download.
     */
    private boolean checkOutFileToDownload;

    /**
     * The is dir.
     */
    boolean isDir;

    /**
     * The abort.
     */
    boolean abort = false;

    /**
     * The complete.
     */
    boolean complete;

    /**
     * The size.
     */
    private String size;

    /**
     * The location.
     */
    List< CommonLocationDTO > location;

    /**
     * Instantiates a new computed data object.
     *
     * @param containerId
     *         the container id
     * @param file
     *         the file
     * @param authToken
     *         the auth token
     * @param checkOutFileToDownload
     *         the check out file to download
     */
    public ComputedDataObject( String containerId, FileInfo file, String authToken, boolean checkOutFileToDownload ) {
        this.containerId = containerId;
        this.file = file;
        this.authToken = authToken;
        this.checkOutFileToDownload = checkOutFileToDownload;
    }

    /**
     * Checks if is check in file to upload.
     *
     * @return true, if is check in file to upload
     */
    public boolean isCheckInFileToUpload() {
        return checkInFileToUpload;
    }

    /**
     * Sets the check in file to upload.
     *
     * @param checkInFileToUpload
     *         the new check in file to upload
     */
    public void setCheckInFileToUpload( boolean checkInFileToUpload ) {
        this.checkInFileToUpload = checkInFileToUpload;
    }

    /**
     * Gets the file path.
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path.
     *
     * @param filePath
     *         the new file path
     */
    public void setFilePath( String filePath ) {
        this.filePath = filePath;
    }

    /**
     * Checks if is complete.
     *
     * @return true, if is complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Sets the complete.
     *
     * @param complete
     *         the new complete
     */
    public void setComplete( boolean complete ) {
        this.complete = complete;
    }

    /**
     * Instantiates a new computed data object.
     *
     * @param containerId
     *         the container id
     * @param fileName
     *         the file name
     * @param authToken
     *         the auth token
     * @param objectId
     *         the object id
     * @param objectType
     *         the object type
     * @param filePath
     *         the file path
     * @param isDir
     *         the is dir
     * @param abort
     *         the abort
     * @param size
     *         the size
     */
    public ComputedDataObject( String containerId, String fileName, String authToken, String objectId, String objectType, String filePath,
            boolean isDir, boolean abort, String size ) {
        super();
        this.containerId = containerId;
        this.fileName = fileName;
        this.authToken = authToken;
        this.objectId = objectId;
        this.objectType = objectType;
        this.isDir = isDir;
        this.abort = abort;
        this.filePath = filePath;
        this.size = size;
    }

    public ComputedDataObject( String containerId, String fileName, String authToken, String objectId, String objectType, String filePath,
            boolean isDir, boolean abort, String size, List< CommonLocationDTO > location ) {
        super();
        this.containerId = containerId;
        this.fileName = fileName;
        this.authToken = authToken;
        this.objectId = objectId;
        this.objectType = objectType;
        this.isDir = isDir;
        this.abort = abort;
        this.filePath = filePath;
        this.size = size;
        this.location = location;
    }

    /**
     * Instantiates a new computed data object.
     *
     * @param containerId
     *         the container id
     * @param fileName
     *         the file name
     * @param authToken
     *         the auth token
     * @param objectId
     *         the object id
     * @param objectType
     *         the object type
     * @param filePath
     *         the file path
     * @param isDir
     *         the is dir
     * @param abort
     *         the abort
     * @param checkInFileToUpload
     *         the check in file to upload
     * @param fileId
     *         the file id
     * @param size
     *         the size
     */
    public ComputedDataObject( String containerId, String fileName, String authToken, String objectId, String objectType, String filePath,
            boolean isDir, boolean abort, boolean checkInFileToUpload, String fileId, String size ) {
        super();
        this.containerId = containerId;
        this.fileName = fileName;
        this.authToken = authToken;
        this.objectId = objectId;
        this.objectType = objectType;
        this.isDir = isDir;
        this.abort = abort;
        this.filePath = filePath;
        this.checkInFileToUpload = checkInFileToUpload;
        this.fileId = fileId;
        this.size = size;
    }

    /**
     * Checks if is dir.
     *
     * @return true, if is dir
     */
    public boolean isDir() {
        return isDir;
    }

    /**
     * Sets the dir.
     *
     * @param isDir
     *         the new dir
     */
    public void setDir( boolean isDir ) {
        this.isDir = isDir;
    }

    /**
     * Gets the container id.
     *
     * @return the container id
     */
    public String getContainerId() {
        return containerId;
    }

    /**
     * Sets the container id.
     *
     * @param containerId
     *         the new container id
     */
    public void setContainerId( String containerId ) {
        this.containerId = containerId;
    }

    /**
     * Gets the file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName
     *         the new file name
     */
    public void setFileName( String fileName ) {
        this.fileName = fileName;
    }

    /**
     * Gets the auth token.
     *
     * @return the auth token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken
     *         the new auth token
     */
    public void setAuthToken( String authToken ) {
        this.authToken = authToken;
    }

    /**
     * Gets the object id.
     *
     * @return the object id
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Sets the object id.
     *
     * @param objectId
     *         the new object id
     */
    public void setObjectId( String objectId ) {
        this.objectId = objectId;
    }

    /**
     * Gets the object type.
     *
     * @return the object type
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * Sets the object type.
     *
     * @param objectType
     *         the new object type
     */
    public void setObjectType( String objectType ) {
        this.objectType = objectType;
    }

    /**
     * Checks if is abort.
     *
     * @return true, if is abort
     */
    public boolean isAbort() {
        return abort;
    }

    /**
     * Sets the abort.
     *
     * @param abort
     *         the new abort
     */
    public void setAbort( boolean abort ) {
        this.abort = abort;
    }

    /**
     * Gets the file id.
     *
     * @return the file id
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * Sets the file id.
     *
     * @param fileId
     *         the new file id
     */
    public void setFileId( String fileId ) {
        this.fileId = fileId;
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public FileInfo getFile() {
        return file;
    }

    /**
     * Sets the file.
     *
     * @param file
     *         the new file
     */
    public void setFile( FileInfo file ) {
        this.file = file;
    }

    /**
     * Checks if is check out file to download.
     *
     * @return true, if is check out file to download
     */
    public boolean isCheckOutFileToDownload() {
        return checkOutFileToDownload;
    }

    /**
     * Sets the check out file to download.
     *
     * @param checkOutFileToDownload
     *         the new check out file to download
     */
    public void setCheckOutFileToDownload( boolean checkOutFileToDownload ) {
        this.checkOutFileToDownload = checkOutFileToDownload;
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( String size ) {
        this.size = size;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public List< CommonLocationDTO > getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location
     *         the new location
     */
    public void setLocation( List< CommonLocationDTO > location ) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ComputedDataObject [fileId=" + fileId + ", file=" + file + ", containerId=" + containerId + ", fileName=" + fileName
                + ", authToken=" + authToken + ", objectId=" + objectId + ", objectType=" + objectType + ", filePath=" + filePath
                + ", checkInFileToUpload=" + checkInFileToUpload + ", checkOutFileToDownload=" + checkOutFileToDownload + ", isDir=" + isDir
                + ", abort=" + abort + ", complete=" + complete + ", size=" + size + ", location=" + location + "]";
    }

}
