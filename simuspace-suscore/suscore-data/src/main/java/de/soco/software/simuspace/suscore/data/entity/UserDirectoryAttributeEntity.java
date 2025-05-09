package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.util.StringListConverter;

/**
 * The entity class for user directory attributes entity .
 *
 * @author Zeeshan jamal
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "user_directory_attribute" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class UserDirectoryAttributeEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -5277560019131076471L;

    /**
     * The primary key.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

    /**
     * The description.
     */
    @Column( name = "description" )
    private String description;

    /**
     * The systemUsername.
     */
    @Column( name = "systemUsername" )
    private String systemUsername;

    /**
     * The systemPassword.
     */
    @Column( name = "systemPassword" )
    private String systemPassword;

    /**
     * The searchBase.
     */
    @Column( name = "searchBase" )
    private String searchBase;

    /**
     * The url .
     */
    @Column( name = "url" )
    private String url;

    /**
     * The userDnTemplate.
     */
    @Column( name = "userDnTemplate" )
    private String userDnTemplate;

    /**
     * The name.
     */
    @Column( name = "ldapUserName" )
    private String ldapUserName;

    /**
     * The description.
     */
    @Column( name = "ldapSurname" )
    private String ldapSurname;

//    PROPERTIES FOR OAUTH PROVIDER
    // only required types are saved to the db

    /**
     * The ClientId
     */
    @Column( name = "client_id" )
    private String clientId;

    /**
     * The Client Secret
     */
    @Column( name = "client_secret" )
    private String clientSecret;

    /**
     * The baseUrl.
     */
    @Column( name = "base_url" )
    private String baseUrl;

    /**
     * The responseType.
     */
    @Column( name = "response_Type" )
    private String responseType;

    /**
     * The scope.
     */
    @Column( name = "scope" , length = 1024)
    @Convert( converter = StringListConverter.class )
    private List< String > scope;

    /**
     * The providerType
     */
    @Column( name = "provider_type" )
    private String providerType;

    /**
     * The redirectUri.
     */
    @Column( name = "redirect_uri" )
    private String redirectUri;


    /**
     * The wellKnownUrl
     */
    @Column( name = "well_known_url" )
    private String wellKnownUrl;

    /**
     * The authorizationEndpoint
     */
    @Column( name = "authorization_endpoint" )
    private String authorizationEndpoint;

    /**
     * The tokenEndpoint.
     */
    @Column( name = "token_endpoint" )
    private String tokenEndpoint;

    /**
     * The userInfoEndpoint.
     */
    @Column( name = "user_info_endpoint" )
    private String userInfoEndpoint;

    /**
     * The revocationEndpoint.
     */
    @Column( name = "revocation_endpoint" )
    private String revocationEndpoint;



    /**
     * @param id
     *         the id
     * @param name
     *         the name
     * @param description
     *         the description
     * @param systemUsername
     *         the systemUsername
     * @param systemPassword
     *         the systemPassword
     * @param searchBase
     *         the searchBase
     * @param url
     *         the url
     * @param userDnTemplate
     *         the userDnTemplate
     */
    public UserDirectoryAttributeEntity( UUID id, String name, String description, String systemUsername, String systemPassword,
            String searchBase, String url, String userDnTemplate ) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.systemUsername = systemUsername;
        this.systemPassword = systemPassword;
        this.searchBase = searchBase;
        this.url = url;
        this.userDnTemplate = userDnTemplate;
    }


    /**
     * Instantiates a new User directory attribute entity with OAuthProvider Properties.
     *
     * @param clientId
     *         the client id
     * @param clientSecret
     *         the client secret
     * @param id
     *         the id
     * @param name
     *         the name
     * @param baseUrl
     *         the base url
     * @param scope
     *         the scope
     * @param responseType
     *         the response type
     */
    public UserDirectoryAttributeEntity( String clientId, String clientSecret, UUID id, String name, String baseUrl,
            List< String > scope,
            String responseType ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.id = id;
        this.name = name;
        this.baseUrl = baseUrl;
        this.scope = scope;
        this.responseType = responseType;
    }

}
