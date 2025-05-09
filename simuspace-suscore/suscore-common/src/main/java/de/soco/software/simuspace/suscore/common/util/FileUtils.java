package de.soco.software.simuspace.suscore.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantSuscoreApi;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.http.model.AcknowledgeBytes;
import de.soco.software.simuspace.suscore.common.http.model.HttpReceiver;
import de.soco.software.simuspace.suscore.common.http.model.HttpSender;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.model.FileObject;
import de.soco.software.simuspace.suscore.common.model.FileVisitor;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class is responsible for providing utility functions for files.
 *
 * @author Nasir.Farooq
 */
@Log4j2
public class FileUtils {

    /**
     * The Constant WINDOWS_ENCODING.
     */
    private static final String WINDOWS_ENCODING = "windows-1252";

    /**
     * The Constant FILE_IS_SYNCED.
     */
    private static final String FILE_IS_SYNCED = "File is synced";

    /**
     * The Constant END_STRING_OF_JSON.
     */
    private static final String END_STRING_OF_JSON = "\"}";

    /**
     * The Constant FILE_KEY_IN_JSON.
     */
    private static final String FILE_KEY_IN_JSON = "{\"file\":\"";

    /**
     * The Constant FIRST_FOUR_BYTES_OF_A_ZIP.
     */
    private static final int FIRST_FOUR_BYTES_OF_A_ZIP = 0x504b0304;

    /**
     * The Constant MINIMUM_NUMBER_OF_BYTES_A_ZIP_SHOULD_HAVE.
     */
    private static final int MINIMUM_NUMBER_OF_BYTES_A_ZIP_SHOULD_HAVE = 4;

    /**
     * The Constant Encoding UTF8.
     */
    public static final String UTF8_ENCODING = "UTF-8";

    /**
     * The ConstantEncoding for window.
     */
    private static final String CP_ENCODE = "cp437";

    /**
     * The Constant FILE.
     */
    private static final String FILE = "file";

    /**
     * The file is opened in a read-write mode. Any modifications to the file's content are written to the storage device immediately.
     */
    private static final String RW = "rw";

    /**
     * The Constant FILE_SEP.
     */
    private static final String FILE_SEP = "fileSeparator";

    /**
     * The Constant CHECKSUM.
     */
    private static final String CHECKSUM = "checkSum";

    /**
     * The Constant READ_MODE.
     */
    private static final String READ_MODE = "r";

    /**
     * The Constant INDEX_POSITION.
     */
    private static final long INDEX_POSITION = 0;

    /**
     * The Constant DEFAULT_CHUNK. // 65 Kilobytes
     */
    private static final long DEFAULT_CHUNK = 65536;

    /**
     * The Constant EMPTY_DIRECTORY.
     */
    private static final String EMPTY_DIRECTORY = "empty directory";

    /**
     * The Constant SHA2.
     */
    private static final String SHA2 = "SHA-256";

    /**
     * Instantiates a new file utils.
     */
    private FileUtils() {
        super();
    }

    /**
     * Extract zip file.
     *
     * @param zipFilePath
     *         the zip file path
     * @param destinationPath
     *         the destination path
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws SusException
     *         the sus exception
     * @throws IllegalArgumentException
     *         the illegal argument exception
     */
    public static boolean extractZipFile( String zipFilePath, String destinationPath ) throws IOException {
        if ( StringUtils.isBlank( zipFilePath ) || StringUtils.isBlank( destinationPath ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_ZIP_PATHS.getKey() ) );
        }
        zipFilePath = OSValidator.convertPathToRelitiveOS( zipFilePath );
        if ( !isZipFile( zipFilePath ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_ZIP.getKey(), zipFilePath ) );
        }

        final File zipFile = new File( zipFilePath );
        if ( zipFile.setReadable( true, false ) ) {
            /*
             * Check whether the provided file is zip file.
             */

            /*
             * Ordered List of all encodings supported in system.
             */
            final String[] encodings = { UTF8_ENCODING, CP_ENCODE };

            /**
             * Try to unzip file with all encodings in sequence. Because if zip file contains German Umlauts then entries of the zip files
             * can't be translated.
             */

            final File destinationDirectory = new File( destinationPath );

            for ( final String encoding : encodings ) {
                extractFolder( zipFile, destinationDirectory, Charset.forName( encoding ) );
                // Break the loop if zip file is extracted successfully
            }
            return true;

        } else {
            return false;
        }
    }

    /**
     * Zip files.
     *
     * @param files
     *         the list of files to zip
     * @param zipPath
     *         the zip path
     */
    public static void zipFiles( List< String > files, String zipPath ) {

        try ( FileOutputStream fos = new FileOutputStream( zipPath ); BufferedOutputStream bos = new BufferedOutputStream(
                fos ); ZipOutputStream zipOut = new ZipOutputStream( bos ) ) {
            for ( String filePath : files ) {
                File input = new File( filePath );
                writeFileToZip( input, zipOut );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * writes file to zip.
     *
     * @param input
     *         the input file
     * @param zipOut
     *         the zip out
     */
    private static void writeFileToZip( File input, ZipOutputStream zipOut ) {
        try ( FileInputStream fis = new FileInputStream( input ) ) {
            ZipEntry ze = new ZipEntry( input.getName() );
            zipOut.putNextEntry( ze );
            byte[] tmp = new byte[ 4 * 1024 ];
            int size = 0;
            while ( ( size = fis.read( tmp ) ) != -1 ) {
                zipOut.write( tmp, 0, size );
            }
            zipOut.flush();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * zip directories.
     *
     * @param zipFileName
     *         the zip File Name
     * @param dirList
     *         the dir list
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static void zipDirectories( String zipFileName, Set< String > dirList ) throws IOException {
        ZipOutputStream out = new ZipOutputStream( new FileOutputStream( zipFileName ) );

        for ( String dir : dirList ) {
            addDirectoryToZip( new File( dir ), out );
        }

        out.close();
    }

    /**
     * add directory to zip.
     *
     * @param dirObj
     *         the dir Obj
     * @param out
     *         the out
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static void addDirectoryToZip( File dirObj, ZipOutputStream out ) throws IOException {
        File[] files = dirObj.listFiles();
        byte[] tmpBuf = new byte[ 1024 ];

        for ( File file : files ) {
            if ( file.isDirectory() ) {
                addDirectoryToZip( file, out );
                continue;
            }

            try ( FileInputStream in = new FileInputStream( file.getAbsolutePath() ) ) {
                out.putNextEntry( new ZipEntry( file.getAbsolutePath() ) );
                int len;
                while ( ( len = in.read( tmpBuf ) ) > 0 ) {
                    out.write( tmpBuf, 0, len );
                }
                out.closeEntry();
            }
        }
    }

    /**
     * zip files and directories to zipfile.
     *
     * @param zipPath
     *         the zip path
     * @param files
     *         the files
     * @param dirList
     *         the dir list
     */
    public static void zipFilesAndDirectories( String zipPath, Set< String > files, Set< String > dirList ) {
        try ( ZipOutputStream zipOut = new ZipOutputStream( new FileOutputStream( zipPath ) ) ) {

            if ( dirList != null && !dirList.isEmpty() ) {
                for ( String dir : dirList ) {
                    addDirectoryToZip( new File( dir ), zipOut );
                }
            }
            if ( files != null && !files.isEmpty() ) {
                for ( String filePath : files ) {
                    File input = new File( filePath );
                    writeFileToZip( input, zipOut );
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Deletes file.
     *
     * @param path
     *         the file path
     *
     * @return true, if deleted successfully
     */
    public static boolean deleteFile( String path ) {
        File file = new File( path );
        return file.delete();
    }

    /**
     * Delete if exists.
     *
     * @param path
     *         the path
     */
    public static void deleteIfExists( String path ) {
        deleteIfExists( Path.of( path ) );
    }

    /**
     * Delete if exists.
     *
     * @param path
     *         the path
     */
    public static void deleteIfExists( Path path ) {
        try {
            Files.deleteIfExists( path );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_REMOVED.getKey(), path.toAbsolutePath() ) );
        }
    }

    /**
     * Checks if is zip file.
     *
     * @param filePath
     *         the file path
     *
     * @return true, if is zip file
     */
    public static boolean isZipFile( String filePath ) {
        final File file = new File( filePath );
        if ( !file.exists() ) {
            throw new SusException( MessagesUtil.getMessage( WFEMessages.FILE_PATH_NOT_EXIST, file.getAbsolutePath() ) );
        }
        if ( file.isDirectory() ) {
            return false;
        }
        if ( file.length() < MINIMUM_NUMBER_OF_BYTES_A_ZIP_SHOULD_HAVE ) {
            return false;
        }

        try ( FileInputStream fis = new FileInputStream( file ); BufferedInputStream bis = new BufferedInputStream(
                fis ); final DataInputStream in = new DataInputStream( bis ) ) {
            final int test = in.readInt();
            return test == FIRST_FOUR_BYTES_OF_A_ZIP;
        } catch ( final IOException e ) {
            log.error( e.getMessage(), e );
        }
        return false;
    }

    /**
     * Extract folder.
     *
     * @param zipFile
     *         zipFile to unzip
     * @param destinationDirectory
     *         the destination directory
     * @param encoding
     *         Encoding to use while unzipping entries of zip file. Note : This encoding will be used to translate the file paths
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws IllegalArgumentException
     *         the illegal argument exception
     */
    private static boolean extractFolder( File zipFile, File destinationDirectory, Charset encoding ) throws IOException {

        try ( final ZipFile zip = new ZipFile( zipFile, encoding ) ) {

            final Enumeration< ? > zipFileEntries = zip.entries();

            // Process each entry
            while ( zipFileEntries.hasMoreElements() ) {
                // grab a zip file entry
                // ZipEntry.nextElement() may throw IllegalArgumentException if
                // it fails to decode file path with given encoding.
                final ZipEntry entry = ( ZipEntry ) zipFileEntries.nextElement();
                final String currentEntry = entry.getName();

                final File destFile = new File( destinationDirectory.getAbsolutePath(), currentEntry );
                final File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs();

                writeFilesInDirectories( entry, zip, destFile );

            }
            // file extracted successfully
            return true;
        } catch ( final java.lang.IllegalArgumentException e ) {
            log.error( e.getMessage(), e );
            // catch java.lang.IllegalArgumentException and rethrow using
            // simcore IllegalArgumentException
            throw new SusException( e, FileUtils.class );
        }
    }

    /**
     * Write files in directories.
     *
     * @param entry
     *         the entry
     * @param zip
     *         the zip
     * @param destFile
     *         the dest file
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static void writeFilesInDirectories( ZipEntry entry, ZipFile zip, File destFile ) throws IOException {
        final int buffer = 2048;
        if ( ( null != entry ) && !entry.isDirectory() && ( null != zip ) && ( null != destFile ) ) {
            try ( final BufferedInputStream is = new BufferedInputStream(
                    zip.getInputStream( entry ) ); final FileOutputStream fos = new FileOutputStream(
                    destFile ); BufferedOutputStream dest = new BufferedOutputStream( fos, buffer ) ) {
                int currentByte;
                // establish buffer for writing file
                final byte[] data = new byte[ buffer ];
                while ( ( currentByte = is.read( data, 0, buffer ) ) != -1 ) {
                    dest.write( data, 0, currentByte );
                }
                dest.flush();
            }
        }
    }

    public static String getFileFirstRequiredCharContent( String file, int requiredCharSize ) {
        StringBuilder resultBuilder = null;
        try ( Reader r = new BufferedReader( new InputStreamReader( new FileInputStream( file ) ) ) ) {
            resultBuilder = new StringBuilder();
            int count = 0;
            int intch;
            while ( ( ( intch = r.read() ) != -1 ) && count <= requiredCharSize ) {
                resultBuilder.append( ( char ) intch );
                count++;
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getLocalizedMessage() );
        }
        return resultBuilder.toString();
    }

    /**
     * Find and return file.
     *
     * @param name
     *         the name
     * @param root
     *         the root
     *
     * @return the file
     */
    public static File findAndReturnFile( String name, File root ) {
        try {
            boolean recursive = true;
            Collection< ? > files = org.apache.commons.io.FileUtils.listFiles( root, null, recursive );
            for ( Object o : files ) {
                File file = ( File ) o;
                if ( file.getName().equals( name ) ) {
                    return file;
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return null;
    }

    /**
     * To Get all the folders list in directory.
     *
     * @param srcDir
     *         the src dir
     *
     * @return the directory with in folder
     */
    public static List< File > getDirectoryWithInFolder( File srcDir ) {

        List< File > files = new ArrayList<>();

        if ( srcDir != null ) {
            for ( File file : srcDir.listFiles() ) {
                if ( file.isDirectory() ) {
                    files.add( file );
                }
            }
        }
        return files;

    }

    /**
     * Gets the directory files with check sum.
     *
     * @param directory
     *         the directory
     * @param rootDir
     *         the root dir
     *
     * @return the directory files with check sum
     */
    public static Map< String, Map< String, String > > getDirectoryFilesWithCheckSum( String directory, String rootDir ) {
        Map< String, Map< String, String > > directoryFiles = new HashMap<>();
        listfileContent( directory, directoryFiles, rootDir );
        return directoryFiles;
    }

    /**
     * Convert byte array to base 64 string.
     *
     * @param fileBytes
     *         the file bytes
     *
     * @return the string
     */
    public static String convertByteArrayToBase64String( byte[] fileBytes ) {
        if ( fileBytes == null || fileBytes.length == 0 ) {
            return "";
        }
        return Base64.getEncoder().encodeToString( fileBytes );
    }

    /**
     * Convert base 64 string to byte array.
     *
     * @param fileString
     *         the file string
     *
     * @return the byte[]
     */
    public static byte[] convertBase64StringToByteArray( String fileString ) {
        try {
            return Base64.getDecoder().decode( fileString );
        } catch ( SusException e ) {
            log.error( "Invalid image string provided.", e );
            throw new SusException( "Invalid image string provided." );
        }
    }

    /**
     * Listfile content.
     *
     * @param directoryName
     *         the directory name
     * @param directoryFiles
     *         the directory files
     * @param rootDir
     *         the root dir
     */
    private static void listfileContent( String directoryName, Map< String, Map< String, String > > directoryFiles, String rootDir ) {

        File directory = null;
        try {
            directory = new File( directoryName ).getCanonicalFile();
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), directoryName ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), directoryName ) );
        }
        // get all the files from a directory
        File[] files = directory.listFiles();
        Map< String, String > fileNameAndContents = new HashMap<>();
        for ( File file : files ) {
            if ( file.isDirectory() ) {
                File[] emptyDir = file.listFiles();
                if ( emptyDir.length == INDEX_POSITION ) {
                    fileNameAndContents.put( file.getAbsolutePath(), EMPTY_DIRECTORY );
                    directoryFiles.put( directoryName, fileNameAndContents );
                }
                listfileContent( file.getAbsolutePath(), directoryFiles, rootDir );
            } else if ( file.isFile() ) {
                Map< String, String > map = fileSignatureForVaultFile( file );
                fileNameAndContents.put( file.getAbsolutePath(), map.get( CHECKSUM ) );
                directoryFiles.put( directoryName, fileNameAndContents );
            }
        }
    }

    /**
     * File signature.
     *
     * @param file
     *         the file
     *
     * @return the map
     */
    public static Map< String, String > fileSignatureForVaultFile( File file ) {

        Map< String, String > map = new HashMap<>();
        map.put( CHECKSUM, getAdler32CheckSumForVaultFile( file, PropertiesManager.getEncryptionDecryptionDTO() ) );
        return map;
    }

    public static Map< String, String > fileSignatureForLocaltFile( File file ) {
        Map< String, String > map = new HashMap<>();
        map.put( CHECKSUM, getAdler32CheckSumForLocalFile( file ) );
        return map;
    }

    /**
     * Gets the sha 256 check sum.
     *
     * @param file
     *         the name
     *
     * @return the sha 256 check sum
     */
    public static String getAdler32CheckSumForVaultFile( File file, EncryptionDecryptionDTO encDec ) {

        try ( InputStream fis = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( file,
                encDec ); CheckedInputStream cis = new CheckedInputStream( fis, new Adler32() ) ) {
            final byte[] buf = new byte[ 128 ];
            while ( cis.read( buf ) >= 0 ) {
            }
            return String.valueOf( cis.getChecksum().getValue() );

        } catch ( final Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.CHECKSUM_NOT_CALCULATED.getKey(), file.getAbsolutePath() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.CHECKSUM_NOT_CALCULATED.getKey(), file.getAbsolutePath() ) );
        }
    }

    public static String getAdler32CheckSumForLocalFile( File file ) {

        try ( FileInputStream fis = new FileInputStream( file ); CheckedInputStream cis = new CheckedInputStream( fis, new Adler32() ) ) {

            final byte[] buf = new byte[ 128 ];
            while ( cis.read( buf ) >= 0 ) {
            }
            return String.valueOf( cis.getChecksum().getValue() );

        } catch ( final Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.CHECKSUM_NOT_CALCULATED.getKey(), file.getAbsolutePath() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.CHECKSUM_NOT_CALCULATED.getKey(), file.getAbsolutePath() ) );
        }
    }

    /**
     * Gets the sha 256 check sum.
     *
     * @param file
     *         the name
     *
     * @return the sha 256 check sum
     */
    public static String getSha256CheckSum( File file ) {
        try ( FileInputStream fis = new FileInputStream( file ) ) {
            MessageDigest md = MessageDigest.getInstance( SHA2 );
            byte[] dataBytes = new byte[ 1024 ];
            int nread = 0;
            while ( ( nread = fis.read( dataBytes ) ) != -1 ) {
                md.update( dataBytes, 0, nread );
            }
            byte[] mdbytes = md.digest();
            // convert the byte to hex format method 2
            StringBuilder hexString = new StringBuilder();
            for ( byte mdbyte : mdbytes ) {
                hexString.append( Integer.toHexString( 0xFF & mdbyte ) );
            }
            return hexString.toString();
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.CHECKSUM_NOT_CALCULATED.getKey(), file.getAbsolutePath() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.CHECKSUM_NOT_CALCULATED.getKey(), file.getAbsolutePath() ) );
        }
    }

    /**
     * Converts the string to sha 256 check sum.
     *
     * @param plainText
     *         the plainText
     *
     * @return the sha 256 check sum
     */
    public static String getSha256CheckSum( String plainText ) {
        try {
            MessageDigest md = MessageDigest.getInstance( SHA2 );
            md.update( plainText.getBytes() );
            return Base64.getEncoder().encodeToString( md.digest() );

        } catch ( NoSuchAlgorithmException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.CHECKSUM_NOT_CALCULATED.getKey(), plainText ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.CHECKSUM_NOT_CALCULATED.getKey(), plainText ) );
        }
    }

    /**
     * Acknowledge bytes.
     *
     * @param map
     *         the map
     *
     * @return the acknowledge bytes
     */
    public static AcknowledgeBytes acknowledgeBytes( Map< String, String > map ) {
        AcknowledgeBytes acknowledgeBytes;
        if ( map == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MAP_NOT_PROVIDED_FOR_ACKNOWLEDGE_BYTES.getKey() ) );
        }
        acknowledgeBytes = new AcknowledgeBytes();
        String unHexfilePath = new String( FileUtils.convertBase64StringToByteArray( map.get( FILE ) ) );
        File filePath = new File( unHexfilePath );
        acknowledgeBytes.setFile( filePath );
        if ( filePath.isFile() ) {
            acknowledgeBytes.setTransferredBytes( filePath.length() );
        } else {
            acknowledgeBytes.setTransferredBytes( INDEX_POSITION );
        }
        return acknowledgeBytes;
    }

    /**
     * Read from file.
     *
     * @param file
     *         the file
     * @param position
     *         the position
     * @param size
     *         the size
     *
     * @return the byte[]
     */
    public static byte[] readFromFile( RandomAccessFile file, long position, long size ) {
        try {
            file.seek( position );
            byte[] bytes = new byte[ ( int ) size ];
            file.read( bytes );
            return bytes;
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.INVALID_FILE_PROVIDED.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_FILE_PROVIDED.getKey() ) );
        }
    }

    /**
     * Write to file.
     *
     * @param file
     *         the file
     * @param b
     *         the b
     * @param position
     *         the position
     */
    public static void writeToFile( RandomAccessFile file, byte[] b, long position ) {
        try {
            file.seek( position );
            file.write( b );
            file.close();
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.INVALID_FILE_PROVIDED.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_FILE_PROVIDED.getKey() ) );
        }
    }

    /**
     * Write bytes in file.
     *
     * @param httpReceiver
     *         the http receiver
     *
     * @return the string
     */
    public static String writeBytesInFile( HttpReceiver httpReceiver ) {
        if ( httpReceiver == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_FILE_PROVIDED.getKey() ) );
        }
        File file = new File( httpReceiver.getFilePath() );
        if ( createFile( file ) ) {
            try ( RandomAccessFile raf = new RandomAccessFile( file, RW ) ) {
                byte[] baseSixtyFour = convertBase64StringToByteArray( httpReceiver.getReceivedBytes() );
                writeToFile( raf, baseSixtyFour, httpReceiver.getInitialRange() );
            } catch ( IOException e ) {
                log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), file.getAbsolutePath() ), e );
                throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), file.getAbsolutePath() ) );
            }

        }

        return "Initial Range : " + httpReceiver.getInitialRange() + "   Max Range : " + httpReceiver.getMaxRange() + "  Total Size : "
                + httpReceiver.getTotalFileSize() + "  Chunk : " + httpReceiver.getChunk();

    }

    /**
     * Send directory.
     *
     * @param httpSender
     *         the http sender
     *
     * @return the string
     */
    public static String sendDirectory( HttpSender httpSender ) {
        if ( httpSender == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NAME_CANNOT_EMPTY.getKey() ) );
        }
        String destinationDir = httpSender.getDestinationFilePath();
        String parentAbsoluteDir = httpSender.getSrcFile().toString();

        String destinationCurrentFolder = parentAbsoluteDir.replace( new File( parentAbsoluteDir ).getParentFile().toString(), "" );
        Map< String, Map< String, String > > map = getDirectoryFilesWithCheckSum( httpSender.getSrcFile().toString(),
                httpSender.getSrcFile().toString() );
        for ( Map.Entry< String, Map< String, String > > root : map.entrySet() ) {

            String currentFolder = root.getKey();
            if ( !currentFolder.equals( parentAbsoluteDir ) && currentFolder.contains( parentAbsoluteDir ) ) {
                currentFolder = currentFolder.replaceAll( parentAbsoluteDir, "" );
                currentFolder = destinationCurrentFolder + currentFolder;
            } else {
                File file = new File( parentAbsoluteDir );
                currentFolder = currentFolder.replaceAll( file.getParentFile().toString(), "" );
            }
            for ( Map.Entry< String, String > child : root.getValue().entrySet() ) {
                String dest = destinationDir + currentFolder;
                String src = child.getKey();
                String checksum = child.getValue();

                if ( checksum.contains( EMPTY_DIRECTORY ) ) {
                    String fileSeparator = detectFileSeparator( httpSender );
                    File file = new File( src );
                    String payload = FILE_KEY_IN_JSON + dest + fileSeparator + file.getName() + END_STRING_OF_JSON;
                    SuSClient.postRequest( httpSender.getLocation() + ConstantSuscoreApi.CREATE_DIRECTORY, payload,
                            httpSender.getHeaders() );
                } else {
                    httpSender.setDestinationFilePath( dest );
                    httpSender.setSrcFile( new File( src ) );
                    httpSender.setCheckSum( checksum );
                    sendBytesFromFile( httpSender );
                }
            }

        }
        return MessageBundleFactory.getMessage( Messages.FILE_UPLOADED_SUCCESSFULLY.getKey(), parentAbsoluteDir );
    }

    /**
     * Send bytes from file.
     *
     * @param httpSender
     *         the http sender
     *
     * @return the http sender
     */
    public static String sendBytesFromFile( HttpSender httpSender ) {
        if ( httpSender == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NAME_CANNOT_EMPTY.getKey() ) );
        }
        Notification notify = httpSender.validate();
        if ( notify != null && notify.hasErrors() ) {
            throw new SusException( notify.getErrors().toString() );
        }

        if ( StringUtils.isEmpty( httpSender.getCheckSum() ) ) {
            // hey file must be unhexlified
            httpSender.setCheckSum( fileSignatureForVaultFile( httpSender.getSrcFile() ).get( CHECKSUM ) );
        }

        if ( httpSender.getChunk() <= INDEX_POSITION ) {
            httpSender.setChunk( DEFAULT_CHUNK );
        }

        String fileSeparator = detectFileSeparator( httpSender );

        httpSender.setDestinationFilePath( httpSender.getDestinationFilePath() + fileSeparator + httpSender.getSrcFile().getName() );

        // acknowledge byte from
        String destination = FileUtils.convertByteArrayToBase64String( httpSender.getDestinationFilePath().getBytes() );
        String payload = FILE_KEY_IN_JSON + destination + END_STRING_OF_JSON;
        AcknowledgeBytes acknowledgeBytes = acknowledgeBytes( httpSender, payload );
        httpSender.setAckBytes( acknowledgeBytes.getTransferredBytes() );

        fileIterateInChunks( httpSender );

        Map< String, String > map = acknowledgeCheckSum( httpSender, payload );

        if ( map.get( CHECKSUM ).equals( httpSender.getCheckSum() ) ) {
            return MessageBundleFactory.getMessage( Messages.FILE_UPLOADED_SUCCESSFULLY.getKey(), httpSender.getSrcFile().getName() );
        } else {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.INVALID_CHECKSUM.getKey(), httpSender.getSrcFile().getName() ) );
        }

    }

    /**
     * Sync file.
     *
     * @param httpSender
     *         the http sender
     *
     * @return the string
     */
    public static String syncFile( HttpSender httpSender ) {
        if ( httpSender == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NAME_CANNOT_EMPTY.getKey() ) );
        }

        Notification notify = httpSender.validate();
        if ( notify != null && notify.hasErrors() ) {
            throw new SusException( notify.getErrors().toString() );
        }

        if ( StringUtils.isEmpty( httpSender.getCheckSum() ) ) {
            // hey file must be unhexlified
            httpSender.setCheckSum( fileSignatureForVaultFile( httpSender.getSrcFile() ).get( CHECKSUM ) );
        }
        if ( ( httpSender.getChunk() <= INDEX_POSITION ) ) {
            httpSender.setChunk( DEFAULT_CHUNK );
        }
        String fileSeparator = detectFileSeparator( httpSender );

        httpSender.setDestinationFilePath( httpSender.getDestinationFilePath() + fileSeparator + httpSender.getSrcFile().getName() );

        String destination = FileUtils.convertByteArrayToBase64String( httpSender.getDestinationFilePath().getBytes() );
        String payload = FILE_KEY_IN_JSON + destination + END_STRING_OF_JSON;

        Map< String, String > map = acknowledgeCheckSum( httpSender, payload );

        if ( !map.isEmpty() && !( map.get( CHECKSUM ).equals( httpSender.getCheckSum() ) ) ) {
            if ( removeFile( httpSender, payload ) ) {
                return syncFile( httpSender, payload );
            } else {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.FILE_NOT_REMOVED.getKey(), httpSender.getSrcFile().getName() ) );
            }
        } else if ( map.isEmpty() ) {
            return syncFile( httpSender, payload );
        } else {
            return FILE_IS_SYNCED;
        }
    }

    /**
     * Sync file.
     *
     * @param httpSender
     *         the http sender
     * @param payload
     *         the payload
     *
     * @return the string
     */
    private static String syncFile( HttpSender httpSender, String payload ) {
        AcknowledgeBytes acknowledgeBytes = acknowledgeBytes( httpSender, payload );
        httpSender.setAckBytes( acknowledgeBytes.getTransferredBytes() );

        fileIterateInChunks( httpSender );
        Map< String, String > checksumMap = acknowledgeCheckSum( httpSender, payload );

        if ( checksumMap.get( CHECKSUM ).equals( httpSender.getCheckSum() ) ) {
            return "File is synced successfully";
        } else {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.INVALID_CHECKSUM.getKey(), httpSender.getSrcFile().getName() ) );
        }

    }

    /**
     * Creates the directory.
     *
     * @param map
     *         the map
     *
     * @return the map
     */
    public static boolean createDirectory( Map< String, String > map ) {
        boolean checker = false;
        File file = new File( map.get( FILE ) );
        if ( makeDirectory( file ) ) {
            checker = true;
        }
        return checker;
    }

    /**
     * Detect OS file separator.
     *
     * @return the map
     */
    public static Map< String, String > detectOSFileSeparator() {
        Map< String, String > map = new HashMap<>();
        // detect OS
        if ( OSValidator.isWindows() ) {
            map.put( FILE_SEP, "\\" );
        } else if ( OSValidator.isMac() || OSValidator.isUnix() || OSValidator.isSolaris() ) {
            map.put( FILE_SEP, "/" );
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OS_NOT_SUPPORTED.getKey() ) );
        }
        return map;
    }

    /**
     * Sets the global file permissions. 777
     *
     * @param f
     *         the f
     *
     * @return true, if successful
     *
     * @deprecated in favor of setGlobalAllFilePermissions(Path p)
     */
    @Deprecated
    public static boolean setGlobalAllFilePermissions( File f ) {
        return setGlobalAllFilePermissions( f.toPath() );
    }

    /**
     * Sets the global file permissions. 777
     *
     * @param p
     *         the path
     *
     * @return true, if successful
     */
    public static boolean setGlobalAllFilePermissions( Path p ) {
        try {
            if ( OSValidator.isUnix() ) {
                Set< PosixFilePermission > perms = Files.readAttributes( p, PosixFileAttributes.class ).permissions();
                perms.add( PosixFilePermission.OWNER_WRITE );
                perms.add( PosixFilePermission.OWNER_READ );
                perms.add( PosixFilePermission.OWNER_EXECUTE );
                perms.add( PosixFilePermission.GROUP_WRITE );
                perms.add( PosixFilePermission.GROUP_READ );
                perms.add( PosixFilePermission.GROUP_EXECUTE );
                perms.add( PosixFilePermission.OTHERS_WRITE );
                perms.add( PosixFilePermission.OTHERS_READ );
                perms.add( PosixFilePermission.OTHERS_EXECUTE );
                Files.setPosixFilePermissions( p, perms );
            }
        } catch ( IOException e ) {
            log.warn( e.getMessage() );
            return false;
        }
        return true;
    }

    /**
     * Sets the global Execute permissions. 755
     *
     * @param f
     *         the f
     *
     * @return true, if successful
     *
     * @deprecated in favor of setGlobalExecuteFilePermissions(Path p)
     */
    @Deprecated
    public static boolean setGlobalExecuteFilePermissions( File f ) {
        return setGlobalExecuteFilePermissions( f.toPath() );
    }

    /**
     * Sets the global Execute permissions. 755
     *
     * @param p
     *         the path
     *
     * @return true, if successful
     */
    public static boolean setGlobalExecuteFilePermissions( Path p ) {
        try {
            if ( OSValidator.isUnix() ) {
                Set< PosixFilePermission > perms = Files.readAttributes( p, PosixFileAttributes.class ).permissions();
                perms.add( PosixFilePermission.OWNER_WRITE );
                perms.add( PosixFilePermission.OWNER_READ );
                perms.add( PosixFilePermission.OWNER_EXECUTE );
                perms.add( PosixFilePermission.GROUP_READ );
                perms.add( PosixFilePermission.GROUP_EXECUTE );
                perms.add( PosixFilePermission.OTHERS_READ );
                perms.add( PosixFilePermission.OTHERS_EXECUTE );
                Files.setPosixFilePermissions( p, perms );
            }
        } catch ( IOException e ) {
            log.warn( e.getMessage() );
            return false;
        }
        return true;
    }

    /**
     * Sets the global Read permissions. 744
     *
     * @param f
     *         the f
     *
     * @return true, if successful
     *
     * @deprecated in favor of setGlobalReadFilePermissions(Path p)
     */
    @Deprecated
    public static boolean setGlobalReadFilePermissions( File f ) {
        return setGlobalReadFilePermissions( f.toPath() );
    }

    /**
     * Sets the global Read permissions. 744
     *
     * @param p
     *         the path
     *
     * @return true, if successful
     */
    public static boolean setGlobalReadFilePermissions( Path p ) {
        try {
            if ( OSValidator.isUnix() ) {
                Set< PosixFilePermission > perms = Files.readAttributes( p, PosixFileAttributes.class ).permissions();
                perms.add( PosixFilePermission.OWNER_WRITE );
                perms.add( PosixFilePermission.OWNER_READ );
                perms.add( PosixFilePermission.OWNER_EXECUTE );
                perms.add( PosixFilePermission.GROUP_READ );
                perms.add( PosixFilePermission.OTHERS_READ );
                Files.setPosixFilePermissions( p, perms );
            }
        } catch ( IOException e ) {
            log.warn( e.getMessage() );
            return false;
        }
        return true;
    }

    /**
     * Sets owner only permissions.
     *
     * @param p
     *         the p
     *
     * @return the owner only permissions
     */
    public static boolean setOwnerOnlyPermissions( Path p ) {
        try {
            if ( OSValidator.isUnix() ) {
                Set< PosixFilePermission > perms = Files.readAttributes( p, PosixFileAttributes.class ).permissions();
                // Clear existing permissions
                perms.clear();
                // Add OWNER permissions (700)
                perms.add( PosixFilePermission.OWNER_READ );
                perms.add( PosixFilePermission.OWNER_WRITE );
                perms.add( PosixFilePermission.OWNER_EXECUTE );
                Files.setPosixFilePermissions( p, perms );
            }
        } catch ( IOException e ) {
            log.warn( e.getMessage() );
            return false;
        }
        return true;
    }

    /**
     * Detect file separator.
     *
     * @param httpSender
     *         the http sender
     *
     * @return the string
     */
    private static String detectFileSeparator( HttpSender httpSender ) {
        SusResponseDTO susResponseDTOForFileSeparator = SuSClient.getRequest(
                httpSender.getLocation() + ConstantSuscoreApi.DETECT_FILE_SEPARATOR, httpSender.getHeaders() );
        String json = JsonUtils.toJson( susResponseDTOForFileSeparator.getData(), String.class );
        Map< String, String > mapFileSep = JsonUtils.convertStringToMap( json );

        return mapFileSep.get( FILE_SEP );
    }

    /**
     * Acknowledge bytes.
     *
     * @param httpSender
     *         the http sender
     * @param payload
     *         the payload
     *
     * @return the acknowledge bytes
     */
    private static AcknowledgeBytes acknowledgeBytes( HttpSender httpSender, String payload ) {
        SusResponseDTO susResponseDTOAcknowledgeBytes = SuSClient.postRequest(
                httpSender.getLocation() + ConstantSuscoreApi.ACKNOWLEDGE_BYTES, payload, httpSender.getHeaders() );
        String json = JsonUtils.toJson( susResponseDTOAcknowledgeBytes.getData(), String.class );
        return JsonUtils.jsonToObject( json, AcknowledgeBytes.class );

    }

    /**
     * Removes the file.
     *
     * @param httpSender
     *         the http sender
     * @param payload
     *         the payload
     *
     * @return true, if successful
     */
    private static boolean removeFile( HttpSender httpSender, String payload ) {
        SusResponseDTO susResponseDTOAcknowledgeBytes = SuSClient.postRequest( httpSender.getLocation() + ConstantSuscoreApi.REMOVE_FILE,
                payload, httpSender.getHeaders() );
        String json = JsonUtils.toJson( susResponseDTOAcknowledgeBytes.getData(), String.class );
        return JsonUtils.jsonToObject( json, Boolean.class );
    }

    /**
     * Acknowledge bytes.
     *
     * @param map
     *         the map
     *
     * @return the acknowledge bytes
     */
    public static boolean removeFile( Map< String, String > map ) {
        if ( map == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MAP_NOT_PROVIDED_FOR_ACKNOWLEDGE_BYTES.getKey() ) );
        }
        String unHexfilePath = new String( FileUtils.convertBase64StringToByteArray( map.get( FILE ) ) );

        Path path = Paths.get( unHexfilePath );
        try {
            Files.deleteIfExists( path );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }

        return true;
    }

    /**
     * Acknowledge check sum.
     *
     * @param httpSender
     *         the http sender
     * @param payload
     *         the payload
     *
     * @return the map
     */
    private static Map< String, String > acknowledgeCheckSum( HttpSender httpSender, String payload ) {
        SusResponseDTO susResponseDTOAcknowledgeCheckSum = SuSClient.postRequest(
                httpSender.getLocation() + ConstantSuscoreApi.ACKNOWLEDGE_CHECKSUM, payload, httpSender.getHeaders() );
        String json = JsonUtils.toJson( susResponseDTOAcknowledgeCheckSum.getData(), String.class );
        return JsonUtils.convertStringToMap( json );
    }

    /**
     * File iterate in chunks.
     *
     * @param httpSender
     *         the http sender
     */
    public static void fileIterateInChunks( HttpSender httpSender ) {

        File f = httpSender.getSrcFile();
        long chunk = httpSender.getChunk();
        long indexer = httpSender.getAckBytes();
        try ( RandomAccessFile randomAccessFile = new RandomAccessFile( f, READ_MODE ) ) {
            long fileLength = f.length();

            if ( indexer > fileLength ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.INVALID_INDEXER.getKey(), httpSender.getSrcFile().getName() ) );
            }

            if ( fileLength < chunk ) {
                byte[] actualBytes = readFromFile( randomAccessFile, INDEX_POSITION, fileLength );
                String bs = convertByteArrayToBase64String( actualBytes );
                chunkTransferring( httpSender, INDEX_POSITION, fileLength, fileLength, fileLength, bs );
            } else {
                long chunkDivisor = ( fileLength - indexer ) / chunk;
                long chunkModulus = ( fileLength - indexer ) % chunk;
                long incremental = indexer;
                if ( chunkModulus != INDEX_POSITION ) {
                    incremental = iterationOnFileViaChunks( httpSender, randomAccessFile, chunk, chunkDivisor, incremental, fileLength );
                    byte[] actualBytes = readFromFile( randomAccessFile, incremental, chunkModulus );
                    String bs = convertByteArrayToBase64String( actualBytes );
                    chunkTransferring( httpSender, incremental, incremental + chunkModulus, fileLength, chunkModulus, bs );
                } else {
                    iterationOnFileViaChunks( httpSender, randomAccessFile, chunk, chunkDivisor, incremental, fileLength );
                }
            }
        } catch ( IOException e ) {
            log.error(
                    MessageBundleFactory.getMessage( Messages.FILE_CHUNK_EXCEPTION.getKey(), f.getAbsolutePath(), chunk, e.getMessage() ),
                    e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FILE_CHUNK_EXCEPTION.getKey(), f.getAbsolutePath(), chunk, e.getMessage() ) );

        }
    }

    /**
     * Iteration on file via chunks.
     *
     * @param httpSender
     *         the http sender
     * @param randomAccessFile
     *         the random access file
     * @param chunk
     *         the chunk
     * @param chunkDivisor
     *         the chunk divisor
     * @param incremental
     *         the incremental
     * @param fileLength
     *         the file length
     *
     * @return the long
     */
    public static long iterationOnFileViaChunks( HttpSender httpSender, RandomAccessFile randomAccessFile, long chunk, long chunkDivisor,
            long incremental, long fileLength ) {
        for ( long i = 0; i < chunkDivisor; i++ ) {
            byte[] actualBytes = readFromFile( randomAccessFile, incremental, chunk );
            String bs = convertByteArrayToBase64String( actualBytes );
            chunkTransferring( httpSender, incremental, incremental + chunk, fileLength, chunk, bs );
            incremental = incremental + chunk;
        }
        return incremental;
    }

    /**
     * Prepare http receiver.
     *
     * @param filePath
     *         the file path
     * @param initialRange
     *         the initial range
     * @param maxRange
     *         the max range
     * @param totalSize
     *         the total size
     * @param chunk
     *         the chunk
     * @param bytes
     *         the bytes
     *
     * @return the http receiver
     */
    private static HttpReceiver prepareHttpReceiver( String filePath, long initialRange, long maxRange, long totalSize, long chunk,
            String bytes ) {
        HttpReceiver httpReceiver = new HttpReceiver();
        httpReceiver.setReceivedBytes( bytes );
        httpReceiver.setFilePath( filePath );
        httpReceiver.setInitialRange( initialRange );
        httpReceiver.setMaxRange( maxRange );
        httpReceiver.setTotalFileSize( totalSize );
        httpReceiver.setChunk( chunk );
        return httpReceiver;
    }

    /**
     * Chunk transferring.
     *
     * @param httpSender
     *         the http sender
     * @param initialRange
     *         the initial range
     * @param maxRange
     *         the max range
     * @param totalSize
     *         the total size
     * @param chunk
     *         the chunk
     * @param bs
     *         the bs
     */
    public static void chunkTransferring( HttpSender httpSender, long initialRange, long maxRange, long totalSize, long chunk, String bs ) {
        HttpReceiver httpReceiver = prepareHttpReceiver( httpSender.getDestinationFilePath(), initialRange, maxRange, totalSize, chunk,
                bs );
        String payload = JsonUtils.toJson( httpReceiver, HttpReceiver.class );
        SuSClient.postRequest( httpSender.getLocation() + ConstantSuscoreApi.RECEIVE_FILE, payload, httpSender.getHeaders() );
    }

    /**
     * Creates the file.
     *
     * @param file
     *         the file
     *
     * @return true, if successful
     */
    public static boolean createFile( File file ) {
        try {
            if ( file.exists() ) {
                return true;
            } else if ( !file.getParentFile().exists() ) {
                file.getParentFile().mkdirs();

                return file.createNewFile();

            } else {
                return file.createNewFile();
            }
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_CREATED.getKey(), file.getAbsolutePath() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_CREATED.getKey(), file.getAbsolutePath() ) );
        }
    }

    /**
     * Make directory.
     *
     * @param dir
     *         the dir
     *
     * @return true, if successful
     */
    public static boolean makeDirectory( File dir ) {
        if ( !dir.isDirectory() ) {
            return dir.mkdir();
        } else {
            return true;
        }
    }

    /**
     * helper function to check file extension
     * <p>
     * This function takes two parameters. The first parameter consists of the registered mime types and the second parameter denotes the
     * actual mime type to be checked. In case the mime type is valid, the file extension is returned, else @SusException is thrown
     * mentioning the mime type is not registered.
     *
     * @param registeredTypes
     *         the registered types
     * @param mimeType
     *         the mime type
     *
     * @return the string
     */
    public static String verifyMimeTypeAndGetFileExtension( String[] registeredTypes, String mimeType ) {
        String fileExt = null;
        String[] splitType = mimeType.split( ConstantsString.FORWARD_SLASH );

        if ( splitType.length == ConstantsInteger.INTEGER_VALUE_TWO ) {
            fileExt = splitType[ ConstantsInteger.INTEGER_VALUE_ONE ];

            boolean registered = false;

            for ( String regFileType : registeredTypes ) {
                if ( fileExt.contentEquals( regFileType ) ) {
                    registered = true;
                }
            }
            if ( !registered ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.DOCUMENT_YOU_HAVE_SELECTED_INVALID_IMAGE_OR_TYPE_ALLOWED_ARE.getKey(),
                                String.join( ConstantsString.COMMA, registeredTypes ) ) );
            }
        }
        return fileExt;
    }

    /**
     * Convert file contents to string.
     *
     * @param file
     *         the file
     *
     * @return the string
     */
    public static String convertFileContentsToString( File file ) {
        try ( InputStream fileStream = new FileInputStream( file ); ObjectInputStream ois = new ObjectInputStream( fileStream ) ) {
            String contents = ois.readObject().toString();
            return contents;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Convert object to input stream.
     *
     * @param object
     *         the object
     *
     * @return the input stream
     */
    public static InputStream convertObjectToInputStream( Object object ) {
        byte[] bytes = null;
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream( baos ) ) {
            oos.writeObject( JsonUtils.toJson( object ) );
            bytes = baos.toByteArray();
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return new ByteArrayInputStream( bytes );
    }

    /**
     * Gets the filelist.
     *
     * @param path
     *         the path
     * @param dirOnly
     *         the dir only
     *
     * @return the filelist
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static List< FileObject > getFilelist( String path, boolean dirOnly ) throws IOException {
        if ( path != null ) {
            path = path.trim();
        }
        Path newPath = Paths.get( path );
        while ( Files.isSymbolicLink( newPath ) ) {
            newPath = Files.readSymbolicLink( newPath );
        }
        FileVisitor fileVisitor = new FileVisitor( dirOnly );
        Files.walkFileTree( newPath, new HashSet<>(), ConstantsInteger.INTEGER_VALUE_ONE, fileVisitor );
        return fileVisitor.getFiles().stream()
                .peek( file -> file.setTitle( new String( file.getTitle().getBytes( StandardCharsets.UTF_8 ), StandardCharsets.UTF_8 ) ) )
                .collect( Collectors.toList() );

    }

    /**
     * Write to file.
     *
     * @param path
     *         the path
     * @param data
     *         the data
     *
     * @throws IOException
     *         the io exception
     */
    public static void writeToFile( String path, String data ) throws IOException {
        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( path, StandardCharsets.UTF_8, false ) ) ) {
            writer.write( data );
        }
    }

    /**
     * Read file as string.
     *
     * @param path
     *         the path
     *
     * @return the string
     *
     * @throws IOException
     *         the io exception
     */
    public static String readFileAsString( String path ) throws IOException {
        Path fileAsPath = Paths.get( path );
        return Files.readString( fileAsPath );
    }

    /**
     * Copy file.
     *
     * @param src
     *         the src
     * @param dest
     *         the dest
     *
     * @throws IOException
     *         the io exception
     */
    public static void copyFile( String src, String dest ) throws IOException {
        Path source = Paths.get( src );
        Path destPath = Paths.get( dest );
        Files.copy( source, destPath );
    }

    /**
     * Read tail string.
     *
     * @param path
     *         the path
     * @param numberOfLines
     *         the number of lines
     *
     * @return the string
     */
    public static String readTail( Path path, int numberOfLines ) {
        if ( path == null || !Files.exists( path ) || numberOfLines <= 0 ) {
            return "";
        }

        List< String > lines = new ArrayList<>();
        try ( RandomAccessFile randomAccessFile = new RandomAccessFile( path.toFile(), "r" ) ) {
            long fileLength = Files.size( path ) - 1;
            StringBuilder sb = new StringBuilder();

            // Set the pointer at the last of the file
            randomAccessFile.seek( fileLength );

            for ( long pointer = fileLength; pointer >= 0; pointer-- ) {
                randomAccessFile.seek( pointer );
                char c = ( char ) randomAccessFile.readByte();
                // Check if it's a line
                if ( c == '\n' ) {
                    if ( sb.length() > 0 ) {
                        lines.add( sb.reverse().toString() );
                        sb = new StringBuilder();
                        if ( lines.size() == numberOfLines ) {
                            break;
                        }
                    }
                } else {
                    sb.append( c );
                }
            }

            // To capture the last line if it exists
            if ( sb.length() > 0 ) {
                lines.add( sb.reverse().toString() );
            }

            Collections.reverse( lines ); // Since lines are read from last to first, we need to reverse them
        } catch ( IOException e ) {
            e.printStackTrace();
            return null;
        }

        return String.join( "\r\n", lines );
    }

    /**
     * Delete temp files.
     *
     * @param files
     *         the files
     */
    public static void deleteFiles( Path... files ) {
        if ( files == null ) {
            return;
        }
        for ( Path file : files ) {
            try {
                if ( file != null ) {
                    if ( !Files.isDirectory( file ) ) {
                        Files.deleteIfExists( file );
                    } else {
                        deleteNonEmptyDirectory( file );
                    }
                }
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_REMOVED.getKey(), file.toAbsolutePath() ) );
            }
        }
    }

    /**
     * Delete non empty directory.
     *
     * @param file
     *         the file
     */
    public static void deleteNonEmptyDirectory( Path file ) {
        try ( Stream< Path > walk = Files.walk( file ) ) {
            walk.sorted( Comparator.reverseOrder() ).forEach( path -> {
                try {
                    Files.deleteIfExists( path );
                } catch ( IOException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_REMOVED.getKey(), file.toAbsolutePath() ) );
                }
            } );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_REMOVED.getKey(), file.toAbsolutePath() ) );
        }
    }

    /**
     * Write input file string.
     *
     * @param payload
     *         the payload
     * @param inputFilePath
     *         the input file path
     *
     * @return the string
     */
    public static String writeInputFile( Object payload, Path inputFilePath ) {
        try {
            if ( Files.notExists( inputFilePath.getParent() ) ) {
                Files.createDirectory( inputFilePath.getParent() );
                FileUtils.setGlobalAllFilePermissions( inputFilePath.getParent() );
            }
            Files.deleteIfExists( inputFilePath );
            Files.createFile( inputFilePath );
            FileUtils.setGlobalAllFilePermissions( inputFilePath );
            Files.writeString( inputFilePath, Objects.requireNonNull( JsonUtils.toJson( payload ) ), StandardCharsets.UTF_8 );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        }
        return inputFilePath.toAbsolutePath().toString();
    }

    /**
     * Create user directory in temp.
     *
     * @param user
     *         the user
     */
    public static void createUserDirectoryInTemp( String userUid ) {
        String userDirectoryInTemp = PropertiesManager.getDefaultServerTempPath() + File.separator + userUid;
        try {
            if ( !userUid.equalsIgnoreCase( ConstantsString.SIMUSPACE ) ) {
                Path dirPath = Path.of( userDirectoryInTemp );
                if ( Files.notExists( dirPath ) ) {
                    Files.createDirectory( dirPath );
                } else if ( !Files.isDirectory( dirPath ) ) {
                    Files.delete( dirPath );
                    Files.createDirectory( dirPath );
                }
                setGlobalAllFilePermissions( dirPath );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_FILE_AT.getKey(), userDirectoryInTemp ), e );
        }
    }

    /**
     * Is directory empty boolean.
     *
     * @param path
     *         the path
     *
     * @return the boolean
     *
     * @throws IOException
     *         the io exception
     */
    public static boolean isDirectoryNotEmpty( Path path ) throws IOException {
        if ( !Files.isDirectory( path ) ) {
            throw new IllegalArgumentException( "The provided path is not a directory: " + path );
        }

        try ( DirectoryStream< Path > directoryStream = Files.newDirectoryStream( path ) ) {
            return directoryStream.iterator().hasNext();
        }
    }

    /**
     * Copy directory.
     *
     * @param source
     *         the source
     * @param destination
     *         the destination
     *
     * @throws IOException
     *         the io exception
     */
    public static void copyDirectory( Path source, Path destination ) throws IOException {
        // Validate source directory
        if ( !Files.isDirectory( source ) ) {
            throw new IllegalArgumentException( "Source path is not a directory: " + source );
        }

        // Create destination directory if it doesn't exist
        if ( !Files.exists( destination ) ) {
            Files.createDirectories( destination );
        }

        // Walk the file tree and copy each file/directory
        Files.walkFileTree( source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) throws IOException {
                // Create sub-directory in the destination directory
                Path targetDir = destination.resolve( source.relativize( dir ) );
                if ( !Files.exists( targetDir ) ) {
                    Files.createDirectories( targetDir );
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
                // Copy file to the corresponding destination path
                Path targetFile = destination.resolve( source.relativize( file ) );
                Files.copy( file, targetFile, StandardCopyOption.REPLACE_EXISTING );
                return FileVisitResult.CONTINUE;
            }
        } );
    }

}
