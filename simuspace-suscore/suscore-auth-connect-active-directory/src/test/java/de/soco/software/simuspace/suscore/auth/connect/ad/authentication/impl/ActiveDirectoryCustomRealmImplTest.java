package de.soco.software.simuspace.suscore.auth.connect.ad.authentication.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
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

import de.soco.software.simuspace.suscore.auth.connect.ad.authentication.PropertiesFileReader;

/**
 * * Simple test case for ActiveDirectoryRealm. Test the connectivity and authenticity of Active directory using custom realm
 */

/**
 * @author fahad
 */
public class ActiveDirectoryCustomRealmImplTest {

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
     * active directoey usernmae
     */
    private static final String VALID_SYSTEM_USERNAME_AD = "cn=Administrator,CN=Users,dc=whpc2,dc=clus";

    /**
     * active directory password
     */
    private static final String VALID_SYSTEM_PASSWORD_AD = "Lite0lite0";

    /**
     * valid url for active directory
     */
    private static final String VALID_URL_AD = "ldap://172.24.1.233:389";

    /**
     * valid search base for active directory
     */
    private static final String VALID_SEARCH_BASE_AD = "CN=Users,DC=WHPC2,DC=CLUS";

    /**
     * LdapCustomAuthRealm . This realm implementation covers basic authentication mechanism using programmatical approach for
     * authenticating user using LDAP/AD
     */

    private static final String INVALID_PROPERTY_NAME = "invalid";

    /**
     * Custom Realm whcih supports authentication using active directory
     */
    private ActiveDirectoryCustomRealmImpl activeDirectoryRealm;

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        activeDirectoryRealm = new ActiveDirectoryCustomRealmImpl();
        initializeAdParameters();
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
     * should not throw error when valid search base provided
     */
    @Test
    public void shouldNotGetErrorFromPropertiesFileWhenValidSearchBaseProvidedForAD() {
        String propertyName = PropertiesFileReader.readPropertiesFile( "ad.searchBase" );
        Assert.assertEquals( VALID_SEARCH_BASE_AD, propertyName );
    }

    /**
     * should throw error when invalid search base provided
     */
    @Test
    public void shouldGetErrorFromPropertiesFileWhenInValidSearchBaseProvidedForAD() {
        String propertyName = PropertiesFileReader.readPropertiesFile( INVALID_PROPERTY_NAME );
        Assert.assertNull( propertyName );
    }

    /**
     * initialization of active directory parameters.
     */
    private void initializeAdParameters() {
        this.activeDirectoryRealm.setSystemUsername( VALID_SYSTEM_USERNAME_AD );
        this.activeDirectoryRealm.setSystemPassword( VALID_SYSTEM_PASSWORD_AD );
        this.activeDirectoryRealm.setUrl( VALID_URL_AD );
        this.activeDirectoryRealm.setSearchBase( VALID_SEARCH_BASE_AD );
    }

}
