package de.soco.software.simuspace.suscore.local.daemon.controller;

import org.springframework.http.ResponseEntity;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;

/**
 * The Interface SyncController is for object download and upload.
 */
public interface SyncController {

    /**
     * Checks if is up.
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > isUp();

    /**
     * Gets all properties.
     *
     * @return the all properties
     */
    ResponseEntity< SusResponseDTO > getAllProperties();

    /**
     * Gets the all objects context.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the all objects context
     */
    ResponseEntity< SusResponseDTO > getAllObjectsContext( String containerId, String typeId, String authToken, String objectFilterJson );

    /**
     * Download objects.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param selectionId
     *         the selection id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > downloadObjects( String containerId, String authToken, String selectionId );

    /**
     * Upload objects.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param selectionId
     *         the selection id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > uploadObjects( String containerId, String authToken, String selectionId );

    /**
     * Checkout objects.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param selectionId
     *         the selection id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > checkoutObjects( String containerId, String authToken, String selectionId );

    /**
     * Discard objects.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param selectionId
     *         the selection id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > discardObjects( String containerId, String authToken, String selectionId );

    /**
     * Checkin objects.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param selectionId
     *         the selection id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > checkinObjects( String containerId, String authToken, String selectionId );

    /**
     * Stop uploading objects.
     *
     * @param authToken
     *         the auth token
     * @param selectionId
     *         the selection id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > performAbortOperation( String authToken, String selectionId );

    /**
     * Drag and save file.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param filePath
     *         the file path
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > dragAndSaveFile( String containerId, String typeId, String authToken, String filePath );

    /**
     * **************************************** project local view ****************************************.
     *
     * @param containerId
     *            the container id
     * @param authToken
     *            the auth token
     * @return the all project local views by project id
     */

    /**
     * Gets the all project local views by project id.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     *
     * @return the all project local views by project id
     */
    ResponseEntity< SusResponseDTO > getAllProjectLocalViewsByProjectId( String containerId, String authToken );

    /**
     * Save project local view by project id.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > saveProjectLocalViewByProjectId( String projectId, String objectJson, String authToken );

    /**
     * Sets the project local view as default by project id.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > setProjectLocalViewAsDefaultByProjectId( String projectId, String viewId, String authToken );

    /**
     * Delete project local view by project id.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > deleteProjectLocalViewByProjectId( String projectId, String viewId, String authToken );

    /**
     * Update project local view by project id.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > updateProjectLocalViewByProjectId( String projectId, String viewId, String objectJson,
            String authToken );

    /**
     * **************************************** project sync view ****************************************.
     *
     * @param containerId
     *            the container id
     * @param authToken
     *            the auth token
     * @return the all project sync views by project id
     */

    /**
     * Gets the all project sync views by project id.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     *
     * @return the all project sync views by project id
     */
    ResponseEntity< SusResponseDTO > getAllProjectSyncViewsByProjectId( String containerId, String authToken );

    /**
     * Save project sync view by project id.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > saveProjectSyncViewByProjectId( String projectId, String objectJson, String authToken );

    /**
     * Sets the project sync view as default by project id.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > setProjectSyncViewAsDefaultByProjectId( String projectId, String viewId, String authToken );

    /**
     * Delete project sync view by project id.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > deleteProjectSyncViewByProjectId( String projectId, String viewId, String authToken );

    /**
     * Update project sync view by project id.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > updateProjectSyncViewByProjectId( String projectId, String viewId, String objectJson,
            String authToken );

    /**
     * Download document.
     *
     * @param authToken
     *         the auth token
     * @param downloadUrlJson
     *         the download url json
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > downloadDocument( String authToken, String downloadUrlJson );

    /**
     * Creates the structure in local sync dir.
     *
     * @param projectId
     *         the project id
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > createStructureInLocalSyncDir( String projectId, String authToken );

    /**
     * Gets the server files.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param authToken
     *         the auth token
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the server files
     */
    ResponseEntity< SusResponseDTO > getAllObjects( String projectId, String typeId, String authToken, String objectFilterJson );

    /**
     * Gets the server files UI.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param authToken
     *         the auth token
     *
     * @return the server files UI
     */
    ResponseEntity< SusResponseDTO > getAllObjectsUI( String projectId, String typeId, String authToken );

    /**
     * Open data object.
     *
     * @param containerId
     *         the container id
     * @param authToken
     *         the auth token
     * @param selectionId
     *         the selection id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > openDataObject( String containerId, String authToken, String selectionId, String app );

    /**
     * Export data object.
     *
     * @param authToken
     *         the auth token
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > exportDataObject( String authToken, String containerId, String selectionId, String osPath );

    /**
     * Recursively download structural objects.
     *
     * @param dataObjectDTO
     *         the data object DTO
     * @param isDir
     *         the is dir
     * @param exportPath
     *         the export path
     * @param authToken
     *         the auth token
     */
    void recursivelyDownloadStructuralObjects( DataObjectDTO dataObjectDTO, boolean isDir, String exportPath, String authToken );

}
