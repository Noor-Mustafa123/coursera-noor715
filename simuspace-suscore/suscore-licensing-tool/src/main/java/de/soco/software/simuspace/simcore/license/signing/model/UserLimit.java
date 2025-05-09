/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.simcore.license.signing.model;

import java.io.Serializable;

/**
 * The POJO class is responsible to hold user limit information of the license.
 *
 * @author M.Nasir.Farooq
 */
public class UserLimit implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1173327981964440502L;

    /**
     * The allowed users.
     */
    private int allowedUsers;

    /**
     * The restricted users.
     */
    private int restrictedUsers;

    /**
     * Instantiates a new user limit.
     */
    public UserLimit() {
        super();
    }

    /**
     * Instantiates a new user limit.
     *
     * @param allowedUsers
     *         the allowed users
     * @param restrictedUsers
     *         the restricted users
     */
    public UserLimit( int allowedUsers, int restrictedUsers ) {
        super();
        this.allowedUsers = allowedUsers;
        this.restrictedUsers = restrictedUsers;
    }

    /**
     * Gets the allowed users.
     *
     * @return the allowed users
     */
    public int getAllowedUsers() {
        return allowedUsers;
    }

    /**
     * Sets the allowed users.
     *
     * @param allowedUsers
     *         the new allowed users
     */
    public void setAllowedUsers( int allowedUsers ) {
        this.allowedUsers = allowedUsers;
    }

    /**
     * Gets the restricted users.
     *
     * @return the restricted users
     */
    public int getRestrictedUsers() {
        return restrictedUsers;
    }

    /**
     * Sets the restricted users.
     *
     * @param restrictedUsers
     *         the new restricted users
     */
    public void setRestrictedUsers( int restrictedUsers ) {
        this.restrictedUsers = restrictedUsers;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + allowedUsers;
        result = prime * result + restrictedUsers;
        return result;
    }

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
        UserLimit other = ( UserLimit ) obj;
        if ( allowedUsers != other.allowedUsers ) {
            return false;
        }
        if ( restrictedUsers != other.restrictedUsers ) {
            return false;
        }
        return true;
    }

}
