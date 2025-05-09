package de.soco.software.simuspace.server.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.UUID;

import de.soco.software.simuspace.server.manager.TrainingAlgoManager;
import de.soco.software.simuspace.server.service.rest.TrainingAlgoService;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.TrainingAlgoDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;

/**
 * The Class TrainingAlgoServiceImpl.
 *
 * @author noman arshad
 */
public class TrainingAlgoServiceImpl extends SuSBaseService implements TrainingAlgoService {

    /**
     * The training algo manager.
     */
    private TrainingAlgoManager trainingAlgoManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTrainerAlgoNames( String userUid ) {
        try {
            return ResponseUtils.success( trainingAlgoManager.getTrainerAlgoNames( userUid ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTrainingAlgoViews() {
        try {
            return ResponseUtils.success(
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.TRAINING_TABLE_KEY, getUserIdFromGeneralHeader(),
                            null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTrainerAlgoScriptPath( String algoName ) {
        try {
            return ResponseUtils.success( trainingAlgoManager.getTrainerAlgoScriptPath( algoName ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTrainingAlgoUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.TRAINING_ALGO.getKey() ) ),
                    new TableUI( trainingAlgoManager.getTrainingAlgoUI( getUserIdFromGeneralHeader() ),
                            objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.TRAINING_TABLE_KEY,
                                    getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTrainingAlgoData( String json ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.TRAINING_ALGO.getKey() ) ),
                    trainingAlgoManager.getTrainingAlgoData( getUserIdFromGeneralHeader(),
                            JsonUtils.jsonToObject( json, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createTrainingAlgoUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.CREATE.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                            Messages.TRAINING_ALGO.getKey() ) ), trainingAlgoManager.createTrainingAlgoUI( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createTrainingAlgo( String json ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.TRAINING_ALGO_CREATED.getKey() ),
                    trainingAlgoManager.createTrainingAlgo( getUserIdFromGeneralHeader(),
                            JsonUtils.jsonToObject( json, TrainingAlgoDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTrainingAlgoContext( String payload ) {

        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( payload, FiltersDTO.class );
            return ResponseUtils.success( trainingAlgoManager.getTrainingAlgoContext( getUserIdFromGeneralHeader(), filter ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editTrainingAlgoUI( String algoId ) {
        try {
            return ResponseUtils.success( trainingAlgoManager.editTrainingAlgoUI( getUserIdFromGeneralHeader(), algoId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateTrainingAlgo( String algoId, String payload ) {
        try {
            return ResponseUtils.success( trainingAlgoManager.updateTrainingAlgo( getUserIdFromGeneralHeader(), algoId,
                    JsonUtils.jsonToObject( payload, TrainingAlgoDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteTrainingAlgo( String workflowschemeId, String mode ) {
        try {
            trainingAlgoManager.deleteTrainingAlgo( getUserIdFromGeneralHeader(), workflowschemeId, mode );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.OBJECT_AND_DEPENDENCIES_DELETED_SUCCESSFULLY.getKey() ),
                    true );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveTrainingAlgoView( String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.TRAINING_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteTrainingAlgoView( String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updatetrainingAlgoView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.TRAINING_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the training algo manager.
     *
     * @return the training algo manager
     */
    public TrainingAlgoManager getTrainingAlgoManager() {
        return trainingAlgoManager;
    }

    /**
     * Sets the training algo manager.
     *
     * @param trainingAlgoManager
     *         the new training algo manager
     */
    public void setTrainingAlgoManager( TrainingAlgoManager trainingAlgoManager ) {
        this.trainingAlgoManager = trainingAlgoManager;
    }

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

}
