package de.soco.software.simuspace.suscore.object.oilswell.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.object.oilswell.manager.OilsWellManager;
import de.soco.software.simuspace.suscore.object.oilswell.service.rest.OilsWellService;

/**
 * The Class OilsWellServiceImpl.
 */
public class OilsWellServiceImpl extends SuSBaseService implements OilsWellService {

    /**
     * The oils well manager.
     */
    private OilsWellManager oilsWellManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getPlungerOptimizationPrediction( String predictionJson ) {
        try {
            return ResponseUtils.success( oilsWellManager.getplungerOptimizationPrediction( predictionJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getPlungerOptimizationPrediction( String indentifier, String predictionJson ) {
        try {
            List< String > NOT_REQUIRED_PROP = new ArrayList<>();
            switch ( indentifier ) {
                case "info":
                    NOT_REQUIRED_PROP.add( "plotfigure" );
                    break;
                case "plot":
                    NOT_REQUIRED_PROP.add( "cur_cycle" );
                    NOT_REQUIRED_PROP.add( "prev_cycle" );
                    NOT_REQUIRED_PROP.add( "Trigger" );
                    break;
            }
            return ResponseUtils.success( oilsWellManager.getplungerOptimizationPrediction( indentifier, predictionJson ),
                    NOT_REQUIRED_PROP.stream().toArray( String[]::new ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getPlungerOptimizationWellList() {
        try {
            return ResponseUtils.success( oilsWellManager.getPlungerOptimizationWellList() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the oils well manager.
     *
     * @return the oils well manager
     */
    public OilsWellManager getOilsWellManager() {
        return oilsWellManager;
    }

    /**
     * Sets the oils well manager.
     *
     * @param oilsWellManager
     *         the new oils well manager
     */
    public void setOilsWellManager( OilsWellManager oilsWellManager ) {
        this.oilsWellManager = oilsWellManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getOilsDataPost( String identifier, String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsDataPost( identifier, json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getOilsDistributionData( String identifier, String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsDistributionData( identifier, json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getOilsTrainData( String identifier, String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainData( identifier, json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getOilsTrainStats( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStats( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getOilsTrainStatsRecall( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStatsRecall( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getOilsTrainStatsPlotShape( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStatsPlotShape( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getOilsTrainStatsPlotModelState( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStatsPlotModelState( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getOilsTrainStatsPlotDataState( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStatsPlotDataState( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getOilsTrainStatsPlotConfusion( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStatsPlotConfusion( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getOilsTrainStatsPlotDouble( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStatsPlotDouble( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getOilsTrainStatsPlotSingleLiqdLd( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStatsPlotSingleLiqdLd( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getOilsTrainStatsPlotSingleUpLift( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStatsPlotSingleUpLift( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getOilsTrainStatsPlotSingleTbgDiff( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStatsPlotSingleTbgDiff( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getOilsTrainStatsPlotSingleTbgLinDiff( String json ) {
        try {
            return ResponseUtils.success( oilsWellManager.getOilsTrainStatsPlotSingleTbgLinDiff( json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
