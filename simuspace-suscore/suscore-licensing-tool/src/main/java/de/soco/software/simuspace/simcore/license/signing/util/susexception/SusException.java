package de.soco.software.simuspace.simcore.license.signing.util.susexception;

import org.apache.http.HttpStatus;

/**
 * This is the base class for all self-created exceptions in the SuS project.
 *
 * @author Nosheen.Sharif
 */
public class SusException extends RuntimeException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2332825345712702639L;

    /**
     * The message.
     */
    private String message;

    /**
     * The status code.
     */
    protected int statusCode = HttpStatus.SC_OK;

    /**
     * Instantiates a new SusException exception.
     */
    public SusException() {
        super();
    }

    /**
     * Instantiates a new sus exception.
     *
     * @param e
     *         the e
     * @param clazz
     *         the clazz
     */
    public SusException( final Exception e, Class< ? > clazz ) {
        super( e );
        setMessage( e.getMessage() );

    }

    /**
     * The Constructor.
     *
     * @param e
     *         the e
     * @param statusCode
     *         the status code
     * @param clazz
     *         the clazz
     */
    public SusException( final Exception e, final int statusCode, Class< ? > clazz ) {
        super( e );
        this.statusCode = statusCode;
        setMessage( e.getMessage() );

    }

    /**
     * Instantiates a new SusException exception.
     *
     * @param message
     *         the message
     */
    public SusException( String message ) {
        super( message );
        setMessage( message );
    }

    /**
     * Instantiates a new simcore exception.
     *
     * @param message
     *         the message
     * @param e
     *         the e
     * @param clazz
     *         the clazz
     */
    public SusException( final String message, final Exception e, Class< ? > clazz ) {
        super( e );
        setMessage( message );

    }

    /**
     * Instantiates a new simcore exception.
     *
     * @param message
     *         the message
     * @param e
     *         the e
     * @param statusCode
     *         the status code
     * @param clazz
     *         the clazz
     */
    public SusException( final String message, final Exception e, final int statusCode, Class< ? > clazz ) {
        super( e );
        this.statusCode = statusCode;
        setMessage( message );
    }

    /**
     * Instantiates a new simcore exception.
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
    public SusException( final String exceptionDescription, final String className, final String methodName, final Exception ex,
            final Class< ? > clazz ) {
        super( ex );
        setMessage( exceptionDescription );
    }

    /**
     * Instantiates a new simcore exception.
     *
     * @param exceptionDescription
     *         the exception description
     * @param className
     *         the class name
     * @param methodName
     *         the method name
     * @param statusCode
     *         the status code
     * @param ex
     *         the ex
     * @param clazz
     *         the clazz
     */
    public SusException( final String exceptionDescription, final String className, final String methodName, final int statusCode,
            final Exception ex, final Class< ? > clazz ) {
        super( ex );
        this.statusCode = statusCode;
        setMessage( exceptionDescription );
    }

    /**
     * Instantiates a new SusException exception.
     *
     * @param message
     *         the message
     * @param cause
     *         the cause
     */
    public SusException( String message, Throwable cause ) {
        super( message, cause );
    }

    /**
     * Instantiates a new SusException exception.
     *
     * @param cause
     *         the cause
     */
    public SusException( Throwable cause ) {
        super( cause );
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Gets the status code of exceptions.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the message for the exception.
     *
     * @param message
     *         the new message
     */
    public void setMessage( String message ) {
        this.message = message;
    }

}
