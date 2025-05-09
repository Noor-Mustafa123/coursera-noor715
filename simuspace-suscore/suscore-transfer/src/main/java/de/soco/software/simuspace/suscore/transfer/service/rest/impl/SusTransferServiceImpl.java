package de.soco.software.simuspace.suscore.transfer.service.rest.impl;

import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.http.model.HttpReceiver;
import de.soco.software.simuspace.suscore.common.http.model.HttpSender;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.transfer.manager.SusTransferManager;
import de.soco.software.simuspace.suscore.transfer.service.rest.SusTransferService;

/**
 * The Class TransferServiceImpl provides functionality of transferring and receiving files and directories.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class SusTransferServiceImpl extends SuSBaseService implements SusTransferService {

    /**
     * The transfer manager.
     */
    private SusTransferManager transferManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createDirectory( String createFile ) {
        Map< String, String > mapReturn;
        mapReturn = JsonUtils.convertStringToMap( createFile );
        if ( transferManager.createDirectory( mapReturn ) ) {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.DIR_CREATED.getKey() ), true );
        } else {
            return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.DIR_NOT_CREATED.getKey() ) );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response receiveFile( String receiveFileParameterString ) {
        HttpReceiver httpReceiver = JsonUtils.jsonToObject( receiveFileParameterString, HttpReceiver.class );
        return ResponseUtils.success( transferManager.receiveFile( httpReceiver ) );
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public Response sendFile( String sendFileParameterString ) {
        HttpSender httpSender = JsonUtils.jsonToObject( sendFileParameterString, HttpSender.class );
        return ResponseUtils.success( transferManager.sendFile( httpSender ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response acknowledgeBytes( String filePathAsString ) {
        Map< String, String > map = new HashMap<>();
        map = ( Map< String, String > ) JsonUtils.jsonToMap( filePathAsString, map );
        return ResponseUtils.success( transferManager.acknowledgeBytes( map ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response acknowledgeCheckSum( String filePath ) {
        Map< String, String > map = JsonUtils.convertStringToMap( filePath );
        return ResponseUtils.success( transferManager.acknowledgeCheckSum( map ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response detectFileSeparator() {
        return ResponseUtils.success( transferManager.detectFileSeparator() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response syncFiles( String sendFileParameterString ) {
        HttpSender httpSender = JsonUtils.jsonToObject( sendFileParameterString, HttpSender.class );
        return ResponseUtils.success( transferManager.syncFile( httpSender ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response removeFile( String filePathAsString ) {
        Map< String, String > map = JsonUtils.convertStringToMap( filePathAsString );
        return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.FILE_REMOVED.getKey() ),
                transferManager.removeFile( map ) );
    }

    /**
     * Gets the transfer manager.
     *
     * @return the transfer manager
     */
    public SusTransferManager getTransferManager() {
        return transferManager;
    }

    /**
     * Sets the transfer manager.
     *
     * @param transferManager
     *         the new transfer manager
     */
    public void setTransferManager( SusTransferManager transferManager ) {
        this.transferManager = transferManager;
    }

}