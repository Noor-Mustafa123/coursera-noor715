package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

/**
 * The Class DataObjectPDFEntity.
 */
@Entity
@Indexed
public class DataObjectPDFEntity extends DataObjectFileEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "de72ccb3-b9c4-4bae-92ab-53d13f6ea8e0" );

    /**
     * Gets the serialversionuid.
     *
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
