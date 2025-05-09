package de.soco.software.simuspace.suscore.rights.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;

/**
 * The interface Rights service.
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface RightsService {

    /**
     * Gets all rights list.
     *
     * @return the all rights list
     */
    @GET
    @Path( "/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllRightsList();

}
