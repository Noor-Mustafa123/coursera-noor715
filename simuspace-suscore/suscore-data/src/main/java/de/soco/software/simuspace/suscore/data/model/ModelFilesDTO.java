package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UserDTO;

public class ModelFilesDTO {

    /**
     * The name.
     */
    @UIColumn( data = "name", name = "name", filter = "", renderer = "text", title = "3000032x4", isSortable = false, orderNum = 1, width = 0 )
    private String name;

    /**
     * The type.
     */
    @UIColumn( data = "size", name = "size", filter = "", renderer = "text", title = "3000123x4", isSortable = false, orderNum = 2 )
    private String size;

    /**
     * The created on.
     */
    @UIColumn( data = "createdOn", filter = "", renderer = "date", title = "3000008x4", name = "createdOn", isSortable = false, orderNum = 12 )
    private Date createdOn;

    /**
     * The created on.
     */
    @UIColumn( data = "createdBy.userUid", filter = "", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", name = "createdBy.userUid", tooltip = "{createdBy.userName}", isSortable = false, orderNum = 13 )
    private UserDTO createdBy;

    /**
     * The updated on.
     */
    @UIColumn( data = "modifiedOn", filter = "", renderer = "date", title = "3000053x4", name = "modifiedOn", isSortable = false, orderNum = 14 )
    private Date modifiedOn;

    /**
     * The created on.
     */
    @UIColumn( data = "modifiedBy.userUid", filter = "", url = "system/user/{modifiedBy.id}", renderer = "link", title = "3000065x4", name = "modifiedBy.userUid", tooltip = "{modifiedBy.userName}", isSortable = false, orderNum = 15 )
    private UserDTO modifiedBy;

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize( String size ) {
        this.size = size;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy( UserDTO createdBy ) {
        this.createdBy = createdBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn( Date modifiedOn ) {
        this.modifiedOn = modifiedOn;
    }

    public UserDTO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy( UserDTO modifiedBy ) {
        this.modifiedBy = modifiedBy;
    }

}
