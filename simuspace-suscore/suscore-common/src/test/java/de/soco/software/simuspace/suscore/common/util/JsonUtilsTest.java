package de.soco.software.simuspace.suscore.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.LanguageDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;

/**
 * The Class is responsible to test public methods of {@link JsonUtils}.
 *
 * @author M.Nasir.Farooq
 */
public class JsonUtilsTest {

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant UID.
     */
    private static final String UID = "SCES122";

    /**
     * The Constant UID_FIELD.
     */
    private static final String UID_FIELD = "uid";

    /**
     * The Constant ID_FIELD.
     */
    private static final String ID_FIELD = "id";

    /**
     * The Constant USER_UID.
     */
    private static final String LANGUAGE_ID = "abc";

    /**
     * The Constant TEST_STRING.
     */
    private static final String TEST_STRING = "test string";

    /**
     * The Constant TEST_STRING.
     */
    private static final String JSON_TEST_STRING = "{\"id\":\"test string\",\"name\":\"test string\",\"icon\":null}";

    /**
     * The Constant TEST_STRING.
     */
    private static final String JSON_LIST_TEST_STRING = "[" + JSON_TEST_STRING + "]";

    /**
     * The Constant EXPECTED_JSON_GROUP_PAYLOAD.
     */
    private static final String EXPECTED_JSON_GROUP_PAYLOAD = "{\"seletionId\":\"7be336e2-8b19-4497-a4fd-9aba094755e5\",\"name\":\"name\",\"description\":\"description\",\"users\":\"7be336e2-8b19-4497-a4fd-9aba094755e5\",\"status\":\"Active\"}";

    /**
     * The Constant JSON_GROUP_PAYLOAD.
     */
    private static final String JSON_GROUP_PAYLOAD = "{\"name\":\"name\",\"description\":\"description\",\"status\":\"Active\",\"users\":\"7be336e2-8b19-4497-a4fd-9aba094755e5\"}";

    /**
     * The Constant USER_PASSWORD.
     */
    private static final String LANGUAGE_NAME = "english";

    /**
     * The Constant EXPECTED_JSON_LANGUAGE_DTO_STRING.
     */
    private static final String EXPECTED_JSON_LANGUAGE_DTO_STRING = "{\"id\":\"" + LANGUAGE_ID + "\",\"name\":\"" + LANGUAGE_NAME + "\"}";

    /**
     * The Constant EXPECTED_JSON_ROLE_PAYLOAD.
     */
    private static final String EXPECTED_JSON_ROLE_PAYLOAD = "{\"seletionId\":\"5b1a6cdc-3d41-4516-8a89-1ad5f5899b2f\",\"name\":\"name\",\"description\":\"description\",\"groups\":\"5b1a6cdc-3d41-4516-8a89-1ad5f5899b2f\",\"status\":\"Active\"}";

    /**
     * The Constant JSON_ROLE_PAYLOAD.
     */
    private static final String JSON_ROLE_PAYLOAD = "{\"name\":\"name\",\"description\":\"description\",\"status\":\"Active\",\"groups\":\"5b1a6cdc-3d41-4516-8a89-1ad5f5899b2f\"}";

    /**
     * The Constant WEBSERVICE_INPUT_CANT_BE_NULL.
     */
    private static final String WEBSERVICE_INPUT_CANT_BE_NULL = "Input can not be null";

    /**
     * The Constant WEBSERVICE_INVALID_PARAMETERS_SUPLIED.
     */
    private static final String WEBSERVICE_INVALID_PARAMETERS_SUPLIED = "Invalid parameters provided.";

    /**
     * The Constant WEBSERVICE_JSON_PARSING_ERROR.
     */
    private static final String WEBSERVICE_JSON_PARSING_ERROR = "Unable to parse the json.";

    /**
     * Should return json string of object when not null Object is passed for to json.
     */
    @Test
    public void shouldReturnJsonStringOfObjectWhenNotNullObjectIsPassedForToJson() {
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        Assert.assertEquals( EXPECTED_JSON_LANGUAGE_DTO_STRING, JsonUtils.toJson( languageDTO ) );
    }

    /**
     * Should return null when null Object is passed for to json.
     */
    @Test
    public void shouldRetunNullWhenNullObjectIsPassedForToJson() {
        LanguageDTO languageDTO = null;
        String expectedObjectString = null;
        String actualObjectString = JsonUtils.toJson( languageDTO );
        Assert.assertEquals( expectedObjectString, actualObjectString );
    }

    /**
     * Should convert select object list to json string when valid select object list is provided.
     */
    @Test
    public void shouldConvertSelectObjectListToJsonStringWhenValidSelectObjectListIsProvided() {
        List< SelectOptionsUI > list = new ArrayList<>();
        list.add( new SelectOptionsUI( TEST_STRING, TEST_STRING ) );
        String convertListToJson = JsonUtils.convertListToJson( list );
        Assert.assertEquals( JSON_LIST_TEST_STRING, convertListToJson.toString() );
    }

    /**
     * Should convert select object to json string when valid select object is provided.
     */
    @Test
    public void shouldConvertSelectObjectToJsonStringWhenValidSelectObjectIsProvided() {
        String json = JsonUtils.objectToJson( new SelectOptionsUI( TEST_STRING, TEST_STRING ) );
        Assert.assertEquals( JSON_TEST_STRING, json );
    }

    /**
     * Should convert linked hash map to specific class object.
     */
    @Test
    public void shouldConvertLinkedHashMapToSpecificClassObject() {
        LinkedHashMap< String, String > userMap = new LinkedHashMap<>();
        userMap.put( ID_FIELD, UUID.randomUUID().toString() );
        userMap.put( UID_FIELD, UID );
        UserDTO userDTO = JsonUtils.linkedMapObjectToClassObject( userMap, UserDTO.class );
        Assert.assertTrue( userDTO instanceof UserDTO );
    }

    /**
     * Should convert linked hash map to specific class object.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidClassIsPassedToConverObject() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) );

        LinkedHashMap< String, String > userMap = new LinkedHashMap<>();
        userMap.put( ID_FIELD, UUID.randomUUID().toString() );
        userMap.put( UID_FIELD, UID );
        JsonUtils.linkedMapObjectToClassObject( userMap, LanguageDTO.class );
    }

    /**
     * Should successfully retuen fixed group json.
     */
    @Test
    public void shouldSuccessfullyRetuenFixedGroupJson() {
        JSONObject actualJson = null;
        try {
            actualJson = JsonUtils.jsonObjectFixForGroup( JSON_GROUP_PAYLOAD );
        } catch ( ParseException e ) {
        }
        Assert.assertEquals( EXPECTED_JSON_GROUP_PAYLOAD, actualJson.toJSONString() );
    }

    /**
     * Should successfully retuen fixed role json.
     */
    @Test
    public void shouldSuccessfullyRetuenFixedRoleJson() {
        JSONObject actualJson = null;
        try {
            actualJson = JsonUtils.jsonObjectFixForRole( JSON_ROLE_PAYLOAD );
        } catch ( ParseException e ) {
        }
        Assert.assertEquals( actualJson.toJSONString(), EXPECTED_JSON_ROLE_PAYLOAD );

    }

    /**
     * Should return object when json string is passed for json to object.
     */
    @Test
    public void shouldReturnObjectWhenJsonStringIsPassedForJsonToObject() {
        LanguageDTO languageDTO = JsonUtils.jsonToObject( EXPECTED_JSON_LANGUAGE_DTO_STRING, LanguageDTO.class );
        Assert.assertTrue( languageDTO instanceof LanguageDTO );
    }

    /**
     * Should return exception empty string is passed for json to object.
     */
    @Test
    public void shouldReturnExceptionEmptyStringIsPassedForJsonToObject() {
        thrown.expect( SusException.class );
        thrown.expectMessage( WEBSERVICE_INVALID_PARAMETERS_SUPLIED );
        JsonUtils.jsonToObject( EMPTY_STRING, LanguageDTO.class );
    }

    /**
     * Should return exception null string is passed for json to object.
     */
    @Test
    public void shouldReturnExceptionNullStringIsPassedForJsonToObject() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
        JsonUtils.jsonToObject( NULL_STRING, LanguageDTO.class );
    }

    /**
     * Should return exception when unknown json string is passed for json to object.
     */
    @Test
    public void shouldReturnExceptionWhenUnknownJsonStringIsPassedForJsonToObject() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ) );
        JsonUtils.jsonToObject( JSON_LIST_TEST_STRING, LanguageDTO.class );
    }

    /**
     * Should return object when json string is passed for filtered json.
     */
    @Test
    public void shouldReturnObjectWhenJsonStringIsPassedForFilteredJson() {
        final String[] notRequiredProps = { DATA };
        final String message = WEBSERVICE_INTERNAL_SERVER_ERROR;
        SusResponseDTO susResponseDTO = new SusResponseDTO( Boolean.FALSE, new Message( Message.ERROR, message ) );
        String actual = JsonUtils.toFilteredJson( notRequiredProps, susResponseDTO );
        Assert.assertEquals( EXPECTED_FILTERED_JSON, actual );
    }

    /**
     * Should return object when file reader is passed for json node.
     */
    @Test
    public void shouldReturnObjectWhenFileReaderIsPassedForJsonNode() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ) );
        JsonUtils.jsonToObject( JSON_LIST_TEST_STRING, LanguageDTO.class );
    }

    /**
     * Should return JSON string when map of objects is passed for map to string gerneric value.
     */
    @Test
    public void shouldReturnJSONStringWhenMapOfObjectsIsPassedForMapToStringGernericValue() {
        String actual = JsonUtils.convertMapToStringGernericValue( TEST_OBJECTS_MAP );
        Assert.assertEquals( EXPECTED_MAP_JSON_STRING, actual );
    }

    /**
     * Should return map when string is passed for string to map gerneric value.
     */
    @Test
    public void shouldReturnMapWhenStringIsPassedForStringToMapGernericValue() {
        Map< String, Object > map = JsonUtils.convertStringToMapGenericValue( EXPECTED_MAP_JSON_STRING );
        String actual = JsonUtils.convertMapToStringGernericValue( map );
        String expected = JsonUtils.convertMapToStringGernericValue( TEST_OBJECTS_MAP );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should return json string when map is passed for map to string.
     */
    @Test
    public void shouldReturnJsonStringWhenMapIsPassedForMapToString() {
        String actual = JsonUtils.convertMapToString( TEST_MAP );
        Assert.assertEquals( JSON_LANGUAGE_DTO_STRING, actual );
    }

    /**
     * Should convert select object list to json string when valid select object list is provided.
     */

    @Test
    public void shouldConvertListToJsonStringWhenValidObjectListIsProvided() {
        List< SelectOptionsUI > list = new ArrayList<>();
        list.add( new SelectOptionsUI( TEST_STRING, TEST_STRING ) );
        String convertListToJson = JsonUtils.convertListToJson( list );
        Assert.assertEquals( JSON_LIST_TEST_STRING, convertListToJson.toString() );
    }

    /**
     * Should return map when json string is passed for string to map.
     */
    @Test
    public void shouldReturnMapWhenJsonStringIsPassedForStringToMap() {
        Map< ?, ? > actual = JsonUtils.convertStringToMap( JSON_LANGUAGE_DTO_STRING );
        Assert.assertEquals( TEST_MAP, actual );
    }

    /**
     * Shouldthrow exception when empty string is passed for string to map.
     */
    @Test
    public void shouldthrowExceptionWhenEmptyStringIsPassedForStringToMap() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) );
        JsonUtils.convertStringToMap( TEST_STRING );
    }

    /**
     * Should return value by key when json string is provided for get value.
     */
    @Test
    public void shouldReturnValueByKeyWhenJsonStringIsProvidedForGetValue() {
        try {
            String actual = JsonUtils.getValue( EXPECTED_JSON_LANGUAGE_DTO_STRING, KEY );
            Assert.assertEquals( LANGUAGE_ID, actual );
        } catch ( IOException e ) {
            e.printStackTrace();
        }

    }

    /**
     * Should convert list of object when json string and object classt is provided.
     */
    @Test
    public void shouldConvertListOfObjectWhenJsonStringAndObjectClasstIsProvided() {
        List< SelectOptionsUI > actual = JsonUtils.jsonToList( JSON_LIST_TEST_STRING, SelectOptionsUI.class );
        List< SelectOptionsUI > list = new ArrayList<>();
        list.add( new SelectOptionsUI( TEST_STRING, TEST_STRING ) );
        Assert.assertEquals( list, actual );
    }

    /**
     * Shouldthrow exception when empty string and object classt is provided.
     */
    @Test
    public void shouldthrowExceptionWhenEmptyStringAndObjectClasstIsProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage( WEBSERVICE_JSON_PARSING_ERROR );
        JsonUtils.jsonToList( EMPTY_STRING, SelectOptionsUI.class );
    }

    /**
     * Shouldthrow exception when invalid string and object classt is provided.
     */
    @Test
    public void shouldthrowExceptionWhenInvalidStringAndObjectClasstIsProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) );
        JsonUtils.jsonToList( TEST_STRING, SelectOptionsUI.class );
    }

    /**
     * Should return json string when map is passed for json.
     */
    @Test
    public void shouldReturnJsonStringWhenMapIsPassedForJson() {
        String actual = JsonUtils.toJson( TEST_MAP );
        Assert.assertEquals( JSON_LANGUAGE_DTO_STRING, actual );
    }

    /**
     * Should return map when json string is passed for json to map.
     */
    @Test
    public void shouldReturnMapWhenJsonStringIsPassedForJsonToMap() {
        Map< ?, ? > map = new HashMap< Object, Object >();
        Map< ?, ? > actual = JsonUtils.jsonToMap( EXPECTED_JSON_LANGUAGE_DTO_STRING, map );
        Assert.assertEquals( TEST_MAP, actual );
    }

    /**
     * Shouldthrow exception when empty string is passed for json to map.
     */
    @Test
    public void shouldthrowExceptionWhenEmptyStringIsPassedForJsonToMap() {
        thrown.expect( SusException.class );
        thrown.expectMessage( WEBSERVICE_JSON_PARSING_ERROR );
        Map< ?, ? > map = new HashMap< Object, Object >();
        JsonUtils.jsonToMap( EMPTY_STRING, map );
    }

    /**
     * Shouldthrow exception when invalid json string is passed for json to map.
     */
    @Test
    public void shouldthrowExceptionWhenInvalidJsonStringIsPassedForJsonToMap() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ) );
        Map< ?, ? > map = new HashMap< Object, Object >();
        JsonUtils.jsonToMap( JSON_DATA_OBJECT_STRING, map );
    }

    /**
     * Should return json string when list is passed for json.
     */
    @Test
    public void shouldReturnJsonStringWhenListIsPassedForJson() {
        List< SelectOptionsUI > list = new ArrayList<>();
        list.add( new SelectOptionsUI( TEST_STRING, TEST_STRING ) );
        String actual = JsonUtils.toJson( list );
        System.out.println( JSON_LIST_TEST_STRING );
        System.out.println( actual );
        Assert.assertEquals( JSON_LIST_TEST_STRING, actual );
    }

    /**
     * Shouldthrow exception when null list is passed for json.
     */
    @Test
    public void shouldthrowExceptionWhenNullListIsPassedForJson() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
        List< SelectOptionsUI > list = null;
        JsonUtils.toJson( list );

    }

    /**
     * Should return json string when object is passed for json.
     */
    @Test
    public void shouldReturnJsonStringWhenObjectIsPassedForJson() {
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        String actual = JsonUtils.toJson( languageDTO );
        Assert.assertEquals( EXPECTED_JSON_LANGUAGE_DTO_STRING, actual );
    }

    /**
     * Should return null when object is passed for json.
     */
    @Test
    public void shouldReturnNullWhenObjectIsPassedForJson() {
        LanguageDTO languageDTO = null;
        String actual = JsonUtils.toJson( languageDTO );
        Assert.assertNull( actual );
    }

    /**
     * Should return json string when object and class is passed for json.
     */
    @Test
    public void shouldReturnJsonStringWhenObjectAndClassIsPassedForJson() {
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        String actual = JsonUtils.toJson( languageDTO, LanguageDTO.class );
        Assert.assertEquals( EXPECTED_JSON_LANGUAGE_DTO_STRING, actual );
    }

    /**
     * Should return null when object and class is passed for json.
     */
    @Test
    public void shouldReturnNullWhenObjectAndClassIsPassedForJson() {
        LanguageDTO languageDTO = null;
        String actual = JsonUtils.toJson( languageDTO, LanguageDTO.class );
        Assert.assertNull( actual );
    }

    /**
     * Shoud return false when json data is passed for duplicate key present inside certain object of payload.
     */
    @Test
    public void shoudReturnFalseWhenJsonDataIsPassedForDuplicateKeyPresentInsideCertainObjectOfPayload() {
        boolean actual = JsonUtils.isDuplicateKeyPresentInsideCertainObjectOfPayload( JSON_DATA_OBJECT_STRING, DATA );
        Assert.assertFalse( actual );
    }

    /**
     * Shoud return true when invalid json data is passed for duplicate key present inside certain object of payload.
     */
    @Test
    public void shoudReturnTrueWhenInvalidJsonDataIsPassedForDuplicateKeyPresentInsideCertainObjectOfPayload() {
        boolean actual = JsonUtils.isDuplicateKeyPresentInsideCertainObjectOfPayload( INVALID_JSON_DATA_OBJECT_STRING, DATA );
        Assert.assertTrue( actual );
    }

    /**
     * Shoud return json when data object is passed for JSON string.
     */
    @Test
    public void shoudReturnJsonWhenDataObjectIsPassedForJSONString() {
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        Assert.assertEquals( EXPECTED_JSON_LANGUAGE_DTO_STRING, JsonUtils.toJsonString( languageDTO ) );
    }

    /**
     * Shoud return exception when empty data object is passed for JSON string.
     */
    @Test
    public void shoudReturnExceptionWhenEmptyDataObjectIsPassedForJSONString() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) );
        LanguageDTO languageDTO = null;
        JsonUtils.toJsonString( languageDTO );
    }

    /**
     * Shoud return true when valid json string is passed for valid JSON.
     */
    @Test
    public void shoudReturnTrueWhenValidJsonStringIsPassedForValidJSON() {
        boolean actual = JsonUtils.isValidJSON( EXPECTED_JSON_LANGUAGE_DTO_STRING );
        Assert.assertTrue( actual );
    }

    /**
     * The Constant EXPECTED_JSON_LANGUAGE_DTO_STRING.
     */
    private static final String JSON_DATA_OBJECT_STRING = "{data={\"id\":\"" + LANGUAGE_ID + "\",\"name\":\"" + LANGUAGE_NAME + "\"}}";

    /**
     * The Constant EXPECTED_JSON_LANGUAGE_DTO_STRING.
     */
    private static final String INVALID_JSON_DATA_OBJECT_STRING = "{data={\"id\":\"" + LANGUAGE_ID + "\",\"id\":\"" + LANGUAGE_NAME
            + "\"}}";

    /**
     * The Constant DATA.
     */
    private static final String DATA = "data";

    /**
     * The Constant WEBSERVICE_INTERNAL_SERVER_ERROR.
     */
    private static final String WEBSERVICE_INTERNAL_SERVER_ERROR = "Internal server error.";

    /**
     * The Constant EXPECTED_FILTERED_JSON.
     */
    private static final String EXPECTED_FILTERED_JSON = "{\"message\":{\"content\":\"Internal server error.\",\"type\":\"ERROR\"},\"success\":false,\"expire\":false}";

    /**
     * The Constant EMPTY_STRING.
     */
    private static final String EMPTY_STRING = "";

    /**
     * The Constant NULL_STRING.
     */
    private static final String NULL_STRING = null;

    /**
     * The Constant TEST_TOKENIZE_EXPECTED.
     */
    public static final String[] TEST_STRING_ARRAY = { "id" };

    /**
     * The test map.
     */
    public static Map< String, String > TEST_MAP;

    /**
     * The test objects map.
     */
    public static Map< String, Object > TEST_OBJECTS_MAP;

    /**
     * The Constant EXPECTED_JSON_LANGUAGE_DTO_STRING.
     */
    private static final String JSON_LANGUAGE_DTO_STRING = "{\"name\":\"" + LANGUAGE_NAME + "\",\"id\":\"" + LANGUAGE_ID + "\"}";

    /**
     * The Constant KEY.
     */
    private static final String KEY = "id";

    /**
     * The Constant EXPECTED_MAP_JSON_STRING.
     */
    private static final String EXPECTED_MAP_JSON_STRING = "{\"SelectObjectUI\":{\"id\":\"test string\",\"name\":\"test string\",\"icon\":null},\"LanguageDTO\":{\"id\":\"abc\",\"name\":\"english\"}}";

    /**
     * Setup.
     */
    @Before
    public void setup() {
        TEST_MAP = new HashMap< String, String >();
        TEST_MAP.put( "id", "abc" );
        TEST_MAP.put( "name", "english" );
        LanguageDTO languageDTO = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        SelectOptionsUI selectOptionsUI = new SelectOptionsUI( TEST_STRING, TEST_STRING );
        TEST_OBJECTS_MAP = new HashMap< String, Object >();
        TEST_OBJECTS_MAP.put( "LanguageDTO", languageDTO );
        TEST_OBJECTS_MAP.put( "SelectObjectUI", selectOptionsUI );
    }

}