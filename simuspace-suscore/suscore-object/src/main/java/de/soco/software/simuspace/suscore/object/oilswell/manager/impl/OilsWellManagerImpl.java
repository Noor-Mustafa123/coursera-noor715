package de.soco.software.simuspace.suscore.object.oilswell.manager.impl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.executor.service.ThreadPoolExecutorService;
import de.soco.software.simuspace.suscore.object.oilswell.manager.OilsWellManager;
import de.soco.software.simuspace.suscore.object.oilswell.model.OilWellDTO;
import de.soco.software.simuspace.suscore.object.oilswell.model.OilWellDataDTO;
import de.soco.software.simuspace.suscore.object.oilswell.model.OilWellDateRange;
import de.soco.software.simuspace.suscore.object.oilswell.model.PlungerPredictionDTO;
import de.soco.software.simuspace.suscore.object.oilswell.model.PredictionPayloadDTO;

/**
 * The Class OilsWellManagerImpl.
 */
@Log4j2
public class OilsWellManagerImpl implements OilsWellManager {

    /**
     * The Constant AR_WHITE_WILLIAMS_9_7_17_PAD1_WELLS_WELL_1.
     */
    private static final String AR_WHITE_WILLIAMS_9_7_17_PAD1_WELLS_WELL_1 = "AR/WHITE/WILLIAMS 9-7-17 PAD1/WELLS/WELL_1";

    /**
     * The Constant OILS_WELL_DATE_PATTERN.
     */
    public static final String OILS_WELL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * The Constant CYCLE_LOGS_WELL_PATTERN.
     */
    public static final String CYCLE_LOGS_WELL_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * The Constant CYCLE_LOGS_WELL_CLOSED_PATTERN.
     */
    public static final String CYCLE_LOGS_WELL_CLOSED_PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * The Constant CASING_INSTANT.
     */
    public static final String CASING_INSTANT = "casing_inst";

    /**
     * The Constant GAS_RATE_INSTANT.
     */
    public static final String GAS_RATE_INSTANT = "gasrate_inst";

    /**
     * The Constant TUBING_INSTANT.
     */
    public static final String TUBING_INSTANT = "tubing_inst";

    /**
     * The Constant DATA.
     */
    public static final String DATA = "data";

    public static final String STAT = "stats";

    public static final String SHAP = "shap";

    /**
     * The thread pool executor service.
     */
    private ThreadPoolExecutorService threadPoolExecutorService;

    /**
     * The oils well thread map.
     */
    private Map< UserThread, Future< ? > > oilsWellThreadMap = new ConcurrentHashMap<>();

    private static final String WELL_LIST = "wells_list";

    private static final String WELL_ID = "well_id";

    private static final String PRECISION_1 = "precision1";

    private static final String RECALL_0 = "recall0";

    private static final String RECALL_1 = "recall1";

    private static final String RECALL_2 = "recall2";

    private static final String CONFUSION = "cnfm_plot";

    private static final String FEATURE_PLOT = "featureplot";

    private static final String DOUBLE_PLOT = "double";

    private static final String SINGLE = "single";

    /**
     * Inits the.
     */
    public void init() {
    }

    /**
     * Destroy.
     */
    public void destroy() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlungerPredictionDTO getplungerOptimizationPrediction( String plungerPredictionJson ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest(
                PropertiesManager.getPlungerUrl() + "api/plunger_optimization/predict?user=usertest&key=Dk%5ESm2Mvn*hd",
                plungerPredictionJson, requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            PlungerPredictionDTO plungerPredictionDTO = new PlungerPredictionDTO();
            plungerPredictionDTO.setCur_cycle( JsonUtils.jsonToObject( data.get( "cur_cycle" ).toString(), Object.class ) );
            plungerPredictionDTO.setPrev_cycle( JsonUtils.jsonToObject( data.get( "prev_cycle" ).toString(), Object.class ) );
            plungerPredictionDTO.setData( JsonUtils.jsonToObject( data.get( "plotfigure" ).get( DATA ).toString(), Object.class ) );
            plungerPredictionDTO.setLayout( JsonUtils.jsonToObject( data.get( "plotfigure" ).get( "layout" ).toString(), Object.class ) );
            plungerPredictionDTO.setTrigger( Boolean.parseBoolean( data.get( "Trigger" ).toString() ) );
            return plungerPredictionDTO;
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    /**
     * Sets the prediction payload.
     *
     * @return the prediction payload DTO
     */
    private PredictionPayloadDTO setPredictionPayload() {
        // TODO save last row values in predictionPayloadValues
        /*Outward Payload to prepare : {
        "Wellid": ,
        "TbgPres": 35.9074897766113,
        "CsgPres": 35.9074897766113,
        "GasRate": 121.660842895508,
        "DateTime": "<Current>",
        "CurrentState": "AfterFlow"*/

        long HRS_IN_MS = 1000 * 60 * 60;
        OilWellDataDTO payloadRaw = new OilWellDataDTO();
        payloadRaw.setWellid( AR_WHITE_WILLIAMS_9_7_17_PAD1_WELLS_WELL_1 );
        Date currentTime = new Date();
        long to = currentTime.getTime();
        long fr = to - ( 12 * HRS_IN_MS );
        OilWellDateRange r = new OilWellDateRange();
        r.setFr( new Date( fr ) );
        r.setTo( new Date( to ) );
        payloadRaw.setTime_range( r );
        String jsonRaw = JsonUtils.toJson( payloadRaw );

        log.info( "Payload for Raw: " + jsonRaw );

        OilWellDTO raw = getOilsDataPost( "raw", jsonRaw );
        JsonNode dataRaw = JsonUtils.toJsonNode( JsonUtils.toJson( raw.getData() ) );

        PredictionPayloadDTO payload = new PredictionPayloadDTO();
        payload.setWellid( AR_WHITE_WILLIAMS_9_7_17_PAD1_WELLS_WELL_1 );

        Format format = new SimpleDateFormat( "YYYY-MM-DD'T'HH:MM:ss.SSSS" );
        payload.setDatetime( format.format( currentTime ) );
        payload.setCurrentstate( "AfterFlow" );

        JsonNode csgNode = dataRaw.get( 1 ).get( "y" );
        payload.setCsgpres( csgNode.get( csgNode.size() - 1 ).asText() );

        JsonNode gasNode = dataRaw.get( 0 ).get( "y" );
        payload.setGasrate( gasNode.get( gasNode.size() - 1 ).asText() );

        JsonNode tbgNode = dataRaw.get( 2 ).get( "y" );
        payload.setTbgpres( tbgNode.get( tbgNode.size() - 1 ).asText() );

        JsonNode timeNode = dataRaw.get( 2 ).get( "x" );
        payload.setDatetime( timeNode.get( timeNode.size() - 1 ).asText() );

        return payload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlungerPredictionDTO getplungerOptimizationPrediction( String indentifier, String plungerPredictionJson ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        PredictionPayloadDTO payload = setPredictionPayload();

        String pJson = JsonUtils.toJson( payload );

        log.info( "Payload for Prediction: " + pJson );

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest(
                PropertiesManager.getPlungerUrl() + "api/plunger_optimization/predict?user=usertest&key=Dk%5ESm2Mvn*hd", pJson,
                requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            log.info( responseString );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode plotfigure = data.get( "plotfigure" );
            JsonNode plotfigureData = null;
            JsonNode plotfigureLayout = null;
            if ( plotfigure.has( DATA ) ) {
                plotfigureData = plotfigure.get( DATA );
            }
            if ( plotfigure.has( "layout" ) ) {
                plotfigureLayout = plotfigure.get( "layout" );
            }
            PlungerPredictionDTO plungerPredictionDTO = new PlungerPredictionDTO();
            plungerPredictionDTO.setTrigger( Boolean.parseBoolean( data.get( "trigger" ).toString() ) );
            switch ( indentifier ) {
                case "info":
                    plungerPredictionDTO.setCur_cycle( JsonUtils.jsonToObject( data.get( "cur_cycle" ).toString(), Object.class ) );
                    plungerPredictionDTO.setPrev_cycle( JsonUtils.jsonToObject( data.get( "prev_cycle" ).toString(), Object.class ) );
                    break;

                case "plot":
                    if ( plotfigureData != null ) {
                        plungerPredictionDTO.setData( JsonUtils.jsonToObject( plotfigureData.toString(), Object.class ) );
                    }
                    if ( plungerPredictionDTO != null ) {
                        plungerPredictionDTO.setLayout( JsonUtils.jsonToObject( plotfigureLayout.toString(), Object.class ) );
                    }
                    break;
                default:
                    break;
            }

            return plungerPredictionDTO;
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public List< String > getPlungerOptimizationWellList() {
        List< String > wellListReturn = null;
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        CloseableHttpResponse closeableHttpResponse = SuSClient.getExternalRequest(
                PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_list?user=usertest&key=Dk%5ESm2Mvn*hd", requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            log.info( responseString );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode wellList = data.get( WELL_LIST );
            if ( wellList.isArray() ) {
                wellListReturn = new ArrayList<>();
                for ( JsonNode wellId : wellList ) {
                    wellListReturn.add( wellId.get( WELL_ID ).asText() );
                }
            }
        } catch ( Exception e ) {
            throw new SusException( e );
        }
        return wellListReturn;
    }

    /**
     * Gets the thread pool executor service.
     *
     * @return the thread pool executor service
     */
    public ThreadPoolExecutorService getThreadPoolExecutorService() {
        return threadPoolExecutorService;
    }

    /**
     * Sets the thread pool executor service.
     *
     * @param threadPoolExecutorService
     *         the new thread pool executor service
     */
    public void setThreadPoolExecutorService( ThreadPoolExecutorService threadPoolExecutorService ) {
        this.threadPoolExecutorService = threadPoolExecutorService;
    }

    /**
     * Gets the oils well thread map.
     *
     * @return the oils well thread map
     */
    public Map< UserThread, Future< ? > > getOilsWellThreadMap() {
        return oilsWellThreadMap;
    }

    /**
     * Sets the oils well thread map.
     *
     * @param oilsWellThreadMap
     *         the oils well thread map
     */
    public void setOilsWellThreadMap( Map< UserThread, Future< ? > > oilsWellThreadMap ) {
        this.oilsWellThreadMap = oilsWellThreadMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OilWellDTO getOilsDataPost( String identifier, String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );

        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_" + identifier
                + "_features_plot?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, json, requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode dataNode = jsonNode.get( DATA );
            JsonNode plotNode = dataNode.get( identifier + "_features_plot" );
            return JsonUtils.jsonToObject( plotNode.toString(), OilWellDTO.class );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OilWellDTO getOilsDistributionData( String identifier, String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );

        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_statistics?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, json, requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode dataNode = jsonNode.get( DATA );
            JsonNode wellStatisticsNode = dataNode.get( "well_statistics" );
            JsonNode retNode = null;
            if ( identifier.equalsIgnoreCase( "velocity" ) ) {
                retNode = wellStatisticsNode.get( "velocity_distribution" );
            } else if ( identifier.equalsIgnoreCase( "ton" ) ) {
                retNode = wellStatisticsNode.get( "timeon_distribution" );
            } else if ( identifier.equalsIgnoreCase( "toff" ) ) {
                retNode = wellStatisticsNode.get( "timeoff_distribution" );
            } else if ( identifier.equalsIgnoreCase( "ventminuts" ) ) {
                retNode = wellStatisticsNode.get( "vent_minuts_distribution" );
            } else if ( identifier.equalsIgnoreCase( "arrivaltype" ) ) {
                retNode = wellStatisticsNode.get( "arrival_type_histogram" );
            } else if ( identifier.equalsIgnoreCase( "vent_distribution" ) ) {
                retNode = wellStatisticsNode.get( "vent_distribution" );
            } else if ( identifier.equalsIgnoreCase( "velocity_graph" ) ) {
                retNode = wellStatisticsNode.get( "velocity_graph" );
            } else if ( identifier.equalsIgnoreCase( "volume_graph" ) ) {
                retNode = wellStatisticsNode.get( "volume_graph" );
            } else if ( identifier.equalsIgnoreCase( "volume_distribution" ) ) {
                retNode = wellStatisticsNode.get( "volume_distribution" );
            }
            return JsonUtils.jsonToObject( retNode.toString(), OilWellDTO.class );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOilsTrainData( String identifier, String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        if ( identifier.equals( "opentrigger" ) ) {
            ( ( ObjectNode ) payload ).remove( "autotuneparams" );
            ( ( ObjectNode ) payload ).put( "trigger", "open" );
        } else if ( identifier.equals( "closetrigger" ) ) {
            ( ( ObjectNode ) payload ).remove( "batchsize" );
            ( ( ObjectNode ) payload ).remove( "epochs" );
            ( ( ObjectNode ) payload ).remove( "learningrate" );
            ( ( ObjectNode ) payload ).remove( "numberofcycles" );
            ( ( ObjectNode ) payload ).put( "trigger", "close" );
        }
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_start?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode dataNode = jsonNode.get( DATA );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( dataNode.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOilsTrainStats( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode dataNode = jsonNode.get( DATA );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( dataNode.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public Object getOilsTrainStatsRecall( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode stat = data.get( STAT );
            Map< String, Float > map = new HashMap<>();
            map.put( PRECISION_1, Float.parseFloat( stat.get( PRECISION_1 ).asText() ) );
            map.put( RECALL_0, Float.parseFloat( stat.get( RECALL_0 ).asText() ) );
            map.put( RECALL_1, Float.parseFloat( stat.get( RECALL_1 ).asText() ) );
            map.put( RECALL_2, Float.parseFloat( stat.get( RECALL_2 ).asText() ) );
            return map;
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public Object getOilsTrainStatsPlotShape( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode stat = data.get( STAT );
            JsonNode shap = stat.get( SHAP );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( shap.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public Object getOilsTrainStatsPlotModelState( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode stat = data.get( STAT );
            JsonNode model_state = stat.get( "model_state" );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( model_state.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public Object getOilsTrainStatsPlotDataState( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode stat = data.get( STAT );
            JsonNode data_state = stat.get( "data_state" );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( data_state.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public Object getOilsTrainStatsPlotConfusion( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode stat = data.get( STAT );
            JsonNode confusion = stat.get( CONFUSION );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( confusion.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public Object getOilsTrainStatsPlotDouble( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode stat = data.get( STAT );
            JsonNode featurePlot = stat.get( FEATURE_PLOT );
            JsonNode doublePlot = featurePlot.get( DOUBLE_PLOT );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( doublePlot.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public Object getOilsTrainStatsPlotSingleLiqdLd( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode stat = data.get( STAT );
            JsonNode featurePlot = stat.get( FEATURE_PLOT );
            JsonNode singlePlot = featurePlot.get( SINGLE );
            JsonNode LiqdLd = singlePlot.get( "LiqdLd" );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( LiqdLd.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public Object getOilsTrainStatsPlotSingleUpLift( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode stat = data.get( STAT );
            JsonNode featurePlot = stat.get( FEATURE_PLOT );
            JsonNode singlePlot = featurePlot.get( SINGLE );
            JsonNode UpLift = singlePlot.get( "UpLift" );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( UpLift.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public Object getOilsTrainStatsPlotSingleTbgDiff( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode stat = data.get( STAT );
            JsonNode featurePlot = stat.get( FEATURE_PLOT );
            JsonNode singlePlot = featurePlot.get( SINGLE );
            JsonNode TbgDiff = singlePlot.get( "TbgDiff" );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( TbgDiff.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

    @Override
    public Object getOilsTrainStatsPlotSingleTbgLinDiff( String json ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        JsonNode payload = JsonUtils.toJsonNode( json );
        String url = PropertiesManager.getPlungerUrl() + "api/plunger_optimization/well_training_stats?user=usertest&key=Dk%5ESm2Mvn*hd";
        CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( url, payload.toString(), requestHeaders );
        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            JsonNode data = jsonNode.get( DATA );
            JsonNode stat = data.get( STAT );
            JsonNode featurePlot = stat.get( FEATURE_PLOT );
            JsonNode singlePlot = featurePlot.get( SINGLE );
            JsonNode TbgLinDiff = singlePlot.get( "TbgLinDiff" );
            Map< String, Object > map = new HashMap<>();
            return JsonUtils.jsonToMap( TbgLinDiff.toString(), map );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
    }

}
