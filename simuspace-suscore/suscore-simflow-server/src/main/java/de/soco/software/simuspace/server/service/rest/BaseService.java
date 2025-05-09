/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.server.service.rest;

import javax.ws.rs.core.Response;

import java.util.UUID;

import org.apache.cxf.phase.PhaseInterceptorChain;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.InternalExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;

/**
 * Base class for Service
 *
 * @author Huzaifah
 * @since 1.0
 */
public class BaseService {

    /**
     * Instantiates a new base service.
     */
    public BaseService() {
        super();
    }

    /**
     * Gets the user id from general header.
     *
     * @return the user id from general header
     */
    protected UUID getUserIdFromGeneralHeader() {

        final String id = BundleUtils.getUserIdFromMessage( PhaseInterceptorChain.getCurrentMessage() );

        return !"-1".equals( id ) ? UUID.fromString( id ) : null;
    }

    /**
     * Gets the user id string from general header.
     *
     * @return the user id string from general header
     */
    protected String getUserIdStringFromGeneralHeader() {

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

    protected String getUserTokenGeneralHeader() {
        return BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() );
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

}
