/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/
/*
 *
 */

package de.soco.software.simuspace.suscore.common.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class UserTokenDTO.
 *
 * @author Shan Arshad
 * @since 1.0
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class JobTokenDTO {

    /**
     * The browser agent.
     */
    private String browserAgent;

    /**
     * The created on.
     */
    private Date createdOn;

    /**
     * The expired.
     */
    private Boolean expired;

    /**
     * The id.
     */
    private String id;

    /**
     * The ip address.
     */
    private String ipAddress;

    /**
     * The key.
     */
    private String key;

    /**
     * The token.
     */
    private String token;

    /**
     * The user id.
     */
    private String jobId;

    /**
     * The user id.
     */
    private String userId;

    /**
     * The user name.
     */
    private String userName;

    /**
     * The auth token.
     */
    private String authToken;

    /**
     * Instantiates a new user token dto.
     */
    public JobTokenDTO() {
    }

    /**
     * Instantiates a new user token dto.
     *
     * @param jobId
     *         the job id
     * @param key
     *         the key
     */
    public JobTokenDTO( String jobId, String key ) {
        this.jobId = jobId;
        this.key = key;
    }

    /**
     * Gets the auth token.
     *
     * @return the auth token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken
     *         the new auth token
     */
    public void setAuthToken( String authToken ) {
        this.authToken = authToken;
    }

    /**
     * Instantiates a new user token dto.
     *
     * @param jobId
     *         the job id
     * @param browserAgent
     *         the browser agent
     * @param ipAddress
     *         the ip address
     */
    public JobTokenDTO( final String jobId, final String browserAgent, final String ipAddress ) {
        this.browserAgent = browserAgent;
        this.ipAddress = ipAddress;
        this.jobId = jobId;
    }

    /**
     * Instantiates a new user token dto.
     *
     * @param id
     *         the id
     * @param jobId
     *         the job id
     * @param token
     *         the token
     * @param ipAddress
     *         the ip address
     * @param browserAgent
     *         the browser agent
     * @param expiryTime
     *         the expiry time
     * @param expired
     *         the expired
     * @param createdOn
     *         the created on
     * @param lastRequestTime
     *         the last request time
     * @param key
     *         the key
     */
    public JobTokenDTO( final String id, final String jobId, final String token, final String ipAddress, final String browserAgent,
            final Boolean expired, final Date createdOn, final String key ) {
        super();
        this.id = id;
        this.jobId = jobId;
        this.token = token;
        this.createdOn = createdOn;
        this.expired = expired;
        this.browserAgent = browserAgent;
        this.ipAddress = ipAddress;
        this.key = key;
    }

    /**
     * Instantiates a new user token dto.
     *
     * @param jobId
     *         the user id
     * @param token
     *         the token
     * @param ipAddress
     *         the ip address
     * @param browserAgent
     *         the browser agent
     * @param expiryTime
     *         the expiry time
     * @param expired
     *         the expired
     * @param createdOn
     *         the created on
     * @param lastRequestTime
     *         the last request time
     */
    public JobTokenDTO( final String jobId, final String token, final String ipAddress, final String browserAgent,
            final Boolean expired, final Date createdOn ) {
        super();
        this.jobId = jobId;
        this.token = token;
        this.createdOn = createdOn;
        this.expired = expired;
        this.browserAgent = browserAgent;
        this.ipAddress = ipAddress;
    }

    /**
     * Instantiates a new user token dto.
     *
     * @param jobId
     *         the user id
     * @param token
     *         the token
     * @param ipAddress
     *         the ip address
     * @param browserAgent
     *         the browser agent
     * @param expiryTime
     *         the expiry time
     * @param expired
     *         the expired
     * @param key
     *         the key
     */
    public JobTokenDTO( final String jobId, final String token, final String ipAddress, final String browserAgent,
            final Boolean expired, final String key ) {
        super();
        this.jobId = jobId;
        this.token = token;
        this.expired = expired;
        this.browserAgent = browserAgent;
        this.ipAddress = ipAddress;
        this.setKey( key );
    }

    /**
     * Instantiates a new user token dto.
     *
     * @param token
     *         the token
     */
    public JobTokenDTO( final String token ) {
        this.token = token;
    }

    /**
     * Instantiates a new user token dto.
     *
     * @param token
     *         the token
     * @param expired
     *         the expired
     */
    public JobTokenDTO( String token, boolean expired ) {
        this.token = token;
        this.expired = expired;
    }

    /**
     * Gets the browser agent.
     *
     * @return the browserAgent
     */
    public String getBrowserAgent() {
        return browserAgent;
    }

    /**
     * Gets the created on.
     *
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Gets the expired.
     *
     * @return the expired
     */
    public Boolean getExpired() {
        return expired;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the ip address.
     *
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
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
     * Checks if is expired.
     *
     * @return the expired
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * Sets the browser agent.
     *
     * @param browserAgent
     *         the new browser agent
     */
    public void setBrowserAgent( String browserAgent ) {
        this.browserAgent = browserAgent;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Sets the expired.
     *
     * @param expired
     *         the new expired
     */
    public void setExpired( boolean expired ) {
        this.expired = expired;
    }

    /**
     * Sets the expired.
     *
     * @param expired
     *         the new expired
     */
    public void setExpired( Boolean expired ) {
        this.expired = expired;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Sets the ip address.
     *
     * @param ipAddress
     *         the new ip address
     */
    public void setIpAddress( String ipAddress ) {
        this.ipAddress = ipAddress;
    }

    /**
     * Sets the key.
     *
     * @param key
     *         the new key
     */
    public void setKey( String key ) {
        this.key = key;
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
     * Gets the job id.
     *
     * @return the job id
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Sets the job id.
     *
     * @param jobId
     *         the new job id
     */
    public void setJobId( String jobId ) {
        this.jobId = jobId;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId
     *         the new user id
     */
    public void setUserId( String userId ) {
        this.userId = userId;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName
     *         the new user name
     */
    public void setUserName( String userName ) {
        this.userName = userName;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "JobTokenDTO [browserAgent=" + browserAgent + ", createdOn=" + createdOn + ", expired=" + expired + ", id=" + id
                + ", ipAddress=" + ipAddress + ", key=" + key + ", token=" + token + ", jobId=" + jobId + ", userId=" + userId
                + ", userName="
                + userName + "]";
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        return result;
    }

    /**
     * Equals.
     *
     * @param obj
     *         the obj
     *
     * @return true, if successful
     */
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        JobTokenDTO other = ( JobTokenDTO ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        return true;
    }

}
