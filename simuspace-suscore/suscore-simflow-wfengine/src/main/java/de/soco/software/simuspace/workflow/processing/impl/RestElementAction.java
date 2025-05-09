package de.soco.software.simuspace.workflow.processing.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;

/**
 * This Class designed to make rest calls to simuspace (from V1.1 onwards) APIs using {@link SuSClient}.
 *
 * @author M.Nasir.Farooq
 */
public class RestElementAction {

    /**
     * The Constant BASE_URL.
     */
    private static final String BASE_URL = "/api/v1/";

    /**
     * This is the tree of all the element connections in a work flow to execute. {@link String} as Key {@link Object} as value
     */
    private final Map< String, Object > parameters;

    /**
     * The rest API server credentials.
     */
    private RestAPI restApi;

    /**
     * The constructor which sets different fields of object
     *
     * @param parameters
     *         the parameters
     */
    public RestElementAction( Map< String, Object > parameters ) {
        this.parameters = parameters;
    }

    /**
     * The doAction. This function is responsible for making rest calls here will create download link then download files.
     *
     * @return the notification
     */
    public Notification doAction() {
        final Notification notif = new Notification();

        for ( final Entry< String, Object > param : parameters.entrySet() ) {

            final Map< String, String > requestInput = ( HashMap< String, String > ) param.getValue();

            try {

                final List< Error > errors = downloadVaultFile( requestInput.get( ConstantRequestHeader.FILE ),
                        requestInput.get( ConstantRequestHeader.URL ) );

                notif.addErrors( errors );
            } catch ( final SusException e ) {
                notif.addError( new Error( e.getMessage() ) );
            }

        }

        return notif;
    }

    /**
     * preparing url and download vault file.
     *
     * @param vaultFile
     *         the vault file
     * @param api
     *         the url of vault file
     *
     * @return the list the list of errors if APIs response fails
     *
     * @throws SusException
     *         the simuspace exception
     */
    private List< Error > downloadVaultFile( String vaultFile, String api ) {
        final List< Error > errors = new ArrayList<>();

        final String vaultFileLink = prepareURL( api );
        final SusResponseDTO susResponseDTO = SuSClient.getRequest( vaultFileLink, prepareHeaders() );

        if ( !susResponseDTO.getSuccess() ) {
            errors.add( new Error( susResponseDTO.getMessage().getContent() ) );
        } else {

            final String downloadLink = JsonUtils.linkedMapObjectToClassObject( susResponseDTO.getData(), HashMap.class )
                    .get( ConstantRequestHeader.URL ).toString();

            SuSClient.downloadRequest( prepareURL( ConstantsString.EMPTY_STRING ) + BASE_URL + downloadLink, vaultFile, prepareHeaders(),
                    errors );
        }
        return errors;
    }

    /**
     * It adds headers for required for communication with server.<br>
     *
     * @return requestHeaders
     */
    private Map< String, String > prepareHeaders() {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, restApi.getRequestHeaders().getToken() );
        requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, restApi.getRequestHeaders().getJobAuthToken() );

        return requestHeaders;

    }

    /**
     * It prepares url by getting protocol : hostname and port<br> from server appended with url provided.
     *
     * @param api
     *         , string of API
     *
     * @return url complete url to request an api
     */
    private String prepareURL( String api ) {
        if ( restApi != null ) {
            return restApi.getProtocol() + restApi.getHostname() + ConstantsString.COLON + restApi.getPort() + api;
        }

        return ConstantsString.EMPTY_STRING;

    }

    /**
     * Sets the rest API.
     *
     * @param restApi
     *         the new rest API
     */
    public void setRestAPI( RestAPI restApi ) {
        this.restApi = restApi;
    }

}
