package de.soco.software.simuspace.workflow.exception;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;

/**
 * A model exception is thrown when the data model is inconsistent.
 */
public class ModelException extends SusException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new model exception.
     */
    public ModelException() {
        super();
    }

    /**
     * Instantiates a new model exception with a message including model object and throwable cause.
     *
     * @param modelObject
     *         the model object
     * @param message
     *         the message
     * @param cause
     *         the cause
     */
    public ModelException( Object modelObject, String message, Throwable cause ) {
        super( modelObject + ConstantsString.COLON + message, cause );
    }

    /**
     * Instantiates a new model exception with the message.
     *
     * @param message
     *         the message
     */
    public ModelException( String message ) {
        super( message );
    }

    /**
     * Instantiates a new model exception with a message and throwable cause.
     *
     * @param message
     *         the message
     * @param cause
     *         the cause
     */
    public ModelException( String message, Throwable cause ) {
        super( message, cause );
    }

    /**
     * Instantiates a new model exception with a throwable cause.
     *
     * @param cause
     *         the cause
     */
    public ModelException( Throwable cause ) {
        super( cause );
    }

}
