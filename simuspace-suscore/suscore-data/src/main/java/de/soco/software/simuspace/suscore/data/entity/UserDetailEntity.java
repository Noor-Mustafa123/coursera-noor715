package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class is responsible to provide user detail related information.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table( name = "user_entity_detail" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class UserDetailEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The id of object .Database primary key
     */
    @Id
    @Column( name = "id" )
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The designation.
     */
    @Column( name = "designation" )
    private String designation;

    /**
     * The contacts.
     */
    @Column( name = "contacts" )
    private String contacts;

    /**
     * The email.
     */
    @Column( name = "email" )
    private String email;

    /**
     * the variable to hold the department name
     **/
    @Column( name = "department", nullable = true )
    private String department;

    /**
     * The language.
     */
    @Column( name = "language" )
    private String language;

    /**
     * Image selected for the user profile
     */
    @Column( name = "profile_photo", nullable = true )
    @Lob
    private byte[] profilePhoto;

    /**
     * extension of the profile image to be used while writing the image on the dist on get requests
     */
    @Column( name = "file_extension", nullable = true )
    private String fileExtension;

    /**
     * The user entity reference .
     */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "user_id", referencedColumnName = "id" )
    private UserEntity userEntity;

    /**
     * The document entity.
     */
    @OneToOne
    @JoinColumns( @JoinColumn( name = "document_id", referencedColumnName = "id" ) )
    private DocumentEntity documentEntity;

    /**
     * Instantiates a new user detail entity.
     *
     * @param lastName
     *         the last name
     * @param designation
     *         the designation
     * @param contacts
     *         the contacts
     * @param email
     *         the email
     */
    public UserDetailEntity( UUID id, String designation, String contacts, String email ) {
        super();
        this.designation = designation;
        this.contacts = contacts;
        this.email = email;
        this.id = id;
    }

    /**
     * Full constructor: Instantiates a new user detail entity with all fields.
     *
     * @param lastName
     *         the last name
     * @param designation
     *         the designation
     * @param contacts
     *         the contacts
     * @param email
     *         the email
     */
    public UserDetailEntity( UUID id, String designation, String contacts, String email, String department, String language,
            byte[] profilePhoto ) {
        super();
        this.designation = designation;
        this.contacts = contacts;
        this.email = email;
        this.id = id;
        this.department = department;
        this.language = language;
        this.profilePhoto = profilePhoto;
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
        UserDetailEntity other = ( UserDetailEntity ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        return true;
    }

}
