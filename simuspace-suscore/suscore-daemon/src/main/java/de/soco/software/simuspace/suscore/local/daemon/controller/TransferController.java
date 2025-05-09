package de.soco.software.simuspace.suscore.local.daemon.controller;

import org.springframework.http.ResponseEntity;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;

/**
 * The Interface TransferController.
 *
 * @author sir huzaifa
 */
public interface TransferController {

    /**
     * Gets the transfer UI.
     *
     * @return the transfer UI
     */
    ResponseEntity< SusResponseDTO > getTransferUI( String authToken );

    /**
     * Gets the transfer status.
     *
     * @param authToken
     *         the auth token
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the transfer status
     */
    ResponseEntity< SusResponseDTO > getTransferStatus( String authToken, String objectFilterJson );

    /**
     * Gets the trasfer context.
     *
     * @param authToken
     *         the auth token
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the trasfer context
     */
    ResponseEntity< SusResponseDTO > getTrasferContext( String authToken, String objectFilterJson );

    /**
     * Gets the transfer view.
     *
     * @param authToken
     *         the auth token
     *
     * @return the file jobs view
     */
    public ResponseEntity< SusResponseDTO > getTransferView( String authToken );

    /**
     * Save transfer view.
     *
     * @param authToken
     *         the auth token
     * @param viewJson
     *         the view json
     *
     * @return the response entity
     */
    public ResponseEntity< SusResponseDTO > saveTransferView( String authToken, String viewJson );

    /**
     * Sets the transfer view as default.
     *
     * @param authToken
     *         the auth token
     * @param viewId
     *         the view id
     *
     * @return the response entity
     */
    public ResponseEntity< SusResponseDTO > setTransferViewAsDefault( String authToken, String viewId );

    /**
     * Update transfer view.
     *
     * @param authToken
     *         the auth token
     * @param viewId
     *         the view id
     * @param viewJson
     *         the view json
     *
     * @return the response entity
     */
    public ResponseEntity< SusResponseDTO > updateTransferView( String authToken, String viewId, String viewJson );

    /**
     * Delete transfer view.
     *
     * @param authToken
     *         the auth token
     * @param viewId
     *         the view id
     *
     * @return the response entity
     */
    public ResponseEntity< SusResponseDTO > deleteTransferView( String authToken, String viewId );

}
