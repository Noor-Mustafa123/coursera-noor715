package de.soco.software.simuspace.suscore.plugin.karaf.client;

import javax.security.auth.Subject;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.karaf.jaas.boot.principal.RolePrincipal;
import org.apache.karaf.jaas.boot.principal.UserPrincipal;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.api.console.SessionFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.plugin.manager.impl.PluginManagerImpl;

/**
 * This Class will be used to connect to karaf Terminal for commands Execution
 *
 * @author Nosheen.Sharif
 */
@Deprecated
public class KarafClient {

    /**
     * karaf sessionFactory reference
     */
    @Reference
    protected SessionFactory sessionFactory;

    /**
     * karaf session reference
     */
    private Session session;

    /**
     * timout for a callable
     */
    private static final long TIMEOUT = 100000L;

    /**
     * boolean flag to print the karaf command in terminal
     */
    private static final boolean SILENT = Boolean.FALSE;

    /**
     * application key for session
     */
    private static final String APPLICATION = "APPLICATION";

    /**
     * user key for session
     */
    private static final String USER = "USER";

    /**
     * executor thread pool reference
     */
    private ExecutorService executor;

    /**
     * java out put stream reference
     */
    private ByteArrayOutputStream byteArrayOutputStream;

    /**
     * java print Stream reference
     */
    private PrintStream printStream;

    /**
     * Constructor for initialization
     */
    public KarafClient() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        printStream = new PrintStream( byteArrayOutputStream );

        executor = Executors.newCachedThreadPool();
        final BundleContext context = FrameworkUtil.getBundle( PluginManagerImpl.class ).getBundleContext();
        final ServiceReference serviceReference = context.getServiceReference( SessionFactory.class.getName() );

        sessionFactory = ( SessionFactory ) context.getService( serviceReference );

        session = sessionFactory.create( System.in, printStream, printStream );
        session.put( APPLICATION, System.getProperty( ConstantsKaraf.KARAF_NAME, ConstantsKaraf.ROOT ) );
        session.put( USER, ConstantsKaraf.ROLE_ADMIN );

    }

    /**
     * Method to Execute Karaf Command
     *
     * @param command
     *
     * @return
     */
    public String executeCommandInKarafShell( final String command ) {
        String response = StringUtils.EMPTY;
        FutureTask< String > commandFuture = new FutureTask<>( new Callable< String >() {

            @Override
            public String call() {
                Subject subject = new Subject();
                subject.getPrincipals().add( new UserPrincipal( ConstantsKaraf.ROLE_ADMIN ) );
                subject.getPrincipals().add( new RolePrincipal( ConstantsKaraf.ROLE_ADMIN ) );
                subject.getPrincipals().add( new RolePrincipal( ConstantsKaraf.ROLE_MANAGER ) );
                subject.getPrincipals().add( new RolePrincipal( ConstantsKaraf.ROLE_VIEW ) );
                return Subject.doAs( subject, new PrivilegedAction< String >() {

                    @Override
                    public String run() {
                        try {
                            if ( !SILENT ) {
                                System.out.println( command );
                                System.out.flush();
                            }
                            session.execute( command );
                        } catch ( Exception e ) {
                            e.printStackTrace( System.err );
                            throw new SusException( "Error Executing Command: " + e.getMessage() );
                        }
                        printStream.flush();
                        return byteArrayOutputStream.toString();
                    }
                } );
            }
        } );
        try {
            executor.submit( commandFuture );
            response = commandFuture.get( TIMEOUT, TimeUnit.MILLISECONDS );
        } catch ( Exception e ) {
            e.printStackTrace( System.err );
            response = "SHELL COMMAND TIMED OUT: ";
            throw new SusException( "Error Executing Command: " + e.getMessage() );
        }
        System.out.println( "Execute: " + command + " - Response:" + response );
        return response;
    }

    /**
     * @return
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory
     */
    public void setSessionFactory( SessionFactory sessionFactory ) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session
     */
    public void setSession( Session session ) {
        this.session = session;
    }

}