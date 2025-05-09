/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;

/**
 * This Class is responsible to create log messages for job
 *
 * @author Aroosa.Bukhari
 * @since 1.0
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LogRecord implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -6428439747213807317L;

    /**
     * The date.
     */
    @UIColumn( data = "date", name = "date", filter = "", renderer = "date", title = "3000083x4", orderNum = 1, isSortable = false )
    private Date date;

    /**
     * The log message.
     */
    @UIColumn( data = "logMessage", name = "logMessage", filter = "", renderer = "text", title = "3000084x4", orderNum = 3, isSortable = false, width = 0 )
    private String logMessage;

    /**
     * The level.
     */
    @UIColumn( data = "type", name = "type", filter = "text", renderer = "messageType", title = "3000051x4", orderNum = 2, isSortable = false )
    private String type;

    /**
     * Instantiates a new log record.
     */
    public LogRecord() {
        super();
    }

    /**
     * Instantiates a new log record.
     *
     * @param type
     *         the type
     * @param logMessage
     *         the log message
     */
    public LogRecord( String type, String logMessage ) {
        this();
        this.type = type;
        this.logMessage = logMessage;

    }

    /**
     * Instantiates a new log record.
     *
     * @param type
     *         the type
     * @param logMessage
     *         the log message
     * @param date
     *         the date
     */
    public LogRecord( String type, String logMessage, Date date ) {
        this( type, logMessage );
        this.date = date;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getLogMessage() {
        return logMessage;
    }

    /**
     * Gets the level.
     *
     * @return the level
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the date.
     *
     * @param date
     *         the new date
     */
    public void setDate( Date date ) {
        this.date = date;
    }

    /**
     * Sets the message.
     *
     * @param message
     *         the message
     */
    public void setLogMessage( String message ) {
        logMessage = message;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "LogRecord [logMessage='" + logMessage + "', type='" + type + "']";
    }

}
