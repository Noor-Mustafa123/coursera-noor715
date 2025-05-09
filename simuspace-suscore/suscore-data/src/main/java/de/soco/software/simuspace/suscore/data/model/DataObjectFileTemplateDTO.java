package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.DataObjectFileTemplateEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectFileTemplateDTO.
 */
public class DataObjectFileTemplateDTO extends DataObjectFileDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -56657333491442891L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectFileTemplateEntity ENTITY_CLASS = new DataObjectFileTemplateEntity();

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.model.DataObjectDTO#prepareEntity(java.lang.String)
     */
    @Override
    public DataObjectFileTemplateEntity prepareEntity( String userId ) {
        DataObjectFileTemplateEntity entity = new DataObjectFileTemplateEntity();
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

}
