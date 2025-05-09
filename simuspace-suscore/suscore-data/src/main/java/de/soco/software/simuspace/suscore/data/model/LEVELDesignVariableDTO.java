package de.soco.software.simuspace.suscore.data.model;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class LEVELDesignVariableDTO.
 *
 * @author Shahzeb Iqbal
 */
public class LEVELDesignVariableDTO extends DesignVariableDTO {

    /**
     * The level.
     */
    @UIFormField( name = "levels", title = "3000208x4", orderNum = 8 )
    @UIColumn( data = "levels", name = "levels", filter = "", renderer = "text", title = "3000208x4", isSortable = false, orderNum = 8 )
    private String levels;

    /**
     * Gets the level.
     *
     * @return the level
     */
    public String getLevels() {
        return levels;
    }

    /**
     * Sets the values.
     *
     * @param values
     *         the new values
     */
    public void setLevels( String levels ) {
        this.levels = levels;
    }

}
