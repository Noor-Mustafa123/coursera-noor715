package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class CustomVariableDTO.
 *
 * @author Ali Haider
 */
public class CustomVariableDTO {

    /**
     * The id.
     */
    private UUID id;

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
    @UIColumn( data = "value", name = "value", filter = "", renderer = "text", title = "3000156x4", isSortable = false, orderNum = 4 )
    private String value;

    /**
     * The workflow id.
     */
    private UUID workflowId;

    /**
     * The created on.
     */
    private Date createdOn;

    /**
     * The Element name.
     */
    private String elementName;

    /**
     * The Field name.
     */
    private String fieldName;

    /**
     * Instantiates a new design variable DTO.
     */
    public CustomVariableDTO() {
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
     * @param value
     *         the value
     * @param workflowId
     *         the workflow id
     */
    public CustomVariableDTO( String label, String name, String type, String value, UUID workflowId ) {
        this.label = label;
        this.name = name;
        this.type = type;
        this.value = value;
        this.workflowId = workflowId;
    }

    /**
     * Gets the workflow id.
     *
     * @return the workflow id
     */
    @JsonIgnore
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
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the value
     */
    public void setValue( String value ) {
        this.value = value;
    }

    /**
     * Gets the created on.
     *
     * @return the created on
     */
    @JsonIgnore
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
    @JsonIgnore
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
        return "CustomVariableDTO [id=" + id + ", name=" + name + ", label=" + label + ", type=" + type + ", value=" + value
                + ", workflowId=" + workflowId + ", createdOn=" + createdOn + "]";
    }

    /**
     * Gets element name.
     *
     * @return the element name
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * Sets element name.
     *
     * @param elementName
     *         the element name
     */
    public void setElementName( String elementName ) {
        this.elementName = elementName;
    }

    /**
     * Gets field name.
     *
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets field name.
     *
     * @param fieldName
     *         the field name
     */
    public void setFieldName( String fieldName ) {
        this.fieldName = fieldName;
    }

}