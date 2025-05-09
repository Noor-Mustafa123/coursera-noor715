package de.soco.software.simuspace.suscore.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.NetworkInterface;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class is responsible to provide utility functions for license.
 *
 * @author M.Nasir.Farooq
 */
public class LicenseUtils {

    /**
     * The Constant MAC_ADDRESS_SEARCH_STRING.
     */
    private static final String MAC_ADDRESS_SEARCH_STRING = "%02X%s";

    /**
     * The Constant WINDOWS_MAC_ADDRESS_SEPRATER.
     */
    private static final String WINDOWS_MAC_ADDRESS_SEPRATER = "-";

    /**
     * The Constant EMPTY_STRING.
     */
    private static final String EMPTY_STRING = "";

    /**
     * The Constant REGEX_TO_GET_MAC.
     */
    private static final String REGEX_TO_GET_MAC = "([\\w]{1,2}(-|:)){5}[\\w]{1,2}";

    /**
     * The Constant ALGORITHM_FOR_SIGNATURE.
     */
    private static final String ALGORITHM_FOR_SIGNATURE = "SHA512withRSA";

    /**
     * The Constant ALGORITHM_FOR_KEY_FACTOR.
     */
    private static final String ALGORITHM_FOR_KEY_FACTOR = "RSA";

    /**
     * Private constructor to hide the implicit public one.
     */
    private LicenseUtils() {
    }

    /**
     * Verify expiry.
     *
     * @param license
     *         the license
     *
     * @return false, if expired
     */
    public static boolean verifyLicenseExpiry( ModuleLicenseDTO license ) {
        return DateFormatStandard.checkIsPriorToNow( license.getExpiryTime() );
    }

    /**
     * Verify signature.
     *
     * @param license
     *         the license
     *
     * @return true, if successful
     */
    public static boolean verifyLicense( ModuleLicenseDTO license ) {

        if ( StringUtils.isBlank( license.getKeyInformation() ) ) {
            return false;
        }

        byte[] docBytesPlusSignatureFromFileSystem = null;
        StringBuilder licenseObjNonEditableFields = license.nonEditableFields();

        try ( ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream( bos ) ) {
            out.writeObject( licenseObjNonEditableFields );
            out.flush();
            docBytesPlusSignatureFromFileSystem = bos.toByteArray();

        } catch ( IOException e ) {
            ExceptionLogger.logException( e, LicenseUtils.class );
            return false;
        }

        byte[] publicKeySpec = { ( byte ) 0x30, ( byte ) 0x81, ( byte ) 0x9F, ( byte ) 0x30, ( byte ) 0x0D, ( byte ) 0x06, ( byte ) 0x09,
                ( byte ) 0x2A, ( byte ) 0x86, ( byte ) 0x48, ( byte ) 0x86, ( byte ) 0xF7, ( byte ) 0x0D, ( byte ) 0x01, ( byte ) 0x01,
                ( byte ) 0x01, ( byte ) 0x05, ( byte ) 0x00, ( byte ) 0x03, ( byte ) 0x81, ( byte ) 0x8D, ( byte ) 0x00, ( byte ) 0x30,
                ( byte ) 0x81, ( byte ) 0x89, ( byte ) 0x02, ( byte ) 0x81, ( byte ) 0x81, ( byte ) 0x00, ( byte ) 0xB1, ( byte ) 0xC0,
                ( byte ) 0x2F, ( byte ) 0x32, ( byte ) 0x46, ( byte ) 0x5F, ( byte ) 0x40, ( byte ) 0x68, ( byte ) 0x54, ( byte ) 0x9F,
                ( byte ) 0x67, ( byte ) 0xF8, ( byte ) 0xA1, ( byte ) 0xFB, ( byte ) 0x1D, ( byte ) 0xCF, ( byte ) 0x28, ( byte ) 0x17,
                ( byte ) 0x12, ( byte ) 0xAF, ( byte ) 0x9C, ( byte ) 0x84, ( byte ) 0x99, ( byte ) 0xCE, ( byte ) 0xE6, ( byte ) 0x70,
                ( byte ) 0xD9, ( byte ) 0xAE, ( byte ) 0xD9, ( byte ) 0xF0, ( byte ) 0xB5, ( byte ) 0x49, ( byte ) 0xD1, ( byte ) 0x65,
                ( byte ) 0x1B, ( byte ) 0xCB, ( byte ) 0x2F, ( byte ) 0x4A, ( byte ) 0x7C, ( byte ) 0x4B, ( byte ) 0x64, ( byte ) 0x94,
                ( byte ) 0xE6, ( byte ) 0xB4, ( byte ) 0x25, ( byte ) 0x41, ( byte ) 0xE6, ( byte ) 0xAC, ( byte ) 0x9B, ( byte ) 0xA4,
                ( byte ) 0xFA, ( byte ) 0xED, ( byte ) 0x9F, ( byte ) 0x26, ( byte ) 0xD9, ( byte ) 0xDA, ( byte ) 0xE7, ( byte ) 0x50,
                ( byte ) 0x7A, ( byte ) 0xA6, ( byte ) 0x63, ( byte ) 0x7B, ( byte ) 0x0E, ( byte ) 0x53, ( byte ) 0xFA, ( byte ) 0x60,
                ( byte ) 0xCE, ( byte ) 0x8A, ( byte ) 0xBA, ( byte ) 0xB1, ( byte ) 0xCE, ( byte ) 0x52, ( byte ) 0x61, ( byte ) 0xE5,
                ( byte ) 0x19, ( byte ) 0x66, ( byte ) 0x9A, ( byte ) 0x8A, ( byte ) 0x2D, ( byte ) 0x94, ( byte ) 0x8C, ( byte ) 0x53,
                ( byte ) 0x0A, ( byte ) 0xA5, ( byte ) 0x2D, ( byte ) 0xB2, ( byte ) 0x09, ( byte ) 0x81, ( byte ) 0x04, ( byte ) 0x7B,
                ( byte ) 0x1C, ( byte ) 0xC7, ( byte ) 0x79, ( byte ) 0xB2, ( byte ) 0xA7, ( byte ) 0x3B, ( byte ) 0x33, ( byte ) 0xFE,
                ( byte ) 0x53, ( byte ) 0x94, ( byte ) 0x51, ( byte ) 0x4B, ( byte ) 0x06, ( byte ) 0x98, ( byte ) 0xBB, ( byte ) 0x2B,
                ( byte ) 0x42, ( byte ) 0x9C, ( byte ) 0x8A, ( byte ) 0xDE, ( byte ) 0xAA, ( byte ) 0x87, ( byte ) 0x13, ( byte ) 0x41,
                ( byte ) 0x6A, ( byte ) 0xF5, ( byte ) 0xAA, ( byte ) 0x0E, ( byte ) 0x00, ( byte ) 0x50, ( byte ) 0x74, ( byte ) 0x92,
                ( byte ) 0xBC, ( byte ) 0x33, ( byte ) 0x4C, ( byte ) 0x7F, ( byte ) 0xCA, ( byte ) 0x15, ( byte ) 0x02, ( byte ) 0x03,
                ( byte ) 0x01, ( byte ) 0x00, ( byte ) 0x01

        };

        boolean signatureIsValid = false;
        try {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec( publicKeySpec );
            KeyFactory keyFactory = KeyFactory.getInstance( ALGORITHM_FOR_KEY_FACTOR );

            PublicKey publicKey = keyFactory.generatePublic( pubKeySpec );

            Signature verifier = Signature.getInstance( ALGORITHM_FOR_SIGNATURE );
            verifier.initVerify( publicKey );
            verifier.update( docBytesPlusSignatureFromFileSystem );
            byte[] bytesSign = license.getKeyInformation().getBytes();
            Base64 decoder = new Base64();
            byte[] byteArrSignature = decoder.decode( bytesSign );
            signatureIsValid = verifier.verify( byteArrSignature );
        } catch ( NoSuchAlgorithmException | SignatureException | InvalidKeyException | InvalidKeySpecException e ) {
            ExceptionLogger.logException( e, LicenseUtils.class );
            return false;
        }

        return signatureIsValid;
    }

    /**
     * Verify mac address.
     *
     * @param macAddress
     *         the mac address
     *
     * @return true, if successful
     */
    public static boolean verifyMacAddress( String macAddress ) {
        boolean match = false;
        List< String > systemMac = getMachineMac();
        if ( systemMac != null ) {
            for ( String mac : systemMac ) {
                if ( mac.equalsIgnoreCase( macAddress ) ) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }

    /**
     * Gets the mac address of machine.
     *
     * @return the machine's mac
     */
    private static List< String > getMachineMac() {
        List< String > macAddress = new ArrayList<>();
        String command = PropertiesManager.getIfConfigPath();
        Process pid = null;
        try {
            pid = Runtime.getRuntime().exec( command );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, LicenseUtils.class );
            throw new SusException( MessageBundleFactory.getMessage( Messages.MAC_ADDRESS_CONFIG_ERROR.getKey() ) + e.getMessage() );
        }
        try ( InputStreamReader inputStream = new InputStreamReader( pid.getInputStream() );
                BufferedReader in = new BufferedReader( inputStream ) ) {
            Pattern p = Pattern.compile( REGEX_TO_GET_MAC );
            while ( true ) {
                String line = in.readLine();
                if ( line == null ) {
                    break;
                }
                Matcher m = p.matcher( line );
                if ( m.find() ) {
                    macAddress.add( m.group() );
                }
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, LicenseUtils.class );
        }
        return macAddress;
    }

    /**
     * Gets the windows mac address.
     *
     * @return the windows mac address
     */
    private static List< String > getWindowsMacAddress() {
        List< String > macAddress = new ArrayList<>();
        try {
            Enumeration< NetworkInterface > networks = NetworkInterface.getNetworkInterfaces();
            while ( networks.hasMoreElements() ) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();
                if ( mac != null ) {
                    StringBuilder sb = new StringBuilder();
                    for ( int i = 0; i < mac.length; i++ ) {
                        sb.append( String.format( MAC_ADDRESS_SEARCH_STRING, mac[ i ],
                                ( i < mac.length - 1 ) ? WINDOWS_MAC_ADDRESS_SEPRATER : EMPTY_STRING ) );
                    }
                    macAddress.add( sb.toString() );
                }
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, LicenseUtils.class );
        }
        return macAddress;
    }

}
