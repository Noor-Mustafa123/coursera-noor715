package de.soco.software.simuspace.workflow.exception;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.workflow.model.ElementConfig;

/**
 * This Class contains exceptions that are thrown in parsing of a workflow configuration
 */
public class ConfigException extends SusException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -133158204918458870L;

    /**
     * Instantiates a new config exception.
     */
    public ConfigException() {
        super();
    }

    /**
     * Instantiates a new config exception with a message including configuration file key and throwable cause.
     *
     * @param elementConfig
     *         the element config
     * @param message
     *         the message
     * @param cause
     *         the cause
     */
    public ConfigException( ElementConfig elementConfig, String message, Throwable cause ) {
        super( elementConfig.getKey() + ConstantsString.COLON + message, cause );
    }

    /**
     * Instantiates a new config exception with a message.
     *
     * @param message
     *         the message
     */
    public ConfigException( String message ) {
        super( message );
    }

    /**
     * Instantiates a new config exception with a message and throwable cause.
     *
     * @param message
     *         the message
     * @param cause
     *         the cause
     */
    public ConfigException( String message, Throwable cause ) {
        super( message, cause );
    }

    /**
     * Instantiates a new config exception with a throwable cause.
     *
     * @param cause
     *         the cause
     */
    public ConfigException( Throwable cause ) {
        super( cause );
    }

}
