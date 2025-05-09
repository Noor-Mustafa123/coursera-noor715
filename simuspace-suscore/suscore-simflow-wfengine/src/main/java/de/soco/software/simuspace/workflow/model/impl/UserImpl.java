package de.soco.software.simuspace.workflow.model.impl;

import java.util.UUID;

import de.soco.software.simuspace.workflow.model.User;

/**
 * This is a model class contains the properties of a user.
 *
 * @author M.Nasir.Farooq
 */
public class UserImpl implements User {

    /**
     * The email address.
     */
    private String emailAddress;

    /**
     * The firstName.
     */
    private String firstName;

    /**
     * The id.
     */
    private UUID id;

    /**
     * The lastName.
     */
    private String lastName;

    /**
     * The name of owner
     */
    private String name;

    /**
     * The uid.
     */
    private String uid;

    /**
     * Instantiates a new user impl.
     */
    public UserImpl() {
        super();
    }

    /**
     * Instantiates a new user impl.
     *
     * @param id
     *         the id
     * @param firstName
     *         the firstName
     * @param lastName
     *         the lastName
     * @param uid
     *         the uid
     * @param emailAddress
     *         the email_address
     */
    public UserImpl( UUID id, String firstName, String lastName, String uid, String emailAddress ) {
        this();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.uid = uid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirstName() {

        return firstName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLastName() {

        return lastName;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUid() {

        return uid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEmailAddress( String emailAddress ) {
        this.emailAddress = emailAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUid( String uid ) {
        this.uid = uid;
    }

}
