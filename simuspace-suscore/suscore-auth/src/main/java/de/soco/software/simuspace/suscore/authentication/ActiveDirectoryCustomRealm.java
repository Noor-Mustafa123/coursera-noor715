package de.soco.software.simuspace.suscore.authentication;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.ldap.AbstractLdapRealm;
import org.apache.shiro.realm.ldap.DefaultLdapContextFactory;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.subject.PrincipalCollection;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ActiveDirectoryCustomRealm extends AbstractLdapRealm {

    /**
     * Configures the {@link LdapContextFactory} implementation that is used to create LDAP connections for authentication and
     * authorization. If this is set, the {@link LdapContextFactory} provided will be used. Otherwise, a {@link DefaultLdapContextFactory}
     * instance will be created based on the properties specified in this realm.
     *
     * @param ldapContextFactory the factory to use - if not specified, a default factory will be created automatically.
     */
    private LdapContextFactory ldapContextFactory = null;

    public ActiveDirectoryCustomRealm() {
        super();
    }

    /**
     * Getter and setters
     */
    public LdapContextFactory getLdapContextFactory() {
        return ldapContextFactory;
    }

    @Override
    public void setLdapContextFactory( LdapContextFactory ldapContextFactory ) {
        this.ldapContextFactory = ldapContextFactory;
    }

    /*--------------------------------------------
    |               M E T H O D S                |
    ============================================*/

    /**
     * Init method calls once on initialization
     */

    @Override
    protected void onInit() {
        super.onInit();
        ensureContextFactory();
    }

    /**
     * Private method for setting up context factory
     *
     * @return the ldapContextFactory
     */
    private LdapContextFactory ensureContextFactory() {
        if ( this.ldapContextFactory == null ) {

            if ( log.isDebugEnabled() ) {
                log.debug( "No LdapContextFactory specified - creating a default instance." );
            }

            DefaultLdapContextFactory defaultFactory = new DefaultLdapContextFactory();
            defaultFactory.setPrincipalSuffix( this.principalSuffix );
            defaultFactory.setSearchBase( this.searchBase );
            defaultFactory.setUrl( this.url );
            defaultFactory.setSystemUsername( this.systemUsername );
            defaultFactory.setSystemPassword( this.systemPassword );

            this.ldapContextFactory = defaultFactory;
        }
        return this.ldapContextFactory;
    }

    /**
     * overrided method to implement authentication mechanism
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token ) throws AuthenticationException {
        AuthenticationInfo info;
        try {
            info = queryForAuthenticationInfo( token, ensureContextFactory() );
        } catch ( javax.naming.AuthenticationException e ) {
            throw new AuthenticationException( "LDAP authentication failed.", e );
        } catch ( NamingException e ) {
            String msg = "LDAP naming error while attempting to authenticate user.";
            throw new AuthenticationException( msg, e );
        }

        return info;
    }

    /**
     * overrided method to implement authentication mechanism
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo( PrincipalCollection principals ) {
        AuthorizationInfo info;
        try {
            info = queryForAuthorizationInfo( principals, ensureContextFactory() );
        } catch ( NamingException e ) {
            String msg = "LDAP naming error while attempting to retrieve authorization for user [" + principals + "].";
            throw new AuthorizationException( msg, e );
        }

        return info;
    }

    /**
     * overrided method to implement authentication mechanism
     */
    @Override
    protected AuthenticationInfo queryForAuthenticationInfo( AuthenticationToken token, LdapContextFactory ldapContextFactory )
            throws NamingException {

        UsernamePasswordToken upToken = ( UsernamePasswordToken ) token;

        // Binds using the username and password provided by the user.
        LdapContext ctx = null;
        try {
            ctx = ldapContextFactory.getLdapContext( upToken.getUsername(), String.valueOf( upToken.getPassword() ) );
        } finally {
            LdapUtils.closeContext( ctx );
        }

        return buildAuthenticationInfo( upToken.getUsername(), upToken.getPassword() );
    }

    protected AuthenticationInfo buildAuthenticationInfo( String username, char[] password ) {
        return new SimpleAuthenticationInfo( username, password, getName() );
    }

    @Override
    protected AuthorizationInfo queryForAuthorizationInfo( PrincipalCollection principal, LdapContextFactory ldapContextFactory )
            throws NamingException {
        return null;
    }

}
