package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.data.entity.ThirdPartyLibraryEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class ThirdPartyLibraryDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ThirdPartyLibraryDTO extends SusDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2779639017020228087L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final ThirdPartyLibraryEntity ENTITY_CLASS = new ThirdPartyLibraryEntity();

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
     * @return the third party library entity
     */
    public ThirdPartyLibraryEntity prepareEntity() {
        ThirdPartyLibraryEntity entity = new ThirdPartyLibraryEntity();
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        entity.setJobId( getJobId() );
        return entity;

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
