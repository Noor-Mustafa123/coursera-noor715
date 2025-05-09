package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.DataObject3DEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObject3DDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class DataObject3DDTO extends DataObjectValueDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -4161870921246171411L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObject3DEntity ENTITY_CLASS = new DataObject3DEntity();

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.model.DataObjectValueDTO#prepareEntity(java.lang.String)
     */
    @Override
    public DataObject3DEntity prepareEntity( String userId ) {
        DataObject3DEntity entity = new DataObject3DEntity();
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        entity.setCreatedOn( now );
        entity.setModifiedOn( now );
        entity.setName( getName() );
        entity.setValue( getValue() );
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
