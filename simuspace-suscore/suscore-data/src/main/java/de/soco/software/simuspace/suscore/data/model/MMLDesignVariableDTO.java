package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.NotNull;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class MMLDesignVariableDTO.
 *
 * @author noman arshad
 */
public class MMLDesignVariableDTO extends DesignVariableDTO {

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
     * The values.
     */
    @UIFormField( name = "level", title = "3000208x4", orderNum = 8 )
    @UIColumn( data = "level", name = "level", filter = "", renderer = "text", title = "3000208x4", isSortable = false, orderNum = 8 )
    private String level;

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
     * Gets the level.
     *
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets the level.
     *
     * @param level
     *         the new level
     */
    public void setLevel( String level ) {
        this.level = level;
    }

}
