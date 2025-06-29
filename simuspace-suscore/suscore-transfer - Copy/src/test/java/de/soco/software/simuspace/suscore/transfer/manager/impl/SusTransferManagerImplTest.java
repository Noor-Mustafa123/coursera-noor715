package de.soco.software.simuspace.suscore.transfer.manager.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.http.model.HttpSender;
import de.soco.software.simuspace.suscore.common.util.FileUtils;

/**
 * The Class TransferManagerImplTest provides test cases of transferring and receiving files/directories.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class SusTransferManagerImplTest {

    /**
     * The Constant FILE.
     */
    private static final String FILE = "file";

    /**
     * The Constant CHECKSUM.
     */
    private static final String CHECKSUM = "checkSum";

    /**
     * The transfer manager impl.
     */
    private SusTransferManagerImpl transferManagerImpl;

    /**
     * The http sender.
     */
    private HttpSender httpSender;

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
     * The Constant DEST_SYNC_PATH.
     */
    private static final String DEST_SYNC_PATH = "destSyncPath";

    /**
     * The local sync get dir response.
     */
    public final String LOCAL_SYNC_GET_DIR = "L2hvbWUvc2NlczEyMC9haHNhbi8yLjA=";

    /**
     * The src file to sync.
     */
    private static String SRC_FILE_TO_SYNC = "src/test/resources/testData/imprtData.zip";

    /**
     * The dest file to sync.
     */
    private static String DEST_FILE_TO_SYNC = "src/test/resources/testData/imprtData1.zip";

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        transferManagerImpl = new SusTransferManagerImpl();
        httpSender = new HttpSender();
    }

    /**
     * Should calculate check sum.
     */
    @Test
    public void shouldCalculateCheckSum() {
        Map< String, String > expectedMap = fileCheckSum();
        Map< String, String > map = hexlifiedFile( FILE );
        Map< String, String > actualMap = transferManagerImpl.acknowledgeCheckSum( map );
        Assert.assertEquals( expectedMap.get( CHECKSUM ), actualMap.get( CHECKSUM ) );
    }

    /**
     * Should sync file when valid input provided.
     */
    @Ignore
    @Test
    public void shouldSyncFileWhenValidInputProvidedWithDestination() {
        String expectedMsg = "File is synced";
        httpSender.setSrcFile( new File( SRC_FILE_TO_SYNC ) );
        httpSender.setDestinationFilePath( DEST_FILE_TO_SYNC );
        PowerMockito.mockStatic( FileUtils.class );
        PowerMockito.when( FileUtils.syncFile( Mockito.anyObject() ) ).thenReturn( "File is synced" );

        String actual = transferManagerImpl.syncFile( httpSender );
        Assert.assertEquals( expectedMsg, actual );

    }

    /**
     * Should sync file when valid input provided.
     *
     * @throws Exception
     */
    @Ignore
    @Test
    public void shouldSyncFileWhenValidInputProvided() throws Exception {
        String expectedMsg = "File is synced";
        httpSender.setSrcFile( new File( SRC_FILE_TO_SYNC ) );
        Map< String, String > responseMap = new HashMap<>();
        responseMap.put( DEST_SYNC_PATH, LOCAL_SYNC_GET_DIR );
        SusResponseDTO susResponseDTO = new SusResponseDTO( true, null, responseMap );
        PowerMockito.spy( FileUtils.class );
        PowerMockito.doReturn( httpSender ).when( FileUtils.class, "syncFile", Matchers.anyObject() );
        PowerMockito.spy( SuSClient.class );
        PowerMockito.doReturn( susResponseDTO ).when( SuSClient.class, "getRequest", Matchers.anyString(), Matchers.anyObject() );
        mockControl.replay();
        String actual = transferManagerImpl.syncFile( httpSender );
        Assert.assertEquals( expectedMsg, actual );

    }

    /**
     * Hexlified file.
     *
     * @return the map
     */
    private Map< String, String > hexlifiedFile( String key ) {
        Map< String, String > map = new HashMap<>();
        String unHexfilePath = FileUtils.convertByteArrayToBase64String( SRC_FILE_TO_SYNC.getBytes() );
        map.put( key, unHexfilePath );
        return map;
    }

    /**
     * File check sum.
     *
     * @return the map
     */
    private Map< String, String > fileCheckSum() {
        Map< String, String > expectedMap = new HashMap<>();
        expectedMap.put( CHECKSUM, "86407176" );
        return expectedMap;
    }

}