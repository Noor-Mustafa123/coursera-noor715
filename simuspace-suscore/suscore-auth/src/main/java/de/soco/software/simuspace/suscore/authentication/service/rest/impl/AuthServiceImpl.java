package de.soco.software.simuspace.suscore.authentication.service.rest.impl;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.authentication.CustomAuthorizingRealm;
import de.soco.software.simuspace.suscore.authentication.OIDCCustomAuthRealm;
import de.soco.software.simuspace.suscore.authentication.SuSshiroAuthentication;
import de.soco.software.simuspace.suscore.authentication.activator.Activator;
import de.soco.software.simuspace.suscore.authentication.manager.AuthManager;
import de.soco.software.simuspace.suscore.authentication.service.rest.AuthService;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLicenseType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Cb2OperationEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.UIThemeEnums;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.LoginDTO;
import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.UserDetail;
import de.soco.software.simuspace.suscore.common.model.UserPasswordDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.CompressionUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PasswordUtils;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.SuSStagingUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.entity.JobTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.SuscoreSession;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.notification.service.rest.WSService;
import de.soco.software.simuspace.suscore.permissions.constants.SpecifiedUser;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * Implementation of AuthService to communicate with usermanager and shiro to authenticate user.
 *
 * @author Zeeshan jamal
 */
@Log4j2
public class AuthServiceImpl extends AuthMutualServiceImpl implements AuthService {

    /**
     * The Constant EXCEEDING_ITS_USER_LIMIT.
     */
    private static final String EXCEEDING_ITS_USER_LIMIT = " Exceeding its user limit.";

    /**
     * The customAuthorizingRealm.
     */
    private CustomAuthorizingRealm customAuthorizingRealm;

    /**
     * The oidc custom auth realm.
     */
    private OIDCCustomAuthRealm oidcCustomAuthRealm;

    /**
     * The su sshiro authentication.
     */
    private SuSshiroAuthentication suSshiroAuthentication;

    /**
     * userManager reference.
     */
    private UserManager userManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * authManager reference.
     */
    private AuthManager authManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * userTokenManager reference.
     */
    private UserTokenManager userTokenManager;

    /**
     * The web socket service.
     */
    private WSService webSocketService;

    /**
     * LOGIN_SUCCESFULL constant.
     */
    private static final String LOGIN_SUCCESFULL = "User Logged in successfully";

    /**
     * PROBLEM_SIGNING_IN constant.
     */
    private static final String PROBLEM_SIGNING_IN = "Problem Signing in";

    /**
     * TOKEN_KEY constant.
     */
    private static final String TOKEN_KEY = "token";

    private static final String USERINFO_NULL_MESSAGE = "UserInfo token validation failed redirected to login again";

    /**
     * User-related constants
     */
    public static final String NAME = "name";

    public static final String GIVEN_NAME = "given_name";

    public static final String FAMILY_NAME = "family_name";

    public static final String PICTURE = "picture";

    public static final String EMAIL = "email";

    public static final String ACCESS_TOKEN = "accessToken";

    public static final String REFRESH_TOKEN = "refreshToken";

    public static final String UID = "uid";

    public static final String PASSWORD = "password";

    public static final String DEPARTMENT = "departmentnumber";

    public static final String DESIGNATION = "employeetype";

    /**
     * Inits the.
     */
    public void init() {

        Thread t = new Thread( () -> {
            while ( permissionManager == null ) {
                try {
                    Thread.sleep( 1000 );
                } catch ( InterruptedException e ) {
                    log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
                }
            }
            Collection< Session > sessions = suSshiroAuthentication.getActiveSessions();
            // setting default realm. API calls may overWrite this
            suSshiroAuthentication.setRealm( customAuthorizingRealm );
            if ( CollectionUtils.isNotEmpty( sessions ) ) {
                sessions.forEach( session -> addSessionAsObject( session ) );
            }
        } );
        t.start();

    }

    private void addSessionAsObject( Session session ) {
        try {
            if ( session.getAttribute( DefaultSubjectContext.PRINCIPALS_SESSION_KEY ) != null ) {
                SimplePrincipalCollection p = ( SimplePrincipalCollection ) session.getAttribute(
                        DefaultSubjectContext.PRINCIPALS_SESSION_KEY );
                UserDTO userDTO = ( UserDTO ) p.getPrimaryPrincipal();
                if ( null != userDTO.getPassword() ) {
                    PasswordUtils.addToPasswordMap( userDTO.getId(), userDTO.getPassword() );
                }
            }
        } catch ( Exception e ) {
            log.error( "On AuthBundle Start Error :active session reading Failed : " + e.getMessage(), e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response authenticationWithOauth( String id_token, String x_oauth_token, String refreshToken, String state, String json ) {
        UserDTO user = null;
        Subject subject = null;
        try {
            user = JsonUtils.jsonToObject( json, UserDTO.class );
            String uid = user.getUserUid();
            String password = user.getPassword();
            if ( uid == null || password == null ) {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) );
            }
            synchronized ( uid.intern() ) {
                suSshiroAuthentication.setRealm( oidcCustomAuthRealm );
                List< UserTokenEntity > userTokenEntityList = userTokenManager.getActiveTokenOrRunningJobByIpAddressAndUserUid(
                        userTokenManager.getIpAddress( PhaseInterceptorChain.getCurrentMessage() ), uid );
                // check if user already logged in from same system
                if ( userTokenEntityList != null && !userTokenEntityList.isEmpty() ) {
                    for ( UserTokenEntity userTokenEntity : userTokenEntityList ) {
                        var getLatestUserFromDb = getLatestUserFromDatabase( userTokenEntity.getUserEntity().getId().toString() );
                        subject = getSubjectByTokenFromActiveSessions( userTokenEntity.getToken() );
                        if ( subject == null ) {
                            userTokenEntity.setExpired( true );
                            userTokenManager.updateUserToken( userTokenEntity );
                        } else if ( userTokenEntity.getRunningJob() && ConstantsStatus.ACTIVE.equals( getLatestUserFromDb.getStatus() ) ) {
                            UserDTO sessionUser = getUserByTokenFromActiveSession( userTokenEntity.getToken() );
                            if ( sessionUser != null && userTokenEntity.getUserEntity() != null
                                    && userManager.validateUserPasswordInDatabase( uid, password ) ) {
                                user = sessionUser;
                                if ( userTokenEntity.isExpired() ) {
                                    Date currentTime = new Date();
                                    userTokenEntity.setLastRequestTime( currentTime );
                                    userTokenEntity.setExpiryTime(
                                            DateUtils.addMinutes( currentTime, Integer.parseInt( PropertiesManager.getSessionExpiry() ) ) );
                                    userTokenEntity.setExpired( false );
                                    userTokenManager.updateUserToken( userTokenEntity );
                                }
                                break;
                            }

                        }
                    }
                }
                if ( subject == null || user.getToken() == null ) {
                    // attempting login to shiro
                    subject = suSshiroAuthentication.login( new Subject.Builder().buildSubject(),
                            new UsernamePasswordToken( user.getUserUid(), user.getPassword().toCharArray() ) );
                    user = ( UserDTO ) subject.getPrincipal();
                    SuscoreSession shiroEnt = suSshiroAuthentication.getResultByShiroId( subject.getSession().getId() );
                    shiroEnt.setToken( user.getToken() );
                    shiroEnt.setUserId( user.getId() );
                    shiroEnt.setOauth2TokenId( CompressionUtils.compress( id_token ) );
                    shiroEnt.setOauthOriginalToken( x_oauth_token );
                    shiroEnt.setRefreshToken( refreshToken );
                    shiroEnt.setWenLogin( true );
                    suSshiroAuthentication.update( shiroEnt );
                    // Setting Cookies

                    // Access Token Cookie
                    NewCookie accessTokenCookie = new NewCookie( "accessToken", x_oauth_token, "/", null, null, 3600, true, true );
                    // Refresh Token Cookie
                    NewCookie refreshTokenCookie = new NewCookie( "refreshToken", refreshToken, "/", null, null, 7 * 24 * 3600, true,
                            true );

                    if ( subject.isAuthenticated() ) {
                        if ( !user.isChangable() ) {
                            if ( validateUserDirectoriesAndDataLicence( user, password ) ) {
                                return logoutUserIfDataLicenseNotAssigned( user, subject );
                            }
                            String failureMsg = validateTokenLicenseSlots(
                                    prepareSessionDTO( shiroEnt, getLatestUserFromDatabase( user.getId() ) ) );

                            if ( failureMsg != null ) {
                                return handleLoginFailure( user, subject, failureMsg );
                            }
                            PasswordUtils.addToPasswordMap( user.getId(), user.getPassword() );
                            // cb2LoginForAuthUsers( user, uid, password );
                        }
                        return ResponseUtils.success( AuthServiceImpl.LOGIN_SUCCESFULL,
                                prepareLoginDTOResponseForWEN( user, user.getToken(), state, user.isChangable() ) );
                    } else {
                        return ResponseUtils.failure( AuthServiceImpl.PROBLEM_SIGNING_IN );
                    }
                } else {
                    PasswordUtils.addToPasswordMap( user.getId(), user.getPassword() );
                    // cb2LoginForAuthUsers( user, uid, password );
                    return ResponseUtils.success( AuthServiceImpl.LOGIN_SUCCESFULL,
                            prepareLoginDTOResponseForWEN( user, user.getToken(), state, user.isChangable() ) );
                }
            }
        } catch ( ExcessiveAttemptsException | SusException e ) {
            log.error( e.getMessage(), e );
            if ( user != null ) {
                logoutUserSessionsWithUserUid( user.getUserUid(), subject );
            }
            return ResponseUtils.failure( e.getMessage() );
        } catch ( AuthenticationException e ) {
            log.error( e.getMessage(), e );
            if ( e.getCause() instanceof SusException ) {
                return ResponseUtils.failure( e.getCause().getMessage() );
            } else {
                return ResponseUtils.failure( e.getMessage() );
            }
        } catch ( Exception e ) {
            // org.apache.shiro.authc.IncorrectCredentialsException will be caught by this block
            log.error( e.getMessage(), e );
            return ResponseUtils.failure( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response registerUserAfterOAuth( String id_token, String x_oauth_token, String refreshToken, String state, String json,
            JsonNode userInfoNode, SuSUserDirectoryEntity suSUserDirectoryEntity ) {
        UserDTO user = null;
        try {
            // Validate the OAuth token information.
            if ( userInfoNode == null || userInfoNode.isEmpty() ) {
                // Redirect the client if token validation fails.
                log.warn( USERINFO_NULL_MESSAGE );
                return Response.status( 302 ).location( new URI( PropertiesManager.getWebBaseURL() ) ).build();
            }

            // Convert the JSON payload into a UserDTO.
            user = JsonUtils.jsonToObject( json, UserDTO.class );

            if ( user == null ) {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) );
            }

            // Safely checking for null values before stripping the strings
            String qNumber = ( user.getUserUid() != null ) ? user.getUserUid().strip() : "";
            String password = ( user.getPassword() != null ) ? user.getPassword().strip()
                    : ""; // Currently for oauth user the userUid is treated as password

            if ( StringUtils.isNullOrEmpty( qNumber ) || StringUtils.isNullOrEmpty( password ) ) {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) );
            }

            //! Checking if a user already exists for this UID.
            UserEntity existingUser = userManager.getUserByUid( qNumber );
            if ( existingUser == null ) {

                // create a new user if not no user is found
                UserDTO userDTO = prepareUserDTOFromUserInfoNode( userInfoNode, new UserDTO(), qNumber, x_oauth_token, password );

                UserDTO userDtoToReturn = userManager.createInactiveUserForOAuth( userDTO, suSUserDirectoryEntity );
                // Setting Cookie
                // Access Token Cookie
                NewCookie accessTokenCookie = new NewCookie( ACCESS_TOKEN, x_oauth_token, "/", null, null, 3600, true, true );
                // Refresh Token Cookie
                NewCookie refreshTokenCookie = new NewCookie( REFRESH_TOKEN, refreshToken, "/", null, null, 7 * 24 * 3600, true, true );
                if ( userDtoToReturn != null ) {
                    NewCookie[] cookieArray = new NewCookie[ 2 ];
                    cookieArray[ 0 ] = accessTokenCookie;
                    cookieArray[ 1 ] = refreshTokenCookie;
                    return ResponseUtils.successResponseWithCookies(
                            MessageBundleFactory.getDefaultMessage( Messages.INACTIVE_USER_CREATION_MESSAGE.getKey() ), false, cookieArray,
                            null );
                } else {
                    return ResponseUtils.failure(
                            MessageBundleFactory.getDefaultMessage( Messages.USER_CREATION_FAILURE_MESSAGE.getKey() ) );
                }
            } else {
                // If the user already exists, delegate to the standard authenticationWithOauth flow.
                log.info( MessageBundleFactory.getDefaultMessage( Messages.ALREADY_REGISTERED_MESSAGE.getKey() ) );
                String updatedPassword = ( existingUser.getPassword() != null ) ? existingUser.getPassword().strip() : password;
                String jsonPayload = String.format( "{\"" + UID + "\":\"%s\",\"" + PASSWORD + "\":\"%s\"}", qNumber,
                        updatedPassword );
                return authenticationWithOauth( id_token, x_oauth_token, refreshToken, state, jsonPayload );
            }
        } catch ( Exception e ) {
            log.error( "Error in registerUserWithOauth: " + e.getMessage(), e );
            return ResponseUtils.failure( MessageBundleFactory.getDefaultMessage( Messages.USER_CREATION_FAILURE_MESSAGE.getKey() ) );
        }
    }

    private UserDTO prepareUserDTOFromUserInfoNode( JsonNode userInfoNode, UserDTO userDTO, String userID, String token,
            String userPassword ) {
        userDTO.setUserUid( userID );
        // GETTING NAME FOR BOTH PROVIDER TYPES
        String[] nameArray = userInfoNode.get( NAME ).asText().split( "\\s+" );
        if ( userInfoNode.get( GIVEN_NAME ) != null ) {
            userDTO.setFirstName( userInfoNode.get( GIVEN_NAME ).asText() );
        } else if ( nameArray.length > 1 ) {
            userDTO.setFirstName( nameArray[ 0 ] );
        }
        if ( userInfoNode.get( FAMILY_NAME ) != null ) {
            userDTO.setSurName( userInfoNode.get( FAMILY_NAME ).asText() );
        } else if ( nameArray.length > 2 ) {
            userDTO.setSurName( nameArray[ 1 ] );
        }
        userDTO.setEditable( true );
        userDTO.setPassword( userPassword ); // currently for oauth user the password is the userId itself
        userDTO.setGroups( null );
        userDTO.setName( userInfoNode.get( NAME ).asText() );
        UserDetail userDetail = new UserDetail();
        userDetail.setEmail( userInfoNode.path( EMAIL ).asText() );
        if ( userInfoNode.get( DEPARTMENT ) != null ) {
            userDetail.setDepartment( userInfoNode.path( DEPARTMENT ).asText() );
        }
        if ( userInfoNode.get( DESIGNATION ) != null ) {
            userDetail.setDesignation( userInfoNode.path( DESIGNATION ).asText() );
        }
        List< UserDetail > list = List.of( userDetail );
        userDTO.setUserDetails( list );
        userDTO.setStatus( ConstantsStatus.INACTIVE );
        userDTO.setRestricted( ConstantsStatus.YES );
        userDTO.setType( UserDTO.USER_TYPE_RESTRICTED );
        userDTO.setToken( token );
        userDTO.setTheme( UIThemeEnums.SYSTEM.getName() );
        userDTO.setChangable( true );
        return userDTO;
    }

    private boolean validateUserDirectoriesAndDataLicence( UserDTO user, String password ) {
        // create staging user directory
        SuSStagingUtils.createUserDirectoryInStaging( user.getUserUid() );
        //create temp user directory
        FileUtils.createUserDirectoryInTemp( user.getUserUid() );
        user.setPassword( password );
        return !user.getId().equalsIgnoreCase( ConstantsID.SUPER_USER_ID ) && !dataLicenseExistsOfLoginUser( user );
    }

    private Response logoutUserIfDataLicenseNotAssigned( UserDTO user, Subject subject ) {
        authManager.logout( user.getToken() );
        suSshiroAuthentication.logout( subject );
        return ResponseUtils.failure( "Data license is not assigned to this user." );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response authenticate( String json ) {
        UserDTO user = null;
        Subject subject = null;
        try {
            user = JsonUtils.jsonToObject( json, UserDTO.class );
            String uid = user.getUserUid();
            String password = user.getPassword();
            if ( uid == null || password == null ) {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) );
            }
            synchronized ( uid.intern() ) {
                suSshiroAuthentication.setRealm( customAuthorizingRealm );
                List< UserTokenEntity > userTokenEntityList = userTokenManager.getActiveTokenOrRunningJobByIpAddressAndUserUid(
                        userTokenManager.getIpAddress( PhaseInterceptorChain.getCurrentMessage() ), uid );
                // check if user already loged in from same system
                if ( userTokenEntityList != null && !userTokenEntityList.isEmpty() ) {
                    for ( UserTokenEntity userTokenEntity : userTokenEntityList ) {
                        var getLatestUserFromDB = getLatestUserFromDatabase( userTokenEntity.getUserEntity().getId().toString() );
                        subject = getSubjectByTokenFromActiveSessions( userTokenEntity.getToken() );
                        if ( subject == null ) {
                            userTokenEntity.setExpired( true );
                            userTokenManager.updateUserToken( userTokenEntity );
                        } else if ( userTokenEntity.getRunningJob() && ConstantsStatus.ACTIVE.equals( getLatestUserFromDB.getStatus() ) ) {
                            UserDTO sessionUser = getUserByTokenFromActiveSession( userTokenEntity.getToken() );
                            if ( sessionUser != null && userTokenEntity.getUserEntity() != null
                                    && userManager.validateUserPasswordInDatabase( uid, password ) ) {
                                user = sessionUser;
                                if ( userTokenEntity.isExpired() ) {
                                    Date currentTime = new Date();
                                    userTokenEntity.setLastRequestTime( currentTime );
                                    userTokenEntity.setExpiryTime(
                                            DateUtils.addMinutes( currentTime, Integer.parseInt( PropertiesManager.getSessionExpiry() ) ) );
                                    userTokenEntity.setExpired( false );
                                    userTokenManager.updateUserToken( userTokenEntity );
                                }
                                break;
                            }

                        }
                    }
                }
                if ( subject == null || user.getToken() == null ) {
                    // attempting login to shiro
                    subject = suSshiroAuthentication.login( new Subject.Builder().buildSubject(),
                            new UsernamePasswordToken( user.getUserUid(), user.getPassword().toCharArray() ) );
                    user = ( UserDTO ) subject.getPrincipal();
                    SuscoreSession shiroEnt = suSshiroAuthentication.getResultByShiroId( subject.getSession().getId() );
                    shiroEnt.setToken( user.getToken() );
                    shiroEnt.setUserId( user.getId() );
                    shiroEnt.setWenLogin( false );
                    suSshiroAuthentication.update( shiroEnt );
                    if ( subject.isAuthenticated() ) {
                        if ( !user.isChangable() ) {
                            // create staging user directory
                            if ( validateUserDirectoriesAndDataLicence( user, password ) ) {
                                return logoutUserIfDataLicenseNotAssigned( user, subject );
                            }
                            String failureMsg = validateTokenLicenseSlots(
                                    prepareSessionDTO( shiroEnt, getLatestUserFromDatabase( user.getId() ) ) );

                            if ( failureMsg != null ) {
                                return handleLoginFailure( user, subject, failureMsg );
                            }
                            PasswordUtils.addToPasswordMap( user.getId(), user.getPassword() );
                            String cb2LoginMessage = cb2LoginForAuthUsers( user, uid, password );
                            if ( cb2LoginMessage != null ) {
                                return handleLoginFailure( user, subject, cb2LoginMessage );
                            }

                        }
                        return ResponseUtils.success( AuthServiceImpl.LOGIN_SUCCESFULL,
                                prepareLoginDTOResponse( user, user.getToken(), user.isChangable() ) );
                    } else {
                        return ResponseUtils.failure( AuthServiceImpl.PROBLEM_SIGNING_IN );
                    }
                } else {
                    licenseManager.addUserInTokenizedLicenseMap( user.getToken(),
                            prepareSessionDTO( suSshiroAuthentication.getResultByShiroId( subject.getSession().getId() ),
                                    getLatestUserFromDatabase( user.getId() ) ) );
                    PasswordUtils.addToPasswordMap( user.getId(), user.getPassword() );
                    String cb2LoginMessage = cb2LoginForAuthUsers( user, uid, password );
                    if ( cb2LoginMessage != null ) {
                        return handleLoginFailure( user, subject, cb2LoginMessage );
                    }
                    return ResponseUtils.success( AuthServiceImpl.LOGIN_SUCCESFULL,
                            prepareLoginDTOResponse( user, user.getToken(), user.isChangable() ) );
                }
            }
        } catch ( ExcessiveAttemptsException | SusException e ) {
            log.error( e.getMessage(), e );
            if ( user != null ) {
                logoutUserSessionsWithUserUid( user.getUserUid(), subject );
            }
            return ResponseUtils.failure( e.getMessage() );
        } catch ( AuthenticationException e ) {
            log.error( e.getMessage(), e );
            if ( e.getCause() instanceof SusException ) {
                return ResponseUtils.failure( e.getCause().getMessage() );
            } else {
                return ResponseUtils.failure( e.getMessage() );
            }
        } catch ( Exception e ) {
            // org.apache.shiro.authc.IncorrectCredentialsException will be caught by this block
            log.error( e.getMessage(), e );
            return ResponseUtils.failure( e.getMessage() );
        }
    }

    /**
     * Handle login failure.
     *
     * @param user
     *         the user
     * @param subject
     *         the subject
     * @param failureMsg
     *         the failure msg
     *
     * @return the response
     */
    private Response handleLoginFailure( UserDTO user, Subject subject, String failureMsg ) {
        licenseManager.removeUserFromTokenizedLicenseMap( user.getToken() );
        authManager.logoutWithoutCheckingRunningJobs( user.getToken() );
        suSshiroAuthentication.logout( subject );
        return ResponseUtils.failure( failureMsg );
    }

    /**
     * Get latest user from database user dto.
     *
     * @param userId
     *         the user id
     *
     * @return the user dto
     */
    private UserDTO getLatestUserFromDatabase( String userId ) {
        return userManager.getUserById( userId, UUID.fromString( userId ) );
    }

    /**
     * Prepare session DTO.
     *
     * @param sessionEntity
     *         the session entity
     * @param user
     *         the user
     *
     * @return the su S core session DTO
     */
    private SuSCoreSessionDTO prepareSessionDTO( SuscoreSession sessionEntity, UserDTO user ) {

        SuSCoreSessionDTO sessionDTO = new SuSCoreSessionDTO();
        sessionDTO.setSession( ByteUtil.convertByteToString( sessionEntity.getSession() ) );
        sessionDTO.setOauth2TokenId( ByteUtil.convertByteToString( sessionEntity.getOauth2TokenId() ) );
        sessionDTO.setRefreshToken( sessionEntity.getRefreshToken() );
        sessionDTO.setOauthOriginalToken( sessionEntity.getOauthOriginalToken() );
        sessionDTO.setToken( sessionEntity.getToken() );
        sessionDTO.setWenLogin( sessionEntity.isWenLogin() );
        sessionDTO.setUser( user );
        return sessionDTO;
    }

    /**
     * Cb 2 login for auth users.
     *
     * @param user
     *         the user
     * @param uid
     *         the uid
     * @param password
     *         the password
     */
    private String cb2LoginForAuthUsers( UserDTO user, String uid, String password ) {
        try {
            if ( !uid.equals( SpecifiedUser.SUPER_USER.getUser() ) && licenseManager.isFeatureAllowedToUser(
                    SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(), user.getId() ) && ( PropertiesManager.loginToCB2()
                    && isUserAuthenticateInLdap( uid, password ) ) ) {
                Thread thread = new Thread( () -> {
                    // login into cb2
                    try {
                        ProcessResult result = PythonUtils.CB2LoginByPython( uid, password,
                                String.valueOf( Cb2OperationEnum.CB2_LOGIN_OR_KEEP_ALIVE_SESSION.getKey() ),
                                userTokenManager.getRefreshTokenFromCurrentSessionUsingUid( uid ) );
                        log.info( "Login : CB2 Login >" + result.toString() );
                    } catch ( Exception e ) {
                        log.error( "Cb2 Login Failed : " + e.getMessage(), e );
                    }
                } );
                thread.start();
            }

            return null;
        } catch ( IllegalStateException e ) {
            String errorMessage = MessageBundleFactory.getMessage( Messages.USER_COULD_NOT_BE_AUTHENTICATED_IN_LDAP.getKey(), uid ) + " "
                    + MessageBundleFactory.getMessage( Messages.REMOVE_CB2_CONNECTOR_LICENSE_TO_LOGIN.getKey() );
            log.error( errorMessage, e );
            return errorMessage;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response cb2Login( String authToken ) {
        UserEntity user = userTokenManager.getUserEntityByToken( authToken );
        String userId = user.getId().toString();
        String password = PasswordUtils.getPasswordById( userId );
        String uid = user.getUserUid();
        log.debug( "Cb2 Login Retry Api called for user : " + uid );
        if ( !uid.equals( SpecifiedUser.SUPER_USER.getUser() ) && licenseManager.isFeatureAllowedToUser(
                SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(), userId ) ) {
            try {
                ValidationUtils.validateCB2AccessWithWENLoginByToken( authToken );
                if ( PropertiesManager.loginToCB2() && isUserAuthenticateInLdap( uid, password ) ) {

                    ProcessResult result = PythonUtils.CB2LoginByPython( uid, password,
                            String.valueOf( Cb2OperationEnum.CB2_LOGIN_OR_KEEP_ALIVE_SESSION.getKey() ),
                            userTokenManager.getRefreshTokenFromCurrentSessionUsingUid( uid ) );
                    log.info( "Login Retry: CB2 Login >" + result.toString() );
                }
            } catch ( Exception e ) {
                log.error( "Cb2 Login Retry api : " + e.getMessage(), e );
            }
        }
        return ResponseUtils.success( "Cb2 Loged In", true );
    }

    @Override
    public Response cb2LoginViaRest( String oidcToken, String refreshToken, String uid ) {
        try {
            // checks if there is a linked user account with the uid
            UserEntity userEntity = userManager.getUserByUid( uid );
            if ( userEntity != null ) {
                // checks if there is a shiro session linked to the user
                List< UserTokenEntity > userTokenEntityList = userTokenManager.getActiveTokenOrRunningJobByIpAddressAndUserUid(
                        userTokenManager.getIpAddress( PhaseInterceptorChain.getCurrentMessage() ), uid );
                if ( userTokenEntityList != null && !userTokenEntityList.isEmpty() ) {
                    for ( UserTokenEntity userTokenEntity : userTokenEntityList ) {
                        UserDTO getLatestUserFromDb = getLatestUserFromDatabase( userTokenEntity.getUserEntity().getId().toString() );
                        if ( !userTokenEntity.isExpired() ) {
                            Subject subject = getSubjectByTokenFromActiveSessions( userTokenEntity.getToken() );
                            if ( subject == null ) {
                                return ResponseUtils.failure(
                                        MessageBundleFactory.getMessage( Messages.USER_SESSION_EXPIRED_LOGIN_AGAIN.getKey() ) );
                            }
                        }
                    }
                }
            } else {
                log.error( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_MATCHING_UID.getKey() ) );
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_MATCHING_UID.getKey() ) );
            }
            ProcessResult processResult = new ProcessResult();
            if ( licenseManager.isFeatureAllowedToUser( SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(), uid )
                    && PropertiesManager.loginToCB2() ) {
                // updating old reference of refreshToken for user with session
                userTokenManager.updateOldRefreshTokenInSuscoreSessionEntityUsingUserUid( refreshToken, uid );
                // use python utils to call the script to initiate cb2 login
                processResult = PythonUtils.CB2LoginByPythonUsingOIDCToken( oidcToken, uid,
                        String.valueOf( Cb2OperationEnum.CB2_LOGIN_OR_KEEP_ALIVE_SESSION.getKey() ) );
            }
            int exitCode = processResult.getExitValue();
            if ( exitCode != 0 ) {
                log.info( MessageBundleFactory.getMessage( Messages.CB2_LOGIN_VIA_REST_FAILED.getKey() ) );
                return Response.status( 302 ).location( new URI( PropertiesManager.getWebBaseURL() ) ).build();
            }
            // Currently user is redirected to the WebBaseURL after successful login
            return Response.status( 302 ).location( new URI( PropertiesManager.getWebBaseURL() ) ).build();
        } catch ( Exception e ) {
            return ResponseUtils.failure( e.getMessage() );
        }
    }

    /**
     * Validate token license slots.
     *
     * @param user
     *         the user
     *
     * @return the response
     */
    private String validateTokenLicenseSlots( SuSCoreSessionDTO sessionDTO ) {

        if ( sessionDTO.getUser().getId().equals( ConstantsID.SUPER_USER_ID ) && sessionDTO.getToken() != null ) {
            licenseManager.addUserInTokenizedLicenseMap( sessionDTO.getToken(), sessionDTO );
            return null;
        }
        // check if token license is available then ignore the named license
        // then add Entry to LicenseToken Map only for normal user
        if ( licenseManager.isTokenBasedLicenseExists() ) {
            // validate all available license and allowed user
            int wfUserLicenseConsume = 0;
            int AssemblyLicenseConsume = 0;
            int dataLicenseConsume = 0;
            int managerLicenseConsume = 0;
            int postLicenseConsume = 0;
            int cb2LicenseConsume = 0;
            int doeLicenseConsume = 0;
            int optimizationLicenseConsume = 0;

            FiltersDTO filtersDTO = new FiltersDTO();
            filtersDTO.setStart( 0 );
            filtersDTO.setLength( Integer.MAX_VALUE );

            List< UserTokenEntity > activeTokenList = userTokenManager.getActiveTokenOrInactiveTokenOnRunningJobByFilter( filtersDTO );
            for ( UserTokenEntity userTokenDTO : activeTokenList ) {
                if ( !TokenizedLicenseUtil.doesTokenExist( userTokenDTO.getToken() ) ) {
                    sessionDTO = licenseManager.addUserInTokenizedLicenseMap( userTokenDTO.getToken(), sessionDTO );
                }

                List< ModuleLicenseDTO > modulesListOfSingleUser = licenseManager.getLicenseUserEntityListByUserId(
                        userTokenDTO.getUserEntity().getId().toString() );
                for ( ModuleLicenseDTO modulesOfSingleUser : modulesListOfSingleUser ) {
                    if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DATA ) ) {
                        dataLicenseConsume++;
                    } else if ( modulesOfSingleUser.getModule()
                            .equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER ) ) {
                        managerLicenseConsume++;
                    } else if ( modulesOfSingleUser.getModule()
                            .equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_ASSEMBLY ) ) {
                        AssemblyLicenseConsume++;
                    } else if ( modulesOfSingleUser.getModule()
                            .equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_POST ) ) {
                        postLicenseConsume++;
                    } else if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_USER ) ) {
                        wfUserLicenseConsume++;
                    } else if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_CB2 ) ) {
                        cb2LicenseConsume++;
                    } else if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DOE ) ) {
                        doeLicenseConsume++;
                    } else if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_OPTIMIZATION ) ) {
                        optimizationLicenseConsume++;
                    }
                }

            }

            List< LicenseEntity > licenseEntities = licenseManager.getLicenseConfigManager().getModuleLicenseEntityList();
            for ( LicenseEntity licenseEntity : licenseEntities ) {

                if ( licenseEntity.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DATA )
                        && dataLicenseConsume > licenseEntity.getAllowedUsers() ) {
                    return licenseEntity.getModule() + AuthServiceImpl.EXCEEDING_ITS_USER_LIMIT;
                } else if ( licenseEntity.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER )
                        && managerLicenseConsume > licenseEntity.getAllowedUsers() ) {
                    return licenseEntity.getModule() + AuthServiceImpl.EXCEEDING_ITS_USER_LIMIT;
                } else if ( licenseEntity.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_ASSEMBLY )
                        && AssemblyLicenseConsume > licenseEntity.getAllowedUsers() ) {
                    return licenseEntity.getModule() + AuthServiceImpl.EXCEEDING_ITS_USER_LIMIT;
                } else if ( licenseEntity.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_POST )
                        && postLicenseConsume > licenseEntity.getAllowedUsers() ) {
                    return licenseEntity.getModule() + AuthServiceImpl.EXCEEDING_ITS_USER_LIMIT;
                } else if ( licenseEntity.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_USER )
                        && wfUserLicenseConsume > licenseEntity.getAllowedUsers() ) {
                    return licenseEntity.getModule() + AuthServiceImpl.EXCEEDING_ITS_USER_LIMIT;
                } else if ( licenseEntity.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_CB2 )
                        && cb2LicenseConsume > licenseEntity.getAllowedUsers() ) {
                    return licenseEntity.getModule() + AuthServiceImpl.EXCEEDING_ITS_USER_LIMIT;
                } else if ( licenseEntity.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DOE )
                        && doeLicenseConsume > licenseEntity.getAllowedUsers() ) {
                    return licenseEntity.getModule() + AuthServiceImpl.EXCEEDING_ITS_USER_LIMIT;
                } else if ( licenseEntity.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_OPTIMIZATION )
                        && optimizationLicenseConsume > licenseEntity.getAllowedUsers() ) {
                    return licenseEntity.getModule() + AuthServiceImpl.EXCEEDING_ITS_USER_LIMIT;
                }
            }
        }
        return null;
    }

    /**
     * Data license exists of login user.
     *
     * @param user
     *         the user
     *
     * @return the response
     */
    private boolean dataLicenseExistsOfLoginUser( UserDTO user ) {
        List< ModuleLicenseDTO > modulesListOfSingleUser = licenseManager.getLicenseUserEntityListByUserId( user.getId() );
        for ( ModuleLicenseDTO moduleLicenseDTO : modulesListOfSingleUser ) {
            if ( moduleLicenseDTO.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DATA ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Logout user with useruid.
     *
     * @param userUid
     *         the userUid
     */
    private void logoutUserSessionsWithUserUid( String userUid, Subject subject ) {
        try {

            // perform login attempt fail safe is working or not: Not tested yet

            if ( subject != null ) {
                UserDTO user = ( UserDTO ) subject.getPrincipal();
                userTokenManager.expireTokenWithoutCheckingRunningJobs( user.getToken() );
                UUID userId = userManager.getUserByUid( userUid ).getId();

                suSshiroAuthentication.logout( subject );
                PasswordUtils.removeFromPasswordMap( userId.toString() );
            }

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response logout( String json ) {
        try {
            String token = JsonUtils.getValue( json, AuthServiceImpl.TOKEN_KEY );
            UserDTO userDto = TokenizedLicenseUtil.getUser( token );

            webSocketService.unmonitorClient( token ); // *

            if ( authManager.logout( token ) ) {

                // check if tokenized license is available then remove entry
                // from map if Exists
                if ( licenseManager.isTokenBasedLicenseExists() && CollectionUtils.isEmpty(
                        userTokenManager.getJobTokenEntityByAuthToken( token ) ) ) {
                    licenseManager.removeUserFromTokenizedLicenseMap( token );
                    SuscoreSession sessionAlive = suSshiroAuthentication.getResultByToken( token );

                    if ( sessionAlive.getOauthOriginalToken() != null && sessionAlive.getOauth2TokenId() != null ) {
                        endOauthSession( sessionAlive.getOauthOriginalToken(), sessionAlive.getRefreshToken(),
                                CompressionUtils.decompress( sessionAlive.getOauth2TokenId() ) );
                    }

                    Subject subject = getSubjectByTokenFromActiveSessions( sessionAlive, token );
                    if ( null != subject ) {
                        suSshiroAuthentication.logout( subject );
                    }
                    cb2Logout( userDto, token );

                } else {
                    UserTokenEntity userTokenEntity = userTokenManager.getUserTokenEntityByToken( token );
                    if ( userTokenEntity != null ) {
                        userTokenEntity.setRunningJob( true );
                        userTokenManager.updateUserToken( userTokenEntity );
                    }
                }

                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.USER_LOGGED_OUT_SUCCESSFULLY.getKey() ), true );
            } else {
                if ( CollectionUtils.isEmpty( userTokenManager.getJobTokenEntityByAuthToken( token ) ) ) {
                    cb2Logout( userDto, token );
                }
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.USER_IS_ALREADY_LOGGED_OUT.getKey() ), true );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the subject by token from active sessions.
     *
     * @param token
     *         the token
     *
     * @return the subject by token from active sessions
     */
    public Subject getSubjectByTokenFromActiveSessions( String token ) {
        SuscoreSession sessionAlive = suSshiroAuthentication.getResultByToken( token );
        if ( sessionAlive != null && null != sessionAlive.getSession() ) {
            Session session = CompressionUtils.deserialize( sessionAlive.getSession() );
            SimplePrincipalCollection p = ( SimplePrincipalCollection ) session.getAttribute(
                    DefaultSubjectContext.PRINCIPALS_SESSION_KEY );
            UserDTO userDTO = ( UserDTO ) p.getPrimaryPrincipal();
            // delete principal from here
            if ( userDTO.getToken().equals( token ) ) {
                return new Subject.Builder().sessionId( session.getId() ).buildSubject();
            }
        }
        return null;
    }

    /**
     * Gets the user by token from active sessions.
     *
     * @param token
     *         the token
     *
     * @return the user by token from active sessions
     */
    public UserDTO getUserByTokenFromActiveSession( String token ) {
        SuscoreSession sessionAlive = suSshiroAuthentication.getResultByToken( token );
        if ( sessionAlive != null && null != sessionAlive.getSession() ) {
            Session session = CompressionUtils.deserialize( sessionAlive.getSession() );
            SimplePrincipalCollection p = ( SimplePrincipalCollection ) session.getAttribute(
                    DefaultSubjectContext.PRINCIPALS_SESSION_KEY );
            return ( UserDTO ) p.getPrimaryPrincipal();
        }
        return null;
    }

    /**
     * Gets the info.
     *
     * @param xAuthToken
     *         the x auth token
     *
     * @return the info
     */
    private JsonNode getInfo( String xAuthToken ) {
        log.info( "getting token info from OAuth" );
        JsonNode jsonNode = null;
        try {
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( "authorization", "Bearer " + xAuthToken );
            CloseableHttpResponse closeableHttpResponse = SuSClient.getExternalRequest( PropertiesManager.webEamTokenInfo(),
                    requestHeaders );
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            jsonNode = JsonUtils.toJsonNode( responseString );
        } catch ( final Exception e ) {
            throw new SusException( e );
        }
        return jsonNode;
    }

    /**
     * Revoke token.
     *
     * @param infoJsonNode
     *         the info json node
     * @param refreshToken
     *         the refresh token
     *
     * @return the json node
     */
    private JsonNode revokeToken( JsonNode infoJsonNode, String refreshToken ) {
        log.info( "attempting to revoke OAuth Token" );
        JsonNode jsonNode = null;
        try {
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( "content-type", "application/x-www-form-urlencoded" );
            CloseableHttpResponse closeableHttpResponse = SuSClient.postExternalRequest( PropertiesManager.webEamTokenRevoke(),
                    "client_id=" + infoJsonNode.get( "client_id" ).asText() + "&token=" + refreshToken, requestHeaders );
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            jsonNode = JsonUtils.toJsonNode( responseString );
        } catch ( final Exception e ) {
            throw new SusException( e );
        }
        return jsonNode;
    }

    /**
     * End oauth session.
     *
     * @param xAuthToken
     *         the x auth token
     * @param refreshToken
     *         the refresh token
     * @param id_token
     *         the id token
     *
     * @return the boolean
     */
    private Boolean endOauthSession( String xAuthToken, String refreshToken, String id_token ) {
        try {
            log.info( "Attempting to logout user from OAuth" );
            JsonNode infoJsonNode = getInfo( xAuthToken );
            if ( infoJsonNode != null ) {
                revokeToken( infoJsonNode, refreshToken );
            }
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( "authorization", "Bearer " + xAuthToken );
            CloseableHttpResponse closeableHttpResponse = SuSClient.getExternalRequest(
                    getOAuth( Activator.WELL_KNOWNS_URL ).get( "end_session_endpoint" ).asText() + "?id_token_hint=" + id_token,
                    requestHeaders );
            return ( closeableHttpResponse.getStatusLine().getStatusCode() == 204 );
        } catch ( final Exception e ) {
            throw new SusException( e );
        }
    }

    /**
     * Gets the subject by token from active sessions.
     *
     * @param sessionAlive
     *         the session alive
     * @param token
     *         the token
     *
     * @return the subject by token from active sessions
     */
    public Subject getSubjectByTokenFromActiveSessions( SuscoreSession sessionAlive, String token ) {
        if ( sessionAlive != null ) {
            Session session = CompressionUtils.deserialize( sessionAlive.getSession() );
            SimplePrincipalCollection p = ( SimplePrincipalCollection ) session.getAttribute(
                    DefaultSubjectContext.PRINCIPALS_SESSION_KEY );
            UserDTO userDTO = ( UserDTO ) p.getPrimaryPrincipal();
            // delete principal from here
            if ( userDTO.getToken().equals( token ) ) {
                return new Subject.Builder().sessionId( session.getId() ).buildSubject();
            }
        }
        return null;

    }

    /**
     * Gets the subject password from active sessions.
     *
     * @param subject
     *         the subject
     *
     * @return the subject password from active sessions
     */
    public String getSubjectPasswordFromActiveSessions( Subject subject ) {
        if ( subject != null ) {
            UserDTO userDTO = ( UserDTO ) subject.getPrincipal();
            if ( userDTO != null && null != userDTO.getPassword() ) {
                return userDTO.getPassword();
            }
        }
        return null;
    }

    /**
     * Cb 2 logout.
     *
     * @param userDto
     *         the user dto
     * @param token
     *         the token
     */
    private void cb2Logout( UserDTO userDto, String token ) {

        if ( userDto != null && licenseManager.isFeatureAllowedToUser( SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(), userDto.getId() )
                && PropertiesManager.loginToCB2() ) {
            Thread thread = new Thread( () -> {
                ValidationUtils.validateCB2AccessWithWENLoginByToken( token );
                // logout from cb2
                try {
                    PythonUtils.CB2LoginByPython( userDto.getUserUid(), userDto.getPassword(),
                            String.valueOf( Cb2OperationEnum.CB2_LOGOUT.getKey() ),
                            userTokenManager.getRefreshTokenFromCurrentSessionUsingUid( userDto.getUserUid() ) );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                }
            } );
            thread.start();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.authentication.service.rest.AuthService#
     * expireJobToken(java.lang.String)
     */
    @Override
    public Response expireJobToken( String json ) {
        try {
            String token = JsonUtils.getValue( json, AuthServiceImpl.TOKEN_KEY );
            JobTokenEntity jobTokenEntity = userTokenManager.getJobTokenEntityByJobToken( token );
            jobTokenEntity.setExpired( true );
            userTokenManager.updateJobTokenEntity( jobTokenEntity );

            UserTokenEntity userTokenEntity = userTokenManager.getUserTokenEntityByToken( jobTokenEntity.getAuthToken() );

            if ( CollectionUtils.isEmpty( userTokenManager.getJobTokenEntityByAuthToken( jobTokenEntity.getAuthToken() ) ) ) {
                userTokenEntity.setRunningJob( false );
                userTokenManager.updateUserToken( userTokenEntity );
            }

            return ResponseUtils.success( "Job expired successfully!" );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateUserPassword( String userId, String token, String json ) {
        try {

            try {
                licenseManager.removeUserFromTokenizedLicenseMap( token );
                authManager.logoutWithoutCheckingRunningJobs( token );
            } catch ( Exception e ) {
                ResponseUtils.failure( "token did not got expire on change password : " + e.getMessage() );
            }

            UserPasswordDTO userPasswordDTO = JsonUtils.jsonToObject( json, UserPasswordDTO.class );
            return ResponseUtils.success( userManager.updateUserPassword( userId, userPasswordDTO ), true, true );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response prepareJobToken( String userId, String uid, String jobId, String authToken ) {
        try {
            return ResponseUtils.success(
                    authManager.prepareJobToken( userId, uid, jobId, PhaseInterceptorChain.getCurrentMessage(), authToken ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * Prepare Login DTO response.
     *
     * @param user
     *         the user
     * @param token
     *         the token
     * @param isChangeable
     *         the is changeable
     *
     * @return the Login DTO
     */
    private LoginDTO prepareLoginDTOResponse( UserDTO user, String token, boolean isChangeable ) {
        LoginDTO loginDTO = new LoginDTO();
        user = getLatestUserFromDatabase( user.getId() );
        loginDTO.setUserName( user.getName() );
        loginDTO.setUserId( user.getId() );
        loginDTO.setxAuthToken( token );
        loginDTO.setBaseUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_BASE_URL ) );
        loginDTO.setDocumentationUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_DOCS_URL ) );
        loginDTO.setChangeable( isChangeable );
        if ( null != user.getProfilePhoto() ) {
            loginDTO.setProfilePhoto(
                    CommonUtils.getBaseUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_BASE_URL ) )
                            + File.separator + ConstantsString.STATIC_PATH + user.getProfilePhoto().getPath() + File.separator
                            + user.getProfilePhoto().getName() );
        }
        loginDTO.setTheme( user.getTheme() == null ? UIThemeEnums.LIGHT.getName().toLowerCase() : user.getTheme().toLowerCase() );
        if ( !user.getId().equalsIgnoreCase( ConstantsID.SUPER_USER_ID ) && null != user.getUserDetails() && StringUtils.isNotNullOrEmpty(
                user.getUserDetails().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getLanguage() ) ) {
            loginDTO.setUserLanguage( user.getUserDetails().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getLanguage() );
        } else {
            loginDTO.setUserLanguage( MessageBundleFactory.EN_LOCALE );
        }
        return loginDTO;
    }

    /**
     * Prepare login DTO response for WEN.
     *
     * @param user
     *         the user
     * @param token
     *         the token
     * @param state
     *         the state
     * @param isChangeable
     *         the is changeable
     *
     * @return the login DTO
     */
    private LoginDTO prepareLoginDTOResponseForWEN( UserDTO user, String token, String state, boolean isChangeable ) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUserName( user.getName() );
        loginDTO.setUserId( user.getId() );
        loginDTO.setxAuthToken( token );
        loginDTO.setBaseUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_BASE_URL ) );
        loginDTO.setDocumentationUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_DOCS_URL ) );
        loginDTO.setChangeable( isChangeable );
        loginDTO.setUrl( state );
        if ( null != user.getProfilePhoto() ) {
            loginDTO.setProfilePhoto(
                    CommonUtils.getBaseUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_BASE_URL ) )
                            + File.separator + ConstantsString.STATIC_PATH + user.getProfilePhoto().getPath() + File.separator
                            + user.getProfilePhoto().getName() );
        }
        loginDTO.setTheme( user.getTheme() == null ? UIThemeEnums.LIGHT.getName().toLowerCase() : user.getTheme().toLowerCase() );
        return loginDTO;
    }

    private boolean isUserAuthenticateInLdap( String userUid, String password ) {
        return authManager.isUserAuthenticateInLdap( userUid, password );
    }

    /**
     * Gets the user manager.
     *
     * @return userManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * sets userManager.
     *
     * @param userManager
     *         the new user manager
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * Gets the auth manager.
     *
     * @return the auth manager
     */
    public AuthManager getAuthManager() {
        return authManager;
    }

    /**
     * * sets authManager.
     *
     * @param authManager
     *         the new auth manager
     */
    public void setAuthManager( AuthManager authManager ) {
        this.authManager = authManager;
    }

    /**
     * sets suSshiroAuthentication.
     *
     * @return the su sshiro authentication
     */
    public SuSshiroAuthentication getSuSshiroAuthentication() {
        return suSshiroAuthentication;
    }

    /**
     * gets suSshiroAuthentication.
     *
     * @param suSshiroAuthentication
     *         the new su sshiro authentication
     */
    public void setSuSshiroAuthentication( SuSshiroAuthentication suSshiroAuthentication ) {
        this.suSshiroAuthentication = suSshiroAuthentication;
    }

    /**
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Gets the license manager.
     *
     * @return the license manager
     */
    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    /**
     * Sets the license manager.
     *
     * @param licenseManager
     *         the new license manager
     */
    public void setLicenseManager( LicenseManager licenseManager ) {
        this.licenseManager = licenseManager;
    }

    /**
     * Gets the user token manager.
     *
     * @return the user token manager
     */
    public UserTokenManager getUserTokenManager() {
        return userTokenManager;
    }

    /**
     * Sets the user token manager.
     *
     * @param userTokenManager
     *         the new user token manager
     */
    public void setUserTokenManager( UserTokenManager userTokenManager ) {
        this.userTokenManager = userTokenManager;
    }

    /**
     * Gets the web socket service.
     *
     * @return the web socket service
     */
    public WSService getWebSocketService() {
        return webSocketService;
    }

    /**
     * Sets the web socket service.
     *
     * @param webSocketService
     *         the new web socket service
     */
    public void setWebSocketService( WSService webSocketService ) {
        this.webSocketService = webSocketService;
    }

    @Override
    public Response verifyUserByHeaderToken() {

        try {

            String userId = authManager.getUserIdByToken( getTokenFromGeneralHeader() ).toString();
            return ResponseUtils.success( authManager.verifyUserByHeaderToken( userId ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }

    }

    @Override
    public Response verifyUserByUserUid( String userUid ) {
        try {
            return ResponseUtils.success( Message.SUCCESS, authManager.getUserByUserUid( userUid ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }

    }

    public CustomAuthorizingRealm getCustomAuthorizingRealm() {
        return customAuthorizingRealm;
    }

    public void setCustomAuthorizingRealm( CustomAuthorizingRealm customAuthorizingRealm ) {
        this.customAuthorizingRealm = customAuthorizingRealm;
    }

    public OIDCCustomAuthRealm getOidcCustomAuthRealm() {
        return oidcCustomAuthRealm;
    }

    public void setOidcCustomAuthRealm( OIDCCustomAuthRealm oidcCustomAuthRealm ) {
        this.oidcCustomAuthRealm = oidcCustomAuthRealm;
    }

}