package de.soco.software.simuspace.suscore.homepage.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;

/**
 * The interface Homepage service.
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( { MediaType.APPLICATION_JSON } )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface HomepageService {

    /**
     * Create widget form response.
     *
     * @return the response
     */
    @GET
    @Path( "/widget/ui/create" )
    Response createWidgetForm();

    /**
     * Create object form widget category response.
     *
     * @param widgetCategory
     *         the widget category
     *
     * @return the response
     */
    @GET
    @Path( "/widget/ui/create/{widgetCategory}" )
    Response createObjectFormWidgetCategory( @PathParam( "widgetCategory" ) String widgetCategory );

    /**
     * Gets widgets list.
     *
     * @return the widgets list
     */
    @GET
    @Path( "/widget/list" )
    Response getWidgetsList();

    /**
     * Add new widget response.
     *
     * @param widgetJSON
     *         the widget json
     *
     * @return the response
     */
    @POST
    @Path( "/widget" )
    Response addNewWidget( String widgetJSON );

    /**
     * Update widget response.
     *
     * @param widgetJSON
     *         the widget json
     *
     * @return the response
     */
    @PUT
    @Path( "/widget" )
    Response updateWidget( String widgetJSON );

    /**
     * Delete widget response.
     *
     * @param widgetId
     *         the widget id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @DELETE
    @Path( "/widget/{widgetId}" )
    Response deleteWidget( @PathParam( "widgetId" ) String widgetId );

}
