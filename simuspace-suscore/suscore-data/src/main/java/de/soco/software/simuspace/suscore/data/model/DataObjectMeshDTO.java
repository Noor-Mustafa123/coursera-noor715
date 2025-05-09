package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.DataObjectMeshEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectMeshDTO.
 */
public class DataObjectMeshDTO extends DataObjectFileDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 8246027933028546288L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectMeshEntity ENTITY_CLASS = new DataObjectMeshEntity();

    /**
     * The preview image.
     */
    @UIFormField( name = "previewImage", title = "3000099x4", orderNum = 2 )
    @UIColumn( data = "previewImage", name = "previewImage", filter = "text", renderer = "text", title = "3000099x4", orderNum = 2 )
    private String previewImage;

    /**
     * Prepare data object mesh entity.
     *
     * @return the data object mesh entity
     */
    @Override
    public DataObjectMeshEntity prepareEntity( String userId ) {
        DataObjectMeshEntity entity = new DataObjectMeshEntity();
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
     * Prepare data object mesh DTO.
     *
     * @param entity
     *         the entity
     *
     * @return the data object mesh DTO
     */
    public DataObjectMeshDTO prepareDataObjectMeshDTO( DataObjectMeshEntity entity ) {
        DataObjectMeshDTO dto = new DataObjectMeshDTO();
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

}
