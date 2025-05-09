package de.soco.software.simuspace.suscore.interceptors.manager.impl;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.interceptors.dao.PropertyAccesser;
import de.soco.software.simuspace.suscore.interceptors.dao.UserTokenDAO;

/**
 * A class used to test the public methods of {@link UserTokenManagerImpl}.
 *
 * @author Zeeshan jamal
 */
public class UserTokenManagerImplTest {

    /**
     * user token manager impl instance.
     */
    private UserTokenManagerImpl userTokenManagerImpl;

    /**
     * user token dao instance.
     */
    private UserTokenDAO userTokenDAO;

    /**
     * constant USER_AGENT.
     */
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0";

    /**
     * user token entity instance.
     */
    private UserTokenEntity userTokenEntity;

    /**
     * user token entity instance.
     */
    private UserTokenDTO userTokenDTO;

    /**
     * property accessor instance.
     */
    private PropertyAccesser propertyAccesser;

    /**
     * HttpServletRequest instance.
     */
    private HttpServletRequest httpServletRequest;

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * constant random UUID.
     */
    private static final UUID RANDOM_UUID = UUID.randomUUID();

    /**
     * constant user expiry time.
     */
    private static final int TOKEN_EXPIRY_TIME = 360;

    /**
     * constant user token.
     */
    private static final String TOKEN = "59c78c9adb09549062962b9b0c30c8821f618bfd";

    /**
     * constant empty string.
     */
    private static final String EMPTY_STRING = "";

    /**
     * constant IP address.
     */
    private static final String IP_ADDRESS = "http://127.0.0.1:8080";

    /**
     * constant resource path.
     */
    private static final String RESOURCE_PATH = "/sus";

    /**
     * consant request method.
     */
    private static final String HTTP_REQUEST_TYPE_POST = "POST";

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        mockControl().resetToNice();
        userTokenManagerImpl = new UserTokenManagerImpl();
        userTokenDAO = mockControl.createMock( UserTokenDAO.class );
        propertyAccesser = mockControl.createMock( PropertyAccesser.class );
        httpServletRequest = mockControl.createMock( HttpServletRequest.class );
        userTokenManagerImpl.setUserTokenDAO( userTokenDAO );
        userTokenManagerImpl.setPropertyAccesser( propertyAccesser );
    }

    /**
     * Test to prepare user token.
     */
    @Test
    public void shouldSuccessfullyPrepareTokenForAuthentication() {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST );
        fillUserTokenEntity();
        EasyMock.expect(
                        userTokenDAO.saveUserToken( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UserTokenEntity.class ) ) )
                .andReturn( userTokenEntity );
        mockControl.replay();
        String actual = userTokenManagerImpl.prepareAndPersistToken( EasyMock.anyObject( EntityManager.class ), message,
                RANDOM_UUID.toString(), RANDOM_UUID.toString(),
                TOKEN_EXPIRY_TIME, EMPTY_STRING );
        Assert.assertEquals( userTokenEntity.getToken(), actual );
    }

    /**
     * Test to checks the ip address from request message.
     */
    @Test
    public void shoudSuccessfullyReturnIpAddressFromRequestMessageHeaders() {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST );
        String actualIpAddress = userTokenManagerImpl.getIpAddress( message );
        Assert.assertEquals( IP_ADDRESS, actualIpAddress );
    }

    /**
     * Test that checks the remote address when ip address is not provided.
     */
    @Test
    public void shoudSuccessfullyReturnRemoteAddressWhenIpAddressIsNullFromRequestMessageHeaders() {
        Message message = prepareMessage( null, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST );
        message.put( AbstractHTTPDestination.HTTP_REQUEST, httpServletRequest );
        EasyMock.expect( httpServletRequest.getRemoteAddr() ).andReturn( IP_ADDRESS );
        mockControl.replay();
        String actualIpAddress = userTokenManagerImpl.getIpAddress( message );
        Assert.assertEquals( IP_ADDRESS, actualIpAddress );
    }

    /**
     * Test that validates http request and successfully return the user token dto with active token when valid token is issued.
     */
    @Test
    public void shouldSuccessfullyValidateTheHTTPRequestAndReturnUserTokenDTOWhenValidTokenHasAlreadyBeenIssued() {
        fillUserTokenEntity();
        EasyMock.expect( propertyAccesser.getExpiryTimeOut() ).andReturn( TOKEN_EXPIRY_TIME );
        EasyMock.expect( userTokenDAO.getUserTokenEntityByActiveToken( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( String.class ) ) ).andReturn( userTokenEntity )
                .anyTimes();
        mockControl.replay();
        UserTokenDTO actualUserTokenDTO = userTokenManagerImpl.checkSusHttpRequest( EasyMock.anyObject( EntityManager.class ),
                userTokenEntity.getToken(), IP_ADDRESS, EMPTY_STRING );
        Assert.assertEquals( false, userTokenEntity.isExpired() );
        Assert.assertEquals( userTokenEntity.isExpired(), actualUserTokenDTO.isExpired() );
        Assert.assertEquals( userTokenEntity.getToken(), actualUserTokenDTO.getToken() );
        Assert.assertEquals( userTokenEntity.getBrowserAgent(), actualUserTokenDTO.getBrowserAgent() );
        Assert.assertEquals( userTokenEntity.getIpAddress(), actualUserTokenDTO.getIpAddress() );

    }

    /**
     * Test that validates http request and successfully return the user token dto with expired token when invalid token is issued.
     */
    @Test
    public void shouldNotSuccessfullyValidateTheHTTPRequestAndReturnUserTokenDTOWhenTokenIsExpired() {
        fillUserTokenEntity();
        EasyMock.expect( propertyAccesser.getExpiryTimeOut() ).andReturn( TOKEN_EXPIRY_TIME - TOKEN_EXPIRY_TIME );
        EasyMock.expect( userTokenDAO.getUserTokenEntityByActiveToken( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( String.class ) ) ).andReturn( userTokenEntity )
                .anyTimes();
        mockControl.replay();
        UserTokenDTO actualUserTokenDTO = userTokenManagerImpl.checkSusHttpRequest( EasyMock.anyObject( EntityManager.class ),
                userTokenEntity.getToken(), IP_ADDRESS, EMPTY_STRING );
        Assert.assertEquals( true, userTokenEntity.isExpired() );
        Assert.assertEquals( userTokenEntity.isExpired(), actualUserTokenDTO.isExpired() );
        Assert.assertEquals( userTokenEntity.getToken(), actualUserTokenDTO.getToken() );
        Assert.assertEquals( userTokenEntity.getBrowserAgent(), actualUserTokenDTO.getBrowserAgent() );
        Assert.assertEquals( userTokenEntity.getIpAddress(), actualUserTokenDTO.getIpAddress() );
    }

    /**
     * Test that does not expire the token if the token provided is invalid.
     */
    @Test
    public void shouldNotSuccessfullyExpireTokenWhenInValidTokenIsProvided() {
        fillUserTokenEntity();
        EasyMock.expect( userTokenDAO.getUserTokenEntityByActiveToken( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( String.class ) ) ).andReturn( null );
        mockControl.replay();
        boolean actualStatus = userTokenManagerImpl.expireToken( EasyMock.anyObject( EntityManager.class ), userTokenEntity.getToken() );
        Assert.assertEquals( false, actualStatus );
        Assert.assertEquals( actualStatus, userTokenEntity.isExpired() );
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
     *
     * @return the message
     */
    private Message prepareMessage( String baseAddress, String pathInfo, String method ) {

        Message message = new MessageImpl();
        message.put( "org.apache.cxf.http.case_insensitive_queries", false );
        message.put( "org.apache.cxf.endpoint.private", false );
        message.put( Message.REQUEST_URI, pathInfo );
        message.put( Message.HTTP_REQUEST_METHOD, method );
        Map< String, List< String > > headers = new HashMap<>();
        headers.put( "User-Agent", Collections.singletonList( USER_AGENT ) );
        headers.put( "HTTP_CLIENT_IP", Collections.singletonList( baseAddress ) );
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
        userTokenEntity.setId( RANDOM_UUID );
        userTokenEntity.setToken( TOKEN );
        userTokenEntity.setIpAddress( IP_ADDRESS );
        userTokenEntity.setBrowserAgent( USER_AGENT );
        userTokenEntity.setUserEntity( fillUserEntity() );
        userTokenEntity.setExpired( false );
        userTokenEntity.setLastRequestTime( new Date() );
        userTokenEntity.setCreatedOn( new Date() );
        userTokenEntity.setExpiryTime( new Date() );
        return userTokenEntity;
    }

    /**
     * A method for filling user token entity.
     *
     * @return the user token entity
     */
    private UserTokenDTO fillUserTokenDTO() {
        userTokenDTO = new UserTokenDTO();
        userTokenDTO.setId( RANDOM_UUID.toString() );
        userTokenDTO.setToken( TOKEN );
        userTokenDTO.setIpAddress( IP_ADDRESS );
        userTokenDTO.setBrowserAgent( USER_AGENT );
        userTokenDTO.setUserId( RANDOM_UUID.toString() );
        userTokenDTO.setExpired( false );
        userTokenDTO.setLastRequestTime( new Date() );
        userTokenDTO.setCreatedOn( new Date() );
        return userTokenDTO;
    }

    /**
     * Fill user entity.
     *
     * @return the user entity
     */
    private UserEntity fillUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId( RANDOM_UUID );
        return userEntity;
    }

}
