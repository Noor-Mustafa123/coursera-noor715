package de.soco.software.simuspace.suscore.location.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The Interface SsfsService.
 */
public interface SsfsService {

    /**
     * Gets the location list.
     *
     * @return the location list
     */
    @GET
    @Path( "/location" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLocationList();

    /**
     * Save user url in history.
     *
     * @param id
     *         the id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/history/location/{locationId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveUserUrlInHistory( @PathParam( "locationId" ) String id, String objectJson );

    @POST
    @Path( "/files/content" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFileContent( String objectJson );

    /**
     * Gets the user url.
     *
     * @param id
     *         the id
     *
     * @return the user url
     */
    @GET
    @Path( "/history/location/{locationId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getUserUrl( @PathParam( "locationId" ) String id );

    /**
     * Gets file list.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/files" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFileList( String objectJson );

}
