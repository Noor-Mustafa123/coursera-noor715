package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * This class is responsible for data transfer of project objects.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ProjectDTO extends SusDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2976246393503011121L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final ProjectEntity ENTITY_CLASS = new ProjectEntity();

    /**
     * The id.
     */
    @UIFormField( name = "parentId", title = "3000041x4", type = "hidden" )
    @UIColumn( data = "parentId", name = UI_COLUMN_NAME_PARENT_ID, filter = "uuid", renderer = "hidden", title = "3000041x4", type = "hidden", isShow = false )
    private UUID parentId;

    /**
     * The object type id.
     */
    @UIFormField( name = "typeId", title = "3000074x4", type = "hidden" )
    @UIColumn( data = "typeId", name = UI_COLUMN_NAME_TYPE_ID, filter = "uuid", renderer = "hidden", title = "3000074x4", isShow = false )
    private UUID typeId;

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden")
    @UIColumn( data = "id", name = "id", filter = "text", renderer = "text", title = "3000021x4", isShow = false )
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
     * The updated on.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "type", title = "3000044x4", type = "select", orderNum = 3 )
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000051x4", name = UI_COLUMN_NAME_TYPE, type = "select", orderNum = 3 )
    private String type;

    /**
     * The updated on.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "config", title = "3000005x4", type = "select", orderNum = 8 )
    @UIColumn( data = "config", filter = "text", renderer = "text", title = "3000005x4", name = UI_COLUMN_NAME_CONFIG, type = "select", orderNum = 8 )
    private String config;

    /**
     * The updated on.
     */
    @UIFormField( name = "lifeCycleStatus.name", title = "3000066x4", type = "select", isAsk = false, orderNum = 5 )
    @UIColumn( data = "lifeCycleStatus.name", filter = "text", renderer = "text", title = "3000066x4", name = "lifeCycleStatus", orderNum = 5 )
    private StatusDTO lifeCycleStatus;

    /**
     * The size.
     */
    @UIFormField( name = "size", title = "3000123x4", isAsk = false, orderNum = 6 )
    @UIColumn( data = "size", filter = "", renderer = "text", title = "3000123x4", name = "entitySize", orderNum = 6 )
    private String size;

    /**
     * The PROJECT_ID COnstant for message.
     */
    public static final String PROJECT_ID = "Project Id";

    /**
     * The PROJECT_NAME Constant for message.
     */
    public static final String PROJECT_NAME = "name";

    /**
     * The PROJECT_DESCPTION Constant for message.
     */
    public static final String PROJECT_DESCPTION = "Project Description";

    /**
     * The Constant UI_COLUMN_NAME_CONFIG.
     */
    public static final String UI_COLUMN_NAME_CONFIG = "config";

    /**
     * The Constant UI_COLUMN_NAME_PARENT_ID.
     */
    public static final String UI_COLUMN_NAME_PARENT_ID = "parentId";

    /**
     * The Constant UI_COLUMN_NAME_TYPE_ID.
     */
    public static final String UI_COLUMN_NAME_TYPE_ID = "typeId";

    /**
     * The Constant UI_COLUMN_NAME_CONFIG_TYPE.
     */
    public static final String UI_COLUMN_NAME_TYPE = "type";

    /**
     * The Constant UI_COLUMN_NAME_STATUS.
     */
    public static final String UI_COLUMN_NAME_STATUS = "status";

    /**
     * version for user.
     */
    private VersionDTO version;

    /**
     * The custom attributes DTO.
     */
    private List< CustomAttributeDTO > customAttributesDTO;

    /**
     * The Constant VARIANT_NAME.
     */
    public static final String VARIANT_NAME = "name";

    /**
     * The html.
     */
    private String html;

    /**
     * The js.
     */
    private String js;

    /**
     * The attachments.
     */
    private List< Map< String, String > > attachments;

    /**
     * The zip file.
     */
    private DocumentDTO zipFile;

    /**
     * The html index.
     */
    private String html_index;

    /**
     * The js index.
     */
    private String js_index;

    /**
     * The baseurl.
     */
    private String baseurl;

    /**
     * The Constant DATAOBJECT_NAME.
     */
    public static final String DATAOBJECT_NAME = "name";

    /**
     * The locations.
     */
    @UIColumn( data = "locations", filter = "", isSortable = false, renderer = "list", title = "3000125x4", type = "table", name = "file"
            + ".locations.name", orderNum = 10 )
    private transient List< LocationDTO > locations;

    /**
     * The file.
     */
    private DocumentDTO file;

    /**
     * Gets the baseurl.
     *
     * @return the baseurl
     */
    public String getBaseurl() {
        return baseurl;
    }

    /**
     * Sets the baseurl.
     *
     * @param baseurl
     *         the new baseurl
     */
    public void setBaseurl( String baseurl ) {
        this.baseurl = baseurl;
    }

    /**
     * Gets the html.
     *
     * @return the html
     */
    public String getHtml() {
        return html;
    }

    /**
     * Sets the html.
     *
     * @param html
     *         the new html
     */
    public void setHtml( String html ) {
        this.html = html;
    }

    /**
     * Gets the zip file.
     *
     * @return the zip file
     */
    public DocumentDTO getZipFile() {
        return zipFile;
    }

    /**
     * Sets the zip file.
     *
     * @param zipFile
     *         the new zip file
     */
    public void setZipFile( DocumentDTO zipFile ) {
        this.zipFile = zipFile;
    }

    /**
     * Gets the attachments.
     *
     * @return the attachments
     */
    public List< Map< String, String > > getAttachments() {
        return attachments;
    }

    /**
     * Sets the attachments.
     *
     * @param attachments
     *         the attachments
     */
    public void setAttachments( List< Map< String, String > > attachments ) {
        this.attachments = attachments;
    }

    /**
     * Gets the js.
     *
     * @return the js
     */
    public String getJs() {
        return js;
    }

    /**
     * Sets the js.
     *
     * @param js
     *         the new js
     */
    public void setJs( String js ) {
        this.js = js;
    }

    /**
     * Gets the html index.
     *
     * @return the html index
     */
    public String getHtml_index() {
        return html_index;
    }

    /**
     * Sets the html index.
     *
     * @param htmlIndex
     *         the new html index
     */
    public void setHtml_index( String htmlIndex ) {
        this.html_index = htmlIndex;
    }

    /**
     * Gets the js index.
     *
     * @return the js index
     */
    public String getJs_index() {
        return js_index;
    }

    /**
     * Sets the js index.
     *
     * @param jsIndex
     *         the new js index
     */
    public void setJs_index( String jsIndex ) {
        this.js_index = jsIndex;
    }

    /**
     * Gets file.
     *
     * @return the file
     */
    public DocumentDTO getFile() {
        return file;
    }

    /**
     * Sets file.
     *
     * @param file
     *         the file
     */
    public void setFile( DocumentDTO file ) {
        this.file = file;
    }

    /**
     * Gets locations.
     *
     * @return the locations
     */
    public List< LocationDTO > getLocations() {
        return locations;
    }

    /**
     * Sets locations.
     *
     * @param locations
     *         the locations
     */
    public void setLocations( List< LocationDTO > locations ) {
        this.locations = locations;
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
     * Prepare document entity.
     *
     * @return the document entity
     */
    private DocumentEntity prepareDocumentEntity() {
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
     * Prepare zip document entity.
     *
     * @return the sets the
     */
    private Set< DocumentEntity > prepareZipDocumentEntity() {
        Set< DocumentEntity > attachmentList = new HashSet<>();
        if ( null != getZipFile() ) {
            DocumentEntity documentEntity = new DocumentEntity( UUID.fromString( getZipFile().getId() ) );
            documentEntity.setFileName( getZipFile().getName() );
            documentEntity.setFilePath( getZipFile().getPath() );
            documentEntity.setEncoding( getZipFile().getEncoding() );
            documentEntity.setHash( getZipFile().getHash() );
            documentEntity.setSize( getZipFile().getSize() );
            documentEntity.setFileSize( getZipFile().getSize() );
            documentEntity.setFileType( getZipFile().getType() );
            documentEntity.setCreatedOn( getZipFile().getCreatedOn() );
            documentEntity.setEncrypted( getZipFile().isEncrypted() );
            if ( null != getZipFile().getEncryptionDecryption() ) {
                EncryptionDecryptionEntity encryptionDecryption = new EncryptionDecryptionEntity(
                        getZipFile().getEncryptionDecryption().getMethod(), getZipFile().getEncryptionDecryption().getSalt(),
                        getZipFile().getEncryptionDecryption().isActive() );
                if ( null != getZipFile().getEncryptionDecryption().getId() ) {
                    encryptionDecryption.setId( UUID.fromString( getZipFile().getEncryptionDecryption().getId() ) );
                } else {
                    encryptionDecryption.setId( UUID.randomUUID() );
                }
                documentEntity.setEncryptionDecryption( encryptionDecryption );
            }
            attachmentList.add( documentEntity );
        }
        return attachmentList;
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
     * Gets the config.
     *
     * @return the config
     */
    public String getConfig() {
        return config;
    }

    /**
     * Sets the config.
     *
     * @param config
     *         the new config
     */
    public void setConfig( String config ) {
        this.config = config;
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
     * Gets the version.
     *
     * @return the versionDto
     */
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param versionDto
     *         the versionDto to set
     */
    public void setVersion( VersionDTO versionDto ) {
        this.version = versionDto;
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
     * Hash code.
     *
     * @return the int
     */
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        return result;
    }

    /**
     * Equals.
     *
     * @param obj
     *         the obj
     *
     * @return true, if successful
     */
    /*
     * (non-Javadoc)
     *
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
        ProjectDTO other = ( ProjectDTO ) obj;
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
        ProjectDTO that = ( ProjectDTO ) obj;
        return Objects.equals( getParentId(), that.getParentId() ) && Objects.equals( getTypeId(), that.getTypeId() )
                && Objects.equals( getId(), that.getId() ) && Objects.equals( getName(), that.getName() )
                && Objects.equals( getType(), that.getType() ) && Objects.equals( getLifeCycleStatus(), that.getLifeCycleStatus() )
                && Objects.equals( getCustomAttributes(), that.getCustomAttributes() )
                && Objects.equals( getCustomAttributesDTO(), that.getCustomAttributesDTO() )
                && Objects.equals( getVersion(), that.getVersion() ) && Objects.equals( getHtml(), that.getHtml() )
                && Objects.equals( getJs(), that.getJs() ) && Objects.equals( getAttachments(), that.getAttachments() )
                && Objects.equals( getZipFile(), that.getZipFile() ) && Objects.equals( getHtml_index(), that.getHtml_index() )
                && Objects.equals( getJs_index(), that.getJs_index() ) && Objects.equals( getBaseurl(), that.getBaseurl() )
                && Objects.equals( getSize(), that.getSize() ) && Objects.equals( getLocations(), that.getLocations() )
                && Objects.equals( getFile(), that.getFile() );
    }

    /**
     * Validate.
     *
     * @return the notification
     */
    public Notification validate() {
        Notification notif = new Notification();

        notif.addNotification( ValidationUtils.validateGlobalFieldAndLength( getName(), PROJECT_NAME, ConstantsLength.STANDARD_NAME_LENGTH,
                false, true ) );

        if ( !StringUtils.isBlank( getDescription() ) ) {
            notif.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( getDescription(),
                    PROJECT_DESCPTION, ConstantsLength.STANDARD_DESCRIPTION_LENGTH, false, false ) );
        }

        return notif;
    }

    /**
     * Prepare entity.
     *
     * @param userId
     *         the user id
     *
     * @return the project entity
     */
    public ProjectEntity prepareEntity( String userId ) {

        ProjectEntity projectEntity = new ProjectEntity();
        int versions = getVersion() != null ? getVersion().getId() : SusConstantObject.DEFAULT_VERSION_NO;
        if ( getId() == null ) {
            projectEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), versions ) );

        } else {
            projectEntity.setComposedId( new VersionPrimaryKey( getId(), versions ) );
        }

        Date now = new Date();
        projectEntity.setCreatedOn( now );
        projectEntity.setModifiedOn( now );
        projectEntity.setName( getName() );
        projectEntity.setDescription( getDescription() );
        projectEntity.setType( getType() );
        projectEntity.setTypeId( getTypeId() );
        projectEntity.setDescription( getDescription() );
        projectEntity.setCustomAttributes( prepareCustomAttributes( projectEntity, getCustomAttributes(), getCustomAttributesDTO() ) );

        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        projectEntity.setOwner( userEntity );

        // set Status From lifeCycle
        projectEntity.setLifeCycleStatus( ( getLifeCycleStatus() != null ) ? getLifeCycleStatus().getId() : null );
        projectEntity.setJobId( getJobId() );
        projectEntity.setSize( StringUtils.isBlank( getSize() ) ? ConstantsInteger.INTEGER_VALUE_ZERO : Long.parseLong( getSize() ) );

        if ( getFile() != null ) {
            DocumentEntity documentEntity = prepareDocumentEntity();
            projectEntity.setFile( documentEntity );
            projectEntity.setSize( documentEntity.getSize() );
        }
        projectEntity.setAttachments( prepareZipDocumentEntity() );
        return projectEntity;

    }

}
