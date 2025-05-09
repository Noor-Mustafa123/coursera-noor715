package de.soco.software.simuspace.suscore.transfer.manager;

import java.util.Map;

import de.soco.software.simuspace.suscore.common.http.model.AcknowledgeBytes;
import de.soco.software.simuspace.suscore.common.http.model.HttpReceiver;
import de.soco.software.simuspace.suscore.common.http.model.HttpSender;

/**
 * The interface TransferManager provides API of transferring and receiving files and directories.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface SusTransferManager {

    /**
     * Acknowledge check sum.
     *
     * @param map
     *         the map
     *
     * @return the map
     */
    Map< String, String > acknowledgeCheckSum( Map< String, String > map );

    /**
     * Detect file separator.
     *
     * @return the map
     */
    Map< String, String > detectFileSeparator();

    /**
     * Acknowledge bytes.
     *
     * @param map
     *         the map
     *
     * @return the acknowledge bytes
     */
    AcknowledgeBytes acknowledgeBytes( Map< String, String > map );

    /**
     * Send file.
     *
     * @param httpSender
     *         the http sender
     *
     * @return the string
     */
    String sendFile( HttpSender httpSender );

    /**
     * Receive file.
     *
     * @param httpReceiver
     *         the http receiver
     *
     * @return the string
     */
    String receiveFile( HttpReceiver httpReceiver );

    /**
     * Creates the directory.
     *
     * @param map
     *         the map
     *
     * @return true, if successful
     */
    boolean createDirectory( Map< String, String > map );

    /**
     * Sync file.
     *
     * @param httpSender
     *         the http sender
     *
     * @return the string
     */
    String syncFile( HttpSender httpSender );

    /**
     * Removes the file.
     *
     * @param map
     *         the map
     *
     * @return true, if successful
     */
    boolean removeFile( Map< String, String > map );

}
