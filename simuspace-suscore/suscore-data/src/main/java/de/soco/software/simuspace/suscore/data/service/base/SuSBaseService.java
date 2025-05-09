package de.soco.software.simuspace.suscore.data.service.base;

import javax.ws.rs.core.Response;

import org.apache.cxf.phase.PhaseInterceptorChain;

import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.InternalExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;

/**
 * * Base class for Service for thread pools executor to get user information.
 *
 * @author Shan Arshad
 */
public class SuSBaseService {

    /**
     * Instantiates a new sus base service.
     */
    public SuSBaseService() {
    }

    /**
     * Instantiates a new su S base service.
     *
     * @param serviceClass
     *         the service class
     */
    public SuSBaseService( Class< ? > serviceClass ) {
    }

    /**
     * Gets the user id from general header.
     *
     * @return the user id from general header
     */
    protected String getUserIdFromGeneralHeader() {
        return BundleUtils.getUserIdFromMessage( PhaseInterceptorChain.getCurrentMessage() );
    }

    /**
     * Gets the user name from general header.
     *
     * @return the user name from general header
     */
    protected String getUserNameFromGeneralHeader() {
        return BundleUtils.getUserNameFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() );
    }

    /**
     * Gets the browser agent from general header.
     *
     * @return the browser agent from general header
     */
    protected String getBrowserAgentFromGeneralHeader() {
        return BundleUtils.getBrowserAgent( PhaseInterceptorChain.getCurrentMessage() );
    }

    /**
     * Gets the token from general header.
     *
     * @return the token from general header
     */
    protected String getTokenFromGeneralHeader() {
        return BundleUtils.getTokenFromMessage( PhaseInterceptorChain.getCurrentMessage() );
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
    protected Response handleException( Exception exception ) {

        if ( exception instanceof SusException susException ) {
            final String userName = BundleUtils.getUserNameFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() );
            ExceptionLogger.logException( userName, susException, getClass() );
            return ResponseUtils.failure( susException.getStatusCode(), exception.getMessage() );
        }
        if ( exception instanceof SusDataBaseException susDataBaseException ) {
            final String userName = BundleUtils.getUserNameFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() );
            ExceptionLogger.logException( userName, susDataBaseException, getClass() );
            return ResponseUtils.failure( susDataBaseException.getStatusCode(),
                    MessageBundleFactory.getMessage( Messages.DB_DATABASE_QUERY_ERROR.getKey() ) );
        } else {
            InternalExceptionLogger.logException( exception, getClass() );
            return ResponseUtils.failure( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    MessageBundleFactory.getMessage( Messages.WEBSERVICE_INTERNAL_SERVER_ERROR.getKey() ) );

        }
    }

    /**
     * Prepare object view DTO.
     *
     * @param objectId
     *         the object id
     * @param viewJson
     *         the view json
     * @param viewKey
     *         the view key
     * @param isUpdateable
     *         the is updateable
     * @param viewId
     *         the view id
     *
     * @return the object view DTO
     */
    protected ObjectViewDTO prepareObjectViewDTO( String objectId, String viewJson, String viewKey, boolean isUpdateable, String viewId ) {
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
        if ( isUpdateable ) {
            objectViewDTO.setId( viewId );
        } else if ( objectViewDTO.isDefaultView() ) {
            objectViewDTO.setId( objectViewDTO.getId() );
        } else {
            objectViewDTO.setId( null );
        }
        objectViewDTO.setObjectId( objectId );
        objectViewDTO.setObjectViewKey( viewKey );
        objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
        objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
        return objectViewDTO;
    }

}
