package de.soco.software.simuspace.suscore.object.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The router service interface class
 */

@Path( "/" )
public interface SuSRouterService {

    @GET
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getRouters();

}
