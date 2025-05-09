package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.NotNull;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class DefaultDesignVariableDTO.
 *
 * @author noman arshad
 */
public class DefaultDesignVariableDTO extends DesignVariableDTO {

    /**
     * The min value.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "min", title = "3000135x4", orderNum = 5 )
    @UIColumn( data = "min", name = "min", filter = "", renderer = "text", title = "3000135x4", isSortable = false, orderNum = 5 )
    private String min;

    /**
     * The max value.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "max", title = "3000136x4", orderNum = 6 )
    @UIColumn( data = "max", name = "max", filter = "", renderer = "text", title = "3000136x4", isSortable = false, orderNum = 6 )
    private String max;

    /**
     * The step.
     */
    @UIFormField( name = "step", title = "3000137x4", orderNum = 7 )
    @UIColumn( data = "step", name = "step", filter = "", renderer = "text", title = "3000137x4", isSortable = false, orderNum = 7 )
    private String step;

    /**
     * The values.
     */
    @UIFormField( name = "values", title = "3000138x4", orderNum = 8 )
    @UIColumn( data = "values", name = "values", filter = "", renderer = "text", title = "3000138x4", isSortable = false, orderNum = 8 )
    private String values;

    /**
     * The optional.
     */
    @UIFormField( name = "optional", title = "3000139x4", orderNum = 9 )
    @UIColumn( data = "optional", name = "optional", filter = "", renderer = "text", title = "3000139x4", isSortable = false, orderNum = 9 )
    private String optional;

    /**
     * Gets the min.
     *
     * @return the min
     */
    public String getMin() {
        return min;
    }

    /**
     * Sets the min.
     *
     * @param min
     *         the new min
     */
    public void setMin( String min ) {
        this.min = min;
    }

    /**
     * Gets the max.
     *
     * @return the max
     */
    public String getMax() {
        return max;
    }

    /**
     * Sets the max.
     *
     * @param max
     *         the new max
     */
    public void setMax( String max ) {
        this.max = max;
    }

    /**
     * Gets the step.
     *
     * @return the step
     */
    public String getStep() {
        return step;
    }

    /**
     * Sets the step.
     *
     * @param step
     *         the new step
     */
    public void setStep( String step ) {
        this.step = step;
    }

    /**
     * Gets the optional.
     *
     * @return the optional
     */
    public String getOptional() {
        return optional;
    }

    /**
     * Sets the optional.
     *
     * @param optional
     *         the new optional
     */
    public void setOptional( String optional ) {
        this.optional = optional;
    }

    /**
     * Gets the values.
     *
     * @return the values
     */
    public String getValues() {
        return values;
    }

    /**
     * Sets the values.
     *
     * @param values
     *         the new values
     */
    public void setValues( String values ) {
        this.values = values;
    }

}
