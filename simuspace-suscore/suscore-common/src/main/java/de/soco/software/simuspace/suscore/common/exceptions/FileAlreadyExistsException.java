package de.soco.software.simuspace.suscore.common.exceptions;

public class FileAlreadyExistsException extends SusException {

    private static final long serialVersionUID = -7742121057164396331L;

    /**
     * Creates a new FileAlreadyExistsException.
     */
    public FileAlreadyExistsException() {
        super();
    }

    /**
     * Constructs a new FileAlreadyExistsException.
     *
     * @param message
     *         the reason for the exception
     */
    public FileAlreadyExistsException( String message ) {
        super( message );
    }

    /**
     * Constructs a new FileAlreadyExistsException.
     *
     * @param cause
     *         the underlying Throwable that caused this exception to be thrown.
     */
    public FileAlreadyExistsException( Throwable cause ) {
        super( cause );
    }

    /**
     * Constructs a new FileAlreadyExistsException.
     *
     * @param message
     *         the reason for the exception
     * @param cause
     *         the underlying Throwable that caused this exception to be thrown.
     */
    public FileAlreadyExistsException( String message, Throwable cause ) {
        super( message, cause );
    }

}
