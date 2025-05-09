package de.soco.software.simuspace.suscore.interceptors.utils;

import java.security.MessageDigest;
import java.util.Formatter;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;

/**
 * A utility Class CipherUtils that holds the methods for Encryption and Decryption .
 *
 * @author Shan Arshad
 * @since 1.0
 */
public class CipherUtils {

    /**
     * The constant ENCRYPTION_ALGORITHM_SHA1
     */
    private static final String ENCRYPTION_ALGORITHM_SHA1 = "SHA-1";

    /**
     * The constant ENCRYPTION_ALGORITHM_SHA1
     */
    private static final String CHARACTER_CODING_UTF8 = "UTF-8";

    /**
     * The constant PRECISION_OF_TWO
     */
    private static final String PRECISION_OF_TWO = "%02x";

    /**
     * Encrypt using SHA-1.
     *
     * @param srcString
     *         the srcString
     *
     * @return the string
     */
    public static String encryptSHA1( final String srcString ) {
        String sha1 = ConstantsString.EMPTY_STRING;
        try {
            MessageDigest crypt = MessageDigest.getInstance( ENCRYPTION_ALGORITHM_SHA1 );
            crypt.reset();
            crypt.update( srcString.getBytes( CHARACTER_CODING_UTF8 ) );
            sha1 = byteToHex( crypt.digest() );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, e.getClass() );
        }
        return sha1;
    }

    /**
     * Byte to hex.
     *
     * @param hash
     *         the hash
     *
     * @return the string
     */
    private static String byteToHex( final byte[] hash ) {
        Formatter formatter = new Formatter();
        for ( byte b : hash ) {
            formatter.format( PRECISION_OF_TWO, b );
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}// EOC
