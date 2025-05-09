package de.soco.software.simuspace.suscore.data.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class MonitorLicenseCurveDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class MonitorLicenseCurveDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1099274829657342916L;

    private String name;

    /**
     * The x units.
     */
    private String xUnit;

    /**
     * The y units.
     */
    private String yUnit;

    /**
     * The x quantity type.
     */
    private String xDimension;

    /**
     * The y quantity type.
     */
    private String yDimension;

    /**
     * The curve data.
     */
    private List< Object > curve;

    /**
     * Gets the x unit.
     *
     * @return the xUnit
     */
    public String getxUnit() {
        return xUnit;
    }

    /**
     * Sets the x unit.
     *
     * @param xUnit
     *         the xUnit to set
     */
    public void setxUnit( String xUnit ) {
        this.xUnit = xUnit;
    }

    /**
     * Gets the y unit.
     *
     * @return the yUnit
     */
    public String getyUnit() {
        return yUnit;
    }

    /**
     * Sets the y unit.
     *
     * @param yUnit
     *         the yUnit to set
     */
    public void setyUnit( String yUnit ) {
        this.yUnit = yUnit;
    }

    /**
     * Gets the x dimension.
     *
     * @return the xDimension
     */
    public String getxDimension() {
        return xDimension;
    }

    /**
     * Sets the x dimension.
     *
     * @param xDimension
     *         the xDimension to set
     */
    public void setxDimension( String xDimension ) {
        this.xDimension = xDimension;
    }

    /**
     * Gets the y dimension.
     *
     * @return the yDimension
     */
    public String getyDimension() {
        return yDimension;
    }

    /**
     * Sets the y dimension.
     *
     * @param yDimension
     *         the yDimension to set
     */
    public void setyDimension( String yDimension ) {
        this.yDimension = yDimension;
    }

    /**
     * Gets the curve.
     *
     * @return the curve
     */
    public List< Object > getCurve() {
        return curve;
    }

    /**
     * Sets the curve.
     *
     * @param curve
     *         the new curve
     */
    public void setCurve( List< Object > curve ) {
        this.curve = curve;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.model.DataObjectValueDTO#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( curve == null ) ? 0 : curve.hashCode() );
        result = prime * result + ( ( xDimension == null ) ? 0 : xDimension.hashCode() );
        result = prime * result + ( ( xUnit == null ) ? 0 : xUnit.hashCode() );
        result = prime * result + ( ( yDimension == null ) ? 0 : yDimension.hashCode() );
        result = prime * result + ( ( yUnit == null ) ? 0 : yUnit.hashCode() );
        return result;
    }

    /**
     * Equals.
     *
     * @param obj
     *         the obj
     *
     * @return true, if successful
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.model.DataObjectValueDTO#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        MonitorLicenseCurveDTO other = ( MonitorLicenseCurveDTO ) obj;

        if ( xDimension == null ) {
            if ( other.xDimension != null ) {
                return false;
            }
        } else if ( !xDimension.equalsIgnoreCase( other.xDimension ) ) {
            return false;
        }
        if ( yDimension == null ) {
            return other.yDimension == null;
        } else {
            return yDimension.equalsIgnoreCase( other.yDimension );
        }
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name
     *         the name
     */
    public void setName( String name ) {
        this.name = name;
    }

}
