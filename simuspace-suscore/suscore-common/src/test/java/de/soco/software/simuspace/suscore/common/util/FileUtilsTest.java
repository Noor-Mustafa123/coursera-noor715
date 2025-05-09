package de.soco.software.simuspace.suscore.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.expectation.PowerMockitoStubber;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDocument;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.http.model.AcknowledgeBytes;
import de.soco.software.simuspace.suscore.common.http.model.HttpReceiver;
import de.soco.software.simuspace.suscore.common.http.model.HttpSender;

/**
 * Test Cases For FileUtils Class.
 *
 * @author Nosheen.Sharif
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { URL.class, HttpUtils.class, RandomAccessFile.class, SuSClient.class, JsonUtils.class } )
public class FileUtilsTest {

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * constant NUMBER_OF_FOLDERS_IN_DIRECTORY.
     */
    private static final int NUMBER_OF_FOLDERS_IN_DIRECTORY = 1;

    /**
     * constant for valid zip file path.
     */
    private static final String VALID_ZIP_SOURCE_FILE_PATH = "src/test/resources/Test.zip";

    /**
     * constant for valid zip file path.
     */
    private static final String VALID_SOURCE_FOLDER_PATH = "src/test/resources/Test/";

    /**
     * The Constant TEST_OBJECT_FILE.
     */
    private static final String TEST_OBJECT_FILE = "testobjectfile";

    /**
     * constant for valid zip file path.
     */
    private static final String VALID_SOURCE_FOLDER_FILE_NAME = "TestZipContentFile.txt";

    /**
     * constant for valid zip file path.
     */
    private static final String INVALID_FILE_NAME = "TestZipContentFile22.txt";

    /**
     * constant for In valid zip file path.
     */
    private static final String INVALID_ZIP_SOURCE_FILE_PATH = "src/test/resources/Test222.zip";

    /**
     * constant for valid zip file path.
     */
    private static final String VALID_ZIP_DESTINATION_PATH = "src/test/resources/";

    /**
     * constant for valid zip file folder.
     */
    private static final File EXPECTED_ZIP_OUTPUT_FILE = new File( "src/test/resources/Test/TestZipContentFile.txt" );

    /**
     * message for test case of invalid paths.
     */
    private static final String MSG_FOR_INVALID_PATHS = "Not Valid zip source/destination paths";

    /**
     * constant for valid zip file path.
     */
    private static final String FOLDER_AS_CHILDS_PATH = "src/test/resources/testData/";

    /** **************************** extractZipFile ********************************************. */

    /**
     * The Constant OBJECT_NAME_CANNOT_EMPTY.
     */
    private static final String OBJECT_NAME_CANNOT_EMPTY = "Object Name cannot be empty or null.";

    /**
     * The Constant FILE_PATH_NOT_EXIST.
     */
    private static final String FILE_PATH_NOT_EXIST = "File path does Not Exist :/simuspace/suscore/abc.txt";

    /**
     * The Constant DUMMY_FILE_PATH.
     */
    private static final String DUMMY_FILE_PATH = "/simuspace/suscore/abc.txt";

    /**
     * The Constant DESTINATION_URL_IS_NOT_NULL_OR_EMPTY.
     */
    private static final String DESTINATION_URL_IS_NOT_NULL_OR_EMPTY = "Destination Url can not be null or empty.";

    /**
     * The Constant DESTINATION_FILE_PATH_IS_NOT_NULL_OR_EMPTY.
     */
    private static final String DESTINATION_FILE_PATH_IS_NOT_NULL_OR_EMPTY = "Destination file can not be null or empty.";

    /**
     * The Constant INVALID_URL_PROVIDED.
     */
    private static final String INVALID_URL_PROVIDED_FOR_HTTP = "Invalid url provided : http://localhost:8080";

    /**
     * The Constant INVALID_URL_PROVIDED_FOR_HTTPS.
     */
    private static final String INVALID_URL_PROVIDED_FOR_HTTPS = "Invalid url provided : https://localhost:8080";

    /**
     * The Constant DUMMY_EMPTY_LOCATION.
     */
    private static final String DUMMY_EMPTY_LOCATION = "";

    /**
     * The Constant LOCATION_WITH_HTTP.
     */
    private static final String LOCATION_WITH_HTTP = "http://localhost:8080";

    /**
     * The Constant LOCATION_WITH_HTTPS.
     */
    private static final String LOCATION_WITH_HTTPS = "https://localhost:8080";

    /**
     * The Constant IS_UP_FOR_HTTP.
     */
    private static final String IS_UP_FOR_HTTP = "isUpForHttp";

    /**
     * The Constant IS_UP_FOR_HTTPS.
     */
    private static final String IS_UP_FOR_HTTPS = "isUpForHttps";

    /**
     * The Constant NULL_DESTINATION_PATH.
     */
    private static final String NULL_DESTINATION_PATH = null;

    /**
     * The Constant EMPTY_DESTINATION_PATH.
     */
    private static final String EMPTY_DESTINATION_PATH = "";

    /**
     * The Constant DESTINATION_PATH.
     */
    private static final String DESTINATION_PATH = "src/test/resources/";

    /**
     * The Constant FILE_UPLOADED.
     */
    private static final String FILE_UPLOADED = "File uploaded successfully : imprtData.zip";

    /**
     * The Constant DESTINATION_NIX_FILE_SEPARATOR.
     */
    private static final String DESTINATION_NIX_FILE_SEPARATOR = "{fileSeparator=/}";

    /**
     * The Constant DESTINATION_NIX_FILE_SEPARATOR_JSON.
     */
    private static final String DESTINATION_NIX_FILE_SEPARATOR_JSON = "{\"fileSeparator\":\"/\"}";

    /**
     * The Constant DESTINATION_ACKNOWLEDGE_BYTES_WITH_ZERO_INDEX.
     */
    private static final String DESTINATION_ACKNOWLEDGE_BYTES_WITH_ZERO_INDEX = "{\"file\":\"/home/sces120/ahsan/apache-karaf-current-latest/bin/hh.q\",\"transferredBytes\":\"0\"}";

    /**
     * The Constant DESTINATION_ACKNOWLEDGE_BYTES_WITH_FOUR_HUNDRED_INDEX.
     */
    private static final String DESTINATION_ACKNOWLEDGE_BYTES_WITH_FOUR_HUNDRED_INDEX = "{\"file\":\"/home/sces120/ahsan/apache-karaf-current-latest/bin/hh.q\",\"transferredBytes\":\"400\"}";

    /**
     * The Constant DESTINATION_CORRECT_CHECKSUM.
     */
    private static final String DESTINATION_CORRECT_CHECKSUM = "{\"checkSum\":\"86407176\"},\"success\":true}";

    /**
     * The Constant DESTINATION_INCORRECT_CHECKSUM.
     */
    private static final String DESTINATION_INCORRECT_CHECKSUM = "{\"checkSum\":\"72c07a69a3769341e22253ecf616aaba\"},\"success\":true}";

    /**
     * The Constant FILE_COPIED_UNSUCCESSFULL.
     */
    private static final String FILE_COPIED_UNSUCCESSFULL = "File copied unsuccessful because CheckSum is invalid for file : imprtData.zip";

    /**
     * The Constant INVALID_INDEXER.
     */
    private static final String INVALID_INDEXER = "Invalid Indexer for file : imprtData.zip";

    /**
     * The Constant FIRST_CHUNK.
     */
    private static final String FIRST_CHUNK = "In CPython, the global interpreter lock, or GIL, is a mutex that protects access to Python";

    /**
     * The Constant SECOND_CHUNK.
     */
    private static final String SECOND_CHUNK = " objects, preventing multiple threads from executing Python bytecodes at once. This lock i";

    /**
     * The Constant THIRD_CHUNK.
     */
    private static final String THIRD_CHUNK = "s necessary mainly because CPython's memory management is not thread-safe. (However, since";

    /**
     * The Constant FOURTH_CHUNK.
     */
    private static final String FOURTH_CHUNK = " the GIL exists, other features have grown to depend on the guarantees that it enforces. )";

    /**
     * The src file to send.
     */
    private static String SRC_FILE_TO_SEND = "src/test/resources/testData/imprtData.zip";

    /**
     * The destination file to feed.
     */
    private static String DESTINATION_FILE_TO_FEED = "src/test/resources/testData/testFile1.txt";

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The http sender.
     */
    private HttpSender httpSender;

    /**
     * The file.
     */
    private File file;

    /**
     * expected extension to verify file extension.
     */
    private static final String EXPECTED_FILE_EXTENSION = "jpeg";

    /**
     * valid mime type for jpg image.
     */
    private static final String VALID_MIME_TYPE = "image/jpeg";

    /**
     * invalid mime type for testing negative scenario.
     */
    private static final String INVALID_MIME_TYPE = "document/docx";

    /**
     * a mocking object representing password to test encryption.
     */
    private static final String PASSWORD = "aaBB2563";

    /**
     * The Constant FILE_SEP.
     */
    private static final String FILE_SEP = "fileSeparator";

    /**
     * The Constant FILE.
     */
    private static final String FILE = "file";

    // ************** Transferring stuff************************

    /**
     * To initialize the objects and mocking objects.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Before
    public void setup() throws IOException {
        mockControl.resetToNice();
        file = Mockito.mock( File.class );
        Mockito.when( file.getAbsolutePath() ).thenReturn( DUMMY_FILE_PATH );

        PowerMockito.spy( SuSClient.class );
        PowerMockito.spy( JsonUtils.class );
        httpSender = new HttpSender();
    }

    /**
     * Should SuccessFully Extract Zip File On Destination If Both Path Are Valid.
     */
    @Test
    public void shouldSuccessFullyExtractZipFileOnDestinationIfBothPathAreValid() {
        boolean actual = false;
        try {
            actual = FileUtils.extractZipFile( VALID_ZIP_SOURCE_FILE_PATH, VALID_ZIP_DESTINATION_PATH );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        Assert.assertTrue( actual );
        Assert.assertTrue( EXPECTED_ZIP_OUTPUT_FILE.exists() );
    }

    /**
     * Should Failed To Extract Zip File If Source And Destination Paths Are Null.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldFailedToExtractZipFileIfSourceAndDestinationPathsAreNull() throws IOException {
        thrown.expect( SusException.class );
        thrown.expectMessage( MSG_FOR_INVALID_PATHS );

        FileUtils.extractZipFile( null, null );

    }

    /**
     * Should Failed To Extract Zip File If Source Path Does Not Exist.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldFailedToExtractZipFileIfSourcePathDoesNotExist() throws IOException {
        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessagesUtil.getMessage( WFEMessages.FILE_PATH_NOT_EXIST,
                        new File( INVALID_ZIP_SOURCE_FILE_PATH ).getAbsolutePath() ) );
        FileUtils.extractZipFile( INVALID_ZIP_SOURCE_FILE_PATH, VALID_ZIP_DESTINATION_PATH );

    }

    /**
     * ********************************* findAndReturnFile ************************************.
     */

    /**
     * Should Succesfully Find And Return File From Valid Directory Path
     */
    @Test
    public void shouldSuccesfullyFindAndReturnFileFromValidDirectoryPath() {
        File actual = FileUtils.findAndReturnFile( VALID_SOURCE_FOLDER_FILE_NAME, new File( VALID_SOURCE_FOLDER_PATH ) );

        Assert.assertEquals( VALID_SOURCE_FOLDER_FILE_NAME, actual.getName() );

    }

    /**
     * Should Not Find File In Valid Directory Path If File Name Does Not Match.
     */
    @Test
    public void shouldNotFindFileInValidDirectoryPathIfFileNameDoesNotMatch() {
        File expected = null;
        File actual = FileUtils.findAndReturnFile( INVALID_FILE_NAME, new File( VALID_SOURCE_FOLDER_PATH ) );

        Assert.assertEquals( expected, actual );

    }

    /**
     * ********************************* getDirectoryWithInFolder ************************************.
     */

    /**
     * Should Get Empty List Of Files If No Directory Exist In Given Folder Path
     */
    @Test
    public void shouldGetEmptyListOfFilesIfNoDirectoryExistInGivenFolderPath() {
        List< File > actual = FileUtils.getDirectoryWithInFolder( new File( VALID_SOURCE_FOLDER_PATH ) );
        Assert.assertTrue( actual.isEmpty() );
    }

    /**
     * Should Get List Of Files If Directory Exist In Given Folder Path.
     */

    @Test
    public void shouldGetListOfFilesIfDirectoryExistInGivenFolderPath() {
        int expected = NUMBER_OF_FOLDERS_IN_DIRECTORY;
        List< File > actual = FileUtils.getDirectoryWithInFolder( new File( FOLDER_AS_CHILDS_PATH ) );
        Assert.assertFalse( actual.isEmpty() );
        Assert.assertEquals( actual.size(), expected );
    }

    /**
     * Should Get Empty List Of Files If Null Is Passed As Directory Path Parameter.
     */
    @Test
    public void shouldGetEmptyListOfFilesIfNullIsPassedAsDirectoryPathParameter() {
        List< File > actual = FileUtils.getDirectoryWithInFolder( null );
        Assert.assertTrue( actual.isEmpty() );
    }

    // ********************** Transfering test cases start here

    /**
     * Should not proceed to send bytes if http sender is not provided.
     */
    @Test
    public void shouldNotProceedToSendBytesIfHttpSenderIsNotProvided() {
        HttpSender httpSender = null;
        thrown.expect( SusException.class );
        thrown.expectMessage( OBJECT_NAME_CANNOT_EMPTY );
        FileUtils.sendBytesFromFile( httpSender );
    }

    /**
     * Should not proceed to send bytes on invalid file path.
     */
    @Test
    public void shouldNotProceedToSendBytesOnInvalidFilePath() {
        Mockito.when( file.exists() ).thenReturn( false );
        thrown.expect( SusException.class );
        thrown.expectMessage( FILE_PATH_NOT_EXIST );
        httpSender = prepareHttpSender( file, DUMMY_EMPTY_LOCATION );
        FileUtils.sendBytesFromFile( httpSender );
    }

    /**
     * Should not proceed to send bytes if destination location is empty.
     */
    @Test
    public void shouldNotProceedToSendBytesIfDestinationLocationIsEmpty() {
        thrown.expect( SusException.class );
        thrown.expectMessage( DESTINATION_URL_IS_NOT_NULL_OR_EMPTY );
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, DUMMY_EMPTY_LOCATION );
        FileUtils.sendBytesFromFile( httpSender );
    }

    /**
     * Should not proceed to send bytes if destination location is null.
     */
    @Test
    public void shouldNotProceedToSendBytesIfDestinationLocationIsNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( DESTINATION_URL_IS_NOT_NULL_OR_EMPTY );
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, DUMMY_EMPTY_LOCATION );
        FileUtils.sendBytesFromFile( httpSender );
    }

    /**
     * Should not proceed to send bytes if destination location is not up on http.
     */
    @Test
    public void shouldNotProceedToSendBytesIfDestinationLocationIsNotUpOnHttp() {
        thrown.expect( SusException.class );
        thrown.expectMessage( INVALID_URL_PROVIDED_FOR_HTTP );
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, LOCATION_WITH_HTTP );
        mockHttpUtilsStaticFunctions( EasyMock.anyObject(), false, IS_UP_FOR_HTTP );
        FileUtils.sendBytesFromFile( httpSender );
    }

    /**
     * Should not proceed to send bytes if destination location is not up on https.
     */
    @Test
    public void shouldNotProceedToSendBytesIfDestinationLocationIsNotUpOnHttps() {
        thrown.expect( SusException.class );
        thrown.expectMessage( INVALID_URL_PROVIDED_FOR_HTTPS );
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, LOCATION_WITH_HTTPS );
        mockHttpUtilsStaticFunctions( EasyMock.anyObject(), false, IS_UP_FOR_HTTPS );
        FileUtils.sendBytesFromFile( httpSender );
    }

    /**
     * Should not proceed to send bytes if destination path is null.
     */
    @Test
    public void shouldNotProceedToSendBytesIfDestinationPathIsNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( DESTINATION_FILE_PATH_IS_NOT_NULL_OR_EMPTY );
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, LOCATION_WITH_HTTP, NULL_DESTINATION_PATH );
        mockHttpUtilsStaticFunctions( EasyMock.anyObject(), true, IS_UP_FOR_HTTP );
        FileUtils.sendBytesFromFile( httpSender );
    }

    /**
     * Should not proceed to send bytes if destination path is empty.
     */
    @Test
    public void shouldNotProceedToSendBytesIfDestinationPathIsEmpty() {
        thrown.expect( SusException.class );
        thrown.expectMessage( DESTINATION_FILE_PATH_IS_NOT_NULL_OR_EMPTY );
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, LOCATION_WITH_HTTP, EMPTY_DESTINATION_PATH );
        mockHttpUtilsStaticFunctions( EasyMock.anyObject(), true, IS_UP_FOR_HTTP );
        FileUtils.sendBytesFromFile( httpSender );
    }

    /**
     * Should proceed to send bytes having single chunk.
     */
    @Test
    public void shouldProceedToSendBytesHavingSingleChunk() {
        String expectedMsg = FILE_UPLOADED;
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, LOCATION_WITH_HTTP, DESTINATION_PATH );
        mockHttpUtilsStaticFunctions( EasyMock.anyObject(), true, IS_UP_FOR_HTTP );
        SusResponseDTO susResponseDTOFileSeperator = prepareSusResponseDTO( DESTINATION_NIX_FILE_SEPARATOR, null, true );
        mockSusClientGetRequest( susResponseDTOFileSeperator );
        mockJsonUtilToJson( DESTINATION_NIX_FILE_SEPARATOR_JSON, DESTINATION_ACKNOWLEDGE_BYTES_WITH_ZERO_INDEX,
                DESTINATION_CORRECT_CHECKSUM );
        SusResponseDTO susResponseDTOAcknowledgeBytes = prepareSusResponseDTO( DESTINATION_ACKNOWLEDGE_BYTES_WITH_ZERO_INDEX, null, true );
        SusResponseDTO susResponseDTOAcknowledgeCheckSum = prepareSusResponseDTO( DESTINATION_CORRECT_CHECKSUM, null, true );
        mockSusClientPostRequest( susResponseDTOAcknowledgeBytes, susResponseDTOAcknowledgeCheckSum );
        String actualMsg = FileUtils.sendBytesFromFile( httpSender );
        Assert.assertEquals( expectedMsg, actualMsg );
    }

    /**
     * Should acknowledge as exception if file not uploaded successfully.
     */
    @Test
    public void shouldAcknowledgeAsExceptionIfFileNotUploadedSuccessfully() {
        thrown.expect( SusException.class );
        thrown.expectMessage( FILE_COPIED_UNSUCCESSFULL );
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, LOCATION_WITH_HTTP, DESTINATION_PATH );
        mockHttpUtilsStaticFunctions( EasyMock.anyObject(), true, IS_UP_FOR_HTTP );
        SusResponseDTO susResponseDTOFileSeperator = prepareSusResponseDTO( DESTINATION_NIX_FILE_SEPARATOR, null, true );
        mockSusClientGetRequest( susResponseDTOFileSeperator );
        mockJsonUtilToJson( DESTINATION_NIX_FILE_SEPARATOR_JSON, DESTINATION_ACKNOWLEDGE_BYTES_WITH_ZERO_INDEX,
                DESTINATION_INCORRECT_CHECKSUM );
        SusResponseDTO susResponseDTOAcknowledgeBytes = prepareSusResponseDTO( DESTINATION_ACKNOWLEDGE_BYTES_WITH_ZERO_INDEX, null, true );
        SusResponseDTO susResponseDTOAcknowledgeCheckSum = prepareSusResponseDTO( DESTINATION_INCORRECT_CHECKSUM, null, true );
        mockSusClientPostRequest( susResponseDTOAcknowledgeBytes, susResponseDTOAcknowledgeCheckSum );
        PowerMockito.when( FileUtils.sendBytesFromFile( httpSender ) ).thenThrow( new SusException( FILE_COPIED_UNSUCCESSFULL ) );
    }

    /**
     * Should proceed in odd iteration to upload file.
     */
    @Test
    public void shouldProceedInOddIterationToUploadFile() {
        String expectedMsg = FILE_UPLOADED;
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, LOCATION_WITH_HTTP, DESTINATION_PATH );
        httpSender.setChunk( 150 );
        mockHttpUtilsStaticFunctions( EasyMock.anyObject(), true, IS_UP_FOR_HTTP );
        SusResponseDTO susResponseDTOFileSeperator = prepareSusResponseDTO( DESTINATION_NIX_FILE_SEPARATOR, null, true );
        mockSusClientGetRequest( susResponseDTOFileSeperator );
        mockJsonUtilToJson( DESTINATION_NIX_FILE_SEPARATOR_JSON, DESTINATION_ACKNOWLEDGE_BYTES_WITH_ZERO_INDEX,
                DESTINATION_CORRECT_CHECKSUM );
        SusResponseDTO susResponseDTOAcknowledgeBytes = prepareSusResponseDTO( DESTINATION_ACKNOWLEDGE_BYTES_WITH_ZERO_INDEX, null, true );
        SusResponseDTO susResponseDTOAcknowledgeCheckSum = prepareSusResponseDTO( DESTINATION_CORRECT_CHECKSUM, null, true );
        mockSusClientPostRequest( susResponseDTOAcknowledgeBytes, susResponseDTOAcknowledgeCheckSum );
        String actualMsg = FileUtils.sendBytesFromFile( httpSender );
        Assert.assertEquals( expectedMsg, actualMsg );
    }

    /**
     * Should acknowledge as exception if file not uploaded successfully in odd iteration.
     */
    @Test
    public void shouldAcknowledgeAsExceptionIfFileNotUploadedSuccessfullyInOddIteration() {
        thrown.expect( SusException.class );
        thrown.expectMessage( FILE_COPIED_UNSUCCESSFULL );
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, LOCATION_WITH_HTTP, DESTINATION_PATH );
        httpSender.setChunk( 150 );
        mockHttpUtilsStaticFunctions( EasyMock.anyObject(), true, IS_UP_FOR_HTTP );
        SusResponseDTO susResponseDTOFileSeperator = prepareSusResponseDTO( DESTINATION_NIX_FILE_SEPARATOR, null, true );
        mockSusClientGetRequest( susResponseDTOFileSeperator );
        mockJsonUtilToJson( DESTINATION_NIX_FILE_SEPARATOR_JSON, DESTINATION_ACKNOWLEDGE_BYTES_WITH_ZERO_INDEX,
                DESTINATION_INCORRECT_CHECKSUM );
        SusResponseDTO susResponseDTOAcknowledgeBytes = prepareSusResponseDTO( DESTINATION_ACKNOWLEDGE_BYTES_WITH_ZERO_INDEX, null, true );
        SusResponseDTO susResponseDTOAcknowledgeCheckSum = prepareSusResponseDTO( DESTINATION_INCORRECT_CHECKSUM, null, true );
        mockSusClientPostRequest( susResponseDTOAcknowledgeBytes, susResponseDTOAcknowledgeCheckSum );
        PowerMockito.when( FileUtils.sendBytesFromFile( httpSender ) ).thenThrow( new SusException( FILE_COPIED_UNSUCCESSFULL ) );
    }

    /**
     * Should not proceed to send bytes if acknowledge bytes greater than file length.
     */
    @Test
    public void shouldNotProceedToSendBytesIfAcknowledgeBytesGreaterThanFileLength() {
        thrown.expect( SusException.class );
        thrown.expectMessage( INVALID_INDEXER );
        final File initialFile = new File( SRC_FILE_TO_SEND );
        httpSender = prepareHttpSender( initialFile, LOCATION_WITH_HTTP, DESTINATION_PATH );
        mockHttpUtilsStaticFunctions( EasyMock.anyObject(), true, IS_UP_FOR_HTTP );
        SusResponseDTO susResponseDTOFileSeperator = prepareSusResponseDTO( DESTINATION_NIX_FILE_SEPARATOR, null, true );
        mockSusClientGetRequest( susResponseDTOFileSeperator );
        mockJsonUtilToJson( DESTINATION_NIX_FILE_SEPARATOR_JSON, DESTINATION_ACKNOWLEDGE_BYTES_WITH_FOUR_HUNDRED_INDEX,
                DESTINATION_INCORRECT_CHECKSUM );
        SusResponseDTO susResponseDTOAcknowledgeBytes = prepareSusResponseDTO( DESTINATION_ACKNOWLEDGE_BYTES_WITH_FOUR_HUNDRED_INDEX, null,
                true );
        SusResponseDTO susResponseDTOAcknowledgeCheckSum = prepareSusResponseDTO( DESTINATION_INCORRECT_CHECKSUM, null, true );
        mockSusClientPostRequest( susResponseDTOAcknowledgeBytes, susResponseDTOAcknowledgeCheckSum );
        PowerMockito.when( FileUtils.sendBytesFromFile( httpSender ) ).thenThrow( new SusException( INVALID_INDEXER ) );
    }

    /**
     * Should receive bytes in first chunk.
     */
    @Test
    public void shouldReceiveBytesInFirstChunk() {
        String expectedMsg = "Initial Range : 0   Max Range : 90  Total Size : 360  Chunk : 90";
        byte[] bytes = FIRST_CHUNK.getBytes();
        String bs = convertByteArrayToBase64String( bytes );
        HttpReceiver httpReceiver = prepareHttpReceiver( bs, DESTINATION_FILE_TO_FEED, 90, 0, 90, 360 );
        String actualMsg = FileUtils.writeBytesInFile( httpReceiver );
        Assert.assertEquals( expectedMsg, actualMsg );
    }

    /**
     * Should receive bytes in second chunk.
     */
    @Test
    public void shouldReceiveBytesInSecondChunk() {
        String expectedMsg = "Initial Range : 90   Max Range : 180  Total Size : 360  Chunk : 90";
        byte[] bytes = SECOND_CHUNK.getBytes();
        String bs = convertByteArrayToBase64String( bytes );
        HttpReceiver httpReceiver = prepareHttpReceiver( bs, DESTINATION_FILE_TO_FEED, 90, 90, 180, 360 );
        String actualMsg = FileUtils.writeBytesInFile( httpReceiver );
        Assert.assertEquals( expectedMsg, actualMsg );
    }

    /**
     * Should receive bytes in third chunk.
     */
    @Test
    public void shouldReceiveBytesInThirdChunk() {
        String expectedMsg = "Initial Range : 180   Max Range : 270  Total Size : 360  Chunk : 90";
        byte[] bytes = THIRD_CHUNK.getBytes();
        String bs = convertByteArrayToBase64String( bytes );
        HttpReceiver httpReceiver = prepareHttpReceiver( bs, DESTINATION_FILE_TO_FEED, 90, 180, 270, 360 );
        String actualMsg = FileUtils.writeBytesInFile( httpReceiver );
        Assert.assertEquals( expectedMsg, actualMsg );
    }

    /**
     * Should receive bytes in fourth chunk.
     */
    @Test
    public void shouldReceiveBytesInFourthChunk() {
        String expectedMsg = "Initial Range : 270   Max Range : 360  Total Size : 360  Chunk : 90";
        byte[] bytes = FOURTH_CHUNK.getBytes();
        String bs = convertByteArrayToBase64String( bytes );
        HttpReceiver httpReceiver = prepareHttpReceiver( bs, DESTINATION_FILE_TO_FEED, 90, 270, 360, 360 );
        String actualMsg = FileUtils.writeBytesInFile( httpReceiver );
        Assert.assertEquals( expectedMsg, actualMsg );
    }

    /**
     * Should successfully get file extension when A registered mime type provided.
     */
    @Test
    public void shouldSuccessfullyGetFileExtensionWhenARegisteredMimeTypeProvided() {
        String actualExt = FileUtils.verifyMimeTypeAndGetFileExtension( ConstantsDocument.REGISTERED_IMAGE_TYPES, VALID_MIME_TYPE );
        Assert.assertNotNull( actualExt );
        Assert.assertEquals( EXPECTED_FILE_EXTENSION, actualExt );
    }

    /**
     * Should throw exception when mime type is not registered.
     */
    @Test
    public void shouldThrowExceptionWhenMimeTypeIsNotRegistered() {
        thrown.expect( SusException.class );
        thrown.expectMessage(
                MessageBundleFactory.getMessage( Messages.DOCUMENT_YOU_HAVE_SELECTED_INVALID_IMAGE_OR_TYPE_ALLOWED_ARE.getKey(),
                        String.join( ConstantsString.COMMA, ConstantsDocument.REGISTERED_IMAGE_TYPES ) ) );
        FileUtils.verifyMimeTypeAndGetFileExtension( ConstantsDocument.REGISTERED_IMAGE_TYPES, INVALID_MIME_TYPE );
    }

    /**
     * test if the utility function converts the password to encrypted form successfully.
     */
    @Test
    public void shouldConvertStringToSHA256ChecksumWhenValidParameterProvided() {
        String validPassword = FileUtils.getSha256CheckSum( PASSWORD );
        Assert.assertNotEquals( PASSWORD, validPassword );
    }

    /**
     * Should return map of detected OS file separator.
     */
    @Test
    public void shouldReturnMapOfDetectedOSFileSeparator() {
        Map actual = FileUtils.detectOSFileSeparator();
        String expected = File.separator;
        Assert.assertEquals( expected, actual.get( FILE_SEP ) );
    }

    /**
     * Should return acknowledge bytes when map of file is provided.
     */
    @Test
    public void shouldReturnAcknowledgeBytesWhenMapOfFileIsProvided() {
        byte[] bytes = FIRST_CHUNK.getBytes();
        Map< String, String > map = new HashMap<>();
        String bs = convertByteArrayToBase64String( bytes );
        map.put( FILE, bs );
        AcknowledgeBytes actual = FileUtils.acknowledgeBytes( map );
        Assert.assertEquals( FIRST_CHUNK, actual.getFile().toString() );
    }

    /**
     * Should throw exception when empty map is passed for acknowledge bytes.
     */
    @Test
    public void shouldThrowExceptionWhenEmptyMapIsPassedForAcknowledgeBytes() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MAP_NOT_PROVIDED_FOR_ACKNOWLEDGE_BYTES.getKey() ) );
        Map< String, String > map = null;
        FileUtils.acknowledgeBytes( map );

    }

    /**
     * Should return true when map of file is provided for create directory.
     */
    @Test
    public void shouldReturnTrueWhenMapOfFileIsProvidedForCreateDirectory() {
        byte[] bytes = FIRST_CHUNK.getBytes();
        Map< String, String > map = new HashMap<>();
        String bs = convertByteArrayToBase64String( bytes );
        map.put( FILE, bs );
        boolean actual = FileUtils.createDirectory( map );
        Assert.assertTrue( actual );
    }

    /**
     * Should successfully read map from file when valid file is provided.
     */
    @Test
    public void shouldSuccessfullyConverFileContentsToStringWhenValidFileIsProvided() {
        String actual = FileUtils.convertFileContentsToString( new File( VALID_SOURCE_FOLDER_PATH + TEST_OBJECT_FILE ) );
        Assert.assertNotNull( actual );
    }

    /**
     * Should not successfully conver file contents to string and return empty string when invalid file is provided.
     */
    @Test
    public void shouldNotSuccessfullyConverFileContentsToStringAndReturnEmptyStringWhenInvalidFileIsProvided() {
        String actual = FileUtils.convertFileContentsToString( new File( INVALID_FILE_NAME ) );
        Assert.assertEquals( ConstantsString.EMPTY_STRING, actual );
    }

    /**
     * Should convert object to input stream when valid object is provided.
     */
    @Test
    public void shouldConvertObjectToInputStreamWhenValidObjectIsProvided() {
        InputStream convertObjectToInputStream = FileUtils
                .convertObjectToInputStream( FileUtils.convertFileContentsToString( new File( VALID_SOURCE_FOLDER_PATH ) ) );
        Assert.assertNotNull( convertObjectToInputStream );
    }

    /**
     * Should not convert object to input stream when in valid object is provided.
     */
    @Test
    public void shouldNotConvertObjectToInputStreamWhenInValidObjectIsProvided() {
        InputStream convertObjectToInputStream = FileUtils.convertObjectToInputStream( null );
        Assert.assertNotNull( convertObjectToInputStream );
    }

    /**
     * Clean.
     */
    @After
    public void clean() {
        File file = new File( DESTINATION_FILE_TO_FEED );
        file.delete();
    }

    /**
     * Prepare http receiver.
     *
     * @param bs
     *         the bs
     * @param destination_path
     *         the destination path
     * @param chunk
     *         the chunk
     * @param initialRange
     *         the initial range
     * @param maxRange
     *         the max range
     * @param totalFileSize
     *         the total file size
     *
     * @return the http receiver
     */
    private HttpReceiver prepareHttpReceiver( String bs, String destination_path, long chunk, long initialRange, long maxRange,
            long totalFileSize ) {
        HttpReceiver httpReceiver = new HttpReceiver();
        httpReceiver.setFilePath( destination_path );
        httpReceiver.setChunk( chunk );
        httpReceiver.setInitialRange( initialRange );
        httpReceiver.setMaxRange( maxRange );
        httpReceiver.setTotalFileSize( totalFileSize );
        httpReceiver.setReceivedBytes( bs );
        return httpReceiver;
    }

    /**
     * Convert byte array to base 64 string.
     *
     * @param fileBytes
     *         the file bytes
     *
     * @return the string
     */
    private static String convertByteArrayToBase64String( byte[] fileBytes ) {
        if ( fileBytes == null || fileBytes.length == 0 ) {
            return "";
        }
        return Base64.getEncoder().encodeToString( fileBytes );
    }

    /**
     * Mock sus client post request.
     *
     * @param susResponseDTOAcknowledgeBytes
     *         the sus response DTO acknowledge bytes
     * @param susResponseDTOAcknowledgeCheckSum
     *         the sus response DTO acknowledge check sum
     */
    private static void mockSusClientPostRequest( SusResponseDTO susResponseDTOAcknowledgeBytes,
            SusResponseDTO susResponseDTOAcknowledgeCheckSum ) {
        try {
            ( ( PowerMockitoStubber ) PowerMockito.doReturn( susResponseDTOAcknowledgeBytes )
                    .doReturn( susResponseDTOAcknowledgeCheckSum ) ).when( SuSClient.class, "postRequest", Matchers.anyString(),
                    Matchers.anyString(), Matchers.anyObject() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Mock json util to json.
     *
     * @param jsonSplit
     *         the json split
     * @param jsonAcknowledgeBytes
     *         the json acknowledge bytes
     * @param jsonCheckSum
     *         the json check sum
     */
    private static void mockJsonUtilToJson( String jsonSplit, String jsonAcknowledgeBytes, String jsonCheckSum ) {
        try {
            ( ( PowerMockitoStubber ) PowerMockito.doReturn( jsonSplit ).doReturn( jsonAcknowledgeBytes ).doReturn( jsonCheckSum ) )
                    .when( JsonUtils.class, "toJson", Matchers.anyObject(), Matchers.any() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Mock sus client get request.
     *
     * @param susResponseDTO
     *         the sus response DTO
     */
    private void mockSusClientGetRequest( SusResponseDTO susResponseDTO ) {
        try {
            PowerMockito.doReturn( susResponseDTO ).when( SuSClient.class, "getRequest", Matchers.anyString(), Matchers.anyObject() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Prepare sus response DTO.
     *
     * @param data
     *         the data
     * @param message
     *         the message
     * @param success
     *         the success
     *
     * @return the sus response DTO
     */
    private SusResponseDTO prepareSusResponseDTO( String data, Message message, boolean success ) {
        SusResponseDTO susResponseDTOFileSeperator = new SusResponseDTO();
        susResponseDTOFileSeperator.setData( data );
        susResponseDTOFileSeperator.setMessage( message );
        susResponseDTOFileSeperator.setSuccess( success );
        return susResponseDTOFileSeperator;
    }

    /**
     * Prepare http sender.
     *
     * @param initialFile
     *         the initial file
     * @param location
     *         the location
     * @param destinationPath
     *         the destination path
     *
     * @return the http sender
     */
    private HttpSender prepareHttpSender( final File initialFile, final String location, final String destinationPath ) {
        HttpSender httpSender = new HttpSender();
        httpSender.setSrcFile( initialFile );
        httpSender.setLocation( location );
        httpSender.setDestinationFilePath( destinationPath );
        return httpSender;
    }

    /**
     * Mock static functions.
     *
     * @param url
     *         the url
     * @param check
     *         the check
     * @param method
     *         the method
     */
    private void mockHttpUtilsStaticFunctions( URL url, boolean check, String method ) {
        PowerMockito.spy( HttpUtils.class );
        try {
            PowerMockito.doReturn( check ).when( HttpUtils.class, method, Matchers.anyObject() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Prepare http sender.
     *
     * @param srcFile
     *         the src file
     * @param location
     *         the location
     *
     * @return the http sender
     */
    private HttpSender prepareHttpSender( File srcFile, String location ) {
        httpSender.setSrcFile( srcFile );
        httpSender.setLocation( location );
        return httpSender;
    }

}
