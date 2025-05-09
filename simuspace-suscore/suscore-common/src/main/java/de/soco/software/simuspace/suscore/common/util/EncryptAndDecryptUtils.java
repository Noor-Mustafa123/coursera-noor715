package de.soco.software.simuspace.suscore.common.util;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import org.apache.commons.fileupload.util.Streams;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.enums.EncrypDecrypEnums;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class EncryptAndDecryptUtils is automated Enc Dec class.
 *
 * @author noman arshad
 */
@Log4j2
public class EncryptAndDecryptUtils {

    /**
     * The Constant DESedeIVSpec.
     */
    private static final byte[] DESedeIVSpec = { -19, 30, -4, 45, -23, 67, 109, -60 };

    private static final String ENCRYPTION_STRING_NULL_ERROR = "the encrypt bytes string value is null: ";

    /**
     * Encrypt stream if encp enabled and save.
     *
     * @param inputStream
     *         the input stream
     * @param saveFileTo
     *         the save file to
     */
    public static void encryptStreamIfEncpEnabledAndSave( InputStream inputStream, File saveFileTo ) {
        try {
            if ( !PropertiesManager.getEncryptionConfigs().isActive() ) {
                FileOutputStream fos = new FileOutputStream( saveFileTo );
                Streams.copy( inputStream, fos, true );
            } else {
                Cipher cipher = initializeEncryptionCipher();
                FileOutputStream fileOutputStream = new FileOutputStream( saveFileTo );
                CipherOutputStream cipherOutStream = new CipherOutputStream( fileOutputStream, cipher );
                byte[] buffer = new byte[ 8192 ];
                int count;
                while ( ( count = inputStream.read( buffer ) ) > 0 ) {
                    cipherOutStream.write( buffer, 0, count );
                }
                cipherOutStream.flush();
                cipherOutStream.close();
                inputStream.close();
                fileOutputStream.close();
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Decrypt file if encp enabled and return stream.
     *
     * @param inputFile
     *         the input file
     * @param encDec
     *         the enc dec
     *
     * @return the input stream
     */
    public static InputStream decryptFileIfEncpEnabledAndReturnStream( File inputFile, EncryptionDecryptionDTO encDec ) {
        try {
            // return simple stream if Encryption is OFF
            if ( encDec == null ) {
                return fileIntoStream( inputFile );
            }
            Cipher cipher = initializeDecryptionCipher( encDec );
            FileInputStream inputStream = new FileInputStream( inputFile );
            CipherInputStream cipherInputStream = new CipherInputStream( inputStream, cipher );
            return cipherInputStream;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Encrypt stream if encp enabled and save.
     *
     * @param document
     *         the document
     * @param originalFile
     *         the original file
     * @param decryptedFilePath
     *         the decrypted file path
     *
     * @return the file
     */
    public static File decryptFileIfEncpEnabledAndSave( DocumentDTO document, File originalFile, String decryptedFilePath ) {
        try {
            FileInputStream inputStream = new FileInputStream( originalFile );
            if ( !document.isEncrypted() ) {
                FileOutputStream fos = new FileOutputStream( decryptedFilePath );
                Streams.copy( inputStream, fos, true );
            } else {
                Cipher cipher = initializeDecryptionCipher( document.getEncryptionDecryption() );
                FileOutputStream fileOutputStream = new FileOutputStream( decryptedFilePath );
                CipherOutputStream cipherOutStream = new CipherOutputStream( fileOutputStream, cipher );
                byte[] buffer = new byte[ 8192 ];
                int count;
                while ( ( count = inputStream.read( buffer ) ) > 0 ) {
                    cipherOutStream.write( buffer, 0, count );
                }
                cipherOutStream.flush();
                cipherOutStream.close();
                inputStream.close();
                fileOutputStream.close();
            }
            return new File( decryptedFilePath );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * File into stream.
     *
     * @param file
     *         the file
     *
     * @return the input stream
     */
    public static InputStream fileIntoStream( File file ) {
        try {
            return new FileInputStream( file );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Initialize encryption cipher.
     *
     * @return the cipher
     */
    private static Cipher initializeEncryptionCipher() {
        try {
            EncryptionDecryptionDTO encDyc = new EncryptionDecryptionDTO();
            for ( EncryptionDecryptionDTO encDycType : PropertiesManager.getEncryptionConfigs().getAvailableMethods() ) {
                if ( encDycType.isActive() ) {
                    encDyc = encDycType;
                }
            }
            EncrypDecrypEnums encrypDecrypEnums = EncrypDecrypEnums.getEnumByKey( encDyc.getMethod() );
            Key secretKey = new SecretKeySpec( encDyc.getSalt().getBytes(), encrypDecrypEnums.getKey() );
            Cipher cipher = Cipher.getInstance( encrypDecrypEnums.getValue() );
            if ( encDyc.getMethod().equalsIgnoreCase( EncrypDecrypEnums.DESede.getKey() ) ) {
                cipher.init( Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec( DESedeIVSpec ) );
            } else {
                cipher.init( Cipher.ENCRYPT_MODE, secretKey );
            }
            return cipher;
        } catch ( NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Initialize decryption cipher.
     *
     * @param encDec
     *         the enc dec
     *
     * @return the cipher
     */
    private static Cipher initializeDecryptionCipher( EncryptionDecryptionDTO encDec ) {
        try {
            EncrypDecrypEnums encrypDecrypEnums = EncrypDecrypEnums.getEnumByKey( encDec.getMethod() );
            Key secretKey = new SecretKeySpec( encDec.getSalt().getBytes(), encrypDecrypEnums.getKey() );
            Cipher cipher = Cipher.getInstance( encrypDecrypEnums.getValue() );
            if ( encDec.getMethod().equalsIgnoreCase( EncrypDecrypEnums.DESede.getKey() ) ) {
                cipher.init( Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec( DESedeIVSpec ) );
            } else {
                cipher.init( Cipher.DECRYPT_MODE, secretKey );
            }
            return cipher;
        } catch ( NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }



    //    METHOD TO ENCRYPT and DECRYPT SECRET KEY

    /**
     * Encrypt string string.
     *
     * @param plainText
     *         the plain text
     *
     * @return the string
     */
    public static String encryptString(String plainText) {

        try {
            Cipher cipher = encryptionCipherForStrings();
            byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
            // Encrypting the bytes using doFinal
            byte[] encryptedBytes = cipher.doFinal(plainBytes);
            if(encryptedBytes != null) {
                // converting it back into string format
               String encodedString =  Base64.getEncoder().encodeToString( encryptedBytes );
               System.out.println("encryptedString :" + encodedString);
//               testing if decryption is also working
                String decryptedString = decryptString( encodedString );
                System.out.println("DecryptedString :" + decryptedString);
                return encodedString;
            }
            else{
                throw new SusException(ENCRYPTION_STRING_NULL_ERROR + Arrays.toString( encryptedBytes ) );
            }


        } catch (Exception e) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Decrypt string string.
     *
     * @param encryptedText
     *         the encrypted text
     *
     * @return the string
     */
    public static String decryptString(String encryptedText) {
        EncryptionDecryptionDTO encDyc = null;
        try {
        // will always use AES for strings
           Cipher cipher = decryptionCipherForStrings();
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }




    private static Cipher encryptionCipherForStrings() {
        try {
//            Always gets the AES method for encrypting strings
            EncryptionDecryptionDTO encDyc = new EncryptionDecryptionDTO();
            for ( EncryptionDecryptionDTO encDycType : PropertiesManager.getEncryptionConfigs().getAvailableMethods() ) {
                if ( encDycType.getMethod().equalsIgnoreCase("AES") ) {
                    encDycType.setActive( true );
                    encDyc = encDycType;
                }
            }
            String enfff = encDyc.getMethod();
            EncrypDecrypEnums encrypDecrypEnums = EncrypDecrypEnums.getEnumByKey( encDyc.getMethod() );
            Key secretKey = new SecretKeySpec( encDyc.getSalt().getBytes(), encrypDecrypEnums.getKey() );
            Cipher cipher = Cipher.getInstance( encrypDecrypEnums.getValue() );
            if ( encDyc.getMethod().equalsIgnoreCase( EncrypDecrypEnums.DESede.getKey() ) ) {
                cipher.init( Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec( DESedeIVSpec ) );
            } else {
                cipher.init( Cipher.ENCRYPT_MODE, secretKey );
            }
            return cipher;
        } catch ( NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }





    private static Cipher decryptionCipherForStrings( ) {
        try {
//            gets the AES method
            EncryptionDecryptionDTO encDec = new EncryptionDecryptionDTO();
            for ( EncryptionDecryptionDTO encDycType : PropertiesManager.getEncryptionConfigs().getAvailableMethods() ) {
                if ( encDycType.getMethod().equalsIgnoreCase("AES") ) {
                    encDycType.setActive( true );
                    encDec = encDycType;
                }
            }
            EncrypDecrypEnums encrypDecrypEnums = EncrypDecrypEnums.getEnumByKey( encDec.getMethod() );
            Key secretKey = new SecretKeySpec( encDec.getSalt().getBytes(), encrypDecrypEnums.getKey() );
            Cipher cipher = Cipher.getInstance( encrypDecrypEnums.getValue() );
            if ( encDec.getMethod().equalsIgnoreCase( EncrypDecrypEnums.DESede.getKey() ) ) {
                cipher.init( Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec( DESedeIVSpec ) );
            } else {
                cipher.init( Cipher.DECRYPT_MODE, secretKey );
            }
            return cipher;
        } catch ( NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }






}
