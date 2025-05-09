package de.soco.software.simuspace.suscore.common.model;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Cb 2 input param dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class CB2InputParamDTO implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 362803493969335174L;

    /**
     * The Tree path.
     */
    private String treePath;

    /**
     * The Oids.
     */
    private String oids;

    /**
     * The Dest path.
     */
    private String destPath;

    /**
     * The Action.
     */
    private String action;

    /**
     * The Object type.
     */
    private String objectType;

    /**
     * The User uid.
     */
    private String userUid;

    /**
     * The User password.
     */
    private String userPassword;

    /**
     * The Cb 2 server.
     */
    private String cb2Server;

    /**
     * The oidc token
     */
    private String oidcToken;

    /**
     * The refresh token
     */
    private String refreshToken;

    /**
     * The refresh token url for token renewal
     */
    private String refreshTokenUrl;

    /**
     * The redirect uri to be passed with the token renewal request
     */
    private String redirectUrl;

}
