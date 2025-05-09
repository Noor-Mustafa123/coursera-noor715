package de.soco.software.simuspace.suscore.permissions.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;

/**
 * The Interface PermissionService provides the Permission API.
 *
 * @author Ahsan Khan
 * @since 2.0
 */

@Path( "/" )
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface PermissionService {

    /**
     * Checks if is permitted.
     *
     * @param resource
     *         the resource
     *
     * @return the response
     */
    @POST
    @Path( "/permitted" )
    @Produces( MediaType.APPLICATION_JSON )
    Response isPermitted( String resource );

}
