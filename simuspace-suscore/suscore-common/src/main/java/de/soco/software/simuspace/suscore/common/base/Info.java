package de.soco.software.simuspace.suscore.common.base;

/**
 * This class contains error message of errors which occur during Run Time
 *
 * @author M.Nasir.Farooq
 */
public class Info {

    /**
     * The error message
     */
    private String message;

    /**
     * Instantiates a new info.
     */
    public Info() {
        super();
    }

    /**
     * The constructor instantiates an object of Error with the error message
     *
     * @param message
     *         The error message
     */
    public Info( String message ) {
        super();
        this.setMessage( message );
    }

    /**
     * Gets the Error message
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

}
