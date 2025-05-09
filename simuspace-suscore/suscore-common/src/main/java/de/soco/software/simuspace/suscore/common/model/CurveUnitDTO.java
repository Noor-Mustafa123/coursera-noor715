package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class CurveUnitDTO.
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class CurveUnitDTO {

    /**
     * The xunit.
     */
    private String xunit;

    /**
     * The yunit.
     */
    private String yunit;

    /**
     * Gets the xunit.
     *
     * @return the xunit
     */
    public String getXunit() {
        return xunit;
    }

    /**
     * Sets the xunit.
     *
     * @param xunit
     *         the new xunit
     */
    public void setXunit( String xunit ) {
        this.xunit = xunit;
    }

    /**
     * Gets the yunit.
     *
     * @return the yunit
     */
    public String getYunit() {
        return yunit;
    }

    /**
     * Sets the yunit.
     *
     * @param yunit
     *         the new yunit
     */
    public void setYunit( String yunit ) {
        this.yunit = yunit;
    }

}
