package de.soco.software.simuspace.wizards.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;

/**
 * The Interface QaDynaService.
 */
public interface QaDynaService {

    /**
     * Gets qa dyna tabs ui.
     *
     * @param projectId
     *         the project id
     *
     * @return the qa dyna tabs ui
     */
    @GET
    @Path( "/project/{projectId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getQaDynaTabsUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Qa dyna base ui response.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/base/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response qaDynaBaseUi( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Save base fields response.
     *
     * @param projectId
     *         the project id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/project/{projectId}/base" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveBaseFields( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId, String payload );

    /**
     * Qa dyna param ui response.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/parameter/selection/{selectionId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response qaDynaParamUi( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String selectionId );

    /**
     * Save params fields response.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/project/{projectId}/parameter/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveParamsFields( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String selectionId, String payload );

    /**
     * Qa dyna options ui response.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param set
     *         the set
     * @param option
     *         the option
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/parameter/selection/{selectionId}/set/{set}/dynaopt/{option}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response qaDynaOptionsUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String selectionId, @PathParam( "set" ) String set,
            @PathParam( ConstantsObjectServiceEndPoints.OPTION ) String option );

    /**
     * Qa dyna options ui response.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param set
     *         the set
     * @param option
     *         the option
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/parameter/selection/{selectionId}/set/{set}/file/{whichfile}/option/{option}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response qaDynaFileSelectionUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String selectionId, @PathParam( "set" ) String set,
            @PathParam( "whichfile" ) String whichFile, @PathParam( ConstantsObjectServiceEndPoints.OPTION ) String option );

    /**
     * Ez field ui response.
     *
     * @param ezTypeName
     *         the ez type name
     *
     * @return the response
     */
    @GET
    @Path( "/eztype/{ezTypeName}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response ezFieldUI( @PathParam( "ezTypeName" ) String ezTypeName );

    /**
     * Qa dyna review ui response.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/review/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response qaDynaReviewUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String selectionId );

    /**
     * Qa dyna review table ui response.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/review/table/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response qaDynaReviewTableUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Gets qa dyna input table.
     *
     * @param projectId
     *         the project id
     * @param payload
     *         the payload
     *
     * @return the qa dyna input table
     */
    @POST
    @Path( "/project/{projectId}/review/table/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getQaDynaInputTable( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId, String payload );

    /**
     * Save input review fields response.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/project/{projectId}/review" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveInputReviewFields( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId, String payload );

    /**
     * Qa dyna ppo ui response.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/ppo/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response qaDynaPPOUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Save ppo fields and submit response.
     *
     * @param projectId
     *         the project id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/project/{projectId}/submit" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response savePPOFieldsAndSubmit( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId, String payload );

}
