package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;

/**
 * The Class is responsible to provide user related information.
 */
@Getter
@Setter
@Entity
@Table( name = "user_entity" )
public class UserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3997427036836884103L;

    /**
     * The Constant FIELD_NAME_MODIFIED_ON.
     */
    public static final String FIELD_NAME_MODIFIED_ON = "modifiedOn";

    /**
     * The primary key.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * Creation date of object
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * modification date of object
     */
    @Column( name = "modified_on" )
    private Date modifiedOn;

    /**
     * isDeleted
     */
    @Column( name = "is_delete" )
    private boolean isDelete;

    /**
     * auditLogEntity mapping
     */
    @ManyToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private AuditLogEntity auditLogEntity;

    /**
     * owner mapping
     */
    @OneToOne( fetch = FetchType.LAZY )
    private UserEntity createdBy;

    /**
     * The modified by.
     */
    @OneToOne( fetch = FetchType.LAZY )
    private UserEntity modifiedBy;

    /**
     * The uid.
     */
    @Column( name = "user_uid" )
    private String userUid;

    /**
     * The first name.
     */
    @Column( name = "first_name" )
    @NotNull( message = "first cannot be empty" )
    @Size( min = 3, max = 40, message = "First name should be between max and min" )
    private String firstName;

    /**
     * The Sur name.
     */
    @Column( name = "sur_name" )
    @NotNull( message = "Surname cannot be empty" )
    @Size( min = 3, max = 40, message = "Sur name should be between max and min" )
    private String surName;

    /**
     * The password.
     */
    @Column( name = "password" )
    @NotNull( message = "Password for user cannot be empty" )
    private String password;

    /**
     * directory to which the user relates.
     */
    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = "dir_id", referencedColumnName = "id" )
    private SuSUserDirectoryEntity directory;

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
     * The list of userDetails.
     */
    @OneToMany( fetch = FetchType.LAZY, mappedBy = "userEntity", cascade = CascadeType.ALL )
    private Set< UserDetailEntity > userDetails;

    /**
     * The document.
     */
    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumns( @JoinColumn( name = "document_id", referencedColumnName = "id" ) )
    private DocumentEntity profilePhoto;

    /**
     * The user entities.
     */
    @ManyToMany( fetch = FetchType.LAZY, mappedBy = "users" )
    private Set< GroupEntity > groups;

    /**
     * The restricted.
     */
    @Column( name = "restricted" )
    private Boolean restricted;

    /**
     * The theme.
     */
    @Column( name = "theme" )
    private String theme;

    /**
     * The security identity entity.
     */
    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinColumn( name = "sid_id", referencedColumnName = "id", nullable = false )
    private AclSecurityIdentityEntity securityIdentityEntity;

    /**
     * The user selection id.
     */
    @Column( name = "location_preference_selection_id" )
    private String locationPreferenceSelectionId;

    /**
     * The first name.
     */
    @Column( name = "ldap_first_name" )
    private String ldapFirstName;

    /**
     * The Sur name.
     */
    @Column( name = "ldap_sur_name" )
    private String ldapSurName;

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
    public UserEntity() {
    }

    /**
     * one argument constructor for setting UUID.
     *
     * @param id
     *         the id
     */
    public UserEntity( UUID id ) {
        super();
        this.setId( id );
        this.firstName = FIRST_NAME;
        this.surName = SUR_NAME;
        this.password = PASS;
    }

    /**
     * Instantiates a new user entity.
     *
     * @param id
     *         the id
     * @param uid
     *         the uid
     * @param userName
     *         the first name
     * @param password
     *         the password
     * @param directory
     *         the directory entity
     * @param isChangeable
     *         the is changeable
     */
    public UserEntity( UUID id, String uid, String userName, String password, SuSUserDirectoryEntity directory, boolean isChangeable ) {
        super();
        this.userUid = uid;
        this.firstName = userName;
        this.password = password;
        this.changeable = isChangeable;
        this.setId( id );
        this.directory = directory;
        this.surName = SUR_NAME;
    }

    /**
     * Instantiates a new user entity.
     *
     * @param id
     *         the id
     * @param uid
     *         the uid
     * @param userName
     *         the user name
     * @param isChangeable
     *         the is changeable
     */
    public UserEntity( UUID id, String uid, String userName, boolean isChangeable ) {
        super();
        this.setId( id );
        this.userUid = uid;
        this.firstName = userName;
        this.changeable = isChangeable;
        this.surName = SUR_NAME;
        this.password = PASS;
    }

    /**
     * @return the isDelete
     */
    public boolean isDelete() {
        return isDelete;
    }

    /**
     * @param isDelete
     *         the isDelete to set
     */
    public void setDelete( boolean isDelete ) {
        this.isDelete = isDelete;
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
     * Checks if is status.
     *
     * @return true, if is status
     */
    public boolean isStatus() {
        return status;
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
     * Gets the document.
     *
     * @return the document
     */
    public DocumentEntity getDocument() {
        return profilePhoto;
    }

    /**
     * Sets the document.
     *
     * @param document
     *         the new document
     */
    public void setDocument( DocumentEntity document ) {
        this.profilePhoto = document;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;

        int result = ConstantsInteger.INTEGER_VALUE_ONE;

        result = prime * result + ( ( getId() == null ) ? 0 : getId().hashCode() );
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
        UserEntity other = ( UserEntity ) obj;
        if ( getId() == null ) {
            if ( other.getId() != null ) {
                return false;
            }
        } else if ( !getId().equals( other.getId() ) ) {
            return false;
        }
        return true;
    }

}