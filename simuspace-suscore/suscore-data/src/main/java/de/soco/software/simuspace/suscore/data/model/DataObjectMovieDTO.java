package de.soco.software.simuspace.suscore.data.model;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.data.entity.DataObjectMovieEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectMovieDTO.
 */
public class DataObjectMovieDTO extends DataObjectFileDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2138429459265738520L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectMovieEntity ENTITY_CLASS = new DataObjectMovieEntity();

    /**
     * The preview image.
     */
    @UIColumn( data = "thumbnailImage.url", name = "thumbnailImage", filter = "image", renderer = "image", title = "3000089x4", isSortable = false, orderNum = 2 )
    private DocumentDTO thumbnailImage;

    /**
     * The preview image.
     */
    private DocumentDTO previewImage;

    /**
     * The thumbnail.
     */
    private String thumbnail;

    /**
     * The poster.
     */
    private String poster;

    /**
     * The sources.
     */
    private MovieSources sources;

    /**
     * Prepare data object movie entity.
     *
     * @return the data object movie entity
     */
    @Override
    public DataObjectMovieEntity prepareEntity( String userId ) {
        DataObjectMovieEntity entity = new DataObjectMovieEntity();
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        entity.setCreatedOn( now );
        entity.setModifiedOn( now );
        entity.setName( getName() );

        entity.setTypeId( getTypeId() );
        entity.setDescription( getDescription() );

        entity.setCustomAttributes( prepareCustomAttributes( entity, getCustomAttributes(), getCustomAttributesDTO() ) );

        // set Status From lifeCycle
        entity.setLifeCycleStatus( ( getLifeCycleStatus() != null ) ? getLifeCycleStatus().getId() : null );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        entity.setOwner( userEntity );

        if ( getFile() != null ) {
            DocumentEntity documentEntity = prepareDocumentEntity();
            entity.setFile( documentEntity );
            entity.setSize( documentEntity.getSize() );
        } else {
            entity.setSize( null );
        }

        if ( getPreviewImage() != null ) {
            entity.setPreviewImage( preparePreviewDocumentEntity() );
        }

        if ( getThumbnailImage() != null ) {
            entity.setThumbnail( prepareThumbnailDocumentEntity() );
        }
        entity.setJobId( getJobId() );
        return entity;
    }

    /**
     * @return the thumnailImage
     */
    public DocumentDTO getThumbnailImage() {
        return thumbnailImage;
    }

    /**
     * @param thumbnailImage
     *         the thumnailImage to set
     */
    public void setThumbnailImage( DocumentDTO thumbnailImage ) {
        this.thumbnailImage = thumbnailImage;
    }

    /**
     * @return the previewImage
     */
    public DocumentDTO getPreviewImage() {
        return previewImage;
    }

    /**
     * @param previewImage
     *         the previewImage to set
     */
    public void setPreviewImage( DocumentDTO previewImage ) {
        this.previewImage = previewImage;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( poster == null ) ? 0 : poster.hashCode() );
        result = prime * result + ( ( sources == null ) ? 0 : sources.hashCode() );
        result = prime * result + ( ( thumbnail == null ) ? 0 : thumbnail.hashCode() );
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
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        DataObjectMovieDTO other = ( DataObjectMovieDTO ) obj;
        if ( poster == null ) {
            if ( other.poster != null ) {
                return false;
            }
        } else if ( !poster.equals( other.poster ) ) {
            return false;
        }
        if ( sources == null ) {
            if ( other.sources != null ) {
                return false;
            }
        } else if ( !sources.equals( other.sources ) ) {
            return false;
        }
        if ( thumbnail == null ) {
            if ( other.thumbnail != null ) {
                return false;
            }
        } else if ( !thumbnail.equals( other.thumbnail ) ) {
            return false;
        }
        return true;
    }

    /**
     * @return the thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail
     *         the thumbnail to set
     */
    public void setThumbnail( String thumbnail ) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return the poster
     */
    public String getPoster() {
        return poster;
    }

    /**
     * @param poster
     *         the poster to set
     */
    public void setPoster( String poster ) {
        this.poster = poster;
    }

    /**
     * @return the sources
     */
    public MovieSources getSources() {
        return sources;
    }

    /**
     * @param sources
     *         the sources to set
     */
    public void setSources( MovieSources sources ) {
        this.sources = sources;
    }

    /**
     * Prepare preview document entity.
     *
     * @return the document entity
     */
    private DocumentEntity preparePreviewDocumentEntity() {
        DocumentEntity documentEntity = new DocumentEntity( UUID.fromString( getPreviewImage().getId() ) );
        documentEntity.setFilePath( getPreviewImage().getPath() );
        documentEntity.setEncoding( FileUtils.UTF8_ENCODING );
        if ( null != getPreviewImage().getEncryptionDecryption() ) {
            EncryptionDecryptionEntity encryptionDecryptionEntity = new EncryptionDecryptionEntity(
                    getPreviewImage().getEncryptionDecryption().getMethod(), getPreviewImage().getEncryptionDecryption().getSalt(),
                    getPreviewImage().getEncryptionDecryption().isActive() );
            if ( null != getPreviewImage().getEncryptionDecryption().getId() ) {
                encryptionDecryptionEntity.setId( UUID.fromString( getPreviewImage().getEncryptionDecryption().getId() ) );
            } else {
                encryptionDecryptionEntity.setId( UUID.randomUUID() );
            }
            documentEntity.setEncryptionDecryption( encryptionDecryptionEntity );
        }
        documentEntity.setEncrypted( getPreviewImage().isEncrypted() );
        documentEntity.setHash( FileUtils.getAdler32CheckSumForVaultFile(
                new File( PropertiesManager.getVaultPath() + getPreviewImage().getPath() ), getPreviewImage().getEncryptionDecryption() ) );
        return documentEntity;
    }

    /**
     * Prepare thumbnail document entity.
     *
     * @return the document entity
     */
    private DocumentEntity prepareThumbnailDocumentEntity() {
        DocumentEntity documentEntity = new DocumentEntity( UUID.fromString( getThumbnailImage().getId() ) );
        documentEntity.setFilePath( getThumbnailImage().getPath() );
        documentEntity.setEncoding( FileUtils.UTF8_ENCODING );
        documentEntity.getEncryptionDecryption().setMethod( getPreviewImage().getEncryptionDecryption().getMethod() );
        documentEntity.setEncrypted( getPreviewImage().isEncrypted() );
        documentEntity.setHash(
                FileUtils.getAdler32CheckSumForVaultFile( new File( PropertiesManager.getVaultPath() + getThumbnailImage().getPath() ),
                        getPreviewImage().getEncryptionDecryption() ) );
        return documentEntity;
    }

}
