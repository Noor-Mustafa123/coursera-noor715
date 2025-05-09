package de.soco.software.simuspace.suscore.core.location.manager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.cxf.message.Message;

import de.soco.software.simuspace.suscore.common.model.FileObject;
import de.soco.software.simuspace.suscore.common.model.TransferLocationObject;
import de.soco.software.simuspace.workflow.location.JobParametersLocationModel;

/**
 * Interface To Handle All business operations related To remote locations.
 *
 * @author M.Nasir.Farooq
 */
public interface LocationCoreManager {

    /**
     * Checks if is up.
     *
     * @return the string
     */
    String isUp();

    /**
     * Checks if is path exists.
     *
     * @param path
     *         the path
     *
     * @return true, if is path exists
     */
    boolean isPathExists( String path );

    /**
     * Checks if is valid token.
     *
     * @param authToken
     *         the auth token
     *
     * @return true, if is valid token
     */
    boolean isValidToken( String authToken );

    /**
     * Export vault file.
     *
     * @param transferObject
     *         the transfer object
     *
     * @return true, if successful
     */
    boolean exportVaultFile( TransferLocationObject transferObject );

    /**
     * Gets the vault file.
     *
     * @param id
     *         the id
     * @param version
     *         the version
     *
     * @return the vault file
     */
    File getVaultFile( UUID id, int version );

    /**
     * Export staging file.
     *
     * @param transferObject
     *         the transfer object
     *
     * @return true, if successful
     */
    boolean exportStagingFile( String userUid, TransferLocationObject transferObject );

    /**
     * Delete vault file.
     *
     * @param fileRelPath
     *         the file rel path
     *
     * @return true, if successful
     */
    boolean deleteVaultFile( String fileRelPath );

    /**
     * Run server job.
     *
     * @param jobParametersLocationModel
     *         the job parameters location model
     */
    void runServerJob( JobParametersLocationModel jobParametersLocationModel );

    /**
     * Execute WF process on server.
     *
     * @param jobParametersLocationModel
     *         the job parameters location model
     * @param message
     *         the message
     *
     * @throws IOException
     *         in case of error
     */
    void executeWFProcessOnServer( JobParametersLocationModel jobParametersLocationModel, Message message ) throws IOException;

    /**
     * Gets the file list.
     *
     * @param userName
     *         the user name
     * @param password
     *         the password
     * @param path
     *         the path
     * @param show
     *         the show
     *
     * @return the file list
     */
    List< FileObject > getFileList( String userName, String password, String path, String show );

    /**
     * Gets the user home.
     *
     * @param userName
     *         the user name
     * @param password
     *         the password
     *
     * @return the user home
     */
    String getUserHome( String userName, String password );

    /**
     * Run server system job.
     *
     * @param jobParametersLocationModel
     *         the job parameters location model
     */
    void runServerSystemJob( JobParametersLocationModel jobParametersLocationModel );

}
