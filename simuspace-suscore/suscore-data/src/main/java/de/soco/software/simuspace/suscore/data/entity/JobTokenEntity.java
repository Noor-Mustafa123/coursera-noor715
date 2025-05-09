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
 * A database mapping class that holds the properties related to the user token.
 *
 * @author Zeeshan jamal
 */
@Getter
@Setter
@Table
@Entity( name = "SUS_JOB_TOKEN" )
public class JobTokenEntity {

    /**
     * The id of object .Database primary key
     */
    @Id
    @Column( name = "uuid" )
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * user token.
     */
    @Column( name = "token", unique = true )
    private String token;

    /**
     * created on date.
     */
    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * is token expired.
     */
    @Column( name = "is_expired" )
    private boolean expired;

    /**
     * ip address of requesting client.
     */
    @Column( name = "ip_address" )
    private String ipAddress;

    /**
     * browser name of the requesting client.
     */
    @Column( name = "browser_agent" )
    private String browserAgent;

    /**
     * token key.
     */
    @Column( name = "obj_key" )
    private String key;

    /**
     * The user id.
     */
    @Column( name = "user_id" )
    private String userId;

    /**
     * The username.
     */
    @Column( name = "user_name" )
    private String userName;

    /**
     * The auth token.
     */
    @Column( name = "authToken" )
    private String authToken;

}
