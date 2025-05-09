package de.soco.software.simuspace.server.service.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.constants.ConstantsAlgorithmsServiceEndpoints;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;

/**
 * The Interface TrainingAlgoService.
 *
 * @author noman arshad
 */
public interface TrainingAlgoService {

    /**
     * Gets the trainer algo names.
     *
     * @param userUID
     *         the user UID
     *
     * @return the trainer algo names
     */
    @GET
    @Path( "/user/{userUID}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTrainerAlgoNames( @PathParam( "userUID" ) String userUID );

    /**
     * Gets training algo views.
     *
     * @return the training algo views
     */
    @GET
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTrainingAlgoViews();

    /**
     * Gets the trainer algo script path.
     *
     * @param algoName
     *         the algo name
     *
     * @return the trainer algo names
     */
    @GET
    @Path( "/{algoName}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTrainerAlgoScriptPath( @PathParam( "algoName" ) String algoName );

    /**
     * Gets the trainer UI.
     *
     * @return the trainer UI
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_SCHEME_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTrainingAlgoUI();

    /**
     * Gets the trainer data.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the trainer data
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_SCHEME_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTrainingAlgoData( String objectFilterJson );

    /**
     * Creates the trainer UI.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.CREATE_SCHEME_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createTrainingAlgoUI();

    /**
     * Creates the trainer.
     *
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.CREATE_SCHEME )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createTrainingAlgo( String payload );

    /**
     * Gets the training algo context.
     *
     * @param payload
     *         the payload
     *
     * @return the training algo context
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_SCHEME_CONTEXT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTrainingAlgoContext( String payload );

    /**
     * Edits the training algo UI.
     *
     * @param algoId
     *         the algo id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.EDIT_SCHEME_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editTrainingAlgoUI( @PathParam( ConstantsAlgorithmsServiceEndpoints.SCHEME_ID_PARAM ) String algoId );

    /**
     * Update training algo.
     *
     * @param algoId
     *         the algo id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsAlgorithmsServiceEndpoints.UPDATE_OR_DELETE_SCHEME_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateTrainingAlgo( @PathParam( ConstantsAlgorithmsServiceEndpoints.SCHEME_ID_PARAM ) String algoId, String payload );

    /**
     * Delete training algo.
     *
     * @param algoId
     *         the algo id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsAlgorithmsServiceEndpoints.UPDATE_OR_DELETE_SCHEME_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteTrainingAlgo( @PathParam( ConstantsAlgorithmsServiceEndpoints.SCHEME_ID_PARAM ) String algoId,
            @QueryParam( ConstantsAlgorithmsServiceEndpoints.MODE ) String mode );

    /**
     * Delete trainingAlgo view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteTrainingAlgoView( @PathParam( "viewId" ) String viewId );

    /**
     * Update trainingAlgo view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updatetrainingAlgoView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Save TrainingAlgo view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveTrainingAlgoView( String objectJson );

}
