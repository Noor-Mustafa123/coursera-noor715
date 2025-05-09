package de.soco.software.simuspace.suscore.object.model;

import de.soco.software.simuspace.suscore.data.entity.SuSEntity;

/**
 * The Class ParentChildDTO for storing the parents and children relation ships.
 */
public class ParentChildDTO {

    /**
     * The sus entity.
     */
    private SuSEntity susEntity;

    /**
     * The parent entity.
     */
    private SuSEntity parentEntity;

    /**
     * Gets the parent entity.
     *
     * @return the parent entity
     */
    public SuSEntity getParentEntity() {
        return parentEntity;
    }

    /**
     * Sets the parent entity.
     *
     * @param parentEntity
     *         the new parent entity
     */
    public void setParentEntity( SuSEntity parentEntity ) {
        this.parentEntity = parentEntity;
    }

    /**
     * Instantiates a new entity.
     *
     * @param susEntity
     *         the sus entity
     * @param parentEntity
     *         the parent entity
     */
    public ParentChildDTO( SuSEntity susEntity, SuSEntity parentEntity ) {
        super();
        this.setSusEntity( susEntity );
        this.parentEntity = parentEntity;
    }

    /**
     * Gets the sus entity.
     *
     * @return the sus entity
     */
    public SuSEntity getSusEntity() {
        return susEntity;
    }

    /**
     * Sets the sus entity.
     *
     * @param susEntity
     *         the new sus entity
     */
    public void setSusEntity( SuSEntity susEntity ) {
        this.susEntity = susEntity;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( susEntity == null ) ? 0 : susEntity.hashCode() );
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
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
        ParentChildDTO other = ( ParentChildDTO ) obj;
        if ( susEntity == null ) {
            if ( other.susEntity != null ) {
                return false;
            }
        } else if ( !susEntity.equals( other.susEntity ) ) {
            return false;
        }
        return true;
    }

}
