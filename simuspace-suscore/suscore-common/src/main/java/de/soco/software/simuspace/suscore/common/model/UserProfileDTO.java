package de.soco.software.simuspace.suscore.common.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;

/**
 * A user Profile Dto model class for mapping to json
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class UserProfileDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -5709207211683802289L;

    /**
     * Group Constant
     */
    public static final String GROUPS = "Groups";

    /**
     * the user uuid
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden", show = false )
    @UIColumn( data = "id", filter = "uuid", renderer = "hidden", title = "3000021x4", name = "userUuid", isShow = false, type = "link" )
    private String id;

    /**
     * the user id
     */
    @UIFormField( name = "uid", title = "3000052x4", readonly = true, orderNum = 0, section = "Personal Details" )
    @UIColumn( data = "uid", name = "uid", filter = "text", renderer = "text", title = "3000052x4" )
    private String uid;

    /**
     * The user name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "firstName", title = "3000018x4", orderNum = 1, section = "Personal Details" )
    @UIColumn( data = "firstName", name = "firstName", filter = "text", renderer = "text", title = "3000018x4" )
    private String firstName;

    /**
     * The sur name.
     */
    @Max( value = 255, message = "3100002x4" )
    @UIFormField( name = "surName", title = "3000050x4", orderNum = 2, section = "Personal Details" )
    @UIColumn( data = "surName", name = "surName", filter = "text", renderer = "text", title = "3000050x4" )
    @NotNull( message = "3100003x4" )
    private String surName;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000011x4", orderNum = 3, section = "Personal Details" )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4" )
    private String description;

    /**
     * User Details reference
     */
    private List< UserDetail > userDetails;

    /**
     * Image path selected for the user profile
     */
    @UIFormField( name = "profilePhoto", title = "3000061x4", acceptedFiles = "image/*", maxFiles = 1, type = "file", section = "Personal Details" )
    @UIColumn( data = "profilePhoto", filter = "file", renderer = "file", title = "3000061x4", name = "profilePhoto" )
    private ImageView profilePhoto;

    /**
     * The location preference selection id.
     */
    @UIFormField( name = LOCATION_PREFERENCE_SELECTION_ID, title = "3000125x4", section = "Profile Details" )
    private String locationPreferenceSelectionId;

    /**
     * The theme.
     */
    private String theme;

    /**
     * The created on.
     */
    @UIFormField( name = "createdOn", title = "3000008x4", orderNum = 20, section = "Profile Details", readonly = true, type = "date")
    @UIColumn( data = "createdOn", name = "createdOn", filter = "dateRange", renderer = "date", title = "3000008x4", orderNum = 20 )
    private String createdOn;

    /**
     * The created by.
     */
    @UIFormField( name = "createdBy.userUid", title = "3000064x4", readonly = true, orderNum = 21, section = "Profile Details" )
    @UIColumn( data = "createdBy.userUid", name = "createdBy.userUid", filter = "text", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", tooltip = "{createdBy.userName}", orderNum = 21 )
    private UserDTO createdBy;

    /**
     * The modified on.
     */
    @UIFormField( name = "modifiedOn", title = "3000029x4", readonly = true, orderNum = 22, section = "Profile Details", type = "date")
    @UIColumn( data = "modifiedOn", name = "modifiedOn", filter = "dateRange", renderer = "date", title = "3000029x4", orderNum = 22 )
    private String modifiedOn;

    /**
     * The modified by.
     */
    @UIFormField( name = "modifiedBy.userUid", title = "3000065x4", readonly = true, orderNum = 23, section = "Profile Details" )
    @UIColumn( data = "modifiedBy.userUid", name = "modifiedBy.userUid", url = "system/user/{modifiedBy.id}", filter = "text", renderer = "link", title = "3000065x4", tooltip = "{modifiedBy.userName}", orderNum = 23 )
    private UserDTO modifiedBy;

    /**
     * the user account status
     */
    @UIFormField( name = "status", filterOptions = { "Active",
            "Inactive" }, type = "select", title = "3000049x4", readonly = true, orderNum = 24, section = "Profile Details" )
    @UIColumn( data = "status", name = "status", filter = "select", filterOptions = { "Active",
            "Inactive" }, renderer = "text", type = "select", title = "3000049x4", isSortable = false, orderNum = 24 )
    private String status;

    /**
     * the user account status
     */
    @UIFormField( name = "restricted", filterOptions = { "Yes",
            "No" }, type = "hidden", title = "3000046x4", readonly = true, orderNum = 25, section = "Profile Details" )
    @UIColumn( data = "restricted", name = "restricted", filter = "select", filterOptions = { "Yes",
            "No" }, renderer = "text", type = "hidden", title = "3000046x4", orderNum = 25 )
    private String restricted;

    /**
     * The dir id.
     */
    @UIFormField( name = "susUserDirectoryDTO.name", title = "3000014x4", readonly = true, orderNum = 26, section = "Profile Details" )
    @UIColumn( data = "susUserDirectoryDTO.name", name = "directory.name", filter = "text", renderer = "text", title = "3000014x4", orderNum = 26 )
    private SuSUserDirectoryDTO susUserDirectoryDTO;

    /**
     * userPasswordDto
     */
    private transient UserPasswordDTO userPasswordDto;

    /**
     * The Constant MAX_LENGTH_OF_NAME.
     */
    private static final int MAX_LENGTH_OF_NAME = 255;

    /**
     * The Constant MAX_LENGTH_OF_UUID.
     */
    public static final int MAX_LENGTH_OF_UUID = 64;

    /**
     * The Constant ID.
     */
    private static final String USER_PROFILE_ID = "ID";

    /**
     * The Constant UID.
     */
    private static final String USER_PROFILE_UID = "UID";

    /**
     * The Constant USER_NAME.
     */
    private static final String FIRST_NAME = "FIRST NAME";

    /**
     * The Constant SUR_NAME.
     */
    private static final String SUR_NAME = "SUR NAME";

    /**
     * The Constant LOCATION_PREFERENCE_SELECTION_ID.
     */
    public static final String LOCATION_PREFERENCE_SELECTION_ID = "locationPreferenceSelectionId";

    /**
     * The Constant USER_TYPE_RESTRICTED.
     */
    public static final int USER_TYPE_RESTRICTED = 1;

    /**
     * The Constant USER_TYPE_FULL_RIGHTS.
     */
    public static final int USER_TYPE_FULL_RIGHTS = 0;

    /**
     * The Constant changeable.
     */
    private boolean changable;

    /**
     * Flag to show edit button on view user profile page.
     */
    private boolean editable;

    /**
     * Type of user.
     */
    private int type;

    /**
     * old password
     */
    private String oldPassword;

    /**
     * new password
     */
    private String newPassword;

    /**
     * confirm password
     */
    private String confirmPassword;

    /**
     * Instantiates a new user.
     */
    public UserProfileDTO() {
    }

    /**
     * Constructor Using Fields
     *
     * @param id
     *         the id
     * @param uid
     *         the uid
     * @param restricted
     *         the restricted
     * @param userPasswordDto
     *         the userPasswordDto
     * @param userName
     *         the userName
     * @param surName
     *         the surName
     * @param groups
     *         the groups
     * @param userDetails
     *         the userDetail
     */
    public UserProfileDTO( String id, String uid, String oldPassword, String newPassword, String confirmPassword, String firstName,
            String surName, List< UserDetail > userDetails ) {
        super();
        this.id = id;
        this.uid = uid;
        this.userPasswordDto = new UserPasswordDTO( oldPassword, newPassword, confirmPassword );
        this.firstName = firstName;
        this.surName = surName;
        this.userDetails = userDetails;

    }

    /**
     * @param id
     *         the id
     */
    public UserProfileDTO( String id ) {
        super();
        this.id = id;
    }

    /**
     * get id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * sets id
     *
     * @param id
     *         the id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets uid
     *
     * @param uid
     *         the uid
     */
    public void setUid( String uid ) {
        this.uid = uid;
    }

    /**
     * @return the changable
     */
    public boolean isChangable() {
        return changable;
    }

    /**
     * @param changable
     *         the changable
     */
    public void setChangable( boolean changable ) {
        this.changable = changable;
    }

    /**
     * @return the userDetail
     */
    public List< UserDetail > getUserDetails() {
        return userDetails;
    }

    /**
     * @param userDetails
     *         the userDetail
     */
    public void setUserDetails( List< UserDetail > userDetails ) {
        this.userDetails = userDetails;
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

    /**
     * A method for validating user object
     *
     * @return the Notification containing errors, if any
     */
    public Notification validate( boolean isLdap ) {
        Notification notification = new Notification();
        if ( StringUtils.isNotBlank( getId() ) ) {
            notification.addNotification(
                    ValidationUtils.validateFieldAndLength( getId(), USER_PROFILE_ID, MAX_LENGTH_OF_UUID, false, true ) );
        }
        notification.addNotification(
                ValidationUtils.validateFieldAndLength( getUid(), USER_PROFILE_UID, MAX_LENGTH_OF_UUID, false, true ) );

        notification
                .addNotification( ValidationUtils.validateFieldAndLength( getFirstName(), FIRST_NAME, MAX_LENGTH_OF_NAME, false, false ) );

        notification.addNotification( ValidationUtils.validateFieldAndLength( getSurName(), SUR_NAME, MAX_LENGTH_OF_NAME, false, false ) );

        if ( CollectionUtil.isNotEmpty( userDetails ) ) {
            for ( UserDetail userDetail : userDetails ) {
                notification.addNotification( userDetail.validate( isLdap ) );
            }
        }

        if ( userPasswordDto != null && ( StringUtils.isNotBlank( userPasswordDto.getOldPassword() ) || StringUtils.isNotBlank(
                userPasswordDto.getNewPassword() ) || StringUtils.isNotBlank( userPasswordDto.getConfirmPassword() ) ) ) {
            notification.addNotification( userPasswordDto.validate() );
        }

        return notification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
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
        UserProfileDTO other = ( UserProfileDTO ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        return true;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( int type ) {
        this.type = type;
    }

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable
     *         the editable to set
     */
    public void setEditable( boolean editable ) {
        this.editable = editable;
    }

    /**
     * @return the userPasswordDto
     */
    public UserPasswordDTO getUserPasswordDto() {
        return userPasswordDto;
    }

    /**
     * @param userPasswordDto
     *         the userPasswordDto to set
     */
    public void setUserPasswordDto( UserPasswordDTO userPasswordDto ) {
        this.userPasswordDto = userPasswordDto;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *         the firstName to set
     */
    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    /**
     * Gets the profile photo.
     *
     * @return the profile photo
     */
    public ImageView getProfilePhoto() {
        return profilePhoto;
    }

    /**
     * Sets the profile photo.
     *
     * @param profilePhoto
     *         the new profile photo
     */
    public void setProfilePhoto( ImageView profilePhoto ) {
        this.profilePhoto = profilePhoto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UserProfileDTO [id=" + id + ", uid=" + uid + ", userName=" + firstName + ", surName=" + surName + ", userDetails="
                + userDetails + ", userPasswordDto=" + userPasswordDto + ", changable=" + changable + ", editable=" + editable + ", type="
                + type + "]";
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
     * Gets the theme.
     *
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Sets the theme.
     *
     * @param theme
     *         the new theme
     */
    public void setTheme( String theme ) {
        this.theme = theme;
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
     * Gets the created on.
     *
     * @return the created on
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( String createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Gets the created by.
     *
     * @return the created by
     */
    public UserDTO getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the created by.
     *
     * @param createdBy
     *         the new created by
     */
    public void setCreatedBy( UserDTO createdBy ) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the modified on.
     *
     * @return the modified on
     */
    public String getModifiedOn() {
        return modifiedOn;
    }

    /**
     * Gets the modified by.
     *
     * @return the modified by
     */
    public UserDTO getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets the modified on.
     *
     * @param modifiedOn
     *         the new modified on
     */
    public void setModifiedOn( String modifiedOn ) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * Sets the modified by.
     *
     * @param modifiedBy
     *         the new modified by
     */
    public void setModifiedBy( UserDTO modifiedBy ) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *         the new status
     */
    public void setStatus( String status ) {
        this.status = status;
    }

    /**
     * Gets the restricted.
     *
     * @return the restricted
     */
    public String getRestricted() {
        return restricted;
    }

    /**
     * Sets the restricted.
     *
     * @param restricted
     *         the new restricted
     */
    public void setRestricted( String restricted ) {
        this.restricted = restricted;
    }

    /**
     * Gets the sus user directory DTO.
     *
     * @return the sus user directory DTO
     */
    public SuSUserDirectoryDTO getSusUserDirectoryDTO() {
        return susUserDirectoryDTO;
    }

    /**
     * Sets the sus user directory DTO.
     *
     * @param susUserDirectoryDTO
     *         the new sus user directory DTO
     */
    public void setSusUserDirectoryDTO( SuSUserDirectoryDTO susUserDirectoryDTO ) {
        this.susUserDirectoryDTO = susUserDirectoryDTO;
    }

}