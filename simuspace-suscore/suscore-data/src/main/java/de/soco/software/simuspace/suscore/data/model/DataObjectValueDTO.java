package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.DataObjectValueEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectValueDTO.
 */
public class DataObjectValueDTO extends DataObjectDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 9031048308692351296L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectValueEntity ENTITY_CLASS = new DataObjectValueEntity();

    /**
     * The value.
     */
    private String value;

    /**
     * The dimension.
     */
    private String dimension;

    /**
     * The unit.
     */
    private String unit;

    /**
     * Prepare data object value entity.
     *
     * @param userId
     *         the user id
     *
     * @return the data object value entity
     */
    @Override
    public DataObjectValueEntity prepareEntity( String userId ) {
        DataObjectValueEntity entity = new DataObjectValueEntity();
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

    /**
     * Prepare data object value DTO.
     *
     * @param entity
     *         the entity
     *
     * @return the data object value DTO
     */
    public DataObjectValueDTO prepareDataObjectValueDTO( DataObjectValueEntity entity ) {
        DataObjectValueDTO dto = new DataObjectValueDTO();
        dto.setValue( entity.getValue() );
        return dto;

    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( String value ) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( !super.equals( obj ) ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        DataObjectValueDTO other = ( DataObjectValueDTO ) obj;
        if ( value == null ) {
            if ( other.value != null ) {
                return false;
            }
        } else if ( !value.equals( other.value ) ) {
            return false;
        }
        return true;
    }

    /**
     * Gets the dimension.
     *
     * @return the dimension
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * Sets the dimension.
     *
     * @param dimension
     *         the new dimension
     */
    public void setDimension( String dimension ) {
        this.dimension = dimension;
    }

    /**
     * Gets the unit.
     *
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit.
     *
     * @param unit
     *         the new unit
     */
    public void setUnit( String unit ) {
        this.unit = unit;
    }

}
