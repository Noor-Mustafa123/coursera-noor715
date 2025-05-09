package de.soco.software.simuspace.server.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public interface PropertiesService {

    /**
     * Gets all properties.
     *
     * @return the all properties
     */
    @GET
    @Path( "/" )
    Response getAllProperties();

}
