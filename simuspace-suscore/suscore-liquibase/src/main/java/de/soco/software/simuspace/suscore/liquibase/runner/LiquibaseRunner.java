package de.soco.software.simuspace.suscore.liquibase.runner;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * This class is responsible for updating the schema using the liquibase
 *
 * @author Zeeshan Jamal
 */
public class LiquibaseRunner {

    /**
     * The constant CONNECTION_DRIVER_CLASS for connection driver class.
     */
    private static final String CONNECTION_DRIVER_CLASS = "driver";

    /**
     * The constant CONNECTION_URL for connection url.
     */
    private static final String CONNECTION_URL = "url";

    /**
     * The constant DB_USER_NAME for connection driver class.
     */
    private static final String DB_USER_NAME = "username";

    /**
     * The constant DB_PASS for database password.
     */
    private static final String DB_PASS = "password";

    /**
     * The constant CHANGE_LOG_FILE for liquibase change log file
     */
    private static final String CHANGE_LOG_FILE = "changeLogFile";

    /**
     * The constant LIQUIBASE_PROPERTY_FILE for liquibase properties file.
     */
    private static final String LIQUIBASE_PROPERTY_FILE = "resources/liquibase.properties";

    /**
     * The configurable properties to read from liquibase.properties
     */
    private static Properties properties;

    /**
     * Starting point for liquibase change log execution
     *
     * @param arg
     *         command line arguments
     */
    public static void main( String[] arg ) {
        loadProperties();
        try ( Connection conn = DriverManager.getConnection( properties.getProperty( CONNECTION_URL ),
                properties.getProperty( DB_USER_NAME ), properties.getProperty( DB_PASS ) ) ) {

            Class.forName( properties.getProperty( CONNECTION_DRIVER_CLASS ) );

            final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation( new JdbcConnection( conn ) );

            final Liquibase liquibase = new Liquibase( properties.getProperty( CHANGE_LOG_FILE ), new ClassLoaderResourceAccessor(),
                    database );

            liquibase.update( new Contexts() );

        } catch ( Exception e ) {
            Logger.getLogger( LiquibaseRunner.class.getSimpleName() ).log( new LogRecord( Level.SEVERE, e.getMessage() ) );
        }
    }

    /**
     * Used to read liquibase.properties file and load properties from it
     */
    public static void loadProperties() {
        try ( FileInputStream fis = new FileInputStream( LIQUIBASE_PROPERTY_FILE ); ) {
            properties = new Properties();
            properties.load( fis );
        } catch ( Exception e ) {
            Logger.getLogger( LiquibaseRunner.class.getSimpleName() ).log( new LogRecord( Level.SEVERE, e.getMessage() ) );
        }
    }

}
