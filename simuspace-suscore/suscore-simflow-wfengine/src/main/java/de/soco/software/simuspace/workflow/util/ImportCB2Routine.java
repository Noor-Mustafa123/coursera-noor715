package de.soco.software.simuspace.workflow.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.data.model.DataObjectValueDTO;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;

/**
 * The class is responsible for filter out directory contents on the behalf of the user information file "Settings.ini".
 *
 * @author M.Nasir.Farooq
 */
public class ImportCB2Routine {

    /**
     * The structures.
     */
    public final List< ImportObject > structures = new ArrayList<>();

    private static Map< String, CB2FileInfoDTO > filesMap;

    /**
     * The inner class is used for scanning data objects with scanning information and will prepare the zip data.
     */
    public static class DataObjectScanner extends SimpleFileVisitor< Path > {

        /**
         * Instantiates a new data object scanner.
         */
        private DataObjectScanner() {
            super();
        }

        /**
         * Gets the files by extension from directory.
         *
         * @param dir
         *         the dir
         */
        private void prepareCB2Import( Path dir ) {

            final File[] allFilesInDir = dir.toFile().listFiles();
            for ( final File file : allFilesInDir ) {
                if ( file.isFile() && file.getName().equals( "object.attributes" ) ) {
                    try ( BufferedReader b = new BufferedReader( new FileReader( file ) ) ) {
                        String name = null;
                        String objectType = null;
                        String filePath = null;
                        // -----------
                        String dataObjectValue;
                        String dataObjectValueQuantityType;
                        String dataObjectValueunit;

                        // -----------
                        String group = null;
                        // -----------
                        String line;
                        DataObjectValueDTO valueDTO = new DataObjectValueDTO();
                        while ( ( line = b.readLine() ) != null ) {
                            String[] parts = line.split( "=", 2 );
                            if ( parts.length >= 2 ) {

                                String key = parts[ 0 ];
                                String value = parts[ 1 ];
                                if ( key.trim().equalsIgnoreCase( "name" ) ) {
                                    name = value.trim();
                                }
                                if ( key.trim().equalsIgnoreCase( "objectType" ) ) {
                                    objectType = value.trim();
                                }

                                if ( key.trim().equalsIgnoreCase( "files" ) ) {
                                    // empty for now.
                                }

                                if ( key.trim().equalsIgnoreCase( "group" ) ) {
                                    group = value.trim();
                                }

                                if ( ( file.getPath().contains( "Curve" ) && key.trim().equalsIgnoreCase( "curve" ) )
                                        || ( file.getPath().contains( "Picture" ) && key.trim().equalsIgnoreCase( "image" ) )
                                        || ( file.getPath().contains( "Movie" ) && key.trim().equalsIgnoreCase( "movie" ) )
                                        || ( file.getPath().contains( "Document" ) && key.trim().equalsIgnoreCase( "files" ) ) ) {
                                    filePath = file.getPath().replace( "object.attributes", value.trim() );
                                }

                                if ( key.trim().equalsIgnoreCase( "Value.nominal" ) ) {
                                    valueDTO = new DataObjectValueDTO();
                                    filePath = file.getPath().replace( "object.attributes", name );
                                    dataObjectValue = value.trim();
                                    valueDTO.setName( name );
                                    valueDTO.setValue( dataObjectValue );
                                }

                                if ( key.trim().equalsIgnoreCase( "value.quantityType" ) ) {
                                    dataObjectValueQuantityType = value.trim();
                                    valueDTO.setDimension( dataObjectValueQuantityType );
                                }

                                if ( key.trim().equalsIgnoreCase( "value.units" ) ) {
                                    dataObjectValueunit = value.trim();
                                    valueDTO.setUnit( dataObjectValueunit );
                                }

                                if ( name != null && objectType != null ) {
                                    CB2FileInfoDTO cb2FileInfoDTO = new CB2FileInfoDTO();
                                    cb2FileInfoDTO.setName( name );
                                    cb2FileInfoDTO.setObjectType( objectType );
                                    cb2FileInfoDTO.setPath( filePath );
                                    cb2FileInfoDTO.setDataObjectValue( valueDTO );
                                    cb2FileInfoDTO.setGroup( group );
                                    filesMap.put( name, cb2FileInfoDTO );
                                }
                            }
                        }
                    } catch ( Exception e ) {
                        ExceptionLogger.logException( e, getClass() );
                    }
                }
            }
        }

        /* (non-Javadoc)
         * @see java.nio.file.SimpleFileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
         */
        @Override
        public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) {

            prepareCB2Import( dir );
            return FileVisitResult.CONTINUE;
        }

        /* (non-Javadoc)
         * @see java.nio.file.SimpleFileVisitor#visitFileFailed(java.lang.Object, java.io.IOException)
         */
        @Override
        public FileVisitResult visitFileFailed( Path file, IOException exc ) {
            ExceptionLogger.logException( exc, getClass() );
            return FileVisitResult.CONTINUE;
        }

    }

    /**
     * Scan directory with import settings and prepare import objects.
     *
     * @param sourcePath
     *         the source path
     *
     * @return the import object
     */
    public static ImportObject scanDirectoryWithImportSettingsAndPrepareImportObjects( String sourcePath ) {

        try {
            sourcePath = Paths.get( sourcePath ).toFile().getAbsolutePath();
            filesMap = new HashMap<>();
            final ImportCB2Routine.DataObjectScanner visitor = new ImportCB2Routine.DataObjectScanner();

            Files.walkFileTree( Paths.get( sourcePath ), visitor );

            final ImportObject firstParent = new ImportObject();
            String path = sourcePath;
            final Path dir = Paths.get( path );
            firstParent.setName( dir.toFile().getName() );
            firstParent.setParent( dir.getParent().toFile().getAbsolutePath() );
            firstParent.setFolder( true );
            firstParent.setPath( path );
            // hack start, use given object-type from config
            firstParent.setObjectType( "PostResult" );
            // hack end
            addFilesToPPO( firstParent, dir );
            return firstParent;

        } catch ( final JsonSerializationException | IOException e ) {
            ExceptionLogger.logException( e, ImportCB2Routine.class );
            throw new SusRuntimeException( e.getMessage() );
        }

    }

    private static void addFilesToPPO( ImportObject firstParent, Path dir ) {

        if ( !filesMap.isEmpty() ) {
            for ( Map.Entry< String, CB2FileInfoDTO > entry : filesMap.entrySet() ) {
                ImportObject importObject = new ImportObject();
                importObject.setName( entry.getKey() );
                importObject.setParent( dir.getParent().toFile().getAbsolutePath() );
                importObject.setFolder( false );
                CB2FileInfoDTO cb2File = entry.getValue();
                importObject.setPath( cb2File.getPath() );
                if ( cb2File.getObjectType().contains( "Image" ) || cb2File.getObjectType().contains( "Picture" ) ) {
                    importObject.setObjectType( "KeyResultImage" );
                    firstParent.getChildren().add( importObject );
                } else if ( cb2File.getObjectType().contains( "Curve" ) ) {
                    importObject.setObjectType( "KeyResultCurve" );
                    firstParent.getChildren().add( importObject );
                } else if ( cb2File.getObjectType().contains( "Movie" ) ) {
                    importObject.setObjectType( "KeyResultMovie" );
                    firstParent.getChildren().add( importObject );
                } else if ( cb2File.getObjectType().contains( "Value" ) ) {
                    importObject.setObjectType( "KeyResultValue" );
                    DataObjectValueDTO dataObjectValueDTO = cb2File.getDataObjectValue();
                    String dataObjectValueJSON = JsonUtils.objectToJson( dataObjectValueDTO );
                    if ( importObject.getPath() != null ) {
                        try ( PrintWriter out = new PrintWriter( importObject.getPath() ) ) {
                            out.println( dataObjectValueJSON );
                        } catch ( FileNotFoundException e ) {
                            ExceptionLogger.logException( e, ImportCB2Routine.class );
                        }

                    }
                    firstParent.getChildren().add( importObject );
                } else if ( cb2File.getObjectType().contains( "Document" ) ) {
                    importObject.setGroup( cb2File.getGroup() );
                    importObject.setObjectType( "KeyResultDocument" );
                    firstParent.getChildren().add( importObject );
                }
            }
        }
    }

    /**
     * Instantiates a new settings routine.
     */
    private ImportCB2Routine() {
        // private constructor to hide implicit public one.
    }

}
