package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Type;

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
@Entity
@Table( name = "document_entity" )
public class DocumentEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -3982755888499063538L;

    /**
     * id of the object. versioning composite primary key reference
     */
    @Id
    @Column( name = "id" )
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The file name.
     */
    @Column( name = "file_name" )
    private String fileName;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

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
     * The encryptionID.
     */
    @Column( name = "is_encrypted" )
    private Boolean isEncrypted;

    /**
     * The encryptionID.
     */
    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "encryption_decrypttion_id", referencedColumnName = "id" )
    private EncryptionDecryptionEntity encryptionDecryption;

    /**
     * The location.
     */
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "document_location" )
    private List< LocationEntity > locations;

    /**
     * The size.
     */
    @Column( name = "entity_size" )
    private Long entitySize;

    /**
     * owner mapping.
     */
    @ManyToOne
    private UserEntity owner;

    /**
     * Creation date of object.
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * modification date of object.
     */
    @Column( name = "updated_on" )
    private Date modifiedOn;

    /**
     * The meta data document.
     */
    @OneToOne( fetch = FetchType.LAZY )
    private DocumentEntity metaDataDocument;

    /**
     * Instantiates a new document entity.
     *
     * @param id
     *         the version primary key
     */
    public DocumentEntity( UUID id ) {
        this.setId( id );
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public Long getSize() {
        return entitySize;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( Long size ) {
        this.entitySize = size;
    }

    /**
     * Checks if is encrypted.
     *
     * @return true, if is encrypted
     */
    public boolean isEncrypted() {
        return isEncrypted;
    }

    /**
     * Sets the encrypted.
     *
     * @param isEncrypted
     *         the new encrypted
     */
    public void setEncrypted( Boolean isEncrypted ) {
        this.isEncrypted = isEncrypted;
    }

    /**
     * {@inheritDoc}
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

    /**
     * {@inheritDoc}
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
