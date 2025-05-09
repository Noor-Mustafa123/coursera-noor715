package de.soco.software.simuspace.suscore.common.model;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;

/**
 * A class for handling user password update function
 *
 * @author Zeeshan jamal
 */
public class UserPasswordDTO {

    /**
     * The Constant NEW_PWD_LABEL.
     */
    public static final String NEW_PASS_LABEL = "3000033x4";

    /**
     * The Constant CONFIRM_PWD_LABEL.
     */
    public static final String CONFIRM_PASS_LABEL = "3000006x4";

    /**
     * Old Password label
     */
    public static final String OLD_PASWRD_LABEL = "3000039x4";

    /**
     * old password
     */
    @UIFormField( name = "oldPassword", title = OLD_PASWRD_LABEL, section = "Personal Details", type = "password" )
    @UIColumn( data = "oldPassword", filter = "password", renderer = "password", title = OLD_PASWRD_LABEL, isShow = false, name = "password" )
    private String oldPassword;

    /**
     * new password
     */
    @UIFormField( name = "newPassword", title = NEW_PASS_LABEL, section = "Personal Details", type = "password" )
    @UIColumn( data = "newPassword", filter = "password", renderer = "password", title = NEW_PASS_LABEL, isShow = false, name = NEW_PASS_LABEL )
    private String newPassword;

    /**
     * confirm password
     */
    @UIFormField( name = "confirmPassword", title = CONFIRM_PASS_LABEL, section = "Personal Details", type = "password" )
    @UIColumn( data = "confirmPassword", filter = "password", renderer = "password", title = CONFIRM_PASS_LABEL, isShow = false, name = CONFIRM_PASS_LABEL )
    private String confirmPassword;

    /**
     *
     */
    public UserPasswordDTO() {
        super();
    }

    /**
     * @param oldPassword
     *         the old Password
     * @param newPassword
     *         the new Password
     * @param confirmPassword
     *         the confirmPassword
     */
    public UserPasswordDTO( String oldPassword, String newPassword, String confirmPassword ) {
        super();
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    /**
     * gets old password
     *
     * @return oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * sets old password
     *
     * @param oldPassword
     *         the old Password
     */
    public void setOldPassword( String oldPassword ) {
        this.oldPassword = oldPassword;
    }

    /**
     * gets new Password
     *
     * @return newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * sets new password
     *
     * @param newPassword
     *         the new Password
     */
    public void setNewPassword( String newPassword ) {
        this.newPassword = newPassword;
    }

    /**
     * gets confirm password
     *
     * @return confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * sets confirm password
     *
     * @param confirmPassword
     *         the confirmPassword
     */
    public void setConfirmPassword( String confirmPassword ) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * this method validates all passowrds fields
     *
     * @return Notification
     */
    public Notification validate() {

        Notification notification = generateErrorMessagesForMandatoryFields();
        if ( !notification.hasErrors() ) {
            String error = ValidationUtils.passwordValidation( getNewPassword(), MessageBundleFactory.getMessage( UserDTO.PASS_LABEL ) );
            if ( error != null ) {
                notification.addError( new Error(
                        ValidationUtils.passwordValidation( getNewPassword(), MessageBundleFactory.getMessage( UserDTO.PASS_LABEL ) ) ) );
            }

            if ( !getNewPassword().equals( getConfirmPassword() ) ) {
                notification.addError(
                        new Error( MessageBundleFactory.getMessage( Messages.PASSWORD_AND_CONFIRM_PASSWORD_DOES_NOT_MATCHED.getKey() ) ) );
                return notification;
            }
        }

        return notification;
    }

    /**
     * Generate error message for mandatory fields.
     *
     * @return the notification
     */
    private Notification generateErrorMessagesForMandatoryFields() {
        Notification notification = new Notification();

        if ( StringUtils.isBlank( getOldPassword() ) ) {
            notification.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(),
                    MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_OLD_PASSWORD.getKey() ) ) ) );

        }
        if ( StringUtils.isBlank( getNewPassword() ) ) {
            notification.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(),
                    MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_NEW_PASSWORD.getKey() ) ) ) );
        }
        if ( StringUtils.isBlank( getConfirmPassword() ) ) {
            notification.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(),
                    MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_CONFIRM_NEW_PASSWORD.getKey() ) ) ) );

        }
        return notification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UserPasswordDTO [oldPassword=" + oldPassword + ", newPassword=" + newPassword + ", confirmPassword=" + confirmPassword
                + "]";
    }

}
