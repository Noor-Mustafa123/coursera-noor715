package de.soco.software.simuspace.server.manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.model.ScanResponseDTO;
import de.soco.software.simuspace.suscore.common.properties.ScanObjectDTO;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DesignVariableLabelDTO;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.suscore.data.model.WFSchemeDTO;
import de.soco.software.simuspace.workflow.model.JobParameters;

/**
 * The Interface WFSchemeManager.
 */
public interface WFSchemeManager {

    /**
     * Gets the WF scheme UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the WF scheme UI
     */
    List< TableColumn > getWFSchemeUI( String userIdFromGeneralHeader );

    /**
     * Gets the WF scheme data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the WF scheme data
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getWFSchemeData( String userIdFromGeneralHeader, FiltersDTO filter );

    /**
     * Creates the WF scheme UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm createWFSchemeUI( String userIdFromGeneralHeader );

    /**
     * Creates the WF scheme.
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
    WFSchemeDTO createWFScheme( String userIdFromGeneralHeader, WFSchemeDTO loadcaseWizardDTO );

    /**
     * Validates the WF run scheme.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow Id
     *
     * @apiNote To be used in service calls only
     */
    void validateWFRunSchemeByWorkflowId( String userId, String workflowId );

    /**
     * Gets the WF scheme context menu.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the WF scheme context menu
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getWFSchemeContextMenu( String userIdFromGeneralHeader, FiltersDTO filter );

    /**
     * Edits the WF scheme UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param schemeId
     *         the scheme id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editWFSchemeUI( String userIdFromGeneralHeader, String schemeId );

    /**
     * Update WF scheme.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadcaseId
     *         the loadcase id
     * @param schemeDTO
     *         the scheme DTO
     *
     * @return the WF scheme DTO
     *
     * @apiNote To be used in service calls only
     */
    WFSchemeDTO updateWFScheme( String userIdFromGeneralHeader, String loadcaseId, WFSchemeDTO schemeDTO );

    /**
     * Delete WF scheme.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param mode
     *         the mode
     *
     * @apiNote To be used in service calls only
     */
    void deleteWFScheme( String userIdFromGeneralHeader, String selectionId, String mode );

    /**
     * Gets the tabs view container UI.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the tabs view container UI
     *
     * @apiNote To be used in service calls only
     */
    SubTabsItem listSchemeTabsUI( String userId, String workflowId );

    /**
     * Gets the Category option form.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the object option form
     *
     * @apiNote To be used in service calls only
     */
    UIForm getCategoryOptionForm( String userId, String workflowId );

    /**
     * Run scheme category form.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param categoryId
     *         the category id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm getSchemeOptionForm( String userId, String workflowId, int categoryId );

    /**
     * Gets the scheme option form UI.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param schemeId
     *         the scheme id
     *
     * @return the scheme option form UI
     *
     * @apiNote To be used in service calls only
     */
    UIForm getSchemeOptionFormUI( String userId, String workflowId, String schemeId );

    /**
     * Scan objective file.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param scanObject
     *         the scan object
     *
     * @return the list
     */
    List< ScanResponseDTO > scanObjectiveFile( String userIdFromGeneralHeader, List< ScanObjectDTO > scanObject );

    /**
     * Scan objective file from path.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param scanObject
     *         the scan object
     *
     * @return the list
     */
    List< ScanResponseDTO > scanObjectiveFileFromPath( String userIdFromGeneralHeader, ScanFileDTO scanObject );

    /**
     * Gets the designvariables UI.
     *
     * @param wfId
     *         the wf id
     * @param userId
     *         the user id
     *
     * @return the designvariables UI
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getDesignvariablesUI( String wfId, String userId );

    /**
     * Gets the design summary UI.
     *
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     *
     * @return the design summary UI
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getDesignSummaryUI( String workflowId, String userId );

    /**
     * Gets the design variable data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     * @param jsonToObject
     *         the json to object
     *
     * @return the design variable data
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getDesignVariableData( String userIdFromGeneralHeader, String workflowId, FiltersDTO jsonToObject );

    /**
     * Gets the design variable data as label.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable data as label
     *
     * @apiNote To be used in service calls only
     */
    DesignVariableLabelDTO getDesignVariableDataAsLabel( String userIdFromGeneralHeader, UUID workflowId );

    /**
     * Update design variables relation with expression.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     * @param expression
     *         the expression
     *
     * @return the design variable label DTO
     *
     * @apiNote To be used in service calls only
     */
    DesignVariableLabelDTO updateDesignVariablesRelationWithExpression( String userIdFromGeneralHeader, UUID workflowId,
            String expression );

    /**
     * Gets the design summary data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     * @param jsonToObject
     *         the json to object
     * @param refreshOriginal
     *         the refresh original
     *
     * @return the design summary data
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Map< String, Object > > getDesignSummaryData( String userIdFromGeneralHeader, String workflowId,
            FiltersDTO jsonToObject, boolean refreshOriginal );

    /**
     * Gets the context menu.
     *
     * @param filter
     *         the filter
     *
     * @return List<ContextMenuItem> design variable context menu
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getDesignVariableContextMenu( FiltersDTO filter );

    /**
     * Gets the objective variable context menu.
     *
     * @param filter
     *         the filter
     *
     * @return List<ContextMenuItem> objective variable context menu
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getObjectiveVariableContextMenu( FiltersDTO filter );

    /**
     * Edits the design variable UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param designVariableId
     *         the design variable id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editDesignVariableUI( String userIdFromGeneralHeader, String designVariableId );

    /**
     * Edits the objective variable UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectiveVariableId
     *         the objective variable id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editObjectiveVariableUI( String userIdFromGeneralHeader, String objectiveVariableId );

    /**
     * Update design variable.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param designVarId
     *         the design var id
     * @param json
     *         the json
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object updateDesignVariable( String userIdFromGeneralHeader, String designVarId, String json );

    /**
     * Update objective variable.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectiveVariableDTO
     *         the objective variable DTO
     *
     * @return the objective variable DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectiveVariableDTO updateObjectiveVariable( String userIdFromGeneralHeader, ObjectiveVariableDTO objectiveVariableDTO );

    /**
     * Gets the ObjectiveVariables UI.
     *
     * @return the ObjectiveVariables UI
     */
    List< TableColumn > getObjectiveVariablesUI();

    /**
     * Gets the objective variable data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param wortkflowId
     *         the wortkflow id
     * @param jsonToObject
     *         the json to object
     *
     * @return the objective variable data
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ObjectiveVariableDTO > getObjectiveVariableData( String userIdFromGeneralHeader, String wortkflowId,
            FiltersDTO jsonToObject );

    /**
     * Save or update scheme option form.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param fromString
     *         the from string
     * @param json
     *         the json
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object saveOrUpdateSchemeOptionForm( String userIdFromGeneralHeader, UUID fromString, String json );

    /**
     * Run wf scheem.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param uid
     *         the uid
     * @param tokenFromGeneralHeader
     *         the token from general header
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    JobParameters runWfScheme( String userIdFromGeneralHeader, String uid, String tokenFromGeneralHeader, String workflowId,
            String objectFilterJson );

    /**
     * Check user scheme license.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     *
     * @apiNote To be used in service calls only
     */
    void validateUserSchemeLicense( String userIdFromGeneralHeader, String workflowId );

    /**
     * Gets the workflow manager.
     *
     * @return the workflow manager
     */
    WorkflowManager getWorkflowManager();

    /**
     * Adds the design summary UI.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm addDesignSummaryUI( String userId, String workflowId );

    /**
     * Adds the design summary.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param map
     *         the map
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    List< Map< String, Object > > addDesignSummary( String userId, String workflowId, Map< String, Object > map );

    /**
     * Edits the design summary UI.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param id
     *         the id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editDesignSummaryUI( String userId, String workflowId, String id );

    /**
     * Update design summary.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param id
     *         the id
     * @param map
     *         the map
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    List< Map< String, Object > > updateDesignSummary( String userId, String workflowId, String id, Map< String, Object > map );

    /**
     * Delete design summary.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param filter
     *         the filter
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteDesignSummary( String userId, String workflowId, FiltersDTO filter );

    /**
     * Gets the design variable all data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable all data
     *
     * @apiNote To be used in service calls only
     */
    List< Object > getDesignVariableAllData( String userIdFromGeneralHeader, String workflowId );

    /**
     * Check syntax.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     * @param aObj
     *         the a obj
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean checkSyntax( String userIdFromGeneralHeader, String workflowId, String aObj );

    /**
     * Download design summary data file CSV.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the string
     *
     * @apiNote To be used in service calls only
     */
    String downloadDesignSummaryDataFileCSV( String userId, String workflowId );

    /**
     * Gets the design summary import UI.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the design summary import UI
     */
    Object getDesignSummaryImportUI( String workflowId );

    /**
     * Update design summary with imported CSV.
     *
     * @param workflowId
     *         the workflow id
     * @param docId
     *         the doc id
     * @param userId
     *         the user id
     *
     * @return the object
     *
     * @throws FileNotFoundException
     *         the file not found exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @apiNote To be used in service calls only
     */
    Object updateDesignSummaryWithImportedCSV( String workflowId, String docId, String userId ) throws IOException;

    /**
     * Gets the generate design summary data.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param refreshOriginal
     *         the refresh original
     *
     * @return the generate design summary data
     *
     * @apiNote To be used in service calls only
     */
    Object getGenerateDesignSummaryData( String userId, String workflowId, boolean refreshOriginal );

    /**
     * Gets the post process form.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     *
     * @return the post process form
     */
    UIForm getPostProcessForm( String userIdFromGeneralHeader, String workflowId );

    /**
     * Gets the workflow from post process form.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow from post process form
     *
     * @apiNote To be used in service calls only
     */
    Object getWorkflowFromPostProcessForm( String userIdFromGeneralHeader, String workflowId );

    /**
     * Save ask on run variables.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param json
     *         the json
     *
     * @apiNote To be used in service calls only
     */
    void saveAskOnRunVariables( String userId, String workflowId, String json );

}