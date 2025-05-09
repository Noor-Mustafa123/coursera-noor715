package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DataObjectMovieEntity.
 */
@Getter
@Setter
@Entity
@Indexed
public class DataObjectMovieEntity extends DataObjectFileEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "77e21396-1db1-4f31-a0ea-5f6708ce5daf" );

    /**
     * The preview image.
     */
    @OneToOne
    private DocumentEntity previewImage;

    @OneToOne
    @JoinColumns( value = { @JoinColumn( name = "thumb_movie_id", referencedColumnName = "id" ) } )
    private DocumentEntity thumbnail;

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( thumbnail == null ) ? 0 : thumbnail.hashCode() );
        result = prime * result + ( ( previewImage == null ) ? 0 : previewImage.hashCode() );
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
        if ( !( obj instanceof DataObjectMovieEntity ) ) {
            return false;
        }
        DataObjectMovieEntity other = ( DataObjectMovieEntity ) obj;
        if ( thumbnail == null ) {
            if ( other.thumbnail != null ) {
                return false;
            }
        } else if ( !thumbnail.equals( other.thumbnail ) ) {
            return false;
        }
        if ( previewImage == null ) {
            if ( other.previewImage != null ) {
                return false;
            }
        } else if ( !previewImage.equals( other.previewImage ) ) {
            return false;
        }
        return true;
    }

}
