package de.soco.software.simuspace.suscore.interceptors;

import static org.mockito.Mockito.mock;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.interceptor.InterceptorChain;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.interceptors.dao.PropertyAccesser;
import de.soco.software.simuspace.suscore.interceptors.dao.UserTokenDAO;
import de.soco.software.simuspace.suscore.interceptors.manager.impl.UserTokenManagerImpl;

/**
 * This Class will test the message pass to the interceptors..
 */
@RunWith( MockitoJUnitRunner.class )
public class SusAuthenticationInterceptorTest {

    /**
     * constant USER_AGENT.
     */
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0";

    /**
     * The Constant NO_TOKEN_PROVIDED.
     */
    private static final String NO_TOKEN_PROVIDED = "No Token provided";

    /**
     * constant resource path.
     */
    private static final String RESOURCE_PATH = "/sus";

    /**
     * consant request method.
     */
    private static final String HTTP_REQUEST_TYPE_POST = "POST";

    /**
     * constant IP address.
     */
    private static final String IP_ADDRESS = "http://127.0.0.1:8080";

    /**
     * The Constant TEST_CONTENT.
     */
    private static final String TEST_MESSAGE_CONTENTS = "test";

    /**
     * user token manager impl instance.
     */
    private UserTokenManagerImpl userTokenManagerImpl;

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * user token dao instance.
     */
    private UserTokenDAO userTokenDAO;

    /**
     * The sus authentication interceptor.
     */
    private SusAuthenticationInterceptor susAuthenticationInterceptor;

    /**
     * user token entity instance.
     */
    private UserTokenEntity userTokenEntity;

    /**
     * constant random UUID.
     */
    private static final UUID RANDON_UUID = UUID.randomUUID();

    /**
     * constant user expiry time.
     */
    private static final int TOKEN_EXPIRY_TIME = 360;

    /**
     * The Constant INVALID_TOKEN.
     */
    private static final String INVALID_TOKEN = "Invalid/expired token, login again";

    /**
     * property accessor instance.
     */
    private PropertyAccesser propertyAccesser;

    /**
     * constant user token.
     */
    private static final String TOKEN = "59c78c9adb09549062962b9b0c30c8821f618bfd";

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * The runtime delegate.
     */
    @Mock
    private RuntimeDelegate runtimeDelegate;

    /**
     * The thrown.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {

        RuntimeDelegate.setInstance( runtimeDelegate );
        mock( ResponseBuilder.class );
        mock( Response.class );
        mockControl().resetToNice();

        susAuthenticationInterceptor = new SusAuthenticationInterceptor();
        userTokenManagerImpl = new UserTokenManagerImpl();
        userTokenDAO = mockControl.createMock( UserTokenDAO.class );
        propertyAccesser = mockControl.createMock( PropertyAccesser.class );
        userTokenManagerImpl.setUserTokenDAO( userTokenDAO );
        userTokenManagerImpl.setPropertyAccesser( propertyAccesser );
        susAuthenticationInterceptor.setUserTokenManager( userTokenManagerImpl );
    }

    /**
     * Test that checks the http request and throws exception if X_Auth_Token is not provided in headers.
     */
    @Test
    public void shouldThrowExceptionOnHttpRequestWhenXAuthTokenIsNotProvidedInHeaders() {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST, false );
        thrown.expect( Exception.class );
        thrown.expectMessage( NO_TOKEN_PROVIDED );
        message.setExchange( mock( Exchange.class ) );
        message.setInterceptorChain( mock( InterceptorChain.class ) );
        mockControl.replay();
        susAuthenticationInterceptor.handleMessage( message );
    }

    /**
     * Test that checks the http request and validate the request when X_Auth_Token is provided in headers.
     *
     * @throws IOException
     */
    @Test
    public void shouldSuccessfullyValidateRequestWhenXAuthTokenIsProvidedInHeaders() throws IOException {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST, true );
        message.setContent( InputStream.class, IOUtils.toInputStream( TEST_MESSAGE_CONTENTS, ConstantsString.UTF8 ) );
        fillUserTokenEntity();
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setUserName( userTokenEntity.getUserEntity().getUserUid() );
        userTokenDTO.setUserId( userTokenEntity.getUserEntity().getId().toString() );
        EasyMock.expect( propertyAccesser.getExpiryTimeOut() ).andReturn( TOKEN_EXPIRY_TIME );
        EasyMock.expect( userTokenDAO.getUserTokenEntityByActiveToken( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( String.class ) ) ).andReturn( userTokenEntity )
                .anyTimes();
        mockControl.replay();
        susAuthenticationInterceptor.handleMessage( message );
    }

    /**
     * Test that checks the http request and throws the exception if expired token is provided in the headers.
     */
    @Test
    public void shouldThrowExceptionOnHttpRequestWhenExpiredOrInvalidXAuthTokenIsrovidedInHeaders() {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST, true );
        fillUserTokenEntity();
        EasyMock.expect( userTokenDAO.getUserTokenEntityByActiveToken( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( String.class ) ) ).andReturn( null ).anyTimes();
        thrown.expect( Exception.class );
        thrown.expectMessage( INVALID_TOKEN );
        message.setExchange( mock( Exchange.class ) );
        message.setInterceptorChain( mock( InterceptorChain.class ) );
        mockControl.replay();
        susAuthenticationInterceptor.handleMessage( message );
    }

    /**
     * A method for preparing cxf message.
     *
     * @param baseAddress
     *         the base address
     * @param pathInfo
     *         the path info
     * @param method
     *         the method
     * @param token
     *         the token
     *
     * @return the message
     */
    private Message prepareMessage( String baseAddress, String pathInfo, String method, boolean token ) {

        Message message = new MessageImpl();
        message.put( "org.apache.cxf.http.case_insensitive_queries", false );
        message.put( "org.apache.cxf.endpoint.private", false );
        message.put( Message.REQUEST_URL, baseAddress + pathInfo );
        message.put( Message.HTTP_REQUEST_METHOD, method );
        Map< String, List< String > > headers = new HashMap<>();
        headers.put( "User-Agent", Collections.singletonList( USER_AGENT ) );
        headers.put( "HTTP_CLIENT_IP", Collections.singletonList( baseAddress ) );
        if ( token ) {
            headers.put( "X-Auth-Token", Collections.singletonList( TOKEN ) );
        }
        message.put( Message.PROTOCOL_HEADERS, headers );
        return message;
    }

    /**
     * A method for filling user token entity.
     *
     * @return the user token entity
     */
    private UserTokenEntity fillUserTokenEntity() {
        userTokenEntity = new UserTokenEntity();
        userTokenEntity.setId( RANDON_UUID );
        userTokenEntity.setToken( TOKEN );
        userTokenEntity.setIpAddress( IP_ADDRESS );
        userTokenEntity.setBrowserAgent( USER_AGENT );
        userTokenEntity.setExpired( false );
        userTokenEntity.setLastRequestTime( new Date() );
        userTokenEntity.setCreatedOn( new Date() );
        userTokenEntity.setExpiryTime( new Date() );
        de.soco.software.simuspace.suscore.data.entity.UserEntity userEntity =
                new de.soco.software.simuspace.suscore.data.entity.UserEntity();
        userEntity.setId( RANDON_UUID );
        userTokenEntity.setUserEntity( userEntity );
        return userTokenEntity;
    }

}