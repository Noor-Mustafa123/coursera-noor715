package de.soco.software.simuspace.suscore.common.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;

/**
 * A class that is used to test the public methods of vaultManagerImpl
 *
 * @author Ali Haider
 */
public class SuSVaultUtilsTest {

    private static final int TEST_VERSION_ID = 1;

    private static final int TEST_FILE_NAME = 2;

    /**
     * The vault manager impl.
     */
    private SuSVaultUtils vaultManagerImpl;

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * a mocked user id
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant hex.
     */
    private static final String hex = CommonUtils.getHex( USER_ID, TEST_VERSION_ID );

    /**
     * The Constant vaultPath.
     */
    private static final String vaultPath = ConstantsString.TEST_RESOURCE_PATH;

    /**
     * The Constant fileName.
     */
    private static final String fileName = "user.png";

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    private static File filePathToDelete = new File( ConstantsString.EMPTY_STRING );

    /**
     * setUp which is called before entering in each test case.
     */
    @Before
    public void setUp() throws Exception {
        mockControl.resetToNice();
        vaultManagerImpl = new SuSVaultUtils();
    }

    @After
    public void removeFiles() throws IOException {
        FileUtils.deleteDirectory( filePathToDelete.getParentFile() );

    }

    /********************
     * Save In Vault
     ********************/

    /**
     * Should successfully save file in vault.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessfullySaveFileInVault() throws Exception {
        File initialFile = new File( vaultPath + fileName );
        InputStream stream = new FileInputStream( initialFile );
        filePathToDelete = SuSVaultUtils.saveInVault( stream, vaultPath, hex, hex.substring( TEST_FILE_NAME ) );
        assertTrue( FileUtils.contentEquals( initialFile, filePathToDelete ) );

    }

    /**
     * Should throw exception when file already exists.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldThrowExceptionWhenFileAlreadyExists() throws IOException {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.FILE_ALREADY_EXIST_ON_SERVER.getKey() ) );
        File initialFile = new File( vaultPath + fileName );
        InputStream stream = new FileInputStream( initialFile );
        filePathToDelete = SuSVaultUtils.saveInVault( stream, vaultPath, hex, hex.substring( TEST_FILE_NAME ) );

        SuSVaultUtils.saveInVault( stream, vaultPath, hex, hex.substring( TEST_FILE_NAME ) );

    }

    /********************
     * Get From Vault
     ********************/

    /**
     * Should return file when valid id is given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnFileWhenValidIdIsGiven() throws IOException {
        File initialFile = new File( vaultPath + fileName );
        InputStream stream = new FileInputStream( initialFile );
        SuSVaultUtils.saveInVault( stream, vaultPath, hex, hex.substring( TEST_FILE_NAME ) );
        /*   filePathToDelete = SuSVaultUtils.getFileFromPath( USER_ID, TEST_VERSION_ID, vaultPath );*/

        assertTrue( FileUtils.contentEquals( initialFile, filePathToDelete ) );
    }

    /**
     * Should throw exception when invalid id is given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldThrowExceptionWhenInvalidIdIsGivenForGetFile() throws IOException {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey() ) );
        File initialFile = new File( vaultPath + fileName );
        InputStream stream = new FileInputStream( initialFile );
        filePathToDelete = SuSVaultUtils.saveInVault( stream, vaultPath, hex, hex.substring( TEST_FILE_NAME ) );
        /*  SuSVaultUtils.getFileFromPath( UUID.randomUUID(), TEST_VERSION_ID, vaultPath );*/

    }

    /********************
     * Delete From Vault
     ********************/

    /**
     * Should successfully delete file when valid id is given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessfullyDeleteFileWhenValidIdIsGiven() throws Exception {
        File initialFile = new File( vaultPath + fileName );
        InputStream stream = new FileInputStream( initialFile );
        filePathToDelete = SuSVaultUtils.saveInVault( stream, vaultPath, hex, hex.substring( 2 ) );

        assertTrue( SuSVaultUtils.deleteFileFromVault( USER_ID, TEST_VERSION_ID, vaultPath ) );
    }

    /**
     * Should return false when vault path not found.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnFalseWhenVaultPathNotFound() throws Exception {
        File initialFile = new File( vaultPath + fileName );
        InputStream stream = new FileInputStream( initialFile );
        filePathToDelete = SuSVaultUtils.saveInVault( stream, vaultPath, hex, hex.substring( 2 ) );

        assertFalse( SuSVaultUtils.deleteFileFromVault( USER_ID, TEST_VERSION_ID, "" ) );
    }

}
