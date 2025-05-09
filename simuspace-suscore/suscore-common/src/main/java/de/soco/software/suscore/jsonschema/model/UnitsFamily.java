package de.soco.software.suscore.jsonschema.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class UnitsFamily.
 *
 * @author Noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class UnitsFamily {

    /**
     * The units family.
     */
    private String unitsFamily;

    /**
     * The base unit.
     */
    private String baseUnit;

    /**
     * The base unit multiplier.
     */
    private String baseUnitMultiplier;

    /**
     * The units.
     */
    private List< UnitsList > units;

    /**
     * Instantiates a new units family.
     */
    public UnitsFamily() {
    }

    /**
     * Gets the units family.
     *
     * @return the units family
     */
    public String getUnitsFamily() {
        return unitsFamily;
    }

    /**
     * Sets the units family.
     *
     * @param unitsFamily
     *         the new units family
     */
    public void setUnitsFamily( String unitsFamily ) {
        this.unitsFamily = unitsFamily;
    }

    /**
     * Gets the base unit.
     *
     * @return the base unit
     */
    public String getBaseUnit() {
        return baseUnit;
    }

    /**
     * Sets the base unit.
     *
     * @param baseUnit
     *         the new base unit
     */
    public void setBaseUnit( String baseUnit ) {
        this.baseUnit = baseUnit;
    }

    /**
     * Gets the base unit multiplier.
     *
     * @return the base unit multiplier
     */
    public String getBaseUnitMultiplier() {
        return baseUnitMultiplier;
    }

    /**
     * Sets the base unit multiplier.
     *
     * @param baseUnitMultiplier
     *         the new base unit multiplier
     */
    public void setBaseUnitMultiplier( String baseUnitMultiplier ) {
        this.baseUnitMultiplier = baseUnitMultiplier;
    }

    /**
     * Gets the units.
     *
     * @return the units
     */
    public List< UnitsList > getUnits() {
        return units;
    }

    /**
     * Sets the units.
     *
     * @param units
     *         the new units
     */
    public void setUnits( List< UnitsList > units ) {
        this.units = units;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UnitsFamily [unitsFamily=" + unitsFamily + ", baseUnit=" + baseUnit + ", baseUnitMultiplier=" + baseUnitMultiplier
                + ", units=" + units + "]";
    }

}