package de.soco.software.simuspace.workflow.model;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.soco.software.simuspace.workflow.model.impl.UserImpl;

/**
 * This interface contains the properties of a user.
 *
 * @author M.Nasir.Farooq
 */
@JsonDeserialize( as = UserImpl.class )
public interface User {

    /**
     * Gets the email address.
     *
     * @return the email address
     */
    String getEmailAddress();

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    String getFirstName();

    /**
     * Gets the id.
     *
     * @return the id
     */
    UUID getId();

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    String getLastName();

    /**
     * Gets the uid.
     *
     * @return the uid
     */

    String getUid();

    /**
     * Sets the email address.
     *
     * @param emailAddress
     *         the new email address
     */
    void setEmailAddress( String emailAddress );

    /**
     * Sets the first name.
     *
     * @param firstName
     *         the new first name
     */
    void setFirstName( String firstName );

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    void setId( UUID id );

    /**
     * Sets the last name .
     *
     * @param lastName
     *         the new last name
     */
    void setLastName( String lastName );

    /**
     * Sets the uid.
     *
     * @param uid
     *         the new uid
     */
    void setUid( String uid );

}