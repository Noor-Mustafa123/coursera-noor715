/**
 *
 */

package de.soco.software.simuspace.wizards.service.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;

/**
 * The Interface SuSObjectTreeService.
 *
 * @author Huzaifah
 */
public interface WizardsService {

    /**
     * Gets the variant.
     *
     * @param id
     *         the id
     *
     * @return the variant
     */
    @GET
    @Path( "/variant/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getVariant( @PathParam( "id" ) String id );

    /**
     * Save variant.
     *
     * @param id
     *         the id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/variant/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response saveVariant( @PathParam( "id" ) String id, String payload );

    /**
     * Run variant.
     *
     * @param id
     *         the id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/variant/{id}/run" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response runVariant( @PathParam( "id" ) String id, String payload );

    /**
     * UI Items *.
     *
     * @param id
     *         the id
     *
     * @return the tabs UI
     */
    @GET
    @Path( "/variant/{id}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getTabsUI( @PathParam( "id" ) String id );

    /**
     * Gets the general tab UI.
     *
     * @param id
     *         the id
     *
     * @return the general tab UI
     */
    @GET
    @Path( "/variant/{id}/general" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getGeneralTabUI( @PathParam( "id" ) String id );

    /**
     * Gets the objects tab UI.
     *
     * @param id
     *         the id
     *
     * @return the objects tab UI
     */
    @GET
    @Path( "/variant/{id}/objects/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getObjectsTabUI( @PathParam( "id" ) String id );

    /**
     * Gets the objects tab data.
     *
     * @param id
     *         the id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the objects tab data
     */
    @POST
    @Path( "/variant/{id}/objects/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getObjectsTabData( @PathParam( "id" ) String id, String objectFilterJson );

    /**
     * Change object to loadcase relation.
     *
     * @param variantId
     *         the variant id
     * @param objectId
     *         the object id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @PUT
    @Path( "/variant/{id}/objects/{objectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response changeObjectToLoadcaseRelation( @PathParam( "id" ) String variantId, @PathParam( "objectId" ) String objectId,
            String payload );

    /**
     * Gets the assembly tab UI.
     *
     * @param id
     *         the id
     *
     * @return the assembly tab UI
     */
    @GET
    @Path( "/variant/{id}/assembly/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getAssemblyTabUI( @PathParam( "id" ) String id );

    /**
     * Gets the solver tab UI.
     *
     * @param id
     *         the id
     *
     * @return the solver tab UI
     */
    @GET
    @Path( "/variant/{id}/solver/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getSolverTabUI( @PathParam( "id" ) String id );

    /**
     * Gets the post tab UI.
     *
     * @param id
     *         the id
     *
     * @return the post tab UI
     */
    @GET
    @Path( "/variant/{id}/post/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getPostTabUI( @PathParam( "id" ) String id );

    /**
     * Gets the workflow fields.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow fields
     */
    @GET
    @Path( "/variant/workflow/{workflowId}/fields" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getWorkflowFields( @PathParam( "workflowId" ) String workflowId );

    /**
     * Gets the submit tab UI.
     *
     * @param id
     *         the id
     *
     * @return the submit tab UI
     */
    @GET
    @Path( "/variant/{id}/submit/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getSubmitTabUI( @PathParam( "id" ) String id );

    /**
     * Gets the submit tab data.
     *
     * @param id
     *         the id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the submit tab data
     */
    @POST
    @Path( "/variant/{id}/submit/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getSubmitTabData( @PathParam( "id" ) String id, String objectFilterJson );

    /**
     * Change submit loadcase relation.
     *
     * @param variantId
     *         the variant id
     * @param loadcaseId
     *         the loadcase id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @PUT
    @Path( "/variant/{id}/submit/{loadcaseId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response changeSubmitLoadcaseRelation( @PathParam( "id" ) String variantId, @PathParam( "loadcaseId" ) String loadcaseId,
            String payload );

    /**
     * FUNCTIONS *.
     *
     * @param id
     *         the id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/variant/{id}/order" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response saveObjectsOrder( @PathParam( "id" ) String id, String payload );

    /**
     * COPY VARIANT *.
     *
     * @param refId
     *         the ref id
     *
     * @return the copy vairnat UI
     */
    @GET
    @Path( "/variant/reference/{refId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCopyVairnatUI( @PathParam( "refId" ) String refId );

    /**
     * Creates the vairnat copy.
     *
     * @param refId
     *         the ref id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/variant/reference/{refId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response createVairnatCopy( @PathParam( "refId" ) String refId, String payload );

    /**
     * Gets the dummy option form.
     *
     * @param variantId
     *         the variantId
     * @param selectionId
     *         the selectionId
     *
     * @return the dummy option form
     */
    @GET
    @Path( "/dummy/create/variant/{variantId}/solverType/{solverType}/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyOptionFormForReferenceSelection( @PathParam( "variantId" ) String variantId,
            @PathParam( "solverType" ) String solverType, @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the dummy refrence option form.
     *
     * @param variantId
     *         the id
     *
     * @return the dummy refrence option form
     */
    @GET
    @Path( "/dummy/create/reference/options/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyRefrenceOptionForm( @PathParam( "id" ) String variantId );

    /**
     * Gets the dummy refrence variant selection form.
     *
     * @param variantId
     *         the variantid
     * @param optionId
     *         the option id
     *
     * @return the dummy refrence variant selection form
     */
    @GET
    @Path( "/dummy/reference/variant/{variantId}/solvertype/{typeId}/option/{optionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyRefrenceVariantSelectionForm( @PathParam( "variantId" ) String variantId,
            @PathParam( "typeId" ) String solverType, @PathParam( "optionId" ) String optionId );

    /**
     * Gets the dummy refrence solver selection form.
     *
     * @param variantId
     *         the variantId
     * @param typeId
     *         the type id
     *
     * @return the dummy refrence solver selection form
     */
    @GET
    @Path( "/dummy/reference/variant/{variantId}/solvertype/{typeId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyRefrenceSolverSelectionForm( @PathParam( "variantId" ) String variantId,
            @PathParam( "typeId" ) String typeId );

    /**
     * Gets the dummy type form.
     *
     * @param typeId
     *         the type id
     *
     * @return the dummy type form
     */
    @GET
    @Path( "/dummy/bind/solvertype/{solverType}/refvar/{variantReference}/dummytype/{dummyType}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyTypeForm( @PathParam( "solverType" ) String solverType, @PathParam( "variantReference" ) String refVariant,
            @PathParam( "dummyType" ) String dummyType );

    /**
     * Gets the dummy type form.
     *
     * @param typeId
     *         the type id
     *
     * @return the dummy type form
     */
    @GET
    @Path( "/dummy/bind/solvertype/{solverType}/refvar/{variantReference}/dummytype/{dummyType}/reference/{referenceId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyTypeFormForReferenceSelection( @PathParam( "solverType" ) String solverType,
            @PathParam( "variantReference" ) String refVariant, @PathParam( "dummyType" ) String dummyType,
            @PathParam( "referenceId" ) String referenceId );

    /**
     * Gets the dummy file UI.
     *
     * @return the dummy file UI
     */
    @GET
    @Path( "/dummyfiles/{dummyType}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyFileUI();

    /**
     * Gets the dummy file list.
     *
     * @param dummyType
     *         the dummy type
     * @param payload
     *         the payload
     *
     * @return the dummy file list
     */
    @POST
    @Path( "/dummyfiles/{dummyType}/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyFileList( @PathParam( "dummyType" ) String dummyType, String payload );

    /**
     * Gets the load case UI.
     *
     * @return the load case UI
     */
    @GET
    @Path( "/loadcase/{dummyType}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getLoadCaseUI();

    /**
     * Gets the load case selected dummy type list.
     *
     * @param dummyType
     *         the dummy type
     * @param payload
     *         the payload
     *
     * @return the load case selected dummy type list
     */
    @POST
    @Path( "/loadcase/{dummyType}/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getLoadCaseSelectedDummyTypeList( @PathParam( "dummyType" ) String dummyType, String payload );

    /**
     * Gets the dummy files selection.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the dummy files selection
     */
    @POST
    @Path( "dummyfiles/{dummyType}/list/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyFilesSelection( @PathParam( "selectionId" ) String selectionId, String filterJson );

    /**
     * Gets the load case selection.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the load case selection
     */
    @POST
    @Path( "loadcase/{dummyType}/list/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getLoadCaseSelection( @PathParam( "selectionId" ) String selectionId, String filterJson );

    /**
     * Gets the dummy load case form.
     *
     * @param id
     *         the id
     *
     * @return the dummy load case form
     */
    @GET
    @Path( "/dummy/loadcase/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyLoadCaseForm( @PathParam( "id" ) UUID id );

    /**
     * Fetch all cb 2 sub models.
     *
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( "/dummy/submodels" )
    Response fetchAllCb2SubModels( String json );

    /**
     * Creates the and run dummy variant.
     *
     * @param parentId
     *         the parent id
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( "/dummy/{parentId}/run" )
    Response createAndRunDummyVariant( @PathParam( "parentId" ) String parentId, String json );

    /**
     * *************************Views*********************************.
     *
     * @return the objects views
     */

    /**
     * Gets objects views.
     *
     * @return the all deleted object views
     */
    @GET
    @Path( "/variant/{id}/objects" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getObjectsViews();

    /**
     * Save objects view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/variant/{id}/objects" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response saveObjectsView( String objectJson );

    /**
     * Sets the objects view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/variant/{id}/objects" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response setObjectsViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete objects view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/variant/{id}/objects" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response deleteObjectsView( @PathParam( "viewId" ) String viewId );

    /**
     * Update objects view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/variant/{id}/objects" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response updateObjectsView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets the bmw cae bench tree data.
     *
     * @return the bmw cae bench tree data
     */
    @GET
    @Path( "/cb2/tree" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getBmwCaeBenchTreeData();

    @GET
    @Path( "dummyfiles/list/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getBmwCaeBenchObjetBySelection( @PathParam( "selectionId" ) String selectionId );

    @GET
    @Path( "/dummy/create/reference/options/cb2/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getDummyRefrenceCb2OptionForm( @PathParam( "id" ) String variantId );

    @GET
    @Path( "/cb2/variant/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2Variant( @PathParam( "id" ) String id );

    @POST
    @Path( "/cb2/variant/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response saveCb2Variant( @PathParam( "id" ) String id, String payload );

    @POST
    @Path( "/cb2/variant/{id}/run" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response runCb2Variant( @PathParam( "id" ) String id, String payload );

    @GET
    @Path( "/cb2/variant/{id}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2TabsUI( @PathParam( "id" ) String id );

    @GET
    @Path( "/cb2/variant/{id}/general" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2GeneralTabUI( @PathParam( "id" ) String id );

    @GET
    @Path( "/cb2/variant/{id}/objects/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2ObjectsTabUI( @PathParam( "id" ) String id );

    @POST
    @Path( "/cb2/variant/{id}/objects/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2ObjectsTabData( @PathParam( "id" ) String id, String objectFilterJson );

    @PUT
    @Path( "/cb2/variant/{id}/objects/{objectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response changeCb2ObjectToLoadcaseRelation( @PathParam( "id" ) String variantId, @PathParam( "objectId" ) String objectId,
            String payload );

    @GET
    @Path( "/cb2/variant/{id}/assembly/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2AssemblyTabUI( @PathParam( "id" ) String id );

    @GET
    @Path( "/cb2/variant/{id}/solver/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2SolverTabUI( @PathParam( "id" ) String id );

    @GET
    @Path( "/cb2/variant/{id}/post/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2PostTabUI( @PathParam( "id" ) String id );

    @GET
    @Path( "/cb2/variant/workflow/{workflowId}/fields" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2WorkflowFields( @PathParam( "workflowId" ) String workflowId );

    @GET
    @Path( "/cb2/variant/{id}/submit/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2SubmitTabUI( @PathParam( "id" ) String id );

    @POST
    @Path( "/cb2/variant/{id}/submit/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2SubmitTabData( @PathParam( "id" ) String id, String objectFilterJson );

    @GET
    @Path( "/cb2/variant/reference/{refId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2CopyVairnatUI( @PathParam( "refId" ) String refId );

    @GET
    @Path( "/cb2/run/general/{objectId}/dropdown/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2RunGeneralDropDown( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String id );

    @GET
    @Path( "/cb2/run/general/{objectId}/item/{item}/dropdown/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2RunGeneralDefinitionDropDown( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String id,
            @PathParam( "item" ) String item );

    @PUT
    @Path( "/cb2/variant/{id}/submit/{loadcaseId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response changeCb2SubmitLoadcaseRelation( @PathParam( "id" ) String variantId, @PathParam( "loadcaseId" ) String loadcaseId,
            String payload );

    @POST
    @Path( "/cb2/variant/{id}/order" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response saveCb2ObjectsOrder( @PathParam( "id" ) String id, String payload );

    /**
     * SECTION FOR CB2AssembleAndSimulate WFE APIs
     */
    @GET
    @Path( "/cb2/wf/studydef/variant/{variantSId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2StudyDefsByVariant( @PathParam( "variantSId" ) String variantSId );

    @GET
    @Path( "/cb2/wf/scenario/variant/{variantSId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2ScenariosByVariant( @PathParam( "variantSId" ) String variantSId );

    @GET
    @Path( "/cb2/wf/simdef/scenario/{variantAndScenarioOid}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2SimDefsByScenario( @PathParam( "variantAndScenarioOid" ) String variantAndScenarioOid );

    @GET
    @Path( "/cb2/wf/setup/{assembleSolvePost}/studydef/{studydefOid}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2SetupByStudyDef( @PathParam( "assembleSolvePost" ) String assembleSolvePost,
            @PathParam( "studydefOid" ) String studydefOid );

    @GET
    @Path( "/cb2/wf/{assembleSolvePost}/{dynamicValKeyName}/setup/{setupOid}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    public Response getCb2ParamsBySetup( @PathParam( "assembleSolvePost" ) String assembleSolvePost,
            @PathParam( "dynamicValKeyName" ) String dynamicValKeyName, @PathParam( "setupOid" ) String setupOid );

}
