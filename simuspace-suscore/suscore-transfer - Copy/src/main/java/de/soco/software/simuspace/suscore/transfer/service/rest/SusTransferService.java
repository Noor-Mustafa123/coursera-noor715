package de.soco.software.simuspace.suscore.transfer.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantSuscoreApi;
import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;

/**
 * The Interface TransferService provides API of transferring and receiving.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
@WebService
@Consumes( { MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface SusTransferService {

    /**
     * Acknowledge check sum.
     *
     * @param filePath
     *         the file path
     *
     * @return the response
     */
    @POST
    @Path( ConstantSuscoreApi.ACKNOWLEDGE_CHECKSUM )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response acknowledgeCheckSum( String filePath );

    /**
     * Detect file separator.
     *
     * @return the response
     */
    @GET
    @Path( ConstantSuscoreApi.DETECT_FILE_SEPARATOR )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response detectFileSeparator();

    /**
     * Acknowledge bytes.
     *
     * @param filePathAsString
     *         the file path as string
     *
     * @return the response
     */
    @POST
    @Path( ConstantSuscoreApi.ACKNOWLEDGE_BYTES )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response acknowledgeBytes( String filePathAsString );

    /**
     * Receive file.
     *
     * @param receiveFileParameterString
     *         the receive file parameter string
     *
     * @return the response
     */
    @POST
    @Path( ConstantSuscoreApi.RECEIVE_FILE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response receiveFile( String receiveFileParameterString );

    /**
     * Send file.
     *
     * @param sendFileParameterString
     *         the send file parameter string
     *
     * @return the response
     */
    @POST
    @Path( ConstantSuscoreApi.SEND_FILE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response sendFile( String sendFileParameterString );

    /**
     * Creates the directory.
     *
     * @param createFile
     *         the create file
     *
     * @return the response
     */
    @POST
    @Path( ConstantSuscoreApi.CREATE_DIRECTORY )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createDirectory( String createFile );

    /**
     * Changed on local.
     *
     * @return the response
     */
    @POST
    @Path( ConstantSuscoreApi.SYNC_FILES )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response syncFiles( String sendFileParameterString );

    /**
     * Removes the file.
     *
     * @param sendFileParameterString
     *         the send file parameter string
     *
     * @return the response
     */
    @POST
    @Path( ConstantSuscoreApi.REMOVE_FILE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response removeFile( String sendFileParameterString );

}