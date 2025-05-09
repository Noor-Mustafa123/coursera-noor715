package de.soco.software.simuspace.wizards.model;

import de.soco.software.simuspace.suscore.data.model.LoadCaseDTO;

public class VariantLoadcaseDTO extends LoadCaseDTO {

    private int value = 0;

    public int getValue() {
        return value;
    }

    public void setValue( int value ) {
        this.value = value;
    }

}
