package de.soco.software.simuspace.suscore.transfer.manager.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantSuscoreApi;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.http.model.AcknowledgeBytes;
import de.soco.software.simuspace.suscore.common.http.model.HttpReceiver;
import de.soco.software.simuspace.suscore.common.http.model.HttpSender;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.transfer.manager.SusTransferManager;

/**
 * The Class TransferManagerImpl provides functionality of transferring and receiving files and directories.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class SusTransferManagerImpl implements SusTransferManager {

    /**
     * The Constant SYNC_PATH.
     */
    private static final String SYNC_PATH = "destSyncPath";

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    @Override
    public Map< String, String > acknowledgeCheckSum( Map< String, String > map ) {
        if ( map == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MAP_NOT_PROVIDED_FOR_CHECKSUM.getKey() ) );
        }
        String unHexfilePath = new String( FileUtils.convertBase64StringToByteArray( map.get( "file" ) ) );
        File filePath = new File( unHexfilePath );
        if ( filePath.exists() ) {
            return FileUtils.fileSignatureForVaultFile( filePath );
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), filePath.getName() ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, String > detectFileSeparator() {
        return FileUtils.detectOSFileSeparator();
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    @Override
    public AcknowledgeBytes acknowledgeBytes( Map< String, String > map ) {
        return FileUtils.acknowledgeBytes( map );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String sendFile( HttpSender httpSender ) {
        String response = null;
        httpSender.setHeaders( prepareHeaders() );
        if ( StringUtils.isEmpty( httpSender.getDestinationFilePath() ) ) {
            httpSender.setDestinationFilePath( detectLocalSyncPath( httpSender ) );
        }
        if ( httpSender.getSrcFile().isFile() ) {
            response = FileUtils.sendBytesFromFile( httpSender );
        } else if ( httpSender.getSrcFile().isDirectory() ) {
            response = FileUtils.sendDirectory( httpSender );
        }
        return response;
    }

    @Override
    public String syncFile( HttpSender httpSender ) {
        String response = null;
        httpSender.setHeaders( prepareHeaders() );
        if ( StringUtils.isEmpty( httpSender.getDestinationFilePath() ) ) {
            httpSender.setDestinationFilePath( detectLocalSyncPath( httpSender ) );
        }

        if ( httpSender.getSrcFile().isFile() ) {
            response = FileUtils.syncFile( httpSender );
        } else if ( httpSender.getSrcFile().isDirectory() ) {
            // sync directory later
        }
        return response;
    }

    @Override
    public boolean removeFile( Map< String, String > map ) {
        return FileUtils.removeFile( map );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String receiveFile( HttpReceiver httpReceiver ) {
        return FileUtils.writeBytesInFile( httpReceiver );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createDirectory( Map< String, String > map ) {
        return FileUtils.createDirectory( map );
    }

    /**
     * Prepare headers.
     *
     * @return the map
     */
    private Map< String, String > prepareHeaders() {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        return requestHeaders;
    }

    /**
     * Detect file separator.
     *
     * @param httpSender
     *         the http sender
     *
     * @return the string
     */
    private static String detectLocalSyncPath( HttpSender httpSender ) {
        SusResponseDTO susResponseDTOForSyncLocalDir = SuSClient
                .getRequest( httpSender.getLocation() + ConstantSuscoreApi.DETECT_LOCAL_SYNC, httpSender.getHeaders() );
        String json = JsonUtils.toJson( susResponseDTOForSyncLocalDir.getData() );
        Map< String, String > mapSyncPath = JsonUtils.convertStringToMap( json );
        return new String( FileUtils.convertBase64StringToByteArray( mapSyncPath.get( SYNC_PATH ) ) );
    }

}