package de.soco.software.simuspace.suscore.common.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.UIThemeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;

/**
 * A user model class for mapping to json
 *
 * @author Zeeshan jamal
 */
@Getter
@Setter
@JsonIgnoreProperties( ignoreUnknown = true )
public class UserDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -5709207211683802289L;

    public static final String UID_UI_COLUMN_NAME = "userUid";

    /**
     * The Constant PASS_LABEL.
     */
    public static final String PASS_LABEL = "3000042x4";

    /**
     * The Constant changable.
     */
    public static final String CHANGABLE_UI_COLUMN_NAME = "changable";

    /**
     * Group Constant
     */
    public static final String LABEL_GROUPS = "Groups";

    /**
     * The constant THEME_LABEL.
     */
    public static final String THEME_LABEL = "Theme";

    /**
     * the user uuid
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden", show = false )
    @UIColumn( data = "id", filter = "uuid", renderer = "hidden", title = "3000021x4", name = "id", type = "link", isShow = false )
    private String id;

    /**
     * The profile photo.
     */
    @UIFormField( name = "profilePhoto.url", title = "3000061x4", orderNum = 1, type = "image" )
    @UIColumn( data = "profilePhoto.url", filter = "", renderer = "image", title = "3000061x4", name = "profilePhoto.filePath", isSortable = false, orderNum = 1 )
    private DocumentDTO profilePhoto;

    /**
     * the user id
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = UID_UI_COLUMN_NAME, title = "3000052x4", orderNum = 2 )
    @UIColumn( data = UID_UI_COLUMN_NAME, name = UID_UI_COLUMN_NAME, filter = "text", renderer = "text", title = "3000052x4", orderNum = 2 )
    private String userUid;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000011x4", orderNum = 3 )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 3 )
    private String description;

    /**
     * the user account status
     */
    @UIFormField( name = "status", filterOptions = { "Active",
            "Inactive" }, type = "select", title = "3000049x4", orderNum = 4 )
    @UIColumn( data = "status", name = "status", filter = "select", filterOptions = { "Active",
            "Inactive" }, renderer = "text", type = "select", title = "3000049x4", isSortable = false, orderNum = 4 )
    private String status;

    /**
     * The name.
     */
    @Max( value = 255, message = "3100002x4" )
    @UIFormField( name = "name", title = "3000054x4", isAsk = false, orderNum = 5 )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000054x4", isShow = false )
    private String name;

    /**
     * The groups list.
     */
    private List< SuSUserGroupDTO > groups;

    /**
     * The Groups count.
     */
    @UIColumn( data = "groupsCount", name = "groups", filter = "", url = "system/user/{id}/groups", renderer = "link", title = "3000020x4", orderNum = 6, isSortable = false )
    private Integer groupsCount;

    /**
     * The dir id.
     */
    @UIFormField( name = "susUserDirectoryDTO.name", title = "3000014x4", orderNum = 8 )
    @UIColumn( data = "susUserDirectoryDTO.name", name = "directory.name", filter = "text", renderer = "text", title = "3000014x4", orderNum = 8 )
    private SuSUserDirectoryDTO susUserDirectoryDTO;

    /**
     * the user account status
     */
    @UIFormField( name = "restricted", filterOptions = { "Yes",
            "No" }, type = "hidden", title = "3000046x4", orderNum = 9 )
    @UIColumn( data = "restricted", name = "restricted", filter = "select", renderer = "text", type = "hidden", title = "3000046x4", orderNum = 9, filterOptions = {
            "Yes", "No" } )
    private String restricted;

    /**
     * The theme.
     */
    @UIFormField( name = "theme", title = "3000211x4", type = "select", orderNum = 14 )
    private String theme;

    /**
     * The created on.
     */
    @UIFormField( name = "createdOn", title = "3000008x4", isAsk = false, orderNum = 15, type = "date" )
    @UIColumn( data = "createdOn", name = "createdOn", filter = "dateRange", renderer = "date", title = "3000008x4", orderNum = 15 )
    private String createdOn;

    /**
     * The created by.
     */
    @UIFormField( name = "createdBy.userUid", title = "3000064x4", isAsk = false, orderNum = 16 )
    @UIColumn( data = "createdBy.userUid", name = "createdBy.userUid", filter = "text", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", tooltip = "{createdBy.name}", orderNum = 16 )
    private UserDTO createdBy;

    /**
     * The modified on.
     */
    @UIFormField( name = "modifiedOn", title = "3000029x4", isAsk = false, orderNum = 17, type = "date" )
    @UIColumn( data = "modifiedOn", name = "modifiedOn", filter = "dateRange", renderer = "date", title = "3000029x4", orderNum = 17 )
    private String modifiedOn;

    /**
     * The modified by.
     */
    @UIFormField( name = "modifiedBy.userUid", title = "3000065x4", isAsk = false, orderNum = 18 )
    @UIColumn( data = "modifiedBy.userUid", name = "modifiedBy.userUid", url = "system/user/{modifiedBy.id}", filter = "text", renderer = "link", title = "3000065x4", tooltip = "{modifiedBy.name}", orderNum = 18 )
    private UserDTO modifiedBy;

    /**
     * The Constant changeable.
     */
    @UIFormField( name = "changable", title = "3000004x4", type = "checkbox" )
    @UIColumn( data = "changable", name = CHANGABLE_UI_COLUMN_NAME, title = "3000004x4", filter = "text", renderer = "checkbox", isShow = false )
    private boolean changable;

    /**
     * The password.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "password", title = "3000042x4", type = "password" )
    @UIColumn( data = "password", filter = "password", renderer = "password", title = "3000042x4", isShow = false, name = "password" )
    private String password;

    /**
     * The username.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "firstName", title = "3000018x4" )
    @UIColumn( data = "firstName", name = "firstName", filter = "text", renderer = "text", title = "3000018x4", orderNum = 6 )
    private String firstName;

    /**
     * The surname.
     */
    @NotNull( message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @UIFormField( name = "surName", title = "3000050x4" )
    @UIColumn( data = "surName", name = "surName", filter = "text", renderer = "text", title = "3000050x4", orderNum = 5 )
    private String surName;

    /**
     * User Details reference
     */
    private List< UserDetail > userDetails;

    /**
     * The location preference selection id.
     */
    @UIFormField( name = LOCATION_PREFERENCE_SELECTION_ID, title = "3000125x4", section = "Profile Details" )
    private String locationPreferenceSelectionId;

    /**
     * The security identity.
     */
    private UUID securityIdentity;

    /**
     * The Constant LOCATION_PREFERENCE_SELECTION_ID.
     */
    public static final String LOCATION_PREFERENCE_SELECTION_ID = "locationPreferenceSelectionId";

    /**
     * The Constant MAX_LENGTH_OF_UUID.
     */
    public static final int MAX_LENGTH_OF_UUID = 255;

    /**
     * The Constant ID.
     */
    private static final String USER_ID = "ID";

    /**
     * The Constant UID.
     */
    public static final String USER_UID = "UID";

    /**
     * The Constant FIRST_NAME.
     */
    private static final String FIRST_NAME = "FIRST NAME";

    /**
     * The Constant SUR_NAME.
     */
    private static final String SUR_NAME = "SUR NAME";

    /**
     * The Constant USER_TYPE_RESTRICTED.
     */
    public static final int USER_TYPE_RESTRICTED = 1;

    /**
     * The Constant USER_TYPE_FULL_RIGHTS.
     */
    public static final int USER_TYPE_FULL_RIGHTS = 0;

    /**
     * Flag to show edit button on view user profile page.
     */
    private boolean editable;

    /**
     * Type of user.
     */
    private int type;

    private String token;

    /**
     * Instantiates a new user.
     */
    public UserDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * Instantiates a new user DTO.
     *
     * @param id
     *         the id
     * @param uid
     *         the uid
     * @param status
     *         the status
     * @param restricted
     *         the restricted
     * @param password
     *         the password
     * @param firstName
     *         the first name
     * @param surName
     *         the sur name
     * @param groups
     *         the groups
     * @param userDetails
     *         the user details
     */
    @JsonCreator
    public UserDTO( @JsonProperty( "id" ) String id, @JsonProperty( "uid" ) String uid, @JsonProperty( "status" ) String status,
            @JsonProperty( "restricted" ) String restricted, @JsonProperty( "password" ) String password,
            @JsonProperty( "firstName" ) String firstName, @JsonProperty( "surName" ) String surName,
            @JsonProperty( "groups" ) List< SuSUserGroupDTO > groups, @JsonProperty( "userDetails" ) List< UserDetail > userDetails ) {
        super();
        this.id = id;
        this.userUid = uid;
        this.status = status;
        this.restricted = restricted;
        this.password = password;
        this.firstName = firstName;
        this.surName = surName;
        if ( CollectionUtil.isNotEmpty( groups ) ) {
            this.groups = groups;
        }

        this.userDetails = userDetails;
        this.name = firstName + StringUtils.SPACE + surName;

    }

    /**
     * @param id
     *         the id
     */
    public UserDTO( String id ) {
        super();
        this.id = id;
    }

    /**
     * @param uid
     *         the uid
     * @param password
     *         the password
     */
    public UserDTO( String uid, String password ) {
        super();
        this.userUid = uid;
        this.password = password;
    }

    /**
     * @param id
     *         the id
     * @param uid
     *         the uid
     * @param status
     *         the status
     */
    public UserDTO( String id, String uid, String status ) {
        super();
        this.id = id;
        this.userUid = uid;
        this.status = status;
    }

    /**
     * Instantiates a new User dto.
     *
     * @param uid
     *         the uid
     * @param firstName
     *         the first name
     * @param password
     *         the password
     * @param status
     *         the status
     */
    public UserDTO( String uid, String firstName, String password, String status ) {
        super();
        this.userUid = uid;
        this.firstName = firstName;
        this.password = password;
        this.status = status;
    }

    /**
     * Instantiates a new User dto.
     *
     * @param uid
     *         the uid
     * @param firstName
     *         the first name
     * @param password
     *         the password
     * @param status
     *         the status
     * @param changable
     *         the changable
     */
    public UserDTO( String uid, String firstName, String password, String status, boolean changable ) {
        super();
        this.userUid = uid;
        this.firstName = firstName;
        this.password = password;
        this.status = status;
        this.changable = changable;
    }

    /**
     * @return the userName
     */
    public String getName() {
        name = getSurName() + StringUtils.SPACE + getFirstName();
        return name;
    }

    /**
     * A method for validating user object
     *
     * @param isUpdate
     *         the is update
     * @param isLdap
     *         the is ldap
     *
     * @return the Notification containing errors, if any
     */
    public Notification validate( boolean isUpdate, boolean isLdap ) {
        Notification notification = new Notification();
        if ( StringUtils.isNotBlank( getId() ) ) {
            notification.addNotification( ValidationUtils.validateFieldAndLength( getId(), USER_ID, MAX_LENGTH_OF_UUID, false, true ) );
        }
        notification.addNotification( ValidationUtils.validateFieldAndLength( getUserUid(), USER_UID, MAX_LENGTH_OF_UUID, false, true ) );

        if ( StringUtils.isBlank( getStatus() ) || ( !getStatus().equalsIgnoreCase( ConstantsStatus.INACTIVE )
                && !getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE ) ) ) {
            notification.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.ACCOUNT_STATUS_NOT_SUPPORTED.getKey(), getStatus() ) ) );
        }

        if ( getSusUserDirectoryDTO().getType().equals( ConstantsUserDirectories.INTERNAL_DIRECTORY ) ) {
            // Creating User
            if ( !isUpdate ) {
                String error = ValidationUtils.passwordValidation( getPassword(), MessageBundleFactory.getMessage( PASS_LABEL ) );
                if ( error != null ) {
                    notification.addError( new Error(
                            ValidationUtils.passwordValidation( getPassword(), MessageBundleFactory.getMessage( PASS_LABEL ) ) ) );
                }
            }

            notification.addNotification( ValidationUtils.validateFieldAndLength( getFirstName(), FIRST_NAME,
                    ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );

            notification.addNotification(
                    ValidationUtils.validateFieldAndLength( getSurName(), SUR_NAME, ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
        }

        Supplier< Stream< UserDetail > > streamSupplier = () -> userDetails.stream();

        if ( streamSupplier.get().findAny().isPresent() ) {
            streamSupplier.get().forEach( userDetail -> notification.addNotification( userDetail.validate( isLdap ) ) );
        }

        notification.addNotification( validateUserInDirectories() );

        return notification;
    }

    /**
     * Check if the Directory Is Valid
     *
     * @return Notifications
     */
    private Notification validateUserInDirectories() {
        Notification notif = new Notification();

        if ( getSusUserDirectoryDTO() == null ) {
            notif.addError( new Error(
                    MessageBundleFactory.getMessage( Messages.DIRECTORY_IS_NOT_VALID.getKey(), getSusUserDirectoryDTO().getName() ) ) );

        } else {
            if ( !ValidationUtils.validateUUIDString( getSusUserDirectoryDTO().getId().toString() ) ) {
                notif.addError( new Error( MessageBundleFactory.getMessage( Messages.DIRECTORY_ID_IS_NULL_OR_EMPTY.getKey() ) ) );
            }

        }

        return notif;

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        return result;
    }

    /*
     * (non-Javadoc)
     *
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
        UserDTO other = ( UserDTO ) obj;
        if ( id == null ) {
            return other.id == null;
        } else {
            return id.equals( other.id );
        }
    }

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @return the isChangable
     */
    public boolean isChangable() {
        return changable;
    }

    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", profilePhoto=" + profilePhoto + ", userUid=" + userUid + ", description=" + description
                + ", status=" + status + ", name=" + name + ", groups=" + groups + ", susUserDirectoryDTO=" + susUserDirectoryDTO
                + ", restricted=" + restricted + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", modifiedOn=" + modifiedOn
                + ", modifiedBy=" + modifiedBy + ", changable=" + changable + ", password=" + password + ", firstName=" + firstName
                + ", surName=" + surName + ", userDetails=" + userDetails + ", locationPreferenceSelectionId="
                + locationPreferenceSelectionId + ", securityIdentity=" + securityIdentity + ", editable=" + editable + ", type=" + type
                + "]";
    }

    /**
     * Gets user language form item.
     *
     * @param user
     *         the user
     *
     * @return the user language form item
     */
    public static UIFormItem getUserLanguageFormItem( UserDTO user ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        item.setTitle( "3000210x4" );
        item.setName( "language" );
        item.setType( FieldTypes.SELECTION.getType() );
        PropertiesManager.getRequiredlanguages();
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( var languages : PropertiesManager.getRequiredlanguages().entrySet() ) {
            options.add( new SelectOptionsUI( languages.getKey(), languages.getValue() ) );
        }
        item.setOptions( options );
        item.setValue( user.getUserDetails().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getLanguage() );
        return item;
    }

    /**
     * Gets user theme form item.
     *
     * @param user
     *         the user
     *
     * @return the user theme form item
     */
    public static UIFormItem getUserThemeFormItem( UserDTO user ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        item.setTitle( "3000211x4" );
        item.setName( "theme" );
        item.setType( FieldTypes.SELECTION.getType() );
        UIThemeEnums.getAll();
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( UIThemeEnums themeEnum : UIThemeEnums.getAll() ) {
            options.add( new SelectOptionsUI( themeEnum.getId(), themeEnum.getName() ) );
        }
        item.setOptions( options );
        item.setValue( user.getTheme() );
        return item;
    }

}