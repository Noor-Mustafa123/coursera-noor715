package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.DummyTypeDTO;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class LoadCaseDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LoadCaseDTO extends AbstractWorkflowDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8816028042154026185L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final LoadCaseEntity ENTITY_CLASS = new LoadCaseEntity();

    /**
     * The dummy type.
     */
    private DummyTypeDTO dummyTypeDTO;

    /**
     * Prepare entity.
     *
     * @return the load case entity
     */

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
        entity.setName( getName() );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        entity.setOwner( userEntity );
        entity.setJobId( getJobId() );
        return entity;
    }

    public DummyTypeDTO getDummyTypeDTO() {
        return dummyTypeDTO;
    }

    public void setDummyTypeDTO( DummyTypeDTO dummyTypeDTO ) {
        this.dummyTypeDTO = dummyTypeDTO;
    }

}
