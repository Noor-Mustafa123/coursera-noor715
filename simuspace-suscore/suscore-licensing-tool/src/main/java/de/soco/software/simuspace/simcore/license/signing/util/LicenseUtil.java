/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.simcore.license.signing.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.soco.software.simuspace.simcore.license.signing.model.LicenseDTO;
import de.soco.software.simuspace.simcore.license.signing.util.susexception.SusException;

/**
 * The Class is responsible to provide the utility functions regarding license signing.
 *
 * @author M.Nasir.Farooq
 */
public class LicenseUtil {

    /**
     * The Constant MESSAGE_LICENSE_FILE_PATH_NOT_NULL.
     */
    public static final String MESSAGE_LICENSE_FILE_PATH_NOT_NULL = "License input file path could not be null or empty";

    /**
     * The Constant PRIVATE_KEY_FACTOR_ALGORITH.
     */
    private static final String PRIVATE_KEY_FACTOR_ALGORITH = "RSA";

    /**
     * The Constant LICENSE_SIGNATURE_ALGORITHM.
     */
    private static final String LICENSE_SIGNATURE_ALGORITHM = "SHA512withRSA";

    /**
     * The Constant LICENSE_INPUT_CAN_NOT_BE_NULL_OR_EMPTY.
     */
    private static final String LICENSE_INPUT_CAN_NOT_BE_NULL_OR_EMPTY = "License input can not be null or empty.";

    /**
     * The Constant UNABLE_TO_FIND_OR_PARSE_LICENSE_JSON_FILE.
     */
    public static final String UNABLE_TO_FIND_OR_PARSE_LICENSE_JSON_FILE = "Unable to find or parse license json file.";

    /**
     * The Constant UNABLE_TO_PARSE_THE_JSON.
     */
    public static final String UNABLE_TO_PARSE_THE_JSON = "Unable to parse the json.";

    /**
     * The Constant MESSAGE_PRIVATE_KEY_FILE_PATH_NOT_NULL.
     */
    public static final String MESSAGE_PRIVATE_KEY_FILE_PATH_NOT_NULL = "License private key file path could not be null or empty";

    /**
     * The mapper.
     */
    private static ObjectMapper mapper = new ObjectMapper().configure( JsonParser.Feature.ALLOW_COMMENTS, true );

    /**
     * Sign license json file with provided private license key file.
     *
     * @param licenseJsonFile
     *         the license json file
     * @param privateKeyFile
     *         the private key file
     *
     * @return the license DTO
     */

    private LicenseUtil() {

    }

    public static LicenseDTO signLicense( String licenseJsonFile, String privateKeyFile ) {

        if ( StringUtils.isBlank( licenseJsonFile ) ) {
            throw new SusException( MESSAGE_LICENSE_FILE_PATH_NOT_NULL );
        } else if ( StringUtils.isBlank( privateKeyFile ) ) {
            throw new SusException( MESSAGE_PRIVATE_KEY_FILE_PATH_NOT_NULL );
        }

        LicenseDTO license = null;
        try ( InputStream licenseJsonFileStream = new FileInputStream( licenseJsonFile ) ) {
            license = LicenseUtil.jsonStreamToObject( licenseJsonFileStream, LicenseDTO.class );
        } catch ( IOException e ) {
            throw new SusException();
        }

        if ( license == null ) {
            throw new SusException( LICENSE_INPUT_CAN_NOT_BE_NULL_OR_EMPTY );
        }

        List< String > messages = license.validate();
        if ( !messages.isEmpty() ) {
            throw new SusException( messages.toString() );
        }

        StringBuilder licenseObjMap = license.nonEditableFields();

        try ( ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream( bos ) ) {
            out.writeObject( licenseObjMap );
            out.flush();
            byte[] licenseObjBytes = bos.toByteArray();
            Signature signer = Signature.getInstance( LICENSE_SIGNATURE_ALGORITHM );

            PrivateKey privateKey = getPrivateKey( read( privateKeyFile ) );
            signer.initSign( privateKey );

            signer.update( licenseObjBytes );
            byte[] signature = signer.sign();
            Base64 encoder = new Base64();
            String signatureStr = new String( encoder.encode( signature ) );
            license.setKeyInformation( signatureStr );

        } catch ( IOException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | InvalidKeySpecException e ) {
            throw new SusException( e.getMessage() );
        }

        return license;
    }

    /**
     * Converts the provided object to json string.
     *
     * @param obj
     *         the obj
     * @param filterClass
     *         the filter class
     *
     * @return the string
     */
    public static String toJson( Object obj, Class< ? > filterClass ) {
        String jsonObj = "";

        try {
            if ( null == obj ) {
                return null;
            }

            // mapper.getSerializationConfig().addMixInAnnotations( Object.class, filterClass );
            mapper.addMixInAnnotations( Object.class, filterClass );
            jsonObj = mapper.writeValueAsString( obj );
        } catch ( Exception e ) {
            throw new SusException( UNABLE_TO_PARSE_THE_JSON );
        }

        return jsonObj;
    }

    /**
     * Will convert Json stream to instance of given value type as parameters.
     *
     * @param <T>
     *         instance of given value type.
     * @param jsonStream
     *         the json stream
     * @param clazz
     *         the clazz
     *
     * @return the t
     *
     * @description method takes InputStream and Class name and return an Object of that class after parsing InputStream to that Object.
     */
    public static < T > T jsonStreamToObject( InputStream jsonStream, Class< T > clazz ) {
        T object = null;

        if ( jsonStream == null ) {
            return null;
        }

        try {
            object = mapper.readValue( jsonStream, clazz );
        } catch ( final Exception e ) {
            throw new SusException( UNABLE_TO_PARSE_THE_JSON );
        }

        return object;
    }

    /**
     * Gets the private key.
     *
     * @param a_byteArrPrivateKey
     *         the a byte arr private key
     *
     * @return the private key
     *
     * @throws InvalidKeySpecException
     *         the invalid key spec exception
     * @throws NoSuchAlgorithmException
     *         the no such algorithm exception
     */
    private static PrivateKey getPrivateKey( byte[] byteArrPrivateKey ) throws InvalidKeySpecException, NoSuchAlgorithmException {

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec( byteArrPrivateKey );
        KeyFactory keyFactory = KeyFactory.getInstance( PRIVATE_KEY_FACTOR_ALGORITH );
        return keyFactory.generatePrivate( privateKeySpec );

    }

    /**
     * Reads the file bytes array form path.
     *
     * @param path
     *         the path
     *
     * @return the byte[]
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static byte[] read( String path ) throws IOException {
        byte[] bb = null;
        try ( ByteArrayOutputStream buffer = new ByteArrayOutputStream(); FileInputStream fis = new FileInputStream( path ) ) {
            byte[] filebytes = new byte[ fis.available() ];
            int nRead;
            while ( ( nRead = fis.read( filebytes, 0, filebytes.length ) ) != -1 ) {
                buffer.write( filebytes, 0, nRead );
            }
            buffer.flush();
            bb = buffer.toByteArray();
        }
        return bb;
    }

}
