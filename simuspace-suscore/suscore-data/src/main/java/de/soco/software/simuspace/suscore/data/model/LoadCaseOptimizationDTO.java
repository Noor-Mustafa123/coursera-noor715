package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseEntity;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseOptimizationEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class LoadCaseOptimizationDTO.
 */
public class LoadCaseOptimizationDTO extends LoadCaseDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8664710267323867727L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final LoadCaseOptimizationEntity ENTITY_CLASS = new LoadCaseOptimizationEntity();

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.model.LoadCaseDTO#prepareEntity(java.lang.String)
     */
    @Override
    public LoadCaseEntity prepareEntity( String userId ) {
        LoadCaseEntity entity = new LoadCaseEntity();
        entity.setTimeout( getTimeout() );
        entity.setIsInternal( getIsInternal() );
        entity.setName( getName() );
        entity.setDescription( getDescription() );
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        entity.setCreatedOn( now );
        entity.setModifiedOn( now );
        entity.setName( getName() );
        entity.setCustomAttributes( prepareCustomAttributes( entity, getCustomAttributes(), getCustomAttributesDTO() ) );
        entity.setTypeId( getTypeId() );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        entity.setOwner( userEntity );
        entity.setJobId( getJobId() );
        return entity;

    }

}
