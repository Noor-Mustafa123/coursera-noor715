/*
 *
 */

package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The class LoginDTO store values of login authentication
 *
 * @author Zain ul Hassan
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LoginDTO implements Serializable {

    /**
     * Constant serialVersionUID
     */
    private static final long serialVersionUID = -5125487202766269492L;

    /**
     * The user name.
     */
    private String userName;

    /**
     * The user id.
     */
    private String userId;

    /**
     * The user language.
     */
    private String userLanguage;

    @JsonProperty( "X-Auth-Token" )
    private String xAuthToken;

    /**
     * The base url.
     */
    private String baseUrl;

    /**
     * The changeable.
     */
    private boolean changeable;

    /**
     * The url.
     */
    private String url;

    /**
     * The profile photo.
     */
    private String profilePhoto;

    /**
     * The documentation url.
     */
    private String documentationUrl;

    /**
     * The theme.
     */
    private String theme;

    /**
     * Default Constructor
     */
    public LoginDTO() {
        super();
    }

    /**
     * Constructor Using Fields.
     *
     * @param userName
     *         the user name
     * @param userId
     *         the user id
     * @param xAuthToken
     *         the x auth token
     * @param baseUrl
     *         the base url
     * @param changable
     *         the changeable
     */

    public LoginDTO( String userName, String userId, String xAuthToken, String baseUrl, boolean changeable ) {
        this.userName = userName;
        this.userId = userId;
        this.xAuthToken = xAuthToken;
        this.baseUrl = baseUrl;
        this.changeable = changeable;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName
     *         the new user name
     */
    public void setUserName( String userName ) {
        this.userName = userName;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId
     *         the new user id
     */
    public void setUserId( String userId ) {
        this.userId = userId;
    }

    /**
     * Gets the x auth token.
     *
     * @return the x auth token
     */
    public String getxAuthToken() {
        return xAuthToken;
    }

    /**
     * Sets the x auth token.
     *
     * @param xAuthToken
     *         the new x auth token
     */
    public void setxAuthToken( String xAuthToken ) {
        this.xAuthToken = xAuthToken;
    }

    /**
     * Gets the base url.
     *
     * @return the base url
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Sets the base url.
     *
     * @param baseUrl
     *         the new base url
     */
    public void setBaseUrl( String baseUrl ) {
        this.baseUrl = baseUrl;
    }

    /**
     * Checks if is changable.
     *
     * @return true, if is changable
     */
    public boolean isChangable() {
        return changeable;
    }

    /**
     * Sets the changeable.
     *
     * @param changable
     *         the new changeable
     */
    public void setChangeable( boolean changeable ) {
        this.changeable = changeable;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url
     *         the new url
     */
    public void setUrl( String url ) {
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "LoginDTO [ userName = " + userName + ", userId " + userId + ", xAuthToken " + xAuthToken + ", baseUrl " + baseUrl
                + ", changeable " + changeable + "]";
    }

    /**
     * Gets the documentation url.
     *
     * @return the documentation url
     */
    public String getDocumentationUrl() {
        return documentationUrl;
    }

    /**
     * Sets the documentation url.
     *
     * @param documentationUrl
     *         the new documentation url
     */
    public void setDocumentationUrl( String documentationUrl ) {
        this.documentationUrl = documentationUrl;
    }

    /**
     * Gets the user language.
     *
     * @return the user language
     */
    public String getUserLanguage() {
        return userLanguage;
    }

    /**
     * Sets the user language.
     *
     * @param userLanguage
     *         the new user language
     */
    public void setUserLanguage( String userLanguage ) {
        this.userLanguage = userLanguage;
    }

    /**
     * Gets the theme.
     *
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Sets the theme.
     *
     * @param theme
     *         the new theme
     */
    public void setTheme( String theme ) {
        this.theme = theme;
    }

    /**
     * Gets the profile photo.
     *
     * @return the profile photo
     */
    public String getProfilePhoto() {
        return profilePhoto;
    }

    /**
     * Sets the profile photo.
     *
     * @param profilePhoto
     *         the new profile photo
     */
    public void setProfilePhoto( String profilePhoto ) {
        this.profilePhoto = profilePhoto;
    }

}