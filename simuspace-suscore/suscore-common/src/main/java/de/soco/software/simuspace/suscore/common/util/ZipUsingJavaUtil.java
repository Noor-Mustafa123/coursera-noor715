package de.soco.software.simuspace.suscore.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The Class ZipUsingJavaUtil handles windows and linux single/multi paths for file and dir zip .
 *
 * @author noman arshad
 * @since 2.0
 */
public class ZipUsingJavaUtil {

    /**
     * Zip files and folders.
     *
     * @param filePath
     *         the file path
     * @param dirPath
     *         the dir path
     * @param destZipFile
     *         the dest zip file
     *
     * @throws Exception
     *         the exception
     */
    /*
     * zip the folders
     */
    public static void zipFilesAndFolders( Set< String > filePath, Set< String > dirPath, String destZipFile ) throws Exception {

        try ( FileOutputStream fileWriter = new FileOutputStream( destZipFile ); ZipOutputStream zip = new ZipOutputStream( fileWriter ) ) {

            for ( String srcFolder : dirPath ) {
                addFolderToZip( "", srcFolder, zip );
            }
            for ( String fileSrc : filePath ) {
                addFileToZip( new File( fileSrc ), zip );
            }
            zip.flush();
        }
    }

    /**
     * Adds the file to zip.
     *
     * @param path
     *         the path
     * @param srcFile
     *         the src file
     * @param zip
     *         the zip
     * @param flag
     *         the flag
     *
     * @throws Exception
     *         the exception
     */
    private static void addFileToZip( String path, String srcFile, ZipOutputStream zip, boolean flag ) throws Exception {

        File folder = new File( srcFile );

        if ( flag ) {
            zip.putNextEntry( new ZipEntry( path + "/" + folder.getName() + "/" ) );
        } else {
            if ( folder.isDirectory() ) {

                addFolderToZip( path, srcFile, zip );
            } else {

                byte[] buf = new byte[ 1024 ];
                int len;
                try ( FileInputStream in = new FileInputStream( srcFile ) ) {
                    zip.putNextEntry( new ZipEntry( path + "/" + folder.getName() ) );
                    while ( ( len = in.read( buf ) ) > 0 ) {

                        zip.write( buf, 0, len );
                    }
                }
            }
        }
    }

    /**
     * Adds the folder to zip.
     *
     * @param path
     *         the path
     * @param srcFolder
     *         the src folder
     * @param zip
     *         the zip
     *
     * @throws Exception
     *         the exception
     */
    private static void addFolderToZip( String path, String srcFolder, ZipOutputStream zip ) throws Exception {
        File folder = new File( srcFolder );

        if ( folder.list().length == 0 ) {
            addFileToZip( path, srcFolder, zip, true );
        } else {

            for ( String fileName : folder.list() ) {
                if ( "".equals( path ) ) {
                    addFileToZip( folder.getName(), srcFolder + "/" + fileName, zip, false );
                } else {
                    addFileToZip( path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false );
                }
            }
        }
    }

    /**
     * Adds the file to zip.
     *
     * @param input
     *         the input
     * @param zipOut
     *         the zip out
     */
    private static void addFileToZip( File input, ZipOutputStream zipOut ) {
        try ( FileInputStream fis = new FileInputStream( input ) ) {
            ZipEntry ze = new ZipEntry( input.getName() );
            zipOut.putNextEntry( ze );
            byte[] tmp = new byte[ 4 * 1024 ];
            int size = 0;
            while ( ( size = fis.read( tmp ) ) != -1 ) {
                zipOut.write( tmp, 0, size );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, FileUtils.class );
        }
    }

}
