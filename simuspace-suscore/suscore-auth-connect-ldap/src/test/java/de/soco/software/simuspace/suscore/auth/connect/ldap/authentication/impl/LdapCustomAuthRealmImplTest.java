package de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.impl;

import javax.naming.ldap.LdapContext;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.PropertiesFileReader;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;

/**
 * * Simple test cases for LdapAuthentication. Testing the connectivity and authentication for LDAP
 */

/**
 * @author fahad
 */
public class LdapCustomAuthRealmImplTest {

    /**
     * The Shiro framework's default concrete implementation of the {@link SecurityManager} interface, based around a collection of
     * {@link org.apache.shiro.realm.Realm}s. This implementation delegates its authentication, authorization, and session operation
     */
    private DefaultSecurityManager securityManager = null;

    /**
     * A top-level abstract implementation of the <tt>Realm</tt> interface that only implements authentication support
     */
    private AuthenticatingRealm realm;

    /**
     * valid scenerio checking system usernmae
     */
    private static final String VALID_SYSTEM_USERNAME_LDAP = "cn=root,dc=thpc, dc=clus";

    /**
     * token user name for ldap
     */
    private static final String userNameLdap = "hamas";

    /**
     * token passwor for ldap
     */
    private static final String passwordLdap = "hamas";

    /**
     * ldap system password
     */
    private static final String VALID_SYSTEM_PASSWORD_LDAP = "simstream";

    /**
     * valid url for ldap
     */
    private static final String VALID_URL_LDAP = "ldap://172.24.2.212:389";

    /**
     * valid user dn for ldap
     */
    private static final String VALID_USER_DN_LDAP = "uid={0},ou=People,dc=thpc,dc=clus";

    /**
     * valid ldap context factory class
     */
    private static final String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    /**
     * LdapCustomAuthRealm . This realm implementation covers basic authentication mechanism using programmatical approach for
     * authenticating user using LDAP/AD
     */

    private static final String INVALID_PROPERTY_NAME = "invalid";

    private LdapCustomAuthRealmImpl ldapCustomAuthRealm;

    /**
     * simple username/password authentication token to support the most widely-used authentication mechanism . This instance for Ldap
     */
    private UsernamePasswordToken userNamePasswordTokenLdap;

    /**
     * A simple username/password authentication token to support the most widely-used authentication mechanism. This instance for Active
     * directory.
     */
    private UsernamePasswordToken userNamePasswordTokenActiveDirectory;

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * LdapContext for checking connectivity
     */
    private LdapContext ldapContext = null;

    /**
     * Runs before each test case . initialization/house keeping at startups on each test case.
     */
    @Before
    public void setup() {
        ThreadContext.remove();
        realm = new AuthenticatingRealm() {

            @Override
            protected AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token ) throws AuthenticationException {
                return null;
            }
        };
        securityManager = new DefaultSecurityManager( realm );
        SecurityUtils.setSecurityManager( securityManager );
        ldapCustomAuthRealm = new LdapCustomAuthRealmImpl();
        setUpAuthenticationTokenForLdap();
        initializeLdapParameters();
    }

    /**
     * Runs after each test case completed.
     */
    @After
    public void tearDown() {
        SecurityUtils.setSecurityManager( null );
        securityManager.destroy();
        ThreadContext.remove();
    }

    /**
     * Test case for testing properties file readin mechanism. when valid input/key provided it should give us correct string value.
     */
    @Test
    public void shouldGetCorrectValueFromPropertiesFileWhenValidSystemUserNameProvidedForLDAP() {
        String propertyName = PropertiesFileReader.readPropertiesFile( "ldap.systemUsername" );
        Assert.assertEquals( VALID_SYSTEM_USERNAME_LDAP, propertyName );
    }

    /**
     * Test case when invalid key provide it should not get the appropriate value
     */
    @Test
    public void shouldGetErrorFromPropertiesFileWhenInValidSystemUserNameProvidedForLDAP() {
        String propertyName = PropertiesFileReader.readPropertiesFile( INVALID_PROPERTY_NAME );
        Assert.assertNotSame( VALID_SYSTEM_USERNAME_LDAP, propertyName );
    }

    /**
     * correct key for password provided then it must give us the correct value for it
     */
    @Test
    public void shouldGetCorrectValueFromPropertiesFileWhenValidSystemPasswordProvidedForLDAP() {
        String propertyName = PropertiesFileReader.readPropertiesFile( "ldap.systemPassword" );

        Assert.assertEquals( VALID_SYSTEM_PASSWORD_LDAP, propertyName );
    }

    /**
     * when invalid password key provided it should throw exception
     */
    @Test
    public void shouldGetErrorFromPropertiesFileWhenInValidSystemPasswordProvidedForLDAP() {
        String propertyName = PropertiesFileReader.readPropertiesFile( INVALID_PROPERTY_NAME );
        Assert.assertNotSame( VALID_SYSTEM_PASSWORD_LDAP, propertyName );
    }

    /**
     * should throw error when invalid url is provided
     */
    @Test
    public void shouldGetErrorFromPropertiesFileWhenInValidURLProvidedForLDAP() {
        String propertyName = PropertiesFileReader.readPropertiesFile( INVALID_PROPERTY_NAME );
        Assert.assertNotSame( VALID_URL_LDAP, propertyName );

    }

    /**
     * should not throw error when valid key is provided.
     */
    @Test
    public void shouldNotGetErrorFromPropertiesFileWhenValidURLProvidedForLDAP() {
        String propertyName = PropertiesFileReader.readPropertiesFile( "ldap.url" );
        Assert.assertEquals( VALID_URL_LDAP, propertyName );
    }

    /**
     * should throw error when in valid user distinguished name is provided.
     */
    @Test
    public void shouldGetErrorFromPropertiesFileWhenInValidUserDNProvidedForLDAP() {
        String propertyName = PropertiesFileReader.readPropertiesFile( INVALID_PROPERTY_NAME );
        Assert.assertNotSame( VALID_USER_DN_LDAP, propertyName );
    }

    /**
     * show not throw error when valid user distinguished name is prvovided.
     */
    @Test
    public void shouldNotGetErrorFromPropertiesFileWhenValidUserDNProvidedForLDAP() {
        String propertyName = PropertiesFileReader.readPropertiesFile( "ldap.userDnTemplate" );
        Assert.assertEquals( VALID_USER_DN_LDAP, propertyName );
    }

    private void setUpAuthenticationTokenForLdap() {
        userNamePasswordTokenLdap = new UsernamePasswordToken( userNameLdap, passwordLdap );

    }

    /**
     * initialization of ldap parameters.
     */
    private void initializeLdapParameters() {
        ldapCustomAuthRealm.getContextFactory().setUrl( VALID_URL_LDAP );
        ldapCustomAuthRealm.setUserDnTemplate( VALID_USER_DN_LDAP );
        ldapContext = null;
    }

    /**
     * Get Valid ldap Paramters
     *
     * @return
     */
    private SusUserDirectoryAttributeDTO getValidLdapParameters() {
        return new SusUserDirectoryAttributeDTO( VALID_SYSTEM_USERNAME_LDAP, VALID_SYSTEM_PASSWORD_LDAP, VALID_URL_LDAP );

    }

}
