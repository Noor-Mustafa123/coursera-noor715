/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is for preparing message in response. If the API request was not successful according to expectations or either it have some
 * exception then this plain java object will prepared and set in response to notify the problem.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Message extends DataTransferObject {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -3167707171178910605L;

    /**
     * The Constant SUCCESS.
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * The Constant ERROR.
     */
    public static final String ERROR = "ERROR";

    /**
     * The Constant INFO.
     */
    public static final String INFO = "INFO";

    public static final String WARNING = "WARNING";

    /**
     * The content.
     */
    @JsonInclude( Include.NON_NULL )
    private String content;

    /**
     * The type.
     */
    @JsonInclude( Include.NON_NULL )
    private String type;

    /**
     * Instantiates a new message dto.
     */
    public Message() {
        super();
    }

    /**
     * Instantiates a new message dto.
     *
     * @param type
     *         the type
     * @param content
     *         the content
     */
    public Message( String type, String content ) {
        this();
        this.type = type;
        this.content = content;
    }

    /**
     * Gets the content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the content.
     *
     * @param content
     *         the new content
     */
    public void setContent( String content ) {
        this.content = content;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( content == null ) ? 0 : content.hashCode() );
        result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
        return result;
    }

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
        Message other = ( Message ) obj;
        if ( content == null ) {
            if ( other.content != null ) {
                return false;
            }
        } else if ( !content.equals( other.content ) ) {
            return false;
        }
        if ( type == null ) {
            if ( other.type != null ) {
                return false;
            }
        } else if ( !type.equals( other.type ) ) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MessageDTO [type=" + type + ", content=" + content + "]";
    }

}
