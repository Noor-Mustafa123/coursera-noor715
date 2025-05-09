package de.soco.software.simuspace.suscore.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.util.Streams;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.FileAlreadyExistsException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * An implementation class responsible for vault actions
 *
 * @author Ali Haider
 */
public class SuSVaultUtils {

    /**
     * {@inheritDoc}
     */
    public static File saveInVault( InputStream stream, String vaultPath, String hex, String encodedFileName ) {
        String folderPath = vaultPath + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_TWO );
        return saveInVault( stream, encodedFileName, folderPath );
    }

    /**
     * Save in vault.
     *
     * @param stream
     *         the stream
     * @param encodedFileName
     *         the encoded file name
     * @param folderPath
     *         the folder path
     *
     * @return the file
     */
    public static File saveInVault( InputStream stream, String encodedFileName, String folderPath ) {
        File file = new File( ConstantsString.EMPTY_STRING );
        File requiredFolder = Paths.get( folderPath ).toFile();
        if ( requiredFolder.exists() || requiredFolder.mkdirs() ) {
            if ( !OSValidator.isWindows() ) {
                try {
                    // set permissions if it's not windows because windows
                    // so far does not support posix.
                    setPermissions( requiredFolder.getAbsolutePath() );
                } catch ( Exception e ) {
                    ExceptionLogger.logException( e, SuSVaultUtils.class );
                }
            }
            file = new File( requiredFolder, encodedFileName );
            if ( file.exists() ) {
                throw new FileAlreadyExistsException( MessageBundleFactory.getMessage( Messages.FILE_ALREADY_EXIST_ON_SERVER.getKey() ) );
            }

            try {
                // encp stram if Encp is enabled otherwise save as it is.
                EncryptAndDecryptUtils.encryptStreamIfEncpEnabledAndSave( stream, file );
            } catch ( Exception e ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.VAULT_SERVICE_COULDNOT_WRITE.getKey() ), e );
            }
        }
        return file;
    }

    /**
     * Save in remote vault without encryption check.
     *
     * @param stream
     *         the stream
     * @param encodedFileName
     *         the encoded file name
     * @param folderPath
     *         the folder path
     *
     * @return the file
     */
    public static File saveInRemoteVaultWithoutEncryptionCheck( InputStream stream, String encodedFileName, String folderPath ) {
        File file = new File( ConstantsString.EMPTY_STRING );
        File requiredFolder = Paths.get( folderPath ).toFile();
        if ( requiredFolder.exists() || requiredFolder.mkdirs() ) {
            if ( !OSValidator.isWindows() ) {
                try {
                    // set permissions if it's not windows because windows
                    // so far does not support posix.
                    setPermissions( requiredFolder.getAbsolutePath() );
                } catch ( Exception e ) {
                    ExceptionLogger.logException( e, SuSVaultUtils.class );
                }
            }
            file = new File( requiredFolder, encodedFileName );
            if ( file.exists() ) {
                throw new FileAlreadyExistsException( MessageBundleFactory.getMessage( Messages.FILE_ALREADY_EXIST_ON_SERVER.getKey() ) );
            }

            try {
                FileOutputStream fos = new FileOutputStream( file );
                Streams.copy( stream, fos, true );
            } catch ( Exception e ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.VAULT_SERVICE_COULDNOT_WRITE.getKey() ), e );
            }
        }
        return file;
    }

    public static InputStream getDecryptionSteamFromPath( UUID documentId, int versionId, String path, EncryptionDecryptionDTO encDec ) {
        File file = getEncrypedFileFromPathIfEncpEnabled( documentId, versionId, path );

        return EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( file, encDec );
    }

    public static File getEncrypedFileFromPathIfEncpEnabled( UUID documentId, int versionId, String path ) {
        String hex = CommonUtils.getHex( documentId, versionId );
        File file = new File(
                path + File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_TWO )
                        + File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_TWO ) );
        if ( !file.exists() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey() ) );
        }
        return file;
    }

    /**
     * {@inheritDoc}
     */
    public static boolean deleteFileFromVault( UUID documentId, int versionId, String vaultPath ) {
        String hex = DigestUtils.shaHex( documentId + ConstantsString.COLON + versionId );
        Path path = Paths
                .get( vaultPath + File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_TWO )
                        + File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_TWO ) );
        try {
            Files.delete( path );
            return true;
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, SuSVaultUtils.class );
        }

        return false;
    }

    /**
     * A util function to set app permissions to the document
     *
     * @param folderPath
     *         the folderPath
     *
     * @throws IOException
     *         in case of failure
     */
    private static void setPermissions( String folderPath ) throws IOException {
        Set< PosixFilePermission > perms = new HashSet<>();
        // add owners permission
        perms.add( PosixFilePermission.OWNER_READ );
        perms.add( PosixFilePermission.OWNER_WRITE );
        perms.add( PosixFilePermission.OWNER_EXECUTE );
        // add group permissions
        perms.add( PosixFilePermission.GROUP_READ );
        perms.add( PosixFilePermission.GROUP_EXECUTE );
        // add others permissions
        perms.add( PosixFilePermission.OTHERS_READ );
        perms.add( PosixFilePermission.OTHERS_EXECUTE );
        Files.setPosixFilePermissions( Paths.get( folderPath ), perms );
    }

    public static void copyFileFromTmpToVault( String Path ) throws IOException {
        String sourcePath = PropertiesManager.getDefaultServerTempPath() + Path;
        String destinationPath = PropertiesManager.getVaultPath() + Path;
        try ( InputStream is = new FileInputStream( sourcePath ); OutputStream os = new FileOutputStream( destinationPath ) ) {
            byte[] buffer = new byte[ 1024 ];
            int length;
            while ( ( length = is.read( buffer ) ) > 0 ) {
                os.write( buffer, 0, length );
            }
        }
    }

}
