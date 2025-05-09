package de.soco.software.simuspace.suscore.common.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import java.io.Serial;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A mapping class for the document related DML operations.
 *
 * @author Ahmar Nadeem
 */
@Getter
@Setter
@NoArgsConstructor
public class DocumentEntity extends SuSEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -3982755888499063538L;

    /**
     * The file name.
     */
    @Column( name = "file_name" )
    private String fileName;

    /**
     * The file type.
     */
    @Column( name = "file_type" )
    private String fileType;

    /**
     * file extension.
     */
    @Column( name = "file_ext" )
    private String fileExt;

    /**
     * file size.
     */
    @Column( name = "file_size" )
    private long fileSize;

    /**
     * file size.
     */
    @Column( name = "file_path" )
    private String filePath;

    /**
     * file origin.
     */
    @Column( name = "agent" )
    private String agent = "browser";

    /**
     * file format.
     */
    @Column( name = "format" )
    private String format;

    /**
     * flag to check if it's a temporary document.
     */
    @Column( name = "is_temp" )
    private Boolean isTemp;

    /**
     * the properties of the document.
     */
    @Column( name = "properties" )
    private String properties;

    /**
     * expiry of the document.
     */
    @Column( name = "expiry" )
    private int expiry;

    /**
     * The encoding.
     */
    @Column( name = "file_encoding" )
    private String encoding;

    /**
     * The hash.
     */
    @Column( name = "file_hash" )
    private String hash;

    /**
     * The location.
     */
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "document_location" )
    private List< LocationEntity > locations;

    /**
     * Instantiates a new document entity.
     *
     * @param versionPrimaryKey
     *         the version primary key
     */
    public DocumentEntity( VersionPrimaryKey versionPrimaryKey ) {
        this.setComposedId( versionPrimaryKey );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( agent == null ) ? 0 : agent.hashCode() );
        result = prime * result + ( ( encoding == null ) ? 0 : encoding.hashCode() );
        result = prime * result + expiry;
        result = prime * result + ( ( fileExt == null ) ? 0 : fileExt.hashCode() );
        result = prime * result + ( ( fileName == null ) ? 0 : fileName.hashCode() );
        result = prime * result + ( ( filePath == null ) ? 0 : filePath.hashCode() );
        result = prime * result + ( int ) ( fileSize ^ ( fileSize >>> 32 ) );
        result = prime * result + ( ( fileType == null ) ? 0 : fileType.hashCode() );
        result = prime * result + ( ( format == null ) ? 0 : format.hashCode() );
        result = prime * result + ( ( hash == null ) ? 0 : hash.hashCode() );
        result = prime * result + ( ( isTemp == null ) ? 0 : isTemp.hashCode() );
        result = prime * result + ( ( properties == null ) ? 0 : properties.hashCode() );
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( !super.equals( obj ) ) {
            return false;
        }
        return !( obj instanceof DocumentEntity );

    }

}
