package de.soco.software.simuspace.suscore.common.base;

/**
 * This class contains error message of errors which occur during Run Time
 *
 * @author Aroosa.Bukhari
 */
public class Error {

    /**
     * The error message
     */
    private String message;

    /**
     * Instantiates a new error.
     */
    public Error() {
        super();
    }

    /**
     * The constructor instantiates an object of Error with the error message
     *
     * @param message
     *         The error message
     */
    public Error( String message ) {
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

    @Override
    public String toString() {
        return getMessage();
    }

    public void setMessage( String message ) {
        this.message = message;
    }

}
