package de.soco.software.simuspace.suscore.auth.connect.ad.authentication;

import javax.naming.NamingException;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

import de.soco.software.simuspace.suscore.auth.connect.ad.authentication.impl.ActiveDirectoryCustomRealmImpl;

/**
 * @author fahad The initiator for authenticating via shiro plugin using LDAP/AD This class can be replaced by the user or can be used by
 * the user who is interesting in accessing/connecting using LDAP/AD
 */

public class LdapAuthentication {

    // Active directory URL
    protected static String urlAD = null;
    // Active directory search base

    protected static String searchBase = null;

    // Active directory system username
    protected static String systemUsername = null;

    // Active directory system password
    protected static UsernamePasswordToken token;
    // Active directory authentication token

    protected static AuthenticationToken authToken;

    // Active directory authentication system password
    protected static String systemPassword = null;
    // Active directory authentication custom abstractLdap

    private static ActiveDirectoryCustomRealmImpl abstractLdap;

    /**
     * private constructor
     */
    private LdapAuthentication() {

    }

    /**
     * Filling the parameters for AD user
     */
    private static void filledADUserDistinguishName() {
        abstractLdap = new ActiveDirectoryCustomRealmImpl();
        systemUsername = getValueByNameFromPropertiesFile( "ad.systemUsername" );
        systemPassword = getValueByNameFromPropertiesFile( "ad.systemPassword" );
        urlAD = getValueByNameFromPropertiesFile( "ad.url" );
        searchBase = getValueByNameFromPropertiesFile( "ad.searchBase" );
        abstractLdap.setSystemUsername( systemUsername );
        abstractLdap.setSystemPassword( systemPassword );
        abstractLdap.setUrl( urlAD );
        abstractLdap.setSearchBase( searchBase );
    }

    /**
     * Authentication for AD user
     *
     * @return the Authentication Info if active directory is connected. Null otherwise
     *
     * @throws NamingException
     */
    public static AuthenticationInfo authenticateADUser() throws NamingException {
        if ( isActiveDirectorySuccesfullyConnected() ) {
            token = new UsernamePasswordToken( systemUsername, systemPassword );
            return abstractLdap.doGetAuthenticationInfo( token );

        }
        return null;
    }

    /**
     * @return boolean
     *
     * @throws NamingException
     *         check whether connection is established or not with ad server
     */
    private static boolean isActiveDirectorySuccesfullyConnected() throws NamingException {
        filledADUserDistinguishName();
        return abstractLdap.isActiveDirectoryConnectionEstablished();
    }

    /**
     * Utility method for getting the value of properties from directory config.
     *
     * @param name
     *         the name
     *
     * @return the property value
     */
    private static String getValueByNameFromPropertiesFile( String name ) {
        return PropertiesFileReader.readPropertiesFile( name );
    }

}