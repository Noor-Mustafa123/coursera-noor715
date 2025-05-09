package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.data.entity.StartDeckEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class StartDeckDTO.
 */
public class StartDeckDTO extends SusDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -9103267893455093806L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final StartDeckEntity ENTITY_CLASS = new StartDeckEntity();

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4", orderNum = 1 )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The type id.
     */
    @UIFormField( name = "typeId", title = "3000037x4", type = "hidden" )
    @UIColumn( data = "typeId", name = "typeId", filter = "uuid", renderer = "hidden", title = "3000037x4", type = "hidden", isShow = false )
    private UUID typeId;

    /**
     * The version.
     */
    private VersionDTO version;

    /**
     * The custom attributes DTO.
     */
    private List< CustomAttributeDTO > customAttributesDTO;

    /**
     * Prepare entity.
     *
     * @return the load case template entity
     */
    public StartDeckEntity prepareEntity( String userId ) {
        StartDeckEntity entity = new StartDeckEntity();
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

    /**
     * Prepare DTO.
     *
     * @param entity
     *         the entity
     *
     * @return the load case template DTO
     */
    public StartDeckDTO prepareDTO( StartDeckEntity entity ) {
        return new StartDeckDTO();

    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the type id.
     *
     * @return the type id
     */
    public UUID getTypeId() {
        return typeId;
    }

    /**
     * Sets the type id.
     *
     * @param typeId
     *         the new type id
     */
    public void setTypeId( UUID typeId ) {
        this.typeId = typeId;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *         the new version
     */
    public void setVersion( VersionDTO version ) {
        this.version = version;
    }

    /**
     * Gets the custom attributes DTO.
     *
     * @return the custom attributes DTO
     */
    public List< CustomAttributeDTO > getCustomAttributesDTO() {
        return customAttributesDTO;
    }

    /**
     * Sets the custom attributes DTO.
     *
     * @param customAttributesDTO
     *         the new custom attributes DTO
     */
    public void setCustomAttributesDTO( List< CustomAttributeDTO > customAttributesDTO ) {
        this.customAttributesDTO = customAttributesDTO;
    }

}
