package de.soco.software.simuspace.suscore.object.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;
import org.json.simple.parser.ParseException;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;

/**
 * This Interface is responsible for all the operation related to sus object.
 *
 * @author Nosheen.Sharif
 * @deprecated use {@link DataService} instead.
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
@Deprecated // endpoints are moved to DataSerivce and that will be deleted after matadata movements.
public interface SuSObjectService {

    /**
     * Get Object Versions by Object Id And Type
     *
     * @param objectType
     *
     * @return
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.GET_OBJECT_VERSION_LIST_BY_TYPE )
    @Produces( { MediaType.APPLICATION_JSON } )

    public Response getObjectVersionsListByIdAndType( @PathParam( "objectType" ) String objectType,
            @PathParam( "objectId" ) String objectId );

    /**
     * Get Object By Id .This will get the object with latest version
     *
     * @param objectType
     * @param objectId
     *
     * @return
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.GET_OBJECT_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )

    public Response getObjectById( @PathParam( "objectType" ) String objectType, @PathParam( "objectId" ) String objectId );

    /**
     * Get Object By Id and version Id.This will get the object with given version
     *
     * @param objectType
     * @param objectId
     * @param version
     *
     * @return
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.GET_OBJECT_BY_ID_AND_VERSION )
    @Produces( { MediaType.APPLICATION_JSON } )

    public Response getObjectByIdAndVersionId( @PathParam( "objectType" ) String objectType, @PathParam( "objectId" ) String objectId,
            @PathParam( "version" ) int version );

    /**
     * Create Object
     *
     * @param objectJson
     *
     * @throws ParseException
     * @throws SusException
     * @Request payload :{ "id": "62c4c2f6-15f8-11e7-93ae-92361f002671", "name": "Varinat_17-may", "class":
     * "de.soco.software.simuspace.suscore.data.entity.VariantEntity", "isCategorizable": false, "isCustomizable": false,
     * "customAttributes": [{"id": "","name": "customAtt1","type": "dropdown", "values": [ "1", "2", "3" ],"defaultValue": "1" }],
     * "hasLifeCycle":true, "lifeCycleStrategy":["Strategy1","Strategy2"], "isMassData":true, "isContainer":true,
     * "contains":["List<dataobject>"], "links":["Loadcase"] }
     * @Response payload:{"data":{"id":"902a30c4-a090-46b9-87e1-8983b57c50e5","name":"Varinat_17-may","hasLifeCycle":true,
     * "lifeCycleStrategy":null,"customAttribute":null,"contains":null,"links":null,"className":
     * "de.soco.software.simuspace.suscore.data.entity.VariantEntity","table":null,"version":{"id":1,"name":""},"status":1,
     * "createdOn":1495024796565,"modifiedOn":null,"massdata":true,"versionable":true,"categorizable":false,"container":true,
     * "customizable":false},"success":true}
     */
    @POST
    @Path( ConstantsObjectServiceEndPoints.CREATE_OBJECT )
    @Produces( { MediaType.APPLICATION_JSON } )

    public Response createObject( String objectJson );

    /**
     * Update Object.
     *
     * @param objectId
     *         the object id*
     * @param object
     *         Json the
     *
     *         object Json*@return
     *
     *         the response
     */

    @PUT
    @Path( ConstantsObjectServiceEndPoints.UPDATE_OBJECT )
    @Produces( { MediaType.APPLICATION_JSON } )

    public Response updateObject( @PathParam( "objectId" ) String objectId, String objectJson );

    /**
     * Update attributes.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsObjectServiceEndPoints.UPDATE_OBJECT_ATTRIBUT_VALUE )
    @Produces( { MediaType.APPLICATION_JSON } )

    public Response updateAttributes( @PathParam( "objectId" ) String objectId, String objectJson );

}