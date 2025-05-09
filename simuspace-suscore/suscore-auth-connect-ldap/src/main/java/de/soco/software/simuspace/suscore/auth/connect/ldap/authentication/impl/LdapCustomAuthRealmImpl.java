package de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.impl;

import javax.naming.AuthenticationNotSupportedException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.ldap.UnsupportedAuthenticationMechanismException;
import org.apache.shiro.realm.ldap.DefaultLdapRealm;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.realm.ldap.LdapContextFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.LdapCustomAuthRealm;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * An LDAP implementation.This implementation for supporting LDAP using Shiro * This realm implementation covers basic authentication
 * mechanism using programmatically approach for authenticating user using LDAP/AD
 *
 * @author fahad
 */
@Getter
@Setter
@Log4j2
public class LdapCustomAuthRealmImpl extends DefaultLdapRealm implements LdapCustomAuthRealm {

    /**
     * Limit for string splitting.
     */
    private static final int SPLIT_LIMIT_TWO = 2;

    /**
     * Second List Index.
     */
    private static final int SECOND_LIST_INDEX = 1;

    /**
     * key for ldap user Cn.
     */
    private static final String LDAP_CN_KEY = "cn";

    /**
     * The Constant LDAP_UID_KEY.
     */
    private static final String LDAP_UID_KEY = "uid=";

    /**
     * key for ldap user Cn.
     */
    private static final String LDAP_FIRST_NAME_KEY = "givenName";

    /**
     * key for ldap user Cn.
     */
    private static final String LDAP_SUR_NAME_KEY = "sn";

    /**
     * The Constant LDAP_EMAIL_KEY.
     */
    private static final String LDAP_EMAIL_KEY = "mail";

    /**
     * Max limit of time in millisecond to search.
     */
    private static final int SEARCH_TIME_LIMIT = 30000;

    /**
     * Authentication listener.
     */
    private Collection< AuthenticationListener > listeners = new ArrayList<>();

    /**
     * The LdapContextFactory instance used to acquire {@link javax.naming.ldap.LdapContext LdapContext}'s at runtime to acquire connections
     * to the LDAP directory to perform authentication attempts and authorization queries.
     */
    private JndiLdapContextFactory contextFactory;

    /**
     * System username for connecting with ldap.
     */

    private String systemUsername;

    /**
     * url for connecting with ldap.
     */
    private String url;

    /**
     * System password for connecting with ldap.
     */

    private String systemPassword;

    /**
     * Pooling enabled or not with ldap.
     */
    private boolean poolingEnabled;

    /**
     * Default no-argument constructor that defaults the internal {@link LdapContextFactory} instance to a {@link JndiLdapContextFactory}.
     */
    public LdapCustomAuthRealmImpl() {
        super();
        this.contextFactory = new JndiLdapContextFactory();
    }

    /**
     * Sets the authentication listeners.
     *
     * @param listeners
     *         the new authentication listeners
     */
    public void setAuthenticationListeners( Collection< AuthenticationListener > listeners ) {
        this.listeners = Objects.requireNonNullElseGet( listeners, ArrayList::new );
    }

    /**
     * Returns the {@link AuthenticationListener AuthenticationListener}s that should be notified during authentication attempts.
     *
     * @return the {@link AuthenticationListener AuthenticationListener}s that should be notified during authentication attempts.
     */
    public Collection< AuthenticationListener > getAuthenticationListeners() {
        return this.listeners;
    }

    /**
     * Returns the LdapContextFactory instance used to acquire connections to the LDAP directory during authentication attempts and
     * authorization queries. Unless specified otherwise, the default is a {@link JndiLdapContextFactory} instance.
     *
     * @param token
     *         the token
     * @param info
     *         the info
     */

    protected void notifySuccess( AuthenticationToken token, AuthenticationInfo info ) {
        for ( AuthenticationListener listener : this.listeners ) {
            listener.onSuccess( token, info );
        }
    }

    /* (non-Javadoc)
     * @see org.apache.shiro.realm.ldap.DefaultLdapRealm#doGetAuthenticationInfo(org.apache.shiro.auth.AuthenticationToken)
     */
    @Override
    public AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token ) throws AuthenticationException {
        AuthenticationInfo info;
        try {
            info = queryForAuthenticationInfo( token, getContextFactory() );
        } catch ( AuthenticationNotSupportedException e ) {
            log.error( e.getMessage(), e );
            String msg = "Unsupported configured authentication mechanism";
            throw new UnsupportedAuthenticationMechanismException( msg, e );
        } catch ( javax.naming.AuthenticationException e ) {
            log.error( e.getMessage(), e );
            throw new AuthenticationException( "LDAP user authentication failed.", e );
        } catch ( NamingException e ) {
            log.error( e.getMessage(), e );
            String msg = "LDAP naming error while attempting to authenticate user.";
            throw new AuthenticationException( msg, e );
        }

        return info;
    }

    /* (non-Javadoc)
     * @see org.apache.shiro.realm.ldap.DefaultLdapRealm#doGetAuthenticationInfo(org.apache.shiro.auth.AuthenticationToken)
     */
    @Override
    public AuthenticationInfo getAuthenticationInfoFromLdap( AuthenticationToken token ) throws AuthenticationException, NamingException {
        return queryForAuthenticationInfo( token, getContextFactory() );
    }

    /* (non-Javadoc)
     * @see org.apache.shiro.realm.ldap.DefaultLdapRealm#createAuthenticationInfo(org.apache.shiro.auth.AuthenticationToken, java.lang.Object, java.lang.Object, javax.naming.ldap.LdapContext)
     */
    @Override
    protected AuthenticationInfo createAuthenticationInfo( AuthenticationToken token, Object ldapPrincipal, Object ldapCredentials,
            LdapContext ldapContext ) {
        return new SimpleAuthenticationInfo( token.getPrincipal(), token.getCredentials(), ConstantsString.EMPTY_STRING );
    }

    /**
     * Checks if is ldap connection established.
     *
     * @return boolean
     *
     * @throws SusException
     *         the sus exception
     */
    public boolean isLdapConnectionEstablished() throws SusException {

        this.getContextFactory().setUrl( url );
        this.getContextFactory().setSystemUsername( systemUsername );
        this.getContextFactory().setSystemPassword( systemPassword );
        this.getContextFactory().setReferral( "follow" );
        LdapContext ldapContext;
        try {
            ldapContext = this.getContextFactory().getSystemLdapContext();
            return null != ldapContext;
        } catch ( NamingException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.LdapCustomAuthRealmi#isLdapConnectionEstablished(de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.SusLdapContextFactory)
     */
    @Override
    public boolean isLdapConnectionEstablished( SusUserDirectoryAttributeDTO contextFactory ) throws SusException {
        if ( contextFactory == null ) {
            return false;
        }
        this.getContextFactory().setUrl( contextFactory.getUrl() );
        this.getContextFactory().setSystemUsername( contextFactory.getSystemUsername() );
        this.getContextFactory().setSystemPassword( contextFactory.getSystemPassword() );
        return null != getLdapConnectionContext( contextFactory );
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.LdapCustomAuthRealm#lookUpUserUidInLdap(java.lang.String)
     */
    @Override
    public boolean lookUpUserUidInLdap( String uid ) {

        try {
            LdapContext ctx = this.getContextFactory().getSystemLdapContext();
            String filter = "(uid=" + uid + ")";
            String contextName = this.getUserDnTemplate().split( ConstantsString.COMMA, SPLIT_LIMIT_TWO )[ SECOND_LIST_INDEX ];
            ctx.setRequestControls( null );
            NamingEnumeration< ? > namingEnum = ctx.search( contextName, filter, getSimpleSearchControls() );
            boolean userFound = false;
            while ( namingEnum.hasMore() ) {

                SearchResult result = ( SearchResult ) namingEnum.next();
                Attributes attrs = result.getAttributes();
                userFound = attrs.get( LDAP_CN_KEY ) != null;
                break;
            }
            namingEnum.close();
            return userFound;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCaseSensitiveUserUidFromLdap( String uid, SuSUserDirectoryDTO susUserDirectoryDTO ) {
        try {
            if ( isLdapConnectionEstablished( susUserDirectoryDTO.getUserDirectoryAttribute() ) ) {
                String filter = "(uid=" + uid + ")";
                LdapContext ctx = this.getContextFactory().getSystemLdapContext();
                String contextName = this.getUserDnTemplate().split( ConstantsString.COMMA, SPLIT_LIMIT_TWO )[ SECOND_LIST_INDEX ];
                ctx.setRequestControls( null );
                NamingEnumeration< ? > namingEnum = ctx.search( contextName, filter, getSimpleSearchControls() );
                String uidFromLdap = null;
                while ( namingEnum.hasMore() ) {
                    SearchResult result = ( SearchResult ) namingEnum.next();
                    Attributes attrs = result.getAttributes();
                    if ( attrs.get( LDAP_CN_KEY ) != null ) {
                        uidFromLdap = result.getName().replace( LDAP_UID_KEY, ConstantsString.EMPTY_STRING );
                        break;
                    }
                }
                namingEnum.close();
                return uidFromLdap;
            } else {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.INVALID_CREADENTIALS_OR_INVALID_CONFIFGURATION_LDAP.getKey() ) );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        throw new SusException( MessageBundleFactory.getMessage( Messages.USER_DOES_NOT_EXIST_IN_DIRECTORY.getKey(), uid ) );
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.LdapCustomAuthRealm#lookUpUserUidInLdap(java.lang.String)
     */
    @Override
    public String[] getUserFirstNameAndSurNameFromUid( String uid ) {
        String[] names = new String[ 2 ];
        try {
            String filter = "(uid=" + uid + ")";
            NamingEnumeration< ? > namingEnum = getNamingEnumeration( filter );
            while ( namingEnum.hasMore() ) {
                SearchResult result = ( SearchResult ) namingEnum.next();
                Attributes attrs = result.getAttributes();

                names[ 0 ] = attrs.get( LDAP_FIRST_NAME_KEY ).get().toString();
                names[ 1 ] = attrs.get( LDAP_SUR_NAME_KEY ).get().toString();
            }
            namingEnum.close();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return names;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.LdapCustomAuthRealm#lookUpUserUidInLdap(java.lang.String)
     */
    @Override
    public String[] getUserFirstNameAndSurNameFromUid( String uid, SuSUserDirectoryDTO directoryDTO ) {
        if ( isLdapConnectionEstablished( directoryDTO.getUserDirectoryAttribute() ) ) {
            String[] names = new String[ 2 ];
            try {
                String filter = "(uid=" + uid + ")";
                NamingEnumeration< ? > namingEnum = getNamingEnumeration( filter );
                while ( namingEnum.hasMore() ) {
                    SearchResult result = ( SearchResult ) namingEnum.next();
                    Attributes attrs = result.getAttributes();

                    names[ 0 ] = attrs.get( LDAP_FIRST_NAME_KEY ).get().toString();
                    names[ 1 ] = attrs.get( LDAP_SUR_NAME_KEY ).get().toString();
                }
                namingEnum.close();
            } catch ( Exception e ) {
                log.error( e );
            }
            if ( Arrays.stream( names ).noneMatch( name -> name != null && !name.isEmpty() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.USER_DOES_NOT_EXIST_IN_DIRECTORY.getKey(), uid ) );
            }
            return names;
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CONNECTION_FAILED.getKey(), directoryDTO.getName() ) );
        }
    }

    private NamingEnumeration< ? > getNamingEnumeration( String filter ) {
        NamingEnumeration< ? > namingEnum = null;
        try {
            LdapContext ctx = this.getContextFactory().getSystemLdapContext();
            String contextName = this.getUserDnTemplate().split( ConstantsString.COMMA, SPLIT_LIMIT_TWO )[ SECOND_LIST_INDEX ];
            ctx.setRequestControls( null );
            namingEnum = ctx.search( contextName, filter, getSimpleSearchControls() );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return namingEnum;
    }

    @Override
    public String getUserEmailFromUid( String uid ) {
        String email = null;
        try {
            String filter = "(uid=" + uid + ")";
            NamingEnumeration< ? > namingEnum = getNamingEnumeration( filter );
            while ( namingEnum.hasMore() ) {
                SearchResult result = ( SearchResult ) namingEnum.next();
                Attributes attrs = result.getAttributes();
                email = attrs.get( LDAP_EMAIL_KEY ).get().toString();
            }
            namingEnum.close();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return email;
    }

    @Override
    public String getUserEmailFromUid( String uid, SuSUserDirectoryDTO directoryDTO ) {
        if ( isLdapConnectionEstablished( directoryDTO.getUserDirectoryAttribute() ) ) {
            String email = null;
            try {
                String filter = "(uid=" + uid + ")";
                NamingEnumeration< ? > namingEnum = getNamingEnumeration( filter );
                while ( namingEnum.hasMore() ) {
                    SearchResult result = ( SearchResult ) namingEnum.next();
                    Attributes attrs = result.getAttributes();
                    email = attrs.get( LDAP_EMAIL_KEY ).get().toString();
                }
                namingEnum.close();
            } catch ( Exception e ) {
                log.error( e );
            }
            return email;
        }
        return null;
    }

    /**
     * Gets the ldap connection context.
     *
     * @param contextFactory
     *         the context factory
     *
     * @return the ldap connection context
     */
    private LdapContext getLdapConnectionContext( SusUserDirectoryAttributeDTO contextFactory ) {
        try {
            if ( contextFactory.getUserDnTemplate() != null ) {
                this.setUserDnTemplate( contextFactory.getUserDnTemplate() );
            }
            this.getContextFactory().setReferral( "follow" );
            LdapContext ldapContext;
            ldapContext = this.getContextFactory().getSystemLdapContext();
            return ldapContext;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            if ( e.getMessage().contains( ConstantsString.INVALID_CREDENTIALS ) || e.getMessage().contains( ConstantsString.INVALID_DN ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_LOGIN_CREDENTIALS.getKey() ) );
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.CONNECTION_FAILED.getKey(), e.getMessage() ) );
        }
    }

    /**
     * Get Simple Search controls configuration for Ldap.
     *
     * @return SearchControls
     */
    private SearchControls getSimpleSearchControls() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE );
        searchControls.setTimeLimit( SEARCH_TIME_LIMIT );

        return searchControls;
    }

    /* (non-Javadoc)
     * @see org.apache.shiro.realm.ldap.DefaultLdapRealm#getContextFactory()
     */
    @Override
    public JndiLdapContextFactory getContextFactory() {
        return this.contextFactory;
    }

}
