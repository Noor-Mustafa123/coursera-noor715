package de.soco.software.simuspace.workflow.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is responsible for the headers of a request.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class RequestHeaders {

    /**
     * The token.
     */
    private String token;

    private String jobAuthToken;

    /**
     * The user agent.
     */
    private String userAgent;

    /**
     * Instantiates a new request headers.
     */
    public RequestHeaders() {
        super();
    }

    /**
     * Instantiates a new request headers.
     *
     * @param token
     *         the token
     * @param userAgent
     *         the user agent
     */
    public RequestHeaders( String token, String userAgent ) {
        this();
        this.token = token;
        this.userAgent = userAgent;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets the user agent.
     *
     * @return the user agent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets the token.
     *
     * @param token
     *         the new token
     */
    public void setToken( String token ) {
        this.token = token;
    }

    /**
     * Sets the user agent.
     *
     * @param userAgent
     *         the new user agent
     */
    public void setUserAgent( String userAgent ) {
        this.userAgent = userAgent;
    }

    public String getJobAuthToken() {
        return jobAuthToken;
    }

    public void setJobAuthToken( String jobAuthToken ) {
        this.jobAuthToken = jobAuthToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "RequestHeaders [token='" + token + "', userAgent='" + userAgent + "']";
    }

}
