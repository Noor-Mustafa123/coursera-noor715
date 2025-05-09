package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class ObjectiveVariableDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ObjectiveVariableDTO {

    /**
     * The id.
     */
    private UUID id;

    /**
     * The label.
     */
    @UIColumn( data = "label", name = "label", filter = "", renderer = "text", title = "3000140x4", isSortable = false, orderNum = 1 )
    private String label;

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "name", name = "name", filter = "", renderer = "text", title = "3000032x4", isSortable = false, orderNum = 2, width = 0 )
    private String name;

    /**
     * The nominal value.
     */
    @UIColumn( data = "nominalValue", name = "nominalValue", filter = "", renderer = "text", title = "3000134x4", isSortable = false, orderNum = 3 )
    private String nominalValue;

    /**
     * The goal.
     */
    @UIFormField( name = "goal", title = "3000141x4", orderNum = 1 )
    @UIColumn( data = "goal", name = "goal", filter = "", renderer = "text", title = "3000141x4", isSortable = false, orderNum = 4 )
    private String goal;

    /**
     * The options.
     */
    @UIFormField( name = "options", title = "3000142x4", orderNum = 2 )
    @UIColumn( data = "options", name = "options", filter = "", renderer = "text", title = "3000142x4", isSortable = false, orderNum = 5 )
    private String options;

    /**
     * The workflow id.
     */
    private UUID workflowId;

    /**
     * The created on.
     */
    private Date createdOn;

    /**
     * Instantiates a new objective variable DTO.
     */
    public ObjectiveVariableDTO() {
    }

    /**
     * Instantiates a new design variable DTO.
     *
     * @param id
     *         the id
     * @param label
     *         the label
     * @param name
     *         the name
     * @param nominalValue
     *         the nominal value
     * @param workflowId
     *         the workflow id
     */
    public ObjectiveVariableDTO( UUID id, String label, String name, String nominalValue, UUID workflowId ) {
        super();
        this.id = id;
        this.label = label;
        this.name = name;
        this.nominalValue = nominalValue;
        this.workflowId = workflowId;
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
     * Gets the nominal value.
     *
     * @return the nominal value
     */
    public String getNominalValue() {
        return nominalValue;
    }

    /**
     * Sets the nominal value.
     *
     * @param nominalValue
     *         the new nominal value
     */
    public void setNominalValue( String nominalValue ) {
        this.nominalValue = nominalValue;
    }

    /**
     * Gets the goal.
     *
     * @return the goal
     */
    public String getGoal() {
        return goal;
    }

    /**
     * Sets the goal.
     *
     * @param goal
     *         the new goal
     */
    public void setGoal( String goal ) {
        this.goal = goal;
    }

    /**
     * Gets the options.
     *
     * @return the options
     */
    public String getOptions() {
        return options;
    }

    /**
     * Sets the options.
     *
     * @param options
     *         the new options
     */
    public void setOptions( String options ) {
        this.options = options;
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

    @Override
    public String toString() {
        return "ObjectiveVariableDTO [id=" + id + ", label=" + label + ", name=" + name + ", nominalValue=" + nominalValue + ", goal="
                + goal + ", options=" + options + ", workflowId=" + workflowId + ", createdOn=" + createdOn + "]";
    }

}
