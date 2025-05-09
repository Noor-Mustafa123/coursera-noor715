package de.soco.software.simuspace.suscore.common.base;

import de.soco.software.simuspace.suscore.common.model.ComputedDataObject;

/**
 * The Class UploadObjectThread is to upload any objects to server.
 *
 * @author Noman Arshad
 */
public class UploadObjectThread extends UserThread {

    String containerId;

    String authToken;

    ComputedDataObject preparedFile;

    TransferManager transferManager;

    /**
     * Instantiates a new upload object thread.
     */
    public UploadObjectThread() {
    }

    /**
     * Instantiates a new upload object thread.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param addAndReturnUploadededFiles
     *         the add and return uploadeded files
     * @param localDir
     *         the local dir
     * @param computedFiles
     *         the computed files
     * @param transferManager
     *         the transfer manager
     */
    public UploadObjectThread( String containerId, String authToken, ComputedDataObject preparedFile, TransferManager transferManager ) {
        this.containerId = containerId;
        this.authToken = authToken;
        this.preparedFile = preparedFile;
        this.transferManager = transferManager;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.executor.model.UserThread#run()
     */
    @Override
    public void run() {
        transferManager.uploadDataObjectToLocations( containerId, preparedFile.getFileName(), authToken, preparedFile.getObjectId(),
                preparedFile.getObjectType(), preparedFile.getSize(), preparedFile.getLocation() );

    }

}
