package de.soco.software.simuspace.suscore.common.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;

/**
 * The Class ValidationUtilsTest.
 */
public class ValidationUtilsTest {

    /**
     * The string constant representing an empty string.
     */
    public static final String EMPTY_STRING = "";

    /**
     * The Constant NULL_STRING.
     */
    public static final String NULL_STRING = null;

    /**
     * The Constant TEST_STRING.
     */
    public static final String TEST_STRING = "Test String";

    /**
     * The Constant TEST_FILED.
     */
    public static final String TEST_FILED = "Test Filed";

    /**
     * The Constant TEST_ISOPTIONAL_TRUE.
     */
    public static final boolean TEST_ISOPTIONAL_TRUE = true;

    /**
     * The Constant TEST_ISOPTIONAL_FALSE.
     */
    public static final boolean TEST_ISOPTIONAL_FALSE = false;

    /**
     * The Constant TEST_SPECIALCHARS_TRUE.
     */
    public static final boolean TEST_SPECIALCHARS_TRUE = true;

    /**
     * The Constant TEST_SPECIALCHARS_FALSE.
     */
    public static final boolean TEST_SPECIALCHARS_FALSE = false;

    /**
     * The Constant STANDARD_NAME_LENGTH.
     */
    public static final int STANDARD_NAME_LENGTH = 12;

    /**
     * The Constant TEST_STRING_LENGTH.
     */
    public static final String TEST_STRING_LENGTH = "Lorem Ipsum is simply dummy text of the printing and typesetting industry";

    /**
     * The Constant TEST_VALID_PASSWORD.
     */
    public static final String TEST_VALID_PASSWORD = "Abcd1234";

    /**
     * The Constant TEST_INVALID_PASSWORD.
     */
    public static final String TEST_INVALID_PASSWORD = "abcdefgi";

    /**
     * The Constant TEST_INVALID_EMAIL.
     */
    public static final String TEST_INVALID_EMAIL = "abc.com";

    /**
     * The Constant TEST_VALID_EMAIL.
     */
    public static final String TEST_VALID_EMAIL = "abc@domain.com";

    /**
     * The Constant TEST_INVALID_PHONE_NUMBER.
     */
    public static final String TEST_INVALID_PHONE_NUMBER = "a123456";

    /**
     * The Constant TEST_INVALID_PHONE_NUMBER_MIN_LENGHT.
     */
    public static final String TEST_INVALID_PHONE_NUMBER_MIN_LENGHT = "1234567";

    /**
     * The Constant TEST_VALID_PHONE_NUMBER.
     */
    public static final String TEST_VALID_PHONE_NUMBER = "+923001234567";

    /**
     * The Constant TEST_INVALID_SPECIAL_CHARACTERS_STRING.
     */
    public static final String TEST_INVALID_SPECIAL_CHARACTERS_STRING = "Test@@@";

    /**
     * The Constant VALID_SPECIAL_CHARACTERS_STRING.
     */
    public static final String VALID_SPECIAL_CHARACTERS_STRING = "Test<>|";

    /**
     * The Constant TEST_VALID_SPECIAL_CHARACTERS_STRING.
     */
    public static final String TEST_VALID_SPECIAL_CHARACTERS_STRING = "Test String";

    /**
     * The Constant TEST_INVALID_UUID.
     */
    public static final String TEST_INVALID_UUID = "814176e2ae7a1";

    /**
     * The Constant TEST_VALID_UUID.
     */
    public static final String TEST_VALID_UUID = "814176e2-e7a1-4474-aecf-718efb9f196e";

    /**
     * The Constant TEST_TOKENIZE_STRING.
     */
    public static final String TEST_TOKENIZE_STRING = "de.soco.software.simuspace.suscore.common.util.ValidationUtils";

    /**
     * The Constant TEST_TOKEN.
     */
    public static final String TEST_TOKEN = ".";

    /**
     * The Constant TEST_TOKENIZE_EXPECTED.
     */
    public static final String[] TEST_TOKENIZE_EXPECTED = { "de", "soco", "software", "simuspace", "suscore", "common", "util",
            "ValidationUtils" };

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Should successfully check string is not null or empty.
     */
    @Test
    public void shouldSuccessfullyCheckStringIsNotNullOrEmpty() {
        boolean actual = ValidationUtils.isNotNullOrEmpty( TEST_STRING );
        Assert.assertTrue( actual );
    }

    /**
     * Test when empty string passed to check is not null or empty.
     */
    @Test
    public void shouldReturnFalseWhenEmptyStringisPassedToCheckIsNotNullOrEmpty() {
        boolean actual = ValidationUtils.isNotNullOrEmpty( EMPTY_STRING );
        Assert.assertFalse( actual );
    }

    /**
     * Test when null string passed to check is not null or empty.
     */
    @Test
    public void shouldReturnFalseWhenNullStringPassedToCheckIsNotNullOrEmpty() {
        boolean actual = ValidationUtils.isNotNullOrEmpty( NULL_STRING );
        Assert.assertFalse( actual );
    }

    /**
     * Should successfully check string is null or empty.
     */
    @Test
    public void shouldSuccessfullyCheckWhenEmptyStringisPassedToCheckStringIsNullOrEmpty() {
        boolean actual = ValidationUtils.isNullOrEmpty( EMPTY_STRING );
        Assert.assertTrue( actual );
    }

    /**
     * Test when string passed to check is null or empty.
     */
    @Test
    public void shouldReturnFalseWhenStringPassedToCheckIsNullOrEmpty() {
        boolean actual = ValidationUtils.isNullOrEmpty( TEST_STRING );
        Assert.assertFalse( actual );
    }

    /**
     * Test validate field and length.
     */
    @Test
    public void shouldSuccessfullyCheckWhenFiledDataIsPassedToValidateFieldAndLength() {
        Notification actual = ValidationUtils.validateFieldAndLength( TEST_STRING, TEST_FILED, STANDARD_NAME_LENGTH, TEST_ISOPTIONAL_FALSE,
                TEST_SPECIALCHARS_FALSE );
        Assert.assertFalse( actual.hasErrors() );
    }

    /**
     * Test validate field and length when special chars passed.
     */
    @Test
    public void shouldSuccessfullyValidateFieldAndLengthWhenSpecialCharsPassed() {
        Notification actual = ValidationUtils.validateFieldAndLength( TEST_INVALID_SPECIAL_CHARACTERS_STRING, TEST_FILED,
                STANDARD_NAME_LENGTH, TEST_ISOPTIONAL_FALSE, TEST_SPECIALCHARS_TRUE );
        Assert.assertTrue( actual.hasErrors() );
    }

    /**
     * Test validate field and length when special chars allowed.
     */
    @Test
    public void shouldSuccessfullyValidateFieldAndLengthWhenSpecialCharsAllowed() {
        Notification actual = ValidationUtils.validateFieldAndLength( TEST_INVALID_SPECIAL_CHARACTERS_STRING, TEST_FILED,
                STANDARD_NAME_LENGTH, TEST_ISOPTIONAL_FALSE, TEST_SPECIALCHARS_FALSE );
        Assert.assertFalse( actual.hasErrors() );
    }

    /**
     * Test validate field and length when empty string passed.
     */
    @Test
    public void shouldSuccessfullyValidateFieldAndLengthWhenEmptyStringPassed() {
        Notification actual = ValidationUtils.validateFieldAndLength( EMPTY_STRING, TEST_FILED, STANDARD_NAME_LENGTH, TEST_ISOPTIONAL_FALSE,
                TEST_SPECIALCHARS_TRUE );
        Assert.assertTrue( actual.hasErrors() );
    }

    /**
     * Test validate field and length when empty string is allowed.
     */
    @Test
    public void shouldSuccessfullyValidateFieldAndLengthWhenEmptyStringIsAllowed() {
        Notification actual = ValidationUtils.validateFieldAndLength( EMPTY_STRING, TEST_FILED, STANDARD_NAME_LENGTH, TEST_ISOPTIONAL_TRUE,
                TEST_SPECIALCHARS_TRUE );
        Assert.assertFalse( actual.hasErrors() );
    }

    /**
     * Test validate field and length when length increases.
     */
    @Test
    public void shouldSuccessfullyValidateFieldAndLengthWhenLengthIncreases() {
        Notification actual = ValidationUtils.validateFieldAndLength( TEST_STRING_LENGTH, TEST_FILED, STANDARD_NAME_LENGTH,
                TEST_ISOPTIONAL_TRUE, TEST_SPECIALCHARS_TRUE );
        Assert.assertTrue( actual.hasErrors() );
    }

    /**
     * Test password format is valid.
     */
    @Test
    public void shouldSuccessfullyCheckPasswordFormatIsValid() {
        String actual = ValidationUtils.passwordValidation( TEST_VALID_PASSWORD, MessageBundleFactory.getMessage( UserDTO.PASS_LABEL ) );
        Assert.assertNull( actual );
    }

    /**
     * Test password format is invalid.
     */
    @Test
    public void shouldSuccessfullyCheckPasswordFormatIsInvalid() {
        String actual = ValidationUtils.passwordValidation( TEST_INVALID_PASSWORD, MessageBundleFactory.getMessage( UserDTO.PASS_LABEL ) );
        Assert.assertNotNull( actual );
    }

    /**
     * Test password format when password is null.
     */
    @Test
    public void shouldSuccessfullyCheckPasswordFormatWhenPasswordIsNull() {
        String actual = ValidationUtils.passwordValidation( EMPTY_STRING, MessageBundleFactory.getMessage( UserDTO.PASS_LABEL ) );
        Assert.assertNotNull( actual );
    }

    /**
     * Test email format when email is null.
     */
    @Test
    public void shouldSuccessfullyCheckEmailFormatWhenEmailIsNull() {
        boolean actual = ValidationUtils.isValidEmailAddress( EMPTY_STRING );
        Assert.assertFalse( actual );
    }

    /**
     * Test email format when email is invalid.
     */
    @Test
    public void shouldSuccessfullyCheckEmailFormatWhenEmailIsInvalid() {
        boolean actual = ValidationUtils.isValidEmailAddress( TEST_INVALID_EMAIL );
        Assert.assertFalse( actual );
    }

    /**
     * Test email format when email is valid.
     */
    @Test
    public void shouldSuccessfullyCheckEmailFormatWhenEmailIsValid() {
        boolean actual = ValidationUtils.isValidEmailAddress( TEST_VALID_EMAIL );
        Assert.assertTrue( actual );
    }

    /**
     * Test phone number format when empty string passed.
     */
    @Test
    public void shouldSuccessfullyCheckPhoneNumberFormatWhenEmptyStringPassed() {
        boolean actual = ValidationUtils.isValidPhoneNumber( EMPTY_STRING );
        Assert.assertTrue( actual );
    }

    /**
     * Test phone number format when invalid string passed.
     */
    @Test
    public void shouldSuccessfullyCheckPhoneNumberFormatWhenInvalidStringPassed() {
        boolean actual = ValidationUtils.isValidPhoneNumber( TEST_INVALID_PHONE_NUMBER );
        Assert.assertFalse( actual );
    }

    /**
     * Test phone number format when valid string passed.
     */
    @Test
    public void shouldSuccessfullyCheckPhoneNumberFormatWhenValidStringPassed() {
        boolean actual = ValidationUtils.isValidPhoneNumber( TEST_VALID_PHONE_NUMBER );
        Assert.assertTrue( actual );
    }

    /**
     * Test special characters when invalid string passed.
     */
    @Test
    public void shouldSuccessfullyCheckSpecialCharactersWhenInvalidStringPassed() {
        boolean actual = ValidationUtils.validateSpecialCharacters( TEST_INVALID_SPECIAL_CHARACTERS_STRING );
        Assert.assertFalse( actual );
    }

    /**
     * Test special characters when valid string passed.
     */
    @Test
    public void shouldSuccessfullyCheckSpecialCharactersWhenValidStringPassed() {
        boolean actual = ValidationUtils.validateSpecialCharacters( TEST_VALID_SPECIAL_CHARACTERS_STRING );
        Assert.assertTrue( actual );
    }

    /**
     * Test validate special characters with filed name and value throw exception.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidStringAndFiledsArePassedForValidateSpecialCharacters() {
        thrown.expect( SusException.class );
        ValidationUtils.validateSpecialCharacters( TEST_INVALID_SPECIAL_CHARACTERS_STRING, TEST_FILED );
    }

    /**
     * Test validate special characters with filed name and value.
     */
    @Test
    public void shouldSuccessfullyValidateSpecialCharactersWithFiledNameAndValue() {
        ValidationUtils.validateSpecialCharacters( TEST_STRING, TEST_FILED );
    }

    /**
     * Should throw exception when invalid string passed to validate UUID.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidStringPassedToValidateUUID() {
        thrown.expect( SusException.class );
        ValidationUtils.validateUUID( TEST_INVALID_UUID );
    }

    /**
     * Test UUID string when invalid string passed.
     */
    @Test
    public void shouldSuccessfullyCheckUUIDStringWhenInvalidStringPassed() {
        boolean actual = ValidationUtils.validateUUIDString( TEST_INVALID_UUID );
        Assert.assertFalse( actual );
    }

    /**
     * Test UUID string when valid string passed.
     */
    @Test
    public void shouldSuccessfullyCheckUUIDStringWhenValidStringPassed() {
        boolean actual = ValidationUtils.validateUUIDString( TEST_VALID_UUID );
        Assert.assertTrue( actual );
    }

    /**
     * Should return empty string when empty string is passed for trimming.
     */
    @Test
    public void shouldReturnEmptyStringWhenEmptyStringIsPassedForStringTrimming() {
        String actual = ValidationUtils.trimString( EMPTY_STRING );
        Assert.assertEquals( EMPTY_STRING, actual );
    }

    /**
     * Should return empty string when null is passed for trimming.
     */
    @Test
    public void shouldReturnEmptyStringWhenNullIsPassedForStringTrimming() {
        String actual = ValidationUtils.trimString( NULL_STRING );
        Assert.assertEquals( EMPTY_STRING, actual );
    }

    /**
     * Should return trimmed string when valid string is passed for trimming.
     */
    @Test
    public void shouldReturnTrimmedStringWhenValidStringIsPassedForStringTrimming() {
        String actual = ValidationUtils.trimString( TEST_STRING );
        Assert.assertNotNull( actual );
    }

    /**
     * Test tokenize string by char.
     */
    @SuppressWarnings( "deprecation" )
    @Test
    public void shouldSuccessfullyCheckTokenizeStringByChar() {
        String[] actual = ValidationUtils.tokenizeStringByChar( TEST_TOKENIZE_STRING, TEST_TOKEN );
        Assert.assertEquals( TEST_TOKENIZE_EXPECTED, actual );
    }

    /**
     * Test tokenize string by char when empty string passed.
     */
    @Test
    public void shouldSuccessfullyCheckTokenizeStringByCharWhenEmptyStringPassed() {
        String[] actual = ValidationUtils.tokenizeStringByChar( EMPTY_STRING, EMPTY_STRING );
        Assert.assertEquals( 0, actual.length );
    }

    /**
     * Should successfully check when filed data is passed to validate global field and length.
     */
    @Test
    public void shouldSuccessfullyCheckWhenFiledDataIsPassedToValidateGlobalFieldAndLength() {
        Notification actual = ValidationUtils.validateGlobalFieldAndLength( TEST_STRING, TEST_FILED, STANDARD_NAME_LENGTH,
                TEST_ISOPTIONAL_FALSE, TEST_SPECIALCHARS_FALSE );
        Assert.assertFalse( actual.hasErrors() );
    }

    /**
     * Should successfully validate global field and length when special chars passed.
     */
    @Test
    public void shouldSuccessfullyValidateGlobalFieldAndLengthWhenSpecialCharsPassed() {
        Notification actual = ValidationUtils.validateGlobalFieldAndLength( VALID_SPECIAL_CHARACTERS_STRING, TEST_FILED,
                STANDARD_NAME_LENGTH, TEST_ISOPTIONAL_FALSE, TEST_SPECIALCHARS_TRUE );
        Assert.assertTrue( actual.hasErrors() );
    }

    /**
     * Should successfully validate global field and length when legnth is greater.
     */
    @Test
    public void shouldSuccessfullyValidateGlobalFieldAndLengthWhenLegnthIsGreater() {
        Notification actual = ValidationUtils.validateGlobalFieldAndLength( TEST_STRING_LENGTH, TEST_FILED, STANDARD_NAME_LENGTH,
                TEST_ISOPTIONAL_FALSE, TEST_SPECIALCHARS_TRUE );
        Assert.assertTrue( actual.hasErrors() );
    }

    /**
     * Should successfully validate global field and length when name is null.
     */
    @Test
    public void shouldSuccessfullyValidateGlobalFieldAndLengthWhenNameIsNull() {
        Notification actual = ValidationUtils.validateGlobalFieldAndLength( null, TEST_FILED, STANDARD_NAME_LENGTH, TEST_ISOPTIONAL_FALSE,
                TEST_SPECIALCHARS_TRUE );
        Assert.assertTrue( actual.hasErrors() );
    }

}
