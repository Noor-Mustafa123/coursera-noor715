package de.soco.software.simuspace.suscore.common.base;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.http.model.TransferInfo;
import de.soco.software.simuspace.suscore.common.model.CommonLocationDTO;
import de.soco.software.simuspace.suscore.common.model.FileInfo;

/**
 * The Interface TransferManager. Responsible for uploading and downloading objects
 */
public interface TransferManager {

    /**
     * Download data object.
     *
     * @param parentId
     *         the parent id
     * @param file
     *         the file
     * @param authToken
     *         the auth token
     *
     * @return true, if successful
     */
    boolean downloadDataObject( String parentId, FileInfo file, String authToken );

    /**
     * Upload data object.
     *
     * @param containerId
     *         the container id
     * @param fileName
     *         the file name
     * @param authToken
     *         the auth token
     * @param objectId
     *         the object id
     * @param typeId
     *         the type id
     * @param size
     *         the size
     *
     * @return true, if successful
     */
    boolean uploadDataObject( String containerId, String fileName, String authToken, String objectId, String typeId, String size );

    /**
     * Gets the transfers.
     *
     * @return the transfers
     */
    Map< String, TransferInfo > getTransfers();

    /**
     * Upload directory.
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
     *
     * @return true, if successful
     */
    boolean uploadDirectory( String containerId, String fileName, String authToken, String objectId, String objectType );

    boolean uploadDataObjectToLocations( String containerId, String fileName, String authToken, String objectId, String typeId, String size,
            List< CommonLocationDTO > locations );

    boolean downloadDataObjectFromLocation( String parentId, FileInfo file, String authToken, List< CommonLocationDTO > locations );

}
