package de.soco.software.simuspace.suscore.core.service;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.constants.ConstantsSelectionServiceEndPoints;

/**
 * This Interface is responsible for all the operation Of Selections.
 *
 * @author Noman Arshad
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface SelectionService {

    /**
     * Save and update user selections.
     *
     * @param jsonSelection
     *         the json selection
     *
     * @return the response
     */
    @POST
    @Path( ConstantsSelectionServiceEndPoints.SAVE_SELECTIONS )
    Response saveAndUpdateSelections( String jsonSelection );

    /**
     * Save and update user selections with origin.
     *
     * @param origin
     *         the origin
     * @param jsonSelection
     *         the json selection
     *
     * @return the response
     */
    @POST
    @Path( ConstantsSelectionServiceEndPoints.SAVE_SELECTIONS_ORIGIN )
    Response saveAndUpdateSelectionsWithOrigin( @PathParam( "origin" ) String origin, String jsonSelection );

    /**
     * Gets the all selections.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the all selections
     */
    @GET
    @Path( ConstantsSelectionServiceEndPoints.GET_SELECTION_IDS_ONLY )
    Response getAllSelectionIds( @PathParam( "id" ) String selectionId );

    /**
     * Adds the selection.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsSelectionServiceEndPoints.ADD_SELECTIONS )
    Response addSelection( @PathParam( "selectionId" ) String selectionId, String filterJson );

    /**
     * Removes the selection.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsSelectionServiceEndPoints.REMOVE_SELECTIONS )
    Response removeSelection( @PathParam( "selectionId" ) String selectionId, String filterJson );

    /**
     * Gets the selection sus entity UI.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the selection sus entity UI
     */
    @GET
    @Path( ConstantsSelectionServiceEndPoints.UI_GENERIC_DTO )
    Response getSelectionSusEntityUI( @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the selection sus entity list.
     *
     * @param selectionId
     *         the selection id
     * @param filter
     *         the filter
     *
     * @return the selection sus entity list
     */
    @POST
    @Path( ConstantsSelectionServiceEndPoints.LIST_GENERIC_DTO )
    Response getSelectionSusEntityList( @PathParam( "selectionId" ) String selectionId, String filter );

    /**
     * Gets the selection sus entity list.
     *
     * @param filter
     *         the filter
     *
     * @return the selection sus entity list
     */
    @POST
    @Path( ConstantsSelectionServiceEndPoints.ITEMS_LIST )
    Response getSelectionSusEntityList( String filter );

    /**
     * Re order selection.
     *
     * @param selectionId
     *         the selection id
     * @param filter
     *         the filter
     *
     * @return the response
     */
    @POST
    @Path( ConstantsSelectionServiceEndPoints.REORDER_SELECTION )
    Response reOrderSelection( @PathParam( "selectionId" ) String selectionId, String filter );

    /**
     * Gets the all selection sort.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the all selection sort
     */
    @GET
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_SORT )
    Response getAllSelectionWFObjectSort( @PathParam( "selectionId" ) String selectionId );

    /**
     * Save sorted selection.
     *
     * @param selectionId
     *         the selection id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_SORT )
    Response saveWFObjectSortedSelection( @PathParam( "selectionId" ) String selectionId, String payload );

    /**
     * Save and update selectionitems with attributes.
     *
     * @param selectionId
     *         the selection id
     * @param selectionItemId
     *         the selection item id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_ITEM_ATTRIBUTE )
    Response saveAndUpdateSelectionitemsWithAttributes( @PathParam( "selectionId" ) String selectionId,
            @PathParam( "selectionItemId" ) String selectionItemId, String payload );

    /**
     * Gets the attribute UI.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the attribute UI
     */
    @GET
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_ATTRIBUTE_UI )
    Response getAttributeUI( @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the attribute ui list.
     *
     * @param selectionId
     *         the selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the attribute ui list
     */
    @POST
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_ATTRIBUTE_LIST )
    Response getAttributeUiList( @PathParam( "selectionId" ) String selectionId, String jsonFilter );

    /**
     * Gets the attribute ui create.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the attribute ui create
     */
    @GET
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_ATTRIBUTE_UI_CREATE )
    Response getAttributeUiCreate( @PathParam( "selectionId" ) String selectionId );

    /**
     * Save attribute ui.
     *
     * @param selectionId
     *         the selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the response
     */
    @POST
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_ATTRIBUTE )
    Response saveAttributeUi( @PathParam( "selectionId" ) String selectionId, String jsonFilter );

    /**
     * Gets the attribute context.
     *
     * @param selectionId
     *         the selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the attribute context
     */
    @POST
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_ATTRIBUTE_CONTEXT )
    Response getAttributeContext( @PathParam( "selectionId" ) String selectionId, String jsonFilter );

    /**
     * Gets the edits the attribute form.
     *
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     *
     * @return the edits the attribute form
     */
    @GET
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_ATTRIBUTE_EDIT )
    Response getEditAttributeForm( @PathParam( "selectionId" ) String selectionId,
            @PathParam( "attribSelectionId" ) String attribSelectionId );

    /**
     * Delete attribute by selection.
     *
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_ATTRIBUTE_DELETE )
    Response deleteAttributeBySelection( @PathParam( "selectionId" ) String selectionId,
            @PathParam( "attribSelectionId" ) String attribSelectionId );

    /**
     * Update attribute W ith selection.
     *
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
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_ATTRIBUTE_DELETE )
    Response updateAttributeWIthSelection( @PathParam( "selectionId" ) String selectionId,
            @PathParam( "attribSelectionId" ) String attribSelectionId, String jsonAtrib );

    /**
     * Save attribute values with selection.
     *
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
    @Path( "/{selectionId}/record/{attribSelectionId}" )
    Response saveAttributeValuesWithSelection( @PathParam( "selectionId" ) String selectionId,
            @PathParam( "attribSelectionId" ) String attribSelectionId, String jsonFilter );

    /**
     * Save attribute values with selection.
     *
     * @param selectionId
     *         the selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the response
     */
    @POST
    @Path( "/{selectionId}/record" )
    Response saveSsfeAttributeValuesWithSelection( @PathParam( "selectionId" ) String selectionId, String jsonFilter );

    /**
     * Gets the attribute ui create option.
     *
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     * @param option
     *         the option
     *
     * @return the attribute ui create option
     */
    @GET
    @Path( ConstantsSelectionServiceEndPoints.SELECTION_ATTRIBUTE_UI_SELECT_OPTION )
    Response getAttributeUiCreateOption( @PathParam( "selectionId" ) String selectionId,
            @PathParam( "attribSelectionId" ) String attribSelectionId, @PathParam( "option" ) String option );

    /**
     * Updates view of selection table.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the selection sus entity UI
     */
    @PUT
    @Path( ConstantsSelectionServiceEndPoints.UI_UPDATE_VIEW )
    Response updateSelectionTableView( @PathParam( "selectionId" ) String selectionId, String columnsJson );

}
