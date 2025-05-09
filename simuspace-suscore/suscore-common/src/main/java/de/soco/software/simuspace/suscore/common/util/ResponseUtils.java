/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.util;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;

/**
 * This class is responsible to prepare failure and success response for every HTTP request.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ResponseUtils {

    /**
     * The Constant DATA.
     */
    private static final String DATA = "data";

    /**
     * The Constant MESSAGE.
     */
    private static final String MESSAGE = "message";

    /**
     * The Constant WEBSERVICE_JSON_PARSING_ERROR.
     */
    private static final Object WEBSERVICE_JSON_PARSING_ERROR = "Unable to parse the json.";

    /**
     * The Constant WEBSERVICE_INTERNAL_SERVER_ERROR.
     */
    private static final String WEBSERVICE_INTERNAL_SERVER_ERROR = "Internal server error.";

    /**
     * Instantiates a new response utils.
     */
    private ResponseUtils() {

    }

    /**
     * Prepares the success response.
     *
     * @param dataToSend
     *         the data to send
     *
     * @return the response
     */
    public static Response success( Object dataToSend ) {
        final String[] notRequiredProps = { MESSAGE };
        try {
            return Response.status( HttpStatus.SC_OK )
                    .entity( JsonUtils.toFilteredJson( notRequiredProps, new SusResponseDTO( Boolean.TRUE, dataToSend ) ) ).build();

        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, JsonUtils.class );
        }

        return Response.status( HttpStatus.SC_INTERNAL_SERVER_ERROR ).entity( WEBSERVICE_JSON_PARSING_ERROR ).build();
    }

    /**
     * Success.
     *
     * @param message
     *         the message
     * @param dataToSend
     *         the data to send
     *
     * @return the response
     */
    public static Response success( String message, Object dataToSend ) {
        try {
            return Response.status( Response.Status.OK ).entity( JsonUtils.toFilteredJson( new String[]{},
                    new SusResponseDTO( Boolean.TRUE, new Message( Message.SUCCESS, message ), dataToSend ) ) ).build();
        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, ResponseUtils.class );
        }
        return Response.status( Response.Status.INTERNAL_SERVER_ERROR )
                .entity( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) ).build();
    }

    /**
     * Success.
     *
     * @param expire
     *         the expire
     * @param message
     *         the message
     * @param dataToSend
     *         the data to send
     *
     * @return the response
     */

    public static Response success( String message, Object dataToSend, boolean expire ) {
        try {
            return Response.status( Response.Status.OK ).entity( JsonUtils.toFilteredJson( new String[]{},
                    new SusResponseDTO( Boolean.TRUE, expire, new Message( Message.SUCCESS, message ), dataToSend ) ) ).build();
        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, ResponseUtils.class );
        }
        return Response.status( Response.Status.INTERNAL_SERVER_ERROR )
                .entity( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) ).build();
    }

    /**
     * Prepares the Failure response.
     *
     * @param httpStatus
     *         the http status
     * @param errorMessages
     *         the error messages
     *
     * @return the response
     */
    public static Response failure( final int httpStatus, final String errorMessages ) {

        final String[] notRequiredProps = { DATA };
        try {
            final String message = errorMessages == null ? WEBSERVICE_INTERNAL_SERVER_ERROR : errorMessages;

            return Response.status( httpStatus == 0 ? HttpStatus.SC_INTERNAL_SERVER_ERROR : httpStatus ).entity(
                    JsonUtils.toFilteredJson( notRequiredProps,
                            new SusResponseDTO( Boolean.FALSE, new Message( Message.ERROR, message ) ) ) ).build();
        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, JsonUtils.class );
        }

        return Response.status( HttpStatus.SC_INTERNAL_SERVER_ERROR ).entity( WEBSERVICE_JSON_PARSING_ERROR ).build();
    }

    /**
     * Prepares the Failure response.
     *
     * @param errorMessages
     *         the error messages
     *
     * @return the response
     */

    public static Response failure( final String errorMessages ) {
        final String[] notRequiredProps = { DATA };
        try {
            final String message = errorMessages == null ? WEBSERVICE_INTERNAL_SERVER_ERROR : errorMessages;
            return Response.status( HttpStatus.SC_OK ).entity( JsonUtils.toFilteredJson( notRequiredProps,
                    new SusResponseDTO( Boolean.FALSE, new Message( Message.ERROR, message ) ) ) ).build();
        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, JsonUtils.class );
        }

        return Response.status( HttpStatus.SC_INTERNAL_SERVER_ERROR ).entity( WEBSERVICE_JSON_PARSING_ERROR ).build();
    }

    /**
     * Failure.
     *
     * @param errorMessages
     *         the error messages
     * @param statusCode
     *         the status code
     *
     * @return the response
     */
    public static Response failure( final String errorMessages, final int statusCode ) {

        final String[] notRequiredProps = { DATA };
        try {
            final String message = errorMessages == null ? WEBSERVICE_INTERNAL_SERVER_ERROR : errorMessages;
            return Response.status( statusCode ).entity( JsonUtils.toFilteredJson( notRequiredProps,
                    new SusResponseDTO( Boolean.FALSE, new Message( Message.ERROR, message ) ) ) ).build();
        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, JsonUtils.class );
        }

        return Response.status( HttpStatus.SC_INTERNAL_SERVER_ERROR ).entity( WEBSERVICE_JSON_PARSING_ERROR ).build();
    }

    /**
     * This method will be used for spring Controller as Response builder
     *
     * @param message
     *         the message
     * @param dataToSend
     *         the data to send
     *
     * @return the response DTO
     */
    public static SusResponseDTO successResponse( String message, Object dataToSend ) {
        final Message msg = new Message( Message.SUCCESS, message );
        return new SusResponseDTO( Boolean.TRUE, msg, dataToSend );

    }

    /**
     * This method will be used for spring Controller as Response builder
     *
     * @param dataToSend
     *         the data to send
     *
     * @return the sus response DTO
     */
    public static SusResponseDTO successResponse( Object dataToSend ) {
        return new SusResponseDTO( Boolean.TRUE, null, dataToSend );

    }

    /**
     * Success info response sus response dto.
     *
     * @param message
     *         the message
     * @param dataToSend
     *         the data to send
     *
     * @return the sus response dto
     */
    public static SusResponseDTO successInfoResponse( String message, Object dataToSend ) {
        final Message msg = new Message( Message.INFO, message );
        return new SusResponseDTO( Boolean.FALSE, msg, dataToSend );

    }

    /**
     * This method will be used for spring Controller as Response builder.
     *
     * @param message
     *         the message
     * @param dataToSend
     *         the data to send
     *
     * @return the response DTO
     */
    public static SusResponseDTO failureResponse( String message, Object dataToSend ) {
        final Message msg = new Message( Message.ERROR, message );
        return new SusResponseDTO( Boolean.FALSE, msg, dataToSend );

    }

    /**
     * Info.
     *
     * @param message
     *         the message
     * @param dataToSend
     *         the data to send
     *
     * @return the response
     */
    public static Response info( String message, Object dataToSend ) {

        try {
            return Response.status( Response.Status.OK ).entity( JsonUtils.toFilteredJson( new String[]{},
                    new SusResponseDTO( Boolean.TRUE, new Message( Message.INFO, message ), dataToSend ) ) ).build();
        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, ResponseUtils.class );
        }
        return Response.status( Response.Status.INTERNAL_SERVER_ERROR )
                .entity( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) ).build();

    }

    /**
     * Prepares the success response.
     *
     * @param dataToSend
     *         the data to send
     * @param notRequiredProps
     *         the not required props
     *
     * @return the response
     */
    public static Response success( Object dataToSend, String[] notRequiredProps ) {

        try {
            return Response.status( HttpStatus.SC_OK )
                    .entity( JsonUtils.toFilteredJson( notRequiredProps, new SusResponseDTO( Boolean.TRUE, dataToSend ) ) ).build();
        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, JsonUtils.class );
        }

        return Response.status( HttpStatus.SC_INTERNAL_SERVER_ERROR )
                .entity( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ) ).build();
    }

    public static SusResponseDTO warning( String message, Object dataToSend ) {
        final Message msg = new Message( Message.WARNING, message );
        return new SusResponseDTO( Boolean.TRUE, msg, dataToSend );

    }

    /**
     * Prepares the success response with cookies.
     *
     * @param message the success message
     * @param expire the expire flag
     * @param cookies the array of cookies to be added to the response
     * @param data the optional data object to be included in the response
     * @return the response with cookies
     */
    public static Response successResponseWithCookies(String message, boolean expire, NewCookie[] cookies, Object data) {
        try {
            SusResponseDTO responseDTO = new SusResponseDTO(Boolean.TRUE, expire, new Message(Message.SUCCESS, message), data);
            Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK).entity(JsonUtils.toFilteredJson(new String[]{}, responseDTO));
            for (NewCookie cookie : cookies) {
                responseBuilder.cookie(cookie);
            }
            return responseBuilder.build();
        } catch (JsonSerializationException e) {
            ExceptionLogger.logException(e, ResponseUtils.class);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(MessageBundleFactory.getMessage(Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey()))
                    .build();
        }
    }

}