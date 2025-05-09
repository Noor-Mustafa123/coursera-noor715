package de.soco.software.simuspace.simcore.license.signing.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.simcore.license.signing.model.LicenseDTO;

/**
 * The Class is responsible to provide test public function of {@link LicenseUtil}.
 *
 * @author M.Nasir.Farooq
 */
public class LicenseUtilTest {

    /**
     * The Constant LICENSE_CONFIG_FILE_PATH.
     */
    private static final String INVALID_LICENSE_CONFIG_FILE_PATH = "src/test/resources/invalid-license.json";

    /**
     * The Constant LICENSE_CONFIG_FILE_PATH.
     */
    private static final String LICENSE_JSON_FILE_PATH = "src/test/resources/license.json";

    /**
     * The Constant LICENSE_PRIVATE_KEY_FILE_PATH.
     */
    private static final String LICENSE_PRIVATE_KEY_FILE_PATH = "src/test/resources/private.key";

    /**
     * The Constant NULL_LICENSE_FILE_PATH.
     */
    private static final String NULL_LICENSE_FILE_PATH = null;

    /**
     * The Constant EMPTY_LICENSE_FILE_PATH.
     */
    private static final String EMPTY_LICENSE_FILE_PATH = "";

    /**
     * The Constant NULL_PRIVATE_KEY_FILE_PATH.
     */
    private static final String NULL_PRIVATE_KEY_FILE_PATH = null;

    /**
     * The Constant EMPTY_PRIVATE_KEY_FILE_PATH.
     */
    private static final String EMPTY_PRIVATE_KEY_FILE_PATH = "";

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Should throw exception when json is not valid.
     */
    @Test
    public void shouldThrowExceptionWhenJsonIsNotValid() {
        thrown.expect( Exception.class );
        thrown.expectMessage( LicenseUtil.UNABLE_TO_PARSE_THE_JSON );
        LicenseUtil.signLicense( INVALID_LICENSE_CONFIG_FILE_PATH, LICENSE_PRIVATE_KEY_FILE_PATH );
    }

    /**
     * Should throw exception when license file path is null.
     */
    @Test
    public void shouldThrowExceptionWhenLicenseFilePathIsNull() {
        thrown.expect( Exception.class );
        thrown.expectMessage( LicenseUtil.MESSAGE_LICENSE_FILE_PATH_NOT_NULL );
        LicenseUtil.signLicense( NULL_LICENSE_FILE_PATH, LICENSE_PRIVATE_KEY_FILE_PATH );
    }

    /**
     * Should throw exception when license file path is empty.
     */
    @Test
    public void shouldThrowExceptionWhenLicenseFilePathIsEmpty() {
        thrown.expect( Exception.class );
        thrown.expectMessage( LicenseUtil.MESSAGE_LICENSE_FILE_PATH_NOT_NULL );
        LicenseUtil.signLicense( EMPTY_LICENSE_FILE_PATH, LICENSE_PRIVATE_KEY_FILE_PATH );
    }

    /**
     * Should throw exception when private key file path is null.
     */
    @Test
    public void shouldThrowExceptionWhenPrivateKeyFilePathIsNull() {
        thrown.expect( Exception.class );
        thrown.expectMessage( LicenseUtil.MESSAGE_PRIVATE_KEY_FILE_PATH_NOT_NULL );
        LicenseUtil.signLicense( LICENSE_JSON_FILE_PATH, NULL_PRIVATE_KEY_FILE_PATH );
    }

    /**
     * Should throw exception when private key file path is empty.
     */
    @Test
    public void shouldThrowExceptionWhenPrivateKeyFilePathIsEmpty() {
        thrown.expect( Exception.class );
        thrown.expectMessage( LicenseUtil.MESSAGE_PRIVATE_KEY_FILE_PATH_NOT_NULL );
        LicenseUtil.signLicense( LICENSE_JSON_FILE_PATH, EMPTY_PRIVATE_KEY_FILE_PATH );
    }

    /**
     * Should sign license when valid input provided.
     *
     * @throws FileNotFoundException
     */
    @Test
    public void shouldSignLicenseWhenValidInputProvided() throws FileNotFoundException {
        LicenseDTO actual = LicenseUtil.jsonStreamToObject( new FileInputStream( new File( LICENSE_JSON_FILE_PATH ) ), LicenseDTO.class );
        Assert.assertNull( actual.getKeyInformation() );

        LicenseDTO expected = LicenseUtil.signLicense( LICENSE_JSON_FILE_PATH, LICENSE_PRIVATE_KEY_FILE_PATH );
        Assert.assertNotNull( expected );
        Assert.assertNotNull( expected.getKeyInformation() );

        actual.setKeyInformation( expected.getKeyInformation() );
        Assert.assertEquals( expected, actual );
    }

}
