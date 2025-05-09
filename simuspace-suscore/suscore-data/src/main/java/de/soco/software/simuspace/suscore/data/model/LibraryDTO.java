package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.data.entity.LibraryEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * This class is responsible for data transfer of library objects.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LibraryDTO extends SusDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 6984861210457381148L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final LibraryEntity ENTITY_CLASS = new LibraryEntity();

    /**
     * The Constant LIBRARY_NAME.
     */
    public static final String LIBRARY_NAME = "name";

    /**
     * The id.
     */
    @UIFormField( name = "parentId", title = "3000041x4", type = "hidden" )
    @UIColumn( data = "parentId", name = "parentId", filter = "uuid", renderer = "hidden", title = "3000041x4", type = "hidden", isShow = false )
    private UUID parentId;

    /**
     * The object type id.
     */
    @UIFormField( name = "typeId", title = "3000037x4", type = "hidden" )
    private UUID typeId;

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden")
    @UIColumn( data = "id", name = "id", filter = "uuid", renderer = "text", title = "3000021x4", isShow = false )
    private UUID id;

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
     * The type.
     */
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000051x4", name = "type", type = "select", orderNum = 3 )
    private String type;

    /**
     * The updated on.
     */
    @UIFormField( name = "lifeCycleStatus.name", title = "3000066x4", isAsk = false, orderNum = 5 )
    @UIColumn( data = "lifeCycleStatus.name", filter = "text", renderer = "text", title = "3000066x4", name = "lifeCycleStatus", orderNum = 5 )
    private StatusDTO lifeCycleStatus;

    /**
     * The custom attributes DTO.
     */
    private List< CustomAttributeDTO > customAttributesDTO;

    /**
     * Version Dto Reference.
     */
    private VersionDTO version;

    /**
     * Prepare entity.
     *
     * @param userId
     *         the user id
     *
     * @return the library entity
     */
    public LibraryEntity prepareEntity( String userId ) {

        LibraryEntity libraryEntity = new LibraryEntity();

        libraryEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        libraryEntity.setCreatedOn( now );
        libraryEntity.setModifiedOn( now );
        libraryEntity.setName( getName() );
        libraryEntity.setTypeId( getTypeId() );
        libraryEntity.setCustomAttributes( prepareCustomAttributes( libraryEntity, getCustomAttributes(), getCustomAttributesDTO() ) );
        // set Status From lifeCycle
        libraryEntity.setLifeCycleStatus( ( getLifeCycleStatus() != null ) ? getLifeCycleStatus().getId() : null );
        libraryEntity.setJobId( getJobId() );

        return libraryEntity;
    }

    /**
     * Prepare library dto from project dto library dto.
     *
     * @param projectDTO
     *         the project dto
     *
     * @return the library dto
     */
    public static LibraryDTO prepareFromProjectDTO( ProjectDTO projectDTO ) {
        LibraryDTO libraryDTO = new LibraryDTO();
        libraryDTO.setId( projectDTO.getId() );
        libraryDTO.setName( projectDTO.getName() );
        libraryDTO.setType( projectDTO.getType() );
        libraryDTO.setVersion( projectDTO.getVersion() );
        libraryDTO.setParentId( projectDTO.getParentId() );
        libraryDTO.setCustomAttributesDTO( projectDTO.getCustomAttributesDTO() );
        libraryDTO.setCustomAttributes( projectDTO.getCustomAttributes() );
        libraryDTO.setLifeCycleStatus( projectDTO.getLifeCycleStatus() );
        libraryDTO.setAutoDeleted( projectDTO.getAutoDeleted() );
        libraryDTO.setCreatedBy( projectDTO.getCreatedBy() );
        libraryDTO.setCreatedOn( projectDTO.getCreatedOn() );
        libraryDTO.setModifiedBy( projectDTO.getModifiedBy() );
        libraryDTO.setModifiedOn( projectDTO.getModifiedOn() );
        libraryDTO.setDescription( projectDTO.getDescription() );
        libraryDTO.setHidden( projectDTO.isHidden() );
        libraryDTO.setJobId( projectDTO.getJobId() );
        return libraryDTO;
    }

    /**
     * Gets the parent id.
     *
     * @return the parent id
     */
    public UUID getParentId() {
        return parentId;
    }

    /**
     * Sets the parent id.
     *
     * @param parentId
     *         the new parent id
     */
    public void setParentId( UUID parentId ) {
        this.parentId = parentId;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    @Override
    public void setId( UUID id ) {
        this.id = id;
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

    /**
     * Gets the life cycle status.
     *
     * @return the lifeCycleStatus
     */
    public StatusDTO getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    /**
     * Sets the life cycle status.
     *
     * @param lifeCycleStatus
     *         the lifeCycleStatus to set
     */
    public void setLifeCycleStatus( StatusDTO lifeCycleStatus ) {
        this.lifeCycleStatus = lifeCycleStatus;
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        LibraryDTO other = ( LibraryDTO ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        return true;
    }

}