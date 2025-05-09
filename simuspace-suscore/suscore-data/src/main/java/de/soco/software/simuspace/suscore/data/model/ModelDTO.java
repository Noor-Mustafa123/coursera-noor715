package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.ModelEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class ModelDTO.
 */
public class ModelDTO extends AbstractModelDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -4211833836982363632L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final ModelEntity ENTITY_CLASS = new ModelEntity();

    /**
     * Prepare entity.
     *
     * @param userId
     *         the user id
     *
     * @return the model entity
     */

    public ModelEntity prepareEntity( String userId ) {
        ModelEntity entity = new ModelEntity();
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        entity.setCreatedOn( now );
        entity.setModifiedOn( now );
        entity.setName( getName() );
        entity.setCustomAttributes( prepareCustomAttributes( entity, getCustomAttributes(), getCustomAttributesDTO() ) );
        entity.setTypeId( getTypeId() );
        entity.setName( getName() );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        entity.setOwner( userEntity );
        entity.setJobId( getJobId() );
        return entity;
    }

}
