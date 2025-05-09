package de.soco.software.simuspace.suscore.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import lombok.extern.log4j.Log4j2;

/**
 * @author Fahad
 */
@Log4j2
public class Authentication {

    /**
     * @param args
     */
    public static void main( String[] args ) {
        Authentication auth = new Authentication();
        String login = "admin";
        char[] pass = { 'a', 'd', 'm', 'i', 'n' };
        Subject subject = auth.authenticateWithShiro( login, pass );
        if ( subject != null && subject.isAuthenticated() ) {
            System.out.println( "successfulln" );
        } else {
            System.out.println( "Failedin" );
        }
        auth.write();
    }

    /**
     * @param username
     * @param pass
     */
    private Subject authenticateWithShiro( String username, char[] pass ) {
        Subject currentUser = null;
        try {
            log.info( "Authentication shiro is started...." );
            Factory factory = new IniSecurityManagerFactory( "classpath:shiro.ini" );
            org.apache.shiro.mgt.SecurityManager securityManager = ( SecurityManager ) factory.getInstance();
            JdbcRealm realm = ( JdbcRealm ) ( ( IniSecurityManagerFactory ) factory ).getBeans().get( "jdbcRealm" );
            realm.setPermissionsLookupEnabled( true );

            SecurityUtils.setSecurityManager( securityManager );
            currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            session.setAttribute( "someKey", "aValue" );
            String value = ( String ) session.getAttribute( "someKey" );
            if ( "aValue".equals( value ) ) {
                log.info( "Retrievedcorrect value! [" + value + "]" );
            }
            // let's login the current user so we ,can check against roles and
            // permissions:
            if ( !currentUser.isAuthenticated() ) {
                UsernamePasswordToken token = new UsernamePasswordToken( username, pass );
                token.setRememberMe( true );
                currentUser.login( token );
            }
        } catch ( Exception e ) {
            log.error( "Authenticationed", e );
        }
        return currentUser;
    }

    public void write() {
        Subject subject = SecurityUtils.getSubject();
        if ( subject.hasRole( "Administrator" ) ) {
            log.info( "has administrator" );
        } else {
            log.info( "hasole" );
        }
        if ( subject.isPermitted( "admin:write" ) ) {
            log.info( "Administratorermitted to write" );
        } else {
            log.info( "AdministratorOT permitted to write" );
        }
    }

}
