/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.document.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.UUID;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.document.constants.ConstantsDocumentServiceEndPoints;

/**
 * The Interface DocumentService for document related crud options.
 *
 * @author MNasir.farooq, ahmar.nadeem
 * @since 1.0
 */
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface DocumentService {

    /**
     * Gets the document list.
     *
     * @return javax.ws.rs.core.Response
     *
     * @url /
     * @method POST
     * @responsePayload {"success":true,"data":[{"name":"100670-1920x1080","type":"image/jpeg","fileExt":"jpeg","format":"","isTemp":false,
     * "isInDB" :false ,"properties":"","id":336,"userId":85,"url":null,"size":0,"thumbnail":null,"expiry":7200,"createdOn":1439635650651
     * },{"name": "100670-1920x1080"
     * ,"type":"image/jpeg","fileExt":"jpeg","format":"","isTemp":false,"isInDB":false,"properties":"","id":335,"userId" :85,"url":null
     * ,"size":0,"thumbnail":null,"expiry":7200,"createdOn":1439622285530},{"name":"20141116_140826","type":"image/jpeg" ,"fileExt":"jpeg"
     * ,"format":"","isTemp":false,"isInDB":false,"properties":"","id":334,"userId":107,"url":null,"size":0,"thumbnail" :null,"expiry":
     * 7200,"createdOn":1439461249648},{"name":"Screenshot-122","type":"image/png","fileExt":"png","format":"","isTemp" :true,"isInDB":
     * false,"properties":"","id":333,"userId":85,"url":null,"size":0,"thumbnail":null,"expiry":7200,"createdOn" :1439020437741}
     * @see javax.ws.rs.core.Response
     */
    @POST
    @Path( ConstantsDocumentServiceEndPoints.GET_ALL_DOCUMENTS )
    @Produces( MediaType.APPLICATION_JSON )
    Response getDocumentList( String filterJSON );

    /**
     * Gets the top document list by user id.
     *
     * @return javax.ws.rs.core.Response
     *
     * @url / my
     * @method POST
     * @responsePayload {"success":true,"data":[{"name":"100670-1920x1080","type":"image/jpeg","fileExt":"jpeg","format":"","isTemp":false,
     * "isInDB" :false ,"properties":"","id":336,"userId":85,"url":null,"size":0,"thumbnail":null,"expiry":7200,"createdOn":1439635650651
     * },{"name": "100670-1920x1080"
     * ,"type":"image/jpeg","fileExt":"jpeg","format":"","isTemp":false,"isInDB":false,"properties":"","id":335,"userId" :85,"url":null
     * ,"size":0,"thumbnail":null,"expiry":7200,"createdOn":1439622285530},{"name":"Screenshot-122","type":"image/png" ,"fileExt" :"png"
     * ,"format":"","isTemp":true,"isInDB":false,"properties":"","id":333,"userId":85,"url":null,"size":0, "thumbnail":null,"expiry" :7200
     * ,"createdOn":1439020437741},{"name":"carsimulation_without_matdb_chota.inp","type":"application/octet-stream" ,"fileExt":"" ,"format"
     * :"","isTemp":true,"isInDB":false,"properties":"","id":332,"userId":85,"url":null,"size":0,"thumbnail" :null,"expiry":7200
     * ,"createdOn" :1439020366729},{"name":"carsimulation.inp","type":"application/octet-stream","fileExt":"","format":"","isTemp":true
     * ,"isInDB":false ,"properties":"","id":330,"userId":85,"url":null,"size":0,"thumbnail":null,"expiry":7200,"createdOn":
     * 1436001277179}]}
     * @see javax.ws.rs.core.Response
     */
    @POST
    @Path( ConstantsDocumentServiceEndPoints.GET_ALL_DOCUMENTS_BY_USER )
    Response getDocumentListByUserId( @PathParam( ConstantsDocumentServiceEndPoints.USER_ID ) UUID userId, String filterJSON );

    /**
     * Gets the document.
     *
     * @param documentId
     *         the document id
     *
     * @return javax.ws.rs.core.Response
     *
     * @url /{documentId}
     * @method GET
     * @responsePayload
     * @see javax.ws.rs.core.Response
     */
    @GET
    @Path( ConstantsDocumentServiceEndPoints.GET_DOCUMENT_BY_ID )
    Response getDocumentById( @PathParam( ConstantsDocumentServiceEndPoints.DOCUMENT_ID ) UUID documentId,
            @PathParam( ConstantsDocumentServiceEndPoints.VERSION ) int version );

    /**
     * Delete document.
     *
     * @param documentId
     *         the document id
     *
     * @return javax.ws.rs.core.Response
     *
     * @url /{documentId}
     * @method DELETE
     * @responsePayload {"success":true,"message":{"type":"SUCCESS","content":"Record deleted successfully."}}
     * @see javax.ws.rs.core.Response
     */
    @DELETE
    @Path( ConstantsDocumentServiceEndPoints.DELETE_DOCUMENT_BY_ID )
    Response deleteDocument( @PathParam( ConstantsDocumentServiceEndPoints.DOCUMENT_ID ) UUID documentId,
            @PathParam( ConstantsDocumentServiceEndPoints.VERSION ) int version );

    /**
     * Download content of the document.
     *
     * @param documentId
     *         the document id
     *
     * @return {@link MediaType}.APPLICATION_OCTET_STREAM
     *
     * @url /{documentId}/{versionId}/download
     * @method GET
     * @see {@link MediaType}
     */
    @GET
    @Path( ConstantsDocumentServiceEndPoints.DOWNLOAD_DOCUMENT_BY_ID )
    @Produces( MediaType.APPLICATION_OCTET_STREAM )
    Response downloadDocument( @PathParam( ConstantsDocumentServiceEndPoints.OBJECT_ID ) UUID documentId,
            @PathParam( ConstantsDocumentServiceEndPoints.VERSION ) int version );

}
