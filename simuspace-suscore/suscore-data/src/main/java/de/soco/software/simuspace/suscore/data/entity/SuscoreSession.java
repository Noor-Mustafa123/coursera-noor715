package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class SuscoreSession.
 */
@Getter
@Setter
@Entity
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
@Table( name = "suscore_session" )
public class SuscoreSession implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -4260317043380439379L;

    /**
     * The id.
     */
    @Id
    private Serializable id;

    /**
     * The session.
     */
    @Lob
    @Column( name = "sussession" )
    private byte[] session;

    /**
     * The token.
     */
    @Column
    private String token;

    /**
     * The user id.
     */
    @Column
    private String userId;

    /**
     * The oauth 2 token id.
     */
    @Lob
    @Column( name = "oauth2_id_token" )
    private byte[] oauth2TokenId;

    /**
     * The oauth original token.
     */
    @Column
    private String oauthOriginalToken;

    /**
     * The refresh token.
     */
    @Column
    private String refreshToken;

    /**
     * The is wen login.
     */
    @Column
    private Boolean isWenLogin;

    /**
     * Is wen login boolean.
     *
     * @return the boolean
     */
    public boolean isWenLogin() {
        return Boolean.TRUE == isWenLogin;
    }

    /**
     * Sets wen login.
     *
     * @param wenLogin
     *         the wen login
     */
    public void setWenLogin( Boolean wenLogin ) {
        isWenLogin = wenLogin;
    }

}