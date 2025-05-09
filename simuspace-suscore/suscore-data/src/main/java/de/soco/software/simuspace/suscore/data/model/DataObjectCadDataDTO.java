package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.DataObjectCadDataEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectCadDataDTO.
 */
public class DataObjectCadDataDTO extends DataObjectFileDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -4656446954986898656L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectCadDataEntity ENTITY_CLASS = new DataObjectCadDataEntity();

    /**
     * The preview image.
     */
    @UIFormField( name = "previewImage", title = "3000099x4", orderNum = 2 )
    @UIColumn( data = "previewImage", name = "previewImage", filter = "text", renderer = "text", title = "3000099x4", orderNum = 2 )
    private String previewImage;

    /**
     * Prepare data object cad data entity.
     *
     * @return the data object cad data entity
     */
    @Override
    public DataObjectCadDataEntity prepareEntity( String userId ) {
        DataObjectCadDataEntity entity = new DataObjectCadDataEntity();
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
        entity.setPreviewImage( getPreviewImage() );
        entity.setJobId( getJobId() );
        return entity;
    }

    /**
     * Prepare data object cad data DTO.
     *
     * @param entity
     *         the entity
     *
     * @return the data object cad data DTO
     */
    public DataObjectCadDataDTO prepareDataObjectCadDataDTO( DataObjectCadDataEntity entity ) {
        DataObjectCadDataDTO dto = new DataObjectCadDataDTO();
        dto.setPreviewImage( entity.getPreviewImage() );
        return dto;
    }

    /**
     * Gets the preview image.
     *
     * @return the preview image
     */
    public String getPreviewImage() {
        return previewImage;
    }

    /**
     * Sets the preview image.
     *
     * @param previewImage
     *         the new preview image
     */
    public void setPreviewImage( String previewImage ) {
        this.previewImage = previewImage;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( previewImage == null ) ? 0 : previewImage.hashCode() );
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
        if ( !( obj instanceof DataObjectCadDataDTO ) ) {
            return false;
        }
        DataObjectCadDataDTO other = ( DataObjectCadDataDTO ) obj;
        if ( previewImage == null ) {
            if ( other.previewImage != null ) {
                return false;
            }
        } else if ( !previewImage.equals( other.previewImage ) ) {
            return false;
        }
        return true;
    }

}
