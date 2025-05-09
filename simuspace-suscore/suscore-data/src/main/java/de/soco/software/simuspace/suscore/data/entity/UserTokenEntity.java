package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * A database mapping class that holds the properties related to the user token.
 *
 * @author Zeeshan jamal
 */
@Getter
@Setter
@Table
@Entity( name = "SUS_USER_TOKEN" )
public class UserTokenEntity {

    /**
     * The id of object .Database primary key
     */
    @Id
    @Column( name = "uuid" )
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * user token
     */
    @Column( name = "token", unique = true )
    private String token;

    /**
     * created on date
     */
    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * is token expired
     */
    @Column( name = "is_expired" )
    private boolean expired = false;

    /**
     * ip address of requesting client
     */
    @Column( name = "ip_address" )
    private String ipAddress;

    /**
     * user entity reference against which the token is added
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "user_id", referencedColumnName = "id" )
    private UserEntity userEntity;

    /**
     * browser name of the requesting client
     */
    @Column( name = "browser_agent" )
    private String browserAgent;

    /**
     * expiry time of token
     */
    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "expiry_time" )
    private Date expiryTime;

    /**
     * last request time
     */
    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "last_req_time" )
    private Date lastRequestTime;

    /**
     * token key
     */
    @Column( name = "obj_key" )
    private String key;

    @Column( name = "is_running_job" )
    private Boolean runningJob = false;

}
