/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class Directory for files.
 *
 * @author Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Directory {

    /**
     * The dir.
     */
    private String dir;

    /**
     * The show.
     */
    private String show;

    /**
     * The location id.
     */
    private UUID locationId;

    /**
     * Gets the dir.
     *
     * @return the dir
     */
    public String getDir() {
        return dir;
    }

    /**
     * Sets the dir.
     *
     * @param dir
     *         the new dir
     */
    public void setDir( String dir ) {
        this.dir = dir;
    }

    /**
     * Gets the show.
     *
     * @return the show
     */
    public String getShow() {
        return show;
    }

    /**
     * Sets the show.
     *
     * @param show
     *         the new show
     */
    public void setShow( String show ) {
        this.show = show;
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

}
