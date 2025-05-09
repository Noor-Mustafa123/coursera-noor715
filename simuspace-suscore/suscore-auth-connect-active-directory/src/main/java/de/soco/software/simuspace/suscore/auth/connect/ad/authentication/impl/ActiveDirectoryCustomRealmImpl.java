package de.soco.software.simuspace.suscore.auth.connect.ad.authentication.impl;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
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

import de.soco.software.simuspace.suscore.auth.connect.ad.authentication.ActiveDirectoryCustomRealm;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * @author fahad Custom ActiveDirectory Realm class for providing methods convenience for authentication and connectivity.
 */
@Log4j2
public class ActiveDirectoryCustomRealmImpl extends AbstractLdapRealm implements ActiveDirectoryCustomRealm {

    /**
     * KEy for AD Search against Sam Account name
     */
    public static final String SAM_ACCOUNT_NAME = "sAMAccountName";

    /**
     * key for user first name.
     */
    private static final String FIRST_NAME_KEY = "givenName";

    /**
     * key for user sur name.
     */
    private static final String SUR_NAME_KEY = "sn";

    /**
     * Active directory bad credential error message
     */
    private static final String AD_ERROR = "LDAP: error code 49 - 80090308: LdapErr: DSID-0C0903C5";

    /**
     * Max limit of time in milisecond to search
     */
    private static final int SEARCH_TIME_LIMIT = 30000;

    /**
     * Configures the {@link LdapContextFactory} implementation that is used to create LDAP connections for authentication and
     * authorization. If this is set, the {@link LdapContextFactory} provided will be used. Otherwise, a {@link DefaultLdapContextFactory}
     * instance will be created based on the properties specified in this realm.
     *
     * @param ldapContextFactory the factory to use - if not specified, a default factory will be created automatically.
     */
    private LdapContextFactory ldapContextFactory = null;

    /**
     * Constructor
     */
    public ActiveDirectoryCustomRealmImpl() {
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
     * @return ldapContextFactory the ldapContextFactory
     */
    public LdapContextFactory ensureContextFactory() {
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
    public AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token ) throws AuthenticationException {
        AuthenticationInfo info;
        try {
            info = queryForAuthenticationInfo( token, ensureContextFactory() );
        } catch ( javax.naming.AuthenticationException e ) {
            log.error( e.getMessage(), e );
            throw new AuthenticationException( "LDAP user authentication failed.", e );
        } catch ( NamingException e ) {
            String msg = "LDAP naming error while attempting to authenticate user.";
            log.error( e.getMessage(), e );
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
        final UsernamePasswordToken upToken = ( UsernamePasswordToken ) token;
        LdapContext ctx = null;
        try {
            ctx = ldapContextFactory.getSystemLdapContext();
            final String attribName = "userPrincipalName";
            final SearchControls searchCtls = new SearchControls( SearchControls.SUBTREE_SCOPE, 1, 0, new String[]{ attribName }, false,
                    false );
            final NamingEnumeration< SearchResult > search = ctx.search( searchBase, "sAMAccountName={0}",
                    new Object[]{ upToken.getPrincipal() }, searchCtls );
            if ( search.hasMore() ) {
                final SearchResult next = search.next();
                String loginUser = next.getAttributes().get( attribName ).get().toString();
                log.info( "Loginuser: " + loginUser );
                ldapContextFactory.getLdapContext( loginUser, upToken.getPassword() );
            }
        } catch ( NamingException ne ) {
            log.error( "Error in ldap name resolving", ne );
            throw ne;
        } finally {
            LdapUtils.closeContext( ctx );
        }
        return buildAuthenticationInfo( upToken.getUsername(), upToken.getPassword() );
    }

    /**
     * Builds the authentication info.
     *
     * @param username
     *         the username
     * @param password
     *         the password
     *
     * @return the authentication info
     */
    protected AuthenticationInfo buildAuthenticationInfo( String username, char[] password ) {

        return new SimpleAuthenticationInfo( username, password, getName() );
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.shiro.realm.ldap.AbstractLdapRealm#queryForAuthorizationInfo(org.
     * apache.shiro.subject.PrincipalCollection,
     * org.apache.shiro.realm.ldap.LdapContextFactory)
     */
    @Override
    protected AuthorizationInfo queryForAuthorizationInfo( PrincipalCollection principal, LdapContextFactory ldapContextFactory )
            throws NamingException {

        return null;
    }

    /**
     * Checks if is active directory connection established.
     *
     * @return true, if is active directory connection established
     *
     * @throws NamingException
     *         the naming exception
     */
    public boolean isActiveDirectoryConnectionEstablished() throws NamingException {

        this.ensureContextFactory();
        LdapContext ldapContext = this.getLdapContextFactory().getSystemLdapContext();
        return null != ldapContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.auth.connect.ad.authentication.
     * ADirectoryCustomRealm#isActiveDirectoryConnectionEstablished(de.soco.software
     * .simuspace.suscore.auth.connect.ad.authentication.model.
     * ActiveDirectoryAttributes)
     */
    @Override
    public boolean isActiveDirectoryConnectionEstablished( SusUserDirectoryAttributeDTO directoryAttributes ) {

        try {
            LdapContext ldapContext = getLdapDefaultContextFactory( directoryAttributes ).getSystemLdapContext();
            return null != ldapContext;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            if ( e.getMessage().contains( ConstantsString.INVALID_CREDENTIALS ) || e.getMessage().contains( AD_ERROR )
                    || e.getMessage().contains( ConstantsString.INVALID_DN ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_LOGIN_CREDENTIALS.getKey() ) );
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.CONNECTION_FAILED.getKey(), e.getMessage() ) );
        }
    }

    /**
     * Gets the context factory.
     *
     * @param activeDirectoryAttributes
     *         the active directory attributes
     *
     * @return the context factory
     */
    public LdapContextFactory getLdapDefaultContextFactory( SusUserDirectoryAttributeDTO activeDirectoryAttributes ) {

        if ( log.isDebugEnabled() ) {
            log.debug( "No LdapContextFactory specified - creating a default instance." );
        }

        this.searchBase = activeDirectoryAttributes.getSearchBase();
        this.systemPassword = activeDirectoryAttributes.getSystemPassword();
        this.systemUsername = activeDirectoryAttributes.getSystemUsername();
        this.url = activeDirectoryAttributes.getUrl();

        DefaultLdapContextFactory defaultFactory = new DefaultLdapContextFactory();
        defaultFactory.setUrl( activeDirectoryAttributes.getUrl() );
        defaultFactory.setSystemUsername( activeDirectoryAttributes.getSystemUsername() );
        defaultFactory.setSystemPassword( activeDirectoryAttributes.getSystemPassword() );
        defaultFactory.setSearchBase( activeDirectoryAttributes.getSearchBase() );

        return defaultFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.auth.connect.ad.authentication.
     * ActiveDirectoryCustomRealm#lookUpUserUidInAD(java.lang.String)
     */

    @Override
    public boolean lookUpUserUidInAD( String uid ) {

        try {

            String filter = "(sAMAccountName=" + uid + ")";
            ensureContextFactory();
            LdapContext ctx = ldapContextFactory.getSystemLdapContext();

            ctx.setRequestControls( null );

            NamingEnumeration< ? > namingEnum = ctx.search( this.searchBase, filter, getSimpleSearchControls() );

            while ( namingEnum.hasMore() ) {
                SearchResult result = ( SearchResult ) namingEnum.next();
                Attributes attrs = result.getAttributes();
                return attrs.get( SAM_ACCOUNT_NAME ) != null;

            }
            namingEnum.close();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.auth.connect.ad.authentication.
     * ActiveDirectoryCustomRealm#lookUpUserUidInAD(java.lang.String)
     */
    @Override
    public String[] getUserFirstNameAndSurNameFromUid( String uid ) {
        String[] names = new String[ 2 ];
        try {
            String filter = "(sAMAccountName=" + uid + ")";
            ensureContextFactory();
            LdapContext ctx = ldapContextFactory.getSystemLdapContext();

            ctx.setRequestControls( null );

            NamingEnumeration< ? > namingEnum = ctx.search( this.searchBase, filter, getSimpleSearchControls() );

            while ( namingEnum.hasMore() ) {
                SearchResult result = ( SearchResult ) namingEnum.next();
                Attributes attrs = result.getAttributes();
                names[ 0 ] = attrs.get( FIRST_NAME_KEY ).get().toString();
                names[ 1 ] = attrs.get( SUR_NAME_KEY ).get().toString();
            }
            namingEnum.close();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return names;
    }

    /**
     * Get Simple Search controls configration for AD
     *
     * @return SearchControls
     */
    private SearchControls getSimpleSearchControls() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE );
        searchControls.setTimeLimit( SEARCH_TIME_LIMIT );
        return searchControls;
    }

}
