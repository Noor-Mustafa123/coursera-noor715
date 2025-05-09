package de.soco.software.simuspace.workflow.dto;

import de.soco.software.simuspace.workflow.model.User;

/**
 * This is a model class contains the feature and tokens of a user.
 *
 * @author Ali.Haider
 */
public class UserLicenseDTO {

    /**
     * The user.
     */
    private User user;

    /**
     * The user component. i.e user/manager.
     */
    private String feature;

    /**
     * The ip address.
     */
    private String ipAddress;

    /**
     * The count for token.
     */
    private Integer token;

    /**
     * Instantiates a new user license data transfer object.
     */
    public UserLicenseDTO() {
        super();
    }

    /**
     * Instantiates a new user license dto.
     *
     * @param user
     *         the user
     * @param feature
     *         the feature
     * @param ipAddress
     *         the ip address
     * @param token
     *         the token
     */
    public UserLicenseDTO( User user, String feature, String ipAddress, Integer token ) {
        this.user = user;
        this.feature = feature;
        this.ipAddress = ipAddress;
        this.token = token;
    }

    /**
     * Gets the feature.
     *
     * @return the feature
     */
    public String getFeature() {
        return feature;
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
     * Gets the token.
     *
     * @return the token
     */
    public Integer getToken() {
        return token;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the feature.
     *
     * @param feature
     *         the new feature
     */
    public void setFeature( String feature ) {
        this.feature = feature;
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
     * Sets the token.
     *
     * @param token
     *         the new token
     */
    public void setToken( Integer token ) {
        this.token = token;
    }

    /**
     * Sets the user.
     *
     * @param user
     *         the new user
     */
    public void setUser( User user ) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserLicenseDTO [user='" + user + "', feature='" + feature + "', ipAddress='" + ipAddress + "', token='" + token + "']";
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
        final UserLicenseDTO other = ( UserLicenseDTO ) obj;
        if ( feature == null ) {
            if ( other.feature != null ) {
                return false;
            }
        } else if ( !feature.equals( other.feature ) ) {
            return false;
        }
        if ( ipAddress == null ) {
            if ( other.ipAddress != null ) {
                return false;
            }
        } else if ( !ipAddress.equals( other.ipAddress ) ) {
            return false;
        }
        if ( token == null ) {
            if ( other.token != null ) {
                return false;
            }
        } else if ( !token.equals( other.token ) ) {
            return false;
        }
        if ( user == null ) {
            if ( other.user != null ) {
                return false;
            }
        } else if ( !user.equals( other.user ) ) {
            return false;
        }
        return true;
    }

}
