package de.soco.software.simuspace.suscore.local.daemon.controller.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.base.TransferManager;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.http.model.TransferInfo;
import de.soco.software.simuspace.suscore.common.model.ProgressBar;
import de.soco.software.simuspace.suscore.common.model.TransferDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.local.daemon.controller.TransferController;
import de.soco.software.simuspace.suscore.local.daemon.manager.SuscoreDaemonManager;
import de.soco.software.simuspace.suscore.local.daemon.properties.DaemonPropertiesManager;

/**
 * The Class TransferControllerImpl. used for handling transferring of files
 */
@RestController
@RequestMapping( value = "api/transfer" )
public class TransferControllerImpl implements TransferController {

    private static final String DATA_PROJECT = "data/project/";

    private static final String DATA_TRANSFER = "data/transfer";

    /**
     * The Constant VIEW_ID_PARAM.
     */
    private static final String VIEW_ID_PARAM = "{viewId}";

    /**
     * The transfer manager.
     */
    @Autowired
    private TransferManager transferManager;

    /**
     * The daemon manager.
     */
    @Autowired
    private SuscoreDaemonManager daemonManager;

    /**
     * Gets the daemon manager.
     *
     * @return the daemon manager
     */
    public SuscoreDaemonManager getDaemonManager() {
        return daemonManager;
    }

    /**
     * Sets the daemon manager.
     *
     * @param daemonManager
     *         the new daemon manager
     */
    public void setDaemonManager( SuscoreDaemonManager daemonManager ) {
        this.daemonManager = daemonManager;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.TransferController#getTransferUI()
     */
    @Override
    @RequestMapping( value = "ui", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getTransferUI( @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            return new ResponseEntity<>(
                    ResponseUtils.successResponse( new TableUI(
                            GUIUtils.getColumnList( DaemonPropertiesManager.hasTranslation(), authToken, TransferDTO.class ) ) ),
                    HttpStatus.OK );
        } catch ( SusException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.TransferController#getTransferStatus(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = "list", method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > getTransferStatus( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String objectFilterJson ) {
        try {
            FiltersDTO filter = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            List< TransferDTO > results = generateTransferData();
            Collections.reverse( results );
            filter.setTotalRecords( ( long ) results.size() );
            filter.setFilteredRecords( ( long ) results.size() );
            results = PaginationUtil.getPaginatedList( results, filter );
            FilteredResponse< TransferDTO > response = PaginationUtil.constructFilteredResponse( filter, results );
            return new ResponseEntity<>( ResponseUtils.successResponse( response ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            StringWriter sw = new StringWriter();
            e.printStackTrace( new PrintWriter( sw ) );
            String exceptionAsString = sw.toString();
            return new ResponseEntity<>( ResponseUtils.failureResponse( exceptionAsString, null ), HttpStatus.OK );
        }
    }

    /**
     * Generate transfer data.
     *
     * @return the list
     */
    private List< TransferDTO > generateTransferData() {

        List< TransferDTO > list = new ArrayList<>();
        Map< String, TransferInfo > transfers = transferManager.getTransfers();
        for ( String name : transfers.keySet() ) {
            list.add( new TransferDTO( name, transfers.get( name ).getAction(), new ProgressBar( 100L, transfers.get( name ).getProgess() ),
                    transfers.get( name ).getFileFullAdress() ) );
        }
        return list;
    }

    /**
     * Gets the transfer manager.
     *
     * @return the transfer manager
     */
    public TransferManager getTransferManager() {
        return transferManager;
    }

    /**
     * Sets the transfer manager.
     *
     * @param transferManager
     *         the new transfer manager
     */

    public void setTransferManager( TransferManager transferManager ) {
        this.transferManager = transferManager;
    }

    @Override
    @RequestMapping( value = "/context", method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > getTrasferContext( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String objectFilterJson ) {
        try {

            SusResponseDTO request = SuSClient.postRequest( daemonManager.getServerAPIBase() + "data/project/transfer/context",
                    objectFilterJson, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            String json = JsonUtils.toJson( request.getData() );
            List< ContextMenuItem > context = JsonUtils.jsonToList( json, ContextMenuItem.class );

            return new ResponseEntity<>( ResponseUtils.successResponse( context ), HttpStatus.OK );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#getSusJobsView(java.lang.String)
     */
    @Override
    @RequestMapping( value = ConstantsViewEndPoints.SAVE_OR_LIST_VIEW, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getTransferView( @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {

            String url = daemonManager.getServerAPIBase() + DATA_TRANSFER + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#saveSusJobsView(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = ConstantsViewEndPoints.SAVE_OR_LIST_VIEW, method = RequestMethod.POST )
    public ResponseEntity< SusResponseDTO > saveTransferView( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @RequestBody String viewJson ) {
        try {
            String url = daemonManager.getServerAPIBase() + DATA_TRANSFER + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW;
            SusResponseDTO reponse = SuSClient.postRequest( url, viewJson, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#saveSusJobsView(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW, method = RequestMethod.PUT )
    public ResponseEntity< SusResponseDTO > updateTransferView( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "viewId" ) String viewId, @RequestBody String viewJson ) {
        try {
            String path = ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW.replace( VIEW_ID_PARAM, viewId );
            String url = daemonManager.getServerAPIBase() + DATA_TRANSFER + path;
            SusResponseDTO reponse = SuSClient.putRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ), viewJson );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#saveSusJobsView(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW, method = RequestMethod.DELETE )
    public ResponseEntity< SusResponseDTO > deleteTransferView( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "viewId" ) String viewId ) {
        try {
            String path = ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW.replace( VIEW_ID_PARAM, viewId );
            String url = daemonManager.getServerAPIBase() + DATA_TRANSFER + path;
            SusResponseDTO reponse = SuSClient.deleteRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.local.daemon.controller.JobController#setSusJobsViewAsDefault(java.lang.String, java.lang.String)
     */
    @Override
    @RequestMapping( value = ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT, method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > setTransferViewAsDefault( @RequestHeader( value = "X-Auth-Token" ) String authToken,
            @PathVariable( "viewId" ) String viewId ) {
        try {
            String path = ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT.replace( VIEW_ID_PARAM, viewId );
            String url = daemonManager.getServerAPIBase() + DATA_TRANSFER + path;
            SusResponseDTO reponse = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            return new ResponseEntity<>( reponse, HttpStatus.OK );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

}
