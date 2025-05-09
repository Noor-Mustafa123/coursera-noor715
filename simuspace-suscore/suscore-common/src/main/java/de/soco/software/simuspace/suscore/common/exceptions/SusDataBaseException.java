package de.soco.software.simuspace.suscore.common.exceptions;

import org.apache.http.HttpStatus;

/**
 * This Class wraps the DataBase Exceptions in the SuS project
 */
public class SusDataBaseException extends RuntimeException {

    /**
     * Auto generated serial version id of class.
     */
    private static final long serialVersionUID = -373655091589229188L;

    /**
     * The status code of http.
     */
    protected int statusCode = HttpStatus.SC_OK;

    /**
     * Instantiates a new sus expression exception.
     */
    public SusDataBaseException() {
        super();
    }

    /**
     * Instantiates a new sus expression exception.
     *
     * @param message
     *         the message
     */
    public SusDataBaseException( String message ) {
        super( message );
    }

    /**
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode( int statusCode ) {
        this.statusCode = statusCode;
    }

}
