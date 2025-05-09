package de.soco.software.simuspace.workflow.exceptions;

/**
 * The Class is custom SusExpressionException. Will be thrown when expression is not parse able or invalid according to SimuSpace Expression
 * Language.
 */
public class SusExpressionException extends RuntimeException {

    /**
     * Auto generated serial version id of class
     */
    private static final long serialVersionUID = -373655091589229188L;

    /**
     * Instantiates a new sus expression exception.
     */
    public SusExpressionException() {
        super();
    }

    /**
     * Instantiates a new sus expression exception.
     *
     * @param message
     *         the message
     */
    public SusExpressionException( String message ) {
        super( message );
    }

}
