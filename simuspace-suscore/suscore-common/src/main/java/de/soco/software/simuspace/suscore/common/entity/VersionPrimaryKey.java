package de.soco.software.simuspace.suscore.common.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This Class is Responsible for composite Primary Key of any object implementing versioning . Composite Primary key will consist of uuid
 * and version Id.
 *
 * @author Nosheen.Sharif
 */
@Getter
@Setter
@ToString
@Embeddable
public class VersionPrimaryKey implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The id of object .Database primary key
     */
    @Column( name = "id" )
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The version id of object.Database primary key
     */
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "version_id" )
    private int versionId;

    /**
     * Instantiates a new workflow Primary Key.
     */
    public VersionPrimaryKey() {
        super();
    }

    /**
     * Instantiates a new workflow primary key.
     *
     * @param id
     *         the id
     * @param versionId
     *         the version id
     */
    public VersionPrimaryKey( UUID id, int versionId ) {
        super();
        this.id = id;
        this.versionId = versionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object object ) {
        if ( this == object ) {
            return true;
        }
        if ( !( object instanceof VersionPrimaryKey ) ) {
            return false;
        }
        final VersionPrimaryKey otherObject = ( VersionPrimaryKey ) object;
        return Objects.equals( id, otherObject.id ) && Objects.equals( versionId, otherObject.versionId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {

        return id.hashCode() * versionId;
    }

}