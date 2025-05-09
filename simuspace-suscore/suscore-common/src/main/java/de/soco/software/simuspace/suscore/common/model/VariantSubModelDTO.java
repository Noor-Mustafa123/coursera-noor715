package de.soco.software.simuspace.suscore.common.model;

/**
 * The Class VariantSubModelDTO.
 *
 * @author noman arshad
 */
public class VariantSubModelDTO extends BmwCaeBenchSubModelDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1463664001190234354L;

    /**
     * The value.
     */
    private int value = 0;

    /**
     * Gets the value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( int value ) {
        this.value = value;
    }

}
