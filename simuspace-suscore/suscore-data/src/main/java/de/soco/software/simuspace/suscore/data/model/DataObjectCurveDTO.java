package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.DataObjectCurveEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectCurveDTO for Representing Graph.
 *
 * @author Nosheen.sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class DataObjectCurveDTO extends DataObjectValueDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1099274829657342916L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectCurveEntity ENTITY_CLASS = new DataObjectCurveEntity();

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
    private List< double[] > curve;

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
    public List< double[] > getCurve() {
        return curve;
    }

    /**
     * Sets the curve.
     *
     * @param curve
     *         the curve to set
     */
    public void setCurve( List< double[] > curve ) {
        this.curve = curve;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.model.DataObjectValueDTO#prepareEntity(java.lang.String)
     */
    @Override
    public DataObjectCurveEntity prepareEntity( String userId ) {
        DataObjectCurveEntity entity = new DataObjectCurveEntity();
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        entity.setCreatedOn( now );
        entity.setModifiedOn( now );
        entity.setName( getName() );
        entity.setValue( getValue() );
        entity.setTypeId( getTypeId() );
        entity.setDescription( getDescription() );

        entity.setCustomAttributes( prepareCustomAttributes( entity, getCustomAttributes(), getCustomAttributesDTO() ) );

        // set Status From lifeCycle
        entity.setLifeCycleStatus( ( getLifeCycleStatus() != null ) ? getLifeCycleStatus().getId() : null );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        entity.setOwner( userEntity );

        if ( getFile() != null ) {
            DocumentEntity documentEntity = prepareDocumentEntity();
            entity.setFile( documentEntity );
            entity.setSize( documentEntity.getSize() );
        } else {
            entity.setSize( null );
        }
        entity.setJobId( getJobId() );
        return entity;
    }

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
        DataObjectCurveDTO other = ( DataObjectCurveDTO ) obj;

        if ( xDimension == null ) {
            if ( other.xDimension != null ) {
                return false;
            }
        } else if ( !xDimension.equalsIgnoreCase( other.xDimension ) ) {
            return false;
        }
        if ( yDimension == null ) {
            if ( other.yDimension != null ) {
                return false;
            }
        } else if ( !yDimension.equalsIgnoreCase( other.yDimension ) ) {
            return false;
        }
        return true;
    }

}
