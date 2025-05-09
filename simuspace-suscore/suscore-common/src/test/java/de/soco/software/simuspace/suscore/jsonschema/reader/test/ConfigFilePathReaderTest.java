/**
 *
 */

package de.soco.software.simuspace.suscore.jsonschema.reader.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.ConfigUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * @author Ahmar Nadeem
 *
 * A test class verify ConfigFilePathReader class functionality
 */

public class ConfigFilePathReaderTest {

    /**
     * The Constant EXPECTED_PROJECT_CONFIG_NAME.
     */
    private static final String EXPECTED_PROJECT_CONFIG_NAME = "Project_Start_Config.js";

    /**
     * The Constant EXPECTED_WORKFLOW_PROJECT_CONFIG_NAME.
     */
    private static final String EXPECTED_WORKFLOW_PROJECT_CONFIG_NAME = "WorkflowProjectConfig.js";

    /**
     * The Constant PROPERTY_FILE_PATH.
     */
    private static final String PROPERTY_FILE_PATH = "src/test/resources/config.properties";

    /**
     * The Constant INVALID_PROPERTY_FILE_PATH.
     */
    private static final String INVALID_PROPERTY_FILE_PATH = "invalid_property_file_path";

    /**
     * The Constant INVALID_FILE_NAME.
     */
    private static final String INVALID_FILE_NAME = "invalidFile";

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * File property in the Properties file
     */
    private static final String PROP_FILE = "jsonschema.parent.filename";

    /**
     * User consent to proceed on data loss property in the Properties file
     */
    private static final String CONTINUE_IF_DATA_LOST_PROPERTY = "continue.if.data.lost";

    /**
     * Master Config file name property in the Properties file
     */
    private static final String MASTER_CONFIG = "MasterConfig.js";

    /**
     * A true value expected from the configuration file
     */
    private static final Boolean EXPECTED_VALUE_CONTINUE_IF_DATA_LOST = true;

    /**
     * A true value expected from the configuration file
     */
    private static final Boolean EXPECTED_VALUE_WHEN_NOT_PRESENT = false;

    /**
     * The name of the properties file
     */
    private static String PROPERTIES_FILE = "config.properties";

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    private static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        ReflectionUtils.setFinalStaticField( ConfigFilePathReader.class, ConfigFilePathReader.PROPERTIES_FILE_PATH_FIELD_NAME,
                ConstantsString.TEST_RESOURCE_PATH + "config.properties" ); // For a String
        ReflectionUtils.setFinalStaticField( ConstantsKaraf.class, ConstantsKaraf.STATIC_CONSTATN_FIELD_KARAF_CONF_PATH,
                ConstantsString.TEST_RESOURCE_PATH ); // For a String
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        mockControl().resetToNice();
    }

    @Test
    public void shouldGetPropertyByNameWhenValidPropertyFileNameAndPropertyNameIsPassed()
            throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtils.setFinalStaticField( ConstantsKaraf.class, ConstantsKaraf.STATIC_CONSTATN_FIELD_KARAF_CONF_PATH,
                ConstantsString.TEST_RESOURCE_PATH ); // For a String
        Assert.assertEquals( MASTER_CONFIG, ConfigFilePathReader.getValueByNameFromPropertiesFileInKaraf( PROP_FILE, PROPERTIES_FILE ) );
    }

    /**
     * Should throw exception property file does not exist.
     *
     * @throws NoSuchFieldException
     *         the no such field exception
     * @throws IllegalAccessException
     *         the illegal access exception
     */
    @Test
    public void shouldThrowExceptionPropertyFileDoesNotExist() throws NoSuchFieldException, IllegalAccessException {
        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), ConstantsString.TEST_RESOURCE_PATH ) );
        ReflectionUtils.setFinalStaticField( ConstantsKaraf.class, ConstantsKaraf.STATIC_CONSTATN_FIELD_KARAF_CONF_PATH,
                ConstantsString.TEST_RESOURCE_PATH + PROPERTIES_FILE ); // For a String
        ConfigFilePathReader.getValueByNameFromPropertiesFileInKaraf( PROP_FILE, PROPERTIES_FILE );
    }

    /**
     * This function tests that the utility function returns the absolute path of the master config
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @NOTE: The expectation here is that the karaf's conf folder path is not available in the test environment, hence the function will
     * always throw FileNotFoundException.
     */
    @Test
    public void shouldGetValidPropertyValueFromConfigurationIfValidKeyProvided() throws FileNotFoundException, IOException {
        EasyMock.replay();
        // Verify the continue if data lost property is returned
        String actualyPropertyValue = ConfigFilePathReader.getPropertyValueFromConfiguration( CONTINUE_IF_DATA_LOST_PROPERTY );
        Assert.assertNotNull( actualyPropertyValue );
        Assert.assertEquals( actualyPropertyValue, EXPECTED_VALUE_CONTINUE_IF_DATA_LOST.toString() );

        // Verify if master config file name is returned
        String expected = MASTER_CONFIG;
        String actual = ConfigFilePathReader.getPropertyValueFromConfiguration( PROP_FILE );
        Assert.assertEquals( expected, actual );
    }

    /**
     * This function tests that the utility function returns the absolute path of the master config
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @NOTE: The expectation here is that the karaf's conf folder path is not available in the test environment, hence the function will
     * always throw FileNotFoundException.
     */
    @Test
    public void shouldGetMasterConfigCompletePathWhenPropertyNamedAsJsonSchemaFileExistsInTheConfig()
            throws FileNotFoundException, IOException {
        EasyMock.replay();
        // Verify the continue if data lost property is returned
        String actualyPropertyValue = ConfigFilePathReader.getMasterConfigFileAbsolutePath( PROP_FILE );
        Assert.assertNotNull( actualyPropertyValue );
        boolean actual = true;
        boolean expected = actualyPropertyValue.contains( MASTER_CONFIG );

        Assert.assertEquals( expected, actual );
    }

    /**
     * This function tests the utility function to return true or false if the property exists in the config.properties files and return
     * false if the property not found in the config.properties file.
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    @Test
    public void shouldReturnTrueOrFalseWhenPropertyExistsAndReturnFalseWhenPropertyDoesNotExistInPropertiesFile()
            throws FileNotFoundException, IOException, NoSuchFieldException, IllegalAccessException {

        // Verify if continue if data lost property is returned as boolean
        boolean actualyBooleanPropertyValue = ConfigFilePathReader
                .getBooleanPropertyValueFromConfiguration( CONTINUE_IF_DATA_LOST_PROPERTY );
        Assert.assertNotNull( actualyBooleanPropertyValue );
        Assert.assertEquals( EXPECTED_VALUE_CONTINUE_IF_DATA_LOST, actualyBooleanPropertyValue );

        ReflectionUtils.setFinalStaticField( ConfigFilePathReader.class, ConfigFilePathReader.PROPERTIES_FILE_PATH_FIELD_NAME,
                ConstantsString.TEST_RESOURCE_PATH + "nopropconfig.properties" ); // For a String
        ConfigFilePathReader.setConfigProperties( new Properties() );
        boolean actualyPropertyValue = ConfigFilePathReader.getBooleanPropertyValueFromConfiguration( CONTINUE_IF_DATA_LOST_PROPERTY );
        boolean expected = EXPECTED_VALUE_WHEN_NOT_PRESENT;
        Assert.assertNotNull( actualyPropertyValue );
        Assert.assertEquals( expected, actualyPropertyValue );
    }

    /**
     * The function tests that the returned absolute path contains the passed file name
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void shouldGetAbsolutePathWhenValidFileNameStringProvided() throws FileNotFoundException, IOException {
        String path = ConfigFilePathReader.getCompleteFilePath( MASTER_CONFIG );
        Assert.assertNotNull( path );

        boolean actual = true;
        boolean expected = path.contains( MASTER_CONFIG );

        Assert.assertEquals( expected, actual );
    }

    /**
     * Should return stream when valid file name is passing.
     */
    @Test
    public void shouldReturnStreamWhenValidFileNameIsPassing() {
        InputStream path = ConfigFilePathReader.getFileByNameFromKaraf( MASTER_CONFIG );
        Assert.assertNotNull( path );
    }

    /**
     * Should throw exception when file name null.
     */
    @Test
    public void shouldThrowExceptionWhenFileNameNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(),
                ConstantsString.TEST_RESOURCE_PATH + INVALID_FILE_NAME ) );
        InputStream path = ConfigFilePathReader.getFileByNameFromKaraf( INVALID_FILE_NAME );
        Assert.assertNotNull( path );
    }

    /**
     * Should fetch properties of A porperty file when valid file is provided.
     *
     * @throws IOException
     */
    @Test
    public void shouldFetchPropertiesOfAPorpertyFileWhenValidFileIsProvided() throws IOException {
        Properties properties = ConfigFilePathReader.fetchProperty( PROPERTY_FILE_PATH );
        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_TWO, properties.size() );
        Assert.assertEquals( MASTER_CONFIG, properties.getProperty( PROP_FILE ) );

        Assert.assertEquals( Boolean.TRUE.toString(), properties.getProperty( CONTINUE_IF_DATA_LOST_PROPERTY ) );
    }

    /**
     * @throws IOException
     *         when valid file is provided it should fetch properties.
     */
    @Test
    public void shouldFetchPropertiesOfAPorpertyFileWhenValidFileIsProvidedaa() throws IOException {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.FETCHING_PROPERTY_ISSUE.getKey() ) );
        ConfigFilePathReader.fetchProperty( INVALID_PROPERTY_FILE_PATH );
    }

    /**
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     *         when valid file name is provided it should get master config file.
     * @throws IOException
     */

    @Test
    public void shouldGetMasterConfigurationFileNamesWhenValidFileNameIsProvided()
            throws NoSuchFieldException, IllegalAccessException, IOException {
        ReflectionUtils.setFinalStaticField( ConstantsKaraf.class, ConstantsKaraf.STATIC_CONSTATN_FIELD_KARAF_CONF_PATH,
                ConstantsString.TEST_RESOURCE_PATH ); // For a String
        List< ConfigUtil.Config > propertyFileNames = ConfigFilePathReader.getMasterConfigurationFileNames( PROP_FILE );
        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_TWO, propertyFileNames.size() );
        Assert.assertEquals( EXPECTED_PROJECT_CONFIG_NAME, propertyFileNames.get( FIRST_INDEX ) );
    }

    /**
     * Should get master configuration file names for workflows when valid file name is provided.
     *
     * @throws NoSuchFieldException
     *         the no such field exception
     * @throws IllegalAccessException
     *         the illegal access exception
     * @throws IOException
     */
    @Test
    public void shouldGetMasterConfigurationFileNamesForWorkflowsWhenValidFileNameIsProvided()
            throws NoSuchFieldException, IllegalAccessException, IOException {
        ReflectionUtils.setFinalStaticField( ConstantsKaraf.class, ConstantsKaraf.STATIC_CONSTATN_FIELD_KARAF_CONF_PATH,
                ConstantsString.TEST_RESOURCE_PATH ); // For a String
        List< ConfigUtil.Config > propertyFileNames = ConfigFilePathReader.getMasterConfigurationFileNamesForWorkflows( PROP_FILE );
        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_ONE, propertyFileNames.size() );
        Assert.assertEquals( EXPECTED_WORKFLOW_PROJECT_CONFIG_NAME, propertyFileNames.get( FIRST_INDEX ) );
    }

    /**
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     *         when provided config not exist it should throw error.
     * @throws IOException
     */
    @Test
    public void shouldThrowExceptionWhenProvidedMasterConfigNotExist() throws NoSuchFieldException, IllegalAccessException, IOException {
        thrown.expect( IOException.class );
        ReflectionUtils.setFinalStaticField( ConstantsKaraf.class, ConstantsKaraf.STATIC_CONSTATN_FIELD_KARAF_CONF_PATH,
                ConstantsString.TEST_RESOURCE_PATH ); // For a String
        ConfigFilePathReader.getMasterConfigurationFileNames( INVALID_PROPERTY_FILE_PATH );
    }

}
