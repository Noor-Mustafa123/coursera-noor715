package de.soco.software.simuspace.suscore.common.util;

import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.model.LanguageDTO;

/**
 * The Class ResponseUtilsTest.
 */
public class ResponseUtilsTest {

    /**
     * The Constant USER_UID.
     */
    private static final String LANGUAGE_ID = "abc";

    /**
     * The Constant USER_PASSWORD.
     */
    private static final String LANGUAGE_NAME = "english";

    /**
     * The Constant MESSAGE.
     */
    private static final String MESSAGE = "message";

    /**
     * The Constant SUCCESS_STATUS.
     */
    private static final int SUCCESS_STATUS = HttpStatus.SC_OK;

    /**
     * The Constant INTERNAL_SERVER_ERROR.
     */
    private static final int INTERNAL_SERVER_ERROR = HttpStatus.SC_INTERNAL_SERVER_ERROR;

    /**
     * The Constant EXPECTED_RESPONSE_DATA.
     */
    private static final String EXPECTED_RESPONSE_DATA = "{\"success\":true,\"expire\":false,\"data\":{\"id\":\"abc\",\"name\":\"english\"}}";

    /**
     * The Constant EXPECTED_RESPONSE_DATA_MESSAGE.
     */
    private static final String EXPECTED_RESPONSE_DATA_MESSAGE = "{\"message\":{\"content\":\"message\",\"type\":\"SUCCESS\"},\"success\":true,\"expire\":false,\"data\":{\"id\":\"abc\",\"name\":\"english\"}}";

    /**
     * The Constant EXPECTED_RESPONSE_DATA_MESSAGE_EXPRIE.
     */
    private static final String EXPECTED_RESPONSE_DATA_MESSAGE_EXPRIE = "{\"message\":{\"content\":\"message\",\"type\":\"SUCCESS\"},\"success\":true,\"expire\":true,\"data\":{\"id\":\"abc\",\"name\":\"english\"}}";

    /**
     * The Constant EXPECTED_RESPONSE_FAILURE_STATUS.
     */
    private static final String EXPECTED_RESPONSE_FAILURE_STATUS = "{\"message\":{\"content\":\"An unexpected error has occurred. Please report this to support\",\"type\":\"ERROR\"},\"success\":false,\"expire\":false}";

    /**
     * The Constant EXPECTED_RESPONSE_FAILURE.
     */
    private static final String EXPECTED_RESPONSE_FAILURE = "{\"message\":{\"content\":\"An unexpected error has occurred. Please report this to support\",\"type\":\"ERROR\"},\"success\":false,\"expire\":false}";

    /**
     * The Constant EXPRIE_TRUE.
     */
    private static final boolean EXPRIE_TRUE = true;

    /**
     * The Constant EXPRIE_FALSE.
     */
    private static final boolean EXPRIE_FALSE = false;

    /**
     * The Constant SUCCESS_STRING.
     */
    private static final String SUCCESS_STRING = "SUCCESS";

    /**
     * The Constant ERROR_STRING.
     */
    private static final String ERROR_STRING = "ERROR";

    /**
     * Should return response when objectis passed for success.
     */
    @Test
    public void shouldReturnResponseWhenObjectisPassedForSuccess() {
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        Response actual = ResponseUtils.success( languageDTO );
        Assert.assertEquals( EXPECTED_RESPONSE_DATA, actual.getEntity() );
        Assert.assertEquals( SUCCESS_STATUS, actual.getStatus() );
    }

    /**
     * Should return response when object and messageis passed for success.
     */
    @Test
    public void shouldReturnResponseWhenObjectAndMessageisPassedForSuccess() {
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        Response actual = ResponseUtils.success( MESSAGE, languageDTO );
        Assert.assertEquals( EXPECTED_RESPONSE_DATA_MESSAGE, actual.getEntity() );
        Assert.assertEquals( SUCCESS_STATUS, actual.getStatus() );
    }

    /**
     * Should return response when object message and expiredis passed for success.
     */
    @Test
    public void shouldReturnResponseWhenObjectMessageAndExpiredisPassedForSuccess() {
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        Response actual = ResponseUtils.success( MESSAGE, languageDTO, EXPRIE_TRUE );
        Assert.assertEquals( EXPECTED_RESPONSE_DATA_MESSAGE_EXPRIE, actual.getEntity() );
        Assert.assertEquals( SUCCESS_STATUS, actual.getStatus() );
    }

    /**
     * Should return response when object message and not expiredis passed for success.
     */
    @Test
    public void shouldReturnResponseWhenObjectMessageAndNotExpiredisPassedForSuccess() {
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        Response actual = ResponseUtils.success( MESSAGE, languageDTO, EXPRIE_FALSE );
        Assert.assertEquals( EXPECTED_RESPONSE_DATA_MESSAGE, actual.getEntity() );
        Assert.assertEquals( SUCCESS_STATUS, actual.getStatus() );
    }

    /**
     * Should return response when error code and message is passed for failure.
     */
    @Test
    public void shouldReturnResponseWhenErrorCodeAndMessageIsPassedForFailure() {
        Response actual = ResponseUtils.failure( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                MessageBundleFactory.getMessage( Messages.WEBSERVICE_INTERNAL_SERVER_ERROR.getKey() ) );
        Assert.assertEquals( EXPECTED_RESPONSE_FAILURE_STATUS, actual.getEntity() );
        Assert.assertEquals( INTERNAL_SERVER_ERROR, actual.getStatus() );
    }

    /**
     * Should return response when message is passed for failure.
     */
    @Test
    public void shouldReturnResponseWhenMessageIsPassedForFailure() {
        Response actual = ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INTERNAL_SERVER_ERROR.getKey() ) );
        Assert.assertEquals( EXPECTED_RESPONSE_FAILURE, actual.getEntity() );
        Assert.assertEquals( SUCCESS_STATUS, actual.getStatus() );
    }

    /**
     * Should return response when message and status is passed for failure.
     */
    @Test
    public void shouldReturnResponseWhenMessageAndStatusIsPassedForFailure() {
        Response actual = ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INTERNAL_SERVER_ERROR.getKey() ),
                INTERNAL_SERVER_ERROR );
        Assert.assertEquals( EXPECTED_RESPONSE_FAILURE_STATUS, actual.getEntity() );
        Assert.assertEquals( INTERNAL_SERVER_ERROR, actual.getStatus() );
    }

    /**
     * Should return response when message and object is passed for success response.
     */
    @Test
    public void shouldReturnResponseWhenMessageAndObjectIsPassedForSuccessResponse() {
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        SusResponseDTO actual = ResponseUtils.successResponse( MESSAGE, languageDTO );
        Assert.assertTrue( actual.getData() instanceof LanguageDTO );
        Assert.assertEquals( true, actual.getSuccess() );
        Assert.assertEquals( SUCCESS_STRING, actual.getMessage().getType() );
    }

    /**
     * Should return response when message and object is passed for failure response.
     */
    @Test
    public void shouldReturnResponseWhenMessageAndObjectIsPassedForFailureResponse() {
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        SusResponseDTO actual = ResponseUtils.failureResponse( MESSAGE, languageDTO );
        Assert.assertTrue( actual.getData() instanceof LanguageDTO );
        Assert.assertEquals( false, actual.getSuccess() );
        Assert.assertEquals( ERROR_STRING, actual.getMessage().getType() );
    }

}
