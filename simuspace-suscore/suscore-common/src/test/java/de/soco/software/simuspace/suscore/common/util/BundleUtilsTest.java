package de.soco.software.simuspace.suscore.common.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test Cases For BundleUtils Class.
 *
 * @author Ghulam.Abbas
 */
public class BundleUtilsTest {

    /**
     * constant USER_AGENT.
     */
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0";

    /**
     * constant user token.
     */
    private static final String TOKEN = "59c78c9adb09549062962b9b0c30c8821f618bfd";

    /**
     * constant resource path.
     */
    private static final String RESOURCE_PATH = "/sus";

    /**
     * constant request method.
     */
    private static final String HTTP_REQUEST_TYPE_POST = "POST";

    /**
     * constant IP address.
     */
    private static final String IP_ADDRESS = "http://127.0.0.1:8080";

    /**
     * The empty string.
     */
    public static final String EMPTY_STRING = "";

    /**
     * The Constant INVALID_USER_ID.
     */
    public static final String INVALID_USER_ID = "-1";

    /**
     * The Constant USER_ID.
     */
    private static final String USER_ID = "userId";

    /**
     * The Constant USER_NAME.
     */
    private static final String USER_NAME = "userName";

    /**
     * The Constant USER_ID.
     */
    private static final String TEST_USER_ID = "1";

    /**
     * The Constant USER_NAME.
     */
    private static final String TEST_USER_NAME = "SUS";

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Should successfully return token.
     */
    @Test
    public void shouldSuccessfullyReturnToken() {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST, true );
        String actual = BundleUtils.getTokenFromMessage( message );
        Assert.assertEquals( TOKEN, actual );
    }

    /**
     * Should return null when message is null does not contains toke.
     */
    @Test
    public void shouldReturnNullWhenMessageIsNullDoesNotContainsToke() {
        Message message = new MessageImpl();
        String actual = BundleUtils.getTokenFromMessage( message );
        Assert.assertEquals( EMPTY_STRING, actual );
    }

    /**
     * Should successfully return user id.
     */
    @Test
    public void shouldSuccessfullyReturnUserId() {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST, true );
        message.put( USER_ID, TEST_USER_ID );
        String actual = BundleUtils.getUserIdFromMessage( message );
        Assert.assertEquals( TEST_USER_ID, actual );
    }

    /**
     * Should return minus one when message is null.
     */
    @Test
    public void shouldReturnMinusOneWhenMessageIsNull() {
        Message message = new MessageImpl();
        String actual = BundleUtils.getUserIdFromMessage( message );
        Assert.assertEquals( INVALID_USER_ID, actual );
    }

    /**
     * Should return minus one when user id not found.
     */
    @Test
    public void shouldReturnMinusOneWhenUserIdNotFound() {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST, true );
        String actual = BundleUtils.getUserIdFromMessage( message );
        Assert.assertEquals( INVALID_USER_ID, actual );
    }

    /**
     * Should successfully return user user name.
     */
    @Test
    public void shouldSuccessfullyReturnUserUserName() {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST, true );
        message.put( USER_NAME, TEST_USER_NAME );
        String actual = BundleUtils.getUserNameFromMessageBundle( message );
        Assert.assertEquals( TEST_USER_NAME, actual );
    }

    /**
     * Should return user name null when message is null.
     */
    @Test
    public void shouldReturnUserNameNullWhenMessageIsNull() {
        Message message = new MessageImpl();
        String actual = BundleUtils.getUserNameFromMessageBundle( message );
        Assert.assertEquals( EMPTY_STRING, actual );
    }

    /**
     * Should return user name null when user name not found.
     */
    @Test
    public void shouldReturnUserNameNullWhenUserNameNotFound() {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST, true );
        String actual = BundleUtils.getUserNameFromMessageBundle( message );
        Assert.assertEquals( EMPTY_STRING, actual );
    }

    /**
     * Should return user name null when user name not found.
     */
    @Test
    public void shouldSuccessfullyReturnBrowserAgentFromMessageWhenValidMessageIsProvided() {
        Message message = prepareMessage( IP_ADDRESS, RESOURCE_PATH, HTTP_REQUEST_TYPE_POST, true );
        String actual = BundleUtils.getBrowserAgent( message );
        Assert.assertEquals( USER_AGENT, actual );
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

}
