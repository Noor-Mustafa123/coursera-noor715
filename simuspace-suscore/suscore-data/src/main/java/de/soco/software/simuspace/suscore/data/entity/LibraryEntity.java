package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

/**
 * Database Entity Mapping Class for variant.
 *
 * @author M.Nasir.Farooq
 */
@Entity
@Indexed( index = "LibraryEntity" )
public class LibraryEntity extends ContainerEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 11530919410822640L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "8f2eef5a-c412-492d-88aa-234b33336108" );

    /**
     * Default constructor for LibraryEntity.
     */
    public LibraryEntity() {
        super();

    }

    /**
     * Instantiates a new library entity.
     *
     * @param composedId
     *         the composed id
     */
    public LibraryEntity( VersionPrimaryKey composedId ) {
        super.setComposedId( composedId );
    }

}
