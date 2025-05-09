package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

/**
 * Database Entity Mapping Class for variant.
 *
 * @author Nosheen.Sharif
 */
@Entity
@Indexed( index = "VariantEntity" )
public class VariantEntity extends ContainerEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "29454761-0667-4629-ad16-2364fba53a5a" );

    /**
     * Constructor.
     */
    public VariantEntity() {
        super();
    }

    /**
     * Instantiates a new variant entity.
     *
     * @param composedId
     *         the composed id
     */
    public VariantEntity( VersionPrimaryKey composedId ) {
        super.setComposedId( composedId );
    }

}
