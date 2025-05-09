package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class DesignVariableDTO.
 */
public class DesignVariableDTO {

    /**
     * The id.
     */
    protected UUID id;

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4", readonly = true, orderNum = 2 )
    @UIColumn( data = "name", name = "name", filter = "", renderer = "text", title = "3000032x4", isSortable = false, orderNum = 2, width = 0 )
    protected String name;

    /**
     * The label.
     */
    @UIColumn( data = "label", name = "label", filter = "", renderer = "text", title = "3000140x4", isSortable = false, orderNum = 1 )
    private String label;

    /**
     * The type.
     */
    @UIColumn( data = "type", name = "type", filter = "", renderer = "text", title = "3000051x4", isSortable = false, orderNum = 3 )
    private String type;

    /**
     * The nominal value.
     */
    @UIColumn( data = "nominalValue", name = "nominalValue", filter = "", renderer = "text", title = "3000134x4", isSortable = false, orderNum = 4 )
    private String nominalValue;

    /**
     * The workflow id.
     */
    private UUID workflowId;

    /**
     * The created on.
     */
    private Date createdOn;

    /**
     * The index.
     */
    private String index;

    /**
     * The algo type.
     */
    private String algoType;

    /**
     * Instantiates a new design variable DTO.
     */
    public DesignVariableDTO() {
    }

    /**
     * Instantiates a new design variable DTO.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param type
     *         the type
     * @param nominalValue
     *         the nominal value
     * @param workflowId
     *         the workflow id
     */
    public DesignVariableDTO( String label, String name, String type, String nominalValue, UUID workflowId ) {
        this.label = label;
        this.name = name;
        this.type = type;
        this.nominalValue = nominalValue;
        this.workflowId = workflowId;
    }

    /**
     * Instantiates a new design variable DTO.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param type
     *         the type
     * @param nominalValue
     *         the nominal value
     * @param index
     *         the index
     * @param workflowId
     *         the workflow id
     */
    public DesignVariableDTO( String label, String name, String type, String nominalValue, String index, UUID workflowId ) {
        this.label = label;
        this.name = name;
        this.type = type;
        this.nominalValue = nominalValue;
        this.workflowId = workflowId;
        this.index = index;
    }

    /**
     * Gets the workflow id.
     *
     * @return the workflow id
     */
    public UUID getWorkflowId() {
        return workflowId;
    }

    /**
     * Sets the workflow id.
     *
     * @param workflowId
     *         the new workflow id
     */
    public void setWorkflowId( UUID workflowId ) {
        this.workflowId = workflowId;
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
     * Gets the algo type.
     *
     * @return the algo type
     */
    public String getAlgoType() {
        return algoType;
    }

    /**
     * Sets the algo type.
     *
     * @param algoType
     *         the new algo type
     */
    public void setAlgoType( String algoType ) {
        this.algoType = algoType;
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
     * Gets the nomimal value.
     *
     * @return the nomimal value
     */
    public String getNominalValue() {
        return nominalValue;
    }

    /**
     * Sets the nomimal value.
     *
     * @param nominalValue
     *         the new nominal value
     */
    public void setNominalValue( String nominalValue ) {
        this.nominalValue = nominalValue;
    }

    /**
     * Gets the created on.
     *
     * @return the created on
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Gets the index.
     *
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * Sets the index.
     *
     * @param index
     *         the new index
     */
    public void setIndex( String index ) {
        this.index = index;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label
     *         the new label
     */
    public void setLabel( String label ) {
        this.label = label;
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
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "DesignVariableDTO [id=" + id + ", name=" + name + ", label=" + label + ", type=" + type + ", nominalValue=" + nominalValue
                + ", workflowId=" + workflowId + ", createdOn=" + createdOn + ", index=" + index + ", algoType=" + algoType + "]";
    }

}