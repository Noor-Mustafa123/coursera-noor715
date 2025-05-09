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

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsAlgorithmsServiceEndpoints;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;

/**
 * The Interface WFSchemeService.
 */
public interface WFSchemeService {

    /**
     * Gets the workflowscheme UI.
     *
     * @return the workflowscheme UI
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_SCHEME_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWFSchemeUI();

    /**
     * Gets the workflowscheme data.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the workflowscheme data
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_SCHEME_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWFSchemeData( String objectFilterJson );

    /**
     * Gets the workflowscheme context menu.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the workflowscheme context menu
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_SCHEME_CONTEXT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWFSchemeContextMenu( String objectFilterJson );

    /**
     * Creates the workflowscheme UI.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.CREATE_SCHEME_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createWFSchemeUI();

    /**
     * Creates the workflowscheme.
     *
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.CREATE_SCHEME )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createWFScheme( String payload );

    /**
     * Edits the workflowscheme UI.
     *
     * @param workflowschemeId
     *         the workflowscheme id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.EDIT_SCHEME_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editWFSchemeUI( @PathParam( ConstantsAlgorithmsServiceEndpoints.SCHEME_ID_PARAM ) String workflowschemeId );

    /**
     * Update workflowscheme.
     *
     * @param workflowschemeId
     *         the workflowscheme id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsAlgorithmsServiceEndpoints.UPDATE_OR_DELETE_SCHEME_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateWFScheme( @PathParam( ConstantsAlgorithmsServiceEndpoints.SCHEME_ID_PARAM ) String workflowschemeId, String payload );

    /**
     * Delete workflowscheme.
     *
     * @param workflowschemeId
     *         the workflowscheme id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsAlgorithmsServiceEndpoints.UPDATE_OR_DELETE_SCHEME_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteWFScheme( @PathParam( ConstantsAlgorithmsServiceEndpoints.SCHEME_ID_PARAM ) String workflowschemeId,
            @QueryParam( ConstantsAlgorithmsServiceEndpoints.MODE ) String mode );

    /**
     * Validates the workflowscheme.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.VALIDATE_SCHEME_BY_WORKFLOW_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response validateWFRunSchemeByWorkflowId( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId );

    /**
     * Gets the workflow by id.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow by id
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_SCHEME_TABS_BY_WORKFLOW_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listSchemeTabsUI( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId );

    /**
     * Gets the category option form.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the category option form
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.CATEGORY_OPTIONS_BY_WORKFLOW_ID )
    Response getCategoryOptionForm( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId );

    /**
     * Save scheme option form.
     *
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.CATEGORY_OPTIONS_BY_WORKFLOW_ID )
    Response saveOrUpdateSchemeOptionForm( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            String objectFilterJson );

    /**
     * generate create object form.
     *
     * @param workflowId
     *         the workflow id
     * @param category
     *         the category
     *
     * @return Response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.RUN_SCHEME_CATEGORY_FORM_UI )
    Response getSchemeOptionForm( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            @PathParam( ConstantsAlgorithmsServiceEndpoints.CATEGORY_ID_PARAM ) int category );

    /**
     * Gets the scheme option form UI.
     *
     * @param workflowId
     *         the workflow id
     * @param schemeId
     *         the scheme id
     *
     * @return the scheme option form UI
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.RUN_SCHEME_SCHEME_FORM_UI )
    Response getSchemeOptionFormUI( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            @PathParam( ConstantsAlgorithmsServiceEndpoints.SCHEME_ID_PARAM ) String schemeId );

    /**
     * Scan objective file.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.SCAN_OBJECTIVE_FILE )
    Response scanObjectiveFile( String objectFilterJson );

    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.SCAN_OBJECTIVE_FILE_ON_SERVER_SIDE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response scanObjectiveFileOnServerSide( String json );

    /**
     * Scan objective file.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.SCAN_OBJECTIVE_FILE_PATH )
    Response scanObjectiveFileFromPath( String objectFilterJson );

    /**
     * Save Ask-On-Run Variables
     *
     * @param workflowId
     *         the workflow id
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.SAVE_VARIABLES )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveAskOnRunVariables( @PathParam( "workflowId" ) String workflowId, String json );

    /**
     * Gets the design variables UI.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the design variables UI
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_DESIGN_VARIABLES_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDesignVariablesUI( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId );

    /**
     * Gets the design summary UI.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the design summary UI
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_DESIGN_SUMMARY_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDesignSummaryUI( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId );

    /**
     * Gets the workflowscheme data.
     *
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the workflowscheme data
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_DESIGN_VARIABLES_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDesignVariableData( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            String objectFilterJson );

    /**
     * Gets the design variable data as label.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable data as label
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_DESIGN_VARIABLES_FILTER )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDesignVariableDataAsLabel( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) UUID workflowId );

    /**
     * Update design variables relation with expression.
     *
     * @param workflowId
     *         the workflow id
     * @param jsonMap
     *         the json map
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_DESIGN_VARIABLES_FILTER )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDesignVariablesRelationWithExpression(
            @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) UUID workflowId, String jsonMap );

    /**
     * Gets the design variable all data.
     *
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the design variable all data
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_DESIGN_VARIABLES_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDesignVariableAllData( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            String objectFilterJson );

    /**
     * Gets the design summary data.
     *
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the design summary data
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_DESIGN_SUMMARY_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDesignSummaryData( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            String objectFilterJson );

    /**
     * Gets the design summary original.
     *
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the design summary original
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_DESIGN_SUMMARY_ORIGINAL_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDesignSummaryOriginal( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            String objectFilterJson );

    /**
     * Gets the context router.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.DESIGN_VARIABLES_CONTEXT )
    @Produces( MediaType.APPLICATION_JSON )
    Response getDesignVariableContextMenu( String filterJson );

    /**
     * Edits the design variable UI.
     *
     * @param designVariableId
     *         the design variable id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.UPDATE_DESIGN_VARIABLES_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editDesignVariableUI( @PathParam( ConstantsAlgorithmsServiceEndpoints.DESIGN_VARIABLE_ID_PARAM ) String designVariableId );

    /**
     * Update SchemeCategory.
     *
     * @param designVariableId
     *         the design variable id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsAlgorithmsServiceEndpoints.UPDATE_DESIGN_VARIABLES_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDesignvariable( @PathParam( ConstantsAlgorithmsServiceEndpoints.DESIGN_VARIABLE_ID_PARAM ) String designVariableId,
            String payload );

    /**
     * Edits the design variable UI.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.ADD_DESIGN_SUMMARY_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addDesignSummaryUI( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId );

    /**
     * Update design summary.
     *
     * @param workflowId
     *         the workflow id
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.ADD_DESIGN_SUMMARY )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addDesignSummary( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId, String json );

    /**
     * Edits the design variable UI.
     *
     * @param workflowId
     *         the workflow id
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.UPDATE_DESIGN_SUMMARY_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editDesignSummaryUI( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            @PathParam( "id" ) String id );

    /**
     * Update design summary.
     *
     * @param workflowId
     *         the workflow id
     * @param id
     *         the id
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.UPDATE_DESIGN_SUMMARY_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDesignSummary( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            @PathParam( "id" ) String id, String json );

    /**
     * Delete design summary.
     *
     * @param workflowId
     *         the workflow id
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.DELETE_DESIGN_SUMMARY )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteDesignSummary( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId, String json );

    /**
     * Gets the objective variables UI.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the objective variables UI
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_OBJECTIVE_VARIABLES_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getObjectiveVariablesUI( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId );

    /**
     * Gets the workflowscheme data.
     *
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the workflowscheme data
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_OBJECTIVE_VARIABLES_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getObjectiveVariableData( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            String objectFilterJson );

    /**
     * Run workflow scheme.
     *
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.RUN_SCHEME_WORKFLOW_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response runWorkflowScheme( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            String objectFilterJson );

    /**
     * Gets the context router.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.OBJECTIVE_VARIABLES_CONTEXT )
    @Produces( MediaType.APPLICATION_JSON )
    Response getObjectiveVariableContextRouter( String filterJson );

    /**
     * Edits the objective variable UI.
     *
     * @param designVariableId
     *         the design variable id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.UPDATE_OBJECTIVE_VARIABLES_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editObjectiveVariableUI(
            @PathParam( ConstantsAlgorithmsServiceEndpoints.OBJECTIVE_VARIABLE_ID_PARAM ) String designVariableId );

    /**
     * Update objective variable.
     *
     * @param objectiveVariableId
     *         the objective variable id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsAlgorithmsServiceEndpoints.UPDATE_OBJECTIVE_VARIABLES_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateObjectiveVariable(
            @PathParam( ConstantsAlgorithmsServiceEndpoints.OBJECTIVE_VARIABLE_ID_PARAM ) String objectiveVariableId, String payload );

    /**
     * Update job.
     *
     * @param jobId
     *         the job id
     * @param jobJson
     *         the job json
     *
     * @return the response
     */
    @PUT
    @Path( "/job/{jobId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateJobAndItsChilds( @PathParam( "jobId" ) UUID jobId, String jobJson );

    /**
     * Get generate image options.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GENERATE_IMAGE_OPTIONS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getGenerateImageOptions();

    /*
     * *************************Views*********************************.
     */

    /**
     * Gets workflow scheme views.
     *
     * @return the all deleted object views
     */
    @GET
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowSchemeViews();

    /**
     * Save workflow scheme view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveWorkflowSchemeView( String objectJson );

    /**
     * Sets the workflow scheme view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setWorkflowSchemeViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete workflow scheme view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteWorkflowSchemeView( @PathParam( "viewId" ) String viewId );

    /**
     * Update workflow scheme view.
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
    Response updateWorkflowSchemeView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets Objective Variables views.
     *
     * @return the all Objective Variables views
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.OBJECTIVE_VARIABLES + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getObjectiveVariablesViews();

    /**
     * Save Objective Variables view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.OBJECTIVE_VARIABLES + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveObjectiveVariablesView( String objectJson );

    /**
     * Sets the Objective Variables view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.OBJECTIVE_VARIABLES + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setObjectiveVariablesViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete Objective Variables view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsAlgorithmsServiceEndpoints.OBJECTIVE_VARIABLES + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteObjectiveVariablesView( @PathParam( "viewId" ) String viewId );

    /**
     * Update Objective Variables view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsAlgorithmsServiceEndpoints.OBJECTIVE_VARIABLES + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateObjectiveVariablesView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets Design Variables views.
     *
     * @return the all Objective Variables views
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.DESIGN_VARIABLES + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDesignVariablesViews();

    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.DESIGN_SUMMARY + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDesignSummaryViews();

    /**
     * Save Design Variables view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.DESIGN_VARIABLES + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveDesignVariablesView( String objectJson );

    /**
     * Sets the Design Variables view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.DESIGN_VARIABLES + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setDesignVariablesViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete Design Variables view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsAlgorithmsServiceEndpoints.DESIGN_VARIABLES + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteDesignVariablesView( @PathParam( "viewId" ) String viewId );

    /**
     * Update Design Variables view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsAlgorithmsServiceEndpoints.DESIGN_VARIABLES + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDesignVariablesView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Check syntax.
     *
     * @param workflowId
     *         the workflow id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.DESIGN_SUMMARY_FILTER_SYNTAX )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response checkSyntax( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId, String objectJson );

    /**
     * Download design summary data file CS vs.
     *
     * @param workflowId
     *         the workflow id
     * @param token
     *         the token
     *
     * @return the response
     */
    @GET
    @Path( "/{workflowId}/designsummary/download" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response downloadDesignSummaryDataFileCSVs( @PathParam( "workflowId" ) String workflowId );

    /**
     * Import design summary data from csv UI.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the response
     */
    @GET
    @Path( "/{workflowId}/designsummary/importcsv/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response importDesignSummaryDataFromCsvUI( @PathParam( "workflowId" ) String workflowId );

    /**
     * Import design summary data from CSV file.
     *
     * @param workflowId
     *         the workflow id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{workflowId}/designsummary/importcsv" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response importDesignSummaryDataFromCSVFile( @PathParam( "workflowId" ) String workflowId, String objectJson );

    /**
     * Gets the generate design summary data.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the generate design summary data
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_GENERATE_DESIGN_SUMMARY_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getGenerateDesignSummaryData( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId );

    /**
     * Gets the post process form.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the post process form
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.FORM_POSTPROCESS_WORKFLOW_ID )
    Response getPostProcessForm( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId );

    /**
     * Save scheme option form.
     *
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.FORM_POSTPROCESS_WORKFLOW_ID )
    Response getWorkflowFromPostProcessForm( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            String objectFilterJson );

}