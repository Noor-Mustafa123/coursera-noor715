package de.soco.software.simuspace.workflow.activator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.workflow.main.WFApplication;
import de.soco.software.simuspace.workflow.properties.EnginePropertiesManager;

/**
 * The Class WfEngineActivate updates log4j properties after initialization
 *
 * @author noman arshad
 */
public class WfEngineActivate {

    /**
     * The engine host properties.
     */
    private static Properties engineHostProperties;

    /**
     * The email properties.
     */
    private static Properties emailProperties;

    /**
     * Inits the.
     */
    public void init() {
        try {
            // createPythonConfiguration();
            // configureLog4jFilePathOnRuntime();
        } catch ( Exception e ) {
        }
    }

    /**
     * Configure.
     */
    public void configure() {
        try {
            createPythonConfiguration();
            createEmailConfiguration();
        } catch ( Exception e ) {
        }

    }

    /**
     * Gets the server log file path.
     *
     * @return the server log file path
     */
    public static String getServerLogFilePath() {
        return engineHostProperties.getProperty( ConstantsFileProperties.SERVER_LOG_PATH );
    }

    /**
     * Gets the sge job python script path.
     *
     * @return the sge job python script path
     */
    public static String getSgePythonPath() {
        return engineHostProperties.getProperty( ConstantsFileProperties.PREPARE_SGE_JOB )
                .replace( "${karaf.base}", EnginePropertiesManager.getKarafBasePath() );
    }

    /**
     * Gets the python execution path on server.
     *
     * @return the python execution path on server
     */
    public static String getPythonExecutionPath() {
        return engineHostProperties.getProperty( ConstantsFileProperties.PYTHON_PATH ).trim();
    }

    /**
     * Gets the email address.
     *
     * @return the email port
     */
    public static String getEmailAddress() {
        return emailProperties.getProperty( ConstantsFileProperties.EMAIL_ADDRESS );
    }

    /**
     * Gets the email password.
     *
     * @return the email password
     */
    public static String getEmailPassword() {
        return emailProperties.getProperty( ConstantsFileProperties.EMAIL_PASSWORD );
    }

    /**
     * Gets the email host.
     *
     * @return the email host
     */
    public static String getEmailHost() {
        return emailProperties.getProperty( ConstantsFileProperties.EMAIL_HOST );
    }

    /**
     * Gets the email port.
     *
     * @return the email port
     */
    public static String getEmailPort() {
        return emailProperties.getProperty( ConstantsFileProperties.EMAIL_PORT );
    }

    /**
     * Gets the starttls enable.
     *
     * @return the email port
     */
    public static String getEmailStarttlsEnable() {
        return emailProperties.getProperty( ConstantsFileProperties.EMAIL_STARTTLS_ENABLE );
    }

    /**
     * Gets the email ssl protocols.
     *
     * @return the email port
     */
    public static String getEmailSslProtocols() {
        return emailProperties.getProperty( ConstantsFileProperties.EMAIL_SSL_PROTOCOLS );
    }

    /**
     * Gets the mail protocol.
     *
     * @return the mail protocol
     */
    public static String getMailProtocol() {
        return emailProperties.getProperty( ConstantsFileProperties.MAIL_PROTOCOL );
    }

    /**
     * Gets the smtps auth.
     *
     * @return the smtps auth
     */
    public static String getSmtpAuth() {
        return emailProperties.getProperty( ConstantsFileProperties.SMTP_AUTH );
    }

    /**
     * Creates the python configuration.
     */
    private static void createPythonConfiguration() {
        engineHostProperties = new Properties();
        try ( InputStream engineStream = new FileInputStream( FilenameUtils.getFullPathNoEndSeparator(
                URLDecoder.decode( WFApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8" ) )
                + File.separator + "engine_host.conf" ) ) {
            engineHostProperties.load( engineStream );
        } catch ( final IOException e ) {
            ExceptionLogger.logMessage( e.getMessage() );
            throw new SusDataBaseException(
                    ( MessageBundleFactory.getDefaultMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), "engine_host.conf" ) ) );
        }
    }

    /**
     * Creates the email configuration.
     */
    private static void createEmailConfiguration() {
        emailProperties = new Properties();
        try ( InputStream emailStream = new FileInputStream( FilenameUtils.getFullPathNoEndSeparator(
                URLDecoder.decode( WFApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8" ) )
                + File.separator + "email.conf" ) ) {
            emailProperties.load( emailStream );
        } catch ( final IOException e ) {
            ExceptionLogger.logMessage( e.getMessage() );
            throw new SusDataBaseException(
                    ( MessageBundleFactory.getDefaultMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), "email.conf" ) ) );
        }
    }

}
