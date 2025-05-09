/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.base;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class is responsible of creating response for every API request.
 *
 * @author Ahsan Khan
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SusResponseDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 351970152014969261L;

    /**
     * This generic holder response field will hold data according to api requested.
     */
    private transient Object data;

    /**
     * The message send to user.
     */
    private Message message;

    /**
     * The success flag either response is of success or not success.
     */
    private boolean success;

    /**
     * The expire flag to redirect a view
     */
    private boolean expire;

    /**
     * Instantiates a new response dto.
     */
    public SusResponseDTO() {

    }

    /**
     * Instantiates a new response dto.
     *
     * @param success
     *         the success
     * @param message
     *         the message
     */
    public SusResponseDTO( boolean success, Message message ) {
        this.success = success;
        this.message = message;
    }

    /**
     * Instantiates a new response dto.
     *
     * @param success
     *         the success
     * @param message
     *         the message
     * @param data
     *         the data
     */
    public SusResponseDTO( boolean success, Message message, Object data ) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    /**
     * Instantiates a new response dto.
     *
     * @param success
     *         the success
     * @param expire
     *         the expire
     * @param message
     *         the message
     * @param data
     *         the data
     */
    public SusResponseDTO( boolean success, boolean expire, Message message, Object data ) {
        this.success = success;
        this.expire = expire;
        this.message = message;
        this.data = data;
    }

    /**
     * Instantiates a new response dto.
     *
     * @param success
     *         the success
     * @param data
     *         the data
     */
    public SusResponseDTO( boolean success, Object data ) {
        this.success = success;
        this.data = data;
    }

    /**
     * Instantiates a new response dto.
     *
     * @param message
     *         the message
     * @param data
     *         the data
     */
    public SusResponseDTO( Message message, Object data ) {

        this.data = data;
        this.message = message;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Gets the success.
     *
     * @return the success
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the data to set
     */
    public void setData( Object data ) {
        this.data = data;
    }

    /**
     * Sets the message.
     *
     * @param message
     *         the message to set
     */
    public void setMessage( Message message ) {
        this.message = message;
    }

    /**
     * Sets the success.
     *
     * @param success
     *         the success to set
     */
    public void setSuccess( boolean success ) {
        this.success = success;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( data == null ) ? 0 : data.hashCode() );
        result = prime * result + ( ( message == null ) ? 0 : message.hashCode() );
        result = prime * result + ( success ? 1231 : 1237 );
        return result;
    }

    /**
     * {@inheritDoc}
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
        SusResponseDTO other = ( SusResponseDTO ) obj;
        if ( data == null ) {
            if ( other.data != null ) {
                return false;
            }
        } else if ( !data.equals( other.data ) ) {
            return false;
        }
        if ( message == null ) {
            if ( other.message != null ) {
                return false;
            }
        } else if ( !message.equals( other.message ) ) {
            return false;
        }
        return success != other.success;
    }

    @Override
    public String toString() {
        return "SusResponseDTO [data=" + data + ", message=" + message + ", success=" + success + ", expire=" + expire + "]";
    }

    /**
     * @return true if expire
     */
    public boolean isExpire() {
        return expire;
    }

    /**
     * @param expire
     *         the expire
     */
    public void setExpire( boolean expire ) {
        this.expire = expire;
    }

}
