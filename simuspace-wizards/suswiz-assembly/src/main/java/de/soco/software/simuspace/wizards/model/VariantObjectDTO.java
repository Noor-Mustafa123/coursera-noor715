package de.soco.software.simuspace.wizards.model;

import java.util.List;

import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;

/**
 * The Class VariantObjectDTO.
 *
 * @author noman arshad
 */
public class VariantObjectDTO extends GenericDTO {

    /**
     *
     */
    private static final long serialVersionUID = -3252526319976749859L;

    /**
     * The loadcases.
     */
    private List< VariantLoadcaseDTO > loadcases;

    /**
     * Gets the loadcases.
     *
     * @return the loadcases
     */
    public List< VariantLoadcaseDTO > getLoadcases() {
        return loadcases;
    }

    /**
     * Sets the loadcases.
     *
     * @param loadcases
     *         the new loadcases
     */
    public void setLoadcases( List< VariantLoadcaseDTO > loadcases ) {
        this.loadcases = loadcases;
    }

}
