package de.soco.software.simuspace.suscore.common.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.OSValidator;

/**
 * The Class FileVisitor to visit files.
 *
 * @author Ali Haider
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class FileVisitor implements java.nio.file.FileVisitor< Path > {

    /**
     * The Constant HIDDEN_FILES_REGEX.
     */
    private static final String HIDDEN_FILES_REGEX = "(?s)\\..*";

    /**
     * The files.
     */
    private List< FileObject > files = new ArrayList<>();

    /**
     * The dir only.
     */
    private boolean dirOnly = false;

    /**
     * Instantiates a new local file visitor.
     */
    public FileVisitor() {
        super();
    }

    /**
     * Instantiates a new local file visitor.
     *
     * @param dirOnly
     *         the dir only
     */
    public FileVisitor( boolean dirOnly ) {
        super();
        this.setDirOnly( dirOnly );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) {
        return FileVisitResult.CONTINUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
        FileObject fileObj = new FileObject();

        if ( !Files.exists( file ) ) {
            return FileVisitResult.CONTINUE;
        }

        try {
            BasicFileAttributes attr = Files.readAttributes( file, BasicFileAttributes.class );
            fileObj.setUpdate( attr.lastModifiedTime().toMillis() );
        } catch ( AccessDeniedException e ) {
            return FileVisitResult.CONTINUE;
        }

        fileObj.setSize( Files.size( file ) );
        if ( file.toFile().isDirectory() ) {
            fileObj.setFolder( true );
            fileObj.setLazy( true );
        } else {
            fileObj.setFolder( false );
            fileObj.setLazy( false );
        }
        fileObj.setPath( file.toString().substring( ConstantsInteger.INTEGER_VALUE_ZERO, file.toString().lastIndexOf( getSeperator() ) )
                + getSeperator() );
        fileObj.setTitle( file.toString().substring( file.toString().lastIndexOf( getSeperator() ) + ConstantsInteger.INTEGER_VALUE_ONE ) );

        String fileType = Files.probeContentType( file );
        if ( new File( file.toString() ).isDirectory() ) {
            fileObj.setType( "Directory" );
        } else if ( file.getFileName().toString().matches( HIDDEN_FILES_REGEX ) ) {
            fileObj.setType( "Binary" );
        } else if ( null == fileType ) {
            fileObj.setType( "Unknown" );
        } else {
            fileObj.setType( fileType );
        }

        if ( dirOnly ) {
            if ( file.toFile().isDirectory() ) {
                files.add( fileObj );
            }
        } else {
            files.add( fileObj );
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * Gets the seperator.
     *
     * @return the seperator
     */
    private String getSeperator() {
        String seperator = "";
        if ( OSValidator.isWindows() ) {
            seperator = "\\";
        } else if ( OSValidator.isMac() || OSValidator.isUnix() || OSValidator.isSolaris() ) {
            seperator = "/";
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OS_NOT_SUPPORTED.getKey() ) );
        }
        return seperator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult visitFileFailed( Path file, IOException exc ) throws IOException {
        if ( exc instanceof AccessDeniedException ) {
            throw exc;
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult postVisitDirectory( Path dir, IOException exc ) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /**
     * Gets the files.
     *
     * @return the files
     */
    public List< FileObject > getFiles() {
        return files;
    }

    /**
     * Sets the files.
     *
     * @param files
     *         the new files
     */
    public void setFiles( List< FileObject > files ) {
        this.files = files;
    }

    /**
     * Checks if is dir only.
     *
     * @return true, if is dir only
     */
    public boolean isDirOnly() {
        return dirOnly;
    }

    /**
     * Sets the dir only.
     *
     * @param dirOnly
     *         the new dir only
     */
    public void setDirOnly( boolean dirOnly ) {
        this.dirOnly = dirOnly;
    }

}
