/**
 * Copyright (C) 2013 - now() SOCO engineers GmbH All rights reserved.
 */
/**
 *
 */

package de.soco.software.simuspace.suscore.common.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;

/**
 * The Class StringUtilsTest.
 */
@PrepareForTest( StringUtils.class )
public class StringUtilsTest {

    /**
     * The Constant EMPTY_STRING.
     */
    private static final String EMPTY_STRING = "";

    /**
     * The Constant NAME_FIELD_USED_IN_MESSAGE_PREPRATION.
     */
    private static final String NAME_FIELD_USED_IN_MESSAGE_PREPRATION = "Name";

    /**
     * The Constant NAME_FIELD_USED_IN_MESSAGE_PREPRATION.
     */
    private static final String SPECIAL_CHARECTER = "<>?:|.";

    /**
     * The variable email
     */
    private String email;

    /**
     * The Constant VALID_SUS_STRING.
     */
    public static final String VALID_SUS_STRING = "test-_";

    /**
     * The Constant INVALID_SUS_STRING.
     */
    public static final String INVALID_SUS_STRING = "test!@#";

    /**
     * The thrown.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Sets the up.
     */
    @BeforeClass
    public static void setUp() {
    }

    /*
     * *************************************************************************
     * Test methods isNullOrEmpty and isNotNullOrEmpty
     * *************************************************************************
     */

    /**
     * Test is not null or empty with empty string.
     */
    @Test
    public void testEmptyString() {
        final String str = "";
        assertTrue( "The empty string must be detected as empty.", StringUtils.isNullOrEmpty( str ) );
        assertFalse( "The empty string must be detected as not not empty.", StringUtils.isNotNullOrEmpty( str ) );
    }

    /**
     * Test is not null or empty with null string.
     */
    @Test
    public void testIsNotNullOrEmptyWithNullString() {
        final String str = null;
        assertTrue( "The null string must be detected as null or empty.", StringUtils.isNullOrEmpty( str ) );
        assertFalse( "The null string must be detected as not not null or empty.", StringUtils.isNotNullOrEmpty( str ) );
    }

    /**
     * Test is not null or empty with proper string.
     */
    @Test
    public void testIsNotNullOrEmptyWithProperString() {
        final String str = "abc";
        assertFalse( "The non empty string must be detected as not empty.", StringUtils.isNullOrEmpty( str ) );
        assertTrue( "The non empty string must be detected as not null or empty.", StringUtils.isNotNullOrEmpty( str ) );
    }

    /*
     * *************************************************************************
     * Test method isValidEmailAddress
     * *************************************************************************
     */

    /**
     * Verifies that a checker method should work properly with an invalid email address.
     */
    @Test
    public void testIsValidEmailAddressInValidEmail() {

        email = "abc@abc@gmail.com";
        final String emailWithoutAt = "abcyahoo.com";
        final String emailWithoutDotAfterAt = "abc@yahoocom";

        assertFalse( "Test for an in valid email " + email + " must detect as invalid.", StringUtils.isValidEmailAddress( email ) );
        assertFalse( "Test for an in valid email " + emailWithoutAt + " must detect as invalid.",
                StringUtils.isValidEmailAddress( emailWithoutAt ) );
        assertFalse( "Test for an in valid email " + emailWithoutDotAfterAt + " must detect as invalid.",
                StringUtils.isValidEmailAddress( emailWithoutDotAfterAt ) );

    }

    /**
     * Verifies that a checker method should work properly with a valid email address.
     */
    @Test
    public void testisValidEmailAddressProperEmail() {
        email = "abc@gmail.com";
        assertTrue( "Test for a valid email " + email + " must be detect valid.", StringUtils.isValidEmailAddress( email ) );
    }

    /*
     * *************************************************************************
     * Test method tokenizeStringByChar
     * *************************************************************************
     */

    /**
     * Verifies that a checker method should work properly and return an array with correct contents as expected.
     */
    @Test
    public void testTokenizeStringByCharWithArrayContents() {
        final String str = "abc.def.ghi";
        assertTrue( "Test for tokenizing non empty string " + str + " over character '.' must detect first element 'abc'",
                StringUtils.tokenizeStringByChar( str, "." )[ 0 ].equalsIgnoreCase( "abc" ) );
        assertTrue( "Test for tokenizing non empty string " + str + " over character '.' is valid if length equal to 3.",
                StringUtils.tokenizeStringByChar( str, "." ).length == 3 );
    }

    /**
     * Verifies that a checker method should work as expected with a null char for tokenizing and return an array of zero size.
     */
    @Test
    public void testTokenizeStringByCharWithCharNull() {
        final String str = "abc.def.ghi";
        final String str2 = null;
        assertTrue( StringUtils.tokenizeStringByChar( str, str2 ).length == 0 );
        assertTrue( StringUtils.tokenizeStringByChar( str2, str ).length == 0 );
    }

    /*
     * *************************************************************************
     * Test method validateFieldAndLength
     * *************************************************************************
     */

    /**
     * Verifies that a checker method should work as expected with an input greater than allowed size.
     */

    @Test
    public void testValidateFieldAndLengthWithMaxValue() {
        final Notification notif = StringUtils.validateFieldAndLength( "abcdefghijklmnop", NAME_FIELD_USED_IN_MESSAGE_PREPRATION, 10, false,
                false );
        if ( notif.hasErrors() ) {
            assertTrue( notif.getErrors().size() == 1 );
            assertTrue( "Large Value Check", notif.getErrors().get( 0 ).getMessage().contentEquals(
                    MessagesUtil.getMessage( WFEMessages.UTILS_VALUE_TOO_LARGE, NAME_FIELD_USED_IN_MESSAGE_PREPRATION, 10 ) ) );
        }
    }

    /**
     * Verifies that a checker method should show field as invalid with having null value.
     */
    @Test
    public void testValidateFieldAndLengthWithNullValue() {
        final Notification notif = StringUtils.validateFieldAndLength( null, NAME_FIELD_USED_IN_MESSAGE_PREPRATION, 100, false, false );
        if ( notif.hasErrors() ) {
            assertTrue( notif.getErrors().size() == 1 );
            assertTrue( "Null Field Check", notif.getErrors().get( 0 ).getMessage().contentEquals(
                    MessagesUtil.getMessage( WFEMessages.UTILS_NAME_CANT_BE_NULL, NAME_FIELD_USED_IN_MESSAGE_PREPRATION ) ) );
        }

    }

    /**
     * Verifies that a checker method should show input valid with valid length.
     */
    @Test
    public void testValidateFieldAndLengthWithProperValue() {
        final Notification notif = StringUtils.validateFieldAndLength( "abcdefghij", NAME_FIELD_USED_IN_MESSAGE_PREPRATION, 20, false,
                true );
        assertTrue( !notif.hasErrors() ); // Will never throw an exception in this case
    }

    /*
     * *************************************************************************
     * Test method validateSpecialCharacters
     * *************************************************************************
     */

    /**
     * Verifies that a checker method should work as expected with invalid input.
     */
    @Test
    public void testValidateSpecialCharactersWithInvalidString() {
        final String str = "$%&*^()_";
        Notification notif = new Notification();
        notif = StringUtils.validateFieldAndLength( str, NAME_FIELD_USED_IN_MESSAGE_PREPRATION, 20, false, true );
        if ( notif.hasErrors() ) {
            assertTrue( notif.getErrors().size() == 1 );
            assertTrue( "Special Characters Check", notif.getErrors().get( 0 ).getMessage().contentEquals(
                    MessagesUtil.getMessage( WFEMessages.UTILS_INVALID_SPECIAL_CHARS, str, StringUtils.ALLOWED_SPECIAL_CHARACTERS ) ) );
        }

    }

    /**
     * Verifies that a checker method should work as expected with valid input.
     */
    @Test
    public void testValidateAllowedSpecialCharactersInString() {

        Notification notif = new Notification();
        notif = StringUtils.validateFieldAndLength( VALID_SUS_STRING, NAME_FIELD_USED_IN_MESSAGE_PREPRATION, 20, false, true );
        assertTrue( !notif.hasErrors() );
    }

    /**
     * Verifies that a checker method should work as expected with valid input.
     */
    @Test
    public void testValidateForbiddenSpecialCharactersInString() {

        Notification notif = new Notification();
        notif = StringUtils.validateFieldAndLength( INVALID_SUS_STRING, NAME_FIELD_USED_IN_MESSAGE_PREPRATION, 20, false, true );
        assertTrue( notif.hasErrors() );
    }

    /**
     * Test validate UUID with empty string.
     */
    @Test
    public void testValidateUUIDWithEmptyString() {

        final boolean result = StringUtils.validateUUIDString( EMPTY_STRING );
        assertFalse( result );
    }

    /**
     * Test validate UUID with invalid string.
     */
    @Test
    public void testValidateUUIDWithInvalidString() {
        final String str = "10insgd";

        final boolean result = StringUtils.validateUUIDString( str );
        assertFalse( result );
    }

    /**
     * Test validate UUID with valid string.
     */
    @Test
    public void testValidateUUIDWithValidString() {
        final String str = UUID.randomUUID().toString();

        final boolean result = StringUtils.validateUUIDString( str );
        assertTrue( result );
    }

    /**
     * Test global naming convention when valid string is provided.
     */
    @Test
    public void testGlobalNamingConventionWhenValidStringIsProvided() {
        boolean actual = StringUtils.isGlobalNamingConventionFollowed( NAME_FIELD_USED_IN_MESSAGE_PREPRATION );
        assertFalse( actual );
    }

    /**
     * Test global naming convention when special charecters string is provided.
     */
    @Test
    public void testGlobalNamingConventionWhenSpecialCharectersStringIsProvided() {
        boolean actual = StringUtils.isGlobalNamingConventionFollowed( SPECIAL_CHARECTER );
        assertTrue( actual );
    }

}
