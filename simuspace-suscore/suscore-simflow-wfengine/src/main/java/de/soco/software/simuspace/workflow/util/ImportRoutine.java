package de.soco.software.simuspace.workflow.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.suscore.data.common.model.ProjectConfiguration;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;

/**
 * The class is responsible for filter out directory contents on the behalf of the user information file "Settings.ini".
 *
 * @author M.Nasir.Farooq
 */
public class ImportRoutine {

    /**
     * The inner class is used for scanning data objects with scanning information and will prepare the zip data.
     */
    public static class DataObjectScanner extends SimpleFileVisitor< Path > {

        /**
         * The Constant FORWARD_SLASH.
         */
        private static final String FORWARD_SLASH = "/";

        /**
         * The Constant ENTITY_CLASS_FIELD_NAME.
         */
        private static final String ENTITY_CLASS_FIELD_NAME = "ENTITY_CLASS";

        /**
         * The Constant CUSTOM_ATTRIBUTES_KEY.
         */
        static final String CUSTOM_ATTRIBUTES_KEY = "CustomAttributes";

        /**
         * The settings info for scanning import folder.
         */
        private final ImportSettings settingsInfo;

        /**
         * The project configuraiton.
         */
        private final ProjectConfiguration projectConfiguration;

        /**
         * The structures.
         */
        private final List< ImportObject > structures = new ArrayList<>();

        /**
         * The all custom attributes from file.
         */
        private java.util.Map< String, Object > allCustomAttributesFromFile = null;

        /**
         * Instantiates a new data object scanner.
         *
         * @param settingsInfo
         *         the settings info
         * @param projectConfiguration
         *         the project configuration
         */
        private DataObjectScanner( ImportSettings settingsInfo, ProjectConfiguration projectConfiguration ) {
            super();
            this.settingsInfo = settingsInfo;
            this.projectConfiguration = projectConfiguration;
        }

        /**
         * Gets the files by extension from directory.
         *
         * @param dir
         *         the dir
         *
         * @return the files by extension from directory
         */
        private List< ImportObject > getFilesByExtensionFromDirectory( Path dir ) {

            final List< ImportObject > childs = new ArrayList<>();

            final File[] allFilesInDir = dir.toFile().listFiles();

            for ( final File file : allFilesInDir ) {
                prepareImportObject( dir, childs, file );

            }

            return childs;
        }

        /**
         * Checks if is slashed wild card matched.
         *
         * @param dir
         *         the dir
         * @param file
         *         the file
         * @param extensionWild
         *         the extension wild
         *
         * @return true, if is slashed wild card matched
         */
        private boolean isSlashedWildCardMatched( Path dir, final File file, final String extensionWild ) {
            boolean slashedWildFulfilled = false;

            if ( extensionWild.contains( FORWARD_SLASH ) ) {
                final String folderzWild = extensionWild.substring( 0, extensionWild.lastIndexOf( FORWARD_SLASH ) );
                final String filezWild = extensionWild.substring( extensionWild.lastIndexOf( FORWARD_SLASH ) + 1 );
                final String folderRegex = StringUtils.replaceWildCardWithRegex( folderzWild );
                final String filezRegex = StringUtils.replaceWildCardWithRegex( filezWild );
                slashedWildFulfilled = dir.toFile().getName().matches( folderRegex ) && file.getName().matches( filezRegex );

            }
            return slashedWildFulfilled;
        }

        /**
         * Prepare import object.
         *
         * @param dir
         *         the dir
         * @param childs
         *         the childs
         * @param file
         *         the file
         */
        private void prepareImportObject( Path dir, final List< ImportObject > childs, final File file ) {
            final ImportObject childObject = new ImportObject();
            childObject.setName( file.getName() );
            childObject.setParent( dir.toFile().getAbsolutePath() );
            childObject.setPath( file.getAbsolutePath() );
            childObject.setFolder( file.isDirectory() );
            String defaultContainerType = ConstantsString.EMPTY_STRING;
            String defaultObjectType = ConstantsString.EMPTY_STRING;

            checkIsIgnore( file, dir, childObject );
            if ( StringUtils.isNullOrEmpty( childObject.getObjectType() ) ) {
                for ( var entry : settingsInfo.entrySet() ) {
                    boolean isFile = false;
                    if ( entry.getKey().equals( CUSTOM_ATTRIBUTES_KEY ) ) {
                        continue;
                    }
                    List< String > entryValue = ( List< String > ) entry.getValue();
                    for ( SuSObjectModel susObject : projectConfiguration.getEntityConfig() ) {
                        if ( susObject.getName().equalsIgnoreCase( entry.getKey() )
                                && !( classInstanceFromConfig( susObject ) instanceof ContainerEntity ) ) {
                            isFile = true;
                            break;
                        }
                    }

                    for ( final String extensionWild : entryValue ) {
                        if ( isSlashedWildCardMatched( dir, file, extensionWild ) ) {
                            childObject.setObjectType( entry.getKey() );
                            break;
                        }
                    }
                    for ( final String extensionWild : entryValue ) {
                        final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );
                        boolean nameMatch = file.getName().equals( extensionWild );
                        boolean regexMatch = false;
                        if ( !nameMatch ) {
                            regexMatch = file.getName().matches( extensionRegex );
                        }
                        if ( ( nameMatch || regexMatch ) && StringUtils.isNullOrEmpty( childObject.getObjectType() )
                                && ( ( !isFile && childObject.isFolder() ) || ( isFile && !childObject.isFolder() ) ) ) {
                            childObject.setObjectType( entry.getKey() );
                            break;
                        } else if ( extensionWild.contentEquals( DEFAULT_DIR_KEY ) ) {
                            defaultContainerType = entry.getKey();
                        } else if ( extensionWild.contentEquals( DEFAULT_FILE_KEY ) ) {
                            defaultObjectType = entry.getKey();
                        }

                    }
                }
            }

            if ( StringUtils.isNullOrEmpty( childObject.getObjectType() ) && file.isDirectory() ) {
                childObject.setObjectType( defaultContainerType );
            } else if ( StringUtils.isNullOrEmpty( childObject.getObjectType() ) && file.isFile() ) {
                childObject.setObjectType( defaultObjectType );
            }
            if ( childObject.getObjectType().equals( CUSTOM_ATTRIBUTES_KEY ) ) {
                return;
            }
            childs.add( childObject );
        }

        /**
         * Class instance from config.
         *
         * @param suSObjectModel
         *         the su S object model
         *
         * @return the object
         */
        private static Object classInstanceFromConfig( SuSObjectModel suSObjectModel ) {
            Class< ? > entityClass = ReflectionUtils
                    .getFieldByName( initializeObject( suSObjectModel.getClassName() ).getClass(), ENTITY_CLASS_FIELD_NAME ).getType();
            return initializeObject( entityClass.getName() );
        }

        /**
         * Initialize object.
         *
         * @param className
         *         the class name
         *
         * @return the object
         */
        private static Object initializeObject( String className ) {
            try {
                return Class.forName( className ).getDeclaredConstructor().newInstance();
            } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException
                      | NoSuchMethodException e ) {
                ExceptionLogger.logException( e, ImportCB2Routine.class );
                throw new SusException(
                        MessageBundleFactory.getDefaultMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ) );
            }
        }

        /**
         * Check is ignore.
         *
         * @param file
         *         the file
         * @param childObject
         *         the child object
         */
        private void checkIsIgnore( final File file, Path dir, final ImportObject childObject ) {
            for ( final Entry< String, Object > entry : settingsInfo.entrySet() ) {
                if ( entry.getKey().equalsIgnoreCase( IGNORE ) ) {
                    List< String > entryValue = ( List< String > ) entry.getValue();
                    for ( final String extensionWild : entryValue ) {
                        if ( isSlashedWildCardMatched( dir, file, extensionWild ) ) {
                            childObject.setObjectType( entry.getKey() );
                            break;
                        }
                    }
                    for ( final String extensionWild : entryValue ) {
                        final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );
                        if ( file.getName().matches( extensionRegex ) && StringUtils.isNullOrEmpty( childObject.getObjectType() ) ) {
                            childObject.setObjectType( entry.getKey() );
                            break;
                        }
                    }
                }
            }
        }

        /* (non-Javadoc)
         * @see java.nio.file.SimpleFileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
         */
        @Override
        public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) {

            final List< ImportObject > childs = getFilesByExtensionFromDirectory( dir );
            structures.addAll( childs );

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

        /**
         * Gets the all custom attributes from file.
         *
         * @return the all custom attributes from file
         */
        public Map< String, Object > getAllCustomAttributesFromFile() {
            return allCustomAttributesFromFile;
        }

        /**
         * Sets the all custom attributes from file.
         *
         * @param allCustomAttributesFromFile
         *         the all custom attributes from file
         */
        public void setAllCustomAttributesFromFile( Map< String, Object > allCustomAttributesFromFile ) {
            this.allCustomAttributesFromFile = allCustomAttributesFromFile;
        }

    }

    /**
     * The Constant DEFAULT_FILE_KEY.
     */
    private static final String DEFAULT_FILE_KEY = "_default_file_";

    /**
     * The Constant DEFAULT_DIR_KEY.
     */
    private static final String DEFAULT_DIR_KEY = "_default_dir_";

    /**
     * The Constant IGNORE.
     */
    public static final String IGNORE = "Ignore";

    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * Populate tree structure.
     *
     * @param structures
     *         the structures
     * @param parent
     *         the parent
     */
    private static void populateTreeStructure( List< ImportObject > structures, ImportObject parent,
            ProjectConfiguration projectConfiguration, Map< String, Object > allCustomAttributesFromFile ) throws SusException {

        final List< ImportObject > childs = new ArrayList<>();
        for ( final ImportObject folderStructure : structures ) {

            if ( folderStructure.getParent().contentEquals( parent.getPath() ) ) {
                childs.add( folderStructure );
            }
        }

        parent.setChildren( childs );
        prepareCustomAttributesForImportObject( parent, projectConfiguration, allCustomAttributesFromFile );

        for ( final ImportObject folderStructure : parent.getChildren() ) {
            populateTreeStructure( structures, folderStructure, projectConfiguration, allCustomAttributesFromFile );
        }
    }

    /**
     * Prepare custom attributes for import object.
     *
     * @param importObject
     *         the import object
     * @param projectConfiguration
     *         the project configuration
     * @param allCustomAttributesFromFile
     *         the all custom attributes from file
     *
     * @throws SusException
     *         the sus exception
     */
    private static void prepareCustomAttributesForImportObject( ImportObject importObject, ProjectConfiguration projectConfiguration,
            Map< String, Object > allCustomAttributesFromFile ) throws SusException {
        List< String > warningMessages = new ArrayList<>();
        if ( null != allCustomAttributesFromFile ) {
            for ( var customAttributes : allCustomAttributesFromFile.entrySet() ) {
                boolean match;
                String key = customAttributes.getKey();
                if ( key.contains( ConstantsString.ASTERISK ) ) {
                    String keyRegex = StringUtils.replaceWildCardWithRegex( customAttributes.getKey() );
                    match = importObject.getPath().contains( keyRegex );
                } else {
                    match = ( importObject.getPath() ).endsWith( key );
                }
                if ( match ) {

                    SuSObjectModel importObjectModel = projectConfiguration.getEntityConfig().stream()
                            .filter( suSObjectModel -> suSObjectModel.getName().equals( importObject.getObjectType() ) ).findFirst()
                            .orElse( null );
                    if ( importObjectModel == null ) {
                        warningMessages.add( "Configuration must be defined to import Custom Attributes. No Configuration found for "
                                + importObject.getName() );
                        return;
                    }
                    if ( importObjectModel.getCustomAttributes() == null || importObjectModel.getCustomAttributes().isEmpty() ) {
                        warningMessages.add( "Custom Attributes are not defined in " + projectConfiguration.getName() + " for object type "
                                + importObject.getObjectType() + " for import object " + importObject.getName() );
                        return;
                    }
                    importObject.setCustomAttributes( ( Map< String, Object > ) customAttributes.getValue() );
                    importObjectModel.getCustomAttributes().stream()
                            .filter( customAttributeDTO -> customAttributeDTO.getRules() != null
                                    && customAttributeDTO.getRules().get( "required" ) != null
                                    && customAttributeDTO.getRules().get( "required" ).equals( Boolean.TRUE ) )
                            .forEach( customAttributeDTO -> {
                                if ( importObject.getCustomAttributes() == null
                                        || importObject.getCustomAttributes().get( customAttributeDTO.getName() ) == null ) {
                                    throw new SusException( MessagesUtil.getMessage( WFEMessages.REQUIRED_CUSTOM_ATTRIBUTE_IS_MISSING_FOR,
                                            customAttributeDTO.getName(), importObject.getName() ) );
                                }
                            } );
                }
            }
        }
        if ( !warningMessages.isEmpty() ) {
            wfLogger.warn( warningMessages.toString() );
        }
    }

    /**
     * Scan directory with import settings and prepare import objects.
     *
     * @param sourcePath
     *         the source path
     * @param settingsFilePath
     *         the settings file path
     * @param projectConfiguration
     *         the project configuration
     *
     * @return the import object
     */
    public static ImportObject scanDirectoryWithImportSettingsAndPrepareImportObjects( String sourcePath, String settingsFilePath,
            ProjectConfiguration projectConfiguration ) {
        try ( InputStream settingFileStream = new FileInputStream( settingsFilePath ) ) {
            // resolve ending slahs bug in Import WFE
            sourcePath = Paths.get( sourcePath ).toFile().getAbsolutePath();
            final ImportSettings contentInfos = JsonUtils.jsonStreamToObject( settingFileStream, ImportSettings.class );
            final ImportRoutine.DataObjectScanner visitor = new ImportRoutine.DataObjectScanner( contentInfos, projectConfiguration );
            Files.walkFileTree( Paths.get( sourcePath ), visitor );
            final Path dir = Paths.get( sourcePath );
            final ImportObject firstParent = new ImportObject();
            firstParent.setName( dir.toFile().getName() );
            firstParent.setParent( dir.getParent().toFile().getAbsolutePath() );
            firstParent.setFolder( true );
            firstParent.setPath( sourcePath );
            String defaultContainerType = null;
            for ( final Entry< String, Object > entry : contentInfos.entrySet() ) {

                if ( entry.getKey().equals( DataObjectScanner.CUSTOM_ATTRIBUTES_KEY ) ) {
                    visitor.setAllCustomAttributesFromFile( ( Map< String, Object > ) entry.getValue() );
                } else {
                    List< String > entryValue = ( List< String > ) entry.getValue();
                    for ( final String extensionWild : entryValue ) {
                        final String extensionRegex = StringUtils.replaceWildCardWithRegex( extensionWild );

                        if ( firstParent.getName().matches( extensionRegex ) ) {
                            firstParent.setObjectType( entry.getKey() );
                            break;
                        } else if ( extensionWild.contentEquals( DEFAULT_DIR_KEY ) ) {
                            defaultContainerType = entry.getKey();
                        }
                    }

                }
            }
            if ( StringUtils.isNullOrEmpty( firstParent.getObjectType() ) ) {
                firstParent.setObjectType( defaultContainerType );
            }
            visitor.structures.add( firstParent );
            populateTreeStructure( visitor.structures, firstParent, projectConfiguration, visitor.getAllCustomAttributesFromFile() );
            return firstParent;
        } catch ( final SusException | IOException e ) {
            ExceptionLogger.logException( e, ImportCB2Routine.class );
            throw new SusRuntimeException( e.getMessage() );
        }
    }

    /**
     * Instantiates a new settings routine.
     */
    private ImportRoutine() {
        // private constructor to hide implicit public one.
    }

}
