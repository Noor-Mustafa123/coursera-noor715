package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WorkflowProjectEntity;

/**
 * A pojo class for mapping workflow manager object to json and back to class.
 *
 * @author noman arshad
 */
public class WorkflowProjectDTO extends SusDTO {

    /**
     * serial id
     */
    private static final long serialVersionUID = 373136642646664260L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final WorkflowProjectEntity ENTITY_CLASS = new WorkflowProjectEntity();

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
     * The object type id.
     */
    @UIFormField( name = "typeId", title = "3000074x4", type = "hidden", orderNum = 3 )
    @UIColumn( data = "typeId", name = UI_COLUMN_NAME_TYPE_ID, filter = "uuid", renderer = "hidden", title = "3000074x4", type = "hidden", isShow = false, orderNum = 3 )
    private UUID typeId;

    /**
     * The id.
     */
    @UIFormField( name = "parentId", title = "3000041x4", type = "hidden", orderNum = 4 )
    @UIColumn( data = "parentId", name = UI_COLUMN_NAME_PARENT_ID, filter = "uuid", renderer = "hidden", title = "3000041x4", type = "hidden", isShow = false, orderNum = 4 )
    private UUID parentId;

    /**
     * The updated on.
     */
    @UIFormField( name = "lifeCycleStatus.name", title = "3000066x4", type = "select", isAsk = false, orderNum = 5 )
    @UIColumn( data = "lifeCycleStatus.name", filter = "", renderer = "text", title = "3000066x4", name = LIFE_CYCLE_STATUS, orderNum = 5 )
    private StatusDTO lifeCycleStatus;

    /**
     * The updated on.
     */
    @UIFormField( name = "config", title = "3000005x4", type = "select", orderNum = 7 )
    private String config;

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, orderNum = 9, type = "hidden")
    @UIColumn( data = "id", name = "id", filter = "", renderer = "text", title = "3000021x4", orderNum = 9 )
    private UUID id;

    /**
     * The WORKFLOW_PROJECT_ID COnstant for message.
     */
    public static final String WORKFLOW_PROJECT_ID = "Workflows Project Id";

    /**
     * The WORKFLOWS_PROJECT_NAME Constant for message.
     */
    public static final String WORKFLOWS_PROJECT_NAME = "Project Name";

    /**
     * The WORKFLOWS_DESCPTION Constant for message.
     */
    public static final String WORKFLOWS_DESCPTION = "Workflows Project Description";

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
     * The Constant LIFE_CYCLE_STATUS.
     */
    public static final String LIFE_CYCLE_STATUS = "lifeCycleStatus";

    /**
     * version for user.
     */
    private VersionDTO version;

    /**
     * The custom attributes DTO.
     */
    private List< CustomAttributeDTO > customAttributesDTO;

    /**
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( version == null ) ? 0 : version.hashCode() );
        return result;
    }

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
        WorkflowProjectDTO other = ( WorkflowProjectDTO ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( version == null ) {
            if ( other.version != null ) {
                return false;
            }
        } else if ( !version.equals( other.version ) ) {
            return false;
        }
        return true;
    }

    /**
     * Validate.
     *
     * @return the notification
     */
    public Notification validate() {
        Notification notif = new Notification();
        notif.addNotification( ValidationUtils.validateGlobalFieldAndLength( getName(), WORKFLOWS_PROJECT_NAME,
                ConstantsLength.STANDARD_NAME_LENGTH, false, true ) );

        if ( !StringUtils.isBlank( getDescription() ) ) {
            notif.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( getDescription(),
                    WORKFLOWS_DESCPTION, ConstantsLength.STANDARD_DESCRIPTION_LENGTH, false, false ) );
        }

        return notif;
    }

    /**
     * Prepare entity.
     *
     * @return the project entity
     */
    public WorkflowProjectEntity prepareEntity( String userId ) {

        WorkflowProjectEntity projectEntity = new WorkflowProjectEntity();

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
        projectEntity.setTypeId( getTypeId() );

        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        projectEntity.setOwner( userEntity );

        projectEntity.setType( "Workflow" );
        projectEntity.setCustomAttributes( prepareCustomAttributes( projectEntity, getCustomAttributes(), getCustomAttributesDTO() ) );

        projectEntity.setLifeCycleStatus( ( getLifeCycleStatus() != null ) ? getLifeCycleStatus().getId() : null );
        projectEntity.setJobId( getJobId() );
        return projectEntity;

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
     * @return the typeId
     */
    public UUID getTypeId() {
        return typeId;
    }

    /**
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

}
