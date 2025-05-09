package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.data.entity.DataObjectImageEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectImageDTO.
 */
public class DataObjectImageDTO extends DataObjectFileDTO {

    /**
     * The Constant serialVersionUID.
     */

    private static final long serialVersionUID = 1L;

    /**
     * The entity class.
     */
    private static final DataObjectImageEntity ENTITY_CLASS = new DataObjectImageEntity();

    /**
     * The preview image.
     */
    private DocumentDTO previewImage;

    /**
     * The thumb image.
     */
    @UIColumn( data = "thumbnailImage.url", filter = "", renderer = "image", isSortable = false, title = "3000085x4", name = "thumbnailImage", orderNum = 11 )
    private DocumentDTO thumbnailImage;

    /**
     * Instantiates a new data object image DTO.
     */
    public DataObjectImageDTO() {
    }

    /**
     * Instantiates a new data object image DTO.
     *
     * @param dataObjectDTO
     *         the data object DTO
     */
    public DataObjectImageDTO( DataObjectDTO dataObjectDTO ) {
        super( dataObjectDTO );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectImageEntity prepareEntity( String userId ) {
        DataObjectImageEntity entity = new DataObjectImageEntity();
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

        entity.setJobId( getJobId() );
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( previewImage == null ) ? 0 : previewImage.hashCode() );
        result = prime * result + ( ( thumbnailImage == null ) ? 0 : thumbnailImage.hashCode() );
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
        if ( !( obj instanceof DataObjectImageDTO ) ) {
            return false;
        }
        DataObjectImageDTO other = ( DataObjectImageDTO ) obj;
        if ( previewImage == null ) {
            if ( other.previewImage != null ) {
                return false;
            }
        } else if ( !previewImage.equals( other.previewImage ) ) {
            return false;
        }
        if ( thumbnailImage == null ) {
            if ( other.thumbnailImage != null ) {
                return false;
            }
        } else if ( !thumbnailImage.equals( other.thumbnailImage ) ) {
            return false;
        }
        return true;
    }

    /**
     * Gets the preview image.
     *
     * @return the preview image
     */

    public DocumentDTO getPreviewImage() {

        return previewImage;
    }

    /**
     * Sets the preview image.
     *
     * @param previewImage
     *         the new preview image
     */

    public void setPreviewImage( DocumentDTO previewImage ) {
        this.previewImage = previewImage;
    }

    /**
     * Gets the thumb image.
     *
     * @return the thumb image
     */
    public DocumentDTO getThumbnailImage() {
        return thumbnailImage;
    }

    /**
     * Sets the thumb image.
     *
     * @param thumbnailImage
     *         the new thumb image
     */
    public void setThumbnailImage( DocumentDTO thumbnailImage ) {
        this.thumbnailImage = thumbnailImage;
    }

}
