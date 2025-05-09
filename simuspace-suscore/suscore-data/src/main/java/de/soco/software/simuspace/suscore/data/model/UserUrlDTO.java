/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The Class UserUrlDTO.
 *
 * @author Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonSerialize( include = JsonSerialize.Inclusion.NON_NULL )
public class UserUrlDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 5626571816410157824L;

    /**
     * The id.
     */
    private UUID id;

    /**
     * The user id.
     */
    private UUID userId;

    /**
     * The path.
     */
    private String path;

    /**
     * The date.
     */
    private Date date;

    /**
     * The label.
     */
    private String label;

    /**
     * The location id.
     */
    private UUID locationId;

    /**
     * Instantiates a new user url.
     */
    public UserUrlDTO() {
    }

    /**
     * Instantiates a new user url.
     *
     * @param path
     *         the path
     * @param locationId
     *         the location id
     */
    public UserUrlDTO( String path, Integer locationId ) {
    }

    /**
     * Instantiates a new user url.
     *
     * @param path
     *         the path
     */
    public UserUrlDTO( final String path ) {
        super();
        this.path = path;
    }

    /**
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path
     *         the new path
     */
    public void setPath( String path ) {
        this.path = path;
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
     * Sets the date.
     *
     * @param date
     *         the new date
     */
    public void setDate( Date date ) {
        this.date = date;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label
     *         the new label
     */
    public void setLabel( String label ) {
        this.label = label;
    }

    /**
     * Gets the location id.
     *
     * @return the location id
     */
    public UUID getLocationId() {
        return locationId;
    }

    /**
     * Sets the location id.
     *
     * @param locationId
     *         the new location id
     */
    public void setLocationId( UUID locationId ) {
        this.locationId = locationId;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId
     *         the new user id
     */
    public void setUserId( UUID userId ) {
        this.userId = userId;
    }

    /**
     * Gets the uid.
     *
     * @return the uid
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UserUrlDTO [id=" + id + ", userId=" + userId + ", path=" + path + ", date=" + date + ", label=" + label + ", locationId="
                + locationId + "]";
    }

}
