package de.soco.software.simuspace.server.manager;

import java.util.List;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.WfFieldsUiDTO;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.TrainingAlgoDTO;

/**
 * The Interface TrainingAlgoManager.
 *
 * @author noman arshad
 */
public interface TrainingAlgoManager {

    /**
     * Gets the trainer algo names.
     *
     * @param userUID
     *         the user UID
     *
     * @return the trainer algo names
     *
     * @apiNote To be used in service calls only
     */
    List< WfFieldsUiDTO > getTrainerAlgoNames( String userUID );

    /**
     * Gets the trainer algo script path.
     *
     * @param algoName
     *         the algo name
     *
     * @return the trainer algo names
     *
     * @apiNote To be used in service calls only
     */
    String getTrainerAlgoScriptPath( String algoName );

    /**
     * Gets the training algo UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the trainer algo UI
     */
    List< TableColumn > getTrainingAlgoUI( String userIdFromGeneralHeader );

    /**
     * Gets the training algo data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the training algo data
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getTrainingAlgoData( String userIdFromGeneralHeader, FiltersDTO filter );

    /**
     * Creates the training algo UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the list
     */
    UIForm createTrainingAlgoUI( String userIdFromGeneralHeader );

    /**
     * Creates the training algo.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadcaseWizardDTO
     *         the loadcase wizard DTO
     *
     * @return the WF scheme DTO
     *
     * @apiNote To be used in service calls only
     */
    TrainingAlgoDTO createTrainingAlgo( String userIdFromGeneralHeader, TrainingAlgoDTO loadcaseWizardDTO );

    /**
     * Gets the training algo context.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the training algo context
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getTrainingAlgoContext( String userIdFromGeneralHeader, FiltersDTO filter );

    /**
     * Delete training algo.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowschemeId
     *         the workflowscheme id
     * @param mode
     *         the mode
     *
     * @apiNote To be used in service calls only
     */
    void deleteTrainingAlgo( String userIdFromGeneralHeader, String workflowschemeId, String mode );

    /**
     * Update training algo.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowschemeId
     *         the workflowscheme id
     * @param jsonToObject
     *         the json to object
     *
     * @return the training algo DTO
     *
     * @apiNote To be used in service calls only
     */
    TrainingAlgoDTO updateTrainingAlgo( String userIdFromGeneralHeader, String workflowschemeId, TrainingAlgoDTO jsonToObject );

    /**
     * Edits the training algo UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param algoId
     *         the algo id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editTrainingAlgoUI( String userIdFromGeneralHeader, String algoId );

}
