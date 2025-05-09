package de.soco.software.simuspace.suscore.common.http.model;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

/**
 * The Class TransferInfo. Responsible for transferring related stuff
 */
public class TransferInfo {

    /**
     * The progess.
     */
    private long progess;

    /**
     * The action.
     */
    private String action;

    /**
     * The file full adress.
     */
    private String fileFullAdress;

    /**
     * The httpPost.
     */
    HttpPost httpPost = null;

    /**
     * The httpGet.
     */
    HttpGet httpGet = null;

    public TransferInfo() {
    }

    /**
     * Gets the progess.
     *
     * @return the progess
     */
    public long getProgess() {
        return progess;
    }

    /**
     * Sets the progess.
     *
     * @param progess
     *         the new progess
     */
    public void setProgess( long progess ) {
        this.progess = progess;
    }

    /**
     * Gets the file full adress.
     *
     * @return the file full adress
     */
    public String getFileFullAdress() {
        return fileFullAdress;
    }

    /**
     * Sets the file full adress.
     *
     * @param fileFullAdress
     *         the new file full adress
     */
    public void setFileFullAdress( String fileFullAdress ) {
        this.fileFullAdress = fileFullAdress;
    }

    /**
     * Gets the action.
     *
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action.
     *
     * @param action
     *         the new action
     */
    public void setAction( String action ) {
        this.action = action;
    }

    /**
     * Instantiates a new transfer info.
     *
     * @param progess
     *         the progess
     * @param action
     *         the action
     */
    public TransferInfo( long progess, String action ) {
        super();
        this.progess = progess;
        this.action = action;
    }

    /**
     * Instantiates a new transfer info.
     *
     * @param progess
     *         the progess
     * @param action
     *         the action
     * @param fileFullAdress
     *         the file full adress
     */
    public TransferInfo( long progess, String action, String fileFullAdress ) {
        super();
        this.progess = progess;
        this.action = action;
        this.fileFullAdress = fileFullAdress;
    }

    /**
     * Gets the httpPost.
     *
     * @return the httpPost
     */
    public HttpPost getHttpPost() {
        return httpPost;
    }

    /**
     * Sets the httpPost.
     *
     * @param httpPost
     *         the new httpPost
     */
    public void setHttpPost( HttpPost httpPost ) {
        this.httpPost = httpPost;
    }

    /**
     * Gets the httpGet.
     *
     * @return the httpGet
     */
    public HttpGet getHttpGet() {
        return httpGet;
    }

    /**
     * Sets the httpGet.
     *
     * @param httpGet
     *         the new httpGet
     */
    public void setHttpGet( HttpGet httpGet ) {
        this.httpGet = httpGet;
    }

}