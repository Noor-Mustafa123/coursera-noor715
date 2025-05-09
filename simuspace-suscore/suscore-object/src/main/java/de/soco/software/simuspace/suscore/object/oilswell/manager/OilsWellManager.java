package de.soco.software.simuspace.suscore.object.oilswell.manager;

import java.util.List;

import de.soco.software.simuspace.suscore.object.oilswell.model.OilWellDTO;
import de.soco.software.simuspace.suscore.object.oilswell.model.PlungerPredictionDTO;

/**
 * The Interface OilsWellManager.
 */
public interface OilsWellManager {

    /**
     * Gets the plunger optimization prediction.
     *
     * @param plungerPredictionJson
     *         the plunger prediction json
     *
     * @return the plunger optimization prediction
     */
    public PlungerPredictionDTO getplungerOptimizationPrediction( String plungerPredictionJson );

    /**
     * Gets the plunger optimization prediction.
     *
     * @param indentifier
     *         the indentifier
     * @param plungerPredictionJson
     *         the plunger prediction json
     *
     * @return the plunger optimization prediction
     */
    public PlungerPredictionDTO getplungerOptimizationPrediction( String indentifier, String plungerPredictionJson );

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
    public OilWellDTO getOilsDataPost( String identifier, String json );

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
    public OilWellDTO getOilsDistributionData( String identifier, String json );

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
    public Object getOilsTrainData( String identifier, String json );

    /**
     * Gets the oils train stats.
     *
     * @param json
     *         the json
     *
     * @return the oils train stats
     */
    public Object getOilsTrainStats( String json );

    public Object getOilsTrainStatsRecall( String json );

    public List< String > getPlungerOptimizationWellList();

    public Object getOilsTrainStatsPlotShape( String json );

    public Object getOilsTrainStatsPlotConfusion( String json );

    public Object getOilsTrainStatsPlotDouble( String json );

    public Object getOilsTrainStatsPlotSingleLiqdLd( String json );

    public Object getOilsTrainStatsPlotSingleUpLift( String json );

    public Object getOilsTrainStatsPlotSingleTbgDiff( String json );

    public Object getOilsTrainStatsPlotSingleTbgLinDiff( String json );

    public Object getOilsTrainStatsPlotModelState( String json );

    public Object getOilsTrainStatsPlotDataState( String json );

}
