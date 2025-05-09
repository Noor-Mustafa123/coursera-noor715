package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.SolverEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class SolverDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SolverDTO extends ThirdPartyLibraryDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -17790192769200626L;

    /**
     * The entity class.
     */
    private static final SolverEntity ENTITY_CLASS = new SolverEntity();

    /**
     * Prepare entity.
     *
     * @param userId
     *         the user id
     *
     * @return the solver entity
     */

    public SolverEntity prepareEntity( String userId ) {
        SolverEntity entity = new SolverEntity();
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
