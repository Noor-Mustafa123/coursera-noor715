package de.soco.software.simuspace.suscore.common.exceptions;

/**
 * Feature not allowed exception
 *
 * @author Zeeshan jamal
 */
public class FeatureNotAllowedException extends SusException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new FeatureNotAllowedException.
     */
    public FeatureNotAllowedException() {
        super();
    }

    /**
     * Constructs a new FeatureNotAllowedException.
     *
     * @param message
     *         the reason for the exception
     */
    public FeatureNotAllowedException( String message ) {
        super( message );
    }

    /**
     * Constructs a new FeatureNotAllowedException.
     *
     * @param cause
     *         the underlying Throwable that caused this exception to be thrown.
     */
    public FeatureNotAllowedException( Throwable cause ) {
        super( cause );
    }

    /**
     * Constructs a new FeatureNotAllowedException.
     *
     * @param message
     *         the reason for the exception
     * @param cause
     *         the underlying Throwable that caused this exception to be thrown.
     */
    public FeatureNotAllowedException( String message, Throwable cause ) {
        super( message, cause );
    }

}
