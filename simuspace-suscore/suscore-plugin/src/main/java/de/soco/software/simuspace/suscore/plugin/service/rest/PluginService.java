package de.soco.software.simuspace.suscore.plugin.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.plugin.constants.ConstantsPluginServiceEndPoints;
import de.soco.software.simuspace.suscore.plugin.constants.ConstantsPlugins;

/**
 * The Interface to provide the API End Points to Manage sus-Plugins.
 *
 * @author Nosheen.Sharif
 * @since 2.0
 */
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface PluginService {

    /**
     * Add the Plugin.
     *
     * @param zipFilePath
     *         the role json
     *
     * @return the response
     *
     * @throws SusException
     *         the sus exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @POST
    @Path( ConstantsPluginServiceEndPoints.ADD_PLUGIN_API )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addPlugin( String zipFilePath );

    /**
     * Enable Plugin
     *
     * @param id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsPluginServiceEndPoints.ENABLE_PLUGIN_API )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response enablePlugin( @PathParam( ConstantsPlugins.PARAM_ID ) String id );

    /**
     * Start Plugin
     *
     * @param id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsPluginServiceEndPoints.START_PLUGIN_API )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response startPlugin( @PathParam( ConstantsPlugins.PARAM_ID ) String id );

    /**
     * Get List Of Plugins
     *
     * @return
     */
    @GET
    @Path( ConstantsPluginServiceEndPoints.GET_PLUGIN_LIST_API )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPluginList();

}
