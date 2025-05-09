package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.DataObjectPDFEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectPDFDTO.
 */
public class DataObjectPDFDTO extends DataObjectFileDTO {

    /**
     * The Constant serialVersionUID.
     */

    private static final long serialVersionUID = 1L;

    /**
     * The entity class.
     */
    private static final DataObjectPDFEntity ENTITY_CLASS = new DataObjectPDFEntity();

    /**
     * Instantiates a new data object PDF DTO.
     */
    public DataObjectPDFDTO() {
    }

    /**
     * Instantiates a new data object PDF DTO.
     *
     * @param dataObjectDTO
     *         the data object DTO
     */
    public DataObjectPDFDTO( DataObjectDTO dataObjectDTO ) {
        super( dataObjectDTO );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectPDFEntity prepareEntity( String userId ) {
        DataObjectPDFEntity entity = new DataObjectPDFEntity();
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
        return super.hashCode();
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
        return obj instanceof DataObjectPDFDTO;
    }

}
