package de.soco.software.simuspace.suscore.authentication.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import org.apache.cxf.message.Message;

import de.soco.software.simuspace.suscore.authentication.SuSshiroAuthentication;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.common.model.VerificationDTO;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;

/**
 * An interface which is responsible for creating the user token on authentication. it will communicate with the user token manager to
 * create the tokens.
 *
 * @author Zeeshan jamal
 */
public interface AuthManager {

    /**
     * A method for creating the user token on authentication for accessing APIs.
     *
     * @param user
     *         the user
     * @param currentMessage
     *         the current message
     *
     * @return token
     *
     * @apiNote To be used in service calls only
     */
    String prepareToken( UserDTO user, Message currentMessage );

    /**
     * Prepare job token.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param jobId
     *         the job id
     * @param currentMessage
     *         the current message
     * @param authToken
     *         the authToken
     *
     * @return the string
     *
     * @apiNote To be used in service calls only
     */
    String prepareJobToken( String userId, String userName, String jobId, Message currentMessage, String authToken );

    /**
     * A method used for logging out a user from sus.
     *
     * @param token
     *         the token
     *
     * @return true if the log out is successful.
     *
     * @apiNote To be used in service calls only
     */
    boolean logout( String token );

    /**
     * Logout without checking running jobs.
     *
     * @param token
     *         the token
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean logoutWithoutCheckingRunningJobs( String token );

    /**
     * Expire job token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return true, if successful
     */
    boolean expireJobToken( EntityManager entityManager, String token );

    /**
     * Gets the user token manager.
     *
     * @return the user token manager
     */
    UserTokenManager getUserTokenManager();

    /**
     * Gets the all active tokens.
     *
     * @return the all active tokens
     *
     * @apiNote To be used in service calls only
     */
    List< UserTokenDTO > getAllActiveTokens();

    /**
     * Gets the user id by token.
     *
     * @param token
     *         the token
     *
     * @return the user id by token
     *
     * @apiNote To be used in service calls only
     */
    UUID getUserIdByToken( String token );

    /**
     * Gets the su sshiro authentication.
     *
     * @return the su sshiro authentication
     */
    SuSshiroAuthentication getSuSshiroAuthentication();

    /**
     * Gets the user entityd by token.
     *
     * @param token
     *         the token
     *
     * @return the user entityd by token
     *
     * @apiNote To be used in service calls only
     */
    UserEntity getUserEntitydByToken( String token );

    /**
     * Gets the user entityd by job token.
     *
     * @param token
     *         the token
     *
     * @return the user entityd by token
     *
     * @apiNote To be used in service calls only
     */
    UserEntity getUserEntityByJobToken( String token );

    /**
     * Verify user by header token object.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object verifyUserByHeaderToken( String userIdFromGeneralHeader );

    /**
     * Is User Exist In Ldap Directory.
     *
     * @param token
     *         the token
     *
     * @return boolean boolean
     */
    boolean isUserAuthenticateInLdap( String uid, String password );

    /**
     * Gets user by user uid.
     *
     * @param userUid
     *         the user uid
     *
     * @return the user by user uid
     */
    VerificationDTO getUserByUserUid( String userUid );

}
