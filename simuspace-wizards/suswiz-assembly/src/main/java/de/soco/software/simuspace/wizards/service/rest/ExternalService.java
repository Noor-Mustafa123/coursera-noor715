package de.soco.software.simuspace.wizards.service.rest;

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

import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;

/**
 * The Interface ExternalService.
 */
public interface ExternalService {

    /**
     * Save and update selections.
     *
     * @param jsonSelection
     *         the json selection
     *
     * @return the response
     */
    @POST
    @Path( "/{client}/selection" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveAndUpdateSelections( String jsonSelection );

    /**
     * Filter object tree.
     *
     * @param external
     *         the external
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{external}/object-tree" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response filterObjectTree( @PathParam( "external" ) String external, String objectJson );

    /**
     * External ui.
     *
     * @param external
     *         the external
     * @param solverType
     *         the solver type
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "/{external}/solverType/{type}/data/project/{id}/data/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response externalUi( @PathParam( "external" ) String external, @PathParam( "type" ) String solverType,
            @PathParam( "id" ) String id );

    /**
     * External workflow ui.
     *
     * @param external
     *         the external
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "/{external}/data/project/{id}/data/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response externalWorkflowUi( @PathParam( "external" ) String external, @PathParam( "id" ) String id );

    /**
     * External list.
     *
     * @param external
     *         the external
     * @param solverType
     *         the solver type
     * @param id
     *         the id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/{external}/solverType/{type}/data/project/{id}/data/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response externalList( @PathParam( "external" ) String external, @PathParam( "type" ) String solverType,
            @PathParam( "id" ) String id, String payload );

    /**
     * External list.
     *
     * @param external
     *         the external
     * @param id
     *         the id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/{external}/data/project/{id}/data/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response externalWorkflowList( @PathParam( "external" ) String external, @PathParam( "id" ) String id, String payload );

    /**
     * Adds the selection.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @PUT
    @Path( "/{external}/selection/{selectionId}/add" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addSelection( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId, String filterJson );

    /**
     * Removes the selection.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @PUT
    @Path( "/{external}/selection/{selectionId}/remove" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response removeSelection( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            String filterJson );

    /**
     * External selection loadcase UI.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( "/{external}/selection/{selectionId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response externalSelectionUI( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId );

    /**
     * External selection list.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/{external}/selection/{selectionId}/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response externalSelectionList( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            String payload );

    /**
     * External all selection ids type.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( "/{external}/solverType/{type}/selection/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response externalAllSelectionIdsType( @PathParam( "external" ) String external, @PathParam( "id" ) String selectionId );

    /**
     * External all selection ids.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( "/{external}/selection/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response externalAllSelectionIds( @PathParam( "external" ) String external, @PathParam( "id" ) String selectionId );

    /**
     * Gets the sorted selection.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the sorted selection
     */
    @GET
    @Path( "/{external}/selection/{selectionId}/sort" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSortedSelection( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId );

    /**
     * Save sorted selection.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/{external}/selection/{selectionId}/sort" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveSortedSelection( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            String payload );

    /**
     * *************************Views*********************************.
     *
     * @param external
     *            the external
     * @param id
     *            the id
     * @return the external selector views
     */

    /**
     * Gets loadcase selector views.
     *
     * @return the all views
     */
    @GET
    @Path( "/{external}/solverType/{type}/data/project/{id}/data" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getExternalSelectorViewsForSolverType( @PathParam( "external" ) String external, @PathParam( "id" ) String id );

    @GET
    @Path( "/{external}/data/project/{id}/data" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getExternalSelectorViewsForDataProject( @PathParam( "external" ) String external, @PathParam( "id" ) String id );

    /**
     * Save loadcase selector view.
     *
     * @param external
     *         the external
     * @param id
     *         the id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{external}/solverType/{type}/data/project/{id}/data" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response saveExternalSelectorViewForSolverType( @PathParam( "external" ) String external, @PathParam( "id" ) String id,
            String objectJson );

    @POST
    @Path( "/{external}/data/project/{id}/data" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response saveExternalSelectorViewForDataProject( @PathParam( "external" ) String external, @PathParam( "id" ) String id,
            String objectJson );

    /**
     * Sets the loadcase selector view as default.
     *
     * @param external
     *         the external
     * @param viewId
     *         the view id
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "/{external}/data/project/{id}/data" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response setExternalSelectorViewAsDefault( @PathParam( "external" ) String external, @PathParam( "viewId" ) String viewId,
            @PathParam( "id" ) String id );

    /**
     * Delete loadcase selector view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{external}/solverType/{type}/data/project/{id}/data" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response deleteExternalSelectorViewForSolverType( @PathParam( "viewId" ) String viewId );

    @DELETE
    @Path( "/{external}/data/project/{id}/data" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response deleteExternalSelectorView( @PathParam( "viewId" ) String viewId );

    /**
     * Update loadcase selector view.
     *
     * @param external
     *         the external
     * @param viewId
     *         the view id
     * @param id
     *         the id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{external}/solverType/{type}/data/project/{id}/data" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response updateExternalSelectorViewForSolverType( @PathParam( "external" ) String external,
            @PathParam( "viewId" ) String viewId, @PathParam( "id" ) String id, String objectJson );

    @PUT
    @Path( "{external}/data/project/{id}/data" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response updateExternalSelectorView( @PathParam( "external" ) String external, @PathParam( "viewId" ) String viewId,
            @PathParam( "id" ) String id, String objectJson );

    /**
     * Gets the all selection ids.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the all selection ids
     */
    @GET
    @Path( "/{external}/selection/{selectionId}" )
    Response getAllSelectionIds( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the selection attribute UI.
     *
     * @param external
     *         the external
     * @param selectionid
     *         the selectionid
     *
     * @return the selection attribute UI
     */
    @GET
    @Path( "/{external}/selection/{selectionid}/attribute/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getSelectionAttributeUI( @PathParam( "external" ) String external, @PathParam( "selectionid" ) String selectionid );

    /**
     * Gets the attribute ui list.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the attribute ui list
     */
    @POST
    @Path( "/{external}/selection/{selectionId}/attribute/list" )
    Response getAttributeUiList( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            String jsonFilter );

    /**
     * Gets the attribute ui create.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the attribute ui create
     */
    @GET
    @Path( "/{external}/selection/{selectionId}/attribute/ui/create" )
    Response getAttributeUiCreate( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId );

    /**
     * Save attribute ui.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the response
     */
    @POST
    @Path( "/{external}/selection/{selectionId}/attribute" )
    Response saveAttributeUi( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            String jsonFilter );

    /**
     * Gets the attribute context.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the attribute context
     */
    @POST
    @Path( "/{external}/selection/{selectionId}/attribute/context" )
    Response getAttributeContext( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            String jsonFilter );

    /**
     * Gets the edits the attribute form.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     *
     * @return the edits the attribute form
     */
    @GET
    @Path( "/{external}/selection/ui/edit/{selectionId}/attribute/{attribSelectionId}" )
    Response getEditAttributeForm( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            @PathParam( "attribSelectionId" ) String attribSelectionId );

    /**
     * Delete attribute by selection.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{external}/selection/{selectionId}/attribute/{attribSelectionId}" )
    Response deleteAttributeBySelection( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            @PathParam( "attribSelectionId" ) String attribSelectionId );

    /**
     * Update attribute W ith selection.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     * @param jsonAtrib
     *         the json atrib
     *
     * @return the response
     */
    @PUT
    @Path( "/{external}/selection/{selectionId}/attribute/{attribSelectionId}" )
    Response updateAttributeWIthSelection( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            @PathParam( "attribSelectionId" ) String attribSelectionId, String jsonAtrib );

    /**
     * Save attribute values with selection.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the response
     */
    @POST
    @Path( "/{external}/selection/{selectionId}/record/{attribSelectionId}" )
    Response saveAttributeValuesWithSelection( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            @PathParam( "attribSelectionId" ) String attribSelectionId, String jsonFilter );

    /**
     * Gets the attribute ui create option.
     *
     * @param external
     *         the external
     * @param option
     *         the option
     *
     * @return the attribute ui create option
     */
    @GET
    @Path( "/bmw/variant/{selectionId}/simdef/{simdefOid}/submodels" )
    Response getBmwCaeBenchVariantSubModelObjectListFromCB2( @PathParam( "selectionId" ) String selectionId,
            @PathParam( "simdefOid" ) String simdefOid );

    /**
     * Adds the bmw cae bench variant sub model object list.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( "/bmw/variant/{selectionId}/simdef/{simdefOid}/submodels/options" )
    Response addBmwCaeBenchVariantSubModelFormUI( @PathParam( "selectionId" ) String selectionId,
            @PathParam( "simdefOid" ) String simdefOid );

    /**
     * Adds the bmw cae bench variant sub model object list.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( "/bmw/variant/{selectionId}/simdef/{simdefOid}/submodels/options/{typeId}" )
    Response addBmwCaeBenchVariantSubModelFormOptions( @PathParam( "selectionId" ) String selectionId,
            @PathParam( "typeId" ) String typeId, @PathParam( "simdefOid" ) String simdefOid );

    /**
     * Get ProjectItem options.
     *
     * @param projectPath
     *         the path
     *
     * @return the response
     */
    @GET
    @Path( "/bmw/variant/projectItem/options" )
    Response getProjectItemOptions( @QueryParam( "path" ) String projectPath );

    /**
     * Get ProjectPhase options.
     *
     * @param path
     *         the path
     *
     * @return the response
     */
    @GET
    @Path( "/bmw/variant/projectPhase/options" )
    Response getProjectPhaseOptions( @QueryParam( "path" ) String projectPath );

    /**
     * Updates view of selection table.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the selection sus entity UI
     */
    @PUT
    @Path( "/{external}/selection/{selectionId}/ui/updateView" )
    Response updateExternalSelectionTableView( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            String columnsJson );

    /**
     * Gets the attribute ui create option.
     *
     * @param external
     *         the external
     * @param option
     *         the option
     *
     * @return the attribute ui create option
     */
    @GET
    @Path( "/{external}/selection/{selectionId}/attribute/{attribSelectionId}/ui/createoption/{option}" )
    Response getAttributeUiCreateOption( @PathParam( "external" ) String external, @PathParam( "selectionId" ) String selectionId,
            @PathParam( "attribSelectionId" ) String attribSelectionId, @PathParam( "option" ) String option );

}
