package de.soco.software.simuspace.workflow.exception;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;

/**
 * This Class contains exceptions thrown in parsing of a workflow
 *
 * @author M.Nasir.Farooq
 */
public class UserWFParseException extends SusException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new user WF parse exception.
     */
    public UserWFParseException() {
        super();
    }

    /**
     * Instantiates a new user WF parse exception with a message.
     *
     * @param message
     *         the message
     */
    public UserWFParseException( String message ) {
        super( message );
    }

    /**
     * Instantiates a new user WF parse exception with a message and throwable cause.
     *
     * @param message
     *         the message
     * @param cause
     *         the cause
     */
    public UserWFParseException( String message, Throwable cause ) {
        super( message, cause );
    }

    /**
     * Instantiates a new user WF parse exception with a throwable cause.
     *
     * @param cause
     *         the cause
     */
    public UserWFParseException( Throwable cause ) {
        super( cause );
    }

    /**
     * Instantiates a new user WF parse exception with a message including workflow file name and throwable cause.
     *
     * @param userWorkFlow
     *         the user work flow
     * @param message
     *         the message
     * @param cause
     *         the cause
     */
    public UserWFParseException( UserWorkFlow userWorkFlow, String message, Throwable cause ) {
        super( userWorkFlow.getName() + ConstantsString.COLON + message, cause );
    }

}
