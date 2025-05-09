package de.soco.software.simuspace.suscore.document.manager.impl.test;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.SuSVaultUtils;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;
import de.soco.software.simuspace.suscore.document.manager.impl.DocumentManagerImpl;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * @author Ahmar Nadeem
 *
 * A JUnit test class to test the functionality of Document manager
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { ConfigFilePathReader.class, de.soco.software.simuspace.suscore.common.util.FileUtils.class, PropertiesManager.class,
        SuSVaultUtils.class } )
public class DocumentManagerImplTest {

    /**
     * The Constant DUMMY_SHA_CHECK_SUM.
     */
    private static final String DUMMY_SHA_CHECK_SUM = "12345";

    /**
     * The Constant DUMMY_FILE_ENCODING.
     */
    private static final String DUMMY_FILE_ENCODING = "abcd";

    /**
     * The Constant VAULT_PATH.
     */
    private static final String VAULT_PATH = "asdf";

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * a mocked document id
     */
    private static final VersionPrimaryKey DOCUMENT_ID = new VersionPrimaryKey( UUID.randomUUID(), 1 );

    /**
     * The Constant DOC_ID.
     */
    private static final UUID DOC_ID = UUID.randomUUID();

    /**
     * a mocked user id
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * a mocked file name
     */
    private static final String FILE_NAME = "fileuploadtest.png";

    private static final String BROWSER_DETAILS = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";

    /**
     * temp file name for testing
     */
    private static final String USER_TEST_PNG_FILENAME = "user.png";

    /**
     * The Constant vaultPath.
     */
    private static final String FILE_PATH = ConstantsString.TEST_RESOURCE_PATH + USER_TEST_PNG_FILENAME;

    /**
     * The Constant TEMP_PATH.
     */
    private static final String TEMP_PATH = "src/test/resources/temp";

    /**
     * The Constant TEMP_PATH.
     */
    private static final String DOC_NAME = "Test Document";

    /**
     * Reference to the document manager
     */
    private static DocumentManagerImpl manager;

    /**
     * The document DAO object.
     */
    private static DocumentDAO documentDAO;

    /**
     * The Constant STATIC_METHOD_GET_VALUE_BY_NAME_FROM_PROPERTIES_FILE_IN_KARAF.
     */
    private static final String STATIC_METHOD_GET_VALUE_BY_NAME_FROM_PROPERTIES_FILE_IN_KARAF = "getValueByNameFromPropertiesFileInKaraf";

    /**
     * Dummy size for Tests
     */
    private static final long DOCUMENT_SIZE = 4656L;

    /**
     * Setup things before any test run in the class.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        documentDAO = mockControl.createMock( DocumentDAO.class );
        manager = new DocumentManagerImpl();
        manager.setDocumentDAO( documentDAO );
    }

    /**
     * Reset the things before every test starts
     */
    @Before
    public void setUp() {
        mockControl.resetToNice();
    }

    /********************
     * Get Document list
     ********************/

    /**
     * test if the document list is return if valid filter provided
     *
     * @throws Exception
     */
    @Test
    public void shouldGetDocumentListWhenValidFiltersProvided() throws Exception {
        FiltersDTO filter = createFilters( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_ZERO,
                ConstantsInteger.INTEGER_VALUE_TWO, 10L );
        List< DocumentEntity > expectedEntities = new ArrayList<>();
        mockForFilteredGet( expectedEntities, filter );
        FilteredResponse< DocumentDTO > actualDocsList = manager.getDocumentList( filter );
        assertFilteredGetResponse( expectedEntities, actualDocsList );
    }

    /*******************************
     * Get document list by User ID
     ******************************/

    /**
     * test if document is returned by user id when valid filters provided
     *
     * @throws Exception
     */
    @Test
    public void shouldGetDocumentListByUserIdWhenValidFiltersProvided() throws Exception {
        FiltersDTO filter = createFilters( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_ZERO,
                ConstantsInteger.INTEGER_VALUE_TWO, 10L );
        List< DocumentEntity > expectedEntities = new ArrayList<>();
        mockForFilteredGet( expectedEntities, filter );
        FilteredResponse< DocumentDTO > actualDocsList = manager.getDocumentListByUserId( USER_ID, filter );
        assertFilteredGetResponse( expectedEntities, actualDocsList );
    }

    /**********************
     * Get document by ID
     *********************/

    /**
     * test if system throws exception when document does not exist
     */
    @Test
    public void shouldThrowExceptionAndDoNotGetDocumentByIdWhenDocumentIdDoesNotExist() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.DOCUMENT_DOES_NOT_EXIST.getKey(), DOCUMENT_ID.getId(),
                DOCUMENT_ID.getVersionId() ) );
        expect( documentDAO.findById( EasyMock.anyObject( EntityManager.class ), DOCUMENT_ID ) ).andReturn( null ).anyTimes();
        mockControl.replay();
        manager.getDocumentById( DOCUMENT_ID.getId() );
    }

    /**
     * test if document is returned if valid document id is provided
     *
     * @throws Exception
     */
    @Test
    public void shouldSuccessfullyGetDocumentByIdWhenValidDocumentIdIsGiven() throws Exception {
        DocumentEntity expectedEntity = createdExpectedEntity( createExpectedDocument( FILE_NAME ) );
        expect( documentDAO.findById( EasyMock.anyObject( EntityManager.class ), DOCUMENT_ID ) ).andReturn( expectedEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockControl.replay();
        DocumentDTO actualDocument = manager.getDocumentById( DOCUMENT_ID.getId() );
        assertAllCommonExpectations( expectedEntity, actualDocument );
    }

    /**
     * Should successfully get document by name when document exist in data base.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessfullyGetDocumentByNameWhenDocumentExistInDataBase() throws Exception {
        DocumentEntity expectedEntity = createdExpectedEntity( createExpectedDocument( FILE_NAME ) );
        expect( documentDAO.findByName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( Class.class ),
                EasyMock.anyString() ) ).andReturn( expectedEntity ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockControl.replay();
        DocumentDTO actualDocument = manager.getDocumentByName( EasyMock.anyObject( EntityManager.class ), DOC_NAME );
        assertAllCommonExpectations( expectedEntity, actualDocument );
    }

    /**
     * Should throw exception and do not get document by name when document name does not exist.
     */
    @Test
    public void shouldThrowExceptionAndDoNotGetDocumentByNameWhenDocumentNameDoesNotExist() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.DOCUMENT_DOES_NOT_EXIST_WITH_NAME.getKey(), DOC_NAME ) );
        expect( documentDAO.findByName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( Class.class ),
                EasyMock.anyString() ) ).andReturn( null ).anyTimes();
        mockControl.replay();
        manager.getDocumentByName( EasyMock.anyObject( EntityManager.class ), DOC_NAME );
    }

    /*******************
     * Delete document
     *******************/

    /**
     * Should successfully delete document when valid document id is given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessfullyDeleteDocumentWhenValidDocumentIdIsGiven() {
        expect( documentDAO.deleteDocument( EasyMock.anyObject( EntityManager.class ), anyObject() ) ).andReturn( true ).anyTimes();
        mockControl.replay();
        boolean isDeleted = manager.deleteDocument( DOCUMENT_ID.getId() );
        assertTrue( isDeleted );
    }

    /**
     * Should throw exception when document id is invalid.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldThrowExceptionWhenDocumentIdIsInvalid() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.DOCUMENT_DOES_NOT_EXIST.getKey(), DOCUMENT_ID.getId(),
                DOCUMENT_ID.getVersionId() ) );
        expect( documentDAO.deleteDocument( EasyMock.anyObject( EntityManager.class ), anyObject() ) ).andReturn( false ).anyTimes();
        mockControl.replay();
        boolean isDeleted = manager.deleteDocument( DOCUMENT_ID.getId() );
        assertFalse( isDeleted );
    }

    /*******************
     * Write file to disk
     *******************/

    /**
     * Should successfully write file in vault.
     *
     * @throws IOException
     */
    @Test
    public void shouldSuccessfullyWriteFileInVault() throws IOException {
        DocumentDTO expectedDocument = createExpectedDocument( FILE_NAME );
        File expected = new File( FILE_PATH );
        InputStream stream = new FileInputStream( new File( FILE_PATH ) );
        PowerMockito.mockStatic( SuSVaultUtils.class );
        Mockito.when( SuSVaultUtils.saveInVault( Mockito.any( InputStream.class ), Matchers.anyString(), Matchers.anyString(),
                Matchers.anyString() ) ).thenReturn( new File( FILE_PATH ) );

        // expect( vaultManager.saveInVault( anyObject(), anyObject(), anyObject(), anyObject() ) ).andReturn( new File( FILE_PATH ) );
        // mockControl.replay();
        SuSVaultUtils.saveInVault( stream, "", "", "" );
        File actual = manager.writeToDiskInVault( BROWSER_DETAILS, expectedDocument, stream );

        assertTrue( FileUtils.contentEquals( expected, actual ) );
    }

    /*******************
     * Read file from disk
     *******************/

    /**
     * Should successfully read file in vault.
     *
     * @throws IOException
     */
    @Test
    public void shouldSuccessfullyReadFileInVault() throws IOException {
        DocumentDTO documentDto = createExpectedDocument( USER_TEST_PNG_FILENAME );
        File expected = new File( FILE_PATH );
        System.out.println( expected );

        PowerMockito.mockStatic( SuSVaultUtils.class );

        /*     Mockito.when( SuSVaultUtils.getFileFromPath( Matchers.any( UUID.class ), Matchers.anyInt(),  Matchers.anyString() ) )
                .thenReturn( expected );
        
        File actual = manager.readVaultFromDisk( documentDto );
        
        assertEquals( DOCUMENT_SIZE, actual.length() );
        assertTrue( FileUtils.contentEquals( expected, actual ) );*/
    }

    /****************************
     * write to disk in temp path
     ****************************/

    /**
     * Should throw exception when null entity return from DAO.
     */
    @Test
    public void shouldThrowExceptionWhenNullEntityReturnFromDAO() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.DOCUMENT_DOES_NOT_EXIST.getKey(), DOC_ID.toString(),
                DOCUMENT_ID.getVersionId() ) );
        DocumentDTO documentDTO = createExpectedDocument( FILE_NAME );
        expect( documentDAO.findById( EasyMock.anyObject( EntityManager.class ), anyObject( VersionPrimaryKey.class ) ) ).andReturn( null )
                .anyTimes();
        mockControl.replay();
        manager.writeToDiskInFETemp( EasyMock.anyObject( EntityManager.class ), documentDTO, TEMP_PATH );
    }

    /**
     * Should throw exception when vault is empty.
     *
     * @throws IOException
     */
    @Test
    public void shouldThrowExceptionWhenVaultIsEmpty() throws IOException {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ) );
        DocumentDTO documentDTO = createExpectedDocument( FILE_NAME );
        DocumentEntity expectedEntity = createdExpectedEntity( documentDTO );
        expect( documentDAO.findById( EasyMock.anyObject( EntityManager.class ), anyObject( VersionPrimaryKey.class ) ) )
                .andReturn( expectedEntity ).anyTimes();
        mockControl.replay();
        try {
            manager.writeToDiskInFETemp( EasyMock.anyObject( EntityManager.class ), documentDTO, TEMP_PATH );
        } finally {
            FileUtils.deleteDirectory( new File( TEMP_PATH ) );
        }

    }

    /**
     * helper function to create a mocked document object
     *
     * @return
     */
    private DocumentDTO createExpectedDocument( String fileName ) {
        DocumentDTO expectedDocument = new DocumentDTO();
        expectedDocument.setId( DOC_ID.toString() );
        expectedDocument.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
        expectedDocument.setName( fileName );
        expectedDocument.setType( "image" );
        expectedDocument.setSize( DOCUMENT_SIZE );
        expectedDocument.setPath( FILE_PATH );
        return expectedDocument;
    }

    /**
     * helper function to mock the document entity object
     *
     * @param doc
     *
     * @return
     */
    private DocumentEntity createdExpectedEntity( DocumentDTO doc ) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId( UUID.fromString( doc.getId() ) );
        entity.setExpiry( doc.getExpiry() );
        entity.setFileName( doc.getName() );
        entity.setFileType( doc.getType() );
        entity.getEncryptionDecryption().setMethod( doc.getEncryptionDecryption().getMethod() );
        entity.setEncrypted( doc.isEncrypted() );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( USER_ID );
        entity.setOwner( userEntity );
        return entity;
    }

    /**
     * helper function to mock filters for get
     *
     * @param expectedEntities
     * @param filter
     *
     * @throws Exception
     */
    private void mockForFilteredGet( List< DocumentEntity > expectedEntities, FiltersDTO filter ) throws Exception {
        DocumentEntity document = new DocumentEntity();
        document.setId( DOCUMENT_ID.getId() );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( USER_ID );
        document.setOwner( userEntity );
        document.setFileName( FILE_NAME );
        expectedEntities.add( document );
        expect( documentDAO.getAllFilteredRecords( EasyMock.anyObject( EntityManager.class ), DocumentEntity.class, filter ) )
                .andReturn( expectedEntities ).anyTimes();
        expect( documentDAO.getFilteredListByUser( EasyMock.anyObject( EntityManager.class ), USER_ID, filter ) )
                .andReturn( expectedEntities ).anyTimes();
        mockStaticGetValueByNameFromPropertiesFileInKaraf();
        mockControl.replay();
    }

    /**
     * helper function to assert filtered responses
     *
     * @param expectedEntities
     * @param actualDocsList
     */
    private void assertFilteredGetResponse( List< DocumentEntity > expectedEntities, FilteredResponse< DocumentDTO > actualDocsList ) {
        assertNotNull( actualDocsList );
        assertEquals( ConstantsInteger.INTEGER_VALUE_ONE, actualDocsList.getData().size() );
        assertEquals( expectedEntities.size(), actualDocsList.getData().size() );
        DocumentEntity expectedDocument = expectedEntities.get( ConstantsInteger.INTEGER_VALUE_ZERO );
        DocumentDTO actualDocument = actualDocsList.getData().get( ConstantsInteger.INTEGER_VALUE_ZERO );
        assertEquals( expectedDocument.getId(), UUID.fromString( actualDocument.getId() ) );
        assertEquals( expectedDocument.getOwner().getId(), actualDocument.getUserId() );
        assertEquals( expectedDocument.getFileName(), actualDocument.getName() );
    }

    /**
     * helper function to asser common expectations
     *
     * @param expectedEntity
     * @param actualDocument
     */
    private void assertAllCommonExpectations( DocumentEntity expectedEntity, DocumentDTO actualDocument ) {
        assertNotNull( actualDocument );
        assertEquals( expectedEntity.getId(), UUID.fromString( actualDocument.getId() ) );
        assertEquals( expectedEntity.getExpiry(), actualDocument.getExpiry() );
        assertEquals( expectedEntity.getFileName(), actualDocument.getName() );
    }

    /**
     * helper function to instantiate a filters dto object using the provided parameters
     *
     * @param draw
     * @param start
     * @param lenght
     *
     * @return
     */
    private FiltersDTO createFilters( int draw, int start, int lenght, Long filteredRecords ) {
        return new FiltersDTO( draw, start, lenght, filteredRecords );
    }

    /**
     * Mock static get value by name from properties file in karaf.
     *
     * @throws Exception
     *         the exception
     */
    private void mockStaticGetValueByNameFromPropertiesFileInKaraf() throws Exception {
        PowerMockito.spy( ConfigFilePathReader.class );
        PowerMockito.doReturn( ConstantsString.TEST_RESOURCE_PATH ).when( ConfigFilePathReader.class,
                STATIC_METHOD_GET_VALUE_BY_NAME_FROM_PROPERTIES_FILE_IN_KARAF, Matchers.anyString(), Matchers.anyString() );

    }

}
