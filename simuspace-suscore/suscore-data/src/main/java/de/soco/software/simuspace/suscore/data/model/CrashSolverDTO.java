package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.CrashSolverEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class CrashSolverDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class CrashSolverDTO extends SolverDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8610398067794396914L;

    /**
     * The entity class.
     */
    private static final CrashSolverEntity ENTITY_CLASS = new CrashSolverEntity();

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.model.SolverDTO#prepareEntity(java.lang.String)
     */
    @Override
    public CrashSolverEntity prepareEntity( String userId ) {
        CrashSolverEntity entity = new CrashSolverEntity();
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
