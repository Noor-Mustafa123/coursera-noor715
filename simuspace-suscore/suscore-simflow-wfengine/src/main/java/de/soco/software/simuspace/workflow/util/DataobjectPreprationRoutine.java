package de.soco.software.simuspace.workflow.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;

/**
 * The class is responsible for filter out directory contents on the behalf of the user information file "Settings.ini".
 *
 * @author M.Nasir.Farooq
 */
public class DataobjectPreprationRoutine {

    /**
     * The inner class is used for scanning data objects with scanning information and will prepare the zip data.
     */
    private static class DataObjectScanner extends SimpleFileVisitor< Path > {

        /**
         * Pojo class parse able on object file "dataobject.json" understandable for simcore 1.1 to import an object.
         */
        private class DataobjectJSONObject {

            /**
             * The name.
             */
            private String name;

            /**
             * The type.
             */
            private String type;

            /**
             * The attachments.
             */
            private List< String > file;

            /**
             * For CurveSimPost.
             */
            private Map< String, Object > curve;

            /**
             * For ImageSimPost.
             */
            private String image;

            /**
             * For MovieSimPost.
             */
            private String movie;

            /**
             * Instantiates a new data object json.
             */
            public DataobjectJSONObject() {
                super();
            }

            /**
             * @return the curve
             */
            public Map< String, Object > getCurve() {
                return curve;
            }

            /**
             * @return the file
             */
            public List< String > getFile() {
                return file;
            }

            /**
             * @return the image
             */
            public String getImage() {
                return image;
            }

            /**
             * @return the movie
             */
            public String getMovie() {
                return movie;
            }

            /**
             * @return the name
             */
            public String getName() {
                return name;
            }

            /**
             * @return the type
             */
            public String getType() {
                return type;
            }

            /**
             * Sets the curve.
             *
             * @param curve
             *         the curve to set
             */
            public void setCurve( Map< String, Object > curve ) {
                this.curve = curve;
            }

            /**
             * Sets the attachments.
             *
             * @param attachments
             *         the attachments to set
             */
            public void setFile( List< String > attachments ) {
                file = attachments;
            }

            /**
             * Sets the image.
             *
             * @param image
             *         the image to set
             */
            public void setImage( String image ) {
                this.image = image;
            }

            /**
             * Sets the movie.
             *
             * @param movie
             *         the movie to set
             */
            public void setMovie( String movie ) {
                this.movie = movie;
            }

            /**
             * Sets the name.
             *
             * @param name
             *         the name to set
             */
            public void setName( String name ) {
                this.name = name;
            }

            /**
             * Sets the type.
             *
             * @param type
             *         the type to set
             */
            public void setType( String type ) {
                this.type = type;
            }

        }

        /**
         * The Constant MINUM_NUMBER_OF_POINTS_XYZ_QUADRANTS.
         */
        private static final int MINUM_NUMBER_OF_POINTS_XYZ_QUADRANTS = 2;

        /**
         * The Constant DATAOBJECT_FILE_NAME.
         */
        private static final String DATAOBJECT_FILE_NAME = "dataobject.json";

        /**
         * The Constant INDEX_OF_POINT_X_AXIS_VALUE.
         */
        private static final int INDEX_OF_POINT_X_AXIS_VALUE = 0;

        /**
         * The Constant INDEX_OF_X_DIMENSION_VALUE.
         */
        private static final int INDEX_OF_X_DIMENSION_VALUE = 0;

        /**
         * The Constant SEPARATION_BETWEEN_CURVE_POINTS.
         */
        private static final String SEPARATION_BETWEEN_CURVE_POINTS = ";";

        /**
         * The Constant MAX_LENGTH_FOR_CURVE_S_SINGLE_POINT.
         */
        private static final int MAX_LENGTH_FOR_CURVE_S_SINGLE_POINT = 4;

        /**
         * The Constant KEY_CURVE_USED_FOR_DETECTING_CURVE_TYPE_OBJECT.
         */
        private static final String KEY_CURVE_USED_FOR_DETECTING_CURVE_TYPE_OBJECT = "curve";

        /**
         * The Constant KEY_MOVIE_USED_FOR_DETECTING_IMAGE_TYPE_OBJECT.
         */
        private static final String KEY_IMAGE_USED_FOR_DETECTING_IMAGE_TYPE_OBJECT = "image";

        /**
         * The Constant KEY_MOVIE_USED_FOR_DETECTING_MOVIE_TYPE_OBJECT.
         */
        private static final String KEY_MOVIE_USED_FOR_DETECTING_MOVIE_TYPE_OBJECT = "movie";

        /**
         * The Constant MAP_KEY_HAVING_SECOND_CURVE_DATA.
         */
        private static final String MAP_KEY_HAVING_SECOND_CURVE_DATA = "secondCurveData";

        /**
         * The Constant MAP_KEY_HAVING_FIRST_CURVE_DATA.
         */
        private static final String MAP_KEY_HAVING_FIRST_CURVE_DATA = "firstCurveData";

        /**
         * The Constant CURVE_OBJECT_KEY_HAVING_DRAWABLE_DATA.
         */
        private static final String CURVE_OBJECT_KEY_HAVING_DRAWABLE_DATA_VALUE = "data";

        /**
         * The Constant CURVE_OBJECT_KEY_HAVING_UNIT_OF_Y_AXIS.
         */
        private static final String CURVE_OBJECT_KEY_WILL_HAVE_UNIT_OF_Y_AXIS_VALUE = "yUnit";

        /**
         * The Constant CURVE_OBJECT_KEY_HAVING_Y_DIMENSION_VALUE.
         */
        private static final String CURVE_OBJECT_KEY_WILL_HAVE_Y_DIMENSION_VALUE = "yDimension";

        /**
         * The Constant CURVE_OBJECT_KEY_HAVING_UNIT_OF_X_AXIS.
         */
        private static final String CURVE_OBJECT_KEY_WILL_HAVE_UNIT_OF_X_AXIS_VALUE = "xUnit";

        /**
         * The Constant CURVE_OBJECT_KEY_HAVING_X_DIMENSION_VALUE.
         */
        private static final String CURVE_OBJECT_KEY_WILL_HAVE_X_DIMENSION_VALUE = "xDimension";

        /**
         * The Constant DEFAULT_FOLDER_NAME_FROM_SETTINGS.
         */
        private static final String KEY_FOR_DEFAULT_FOLDER_FROM_SETTING_FILE = "default";

        /**
         * The Constant INDEX_OF_X_UNIT_VALUE.
         */
        private static final int INDEX_OF_X_UNIT_VALUE = 1;

        /**
         * The Constant INDEX_OF_Y_DIMENSION_VALUE.
         */
        private static final int INDEX_OF_Y_DIMENSION_VALUE = 2;

        /**
         * The Constant INDEX_OF_Y_UNIT_VALUE.
         */
        private static final int INDEX_OF_Y_UNIT_VALUE = 3;

        /**
         * The Constant INDEX_OF_POINT_Y_AXIS_VALUE.
         */
        private static final int INDEX_OF_POINT_Y_AXIS_VALUE = 1;

        /**
         * The Constant INDEX_OF_POINT_Z_AXIS_VALUE.
         */
        private static final int INDEX_OF_POINT_Z_AXIS_VALUE = 2;

        /**
         * The settings info for scanning import folder.
         */
        private final List< FolderContentInfo > settingsInfo;

        /**
         * The zip out stream.
         */
        private final ZipOutputStream zipOutStream;

        /**
         * Instantiates a new data object scanner.
         *
         * @param settingsInfo
         *         the settings info
         * @param zipOutStream
         *         the zip out stream
         */
        private DataObjectScanner( List< FolderContentInfo > settingsInfo, ZipOutputStream zipOutStream ) {
            super();
            this.settingsInfo = settingsInfo;
            this.zipOutStream = zipOutStream;

        }

        /**
         * Adds the data object files to zip output stream object.
         *
         * @param folderName
         *         the folder name
         * @param files
         *         the files
         * @param jsonNode
         *         the json node
         */
        public void addDataObjectFilesToZipOutputStream( String folderName, List< File > files, String jsonNode ) {

            try {
                if ( files == null ) {
                    return;
                }
                for ( final File file : files ) {

                    if ( file.isFile() ) {
                        final ZipEntry zipEntry = new ZipEntry( folderName + File.separator + file.getName() );
                        zipEntry.setTime( file.lastModified() );
                        zipOutStream.putNextEntry( zipEntry );
                        try ( FileInputStream fis = new FileInputStream( file.getPath() ) ) {
                            IOUtils.copy( fis, zipOutStream );
                        }
                        zipOutStream.closeEntry();

                    }
                }
                final ZipEntry dataobjectJson = new ZipEntry( folderName + File.separator + DATAOBJECT_FILE_NAME );
                dataobjectJson.setTime( DateUtils.getCurrentTime() );
                zipOutStream.putNextEntry( dataobjectJson );
                zipOutStream.write( jsonNode.getBytes() );
                zipOutStream.closeEntry();

            } catch ( final PatternSyntaxException | IOException e ) {
                ExceptionLogger.logException( e, getClass() );
            }
        }

        /**
         * Gets the content folder info of defaults.
         *
         * @return the content folder info of defaults
         */
        private List< FolderContentInfo > getContentFolderInfoOfDefaults() {
            List< FolderContentInfo > contentInfos = null;
            try {
                contentInfos = settingsInfo.stream().filter( t -> t.getFolder().contentEquals( KEY_FOR_DEFAULT_FOLDER_FROM_SETTING_FILE ) )
                        .toList();
            } catch ( final NoSuchElementException e ) {
                ExceptionLogger.logException( e, getClass() );
            }
            return contentInfos;
        }

        /**
         * Gets the file names from file objects.
         *
         * @param filesOfSingleDataObect
         *         the files of single data obect
         *
         * @return the file names from file objects
         */
        private List< String > getFileNamesFromFileObjects( List< File > filesOfSingleDataObect ) {
            final List< String > fileNames = new ArrayList<>();

            for ( final File file : filesOfSingleDataObect ) {
                fileNames.add( file.getName() );
            }

            return fileNames;
        }

        /**
         * Gets the files by extension from directory.
         *
         * @param dir
         *         the dir
         * @param folderObject
         *         the folder object
         *
         * @return the files by extension from directory
         */
        private List< File > getFilesByExtensionFromDirectory( Path dir, final FolderObject folderObject ) {

            final List< File > filesOfSingleDataObect = new ArrayList<>();

            for ( final String extensionWild : folderObject.getExtensions() ) {

                final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );

                try {
                    final File[] files = dir.toFile().listFiles( ( directory, name ) -> name.matches( extensionRegex ) );

                    filesOfSingleDataObect.addAll( Arrays.asList( files ) );
                } catch ( final PatternSyntaxException e ) {
                    ExceptionLogger.logException( e, getClass() );
                }

            }

            return filesOfSingleDataObect;
        }

        /**
         * Gets the folder content info by folder name.
         *
         * @param folderName
         *         the folder name
         *
         * @return the folder content info by folder name
         */
        private FolderContentInfo getFolderContentInfoByFolderName( String folderName ) {
            FolderContentInfo contentInfo = null;
            try {
                contentInfo = settingsInfo.stream().filter( t -> t.getFolder().contentEquals( folderName ) ).findFirst().get();
            } catch ( final NoSuchElementException e ) {
                ExceptionLogger.logException( e, getClass() );
            }
            return contentInfo;
        }

        /**
         * Handle for curve objects.
         *
         * @param objectName
         *         the object name
         * @param objectType
         *         the object type
         * @param filesOfSingleDataObect
         *         the files of single data obect
         */
        private void handleForCurveObjects( String objectName, String objectType, List< File > filesOfSingleDataObect ) {
            for ( final File file : filesOfSingleDataObect ) {
                Map< String, Map< String, Object > > twoCurvesData = new HashMap<>();
                final DataobjectJSONObject dataobjectJson = new DataobjectJSONObject();
                dataobjectJson.setName( objectName );
                dataobjectJson.setType( objectType );
                try {
                    twoCurvesData = prepareCurvesFromCSV( file.getPath() );
                } catch ( final SusException e ) {
                    ExceptionLogger.logException( e, getClass() );
                }

                dataobjectJson.setCurve( twoCurvesData.get( MAP_KEY_HAVING_FIRST_CURVE_DATA ) );

                try {
                    addDataObjectFilesToZipOutputStream( objectName + objectType + DateUtils.getCurrentTime(), new ArrayList<>(),
                            JsonUtils.objectToJson( dataobjectJson ) );
                } catch ( final JsonSerializationException e ) {
                    ExceptionLogger.logException( e, getClass() );
                }

                if ( twoCurvesData.containsKey( MAP_KEY_HAVING_SECOND_CURVE_DATA ) ) {
                    dataobjectJson.setCurve( twoCurvesData.get( MAP_KEY_HAVING_SECOND_CURVE_DATA ) );

                    try {
                        addDataObjectFilesToZipOutputStream( objectName + objectType + DateUtils.getCurrentTime(), new ArrayList<>(),
                                JsonUtils.objectToJson( dataobjectJson ) );
                    } catch ( final JsonSerializationException e ) {
                        ExceptionLogger.logException( e, getClass() );
                    }
                }
            }

        }

        /**
         * Handle for image and movie objects.
         *
         * @param objectName
         *         the object name
         * @param objectType
         *         the object type
         * @param filesOfSingleDataObect
         *         the files of single data obect
         */
        private void handleForImageAndMovieObjects( String objectName, String objectType, List< File > filesOfSingleDataObect ) {

            for ( final File file : filesOfSingleDataObect ) {
                final DataobjectJSONObject dataobjectJson = new DataobjectJSONObject();
                if ( objectType.contains( KEY_IMAGE_USED_FOR_DETECTING_IMAGE_TYPE_OBJECT ) ) {
                    final String image = file.getName();
                    dataobjectJson.setImage( image );

                } else if ( objectType.contains( KEY_MOVIE_USED_FOR_DETECTING_MOVIE_TYPE_OBJECT ) ) {
                    final String movie = file.getName();
                    dataobjectJson.setMovie( movie );
                }
                dataobjectJson.setName( objectName );
                dataobjectJson.setType( objectType );

                try {
                    addDataObjectFilesToZipOutputStream( objectName + objectType + DateUtils.getCurrentTime(),
                            Collections.singletonList( file ), JsonUtils.objectToJson( dataobjectJson ) );
                } catch ( final JsonSerializationException e ) {
                    ExceptionLogger.logException( e, getClass() );
                }

            }

        }

        /**
         * Prepare two curves from CSV file.
         *
         * @param filePath
         *         the file path
         *
         * @return the map
         */
        private Map< String, Map< String, Object > > prepareCurvesFromCSV( String filePath ) {
            final Map< String, Map< String, Object > > twoCurvesData = new HashMap<>();
            final Map< String, Object > firstCurveObj = new HashMap<>();
            final Map< String, Object > secondCurveObj = new HashMap<>();

            try ( FileReader fr = new FileReader( filePath ); BufferedReader bufferedReader = new BufferedReader( fr ) ) {

                // First line contains the info about the curve dimensions and their units.

                final String firstLineHavingdimensionAndUnit = bufferedReader.readLine();
                if ( StringUtils.isNotNullOrEmpty( firstLineHavingdimensionAndUnit ) ) {
                    final String[] dimentsionTokens = firstLineHavingdimensionAndUnit.split( SEPARATION_BETWEEN_CURVE_POINTS );
                    if ( dimentsionTokens.length != MAX_LENGTH_FOR_CURVE_S_SINGLE_POINT ) {
                        throw new SusException( MessagesUtil.getMessage( WFEMessages.CSV_DO_NOT_HAVE_VALID_DIMENSIONS_AND_UNITS ) );
                    }

                    firstCurveObj.put( CURVE_OBJECT_KEY_WILL_HAVE_X_DIMENSION_VALUE, dimentsionTokens[ INDEX_OF_X_DIMENSION_VALUE ] );
                    firstCurveObj.put( CURVE_OBJECT_KEY_WILL_HAVE_UNIT_OF_X_AXIS_VALUE, dimentsionTokens[ INDEX_OF_X_UNIT_VALUE ] );
                    firstCurveObj.put( CURVE_OBJECT_KEY_WILL_HAVE_Y_DIMENSION_VALUE, dimentsionTokens[ INDEX_OF_Y_DIMENSION_VALUE ] );
                    firstCurveObj.put( CURVE_OBJECT_KEY_WILL_HAVE_UNIT_OF_Y_AXIS_VALUE, dimentsionTokens[ INDEX_OF_Y_UNIT_VALUE ] );

                    secondCurveObj.put( CURVE_OBJECT_KEY_WILL_HAVE_X_DIMENSION_VALUE, dimentsionTokens[ INDEX_OF_X_DIMENSION_VALUE ] );
                    secondCurveObj.put( CURVE_OBJECT_KEY_WILL_HAVE_UNIT_OF_X_AXIS_VALUE, dimentsionTokens[ INDEX_OF_X_UNIT_VALUE ] );
                    secondCurveObj.put( CURVE_OBJECT_KEY_WILL_HAVE_Y_DIMENSION_VALUE, dimentsionTokens[ INDEX_OF_Y_DIMENSION_VALUE ] );
                    secondCurveObj.put( CURVE_OBJECT_KEY_WILL_HAVE_UNIT_OF_Y_AXIS_VALUE, dimentsionTokens[ INDEX_OF_Y_UNIT_VALUE ] );
                }
                // Wasting line having X,Y,Z text
                bufferedReader.readLine();
                String nextLine;
                final List< float[] > curveData = new ArrayList<>();
                final List< float[] > curveData2 = new ArrayList<>();
                while ( ( nextLine = bufferedReader.readLine() ) != null ) {
                    final String[] point = nextLine.split( SEPARATION_BETWEEN_CURVE_POINTS );
                    if ( point.length > MINUM_NUMBER_OF_POINTS_XYZ_QUADRANTS ) {
                        final float x = Float.parseFloat( point[ INDEX_OF_POINT_X_AXIS_VALUE ] );
                        final float y = Float.parseFloat( point[ INDEX_OF_POINT_Y_AXIS_VALUE ] );
                        final float z = Float.parseFloat( point[ INDEX_OF_POINT_Z_AXIS_VALUE ] );
                        curveData.add( new float[]{ x, y } );
                        curveData2.add( new float[]{ x, z } );
                    } else if ( point.length == MINUM_NUMBER_OF_POINTS_XYZ_QUADRANTS ) {
                        final float x = Float.parseFloat( point[ INDEX_OF_POINT_X_AXIS_VALUE ] );
                        final float y = Float.parseFloat( point[ INDEX_OF_POINT_Y_AXIS_VALUE ] );
                        curveData.add( new float[]{ x, y } );
                    }

                }
                firstCurveObj.put( CURVE_OBJECT_KEY_HAVING_DRAWABLE_DATA_VALUE, curveData );
                twoCurvesData.put( MAP_KEY_HAVING_FIRST_CURVE_DATA, firstCurveObj );
                if ( !curveData2.isEmpty() ) {
                    secondCurveObj.put( CURVE_OBJECT_KEY_HAVING_DRAWABLE_DATA_VALUE, curveData2 );
                    twoCurvesData.put( MAP_KEY_HAVING_SECOND_CURVE_DATA, secondCurveObj );
                }
            } catch ( final IOException e ) {
                ExceptionLogger.logException( e, getClass() );
            }

            return twoCurvesData;
        }

        /**
         * Prepare data object json file add to zip.
         *
         * @param objectName
         *         the object name
         * @param objectType
         *         the object type
         * @param filesOfSingleDataObect
         *         the files of single data obect
         */
        private void prepareDataObjectJsonFileAddToZip( final String objectName, final String objectType,
                final List< File > filesOfSingleDataObect ) {

            final List< String > fileNames = getFileNamesFromFileObjects( filesOfSingleDataObect );

            if ( objectType.contains( KEY_MOVIE_USED_FOR_DETECTING_MOVIE_TYPE_OBJECT )
                    || objectType.contains( KEY_IMAGE_USED_FOR_DETECTING_IMAGE_TYPE_OBJECT ) ) {
                handleForImageAndMovieObjects( objectName, objectType, filesOfSingleDataObect );
                return;
            } else if ( objectType.contains( KEY_CURVE_USED_FOR_DETECTING_CURVE_TYPE_OBJECT ) ) {
                handleForCurveObjects( objectName, objectType, filesOfSingleDataObect );
                return;
            }
            final DataobjectJSONObject dataobjectJson = new DataobjectJSONObject();
            dataobjectJson.setName( objectName );
            dataobjectJson.setFile( fileNames );
            dataobjectJson.setType( objectType );

            try {
                addDataObjectFilesToZipOutputStream( objectName + objectType + DateUtils.getCurrentTime(), filesOfSingleDataObect,
                        JsonUtils.objectToJson( dataobjectJson ) );
            } catch ( final JsonSerializationException e ) {
                ExceptionLogger.logException( e, getClass() );
            }
        }

        /* (non-Javadoc)
         * @see java.nio.file.SimpleFileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
         */
        @Override
        public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) {
            final String folderName = dir.getFileName().toString();

            final FolderContentInfo contentInfo = getFolderContentInfoByFolderName( folderName );
            if ( contentInfo == null ) {

                for ( final FolderContentInfo folderContentInfo : getContentFolderInfoOfDefaults() ) {
                    for ( final FolderObject folderObject : folderContentInfo.getObjects() ) {

                        final List< File > filesOfSingleDataObect = getFilesByExtensionFromDirectory( dir, folderObject );
                        if ( !filesOfSingleDataObect.isEmpty() ) {
                            prepareDataObjectJsonFileAddToZip( KEY_FOR_DEFAULT_FOLDER_FROM_SETTING_FILE + folderName,
                                    folderObject.getType(), filesOfSingleDataObect );
                        }
                    }
                }

            } else {

                for ( final FolderObject folderObject : contentInfo.getObjects() ) {

                    final List< File > filesOfSingleDataObect = getFilesByExtensionFromDirectory( dir, folderObject );
                    if ( !filesOfSingleDataObect.isEmpty() ) {
                        prepareDataObjectJsonFileAddToZip( contentInfo.getFolder(), folderObject.getType(), filesOfSingleDataObect );
                    }

                }

            }

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
     * Reads and validates the import routine setting json file.
     *
     * @param jsonFilePath
     *         the json file path
     *
     * @return the JSON array
     */
    public static JSONArray readSettingJSONFile( String jsonFilePath ) {
        final JSONParser parser = new JSONParser();
        JSONArray jsonObjectArray;
        try ( FileInputStream fis = new FileInputStream( jsonFilePath ); InputStreamReader isr = new InputStreamReader( fis ) ) {
            final Object jsonObj = parser.parse( isr );
            jsonObjectArray = ( JSONArray ) jsonObj;
        } catch ( final IOException e ) {
            ExceptionLogger.logException( e, DataobjectPreprationRoutine.class );
            throw new SusRuntimeException( MessagesUtil.getMessage( WFEMessages.JSON_FILE_NOT_FOUND ) );

        } catch ( final ParseException e ) {
            ExceptionLogger.logException( e, DataobjectPreprationRoutine.class );
            throw new SusRuntimeException( MessagesUtil.getMessage( WFEMessages.JSON_FILE_IS_NOT_VALID_JSON ) );
        }
        return jsonObjectArray;
    }

    /**
     * Scan with setting file and prepare import zip.
     *
     * @param sourcePath
     *         the source path
     * @param destZipPath
     *         the dest zip path
     * @param settingsFilePath
     *         the settings file path
     */
    public static void scanWithSettingFileAndPrepareImportZip( String sourcePath, String destZipPath, String settingsFilePath ) {
        if ( readSettingJSONFile( settingsFilePath ) != null ) {
            List< FolderContentInfo > contentInfos;
            try ( FileOutputStream fos = new FileOutputStream( destZipPath ); final ZipOutputStream out = new ZipOutputStream( fos ) ) {
                contentInfos = JsonUtils.jsonFileToObjectList( new File( settingsFilePath ), FolderContentInfo.class );
                final DataobjectPreprationRoutine.DataObjectScanner visitor = new DataobjectPreprationRoutine.DataObjectScanner(
                        contentInfos, out );
                Files.walkFileTree( Paths.get( sourcePath ), visitor );
            } catch ( final JsonSerializationException | IOException e ) {
                ExceptionLogger.logException( e, DataobjectPreprationRoutine.class );
                throw new SusRuntimeException( e.getMessage() );
            }
        }
    }

    /**
     * Validates the provided json settings file i.e either file exists and is valid json.
     *
     * @param jsonFilePath
     *         the json file path
     *
     * @return true, if successful
     */
    public static boolean validateSettings( String jsonFilePath ) {

        boolean validatedAs = true;
        try {
            readSettingJSONFile( jsonFilePath );
        } catch ( final SusRuntimeException e ) {
            ExceptionLogger.logException( e, DataobjectPreprationRoutine.class );
            validatedAs = false;
        }

        return validatedAs;
    }

    /**
     * Instantiates a new settings routine.
     */
    private DataobjectPreprationRoutine() {
        // private constructor to hide implicit public one.
    }

}
