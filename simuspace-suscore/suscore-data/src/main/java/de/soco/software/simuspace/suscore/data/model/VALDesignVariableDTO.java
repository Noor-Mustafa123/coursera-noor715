package de.soco.software.simuspace.suscore.data.model;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class ValDesignVariableDTO.
 *
 * @author noman arshad
 */
public class VALDesignVariableDTO extends DesignVariableDTO {

    /**
     * The values.
     */
    @UIFormField( name = "values", title = "3000138x4", orderNum = 8 )
    @UIColumn( data = "values", name = "values", filter = "", renderer = "text", title = "3000138x4", isSortable = false, orderNum = 8 )
    private String values;

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
