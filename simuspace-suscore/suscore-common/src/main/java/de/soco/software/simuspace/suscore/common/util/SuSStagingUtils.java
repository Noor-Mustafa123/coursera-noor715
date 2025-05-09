package de.soco.software.simuspace.suscore.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.fileupload.util.Streams;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class SuSStagingUtils.
 */
@Log4j2
public class SuSStagingUtils {

    /**
     * Save in staging.
     *
     * @param stream
     *         the stream
     * @param filePath
     *         the file path
     * @param userUid
     *         the user uid
     *
     * @return the file
     */
    public static File saveInStaging( InputStream stream, String filePath, String userUid ) {
        File file = new File( filePath );
        File requiredFolder = new File( file.getParent() );

        if ( !requiredFolder.exists() ) {
            LinuxUtils.createDirectory( userUid, requiredFolder.getAbsolutePath() );
        }

        if ( OSValidator.isUnix() ) {
            // set permissions if it's not windows because windows
            // so far does not support posix.
            setPermissions( requiredFolder.getAbsolutePath() );
        }
        try ( FileOutputStream fos = new FileOutputStream( file ) ) {
            Streams.copy( stream, fos, true );
        } catch ( Exception e ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.STAGING_SERVICE_COULDNOT_WRITE.getKey() ), e );
        }

        return file;
    }

    /**
     * A util function to set app permissions to the document.
     *
     * @param folderPath
     *         the new permissions
     */
    private static boolean setPermissions( String folderPath ) {
        try {
            Set< PosixFilePermission > perms = new HashSet<>();
            perms.add( PosixFilePermission.OWNER_WRITE );
            perms.add( PosixFilePermission.OWNER_READ );
            perms.add( PosixFilePermission.OWNER_EXECUTE );
            perms.add( PosixFilePermission.GROUP_WRITE );
            perms.add( PosixFilePermission.GROUP_READ );
            perms.add( PosixFilePermission.GROUP_EXECUTE );
            perms.add( PosixFilePermission.OTHERS_WRITE );
            perms.add( PosixFilePermission.OTHERS_READ );
            perms.add( PosixFilePermission.OTHERS_EXECUTE );
            Files.setPosixFilePermissions( Paths.get( folderPath ), perms );
        } catch ( IOException e ) {
            return false;
        }
        return true;
    }

    /**
     * Instantiates a new su S staging utils.
     */
    private SuSStagingUtils() {

    }

    /**
     * Creates the user staging path dir.
     *
     * @param user
     *         the user
     */
    public static void createUserDirectoryInStaging( String userUid ) {
        if ( !userUid.equalsIgnoreCase( ConstantsString.SIMUSPACE ) && LinuxUtils.checkUserImpersonation( userUid ) ) {
            String dirPath = PropertiesManager.getStagingPath() + File.separator + userUid;
            LinuxUtils.createDirectory( userUid, dirPath );
            log.info( "User directory created in staging" );
        } else {
            log.warn( "Staging directory not created for user {}", userUid );
        }
    }

}
