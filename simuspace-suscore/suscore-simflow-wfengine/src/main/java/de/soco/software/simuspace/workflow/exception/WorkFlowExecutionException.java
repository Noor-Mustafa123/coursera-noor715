package de.soco.software.simuspace.workflow.exception;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;

/**
 * This exception is thrown when a problem occurs regarding the execution of a workflow.
 */

public class WorkFlowExecutionException extends SusException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new work flow execution exception.
     */
    public WorkFlowExecutionException() {
        super();
    }

    /**
     * Instantiates a new work flow execution exception with a message.
     *
     * @param message
     *         the message
     */
    public WorkFlowExecutionException( String message ) {
        super( message );
    }

    /**
     * Instantiates a new work flow execution exception with a message and throwable cause.
     *
     * @param message
     *         the message
     * @param cause
     *         the cause
     */
    public WorkFlowExecutionException( String message, Throwable cause ) {
        super( message, cause );
    }

    /**
     * Instantiates a new work flow execution exception with a throwable cause.
     *
     * @param cause
     *         the cause
     */
    public WorkFlowExecutionException( Throwable cause ) {
        super( cause );
    }

    /**
     * Instantiates a new work flow execution exception with a message including workflow object name and throwable cause.
     *
     * @param userWorkFlow
     *         the user work flow
     * @param message
     *         the message
     * @param cause
     *         the cause
     */
    public WorkFlowExecutionException( UserWorkFlow userWorkFlow, String message, Throwable cause ) {
        super( userWorkFlow.getName() + ConstantsString.COLON + message, cause );
    }

}
