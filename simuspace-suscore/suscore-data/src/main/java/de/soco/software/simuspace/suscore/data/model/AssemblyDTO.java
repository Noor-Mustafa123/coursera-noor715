package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.AssemblyEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class AssemblyDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class AssemblyDTO extends AbstractModelDTO {

    /**
     *
     */
    private static final long serialVersionUID = -8577875429211481318L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final AssemblyEntity ENTITY_CLASS = new AssemblyEntity();

    /**
     * Prepare entity.
     *
     * @param userId
     *         the user id
     *
     * @return the assembly entity
     */
    public AssemblyEntity prepareEntity( String userId ) {
        AssemblyEntity entity = new AssemblyEntity();
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
