package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;

/**
 * The Class DataObjectFileEntity.
 */
@Getter
@Entity
@Indexed
public class DataObjectFileEntity extends DataObjectEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "8f2eef5a-c412-492d-88aa-234b33332020" );

    /**
     * The encoding.
     */
    @Column( name = "encoding" )
    private String encoding;

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( encoding == null ) ? 0 : encoding.hashCode() );
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
        DataObjectFileEntity other = ( DataObjectFileEntity ) obj;
        if ( encoding == null ) {
            if ( other.encoding != null ) {
                return false;
            }
        } else if ( !encoding.equals( other.encoding ) ) {
            return false;
        }

        return true;
    }

}
