package de.soco.software.simuspace.suscore.data.activator;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.cxf.helpers.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.blueprint.container.BlueprintEvent;
import org.osgi.service.blueprint.container.BlueprintListener;
import org.osgi.util.tracker.ServiceTracker;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.model.RouterConfigItem;
import de.soco.software.simuspace.suscore.data.model.RouterConfigList;

/**
 * This class responsible for bundle activation , start bundle ,stop and change/update evaluations
 */
@Log4j2
public class Activator implements BundleActivator, BundleListener, BlueprintListener {

    /**
     * The configurable properties to read from karaf.
     */
    private static Properties properties;

    /**
     * db url property.
     */
    private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";

    /**
     * db user name property.
     */
    private static final String HIBERNATE_CONNECTION_USER_NAME = "hibernate.connection.username";

    /**
     * db user pwd property.
     */
    private static final String HIBERNATE_CONNECTION_PASS = "hibernate.connection.password";

    /**
     * Maximum number of actual connection in the pool.
     */
    private static final String HIBERNATE_HIKARI_MAXIMUM_POOL_SIZE = "hibernate.hikari.maximumPoolSize";

    /**
     * Minimum number of ideal connections in the pool.
     */
    private static final String HIBERNATE_HIKARI_MINIMUM_IDLE = "hibernate.hikari.minimumIdle";

    /**
     * The Constant HIBERNATE_HIKARI_MAX_LIFETIME.
     */
    private static final String HIBERNATE_HIKARI_MAX_LIFETIME = "hibernate.hikari.setMaxLifetime";

    /**
     * Maximum time that a connection is allowed to sit ideal in the pool.
     */
    private static final String HIBERNATE_HIKARI_IDLE_TIMEOUT = "hibernate.hikari.idleTimeout";

    /**
     * Maximum waiting time for a connection from the pool.
     */
    private static final String HIBERNATE_HIKARI_CONNECTION_TIMEOUT = "hibernate.hikari.connectionTimeout";

    private static final String HIBERNATE_DEFAULT_SCHEMA = "hibernate.default_schema";

    private static final Object LIFECYCLE_BUNDLE_NAME = "de.soco.software.simuspace.suscore-lifecycle";

    /**
     * db driver class property.
     */
    private static final String HIBERNATE_CONNECTION_DRIVER_CLASS = "hibernate.connection.driver_class";

    /**
     * hibernate dialect property.
     */
    private static final String HIBERNATE_DIALECT = "hibernate.dialect";

    /**
     * Hibernate auto property to manage db create/update.
     */
    private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

    /**
     * Hibernate current session property.
     */
    private static final String HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS = "hibernate.current_session_context_class";

    /**
     * Hibernate property to show genearted sql on console.
     */
    private static final String SHOW_SQL = "hibernate.show_sql";

    /**
     * The Constant HIBERNATE_SEARCH_DIRECTORY_PROVIDER.
     */
    private static final String HIBERNATE_SEARCH_DIRECTORY_PROVIDER = "hibernate.search.default.directory_provider";

    /**
     * The Constant HIBERNATE_SEARCH_LOCKING_STRATEGY.
     */
    private static final String HIBERNATE_SEARCH_LOCKING_STRATEGY = "hibernate.search.default.locking_strategy";

    /**
     * The Constant HIBERNATE_GENERATE_STATISTICS.
     */
    private static final String HIBERNATE_GENERATE_STATISTICS = "hibernate.generate_statistics";

    /**
     * The Constant HIBERNATE_CONNECTION_AUTOCOMMIT.
     */
    private static final String HIBERNATE_CONNECTION_AUTOCOMMIT = "hibernate.connection.autocommit";

    /**
     * The Constant HIBERNATE_CONNECTION_PROVIDER_DISABLES_AUTOCOMMIT.
     */
    private static final String HIBERNATE_CONNECTION_PROVIDER_DISABLES_AUTOCOMMIT = "hibernate.connection.provider_disables_autocommit";

    /**
     * Peristance name.
     */
    private static final String UNMANAGED_JPA = "unmanaged-jpa";

    private static boolean isLifeCycleUp = false;

    private static EntityManagerFactory entityManagerFactory;

    /**
     * The hibernate session factory.
     */
    private static SessionFactory sessionFactory;

    private ServiceRegistration< EntityManagerFactory > serviceRegistrationForEntityManagerFactory;

    private ServiceRegistration< SessionFactory > serviceRegistrationForSessionFactory;

    /**
     * Routers for configurations items.
     */
    private static Map< String, List< RouterConfigItem > > routers = new HashMap<>();

    private static String typeAsString( BundleEvent event ) {
        if ( event == null ) {
            return "null";
        }
        int type = event.getType();
        switch ( type ) {
            case BundleEvent.INSTALLED:
                return "INSTALLED";
            case BundleEvent.LAZY_ACTIVATION:
                return "LAZY_ACTIVATION";
            case BundleEvent.RESOLVED:
                return "RESOLVED";
            case BundleEvent.STARTED:
                return "STARTED";
            case BundleEvent.STARTING:
                return "Starting";
            case BundleEvent.STOPPED:
                return "STOPPED";
            case BundleEvent.UNINSTALLED:
                return "UNINSTALLED";
            case BundleEvent.UNRESOLVED:
                return "UNRESOLVED";
            case BundleEvent.UPDATED:
                return "UPDATED";
            default:
                return "unknown event type: " + type;
        }
    }

    /**
     * Start the bundle listener
     */
    @Override
    public void start( BundleContext context ) throws Exception {
        log.info( "Starting Bundle Listener - " + context.hashCode() );
        context.addBundleListener( this );
        context.registerService( BlueprintListener.class, this, null );
        entityManagerFactory = getEntityManagerFactory( context );
        sessionFactory = getSessionFactory( context );
        serviceRegistrationForEntityManagerFactory = context.registerService( EntityManagerFactory.class, entityManagerFactory, null );
        serviceRegistrationForSessionFactory = context.registerService( SessionFactory.class, sessionFactory, null );
    }

    /**
     * Stopped the currently running bundle
     */
    @Override
    public void stop( BundleContext context ) throws Exception {
        log.info( "Stopping Bundle Listener - " + context.hashCode() );
        context.removeBundleListener( this );
        entityManagerFactory.close();
        sessionFactory.close();
        serviceRegistrationForEntityManagerFactory.unregister();
        serviceRegistrationForSessionFactory.unregister();
    }

    /**
     * Evaluate the change of the bundle and update the listener.
     */
    @Override
    public void bundleChanged( BundleEvent event ) {
        String symbolicName = event.getBundle().getSymbolicName();
        String type = typeAsString( event );

        log.info( "BundleChanged: " + symbolicName + ", event.type: " + type );

        evaluatePlugin( event );
    }

    /**
     * Evaluate the plugin Read from router configuration list
     *
     * @param event
     */
    private void evaluatePlugin( BundleEvent event ) {
        if ( event.getType() == BundleEvent.STARTED ) {
            Bundle plugin = event.getBundle();
            URL routerURL = plugin.getResource( "sus/router.json" );

            if ( routerURL == null ) {
                return;
            }

            RouterConfigList rcl = null;
            try ( InputStream is = routerURL.openStream() ) {
                String routerText = IOUtils.toString( is, "UTF-8" );
                rcl = JsonUtils.jsonToObject( routerText, RouterConfigList.class );
            } catch ( IOException e ) {
                log.warn( e );
            }
            if ( rcl != null ) {
                routers.put( rcl.getName(), rcl.getRoutes() );
            }

        }
        if ( event.getType() == BundleEvent.STOPPED ) {
            Bundle plugin = event.getBundle();
            URL routerURL = plugin.getResource( "sus/router.json" );

            if ( routerURL == null ) {
                return;
            }

            RouterConfigList rcl = null;
            try ( InputStream is = routerURL.openStream() ) {
                String routerText = IOUtils.toString( is, "UTF-8" );
                rcl = JsonUtils.jsonToObject( routerText, RouterConfigList.class );
                if ( routers.containsKey( rcl.getName() ) ) {
                    routers.remove( rcl.getName() );
                }
            } catch ( IOException e ) {
                log.warn( e );
            }
        }

    }

    public static Map< String, List< RouterConfigItem > > getRouters() {
        return routers;
    }

    /**
     * Gets the routers by plugin name.
     *
     * @param pluginName
     *         the plugin name
     *
     * @return the router by plugin name
     */
    public static List< RouterConfigItem > getRoutersBypluginName( String pluginName ) {
        return routers.get( pluginName );
    }

    @Override
    public void blueprintEvent( BlueprintEvent event ) {
        BlueprintState state = getState( event );
        Bundle bundle = event.getBundle();
        log.info( "SuS:>> Blueprint app state changed to " + state + " for bundle " + Long.valueOf( event.getBundle().getBundleId() ) );
        if ( bundle.getSymbolicName().equals( LIFECYCLE_BUNDLE_NAME ) ) {
            isLifeCycleUp = event.getType() == BlueprintEvent.CREATED;
        }
    }

    /**
     * Gets the state.
     *
     * @param blueprintEvent
     *         the blueprint event
     *
     * @return the state
     */
    private BlueprintState getState( BlueprintEvent blueprintEvent ) {
        switch ( blueprintEvent.getType() ) {
            case 1:
                return BlueprintState.CREATING;
            case 2:
                return BlueprintState.CREATED;
            case 3:
                return BlueprintState.DESTROYING;
            case 4:
                return BlueprintState.DESTROYED;
            case 5:
                return BlueprintState.FAILURE;
            case 6:
                return BlueprintState.GRACEPERIOD;
            case 7:
                return BlueprintState.WAITING;
        }
        return BlueprintState.UNKNOWN;
    }

    /**
     * The Enum BlueprintState.
     */
    public enum BlueprintState {

        /**
         * The Unknown.
         */
        UNKNOWN,
        /**
         * The Creating.
         */
        CREATING,
        /**
         * The Created.
         */
        CREATED,
        /**
         * The Destroying.
         */
        DESTROYING,
        /**
         * The Destroyed.
         */
        DESTROYED,
        /**
         * The Failure.
         */
        FAILURE,
        /**
         * The Grace period.
         */
        GRACEPERIOD,
        /**
         * The Waiting.
         */
        WAITING;
    }

    public static boolean isLifeCycleUp() {
        return isLifeCycleUp;
    }

    public void setLifeCycleUp( boolean isLifeCycleUp ) {
        this.isLifeCycleUp = isLifeCycleUp;
    }

    /**
     * Gets session.
     *
     * @return the session
     *
     * @deprecated since SDM-4144. All DAO usage removed. only used in test files
     */
    @Deprecated( forRemoval = true )
    public static Session getSession() {
        if ( sessionFactory != null ) {
            Session session = sessionFactory.openSession();
            return session;
        }
        log.info( "Hibernate session is null" );
        return null;
    }

    /**
     * Gets the pool.
     *
     * @param ds
     *         the ds
     *
     * @return the pool
     */
    private static HikariPool getPool( HikariDataSource ds ) {
        try {
            Field field = ds.getClass().getDeclaredField( "pool" );
            field.setAccessible( true );
            return ( HikariPool ) field.get( ds );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, Activator.class );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Prints the connection details.
     *
     * @param session
     *         the session
     */
    private static void printConnectionDetails( Session session ) {
        Map< String, Object > sadf = session.getEntityManagerFactory().getProperties();
        for ( Entry< String, Object > entry : sadf.entrySet() ) {
            if ( "hibernate.connection.datasource" == entry.getKey() ) {
                HikariPool pool = getPool( ( HikariDataSource ) entry.getValue() );
                log.info( "Total Connection :" + pool.getTotalConnections() + "    Active Connection :" + pool.getActiveConnections()
                        + "    Free Connection :" + ( pool.getTotalConnections() - pool.getActiveConnections() )
                        + "    Threads Awaiting Connection :" + pool.getThreadsAwaitingConnection() + "    Idle Connection :"
                        + pool.getIdleConnections() );

            }
        }
    }

    private static SessionFactory getSessionFactory( BundleContext context ) {
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = getEntityManagerFactory( context ).unwrap( SessionFactory.class );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, Activator.class );
            throw e;
        }
        return sessionFactory;
    }

    private static EntityManagerFactory getEntityManagerFactory( BundleContext context ) {
        EntityManagerFactory entityManagerFactory = null;
        PersistenceProvider persistenceProvider = null;
        ServiceTracker connectionServiceTracker = new ServiceTracker( context, PersistenceProvider.class.getName(), null );
        connectionServiceTracker.open();
        try {
            persistenceProvider = ( PersistenceProvider ) connectionServiceTracker.waitForService( 10000 );
        } catch ( InterruptedException e2 ) {
            Thread.currentThread().interrupt();
            return null;
        }
        Properties props = new Properties();
        setPropertiesForUrl();
        props.put( "hibernate.connection.datasource", getDataSourceFromConfig() );
        props.put( HIBERNATE_CONNECTION_DRIVER_CLASS, properties.getProperty( HIBERNATE_CONNECTION_DRIVER_CLASS ) );
        props.put( HIBERNATE_DIALECT, properties.getProperty( HIBERNATE_DIALECT ) );
        props.put( HIBERNATE_HBM2DDL_AUTO, properties.getProperty( HIBERNATE_HBM2DDL_AUTO ) );
        props.put( HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS, properties.getProperty( HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS ) );
        props.put( SHOW_SQL, properties.getProperty( SHOW_SQL ) );
        props.put( HIBERNATE_SEARCH_DIRECTORY_PROVIDER, properties.getProperty( HIBERNATE_SEARCH_DIRECTORY_PROVIDER ) );
        props.put( HIBERNATE_SEARCH_LOCKING_STRATEGY, properties.getProperty( HIBERNATE_SEARCH_LOCKING_STRATEGY ) );
        props.put( HIBERNATE_CONNECTION_AUTOCOMMIT, "false" );
        props.put( HIBERNATE_CONNECTION_PROVIDER_DISABLES_AUTOCOMMIT, "true" );
        props.put( "hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.internal.SingletonEhcacheRegionFactory" );
        props.put( "hibernate.cache.provider_class", "net.sf.ehcache.hibernate.EhCacheProvider" );
        props.put( "hibernate.cache.use_second_level_cache", "true" );
        props.put( "hibernate.cache.use_query_cache", "true" );
        props.put( "hibernate.cache.ehcache.missing_cache_strategy", "create" );
        props.put( "javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE" );
        props.put( "net.sf.ehcache.configurationResourceName", "/WEB-INF/classes/ehcache.xml" );
        props.put( HIBERNATE_GENERATE_STATISTICS, properties.getProperty( HIBERNATE_GENERATE_STATISTICS ) );
        props.put( Environment.DEFAULT_SCHEMA, properties.getProperty( HIBERNATE_DEFAULT_SCHEMA ) );

        try {
            entityManagerFactory = persistenceProvider.createEntityManagerFactory( UNMANAGED_JPA, props );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, Activator.class );
            throw e;
        }
        return entityManagerFactory;
    }

    private static HikariDataSource getDataSourceFromConfig() {
        HikariConfig jdbcConfig = new HikariConfig();
        jdbcConfig.setJdbcUrl( properties.getProperty( HIBERNATE_CONNECTION_URL ) );
        jdbcConfig.setUsername( properties.getProperty( HIBERNATE_CONNECTION_USER_NAME ) );
        jdbcConfig.setPassword( properties.getProperty( HIBERNATE_CONNECTION_PASS ) );

        jdbcConfig.setMaximumPoolSize( Integer.valueOf( properties.getProperty( HIBERNATE_HIKARI_MAXIMUM_POOL_SIZE ).trim() ) );
        jdbcConfig.setMinimumIdle( Integer.valueOf( properties.getProperty( HIBERNATE_HIKARI_MINIMUM_IDLE ) ) );
        jdbcConfig.setMaxLifetime( Integer.valueOf( properties.getProperty( HIBERNATE_HIKARI_MAX_LIFETIME ) ) );
        jdbcConfig.setIdleTimeout( Integer.valueOf( properties.getProperty( HIBERNATE_HIKARI_IDLE_TIMEOUT ) ) );
        jdbcConfig.setConnectionTimeout( Integer.valueOf( properties.getProperty( HIBERNATE_HIKARI_CONNECTION_TIMEOUT ) ) );
        jdbcConfig.setAutoCommit( false );

        return new HikariDataSource( jdbcConfig );
    }

    /**
     * Sets the properties for url.
     *
     * @throws SusException
     *         the sus exception
     */

    private static void setPropertiesForUrl() {
        properties = new Properties();
        final String hibernate_cfg = ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + File.separator
                + ConstantsString.HIBERNATE_CFG;
        try ( InputStream hibernateCfgStream = new FileInputStream( hibernate_cfg ) ) {
            properties.load( hibernateCfgStream );
        } catch ( final IOException e ) {
            ExceptionLogger.logMessage( e.getMessage() );
            throw new SusDataBaseException(
                    ( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), ConstantsString.HIBERNATE_CFG ) ) );
        }
    }

}