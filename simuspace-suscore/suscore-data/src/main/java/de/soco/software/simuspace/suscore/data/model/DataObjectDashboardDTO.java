package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.data.entity.DataObjectDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectDashboardDTO.
 *
 * @author Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
@Deprecated( since = "soco/2.3.1/release", forRemoval = true )
public class DataObjectDashboardDTO extends SusDTO {

    /**
     * The Constant serialVersionUID.
     */

    private static final long serialVersionUID = 1L;

    /**
     * The entity class.
     */
    private static final DataObjectDashboardEntity ENTITY_CLASS = new DataObjectDashboardEntity();

    /**
     * The id.
     */
    @UIFormField( name = "parentId", title = "3000041x4", type = "hidden" )
    @UIColumn( data = "parentId", name = "parentId", filter = "text", renderer = "hidden", title = "3000041x4", type = "hidden", isShow = false )
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
    @UIColumn( data = "id", name = "id", filter = "text", renderer = "text", title = "3000021x4", isShow = false )
    private UUID id;

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4", orderNum = 1 )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1 )
    private String name;

    /**
     * The type.
     */
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000051x4", name = "type", type = "select", orderNum = 2 )
    private String type;

    /**
     * The updated on.
     */
    @UIFormField( name = "lifeCycleStatus.name", title = "3000066x4", isAsk = false, orderNum = 3 )
    @UIColumn( data = "lifeCycleStatus.name", filter = "text", renderer = "text", title = "3000066x4", name = "lifeCycleStatus", orderNum = 3 )
    private StatusDTO lifeCycleStatus;

    /**
     * The plugin.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "plugin", title = "3000184x4", type = "select", orderNum = 4 )
    @UIColumn( data = "plugin", filter = "text", renderer = "text", title = "3000184x4", name = UI_COLUMN_NAME_PLUGIN, type = "select", orderNum = 4 )
    private String plugin;

    /**
     * The settings.
     */
    private Map< String, String > settings;

    /**
     * The custom attributes DTO.
     */
    private List< CustomAttributeDTO > customAttributesDTO;

    /**
     * the project selection id
     */
    private String projectSelection;

    @UIColumn( data = "projectName", name = "projectName", filter = "text", url = "view/data/project/{projectId}", renderer = "link", title = "3000166x4", orderNum = 5 )
    private String projectName;

    private String projectId;

    private String config;

    /**
     * The Constant UI_COLUMN_NAME_PLUGIN.
     */
    public static final String UI_COLUMN_NAME_PLUGIN = "plugin";

    /**
     * The Constant UI_COLUMN_NAME_CONFIG.
     */
    public static final String UI_COLUMN_NAME_CONFIG = "config";

    /**
     * Instantiates a new data object image DTO.
     */
    public DataObjectDashboardDTO() {
    }

    /**
     * {@inheritDoc}
     */
    public DataObjectDashboardEntity prepareEntity( String userId ) {
        DataObjectDashboardEntity entity = new DataObjectDashboardEntity();
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        entity.setCreatedOn( now );
        entity.setModifiedOn( now );
        entity.setName( getName() );
        entity.setTypeId( getTypeId() );
        entity.setDescription( getDescription() );
        entity.setPlugin( getPlugin() );
        entity.setSettings( ByteUtil.convertStringToByte( JsonUtils.toJson( getSettings() ) ) );
        entity.setCustomAttributes( prepareCustomAttributes( entity, getCustomAttributes(), getCustomAttributesDTO() ) );

        // set Status From lifeCycle
        entity.setLifeCycleStatus( ( getLifeCycleStatus() != null ) ? getLifeCycleStatus().getId() : null );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        entity.setOwner( userEntity );

        if ( StringUtils.isNotBlank( getProjectSelection() ) ) {
            SelectionEntity selection = new SelectionEntity();
            selection.setId( UUID.fromString( getProjectSelection() ) );
            entity.setSelection( selection );
        }
        entity.setJobId( getJobId() );
        return entity;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.model.DataObjectDTO#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( getPlugin() == null ) ? 0 : getPlugin().hashCode() );
        result = prime * result + ( ( getSettings() == null ) ? 0 : getSettings().hashCode() );
        return result;
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
     * Gets the life cycle status.
     *
     * @return the life cycle status
     */
    public StatusDTO getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    /**
     * Sets the life cycle status.
     *
     * @param lifeCycleStatus
     *         the new life cycle status
     */
    public void setLifeCycleStatus( StatusDTO lifeCycleStatus ) {
        this.lifeCycleStatus = lifeCycleStatus;
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
     * Gets the plugin.
     *
     * @return the plugin
     */
    public String getPlugin() {
        return plugin;
    }

    /**
     * Sets the plugin.
     *
     * @param plugin
     *         the new plugin
     */
    public void setPlugin( String plugin ) {
        this.plugin = plugin;
    }

    /**
     * Gets the settings.
     *
     * @return the settings
     */
    public Map< String, String > getSettings() {
        return settings;
    }

    /**
     * Sets the settings.
     *
     * @param settings
     *         the settings
     */
    public void setSettings( Map< String, String > settings ) {
        this.settings = settings;
    }

    public String getProjectSelection() {
        return projectSelection;
    }

    public void setProjectSelection( String projectSelection ) {
        this.projectSelection = projectSelection;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName( String projectName ) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId( String projectId ) {
        this.projectId = projectId;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig( String config ) {
        this.config = config;
    }

}
