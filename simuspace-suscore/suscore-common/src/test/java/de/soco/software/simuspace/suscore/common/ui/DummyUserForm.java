package de.soco.software.simuspace.suscore.common.ui;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * This is dummy Class with annotation fields for testing of GUIUtil class
 *
 * @author Nosheen.Sharif
 */
public class DummyUserForm {

    /**
     * Dummy Constant Id field
     */
    @UIFormField( name = "id", title = "3000021x4", type = "text" )
    @UIColumn( data = "id", filter = "text", renderer = "hidden", title = "3000021x4", name = "id", isShow = false, type = "text" )
    private String id;

    /**
     * Dummy Constant name field
     */
    @UIFormField( name = "name", title = "3000032x4" )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", width = 0 )
    private String name;

    /**
     * Dummy Constant description field
     */
    @UIFormField( name = "description", type = "select", title = "3000011x4" )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", type = "select", title = "3000011x4" )
    private String description;

    /**
     * Dummy Constant status field
     */
    @UIFormField( name = "status", type = "select", title = "3000049x4" )
    @UIColumn( data = "status", name = "status", filter = "text", renderer = "text", type = "select", title = "3000049x4", isSortable = false )
    private String status;

    /**
     * Construtor
     */
    public DummyUserForm() {
        super();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *         the id to set
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *         the name to set
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *         the description to set
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *         the status to set
     */
    public void setStatus( String status ) {
        this.status = status;
    }

}
