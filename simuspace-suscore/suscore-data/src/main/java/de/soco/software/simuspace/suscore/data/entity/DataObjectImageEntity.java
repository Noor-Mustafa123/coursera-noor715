package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DataObjectImageEntity.
 */
@Getter
@Setter
@Entity
@Indexed
public class DataObjectImageEntity extends DataObjectFileEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "9c363bb4-f6fe-4cef-9e09-cc59aa595425" );

    /**
     * The file.
     */
    @OneToOne
    private DocumentEntity thumbNail;

    /**
     * Gets the serialversionuid.
     *
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
