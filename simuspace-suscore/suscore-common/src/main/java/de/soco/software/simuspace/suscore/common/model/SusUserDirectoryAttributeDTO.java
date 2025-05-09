package de.soco.software.simuspace.suscore.common.model;

import javax.validation.constraints.NotNull;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.constants.OAuthConstants;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The model class for directory attributes that would hold the properties for ldap or active directory directories
 *
 * @author Zeeshan jamal
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SusUserDirectoryAttributeDTO {

    /**
     * system user name field
     */
    public static final String SYSTEM_USER_NAME_FIELD = "System user name";

    /**
     * search base field
     */
    public static final String SEARCH_BASE_FIELD = "Search base";

    /**
     * system password field
     */
    public static final String SYSTEM_PASS_FIELD = "System password";

    /**
     * User DN template field
     */
    public static final String USER_DN_TEMPLATE_FIELD = "User DN template";

    /**
     * url field
     */
    public static final String URL_FIELD = "url";

    /**
     * seach base key for map
     */
    public static final String SEARCH_BASE_KEY = "searchBase";

    /**
     * system user name key for map
     */
    public static final String SYSTEM_USER_NAME_KEY = "systemUsername";

    /**
     * system password key for map
     */
    public static final String SYSTEM_PASS_KEY = "systemPassword";

    /**
     * url key for map
     */
    public static final String URL_KEY = "url";

    /**
     * User DN template key
     */
    public static final String USER_DN_TEMPLATE = "ldap.userDnTemplate";

    /**
     * The id.
     */
    @UIFormField( name = "userDirectoryAttribute.id", type = "hidden", title = "3000021x4" )
    @UIColumn( data = "userDirectoryAttribute.id", name = "userDirectoryAttribute.id", filter = "text", type = "hidden", renderer = "hidden", title = "3000021x4", isShow = false )
    private String id;

    /**
     * The search base.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "userDirectoryAttribute.searchBase", title = "3000001x4", orderNum = 9 )
    @UIColumn( data = "userDirectoryAttribute.searchBase", name = "userDirectoryAttribute.searchBase", filter = "text", renderer = "text", title = "3000001x4", orderNum = 9 )
    private String searchBase;

    /**
     * User DnTemplate for user LDAP Authentication
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "userDirectoryAttribute.userDnTemplate", title = "3000025x4", orderNum = 10 )
    @UIColumn( data = "userDirectoryAttribute.userDnTemplate", name = "userDirectoryAttribute.userDnTemplate", filter = "text", renderer = "text", title = "3000025x4", orderNum = 10 )
    private String userDnTemplate;

    /**
     * url for connecting with ldap.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "userDirectoryAttribute.url", title = "3000024x4", orderNum = 11 )
    @UIColumn( data = "userDirectoryAttribute.url", name = "userDirectoryAttribute.url", filter = "text", renderer = "text", title = "3000024x4", orderNum = 11 )
    private String url;

    /**
     * The system username.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "userDirectoryAttribute.systemUsername", title = "3000023x4", orderNum = 12 )
    @UIColumn( data = "userDirectoryAttribute.systemUsername", name = "userDirectoryAttribute.systemUsername", filter = "text", renderer = "text", title = "3000023x4", orderNum = 12 )
    private String systemUsername;

    /**
     * System password for connecting with ldap.
     */
    @JsonProperty( access = Access.WRITE_ONLY )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "userDirectoryAttribute.systemPassword", title = "3000022x4", isAsk = true, orderNum = 13, type = "password" )
    @UIColumn( data = "userDirectoryAttribute.systemPassword", name = "userDirectoryAttribute.systemPassword", filter = "password", renderer = "password", title = "3000022x4", isShow = false, orderNum = 13 )
    private String systemPassword;

    /**
     * The ldap user name.
     */

    @NotNull( message = "3000129x4" )
    @UIColumn( data = "userDirectoryAttribute.ldapFirstName", name = "userDirectoryAttribute.ldapUserName", filter = "text", renderer = "text", title = "3000129x4", isShow = false, orderNum = 14 )
    private String ldapFirstName;

    /**
     * The ldap sur name.
     */
    @NotNull( message = "3000130x4" )
    @UIColumn( data = "userDirectoryAttribute.ldapSurName", name = "userDirectoryAttribute.ldapSurname", filter = "text", renderer = "text", title = "3000130x4", isShow = false, orderNum = 15 )
    private String ldapSurName;

/**
 * NEW OAUTH PROVIDER DIRECTORY TYPE ATTRIBUTES
 */
    /**
     * Required Types
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "userDirectoryAttribute.name", title = "8800001x4", isAsk = true, orderNum = 14 )
    @UIColumn( data = "userDirectoryAttribute.name", name = "userDirectoryAttribute.name", filter = "text", renderer = "text", title = "8800001x4", orderNum = 16 )
    private String name;

    @NotNull( message = "3100003x4" )
    @UIFormField( name = "userDirectoryAttribute.baseUrl", title = "8800002x4", isAsk = true, orderNum = 15 )
    @UIColumn( data = "userDirectoryAttribute.baseUrl", name = "userDirectoryAttribute.baseUrl", filter = "text", renderer = "text", title = "8800002x4", orderNum = 17 )
    private String base_url;

    @UIFormField( name = "userDirectoryAttribute.clientId", title = "8800003x4", isAsk = true, orderNum = 16 )
    @UIColumn( data = "userDirectoryAttribute.clientId", name = "userDirectoryAttribute.clientId", filter = "text", renderer = "text", title = "8800003x4", orderNum = 18 )
    @NotNull( message = "3100003x4" )
    private String client_id;

    @UIFormField( name = "userDirectoryAttribute.responseType", title = "8800004x4", isAsk = true, orderNum = 17 )
    @UIColumn( data = "userDirectoryAttribute.responseType", name = "userDirectoryAttribute.responseType", filter = "text", renderer = "text", title = "8800004x4", orderNum = 19 )
    @NotNull( message = "3100003x4" )
    private String response_type;

//    Treated as password will not be serialized to json
    @JsonProperty( access = Access.WRITE_ONLY )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "userDirectoryAttribute.client_secret", title = "8800005x4", isAsk = true, orderNum = 18, type = "password" )
    @UIColumn( data = "userDirectoryAttribute.client_secret", name = "userDirectoryAttribute.client_secret", filter = "password", renderer = "password", title = "8800005x4", isShow = false, orderNum = 20 )
    private String client_secret;

    @UIFormField( name = "userDirectoryAttribute.scope", title = "8800006x4", isAsk = true, orderNum = 19 )
    @UIColumn( data = "userDirectoryAttribute.scope", name = "userDirectoryAttribute.scope", filter = "text", renderer = "text", title = "8800006x4", orderNum = 21 )
    @NotNull( message = "3100003x4" )
    private List< String > scope;

//    the field would be of select type
    @UIFormField( name = "userDirectoryAttribute.providerType", filterOptions = { OAuthConstants.OIDC, OAuthConstants.NON_OIDC }, type = "select", title = "8800012x4", orderNum = 25 )
    @UIColumn( data = "userDirectoryAttribute.providerType", name = "userDirectoryAttribute.providerType", filter = "select", filterOptions = { "OIDC","Non-OIDC" }, renderer = "select", title = "8800012x4", orderNum = 26 )
    @NotNull( message = "3100003x4" )
    private String provider_type;


    /**
     * Optional Attributes
     */

    @UIFormField( name = "userDirectoryAttribute.wellKnownUrl", title = "8800007x4", isAsk = true, orderNum = 20 )
    @UIColumn( data = "userDirectoryAttribute.wellKnownUrl", name = "userDirectoryAttribute.wellKnownUrl", filter = "text", renderer = "text", title = "8800007x4", orderNum = 22 )
    private String well_known_url;

    @UIFormField( name = "userDirectoryAttribute.authorizationEndpoint", title = "8800008x4", isAsk = true, orderNum = 21 )
    @UIColumn( data = "userDirectoryAttribute.authorizationEndpoint", name = "userDirectoryAttribute.authorizationEndpoint", filter = "text", renderer = "text", title = "8800008x4", orderNum = 23 )
    private String authorization_endpoint;

    @UIFormField( name = "userDirectoryAttribute.tokenEndpoint", title = "8800009x4", isAsk = true, orderNum = 22 )
    @UIColumn( data = "userDirectoryAttribute.tokenEndpoint", name = "userDirectoryAttribute.tokenEndpoint", filter = "text", renderer = "text", title = "8800009x4", orderNum = 24 )
    private String token_endpoint;

    @UIFormField( name = "userDirectoryAttribute.userInfoEndpoint", title = "8800010x4", isAsk = true, orderNum = 23 )
    @UIColumn( data = "userDirectoryAttribute.userInfoEndpoint", name = "userDirectoryAttribute.userInfoEndpoint", filter = "text", renderer = "text", title = "8800010x4", orderNum = 25 )
    private String user_info_endpoint;

    @UIFormField( name = "userDirectoryAttribute.revocationEndpoint", title = "8800011x4", isAsk = true, orderNum = 24 )
    @UIColumn( data = "userDirectoryAttribute.revocationEndpoint", name = "userDirectoryAttribute.revocationEndpoint", filter = "text", renderer = "text", title = "8800011x4", orderNum = 26 )
    private String revocation_endpoint;

    /**
     * Required
     */
    @UIFormField( name = "userDirectoryAttribute.redirectUri", title = "8800013x4", isAsk = true, orderNum = 26 )
    @UIColumn( data = "userDirectoryAttribute.redirectUri", name = "userDirectoryAttribute.redirectUri", filter = "text", renderer = "text", title = "8800013x4", orderNum = 28 )
    @NotNull( message = "3100003x4" )
    private String redirectUri;

    /**
     * auto-generated/appended Attributes
     */

    private String nonce;

    private String state;

    /**
     * instantiate SusUserDirectoryAttribute
     */
    public SusUserDirectoryAttributeDTO() {
        super();
    }

    /**
     * Instantiates a new Sus user directory attribute dto.
     *
     * @param systemUsername
     *         the systemUsername
     * @param systemPassword
     *         the systemPassword
     * @param url
     *         the url
     */
    public SusUserDirectoryAttributeDTO( String systemUsername, String systemPassword, String url ) {
        this.systemUsername = systemUsername;
        this.systemPassword = systemPassword;
        this.url = url;

    }

    /**
     * Instantiates a new Sus user directory attribute dto.
     *
     * @param searchBase
     *         the searchBase
     * @param systemUsername
     *         the systemUsername
     * @param url
     *         the url
     * @param systemPassword
     *         the systemPassword
     * @param userDnTemplate
     *         the userDnTemplate
     * @param name
     *         the name
     * @param description
     *         the description
     */
    public SusUserDirectoryAttributeDTO( String searchBase, String systemUsername, String url, String systemPassword, String userDnTemplate,
            String name, String description ) {
        super();
        this.searchBase = searchBase;
        this.systemUsername = systemUsername;
        this.url = url;
        this.systemPassword = systemPassword;
        this.userDnTemplate = userDnTemplate;
    }

    /**
     * gets systemUsername
     *
     * @return systemUsername system username
     */
    public String getSystemUsername() {
        return systemUsername;
    }

    /**
     * sets systemUsername
     *
     * @param systemUsername
     *         the systemUsername
     */
    public void setSystemUsername( String systemUsername ) {
        this.systemUsername = systemUsername;
    }

    /**
     * gets url
     *
     * @return url url
     */
    public String getUrl() {
        return url;
    }

    /**
     * sets url
     *
     * @param url
     *         the url
     */
    public void setUrl( String url ) {
        this.url = url;
    }

    /**
     * gets systemPassword
     *
     * @return systemPassword system password
     */
    @JsonIgnore
    public String getSystemPassword() {
        return systemPassword;
    }

    /**
     * sets systemPassword
     *
     * @param systemPassword
     *         the systemPassword
     */
    public void setSystemPassword( String systemPassword ) {
        this.systemPassword = systemPassword;
    }

    /**
     * gets userDnTemplate
     *
     * @return userDnTemplate user dn template
     */
    public String getUserDnTemplate() {
        return userDnTemplate;
    }

    /**
     * sets userDnTemplate
     *
     * @param userDnTemplate
     *         the userDnTemplate
     */
    public void setUserDnTemplate( String userDnTemplate ) {
        this.userDnTemplate = userDnTemplate;
    }

    /**
     * gets searchBase
     *
     * @return searchBase search base
     */
    public String getSearchBase() {
        return searchBase;
    }

    /**
     * sets searchBase
     *
     * @param searchBase
     *         the searchBase
     */
    public void setSearchBase( String searchBase ) {
        this.searchBase = searchBase;
    }

    /**
     * gets id
     *
     * @return id id
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name
     *         the name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets base url.
     *
     * @return the base url
     */
    public String getBase_url() {
        return base_url;
    }

    /**
     * Sets base url.
     *
     * @param base_url
     *         the base url
     */
    public void setBase_url( String base_url ) {
        this.base_url = base_url;
    }

    /**
     * Gets client id.
     *
     * @return the client id
     */
    public String getClient_id() {
        return client_id;
    }

    /**
     * Sets client id.
     *
     * @param client_id
     *         the client id
     */
    public void setClient_id( String client_id ) {
        this.client_id = client_id;
    }

    /**
     * Gets response type.
     *
     * @return the response type
     */
    public String getResponse_type() {
        return response_type;
    }

    /**
     * Sets response type.
     *
     * @param response_type
     *         the response type
     */
    public void setResponse_type( String response_type ) {
        this.response_type = response_type;
    }

    /**
     * Gets client secret.
     *
     * @return the client secret
     */
    public String getClient_secret() {
        return client_secret;
    }

    /**
     * Sets client secret.
     *
     * @param client_secret
     *         the client secret
     */
    public void setClient_secret( String client_secret ) {
        this.client_secret = client_secret;
    }

    /**
     * Gets scope.
     *
     * @return the scope
     */
    public List< String > getScope() {
        return scope;
    }

    /**
     * Sets scope.
     *
     * @param scope
     *         the scope
     */
    public void setScope( List< String > scope ) {
        this.scope = scope;
    }

    /**
     * Validate directory attributes
     *
     * @param directoryType
     *         the directoryType
     *
     * @return the notification containing errors, if any
     */
    public Notification validate( String directoryType ) {

        final Notification notify = new Notification();

        if ( directoryType.equals( ConstantsUserDirectories.LDAP_DIRECTORY ) && StringUtils.isBlank( getUserDnTemplate() ) ) {
            notify.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), USER_DN_TEMPLATE_FIELD ) ) );
        }

        if ( directoryType.equals( ConstantsUserDirectories.ACTIVE_DIRECTORY ) && StringUtils.isBlank( getSearchBase() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), SEARCH_BASE_FIELD ) ) );
        }

        if ( StringUtils.isBlank( getSystemUsername() ) ) {
            notify.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), SYSTEM_USER_NAME_FIELD ) ) );
        }

        if ( StringUtils.isBlank( getSystemPassword() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), SYSTEM_PASS_FIELD ) ) );
        }

        if ( StringUtils.isBlank( getUrl() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), URL_FIELD ) ) );
        }
        return notify;
    }

    /**
     * Validate o auth attributes notification.
     *
     * @return the notification
     */
// Specific validator for OAuth Attributes
    public Notification validateOAuthAttributes() {
        final Notification notify = new Notification();

        if ( StringUtils.isBlank( getClient_id() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Client ID" ) ) );
        }

        if ( StringUtils.isBlank( getClient_secret() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Client Secret" ) ) );
        }

        if ( StringUtils.isBlank( getName() ) ) {
            notify.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Auth Provider Name" ) ) );
        }

        if ( StringUtils.isBlank( getBase_url() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Base_url" ) ) );
        }

        if ( StringUtils.isBlank( getResponse_type() ) ) {
            notify.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Response Type" ) ) );
        }
        // checks the collection "scope"
        if ( CollectionUtils.isEmpty( getScope() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Scope" ) ) );
        }

        if ( StringUtils.isBlank( getRedirectUri() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Redirect Uri" ) ) );
        }

        if ( StringUtils.isBlank( getProvider_type() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Provider Type" ) ) );
        }

    // if the provider type is OIDC then check for nullability of the well known url
        if( StringUtils.equals( getProvider_type(), OAuthConstants.OIDC )){
            if ( StringUtils.isBlank( getWell_known_url() ) ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Well Known Url" ) ) );
            }
        }

    // if the provider type is Non-OIDC then check for nullability of the endpoints
    if( StringUtils.equals( getProvider_type(), OAuthConstants.NON_OIDC )){
        if ( StringUtils.isBlank( getAuthorization_endpoint()) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Authorization Endpoint" ) ) );
        }
        if ( StringUtils.isBlank( getToken_endpoint() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Token Endpoint" ) ) );
        }
        if ( StringUtils.isBlank( getUser_info_endpoint() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "User Info Endpoint" ) ) );
        }
        if ( StringUtils.isBlank( getRevocation_endpoint() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Revocation Endpoint" ) ) );
        }

    }

        return notify;
    }

    @Override
    public String toString() {
        return "SusUserDirectoryAttribute [id=" + id + ", searchBase=" + searchBase + ", systemUsername=" + systemUsername + ", url=" + url
                + ", systemPassword=" + systemPassword + ", userDnTemplate=" + userDnTemplate + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
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
        SusUserDirectoryAttributeDTO other = ( SusUserDirectoryAttributeDTO ) obj;
        if ( searchBase == null ) {
            if ( other.searchBase != null ) {
                return false;
            }
        } else if ( !searchBase.equals( other.searchBase ) ) {
            return false;
        }
        if ( systemUsername == null ) {
            if ( other.systemUsername != null ) {
                return false;
            }
        } else if ( !systemUsername.equals( other.systemUsername ) ) {
            return false;
        }
        if ( url == null ) {
            if ( other.url != null ) {
                return false;
            }
        } else if ( !url.equals( other.url ) ) {
            return false;
        }
        if ( systemPassword == null ) {
            if ( other.systemPassword != null ) {
                return false;
            }
        } else if ( !systemPassword.equals( other.systemPassword ) ) {
            return false;
        }
        if ( userDnTemplate == null ) {
            if ( other.userDnTemplate != null ) {
                return false;
            }
        } else if ( !userDnTemplate.equals( other.userDnTemplate ) ) {
            return false;
        }
        return true;
    }

    /**
     * Gets ldap first name.
     *
     * @return the ldapFirstName
     */
    public String getLdapFirstName() {
        return ldapFirstName;
    }

    /**
     * Sets ldap first name.
     *
     * @param ldapFirstName
     *         the ldapFirstName to set
     */
    public void setLdapFirstName( String ldapFirstName ) {
        this.ldapFirstName = ldapFirstName;
    }

    /**
     * Gets ldap sur name.
     *
     * @return the ldapSurName
     */
    public String getLdapSurName() {
        return ldapSurName;
    }

    /**
     * Sets ldap sur name.
     *
     * @param ldapSurName
     *         the ldapSurName to set
     */
    public void setLdapSurName( String ldapSurName ) {
        this.ldapSurName = ldapSurName;
    }

    public String getState() {
        return state;
    }

    public void setState( String state ) {
        this.state = state;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce( String nonce ) {
        this.nonce = nonce;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri( String redirectUri ) {
        this.redirectUri = redirectUri;
    }

    public String getRevocation_endpoint() {
        return revocation_endpoint;
    }

    public void setRevocation_endpoint( String revocation_endpoint ) {
        this.revocation_endpoint = revocation_endpoint;
    }

    public String getUser_info_endpoint() {
        return user_info_endpoint;
    }

    public void setUser_info_endpoint( String user_info_endpoint ) {
        this.user_info_endpoint = user_info_endpoint;
    }

    public String getToken_endpoint() {
        return token_endpoint;
    }

    public void setToken_endpoint( String token_endpoint ) {
        this.token_endpoint = token_endpoint;
    }

    public String getAuthorization_endpoint() {
        return authorization_endpoint;
    }

    public void setAuthorization_endpoint( String authorization_endpoint ) {
        this.authorization_endpoint = authorization_endpoint;
    }

    public String getWell_known_url() {
        return well_known_url;
    }

    public void setWell_known_url( String well_known_url ) {
        this.well_known_url = well_known_url;
    }

    public String getProvider_type() { return provider_type; }

    public void setProvider_type( String provider_type ) { this.provider_type = provider_type; }



}
