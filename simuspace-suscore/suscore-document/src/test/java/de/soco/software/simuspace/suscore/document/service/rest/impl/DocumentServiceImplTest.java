package de.soco.software.simuspace.suscore.document.service.rest.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;

/**
 * @author Ahmar Nadeem
 *
 * A junit test class for the document service.
 */
public class DocumentServiceImplTest {

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * user filter json for fetching paginated list
     */
    private String filterRequestJSON = "{\"draw\":0,\"start\":0,\"length\":10,\"search\":null, \"filteredRecords\":10}";

    /**
     * version id used in various tests
     */
    private static int version = 1;

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * temp file name for testing
     */
    private static final String USER_TEST_PNG_FILENAME = "user.png";

    /**
     * The Constant vaultPath.
     */
    private static final String FILE_PATH = ConstantsString.TEST_RESOURCE_PATH + USER_TEST_PNG_FILENAME;

    /**
     * a mocked document id
     */
    private static final VersionPrimaryKey DOCUMENT_ID = new VersionPrimaryKey( UUID.randomUUID(), 1 );

    /**
     * user id to be used in tests
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant DOC_ID.
     */
    private static final UUID DOC_ID = UUID.randomUUID();

    /**
     * expected response data in get request
     */
    private static final String EXPECTED_GET_BY_ID_RESPONSE_DATA = "{name=null, type=null, isTemp=null, properties=null, id=" + DOC_ID
            + ", version={id=" + DOCUMENT_ID.getVersionId() + "}, userId=" + USER_ID
            + ", size=0, expiry=0, createdOn=null, agent=null, path=null, encoding=null, hash=null, url=null, stream=null}";

    /**
     * expected message in the success response
     */
    private static final String EXPECTED_RESPONSE_MESSAGE = null;

    /**
     * Reference to the document manager
     */
    private static DocumentServiceImpl service;

    /**
     * The object Manager Refernence.
     */
    private static DocumentManager manager;

    /**
     * Setup things before any test run in the class.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        service = new DocumentServiceImpl();
        manager = mockControl.createMock( DocumentManager.class );
        service.setDocumentManager( manager );
    }

    /**
     * Reset the things before every test starts
     */
    @Before
    public void setUp() {
        mockControl.resetToNice();
    }

    /**
     * should Get Bad Request Response When document id Is Null
     *
     * @throws JsonSerializationException
     */
    @Test
    public void shouldGetBadRequestResponseWhenNullDocumentIdProvidedToGetDocument() {

        final Response response = service.getDocumentById( null, version );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * should Get Bad Request Response When filter is Null
     *
     * @throws JsonSerializationException
     */
    @Test
    public void shouldGetBadRequestResponseWhenNullFilterJSONProvidedToGetAllDocuments() {

        final Response response = service.getDocumentList( null );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * should Get Bad Request Response When filter Is Null
     *
     * @throws JsonSerializationException
     */
    @Test
    public void shouldGetBadRequestResponseWhenNullFilterJSONProvidedToGetAllDocumentsByUser() {

        final Response response = service.getDocumentListByUserId( USER_ID, null );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * should Get Bad Request Response When user id Is Null
     *
     * @throws JsonSerializationException
     */
    @Test
    public void shouldGetBadRequestResponseWhenNullUserIdProvidedToGetAllDocumentsByUser() {

        final Response response = service.getDocumentListByUserId( null, filterRequestJSON );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * should Get Bad Request Response When document id Is Null
     *
     * @throws JsonSerializationException
     */
    @Test
    public void shouldGetBadRequestResponseWhenNullDocumentIdProvidedToDeleteDocument() {
        final Response response = service.deleteDocument( null, version );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * should Successfully Get Document By Id When Valid DocumentId And Version Provided
     */
    @Test
    public void shouldSuccessfullyGetDocumentByIdWhenValidDocumentIdAndVersionProvided() {
        DocumentDTO expectedDocument = buildExpectedDocument();
        expect( manager.getDocumentById( anyObject() ) ).andReturn( expectedDocument );
        mockControl.replay();
        final Response expected = service.getDocumentById( DOC_ID, DOCUMENT_ID.getVersionId() );
        assertCommonExpectations( expected, EXPECTED_GET_BY_ID_RESPONSE_DATA );

    }

    /**
     * should return status as false When manager unable to get the document by id
     */
    @Test
    public void shouldReturnFalseStatusWhenManagerFailedToGetDocumentById() {
        expect( manager.getDocumentById( anyObject() ) ).andThrow( new SusException() );
        mockControl.replay();
        final Response expected = service.getDocumentById( DOCUMENT_ID.getId(), DOCUMENT_ID.getVersionId() );
        SusResponseDTO actualResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        Assert.assertFalse( actualResponseDTO.getSuccess() );

    }

    /**
     * should return status as false When manager unable to delete the document
     */
    @Test
    public void shouldReturnFalseStatusWhenManagerFailedToDeleteDocumentById() {

        expect( manager.deleteDocument( anyObject() ) ).andThrow( new SusException() );
        mockControl.replay();
        final Response expected = service.deleteDocument( DOCUMENT_ID.getId(), DOCUMENT_ID.getVersionId() );
        SusResponseDTO actualResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        Assert.assertFalse( actualResponseDTO.getSuccess() );
    }

    /**
     * should Successfully Get Document List By UserId When Valid UserId And Filter JSON Provided
     */
    @Test
    public void shouldSuccessfullyGetDocumentListByUserIdWhenValidUserIdAndFilterJSONProvided() {
        DocumentDTO expectedDocument = buildExpectedDocument();
        mockPaginatedResponse();
        final Response expected = service.getDocumentListByUserId( USER_ID, filterRequestJSON );
        SusResponseDTO actual = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        Assert.assertNotNull( actual.getData() );

        String filterResponse = JsonUtils.convertMapToString( ( Map< String, String > ) actual.getData() );
        FilteredResponse< DocumentDTO > filteredResponse = JsonUtils.jsonToObject( filterResponse, FilteredResponse.class );
        List< DocumentDTO > actual1 = JsonUtils.jsonToList( JsonUtils.toJson( filteredResponse.getData() ), DocumentDTO.class );

        checkEqualAsserts( expectedDocument, actual1 );

    }

    /**
     * should Successfully Get Document List When Valid Filter JSON Provided
     */
    @Test
    public void shouldSuccessfullyGetDocumentListWhenValidFilterJSONProvided() {
        DocumentDTO expectedDocument = buildExpectedDocument();
        mockPaginatedResponse();
        final Response expected = service.getDocumentList( filterRequestJSON );
        SusResponseDTO actual = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        Assert.assertNotNull( actual.getData() );

        String filterResponse = JsonUtils.convertMapToString( ( Map< String, String > ) actual.getData() );
        FilteredResponse< DocumentDTO > filteredResponse = JsonUtils.jsonToObject( filterResponse, FilteredResponse.class );
        List< DocumentDTO > actual1 = JsonUtils.jsonToList( JsonUtils.toJson( filteredResponse.getData() ), DocumentDTO.class );

        checkEqualAsserts( expectedDocument, actual1 );
    }

    /**
     * should successfully delete document when valid document parameters provided
     */
    @Test
    public void shouldSuccessfullyDeleteDocumentWhenValidDocumentParametersProvided() {
        expect( manager.deleteDocument( anyObject() ) ).andReturn( true );
        mockControl.replay();
        final Response expected = service.deleteDocument( DOCUMENT_ID.getId(), DOCUMENT_ID.getVersionId() );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
    }

    /**
     * helper function to mock paginated response for most of the tests
     */
    private void mockPaginatedResponse() {
        FiltersDTO filter = new FiltersDTO();
        filter.setDraw( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setLength( ConstantsInteger.INTEGER_VALUE_TWO );
        filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        filter.setFilteredRecords( 10L );
        DocumentDTO doc = buildExpectedDocument();
        List< DocumentDTO > data = new ArrayList<>();
        data.add( doc );
        FilteredResponse< DocumentDTO > expectedResponse = PaginationUtil.constructFilteredResponse( filter, data );
        expect( manager.getDocumentListByUserId( anyObject(), anyObject() ) ).andReturn( expectedResponse );
        expect( manager.getDocumentList( anyObject() ) ).andReturn( expectedResponse );
        mockControl.replay();
    }

    /**
     * helper function to build expected document object
     *
     * @return
     */
    private DocumentDTO buildExpectedDocument() {
        DocumentDTO document = new DocumentDTO();
        document.setUserId( USER_ID );
        document.setId( DOC_ID.toString() );
        document.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
        return document;
    }

    /**
     * helper function to assert the response for most of the tests
     *
     * @param expected
     * @param expectedData
     */
    private void assertCommonExpectations( Response expected, String expectedData ) {
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO actualResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertTrue( actualResponseDTO.getSuccess() );
        Assert.assertNotNull( actualResponseDTO.getData() );
        Assert.assertEquals( expectedData, actualResponseDTO.getData().toString() );
        Assert.assertEquals( EXPECTED_RESPONSE_MESSAGE, actualResponseDTO.getMessage() );
    }

    /**
     * Check equals for expected and actual.
     *
     * @param expectedDocument
     *         the expected document
     * @param actual
     *         the actual
     */
    private void checkEqualAsserts( DocumentDTO expectedDocument, List< DocumentDTO > actual ) {
        Assert.assertEquals( expectedDocument.getId(), actual.get( FIRST_INDEX ).getId() );
        Assert.assertEquals( expectedDocument.getVersion().getId(), actual.get( FIRST_INDEX ).getVersion().getId() );
        Assert.assertEquals( expectedDocument.getName(), actual.get( FIRST_INDEX ).getName() );
        Assert.assertEquals( expectedDocument.getPath(), actual.get( FIRST_INDEX ).getPath() );
        Assert.assertEquals( expectedDocument.getAgent(), actual.get( FIRST_INDEX ).getAgent() );

    }

}
