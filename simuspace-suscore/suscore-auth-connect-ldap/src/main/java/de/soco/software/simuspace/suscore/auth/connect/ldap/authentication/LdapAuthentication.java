package de.soco.software.simuspace.suscore.auth.connect.ldap.authentication;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

import de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.impl.LdapCustomAuthRealmImpl;

/**
 * The initiator for authenticating via shiro plugin using LDAP/AD This class can be replaced by the user or can be used by the user who is
 * interesting in accessing/connecting using LDAP/AD.
 *
 * @author fahad
 */

public class LdapAuthentication {

    /**
     * The ldap custom auth realm.
     */
    private static LdapCustomAuthRealmImpl ldapCustomAuthRealm;

    /**
     * The template.
     */
    private static String template;

    /**
     * The system username.
     */
    protected static String systemUsername = null;

    /**
     * The token.
     */
    protected static UsernamePasswordToken token;

    /**
     * The auth token.
     */
    protected static AuthenticationToken authToken;

    /**
     * The system password.
     */
    protected static String systemPassword = null;

    /**
     * The Constant LDAP_SYSTEM_USERNAME.
     */
    private static final String LDAP_SYSTEM_USERNAME = "cn=root,dc=thpc, dc=clus";

    /**
     * The Constant LDAP_SYSTEM_PASSWORD.
     */
    private static final String LDAP_SYSTEM_PASS = "simstream";

    /**
     * The Constant LDAP_SYSTEM_PASSWORD.
     */
    private static final String LDAP_SYSTEM_URL = "ldap://172.24.2.212:389";

    /**
     * The Constant LDAP_SYSTEM_PASSWORD.
     */
    private static final String LDAP_USER_DN_TEMPLATE_PROPERTY = "ldap.userDnTemplate";

    /**
     * The Constant LDAP_USER_NAME.
     */
    private static final String LDAP_USER_NAME = "hamas";

    /**
     * The Constant LDAP_USER_PASSWORD.
     */
    private static final String LDAP_USER_PASS = "hamas";

    /**
     * private constructor.
     */
    private LdapAuthentication() {

    }

    /**
     * Authenticate ldap user.
     *
     * @return the authentication info
     */
    public static AuthenticationInfo authenticateLdapUser() {
        if ( isLdapSuccesfullyConnected() ) {
            template = getValueByNameFromPropertiesFile( LDAP_USER_DN_TEMPLATE_PROPERTY );
            ldapCustomAuthRealm.setUserDnTemplate( template );
            token = new UsernamePasswordToken( LDAP_USER_NAME, LDAP_USER_PASS );
            return ldapCustomAuthRealm.doGetAuthenticationInfo( token );
        }
        return null;
    }

    /**
     * Checks if is ldap successfully connected.
     *
     * @return true, if is ldap successfully connected
     */
    private static boolean isLdapSuccesfullyConnected() {
        ldapCustomAuthRealm = new LdapCustomAuthRealmImpl();
        ldapCustomAuthRealm.setSystemUsername( LDAP_SYSTEM_USERNAME );
        ldapCustomAuthRealm.setSystemPassword( LDAP_SYSTEM_PASS );
        ldapCustomAuthRealm.setUrl( LDAP_SYSTEM_URL );
        return ldapCustomAuthRealm.isLdapConnectionEstablished();
    }

    /**
     * Utility method for getting the value of properties from directory config.
     *
     * @param name
     *         the name
     *
     * @return the value by name from properties file
     */
    private static String getValueByNameFromPropertiesFile( String name ) {
        return PropertiesFileReader.readPropertiesFile( name );
    }

}