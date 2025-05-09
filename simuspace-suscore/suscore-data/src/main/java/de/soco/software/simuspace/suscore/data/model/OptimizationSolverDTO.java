package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.OptimizationSolverEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class OptimizationSolverDTO.
 */
public class OptimizationSolverDTO extends SolverDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -847713441656826579L;

    /**
     * The entity class.
     */
    private static final OptimizationSolverEntity ENTITY_CLASS = new OptimizationSolverEntity();

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.model.SolverDTO#prepareEntity(java.lang.String)
     */
    @Override
    public OptimizationSolverEntity prepareEntity( String userId ) {
        OptimizationSolverEntity entity = new OptimizationSolverEntity();
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
