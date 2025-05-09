package de.soco.software.simuspace.suscore.local.daemon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;

/**
 * The Class UserDaemonEntity.
 */
@Entity
@Table( name = "user_daemon_entity" )
@JsonIgnoreProperties( ignoreUnknown = true )
public class UserDaemonEntity {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -9217419489132160395L;

    /**
     * The primary key.
     */
    @Id
    @Column( name = "id" )
    private String id;

    /**
     * The uid.
     */
    @Column( name = "userUid" )
    private String userUid;

    /**
     * The first name.
     */
    @Column( name = "first_name" )
    private String firstName;

    /**
     * The Sur name.
     */
    @Column( name = "sur_name" )
    private String surName;

    /**
     * The password.
     */
    @Column( name = "password" )
    private String password;

    /**
     * The is changeable.
     */
    @Column( name = "changeable" )
    private Boolean changeable;

    /**
     * status id of an object.
     */
    @Column( name = "status" )
    private Boolean status;

    /**
     * The description.
     */
    @Column( name = "description" )
    private String description;

    /**
     * The restricted.
     */
    @Column( name = "restricted" )
    private Boolean restricted;

    /**
     * The user selection id.
     */
    @Column( name = "location_preference_selection_id" )
    private String locationPreferenceSelectionId;

    /**
     * The Constant FIRST_NAME.
     */
    private static final String FIRST_NAME = "FIRST NAME";

    /**
     * The Constant SUR_NAME.
     */
    private static final String SUR_NAME = "SUR NAME";

    /**
     * The Constant PASS.
     */
    private static final String PASS = "password";

    /**
     * Instantiates a new user entity.
     */
    public UserDaemonEntity() {
    }

    /**
     * one argument constructor for setting UUID.
     *
     * @param id
     *         the id
     */
    public UserDaemonEntity( String id ) {
        super();
        this.id = id;
        this.firstName = FIRST_NAME;
        this.surName = SUR_NAME;
        this.password = PASS;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *         the new description
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * Gets the first name.
     *
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName
     *         the firstName to set
     */
    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *         the new password
     */
    public void setPassword( String password ) {
        this.password = password;
    }

    /**
     * Gets the checks if is changeable.
     *
     * @return the checks if is changeable
     */
    public boolean isChangeable() {
        return changeable;
    }

    /**
     * Sets the changeable.
     *
     * @param changeable
     *         the changeable to set
     */
    public void setChangeable( Boolean changeable ) {
        this.changeable = changeable;
    }

    /**
     * Checks if is status.
     *
     * @return true, if is status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *         the new status
     */
    public void setStatus( Boolean status ) {
        this.status = status;
    }

    /**
     * Checks if is restricted.
     *
     * @return the restricted
     */
    public Boolean isRestricted() {
        return restricted;
    }

    /**
     * Sets the restricted.
     *
     * @param restricted
     *         the restricted to set
     */
    public void setRestricted( Boolean restricted ) {
        this.restricted = restricted;
    }

    /**
     * Gets the sur name.
     *
     * @return the sur name
     */
    public String getSurName() {
        return surName;
    }

    /**
     * Sets the sur name.
     *
     * @param surName
     *         the new sur name
     */
    public void setSurName( String surName ) {
        this.surName = surName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;

        int result = ConstantsInteger.INTEGER_VALUE_ONE;

        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
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
        UserDaemonEntity other = ( UserDaemonEntity ) obj;
        if ( id == null ) {
            if ( other.getId() != null ) {
                return false;
            }
        } else if ( !id.equals( other.getId() ) ) {
            return false;
        }
        return true;
    }

    /**
     * Gets the location preference selection id.
     *
     * @return the location preference selection id
     */
    public String getLocationPreferenceSelectionId() {
        return locationPreferenceSelectionId;
    }

    /**
     * Sets the location preference selection id.
     *
     * @param locationPreferenceSelectionId
     *         the new location preference selection id
     */
    public void setLocationPreferenceSelectionId( String locationPreferenceSelectionId ) {
        this.locationPreferenceSelectionId = locationPreferenceSelectionId;
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
     * Gets the changeable.
     *
     * @return the changeable
     */
    public Boolean getChangeable() {
        return changeable;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * Gets the restricted.
     *
     * @return the restricted
     */
    public Boolean getRestricted() {
        return restricted;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid( String userUid ) {
        this.userUid = userUid;
    }

}
