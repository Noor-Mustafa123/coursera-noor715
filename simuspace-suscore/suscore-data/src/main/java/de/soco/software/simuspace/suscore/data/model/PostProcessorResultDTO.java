package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.PostProcessorResultEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class PostProcessorResultDTO.
 */
public class PostProcessorResultDTO extends AbstractModelDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2532972600036114713L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final PostProcessorResultEntity ENTITY_CLASS = new PostProcessorResultEntity();

    /**
     * Prepare entity.
     *
     * @param userId
     *         the user id
     *
     * @return the post processor result entity
     */
    public PostProcessorResultEntity prepareEntity( String userId ) {
        PostProcessorResultEntity entity = new PostProcessorResultEntity();
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
