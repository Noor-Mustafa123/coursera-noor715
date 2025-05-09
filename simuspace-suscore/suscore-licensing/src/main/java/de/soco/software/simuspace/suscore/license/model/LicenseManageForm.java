package de.soco.software.simuspace.suscore.license.model;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class is responsible to provide columns for manage license form.
 *
 * @author M.Nasir.Farooq
 */
public class LicenseManageForm {

    /**
     * The index pattern.
     */
    public static final String INDEX_PATTERN = "{index}";

    /**
     * The module name pattern.
     */
    public static final String MODULE_NAME_PATTERN = "{module}";

    /**
     * The user name.
     */
    @UIFormField( name = "user.userUid", title = "3000054x4" )
    @UIColumn( data = "user.userUid", name = "uid", filter = "text", url = "system/user/{user.id}", renderer = "link", title = "3000054x4" )
    private String uid;

    /**
     * The type.
     */
    @UIFormField( name = "user.type", title = "3000046x4" )
    @UIColumn( data = "user.type", name = "userType", filter = "", renderer = "checkbox", title = "3000046x4", isSortable = false )
    private int type;

    /**
     * The module.
     */
    @UIFormField( name = "modules.{index}.value", title = "3000031x4" )
    @UIColumn( data = "modules.{index}.value", name = "modules-{module}", filter = "", renderer = "checkbox", title = "3000031x4", isSortable = false )
    private String module;

}
