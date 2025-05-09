package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * This class is used for data transfer of project's data objects.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class DataObjectDTO extends SusDTO {

    /**
     * The entity class.
     */
    private static final DataObjectEntity ENTITY_CLASS = new DataObjectEntity();

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2454061708464297024L;

    /**
     * The Constant For data Object Name.
     */
    public static final String DATAOBJECT_NAME = "name";

    /**
     * The id.
     */
    @UIFormField( name = "parentId", title = "3000041x4", type = "hidden" )
    @UIColumn( data = "parentId", name = "parentId", filter = "uuid", renderer = "hidden", title = "3000041x4", type = "hidden", isShow = false )
    private UUID parentId;

    /**
     * The type id.
     */
    @UIFormField( name = "typeId", title = "3000037x4", type = "hidden" )
    @UIColumn( data = "typeId", name = "typeId", filter = "uuid", renderer = "hidden", title = "3000037x4", type = "hidden", isShow = false )
    private UUID typeId;

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden")
    @UIColumn( data = "id", name = "id", filter = "", renderer = "text", title = "3000021x4", isShow = false )
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
    @UIColumn( data = "lifeCycleStatus.name", filter = "", renderer = "text", title = "3000066x4", name = "lifeCycleStatus", orderNum = 5 )
    private StatusDTO lifeCycleStatus;

    /**
     * The size.
     */
    @UIFormField( name = "size", title = "3000123x4", isAsk = false, orderNum = 6 )
    @UIColumn( data = "size", filter = "", renderer = "text", title = "3000123x4", name = "entitySize", orderNum = 6 )
    private String size;

    /**
     * The location DT os.
     */
    @UIColumn( data = "locations", filter = "", isSortable = false, renderer = "list", title = "3000125x4", type = "table", name = "file.locations.name", orderNum = 10 )
    private transient List< LocationDTO > locations;

    /**
     * Version Dto Reference.
     */
    private VersionDTO version;

    /**
     * The custom attributes DTO.
     */
    private List< CustomAttributeDTO > customAttributesDTO;

    /**
     * The file.
     */
    @UIFormField( name = "file", title = "0300053x4", type = "file-upload", multiple = false, orderNum = 6 )
    private DocumentDTO file;

    /**
     * The checked out.
     */
    private boolean checkedOut;

    /**
     * The checked out user.
     */
    private UserEntity checkedOutUser;

    /**
     * The checked out same.
     */
    private boolean checkedOutSame;

    /**
     * Gets the checked out user.
     *
     * @return the checked out user
     */
    public UserEntity getCheckedOutUser() {
        return checkedOutUser;
    }

    /**
     * Sets the checked out user.
     *
     * @param checkedOutUser
     *         the new checked out user
     */
    public void setCheckedOutUser( UserEntity checkedOutUser ) {
        this.checkedOutUser = checkedOutUser;
    }

    /**
     * Creates the data object entity from data object DTO.
     *
     * @param userId
     *         the user id
     *
     * @return the data object entity
     */
    public DataObjectEntity prepareEntity( String userId ) {

        DataObjectEntity dataObjectEntity = new DataObjectEntity();

        int versions = getVersion() != null ? getVersion().getId() : SusConstantObject.DEFAULT_VERSION_NO;

        if ( getId() == null ) {
            dataObjectEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), versions ) );

        } else {
            dataObjectEntity.setComposedId( new VersionPrimaryKey( getId(), versions ) );
        }
        Date now = new Date();
        dataObjectEntity.setCreatedOn( now );
        dataObjectEntity.setModifiedOn( now );
        dataObjectEntity.setName( getName() );
        dataObjectEntity.setDescription( getDescription() );
        dataObjectEntity.setTypeId( getTypeId() );

        dataObjectEntity
                .setCustomAttributes( prepareCustomAttributes( dataObjectEntity, getCustomAttributes(), getCustomAttributesDTO() ) );

        // set Status From lifeCycle
        dataObjectEntity.setLifeCycleStatus( ( getLifeCycleStatus() != null ) ? getLifeCycleStatus().getId() : null );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        dataObjectEntity.setOwner( userEntity );

        if ( getFile() != null ) {
            DocumentEntity documentEntity = prepareDocumentEntity();
            dataObjectEntity.setFile( documentEntity );
            dataObjectEntity.setSize( documentEntity.getSize() );
        } else {
            dataObjectEntity.setSize( null );
        }
        dataObjectEntity.setJobId( getJobId() );
        return dataObjectEntity;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( parentId == null ) ? 0 : parentId.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
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
        DataObjectDTO other = ( DataObjectDTO ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        if ( parentId == null ) {
            if ( other.parentId != null ) {
                return false;
            }
        } else if ( !parentId.equals( other.parentId ) ) {
            return false;
        }
        return true;
    }

    /**
     * Instantiates a new data object DTO.
     */
    public DataObjectDTO() {
        super();
        name = ConstantsString.EMPTY_STRING;
    }

    /**
     * Instantiates a new data object DTO.
     *
     * @param dataObjectDTO
     *         the data object DTO
     */
    public DataObjectDTO( DataObjectDTO dataObjectDTO ) {
        super();
        name = ConstantsString.EMPTY_STRING;
        if ( dataObjectDTO != null ) {
            this.parentId = dataObjectDTO.getParentId();
            this.typeId = dataObjectDTO.getTypeId();
            this.id = dataObjectDTO.getId();
            this.name = dataObjectDTO.getName();
            setDescription( dataObjectDTO.getDescription() );
            this.setCreatedBy( dataObjectDTO.getCreatedBy() );
            setCustomAttributes( dataObjectDTO.getCustomAttributes() );
            this.lifeCycleStatus = dataObjectDTO.getLifeCycleStatus();
            this.version = dataObjectDTO.getVersion();
            this.customAttributesDTO = dataObjectDTO.getCustomAttributesDTO();
            this.file = dataObjectDTO.getFile();
            this.checkedOut = dataObjectDTO.isCheckedOut();
            this.checkedOutUser = dataObjectDTO.getCheckedOutUser();

        }
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
     * Checks if is checked out.
     *
     * @return true, if is checked out
     */
    public boolean isCheckedOut() {
        return checkedOut;
    }

    /**
     * Sets the checked out.
     *
     * @param checkedOut
     *         the new checked out
     */
    public void setCheckedOut( boolean checkedOut ) {
        this.checkedOut = checkedOut;
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
     * @param versionDto
     *         the new version
     */
    public void setVersion( VersionDTO versionDto ) {
        this.version = versionDto;
    }

    /**
     * Gets the type id.
     *
     * @return the typeId
     */
    public UUID getTypeId() {
        return typeId;
    }

    /**
     * Sets the type id.
     *
     * @param typeId
     *         the typeId to set
     */
    public void setTypeId( UUID typeId ) {
        this.typeId = typeId;
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
     * Gets the size.
     *
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( String size ) {
        this.size = size;
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
     * Gets the file.
     *
     * @return the file
     */
    public DocumentDTO getFile() {
        return file;
    }

    /**
     * Sets the file.
     *
     * @param file
     *         the new file
     */
    public void setFile( DocumentDTO file ) {
        this.file = file;
    }

    /**
     * Checks if is checked out same.
     *
     * @return true, if is checked out same
     */
    public boolean isCheckedOutSame() {
        return checkedOutSame;
    }

    /**
     * Sets the checked out same.
     *
     * @param checkedOutSame
     *         the new checked out same
     */
    public void setCheckedOutSame( boolean checkedOutSame ) {
        this.checkedOutSame = checkedOutSame;
    }

    /**
     * Gets the locations.
     *
     * @return the locations
     */
    public List< LocationDTO > getLocations() {
        return locations;
    }

    /**
     * Sets the locations.
     *
     * @param locations
     *         the new locations
     */
    public void setLocations( List< LocationDTO > locations ) {
        this.locations = locations;
    }

    /**
     * Prepare document entity.
     *
     * @return the document entity
     */
    public DocumentEntity prepareDocumentEntity() {
        DocumentEntity documentEntity = new DocumentEntity( UUID.fromString( getFile().getId() ) );
        documentEntity.setFileName( getFile().getName() );
        documentEntity.setFilePath( getFile().getPath() );
        documentEntity.setEncoding( getFile().getEncoding() );
        documentEntity.setHash( getFile().getHash() );
        documentEntity.setSize( getFile().getSize() );
        if ( null != getFile().getEncryptionDecryption() ) {
            EncryptionDecryptionEntity encryptionDecryption = new EncryptionDecryptionEntity(
                    getFile().getEncryptionDecryption().getMethod(), getFile().getEncryptionDecryption().getSalt(),
                    getFile().getEncryptionDecryption().isActive() );
            if ( null != getFile().getEncryptionDecryption().getId() ) {
                encryptionDecryption.setId( UUID.fromString( getFile().getEncryptionDecryption().getId() ) );
            } else {
                encryptionDecryption.setId( UUID.randomUUID() );
            }
            documentEntity.setEncryptionDecryption( encryptionDecryption );
        }
        documentEntity.setEncrypted( getFile().isEncrypted() );
        return documentEntity;
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

    @Override
    public String toString() {
        return "DataObjectDTO [parentId=" + parentId + ", typeId=" + typeId + ", id=" + id + ", name=" + name + ", type=" + type
                + ", lifeCycleStatus=" + lifeCycleStatus + ", size=" + size + ", version=" + version + ", customAttributesDTO="
                + customAttributesDTO + ", file=" + file + ", checkedOut=" + checkedOut + ", checkedOutUser=" + checkedOutUser
                + ", checkedOutSame=" + checkedOutSame + "]";
    }

}
