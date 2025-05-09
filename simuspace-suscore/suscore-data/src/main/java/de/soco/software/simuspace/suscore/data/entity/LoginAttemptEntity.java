package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * An entity class that would be mapped as the login_attempt as table in database. The class maps the data related failed attempts to login
 * in simuspace.
 *
 * @author Zeeshan jamal
 */
@Getter
@Setter
@Entity
@Table( name = "login_attempt" )
public class LoginAttemptEntity {

    /**
     * The id of login_attempt .Database table primary key
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID uuid;

    /**
     * The user name
     */
    @Column( name = "user_uid", unique = true, nullable = false )
    private String userUid;

    /**
     * The attempts
     */
    @Column
    private Integer attempts;

    /**
     * The timestamp
     */
    @Temporal( TemporalType.TIMESTAMP )
    private Date lastMofied;

    /**
     * The ldap attempts.
     */
    @Column
    private Integer ldapAttempts;

    /**
     * The Unlock time.
     */
    @Column
    private Date unlockTime;

    /**
     * The Lock time.
     */
    @Column
    private Date lockTime;

    /**
     * The Locked.
     */
    @Column
    private Boolean locked;

    /**
     * @return the userUid
     */
    public String getUid() {
        return userUid;
    }

    /**
     * @param uid
     *         the userUid
     */
    public void setUid( String uid ) {
        this.userUid = uid;
    }

}