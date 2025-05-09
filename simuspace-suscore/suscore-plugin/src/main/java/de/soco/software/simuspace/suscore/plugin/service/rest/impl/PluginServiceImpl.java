/**
 *
 */

package de.soco.software.simuspace.suscore.plugin.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;

import org.apache.cxf.phase.PhaseInterceptorChain;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.InternalExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.plugin.constants.ConstantsPlugins;
import de.soco.software.simuspace.suscore.plugin.dto.PluginDTO;
import de.soco.software.simuspace.suscore.plugin.manager.PluginManager;
import de.soco.software.simuspace.suscore.plugin.service.rest.PluginService;

/**
 * Implementation Class of Plugin Service Interface . Responsible for Preparing and Parsing Json request and reponse
 *
 * @author Nosheen.Sharif
 */
public class PluginServiceImpl implements PluginService {

    /**
     * Plugin Manager refernece
     */
    private PluginManager manager;

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.service.rest.PluginService#addPlugin(java.lang.String)
     */
    @Override
    public Response addPlugin( String zipFilePath ) {
        Response response;
        try {

            HashMap< String, String > map = ( HashMap< String, String > ) JsonUtils.jsonToMap( zipFilePath, new HashMap<>() );

            PluginDTO returnPlugin = manager.addPlugin( map.get( ConstantsPlugins.PATH ) );
            response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PLUGIN_ADDED_SUCCESSFULLY.getKey() ),
                    returnPlugin );

        } catch ( Exception e ) {
            return handleException( e, getClass() );
        }

        return response;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.service.rest.PluginService#enablePlugin(java.lang.String)
     */
    @Override
    public Response enablePlugin( String id ) {
        Response response;
        try {

            PluginDTO returnPlugin = manager.enablePlugin( new PluginDTO( id ) );
            response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PLUGIN_ENABLE_SUCCESSFULLY.getKey() ),
                    returnPlugin );

        } catch ( Exception e ) {
            return handleException( e, getClass() );
        }

        return response;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.service.rest.PluginService#startPlugin(java.lang.String)
     */
    @Override
    public Response startPlugin( String id ) {
        Response response;
        try {

            boolean isPluginActive = manager.startPlugin( new PluginDTO( id ) );
            if ( isPluginActive ) {
                response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PLUGIN_STARTED_SUCCESSFULLY.getKey() ),
                        isPluginActive );
            } else {
                response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PLUGIN_FAILED_TO_START.getKey() ),
                        isPluginActive );
            }

        } catch ( Exception e ) {
            return handleException( e, getClass() );
        }

        return response;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.service.rest.PluginService#getPluginList()
     */
    @Override
    public Response getPluginList() {
        Response response;
        try {

            List< PluginDTO > list = manager.getPluginList();

            response = ResponseUtils.success( list );

        } catch ( Exception e ) {
            return handleException( e, getClass() );
        }

        return response;
    }

    /**
     * Handle exception.
     *
     * @param exception
     *         the e
     * @param Class
     *         clazz
     *
     * @return the response
     */
    protected Response handleException( Exception exception, Class clazz ) {

        if ( exception instanceof SusException susException ) {
            final String userName = BundleUtils.getUserNameFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() );
            ExceptionLogger.logException( userName, susException, clazz );
            return ResponseUtils.failure( susException.getStatusCode(), susException.getMessage() );
        }
        if ( exception instanceof SusDataBaseException susDataBaseException ) {
            final String userName = BundleUtils.getUserNameFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() );
            ExceptionLogger.logException( userName, susDataBaseException, clazz );
            return ResponseUtils.failure( susDataBaseException.getStatusCode(),
                    MessageBundleFactory.getMessage( Messages.DB_DATABASE_QUERY_ERROR.getKey() ) );
        } else {
            InternalExceptionLogger.logException( exception, clazz );
            return ResponseUtils.failure( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    MessageBundleFactory.getMessage( Messages.WEBSERVICE_INTERNAL_SERVER_ERROR.getKey() ) );

        }
    }

    /**
     * Gets manager.
     *
     * @return manager
     */
    public PluginManager getManager() {
        return manager;
    }

    /**
     * Sets manager.
     *
     * @param manager
     *         the manager
     */
    public void setManager( PluginManager manager ) {
        this.manager = manager;
    }

}
