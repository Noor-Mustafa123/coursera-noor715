package de.soco.software.simuspace.suscore.common.exceptions;

import org.apache.http.HttpStatus;

/**
 * This is the throwable custom Exception will be thrown if a json is not parse able on some object.
 *
 * @author M.Nasir.Farooq
 */
public class JsonSerializationException extends SusException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 6055160948558703873L;

    /**
     * Instantiates a new json serialization exception with a message only
     *
     * @param message
     *         the message
     */
    public JsonSerializationException( String message ) {
        super( message );
    }

    /**
     * Instantiates a new json serialization exception.
     *
     * @param e
     *         the e
     * @param clazz
     *         the clazz
     */
    public JsonSerializationException( Exception e, Class< ? > clazz ) {
        super( e, HttpStatus.SC_OK, clazz );
    }

    /**
     * Instantiates a new json serialization exception.
     *
     * @param e
     *         the e
     * @param statusCode
     *         the status code
     * @param clazz
     *         the clazz
     */
    public JsonSerializationException( final Exception e, final int statusCode, Class< ? > clazz ) {
        super( e, statusCode, clazz );

    }

    /**
     * Instantiates a new json serialization exception.
     *
     * @param exceptionMessage
     *         the exception message
     * @param ex
     *         the ex
     * @param clazz
     *         the clazz
     */
    public JsonSerializationException( String exceptionMessage, final Exception ex, Class< ? > clazz ) {
        super( exceptionMessage, ex, HttpStatus.SC_OK, clazz );

    }

    /**
     * Instantiates a new json serialization exception.
     *
     * @param message
     *         the message
     * @param ex
     *         the ex
     * @param statusCode
     *         the status code
     * @param clazz
     *         the clazz
     */

    public JsonSerializationException( final String message, final Exception ex, final int statusCode, Class< ? > clazz ) {
        super( message, ex, statusCode, clazz );
    }

    /**
     * Instantiates a new json serialization exception.
     *
     * @param exceptionDescription
     *         the exception description
     * @param className
     *         the class name
     * @param methodName
     *         the method name
     * @param ex
     *         the ex
     * @param clazz
     *         the clazz
     */
    public JsonSerializationException( final String exceptionDescription, final String className, final String methodName,
            final Exception ex, final Class< ? > clazz ) {
        super( exceptionDescription, className, methodName, HttpStatus.SC_OK, ex, clazz );

    }

}
