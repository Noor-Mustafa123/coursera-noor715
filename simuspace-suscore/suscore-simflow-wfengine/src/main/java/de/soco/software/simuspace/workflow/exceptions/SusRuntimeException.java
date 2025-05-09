package de.soco.software.simuspace.workflow.exceptions;

/**
 * This is the runtime exception will appear during workflow execution.
 */
public class SusRuntimeException extends RuntimeException {

    /**
     * Auto generated serial version id of class.
     */
    private static final long serialVersionUID = -373655091589229188L;

    /**
     * Instantiates a new sus expression exception.
     */
    public SusRuntimeException() {
        super();
    }

    /**
     * Instantiates a new sus expression exception.
     *
     * @param message
     *         the message
     */
    public SusRuntimeException( String message ) {
        super( message );
    }

}
