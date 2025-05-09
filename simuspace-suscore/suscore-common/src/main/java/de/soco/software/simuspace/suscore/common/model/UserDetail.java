/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.model;

import javax.validation.constraints.Max;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;

/**
 * A user detail model class for mapping to json.
 *
 * @author Nosheen.Sharif
 */
public class UserDetail implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 5805126803975675925L;

    /**
     * the user detail uuid.
     */
    private String id;

    /**
     * The designation.
     */
    @UIFormField( name = "userDetails[0].designation", title = "3000012x4", orderNum = 9, section = "Personal Details" )
    @UIColumn( data = "userDetails[0].designation", filter = "text", renderer = "text", title = "3000012x4", name = "userDetails.designation", orderNum = 9 )
    private String designation;

    /**
     * The contacts.
     */
    @UIFormField( name = "userDetails[0].contacts", title = "3000007x4", orderNum = 10, section = "Personal Details" )
    @UIColumn( data = "userDetails[0].contacts", filter = "text", renderer = "text", title = "3000007x4", name = "userDetails.contacts", orderNum = 10 )
    private String contacts;

    /**
     * The email.
     */
    @Max( value = 255, message = "3100002x4" )
    @UIFormField( name = "userDetails[0].email", title = "3000015x4", orderNum = 11, section = "Personal Details" )
    @UIColumn( data = "userDetails[0].email", filter = "text", renderer = "text", title = "3000015x4", name = "userDetails.email", orderNum = 11 )
    private String email;

    /**
     * the variable to hold the department name *.
     */
    @UIFormField( name = "userDetails[0].department", title = "3000010x4", orderNum = 12, section = "Personal Details" )
    @UIColumn( data = "userDetails[0].department", filter = "text", renderer = "text", title = "3000010x4", name = "userDetails.department", orderNum = 12 )
    private String department;

    /**
     * The language.
     */
    private String language = ConstantsString.DEFAULT_LANGUAGE;

    /**
     * Image resize to height.
     */
    private int resizeToHeight;

    /**
     * Image resize to width.
     */
    private int resizeToWidth;

    /**
     * The Constant MAX_LENGTH_OF_CONTACTS.
     */
    private static final int MAX_LENGTH_OF_CONTACTS = 20;

    /**
     * The Constant MAX_LENGTH_OF_NAME.
     */
    private static final int MAX_LENGTH_OF_NAME = 64;

    /**
     * The Constant MAX_LENGTH_OF_EMAIL.
     */
    private static final int MAX_LENGTH_OF_EMAIL = 64;

    /**
     * The Constant CONTACTS.
     */
    private static final String USER_CONTACTS = "contact";

    /**
     * The Constant EMAIL.
     */
    private static final String USER_EMAIL = "email";

    /**
     * The Constant DEPARTMENT.
     */
    private static final String USER_DEPARTMENT = "department";

    /**
     * The Constant language.
     */
    private static final String LANGUAGE = "language";

    /**
     * The Constant notRequiredPropsForAudit.
     */
    private static final String[] notRequiredPropsForAudit = { "profileImage" };

    /**
     * Instantiates a new user detail.
     */
    public UserDetail() {
    }

    /**
     * Instantiates a new user detail.
     *
     * @param designation
     *         the designation
     * @param contacts
     *         the contacts
     * @param email
     *         the email
     */
    public UserDetail( String designation, String contacts, String email ) {
        this.designation = designation;
        this.contacts = contacts;
        this.email = email;
    }

    /**
     * Instantiates a new user detail.
     *
     * @param id
     *         the id
     * @param designation
     *         the designation
     * @param contacts
     *         the contacts
     * @param email
     *         the email
     * @param department
     *         the department
     */
    public UserDetail( String id, String designation, String contacts, String email, String department ) {
        this( designation, contacts, email );
        this.id = id;
        this.department = department;
    }

    /**
     * Instantiates a new user detail.
     *
     * @param id
     *         the id
     * @param designation
     *         the designation
     * @param contacts
     *         the contacts
     * @param email
     *         the email
     * @param department
     *         the department
     * @param languageId
     *         the language id
     */
    public UserDetail( String id, String designation, String contacts, String email, String department, String language ) {
        this( designation, contacts, email );
        this.id = id;
        this.department = department;
        this.language = language;
    }

    /**
     * Gets the designation.
     *
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the designation.
     *
     * @param designation
     *         the new designation
     */
    public void setDesignation( String designation ) {
        this.designation = designation;
    }

    /**
     * Gets the contacts.
     *
     * @return the contacts
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * Sets the contacts.
     *
     * @param contacts
     *         the new contacts
     */
    public void setContacts( String contacts ) {
        this.contacts = contacts;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email
     *         the new email
     */
    public void setEmail( String email ) {
        this.email = email;
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
     *         the id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets the notrequiredpropsforaudit.
     *
     * @return the notrequiredpropsforaudit
     */
    public static String[] getNotrequiredpropsforaudit() {
        return notRequiredPropsForAudit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UserDetail [ designation=" + designation + ", contacts=" + contacts + ", email=" + email + ", language=" + language + "]";
    }

    /**
     * A method for validating user object.
     *
     * @param isLdap
     *         the is ldap
     *
     * @return notification
     */
    public Notification validate( boolean isLdap ) {
        Notification notification = new Notification();
        notification.addNotification(
                ValidationUtils.validateFieldAndLength( getContacts(), USER_CONTACTS, MAX_LENGTH_OF_CONTACTS, true, false ) );
        notification.addNotification(
                ValidationUtils.validateFieldAndLength( getDepartment(), USER_DEPARTMENT, MAX_LENGTH_OF_NAME, true, true ) );
        notification.addNotification( ValidationUtils.validateFieldAndLength( getLanguage(), LANGUAGE, MAX_LENGTH_OF_NAME, true, true ) );
        notification.addNotification( ValidationUtils.validateFieldAndLength( getEmail(), USER_EMAIL, MAX_LENGTH_OF_EMAIL, true, false ) );
        if ( StringUtils.isNotBlank( getContacts() ) && !ValidationUtils.isValidPhoneNumber( getContacts() ) ) {
            notification.addError( new Error( MessageBundleFactory.getMessage( Messages.INVALID_PHONE_NUMBER.getKey() ) ) );
        }
        if ( StringUtils.isNotBlank( getEmail() ) && !ValidationUtils.isValidEmailAddress( getEmail() ) && !isLdap ) {
            notification.addError( new Error( MessageBundleFactory.getMessage( Messages.INVALID_EMAIL.getKey() ) ) );
        }
        return notification;
    }

    /**
     * Gets the department.
     *
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department.
     *
     * @param department
     *         the department to set
     */
    public void setDepartment( String department ) {
        this.department = department;
    }

    /**
     * Gets the resize to height.
     *
     * @return the resizeToHeight
     */
    public int getResizeToHeight() {
        return resizeToHeight;
    }

    /**
     * Sets the resize to height.
     *
     * @param resizeToHeight
     *         the resizeToHeight to set
     */
    public void setResizeToHeight( int resizeToHeight ) {
        this.resizeToHeight = resizeToHeight;
    }

    /**
     * Gets the resize to width.
     *
     * @return the resizeToWidth
     */
    public int getResizeToWidth() {
        return resizeToWidth;
    }

    /**
     * Sets the resize to width.
     *
     * @param resizeToWidth
     *         the resizeToWidth to set
     */
    public void setResizeToWidth( int resizeToWidth ) {
        this.resizeToWidth = resizeToWidth;
    }

    /**
     * Gets the language.
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language.
     *
     * @param language
     *         the new language
     */
    public void setLanguage( String language ) {
        this.language = language;
    }

}
