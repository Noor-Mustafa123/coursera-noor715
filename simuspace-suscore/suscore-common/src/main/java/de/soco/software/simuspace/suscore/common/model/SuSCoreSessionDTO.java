package de.soco.software.simuspace.suscore.common.model;

/**
 * The Class SuSCoreSessionDTO.
 */
public class SuSCoreSessionDTO {

    /**
     * The User.
     */
    private UserDTO user;

    /**
     * The session.
     */
    private String session;

    /**
     * The token.
     */
    private String token;

    /**
     * The user id.
     */
    private String userId;

    /**
     * The Oauth 2 token id.
     */
    private String oauth2TokenId;

    /**
     * The Oauth original token.
     */
    private String oauthOriginalToken;

    /**
     * The Refresh token.
     */
    private String refreshToken;

    /**
     * The Is wen login.
     */
    private boolean isWenLogin;

    /**
     * Gets user.
     *
     * @return the user
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user
     *         the user
     */
    public void setUser( UserDTO user ) {
        this.user = user;
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public String getSession() {
        return session;
    }

    /**
     * Sets session.
     *
     * @param session
     *         the session
     */
    public void setSession( String session ) {
        this.session = session;
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets token.
     *
     * @param token
     *         the token
     */
    public void setToken( String token ) {
        this.token = token;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId
     *         the user id
     */
    public void setUserId( String userId ) {
        this.userId = userId;
    }

    /**
     * Gets oauth 2 token id.
     *
     * @return the oauth 2 token id
     */
    public String getOauth2TokenId() {
        return oauth2TokenId;
    }

    /**
     * Sets oauth 2 token id.
     *
     * @param oauth2TokenId
     *         the oauth 2 token id
     */
    public void setOauth2TokenId( String oauth2TokenId ) {
        this.oauth2TokenId = oauth2TokenId;
    }

    /**
     * Gets oauth original token.
     *
     * @return the oauth original token
     */
    public String getOauthOriginalToken() {
        return oauthOriginalToken;
    }

    /**
     * Sets oauth original token.
     *
     * @param oauthOriginalToken
     *         the oauth original token
     */
    public void setOauthOriginalToken( String oauthOriginalToken ) {
        this.oauthOriginalToken = oauthOriginalToken;
    }

    /**
     * Gets refresh token.
     *
     * @return the refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets refresh token.
     *
     * @param refreshToken
     *         the refresh token
     */
    public void setRefreshToken( String refreshToken ) {
        this.refreshToken = refreshToken;
    }

    /**
     * Is wen login boolean.
     *
     * @return the boolean
     */
    public boolean isWenLogin() {
        return isWenLogin;
    }

    /**
     * Sets wen login.
     *
     * @param wenLogin
     *         the wen login
     */
    public void setWenLogin( boolean wenLogin ) {
        isWenLogin = wenLogin;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "SuSCoreSessionDTO{" + "user=" + user + ", session='" + session + '\'' + ", token='" + token + '\'' + ", userId='" + userId
                + '\'' + ", oauth2TokenId='" + oauth2TokenId + '\'' + ", oauthOriginalToken='" + oauthOriginalToken + '\''
                + ", refreshToken='" + refreshToken + '\'' + ", isWenLogin=" + isWenLogin + '}';
    }

}
