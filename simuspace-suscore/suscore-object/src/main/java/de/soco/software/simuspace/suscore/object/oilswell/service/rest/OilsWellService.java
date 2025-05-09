package de.soco.software.simuspace.suscore.object.oilswell.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;

/**
 * The Interface OilsWellService.
 */
@WebService
@Path( "/" )
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface OilsWellService {

    /**
     * Gets the plunger optimization prediction.
     *
     * @param predictionJson
     *         the prediction json
     *
     * @return the plunger optimization prediction
     */
    @POST
    @Path( "/plunger_prediction" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getPlungerOptimizationPrediction( String predictionJson );

    /**
     * Gets the plunger optimization prediction.
     *
     * @param indentifier
     *         the indentifier
     * @param predictionJson
     *         the prediction json
     *
     * @return the plunger optimization prediction
     */
    @POST
    @Path( "/plunger_prediction/{indentifier}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getPlungerOptimizationPrediction( @PathParam( "indentifier" ) String indentifier, String predictionJson );

    @GET
    @Path( "/plunger_optimization/well_list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getPlungerOptimizationWellList();

    /**
     * Gets the oils data post.
     *
     * @param identifier
     *         the identifier
     * @param json
     *         the json
     *
     * @return the oils data post
     */
    @POST
    @Path( "/feature/{identifier}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsDataPost( @PathParam( "identifier" ) String identifier, String json );

    /**
     * Gets the oils distribution data.
     *
     * @param identifier
     *         the identifier
     * @param json
     *         the json
     *
     * @return the oils distribution data
     */
    @POST
    @Path( "/distribution/{identifier}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsDistributionData( @PathParam( "identifier" ) String identifier, String json );

    /**
     * Gets the oils train data.
     *
     * @param identifier
     *         the identifier
     * @param json
     *         the json
     *
     * @return the oils train data
     */
    @POST
    @Path( "/train/{identifier}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainData( @PathParam( "identifier" ) String identifier, String json );

    /**
     * Gets the oils train stats.
     *
     * @param json
     *         the json
     *
     * @return the oils train stats
     */
    @POST
    @Path( "/train/stats" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStats( String json );

    @POST
    @Path( "/train/stats/recall" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStatsRecall( String json );

    @POST
    @Path( "/train/stats/plot/shape" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStatsPlotShape( String json );

    @POST
    @Path( "/train/stats/plot/model_state" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStatsPlotModelState( String json );

    @POST
    @Path( "/train/stats/plot/data_state" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStatsPlotDataState( String json );

    @POST
    @Path( "/train/stats/plot/confusion" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStatsPlotConfusion( String json );

    @POST
    @Path( "/train/stats/plot/double" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStatsPlotDouble( String json );

    @POST
    @Path( "/train/stats/plot/single/LiqdLd" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStatsPlotSingleLiqdLd( String json );

    @POST
    @Path( "/train/stats/plot/single/UpLift" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStatsPlotSingleUpLift( String json );

    @POST
    @Path( "/train/stats/plot/single/TbgDiff" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStatsPlotSingleTbgDiff( String json );

    @POST
    @Path( "/train/stats/plot/single/TbgLinDiff" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getOilsTrainStatsPlotSingleTbgLinDiff( String json );

}
