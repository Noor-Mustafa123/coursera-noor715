package de.soco.software.simuspace.suscore.permissions.model;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class is responsible to provide columns for manage permission form for Object.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class ManageObjectDTO {

    /**
     * The Constant NAME.
     */
    public static final String NAME = "name";

    /**
     * The Constant TYPE.
     */
    public static final String TYPE = "type";

    /**
     * The Constant LEVEL.
     */
    public static final String LEVEL_LITERAL = "level";

    /**
     * The name.
     */
    @UIFormField( name = "name", title = "3000032x4" )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", isSortable = false, width = 0 )
    private String objectName;

    /**
     * The object type.
     */
    @UIFormField( name = "type", title = "3000051x4" )
    @UIColumn( data = "type", name = "type", filter = "", renderer = "text", title = "3000051x4" )
    private String objectType;

    /**
     * The object type.
     */
    @UIFormField( name = "currentLevel", title = "3000102x4" )
    @UIColumn( data = "currentLevel", name = LEVEL_LITERAL, filter = "", renderer = "menu-picker", options = LEVEL_LITERAL, title = "3000102x4", manage = "manage" )
    private List< String > level;

    /**
     * The current level.
     */
    private String currentLevel;

    /**
     * The manage.
     */
    private boolean manage;

    /**
     * The permissions.
     */
    @UIFormField( name = "permissionDTOs.{index}.value", title = "3000043x4" )
    @UIColumn( data = "permissionDTOs.{index}.value", name = "permissionDTOs-{permission}", filter = "", renderer = "checkbox", title = "3000043x4", isSortable = false, manage = "permissionDTOs.{index}.manage" )
    private List< PermissionDTO > permissionDTOs;

    /**
     * The sid id.
     */
    private UUID sidId;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return objectName;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.objectName = name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return objectType;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.objectType = type;
    }

    /**
     * Gets the permission DT os.
     *
     * @return the permission DT os
     */
    public List< PermissionDTO > getPermissionDTOs() {
        return permissionDTOs;
    }

    /**
     * Sets the permission DT os.
     *
     * @param permissionDTOs
     *         the new permission DT os
     */
    public void setPermissionDTOs( List< PermissionDTO > permissionDTOs ) {
        this.permissionDTOs = permissionDTOs;
    }

    /**
     * Gets the sid id.
     *
     * @return the sid id
     */
    public UUID getSidId() {
        return sidId;
    }

    /**
     * Sets the sid id.
     *
     * @param sidId
     *         the new sid id
     */
    public void setSidId( UUID sidId ) {
        this.sidId = sidId;
    }

    public List< String > getLevel() {
        return level;
    }

    public void setLevel( List< String > level ) {
        this.level = level;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel( String currentLevel ) {
        this.currentLevel = currentLevel;
    }

    public boolean isManage() {
        return manage;
    }

    public void setManage( boolean manage ) {
        this.manage = manage;
    }

}
