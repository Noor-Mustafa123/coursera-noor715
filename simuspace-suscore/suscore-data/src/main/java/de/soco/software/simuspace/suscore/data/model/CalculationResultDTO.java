package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.CalculationResultEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class CalculationResultDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class CalculationResultDTO extends AbstractModelDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 3240349899656467824L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final CalculationResultEntity ENTITY_CLASS = new CalculationResultEntity();

    /**
     * Prepare entity.
     *
     * @param userId
     *         the user id
     *
     * @return the calculation result entity
     */
    public CalculationResultEntity prepareEntity( String userId ) {
        CalculationResultEntity entity = new CalculationResultEntity();
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
