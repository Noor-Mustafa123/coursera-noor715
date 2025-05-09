package de.soco.software.simuspace.suscore.permissions.constants;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.permissions.model.PermissionDTO;

/**
 * The Class is responsible to provide columns for manage permission form.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class PermissionManageForm {

    /**
     * The index pattern.
     */
    public static final String INDEX_PATTERN = "{index}";

    /**
     * The Constant PERMISSION_NAME_PATTERN.
     */
    public static final String PERMISSION_NAME_PATTERN = "{permission}";

    /**
     * The Constant NAME.
     */
    public static final String NAME = "name";

    private static final String LEVEL = "level";

    private UUID id;

    /**
     * The name.
     */
    @UIFormField( name = "name", title = "3000060x4" )
    @UIColumn( data = "name", name = "name", filter = "", renderer = "text", title = "3000060x4", isSortable = false, width = 0 )
    private String name;

    /**
     * The object type.
     */
    @UIFormField( name = "currentLevel", title = "3000102x4" )
    @UIColumn( data = "currentLevel", name = LEVEL, filter = "text", renderer = "menu-picker", options = LEVEL, title = "3000102x4", manage = "manage" )
    private List< String > level;

    private String currentLevel;

    private boolean manage;

    /**
     * The permissions.
     */
    @UIFormField( name = "permissionDTOs.{index}.value", title = "3000043x4" )
    @UIColumn( data = "permissionDTOs.{index}.value", name = "permissionDTOs-{permission}", filter = "", renderer = "checkbox", title = "3000043x4", manage = "permissionDTOs.{index}.manage" )
    private List< PermissionDTO > permissionDTOs;

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel( String currentLevel ) {
        this.currentLevel = currentLevel;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public List< String > getLevel() {
        return level;
    }

    public void setLevel( List< String > level ) {
        this.level = level;
    }

    public List< PermissionDTO > getPermissionDTOs() {
        return permissionDTOs;
    }

    public void setPermissionDTOs( List< PermissionDTO > permissionDTOs ) {
        this.permissionDTOs = permissionDTOs;
    }

    public UUID getId() {
        return id;
    }

    public void setId( UUID id ) {
        this.id = id;
    }

    public boolean isManage() {
        return manage;
    }

    public void setManage( boolean manage ) {
        this.manage = manage;
    }

}
