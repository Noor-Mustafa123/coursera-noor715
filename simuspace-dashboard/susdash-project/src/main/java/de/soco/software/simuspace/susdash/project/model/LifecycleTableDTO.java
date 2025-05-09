package de.soco.software.simuspace.susdash.project.model;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;

/**
 * The type Lifecycle table dto.
 */
public class LifecycleTableDTO {

    /**
     * The constant NAME_COLUMN.
     */
    public static final String NAME_COLUMN = "name";

    /**
     * The constant TYPE_COLUMN.
     */
    public static final String TYPE_COLUMN = "type";

    /**
     * The constant PATH_COLUMN.
     */
    public static final String PATH_COLUMN = "path";

    /**
     * The constant LIFECYCLE_COLUMN.
     */
    public static final String LIFECYCLE_COLUMN = "lifeCycleStatus";

    /**
     * The Id.
     */
    private UUID id;

    /**
     * The Name.
     */
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "link", url = "{url}", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The Type.
     */
    @UIColumn( data = "type", name = "type", filter = "text", renderer = "replaceText", options = "<i class='{icon}'></i> {type}", title = "3000051x4", orderNum = 2 )
    private String type;

    /**
     * The Life cycle status.
     */
    @UIColumn( data = "lifeCycleStatus.name", filter = "text", renderer = "text", title = "3000066x4", name = "lifeCycleStatus", orderNum = 3 )
    private StatusDTO lifeCycleStatus;

    /**
     * The Path.
     */
    @UIColumn( data = "path", filter = "text", renderer = "text", title = "3000214x4", name = "path", orderNum = 3 )
    private String path;

    /**
     * The Icon.
     */
    private String icon;

    /**
     * The Url.
     */
    private String url;

    /**
     * Gets id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets id.
     *
     * @param id
     *         the id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name
     *         the name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type
     *         the type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets life cycle status.
     *
     * @return the life cycle status
     */
    public StatusDTO getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    /**
     * Sets life cycle status.
     *
     * @param lifeCycleStatus
     *         the life cycle status
     */
    public void setLifeCycleStatus( StatusDTO lifeCycleStatus ) {
        this.lifeCycleStatus = lifeCycleStatus;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets icon.
     *
     * @param icon
     *         the icon
     */
    public void setIcon( String icon ) {
        this.icon = icon;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url
     *         the url
     */
    public void setUrl( String url ) {
        this.url = url;
    }

    /**
     * Sets path.
     *
     * @param path
     *         the path
     */
    public void setPath( String path ) {
        this.path = path;
    }

    /**
     * Gets column value.
     *
     * @param columnName
     *         the column name
     *
     * @return the column value
     */
    public String getColumnValue( String columnName ) {
        if ( LIFECYCLE_COLUMN.equals( columnName ) ) {
            return getLifeCycleStatus().getName();
        } else if ( NAME_COLUMN.equals( columnName ) ) {
            return getName();
        } else if ( TYPE_COLUMN.equals( columnName ) ) {
            return getType();
        } else if ( PATH_COLUMN.equals( columnName ) ) {
            return getPath();
        }
        return null;
    }

}
