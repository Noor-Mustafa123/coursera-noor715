package de.soco.software.suscore.jsonschema.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class UnitsList.
 *
 * @author Noman Arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class UnitsList {

    /**
     * The name.
     */
    private String name;

    /**
     * The label.
     */
    private String label;

    /**
     * The scale.
     */
    private String scale;

    /**
     * The multiplier.
     */
    private String multiplier;

    /**
     * Instantiates a new units list.
     */
    public UnitsList() {
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
     * Gets the scale.
     *
     * @return the scale
     */
    public String getScale() {
        return scale;
    }

    /**
     * Sets the scale.
     *
     * @param scale
     *         the new scale
     */
    public void setScale( String scale ) {
        this.scale = scale;
    }

    /**
     * Gets the multiplier.
     *
     * @return the multiplier
     */
    public String getMultiplier() {
        return multiplier;
    }

    /**
     * Sets the multiplier.
     *
     * @param multiplier
     *         the new multiplier
     */
    public void setMultiplier( String multiplier ) {
        this.multiplier = multiplier;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UnitsList [name=" + name + ", scale=" + scale + ", multiplier=" + multiplier + "]";
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

}
