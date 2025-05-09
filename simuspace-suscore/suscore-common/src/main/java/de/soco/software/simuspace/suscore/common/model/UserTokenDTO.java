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

import java.io.Serializable;
import java.util.Date;

/**
 * The Class UserTokenDTO.
 *
 * @author Shan Arshad
 * @since 1.0
 */
public class UserTokenDTO implements Serializable {

    /**
     * The key.
     */
    private String key;

    /**
     * The id.
     */
    private String id;

    /**
     * The user id.
     */
    @UIColumn( data = "userId", name = "userId", filter = "hidden", renderer = "hidden", title = "3000052x4", isShow = false, orderNum = 1 )
    private String userId;

    /**
     * The user name.
     */
    @UIColumn( data = "userName", name = "userEntity.userUid", filter = "", renderer = "text", title = "3000054x4", orderNum = 2, isSortable = false )
    private String userName;

    @UIColumn( data = "module", name = "module", filter = "", renderer = "html", title = "3000030x4", orderNum = 3, isSortable = false )
    private String module;

    /**
     * The token.
     */
    @UIColumn( data = "token", name = "token", filter = "text", renderer = "hidden", title = "3000118x4", isShow = false, orderNum = 4 )
    private String token;

    /**
     * The created on.
     */
    @UIColumn( data = "createdOn", name = "createdOn", filter = "dateRange", renderer = "date", title = "3000008x4", orderNum = 5 )
    private Date createdOn;

    /**
     * The expiry time.
     */
    @UIColumn( data = "expiryTime", name = "expiryTime", filter = "dateRange", renderer = "date", title = "9900000x4", orderNum = 6 )
    private Long expiryTime;

    /**
     * The last request time.
     */
    @UIColumn( data = "lastRequestTime", name = "lastRequestTime", filter = "dateRange", renderer = "date", title = "9900001x4", orderNum = 7 )
    private Date lastRequestTime;

    /**
     * The expired.
     */
    @UIColumn( data = "expired", name = "expired", filter = "text", renderer = "hidden", title = "9900002x4", isShow = false, orderNum = 9 )
    private boolean expired;

    /**
     * The ip address.
     */
    @UIColumn( data = "ipAddress", name = "ipAddress", filter = "text", renderer = "text", title = "9900003x4", orderNum = 10 )
    private String ipAddress;

    /**
     * The browser agent.
     */
    @UIColumn( data = "browserAgent", name = "browserAgent", filter = "", renderer = "hidden", title = "9900004x4", orderNum = 12 )
    private String browserAgent;

    public String getModule() {
        return module;
    }

    public void setModule( String module ) {
        this.module = module;
    }

    /**
     * Instantiates a new user token dto.
     */
    public UserTokenDTO() {
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired( boolean expired ) {
        this.expired = expired;
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
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
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
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
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
     * Gets the created on.
     *
     * @return the created on
     */
    public Date getCreatedOn() {
        return createdOn;
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

    public Long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime( Long expiryTime ) {
        this.expiryTime = expiryTime;
    }

    /**
     * Gets the last request time.
     *
     * @return the last request time
     */
    public Date getLastRequestTime() {
        return lastRequestTime;
    }

    /**
     * Sets the last request time.
     *
     * @param lastRequestTime
     *         the new last request time
     */
    public void setLastRequestTime( Date lastRequestTime ) {
        this.lastRequestTime = lastRequestTime;
    }

    /**
     * Gets the ip address.
     *
     * @return the ip address
     */
    public String getIpAddress() {
        return ipAddress;
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
     * Gets the browser agent.
     *
     * @return the browser agent
     */
    public String getBrowserAgent() {
        return browserAgent;
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
        UserTokenDTO other = ( UserTokenDTO ) obj;
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
