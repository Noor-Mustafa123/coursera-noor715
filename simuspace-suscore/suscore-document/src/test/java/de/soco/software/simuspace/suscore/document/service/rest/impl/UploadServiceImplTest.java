package de.soco.software.simuspace.suscore.document.service.rest.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMessageHeaders;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.HTTPResponseHeaderEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.JobTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.interceptors.dao.JobTokenDAO;
import de.soco.software.simuspace.suscore.interceptors.dao.UserTokenDAO;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * A Junit class for testing different scenario's relating to the file upload service.
 *
 * @author Ahmar Nadeem
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { ConfigFilePathReader.class, ServletFileUpload.class } )
public class UploadServiceImplTest {

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
     * The Constant X_AUTH_TOKEN.
     */
    private static final String TOKEN = "42dea21da7af3f772fd340bb204d35dffe2b3245";

    /**
     * a mocked user uuid
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * a mocked file name
     */
    private static final String FILE_NAME = "fileuploadtest.png";

    /**
     * Max File upload size for testing
     */
    private static final String ONE_MB = "1024";

    /**
     * Reference to the document manager
     */
    private static UploadServiceImpl uploadService;

    /**
     * The document DAO object.
     */
    private static DocumentManager documentManager;

    /**
     * The user token dao.
     */
    private static UserTokenDAO userTokenDAO;

    private JobTokenDAO jobTokenDAO;

    private UserCommonDAO userCommonDAO;

    /**
     * a mocked http request object
     */
    private static HttpServletRequest request;

    /**
     * a mocked http response object
     */
    private static HttpServletResponse response;

    /**
     * The servlet file upload.
     */
    @Mock
    private static ServletFileUpload servletFileUpload;

    /**
     * The Constant STATIC_METHOD_GET_VALUE_BY_NAME_FROM_PROPERTIES_FILE_IN_KARAF.
     */
    private static final String STATIC_METHOD_GET_VALUE_BY_NAME_FROM_PROPERTIES_FILE_IN_KARAF = "getValueByNameFromPropertiesFileInKaraf";

    /**
     * Setup things before any test run in the class.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        documentManager = mockControl.createMock( DocumentManager.class );
        userTokenDAO = mockControl.createMock( UserTokenDAO.class );
        uploadService = new UploadServiceImpl();
        uploadService.setDocumentManager( documentManager );
        uploadService.setUserTokenDAO( userTokenDAO );
    }

    /**
     * Reset the things before every test starts
     */
    @Before
    public void setUp() {
        mockControl.resetToNice();
        request = mockControl.createMock( HttpServletRequest.class );
        jobTokenDAO = mockControl.createMock( JobTokenDAO.class );
        userCommonDAO = mockControl.createMock( UserCommonDAO.class );
        response = mockControl.createMock( HttpServletResponse.class );
        uploadService.setJobTokenDAO( jobTokenDAO );

    }

    /**
     * test if create document working with valid parameters
     *
     * @throws IOException
     */
    @Test
    public void shouldCreateDocumentWhenValidParametersProvided() throws IOException {
        mockControl.replay();
        uploadService.doOptions( request, response );
        assertNotNull( response );
        boolean expectedValue = false;
        assertEquals( expectedValue, response.containsHeader( HTTPResponseHeaderEnum.SERVER.getKey() ) );
        assertEquals( expectedValue, response.containsHeader( HTTPResponseHeaderEnum.ALLOW.getKey() ) );

    }

    /**
     * Should throw exception when no token provided.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldThrowExceptionWhenNoTokenProvided() throws IOException {

        expect( request.getHeader( ConstantsMessageHeaders.X_AUTH_TOKEN ) ).andReturn( null );
        expect( request.getHeader( ConstantsMessageHeaders.X_JOB_TOKEN ) ).andReturn( null );
        JobTokenEntity jobTokenEntity = new JobTokenEntity();
        UserTokenEntity userTokenEntity = new UserTokenEntity();

        expect( userTokenDAO.getUserTokenEntityByActiveToken( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( userTokenEntity );
        expect( jobTokenDAO.getJobTokenEntityByJobToken( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( jobTokenEntity );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( USER_ID );
        expect( userCommonDAO.readUser( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity );
        mockControl.replay();

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.NO_TOKEN_PROVIDED.getKey() ) );
        uploadService.doPost( request, response );
    }

    /**
     * Should throw exception when token is expired.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Test
    public void shouldThrowExceptionWhenTokenIsExpired() throws IOException {
        UserTokenEntity userTokenEntity = createUserTokenEntity();
        userTokenEntity.setExpired( true );
        expect( request.getHeader( ConstantsMessageHeaders.X_AUTH_TOKEN ) ).andReturn( TOKEN );
        expect( userTokenDAO.getUserTokenEntityByActiveToken( EasyMock.anyObject( EntityManager.class ), anyObject() ) )
                .andReturn( userTokenEntity );
        mockControl.replay();

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.INVALID_OR_EXPIRED_TOKEN.getKey() ) );
        uploadService.doPost( request, response );
    }

    /**
     * Should log exception when throw from servlet.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldLogExceptionWhenThrowFromServlet() throws Exception {
        UserTokenEntity userTokenEntity = createUserTokenEntity();

        expect( response.getWriter() ).andReturn( new PrintWriter( new StringWriter() ) ).anyTimes();

        mockStaticMultipartContentFromServlet( request );

        expect( request.getHeader( ConstantsMessageHeaders.X_AUTH_TOKEN ) ).andReturn( TOKEN );
        expect( userTokenDAO.getUserTokenEntityByActiveToken( EasyMock.anyObject( EntityManager.class ), anyObject() ) )
                .andReturn( userTokenEntity );
        mockControl.replay();

        uploadService.doPost( request, response );
    }

    /**
     * Creates the user token entity.
     *
     * @return the user token entity
     */
    private UserTokenEntity createUserTokenEntity() {
        UserTokenEntity userTokenEntity = new UserTokenEntity();
        userTokenEntity.setExpired( false );
        userTokenEntity.setUserEntity( createUserEntity() );
        return userTokenEntity;

    }

    /**
     * Creates the user entity.
     *
     * @return the user entity
     */
    private UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId( USER_ID );
        return userEntity;
    }

    /**
     * test if mime type is validated if type is image
     *
     * @throws Exception
     */
    @Test
    public void shouldSuccessfullyValidateMimeTypeIfTypeIsImage() throws Exception {
        File filePath = new File( ConstantsString.TEST_RESOURCE_PATH + FILE_NAME );
        String origin = DocumentDTO.BROWSER;

        mockStaticGetValueByNameFromPropertiesFileInKarafAndReturnOneMB();
        mockControl.replay();
        DocumentDTO expectedDocument = createDocument( filePath.getAbsolutePath(), origin, USER_ID );
        DocumentDTO actualDocument = uploadService.validateMimeTypeAndPrepareDocument( new DocumentDTO(), filePath,
                origin, USER_ID );
        assertCommonExpectations( expectedDocument, actualDocument );
    }

    /**
     * test if the srevice throws excpetion when invalid origin is provided
     *
     * @throws Exception
     */
    @Test
    public void shouldThrowExceptionWhenInvalidOriginProvided() throws Exception {
        File filePath = new File( ConstantsString.TEST_RESOURCE_PATH + FILE_NAME );
        String origin = DocumentDTO.BROWSER + ConstantsInteger.INTEGER_VALUE_ONE;

        mockStaticGetValueByNameFromPropertiesFileInKarafAndReturnOneMB();
        mockControl.replay();

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.DOCUMENT_NOT_REGISTERED_AGENT.getKey(), origin ) );
        uploadService.validateMimeTypeAndPrepareDocument( new DocumentDTO(), filePath, origin, USER_ID );
    }

    /**
     * test if file name is encoded according to the windows user agent
     */
    @Test
    public void shouldEncodeFileNameAccordingToWindowsWhenWindowsUserAgent() {
        expect( request.getHeader( ConstantsString.USER_AGENT_HEADER ) ).andReturn( ConstantsString.WINDOWS_PREFIX );
        mockControl.replay();
        String actualName = CommonUtils.getEncodedName( FILE_NAME, ConstantsString.WINDOWS_PREFIX );
        assertEquals( FILE_NAME, actualName );
    }

    /**
     * test if file name is encoded to UTF8 for other than windows user agent
     */
    @Test
    public void shouldEncodeFileNameToUTF8WhenOtherThanWindowsUserAgent() {
        expect( request.getHeader( ConstantsString.USER_AGENT_HEADER ) ).andReturn( ConstantsString.OS_LINUX );
        mockControl.replay();
        String actualName = CommonUtils.getEncodedName( FILE_NAME, ConstantsString.WINDOWS_PREFIX );
        assertEquals( FILE_NAME, actualName );
    }

    /**
     * helper function to prepare document object for mocking
     *
     * @param fileName
     *
     * @return
     */
    private DocumentDTO createDocument( String fileName, String origin, UUID userId ) {
        DocumentDTO doc = new DocumentDTO();
        doc.setName( fileName );
        doc.setAgent( origin );
        doc.setUserId( userId );
        doc.setId( UUID.randomUUID().toString() );
        doc.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );

        return doc;
    }

    /**
     * A helper function to assert some common expectations
     *
     * @param expectedDocument
     * @param actualDocument
     */
    private void assertCommonExpectations( DocumentDTO expectedDocument, DocumentDTO actualDocument ) {
        Assert.assertEquals( expectedDocument.getUserId(), actualDocument.getUserId() );
        Assert.assertEquals( expectedDocument.getAgent(), actualDocument.getAgent() );
        Assert.assertEquals( expectedDocument.getExpiry(), actualDocument.getExpiry() );
    }

    /**
     * Mock static get value by name from properties file in karaf.
     *
     * @throws Exception
     *         the exception
     */
    private void mockStaticGetValueByNameFromPropertiesFileInKarafAndReturnOneMB() throws Exception {
        PowerMockito.spy( ConfigFilePathReader.class );
        PowerMockito.doReturn( ONE_MB ).when( ConfigFilePathReader.class,
                STATIC_METHOD_GET_VALUE_BY_NAME_FROM_PROPERTIES_FILE_IN_KARAF, Matchers.anyString(),
                Matchers.anyString() );
    }

    /**
     * Mock static get value by name from properties file in karaf.
     *
     * @param request
     *
     * @throws Exception
     *         the exception
     */
    private void mockStaticMultipartContentFromServlet( HttpServletRequest request ) throws Exception {

        mockStatic( ServletFileUpload.class );
        expect( ServletFileUpload.isMultipartContent( request ) ).andReturn( true ).anyTimes();
        replayAll();
    }

}
