package de.soco.software.simuspace.suscore.transfer.service.rest.impl;

import javax.ws.rs.core.Response;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.http.model.AcknowledgeBytes;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.transfer.manager.impl.SusTransferManagerImpl;

/**
 * The Class TransferServiceImplTest provides transferring and receiving files and directories test cases.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
@PrepareForTest( SusTransferServiceImpl.class )
public class SusTransferServiceImplTest {

    /**
     * The transfer service impl.
     */
    private SusTransferServiceImpl transferServiceImpl;

    /**
     * The transfer manager impl.
     */
    private SusTransferManagerImpl transferManagerImpl;

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant CREATE_DIRECTORY_PAYLOAD.
     */
    private static final String FILE = "{\"file\":\"abc\"}";

    /**
     * The Constant HTTP_RECEIVER.
     */
    private static final String HTTP_RECEIVER = "{\"filePath\":\"abc\",\"initialRange\":\"90\",\"maxRange\":\"180\",\"totalFileSize\":\"360\",\"receivedBytes\":\"whateveritis\",\"chunk\":\"90\"}";

    /**
     * The Constant HTTP_SENDER.
     */
    private static final String HTTP_SENDER = "{\"srcFile\":\"abc\",\"location\":\"http://localhost\",\"ackBytes\":\"180\",\"destinationFilePath\":\"abc\",\"chunk\":\"90\"}";

    /**
     * The Constant ACKNOWLEDGE_BYTES.
     */
    private static final String ACKNOWLEDGE_BYTES = "{\"file\":\"/home/sces120/ahsan/apache-karaf-current-latest/bin/hh.q\",\"transferredBytes\":\"0\"}";

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        transferServiceImpl = new SusTransferServiceImpl();
        transferManagerImpl = mockControl.createMock( SusTransferManagerImpl.class );
        transferServiceImpl.setTransferManager( transferManagerImpl );
    }

    /**
     * Should proceed to create directory successfully.
     */
    @Test
    public void shouldProceedToCreateDirectorySuccessfully() {
        String expectedMsg = "Directory created successfully";
        EasyMock.expect( transferManagerImpl.createDirectory( EasyMock.anyObject() ) ).andReturn( true ).anyTimes();
        mockControl.replay();
        Response expected = transferServiceImpl.createDirectory( FILE );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( susResponseDTO.getData() );
        Assert.assertEquals( expectedMsg, susResponseDTO.getMessage().getContent() );
    }

    /**
     * Should not proceed to create directory.
     */
    @Test
    public void shouldNotProceedToCreateDirectory() {
        String expectedMsg = "Unable to create directory.";
        EasyMock.expect( transferManagerImpl.createDirectory( EasyMock.anyObject() ) ).andReturn( false ).anyTimes();
        mockControl.replay();
        Response expected = transferServiceImpl.createDirectory( FILE );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( expectedMsg, susResponseDTO.getMessage().getContent() );
    }

    /**
     * Should receive chunk and proceed.
     */
    @Test
    public void shouldReceiveChunkAndProceed() {
        String expectedMsg = "Initial Range : 90   Max Range : 180  Total Size : 360  Chunk : 90";
        EasyMock.expect( transferManagerImpl.receiveFile( EasyMock.anyObject() ) ).andReturn( expectedMsg ).anyTimes();
        mockControl.replay();
        Response expected = transferServiceImpl.receiveFile( HTTP_RECEIVER );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( susResponseDTO.getData() );
        Assert.assertEquals( expectedMsg, susResponseDTO.getData() );
    }

    /**
     * Should proceed to transfer file or directory.
     */
    @Test
    public void shouldProceedToTransferFileOrDirectory() {
        String expectedMsg = "File uploaded successfully";
        EasyMock.expect( transferManagerImpl.sendFile( EasyMock.anyObject() ) ).andReturn( "File uploaded successfully" ).anyTimes();
        mockControl.replay();
        Response expected = transferServiceImpl.sendFile( HTTP_SENDER );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( susResponseDTO.getData() );
        Assert.assertEquals( expectedMsg, susResponseDTO.getData() );
    }

    /**
     * Should acknowledge about the file bytes.
     */
    @Test
    public void shouldAcknowledgeAboutTheFileBytes() {
        AcknowledgeBytes expectedAcknowledgeBytes = new AcknowledgeBytes();
        expectedAcknowledgeBytes.setFile( new File( "abc" ) );
        expectedAcknowledgeBytes.setTransferredBytes( 776 );
        EasyMock.expect( transferManagerImpl.acknowledgeBytes( EasyMock.anyObject() ) ).andReturn( expectedAcknowledgeBytes ).anyTimes();
        mockControl.replay();
        Response expected = transferServiceImpl.acknowledgeBytes( ACKNOWLEDGE_BYTES );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( susResponseDTO.getData() );
        String json = JsonUtils.convertMapToString( ( Map< String, String > ) susResponseDTO.getData() );
        AcknowledgeBytes actualAcknowledgeBytes = JsonUtils.jsonToObject( json, AcknowledgeBytes.class );
        Assert.assertEquals( expectedAcknowledgeBytes.getTransferredBytes(), actualAcknowledgeBytes.getTransferredBytes() );
        Assert.assertEquals( expectedAcknowledgeBytes.getFile().getName(), actualAcknowledgeBytes.getFile().getName() );
    }

    /**
     * Should acknowledge about file check sum.
     */
    @Test
    public void shouldAcknowledgeAboutFileCheckSum() {
        Map< String, String > expectedMap = new HashMap<>();
        expectedMap.put( "file", "85bcde3ef0659b6d20ce43c9cbcf364c86834139ad7e25319cf9bc83cccb58" );
        EasyMock.expect( transferManagerImpl.acknowledgeCheckSum( EasyMock.anyObject() ) ).andReturn( expectedMap ).anyTimes();
        mockControl.replay();
        String filePathAsMap = "{\"file\":\"85bcde3ef0659b6d20ce43c9cbcf364c86834139ad7e25319cf9bc83cccb58\"}";
        Response expected = transferServiceImpl.acknowledgeCheckSum( filePathAsMap );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( susResponseDTO.getData() );
        String json = JsonUtils.convertMapToString( ( Map< String, String > ) susResponseDTO.getData() );
        Map< String, String > actualMap = JsonUtils.convertStringToMap( json );
        Assert.assertEquals( expectedMap.get( "file" ), actualMap.get( "file" ) );
    }

    /**
     * Should acknowledge about the destination OS file seperator.
     */
    @Test
    public void shouldAcknowledgeAboutTheDestinationOSFileSeperator() {
        Map< String, String > expectedMap = new HashMap<>();
        expectedMap.put( "fileSeparator", "/" );
        EasyMock.expect( transferManagerImpl.detectFileSeparator() ).andReturn( expectedMap ).anyTimes();
        mockControl.replay();
        Response expected = transferServiceImpl.detectFileSeparator();
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( susResponseDTO.getData() );
        String json = JsonUtils.convertMapToString( ( Map< String, String > ) susResponseDTO.getData() );
        Map< String, String > actualMap = JsonUtils.convertStringToMap( json );
        Assert.assertEquals( expectedMap.get( "fileSeparator" ), actualMap.get( "fileSeparator" ) );
    }

    /**
     * Should proceed to sync file.
     */
    @Test
    public void shouldProceedToSyncFile() {
        String expectedMsg = "File is synced successfully";
        EasyMock.expect( transferManagerImpl.syncFile( EasyMock.anyObject() ) ).andReturn( "File is synced successfully" ).anyTimes();
        mockControl.replay();
        Response expected = transferServiceImpl.syncFiles( HTTP_SENDER );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( susResponseDTO.getData() );
        Assert.assertEquals( expectedMsg, susResponseDTO.getData() );
    }

    /**
     * Should proceed to remove file.
     */
    @Test
    public void shouldProceedToRemoveFile() {
        EasyMock.expect( transferManagerImpl.removeFile( EasyMock.anyObject() ) ).andReturn( true ).anyTimes();
        mockControl.replay();
        Response expected = transferServiceImpl.removeFile( HTTP_SENDER );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( susResponseDTO.getData() );
        Assert.assertEquals( true, susResponseDTO.getData() );
    }

}