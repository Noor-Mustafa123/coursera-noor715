package de.soco.software.simuspace.server.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.constants.ConstantsAlgorithmsServiceEndpoints;

/**
 * The interface Algorithm service.
 */
public interface AlgorithmService {

    /**
     * Gets algorithms data.
     *
     * @return the algorithms data
     */
    @GET
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_SCHEME_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAlgorithmsDataCount();

}
