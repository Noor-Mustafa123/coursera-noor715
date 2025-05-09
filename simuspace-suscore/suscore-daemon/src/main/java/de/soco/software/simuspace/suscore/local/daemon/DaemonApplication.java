package de.soco.software.simuspace.suscore.local.daemon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.model.UserConfigFile;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.local.daemon.controller.impl.SyncControllerImpl;
import de.soco.software.simuspace.suscore.local.daemon.model.DeamonResponse;

/**
 * The class is responsible to start and stop running local daemon.
 *
 * @author Nosheen.Shrif
 */

/**
 * The Class DaemonApplication.
 */

@EnableWebMvc
@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@EntityScan( { "de.soco.software.simuspace.suscore.local.daemon.entity" } )
@EnableJpaRepositories( { "de.soco.software.simuspace.suscore.local.daemon.dao" } )
@ComponentScan( { "de.soco.software.simuspace.suscore.local.daemon", "de.soco.software.simuspace.suscore.local.daemon.config",
        "de.soco.software.simuspace.suscore.local.daemon.controller", "de.soco.software.simuspace.suscore.local.daemon.controller.impl",
        "de.soco.software.simuspace.suscore.local.daemon.manager", "de.soco.software.simuspace.suscore.local.daemon.manager.impl",
        "de.soco.software.simuspace.suscore.local.daemon.model", "de.soco.software.simuspace.suscore.local.daemon.thread",
        "de.soco.software.simuspace.suscore.local.daemon.thread.service",
        "de.soco.software.simuspace.suscore.local.daemon.thread.service.impl" } )
public class DaemonApplication {

    /**
     * The Constant API_WORKFLOW_ISUP.
     */
    public static final String API_WORKFLOW_ISUP = "api/data/project/isup";

    /**
     * The constant API_GET_ALL_PROPERTIES.
     */
    public static final String API_GET_ALL_PROPERTIES = "api/data/project/properties";

    /**
     * The Constant API_WORKFLOW_ADDPID.
     */
    public static final String API_WORKFLOW_ADDPID = "api/workflow/addPID/%d";

    /**
     * The Constant DAEMON_PORT_FILE_NAME.
     */
    public static final String DAEMON_PORT_FILE_NAME = "/.daemonPort";

    /**
     * The Constant HTTP_LOCALHOST_SHUTDOWN.
     */
    public static final String HTTP_LOCALHOST_URI = "http://localhost:%d/%s";

    /**
     * The Constant daemonPropertyFilePath.
     */
    public static final String daemonPropertyFilePath = System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator
            + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY + DAEMON_PORT_FILE_NAME;

    /**
     * The Constant message PORT_ALREAD_IN_USE.
     */
    private static final String PORT_ALREAD_IN_USE = "Connector configured to listen on port %s failed to start";

    /**
     * The Constant PORT_KEY_IN_PROPERTY_FILE.
     */
    private static final String PORT_KEY_IN_PROPERTY_FILE = "port";

    /**
     * springApplication key constant for port .
     */
    private static final String SERVER_PORT = "server.port";

    /**
     * The Constant susClientPIDList.
     */
    private static final List< Integer > susClientPIDList = new ArrayList<>();

    /**
     * This method will be the starting point of susclient Designer to start Deamon.
     *
     * @param args
     *         the arguments
     */
    public static void main( String[] args ) {

        // configureLog4jFilePathOnRuntime( args );

        Integer susClientPID = 0;
        int port = getFreePort( false );
        // overriding existing property
        System.setProperty( "SERVER_PORT", port + "" );
        if ( isDaemonUp( port ) ) {

            System.out.println( "****************isDaemonUp  000000  ***************" );
            System.out.println( port );

            System.out.println( JsonUtils.toFilteredJson( new String[]{},
                    ResponseUtils.successResponse( "Daemon already running", new DeamonResponse( port ) ) ) );
            // call API of remote daemon to add PID
            addPIDToExistingDaemon( port, susClientPID );
            System.exit( 1 );
        }

        System.out.println( "isDaemonUp False >>>" );
        System.out.println( "**************** 111111  ***************" );
        System.out.println( "" );

        // create the parser
        Options options = new Options();
        options.addOption( "pid", true, "Provide PID of SIMuSPACE-Client" );
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            // CommandLine line = parser.parse( options, args );
            // if ( line.hasOption( "pid" ) ) {
            if ( true ) {
                System.out.println( "**************** line.hasOption( \"pid\" )   333  ***************" );
                // susClientPID = Integer.parseInt( line.getOptionValue( "pid" ) );
                susClientPID = 767678;
                // System.out.println( line.getOptionValue( "pid" ) );
            } else {
                System.err.println( "SIMuSPACE-Client PID not provided" );
                System.exit( 20 );
            }
        } catch ( Exception exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }

        try {

            System.out.println( "**************** startDaemon     ***************" );
            System.out.println( "" );
            startDaemon( args, port, susClientPID );

        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, DaemonApplication.class );
        } catch ( final Exception e ) {
            try {
                if ( e.getMessage().contains( String.format( PORT_ALREAD_IN_USE, port ) ) ) {
                    if ( isDaemonUp( port ) ) {
                        System.out.println( JsonUtils.toFilteredJson( new String[]{},
                                ResponseUtils.successResponse( "Daemon already running", new DeamonResponse( port ) ) ) );
                        // call API of remote daemon to add PID
                        addPIDToExistingDaemon( port, susClientPID );
                        System.exit( 1 );
                    } else {
                        System.exit( 2 );
                    }
                }

            } catch ( JsonSerializationException jse ) {
                ExceptionLogger.logException( jse, DaemonApplication.class );
            }
            ExceptionLogger.logException( e, DaemonApplication.class );
        }
    }

    /**
     * Gets the free port.
     *
     * @param updateExistingPort
     *         the update existing port
     *
     * @return the free port
     */
    private static int getFreePort( boolean updateExistingPort ) {

        File file = new File( daemonPropertyFilePath );
        Properties daemonProperties = new Properties();

        if ( !file.exists() || !file.isFile() || file.length() == 0 || updateExistingPort ) {

            try {
                file.createNewFile();
                FileUtils.setGlobalReadFilePermissions( file );
            } catch ( IOException e1 ) {
                ExceptionLogger.logException( e1, DaemonApplication.class );
            }

            try ( OutputStream outPut = new FileOutputStream( file ) ) {
                int freePortToRunDaemon = CommonUtils.getOpenPort();
                daemonProperties.put( PORT_KEY_IN_PROPERTY_FILE, Integer.toString( freePortToRunDaemon ) );
                daemonProperties.store( outPut, null );
            } catch ( IOException e ) {
                ExceptionLogger.logException( e, DaemonApplication.class );
            }

        } else {
            FileUtils.setGlobalReadFilePermissions( file );
            try ( InputStream input = new FileInputStream( file ) ) {
                daemonProperties.load( input );
            } catch ( IOException e ) {
                ExceptionLogger.logException( e, DaemonApplication.class );
            }

        }

        return Integer.parseInt( daemonProperties.get( PORT_KEY_IN_PROPERTY_FILE ).toString() );
    }

    /**
     * Checks if is daemon up.
     *
     * @param port
     *         the port
     *
     * @return true, if is daemon up
     */
    private static boolean isDaemonUp( int port ) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity< SusResponseDTO > response = restTemplate
                    .getForEntity( String.format( HTTP_LOCALHOST_URI, port, API_WORKFLOW_ISUP ), SusResponseDTO.class );
            if ( response != null
                    && response.getBody().getMessage().getContent().contentEquals( SyncControllerImpl.DAEMON_IS_UP_AND_RUNNING ) ) {
                return true;
            }
        } catch ( RestClientException e ) {
            return false;
        }
        return false;
    }

    /**
     * Adds the PID to existing daemon.
     *
     * @param port
     *         the port
     * @param susClientPID
     *         the sus client PID
     *
     * @return true, if successful
     */
    private static boolean addPIDToExistingDaemon( int port, Integer susClientPID ) {
        ExceptionLogger.logMessage( ">>addPIDToExistingDaemon susClientPID: " + susClientPID );
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity< SusResponseDTO > response = restTemplate.getForEntity(
                    String.format( HTTP_LOCALHOST_URI, port, String.format( API_WORKFLOW_ADDPID, susClientPID ) ), SusResponseDTO.class );
            if ( response != null && response.getBody().getMessage().getContent().contentEquals( "added" ) ) {
                return true;
            }
        } catch ( RestClientException e ) {
            return false;
        }
        return false;
    }

    /**
     * Adds the PID.
     *
     * @param susClientPID
     *         the sus client PID
     *
     * @return true, if successful
     */
    public static boolean addPID( Integer susClientPID ) {
        ExceptionLogger.logMessage( ">>addPID susClientPIDList: " + susClientPIDList );
        if ( !susClientPIDList.contains( susClientPID ) ) {
            susClientPIDList.add( susClientPID );
        }
        ExceptionLogger.logMessage( ">>addPID susClientPIDList: " + susClientPIDList );
        return true;
    }

    /**
     * Removes the PID.
     *
     * @param susClientPID
     *         the sus client PID
     *
     * @return true, if successful
     */
    public static boolean removePID( Integer susClientPID ) {
        ExceptionLogger.logMessage( ">>removePID susClientPIDList: " + susClientPIDList );
        if ( susClientPIDList.contains( susClientPID ) ) {
            susClientPIDList.remove( susClientPID );
        }
        ExceptionLogger.logMessage( "<<removePID susClientPIDList: " + susClientPIDList );
        return true;
    }

    /**
     * Checks if is PID list empty.
     *
     * @return true, if is PID list empty
     */
    public static boolean isPIDListEmpty() {
        ExceptionLogger.logMessage( ">>isPIDListEmpty susClientPIDList: " + susClientPIDList );
        if ( susClientPIDList.isEmpty() ) {
            return true;
        }
        return false;
    }

    /**
     * Starts daemon on specified port.
     *
     * @param args
     *         the args
     * @param port
     *         the port
     * @param suSClientPID
     *         the su S client PID
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    private static void startDaemon( String[] args, int port, Integer suSClientPID ) {
        HashMap< String, Object > startUpProperties = new HashMap<>();
        startUpProperties.put( SERVER_PORT, port );
        System.out.println( startUpProperties.toString() );
        new SpringApplicationBuilder().sources( DaemonApplication.class ).properties( startUpProperties ).bannerMode( Banner.Mode.CONSOLE )
                .run( new String[]{} );

        System.out.println( "daemon startrfd " );
        DeamonResponse daemonPort = new DeamonResponse( port );
        addPID( suSClientPID );

        // get All properties
        new RestTemplate().getForEntity( String.format( HTTP_LOCALHOST_URI, port, API_GET_ALL_PROPERTIES ), SusResponseDTO.class );
        // To print on console
        System.out.println( JsonUtils.toFilteredJson( new String[]{}, ResponseUtils
                .successResponse( MessageBundleFactory.getDefaultMessage( Messages.DEAMON_STARTED.getKey(), port ), daemonPort ) ) );
    }

    /**
     * Configure log 4 j file path on runtime.
     *
     * @param args
     *         the args
     */
    private static void configureLog4jFilePathOnRuntime( String[] args ) {
        try {
            InputStream configStream = DaemonApplication.class.getResourceAsStream( "/log4j.properties" );

            Properties props = new Properties();
            props.load( configStream );
            String pathLog = getDaemonLogPathFromConfig();
            Properties props2 = new Properties();
            Set< String > keys = props.stringPropertyNames();
            for ( String key : keys ) {
                System.out.println( key + " : " + props.getProperty( key ) );
                if ( key.equalsIgnoreCase( "log4j.appender.file.File" ) ) {
                    props2.setProperty( key, pathLog );
                } else {
                    props2.setProperty( key, props.getProperty( key ) );
                }
            }
            LogManager.resetConfiguration();
            PropertyConfigurator.configure( props2 );
        } catch ( Exception e ) {
            System.err.println( "daemon Log4j error" + e );
        }
    }

    /**
     * Gets the daemon log path from config.
     *
     * @return the daemon log path from config
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static String getDaemonLogPathFromConfig() throws IOException {
        final String propFileName = System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator
                + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY + File.separator + ConstantsString.CONFIG + File.separator
                + ConstantsString.CONIGFILE;
        final File file = new File( propFileName );
        UserConfigFile userConfigFile = null;

        try ( final InputStream targetConfigStream = new FileInputStream( file ) ) {
            userConfigFile = JsonUtils.jsonToObject( targetConfigStream, UserConfigFile.class );
        }

        return userConfigFile.getDaemonLog();
    }

}