/**
 *
 */

package de.soco.software.simuspace.suscore.authentication;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * @author Ahmar Nadeem
 * @Description This class provides the check for the shiro.ini file to keep track of unsuccessful login attempts. It has been added just
 * for reference in the future when actual authentication and authorization is required to be implemented. Further behavior will be added
 * according to the business requirements.
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private static final int MAX_LOGIN_ATTEMPTS = 10;

    /**
     * Shiro cache container. Used for holding the unsuccessful attempts.
     */
    private Ehcache passwordRetryCache;

    /**
     *
     */
    public RetryLimitHashedCredentialsMatcher() {
        CacheManager cacheManager = CacheManager.create( CacheManager.class.getClassLoader().getResource( "password-ehcache.xml" ) );
        passwordRetryCache = cacheManager.getCache( "passwordRetryCache" );
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.shiro.authc.credenteep
     *              track of unsuccessful login attempts. It has been added juial.HashedCredentialsMatcher#
     * doCredentialsMatch(org.apache.shiro.authc.AuthenticationToken,
     * org.apache.shiro.authc.AuthenticationInfo)
     */
    @Override
    public boolean doCredentialsMatch( AuthenticationToken token, AuthenticationInfo info ) {
        String username = ( String ) token.getPrincipal();
        // retry count + 1
        Element element = passwordRetryCache.get( username );
        if ( element == null ) {
            element = new Element( username, new AtomicInteger( 0 ) );
            passwordRetryCache.put( element );
        }
        AtomicInteger retryCount = ( AtomicInteger ) element.getObjectValue();
        if ( retryCount.incrementAndGet() > MAX_LOGIN_ATTEMPTS ) {
            throw new ExcessiveAttemptsException();
        }

        boolean matches = super.doCredentialsMatch( token, info );
        if ( matches ) {
            // clear retry count
            passwordRetryCache.remove( username );
        }
        return matches;
    }

}
