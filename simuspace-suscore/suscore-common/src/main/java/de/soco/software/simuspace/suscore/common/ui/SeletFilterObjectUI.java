package de.soco.software.simuspace.suscore.common.ui;

import java.util.List;

import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;

/**
 * Common class for creating Filter object and list options in json
 *
 * @author Noman arshad
 */
public class SeletFilterObjectUI {

    /**
     * The filter type
     */
    private String type;

    /**
     * The list containing options or values for filter
     */
    private List< SelectOptionsUI > values;

    /**
     * @return the Filtertype
     */
    public String getType() {
        return type;
    }

    /**
     * @param the
     *         FilterType
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * @return the filter options List
     */
    public List< SelectOptionsUI > getValues() {
        return values;
    }

    /**
     * @param to
     *         set the filter options List
     */
    public void setValues( List< SelectOptionsUI > values ) {
        this.values = values;
    }

}
