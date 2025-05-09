package de.soco.software.simuspace.suscore.auth.connect.ldap.authentication;

import javax.naming.NamingException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;

/**
 * The Interface LdapCustomAuthRealm.
 */
public interface LdapCustomAuthRealm {

    /**
     * Checks if is ldap connection established.
     *
     * @param contextFactory
     *         the context factory
     *
     * @return boolean
     */
    boolean isLdapConnectionEstablished( SusUserDirectoryAttributeDTO contextFactory );

    /**
     * Do get authentication info.
     *
     * @param token
     *         the token
     *
     * @return the authentication info
     */
    AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token ) throws AuthenticationException;

    /**
     * Do get authentication info.
     *
     * @param token
     *         the token
     *
     * @return the authentication info
     *
     * @throws AuthenticationException
     *         the authentication exception
     * @throws NamingException
     *         the naming exception
     */
    AuthenticationInfo getAuthenticationInfoFromLdap( AuthenticationToken token ) throws AuthenticationException, NamingException;

    /**
     * Check if User Uid Exist In Ldap directory
     *
     * @param uid
     *         the uid
     *
     * @return boolean
     */
    boolean lookUpUserUidInLdap( String uid );

    /**
     * Gets the case sensitive user uid from ldap.
     *
     * @param uid
     *         the uid
     * @param susUserDirectoryDTO
     *         the sus user directory DTO
     *
     * @return the case sensitive user uid from ldap
     */
    String getCaseSensitiveUserUidFromLdap( String uid, SuSUserDirectoryDTO susUserDirectoryDTO );

    /**
     * Get user firstname and surname from uid String[0] contains first name String[1] contains surname
     *
     * @param uid
     *         the uid
     *
     * @return names
     */
    String[] getUserFirstNameAndSurNameFromUid( String uid );

    /**
     * Gets the user first name and sur name from uid.
     *
     * @param uid
     *         the uid
     * @param directoryDTO
     *         the directory DTO
     *
     * @return the user first name and sur name from uid
     */
    String[] getUserFirstNameAndSurNameFromUid( String uid, SuSUserDirectoryDTO directoryDTO );

    /**
     * Gets the user email from uid.
     *
     * @param uid
     *         the uid
     *
     * @return the user email from uid
     */
    String getUserEmailFromUid( String uid );

    /**
     * Gets the user email from uid.
     *
     * @param uid
     *         the uid
     * @param directoryDTO
     *         the directory DTO
     *
     * @return the user email from uid
     */
    String getUserEmailFromUid( String uid, SuSUserDirectoryDTO directoryDTO );

}